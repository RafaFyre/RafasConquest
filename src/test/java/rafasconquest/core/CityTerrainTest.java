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
    private static final int MID = 200;

    /** Local-coordinate terrain, wrapped as world heights for the site. */
    private static CityTerrain.Heights local(java.util.function.IntBinaryOperator f) {
        return (x, z) -> f.applyAsInt(x - SITE.originX(), z - SITE.originZ());
    }

    /** Plate at 80; land north of the box at {@code north}, everywhere else level with the plate. */
    private static CityTerrain.Plan northAt(int north) {
        return CityTerrain.plan(SITE, local((x, z) -> z < CityTerrain.BOX_MIN ? north : 80), SEA);
    }

    @Test
    void plateIsTheMedianOfTheWholeBox() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> x < 200 ? 70 : 100), SEA);
        assertEquals(100, plan.plateY());
    }

    @Test
    void plateNeverSinksBelowTheSea() {
        assertEquals(SEA - 1, CityTerrain.plan(SITE, local((x, z) -> 40), SEA).plateY());
    }

    @Test
    void levelOrRisingLandIsCarvedNeverFilled() {
        for (int north : new int[]{80, 95, 140}) {
            CityTerrain.Plan plan = northAt(north);
            assertEquals(CityTerrain.Edge.CARVE, plan.edge(CityTerrain.Side.NORTH, 6), "land at " + north);
            for (int d = 1; d <= CityTerrain.SLOPE_REACH; d++) {
                assertFalse(CityTerrain.column(plan, MID, CityTerrain.BOX_MIN - d).fill(), "fill on rising land, " + d + " out");
            }
        }
    }

    @Test
    void fallingLandIsFilledAtOneUpTwoOut() {
        CityTerrain.Plan plan = northAt(65);
        assertEquals(CityTerrain.Edge.FILL, plan.edge(CityTerrain.Side.NORTH, 6));
        for (int d = 1; d <= CityTerrain.SLOPE_REACH; d++) {
            CityTerrain.Column c = CityTerrain.column(plan, MID, CityTerrain.BOX_MIN - d);
            assertTrue(c.fill());
            assertEquals(80 - (d + 1) / 2, c.low(), "fill height " + d + " out");
        }
    }

    @Test
    void gentleCarveIsOneUpTwoOut() {
        CityTerrain.Plan plan = northAt(95); // 15 above the plate: 1:2 reaches it well within 44 blocks
        for (int d = 1; d <= CityTerrain.SLOPE_REACH; d++) {
            assertEquals(80 + (d + 1) / 2, CityTerrain.column(plan, MID, CityTerrain.BOX_MIN - d).high());
        }
    }

    @Test
    void steepLandIsCarvedSteeperSoTheCarveNeverEndsInAWall() {
        CityTerrain.Plan plan = northAt(140); // 60 above the plate; 1:2 would only reach +22
        assertEquals(60, plan.carveTo()[CityTerrain.Side.NORTH.ordinal()][6]);
        CityTerrain.Column atReach = CityTerrain.column(plan, MID, CityTerrain.BOX_MIN - CityTerrain.SLOPE_REACH);
        assertTrue(atReach.high() >= 140, "carve ends at " + atReach.high() + ", below the land at 140");
        int previous = 80;
        for (int d = 1; d <= CityTerrain.SLOPE_REACH; d++) {
            int high = CityTerrain.column(plan, MID, CityTerrain.BOX_MIN - d).high();
            assertTrue(high >= previous && high - previous <= 2, "carve jumps from " + previous + " to " + high + " at " + d);
            previous = high;
        }
    }

    @Test
    void carveBlendsBetweenStretches() {
        // A mountain rising only in front of the west half of the north edge.
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> z < CityTerrain.BOX_MIN && x < 240 ? 150 : 80), SEA);
        int z = CityTerrain.BOX_MIN - CityTerrain.SLOPE_REACH;
        int previous = CityTerrain.column(plan, CityTerrain.BOX_MIN, z).high();
        for (int x = CityTerrain.BOX_MIN + 1; x <= CityTerrain.BOX_MAX; x++) {
            int high = CityTerrain.column(plan, x, z).high();
            assertTrue(Math.abs(high - previous) <= 3, "carve steps from " + previous + " to " + high + " at x " + x);
            previous = high;
        }
    }

    @Test
    void cornersTakeTheSteeperSide() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> z < CityTerrain.BOX_MIN ? 140 : 80), SEA);
        CityTerrain.Column corner = CityTerrain.column(plan, CityTerrain.BOX_MIN - 44, CityTerrain.BOX_MIN - 44);
        assertTrue(corner.high() >= 140);
    }

    @Test
    void valleyHasNoFillAndTheWallStandsOnTheCliff() {
        CityTerrain.Plan plan = CityTerrain.plan(SITE, local((x, z) -> z > CityTerrain.BOX_MAX ? 10 : 80), SEA);
        assertEquals(CityTerrain.Edge.VALLEY, plan.edge(CityTerrain.Side.SOUTH, 6));
        int far = CityLayout.SIZE - 1;
        // Outside the wall: nothing built, only anything above the plate cleared.
        assertEquals(CityTerrain.Kind.CLEAR_ABOVE, CityTerrain.column(plan, MID, far + 1).kind());
        assertEquals(CityTerrain.Kind.CLEAR_ABOVE, CityTerrain.column(plan, MID, CityTerrain.BOX_MAX).kind());
        // Under the wall: bare stone fill, so the cliff face below the wall is natural.
        for (int z = far - CityLayout.ROAD_WIDTH + 1; z <= far; z++) {
            assertEquals(CityTerrain.Kind.WALL_BASE, CityTerrain.column(plan, MID, z).kind());
        }
        // Just inside the wall: ordinary plate.
        assertEquals(CityTerrain.Kind.PLATE, CityTerrain.column(plan, MID, far - CityLayout.ROAD_WIDTH).kind());
        // Beyond the box: no fill down into the valley.
        assertFalse(CityTerrain.column(plan, MID, CityTerrain.BOX_MAX + 10).fill());
        // A side facing level land keeps its ledge.
        assertEquals(CityTerrain.Kind.PLATE, CityTerrain.column(plan, MID, CityTerrain.BOX_MIN).kind());
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
        assertEquals(CityTerrain.BOX_MIN - CityTerrain.SLOPE_REACH, CityTerrain.ownedMin(0));
        assertEquals(CityTerrain.BOX_MAX + CityTerrain.SLOPE_REACH, CityTerrain.ownedMax(CityLayout.CELLS_PER_SIDE - 1));
    }
}
