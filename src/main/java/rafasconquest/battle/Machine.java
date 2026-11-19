package rafasconquest.battle;

import rafasconquest.core.Age;
import rafasconquest.people.Homeland;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * An engine armies bring ({@link BattlePlan.Arm#ENGINES}), read from its file in {@code mods/RafasConquest/units/engines}
 * ({@link UnitCards}): its {@code entity} (a Siegeworks one), each unit its {@link BattlePlan#PLATOON_SOLDIERS} soldiers
 * working {@code perUnit} engines, shared out over its blocks. An engine fights from {@code age} on, in the battles it is
 * {@code field}, siege {@code attack} or siege {@code defence} gear for, and, if it has a {@code homeland}, for that
 * land's nations only. In the numbers each engine shoots once every {@code reload} seconds as far as {@code reach}, each
 * shot (misses included) doing {@code damage} health; in a siege an attacker's shot batters the wall for {@code batter}
 * ({@link Siege#batter}). Its crew carry {@code ammo}: item id and count. Its {@link Role} says what the battle does with
 * it: shooters shoot; the battering ram, siege tower and mantlet shoot nothing, their crews bringing them to the gate,
 * the wall, or before the shooters ({@link Siege}).
 * <p>
 * The built-in engines below always exist (their files only change their numbers); a file of a new id adds one.
 */
public final class Machine {
    public enum Role { SHOOTER, RAM, TOWER, MANTLET }

    /** Siegeworks' mod id; its items and engines are named under it unless an id says otherwise. */
    public static final String MOD = "siegeworks";
    /** Shots each engine carries into battle, in the numbers. */
    public static final int SHOTS_EACH = 40;

    private static final List<Machine> ALL = new ArrayList<>();
    private static Machine[] array = new Machine[0];

    public static final Machine CULVERIN = builtIn("culverin");
    public static final Machine SERPENTINE = builtIn("serpentine");
    public static final Machine MONS_MEG = builtIn("mons_meg");
    public static final Machine HWACHA = builtIn("hwacha");
    public static final Machine ARCBALLISTA = builtIn("arcballista");
    public static final Machine ONAGER = builtIn("onager");
    public static final Machine TREBUCHET = builtIn("trebuchet");
    public static final Machine TOWER_CROSSBOW = builtIn("tower_crossbow");
    public static final Machine BATTERING_RAM = builtIn("battering_ram");
    public static final Machine SIEGE_TOWER = builtIn("siege_tower");
    public static final Machine MANTLET = builtIn("mantlet");
    /** The Medium Cannon: a ship's gun on its own block (MediumCannonBlock), not a Siegeworks engine; never an army's. */
    public static final Machine MEDIUM_CANNON = builtIn("medium_cannon");

    /** Its file's name; its entity's path under {@link #MOD} unless {@link #entity} says otherwise. */
    public final String id;
    private int ordinal;
    String entity = "";
    String name = "";
    Role role = Role.SHOOTER;
    /** Engines a full unit works, and so soldiers to an engine (a fraction: a Mons Meg's 100 span five blocks). */
    public int perUnit = 1;
    public double crew = BattlePlan.PLATOON_SOLDIERS;
    public Age age = Age.STEEL;
    @Nullable
    public Homeland homeland;
    public boolean field;
    public boolean attack;
    public boolean defence;
    public double reach;
    public double reload;
    public double damage;
    public double batter;
    /** What it looses, as the client draws it ({@code ArrowVolleys.Kind}, lower case), and the recording of its shot ("" none). */
    String projectile = "arrow";
    String sound = "";
    /** Its box shape where its real model can't be drawn ({@code SoldierFigure.Body}, lower case), how much bigger it is drawn. */
    String body = "cannon";
    float size = 1;
    /** Its shot shakes and flashes big (a Mons Meg's, a trebuchet's); it swings a throwing arm as a trebuchet does. */
    boolean heavy;
    boolean throwing;
    /** Item ids (bare ones under {@link #MOD}) and counts. */
    final List<java.util.Map.Entry<String, Integer>> ammo = new ArrayList<>();

    private Machine(String id) {
        this.id = id;
    }

    private static Machine builtIn(String id) {
        Machine m = add(id);
        UnitCards.readBuiltIn(m);
        return m;
    }

    /** The engine {@code id}, made (shooting nothing, after every other) if there is none yet. */
    static Machine add(String id) {
        Machine m = byId(id);
        if (m == null) {
            m = new Machine(id);
            m.ordinal = ALL.size();
            ALL.add(m);
            array = ALL.toArray(Machine[]::new);
        }
        return m;
    }

    /** Drops every engine but the built-in ones, before another's set is added in its order ({@link UnitCards#replace}). */
    static void keepBuiltIns() {
        ALL.subList(MEDIUM_CANNON.ordinal + 1, ALL.size()).clear();
        array = ALL.toArray(Machine[]::new);
    }

    public static Machine[] values() {
        return array.clone();
    }

    public static int count() {
        return array.length;
    }

    /** The engine at {@code ordinal}; null if there is none. */
    @Nullable
    public static Machine get(int ordinal) {
        return ordinal >= 0 && ordinal < array.length ? array[ordinal] : null;
    }

    @Nullable
    public static Machine byId(String id) {
        for (Machine m : array) {
            if (m.id.equals(id)) {
                return m;
            }
        }
        return null;
    }

    public int ordinal() {
        return ordinal;
    }

    public String name() {
        return id.toUpperCase(java.util.Locale.ROOT);
    }

    @Override
    public String toString() {
        return name();
    }

    /** Its entity's full id. */
    public String entity() {
        return entity.isEmpty() ? MOD + ":" + id : entity.indexOf(':') >= 0 ? entity : MOD + ":" + entity;
    }

    public Role role() {
        return role;
    }

    public String projectile() {
        return projectile;
    }

    public String sound() {
        return sound;
    }

    public String body() {
        return body;
    }

    public float size() {
        return size;
    }

    public boolean heavy() {
        return heavy;
    }

    public boolean throwing() {
        return throwing;
    }

    /** Whether it shoots: the ram, tower and mantlet don't. */
    public boolean shoots() {
        return role == Role.SHOOTER && reach > 0;
    }

    /** Whether its operator rides it from where the crew stands; a ram or tower is brought up as a siege work instead. */
    public boolean ridden() {
        return role != Role.RAM && role != Role.TOWER;
    }

    /** How many of it a block of crew works, on average (a fraction for the heavy ones). */
    public double perBlock() {
        return BattlePlan.SOLDIERS_PER_FORMATION / crew;
    }

    /**
     * How many of it block {@code id} has to show: {@link #perBlock} shared out over blocks numbered in a row, so the
     * fractions add up (a Mons Meg in every fifth block, three culverins in every other block, two in the rest).
     */
    public int inBlock(int id) {
        return (int) (Math.floor((id + 1) * perBlock() + 1e-9) - Math.floor(id * perBlock() + 1e-9));
    }

    /** Whether soldier {@code i} of {@code soldiers} works one of a block's {@code engines}: spread evenly, the first first. */
    public static boolean works(int i, int engines, int soldiers) {
        return engines > 0 && soldiers > 0 && (long) i * engines % soldiers < engines;
    }

    /** The ammunition its crew carry: full item id, count. */
    public List<java.util.Map.Entry<String, Integer>> ammo() {
        List<java.util.Map.Entry<String, Integer>> out = new ArrayList<>();
        for (var e : ammo) {
            out.add(java.util.Map.entry(e.getKey().indexOf(':') >= 0 ? e.getKey() : MOD + ":" + e.getKey(), e.getValue()));
        }
        return out;
    }

    /**
     * The engines an army may bring, by the age its nation is in, its homeland and its battle: a field battle's, a
     * siege's attacker's or defender's (an amphibious assault's defender holds walls too); none at sea.
     */
    public static List<Machine> allowed(Age reached, @Nullable Homeland land, BattleType type, boolean defending) {
        List<Machine> out = new ArrayList<>();
        for (Machine e : array) {
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
