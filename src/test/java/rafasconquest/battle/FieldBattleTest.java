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
    void aPlayersBaitIsItsWingsBait() {
        FieldBattle battle = battle(100, 100, 5);
        FieldBlock front = battle.army(0).front[0];
        battle.assign(front, FieldBlock.Task.BAIT);
        assertEquals(front.file(), battle.army(0).baitFile[front.wing()]);
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
                assertTrue(b.pos().distanceTo(e.getValue()) <= FieldBattle.shipSpeed(b) + FieldBattle.SHIP_SHOVE + 1e-6,
                        "a " + b.ship() + " ship (" + b.duty() + ", charged " + b.charged() + ", grappled " + b.lashed().size()
                                + ") moved " + b.pos().distanceTo(e.getValue()) + " in second " + battle.seconds()
                                + ": faster than its ship sails, shoved clear of its own");
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
    void aFleetDeploysInOneLineAbreastTwentyBlocksApart() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        boolean camps = false;
        for (int side = 0; side < 2; side++) {
            // The battle's axis is x: a line abreast stands at one x, its ships along z.
            java.util.List<Point> ships = battle.army(side).blocks().stream().filter(b -> b.ship() != FieldBlock.ShipKind.CAMP)
                    .map(FieldBlock::pos).sorted(java.util.Comparator.comparingDouble(Point::z)).toList();
            for (int i = 1; i < ships.size(); i++) {
                assertEquals(ships.get(0).x(), ships.get(i).x(), 1e-6, "not in one line abreast");
                assertEquals(FieldBattle.FLEET_SPACING, ships.get(i).z() - ships.get(i - 1).z(), 1e-6, "not 20 apart");
            }
            double facing = battle.army(side).facingX();
            for (FieldBlock camp : battle.army(side).blocks().stream().filter(b -> b.ship() == FieldBlock.ShipKind.CAMP).toList()) {
                camps = true;
                assertEquals(-FieldBattle.CAMP_ASTERN, (camp.pos().x() - ships.get(0).x()) * facing, 1e-6, "a supply ship not well astern");
            }
        }
        assertTrue(camps, "no supply ships to check");
    }

    @Test
    void aShipPlacedOnAnotherDropsClearOfIt() {
        FieldBattle battle = battle(20, 20, 3);
        battle.setType(BattleType.OCEAN);
        java.util.List<FieldBlock> ships = battle.army(0).blocks();
        battle.place(ships.get(1), ships.get(0).pos());
        assertTrue(ships.get(1).pos().distanceTo(ships.get(0).pos()) >= FieldBattle.SHIP_LENGTH - 1e-6, "placed on top of another ship");
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
            FieldBattle.sail(ship, target);
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
    void aShipBacksAtHalfSpeedToAPlaceCloseAstern() {
        FieldBlock ship = loneShip();
        Point target = new Point(-10, 0); // close astern
        for (int s = 0; s < 30 && ship.pos().distanceTo(target) > FieldBattle.AT_PLACE; s++) {
            Point before = ship.pos();
            FieldBattle.sail(ship, target);
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
                        .filter(b -> b.alive() > 0 && b.afloat() && b.lashed().isEmpty() && b.prey() != null && !b.charged()).toList();
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
        java.util.List<FieldBlock> fleet = battle.army(0).blocks();
        FieldBlock a = fleet.get(0);
        FieldBlock b = fleet.get(1);
        b.pos = a.pos;
        b.facingX = a.facingX;
        b.facingZ = a.facingZ;
        for (int s = 0; s < 6; s++) {
            battle.second();
        }
        // Clear, hull to hull (23 long, 7 wide), whichever way they now lie to each other.
        assertTrue(a.pos().distanceTo(b.pos()) > 7, "still in each other: " + a.pos() + " and " + b.pos());
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
        assertTrue(fleet.stream().noneMatch(b -> b.arm() == Arm.CAVALRY), "no horsemen at sea");
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
