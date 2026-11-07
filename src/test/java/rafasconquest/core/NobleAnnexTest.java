package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NobleAnnexTest {
    @Test
    void thePathClimbsFromTheCityToThePlatformNoSteeperThanAStep() {
        for (int gap : new int[]{NobleAnnex.GAP_MIN, NobleAnnex.GAP_MAX}) {
            for (int offset : new int[]{0, 7, -NobleAnnex.OFFSET_MAX, NobleAnnex.OFFSET_MAX}) {
                List<int[]> path = NobleAnnex.path(100, 10, 100 + offset, 10 + gap - 1);
                Map<Long, Integer> half = new HashMap<>();
                path.forEach(c -> half.put(((long) c[0] << 32) ^ (c[1] & 0xffffffffL), c[2]));
                assertEquals(0, half.get((100L << 32) ^ 10), gap + " " + offset + ": level with the city's gate");
                assertEquals(2 * NobleAnnex.RISE, half.get(((long) (100 + offset) << 32) ^ (10 + gap - 1)), gap + " " + offset + ": level with the district's");
                // It bows away to +u, so its -u edge at the gate shows the width: 2 to that side of the middle, 3 on the other.
                assertTrue(offset < 0 || half.containsKey((98L << 32) ^ 10) && !half.containsKey((97L << 32) ^ 10), gap + " " + offset + ": 6 wide at the city's gate");
                for (int[] c : path) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            Integer n = half.get(((long) (c[0] + dx) << 32) ^ ((c[1] + dz) & 0xffffffffL));
                            assertTrue(n == null || Math.abs(n - c[2]) <= 2, gap + " " + offset + ": a step of more than a block at " + c[0] + ", " + c[1]);
                        }
                    }
                }
            }
        }
    }

    @Test
    void roadsLeaveByAnotherGateThanTheNobleDistrictsOrTheHarbours() {
        Roads.Place east = new Roads.Place("t", 1000, 0, 0, 0);
        Roads.Place city = new Roads.Place("c", 0, 0, 200, 3, -1, CityLayout.Side.EAST.ordinal());
        assertEquals(0, Roads.side(city, east)[0], "not out of the east gate");
        Roads.Place port = new Roads.Place("c", 0, 0, 200, 3, CityLayout.Side.NORTH.ordinal(), CityLayout.Side.EAST.ordinal());
        assertArrayEquals(new int[]{0, 1}, Roads.side(port, new Roads.Place("t", 1000, -10, 0, 0)), "south, as north is the harbour");
    }

    @Test
    void thePlatformStandsOverTheCitysGroundAndItsEasingMeetsTheCitys() {
        CityLayout.Site site = new CityLayout.Site(0, 0, false, 2);
        CityTerrain.Plan city = CityTerrain.plan(site, (x, z) -> 70, 63);
        int size = 125;
        int top = city.plateY() + NobleAnnex.RISE;
        CityTerrain.Plan platform = CityTerrain.platform(0, 0, size, 0, top, (x, z) -> 70);
        assertEquals(top, CityTerrain.column(city, -100, -100, platform, 10, 10).low(), "the platform's plate on it");
        CityTerrain.Column out = CityTerrain.column(platform, -5, 10);
        assertEquals(CityTerrain.Kind.SLOPE, out.kind());
        assertEquals(top - 3 * NobleAnnex.RISE / NobleAnnex.EASE_REACH, out.low(), "easing down: 3 out past its box");
        CityTerrain.Column foot = CityTerrain.column(platform, platform.boxMin() - NobleAnnex.EASE_REACH, 10);
        assertEquals(city.plateY(), foot.low(), "the easing's foot on the city's height");
        assertTrue(foot.fill() && foot.high() == foot.low(), "filled and carved to the easing");
        CityTerrain.Column past = CityTerrain.column(platform, platform.boxMin() - NobleAnnex.EASE_REACH - 1, 10);
        assertEquals(foot.low() - 2, past.low(), "past it, still filling down as steeply: no drop to lower land");
        assertTrue(past.fill() && past.stone() && past.high() == Integer.MAX_VALUE, "fill only, in stone");
        assertEquals(CityTerrain.Kind.NONE, CityTerrain.column(platform, platform.boxMin() - NobleAnnex.FILL_REACH - 1, 10).kind(), "nothing past the fill");
        // Where both ease the land, neither carves into the other: the higher one stands.
        int overlaps = 0;
        for (int lx = -200; lx <= 200; lx += 3) {
            for (int lz = -200; lz <= 200; lz += 3) {
                CityTerrain.Column c = CityTerrain.column(city, lx, lz);
                CityTerrain.Column p = CityTerrain.column(platform, lx + 30, lz + 30);
                if (c.kind() != CityTerrain.Kind.SLOPE || p.kind() != CityTerrain.Kind.SLOPE) {
                    continue;
                }
                CityTerrain.Column both = CityTerrain.column(city, lx, lz, platform, lx + 30, lz + 30);
                assertTrue(both.high() >= c.high(), "not below the city's easing at " + lx + "," + lz);
                assertTrue(p.high() == Integer.MAX_VALUE || both.high() >= p.high(), "not below the platform's at " + lx + "," + lz);
                assertTrue(both.stone(), "stone where they merge");
                overlaps++;
            }
        }
        assertTrue(overlaps > 0, "the two easings do overlap here");
    }
}
