package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Where cities go and how each one is laid out.
 * <p>
 * A city is a planned square: a street grid with a 3-wide road every 32 blocks (including an edge road all round),
 * giving city blocks of 29 × 29, each split into four 14 × 14 plots. The square is cut into N × N cells (districts) of
 * 5 × 5 blocks; each cell is its own structure start (the town centre in the one holding the middle block, a district in
 * each of the others), which keeps every piece inside the engine's 128-block reach from its start. Every cell builds
 * its share of the same grid, so cells meet seamlessly. The constants and size-less methods describe a 3 × 3 city.
 * <p>
 * The cities built are the titles' seat cities ({@link SeatCities}), published from the world map. The old seeded
 * City-state sites ({@link #gridSite}, {@link #cityStateSitesInBox}) only place City-state factions now, with no city.
 */
public final class CityLayout {
    public static final int ROAD_PERIOD = 32;
    public static final int ROAD_WIDTH = 3;
    /** Two plots per block side, with a 1-block gap between them. */
    public static final int PLOT_SIZE = (ROAD_PERIOD - ROAD_WIDTH - 1) / 2;
    public static final int BLOCKS_PER_CELL = 5;
    public static final int CELLS_PER_SIDE = 3;
    /** The cities built, from the world map; set from the server thread when the overworld loads. */
    private static volatile java.util.function.BiFunction<int[], int[], List<SeatCities.City>> builtCities = (min, max) -> List.of();
    public static final int BLOCKS_PER_SIDE = BLOCKS_PER_CELL * CELLS_PER_SIDE;
    /** The block holding the town centre instead of plots. */
    public static final int CENTER_BLOCK = BLOCKS_PER_SIDE / 2;
    /** Side length of the whole city, edge roads included. */
    public static final int SIZE = BLOCKS_PER_SIDE * ROAD_PERIOD + ROAD_WIDTH;
    public static final int HALF_EXTENT = SIZE / 2;
    /** Half the side of the largest city (a 9 × 9 metropolis), for searches that must find any city nearby. */
    public static final int MAX_HALF_EXTENT = (9 * BLOCKS_PER_CELL * ROAD_PERIOD + ROAD_WIDTH) / 2;
    /** Distance between cell centres, and so between district starts. */
    public static final int DISTRICT_SPACING = BLOCKS_PER_CELL * ROAD_PERIOD;
    /** Extra gap kept between a city's edge and a Capital or Farm. */
    public static final int CLEARANCE = 64;
    /** The road that runs out through a gate on every side: the avenue beside the town centre. */
    public static final int GATE_ROAD = BLOCKS_PER_SIDE / 2;
    public static final int WALL_WIDTH = 5;
    /** How far the wall stands out past the city square: it covers the 3-wide edge road and 2 blocks beyond. */
    public static final int WALL_OUTSET = WALL_WIDTH - ROAD_WIDTH;

    /**
     * One wall segment, in city-local coordinates. The wall stands on the edge road band plus {@link #WALL_OUTSET}
     * blocks outside it, and is laid as a pinwheel: each side runs clockwise for 15 segments of 32. The first segment
     * of a side is a corner, {@link #WALL_OUTSET} longer at its start, so it also covers the corner the previous side
     * leaves open: 2 + 15 × 32 + 5 = 487 closes exactly. {@code quarterTurns} rotates the template (whose outer face
     * is template z = 0) clockwise.
     */
    public record WallSegment(Side side, int index, int originX, int originZ, int quarterTurns, boolean gate) {
        public boolean corner() {
            return index == 0;
        }

        /** Template length along the wall. */
        public int length() {
            return ROAD_PERIOD + (corner() ? WALL_OUTSET : 0);
        }
    }

    public enum Side {
        NORTH, EAST, SOUTH, WEST
    }

    private CityLayout() {
    }

    /**
     * A city's centre. {@code capital} cities are Empire capitals (at the Capital sites); the rest are City-state
     * homes from the city grid. Both are built the same way.
     */
    public record Site(int x, int z, boolean capital, int cells) {
        /** A 3 × 3 city. */
        public Site(int x, int z, boolean capital) {
            this(x, z, capital, CELLS_PER_SIDE);
        }

        /** City blocks along a side. */
        public int blocksPerSide() {
            return cells * BLOCKS_PER_CELL;
        }

        /** Side length, edge roads included. */
        public int size() {
            return blocksPerSide() * ROAD_PERIOD + ROAD_WIDTH;
        }

        public int halfExtent() {
            return size() / 2;
        }

        /** The block holding the town centre. */
        public int centerBlock() {
            return blocksPerSide() / 2;
        }

        /** The cell holding the town centre: the city's own structure start. */
        public int centerCell() {
            return centerBlock() / BLOCKS_PER_CELL;
        }

        /** World x of the city's west edge (local 0). */
        public int originX() {
            return x - halfExtent();
        }

        /** World z of the city's north edge (local 0). */
        public int originZ() {
            return z - halfExtent();
        }
    }

    /**
     * One of the city's N × N cells, its structure start at the cell's middle. The cell holding the town centre is the
     * city's own start; the others are districts.
     */
    public record District(Site city, int cellX, int cellZ) {
        public int x() {
            return city.originX() + cellX * DISTRICT_SPACING + DISTRICT_SPACING / 2 + 1;
        }

        public int z() {
            return city.originZ() + cellZ * DISTRICT_SPACING + DISTRICT_SPACING / 2 + 1;
        }
    }

    /** Road indices (0 .. BLOCKS_PER_SIDE) a cell builds along one axis. The last cell also builds the far edge road. */
    public static List<Integer> roadsInCell(int cell) {
        return roadsInCell(CELLS_PER_SIDE, cell);
    }

    /** The same, for a city {@code cells} cells a side. */
    public static List<Integer> roadsInCell(int cells, int cell) {
        List<Integer> roads = new ArrayList<>();
        for (int k = cell * BLOCKS_PER_CELL; k < (cell + 1) * BLOCKS_PER_CELL; k++) {
            roads.add(k);
        }
        if (cell == cells - 1) {
            roads.add(cells * BLOCKS_PER_CELL);
        }
        return roads;
    }

    /** Block indices (0 .. BLOCKS_PER_SIDE - 1) a cell builds along one axis. */
    public static List<Integer> blocksInCell(int cell) {
        List<Integer> blocks = new ArrayList<>();
        for (int k = cell * BLOCKS_PER_CELL; k < (cell + 1) * BLOCKS_PER_CELL; k++) {
            blocks.add(k);
        }
        return blocks;
    }

    /** Local coordinate of road k's first block. */
    public static int roadStart(int k) {
        return k * ROAD_PERIOD;
    }

    /** Local coordinate of block k's first block (just past its road). */
    public static int blockStart(int k) {
        return k * ROAD_PERIOD + ROAD_WIDTH;
    }

    /** Local extent [min, max] of a cell along one axis, including the road on its far side. */
    public static int cellMin(int cell) {
        return roadStart(cell * BLOCKS_PER_CELL);
    }

    public static int cellMax(int cell) {
        return roadStart((cell + 1) * BLOCKS_PER_CELL) + ROAD_WIDTH - 1;
    }

    /** All wall segments, every side, in clockwise order. */
    public static List<WallSegment> wallSegments() {
        return wallSegments(CELLS_PER_SIDE);
    }

    /** All wall segments of a city {@code cells} cells a side. */
    public static List<WallSegment> wallSegments(int cells) {
        List<WallSegment> all = new ArrayList<>();
        for (Side side : Side.values()) {
            for (int j = 0; j < cells * BLOCKS_PER_CELL; j++) {
                all.add(wallSegment(cells, side, j));
            }
        }
        return all;
    }

    /** The wall segments a cell builds, in clockwise order along each side. */
    public static List<WallSegment> wallSegments(int cellX, int cellZ) {
        return wallSegments(CELLS_PER_SIDE, cellX, cellZ);
    }

    public static List<WallSegment> wallSegments(int cells, int cellX, int cellZ) {
        List<WallSegment> mine = new ArrayList<>();
        for (WallSegment seg : wallSegments(cells)) {
            int[] cell = wallCell(cells, seg);
            if (cell[0] == cellX && cell[1] == cellZ) {
                mine.add(seg);
            }
        }
        return mine;
    }

    static WallSegment wallSegment(Side side, int j) {
        return wallSegment(CELLS_PER_SIDE, side, j);
    }

    static WallSegment wallSegment(int cells, Side side, int j) {
        int blocks = cells * BLOCKS_PER_CELL;
        int far = blocks * ROAD_PERIOD + ROAD_WIDTH - 1;
        int out = WALL_OUTSET;
        int gate = blocks / 2;
        // A corner segment starts WALL_OUTSET early; every other one starts on its road.
        int step = j * ROAD_PERIOD - (j == 0 ? out : 0);
        return switch (side) {
            case NORTH -> new WallSegment(side, j, step, -out, 0, j == gate);
            case EAST -> new WallSegment(side, j, far + out, step, 1, j == gate);
            // South and west run back toward the origin, so road k is met by segment (blocks - k).
            case SOUTH -> new WallSegment(side, j, far - step, far + out, 2, j == blocks - gate);
            case WEST -> new WallSegment(side, j, -out, far - step, 3, j == blocks - gate);
        };
    }

    private static int[] wallCell(int cells, WallSegment seg) {
        int along = seg.index() / BLOCKS_PER_CELL;
        int last = cells - 1;
        return switch (seg.side()) {
            case NORTH -> new int[]{along, 0};
            case EAST -> new int[]{last, along};
            case SOUTH -> new int[]{last - along, last};
            case WEST -> new int[]{0, last - along};
        };
    }

    /**
     * City-local (x, z) of template block (tx, tz) for a segment, matching vanilla's clockwise template rotation
     * about the template origin.
     */
    public static int[] wallBlock(WallSegment seg, int tx, int tz) {
        return switch (seg.quarterTurns()) {
            case 0 -> new int[]{seg.originX() + tx, seg.originZ() + tz};
            case 1 -> new int[]{seg.originX() - tz, seg.originZ() + tx};
            case 2 -> new int[]{seg.originX() - tx, seg.originZ() - tz};
            default -> new int[]{seg.originX() + tz, seg.originZ() - tx};
        };
    }

    /** True if city-local (x, z) is on the wall: the outermost road band and the {@link #WALL_OUTSET} blocks past it. */
    public static boolean inWallBand(int localX, int localZ) {
        return inWallBand(SIZE, localX, localZ);
    }

    /** The same, for a city {@code size} blocks a side. */
    public static boolean inWallBand(int size, int localX, int localZ) {
        if (localX < -WALL_OUTSET || localZ < -WALL_OUTSET || localX >= size + WALL_OUTSET || localZ >= size + WALL_OUTSET) {
            return false;
        }
        int inner = size - ROAD_WIDTH;
        return localX < ROAD_WIDTH || localZ < ROAD_WIDTH || localX >= inner || localZ >= inner;
    }

    /** The city whose wall band contains world (x, z), if any. */
    public static Optional<Site> wallAt(long seed, WorldSettings s, int x, int z) {
        int r = MAX_HALF_EXTENT + WALL_OUTSET + 1;
        for (Site city : allSitesInBox(seed, s, x - r, z - r, x + r, z + r)) {
            if (inWallBand(city.size(), x - city.originX(), z - city.originZ())) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }

    /** True if any block of the chunk lies on some city's wall. Cheap; safe on any thread. */
    public static boolean chunkTouchesWall(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int r = MAX_HALF_EXTENT + WALL_OUTSET + 1;
        for (Site city : allSitesInBox(seed, s, minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            int lx0 = minX - city.originX();
            int lz0 = minZ - city.originZ();
            int lo = -WALL_OUTSET;
            int hi = city.size() + WALL_OUTSET;
            boolean overlapsCity = lx0 + 15 >= lo && lz0 + 15 >= lo && lx0 < hi && lz0 < hi;
            int inner = city.size() - ROAD_WIDTH;
            boolean insideInner = lx0 >= ROAD_WIDTH && lz0 >= ROAD_WIDTH && lx0 + 15 < inner && lz0 + 15 < inner;
            if (overlapsCity && !insideInner) {
                return true;
            }
        }
        return false;
    }

    /** True if any block of the chunk lies inside some city's square (its walls included). Cheap; safe on any thread. */
    public static boolean chunkInCity(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int r = MAX_HALF_EXTENT + WALL_OUTSET + 1;
        for (Site city : allSitesInBox(seed, s, minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            int lx0 = minX - city.originX();
            int lz0 = minZ - city.originZ();
            int lo = -WALL_OUTSET;
            int hi = city.size() + WALL_OUTSET;
            if (lx0 + 15 >= lo && lz0 + 15 >= lo && lx0 < hi && lz0 < hi) {
                return true;
            }
        }
        return false;
    }

    public static Optional<Site> gridSite(long seed, WorldSettings s, int cellX, int cellZ) {
        if (!s.citiesEnabled()) {
            return Optional.empty();
        }
        int size = s.cityCellSize();
        int span = Math.max(1, size - 2 * s.cityCellMargin());
        long h = SeedHash.hash(seed, SeedHash.SALT_CITY, cellX, cellZ);
        int x = cellX * size + s.cityCellMargin() + SeedHash.intInRange(h, 0, span - 1);
        int z = cellZ * size + s.cityCellMargin() + SeedHash.intInRange(SeedHash.mix(h), 0, span - 1);
        if (Math.sqrt((double) x * x + (double) z * z) < s.eyeRadius() + HALF_EXTENT) {
            return Optional.empty();
        }
        // Capitals are whole cities too, so keep a full city width (plus clearance) away from them. Their seeded
        // sites: a Capital that moves to land keeps clear of City-states itself.
        int clear = SIZE + CLEARANCE;
        for (CapitalLayout.Site capital : CapitalLayout.rawSitesInBox(seed, s, x - clear, z - clear, x + clear, z + clear)) {
            if (Math.abs(capital.x() - x) <= clear && Math.abs(capital.z() - z) <= clear) {
                return Optional.empty();
            }
        }
        return Optional.of(new Site(x, z, false));
    }

    /** Publishes the world's seat cities: those whose square reaches into a box ({min x, min z}, {max x, max z}). */
    public static void publishBuilt(java.util.function.BiFunction<int[], int[], List<SeatCities.City>> cities) {
        builtCities = cities;
    }

    /**
     * Every city built, whose centre lies in the box: the titles' seat cities. A kingdom's or empire's is built whatever
     * its biome ({@code capital}).
     */
    public static List<Site> allSitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
        List<Site> sites = new ArrayList<>();
        for (SeatCities.City c : builtCities.apply(new int[]{minX, minZ}, new int[]{maxX, maxZ})) {
            if (c.x() >= minX && c.x() <= maxX && c.z() >= minZ && c.z() <= maxZ) {
                sites.add(new Site(c.x(), c.z(), c.tier().ordinal() >= TitleMap.Tier.KINGDOM.ordinal(), c.districts()));
            }
        }
        return sites;
    }

    /**
     * Every old seeded City-state site with minX <= x <= maxX and minZ <= z <= maxZ: where City-state factions start,
     * with no city built there (cities are the titles' seats now).
     */
    public static List<Site> cityStateSitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
        List<Site> sites = new ArrayList<>();
        if (!s.citiesEnabled()) {
            return sites;
        }
        int size = s.cityCellSize();
        for (int cx = Math.floorDiv(minX, size); cx <= Math.floorDiv(maxX, size); cx++) {
            for (int cz = Math.floorDiv(minZ, size); cz <= Math.floorDiv(maxZ, size); cz++) {
                gridSite(seed, s, cx, cz)
                        .filter(site -> site.x() >= minX && site.x() <= maxX && site.z() >= minZ && site.z() <= maxZ)
                        .ifPresent(sites::add);
            }
        }
        return sites;
    }

    /** The city (City-state or capital) whose centre block lies in this chunk, if any. */
    public static Optional<Site> siteInChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int r = MAX_HALF_EXTENT;
        for (Site city : allSitesInBox(seed, s, minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            District centre = new District(city, city.centerCell(), city.centerCell());
            if (centre.x() >= minX && centre.x() <= minX + 15 && centre.z() >= minZ && centre.z() <= minZ + 15) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }

    /** A city's cells other than the one holding its town centre. */
    public static List<District> districts(Site city) {
        List<District> districts = new ArrayList<>();
        for (int cz = 0; cz < city.cells(); cz++) {
            for (int cx = 0; cx < city.cells(); cx++) {
                if (cx != city.centerCell() || cz != city.centerCell()) {
                    districts.add(new District(city, cx, cz));
                }
            }
        }
        return districts;
    }

    /** The district whose start block lies in this chunk, if any. */
    public static Optional<District> districtInChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int r = MAX_HALF_EXTENT;
        for (Site city : allSitesInBox(seed, s, minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            for (District d : districts(city)) {
                if (d.x() >= minX && d.x() <= minX + 15 && d.z() >= minZ && d.z() <= minZ + 15) {
                    return Optional.of(d);
                }
            }
        }
        return Optional.empty();
    }

    /** True if (x, z) lies within a city's footprint plus {@code margin} blocks. Site-level, so it ignores biome checks. */
    public static boolean nearCity(long seed, WorldSettings s, int x, int z, int margin) {
        int search = MAX_HALF_EXTENT + 1 + margin;
        for (Site city : allSitesInBox(seed, s, x - search, z - search, x + search, z + search)) {
            int r = city.halfExtent() + 1 + margin;
            if (Math.abs(city.x() - x) <= r && Math.abs(city.z() - z) <= r) {
                return true;
            }
        }
        return false;
    }
}
