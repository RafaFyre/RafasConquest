package rafasconquest.style;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockPackerTest {
    /** The sizes of a set of real houses, 10 to 32 blocks across. */
    private static final List<BlockPacker.Size> HOUSES = List.of(new BlockPacker.Size(15, 11), new BlockPacker.Size(17, 17),
            new BlockPacker.Size(23, 15), new BlockPacker.Size(30, 31), new BlockPacker.Size(12, 15), new BlockPacker.Size(13, 10),
            new BlockPacker.Size(32, 29), new BlockPacker.Size(10, 15), new BlockPacker.Size(25, 14), new BlockPacker.Size(18, 26));
    private static final int BLOCK = 29;

    @Test
    void housesStayInTheBlockWithoutOverlapping() {
        for (long seed = 0; seed < 200; seed++) {
            List<BlockPacker.Placed> placed = BlockPacker.pack(BLOCK, HOUSES, new Random(seed));
            assertFalse(placed.isEmpty(), "an empty block, seed " + seed);
            int[][] used = new int[BLOCK][BLOCK];
            for (BlockPacker.Placed p : placed) {
                BlockPacker.Size s = HOUSES.get(p.building());
                boolean turned = p.turns() % 2 == 1;
                int w = turned ? s.z() : s.x();
                int d = turned ? s.x() : s.z();
                assertTrue(p.x() >= 0 && p.z() >= 0 && p.x() + w <= BLOCK && p.z() + d <= BLOCK, "a house out of its block, seed " + seed);
                for (int x = p.x(); x < p.x() + w; x++) {
                    for (int z = p.z(); z < p.z() + d; z++) {
                        assertTrue(used[x][z]++ == 0, "houses overlapping, seed " + seed);
                    }
                }
            }
        }
    }

    @Test
    void aCutLeavesTheRestFreeAndNoneOfTheTaken() {
        int size = 100;
        BlockPacker.Rect taken = new BlockPacker.Rect(30, 40, 25, 20);
        List<BlockPacker.Rect> free = BlockPacker.cut(List.of(new BlockPacker.Rect(0, 0, size, size)), taken);
        int[][] used = new int[size][size];
        for (BlockPacker.Rect r : free) {
            for (int x = r.x(); x < r.x() + r.w(); x++) {
                for (int z = r.z(); z < r.z() + r.d(); z++) {
                    assertTrue(used[x][z]++ == 0, "free room counted twice at " + x + ", " + z);
                }
            }
        }
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                boolean near = x >= 29 && x < 56 && z >= 39 && z < 61;
                assertTrue(near ? used[x][z] == 0 : used[x][z] == 1, "wrong at " + x + ", " + z);
            }
        }
        // Packing what is left never builds on the taken room.
        for (BlockPacker.Placed p : BlockPacker.pack(free, size, size, HOUSES, new Random(7))) {
            BlockPacker.Size s = HOUSES.get(p.building());
            int w = p.turns() % 2 == 1 ? s.z() : s.x();
            int d = p.turns() % 2 == 1 ? s.x() : s.z();
            assertFalse(new BlockPacker.Rect(p.x(), p.z(), w, d).overlaps(taken), "built on the taken room");
        }
    }
}
