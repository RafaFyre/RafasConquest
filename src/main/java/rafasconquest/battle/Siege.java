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
 * A siege's rules on top of a {@link FieldBattle}: the attacker (side 0) comes at one wall, its flattest, with ladders,
 * siege towers and battering rams; the defender (side 1) holds that wall, archers in front and foot behind them, its
 * reserves inside sent up to replace whoever is worn down there. Nine in ten of the defenders' cavalry fight on foot
 * as super heavy infantry, by the gate; the rest stay mounted to ride down attackers who get in. The attackers win by
 * breaching a gate: one opened from inside (its Gate Block's button) and left open a minute, or attackers inside the
 * walls for a minute on end. The defenders never withdraw.
 * <p>
 * The fortress is a box of walls, its sides along x and z. Points are (x, z) in blocks.
 */
public final class Siege {
    /** The attackers' strategems. */
    public enum Engine { LADDERS, TOWERS, RAMS }

    /** At most this many of each an army brings. */
    public static final int LADDERS = 16;
    public static final int TOWERS = 4;
    public static final int RAMS = 2;
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
    /** A tower rolls at half a walk, its centre this far out from the wall when it is against it (half its length). */
    static final double TOWER_SPEED = FieldRules.FOOT_SPEED / 2;
    public static final double TOWER_OUT = 5;
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
    static final double WEDGE_OUT = 40;
    /** Rams come round the walls this far out. */
    static final double ROUND_OUT = 12;
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
                       double outX, double outZ, List<Gate> gates, Point hall, Set<Engine> engines) {
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

    /** Where a place is: outside the walls, on them, or inside them. */
    public enum Region { OUT, WALL, IN }

    public enum State { CARRIED, RAISING, UP }

    /** A ladder, tower or ram, and the blocks that go with it: the first carries it; the rest climb after. */
    public static final class Work {
        final Engine kind;
        /** Where it stands against the wall (outside), where its climbers make for on the walkway, and its gate (rams). */
        final Point foot;
        final Point top;
        final int gate;
        final List<Point> route = new ArrayList<>();
        final List<FieldBlock> crews = new ArrayList<>();
        /** A tower's middle now, and where it set out from each morning. */
        Point at;
        Point start;
        State state = State.CARRIED;
        int since;
        int leg;

        Work(Engine kind, Point foot, Point top, int gate) {
            this.kind = kind;
            this.foot = foot;
            this.top = top;
            this.gate = gate;
        }

        public Engine kind() {
            return kind;
        }

        public Point foot() {
            return foot;
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
        @Nullable
        FieldBlock holder;

        Post(Point at, boolean archers) {
            this.at = at;
            this.archers = archers;
        }
    }

    final Plan plan;
    private final Point wallMid;
    private final List<GateState> gates = new ArrayList<>();
    private final List<Work> works = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();
    /** Defenders waiting inside, and where each waits. */
    private final List<FieldBlock> reserves = new ArrayList<>();
    private final java.util.Map<FieldBlock, Point> homes = new java.util.HashMap<>();
    private final List<FieldBlock> superHeavy = new ArrayList<>();
    private final List<FieldBlock> riders = new ArrayList<>();
    private final List<FieldBlock> wedge = new ArrayList<>();
    private final Set<FieldBlock> pressed = new java.util.HashSet<>();
    private final List<GateAction> actions = new ArrayList<>();
    private final List<FieldBattle.Event> events = new ArrayList<>();
    /** The gate the attack makes for: the nearest to the attacked wall's middle (-1: none). */
    private final int target;
    private int openSeconds;
    private int insideSeconds;
    @Nullable
    private String taken;

    public Siege(Plan plan) {
        this.plan = plan;
        this.wallMid = plan.wallMid();
        for (Gate g : plan.gates()) {
            gates.add(new GateState(g));
        }
        int best = -1;
        for (int i = 0; i < gates.size(); i++) {
            if (best < 0 || gates.get(i).gate.mouth().distanceTo(wallMid) < gates.get(best).gate.mouth().distanceTo(wallMid)) {
                best = i;
            }
        }
        target = best;
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
     * Lays both armies out for the siege: the defenders on and behind the attacked wall, the attackers' foot given the
     * strategems to carry, their cavalry in a wedge before the gate.
     */
    void deploy(FieldArmy a, FieldArmy d) {
        deployDefenders(d);
        deployAttackers(a);
    }

    private void deployDefenders(FieldArmy d) {
        double t = plan.thickness();
        double pitch = BattlePlan.pitch(POST_FILES);
        int perRow = Math.max(1, (int) Math.floor(plan.wallLength() / pitch));
        // Archers just behind the merlons (mid second row, so none stand on them), foot behind them: on the walkway if
        // it is wide enough, else at the wall's foot inside.
        double archerIn = Math.min(1.5, t - 0.5);
        double footIn = t >= 3.5 ? t - 1.75 : t + 1.5;
        for (int i = 0; i < perRow; i++) {
            double right = (i - (perRow - 1) / 2.0) * pitch;
            posts.add(new Post(onWall(right, archerIn), true));
        }
        for (int i = 0; i < perRow; i++) {
            double right = (i - (perRow - 1) / 2.0) * pitch;
            posts.add(new Post(onWall(right, footIn), false));
        }
        List<FieldBlock> archers = new ArrayList<>();
        List<FieldBlock> foot = new ArrayList<>();
        List<FieldBlock> cavalry = new ArrayList<>();
        for (FieldBlock b : d.blocks) {
            if (b.duty == Duty.GENERAL || b.duty == Duty.GUARD || b.duty == Duty.BEARER || b.afloat) {
                continue;
            }
            (b.arm == Arm.ARCHERS ? archers : b.arm.mounted() ? cavalry : foot).add(b);
        }
        for (Post p : posts) {
            List<FieldBlock> from = p.archers && !archers.isEmpty() ? archers : !foot.isEmpty() ? foot : archers;
            if (from.isEmpty()) {
                continue;
            }
            p.holder = from.remove(0);
            p.holder.duty = Duty.FRONT;
            p.holder.homeDuty = Duty.FRONT;
            p.holder.files = POST_FILES;
            put(p.holder, p.at);
        }
        // The reserves wait inside in rows behind the wall, archers among the foot.
        List<FieldBlock> waiting = new ArrayList<>(foot);
        waiting.addAll(archers);
        int across = Math.max(1, (int) Math.floor(plan.wallLength() / BattlePlan.BLOCK_PITCH));
        for (int i = 0; i < waiting.size(); i++) {
            FieldBlock b = waiting.get(i);
            int row = i / across;
            Point at = within(onWall((i % across - (Math.min(across, waiting.size() - row * across) - 1) / 2.0) * BattlePlan.BLOCK_PITCH,
                    t + 8 + row * (BattlePlan.LINE_GAP / 2)));
            b.duty = Duty.LINE;
            b.homeDuty = Duty.LINE;
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

    private void deployAttackers(FieldArmy a) {
        List<FieldBlock> foot = new ArrayList<>();
        List<FieldBlock> cavalry = new ArrayList<>();
        for (FieldBlock b : a.blocks) {
            if (b.duty == Duty.GENERAL || b.duty == Duty.GUARD || b.duty == Duty.BEARER || b.afloat) {
                continue;
            }
            if (b.arm.infantry()) {
                foot.add(b);
            } else if (b.arm.mounted()) {
                cavalry.add(b);
            }
        }
        // Nearest the middle of the line first: they carry the strategems; the rest follow up after them.
        foot.sort(Comparator.comparingDouble(b -> Math.abs(BattlePlan.offset(a.origin, a.facingX, a.facingZ, b.line).x())));
        Set<Engine> wanted = plan.engines().isEmpty() ? EnumSet.of(Engine.LADDERS) : plan.engines();
        int[] left = {wanted.contains(Engine.LADDERS) ? LADDERS : 0, wanted.contains(Engine.TOWERS) ? TOWERS : 0,
                wanted.contains(Engine.RAMS) && target >= 0 ? RAMS : 0};
        List<Engine> order = new ArrayList<>();
        while (order.size() < foot.size() && left[0] + left[1] + left[2] > 0) {
            for (Engine e : Engine.values()) {
                if (left[e.ordinal()] > 0 && order.size() < foot.size()) {
                    order.add(e);
                    left[e.ordinal()]--;
                }
            }
        }
        long ladders = order.stream().filter(e -> e == Engine.LADDERS).count();
        long towers = order.stream().filter(e -> e == Engine.TOWERS).count();
        List<Point> ladderSpots = spots((int) ladders);
        List<Point> towerSpots = spots((int) towers);
        int li = 0;
        int ti = 0;
        for (int i = 0; i < order.size(); i++) {
            Engine e = order.get(i);
            Work w = switch (e) {
                case LADDERS -> climbWork(e, ladderSpots.get(li++), 1.5);
                case TOWERS -> climbWork(e, towerSpots.get(ti++), TOWER_OUT);
                case RAMS -> ramWork(a);
            };
            w.crews.add(foot.get(i));
            works.add(w);
        }
        // Each tower starts straight out from its spot, facing the wall, its crew behind it.
        for (Work w : works) {
            if (w.kind == Engine.TOWERS) {
                FieldBlock carrier = w.crews.get(0);
                double right = BattlePlan.offset(wallMid, plan.outX(), plan.outZ(), w.foot).x();
                double in = Math.min(BattlePlan.offset(wallMid, plan.outX(), plan.outZ(), carrier.line).z() + TOWER_BEHIND, -2 * TOWER_OUT);
                w.at = w.start = onWall(right, in);
                put(carrier, onWall(right, in - TOWER_BEHIND));
                carrier.facingX = -plan.outX();
                carrier.facingZ = -plan.outZ();
            }
        }
        // The rest of the foot follow the ladders and towers up, in turn.
        List<Work> climbs = works.stream().filter(w -> w.kind != Engine.RAMS).toList();
        for (int i = order.size(); i < foot.size() && !climbs.isEmpty(); i++) {
            climbs.get(i % climbs.size()).crews.add(foot.get(i));
        }
        // The cavalry forms a wedge before the gate, to charge it once it opens.
        if (target >= 0) {
            Gate g = gates.get(target).gate;
            Point tip = g.mouth().plus(g.outX() * WEDGE_OUT, g.outZ() * WEDGE_OUT);
            for (int i = 0; i < cavalry.size(); i++) {
                double[] slot = BattlePlan.wedgeSlot(i);
                FieldBlock b = cavalry.get(i);
                put(b, BattlePlan.place(tip, -g.outX(), -g.outZ(), slot[0], slot[1]));
                b.facingX = -g.outX();
                b.facingZ = -g.outZ();
                wedge.add(b);
            }
        }
        // Nobody of the attackers starts on or within the walls (nor hard against them): those who would are set back
        // before the attacked wall.
        for (FieldBlock b : a.blocks) {
            Point p = b.line;
            if (p.x() > plan.minX() - CLEAR && p.x() < plan.maxX() + CLEAR && p.z() > plan.minZ() - CLEAR && p.z() < plan.maxZ() + CLEAR) {
                put(b, onWall(BattlePlan.offset(wallMid, plan.outX(), plan.outZ(), p).x(), -CLEAR));
            }
        }
    }

    /** Attackers deploy at least this far out from the walls. */
    static final double CLEAR = 6;

    /** Evenly along the attacked wall, {@code n} of them, kept clear of its gates. Each is {right} along the wall. */
    private List<Point> spots(int n) {
        List<Point> spots = new ArrayList<>();
        double length = plan.wallLength();
        for (int k = 0; k < n; k++) {
            double right = -length / 2 + length * (k + 0.5) / n;
            for (GateState g : gates) {
                Point mouth = BattlePlan.offset(wallMid, plan.outX(), plan.outZ(), g.gate.mouth());
                if (Math.abs(mouth.z()) < 4 && Math.abs(right - mouth.x()) < 6) {
                    right = mouth.x() + (right < mouth.x() ? -6 : 6); // beside the gate, not in front of it
                }
            }
            spots.add(new Point(right, 0));
        }
        return spots;
    }

    /** A ladder or tower at {@code spot} along the wall, standing {@code out} out from its face. */
    private Work climbWork(Engine kind, Point spot, double out) {
        return new Work(kind, onWall(spot.x(), -out), onWall(spot.x(), plan.thickness() / 2), -1);
    }

    /** A ram, its way round the walls to the target gate from the attacked side. */
    private Work ramWork(FieldArmy a) {
        Gate g = gates.get(target).gate;
        Work w = new Work(Engine.RAMS, g.mouth(), g.mouth(), target);
        Point c = plan.centre();
        double hx = (plan.maxX() - plan.minX()) / 2 + ROUND_OUT;
        double hz = (plan.maxZ() - plan.minZ()) / 2 + ROUND_OUT;
        double ox = plan.outX();
        double oz = plan.outZ();
        double dot = ox * g.outX() + oz * g.outZ();
        if (dot < 0.5) {
            // Round a corner (two, for the far side): the corner between the attacked side and the next toward the gate.
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
                    && (!p.archers || b.arm == Arm.ARCHERS || reserves.stream().noneMatch(o -> o.arm == Arm.ARCHERS && o.alive > 0)));
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
                homes.put(h, homes.getOrDefault(fresh, h.deployed));
                reserves.add(h);
            }
            p.holder = fresh;
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
                foe = nearestEnemy(a, gates.get(target).gate.mouth(), 12, r -> true);
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
                            w.at = left <= TOWER_SPEED ? w.foot
                                    : w.at.plus((w.foot.x() - w.at.x()) / left * TOWER_SPEED, (w.foot.z() - w.at.z()) / left * TOWER_SPEED);
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
        // The wedge waits for the gate to open, then rides through it at whoever is inside.
        if (target >= 0) {
            GateState g = gates.get(target);
            for (FieldBlock b : wedge) {
                if (b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED) {
                    continue;
                }
                if (!g.breached()) {
                    hold(b, b.deployed);
                } else if (region(b.pos) == Region.OUT && b.pos.distanceTo(g.gate.mouth()) > AT) {
                    hold(b, g.gate.mouth());
                } else {
                    FieldBlock foe = nearestEnemy(d, b.pos, Double.MAX_VALUE, r -> r != Region.OUT);
                    if (foe != null) {
                        charge(b, foe);
                    } else {
                        hold(b, g.gate.button());
                    }
                }
            }
        }
    }

    private void crewOrders(Work w, FieldBlock b, boolean carrier, int place, FieldArmy a, FieldArmy d) {
        Region at = region(b.pos);
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
            hold(b, w.kind == Engine.TOWERS ? behind(w, place) : w.foot.plus(plan.outX() * 6 * place, plan.outZ() * 6 * place));
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
        return f;
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
        return w.at.plus(plan.outX() * back, plan.outZ() * back);
    }

    /** Where climbers start up: a ladder's foot, or the foot of a tower's back. */
    private Point climbFoot(Work w) {
        return w.kind == Engine.TOWERS ? w.foot.plus(plan.outX() * (TOWER_BEHIND - 1), plan.outZ() * (TOWER_BEHIND - 1)) : w.foot;
    }

    /** Why the fortress fell, or null while it holds. */
    @Nullable
    String taken() {
        return taken;
    }

    // ---- for the live battle ----

    public List<Work> works() {
        return works;
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
