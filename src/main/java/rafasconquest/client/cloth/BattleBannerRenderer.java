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
import rafasconquest.content.FlagBlock;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.RotationSegment;

/** Draws battle banners: vanilla's pole and bar, and the nation's arms in moving cloth. */
public class BattleBannerRenderer implements BlockEntityRenderer<BattleBannerBlockEntity> {
    public BattleBannerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BattleBannerBlockEntity banner, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        BlockState state = banner.getBlockState();
        if (state.getBlock() instanceof FlagBlock flag) {
            Direction facing = state.getValue(FlagBlock.FACING);
            pose.pushPose();
            pose.translate(0.5, 0, 0.5);
            if (facing == Direction.UP) {
                pose.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(state.getValue(BattleBannerBlock.ROTATION))));
                BannerArt.flagOnPole(pose, buffers, light, overlay, flag.size(), 3 * flag.size(), banner, BannerArt.arms(banner.nation()), null);
            } else {
                // +x along facing; the edge on the holder's face (or in at its post), its top at the block's top
                pose.mulPose(Axis.YP.rotationDegrees(270 - facing.toYRot()));
                pose.translate(-0.5 - state.getValue(FlagBlock.MOUNT).inset, 0.5, 0);
                BannerArt.flagHung(pose, buffers, light, overlay, flag.size(), banner, BannerArt.arms(banner.nation()));
            }
            pose.popPose();
            return;
        }
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

    /** Seen as far as sails are, not vanilla's 64 blocks (the chunk must still be loaded). */
    @Override
    public int getViewDistance() {
        return 512;
    }

    /** The cloth blows well outside the block, the big one twice as far. */
    @Override
    public AABB getRenderBoundingBox(BattleBannerBlockEntity banner) {
        if (banner.getBlockState().getBlock() instanceof FlagBlock flag) { // the pole 3·size up, the cloth 2·size out
            return new AABB(banner.getBlockPos()).inflate(2 * flag.size() + 1).expandTowards(0, 3 * flag.size(), 0);
        }
        boolean big = banner.getBlockState().getBlock() instanceof BattleBannerBlock b ? b.big()
                : banner.getBlockState().getBlock() instanceof BattleWallBannerBlock w && w.big();
        return new AABB(banner.getBlockPos()).inflate(big ? 5 : 3);
    }
}
