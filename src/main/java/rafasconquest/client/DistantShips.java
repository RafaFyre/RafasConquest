package rafasconquest.client;

import net.minecraft.client.Minecraft;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.core.SeaLanes;
import rafasconquest.map.BaseMap;

import java.util.List;

/**
 * The ships on the sea lanes as the client sees them. Within the render distance the server sails real Sable ships
 * (bridge/SableShips), so nothing is drawn for them here; beyond it, up to {@link #SPRITE_PAST} blocks, Distant
 * Horizons draws each as boxes of its own blocks ({@link DistantHorizonsArmies}); past that, or without Distant
 * Horizons, each is a flat sprite ({@link DistantArmyRenderer#renderShips}).
 */
final class DistantShips {
    static final double SPRITE_PAST = 2000;

    private DistantShips() {
    }

    static List<SeaLanes.Ship> now(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        BaseMap map = ClientMapCache.map();
        if (map == null || mc.level == null) {
            return List.of();
        }
        return SeaLanes.at(map.roads().shipping(), mc.level.getGameTime() + partialTick);
    }

    /** Where a ship's keel sits: a block down in the water, as the server holds the real ones. */
    static double keel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? 62 : mc.level.getSeaLevel() - 1;
    }
}
