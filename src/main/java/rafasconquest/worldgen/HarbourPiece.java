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

/**
 * A port city's wooden deck ({@link Harbour}): a wharf along its harbour wall, or a pier out over the water. Spruce
 * planks at {@code deckY} with headroom cleared above, log posts down to the ground every few blocks along its long
 * edges, and on a pier, lanterns on fence posts.
 */
public class HarbourPiece extends StructurePiece {
    /** How far down a post may reach for the sea floor. */
    static final int DEPTH = 48;
    private static final int POST_EVERY = 4;
    private static final int LANTERN_EVERY = 8;
    private static final int HEADROOM = 3;

    private final int deckY;
    private final boolean pier;

    public HarbourPiece(int minX, int minZ, int maxX, int maxZ, int deckY, boolean pier) {
        super(ConquestWorldgen.HARBOUR_PIECE.get(), 0, new BoundingBox(minX, deckY - DEPTH, minZ, maxX, deckY + HEADROOM, maxZ));
        this.deckY = deckY;
        this.pier = pier;
        setOrientation(null);
    }

    public HarbourPiece(CompoundTag tag) {
        super(ConquestWorldgen.HARBOUR_PIECE.get(), tag);
        this.deckY = tag.getInt("deck");
        this.pier = tag.getBoolean("pier");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("deck", deckY);
        tag.putBoolean("pier", pier);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BoundingBox b = boundingBox;
        // The long edges run along x when the deck is longer that way.
        boolean alongX = b.getXSpan() >= b.getZSpan();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState deck = Blocks.SPRUCE_PLANKS.defaultBlockState();
        for (int x = Math.max(b.minX(), chunkBox.minX()); x <= Math.min(b.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(b.minZ(), chunkBox.minZ()); z <= Math.min(b.maxZ(), chunkBox.maxZ()); z++) {
                level.setBlock(pos.set(x, deckY, z), deck, 2);
                for (int y = deckY + 1; y <= deckY + HEADROOM; y++) {
                    level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
                }
                boolean edge = alongX ? z == b.minZ() || z == b.maxZ() : x == b.minX() || x == b.maxX();
                int run = alongX ? x - b.minX() : z - b.minZ();
                int length = alongX ? b.getXSpan() : b.getZSpan();
                if (!edge) {
                    continue;
                }
                if (run % POST_EVERY == 0 || run == length - 1) {
                    for (int y = deckY - 1; y > deckY - DEPTH; y--) {
                        BlockState below = level.getBlockState(pos.set(x, y, z));
                        if (!below.isAir() && below.getFluidState().isEmpty()) {
                            break;
                        }
                        level.setBlock(pos, Blocks.SPRUCE_LOG.defaultBlockState(), 2);
                    }
                }
                if (pier && run % LANTERN_EVERY == LANTERN_EVERY / 2) {
                    level.setBlock(pos.set(x, deckY + 1, z), Blocks.SPRUCE_FENCE.defaultBlockState(), 2);
                    level.setBlock(pos.set(x, deckY + 2, z), Blocks.LANTERN.defaultBlockState(), 2);
                }
            }
        }
    }
}
