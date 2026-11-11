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
    void archersClimbAHillWithTheirWholePlatoon() {
        FieldBattle battle = battle();
        FieldArmy army = battle.army(0);
        int platoon = army.blocks().stream().filter(b -> b.arm() == Arm.ARCHERS).findFirst().orElseThrow().platoon();
        java.util.List<FieldBlock> archers = army.blocks().stream().filter(b -> b.platoon() == platoon).toList();
        double mx = archers.stream().mapToDouble(b -> b.line().x()).average().orElseThrow();
        double mz = archers.stream().mapToDouble(b -> b.line().z()).average().orElseThrow();
        // A knoll behind them, further back than the front may go to reach it.
        Point top = new Point(mx - army.facingX() * 45, mz - army.facingZ() * 45);
        GroundPlan.Ground land = new Land() {
            @Override
            public double height(double x, double z) {
                return 64 + Math.max(0, 20 - Math.hypot(x - top.x(), z - top.z()));
            }
        };
        double before = archers.stream().mapToDouble(b -> land.height(b.line().x(), b.line().z())).average().orElseThrow();
        java.util.Map<FieldBlock, Point> was = new java.util.HashMap<>();
        army.blocks().forEach(b -> was.put(b, b.line()));
        GroundPlan.deploy(battle, land);
        double after = archers.stream().mapToDouble(b -> land.height(b.line().x(), b.line().z())).average().orElseThrow();
        assertTrue(after > before + 3, "the archers stay low: " + before + " -> " + after);
        // Their whole platoon climbed with them, every block moved alike; so did every other platoon's.
        for (FieldBlock b : battle.army(0).blocks()) {
            if (b.platoon() < 0) {
                continue;
            }
            FieldBlock first = battle.army(0).blocks().stream().filter(o -> o.platoon() == b.platoon()).findFirst().orElseThrow();
            Point moved = new Point(b.line().x() - was.get(b).x(), b.line().z() - was.get(b).z());
            Point firstMoved = new Point(first.line().x() - was.get(first).x(), first.line().z() - was.get(first).z());
            assertEquals(0, moved.distanceTo(firstMoved), 1e-6, "platoon " + b.platoon() + " came apart");
        }
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
