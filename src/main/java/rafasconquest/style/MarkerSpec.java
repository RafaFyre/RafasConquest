package rafasconquest.style;

import net.minecraft.nbt.CompoundTag;

import java.util.List;

/**
 * What a Character Marker becomes: its type, and the settings that type uses (the rest are kept, unused). Recruits are
 * soldiers without stats or names; characters are people of the realm, chosen when the marker fires.
 *
 * @param type      one of {@link #TYPES}
 * @param kit       a soldier's kit: 1 levy, 2 heavy infantry, 3 elite, 4 officer
 * @param patrol    soldiers patrol about the marker (else they stand guard on it)
 * @param count     how many soldiers, 1 to 5
 * @param label     a name for the marker, by which another marker of the same build can refer to whom it placed
 * @param rank      a named character's rank in the peerage: one of {@link #RANKS}
 * @param relation  how they stand to {@code other}: one of {@link #RELATIONS}
 * @param other     whom: "place" (this place's holder), "nation" (its nation's ruler), or another marker's label
 * @param trait     a trait they have (a {@code Personality} name), or ""
 * @param stat      a stat they are chosen by (a {@code Stat} name), or ""
 * @param statMin   the least of it they must have; -1: the highest of it among those that fit
 * @param twoPlaces whether someone already placed elsewhere may be placed here too
 * @param ifNone    if no one fits: "EMPTY" (nothing), or "GUARD" (a soldier stands guard instead)
 * @param replace   the block the marker becomes once it fires ("" for air), as a block id
 */
public record MarkerSpec(String type, int kit, boolean patrol, int count, String label, String rank, String relation, String other,
                         String trait, String stat, int statMin, boolean twoPlaces, String ifNone, String replace) {
    public static final List<String> TYPES = List.of("BOWMAN", "SHIELDMAN", "KNIGHT", "CAVALRY", "SWORD_GOD", "SWORD_KING", "SWORD_SAINT",
            "RECRUITER", "CITY_MANAGER", "QUEST_GIVER", "SHOP_OWNER", "SHOP_AND_QUESTS", "NAMED");
    public static final List<String> RANKS = List.of("ANY", "RULER", "CAPITAL_LORD", "CITY", "TOWN", "CASTLE");
    public static final List<String> RELATIONS = List.of("NONE", "LIEGE_OF", "VASSAL_OF", "FRIEND_OF", "RIVAL_OF");
    public static final MarkerSpec DEFAULT = new MarkerSpec("SHIELDMAN", 2, false, 1, "", "ANY", "NONE", "place", "", "", -1, false, "EMPTY", "");

    public static String title(String type) {
        return switch (type) {
            case "BOWMAN" -> "Recruits: Bowman";
            case "SHIELDMAN" -> "Recruits: Shieldman";
            case "KNIGHT" -> "Recruits: Knight";
            case "CAVALRY" -> "Recruits: Cavalry";
            case "SWORD_GOD" -> "Character: Sword God";
            case "SWORD_KING" -> "Character: Sword King";
            case "SWORD_SAINT" -> "Character: Sword Saint";
            case "RECRUITER" -> "NPC: Recruiter";
            case "CITY_MANAGER" -> "NPC: City manager";
            case "QUEST_GIVER" -> "NPC: Quest giver";
            case "SHOP_OWNER" -> "NPC: Shop owner";
            case "SHOP_AND_QUESTS" -> "NPC: Shop owner and quest giver";
            default -> "Named Character";
        };
    }

    public boolean soldiers() {
        return type.equals("BOWMAN") || type.equals("SHIELDMAN") || type.equals("KNIGHT") || type.equals("CAVALRY");
    }

    /** What floats above an unarmed marker. */
    public String describe() {
        String what = title(type);
        if (soldiers()) {
            what += " x" + count + ", " + new String[]{"levy", "heavy", "elite", "officer"}[Math.max(1, Math.min(4, kit)) - 1]
                    + (patrol ? ", patrols" : ", guards");
        } else if (type.equals("NAMED")) {
            StringBuilder b = new StringBuilder();
            if (!rank.equals("ANY")) {
                b.append(' ').append(rank.toLowerCase().replace('_', ' '));
            }
            if (!relation.equals("NONE")) {
                b.append(' ').append(relation.toLowerCase().replace('_', ' ')).append(' ').append(other);
            }
            if (!trait.isEmpty()) {
                b.append(", ").append(trait.toLowerCase());
            }
            if (!stat.isEmpty()) {
                b.append(", ").append(statMin < 0 ? "highest " + stat.toLowerCase() : stat.toLowerCase() + " " + statMin + "+");
            }
            what += b;
        }
        return label.isEmpty() ? what : "[" + label + "] " + what;
    }

    public CompoundTag save() {
        CompoundTag t = new CompoundTag();
        t.putString("type", type);
        t.putInt("kit", kit);
        t.putBoolean("patrol", patrol);
        t.putInt("count", count);
        t.putString("label", label);
        t.putString("rank", rank);
        t.putString("relation", relation);
        t.putString("other", other);
        t.putString("trait", trait);
        t.putString("stat", stat);
        t.putInt("statMin", statMin);
        t.putBoolean("twoPlaces", twoPlaces);
        t.putString("ifNone", ifNone);
        t.putString("replace", replace);
        return t;
    }

    public static MarkerSpec load(CompoundTag t) {
        if (!t.contains("type")) {
            return DEFAULT;
        }
        return new MarkerSpec(t.getString("type"), t.getInt("kit"), t.getBoolean("patrol"), Math.max(1, Math.min(5, t.getInt("count"))),
                t.getString("label"), t.getString("rank"), t.getString("relation"), t.getString("other"), t.getString("trait"),
                t.getString("stat"), t.getInt("statMin"), t.getBoolean("twoPlaces"), t.getString("ifNone"), t.getString("replace"));
    }
}
