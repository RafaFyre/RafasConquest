package rafasconquest.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatCitiesTest {
    private static final int BPP = 16;
    /** Provinces 30 pixels (480 blocks) square, a few rows of sea along the west. */
    private static final int CELL = 30;
    private static final int CELLS = 16;

    private record World(SeatCities.Grid grid, TitleMap titles) {
    }

    /** Provinces this small are a size-1 world's; its cities look for room no further than its provinces reach. */
    @BeforeEach
    void provinceSizeOne() {
        ProvinceMap.setSize(1);
    }

    @AfterEach
    void provinceSizeBack() {
        ProvinceMap.setSize(2);
    }

    private static World world(long seed) {
        int width = CELLS * CELL;
        int[] provinces = new int[width * width];
        boolean[] sea = new boolean[width * width];
        List<ProvinceMap.Centre> centres = new ArrayList<>();
        int[][] index = new int[CELLS][CELLS];
        for (int cz = 0; cz < CELLS; cz++) {
            for (int cx = 0; cx < CELLS; cx++) {
                if (cx == 0) {
                    index[cz][cx] = -1;
                    continue;
                }
                index[cz][cx] = centres.size();
                centres.add(new ProvinceMap.Centre(cx * CELL * BPP + CELL * BPP / 2, cz * CELL * BPP + CELL * BPP / 2, false));
            }
        }
        for (int z = 0; z < width; z++) {
            for (int x = 0; x < width; x++) {
                int p = index[z / CELL][x / CELL];
                provinces[z * width + x] = p < 0 ? ProvinceMap.WATER : p;
                sea[z * width + x] = p < 0;
            }
        }
        TitleMap titles = TitleMap.build(seed, width, width, provinces, centres.size(), sea);
        return new World(new SeatCities.Grid(0, 0, BPP, width, width, provinces, centres), titles);
    }

    @Test
    void citiesStandOnLandAndKeepApart() {
        World w = world(5);
        List<SeatCities.City> cities = SeatCities.plan(5, w.grid(), w.titles());
        assertFalse(cities.isEmpty());
        for (SeatCities.City c : cities) {
            int half = c.size() / 2 + (c.port() ? SeatCities.WALL_FOOT : 0);
            assertTrue(c.x() - half >= CELL * BPP - (c.port() ? BPP : 0), "a city stands in the sea: " + c);
            for (SeatCities.City o : cities) {
                if (o != c) {
                    int apart = c.size() / 2 + o.size() / 2;
                    assertTrue(Math.abs(c.x() - o.x()) > apart || Math.abs(c.z() - o.z()) > apart, "cities overlap: " + c + " " + o);
                }
            }
        }
    }

    @Test
    void coastalSeatsBecomePortsFacingTheSea() {
        World w = world(21);
        List<SeatCities.City> cities = SeatCities.plan(21, w.grid(), w.titles());
        List<SeatCities.City> ports = cities.stream().filter(SeatCities.City::port).toList();
        assertFalse(ports.isEmpty(), "no ports on the west coast");
        for (SeatCities.City c : ports) {
            // The sea is the west column of provinces: every port faces it, wall on land, the water just past it.
            assertEquals(CityLayout.Side.WEST.ordinal(), c.harbour(), c.toString());
            int wallFoot = c.x() - c.size() / 2 - SeatCities.WALL_FOOT;
            assertTrue(wallFoot >= CELL * BPP - BPP, "a port's wall stands in the sea: " + c);
            assertTrue(wallFoot - SeatCities.HARBOUR_SEA < CELL * BPP, "a port far from the sea: " + c);
        }
        assertTrue(cities.stream().anyMatch(c -> !c.port()), "inland cities became ports");
        // A realm on the coast has its capital there.
        TitleMap t = w.titles();
        for (SeatCities.City c : cities) {
            if (c.tier().ordinal() < TitleMap.Tier.KINGDOM.ordinal()) {
                continue;
            }
            int realm = t.kingdom()[t.duchy()[t.county()[c.province()]]];
            boolean empire = c.tier() == TitleMap.Tier.EMPIRE;
            boolean coastal = false;
            for (int p = 0; p < t.county().length; p++) {
                int k = t.kingdom()[t.duchy()[t.county()[p]]];
                boolean ours = empire ? t.empire()[k] == t.empire()[realm] : k == realm;
                coastal |= ours && w.grid().centres().get(p).x() < 2 * CELL * BPP;
            }
            assertTrue(!coastal || c.port(), "a coastal realm's capital is inland: " + c);
        }
        // The ports' shipping routes keep to the sea, and join them.
        int width = CELLS * CELL;
        boolean[] ocean = new boolean[width * width];
        for (int i = 0; i < ocean.length; i++) {
            ocean[i] = w.grid().provinces()[i] == ProvinceMap.WATER;
        }
        Roads.Land land = new Roads.Land(0, 0, BPP, width, width, new int[width * width], ocean, ocean, new byte[width * width], 63);
        List<String> names = java.util.stream.IntStream.range(0, w.grid().centres().size()).mapToObj(i -> "P" + i).toList();
        List<Roads.Road> lanes = ShippingRoutes.plan(land, ocean, cities, names);
        assertTrue(ports.size() < 2 || !lanes.isEmpty(), "no shipping routes between " + ports.size() + " ports");
        for (Roads.Road r : lanes) {
            assertEquals(Roads.Grade.SEA, r.grade());
            for (int i = 0; i < r.points(); i++) {
                assertTrue(r.xs()[i] < CELL * BPP, "a shipping route crosses land at " + r.xs()[i] + ", " + r.zs()[i]);
            }
        }
    }

    @Test
    void aPondIsNoSeaForAHarbour() {
        World w = world(3);
        boolean[] ocean = new boolean[w.grid().provinces().length];
        for (int i = 0; i < ocean.length; i++) {
            ocean[i] = w.grid().provinces()[i] == ProvinceMap.WATER;
        }
        int width = CELLS * CELL;
        int pond = 200 * width + 300;
        ocean[pond] = true;
        boolean[] seas = SeatCities.seas(w.grid(), ocean);
        assertTrue(seas[0], "the west sea is a sea");
        assertFalse(seas[pond], "a one-pixel pond is a sea");
    }

    @Test
    void aCityIsSizedByItsHighestTitle() {
        World w = world(9);
        for (SeatCities.City c : SeatCities.plan(9, w.grid(), w.titles())) {
            switch (c.tier()) {
                case COUNTY -> assertEquals(2, c.districts());
                case DUCHY -> assertTrue(c.districts() == 3 || c.districts() == 4, c.toString());
                case KINGDOM -> assertTrue(c.districts() == 4 || c.districts() == 5, c.toString());
                case EMPIRE -> assertTrue(c.districts() >= 7 && c.districts() <= 9, c.toString());
            }
            assertEquals(c.tier(), w.titles().seatOf(c.province()), "a city isn't on its title's seat");
        }
    }

    @Test
    void movedSeatsStayNestedAndMostTitlesGetACity() {
        World w = world(13);
        TitleMap t = w.titles();
        List<SeatCities.City> cities = SeatCities.plan(13, w.grid(), t);
        for (int d = 0; d < t.duchies(); d++) {
            int seat = t.duchySeat()[d];
            assertEquals(seat, t.countySeat()[t.county()[seat]], "a duchy's seat isn't a county seat");
        }
        for (int e = 0; e < t.empires(); e++) {
            int seat = t.empireSeat()[e];
            assertEquals(seat, t.kingdomSeat()[t.kingdom()[t.duchy()[t.county()[seat]]]], "an empire's seat isn't a kingdom seat");
        }
        long seats = java.util.Arrays.stream(t.countySeat()).distinct().count();
        assertTrue(cities.size() >= seats * 0.8, cities.size() + " cities for " + seats + " seats");
    }

    @Test
    void aBigCityCoversTheProvincesAroundIt() {
        World w = world(17);
        for (SeatCities.City c : SeatCities.plan(17, w.grid(), w.titles())) {
            List<Integer> covered = SeatCities.covered(w.grid(), c);
            assertTrue(covered.contains(c.province()), "a city doesn't cover its own province: " + c + " covers " + covered);
            if (c.districts() >= 7) {
                assertTrue(covered.size() >= 4, "a " + c.districts() + "-district city covers only " + covered);
            }
        }
    }
}
