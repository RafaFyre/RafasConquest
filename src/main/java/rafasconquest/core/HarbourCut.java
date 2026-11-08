package rafasconquest.core;

/**
 * Where a port's land stands between its wharf and the sea, the harbour cut clears it so the water reaches the planks:
 * from the wharf's outer edge the floor drops {@link #STEP_RISE} blocks for each block out, {@link #STEPS} times
 * ({@code 15} deep), then falls on at the city's gentle 1-in-2 slope, no deeper than {@link #DEEPEST}. Everything above
 * the floor goes: below sea level it becomes water, above it air. Each lane runs out to where it meets the sea, at most
 * {@link #REACH} blocks; along the side it spans the wall from corner to corner.
 */
public final class HarbourCut {
    /** The wharf's width, out past the wall. */
    public static final int WHARF = 5;
    static final int STEP_RISE = 5;
    static final int STEPS = 3;
    /** How far below the plate the floor may fall. */
    public static final int DEEPEST = 24;
    /** The furthest out past the wharf the cut runs. */
    public static final int REACH = 192;

    private HarbourCut() {
    }

    /** Blocks out past the city square of the cut's first column, for a wall standing {@code outset} out. */
    public static int start(int outset) {
        return outset + WHARF + 1;
    }

    /** The floor's y {@code d} blocks out past the wharf (the first column is 1). */
    public static int floor(int plate, int d) {
        if (d <= STEPS) {
            return plate - STEP_RISE * d;
        }
        return Math.max(plate - STEP_RISE * STEPS - CityTerrain.rise(d - STEPS), plate - DEEPEST);
    }

    /** Looks in a row this many apart, all wet, that count as the open sea rather than a river or an inlet. */
    static final int OPEN = 3;

    /**
     * How far out past the wharf a lane must be cut to open into the sea: a look past the first of {@link #OPEN} wet
     * looks in a row, {@code step} apart from 1, or the whole {@link #REACH} if it never meets them. A river or inlet
     * by the wall no longer stops the cut short of the land beyond it. Past the open sea, an island whose far side is
     * sea again within reach is cut too, out to a look past it; a far shore that runs on to the reach is left alone.
     */
    public static int coast(java.util.function.IntPredicate wetAt, int step) {
        int run = 0;
        int end = -1;
        boolean wasDry = false;
        for (int d = 1; d <= REACH; d += step) {
            boolean wet = wetAt.test(d);
            run = wet ? run + 1 : 0;
            if (end < 0 && run == OPEN) {
                end = d - (OPEN - 1) * step + step;
            } else if (end >= 0 && wet && wasDry) {
                end = d + step;
            }
            wasDry = !wet;
        }
        return end < 0 ? REACH : Math.min(REACH, end);
    }

    /** The cut's city-local box {minX, minZ, maxX, maxZ} on {@code side} of a square {@code size} a side. */
    public static int[] box(CityLayout.Side side, int size, int outset) {
        int in = start(outset);
        int out = in + REACH - 1;
        int lo = -outset;
        int hi = size - 1 + outset;
        return switch (side) {
            case NORTH -> new int[]{lo, -out, hi, -in};
            case SOUTH -> new int[]{lo, size - 1 + in, hi, size - 1 + out};
            case WEST -> new int[]{-out, lo, -in, hi};
            case EAST -> new int[]{size - 1 + in, lo, size - 1 + out, hi};
        };
    }

    public static boolean inBox(int[] box, int lx, int lz) {
        return box != null && lx >= box[0] && lx <= box[2] && lz >= box[1] && lz <= box[3];
    }
}
