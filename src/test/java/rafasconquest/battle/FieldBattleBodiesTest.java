package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Point;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** A watched battle, its soldiers walking and touching as bodies ({@link FieldBattle#bodiesTick}). */
class FieldBattleBodiesTest {
    private static final int SECONDS = 400;

    private static int fallen(FieldBattle battle) {
        int fallen = 0;
        for (int side = 0; side < 2; side++) {
            for (FieldBlock b : battle.army(side).blocks) {
                fallen += b.started() - b.alive();
            }
        }
        return fallen;
    }

    @Test
    void onlySoldiersWhoReachTheEnemyFight() {
        // The same 500-a-side battle as numbers (every soldier of a block in reach fights) and as bodies (only those
        // whose bodies reach an enemy's): the bodies fight on a narrower front, so fewer fall in the same time.
        FieldBattle numbers = new FieldBattle(25, 25, new Point(0, 0), 1, 0, 8);
        FieldBattle bodies = new FieldBattle(25, 25, new Point(0, 0), 1, 0, 8);
        long nanos = 0;
        for (int s = 0; s < SECONDS; s++) {
            if (!numbers.over()) {
                numbers.second();
            }
            if (!bodies.over()) {
                long t = System.nanoTime();
                for (int tick = 0; tick < 20; tick++) {
                    bodies.bodiesTick();
                }
                nanos += System.nanoTime() - t;
                bodies.second();
            }
        }
        int byNumbers = fallen(numbers);
        int byBodies = fallen(bodies);
        System.out.printf("[bodies] 1,000 soldiers, %d s: %d fell as numbers, %d as bodies; bodies %.3f ms a tick%n",
                SECONDS, byNumbers, byBodies, nanos / 1e6 / (SECONDS * 20.0));
        assertTrue(byBodies > 0, "the bodies never fought");
        assertTrue(byBodies < byNumbers, "a narrower front should kill slower: " + byBodies + " vs " + byNumbers);
    }
}
