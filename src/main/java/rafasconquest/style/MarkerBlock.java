package rafasconquest.style;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import rafasconquest.content.ConquestContent;
import rafasconquest.net.ConquestNetwork;

/**
 * The Character Marker: blue bedrock, set in a structure where someone is to stand ({@link MarkerSpec}), facing the way
 * they will face. Right-clicked in creative, it opens its settings. One placed by hand only shows what it will become;
 * one built by world generation is armed: unseen and not in the way, it becomes its people once its chunk is in the
 * world ({@link CharacterMarkers#fire}), then the block its settings name.
 */
public class MarkerBlock extends BaseEntityBlock {
    private static final com.mojang.serialization.MapCodec<MarkerBlock> CODEC = simpleCodec(MarkerBlock::new);

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /** Each player's last saved settings, given to the next marker they place. */
    // shortcut: kept in memory only, so a server restart forgets it; move to player data if that matters
    public static final java.util.Map<java.util.UUID, MarkerSpec> LAST = new java.util.HashMap<>();

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ARMED = BooleanProperty.create("armed");

    public MarkerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ARMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ARMED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer != null && LAST.containsKey(placer.getUUID()) && level.getBlockEntity(pos) instanceof MarkerBlockEntity marker) {
            marker.setSpec(LAST.get(placer.getUUID()));
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(ARMED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(ARMED) ? Shapes.empty() : Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(ARMED) ? Shapes.empty() : Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isCreative()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide && player instanceof ServerPlayer server && level.getBlockEntity(pos) instanceof MarkerBlockEntity marker) {
            ConquestNetwork.openMarker(server, pos, marker.spec());
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MarkerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide || !state.getValue(ARMED) ? null
                : createTickerHelper(type, ConquestContent.MARKER_ENTITY.get(), MarkerBlockEntity::serverTick);
    }

    /** Facing, as a yaw for whoever stands on it. */
    static float yaw(BlockState state) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
    }
}
