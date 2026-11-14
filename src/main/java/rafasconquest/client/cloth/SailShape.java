package rafasconquest.client.cloth;

/**
 * A square sail's shape, with no physics: a set belly plus travelling ripples, after the way Total War's sails move.
 * The sheet is {@code w} by {@code h} blocks; a point on it is (x across, y down from the top edge), both in blocks.
 * Out comes (across, up, forward): the top edge stays on its yard, the sides and the foot's corners stay in the plane,
 * and the belly is deepest a little below the middle, where it also draws the foot up.
 *
 * @param fill how full the wind holds it, 0 slack to 1 full
 * @param flap how much it ripples, 0 still to 1 flogging
 * @param time seconds, the ripples' clock
 */
record SailShape(int w, int h, double fill, double flap, double time) {
    /** The deepest belly, as a share of the sheet's smaller side. */
    private static final double DEPTH = 0.22;
    /** Ripple height at the foot, blocks, flogging. */
    private static final double RIPPLE = 0.18;

    /** The point at (x, y) on the sheet, into {@code out} (across, up, forward). */
    void point(double x, double y, double[] out) {
        double s = x / w;
        double t = y / h;
        double across = 1 - (2 * s - 1) * (2 * s - 1);
        double belly = fill * DEPTH * Math.min(w, h) * across * Math.sin(Math.PI * t / 1.6);
        // Ripples run aft and down, none at the yard; more as the cloth hangs looser.
        double hang = Math.sin(Math.PI * t / 2) * (0.25 + 0.75 * across);
        double ripple = RIPPLE * flap * hang * (Math.sin(x / 3.1 * 2 * Math.PI + y / 4.7 * 2 * Math.PI - time * 5.0)
                + 0.5 * Math.sin(x / 1.9 * 2 * Math.PI - y / 2.6 * 2 * Math.PI - time * 8.3));
        out[0] = x;
        out[1] = -y + 0.35 * belly * t;
        out[2] = belly + ripple;
    }

    /** The unit normal of the sheet's front (the forward side) at (x, y), into {@code out}. */
    void normal(double x, double y, double[] out) {
        double e = 0.05;
        double[] a = new double[3];
        double[] b = new double[3];
        point(x + e, y, a);
        point(x - e, y, b);
        double ax = a[0] - b[0], ay = a[1] - b[1], az = a[2] - b[2]; // along x
        point(x, y + e, a);
        point(x, y - e, b);
        double bx = a[0] - b[0], by = a[1] - b[1], bz = a[2] - b[2]; // along y (down)
        // down × across faces forward
        double nx = by * az - bz * ay;
        double ny = bz * ax - bx * az;
        double nz = bx * ay - by * ax;
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
        out[0] = nx / len;
        out[1] = ny / len;
        out[2] = nz / len;
    }
}
