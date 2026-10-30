package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoadsTest {
    private static final int BPP = 16;
    private static final int SIZE = 160;

    /** Flat land 2,560 blocks a side; a river runs north-south down its middle; a block of mountains north of the middle. */
    private static Roads.Land land() {
        int n = SIZE * SIZE;
        int[] ground = new int[n];
        boolean[] wet = new boolean[n];
        boolean[] sea = new boolean[n];
        byte[] hard = new byte[n];
        for (int pz = 0; pz < SIZE; pz++) {
            for (int px = 0; px < SIZE; px++) {
                int i = pz * SIZE + px;
                ground[i] = 70;
                wet[i] = px == 80;
                if (px > 60 && px < 100 && pz > 40 && pz < 75) {
                    hard[i] = 2;
                    ground[i] = 160;
                }
            }
        }
        return new Roads.Land(0, 0, BPP, SIZE, SIZE, ground, wet, sea, hard, 63);
    }

    private static final Roads.Place WEST = new Roads.Place("Westford", 600, 1280, 160, 2);
    private static final Roads.Place EAST = new Roads.Place("Eastholm", 1960, 1280, 160, 2);

    @Test
    void aRoadJoinsTwoCitiesFromGateToGate() {
        Roads.Plan plan = Roads.plan(land(), List.of(new Roads.Link(Roads.Grade.HIGH, WEST, EAST)), List.of(), List.of(WEST, EAST));
        assertEquals(1, plan.roads().size());
        Roads.Road road = plan.roads().get(0);
        int[] start = Roads.gate(WEST, EAST);
        int[] end = Roads.gate(EAST, WEST);
        assertEquals(start[0], road.xs()[0]);
        assertEquals(start[1], road.zs()[0]);
        assertEquals(end[0], road.xs()[road.points() - 1]);
        assertEquals(end[1], road.zs()[road.points() - 1]);
        assertTrue(start[0] > WEST.x() + WEST.half(), "the road doesn't leave by the east gate");
        for (int i = 1; i < road.points(); i++) {
            double step = Math.hypot(road.xs()[i] - road.xs()[i - 1], road.zs()[i] - road.zs()[i - 1]);
            assertTrue(step <= Roads.POINT_SPACING + 1.5, "points " + step + " apart");
        }
    }

    @Test
    void roadsGoRoundMountainsAndCrossTheRiverOnce() {
        Roads.Plan plan = Roads.plan(land(), List.of(new Roads.Link(Roads.Grade.HIGH, WEST, EAST)), List.of(), List.of(WEST, EAST));
        Roads.Road road = plan.roads().get(0);
        int crossings = 0;
        for (int i = 0; i < road.points(); i++) {
            int px = road.xs()[i] / BPP;
            int pz = road.zs()[i] / BPP;
            assertFalse(px > 62 && px < 98 && pz > 42 && pz < 73, "a road over the mountains at " + road.xs()[i] + ", " + road.zs()[i]);
            if (i > 0 && (road.xs()[i - 1] / BPP < 80) != (px < 80)) {
                crossings++;
            }
        }
        assertEquals(1, crossings, "the road crossed the river " + crossings + " times");
    }

    @Test
    void aSpurJoinsTheNearestRoadAndForksAreNamed() {
        Roads.Place village = new Roads.Place("Village of Mill", 1280, 1900, 0, 0);
        Roads.Plan plan = Roads.plan(land(), List.of(new Roads.Link(Roads.Grade.HIGH, WEST, EAST)),
                List.of(new Roads.Spur(Roads.Grade.TOWN, village, false)), List.of(WEST, EAST));
        assertEquals(2, plan.roads().size(), "no spur to the village");
        Roads.Road spur = plan.roads().get(1);
        assertEquals("Village of Mill", spur.from());
        assertEquals(village.x(), spur.xs()[0]);
        assertFalse(plan.junctions().isEmpty(), "no fork where the spur meets the road");
        assertTrue(plan.junctions().stream().anyMatch(j -> j.leadsTo().contains("Village of Mill") && j.leadsTo().contains("Westford")));
    }
}
