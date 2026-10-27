package rafasconquest.dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Dev tool: writes template pools from the live registry as datapack JSON. The city pools are derived from the
 * vanilla village pools here, so re-running {@code /pve debug exportpools} regenerates them after weight changes;
 * copy run/rafasconquest-export/city/data into src/main/resources/data.
 */
public final class PoolExporter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final String[] VARIANTS = {"plains", "desert", "savanna", "snowy", "taiga"};

    // Density tuning. Crossroads and squares add street frontage per area; every house slot gets a building.
    private static final int JUNCTION_WEIGHT_MULTIPLIER = 3;
    private static final int STRAIGHT_WEIGHT_MULTIPLIER = 1;
    /** Farms and pens cover a lot of ground for one building; their weight is divided by this (minimum 1). */
    private static final int FARM_WEIGHT_DIVISOR = 1;

    private PoolExporter() {
    }

    public static JsonElement encode(MinecraftServer server, ResourceLocation id) {
        Registry<StructureTemplatePool> pools = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool pool = pools.get(id);
        if (pool == null) {
            throw new IllegalArgumentException("No template pool " + id);
        }
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        return StructureTemplatePool.DIRECT_CODEC.encodeStart(ops, pool).getOrThrow(false, msg -> {
        });
    }

    public static void write(Path file, JsonElement json) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, GSON.toJson(json) + "\n");
    }

    /** Dumps the vanilla village pools this mod builds on, for reference. Returns the number of files written. */
    public static int exportVanilla(MinecraftServer server, Path root) throws IOException {
        int n = 0;
        for (String v : VARIANTS) {
            for (String pool : new String[]{"town_centers", "streets", "houses", "terminators"}) {
                write(root.resolve("vanilla/" + v + "/" + pool + ".json"), encode(server, vanilla(v, pool)));
                n++;
            }
        }
        return n;
    }

    /** Writes rafasconquest:city/&lt;variant&gt;/{town_centers,streets,houses}. Returns the number of files written. */
    public static int exportCity(MinecraftServer server, Path root) throws IOException {
        Path dir = root.resolve("city/data/rafasconquest/worldgen/template_pool/city");
        int n = 0;
        for (String v : VARIANTS) {
            for (String pool : new String[]{"town_centers", "streets", "houses"}) {
                JsonObject json = encode(server, vanilla(v, pool)).getAsJsonObject();
                JsonArray out = new JsonArray();
                for (JsonElement e : json.getAsJsonArray("elements")) {
                    JsonObject entry = e.getAsJsonObject();
                    JsonObject element = entry.getAsJsonObject("element");
                    String type = element.get("element_type").getAsString();
                    String location = element.has("location") ? element.get("location").getAsString() : "";
                    int weight = cityWeight(pool, type, location, entry.get("weight").getAsInt());
                    if (weight <= 0) {
                        continue;
                    }
                    // Only pieces that link onward to streets or houses need the remapping element type.
                    if (!pool.equals("houses")) {
                        element.addProperty("element_type", cityElementType(type));
                    }
                    entry.addProperty("weight", weight);
                    out.add(entry);
                }
                json.add("elements", out);
                write(dir.resolve(v + "/" + pool + ".json"), json);
                n++;
            }
        }
        return n;
    }

    private static int cityWeight(String pool, String type, String location, int weight) {
        if (location.contains("/zombie/") || type.equals("minecraft:empty_pool_element")) {
            return 0;
        }
        String piece = location.substring(location.lastIndexOf('/') + 1);
        if (pool.equals("streets")) {
            boolean junction = piece.startsWith("crossroad") || piece.startsWith("square");
            boolean straight = piece.startsWith("straight");
            return weight * (junction ? JUNCTION_WEIGHT_MULTIPLIER : straight ? STRAIGHT_WEIGHT_MULTIPLIER : 1);
        }
        if (pool.equals("houses") && (piece.contains("farm") || piece.contains("animal_pen"))) {
            return Math.max(1, weight / FARM_WEIGHT_DIVISOR);
        }
        return weight;
    }

    private static String cityElementType(String vanillaType) {
        return switch (vanillaType) {
            case "minecraft:legacy_single_pool_element" -> "rafasconquest:city_legacy_single";
            case "minecraft:single_pool_element" -> "rafasconquest:city_single";
            default -> vanillaType;
        };
    }

    private static ResourceLocation vanilla(String variant, String pool) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", "village/" + variant + "/" + pool);
    }
}
