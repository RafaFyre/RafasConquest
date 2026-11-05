package rafasconquest.style;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.people.Homeland;

import java.io.IOException;
import java.io.InputStream;
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
            "city/walls/corners", "city/walls/gates", "city/walls/watchtowers", "city/walls/walls",
            "city/buildings/common", "city/buildings/noble",
            "city/buildings/landmarks/mandatory/palace/small", "city/buildings/landmarks/mandatory/palace/medium",
            "city/buildings/landmarks/mandatory/palace/large", "city/buildings/landmarks/mandatory/palace/xlarge",
            "city/buildings/landmarks/mandatory/palace/xxlarge",
            "city/buildings/landmarks/mandatory/barracks/small", "city/buildings/landmarks/mandatory/barracks/medium",
            "city/buildings/landmarks/mandatory/barracks/large", "city/buildings/landmarks/mandatory/barracks/xlarge",
            "city/buildings/landmarks/style/churches", "city/buildings/landmarks/style/monuments",
            "city/buildings/landmarks/unique", "city/buildings/landmarks/unique/sword_sanctum",
            "lone/castles/small", "lone/castles/medium", "lone/castles/large", "lone/castles/grand",
            "lone/hamlets");
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
            copyShipped(root);
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[style] Could not lay out {}: {}", root(), e.toString());
        }
        reload();
    }

    private static void copyShipped(Path root) throws IOException {
        Path shipped = ModList.get().getModFileById(RafasConquest.MOD_ID).getFile().findResource(SHIPPED);
        if (!Files.isDirectory(shipped)) {
            return;
        }
        try (Stream<Path> files = Files.walk(shipped)) {
            for (Path file : (Iterable<Path>) files.filter(Files::isRegularFile)::iterator) {
                Path to = root.resolve(shipped.relativize(file).toString());
                if (!Files.exists(to)) {
                    Files.createDirectories(to.getParent());
                    try (InputStream in = Files.newInputStream(file)) {
                        Files.copy(in, to, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    /** Forgets the templates read, and reads again which homeland builds in which style. */
    public static String reload() {
        TEMPLATES.clear();
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
        for (String style : new String[]{styleOf(land), DEFAULT}) {
            String found = choose("style/" + style + "/" + slot, seed);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /** A structure from one style's own slot (no default), chosen by {@code seed}; null if that slot is empty. */
    @Nullable
    public static String pickIn(String style, String slot, long seed) {
        return choose("style/" + style + "/" + slot, seed);
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
        return choose("world/" + land.name().toLowerCase(Locale.ROOT) + "/" + slot, seed);
    }

    @Nullable
    private static String choose(String folder, long seed) {
        List<Path> files = files(root().resolve(folder));
        if (files.isEmpty()) {
            return null;
        }
        Path chosen = files.get((int) Math.floorMod(seed * 0x9E3779B97F4A7C15L >>> 17, (long) files.size()));
        return root().relativize(chosen).toString().replace('\\', '/');
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
            CompoundTag tag = NbtIo.readCompressed(file.toFile());
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
