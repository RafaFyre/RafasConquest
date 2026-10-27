package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityLayoutTest {
    private final WorldSettings s = WorldSettings.defaults();

    @Test
    void cityIsThreeVillagesWide() {
        // A vanilla village reaches 80 blocks from its centre (160 across); a city spans 3 × 160.
        assertEquals(480, 2 * CityLayout.HALF_EXTENT);
        assertEquals(8, CityLayout.districts(new CityLayout.Site(0, 0)).size());
        assertTrue(CityLayout.JIGSAW_REACH * 2 < CityLayout.DISTRICT_SPACING, "district cells must not overlap");
        assertTrue(CityLayout.JIGSAW_REACH <= 128, "each start must stay within the engine's 128-block reach");
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
                        assertTrue(Math.abs(other.x() - c.x()) >= 2 * CityLayout.HALF_EXTENT || Math.abs(other.z() - c.z()) >= 2 * CityLayout.HALF_EXTENT);
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
            for (int dx = -CityLayout.HALF_EXTENT; dx <= CityLayout.HALF_EXTENT; dx += 16) {
                for (int dz = -CityLayout.HALF_EXTENT; dz <= CityLayout.HALF_EXTENT; dz += 16) {
                    int cx = (c.x() + dx) >> 4;
                    int cz = (c.z() + dz) >> 4;
                    assertFalse(FarmLayout.isFarmChunk(4L, s, cx, cz), "farm inside city at " + c);
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
