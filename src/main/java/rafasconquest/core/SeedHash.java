package rafasconquest.core;

/**
 * Deterministic hashing of the world seed and positions. Everything that "rolls" in worldgen goes through here,
 * so the same seed always produces the same factions and POIs, whatever order chunks load in.
 */
public final class SeedHash {
    public static final long SALT_RING = 0x52494E47L;
    public static final long SALT_GRID = 0x47524944L;
    public static final long SALT_RADIUS = 0x52414449L;
    public static final long SALT_STRENGTH = 0x53545245L;
    public static final long SALT_NAME = 0x4E414D45L;
    public static final long SALT_FARM = 0x4641524DL;
    public static final long SALT_TIE = 0x54494542L;
    public static final long SALT_ID = 0x49444944L;

    private static final long GOLDEN = 0x9E3779B97F4A7C15L;

    private SeedHash() {
    }

    /** SplitMix64 finaliser. */
    public static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }

    public static long hash(long seed, long... parts) {
        long h = mix(seed + GOLDEN);
        for (long part : parts) {
            h = mix(h ^ mix(part + GOLDEN));
        }
        return h;
    }

    /** Uniform double in [0, 1). */
    public static double unit(long hash) {
        return (hash >>> 11) * 0x1.0p-53;
    }

    /** Uniform int in [min, max], both inclusive. */
    public static int intInRange(long hash, int min, int max) {
        if (max <= min) {
            return min;
        }
        long span = (long) max - min + 1;
        return (int) (min + (long) (unit(hash) * span));
    }
}
