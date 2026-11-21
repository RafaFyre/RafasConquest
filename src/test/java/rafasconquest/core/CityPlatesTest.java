package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityPlatesTest {
    // A 100-block box from (0, 0): plates 0..3 a side, the last only 4 blocks in it; walls 30..69.
    private final CityPlates plates = new CityPlates(0, 0, 100, 64, new int[]{30, 30, 69, 69}, null, null);

    @Test
    void platesTouchingTheBoxOrAnAcceptedOneAreAccepted() {
        long west = CityPlates.key(-1, 1);
        long further = CityPlates.key(-2, 1);
        long corner = CityPlates.key(-1, -1); // only a corner touches the box
        long far = CityPlates.key(-5, 1);
        assertEquals(List.of(further, west), plates.accept(List.of(further, west, corner, far), Set.of()));
    }

    @Test
    void partlyInBoxPlateIsAcceptedButWallsAndFlatOnesAreNot() {
        assertEquals(List.of(CityPlates.key(3, 0)), plates.accept(List.of(CityPlates.key(3, 0)), Set.of()));
        assertEquals(List.of(), plates.accept(List.of(CityPlates.key(0, 0), CityPlates.key(1, 1)), Set.of())); // in the box; over the walls
        long done = CityPlates.key(-1, 1);
        assertEquals(List.of(CityPlates.key(-2, 1)), plates.accept(List.of(done, CityPlates.key(-2, 1)), Set.of(done)));
    }

    @Test
    void keysRoundTripNegatives() {
        long k = CityPlates.key(-3, -7);
        assertEquals(-3, CityPlates.i(k));
        assertEquals(-7, CityPlates.j(k));
        assertEquals(CityPlates.key(-1, 0), plates.at(-0.5, 31.9));
    }
}
