package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutTest {
    private final WorldSettings s = WorldSettings.defaults();

    @Test
    void stormWallRingHasSixCapitalsWithinJitter() {
        for (long seed : new long[]{0L, 1L, -42L, 8_675_309L, Long.MAX_VALUE}) {
            List<CapitalLayout.Site> ring = CapitalLayout.ringSites(seed, s);
            assertEquals(6, ring.size());
            for (CapitalLayout.Site site : ring) {
                double d = site.distanceFromSpawn();
                assertTrue(d >= 2100 - 1 && d <= 2900 + 1, "ring site at " + d);
                OriginRoller.Roll roll = OriginRoller.roll(seed, site.x(), site.z(), s);
                assertTrue(roll.radiusSteps() >= 1 && roll.radiusSteps() <= 3);
                assertTrue(roll.strength() >= 300 && roll.strength() <= 1500);
            }
        }
    }

    @Test
    void sameSeedSameLayout() {
        assertEquals(CapitalLayout.ringSites(99L, s), CapitalLayout.ringSites(99L, s));
        assertEquals(CapitalLayout.sitesInBox(99L, s, -20_000, -20_000, 20_000, 20_000),
                CapitalLayout.sitesInBox(99L, s, -20_000, -20_000, 20_000, 20_000));
        assertEquals(OriginRoller.roll(99L, 2500, 10, s), OriginRoller.roll(99L, 2500, 10, s));
    }

    @Test
    void outerCapitalsLieBeyondR2() {
        for (CapitalLayout.Site site : CapitalLayout.sitesInBox(7L, s, -40_000, -40_000, 40_000, 40_000)) {
            if (!site.ring()) {
                assertTrue(site.distanceFromSpawn() > s.warZoneOuterEdge());
            } else {
                assertTrue(site.distanceFromSpawn() > s.eyeRadius());
            }
        }
    }

    @Test
    void siteInChunkFindsEverySite() {
        for (CapitalLayout.Site site : CapitalLayout.sitesInBox(5L, s, -30_000, -30_000, 30_000, 30_000)) {
            assertEquals(site, CapitalLayout.siteInChunk(5L, s, site.x() >> 4, site.z() >> 4).orElseThrow());
        }
    }

    @Test
    void farmCandidatesRespectSeparation() {
        int range = s.farmSpacingChunks() - s.farmSeparationChunks();
        for (int cx = -10; cx <= 10; cx++) {
            for (int cz = -10; cz <= 10; cz++) {
                FarmLayout.ChunkCoord c = FarmLayout.candidateChunk(3L, s, cx, cz);
                assertTrue(c.x() - cx * s.farmSpacingChunks() >= 0 && c.x() - cx * s.farmSpacingChunks() < range);
                assertTrue(c.z() - cz * s.farmSpacingChunks() >= 0 && c.z() - cz * s.farmSpacingChunks() < range);
                assertTrue(FarmLayout.isCandidateChunk(3L, s, c.x(), c.z()));
                assertTrue(!FarmLayout.isCandidateChunk(3L, s, c.x() + 1, c.z()) || range == 1);
            }
        }
    }
}
