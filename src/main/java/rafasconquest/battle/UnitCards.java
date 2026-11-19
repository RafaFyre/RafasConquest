package rafasconquest.battle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import rafasconquest.core.Age;
import rafasconquest.people.Homeland;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * The unit cards ({@link BattlePlan.Arm}) and engines ({@link Machine}), as data: one JSON file each, in
 * {@code mods/RafasConquest/units/arms}, {@code .../engines} and {@code .../ships} (ship cards,
 * {@link rafasconquest.core.SeaLanes.Size}, whose hull .nbt files lie beside them, {@link rafasconquest.style.ShipHulls}). The built-in ones ship in the jar
 * ({@code rafasconquest_units/}) and are written there when missing, so they can be edited; a field left out of a file
 * keeps the built-in value; a file of a new id adds a card or an engine. Read at startup; a server sends its own to
 * each client that joins ({@link #texts}, {@link #apply}), so both number them alike.
 */
public final class UnitCards {
    public static final String ARMS = "arms";
    public static final String ENGINES = "engines";
    public static final String SHIPS = "ships";
    private static final String JAR = "/rafasconquest_units/";

    private UnitCards() {
    }

    /** The files this game read, {kind folder, id, JSON}, engines first: what a server sends its clients. */
    private static final List<String[]> READ = new ArrayList<>();

    static void readBuiltIn(BattlePlan.Arm a) {
        JsonObject o = jar(ARMS, a.id);
        if (o != null) {
            arm(a, o);
        }
    }

    static void readBuiltIn(Machine m) {
        JsonObject o = jar(ENGINES, m.id);
        if (o != null) {
            machine(m, o);
        }
    }

    public static void readBuiltIn(rafasconquest.core.SeaLanes.Size s) {
        JsonObject o = jar(SHIPS, s.id);
        if (o != null) {
            ship(s, o);
        }
    }

    private static JsonObject jar(String folder, String id) {
        String text = jarText(folder, id);
        return text == null ? null : JsonParser.parseString(text).getAsJsonObject();
    }

    private static String jarText(String folder, String id) {
        try (InputStream in = UnitCards.class.getResourceAsStream(JAR + folder + "/" + id + ".json")) {
            return in == null ? null : new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads every card and engine file under {@code root}, first writing out the built-in ones that are missing. A file
     * that can't be read is left out (logged), its card keeping the built-in values.
     */
    public static void load(Path root) {
        READ.clear();
        for (String folder : new String[]{SHIPS, ENGINES, ARMS}) {
            Path dir = root.resolve(folder);
            try {
                Files.createDirectories(dir);
                List<String> builtIns = folder.equals(SHIPS)
                        ? java.util.Arrays.stream(rafasconquest.core.SeaLanes.Size.values()).filter(rafasconquest.core.SeaLanes.Size::builtIn).map(s -> s.id).toList()
                        : folder.equals(ENGINES)
                        ? java.util.Arrays.stream(Machine.values()).map(m -> m.id).toList()
                        : java.util.Arrays.stream(BattlePlan.Arm.values()).filter(BattlePlan.Arm::builtIn).map(a -> a.id).toList();
                for (String id : builtIns) {
                    Path file = dir.resolve(id + ".json");
                    String text = jarText(folder, id);
                    if (!Files.exists(file) && text != null) {
                        Files.writeString(file, text);
                    }
                }
                List<Path> files;
                try (var listing = Files.list(dir)) {
                    files = listing.filter(p -> p.getFileName().toString().endsWith(".json")).sorted().toList();
                }
                for (Path file : files) {
                    String name = file.getFileName().toString();
                    String id = name.substring(0, name.length() - ".json".length()).toLowerCase(Locale.ROOT);
                    try {
                        String text = Files.readString(file);
                        apply(folder, id, text);
                        READ.add(new String[]{folder, id, text});
                    } catch (IOException | RuntimeException e) {
                        rafasconquest.RafasConquest.LOGGER.warn("Unit cards: couldn't read {}; it keeps its built-in values", file, e);
                    }
                }
            } catch (IOException e) {
                rafasconquest.RafasConquest.LOGGER.warn("Unit cards: couldn't read {}", dir, e);
            }
        }
        rafasconquest.RafasConquest.LOGGER.info("Unit cards: {} cards, {} engines, {} ships", BattlePlan.Arm.values().length, Machine.count(),
                rafasconquest.core.SeaLanes.Size.values().length);
    }

    /** The files read, {folder, id, JSON}, engines first, to send to a client. */
    public static List<String[]> texts() {
        return List.copyOf(READ);
    }

    /**
     * A server's files, {folder, id, JSON} engines first, in place of this game's own: a client's cards then number
     * as the server's do. A file that can't be read is left out (logged).
     */
    public static void replace(List<String[]> files) {
        rafasconquest.core.SeaLanes.Size.keepBuiltIns();
        Machine.keepBuiltIns();
        BattlePlan.Arm.keepBuiltIns();
        for (String[] f : files) {
            try {
                apply(f[0], f[1], f[2]);
            } catch (RuntimeException e) {
                rafasconquest.RafasConquest.LOGGER.warn("Unit cards: the server's {} {} couldn't be read", f[0], f[1], e);
            }
        }
    }

    /** One file's card or engine, made if new. */
    public static void apply(String folder, String id, String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        if (folder.equals(SHIPS)) {
            ship(rafasconquest.core.SeaLanes.Size.add(id), o);
        } else if (folder.equals(ENGINES)) {
            machine(Machine.add(id), o);
        } else {
            arm(BattlePlan.Arm.add(id), o);
        }
    }

    static void arm(BattlePlan.Arm a, JsonObject o) {
        a.name = str(o, "name", a.name);
        a.kind = upper(o, "kind", a.kind, BattlePlan.Arm.Kind.class);
        a.hits = num(o, "hits", a.hits);
        a.takes = num(o, "takes", a.takes);
        a.pace = num(o, "pace", a.pace);
        a.nerve = num(o, "nerve", a.nerve);
        if (o.has("traits")) {
            java.util.Set<String> traits = new HashSet<>();
            for (JsonElement t : o.getAsJsonArray("traits")) {
                traits.add(t.getAsString().toLowerCase(Locale.ROOT));
            }
            a.traits = java.util.Set.copyOf(traits);
        }
        a.missile = upper(o, "missile", a.missile, BattlePlan.Arm.Missile.class);
        a.recruit = str(o, "recruit", a.recruit);
        a.kit = str(o, "kit", a.kit).toLowerCase(Locale.ROOT);
        if (o.has("gear")) {
            JsonObject g = o.getAsJsonObject("gear");
            String[] slots = {"head", "chest", "legs", "feet", "mainhand", "offhand"};
            for (int i = 0; i < slots.length; i++) {
                a.gear[i] = str(g, slots[i], "");
            }
        }
        a.figure = str(o, "figure", a.figure).toLowerCase(Locale.ROOT);
        a.icon = str(o, "icon", a.icon);
        a.front = (int) num(o, "front", a.front);
        a.requires = str(o, "requires", a.requires);
        a.otherwise = str(o, "otherwise", a.otherwise);
        a.roster = o.has("roster") ? o.get("roster").getAsBoolean() : a.roster;
        a.engine = str(o, "engine", a.engine);
        a.ship = str(o, "ship", a.ship).toLowerCase(Locale.ROOT);
    }

    static void machine(Machine m, JsonObject o) {
        m.entity = str(o, "entity", m.entity);
        m.name = str(o, "name", m.name);
        m.role = upper(o, "role", m.role, Machine.Role.class);
        if (o.has("per_unit")) {
            m.perUnit = Math.max(1, o.get("per_unit").getAsInt());
            m.crew = BattlePlan.PLATOON_SOLDIERS / (double) m.perUnit;
        }
        m.age = upper(o, "age", m.age, Age.class);
        if (o.has("homeland")) {
            m.homeland = o.get("homeland").isJsonNull() || o.get("homeland").getAsString().isEmpty() ? null
                    : Homeland.valueOf(o.get("homeland").getAsString().toUpperCase(Locale.ROOT));
        }
        m.field = bool(o, "field", m.field);
        m.attack = bool(o, "attack", m.attack);
        m.defence = bool(o, "defence", m.defence);
        m.reach = num(o, "reach", m.reach);
        m.reload = num(o, "reload", m.reload);
        m.damage = num(o, "damage", m.damage);
        m.batter = num(o, "batter", m.batter);
        m.projectile = str(o, "projectile", m.projectile).toLowerCase(Locale.ROOT);
        m.sound = str(o, "sound", m.sound);
        m.body = str(o, "body", m.body).toLowerCase(Locale.ROOT);
        m.size = (float) num(o, "size", m.size);
        m.heavy = bool(o, "heavy", m.heavy);
        m.throwing = bool(o, "throwing", m.throwing);
        if (o.has("ammo")) {
            m.ammo.clear();
            for (var e : o.getAsJsonObject("ammo").entrySet()) {
                m.ammo.add(java.util.Map.entry(e.getKey(), e.getValue().getAsInt()));
            }
        }
    }

    static void ship(rafasconquest.core.SeaLanes.Size s, JsonObject o) {
        s.name = str(o, "name", s.name);
        s.length = Math.max(3, (int) num(o, "length", s.length));
        s.width = Math.max(3, (int) num(o, "width", s.width));
        s.deck = Math.max(1, (int) num(o, "deck", s.deck));
        s.speed = Math.max(0.1, num(o, "speed", s.speed));
        s.lanes = Math.max(0, (int) num(o, "lanes", s.lanes));
        s.battle = str(o, "battle", s.battle).toLowerCase(Locale.ROOT);
        s.hull = str(o, "hull", s.hull);
        s.merchant = str(o, "merchant", s.merchant);
        s.type = str(o, "type", s.type).toLowerCase(Locale.ROOT);
        s.gunBlock = str(o, "gun_block", s.gunBlock);
    }

    private static String str(JsonObject o, String key, String otherwise) {
        return o.has(key) && !o.get(key).isJsonNull() ? o.get(key).getAsString() : otherwise;
    }

    private static double num(JsonObject o, String key, double otherwise) {
        return o.has(key) ? o.get(key).getAsDouble() : otherwise;
    }

    private static boolean bool(JsonObject o, String key, boolean otherwise) {
        return o.has(key) ? o.get(key).getAsBoolean() : otherwise;
    }

    private static <E extends Enum<E>> E upper(JsonObject o, String key, E otherwise, Class<E> type) {
        return o.has(key) ? Enum.valueOf(type, o.get(key).getAsString().toUpperCase(Locale.ROOT)) : otherwise;
    }
}
