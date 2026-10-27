package rafasconquest.core;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Where Capitals go, computed from the seed so positions are known before their chunks exist.
 * Storm wall: an evenly spaced ring with a seeded angle offset and seeded distance jitter.
 * Outer world: one seeded site per grid cell, kept only if it lies beyond R2.
 * A site whose city would touch ocean then moves to nearby land, which needs the world's biomes (see {@link Land}).
 */
public final class CapitalLayout {
    /** How far a Capital may move to reach land. */
    public static final int MAX_SHIFT = 1024;
    static final int SHIFT_STEP = 32;

    /** Whether a whole city centred on (x, z) would stand on allowed land. Needs the world's biomes. */
    @FunctionalInterface
    public interface Land {
        boolean cityFits(int x, int z);
    }

    @Nullable
    private static volatile Land land;
    private static volatile long landSeed;
    private static final Map<Long, Site> MOVED = new ConcurrentHashMap<>();

    private CapitalLayout() {
    }

    public record Site(int x, int z, boolean ring) {
        public double distanceFromSpawn() {
            return Math.sqrt((double) x * x + (double) z * z);
        }
    }

    public static List<Site> ringSites(long seed, WorldSettings s) {
        List<Site> sites = new ArrayList<>(s.ringCapitalCount());
        double offset = SeedHash.unit(SeedHash.hash(seed, SeedHash.SALT_RING, -1)) * Math.PI * 2;
        for (int i = 0; i < s.ringCapitalCount(); i++) {
            double angle = offset + i * Math.PI * 2 / s.ringCapitalCount();
            int distance = s.ringDistance() + SeedHash.intInRange(SeedHash.hash(seed, SeedHash.SALT_RING, i), -s.ringJitter(), s.ringJitter());
            sites.add(new Site((int) Math.round(Math.cos(angle) * distance), (int) Math.round(Math.sin(angle) * distance), true));
        }
        return sites;
    }

    public static Optional<Site> gridSite(long seed, WorldSettings s, int cellX, int cellZ) {
        int size = s.outerCellSize();
        int span = Math.max(1, size - 2 * s.outerCellMargin());
        long h = SeedHash.hash(seed, SeedHash.SALT_GRID, cellX, cellZ);
        int x = cellX * size + s.outerCellMargin() + SeedHash.intInRange(h, 0, span - 1);
        int z = cellZ * size + s.outerCellMargin() + SeedHash.intInRange(SeedHash.mix(h), 0, span - 1);
        Site site = new Site(x, z, false);
        return site.distanceFromSpawn() > s.warZoneOuterEdge() ? Optional.of(site) : Optional.empty();
    }

