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
        assertEquals(2500, BattlePlan.blocks(50_000));
        assertEquals(5000, BattlePlan.blocks(500_000));
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
    void anArmyOfAThousandStandsEachWingInLinesBehindAFrontOfAtMostTenBlocks() {
        List<Slot> slots = BattlePlan.deployment(50);
        List<Slot> infantry = slots.stream().filter(s -> s.arm().infantry()).toList();
        for (int wing = 0; wing < 3; wing++) {
            int w = wing;
            long front = infantry.stream().filter(s -> s.line() == 0 && s.wing() == w).count();
            assertTrue(front > 0 && front <= BattlePlan.MAX_FRONT, "wing " + w + " front " + front);
        }
        int lines = infantry.stream().mapToInt(Slot::line).max().orElseThrow() + 1;
        assertTrue(lines >= 2, "relief lines behind the front: " + lines);
        assertTrue(infantry.stream().anyMatch(s -> s.arm() == Arm.SHIELDWALL));
    }

    @Test
    void everyWingStandsInfantryInFrontArchersBehindCavalryInTheRear() {
        for (boolean shield : new boolean[]{false, true}) {
            for (int blocks : new int[]{15, 50, 100, 500}) {
                List<Slot> slots = BattlePlan.deployment(blocks, shield);
                for (int wing = 0; wing < 3; wing++) {
                    int w = wing;
                    List<Slot> mine = slots.stream().filter(s -> s.wing() == w).toList();
                    double front = mine.stream().filter(s -> s.arm().infantry() && s.line() == 0).mapToDouble(Slot::back).max().orElse(0);
                    double archers = mine.stream().filter(s -> s.arm() == Arm.ARCHERS).mapToDouble(Slot::back).min().orElse(Double.MAX_VALUE);
                    double lastInfantry = slots.stream().filter(s -> s.arm().infantry()).mapToDouble(Slot::back).max().orElseThrow();
                    double lastArchers = slots.stream().filter(s -> s.arm() == Arm.ARCHERS).mapToDouble(Slot::back).max().orElse(0);
                    double cavalry = mine.stream().filter(s -> s.arm() == Arm.CAVALRY).mapToDouble(Slot::back).min().orElse(Double.MAX_VALUE);
                    String what = blocks + " blocks, shield " + shield + ", wing " + wing;
                    assertTrue(archers > front, what + ": archers behind the front");
                    assertTrue(cavalry > Math.max(lastInfantry, lastArchers), what + ": cavalry in the rear");
                }
                if (blocks * BattlePlan.SOLDIERS_PER_FORMATION > BattlePlan.WINGS_OVER) {
                    for (int wing = 0; wing < 3; wing++) {
                        int w = wing;
                        assertTrue(slots.stream().anyMatch(s -> s.wing() == w && s.arm() == Arm.CAVALRY), "cavalry in every wing's reserves");
                    }
                }
            }
        }
    }

    @Test
    void theFrontCoversTheArchersAndTheReservesStandWellBehind() {
        for (boolean shield : new boolean[]{false, true}) {
            for (int blocks : new int[]{15, 61, 170, 500}) {
                List<Slot> slots = BattlePlan.deployment(blocks, shield);
                String what = blocks + " blocks, shield " + shield;
                double frontLo = slots.stream().filter(s -> s.line() == 0).mapToDouble(s -> s.right() - (s.files() - 1) * BattlePlan.SPACING / 2).min().orElseThrow();
                double frontHi = slots.stream().filter(s -> s.line() == 0).mapToDouble(s -> s.right() + (s.files() - 1) * BattlePlan.SPACING / 2).max().orElseThrow();
                for (Slot a : slots.stream().filter(s -> s.arm() == Arm.ARCHERS).toList()) {
                    double half = (a.files() - 1) * BattlePlan.SPACING / 2;
                    assertTrue(a.right() - half >= frontLo - 3 && a.right() + half <= frontHi + 3, what + ": archers covered by the front");
                }
                double archersRear = slots.stream().filter(s -> s.arm() == Arm.ARCHERS).mapToDouble(Slot::back).max().orElse(0);
                int frontRows = BattlePlan.stackFiles(blocks, shield) > 0 ? BattlePlan.SHIELD_STACK : 1;
                slots.stream().filter(s -> s.arm().infantry() && s.line() >= frontRows).forEach(r ->
                        assertTrue(r.back() >= archersRear + BattlePlan.RESERVE_GAP - 1e-9, what + ": reserves well behind"));
            }
        }
    }

    @Test
    void theWingsStandApartWithAGapBetweenThem() {
        for (boolean shield : new boolean[]{false, true}) {
            for (int blocks : new int[]{61, 100, 500}) {
                List<Slot> slots = BattlePlan.deployment(blocks, shield);
                double[] lo = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
                double[] hi = {-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE};
                for (Slot s : slots) {
                    if (s.line() != 0) {
                        continue; // the front line's wings
                    }
                    double half = (s.files() - 1) * BattlePlan.SPACING / 2;
                    lo[s.wing()] = Math.min(lo[s.wing()], s.right() - half);
                    hi[s.wing()] = Math.max(hi[s.wing()], s.right() + half);
                }
                String what = blocks + " blocks, shield " + shield;
                assertTrue(lo[BattlePlan.CENTRE] - hi[BattlePlan.LEFT] >= BattlePlan.WING_GAP, what + ": left to centre " + (lo[1] - hi[0]));
                assertTrue(lo[BattlePlan.RIGHT] - hi[BattlePlan.CENTRE] >= BattlePlan.WING_GAP, what + ": centre to right " + (lo[2] - hi[1]));
            }
        }
    }

    @Test
    void theWingArmiesStandFurtherApartTheBiggerTheBattle() {
        assertEquals(BattlePlan.WING_GAP, BattlePlan.wingGap(9_000), 1e-9);
        assertEquals(35, BattlePlan.wingGap(10_000), 1e-9);
        assertEquals(267.5, BattlePlan.wingGap(55_000), 1e-9);
        assertEquals(500, BattlePlan.wingGap(100_000), 1e-9);
        List<Slot> slots = BattlePlan.deployment(BattlePlan.blocks(100_000), false);
        double leftEdge = slots.stream().filter(s -> s.wing() == BattlePlan.LEFT).mapToDouble(s -> s.right() + (s.files() - 1) * BattlePlan.SPACING / 2).max().orElseThrow();
        double centreEdge = slots.stream().filter(s -> s.wing() == BattlePlan.CENTRE).mapToDouble(s -> s.right() - (s.files() - 1) * BattlePlan.SPACING / 2).min().orElseThrow();
        assertTrue(centreEdge - leftEdge >= 500, "gap " + (centreEdge - leftEdge));
    }

    @Test
    void aStackedWingTakesItsShareOfEveryArm() {
        for (Doctrine.Distribution d : Doctrine.Distribution.values()) {
            double[] shares = WarPlan.of(List.of(), d, 0.4).wingShares();
            List<Slot> slots = BattlePlan.deployment(500, false, shares);
            for (Arm arm : List.of(Arm.SWORDSMEN, Arm.ARCHERS, Arm.CAVALRY)) {
                java.util.function.Predicate<Slot> of = s -> arm == Arm.SWORDSMEN ? s.arm().infantry() : s.arm() == arm;
                long all = slots.stream().filter(of).count();
                for (int wing = 0; wing < 3; wing++) {
                    int w = wing;
                    long mine = slots.stream().filter(s -> of.test(s) && s.wing() == w).count();
                    assertEquals(all * shares[wing], mine, 1, d + " " + arm + " wing " + wing);
                }
            }
        }
    }

    @Test
    void eachWingStandsAsARectangleNoWiderThanItsFront() {
        for (boolean shield : new boolean[]{false, true}) {
            for (int blocks : new int[]{15, 61, 170, 500, 2500, 5000}) {
                List<Slot> slots = BattlePlan.deployment(blocks, shield);
                for (int wing = 0; wing < 3; wing++) {
                    int w = wing;
                    List<Slot> mine = slots.stream().filter(s -> s.wing() == w).toList();
                    if (mine.stream().noneMatch(s -> s.line() == 0)) {
                        continue;
                    }
                    double lo = mine.stream().filter(s -> s.line() == 0).mapToDouble(s -> s.right() - (s.files() - 1) * BattlePlan.SPACING / 2).min().orElseThrow();
                    double hi = mine.stream().filter(s -> s.line() == 0).mapToDouble(s -> s.right() + (s.files() - 1) * BattlePlan.SPACING / 2).max().orElseThrow();
                    for (Slot s : mine) {
                        double half = (s.files() - 1) * BattlePlan.SPACING / 2;
                        assertTrue(s.right() - half >= lo - 3 && s.right() + half <= hi + 3,
                                blocks + " blocks, shield " + shield + ", wing " + w + ": " + s + " sticks out past the front");
                    }
                }
            }
        }
    }

    @Test
    void noTwoBlocksOverlapWhenAnArmyDeploys() {
        for (boolean shield : new boolean[]{false, true}) {
            for (int blocks : new int[]{5, 15, 30, 50, 100, 300, 500}) {
                {
                    List<Slot> slots = BattlePlan.deployment(blocks, shield);
                    for (int i = 0; i < slots.size(); i++) {
                        for (int j = i + 1; j < slots.size(); j++) {
                            assertFalse(overlap(slots.get(i), slots.get(j)), String.format(
                                    "%d blocks, shield %s: %s overlaps %s", blocks, shield, slots.get(i), slots.get(j)));
                        }
                    }
                }
            }
        }
    }

    /** Whether two blocks' footprints overlap: each as wide as its ranks, as deep as its ranks go back from its slot. */
    private static boolean overlap(Slot a, Slot b) {
        double aw = (a.files() - 1) * BattlePlan.SPACING / 2;
        double bw = (b.files() - 1) * BattlePlan.SPACING / 2;
        double ad = (Math.ceil((double) BattlePlan.SOLDIERS_PER_FORMATION / a.files()) - 1) * BattlePlan.SPACING;
        double bd = (Math.ceil((double) BattlePlan.SOLDIERS_PER_FORMATION / b.files()) - 1) * BattlePlan.SPACING;
        boolean across = Math.abs(a.right() - b.right()) < aw + bw + 0.5;
        boolean deep = a.back() < b.back() + bd + 0.5 && b.back() < a.back() + ad + 0.5;
        return across && deep;
    }

    @Test
    void anArmyOverAThousandStandsItsFrontFilesThreeBlocksDeep() {
        for (int blocks : new int[]{51, 100, 150, 500}) {
            List<Slot> infantry = BattlePlan.deployment(blocks).stream().filter(s -> s.arm().infantry()).toList();
            long files = infantry.stream().filter(s -> s.line() == 0).count();
            for (int row = 1; row < BattlePlan.SHIELD_STACK; row++) {
                int r = row;
                assertEquals(files, infantry.stream().filter(s -> s.line() == r).count(), blocks + " blocks: every front file " + (r + 1) + " deep");
                assertEquals(r * BattlePlan.stackPitch(BattlePlan.FILE_WIDTH), infantry.stream().filter(s -> s.line() == r).findFirst().orElseThrow().back(), 1e-9);
            }
            assertTrue(files <= BattlePlan.MAX_WIDE_FRONT);
        }
    }

    @Test
    void aHugeArmyWidensItsFrontInsteadOfStandingInEndlessLines() {
        List<Slot> slots = BattlePlan.deployment(500);
        assertEquals(500, slots.size());
        long front = slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        assertTrue(front > BattlePlan.MAX_FRONT && front <= BattlePlan.MAX_WIDE_FRONT, "front " + front);
        double deepestRider = slots.stream().filter(s -> s.arm() == Arm.CAVALRY).mapToDouble(Slot::back).max().orElseThrow();
        double lastLine = slots.stream().filter(s -> s.arm().infantry()).mapToDouble(Slot::back).max().orElseThrow();
        assertTrue(deepestRider <= lastLine + BattlePlan.LINE_GAP + 5 * BattlePlan.CAVALRY_ROW, "the reserves " + deepestRider + " deep");
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
    void theGeneralStandsBehindTheLineOutOfTheEnemysReach() {
        // With the lines in contact, an enemy block holding its place can't reach past the front to the general.
        assertTrue(BattlePlan.GENERAL_BACK > FieldRules.HOLDING_REACH + BattlePlan.FRONT_CONTACT);
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
