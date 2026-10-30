package rafasconquest.client;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.enums.config.EDhApiHorizontalQuality;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfig;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfigValue;
import rafasconquest.RafasConquest;

/**
 * Distant Horizons' horizontal quality, raised to suit a stretched world: its land is wider, so its far terrain is kept
 * at finer detail further out (HIGH at 2×, EXTREME at 3× and more). Set through Distant Horizons' API as an override,
 * never below the player's own setting and never saved over it; cleared when they leave the world. Touches Distant
 * Horizons' API, so it is only loaded when Distant Horizons is ({@link DistantHorizonsLink}).
 */
final class DistantHorizonsQuality {
    private static boolean overriding;

    private DistantHorizonsQuality() {
    }

    static void forStretch(int factor) {
        IDhApiConfig configs = DhApi.Delayed.configs;
        if (configs == null) {
            return;
        }
        IDhApiConfigValue<EDhApiHorizontalQuality> quality = configs.graphics().horizontalQuality();
        EDhApiHorizontalQuality wanted = factor >= 3 ? EDhApiHorizontalQuality.EXTREME
                : factor == 2 ? EDhApiHorizontalQuality.HIGH : null;
        EDhApiHorizontalQuality own = quality.getTrueValue();
        if (wanted == null || own != null && own.ordinal() >= wanted.ordinal()) {
            clear();
            return;
        }
        if (quality.setValue(wanted)) {
            overriding = true;
            RafasConquest.LOGGER.info("Distant Horizons: horizontal quality {} for a world stretched {}×", wanted, factor);
        }
    }

    static void clear() {
        IDhApiConfig configs = DhApi.Delayed.configs;
        if (overriding && configs != null) {
            configs.graphics().horizontalQuality().clearValue();
        }
        overriding = false;
    }
}
