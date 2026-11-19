package rafasconquest.content;

import net.minecraft.core.Direction;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CannonAimTest {
    @Test
    void atRestItPointsTheWayTheBlockFaces() {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            Vector3d bore = CannonAim.bore(CannonAim.barrel(CannonAim.carriage(null, CannonAim.facing(d), 0), 0));
            assertEquals(d.getStepX(), bore.x, 1e-9);
            assertEquals(d.getStepZ(), bore.z, 1e-9);
        }
    }

    @Test
    void aLayReadsBackFromItsBoreOnATurnedShip() {
        Quaterniond ship = new Quaterniond().rotateY(0.7).rotateX(0.05);
        double facing = CannonAim.facing(Direction.EAST);
        Vector3d bore = CannonAim.bore(CannonAim.barrel(CannonAim.carriage(ship, facing, 20), 10));
        double[] lay = CannonAim.layFor(bore, ship, facing);
        assertEquals(20, lay[0], 1e-6);
        assertEquals(10, lay[1], 1e-6);
    }

    @Test
    void aLayOutsideTheArcIsHeldAtItsEdge() {
        double[] lay = CannonAim.layFor(new Vector3d(1, 0, 0), null, CannonAim.facing(Direction.SOUTH)); // 90 degrees off
        assertEquals(CannonAim.ARC, Math.abs(lay[0]), 1e-9);
    }

    @Test
    void aBallLaidByTheBallisticSolutionFallsOnItsMark() {
        Vector3d from = new Vector3d(0, 5, 0);
        Vector3d to = new Vector3d(120, 2, 40);
        Vector3d v = CannonAim.ballistic(from, to).mul(CannonAim.SPEED);
        Vector3d p = new Vector3d(from);
        double nearest = Double.MAX_VALUE;
        for (int t = 0; t < 200; t++) {
            p.add(v);
            v.y -= CannonAim.GRAVITY;
            nearest = Math.min(nearest, p.distance(to));
        }
        assertTrue(nearest < CannonAim.SPEED, "missed by " + nearest);
    }

    /** How near a ball fired along {@code dir}, flown as CannonShots flies it (move, then fall), passes {@code to} at tick t. */
    private static double missBy(Vector3d from, Vector3d dir, java.util.function.IntFunction<Vector3d> to) {
        Vector3d v = new Vector3d(dir).mul(CannonAim.SPEED);
        Vector3d p = new Vector3d(from);
        double nearest = Double.MAX_VALUE;
        for (int t = 1; t < 200; t++) {
            Vector3d last = new Vector3d(p);
            p.add(v);
            v.y -= CannonAim.GRAVITY;
            Vector3d mark = to.apply(t);
            // nearest the mark along this tick's step, not only at its ends
            Vector3d step = new Vector3d(p).sub(last);
            double k = Math.max(0, Math.min(1, new Vector3d(mark).sub(last).dot(step) / step.lengthSquared()));
            nearest = Math.min(nearest, new Vector3d(last).fma(k, step).distance(mark));
        }
        return nearest;
    }

    @Test
    void aBallFallsOnAShipAtGunRangeWithinABlock() {
        Vector3d from = new Vector3d(0, 66, 0);
        for (double range : new double[]{40, 90, 128, 160}) {
            Vector3d to = new Vector3d(range * 0.6, 64, range * 0.8);
            double miss = missBy(from, CannonAim.ballistic(from, to), t -> to);
            assertTrue(miss < 1, "missed at " + range + " by " + miss);
        }
    }

    @Test
    void aSailingShipIsLedSoTheBallMeetsIt() {
        Vector3d from = new Vector3d(0, 66, 0);
        Vector3d to = new Vector3d(0, 64, 128);
        Vector3d way = new Vector3d(0.15, 0, 0); // a gunship's way across the line of fire, blocks a tick
        Vector3d aim = CannonAim.lead(from, to, way);
        double miss = missBy(from, CannonAim.ballistic(from, aim), t -> new Vector3d(to).fma(t, way));
        assertTrue(miss < 1.5, "missed the sailing ship by " + miss);
        double unled = missBy(from, CannonAim.ballistic(from, to), t -> new Vector3d(to).fma(t, way));
        assertTrue(unled > miss, "leading made it no better");
    }

    @Test
    void aMarkPastTheArcIsNotBorne() {
        assertTrue(CannonAim.bears(new double[]{10, 5}));
        assertTrue(!CannonAim.bears(CannonAim.layFor(new Vector3d(1, 0, 0), null, CannonAim.facing(Direction.SOUTH))));
    }
}
