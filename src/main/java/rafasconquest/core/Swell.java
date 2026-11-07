package rafasconquest.core;

import java.util.Locale;

/**
 * The sea's swell, worked out from where and when alone, so every ship and every player's view of it agree without
 * anything being sent. It is the Physics Mod ocean's shape and scale, from afl_ext's public "very fast procedural ocean":
 * waves running pseudo-random ways (each turned 12 radians on from the last), each sharp-crested ({@code exp(sin x - 1)})
 * and each pushing the point the next is read at towards its crest ("drag"), each 1.18× shorter, 1.07× faster and 0.8×
 * as heavy as the last. Positions are read at 0.035 of a block; the first wave is about 30 blocks long. The sea's shape
 * is {@link #WAVES} waves; its shading reads {@link #DETAIL}. Open sea stands {@link #tall} blocks from the bottom of a
 * trough to the top of the highest crest (9.1 in clear weather, 13 in rain, 14.3 in a storm), times how much of it each
 * place gets ({@link SeaCalm}). Heights are in blocks off the still waterline; time is in game ticks.
 */
public final class Swell {
    static final int WAVES = 13;
    /** Waves the water's shading reads (Physics Mod's at full ocean detail: 13 + 35). */
    static final int DETAIL = 48;
    /** Wave space per block. */
    private static final double SCALE = 0.035;
    private static final double DRAG = 0.048;
    /** How fast the waves run, in their own time a second: Physics Mod's 0.45, at its clear-weather speed (0.9). */
    // shortcut: the speed doesn't rise with rain as Physics Mod's does (its client counts time on; ours is the shared game time).
    private static final double PACE = 0.45 * 0.9;

    /** Each wave's (k·x, k·z) — wave number times its way, per block — its spin (radians a tick), weight, push (blocks), and slope share. */
    private static final double[] KX = new double[DETAIL];
    private static final double[] KZ = new double[DETAIL];
    private static final double[] SPIN = new double[DETAIL];
    private static final double[] WEIGHT = new double[DETAIL];
    private static final double[] PUSH_X = new double[DETAIL];
    private static final double[] PUSH_Z = new double[DETAIL];
    private static final double[] SLOPE_X = new double[DETAIL];
    private static final double[] SLOPE_Z = new double[DETAIL];
    private static final double WEIGHTS;
    private static final double DETAIL_WEIGHTS;

    static {
        double frequency = 6;
        double speed = 2;
        double weight = 1;
        double way = 0;
        double weights = 0;
        double all = 0;
        for (int i = 0; i < DETAIL; i++) {
            all += weight;
            if (i < WAVES) {
                weights += weight;
            }
            double sx = Math.sin(way);
            double sz = Math.cos(way);
            KX[i] = sx * frequency * SCALE;
            KZ[i] = sz * frequency * SCALE;
            SPIN[i] = PACE * speed / 20;
            WEIGHT[i] = weight;
            PUSH_X[i] = sx * weight * DRAG / SCALE;
            PUSH_Z[i] = sz * weight * DRAG / SCALE;
            SLOPE_X[i] = sx * Math.pow(weight, 0.25);
            SLOPE_Z[i] = sz * Math.pow(weight, 0.25);
            way += 12;
            frequency *= 1.18;
            weight *= 0.8;
            speed *= 1.07;
        }
        WEIGHTS = weights;
        DETAIL_WEIGHTS = all;
    }

    /** The sea lies still: the seaSwell game rule is off (set on the server, and on each client from it). */
    private static volatile boolean calm;
    /** How stormy it is (0 clear – 1 thunder), from the overworld's rain and thunder. */
    private static volatile double storm;

    private Swell() {
    }

    public static void calm(boolean still) {
        calm = still;
    }

    public static boolean calm() {
        return calm;
    }

    /** The overworld's rain and thunder levels (0–1 each), which raise the sea. */
    public static void weather(double rain, double thunder) {
        storm = rain * 0.75 + thunder * 0.25;
    }