    /**
     * Every Capital site with minX <= x <= maxX and minZ <= z <= maxZ, after moving any whose city would touch ocean
     * to the nearest land ({@link #relocated}).
     */
    public static List<Site> sitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
        Land check = land;
        if (check == null || seed != landSeed) {
            return rawSitesInBox(seed, s, minX, minZ, maxX, maxZ);
        }
        List<Site> sites = new ArrayList<>();
        for (Site raw : rawSitesInBox(seed, s, minX - MAX_SHIFT, minZ - MAX_SHIFT, maxX + MAX_SHIFT, maxZ + MAX_SHIFT)) {
            Site site = relocated(seed, s, raw, check);
            if (inBox(site, minX, minZ, maxX, maxZ)) {
                sites.add(site);
            }
        }
        return sites;
    }

    /**
     * Capital sites as the seed places them, before any move to land. City-states keep their distance from these,
     * which keeps the two layouts from depending on each other.
     */
    public static List<Site> rawSitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
        List<Site> sites = new ArrayList<>();
        for (Site site : ringSites(seed, s)) {
            if (inBox(site, minX, minZ, maxX, maxZ)) {
                sites.add(site);
            }
        }
        int size = s.outerCellSize();
        for (int cx = Math.floorDiv(minX, size); cx <= Math.floorDiv(maxX, size); cx++) {
            for (int cz = Math.floorDiv(minZ, size); cz <= Math.floorDiv(maxZ, size); cz++) {
                gridSite(seed, s, cx, cz).filter(site -> inBox(site, minX, minZ, maxX, maxZ)).ifPresent(sites::add);
            }
        }
        return sites;
    }

    /** The Capital site whose block lies in this chunk, if any. Used by structure placement on worldgen threads. */
    public static Optional<Site> siteInChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        List<Site> sites = sitesInBox(seed, s, minX, minZ, minX + 15, minZ + 15);
        return sites.isEmpty() ? Optional.empty() : Optional.of(sites.get(0));
    }

    /**
     * The site a Capital's city actually takes. Where a whole city on the seeded site would touch ocean, it moves to the
     * nearest spot (searched every {@link #SHIFT_STEP} blocks, up to {@link #MAX_SHIFT} away) where it fits on land,
     * stays out of the eye, and keeps a city's width clear of City-states and other Capitals. If there is none, the
     * site stays put (and no city, so no Empire, forms there). Cached: the search runs once per site per world.
     */
    static Site relocated(long seed, WorldSettings s, Site site, Land check) {
        return MOVED.computeIfAbsent(((long) site.x() << 32) | (site.z() & 0xFFFFFFFFL), k -> findLand(seed, s, site, check));
    }

    private static Site findLand(long seed, WorldSettings s, Site site, Land check) {
        if (check.cityFits(site.x(), site.z())) {
            return site;
        }
        int clear = CityLayout.SIZE + CityLayout.CLEARANCE;
        int reach = MAX_SHIFT + clear;
        List<CityLayout.Site> cityStates = CityLayout.sitesInBox(seed, s, site.x() - reach, site.z() - reach, site.x() + reach, site.z() + reach);
        List<Site> capitals = rawSitesInBox(seed, s, site.x() - reach, site.z() - reach, site.x() + reach, site.z() + reach);
        List<int[]> offsets = new ArrayList<>();
        for (int dx = -MAX_SHIFT; dx <= MAX_SHIFT; dx += SHIFT_STEP) {
            for (int dz = -MAX_SHIFT; dz <= MAX_SHIFT; dz += SHIFT_STEP) {
                if ((dx != 0 || dz != 0) && (long) dx * dx + (long) dz * dz <= (long) MAX_SHIFT * MAX_SHIFT) {
                    offsets.add(new int[]{dx, dz});
                }
            }
        }
        offsets.sort(Comparator.<int[]>comparingLong(o -> (long) o[0] * o[0] + (long) o[1] * o[1])
                .thenComparingInt(o -> o[0]).thenComparingInt(o -> o[1]));
        for (int[] o : offsets) {
            int x = site.x() + o[0];
            int z = site.z() + o[1];
            if (Math.sqrt((double) x * x + (double) z * z) < s.eyeRadius() + CityLayout.HALF_EXTENT
                    || cityStates.stream().anyMatch(c -> Math.abs(c.x() - x) <= clear && Math.abs(c.z() - z) <= clear)
                    || capitals.stream().anyMatch(c -> !c.equals(site) && Math.abs(c.x() - x) <= clear && Math.abs(c.z() - z) <= clear)) {
                continue;
            }
            if (check.cityFits(x, z)) {
                return new Site(x, z, site.ring());
            }
        }
        return site;
    }

    /** Publishes the current world's land check (null when no world is loaded), which turns on moving Capitals. */
    public static void setLand(long seed, @Nullable Land check) {
        MOVED.clear();
        landSeed = seed;
        land = check;
    }

    private static boolean inBox(Site site, int minX, int minZ, int maxX, int maxZ) {
        return site.x() >= minX && site.x() <= maxX && site.z() >= minZ && site.z() <= maxZ;
    }
}
