package rafasconquest.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeaFoamTest {
    private static final int S = WaterSwell.FOAM_SIDE;

    @Test
    void theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread() {
        byte[] t = WaterSwell.foamTexels();
        assertEquals(S * S * WaterSwell.FOAM_SLICES * 2, t.length);
        for (int c = 0; c < 2; c++) {
            double seamX = 0;
            double innerX = 0;
            double seamZ = 0;
            double innerZ = 0;
            double sum = 0;
            for (int y = 0; y < S; y++) {
                for (int x = 0; x < S; x++) {
                    seamZ += Math.abs(at(t, x, y, WaterSwell.FOAM_SLICES - 1, c) - at(t, x, y, 0, c));
                    innerZ += Math.abs(at(t, x, y, 1, c) - at(t, x, y, 0, c));
                }
                seamX += Math.abs(at(t, S - 1, y, 0, c) - at(t, 0, y, 0, c));
                innerX += Math.abs(at(t, 1, y, 0, c) - at(t, 0, y, 0, c));
            }
            assertTrue(seamX < innerX * 2 + S, "x seam " + seamX + " vs " + innerX);
            assertTrue(seamZ < innerZ * 2, "time seam " + seamZ + " vs " + innerZ);
            for (int i = c; i < t.length; i += 2) {
                sum += (t[i] & 0xff) / 255.0;
            }
            assertEquals(c == 0 ? 0.498 : 0.475, sum / (t.length / 2.0), 0.01);
        }
    }

    private static int at(byte[] t, int x, int y, int z, int c) {
        return t[((z * S + y) * S + x) * 2 + c] & 0xff;
    }
}