    /** How tall open sea stands, trough to highest crest, in blocks: 13 × (0.7 + 0.4 × storminess). */
    public static double tall() {
        return 13 * (0.7 + 0.4 * storm);
    }

    /** How far open sea (full strength) stands above (or below) its still line here and now. */
    public static double height(double x, double z, double time) {
        return calm ? 0 : tall() * heightOff(phases(x, z, time), 0, 0);
    }

    /** How far the sea stands off its still line here and now, where {@code calmness} gives the wave strength (0–1). */
    public static double height(double x, double z, double time, SeaCalm calmness) {
        return calm ? 0 : calmness.strength(x, z) * height(x, z, time);
    }

    /**
     * Each wave's phase at this place and time (time may be part way through a tick), brought to within a turn: what
     * the water shader is given for the camera, so it only works with small offsets off it and stays smooth far out.
     */
    public static float[] phases(double x, double z, double time) {
        float[] p = new float[DETAIL];
        for (int i = 0; i < DETAIL; i++) {
            p[i] = (float) ((KX[i] * x + KZ[i] * z + SPIN[i] * time) % (2 * Math.PI));
        }
        return p;
    }

    /**
     * The open sea's shape (dx, dz) blocks off the place whose {@link #phases} these are, in float as the water shader
     * works it out: the weighted mean of the waves less a half, so times {@link #tall} it is in blocks. The push of each
     * wave shifts the read point the same however far off the place it is, so reading phases at the camera gives the
     * very sea the server works out.
     */
    public static double heightOff(float[] phases, double dx, double dz) {
        float qx = (float) dx;
        float qz = (float) dz;
        float h = 0;
        for (int i = 0; i < WAVES; i++) {
            float p = (float) KX[i] * qx + (float) KZ[i] * qz + phases[i];
            float s = (float) Math.exp(Math.sin(p) - 1);
            h += (float) WEIGHT[i] * s;
            float c = s * (float) Math.cos(p);
            qx -= (float) PUSH_X[i] * c;
            qz -= (float) PUSH_Z[i] * c;
        }
        return h / WEIGHTS - 0.5;
    }

    /**
     * {@link #heightOff} and its shading as GLSL, the phases coming in three mat4s (16 waves each, column by column):
     * {@code float swell(vec2 q, mat4 pa, mat4 pb, mat4 pc)} gives the shape q blocks off the camera (times the sea's
     * height gives blocks); {@code float swellDetail(vec2 q, mat4 pa, mat4 pb, mat4 pc, out vec2 slope, out vec2 moved)}
     * gives it over all {@link #DETAIL} waves, with the way the water slopes and where the waves pushed the point to.
     */
    public static String glsl() {
        StringBuilder s = new StringBuilder("float swell(vec2 q, mat4 pa, mat4 pb, mat4 pc) {\n    float h = 0.0;\n    float x;\n    float s;\n");
        for (int i = 0; i < WAVES; i++) {
            s.append(String.format(Locale.ROOT,
                    "    x = dot(q, vec2(%.8f, %.8f)) + %s; s = exp(sin(x) - 1.0); h += %.8f * s; q -= vec2(%.8f, %.8f) * (s * cos(x));\n",
                    KX[i], KZ[i], phase(i), WEIGHT[i], PUSH_X[i], PUSH_Z[i]));
        }
        s.append(String.format(Locale.ROOT, "    return h / %.8f - 0.5;\n}\n\n", WEIGHTS));
        s.append("float swellDetail(vec2 q, mat4 pa, mat4 pb, mat4 pc, out vec2 slope, out vec2 moved) {\n"
                + "    float h = 0.0;\n    vec2 d = vec2(0.0);\n    float x;\n    float s;\n    float c;\n");
        for (int i = 0; i < DETAIL; i++) {
            s.append(String.format(Locale.ROOT,
                    "    x = dot(q, vec2(%.8f, %.8f)) + %s; s = exp(sin(x) - 1.0); c = s * cos(x); h += %.8f * s; d += vec2(%.8f, %.8f) * c; q -= vec2(%.8f, %.8f) * c;\n",
                    KX[i], KZ[i], phase(i), WEIGHT[i], SLOPE_X[i], SLOPE_Z[i], PUSH_X[i], PUSH_Z[i]));
        }
        return s.append(String.format(Locale.ROOT, "    moved = q;\n    slope = -d / %.8f;\n    return h / %.8f - 0.5;\n}",
                Math.pow(DETAIL_WEIGHTS, 0.25), DETAIL_WEIGHTS)).toString();
    }

