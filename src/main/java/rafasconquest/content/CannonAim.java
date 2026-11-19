package rafasconquest.content;

import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

/**
 * A Medium Cannon's lay, shared by the server (where its shot leaves) and the client (how it is drawn). In the block's
 * own frame the model points +z; the carriage turns {@code yaw} degrees about the block's bottom middle, and the barrel
 * tips up {@code pitch} degrees on its trunnions. The block's facing and its ship's turn go on top ({@code ship} null
 * on land). Angles are degrees, each kept within {@link #ARC} of rest.
 */
public final class CannonAim {
    /** How far it lays either way, up and down and left and right. */
    public static final double ARC = 30;
    /** Its trunnions' axis, from the block's bottom middle (blocks): where the barrel tips. */
    public static final Vector3d PIVOT = new Vector3d(0, 13.1 / 16, (5.4 - 8) / 16);
    /** Its muzzle, from the trunnions, in the barrel's frame. */
    public static final Vector3d MUZZLE = new Vector3d(0, 1 / 16.0, (31.3 - 5.4) / 16);
    /** A ball's speed leaving the muzzle and its fall, blocks a tick. No drag: a ball flies a true parabola, as clients draw it. */
    public static final double SPEED = 3.5;
    public static final double GRAVITY = 0.05;

    private CannonAim() {
    }

    /** The block's facing as a turn about y (radians): the one taking +z to the way it faces. */
    public static double facing(net.minecraft.core.Direction d) {
        return Math.atan2(d.getStepX(), d.getStepZ());
    }

    /** The carriage's turn: ship, then facing, then its own yaw. */
    public static Quaterniond carriage(Quaterniondc ship, double facing, double yaw) {
        Quaterniond q = ship == null ? new Quaterniond() : new Quaterniond(ship);
        return q.rotateY(facing + Math.toRadians(yaw));
    }

    /** The barrel's turn: the carriage's, tipped up {@code pitch}. */
    public static Quaterniond barrel(Quaterniond carriage, double pitch) {
        return new Quaterniond(carriage).rotateX(-Math.toRadians(pitch));
    }

    /** Where its shot leaves, from the block's bottom middle (as its ship lies). */
    public static Vector3d muzzle(Quaterniond carriage, Quaterniond barrel) {
        return carriage.transform(new Vector3d(PIVOT)).add(barrel.transform(new Vector3d(MUZZLE)));
    }

    /** The way the barrel points (unit). */
    public static Vector3d bore(Quaterniond barrel) {
        return barrel.transform(new Vector3d(0, 0, 1));
    }

    /**
     * The lay, {yaw, pitch} clamped to the arc, that points the bore along world direction {@code dir}, the block facing
     * {@code facing} on a ship turned {@code ship}.
     */
    public static double[] layFor(Vector3d dir, Quaterniondc ship, double facing) {
        Vector3d local = new Vector3d(dir).normalize();
        if (ship != null) {
            new Quaterniond(ship).conjugate().transform(local);
        }
        double yaw = Math.toDegrees(wrap(Math.atan2(local.x, local.z) - facing));
        double pitch = Math.toDegrees(Math.asin(Math.max(-1, Math.min(1, local.y))));
        return new double[]{clamp(yaw), clamp(pitch)};
    }

    /**
     * The world direction to fire a ball from {@code from} so it falls on {@code to}: the low arc; the highest it may
     * be laid (along the arc's top) when out of reach.
     */
    public static Vector3d ballistic(Vector3d from, Vector3d to) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        double d = Math.max(1e-6, Math.hypot(dx, dz));
        double h = to.y - from.y;
        double v2 = SPEED * SPEED;
        double root = v2 * v2 - GRAVITY * (GRAVITY * d * d + 2 * h * v2);
        double angle = root < 0 ? Math.toRadians(45) : Math.atan((v2 - Math.sqrt(root)) / (GRAVITY * d));
        return new Vector3d(dx / d * Math.cos(angle), Math.sin(angle), dz / d * Math.cos(angle));
    }

    public static double clamp(double degrees) {
        return Math.max(-ARC, Math.min(ARC, degrees));
    }

    private static double wrap(double radians) {
        return Math.atan2(Math.sin(radians), Math.cos(radians));
    }
}
