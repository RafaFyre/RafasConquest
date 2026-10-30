package rafasconquest.client;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.enums.config.EDhApiHorizontalQuality;
import com.seibel.distanthorizons.api.enums.worldGeneration.EDhApiGeneratorPlan;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfig;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfigValue;
import rafasconquest.RafasConquest;

/**
 * Distant Horizons, set up for a stretched world. Set through Distant Horizons' API as overrides, never saved over the
 * player's own settings, and cleared when they leave the world:
 * <ul>
 *   <li><b>Generator plan</b> {@code CHUNKS_ONLY}: Distant Horizons' quick "rough surface" pass guesses each biome's
 *       blocks and leaves out trees, and in a stretched world its far terrain comes out coarse and wrongly coloured
 *       (savannas grey as stone). Far terrain is built only from properly generated chunks instead.</li>
 *   <li><b>Horizontal quality</b> raised to HIGH, never below the player's own setting: the land is wider, so it is kept
 *       at finer detail a little further out. No higher: EXTREME over the whole horizon keeps the machine flat out.</li>
 * </ul>
 * Touches Distant Horizons' API, so it is only loaded when Distant Horizons is ({@link DistantHorizonsLink}).
 */
final class DistantHorizonsQuality {
    private static boolean overridingQuality;
    private static boolean overridingPlan;

    private DistantHorizonsQuality() {
    }

    static void forStretch(int factor) {
        IDhApiConfig configs = DhApi.Delayed.configs;
        if (configs == null) {
            return;
        }
        clear();
        if (factor < 2) {
            return;
        }
        IDhApiConfigValue<EDhApiGeneratorPlan> plan = configs.worldGenerator().GeneratorPlan();
        EDhApiGeneratorPlan ownPlan = plan.getTrueValue();
        if (ownPlan != null && ownPlan.surfaceGenEnabled && ownPlan.chunkGenEnabled && plan.setValue(EDhApiGeneratorPlan.CHUNKS_ONLY)) {
            overridingPlan = true;
            RafasConquest.LOGGER.info("Distant Horizons: far terrain built from chunks only (no rough surface) for a world stretched {}×", factor);
        }
        IDhApiConfigValue<EDhApiHorizontalQuality> quality = configs.graphics().horizontalQuality();
        EDhApiHorizontalQuality own = quality.getTrueValue();
        if ((own == null || own.ordinal() < EDhApiHorizontalQuality.HIGH.ordinal()) && quality.setValue(EDhApiHorizontalQuality.HIGH)) {
            overridingQuality = true;
            RafasConquest.LOGGER.info("Distant Horizons: horizontal quality HIGH for a world stretched {}×", factor);
        }
    }

    static void clear() {
        IDhApiConfig configs = DhApi.Delayed.configs;
        if (configs != null) {
            if (overridingQuality) {
                configs.graphics().horizontalQuality().clearValue();
            }
            if (overridingPlan) {
                configs.worldGenerator().GeneratorPlan().clearValue();
            }
        }
        overridingQuality = false;
        overridingPlan = false;
    }
}