    /** Wave i's phase in the shader: the i/16th mat4, column (i%16)/4, row i%4 (as glUniformMatrix4fv lays out 16 floats). */
    private static String phase(int i) {
        return "p" + "abc".charAt(i / 16) + "[" + (i % 16) / 4 + "][" + i % 4 + "]";
    }

    /**
     * A ship floating on the swell as Physics Mod floats boats, ticked once a game tick: its waterline chases the sea
     * under its middle on a spring that lifts hard (0.241 a tick, then ×0.6) and lets it fall slowly (0.041, ×0.95),
     * gentler within half a block; it rolls and pitches toward the sea's slope one block either side and fore and aft,
     * on damped springs, and hangs on its last swing while it rides more than 0.075 above the water.
     */
    public static final class Ride {
        private double offset;
        private double rise;
        private double roll;
        private double pitch;
        private double rollSpeed;
        private double pitchSpeed;
        private double dirX;
        private double dirZ = 1;

        public void tick(double x, double z, double dirX, double dirZ, long time, SeaCalm calmness) {
            this.dirX = dirX;
            this.dirZ = dirZ;
            double target = height(x, z, time, calmness);
            double off = Math.abs(target - offset);
            double damping = Math.min(1, off / 0.5);
            if (target >= offset) {
                rise = (rise + 0.241 * damping) * 0.6;
            } else {
                rise = (rise - 0.041 * damping) * 0.95;
            }
            boolean airborne = offset - target > 0.075;
            offset += rise;
            double left = height(x + dirZ, z - dirX, time, calmness);
            double right = height(x - dirZ, z + dirX, time, calmness);
            double ahead = height(x + dirX, z + dirZ, time, calmness);
            double behind = height(x - dirX, z - dirZ, time, calmness);
            double rollTo = Math.atan2(right - left, 2);
            double pitchTo = Math.atan2(behind - ahead, 2);
            double rollHold = 1 - Math.pow(Math.min(1, Math.abs(roll) / Math.toRadians(130)), 3);
            double pitchHold = 1 - Math.pow(Math.min(1, Math.abs(pitch) / Math.toRadians(130)), 3);
            if (airborne) {
                rollSpeed *= 0.95 * rollHold;
                pitchSpeed *= 0.95 * pitchHold;
            } else {
                rollSpeed = (rollSpeed + (rollTo - roll) * 0.2) * 0.6 * rollHold;
                pitchSpeed = (pitchSpeed + (pitchTo - pitch) * 0.2) * 0.6 * pitchHold;
            }
            roll += rollSpeed;
            pitch += pitchSpeed;
        }

        /** How far its waterline stands off the still line, in blocks. */
        public double offset() {
            return offset;
        }

        /** How fast its waterline is rising, in blocks a second. */
        public double rise() {
            return rise * 20;
        }

        /** The slope (rise per block along x, then z) its deck lies along: pitched bow to stern, rolled side to side. */
        public double[] slope() {
            double fore = -Math.tan(pitch); // rise per block toward the bow
            double port = -Math.tan(roll); // rise per block toward the left
            return new double[] {fore * dirX + port * dirZ, fore * dirZ - port * dirX};
        }
    }
}
