package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

/** Places the countryside's settlements where the world map planned them ({@link CountryIndex}). */
public class CountryPlacement extends StructurePlacement {
    public static final Codec<CountryPlacement> CODEC = RecordCodecBuilder.create(i -> placementCodec(i).apply(i, CountryPlacement::new));

    public CountryPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        super(locateOffset, method, frequency, salt, exclusionZone);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        return ConquestRuntime.countryside().inChunk(chunkX, chunkZ).isPresent();
    }

    @Override
    public StructurePlacementType<?> type() {
        return ConquestWorldgen.COUNTRY_PLACEMENT.get();
    }
}
