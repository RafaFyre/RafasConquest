package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * The Medium Cannon: our own gun on its carriage (MediumCannon.bbmodel), made for Sable ships rather than taken from
 * Siegeworks. It points the way its placer looked, and lays {@link CannonAim#ARC} degrees either way, up and down and
 * left and right. Its gun is its {@link MediumCannonEntity}; it is drawn instanced with the oars (OarFigures), its
 * block model only for its item and for far ships. Right click to work it.
 */
public class MediumCannonBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final MapCodec<MediumCannonBlock> CODEC = simpleCodec(MediumCannonBlock::new);

    public MediumCannonBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level instanceof ServerLevel && player instanceof ServerPlayer sp && level.getBlockEntity(pos) instanceof MediumCannonEntity gun) {
            if (gun.takeBy(sp)) {
                rafasconquest.net.ConquestNetwork.sendCannonTaken(sp, pos);
                sp.displayClientMessage(Component.translatable("block.rafasconquest.medium_cannon.taken"), true);
            } else {
                sp.displayClientMessage(Component.translatable("block.rafasconquest.medium_cannon.busy"), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public MediumCannonEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MediumCannonEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide || type != ConquestContent.MEDIUM_CANNON_ENTITY.get() ? null
                : (l, p, s, be) -> ((MediumCannonEntity) be).serverTick((ServerLevel) l, s);
    }
}
