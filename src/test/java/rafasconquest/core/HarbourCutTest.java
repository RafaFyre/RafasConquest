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
    void cutRunsPastARiverToTheOpenSea() {
        // a river 16 wide at 1..16, land, then the sea from 41 on
        assertEquals(49, HarbourCut.coast(d -> d <= 16 || d >= 41, 8));
        // the sea right at the wharf
        assertEquals(9, HarbourCut.coast(d -> true, 8));
        // no sea within reach
        assertEquals(HarbourCut.REACH, HarbourCut.coast(d -> d <= 16, 8));
    }

    @Test
    void cutClearsAnIslandOutInTheSea() {
        // sea from the wharf, an island at 41..49, sea again from 57
        assertEquals(65, HarbourCut.coast(d -> d < 41 || d > 49, 8));
        // a far shore from 57 that runs on to the reach is not cut
        assertEquals(9, HarbourCut.coast(d -> d < 57, 8));
    }

    @Test
    void boxStartsPastTheWharfAndSpansTheWall() {
        int in = HarbourCut.start(4);
        int out = in + HarbourCut.REACH - 1;
        assertArrayEquals(new int[]{-4, -out, 103, -in}, HarbourCut.box(CityLayout.Side.NORTH, 100, 4));
        assertArrayEquals(new int[]{99 + in, -4, 99 + out, 103}, HarbourCut.box(CityLayout.Side.EAST, 100, 4));
    }
}
