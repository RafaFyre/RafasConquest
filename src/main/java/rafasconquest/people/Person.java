package rafasconquest.people;

import java.util.List;

/**
 * A character: who they are (name, sex, age, culture, faith, race: a {@link Race} ordinal), their six skills ({@link Stat}, in order) and personality
 * ({@link Personality}), the hold they keep, their liege (a character's index, -1 for none) and the nation they serve
 * (an index into the map's nations, -1 for none), and whether they are away from their hold (as a sanctum's Sword God
 * sometimes is), and whom they are married to (a character's index, -1 for none; one who has died is their late spouse).
 */
public record Person(String given, String family, boolean female, int age, int culture, int faith, int race, int[] stats,
                     List<Personality> traits, Hold hold, int liege, int nation, boolean away, int spouse) {
    /** One not married. */
    public Person(String given, String family, boolean female, int age, int culture, int faith, int race, int[] stats,
                  List<Personality> traits, Hold hold, int liege, int nation, boolean away) {
        this(given, family, female, age, culture, faith, race, stats, traits, hold, liege, nation, away, -1);
    }

    /** What a character keeps: a capital, a city, a town or a castle, in a province, standing at (x, z). */
    public record Hold(Kind kind, int province, int x, int z) {
    }

    public enum Kind {
        CAPITAL, CITY, TOWN, CASTLE,
        /** The lord of a duchy's or a nation's capital city, sworn to its ruler, who holds the city above them. */
        LORD,
        /** One who lives at a sword sanctum, trained there, holding nothing: its Sword Kings and Saints. */
        RETAINER,
        /** A title holder's child or sibling, living at their hold, in line for their titles (children first). */
        CHILD, SIBLING,
        /** One married to a holder or an heir, living with them at their hold; not in anyone's line. */
        SPOUSE,
        /** One put out of their hold by a conqueror, living on where it was, holding nothing. */
        EXILE,
        /** One who has died, remembered where they last lived; they hold nothing and stand nowhere. */
        DEAD;

        /** Whether this is one in line for a holder's titles rather than one holding anything. */
        public boolean heir() {
            return this == CHILD || this == SIBLING;
        }

        /** Whether this is one of a holder's household (their heirs and spouses), not one sworn to them. */
        public boolean household() {
            return heir() || this == SPOUSE;
        }
    }

    /** Whether they are married now (not widowed): their spouse lives. Pass the realm's people. */
    public boolean married(List<Person> people) {
        return spouse >= 0 && spouse < people.size() && people.get(spouse).hold().kind() != Kind.DEAD;
    }

    /** A noble: one who holds a title, or is in a title holder's line of succession. */
    public boolean noble() {
        return switch (hold.kind()) {
            case CAPITAL, CITY, TOWN, CASTLE, CHILD, SIBLING -> true;
            case LORD, RETAINER, SPOUSE, EXILE, DEAD -> false;
        };
    }

    public int stat(Stat stat) {
        return stats[stat.ordinal()];
    }

    public String name() {
        return given + " " + family;
    }

    public boolean has(Personality trait) {
        return traits.contains(trait);
    }
}
