package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Direction;

/** Placeholder Farm: a fenced wheat plot around a water source, with a gate on the north side. */
public class FarmPiece extends PlaceholderPiece {
    private static final int HEIGHT = 3;

    public FarmPiece(BlockPos surface) {
        super(ConquestWorldgen.FARM_PIECE.get(), surface, HEIGHT);
    }

    public FarmPiece(CompoundTag tag) {
        super(ConquestWorldgen.FARM_PIECE.get(), tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState farmland = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE);
        for (int dx = -HALF; dx <= HALF; dx++) {
            for (int dz = -HALF; dz <= HALF; dz++) {
                boolean edge = Math.abs(dx) == HALF || Math.abs(dz) == HALF;
                foundation(level, chunkBox, dx, dz, Blocks.DIRT.defaultBlockState());
                for (int dy = 1; dy <= HEIGHT; dy++) {
                    set(level, chunkBox, dx, dy, dz, Blocks.AIR.defaultBlockState());
                }
                if (edge) {
                    set(level, chunkBox, dx, 0, dz, Blocks.COARSE_DIRT.defaultBlockState());
                    if (dx == 0 && dz == -HALF) {
                        set(level, chunkBox, dx, 1, dz, Blocks.OAK_FENCE_GATE.defaultBlockState().setValue(FenceGateBlock.FACING, Direction.NORTH));
                    } else {
                        set(level, chunkBox, dx, 1, dz, fence
                                .setValue(FenceBlock.NORTH, isEdge(dx, dz - 1))
                                .setValue(FenceBlock.SOUTH, isEdge(dx, dz + 1))
                                .setValue(FenceBlock.WEST, isEdge(dx - 1, dz))
                                .setValue(FenceBlock.EAST, isEdge(dx + 1, dz)));
                    }
                } else if (dx == 0 && dz == 0) {
                    set(level, chunkBox, dx, 0, dz, Blocks.WATER.defaultBlockState());
                } else {
                    set(level, chunkBox, dx, 0, dz, farmland);
                    set(level, chunkBox, dx, 1, dz, ((CropBlock) Blocks.WHEAT).getStateForAge(random.nextInt(CropBlock.MAX_AGE + 1)));
                }
            }
        }
    }

    private static boolean isEdge(int dx, int dz) {
        return Math.abs(dx) <= HALF && Math.abs(dz) <= HALF && (Math.abs(dx) == HALF || Math.abs(dz) == HALF);
    }
}
