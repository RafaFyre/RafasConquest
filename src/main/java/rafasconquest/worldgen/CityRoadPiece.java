package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
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

/**
 * A stretch of city ground laid at its own height column by column ({@code ys}, x-major over the box): a road of gravel,
 * or cobblestone where the noble district's, the 4 columns before each 1-block step up along it slabs, half a step; or,
 * with no slab, the flat ground a building stands on. Each column is laid on solid ground: land up to 3 blocks above
 * it is cleared and gaps up to 3 below it filled, as the tile it crosses may stand a step or two off.
 */
public class CityRoadPiece extends StructurePiece {
    /** How far above and below its height the piece reshapes the land: past the steps tiles may stand apart. */
    private static final int REACH = 3;

    private final BlockState surface;
    /** The slab easing each step, or air for none. */
    private final BlockState slab;
    private final int[] ys;

    public CityRoadPiece(BoundingBox box, BlockState surface, BlockState slab, int[] ys) {
        super(ConquestWorldgen.CITY_ROAD_PIECE.get(), 0, box);
        this.surface = surface;
        this.slab = slab;
        this.ys = ys;
        setOrientation(null);
    }

    /** A flat road over {@code box} at its foot: cobblestone where {@code paved}, else gravel. */
    public static CityRoadPiece flat(BoundingBox box, boolean paved) {
        int[] ys = new int[box.getXSpan() * box.getZSpan()];
        java.util.Arrays.fill(ys, box.minY());
        return new CityRoadPiece(box, (paved ? Blocks.COBBLESTONE : Blocks.GRAVEL).defaultBlockState(), Blocks.AIR.defaultBlockState(), ys);
    }

    public CityRoadPiece(CompoundTag tag) {
        super(ConquestWorldgen.CITY_ROAD_PIECE.get(), tag);
        var blocks = BuiltInRegistries.BLOCK.asLookup();
        if (tag.contains("surface")) {
            this.surface = NbtUtils.readBlockState(blocks, tag.getCompound("surface"));
            this.slab = NbtUtils.readBlockState(blocks, tag.getCompound("slab"));
            this.ys = tag.getIntArray("ys");
        } else { // an older save: a flat road at its box's foot
            this.surface = (tag.getBoolean("paved") ? Blocks.COBBLESTONE : Blocks.GRAVEL).defaultBlockState();
            this.slab = Blocks.AIR.defaultBlockState();
            this.ys = new int[boundingBox.getXSpan() * boundingBox.getZSpan()];
            java.util.Arrays.fill(ys, boundingBox.minY());
        }
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.put("surface", NbtUtils.writeBlockState(surface));
        tag.put("slab", NbtUtils.writeBlockState(slab));
        tag.putIntArray("ys", ys);
    }

    private int y(int x, int z) {
        return ys[(x - boundingBox.minX()) * boundingBox.getZSpan() + z - boundingBox.minZ()];
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                int y = y(x, z);
                for (int up = y + REACH; up > y; up--) {
                    if (isLand(level.getBlockState(pos.set(x, up, z)))) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
                for (int down = y - 1; down >= y - REACH && level.getBlockState(pos.set(x, down, z)).canBeReplaced(); down--) {
                    level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
                }
                level.setBlock(pos.set(x, y, z), surface, 2);
                if (!slab.isAir() && higherBeside(x, z, y)) {
                    level.setBlock(pos.set(x, y + 1, z), slab, 2); // half a step up to the next tile, walked without a jump
                }
            }
        }
    }

    /**
     * Whether a column of this piece within {@link #SLAB_RUN} along a straight line from (x, z) stands one higher: the
     * half of an 8-block tile nearest the next one up is slabs, so a tile is 4 blocks, 4 slabs, then the next tile.
     */
    private boolean higherBeside(int x, int z, int y) {
        int[][] around = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : around) {
            for (int k = 1; k <= SLAB_RUN; k++) {
                int nx = x + d[0] * k;
                int nz = z + d[1] * k;
                if (nx < boundingBox.minX() || nx > boundingBox.maxX() || nz < boundingBox.minZ() || nz > boundingBox.maxZ() || y(nx, nz) != y) {
                    if (nx >= boundingBox.minX() && nx <= boundingBox.maxX() && nz >= boundingBox.minZ() && nz <= boundingBox.maxZ() && y(nx, nz) == y + 1) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    /** Slabs leading up to each step: half an 8-block tile. */
    private static final int SLAB_RUN = 4;

    /** Natural ground (or what the city laid as ground), never a building. */
    private static boolean isLand(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(Blocks.GRAVEL) || state.is(Blocks.SNOW_BLOCK) || state.is(BlockTags.TERRACOTTA);
    }
}
