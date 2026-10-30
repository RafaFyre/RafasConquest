package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * The cell of a city holding its town centre. Its other cells are {@link CityDistrictStructure}s. Used for every
 * title seat city, whatever its size. Whether the city exists, and its style, come from
 * {@link CityVariant#forSite}; districts ask the same thing, so the whole square always generates together.
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
                CityVariant.forSite(ctx, site).map(variant -> {
                    // The cell holding the town centre: its start is in this chunk.
                    CityLayout.District centre = new CityLayout.District(site, site.centerCell(), site.centerCell());
                    BlockPos pos = new BlockPos(centre.x(), CityGridBuilder.ground(ctx, centre.x(), centre.z()).y(), centre.z());
                    return new GenerationStub(pos, pieces -> CityGridBuilder.build(ctx, site, variant, site.centerCell(), site.centerCell(), pieces));
                }));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_STRUCTURE.get();
    }
}
