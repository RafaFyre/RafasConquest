package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
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
        for (int blocks : new int[]{5, 50, 100}) {
            List<Slot> slots = BattlePlan.deployment(blocks);
            assertEquals(blocks, slots.size());
            assertEquals(blocks / 5, slots.stream().filter(s -> s.arm() == Arm.ARCHERS).count());
            assertEquals(blocks / 5, slots.stream().filter(s -> s.arm() == Arm.CAVALRY).count());
        }
        List<Slot> lone = BattlePlan.deployment(1);
        assertEquals(1, lone.size());
        assertTrue(lone.get(0).arm().infantry(), "a single block is infantry");
    }

    @Test
    void aHundredStrongArmyStandsThreeBlocksWide() {
        List<Slot> slots = BattlePlan.deployment(5);
        assertEquals(3, slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count());
    }

    @Test
    void aBigArmyStandsInLinesBehindAFrontOfAtMostTenBlocks() {
        List<Slot> slots = BattlePlan.deployment(100);
        List<Slot> infantry = slots.stream().filter(s -> s.arm().infantry()).toList();
        assertEquals(BattlePlan.MAX_FRONT, infantry.stream().filter(s -> s.line() == 0).count());
        int lines = infantry.stream().mapToInt(Slot::line).max().orElseThrow() + 1;
        assertTrue(lines >= 4, "relief lines behind the front: " + lines);
        assertTrue(infantry.stream().anyMatch(s -> s.arm() == Arm.SHIELDWALL));
    }

    @Test
    void aHugeArmyWidensItsFrontInsteadOfStandingInEndlessLines() {
        List<Slot> slots = BattlePlan.deployment(500);
        assertEquals(500, slots.size());
        long front = slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        int lines = slots.stream().filter(s -> s.arm().infantry()).mapToInt(Slot::line).max().orElseThrow() + 1;
        assertTrue(front > BattlePlan.MAX_FRONT && front <= BattlePlan.MAX_WIDE_FRONT, "front " + front);
        assertTrue(lines <= BattlePlan.LINES_DEEP + 2, "stands " + lines + " lines deep");
        double deepestRider = slots.stream().filter(s -> s.arm() == Arm.CAVALRY).mapToDouble(Slot::back).max().orElseThrow();
        assertTrue(deepestRider <= BattlePlan.CAVALRY_BACK + BattlePlan.CAVALRY_ROWS * BattlePlan.CAVALRY_ROW, "a wing " + deepestRider + " deep");
    }

    @Test
    void blocksInALineDoNotOverlap() {
        double width = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING;
        for (int blocks : new int[]{100, 500}) {
            List<Slot> slots = BattlePlan.deployment(blocks);
            for (int line = 0; line < 10; line++) {
                int l = line;
                List<Double> rights = slots.stream().filter(s -> s.arm().infantry() && s.line() == l)
                        .map(Slot::right).sorted().toList();
                for (int i = 1; i < rights.size(); i++) {
                    assertTrue(rights.get(i) - rights.get(i - 1) > width + 1, blocks + " blocks: line " + l + " overlaps");
                }
            }
        }
    }

    @Test
    void cavalryStandsOnBothWingsBeyondTheLine() {
        List<Slot> slots = BattlePlan.deployment(100);
        double lineEnd = slots.stream().filter(s -> s.line() == 0).mapToDouble(s -> Math.abs(s.right())).max().orElseThrow();
        List<Slot> cavalry = slots.stream().filter(s -> s.arm() == Arm.CAVALRY).toList();
        assertTrue(cavalry.stream().allMatch(s -> Math.abs(s.right()) > lineEnd + BattlePlan.BLOCK_PITCH / 2));
        assertEquals(10, cavalry.stream().filter(s -> s.right() > 0).count());
        assertEquals(10, cavalry.stream().filter(s -> s.right() < 0).count());
    }

    @Test
    void theGeneralStandsJustBehindTheLine() {
        double nearestLineBehind = BattlePlan.LINE_GAP;
        assertTrue(BattlePlan.GENERAL_BACK > 0 && BattlePlan.GENERAL_BACK < nearestLineBehind);
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
    void theInfantryHoldsWhileFormingThenAdvancesAndEngages() {
        assertEquals(Order.HOLD, BattlePlan.order(Arm.SWORDSMEN, 0, 5, false));
        assertEquals(Order.MARCH, BattlePlan.order(Arm.SWORDSMEN, FORMED, 50, false));
        assertEquals(Order.ENGAGE, BattlePlan.order(Arm.SHIELDWALL, FORMED, BattlePlan.INFANTRY_ENGAGE, false));
        assertTrue(BattlePlan.FRONT_CONTACT < BattlePlan.INFANTRY_ENGAGE, "engaged lines close in until they meet");
    }

    @Test
    void archersHaltInRangeAndCavalryWaitsForTheLinesToMeet() {
        assertEquals(Order.MARCH, BattlePlan.order(Arm.ARCHERS, 10, 50, false));
        assertEquals(Order.HOLD, BattlePlan.order(Arm.ARCHERS, 10, BattlePlan.ARCHER_RANGE, true));
        assertEquals(Order.HOLD, BattlePlan.order(Arm.CAVALRY, 10, 50, false));
        assertEquals(Order.CHARGE, BattlePlan.order(Arm.CAVALRY, 10, 50, true));
        assertEquals(Order.CHARGE, BattlePlan.order(Arm.CAVALRY, 10, BattlePlan.CAVALRY_THREAT, false));
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
