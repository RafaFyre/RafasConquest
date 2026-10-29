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

/**
 * An enlisted player's spot in their block, from the server: a white ring about a block wide on the ground where they
 * should stand, shown only while they are more than {@link #HIDE_WITHIN} blocks from it. Client thread only.
 */
public final class BattleSpotRing {
    private static final double HIDE_WITHIN = 5;
    private static final double BEACON_BEYOND = 10;
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final float[] WHITE = {1, 1, 1};
    private static final float OUTER = 0.55f;
    private static final float INNER = 0.42f;
    private static final int SEGMENTS = 32;

    private static boolean has;
    /** While the armies deploy: a beacon beam over the spot, for a player more than {@link #BEACON_BEYOND} from it. */
    private static boolean beacon;
    private static double x;
    private static double y;
    private static double z;

    private BattleSpotRing() {
    }

    public static void set(boolean has, double x, double y, double z, boolean beacon) {
        BattleSpotRing.beacon = beacon;
        BattleSpotRing.has = has;
        BattleSpotRing.x = x;
        BattleSpotRing.y = y;
        BattleSpotRing.z = z;
    }

    public static void clear() {
        has = false;
    }

    public static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (!has || mc.player == null || mc.level == null) {
            return;
        }
        double away = mc.player.position().distanceTo(new Vec3(x, mc.player.getY(), z));
        Vec3 cam = camera.getPosition();
        if (beacon && away > BEACON_BEYOND) {
            pose.pushPose();
            pose.translate(Math.floor(x) - cam.x, y - cam.y, Math.floor(z) - cam.z);
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            BeaconRenderer.renderBeaconBeam(pose, buffers, BEAM, partialTick, 1, mc.level.getGameTime(), 0, BeaconRenderer.MAX_RENDER_Y, WHITE, 0.2F, 0.25F);
            buffers.endBatch();
            pose.popPose();
        }
        if (away <= HIDE_WITHIN) {
            return;
        }
        pose.pushPose();
        pose.translate(x - cam.x, y + 0.05 - cam.y, z - cam.z);
        Matrix4f matrix = pose.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= SEGMENTS; i++) {
            double a = 2 * Math.PI * i / SEGMENTS;
            float cos = (float) Math.cos(a);
            float sin = (float) Math.sin(a);
            builder.vertex(matrix, cos * OUTER, 0, sin * OUTER).color(255, 255, 255, 230).endVertex();
            builder.vertex(matrix, cos * INNER, 0, sin * INNER).color(255, 255, 255, 230).endVertex();
        }
        BufferUploader.drawWithShader(builder.end());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        pose.popPose();
    }
}
