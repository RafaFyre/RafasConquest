package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import rafasconquest.RafasConquest;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Biomes no city or farm may stand on, even in part: the tag rafasconquest:no_settlement (all oceans by default), so
 * packs can add their own. Biomes are sampled at sea level over the whole footprint.
 */
public final class SettlementBiomes {
    public static final TagKey<Biome> NO_SETTLEMENT = TagKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "no_settlement"));
    /** Spacing of the city samples. Biomes are stored per 4 blocks, and oceans are far wider than this. */
    private static final int CITY_STEP = 16;
    /** Every cell of a city asks, so remember the answer per city. Deterministic from the seed, so safe to cache. */
    private static final Map<CityKey, Boolean> CITY_BLOCKED = new ConcurrentHashMap<>();

    private record CityKey(long seed, CityLayout.Site site) {
    }

    private SettlementBiomes() {
    }

    /** True if any part of a city's flattened box lies on a forbidden biome. */
    public static boolean blocksCity(Structure.GenerationContext ctx, CityLayout.Site site) {
        return CITY_BLOCKED.computeIfAbsent(new CityKey(ctx.seed(), site), k -> touches(ctx,
                site.originX() + CityTerrain.BOX_MIN, site.originZ() + CityTerrain.BOX_MIN,
                site.originX() + CityTerrain.BOX_MAX, site.originZ() + CityTerrain.BOX_MAX, CITY_STEP));
    }

    /**
     * True if any biome sampled every {@code step} blocks over the box, its far edges included, is forbidden.
     */
    public static boolean touches(Structure.GenerationContext ctx, int minX, int minZ, int maxX, int maxZ, int step) {
        int y = QuartPos.fromBlock(ctx.chunkGenerator().getSeaLevel());
        for (int x = minX; ; x = Math.min(maxX, x + step)) {
            for (int z = minZ; ; z = Math.min(maxZ, z + step)) {
                Holder<Biome> biome = ctx.biomeSource().getNoiseBiome(QuartPos.fromBlock(x), y, QuartPos.fromBlock(z), ctx.randomState().sampler());
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
