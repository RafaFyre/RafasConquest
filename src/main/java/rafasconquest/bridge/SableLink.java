package rafasconquest.bridge;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.fml.ModList;
import rafasconquest.RafasConquest;

/**
 * Sails real Sable ships on the sea lanes near players ({@link SableShips}). Sable is optional: this class never
 * touches its API itself, so nothing of it is loaded unless the mod is installed.
 */
public final class SableLink {
    private static boolean enabled = ModList.get() != null && ModList.get().isLoaded("sable");

    private SableLink() {
    }

    /** Each overworld tick. */
    public static void tick(ServerLevel level) {
        if (!enabled) {
            return;
        }
        try {
            SableShips.tick(level);
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; no real ships will sail", e);
        }
    }
}
