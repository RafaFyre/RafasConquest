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
    /** Bait: the front file that runs ahead as bait (-1 none). */
    int baitFile = -1;
    boolean baitSprung;
    /** Pincer: the lured enemy block being struck, and since when. */
    @Nullable
    FieldBlock pincerTarget;
    int pincerSince;
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

    public WarPlan plan() {
        return plan;
    }

    boolean greatShield() {
        return plan.has(Doctrine.Formation.GREAT_SHIELD);
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
