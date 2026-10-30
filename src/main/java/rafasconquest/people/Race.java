package rafasconquest.people;

/**
 * A people's race: one for each kind of land ({@link Homeland}), as many as there are vanilla villager types. Where a
 * nation's people are of a race follows the lands it holds; their culture follows its seat ({@link Demographics}).
 */
public enum Race {
    SUNBORN("Sunborn"),
    VINEKIN("Vinekin"),
    MEADOWFOLK("Meadowfolk"),
    GRASSWALKERS("Grasswalkers"),
    FROSTBORN("Frostborn"),
    BOGFOLK("Bogfolk"),
    PINEKIN("Pinekin");

    private final String title;

    Race(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public Homeland homeland() {
        return Homeland.values()[ordinal()];
    }
}
