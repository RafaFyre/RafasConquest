package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A siege's rules on top of a {@link FieldBattle}: the attacker (side 0) surrounds the walls ({@link Front}), its main
 * attack at its flattest ({@link #MAIN_SHARE} of it), the walls beside that and the far wall taking the rest, every kind
 * of block and engine shared alike: its foot carrying ladders, its siege train ({@link Machine}) bringing towers, rams and
 * mantlets and battering each wall with its artillery; the defender (side 1) holds every wall, archers in front and foot
 * behind them, its reserves inside, close to the walls, sent up to replace whoever is worn down there. Nine in ten of the defenders' cavalry fight on foot
 * as super heavy infantry, by the gate; the rest stay mounted to ride down attackers who get in. The attackers win by
 * breaching a gate: one opened from inside (its Gate Block's button) and left open a minute, or attackers inside the
 * walls for a minute on end, through a gate or a breach in the wall. The defenders never withdraw.
 * <p>
 * The fortress is a box of walls, its sides along x and z. Points are (x, z) in blocks.
 */
public final class Siege {
    /** The attackers' strategems. */
    public enum Engine { LADDERS, TOWERS, RAMS }

    /** At most this many ladders an army brings. */
    public static final int LADDERS = 16;
    /** At most this many rams, one to a gate, and this many siege towers to a wall; the rest of the train fights on foot. */
    public static final int MAX_RAMS = 4;
    public static final int TOWERS_PER_WALL = 4;
    /** A gate left open this long, or attackers inside the walls this long on end, and the fortress is taken. */
    public static final int BREACH_SECONDS = 60;
    /** A ladder takes this long to raise against the wall, a tower to be made fast to it. */
    static final int HOIST_SECONDS = 8;
    static final int DOCK_SECONDS = 3;
    /** Of a block climbing a ladder, or a tower, this share is at the top fighting at once. */
    static final double LADDER_FIGHTS = 0.35;
    static final double TOWER_FIGHTS = 0.7;
    /** Climbing slows a block this much: up a ladder, up a tower. */
    static final double LADDER_PACE = 0.15;
    static final double TOWER_PACE = 0.4;
    /** A tower's crew pushing it is behind it, from the walls' arrows: they take this share. */
    static final double TOWER_SHIELDS = 0.3;
    /** A tower rolls at half a walk, unless the real towers' own speed is known ({@link #towerSpeed(double)}). */
    static final double TOWER_SPEED = FieldRules.FOOT_SPEED / 2;
    /** Its front is half its length out from its centre. */
    public static final double TOWER_HALF_LENGTH = 5;
    /**
     * Where it stops when nothing better is known: this far short of the wall, its centre then TOWER_OUT out. A real
     * tower picks its own stand-off for its ramp ({@link #rampStandOff}).
     */
    public static final double TOWER_GAP = 3;
    public static final double TOWER_OUT = TOWER_HALF_LENGTH + TOWER_GAP;
    /**
     * A Siegeworks tower's ramp, from its collision shapes: hinged this far before the tower's centre and this high
     * over its base, this long, and swinging down at most this far below level.
     */
    public static final double RAMP_HINGE_OUT = 4.1875;
    public static final double RAMP_HINGE_UP = 10;
    public static final double RAMP_LENGTH = 9.5625;
    static final double RAMP_MAX_DROP = Math.toRadians(30);
    /** Its tip comes down at least this far in past the wall's outer edge, onto the walkway. */
    static final double RAMP_LAND = 2;
    /** Stand-offs a tower weighs, this far apart. */
    static final double RAMP_STEP = 0.5;

    /**
     * How far out from the wall's face a tower's centre should stop for its ramp to come down as near level as it can:
     * the ramp, swinging down, rests on the wall's edge at {@code walkY}, so its slope is the rise or fall from its hinge
     * over the run to the edge, and the further out it stands the flatter it lies, as far as it still reaches onto the
     * walkway. Among equally flat stand-offs (level ground and a walkway at the hinge's height) it keeps
     * {@link #TOWER_OUT}. {@code groundAt} is the ground's height under the tower's centre that far out. With no
     * stand-off reaching, it goes as close as it can.
     */
    public static double rampStandOff(double walkY, java.util.function.DoubleUnaryOperator groundAt) {
        double best = TOWER_HALF_LENGTH;
        double bestTilt = Double.MAX_VALUE;
        for (double out = TOWER_HALF_LENGTH; out <= RAMP_HINGE_OUT + RAMP_LENGTH; out += RAMP_STEP) {
            double rise = walkY - (groundAt.applyAsDouble(out) + RAMP_HINGE_UP);
            double run = out - RAMP_HINGE_OUT;
            double tilt = Math.atan2(rise, run);
            if (Math.hypot(run, rise) > RAMP_LENGTH - RAMP_LAND || tilt < -RAMP_MAX_DROP) {
                continue; // it would not reach onto the walkway, or swing down that far
            }
            tilt = Math.abs(tilt);
            if (tilt < bestTilt - 1e-9 || tilt < bestTilt + 1e-9 && Math.abs(out - TOWER_OUT) < Math.abs(best - TOWER_OUT)) {
                best = out;
                bestTilt = tilt;
            }
        }
        return best;
    }
    /** Its crew pushes from this far behind its centre: just behind the ladders up its back. */
    public static final double TOWER_BEHIND = 6.5;
    /** Door hit points a ram takes off a gate each second, for each soldier on it. */
    static final double RAM_PER_SOLDIER = 1.5;
    /** Super heavy infantry takes this share of the harm done it. */
    static final double SUPER_HEAVY_TAKES = 0.6;
    /** A block this close to where it is going is there. */
    static final double AT = 2.5;
    /** Defenders sent to a breach go after attackers this far from the gate, inside. */
    static final double BREACH_REACH = 40;
    /** The cavalry's wedge waits this far out from the gate it will charge. */
    static final double WEDGE_OUT = Siege.START;
    /** Rams come round the walls this far out. */
    static final double ROUND_OUT = 12;
    /** Artillery shots (a trebuchet's stone a point, {@link Machine#batter}) that break a stretch of wall open. */
    static final double WALL_HP = 40;
    /**
     * A mantlet block walks this far before the middle of its shooters, toward the wall; shooters within its cover (its
     * mantlets spread before up to four blocks abreast) take this share of arrows.
     */
    static final double MANTLET_AHEAD = 4;
    static final double MANTLET_COVER = 2.5 * BattlePlan.BLOCK_PITCH;
    static final double MANTLET_SHIELDS = 0.3;
    /** A wall post's block stands in one rank, this many wide. */
    static final int POST_FILES = 20;
    /** A block on a post with fewer than this share left is relieved. */
    static final double RELIEVE_UNDER = 0.5;

    /**
     * A gate: {@code mouth} just outside its doors, {@code button} its Gate Block's button inside, and ({@code outX},
     * {@code outZ}) the way out through it.
     */
    public record Gate(Point mouth, Point button, double outX, double outZ) {
    }

    /**
     * A fortress as surveyed: its walls' outer faces (a box), their thickness, the length along the attacked wall that
     * soldiers can stand on, the way out from that wall (a unit vector along x or z), its gates, where the defending
     * general holds their council, and the strategems the attacker brings.
     */
    public record Plan(double minX, double minZ, double maxX, double maxZ, double thickness, double wallLength,
                       double outX, double outZ, List<Gate> gates, Point hall, Set<Engine> engines, List<WallTower> towers,
                       List<Point> barred) {
        /** A fortress whose walls have no towers of their own (or none surveyed), every wall open to attack. */
        public Plan(double minX, double minZ, double maxX, double maxZ, double thickness, double wallLength,
                    double outX, double outZ, List<Gate> gates, Point hall, Set<Engine> engines) {
            this(minX, minZ, maxX, maxZ, thickness, wallLength, outX, outZ, gates, hall, engines, List.of(), List.of());
        }

        /** The same, with its walls' own towers. */
        public Plan(double minX, double minZ, double maxX, double maxZ, double thickness, double wallLength,
                    double outX, double outZ, List<Gate> gates, Point hall, Set<Engine> engines, List<WallTower> towers) {
            this(minX, minZ, maxX, maxZ, thickness, wallLength, outX, outZ, gates, hall, engines, towers, List.of());
        }

        /** Whether the wall the way out ({@code outX}, {@code outZ}) leads from can't be attacked (its {@code barred}). */
        boolean barred(double outX, double outZ) {
            return barred.stream().anyMatch(o -> Math.round(o.x()) == Math.round(outX) && Math.round(o.z()) == Math.round(outZ));
        }

        public Point centre() {
            return new Point((minX + maxX) / 2, (minZ + maxZ) / 2);
        }

        /** The middle of the attacked wall's outer face. */
        public Point wallMid() {
            Point c = centre();
            return new Point(outX > 0 ? maxX : outX < 0 ? minX : c.x(), outZ > 0 ? maxZ : outZ < 0 ? minZ : c.z());
        }

        /** Where to centre the {@link FieldBattle} so the defenders' front falls on the attacked wall, and its axis. */
        public Point battleCentre() {
            return wallMid().plus(outX * BattlePlan.DEPLOY_DISTANCE, outZ * BattlePlan.DEPLOY_DISTANCE);
        }

        public double[] axis() {
            return new double[]{-outX, -outZ};
        }
    }

    /** One of a wall's own towers, from end to end along its outer face. */
    public record WallTower(Point from, Point to) {
    }

    /** The share of the attacking army at the main wall, at each wall beside it, and at the far wall. */
    public static final double MAIN_SHARE = 0.35;
    public static final double SIDE_SHARE = 0.225;
    public static final double FAR_SHARE = 0.20;

    /**
     * One wall the attackers come at, the main one first: its share of the army and of its strategems, and that share's
     * blocks by what each does; its own gate (-1: none); where its artillery batters it, how much of that still stands, and
     * its breach once open (-1); and the camp its blocks fall back to.
     */
    public static final class Front {
        final Face face;
        final boolean main;
        double share;
        final int gate;
        double batteredRight;
        double wallLeft = WALL_HP;
        int breach = -1;
        @Nullable
        Point camp;
        final List<FieldBlock> blocks = new ArrayList<>();
        final List<FieldBlock> foot = new ArrayList<>();
        final List<FieldBlock> cavalry = new ArrayList<>();
        final List<FieldBlock> towers = new ArrayList<>();
        final List<FieldBlock> rams = new ArrayList<>();
        final List<FieldBlock> mantlets = new ArrayList<>();
        final List<FieldBlock> shooters = new ArrayList<>();
        final List<FieldBlock> artillery = new ArrayList<>();
        /** Its foot with nothing to climb: they wait until its wall or a gate is broken open. */
        final List<FieldBlock> waiting = new ArrayList<>();

        Front(Face face, boolean main, double share, int gate) {
            this.face = face;
            this.main = main;
            this.share = share;
            this.gate = gate;
        }

        /** The way out from its wall: its blocks face the other way, at it. */
        public double outX() {
            return face.outX();
        }

        public double outZ() {
            return face.outZ();
        }

        public boolean main() {
            return main;
        }

        /** The middle of its wall's outer face, and how long a stretch of it is attacked. */
        public Point mid() {
            return face.mid();
        }

        public double length() {
            return face.length();
        }

        /** Whether its wall is broken open. */
        public boolean breached() {
            return breach >= 0;
        }

        public double share() {
            return share;
        }

        /** Every block of its share of the army. */
        public List<FieldBlock> blocks() {
            return blocks;
        }

        /** Where its blocks camp, {@code gap} behind the furthest out of them, across from their middle; null with none. */
        @Nullable
        public Point campSpot(double gap) {
            if (blocks.isEmpty()) {
                return null;
            }
            double out = 0;
            double right = 0;
            for (FieldBlock b : blocks) {
                Point p = BattlePlan.offset(face.mid(), face.outX(), face.outZ(), b.deployed);
                out = Math.max(out, -p.z());
                right += p.x();
            }
            return face.at(right / blocks.size(), -(out + gap));
        }

        /** Its camp, as built: where its shattered blocks retreat. */
        public void setCamp(Point at) {
            camp = at;
        }
    }

    /** Where a place is: outside the walls, on them, or inside them. */
    public enum Region { OUT, WALL, IN }

    public enum State { CARRIED, RAISING, UP }

    /** A ladder, tower or ram, and the blocks that go with it: the first carries it; the rest climb after. */
    public static final class Work {
        final Engine kind;
        /** Where it stands against the wall (outside), where its climbers make for on the walkway, and its gate (rams). */
        Point foot;
        /** A tower's stand-off: how far out from the wall's face its centre stops. */
        double out = TOWER_OUT;
        final Point top;
        final int gate;
        final List<Point> route = new ArrayList<>();
        final List<FieldBlock> crews = new ArrayList<>();
        /** The wall it is brought against (a ram: the wall its crew came from). */
        final Front front;
        /** A tower's middle now, and where it set out from each morning. */
        Point at;
        Point start;
        State state = State.CARRIED;
        int since;
        int leg;

        Work(Engine kind, Point foot, Point top, int gate, Front front) {
            this.kind = kind;
            this.foot = foot;
            this.top = top;
            this.gate = gate;
            this.front = front;
        }

        public Engine kind() {
            return kind;
        }

        /** The way out from the wall it is brought against: a ladder or tower faces the other way. */
        public double outX() {
            return front.outX();
        }

        public double outZ() {
            return front.outZ();
        }

        public Point foot() {
            return foot;
        }

        public double out() {
            return out;
        }

        /** A tower stops {@code out} from the wall's face instead, where its ramp comes down best. */
        public void standOff(double out) {
            foot = foot.plus(outX() * (out - this.out), outZ() * (out - this.out));
            this.out = out;
        }

        public Point top() {
            return top;
        }

        public State state() {
            return state;
        }

        /** Where a tower stands now (its middle); null for a ladder or ram. */
        @Nullable
        public Point at() {
            return at;
        }

        public int gate() {
            return gate;
        }

        /** The block carrying it (the first of its crews still standing), or null. */
        @Nullable
        public FieldBlock carrier() {
            for (FieldBlock b : crews) {
                if (b.alive > 0) {
                    return b;
                }
            }
            return null;
        }

        public List<FieldBlock> crews() {
            return crews;
        }
    }

    /** A gate's state: hit points left (both doors), whether it stands open, and whether it is smashed. */
    public static final class GateState {
        final Gate gate;
        double hp = 2000;
        boolean open;
        boolean broken;

        GateState(Gate gate) {
            this.gate = gate;
        }

        public Gate gate() {
            return gate;
        }

        public boolean open() {
            return open;
        }

        public boolean broken() {
            return broken;
        }

        boolean breached() {
            return open || broken;
        }
    }

    /** Something done to a gate by soldiers who are only numbers, for the live battle to do to the real gate. */
    public record GateAction(int gate, Kind kind, double amount) {
        public enum Kind { RAM, OPEN, CLOSE }
    }

    /** A place on the wall a defending block holds, and the block holding it. */
    private static final class Post {
        final Point at;
        final boolean archers;
        /** The way out through its wall, which its holder faces. */
        final double outX;
        final double outZ;
        @Nullable
        FieldBlock holder;

        Post(Point at, boolean archers, double outX, double outZ) {
            this.at = at;
            this.archers = archers;
            this.outX = outX;
            this.outZ = outZ;
        }

        /** {@code b} takes this post: it stands there, facing out over the wall. */
        void take(FieldBlock b) {
            holder = b;
            b.facingX = outX;
            b.facingZ = outZ;
        }
    }

    final Plan plan;
    private final Point wallMid;
    private final List<GateState> gates = new ArrayList<>();
    private final List<Work> works = new ArrayList<>();
    /** How fast its towers roll, blocks a second: the real towers' own speed when Siegeworks says. */
    private double towerSpeed = TOWER_SPEED;
    private final List<Post> posts = new ArrayList<>();
    /** Defenders waiting inside, and where each waits. */
    private final List<FieldBlock> reserves = new ArrayList<>();
    private final java.util.Map<FieldBlock, Point> homes = new java.util.HashMap<>();
    private final List<FieldBlock> superHeavy = new ArrayList<>();
    private final List<FieldBlock> riders = new ArrayList<>();
    private final Set<FieldBlock> pressed = new java.util.HashSet<>();
    private final List<GateAction> actions = new ArrayList<>();
    private final List<FieldBattle.Event> events = new ArrayList<>();
    /** The gate the main attack makes for (and the defenders guard most): the nearest to the main wall's middle (-1: none). */
    private final int target;
    private int openSeconds;
    private int insideSeconds;
    @Nullable
    private String taken;
    /** Each mantlet block, the attacking shooters it screens; and each screened shooter's mantlet. */
    private final java.util.Map<FieldBlock, List<FieldBlock>> screens = new java.util.HashMap<>();
    private final java.util.Map<FieldBlock, FieldBlock> screenOf = new java.util.HashMap<>();
    /** The train's ram and tower crews done with their work, and any it couldn't use: they stand by. */
    private final List<FieldBlock> standBy = new ArrayList<>();
    /** The walls the attackers come at, the main one first; and each attacking block's. */
    private final List<Front> fronts = new ArrayList<>();
    private final java.util.Map<FieldBlock, Front> frontOf = new java.util.HashMap<>();

    public Siege(Plan plan) {
        this.plan = plan;
        this.wallMid = plan.wallMid();
        for (Gate g : plan.gates()) {
            gates.add(new GateState(g));
        }
        target = nearestRealGate(wallMid);
        // The army surrounds the walls: the main attack's, those beside it, the far one; none it can't come at.
        double total = 0;
        for (Face f : faces()) {
            boolean main = fronts.isEmpty();
            if (!main && plan.barred(f.outX(), f.outZ())) {
                continue;
            }
            double dot = f.outX() * plan.outX() + f.outZ() * plan.outZ();
            double share = main ? MAIN_SHARE : dot < -0.5 ? FAR_SHARE : SIDE_SHARE;
            fronts.add(new Front(f, main, share, gateIn(f)));
            total += share;
        }
        for (Front f : fronts) {
            f.share /= total; // a barred wall's share goes to the others, as their own shares stand
            f.batteredRight = spots(f, 1).get(0).x(); // the wall's middle, or beside its gate
        }
    }

    /** The gate in a wall: the nearest to its middle of those whose mouths lie on its face (-1: none). */
    private int gateIn(Face f) {
        int best = -1;
        for (int i = 0; i < gates.size(); i++) {
            Point mouth = BattlePlan.offset(f.mid(), f.outX(), f.outZ(), gates.get(i).gate.mouth());
            if (Math.abs(mouth.z()) < 6 && Math.abs(mouth.x()) <= f.length() / 2 + 6
                    && (best < 0 || gates.get(i).gate.mouth().distanceTo(f.mid()) < gates.get(best).gate.mouth().distanceTo(f.mid()))) {
                best = i;
            }
        }
        return best;
    }

    /** The gate nearest a place by its mouth, of those not a breach (-1: none). */
    private int nearestRealGate(Point p) {
        int best = -1;
        for (int i = 0; i < plan.gates().size(); i++) {
            if (best < 0 || gates.get(i).gate.mouth().distanceTo(p) < gates.get(best).gate.mouth().distanceTo(p)) {
                best = i;
            }
        }
        return best;
    }

    /** The real gate nearest a place by its mouth that no ram is bound for yet (-1: none). */
    private int freeGate(Point p) {
        int best = -1;
        for (int i = 0; i < plan.gates().size(); i++) {
            int g = i;
            if (works.stream().noneMatch(w -> w.kind == Engine.RAMS && w.gate == g)
                    && (best < 0 || gates.get(i).gate.mouth().distanceTo(p) < gates.get(best).gate.mouth().distanceTo(p))) {
                best = i;
            }
        }
        return best;
    }

    // ---- where things are ----

    public Region region(Point p) {
        double t = plan.thickness();
        if (p.x() < plan.minX() || p.x() > plan.maxX() || p.z() < plan.minZ() || p.z() > plan.maxZ()) {
            return Region.OUT;
        }
        boolean inside = p.x() > plan.minX() + t && p.x() < plan.maxX() - t && p.z() > plan.minZ() + t && p.z() < plan.maxZ() - t;
        return inside ? Region.IN : Region.WALL;
    }

    /** A place relative to the attacked wall: {@code right} along it (facing out), {@code in} inward from its outer face. */
    private Point onWall(double right, double in) {
        return BattlePlan.place(wallMid, plan.outX(), plan.outZ(), right, in);
    }

    private boolean anyBreach() {
        return gates.stream().anyMatch(GateState::breached);
    }

    @Nullable
    private Work workOf(FieldBlock b) {
        for (Work w : works) {
            if (w.crews.contains(b)) {
                return w;
            }
        }
        return null;
    }

    // ---- deployment ----

    /**
     * Lays both armies out for the siege: the defenders on every wall and close inside them, the attackers round the
     * walls by their shares, each wall's foot given its strategems to carry, its cavalry waiting for a way in.
     */
    void deploy(FieldArmy a, FieldArmy d) {
        deployDefenders(d);
        deployAttackers(a);
    }

    /** One wall of the box: the middle of its outer face, the way out through it, and how long a stretch of it is manned. */
    private record Face(Point mid, double outX, double outZ, double length) {
        Point at(double right, double in) {
            return BattlePlan.place(mid, outX, outZ, right, in);
        }
    }

    /** The box's walls, the attacked one first, each other one manned short of its corners by the walls' thickness. */
    private List<Face> faces() {
        List<Face> faces = new ArrayList<>(List.of(new Face(wallMid, plan.outX(), plan.outZ(), plan.wallLength())));
        Point c = plan.centre();
        double t = plan.thickness();
        for (int[] o : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
            if (o[0] == Math.round(plan.outX()) && o[1] == Math.round(plan.outZ())) {
                continue;
            }
            Point mid = new Point(o[0] > 0 ? plan.maxX() : o[0] < 0 ? plan.minX() : c.x(), o[1] > 0 ? plan.maxZ() : o[1] < 0 ? plan.minZ() : c.z());
            double side = o[0] != 0 ? plan.maxZ() - plan.minZ() : plan.maxX() - plan.minX();
            faces.add(new Face(mid, o[0], o[1], side - 2 * (t + 1)));
        }
        return faces;
    }

    /** Reserves wait in rings in from the walls: the first this far in from a wall's inner face, the next this far behind. */
    static final double RESERVE_IN = 3;
    static final double RESERVE_ROW = 5;

    /**
     * Where {@code n} reserve blocks wait, and which way each faces (toward its wall): as close to the walls as they can
     * stand, ring after ring in from every wall, those nearest the attacked wall first. Any the rings can't hold wait in
     * rows behind the attacked wall, kept inside.
     */
    private List<Point[]> reserveSpots(int n) {
        double t = plan.thickness();
        double pitch = BattlePlan.BLOCK_PITCH;
        List<Point[]> spots = new ArrayList<>();
        for (int ring = 0; spots.size() < n; ring++) {
            double in = t + RESERVE_IN + ring * RESERVE_ROW;
            List<Point[]> round = new ArrayList<>();
            for (Face f : faces()) {
                // Along the inside of this wall at this depth, half a block's width clear of the next wall's ring.
                double side = f.outX() != 0 ? plan.maxZ() - plan.minZ() : plan.maxX() - plan.minX();
                double span = side - 2 * in - pitch;
                for (int k = 0, count = span < 0 ? 0 : (int) (span / pitch) + 1; k < count; k++) {
                    round.add(new Point[]{f.at((k - (count - 1) / 2.0) * pitch, in), new Point(f.outX(), f.outZ())});
                }
            }
            if (round.isEmpty()) {
                break; // the rings have met in the middle
            }
            round.sort(Comparator.comparingDouble(s -> s[0].distanceTo(wallMid)));
            spots.addAll(round);
        }
        int across = Math.max(1, (int) Math.floor(plan.wallLength() / pitch));
        for (int j = 0; spots.size() < n; j++) {
            spots.add(new Point[]{within(onWall((j % across - (across - 1) / 2.0) * pitch, t + RESERVE_IN + (j / across) * RESERVE_ROW)),
                    new Point(plan.outX(), plan.outZ())});
        }
        return spots;
    }

    private void deployDefenders(FieldArmy d) {
        double t = plan.thickness();
        double pitch = BattlePlan.pitch(POST_FILES);
        // Archers just behind the merlons (mid second row, so none stand on them), foot behind them on the walkway; on a
        // wall too thin for both, the foot at its foot inside. Every wall is manned, the attacked one first.
        double archerIn = Math.min(1.5, t - 0.5);
        double footIn = t >= 3.5 ? t - 1.75 : t >= 3 ? t - 0.5 : t + 1.5;
        for (Face f : faces()) {
            int perRow = Math.max(1, (int) Math.floor(f.length() / pitch));
            for (boolean shooters : new boolean[]{true, false}) {
                for (int i = 0; i < perRow; i++) {
                    posts.add(new Post(f.at((i - (perRow - 1) / 2.0) * pitch, shooters ? archerIn : footIn), shooters, f.outX(), f.outZ()));
                }
            }
        }
        List<FieldBlock> archers = new ArrayList<>();
        List<FieldBlock> foot = new ArrayList<>();
        List<FieldBlock> cavalry = new ArrayList<>();
        for (FieldBlock b : d.blocks) {
            if (b.duty == Duty.GENERAL || b.duty == Duty.GUARD || b.duty == Duty.BEARER || b.afloat) {
                continue;
            }
            (b.arm.shoots() ? archers : b.arm.mounted() ? cavalry : foot).add(b);
        }
        for (Post p : posts) {
            List<FieldBlock> from = p.archers && !archers.isEmpty() ? archers : !foot.isEmpty() ? foot : archers;
            if (from.isEmpty()) {
                continue;
            }
            p.take(from.remove(0));
            p.holder.duty = Duty.FRONT;
            p.holder.homeDuty = Duty.FRONT;
            p.holder.files = POST_FILES;
            put(p.holder, p.at);
        }
        // The rest wait inside, as close to the walls as they can stand, facing them: the foot nearest, then the archers.
        List<FieldBlock> waiting = new ArrayList<>(foot);
        waiting.addAll(archers);
        List<Point[]> spots = reserveSpots(waiting.size());
        for (int i = 0; i < waiting.size(); i++) {
            FieldBlock b = waiting.get(i);
            Point at = spots.get(i)[0];
            b.duty = Duty.LINE;
            b.homeDuty = Duty.LINE;
            b.facingX = spots.get(i)[1].x();
            b.facingZ = spots.get(i)[1].z();
            put(b, at);
            reserves.add(b);
            homes.put(b, at);
        }
        // Nine in ten of the cavalry dismount as super heavy infantry, to hold the gate; the rest stay mounted behind them.
        Gate gate = target >= 0 ? gates.get(target).gate : null;
        Point gateIn = gate == null ? plan.centre() : gate.button();
        double inX = gate == null ? -plan.outX() : -gate.outX();
        double inZ = gate == null ? -plan.outZ() : -gate.outZ();
        int mounted = (int) Math.round(cavalry.size() / 10.0);
        for (int i = 0; i < cavalry.size(); i++) {
            FieldBlock b = cavalry.get(i);
            boolean rides = i >= cavalry.size() - mounted;
            int k = rides ? i - (cavalry.size() - mounted) : i;
            double back = rides ? 14 : 4 + (k / 2) * 5;
            double side = (k % 2 == 0 ? -1 : 1) * BattlePlan.BLOCK_PITCH / 2;
            Point at = within(gateIn.plus(inX * back - inZ * side, inZ * back + inX * side));
            if (!rides) {
                b.arm = Arm.SKIRMISHERS;
                b.superHeavy = true;
                superHeavy.add(b);
            } else {
                riders.add(b);
            }
            b.duty = Duty.SUPPORT;
            b.homeDuty = Duty.SUPPORT;
            put(b, at);
            homes.put(b, at);
        }
        // The general holds council in the hall (the barracks, or the keep), the guard and the standard by them.
        Point hall = plan.hall();
        if (d.commander != null) {
            put(d.commander, hall);
        }
        if (d.guard != null) {
            put(d.guard, hall.plus(inX * 4, inZ * 4));
        }
        if (d.bearer != null) {
            put(d.bearer, hall.plus(-inZ * 2, inX * 2));
        }
    }

    /**
     * How far out from a wall's face the attackers start, front row and towers alike; the line their shooters and
     * artillery then march up to, in bowshot of the walls; and a row of blocks behind the one before it.
     */
    static final double START = 50;
    static final double FIRING_LINE = 25;
    static final double TOWER_START = START;
    static final double ROW = 6;

    /**
     * The attackers surround the walls: every kind of block (foot, cavalry, shooters, artillery, and each engine of the
     * train) shared between the walls they come at by their shares, as are the ladders, then each wall's force laid out
     * before it ({@link #deployFront}). The general, their guard and the standard stay behind the main attack.
     */
    private void deployAttackers(FieldArmy a) {
        List<FieldBlock> foot = new ArrayList<>();
        List<FieldBlock> cavalry = new ArrayList<>();
        List<FieldBlock> towers = new ArrayList<>();
        List<FieldBlock> rams = new ArrayList<>();
        List<FieldBlock> mantlets = new ArrayList<>();
        List<FieldBlock> shooters = new ArrayList<>();
        List<FieldBlock> artillery = new ArrayList<>();
        for (FieldBlock b : a.blocks) {
            if (b.duty == Duty.GENERAL || b.duty == Duty.GUARD || b.duty == Duty.BEARER || b.afloat) {
                continue;
            }
            if (b.arm.mounted()) {
                cavalry.add(b);
            } else if (b.machine == Machine.SIEGE_TOWER) {
                towers.add(b);
            } else if (b.machine == Machine.BATTERING_RAM) {
                rams.add(b);
            } else if (b.machine == Machine.MANTLET) {
                mantlets.add(b);
            } else if (b.arm == Arm.ENGINES && b.machine != null) {
                artillery.add(b);
            } else if (b.arm.shoots()) {
                shooters.add(b);
            } else {
                foot.add(b); // the infantry, and a train with no engines to work, fighting as light troops
            }
        }
        while (rams.size() > Math.min(MAX_RAMS, plan.gates().size())) {
            foot.add(rams.remove(rams.size() - 1));
        }
        deal(towers, f -> f.towers);
        for (Front f : fronts) {
            while (f.towers.size() > TOWERS_PER_WALL) {
                FieldBlock spare = f.towers.remove(f.towers.size() - 1);
                f.blocks.remove(spare);
                foot.add(spare);
            }
        }
        deal(foot, f -> f.foot);
        deal(cavalry, f -> f.cavalry);
        deal(rams, f -> f.rams);
        deal(mantlets, f -> f.mantlets);
        deal(shooters, f -> f.shooters);
        deal(artillery, f -> f.artillery);
        boolean ladders = plan.engines().isEmpty() || plan.engines().contains(Engine.LADDERS);
        int[] ladderShares = split(ladders ? Math.min(LADDERS, foot.size()) : 0);
        for (int i = 0; i < fronts.size(); i++) {
            deployFront(fronts.get(i), ladderShares[i]);
        }
    }

    /** Deals {@code blocks} out to the walls by their shares, in whole blocks and in order, into each's {@code kind}. */
    private void deal(List<FieldBlock> blocks, java.util.function.Function<Front, List<FieldBlock>> kind) {
        int[] counts = split(blocks.size());
        int next = 0;
        for (int i = 0; i < fronts.size(); i++) {
            Front f = fronts.get(i);
            for (int j = 0; j < counts[i]; j++) {
                FieldBlock b = blocks.get(next++);
                kind.apply(f).add(b);
                f.blocks.add(b);
                frontOf.put(b, f);
            }
        }
    }

    /** {@code n} shared between the walls by their shares, in whole numbers: the largest remainders round up, the main wall's first. */
    int[] split(int n) {
        int[] out = new int[fronts.size()];
        double[] rest = new double[out.length];
        int given = 0;
        for (int i = 0; i < out.length; i++) {
            double exact = n * fronts.get(i).share;
            out[i] = (int) Math.floor(exact + 1e-9);
            rest[i] = exact - out[i];
            given += out[i];
        }
        while (given < n) {
            int best = 0;
            for (int i = 1; i < out.length; i++) {
                best = rest[i] > rest[best] ? i : best;
            }
            out[best]++;
            rest[best] = -1;
            given++;
        }
        return out;
    }

    /**
     * One wall's force, before its wall and facing it: its foot carrying its share of the ladders (the rest following up
     * them and its towers), each tower starting straight out from its spot, each ram making for the nearest gate, the
     * mantlets before the shooters; and in rows, the shooters' line in bowshot, the artillery and rams behind, then the
     * foot, then the cavalry (unless it waits in a wedge before this wall's own gate) and those standing by.
     */
    private void deployFront(Front f, int ladderCount) {
        Face face = f.face;
        List<FieldBlock> carriers = f.foot.subList(0, Math.min(ladderCount, f.foot.size()));
        List<Point> ladderSpots = spots(f, carriers.size());
        for (int i = 0; i < carriers.size(); i++) {
            Work w = climbWork(f, Engine.LADDERS, ladderSpots.get(i), 1.5);
            w.crews.add(carriers.get(i));
            works.add(w);
        }
        List<Point> towerSpots = towerSpots(f, f.towers.size());
        for (int i = 0; i < f.towers.size(); i++) {
            FieldBlock crew = f.towers.get(i);
            Work w = climbWork(f, Engine.TOWERS, towerSpots.get(i), TOWER_OUT);
            w.crews.add(crew);
            works.add(w);
            double right = towerSpots.get(i).x();
            w.at = w.start = face.at(right, -TOWER_START);
            put(crew, face.at(right, -(TOWER_START + TOWER_BEHIND)));
            faceWall(crew, f);
        }
        List<FieldBlock> idle = new ArrayList<>();
        for (FieldBlock ram : f.rams) {
            int gate = freeGate(face.mid());
            if (gate < 0) {
                idle.add(ram); // no gate left to ram: it stands by
                continue;
            }
            Work w = ramWork(f, gate);
            w.crews.add(ram);
            works.add(w);
        }
        List<Work> climbs = works.stream().filter(w -> w.front == f && w.kind != Engine.RAMS).toList();
        for (int i = carriers.size(); i < f.foot.size(); i++) {
            if (climbs.isEmpty()) {
                f.waiting.add(f.foot.get(i)); // nothing to climb: they wait for a way in
            } else {
                climbs.get(i % climbs.size()).crews.add(f.foot.get(i));
            }
        }
        for (int i = 0; i < f.shooters.size() && i / Machine.MANTLET.perBlock() < f.mantlets.size(); i++) {
            FieldBlock m = f.mantlets.get(i / Machine.MANTLET.perBlock());
            screens.computeIfAbsent(m, k -> new ArrayList<>()).add(f.shooters.get(i));
            screenOf.put(f.shooters.get(i), m);
        }
        f.mantlets.stream().filter(m -> !screens.containsKey(m)).forEach(idle::add);
        standBy.addAll(idle);

        Rows rows = new Rows(f, START);
        rows.place(f.shooters, ROW);
        rows.place(f.artillery, ROW);
        rows.place(f.rams.stream().filter(r -> !idle.contains(r)).toList(), ROW);
        rows.place(f.foot, ROW);
        if (f.gate < 0) {
            rows.place(f.cavalry, BattlePlan.CAVALRY_ROW);
        } else {
            // Its cavalry forms a wedge before its gate, to charge it once it opens.
            Gate g = gates.get(f.gate).gate;
            Point tip = g.mouth().plus(g.outX() * WEDGE_OUT, g.outZ() * WEDGE_OUT);
            for (int i = 0; i < f.cavalry.size(); i++) {
                double[] slot = BattlePlan.wedgeSlot(i);
                FieldBlock b = f.cavalry.get(i);
                put(b, BattlePlan.place(tip, -g.outX(), -g.outZ(), slot[0], slot[1]));
                b.facingX = -g.outX();
                b.facingZ = -g.outZ();
            }
        }
        rows.place(idle, ROW);
        // Each mantlet block before the middle of the shooters it screens.
        for (FieldBlock m : f.mantlets) {
            List<FieldBlock> shot = screens.get(m);
            if (shot != null) {
                double x = shot.stream().mapToDouble(s -> s.pos.x()).average().orElse(m.pos.x());
                double z = shot.stream().mapToDouble(s -> s.pos.z()).average().orElse(m.pos.z());
                put(m, new Point(x - f.outX() * MANTLET_AHEAD, z - f.outZ() * MANTLET_AHEAD));
                faceWall(m, f);
            }
        }
    }

    /**
     * Places in rows before a wall, from {@code out} on: each row as wide as the wall and as far again either side as it
     * stands out from it, so the walls' forces never meet at the corners.
     */
    private static final class Rows {
        private final Front f;
        private double out;
        private int k;
        private double lastRow;

        Rows(Front f, double out) {
            this.f = f;
            this.out = out;
        }

        private int across() {
            return Math.max(1, (int) ((f.face.length() + 2 * out) / BattlePlan.BLOCK_PITCH));
        }

        /** {@code blocks} in rows {@code row} apart, after (a row behind) whoever was placed before them, facing the wall. */
        void place(List<FieldBlock> blocks, double row) {
            if (blocks.isEmpty()) {
                return;
            }
            if (k > 0) {
                out += lastRow;
                k = 0;
            }
            lastRow = row;
            for (FieldBlock b : blocks) {
                int n = across();
                if (k == n) {
                    out += row;
                    k = 0;
                    n = across();
                }
                put(b, f.face.at((k - (n - 1) / 2.0) * BattlePlan.BLOCK_PITCH, -out));
                faceWall(b, f);
                k++;
            }
        }
    }

    /** A block of {@code f}'s turned to face its wall. */
    private static void faceWall(FieldBlock b, Front f) {
        b.facingX = -f.outX();
        b.facingZ = -f.outZ();
    }

    /** Attackers deploy at least this far out from the walls. */
    static final double CLEAR = 6;

    /** Evenly along a wall, {@code n} of them, kept clear of its gates. Each is {right} along the wall. */
    private List<Point> spots(Front f, int n) {
        List<Point> spots = new ArrayList<>();
        double length = f.face.length();
        for (int k = 0; k < n; k++) {
            double right = -length / 2 + length * (k + 0.5) / n;
            for (GateState g : gates) {
                Point mouth = BattlePlan.offset(f.face.mid(), f.outX(), f.outZ(), g.gate.mouth());
                if (Math.abs(mouth.z()) < 4 && Math.abs(right - mouth.x()) < 6) {
                    right = mouth.x() + (right < mouth.x() ? -6 : 6); // beside the gate, not in front of it
                }
            }
            spots.add(new Point(right, 0));
        }
        return spots;
    }

    /** A siege tower is this wide either side of its middle, and keeps this far besides clear of a wall tower or gate. */
    static final double TOWER_HALF = 3;
    static final double TOWER_CLEAR = 1;

    /**
     * Where {@code n} siege towers come at a wall: evenly along the stretches of it between its own towers (and its
     * gates), so each bridge comes down on a walkway, never against a wall tower's side. Without room for one anywhere,
     * as {@link #spots}.
     */
    private List<Point> towerSpots(Front f, int n) {
        double half = f.face.length() / 2;
        double keep = TOWER_HALF + TOWER_CLEAR;
        List<double[]> blocked = new ArrayList<>();
        for (WallTower t : plan.towers()) {
            Point a = BattlePlan.offset(f.face.mid(), f.outX(), f.outZ(), t.from());
            Point b = BattlePlan.offset(f.face.mid(), f.outX(), f.outZ(), t.to());
            if (Math.abs(a.z()) < 3 && Math.abs(b.z()) < 3) { // on this wall
                blocked.add(new double[]{Math.min(a.x(), b.x()) - keep, Math.max(a.x(), b.x()) + keep});
            }
        }
        for (GateState g : gates) {
            Point mouth = BattlePlan.offset(f.face.mid(), f.outX(), f.outZ(), g.gate.mouth());
            if (Math.abs(mouth.z()) < 4) {
                blocked.add(new double[]{mouth.x() - 6, mouth.x() + 6});
            }
        }
        blocked.sort(Comparator.comparingDouble(s -> s[0]));
        // The stretches left, end to end, short of the wall's ends by a tower's half.
        List<double[]> free = new ArrayList<>();
        double from = -half + TOWER_HALF;
        for (double[] s : blocked) {
            if (s[0] > from) {
                free.add(new double[]{from, Math.min(s[0], half - TOWER_HALF)});
            }
            from = Math.max(from, s[1]);
        }
        if (from < half - TOWER_HALF) {
            free.add(new double[]{from, half - TOWER_HALF});
        }
        free.removeIf(s -> s[1] <= s[0]);
        double total = free.stream().mapToDouble(s -> s[1] - s[0]).sum();
        if (n == 0 || total <= 0) {
            return spots(f, n);
        }
        List<Point> spots = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            double along = total * (k + 0.5) / n;
            for (double[] s : free) {
                if (along <= s[1] - s[0]) {
                    spots.add(new Point(s[0] + along, 0));
                    break;
                }
                along -= s[1] - s[0];
            }
        }
        return spots;
    }

    /** A ladder or tower at {@code spot} along a wall, standing {@code out} out from its face. */
    private Work climbWork(Front f, Engine kind, Point spot, double out) {
        return new Work(kind, f.face.at(spot.x(), -out), f.face.at(spot.x(), plan.thickness() / 2), -1, f);
    }

    /** A ram, its way round the walls to gate {@code gate} from wall {@code f}'s side. */
    private Work ramWork(Front f, int gate) {
        Gate g = gates.get(gate).gate;
        Work w = new Work(Engine.RAMS, g.mouth(), g.mouth(), gate, f);
        Point c = plan.centre();
        double hx = (plan.maxX() - plan.minX()) / 2 + ROUND_OUT;
        double hz = (plan.maxZ() - plan.minZ()) / 2 + ROUND_OUT;
        double ox = f.outX();
        double oz = f.outZ();
        double dot = ox * g.outX() + oz * g.outZ();
        if (dot < 0.5) {
            // Round a corner (two, for the far side): the corner between its side and the next toward the gate.
            double rx = -oz;
            double rz = ox;
            boolean viaRight = dot < -0.5 || rx * g.outX() + rz * g.outZ() > 0;
            double sx = viaRight ? rx : -rx;
            double sz = viaRight ? rz : -rz;
            w.route.add(new Point(c.x() + (ox + sx) * hx, c.z() + (oz + sz) * hz));
            if (dot < -0.5) {
                w.route.add(new Point(c.x() + (sx + g.outX()) * hx, c.z() + (sz + g.outZ()) * hz));
            }
        }
        w.route.add(g.mouth().plus(g.outX() * 10, g.outZ() * 10));
        w.route.add(g.mouth());
        return w;
    }

    /** A place pulled inside the walls (a crowded castle packs its reserves close). */
    private Point within(Point p) {
        double t = plan.thickness() + 1;
        return new Point(Math.max(plan.minX() + t, Math.min(plan.maxX() - t, p.x())),
                Math.max(plan.minZ() + t, Math.min(plan.maxZ() - t, p.z())));
    }

    private static void put(FieldBlock b, Point at) {
        b.line = at;
        b.pos = at;
        b.deployed = at;
    }

    // ---- each second ----

    /** The defenders' general's orders for the second, in place of a field battle's. */
    void defend(FieldArmy d, FieldArmy a, int seconds) {
        for (FieldBlock b : d.blocks) {
            if (b.afloat || b.alive == 0) {
                continue;
            }
            b.approaching = false;
            b.strikeAt = null;
        }
        // Worn blocks on the wall are relieved by fresh ones from inside.
        for (Post p : posts) {
            FieldBlock h = p.holder;
            if (h != null && h.alive >= h.started() * RELIEVE_UNDER) {
                hold(h, p.at);
                continue;
            }
            FieldBlock fresh = nearest(reserves, p.at, b -> b.alive >= b.started() * RELIEVE_UNDER
                    && (!p.archers || b.arm.shoots() || reserves.stream().noneMatch(o -> o.arm.shoots() && o.alive > 0)));
            if (fresh == null) {
                if (h != null && h.alive > 0) {
                    hold(h, p.at); // nobody to send: they hold on
                }
                continue;
            }
            reserves.remove(fresh);
            fresh.duty = Duty.FRONT;
            fresh.files = POST_FILES;
            if (h != null && h.alive > 0) {
                h.duty = Duty.LINE;
                h.files = BattlePlan.FILE_WIDTH;
                h.facingX = fresh.facingX; // it takes the fresh block's place inside, facing as it did
                h.facingZ = fresh.facingZ;
                homes.put(h, homes.getOrDefault(fresh, h.deployed));
                reserves.add(h);
            }
            p.take(fresh);
            hold(fresh, p.at);
            events.add(new FieldBattle.Event(1, rafasconquest.data.Dialogue.say("battle.events.reinforce")));
        }
        for (FieldBlock b : reserves) {
            if (b.alive > 0) {
                hold(b, homes.getOrDefault(b, b.deployed));
            }
        }
        boolean breach = anyBreach() || insideSeconds > 0;
        // The super heavy infantry goes for attackers on the walls or inside near the gate; else it holds the gate, and
        // shuts it again if it stands open.
        for (FieldBlock b : superHeavy) {
            if (b.alive == 0) {
                continue;
            }
            Point home = homes.get(b);
            FieldBlock foe = nearestEnemy(a, home, BREACH_REACH, r -> r != Region.OUT);
            if (foe != null) {
                charge(b, foe);
                continue;
            }
            int open = openGateNear(home);
            hold(b, open >= 0 ? gates.get(open).gate.button() : home);
        }
        // The mounted few ride down whoever gets in once the gate is breached. (Ride Out: not yet decided.)
        for (FieldBlock b : riders) {
            if (b.alive == 0) {
                continue;
            }
            Point home = homes.get(b);
            FieldBlock foe = breach ? nearestEnemy(a, home, BREACH_REACH, r -> r == Region.IN) : null;
            if (foe == null && breach && target >= 0) {
                foe = nearestEnemy(a, gates.get(target).gate.mouth(), 12, r -> r != Region.WALL);
            }
            if (foe != null) {
                charge(b, foe);
            } else {
                hold(b, home);
            }
        }
        for (FieldBlock b : new FieldBlock[]{d.commander, d.guard, d.bearer}) {
            if (b != null && b.alive > 0) {
                hold(b, b.deployed);
            }
        }
        // Whoever stands at an open gate's button shuts it (those who are only numbers; real soldiers press it themselves).
        for (int i = 0; i < gates.size(); i++) {
            GateState g = gates.get(i);
            if (!g.open || g.broken) {
                continue;
            }
            for (FieldBlock b : d.blocks) {
                if (b.alive > 0 && !b.live && b.pos.distanceTo(g.gate.button()) <= AT) {
                    g.open = false;
                    actions.add(new GateAction(i, GateAction.Kind.CLOSE, 0));
                    events.add(new FieldBattle.Event(1, rafasconquest.data.Dialogue.say("battle.events.gate_closed")));
                    break;
                }
            }
        }
    }

    /** The attackers' orders for the second, after their general's: the strategems' crews and the wedge go their own way. */
    void attack(FieldArmy a, FieldArmy d, int seconds) {
        for (Work w : works) {
            FieldBlock carrier = w.carrier();
            if (carrier == null) {
                continue;
            }
            switch (w.state) {
                case CARRIED -> {
                    Point next = w.leg < w.route.size() ? w.route.get(w.leg) : w.foot;
                    boolean arrived;
                    if (w.kind == Engine.TOWERS) {
                        // Rolled straight at the wall while its crew is behind it pushing; it stops against the wall.
                        double left = w.at.distanceTo(w.foot);
                        if (carrier.pos.distanceTo(behind(w, 0)) <= AT * 2) {
                            w.at = left <= towerSpeed ? w.foot
                                    : w.at.plus((w.foot.x() - w.at.x()) / left * towerSpeed, (w.foot.z() - w.at.z()) / left * towerSpeed);
                        }
                        arrived = w.at == w.foot;
                    } else if (carrier.pos.distanceTo(next) <= AT && w.leg < w.route.size()) {
                        w.leg++;
                        arrived = false;
                    } else {
                        arrived = carrier.pos.distanceTo(w.foot) <= AT;
                    }
                    if (arrived) {
                        w.state = w.kind == Engine.RAMS ? State.UP : State.RAISING;
                        w.since = seconds;
                        events.add(new FieldBattle.Event(0, rafasconquest.data.Dialogue.say("battle.events." + switch (w.kind) {
                            case LADDERS -> "ladder_up";
                            case TOWERS -> "tower_up";
                            case RAMS -> "ram_at_gate";
                        })));
                    }
                }
                case RAISING -> {
                    if (seconds - w.since >= (w.kind == Engine.LADDERS ? HOIST_SECONDS : DOCK_SECONDS)) {
                        w.state = State.UP;
                    }
                }
                case UP -> {
                }
            }
            for (int i = 0; i < w.crews.size(); i++) {
                FieldBlock b = w.crews.get(i);
                if (b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED || b.duty == Duty.RALLYING) {
                    continue;
                }
                crewOrders(w, b, b == carrier, i, a, d);
            }
        }
        for (Front f : fronts) {
            // Each wall's cavalry waits (in a wedge before its own gate, or in its rows) until a way in opens, its own
            // wall's first; then rides through at whoever is inside. Its foot with nothing to climb follows it in.
            int via = wayIn(f);
            Gate g = via < 0 ? null : gates.get(via).gate;
            for (FieldBlock b : f.cavalry) {
                if (b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED || b.led) {
                    continue;
                }
                if (g == null) {
                    hold(b, b.deployed);
                } else if (region(b.pos) == Region.OUT && b.pos.distanceTo(g.mouth()) > AT) {
                    hold(b, g.mouth());
                } else {
                    FieldBlock foe = nearestEnemy(d, b.pos, Double.MAX_VALUE, r -> r == Region.IN); // horses can't go up onto the walls
                    if (foe != null) {
                        charge(b, foe);
                    } else {
                        hold(b, g.button());
                    }
                }
            }
            for (FieldBlock b : f.waiting) {
                if (b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED || b.duty == Duty.RALLYING || b.led) {
                    continue;
                }
                if (region(b.pos) != Region.OUT) {
                    inside(b, a, d);
                } else if (g == null) {
                    hold(b, b.deployed);
                } else {
                    hold(b, b.pos.distanceTo(g.mouth()) > AT ? g.mouth() : g.button());
                }
            }
            // Its shooters and artillery march up from where they started to their lines in bowshot, loosing at whatever is in reach.
            double up = START - FIRING_LINE;
            for (List<FieldBlock> line : List.of(f.shooters, f.artillery)) {
                for (FieldBlock b : line) {
                    if (b.alive > 0 && b.duty != Duty.FLEEING && b.duty != Duty.ROUTED && b.duty != Duty.RALLYING && !b.led) {
                        hold(b, b.deployed.plus(-f.outX() * up, -f.outZ() * up));
                    }
                }
            }
        }
        // Each mantlet block walks before its shooters, toward their wall; train blocks with nothing to do stand by.
        screens.forEach((m, shot) -> {
            List<FieldBlock> up = shot.stream().filter(s -> s.alive > 0 && s.duty != Duty.FLEEING && s.duty != Duty.ROUTED).toList();
            if (m.alive == 0 || m.duty == Duty.FLEEING || m.duty == Duty.ROUTED) {
                return;
            }
            if (up.isEmpty()) {
                hold(m, m.pos);
                return;
            }
            Front f = frontOf.get(m);
            double x = up.stream().mapToDouble(s -> s.pos.x()).average().orElse(m.pos.x());
            double z = up.stream().mapToDouble(s -> s.pos.z()).average().orElse(m.pos.z());
            hold(m, new Point(x - f.outX() * MANTLET_AHEAD, z - f.outZ() * MANTLET_AHEAD));
        });
        for (FieldBlock b : standBy) {
            if (b.alive > 0 && b.duty != Duty.FLEEING && b.duty != Duty.ROUTED) {
                hold(b, b.deployed);
            }
        }
    }

    /** The way in a wall's force makes for: its own gate or breach once open, else the nearest of any open (-1: none yet). */
    private int wayIn(Front f) {
        if (f.gate >= 0 && gates.get(f.gate).breached()) {
            return f.gate;
        }
        if (f.breach >= 0) {
            return f.breach;
        }
        int best = -1;
        for (int i = 0; i < gates.size(); i++) {
            if (gates.get(i).breached() && (best < 0
                    || gates.get(i).gate.mouth().distanceTo(f.face.mid()) < gates.get(best).gate.mouth().distanceTo(f.face.mid()))) {
                best = i;
            }
        }
        return best;
    }

    private void crewOrders(Work w, FieldBlock b, boolean carrier, int place, FieldArmy a, FieldArmy d) {
        Region at = region(b.pos);
        if (carrier && b.arm == Arm.ENGINES && w.state != State.CARRIED) {
            // A tower's or ram's crew stays with it once it is there: a tower's behind it, a ram's at the gate.
            hold(b, w.kind == Engine.TOWERS ? behind(w, 0) : w.foot);
            return;
        }
        if (w.front.breach >= 0 && at == Region.OUT && b.arm != Arm.ENGINES) {
            // The wall is open: through the breach, rather than up a ladder.
            Gate g = gates.get(w.front.breach).gate;
            hold(b, b.pos.distanceTo(g.mouth()) > AT ? g.mouth() : g.button());
            return;
        }
        if (w.kind == Engine.RAMS) {
            GateState g = gates.get(w.gate);
            if (!g.broken && !g.open) {
                // The carrier rams; any other waits behind it.
                Point next = w.state != State.CARRIED ? w.foot : w.leg < w.route.size() ? w.route.get(w.leg) : w.foot;
                hold(b, carrier ? next : next.plus(g.gate.outX() * (6 + 4 * place), g.gate.outZ() * (6 + 4 * place)));
                return;
            }
            inside(b, a, d);
            return;
        }
        if (w.state != State.UP) {
            // Carried up to the wall (a tower pushed from behind), the others close behind; then they wait at its foot
            // while it goes up.
            hold(b, w.kind == Engine.TOWERS ? behind(w, place) : w.foot.plus(w.outX() * 6 * place, w.outZ() * 6 * place));
            return;
        }
        if (at == Region.OUT) {
            // Up the ladder or tower: from its foot onto the walkway, slowly (pace), a few at a time (strike).
            Point foot = climbFoot(w);
            hold(b, b.pos.distanceTo(foot) > AT * 2 && region(b.pos) == Region.OUT ? foot : w.top);
            return;
        }
        if (at == Region.WALL) {
            FieldBlock foe = nearestEnemy(d, b.pos, BattlePlan.BLOCK_PITCH * 2, r -> r == Region.WALL);
            if (foe != null) {
                charge(b, foe);
                return;
            }
        }
        inside(b, a, d);
    }

    /** Over the wall or through the gate: make for the gate's button, fighting whoever stands in the way, and open it. */
    private void inside(FieldBlock b, FieldArmy a, FieldArmy d) {
        int g = nearestGate(b.pos);
        if (g < 0) {
            FieldBlock foe = nearestEnemy(d, b.pos, Double.MAX_VALUE, r -> true);
            if (foe != null) {
                charge(b, foe);
            }
            return;
        }
        GateState gate = gates.get(g);
        FieldBlock foe = nearestEnemy(d, b.pos, FieldRules.HOLDING_REACH, r -> r != Region.OUT);
        if (foe != null) {
            charge(b, foe);
            return;
        }
        hold(b, gate.gate.button());
        if (!gate.breached() && !b.live && b.pos.distanceTo(gate.gate.button()) <= AT && pressed.add(b)) {
            gate.open = true;
            actions.add(new GateAction(g, GateAction.Kind.OPEN, 0));
        }
    }

    /**
     * After the blocks move and fight: rams batter their gates, and the clocks run on an open gate and on attackers
     * inside the walls.
     */
    void afterFighting(FieldArmy a, FieldArmy d, int seconds) {
        for (Work w : works) {
            FieldBlock carrier = w.carrier();
            if (w.kind != Engine.RAMS || w.state != State.UP || carrier == null || carrier.duty == Duty.FLEEING) {
                continue;
            }
            GateState g = gates.get(w.gate);
            if (!g.broken && !g.open && carrier.pos.distanceTo(w.foot) <= AT) {
                double blow = carrier.alive * RAM_PER_SOLDIER;
                g.hp -= blow;
                actions.add(new GateAction(w.gate, GateAction.Kind.RAM, blow));
                if (g.hp <= 0) {
                    g.broken = true;
                    events.add(new FieldBattle.Event(0, rafasconquest.data.Dialogue.say("battle.events.gate_smashed")));
                }
            }
        }
        boolean open = gates.stream().anyMatch(g -> g.open && !g.broken);
        openSeconds = open ? openSeconds + 1 : 0;
        boolean in = a.blocks.stream().anyMatch(b -> b.alive > 0 && b.duty != Duty.FLEEING && region(b.pos) == Region.IN);
        insideSeconds = in ? insideSeconds + 1 : 0;
        if (taken == null && openSeconds >= BREACH_SECONDS) {
            taken = "the gate stood open a minute";
        } else if (taken == null && insideSeconds >= BREACH_SECONDS) {
            taken = "the attackers held the inside of the walls a minute";
        }
    }

    // ---- rules the field battle asks about ----

    /** Whether {@code striker} can reach {@code foe} hand to hand: walls stand between those outside and those on or in them. */
    boolean reaches(FieldBlock striker, FieldBlock foe) {
        Region a = region(striker.pos);
        Region b = region(foe.pos);
        if ((striker.arm.mounted() || foe.arm.mounted()) && (a == Region.WALL) != (b == Region.WALL)) {
            return false; // a rider and a man on the walkway: one is up the wall, the other not
        }
        if (a == b || a != Region.OUT && b != Region.OUT) {
            return true;
        }
        FieldBlock outside = a == Region.OUT ? striker : foe;
        Region other = a == Region.OUT ? b : a;
        if (other == Region.IN) {
            int g = nearestGate(outside.pos);
            return g >= 0 && gates.get(g).breached() && outside.pos.distanceTo(gates.get(g).gate.mouth()) <= FieldRules.HOLDING_REACH;
        }
        Work w = outside.side == 0 ? workOf(outside) : null;
        return w != null && w.kind != Engine.RAMS && w.state == State.UP && outside.pos.distanceTo(climbFoot(w)) <= AT * 2;
    }

    /**
     * Where a block that is only numbers may step, wanting {@code to}: the walls stop it going over or out, save up a
     * ladder or tower of its own that stands raised (or back down it), or through a gate or breach broken open. Real
     * soldiers have the real walls to stop them.
     */
    Point fence(FieldBlock b, Point to) {
        if (b.arm.mounted() && !byBreach(b.pos)) {
            // Horses never go up onto the walls, only through a gate broken open: one up there is pushed off.
            if (region(b.pos) == Region.WALL) {
                return offWall(b.pos);
            }
            if (region(to) == Region.WALL) {
                return b.pos;
            }
        }
        boolean out = region(b.pos) == Region.OUT;
        if (out == (region(to) == Region.OUT)) {
            return to;
        }
        if (byBreach(b.pos)) {
            return to;
        }
        Work w = b.side == 0 && b.arm != Arm.ENGINES ? workOf(b) : null;
        if (w != null && w.kind != Engine.RAMS && w.state == State.UP
                && (b.pos.distanceTo(w.foot) <= (w.kind == Engine.TOWERS ? w.out : 1.5) + AT || b.pos.distanceTo(w.top) <= AT * 2)) {
            return to;
        }
        return b.pos;
    }

    /** Whether {@code p} is in the way through a gate or breach broken open (its passage crosses the wall). */
    private boolean byBreach(Point p) {
        double pass = plan.thickness() + AT;
        for (GateState g : gates) {
            if (g.breached() && (p.distanceTo(g.gate.mouth()) <= pass || p.distanceTo(g.gate.button()) <= pass)) {
                return true;
            }
        }
        return false;
    }

    /** The nearest place off the walls from {@code p} on them: just outside their outer face, or just inside their inner. */
    Point offWall(Point p) {
        double t = plan.thickness();
        double outward = Math.min(Math.min(p.x() - plan.minX(), plan.maxX() - p.x()), Math.min(p.z() - plan.minZ(), plan.maxZ() - p.z()));
        double ix = Math.max(plan.minX() + t + 1, Math.min(plan.maxX() - t - 1, p.x()));
        double iz = Math.max(plan.minZ() + t + 1, Math.min(plan.maxZ() - t - 1, p.z()));
        if (Math.hypot(ix - p.x(), iz - p.z()) <= outward + 1) {
            return new Point(ix, iz);
        }
        // Out across whichever face is nearest.
        double x = p.x();
        double z = p.z();
        if (outward == p.x() - plan.minX()) {
            x = plan.minX() - 1;
        } else if (outward == plan.maxX() - p.x()) {
            x = plan.maxX() + 1;
        } else if (outward == p.z() - plan.minZ()) {
            z = plan.minZ() - 1;
        } else {
            z = plan.maxZ() + 1;
        }
        return new Point(x, z);
    }

    /** How much of a hand-to-hand blow lands: climbers fight only a few at a time; super heavy infantry shrugs off much. */
    double meleeFactor(FieldBlock striker, FieldBlock struck) {
        double f = struck.superHeavy ? SUPER_HEAVY_TAKES : 1;
        Work w = striker.side == 0 ? workOf(striker) : null;
        if (w != null && w.kind != Engine.RAMS && region(striker.pos) == Region.OUT) {
            f *= w.kind == Engine.LADDERS ? LADDER_FIGHTS : TOWER_FIGHTS;
        }
        return f;
    }

    /** How much of an arrow volley lands: defenders behind their walls take little, a tower's crew is behind it. */
    double arrowFactor(FieldBlock struck) {
        double f = struck.superHeavy ? SUPER_HEAVY_TAKES : 1;
        if (struck.side == 1 && region(struck.pos) != Region.OUT) {
            f *= FieldBattle.WALL_TAKES;
        }
        Work w = struck.side == 0 ? workOf(struck) : null;
        if (w != null && w.kind == Engine.TOWERS && region(struck.pos) == Region.OUT) {
            f *= TOWER_SHIELDS;
        }
        FieldBlock m = struck.side == 0 ? screenOf.get(struck) : null;
        if (m != null && m.alive > 0 && m.pos.distanceTo(struck.pos) <= MANTLET_COVER) {
            f *= MANTLET_SHIELDS; // behind its mantlets
        }
        return f;
    }

    /** Where the artillery batters the main wall: its outer face. */
    public Point battered() {
        return battered(fronts.get(0));
    }

    /** Where a wall's artillery batters it: its outer face. */
    private static Point battered(Front f) {
        return f.face.at(f.batteredRight, 0);
    }

    /**
     * Whether an attacker's engine shoots at the wall, not the men: artillery, its own wall unbroken, the stretch it
     * batters in reach.
     */
    boolean batters(FieldBlock b) {
        Front f = b.side == 0 ? frontOf.get(b) : null;
        return f != null && f.breach < 0 && b.machine != null && b.machine.batter > 0 && b.pos.distanceTo(battered(f)) <= b.machine.reach;
    }

    /** Shots from {@code b} striking its wall, {@code blows} of them (a trebuchet's stone each): enough, and it breaks open. */
    void batter(FieldBlock b, double blows) {
        Front f = frontOf.get(b);
        if (f == null) {
            return;
        }
        f.wallLeft -= blows;
        if (f.wallLeft <= 0) {
            openBreach(battered(f));
        }
    }

    /**
     * A wall broken open at {@code at} (by the artillery, or a hole found in the real wall): a way in, like a smashed
     * gate, for the rest of the day. Each wall the attackers come at opens once at most; a hole in a wall none come at
     * opens for whoever is nearest.
     */
    public void openBreach(Point at) {
        Front f = null;
        double nearest = Double.MAX_VALUE;
        for (Front front : fronts) {
            double off = Math.abs(BattlePlan.offset(front.face.mid(), front.outX(), front.outZ(), at).z());
            if (off < nearest) {
                nearest = off;
                f = front;
            }
        }
        if (f == null || f.breach >= 0) {
            return;
        }
        double right = BattlePlan.offset(f.face.mid(), f.outX(), f.outZ(), at).x();
        GateState g = new GateState(new Gate(f.face.at(right, -1), f.face.at(right, plan.thickness() + 4), f.outX(), f.outZ()));
        g.broken = true;
        gates.add(g);
        f.breach = gates.size() - 1;
        events.add(new FieldBattle.Event(0, rafasconquest.data.Dialogue.say("battle.events.wall_breached")));
    }

    /** The main wall's breach, else the first opened anywhere; null while every wall stands. */
    @Nullable
    public Gate breach() {
        List<Gate> all = breaches();
        return fronts.get(0).breach >= 0 ? gates.get(fronts.get(0).breach).gate : all.isEmpty() ? null : all.get(0);
    }

    /** Every breach opened, in the order they were. */
    public List<Gate> breaches() {
        return gates.subList(plan.gates().size(), gates.size()).stream().map(GateState::gate).toList();
    }

    /** The walls the attackers come at, the main one first. */
    public List<Front> fronts() {
        return fronts;
    }

    /** The camp an attacking block falls back to: its wall's force's; null if it has none (or isn't one of them). */
    @Nullable
    Point campOf(FieldBlock b) {
        Front f = frontOf.get(b);
        return f == null ? null : f.camp;
    }

    /** Where an attacking block stands {@code back} out from where it deployed, away from its wall; null if it has no wall. */
    @Nullable
    Point away(FieldBlock b, double back) {
        Front f = frontOf.get(b);
        return f == null ? null : b.deployed.plus(f.outX() * back, f.outZ() * back);
    }

    /** How fast a block walks here: climbing a ladder or a tower is slow. */
    double pace(FieldBlock b) {
        Work w = b.side == 0 ? workOf(b) : null;
        if (w == null || w.kind == Engine.RAMS || w.state != State.UP || region(b.pos) == Region.IN
                || b.pos.distanceTo(climbFoot(w)) > AT * 2 && region(b.pos) == Region.OUT) {
            return 1;
        }
        return w.kind == Engine.LADDERS ? LADDER_PACE : TOWER_PACE;
    }

    /** Evening: the strategems go back to camp with their crews, to be brought again tomorrow. */
    void nightfall() {
        for (Work w : works) {
            w.state = State.CARRIED;
            w.leg = 0;
            w.at = w.start;
        }
    }

    /** Where a tower's crew stands, {@code place} in line behind it: the first pushing it. */
    private Point behind(Work w, int place) {
        double back = TOWER_BEHIND + 6 * place;
        return w.at.plus(w.outX() * back, w.outZ() * back);
    }

    /** Where climbers start up: a ladder's foot, or the foot of a tower's back. */
    private Point climbFoot(Work w) {
        return w.kind == Engine.TOWERS ? w.foot.plus(w.outX() * (TOWER_BEHIND - 1), w.outZ() * (TOWER_BEHIND - 1)) : w.foot;
    }

    /** Why the fortress fell, or null while it holds. */
    @Nullable
    String taken() {
        return taken;
    }

    // ---- for the live battle ----

    public Plan plan() {
        return plan;
    }

    public List<Work> works() {
        return works;
    }

    /** Towers roll at {@code blocksPerSecond}, the real towers' own speed. */
    public void towerSpeed(double blocksPerSecond) {
        towerSpeed = blocksPerSecond;
    }

    public List<GateState> gates() {
        return gates;
    }

    /** The real gate's state, read back each second (it is what counts while the battle is fought for real). */
    public void setGate(int i, boolean open, boolean broken) {
        GateState g = gates.get(i);
        if (open && !g.open && !broken) {
            events.add(new FieldBattle.Event(0, rafasconquest.data.Dialogue.say("battle.events.gate_opened")));
        }
        g.open = open;
        g.broken = broken;
    }

    /** What soldiers who are only numbers did to the gates since last asked. */
    public List<GateAction> takeActions() {
        List<GateAction> taken = new ArrayList<>(actions);
        actions.clear();
        return taken;
    }

    List<FieldBattle.Event> takeEvents() {
        List<FieldBattle.Event> taken = new ArrayList<>(events);
        events.clear();
        return taken;
    }

    // ---- helpers ----

    private static void hold(FieldBlock b, Point at) {
        b.charged = false;
        b.approaching = false;
        b.prey = null;
        b.strikeAt = null;
        b.waypoint = null;
        b.line = at;
    }

    private static void charge(FieldBlock b, FieldBlock foe) {
        b.charged = true;
        b.approaching = false;
        b.prey = foe;
    }

    private int nearestGate(Point p) {
        int best = -1;
        for (int i = 0; i < gates.size(); i++) {
            if (best < 0 || gates.get(i).gate.button().distanceTo(p) < gates.get(best).gate.button().distanceTo(p)) {
                best = i;
            }
        }
        return best;
    }

    /** The gate standing open (not smashed) nearest {@code p}, within the breach reach, or -1. */
    private int openGateNear(Point p) {
        int best = -1;
        for (int i = 0; i < gates.size(); i++) {
            GateState g = gates.get(i);
            if (g.open && !g.broken && g.gate.button().distanceTo(p) <= BREACH_REACH
                    && (best < 0 || g.gate.button().distanceTo(p) < gates.get(best).gate.button().distanceTo(p))) {
                best = i;
            }
        }
        return best;
    }

    @Nullable
    private FieldBlock nearestEnemy(FieldArmy enemy, Point from, double reach, java.util.function.Predicate<Region> where) {
        FieldBlock best = null;
        double bestDistance = reach;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive == 0 || e.duty == Duty.FLEEING || e.afloat || !where.test(region(e.pos))) {
                continue;
            }
            double distance = e.pos.distanceTo(from);
            if (distance <= bestDistance) {
                bestDistance = distance;
                best = e;
            }
        }
        return best;
    }

    @Nullable
    private static FieldBlock nearest(List<FieldBlock> blocks, Point to, java.util.function.Predicate<FieldBlock> ok) {
        FieldBlock best = null;
        for (FieldBlock b : blocks) {
            if (b.alive > 0 && ok.test(b) && (best == null || b.pos.distanceTo(to) < best.pos.distanceTo(to))) {
                best = b;
            }
        }
        return best;
    }
}
