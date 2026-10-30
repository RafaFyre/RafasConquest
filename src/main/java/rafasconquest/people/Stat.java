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

    /** Where the superhuman begins, and the peak no more than {@link #PEAK_HOLDERS} may stand at at once. */
    public static final int SUPERHUMAN = 21;
    public static final int PEAK_HOLDERS = 3;

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

    /** What a prowess this high is called: "" for ordinary people, else Champion, Legend or Mythic. */
    public static String superhumanTier(int prowess) {
        return prowess >= 40 ? "Mythic" : prowess >= 30 ? "Legend" : prowess >= SUPERHUMAN ? "Champion" : "";
    }
}
