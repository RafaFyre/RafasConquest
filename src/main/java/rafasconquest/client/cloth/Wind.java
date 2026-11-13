package rafasconquest.client.cloth;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

/**
 * The wind on cloth, after Physics Mod's: a steady push whose direction wanders over minutes and whose force varies
 * across the land, scaled by the weather (clear 0.1, rain +1, thunder +1.4, a third of those), plus fast gusts.
 * A slight breeze always blows; under a roof (below the highest blocking block in its column) that is all there is.
 */
final class Wind {
    private static final ImprovedNoise NOISE = new ImprovedNoise(RandomSource.create(0x5EED_C107L));
    /** Physics Mod's scale from wind strength to push, in blocks/s². */
    private static final double PUSH = 42;
    /** Gust strength, blocks/s² per unit of noise. */
    private static final double GUSTS = 16;
    /** The least wind there ever is, blocks/s² (a fifth of gravity: cloth leans about 11°). */
    private static final double BREEZE = 2;

    private Wind() {
    }

    /**
     * The wind's acceleration on cloth at a point, written into {@code out} (x, y, z). {@code flutter} speeds or slows
     * the gusts (1: Physics Mod's flags; heavy cloth answers slower); {@code share} is how much of the weather's wind
     * and gusts move the cloth (heavier, less). The breeze moves all cloth alike.
     */
    static void at(Level level, double x, double y, double z, double flutter, double share, double[] out) {
        double ticks = level.getGameTime();
        double angle = Math.toRadians(NOISE.noise(0.2412, ticks / 4000.0, 0) * 1080);
        // A slight breeze always blows, even under a roof, so cloth is never quite still.
        out[0] = Math.sin(angle) * BREEZE;
        out[1] = 0;
        out[2] = Math.cos(angle) * BREEZE;
        if (y < level.getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(x), Mth.floor(z))) {
            return;
        }
        double strength = (level.getRainLevel(1) * 1.0 + level.getThunderLevel(1) * 1.4) / 3 + 0.1;
        double big = NOISE.noise(x / 100, z / 100, ticks / 80);
        double near = NOISE.noise(x / 10, z / 10, ticks / 80 + 37);
        double jitter = NOISE.noise(x / 4, z / 4, ticks / 2 + 71);
        double force = Mth.clamp((big * 0.5 + 0.5) * 0.7 + near * 0.125 + (jitter * 0.5 + 0.5) * 0.05, 0, 1);
        double push = Math.max(BREEZE, force * strength * PUSH * share);
        out[0] = Math.sin(angle) * push;
        out[2] = Math.cos(angle) * push;
        // Gusts: a direction changing every few seconds, a strength fluttering faster.
        double gust = NOISE.noise(x, z, -11.5 + y + ticks * flutter / 7) * GUSTS * share;
        out[0] += NOISE.noise(x, z, 0.5 + y + ticks * flutter / 100) * gust;
        out[1] += NOISE.noise(x, z, 89641.5 + y + ticks * flutter / 100) * gust;
        out[2] += NOISE.noise(x, z, 1641.5 + y + ticks * flutter / 100) * gust;
    }
}
