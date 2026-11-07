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
        CityTerrain.Plan platform = CityTerrain.platform(0, 0, size, 0, 80, (x, z) -> 70);
        assertEquals(80, CityTerrain.column(city, -100, -100, platform, 10, 10).low(), "the platform's plate on it");
        CityTerrain.Column out = CityTerrain.column(platform, -5, 10);
        assertEquals(CityTerrain.Kind.SLOPE, out.kind());
        assertEquals(80 - 9, out.low(), "a cliff, 3 down for 1 out: 3 out past its box");
    }
}
