package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * How a city reshapes the land, decided from the original terrain alone so every cell of a city agrees.
 * <p>
 * The <b>box</b> is the city square plus {@link #BOX_MARGIN} blocks past the wall. Inside it, the ground becomes a flat
 * <b>plate</b> at the median height of the box: everything above is cleared, everything below is filled.
 * <p>
 * Around the box, out to {@link #SLOPE_REACH} blocks:
 * <ul>
 *   <li>The <b>carve</b> removes land above a slope rising from the plate. The slope is 1 up per 2 out, made steeper
 *       wherever that is needed to meet the land at the end of its reach, so the carve never ends in a wall. How steep
 *       blends smoothly along the edge. The carve only ever removes blocks.</li>
 *   <li>The <b>fill</b> eases the plate down into lower land with a 1-up-2-out slope. It only runs on stretches of the
 *       edge where the land falls away, so blocks are never added to build up toward higher ground.</li>
 *   <li>Where the land falls away too far for the fill to reach (a <b>valley</b>), there is no fill and no ledge: the
 *       wall's outer face drops straight into the natural cliff, with stone under the wall.</li>
 * </ul>
 * Coordinates here are city-local, as in {@link CityLayout}: (0, 0) is the city square's north-west corner.
 */
public final class CityTerrain {
    public static final int BOX_MARGIN = 3;
    /** Lowest and highest local coordinate inside the box, on both axes. */
    public static final int BOX_MIN = -BOX_MARGIN;
    public static final int BOX_MAX = CityLayout.SIZE - 1 + BOX_MARGIN;
    /**
     * How far the carve and fill may run past the box. An edge cell's structure start sits 84 blocks from the box
     * edge, and Minecraft only places pieces within about 128 blocks of a start.
     */
    public static final int SLOPE_REACH = 44;
    public static final int EDGE_SEGMENT = 32;
    public static final int SEGMENTS_PER_SIDE = (BOX_MAX - BOX_MIN + EDGE_SEGMENT) / EDGE_SEGMENT;
    static final int MEDIAN_STEP = 24;
    static final int RAY_STEP = 4;
    /** How far past the reach to look for land the carve has to meet. */
    static final int BEYOND_REACH = 12;
    /** Rays per stretch when measuring the land the carve meets: the stretch centre and this far either side. */
    static final int RAY_SPREAD = 12;

    private CityTerrain() {
    }

    /** Original ground height (top non-air block, water included) at world (x, z). */
    @FunctionalInterface
    public interface Heights {
        int at(int x, int z);
    }

    public enum Edge {
        /** Land rises (or is level): carve only. */
        CARVE,
        /** Land falls away within reach: fill down to it (and carve any bumps). */
        FILL,
        /** Land falls away beyond reach: no fill; the wall stands flush on the natural cliff. */
        VALLEY
    }

    /** Sides of the box, in the order the edges are stored. */
    public enum Side {
        NORTH, EAST, SOUTH, WEST
    }

    /**
     * {@code edges[side][segment]}; segment 0 is at the box's west end (north/south sides) or north end (east/west).
     * {@code carveTo[side][segment]} is how far above the plate the carve must reach at {@link #SLOPE_REACH}, measured
     * at that stretch (0 or less if 1:2 already clears the land).
     */
    public record Plan(int plateY, Edge[][] edges, int[][] carveTo) {
        public Edge edge(Side side, int segment) {
            return edges[side.ordinal()][Math.max(0, Math.min(SEGMENTS_PER_SIDE - 1, segment))];
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Plan p && p.plateY == plateY && Arrays.deepEquals(p.edges, edges) && Arrays.deepEquals(p.carveTo, carveTo);
        }

        @Override
        public int hashCode() {
            return 31 * (31 * plateY + Arrays.deepHashCode(edges)) + Arrays.deepHashCode(carveTo);
        }
    }

    public enum Kind {
        /** Inside the box: flatten to the plate. */
        PLATE,
        /** Under the wall on a valley stretch: flatten, with stone right up to the plate so the cliff face is natural. */
        WALL_BASE,
        /** Outside the wall on a valley stretch: only clear anything above the plate. */
        CLEAR_ABOVE,
        /** Outside the box: carve down to {@code high} if higher; fill up to {@code low} if lower and {@code fill}. */
        SLOPE,
        /** Leave the land alone. */
        NONE
    }

    /** What to do with one column. */
    public record Column(Kind kind, int low, int high, boolean fill) {
        static final Column NONE = new Column(Kind.NONE, 0, 0, false);
    }

    /**
     * Plans a city's terrain. The plate is the median ground height over the box, but never below sea level, so a
     * coastal city doesn't sit in a pit the sea could pour into.
     */
    public static Plan plan(CityLayout.Site site, Heights heights, int seaLevel) {
        List<Integer> samples = new ArrayList<>();
        for (int lx = BOX_MIN; lx <= BOX_MAX; lx = next(lx)) {
            for (int lz = BOX_MIN; lz <= BOX_MAX; lz = next(lz)) {
                samples.add(heights.at(site.originX() + lx, site.originZ() + lz));
            }
        }
        samples.sort(Integer::compare);
        int plate = Math.max(seaLevel - 1, samples.get(samples.size() / 2));

        Edge[][] edges = new Edge[Side.values().length][SEGMENTS_PER_SIDE];
        int[][] carveTo = new int[Side.values().length][SEGMENTS_PER_SIDE];
        for (Side side : Side.values()) {
            for (int seg = 0; seg < SEGMENTS_PER_SIDE; seg++) {
                int along = center(seg);
                edges[side.ordinal()][seg] = classify(site, heights, plate, side, along);
                carveTo[side.ordinal()][seg] = carveTarget(site, heights, plate, side, along);
            }
        }
        return new Plan(plate, edges, carveTo);
    }

    private static int next(int l) {
        return l == BOX_MAX ? BOX_MAX + 1 : Math.min(BOX_MAX, l + MEDIAN_STEP);
    }

    /** Along-edge position of a stretch's centre. */
    static int center(int segment) {
        return Math.min(BOX_MAX, BOX_MIN + segment * EDGE_SEGMENT + EDGE_SEGMENT / 2);
    }

    /** Looks straight out from the box edge: does the land fall away, and if so does a 1:2 fill reach it? */
    static Edge classify(CityLayout.Site site, Heights heights, int plate, Side side, int along) {
        long sum = 0;
        int n = 0;
        boolean fillMeets = false;
        for (int d = RAY_STEP; d <= SLOPE_REACH; d += RAY_STEP) {
            int y = height(site, heights, side, along, d);
            sum += y - plate;
            n++;
            if (y >= plate - rise(d)) {
                fillMeets = true;
            }
        }
        if (sum >= 0) {
            return Edge.CARVE;
        }
        return fillMeets ? Edge.FILL : Edge.VALLEY;
    }

    /** The highest land around the end of the carve's reach, relative to the plate: what the carve has to meet. */
    static int carveTarget(CityLayout.Site site, Heights heights, int plate, Side side, int along) {
        int highest = Integer.MIN_VALUE;
        for (int offset = -RAY_SPREAD; offset <= RAY_SPREAD; offset += RAY_SPREAD) {
            int a = Math.max(BOX_MIN, Math.min(BOX_MAX, along + offset));
            for (int d = SLOPE_REACH - RAY_STEP; d <= SLOPE_REACH + BEYOND_REACH; d += RAY_STEP) {
                highest = Math.max(highest, height(site, heights, side, a, d) - plate);
            }
        }
        return highest;
    }

    private static int height(CityLayout.Site site, Heights heights, Side side, int along, int d) {
        int[] l = outward(side, along, d);
        return heights.at(site.originX() + l[0], site.originZ() + l[1]);
    }

    /** Local (x, z) {@code d} blocks straight out from the box edge at position {@code along} on {@code side}. */
    static int[] outward(Side side, int along, int d) {
        return switch (side) {
            case NORTH -> new int[]{along, BOX_MIN - d};
            case EAST -> new int[]{BOX_MAX + d, along};
            case SOUTH -> new int[]{along, BOX_MAX + d};
            case WEST -> new int[]{BOX_MIN - d, along};
        };
    }

    /** Height of the gentle slope {@code d} blocks out from the box: one block per two out. */
    public static int rise(int d) {
        return (d + 1) / 2;
    }

    /**
     * Height of the carve {@code d} blocks out: the gentle slope, or steeper if the land it must meet at the end of its
     * reach ({@code target} above the plate) is higher than the gentle slope gets there.
     */
    public static int carveRise(int d, int target) {
        int steep = target <= 0 ? 0 : (int) Math.ceil((double) target * d / SLOPE_REACH);
        return Math.max(rise(d), steep);
    }

    public static Column column(Plan plan, int lx, int lz) {
        int dx = lx < BOX_MIN ? BOX_MIN - lx : lx > BOX_MAX ? lx - BOX_MAX : 0;
        int dz = lz < BOX_MIN ? BOX_MIN - lz : lz > BOX_MAX ? lz - BOX_MAX : 0;
        int plate = plan.plateY();
        if (dx == 0 && dz == 0) {
            return insideBox(plan, lx, lz);
        }
        int d = Math.max(dx, dz);
        if (d > SLOPE_REACH) {
            return Column.NONE;
        }
        int target;
        boolean fill;
        if (dx > 0 && dz > 0) {
            // Corner: as steep as the steeper of the two sides meeting there, and only filled if both are filled.
            Side xSide = lx < BOX_MIN ? Side.WEST : Side.EAST;
            Side zSide = lz < BOX_MIN ? Side.NORTH : Side.SOUTH;
            int xEnd = zSide == Side.NORTH ? BOX_MIN : BOX_MAX;
            int zEnd = xSide == Side.WEST ? BOX_MIN : BOX_MAX;
            target = Math.max(carveTarget(plan, xSide, xEnd), carveTarget(plan, zSide, zEnd));
            fill = plan.edge(xSide, segment(xEnd)) == Edge.FILL && plan.edge(zSide, segment(zEnd)) == Edge.FILL;
        } else {
            Side side = dz > 0 ? (lz < BOX_MIN ? Side.NORTH : Side.SOUTH) : (lx < BOX_MIN ? Side.WEST : Side.EAST);
            int along = dz > 0 ? lx : lz;
            target = carveTarget(plan, side, along);
            fill = plan.edge(side, segment(along)) == Edge.FILL;
        }
        return new Column(Kind.SLOPE, plate - rise(d), plate + carveRise(d, target), fill);
    }

    /** Inside the box: the plate, except under and outside the wall on valley stretches. */
    private static Column insideBox(Plan plan, int lx, int lz) {
        int plate = plan.plateY();
        int far = CityLayout.SIZE - 1;
        int wall = CityLayout.ROAD_WIDTH;
        // Which valley stretch, if any, this column faces, and whether it is outside the wall or under it.
        boolean outside = false;
        boolean under = false;
        if (lz < wall && valley(plan, Side.NORTH, lx)) {
            outside |= lz < 0;
            under |= lz >= 0;
        }
        if (lz > far - wall && valley(plan, Side.SOUTH, lx)) {
            outside |= lz > far;
            under |= lz <= far;
        }
        if (lx < wall && valley(plan, Side.WEST, lz)) {
            outside |= lx < 0;
            under |= lx >= 0;
        }
        if (lx > far - wall && valley(plan, Side.EAST, lz)) {
            outside |= lx > far;
            under |= lx <= far;
        }
        if (outside) {
            return new Column(Kind.CLEAR_ABOVE, plate, plate, false);
        }
        if (under) {
            return new Column(Kind.WALL_BASE, plate, plate, false);
        }
        return new Column(Kind.PLATE, plate, plate, false);
    }

    private static boolean valley(Plan plan, Side side, int along) {
        return plan.edge(side, segment(along)) == Edge.VALLEY;
    }

    /** The carve's target at a position along a side, blended linearly between neighbouring stretch centres. */
    static int carveTarget(Plan plan, Side side, int along) {
        int[] targets = plan.carveTo()[side.ordinal()];
        if (along <= center(0)) {
            return targets[0];
        }
        for (int seg = 0; seg < SEGMENTS_PER_SIDE - 1; seg++) {
            int a = center(seg);
            int b = center(seg + 1);
            if (along <= b) {
                double t = b == a ? 1 : (double) (along - a) / (b - a);
                return (int) Math.round(targets[seg] + (targets[seg + 1] - targets[seg]) * t);
            }
        }
        return targets[SEGMENTS_PER_SIDE - 1];
    }

    static int segment(int along) {
        return Math.max(0, Math.min(SEGMENTS_PER_SIDE - 1, (along - BOX_MIN) / EDGE_SEGMENT));
    }

    /**
     * Which cell (0–2 along each axis) owns a column, inside or outside the box. Columns outside the city square
     * belong to the nearest edge cell, so each cell's flattening covers a disjoint area and they never overlap.
     */
    public static int cellOf(int local) {
        int clamped = Math.max(0, Math.min(CityLayout.SIZE - 1, local));
        return Math.min(CityLayout.CELLS_PER_SIDE - 1, clamped / CityLayout.DISTRICT_SPACING);
    }

    /** Local extent [min, max] of the columns a cell flattens along one axis, slopes included. */
    public static int ownedMin(int cell) {
        return cell == 0 ? BOX_MIN - SLOPE_REACH : cell * CityLayout.DISTRICT_SPACING;
    }

    public static int ownedMax(int cell) {
        return cell == CityLayout.CELLS_PER_SIDE - 1 ? BOX_MAX + SLOPE_REACH : (cell + 1) * CityLayout.DISTRICT_SPACING - 1;
    }
}
