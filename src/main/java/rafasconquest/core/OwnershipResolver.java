package rafasconquest.core;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

/**
 * Picks a POI's owner among the Empires whose radius covers it: highest strength, then the nearest Capital,
 * then the lower position hash. No covering Empire means the POI is unowned.
 */
public final class OwnershipResolver {
    private OwnershipResolver() {
    }

    /**
     * @param tieHash seeded hash of the Capital's position, the last tie-break
     */
    public record Candidate(UUID factionId, int originX, int originZ, int radiusSteps, int strength, long tieHash) {
        long distanceSq(int x, int z) {
            long dx = (long) x - originX;
            long dz = (long) z - originZ;
            return dx * dx + dz * dz;
        }

        boolean covers(int x, int z) {
            long r = (long) radiusSteps * WorldSettings.RADIUS_STEP_BLOCKS;
            return distanceSq(x, z) <= r * r;
        }
    }

    public static Optional<UUID> resolve(int x, int z, Iterable<Candidate> empires) {
        Comparator<Candidate> better = Comparator.comparingInt(Candidate::strength).reversed()
                .thenComparingLong(c -> c.distanceSq(x, z))
                .thenComparingLong(Candidate::tieHash);
        Candidate best = null;
        for (Candidate c : empires) {
            if (c.covers(x, z) && (best == null || better.compare(c, best) < 0)) {
                best = c;
            }
        }
        return Optional.ofNullable(best).map(Candidate::factionId);
    }
}
