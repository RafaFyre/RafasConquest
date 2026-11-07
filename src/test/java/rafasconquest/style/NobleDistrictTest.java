package rafasconquest.style;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NobleDistrictTest {
    @Test
    void aSixthOfTheCityInTheMiddleOrAgainstAnEdge() {
        for (int bps : new int[]{5, 10, 15, 25, 30}) {
            for (int edge = -1; edge < 4; edge++) {
                int[] d = CityPlan.district(bps, edge);
                int x = d[0], z = d[1], side = d[2];
                double share = (double) side * side / (bps * bps);
                assertTrue(share > 0.15 && share <= 0.17, bps + " " + edge + ": " + share);
                assertTrue(x >= 0 && z >= 0 && x + side <= bps && z + side <= bps);
                // Centred across the edge it does not touch (or both, in the middle).
                if (edge != 1 && edge != 3) {
                    assertEquals((bps - side) / 2, x);
                }
                if (edge != 0 && edge != 2) {
                    assertEquals((bps - side) / 2, z);
                }
            }
            assertEquals(0, CityPlan.district(bps, 0)[1]);
            assertEquals(bps, CityPlan.district(bps, 1)[0] + CityPlan.district(bps, 1)[2]);
            assertEquals(bps, CityPlan.district(bps, 2)[1] + CityPlan.district(bps, 2)[2]);
            assertEquals(0, CityPlan.district(bps, 3)[0]);
        }
    }

    @Test
    void theBarracksStandOutsideTheNobleDistrict() {
        for (int bps : new int[]{5, 10, 15, 25}) {
            int c = bps / 2;
            for (int edge = -1; edge < 4; edge++) {
                int[] d = CityPlan.district(bps, edge);
                int[] b = CityPlan.barracksBlock(bps, c, d[0], d[0] + d[2], d[1], d[1] + d[2]);
                boolean inside = b[0] >= d[0] && b[0] < d[0] + d[2] && b[1] >= d[1] && b[1] < d[1] + d[2];
                assertTrue(!inside && !(b[0] == c && b[1] == c), bps + " " + edge);
            }
        }
        assertArrayEquals(new int[]{6, 5}, CityPlan.barracksBlock(10, 5, 0, 0, 0, 0)); // no district: east of the centre
    }

    @Test
    void aSpurRunsFromTheDoorToTheNearestRoad() {
        int[] bounds = {0, 100, 0, 100};
        CityPlan.Piece tower = new CityPlan.Piece(CityPlan.Kind.NOBLE, "t", 40, 40, 10, 10, 5, 0); // door north
        List<int[]> roads = List.of(new int[]{0, 20, 99, 22});
        assertArrayEquals(new int[][]{{44, 23, 46, 39}}, CityPlan.spur(tower, List.of(tower), roads, bounds).toArray(int[][]::new));
        // Another building in the way north: east to the road there instead.
        CityPlan.Piece other = new CityPlan.Piece(CityPlan.Kind.NOBLE, "o", 40, 25, 10, 10, 5, 0);
        List<int[]> both = List.of(roads.get(0), new int[]{60, 0, 62, 99});
        assertArrayEquals(new int[][]{{50, 44, 59, 46}}, CityPlan.spur(tower, List.of(tower, other), both, bounds).toArray(int[][]::new));
        // Only the road north: out east, then turning north past the other building.
        assertArrayEquals(new int[][]{{50, 44, 52, 46}, {50, 23, 52, 43}},
                CityPlan.spur(tower, List.of(tower, other), roads, bounds).toArray(int[][]::new));
        assertNull(CityPlan.spur(tower, List.of(tower, other), List.of(), bounds));
    }

    @Test
    void bigBuildingsMergeAsManyBlocksAsTheyNeed() {
        assertEquals(1, CityPlan.blocksFor(14));
        assertEquals(1, CityPlan.blocksFor(29)); // a whole block: 32 less its road
        assertEquals(2, CityPlan.blocksFor(30));
        assertEquals(2, CityPlan.blocksFor(61)); // two blocks and the road between them
        assertEquals(3, CityPlan.blocksFor(62));
    }
}
