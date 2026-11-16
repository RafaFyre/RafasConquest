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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
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
 * stepped every client tick and forgotten five seconds after it was last drawn (past 64 blocks more cheaply, through
 * blocks). Cloth farther than 512 blocks, in a
 * menu, on the ground or in a frame is drawn hanging at rest.
 * <p>
 * The cloth is simulated in world blocks. Each draw gives its frame (pose to world); between steps the cloth is
 * carried along with that frame, so a running wearer's cape stays on their shoulders.
 */
public final class ClothRenderer {
    /**
     * One textured pass over the cloth: the cloth itself (solid), then each dye layer on top. The cloth spans the
     * texture's pixels {@code u0}–{@code u1}, {@code v0}–{@code v1} (of 64), the banner's flag unless said.
     */
    public record Layer(Material material, int argb, boolean solid, float u0, float v0, float u1, float v1) {
        public Layer(Material material, int argb, boolean solid) {
            this(material, argb, solid, 1, 1, 21, 41);
        }
    }

    /**
     * What a cloth is made of, which is how heavy and stiff it moves.
     *
     * @param keep    share of its speed kept each tick (less: it settles sooner, as heavy cloth does)
     * @param wind    share of the wind that moves it (heavier cloth, the same wind, less push)
     * @param aero    share of the wind that presses along its facing rather than evenly (billowing, not twisting)
     * @param bend    stiffness of its bend springs (0 silk that creases anywhere, 1 felt)
     * @param flutter how fast gusts come and go on it (1 Physics Mod's flag flutter)
     * @param thick   how thick it is drawn, in the kind's local units
     */
    public record Fabric(double keep, double wind, double aero, double bend, double flutter, double thick) {
    }

    /**
     * A kind of cloth: its grid, depth, fabric, the boxes it stays out of, whether blocks stop it, and whether it hangs
     * on a back (local +z behind, −x the wearer's right), where wind must not press it into the wearer.
     */
    public record Kind(int w, int h, double sx, double sy, double depth, Fabric fabric, boolean blocks, boolean onBack,
                       double[]... boxes) {
        Cloth make() {
            return new Cloth(w, h, sx, sy, depth, fabric.bend()).keepOut(boxes);
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
        /** Past {@link #RANGE}: stepped cheaply, through blocks. */
        boolean distant;
        final Object key;

        Live(Kind kind, Level level, Object key) {
            this.cloth = kind.make();
            this.kind = kind;
            this.level = level;
            this.key = key;
        }
    }

    /** A cloth an entity carries, by where it is carried. */
    public record Carried(int entity, Object where) {
    }

    /**
     * A carrier drawn far off and {@code scale} times its size (the grand strategy map's marcher): its cloth moves at
     * any distance, under gravity and a steady {@code wind} (blocks/s² at full size) both scaled with it, so it moves
     * as a life-size cloth would on screen.
     */
    public record Far(double scale, double[] wind) {
    }

    private static final Map<Integer, Far> FAR = new HashMap<>();

    /** Marks an entity as carried far (null: no longer). */
    public static void far(int entity, @Nullable Far far) {
        if (far == null) {
            FAR.remove(entity);
        } else {
            FAR.put(entity, far);
        }
    }

    @Nullable
    private static Far farOf(Object key) {
        return key instanceof Carried c ? FAR.get(c.entity()) : null;
    }

    private static final Map<Object, Live> LIVE = new HashMap<>();
    private static final double RANGE = 64;
    /** Out to here cloth still moves, with {@link #FAR_ITERATIONS}; past it, it hangs at rest. */
    private static final double FAR_RANGE = 512;
    private static final int FAR_ITERATIONS = 6;
    /** How far a carried cloth's frame may be from its carrier before it is taken for a menu's picture of it. */
    private static final double NEAR = 8;
    private static final double GRAVITY = 9.81;
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

    private static final Matrix4f UPRIGHT = new Matrix4f();

    /**
     * As {@link #setCurrent}, with the pose at its feet and its body's turn: its held flags stand upright in that
     * frame, at life size, whatever its arm does.
     */
    public static void setCurrent(LivingEntity entity, Matrix4f feet, float bodyYaw) {
        current = entity;
        UPRIGHT.set(feet).rotateY((float) Math.toRadians(180 - bodyYaw));
        Vector3f s = UPRIGHT.getScale(new Vector3f());
        UPRIGHT.scale(1 / s.x, 1 / s.y, 1 / s.z);
    }

    /** The current entity's upright frame (its translation is its feet's). */
    public static Matrix4f upright() {
        return UPRIGHT;
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
            Fabric fabric = live.kind.fabric();
            Far far = farOf(live.key);
            c.stretch = far == null ? 1 : far.scale();
            c.tethered = live.distant;
            if (far != null) {
                for (int k = 0; k < 3; k++) {
                    wind[k] = far.wind()[k] * far.scale();
                }
            } else {
                Wind.at(live.level, c.x[0], c.y[0], c.z[0], fabric.flutter(), fabric.wind(), wind);
            }
            if (live.kind.onBack()) {
                offTheBack(live.atTick, wind);
            }
            Level level = live.level;
            Cloth.Solid solid = live.kind.blocks() && far == null && !live.distant // far off, the blocks under it are not where it is drawn
                    ? (x, y, z) -> level.getBlockState(at.set(x, y, z)).isCollisionShapeFullBlock(level, at)
                    : null;
            c.step(0.05, GRAVITY * (far == null ? 1 : far.scale()), wind, fabric.aero(), live.distant ? FAR_ITERATIONS : ITERATIONS,
                    fabric.keep(), live.atTick, carry, solid);
        }
    }

