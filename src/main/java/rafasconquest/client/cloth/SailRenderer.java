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
 * Draws each {@link Sail} block's square of its sheet ({@link SailShape}): one canvas stretched over the whole sail,
 * tinted by each block's dye. How full the sail is comes from the wind on the ship's bow: full from astern, slack and
 * flogging head to wind. Every square reads the wind at the sheet's top corner, so the squares meet.
 */
public class SailRenderer implements BlockEntityRenderer<Sail> {
    private static final ResourceLocation CANVAS = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "textures/block/sail.png");
    private static final float HALF_THICK = 1 / 64F;
    /** The furthest a sheet reaches from its corner block along a row or column. */
    private static final int MOST = 32;

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
        int col = sail.left;
        int row = sail.up;
        Direction across = facing.getClockWise();
        int ax = across.getStepX(), az = across.getStepZ(), fx = facing.getStepX(), fz = facing.getStepZ();

        // The sheet's top corner in the world (a Sable ship's turn is in the pose), and its bow.
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();
        Matrix4d frame = new Matrix4d().translate(cam.x, cam.y, cam.z).rotate(new Quaternionf(camera.rotation()))
                .mul(new Matrix4d(RenderSystem.getModelViewMatrix())).mul(new Matrix4d(pose.last().pose()));
        Vector3d corner = frame.transformPosition(new Vector3d(0.5 - ax * (col + 0.5), 1 + row, 0.5 - az * (col + 0.5)));
        Vector3d bow = frame.transformDirection(new Vector3d(fx, 0, fz));
        double[] wind = new double[3];
        Wind.at(level, corner.x, corner.y, corner.z, 1, 1, wind);
        double speed = Math.hypot(wind[0], wind[2]);
        double flat = Math.max(1e-6, Math.hypot(bow.x, bow.z));
        double along = speed < 1e-6 ? 1 : (wind[0] * bow.x + wind[2] * bow.z) / (speed * flat);
        double windy = Math.min(1, speed / 20);
        double caught = smoothstep(-0.6, 0.2, along);
        SailShape shape = new SailShape(sail.left + sail.right + 1, sail.up + sail.down + 1,
                (0.5 + 0.5 * windy) * (0.2 + 0.8 * caught),
                0.15 + 0.15 * windy + 0.7 * (1 - caught) * (0.4 + 0.6 * windy),
                (level.getGameTime() + partialTick) / 20.0);

        // Squares per block side: every block of one sheet picks the same, from its corner's distance, so they meet.
        double far = corner.distance(cam.x, cam.y, cam.z);
        int k = far < 24 ? 4 : far < 64 ? 2 : 1;
        int n = k + 1;
        float[] px = new float[n * n], py = new float[n * n], pz = new float[n * n];
        float[] nx = new float[n * n], ny = new float[n * n], nz = new float[n * n];
        float[] u = new float[n * n], v = new float[n * n];
        double[] p = new double[3];
        double[] q = new double[3];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                int at = j * n + i;
                double x = col + (double) i / k;
                double y = row + (double) j / k;
                shape.point(x, y, p);
                shape.normal(x, y, q);
                double off = p[0] - col - 0.5;
                px[at] = (float) (0.5 + ax * off + fx * p[2]);
                py[at] = (float) (1 + row + p[1]);
                pz[at] = (float) (0.5 + az * off + fz * p[2]);
                nx[at] = (float) (ax * q[0] + fx * q[2]);
                ny[at] = (float) q[1];
                nz[at] = (float) (az * q[0] + fz * q[2]);
                u[at] = (float) (x / shape.w());
                v[at] = (float) (y / shape.h());
            }
        }
        int argb = 0xFF000000 | state.getValue(SailBlock.COLOR).getTextureDiffuseColor();
        VertexConsumer vc = buffers.getBuffer(RenderType.entityCutoutNoCull(CANVAS));
        PoseStack.Pose last = pose.last();
        // A front and a back face a hair apart, each lit from its own side.
        for (int side = 1; side >= -1; side -= 2) {
            float off = HALF_THICK * side;
            for (int j = 0; j < k; j++) {
                for (int i = 0; i < k; i++) {
                    int a = j * n + i;
                    for (int round = 0; round < 4; round++) {
                        int c = a + (round == 1 ? n : round == 2 ? n + 1 : round == 3 ? 1 : 0);
                        vc.addVertex(last, px[c] + nx[c] * off, py[c] + ny[c] * off, pz[c] + nz[c] * off).setColor(argb)
                                .setUv(u[c], v[c]).setOverlay(overlay).setLight(light)
                                .setNormal(last, nx[c] * side, ny[c] * side, nz[c] * side);
                    }
                }
            }
        }
    }

    /** Where the block sits in its sheet: sail blocks facing alike to its left, right, above and below; once a second. */
    private static void measure(Sail sail, Level level, Direction facing) {
        long now = level.getGameTime();
        if (now - sail.measured < 20 && now >= sail.measured) {
            return;
        }
        sail.measured = now;
        BlockPos at = sail.getBlockPos();
        Direction across = facing.getClockWise();
        sail.left = run(level, at, across.getOpposite(), facing);
        sail.right = run(level, at, across, facing);
        sail.up = run(level, at, Direction.UP, facing);
        sail.down = run(level, at, Direction.DOWN, facing);
    }

    private static int run(Level level, BlockPos from, Direction way, Direction facing) {
        BlockPos.MutableBlockPos at = from.mutable();
        int n = 0;
        while (n < MOST) {
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

    /** The cloth bellies and ripples out of its block. */
    @Override
    public AABB getRenderBoundingBox(Sail sail) {
        return new AABB(sail.getBlockPos()).inflate(2.5);
    }

    /** Seen as far as the world is, as the wool sails were. */
    @Override
    public int getViewDistance() {
        return Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
    }
}
