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
        CityLayout.Site site = new CityLayout.Site(1000, -2000);
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
            List<CityLayout.Site> cities = CityLayout.sitesInBox(seed, s, -30_000, -30_000, 30_000, 30_000);
            assertFalse(cities.isEmpty());
            for (CityLayout.Site c : cities) {
                assertTrue(Math.hypot(c.x(), c.z()) >= s.eyeRadius() + CityLayout.HALF_EXTENT);
                int clear = CityLayout.HALF_EXTENT + CityLayout.CLEARANCE;
                for (CapitalLayout.Site cap : CapitalLayout.sitesInBox(seed, s, c.x() - clear, c.z() - clear, c.x() + clear, c.z() + clear)) {
                    assertFalse(Math.abs(cap.x() - c.x()) <= clear && Math.abs(cap.z() - c.z()) <= clear, "city overlaps a capital");
                }
                for (CityLayout.Site other : cities) {
                    if (other != c) {
                        assertTrue(Math.abs(other.x() - c.x()) > CityLayout.SIZE || Math.abs(other.z() - c.z()) > CityLayout.SIZE);
                    }
                }
            }
        }
    }

    @Test
    void everyCentreAndDistrictIsFoundFromItsChunk() {
        for (CityLayout.Site c : CityLayout.sitesInBox(9L, s, -20_000, -20_000, 20_000, 20_000)) {
            assertEquals(c, CityLayout.siteInChunk(9L, s, c.x() >> 4, c.z() >> 4).orElseThrow());
            for (CityLayout.District d : CityLayout.districts(c)) {
                assertEquals(d, CityLayout.districtInChunk(9L, s, d.x() >> 4, d.z() >> 4).orElseThrow());
            }
        }
    }

    @Test
    void farmsAreKeptOutOfCities() {
        for (CityLayout.Site c : CityLayout.sitesInBox(4L, s, -20_000, -20_000, 20_000, 20_000)) {
            for (int dx = -CityLayout.HALF_EXTENT; dx <= CityLayout.HALF_EXTENT + 1; dx += 8) {
                for (int dz = -CityLayout.HALF_EXTENT; dz <= CityLayout.HALF_EXTENT + 1; dz += 8) {
                    assertFalse(FarmLayout.isFarmChunk(4L, s, (c.x() + dx) >> 4, (c.z() + dz) >> 4), "farm inside city at " + c);
                }
            }
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
        assertTrue(CityLayout.sitesInBox(1L, off, -50_000, -50_000, 50_000, 50_000).isEmpty());
        assertFalse(CityLayout.nearCity(1L, off, 0, 0, 1000));
    }
}
