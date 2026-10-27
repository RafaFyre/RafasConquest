package rafasconquest.core;

import org.junit.jupiter.api.Test;
import rafasconquest.dev.WallTemplates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityWallTest {
    /** Every block a segment's footprint covers, keyed by local (x, z). */
    private static Map<Long, CityLayout.WallSegment> footprint() {
        Map<Long, CityLayout.WallSegment> covered = new HashMap<>();
        for (CityLayout.WallSegment seg : CityLayout.wallSegments()) {
            for (int tx = 0; tx < WallTemplates.LENGTH; tx++) {
                for (int tz = 0; tz < WallTemplates.WIDTH; tz++) {
                    int[] p = CityLayout.wallBlock(seg, tx, tz);
                    CityLayout.WallSegment before = covered.put(key(p[0], p[1]), seg);
                    assertEquals(null, before, "segments " + before + " and " + seg + " overlap at " + p[0] + "," + p[1]);
                }
            }
        }
        return covered;
    }

    private static long key(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    @Test
    void segmentIsOneStreetLongEightTallThreeWide() {
        assertEquals(CityLayout.ROAD_PERIOD, WallTemplates.LENGTH);
        assertEquals(8, WallTemplates.HEIGHT);
        assertEquals(3, WallTemplates.WIDTH);
        assertEquals(CityLayout.ROAD_WIDTH, WallTemplates.WIDTH);
        // Two banners, evenly spaced along the segment.
        assertEquals(WallTemplates.LENGTH / 4, WallTemplates.BANNER_X[0]);
        assertEquals(3 * WallTemplates.LENGTH / 4, WallTemplates.BANNER_X[1]);
    }

    @Test
    void ringCoversTheWholeEdgeBandExactlyOnce() {
        Map<Long, CityLayout.WallSegment> covered = footprint();
        int band = 0;
        for (int x = 0; x < CityLayout.SIZE; x++) {
            for (int z = 0; z < CityLayout.SIZE; z++) {
                if (CityLayout.inWallBand(x, z)) {
                    band++;
                    assertTrue(covered.containsKey(key(x, z)), "gap in the wall at " + x + "," + z);
                }
            }
        }
        assertEquals(band, covered.size(), "wall blocks outside the edge band");
        assertEquals(4 * CityLayout.BLOCKS_PER_SIDE, CityLayout.wallSegments().size());
    }

    @Test
    void outerFaceAlwaysPointsAwayFromTheCity() {
        int far = CityLayout.SIZE - 1;
        for (CityLayout.WallSegment seg : CityLayout.wallSegments()) {
            int[] outer = CityLayout.wallBlock(seg, WallTemplates.LENGTH / 2, 0);
            boolean onEdge = outer[0] == 0 || outer[1] == 0 || outer[0] == far || outer[1] == far;
            assertTrue(onEdge, seg + " has its outer face at " + outer[0] + "," + outer[1]);
        }
    }

    @Test
    void oneGatePerSideWhereTheAvenueMeetsTheWall() {
        int road = CityLayout.roadStart(CityLayout.GATE_ROAD);
        for (CityLayout.Side side : CityLayout.Side.values()) {
            List<CityLayout.WallSegment> gates = CityLayout.wallSegments().stream().filter(s -> s.side() == side && s.gate()).toList();
            assertEquals(1, gates.size(), side + " gates");
            CityLayout.WallSegment gate = gates.get(0);
            // The opening is template x 0..2; it must sit exactly on the avenue's 3 road columns.
            for (int tx = 0; tx < WallTemplates.GATE_WIDTH; tx++) {
                int[] p = CityLayout.wallBlock(gate, tx, 1);
                int along = (side == CityLayout.Side.NORTH || side == CityLayout.Side.SOUTH) ? p[0] : p[1];
                assertTrue(along >= road && along < road + CityLayout.ROAD_WIDTH, side + " gate column " + along + " is off the avenue");
            }
        }
    }

    @Test
    void eachCellBuildsOnlyWallWithinReachOfItsStart() {
        CityLayout.Site site = new CityLayout.Site(0, 0);
        int total = 0;
        for (int cx = 0; cx < CityLayout.CELLS_PER_SIDE; cx++) {
            for (int cz = 0; cz < CityLayout.CELLS_PER_SIDE; cz++) {
                List<CityLayout.WallSegment> mine = CityLayout.wallSegments(cx, cz);
                total += mine.size();
                if (cx == 1 && cz == 1) {
                    assertTrue(mine.isEmpty(), "the town-centre cell has no wall");
                }
                int startX = site.x() + (cx - 1) * CityLayout.DISTRICT_SPACING;
                int startZ = site.z() + (cz - 1) * CityLayout.DISTRICT_SPACING;
                for (CityLayout.WallSegment seg : mine) {
                    for (int tx : new int[]{0, WallTemplates.LENGTH - 1}) {
                        int[] p = CityLayout.wallBlock(seg, tx, 0);
                        assertTrue(Math.abs(site.originX() + p[0] - startX) <= 128 && Math.abs(site.originZ() + p[1] - startZ) <= 128,
                                seg + " is out of reach of cell " + cx + "," + cz);
                    }
                }
            }
        }
        assertEquals(CityLayout.wallSegments().size(), total, "every segment is built by exactly one cell");
    }

    @Test
    void consecutiveSegmentsMeetEndToStart() {
        // The jigsaw on template x 31 faces +x; the next segment's start jigsaw is on its x 0, one block further on.
        List<CityLayout.WallSegment> all = CityLayout.wallSegments();
        for (int i = 1; i < all.size(); i++) {
            CityLayout.WallSegment prev = all.get(i - 1);
            CityLayout.WallSegment next = all.get(i);
            if (prev.side() != next.side()) {
                continue;
            }
            int[] end = CityLayout.wallBlock(prev, WallTemplates.LENGTH - 1, 1);
            int[] beyond = CityLayout.wallBlock(prev, WallTemplates.LENGTH, 1);
            int[] start = CityLayout.wallBlock(next, 0, 1);
            assertEquals(beyond[0], start[0]);
            assertEquals(beyond[1], start[1]);
            assertFalse(end[0] == start[0] && end[1] == start[1]);
        }
    }
}
