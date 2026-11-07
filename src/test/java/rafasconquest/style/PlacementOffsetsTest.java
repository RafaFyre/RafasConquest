package rafasconquest.style;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacementOffsetsTest {
    @Test
    void readsTablesAndPlainNumbersByName() {
        Map<String, Integer> o = StyleLibrary.readOffsets("""
                # comment
                "Example House" = { y = -10 }
                "tower.v2" = { Y = 3 }
                plain = 4
                """);
        assertEquals(Map.of("Example House", -10, "tower.v2", 3, "plain", 4), o);
    }

    @Test
    void readsUniquenessLimits() {
        Map<String, StyleLibrary.Placement> p = StyleLibrary.readPlacements("""
                "Grand Hall" = { y = -2, "Uniqueness Global" = 3, "Uniqueness City" = 1 }
                "Only City" = { "Uniqueness City" = 2 }
                plain = 4
                """);
        assertEquals(new StyleLibrary.Placement(-2, 3, 1), p.get("Grand Hall"));
        assertEquals(new StyleLibrary.Placement(0, 0, 2), p.get("Only City"));
        assertEquals(new StyleLibrary.Placement(4, 0, 0), p.get("plain"));
    }

    @Test
    void upgradeAddsLimitsToEveryLineAndNamesMissingStructures() {
        String old = """
                # How far up (+) or down (-) each structure in this folder is built, from where its .nbt would stand.
                # Read when the structure is next placed (or after /pve style reload).
                "Big Palace" = { y = -3 }
                plain = 4
                "Kept" = { y = 1, "Uniqueness Global" = 2, "Uniqueness City" = 0 }
                """;
        String up = StyleLibrary.upgrade(old, List.of("Big Palace", "plain", "Kept", "New One"), 1);
        Map<String, StyleLibrary.Placement> p = StyleLibrary.readPlacements(up);
        assertEquals(new StyleLibrary.Placement(-3, 0, 1), p.get("Big Palace"));
        assertEquals(new StyleLibrary.Placement(4, 0, 1), p.get("plain"));
        assertEquals(new StyleLibrary.Placement(1, 2, 0), p.get("Kept"));
        assertEquals(new StyleLibrary.Placement(0, 0, 1), p.get("New One"));
        assertTrue(up.contains("0 is no limit"));
        assertEquals(up, StyleLibrary.upgrade(up, List.of("Big Palace", "plain", "Kept", "New One"), 1));
    }

    @Test
    void packerBuildsALimitedHouseNoMoreThanAllowed() {
        List<BlockPacker.Size> sizes = List.of(new BlockPacker.Size(3, 3), new BlockPacker.Size(3, 3));
        List<BlockPacker.Placed> placed = BlockPacker.pack(List.of(new BlockPacker.Rect(0, 0, 40, 40)), 40, 40, sizes,
                new java.util.Random(1), new int[]{1, Integer.MAX_VALUE});
        assertEquals(1, placed.stream().filter(x -> x.building() == 0).count());
        assertTrue(placed.size() > 1);
    }
}
