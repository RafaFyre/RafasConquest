package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class WeaponSkillsTest {
    @Test
    void bonusesCycleHasteStrengthKnockback() {
        assertArrayEquals(new int[]{0, 0, 0}, WeaponSkills.bonuses(0));
        assertArrayEquals(new int[]{1, 0, 0}, WeaponSkills.bonuses(1));
        assertArrayEquals(new int[]{1, 1, 0}, WeaponSkills.bonuses(2));
        assertArrayEquals(new int[]{1, 1, 1}, WeaponSkills.bonuses(3));
        assertArrayEquals(new int[]{2, 1, 1}, WeaponSkills.bonuses(4));
        assertArrayEquals(new int[]{2, 2, 2}, WeaponSkills.bonuses(6));
    }

    @Test
    void levelFollowsMinecraftsCurve() {
        assertArrayEquals(new int[]{0, 6, 7}, PlayerStats.level(6));
        assertArrayEquals(new int[]{1, 0, 9}, PlayerStats.level(7));
        assertArrayEquals(new int[]{30, 0, 112}, PlayerStats.level(1395)); // Minecraft's total for level 30
    }
}
