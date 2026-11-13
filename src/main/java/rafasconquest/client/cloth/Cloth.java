package rafasconquest.client.cloth;

import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;

/**
 * A sheet of cloth: a grid of points tied by sticks (rows, columns and both diagonals of each cell), stepped by
 * Verlet integration and relaxed by moving each stick's ends back to its length, many times a step. The top row is
 * pinned to a frame (a banner's bar, a cape's shoulders) that moves the sheet with whatever carries it.
 * <p>
 * Tuned to Physics Mod's cloth: steps of a game tick, 25 relaxations, 93% of the speed kept a step, a 9 × 17 grid.
 * Positions are world blocks; the grid's rest shape is given in the frame's own units ("local").
 */
public final class Cloth {
    /** Whether a block is solid: the cloth is pushed out of solid blocks. */
    @FunctionalInterface
    public interface Solid {
        boolean at(int x, int y, int z);
    }

    public final int w;
    public final int h;
    /** Rest shape in the frame's units: the grid hangs from row 0 down. */
    final double[] lx;
    final double[] ly;
    final double[] lz;
    /** Where each point is, was a step ago (for speed), and where it was drawn from (for smooth drawing). */
    final double[] x;
    final double[] y;
    final double[] z;
    private final double[] ox;
    private final double[] oy;
    private final double[] oz;
    final double[] rx;
    final double[] ry;
    final double[] rz;
    private final int[] stickA;
    private final int[] stickB;
    private final double[] stickHalf;
    /** How far each relaxation moves a stick's ends toward its length: 1 for the weave, less for bend springs. */
    private final double[] stickK;
    /** Boxes the cloth stays out of, in the frame's units: a pole, a bar, a wearer's back. */
    private double[][] boxes = new double[0][];

    /**
     * A {@code w} × {@code h} grid, {@code sx} apart across and {@code sy} down, centred on x = 0, at depth {@code depth}.
     * Local y grows downward from the pinned row, as in a banner's flag.
     */
    public Cloth(int w, int h, double sx, double sy, double depth) {
        this(w, h, sx, sy, depth, 0);
    }

