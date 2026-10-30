package rafasconquest.people;

import java.util.Random;

/**
 * A faith: one of a world's 3 to 5 broad faiths, each held across great stretches of land, or one of its 4 small cults,
 * each with a home where it began. A character's faith is their own: most share their nation's, not all.
 */
public record Faith(int id, String name, boolean cult) {
    private static final String[] STEMS = {
            "Ael", "Bor", "Cael", "Dar", "Esh", "Fyr", "Gal", "Hal", "Ith", "Kor", "Lum", "Mor", "Nae", "Or", "Sol", "Tyr",
            "Ul", "Ver", "Yr", "Zor"};
    private static final String[] FAITH_ENDS = {"ism", "ian Creed", "ic Rite", "ine Way", "ar Covenant"};
    private static final String[] CULT_WHAT = {
            "Ashen Star", "Drowned King", "Green Mother", "Hollow Moon", "Red Stag", "Silent Bell", "Weeping Saint",
            "Iron Root", "Pale Flame", "Seventh Gate", "Black Hound", "Sleeping Sun"};

    static Faith broad(int id, Random random) {
        return new Faith(id, STEMS[random.nextInt(STEMS.length)] + FAITH_ENDS[random.nextInt(FAITH_ENDS.length)], false);
    }

    static Faith cult(int id, Random random, java.util.Set<String> taken) {
        String what;
        do {
            what = CULT_WHAT[random.nextInt(CULT_WHAT.length)];
        } while (!taken.add(what));
        return new Faith(id, "Cult of the " + what, true);
    }
}
