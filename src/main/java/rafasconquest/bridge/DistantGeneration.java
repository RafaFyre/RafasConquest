package rafasconquest.bridge;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfig;
import net.minecraftforge.fml.ModList;
import rafasconquest.RafasConquest;

/**
 * Distant Horizons' distant generation (building far terrain from generated chunks), paused while a battle is set up
 * and fought: it keeps every core it is given busy, and the battle needs them. Set through Distant Horizons' API as an
 * override, never saved over the player's own settings, and cleared when the battle ends. Distant Horizons is
 * optional: its API is only touched (in {@link Api}) when it is installed.
 */
final class DistantGeneration {
    private static boolean paused;

    private DistantGeneration() {
    }

    static void pause() {
        if (!paused && ModList.get().isLoaded("distanthorizons")) {
            try {
                paused = Api.set(false);
                if (paused) {
                    RafasConquest.LOGGER.info("[battle] Distant Horizons' distant generation paused for the battle");
                }
            } catch (LinkageError | RuntimeException e) {
                RafasConquest.LOGGER.warn("[battle] couldn't pause Distant Horizons' distant generation", e);
            }
        }
    }

    static void resume() {
        if (paused) {
            paused = false;
            try {
                Api.clear();
                RafasConquest.LOGGER.info("[battle] Distant Horizons' distant generation resumed");
            } catch (LinkageError | RuntimeException e) {
                RafasConquest.LOGGER.warn("[battle] couldn't resume Distant Horizons' distant generation", e);
            }
        }
    }

    /** The calls into Distant Horizons' API, loaded only when it is installed. */
    private static final class Api {
        static boolean set(boolean on) {
            IDhApiConfig configs = DhApi.Delayed.configs;
            return configs != null && configs.worldGenerator().enableDistantWorldGeneration().setValue(on);
        }

        static void clear() {
            IDhApiConfig configs = DhApi.Delayed.configs;
            if (configs != null) {
                configs.worldGenerator().enableDistantWorldGeneration().clearValue();
            }
        }
    }
}
