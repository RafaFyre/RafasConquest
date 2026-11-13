package rafasconquest.client.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import rafasconquest.content.BattleBannerBlock;
import rafasconquest.content.BattleBannerBlockEntity;
import rafasconquest.content.BattleWallBannerBlock;

/** Draws battle banners: vanilla's pole and bar, and the nation's arms in moving cloth. */
public class BattleBannerRenderer implements BlockEntityRenderer<BattleBannerBlockEntity> {
    public BattleBannerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BattleBannerBlockEntity banner, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        BlockState state = banner.getBlockState();
        pose.pushPose();
        boolean standing = state.getBlock() instanceof BattleBannerBlock;
        if (standing) {
            BannerArt.standing(pose, state.getValue(BattleBannerBlock.ROTATION), ((BattleBannerBlock) state.getBlock()).big());
        } else if (state.getBlock() instanceof BattleWallBannerBlock wall) {
            BannerArt.wall(pose, state.getValue(BattleWallBannerBlock.FACING), wall.big());
        }
        BannerArt.draw(pose, buffers, light, overlay, standing, true, banner, standing ? BannerArt.STANDING : BannerArt.WALL,
                BannerArt.arms(banner.nation()), null);
        pose.popPose();
    }

    /** The cloth blows well outside the block, the big one twice as far. */
    @Override
    public AABB getRenderBoundingBox(BattleBannerBlockEntity banner) {
        boolean big = banner.getBlockState().getBlock() instanceof BattleBannerBlock b ? b.big()
                : banner.getBlockState().getBlock() instanceof BattleWallBannerBlock w && w.big();
        return new AABB(banner.getBlockPos()).inflate(big ? 5 : 3);
    }
}
