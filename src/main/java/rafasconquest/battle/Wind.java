package rafasconquest.battle;

/**
 * A sea battle's wind, as in Empire: it starts blowing one way and turns slowly clockwise or anticlockwise as the battle
 * goes on. Directions are unit vectors (x, z), angles as {@code atan2(x, z)}, as ships' headings are. A sailship can't sail
 * within {@link #NO_GO} of where the wind comes from, and its pace depends on its angle to the wind ({@link #pace}).
 */
public final class Wind {
    /** Radians a second the wind turns: once round in twenty minutes. */
    // shortcut: a fixed rate and no gusts or calms; give it strength and change when the AI fights for the weather gauge.
    static final double TURN = 2 * Math.PI / 1200;
    /** A sailship sails no closer than this to where the wind comes from (close-hauled). */
    static final double NO_GO = Math.toRadians(45);
    /** Pace by angle off the wind's run, in degrees (0: running before it, 180: straight into it): a beam reach is fastest. */
    private static final double[][] POLAR = {{0, 0.75}, {45, 0.95}, {90, 1}, {135, 0.6}, {150, 0.1}, {180, 0.1}};

    private Wind() {
    }

    /** The way the wind blows (toward), as an angle, {@code seconds} into a battle whose wind began at {@code start}, turning {@code turn} (+1 or -1). */
    static double at(double start, int turn, int seconds) {
        return start + turn * TURN * seconds;
    }

    /** A sailship's pace, times its full pace, heading (x, z) in wind blowing toward angle {@code wind}. */
    public static double pace(double x, double z, double wind) {
        double off = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, x * Math.sin(wind) + z * Math.cos(wind)))));
        for (int i = 1; i < POLAR.length; i++) {
            if (off <= POLAR[i][0]) {
                double t = (off - POLAR[i - 1][0]) / (POLAR[i][0] - POLAR[i - 1][0]);
                return POLAR[i - 1][1] + t * (POLAR[i][1] - POLAR[i - 1][1]);
            }
        }
        return POLAR[POLAR.length - 1][1];
    }

    /**
     * The heading a sailship facing (faceX, faceZ) takes to make for (wantX, wantZ): that one if it can sail it, else
     * close-hauled on the tack it is on (the side of the wind it faces), so it beats upwind in long boards and comes
     * about only once its mark lies off its other bow outside the no-go.
     */
    public static double[] steer(double wantX, double wantZ, double faceX, double faceZ, double wind) {
        double wx = Math.sin(wind);
        double wz = Math.cos(wind);
        if (wantX * wx + wantZ * wz >= -Math.cos(NO_GO)) {
            return new double[]{wantX, wantZ};
        }
        double side = Math.signum(wx * faceZ - wz * faceX);
        if (side == 0) {
            side = Math.signum(wx * wantZ - wz * wantX);
        }
        double a = wind - (side >= 0 ? 1 : -1) * (Math.PI - NO_GO); // cross > 0: turned the negative way from the wind
        return new double[]{Math.sin(a), Math.cos(a)};
    }

    /** Where the wind comes from, as a compass point (north -z, east +x), for players to read. */
    public static String from(double wind) {
        String[] points = {"north", "north-east", "east", "south-east", "south", "south-west", "west", "north-west"};
        double bearing = Math.toDegrees(Math.atan2(-Math.sin(wind), Math.cos(wind))); // from: opposite of toward; north is -z
        int i = (int) Math.round(((bearing % 360) + 360) % 360 / 45) % 8;
        return points[i];
    }
}
