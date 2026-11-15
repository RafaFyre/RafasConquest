package rafasconquest.peerage;

/**
 * A player's place on the noble ladder: none yet, a Knight (a noble without land, knighted by a noble they have
 * served), or the holder of a title: a Count (a county), Duke, King or Emperor.
 */
public enum NobleTitle {
    NONE(""), KNIGHT("Knight"), COUNT("Count"), DUKE("Duke"), KING("King"), EMPEROR("Emperor");

    private final String title;

    NobleTitle(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    /** The holder of a title of this tier: a county's a Count, an empire's an Emperor. */
    public static NobleTitle of(rafasconquest.core.TitleMap.Tier tier) {
        return values()[COUNT.ordinal() + tier.ordinal()];
    }

    /** Whether this makes a noble: enlisted as a Major, and dealt with as one. */
    public boolean noble() {
        return this != NONE;
    }
}
