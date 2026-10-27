package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.General;
import rafasconquest.battle.BattlePlan.Order;
import rafasconquest.battle.BattlePlan.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattlePlanTest {
    private static final int FORMED = BattlePlan.FORMING_SECONDS;

    @Test
    void anArmyIsFiveFormationsOfTwentyWithAFifthArchersAndAFifthCavalry() {
        for (General general : General.values()) {
            List<BattlePlan.Slot> slots = BattlePlan.deployment(general);
            assertEquals(5, slots.size());
            assertEquals(1, slots.stream().filter(s -> s.arm() == Arm.ARCHERS).count());
            assertEquals(1, slots.stream().filter(s -> s.arm() == Arm.CAVALRY).count());
            assertEquals(3, slots.stream().filter(s -> s.arm().infantry()).count());
        }
        assertEquals(0, BattlePlan.WARRIOR_DEPLOYMENT.stream().filter(BattlePlan.Slot::reserve).count());
        assertEquals(1, BattlePlan.TACTICIAN_DEPLOYMENT.stream().filter(s -> s.reserve() && s.arm() == Arm.SWORDSMEN).count());
    }

    @Test
    void frontLineFormationsDoNotOverlap() {
        double width = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING;
        for (General general : General.values()) {
            var front = BattlePlan.deployment(general).stream().filter(s -> s.back() == 0).map(BattlePlan.Slot::right).sorted().toList();
            for (int i = 1; i < front.size(); i++) {
                assertTrue(front.get(i) - front.get(i - 1) > width + 1, general + ": formations " + (i - 1) + " and " + i + " overlap");
            }
        }
    }

    @Test
    void aTacticianStaysWellBackAndAWarriorJustBehindTheLine() {
        assertTrue(BattlePlan.generalBack(General.WARRIOR) < 10);
        double behindEveryone = BattlePlan.TACTICIAN_DEPLOYMENT.stream().mapToDouble(BattlePlan.Slot::back).max().orElseThrow();
        assertTrue(BattlePlan.generalBack(General.TACTICIAN) > behindEveryone + 3);
    }

    @Test
    void slotsLandOnTheArmysRightAndBehindItsFront() {
        // Facing south (+z): the army's right is west (-x), behind is north (-z).
        Point p = BattlePlan.place(new Point(100, 100), 0, 1, 10, 5);
        assertEquals(90, p.x(), 1e-9);
        assertEquals(95, p.z(), 1e-9);
        // Facing east (+x): right is south (+z), behind is west (-x).
        Point q = BattlePlan.place(new Point(0, 0), 1, 0, 10, 5);
        assertEquals(-5, q.x(), 1e-9);
        assertEquals(10, q.z(), 1e-9);
    }

    @Test
    void theArmiesStartFacingEachOtherAcrossTheCentre() {
        Point[] fronts = BattlePlan.fronts(new Point(0, 0), 1, 0);
        assertEquals(-BattlePlan.DEPLOY_DISTANCE, fronts[0].x(), 1e-9);
        assertEquals(BattlePlan.DEPLOY_DISTANCE, fronts[1].x(), 1e-9);
    }

    @Test
    void aWarriorHoldsWhileFormingThenAdvancesAndCharges() {
        General w = General.WARRIOR;
        assertEquals(Order.HOLD, BattlePlan.order(w, Arm.SWORDSMEN, 0, 5, false));
        assertEquals(Order.MARCH, BattlePlan.order(w, Arm.SWORDSMEN, FORMED, 50, false));
        assertEquals(Order.CHARGE, BattlePlan.order(w, Arm.SHIELDWALL, FORMED, BattlePlan.INFANTRY_ENGAGE, false));
        assertEquals(Order.MARCH, BattlePlan.order(w, Arm.ARCHERS, 10, 50, false));
        assertEquals(Order.HOLD, BattlePlan.order(w, Arm.ARCHERS, 10, BattlePlan.ARCHER_RANGE, true));
        assertEquals(Order.HOLD, BattlePlan.order(w, Arm.CAVALRY, 10, 50, false));
        assertEquals(Order.CHARGE, BattlePlan.order(w, Arm.CAVALRY, 10, 50, true));
        assertEquals(Order.CHARGE, BattlePlan.order(w, Arm.CAVALRY, 10, BattlePlan.CAVALRY_THREAT, false));
    }

    @Test
    void aTacticiansInfantryWaitsWhileTheArchersShoot() {
        General t = General.TACTICIAN;
        assertEquals(Order.HOLD, BattlePlan.order(t, Arm.SWORDSMEN, FORMED, 50, false));
        assertEquals(Order.MARCH, BattlePlan.order(t, Arm.ARCHERS, FORMED, 50, false));
        assertEquals(Order.MARCH, BattlePlan.order(t, Arm.SWORDSMEN, FORMED + BattlePlan.PATIENCE_SECONDS, 50, false));
        assertEquals(Order.MARCH, BattlePlan.order(t, Arm.SWORDSMEN, FORMED, BattlePlan.PATIENCE_RANGE, false), "an enemy close by ends the wait");
        assertEquals(Order.CHARGE, BattlePlan.order(t, Arm.SHIELDWALL, FORMED, BattlePlan.INFANTRY_ENGAGE, false));
    }

    @Test
    void aTacticiansCavalryFlanksOnceTheLinesLock() {
        General t = General.TACTICIAN;
        assertEquals(Order.HOLD, BattlePlan.order(t, Arm.CAVALRY, 10, 50, false));
        assertEquals(Order.FLANK, BattlePlan.order(t, Arm.CAVALRY, 10, 50, true));
        assertEquals(Order.CHARGE, BattlePlan.order(t, Arm.CAVALRY, 10, BattlePlan.CAVALRY_THREAT, false));
    }

    @Test
    void theFlankRouteGoesPastTheEnemysEndThenBehindIt() {
        // Facing east toward an enemy at the origin: our right is south (+z), behind the enemy is further east.
        List<Point> route = BattlePlan.flankRoute(new Point(0, 0), 1, 0);
        assertEquals(2, route.size());
        assertTrue(route.get(0).z() > 30, "first wide past the end of the enemy line");
        assertTrue(route.get(1).x() > 20, "then round behind it");
    }

    @Test
    void theReserveGoesToTheWeakestFightingFormation() {
        int[] started = {20, 20};
        assertEquals(-1, BattlePlan.reserveToCommit(new int[]{20, 20}, started, new boolean[]{true, true}));
        assertEquals(-1, BattlePlan.reserveToCommit(new int[]{5, 20}, started, new boolean[]{false, true}), "only once it is fighting");
        assertEquals(1, BattlePlan.reserveToCommit(new int[]{11, 8}, started, new boolean[]{true, true}));
        assertEquals(0, BattlePlan.reserveToCommit(new int[]{12, 20}, started, new boolean[]{true, true}));
    }

    @Test
    void anArmyRoutsAtAFifthOfItsStrength() {
        assertFalse(BattlePlan.routs(21, 100));
        assertTrue(BattlePlan.routs(20, 100));
        assertTrue(BattlePlan.routs(0, 100));
    }

    @Test
    void factionsSharingAColourGetDifferentUniforms() {
        int red = 0xB02E26;
        int blue = 0x3C44AA;
        assertArrayEquals(new int[]{red, blue}, BattlePlan.uniformColors(red, blue));
        int[] same = BattlePlan.uniformColors(red, red);
        assertEquals(red, same[0]);
        assertNotEquals(red, same[1]);
        int black = 0x1D1D21;
        int[] dark = BattlePlan.uniformColors(black, black);
        assertTrue((dark[1] & 255) > (black & 255), "too dark to darken, so it lightens");
    }
}
