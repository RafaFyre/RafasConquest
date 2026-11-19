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
    /**
     * The engines it can hold: the built-in ones that shoot, named as their {@link Machine}s, so a block state has a fixed set.
     * shortcut: an engine added by a file can't stand on the block; upgrade to a block entity holding its id if one should.
     */
    public enum Weapon implements net.minecraft.util.StringRepresentable {
        CULVERIN, SERPENTINE, MONS_MEG, HWACHA, ARCBALLISTA, ONAGER, TREBUCHET, TOWER_CROSSBOW;

        public Machine machine() {
            return Machine.byId(getSerializedName());
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    public static final EnumProperty<Weapon> WEAPON = EnumProperty.create("weapon", Weapon.class);
    /** Its engine stands in its place: the block is then gone to the eye and the hand, the engine where it was. */
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty ARMED =
            net.minecraft.world.level.block.state.properties.BooleanProperty.create("armed");

    public ArtilleryBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WEAPON, Weapon.CULVERIN).setValue(ARMED, false));
    }

    @Override
    protected net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
        return state.getValue(ARMED) ? net.minecraft.world.level.block.RenderShape.INVISIBLE : net.minecraft.world.level.block.RenderShape.MODEL;
    }

    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos,
                                                                 net.minecraft.world.phys.shapes.CollisionContext context) {
        // Armed, clicks and the gun's own footing go to the engine standing in its place.
        return state.getValue(ARMED) ? net.minecraft.world.phys.shapes.Shapes.empty() : super.getShape(state, level, pos, context);
    }

    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos,
                                                                          net.minecraft.world.phys.shapes.CollisionContext context) {
        return state.getValue(ARMED) ? net.minecraft.world.phys.shapes.Shapes.empty() : super.getCollisionShape(state, level, pos, context);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WEAPON, ARMED);
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
