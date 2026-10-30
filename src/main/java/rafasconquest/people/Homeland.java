package rafasconquest.people;

/**
 * The kinds of land people come from, one for each vanilla villager type, read from the biome under each part of a
 * province. Each has its {@link Culture} and its {@link Race}.
 */
public enum Homeland {
    DESERT("Desert"),
    JUNGLE("Jungle"),
    PLAINS("Plains"),
    SAVANNA("Savanna"),
    SNOW("Snow"),
    SWAMP("Swamp"),
    TAIGA("Taiga");

    private final String title;

    Homeland(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public Race race() {
        return Race.values()[ordinal()];
    }
}
