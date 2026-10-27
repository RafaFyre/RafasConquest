package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Who holds each point of the map. Empires hold what {@link OwnershipResolver} gives them (the same rule that decides
 * who owns a Farm); a City-state holds a small circle around its city, which it keeps even inside an Empire's radius.
 * Pure, so it can be tested and drawn without the game.
 */
public final class TerritoryMap {
    /** A City-state's territory: its city (about 250 blocks out from the centre) and a margin. */
    public static final int CITY_STATE_RADIUS = 400;

    private TerritoryMap() {
    }

    /** One faction's claim. City-states ignore radiusSteps, strength and tie. */
    public record Claim(UUID factionId, int x, int z, boolean cityState, int radiusSteps, int strength, long tie) {
    }

    /** The holder of (x, z), if anyone holds it. */
    public static Optional<UUID> holder(int x, int z, List<Claim> claims, List<OwnershipResolver.Candidate> empires) {
        long best = Long.MAX_VALUE;
        UUID city = null;
        long r2 = (long) CITY_STATE_RADIUS * CITY_STATE_RADIUS;
        for (Claim c : claims) {
            if (c.cityState()) {
                long dx = (long) x - c.x();
                long dz = (long) z - c.z();
                long d2 = dx * dx + dz * dz;
                if (d2 <= r2 && d2 < best) {
                    best = d2;
                    city = c.factionId();
                }
            }
        }
        return city != null ? Optional.of(city) : OwnershipResolver.resolve(x, z, empires);
    }

    public static List<OwnershipResolver.Candidate> empires(List<Claim> claims) {
        List<OwnershipResolver.Candidate> empires = new ArrayList<>();
        for (Claim c : claims) {
            if (!c.cityState()) {
                empires.add(new OwnershipResolver.Candidate(c.factionId(), c.x(), c.z(), c.radiusSteps(), c.strength(), c.tie()));
            }
        }
        return empires;
    }
}
