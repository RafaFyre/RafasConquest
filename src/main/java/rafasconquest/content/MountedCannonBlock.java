package rafasconquest.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Set;
import java.util.UUID;

/**
 * The Mounted Light Cannon: a block of planks with a culverin's barrel on it, no wheels, no carriage, swinging up to
 * {@link #ARC} degrees either side of the way the block faces. Its gun is a Siegeworks culverin in all else, made, held
 * and crewed by its {@link ArtilleryMount} as an Artillery Block's is; the block stays in sight under it. The culverin
 * stands on the block's low {@link #FOOT}, so its barrel (12 px above its feet) lies on the planks' top.
 */
public class MountedCannonBlock extends ArtilleryBlock {
    private static final MapCodec<MountedCannonBlock> CODEC = simpleCodec(MountedCannonBlock::new);
    /** How far it turns either side of its facing, in degrees. */
    public static final float ARC = 30;
    /** Its culverin's footing, blocks above its bottom. */
    public static final double FOOT = 4 / 16.0;
    private static final VoxelShape FOOTING = Block.box(0, 0, 0, 16, 4, 16);
    /** The culverins standing on one, by UUID: drawn barrel only (FieldGunMountedMixin). */
    public static final Set<UUID> MOUNTED = java.util.concurrent.ConcurrentHashMap.newKeySet();

    public MountedCannonBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FOOTING;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS; // its gun is always a culverin
    }
}
