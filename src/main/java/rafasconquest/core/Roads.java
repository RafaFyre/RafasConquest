package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * The world's roads, planned on the world map with the cities:
 * <ul>
 *   <li><b>High roads</b> (cobblestone, 5 wide) join each capital (a kingdom's or empire's seat city) to its nearest
 *       capitals; <b>city roads</b> (gravel with dirt-path edges, 4 wide) join each small city (a duchy's seat) to its
 *       capital; <b>town roads</b> (dirt path, 3 wide) join each town (a county's seat) to its city, and to its nearest
 *       town, for loops. A road between two ranks takes the lower one's grade.</li>
 *   <li><b>Spurs</b>: a city-grade road to every castle and a dirt path to every village, from the nearest road; a
 *       cobblestone road to every known dojo from the nearest fork (a junction). Hamlets and hidden dojos have none.</li>
 * </ul>
 * Routes are found over the map's pixels, cheapest first: steep and high ground, jungles and above all water cost more
 * (a river is crossed where it is narrow: a bridge), and the sea most of all; a road already laid costs less, so roads
 * share their way and meet at forks. Roads leave and enter cities by the gate facing where they go. The pixel steps are
 * smoothed into gentle curves.
 */
public final class Roads {
    public enum Grade {
        TOWN(3), CITY(4), HIGH(5), DOJO(3);

        private final int width;

        Grade(int width) {
            this.width = width;
        }

        public int width() {
            return width;
        }
    }

    /** A road: its grade, its points every {@link #POINT_SPACING} blocks or so, and the names of its two ends. */
    public record Road(Grade grade, int[] xs, int[] zs, String from, String to) {
        public int points() {
            return xs.length;
        }
    }

    /** A fork, where roads meet, and the places its roads lead to. */
    public record Junction(int x, int z, List<String> leadsTo) {
    }

    public record Plan(List<Road> roads, List<Junction> junctions) {
        public static final Plan EMPTY = new Plan(List.of(), List.of());
    }

    /**
     * The map the roads are planned on: per pixel ({@code bpp} blocks), the ground's height, whether it lies in water (sea
     * or river), whether in the sea itself, and how hard its land is otherwise (0 plain, 1 jungle, 2 mountains).
     */
    public record Land(int minX, int minZ, int bpp, int width, int height, int[] ground, boolean[] wet, boolean[] sea,
                       byte[] hard, int seaLevel) {
        int index(int px, int pz) {
            return pz * width + px;
        }

        /** The pixel under (x, z), held onto the map's edge. */
        int pixel(int x, int z) {
            return index(clamp(Math.floorDiv(x - minX, bpp), width), clamp(Math.floorDiv(z - minZ, bpp), height));
        }

        /** A pixel's middle, in blocks. */
        int[] centre(int pixel) {
            return new int[]{minX + (pixel % width) * bpp + bpp / 2, minZ + (pixel / width) * bpp + bpp / 2};
        }
    }

    /** A place a road can reach: a city (its square {@code half} blocks each way), a castle, village or dojo (a point). */
    public record Place(String name, int x, int z, int half, int districts) {
    }

    static final int POINT_SPACING = 8;
    /** A spur looks this many pixels about for a road (or a fork) to join. */
    static final int SPUR_REACH = 40;
    /** How far past a city's walls a road begins. */
    static final int GATE_OUT = 4;
    /** How far a road runs straight out from a city's gate before turning toward where it goes. */
    static final int GATE_STUB = 48;
    private static final double WATER_COST = 25;
    private static final double SEA_COST = 300;
    private static final double CITY_COST = 200;
    private static final double JUNGLE_COST = 3;
    private static final double MOUNTAIN_COST = 6;
    private static final double CLIMB_COST = 0.6;
    private static final double HIGH_GROUND_COST = 0.05;
    private static final double REUSE = 0.35;

    private Roads() {
    }

    /** One road wanted: of a grade, between two places. */
    public record Link(Grade grade, Place a, Place b) {
    }

    /** A spur wanted: of a grade, from a place to the nearest road (or fork). */
    public record Spur(Grade grade, Place place, boolean toFork) {
    }

    /** How many nearest capitals each capital's high roads reach. */
    static final int CAPITAL_LINKS = 2;
    /** A town's road to its nearest town reaches no further than this. */
    static final int TOWN_LINK_REACH = 3000;

    /** A city as a place roads reach: named after its province, its square its walls. */
    public static Place place(SeatCities.City city, String name) {
        return new Place(name, city.x(), city.z(), city.size() / 2, city.districts());
    }

    /**
     * The roads the seat cities want: capitals (kingdom and empire seats) to their nearest capitals; small cities
     * (duchy seats) to their kingdom's capital (else the nearest); towns (county seats) to their duchy's city (else the
     * nearest city), and to their nearest town if near enough.
     */
    public static List<Link> links(List<SeatCities.City> cities, TitleMap t, List<String> names) {
        List<Link> links = new ArrayList<>();
        Set<Long> joined = new HashSet<>();
        List<SeatCities.City> capitals = cities.stream().filter(c -> c.tier().ordinal() >= TitleMap.Tier.KINGDOM.ordinal()).toList();
        List<SeatCities.City> greater = cities.stream().filter(c -> c.tier().ordinal() >= TitleMap.Tier.DUCHY.ordinal()).toList();
        List<SeatCities.City> towns = cities.stream().filter(c -> c.tier() == TitleMap.Tier.COUNTY).toList();
        for (SeatCities.City c : capitals) {
            nearest(c, capitals, CAPITAL_LINKS, Integer.MAX_VALUE).forEach(o -> link(links, joined, Grade.HIGH, c, o, names));
        }
        for (SeatCities.City c : cities) {
            if (c.tier() == TitleMap.Tier.DUCHY) {
                SeatCities.City seat = cityAt(cities, t.kingdomSeat()[t.kingdom()[t.duchy()[t.county()[c.province()]]]]);
                SeatCities.City to = seat != null && seat != c ? seat : first(nearest(c, capitals, 1, Integer.MAX_VALUE));
                if (to != null) {
                    link(links, joined, Grade.CITY, c, to, names);
                }
            }
        }
        for (SeatCities.City c : towns) {
            SeatCities.City seat = cityAt(cities, t.duchySeat()[t.duchy()[t.county()[c.province()]]]);
            SeatCities.City to = seat != null && seat != c ? seat : first(nearest(c, greater, 1, Integer.MAX_VALUE));
            if (to != null) {
                link(links, joined, Grade.TOWN, c, to, names);
            }
            SeatCities.City town = first(nearest(c, towns, 1, TOWN_LINK_REACH));
            if (town != null) {
                link(links, joined, Grade.TOWN, c, town, names);
            }
        }
        return links;
    }

    private static void link(List<Link> links, Set<Long> joined, Grade grade, SeatCities.City a, SeatCities.City b, List<String> names) {
        long key = Math.min(a.province(), b.province()) * 1_000_003L + Math.max(a.province(), b.province());
        if (a != b && joined.add(key)) {
            links.add(new Link(grade, place(a, names.get(a.province())), place(b, names.get(b.province()))));
        }
    }

    private static SeatCities.City cityAt(List<SeatCities.City> cities, int province) {
        for (SeatCities.City c : cities) {
            if (c.province() == province) {
                return c;
            }
        }
        return null;
    }

    private static SeatCities.City first(List<SeatCities.City> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    private static List<SeatCities.City> nearest(SeatCities.City c, List<SeatCities.City> among, int n, int reach) {
        return among.stream().filter(o -> o != c && Math.hypot(o.x() - c.x(), o.z() - c.z()) <= reach)
                .sorted(java.util.Comparator.comparingDouble(o -> Math.hypot(o.x() - c.x(), o.z() - c.z()))).limit(n).toList();
    }

    public static Plan plan(Land land, List<Link> links, List<Spur> spurs, List<Place> cities) {
        Router router = new Router(land, cities);
        List<Road> roads = new ArrayList<>();
        // The best roads first, so the lesser ones join them.
        List<Link> ordered = new ArrayList<>(links);
        ordered.sort((p, q) -> Integer.compare(rank(q.grade()), rank(p.grade())));
        for (Link link : ordered) {
            int[] a = gate(link.a(), link.b());
            int[] b = gate(link.b(), link.a());
            // Straight out of each gate first, so the road meets the gate head on rather than running along the wall.
            int[] sa = outside(link.a(), link.b());
            int[] sb = outside(link.b(), link.a());
            List<Integer> path = router.route(land.pixel(sa[0], sa[1]), land.pixel(sb[0], sb[1]), false);
            if (path != null) {
                roads.add(router.lay(link.grade(), path, a, sa, sb, b, link.a().name(), link.b().name()));
            }
        }
        for (Spur spur : spurs) {
            int start = land.pixel(spur.place().x(), spur.place().z());
            List<Integer> path = router.route(start, -1, spur.toFork());
            if (path != null && path.size() > 1) {
                int end = path.get(path.size() - 1);
                int[] at = land.centre(end);
                roads.add(router.lay(spur.grade(), path, new int[]{spur.place().x(), spur.place().z()}, null, null, at, spur.place().name(),
                        router.nameAt(end)));
            }
        }
        return new Plan(roads, router.junctions(roads));
    }

    private static int rank(Grade g) {
        return switch (g) {
            case HIGH -> 3;
            case CITY -> 2;
            case TOWN -> 1;
            case DOJO -> 0;
        };
    }

    /** Where a road to {@code toward} leaves a place: a city's gate facing it, just outside the walls; else the place. */
    static int[] gate(Place place, Place toward) {
        if (place.half() <= 0) {
            return new int[]{place.x(), place.z()};
        }
        int dx = toward.x() - place.x();
        int dz = toward.z() - place.z();
        int blocks = place.districts() * CityLayout.BLOCKS_PER_CELL;
        int origin = -place.half();
        int along = origin + CityLayout.roadStart(blocks / 2) + 1; // the gate avenue's middle, from the centre
        int out = place.half() + GATE_OUT;
        return Math.abs(dx) >= Math.abs(dz)
                ? new int[]{place.x() + Integer.signum(dx) * out, place.z() + along}
                : new int[]{place.x() + along, place.z() + Integer.signum(dz) * out};
    }

    /** {@link #GATE_STUB} blocks straight out from the gate a road to {@code toward} leaves by (the place, for a point). */
    static int[] outside(Place place, Place toward) {
        int[] g = gate(place, toward);
        if (place.half() <= 0) {
            return g;
        }
        int dx = toward.x() - place.x();
        int dz = toward.z() - place.z();
        return Math.abs(dx) >= Math.abs(dz)
                ? new int[]{g[0] + Integer.signum(dx) * GATE_STUB, g[1]}
                : new int[]{g[0], g[1] + Integer.signum(dz) * GATE_STUB};
    }

    /** The pixel under (x, z), clamped onto the map. */
    private static int clamp(int v, int max) {
        return Math.max(0, Math.min(max - 1, v));
    }

    /** Finds and lays roads over the map. */
    private static final class Router {
        private final Land land;
        private final double[] cost;
        /** The best grade laid on each pixel (rank, 0 none), which road reached it first, and the forks. */
        private final byte[] laid;
        private final String[] near;
        private final Set<Integer> forks = new HashSet<>();

        Router(Land land, List<Place> cities) {
            this.land = land;
            int n = land.width() * land.height();
            cost = new double[n];
            laid = new byte[n];
            near = new String[n];
            for (int i = 0; i < n; i++) {
                double c = 1;
                if (land.sea()[i]) {
                    c += SEA_COST;
                } else if (land.wet()[i]) {
                    c += WATER_COST;
                }
                c += land.hard()[i] == 1 ? JUNGLE_COST : land.hard()[i] == 2 ? MOUNTAIN_COST : 0;
                c += Math.max(0, land.ground()[i] - land.seaLevel() - 40) * HIGH_GROUND_COST;
                cost[i] = c;
            }
            // The cities' squares: roads go round, not through.
            for (Place city : cities) {
                int x0 = clamp(Math.floorDiv(city.x() - city.half() - land.minX(), land.bpp()), land.width());
                int x1 = clamp(Math.floorDiv(city.x() + city.half() - land.minX(), land.bpp()), land.width());
                int z0 = clamp(Math.floorDiv(city.z() - city.half() - land.minZ(), land.bpp()), land.height());
                int z1 = clamp(Math.floorDiv(city.z() + city.half() - land.minZ(), land.bpp()), land.height());
                for (int pz = z0; pz <= z1; pz++) {
                    for (int px = x0; px <= x1; px++) {
                        cost[land.index(px, pz)] += CITY_COST;
                    }
                }
            }
        }

        /**
         * The cheapest way from {@code start} to {@code goal} (pixels); with {@code goal} -1, to the nearest road (or, with
         * {@code toFork}, the nearest fork, failing which the nearest road) within {@link #SPUR_REACH}. Null if none.
         */
        List<Integer> route(int start, int goal, boolean toFork) {
            if (start < 0 || goal < -1) {
                return null;
            }
            List<Integer> found = search(start, goal, toFork);
            if (found == null && goal < 0 && toFork) {
                found = search(start, goal, false);
            }
            return found;
        }

        private List<Integer> search(int start, int goal, boolean toFork) {
            int w = land.width();
            int sx = start % w;
            int sz = start / w;
            int gx = goal >= 0 ? goal % w : sx;
            int gz = goal >= 0 ? goal / w : sz;
            int margin = goal >= 0 ? 40 : SPUR_REACH;
            int x0 = Math.max(0, Math.min(sx, gx) - margin);
            int x1 = Math.min(w - 1, Math.max(sx, gx) + margin);
            int z0 = Math.max(0, Math.min(sz, gz) - margin);
            int z1 = Math.min(land.height() - 1, Math.max(sz, gz) + margin);
            int bw = x1 - x0 + 1;
            int bh = z1 - z0 + 1;
            double[] best = new double[bw * bh];
            int[] from = new int[bw * bh];
            Arrays.fill(best, Double.MAX_VALUE);
            PriorityQueue<double[]> open = new PriorityQueue<>((p, q) -> Double.compare(p[0], q[0]));
            int s = (sz - z0) * bw + (sx - x0);
            best[s] = 0;
            from[s] = -1;
            open.add(new double[]{0, s});
            while (!open.isEmpty()) {
                double[] top = open.poll();
                int at = (int) top[1];
                int lx = at % bw;
                int lz = at / bw;
                int pixel = land.index(x0 + lx, z0 + lz);
                double g = best[at];
                if (top[0] - heuristic(x0 + lx, z0 + lz, gx, gz, goal) > g + 1e-9) {
                    continue;
                }
                boolean arrived = goal >= 0 ? pixel == goal
                        : at != s && (toFork ? forks.contains(pixel) : laid[pixel] > 0);
                if (arrived) {
                    List<Integer> path = new ArrayList<>();
                    for (int c = at; c >= 0; c = from[c]) {
                        path.add(land.index(x0 + c % bw, z0 + c / bw));
                    }
                    java.util.Collections.reverse(path);
                    return path;
                }
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dz == 0) {
                            continue;
                        }
                        int nx = lx + dx;
                        int nz = lz + dz;
                        if (nx < 0 || nz < 0 || nx >= bw || nz >= bh) {
                            continue;
                        }
                        int next = land.index(x0 + nx, z0 + nz);
                        double step = (dx != 0 && dz != 0 ? Math.sqrt(2) : 1)
                                * (cost[next] + Math.abs(land.ground()[next] - land.ground()[pixel]) * CLIMB_COST)
                                * (laid[next] > 0 ? REUSE : 1);
                        int ni = nz * bw + nx;
                        if (g + step < best[ni]) {
                            best[ni] = g + step;
                            from[ni] = at;
                            open.add(new double[]{g + step + heuristic(x0 + nx, z0 + nz, gx, gz, goal), ni});
                        }
                    }
                }
            }
            return null;
        }

        private double heuristic(int x, int z, int gx, int gz, int goal) {
            if (goal < 0) {
                return 0;
            }
            int dx = Math.abs(x - gx);
            int dz = Math.abs(z - gz);
            return (Math.max(dx, dz) + (Math.sqrt(2) - 1) * Math.min(dx, dz)) * REUSE;
        }

        /** Lays a road along a found path: marks its pixels (and its forks), and smooths it into points. */
        Road lay(Grade grade, List<Integer> path, int[] a, int[] stubA, int[] stubB, int[] b, String fromName, String toName) {
            byte r = (byte) (rank(grade) + 1);
            boolean wasOn = false;
            for (int i = 0; i < path.size(); i++) {
                int p = path.get(i);
                boolean on = laid[p] > 0;
                if (i > 0 && on != wasOn) {
                    forks.add(on ? p : path.get(i - 1)); // where this road meets or leaves another
                }
                wasOn = on;
                if (laid[p] < r) {
                    laid[p] = r;
                }
                if (near[p] == null) {
                    near[p] = toName;
                }
            }
            // From the exact ends through the pixel centres, smoothed and spaced out.
            List<double[]> line = new ArrayList<>();
            line.add(new double[]{a[0], a[1]});
            if (stubA != null) {
                line.add(new double[]{stubA[0], stubA[1]});
            }
            for (int i = 1; i < path.size() - 1; i++) {
                int[] c = land.centre(path.get(i));
                line.add(new double[]{c[0], c[1]});
            }
            if (stubB != null) {
                line.add(new double[]{stubB[0], stubB[1]});
            }
            line.add(new double[]{b[0], b[1]});
            for (int pass = 0; pass < 3; pass++) {
                line = chaikin(line);
            }
            return spaced(grade, line, fromName, toName);
        }

        String nameAt(int pixel) {
            return near[pixel] != null ? near[pixel] : "the road";
        }

        /** The forks, each with the places of the roads that pass it. */
        List<Junction> junctions(List<Road> roads) {
            List<Junction> junctions = new ArrayList<>();
            for (int fork : forks) {
                int[] at = land.centre(fork);
                // The road points nearest the fork's pixel: the fork stands where they come closest.
                double bestDistance = Double.MAX_VALUE;
                int jx = at[0];
                int jz = at[1];
                List<String> names = new ArrayList<>();
                for (Road road : roads) {
                    boolean passes = false;
                    for (int i = 0; i < road.points(); i++) {
                        double d = Math.hypot(road.xs()[i] - at[0], road.zs()[i] - at[1]);
                        if (d <= land.bpp()) {
                            passes = true;
                            if (d < bestDistance) {
                                bestDistance = d;
                                jx = road.xs()[i];
                                jz = road.zs()[i];
                            }
                        }
                    }
                    if (passes) {
                        for (String name : new String[]{road.from(), road.to()}) {
                            if (!names.contains(name) && !name.equals("the road")) {
                                names.add(name);
                            }
                        }
                    }
                }
                if (names.size() >= 2) {
                    junctions.add(new Junction(jx, jz, List.copyOf(names.subList(0, Math.min(4, names.size())))));
                }
            }
            return junctions;
        }
    }

    /** Chaikin's corner cutting: each pass rounds every bend, keeping the two ends. */
    static List<double[]> chaikin(List<double[]> line) {
        if (line.size() < 3) {
            return line;
        }
        List<double[]> out = new ArrayList<>();
        out.add(line.get(0));
        for (int i = 0; i < line.size() - 1; i++) {
            double[] p = line.get(i);
            double[] q = line.get(i + 1);
            out.add(new double[]{0.75 * p[0] + 0.25 * q[0], 0.75 * p[1] + 0.25 * q[1]});
            out.add(new double[]{0.25 * p[0] + 0.75 * q[0], 0.25 * p[1] + 0.75 * q[1]});
        }
        out.add(line.get(line.size() - 1));
        return out;
    }

    /** A line as a road with points about {@link #POINT_SPACING} apart along it. */
    static Road spaced(Grade grade, List<double[]> line, String from, String to) {
        List<int[]> points = new ArrayList<>();
        points.add(new int[]{(int) Math.round(line.get(0)[0]), (int) Math.round(line.get(0)[1])});
        double carried = 0;
        for (int i = 1; i < line.size(); i++) {
            double[] p = line.get(i - 1);
            double[] q = line.get(i);
            double len = Math.hypot(q[0] - p[0], q[1] - p[1]);
            double t = POINT_SPACING - carried;
            while (t <= len) {
                double f = t / len;
                points.add(new int[]{(int) Math.round(p[0] + (q[0] - p[0]) * f), (int) Math.round(p[1] + (q[1] - p[1]) * f)});
                t += POINT_SPACING;
            }
            carried = len - (t - POINT_SPACING);
        }
        double[] last = line.get(line.size() - 1);
        points.add(new int[]{(int) Math.round(last[0]), (int) Math.round(last[1])});
        int[] xs = new int[points.size()];
        int[] zs = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xs[i] = points.get(i)[0];
            zs[i] = points.get(i)[1];
        }
        return new Road(grade, xs, zs, from, to);
    }
}
