package rafasconquest.client.cloth;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.content.Sail;
import rafasconquest.content.SailBlock;

/**
 * Draws sails: the top corner block of each sheet draws the whole sheet ({@link SailShape}) once a frame, one canvas
 * stretched over it, each square tinted by its block's dye; the other blocks draw nothing. How full the sail is comes
 * from the wind on the ship's bow: full from astern, slack and flogging head to wind.
 */
public class SailRenderer implements BlockEntityRenderer<Sail> {
    private static final ResourceLocation CANVAS = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "textures/block/sail.png");
    private static final float HALF_THICK = 1 / 64F;
    /** The furthest a sheet reaches from its corner block along a row or column. */
    private static final int MOST = 32;
    // Render thread only: the sheet's grid, kept between frames so drawing makes no garbage.
    private static float[] px = new float[0], py = px, pz = px, nx = px, ny = px, nz = px;
    private static final double[] P = new double[3];
    private static final double[] WIND = new double[3];
    private static final Matrix4d FRAME = new Matrix4d();
    private static final Matrix4d SCRATCH = new Matrix4d();
    private static final Vector3d CORNER = new Vector3d();
    private static final Vector3d BOW = new Vector3d();

    public SailRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(Sail sail, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        BlockState state = sail.getBlockState();
        Level level = sail.getLevel();
        if (level == null || !(state.getBlock() instanceof SailBlock)) {
            return;
        }
        Direction facing = state.getValue(SailBlock.FACING);
        measure(sail, level, facing);
        // shortcut: the corner block draws all, so a sheet goes when the corner's chunk section is out of view.
        if (sail.inner) {
            return;
        }
        int w = sail.right + 1;
        int h = sail.down + 1;
        Direction across = facing.getClockWise();
        int ax = across.getStepX(), az = across.getStepZ(), fx = facing.getStepX(), fz = facing.getStepZ();

        // The sheet's top corner in the world (a Sable ship's turn is in the pose), and its bow.
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();
        FRAME.identity().translate(cam.x, cam.y, cam.z).rotate(new Quaternionf(camera.rotation()))
                .mul(SCRATCH.set(RenderSystem.getModelViewMatrix())).mul(SCRATCH.set(pose.last().pose()));
        FRAME.transformPosition(CORNER.set(0.5 - ax * 0.5, 1, 0.5 - az * 0.5));
        FRAME.transformDirection(BOW.set(fx, 0, fz));
        Wind.at(level, CORNER.x, CORNER.y, CORNER.z, 1, 1, WIND);
        double speed = Math.hypot(WIND[0], WIND[2]);
        double flat = Math.max(1e-6, Math.hypot(BOW.x, BOW.z));
        double along = speed < 1e-6 ? 1 : (WIND[0] * BOW.x + WIND[2] * BOW.z) / (speed * flat);
        double windy = Math.min(1, speed / 20);
        double caught = smoothstep(-0.6, 0.2, along);
        SailShape shape = new SailShape(w, h, (0.5 + 0.5 * windy) * (0.2 + 0.8 * caught),
                0.15 + 0.15 * windy + 0.7 * (1 - caught) * (0.4 + 0.6 * windy),
                (level.getGameTime() + partialTick) / 20.0);

        // Squares per block side, fewer as it gets further.
        double far = CORNER.distance(cam.x, cam.y, cam.z);
        int k = far < 24 ? 4 : far < 64 ? 2 : 1;
        int gw = w * k + 1;
        int gh = h * k + 1;
        int n = gw * gh;
        if (px.length < n) {
            px = new float[n]; py = new float[n]; pz = new float[n];
            nx = new float[n]; ny = new float[n]; nz = new float[n];
        }
        // The grid in the sheet's own frame (across, up, forward) first.
        for (int j = 0; j < gh; j++) {
            for (int i = 0; i < gw; i++) {
                shape.point((double) i / k, (double) j / k, P);
                int at = j * gw + i;
                px[at] = (float) P[0];
                py[at] = (float) P[1];
                pz[at] = (float) P[2];
            }
        }
        // Normals from the neighbours (down × across faces forward), then both into the block's frame.
        for (int j = 0; j < gh; j++) {
            for (int i = 0; i < gw; i++) {
                int at = j * gw + i;
                int l = j * gw + Math.max(i - 1, 0), r = j * gw + Math.min(i + 1, gw - 1);
                int u = Math.max(j - 1, 0) * gw + i, d = Math.min(j + 1, gh - 1) * gw + i;
                float aX = px[r] - px[l], aY = py[r] - py[l], aZ = pz[r] - pz[l];
                float bX = px[d] - px[u], bY = py[d] - py[u], bZ = pz[d] - pz[u];
                float cX = bY * aZ - bZ * aY, cY = bZ * aX - bX * aZ, cZ = bX * aY - bY * aX;
                float len = (float) Math.sqrt(cX * cX + cY * cY + cZ * cZ);
                nx[at] = (ax * cX + fx * cZ) / len;
                ny[at] = cY / len;
                nz[at] = (az * cX + fz * cZ) / len;
            }
        }
        for (int at = 0; at < n; at++) {
            float off = px[at] - 0.5F;
            float fwd = pz[at];
            px[at] = 0.5F + ax * off + fx * fwd;
            py[at] = 1 + py[at];
            pz[at] = 0.5F + az * off + fz * fwd;
        }

        VertexConsumer vc = buffers.getBuffer(RenderType.entityCutoutNoCull(CANVAS));
        PoseStack.Pose last = pose.last();
        float du = 1F / (w * k), dv = 1F / (h * k);
        // A front and a back face a hair apart, each lit from its own side; each block's square in its dye.
        for (int side = 1; side >= -1; side -= 2) {
            float off = HALF_THICK * side;
            for (int j = 0; j < gh - 1; j++) {
                for (int i = 0; i < gw - 1; i++) {
                    int argb = sail.colours[(j / k) * w + i / k];
                    if (argb == 0) {
                        continue;
                    }
                    int a = j * gw + i;
                    for (int round = 0; round < 4; round++) {
                        int c = a + (round == 1 ? gw : round == 2 ? gw + 1 : round == 3 ? 1 : 0);
                        vc.addVertex(last, px[c] + nx[c] * off, py[c] + ny[c] * off, pz[c] + nz[c] * off).setColor(argb)
                                .setUv((c % gw) * du, (c / gw) * dv).setOverlay(overlay).setLight(light)
                                .setNormal(last, nx[c] * side, ny[c] * side, nz[c] * side);
                    }
                }
            }
        }
    }

    /**
     * Once a second: whether the block is inside a sheet (a sail facing alike to its left or above it); if it is the
     * corner, how far the sheet runs right and down and each square's dye.
     */
    private static void measure(Sail sail, Level level, Direction facing) {
        long now = level.getGameTime();
        if (now - sail.measured < 20 && now >= sail.measured) {
            return;
        }
        sail.measured = now;
        BlockPos at = sail.getBlockPos();
        Direction across = facing.getClockWise();
        sail.inner = run(level, at, across.getOpposite(), facing, 1) > 0 || run(level, at, Direction.UP, facing, 1) > 0;
        if (sail.inner) {
            return;
        }
        // shortcut: the sheet is the rectangle of the top row and left column; a ragged sail draws only that.
        sail.right = run(level, at, across, facing, MOST);
        sail.down = run(level, at, Direction.DOWN, facing, MOST);
        int w = sail.right + 1;
        int[] colours = new int[w * (sail.down + 1)];
        BlockPos.MutableBlockPos cell = new BlockPos.MutableBlockPos();
        for (int r = 0; r <= sail.down; r++) {
            for (int c = 0; c < w; c++) {
                BlockState s = level.getBlockState(cell.set(at).move(across, c).move(Direction.DOWN, r));
                colours[r * w + c] = s.getBlock() instanceof SailBlock && s.getValue(SailBlock.FACING) == facing
                        ? 0xFF000000 | s.getValue(SailBlock.COLOR).getTextureDiffuseColor() : 0;
            }
        }
        sail.colours = colours;
    }

    private static int run(Level level, BlockPos from, Direction way, Direction facing, int most) {
        BlockPos.MutableBlockPos at = from.mutable();
        int n = 0;
        while (n < most) {
            BlockState s = level.getBlockState(at.move(way));
            if (!(s.getBlock() instanceof SailBlock) || s.getValue(SailBlock.FACING) != facing) {
                break;
            }
            n++;
        }
        return n;
    }

    private static double smoothstep(double from, double to, double x) {
        double t = Math.max(0, Math.min(1, (x - from) / (to - from)));
        return t * t * (3 - 2 * t);
    }

    /** The corner's box holds its whole sheet, bellying and rippling out of its blocks. */
    @Override
    public AABB getRenderBoundingBox(Sail sail) {
        BlockPos at = sail.getBlockPos();
        BlockState state = sail.getBlockState();
        if (sail.inner || !(state.getBlock() instanceof SailBlock)) {
            return new AABB(at);
        }
        Direction across = state.getValue(SailBlock.FACING).getClockWise();
        return AABB.encapsulatingFullBlocks(at, at.relative(across, sail.right).below(sail.down)).inflate(2.5);
    }

    /** Seen as far as the world is, as the wool sails were. */
    @Override
    public int getViewDistance() {
        return Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
    }
}
