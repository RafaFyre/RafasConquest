package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import rafasconquest.core.SeaCalm;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Each level's {@link SeaCalm}, read from its blocks: the server's for ships riding the swell, the client's for the water
 * drawn. A column is sea where the block just under the still surface is water with nothing solid on top (a pier deck
 * makes it land); unloaded columns count as open sea for now, and are looked at again once loaded, so nothing is loaded
 * to look and nothing guessed is remembered.
 */
public final class Sea {
    private static final Map<Level, SeaCalm> calms = new WeakHashMap<>();

    private Sea() {
    }

    /** The level's sea calm, as of now. */
    public static SeaCalm calm(Level level) {
        return calms.computeIfAbsent(level, l -> new SeaCalm((x, z) -> depth(l, x, z))).at(level.getGameTime());
    }

    private static int depth(Level level, int x, int z) {
        int surface = level.getSeaLevel() - 1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, surface + 1, z);
        if (!level.hasChunkAt(pos)) {
            return SeaCalm.UNKNOWN;
        }
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
            return 0;
        }
        int depth = 0;
        for (pos.setY(surface); depth < SeaCalm.DEEP && level.getFluidState(pos).is(FluidTags.WATER); pos.move(0, -1, 0)) {
            depth++;
        }
        return depth;
    }
}
