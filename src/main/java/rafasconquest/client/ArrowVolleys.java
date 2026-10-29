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
import java.util.Iterator;
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

    private record Arrow(Vec3 from, Vec3 to, double rise, long start, double seconds) {
        /** Where it is a share {@code u} of the way along its flight. */
        Vec3 at(double u) {
            return from.lerp(to, u).add(0, 4 * rise * u * (1 - u), 0);
        }

        /** Which way it flies there (not normalised). */
        Vec3 heading(double u) {
            return to.subtract(from).add(0, 4 * rise * (1 - 2 * u), 0);
        }
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

    public static void render(PoseStack pose, Camera camera) {
        if (ARROWS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Vec3 eye = camera.getPosition();
        long now = Util.getMillis();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        Matrix4f matrix = pose.last().pose();
        int drawn = 0;
        for (Iterator<Arrow> it = ARROWS.iterator(); it.hasNext(); ) {
            Arrow a = it.next();
            double u = (now - a.start()) / 1000.0 / a.seconds();
            if (u > 1) {
                it.remove();
                continue;
            }
            if (u < 0) {
                continue; // not loosed yet
            }
            Vec3 head = a.at(u).subtract(eye);
            double distance = head.length();
            if (distance < 1e-3) {
                continue;
            }
            double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
            Vec3 along = a.heading(u).normalize();
            Vec3 side = along.cross(head).normalize();
            if (side.lengthSqr() < 1e-6) {
                continue; // flying straight at the camera
            }
            double length = Math.max(LENGTH, distance * MIN_LENGTH_PER_BLOCK);
            Vec3 h = head.scale(scale);
            Vec3 step = along.scale(-length * scale);
            Vec3 w = side.scale(length * WIDTH_SHARE * scale / 2);
            int light = lightAt(mc, a.at(u));
            streak(buffer, matrix, h, step, 0, 0.1, w, HEAD, light);
            streak(buffer, matrix, h, step, 0.1, 0.75, w, SHAFT, light);
            streak(buffer, matrix, h, step, 0.75, 1, w.scale(1.6), FLETCHING, light);
            drawn++;
        }
        if (drawn == 0) {
            buffer.end().release();
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        BufferUploader.drawWithShader(buffer.end());
        mc.gameRenderer.lightTexture().turnOffLightLayer();
        RenderSystem.enableCull();
    }

    /** The part of an arrow from {@code a} to {@code b} of the way back from its head, {@code w} to each side. */
    private static void streak(BufferBuilder buffer, Matrix4f matrix, Vec3 head, Vec3 step, double a, double b, Vec3 w,
                               int rgb, int light) {
        float r = (rgb >> 16 & 255) / 255F;
        float g = (rgb >> 8 & 255) / 255F;
        float bl = (rgb & 255) / 255F;
        Vec3 p = head.add(step.scale(a));
        Vec3 q = head.add(step.scale(b));
        vertex(buffer, matrix, p.add(w), r, g, bl, light);
        vertex(buffer, matrix, q.add(w), r, g, bl, light);
        vertex(buffer, matrix, q.subtract(w), r, g, bl, light);
        vertex(buffer, matrix, p.subtract(w), r, g, bl, light);
    }

    private static void vertex(BufferBuilder buffer, Matrix4f matrix, Vec3 v, float r, float g, float b, int light) {
        buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z).color(r, g, b, 1F).uv2(light).endVertex();
    }

    /** The light where the arrow flies, as for the figures; out past the chunks the client has, open sky. */
    private static int lightAt(Minecraft mc, Vec3 at) {
        BlockPos pos = BlockPos.containing(at);
        if (mc.level == null || !mc.level.hasChunkAt(pos)) {
            return LightTexture.FULL_SKY;
        }
        return LevelRenderer.getLightColor(mc.level, pos);
    }
}
