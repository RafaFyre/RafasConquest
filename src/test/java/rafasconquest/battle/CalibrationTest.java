package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.Calibration.Striker;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalibrationTest {
    @Test
    void recoversTheRatesThatMadeTheLosses() {
        double[] truth = {0.2, 0.5, 0.1, 0.3};
        Random random = new Random(1);
        Calibration calibration = new Calibration();
        for (int i = 0; i < 5000; i++) {
            double[] strikers = new double[4];
            double lost = 0;
            for (int k = 0; k < 4; k++) {
                strikers[k] = random.nextInt(3) == 0 ? random.nextInt(21) * (0.6 + random.nextDouble()) : 0;
                lost += strikers[k] * truth[k];
            }
            calibration.addDamage(lost + random.nextGaussian(), strikers);
        }
        Calibration.Fit fit = calibration.fit();
        for (int k = 0; k < 4; k++) {
            assertEquals(truth[k], fit.dps()[k], 0.02, Striker.values()[k].name());
        }
    }

    @Test
    void kindsSeenTooRarelyAreLeftUnfitted() {
        Calibration calibration = new Calibration();
        for (int i = 0; i < 100; i++) {
            calibration.addDamage(2, new double[]{10, 0, 0, i < 5 ? 1 : 0});
        }
        Calibration.Fit fit = calibration.fit();
        assertTrue(Double.isNaN(fit.dps()[Striker.ARROWS.ordinal()]));
        assertTrue(Double.isNaN(fit.dps()[Striker.CAVALRY.ordinal()]));
        assertEquals(0.2, fit.dps()[Striker.INFANTRY.ordinal()], 1e-6);
    }

    @Test
    void speedsAndHealthAreMediansAndAverages() {
        Calibration calibration = new Calibration();
        for (double s : new double[]{1, 3, 4, 100}) {
            calibration.addSpeed(false, s);
        }
        calibration.addSoldier(20);
        calibration.addSoldier(30);
        Calibration merged = new Calibration();
        merged.addAll(calibration);
        Calibration.Fit fit = merged.fit();
        assertEquals(4, fit.footSpeed());
        assertEquals(25, fit.health());
        assertTrue(Double.isNaN(fit.horseSpeed()));
        assertEquals(3, Calibration.median(List.of(1.0, 3.0, 4.0)));
    }
}
