package rafasconquest.client.cloth;

import org.joml.Matrix4d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WoolTest {
    private static final Matrix4d BAR = new Matrix4d().translate(0, 10, 0).scale(1, -1, 1);

    /** The sharpest turn (radians) down the middle column: a crease shows as a large one. */
    private static double sharpestFold(Cloth c) {
        double worst = 0;
        int i = c.w / 2;
        for (int j = 1; j < c.h - 1; j++) {
            int a = (j - 1) * c.w + i, b = j * c.w + i, d = (j + 1) * c.w + i;
            double ux = c.x[b] - c.x[a], uy = c.y[b] - c.y[a], uz = c.z[b] - c.z[a];
            double vx = c.x[d] - c.x[b], vy = c.y[d] - c.y[b], vz = c.z[d] - c.z[b];
            double cos = (ux * vx + uy * vy + uz * vz) / Math.sqrt((ux * ux + uy * uy + uz * uz) * (vx * vx + vy * vy + vz * vz));
            worst = Math.max(worst, Math.acos(Math.max(-1, Math.min(1, cos))));
        }
        return worst;
    }

    /** A cloth with its middle row yanked forward, then let go for a few steps. */
    private static double foldAfterYank(double bend) {
        Cloth c = new Cloth(9, 17, 0.1, 0.1, 0, bend);
        c.place(BAR);
        for (int i = 0; i < 9; i++) {
            c.z[8 * 9 + i] -= 0.6;
        }
        for (int s = 0; s < 3; s++) {
            c.step(0.05, 9.81, new double[3], 0, 25, 0.8, BAR, new Matrix4d(), null);
        }
        return sharpestFold(c);
    }

    @Test
    void bendSpringsRoundOffCreases() {
        double silk = foldAfterYank(0);
        double wool = foldAfterYank(0.8);
        assertTrue(wool < silk * 0.8, "wool fold " + wool + " vs silk " + silk);
    }

    /** Wind along the sheet's plane (edge-on): pressing on the facing, it barely stirs the cloth. */
    @Test
    void edgeOnWindBarelyMovesIt() {
        double even = sideways(0);
        double aero = sideways(1);
        assertTrue(aero < even * 0.2, "edge-on drift " + aero + " vs even " + even);
    }

    private static double sideways(double aero) {
        Cloth c = new Cloth(9, 17, 0.1, 0.1, 0, 0.5);
        c.place(BAR);
        for (int s = 0; s < 60; s++) {
            c.step(0.05, 9.81, new double[]{6, 0, 0}, aero, 25, 0.85, BAR, new Matrix4d(), null);
        }
        return Math.abs(c.x[16 * 9 + 4]);
    }
}
