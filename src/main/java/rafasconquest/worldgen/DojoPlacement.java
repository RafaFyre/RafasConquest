package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import rafasconquest.core.Dojos;
import rafasconquest.map.BaseMap;

import java.util.Optional;

/**
 * Places the mantra schools' dojos where the world map planned them ({@link Dojos}): the known schools' with one
 * structure set ({@code hidden} false, a pillager outpost for now), the hidden schools' with another ({@code hidden}
 * true, an underground stronghold for now).
 */
public class DojoPlacement extends StructurePlacement {
    public static final Codec<DojoPlacement> CODEC = RecordCodecBuilder.create(i -> placementCodec(i)
            .and(Codec.BOOL.fieldOf("hidden").forGetter(p -> p.hidden))
            .apply(i, DojoPlacement::new));

    private final boolean hidden;

    public DojoPlacement(Vec3i locateOffset, FrequencyReductionMethod method, float frequency, int salt, Optional<ExclusionZone> exclusionZone,
                         boolean hidden) {
        super(locateOffset, method, frequency, salt, exclusionZone);
        this.hidden = hidden;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return false;
        }
        for (Dojos.Dojo dojo : map.dojos()) {
            if (dojo.school().hidden() == hidden && dojo.x() >> 4 == chunkX && dojo.z() >> 4 == chunkZ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StructurePlacementType<?> type() {
        return ConquestWorldgen.DOJO_PLACEMENT.get();
    }
}
