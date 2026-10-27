package rafasconquest.core;

/**
 * Random-spread placement for Farms: one candidate chunk per spacing × spacing chunk cell, offset inside the cell by
 * a seeded amount below (spacing - separation). Same rule as vanilla random_spread, driven by the frozen settings.
 * Candidates inside a city's footprint are dropped. Whether a remaining candidate actually becomes a Farm is decided
 * by the structure's biome check.
 */
public final class FarmLayout {
    private FarmLayout() {
    }

    public record ChunkCoord(int x, int z) {
    }

    public static ChunkCoord candidateChunk(long seed, WorldSettings s, int cellX, int cellZ) {
        int spacing = s.farmSpacingChunks();
        int range = Math.max(1, spacing - s.farmSeparationChunks());
        long h = SeedHash.hash(seed, SeedHash.SALT_FARM, cellX, cellZ);
        int offsetX = (int) Long.remainderUnsigned(h, range);
        int offsetZ = (int) Long.remainderUnsigned(SeedHash.mix(h), range);
        return new ChunkCoord(cellX * spacing + offsetX, cellZ * spacing + offsetZ);
    }

    public static boolean isCandidateChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        int spacing = s.farmSpacingChunks();
        ChunkCoord candidate = candidateChunk(seed, s, Math.floorDiv(chunkX, spacing), Math.floorDiv(chunkZ, spacing));
        return candidate.x() == chunkX && candidate.z() == chunkZ;
    }

    /** A candidate that isn't inside a city. Placement and precompute both use this. */
    public static boolean isFarmChunk(long seed, WorldSettings s, int chunkX, int chunkZ) {
        return isCandidateChunk(seed, s, chunkX, chunkZ)
                && !CityLayout.nearCity(seed, s, (chunkX << 4) + 7, (chunkZ << 4) + 7, CityLayout.CLEARANCE);
    }
}
