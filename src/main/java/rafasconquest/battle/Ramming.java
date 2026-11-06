package rafasconquest.battle;

/**
 * When one ship's bow strikes another, and how hard the blow throws the soldiers aboard the ship struck: plain geometry
 * over two ships' footprints on the water, for a battle's real ships.
 */
public final class Ramming {
    /** A ship on the water: its centre, unit heading (toward its bow), half its length and beam, velocity in blocks a second. */
    public record Hull(double x, double z, double dirX, double dirZ, double halfLength, double halfBeam, double vx, double vz) {
    }

    /** A bow this close to another ship's hull strikes it. */
    static final double TOUCH = 1.5;
    /** Below this closing speed, in blocks a second, a ship only comes alongside: no blow. */
    static final double MIN_CLOSING = 0.5;
    /** Ships closer to in line than this (cosine of the angle between their headings) meet head-on or bow to stern: nobody is thrown. */
    static final double IN_LINE = Math.cos(Math.toRadians(45));
    /** Soldiers are thrown this many blocks a tick for each block a second the blow drives square into their side, up to MAX_THROW. */
    static final double THROW_PER_SPEED = 0.12;
    static final double MAX_THROW = 1.2;

    private Ramming() {
    }

    /** How fast {@code a}'s bow drives into {@code b}, in blocks a second; 0 if it doesn't strike {@code b}. */
    public static double closing(Hull a, Hull b) {
        double ox = a.x() + a.dirX() * a.halfLength() - b.x();
        double oz = a.z() + a.dirZ() * a.halfLength() - b.z();
        double along = ox * b.dirX() + oz * b.dirZ();
        double across = -ox * b.dirZ() + oz * b.dirX();
        if (Math.abs(along) > b.halfLength() + TOUCH || Math.abs(across) > b.halfBeam() + TOUCH) {
            return 0;
        }
        double speed = (a.vx() - b.vx()) * a.dirX() + (a.vz() - b.vz()) * a.dirZ();
        return speed >= MIN_CLOSING ? speed : 0;
    }

    /** Whether two ships' footprints, each grown by {@code room} all round, overlap (separating axes of two rectangles). */
    public static boolean overlap(Hull a, Hull b, double room) {
        double dx = b.x() - a.x();
        double dz = b.z() - a.z();
        double[][] axes = {{a.dirX(), a.dirZ()}, {-a.dirZ(), a.dirX()}, {b.dirX(), b.dirZ()}, {-b.dirZ(), b.dirX()}};
        for (double[] u : axes) {
            double gap = Math.abs(dx * u[0] + dz * u[1]);
            if (gap > reach(a, u) + reach(b, u) + 2 * room) {
                return false;
            }
        }
        return true;
    }

    /** How far a ship's footprint reaches from its centre along unit axis {@code u}. */
    private static double reach(Hull h, double[] u) {
        return h.halfLength() * Math.abs(h.dirX() * u[0] + h.dirZ() * u[1]) + h.halfBeam() * Math.abs(-h.dirZ() * u[0] + h.dirX() * u[1]);
    }

    /**
     * The throw on the soldiers aboard {@code b} when {@code a} strikes it at {@code closing} blocks a second: a velocity
     * (x, z) in blocks a tick, along {@code a}'s heading, as strong as the part of the blow square to {@code b}'s side
     * (both hulls weigh alike, so the blow's force goes with its speed alone). Head-on and bow-to-stern blows throw nobody.
     */
    public static double[] knock(Hull a, Hull b, double closing) {
        double cos = a.dirX() * b.dirX() + a.dirZ() * b.dirZ();
        if (Math.abs(cos) > IN_LINE) {
            return new double[]{0, 0};
        }
        double square = Math.sqrt(1 - cos * cos) * closing;
        double t = Math.min(MAX_THROW, square * THROW_PER_SPEED);
        return new double[]{a.dirX() * t, a.dirZ() * t};
    }
}
