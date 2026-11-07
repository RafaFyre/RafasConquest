package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * A straight city road segment, laid into the surface of the flattened plate (its bounding box's y): gravel, or
 * cobblestone where {@code paved} (the noble district's).
 */
public class CityRoadPiece extends StructurePiece {
    private final boolean paved;

    public CityRoadPiece(BoundingBox box, boolean paved) {
        super(ConquestWorldgen.CITY_ROAD_PIECE.get(), 0, box);
        this.paved = paved;
        setOrientation(null);
    }

    public CityRoadPiece(CompoundTag tag) {
        super(ConquestWorldgen.CITY_ROAD_PIECE.get(), tag);
        this.paved = tag.getBoolean("paved");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putBoolean("paved", paved);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int y = boundingBox.minY();
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                level.setBlock(pos.set(x, y, z), (paved ? Blocks.COBBLESTONE : Blocks.GRAVEL).defaultBlockState(), 2);
            }
        }
    }
}
