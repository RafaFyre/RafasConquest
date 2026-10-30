package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProvinceMapTest {
    private static final int BPP = 16;

    /** Flat grassland, {@code size} pixels square from (0, 0), with water wherever {@code wet} says. */
    private static ProvinceMap.Terrain flat(int size, java.util.function.IntBinaryOperator wet) {
        boolean[] water = new boolean[size * size];
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                water[z * size + x] = wet.applyAsInt(x, z) != 0;
            }
        }
        return new ProvinceMap.Terrain(0, 0, BPP, size, size, water, new float[size * size], new int[size * size], new boolean[size * size]);
    }

    @Test
    void aRiverIsABorder() {
        // A river 3 pixels wide at x = 448..495; centres either side of it.
        ProvinceMap.Terrain t = flat(64, (x, z) -> x >= 28 && x <= 30 ? 1 : 0);
        List<ProvinceMap.Centre> centres = List.of(new ProvinceMap.Centre(300, 512, false), new ProvinceMap.Centre(900, 512, false));
        int[] p = ProvinceMap.assign(t, centres);
        assertEquals(ProvinceMap.WATER, p[32 * 64 + 29]);
        assertEquals(0, p[32 * 64 + 20], "west of the river");
        // Just across the river: nearer the west centre, but the river makes the east one cheaper.
        assertEquals(1, p[32 * 64 + 31], "east bank");
    }

    @Test
    void aRidgeIsABorder() {
        float[] heights = new float[64 * 64];
        for (int z = 0; z < 64; z++) {
            heights[z * 64 + 30] = 60; // a narrow 60-block ridge at x = 480
        }
        ProvinceMap.Terrain t = new ProvinceMap.Terrain(0, 0, BPP, 64, 64, new boolean[64 * 64], heights, new int[64 * 64], new boolean[64 * 64]);
        int[] p = ProvinceMap.assign(t, List.of(new ProvinceMap.Centre(300, 512, false), new ProvinceMap.Centre(900, 512, false)));
        assertEquals(1, p[32 * 64 + 32], "just over the ridge goes to the far centre");
        assertEquals(0, p[32 * 64 + 25]);
    }

    @Test
    void citiesAreCentresAndClearTheGridAroundThem() {
        ProvinceMap.Terrain t = flat(256, (x, z) -> 0);
        int[] city = {2048, 2048};
        List<ProvinceMap.Centre> centres = ProvinceMap.centres(7L, t, List.<int[]>of(city));
        assertTrue(centres.contains(new ProvinceMap.Centre(2048, 2048, true)));
        for (ProvinceMap.Centre c : centres) {
            if (!c.city()) {
                assertFalse(Math.abs(c.x() - 2048) < ProvinceMap.ANCHOR_CLEARANCE && Math.abs(c.z() - 2048) < ProvinceMap.ANCHOR_CLEARANCE);
            }
        }
        assertEquals(centres, ProvinceMap.centres(7L, t, List.<int[]>of(city)), "same seed, same centres");
        // Roughly one centre per SPACING square of land.
        int expected = (256 * BPP / ProvinceMap.SPACING) * (256 * BPP / ProvinceMap.SPACING);
        assertTrue(Math.abs(centres.size() - expected) <= expected / 5, centres.size() + " centres, expected about " + expected);
    }

    @Test
    void anAreaWithItsMarginMatchesTheWholeMap() {
        // Wavy coastline and hills, so paths matter.
        int size = 640;
        boolean[] water = new boolean[size * size];
        float[] heights = new float[size * size];
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                water[z * size + x] = x + 12 * Math.sin(z / 9.0) > 560;
                heights[z * size + x] = (float) (30 * Math.sin(x / 13.0) * Math.cos(z / 17.0));
            }
        }
        ProvinceMap.Terrain whole = new ProvinceMap.Terrain(0, 0, BPP, size, size, water, heights, new int[size * size], new boolean[size * size]);
        List<ProvinceMap.Centre> centres = ProvinceMap.centres(3L, whole, List.of());
        int[] all = ProvinceMap.assign(whole, centres);

        // A 40-pixel tile in the middle, computed from just itself plus SEARCH blocks of margin.
        int margin = ProvinceMap.SEARCH / BPP + 1;
        int from = 300 - margin;
        int span = 40 + 2 * margin;
        boolean[] w2 = new boolean[span * span];
        float[] h2 = new float[span * span];
        for (int z = 0; z < span; z++) {
            for (int x = 0; x < span; x++) {
                w2[z * span + x] = water[(from + z) * size + from + x];
                h2[z * span + x] = heights[(from + z) * size + from + x];
            }
        }
        ProvinceMap.Terrain tile = new ProvinceMap.Terrain(from * BPP, from * BPP, BPP, span, span, w2, h2, new int[span * span], new boolean[span * span]);
        int[] part = ProvinceMap.assign(tile, centres);
        for (int z = 0; z < 40; z++) {
            for (int x = 0; x < 40; x++) {
                assertEquals(all[(300 + z) * size + 300 + x], part[(margin + z) * span + margin + x], "pixel " + x + "," + z);
            }
        }
        assertTrue(Arrays.stream(all).filter(p -> p >= 0).distinct().count() > 20, "split into many provinces");
    }
}
