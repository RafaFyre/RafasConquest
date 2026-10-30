package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DojosTest {
    private static final List<int[]> SANCTUMS = List.of(new int[]{12000, 3000}, new int[]{-9000, -14000}, new int[]{-15000, 9000});

    @Test
    void tenDojosOneForEachSchoolAllFarApart() {
        for (long seed = 0; seed < 5; seed++) {
            List<Dojos.Dojo> dojos = Dojos.plan(seed, 32000, 1000, SANCTUMS, (x, z) -> true);
            assertEquals(10, dojos.size(), "seed " + seed);
            Set<Dojos.School> schools = EnumSet.noneOf(Dojos.School.class);
            for (Dojos.Dojo d : dojos) {
                assertTrue(schools.add(d.school()), "two dojos of " + d.school());
                for (int[] s : SANCTUMS) {
                    assertTrue(Math.hypot(s[0] - d.x(), s[1] - d.z()) >= Dojos.APART, "a dojo too near a sanctum");
                }
                for (Dojos.Dojo other : dojos) {
                    if (other != d) {
                        assertTrue(Math.hypot(other.x() - d.x(), other.z() - d.z()) >= Dojos.APART, "two dojos too near");
                    }
                }
                assertTrue(Math.hypot(d.x(), d.z()) <= 32000 && Math.hypot(d.x(), d.z()) >= 1000, "a dojo off the known world");
            }
            assertEquals(3, dojos.stream().filter(d -> d.school().hidden()).count());
        }
    }

    @Test
    void theSchoolsAreTheTenNamed() {
        assertEquals(List.of("Fire", "Water", "Wind", "Lightning", "Ice", "Stone", "Iron"),
                java.util.Arrays.stream(Dojos.School.values()).filter(s -> !s.hidden()).map(Dojos.School::title).toList());
        assertEquals(List.of("Blood", "Shadow", "Light"),
                java.util.Arrays.stream(Dojos.School.values()).filter(Dojos.School::hidden).map(Dojos.School::title).toList());
    }

    @Test
    void dojosKeepToTheGroundAllowed() {
        // Only the east half is land.
        for (Dojos.Dojo d : Dojos.plan(3, 32000, 1000, List.of(), (x, z) -> x > 0)) {
            assertTrue(d.x() > 0, "a dojo in the sea");
        }
    }

    @Test
    void aSmallWorldLosesHiddenDojosFirst() {
        List<Dojos.Dojo> dojos = Dojos.plan(1, 12000, 1000, List.of(), (x, z) -> true);
        assertTrue(dojos.size() < 10, dojos.size() + " dojos in a small world");
        long known = dojos.stream().filter(d -> !d.school().hidden()).count();
        assertEquals(Math.min(7, dojos.size()), known, "a hidden dojo placed before a known one");
        assertFalse(dojos.isEmpty());
    }
}
