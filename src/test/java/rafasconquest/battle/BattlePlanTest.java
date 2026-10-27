package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Order;
import rafasconquest.battle.BattlePlan.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattlePlanTest {
    @Test
    void anArmyIsFiveFormationsOfTwentyWithAFifthArchersAndAFifthCavalry() {
        assertEquals(5, BattlePlan.DEPLOYMENT.size());
        assertEquals(1, BattlePlan.DEPLOYMENT.stream().filter(s -> s.arm() == Arm.ARCHERS).count());
        assertEquals(1, BattlePlan.DEPLOYMENT.stream().filter(s -> s.arm() == Arm.CAVALRY).count());
        assertEquals(3, BattlePlan.DEPLOYMENT.stream().filter(s -> s.arm().infantry()).count());
        assertEquals(100, BattlePlan.DEPLOYMENT.size() * BattlePlan.SOLDIERS_PER_FORMATION);
    }

    @Test
    void frontLineFormationsDoNotOverlap() {
        double width = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING;
        var front = BattlePlan.DEPLOYMENT.stream().filter(s -> s.back() == 0).map(BattlePlan.Slot::right).sorted().toList();
        for (int i = 1; i < front.size(); i++) {
            assertTrue(front.get(i) - front.get(i - 1) > width + 1, "formations " + (i - 1) + " and " + i + " overlap");
        }
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
    void theCommanderHoldsWhileFormingThenAdvancesAndCharges() {
        assertEquals(Order.HOLD, BattlePlan.order(Arm.SWORDSMEN, 0, 5, false));
        assertEquals(Order.MARCH, BattlePlan.order(Arm.SWORDSMEN, BattlePlan.FORMING_SECONDS, 50, false));
        assertEquals(Order.CHARGE, BattlePlan.order(Arm.SHIELDWALL, BattlePlan.FORMING_SECONDS, BattlePlan.INFANTRY_ENGAGE, false));
        assertEquals(Order.MARCH, BattlePlan.order(Arm.ARCHERS, 10, 50, false));
        assertEquals(Order.HOLD, BattlePlan.order(Arm.ARCHERS, 10, BattlePlan.ARCHER_RANGE, true));
        assertEquals(Order.HOLD, BattlePlan.order(Arm.CAVALRY, 10, 50, false));
        assertEquals(Order.CHARGE, BattlePlan.order(Arm.CAVALRY, 10, 50, true));
        assertEquals(Order.CHARGE, BattlePlan.order(Arm.CAVALRY, 10, BattlePlan.CAVALRY_THREAT, false));
    }

    @Test
    void anArmyRoutsAtAFifthOfItsStrength() {
        assertFalse(BattlePlan.routs(21, 100));
        assertTrue(BattlePlan.routs(20, 100));
        assertTrue(BattlePlan.routs(0, 100));
    }
}
