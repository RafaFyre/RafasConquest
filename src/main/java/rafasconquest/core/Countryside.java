package rafasconquest.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * The countryside, planned with the titles and their seat cities: about half the provinces with no city on them have one
 * settlement; the rest are wilderness. Most are a **hamlet** (one shack and a sheep pen); some a **small village** (3 to 6 hamlets about a well),
 * never two in neighbouring provinces, and always at least twice as many hamlets as villages. About one hamlet in eight
 * is a **watchtower** instead, more often on the edge of a kingdom; about one county Town in four is a **castle**
 * instead of a city, more often on the edge of a duchy.
 */
public final class Countryside {
    public enum Kind {
        HAMLET, VILLAGE, WATCHTOWER, CASTLE,
        /** An unowned seat's ruin: a single empty house where its city would have stood (placed with the nations). */
        RUIN
    }

    /** One settlement: its centre, kind, province, and for a village how many hamlets it has. */
    public record Settlement(int x, int z, Kind kind, int province, int hamlets) {
        /** How far it reaches from its centre (blocks): kept on land and clear of cities. */
        public int reach() {
            return switch (kind) {
                case HAMLET -> 8;
                case VILLAGE -> 28;
                case WATCHTOWER -> 5;
                case CASTLE -> 24;
                case RUIN -> 4;
            };
        }
    }

    /** What the countryside is: the settlements, and the seat cities left once some Towns became castles. */
    public record Plan(List<Settlement> settlements, List<SeatCities.City> cities) {
    }

    /** A province with no city is left wilderness (no settlement) this often. */
    static final double WILD_CHANCE = 0.5;
    /** A province is a village this often, where a neighbour isn't one already. */
    static final double VILLAGE_CHANCE = 0.3;
    /** Watchtowers: one hamlet in this many on a kingdom's edge, and in this many elsewhere (about one in eight in all). */
    static final double TOWER_ON_EDGE = 0.25;
    static final double TOWER_INSIDE = 0.08;
    /** Castles: one county Town in this many on a duchy's edge, and in this many elsewhere (about one in four in all). */
    static final double CASTLE_ON_EDGE = 0.4;
    static final double CASTLE_INSIDE = 0.12;
    private static final int DUCHY_EDGE = 2;
    private static final int KINGDOM_EDGE = 3;
    private static final long SALT = 0x434F554E54525953L;

    private Countryside() {
    }

    public static Plan plan(long seed, SeatCities.Grid g, TitleMap t, List<SeatCities.City> cities) {
        Random random = new Random(seed ^ SALT);
        int n = g.centres().size();
        List<TreeSet<Integer>> adjacent = adjacency(g, n);
        // Castles in place of some county Towns: a castle's province is taken, but not the land the Town would have spread
        // over.
        List<Settlement> settlements = new ArrayList<>();
        List<SeatCities.City> kept = new ArrayList<>();
        boolean[] covered = new boolean[n];
        for (SeatCities.City c : cities) {
            boolean edge = onEdge(t, adjacent, c.province(), DUCHY_EDGE);
            if (c.tier() == TitleMap.Tier.COUNTY && random.nextDouble() < (edge ? CASTLE_ON_EDGE : CASTLE_INSIDE)) {
                settlements.add(new Settlement(c.x(), c.z(), Kind.CASTLE, c.province(), 0));
                covered[c.province()] = true;
            } else {
                kept.add(c);
                for (int p : SeatCities.covered(g, c)) {
                    covered[p] = true;
                }
            }
        }
        // Hamlets and villages in about half the other provinces (the rest wilderness), villages never side by side.
        Kind[] kind = new Kind[n];
        List<Integer> order = new ArrayList<>();
        for (int p = 0; p < n; p++) {
            if (!covered[p] && random.nextDouble() >= WILD_CHANCE) {
                order.add(p);
            }
        }
        Collections.shuffle(order, random);
        for (int p : order) {
            boolean besideVillage = adjacent.get(p).stream().anyMatch(q -> kind[q] == Kind.VILLAGE);
            kind[p] = !besideVillage && random.nextDouble() < VILLAGE_CHANCE ? Kind.VILLAGE : Kind.HAMLET;
        }
        // Watchtowers in place of some hamlets.
        for (int p : order) {
            if (kind[p] == Kind.HAMLET && random.nextDouble() < (onEdge(t, adjacent, p, KINGDOM_EDGE) ? TOWER_ON_EDGE : TOWER_INSIDE)) {
                kind[p] = Kind.WATCHTOWER;
            }
        }
        // At least twice as many hamlets as villages.
        List<Integer> villages = new ArrayList<>();
        int hamlets = 0;
        for (int p : order) {
            if (kind[p] == Kind.VILLAGE) {
                villages.add(p);
            } else {
                hamlets++;
            }
        }
        while (!villages.isEmpty() && hamlets < 2 * villages.size()) {
            int p = villages.remove(random.nextInt(villages.size()));
            kind[p] = Kind.HAMLET;
            hamlets++;
        }
        for (int p : order) {
            int count = kind[p] == Kind.VILLAGE ? 3 + random.nextInt(4) : 0;
            Settlement draft = new Settlement(0, 0, kind[p], p, count);
            int[] at = site(g, p, draft.reach(), kept);
            if (at != null) {
                settlements.add(new Settlement(at[0], at[1], kind[p], p, count));
            }
        }
        return new Plan(settlements, kept);
    }

