package rafasconquest.core;

import org.junit.jupiter.api.Test;
import rafasconquest.core.Age.Range;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rafasconquest.core.Age.BRONZE;
import static rafasconquest.core.Age.GUNPOWDER;
import static rafasconquest.core.Age.STEAM;
import static rafasconquest.core.Age.STEEL;

class AgeTest {
    @Test
    void bronzeIsOffByDefault() {
        assertFalse(Range.DEFAULT.has(BRONZE));
        assertTrue(Range.DEFAULT.has(STEEL) && Range.DEFAULT.has(GUNPOWDER) && Range.DEFAULT.has(STEAM));
    }

    @Test
    void onlyEndsGoAndOnlyNeighboursJoin() {
        Range r = Range.DEFAULT;
        assertFalse(r.canToggle(GUNPOWDER)); // would leave Steel and Steam apart
        assertEquals(r, r.toggle(GUNPOWDER));
        assertEquals(new Range(BRONZE, STEAM), r.toggle(BRONZE));
        assertEquals(new Range(GUNPOWDER, STEAM), r.toggle(STEEL));
        assertEquals(new Range(STEEL, GUNPOWDER), r.toggle(STEAM));
        Range late = new Range(GUNPOWDER, STEAM);
        assertFalse(late.canToggle(BRONZE)); // Bronze and Gunpowder without Steel
        assertEquals(late, late.toggle(BRONZE));
    }

    @Test
    void theLastAgeCannotGo() {
        Range one = new Range(STEEL, STEEL);
        assertFalse(one.canToggle(STEEL));
        assertEquals(one, one.toggle(STEEL));
    }

    @Test
    void unknownNamesFallBack() {
        assertEquals(STEAM, Age.parse("steam", STEEL));
        assertEquals(STEEL, Age.parse("plastic", STEEL));
        assertEquals(STEEL, Age.parse(null, STEEL));
    }
}
