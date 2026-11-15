package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GarrisonTest {
    @Test
    void anArmyWalksAtItsPaceAndStopsAtItsGoal() {
        assertNull(Garrison.at(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0, 100), "home: not on the map");
        long half = (long) (500 / Garrison.PACE);
        assertArrayEquals(new double[]{500, 0}, Garrison.at(0, 0, 1000, 0, 0, half), 1);
        assertArrayEquals(new double[]{1000, 0}, Garrison.at(0, 0, 1000, 0, 0, half * 10), 1e-9);
        assertArrayEquals(new double[]{0, 0}, Garrison.at(0, 0, 1000, 0, 50, 10), 1e-9);
    }
}
