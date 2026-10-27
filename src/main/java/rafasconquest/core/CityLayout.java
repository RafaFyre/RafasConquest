package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Where cities go and how each one is laid out.
 * <p>
 * A city is a planned square: a street grid with a 3-wide road every 32 blocks (including an edge road all round),
 * giving 15 × 15 city blocks of 29 × 29, each split into four 14 × 14 plots. The square is cut into 3 × 3 cells of 5 × 5
 * blocks; each cell is its own structure start (the town centre in the middle, a district in each of the other 8),
 * which keeps every piece inside the engine's 128-block reach from its start. Every cell builds its share of the same
 * grid, so cells meet seamlessly.
 * <p>
 * One seeded site per city grid cell; sites inside the eye or too close to a Capital are dropped.
 */
public final class CityLayout {
    public static final int ROAD_PERIOD = 32;
    public static final int ROAD_WIDTH = 3;
    /** Two plots per block side, with a 1-block gap between them. */
    public static final int PLOT_SIZE = (ROAD_PERIOD - ROAD_WIDTH - 1) / 2;
    public static final int BLOCKS_PER_CELL = 5;
    public static final int CELLS_PER_SIDE = 3;
    public static final int BLOCKS_PER_SIDE = BLOCKS_PER_CELL * CELLS_PER_SIDE;
    /** The block holding the town centre instead of plots. */
    public static final int CENTER_BLOCK = BLOCKS_PER_SIDE / 2;
    /** Side length of the whole city, edge roads included. */
    public static final int SIZE = BLOCKS_PER_SIDE * ROAD_PERIOD + ROAD_WIDTH;
    public static final int HALF_EXTENT = SIZE / 2;
    /** Distance between cell centres, and so between district starts. */
    public static final int DISTRICT_SPACING = BLOCKS_PER_CELL * ROAD_PERIOD;
    /** Extra gap kept between a city's edge and a Capital or Farm. */
    public static final int CLEARANCE = 64;
    /** The road that runs out through a gate on every side: the avenue beside the town centre. */
    public static final int GATE_ROAD = BLOCKS_PER_SIDE / 2;

    /**
     * One 32-long wall segment, in city-local coordinates. The wall stands on the edge road band and is laid as a
     * pinwheel: each side runs clockwise for 15 segments and its last one covers the corner, so 15 × 32 + 3 = 483
     * closes exactly. {@code quarterTurns} rotates the template (whose outer face is local z = 0) clockwise.
     */
    public record WallSegment(Side side, int index, int originX, int originZ, int quarterTurns, boolean gate) {
    }

    public enum Side {
        NORTH, EAST, SOUTH, WEST
    }

    private CityLayout() {
    }

    public record Site(int x, int z) {
        /** World x of the city's west edge (local 0). */
        public int originX() {
            return x - HALF_EXTENT;
        }

        /** World z of the city's north edge (local 0). */
        public int originZ() {
            return z - HALF_EXTENT;
        }
    }

    /**
     * One of the 3 × 3 cells. Cell (1, 1) is the town centre; the others are districts.
     */
    public record District(Site city, int cellX, int cellZ) {
        public int x() {
            return city.x() + (cellX - 1) * DISTRICT_SPACING;
        }

        public int z() {
            return city.z() + (cellZ - 1) * DISTRICT_SPACING;
        }
    }

