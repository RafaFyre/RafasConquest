package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import rafasconquest.core.CapitalLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/** A Capital sits on the surface at its exact seeded site. */
public class CapitalStructure extends Structure {
    public static final Codec<CapitalStructure> CODEC = simpleCodec(CapitalStructure::new);

    public CapitalStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return Optional.empty();
        }
        return CapitalLayout.siteInChunk(ctx.seed(), settings, ctx.chunkPos().x, ctx.chunkPos().z).map(site -> {
            int y = ctx.chunkGenerator().getFirstOccupiedHeight(site.x(), site.z(), Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
            BlockPos pos = new BlockPos(site.x(), y, site.z());
            return new GenerationStub(pos, builder -> builder.addPiece(new CapitalPiece(pos)));
        });
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.CAPITAL_STRUCTURE.get();
    }
}
