package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

/** The Strategy Board, a cartography table dyed red: right-click to open the battle screen, as the war horn does. */
public class StrategyBoardBlock extends Block {
    public StrategyBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                rafasconquest.client.BattleScreen.open();
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
