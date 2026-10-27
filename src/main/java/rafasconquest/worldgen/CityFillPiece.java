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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * Reclaims shallow water inside a city block: fills each water column (up to {@link #MAX_DEPTH} deep) with earth up
 * to the water surface, so the block can be built on. Deeper water is left alone.
 */
public class CityFillPiece extends StructurePiece {
    public static final int MAX_DEPTH = 24;

    private final CityVariant variant;

    public CityFillPiece(BoundingBox box, CityVariant variant) {
        super(ConquestWorldgen.CITY_FILL_PIECE.get(), 0, box);
        this.variant = variant;
        setOrientation(null);
    }

    public CityFillPiece(CompoundTag tag) {
        super(ConquestWorldgen.CITY_FILL_PIECE.get(), tag);
        this.variant = CityVariant.valueOf(tag.getString("variant"));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("variant", variant.name());
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                reclaim(level, x, z, variant.ground());
            }
        }
    }

    /**
     * Fills the water column at (x, z) with earth, topped with {@code top}, if it is no deeper than MAX_DEPTH.
     * Returns the y of the new top block, or Integer.MIN_VALUE if the column was dry or too deep.
     */
    static int reclaim(WorldGenLevel level, int x, int z, BlockState top) {
        int surface = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, surface, z);
        if (level.getBlockState(pos).getFluidState().isEmpty()) {
            return Integer.MIN_VALUE;
        }
        int bottom = surface;
        while (bottom > level.getMinBuildHeight() && !level.getBlockState(pos.setY(bottom - 1)).getFluidState().isEmpty()) {
            bottom--;
            if (surface - bottom >= MAX_DEPTH) {
                return Integer.MIN_VALUE;
            }
        }
        for (int y = bottom; y < surface; y++) {
            level.setBlock(pos.setY(y), Blocks.DIRT.defaultBlockState(), 2);
        }
        level.setBlock(pos.setY(surface), top, 2);
        return surface;
    }
}
