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
    private final ResourceKey<StructureTemplatePool> wall;
    private final ResourceKey<StructureTemplatePool> gate;
    private final BlockState road;
    private final BlockState bridge;
    private final BlockState ground;

    CityVariant(String name, BlockState road, BlockState bridge, BlockState ground) {
        this.name = name;
        this.biomes = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "city/" + name));
        this.townCenters = vanillaPool(name, "town_centers");
        this.houses = vanillaPool(name, "houses");
        this.wall = modPool("city/wall/" + name);
        this.gate = modPool("city/gate/" + name);
        this.road = road;
        this.bridge = bridge;
        this.ground = ground;
    }

    /** Top block of the city plate and of reclaimed land. */
    public BlockState ground() {
        return ground;
    }

    /** What lies just under the plate's surface. */
    public BlockState subsoil() {
        return this == DESERT ? Blocks.SANDSTONE.defaultBlockState() : Blocks.DIRT.defaultBlockState();
    }

    /** The wall's brick, as its processor list makes it; used to cap sheer edges over valleys. */
    public BlockState wallBrick() {
        return this == DESERT ? Blocks.CUT_SANDSTONE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState();
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

    /** rafasconquest:city/wall/&lt;variant&gt;: the plain wall segment, with this style's materials. */
    public ResourceKey<StructureTemplatePool> wall() {
        return wall;
    }

    /** rafasconquest:city/gate/&lt;variant&gt;: the wall segment with a gate where an avenue leaves the city. */
    public ResourceKey<StructureTemplatePool> gate() {
        return gate;
    }

    public BlockState road() {
        return road;
    }

    public BlockState bridge() {
        return bridge;
    }

    /**
     * The style of a city. A City-state city exists only where its centre biome matches a style; an Empire capital
     * always exists, falling back to plains where no style matches.
     */
    public static Optional<CityVariant> forSite(Structure.GenerationContext ctx, rafasconquest.core.CityLayout.Site site) {
        Optional<CityVariant> variant = at(ctx, site.x(), site.z());
        return variant.isPresent() || !site.capital() ? variant : Optional.of(PLAINS);
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

    private static ResourceKey<StructureTemplatePool> modPool(String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, path));
    }

    private static ResourceKey<StructureTemplatePool> vanillaPool(String variant, String pool) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath("minecraft", "village/" + variant + "/" + pool));
    }
}
