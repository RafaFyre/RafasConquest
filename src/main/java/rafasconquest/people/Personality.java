package rafasconquest.people;

/**
 * A character's personality traits, in opposed pairs (no one is both of a pair). Each bends a few skills and, later,
 * what the character chooses to do. Two characters sharing a trait think better of each other; opposed traits, worse.
 */
public enum Personality {
    BRAVE("Brave", Stat.PROWESS, 2, Stat.MARTIAL, 1),
    CRAVEN("Craven", Stat.PROWESS, -2, Stat.INTRIGUE, 1),
    CALM("Calm", Stat.DIPLOMACY, 1, Stat.INTRIGUE, 1),
    WRATHFUL("Wrathful", Stat.MARTIAL, 2, Stat.DIPLOMACY, -1),
    CONTENT("Content", Stat.LEARNING, 1, Stat.INTRIGUE, -1),
    AMBITIOUS("Ambitious", Stat.DIPLOMACY, 1, Stat.MARTIAL, 1),
    DILIGENT("Diligent", Stat.STEWARDSHIP, 2, Stat.LEARNING, 1),
    LAZY("Lazy", Stat.STEWARDSHIP, -1, Stat.LEARNING, -1),
    FORGIVING("Forgiving", Stat.DIPLOMACY, 2, Stat.INTRIGUE, -2),
    VENGEFUL("Vengeful", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -2),
    GENEROUS("Generous", Stat.DIPLOMACY, 2, Stat.STEWARDSHIP, -1),
    GREEDY("Greedy", Stat.STEWARDSHIP, 2, Stat.DIPLOMACY, -1),
    GREGARIOUS("Gregarious", Stat.DIPLOMACY, 2, Stat.LEARNING, -1),
    SHY("Shy", Stat.LEARNING, 1, Stat.DIPLOMACY, -2),
    HONEST("Honest", Stat.DIPLOMACY, 1, Stat.INTRIGUE, -2),
    DECEITFUL("Deceitful", Stat.INTRIGUE, 3, Stat.DIPLOMACY, -1),
    HUMBLE("Humble", Stat.LEARNING, 1, Stat.DIPLOMACY, 0),
    ARROGANT("Arrogant", Stat.MARTIAL, 1, Stat.DIPLOMACY, -1),
    JUST("Just", Stat.STEWARDSHIP, 1, Stat.LEARNING, 1),
    ARBITRARY("Arbitrary", Stat.INTRIGUE, 1, Stat.STEWARDSHIP, -1),
    PATIENT("Patient", Stat.LEARNING, 2, Stat.MARTIAL, 0),
    IMPATIENT("Impatient", Stat.MARTIAL, 1, Stat.LEARNING, -1),
    TRUSTING("Trusting", Stat.DIPLOMACY, 1, Stat.INTRIGUE, -2),
    PARANOID("Paranoid", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -1),
    ZEALOUS("Zealous", Stat.LEARNING, 1, Stat.MARTIAL, 1),
    CYNICAL("Cynical", Stat.INTRIGUE, 1, Stat.LEARNING, 1),
    COMPASSIONATE("Compassionate", Stat.DIPLOMACY, 2, Stat.INTRIGUE, -2),
    CALLOUS("Callous", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -2);

    private final String title;
    private final Stat first;
    private final int firstBy;
    private final Stat second;
    private final int secondBy;

    Personality(String title, Stat first, int firstBy, Stat second, int secondBy) {
        this.title = title;
        this.first = first;
        this.firstBy = firstBy;
        this.second = second;
        this.secondBy = secondBy;
    }

    public String title() {
        return title;
    }

    /** The other of its pair: Brave's is Craven. */
    public Personality opposite() {
        Personality[] all = values();
        return all[ordinal() ^ 1];
    }

    /** How much it changes a skill. */
    public int modifies(Stat stat) {
        return (stat == first ? firstBy : 0) + (stat == second ? secondBy : 0);
    }
}
