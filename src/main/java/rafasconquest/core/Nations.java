package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Who takes up the land's titles, decided once the titles are drawn (never the other way round), by zone, from where
 * each title's seat stands:
 * <ul>
 *   <li><b>The eye</b> (within the eye radius of spawn) and <b>mini-eyes</b> (as big as the eye, about one for every
 *       {@link #KINGDOMS_PER_MINI_EYE} kingdoms of the outer world, centred on a kingdom's seat): no nation.</li>
 *   <li><b>The war zone</b> (out to its outer edge): a kingdom is held whole by a small nation
 *       {@link #SMALL_NATION_CHANCE} of the time; otherwise each of its counties is a City-state
 *       {@link #CITY_STATE_CHANCE} of the time, and the rest is nobody's.</li>
 *   <li><b>The outer world</b>: an empire is held whole by one great nation {@link #EMPIRE_CHANCE} of the time;
 *       otherwise each of its kingdoms is a large nation {@link #LARGE_NATION_CHANCE} of the time, and the rest is
 *       nobody's.</li>
 * </ul>
 * A nation holds its title and every title within it; its seat is the title's seat. The provinces of titles no nation
 * holds are nobody's, and their seats are unowned.
 * <p>
 * The world has at most {@link #MAX_CITY_STATES} City-states, and the known world at least {@link #MIN_NATIONS}
 * nations (sword sanctums not counted): if the rolls leave fewer, more of its free kingdoms, then duchies, become
 * nations, in a seeded order.
 */
public final class Nations {
    static final double SMALL_NATION_CHANCE = 0.4;
    static final double CITY_STATE_CHANCE = 0.35;
    static final double EMPIRE_CHANCE = 0.35;
    static final double LARGE_NATION_CHANCE = 0.7;
    static final int KINGDOMS_PER_MINI_EYE = 4;
    static final int MIN_NATIONS = 12;
    static final int MAX_CITY_STATES = 4;
    private static final long SALT = 0x4E4154494F4E5321L;

    /** How many sword sanctums a world has. */
    public static final int SANCTUMS = 3;

    /**
     * A nation: the title it holds (tier and index), its seat province, where its seat city stands, and whether it is a
     * sword sanctum (a City-state of a Sword God's).
     */
    public record Nation(TitleMap.Tier tier, int title, int seat, int x, int z, boolean sanctum) {
        public Nation(TitleMap.Tier tier, int title, int seat, int x, int z) {
            this(tier, title, seat, x, z, false);
        }
    }

    /** The nations, which nation (index) holds each province (-1: nobody), and the mini-eyes' centres. */
    public record Plan(List<Nation> nations, int[] holder, List<int[]> miniEyes) {
    }

    /** Where each seat stands (its city, or its province's centre), for zones: (x, z) of a province. */
    @FunctionalInterface
    public interface Seats {
        int[] at(int province);
    }

    private Nations() {
    }

    public static Plan plan(long seed, TitleMap t, Seats seats, int eyeRadius, int warZoneEdge) {
        return plan(seed, t, seats, eyeRadius, warZoneEdge, Integer.MAX_VALUE, SANCTUMS);
    }

    /**
     * The same, with the known world's radius (seats within it count towards {@link #MIN_NATIONS}) and this many sword
     * sanctums (none where the world has no martial titles).
     */
    public static Plan plan(long seed, TitleMap t, Seats seats, int eyeRadius, int warZoneEdge, int knownRadius, int sanctumCount) {
        // Empires and kingdoms, rolled or topped up, can take so much land that too little is left free for the rest;
        // then the plan is drawn again topping up with duchies alone, then with one empire fewer each time, then (no
        // empires left) one rolled kingdom fewer.
        int maxEmpires = Integer.MAX_VALUE;
        int maxKingdoms = Integer.MAX_VALUE;
        while (true) {
            Plan plan = null;
            for (boolean topUpKingdoms : new boolean[]{true, false}) {
                plan = plan(seed, t, seats, eyeRadius, warZoneEdge, knownRadius, sanctumCount, maxEmpires, maxKingdoms, topUpKingdoms);
                if (known(plan.nations(), knownRadius) >= MIN_NATIONS) {
                    return plan;
                }
            }
            int empires = count(plan.nations(), TitleMap.Tier.EMPIRE);
            int kingdoms = count(plan.nations(), TitleMap.Tier.KINGDOM);
            if (empires > 0) {
                maxEmpires = empires - 1;
            } else if (kingdoms > 0) {
                maxKingdoms = kingdoms - 1;
            } else {
                return plan; // the known world has too little free land for more
            }
        }
    }

    private static int count(List<Nation> nations, TitleMap.Tier tier) {
        return (int) nations.stream().filter(n -> !n.sanctum() && n.tier() == tier).count();
    }

    private static Plan plan(long seed, TitleMap t, Seats seats, int eyeRadius, int warZoneEdge, int knownRadius, int sanctumCount,
                             int maxEmpires, int maxKingdoms, boolean topUpKingdoms) {
        Random random = new Random(seed ^ SALT);
        int provinces = t.county().length;
        int[] holder = new int[provinces];
        java.util.Arrays.fill(holder, -1);
        List<Nation> nations = new ArrayList<>();

        // The mini-eyes: among the outer world's kingdoms, one in every few seats one, clear of the others.
        List<int[]> miniEyes = new ArrayList<>();
        List<Integer> outer = new ArrayList<>();
        for (int k = 0; k < t.kingdoms(); k++) {
            if (distance(seats.at(t.kingdomSeat()[k])) > warZoneEdge) {
                outer.add(k);
            }
        }
        java.util.Collections.shuffle(outer, random);
        int wanted = outer.size() / KINGDOMS_PER_MINI_EYE;
        List<Integer> eyeKingdoms = new ArrayList<>();
        for (int k : outer) {
            if (miniEyes.size() >= wanted) {
                break;
            }
            int[] at = seats.at(t.kingdomSeat()[k]);
            boolean clear = miniEyes.stream().allMatch(e -> Math.hypot(e[0] - at[0], e[1] - at[1]) > 4.0 * eyeRadius)
                    && distance(at) > warZoneEdge + eyeRadius;
            if (clear) {
                miniEyes.add(at);
                eyeKingdoms.add(k);
            }
        }

        // The sword sanctums: in the mini-eyes' hearts (a kingdom's seat), else, if there are too few, at other kingdoms'
        // seats of the outer world; each a City-state holding the county round its seat, calm or not.
        List<Integer> sanctums = new ArrayList<>(eyeKingdoms);
        for (int k : outer) {
            if (sanctums.size() >= sanctumCount) {
                break;
            }
            int[] at = seats.at(t.kingdomSeat()[k]);
            if (!sanctums.contains(k) && distance(at) > warZoneEdge + eyeRadius
                    && sanctums.stream().allMatch(s -> far(seats.at(t.kingdomSeat()[s]), at, 4.0 * eyeRadius))) {
                sanctums.add(k);
            }
        }
        for (int k : sanctums.subList(0, Math.min(sanctumCount, sanctums.size()))) {
            int seat = t.kingdomSeat()[k];
            int county = t.county()[seat];
            int[] at = seats.at(seat);
            int index = nations.size();
            nations.add(new Nation(TitleMap.Tier.COUNTY, county, seat, at[0], at[1], true));
            for (int p = 0; p < provinces; p++) {
                if (t.county()[p] == county) {
                    holder[p] = index;
                }
            }
        }

        // The outer world: empires, else large nations in their kingdoms.
        for (int e = 0; e < t.empires(); e++) {
            int[] at = seats.at(t.empireSeat()[e]);
            if (distance(at) <= warZoneEdge || calm(at, eyeRadius, miniEyes)) {
                continue;
            }
            if (random.nextDouble() < EMPIRE_CHANCE && count(nations, TitleMap.Tier.EMPIRE) < maxEmpires) {
                take(t, nations, holder, new Nation(TitleMap.Tier.EMPIRE, e, t.empireSeat()[e], at[0], at[1]), seats, eyeRadius, miniEyes);
            }
        }
        for (int k = 0; k < t.kingdoms(); k++) {
            int seat = t.kingdomSeat()[k];
            int[] at = seats.at(seat);
            if (held(t, holder, seat) || calm(at, eyeRadius, miniEyes) || distance(at) <= eyeRadius) {
                continue;
            }
            boolean warZone = distance(at) <= warZoneEdge;
            double chance = warZone ? SMALL_NATION_CHANCE : LARGE_NATION_CHANCE;
            if (random.nextDouble() < chance && count(nations, TitleMap.Tier.KINGDOM) < maxKingdoms) {
                take(t, nations, holder, new Nation(TitleMap.Tier.KINGDOM, k, seat, at[0], at[1]), seats, eyeRadius, miniEyes);
            } else if (warZone) {
                // City-states in the kingdom's counties.
                for (int c = 0; c < t.counties(); c++) {
                    if (t.kingdom()[t.duchy()[c]] != k) {
                        continue;
                    }
                    int cs = t.countySeat()[c];
                    int[] cat = seats.at(cs);
                    if (distance(cat) > eyeRadius && random.nextDouble() < CITY_STATE_CHANCE && cityStates(nations) < MAX_CITY_STATES) {
                        take(t, nations, holder, new Nation(TitleMap.Tier.COUNTY, c, cs, cat[0], cat[1]), seats, eyeRadius, miniEyes);
                    }
                }
            }
        }

        // Too few nations in the known world: free kingdoms there become nations, then free duchies.
        List<Integer> kingdoms = new ArrayList<>();
        for (int k = 0; k < t.kingdoms(); k++) {
            kingdoms.add(k);
        }
        java.util.Collections.shuffle(kingdoms, random);
        for (int k : topUpKingdoms ? kingdoms : List.<Integer>of()) {
            fill(t, nations, holder, TitleMap.Tier.KINGDOM, k, t.kingdomSeat()[k], seats, eyeRadius, knownRadius, miniEyes);
        }
        List<Integer> duchies = new ArrayList<>();
        for (int d = 0; d < t.duchies(); d++) {
            duchies.add(d);
        }
        java.util.Collections.shuffle(duchies, random);
        for (int d : duchies) {
            fill(t, nations, holder, TitleMap.Tier.DUCHY, d, t.duchySeat()[d], seats, eyeRadius, knownRadius, miniEyes);
        }
        return new Plan(nations, holder, miniEyes);
    }

    /**
     * Gives the nations whole, thick land, once their titles are taken: each holds one piece round its seat (land cut off
     * from it is let go); free land a nation mostly surrounds joins it, and its land reaching out thinly (less than a
     * third of a province's neighbours its own) is let go or joins the nation that mostly surrounds it, over
     * {@link #SMOOTHING_ROUNDS} rounds, never cutting a nation in two; last, free land wholly within one nation (a hole:
     * touching no other nation, the map's edge or a calm) is filled. The calms stay nobody's, and the sword sanctums keep
     * their county as it is.
     */
    public static void consolidate(Plan plan, List<? extends java.util.Set<Integer>> adjacent, boolean[] mapEdge, Seats seats, int eyeRadius) {
        int[] holder = plan.holder();
        List<Nation> nations = plan.nations();
        boolean[] open = new boolean[holder.length];
        for (int p = 0; p < holder.length; p++) {
            int[] at = seats.at(p);
            open[p] = distance(at) > eyeRadius && !calm(at, eyeRadius, plan.miniEyes());
        }
        for (int n = 0; n < nations.size(); n++) {
            if (!nations.get(n).sanctum()) {
                boolean[] reached = reach(holder, adjacent, n, nations.get(n).seat());
                for (int p = 0; p < holder.length; p++) {
                    if (holder[p] == n && !reached[p]) {
                        holder[p] = -1;
                    }
                }
            }
        }
        for (int round = 0; round < SMOOTHING_ROUNDS; round++) {
            for (int p = 0; p < holder.length; p++) {
                if (holder[p] < 0 && open[p]) {
                    holder[p] = surrounding(holder, adjacent, nations, p, -1);
                }
            }
            for (int p = 0; p < holder.length; p++) {
                int n = holder[p];
                if (n < 0 || nations.get(n).sanctum() || p == nations.get(n).seat()) {
                    continue;
                }
                int own = 0;
                for (int q : adjacent.get(p)) {
                    if (holder[q] == n) {
                        own++;
                    }
                }
                if (own * 3 >= adjacent.get(p).size()) {
                    continue;
                }
                holder[p] = surrounding(holder, adjacent, nations, p, n);
                if (!whole(holder, adjacent, n, nations.get(n).seat())) {
                    holder[p] = n; // it would cut its nation in two
                }
            }
        }
        // The holes: free land wholly within one nation.
        boolean[] seen = new boolean[holder.length];
        for (int start = 0; start < holder.length; start++) {
            if (holder[start] >= 0 || !open[start] || seen[start]) {
                continue;
            }
            List<Integer> hole = new ArrayList<>();
            java.util.Set<Integer> around = new java.util.TreeSet<>();
            boolean enclosed = true;
            java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>(List.of(start));
            seen[start] = true;
            while (!queue.isEmpty()) {
                int p = queue.poll();
                hole.add(p);
                enclosed &= !mapEdge[p];
                for (int q : adjacent.get(p)) {
                    if (holder[q] >= 0) {
                        around.add(holder[q]);
                    } else if (!open[q]) {
                        enclosed = false; // it opens onto a calm
                    } else if (!seen[q]) {
                        seen[q] = true;
                        queue.add(q);
                    }
                }
            }
            if (enclosed && around.size() == 1 && !nations.get(around.iterator().next()).sanctum()) {
                int n = around.iterator().next();
                hole.forEach(p -> holder[p] = n);
            }
        }
    }

    /** Rounds of thickening and thinning in {@link #consolidate}. */
    static final int SMOOTHING_ROUNDS = 4;

    /** The nation (not a sanctum, nor {@code not}) holding more than half of a province's neighbours, or -1. */
    private static int surrounding(int[] holder, List<? extends java.util.Set<Integer>> adjacent, List<Nation> nations, int p, int not) {
        java.util.Map<Integer, Integer> count = new java.util.HashMap<>();
        for (int q : adjacent.get(p)) {
            if (holder[q] >= 0 && holder[q] != not && !nations.get(holder[q]).sanctum()) {
                count.merge(holder[q], 1, Integer::sum);
            }
        }
        for (java.util.Map.Entry<Integer, Integer> e : count.entrySet()) {
            if (e.getValue() * 2 > adjacent.get(p).size()) {
                return e.getKey();
            }
        }
        return -1;
    }

    /** The provinces of a nation's land reached from its seat through its own. */
    private static boolean[] reach(int[] holder, List<? extends java.util.Set<Integer>> adjacent, int n, int seat) {
        boolean[] reached = new boolean[holder.length];
        if (holder[seat] != n) {
            return reached;
        }
        java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>(List.of(seat));
        reached[seat] = true;
        while (!queue.isEmpty()) {
            for (int q : adjacent.get(queue.poll())) {
                if (holder[q] == n && !reached[q]) {
                    reached[q] = true;
                    queue.add(q);
                }
            }
        }
        return reached;
    }

    /** Whether a nation's land is one piece. */
    static boolean whole(int[] holder, List<? extends java.util.Set<Integer>> adjacent, int n, int seat) {
        boolean[] reached = reach(holder, adjacent, n, seat);
        for (int p = 0; p < holder.length; p++) {
            if (holder[p] == n && !reached[p]) {
                return false;
            }
        }
        return true;
    }

    /** While the known world has too few nations, a title whose seat is free there becomes one. */
    private static void fill(TitleMap t, List<Nation> nations, int[] holder, TitleMap.Tier tier, int title, int seat, Seats seats,
                             int eyeRadius, int knownRadius, List<int[]> miniEyes) {
        int[] at = seats.at(seat);
        if (known(nations, knownRadius) >= MIN_NATIONS || held(t, holder, seat) || calm(at, eyeRadius, miniEyes)
                || distance(at) <= eyeRadius || distance(at) > knownRadius) {
            return;
        }
        take(t, nations, holder, new Nation(tier, title, seat, at[0], at[1]), seats, eyeRadius, miniEyes);
    }

    /** The nations (not sword sanctums) seated in the known world. */
    static int known(List<Nation> nations, int knownRadius) {
        return (int) nations.stream().filter(n -> !n.sanctum() && Math.hypot(n.x(), n.z()) <= knownRadius).count();
    }

    /** The City-states (not sword sanctums). */
    static int cityStates(List<Nation> nations) {
        return (int) nations.stream().filter(n -> !n.sanctum() && n.tier() == TitleMap.Tier.COUNTY).count();
    }

    /** A nation takes its title: every province under it not in a calm (the eye or a mini-eye) is its. */
    private static void take(TitleMap t, List<Nation> nations, int[] holder, Nation n, Seats seats, int eyeRadius, List<int[]> miniEyes) {
        int index = nations.size();
        nations.add(n);
        for (int p = 0; p < holder.length; p++) {
            if (holder[p] < 0 && under(t, p, n.tier(), n.title()) && !calm(seats.at(p), eyeRadius, miniEyes) && distance(seats.at(p)) > eyeRadius) {
                holder[p] = index;
            }
        }
    }

    /** Whether a province lies under a title. */
    static boolean under(TitleMap t, int province, TitleMap.Tier tier, int title) {
        int c = t.county()[province];
        return switch (tier) {
            case COUNTY -> c == title;
            case DUCHY -> t.duchy()[c] == title;
            case KINGDOM -> t.kingdom()[t.duchy()[c]] == title;
            case EMPIRE -> t.empire()[t.kingdom()[t.duchy()[c]]] == title;
        };
    }

    private static boolean held(TitleMap t, int[] holder, int province) {
        return holder[province] >= 0;
    }

    private static boolean calm(int[] at, int eyeRadius, List<int[]> miniEyes) {
        for (int[] e : miniEyes) {
            if (Math.hypot(e[0] - at[0], e[1] - at[1]) <= eyeRadius) {
                return true;
            }
        }
        return false;
    }

    private static boolean far(int[] a, int[] b, double apart) {
        return Math.hypot(a[0] - b[0], a[1] - b[1]) > apart;
    }

    private static double distance(int[] at) {
        return Math.hypot(at[0], at[1]);
    }
}
