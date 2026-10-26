package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceScalingTest {
    private final WorldSettings s = WorldSettings.defaults();

    @Test
    void stormWallUsesStormBounds() {
        assertEquals(new DistanceScaling.Bounds(1, 3, 300, 1500), DistanceScaling.boundsAt(2500, s));
        assertEquals(new DistanceScaling.Bounds(1, 3, 300, 1500), DistanceScaling.boundsAt(4000, s));
    }

    @Test
    void farWorldUsesFarBounds() {
        assertEquals(new DistanceScaling.Bounds(6, 10, 3500, 5000), DistanceScaling.boundsAt(24_000, s));
        assertEquals(new DistanceScaling.Bounds(6, 10, 3500, 5000), DistanceScaling.boundsAt(100_000, s));
    }

    @Test
    void boundsInterpolateLinearlyBetween() {
        // Halfway from R2 (4,000) to R2 + 20,000.
        DistanceScaling.Bounds mid = DistanceScaling.boundsAt(14_000, s);
        assertEquals(new DistanceScaling.Bounds(4, 7, 1900, 3250), mid);
    }

    @Test
    void boundsNeverShrinkWithDistance() {
        DistanceScaling.Bounds prev = DistanceScaling.boundsAt(0, s);
        for (int d = 0; d <= 30_000; d += 250) {
            DistanceScaling.Bounds b = DistanceScaling.boundsAt(d, s);
            assertTrue(b.radiusMin() >= prev.radiusMin() && b.radiusMax() >= prev.radiusMax());
            assertTrue(b.strengthMin() >= prev.strengthMin() && b.strengthMax() >= prev.strengthMax());
            prev = b;
        }
    }

    @Test
    void rolledEmpiresStayInsideTheirBounds() {
        for (int i = 0; i < 2000; i++) {
            int x = i * 37 - 30_000;
            int z = i * 53 - 40_000;
            OriginRoller.Roll roll = OriginRoller.roll(12345L, x, z, s);
            DistanceScaling.Bounds b = DistanceScaling.boundsAt(Math.sqrt((double) x * x + (double) z * z), s);
            assertTrue(roll.radiusSteps() >= b.radiusMin() && roll.radiusSteps() <= b.radiusMax());
            assertTrue(roll.strength() >= b.strengthMin() && roll.strength() <= b.strengthMax());
            assertTrue(roll.strength() >= 300 && roll.strength() <= 5000);
        }
    }
}
