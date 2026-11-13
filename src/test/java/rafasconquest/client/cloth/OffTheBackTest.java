package rafasconquest.client.cloth;

import org.joml.Matrix4d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OffTheBackTest {
    /** Local +z is behind the wearer, local −x their right; the frame here is the world's own axes. */
    @Test
    void windPressingTheCapeInBlowsItRightInstead() {
        double[] wind = {0, 0, -4};
        ClothRenderer.offTheBack(new Matrix4d(), wind);
        assertArrayEquals(new double[]{-2, 0, 0}, wind, 1e-9);
    }

    @Test
    void windFromInFrontIsLeftAlone() {
        double[] wind = {1, 0, 4};
        ClothRenderer.offTheBack(new Matrix4d(), wind);
        assertArrayEquals(new double[]{1, 0, 4}, wind, 1e-9);
    }

    @Test
    void sideWindKeepsItsSideAndLosesThePartIntoTheBack() {
        double[] wind = {3, 0, -2};
        ClothRenderer.offTheBack(new Matrix4d().rotateY(Math.PI), wind); // turned round: behind is −z, right is +x
        assertArrayEquals(new double[]{3, 0, -2}, wind, 1e-9);
    }
}
