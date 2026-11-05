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
        // Antique Legacy 1.13's decorations, by slot (its armour is never used)
        java.util.Set<String> antiqueHead = java.util.stream.Stream.of("bronze_horns", "bronze_small_wings", "greek_crest",
                "greek_high_crest", "hair_plume", "roman_crest", "roman_crest_alt", "roman_feathers", "roman_two_feathers",
                "triple_feather", "two_hair_plumes", "laurel_wreath", "golden_laurel_wreath")
                .map(d -> "antiquelegacy:" + d + "_decoration").collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> antiqueChest = java.util.stream.Stream.of("antique_cloak", "fur_cloak", "chiton", "subarmalis",
                "celtic_tunic_underarmor", "left_gladiator_shoulder_pad", "right_gladiator_shoulder_pad",
                "iron_left_hand_manika", "bronze_left_hand_manika", "fabric_left_hand_manika",
                "iron_right_hand_manika", "bronze_right_hand_manika", "fabric_right_hand_manika")
                .map(d -> "antiquelegacy:" + d + "_decoration").collect(java.util.stream.Collectors.toSet());
        Field f = Armoury.class.getDeclaredField("STYLES");
        f.setAccessible(true);
        for (Object looks : ((Map<?, ?>) f.get(null)).values()) {
            for (Object o : (java.util.List<?>) looks) {
                Armoury.Style s = (Armoury.Style) o;
                org.junit.jupiter.api.Assertions.assertTrue(chest.contains(s.officerChest()), s.officerChest());
                org.junit.jupiter.api.Assertions.assertFalse(chest.contains(s.crest()), s.crest());
                org.junit.jupiter.api.Assertions.assertFalse(chest.contains(s.troopCrest()), s.troopCrest());
                org.junit.jupiter.api.Assertions.assertTrue(antiqueHead.contains(s.antiqueCrest()), s.antiqueCrest());
                org.junit.jupiter.api.Assertions.assertTrue(antiqueChest.contains(s.antiqueChest()), s.antiqueChest());
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
