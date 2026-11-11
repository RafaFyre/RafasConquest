package rafasconquest.client.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrandStrategyDateTest {
    @Test
    void daysReadAsSeasonsOfTheYear() {
        assertEquals("Spring 1st, 1 AD", GrandStrategyScreen.date(0));
        assertEquals("Spring 2nd, 12 AD", GrandStrategyScreen.date(11 * 24 + 1));
        assertEquals("Summer 3rd, 1 AD", GrandStrategyScreen.date(8));
        assertEquals("Winter 6th, 1 AD", GrandStrategyScreen.date(23));
        assertEquals("Spring 1st, 2 AD", GrandStrategyScreen.date(24));
    }
}
