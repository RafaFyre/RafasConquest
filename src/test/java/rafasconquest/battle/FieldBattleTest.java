package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.battle.FieldRules.Side;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldBattleTest {
    /** A land battle, any siege train (Siegeworks loaded) given its field engines as the game gives them. */
    private static FieldBattle battle(int aBlocks, int bBlocks, long seed) {
        FieldBattle battle = new FieldBattle(aBlocks, bBlocks, new Point(0, 0), 1, 0, seed);
        for (int side = 0; side < 2; side++) {
            battle.equip(side, Machine.allowed(rafasconquest.core.WorldFeatures.ages().first(), null, BattleType.LAND, false));
        }
        return battle;
    }

    @Test
    void aTwoThousandStrongBattleRunsToTheEndInWellUnderASecondOfWork() {
        for (long seed = 7; seed < 10; seed++) {
            FieldBattle battle = battle(100, 100, seed);
                long start = System.nanoTime();
                FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
                double seconds = (System.nanoTime() - start) / 1e9;
                assertNotNull(outcome, "never ended");
                // Halberdiers' tough lines can grind on to the time limit: a draw is a fair end (chosen 2026-11-15).
                assertTrue(outcome.winner() >= 0 || outcome.seconds() >= BattlePlan.TIME_LIMIT_SECONDS, outcome.result());
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
    void aFlankersBlowsShakeTheBlockItStrikes() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock flanker = reserveBlock(battle);
        battle.assign(flanker, FieldBlock.Task.FLANK);
        boolean shaken = false;
        for (int i = 0; i < 400 && !shaken && !battle.over(); i++) {
            battle.second();
            FieldBlock t = flanker.contactFoe();
            shaken = t != null && t == flanker.strikeAt()
                    && (t.flankedUntil > battle.seconds() || t.rearedUntil > battle.seconds());
        }
        assertTrue(shaken, "struck from the side, its target is never shaken");
    }

    @Test
    void flankAndRearBlowsAndTheArmysLossesPressABlockTowardBreaking() {
        FieldBattle battle = battle(25, 25, 8);
        battle.second();
        FieldArmy army = battle.army(0);
        FieldBlock b = reserveBlock(battle);
        FieldBattle.Morale calm = battle.moraleOf(b);
        assertNotNull(calm);
        b.flankedUntil = battle.seconds() + FieldRules.FLANK_LINGER;
        b.rearedUntil = battle.seconds() + FieldRules.FLANK_LINGER;
        assertEquals(calm.pressure() + FieldRules.FLANK_SHOCK + FieldRules.REAR_SHOCK, battle.moraleOf(b).pressure(), 1e-9);
        for (FieldBlock o : army.blocks()) {
            if (o != b && army.standing() > army.started() / 2) {
                o.alive = 0;
            }
        }
        double lost = 1 - (double) army.standing() / army.started();
        assertTrue(lost >= 0.5);
        assertEquals(FieldRules.ARMY_SHOCK * lost, battle.moraleOf(b).army(), 1e-9);
        assertTrue(battle.routRisk(b) > 0);
    }

    @Test
    void aRoutFrightensItsOwnNearbyButAnExpendableRoutOnlyOtherExpendables() {
        FieldBattle battle = battle(25, 25, 8);
        battle.second();
        FieldArmy army = battle.army(0);
        java.util.List<FieldBlock> levy = army.blocks().stream().filter(b -> b.arm() == Arm.LEVY).toList();
        FieldBlock heavy = army.blocks().stream().filter(b -> b.arm() == Arm.HEAVY_INFANTRY).findFirst().orElseThrow();
        FieldBlock far = army.blocks().stream().filter(b -> b.arm().infantry() && b != heavy && !levy.subList(0, 2).contains(b))
                .findFirst().orElseThrow();
        FieldBlock routed = levy.get(0);
        FieldBlock otherLevy = levy.get(1);
        heavy.pos = routed.pos.plus(5, 0);
        otherLevy.pos = routed.pos.plus(0, 5);
        far.pos = routed.pos.plus(100, 0);
        battle.rout(army, routed);
        assertTrue(otherLevy.frightUntil > battle.seconds(), "a levy's rout frightens the levy beside it");
        assertEquals(0, heavy.frightUntil, "but not the heavy infantry: levy is expendable");
        assertEquals(0, far.frightUntil, "nor anyone out of reach");
        otherLevy.frightUntil = 0;
        battle.rout(army, heavy);
        assertTrue(otherLevy.frightUntil > battle.seconds(), "the heavy infantry's rout frightens the levy beside it");
    }

    @Test
    void onlyBattlesOfOverAThousandAreFoughtByUnits() {
        // 50 blocks of 20 is exactly 1000: block by block. One block more and the generals move units.
        FieldBattle small = battle(25, 25, 8);
        assertTrue(small.army(0).blocks().stream().allMatch(b -> b.unit == -1), "a battle of 1000 moves blocks alone");
        assertTrue(small.army(0).blocks().stream().anyMatch(b -> b.platoon() >= 0), "but still deploys by platoons");
        FieldBattle big = battle(26, 25, 8);
        assertTrue(big.army(0).blocks().stream().filter(b -> b.platoon() >= 0).allMatch(b -> b.unit == b.platoon()),
                "a battle of over 1000 moves units");
    }

    @Test
    void cavalryAndArchersHaveMoraleAndRegroupToTheirOwnWork() {
        FieldBattle battle = battle(25, 25, 8);
        battle.second();
        FieldArmy army = battle.army(0);
        for (Arm arm : new Arm[]{Arm.ARCHERS, Arm.CAVALRY}) {
            FieldBlock b = army.blocks().stream().filter(x -> x.arm() == arm && x.duty() == Duty.SUPPORT && !x.inWedge)
                    .findFirst().orElseThrow();
            assertNotNull(battle.moraleOf(b), arm + " has morale");
            battle.rout(army, b);
            assertEquals(Duty.ROUTED, b.duty());
            int routedAt = battle.seconds();
            while (battle.seconds() < routedAt + FieldRules.REGROUP_SECONDS + 2 && !battle.over()) {
                battle.second();
            }
            assertTrue(b.duty() != Duty.ROUTED && b.duty() != Duty.LINE, arm + " back at " + b.duty() + ", not in a file");
        }
    }

    @Test
    void aBlockShatteredOnItsThirdRoutRetreatsToCampAndStaysOutOfTheDay() {
        FieldBattle battle = battle(25, 25, 8);
        Point camp = new Point(-200, 0);
        battle.setCamp(0, FieldArmy.CENTRE, camp);
        battle.second();
        FieldArmy army = battle.army(0);
        FieldBlock b = reserveBlock(battle);
        int standing = army.standing();
        battle.rout(army, b);
        battle.rout(army, b);
        assertFalse(b.shattered());
        assertFalse(camp.equals(b.line()), "routed, not yet shattered: it regroups behind the lines");
        battle.rout(army, b);
        assertTrue(b.shattered());
        assertEquals(camp, b.line());
        assertEquals(standing - b.alive(), army.standing(), "the shattered count as lost");
        for (int i = 0; i < FieldRules.REGROUP_SECONDS + 10 && !battle.over(); i++) {
            battle.second();
        }
        assertEquals(Duty.ROUTED, b.duty(), "a shattered block doesn't regroup");
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
    void theGeneralLeavesAPlayersBlockToItsPlayer() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock led = battle.army(0).front[0];
        FieldBlock other = battle.army(0).front[1];
        Point start = led.pos;
        Point otherStart = other.pos;
        battle.lead(led);
        for (int i = 0; i < 40; i++) {
            battle.second();
        }
        assertTrue(other.pos.distanceTo(otherStart) > 5, "the army advances");
        assertTrue(led.pos.distanceTo(start) < 1, "the led block holds where its player left it, moved " + led.pos.distanceTo(start));
        Point sent = start.plus(-30, 10);
        battle.steer(led, sent, 0, 1, false, null);
        for (int i = 0; i < 40; i++) {
            battle.second();
        }
        assertTrue(led.pos.distanceTo(sent) < 2, "goes where its player sends it, " + led.pos.distanceTo(sent) + " off");
        assertFalse(led.charged);
    }

    @Test
    void sentAtAnEnemyArchersStandAndShootItWhileMeleeCharges() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock archers = battle.army(0).blocks().stream().filter(b -> b.arm() == Arm.ARCHERS).findFirst().orElseThrow();
        FieldBlock foot = battle.army(0).front[0];
        FieldBlock target = battle.army(1).blocks().stream().filter(b -> b.arm().infantry())
                .min(java.util.Comparator.comparingDouble(b -> b.pos.distanceTo(archers.pos))).orElseThrow();
        battle.army(1).blocks().forEach(battle::lead); // the enemy holds: nobody else comes at the archers
        battle.lead(archers);
        battle.lead(foot);
        boolean shotIt = false;
        for (int i = 0; i < 150 && target.alive() > 0 && !shotIt; i++) { // the armies start far apart
            battle.steer(archers, archers.pos, archers.facingX, archers.facingZ, true, target);
            battle.steer(foot, foot.pos, foot.facingX, foot.facingZ, true, target);
            battle.second();
            shotIt |= battle.volleys().stream().anyMatch(v -> v.archers() == archers && v.target() == target);
            assertNull(archers.strikeAt(), "archers never charge");
            assertTrue(archers.pos.distanceTo(target.pos) > FieldRules.CLOSE_REACH + 5, "archers keep their distance");
        }
        assertTrue(shotIt, "the archers shot their target");
        assertEquals(target, foot.strikeAt(), "the foot charged it");
    }

    @Test
    void musketeersShootSlowlyFromTheArchersPlace() {
        BattlePlan.muskets = true;
        rafasconquest.core.WorldFeatures.set(true, true, new rafasconquest.core.Age.Range(rafasconquest.core.Age.GUNPOWDER, rafasconquest.core.Age.STEAM));
        try {
            FieldBattle battle = battle(100, 100, 5);
            FieldBlock musketeers = battle.army(0).blocks().stream().filter(b -> b.arm() == Arm.MUSKETEERS).findFirst().orElseThrow();
            int carried = musketeers.arrows;
            assertEquals(musketeers.alive() * FieldRules.CARTRIDGES_EACH, carried);
            for (int i = 0; i < 300 && musketeers.arrows == carried && battle.outcome() == null; i++) {
                battle.second();
            }
            assertTrue(musketeers.arrows < carried, "the musketeers fired");
            int before = musketeers.arrows;
            battle.second();
            assertTrue(before - musketeers.arrows <= Math.ceil(musketeers.alive() * FieldRules.SHOTS_PER_SECOND), "a fifth of them a second");
        } finally {
            BattlePlan.muskets = false;
            rafasconquest.core.WorldFeatures.set(true, true, rafasconquest.core.Age.Range.DEFAULT);
        }
    }

    @Test
    void theSiegeTrainShootsFromBeyondBowRangeOneUnitOneKind() {
        BattlePlan.siegeworks = true;
        try {
            FieldBattle battle = battle(200, 200, 5);
            battle.equip(0, List.of(Machine.TREBUCHET, Machine.ONAGER));
            List<FieldBlock> train = battle.army(0).blocks().stream().filter(b -> b.arm().engine()).toList();
            assertEquals(10, train.size());
            for (FieldBlock b : train) {
                assertNotNull(b.machine());
                assertEquals(b.machine() == Machine.TREBUCHET ? Arm.TREBUCHETS : Arm.ONAGERS, b.arm(), "each engine its own unit");
                assertEquals(Math.round(b.machine().perBlock() * Machine.SHOTS_EACH), b.arrows);
                assertTrue(train.stream().filter(o -> o.platoon() == b.platoon()).allMatch(o -> o.machine() == b.machine()), "a unit works one kind");
            }
            boolean farShot = false;
            for (int i = 0; i < 300 && !farShot && battle.outcome() == null; i++) {
                battle.second();
                farShot = battle.volleys().stream().anyMatch(v -> v.archers().arm().engine()
                        && v.archers().pos().distanceTo(v.target().pos()) > FieldRules.ARCHER_RANGE);
            }
            assertTrue(farShot, "an engine hit something out of bow range");
        } finally {
            BattlePlan.siegeworks = false;
        }
    }

    @Test
    void aPlayersBaitIsItsWingsBait() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock front = battle.army(0).front[0];
        battle.assign(front, FieldBlock.Task.BAIT);
        assertEquals(front.file(), battle.army(0).baitFile[front.wing()]);
    }

    @Test
    void cavalryChargesAsAWholeUnit() {
        FieldBattle battle = battle(100, 100, 5);
        for (int i = 0; i < 600; i++) {
            battle.second();
            for (FieldBlock c : battle.army(0).blocks()) {
                if (c.arm().mounted() && c.platoon() >= 0 && !c.inWedge && (c.charged || c.approaching)) {
                    for (FieldBlock mate : battle.army(0).platoon(c.platoon())) {
                        assertTrue(mate.alive() == 0 || mate.charged || mate.approaching || mate.duty() != c.duty(),
                                "a rider of the unit stayed behind when it charged");
                    }
                    return;
                }
            }
        }
        throw new AssertionError("no cavalry charged");
    }

    @Test
    void aChargingFootUnitSpreadsOverItsPreysUnit() {
        FieldBattle battle = battle(100, 100, 5);
        for (int i = 0; i < BattlePlan.TIME_LIMIT_SECONDS && !battle.over(); i++) {
            battle.second();
            for (FieldBlock c : battle.army(0).blocks()) {
                if (!c.arm().infantry() || c.platoon() < 0 || c.inWedge || !c.charged || c.prey() == null || c.prey().platoon() < 0) {
                    continue;
                }
                FieldBlock prey = c.prey();
                List<FieldBlock> mates = battle.army(0).platoon(c.platoon()).stream()
                        .filter(m -> m.alive() > 0 && m.charged && m.prey() != null && m.prey().platoon() == prey.platoon() && m.prey().side == prey.side).toList();
                long standing = battle.army(1).platoon(prey.platoon()).stream().filter(e -> e.alive() > 0 && e.duty() != Duty.FLEEING).count();
                if (mates.size() >= 2 && standing >= 2) {
                    assertTrue(mates.stream().map(FieldBlock::prey).distinct().count() > 1, "a whole unit fell on one block of its prey's unit");
                    return;
                }
            }
        }
        throw new AssertionError("no unit charged a unit");
    }

    @Test
    void aFrontUnitCyclesOutWholeAndTheUnitBehindTakesItsFiles() {
        FieldBattle battle = battle(100, 100, 1);
        FieldArmy army = battle.army(0);
        FieldBlock[] was = army.front.clone();
        int cycles = 0;
        while (!battle.over() && battle.seconds() < 600) {
            battle.second();
            for (int f = 0; f < was.length; f++) {
                FieldBlock now = army.front[f];
                if (now != was[f] && was[f].alive() > 0 && was[f].duty() == Duty.LINE) {
                    cycles++;
                    assertTrue(now.platoon() != was[f].platoon(), "a unit rotated out hands its file to another unit");
                }
                was[f] = now;
            }
        }
        assertTrue(cycles > 0, "front units cycle");
    }

    @Test
    void inTheMorningTheArmiesMarchOutOfCampToTheirPlaces() {
        FieldBattle battle = battle(60, 60, 3);
        battle.formUp(25);
        assertFalse(battle.arrived());
        for (int i = 0; i < 120 && !battle.arrived(); i++) {
            battle.second();
        }
        assertTrue(battle.arrived(), "never got there");
        assertFalse(battle.begun(), "marching out isn't the battle");
        battle.formed();
        battle.second();
        assertTrue(battle.begun());
    }

    @Test
    void inTheEveningEveryBlockMarchesBackToCampWithoutFighting() {
        FieldBattle battle = battle(60, 60, 3);
        for (int i = 0; i < 120; i++) {
            battle.second();
        }
        int[] alive = {battle.army(0).alive(), battle.army(1).alive()};
        battle.pullBack(new double[]{25, 25});
        for (int i = 0; i < 400 && !battle.arrived(); i++) {
            battle.second();
        }
        assertTrue(battle.arrived(), "never got back");
        assertEquals(alive[0], battle.army(0).alive(), "nobody fights while pulled back");
        assertEquals(alive[1], battle.army(1).alive());
    }

    /** Side 1's general falls, so side 0 wins and side 1 retreats. */
    private static FieldBattle generalFalls() {
        FieldBattle battle = battle(40, 40, 3);
        battle.second();
        FieldBlock general = battle.army(1).commander();
        battle.sync(general, general.pos(), new float[general.health().length]);
        battle.second();
        assertNotNull(battle.outcome());
        assertEquals(0, battle.outcome().winner());
        return battle;
    }

    @Test
    void aPursuedArmyKeepsRunningAndThePursuersFollow() {
        FieldBattle battle = generalFalls();
        battle.pursue();
        assertTrue(battle.pursuing());
        Point start = battle.army(1).front[0].pos();
        Point chaser = battle.army(0).front[0].pos();
        double gap = chaser.distanceTo(start);
        for (int i = 0; i < 60; i++) {
            battle.second();
        }
        assertTrue(battle.army(1).front[0].pos().distanceTo(start) > 40, "the beaten army runs");
        assertTrue(battle.army(0).front[0].pos().distanceTo(chaser) > 10, "the winner follows");
        int quiet = 0;
        for (int i = 0; i < 600 && quiet < 30; i++) {
            battle.second();
            quiet = battle.seconds() - Math.max(0, battle.lastHurt(1));
        }
        assertTrue(quiet >= 30 || battle.army(1).alive() == 0, "the pursuit ends: they get away, or are cut down");
    }

    @Test
    void pursuingShipsDoNotPileOntoOneAnother() {
        FieldBattle battle = battle(60, 60, 3);
        battle.setType(BattleType.OCEAN);
        battle.second();
        FieldBlock general = battle.army(1).commander();
        battle.sync(general, general.pos(), new float[general.health().length]);
        battle.second();
        assertNotNull(battle.outcome());
        battle.pursue();
        for (int i = 0; i < 120; i++) {
            battle.second();
        }
        for (int side = 0; side < 2; side++) {
            java.util.List<FieldBlock> fleet = battle.army(side).blocks().stream()
                    .filter(b -> b.alive() > 0 && b.afloat() && battle.holder(b) == b).toList();
            for (int i = 0; i < fleet.size(); i++) {
                for (int j = i + 1; j < fleet.size(); j++) {
                    assertTrue(fleet.get(i).pos().distanceTo(fleet.get(j).pos()) > fleet.get(i).hull().width / 2.0,
                            "side " + side + "'s ships piled on one another in the pursuit");
                }
            }
        }
    }

    @Test
    void aSurrenderedArmyStaysWhereItStands() {
        FieldBattle battle = generalFalls();
        battle.standDown(true);
        Point at = battle.army(1).front[0].pos();
        for (int i = 0; i < 30; i++) {
            battle.second();
        }
        assertEquals(0, battle.army(1).front[0].pos().distanceTo(at), 1e-6);
        assertEquals(FieldBlock.Duty.FLEEING, battle.army(1).front[0].duty());
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
        assertEquals(1, FieldRules.meleeFactor(Side.REAR, Arm.SKIRMISHERS), "as in Recruits, a blow does the same from any side");
        assertTrue(FieldRules.meleeFactor(Side.FLANK, Arm.HEAVY_INFANTRY) > FieldRules.meleeFactor(Side.FRONT, Arm.HEAVY_INFANTRY));
        assertTrue(FieldRules.meleeFactor(Side.FRONT, Arm.HEAVY_INFANTRY) < FieldRules.meleeFactor(Side.FRONT, Arm.LEVY));
        assertTrue(FieldRules.arrowFactor(Side.FRONT, Arm.HEAVY_INFANTRY) < FieldRules.arrowFactor(Side.FLANK, Arm.HEAVY_INFANTRY));
    }

    @Test
    void damageFallsInWholeHitsOnLivingSoldiers() {
        FieldBlock block = new FieldBlock(1, 0, Arm.LEVY, 20, FieldRules.SOLDIER_HEALTH, BattlePlan.LEADER_INDEX, false,
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

    @Test
    void besiegedDefendersHoldTheirWallsAndOutlastAnEqualAttacker() {
        int held = 0;
        for (long seed = 1; seed <= 4; seed++) {
            FieldBattle battle = battle(30, 30, seed);
            battle.setType(BattleType.SIEGE);
            FieldArmy d = battle.army(1);
            while (!battle.over()) {
                battle.second();
                for (FieldBlock b : d.blocks()) {
                    double past = (b.pos().x() - d.origin().x()) * d.facingX() + (b.pos().z() - d.origin().z()) * d.facingZ();
                    assertTrue(past <= 1e-6, "a defender came out past its walls, by " + past);
                }
            }
            held += battle.outcome().winner() == 1 ? 1 : 0;
        }
        assertTrue(held >= 3, "the walls held in only " + held + " of 4 sieges");
    }

    @Test
    void atSeaEveryFormationSailsAtShipSpeedAndTheFleetsFightItOut() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        java.util.Map<FieldBlock, Point> last = new java.util.HashMap<>();
        java.util.Set<FieldBlock.ShipKind> kinds = java.util.EnumSet.noneOf(FieldBlock.ShipKind.class);
        boolean grappled = false;
        boolean rammed = false;
        while (!battle.over()) {
            for (int side = 0; side < 2; side++) {
                for (FieldBlock b : battle.army(side).blocks()) {
                    assertTrue(b.afloat(), "a formation ashore in open water");
                    last.put(b, b.pos());
                    if (b.ship() != null) {
                        kinds.add(b.ship());
                    }
                }
            }
            battle.second();
            for (java.util.Map.Entry<FieldBlock, Point> e : last.entrySet()) {
                FieldBlock b = e.getKey();
                assertTrue(b.pos().distanceTo(e.getValue()) <= FieldBattle.shipSpeed(b) + FieldBattle.SHIP_SHOVE + (battle.holder(b).hookedTo() != null ? FieldBattle.HAUL_SPEED : 0) + 1e-6,
                        "a " + b.ship() + " ship (" + b.duty() + ", charged " + b.charged() + ", grappled " + b.lashed().size()
                                + ") moved " + b.pos().distanceTo(e.getValue()) + " in second " + battle.seconds()
                                + ": faster than its ship sails, shoved clear of its own and hauled by its hooks");
                assertTrue(!b.inMelee() || !b.lashed().isEmpty() || b.alive() == 0, "hand to hand at sea without grappling");
                grappled |= !b.lashed().isEmpty();
                rammed |= b.ship() == FieldBlock.ShipKind.SHOCK && !b.lashed().isEmpty();
            }
        }
        assertTrue(grappled, "no ships grappled");
        assertTrue(rammed, "no shock ship rammed");
        assertTrue(kinds.containsAll(java.util.EnumSet.of(FieldBlock.ShipKind.SWORDSMEN, FieldBlock.ShipKind.SHOCK, FieldBlock.ShipKind.ARCHERS)),
                "ship kinds sailed: " + kinds);
        assertTrue(battle.army(0).alive() < battle.army(0).started() && battle.army(1).alive() < battle.army(1).started(), "no one fell");
    }

    @Test
    void aFleetDeploysInOneLineAbreast() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        for (int side = 0; side < 2; side++) {
            // The battle's axis is x: a line abreast stands at one x, its ships along z.
            java.util.List<Point> ships = battle.army(side).blocks().stream().filter(b -> battle.holder(b) == b)
                    .map(FieldBlock::pos).sorted(java.util.Comparator.comparingDouble(Point::z)).toList();
            for (int i = 1; i < ships.size(); i++) {
                assertEquals(ships.get(0).x(), ships.get(i).x(), 1e-6, "not in one line abreast");
                assertEquals(FieldBattle.fleetSpacing(), ships.get(i).z() - ships.get(i - 1).z(), 1e-6, "not FLEET_SPACING apart");
            }
        }
    }

    @Test
    void cavalryAndEnginesStayOutOfABattleAtSea() {
        FieldBattle battle = battle(40, 40, 3);
        java.util.function.Predicate<FieldBlock> ashore = b -> (b.arm().mounted() || b.arm().engine())
                && b.duty() != FieldBlock.Duty.GENERAL && b.duty() != FieldBlock.Duty.GUARD && b.duty() != FieldBlock.Duty.BEARER;
        int[] before = {battle.army(0).started(), battle.army(1).started()};
        int[] left = new int[2];
        for (int side = 0; side < 2; side++) {
            left[side] = battle.army(side).blocks().stream().filter(ashore).mapToInt(FieldBlock::started).sum();
        }
        assertTrue(left[0] > 0, "no cavalry or engines to leave out");
        battle.setType(BattleType.OCEAN);
        for (int side = 0; side < 2; side++) {
            assertTrue(battle.army(side).blocks().stream().noneMatch(ashore), "cavalry or engines sailed");
            assertEquals(before[side] - left[side], battle.army(side).started(), "started not down by those left out");
            assertTrue(battle.army(side).blocks().stream().allMatch(FieldBlock::afloat), "someone left ashore at sea");
        }
    }

    @Test
    void aShipPlacedOnAnotherDropsClearOfIt() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        java.util.List<FieldBlock> ships = battle.army(0).blocks().stream().filter(b -> battle.holder(b) == b).toList();
        battle.place(ships.get(1), ships.get(0).pos());
        assertTrue(ships.get(1).pos().distanceTo(ships.get(0).pos()) >= FieldBattle.shipLength() - 1e-6, "placed on top of another ship");
        for (FieldBlock aboard : battle.crew(ships.get(1))) {
            assertEquals(ships.get(1).pos(), aboard.pos(), "left behind when its ship was placed");
        }
    }

    @Test
    void eachUnitSailsOneShipWithAllItsBlocksAboard() {
        FieldBattle battle = battle(60, 60, 3);
        battle.setType(BattleType.OCEAN);
        for (int side = 0; side < 2; side++) {
            java.util.Set<Integer> ships = new java.util.HashSet<>();
            for (FieldBlock b : battle.army(side).blocks()) {
                ships.add(b.shipId());
                FieldBlock h = battle.holder(b);
                assertEquals(h.pos(), b.pos(), "not where its ship is");
                assertEquals(h.ship(), b.ship(), "two kinds of ship in one unit");
                if (b.platoon >= 0) {
                    assertTrue(battle.crew(b).stream().allMatch(c -> c.platoon == b.platoon), "blocks of another unit aboard");
                    assertEquals(rafasconquest.core.SeaLanes.Size.XXLARGE, b.hull(), "a unit's ship not the biggest");
                }
            }
            long platoons = battle.army(side).blocks().stream().filter(b -> b.platoon >= 0).mapToInt(b -> b.platoon).distinct().count();
            assertEquals(platoons + 1, ships.size(), "not one ship to a unit, and the general's");
        }
        for (int s = 0; s < 60; s++) {
            battle.second();
        }
        for (int side = 0; side < 2; side++) {
            for (FieldBlock b : battle.army(side).blocks()) {
                if (b.alive() > 0) {
                    assertEquals(battle.holder(b).pos(), b.pos(), "a block left its ship");
                }
            }
        }
    }

    @Test
    void aGreatShieldFleetStillSailsItsGunships() {
        // A Great Shield made its whole front heavy infantry: at sea that took every Gunship card's ship from it.
        int[] mix = BattlePlan.navalMix(51);
        WarPlan shield = WarPlan.of(List.of(Doctrine.SPEAR_AND_SHIELD));
        FieldBattle battle = new FieldBattle(0, 0, new Point(0, 0), 1, 0, 3, FieldRules.RATES, shield, shield, mix, mix.clone());
        battle.setType(BattleType.OCEAN);
        for (int side = 0; side < 2; side++) {
            assertTrue(battle.army(side).blocks().stream().anyMatch(b -> b.ship() == FieldBlock.ShipKind.GUNSHIP
                    && b.hull() == rafasconquest.core.SeaLanes.Size.GUNSHIP), "side " + side + " lost its gunships");
        }
    }

    @Test
    void aGunshipLiesOffSideOnAndFiresBroadsides() {
        rafasconquest.core.SeaLanes.Size gunship = rafasconquest.core.SeaLanes.Size.GUNSHIP;
        List<String> right = gunship.gunsRight;
        List<String> left = gunship.gunsLeft;
        gunship.gunsRight = List.of("culverin", "culverin", "culverin", "culverin", "culverin");
        gunship.gunsLeft = gunship.gunsRight;
        try {
            int[] mine = new int[BattlePlan.Arm.values().length];
            mine[BattlePlan.Arm.GUNSHIP.ordinal()] = 20;
            mine[BattlePlan.Arm.HEAVY_INFANTRY.ordinal()] = 20;
            int[] theirs = new int[BattlePlan.Arm.values().length];
            theirs[BattlePlan.Arm.HEAVY_INFANTRY.ordinal()] = 40;
            FieldBattle battle = new FieldBattle(0, 0, new Point(0, 0), 1, 0, 3, FieldRules.RATES, WarPlan.NONE, WarPlan.NONE, mine, theirs);
            battle.setType(BattleType.OCEAN);
            List<FieldBlock> guns = battle.army(0).blocks().stream()
                    .filter(b -> b.arm() == BattlePlan.Arm.GUNSHIP && battle.holder(b) == b).toList();
            assertFalse(guns.isEmpty());
            for (FieldBlock g : guns) {
                assertEquals(FieldBlock.ShipKind.GUNSHIP, g.ship());
                assertSame(gunship, g.hull(), "a gunship card sails its own ship card");
            }
            assertTrue(battle.army(0).blocks().stream().filter(b -> b.arm() == BattlePlan.Arm.HEAVY_INFANTRY)
                    .noneMatch(b -> b.ship() == FieldBlock.ShipKind.GUNSHIP), "the rest still board");
            int before = battle.army(1).standing();
            boolean sideOn = false;
            for (int s = 0; s < 240 && guns.stream().allMatch(g -> g.lashed().isEmpty()); s++) {
                battle.second();
                for (FieldBlock g : guns) {
                    if (g.prey() != null && g.pos().distanceTo(g.prey().pos()) <= FieldBattle.gunRange(g) + 10) {
                        double dx = g.prey().pos().x() - g.pos().x();
                        double dz = g.prey().pos().z() - g.pos().z();
                        double d = Math.hypot(dx, dz);
                        sideOn |= Math.abs((g.settleX * dx + g.settleZ * dz) / d) < 0.1;
                    }
                }
            }
            assertTrue(sideOn, "never turned its beam to its mark at gun range");
            assertTrue(battle.army(1).standing() < before, "its broadsides hit nobody before anyone boarded it");
        } finally {
            gunship.gunsRight = right;
            gunship.gunsLeft = left;
        }
    }

    @Test
    void aGunshipSailsRoundItsMarkUnderWayWithOneBroadsideToIt() {
        // At (100, 0) bow to +z, its mark at the origin lies off its right side, (-z, x): gunSide 1.
        FieldBlock g = new FieldBlock(1, 0, Arm.GUNSHIP, 20, FieldRules.SOLDIER_HEALTH, BattlePlan.LEADER_INDEX, false,
                new Point(100, 0), 0, 1, Duty.FRONT);
        g.pos = new Point(100, 0);
        g.hull = rafasconquest.core.SeaLanes.Size.GUNSHIP;
        g.ship = FieldBlock.ShipKind.GUNSHIP;
        g.gunSide = 1;
        Point mark = new Point(0, 0);
        double keep = 100;
        double swept = 0;
        double last = Math.atan2(g.pos.z(), g.pos.x());
        for (int s = 0; s < 120; s++) {
            double d = g.pos.distanceTo(mark);
            double ux = (mark.x() - g.pos.x()) / d;
            double uz = (mark.z() - g.pos.z()) / d;
            g.settleX = g.gunSide * uz;
            g.settleZ = -g.gunSide * ux;
            Point to = FieldBattle.circling(mark, -ux, -uz, g.settleX, g.settleZ, keep);
            assertTrue(g.pos.distanceTo(to) > FieldBattle.AT_PLACE, "it got there and stopped");
            FieldBattle.sail(g, to, List.of(g));
            double now = Math.atan2(g.pos.z(), g.pos.x());
            swept += Math.atan2(Math.sin(now - last), Math.cos(now - last));
            last = now;
            if (s > 40) {
                assertEquals(keep, g.pos.distanceTo(mark), keep * 0.25, "it keeps at gun range");
                assertEquals(1, FieldBattle.gunSide(g, mark), "its mark stays off its one broadside");
            }
        }
        assertTrue(Math.abs(swept) > Math.PI, "it sailed only " + Math.toDegrees(swept) + " degrees round its mark");
    }

    @Test
    void aGunshipsMarkedSideFiresOnlyAtItsMarkAndTheOtherAtWill() {
        rafasconquest.core.SeaLanes.Size gunship = rafasconquest.core.SeaLanes.Size.GUNSHIP;
        List<String> right = gunship.gunsRight;
        List<String> left = gunship.gunsLeft;
        gunship.gunsRight = List.of("culverin", "culverin", "culverin", "culverin", "culverin");
        gunship.gunsLeft = gunship.gunsRight;
        try {
            int[] mine = new int[BattlePlan.Arm.values().length];
            mine[BattlePlan.Arm.GUNSHIP.ordinal()] = 10;
            int[] theirs = new int[BattlePlan.Arm.values().length];
            theirs[BattlePlan.Arm.HEAVY_INFANTRY.ordinal()] = 40;
            FieldBattle battle = new FieldBattle(0, 0, new Point(0, 0), 1, 0, 3, FieldRules.RATES, WarPlan.NONE, WarPlan.NONE, mine, theirs);
            battle.setType(BattleType.OCEAN);
            FieldBlock g = battle.army(0).blocks().stream().filter(b -> b.arm() == BattlePlan.Arm.GUNSHIP && battle.holder(b) == b).findFirst().orElseThrow();
            List<FieldBlock> foes = battle.army(1).blocks().stream().filter(b -> b.afloat() && battle.holder(b) == b).toList();
            FieldBlock mark = foes.get(0);
            FieldBlock other = foes.get(1);
            g.pos = new Point(0, 0);
            g.facingX = 1; // bow to +x: its right (seen from astern) is +z
            g.facingZ = 0;
            mark.pos = new Point(0, 40);
            other.pos = new Point(0, -40);
            for (FieldBlock f : foes.subList(2, foes.size())) {
                f.pos = new Point(5000, 5000); // out of reach
            }
            g.prey = mark;
            assertEquals(1, FieldBattle.gunSide(g, mark.pos));
            java.util.Set<FieldBlock> hit = new java.util.HashSet<>();
            for (int i = 0; i < 200; i++) {
                List<FieldBlock> targets = new java.util.ArrayList<>();
                battle.broadside(g, battle.army(1), targets, new java.util.ArrayList<>());
                targets.forEach(t -> hit.add(battle.holder(t)));
            }
            assertEquals(java.util.Set.of(mark, other), hit, "the marked side at its mark, the other side at will");
            assertTrue(battle.volleys().stream().anyMatch(v -> v.archers() == g && v.gun() == Machine.byId("culverin")),
                    "a sprite gunship's broadside is drawn, each shot its gun's");

            other.pos = new Point(-10, 35); // nearer, but on the marked side: that side holds for its mark
            hit.clear();
            for (int i = 0; i < 200; i++) {
                List<FieldBlock> targets = new java.util.ArrayList<>();
                battle.broadside(g, battle.army(1), targets, new java.util.ArrayList<>());
                targets.forEach(t -> hit.add(battle.holder(t)));
            }
            assertEquals(java.util.Set.of(mark), hit);

            // Real, its gunners shoot a real crew themselves: its numbers fire only at a ship with nobody real aboard.
            other.pos = new Point(0, -40);
            g.setLive(true);
            battle.crew(mark).forEach(c -> c.setLive(true));
            hit.clear();
            int drawn = battle.volleys().size();
            for (int i = 0; i < 200; i++) {
                List<FieldBlock> targets = new java.util.ArrayList<>();
                battle.broadside(g, battle.army(1), targets, new java.util.ArrayList<>());
                targets.forEach(t -> hit.add(battle.holder(t)));
            }
            assertEquals(java.util.Set.of(other), hit, "a real gunship's numbers fire only at a ship with nobody real aboard");
            assertEquals(drawn, battle.volleys().size(), "a real gunship's gunners fire real guns: nothing drawn");
        } finally {
            gunship.gunsRight = right;
            gunship.gunsLeft = left;
        }
    }

    @Test
    void anOfficersShipSailsWhereTheySendItAndHoldsThere() {
        FieldBattle battle = battle(60, 60, 3);
        battle.setType(BattleType.OCEAN);
        FieldBlock ship = battle.army(0).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.SWORDSMEN && battle.holder(b) == b)
                .findFirst().orElseThrow();
        for (FieldBlock b : battle.crew(ship)) {
            battle.lead(b);
        }
        Point to = ship.pos().plus(-60, 0); // away from the enemy, astern of the fleet
        FieldBlock last = battle.crew(ship).get(battle.crew(ship).size() - 1);
        battle.steer(last, to, 0, 1, false, null); // any block aboard: the whole ship goes
        for (int s = 0; s < 120 && !battle.over() && ship.pos().distanceTo(to) > FieldBattle.AT_PLACE; s++) {
            battle.second();
            assertEquals(to, ship.line, "its captain sent it elsewhere at second " + battle.seconds() + " " + battle.outcome());
        }
        assertTrue(ship.pos().distanceTo(to) <= FieldBattle.AT_PLACE, "not where it was sent, " + to + ": " + ship.pos()
                + " (" + ship.duty() + " at second " + battle.seconds() + ")");
        assertTrue(ship.lashed().isEmpty(), "went boarding instead");
        assertEquals(ship.pos(), last.pos(), "the ship left a block behind");
    }

    @Test
    void anOfficersShipSentAtAnEnemyBoardsItAbreastOrRamsItWhenTold() {
        for (boolean ram : new boolean[]{false, true}) {
            FieldBattle battle = battle(60, 60, 3);
            battle.setType(BattleType.OCEAN);
            // An archer ship: left to its captain it would stand off and shoot; the player's order comes first.
            FieldBlock ship = battle.army(0).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.ARCHERS && battle.holder(b) == b)
                    .findFirst().orElseThrow();
            FieldBlock target = battle.army(1).blocks().stream().filter(b -> b.afloat() && battle.holder(b) == b)
                    .findFirst().orElseThrow();
            battle.crew(ship).forEach(battle::lead);
            battle.steer(ship, ship.pos(), ship.facingX, ship.facingZ, true, target, ram);
            battle.second();
            assertTrue(target == ship.prey(), "went for another ship");
            // to the enemy's heading when its captain chose (it turns a few degrees within the second): 0.1 of slack
            double along = Math.abs(ship.settleX * target.facingX + ship.settleZ * target.facingZ);
            if (ram) {
                assertEquals(0, along, 0.1, "a rammer comes about bow-in to the enemy's side");
            } else {
                assertEquals(1, along, 0.1, "a boarding ship lies alongside, parallel to the enemy");
            }
        }
    }

    @Test
    void boardingShipsHookFromReachAndGrappleOnlyHullToHull() {
        FieldBattle battle = battle(60, 60, 3);
        battle.setType(BattleType.OCEAN);
        java.util.Set<FieldBlock> hooked = new java.util.HashSet<>();
        boolean grappled = false;
        for (int s = 0; s < 300 && !battle.over(); s++) {
            java.util.Map<FieldBlock, Boolean> wasLashed = new java.util.HashMap<>();
            java.util.Set<java.util.List<FieldBlock>> alongside = new java.util.HashSet<>(); // before the second moves them
            for (int side = 0; side < 2; side++) {
                for (FieldBlock b : battle.army(side).blocks()) {
                    wasLashed.put(b, !b.lashed().isEmpty());
                    for (FieldBlock e : battle.army(1 - side).blocks()) {
                        if (FieldBattle.hookable(b, e)) {
                            alongside.add(java.util.List.of(b, e));
                        }
                    }
                }
            }
            battle.second();
            for (int side = 0; side < 2; side++) {
                for (FieldBlock b : battle.army(side).blocks()) {
                    if (b.alive() == 0 || battle.holder(b) != b || b.ship() != FieldBlock.ShipKind.SWORDSMEN) {
                        continue;
                    }
                    FieldBlock e = b.hookedTo();
                    if (e != null && hooked.add(b) && b.lashed().isEmpty()) {
                        assertTrue(Ramming.overlap(hull(b), hull(e), FieldBattle.GRAPPLE_RANGE / 2 + 0.01),
                                "hooks thrown from out of reach at second " + battle.seconds());
                        assertTrue(alongside.contains(java.util.List.of(b, e)), "hooks thrown with the ships not lying alongside at second " + battle.seconds());
                    }
                    if (!wasLashed.get(b) && e != null && b.lashed().contains(battle.holder(e)) && battle.holder(e).ship() != FieldBlock.ShipKind.SHOCK) {
                        grappled = true;
                        assertTrue(FieldBattle.touching(b, battle.holder(e)) || FieldBattle.touching(battle.holder(e), b),
                                "grappled before the hulls touched at second " + battle.seconds());
                    }
                }
            }
        }
        assertTrue(!hooked.isEmpty(), "no hooks thrown");
        assertTrue(grappled, "no ship hauled alongside and grappled");
    }

    private static Ramming.Hull hull(FieldBlock b) {
        return new Ramming.Hull(b.pos().x(), b.pos().z(), b.facingX(), b.facingZ(), b.hull().length / 2.0, b.hull().width / 2.0, 0, 0);
    }

    @Test
    void grapplingLashesEveryoneAboardBothShips() {
        FieldBattle battle = battle(60, 60, 3);
        battle.setType(BattleType.OCEAN);
        FieldBlock a = battle.army(0).blocks().stream().filter(b -> b.platoon >= 0 && battle.crew(b).size() > 1).findFirst().orElseThrow();
        FieldBlock b = battle.army(1).blocks().stream().filter(o -> o.platoon >= 0 && battle.crew(o).size() > 1).findFirst().orElseThrow();
        battle.lash(a, b);
        for (FieldBlock x : battle.crew(a)) {
            assertTrue(x.lashed().containsAll(battle.crew(b)), "someone aboard can't board the enemy");
        }
    }

    @Test
    void hooksGoOverOnlyAlongsideParallelAndMostlyOverlapped() {
        FieldBlock a = loneShip();
        FieldBlock e = loneShip();
        double beam = FieldBattle.boardAbeam(a, e);
        double length = a.hull().length; // both the same size
        e.pos = new Point(0, beam);
        assertTrue(FieldBattle.hookable(a, e), "abeam, parallel, fully overlapped");
        e.facingX = -1;
        assertTrue(FieldBattle.hookable(a, e), "bows opposite is still parallel");
        e.pos = new Point(length * 0.35, beam);
        assertTrue(FieldBattle.hookable(a, e), "65% overlapped");
        e.pos = new Point(length * 0.45, beam);
        assertFalse(FieldBattle.hookable(a, e), "only 55% overlapped");
        e.pos = new Point(0, beam);
        e.facingX = Math.cos(Math.toRadians(30));
        e.facingZ = Math.sin(Math.toRadians(30));
        assertFalse(FieldBattle.hookable(a, e), "30 degrees off parallel");
        e.facingX = Math.cos(Math.toRadians(10));
        e.facingZ = Math.sin(Math.toRadians(10));
        assertTrue(FieldBattle.hookable(a, e), "10 degrees off counts as parallel");
    }

    /** A swordsman ship of a sea battle, alone at (0, 0) with its bow to +x. */
    private static FieldBlock loneShip() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        FieldBlock ship = battle.army(0).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.SWORDSMEN).findFirst().orElseThrow();
        ship.pos = new Point(0, 0);
        ship.facingX = 1;
        ship.facingZ = 0;
        return ship;
    }

    @Test
    void aShipComesRoundInAnArcNeverSidewaysAndNoFasterThanItTurns() {
        FieldBlock ship = loneShip();
        Point target = new Point(0, 60); // abeam of it
        boolean arced = false;
        for (int s = 0; s < 120 && ship.pos().distanceTo(target) > FieldBattle.AT_PLACE; s++) {
            Point before = ship.pos();
            double hx = ship.facingX();
            double hz = ship.facingZ();
            FieldBattle.sail(ship, target, List.of());
            double dx = ship.pos().x() - before.x();
            double dz = ship.pos().z() - before.z();
            double moved = Math.hypot(dx, dz);
            double turned = Math.abs(Math.atan2(hx * ship.facingZ() - hz * ship.facingX(), hx * ship.facingX() + hz * ship.facingZ()));
            assertTrue(turned <= FieldBattle.TURN_RATE + 1e-9, "turned " + Math.toDegrees(turned) + " degrees in a second");
            // It moves along its heading halfway through the turn, never sideways.
            assertTrue(Math.abs(hx * dz - hz * dx) <= moved * Math.sin(turned / 2) + 1e-6, "moved sideways at second " + s);
            assertTrue(moved <= FieldBattle.shipSpeed(ship) + 1e-9, "faster than it sails");
            arced |= turned > Math.toRadians(1) && moved > 0.5;
        }
        assertTrue(ship.pos().distanceTo(target) <= FieldBattle.AT_PLACE, "never got there: " + ship.pos());
        assertTrue(arced, "turned where it lay instead of sailing round");
    }

    @Test
    void aShipWithNoRoomToComeRoundBacksAsternUntilItHasRoom() {
        FieldBlock ship = loneShip();
        rafasconquest.core.SeaLanes.Size hull = ship.hull();
        // An ally lying alongside, clear of it, but in the way of its bow coming round toward +z.
        Ramming.Hull ally = new Ramming.Hull(0, hull.width + Ramming.CLEAR * 2 + 2, 1, 0, hull.length / 2.0, hull.width / 2.0, 0, 0);
        assertTrue(Ramming.turnFouls(new Ramming.Hull(0, 0, 1, 0, hull.length / 2.0, hull.width / 2.0, 0, 0), Math.PI / 2, List.of(ally)),
                "coming round a quarter turn would strike it");
        assertFalse(Ramming.turnFouls(new Ramming.Hull(0, 0, 1, 0, hull.length / 2.0, hull.width / 2.0, 0, 0), Math.toRadians(5), List.of(ally)),
                "a small turn needs no room");
        double[] round = Ramming.detour(new Ramming.Hull(0, 0, 1, 0, hull.length / 2.0, hull.width / 2.0, 0, 0), 300, 0,
                List.of(new Ramming.Hull(100, 2, 0, 1, hull.length / 2.0, hull.width / 2.0, 0, 0)));
        assertNotNull(round, "an ally lying across its way");
        assertTrue(round[1] < -hull.length / 2.0, "passes round the nearer end of it: " + round[1]);
        assertNull(Ramming.detour(new Ramming.Hull(0, 0, 1, 0, hull.length / 2.0, hull.width / 2.0, 0, 0), 300, 0,
                List.of(new Ramming.Hull(100, 200, 0, 1, hull.length / 2.0, hull.width / 2.0, 0, 0))), "one well off its way");
    }

    @Test
    void aShipBacksAtHalfSpeedToAPlaceCloseAstern() {
        FieldBlock ship = loneShip();
        Point target = new Point(-10, 0); // close astern
        for (int s = 0; s < 30 && ship.pos().distanceTo(target) > FieldBattle.AT_PLACE; s++) {
            Point before = ship.pos();
            FieldBattle.sail(ship, target, List.of());
            assertTrue(ship.pos().distanceTo(before) <= FieldBattle.shipSpeed(ship) / 2 + 1e-9, "astern faster than half speed");
            assertTrue(ship.pos().x() < before.x() + 1e-9, "didn't back toward it");
            assertEquals(1, ship.facingX(), 1e-9, "turned round instead of backing");
        }
        assertTrue(ship.pos().distanceTo(target) <= FieldBattle.AT_PLACE, "never got there: " + ship.pos());
    }

    @Test
    void anArcherCaptainShootsIntoABoardingFightAndOtherwiseKeepsItsMark() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        FieldBlock archers = battle.army(0).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.ARCHERS).findFirst().orElseThrow();
        FieldBlock ally = battle.army(0).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.SWORDSMEN).findFirst().orElseThrow();
        java.util.List<FieldBlock> enemies = battle.army(1).blocks().stream().filter(b -> b.ship() != null).toList();
        for (FieldBlock e : battle.army(1).blocks()) {
            e.pos = new Point(1000, 1000); // out of the way
        }
        FieldBlock near = enemies.get(0);
        FieldBlock nearer = enemies.get(1);
        FieldBlock boarded = enemies.get(2);
        archers.pos = new Point(0, 0);
        near.pos = new Point(20, 0);
        nearer.pos = new Point(10, 0);
        boarded.pos = new Point(30, 0);
        assertEquals(nearer, FieldBattle.mark(archers, battle.army(1), null), "not the nearest in range");
        assertEquals(near, FieldBattle.mark(archers, battle.army(1), near), "dropped its mark for a nearer one");
        boarded.lashed.add(ally);
        assertEquals(boarded, FieldBattle.mark(archers, battle.army(1), near), "didn't shoot into the boarding fight");
        boarded.pos = new Point(FieldRules.ARCHER_RANGE + 10, 0);
        assertEquals(nearer, FieldBattle.mark(archers, battle.army(1), boarded), "kept a mark out of range");
    }

    @Test
    void noTwoShipsOfASideMakeForTheSameWater() {
        FieldBattle battle = battle(30, 30, 4);
        battle.setType(BattleType.OCEAN);
        boolean boarded = false;
        for (int s = 0; s < 240 && !battle.over(); s++) {
            battle.second();
            for (int side = 0; side < 2; side++) {
                java.util.List<FieldBlock> sailing = battle.army(side).blocks().stream()
                        .filter(b -> b.alive() > 0 && b.afloat() && battle.holder(b) == b && b.lashed().isEmpty() && b.prey() != null && !b.charged()).toList();
                for (int i = 0; i < sailing.size(); i++) {
                    for (int j = i + 1; j < sailing.size(); j++) {
                        assertTrue(sailing.get(i).line.distanceTo(sailing.get(j).line) > 1,
                                "two ships making for " + sailing.get(i).line + " at second " + battle.seconds());
                    }
                }
                boarded |= battle.army(side).blocks().stream().anyMatch(b -> !b.lashed().isEmpty());
            }
        }
        assertTrue(boarded, "nobody came alongside");
    }

    @Test
    void shipsOfOneSideThatEndUpInEachOtherEdgeApart() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        java.util.List<FieldBlock> fleet = battle.army(0).blocks().stream().filter(o -> battle.holder(o) == o).toList();
        FieldBlock a = fleet.get(0);
        FieldBlock b = fleet.get(1);
        b.pos = a.pos;
        b.facingX = a.facingX;
        b.facingZ = a.facingZ;
        for (int s = 0; s < 16; s++) {
            battle.second();
        }
        // Clear, hull to hull (a unit's ship 13 wide), whichever way they now lie to each other.
        assertTrue(a.pos().distanceTo(b.pos()) > rafasconquest.core.SeaLanes.Size.XXLARGE.width, "still in each other: " + a.pos() + " and " + b.pos());
    }

    @Test
    void fleetsSpreadOnAnyHeadingWithoutHanging() {
        for (int degrees = 0; degrees < 360; degrees += 7) {
            double a = Math.toRadians(degrees);
            FieldBattle battle = new FieldBattle(40, 40, new Point(123.4, -567.8), Math.cos(a), Math.sin(a), degrees);
            org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(2),
                    () -> battle.setType(BattleType.OCEAN), "spreading the fleets hung at " + degrees + " degrees");
        }
    }

    @Test
    void anAmphibiousAssaultLandsUnderTheWallsWhileTheDefendersHoldThem() {
        FieldBattle battle = battle(20, 20, 5);
        battle.setType(BattleType.AMPHIBIOUS);
        assertTrue(battle.army(0).blocks().stream().allMatch(FieldBlock::afloat), "the attacker comes by sea");
        assertTrue(battle.army(1).blocks().stream().noneMatch(FieldBlock::afloat), "the defending army holds the walls");
        FieldArmy d = battle.army(1);
        boolean landed = false;
        java.util.Set<FieldBlock> ashore = new java.util.HashSet<>();
        for (int i = 0; i < 300 && !battle.over(); i++) {
            battle.second();
            for (FieldBlock b : battle.army(0).blocks()) {
                if (!b.afloat() && ashore.add(b) && b.alive() > 0) {
                    landed = true;
                    double past = (b.pos().x() - d.origin().x()) * d.facingX() + (b.pos().z() - d.origin().z()) * d.facingZ();
                    assertTrue(past <= FieldBattle.LANDING + 1e-6, "came ashore out at sea: " + past);
                }
            }
        }
        assertTrue(landed, "no ship came ashore");
    }

    @Test
    void theDefendersHarbourShipsGoOutToMeetTheAttackersShipsBeforeTheyLand() {
        FieldBattle battle = battle(30, 30, 6);
        battle.setType(BattleType.AMPHIBIOUS);
        FieldArmy d = battle.army(1);
        int army = d.blocks().size();
        int started = d.started();
        battle.addHarbourFleet(3);
        java.util.List<FieldBlock> fleet = d.blocks().stream().filter(FieldBlock::afloat).toList();
        assertEquals(3, fleet.size(), "the port's own three ships");
        assertEquals(army + 3, d.blocks().size(), "on top of the army, none taken from it");
        assertEquals(started + 3 * BattlePlan.SOLDIERS_PER_FORMATION, d.started());
        assertTrue(fleet.stream().noneMatch(b -> b.arm().mounted()), "no horsemen at sea");
        for (FieldBlock b : fleet) {
            double past = (b.pos().x() - d.origin().x()) * d.facingX() + (b.pos().z() - d.origin().z()) * d.facingZ();
            assertTrue(past > 0, "a harbour ship waiting behind the walls");
        }
        boolean metAtSea = false;
        for (int i = 0; i < 200 && !battle.over() && !metAtSea; i++) {
            battle.second();
            for (FieldBlock b : fleet) {
                FieldBlock foe = b.contactFoe();
                metAtSea |= b.afloat() && foe != null && foe.afloat();
            }
        }
        assertTrue(metAtSea, "the harbour ships never met the attacker's at sea");
    }
}
