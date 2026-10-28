package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** One side of a {@link FieldBattle}: its blocks, its general and their plan, and how its lines stand. */
public final class FieldArmy {
    final int side;
    final double facingX;
    final double facingZ;
    final List<FieldBlock> blocks = new ArrayList<>();
    /** The block holding each file of the front line (it may have fallen; a relief then takes its place). */
    FieldBlock[] front = new FieldBlock[0];
    @Nullable
    FieldBlock commander;
    @Nullable
    FieldBlock bearer;
    /** The general's guard of 20 riders. */
    @Nullable
    FieldBlock guard;
    final WarPlan plan;
    int stackFiles;
    /** Where its front centre stood when it deployed, which hand-placed blocks are saved relative to. */
    Point origin;
    /** The wedge's blocks, tip first (Great Spear, Unrelenting Charge); empty if it has none. */
    final List<FieldBlock> wedge = new ArrayList<>();
    /** Where the wedge's tip waits, relative to the front centre: {right, back}. */
    double wedgeHomeRight;
    double wedgeHomeBack;
    /** The wedge's charge under way: whom it rides at, and which half rides (-1 all); the half to ride next. */
    @Nullable
    FieldBlock wedgeTarget;
    int wedgeHalf = -1;
    int nextHalf;
    int wedgeSince;
    /** Whether the wedge has formed (once the front line is established), and since when the front lines have met. */
    boolean wedgeFormed;
    int frontlineSince = -1;
    /**
     * Wings: an army over {@link BattlePlan#WINGS_OVER} soldiers fights as a left wing, centre and right wing
     * ({@link #LEFT}, {@link #CENTRE}, {@link #RIGHT}), each with its own share of the front, its own cavalry, bait and
     * pincer, so several actions go on at once across the field. A smaller army's front is all centre, its cavalry
     * still on two wings.
     */
    public static final int LEFT = 0;
    public static final int CENTRE = 1;
    public static final int RIGHT = 2;
    boolean winged;
    /** How far to the army's right the middle of each wing stands, and how far behind the front its backline is. */
    final double[] wingRight = new double[3];
    double rearBack;
    /** Bait: the front file in each wing that runs ahead as bait (-1 none), and whether it has drawn the enemy in. */
    final int[] baitFile = {-1, -1, -1};
    final boolean[] baitSprung = new boolean[3];
    /** Pincer: the lured enemy block each wing is striking, and since when. */
    final FieldBlock[] pincerTarget = new FieldBlock[3];
    final int[] pincerSince = new int[3];
    /** Whether each wing is wheeling onto the enemy's exposed flank. */
    final boolean[] enveloping = new boolean[3];
    int reliefs;
    int started;
    int queueTicket;

    FieldArmy(int side, double facingX, double facingZ, WarPlan plan) {
        this.side = side;
        this.plan = plan;
        this.facingX = facingX;
        this.facingZ = facingZ;
    }

    public int side() {
        return side;
    }

    public double facingX() {
        return facingX;
    }

    public double facingZ() {
        return facingZ;
    }

    @Nullable
    public FieldBlock commander() {
        return commander;
    }

    @Nullable
    public FieldBlock bearer() {
        return bearer;
    }

    @Nullable
    public FieldBlock guard() {
        return guard;
    }

    public Point origin() {
        return origin;
    }

    public WarPlan plan() {
        return plan;
    }

    boolean greatShield() {
        return plan.has(Doctrine.Formation.GREAT_SHIELD);
    }

    /** How wide its stacked front's blocks are (a Great Shield, or a big army), or 0 for a front of single blocks. */
    int stackFiles() {
        return stackFiles;
    }

    public List<FieldBlock> blocks() {
        return List.copyOf(blocks);
    }

    public int started() {
        return started;
    }

    public int reliefs() {
        return reliefs;
    }

    public int alive() {
        int n = 0;
        for (FieldBlock b : blocks) {
            n += b.alive;
        }
        return n;
    }

    public boolean generalAlive() {
        return commander != null && commander.alive > 0;
    }

    public boolean standardAlive() {
        return bearer != null && bearer.alive > 0;
    }

    boolean infantryEngaged() {
        for (FieldBlock f : front) {
            if (f.engaged && f.alive > 0) {
                return true;
            }
        }
        return false;
    }

    /** Whether a wing's front is fighting (the whole front's, for a wing with no front of its own). */
    boolean wingEngaged(int wing) {
        boolean any = false;
        for (FieldBlock f : front) {
            if (f.wing == wing) {
                any = true;
                if (f.engaged && f.alive > 0) {
                    return true;
                }
            }
        }
        return !any && infantryEngaged();
    }

    /** Whether the army fights in wings; a block's wing is {@link FieldBlock#wing()}. */
    public boolean winged() {
        return winged;
    }

    /** "on the left wing" and so on, for telling what a wing did; nothing for an army without wings. */
    String where(int wing) {
        if (!winged) {
            return "";
        }
        return switch (wing) {
            case LEFT -> " on the left wing";
            case RIGHT -> " on the right wing";
            default -> " in the centre";
        };
    }

    /** The middle of the front line: the average of its blocks' lines. */
    Point frontCentre() {
        if (front.length == 0) {
            return blocks.get(0).line;
        }
        double x = 0;
        double z = 0;
        for (FieldBlock f : front) {
            x += f.line.x();
            z += f.line.z();
        }
        return new Point(x / front.length, z / front.length);
    }

    /** The blocks waiting behind the front in {@code file}, nearest the front first. */
    List<FieldBlock> queue(int file) {
        return blocks.stream().filter(f -> f.duty == Duty.LINE && f.file == file && f.alive > 0 && f.strikeAt == null)
                .sorted(Comparator.comparingInt(f -> f.queueTicket)).toList();
    }

    /** Puts a block at the back of a file's queue. */
    void join(FieldBlock f, int file) {
        f.duty = Duty.LINE;
        f.file = file;
        f.queueTicket = queueTicket++;
    }
}
