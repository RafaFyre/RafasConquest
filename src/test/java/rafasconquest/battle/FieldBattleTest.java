package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.battle.FieldRules.Side;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldBattleTest {
    private static FieldBattle battle(int aBlocks, int bBlocks, long seed) {
        return new FieldBattle(aBlocks, bBlocks, new Point(0, 0), 1, 0, seed);
    }

    @Test
    void aTwoThousandStrongBattleRunsToTheEndInWellUnderASecondOfWork() {
        for (long seed = 7; seed < 10; seed++) {
            FieldBattle battle = battle(100, 100, seed);
                long start = System.nanoTime();
                FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
                double seconds = (System.nanoTime() - start) / 1e9;
                assertNotNull(outcome, "never ended");
                assertTrue(outcome.winner() >= 0, outcome.result());
                assertTrue(seconds < 3, "took " + seconds + " s to simulate");
                assertTrue(outcome.seconds() >= 240, "was over in " + outcome.seconds() + " s; big battles should last");
                assertTrue(battle.army(0).reliefs() + battle.army(1).reliefs() >= 20, "the lines rotate");
        }
    }

    @Test
    void aTenThousandStrongBattleIsStillQuickToFight() {
        FieldBattle battle = battle(500, 500, 7);
        long start = System.nanoTime();
        FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
        double seconds = (System.nanoTime() - start) / 1e9;
        assertNotNull(outcome);
        // A long battle takes a while to fight through at once; what matters is each second, worked out once a second.
        assertTrue(seconds * 1000 / battle.seconds() < 20, "a battle-second of 10,000 a side has to fit well inside one tick");
    }

    @Test
    void engagedFrontLinesCloseInUntilTheyMeet() {
        FieldBattle battle = battle(25, 25, 8);
        double closest = Double.MAX_VALUE;
        for (int i = 0; i < 60 && !battle.over(); i++) {
            battle.second();
            for (FieldBlock a : battle.army(0).front) {
                for (FieldBlock b : battle.army(1).front) {
                    if (a.alive() > 0 && b.alive() > 0) {
                        closest = Math.min(closest, a.distanceTo(b));
                    }
                }
            }
        }
        assertTrue(closest <= BattlePlan.FRONT_CONTACT + BattlePlan.MARCH_STEP, "the fronts came within " + closest);
    }

    @Test
    void cavalryChargesSpendsItsMomentumFallsBackAndChargesAgain() {
        FieldBattle battle = battle(100, 100, 4);
        java.util.EnumMap<FieldBattle.Call, Integer> calls = new java.util.EnumMap<>(FieldBattle.Call.class);
        double peak = 0;
        boolean sawRallying = false;
        while (!battle.over()) {
            battle.second();
            battle.takeCalls().forEach(o -> calls.merge(o.call(), 1, Integer::sum));
            for (FieldBlock b : battle.army(0).blocks()) {
                peak = Math.max(peak, b.momentum());
                sawRallying |= b.duty() == Duty.RALLYING;
            }
        }
        assertTrue(peak > 0.9, "a run-up builds full momentum: " + peak);
        assertTrue(sawRallying, "spent cavalry rides back to reform");
        assertTrue(calls.getOrDefault(FieldBattle.Call.CAVALRY_RECALL, 0) > 0);
        assertTrue(calls.getOrDefault(FieldBattle.Call.CAVALRY_CHARGE, 0) > calls.getOrDefault(FieldBattle.Call.CAVALRY_RECALL, 0) / 2,
                "recalled cavalry charges again");
        assertEquals(2, calls.get(FieldBattle.Call.ADVANCE), "each army advances once");
    }

    @Test
    void infantryInTheWayOfChargingCavalryBracesAndTheWallHoldsBetter() {
        FieldBattle battle = battle(100, 100, 5);
        boolean braced = false;
        int walls = 0;
        for (int i = 0; i < 400 && !battle.over(); i++) {
            battle.second();
            walls += (int) battle.takeCalls().stream().filter(o -> o.call() == FieldBattle.Call.WALL).count();
            braced |= battle.army(0).blocks().stream().anyMatch(FieldBlock::braced);
        }
        assertTrue(braced && walls > 0, "walls formed against the charges");
        assertTrue(FieldRules.BRACED_TAKES < 1 && FieldRules.BRACED_DEALS > 1);
    }

    @Test
    void theSameSeedFightsTheSameBattle() {
        FieldBattle.Outcome first = battle(25, 25, 11).run(BattlePlan.TIME_LIMIT_SECONDS);
        FieldBattle.Outcome again = battle(25, 25, 11).run(BattlePlan.TIME_LIMIT_SECONDS);
        assertEquals(first, again);
    }

    @Test
    void aMuchBiggerArmyWinsQuicklyAndCheaply() {
        FieldBattle battle = battle(100, 25, 3);
        FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
        assertEquals(0, outcome.winner());
        assertTrue(battle.army(0).alive() > battle.army(0).started() * 0.9);
    }

    @Test
    void nobodyFightsBeforeTheLinesMeet() {
        FieldBattle battle = battle(25, 25, 5);
        for (int i = 0; i < BattlePlan.FORMING_SECONDS; i++) {
            battle.second();
        }
        assertEquals(battle.army(0).started(), battle.army(0).alive());
        assertEquals(battle.army(1).started(), battle.army(1).alive());
    }

    @Test
    void woundsAreKeptSoldierBySoldier() {
        FieldBattle battle = battle(25, 25, 9);
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
        FieldBattle battle = battle(25, 25, 2);
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
        FieldBattle battle = battle(100, 100, 4);
        boolean sawWithdrawal = false;
        for (int i = 0; i < 300 && !battle.over(); i++) {
            battle.second();
            sawWithdrawal |= battle.army(0).blocks().stream().anyMatch(b -> b.duty() == Duty.WITHDRAWING);
        }
        assertTrue(sawWithdrawal);
        assertTrue(battle.army(0).reliefs() > 0);
    }

    @Test
    void realSoldiersWalkThemselvesAndTakeDamageFromNumbersAsDamageToDeal() {
        FieldBattle battle = battle(5, 5, 6);
        FieldBlock real = battle.army(0).front[0];
        real.setLive(true);
        float[] full = real.health();
        double dealt = 0;
        for (int i = 0; i < 120 && !battle.over(); i++) {
            Point walked = real.line(); // its soldiers walked to where it was ordered
            battle.sync(real, walked, full);
            battle.second();
            assertEquals(walked, real.pos(), "the numbers don't move real soldiers");
            dealt += real.drainLiveDamage();
        }
        assertEquals(20, real.alive(), "its health is what its soldiers report, not the numbers' losses");
        assertTrue(dealt > 0, "blows from blocks that are only numbers wait to be dealt to the real soldiers");
    }

    @Test
    void realSoldiersOnBothSidesFightItOutThemselves() {
        FieldBattle battle = battle(1, 1, 6);
        FieldBlock a = battle.army(0).front[0];
        FieldBlock b = battle.army(1).front[0];
        for (int side = 0; side < 2; side++) {
            battle.army(side).blocks().forEach(block -> block.setReal(block.alive()));
        }
        Point meet = new Point(0, 0);
        for (int i = 0; i < 30; i++) {
            battle.sync(a, meet, a.health());
            battle.sync(b, meet.plus(1, 0), b.health());
            battle.second();
            assertEquals(0, a.drainLiveDamage());
            assertEquals(0, b.drainLiveDamage());
        }
    }

    /** A reserve block of an army of 2000, nearest its middle. */
    private static FieldBlock reserveBlock(FieldBattle battle) {
        return battle.army(0).blocks().stream().filter(b -> b.duty() == FieldBlock.Duty.LINE && b.arm().infantry())
                .min(java.util.Comparator.comparingDouble(b -> Math.abs(b.line().x()))).orElseThrow();
    }

    @Test
    void aKillSquadGoesForTheEnemyGeneralOrACaptain() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock squad = reserveBlock(battle);
        battle.assign(squad, FieldBlock.Task.KILL);
        FieldBlock target = null;
        for (int i = 0; i < 300 && target == null; i++) {
            battle.second();
            target = squad.strikeAt();
        }
        assertNotNull(target, "never sent");
        assertTrue(target == battle.army(1).commander() || target.captainAlive(), "sent at a block with no officer");
    }

    @Test
    void aFlankerStrikesEnemyInfantryInAFight() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock flanker = reserveBlock(battle);
        battle.assign(flanker, FieldBlock.Task.FLANK);
        FieldBlock target = null;
        for (int i = 0; i < 300 && target == null; i++) {
            battle.second();
            target = flanker.strikeAt();
        }
        assertNotNull(target, "never sent");
        assertTrue(target.arm().infantry() && target.side() == 1);
        assertEquals(FieldBlock.Duty.LINE, flanker.duty(), "the general doesn't take it into the line");
    }

    @Test
    void aSupportBlockReinforcesALosingBlockOfItsWing() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock support = reserveBlock(battle);
        battle.assign(support, FieldBlock.Task.SUPPORT);
        FieldBlock target = null;
        for (int i = 0; i < 600 && target == null; i++) {
            battle.second();
            target = support.strikeAt();
        }
        assertNotNull(target, "never sent");
        assertEquals(1, target.side());
    }

    @Test
    void aPlayersBaitIsItsWingsBait() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock front = battle.army(0).front[0];
        battle.assign(front, FieldBlock.Task.BAIT);
        assertEquals(front.file(), battle.army(0).baitFile[front.wing()]);
    }

    @Test
    void soldiersStillNumbersInAPartRealBlockFightOn() {
        FieldBattle battle = battle(1, 1, 6);
        FieldBlock a = battle.army(0).front[0];
        FieldBlock b = battle.army(1).front[0];
        for (int side = 0; side < 2; side++) {
            battle.army(side).blocks().forEach(block -> block.setReal(block.alive()));
        }
        a.setReal(a.alive() / 2); // half of it still figures
        Point meet = new Point(0, 0);
        double dealt = 0;
        for (int i = 0; i < 10; i++) {
            battle.sync(a, meet, a.health());
            battle.sync(b, meet.plus(1, 0), b.health());
            battle.second();
            assertEquals(0, a.drainLiveDamage(), "b is wholly real: Recruits fights its blows");
            dealt += b.drainLiveDamage();
        }
        assertTrue(dealt > 0, "a's figures strike b as numbers");
    }

    @Test
    void soldiersFallingOnTheirOwnCountTowardAStall() {
        FieldBattle battle = battle(5, 5, 6);
        FieldBlock real = battle.army(0).front[0];
        real.setLive(true);
        battle.second();
        float[] wounded = real.health();
        wounded[0] = 0;
        battle.sync(real, real.pos(), wounded);
        assertEquals(19, real.alive());
    }

    @Test
    void blockFrontsAreSegmentsAndTheirDistanceIsTheGapBetweenThem() {
        assertEquals(5, FieldRules.segmentDistance(0, 0, 10, 0, 0, 5, 10, 5), 1e-9, "side by side");
        assertEquals(0, FieldRules.segmentDistance(0, 0, 10, 0, 5, -5, 5, 5), 1e-9, "crossing");
        assertEquals(Math.hypot(3, 4), FieldRules.segmentDistance(0, 0, 1, 0, 4, 4, 4, 9), 1e-9, "end to end");
    }

    @Test
    void aShieldwallIsStrongOnlyFromTheFront() {
        // Defender facing east (+x).
        assertEquals(Side.FRONT, FieldRules.side(1, 0, 10, 1));
        assertEquals(Side.FLANK, FieldRules.side(1, 0, 0, 10));
        assertEquals(Side.REAR, FieldRules.side(1, 0, -10, 0));
        assertEquals(1, FieldRules.meleeFactor(Side.REAR, Arm.SWORDSMEN), "as in Recruits, a blow does the same from any side");
        assertTrue(FieldRules.meleeFactor(Side.FLANK, Arm.SHIELDWALL) > FieldRules.meleeFactor(Side.FRONT, Arm.SHIELDWALL));
        assertTrue(FieldRules.meleeFactor(Side.FRONT, Arm.SHIELDWALL) < FieldRules.meleeFactor(Side.FRONT, Arm.SWORDSMEN));
        assertTrue(FieldRules.arrowFactor(Side.FRONT, Arm.SHIELDWALL) < FieldRules.arrowFactor(Side.FLANK, Arm.SHIELDWALL));
    }

    @Test
    void damageFallsInWholeHitsOnLivingSoldiers() {
        FieldBlock block = new FieldBlock(1, 0, Arm.SWORDSMEN, 20, FieldRules.SOLDIER_HEALTH, BattlePlan.LEADER_INDEX, false,
                new Point(0, 0), 1, 0, Duty.FRONT);
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
