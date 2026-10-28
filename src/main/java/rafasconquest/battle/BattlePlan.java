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
    /** The wings of such an army stand side by side, this far apart. */
    public static final double WING_GAP = 3;
    /**
     * Archers stand at most this many rows deep; the reserves this far behind the archers, their lines this far apart
     * (rear rank to front rank); the cavalry this far behind the reserves, at most this many rows deep.
     */
    public static final int ARCHER_ROWS = 3;
    public static final double RESERVE_GAP = 24;
    public static final double RESERVE_ROW_GAP = 4;
    public static final double CAVALRY_GAP_BACK = 10;
    public static final int CAVALRY_ROWS = 3;
    /** A stacked front takes about this share of the infantry; the rest stands in reserve. */
    public static final double FRONT_SHARE = 0.7;

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

    /** Which wing a front file stands in: the left and right quarters of the front, the middle half the centre. */
    public static int wingOf(int file, int files, boolean winged) {
        if (!winged) {
            return CENTRE;
        }
        int quarter = files / 4;
        return file < quarter ? LEFT : file >= files - quarter ? RIGHT : CENTRE;
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
     * How far behind the front block of its file the block {@code place} places back stands. In a front of single
     * blocks ({@code stackFiles} 0) {@link #LINE_GAP} apart. In a stacked front (a Great Shield, or a big army), whose
     * blocks are {@code stackFiles} wide, the first {@link #SHIELD_STACK} closed up one behind the other, the rest
     * further back (the reserves keep the places they deployed to; this is where they would close up to).
     */
    public static double fileBack(int place, int stackFiles) {
        if (stackFiles == 0) {
            return place * LINE_GAP;
        }
        if (place < SHIELD_STACK) {
            return place * stackPitch(stackFiles);
        }
        return archerBack(stackFiles) + ARCHER_BAND + (place - SHIELD_STACK) * LINE_GAP;
    }

    /** The same for a Great Shield's blocks, or a front of single blocks. */
    public static double fileBack(int place, boolean greatShield) {
        return fileBack(place, greatShield ? SHIELD_FILES : 0);
    }

    /** From one block's front rank to the next's in a stacked file: its ranks and a rank's gap. */
    public static double stackPitch(int files) {
        return (double) SOLDIERS_PER_FORMATION / files * SPACING;
    }

    /**
     * How far behind the front archers stand, at the least: {@link #ARCHER_BACK}, or behind a stacked front (blocks
     * {@code stackFiles} wide) clear of its whole stack (its last block's rear rank, and a gap).
     */
    public static double archerBack(int stackFiles) {
        if (stackFiles == 0) {
            return ARCHER_BACK;
        }
        double stackRear = (SHIELD_STACK - 1) * stackPitch(stackFiles) + (SOLDIERS_PER_FORMATION / stackFiles - 1) * SPACING;
        return stackRear + ARCHER_CLEARANCE;
    }

    /** The same for a Great Shield, or a front of single blocks. */
    public static double archerBack(boolean greatShield) {
        return archerBack(greatShield ? SHIELD_FILES : 0);
    }

    /** How wide the blocks of a stacked front are, or 0 if the army's front is single blocks. */
    public static int stackFiles(int blocks, boolean greatShield) {
        return greatShield ? SHIELD_FILES : blocks * SOLDIERS_PER_FORMATION > WIDE_OVER ? FILE_WIDTH : 0;
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
        // The front: wide enough to cover the archers behind it (at most ARCHER_ROWS rows of them). In a Great Shield
        // each front file is SHIELD_STACK blocks deep and the front only as wide as that; otherwise a plain front as
        // wide as the army's size asks (a big army three lines deep), and at least that wide.
        int files = greatShield ? SHIELD_FILES : FILE_WIDTH;
        double pitch = pitch(files);
        int archersPerRow = (archers + ARCHER_ROWS - 1) / ARCHER_ROWS;
        int cover = Math.max(1, (int) Math.ceil(archersPerRow * BLOCK_PITCH / pitch));
        int stackFiles = stackFiles(blocks, greatShield);
        int width;
        int frontRows;
        if (stackFiles > 0) {
            // A stacked front: each file SHIELD_STACK blocks closed up, about FRONT_SHARE of the infantry, and at least
            // as wide as the archers need; the rest in reserve.
            int share = (int) Math.ceil(infantry * FRONT_SHARE / SHIELD_STACK);
            width = Math.max(1, Math.min(Math.min(MAX_WIDE_FRONT, Math.max(cover, share)), infantry / SHIELD_STACK));
            frontRows = Math.min(SHIELD_STACK, infantry / width);
        } else {
            int plain = infantry <= 4 ? infantry : Math.min(MAX_WIDE_FRONT,
                    Math.max(Math.min(MAX_FRONT, (infantry + 1) / 2), (infantry + LINES_DEEP - 1) / LINES_DEEP));
            if (blocks * SOLDIERS_PER_FORMATION > WIDE_OVER) {
                plain = Math.min(MAX_WIDE_FRONT, (infantry + WIDE_LINES_DEEP - 1) / WIDE_LINES_DEEP);
            }
            width = Math.min(infantry, Math.min(MAX_WIDE_FRONT, Math.max(plain, cover)));
            frontRows = 1;
        }
        double blockDepth = (SOLDIERS_PER_FORMATION / files - 1) * SPACING;
        // A front that can't widen enough to cover three rows of archers (its limit reached) covers more rows instead.
        archersPerRow = Math.max(1, Math.min(archersPerRow, (int) (width * pitch / BLOCK_PITCH)));

        // The wings: the front's left quarter, middle half and right quarter, side by side, WING_GAP apart.
        boolean winged = winged(blocks, width);
        int[] wingFiles = new int[3];
        for (int file = 0; file < width; file++) {
            wingFiles[wingOf(file, width, winged)]++;
        }
        double total = width * pitch + (winged ? 2 * WING_GAP : 0);
        double[] fileRight = new double[width];
        double at = -total / 2 + pitch / 2;
        for (int file = 0; file < width; file++) {
            if (file > 0 && wingOf(file, width, winged) != wingOf(file - 1, width, winged)) {
                at += WING_GAP;
            }
            fileRight[file] = at;
            at += pitch;
        }
        List<Slot> slots = new ArrayList<>(blocks);
        for (int row = 0; row < frontRows; row++) {
            for (int file = 0; file < width; file++) {
                slots.add(new Slot(infantryArm(file, greatShield), fileRight[file], fileBack(row, stackFiles), row, file, files,
                        wingOf(file, width, winged)));
            }
        }
        double frontRear = fileBack(frontRows - 1, stackFiles) + blockDepth;

        // Archers: in rows right behind the front, which covers them.
        double archersBack = archerBack(stackFiles);
        int archerRows = archersPerRow == 0 ? 0 : (archers + archersPerRow - 1) / archersPerRow;
        for (int i = 0; i < archers; i++) {
            int row = i / archersPerRow;
            int inRow = Math.min(archersPerRow, archers - row * archersPerRow);
            double right = across(i % archersPerRow, inRow, BLOCK_PITCH);
            slots.add(new Slot(Arm.ARCHERS, right, archersBack + row * ARCHER_ROW, -1, i, FILE_WIDTH, wingAt(right, fileRight, width, winged)));
        }
        double archersRear = archerRows == 0 ? frontRear : archersBack + (archerRows - 1) * ARCHER_ROW + (SOLDIERS_PER_FORMATION / FILE_WIDTH - 1) * SPACING;

        // Reserves: the rest of the infantry, after a wide gap, in lines of one width, centred; each joins the file of
        // the front block it stands behind.
        int reserves = infantry - width * frontRows;
        int reserveLines = reserves == 0 ? 0 : (reserves + width - 1) / width;
        int perLine = reserveLines == 0 ? 0 : (reserves + reserveLines - 1) / reserveLines;
        double reservesBack = Math.max(frontRear, archersRear) + RESERVE_GAP;
        for (int i = 0; i < reserves; i++) {
            int line = i / perLine;
            int inLine = Math.min(perLine, reserves - line * perLine);
            double right = across(i % perLine, inLine, pitch);
            int file = nearestFile(right, fileRight);
            slots.add(new Slot(infantryArm(file, greatShield), right, reservesBack + line * (blockDepth + RESERVE_ROW_GAP),
                    frontRows + line, file, files, wingOf(file, width, winged)));
        }
        double reservesRear = reserveLines == 0 ? Math.max(frontRear, archersRear)
                : reservesBack + (reserveLines - 1) * (blockDepth + RESERVE_ROW_GAP) + blockDepth;

        // Cavalry: in at most CAVALRY_ROWS rows behind the reserves, centred; its left, middle and right thirds are the
        // left wing's, the centre's and the right wing's.
        double cavalryBack = (reserveLines == 0 ? reservesRear + RESERVE_GAP : reservesRear) + CAVALRY_GAP_BACK;
        int cavalryPerRow = Math.max(1, (cavalry + CAVALRY_ROWS - 1) / CAVALRY_ROWS);
        for (int i = 0; i < cavalry; i++) {
            int row = i / cavalryPerRow;
            int inRow = Math.min(cavalryPerRow, cavalry - row * cavalryPerRow);
            int k = i % cavalryPerRow;
            int third = inRow / 3;
            int wing = !winged ? CENTRE : k < third ? LEFT : k >= inRow - third ? RIGHT : CENTRE;
            slots.add(new Slot(Arm.CAVALRY, across(k, inRow, BLOCK_PITCH), cavalryBack + row * CAVALRY_ROW, -1, i, FILE_WIDTH, wing));
        }
        return slots;
    }

    private static Arm infantryArm(int file, boolean greatShield) {
        return greatShield || file % 3 == 1 ? Arm.SHIELDWALL : Arm.SWORDSMEN;
    }

    /** The front file standing nearest a point {@code right} of the army's middle. */
    private static int nearestFile(double right, double[] fileRight) {
        int best = 0;
        for (int file = 1; file < fileRight.length; file++) {
            if (Math.abs(fileRight[file] - right) < Math.abs(fileRight[best] - right)) {
                best = file;
            }
        }
        return best;
    }

    /** The wing of the front file nearest a point {@code right} of the army's middle. */
    private static int wingAt(double right, double[] fileRight, int width, boolean winged) {
        return wingOf(nearestFile(right, fileRight), width, winged);
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
        return archersMayAdvance(behindFront, 0);
    }

    /** The same, for an army in a Great Shield or not: never into its shield stack either. */
    public static boolean archersMayAdvance(double behindFront, int stackFiles) {
        return behindFront - MARCH_STEP >= archerBack(stackFiles);
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
