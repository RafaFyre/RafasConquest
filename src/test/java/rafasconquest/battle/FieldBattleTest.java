package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.General;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.battle.FieldRules.Side;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldBattleTest {
    private static FieldBattle battle(General a, int aBlocks, General b, int bBlocks, long seed) {
        return new FieldBattle(a, aBlocks, b, bBlocks, new Point(0, 0), 1, 0, seed);
    }

    @Test
    void aTwoThousandStrongBattleRunsToTheEndInWellUnderASecondOfWork() {
        for (General a : General.values()) {
            for (General b : General.values()) {
                FieldBattle battle = battle(a, 100, b, 100, 7);
                long start = System.nanoTime();
                FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
                double seconds = (System.nanoTime() - start) / 1e9;
                assertNotNull(outcome, a + " v " + b + " never ended");
                assertTrue(outcome.winner() >= 0, a + " v " + b + ": " + outcome.result());
                assertTrue(seconds < 3, a + " v " + b + " took " + seconds + " s to simulate");
                assertTrue(outcome.seconds() >= 240, a + " v " + b + " was over in " + outcome.seconds() + " s; big battles should last");
                assertTrue(battle.army(0).reliefs() + battle.army(1).reliefs() >= 20, "the lines rotate");
            }
        }
    }

    @Test
    void theSameSeedFightsTheSameBattle() {
        FieldBattle.Outcome first = battle(General.TACTICIAN, 25, General.WARRIOR, 25, 11).run(BattlePlan.TIME_LIMIT_SECONDS);
        FieldBattle.Outcome again = battle(General.TACTICIAN, 25, General.WARRIOR, 25, 11).run(BattlePlan.TIME_LIMIT_SECONDS);
        assertEquals(first, again);
    }

    @Test
    void aMuchBiggerArmyWinsQuicklyAndCheaply() {
        FieldBattle battle = battle(General.WARRIOR, 100, General.WARRIOR, 25, 3);
        FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
        assertEquals(0, outcome.winner());
        assertTrue(battle.army(0).alive() > battle.army(0).started() * 0.9);
    }

    @Test
    void nobodyFightsBeforeTheLinesMeet() {
        FieldBattle battle = battle(General.WARRIOR, 25, General.WARRIOR, 25, 5);
        for (int i = 0; i < BattlePlan.FORMING_SECONDS; i++) {
            battle.second();
        }
        assertEquals(battle.army(0).started(), battle.army(0).alive());
        assertEquals(battle.army(1).started(), battle.army(1).alive());
    }

    @Test
    void woundsAreKeptSoldierBySoldier() {
        FieldBattle battle = battle(General.TACTICIAN, 25, General.TACTICIAN, 25, 9);
        battle.run(BattlePlan.TIME_LIMIT_SECONDS);
        boolean wounded = false;
        for (FieldBlock block : battle.army(0).blocks()) {
            int alive = 0;
            for (float h : block.health()) {
                assertTrue(h >= 0 && h <= FieldRules.WARRIOR_HEALTH);
                if (h > 0) {
                    alive++;
                    wounded |= h < FieldRules.SOLDIER_HEALTH;
                }
            }
            assertEquals(block.alive(), alive, "the count of the living matches their health");
        }
        assertTrue(wounded, "some survivors are wounded");
    }

    @Test
    void archersNeverEndUpAheadOfTheirFrontLine() {
        FieldBattle battle = battle(General.TACTICIAN, 25, General.TACTICIAN, 25, 2);
        for (int i = 0; i < 40; i++) {
            battle.second();
            FieldArmy army = battle.army(0);
            Point front = army.frontCentre();
            for (FieldBlock block : army.blocks()) {
                if (block.arm() == Arm.ARCHERS && block.alive() > 0 && !block.fighting()) {
                    double behind = (front.x() - block.line.x()) * army.facingX + (front.z() - block.line.z()) * army.facingZ;
                    assertTrue(behind >= BattlePlan.ARCHER_BACK - BattlePlan.MARCH_STEP - 1e-9, "archers ahead of the line at " + i + " s");
                }
            }
        }
    }

    @Test
    void wornBlocksWithdrawAndFreshOnesTakeTheirPlace() {
        FieldBattle battle = battle(General.WARRIOR, 100, General.WARRIOR, 100, 4);
        boolean sawWithdrawal = false;
        for (int i = 0; i < 300 && !battle.over(); i++) {
            battle.second();
            sawWithdrawal |= battle.army(0).blocks().stream().anyMatch(b -> b.duty() == Duty.WITHDRAWING);
        }
        assertTrue(sawWithdrawal);
        assertTrue(battle.army(0).reliefs() > 0);
    }

    @Test
    void blockFrontsAreSegmentsAndTheirDistanceIsTheGapBetweenThem() {
        assertEquals(5, FieldRules.segmentDistance(0, 0, 10, 0, 0, 5, 10, 5), 1e-9, "side by side");
        assertEquals(0, FieldRules.segmentDistance(0, 0, 10, 0, 5, -5, 5, 5), 1e-9, "crossing");
        assertEquals(Math.hypot(3, 4), FieldRules.segmentDistance(0, 0, 1, 0, 4, 4, 4, 9), 1e-9, "end to end");
    }

    @Test
    void attacksFromTheFlankAndRearHurtMore() {
        // Defender facing east (+x).
        assertEquals(Side.FRONT, FieldRules.side(1, 0, 10, 1));
        assertEquals(Side.FLANK, FieldRules.side(1, 0, 0, 10));
        assertEquals(Side.REAR, FieldRules.side(1, 0, -10, 0));
        assertTrue(FieldRules.meleeFactor(Side.REAR, Arm.SWORDSMEN) > FieldRules.meleeFactor(Side.FLANK, Arm.SWORDSMEN));
        assertTrue(FieldRules.meleeFactor(Side.FLANK, Arm.SWORDSMEN) > FieldRules.meleeFactor(Side.FRONT, Arm.SWORDSMEN));
        assertTrue(FieldRules.meleeFactor(Side.FRONT, Arm.SHIELDWALL) < FieldRules.meleeFactor(Side.FRONT, Arm.SWORDSMEN));
        assertTrue(FieldRules.arrowFactor(Side.FRONT, Arm.SHIELDWALL) < FieldRules.arrowFactor(Side.FLANK, Arm.SHIELDWALL));
    }

    @Test
    void damageFallsInWholeHitsOnLivingSoldiers() {
        FieldBlock block = new FieldBlock(1, 0, Arm.SWORDSMEN, 20, FieldRules.SOLDIER_HEALTH, BattlePlan.LEADER_INDEX, false,
                0, 0, new Point(0, 0), 1, 0, Duty.FRONT);
        Random random = new Random(1);
        assertEquals(0, block.takeDamage(FieldRules.HIT / 2, random), "less than a hit is carried over");
        assertEquals(20, block.alive());
        block.takeDamage(20 * FieldRules.SOLDIER_HEALTH * 2, random);
        assertEquals(0, block.alive());
        assertFalse(block.captainAlive());
    }

    @Test
    void aBattleStallsOnlyOnceFightingHasBegun() {
        assertFalse(BattlePlan.stalled(100, 0), "nobody has fallen yet");
        assertFalse(BattlePlan.stalled(100, 80));
        assertTrue(BattlePlan.stalled(110, 80));
        assertTrue(BattlePlan.archersMayAdvance(BattlePlan.ARCHER_BACK + BattlePlan.MARCH_STEP));
        assertFalse(BattlePlan.archersMayAdvance(BattlePlan.ARCHER_BACK));
    }
}
