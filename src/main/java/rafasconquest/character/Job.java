package rafasconquest.character;

import java.util.Locale;

/**
 * What a player fights as in a battle, and what the soldiers they command fight as: their kind of soldier and kit.
 * Heavy infantry is the default.
 */
public enum Job {
    /** Rides a horse, as do their soldiers; cavalry momentum applies to the player's horse and to their soldiers. */
    CAVALRY("cavalry"),
    /** Swordsmen with iron swords; the player gets an iron sword with Sweeping Edge. */
    FOOTMAN("footman"),
    /** Bowmen; the player gets a bow with Infinity and Unbreaking II. */
    ARCHER("archer"),
    /** Shieldmen with the nation's banner on their shields; the player gets one too. Shields up braces like a wall. */
    SHIELDMAN("shieldman"),
    /** In iron armour, with axes and the nation's banner on their shields; the player too. */
    HEAVY_INFANTRY("heavy_infantry");

    private final String id;

    Job(String id) {
        this.id = id;
    }

    /** The name used in commands. */
    public String id() {
        return id;
    }

    /** As said in a sentence: "heavy infantry". */
    public String title() {
        return id.replace('_', ' ');
    }

    public static Job byId(String id) {
        for (Job j : values()) {
            if (j.id.equals(id.toLowerCase(Locale.ROOT))) {
                return j;
            }
        }
        return HEAVY_INFANTRY;
    }
}
