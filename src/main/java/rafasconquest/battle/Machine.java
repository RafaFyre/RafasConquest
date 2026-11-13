package rafasconquest.battle;

import rafasconquest.core.Age;
import rafasconquest.people.Homeland;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Siegeworks' engines that armies bring in their siege train ({@link BattlePlan.Arm#ENGINES}): each its own unit, its
 * blocks crewing {@code BattlePlan.SOLDIERS_PER_FORMATION / crew} engines each. An engine fights from {@code age} on, in
 * the battles it is {@code field}, siege {@code attack} or siege {@code defence} gear for, and, if it has a
 * {@code homeland}, for that land's nations only. In the numbers each engine shoots once every {@code reload} seconds
 * as far as {@code reach}, each shot (misses included) doing {@code damage} health; first-pass figures from Siegeworks'
 * own loading times and damage; in a siege an attacker's shot batters the wall for {@code batter} ({@link Siege#batter}).
 * Its crew carry {@code ammo}: item id and count, each a shot's worth or a tool kept. The battering ram, siege tower
 * and mantlet shoot nothing: their crews bring them to the gate, the wall, or before the shooters ({@link Siege}).
 */
public enum Machine {
    CULVERIN("culverin", 5, Age.GUNPOWDER, null, true, false, true, 120, 15, 42, 0,
            "black_powder", 64, "cannon_ball", 16, "ramrod", 1),
    SERPENTINE("serpentine", 10, Age.GUNPOWDER, null, true, false, true, 140, 19, 63, 0,
            "black_powder", 64, "cannon_ball", 16, "ramrod", 1),
    MONS_MEG("mons_meg", 20, Age.GUNPOWDER, null, false, true, false, 160, 34, 84, 2,
            "black_powder", 64, "giant_cannon_ball", 6, "ramrod", 1),
    HWACHA("hwacha", 5, Age.GUNPOWDER, Homeland.JUNGLE, true, false, false, 90, 35, 84, 0,
            "so_singijeon", 64),
    ARCBALLISTA("arcballista", 5, Age.STEEL, null, true, false, false, 80, 7, 21, 0,
            "arcballista_bolt", 32),
    ONAGER("onager", 10, Age.STEEL, null, true, true, false, 90, 13, 63, 0.5,
            "minecraft:stone", 64),
    TREBUCHET("trebuchet", 20, Age.STEEL, null, false, true, false, 150, 24, 84, 1,
            "minecraft:stone", 64),
    TOWER_CROSSBOW("tower_crossbow", 10, Age.STEEL, null, false, false, true, 80, 11, 21, 0,
            "tower_crossbow_bolt", 32),
    BATTERING_RAM("battering_ram", 20, Age.STEEL, null, false, true, false, 0, 0, 0, 0),
    SIEGE_TOWER("siege_tower", 20, Age.STEEL, null, false, true, false, 0, 0, 0, 0),
    MANTLET("mantlet", 5, Age.STEEL, null, false, true, false, 0, 0, 0, 0);

    /** Siegeworks' mod id; its items and engines are named under it unless an id says otherwise. */
    public static final String MOD = "siegeworks";
    /** Shots each engine carries into battle, in the numbers. */
    public static final int SHOTS_EACH = 40;

    /** Its entity's id, under {@link #MOD}. */
    public final String id;
    /** Soldiers to an engine: 5 light, 10 medium, 20 heavy. */
    public final int crew;
    public final Age age;
    @Nullable
    public final Homeland homeland;
    public final boolean field;
    public final boolean attack;
    public final boolean defence;
    public final double reach;
    public final double reload;
    public final double damage;
    public final double batter;
    /** Item ids (bare ones under {@link #MOD}) and counts, in pairs. */
    private final Object[] ammo;

    Machine(String id, int crew, Age age, @Nullable Homeland homeland, boolean field, boolean attack, boolean defence,
           double reach, double reload, double damage, double batter, Object... ammo) {
        this.id = id;
        this.crew = crew;
        this.age = age;
        this.homeland = homeland;
        this.field = field;
        this.attack = attack;
        this.defence = defence;
        this.reach = reach;
        this.reload = reload;
        this.damage = damage;
        this.batter = batter;
        this.ammo = ammo;
    }

    /** Whether it shoots: the ram, tower and mantlet don't. */
    public boolean shoots() {
        return reach > 0;
    }

    /** Whether its operator rides it from where the crew stands; a ram or tower is brought up as a siege work instead. */
    public boolean ridden() {
        return this != BATTERING_RAM && this != SIEGE_TOWER;
    }

    /** How many of it a block of crew works. */
    public int perBlock() {
        return Math.max(1, BattlePlan.SOLDIERS_PER_FORMATION / crew);
    }

    /** The ammunition its crew carry: full item id, count. */
    public List<java.util.Map.Entry<String, Integer>> ammo() {
        List<java.util.Map.Entry<String, Integer>> out = new ArrayList<>();
        for (int i = 0; i < ammo.length; i += 2) {
            String item = (String) ammo[i];
            out.add(java.util.Map.entry(item.indexOf(':') >= 0 ? item : MOD + ":" + item, (Integer) ammo[i + 1]));
        }
        return out;
    }

    /**
     * The engines an army may bring, by the age its nation is in, its homeland and its battle: a field battle's, a
     * siege's attacker's or defender's (an amphibious assault's defender holds walls too); none at sea.
     */
    public static List<Machine> allowed(Age reached, @Nullable Homeland land, BattleType type, boolean defending) {
        List<Machine> out = new ArrayList<>();
        for (Machine e : values()) {
            boolean serves = switch (type) {
                case LAND -> e.field;
                case SIEGE -> defending ? e.defence : e.attack;
                case AMPHIBIOUS -> defending && e.defence;
                case OCEAN -> false;
            };
            if (serves && reached.compareTo(e.age) >= 0 && (e.homeland == null || e.homeland == land)) {
                out.add(e);
            }
        }
        return out;
    }
}
