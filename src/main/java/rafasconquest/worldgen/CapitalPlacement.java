package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import rafasconquest.core.CapitalLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/** Places Capitals at the sites from {@link CapitalLayout}: the storm-wall ring and one per outer grid cell. */
public class CapitalPlacement extends StructurePlacement {
    public static final Codec<CapitalPlacement> CODEC = RecordCodecBuilder.create(i -> placementCodec(i).apply(i, CapitalPlacement::new));

    public CapitalPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        super(locateOffset, method, frequency, salt, exclusionZone);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        WorldSettings settings = ConquestRuntime.settings();
        return settings != null && CapitalLayout.siteInChunk(state.getLevelSeed(), settings, chunkX, chunkZ).isPresent();
    }

    @Override
    public StructurePlacementType<?> type() {
        return ConquestWorldgen.CAPITAL_PLACEMENT.get();
    }
}
