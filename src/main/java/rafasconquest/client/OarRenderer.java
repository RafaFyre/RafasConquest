package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import rafasconquest.content.Oar;

/** An oar, built from vanilla blocks: a shaft of four oak fence posts, its top two faced with two open oak trapdoors. */
final class OarRenderer extends EntityRenderer<Oar> {
    private static final BlockState FENCE = Blocks.OAK_FENCE.defaultBlockState();
    /** Open, facing north: a 3/16-thick panel standing at the block's south side (z 13/16 to 1). */
    private static final BlockState BLADE = Blocks.OAK_TRAPDOOR.defaultBlockState()
            .setValue(TrapDoorBlock.OPEN, true).setValue(TrapDoorBlock.FACING, Direction.NORTH);
    /** Shifts the blade panel from z 13/16 back to 10/16, flat against the fence post's face (z 6/16 to 10/16). */
    private static final float BLADE_SHIFT = -3 / 16F;

    OarRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Oar oar, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        BlockRenderDispatcher blocks = Minecraft.getInstance().getBlockRenderer();
        pose.pushPose();
        // Lay the shaft (built along +Y) along the oar's look direction: Y up turned to Z forward, then pitch, then yaw.
        pose.mulPose(Axis.YP.rotationDegrees(-yaw));
        pose.mulPose(Axis.XP.rotationDegrees(90 + Mth.lerp(partialTick, oar.xRotO, oar.getXRot())));
        pose.translate(-0.5, 0, -0.5); // centre the fence posts on the shaft's line
        for (int i = 0; i < Oar.LENGTH; i++) {
            blocks.renderSingleBlock(FENCE, pose, buffers, light, OverlayTexture.NO_OVERLAY);
            if (i >= Oar.LENGTH - 2) {
                pose.pushPose();
                pose.translate(0, 0, BLADE_SHIFT);
                blocks.renderSingleBlock(BLADE, pose, buffers, light, OverlayTexture.NO_OVERLAY);
                pose.popPose();
            }
            pose.translate(0, 1, 0);
        }
        pose.popPose();
        super.render(oar, yaw, partialTick, pose, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(Oar oar) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
