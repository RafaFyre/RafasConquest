package rafasconquest.core;

/** Day counting against the overworld clock, and the offline catch-up math. */
public final class DayClock {
    public static final long TICKS_PER_DAY = 24_000L;
    /** One in-game day of catch-up per 20 real minutes, so 1 hour = 3 days. */
    public static final long REAL_MILLIS_PER_DAY = 20L * 60L * 1000L;
    public static final int CATCH_UP_DAYS_PER_TICK = 50;

    private DayClock() {
    }

    public record Step(long daysToProcess, long newLastProcessedDay) {
    }

    public static long dayIndex(long dayTime) {
        return Math.floorDiv(dayTime, TICKS_PER_DAY);
    }

    /**
     * Forward: pay each day crossed. Backward (/time set lower): resync to the new index and pay nothing,
     * so no day is ever paid twice.
     */
    public static Step advance(long lastProcessedDay, long currentDayIndex) {
        if (currentDayIndex > lastProcessedDay) {
            return new Step(currentDayIndex - lastProcessedDay, currentDayIndex);
        }
        return new Step(0, currentDayIndex);
    }

    /** Days missed while the server was offline, rounded down and capped by maxCatchUpDays (0 = no cap). */
    public static long missedDays(long nowMillis, long lastSavedRealTime, int maxCatchUpDays) {
        if (lastSavedRealTime <= 0 || nowMillis <= lastSavedRealTime) {
            return 0;
        }
        long days = (nowMillis - lastSavedRealTime) / REAL_MILLIS_PER_DAY;
        return maxCatchUpDays > 0 ? Math.min(days, maxCatchUpDays) : days;
    }
}
