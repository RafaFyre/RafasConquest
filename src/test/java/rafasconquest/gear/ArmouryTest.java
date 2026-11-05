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
            // nations of one land take its looks in turn
            org.junit.jupiter.api.Assertions.assertNotSame(Armoury.style(land, 0), Armoury.style(land, 1), land + " has one look");
            org.junit.jupiter.api.Assertions.assertSame(Armoury.style(land, 0), Armoury.style(land, ((java.util.List<?>) styles.get(land)).size()));
        }
    }

    @Test
    void decorationsSitInTheirOwnSlot() throws ReflectiveOperationException {
        // Epic Knights' chestplate decorations; every other decoration goes on a helmet
        java.util.Set<String> chest = java.util.Set.of("hood_decoration", "ecranche_decoration", "rondel_decoration");
        Field f = Armoury.class.getDeclaredField("STYLES");
        f.setAccessible(true);
        for (Object looks : ((Map<?, ?>) f.get(null)).values()) {
            for (Object o : (java.util.List<?>) looks) {
                Armoury.Style s = (Armoury.Style) o;
                org.junit.jupiter.api.Assertions.assertTrue(chest.contains(s.officerChest()), s.officerChest());
                org.junit.jupiter.api.Assertions.assertFalse(chest.contains(s.crest()), s.crest());
                org.junit.jupiter.api.Assertions.assertFalse(chest.contains(s.troopCrest()), s.troopCrest());
            }
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
