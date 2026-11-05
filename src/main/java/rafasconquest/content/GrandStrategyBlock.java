package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import rafasconquest.peerage.GrandStrategy;

/** The Grand Strategy table: right-click to manage the player's fief over the world map ({@link GrandStrategy}). */
public class GrandStrategyBlock extends Block {
    public GrandStrategyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player instanceof ServerPlayer server) {
            GrandStrategy.open(server);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
