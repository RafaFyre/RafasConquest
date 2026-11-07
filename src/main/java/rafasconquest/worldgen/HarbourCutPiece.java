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
import rafasconquest.core.CityLayout;
import rafasconquest.core.HarbourCut;

/**
 * One stretch of a port's {@link HarbourCut}: clears everything above the cut's floor, water below sea level and air
 * above it, out to where each column's lane met the sea ({@code ends}, blocks out past the wharf).
 */
public class HarbourCutPiece extends StructurePiece {
    private final int side;
    /** World (x, z) of the column 1 out past the wharf at the stretch's first lane. */
    private final int lineX;
    private final int lineZ;
    private final int plate;
    private final int[] ends;

    public HarbourCutPiece(BoundingBox box, CityLayout.Side side, int lineX, int lineZ, int plate, int[] ends) {
        super(ConquestWorldgen.HARBOUR_CUT_PIECE.get(), 0, box);
        this.side = side.ordinal();
        this.lineX = lineX;
        this.lineZ = lineZ;
        this.plate = plate;
        this.ends = ends;
        setOrientation(null);
    }

    public HarbourCutPiece(CompoundTag tag) {
        super(ConquestWorldgen.HARBOUR_CUT_PIECE.get(), tag);
        this.side = tag.getInt("side");
        this.lineX = tag.getInt("lineX");
        this.lineZ = tag.getInt("lineZ");
        this.plate = tag.getInt("plate");
        this.ends = tag.getIntArray("ends");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("side", side);
        tag.putInt("lineX", lineX);
        tag.putInt("lineZ", lineZ);
        tag.putInt("plate", plate);
        tag.putIntArray("ends", ends);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        int sea = generator.getSeaLevel();
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                int[] ad = alongAndOut(x, z);
                if (ad[0] < 0 || ad[0] >= ends.length || ad[1] < 1 || ad[1] > ends[ad[0]]) {
                    continue;
                }
                int floor = HarbourCut.floor(plate, ad[1]);
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                for (int y = top; y > floor; y--) {
                    level.setBlock(pos.set(x, y, z), y < sea ? water : air, 2);
                }
            }
        }
    }

    /** {index into ends, blocks out past the wharf} of world (x, z). */
    private int[] alongAndOut(int x, int z) {
        return switch (CityLayout.Side.values()[side]) {
            case NORTH -> new int[]{x - lineX, 1 + lineZ - z};
            case SOUTH -> new int[]{x - lineX, 1 + z - lineZ};
            case WEST -> new int[]{z - lineZ, 1 + lineX - x};
            case EAST -> new int[]{z - lineZ, 1 + x - lineX};
        };
    }
}