    /** Whether a province borders one in another title of the given level ({@link #level}: 2 a duchy, 3 a kingdom). */
    private static boolean onEdge(TitleMap t, List<TreeSet<Integer>> adjacent, int p, int level) {
        for (int q : adjacent.get(p)) {
            if (level(t, p, q) >= level) {
                return true;
            }
        }
        return false;
    }

    /** 0 the same county, 1 another county, 2 another duchy, 3 another kingdom, 4 another empire. */
    static int level(TitleMap t, int p, int q) {
        int cp = t.county()[p];
        int cq = t.county()[q];
        if (cp == cq) {
            return 0;
        }
        int dp = t.duchy()[cp];
        int dq = t.duchy()[cq];
        if (dp == dq) {
            return 1;
        }
        int kp = t.kingdom()[dp];
        int kq = t.kingdom()[dq];
        if (kp == kq) {
            return 2;
        }
        return t.empire()[kp] == t.empire()[kq] ? 3 : 4;
    }

    /**
     * Where a settlement of a province goes: its centre if the settlement fits on land there, clear of every city, else
     * the nearest such spot within the province; null if there is none.
     */
    private static int[] site(SeatCities.Grid g, int province, int reach, List<SeatCities.City> cities) {
        ProvinceMap.Centre c = g.centres().get(province);
        int search = ProvinceMap.SPACING / 2;
        int[] best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dz = -search; dz <= search; dz += g.bpp()) {
            for (int dx = -search; dx <= search; dx += g.bpp()) {
                double d = Math.hypot(dx, dz);
                if (d >= bestDistance) {
                    continue;
                }
                int x = c.x() + dx;
                int z = c.z() + dz;
                if (provinceAt(g, x, z) != province || !land(g, x, z, reach) || nearCity(cities, x, z, reach)) {
                    continue;
                }
                bestDistance = d;
                best = new int[]{x, z};
            }
        }
        return best;
    }

    private static int provinceAt(SeatCities.Grid g, int x, int z) {
        int px = Math.floorDiv(x - g.minX(), g.bpp());
        int pz = Math.floorDiv(z - g.minZ(), g.bpp());
        if (px < 0 || pz < 0 || px >= g.width() || pz >= g.height()) {
            return -1;
        }
        return g.provinces()[pz * g.width() + px];
    }

    private static boolean land(SeatCities.Grid g, int x, int z, int reach) {
        int r = reach + g.bpp();
        return provinceAt(g, x - r, z - r) >= 0 && provinceAt(g, x + r, z - r) >= 0 && provinceAt(g, x - r, z + r) >= 0
                && provinceAt(g, x + r, z + r) >= 0 && provinceAt(g, x, z) >= 0;
    }

    private static boolean nearCity(List<SeatCities.City> cities, int x, int z, int reach) {
        for (SeatCities.City c : cities) {
            int apart = c.reach() + reach + SeatCities.CLEARANCE;
            if (Math.abs(c.x() - x) < apart && Math.abs(c.z() - z) < apart) {
                return true;
            }
        }
        return false;
    }

    /** Which provinces neighbour which, from the map's pixels. */
    static List<TreeSet<Integer>> adjacency(SeatCities.Grid g, int n) {
        List<TreeSet<Integer>> adjacent = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjacent.add(new TreeSet<>());
        }
        int w = g.width();
        for (int z = 0; z < g.height(); z++) {
            for (int x = 0; x < w; x++) {
                int p = g.provinces()[z * w + x];
                if (p < 0) {
                    continue;
                }
                if (x + 1 < w) {
                    int q = g.provinces()[z * w + x + 1];
                    if (q >= 0 && q != p) {
                        adjacent.get(p).add(q);
                        adjacent.get(q).add(p);
                    }
                }
                if (z + 1 < g.height()) {
                    int q = g.provinces()[(z + 1) * w + x];
                    if (q >= 0 && q != p) {
                        adjacent.get(p).add(q);
                        adjacent.get(q).add(p);
                    }
                }
            }
        }
        return adjacent;
    }
}
