package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityTerrainTest {
    private static final CityLayout.Site SITE = new CityLayout.Site(5000, -3000, false);
    private static final int SEA = 63;

    /** Local-coordinate terrain, wrapped as world heights for the site. */
    private static CityTerrain.Heights local(java.util.function.IntBinaryOperator f) {
        return (x, z) -> f.applyAsInt(x - SITE.originX(), z - SITE.originZ());
    }

    @Test
    void flatLandIsEasedAllRound() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> 80), SEA);
        assertEquals(80, plan.plateY());
        for (CityTerrain.Side side : CityTerrain.Side.values()) {
            for (int s = 0; s < CityTerrain.SEGMENTS_PER_SIDE; s++) {
                assertEquals(CityTerrain.Edge.EASED, plan.edge(side, s));
            }
        }
    }

    @Test
    void plateIsTheMedianOfTheWholeBox() {
        // West half of the box at 70, east half at 100: median lands on one of them, not the mean.
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> x < 200 ? 70 : 100), SEA);
        assertEquals(100, plan.plateY());
    }

    @Test
    void plateNeverSinksBelowTheSea() {
        assertEquals(SEA - 1, CityTerrain.plan(SITE, local((x, z) -> 40), SEA).plateY());
    }

    @Test
    void gentleHillOutsideIsEasedDeepValleyAndCliffAreNot() {
        // Plate at 80. North: land 15 lower (a slope reaches it). South: 60 lower (valley). East: 60 higher (cliff).
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> {
            if (z < CityTerrain.BOX_MIN) {
                return 65;
            }
            if (z > CityTerrain.BOX_MAX) {
                return 20;
            }
            if (x > CityTerrain.BOX_MAX) {
                return 140;
            }
            return 80;
        }), SEA);
        assertEquals(80, plan.plateY());
        assertEquals(CityTerrain.Edge.EASED, plan.edge(CityTerrain.Side.NORTH, 5));
        assertEquals(CityTerrain.Edge.VALLEY, plan.edge(CityTerrain.Side.SOUTH, 5));
        assertEquals(CityTerrain.Edge.CLIFF, plan.edge(CityTerrain.Side.EAST, 5));
        assertEquals(CityTerrain.Edge.EASED, plan.edge(CityTerrain.Side.WEST, 5));
    }

    @Test
    void slopeDropsOneBlockForEveryTwoOut() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> 80), SEA);
        int along = 200;
        for (int d = 1; d <= CityTerrain.SLOPE_REACH; d++) {
            CityTerrain.Column c = CityTerrain.column(plan, along, CityTerrain.BOX_MIN - d);
            assertEquals(CityTerrain.Kind.SLOPE, c.kind());
            assertEquals(80 - (d + 1) / 2, c.low(), "fill height " + d + " out");
            assertEquals(80 + (d + 1) / 2, c.high(), "cut height " + d + " out");
        }
        assertEquals(CityTerrain.Kind.NONE, CityTerrain.column(plan, along, CityTerrain.BOX_MIN - CityTerrain.SLOPE_REACH - 1).kind());
    }

    @Test
    void slopesFormAPyramidAtTheCorners() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> 80), SEA);
        // Diagonal out from the north-west corner: height follows the larger of the two distances.
        CityTerrain.Column c = CityTerrain.column(plan, CityTerrain.BOX_MIN - 10, CityTerrain.BOX_MIN - 4);
        assertEquals(80 - 5, c.low());
    }

    @Test
    void valleySideGetsABrickRimAndNoSlope() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> z > CityTerrain.BOX_MAX ? 10 : 80), SEA);
        assertEquals(CityTerrain.Kind.RIM, CityTerrain.column(plan, 200, CityTerrain.BOX_MAX).kind());
        assertEquals(CityTerrain.Kind.PLATE, CityTerrain.column(plan, 200, CityTerrain.BOX_MAX - 1).kind());
        assertEquals(CityTerrain.Kind.NONE, CityTerrain.column(plan, 200, CityTerrain.BOX_MAX + 5).kind());
        // The north edge faces flat land, so it gets no rim.
        assertEquals(CityTerrain.Kind.PLATE, CityTerrain.column(plan, 200, CityTerrain.BOX_MIN).kind());
    }

    @Test
    void cliffSideIsLeftAlone() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> x > CityTerrain.BOX_MAX ? 200 : 80), SEA);
        assertEquals(CityTerrain.Kind.PLATE, CityTerrain.column(plan, CityTerrain.BOX_MAX, 200).kind());
        assertEquals(CityTerrain.Kind.NONE, CityTerrain.column(plan, CityTerrain.BOX_MAX + 3, 200).kind());
    }

    @Test
    void sameTerrainSamePlan() {
        CityTerrain.Heights h = local((x, z) -> 70 + Math.floorMod(x * 31 + z * 17, 25));
        assertEquals(CityTerrain.plan(SITE, h, SEA), CityTerrain.plan(SITE, h, SEA));
    }

    @Test
    void everyColumnIsOwnedByExactlyOneCellWithinReachOfItsStart() {
        Set<Long> seen = new HashSet<>();
        for (int cx = 0; cx < CityLayout.CELLS_PER_SIDE; cx++) {
            for (int cz = 0; cz < CityLayout.CELLS_PER_SIDE; cz++) {
                int startX = (cx - 1) * CityLayout.DISTRICT_SPACING + CityLayout.HALF_EXTENT;
                int startZ = (cz - 1) * CityLayout.DISTRICT_SPACING + CityLayout.HALF_EXTENT;
                for (int lx = CityTerrain.ownedMin(cx); lx <= CityTerrain.ownedMax(cx); lx += 3) {
                    for (int lz = CityTerrain.ownedMin(cz); lz <= CityTerrain.ownedMax(cz); lz += 3) {
                        assertEquals(cx, CityTerrain.cellOf(lx));
                        assertEquals(cz, CityTerrain.cellOf(lz));
                        assertTrue(seen.add(((long) lx << 32) | (lz & 0xFFFFFFFFL)));
                        assertTrue(Math.abs(lx - startX) <= 128 && Math.abs(lz - startZ) <= 128,
                                "column " + lx + "," + lz + " out of reach of cell " + cx + "," + cz);
                    }
                }
            }
        }
        assertFalse(seen.isEmpty());
        // The whole box plus slopes is covered.
        assertEquals(CityTerrain.BOX_MIN - CityTerrain.SLOPE_REACH, CityTerrain.ownedMin(0));
        assertEquals(CityTerrain.BOX_MAX + CityTerrain.SLOPE_REACH, CityTerrain.ownedMax(CityLayout.CELLS_PER_SIDE - 1));
    }
}
