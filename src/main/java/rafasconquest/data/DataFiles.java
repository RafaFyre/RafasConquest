package rafasconquest.data;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import rafasconquest.RafasConquest;
import rafasconquest.peerage.Quests;

import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * The mod's data tree, beside its structures: {@code mods/RafasConquest/quests} (one {@code .toml} per kind of quest)
 * and {@code mods/RafasConquest/dialogue} (what characters say). What the mod ships is copied out when missing, and is
 * also read first, under the folder's own files: a line or quest left out of a file falls back to the shipped one.
 * Read on server start and by {@code /pve data reload}.
 */
public final class DataFiles {
    /** What the mod ships, inside its jar. */
    static final String SHIPPED = "rafasconquest_data";

    private DataFiles() {
    }

    public static Path root() {
        return FMLPaths.GAMEDIR.get().resolve("mods").resolve("RafasConquest");
    }

    public static void init() {
        try {
            copyShipped(SHIPPED, root());
        } catch (IOException | RuntimeException e) {
            RafasConquest.LOGGER.warn("[data] Could not lay out {}: {}", root(), e.toString());
        }
        reload();
    }

    public static String reload() {
        int lines = Dialogue.load();
        int quests = Quests.load();
        return lines + " dialogue line(s) and " + quests + " kind(s) of quest read from " + root();
    }

    /** Every {@code .toml} in a folder of the tree, as its id (its path in the folder, without .toml) and contents: the shipped first, then the user's. */
    public static void each(String folder, BiConsumer<String, Config> use) {
        Path shipped = shipped(SHIPPED);
        if (shipped != null) {
            eachIn(shipped.resolve(folder), use);
        }
        Path root;
        try {
            root = root();
        } catch (RuntimeException e) {
            return; // outside the game (tests): no game folder
        }
        eachIn(root.resolve(folder), use);
    }

    private static void eachIn(Path dir, BiConsumer<String, Config> use) {
        if (!Files.isDirectory(dir)) {
            return;
        }
        List<Path> files;
        try (Stream<Path> walk = Files.walk(dir)) {
            files = walk.filter(f -> Files.isRegularFile(f) && f.getFileName().toString().endsWith(".toml")).sorted().toList();
        } catch (IOException e) {
            RafasConquest.LOGGER.warn("[data] Could not list {}: {}", dir, e.toString());
            return;
        }
        for (Path file : files) {
            String id = dir.relativize(file).toString().replace('\\', '/').replaceFirst("\\.toml$", "");
            try {
                use.accept(id, new TomlParser().parse(Files.readString(file, StandardCharsets.UTF_8)));
            } catch (IOException | RuntimeException e) {
                RafasConquest.LOGGER.warn("[data] Could not read {}: {}", file, e.toString());
            }
        }
    }

    /** A folder the mod ships; outside the game (tests) from the classpath's resources folder, or null. */
    @Nullable
    static Path shipped(String name) {
        ModList mods = ModList.get();
        Path path = null;
        if (mods != null) {
            path = mods.getModFileById(RafasConquest.MOD_ID).getFile().findResource(name);
        } else {
            java.net.URL url = DataFiles.class.getResource("/" + name);
            try {
                path = url != null && url.getProtocol().equals("file") ? Path.of(url.toURI()) : null;
            } catch (java.net.URISyntaxException e) {
                path = null;
            }
        }
        return path != null && Files.isDirectory(path) ? path : null;
    }

    /** Copies out every file of a shipped folder that is missing under root, leaving the user's own alone. */
    public static void copyShipped(String name, Path root) throws IOException {
        Path shipped = shipped(name);
        if (shipped == null) {
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
}
