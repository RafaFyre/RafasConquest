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

    /** Where the superhuman begins (a Sword Saint), a Sword King, and a Sword God, of whom no more than seven live at once. */
    public static final int SUPERHUMAN = 21;
    public static final int SWORD_KING = 30;
    public static final int SWORD_GOD = 40;
    public static final int MAX_SWORD_GODS = 7;

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
