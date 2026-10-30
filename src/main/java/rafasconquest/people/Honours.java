package rafasconquest.people;

import rafasconquest.core.TitleMap;

import java.util.ArrayList;
import java.util.List;

/**
 * What a character has to their name besides their traits: their <b>titles</b> (the land titles seated at their hold,
 * a castle's lordship, their sword title) and their <b>feats</b> (for now, what their skills show they have done: a
 * skill of {@link #FEAT_SKILL} or more, a long life). Each has a name and what it means, for the character page.
 */
public final class Honours {
    /** A skill this high is a feat of its own. */
    public static final int FEAT_SKILL = 15;
    /** Living this long is a feat of its own. */
    public static final int FEAT_AGE = 60;

    /** A title or feat: its name and what it means. */
    public record Honour(String name, String description) {
    }

    private Honours() {
    }

    /**
     * A character's titles, highest first: every land title seated at their hold (an empire's seat is also its kingdom's,
     * duchy's and county's), a castle's lordship where it seats none, and their sword title if their prowess earns one.
     * {@code place} is the hold's province name, {@code nation} the name of the nation they serve, or null.
     */
    public static List<Honour> titles(Person p, TitleMap.Tier seat, String place, String nation, boolean ruler) {
        List<Honour> titles = new ArrayList<>();
        if (ruler && nation != null) {
            titles.add(new Honour("Ruler of " + nation, "They rule " + nation + " from its capital, " + place + "."));
        }
        if (seat != null) {
            for (int t = seat.ordinal(); t >= 0; t--) {
                TitleMap.Tier tier = TitleMap.Tier.values()[t];
                titles.add(new Honour(tier.title() + " of " + place, "Its seat is their hold, " + place + "."));
            }
        } else if (p.hold().kind() == Person.Kind.CASTLE) {
            titles.add(new Honour("Lordship of " + place, "They keep the castle of " + place + "."));
        }
        int prowess = p.stat(Stat.PROWESS);
        String sword = Stat.superhumanTier(prowess);
        if (!sword.isEmpty()) {
            titles.add(new Honour(sword, "A martial title: " + Stat.superhumanMeaning(prowess) + "."));
        }
        return titles;
    }

    /** A character's feats: each skill they have mastered, and a long life. */
    public static List<Honour> feats(Person p) {
        List<Honour> feats = new ArrayList<>();
        for (Stat s : Stat.values()) {
            if (p.stat(s) >= FEAT_SKILL && !(s == Stat.PROWESS && p.stat(s) >= Stat.SUPERHUMAN)) {
                feats.add(feat(s));
            }
        }
        if (p.age() >= FEAT_AGE) {
            feats.add(new Honour("Long-Lived", "They have seen more than " + FEAT_AGE + " winters."));
        }
        return feats;
    }

    private static Honour feat(Stat s) {
        return switch (s) {
            case DIPLOMACY -> new Honour("Silver Tongue", "A diplomat of renown: few leave their table unpersuaded.");
            case MARTIAL -> new Honour("Battle-Proven", "A commander whose armies have learned to trust them.");
            case STEWARDSHIP -> new Honour("Master of Coin", "Their lands are kept in good order and their coffers full.");
            case INTRIGUE -> new Honour("Spymaster", "Little is whispered near them that they don't hear.");
            case LEARNING -> new Honour("Sage", "Learned beyond most of their age.");
            case PROWESS -> new Honour("Famed Blade", "Their skill at arms is spoken of across the land.");
        };
    }
}
