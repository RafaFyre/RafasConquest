package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * A city's middle cell, holding the town centre. The 8 surrounding cells are {@link CityDistrictStructure}s. The city
 * exists only where the centre's surface biome maps to a {@link CityVariant}; districts check the same thing, so the
 * whole square always generates together.
 */
public class CityStructure extends Structure {
    public static final Codec<CityStructure> CODEC = simpleCodec(CityStructure::new);

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
                CityVariant.at(ctx, site.x(), site.z()).map(variant -> {
                    BlockPos pos = new BlockPos(site.x(), CityGridBuilder.ground(ctx, site.x(), site.z()).y(), site.z());
                    return new GenerationStub(pos, pieces -> CityGridBuilder.build(ctx, site, variant, 1, 1, pieces));
                }));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_STRUCTURE.get();
    }
}
