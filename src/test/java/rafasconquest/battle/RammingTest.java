package rafasconquest.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RammingTest {
    /** A ship 23 long and 7 wide, as the battle ships are. */
    private static Ramming.Hull ship(double x, double z, double dirX, double dirZ, double speed) {
        return new Ramming.Hull(x, z, dirX, dirZ, 11.5, 3.5, dirX * speed, dirZ * speed);
    }

    @Test
    void aBowIntoTheSideThrowsTheCrewAlongTheRammersCourse() {
        Ramming.Hull rammer = ship(-14, 0, 1, 0, 4); // its bow at x = -2.5, just past the side at x = -3.5
        Ramming.Hull struck = ship(0, 0, 0, 1, 0);
        double closing = Ramming.closing(rammer, struck);
        assertEquals(4, closing, 1e-9);
        double[] knock = Ramming.knock(rammer, struck, closing);
        assertEquals(4 * Ramming.THROW_PER_SPEED, knock[0], 1e-9);
        assertEquals(0, knock[1], 1e-9);
        assertEquals(0, Ramming.closing(struck, rammer), "the ship struck didn't ram");
    }

    @Test
    void headOnBlowsThrowNobody() {
        Ramming.Hull a = ship(0, -11, 0, 1, 2.5);
        Ramming.Hull b = ship(0, 11, 0, -1, 2.5);
        double closing = Ramming.closing(a, b);
        assertEquals(5, closing, 1e-9, "both speeds close the gap");
        double[] knock = Ramming.knock(a, b, closing);
        assertEquals(0, knock[0]);
        assertEquals(0, knock[1]);
    }

    @Test
    void shipsApartOrDriftingDontRam() {
        assertEquals(0, Ramming.closing(ship(-30, 0, 1, 0, 4), ship(0, 0, 0, 1, 0)), "out of reach");
        assertEquals(0, Ramming.closing(ship(-14, 0, 1, 0, 0.1), ship(0, 0, 0, 1, 0)), "only drifting alongside");
        double[] hard = Ramming.knock(ship(-14, 0, 1, 0, 100), ship(0, 0, 0, 1, 0), 100);
        assertTrue(hard[0] <= Ramming.MAX_THROW + 1e-9, "thrown too far");
    }

    @Test
    void footprintsOverlapOnlyWhereTheHullsMeet() {
        Ramming.Hull a = ship(0, 0, 0, 1, 0);
        assertTrue(Ramming.overlap(a, ship(5, 0, 0, 1, 0), 0), "side by side, 5 apart, with 7-wide hulls");
        assertTrue(!Ramming.overlap(a, ship(20, 0, 0, 1, 0), 0), "a line abreast, 20 apart");
        assertTrue(!Ramming.overlap(a, ship(0, 24, 0, 1, 0), 0), "bow to stern, a hair clear");
        assertTrue(Ramming.overlap(a, ship(0, 24, 0, 1, 0), 1), "the same, with room kept");
        assertTrue(Ramming.overlap(a, ship(14, 0, 1, 0, 0), 0), "a bow across the side");
        assertTrue(!Ramming.overlap(a, ship(16, 0, 1, 0, 0), 0), "a bow just short of the side");
    }
}
