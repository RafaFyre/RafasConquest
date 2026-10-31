package rafasconquest.worldgen;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoadNetworkTest {
    /** Rolling ground with a ravine, a river and a marsh, 600 samples long. */
    private static float[][] land() {
        int n = 600;
        float[] ground = new float[n];
        float[] water = new float[n];
        Random random = new Random(7);
        for (int i = 0; i < n; i++) {
            ground[i] = 70 + (float) (8 * Math.sin(i / 23.0) + 3 * Math.sin(i / 7.0)) + random.nextInt(2);
            water[i] = ground[i];
        }
        for (int i = 150; i < 156; i++) {
            ground[i] -= 20; // a ravine
            water[i] = ground[i];
        }
        for (int i = 300; i < 318; i++) {
            ground[i] = 55; // a river
            water[i] = 63;
        }
        for (int i = 420; i < 432; i++) {
            water[i] = ground[i] + 1; // a marsh
        }
        return new float[][]{ground, water};
    }

    @Test
    void stretchesMeetWithoutSeams() {
        float[][] land = land();
        int n = land[0].length;
        RoadNetwork.Stretch whole = RoadNetwork.work(land[0], land[1]);
        for (int w = 0; w * RoadNetwork.WINDOW < n; w++) {
            int lo = Math.max(0, w * RoadNetwork.WINDOW - RoadNetwork.HALO);
            int hi = Math.min(n - 1, (w + 1) * RoadNetwork.WINDOW - 1 + RoadNetwork.HALO);
            float[] g = java.util.Arrays.copyOfRange(land[0], lo, hi + 1);
            float[] s = java.util.Arrays.copyOfRange(land[1], lo, hi + 1);
            RoadNetwork.Stretch part = RoadNetwork.work(g, s);
            for (int i = w * RoadNetwork.WINDOW; i < Math.min(n, (w + 1) * RoadNetwork.WINDOW); i++) {
                assertEquals(whole.height()[i], part.height()[i - lo], 1e-4, "height at sample " + i);
                assertEquals(whole.bridge()[i], part.bridge()[i - lo], "bridge at sample " + i);
            }
        }
    }

    @Test
    void aPointAlongARoadFindsItsSegment() {
        double[] cum = {0, 10, 25, 25, 40};
        assertEquals(0, RoadNetwork.segment(cum, 0));
        assertEquals(0, RoadNetwork.segment(cum, 9.9));
        assertEquals(1, RoadNetwork.segment(cum, 10));
        assertEquals(3, RoadNetwork.segment(cum, 30));
        assertEquals(3, RoadNetwork.segment(cum, 40));
    }
}
