package rafasconquest.character;

import java.util.Locale;

/** The part a player's unit plays in a battle: where it starts and what it is sent to do. Frontline is the default. */
public enum BattleRole {
    /** Starts in the front line and runs ahead of it, falling back once fighting to draw the enemy on. */
    BAIT("bait"),
    /** Starts in reserve and moves through its wing to reinforce blocks that are losing. */
    SUPPORT("support"),
    /** Starts in the front line, in the first block to charge. */
    FRONTLINE("frontline"),
    /** Starts in reserve on a wing and strikes enemy blocks in the flank or rear. */
    FLANKER("flanker"),
    /** Starts in reserve and goes after the enemy's officers: their general, else their captains. */
    KILL_SQUAD("kill_squad");

    private final String id;

    BattleRole(String id) {
        this.id = id;
    }

    /** The name used in commands. */
    public String id() {
        return id;
    }

    /** As said in a sentence: "kill squad". */
    public String title() {
        return id.replace('_', ' ');
    }

    public static BattleRole byId(String id) {
        for (BattleRole r : values()) {
            if (r.id.equals(id.toLowerCase(Locale.ROOT))) {
                return r;
            }
        }
        return FRONTLINE;
    }
}
