package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import rafasconquest.core.FarmLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/** Random-spread placement driven by the world's frozen spacing and separation, so it always agrees with the precompute. */
public class FarmPlacement extends StructurePlacement {
    public static final Codec<FarmPlacement> CODEC = RecordCodecBuilder.create(i -> placementCodec(i).apply(i, FarmPlacement::new));

    public FarmPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        super(locateOffset, method, frequency, salt, exclusionZone);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        WorldSettings settings = ConquestRuntime.settings();
        return settings != null && FarmLayout.isFarmChunk(state.getLevelSeed(), settings, chunkX, chunkZ);
    }

    @Override
    public StructurePlacementType<?> type() {
        return ConquestWorldgen.FARM_PLACEMENT.get();
    }
}
