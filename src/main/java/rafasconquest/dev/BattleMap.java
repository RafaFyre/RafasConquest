package rafasconquest.dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldArmy;
import rafasconquest.battle.FieldBattle;
import rafasconquest.battle.FieldBlock;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.bridge.RecruitsBattle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * The battle map: a developer's web page, served on this machine only, that shows the debug battle block by block as
 * it stands (one army on the top half, the other on the bottom) and lets its blocks be placed by hand before the
 * battle starts, saved as named formations and named groups, and the battle started.
 * <p>
 * The page asks for the state a few times a second; its edits are queued and applied on the server thread, so the
 * battle is only ever touched there. Formations are saved as JSON in {@code config/rafasconquest/formations}.
 */
public final class BattleMap {
    public static final int PORT = 8099;
    private static final int STATE_EVERY_TICKS = 5;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    private static HttpServer http;
    private static volatile String state = "{\"phase\":\"none\"}";
    private static final ConcurrentLinkedQueue<Runnable> pending = new ConcurrentLinkedQueue<>();
    private static int ticks;

    private BattleMap() {
    }

    /** Starts the map's web server if it isn't running; returns the page's address. */
    public static synchronized String open(MinecraftServer server) {
        if (http == null) {
            try {
                HttpServer created = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), PORT), 0);
                created.createContext("/", BattleMap::page);
                created.createContext("/api/state", exchange -> send(exchange, 200, "application/json", state));
                created.createContext("/api/formations", exchange -> send(exchange, 200, "application/json", GSON.toJson(formationNames())));
                created.createContext("/api/action", BattleMap::action);
                created.start();
                http = created;
                RafasConquest.LOGGER.info("[battle map] at http://localhost:{}/", PORT);
            } catch (IOException e) {
                RafasConquest.LOGGER.error("[battle map] couldn't start on port {}", PORT, e);
            }
        }
        return "http://localhost:" + PORT + "/";
    }

    public static synchronized void close() {
        if (http != null) {
            http.stop(0);
            http = null;
        }
    }

    /** Each server tick: applies the page's edits, and a few times a second takes down the battle's state for it. */
    public static void tick() {
        Runnable edit;
        while ((edit = pending.poll()) != null) {
            edit.run();
        }
        if (http != null && ++ticks % STATE_EVERY_TICKS == 0) {
            state = GSON.toJson(state(RecruitsBattle.current()));
        }
    }

    // ---- the page and its requests (web server threads) ----

    private static void page(HttpExchange exchange) throws IOException {
        try (InputStream in = BattleMap.class.getResourceAsStream("/assets/rafasconquest/web/battle_map.html")) {
            if (in == null) {
                send(exchange, 404, "text/plain", "battle_map.html is missing");
                return;
            }
            send(exchange, 200, "text/html; charset=utf-8", new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    /** An edit from the page, done on the server thread; answers {ok, message}. */
    private static void action(HttpExchange exchange) throws IOException {
        JsonObject reply = new JsonObject();
        try {
            JsonObject request = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
            CompletableFuture<String> done = new CompletableFuture<>();
            pending.add(() -> {
                try {
                    done.complete(act(request));
                } catch (RuntimeException e) {
                    done.completeExceptionally(e);
                }
            });
            reply.addProperty("message", done.get(5, TimeUnit.SECONDS));
            reply.addProperty("ok", true);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            reply.addProperty("ok", false);
            reply.addProperty("message", String.valueOf(cause.getMessage()));
        }
        send(exchange, 200, "application/json", GSON.toJson(reply));
    }

    private static void send(HttpExchange exchange, int status, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    // ---- edits (server thread) ----

    private static String act(JsonObject request) {
        RecruitsBattle battle = RecruitsBattle.current();
        if (battle == null) {
            throw new IllegalStateException("No battle: start one with /pve debug battle");
        }
        String type = request.get("type").getAsString();
        switch (type) {
            case "start" -> {
                return RecruitsBattle.startFighting();
            }
            case "move" -> {
                if (battle.started()) {
                    throw new IllegalStateException("Blocks can only be placed before the battle starts");
                }
                int moved = 0;
                for (JsonElement e : request.getAsJsonArray("moves")) {
                    JsonObject m = e.getAsJsonObject();
                    FieldBlock block = block(battle.field(), m.get("id").getAsInt());
                    if (block != null) {
                        battle.field().place(block, new Point(m.get("x").getAsDouble(), m.get("z").getAsDouble()));
                        moved++;
                    }
                }
                return "Placed " + moved + " block" + (moved == 1 ? "" : "s");
            }
            case "saveGroup" -> {
                int side = request.get("side").getAsInt();
                String name = name(request);
                List<Integer> ids = new ArrayList<>();
                request.getAsJsonArray("ids").forEach(e -> ids.add(e.getAsInt()));
                battle.groups(side).put(name, ids);
                return "Saved group \"" + name + "\" (" + ids.size() + " blocks)";
            }
            case "deleteGroup" -> {
                String name = name(request);
                battle.groups(request.get("side").getAsInt()).remove(name);
                return "Deleted group \"" + name + "\"";
            }
            case "saveFormation" -> {
                return saveFormation(battle, request.get("side").getAsInt(), name(request));
            }
            case "loadFormation" -> {
                return loadFormation(battle, request.get("side").getAsInt(), name(request));
            }
            default -> throw new IllegalArgumentException("Unknown action " + type);
        }
    }

    /** A name for a group or formation file: letters, digits, spaces, - and _ only. */
    private static String name(JsonObject request) {
        String name = request.get("name").getAsString().trim();
        if (name.isEmpty() || !name.matches("[A-Za-z0-9 _-]{1,40}")) {
            throw new IllegalArgumentException("Names may use letters, digits, spaces, - and _ (at most 40)");
        }
        return name;
    }

    @Nullable
    private static FieldBlock block(FieldBattle field, int id) {
        for (int side = 0; side < 2; side++) {
            for (FieldBlock b : field.army(side).blocks()) {
                if (b.id() == id) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * Each block's name within its army, which a formation is saved by so it fits any army of the same make-up:
     * its kind (GENERAL, GUARD, BEARER, or its arm) and how many of that kind came before it, e.g. "CAVALRY#3".
     */
    private static Map<String, FieldBlock> keys(FieldArmy army) {
        Map<String, FieldBlock> keys = new LinkedHashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        for (FieldBlock b : army.blocks()) {
            String kind = kind(b);
            int n = counts.merge(kind, 1, Integer::sum) - 1;
            keys.put(kind + "#" + n, b);
        }
        return keys;
    }

    private static String kind(FieldBlock b) {
        return switch (b.duty()) {
            case GENERAL -> "GENERAL";
            case GUARD -> "GUARD";
            case BEARER -> "BEARER";
            default -> b.arm().name();
        };
    }

    private static Path formationsDir() {
        return FMLPaths.CONFIGDIR.get().resolve(RafasConquest.MOD_ID).resolve("formations");
    }

    private static List<String> formationNames() {
        Path dir = formationsDir();
        if (!Files.isDirectory(dir)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(dir)) {
            return files.map(p -> p.getFileName().toString()).filter(n -> n.endsWith(".json"))
                    .map(n -> n.substring(0, n.length() - 5)).sorted().toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * Saves a side's formation: where each block is ordered to stand, {right, back} of where its army's front centre
     * deployed (in the army's own frame, so it fits either side), with its kind, duty, wing and width, and the side's
     * groups by block name.
     */
    private static String saveFormation(RecruitsBattle battle, int side, String name) {
        FieldArmy army = battle.field().army(side);
        Map<String, FieldBlock> keys = keys(army);
        Map<Integer, String> keyOf = new HashMap<>();
        JsonObject file = new JsonObject();
        file.addProperty("name", name);
        file.addProperty("savedFrom", battle.factionName(side));
        file.addProperty("soldiers", army.started());
        file.addProperty("plan", army.plan().describe());
        JsonArray blocks = new JsonArray();
        for (Map.Entry<String, FieldBlock> e : keys.entrySet()) {
            FieldBlock b = e.getValue();
            keyOf.put(b.id(), e.getKey());
            Point at = rafasconquest.battle.BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), b.line());
            JsonObject o = new JsonObject();
            o.addProperty("key", e.getKey());
            o.addProperty("arm", b.arm().name());
            o.addProperty("duty", b.duty().name());
            o.addProperty("wing", wingName(b.wing()));
            o.addProperty("file", b.file());
            o.addProperty("files", b.files());
            o.addProperty("soldiers", b.started());
            o.addProperty("right", round(at.x()));
            o.addProperty("back", round(at.z()));
            blocks.add(o);
        }
        file.add("blocks", blocks);
        JsonObject groups = new JsonObject();
        battle.groups(side).forEach((group, ids) -> {
            JsonArray members = new JsonArray();
            ids.stream().map(keyOf::get).filter(k -> k != null).forEach(members::add);
            groups.add(group, members);
        });
        file.add("groups", groups);
        try {
            Files.createDirectories(formationsDir());
            Files.writeString(formationsDir().resolve(name + ".json"), GSON.toJson(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't save: " + e.getMessage());
        }
        return "Saved formation \"" + name + "\" from " + battle.factionName(side) + " (" + blocks.size() + " blocks)";
    }

    /** Places a side's blocks as a saved formation (matched by block name; blocks it doesn't name stay), with its groups. */
    private static String loadFormation(RecruitsBattle battle, int side, String name) {
        if (battle.started()) {
            throw new IllegalStateException("Formations can only be loaded before the battle starts");
        }
        JsonObject file;
        try {
            file = JsonParser.parseString(Files.readString(formationsDir().resolve(name + ".json"), StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalStateException("No formation \"" + name + "\"");
        }
        FieldArmy army = battle.field().army(side);
        Map<String, FieldBlock> keys = keys(army);
        int placed = 0;
        for (JsonElement e : file.getAsJsonArray("blocks")) {
            JsonObject o = e.getAsJsonObject();
            FieldBlock b = keys.get(o.get("key").getAsString());
            if (b != null) {
                Point at = rafasconquest.battle.BattlePlan.place(army.origin(), army.facingX(), army.facingZ(),
                        o.get("right").getAsDouble(), o.get("back").getAsDouble());
                battle.field().place(b, at);
                placed++;
            }
        }
        if (file.has("groups")) {
            for (Map.Entry<String, JsonElement> g : file.getAsJsonObject("groups").entrySet()) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement k : g.getValue().getAsJsonArray()) {
                    FieldBlock b = keys.get(k.getAsString());
                    if (b != null) {
                        ids.add(b.id());
                    }
                }
                battle.groups(side).put(g.getKey(), ids);
            }
        }
        return String.format(Locale.ROOT, "Loaded formation \"%s\" into %s: %d of %d blocks placed", name, battle.factionName(side),
                placed, keys.size());
    }

    // ---- the state the page draws (server thread) ----

    private static JsonObject state(@Nullable RecruitsBattle battle) {
        JsonObject o = new JsonObject();
        if (battle == null) {
            o.addProperty("phase", "none");
            return o;
        }
        FieldBattle field = battle.field();
        o.addProperty("phase", battle.isOver() || field.over() ? "over" : battle.started() ? "fighting" : "preparing");
        o.addProperty("seconds", field.seconds());
        if (field.outcome() != null) {
            o.addProperty("result", field.outcome().result());
        }
        FieldArmy first = field.army(0);
        FieldArmy second = field.army(1);
        o.addProperty("cx", (first.origin().x() + second.origin().x()) / 2);
        o.addProperty("cz", (first.origin().z() + second.origin().z()) / 2);
        o.addProperty("ax", first.facingX());
        o.addProperty("az", first.facingZ());
        JsonArray armies = new JsonArray();
        JsonArray blocks = new JsonArray();
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            JsonObject a = new JsonObject();
            a.addProperty("side", side);
            a.addProperty("faction", battle.factionName(side));
            a.addProperty("color", String.format(Locale.ROOT, "#%06x", battle.uniform(side) & 0xFFFFFF));
            a.addProperty("general", battle.general(side).name());
            a.addProperty("plan", army.plan().describe());
            a.addProperty("alive", army.alive());
            a.addProperty("started", army.started());
            a.addProperty("winged", army.winged());
            JsonObject groups = new JsonObject();
            battle.groups(side).forEach((name, ids) -> {
                JsonArray members = new JsonArray();
                ids.forEach(members::add);
                groups.add(name, members);
            });
            a.add("groups", groups);
            armies.add(a);
            for (Map.Entry<String, FieldBlock> e : keys(army).entrySet()) {
                JsonObject b = block(e.getKey(), e.getValue());
                b.addProperty("rallied", e.getValue().rallied(field.seconds()));
                blocks.add(b);
            }
        }
        o.add("armies", armies);
        o.add("blocks", blocks);
        JsonArray lines = new JsonArray();
        List<String> recent = battle.recentLines();
        recent.subList(Math.max(0, recent.size() - 30), recent.size()).forEach(lines::add);
        o.add("log", lines);
        return o;
    }

    private static JsonObject block(String key, FieldBlock b) {
        JsonObject o = new JsonObject();
        o.addProperty("id", b.id());
        o.addProperty("side", b.side());
        o.addProperty("key", key);
        o.addProperty("arm", b.arm().name());
        o.addProperty("duty", b.duty().name());
        o.addProperty("wing", wingName(b.wing()));
        o.addProperty("x", round(b.pos().x()));
        o.addProperty("z", round(b.pos().z()));
        o.addProperty("lx", round(b.line().x()));
        o.addProperty("lz", round(b.line().z()));
        o.addProperty("fx", round(b.facingX()));
        o.addProperty("fz", round(b.facingZ()));
        o.addProperty("files", b.files());
        o.addProperty("started", b.started());
        o.addProperty("alive", b.alive());
        o.addProperty("live", b.live());
        o.addProperty("charged", b.charged());
        o.addProperty("approaching", b.approaching());
        o.addProperty("braced", b.braced());
        o.addProperty("fighting", b.fighting());
        o.addProperty("inWedge", b.inWedge());
        o.addProperty("fightSeconds", b.fightSeconds());
        o.addProperty("prey", b.prey() == null ? -1 : b.prey().id());
        o.addProperty("strike", b.strikeAt() == null ? -1 : b.strikeAt().id());
        return o;
    }

    private static String wingName(int wing) {
        return switch (wing) {
            case FieldArmy.LEFT -> "left";
            case FieldArmy.RIGHT -> "right";
            default -> "centre";
        };
    }

    private static double round(double v) {
        return Math.round(v * 100) / 100.0;
    }
}
