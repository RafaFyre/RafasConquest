package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Where cities go. A city is a 3 × 3 grid of 160-block cells: a town centre in the middle cell and a district in each
 * of the 8 cells around it, so it spans 480 blocks (3× a vanilla village's 160). Each cell is its own structure start,
 * which keeps every piece inside the engine's 128-block reach from its start. One seeded site per city grid cell;
 * sites inside the eye or too close to a Capital are dropped.
 */
public final class CityLayout {
    public static final int DISTRICT_SPACING = 160;
    public static final int HALF_EXTENT = DISTRICT_SPACING * 3 / 2;
    /** Jigsaw reach from each start; a little under half a cell, so neighbouring cells never overlap. */
    public static final int JIGSAW_REACH = DISTRICT_SPACING / 2 - 4;
    /** Extra gap kept between a city's edge and a Capital or Farm. */
    public static final int CLEARANCE = 64;

    private static final int[][] DISTRICT_OFFSETS = {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {0, 1}, {1, 1}};

    private CityLayout() {
    }

    public record Site(int x, int z) {
    }

    public record District(Site city, int x, int z) {
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

    public static List<District> districts(Site city) {
        List<District> districts = new ArrayList<>(DISTRICT_OFFSETS.length);
        for (int[] o : DISTRICT_OFFSETS) {
            districts.add(new District(city, city.x() + o[0] * DISTRICT_SPACING, city.z() + o[1] * DISTRICT_SPACING));
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
        int r = HALF_EXTENT + margin;
        for (Site city : sitesInBox(seed, s, x - r, z - r, x + r, z + r)) {
            if (Math.abs(city.x() - x) <= r && Math.abs(city.z() - z) <= r) {
                return true;
            }
        }
        return false;
    }
}
