package rafasconquest.client;

import org.junit.jupiter.api.Test;
import rafasconquest.core.SeaLanes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DistantShipsTest {
    private static DistantShips.Want want(double x) {
        return new DistantShips.Want(x, x, 0, 0, SeaLanes.Size.MEDIUM, null, null);
    }

    @Test
    void oneRealShipIsGivenToOneShipOnly() {
        double[] real = {10, 0, 0, 0, 0};
        // Both ships lie within reach of the one real ship: the nearer has it, the other is drawn as a stand-in.
        double[][] owns = DistantShips.match(List.of(want(0), want(8)), List.of(real));
        assertNull(owns[0]);
        assertSame(real, owns[1]);
    }

    @Test
    void eachShipKeepsItsOwnRealShip() {
        double[] a = {1, 0, 0, 0, 0};
        double[] b = {11, 0, 0, 0, 0};
        // Ship 0 is within reach of both; the old first-found rule gave it b and left ship 1 without a hull.
        double[][] owns = DistantShips.match(List.of(want(0), want(10)), List.of(b, a));
        assertSame(a, owns[0]);
        assertSame(b, owns[1]);
    }
    @Test
    void equalHullsShareOneKey() {
        // A lane's hull list is made anew every few seconds: the new list, equal to the old, must find the old model.
        java.util.List<rafasconquest.style.ShipHulls.Part> first = new java.util.ArrayList<>();
        java.util.List<rafasconquest.style.ShipHulls.Part> again = new java.util.ArrayList<>();
        assertSame(DistantShips.hullKey(first), DistantShips.hullKey(again));
        assertSame(DistantShips.hullKey(first), DistantShips.hullKey(first));
    }
}
