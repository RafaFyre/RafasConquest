package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/** Placeholder Capital: a small stone-brick keep with a doorway on the north side and crenellations on top. */
public class CapitalPiece extends PlaceholderPiece {
    private static final int WALL_HEIGHT = 5;
    private static final int HEIGHT = WALL_HEIGHT + 2;

    public CapitalPiece(BlockPos surface) {
        super(ConquestWorldgen.CAPITAL_PIECE.get(), surface, HEIGHT);
    }

    public CapitalPiece(CompoundTag tag) {
        super(ConquestWorldgen.CAPITAL_PIECE.get(), tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockState bricks = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int dx = -HALF; dx <= HALF; dx++) {
            for (int dz = -HALF; dz <= HALF; dz++) {
                boolean edge = Math.abs(dx) == HALF || Math.abs(dz) == HALF;
                foundation(level, chunkBox, dx, dz, Blocks.COBBLESTONE.defaultBlockState());
                set(level, chunkBox, dx, 0, dz, bricks);
                for (int dy = 1; dy <= WALL_HEIGHT; dy++) {
                    boolean doorway = dx == 0 && dz == -HALF && dy <= 2;
                    boolean window = dy == 3 && edge && (dx == 0 || dz == 0) && !(dx == 0 && dz == -HALF);
                    set(level, chunkBox, dx, dy, dz, edge && !doorway && !window ? bricks : air);
                }
                set(level, chunkBox, dx, WALL_HEIGHT + 1, dz, bricks);
                boolean merlon = edge && (dx + dz) % 2 == 0;
                set(level, chunkBox, dx, WALL_HEIGHT + 2, dz, merlon ? bricks : air);
            }
        }
    }
}
