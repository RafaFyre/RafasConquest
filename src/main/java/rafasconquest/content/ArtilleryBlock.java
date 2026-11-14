package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.Machine;

/**
 * The Artillery Block: set in a ship's deck where a Siegeworks engine is to stand, facing the way it points (the way its
 * placer looked). Its {@link #WEAPON} is picked in creative from a dropdown (right click). On land it is only a block; once
 * Sable makes its hull a ship, its {@link ArtilleryMount} puts the engine on top with a siege engineer working it, and
 * keeps it there. The weapon is in the block state, so a ship file keeps it with no block entity data.
 */
public class ArtilleryBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final MapCodec<ArtilleryBlock> CODEC = simpleCodec(ArtilleryBlock::new);
    /** The engine it holds: any that shoots. */
    public static final EnumProperty<Machine> WEAPON = EnumProperty.create("weapon", Machine.class, Machine::shoots);

    public ArtilleryBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WEAPON, Machine.CULVERIN));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WEAPON);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isCreative()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            rafasconquest.client.ClientScreens.openArtillery(pos, state.getValue(WEAPON));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** The block gone (broken, or its ship sunk): its engine goes with it; its crew stay where they stand. */
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel server && level.getBlockEntity(pos) instanceof ArtilleryMount mount) {
            mount.dismantle(server);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public ArtilleryMount newBlockEntity(BlockPos pos, BlockState state) {
        return new ArtilleryMount(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide || type != ConquestContent.ARTILLERY_ENTITY.get() ? null
                : (l, p, s, be) -> ((ArtilleryMount) be).serverTick((ServerLevel) l, s);
    }
}
