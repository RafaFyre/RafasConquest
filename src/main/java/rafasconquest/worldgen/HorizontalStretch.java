package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.WorldFeatures;

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

    /** Province sizes a world can be made with: 1, provinces 768 blocks apart; 2, twice that and a map twice as far. */
    public static final int MIN_PROVINCE_SIZE = 1;
    public static final int MAX_PROVINCE_SIZE = 2;
    public static final int DEFAULT_PROVINCE_SIZE = 2;

    /**
     * A world's choices: whether the stretch is on, how far, how big its provinces are, and whether it has the martial
     * titles (Sword Gods, Sword Kings, Sword Saints) and the mantra schools (an offensive magic system of ten schools).
     */
    public record Choice(boolean on, int factor, int provinceSize, boolean martialTitles, boolean mantraSchools) {
        public Choice {
            factor = Math.max(MIN, Math.min(MAX, factor));
            provinceSize = Math.max(MIN_PROVINCE_SIZE, Math.min(MAX_PROVINCE_SIZE, provinceSize));
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
        return new Choice(incompatibleInstalled().isEmpty(), DEFAULT_FACTOR, DEFAULT_PROVINCE_SIZE, true, true);
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
            choice = made ? new Choice(false, MIN, MIN_PROVINCE_SIZE, true, true) : wanted != null ? wanted : defaultChoice();
            write(file, choice);
        }
        pending = null;
        current = MIN;
        // The province size first: the map, drawn when the overworld loads, is laid out with it.
        ProvinceMap.setSize(choice.provinceSize());
        WorldFeatures.set(choice.martialTitles(), choice.mantraSchools());
        RafasConquest.LOGGER.info("[world] province size {}: provinces {} blocks apart", choice.provinceSize(), ProvinceMap.spacing());
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

    /**
     * Rescales every climate noise of the overworld's generator; returns how many were. Everything is edited as JSON
     * through the game's own codecs (its classes are closed to mods), where a named density function stays a name:
     * the generator's settings for the climate noises written in place, and the named functions (vanilla's
     * {@code overworld/continents} and the like) for the rest, each rebound in place so every reference to it sees the
     * change. Nothing is inlined, so the noise router keeps its size; the game rebuilds a copy of it for every height
     * lookup and every chunk, and an inlined router makes those many times slower.
     */
    private static int apply(MinecraftServer server, int factor) {
        LevelStem stem = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.OVERWORLD);
        if (stem == null || !(stem.generator() instanceof NoiseBasedChunkGenerator generator)) {
            return 0;
        }
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        int count = 0;
        try {
            // The named functions, rebound in place.
            Field value = referenceValue();
            Registry<DensityFunction> functions = server.registryAccess().registryOrThrow(Registries.DENSITY_FUNCTION);
            for (Holder.Reference<DensityFunction> ref : functions.holders().toList()) {
                JsonElement json;
                try {
                    json = DensityFunction.DIRECT_CODEC.encodeStart(ops, ref.value()).result().orElse(null);
                } catch (UnsupportedOperationException e) {
                    continue; // a bare link to another named function: that one is edited itself
                }
                int edited = json == null ? 0 : stretch(json, factor);
                if (edited == 0) {
                    continue;
                }
                DensityFunction stretched = DensityFunction.DIRECT_CODEC.parse(ops, json).result().orElse(null);
                if (stretched != null) {
                    value.set(ref, stretched);
                    count += edited;
                }
            }
            // The router's own climate noises, written in the settings rather than named (vanilla's temperature and
            // vegetation): each field that is one is replaced, and every other field kept as it is. The settings are
            // never sent through the codec whole, which would write every named function out in full.
            NoiseGeneratorSettings settings = generator.generatorSettings().value();
            NoiseRouter r = settings.noiseRouter();
            int[] edited = {0};
            // Every router field is a link: to a named function (edited above), or to its own function written in place.
            java.util.function.UnaryOperator<DensityFunction> own = f -> {
                if (!(f instanceof DensityFunctions.HolderHolder link) || link.function().unwrapKey().isPresent()) {
                    return f;
                }
                DensityFunction inner = link.function().value();
                if (!inner.getClass().getSimpleName().equals("ShiftedNoise")) {
                    return f;
                }
                JsonElement json = DensityFunction.DIRECT_CODEC.encodeStart(ops, inner).result().orElse(null);
                if (json == null || !json.isJsonObject() || !isClimateNoise(json.getAsJsonObject())) {
                    return f;
                }
                stretch(json, factor);
                DensityFunction stretched = DensityFunction.DIRECT_CODEC.parse(ops, json).result().orElse(null);
                if (stretched == null) {
                    return f;
                }
                edited[0]++;
                return new DensityFunctions.HolderHolder(Holder.direct(stretched));
            };
            NoiseRouter router = new NoiseRouter(own.apply(r.barrierNoise()), own.apply(r.fluidLevelFloodednessNoise()),
                    own.apply(r.fluidLevelSpreadNoise()), own.apply(r.lavaNoise()), own.apply(r.temperature()),
                    own.apply(r.vegetation()), own.apply(r.continents()), own.apply(r.erosion()), own.apply(r.depth()),
                    own.apply(r.ridges()), own.apply(r.initialDensityWithoutJaggedness()), own.apply(r.finalDensity()),
                    own.apply(r.veinToggle()), own.apply(r.veinRidged()), own.apply(r.veinGap()));
            if (edited[0] > 0) {
                NoiseGeneratorSettings stretched = new NoiseGeneratorSettings(settings.noiseSettings(), settings.defaultBlock(),
                        settings.defaultFluid(), router, settings.surfaceRule(), settings.spawnTarget(), settings.seaLevel(),
                        settings.disableMobGeneration(), settings.isAquifersEnabled(), settings.oreVeinsEnabled(),
                        settings.useLegacyRandomSource());
                // Named settings (a world made from a preset) are rebound in place, so the world's save keeps naming them
                // and the next load stretches the originals once more; only settings written into the save are replaced.
                if (generator.generatorSettings() instanceof Holder.Reference<NoiseGeneratorSettings> named) {
                    value.set(named, stretched);
                } else {
                    ObfuscationReflectionHelper.findField(NoiseBasedChunkGenerator.class, "f_64318_").set(generator, Holder.direct(stretched));
                }
                count += edited[0];
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.error("[world] couldn't stretch the overworld", e);
            return 0;
        }
        return count;
    }

    /**
     * Divides the xz_scale of every climate noise in a density function's JSON, in place; returns how many. A climate
     * noise is a "shifted_noise" over one of the climate noises, flat (no y scale).
     */
    private static int stretch(JsonElement json, int factor) {
        int count = 0;
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                count += stretch(e, factor);
            }
        } else if (json.isJsonObject()) {
            JsonObject o = json.getAsJsonObject();
            if (isClimateNoise(o)) {
                o.addProperty("xz_scale", o.get("xz_scale").getAsDouble() / factor);
                // The shift that wiggles the noise's edges is added after the scale, so it is scaled down alike;
                // otherwise it would be factor times stronger at the same fineness, and break biome edges into specks.
                for (String shift : new String[]{"shift_x", "shift_z"}) {
                    if (o.has(shift)) {
                        JsonObject scaled = new JsonObject();
                        scaled.addProperty("type", "minecraft:mul");
                        scaled.add("argument1", o.get(shift));
                        scaled.addProperty("argument2", 1.0 / factor);
                        o.add(shift, scaled);
                    }
                }
                return count + 1;
            }
            for (var entry : o.entrySet()) {
                count += stretch(entry.getValue(), factor);
            }
        }
        return count;
    }

    /** Whether a density function's JSON is a climate noise itself (not just holding one). */
    private static boolean isClimateNoise(JsonObject o) {
        return o.has("type") && o.get("type").isJsonPrimitive() && o.get("type").getAsString().endsWith("shifted_noise")
                && o.has("noise") && o.get("noise").isJsonPrimitive() && isClimate(o.get("noise").getAsString())
                && o.has("xz_scale") && !(o.has("y_scale") && o.get("y_scale").getAsDouble() != 0);
    }

    /** The field a registry entry's holder keeps its value in: the only one of type Object. */
    private static Field referenceValue() throws NoSuchFieldException {
        for (Field f : Holder.Reference.class.getDeclaredFields()) {
            if (f.getType() == Object.class) {
                f.setAccessible(true);
                return f;
            }
        }
        throw new NoSuchFieldException("Holder.Reference value");
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
                    Integer.parseInt(p.getProperty("horizontalStretchFactor", "1").trim()),
                    Integer.parseInt(p.getProperty("provinceSize", "1").trim()),
                    Boolean.parseBoolean(p.getProperty("martialTitles", "true")),
                    Boolean.parseBoolean(p.getProperty("mantraSchools", "true")));
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static void write(Path file, Choice choice) {
        Properties p = new Properties();
        p.setProperty("horizontalStretch", Boolean.toString(choice.on()));
        p.setProperty("horizontalStretchFactor", Integer.toString(choice.factor()));
        p.setProperty("provinceSize", Integer.toString(choice.provinceSize()));
        p.setProperty("martialTitles", Boolean.toString(choice.martialTitles()));
        p.setProperty("mantraSchools", Boolean.toString(choice.mantraSchools()));
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
