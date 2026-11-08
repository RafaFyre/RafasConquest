package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GateGeometryTest {
    /** The castle unturned: wall at x 20, doors z -2..0 (left) and 1..2 (right), Gate Block inside at (19, 0, 3). */
    private static final BlockPos GATE = new BlockPos(19, 0, 3);
    private static final int[] LEFT = {1, 0, -5, 1, 2, -3};
    private static final int[] RIGHT = {1, 0, -2, 1, 2, -1};

    @Test
    void eachDoorHingesOnItsOuterEdgeOnTheInsideFace() {
        GateGeometry.Hinge right = GateGeometry.hinge(GATE, Direction.WEST, RIGHT, LEFT);
        assertEquals(20, right.x());
        assertEquals(3, right.z());
        assertEquals(1.5, right.y());
        GateGeometry.Hinge left = GateGeometry.hinge(GATE, Direction.WEST, LEFT, RIGHT);
        assertEquals(20, left.x());
        assertEquals(-2, left.z());
    }

    @Test
    void bothDoorsSwingInward() {
        for (int[][] pair : new int[][][]{{LEFT, RIGHT}, {RIGHT, LEFT}}) {
            GateGeometry.Hinge h = GateGeometry.hinge(GATE, Direction.WEST, pair[0], pair[1]);
            // The free edge, from the hinge along the door, turned by openYaw about +y (JOML: x' = cos x + sin z, z' = -sin x + cos z).
            double mid = GATE.getZ() + (pair[0][2] + pair[0][5] + 1) / 2.0;
            double ex = 0;
            double ez = Math.signum(mid - h.z());
            double x = Math.cos(h.openYaw()) * ex + Math.sin(h.openYaw()) * ez;
            assertEquals(-1, x, 1e-9, "opens toward the Gate Block, -x");
        }
        assertEquals(-GateGeometry.hinge(GATE, Direction.WEST, LEFT, RIGHT).openYaw(),
                GateGeometry.hinge(GATE, Direction.WEST, RIGHT, LEFT).openYaw());
    }

    @Test
    void aGateBlockInTheDoorsPlaneTakesItsFacingAsInward() {
        // Doors along x at z 0, the Gate Block beside them in the same row, facing south (+z): the hinge is on the +z face.
        GateGeometry.Hinge h = GateGeometry.hinge(BlockPos.ZERO, Direction.SOUTH, new int[]{-3, 0, 0, -1, 2, 0}, new int[]{-6, 0, 0, -4, 2, 0});
        assertEquals(1, h.z());
        assertEquals(0, h.x());
    }
}
