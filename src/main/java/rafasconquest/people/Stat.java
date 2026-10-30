package rafasconquest.people;

/**
 * A character's six skills. Five run 0 to 20; prowess, a body's own strength at arms, runs 0 to 45, where 21 and up is
 * the realm of the superhuman ({@link #superhumanTier}).
 */
public enum Stat {
    DIPLOMACY("Diplomacy", 20),
    MARTIAL("Martial", 20),
    STEWARDSHIP("Stewardship", 20),
    INTRIGUE("Intrigue", 20),
    LEARNING("Learning", 20),
    PROWESS("Prowess", 45);

    /** Where the superhuman begins (a Sword Saint), a Sword King, and a Sword God. */
    public static final int SUPERHUMAN = 21;
    public static final int SWORD_KING = 30;
    public static final int SWORD_GOD = 40;
    /** How many of each martial title the world has: seven Sword Gods, and three times as many at each title below. */
    public static final int MAX_SWORD_GODS = 7;
    public static final int MAX_SWORD_KINGS = 3 * MAX_SWORD_GODS;
    public static final int MAX_SWORD_SAINTS = 3 * MAX_SWORD_KINGS;

    /** The martial titles from the highest down: where each begins, and how many the world has. */
    public static final int[] TITLE_FLOORS = {SWORD_GOD, SWORD_KING, SUPERHUMAN};
    public static final int[] TITLE_LIMITS = {MAX_SWORD_GODS, MAX_SWORD_KINGS, MAX_SWORD_SAINTS};

    private final String title;
    private final int max;

    Stat(String title, int max) {
        this.title = title;
        this.max = max;
    }

    public String title() {
        return title;
    }

    public int max() {
        return max;
    }

    /** The title a prowess this high grants: "" for ordinary people, else Sword Saint, Sword King or Sword God. */
    public static String superhumanTier(int prowess) {
        return prowess >= SWORD_GOD ? "Sword God" : prowess >= SWORD_KING ? "Sword King" : prowess >= SUPERHUMAN ? "Sword Saint" : "";
    }

    /** What that title means. */
    public static String superhumanMeaning(int prowess) {
        return prowess >= SWORD_GOD ? "their sword's power is biblical"
                : prowess >= SWORD_KING ? "their sword possesses the might of a nation"
                : prowess >= SUPERHUMAN ? "they have achieved a miracle in their prowess" : "";
    }
}
