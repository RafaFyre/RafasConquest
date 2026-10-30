package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Stretches the overworld horizontally: its biomes (the temperature and humidity maps) and its land (continentalness,
 * erosion and ridges), {@code factor} times wider each way. It works on whatever overworld generator is in use, vanilla
 * or a terrain mod's, by rescaling every climate noise in its noise router when the server starts, before any level is
 * made. Each world keeps its own choice, made when it is created (in the world creation screen's RafasConquest
 * options, else the defaults), in a file in its save folder; a world made before this existed keeps vanilla's scale.
 * It is off by default when a mod known not to work with it is installed ({@link #INCOMPATIBLE}).
 */
public final class HorizontalStretch {
    /** Mods (by id) whose terrain the stretch doesn't suit; it is off by default with any of them installed. */
    public static final List<String> INCOMPATIBLE = List.of("tectonic", "terralith");
    public static final int MIN = 1;
    public static final int MAX = 4;
    public static final int DEFAULT_FACTOR = 3;
    /** The noises of the climate maps, vanilla's and their "large biomes" forms, by name. */
    private static final Set<String> CLIMATE = Set.of("temperature", "vegetation", "continentalness", "erosion", "ridge");
    private static final String FILE = "rafasconquest_world.properties";

    /** A world's choice: whether the stretch is on, and how far. */
    public record Choice(boolean on, int factor) {
        public Choice {
            factor = Math.max(MIN, Math.min(MAX, factor));
        }
    }

    /** How far the running world is stretched (1: not at all). */
    private static volatile int current = MIN;

    public static int current() {
        return current;
    }

    /** The choice made in the world creation screen for the world about to be made, if any. Client thread sets it. */
    @Nullable
    private static volatile Choice pending;

    private HorizontalStretch() {
    }

    public static void setPending(@Nullable Choice choice) {
        pending = choice;
    }

    @Nullable
    public static Choice pending() {
        return pending;
    }

    /** On, 3× wider, unless an incompatible mod is installed. */
    public static Choice defaultChoice() {
        return new Choice(incompatibleInstalled().isEmpty(), DEFAULT_FACTOR);
    }

    /** The names of the installed mods the stretch doesn't suit. */
    public static List<String> incompatibleInstalled() {
        List<String> names = new ArrayList<>();
        for (String id : INCOMPATIBLE) {
            ModList.get().getModContainerById(id).ifPresent(m -> names.add(m.getModInfo().getDisplayName()));
        }
        return names;
    }

    /**
     * When a server starts, before its levels are made: works out the world's choice (kept from before; else, for a new
     * world, the one made at its creation or the defaults; else, for a world made before this, off) and stretches the
     * overworld if it is on.
     */
    public static void onServerAboutToStart(MinecraftServer server) {
        Path root = server.getWorldPath(LevelResource.ROOT);
        Path file = root.resolve(FILE);
        Choice choice = read(file);
        if (choice == null) {
            boolean made = hasChunks(root.resolve("region"));
            Choice wanted = pending;
            choice = made ? new Choice(false, MIN) : wanted != null ? wanted : defaultChoice();
            write(file, choice);
        }
        pending = null;
        current = MIN;
        pending = null;
        if (!choice.on() || choice.factor() == MIN) {
            RafasConquest.LOGGER.info("[world] horizontal stretch off");
            return;
        }
        int stretched = apply(server, choice.factor());
        if (stretched > 0) {
            current = choice.factor();
        }
        if (stretched == 0) {
            RafasConquest.LOGGER.warn("[world] horizontal stretch: no climate noise found in the overworld's generator; left as it is");
        } else {
            RafasConquest.LOGGER.info("[world] overworld stretched {}× horizontally ({} climate noises)", choice.factor(), stretched);
        }
    }

    /** Rescales every climate noise of the overworld's noise router; returns how many were. */
    private static int apply(MinecraftServer server, int factor) {
        LevelStem stem = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.OVERWORLD);
        if (stem == null || !(stem.generator() instanceof NoiseBasedChunkGenerator generator)) {
            return 0;
        }
        NoiseGeneratorSettings settings = generator.generatorSettings().value();
        int[] count = {0};
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        // A climate noise is a "shifted_noise" over one of the climate noises, flat (no y scale). It is edited through the
        // game's own codec (out to JSON, its xz_scale divided, back again), as its class is closed to mods.
        DensityFunction.Visitor visitor = f -> {
            ResourceLocation type;
            try {
                type = BuiltInRegistries.DENSITY_FUNCTION_TYPE.getKey(f.codec().codec());
            } catch (UnsupportedOperationException e) {
                return f; // a holder link (its codec throws); mapAll has already visited what it holds
            }
            if (type == null || !type.getPath().equals("shifted_noise")) {
                return f;
            }
            JsonElement json = DensityFunction.DIRECT_CODEC.encodeStart(ops, f).result().orElse(null);
            if (json == null || !json.isJsonObject()) {
                return f;
            }
            JsonObject o = json.getAsJsonObject();
            if (!o.has("noise") || !o.get("noise").isJsonPrimitive() || !isClimate(o.get("noise").getAsString())
                    || o.has("y_scale") && o.get("y_scale").getAsDouble() != 0) {
                return f;
            }
            o.addProperty("xz_scale", o.get("xz_scale").getAsDouble() / factor);
            DensityFunction stretched = DensityFunction.DIRECT_CODEC.parse(ops, o).result().orElse(null);
            if (stretched == null) {
                return f;
            }
            count[0]++;
            return stretched;
        };
        NoiseRouter router = settings.noiseRouter().mapAll(visitor);
        if (count[0] == 0) {
            return 0;
        }
        NoiseGeneratorSettings stretched = new NoiseGeneratorSettings(settings.noiseSettings(), settings.defaultBlock(),
                settings.defaultFluid(), router, settings.surfaceRule(), settings.spawnTarget(), settings.seaLevel(),
                settings.disableMobGeneration(), settings.isAquifersEnabled(), settings.oreVeinsEnabled(),
                settings.useLegacyRandomSource());
        try {
            Field field = ObfuscationReflectionHelper.findField(NoiseBasedChunkGenerator.class, "f_64318_");
            field.set(generator, Holder.direct(stretched));
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.error("[world] couldn't stretch the overworld", e);
            return 0;
        }
        return count[0];
    }

    /** Whether a noise (by its id) is one of the climate maps', vanilla's or a "large biomes" form. */
    private static boolean isClimate(String noise) {
        String name = noise.substring(noise.indexOf(':') + 1);
        return CLIMATE.contains(name) || name.endsWith("_large") && CLIMATE.contains(name.substring(0, name.length() - 6));
    }

    private static boolean hasChunks(Path region) {
        if (!Files.isDirectory(region)) {
            return false;
        }
        try (Stream<Path> files = Files.list(region)) {
            return files.anyMatch(p -> p.toString().endsWith(".mca"));
        } catch (IOException e) {
            return true;
        }
    }

    @Nullable
    private static Choice read(Path file) {
        if (!Files.exists(file)) {
            return null;
        }
        Properties p = new Properties();
        try (Reader in = Files.newBufferedReader(file)) {
            p.load(in);
            return new Choice(Boolean.parseBoolean(p.getProperty("horizontalStretch", "false")),
                    Integer.parseInt(p.getProperty("horizontalStretchFactor", "1").trim()));
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static void write(Path file, Choice choice) {
        Properties p = new Properties();
        p.setProperty("horizontalStretch", Boolean.toString(choice.on()));
        p.setProperty("horizontalStretchFactor", Integer.toString(choice.factor()));
        try {
            Files.createDirectories(file.getParent());
            try (Writer out = Files.newBufferedWriter(file)) {
                p.store(out, "RafasConquest: this world's choices, made when it was created");
            }
        } catch (IOException e) {
            RafasConquest.LOGGER.warn("[world] couldn't save the world's choices to {}", file, e);
        }
    }
}
