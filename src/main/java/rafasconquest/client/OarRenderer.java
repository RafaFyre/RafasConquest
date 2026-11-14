package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import rafasconquest.content.Oar;
import rafasconquest.content.OarBlock;

/** An oar, built from vanilla blocks: a shaft of four oak fence posts, its top two faced with two open oak trapdoors. */
final class OarRenderer implements BlockEntityRenderer<Oar> {
    private static final BlockState FENCE = Blocks.OAK_FENCE.defaultBlockState();
    /** Open, facing north: a 3/16-thick panel standing at the block's south side (z 13/16 to 1). */
    private static final BlockState BLADE = Blocks.OAK_TRAPDOOR.defaultBlockState()
            .setValue(TrapDoorBlock.OPEN, true).setValue(TrapDoorBlock.FACING, Direction.NORTH);
    /** Shifts the blade panel from z 13/16 back to 10/16, flat against the fence post's face (z 6/16 to 10/16). */
    private static final float BLADE_SHIFT = -3 / 16F;

    OarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(Oar oar, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (!(oar.getBlockState().getBlock() instanceof OarBlock block)) {
            return;
        }
        Direction side = block.side(oar.getBlockState());
        // The block is solid and dark inside; light the oar from the open air it sticks into.
        light = LevelRenderer.getLightColor(oar.getLevel(), oar.getBlockPos().relative(side));
        BlockRenderDispatcher blocks = Minecraft.getInstance().getBlockRenderer();
        pose.pushPose();
        pose.translate(0.5 + side.getStepX() * 0.5, 0.5, 0.5 + side.getStepZ() * 0.5); // handle at the side's face
        // Lay the shaft (built along +Y) out of that side: Y up turned to Z forward, then yaw.
        pose.mulPose(Axis.YP.rotationDegrees(-side.toYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(90));
        pose.translate(-0.5, 0, -0.5); // centre the fence posts on the shaft's line
        for (int i = 0; i < Oar.LENGTH; i++) {
            blocks.renderSingleBlock(FENCE, pose, buffers, light, overlay);
            if (i >= Oar.LENGTH - 2) {
                pose.pushPose();
                pose.translate(0, 0, BLADE_SHIFT);
                blocks.renderSingleBlock(BLADE, pose, buffers, light, overlay);
                pose.popPose();
            }
            pose.translate(0, 1, 0);
        }
        pose.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(Oar oar) {
        return new AABB(oar.getBlockPos()).inflate(Oar.LENGTH); // the shaft reaches far past its block
    }
}
