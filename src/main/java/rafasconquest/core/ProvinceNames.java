package rafasconquest.core;

/**
 * A province's name, from its centre and the world seed, in the same style as the factions' names and always the
 * same for the same province.
 */
public final class ProvinceNames {
    /** Kept apart from the factions' names, so a province isn't named after the faction founded there. */
    private static final long SALT = 0x50524F56494E4345L;

    private ProvinceNames() {
    }

    public static String name(long seed, int centreX, int centreZ) {
        return OriginRoller.name(seed ^ SALT, centreX, centreZ);
    }
}
