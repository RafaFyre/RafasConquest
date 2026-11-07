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

/**
 * Places city centres (City-state homes and Empire capitals), the other cells' district starts around each
 * centre, or the tiles of each city's outer slope.
 */
public class CityPlacement extends StructurePlacement {
    public static final com.mojang.serialization.MapCodec<CityPlacement> CENTER_CODEC = RecordCodecBuilder.mapCodec(i -> placementCodec(i).apply(i, CityPlacement::center));
    public static final com.mojang.serialization.MapCodec<CityPlacement> DISTRICT_CODEC = RecordCodecBuilder.mapCodec(i -> placementCodec(i).apply(i, CityPlacement::district));
    public static final com.mojang.serialization.MapCodec<CityPlacement> SLOPE_CODEC = RecordCodecBuilder.mapCodec(i -> placementCodec(i).apply(i, CityPlacement::slope));

    /** What it places: 0 city centres, 1 districts, 2 slope tiles. */
    private final int kind;

    private CityPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone, int kind) {
        super(locateOffset, method, frequency, salt, exclusionZone);
        this.kind = kind;
    }

    private static CityPlacement center(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        return new CityPlacement(locateOffset, method, frequency, salt, exclusionZone, 0);
    }

    private static CityPlacement district(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        return new CityPlacement(locateOffset, method, frequency, salt, exclusionZone, 1);
    }

    private static CityPlacement slope(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        return new CityPlacement(locateOffset, method, frequency, salt, exclusionZone, 2);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        // Not gated on citiesEnabled: Empire capitals are cities in every world.
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return false;
        }
        long seed = state.getLevelSeed();
        return switch (kind) {
            case 1 -> CityLayout.districtInChunk(seed, settings, chunkX, chunkZ).isPresent();
            case 2 -> !CityLayout.slopeTilesInChunk(seed, settings, chunkX, chunkZ).isEmpty();
            default -> CityLayout.siteInChunk(seed, settings, chunkX, chunkZ).isPresent();
        };
    }

    @Override
    public StructurePlacementType<?> type() {
        return switch (kind) {
            case 1 -> ConquestWorldgen.CITY_DISTRICT_PLACEMENT.get();
            case 2 -> ConquestWorldgen.CITY_SLOPE_PLACEMENT.get();
            default -> ConquestWorldgen.CITY_PLACEMENT.get();
        };
    }
}
