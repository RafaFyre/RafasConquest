package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * How a city reshapes the land, decided from the original terrain alone so every cell of a city agrees.
 * <p>
 * The <b>box</b> is the city square plus the wall, which stands {@link CityLayout#WALL_OUTSET} blocks out past it, plus
 * {@link #BOX_MARGIN} blocks past the wall. Inside it, the ground follows the land in
 * {@link #TILE}-block <b>tiles</b>, each one level, neighbouring tiles at most 1 block apart (a 4-block rise is spread
 * over 4 tiles); {@link Pin pinned} areas (the town centre, walls, big buildings) are flat. Everything above is cleared,
 * everything below is filled. The <b>plate</b> is the box's median height: the level the harbour and the path out to an
 * outside noble district keep.
 * <p>
 * Around the box, out to {@link #SLOPE_REACH} blocks:
 * <ul>
 *   <li>The <b>carve</b> removes land above a slope rising from the plate. The slope is 1 up per 2 out, made steeper
 *       wherever that is needed to meet the land at the end of its reach, so the carve never ends in a wall. How steep
 *       blends smoothly along the edge. The carve only ever removes blocks.</li>
 *   <li>The <b>fill</b> eases the plate down into lower land with a 1-up-2-out slope, made steeper wherever that is
 *       needed to meet the land at the end of its reach, so even a deep valley is eased into rather than left as a
 *       cliff. It only runs on stretches of the edge where the land falls away, so blocks are never added to build up
 *       toward higher ground.</li>
 * </ul>
 * Coordinates here are city-local, as in {@link CityLayout}: (0, 0) is the city square's north-west corner.
 */
public final class CityTerrain {
    /** How far past the wall the box reaches: a flat zone outside the walls, where the easing starts. */
    public static final int BOX_MARGIN = 30;
    /** An outside noble district's platform keeps a narrow margin, so its easing starts at its walls. */
    static final int PLATFORM_MARGIN = 2;
    /** Lowest and highest local coordinate inside the box, on both axes. The wall stands out past the city square. */
    public static final int BOX_MIN = -CityLayout.WALL_OUTSET - BOX_MARGIN;
    public static final int BOX_MAX = CityLayout.SIZE - 1 + CityLayout.WALL_OUTSET + BOX_MARGIN;
    /** How far the carve and fill may run past the box. */
    public static final int SLOPE_REACH = 84;
    /**
     * How far past the box an edge cell lays the slope itself. An edge cell's structure start sits about 110 blocks
     * from the box edge, and Minecraft only places pieces within about 128 blocks of a start, so the slope beyond this
     * is laid by {@link #slopeTiles slope tiles}, each its own structure start.
     */
    public static final int CELL_REACH = 18;
    /** Longest side of a slope tile: with its start at its middle, every column is well within the engine's reach. */
    static final int SLOPE_TILE = 96;
    public static final int EDGE_SEGMENT = 32;
    public static final int SEGMENTS_PER_SIDE = (BOX_MAX - BOX_MIN + EDGE_SEGMENT) / EDGE_SEGMENT;
    static final int MEDIAN_STEP = 24;
    /** Side of a height tile: neighbouring tiles' ground is at most 1 block apart. */
    public static final int TILE = 8;
    /** The ground is read once every this many tiles each way: the field smooths between readings anyway. */
    static final int SAMPLE_TILES = 2;
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
        /** Land falls away: fill down to it, however deep (and carve any bumps). */
        FILL
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
    public record Plan(int plateY, Edge[][] edges, int[][] carveTo, int cells, int outset, int size, int run, int[] levels) {
        /** A plan with no tile field: the plate everywhere. */
        public Plan(int plateY, Edge[][] edges, int[][] carveTo, int cells, int outset, int size, int run) {
            this(plateY, edges, carveTo, cells, outset, size, run, null);
        }

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
            return -outset - margin();
        }

        public int boxMax() {
            return size - 1 + outset + margin();
        }

        private int margin() {
            return run < 0 ? PLATFORM_MARGIN : BOX_MARGIN;
        }

        /** The stretches along each side. */
        public int segments() {
            return (boxMax() - boxMin() + EDGE_SEGMENT) / EDGE_SEGMENT;
        }

        /** How high the easing rises {@code d} blocks out: one block per {@code run}, or {@code -run} per block. */
        public int rise(int d) {
            return run < 0 ? -run * d : (d + run - 1) / run;
        }

        /** The first tile's index along each axis (tiles start at local multiples of {@link #TILE}). */
        int tile0() {
            return Math.floorDiv(boxMin(), TILE);
        }

        /** Tiles along each side of the box. */
        public int tiles() {
            return Math.floorDiv(boxMax(), TILE) - tile0() + 1;
        }

        /** The ground's level at local (x, z): its tile's, held onto the box; the plate where there is no field. */
        public int level(int lx, int lz) {
            if (levels == null) {
                return plateY;
            }
            int n = tiles();
            int i = Math.max(0, Math.min(n - 1, Math.floorDiv(lx, TILE) - tile0()));
            int j = Math.max(0, Math.min(n - 1, Math.floorDiv(lz, TILE) - tile0()));
            return levels[j * n + i];
        }

        /** The lowest ground in the box. */
        public int minLevel() {
            return levels == null ? plateY : Arrays.stream(levels).min().orElse(plateY);
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
                    && Arrays.deepEquals(p.edges, edges) && Arrays.deepEquals(p.carveTo, carveTo) && Arrays.equals(p.levels, levels);
        }

        @Override
        public int hashCode() {
            return 31 * (31 * (31 * (31 * (31 * plateY + cells) + outset) + size * 7 + run) + Arrays.deepHashCode(edges)) + Arrays.deepHashCode(carveTo) + 7 * Arrays.hashCode(levels);
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
        return -outset - BOX_MARGIN;
    }

    public static int boxMax(int cells, int outset) {
        return size(cells) - 1 + outset + BOX_MARGIN;
    }

    public static int segments(int cells) {
        return segments(cells, CityLayout.WALL_OUTSET);
    }

    public static int segments(int cells, int outset) {
        return (boxMax(cells, outset) - boxMin(cells, outset) + EDGE_SEGMENT) / EDGE_SEGMENT;
    }

    public enum Kind {
        /** Inside the box: flatten to its tile's level ({@code low}). */
        PLATE,
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
        return plan(site, heights, seaLevel, outset, port, List.of());
    }

    /**
     * A city-local rectangle [minX, maxX] × [minZ, maxZ] whose tiles all stand at one level: the plate if
     * {@code atPlate}, else the field's mean under it; either way held within reach of the pins laid before it.
     */
    public record Pin(int minX, int minZ, int maxX, int maxZ, boolean atPlate) {
    }

    /** The same, with {@code pins} laid in order over the tile field. */
    public static Plan plan(CityLayout.Site site, Heights heights, int seaLevel, int outset, boolean port, List<Pin> pins) {
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
        int[] levels = levels(site.originX(), site.originZ(), heights, seaLevel - 1, plate, box, pins);
        return edges(site.originX(), site.originZ(), heights, new Plan(plate, null, null, cells, outset, box.size(), box.run(), levels));
    }

    /**
     * The tile field: the land under each tile (read every {@link #SAMPLE_TILES} tiles, never below {@code floor}),
     * brought to the nearest field whose neighbouring tiles (diagonals too) differ by at most 1, as the middle of the
     * lowest such field above the land and the highest below it; then the pins, each flat, the rest kept within 1 a
     * tile of them.
     */
    static int[] levels(int originX, int originZ, Heights heights, int floor, int plate, Plan box, List<Pin> pins) {
        int n = box.tiles();
        int t0 = box.tile0();
        int[] read = new int[n * n];
        for (int j = 0; j < n; j += SAMPLE_TILES) {
            for (int i = 0; i < n; i += SAMPLE_TILES) {
                int lx = Math.max(box.boxMin(), Math.min(box.boxMax(), (t0 + i) * TILE + TILE / 2));
                int lz = Math.max(box.boxMin(), Math.min(box.boxMax(), (t0 + j) * TILE + TILE / 2));
                read[j * n + i] = Math.max(floor, heights.at(originX + lx, originZ + lz));
            }
        }
        int[] above = new int[n * n];
        int[] below = new int[n * n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                above[j * n + i] = read[(j - j % SAMPLE_TILES) * n + i - i % SAMPLE_TILES];
                below[j * n + i] = -above[j * n + i];
            }
        }
        lowestAbove(above, n);
        lowestAbove(below, n); // negated: the highest field below the land
        int[] mid = new int[n * n];
        for (int k = 0; k < mid.length; k++) {
            mid[k] = Math.floorDiv(above[k] - below[k], 2);
        }
        // The pins in tiles, {a0, b0, a1, b1, level}, each held within reach of those laid before it.
        List<int[]> laid = new ArrayList<>();
        for (Pin p : pins) {
            if (p.maxX() < box.boxMin() || p.maxZ() < box.boxMin() || p.minX() > box.boxMax() || p.minZ() > box.boxMax()) {
                continue; // wholly outside the box
            }
            int a0 = Math.floorDiv(Math.max(p.minX(), box.boxMin()), TILE) - t0;
            int b0 = Math.floorDiv(Math.max(p.minZ(), box.boxMin()), TILE) - t0;
            int a1 = Math.floorDiv(Math.min(p.maxX(), box.boxMax()), TILE) - t0;
            int b1 = Math.floorDiv(Math.min(p.maxZ(), box.boxMax()), TILE) - t0;
            long sum = 0;
            for (int j = b0; j <= b1; j++) {
                for (int i = a0; i <= a1; i++) {
                    sum += mid[j * n + i];
                }
            }
            int v = p.atPlate() ? plate : (int) Math.round((double) sum / ((a1 - a0 + 1) * (b1 - b0 + 1)));
            laid.add(new int[]{a0, b0, a1, b1, within(laid, a0, b0, a1, b1, v)});
        }
        int[] levels = new int[n * n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                levels[j * n + i] = within(laid, i, j, i, j, mid[j * n + i]);
            }
        }
        return levels;
    }

    /** {@code v} for tiles [a0, a1] × [b0, b1], held within 1 a tile of each pin laid: their level ± their distance. */
    private static int within(List<int[]> laid, int a0, int b0, int a1, int b1, int v) {
        int lo = Integer.MIN_VALUE;
        int hi = Integer.MAX_VALUE;
        for (int[] q : laid) {
            int d = Math.max(Math.max(0, Math.max(q[0] - a1, a0 - q[2])), Math.max(0, Math.max(q[1] - b1, b0 - q[3])));
            lo = Math.max(lo, q[4] - d);
            hi = Math.min(hi, q[4] + d);
        }
        // shortcut: pins too close for their levels (a huge one between two far apart) meet halfway; none do in a city.
        return lo <= hi ? Math.max(lo, Math.min(hi, v)) : Math.floorDiv(lo + hi, 2);
    }

    /**
     * Turns {@code a} (n × n) into the lowest field at or above it whose neighbours, diagonals too, differ by at most 1:
     * each tile the least of every tile's value plus its distance in tiles. Two sweeps, as a chessboard distance map.
     */
    static void lowestAbove(int[] a, int n) {
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                int v = a[j * n + i];
                if (i > 0) {
                    v = Math.min(v, a[j * n + i - 1] + 1);
                }
                if (j > 0) {
                    v = Math.min(v, a[(j - 1) * n + i] + 1);
                    if (i > 0) {
                        v = Math.min(v, a[(j - 1) * n + i - 1] + 1);
                    }
                    if (i < n - 1) {
                        v = Math.min(v, a[(j - 1) * n + i + 1] + 1);
                    }
                }
                a[j * n + i] = v;
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            for (int i = n - 1; i >= 0; i--) {
                int v = a[j * n + i];
                if (i < n - 1) {
                    v = Math.min(v, a[j * n + i + 1] + 1);
                }
                if (j < n - 1) {
                    v = Math.min(v, a[(j + 1) * n + i] + 1);
                    if (i < n - 1) {
                        v = Math.min(v, a[(j + 1) * n + i + 1] + 1);
                    }
                    if (i > 0) {
                        v = Math.min(v, a[(j + 1) * n + i - 1] + 1);
                    }
                }
                a[j * n + i] = v;
            }
        }
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
                int[] foot = outward(side, along, 0, box.boxMin(), box.boxMax());
                int base = box.level(foot[0], foot[1]); // the slope starts from the box's ground at this stretch
                edges[side.ordinal()][seg] = classify(originX, originZ, heights, base, side, along, box);
                carveTo[side.ordinal()][seg] = carveTarget(originX, originZ, heights, base, side, along, box);
            }
        }
        return new Plan(box.plateY(), edges, carveTo, box.cells(), box.outset(), box.size(), box.run(), box.levels());
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

    /** Looks straight out from the box edge: does the land fall away? */
    static Edge classify(int originX, int originZ, Heights heights, int plate, Side side, int along, Plan box) {
        long sum = 0;
        for (int d = RAY_STEP; d <= SLOPE_REACH; d += RAY_STEP) {
            sum += height(originX, originZ, heights, side, along, d, box) - plate;
        }
        return sum >= 0 ? Edge.CARVE : Edge.FILL;
    }

    /**
     * The highest land around the end of the slope's reach, relative to the plate: what the carve has to meet, or (below
     * the plate) what the fill comes down to.
     */
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

    /** How far the fill drops {@code d} blocks out: the gentle slope, or steeper to come down to land {@code -target} below. */
    private static int fillDrop(int d, int target, int gentle) {
        return carveRise(d, -target, gentle);
    }

    /**
     * A column of a city with an outside noble district: the platform's own inside its box, else the city's inside its;
     * else, where both shape the land, fill to the higher of their fills and carve to the higher of their carves, so
     * the platform's easing and the city's merge as one.
     */
    public static Column column(Plan city, int lx, int lz, Plan platform, int px, int pz) {
        if (platform.inBox(px, pz)) {
            return column(platform, px, pz);
        }
        Column c = column(city, lx, lz);
        Column p = column(platform, px, pz);
        // The platform's easing runs on over the city's flat zone where it stands above it, not cut off into a cliff.
        if (c.kind() == Kind.PLATE && p.kind() == Kind.SLOPE && p.low() > c.low()) {
            return new Column(Kind.SLOPE, p.low(), p.low(), true, true);
        }
        if (c.kind() != Kind.SLOPE && c.kind() != Kind.NONE) {
            return c;
        }
        if (p.kind() == Kind.NONE) {
            return c;
        }
        if (c.kind() == Kind.NONE) {
            return p;
        }
        boolean fill = c.fill() || p.fill();
        int low = c.fill() && p.fill() ? Math.max(c.low(), p.low()) : p.fill() ? p.low() : c.fill() ? c.low() : Math.min(c.low(), p.low());
        // The higher of the two easings stands, so neither cuts into the other; the platform's fill-only reach never carves.
        boolean filling = p.high() == Integer.MAX_VALUE;
        int high = Math.max(low, filling ? c.high() : Math.max(c.high(), p.high()));
        return new Column(Kind.SLOPE, low, high, fill, p.stone()); // all stone where they merge, no grass seam
    }

    public static Column column(Plan plan, int lx, int lz) {
        int boxMin = plan.boxMin();
        int boxMax = plan.boxMax();
        int dx = lx < boxMin ? boxMin - lx : lx > boxMax ? lx - boxMax : 0;
        int dz = lz < boxMin ? boxMin - lz : lz > boxMax ? lz - boxMax : 0;
        // The slope starts from the box's ground nearest the column.
        int plate = plan.level(Math.max(boxMin, Math.min(boxMax, lx)), Math.max(boxMin, Math.min(boxMax, lz)));
        if (dx == 0 && dz == 0) {
            return new Column(Kind.PLATE, plate, plate, false);
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
        int gentle = plan.rise(d);
        return new Column(Kind.SLOPE, plate - fillDrop(d, target, gentle), plate + carveRise(d, target, gentle), fill);
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
