package rafasconquest.style;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import rafasconquest.worldgen.ConquestWorldgen;

import java.util.ArrayList;
import java.util.List;

/**
 * A structure from a style's folder ({@link StyleLibrary}), its floor (the template's y = 0) on the ground at (x, z),
 * centred there and turned a number of quarter turns. A big structure is built as one piece per tile of it
 * ({@link StyleLibrary#TILE} blocks a side), so each chunk only walks the blocks of the tiles it holds. Its Character
 * Markers are armed as it is built: they become the people they name once their chunk is in the world.
 */
public class StylePiece extends StructurePiece {
    private final String file;
    private final BlockPos centre;
    private final int turns;
    /** The tile of the file this piece builds; -1, -1 for a file of one tile. */
    private final int tileX;
    private final int tileZ;

    /** The pieces of a structure centred on (x, z); none if its file can't be read. */
    public static List<StylePiece> of(String file, int x, int y, int z, int turns) {
        Vec3i size = StyleLibrary.size(file);
        y += StyleLibrary.yOffset(file);
        return size == null ? List.of() : tiles(file, new BlockPos(x, y, z), turns, size);
    }

    /** The pieces of a structure with its footprint's corner (whichever way it is turned) at (minX, minZ). */
    public static List<StylePiece> at(String file, int minX, int y, int minZ, int turns) {
        Vec3i size = StyleLibrary.size(file);
        if (size == null) {
            return List.of();
        }
        y += StyleLibrary.yOffset(file);
        BoundingBox atZero = box(new BlockPos(0, y, 0), turns, size, -1, -1);
        return tiles(file, new BlockPos(minX - atZero.minX(), y, minZ - atZero.minZ()), turns, size);
    }

    private static List<StylePiece> tiles(String file, BlockPos centre, int turns, Vec3i size) {
        int t = StyleLibrary.TILE;
        if (size.getX() <= t && size.getZ() <= t) {
            return List.of(new StylePiece(file, centre, turns, size, -1, -1));
        }
        List<StylePiece> tiles = new ArrayList<>();
        for (int i = 0; i * t < size.getX(); i++) {
            for (int j = 0; j * t < size.getZ(); j++) {
                tiles.add(new StylePiece(file, centre, turns, size, i, j));
            }
        }
        return tiles;
    }

    private StylePiece(String file, BlockPos centre, int turns, Vec3i size, int tileX, int tileZ) {
        super(ConquestWorldgen.STYLE_PIECE.get(), 0, box(centre, turns, size, tileX, tileZ));
        this.file = file;
        this.centre = centre;
        this.turns = turns;
        this.tileX = tileX;
        this.tileZ = tileZ;
        setOrientation(null);
    }

    public StylePiece(CompoundTag tag) {
        super(ConquestWorldgen.STYLE_PIECE.get(), tag);
        this.file = tag.getString("file");
        this.centre = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        this.turns = tag.getInt("turns");
        this.tileX = tag.contains("tileX") ? tag.getInt("tileX") : -1;
        this.tileZ = tag.contains("tileZ") ? tag.getInt("tileZ") : -1;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("file", file);
        tag.putInt("x", centre.getX());
        tag.putInt("y", centre.getY());
        tag.putInt("z", centre.getZ());
        tag.putInt("turns", turns);
        tag.putInt("tileX", tileX);
        tag.putInt("tileZ", tileZ);
    }

    private static Rotation rotation(int turns) {
        return Rotation.values()[Math.floorMod(turns, 4)];
    }

    /** Where the template's corner goes, so that its middle stands at the centre whichever way it is turned. */
    private static BlockPos origin(BlockPos centre, Vec3i size) {
        return centre.offset(-size.getX() / 2, 0, -size.getZ() / 2);
    }

    private static StructurePlaceSettings settings(int turns, Vec3i size) {
        return new StructurePlaceSettings().setRotation(rotation(turns)).setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));
    }

    /** The box of the whole structure, or of one tile of it. */
    private static BoundingBox box(BlockPos centre, int turns, Vec3i size, int tileX, int tileZ) {
        int t = StyleLibrary.TILE;
        BlockPos lo = tileX < 0 ? BlockPos.ZERO : new BlockPos(tileX * t, 0, tileZ * t);
        BlockPos hi = tileX < 0 ? new BlockPos(size.getX() - 1, size.getY() - 1, size.getZ() - 1)
                : new BlockPos(Math.min((tileX + 1) * t, size.getX()) - 1, size.getY() - 1, Math.min((tileZ + 1) * t, size.getZ()) - 1);
        StructurePlaceSettings settings = settings(turns, size);
        BlockPos origin = origin(centre, size);
        return BoundingBox.fromCorners(StructureTemplate.calculateRelativePosition(settings, lo).offset(origin),
                StructureTemplate.calculateRelativePosition(settings, hi).offset(origin));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunk, ChunkPos chunkPos, BlockPos pivot) {
        StructureTemplate template = StyleLibrary.tile(file, Math.max(0, tileX), Math.max(0, tileZ));
        if (template == null) {
            return;
        }
        Vec3i size = template.getSize();
        BlockPos origin = origin(centre, size);
        StructurePlaceSettings settings = settings(turns, size).setBoundingBox(chunk).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        template.placeInWorld(level, origin, origin, settings, random, 2);
        CharacterMarkers.arm(level, template, origin, settings, chunk);
    }
}
