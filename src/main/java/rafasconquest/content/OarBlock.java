package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;

/**
 * An oar block: a spruce plank with an {@link Oar} sticking straight out of its left or right side. It faces the way
 * its placer looked (the ship's bow), and its oar leaves the side to that facing's left or right. Placing it puts the
 * oar there; breaking it takes the oar away (the oar watches its block).
 */
public class OarBlock extends HorizontalDirectionalBlock {
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
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean moved) {
        super.onPlace(state, level, pos, old, moved);
        if (level.isClientSide || old.is(this)) {
            return; // turning it in place keeps its oar
        }
        Direction side = side(state);
        Vec3 at = pos.getCenter().add(side.getStepX() * 0.5, 0, side.getStepZ() * 0.5);
        level.addFreshEntity(new Oar(level, pos, at, side.toYRot()));
    }
}
