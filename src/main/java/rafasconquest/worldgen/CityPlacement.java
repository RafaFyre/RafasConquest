package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

import java.util.Optional;

/** Places city centres, or with {@code districts} set, the 8 district starts around each centre. */
public class CityPlacement extends StructurePlacement {
    public static final Codec<CityPlacement> CENTER_CODEC = RecordCodecBuilder.create(i -> placementCodec(i).apply(i, CityPlacement::center));
    public static final Codec<CityPlacement> DISTRICT_CODEC = RecordCodecBuilder.create(i -> placementCodec(i).apply(i, CityPlacement::district));

    private final boolean districts;

    private CityPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone, boolean districts) {
        super(locateOffset, method, frequency, salt, exclusionZone);
        this.districts = districts;
    }

    private static CityPlacement center(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        return new CityPlacement(locateOffset, method, frequency, salt, exclusionZone, false);
    }

    private static CityPlacement district(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        return new CityPlacement(locateOffset, method, frequency, salt, exclusionZone, true);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null || !settings.citiesEnabled()) {
            return false;
        }
        long seed = state.getLevelSeed();
        return districts
                ? CityLayout.districtInChunk(seed, settings, chunkX, chunkZ).isPresent()
                : CityLayout.siteInChunk(seed, settings, chunkX, chunkZ).isPresent();
    }

    @Override
    public StructurePlacementType<?> type() {
        return districts ? ConquestWorldgen.CITY_DISTRICT_PLACEMENT.get() : ConquestWorldgen.CITY_PLACEMENT.get();
    }
}
