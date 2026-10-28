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
     * to {@link #MAX_WIDE_FRONT} blocks. An army of more than {@link #WIDE_OVER} soldiers stands
     * {@link #WIDE_LINES_DEEP} lines deep, so each wing has a broad front of its own.
     */
    public static final int MAX_FRONT = 10;
    public static final int LINES_DEEP = 6;
    public static final int MAX_WIDE_FRONT = 40;
    public static final int WIDE_OVER = 1000;
    public static final int WIDE_LINES_DEEP = 3;
    /** From one line to the next behind it. */
    public static final double LINE_GAP = 16;
    /** Archers stand in rows this far behind the front line, and this far apart. */
    public static final double ARCHER_BACK = 4;
    /**
     * In a Great Shield the archers stand this far behind the stack's rear rank, and the blocks waiting in the files
     * this far behind the archers' front row: room for six rows of archers and a gap.
     */
    public static final double ARCHER_CLEARANCE = 3;
    public static final double ARCHER_BAND = 6 * 4 + 6;
    public static final double ARCHER_ROW = 4;
    /** Cavalry in reserve stands in rows this far apart; a wing with no front of its own is this far past the line's end. */
    public static final double CAVALRY_ROW = 8;
    public static final double CAVALRY_GAP = 12;
    /** Each army deploys this far from the battlefield's centre, so the fronts start twice this apart. */
    public static final double DEPLOY_DISTANCE = 30;
    /** An army of more than this many soldiers fights in a left wing, centre and right wing. */
    public static final int WINGS_OVER = 300;

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
     * -1. {@code files} is how many soldiers wide its ranks are; {@code wing} which wing it stands in (0 left, 1
     * centre, 2 right; all centre in an army too small for wings).
     */
    public record Slot(Arm arm, double right, double back, int line, int file, int files, int wing) {
    }

    /** The wings, as {@link Slot#wing()} numbers them. */
    public static final int LEFT = 0;
    public static final int CENTRE = 1;
    public static final int RIGHT = 2;

    /** Whether an army of {@code blocks} blocks, with {@code files} front files, fights in wings. */
    public static boolean winged(int blocks, int files) {
        return blocks * SOLDIERS_PER_FORMATION > WINGS_OVER && files >= 3;
    }

    /** Which wing a front file stands in: the left and right thirds of the front, the rest the centre. */
    public static int wingOf(int file, int files, boolean winged) {
        if (!winged) {
            return CENTRE;
        }
        int third = files / 3;
        return file < third ? LEFT : file >= files - third ? RIGHT : CENTRE;
    }

    /** How many blocks of 20 an army of {@code soldiers} is: the nearest whole number, within the limits. */
    public static int blocks(int soldiers) {
        int clamped = Math.max(MIN_SOLDIERS, Math.min(MAX_SOLDIERS, soldiers));
        return Math.max(1, Math.round(clamped / (float) SOLDIERS_PER_FORMATION));
    }

    /** How an army of {@code blocks} blocks deploys in plain lines ({@link #deployment(int, boolean)}). */
    public static List<Slot> deployment(int blocks) {
        return deployment(blocks, false);
    }

    /** From one block's centre to the next in a line, for blocks {@code files} wide. */
    public static double pitch(int files) {
        return (files - 1) * SPACING + 3;
    }

    /**
     * How far behind the front block of its file the block {@code place} places back stands: {@link #LINE_GAP} apart,
     * or in a Great Shield file the first {@link #SHIELD_STACK} closed up five ranks apart, and the rest behind the
     * archers, who stand behind the stack ({@link #archerBack}).
     */
    public static double fileBack(int place, boolean greatShield) {
        if (!greatShield) {
            return place * LINE_GAP;
        }
        if (place < SHIELD_STACK) {
            return place * SHIELD_BLOCK_DEPTH;
        }
        return archerBack(true) + ARCHER_BAND + (place - SHIELD_STACK) * LINE_GAP;
    }

    /**
     * How far behind the front archers stand, at the least: {@link #ARCHER_BACK}, or in a Great Shield clear behind
     * the whole shield stack (its last block's rear rank, and a gap), so they don't stand among its blocks.
     */
    public static double archerBack(boolean greatShield) {
        if (!greatShield) {
            return ARCHER_BACK;
        }
        double stackRear = (SHIELD_STACK - 1) * SHIELD_BLOCK_DEPTH + (SOLDIERS_PER_FORMATION / SHIELD_FILES - 1) * SPACING;
        return stackRear + ARCHER_CLEARANCE;
    }

    /**
     * How an army of {@code blocks} blocks deploys: a fifth archers, a fifth cavalry (rounded), the rest infantry.
     * The infantry stands in lines at most {@link #MAX_FRONT} blocks wide (a big army wider, see
     * {@link #MAX_FRONT}), at least two lines once there are more than 4 blocks, shieldwalls every third file.
     * <p>
     * Every wing (the left and right thirds of the front and the centre; all centre for a small army) stands alike:
     * its infantry in front, its archers in rows behind its front line, and its cavalry in reserve in the rear, behind
     * the last infantry line, in rows as wide as the wing. Wedges are formed later, once the battle's front line is
     * established ({@link FieldBattle}).
     * <p>
     * In a Great Shield ({@code greatShield}) every infantry block bears shields and stands {@link #SHIELD_FILES} wide
     * and five deep, and each front file is {@link #SHIELD_STACK} blocks closed up one behind the other (fifteen
     * ranks), so the front is narrower; its archers stand clear behind the stack.
     */
    public static List<Slot> deployment(int blocks, boolean greatShield) {
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
        if (blocks * SOLDIERS_PER_FORMATION > WIDE_OVER) {
            width = Math.min(MAX_WIDE_FRONT, (inLines + WIDE_LINES_DEEP - 1) / WIDE_LINES_DEEP);
        }
        if (greatShield) {
            width = Math.max(1, Math.min(width, inLines / SHIELD_STACK));
        }
        int lines = (inLines + width - 1) / width;
        int files = greatShield ? SHIELD_FILES : FILE_WIDTH;
        double pitch = pitch(files);

        boolean winged = winged(blocks, width);
        List<Slot> slots = new ArrayList<>(blocks);
        double[] wingSum = new double[3];
        int[] wingFiles = new int[3];
        for (int line = 0; line < lines; line++) {
            int inThisLine = Math.min(width, inLines - line * width);
            for (int file = 0; file < inThisLine; file++) {
                Arm arm = greatShield || file % 3 == 1 ? Arm.SHIELDWALL : Arm.SWORDSMEN;
                int wing = wingOf(file, width, winged);
                double right = across(file, inThisLine, pitch);
                slots.add(new Slot(arm, right, fileBack(line, greatShield), line, file, files, wing));
                if (line == 0) {
                    wingSum[wing] += right;
                    wingFiles[wing]++;
                }
            }
        }
        double frontHalf = width * pitch / 2;
        // Archers: rows behind the front line, each as wide as the front, in the wing they stand behind.
        int perRow = Math.max(1, Math.min(archers, (int) (2 * frontHalf / BLOCK_PITCH)));
        double archersBack = archerBack(greatShield);
        double rows = 0;
        for (int i = 0; i < archers; i++) {
            int row = i / perRow;
            int inRow = Math.min(perRow, archers - row * perRow);
            double right = across(i % perRow, inRow, BLOCK_PITCH);
            slots.add(new Slot(Arm.ARCHERS, right, archersBack + row * ARCHER_ROW, -1, i, FILE_WIDTH, wingAt(right, frontHalf, winged)));
            rows = row + 1;
        }
        // Cavalry: in reserve in the rear of each wing (all in the centre for a small army), behind the last infantry
        // line and the archers, in rows as wide as its wing.
        double rear = Math.max(fileBack(lines - 1, greatShield), archersBack + Math.max(0, rows - 1) * ARCHER_ROW) + LINE_GAP;
        int[] wingCavalry = new int[3];
        for (int i = 0; i < cavalry; i++) {
            wingCavalry[winged ? new int[]{CENTRE, LEFT, RIGHT}[i % 3] : CENTRE]++;
        }
        int[] placed = new int[3];
        for (int i = 0; i < cavalry; i++) {
            int wing = winged ? new int[]{CENTRE, LEFT, RIGHT}[i % 3] : CENTRE;
            double wingWidth = winged ? wingFiles[wing] * pitch : 2 * frontHalf;
            int columns = Math.max(1, Math.min(wingCavalry[wing], (int) (wingWidth / BLOCK_PITCH)));
            int k = placed[wing]++;
            int row = k / columns;
            int inRow = Math.min(columns, wingCavalry[wing] - row * columns);
            double middle = wingFiles[wing] > 0 ? wingSum[wing] / wingFiles[wing] : 0;
            slots.add(new Slot(Arm.CAVALRY, middle + across(k % columns, inRow, BLOCK_PITCH), rear + row * CAVALRY_ROW, -1, i,
                    FILE_WIDTH, wing));
        }
        return slots;
    }

    /** Which wing a point {@code right} of the army's middle stands in: the thirds of a front {@code 2 frontHalf} wide. */
    private static int wingAt(double right, double frontHalf, boolean winged) {
        if (!winged) {
            return CENTRE;
        }
        return right < -frontHalf / 3 ? LEFT : right > frontHalf / 3 ? RIGHT : CENTRE;
    }

    /**
     * Where the {@code i}th block of a wedge stands, relative to its tip: {right, back}. Row r (from the tip) holds
     * r + 1 blocks side by side, so the wedge widens behind its point.
     */
    public static double[] wedgeSlot(int i) {
        int row = 0;
        while ((row + 1) * (row + 2) / 2 <= i) {
            row++;
        }
        int inRow = i - row * (row + 1) / 2;
        return new double[]{(inRow - row / 2.0) * WEDGE_PITCH, row * WEDGE_ROW};
    }

    /** Where block {@code i} of {@code n} side by side stands along the line, centred on the army's middle. */
    private static double across(int i, int n, double pitch) {
        return (i - (n - 1) / 2.0) * pitch;
    }

    /**
     * Great Shield: each block re-forms this many soldiers wide (so five deep), and each front file holds this many
     * blocks closed up one behind the other, {@link #SHIELD_BLOCK_DEPTH} apart: fifteen ranks.
     */
    public static final int SHIELD_FILES = 4;
    public static final int SHIELD_STACK = 3;
    public static final double SHIELD_BLOCK_DEPTH = 5 * SPACING;

    /**
     * A wedge of cavalry (Great Spear, Unrelenting Charge) stands at least this far behind the front, behind the last
     * infantry line; its blocks stand this far apart across, and its rows this far apart.
     */
    public static final double SPEAR_BACK = 45;
    public static final double WEDGE_PITCH = BLOCK_PITCH;
    public static final double WEDGE_ROW = 6;

    /** How far behind the front centre the general stands, mounted, with the guard this far behind them. */
    public static final double GENERAL_BACK = 20;
    public static final double GUARD_BACK = 4;

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

    /** Where a point lies in the frame of {@link #place}: {right, back} of {@code origin} for an army facing (fx, fz). */
    public static Point offset(Point origin, double facingX, double facingZ, Point at) {
        double dx = at.x() - origin.x();
        double dz = at.z() - origin.z();
        return new Point(dx * -facingZ + dz * facingX, -(dx * facingX + dz * facingZ));
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
        return archersMayAdvance(behindFront, false);
    }

    /** The same, for an army in a Great Shield or not: never into its shield stack either. */
    public static boolean archersMayAdvance(double behindFront, boolean greatShield) {
        return behindFront - MARCH_STEP >= archerBack(greatShield);
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
