package rafasconquest.battle;

/**
 * What kind of battle is fought: fleets in open water, armies in the field, an army at a castle's or walled city's walls,
 * or one coming at a port's walls from the water, by ship and on foot. The defender is always the second side.
 */
public enum BattleType {
    OCEAN("Sea battle", 1.0),
    LAND("Battle", 1.15),
    SIEGE("Siege", 2.0),
    AMPHIBIOUS("Amphibious assault", 2.4);

    private final String title;
    private final double defenderEdge;

    BattleType(String title, double defenderEdge) {
        this.title = title;
        this.defenderEdge = defenderEdge;
    }

    public String title() {
        return title;
    }

    /** How much each defender counts for, against an attacker, when the battle is worked out by strength alone. */
    public double defenderEdge() {
        return defenderEdge;
    }

    /** Whether the defenders hold walls: they never come out past them, and are hard to hurt behind them. */
    public boolean walled() {
        return this == SIEGE || this == AMPHIBIOUS;
    }
}
