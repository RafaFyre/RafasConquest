package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Where Capitals go, computed from the seed alone so positions are known before their chunks exist.
 * Storm wall: an evenly spaced ring with a seeded angle offset and seeded distance jitter.
 * Outer world: one seeded site per grid cell, kept only if it lies beyond R2.
 */
public final class CapitalLayout {
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

    /** Every Capital site with minX <= x <= maxX and minZ <= z <= maxZ. */
    public static List<Site> sitesInBox(long seed, WorldSettings s, int minX, int minZ, int maxX, int maxZ) {
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

    private static boolean inBox(Site site, int minX, int minZ, int maxX, int maxZ) {
        return site.x() >= minX && site.x() <= maxX && site.z() >= minZ && site.z() <= maxZ;
    }
}
