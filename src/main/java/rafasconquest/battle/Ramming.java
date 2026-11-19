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

    /** Room a ship keeps from another it turns near or sails round, in blocks. */
    public static final double CLEAR = 3;
    /** A turn smaller than this needs no room checked (radians): a ship under way swings little. */
    static final double REAL_TURN = Math.toRadians(20);

    /** {@code h} turned about its middle by {@code angle} radians (toward +z from +x). */
    static Hull turned(Hull h, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Hull(h.x(), h.z(), h.dirX() * c - h.dirZ() * s, h.dirX() * s + h.dirZ() * c, h.halfLength(), h.halfBeam(), 0, 0);
    }

    /**
     * Whether coming round by {@code off} radians where it lies, {@code me} would strike one of {@code others} it is
     * clear of now: its hull checked halfway round and all the way (up to a quarter turn, past which a hull sweeps no
     * more). Such a ship backs astern first, to where it has room to turn.
     */
    public static boolean turnFouls(Hull me, double off, Iterable<Hull> others) {
        if (Math.abs(off) < REAL_TURN) {
            return false;
        }
        double full = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, off));
        Hull half = turned(me, full / 2);
        Hull round = turned(me, full);
        for (Hull o : others) {
            if (!overlap(me, o, CLEAR) && (overlap(half, o, CLEAR) || overlap(round, o, CLEAR))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Where {@code me}, going for (tx, tz), should make for instead to pass round the nearest of {@code others} lying across
     * its way: a point off that ship's nearer side, wide enough for its own beam; null if its way is clear.
     */
    @org.jetbrains.annotations.Nullable
    public static double[] detour(Hull me, double tx, double tz, Iterable<Hull> others) {
        double d = Math.hypot(tx - me.x(), tz - me.z());
        if (d < 1e-6) {
            return null;
        }
        double ux = (tx - me.x()) / d;
        double uz = (tz - me.z()) / d;
        double nx = -uz; // across its way
        double nz = ux;
        Hull block = null;
        double blockAlong = Double.MAX_VALUE;
        double blockAcross = 0;
        double blockWide = 0;
        for (Hull o : others) {
            double ox = o.x() - me.x();
            double oz = o.z() - me.z();
            double along = ox * ux + oz * uz;
            double across = ox * nx + oz * nz;
            // How far the other's hull reaches across the way, and how near it may lie before it is in it.
            double wide = Math.abs(o.dirX() * nx + o.dirZ() * nz) * o.halfLength() + Math.abs(-o.dirZ() * nx + o.dirX() * nz) * o.halfBeam()
                    + me.halfBeam() + CLEAR;
            if (along > 0 && along < d && Math.abs(across) < wide && along < blockAlong) {
                block = o;
                blockAlong = along;
                blockAcross = across;
                blockWide = wide;
            }
        }
        if (block == null) {
            return null;
        }
        double way = blockAcross > 0 ? -1 : 1; // round the side it lies off the way less
        return new double[]{block.x() + nx * way * blockWide, block.z() + nz * way * blockWide};
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