    /** Road indices (0 .. BLOCKS_PER_SIDE) a cell builds along one axis. The last cell also builds the far edge road. */
    public static List<Integer> roadsInCell(int cell) {
        List<Integer> roads = new ArrayList<>();
        for (int k = cell * BLOCKS_PER_CELL; k < (cell + 1) * BLOCKS_PER_CELL; k++) {
            roads.add(k);
        }
        if (cell == CELLS_PER_SIDE - 1) {
            roads.add(BLOCKS_PER_SIDE);
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
        List<WallSegment> all = new ArrayList<>();
        for (Side side : Side.values()) {
            for (int j = 0; j < BLOCKS_PER_SIDE; j++) {
                all.add(wallSegment(side, j));
            }
        }
        return all;
    }

    /** The wall segments a cell builds, in clockwise order along each side. */
    public static List<WallSegment> wallSegments(int cellX, int cellZ) {
        List<WallSegment> mine = new ArrayList<>();
        for (WallSegment seg : wallSegments()) {
            int[] cell = wallCell(seg);
            if (cell[0] == cellX && cell[1] == cellZ) {
                mine.add(seg);
            }
        }
        return mine;
    }

    static WallSegment wallSegment(Side side, int j) {
        int far = SIZE - 1;
        int step = j * ROAD_PERIOD;
        return switch (side) {
            case NORTH -> new WallSegment(side, j, step, 0, 0, j == GATE_ROAD);
            case EAST -> new WallSegment(side, j, far, step, 1, j == GATE_ROAD);
            // South and west run back toward the origin, so road k is met by segment 15 - k.
            case SOUTH -> new WallSegment(side, j, far - step, far, 2, j == BLOCKS_PER_SIDE - GATE_ROAD);
            case WEST -> new WallSegment(side, j, 0, far - step, 3, j == BLOCKS_PER_SIDE - GATE_ROAD);
        };
    }

    private static int[] wallCell(WallSegment seg) {
        int along = seg.index() / BLOCKS_PER_CELL;
        int last = CELLS_PER_SIDE - 1;
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

    /** True if city-local (x, z) is on the wall: the outermost road band, all round. */
    public static boolean inWallBand(int localX, int localZ) {
        if (localX < 0 || localZ < 0 || localX >= SIZE || localZ >= SIZE) {
            return false;
        }
        int inner = SIZE - ROAD_WIDTH;
        return localX < ROAD_WIDTH || localZ < ROAD_WIDTH || localX >= inner || localZ >= inner;
    }

    /** The city whose wall band contains world (x, z), if any. */
    public static Optional<Site> wallAt(long seed, WorldSettings s, int x, int z) {
        for (Site city : sitesInBox(seed, s, x - HALF_EXTENT - 1, z - HALF_EXTENT - 1, x + HALF_EXTENT + 1, z + HALF_EXTENT + 1)) {
            if (inWallBand(x - city.originX(), z - city.originZ())) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }

    /** True if any block of the chunk lies on some city's wall. Cheap; safe on any thread. */
    public static boolean chunkTouchesWall(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        for (Site city : sitesInBox(seed, s, minX - HALF_EXTENT - 1, minZ - HALF_EXTENT - 1, minX + 15 + HALF_EXTENT + 1, minZ + 15 + HALF_EXTENT + 1)) {
            int lx0 = minX - city.originX();
            int lz0 = minZ - city.originZ();
            boolean overlapsCity = lx0 + 15 >= 0 && lz0 + 15 >= 0 && lx0 < SIZE && lz0 < SIZE;
            int inner = SIZE - ROAD_WIDTH;
            boolean insideInner = lx0 >= ROAD_WIDTH && lz0 >= ROAD_WIDTH && lx0 + 15 < inner && lz0 + 15 < inner;
            if (overlapsCity && !insideInner) {
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
        int clear = HALF_EXTENT + CLEARANCE;
        for (CapitalLayout.Site capital : CapitalLayout.sitesInBox(seed, s, x - clear, z - clear, x + clear, z + clear)) {
            if (Math.abs(capital.x() - x) <= clear && Math.abs(capital.z() - z) <= clear) {
                return Optional.empty();
            }
        }
        return Optional.of(new Site(x, z));
    }

    /** Every city site with minX <= x <= maxX and minZ <= z <= maxZ. */
    public static List<Site> sitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
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

    /** The city whose centre block lies in this chunk, if any. */
    public static Optional<Site> siteInChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        List<Site> sites = sitesInBox(seed, s, minX, minZ, minX + 15, minZ + 15);
        return sites.isEmpty() ? Optional.empty() : Optional.of(sites.get(0));
    }

    /** The 8 outer cells of a city. */
    public static List<District> districts(Site city) {
        List<District> districts = new ArrayList<>(8);
        for (int cz = 0; cz < CELLS_PER_SIDE; cz++) {
            for (int cx = 0; cx < CELLS_PER_SIDE; cx++) {
                if (cx != 1 || cz != 1) {
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
        int r = DISTRICT_SPACING;
        for (Site city : sitesInBox(seed, s, minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
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
        int r = HALF_EXTENT + 1 + margin;
        for (Site city : sitesInBox(seed, s, x - r, z - r, x + r, z + r)) {
            if (Math.abs(city.x() - x) <= r && Math.abs(city.z() - z) <= r) {
                return true;
            }
        }
        return false;
    }
}
