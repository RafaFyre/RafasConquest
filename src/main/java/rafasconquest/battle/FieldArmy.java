package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.General;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** One side of a {@link FieldBattle}: its blocks, its general and how its lines stand. */
public final class FieldArmy {
    final int side;
    final General general;
    final double facingX;
    final double facingZ;
    final List<FieldBlock> blocks = new ArrayList<>();
    /** The block holding each file of the front line (it may have fallen; a relief then takes its place). */
    FieldBlock[] front = new FieldBlock[0];
    /** Half the width of the front line, for the enemy's cavalry to ride round. */
    double halfWidth;
    double generalBack;
    @Nullable
    FieldBlock commander;
    @Nullable
    FieldBlock bearer;
    boolean reserveCommitted;
    int reliefs;
    int started;
    int queueTicket;

    FieldArmy(int side, General general, double facingX, double facingZ) {
        this.side = side;
        this.general = general;
        this.facingX = facingX;
        this.facingZ = facingZ;
    }

    public int side() {
        return side;
    }

    public General general() {
        return general;
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

    public List<FieldBlock> blocks() {
        return List.copyOf(blocks);
    }

    public int started() {
        return started;
    }

    public boolean reserveCommitted() {
        return reserveCommitted;
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
        return blocks.stream().filter(f -> f.duty == Duty.LINE && f.file == file && f.alive > 0)
                .sorted(Comparator.comparingInt(f -> f.queueTicket)).toList();
    }

    /** Puts a block at the back of a file's queue. */
    void join(FieldBlock f, int file) {
        f.duty = Duty.LINE;
        f.file = file;
        f.queueTicket = queueTicket++;
    }
}
