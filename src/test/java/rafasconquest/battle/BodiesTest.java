package rafasconquest.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BodiesTest {
    @Test
    void enemiesCannotWalkThroughEachOther() {
        Bodies b = new Bodies();
        int a = b.add(0, Bodies.Kind.FOOT, 0, 0, 1, 0);
        int e = b.add(1, Bodies.Kind.FOOT, 4, 0, -1, 0);
        b.goal(a, 10, 0, 1, 0);
        b.goal(e, -6, 0, -1, 0);
        for (int s = 0; s < 40; s++) {
            b.step();
            assertTrue(b.x(e) - b.x(a) > 0.5f, "step " + s + ": they met and stopped, " + b.x(a) + " / " + b.x(e));
        }
    }

    @Test
    void friendsSqueezePastEachOther() {
        Bodies b = new Bodies();
        int a = b.add(0, Bodies.Kind.FOOT, 0, 0, 1, 0);
        int f = b.add(0, Bodies.Kind.FOOT, 4, 0.05f, -1, 0);
        b.goal(a, 8, 0, 1, 0);
        b.goal(f, -4, 0.05f, -1, 0);
        for (int s = 0; s < 100; s++) {
            b.step();
        }
        assertEquals(8, b.x(a), 0.5, "got by");
        assertEquals(-4, b.x(f), 0.5, "got by");
    }

    @Test
    void theHeavierBodyPushesTheLighterOneMore() {
        Bodies b = new Bodies();
        int horse = b.add(0, Bodies.Kind.HORSE, 0, 0, 1, 0);
        int man = b.add(1, Bodies.Kind.FOOT, 0.5f, 0, -1, 0); // overlapping, both standing at their places
        b.step();
        assertTrue(b.x(man) - 0.5f > 5 * -b.x(horse), "man moved " + (b.x(man) - 0.5f) + ", horse " + -b.x(horse));
    }

    @Test
    void aGallopingRiderKnocksAFootSoldierDownAndIsSlowed() {
        Bodies b = new Bodies();
        int rider = b.add(0, Bodies.Kind.HORSE, 0, 0, 1, 0);
        int man = b.add(1, Bodies.Kind.FOOT, 6, 0, -1, 0);
        b.goal(rider, 30, 0, 1, 0);
        b.velocity(rider, 11, 0);
        boolean knocked = false;
        for (int s = 0; s < 20 && !knocked; s++) {
            b.step();
            knocked = b.down(man);
        }
        assertTrue(knocked, "knocked down");
        // Two men walking into each other don't.
        Bodies walk = new Bodies();
        int m1 = walk.add(0, Bodies.Kind.FOOT, 0, 0, 1, 0);
        int m2 = walk.add(1, Bodies.Kind.FOOT, 3, 0, -1, 0);
        walk.goal(m1, 10, 0, 1, 0);
        walk.goal(m2, -7, 0, -1, 0);
        for (int s = 0; s < 40; s++) {
            walk.step();
            assertTrue(!walk.down(m1) && !walk.down(m2), "nobody knocked down at a walk");
        }
    }

    @Test
    void blocksThatMeetFight() {
        Bodies b = new Bodies();
        line(b, 0, 0, 5, 4, 1);
        line(b, 1, 3.2f, 5, 4, -1);
        for (int s = 0; s < 200; s++) {
            b.step();
        }
        assertTrue(b.fallen(0) > 0 && b.fallen(1) > 0, "they fought: " + b.fallen(0) + " / " + b.fallen(1));
    }

    /**
     * Two armies of 5,000 (each 100 files by 50 ranks, a tenth of each riders on its right) march into each other.
     * Prints the milliseconds a step takes; fails only if far over a tick, or if the lines pass through each other.
     */
    @Test
    void tenThousandSoldiersClashWithinATick() {
        Bodies b = new Bodies();
        line(b, 0, 0, 100, 50, 1);
        line(b, 1, 6, 100, 50, -1);
        for (int s = 0; s < 100; s++) { // warm up the JIT
            b.step();
        }
        int steps = 400;
        long worst = 0;
        long start = System.nanoTime();
        for (int s = 0; s < steps; s++) {
            long t = System.nanoTime();
            b.step();
            worst = Math.max(worst, System.nanoTime() - t);
        }
        double ms = (System.nanoTime() - start) / 1e6 / steps;
        double front0 = -Double.MAX_VALUE;
        double front1 = Double.MAX_VALUE;
        double mean0 = 0;
        double mean1 = 0;
        for (int i = 0; i < b.size(); i++) {
            if (b.alive(i)) {
                if (b.side(i) == 0) {
                    mean0 += b.x(i);
                } else {
                    mean1 += b.x(i);
                }
            }
        }
        mean0 /= 5000 - b.fallen(0);
        mean1 /= 5000 - b.fallen(1);
        System.out.printf("[bodies] 10,000 soldiers: %.2f ms a step on average, worst %.2f ms; fallen %d / %d; middles x %.1f / %.1f%n",
                ms, worst / 1e6, b.fallen(0), b.fallen(1), mean0, mean1);
        assertTrue(mean0 < mean1, "the armies passed through each other");
        assertTrue(b.fallen(0) + b.fallen(1) > 0, "nobody fought");
        assertTrue(ms < 50, "a step took " + ms + " ms, over a whole tick");
    }

    /**
     * A block of {@code files} by {@code ranks} for {@code side}, its front rank at x = {@code front}, facing
     * {@code facing} (+1 east, -1 west), each soldier marching 40 blocks on; its right-most tenth of files are riders.
     */
    private static void line(Bodies b, int side, float front, int files, int ranks, int facing) {
        int riders = files / 10;
        for (int f = 0; f < files; f++) {
            boolean horse = riders > 0 && f >= files - riders;
            for (int r = 0; r < ranks; r++) {
                float x = front - facing * r * 1.75f;
                float z = f * (horse ? 1.6f : 1f);
                int i = b.add(side, horse ? Bodies.Kind.HORSE : Bodies.Kind.FOOT, x, z, facing, 0);
                b.goal(i, x + facing * 40, z, facing, 0);
            }
        }
    }
}
