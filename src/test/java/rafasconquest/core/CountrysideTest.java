package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountrysideTest {
    private static final int BPP = 16;
    private static final int CELL = 96;
    private static final int CELLS = 20;

    private record World(SeatCities.Grid grid, TitleMap titles, List<SeatCities.City> cities, Countryside.Plan plan) {
    }

    /** A land of square provinces 1536 blocks a side, the sea along the west. */
    private static World world(long seed) {
        int width = CELLS * CELL;
        int[] provinces = new int[width * width];
        boolean[] sea = new boolean[width * width];
        List<ProvinceMap.Centre> centres = new ArrayList<>();
        int[][] index = new int[CELLS][CELLS];
        for (int cz = 0; cz < CELLS; cz++) {
            for (int cx = 0; cx < CELLS; cx++) {
                index[cz][cx] = cx == 0 ? -1 : centres.size();
                if (cx > 0) {
                    centres.add(new ProvinceMap.Centre(cx * CELL * BPP + CELL * BPP / 2, cz * CELL * BPP + CELL * BPP / 2, false));
                }
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
        SeatCities.Grid grid = new SeatCities.Grid(0, 0, BPP, width, width, provinces, centres);
        List<SeatCities.City> cities = SeatCities.plan(seed, grid, titles);
        return new World(grid, titles, cities, Countryside.plan(seed, grid, titles, cities));
    }

    @Test
    void aboutHalfTheProvincesWithoutACityHaveOneSettlementInThem() {
        World w = world(3);
        Set<Integer> covered = new HashSet<>();
        for (SeatCities.City c : w.plan().cities()) {
            covered.addAll(SeatCities.covered(w.grid(), c));
        }
        Set<Integer> seen = new HashSet<>();
        for (Countryside.Settlement s : w.plan().settlements()) {
            assertTrue(seen.add(s.province()), "two settlements in province " + s.province());
            int px = s.x() / BPP;
            int pz = s.z() / BPP;
            assertEquals(s.province(), w.grid().provinces()[pz * w.grid().width() + px], "a settlement outside its province");
            if (s.kind() != Countryside.Kind.CASTLE) {
                assertFalse(covered.contains(s.province()), "a settlement under a city");
            }
        }
        long free = java.util.stream.IntStream.range(0, w.grid().centres().size()).filter(p -> !covered.contains(p)).count();
        assertTrue(seen.size() >= free * 0.35 && seen.size() <= free * 0.65, seen.size() + " settlements for " + free + " free provinces");
    }

    @Test
    void villagesNeverNeighbourAndHamletsOutnumberThemTwice() {
        for (long seed = 0; seed < 5; seed++) {
            World w = world(seed);
            List<TreeSet<Integer>> adjacent = Countryside.adjacency(w.grid(), w.grid().centres().size());
            Set<Integer> villages = new HashSet<>();
            int hamlets = 0;
            for (Countryside.Settlement s : w.plan().settlements()) {
                if (s.kind() == Countryside.Kind.VILLAGE) {
                    villages.add(s.province());
                    assertTrue(s.hamlets() >= 3 && s.hamlets() <= 6, "a village of " + s.hamlets());
                } else if (s.kind() == Countryside.Kind.HAMLET) {
                    hamlets++;
                }
            }
            for (int v : villages) {
                for (int q : adjacent.get(v)) {
                    assertFalse(villages.contains(q), "neighbouring villages in " + v + " and " + q);
                }
            }
            assertTrue(hamlets >= 2 * villages.size(), hamlets + " hamlets for " + villages.size() + " villages");
            assertFalse(villages.isEmpty(), "no villages at all");
        }
    }

    @Test
    void someHamletsAreWatchtowersAndSomeTownsCastles() {
        int towers = 0;
        int hamlets = 0;
        int castles = 0;
        int towns = 0;
        for (long seed = 0; seed < 5; seed++) {
            World w = world(seed);
            for (Countryside.Settlement s : w.plan().settlements()) {
                switch (s.kind()) {
                    case WATCHTOWER -> towers++;
                    case HAMLET -> hamlets++;
                    case CASTLE -> {
                        castles++;
                        assertEquals(TitleMap.Tier.COUNTY, w.titles().seatOf(s.province()), "a castle off a county seat");
                        assertTrue(w.plan().cities().stream().noneMatch(c -> c.province() == s.province()), "a castle and a Town");
                    }
                    default -> {
                    }
                }
            }
            towns += (int) w.cities().stream().filter(c -> c.tier() == TitleMap.Tier.COUNTY).count();
        }
        double towerShare = (double) towers / (towers + hamlets);
        double castleShare = (double) castles / towns;
        assertTrue(towerShare > 0.04 && towerShare < 0.25, "watchtowers: " + towers + " of " + (towers + hamlets));
        assertTrue(castleShare > 0.1 && castleShare < 0.45, "castles: " + castles + " of " + towns + " Towns");
    }
}
