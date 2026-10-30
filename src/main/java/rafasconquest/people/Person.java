package rafasconquest.people;

import java.util.List;

/**
 * A character: who they are (name, sex, age, culture, faith, race: a {@link Race} ordinal), their six skills ({@link Stat}, in order) and personality
 * ({@link Personality}), the hold they keep, their liege (a character's index, -1 for none) and the nation they serve
 * (an index into the map's nations, -1 for none), and whether they are away from their hold (as a sanctum's Sword God
 * sometimes is).
 */
public record Person(String given, String family, boolean female, int age, int culture, int faith, int race, int[] stats,
                     List<Personality> traits, Hold hold, int liege, int nation, boolean away) {
    /** What a character keeps: a capital, a city, a town or a castle, in a province, standing at (x, z). */
    public record Hold(Kind kind, int province, int x, int z) {
    }

    public enum Kind {
        CAPITAL, CITY, TOWN, CASTLE
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
