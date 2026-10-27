package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * One of a city's 8 outer cells: its share of the street grid and plots. Uses the variant of the city centre, so the
 * whole city shares one style, and never skips, so the square is always complete.
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
        return CityLayout.districtInChunk(ctx.seed(), settings, ctx.chunkPos().x, ctx.chunkPos().z).flatMap(d ->
                CityVariant.forSite(ctx, d.city()).map(variant -> {
                    BlockPos pos = new BlockPos(d.x(), CityGridBuilder.ground(ctx, d.x(), d.z()).y(), d.z());
                    return new GenerationStub(pos, pieces -> CityGridBuilder.build(ctx, d.city(), variant, d.cellX(), d.cellZ(), pieces));
                }));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_DISTRICT_STRUCTURE.get();
    }
}
