package rafasconquest.client.cloth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SailShapeTest {
    private final SailShape still = new SailShape(9, 7, 1, 0, 0);
    private final double[] p = new double[3];

    @Test
    void topEdgeAndSidesStayInThePlane() {
        SailShape blowing = new SailShape(9, 7, 1, 1, 12.3);
        for (double x = 0; x <= 9; x += 0.25) {
            blowing.point(x, 0, p);
            assertEquals(0, p[2], 1e-9, "on the yard at x " + x);
            assertEquals(0, p[1], 1e-9);
        }
        for (double y = 0; y <= 7; y += 0.25) {
            still.point(0, y, p);
            assertEquals(0, p[2], 1e-9, "left side at y " + y);
            still.point(9, y, p);
            assertEquals(0, p[2], 1e-9, "right side at y " + y);
        }
    }

    @Test
    void belliesForwardDeepestBelowTheMiddleAndDrawsTheFootUp() {
        double deepest = 0;
        double deepestY = 0;
        for (double y = 0; y <= 7; y += 0.25) {
            still.point(4.5, y, p);
            if (p[2] > deepest) {
                deepest = p[2];
                deepestY = y;
            }
        }
        assertTrue(deepest > 1, "a full 9x7 sail bellies over a block: " + deepest);
        assertTrue(deepestY > 3.5 && deepestY < 7, "deepest below the middle: " + deepestY);
        still.point(4.5, 7, p);
        assertTrue(p[1] > -7, "the foot's middle is drawn up");
    }
}