    /**
     * As above, with bend springs of stiffness {@code bend} (0 none, 1 full) tying each point to the one two along and
     * two down: they let the sheet curve but not crease, so it falls in a few wide folds, as thick cloth does.
     */
    public Cloth(int w, int h, double sx, double sy, double depth, double bend) {
        this.w = w;
        this.h = h;
        int n = w * h;
        lx = new double[n];
        ly = new double[n];
        lz = new double[n];
        x = new double[n];
        y = new double[n];
        z = new double[n];
        ox = new double[n];
        oy = new double[n];
        oz = new double[n];
        rx = new double[n];
        ry = new double[n];
        rz = new double[n];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int p = j * w + i;
                lx[p] = (i - (w - 1) * 0.5) * sx;
                ly[p] = j * sy;
                lz[p] = depth;
            }
        }
        int bends = bend > 0 ? Math.max(0, w - 2) * h + w * Math.max(0, h - 2) : 0;
        int sticks = (w - 1) * h + w * (h - 1) + 2 * (w - 1) * (h - 1) + bends;
        stickA = new int[sticks];
        stickB = new int[sticks];
        stickHalf = new double[sticks];
        stickK = new double[sticks];
        int s = 0;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int p = j * w + i;
                if (i < w - 1) {
                    s = stick(s, p, p + 1, 1);
                }
                if (j < h - 1) {
                    s = stick(s, p, p + w, 1);
                }
                if (i < w - 1 && j < h - 1) {
                    s = stick(s, p, p + w + 1, 1);
                    s = stick(s, p + 1, p + w, 1);
                }
                if (bends > 0 && i < w - 2) {
                    s = stick(s, p, p + 2, bend);
                }
                if (bends > 0 && j < h - 2) {
                    s = stick(s, p, p + 2 * w, bend);
                }
            }
        }
    }

    private int stick(int s, int a, int b, double k) {
        stickA[s] = a;
        stickB[s] = b;
        stickHalf[s] = Math.sqrt(sq(lx[a] - lx[b]) + sq(ly[a] - ly[b]) + sq(lz[a] - lz[b])) * 0.5;
        stickK[s] = k;
        return s + 1;
    }

    /** Boxes in local units ({minX, minY, minZ, maxX, maxY, maxZ} each) the cloth is kept out of. */
    public Cloth keepOut(double[]... boxes) {
        this.boxes = boxes;
        return this;
    }

    public boolean pinned(int p) {
        return p < w;
    }

    /** Lays the sheet out flat in its rest shape, at rest, under a frame (local to world). */
    public void place(Matrix4dc frame) {
        Vector3d v = new Vector3d();
        for (int p = 0; p < x.length; p++) {
            frame.transformPosition(v.set(lx[p], ly[p], lz[p]));
            x[p] = ox[p] = rx[p] = v.x;
            y[p] = oy[p] = ry[p] = v.y;
            z[p] = oz[p] = rz[p] = v.z;
        }
    }

    /** One step under an even acceleration (blocks/s²): the old form, all of it on every point. */
    public void step(double dt, double ax, double ay, double az, int iterations, double friction, Matrix4dc frame,
                     Matrix4dc carry, Solid solid) {
        step(dt, 0, new double[]{ax, ay, az}, 0, iterations, friction, frame, carry, solid);
    }

    /**
     * One step of {@code dt} seconds under gravity (blocks/s², down) and a wind (blocks/s²), with the pinned row on
     * {@code frame}. Of the wind, the share {@code aero} pushes each point only along its surface's facing, as air
     * presses on cloth: face-on it billows, edge-on it barely stirs; the rest pushes evenly. {@code keep} is the share
     * of its speed a point keeps a step. {@code carry} moves where points were drawn from along with the frame (last
     * frame to this), so drawing between steps stays smooth while the carrier moves; {@code solid} may be null.
     */
    public void step(double dt, double gravity, double[] wind, double aero, int iterations, double keep, Matrix4dc frame,
                     Matrix4dc carry, Solid solid) {
        double friction = keep;
        double ax = wind[0] * (1 - aero);
        double ay = wind[1] * (1 - aero) - gravity;
        double az = wind[2] * (1 - aero);
        double[] nrm = new double[3];
        Vector3d v = new Vector3d();
        int n = x.length;
        for (int p = 0; p < n; p++) {
            carry.transformPosition(v.set(x[p], y[p], z[p]));
            rx[p] = v.x;
            ry[p] = v.y;
            rz[p] = v.z;
        }
        for (int p = 0; p < w; p++) {
            frame.transformPosition(v.set(lx[p], ly[p], lz[p]));
            x[p] = ox[p] = v.x;
            y[p] = oy[p] = v.y;
            z[p] = oz[p] = v.z;
        }
        double t2 = dt * dt;
        double fraction = 1.0 / iterations;
        double[] fx = new double[n];
        double[] fy = new double[n];
        double[] fz = new double[n];
        for (int p = w; p < n; p++) {
            double px = ax;
            double py = ay;
            double pz = az;
            if (aero > 0) {
                normal(p, nrm);
                double on = (wind[0] * nrm[0] + wind[1] * nrm[1] + wind[2] * nrm[2]) * aero;
                px += nrm[0] * on;
                py += nrm[1] * on;
                pz += nrm[2] * on;
            }
            fx[p] = (px * t2 + (x[p] - ox[p]) * friction) * fraction;
            fy[p] = (py * t2 + (y[p] - oy[p]) * friction) * fraction;
            fz[p] = (pz * t2 + (z[p] - oz[p]) * friction) * fraction;
            ox[p] = x[p];
            oy[p] = y[p];
            oz[p] = z[p];
        }
        Matrix4d inverse = boxes.length == 0 ? null : frame.invert(new Matrix4d());
        for (int it = 0; it < iterations; it++) {
            for (int p = w; p < n; p++) {
                x[p] += fx[p];
                y[p] += fy[p];
                z[p] += fz[p];
            }
            for (int s = 0; s < stickA.length; s++) {
                relax(s);
            }
            if (inverse != null) {
                keepOutOfBoxes(frame, inverse, v);
            }
        }
        if (solid != null) {
            keepOutOfBlocks(solid);
        }
    }

    private void relax(int s) {
        int a = stickA[s];
        int b = stickB[s];
        boolean freeA = a >= w;
        boolean freeB = b >= w;
        if (!freeA && !freeB) {
            return;
        }
        double cx = (x[a] + x[b]) * 0.5;
        double cy = (y[a] + y[b]) * 0.5;
        double cz = (z[a] + z[b]) * 0.5;
        double dx = x[a] - x[b];
        double dy = y[a] - y[b];
        double dz = z[a] - z[b];
        double len2 = dx * dx + dy * dy + dz * dz;
        if (len2 != 0) {
            double k = stickHalf[s] / Math.sqrt(len2);
            dx *= k;
            dy *= k;
            dz *= k;
        } else {
            dz = stickHalf[s];
        }
        double k = stickK[s];
        if (freeA) {
            x[a] += (cx + dx - x[a]) * k;
            y[a] += (cy + dy - y[a]) * k;
            z[a] += (cz + dz - z[a]) * k;
        }
        if (freeB) {
            x[b] += (cx - dx - x[b]) * k;
            y[b] += (cy - dy - y[b]) * k;
            z[b] += (cz - dz - z[b]) * k;
        }
    }

    /** The unit facing of the sheet at a point (across × down; −z of the frame at rest), from its neighbours. */
    void normal(int p, double[] out) {
        int i = p % w;
        int j = p / w;
        int l = j * w + Math.max(i - 1, 0);
        int r = j * w + Math.min(i + 1, w - 1);
        int u = Math.max(j - 1, 0) * w + i;
        int d = Math.min(j + 1, h - 1) * w + i;
        double ax = x[r] - x[l], ay = y[r] - y[l], az = z[r] - z[l];
        double bx = x[d] - x[u], by = y[d] - y[u], bz = z[d] - z[u];
        double cx = ay * bz - az * by, cy = az * bx - ax * bz, cz = ax * by - ay * bx;
        double len = Math.sqrt(cx * cx + cy * cy + cz * cz);
        if (len < 1e-12) {
            out[0] = out[1] = 0;
            out[2] = 0;
            return;
        }
        out[0] = cx / len;
        out[1] = cy / len;
        out[2] = cz / len;
    }

    private void keepOutOfBoxes(Matrix4dc frame, Matrix4dc inverse, Vector3d v) {
        for (int p = w; p < x.length; p++) {
            inverse.transformPosition(v.set(x[p], y[p], z[p]));
            boolean moved = false;
            for (double[] b : boxes) {
                moved |= outOfBox(v, b[0], b[1], b[2], b[3], b[4], b[5]);
            }
            if (moved) {
                frame.transformPosition(v);
                x[p] = v.x;
                y[p] = v.y;
                z[p] = v.z;
            }
        }
    }

    private void keepOutOfBlocks(Solid solid) {
        Vector3d v = new Vector3d();
        for (int p = w; p < x.length; p++) {
            int bx = (int) Math.floor(x[p]);
            int by = (int) Math.floor(y[p]);
            int bz = (int) Math.floor(z[p]);
            if (solid.at(bx, by, bz)) {
                v.set(x[p], y[p], z[p]);
                outOfBox(v, bx, by, bz, bx + 1, by + 1, bz + 1);
                x[p] = v.x;
                y[p] = v.y;
                z[p] = v.z;
            }
        }
    }

    /** Moves a point inside a box out through the nearest face; whether it moved. */
    static boolean outOfBox(Vector3d v, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (v.x <= minX || v.x >= maxX || v.y <= minY || v.y >= maxY || v.z <= minZ || v.z >= maxZ) {
            return false;
        }
        double best = v.x - minX;
        int face = 0;
        double[] d = {v.x - minX, maxX - v.x, v.y - minY, maxY - v.y, v.z - minZ, maxZ - v.z};
        for (int f = 1; f < 6; f++) {
            if (d[f] < best) {
                best = d[f];
                face = f;
            }
        }
        switch (face) {
            case 0 -> v.x = minX;
            case 1 -> v.x = maxX;
            case 2 -> v.y = minY;
            case 3 -> v.y = maxY;
            case 4 -> v.z = minZ;
            default -> v.z = maxZ;
        }
        return true;
    }

    /** The longest stretch of any stick over its length (0 = none), for checking. */
    double worstStretch() {
        double worst = 0;
        for (int s = 0; s < stickA.length; s++) {
            int a = stickA[s];
            int b = stickB[s];
            double len = Math.sqrt(sq(x[a] - x[b]) + sq(y[a] - y[b]) + sq(z[a] - z[b]));
            worst = Math.max(worst, Math.abs(len / (stickHalf[s] * 2) - 1));
        }
        return worst;
    }

    private static double sq(double v) {
        return v * v;
    }
}
