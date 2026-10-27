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
            for (int tx = 0; tx < seg.length(); tx++) {
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
    void segmentIsOneStreetLongEightTallFiveWide() {
        assertEquals(CityLayout.ROAD_PERIOD, WallTemplates.LENGTH);
        assertEquals(8, WallTemplates.HEIGHT);
        assertTrue(WallTemplates.JIGSAW_Y > WallTemplates.GATE_HEIGHT && WallTemplates.JIGSAW_Y < WallTemplates.BANNER_Y - 1,
                "the connectors must sit in solid wall above the gate");
        assertEquals(5, WallTemplates.WIDTH);
        assertEquals(CityLayout.WALL_WIDTH, WallTemplates.WIDTH);
        assertEquals(CityLayout.WALL_OUTSET, WallTemplates.CORNER_LEAD);
        // Two banners, evenly spaced along the segment.
        assertEquals(WallTemplates.LENGTH / 4, WallTemplates.BANNER_X[0]);
        assertEquals(3 * WallTemplates.LENGTH / 4, WallTemplates.BANNER_X[1]);
    }

    @Test
    void threeGuardsPerSegmentClearOfTheBanners() {
        assertEquals(3, WallTemplates.GUARD_X.length);
        for (int x : WallTemplates.GUARD_X) {
            assertTrue(x > 0 && x < WallTemplates.LENGTH - 1);
            assertTrue(x != WallTemplates.BANNER_X[0] && x != WallTemplates.BANNER_X[1]);
        }
        // Symmetric about the middle of the segment.
        assertEquals(WallTemplates.LENGTH - 1, WallTemplates.GUARD_X[0] + WallTemplates.GUARD_X[2]);
    }

    @Test
    void ringCoversTheWholeWallBandExactlyOnce() {
        Map<Long, CityLayout.WallSegment> covered = footprint();
        int band = 0;
        int out = CityLayout.WALL_OUTSET;
        for (int x = -out - 1; x < CityLayout.SIZE + out + 1; x++) {
            for (int z = -out - 1; z < CityLayout.SIZE + out + 1; z++) {
                if (CityLayout.inWallBand(x, z)) {
                    band++;
                    assertTrue(covered.containsKey(key(x, z)), "gap in the wall at " + x + "," + z);
                }
            }
        }
        int outer = CityLayout.SIZE + 2 * out;
        int inner = CityLayout.SIZE - 2 * CityLayout.ROAD_WIDTH;
        assertEquals(outer * outer - inner * inner, band, "the band is the edge road plus the outset, all round");
        assertEquals(band, covered.size(), "wall blocks outside the wall band");
        assertEquals(4 * CityLayout.BLOCKS_PER_SIDE, CityLayout.wallSegments().size());
    }

    @Test
    void outerFaceAlwaysPointsAwayFromTheCity() {
        int lo = -CityLayout.WALL_OUTSET;
        int hi = CityLayout.SIZE - 1 + CityLayout.WALL_OUTSET;
        for (CityLayout.WallSegment seg : CityLayout.wallSegments()) {
            int[] outer = CityLayout.wallBlock(seg, seg.length() / 2, 0);
            boolean onEdge = outer[0] == lo || outer[1] == lo || outer[0] == hi || outer[1] == hi;
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
            assertFalse(gate.corner(), "the gate template has no corner lead");
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
        CityLayout.Site site = new CityLayout.Site(0, 0, false);
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
                    for (int tx : new int[]{0, seg.length() - 1}) {
                        for (int tz : new int[]{0, WallTemplates.WIDTH - 1}) {
                            int[] p = CityLayout.wallBlock(seg, tx, tz);
                            assertTrue(Math.abs(site.originX() + p[0] - startX) <= 128 && Math.abs(site.originZ() + p[1] - startZ) <= 128,
                                    seg + " is out of reach of cell " + cx + "," + cz);
                        }
                    }
                }
            }
        }
        assertEquals(CityLayout.wallSegments().size(), total, "every segment is built by exactly one cell");
    }

    @Test
    void consecutiveSegmentsMeetEndToStart() {
        // The end jigsaw on a template's last x faces +x; the next segment's start jigsaw is on its x 0, one block further on.
        List<CityLayout.WallSegment> all = CityLayout.wallSegments();
        for (int i = 1; i < all.size(); i++) {
            CityLayout.WallSegment prev = all.get(i - 1);
            CityLayout.WallSegment next = all.get(i);
            if (prev.side() != next.side()) {
                continue;
            }
            int[] end = CityLayout.wallBlock(prev, prev.length() - 1, 1);
            int[] beyond = CityLayout.wallBlock(prev, prev.length(), 1);
            int[] start = CityLayout.wallBlock(next, 0, 1);
            assertEquals(beyond[0], start[0]);
            assertEquals(beyond[1], start[1]);
            assertFalse(end[0] == start[0] && end[1] == start[1]);
        }
    }
}
