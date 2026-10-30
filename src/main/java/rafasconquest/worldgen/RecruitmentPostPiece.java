package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import rafasconquest.core.RecruitmentPost;

/**
 * A city's recruitment post ({@link RecruitmentPost}), built in code for now: a stone house on the city's plate, a
 * stone brick floor, cobblestone walls on stone brick corners with windows, an oak door to the west, a stone brick slab
 * roof, lanterns, and the bedrock counter the recruiter stands behind. Its guards and recruiter are set there when its
 * chunk first loads ({@code sim.RecruitmentPosts}).
 */
public class RecruitmentPostPiece extends StructurePiece {
    private static final int WALL_HEIGHT = 4;

    private final int plotX;
    private final int plotZ;
    /** The city's plate: the floor stands on it. */
    private final int floor;

    public RecruitmentPostPiece(RecruitmentPost post, int plate) {
        super(ConquestWorldgen.RECRUITMENT_POST_PIECE.get(), 0,
                new BoundingBox(post.x0(), plate, post.z0(), post.x1(), plate + WALL_HEIGHT + 1, post.z1()));
        this.plotX = post.plotX();
        this.plotZ = post.plotZ();
        this.floor = plate;
        setOrientation(null);
    }

    public RecruitmentPostPiece(CompoundTag tag) {
        super(ConquestWorldgen.RECRUITMENT_POST_PIECE.get(), tag);
        this.plotX = tag.getInt("plotX");
        this.plotZ = tag.getInt("plotZ");
        this.floor = tag.getInt("floor");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("plotX", plotX);
        tag.putInt("plotZ", plotZ);
        tag.putInt("floor", floor);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunk, ChunkPos chunkPos, BlockPos pivot) {
        RecruitmentPost post = new RecruitmentPost(plotX, plotZ);
        BlockState bricks = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        for (int x = post.x0(); x <= post.x1(); x++) {
            for (int z = post.z0(); z <= post.z1(); z++) {
                boolean wallX = x == post.x0() || x == post.x1();
                boolean wallZ = z == post.z0() || z == post.z1();
                set(level, chunk, x, floor, z, bricks);
                for (int y = floor + 1; y <= floor + WALL_HEIGHT; y++) {
                    BlockState state;
                    if (wallX && wallZ) {
                        state = bricks;
                    } else if (wallX || wallZ) {
                        boolean window = y == floor + 2 && ((x - post.x0()) % 3 == 2 || (z - post.z0()) % 3 == 2)
                                && !(x == post.door()[0] && Math.abs(z - post.door()[1]) <= 1);
                        state = window ? Blocks.GLASS_PANE.defaultBlockState() : cobble;
                    } else {
                        state = Blocks.AIR.defaultBlockState();
                    }
                    set(level, chunk, x, y, z, state);
                }
                set(level, chunk, x, floor + WALL_HEIGHT + 1, z,
                        Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
            }
        }
        int[] door = post.door();
        set(level, chunk, door[0], floor + 1, door[1], Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        set(level, chunk, door[0], floor + 2, door[1], Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        int[] counter = post.counter();
        set(level, chunk, counter[0], floor + 1, counter[1], Blocks.BEDROCK.defaultBlockState());
        // Lanterns hung from the ceiling, either side of the counter.
        BlockState lantern = Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
        set(level, chunk, counter[0], floor + WALL_HEIGHT, counter[1] - 3, lantern);
        set(level, chunk, counter[0], floor + WALL_HEIGHT, counter[1] + 3, lantern);
    }

    private static void set(WorldGenLevel level, BoundingBox chunk, int x, int y, int z, BlockState state) {
        BlockPos pos = new BlockPos(x, y, z);
        if (chunk.isInside(pos)) {
            level.setBlock(pos, state, 2);
        }
    }
}
