package rafasconquest.character;

import java.util.Random;

/**
 * An officer's character, which colours what they say at the war council and what they offer a player there: a bold
 * Marshal invites them to a flank attack, a cautious one asks them to hold in support, a cunning general wants a bait,
 * a proud one won't hear the plan questioned. A bold general pursues a beaten army; a cautious one lets it go.
 */
public enum Trait {
    BOLD("bold"),
    CAUTIOUS("cautious"),
    CUNNING("cunning"),
    PROUD("proud");

    private final String title;

    Trait(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public static Trait random(Random random) {
        Trait[] all = values();
        return all[random.nextInt(all.length)];
    }
}
