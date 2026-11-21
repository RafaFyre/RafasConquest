package rafasconquest.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindTest {
    private static final double SOUTH = 0; // blowing toward +z: from the north

    @Test
    void aBeamReachIsFastestAndIntoTheWindAlmostStill() {
        assertEquals(1, Wind.pace(1, 0, SOUTH), 1e-9);
        assertEquals(0.75, Wind.pace(0, 1, SOUTH), 1e-9);
        assertEquals(0.1, Wind.pace(0, -1, SOUTH), 1e-9);
    }

    @Test
    void itSailsCloseHauledOnTheTackItIsOnWhenItsMarkLiesUpwind() {
        double c = Math.sqrt(0.5);
        assertArrayEquals(new double[]{c, -c}, Wind.steer(0, -1, 1, 0, SOUTH), 1e-9);
        assertArrayEquals(new double[]{-c, -c}, Wind.steer(0, -1, -1, 0, SOUTH), 1e-9);
        assertArrayEquals(new double[]{1, 0}, Wind.steer(1, 0, 0, 1, SOUTH), 1e-9, "a reach is sailed straight");
    }

    @Test
    void itBeatsUpToAMarkUpwindInAFewLongBoards() {
        double x = 0, z = 0, fx = 1, fz = 0; // lying east, its mark 200 blocks upwind
        int tacks = 0;
        int s = 0;
        for (; s < 600 && Math.hypot(x, z + 200) > 3; s++) {
            double d = Math.hypot(x, z + 200);
            double[] want = Wind.steer(-x / d, (-200 - z) / d, fx, fz, SOUTH);
            double off = Math.atan2(fx * want[1] - fz * want[0], fx * want[0] + fz * want[1]);
            double turn = Math.max(-FieldBattle.TURN_RATE, Math.min(FieldBattle.TURN_RATE, off));
            double nx = fx * Math.cos(turn) - fz * Math.sin(turn);
            double nz = fx * Math.sin(turn) + fz * Math.cos(turn);
            if (Math.signum(nx) != Math.signum(fx) && Math.abs(fx) > 0.1) {
                tacks++;
            }
            fx = nx;
            fz = nz;
            double pace = Math.min(2.5, d) * Wind.pace(fx, fz, SOUTH);
            x += fx * pace;
            z += fz * pace;
        }
        assertTrue(Math.hypot(x, z + 200) <= 3, "never got there");
        assertTrue(tacks <= 2, tacks + " tacks");
    }

    @Test
    void itNamesWhereTheWindComesFrom() {
        assertEquals("north", Wind.from(SOUTH));
        assertEquals("west", Wind.from(Math.PI / 2)); // toward +x, east
    }

    @Test
    void itTurnsSteadilyOneWay() {
        assertEquals(Wind.TURN * 60, Wind.at(0, 1, 60), 1e-12);
        assertEquals(-Wind.TURN * 60, Wind.at(0, -1, 60), 1e-12);
    }
}
