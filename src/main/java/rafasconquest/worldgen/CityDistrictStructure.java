package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * One of a city's 8 outer districts: a dense street network started from the city street pool at the district's
 * cell centre. Uses the variant of the city centre, so the whole city shares one style. Skipped over open water.
 */
public class CityDistrictStructure extends Structure {
    public static final Codec<CityDistrictStructure> CODEC = simpleCodec(CityDistrictStructure::new);

    public CityDistrictStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return Optional.empty();
        }
        return CityLayout.districtInChunk(ctx.seed(), settings, ctx.chunkPos().x, ctx.chunkPos().z)
                .filter(d -> !CityVariant.isWater(ctx, d.x(), d.z()))
                .flatMap(d -> CityVariant.at(ctx, d.city().x(), d.city().z()).flatMap(variant ->
                        CityStructure.jigsaw(ctx, variant.streets(), d.x(), d.z())));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_DISTRICT_STRUCTURE.get();
    }
}
