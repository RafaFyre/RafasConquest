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
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.List;

/**
 * The stone path from a city's gate up to its outside noble district's ({@link rafasconquest.core.NobleAnnex}): each
 * column stone, climbing in half blocks (a stone slab on the half), on stone down to the ground, with the air cleared
 * above it.
 */
public class NoblePathPiece extends StructurePiece {
    /** Air kept above the path. */
    private static final int HEADROOM = 4;

    /** World x, z and height above the plate in half blocks, one triple per column. */
    private final int[] columns;
    private final int plate;

    public NoblePathPiece(List<int[]> worldColumns, int plate) {
        super(ConquestWorldgen.NOBLE_PATH_PIECE.get(), 0, box(worldColumns, plate));
        this.columns = new int[worldColumns.size() * 3];
        for (int i = 0; i < worldColumns.size(); i++) {
            System.arraycopy(worldColumns.get(i), 0, columns, i * 3, 3);
        }
        this.plate = plate;
        setOrientation(null);
    }

    public NoblePathPiece(CompoundTag tag) {
        super(ConquestWorldgen.NOBLE_PATH_PIECE.get(), tag);
        this.columns = tag.getIntArray("columns");
        this.plate = tag.getInt("plate");
    }

    private static BoundingBox box(List<int[]> columns, int plate) {
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE, top = 0;
        for (int[] c : columns) {
            minX = Math.min(minX, c[0]);
            minZ = Math.min(minZ, c[1]);
            maxX = Math.max(maxX, c[0]);
            maxZ = Math.max(maxZ, c[1]);
            top = Math.max(top, c[2] / 2 + 1);
        }
        return new BoundingBox(minX, plate - 64, minZ, maxX, plate + top + HEADROOM, maxZ);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putIntArray("columns", columns);
        tag.putInt("plate", plate);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState stone = Blocks.STONE.defaultBlockState();
        for (int i = 0; i + 2 < columns.length; i += 3) {
            int x = columns[i];
            int z = columns[i + 1];
            if (!chunkBox.isInside(x, chunkBox.minY(), z)) {
                continue;
            }
            int full = plate + columns[i + 2] / 2;
            boolean slab = columns[i + 2] % 2 == 1;
            int top = full + (slab ? 1 : 0);
            for (int y = top + 1; y <= top + HEADROOM; y++) {
                level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            }
            if (slab) {
                level.setBlock(pos.set(x, top, z), Blocks.STONE_SLAB.defaultBlockState(), 2);
            }
            // Stone from the walking height down to the ground under it.
            for (int y = full; y > level.getMinBuildHeight(); y--) {
                BlockState there = level.getBlockState(pos.set(x, y, z));
                if (y < full && !there.isAir() && there.getFluidState().isEmpty() && !there.canBeReplaced()) {
                    break;
                }
                level.setBlock(pos, stone, 2);
            }
        }
    }
}
