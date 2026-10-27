package rafasconquest.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import rafasconquest.RafasConquest;

import java.util.Optional;

/**
 * Which vanilla village style a city uses, picked from the biome at the city centre. Biomes are matched through the
 * tags rafasconquest:city/&lt;variant&gt;, checked in declaration order, so packs can add their own biomes. Buildings
 * come from the vanilla village pools of the same style.
 */
public enum CityVariant {
    DESERT("desert", Blocks.SMOOTH_SANDSTONE.defaultBlockState(), Blocks.SMOOTH_SANDSTONE.defaultBlockState(), Blocks.SAND.defaultBlockState()),
    SNOWY("snowy", Blocks.DIRT_PATH.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()),
    SAVANNA("savanna", Blocks.DIRT_PATH.defaultBlockState(), Blocks.ACACIA_PLANKS.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()),
    TAIGA("taiga", Blocks.DIRT_PATH.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()),
    PLAINS("plains", Blocks.DIRT_PATH.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState());

    private final String name;
    private final TagKey<Biome> biomes;
    private final ResourceKey<StructureTemplatePool> townCenters;
    private final ResourceKey<StructureTemplatePool> houses;
    private final BlockState road;
    private final BlockState bridge;
    private final BlockState ground;

    CityVariant(String name, BlockState road, BlockState bridge, BlockState ground) {
        this.name = name;
        this.biomes = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "city/" + name));
        this.townCenters = vanillaPool(name, "town_centers");
        this.houses = vanillaPool(name, "houses");
        this.road = road;
        this.bridge = bridge;
        this.ground = ground;
    }

    /** Top block of reclaimed land. */
    public BlockState ground() {
        return ground;
    }

    public String id() {
        return name;
    }

    public ResourceKey<StructureTemplatePool> townCenters() {
        return townCenters;
    }

    public ResourceKey<StructureTemplatePool> houses() {
        return houses;
    }

    public BlockState road() {
        return road;
    }

    public BlockState bridge() {
        return bridge;
    }

    /** The variant for the surface biome at (x, z), or empty if no city can stand there. */
    public static Optional<CityVariant> at(Structure.GenerationContext ctx, int x, int z) {
        int y = CityGridBuilder.ground(ctx, x, z).y();
        Holder<Biome> biome = ctx.biomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), ctx.randomState().sampler());
        for (CityVariant v : values()) {
            if (biome.is(v.biomes)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    private static ResourceKey<StructureTemplatePool> vanillaPool(String variant, String pool) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath("minecraft", "village/" + variant + "/" + pool));
    }
}
