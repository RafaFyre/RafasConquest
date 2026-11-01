package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix4f;
import rafasconquest.style.MarkerBlock;
import rafasconquest.style.MarkerBlockEntity;

/** What an unarmed Character Marker will become, floating above it, as a name tag is drawn. */
public class MarkerRenderer implements BlockEntityRenderer<MarkerBlockEntity> {
    private static final double SEEN_WITHIN = 24;

    public MarkerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MarkerBlockEntity marker, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        if (marker.getBlockState().getValue(MarkerBlock.ARMED) || mc.player == null
                || mc.player.distanceToSqr(marker.getBlockPos().getCenter()) > SEEN_WITHIN * SEEN_WITHIN) {
            return;
        }
        String text = marker.spec().describe();
        pose.pushPose();
        pose.translate(0.5, 1.4, 0.5);
        pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        pose.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f matrix = pose.last().pose();
        Font font = mc.font;
        float x = -font.width(text) / 2f;
        int background = (int) (mc.options.getBackgroundOpacity(0.25f) * 255f) << 24;
        font.drawInBatch(text, x, 0, 0x20FFFFFF, false, matrix, buffers, Font.DisplayMode.SEE_THROUGH, background, light);
        font.drawInBatch(text, x, 0, 0xFF66CCFF, false, matrix, buffers, Font.DisplayMode.NORMAL, 0, light);
        pose.popPose();
    }
}
