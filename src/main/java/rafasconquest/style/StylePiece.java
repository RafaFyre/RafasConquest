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

/**
 * A structure from a style's folder ({@link StyleLibrary}), its floor (the template's y = 0) on the ground at (x, z),
 * centred there and turned a number of quarter turns. Its Character Markers are armed as it is built: they become the
 * people they name once their chunk is in the world.
 */
public class StylePiece extends StructurePiece {
    private final String file;
    private final BlockPos centre;
    private final int turns;

    /** The piece, or null if its file can't be read. */
    @Nullable
    public static StylePiece of(String file, int x, int y, int z, int turns) {
        StructureTemplate template = StyleLibrary.template(file);
        return template == null ? null : new StylePiece(file, new BlockPos(x, y, z), turns, template.getSize());
    }

    private StylePiece(String file, BlockPos centre, int turns, Vec3i size) {
        super(ConquestWorldgen.STYLE_PIECE.get(), 0, box(centre, turns, size));
        this.file = file;
        this.centre = centre;
        this.turns = turns;
        setOrientation(null);
    }

    public StylePiece(CompoundTag tag) {
        super(ConquestWorldgen.STYLE_PIECE.get(), tag);
        this.file = tag.getString("file");
        this.centre = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        this.turns = tag.getInt("turns");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("file", file);
        tag.putInt("x", centre.getX());
        tag.putInt("y", centre.getY());
        tag.putInt("z", centre.getZ());
        tag.putInt("turns", turns);
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

    private static BoundingBox box(BlockPos centre, int turns, Vec3i size) {
        return boxOf(origin(centre, size), size, settings(turns, size));
    }

    private static BoundingBox boxOf(BlockPos origin, Vec3i size, StructurePlaceSettings settings) {
        BlockPos a = StructureTemplate.calculateRelativePosition(settings, BlockPos.ZERO).offset(origin);
        BlockPos b = StructureTemplate.calculateRelativePosition(settings, new BlockPos(size.getX() - 1, size.getY() - 1, size.getZ() - 1)).offset(origin);
        return BoundingBox.fromCorners(a, b);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunk, ChunkPos chunkPos, BlockPos pivot) {
        StructureTemplate template = StyleLibrary.template(file);
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
