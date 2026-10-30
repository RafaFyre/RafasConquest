package rafasconquest.client;

import net.minecraftforge.fml.ModList;
import rafasconquest.RafasConquest;

/**
 * Whether Distant Horizons is drawing the far battlefield. Distant Horizons is optional: this class never touches its
 * API itself, so nothing of it is loaded unless the mod is installed. When it is, the soldiers beyond the game's own
 * terrain are drawn in its terrain ({@link DistantHorizonsArmies}), so its far hills hide them.
 */
public final class DistantHorizonsLink {
    private static final String MOD_ID = "distanthorizons";
    private static boolean linked;

    private DistantHorizonsLink() {
    }

    /** At client setup: links up if Distant Horizons is installed and its API is new enough. */
    public static void link() {
        if (!ModList.get().isLoaded(MOD_ID)) {
            return;
        }
        try {
            DistantHorizonsArmies.register();
            linked = true;
            RafasConquest.LOGGER.info("Distant Horizons found: distant battles are drawn in its terrain");
        } catch (LinkageError | RuntimeException e) {
            RafasConquest.LOGGER.warn("Distant Horizons found, but its API doesn't match; distant battles are drawn without it", e);
        }
    }

    /** Whether Distant Horizons is drawing terrain, and our soldiers in it, right now. */
    public static boolean drawsFarSoldiers() {
        return linked && DistantHorizonsArmies.drawing();
    }

    /**
     * How far from the camera Distant Horizons starts fading the world into its own terrain this frame, or 0 if it
     * isn't drawing.
     */
    public static double fadeStart(float partialTick) {
        return linked ? DistantHorizonsArmies.fadeStart(partialTick) : 0;
    }

    /** The world joined is stretched {@code factor} times (1: not at all): Distant Horizons' quality is raised to suit it. */
    public static void worldStretch(int factor) {
        if (linked) {
            DistantHorizonsQuality.forStretch(factor);
        }
    }

    /** The player left the world: Distant Horizons' quality goes back to their own setting. */
    public static void leftWorld() {
        if (linked) {
            DistantHorizonsQuality.clear();
        }
    }

    /** Each frame, before the world is drawn: moves the soldiers drawn in Distant Horizons' terrain. */
    public static void update() {
        if (linked) {
            DistantHorizonsArmies.update();
        }
    }
}