    /**
     * Wind from behind would press a cape into its wearer: that part of it blows the cape to the wearer's right instead,
     * at half strength.
     */
    static void offTheBack(Matrix4dc frame, double[] wind) {
        Vector3d back = frame.transformDirection(new Vector3d(0, 0, 1));
        back.y = 0;
        if (back.lengthSquared() < 1e-12) {
            return;
        }
        back.normalize();
        double along = wind[0] * back.x + wind[2] * back.z;
        if (along >= 0) {
            return;
        }
        Vector3d right = frame.transformDirection(new Vector3d(-1, 0, 0));
        right.y = 0;
        right.normalize();
        wind[0] += -along * (back.x + right.x * 0.5);
        wind[2] += -along * (back.z + right.z * 0.5);
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
        float thick = (float) kind.fabric().thick();
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
            thick *= (float) live.now.transformDirection(new Vector3d(1, 0, 0)).length(); // local units to blocks
        }
        emit(kind, px, py, pz, thick, drawPose, buffers, light, overlay, layers);
    }

    @Nullable
    private static Live live(Object key, Kind kind, Minecraft mc, PoseStack pose, Matrix4f modelView, @Nullable Vec3 near) {
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();
        Matrix4d frame = new Matrix4d().translate(cam.x, cam.y, cam.z).rotate(new Quaternionf(camera.rotation()))
                .mul(new Matrix4d(modelView)).mul(new Matrix4d(pose.last().pose()));
        Vector3d anchor = frame.transformPosition(new Vector3d());
        double away = anchor.distanceSquared(cam.x, cam.y, cam.z);
        boolean marcher = farOf(key) != null;
        if (!marcher && (away > FAR_RANGE * FAR_RANGE
                || near != null && anchor.distanceSquared(near.x, near.y, near.z) > NEAR * NEAR)) {
            return null;
        }
        Live live = LIVE.get(key);
        if (live == null || live.kind != kind || live.level != mc.level) {
            live = new Live(kind, mc.level, key);
            live.cloth.place(frame);
            LIVE.put(key, live);
        }
        live.now.set(frame);
        live.distant = !marcher && away > RANGE * RANGE;
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

    /**
     * The grid as quads, once per layer, on the banner texture's flag (u 1–21, v 1–41 of 64): a front and a back face
     * {@code thick} apart, and (in the cloth's own colours) a strip round the edge between them, so it looks thick.
     */
    private static void emit(Kind kind, float[] px, float[] py, float[] pz, float thick, PoseStack.Pose pose,
                             MultiBufferSource buffers, int light, int overlay, List<Layer> layers) {
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
        int n = px.length;
        float half = thick * 0.5F;
        float[][] front = new float[3][n];
        float[][] back = new float[3][n];
        for (int p = 0; p < n; p++) {
            front[0][p] = px[p] + nx[p] * half;
            front[1][p] = py[p] + ny[p] * half;
            front[2][p] = pz[p] + nz[p] * half;
            back[0][p] = px[p] - nx[p] * half;
            back[1][p] = py[p] - ny[p] * half;
            back[2][p] = pz[p] - nz[p] * half;
        }
        // The rim: down each side, along the bottom, and along the top under the bar.
        int[] rim = new int[2 * (w - 1) + 2 * (h - 1)];
        int r = 0;
        for (int i = 0; i < w - 1; i++) {
            rim[r++] = i;
            rim[r++] = (h - 1) * w + i;
        }
        for (int j = 0; j < h - 1; j++) {
            rim[r++] = j * w;
            rim[r++] = j * w + w - 1;
        }
        for (int l = 0; l < layers.size(); l++) {
            Layer layer = layers.get(l);
            VertexConsumer vc = layer.solid()
                    ? layer.material().buffer(buffers, RenderType::entityCutoutNoCull)
                    : layer.material().buffer(buffers, RenderType::entityNoOutline);
            for (int j = 0; j < h - 1; j++) {
                for (int i = 0; i < w - 1; i++) {
                    int p = j * w + i;
                    quad(vc, pose, front, nx, ny, nz, 1, w, h, layer, light, overlay, p, p + w, p + w + 1, p + 1);
                    quad(vc, pose, back, nx, ny, nz, -1, w, h, layer, light, overlay, p + 1, p + w + 1, p + w, p);
                }
            }
            if (l < 2 && thick > 0) { // the cloth and its base colour; dye patterns stay on the faces
                for (int e = 0; e < rim.length; e++) {
                    int a = rim[e];
                    int b = e < 2 * (w - 1) ? a + 1 : a + w;
                    vertex(vc, pose, front, nx, ny, nz, 1, a, w, h, layer, light, overlay, 0.5F);
                    vertex(vc, pose, front, nx, ny, nz, 1, b, w, h, layer, light, overlay, 0.5F);
                    vertex(vc, pose, back, nx, ny, nz, 1, b, w, h, layer, light, overlay, 0.5F);
                    vertex(vc, pose, back, nx, ny, nz, 1, a, w, h, layer, light, overlay, 0.5F);
                }
            }
        }
    }

    private static void quad(VertexConsumer vc, PoseStack.Pose pose, float[][] at, float[] nx, float[] ny, float[] nz, int side,
                             int w, int h, Layer layer, int light, int overlay, int a, int b, int c, int d) {
        vertex(vc, pose, at, nx, ny, nz, side, a, w, h, layer, light, overlay);
        vertex(vc, pose, at, nx, ny, nz, side, b, w, h, layer, light, overlay);
        vertex(vc, pose, at, nx, ny, nz, side, c, w, h, layer, light, overlay);
        vertex(vc, pose, at, nx, ny, nz, side, d, w, h, layer, light, overlay);
    }

    private static void vertex(VertexConsumer vc, PoseStack.Pose pose, float[][] at, float[] nx, float[] ny, float[] nz,
                               int side, int p, int w, int h, Layer layer, int light, int overlay) {
        vertex(vc, pose, at, nx, ny, nz, side, p, w, h, layer, light, overlay, 0);
    }

    /**
     * {@code inset} pixels in from the flag's edge: the rim, sampled right on the edge, flickered between the flag's
     * pixel and the clear one outside it.
     */
    private static void vertex(VertexConsumer vc, PoseStack.Pose pose, float[][] at, float[] nx, float[] ny, float[] nz,
                               int side, int p, int w, int h, Layer layer, int light, int overlay, float inset) {
        float u = Mth.clamp(layer.u0() + (layer.u1() - layer.u0()) * (p % w) / (w - 1), layer.u0() + inset, layer.u1() - inset) / 64F;
        float v = Mth.clamp(layer.v0() + (layer.v1() - layer.v0()) * (p / w) / (h - 1), layer.v0() + inset, layer.v1() - inset) / 64F;
        vc.addVertex(pose, at[0][p], at[1][p], at[2][p]).setColor(layer.argb()).setUv(u, v).setOverlay(overlay).setLight(light)
                .setNormal(pose, nx[p] * side, ny[p] * side, nz[p] * side);
    }
}
