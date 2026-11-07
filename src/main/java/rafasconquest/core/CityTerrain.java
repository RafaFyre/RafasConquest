package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * How a city reshapes the land, decided from the original terrain alone so every cell of a city agrees.
 * <p>
 * The <b>box</b> is the city square plus the wall, which stands {@link CityLayout#WALL_OUTSET} blocks out past it, plus
 * {@link #BOX_MARGIN} blocks past the wall. Inside it, the ground becomes a flat
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
    /** How far past the wall the box reaches, and so where the easing starts, for a city {@code cells} districts a side. */
    public static int margin(int cells) {
        return 2 * cells;
    }

    /** A 3 × 3 city's margin. */
    public static final int BOX_MARGIN = 2 * CityLayout.CELLS_PER_SIDE;
    /** Lowest and highest local coordinate inside the box, on both axes. The wall stands out past the city square. */
    public static final int BOX_MIN = -CityLayout.WALL_OUTSET - BOX_MARGIN;
    public static final int BOX_MAX = CityLayout.SIZE - 1 + CityLayout.WALL_OUTSET + BOX_MARGIN;
    /** How far the carve and fill may run past the box. */
    public static final int SLOPE_REACH = 84;
    /**
     * How far past the box an edge cell lays the slope itself. An edge cell's structure start sits 86 blocks from the
     * box edge, and Minecraft only places pieces within about 128 blocks of a start, so the slope beyond this is laid
     * by {@link #slopeTiles slope tiles}, each its own structure start.
     */
    public static final int CELL_REACH = 42;
    /** Longest side of a slope tile: with its start at its middle, every column is well within the engine's reach. */
    static final int SLOPE_TILE = 96;
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
     * {@code carveTo[side][segment]} is how far above the plate the carve must reach at the end of its reach, measured
     * at that stretch (0 or less if 1:2 already clears the land). {@code outset} is how far the city's wall stands out
     * past its square; the box is that plus {@link #BOX_MARGIN}. {@code size} is the square's side ({@link #size(int)}
     * for a city's), and {@code run} how many blocks out the easing goes for each block up: 2 for a city; negative,
     * blocks up for each one out: -3 for an outside noble district's platform ({@link NobleAnnex}), a cliff face.
     */
    public record Plan(int plateY, Edge[][] edges, int[][] carveTo, int cells, int outset, int size, int run) {
        /** A 3 × 3 city's plan. */
        public Plan(int plateY, Edge[][] edges, int[][] carveTo) {
            this(plateY, edges, carveTo, CityLayout.CELLS_PER_SIDE);
        }

        public Plan(int plateY, Edge[][] edges, int[][] carveTo, int cells) {
            this(plateY, edges, carveTo, cells, CityLayout.WALL_OUTSET);
        }

        public Plan(int plateY, Edge[][] edges, int[][] carveTo, int cells, int outset) {
            this(plateY, edges, carveTo, cells, outset, CityTerrain.size(cells), 2);
        }

        public int boxMin() {
            return CityTerrain.boxMin(cells, outset);
        }

        public int boxMax() {
            return size - 1 + outset + margin(cells);
        }

        /** The stretches along each side. */
        public int segments() {
            return (boxMax() - boxMin() + EDGE_SEGMENT) / EDGE_SEGMENT;
        }

        /** How high the easing rises {@code d} blocks out: one block per {@code run}, or {@code -run} per block. */
        public int rise(int d) {
            return run < 0 ? -run * d : (d + run - 1) / run;
        }

        /** Whether local (x, z) lies in the box. */
        public boolean inBox(int x, int z) {
            return x >= boxMin() && x <= boxMax() && z >= boxMin() && z <= boxMax();
        }

        public Edge edge(Side side, int segment) {
            Edge[] side0 = edges[side.ordinal()];
            return side0[Math.max(0, Math.min(side0.length - 1, segment))];
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Plan p && p.plateY == plateY && p.cells == cells && p.outset == outset && p.size == size && p.run == run
                    && Arrays.deepEquals(p.edges, edges) && Arrays.deepEquals(p.carveTo, carveTo);
        }

        @Override
        public int hashCode() {
            return 31 * (31 * (31 * (31 * (31 * plateY + cells) + outset) + size * 7 + run) + Arrays.deepHashCode(edges)) + Arrays.deepHashCode(carveTo);
        }
    }

    /** A city {@code cells} cells a side: its side in blocks, the box's far edge, and the stretches along each side. */
    public static int size(int cells) {
        return cells * CityLayout.BLOCKS_PER_CELL * CityLayout.ROAD_PERIOD + CityLayout.ROAD_WIDTH;
    }

    public static int boxMax(int cells) {
        return boxMax(cells, CityLayout.WALL_OUTSET);
    }

    public static int boxMin(int cells, int outset) {
        return -outset - margin(cells);
    }

    public static int boxMax(int cells, int outset) {
        return size(cells) - 1 + outset + margin(cells);
    }

    public static int segments(int cells) {
        return segments(cells, CityLayout.WALL_OUTSET);
    }

    public static int segments(int cells, int outset) {
        return (boxMax(cells, outset) - boxMin(cells, outset) + EDGE_SEGMENT) / EDGE_SEGMENT;
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

    /** What to do with one column; a {@code stone} one (an outside noble district's easing) is laid in bare stone. */
    public record Column(Kind kind, int low, int high, boolean fill, boolean stone) {
        static final Column NONE = new Column(Kind.NONE, 0, 0, false);

        public Column(Kind kind, int low, int high, boolean fill) {
            this(kind, low, high, fill, false);
        }
    }

    /**
     * Plans a city's terrain. The plate is the median ground height over the box, but never below sea level, so a
     * coastal city doesn't sit in a pit the sea could pour into.
     */
    public static Plan plan(CityLayout.Site site, Heights heights, int seaLevel) {
        return plan(site, heights, seaLevel, CityLayout.WALL_OUTSET);
    }

    /** The same, for a wall standing {@code outset} blocks out past the city square. */
    public static Plan plan(CityLayout.Site site, Heights heights, int seaLevel, int outset) {
        return plan(site, heights, seaLevel, outset, false);
    }

    /** The same; a {@code port}'s plate is always at sea level, flush with the water, and the land is shaped to meet it. */
    public static Plan plan(CityLayout.Site site, Heights heights, int seaLevel, int outset, boolean port) {
        int cells = site.cells();
        Plan box = new Plan(0, null, null, cells, outset); // only its box and reach are read
        int boxMin = box.boxMin();
        int boxMax = box.boxMax();
        List<Integer> samples = new ArrayList<>();
        for (int lx = boxMin; lx <= boxMax; lx = next(lx, boxMax)) {
            for (int lz = boxMin; lz <= boxMax; lz = next(lz, boxMax)) {
                samples.add(heights.at(site.originX() + lx, site.originZ() + lz));
            }
        }
        samples.sort(Integer::compare);
        int plate = port ? seaLevel - 1 : Math.max(seaLevel - 1, samples.get(samples.size() / 2));
        return edges(site.originX(), site.originZ(), heights, new Plan(plate, null, null, cells, outset));
    }

    /**
     * An outside noble district's platform ({@link NobleAnnex}): its square {@code size} a side from world (originX,
     * originZ), its walls {@code outset} out, its plate {@code plateY}; a box margin of 2 and easing straight down to
     * the city's height over {@link NobleAnnex#EASE_REACH} blocks.
     */
    public static Plan platform(int originX, int originZ, int size, int outset, int plateY, Heights heights) {
        return edges(originX, originZ, heights, new Plan(plateY, null, null, 1, outset, size, -NobleAnnex.RISE / NobleAnnex.EASE_REACH));
    }

    /** {@code box}'s plan with its edges read from the land around it. */
    private static Plan edges(int originX, int originZ, Heights heights, Plan box) {
        int segments = box.segments();
        Edge[][] edges = new Edge[Side.values().length][segments];
        int[][] carveTo = new int[Side.values().length][segments];
        for (Side side : Side.values()) {
            for (int seg = 0; seg < segments; seg++) {
                int along = center(seg, box.boxMin(), box.boxMax());
                edges[side.ordinal()][seg] = classify(originX, originZ, heights, box.plateY(), side, along, box);
                carveTo[side.ordinal()][seg] = carveTarget(originX, originZ, heights, box.plateY(), side, along, box);
            }
        }
        return new Plan(box.plateY(), edges, carveTo, box.cells(), box.outset(), box.size(), box.run());
    }

    private static int next(int l, int boxMax) {
        return l == boxMax ? boxMax + 1 : Math.min(boxMax, l + MEDIAN_STEP);
    }

    /** Along-edge position of a stretch's centre. */
    static int center(int segment) {
        return center(segment, BOX_MIN, BOX_MAX);
    }

    static int center(int segment, int boxMin, int boxMax) {
        return Math.min(boxMax, boxMin + segment * EDGE_SEGMENT + EDGE_SEGMENT / 2);
    }

    /** Looks straight out from the box edge: does the land fall away, and if so does a 1:2 fill reach it? */
    static Edge classify(int originX, int originZ, Heights heights, int plate, Side side, int along, Plan box) {
        long sum = 0;
        boolean fillMeets = false;
        for (int d = RAY_STEP; d <= SLOPE_REACH; d += RAY_STEP) {
            int y = height(originX, originZ, heights, side, along, d, box);
            sum += y - plate;
            if (y >= plate - box.rise(d)) {
                fillMeets = true;
            }
        }
        if (sum >= 0) {
            return Edge.CARVE;
        }
        return fillMeets ? Edge.FILL : Edge.VALLEY;
    }

    /** The highest land around the end of the carve's reach, relative to the plate: what the carve has to meet. */
    static int carveTarget(int originX, int originZ, Heights heights, int plate, Side side, int along, Plan box) {
        int highest = Integer.MIN_VALUE;
        for (int offset = -RAY_SPREAD; offset <= RAY_SPREAD; offset += RAY_SPREAD) {
            int a = Math.max(box.boxMin(), Math.min(box.boxMax(), along + offset));
            for (int d = SLOPE_REACH - RAY_STEP; d <= SLOPE_REACH + BEYOND_REACH; d += RAY_STEP) {
                highest = Math.max(highest, height(originX, originZ, heights, side, a, d, box) - plate);
            }
        }
        return highest;
    }

    private static int height(int originX, int originZ, Heights heights, Side side, int along, int d, Plan box) {
        int[] l = outward(side, along, d, box.boxMin(), box.boxMax());
        return heights.at(originX + l[0], originZ + l[1]);
    }

    /** Local (x, z) {@code d} blocks straight out from the box edge at position {@code along} on {@code side}. */
    static int[] outward(Side side, int along, int d, int boxMin, int boxMax) {
        return switch (side) {
            case NORTH -> new int[]{along, boxMin - d};
            case EAST -> new int[]{boxMax + d, along};
            case SOUTH -> new int[]{along, boxMax + d};
            case WEST -> new int[]{boxMin - d, along};
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
        return carveRise(d, target, rise(d));
    }

    private static int carveRise(int d, int target, int gentle) {
        int steep = target <= 0 ? 0 : (int) Math.ceil((double) target * d / SLOPE_REACH);
        return Math.max(gentle, steep);
    }

    /**
     * A column of a city with an outside noble district: the platform's own inside its box, else the city's inside its;
     * else, where both shape the land, fill to the higher of their fills and carve to the lower of their carves (but
     * never below that fill), so the platform's easing and the city's meet.
     */
    public static Column column(Plan city, int lx, int lz, Plan platform, int px, int pz) {
        if (platform.inBox(px, pz)) {
            return column(platform, px, pz);
        }
        Column c = column(city, lx, lz);
        if (c.kind() != Kind.SLOPE && c.kind() != Kind.NONE) {
            return c;
        }
        Column p = column(platform, px, pz);
        if (p.kind() == Kind.NONE) {
            return c;
        }
        if (c.kind() == Kind.NONE) {
            return p;
        }
        boolean fill = c.fill() || p.fill();
        int low = c.fill() && p.fill() ? Math.max(c.low(), p.low()) : p.fill() ? p.low() : c.fill() ? c.low() : Math.min(c.low(), p.low());
        return new Column(Kind.SLOPE, low, Math.max(low, Math.min(c.high(), p.high())), fill, p.stone() && low == p.low());
    }

    public static Column column(Plan plan, int lx, int lz) {
        int boxMin = plan.boxMin();
        int boxMax = plan.boxMax();
        int dx = lx < boxMin ? boxMin - lx : lx > boxMax ? lx - boxMax : 0;
        int dz = lz < boxMin ? boxMin - lz : lz > boxMax ? lz - boxMax : 0;
        int plate = plan.plateY();
        if (dx == 0 && dz == 0) {
            return insideBox(plan, lx, lz);
        }
        int d = Math.max(dx, dz);
        if (plan.run() < 0) {
            // An outside noble district's platform: its easing, carved and filled, reaches the city's height; past that
            // it only fills, as steeply, so land lower than the city's meets it instead of dropping away.
            if (d > NobleAnnex.FILL_REACH) {
                return Column.NONE;
            }
            int low = plate - plan.rise(d);
            return new Column(Kind.SLOPE, low, d > NobleAnnex.EASE_REACH ? Integer.MAX_VALUE : low, true, true);
        }
        if (d > SLOPE_REACH) {
            return Column.NONE;
        }
        int target;
        boolean fill;
        if (dx > 0 && dz > 0) {
            // Corner: as steep as the steeper of the two sides meeting there, and only filled if both are filled.
            Side xSide = lx < boxMin ? Side.WEST : Side.EAST;
            Side zSide = lz < boxMin ? Side.NORTH : Side.SOUTH;
            int xEnd = zSide == Side.NORTH ? boxMin : boxMax;
            int zEnd = xSide == Side.WEST ? boxMin : boxMax;
            target = Math.max(carveTarget(plan, xSide, xEnd), carveTarget(plan, zSide, zEnd));
            fill = plan.edge(xSide, segment(plan, xEnd)) == Edge.FILL && plan.edge(zSide, segment(plan, zEnd)) == Edge.FILL;
        } else {
            Side side = dz > 0 ? (lz < boxMin ? Side.NORTH : Side.SOUTH) : (lx < boxMin ? Side.WEST : Side.EAST);
            int along = dz > 0 ? lx : lz;
            target = carveTarget(plan, side, along);
            fill = plan.edge(side, segment(plan, along)) == Edge.FILL;
        }
        return new Column(Kind.SLOPE, plate - plan.rise(d), plate + carveRise(d, target, plan.rise(d)), fill);
    }

    /** Inside the box: the plate, except under and outside the wall on valley stretches. */
    private static Column insideBox(Plan plan, int lx, int lz) {
        int plate = plan.plateY();
        int far = plan.size() - 1;
        int wall = CityLayout.ROAD_WIDTH;
        // The wall's outer face, on the low and high side of each axis.
        int lo = -plan.outset();
        int hi = far + plan.outset();
        // Which valley stretch, if any, this column faces, and whether it is outside the wall or under it.
        boolean outside = false;
        boolean under = false;
        if (lz < wall && valley(plan, Side.NORTH, lx)) {
            outside |= lz < lo;
            under |= lz >= lo;
        }
        if (lz > far - wall && valley(plan, Side.SOUTH, lx)) {
            outside |= lz > hi;
            under |= lz <= hi;
        }
        if (lx < wall && valley(plan, Side.WEST, lz)) {
            outside |= lx < lo;
            under |= lx >= lo;
        }
        if (lx > far - wall && valley(plan, Side.EAST, lz)) {
            outside |= lx > hi;
            under |= lx <= hi;
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
        return plan.edge(side, segment(plan, along)) == Edge.VALLEY;
    }

    /** The carve's target at a position along a side, blended linearly between neighbouring stretch centres. */
    static int carveTarget(Plan plan, Side side, int along) {
        int[] targets = plan.carveTo()[side.ordinal()];
        int boxMin = plan.boxMin();
        int boxMax = plan.boxMax();
        if (along <= center(0, boxMin, boxMax)) {
            return targets[0];
        }
        for (int seg = 0; seg < targets.length - 1; seg++) {
            int a = center(seg, boxMin, boxMax);
            int b = center(seg + 1, boxMin, boxMax);
            if (along <= b) {
                double t = b == a ? 1 : (double) (along - a) / (b - a);
                return (int) Math.round(targets[seg] + (targets[seg + 1] - targets[seg]) * t);
            }
        }
        return targets[targets.length - 1];
    }

    static int segment(Plan plan, int along) {
        return Math.max(0, Math.min(plan.segments() - 1, (along - plan.boxMin()) / EDGE_SEGMENT));
    }

    /**
     * Which cell (0 to cells - 1 along each axis) owns a column, inside or outside the box. Columns outside the city
     * square belong to the nearest edge cell, so each cell's flattening covers a disjoint area and they never overlap.
     * The size-less forms are for a 3 × 3 city.
     */
    public static int cellOf(int local) {
        return cellOf(CityLayout.CELLS_PER_SIDE, local);
    }

    public static int cellOf(int cells, int local) {
        int clamped = Math.max(0, Math.min(size(cells) - 1, local));
        return Math.min(cells - 1, clamped / CityLayout.DISTRICT_SPACING);
    }

    /** Local extent [min, max] of the columns a cell flattens along one axis, slopes included. */
    public static int ownedMin(int cell) {
        return ownedMin(CityLayout.CELLS_PER_SIDE, cell);
    }

    public static int ownedMin(int cells, int cell) {
        return ownedMin(cells, cell, CityLayout.WALL_OUTSET);
    }

    public static int ownedMin(int cells, int cell, int outset) {
        return cell == 0 ? boxMin(cells, outset) - CELL_REACH : cell * CityLayout.DISTRICT_SPACING;
    }

    public static int ownedMax(int cell) {
        return ownedMax(CityLayout.CELLS_PER_SIDE, cell);
    }

    public static int ownedMax(int cells, int cell) {
        return ownedMax(cells, cell, CityLayout.WALL_OUTSET);
    }

    public static int ownedMax(int cells, int cell, int outset) {
        return cell == cells - 1 ? boxMax(cells, outset) + CELL_REACH : (cell + 1) * CityLayout.DISTRICT_SPACING - 1;
    }

    /**
     * The outer slope, past what the edge cells lay ({@link #CELL_REACH}) out to {@link #SLOPE_REACH}, cut into tiles
     * no longer than {@link #SLOPE_TILE}: each a local box {minX, minZ, maxX, maxZ}. The north and south strips run
     * the full width, corners included; the west and east strips fill between them. Together they cover the ring once.
     */
    public static List<int[]> slopeTiles(int cells, int outset) {
        int inMin = boxMin(cells, outset) - CELL_REACH;
        int inMax = boxMax(cells, outset) + CELL_REACH;
        int outMin = boxMin(cells, outset) - SLOPE_REACH;
        int outMax = boxMax(cells, outset) + SLOPE_REACH;
        List<int[]> tiles = new ArrayList<>();
        for (int[] a : split(outMin, outMax)) {
            tiles.add(new int[]{a[0], outMin, a[1], inMin - 1});
            tiles.add(new int[]{a[0], inMax + 1, a[1], outMax});
        }
        for (int[] a : split(inMin, inMax)) {
            tiles.add(new int[]{outMin, a[0], inMin - 1, a[1]});
            tiles.add(new int[]{inMax + 1, a[0], outMax, a[1]});
        }
        return tiles;
    }

    /** A box cut into a grid of tiles no longer than {@link #SLOPE_TILE} a side: each {minX, minZ, maxX, maxZ}. */
    public static List<int[]> tiles(int minX, int minZ, int maxX, int maxZ) {
        List<int[]> tiles = new ArrayList<>();
        for (int[] x : split(minX, maxX)) {
            for (int[] z : split(minZ, maxZ)) {
                tiles.add(new int[]{x[0], z[0], x[1], z[1]});
            }
        }
        return tiles;
    }

    /** [min, max] cut into the fewest equal runs no longer than {@link #SLOPE_TILE}. */
    private static List<int[]> split(int min, int max) {
        int length = max - min + 1;
        int runs = (length + SLOPE_TILE - 1) / SLOPE_TILE;
        List<int[]> out = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            out.add(new int[]{min + i * length / runs, min + (i + 1) * length / runs - 1});
        }
        return out;
    }
}
