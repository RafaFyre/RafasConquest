package rafasconquest.core;

import java.util.HashMap;
import java.util.Map;

/**
 * How much of the {@link Swell} each place on the sea gets (0 flat – 1 open sea), as Physics Mod works out its ocean's
 * "waviness": each water column's calm is {@link #REACH} on land (or under a pier) and 33 less its depth in water; calm
 * then spreads out from where it is, one less per block (a distance on the grid, x plus z); the strength is
 * 255 - (calm + 1) × 8, out of 255. So waves build over the first 32 blocks out from a beach, and need water
 * {@link #DEEP} deep for their most (about 0.94; half at 17 deep). Server (ships) and client (the water drawn) work it
 * out the same way from the blocks they see.
 */
public final class SeaCalm {
    public static final int REACH = 33;
    public static final int DEEP = 32;
    /** Remembered columns are looked at again after this many ticks, so a new pier or a filled bay shows up. */
    private static final long STALE = 600;

    /** What {@link Depths} gives for a column it can't see yet (not loaded): taken as open sea for now, never remembered. */
    public static final int UNKNOWN = -1;

    /**
     * The water under the still surface at a column: 0 where it isn't sea (land, a pier), else how deep, up to
     * {@link #DEEP}; {@link #UNKNOWN} where it can't be seen yet.
     */
    @FunctionalInterface
    public interface Depths {
        int depth(int x, int z);
    }

    private final Depths depths;
    private final Map<Long, byte[]> chunks = new HashMap<>();
    private final Map<Long, Long> made = new HashMap<>();
    private final Map<Long, double[]> strengths = new HashMap<>(); // column -> {strength, when}
    private long now;
    private long swept;
    /** Some column read since this was last cleared couldn't be seen, so what was worked out from it isn't remembered. */
    private boolean guessed;

    public SeaCalm(Depths depths) {
        this.depths = depths;
    }

    /** Sets the game time, for when remembered columns go stale; now and then forgets the stale ones. */
    public SeaCalm at(long time) {
        now = time;
        if (Math.abs(now - swept) > STALE) {
            swept = now;
            made.entrySet().removeIf(e -> stale(e.getValue()) && chunks.remove(e.getKey()) != null);
            strengths.values().removeIf(s -> stale((long) s[1]));
        }
        return this;
    }

    private boolean stale(long when) {
        return now - when > STALE || when > now;
    }

    /** The wave strength here, blended between the four nearest columns' middles (as the shader's texture does). */
    public double strength(double x, double z) {
        double fx = x - 0.5;
        double fz = z - 0.5;
        int ix = (int) Math.floor(fx);
        int iz = (int) Math.floor(fz);
        double tx = fx - ix;
        double tz = fz - iz;
        double a = column(ix, iz) * (1 - tx) + column(ix + 1, iz) * tx;
        double b = column(ix, iz + 1) * (1 - tx) + column(ix + 1, iz + 1) * tx;
        return a * (1 - tz) + b * tz;
    }

    /** One column's wave strength: the calm reaching it from anywhere within {@link #REACH}. */
    public double column(int x, int z) {
        long key = key(x, z);
        double[] known = strengths.get(key);
        if (known != null && !stale((long) known[1])) {
            return known[0];
        }
        if (depths.depth(x, z) == UNKNOWN) {
            return strength(REACH - DEEP); // not loaded here: open sea, without sweeping thousands of unseen columns
        }
        guessed = false;
        int most = 0;
        for (int dx = -REACH; dx <= REACH; dx++) {
            int left = REACH - Math.abs(dx);
            for (int dz = -left; dz <= left; dz++) {
                most = Math.max(most, own(x + dx, z + dz) - Math.abs(dx) - Math.abs(dz));
            }
        }
        double strength = strength(most);
        if (!guessed) {
            strengths.put(key, new double[] {strength, now});
        }
        return strength;
    }

    /**
     * Every column's wave strength over a square (x0, z0, size), as bytes (0–255) row by row along x: the whole square at
     * once, by sweeping calm across it, for the water shader's texture.
     */
    public byte[] square(int x0, int z0, int size) {
        int n = size + 2 * REACH;
        int[] calm = new int[n * n];
        for (int z = 0; z < n; z++) {
            for (int x = 0; x < n; x++) {
                calm[z * n + x] = own(x0 - REACH + x, z0 - REACH + z);
            }
        }
        for (int z = 0; z < n; z++) { // spread along x, both ways, then along z: a grid distance (x plus z)
            for (int x = 1; x < n; x++) {
                calm[z * n + x] = Math.max(calm[z * n + x], calm[z * n + x - 1] - 1);
            }
            for (int x = n - 2; x >= 0; x--) {
                calm[z * n + x] = Math.max(calm[z * n + x], calm[z * n + x + 1] - 1);
            }
        }
        for (int x = 0; x < n; x++) {
            for (int z = 1; z < n; z++) {
                calm[z * n + x] = Math.max(calm[z * n + x], calm[(z - 1) * n + x] - 1);
            }
            for (int z = n - 2; z >= 0; z--) {
                calm[z * n + x] = Math.max(calm[z * n + x], calm[(z + 1) * n + x] - 1);
            }
        }
        byte[] out = new byte[size * size];
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                out[z * size + x] = (byte) Math.round(255 * strength(calm[(z + REACH) * n + x + REACH]));
            }
        }
        return out;
    }

    /** One key per (x, z), times an odd number so it stays one each but spreads: a Long hashes as x ^ z, which collides. */
    private static long key(int x, int z) {
        return ((long) x << 32 | (z & 0xffffffffL)) * 0x9E3779B97F4A7C15L;
    }

    /** Wave strength from a column's calm: Physics Mod's 255 - (calm + 1) × 8, out of 255. */
    private static double strength(int calm) {
        return Math.max(0, 255 - (calm + 1) * 8) / 255.0;
    }

    /** A column's own calm, before it spreads: {@link #REACH} on land, 33 less its depth in water (down to 1). */
    private int own(int x, int z) {
        long key = key(x >> 4, z >> 4);
        byte[] chunk = chunks.get(key);
        Long when = made.get(key);
        if (chunk == null || when == null || stale(when)) {
            if (depths.depth(x & ~15, z & ~15) == UNKNOWN) { // a whole chunk is seen or not
                guessed = true;
                return REACH - DEEP;
            }
            chunk = new byte[256];
            boolean seen = true;
            for (int i = 0; i < 256; i++) {
                int depth = depths.depth((x & ~15) + (i & 15), (z & ~15) + (i >> 4));
                seen &= depth != UNKNOWN;
                depth = depth == UNKNOWN ? DEEP : Math.min(DEEP, depth);
                chunk[i] = (byte) (depth == 0 ? REACH : REACH - depth);
            }
            if (seen) {
                chunks.put(key, chunk);
                made.put(key, now);
            } else {
                guessed = true; // looked at again next time, once it can be seen
            }
        }
        return chunk[(z & 15) << 4 | (x & 15)];
    }
}
