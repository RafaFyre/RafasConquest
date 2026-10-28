package rafasconquest.battle;

import java.util.ArrayList;
import java.util.List;

/**
 * How an army of blocks of 20 and its command group deploy, and how its general decides, worked out on plain numbers
 * so it can be tested without a server. Positions are in the army's own frame: {@code right} along its battle line
 * (positive to the army's right), {@code back} behind its front (positive away from the enemy).
 * <p>
 * An army fights in lines: a front line of infantry blocks and relief lines behind it.
 * A front block that has lost half its soldiers is relieved by a fresh block from behind and withdraws to the rear;
 * that rotation is what makes a big battle last.
 */
public final class BattlePlan {
    public static final int SOLDIERS_PER_FORMATION = 20;
    /** Armies are this many soldiers at least and at most (whole blocks). */
    public static final int MIN_SOLDIERS = SOLDIERS_PER_FORMATION;
    public static final int MAX_SOLDIERS = 10_000;
    /** Ranks are this many soldiers wide, so a block of 20 stands 2 deep. */
    public static final int FILE_WIDTH = 10;
    /** Which soldier of a formation is its leader (its captain): the middle of the front rank. */
    public static final int LEADER_INDEX = FILE_WIDTH / 2 - 1;
    /** Blocks between soldiers in a rank (Recruits' own formation spacing). */
    public static final double SPACING = 1.75;
    /** From one block's centre to the next in a line: a block's width and a gap. */
    public static final double BLOCK_PITCH = (FILE_WIDTH - 1) * SPACING + 3;
    /**
     * The front line is at most this many blocks wide; more infantry stands in more lines behind it. A big army
     * (over about 2000 soldiers) widens its front instead, to stand no more than {@link #LINES_DEEP} lines deep, up
     * to {@link #MAX_WIDE_FRONT} blocks.
     */
    public static final int MAX_FRONT = 10;
    public static final int LINES_DEEP = 6;
    public static final int MAX_WIDE_FRONT = 40;
    /** From one line to the next behind it. */
    public static final double LINE_GAP = 16;
    /** Archers stand in rows this far behind the front line, and this far apart. */
    public static final double ARCHER_BACK = 6;
    public static final double ARCHER_ROW = 4;
    /** Cavalry stands on the wings, this far beyond the end of the front line, in columns of blocks. */
    public static final double CAVALRY_GAP = 12;
    public static final int CAVALRY_COLUMNS = 2;
    /** A big wing (more than this many blocks per column) has more columns, so it isn't a long thin file. */
    public static final int CAVALRY_ROWS = 5;
    public static final double CAVALRY_ROW = 8;
    public static final double CAVALRY_BACK = 4;
    /** Each army deploys this far from the battlefield's centre, so the fronts start twice this apart. */
    public static final double DEPLOY_DISTANCE = 30;

    /** Seconds the armies stand in their deployed lines before anyone moves. */
    public static final int FORMING_SECONDS = 5;
    /** How far a marching formation's line moves each second. */
    public static final double MARCH_STEP = 2;
    /** Infantry engages once an enemy is this close to its line... */
    public static final double INFANTRY_ENGAGE = 10;
    /** ...and presses on, keeping its ranks, until its front rank is this close to the enemy's, so the lines meet. */
    public static final double FRONT_CONTACT = 2;
    /** Archers halt this far from the nearest enemy and shoot (Recruits soldiers look for targets within 40). */
    public static final double ARCHER_RANGE = 30;
    /** Cavalry charges once an enemy comes this close, whatever else it was doing. */
    public static final double CAVALRY_THREAT = 20;
    /** A front block down to this share of its soldiers is relieved. */
    public static final double RELIEF_SHARE = 0.5;
    /** Only a block with at least this share of its soldiers goes forward to relieve another. */
    public static final double FIT_SHARE = 0.6;
    /** A relieving block takes over once it is this close to the spot, or after {@link #RELIEF_SECONDS}. */
    public static final double RELIEF_ARRIVED = 6;
    public static final int RELIEF_SECONDS = 20;
    /** An army whose soldiers fall to this share of its starting strength routs. */
    public static final double ROUT_SHARE = 0.2;
    /** The battle is called a draw after this long. */
    public static final int TIME_LIMIT_SECONDS = 1800;
    /**
     * Once fighting has begun, a battle where nobody has fallen for this long has stalled (what is left of the lines
     * can't reach each other): everyone left charges.
     */
    public static final int STALL_SECONDS = 30;

    public enum Arm {
        SWORDSMEN, SHIELDWALL, ARCHERS, CAVALRY;

        public boolean infantry() {
            return this == SWORDSMEN || this == SHIELDWALL;
        }
    }

    /**
     * Where a block stands when the army deploys, in the army's frame (the centre of its front rank). Infantry stands
     * in {@code line} 0 (the front), 1, 2... behind it, at {@code file} along the line. Archers and cavalry have line
     * -1.
     */
    public record Slot(Arm arm, double right, double back, int line, int file) {
    }

    /** How many blocks of 20 an army of {@code soldiers} is: the nearest whole number, within the limits. */
    public static int blocks(int soldiers) {
        int clamped = Math.max(MIN_SOLDIERS, Math.min(MAX_SOLDIERS, soldiers));
        return Math.max(1, Math.round(clamped / (float) SOLDIERS_PER_FORMATION));
    }

