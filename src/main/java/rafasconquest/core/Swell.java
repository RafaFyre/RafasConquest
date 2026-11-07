package rafasconquest.core;

import java.util.Locale;

/**
 * The sea's swell: a few long waves rolling one way across all open water, worked out from where and when alone, so
 * every ship and every player's view of it agree without anything being sent. Heights are in blocks off the still
 * waterline; time is in game ticks. The water shader draws the same waves ({@link #glsl}).
 */
public final class Swell {
    /** Each wave: height, length (blocks), period (seconds), and the way it runs (radians from +x). */
    private static final double[][] WAVES = {
            {0.66, 48, 9.0, 0.6},
            {0.36, 27, 6.5, 1.1},
            {0.18, 17, 5.0, 0.2},
    };

    /** The sea lies still: the seaSwell game rule is off (set on the server, and on each client from it). */
    private static volatile boolean calm;

    private Swell() {
    }

    public static void calm(boolean still) {
        calm = still;
    }

    public static boolean calm() {
        return calm;
    }

    /** How far the sea stands above (or below) its still line here and now. */
    public static double height(double x, double z, long time) {
        return calm ? 0 : heightOff(phases(x, z, time), 0, 0);
    }

    /** How fast the sea is rising here and now, in blocks a second. */
    public static double rise(double x, double z, long time) {
        if (calm) {
            return 0;
        }
        double r = 0;
        for (double[] w : WAVES) {
            r -= w[0] * 2 * Math.PI / w[2] * Math.cos(phase(w, x, z, time));
        }
        return r;
    }

    /** The slope of the sea here and now: its rise per block along x, then along z. */
    public static double[] slope(double x, double z, long time) {
        if (calm) {
            return new double[] {0, 0};
        }
        double sx = 0;
        double sz = 0;
        for (double[] w : WAVES) {
            double c = w[0] * 2 * Math.PI / w[1] * Math.cos(phase(w, x, z, time));
            sx += c * Math.cos(w[3]);
            sz += c * Math.sin(w[3]);
        }
        return new double[] {sx, sz};
    }

    /** Each wave's phase here and now (time may be part way through a tick), brought to within a turn. */
    public static float[] phases(double x, double z, double time) {
        float[] p = new float[WAVES.length];
        for (int i = 0; i < WAVES.length; i++) {
            p[i] = (float) (phase(WAVES[i], x, z, time) % (2 * Math.PI));
        }
        return p;
    }

    /** The sea's height (dx, dz) blocks off the place whose {@link #phases} these are: what the water shader works out. */
    public static double heightOff(float[] phases, double dx, double dz) {
        double h = 0;
        for (int i = 0; i < WAVES.length; i++) {
            double[] w = WAVES[i];
            double k = 2 * Math.PI / w[1];
            h += w[0] * Math.sin(k * (dx * Math.cos(w[3]) + dz * Math.sin(w[3])) + phases[i]);
        }
        return h;
    }

    /** {@link #heightOff} in GLSL: the sum, for a vec2 named by off (blocks off the camera) and a float[] of phases. */
    public static String glsl(String off, String phases) {
        StringBuilder s = new StringBuilder("0.0");
        for (int i = 0; i < WAVES.length; i++) {
            double[] w = WAVES[i];
            double k = 2 * Math.PI / w[1];
            s.append(String.format(Locale.ROOT, " + %.6f * sin(dot(%s, vec2(%.6f, %.6f)) + %s[%d])",
                    w[0], off, k * Math.cos(w[3]), k * Math.sin(w[3]), phases, i));
        }
        return s.toString();
    }

    /** How many waves {@link #phases} gives. */
    public static int waves() {
        return WAVES.length;
    }

    private static double phase(double[] w, double x, double z, double time) {
        double k = 2 * Math.PI / w[1];
        return k * (x * Math.cos(w[3]) + z * Math.sin(w[3])) - 2 * Math.PI / w[2] * time / 20.0;
    }
}
