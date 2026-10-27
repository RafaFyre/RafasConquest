package rafasconquest.battle;

import java.util.List;

/**
 * How an army of 5 formations of 20 and its command group deploy, and how its general decides, worked out on plain
 * numbers so it can be tested without a server. Positions are in the army's own frame: {@code right} along its battle
 * line (positive to the army's right), {@code back} behind its front (positive away from the enemy).
 */
public final class BattlePlan {
    public static final int SOLDIERS_PER_FORMATION = 20;
    /** Ranks are this many soldiers wide, so a formation of 20 stands 2 deep. */
    public static final int FILE_WIDTH = 10;
    /** Which soldier of a formation is its leader: the middle of the front rank. */
    public static final int LEADER_INDEX = FILE_WIDTH / 2 - 1;
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
    /** Cavalry charges once an enemy comes this close, whatever else it was doing. */
    public static final double CAVALRY_THREAT = 20;
    /** A tactician's infantry waits this long after forming while the archers shoot... */
    public static final int PATIENCE_SECONDS = 20;
    /** ...unless an enemy comes this close first. */
    public static final double PATIENCE_RANGE = 25;
    /** A tactician commits the reserve where a fighting infantry formation is down to this share of its soldiers. */
    public static final double RESERVE_TRIGGER = 0.6;
    /** A flanking formation moves on to its next waypoint once its line is this close to the current one. */
    public static final double WAYPOINT_REACHED = 6;
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

    /** The two kinds of general, rolled 50/50 for each army. */
    public enum General {
        /** A max-level soldier who charges in with the infantry. Commands by the simple rules. */
        WARRIOR,
        /** Stays back and commands better: patience, cavalry flanking, a reserve. */
        TACTICIAN;

        public String label() {
            return this == WARRIOR ? "warrior" : "tactician";
        }
    }

    /**
     * Where a formation stands when the army deploys, in the army's frame (the centre of its front rank). A reserve
     * formation waits behind the line until the general commits it.
     */
    public record Slot(Arm arm, double right, double back, boolean reserve) {
        public Slot(Arm arm, double right, double back) {
            this(arm, right, back, false);
        }
    }

    /**
     * A warrior's army: 3 infantry formations in the front line (swordsmen on both wings of a shieldwall), archers
     * behind its centre and cavalry off its right wing. So 1/5 archers, 1/5 cavalry.
     */
    public static final List<Slot> WARRIOR_DEPLOYMENT = List.of(
            new Slot(Arm.SWORDSMEN, -21, 0),
            new Slot(Arm.SHIELDWALL, 0, 0),
            new Slot(Arm.SWORDSMEN, 21, 0),
            new Slot(Arm.ARCHERS, 0, 12),
            new Slot(Arm.CAVALRY, 40, 4));

    /** A tactician's army: the same formations, but one of the swordsmen waits in reserve behind the archers. */
    public static final List<Slot> TACTICIAN_DEPLOYMENT = List.of(
            new Slot(Arm.SWORDSMEN, -10.5, 0),
            new Slot(Arm.SHIELDWALL, 10.5, 0),
            new Slot(Arm.SWORDSMEN, 0, 24, true),
            new Slot(Arm.ARCHERS, 0, 12),
            new Slot(Arm.CAVALRY, 40, 4));

    /** How far behind the front centre the general stands: a warrior just behind the line, a tactician well back. */
    public static double generalBack(General general) {
        return general == General.WARRIOR ? 6 : 32;
    }

    /** The standard-bearer stands this far to the general's right. */
    public static final double BEARER_RIGHT = 2;

