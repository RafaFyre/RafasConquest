package rafasconquest.client.cloth;

import org.joml.Matrix4d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** A far flag (few relaxations, tethered) in a battle's wind keeps its size. */
class StretchTest {
    /** The worst stretch over 20 s of wind ({@code wind} blocks/s² along the fly) for a size-3 flag. */
    static double worst(int iterations, double wind, boolean tethered) {
        // Pinned along a pole (local x down it), flown out along local y: as BannerArt's flags hang.
        Matrix4d pole = new Matrix4d().translate(0, 10, 0).rotateZ(-Math.PI / 2);
        Cloth c = new Cloth(9, 17, 3 / 8.0, 3 / 8.0, 0, 0.5);
        c.tethered = tethered;
        c.place(pole);
        double worst = 0;
        for (int s = 0; s < 400; s++) {
            c.step(0.05, 9.81, new double[]{wind, 0, wind * 0.3}, 0.6, iterations, 0.85, pole, new Matrix4d(), null);
            worst = Math.max(worst, c.worstStretch());
        }
        return worst;
    }

    @Test
    void tetheredFarClothKeepsItsSizeInBattleWind() {
        double loose = worst(6, 80, false);
        double tethered = worst(6, 80, true);
        System.out.println("STRETCH far loose " + loose + ", tethered " + tethered);
        assertTrue(tethered < 0.2 && tethered < loose / 2, "tethered " + tethered + " vs loose " + loose);
    }
}
