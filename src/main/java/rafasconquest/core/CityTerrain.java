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
 * Around the box, each 32-block stretch of the edge is eased into the surroundings with a 1-up-2-out slope, filled
 * where the land is lower and cut back into a bowl where it is higher, as long as the slope meets the ground within
 * {@link #SLOPE_REACH} blocks. Where it wouldn't (a valley or sheer cliff), that stretch gets no slope: over a valley
 * the plate ends in a sheer face whose top 2 blocks are capped with wall brick; at a cliff the cliff stays as it is.
 * <p>
 * Coordinates here are city-local, as in {@link CityLayout}: (0, 0) is the city square's north-west corner.
 */
public final class CityTerrain {
    public static final int BOX_MARGIN = 3;
    /** Lowest and highest local coordinate inside the box, on both axes. */
    public static final int BOX_MIN = -BOX_MARGIN;
    public static final int BOX_MAX = CityLayout.SIZE - 1 + BOX_MARGIN;
    /**
     * How far a slope may run past the box. An edge cell's structure start sits 84 blocks from the box edge, and
     * Minecraft only places pieces within about 128 blocks of a start.
     */
    public static final int SLOPE_REACH = 44;
    public static final int EDGE_SEGMENT = 32;
    public static final int SEGMENTS_PER_SIDE = (BOX_MAX - BOX_MIN + EDGE_SEGMENT) / EDGE_SEGMENT;
    /** Blocks of wall brick capping a sheer face over a valley. */
    public static final int RIM_DEPTH = 2;
    static final int MEDIAN_STEP = 24;
    static final int RAY_STEP = 4;

    private CityTerrain() {
    }

    /** Original ground height (top non-air block, water included) at world (x, z). */
    @FunctionalInterface
    public interface Heights {
        int at(int x, int z);
    }

    public enum Edge {
        /** A slope joins the plate to the land around it. */
        EASED,
        /** Land drops away too far for a slope: sheer face, capped with wall brick. */
        VALLEY,
        /** Land rises too far for a slope: the cliff is left as it is. */
        CLIFF
    }

    /** Sides of the box, in the order the edges are stored. */
    public enum Side {
        NORTH, EAST, SOUTH, WEST
    }

    /** {@code edges[side][segment]}; segment 0 is at the box's west end (north/south sides) or north end (east/west). */
    public record Plan(int plateY, Edge[][] edges) {
        public Edge edge(Side side, int segment) {
            return edges[side.ordinal()][Math.max(0, Math.min(SEGMENTS_PER_SIDE - 1, segment))];
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Plan p && p.plateY == plateY && Arrays.deepEquals(p.edges, edges);
        }

        @Override
        public int hashCode() {
            return 31 * plateY + Arrays.deepHashCode(edges);
        }
    }

    public enum Kind {
        /** Inside the box: flatten to the plate. */
        PLATE,
        /** Inside the box on its outermost ring, beside a valley: flatten, then cap the top 2 blocks with wall brick. */
        RIM,
        /** Outside the box on an eased stretch: fill up to {@code low} if lower, cut down to {@code high} if higher. */
        SLOPE,
        /** Leave the land alone. */
        NONE
    }

    /** What to do with one column. For SLOPE, the ground ends up between {@code low} and {@code high}. */
    public record Column(Kind kind, int low, int high) {
        static final Column NONE = new Column(Kind.NONE, 0, 0);
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
        for (Side side : Side.values()) {
            for (int seg = 0; seg < SEGMENTS_PER_SIDE; seg++) {
                int along = Math.min(BOX_MAX, BOX_MIN + seg * EDGE_SEGMENT + EDGE_SEGMENT / 2);
                edges[side.ordinal()][seg] = classify(site, heights, plate, side, along);
            }
        }
        return new Plan(plate, edges);
    }

    private static int next(int l) {
        return l == BOX_MAX ? BOX_MAX + 1 : Math.min(BOX_MAX, l + MEDIAN_STEP);
    }

    /** Looks straight out from the box edge: does a 1-up-2-out slope meet the ground within reach? */
    static Edge classify(CityLayout.Site site, Heights heights, int plate, Side side, int along) {
        long sum = 0;
        int n = 0;
        for (int d = RAY_STEP; d <= SLOPE_REACH; d += RAY_STEP) {
            int[] l = outward(side, along, d);
            int y = heights.at(site.originX() + l[0], site.originZ() + l[1]);
            if (Math.abs(y - plate) <= rise(d)) {
                return Edge.EASED;
            }
            sum += y;
            n++;
        }
        return sum < (long) plate * n ? Edge.VALLEY : Edge.CLIFF;
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

    /** Height of the slope {@code d} blocks out from the box: one block per two out. */
    public static int rise(int d) {
        return (d + 1) / 2;
    }

    public static Column column(Plan plan, int lx, int lz) {
        int dx = lx < BOX_MIN ? BOX_MIN - lx : lx > BOX_MAX ? lx - BOX_MAX : 0;
        int dz = lz < BOX_MIN ? BOX_MIN - lz : lz > BOX_MAX ? lz - BOX_MAX : 0;
        if (dx == 0 && dz == 0) {
            return onRim(plan, lx, lz) ? new Column(Kind.RIM, plan.plateY(), plan.plateY())
                    : new Column(Kind.PLATE, plan.plateY(), plan.plateY());
        }
        int d = Math.max(dx, dz);
        if (d > SLOPE_REACH) {
            return Column.NONE;
        }
        boolean eased;
        if (dx > 0 && dz > 0) {
            // Corner: eased only if both stretches meeting there are.
            Side xSide = lx < BOX_MIN ? Side.WEST : Side.EAST;
            Side zSide = lz < BOX_MIN ? Side.NORTH : Side.SOUTH;
            eased = plan.edge(xSide, segment(zSide == Side.NORTH ? BOX_MIN : BOX_MAX)) == Edge.EASED
                    && plan.edge(zSide, segment(xSide == Side.WEST ? BOX_MIN : BOX_MAX)) == Edge.EASED;
        } else if (dz > 0) {
            eased = plan.edge(lz < BOX_MIN ? Side.NORTH : Side.SOUTH, segment(lx)) == Edge.EASED;
        } else {
            eased = plan.edge(lx < BOX_MIN ? Side.WEST : Side.EAST, segment(lz)) == Edge.EASED;
        }
        if (!eased) {
            return Column.NONE;
        }
        int rise = rise(d);
        return new Column(Kind.SLOPE, plan.plateY() - rise, plan.plateY() + rise);
    }

    /** Outermost ring of the box, on a stretch that faces a valley. */
    private static boolean onRim(Plan plan, int lx, int lz) {
        return (lz == BOX_MIN && plan.edge(Side.NORTH, segment(lx)) == Edge.VALLEY)
                || (lz == BOX_MAX && plan.edge(Side.SOUTH, segment(lx)) == Edge.VALLEY)
                || (lx == BOX_MIN && plan.edge(Side.WEST, segment(lz)) == Edge.VALLEY)
                || (lx == BOX_MAX && plan.edge(Side.EAST, segment(lz)) == Edge.VALLEY);
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
