package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * The land's titles, drawn once when the world map is, from the provinces alone (factions take them up later, not the
 * other way round). Provinces group into counties (2–6 neighbouring provinces), counties into duchies (3–6), duchies
 * into kingdoms (2–4), kingdoms into empires (3–6), each group grown from a random start through its neighbours. Every
 * title has a seat: a county's is one of its provinces, a duchy's one of its counties' seats, and so on up, so an
 * empire's seat is also the seat of its kingdom, duchy and county. Seats are drawn at random, shores three times as
 * likely. A group too small (say an island) joins a neighbour if it has one, and stands alone if not.
 * <p>
 * Each level is an index array: {@code county[province]}, {@code duchy[county]}, {@code kingdom[duchy]},
 * {@code empire[kingdom]} (-1 for a province in no province, i.e. water); and each title's seat province.
 */
public record TitleMap(int[] county, int[] duchy, int[] kingdom, int[] empire,
                       int[] countySeat, int[] duchySeat, int[] kingdomSeat, int[] empireSeat) {
    /** The size of each level's groups: counties in provinces, duchies in counties, and so on. */
    public static final int[] MIN = {2, 3, 2, 3};
    public static final int[] MAX = {6, 6, 4, 6};
    /** A shore province is this many times as likely to be drawn as a seat. */
    private static final int SHORE_WEIGHT = 3;
    private static final long SALT = 0x5449544C45534D41L;

    public enum Tier {
        COUNTY("County"), DUCHY("Duchy"), KINGDOM("Kingdom"), EMPIRE("Empire");

        private final String title;

        Tier(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    public int counties() {
        return countySeat.length;
    }

    public int duchies() {
        return duchySeat.length;
    }

    public int kingdoms() {
        return kingdomSeat.length;
    }

    public int empires() {
        return empireSeat.length;
    }

    /** The highest title whose seat a province is, or null if it is no seat. */
    public Tier seatOf(int province) {
        Tier best = null;
        for (int s : countySeat) {
            if (s == province) {
                best = Tier.COUNTY;
            }
        }
        for (int s : duchySeat) {
            if (s == province) {
                best = Tier.DUCHY;
            }
        }
        for (int s : kingdomSeat) {
            if (s == province) {
                best = Tier.KINGDOM;
            }
        }
        for (int s : empireSeat) {
            if (s == province) {
                best = Tier.EMPIRE;
            }
        }
        return best;
    }

    /**
     * A hold's fief: every province under the highest title seated at {@code seat} (an empire's seat, all the empire's),
     * or none if no title is seated there.
     */
    public List<Integer> fief(int seat) {
        Tier tier = seatOf(seat);
        List<Integer> provinces = new ArrayList<>();
        if (tier == null) {
            return provinces;
        }
        int title = titleOf(seat, tier);
        for (int p = 0; p < county.length; p++) {
            if (county[p] >= 0 && titleOf(p, tier) == title) {
                provinces.add(p);
            }
        }
        return provinces;
    }

    /** A title's rank as its fief now stands: a duchy's worth of land or more is a duke's, and so on; at least a count's. */
    public static final double[] RANK_WORTH = {0, 3, 6, 18};

    /**
     * The rank of whoever holds the hold at {@code seat}, by the size of its fief in counties' worth (provinces over the
     * drawn map's average county): its titles keep their names, but a duchy left with one county is a count's, a county
     * grown to three counties' worth a duke's. Null if no title is seated there.
     */
    public Tier rank(int seat) {
        if (seatOf(seat) == null) {
            return null;
        }
        int land = 0;
        for (int c : county) {
            land += c >= 0 ? 1 : 0;
        }
        double worth = fief(seat).size() / ((double) land / Math.max(1, counties()));
        Tier rank = Tier.COUNTY;
        for (Tier t : Tier.values()) {
            if (worth >= RANK_WORTH[t.ordinal()]) {
                rank = t;
            }
        }
        return rank;
    }

    /** Each title map's groups as drawn, before any conquest moved land between them. */
    private static final java.util.Map<TitleMap, int[][]> DRAWN = new java.util.IdentityHashMap<>();

    /**
     * Moves land between titles as it has changed hands, in place, from the titles as drawn: a province held by someone
     * other than the holder of its county's seat joins a neighbouring county whose seat its holder holds, if one touches
     * it (and land beyond it, once that has); likewise a county between duchies, a duchy between kingdoms and a kingdom
     * between empires. A seat never leaves its title, and land given back goes back where it was drawn.
     * {@code holders[province]} is who holds each province; {@code adjacent} which provinces touch.
     */
    public void regroup(int[] holders, List<? extends java.util.Set<Integer>> adjacent) {
        int[][] drawn;
        synchronized (DRAWN) {
            if (DRAWN.size() > 8 && !DRAWN.containsKey(this)) {
                DRAWN.clear(); // old worlds' maps, long gone
            }
            drawn = DRAWN.computeIfAbsent(this, t -> new int[][]{t.county.clone(), t.duchy.clone(), t.kingdom.clone(), t.empire.clone()});
        }
        int[][] seats = {countySeat, duchySeat, kingdomSeat, empireSeat};
        int[][] unitSeats = {null, countySeat, duchySeat, kingdomSeat};
        List<? extends java.util.Set<Integer>> adj = adjacent;
        int[] below = null;
        for (int level = 0; level < 4; level++) {
            int[] of = drawn[level].clone();
            if (level > 0) {
                adj = lift(adj, below, of.length);
            }
            int[] owner = new int[of.length];
            for (int u = 0; u < of.length; u++) {
                int at = level == 0 ? u : unitSeats[level][u];
                owner[u] = at >= 0 && at < holders.length ? holders[at] : -1;
            }
            shift(of, seats[level], owner, holders, adj);
            System.arraycopy(of, 0, new int[][]{county, duchy, kingdom, empire}[level], 0, of.length);
            below = of;
        }
    }

    /** Moves each unit held apart from its group's seat into a touching group whose seat its holder holds, until none moves. */
    private static void shift(int[] of, int[] seat, int[] owner, int[] holders, List<? extends java.util.Set<Integer>> adjacent) {
        boolean moved = true;
        while (moved) {
            moved = false;
            for (int u = 0; u < of.length; u++) {
                int o = owner[u];
                if (of[u] < 0 || o < 0 || holders[seat[of[u]]] == o) {
                    continue;
                }
                int best = -1;
                for (int v : adjacent.get(u)) {
                    int g = of[v];
                    if (g >= 0 && g != of[u] && holders[seat[g]] == o && (best < 0 || g < best)) {
                        best = g;
                    }
                }
                if (best >= 0) {
                    of[u] = best;
                    moved = true;
                }
            }
        }
    }

    /** The seats of the titles a province lies in, empire first down to county; empty if it lies in none. */
    public int[] seatsOver(int province) {
        int c = county[province];
        if (c < 0) {
            return new int[0];
        }
        int d = duchy[c];
        int k = kingdom[d];
        return new int[]{empireSeat[empire[k]], kingdomSeat[k], duchySeat[d], countySeat[c]};
    }

    /** The title of this tier a province lies in. */
    public int titleOf(int province, Tier tier) {
        int t = county[province];
        if (tier.ordinal() >= Tier.DUCHY.ordinal()) {
            t = duchy[t];
        }
        if (tier.ordinal() >= Tier.KINGDOM.ordinal()) {
            t = kingdom[t];
        }
        if (tier == Tier.EMPIRE) {
            t = empire[t];
        }
        return t;
    }

    /**
     * Draws the titles over provinces on a {@code width} × {@code height} grid ({@code provinces[pixel]} is a province
     * index, or negative for none). {@code sea[pixel]} marks the sea, for telling shore provinces.
     */
    public static TitleMap build(long seed, int width, int height, int[] provinces, int count, boolean[] sea) {
        Random random = new Random(seed ^ SALT);
        List<TreeSet<Integer>> adjacent = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            adjacent.add(new TreeSet<>());
        }
        boolean[] shore = new boolean[count];
        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                int p = provinces[z * width + x];
                if (p < 0) {
                    continue;
                }
                for (int[] d : new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}}) {
                    int nx = x + d[0];
                    int nz = z + d[1];
                    if (nx < 0 || nz < 0 || nx >= width || nz >= height) {
                        continue;
                    }
                    int i = nz * width + nx;
                    int q = provinces[i];
                    if (q >= 0 && q != p) {
                        adjacent.get(p).add(q);
                    } else if (q < 0 && sea[i]) {
                        shore[p] = true;
                    }
                }
            }
        }
        int[] county = group(count, adjacent, MIN[0], MAX[0], random);
        int counties = max(county) + 1;
        int[] duchy = group(counties, lift(adjacent, county, counties), MIN[1], MAX[1], random);
        int duchies = max(duchy) + 1;
        int[] kingdom = group(duchies, lift(lift(adjacent, county, counties), duchy, duchies), MIN[2], MAX[2], random);
        int kingdoms = max(kingdom) + 1;
        List<TreeSet<Integer>> kingdomAdjacent = lift(lift(lift(adjacent, county, counties), duchy, duchies), kingdom, kingdoms);
        int[] empire = group(kingdoms, kingdomAdjacent, MIN[3], MAX[3], random);
        int empires = max(empire) + 1;

        int[] countySeat = seats(counties, members(county, counties), shore, random);
        int[] duchySeat = seats(duchies, seatsOf(members(duchy, duchies), countySeat), shore, random);
        int[] kingdomSeat = seats(kingdoms, seatsOf(members(kingdom, kingdoms), duchySeat), shore, random);
        int[] empireSeat = seats(empires, seatsOf(members(empire, empires), kingdomSeat), shore, random);
        return new TitleMap(county, duchy, kingdom, empire, countySeat, duchySeat, kingdomSeat, empireSeat);
    }

    /**
     * Groups {@code n} nodes into groups of {@code min} to {@code max} neighbours: each group starts at a random
     * ungrouped node and takes random ungrouped neighbours until it reaches a size drawn between the two. Groups left
     * smaller than {@code min} join their smallest neighbouring group; one with no neighbours stands alone.
     */
    static int[] group(int n, List<TreeSet<Integer>> adjacent, int min, int max, Random random) {
        int[] of = new int[n];
        Arrays.fill(of, -1);
        List<List<Integer>> groups = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        java.util.Collections.shuffle(order, random);
        for (int start : order) {
            if (of[start] >= 0) {
                continue;
            }
            int id = groups.size();
            int target = min + random.nextInt(max - min + 1);
            List<Integer> members = new ArrayList<>(List.of(start));
            of[start] = id;
            while (members.size() < target) {
                List<Integer> free = new ArrayList<>();
                for (int m : members) {
                    for (int q : adjacent.get(m)) {
                        if (of[q] < 0 && !free.contains(q)) {
                            free.add(q);
                        }
                    }
                }
                if (free.isEmpty()) {
                    break;
                }
                int pick = free.get(random.nextInt(free.size()));
                of[pick] = id;
                members.add(pick);
            }
            groups.add(members);
        }
        // Too small: join the smallest neighbouring group (one still too small looks again once it has grown).
        boolean merged = true;
        while (merged) {
            merged = false;
            for (int g = 0; g < groups.size(); g++) {
                List<Integer> members = groups.get(g);
                if (members.isEmpty() || members.size() >= min) {
                    continue;
                }
                int best = -1;
                for (int m : members) {
                    for (int q : adjacent.get(m)) {
                        int other = of[q];
                        if (other != g && (best < 0 || groups.get(other).size() < groups.get(best).size())) {
                            best = other;
                        }
                    }
                }
                if (best < 0) {
                    continue; // nobody to join: it stands alone
                }
                for (int m : members) {
                    of[m] = best;
                }
                groups.get(best).addAll(members);
                members.clear();
                merged = true;
            }
        }
        // Number the groups left, in order.
        int[] renumber = new int[groups.size()];
        int next = 0;
        for (int g = 0; g < groups.size(); g++) {
            renumber[g] = groups.get(g).isEmpty() ? -1 : next++;
        }
        for (int i = 0; i < n; i++) {
            of[i] = renumber[of[i]];
        }
        return of;
    }

    /** Which groups neighbour which, from which of their members do. */
    static List<TreeSet<Integer>> lift(List<? extends java.util.Set<Integer>> adjacent, int[] of, int groups) {
        List<TreeSet<Integer>> lifted = new ArrayList<>(groups);
        for (int g = 0; g < groups; g++) {
            lifted.add(new TreeSet<>());
        }
        for (int i = 0; i < adjacent.size(); i++) {
            for (int q : adjacent.get(i)) {
                if (of[i] >= 0 && of[q] >= 0 && of[i] != of[q]) {
                    lifted.get(of[i]).add(of[q]);
                }
            }
        }
        return lifted;
    }

    private static List<List<Integer>> members(int[] of, int groups) {
        List<List<Integer>> members = new ArrayList<>(groups);
        for (int g = 0; g < groups; g++) {
            members.add(new ArrayList<>());
        }
        for (int i = 0; i < of.length; i++) {
            if (of[i] >= 0) {
                members.get(of[i]).add(i);
            }
        }
        return members;
    }

    /** Each group's candidate seats: the seats of the titles it is made of. */
    private static List<List<Integer>> seatsOf(List<List<Integer>> members, int[] seat) {
        List<List<Integer>> seats = new ArrayList<>(members.size());
        for (List<Integer> m : members) {
            List<Integer> s = new ArrayList<>(m.size());
            m.forEach(t -> s.add(seat[t]));
            seats.add(s);
        }
        return seats;
    }

    /** A seat for each group, drawn among its candidates, shores {@link #SHORE_WEIGHT} times as likely. */
    private static int[] seats(int groups, List<List<Integer>> candidates, boolean[] shore, Random random) {
        int[] seat = new int[groups];
        for (int g = 0; g < groups; g++) {
            List<Integer> c = candidates.get(g);
            int total = 0;
            for (int p : c) {
                total += shore[p] ? SHORE_WEIGHT : 1;
            }
            int pick = random.nextInt(total);
            for (int p : c) {
                pick -= shore[p] ? SHORE_WEIGHT : 1;
                if (pick < 0) {
                    seat[g] = p;
                    break;
                }
            }
        }
        return seat;
    }

    private static int max(int[] values) {
        int m = -1;
        for (int v : values) {
            m = Math.max(m, v);
        }
        return m;
    }
}