    /**
     * How an army of {@code blocks} blocks deploys: a fifth archers, a fifth cavalry (rounded), the rest infantry.
     * The infantry stands in lines at most {@link #MAX_FRONT} blocks wide (a big army wider, see
     * {@link #MAX_FRONT}), at least two lines once there are more than 4 blocks, shieldwalls every third file.
     * Archers stand in rows behind the front line; cavalry on both wings (the right wing first).
     */
    public static List<Slot> deployment(int blocks) {
        int archers = Math.round(blocks / 5f);
        int cavalry = Math.round(blocks / 5f);
        int infantry = blocks - archers - cavalry;
        while (infantry < 1) {
            if (cavalry > 0) {
                cavalry--;
            } else {
                archers--;
            }
            infantry++;
        }
        int inLines = infantry;
        int width = inLines <= 4 ? inLines : Math.min(MAX_WIDE_FRONT,
                Math.max(Math.min(MAX_FRONT, (inLines + 1) / 2), (inLines + LINES_DEEP - 1) / LINES_DEEP));
        int lines = (inLines + width - 1) / width;

        List<Slot> slots = new ArrayList<>(blocks);
        for (int line = 0; line < lines; line++) {
            int inThisLine = Math.min(width, inLines - line * width);
            for (int file = 0; file < inThisLine; file++) {
                Arm arm = file % 3 == 1 ? Arm.SHIELDWALL : Arm.SWORDSMEN;
                slots.add(new Slot(arm, across(file, inThisLine), line * LINE_GAP, line, file));
            }
        }
        for (int i = 0; i < archers; i++) {
            int row = i / width;
            int inRow = Math.min(width, archers - row * width);
            slots.add(new Slot(Arm.ARCHERS, across(i % width, inRow), ARCHER_BACK + row * ARCHER_ROW, -1, i));
        }
        double wingStart = width * BLOCK_PITCH / 2 + CAVALRY_GAP;
        int perWing = (cavalry + 1) / 2;
        int columns = Math.max(CAVALRY_COLUMNS, (perWing + CAVALRY_ROWS - 1) / CAVALRY_ROWS);
        for (int i = 0; i < cavalry; i++) {
            int side = i % 2 == 0 ? 1 : -1; // right wing first
            int inWing = i / 2;
            double out = wingStart + (inWing % columns) * BLOCK_PITCH;
            slots.add(new Slot(Arm.CAVALRY, side * out, CAVALRY_BACK + (inWing / columns) * CAVALRY_ROW, -1, i));
        }
        return slots;
    }

    /** Where block {@code i} of {@code n} side by side stands along the line, centred on the army's middle. */
    private static double across(int i, int n) {
        return (i - (n - 1) / 2.0) * BLOCK_PITCH;
    }

    /** How far behind the front centre the general stands: just behind the line, ready to go in with it. */
    public static final double GENERAL_BACK = 6;

    /** The standard-bearer stands this far to the general's right. */
    public static final double BEARER_RIGHT = 2;

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
        /**
         * Fight in line: press on to {@link #FRONT_CONTACT} from the enemy's front, keeping ranks, and fight there; its
         * soldiers take on enemies within reach and return to their places.
         */
        ENGAGE,
        /** Break formation and fight. */
        CHARGE
    }

    /**
     * The general's order for a block in the front line, or archers or cavalry that haven't charged yet.
     * {@code nearestEnemy} is the distance from the formation's line to the nearest enemy soldier;
     * {@code infantryEngaged} is whether any of its own army's front line is fighting.
     */
    public static Order order(Arm arm, int secondsElapsed, double nearestEnemy, boolean infantryEngaged) {
        if (secondsElapsed < FORMING_SECONDS) {
            return Order.HOLD;
        }
        return switch (arm) {
            case SWORDSMEN, SHIELDWALL -> nearestEnemy <= INFANTRY_ENGAGE ? Order.ENGAGE : Order.MARCH;
            case ARCHERS -> nearestEnemy <= ARCHER_RANGE ? Order.HOLD : Order.MARCH;
            case CAVALRY -> {
                if (nearestEnemy <= CAVALRY_THREAT) {
                    yield Order.CHARGE;
                }
                yield infantryEngaged ? Order.CHARGE : Order.HOLD;
            }
        };
    }

    /** Whether a front block with {@code alive} of its {@code started} soldiers should be relieved. */
    public static boolean needsRelief(int alive, int started) {
        return alive <= started * RELIEF_SHARE;
    }

    /**
     * Whether archers whose line is {@code behindFront} blocks behind their own front line (negative: ahead of it)
     * may march another step: never past the line, so they don't walk out alone ahead of infantry that is waiting.
     */
    public static boolean archersMayAdvance(double behindFront) {
        return behindFront - MARCH_STEP >= ARCHER_BACK;
    }

    /** Whether a battle has stalled: fighting began, and nobody has fallen for {@link #STALL_SECONDS}. */
    public static boolean stalled(int seconds, int lastFall) {
        return lastFall > 0 && seconds - lastFall >= STALL_SECONDS;
    }

    /** Whether a block with {@code alive} of its {@code started} soldiers is fit to go forward and relieve another. */
    public static boolean fitToRelieve(int alive, int started) {
        return alive >= started * FIT_SHARE;
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
