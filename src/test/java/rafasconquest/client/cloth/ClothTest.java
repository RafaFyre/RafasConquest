package rafasconquest.client.cloth;

import org.joml.Matrix4d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClothTest {
    /** A banner's flag in world blocks: hangs down (local y down) from a bar 10 blocks up. */
    private static Matrix4d bar() {
        return new Matrix4d().translate(0, 10, 0).scale(1, -1, 1);
    }

    private static Cloth settle(Cloth c, double wind, int steps) {
        Matrix4d frame = bar();
        c.place(frame);
        for (int i = 0; i < steps; i++) {
            c.step(0.05, wind, -9.81, 0, 25, 0.93, frame, new Matrix4d(), null);
        }
        return c;
    }

    @Test
    void hangsStraightDownAndKeepsItsShape() {
        Cloth c = settle(new Cloth(9, 17, 0.1, 0.1, 0), 0, 200);
        int bottom = 16 * 9 + 4;
        assertEquals(10 - 1.6, c.y[bottom], 0.1); // sags ~5% under its own weight, as relaxed cloth does
        assertEquals(0, c.x[bottom], 0.01);
        // 25 relaxations leave the top cells (which carry the whole sheet) the most stretched
        assertTrue(c.worstStretch() < 0.3, "stretch " + c.worstStretch());
    }

    @Test
    void aSheetWrappedRoundABodyHangsUnderItsRowAndKeepsItsSeam() {
        // A U round a body (front flap, side, back, other side, other flap), its two middle columns one seamed point.
        double[] xs = {0.4, 0.5, 0.5, 0.2, -0.2, -0.5, -0.5, -0.5, -0.4};
        double[] zs = {-0.2, -0.2, 0.2, 0.2, 0.2, 0.2, 0.2, -0.2, -0.2};
        Cloth c = settle(new Cloth(9, xs, zs, 0.1, 0.8), 0, 200);
        for (int i = 0; i < xs.length; i++) {
            int bottom = 8 * xs.length + i;
            assertEquals(xs[i], c.x[bottom], 0.05, "column " + i + " x");
            assertEquals(zs[i], c.z[bottom], 0.05, "column " + i + " z");
        }
        int seamA = 8 * xs.length + 5;
        assertEquals(0, Math.hypot(c.x[seamA] - c.x[seamA + 1], c.z[seamA] - c.z[seamA + 1]), 0.01);
        assertTrue(c.worstStretch() < 0.3, "stretch " + c.worstStretch());
    }

    @Test
    void aTurnedLimbBoxPushesItOut() {
        // An arm swung 40° forward through the sheet's plane: no point may stay inside it, in the arm's own units.
        Cloth c = new Cloth(9, 17, 0.1, 0.1, 0);
        Matrix4d toArm = new Matrix4d().rotateX(Math.toRadians(-40)).translate(0, -0.3, 0); // local → arm
        double[] arm = {-0.15, 0, -0.1, 0.15, 0.8, 0.1};
        c.limbs(new double[][]{arm}, new Matrix4d[]{toArm});
        settle(c, 0, 200);
        Matrix4d toLocal = bar().invert();
        org.joml.Vector3d v = new org.joml.Vector3d();
        for (int p = c.w; p < c.x.length; p++) {
            toArm.transformPosition(toLocal.transformPosition(v.set(c.x[p], c.y[p], c.z[p])));
            boolean inside = v.x > arm[0] + 1e-6 && v.x < arm[3] - 1e-6 && v.y > arm[1] + 1e-6 && v.y < arm[4] - 1e-6
                    && v.z > arm[2] + 1e-6 && v.z < arm[5] - 1e-6;
            assertTrue(!inside, "point " + p + " inside the arm at " + v);
        }
    }

    @Test
    void windBlowsItSideways() {
        Cloth c = settle(new Cloth(9, 17, 0.1, 0.1, 0), 9.81, 300);
        int bottom = 16 * 9 + 4;
        assertTrue(c.x[bottom] > 0.8, "bottom at x " + c.x[bottom]);
        assertTrue(c.y[0] == 10, "pinned row stays on the bar");
    }

    @Test
    void staysOutOfBoxes() {
        // A box below the bar, in front of the flag's plane: wind toward it (−z) must not push the cloth in.
        Cloth c = new Cloth(5, 9, 0.1, 0.1, 0).keepOut(new double[]{-1, 0.2, -1, 1, 2, -0.01});
        Matrix4d frame = bar();
        c.place(frame);
        for (int i = 0; i < 200; i++) {
            c.step(0.05, 0, -9.81, -20, 25, 0.93, frame, new Matrix4d(), null);
        }
        for (int p = 5; p < 45; p++) {
            double localY = 10 - c.y[p];
            assertTrue(localY < 0.2 || c.z[p] >= -0.011, "point " + p + " inside the box at z " + c.z[p]);
        }
    }

    @Test
    void solidBlocksPushItOut() {
        Cloth c = new Cloth(5, 9, 0.1, 0.1, 0);
        Matrix4d frame = new Matrix4d().translate(0.5, 10.5, 0.5).scale(1, -1, 1);
        c.place(frame);
        // The floor at y 9 is solid: nothing may end below y 10 (the cloth would hang to 9.7).
        for (int i = 0; i < 100; i++) {
            c.step(0.05, 0, -9.81, 0, 25, 0.93, frame, new Matrix4d(), (x, y, z) -> y <= 9);
        }
        for (int p = 0; p < 45; p++) {
            assertTrue(c.y[p] >= 10 - 1e-9, "point " + p + " at y " + c.y[p]);
        }
    }
}
