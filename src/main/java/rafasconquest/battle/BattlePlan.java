package rafasconquest.battle;

import java.util.List;

/**
 * How an army of 5 formations of 20 deploys and how its commander decides, worked out on plain numbers so it can be
 * tested without a server. Positions are in the army's own frame: {@code right} along its battle line (positive to
 * the army's right), {@code back} behind its front (positive away from the enemy).
 */
public final class BattlePlan {
    public static final int SOLDIERS_PER_FORMATION = 20;
    /** Ranks are this many soldiers wide, so a formation of 20 stands 2 deep. */
    public static final int FILE_WIDTH = 10;
    /** Blocks between soldiers in a rank (Recruits' own formation spacing). */
    public static final double SPACING = 1.75;
    /** Each army deploys this far from the battlefield's centre, so the fronts start twice this apart. */
    public static final double DEPLOY_DISTANCE = 30;

    /** Seconds the armies stand in their deployed lines before anyone moves. */
    public static final int FORMING_SECONDS = 5;
    /** How far a marching formation's line moves each second. */
    public static final double MARCH_STEP = 2;
    /** Infantry breaks into a charge once an enemy is this close to its line. */
    public static final double INFANTRY_ENGAGE = 10;
    /** Archers halt this far from the nearest enemy and shoot (Recruits soldiers look for targets within 40). */
    public static final double ARCHER_RANGE = 30;
    /** Cavalry charges once an enemy comes this close, even if the infantry hasn't met yet. */
    public static final double CAVALRY_THREAT = 20;
    /** An army whose soldiers fall to this share of its starting strength routs. */
    public static final double ROUT_SHARE = 0.2;
    /** The battle is called a draw after this long. */
    public static final int TIME_LIMIT_SECONDS = 300;

    public enum Arm {
        SWORDSMEN, SHIELDWALL, ARCHERS, CAVALRY;

        public boolean infantry() {
            return this == SWORDSMEN || this == SHIELDWALL;
        }
    }

    /** Where a formation stands when the army deploys, in the army's frame (the centre of its front rank). */
    public record Slot(Arm arm, double right, double back) {
    }

    /**
     * The army's 5 formations: 3 of infantry in the front line (swordsmen on both wings of a shieldwall), 1 of archers
     * behind its centre and 1 of cavalry off its right wing. So 1/5 archers, 1/5 cavalry.
     */
    public static final List<Slot> DEPLOYMENT = List.of(
            new Slot(Arm.SWORDSMEN, -21, 0),
            new Slot(Arm.SHIELDWALL, 0, 0),
            new Slot(Arm.SWORDSMEN, 21, 0),
            new Slot(Arm.ARCHERS, 0, 12),
            new Slot(Arm.CAVALRY, 40, 4));

    private BattlePlan() {
    }

    /** A point on the ground. */
    public record Point(double x, double z) {
        public Point plus(double dx, double dz) {
            return new Point(x + dx, z + dz);
        }

        public double distanceTo(Point o) {
            return Math.hypot(x - o.x, z - o.z);
        }
    }

    /**
     * Where a slot lands in the world for an army whose front faces ({@code facingX}, {@code facingZ}) (a unit
     * vector toward the enemy) and whose front line is centred on {@code front}.
     */
    public static Point place(Point front, double facingX, double facingZ, double right, double back) {
        // The army's right, seen from behind it: Minecraft's x east, z south, so right of facing (fx, fz) is (-fz, fx).
        double rightX = -facingZ;
        double rightZ = facingX;
        return front.plus(rightX * right - facingX * back, rightZ * right - facingZ * back);
    }

    /** The two armies' front centres: each {@link #DEPLOY_DISTANCE} from the centre along the line of battle. */
    public static Point[] fronts(Point centre, double axisX, double axisZ) {
        return new Point[]{
                centre.plus(-axisX * DEPLOY_DISTANCE, -axisZ * DEPLOY_DISTANCE),
                centre.plus(axisX * DEPLOY_DISTANCE, axisZ * DEPLOY_DISTANCE)};
    }

    /** What a formation does this second. */
    public enum Order {
        /** Stand in formation where it is. */
        HOLD,
        /** Move the formation's line one {@link #MARCH_STEP} toward the enemy, keeping formation. */
        MARCH,
        /** Break formation and fight. */
        CHARGE
    }

    /**
     * The commander's order for a formation that hasn't charged yet. {@code nearestEnemy} is the distance from the
     * formation's line to the nearest enemy soldier; {@code infantryEngaged} is whether any of its own army's infantry
     * has charged.
     */
    public static Order order(Arm arm, int secondsElapsed, double nearestEnemy, boolean infantryEngaged) {
        if (secondsElapsed < FORMING_SECONDS) {
            return Order.HOLD;
        }
        return switch (arm) {
            case SWORDSMEN, SHIELDWALL -> nearestEnemy <= INFANTRY_ENGAGE ? Order.CHARGE : Order.MARCH;
            case ARCHERS -> nearestEnemy <= ARCHER_RANGE ? Order.HOLD : Order.MARCH;
            case CAVALRY -> infantryEngaged || nearestEnemy <= CAVALRY_THREAT ? Order.CHARGE : Order.HOLD;
        };
    }

    /** Whether an army with {@code alive} of its {@code started} soldiers left routs. */
    public static boolean routs(int alive, int started) {
        return alive <= started * ROUT_SHARE;
    }
}
