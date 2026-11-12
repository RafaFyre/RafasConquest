package rafasconquest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import rafasconquest.battle.DistantDepth;
import rafasconquest.battle.FormationSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Arrows in flight from archers the client sees only as figures: each second the server says which formation shot at
 * which, and how many arrows ({@link rafasconquest.net.ConquestNetwork.Volleys}). Each arrow leaves a figure of the
 * archers at a moment within the second, arcs over and comes down somewhere in the target formation. They are drawn as
 * thin streaks along their flight, never smaller on screen than a streak a little over a block long would be at 80
 * blocks, so volleys can be seen from far off; like the figures, those beyond what the camera sees are pulled in
 * ({@link DistantDepth}). Client thread only.
 */
public final class ArrowVolleys {
    /** At most this many arrows of one volley are drawn; a big volley is still a cloud of them. */
    private static final int MAX_PER_VOLLEY = 24;
    /** At most this many arrows in the air at once. */
    private static final int MAX_IN_FLIGHT = 3000;
    /** How fast an arrow crosses the ground, in blocks a second, and the shortest flight. */
    private static final double SPEED = 30;
    private static final double MIN_FLIGHT = 0.6;
    /** How high an arrow rises over the middle of its flight, as a share of the distance, and at most. */
    private static final double ARC = 0.18;
    private static final double MAX_RISE = 25;
    /** An arrow's length in blocks, and how long it is drawn at the least for its distance (blocks per block away). */
    private static final double LENGTH = 0.8;
    private static final double MIN_LENGTH_PER_BLOCK = 0.015;
    private static final double WIDTH_SHARE = 0.08;
    private static final int SHAFT = 0x8B6B4A;
    private static final int FLETCHING = 0xE8E8E8;
    private static final int HEAD = 0x9A9A9A;

    /** An arrow: from where, to where, how high its arc rises, when it is loosed and how long it flies. */
    private record Arrow(Vec3 from, Vec3 to, double rise, long start, double seconds) {
    }

    private static final List<Arrow> ARROWS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    private ArrowVolleys() {
    }

    public static void receive(int[] from, int[] to, int[] shots) {
        long now = Util.getMillis();
        for (int v = 0; v < from.length; v++) {
            FormationSnapshot archers = DistantArmies.at(from[v]);
            FormationSnapshot target = DistantArmies.at(to[v]);
            if (archers == null || target == null || archers.alive() <= 0) {
                continue;
            }
            List<FormationSnapshot.Sprite> figures = archers.layout();
            int n = Math.min(shots[v], MAX_PER_VOLLEY);
            for (int i = 0; i < n && ARROWS.size() < MAX_IN_FLIGHT; i++) {
                FormationSnapshot.Sprite s = figures.get(RANDOM.nextInt(figures.size()));
                Vec3 start = new Vec3(s.x(), s.y() + 1.4, s.z());
                double right = (RANDOM.nextDouble() * 2 - 1) * Math.max(1, target.halfWidth());
                double back = (RANDOM.nextDouble() * 2 - 1) * Math.max(1, target.halfDepth());
                Vec3 end = new Vec3(target.x() - target.facingZ() * right - target.facingX() * back, target.y() + 0.5,
                        target.z() + target.facingX() * right - target.facingZ() * back);
                double distance = Math.hypot(end.x - start.x, end.z - start.z);
                ARROWS.add(new Arrow(start, end, Math.min(MAX_RISE, distance * ARC), now + RANDOM.nextInt(1000),
                        MIN_FLIGHT + distance / SPEED));
            }
        }
    }

    public static void clear() {
        ARROWS.clear();
    }

