package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import rafasconquest.RafasConquest;

import java.util.Optional;

/**
 * Which vanilla village style a city uses, picked from the biome at the city centre. Biomes are matched through the
 * tags rafasconquest:city/&lt;variant&gt;, checked in declaration order, so packs can add their own biomes.
 */
public enum CityVariant {
    DESERT("desert"),
    SNOWY("snowy"),
    SAVANNA("savanna"),
    TAIGA("taiga"),
    PLAINS("plains");

    private final String name;
    private final TagKey<Biome> biomes;
    private final ResourceKey<StructureTemplatePool> townCenters;
    private final ResourceKey<StructureTemplatePool> streets;

    CityVariant(String name) {
        this.name = name;
        this.biomes = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "city/" + name));
        this.townCenters = pool(RafasConquest.MOD_ID, "city/" + name + "/town_centers");
        this.streets = pool(RafasConquest.MOD_ID, "city/" + name + "/streets");
    }

    public String id() {
        return name;
    }

    public ResourceKey<StructureTemplatePool> townCenters() {
        return townCenters;
    }

    public ResourceKey<StructureTemplatePool> streets() {
        return streets;
    }

    /** The variant for the surface biome at (x, z), or empty if no city can stand there. */
    public static Optional<CityVariant> at(Structure.GenerationContext ctx, int x, int z) {
        Holder<Biome> biome = surfaceBiome(ctx, x, z);
        for (CityVariant v : values()) {
            if (biome.is(v.biomes)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    public static boolean isWater(Structure.GenerationContext ctx, int x, int z) {
        Holder<Biome> biome = surfaceBiome(ctx, x, z);
        return biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_BEACH);
    }

    private static Holder<Biome> surfaceBiome(Structure.GenerationContext ctx, int x, int z) {
        int y = ctx.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
        return ctx.biomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), ctx.randomState().sampler());
    }

    static ResourceKey<StructureTemplatePool> pool(String namespace, String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
