package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * An oar block: a spruce plank with an {@link Oar} sticking straight out of its left or right side. It faces the way
 * its placer looked (the ship's bow), and its oar leaves the side to that facing's left or right. The oar is its block
 * entity, so it comes and goes with the block and rides along when Sable makes the hull a ship.
 */
public class OarBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final MapCodec<OarBlock> LEFT_CODEC = simpleCodec(p -> new OarBlock(p, false));
    private static final MapCodec<OarBlock> RIGHT_CODEC = simpleCodec(p -> new OarBlock(p, true));

    private final boolean right;

    public OarBlock(Properties properties, boolean right) {
        super(properties);
        this.right = right;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return right ? RIGHT_CODEC : LEFT_CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    /** The side its oar sticks out of. */
    public Direction side(BlockState state) {
        Direction facing = state.getValue(FACING);
        return right ? facing.getClockWise() : facing.getCounterClockWise();
    }

    @Override
    public Oar newBlockEntity(BlockPos pos, BlockState state) {
        return new Oar(pos, state);
    }
}
