package rafasconquest.client.cloth;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Draws cloth, and keeps the cloth that moves: each banner, cape or standard in sight has its own {@link Cloth},
 * stepped every client tick and forgotten five seconds after it was last drawn. Cloth farther than 64 blocks, in a
 * menu, on the ground or in a frame is drawn hanging at rest.
 * <p>
 * The cloth is simulated in world blocks. Each draw gives its frame (pose to world); between steps the cloth is
 * carried along with that frame, so a running wearer's cape stays on their shoulders.
 */
public final class ClothRenderer {
    /** One textured pass over the cloth: the cloth itself (solid), then each dye layer on top. */
    public record Layer(Material material, int argb, boolean solid) {
    }

    /** A kind of cloth: its grid, depth, the boxes it stays out of, how much speed it keeps, whether blocks stop it. */
    public record Kind(int w, int h, double sx, double sy, double depth, double friction, boolean blocks, double[]... boxes) {
        Cloth make() {
            return new Cloth(w, h, sx, sy, depth).keepOut(boxes);
        }
    }

    private static final class Live {
        final Cloth cloth;
        final Kind kind;
        final Level level;
        final Matrix4d now = new Matrix4d();
        @Nullable
        Matrix4d atTick;
        long seen;

        Live(Kind kind, Level level) {
            this.cloth = kind.make();
            this.kind = kind;
            this.level = level;
        }
    }

    /** A cloth an entity carries, by where it is carried. */
    public record Carried(int entity, Object where) {
    }

    private static final Map<Object, Live> LIVE = new HashMap<>();
    private static final double RANGE = 64;
    /** How far a carried cloth's frame may be from its carrier before it is taken for a menu's picture of it. */
    private static final double NEAR = 8;
    private static final double GRAVITY = -9.81;
    private static final int ITERATIONS = 25;
    private static long ticks;
    @Nullable
    private static LivingEntity current;

    private ClothRenderer() {
    }

    /** The entity being drawn, for cloth drawn through item renderers (which are not told). */
    public static void setCurrent(@Nullable LivingEntity entity) {
        current = entity;
    }

    @Nullable
    public static LivingEntity current() {
        return current;
    }

