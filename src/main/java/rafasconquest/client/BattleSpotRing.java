package rafasconquest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

/**
 * An enlisted player's spot in their block, from the server: a white ring about a block wide on the ground where they
 * should stand, shown only while they are more than {@link #HIDE_WITHIN} blocks from it. Client thread only.
 */
public final class BattleSpotRing {
    private static final double HIDE_WITHIN = 5;
    private static final ResourceLocation BEAM = ResourceLocation.parse("textures/entity/beacon_beam.png");
    private static final int WHITE = 0xFFFFFFFF;
    private static final float OUTER = 0.55f;
    private static final float INNER = 0.42f;
    private static final int SEGMENTS = 32;

    private static boolean has;
    /** A beacon beam where an order sends the player (the server says when it shows), at (bx, by, bz). */
    private static boolean beacon;
    private static double bx;
    private static double by;
    private static double bz;
    private static double x;
    private static double y;
    private static double z;

    private BattleSpotRing() {
    }

    public static void set(boolean has, double x, double y, double z, boolean beacon, double bx, double by, double bz) {
        BattleSpotRing.beacon = beacon;
        BattleSpotRing.bx = bx;
        BattleSpotRing.by = by;
        BattleSpotRing.bz = bz;
        BattleSpotRing.has = has;
        BattleSpotRing.x = x;
        BattleSpotRing.y = y;
        BattleSpotRing.z = z;
    }

    public static void clear() {
        has = false;
    }

    private static final float FLASH_MS = 1000;
    @Nullable
    private static Vec3 flash;
    private static long flashAt;

    /** A white beacon that shoots up at a place an order was given to, and fades within a second. */
    public static void flash(Vec3 at) {
        flash = at;
        flashAt = net.minecraft.Util.getMillis();
    }

    public static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        Vec3 cam = camera.getPosition();
        float t = (net.minecraft.Util.getMillis() - flashAt) / FLASH_MS;
        if (flash != null && t < 1) {
            float r = 0.3F * Math.min(1, t * 6) * (1 - t); // grows in fast, then thins away: the beam has no alpha to fade
            pose.pushPose();
            pose.translate(Math.floor(flash.x) - cam.x, flash.y - cam.y, Math.floor(flash.z) - cam.z);
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            BeaconRenderer.renderBeaconBeam(pose, buffers, BEAM, partialTick, 1, mc.level.getGameTime(), 0, BeaconRenderer.MAX_RENDER_Y, WHITE, r, r * 1.25F);
            buffers.endBatch();
            pose.popPose();
        }
        if (beacon) {
            pose.pushPose();
            pose.translate(Math.floor(bx) - cam.x, by - cam.y, Math.floor(bz) - cam.z);
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            BeaconRenderer.renderBeaconBeam(pose, buffers, BEAM, partialTick, 1, mc.level.getGameTime(), 0, BeaconRenderer.MAX_RENDER_Y, WHITE, 0.2F, 0.25F);
            buffers.endBatch();
            pose.popPose();
        }
        if (!has || mc.player.position().distanceTo(new Vec3(x, mc.player.getY(), z)) <= HIDE_WITHIN) {
            return;
        }
        ring(pose, cam, x, y, z, OUTER, INNER, 0xE6FFFFFF);
    }

    /** A flat band on the ground from {@code a} to {@code b}, {@code half} wide each side, {@code argb} coloured. */
    static void strip(PoseStack pose, Vec3 cam, Vec3 a, Vec3 b, float half, int argb) {
        double len = Math.hypot(b.x - a.x, b.z - a.z);
        if (len < 1e-3) {
            return;
        }
        float sx = (float) (-(b.z - a.z) / len * half); // square to the band
        float sz = (float) ((b.x - a.x) / len * half);
        float ax = (float) (a.x - cam.x);
        float ay = (float) (a.y + 0.06 - cam.y);
        float az = (float) (a.z - cam.z);
        float bx = (float) (b.x - cam.x);
        float by = (float) (b.y + 0.06 - cam.y);
        float bz = (float) (b.z - cam.z);
        int alpha = argb >>> 24;
        int r = argb >> 16 & 0xFF;
        int g = argb >> 8 & 0xFF;
        int bl = argb & 0xFF;
        Matrix4f matrix = pose.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, ax - sx, ay, az - sz).setColor(r, g, bl, alpha);
        builder.addVertex(matrix, ax + sx, ay, az + sz).setColor(r, g, bl, alpha);
        builder.addVertex(matrix, bx - sx, by, bz - sz).setColor(r, g, bl, alpha);
        builder.addVertex(matrix, bx + sx, by, bz + sz).setColor(r, g, bl, alpha);
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    /**
     * Many flat rings on the ground, {@code argb} coloured, in one draw: ring i centred at ({@code at[4i]},
     * {@code at[4i+1]}, {@code at[4i+2]}), {@code at[4i+3]} across to its outer edge, {@code band} wide.
     */
    static void rings(PoseStack pose, Vec3 cam, double[] at, int count, float band, int argb) {
        if (count == 0) {
            return;
        }
        int a = argb >>> 24;
        int r = argb >> 16 & 0xFF;
        int g = argb >> 8 & 0xFF;
        int b = argb & 0xFF;
        Matrix4f matrix = pose.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        for (int k = 0; k < count; k++) {
            float x = (float) (at[4 * k] - cam.x);
            float y = (float) (at[4 * k + 1] + 0.05 - cam.y);
            float z = (float) (at[4 * k + 2] - cam.z);
            float outer = (float) at[4 * k + 3];
            float inner = outer - band;
            for (int i = 0; i < SEGMENTS; i++) {
                float cos0 = (float) Math.cos(2 * Math.PI * i / SEGMENTS);
                float sin0 = (float) Math.sin(2 * Math.PI * i / SEGMENTS);
                float cos1 = (float) Math.cos(2 * Math.PI * (i + 1) / SEGMENTS);
                float sin1 = (float) Math.sin(2 * Math.PI * (i + 1) / SEGMENTS);
                builder.addVertex(matrix, x + cos0 * outer, y, z + sin0 * outer).setColor(r, g, b, a);
                builder.addVertex(matrix, x + cos0 * inner, y, z + sin0 * inner).setColor(r, g, b, a);
                builder.addVertex(matrix, x + cos1 * inner, y, z + sin1 * inner).setColor(r, g, b, a);
                builder.addVertex(matrix, x + cos1 * outer, y, z + sin1 * outer).setColor(r, g, b, a);
            }
        }
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    /** A flat ring on the ground at (x, y, z), {@code argb} coloured. */
    static void ring(PoseStack pose, Vec3 cam, double x, double y, double z, float outer, float inner, int argb) {
        int a = argb >>> 24;
        int r = argb >> 16 & 0xFF;
        int g = argb >> 8 & 0xFF;
        int b = argb & 0xFF;
        pose.pushPose();
        pose.translate(x - cam.x, y + 0.05 - cam.y, z - cam.z);
        Matrix4f matrix = pose.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= SEGMENTS; i++) {
            double angle = 2 * Math.PI * i / SEGMENTS;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            builder.addVertex(matrix, cos * outer, 0, sin * outer).setColor(r, g, b, a);
            builder.addVertex(matrix, cos * inner, 0, sin * inner).setColor(r, g, b, a);
        }
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        pose.popPose();
    }
}
