package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import rafasconquest.content.GateBlockEntity;

/** A Gate Block's door boxes, outlined for a player in creative nearby: the left door green, the right blue. */
public class GateRenderer implements BlockEntityRenderer<GateBlockEntity> {
    private static final double SEEN_WITHIN = 32;

    public GateRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(GateBlockEntity gate, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.player.isCreative()
                || mc.player.distanceToSqr(gate.getBlockPos().getCenter()) > SEEN_WITHIN * SEEN_WITHIN) {
            return;
        }
        outline(pose, buffers, gate.left.box, 0.3f, 1f, 0.3f);
        outline(pose, buffers, gate.right.box, 0.3f, 0.6f, 1f);
    }

    private static void outline(PoseStack pose, MultiBufferSource buffers, int[] box, float r, float g, float b) {
        if (box != null) {
            LevelRenderer.renderLineBox(pose, buffers.getBuffer(RenderType.lines()),
                    box[0], box[1], box[2], box[3] + 1, box[4] + 1, box[5] + 1, r, g, b, 1f);
        }
    }

    @Override
    public AABB getRenderBoundingBox(GateBlockEntity gate) {
        return AABB.INFINITE;
    }
}
