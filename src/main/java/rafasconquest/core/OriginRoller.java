package rafasconquest.core;

import java.util.UUID;

/** Rolls a Capital's id, name, radius and strength from the world seed and its position. Never random. */
public final class OriginRoller {
    private static final String[] PREFIXES = {
            "Al", "Bar", "Cal", "Dor", "El", "Fen", "Gal", "Har", "Ist", "Jor", "Kar", "Lor", "Mar", "Nor", "Os",
            "Pel", "Quar", "Ros", "Sar", "Tal", "Ul", "Val", "Wes", "Xan", "Yr", "Zan", "Ash", "Bel", "Cor", "Dun",
            "Ev", "Gor", "Hel", "Kel", "Mor", "Thal", "Vor", "Ar", "Bran", "Drak"};
    private static final String[] MIDDLES = {"", "", "", "a", "e", "i", "o", "an", "en", "or", "ar", "il", "un"};
    private static final String[] SUFFIXES = {
            "dor", "mark", "heim", "gard", "mere", "ia", "oria", "wyn", "stead", "holm", "rath", "vale", "fell",
            "moor", "crest", "haven", "ford", "burg", "thar", "keep", "ros", "ven", "dun", "wick", "land", "rin",
            "goth", "ster", "ion", "mont"};

    private OriginRoller() {
    }

    public record Roll(UUID id, String name, int radiusSteps, int strength) {
    }

    public static Roll roll(long seed, int x, int z, WorldSettings s) {
        DistanceScaling.Bounds b = DistanceScaling.boundsAt(Math.sqrt((double) x * x + (double) z * z), s);
        int radius = SeedHash.intInRange(SeedHash.hash(seed, SeedHash.SALT_RADIUS, x, z), b.radiusMin(), b.radiusMax());
        int strength = SeedHash.intInRange(SeedHash.hash(seed, SeedHash.SALT_STRENGTH, x, z), b.strengthMin(), b.strengthMax());
        radius = Math.max(1, Math.min(WorldSettings.MAX_RADIUS_STEPS, radius));
        strength = Math.max(0, Math.min(WorldSettings.MAX_STRENGTH, strength));
        UUID id = new UUID(SeedHash.hash(seed, SeedHash.SALT_ID, x, z, 0), SeedHash.hash(seed, SeedHash.SALT_ID, x, z, 1));
        return new Roll(id, name(seed, x, z), radius, strength);
    }

    static String name(long seed, int x, int z) {
        long h = SeedHash.hash(seed, SeedHash.SALT_NAME, x, z);
        String prefix = PREFIXES[(int) Long.remainderUnsigned(h, PREFIXES.length)];
        String middle = MIDDLES[(int) Long.remainderUnsigned(h >>> 16, MIDDLES.length)];
        String suffix = SUFFIXES[(int) Long.remainderUnsigned(h >>> 32, SUFFIXES.length)];
        return prefix + middle + suffix;
    }
}
