package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * A flag: a battle banner's cloth flown sideways, with no bar. {@code size} 1–4 (small to huge) makes the cloth
 * size × 2·size blocks. Placed on the ground ({@code FACING} up) it stands on its own pole 3·size tall; placed on a
 * block's side it has no pole and the block holds it, the cloth flying out along {@code FACING}. A wall joins up to
 * it; on a fence post or bars it reaches in to the post, and they do not reach out to it. Takes its placer's nation
 * as a banner does.
 */
public class FlagBlock extends BattleBannerBlock {
    /** Up: on the ground, on its pole. Sideways: held by the block behind it, flying this way. */
    public static final DirectionProperty FACING = DirectionProperty.create("facing", d -> d != Direction.DOWN);
    public static final EnumProperty<Mount> MOUNT = EnumProperty.create("mount", Mount.class);

    /** What holds a flag on its side, and how far (in blocks) into that block its edge reaches to meet the post. */
    public enum Mount implements StringRepresentable {
        BLOCK(0), WALL(0), FENCE(6 / 16.0), BARS(7 / 16.0);

        public final double inset;

        Mount(double inset) {
            this.inset = inset;
        }

        static Mount of(BlockState holder) {
            return holder.getBlock() instanceof WallBlock ? WALL
                    : holder.getBlock() instanceof FenceBlock ? FENCE
                    : holder.getBlock() instanceof IronBarsBlock ? BARS : BLOCK;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    private final int size;
    private final MapCodec<FlagBlock> codec;

    public FlagBlock(Properties properties, int size) {
        super(properties, false);
        this.size = size;
        this.codec = simpleCodec(p -> new FlagBlock(p, size));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(MOUNT, Mount.BLOCK));
    }

    public int size() {
        return size;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, MOUNT);
    }

    /** On a block's side if one was clicked and it holds; otherwise on the ground. */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState ground = super.getStateForPlacement(context).setValue(FACING, Direction.UP);
        Direction face = context.getClickedFace();
        if (face.getAxis().isHorizontal()) {
            BlockPos pos = context.getClickedPos();
            BlockState side = ground.setValue(FACING, face)
                    .setValue(MOUNT, Mount.of(context.getLevel().getBlockState(pos.relative(face.getOpposite()))));
            if (side.canSurvive(context.getLevel(), pos)) {
                return side;
            }
        }
        return ground.canSurvive(context.getLevel(), context.getClickedPos()) ? ground : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return facing == Direction.UP ? super.canSurvive(state, level, pos)
                : level.getBlockState(pos.relative(facing.getOpposite())).isSolid();
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
                                     BlockPos pos, BlockPos facingPos) {
        Direction holder = state.getValue(FACING).getOpposite();
        if (holder.getAxis().isHorizontal() && facing == holder) {
            return state.canSurvive(level, pos) ? state.setValue(MOUNT, Mount.of(facingState)) : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    /**
     * On a wall, the face toward it is full, so the wall joins up to the flag (walls join sturdy faces; that is cached
     * per state, hence the mount in the state). Fences and bars get no such face, so they do not reach out.
     */
    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(MOUNT) != Mount.WALL || state.getValue(FACING) == Direction.UP) {
            return Shapes.empty();
        }
        return switch (state.getValue(FACING).getOpposite()) {
            case WEST -> Block.box(0, 0, 0, 1, 16, 16);
            case EAST -> Block.box(15, 0, 0, 16, 16, 16);
            case NORTH -> Block.box(0, 0, 0, 16, 16, 1);
            default -> Block.box(0, 0, 15, 16, 16, 16);
        };
    }

    @Override
    protected BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        BlockState turned = super.rotate(state, rotation);
        Direction facing = state.getValue(FACING);
        return facing == Direction.UP ? turned : turned.setValue(FACING, rotation.rotate(facing));
    }

    @Override
    protected BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        BlockState mirrored = super.mirror(state, mirror);
        Direction facing = state.getValue(FACING);
        return facing == Direction.UP ? mirrored : mirrored.setValue(FACING, mirror.mirror(facing));
    }
}
