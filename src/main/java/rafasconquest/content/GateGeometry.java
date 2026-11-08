package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Where a gate door turns: a vertical line at its outer edge (away from the other door), on its face toward the inside
 * (the side its Gate Block stands on), and which way round it turns to open inward. Boxes are {x0, y0, z0, x1, y1, z1},
 * inclusive, relative to the Gate Block.
 */
public final class GateGeometry {
    /** World (x, z) of the hinge line, the middle of its height y, and the yaw (radians about +y, JOML's sense) that opens it. */
    public record Hinge(double x, double y, double z, double openYaw) {
    }

    private GateGeometry() {
    }

    public static Hinge hinge(BlockPos gate, Direction facing, int[] own, @Nullable int[] other) {
        int[] all = other == null ? own : new int[]{Math.min(own[0], other[0]), 0, Math.min(own[2], other[2]),
                Math.max(own[3], other[3]), 0, Math.max(own[5], other[5])};
        int spanX = all[3] - all[0] + 1;
        int spanZ = all[5] - all[2] + 1;
        boolean alongX = spanX > spanZ || (spanX == spanZ && facing.getAxis() == Direction.Axis.Z);
        // a: the axis the doors lie along; n: across them.
        int a0 = alongX ? own[0] : own[2];
        int a1 = alongX ? own[3] : own[5];
        int n0 = alongX ? own[2] : own[0];
        int n1 = alongX ? own[5] : own[3];
        double gateN = 0.5; // the Gate Block's middle, relative to itself
        double doorN = (n0 + n1 + 1) / 2.0;
        int in = (int) Math.signum(gateN - doorN);
        if (Math.abs(gateN - doorN) < 0.5) {
            in = alongX ? facing.getStepZ() : facing.getStepX();
            if (in == 0) {
                in = 1;
            }
        }
        double mid = (a0 + a1 + 1) / 2.0;
        double otherMid = other == null ? Double.MAX_VALUE
                : alongX ? (other[0] + other[3] + 1) / 2.0 : (other[2] + other[5] + 1) / 2.0;
        double outer = otherMid > mid ? a0 : a1 + 1;
        double edgeN = in > 0 ? n1 + 1 : n0;
        int free = (int) Math.signum(mid - outer);
        // Turned +90° about y, (x, z) goes to (z, -x): the free edge (along a) must end up pointing inward.
        double dot = alongX ? -free * in : free * in;
        double yaw = dot > 0 ? Math.PI / 2 : -Math.PI / 2;
        double y = gate.getY() + (own[1] + own[4] + 1) / 2.0;
        return alongX
                ? new Hinge(gate.getX() + outer, y, gate.getZ() + edgeN, yaw)
                : new Hinge(gate.getX() + edgeN, y, gate.getZ() + outer, yaw);
    }
}
