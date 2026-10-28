package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.General;
import rafasconquest.battle.BattlePlan.Order;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.BattlePlan.Slot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattlePlanTest {
    private static final int FORMED = BattlePlan.FORMING_SECONDS;

    @Test
    void armiesComeInWholeBlocksOfTwentyWithinTheLimits() {
        assertEquals(5, BattlePlan.blocks(100));
        assertEquals(100, BattlePlan.blocks(2000));
        assertEquals(250, BattlePlan.blocks(5000));
        assertEquals(500, BattlePlan.blocks(10_000));
        assertEquals(500, BattlePlan.blocks(50_000));
        assertEquals(1, BattlePlan.blocks(1));
        assertEquals(8, BattlePlan.blocks(150), "rounded to the nearest block");
    }

    @Test
    void anArmyIsAFifthArchersAndAFifthCavalry() {
        for (General general : General.values()) {
            for (int blocks : new int[]{5, 50, 100}) {
                List<Slot> slots = BattlePlan.deployment(general, blocks);
                assertEquals(blocks, slots.size());
                assertEquals(blocks / 5, slots.stream().filter(s -> s.arm() == Arm.ARCHERS).count());
                assertEquals(blocks / 5, slots.stream().filter(s -> s.arm() == Arm.CAVALRY).count());
            }
        }
        List<Slot> lone = BattlePlan.deployment(General.WARRIOR, 1);
        assertEquals(1, lone.size());
        assertTrue(lone.get(0).arm().infantry(), "a single block is infantry");
    }

    @Test
    void theHundredStrongArmiesKeepTheirOldShape() {
        List<Slot> warrior = BattlePlan.deployment(General.WARRIOR, 5);
        assertEquals(3, warrior.stream().filter(s -> s.arm().infantry() && s.line() == 0).count());
        assertEquals(0, warrior.stream().filter(Slot::reserve).count());
        List<Slot> tactician = BattlePlan.deployment(General.TACTICIAN, 5);
        assertEquals(2, tactician.stream().filter(s -> s.arm().infantry() && s.line() == 0).count());
        assertEquals(1, tactician.stream().filter(Slot::reserve).count());
    }

    @Test
    void aBigArmyStandsInLinesBehindAFrontOfAtMostTenBlocks() {
        List<Slot> slots = BattlePlan.deployment(General.TACTICIAN, 100);
        List<Slot> infantry = slots.stream().filter(s -> s.arm().infantry() && !s.reserve()).toList();
        assertEquals(BattlePlan.MAX_FRONT, infantry.stream().filter(s -> s.line() == 0).count());
        int lines = infantry.stream().mapToInt(Slot::line).max().orElseThrow() + 1;
        assertTrue(lines >= 4, "relief lines behind the front: " + lines);
        assertEquals(12, slots.stream().filter(Slot::reserve).count(), "a fifth of 60 infantry blocks");
        double reserveBack = slots.stream().filter(Slot::reserve).mapToDouble(Slot::back).min().orElseThrow();
        assertTrue(reserveBack > infantry.stream().mapToDouble(Slot::back).max().orElseThrow(), "the reserve is behind every line");
        assertTrue(infantry.stream().anyMatch(s -> s.arm() == Arm.SHIELDWALL));
    }

    @Test
    void aHugeArmyWidensItsFrontInsteadOfStandingInEndlessLines() {
        for (General general : General.values()) {
            List<Slot> slots = BattlePlan.deployment(general, 500);
            assertEquals(500, slots.size());
            long front = slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
            int lines = slots.stream().filter(s -> s.arm().infantry() && !s.reserve()).mapToInt(Slot::line).max().orElseThrow() + 1;
            assertTrue(front > BattlePlan.MAX_FRONT && front <= BattlePlan.MAX_WIDE_FRONT, general + " front " + front);
            assertTrue(lines <= BattlePlan.LINES_DEEP + 2, general + " stands " + lines + " lines deep");
            double deepestRider = slots.stream().filter(s -> s.arm() == Arm.CAVALRY).mapToDouble(Slot::back).max().orElseThrow();
            assertTrue(deepestRider <= BattlePlan.CAVALRY_BACK + BattlePlan.CAVALRY_ROWS * BattlePlan.CAVALRY_ROW, "a wing " + deepestRider + " deep");
        }
        assertEquals(BattlePlan.MAX_FRONT, BattlePlan.deployment(General.WARRIOR, 100).stream()
                .filter(s -> s.arm().infantry() && s.line() == 0).count(), "2000 soldiers still stand 10 blocks wide");
    }

    @Test
    void blocksInALineDoNotOverlap() {
        double width = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING;
        for (General general : General.values()) {
            List<Slot> slots = BattlePlan.deployment(general, 100);
            for (int line = 0; line < 6; line++) {
                int l = line;
                List<Double> rights = slots.stream().filter(s -> s.arm().infantry() && !s.reserve() && s.line() == l)
                        .map(Slot::right).sorted().toList();
                for (int i = 1; i < rights.size(); i++) {
                    assertTrue(rights.get(i) - rights.get(i - 1) > width + 1, general + " line " + l + " overlaps");
                }
            }
        }
    }

    @Test
    void cavalryStandsOnBothWingsBeyondTheLine() {
        List<Slot> slots = BattlePlan.deployment(General.WARRIOR, 100);
        double lineEnd = slots.stream().filter(s -> s.line() == 0).mapToDouble(s -> Math.abs(s.right())).max().orElseThrow();
        List<Slot> cavalry = slots.stream().filter(s -> s.arm() == Arm.CAVALRY).toList();
        assertTrue(cavalry.stream().allMatch(s -> Math.abs(s.right()) > lineEnd + BattlePlan.BLOCK_PITCH / 2));
        assertEquals(10, cavalry.stream().filter(s -> s.right() > 0).count());
        assertEquals(10, cavalry.stream().filter(s -> s.right() < 0).count());
    }

    @Test
    void aTacticianStaysBehindEverythingAndAWarriorJustBehindTheLine() {
        assertTrue(BattlePlan.generalBack(General.WARRIOR, BattlePlan.deployment(General.WARRIOR, 100)) < 10);
        List<Slot> slots = BattlePlan.deployment(General.TACTICIAN, 100);
        double behindEveryone = slots.stream().mapToDouble(Slot::back).max().orElseThrow();
        assertTrue(BattlePlan.generalBack(General.TACTICIAN, slots) >= behindEveryone + 3);
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
    void aWarriorHoldsWhileFormingThenAdvancesAndFightsInLine() {
        General w = General.WARRIOR;
        assertEquals(Order.HOLD, BattlePlan.order(w, Arm.SWORDSMEN, 0, 5, false));
        assertEquals(Order.MARCH, BattlePlan.order(w, Arm.SWORDSMEN, FORMED, 50, false));
        assertEquals(Order.ENGAGE, BattlePlan.order(w, Arm.SHIELDWALL, FORMED, BattlePlan.INFANTRY_ENGAGE, false));
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
        assertEquals(Order.ENGAGE, BattlePlan.order(t, Arm.SHIELDWALL, FORMED, BattlePlan.INFANTRY_ENGAGE, false));
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
        // Facing east toward an enemy line 100 wide at the origin: our right is south (+z), behind them is east.
        List<Point> right = BattlePlan.flankRoute(new Point(0, 0), 1, 0, 1, 50);
        assertEquals(2, right.size());
        assertTrue(right.get(0).z() > 70, "first wide past the end of the enemy line");
        assertTrue(right.get(1).x() > 20, "then round behind it");
        List<Point> left = BattlePlan.flankRoute(new Point(0, 0), 1, 0, -1, 50);
        assertTrue(left.get(0).z() < -70, "the left wing rides round the other end");
    }

    @Test
    void aBlockIsRelievedAtHalfStrengthByAFitOne() {
        assertFalse(BattlePlan.needsRelief(11, 20));
        assertTrue(BattlePlan.needsRelief(10, 20));
        assertTrue(BattlePlan.needsRelief(0, 20));
        assertTrue(BattlePlan.fitToRelieve(20, 20));
        assertTrue(BattlePlan.fitToRelieve(12, 20));
        assertFalse(BattlePlan.fitToRelieve(11, 20), "a worn block stays at the rear");
    }

    @Test
    void theReserveGoesInWhenTheLinesRunOutOrTheArmyIsWorn() {
        assertFalse(BattlePlan.commitReserve(1000, 1000, true, true));
        assertTrue(BattlePlan.commitReserve(1000, 1000, true, false), "no fit block left to relieve with");
        assertFalse(BattlePlan.commitReserve(1000, 1000, false, false));
        assertTrue(BattlePlan.commitReserve(600, 1000, false, true), "the army is down to 60 %");
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
