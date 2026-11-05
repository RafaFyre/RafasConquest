package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * The sea lanes between port cities ({@link SeatCities.City#port}), for ships to sail: no blocks, only a line of
 * points. Each port is joined to at least {@link #PORT_LINKS} of its nearest ports on the same sea (where it has that
 * many), from its harbour mouth (out past its harbour wall) to theirs. Lanes go only over the ocean, keeping a
 * {@link #BERTH}-block berth from land (islands too) wherever the sea is wide enough, and are smoothed into curves like
 * the roads.
 */
public final class ShippingRoutes {
    static final int PORT_LINKS = 3;
    /** The harbour mouth: this far out past the middle of a port's harbour wall, then the nearest sea pixel. */
    static final int MOUTH = 64;
    private static final int MOUTH_SEARCH = 8;
    /** Lanes keep this many blocks off land where they can. */
    public static final int BERTH = 100;
    /** Extra cost of a sea pixel right beside land, falling to none at the berth's edge. */
    private static final double SHORE_COST = 8;

    private ShippingRoutes() {
    }

    /**
     * The lanes between the ports among {@code cities} (named by {@code names}, per province), over the {@code ocean}
     * pixels of {@code land}'s grid.
     */
    public static List<Roads.Road> plan(Roads.Land land, boolean[] ocean, List<SeatCities.City> cities, List<String> names) {
        int w = land.width();
        int n = w * land.height();
        // Which sea each pixel lies in, so ports only link across water that joins them.
        int[] sea = new int[n];
        Arrays.fill(sea, -1);
        int seas = 0;
        int[] queue = new int[n];
        for (int i = 0; i < n; i++) {
            if (!ocean[i] || sea[i] >= 0) {
                continue;
            }
            int head = 0;
            int tail = 0;
            queue[tail++] = i;
            sea[i] = seas;
            while (head < tail) {
                int p = queue[head++];
                for (int q : neighbours(p, w, land.height())) {
                    if (q >= 0 && ocean[q] && sea[q] < 0) {
                        sea[q] = seas;
                        queue[tail++] = q;
                    }
                }
            }
            seas++;
        }
        // How far each sea pixel lies from land, in pixels (up to the berth), for the lanes' berth.
        int berth = Math.max(1, (BERTH + land.bpp() - 1) / land.bpp());
        int[] fromLand = new int[n];
        Arrays.fill(fromLand, berth);
        int head = 0;
        int tail = 0;
        for (int i = 0; i < n; i++) {
            if (!ocean[i]) {
                fromLand[i] = 0;
                queue[tail++] = i;
            }
        }
        while (head < tail) {
            int p = queue[head++];
            for (int q : neighbours(p, w, land.height())) {
                if (q >= 0 && fromLand[q] > fromLand[p] + 1) {
                    fromLand[q] = fromLand[p] + 1;
                    queue[tail++] = q;
                }
            }
        }
        double[] shore = new double[n];
        for (int i = 0; i < n; i++) {
            shore[i] = SHORE_COST * (berth - fromLand[i]) / berth;
        }
        List<SeatCities.City> ports = new ArrayList<>();
        List<Integer> mouths = new ArrayList<>();
        for (SeatCities.City c : cities) {
            int mouth = c.port() ? mouth(land, ocean, c) : -1;
            if (mouth >= 0) {
                ports.add(c);
                mouths.add(mouth);
            }
        }
        Search search = new Search(land, ocean, shore);
        List<Roads.Road> lanes = new ArrayList<>();
        Set<Long> joined = new HashSet<>();
        int[] links = new int[ports.size()];
        for (int a = 0; a < ports.size(); a++) {
            SeatCities.City from = ports.get(a);
            int fromSea = sea[mouths.get(a)];
            List<Integer> nearest = new ArrayList<>();
            for (int b = 0; b < ports.size(); b++) {
                if (b != a && sea[mouths.get(b)] == fromSea) {
                    nearest.add(b);
                }
            }
            nearest.sort(java.util.Comparator.comparingDouble(b -> Math.hypot(ports.get(b).x() - from.x(), ports.get(b).z() - from.z())));
            // The nearest first, going further out until this port has its links (lanes that fail don't count).
            for (int b : nearest) {
                if (links[a] >= PORT_LINKS) {
                    break;
                }
                long pair = Math.min(a, b) * 1_000_003L + Math.max(a, b);
                if (joined.contains(pair)) {
                    continue;
                }
                List<Integer> path = search.route(mouths.get(a), mouths.get(b));
                if (path == null) {
                    continue;
                }
                joined.add(pair);
                links[a]++;
                links[b]++;
                List<double[]> line = new ArrayList<>();
                for (int p : path) {
                    int[] c = land.centre(p);
                    line.add(new double[]{c[0], c[1]});
                }
                for (int pass = 0; pass < 3; pass++) {
                    line = Roads.chaikin(line);
                }
                lanes.add(Roads.spaced(Roads.Grade.SEA, line, names.get(from.province()), names.get(ports.get(b).province())));
            }
        }
        return lanes;
    }

    /** A port's harbour mouth: the ocean pixel nearest {@link #MOUTH} blocks out from its harbour wall's middle; -1 if none near. */
    static int mouth(Roads.Land land, boolean[] ocean, SeatCities.City c) {
        int out = c.size() / 2 + MOUTH;
        int[] at = switch (CityLayout.Side.values()[c.harbour()]) {
            case NORTH -> new int[]{c.x(), c.z() - out};
            case SOUTH -> new int[]{c.x(), c.z() + out};
            case WEST -> new int[]{c.x() - out, c.z()};
            case EAST -> new int[]{c.x() + out, c.z()};
        };
        int px = Math.floorDiv(at[0] - land.minX(), land.bpp());
        int pz = Math.floorDiv(at[1] - land.minZ(), land.bpp());
        int best = -1;
        int bestDistance = Integer.MAX_VALUE;
        for (int dz = -MOUTH_SEARCH; dz <= MOUTH_SEARCH; dz++) {
            for (int dx = -MOUTH_SEARCH; dx <= MOUTH_SEARCH; dx++) {
                int x = px + dx;
                int z = pz + dz;
                int d = dx * dx + dz * dz;
                if (x >= 0 && z >= 0 && x < land.width() && z < land.height() && d < bestDistance && ocean[z * land.width() + x]) {
                    best = z * land.width() + x;
                    bestDistance = d;
                }
            }
        }
        return best;
    }

    /** The 8 pixels about {@code p}, -1 for those off the map. */
    private static int[] neighbours(int p, int w, int h) {
        int x = p % w;
        int z = p / w;
        int[] out = new int[8];
        int k = 0;
        for (int dz = -1; dz <= 1; dz++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx != 0 || dz != 0) {
                    int nx = x + dx;
                    int nz = z + dz;
                    out[k++] = nx < 0 || nz < 0 || nx >= w || nz >= h ? -1 : nz * w + nx;
                }
            }
        }
        return out;
    }

    /** A* over the ocean pixels; its arrays kept between searches, each search stamping the pixels it touches. */
    private static final class Search {
        private final Roads.Land land;
        private final boolean[] ocean;
        private final double[] shore;
        private final double[] best;
        private final int[] from;
        private final int[] stamp;
        private int search;

        Search(Roads.Land land, boolean[] ocean, double[] shore) {
            this.land = land;
            this.ocean = ocean;
            this.shore = shore;
            int n = land.width() * land.height();
            best = new double[n];
            from = new int[n];
            stamp = new int[n];
        }

        List<Integer> route(int start, int goal) {
            search++;
            int w = land.width();
            int gx = goal % w;
            int gz = goal / w;
            PriorityQueue<double[]> open = new PriorityQueue<>((p, q) -> Double.compare(p[0], q[0]));
            stamp[start] = search;
            best[start] = 0;
            from[start] = -1;
            open.add(new double[]{heuristic(start, gx, gz), start});
            while (!open.isEmpty()) {
                double[] top = open.poll();
                int at = (int) top[1];
                if (top[0] - heuristic(at, gx, gz) > best[at] + 1e-9) {
                    continue;
                }
                if (at == goal) {
                    List<Integer> path = new ArrayList<>();
                    for (int c = at; c >= 0; c = from[c]) {
                        path.add(c);
                    }
                    java.util.Collections.reverse(path);
                    return path;
                }
                int[] next = neighbours(at, w, land.height());
                for (int i = 0; i < next.length; i++) {
                    int q = next[i];
                    if (q < 0 || !ocean[q]) {
                        continue;
                    }
                    boolean diagonal = i == 0 || i == 2 || i == 5 || i == 7;
                    double g = best[at] + (diagonal ? Math.sqrt(2) : 1) * (1 + shore[q]);
                    if (stamp[q] != search || g < best[q]) {
                        stamp[q] = search;
                        best[q] = g;
                        from[q] = at;
                        open.add(new double[]{g + heuristic(q, gx, gz), q});
                    }
                }
            }
            return null;
        }

        private double heuristic(int p, int gx, int gz) {
            int dx = Math.abs(p % land.width() - gx);
            int dz = Math.abs(p / land.width() - gz);
            return Math.max(dx, dz) + (Math.sqrt(2) - 1) * Math.min(dx, dz);
        }
    }
}
