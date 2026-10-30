package rafasconquest.peerage;

/**
 * A player's place on the noble ladder: none yet, a Knight (a noble without land, knighted by a noble they have
 * served), or a Count (granted a county). Higher titles will follow as players can hold more.
 */
public enum NobleTitle {
    NONE(""), KNIGHT("Knight"), COUNT("Count");

    private final String title;

    NobleTitle(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    /** Whether this makes a noble: enlisted as a Major, and dealt with as one. */
    public boolean noble() {
        return this != NONE;
    }
}
