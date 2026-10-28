package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.Doctrine.Formation;
import rafasconquest.battle.Doctrine.Strategy;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.character.GameCharacter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoctrineTest {
    private static final List<Doctrine> SPEAR_CHARGE = List.of(Doctrine.SPEAR_AND_SHIELD, Doctrine.CHARGE_AND_DECAPITATE);
    private static final List<Doctrine> SPEAR_BAIT = List.of(Doctrine.SPEAR_AND_SHIELD, Doctrine.BAIT_AND_PINCER);
    private static final List<Doctrine> CHARGE_BAIT = List.of(Doctrine.CHARGE_AND_DECAPITATE, Doctrine.BAIT_AND_PINCER);

    private static FieldBattle battle(int blocks, List<Doctrine> first, List<Doctrine> second, long seed) {
        return new FieldBattle(blocks, blocks, new Point(0, 0), 1, 0, seed, WarPlan.of(first), WarPlan.of(second));
    }

    /** Runs a battle to its end; returns every event's text, side first ("1:..."). */
    private static List<String> fight(FieldBattle battle) {
        List<String> events = new ArrayList<>();
        while (!battle.over() && battle.seconds() < BattlePlan.TIME_LIMIT_SECONDS) {
            battle.second();
            battle.takeEvents().forEach(e -> events.add(e.side() + ":" + e.text()));
            battle.takeCalls();
        }
        return events;
    }

    @Test
    void aGeneralFollowsTwoOfTheirFactionsDoctrinesAndPlansWithEverythingInThem() {
        Set<List<Doctrine>> seen = new HashSet<>();
        Random random = new Random(1);
        for (int i = 0; i < 60; i++) {
            GameCharacter general = GameCharacter.general(List.of(Doctrine.values()), random);
            assertEquals(GameCharacter.Role.GENERAL, general.role());
            assertEquals(2, general.doctrines().size());
            assertEquals(2, new HashSet<>(general.doctrines()).size());
            seen.add(general.doctrines());
        }
        assertEquals(3, seen.size(), "every pair of the three doctrines comes up");

        WarPlan plan = WarPlan.of(SPEAR_BAIT);
        assertEquals(Set.of(Formation.GREAT_SPEAR, Formation.GREAT_SHIELD), plan.formations());
        assertEquals(Set.of(Strategy.DECAPITATE, Strategy.MASS_CASUALTIES, Strategy.CAVALRY_FLANK, Strategy.BAIT, Strategy.PINCER),
                plan.strategies());
    }

    @Test
    void eachGeneralRidesWithAGuardOfTwentyAndTheirArmyRetreatsWhenTheyFall() {
        FieldBattle battle = new FieldBattle(25, 25, new Point(0, 0), 1, 0, 3);
        for (int side = 0; side < 2; side++) {
            FieldArmy army = battle.army(side);
            assertEquals(Arm.CAVALRY, army.commander().arm());
            assertEquals(1, army.commander().started());
            assertEquals(Arm.CAVALRY, army.guard().arm());
            assertEquals(20, army.guard().started());
            assertEquals(Duty.GUARD, army.guard().duty());
        }
        battle.run(20);
        FieldBlock general = battle.army(1).commander();
        battle.sync(general, general.pos(), new float[]{0});
        battle.second();
        assertTrue(battle.over());
        assertEquals(0, battle.outcome().winner());
        for (FieldBlock block : battle.army(1).blocks()) {
            assertEquals(Duty.FLEEING, block.duty());
        }
    }

    @Test
    void aGreatShieldStandsFifteenRanksDeepBehindShields() {
        List<BattlePlan.Slot> slots = BattlePlan.deployment(100, true, false);
        List<BattlePlan.Slot> infantry = slots.stream().filter(s -> s.arm().infantry()).toList();
        assertTrue(infantry.stream().allMatch(s -> s.arm() == Arm.SHIELDWALL && s.files() == BattlePlan.SHIELD_FILES));
        int files = (int) infantry.stream().filter(s -> s.line() == 0).count();
        for (int file = 0; file < files; file++) {
            int f = file;
            List<BattlePlan.Slot> stack = infantry.stream().filter(s -> s.file() == f && s.line() < BattlePlan.SHIELD_STACK).toList();
            assertEquals(BattlePlan.SHIELD_STACK, stack.size(), "three blocks in every front file");
            int ranks = BattlePlan.SHIELD_STACK * BattlePlan.SOLDIERS_PER_FORMATION / BattlePlan.SHIELD_FILES;
            assertEquals(15, ranks);
            assertEquals(BattlePlan.SHIELD_BLOCK_DEPTH, stack.get(1).back() - stack.get(0).back(), 1e-9, "closed up one behind the other");
        }
        FieldBattle battle = battle(100, SPEAR_BAIT, List.of(), 1);
        FieldBlock front = battle.army(0).blocks().stream().filter(b -> b.duty() == Duty.FRONT).findFirst().orElseThrow();
        assertEquals(BattlePlan.SHIELD_FILES, front.files());
    }

    @Test
    void aGreatSpearWaitsInOneWedgeFarBehindAndGathersTwiceTheMomentum() {
        FieldBattle battle = battle(50, SPEAR_BAIT, List.of(), 2);
        FieldArmy army = battle.army(0);
        List<FieldBlock> cavalry = army.blocks().stream()
                .filter(b -> b.arm() == Arm.CAVALRY && b.duty() == Duty.SUPPORT).toList();
        assertFalse(cavalry.isEmpty());
        for (FieldBlock c : cavalry) {
            assertTrue(c.pos().x() <= -30 - BattlePlan.SPEAR_BACK + 1, "far behind the front");
        }
        double most = 0;
        while (!battle.over() && battle.seconds() < 300) {
            battle.second();
            for (FieldBlock c : cavalry) {
                most = Math.max(most, c.momentum());
            }
        }
        assertTrue(most > 1.5, "more than a plain charge's full momentum: " + most);
    }

    @Test
    void theHalvesOfAnUnrelentingChargeTakeTurnsAndTheGeneralLeadsTheFirst() {
        FieldBattle battle = battle(25, CHARGE_BAIT, List.of(), 4);
        FieldArmy army = battle.army(0);
        FieldBlock general = army.commander();
        FieldBlock guard = army.guard();
        assertTrue(general.inWedge() && guard.inWedge());
        assertTrue(guard.pos().x() > general.pos().x(), "the guard rides at the wedge's tip, ahead of the general");
        assertEquals(guard.pos().z(), general.pos().z(), 1e-9, "the general straight behind it");
        List<String> events = fight(battle);
        int first = events.indexOf("0:leads the first half of the wedge at the enemy line");
        assertTrue(first >= 0 || events.stream().anyMatch(e -> e.startsWith("0:leads the first half")), events.toString());
        assertTrue(events.stream().anyMatch(e -> e.startsWith("0:sends the second half")), events.toString());
    }

    @Test
    void aGeneralLeadingTheChargeAndTheirGuardSpendMomentumAtHalfTheRate() {
        double leaderLoss = 0;
        double riderLoss = 0;
        for (long seed = 1; seed <= 4; seed++) {
            FieldBattle battle = battle(25, CHARGE_BAIT, List.of(), seed);
            FieldArmy army = battle.army(0);
            assertTrue(FieldBattle.leadsTheCharge(army.commander()) && FieldBattle.leadsTheCharge(army.guard()));
            List<FieldBlock> cavalry = army.blocks().stream().filter(b -> b.arm() == Arm.CAVALRY).toList();
            while (!battle.over() && battle.seconds() < BattlePlan.TIME_LIMIT_SECONDS) {
                double[] before = cavalry.stream().mapToDouble(FieldBlock::momentum).toArray();
                battle.second();
                for (int i = 0; i < cavalry.size(); i++) {
                    FieldBlock c = cavalry.get(i);
                    FieldBlock foe = c.contactFoe();
                    if (foe == null || foe.braced() || !foe.arm().infantry() || c.alive() == 0 || before[i] < 0.5) {
                        continue; // only full losses to infantry not braced
                    }
                    double lost = before[i] - c.momentum();
                    if (FieldBattle.leadsTheCharge(c)) {
                        leaderLoss = Math.max(leaderLoss, lost);
                    } else {
                        riderLoss = Math.max(riderLoss, lost);
                    }
                }
            }
        }
        assertEquals(FieldRules.MOMENTUM_LOSS_INFANTRY, riderLoss, 1e-9);
        assertEquals(FieldRules.MOMENTUM_LOSS_INFANTRY * FieldRules.LEADER_MOMENTUM_LOSS, leaderLoss, 1e-9);
        assertFalse(FieldBattle.leadsTheCharge(battle(25, List.of(), List.of(), 1).army(0).guard()), "only in a charge the general leads");
    }

    @Test
    void theGuardMeetsWhateverComesForTheGeneralAndTheGeneralNeverRidesOnAlone() {
        int metThreats = 0;
        int alone = 0;
        for (long seed = 1; seed <= 4; seed++) {
            // Spear and Shield sends its wedge at the general; the other general leads an unrelenting charge.
            FieldBattle battle = battle(100, CHARGE_BAIT, SPEAR_BAIT, seed);
            FieldBlock general = battle.army(0).commander();
            FieldBlock guard = battle.army(0).guard();
            boolean wasAlone = false;
            while (!battle.over() && battle.seconds() < BattlePlan.TIME_LIMIT_SECONDS) {
                battle.second();
                metThreats += (int) battle.takeEvents().stream()
                        .filter(e -> e.side() == 0 && e.text().startsWith("has their guard ride out")).count();
                battle.takeCalls();
                // A guard cut down mid-charge leaves the general alone until their next order, a second later.
                boolean isAlone = general.alive() > 0 && general.approaching() && !(guard.approaching() && guard.alive() > 0);
                if (isAlone && wasAlone) {
                    alone++;
                }
                wasAlone = isAlone;
            }
        }
        assertTrue(metThreats > 0, "the guard rode out at a threat to the general");
        assertEquals(0, alone, "the general rode on in the charge without the guard ahead");
    }

    @Test
    void theBaitDrawsTheEnemyInAndThePincerSprings() {
        int springs = 0;
        int baits = 0;
        for (long seed = 1; seed <= 4; seed++) {
            List<String> events = fight(battle(25, CHARGE_BAIT, List.of(), seed));
            baits += events.stream().anyMatch(e -> e.startsWith("0:has a bait fall back")) ? 1 : 0;
            springs += events.stream().anyMatch(e -> e.startsWith("0:springs the pincer")) ? 1 : 0;
        }
        assertEquals(4, baits);
        assertTrue(springs >= 3, "the pincer sprang in " + springs + " of 4");
    }

    @Test
    void anArmyOverThreeHundredFightsInWingsEachWithItsOwnBait() {
        FieldBattle small = battle(15, CHARGE_BAIT, List.of(), 1);
        assertFalse(small.army(0).winged());
        FieldBattle big = battle(100, CHARGE_BAIT, List.of(), 1);
        FieldArmy army = big.army(0);
        assertTrue(army.winged());
        Set<Integer> wings = new HashSet<>();
        army.blocks().stream().filter(b -> b.duty() == Duty.FRONT).forEach(b -> wings.add(b.wing()));
        assertEquals(Set.of(FieldArmy.LEFT, FieldArmy.CENTRE, FieldArmy.RIGHT), wings);
        List<String> events = fight(big);
        for (String wing : List.of("on the left wing", "in the centre", "on the right wing")) {
            assertTrue(events.stream().anyMatch(e -> e.startsWith("0:has a bait fall back " + wing)), wing + ": " + events);
        }
    }

    @Test
    void anUnrelentingChargeTakesHalfTheCavalryAndTheRestWaitsOnTheWingsBacklines() {
        FieldBattle battle = battle(100, CHARGE_BAIT, List.of(), 1);
        FieldArmy army = battle.army(0);
        List<FieldBlock> cavalry = army.blocks().stream()
                .filter(b -> b.arm() == Arm.CAVALRY && b.duty() == Duty.SUPPORT).toList();
        List<FieldBlock> onWings = cavalry.stream().filter(b -> !b.inWedge()).toList();
        long left = onWings.stream().filter(b -> b.wing() == FieldArmy.LEFT && b.pos().z() < -20).count();
        long right = onWings.stream().filter(b -> b.wing() == FieldArmy.RIGHT && b.pos().z() > 20).count();
        assertEquals(cavalry.size() / 2, onWings.size());
        assertEquals(onWings.size(), left + right, "out on the wings");
        assertTrue(Math.abs(left - right) <= 1, left + " left, " + right + " right");
    }

    @Test
    void cavalryThatHasChargedKeepsRidingAlongItsBackline() {
        FieldBattle battle = new FieldBattle(100, 100, new Point(0, 0), 1, 0, 2);
        List<FieldBlock> cavalry = battle.army(0).blocks().stream()
                .filter(b -> b.arm() == Arm.CAVALRY && b.duty() == Duty.SUPPORT).toList();
        int riding = 0;
        int waiting = 0;
        while (!battle.over() && battle.seconds() < 400) {
            battle.second();
            for (FieldBlock c : cavalry) {
                if (c.alive() > 0 && c.duty() == Duty.SUPPORT && c.hasCharged() && !c.charged() && !c.approaching()) {
                    double moved = c.pos().distanceTo(c.line());
                    if (moved > 1) {
                        riding++;
                    } else {
                        waiting++;
                    }
                }
            }
        }
        assertTrue(riding > 0, "cavalry riding its backline");
        assertTrue(riding > waiting * 3, riding + " seconds riding to " + waiting + " standing");
    }

    @Test
    void aWingThatOverlapsTheEnemysLineWheelsOntoItsExposedFlank() {
        // 2000 stand 20 files wide, 800 only 10: the bigger army's wings reach past the smaller one's line.
        FieldBattle battle = new FieldBattle(100, 40, new Point(0, 0), 1, 0, 1);
        List<String> events = fight(battle);
        assertTrue(events.stream().anyMatch(e -> e.startsWith("0:wheels the left wing onto")), events.toString());
        assertTrue(events.stream().anyMatch(e -> e.startsWith("0:wheels the right wing onto")), events.toString());
        assertEquals(0, battle.outcome().winner());
    }

    @Test
    void blocksPlacedByHandBeforeTheBattleKeepTheirPlaces() {
        FieldBattle battle = new FieldBattle(60, 60, new Point(0, 0), 1, 0, 1);
        FieldArmy army = battle.army(0);
        FieldBlock waiting = army.blocks().stream().filter(b -> b.duty() == Duty.LINE).findFirst().orElseThrow();
        FieldBlock front = army.blocks().stream().filter(b -> b.duty() == Duty.FRONT && b.file() == waiting.file()).findFirst().orElseThrow();
        FieldBlock general = army.commander();
        // Ten blocks further back and five to the side of where it stood; the general far out on the left.
        Point waitingAt = waiting.line().plus(-10, 5);
        battle.place(waiting, waitingAt);
        Point generalAt = general.line().plus(-15, -30);
        battle.place(general, generalAt);
        Point offset = BattlePlan.offset(front.line(), army.facingX(), army.facingZ(), waitingAt);
        battle.run(3);
        assertTrue(battle.begun());
        if (waiting.duty() == Duty.LINE) {
            Point expected = BattlePlan.place(front.line(), army.facingX(), army.facingZ(), offset.x(), offset.z());
            assertEquals(0, expected.distanceTo(waiting.line()), 1e-6, "keeps its place behind its file's front block");
        }
        assertTrue(general.line().distanceTo(generalAt) < 5, "the general keeps their place: " + general.line());
        assertThrows(IllegalStateException.class, () -> battle.place(waiting, new Point(0, 0)));
        Point p = new Point(3, 4);
        Point back = BattlePlan.offset(p, 0.6, 0.8, BattlePlan.place(p, 0.6, 0.8, 7, -2));
        assertEquals(7, back.x(), 1e-9);
        assertEquals(-2, back.z(), 1e-9);
    }

    @Test
    void infantrySetsOnStoppedCavalry() {
        boolean hunted = false;
        for (long seed = 1; seed <= 3 && !hunted; seed++) {
            List<String> events = fight(new FieldBattle(25, 25, new Point(0, 0), 1, 0, seed));
            hunted = events.stream().anyMatch(e -> e.contains("sends infantry at stopped enemy cavalry"));
        }
        assertTrue(hunted);
    }

    @Test
    void cavalryFlankingRidesAroundToTheSideOfItsPrey() {
        boolean around = false;
        for (long seed = 1; seed <= 3 && !around; seed++) {
            List<String> events = fight(battle(25, SPEAR_CHARGE, List.of(), seed));
            around = events.stream().anyMatch(e -> e.contains("around its flank"));
        }
        assertTrue(around);
    }

    @Test
    void everyPairingOfDoctrinesFightsToAnEnd() {
        List<List<Doctrine>> plans = List.of(List.of(), SPEAR_CHARGE, SPEAR_BAIT, CHARGE_BAIT);
        for (List<Doctrine> a : plans) {
            for (List<Doctrine> b : plans) {
                FieldBattle battle = battle(30, a, b, 7);
                fight(battle);
                assertNotNull(battle.outcome(), a + " v " + b);
            }
        }
    }
}
