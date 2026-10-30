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
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import rafasconquest.core.CityLayout;

/**
 * A city's gold church ({@link GoldChurch}): vanilla's plains temple with its cobblestone turned to gold blocks (its
 * stairs and slabs to smooth sandstone ones, so steps stay steps). The hold's lord stands inside, set there when its
 * chunk first loads ({@code sim.RecruitmentPosts}).
 */
public class GoldChurchPiece extends StructurePiece {
    private final int plotX;
    private final int plotZ;
    private final int floor;

    public GoldChurchPiece(int plotX, int plotZ, int plate) {
        super(ConquestWorldgen.GOLD_CHURCH_PIECE.get(), 0,
                new BoundingBox(plotX, plate - 1, plotZ, plotX + CityLayout.PLOT_SIZE - 1, plate + 16, plotZ + CityLayout.PLOT_SIZE - 1));
        this.plotX = plotX;
        this.plotZ = plotZ;
        this.floor = plate;
        setOrientation(null);
    }

    public GoldChurchPiece(CompoundTag tag) {
        super(ConquestWorldgen.GOLD_CHURCH_PIECE.get(), tag);
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
        GoldChurch.layout(level.getLevel().getServer().getStructureManager(), plotX, plotZ, floor).ifPresent(church -> {
            StructurePlaceSettings settings = church.settings().setBoundingBox(chunk).addProcessor(JigsawReplacementProcessor.INSTANCE);
            church.template().placeInWorld(level, church.origin(), church.origin(), settings, random, 2);
            BoundingBox box = church.box();
            for (int x = Math.max(box.minX(), chunk.minX()); x <= Math.min(box.maxX(), chunk.maxX()); x++) {
                for (int z = Math.max(box.minZ(), chunk.minZ()); z <= Math.min(box.maxZ(), chunk.maxZ()); z++) {
                    for (int y = box.minY(); y <= box.maxY(); y++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState gold = gilded(level.getBlockState(pos));
                        if (gold != null) {
                            level.setBlock(pos, gold, 2);
                        }
                    }
                }
            }
        });
    }

    /** What a block of the temple becomes in gold, or null if it stays as it is. */
    private static BlockState gilded(BlockState state) {
        if (state.is(Blocks.COBBLESTONE) || state.is(Blocks.MOSSY_COBBLESTONE) || state.is(Blocks.COBBLESTONE_WALL)) {
            return Blocks.GOLD_BLOCK.defaultBlockState();
        }
        if (state.is(Blocks.COBBLESTONE_STAIRS)) {
            return Blocks.SMOOTH_SANDSTONE_STAIRS.withPropertiesOf(state);
        }
        if (state.is(Blocks.COBBLESTONE_SLAB)) {
            return Blocks.SMOOTH_SANDSTONE_SLAB.withPropertiesOf(state);
        }
        return null;
    }
}
