package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Helm: a red lectern that marks a Sable ship's bearings. FACING is the ship's front (the way whoever placed it
 * was looking); the back is its opposite, left and right are {@link #left} and {@link #right}.
 */
public class HelmBlock extends HorizontalDirectionalBlock {
    private static final com.mojang.serialization.MapCodec<HelmBlock> CODEC = simpleCodec(HelmBlock::new);

    public HelmBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends HorizontalDirectionalBlock> codec() {
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

    public static Direction front(BlockState state) {
        return state.getValue(FACING);
    }

    public static Direction left(BlockState state) {
        return front(state).getCounterClockWise();
    }

    public static Direction right(BlockState state) {
        return front(state).getClockWise();
    }

    // The lectern's slope faces the helmsman, who stands behind it looking to the front.
    private static BlockState lectern(BlockState state) {
        return Blocks.LECTERN.defaultBlockState().setValue(LecternBlock.FACING, front(state).getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return lectern(state).getShape(level, pos, context);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return lectern(state).getCollisionShape(level, pos, context);
    }
}
