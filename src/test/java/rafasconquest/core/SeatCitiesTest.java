package rafasconquest.core;

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
            int half = c.size() / 2;
            assertTrue(c.x() - half >= CELL * BPP, "a city stands in the sea: " + c);
            for (SeatCities.City o : cities) {
                if (o != c) {
                    int apart = c.size() / 2 + o.size() / 2;
                    assertTrue(Math.abs(c.x() - o.x()) > apart || Math.abs(c.z() - o.z()) > apart, "cities overlap: " + c + " " + o);
                }
            }
        }
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
            assertTrue(covered.contains(c.province()), "a city doesn't cover its own province");
            if (c.districts() >= 7) {
                assertTrue(covered.size() >= 4, "a " + c.districts() + "-district city covers only " + covered);
            }
        }
    }
}
