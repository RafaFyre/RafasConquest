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
}
