package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/**
 * One stretch of a city's outer slope ({@link CityLayout.SlopeTile}): the carve and fill past what the edge cells
 * reach, laid by the city's own {@link CityTerrain.Plan}, so the slope runs further than one structure start can.
 */
public class CitySlopeStructure extends Structure {
    public static final com.mojang.serialization.MapCodec<CitySlopeStructure> CODEC = simpleCodec(CitySlopeStructure::new);

    public CitySlopeStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return Optional.empty();
        }
        return CityLayout.slopeTileInChunk(ctx.seed(), settings, ctx.chunkPos().x, ctx.chunkPos().z).flatMap(t ->
                CityVariant.forSite(ctx, t.city()).map(variant -> {
                    BlockPos pos = new BlockPos(t.x(), CityGridBuilder.ground(ctx, t.x(), t.z()).y(), t.z());
                    return new GenerationStub(pos, pieces -> {
                        CityTerrain.Plan plan = CityGridBuilder.terrainPlan(ctx, t.city());
                        int ox = t.city().originX();
                        int oz = t.city().originZ();
                        BoundingBox box = new BoundingBox(ox + t.minX(), ctx.heightAccessor().getMinBuildHeight(), oz + t.minZ(),
                                ox + t.maxX(), ctx.heightAccessor().getMaxBuildHeight() - 1, oz + t.maxZ());
                        pieces.addPiece(new CityTerrainPiece(ox, oz, variant, plan, box));
                    });
                }));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_SLOPE_STRUCTURE.get();
    }
}
