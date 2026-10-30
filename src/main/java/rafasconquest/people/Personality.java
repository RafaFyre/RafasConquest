package rafasconquest.people;

/**
 * A character's personality traits, in opposed pairs (no one is both of a pair). Each bends a few skills and, later,
 * what the character chooses to do. Two characters sharing a trait think better of each other; opposed traits, worse.
 */
public enum Personality {
    BRAVE("Brave", "Fear has little hold on them; they are first into the fray.", Stat.PROWESS, 2, Stat.MARTIAL, 1),
    CRAVEN("Craven", "They flinch from danger, and would sooner scheme than fight.", Stat.PROWESS, -2, Stat.INTRIGUE, 1),
    CALM("Calm", "Slow to anger and hard to rattle, they weigh things coolly.", Stat.DIPLOMACY, 1, Stat.INTRIGUE, 1),
    WRATHFUL("Wrathful", "Their temper is quick, and their vengeance quicker.", Stat.MARTIAL, 2, Stat.DIPLOMACY, -1),
    CONTENT("Content", "Satisfied with their lot, they seek no more than they have.", Stat.LEARNING, 1, Stat.INTRIGUE, -1),
    AMBITIOUS("Ambitious", "They want more: more land, more power, a higher seat.", Stat.DIPLOMACY, 1, Stat.MARTIAL, 1),
    DILIGENT("Diligent", "Hard-working and thorough, they leave nothing half done.", Stat.STEWARDSHIP, 2, Stat.LEARNING, 1),
    LAZY("Lazy", "They put off until tomorrow what might be done today, or never.", Stat.STEWARDSHIP, -1, Stat.LEARNING, -1),
    FORGIVING("Forgiving", "They let old wrongs go, and are loved for it.", Stat.DIPLOMACY, 2, Stat.INTRIGUE, -2),
    VENGEFUL("Vengeful", "They forget no slight, and repay every one.", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -2),
    GENEROUS("Generous", "Open-handed with gifts and favours, if not with good sense.", Stat.DIPLOMACY, 2, Stat.STEWARDSHIP, -1),
    GREEDY("Greedy", "Every coin counts, and they mean to have them all.", Stat.STEWARDSHIP, 2, Stat.DIPLOMACY, -1),
    GREGARIOUS("Gregarious", "Never happier than in company, the life of every hall.", Stat.DIPLOMACY, 2, Stat.LEARNING, -1),
    SHY("Shy", "They keep to themselves, and to their books.", Stat.LEARNING, 1, Stat.DIPLOMACY, -2),
    HONEST("Honest", "Their word is good, and they cannot lie well.", Stat.DIPLOMACY, 1, Stat.INTRIGUE, -2),
    DECEITFUL("Deceitful", "A liar and a plotter, with a smile for everyone.", Stat.INTRIGUE, 3, Stat.DIPLOMACY, -1),
    HUMBLE("Humble", "They think little of their own greatness.", Stat.LEARNING, 1, Stat.DIPLOMACY, 0),
    ARROGANT("Arrogant", "They think much of their own greatness, and say so.", Stat.MARTIAL, 1, Stat.DIPLOMACY, -1),
    JUST("Just", "They judge fairly and keep to the law.", Stat.STEWARDSHIP, 1, Stat.LEARNING, 1),
    ARBITRARY("Arbitrary", "Their judgements follow their moods, not the law.", Stat.INTRIGUE, 1, Stat.STEWARDSHIP, -1),
    PATIENT("Patient", "They wait for the right moment, however long it takes.", Stat.LEARNING, 2, Stat.MARTIAL, 0),
    IMPATIENT("Impatient", "They want it done now, and done their way.", Stat.MARTIAL, 1, Stat.LEARNING, -1),
    TRUSTING("Trusting", "They believe the best of people, sometimes wrongly.", Stat.DIPLOMACY, 1, Stat.INTRIGUE, -2),
    PARANOID("Paranoid", "They see daggers in every shadow, and are sometimes right.", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -1),
    ZEALOUS("Zealous", "Their faith burns bright, and they would spread it.", Stat.LEARNING, 1, Stat.MARTIAL, 1),
    CYNICAL("Cynical", "They put little stock in gods, or in those who serve them.", Stat.INTRIGUE, 1, Stat.LEARNING, 1),
    COMPASSIONATE("Compassionate", "They feel the suffering of others as their own.", Stat.DIPLOMACY, 2, Stat.INTRIGUE, -2),
    CALLOUS("Callous", "The pain of others moves them not at all.", Stat.INTRIGUE, 2, Stat.DIPLOMACY, -2);

    private final String title;
    private final String meaning;
    private final Stat first;
    private final int firstBy;
    private final Stat second;
    private final int secondBy;

    Personality(String title, String meaning, Stat first, int firstBy, Stat second, int secondBy) {
        this.title = title;
        this.meaning = meaning;
        this.first = first;
        this.firstBy = firstBy;
        this.second = second;
        this.secondBy = secondBy;
    }

    public String title() {
        return title;
    }

    /** What the trait says of someone, and what it does to their skills: "...  +2 Prowess, +1 Martial". */
    public String description() {
        StringBuilder effects = new StringBuilder();
        for (Stat s : new Stat[]{first, second}) {
            int by = modifies(s);
            if (by != 0 && effects.indexOf(s.title()) < 0) {
                effects.append(effects.length() == 0 ? "" : ", ").append(by > 0 ? "+" : "").append(by).append(' ').append(s.title());
            }
        }
        return effects.length() == 0 ? meaning : meaning + " " + effects + ".";
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
