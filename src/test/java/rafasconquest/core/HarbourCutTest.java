package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HarbourCutTest {
    @Test
    void threeStepsFiveDeepThenTheGentleSlopeDownToTheDeepest() {
        assertEquals(57, HarbourCut.floor(62, 1));
        assertEquals(52, HarbourCut.floor(62, 2));
        assertEquals(47, HarbourCut.floor(62, 3));
        assertEquals(46, HarbourCut.floor(62, 4));
        assertEquals(46, HarbourCut.floor(62, 5));
        assertEquals(45, HarbourCut.floor(62, 6));
        assertEquals(62 - HarbourCut.DEEPEST, HarbourCut.floor(62, HarbourCut.REACH));
    }

    @Test
    void boxStartsPastTheWharfAndSpansTheWall() {
        int in = HarbourCut.start(4);
        int out = in + HarbourCut.REACH - 1;
        assertArrayEquals(new int[]{-4, -out, 103, -in}, HarbourCut.box(CityLayout.Side.NORTH, 100, 4));
        assertArrayEquals(new int[]{99 + in, -4, 99 + out, 103}, HarbourCut.box(CityLayout.Side.EAST, 100, 4));
    }
}
