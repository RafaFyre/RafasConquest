package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityLayoutTest {
    private final WorldSettings s = WorldSettings.defaults();

    @Test
    void gridIsASquareOfFifteenBlocks() {
        assertEquals(483, CityLayout.SIZE);
        assertEquals(14, CityLayout.PLOT_SIZE);
        // Two plots and the gap between them exactly fill a block.
        assertEquals(CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH, 2 * CityLayout.PLOT_SIZE + 1);
        // The far edge road ends exactly at the city's edge.
        assertEquals(CityLayout.SIZE - 1, CityLayout.roadStart(CityLayout.BLOCKS_PER_SIDE) + CityLayout.ROAD_WIDTH - 1);
        // The town-centre block is centred on the site.
        assertEquals(CityLayout.HALF_EXTENT, CityLayout.blockStart(CityLayout.CENTER_BLOCK) + (CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH) / 2);
    }

    @Test
    void cellsShareTheGridWithNoGapsOrOverlaps() {
        Set<Integer> roads = new HashSet<>();
        Set<Integer> blocks = new HashSet<>();
        for (int cell = 0; cell < CityLayout.CELLS_PER_SIDE; cell++) {
            for (int k : CityLayout.roadsInCell(cell)) {
                assertTrue(roads.add(k), "road " + k + " built twice");
            }
            for (int k : CityLayout.blocksInCell(cell)) {
                assertTrue(blocks.add(k), "block " + k + " built twice");
            }
            // Adjacent cells touch: each cell ends where the next begins.
            if (cell > 0) {
                assertEquals(CityLayout.cellMax(cell - 1) + 1 - CityLayout.ROAD_WIDTH, CityLayout.cellMin(cell));
            }
        }
        assertEquals(CityLayout.BLOCKS_PER_SIDE + 1, roads.size());
        assertEquals(CityLayout.BLOCKS_PER_SIDE, blocks.size());
        assertEquals(0, CityLayout.cellMin(0));
        assertEquals(CityLayout.SIZE - 1, CityLayout.cellMax(CityLayout.CELLS_PER_SIDE - 1));
    }

    @Test
    void everyCellStaysWithinTheEnginesReachOfItsStart() {
        CityLayout.Site site = new CityLayout.Site(1000, -2000, false);
        for (CityLayout.District d : CityLayout.districts(site)) {
            int minX = site.originX() + CityLayout.cellMin(d.cellX());
            int maxX = site.originX() + CityLayout.cellMax(d.cellX());
            assertTrue(d.x() - minX <= 128 && maxX - d.x() <= 128);
            assertTrue(d.x() >= minX && d.x() <= maxX);
        }
        assertEquals(8, CityLayout.districts(site).size());
    }

    @Test
    void citiesAvoidTheEyeCapitalsAndEachOther() {
        for (long seed : new long[]{1L, 12345L, -77L}) {
            List<CityLayout.Site> cities = CityLayout.cityStateSitesInBox(seed, s, -30_000, -30_000, 30_000, 30_000);
            assertFalse(cities.isEmpty());
            for (CityLayout.Site c : cities) {
                assertTrue(Math.hypot(c.x(), c.z()) >= s.eyeRadius() + CityLayout.HALF_EXTENT);
                // Capitals are whole cities, so the two squares must not overlap.
                int clear = CityLayout.SIZE + CityLayout.CLEARANCE;
                for (CapitalLayout.Site cap : CapitalLayout.sitesInBox(seed, s, c.x() - clear, c.z() - clear, c.x() + clear, c.z() + clear)) {
                    assertFalse(Math.abs(cap.x() - c.x()) <= clear && Math.abs(cap.z() - c.z()) <= clear, "city overlaps a capital city");
                }
                for (CityLayout.Site other : cities) {
                    if (other != c) {
                        assertTrue(Math.abs(other.x() - c.x()) > CityLayout.SIZE || Math.abs(other.z() - c.z()) > CityLayout.SIZE);
                    }
                }
            }
        }
    }

    /** Seat cities of every size, published as the world map would. */
    private static final List<SeatCities.City> SEATS = List.of(
            new SeatCities.City(3000, 3000, 2, TitleMap.Tier.COUNTY, 0),
            new SeatCities.City(6000, -4000, 3, TitleMap.Tier.DUCHY, 1),
            new SeatCities.City(-7000, 2000, 4, TitleMap.Tier.DUCHY, 2),
            new SeatCities.City(-3000, -9000, 5, TitleMap.Tier.KINGDOM, 3),
            new SeatCities.City(12000, 9000, 9, TitleMap.Tier.EMPIRE, 4));

    private static void publishSeats() {
        CityLayout.publishBuilt((min, max) -> SEATS.stream().filter(c -> c.x() + c.reach() >= min[0] && c.x() - c.reach() <= max[0]
                && c.z() + c.reach() >= min[1] && c.z() - c.reach() <= max[1]).toList());
    }

    @org.junit.jupiter.api.AfterEach
    void unpublish() {
        CityLayout.publishBuilt((min, max) -> List.of());
    }

    @Test
    void everyCentreAndDistrictIsFoundFromItsChunk() {
        publishSeats();
        for (CityLayout.Site c : CityLayout.allSitesInBox(9L, s, -20_000, -20_000, 20_000, 20_000)) {
            CityLayout.District centre = new CityLayout.District(c, c.centerCell(), c.centerCell());
            assertEquals(c, CityLayout.siteInChunk(9L, s, centre.x() >> 4, centre.z() >> 4).orElseThrow());
            assertEquals(c.cells() * c.cells() - 1, CityLayout.districts(c).size());
            for (CityLayout.District d : CityLayout.districts(c)) {
                assertEquals(d, CityLayout.districtInChunk(9L, s, d.x() >> 4, d.z() >> 4).orElseThrow());
            }
        }
    }

    @Test
    void farmsAreKeptOutOfCities() {
        publishSeats();
        assertEquals(SEATS.size(), CityLayout.allSitesInBox(4L, s, -20_000, -20_000, 20_000, 20_000).size());
        for (CityLayout.Site c : CityLayout.allSitesInBox(4L, s, -20_000, -20_000, 20_000, 20_000)) {
            for (int dx = -c.halfExtent(); dx <= c.halfExtent() + 1; dx += 8) {
                for (int dz = -c.halfExtent(); dz <= c.halfExtent() + 1; dz += 8) {
                    assertFalse(FarmLayout.isFarmChunk(4L, s, (c.x() + dx) >> 4, (c.z() + dz) >> 4), "farm inside city at " + c);
                }
            }
        }
    }

    @Test
    void capitalCitiesStayClearOfTheEye() {
        for (CapitalLayout.Site cap : CapitalLayout.ringSites(12345L, s)) {
            assertTrue(cap.distanceFromSpawn() - CityLayout.HALF_EXTENT > s.eyeRadius());
        }
    }

    @Test
    void disabledCitiesProduceNothing() {
        WorldSettings d = s;
        WorldSettings off = new WorldSettings(d.eyeRadius(), d.warZoneOuterEdge(), d.ringCapitalCount(), d.ringDistance(), d.ringJitter(),
                d.outerCellSize(), d.outerCellMargin(), d.farmSpacingChunks(), d.farmSeparationChunks(), d.farmDailyOutput(),
                d.stormWallRadiusMin(), d.stormWallRadiusMax(), d.stormWallStrengthMin(), d.stormWallStrengthMax(),
                d.farRadiusMin(), d.farRadiusMax(), d.farStrengthMin(), d.farStrengthMax(), d.farDistanceOffset(),
                d.maxCatchUpDays(), d.initialPrecomputeRadius(), d.lazyPrecomputeDistance(), 0, 0, 0, 0, 0);
        assertTrue(CityLayout.cityStateSitesInBox(1L, off, -50_000, -50_000, 50_000, 50_000).isEmpty());
        assertFalse(CityLayout.nearCity(1L, off, 0, 0, 1000));
        // The cities built are the title seats', whatever the City-state setting (none are published here).
        assertTrue(CityLayout.allSitesInBox(1L, off, -5_000, -5_000, 5_000, 5_000).isEmpty());
    }
}
