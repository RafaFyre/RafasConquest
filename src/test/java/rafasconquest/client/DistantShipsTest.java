package rafasconquest.client;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.core.SeaLanes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DistantShipsTest {
    private static DistantShips.Want want(double x) {
        return new DistantShips.Want(x, x, 0, 0, SeaLanes.Size.MEDIUM, null, null);
    }

    private static DistantShips.Want want(double x, UUID sub) {
        return new DistantShips.Want(x, x, 0, 0, SeaLanes.Size.MEDIUM, null,
                new FormationSnapshot.Ship(0, 0, SeaLanes.Size.MEDIUM.ordinal(), -1, 0, sub));
    }

    /** The real ships, in this order, each by a fresh id. */
    private static Map<UUID, double[]> seen(double[]... ships) {
        Map<UUID, double[]> seen = new LinkedHashMap<>();
        for (double[] s : ships) {
            seen.put(UUID.randomUUID(), s);
        }
        return seen;
    }

    @Test
    void oneRealShipIsGivenToOneShipOnly() {
        double[] real = {10, 0, 0, 0, 0};
        // Both ships lie within reach of the one real ship: the nearer has it, the other is drawn as a stand-in.
        double[][] owns = DistantShips.match(List.of(want(0), want(8)), seen(real));
        assertNull(owns[0]);
        assertSame(real, owns[1]);
    }

    @Test
    void eachShipKeepsItsOwnRealShip() {
        double[] a = {1, 0, 0, 0, 0};
        double[] b = {11, 0, 0, 0, 0};
        // Ship 0 is within reach of both; the old first-found rule gave it b and left ship 1 without a hull.
        double[][] owns = DistantShips.match(List.of(want(0), want(10)), seen(b, a));
        assertSame(a, owns[0]);
        assertSame(b, owns[1]);
    }

    @Test
    void aNamedShipHasItsOwnHullHoweverFarItsFormationLags() {
        // Two ships in line ahead, sailing +x: each formation lags 6 blocks, so the leader's sits on the follower's hull.
        double[] lead = {20, 0, 0, 0, 0};
        double[] follow = {14, 0, 0, 0, 0};
        UUID leadId = UUID.randomUUID();
        UUID followId = UUID.randomUUID();
        Map<UUID, double[]> seen = new LinkedHashMap<>();
        seen.put(followId, follow);
        seen.put(leadId, lead);
        double[][] owns = DistantShips.match(List.of(want(14, leadId), want(8, followId), want(15)), seen);
        assertSame(lead, owns[0]);
        assertSame(follow, owns[1]);
        assertNull(owns[2], "a hull taken by its own ship's id isn't given to another by distance");
    }

    @Test
    void aNamedShipTheClientLacksIsAStandIn() {
        // Its Sable ship isn't here yet: it must not take the nearby hull of another.
        double[][] owns = DistantShips.match(List.of(want(0, UUID.randomUUID())), seen(new double[]{1, 1, 0, 0, 0}));
        assertNull(owns[0]);
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
