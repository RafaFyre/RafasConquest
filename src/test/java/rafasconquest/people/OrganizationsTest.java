package rafasconquest.people;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrganizationsTest {
    private static Person swordsman(int prowess, int x, int z) {
        int[] stats = new int[Stat.values().length];
        stats[Stat.PROWESS.ordinal()] = prowess;
        return new Person("A", "B", false, 30, 0, 0, 0, stats, List.of(), new Person.Hold(Person.Kind.CASTLE, 0, x, z), -1, 0, false);
    }

    /** Three sanctums far apart; their gods; and kings and saints scattered near each. */
    private static List<Person> people(Random random) {
        List<Person> people = new ArrayList<>(List.of(swordsman(45, 0, 0), swordsman(44, 20000, 0), swordsman(42, 0, 20000)));
        int[][] at = {{0, 0}, {20000, 0}, {0, 20000}};
        for (int i = 0; i < 300; i++) {
            int[] near = at[i % 3];
            int prowess = i % 5 == 0 ? Stat.SWORD_KING + random.nextInt(10) : Stat.SUPERHUMAN + random.nextInt(9);
            people.add(swordsman(prowess, near[0] + random.nextInt(3000) - 1500, near[1] + random.nextInt(3000) - 1500));
        }
        people.add(swordsman(10, 0, 0)); // no sword title: in no sanctum
        return people;
    }

    private static List<Organizations.Sanctum> sanctums() {
        return List.of(new Organizations.Sanctum("Sword Sanctum of A", 0, 0, 0, 0), new Organizations.Sanctum("Sword Sanctum of B", 1, 1, 20000, 0),
                new Organizations.Sanctum("Sword Sanctum of C", 2, 2, 0, 20000));
    }

    @Test
    void eachSanctumKeepsTwoToSevenKingsAndEverySaint() {
        List<Person> people = people(new Random(1));
        List<Organization> all = Organizations.plan(new Random(2), people, sanctums());
        assertEquals(3, all.size());
        int saints = 0;
        for (Organization o : all) {
            assertTrue(o.kings().size() >= 2 && o.kings().size() <= 7, "kings: " + o.kings().size());
            for (int k : o.kings()) {
                int prowess = people.get(k).stat(Stat.PROWESS);
                assertTrue(prowess >= Stat.SWORD_KING && prowess < Stat.SWORD_GOD);
            }
            for (int s : o.saints()) {
                int prowess = people.get(s).stat(Stat.PROWESS);
                assertTrue(prowess >= Stat.SUPERHUMAN && prowess < Stat.SWORD_KING);
            }
            assertFalse(o.members().contains(people.size() - 1));
            saints += o.saints().size();
        }
        long allSaints = people.stream().filter(p -> p.stat(Stat.PROWESS) >= Stat.SUPERHUMAN && p.stat(Stat.PROWESS) < Stat.SWORD_KING).count();
        assertEquals(allSaints, saints, "every Sword Saint trained somewhere");
        assertEquals(0, all.get(0).head());
        assertFalse(all.get(0).kings().contains(1) || all.get(0).saints().contains(1), "another sanctum's god among the members");
    }

    @Test
    void mostTrainNearHome() {
        List<Person> people = people(new Random(3));
        List<Organization> all = Organizations.plan(new Random(4), people, sanctums());
        int home = 0;
        int total = 0;
        for (int o = 0; o < all.size(); o++) {
            Organizations.Sanctum s = sanctums().get(o);
            for (int saint : all.get(o).saints()) {
                Person p = people.get(saint);
                total++;
                if (Math.hypot(p.hold().x() - s.x(), p.hold().z() - s.z()) < 5000) {
                    home++;
                }
            }
        }
        assertTrue(home > total * 0.75, home + " of " + total + " at the nearest sanctum");
        assertTrue(home < total, "no one trained far from home");
    }
}
