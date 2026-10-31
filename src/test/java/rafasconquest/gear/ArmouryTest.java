package rafasconquest.gear;

import org.junit.jupiter.api.Test;
import rafasconquest.character.Rank;
import rafasconquest.people.Homeland;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArmouryTest {
    @Test
    void everyLandHasItsLook() throws ReflectiveOperationException {
        Field f = Armoury.class.getDeclaredField("STYLES");
        f.setAccessible(true);
        Map<?, ?> styles = (Map<?, ?>) f.get(null);
        for (Homeland land : Homeland.values()) {
            assertNotNull(styles.get(land), land + " has no style");
        }
    }

    @Test
    void ranksComeInFourGradesOfHarness() {
        assertEquals(0, RankKits.grade(Rank.SOLDIER));
        assertEquals(0, RankKits.grade(Rank.CORPORAL));
        assertEquals(1, RankKits.grade(Rank.SERJEANT));
        assertEquals(2, RankKits.grade(Rank.MAJOR));
        assertEquals(3, RankKits.grade(Rank.GRAND_MARSHAL));
    }
}
