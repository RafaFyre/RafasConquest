package rafasconquest.peerage;

import net.minecraft.nbt.CompoundTag;

/**
 * A quest, as posted on a quest board ({@link Quests}): what to do, for whom, where, and what it pays.
 * <ul>
 *   <li><b>Deliver</b>: carry the giver's letter to a hold; done on arriving there.</li>
 *   <li><b>Scout</b>: reach a place and report back to the giver.</li>
 *   <li><b>Clear</b>: kill {@code need} hostile creatures about a place, and report back.</li>
 * </ul>
 * Its ladder says whose renown it earns: a noble's quest the noble ladder's, an officer's the military's, a board's
 * general quest none (emeralds only). Its giver is a character (index), {@link #OFFICER} (a post's recruiter) or
 * {@link #BOARD} (taken and reported at the board itself), found at (giverX, giverZ).
 */
public record Quest(long id, Type type, Ladder ladder, int giver, String giverName, int giverX, int giverZ, String targetName,
                    int targetX, int targetZ, int need, int renown, int emeralds, String nation) {
    public static final int OFFICER = -1;
    public static final int BOARD = -2;
    /** How near a place counts as reaching it, and how far about it a kill counts toward clearing it. */
    public static final int ARRIVE = 32;
    public static final int CLEAR_REACH = 64;

    public enum Type {
        DELIVER, SCOUT, CLEAR
    }

    public enum Ladder {
        NONE, NOBLE, MILITARY
    }

    /** What the quest asks, as the board and the dialogue say it. */
    public String title() {
        return switch (type) {
            case DELIVER -> "Deliver a letter to " + targetName;
            case SCOUT -> "Scout " + targetName;
            case CLEAR -> "Clear " + need + " hostile creatures about " + targetName;
        };
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("id", id);
        tag.putString("type", type.name());
        tag.putString("ladder", ladder.name());
        tag.putInt("giver", giver);
        tag.putString("giverName", giverName);
        tag.putInt("giverX", giverX);
        tag.putInt("giverZ", giverZ);
        tag.putString("targetName", targetName);
        tag.putInt("targetX", targetX);
        tag.putInt("targetZ", targetZ);
        tag.putInt("need", need);
        tag.putInt("renown", renown);
        tag.putInt("emeralds", emeralds);
        tag.putString("nation", nation);
        return tag;
    }

    public static Quest load(CompoundTag tag) {
        return new Quest(tag.getLong("id"), Type.valueOf(tag.getString("type")), Ladder.valueOf(tag.getString("ladder")), tag.getInt("giver"),
                tag.getString("giverName"), tag.getInt("giverX"), tag.getInt("giverZ"), tag.getString("targetName"), tag.getInt("targetX"),
                tag.getInt("targetZ"), tag.getInt("need"), tag.getInt("renown"), tag.getInt("emeralds"), tag.getString("nation"));
    }
}
