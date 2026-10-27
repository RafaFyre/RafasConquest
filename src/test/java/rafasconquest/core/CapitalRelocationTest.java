package rafasconquest.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CapitalRelocationTest {
    private static final long SEED = 12345L;
    private final WorldSettings s = WorldSettings.defaults();

    @AfterEach
    void clearLand() {
        CapitalLayout.setLand(0, null);
    }

    /** "Ocean" within 300 blocks of the first ring Capital; land everywhere else. */
    private CapitalLayout.Land oceanAround(CapitalLayout.Site wet) {
        return (x, z) -> Math.abs(x - wet.x()) > 300 || Math.abs(z - wet.z()) > 300;
    }

    @Test
    void capitalOnOceanMovesToTheNearestLandAndOthersStay() {
        List<CapitalLayout.Site> seeded = CapitalLayout.ringSites(SEED, s);
        CapitalLayout.Site wet = seeded.get(0);
        CapitalLayout.Land land = oceanAround(wet);
        CapitalLayout.setLand(SEED, land);

        List<CapitalLayout.Site> placed = CapitalLayout.sitesInBox(SEED, s, -5_000, -5_000, 5_000, 5_000).stream()
                .filter(CapitalLayout.Site::ring).toList();
        assertEquals(seeded.size(), placed.size(), "every ring Capital still exists");
        CapitalLayout.Site moved = placed.stream().filter(p -> !seeded.contains(p)).findFirst().orElseThrow();
        assertTrue(land.cityFits(moved.x(), moved.z()));
        double shift = Math.hypot(moved.x() - wet.x(), moved.z() - wet.z());
        assertTrue(shift > 300 && shift <= 300 + 2 * CapitalLayout.SHIFT_STEP, "moved only as far as the land: " + shift);
        for (CapitalLayout.Site site : seeded.subList(1, seeded.size())) {
            assertTrue(placed.contains(site), site + " was on land and should not move");
        }
        // Chunk lookups follow the move.
        assertEquals(moved, CapitalLayout.siteInChunk(SEED, s, moved.x() >> 4, moved.z() >> 4).orElseThrow());
        assertTrue(CapitalLayout.siteInChunk(SEED, s, wet.x() >> 4, wet.z() >> 4).isEmpty());
    }

    @Test
    void movedCapitalKeepsClearOfCityStates() {
        CapitalLayout.Site wet = CapitalLayout.ringSites(SEED, s).get(0);
        CapitalLayout.setLand(SEED, oceanAround(wet));
        CapitalLayout.Site moved = CapitalLayout.relocated(SEED, s, wet, oceanAround(wet));
        int clear = CityLayout.SIZE + CityLayout.CLEARANCE;
        for (CityLayout.Site city : CityLayout.sitesInBox(SEED, s, moved.x() - clear, moved.z() - clear, moved.x() + clear, moved.z() + clear)) {
            assertFalse(Math.abs(city.x() - moved.x()) <= clear && Math.abs(city.z() - moved.z()) <= clear, "too close to " + city);
        }
    }

    @Test
    void withNoLandNearbyTheSiteStaysPut() {
        CapitalLayout.Site wet = CapitalLayout.ringSites(SEED, s).get(0);
        CapitalLayout.setLand(SEED, (x, z) -> false);
        assertEquals(wet, CapitalLayout.relocated(SEED, s, wet, (x, z) -> false));
    }

    @Test
    void withoutAWorldNothingMoves() {
        assertEquals(CapitalLayout.rawSitesInBox(SEED, s, -20_000, -20_000, 20_000, 20_000),
                CapitalLayout.sitesInBox(SEED, s, -20_000, -20_000, 20_000, 20_000));
    }
}
