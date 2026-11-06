package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.structure.Structure;
import rafasconquest.RafasConquest;
import rafasconquest.core.CapitalLayout;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;

/**
 * Biomes no city or farm may stand on, even in part: the tag rafasconquest:no_settlement (all oceans by default), so
 * packs can add their own. Biomes are sampled at sea level over the whole footprint.
 */
public final class SettlementBiomes {
    public static final TagKey<Biome> NO_SETTLEMENT = TagKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "no_settlement"));
    /** Spacing of the city samples. Biomes are stored per 4 blocks, and oceans are far wider than this. */
    private static final int CITY_STEP = 16;

    private SettlementBiomes() {
    }

    /**
     * The check Capitals use to find land ({@link CapitalLayout.Land}): no part of a city's box on a forbidden biome,
     * after a coarser pass that rules out most ocean spots cheaply.
     */
    public static CapitalLayout.Land land(BiomeSource source, Climate.Sampler sampler, int seaLevel) {
        return (x, z) -> !cityTouches(source, sampler, seaLevel, x, z, CityLayout.CELLS_PER_SIDE, 2 * CITY_STEP)
                && !cityTouches(source, sampler, seaLevel, x, z, CityLayout.CELLS_PER_SIDE, CITY_STEP);
    }

    private static boolean cityTouches(BiomeSource source, Climate.Sampler sampler, int seaLevel, int centreX, int centreZ, int cells,
                                       int step) {
        int half = CityTerrain.size(cells) / 2;
        int originX = centreX - half;
        int originZ = centreZ - half;
        int boxMax = CityTerrain.boxMax(cells);
        return touches(source, sampler, seaLevel, originX + CityTerrain.BOX_MIN, originZ + CityTerrain.BOX_MIN,
                originX + boxMax, originZ + boxMax, step);
    }

    public static boolean touches(Structure.GenerationContext ctx, int minX, int minZ, int maxX, int maxZ, int step) {
        return touches(ctx.biomeSource(), ctx.randomState().sampler(), ctx.chunkGenerator().getSeaLevel(), minX, minZ, maxX, maxZ, step);
    }

    /**
     * True if any biome sampled every {@code step} blocks over the box, its far edges included, is forbidden.
     */
    public static boolean touches(BiomeSource source, Climate.Sampler sampler, int seaLevel, int minX, int minZ, int maxX, int maxZ, int step) {
        int y = QuartPos.fromBlock(seaLevel);
        for (int x = minX; ; x = Math.min(maxX, x + step)) {
            for (int z = minZ; ; z = Math.min(maxZ, z + step)) {
                Holder<Biome> biome = source.getNoiseBiome(QuartPos.fromBlock(x), y, QuartPos.fromBlock(z), sampler);
                if (biome.is(NO_SETTLEMENT)) {
                    return true;
                }
                if (z == maxZ) {
                    break;
                }
            }
            if (x == maxX) {
                return false;
            }
        }
    }
}
