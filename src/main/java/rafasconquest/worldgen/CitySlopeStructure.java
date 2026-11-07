package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.WorldSettings;

import rafasconquest.core.NobleAnnex;
import rafasconquest.style.CityPlan;

import java.util.List;
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
                        CityGridBuilder.Platform platform = CityGridBuilder.platform(ctx, t.city());
                        int ox = t.city().originX();
                        int oz = t.city().originZ();
                        BoundingBox box = new BoundingBox(ox + t.minX(), ctx.heightAccessor().getMinBuildHeight(), oz + t.minZ(),
                                ox + t.maxX(), ctx.heightAccessor().getMaxBuildHeight() - 1, oz + t.maxZ());
                        pieces.addPiece(new CityTerrainPiece(ox, oz, variant, plan, box, platform, t.annex()));
                        if (t.annex()) {
                            annex(t, plan.plateY(), pieces);
                        }
                    });
                }));
    }

    /**
     * What a tile of an outside noble district builds besides its ground: the district's pieces and cobbled roads whose
     * middles it holds, on the platform ({@link NobleAnnex#RISE} above the plate), and the stone path if its middle is here.
     */
    private static void annex(CityLayout.SlopeTile t, int plate, net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder pieces) {
        CityPlan.Plan styled = CityPlan.of(t.city());
        int ox = t.city().originX();
        int oz = t.city().originZ();
        int top = plate + NobleAnnex.RISE;
        for (CityPlan.Piece p : styled.pieces()) {
            if (p.file() != null && styled.inAnnex(p) && t.holds(p.centreX(), p.centreZ())) {
                rafasconquest.style.StylePiece.at(p.file(), ox + p.minX(), top, oz + p.minZ(), p.turns()).forEach(pieces::addPiece);
            }
        }
        for (int[] r : styled.roads()) {
            if (styled.inAnnex(r) && t.holds((r[0] + r[2]) / 2, (r[1] + r[3]) / 2)) {
                pieces.addPiece(new CityRoadPiece(new BoundingBox(ox + r[0], top, oz + r[1], ox + r[2], top, oz + r[3]), true));
            }
        }
        List<int[]> path = styled.path();
        if (!path.isEmpty() && t.holds(path.get(path.size() / 2)[0], path.get(path.size() / 2)[1])) {
            pieces.addPiece(new NoblePathPiece(path.stream().map(c -> new int[]{ox + c[0], oz + c[1], c[2]}).toList(), plate));
        }
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CITY_SLOPE_STRUCTURE.get();
    }
}
