package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayClockTest {
    private static final long MINUTE = 60_000L;

    @Test
    void dayIndexRoundsDown() {
        assertEquals(0, DayClock.dayIndex(0));
        assertEquals(0, DayClock.dayIndex(23_999));
        assertEquals(1, DayClock.dayIndex(24_000));
        assertEquals(3, DayClock.dayIndex(95_000));
    }

    @Test
    void noNewDayPaysNothing() {
        assertEquals(new DayClock.Step(0, 4), DayClock.advance(4, 4));
    }

    @Test
    void sleepingCrossesOneBoundary() {
        // Sleeping at dayTime 37,000 (day 1) jumps to 48,000 (day 2).
        assertEquals(new DayClock.Step(1, 2), DayClock.advance(DayClock.dayIndex(37_000), DayClock.dayIndex(48_000)));
    }

    @Test
    void timeAddPaysEveryDayCrossed() {
        long before = 48_000;
        assertEquals(new DayClock.Step(3, 5), DayClock.advance(DayClock.dayIndex(before), DayClock.dayIndex(before + 72_000)));
    }

    @Test
    void timeSetBackwardsResyncsWithoutPaying() {
        DayClock.Step back = DayClock.advance(5, DayClock.dayIndex(0));
        assertEquals(new DayClock.Step(0, 0), back);
        // Next sunrise after the reset is a new crossing, paid once.
        assertEquals(new DayClock.Step(1, 1), DayClock.advance(back.newLastProcessedDay(), 1));
    }

    @Test
    void missedDaysRoundDown() {
        long saved = 1_000_000L;
        assertEquals(0, DayClock.missedDays(saved + 19 * MINUTE, saved, 0));
        assertEquals(2, DayClock.missedDays(saved + 40 * MINUTE, saved, 0));
        assertEquals(2, DayClock.missedDays(saved + 59 * MINUTE, saved, 0));
        assertEquals(3, DayClock.missedDays(saved + 60 * MINUTE, saved, 0));
    }

    @Test
    void missedDaysCapped() {
        long saved = 1_000_000L;
        assertEquals(10, DayClock.missedDays(saved + 24 * 60 * MINUTE, saved, 10));
        assertEquals(72, DayClock.missedDays(saved + 24 * 60 * MINUTE, saved, 0));
    }

    @Test
    void missedDaysIgnoresMissingOrFutureSaveTime() {
        assertEquals(0, DayClock.missedDays(5_000_000L, 0, 0));
        assertEquals(0, DayClock.missedDays(5_000_000L, 9_000_000L, 0));
    }
}
