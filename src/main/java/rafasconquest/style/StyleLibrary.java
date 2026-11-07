package rafasconquest.style;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.people.Homeland;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The nations' building styles, as folders of structure files beside the mods, in {@code mods/RafasConquest/structures}:
 * <ul>
 *   <li>{@code style/<name>/}: a style, every slot of it ({@link #SLOTS}) a folder of {@code .nbt} files, any of which may
 *       be built there; and its {@code style.json}, naming the homelands that build in it.</li>
 *   <li>{@code world/<land>/}: what stands in the wild of each land ({@link #WORLD_SLOTS}).</li>
 * </ul>
 * The mod ships the default style in its jar and copies it out the first time (and whatever of it is missing since,
 * never over a file that is there). Files are read from the folders as they are wanted, so a structure dropped into one
 * is built in land generated after it; {@code /pve style reload} forgets those read. A slot a style leaves empty is the
 * default style's; one empty in both is built as the mod has always built it.
 */
public final class StyleLibrary {
    public static final String DEFAULT = "default";
    /** Every slot of a style, as its folder's path. */
    public static final List<String> SLOTS = List.of(
            "city/walls/corners", "city/walls/gates", "city/walls/watchtowers", "city/walls/walls", "city/walls/junctions",
            "city/buildings/common", "city/buildings/noble",
            "city/buildings/landmarks/mandatory/palace/small", "city/buildings/landmarks/mandatory/palace/medium",
            "city/buildings/landmarks/mandatory/palace/large", "city/buildings/landmarks/mandatory/palace/xlarge",
            "city/buildings/landmarks/mandatory/palace/xxlarge",
            "city/buildings/landmarks/mandatory/barracks/small", "city/buildings/landmarks/mandatory/barracks/medium",
            "city/buildings/landmarks/mandatory/barracks/large", "city/buildings/landmarks/mandatory/barracks/xlarge",
            "city/buildings/landmarks/style/churches", "city/buildings/landmarks/style/monuments",
            "city/buildings/landmarks/unique", "city/buildings/landmarks/unique/sword_sanctum",
            "lone/castles/small", "lone/castles/medium", "lone/castles/large", "lone/castles/grand",
            "lone/hamlets",
            "ships/warships/small", "ships/warships/medium", "ships/warships/large", "ships/warships/xlarge",
            "ships/merchant/small", "ships/merchant/medium", "ships/merchant/large", "ships/merchant/xlarge");
    /** Every slot of a land's wild. */
    public static final List<String> WORLD_SLOTS = List.of("ruins", "hamlets", "dungeons");

    /** What the mod ships, inside its jar. */
    private static final String SHIPPED = "rafasconquest_structures";

    private static final Map<Path, Loaded> TEMPLATES = new ConcurrentHashMap<>();
    /** Each homeland's style, from the styles' {@code style.json}. */
    private static volatile Map<Homeland, String> styleOf = Map.of();

    /**
     * A structure file read: its size, and either the whole template (a file of one tile) or its tiles, by
     * {@link #tileKey}. Placing a template walks every block in it for each chunk it is built in, so a big file is held
     * in {@link #TILE}-block squares, each placed on its own.
     */
    private record Loaded(long modified, net.minecraft.core.Vec3i size, @Nullable StructureTemplate whole, Map<Long, StructureTemplate> tiles) {
    }

    /** The side of a tile, in blocks of the template. */
    public static final int TILE = 32;

    private StyleLibrary() {
    }

    public static Path root() {
        return FMLPaths.GAMEDIR.get().resolve("mods").resolve("RafasConquest").resolve("structures");
    }

    /** Lays out the folders, copies out what the mod ships that is missing, and reads which homeland builds in which style. */
    public static void init() {
        try {
            Path root = root();
            for (String slot : SLOTS) {
                Files.createDirectories(root.resolve("style").resolve(DEFAULT).resolve(slot));
            }
            for (Homeland land : Homeland.values()) {
                for (String slot : WORLD_SLOTS) {
                    Files.createDirectories(root.resolve("world").resolve(land.name().toLowerCase(Locale.ROOT)).resolve(slot));
                }
            }
            rafasconquest.data.DataFiles.copyShipped(SHIPPED, root);
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[style] Could not lay out {}: {}", root(), e.toString());
        }
        reload();
    }

    /** Forgets the templates read, and reads again which homeland builds in which style. */
    public static String reload() {
        TEMPLATES.clear();
        OFFSETS.clear();
        writePlacements();
        CityStyles.forget();
        CityPlan.forget();
        Map<Homeland, String> styles = new java.util.EnumMap<>(Homeland.class);
        int count = 0;
        try (Stream<Path> dirs = Files.list(root().resolve("style"))) {
            for (Path dir : (Iterable<Path>) dirs.filter(Files::isDirectory)::iterator) {
                count++;
                String name = dir.getFileName().toString();
                Path json = dir.resolve("style.json");
                if (name.equals(DEFAULT) || !Files.exists(json)) {
                    continue;
                }
                JsonObject o = JsonParser.parseString(Files.readString(json, StandardCharsets.UTF_8)).getAsJsonObject();
                JsonArray lands = o.has("homelands") ? o.getAsJsonArray("homelands") : new JsonArray();
                lands.forEach(l -> {
                    try {
                        styles.put(Homeland.valueOf(l.getAsString().toUpperCase(Locale.ROOT)), name);
                    } catch (IllegalArgumentException ignored) {
                        RafasConquest.LOGGER.warn("[style] {} names no such homeland: {}", json, l);
                    }
                });
            }
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[style] Could not read the styles: {}", e.toString());
        }
        styleOf = styles;
        return count + " style(s) in " + root() + "; " + styles.size() + " homeland(s) with their own";
    }

    /** The file in every structure folder that moves its structures up or down. */
    public static final String PLACEMENT = "placement.toml";
    private record Offsets(long modified, Map<String, Placement> byName) {
    }

    /**
     * What a {@link #PLACEMENT} says of one structure: how far up or down it is built, and how many of it the world
     * ({@code "Uniqueness Global"}) and one city ({@code "Uniqueness City"}) may hold; 0 is no limit.
     */
    public record Placement(int y, int global, int city) {
        static final Placement NONE = new Placement(0, 0, 0);
    }
    private static final Map<Path, Offsets> OFFSETS = new ConcurrentHashMap<>();

    /**
     * Writes a {@link #PLACEMENT} into every slot folder of every style and land, or brings the one there up to date:
     * a line for each of its structures, each with both uniqueness limits.
     */
    private static void writePlacements() {
        List<Path> folders = new ArrayList<>();
        for (String kind : new String[]{"style", "world"}) {
            Path parent = root().resolve(kind);
            if (!Files.isDirectory(parent)) {
                continue;
            }
            try (Stream<Path> dirs = Files.list(parent)) {
                for (Path dir : (Iterable<Path>) dirs.filter(Files::isDirectory)::iterator) {
                    for (String slot : kind.equals("style") ? SLOTS : WORLD_SLOTS) {
                        if (Files.isDirectory(dir.resolve(slot))) {
                            folders.add(dir.resolve(slot));
                        }
                    }
                }
            } catch (IOException e) {
                RafasConquest.LOGGER.warn("[style] Could not list {}: {}", parent, e.toString());
            }
        }
        for (Path folder : folders) {
            Path toml = folder.resolve(PLACEMENT);
            // A palace stands once in its city.
            int city = folder.toString().replace('\\', '/').contains("/mandatory/palace/") ? 1 : 0;
            try {
                String old = Files.exists(toml) ? Files.readString(toml, StandardCharsets.UTF_8) : HEADER;
                String text = upgrade(old, files(folder).stream()
                        .map(p -> p.getFileName().toString().replaceFirst("\\.nbt$", "")).toList(), city);
                if (!text.equals(old) || !Files.exists(toml)) {
                    Files.writeString(toml, text, StandardCharsets.UTF_8);
                }
            } catch (IOException | RuntimeException e) {
                RafasConquest.LOGGER.warn("[style] Could not write {}: {}", toml, e.toString());
            }
        }
    }

    private static final String OLD_HEADER_END = "# Read when the structure is next placed (or after /pve style reload).\n";
    private static final String LIMITS_HELP = """
            # "Uniqueness Global": how many of it the whole world may hold; "Uniqueness City": how many one city may.
            # 0 is no limit. Read after /pve style reload, for cities generated after it.
            """;
    private static final String HEADER = """
            # How far up (+) or down (-) each structure in this folder is built, from where its .nbt would stand.
            # Name a structure by its file name without .nbt, e.g.  "Example House" = { y = -10 }
            """ + OLD_HEADER_END + LIMITS_HELP;
    private static final java.util.regex.Pattern TABLE_LINE = java.util.regex.Pattern.compile("^(\\s*(?:\"[^\"]*\"|[\\w.-]+)\\s*=\\s*\\{)(.*)}(\\s*)$");
    private static final java.util.regex.Pattern NUMBER_LINE = java.util.regex.Pattern.compile("^(\\s*(?:\"[^\"]*\"|[\\w.-]+)\\s*=\\s*)(-?\\d+)(\\s*)$");

    /**
     * A placement file with both limits on every line (0, or {@code city} for "Uniqueness City", where a line has
     * none), a line for every structure it names not, and the limits explained under its header.
     */
    static String upgrade(String text, List<String> structures, int city) {
        String limits = "\"Uniqueness Global\" = 0, \"Uniqueness City\" = " + city;
        StringBuilder out = new StringBuilder();
        for (String line : text.split("\n", -1)) {
            java.util.regex.Matcher t = TABLE_LINE.matcher(line);
            java.util.regex.Matcher n = NUMBER_LINE.matcher(line);
            if (!line.trim().startsWith("#") && !line.contains("Uniqueness") && t.matches()) {
                String inner = t.group(2).trim();
                line = t.group(1) + " " + (inner.isEmpty() ? "" : inner + ", ") + limits + " }" + t.group(3);
            } else if (!line.trim().startsWith("#") && n.matches()) {
                line = n.group(1) + "{ y = " + n.group(2) + ", " + limits + " }" + n.group(3);
            }
            out.append(line).append('\n');
        }
        out.setLength(out.length() - 1); // split's last piece had no newline after it
        String result = out.toString();
        if (!result.contains("\"Uniqueness Global\": how many") && result.contains(OLD_HEADER_END)) {
            result = result.replace(OLD_HEADER_END, OLD_HEADER_END + LIMITS_HELP);
        }
        java.util.Set<String> named = readPlacements(result).keySet();
        StringBuilder missing = new StringBuilder();
        for (String name : structures) {
            if (!named.contains(name)) {
                missing.append('"').append(name).append("\" = { y = 0, ").append(limits).append(" }\n");
            }
        }
        if (missing.length() > 0 && !result.isEmpty() && !result.endsWith("\n")) {
            result += "\n";
        }
        return result + missing;
    }

    /** How far up or down a structure is built, from its folder's {@link #PLACEMENT}; 0 if it names it not. */
    public static int yOffset(String relative) {
        return placement(relative).y();
    }

    /** A structure's line in its folder's {@link #PLACEMENT}; all 0 if it names it not. */
    public static Placement placement(String relative) {
        Path file = root().resolve(relative);
        String name = file.getFileName().toString().replaceFirst("\\.nbt$", "");
        Path toml = file.resolveSibling(PLACEMENT);
        try {
            if (!Files.exists(toml)) {
                return Placement.NONE;
            }
            long modified = Files.getLastModifiedTime(toml).toMillis();
            Offsets offsets = OFFSETS.get(toml);
            if (offsets == null || offsets.modified() != modified) {
                offsets = new Offsets(modified, readPlacements(Files.readString(toml, StandardCharsets.UTF_8)));
                OFFSETS.put(toml, offsets);
            }
            return offsets.byName().getOrDefault(name, Placement.NONE);
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[style] Could not read {}: {}", toml, e.toString());
            OFFSETS.put(toml, new Offsets(-1, Map.of()));
            return Placement.NONE;
        }
    }

    /** A placement file's offsets: {@code "Name" = { y = -10 }}, or just {@code "Name" = -10}. */
    static Map<String, Integer> readOffsets(String toml) {
        Map<String, Integer> byName = new java.util.HashMap<>();
        readPlacements(toml).forEach((name, p) -> byName.put(name, p.y()));
        return byName;
    }

    /** A placement file's lines: {@code "Name" = { y = -10, "Uniqueness Global" = 1, "Uniqueness City" = 0 }}, or just {@code "Name" = -10}. */
    static Map<String, Placement> readPlacements(String toml) {
        Map<String, Placement> byName = new java.util.HashMap<>();
        com.electronwill.nightconfig.core.Config config = new com.electronwill.nightconfig.toml.TomlParser().parse(toml);
        for (com.electronwill.nightconfig.core.Config.Entry e : config.entrySet()) {
            Object v = e.getValue();
            int global = 0;
            int city = 0;
            if (v instanceof com.electronwill.nightconfig.core.Config table) {
                v = table.contains("y") ? table.get("y") : table.get("Y");
                global = count(table, "Uniqueness Global", e.getKey());
                city = count(table, "Uniqueness City", e.getKey());
            }
            if (v == null || v instanceof Number) {
                byName.put(e.getKey(), new Placement(v == null ? 0 : ((Number) v).intValue(), global, city));
            } else {
                RafasConquest.LOGGER.warn("[style] {} in a {} is not a number of blocks: {}", e.getKey(), PLACEMENT, v);
            }
        }
        return byName;
    }

    /** A limit in a placement line: a whole number, else 0 (no limit). */
    private static int count(com.electronwill.nightconfig.core.Config table, String key, String name) {
        Object v = table.get(List.of(key)); // a quoted key with spaces, read as one name, not a dotted path
        if (v instanceof Number n && n.intValue() >= 0) {
            return n.intValue();
        }
        if (v != null) {
            RafasConquest.LOGGER.warn("[style] {} of {} in a {} is not a count: {}", key, name, PLACEMENT, v);
        }
        return 0;
    }

    /** The style a homeland builds in. */
    public static String styleOf(Homeland land) {
        return styleOf.getOrDefault(land, DEFAULT);
    }

    /** Every style in use, as the homelands that build in it (the default style's: those with none of their own). */
    public static List<List<Homeland>> styleLands() {
        Map<String, List<Homeland>> by = new java.util.TreeMap<>();
        for (Homeland land : Homeland.values()) {
            by.computeIfAbsent(styleOf(land), k -> new ArrayList<>()).add(land);
        }
        return List.copyOf(by.values());
    }

    /** The structure files of a folder, in name order (the same choice for the same seed). */
    public static List<Path> files(Path folder) {
        if (!Files.isDirectory(folder)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(folder)) {
            List<Path> found = new ArrayList<>(files.filter(p -> p.toString().endsWith(".nbt")).toList());
            Collections.sort(found);
            return found;
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * A structure for a style slot of a homeland, chosen by {@code seed}: from the homeland's style, else the default
     * style; null if both leave it empty (the mod builds its own). Its path is relative to {@link #root()}.
     */
    @Nullable
    public static String pick(Homeland land, String slot, long seed) {
        return pick(land, slot, seed, f -> true);
    }

    /** {@link #pick}, of the structures {@code ok} allows. */
    @Nullable
    public static String pick(Homeland land, String slot, long seed, java.util.function.Predicate<String> ok) {
        for (String style : new String[]{styleOf(land), DEFAULT}) {
            String found = choose("style/" + style + "/" + slot, seed, ok);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /** A structure from one style's own slot (no default), chosen by {@code seed}; null if that slot is empty. */
    @Nullable
    public static String pickIn(String style, String slot, long seed) {
        return pickIn(style, slot, seed, f -> true);
    }

    /** {@link #pickIn}, of the structures {@code ok} allows. */
    @Nullable
    public static String pickIn(String style, String slot, long seed, java.util.function.Predicate<String> ok) {
        return choose("style/" + style + "/" + slot, seed, ok);
    }

    /** Every structure of a homeland's own style in a slot (none if it builds in the default style). */
    public static List<String> own(Homeland land, String slot) {
        String style = styleOf(land);
        if (style.equals(DEFAULT)) {
            return List.of();
        }
        return files(root().resolve("style").resolve(style).resolve(slot)).stream()
                .map(p -> root().relativize(p).toString().replace(java.io.File.separatorChar, '/')).toList();
    }

    /**
     * Every structure of a style slot of a homeland: its style's, else (if it has none there) the default style's. Paths
     * relative to {@link #root()}.
     */
    public static List<String> all(Homeland land, String slot) {
        for (String style : new String[]{styleOf(land), DEFAULT}) {
            List<Path> found = files(root().resolve("style").resolve(style).resolve(slot));
            if (!found.isEmpty()) {
                return found.stream().map(p -> root().relativize(p).toString().replace(java.io.File.separatorChar, '/')).toList();
            }
        }
        return List.of();
    }

    /** A structure for the wild of a land
 ({@link #WORLD_SLOTS}), chosen by {@code seed}; null if it has none. */
    @Nullable
    public static String pickWorld(Homeland land, String slot, long seed) {
        return choose("world/" + land.name().toLowerCase(Locale.ROOT) + "/" + slot, seed, f -> true);
    }

    @Nullable
    private static String choose(String folder, long seed, java.util.function.Predicate<String> ok) {
        List<String> files = files(root().resolve(folder)).stream()
                .map(p -> root().relativize(p).toString().replace('\\', '/')).filter(ok).toList();
        if (files.isEmpty()) {
            return null;
        }
        return files.get((int) Math.floorMod(seed * 0x9E3779B97F4A7C15L >>> 17, (long) files.size()));
    }

    /** A structure file of one tile (no more than {@link #TILE} blocks across), whole; null if it is bigger or can't be read. */
    @Nullable
    public static StructureTemplate template(String relative) {
        Loaded loaded = read(relative);
        return loaded == null ? null : loaded.whole();
    }

    /** A structure file's size; null if it can't be read. */
    @Nullable
    public static net.minecraft.core.Vec3i size(String relative) {
        Loaded loaded = read(relative);
        return loaded == null ? null : loaded.size();
    }

    /** Tile (i, j) of a structure file: only its blocks, at their places in the whole; null if it can't be read. */
    @Nullable
    public static StructureTemplate tile(String relative, int i, int j) {
        Loaded loaded = read(relative);
        return loaded == null ? null : loaded.whole() != null ? loaded.whole() : loaded.tiles().get(tileKey(i, j));
    }

    private static long tileKey(int i, int j) {
        return (long) i << 32 | (j & 0xffffffffL);
    }

    /** A structure file read (once, until it changes or the library reloads); null if it can't be. */
    @Nullable
    private static Loaded read(String relative) {
        Path file = root().resolve(relative);
        try {
            long modified = Files.getLastModifiedTime(file).toMillis();
            Loaded loaded = TEMPLATES.get(file);
            if (loaded != null && loaded.modified() == modified) {
                return loaded;
            }
            CompoundTag raw = NbtIo.readCompressed(file, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
            // Brought up to this game version, as vanilla does its own structure files (styles may be saved in older ones).
            CompoundTag tag = net.minecraft.util.datafix.DataFixTypes.STRUCTURE.updateToCurrentVersion(
                    net.minecraft.util.datafix.DataFixers.getDataFixer(), raw, net.minecraft.nbt.NbtUtils.getDataVersion(raw, 500));
            net.minecraft.nbt.ListTag s = tag.getList("size", net.minecraft.nbt.Tag.TAG_INT);
            net.minecraft.core.Vec3i size = new net.minecraft.core.Vec3i(s.getInt(0), s.getInt(1), s.getInt(2));
            if (size.getX() <= TILE && size.getZ() <= TILE) {
                StructureTemplate whole = new StructureTemplate();
                whole.load(BuiltInRegistries.BLOCK.asLookup(), tag);
                loaded = new Loaded(modified, size, whole, Map.of());
            } else {
                loaded = new Loaded(modified, size, null, tiles(tag, size));
            }
            TEMPLATES.put(file, loaded);
            return loaded;
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[style] Could not read {}: {}", file, e.toString());
            return null;
        }
    }

    /** A big file cut into tiles: each the whole file's size and palette, with only its own blocks and entities. */
    private static Map<Long, StructureTemplate> tiles(CompoundTag tag, net.minecraft.core.Vec3i size) {
        Map<Long, net.minecraft.nbt.ListTag> blocks = new java.util.HashMap<>();
        for (net.minecraft.nbt.Tag t : tag.getList("blocks", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            net.minecraft.nbt.ListTag pos = ((CompoundTag) t).getList("pos", net.minecraft.nbt.Tag.TAG_INT);
            blocks.computeIfAbsent(tileKey(pos.getInt(0) / TILE, pos.getInt(2) / TILE), k -> new net.minecraft.nbt.ListTag()).add(t);
        }
        Map<Long, net.minecraft.nbt.ListTag> entities = new java.util.HashMap<>();
        for (net.minecraft.nbt.Tag t : tag.getList("entities", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            net.minecraft.nbt.ListTag pos = ((CompoundTag) t).getList("blockPos", net.minecraft.nbt.Tag.TAG_INT);
            entities.computeIfAbsent(tileKey(pos.getInt(0) / TILE, pos.getInt(2) / TILE), k -> new net.minecraft.nbt.ListTag()).add(t);
        }
        CompoundTag base = tag.copy();
        base.remove("blocks");
        base.remove("entities");
        Map<Long, StructureTemplate> tiles = new java.util.HashMap<>();
        for (int i = 0; i * TILE < size.getX(); i++) {
            for (int j = 0; j * TILE < size.getZ(); j++) {
                CompoundTag one = base.copy();
                one.put("blocks", blocks.getOrDefault(tileKey(i, j), new net.minecraft.nbt.ListTag()));
                one.put("entities", entities.getOrDefault(tileKey(i, j), new net.minecraft.nbt.ListTag()));
                StructureTemplate t = new StructureTemplate();
                t.load(BuiltInRegistries.BLOCK.asLookup(), one);
                tiles.put(tileKey(i, j), t);
            }
        }
        return tiles;
    }

    /**
     * Moves a structure saved in game with a structure block (as {@code rafasconquest:<name>}, into the world's
     * {@code generated} folder) into a slot of a style.
     */
    public static String save(Path worldGenerated, String style, String slot, String name) {
        if (!SLOTS.contains(slot) && !slot.startsWith("world/")) {
            return "No such slot: " + slot;
        }
        Path from = worldGenerated.resolve(RafasConquest.MOD_ID).resolve("structures").resolve(name + ".nbt");
        if (!Files.exists(from)) {
            return "No structure saved as " + RafasConquest.MOD_ID + ":" + name + " (save it with a structure block first)";
        }
        Path to = (slot.startsWith("world/") ? root().resolve(slot) : root().resolve("style").resolve(style).resolve(slot)).resolve(name + ".nbt");
        try {
            Files.createDirectories(to.getParent());
            Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return "Could not move it: " + e.getMessage();
        }
        return "Saved to " + to;
    }
}
