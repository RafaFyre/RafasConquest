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

    /**
     * The holder of every map pixel, as an index into {@code claims} (-1 for no one). Pixel (px, pz) covers the
     * {@code blocksPerPixel}-wide square starting at (minX + px × bpp, minZ + pz × bpp) and is sampled at its centre.
     * Pixels beyond {@code visibleRadius} of the world origin (the storm wall) are left at -1.
     */
    public static int[] rasterize(List<Claim> claims, int minX, int minZ, int blocksPerPixel, int width, int height, int visibleRadius) {
        List<OwnershipResolver.Candidate> empires = empires(claims);
        List<UUID> ids = claims.stream().map(Claim::factionId).toList();
        int[] owners = new int[width * height];
        long visible2 = (long) visibleRadius * visibleRadius;
        for (int pz = 0; pz < height; pz++) {
            int z = minZ + pz * blocksPerPixel + blocksPerPixel / 2;
            for (int px = 0; px < width; px++) {
                int x = minX + px * blocksPerPixel + blocksPerPixel / 2;
                int index = -1;
                if ((long) x * x + (long) z * z <= visible2) {
                    index = holder(x, z, claims, empires).map(ids::indexOf).orElse(-1);
                }
                owners[pz * width + px] = index;
            }
        }
        return owners;
    }
}
