package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreetWalksTest {
    private static final CityLayout.Site CITY = new CityLayout.Site(1000, -2000, false, 3);

    @Test
    void fortyOnEachDistrictsOrdinaryRoads() {
        for (int cx = 0; cx < 3; cx++) {
            for (int cz = 0; cz < 3; cz++) {
                long ordinary = StreetWalks.walks(CITY, cx, cz).stream().filter(w -> !w.mainRoad()).count();
                assertTrue(Math.abs(ordinary - StreetWalks.PER_DISTRICT) <= 4, ordinary + " in district " + cx + ", " + cz);
            }
        }
    }

    @Test
    void mainRoadsAreTwiceAsBusy() {
        List<StreetWalks.Walk> walks = StreetWalks.walks(CITY, 1, 1);
        double ordinary = walks.stream().filter(w -> !w.mainRoad()).mapToDouble(StreetWalks.Walk::length).average().orElseThrow();
        double main = walks.stream().filter(StreetWalks.Walk::mainRoad).mapToDouble(StreetWalks.Walk::length).average().orElseThrow();
        assertTrue(Math.abs(ordinary / main - 2) < 0.4, "stretches " + ordinary + " on ordinary roads, " + main + " on main roads");
        assertTrue(StreetWalks.walks(CITY, 0, 0).stream().noneMatch(StreetWalks.Walk::mainRoad), "a main road in a corner district");
    }

    @Test
    void everyStretchLiesOnARoadInsideTheWalls() {
        for (int cx = 0; cx < 3; cx++) {
            for (int cz = 0; cz < 3; cz++) {
                for (StreetWalks.Walk w : StreetWalks.walks(CITY, cx, cz)) {
                    for (double f : new double[]{0, 0.5, 1}) {
                        double lx = w.x0() + (w.x1() - w.x0()) * f - CITY.originX();
                        double lz = w.z0() + (w.z1() - w.z0()) * f - CITY.originZ();
                        boolean onRoad = Math.floorMod((int) Math.floor(lx), CityLayout.ROAD_PERIOD) < CityLayout.ROAD_WIDTH
                                || Math.floorMod((int) Math.floor(lz), CityLayout.ROAD_PERIOD) < CityLayout.ROAD_WIDTH;
                        assertTrue(onRoad, "off the road at " + lx + ", " + lz);
                        assertTrue(lx >= CityLayout.ROAD_WIDTH && lz >= CityLayout.ROAD_WIDTH
                                && lx < CITY.size() - CityLayout.ROAD_WIDTH && lz < CITY.size() - CityLayout.ROAD_WIDTH, "on the wall's road");
                    }
                    assertEquals(0, Math.min(Math.abs(w.x1() - w.x0()), Math.abs(w.z1() - w.z0())), 1e-9, "a stretch not along a road");
                }
            }
        }
    }
}
