package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RetinuesTest {
    @Test
    void retinuesGrowInALineFromOneToTwelve() {
        assertEquals(1, Retinues.size(0));
        assertEquals(4, Retinues.size(1));
        assertEquals(7, Retinues.size(2));
        assertEquals(9, Retinues.size(3));
        assertEquals(12, Retinues.size(Retinues.TOP));
    }

    @Test
    void playersStandByTheirTitle() {
        assertEquals(0, Retinues.level(NobleTitle.NONE));
        assertEquals(1, Retinues.level(NobleTitle.KNIGHT));
        assertEquals(2, Retinues.level(NobleTitle.COUNT));
    }
}
