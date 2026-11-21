package rafasconquest.sim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameSpeedTest {
    @Test
    void sixtyPercentRoundedUp() {
        assertEquals(1, GameSpeed.need(1));
        assertEquals(2, GameSpeed.need(2));
        assertEquals(2, GameSpeed.need(3));
        assertEquals(3, GameSpeed.need(4));
        assertEquals(3, GameSpeed.need(5));
        assertEquals(6, GameSpeed.need(10));
    }

    @Test
    void speedsMatchTheirDays() {
        int[] ticksPerTick = {1, 2, 4, 120, 600}; // 24000 day ticks over 20 real ticks a second
        for (int s = 0; s < 5; s++) {
            assertEquals(ticksPerTick[s], 1200 / GameSpeed.SECONDS_PER_DAY[s]);
        }
    }
}
