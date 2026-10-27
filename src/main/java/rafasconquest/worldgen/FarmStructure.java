package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

/** A Farm sits on the surface at its chunk's middle block. The precompute calls the same code to predict it. */
public class FarmStructure extends Structure {
    public static final Codec<FarmStructure> CODEC = simpleCodec(FarmStructure::new);

    public FarmStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        ChunkPos chunk = ctx.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();
        // The structure's biome tag checks the centre; no part of the plot may be on ocean either.
        if (SettlementBiomes.touches(ctx, x - PlaceholderPiece.HALF, z - PlaceholderPiece.HALF,
                x + PlaceholderPiece.HALF, z + PlaceholderPiece.HALF, PlaceholderPiece.HALF)) {
            return Optional.empty();
        }
        int y = ctx.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
        BlockPos pos = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(pos, builder -> builder.addPiece(new FarmPiece(pos))));
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.FARM_STRUCTURE.get();
    }
}
