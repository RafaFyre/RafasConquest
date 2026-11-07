package rafasconquest.style;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void bigBuildingsMergeAsManyBlocksAsTheyNeed() {
        assertEquals(1, CityPlan.blocksFor(14));
        assertEquals(1, CityPlan.blocksFor(29)); // a whole block: 32 less its road
        assertEquals(2, CityPlan.blocksFor(30));
        assertEquals(2, CityPlan.blocksFor(61)); // two blocks and the road between them
        assertEquals(3, CityPlan.blocksFor(62));
    }
}
