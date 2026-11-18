package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import rafasconquest.core.SeaCalm;

import java.lang.ref.WeakReference;
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
        return calms.computeIfAbsent(level, l -> {
            // Weak: a value holding its own key keeps it in the WeakHashMap forever, and every world left with it.
            WeakReference<Level> weak = new WeakReference<>(l);
            return new SeaCalm((x, z) -> {
                Level held = weak.get();
                return held == null ? SeaCalm.UNKNOWN : depth(held, x, z);
            });
        }).at(level.getGameTime());
    }

    /** A sea calm of the level's own, not shared: for one other thread to work out on its own (it keeps no locks). */
    public static SeaCalm unshared(Level level) {
        return new SeaCalm((x, z) -> depth(level, x, z));
    }

    private static int depth(Level level, int x, int z) {
        int surface = level.getSeaLevel() - 1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, surface + 1, z);
        // The chunk if it is here now, never waited for: a ship's force-load ticket makes a chunk "there" before it has
        // loaded, and reading a block of it then held the server's tick up until it had (a sea battle's ships each read
        // five columns a tick). ClientLevel.hasChunk is always true, too, and an unloaded chunk reads as air (land).
        net.minecraft.world.level.chunk.LevelChunk chunk = level.getChunkSource().getChunkNow(x >> 4, z >> 4);
        if (chunk == null) {
            return SeaCalm.UNKNOWN;
        }
        if (!chunk.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
            return 0;
        }
        int depth = 0;
        for (pos.setY(surface); depth < SeaCalm.DEEP && chunk.getFluidState(pos).is(FluidTags.WATER); pos.move(0, -1, 0)) {
            depth++;
        }
        return depth;
    }
}
