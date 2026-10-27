package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * A city's town centre: a dense village-style jigsaw from the city town-centre pool, started at the city's site.
 * The 8 surrounding districts are {@link CityDistrictStructure}s. The city exists only where the centre's surface
 * biome maps to a {@link CityVariant}; districts check the same thing, so the two always agree.
 */
public class CityStructure extends Structure {
    public static final Codec<CityStructure> CODEC = simpleCodec(CityStructure::new);
    /** Deeper than vanilla's 6: the reach box, not depth, is what bounds each cell. */
    static final int JIGSAW_DEPTH = 10;

    public CityStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return Optional.empty();
        }
        return CityLayout.siteInChunk(ctx.seed(), settings, ctx.chunkPos().x, ctx.chunkPos().z).flatMap(site ->
                CityVariant.at(ctx, site.x(), site.z()).flatMap(variant ->
                        jigsaw(ctx, variant.townCenters(), site.x(), site.z())));
    }

    static Optional<GenerationStub> jigsaw(GenerationContext ctx, ResourceKey<StructureTemplatePool> poolKey, int x, int z) {
        Optional<? extends Holder<StructureTemplatePool>> pool = ctx.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL).getHolder(poolKey);
        if (pool.isEmpty()) {
            return Optional.empty();
        }
        // useExpansionHack = true, as vanilla villages use: terrain-matching streets need it for houses to attach beside them.
        return JigsawPlacement.addPieces(ctx, pool.get(), Optional.empty(), JIGSAW_DEPTH, new BlockPos(x, 0, z), true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG), CityLayout.JIGSAW_REACH);
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_STRUCTURE.get();
    }
}
