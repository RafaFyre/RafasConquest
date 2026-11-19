package rafasconquest.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Where the ships of the sea lanes ({@link ShippingRoutes}) are at a given game time. Worked out from the time alone,
 * so the server (which sails real ships near players) and every client (which draws the far ones) agree untold.
 * <p>
 * Every ship belongs to a home port and sails voyage after voyage: moored at home for 2 to 5 minutes, out of the
 * harbour onto a lane, along it to the port at its far end, a slow turn about there for {@link #WAIT_TICKS}, back
 * along it, into the harbour to its mooring, and then the next voyage, to the next of its home's lanes in turn (a new
 * destination each time). At sea ships keep to the right of the lane, each on one of {@link #TRACKS} inside the lane's
 * {@link ShippingRoutes#BERTH}, all at one pace, so ships going opposite ways never meet; they turn about
 * {@link #END_GAP} blocks out past the harbour mouth, clear of the moored ships.
 * <p>
 * At launch each port has 3 to 9 ships moored, and {@link #AT_SEA_PER_SIDE} more for each of its lanes spread along
 * that lane, so each lane starts with 8 at sea; later the count drifts about that as moorings and lanes differ.
 */
public final class SeaLanes {
    /** Blocks a tick: four blocks a second, a sailing ship's pace. */
    public static final double SPEED = 0.2;
    /** How long a ship waits at a port it sailed to before sailing home: two minutes. */
    public static final double WAIT_TICKS = 120 * 20;
    /** How long a ship stays moored at home between voyages: 2 to 5 minutes. */
    static final int MOOR_MIN_TICKS = 2 * 60 * 20;
    static final int MOOR_MAX_TICKS = 5 * 60 * 20;
    /** How far right of the lane's line each track runs, inside half the berth. */
    static final double[] TRACKS = {12, 26, 40};
    /** Ships turn about and join their lane this far out past the harbour mouth (the lane's end). */
    static final double END_GAP = 100;
    /** Moorings: two rows across the harbour mouth, this far in from it and this far out past it; 8 to a row. */
    static final double MOOR_IN = 14;
    static final double MOOR_OUT = 22;
    static final double MOOR_APART = 36;
    static final int MOOR_ROW = 8;
    /** Ships each port sends to sea per lane at launch. */
    static final int AT_SEA_PER_SIDE = 4;

    /**
     * A ship card: a kind of ship, from its file in {@code mods/RafasConquest/units/ships} ({@link rafasconquest.battle.UnitCards}).
     * Its hull is a structure {@code .nbt} beside it ({@link rafasconquest.style.ShipHulls}; {@code merchant} the lanes'
     * unarmed one, blank: the same), with its length and beam, its deck's height above its keel, its pace in battle (times
     * the usual), how often it sails the lanes (a weight; 0: never), and which battle ship it is ({@code battle}: "unit",
     * "flagship"; blank: none). The built-in five are constants; a file of a new id adds a card.
     */
    public static final class Size {
        private static final List<Size> ALL = new ArrayList<>();
        private static Size[] array = new Size[0];

        public static final Size SMALL = builtIn("small");
        public static final Size MEDIUM = builtIn("medium");
        public static final Size LARGE = builtIn("large");
        public static final Size XLARGE = builtIn("xlarge");
        /** A battle's unit ship, two hundred soldiers on deck; never on the lanes. */
        public static final Size XXLARGE = builtIn("xxlarge");
        /** A ship of the line, Medium Cannons for guns: its crew stand only at the Character Markers in its hull and work them; never on the lanes. */
        public static final Size GUNSHIP = builtIn("gunship");
        /** A Gunship with Mounted Light Cannons for guns ({@link #gunBlock}). */
        public static final Size GUNSHIP_2 = builtIn("gunship2");

        public final String id;
        private int ordinal;
        private boolean builtIn;
        public String name;
        public int length = 17;
        public int width = 5;
        public int deck = 4;
        public double speed = 1;
        public int lanes;
        public String battle = "";
        public String hull = "";
        public String merchant = "";
        /** The block a made gunship hull puts in its gun ports (an Artillery Block, or a kind of one). */
        public String gunBlock = "rafasconquest:artillery";
        /** "boarder": its unit stands on its deck and boards (the captains' AI); "gunship": it lies off and fires broadsides. */
        public String type = "boarder";
        /**
         * Read from its hull ({@link rafasconquest.style.ShipHulls}): its guns (the engines on its Artillery Blocks, by id),
         * those on its right side (-x) and its left (+x), a centreline gun on both; and where its crew stand, its Character
         * Markers, one post a soldier.
         */
        public List<String> gunsRight = List.of();
        public List<String> gunsLeft = List.of();
        public List<Post> posts = List.of();

        /** A crewman's place in the hull's frame (x across, y up from the keel, z to the bow), and the gun beside it ("" none). */
        public record Post(int x, int y, int z, String gun) {
        }

        private Size(String id) {
            this.id = id;
            this.name = id;
        }

        private static Size builtIn(String id) {
            Size s = add(id);
            s.builtIn = true;
            rafasconquest.battle.UnitCards.readBuiltIn(s);
            return s;
        }

        /** The card of {@code id}, made (after the others) if new. */
        public static Size add(String id) {
            Size s = byId(id);
            if (s == null) {
                s = new Size(id);
                s.ordinal = ALL.size();
                ALL.add(s);
                array = ALL.toArray(Size[]::new);
            }
            return s;
        }

        public static void keepBuiltIns() {
            ALL.removeIf(s -> !s.builtIn);
            array = ALL.toArray(Size[]::new);
        }

        /** Every card, by ordinal. A copy: don't hold it across a reload. */
        public static Size[] values() {
            return array.clone();
        }

        /** The card at {@code ordinal}; a large ship if there is none (a server's card this client lacks). */
        public static Size get(int ordinal) {
            return ordinal >= 0 && ordinal < array.length ? array[ordinal] : LARGE;
        }

        @org.jetbrains.annotations.Nullable
        public static Size byId(String id) {
            for (Size s : array) {
                if (s.id.equals(id)) {
                    return s;
                }
            }
            return null;
        }

        /** The first card that is battle ship {@code role}, else {@code otherwise}. */
        public static Size battle(String role, Size otherwise) {
            for (Size s : array) {
                if (s.battle.equals(role)) {
                    return s;
                }
            }
            return otherwise;
        }

        public boolean gunship() {
            return type.equals("gunship");
        }

        /** Most soldiers this card's crew may be, up to {@code most}: a gunship's one a post (its gunners), once its hull is read. */
        public int crewCap(int most) {
            return gunship() && !posts.isEmpty() ? Math.min(most, posts.size()) : most;
        }

        public static Size unitShip() {
            return battle("unit", XXLARGE);
        }

        public int ordinal() {
            return ordinal;
        }

        public boolean builtIn() {
            return builtIn;
        }

        /** Its style slot folder name. */
        public String slot() {
            return id;
        }

        @Override
        public String toString() {
            return id;
        }

        /** A card by its lane weight, the most common first, so the built-ins' 40, 30, 20, 10 in 100 fall as they always did. */
        static Size pick(long hash) {
            List<Size> sailing = ALL.stream().filter(s -> s.lanes > 0)
                    .sorted(java.util.Comparator.comparingInt((Size s) -> -s.lanes)).toList();
            int total = sailing.stream().mapToInt(s -> s.lanes).sum();
            if (total == 0) {
                return MEDIUM;
            }
            int roll = (int) Math.floorMod(mix(hash) >>> 20, (long) total);
            for (Size s : sailing) {
                roll -= s.lanes;
                if (roll < 0) {
                    return s;
                }
            }
            return MEDIUM;
        }

        /** The longest ship on the lanes. */
        public static int longestOnLanes() {
            return ALL.stream().filter(s -> s.lanes > 0).mapToInt(s -> s.length).max().orElse(XLARGE.length);
        }
    }

    /**
     * A ship: its id (stable for as long as its home port's lanes are), where it is, which way it faces, its velocity
     * in blocks a tick, what it is, its home port, and the lane it is sailing ({@code from>to}), or null when it is in
     * harbour (moored, or going to or from its mooring).
     */
    public record Ship(String id, double x, double z, double dirX, double dirZ, double vx, double vz, boolean warship,
                       Size size, String home, String lane) {
        /** Its heading as a turn about the up axis, 0 facing +z (as {@code Quaterniond.rotationY} turns). */
        public double yaw() {
            return Math.atan2(dirX, dirZ);
        }
    }

    /** A lane measured once: its points' distances along it, and how far in from each end ships turn about. */
    private record Lane(Roads.Road road, double[] along, double gap) {
        double length() {
            return along[along.length - 1];
        }

        String key() {
            return road.from() + ">" + road.to();
        }
    }

    /** One of a port's lanes, and whether the port is at its start (so ships sail out along it forward). */
    private record Leg(Lane lane, boolean forward) {
    }

    /**
     * A port: its harbour mouth, the way out to sea from it, its lanes, how many of its ships start moored, and each
     * ship's last worked-out voyage (its number and start tick), so the next call needn't start from the first.
     */
    private record Port(String name, double x, double z, double outX, double outZ, List<Leg> legs, int moored,
                        int[] voyage, double[] start) {
        int ships() {
            return voyage.length;
        }
    }

    private static final Map<List<Roads.Road>, List<Port>> PLANS = new WeakHashMap<>();

    private SeaLanes() {
    }

    public static List<Ship> at(List<Roads.Road> roads, double ticks) {
        List<Port> ports;
        synchronized (PLANS) {
            ports = PLANS.computeIfAbsent(roads, SeaLanes::plan);
        }
        List<Ship> ships = new ArrayList<>();
        for (Port port : ports) {
            synchronized (port) {
                for (int j = 0; j < port.ships(); j++) {
                    ships.add(ship(port, j, ticks));
                }
            }
        }
        return ships;
    }

    private static List<Port> plan(List<Roads.Road> roads) {
        Map<String, List<Leg>> legs = new LinkedHashMap<>();
        for (Roads.Road road : roads) {
            int n = road.points();
            if (n < 2) {
                continue;
            }
            double[] along = new double[n];
            for (int i = 1; i < n; i++) {
                along[i] = along[i - 1] + Math.hypot(road.xs()[i] - road.xs()[i - 1], road.zs()[i] - road.zs()[i - 1]);
            }
            if (along[n - 1] <= 0) {
                continue;
            }
            Lane lane = new Lane(road, along, Math.min(END_GAP, along[n - 1] / 4));
            legs.computeIfAbsent(road.from(), k -> new ArrayList<>()).add(new Leg(lane, true));
            legs.computeIfAbsent(road.to(), k -> new ArrayList<>()).add(new Leg(lane, false));
        }
        List<Port> ports = new ArrayList<>();
        legs.forEach((name, own) -> {
            Leg first = own.get(0);
            double length = first.lane().length();
            double[] mouth = point(first.lane(), first.forward() ? 0 : length);
            double[] out = point(first.lane(), first.forward() ? Math.min(length, 100) : Math.max(0, length - 100));
            double dx = out[0] - mouth[0];
            double dz = out[1] - mouth[1];
            double d = Math.max(1e-9, Math.hypot(dx, dz));
            int moored = 3 + (int) Math.floorMod(mix(name.hashCode()), 7L);
            int ships = moored + AT_SEA_PER_SIDE * own.size();
            ports.add(new Port(name, mouth[0], mouth[1], dx / d, dz / d, List.copyOf(own), moored, new int[ships], new double[ships]));
        });
        for (Port port : ports) {
            for (int j = 0; j < port.ships(); j++) {
                port.voyage()[j] = 0;
                port.start()[j] = firstStart(port, j);
            }
        }
        return ports;
    }

    /**
     * When ship {@code j}'s first voyage starts. Ships that start moored begin it at launch; the others began it
     * earlier, so that at launch each is out on its lane, the port's ships for each lane spread evenly along it.
     */
    private static double firstStart(Port port, int j) {
        if (j < port.moored()) {
            return 0;
        }
        int n = port.legs().size();
        int k = (j - port.moored()) / n;
        Voyage v = voyage(port, j, 0);
        double atSea = 2 * v.sail() + WAIT_TICKS;
        return -(v.moor() + v.leave() + atSea * (k + 0.5) / AT_SEA_PER_SIDE);
    }

    /** One voyage of a ship: its lane, and how long each part takes, in ticks. */
    private record Voyage(Leg leg, double moor, double leave, double sail, double arrive) {
        double total() {
            return moor + leave + 2 * sail + WAIT_TICKS + arrive;
        }
    }

    private static Voyage voyage(Port port, int j, int v) {
        int n = port.legs().size();
        // A new destination each voyage: the next of the home's lanes in turn, from where this ship first sailed.
        int first = j < port.moored() ? (int) Math.floorMod(mix(id(port, j).hashCode()), (long) n) : (j - port.moored()) % n;
        Leg leg = port.legs().get((first + v) % n);
        long roll = mix(id(port, j).hashCode() * 31L + v);
        double moor = MOOR_MIN_TICKS + Math.floorMod(roll, (long) (MOOR_MAX_TICKS - MOOR_MIN_TICKS + 1));
        double[] berth = mooring(port, j);
        double[] out = trackPoint(leg.lane(), home(leg), !leg.forward(), track(j));
        double[] in = trackPoint(leg.lane(), home(leg), leg.forward(), track(j));
        double sail = (leg.lane().length() - 2 * leg.lane().gap()) / SPEED;
        return new Voyage(leg, moor, Math.hypot(out[0] - berth[0], out[1] - berth[1]) / SPEED, sail,
                Math.hypot(in[0] - berth[0], in[1] - berth[1]) / SPEED);
    }

    private static Ship ship(Port port, int j, double ticks) {
        int v = port.voyage()[j];
        double start = port.start()[j];
        if (ticks < start) {
            v = 0;
            start = firstStart(port, j);
        }
        Voyage voyage = voyage(port, j, v);
        while (ticks >= start + voyage.total()) {
            start += voyage.total();
            voyage = voyage(port, j, ++v);
        }
        port.voyage()[j] = v;
        port.start()[j] = start;
        return place(port, j, voyage, ticks - start);
    }

    /** Ship {@code j} {@code u} ticks into a voyage. */
    private static Ship place(Port port, int j, Voyage v, double u) {
        String id = id(port, j);
        Size size = Size.pick(id.hashCode());
        boolean warship = j % 3 == 2;
        Leg leg = v.leg();
        Lane lane = leg.lane();
        double track = track(j);
        double[] berth = mooring(port, j);
        if (u < v.moor()) {
            return new Ship(id, berth[0], berth[1], -port.outZ(), port.outX(), 0, 0, warship, size, port.name(), null);
        }
        u -= v.moor();
        if (u < v.leave()) {
            double[] to = trackPoint(lane, home(leg), !leg.forward(), track);
            return straight(id, berth, to, u, warship, size, port.name());
        }
        u -= v.leave();
        double far = leg.forward() ? lane.length() - lane.gap() : lane.gap();
        if (u < v.sail()) {
            double s = leg.forward() ? lane.gap() + u * SPEED : lane.length() - lane.gap() - u * SPEED;
            return onTrack(lane, s, !leg.forward(), track, id, warship, size, port.name());
        }
        u -= v.sail();
        if (u < WAIT_TICKS) {
            return turn(lane, far, leg.forward(), track, u, id, warship, size, port.name());
        }
        u -= WAIT_TICKS;
        if (u < v.sail()) {
            double s = leg.forward() ? far - u * SPEED : far + u * SPEED;
            return onTrack(lane, s, leg.forward(), track, id, warship, size, port.name());
        }
        u -= v.sail();
        double[] from = trackPoint(lane, home(leg), leg.forward(), track);
        return straight(id, from, berth, u, warship, size, port.name());
    }

    private static String id(Port port, int j) {
        return port.name() + "#" + j;
    }

    private static double track(int j) {
        return TRACKS[j % TRACKS.length];
    }

    /** Where along its lane a port's ships join and leave it. */
    private static double home(Leg leg) {
        return leg.forward() ? leg.lane().gap() : leg.lane().length() - leg.lane().gap();
    }

    /** A ship's mooring: in one of two rows across its home's harbour mouth, alternately either side of the way in. */
    private static double[] mooring(Port port, int j) {
        int slot = j % (2 * MOOR_ROW);
        int i = slot % MOOR_ROW;
        double side = (i % 2 == 0 ? 1 : -1) * MOOR_APART * (i / 2 + 0.5);
        double out = slot < MOOR_ROW ? -MOOR_IN : MOOR_OUT;
        return new double[]{port.x() + port.outX() * out - port.outZ() * side, port.z() + port.outZ() * out + port.outX() * side};
    }

    /** In harbour: straight from one point to another at the sailing pace. */
    private static Ship straight(String id, double[] from, double[] to, double u, boolean warship, Size size, String home) {
        double dx = to[0] - from[0];
        double dz = to[1] - from[1];
        double d = Math.max(1e-9, Math.hypot(dx, dz));
        double t = Math.min(1, u * SPEED / d);
        return new Ship(id, from[0] + dx * t, from[1] + dz * t, dx / d, dz / d, dx / d * SPEED, dz / d * SPEED, warship, size, home, null);
    }

    /** The point {@code track} blocks right of a lane's line at {@code s} along it, for a ship sailing forward or back. */
    private static double[] trackPoint(Lane lane, double s, boolean back, double track) {
        double[] p = point(lane, s);
        double hx = back ? -p[2] : p[2];
        double hz = back ? -p[3] : p[3];
        // Right of the heading, x east and z south: facing south (+z), right is west.
        return new double[]{p[0] - hz * track, p[1] + hx * track};
    }

    /** Sailing {@code track} blocks right of the lane's line at {@code s} along it, forward or back. */
    private static Ship onTrack(Lane lane, double s, boolean back, double track, String id, boolean warship, Size size, String home) {
        double[] p = point(lane, s);
        double hx = back ? -p[2] : p[2];
        double hz = back ? -p[3] : p[3];
        return new Ship(id, p[0] - hz * track, p[1] + hx * track, hx, hz, hx * SPEED, hz * SPEED, warship, size, home, lane.key());
    }

    /**
     * Waiting at the port it sailed to: a slow half circle about the lane's line, from the right of the way it came in
     * to the right of the way back, taking the whole wait.
     */
    private static Ship turn(Lane lane, double s, boolean arrivedForward, double track, double u, String id, boolean warship,
                             Size size, String home) {
        double[] p = point(lane, s);
        double hx = arrivedForward ? p[2] : -p[2];
        double hz = arrivedForward ? p[3] : -p[3];
        double rx = -hz;
        double rz = hx;
        double phi = Math.PI * u / WAIT_TICKS;
        double cos = Math.cos(phi);
        double sin = Math.sin(phi);
        double dx = -rx * sin + hx * cos;
        double dz = -rz * sin + hz * cos;
        double pace = track * Math.PI / WAIT_TICKS;
        return new Ship(id, p[0] + track * (rx * cos + hx * sin), p[1] + track * (rz * cos + hz * sin), dx, dz,
                dx * pace, dz * pace, warship, size, home, lane.key());
    }

    /** The point {@code s} along a lane, and the lane's direction there: {x, z, dirX, dirZ}. */
    private static double[] point(Lane lane, double s) {
        double[] along = lane.along();
        Roads.Road road = lane.road();
        int lo = 1;
        int hi = along.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (along[mid] < s) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        double x0 = road.xs()[lo - 1];
        double z0 = road.zs()[lo - 1];
        double dx = road.xs()[lo] - x0;
        double dz = road.zs()[lo] - z0;
        double seg = Math.max(1e-9, along[lo] - along[lo - 1]);
        double t = Math.max(0, Math.min(1, (s - along[lo - 1]) / seg));
        return new double[]{x0 + dx * t, z0 + dz * t, dx / seg, dz / seg};
    }

    private static long mix(long h) {
        h *= 0x9E3779B97F4A7C15L;
        return h ^ (h >>> 29);
    }
}
