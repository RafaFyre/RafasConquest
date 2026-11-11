package rafasconquest.core;

/**
 * Which of RafasConquest's optional systems a world has, chosen when it was made and set when its server starts, before
 * its map is drawn: the martial titles (Sword Gods, Sword Kings, Sword Saints, and the sword sanctums), the mantra
 * schools (an offensive magic system of ten schools, each with a dojo), and the ages its nations research through.
 */
public final class WorldFeatures {
    private static volatile boolean martialTitles = true;
    private static volatile boolean mantraSchools = true;
    private static volatile Age.Range ages = Age.Range.DEFAULT;

    private WorldFeatures() {
    }

    public static boolean martialTitles() {
        return martialTitles;
    }

    public static boolean mantraSchools() {
        return mantraSchools;
    }

    public static Age.Range ages() {
        return ages;
    }

    public static void set(boolean martial, boolean mantra, Age.Range range) {
        martialTitles = martial;
        mantraSchools = mantra;
        ages = range;
    }
}
