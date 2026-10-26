package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

/**
 * Code-built 9 × 9 placeholder centred on a surface block. Coordinates are relative to the centre, with y = 0 the
 * surface block itself. The bounding box fully encodes the piece, so nothing extra is saved.
 */
public abstract class PlaceholderPiece extends StructurePiece {
    protected static final int HALF = 4;
    private static final int MAX_FOUNDATION_DEPTH = 24;

    protected PlaceholderPiece(StructurePieceType type, BlockPos surface, int height) {
        super(type, 0, new BoundingBox(surface.getX() - HALF, surface.getY(), surface.getZ() - HALF,
                surface.getX() + HALF, surface.getY() + height, surface.getZ() + HALF));
        setOrientation(null);
    }

    protected PlaceholderPiece(StructurePieceType type, CompoundTag tag) {
        super(type, tag);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
    }

    protected BlockPos at(int dx, int dy, int dz) {
        return new BlockPos(boundingBox.minX() + HALF + dx, boundingBox.minY() + dy, boundingBox.minZ() + HALF + dz);
    }

    /** Places a block if it falls inside the chunk being decorated. */
    protected void set(WorldGenLevel level, BoundingBox chunkBox, int dx, int dy, int dz, BlockState state) {
        BlockPos pos = at(dx, dy, dz);
        if (chunkBox.isInside(pos)) {
            level.setBlock(pos, state, 2);
        }
    }

    /** Fills from just below (dx, dz) down to the first solid block, so the piece never floats. */
    protected void foundation(WorldGenLevel level, BoundingBox chunkBox, int dx, int dz, BlockState state) {
        for (int dy = -1; dy >= -MAX_FOUNDATION_DEPTH; dy--) {
            BlockPos pos = at(dx, dy, dz);
            if (!chunkBox.isInside(pos) || pos.getY() <= level.getMinBuildHeight()) {
                return;
            }
            BlockState existing = level.getBlockState(pos);
            if (!existing.isAir() && !existing.liquid() && !existing.canBeReplaced()) {
                return;
            }
            level.setBlock(pos, state, 2);
        }
    }
}
