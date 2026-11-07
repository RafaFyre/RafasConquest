package rafasconquest.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A noble district standing outside its city (an annex), on one side that is not its harbour: {@link #GAP_MIN} to
 * {@link #GAP_MAX} blocks out past the city's wall, on a stone platform {@link #RISE} blocks above the city's plate,
 * its middle up to {@link #OFFSET_MAX} blocks to either side of the city's gate there, and reached from that gate by a
 * stone path climbing no more than 1 block in 2.
 */
public final class NobleAnnex {
    public static final int GAP_MIN = 35;
    public static final int GAP_MAX = 50;
    public static final int OFFSET_MAX = 15;
    public static final int RISE = 40;
    /** The path's longest climb: one block up for this many along. */
    public static final int RUN = 2;
    /** The path's width: twice a city road's. */
    public static final int WIDTH = 2 * CityLayout.ROAD_WIDTH;
    /** How far out the platform's easing runs, from its top down to the city's height ({@link #RISE} over this). */
    public static final int EASE_REACH = 20;
    /** How far out it keeps filling, as steeply, down to land below the city's height. */
    public static final int FILL_REACH = 2 * EASE_REACH;
    /** Room the world map keeps for a style's wall ring (either city's or the annex's), which it can't know yet. */
    static final int WALL_ROOM = 40;
    /** How far past a city's square its annex and the land it shapes may reach, at most (a 9-district city's). */
    public static final int MAX_REACH = 2 * WALL_ROOM + GAP_MAX + 9 * CityLayout.BLOCKS_PER_CELL * 2 / 5 * CityLayout.ROAD_PERIOD
            + OFFSET_MAX + CityTerrain.SLOPE_REACH;

    private NobleAnnex() {
    }

    /** Whether a city at (x, z) has its noble district outside (half of them), before its sides are checked for room. */
    public static boolean wanted(long seed, int x, int z) {
        return (SeedHash.hash(seed, 0x414E4E4558L, x, z) & 1) == 0;
    }

    /** The order a city at (x, z) tries its sides in for an annex: a turn of north, east, south, west. */
    public static int firstSide(long seed, int x, int z) {
        return SeedHash.intInRange(SeedHash.hash(seed, 0x53494445L, x, z), 0, 3);
    }

    /** How far out past the city's wall the annex's wall stands, for a city at (x, z). */
    public static int gap(int x, int z) {
        return SeedHash.intInRange(SeedHash.hash(0, 0x474150L, x, z), GAP_MIN, GAP_MAX);
    }

    /** How far the annex's middle stands to the side of the city's gate, either way, for a city at (x, z). */
    public static int offset(int x, int z) {
        return SeedHash.intInRange(SeedHash.hash(0, 0x4F4646L, x, z), -OFFSET_MAX, OFFSET_MAX);
    }

    /** The side of the annex's area (inside its walls) for a city {@code cells} districts a side: as the inner district's. */
    public static int area(int cells) {
        return Math.max(1, cells * CityLayout.BLOCKS_PER_CELL * 2 / 5) * CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH;
    }

    /**
     * The world box {minX, minZ, maxX, maxZ} the map keeps for the annex of a city at (x, z), {@code half} blocks to
     * its square's edge, on {@code side} (a {@link CityLayout.Side} ordinal): generous, as the walls aren't known yet.
     */
    public static int[] reserve(int x, int z, int half, int districts, int side) {
        int a = area(districts);
        int out0 = half;
        int out1 = half + WALL_ROOM + GAP_MAX + WALL_ROOM + a;
        int across = a / 2 + WALL_ROOM + OFFSET_MAX;
        return switch (CityLayout.Side.values()[side]) {
            case NORTH -> new int[]{x - across, z - out1, x + across, z - out0};
            case SOUTH -> new int[]{x - across, z + out0, x + across, z + out1};
            case WEST -> new int[]{x - out1, z - across, x - out0, z + across};
            case EAST -> new int[]{x + out0, z - across, x + out1, z + across};
        };
    }

    /**
     * The path's columns, each {u, v, half}: u across and v out from the city, from the city gate's middle (u0, v0) to
     * the annex gate's (u1, v1), {@link #WIDTH} wide; {@code half} is its height above the city's plate
     * in half blocks, 0 at the city's gate to {@code 2 * RISE} at the annex's. Where the gap is too short to climb at
     * 1 in {@link #RUN}, the path bows out sideways until it is long enough; with the gates out of line it bends, an S.
     */
    public static List<int[]> path(int u0, int v0, int u1, int v1) {
        int need = RISE * RUN;
        double bow = 0;
        double sign = u1 >= u0 ? 1 : -1;
        while (length(u0, v0, u1, v1, bow * sign) < need && bow < 4 * need) {
            bow += 0.5;
        }
        double b = bow * sign;
        double total = length(u0, v0, u1, v1, b);
        int samples = (int) Math.ceil(total * 4) + 1;
        // Each column takes the height of the nearest point on the middle line: {u, v, half, distance squared x 1000}.
        Map<Long, int[]> columns = new LinkedHashMap<>();
        double s = 0;
        double[] last = at(u0, v0, u1, v1, b, 0);
        // WIDTH blocks a side about the middle line: those with -WIDTH/2 < offset <= WIDTH/2 (even, so half a block off centre).
        int w = WIDTH / 2;
        int vMin = Math.min(v0, v1) - 1;
        int vMax = Math.max(v0, v1) + 1;
        for (int i = 0; i <= samples; i++) {
            double[] p = at(u0, v0, u1, v1, b, (double) i / samples);
            s += Math.hypot(p[0] - last[0], p[1] - last[1]);
            last = p;
            int half = (int) Math.min(2 * RISE, Math.round(2.0 * RISE * s / total));
            int cu = (int) Math.floor(p[0] + w) - WIDTH + 1;
            int cv = (int) Math.floor(p[1] + w) - WIDTH + 1;
            for (int du = 0; du < WIDTH; du++) {
                for (int dv = 0; dv < WIDTH; dv++) {
                    int u = cu + du;
                    int v = cv + dv;
                    if (v < vMin || v > vMax) {
                        continue; // no further into either gate than a block
                    }
                    int near = (int) Math.round(1000 * (Math.pow(u - p[0], 2) + Math.pow(v - p[1], 2)));
                    columns.merge(((long) u << 32) ^ (v & 0xffffffffL), new int[]{u, v, half, near}, (old, now) -> now[3] < old[3] ? now : old);
                }
            }
        }
        // On a tight bend the inside columns sit by others far along the path: raise each to a block under its highest neighbour.
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int[] c : columns.values()) {
                for (int du = -1; du <= 1; du++) {
                    for (int dv = -1; dv <= 1; dv++) {
                        int[] n = columns.get(((long) (c[0] + du) << 32) ^ ((c[1] + dv) & 0xffffffffL));
                        if (n != null && n[2] - 2 > c[2]) {
                            c[2] = n[2] - 2;
                            changed = true;
                        }
                    }
                }
            }
        }
        return columns.values().stream().map(c -> new int[]{c[0], c[1], c[2]}).toList();
    }

    /** A point on the path's middle line, {@code t} from 0 to 1: straight out, easing across, bowed by {@code bow}. */
    private static double[] at(int u0, int v0, int u1, int v1, double bow, double t) {
        double ease = t * t * (3 - 2 * t);
        return new double[]{u0 + (u1 - u0) * ease + bow * Math.sin(Math.PI * t), v0 + (v1 - v0) * t};
    }

    static double length(int u0, int v0, int u1, int v1, double bow) {
        double total = 0;
        double[] last = at(u0, v0, u1, v1, bow, 0);
        for (int i = 1; i <= 200; i++) {
            double[] p = at(u0, v0, u1, v1, bow, i / 200.0);
            total += Math.hypot(p[0] - last[0], p[1] - last[1]);
            last = p;
        }
        return total;
    }
}
