package rafasconquest.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OarRowingTest {
    private static final double TURN = 0.01; // radians a tick, well past the turning threshold

    @Test
    void aheadAndAsternRowAllOarsTheSameWay() {
        assertEquals(1, OarFigures.Rowing.target(0.2, 0, true));
        assertEquals(1, OarFigures.Rowing.target(0.2, 0, false));
        assertEquals(-1, OarFigures.Rowing.target(-0.2, 0, true));
        assertEquals(-1, OarFigures.Rowing.target(-0.2, 0, false));
        assertEquals(0, OarFigures.Rowing.target(0, 0, true));
    }

    @Test
    void turningRowsTheOutsideAheadAndTheInsideBackwards() {
        // Left turn: right oars ahead, left oars backwards, even while making way.
        assertEquals(1, OarFigures.Rowing.target(0.2, TURN, true));
        assertEquals(-1, OarFigures.Rowing.target(0.2, TURN, false));
        // Right turn: left oars ahead, right oars backwards.
        assertEquals(1, OarFigures.Rowing.target(0, -TURN, false));
        assertEquals(-1, OarFigures.Rowing.target(0, -TURN, true));
    }
}
