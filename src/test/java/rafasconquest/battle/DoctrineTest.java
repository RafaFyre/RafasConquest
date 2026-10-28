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
        assertTrue(army.commander().pos().x() < -30 - BattlePlan.SPEAR_BACK + 1, "the general waits at the wedge's tip");
        List<String> events = fight(battle);
        int first = events.indexOf("0:leads the first half of the wedge at the enemy line");
        assertTrue(first >= 0 || events.stream().anyMatch(e -> e.startsWith("0:leads the first half")), events.toString());
        assertTrue(events.stream().anyMatch(e -> e.startsWith("0:sends the second half")), events.toString());
    }

    @Test
    void theBaitDrawsTheEnemyInAndThePincerSprings() {
        int springs = 0;
        int baits = 0;
        for (long seed = 1; seed <= 4; seed++) {
            List<String> events = fight(battle(25, CHARGE_BAIT, List.of(), seed));
            baits += events.stream().anyMatch(e -> e.startsWith("0:has the bait fall back")) ? 1 : 0;
            springs += events.stream().anyMatch(e -> e.startsWith("0:springs the pincer")) ? 1 : 0;
        }
        assertEquals(4, baits);
        assertTrue(springs >= 3, "the pincer sprang in " + springs + " of 4");
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