    /**
     * Draws every arrow in flight that the camera can see. Worked in plain numbers, not vectors: thousands of arrows a
     * frame would otherwise make tens of thousands of objects to throw away.
     */
    public static void render(PoseStack pose, Camera camera, net.minecraft.client.renderer.culling.Frustum frustum) {
        long now = Util.getMillis();
        ARROWS.removeIf(a -> (now - a.start()) / 1000.0 / a.seconds() > 1); // landed
        if (ARROWS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Vec3 eye = camera.getPosition();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        Matrix4f matrix = pose.last().pose();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int drawn = 0;
        for (Arrow a : ARROWS) {
            double u = (now - a.start()) / 1000.0 / a.seconds();
            if (u < 0) {
                continue; // not loosed yet
            }
            Vec3 from = a.from();
            Vec3 to = a.to();
            // Where it is along its flight, from the camera, and which way it flies there.
            double x = from.x + (to.x - from.x) * u;
            double y = from.y + (to.y - from.y) * u + 4 * a.rise() * u * (1 - u);
            double z = from.z + (to.z - from.z) * u;
            double hx = x - eye.x;
            double hy = y - eye.y;
            double hz = z - eye.z;
            double distance = Math.sqrt(hx * hx + hy * hy + hz * hz);
            if (distance < 1e-3) {
                continue;
            }
            double ax = to.x - from.x;
            double ay = to.y - from.y + 4 * a.rise() * (1 - 2 * u);
            double az = to.z - from.z;
            double alongLength = Math.sqrt(ax * ax + ay * ay + az * az);
            if (alongLength < 1e-9) {
                continue;
            }
            ax /= alongLength;
            ay /= alongLength;
            az /= alongLength;
            double sx = ay * hz - az * hy; // square to its flight and to the line of sight
            double sy = az * hx - ax * hz;
            double sz = ax * hy - ay * hx;
            double sideLength = Math.sqrt(sx * sx + sy * sy + sz * sz);
            if (sideLength < 1e-3) {
                continue; // flying straight at the camera
            }
            double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
            double length = Math.max(LENGTH, distance * MIN_LENGTH_PER_BLOCK) * scale;
            double px = hx * scale;
            double py = hy * scale;
            double pz = hz * scale;
            if (!frustum.isVisible(new net.minecraft.world.phys.AABB(eye.x + px - length, eye.y + py - length, eye.z + pz - length,
                    eye.x + px + length, eye.y + py + length, eye.z + pz + length))) {
                continue;
            }
            double w = length * WIDTH_SHARE / 2 / sideLength;
            int light = lightAt(mc, pos.set(x, y, z));
            float[] head = {(float) px, (float) py, (float) pz};
            float[] back = {(float) (-ax * length), (float) (-ay * length), (float) (-az * length)};
            float[] side = {(float) (sx * w), (float) (sy * w), (float) (sz * w)};
            streak(buffer, matrix, head, back, 0, 0.1F, side, 1, HEAD, light);
            streak(buffer, matrix, head, back, 0.1F, 0.75F, side, 1, SHAFT, light);
            streak(buffer, matrix, head, back, 0.75F, 1, side, 1.6F, FLETCHING, light);
            drawn++;
        }
        if (drawn == 0) {
            try (com.mojang.blaze3d.vertex.MeshData unused = buffer.build()) { // nothing to draw: let the buffer go
            }
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        BufferUploader.drawWithShader(buffer.buildOrThrow());
        mc.gameRenderer.lightTexture().turnOffLightLayer();
        RenderSystem.enableCull();
    }

    /**
     * The part of an arrow from {@code a} to {@code b} of the way along {@code back} from its head, {@code side}
     * (times {@code widen}) to each side.
     */
    private static void streak(BufferBuilder buffer, Matrix4f matrix, float[] head, float[] back, float a, float b, float[] side,
                               float widen, int rgb, int light) {
        float r = (rgb >> 16 & 255) / 255F;
        float g = (rgb >> 8 & 255) / 255F;
        float bl = (rgb & 255) / 255F;
        float px = head[0] + back[0] * a, py = head[1] + back[1] * a, pz = head[2] + back[2] * a;
        float qx = head[0] + back[0] * b, qy = head[1] + back[1] * b, qz = head[2] + back[2] * b;
        float wx = side[0] * widen, wy = side[1] * widen, wz = side[2] * widen;
        buffer.addVertex(matrix, px + wx, py + wy, pz + wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, qx + wx, qy + wy, qz + wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, qx - wx, qy - wy, qz - wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, px - wx, py - wy, pz - wz).setColor(r, g, bl, 1F).setLight(light);
    }

    /** The light where the arrow flies, as for the figures; out past the chunks the client has, open sky. */
    private static int lightAt(Minecraft mc, BlockPos pos) {
        if (mc.level == null || !mc.level.hasChunkAt(pos)) {
            return LightTexture.FULL_SKY;
        }
        return LevelRenderer.getLightColor(mc.level, pos);
    }
}