    /** Steps every cloth drawn in the last two ticks; forgets those not drawn for five seconds. */
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.isPaused()) {
            if (mc.level == null) {
                LIVE.clear();
            }
            return;
        }
        ticks++;
        double[] wind = new double[3];
        BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();
        Iterator<Live> it = LIVE.values().iterator();
        while (it.hasNext()) {
            Live live = it.next();
            if (live.level != mc.level || ticks - live.seen > 100) {
                it.remove();
                continue;
            }
            if (ticks - live.seen > 2 || live.atTick == null) {
                continue;
            }
            Matrix4d carry = new Matrix4d(live.now).mul(live.atTick.invert(new Matrix4d()));
            live.atTick.set(live.now);
            Cloth c = live.cloth;
            Wind.at(live.level, c.x[0], c.y[0], c.z[0], wind);
            Level level = live.level;
            Cloth.Solid solid = live.kind.blocks()
                    ? (x, y, z) -> level.getBlockState(at.set(x, y, z)).isCollisionShapeFullBlock(level, at)
                    : null;
            c.step(0.05, wind[0], GRAVITY + wind[1], wind[2], ITERATIONS, live.kind.friction(), live.atTick, carry, solid);
        }
    }

    /**
     * Draws a cloth of {@code kind} hanging from the pose (local units as the kind gives them). With a {@code key}
     * it moves; {@code near} is where its carrier
     * stands, to tell a menu's picture from the world.
     */
    public static void draw(@Nullable Object key, Kind kind, PoseStack pose, MultiBufferSource buffers, int light, int overlay,
                            List<Layer> layers, @Nullable Vec3 near) {
        Minecraft mc = Minecraft.getInstance();
        // The model-view matrix the pose is drawn under: the camera's turn in the world, the projection in first person.
        Matrix4f modelView = new Matrix4f(RenderSystem.getModelViewMatrix());
        Live live = key == null || mc.level == null ? null : live(key, kind, mc, pose, modelView, near);
        int n = kind.w() * kind.h();
        float[] px = new float[n];
        float[] py = new float[n];
        float[] pz = new float[n];
        PoseStack.Pose drawPose;
        if (live == null) {
            Cloth rest = restOf(kind);
            for (int p = 0; p < n; p++) {
                px[p] = (float) rest.lx[p];
                py[p] = (float) rest.ly[p];
                pz[p] = (float) rest.lz[p];
            }
            drawPose = pose.last();
        } else {
            moving(live, mc, px, py, pz);
            PoseStack undo = new PoseStack();
            undo.mulPose(modelView.invert(new Matrix4f()));
            drawPose = undo.last();
        }
        emit(kind, px, py, pz, drawPose, buffers, light, overlay, layers);
    }

    @Nullable
    private static Live live(Object key, Kind kind, Minecraft mc, PoseStack pose, Matrix4f modelView, @Nullable Vec3 near) {
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();
        Matrix4d frame = new Matrix4d().translate(cam.x, cam.y, cam.z).rotate(new Quaternionf(camera.rotation()))
                .mul(new Matrix4d(modelView)).mul(new Matrix4d(pose.last().pose()));
        Vector3d anchor = frame.transformPosition(new Vector3d());
        if (anchor.distanceSquared(cam.x, cam.y, cam.z) > RANGE * RANGE
                || near != null && anchor.distanceSquared(near.x, near.y, near.z) > NEAR * NEAR) {
            return null;
        }
        Live live = LIVE.get(key);
        if (live == null || live.kind != kind || live.level != mc.level) {
            live = new Live(kind, mc.level);
            live.cloth.place(frame);
            LIVE.put(key, live);
        }
        live.now.set(frame);
        if (live.atTick == null) {
            live.atTick = new Matrix4d(frame);
        }
        live.seen = ticks;
        return live;
    }

    /** Where the moving cloth is now: between its last two steps, carried along to the frame of this draw. */
    private static void moving(Live live, Minecraft mc, float[] px, float[] py, float[] pz) {
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();
        Quaternionf toView = new Quaternionf(camera.rotation()).conjugate();
        Matrix4d carry = new Matrix4d(live.now).mul(live.atTick.invert(new Matrix4d()));
        double t = mc.getTimer().getGameTimeDeltaPartialTick(false);
        Cloth c = live.cloth;
        Vector3d v = new Vector3d();
        Vector3f f = new Vector3f();
        for (int p = 0; p < px.length; p++) {
            v.set(c.rx[p] + (c.x[p] - c.rx[p]) * t, c.ry[p] + (c.y[p] - c.ry[p]) * t, c.rz[p] + (c.z[p] - c.rz[p]) * t);
            carry.transformPosition(v);
            f.set((float) (v.x - cam.x), (float) (v.y - cam.y), (float) (v.z - cam.z));
            toView.transform(f);
            px[p] = f.x;
            py[p] = f.y;
            pz[p] = f.z;
        }
    }

    private static final Map<Kind, Cloth> REST = new HashMap<>();

    private static Cloth restOf(Kind kind) {
        return REST.computeIfAbsent(kind, Kind::make);
    }

    /** The grid as quads, once per layer, on the banner texture's flag (u 1–21, v 1–41 of 64). */
    private static void emit(Kind kind, float[] px, float[] py, float[] pz, PoseStack.Pose pose, MultiBufferSource buffers,
                             int light, int overlay, List<Layer> layers) {
        int w = kind.w();
        int h = kind.h();
        float[] nx = new float[px.length];
        float[] ny = new float[px.length];
        float[] nz = new float[px.length];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int p = j * w + i;
                int l = j * w + Math.max(i - 1, 0);
                int r = j * w + Math.min(i + 1, w - 1);
                int u = Math.max(j - 1, 0) * w + i;
                int d = Math.min(j + 1, h - 1) * w + i;
                // across × down faces the flag's front (−z at rest)
                float ax = px[r] - px[l], ay = py[r] - py[l], az = pz[r] - pz[l];
                float bx = px[d] - px[u], by = py[d] - py[u], bz = pz[d] - pz[u];
                float cx = ay * bz - az * by, cy = az * bx - ax * bz, cz = ax * by - ay * bx;
                float len = (float) Math.sqrt(cx * cx + cy * cy + cz * cz);
                if (len < 1e-9F) {
                    cz = -1;
                    len = 1;
                }
                nx[p] = cx / len;
                ny[p] = cy / len;
                nz[p] = cz / len;
            }
        }
        for (Layer layer : layers) {
            VertexConsumer vc = layer.solid()
                    ? layer.material().buffer(buffers, RenderType::entityCutoutNoCull)
                    : layer.material().buffer(buffers, RenderType::entityNoOutline);
            for (int j = 0; j < h - 1; j++) {
                for (int i = 0; i < w - 1; i++) {
                    int p = j * w + i;
                    vertex(vc, pose, px, py, pz, nx, ny, nz, p, i, j, w, h, layer.argb(), light, overlay);
                    vertex(vc, pose, px, py, pz, nx, ny, nz, p + w, i, j + 1, w, h, layer.argb(), light, overlay);
                    vertex(vc, pose, px, py, pz, nx, ny, nz, p + w + 1, i + 1, j + 1, w, h, layer.argb(), light, overlay);
                    vertex(vc, pose, px, py, pz, nx, ny, nz, p + 1, i + 1, j, w, h, layer.argb(), light, overlay);
                }
            }
        }
    }

    private static void vertex(VertexConsumer vc, PoseStack.Pose pose, float[] px, float[] py, float[] pz,
                               float[] nx, float[] ny, float[] nz, int p, int i, int j, int w, int h, int argb, int light, int overlay) {
        float u = (1 + 20F * i / (w - 1)) / 64F;
        float v = (1 + 40F * j / (h - 1)) / 64F;
        vc.addVertex(pose, px[p], py[p], pz[p]).setColor(argb).setUv(u, v).setOverlay(overlay).setLight(light)
                .setNormal(pose, nx[p], ny[p], nz[p]);
    }
}
