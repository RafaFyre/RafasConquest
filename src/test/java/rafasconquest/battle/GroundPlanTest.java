package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroundPlanTest {
    /** Flat dry land, with whatever a test adds. */
    private static class Land implements GroundPlan.Ground {
        @Override
        public double height(double x, double z) {
            return 64;
        }

        @Override
        public boolean water(double x, double z) {
            return false;
        }

        @Override
        public boolean forest(double x, double z) {
            return false;
        }
    }

    private static FieldBattle battle() {
        return new FieldBattle(100, 100, new Point(0, 0), 1, 0, 3);
    }

    @Test
    void armiesDeployAroundALake() {
        FieldBattle battle = battle();
        Point lake = battle.army(0).front[battle.army(0).front.length / 2].line();
        GroundPlan.Ground land = new Land() {
            @Override
            public boolean water(double x, double z) {
                return Math.hypot(x - lake.x(), z - lake.z()) < 12;
            }
        };
        GroundPlan.deploy(battle, land);
        for (FieldBlock b : battle.army(0).blocks()) {
            if (b.duty() != FieldBlock.Duty.GENERAL && b.duty() != FieldBlock.Duty.GUARD && b.duty() != FieldBlock.Duty.BEARER) {
                assertFalse(land.water(b.line().x(), b.line().z()), "a block stands in the lake: " + b.id());
            }
        }
    }

    @Test
    void archersClimbAHill() {
        FieldBattle battle = battle();
        FieldBlock archers = battle.army(0).blocks().stream().filter(b -> b.arm() == Arm.ARCHERS).findFirst().orElseThrow();
        Point near = archers.line();
        Point top = near.plus(-battle.army(0).facingZ() * 25, battle.army(0).facingX() * 25); // off to one side
        GroundPlan.Ground land = new Land() {
            @Override
            public double height(double x, double z) {
                return 64 + Math.max(0, 20 - Math.hypot(x - top.x(), z - top.z()) / 2);
            }
        };
        double before = land.height(near.x(), near.z());
        GroundPlan.deploy(battle, land);
        assertTrue(land.height(archers.line().x(), archers.line().z()) > before + 5, "the archers stay low");
    }

    @Test
    void aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes() {
        FieldBattle battle = battle();
        FieldArmy army = battle.army(0);
        FieldBlock leftEnd = null;
        for (FieldBlock f : army.front) {
            if (f.wing() == FieldArmy.LEFT && (leftEnd == null
                    || BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), f.line()).x()
                    < BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), leftEnd.line()).x())) {
                leftEnd = f;
            }
        }
        Point end = leftEnd.line();
        Point wood = end.plus(army.facingZ() * 40, -army.facingX() * 40); // off the left end
        GroundPlan.Ground land = new Land() {
            @Override
            public boolean forest(double x, double z) {
                return Math.hypot(x - wood.x(), z - wood.z()) < 15;
            }
        };
        int hidden = GroundPlan.deploy(battle, land);
        assertTrue(hidden > 0, "nobody hid in the wood");
        FieldBlock ambush = army.blocks().stream().filter(b -> b.task() == FieldBlock.Task.AMBUSH).findFirst().orElseThrow();
        assertTrue(land.forest(ambush.line().x(), ambush.line().z()), "the ambush isn't in the wood");
        Point hiding = ambush.line();
        FieldBlock target = null;
        for (int i = 0; i < 900 && target == null; i++) {
            battle.second();
            if (ambush.strikeAt() == null) {
                assertEquals(0, ambush.line().distanceTo(hiding), 1e-6, "the ambush moved before it struck");
            }
            target = ambush.strikeAt();
        }
        assertTrue(target == null || target.side() == 1, "it struck its own side");
    }
}
