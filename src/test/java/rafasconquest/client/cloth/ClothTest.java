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