    public static List<Slot> deployment(General general) {
        return general == General.WARRIOR ? WARRIOR_DEPLOYMENT : TACTICIAN_DEPLOYMENT;
    }

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
        /** Ride around the enemy's flank to strike from behind ({@link #flankRoute}). */
        FLANK,
        /** Break formation and fight. */
        CHARGE
    }

    /**
     * The general's order for a formation that hasn't charged yet (reserves aside: see {@link #reserveToCommit}).
     * {@code nearestEnemy} is the distance from the formation's line to the nearest enemy soldier;
     * {@code infantryEngaged} is whether any of its own army's infantry has charged.
     */
    public static Order order(General general, Arm arm, int secondsElapsed, double nearestEnemy, boolean infantryEngaged) {
        if (secondsElapsed < FORMING_SECONDS) {
            return Order.HOLD;
        }
        boolean tactician = general == General.TACTICIAN;
        return switch (arm) {
            case SWORDSMEN, SHIELDWALL -> {
                if (nearestEnemy <= INFANTRY_ENGAGE) {
                    yield Order.CHARGE;
                }
                boolean waiting = tactician && secondsElapsed < FORMING_SECONDS + PATIENCE_SECONDS && nearestEnemy > PATIENCE_RANGE;
                yield waiting ? Order.HOLD : Order.MARCH;
            }
            case ARCHERS -> nearestEnemy <= ARCHER_RANGE ? Order.HOLD : Order.MARCH;
            case CAVALRY -> {
                if (nearestEnemy <= CAVALRY_THREAT) {
                    yield Order.CHARGE;
                }
                if (!infantryEngaged) {
                    yield Order.HOLD;
                }
                yield tactician ? Order.FLANK : Order.CHARGE;
            }
        };
    }

    /**
     * Which fighting infantry formation a tactician sends the reserve to: the weakest one that has charged and is
     * down to {@link #RESERVE_TRIGGER} of its soldiers. -1 if none needs it yet.
     */
    public static int reserveToCommit(int[] alive, int[] started, boolean[] fighting) {
        int pick = -1;
        double weakest = RESERVE_TRIGGER;
        for (int i = 0; i < alive.length; i++) {
            double share = started[i] == 0 ? 1 : (double) alive[i] / started[i];
            if (fighting[i] && share <= weakest) {
                weakest = share;
                pick = i;
            }
        }
        return pick;
    }

    /**
     * The flanking ride for cavalry on its army's right: first wide past the end of the enemy line, then round
     * behind it. {@code enemyCentre} is the middle of the enemy's soldiers; ({@code facingX}, {@code facingZ}) is
     * the flanking army's facing, toward the enemy.
     */
    public static List<Point> flankRoute(Point enemyCentre, double facingX, double facingZ) {
        return List.of(
                place(enemyCentre, facingX, facingZ, 45, 0),
                place(enemyCentre, facingX, facingZ, 20, -25));
    }

    /** Whether an army with {@code alive} of its {@code started} soldiers left routs. */
    public static boolean routs(int alive, int started) {
        return alive <= started * ROUT_SHARE;
    }

    /**
     * The two armies' uniform colours (RGB) from their factions' main colours. Factions sharing a colour, or close to
     * it, would look like one army, so then the second side wears a darker shade (a lighter one if that is too dark).
     */
    public static int[] uniformColors(int first, int second) {
        if (colorDistance(first, second) >= 60) {
            return new int[]{first, second};
        }
        int darker = scale(second, 0.55);
        int shade = brightness(darker) >= 40 ? darker : lighten(second, 0.5);
        return new int[]{first, shade};
    }

    private static double colorDistance(int a, int b) {
        int dr = (a >> 16 & 255) - (b >> 16 & 255);
        int dg = (a >> 8 & 255) - (b >> 8 & 255);
        int db = (a & 255) - (b & 255);
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }

    private static int brightness(int rgb) {
        return Math.max(rgb >> 16 & 255, Math.max(rgb >> 8 & 255, rgb & 255));
    }

    private static int scale(int rgb, double f) {
        return (int) ((rgb >> 16 & 255) * f) << 16 | (int) ((rgb >> 8 & 255) * f) << 8 | (int) ((rgb & 255) * f);
    }

    private static int lighten(int rgb, double f) {
        int r = rgb >> 16 & 255;
        int g = rgb >> 8 & 255;
        int b = rgb & 255;
        return (int) (r + (255 - r) * f) << 16 | (int) (g + (255 - g) * f) << 8 | (int) (b + (255 - b) * f);
    }
}
