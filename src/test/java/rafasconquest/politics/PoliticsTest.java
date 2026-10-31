package rafasconquest.politics;

import org.junit.jupiter.api.Test;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Stat;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoliticsTest {
    /**
     * A row of nine provinces: nation 0 holds 0-2 (seat 0), nation 1 holds 3-5 (seat 5), nation 2 holds 6-8 (seat 8).
     * Each nation's ruler is person n.
     */
    static final class Row implements Lands {
        final int[] owner = {0, 0, 0, 1, 1, 1, 2, 2, 2};
        final int[] soldiers = {1000, 1000, 1000};
        final Person[] rulers;
        int opinion;

        Row(Person... rulers) {
            this.rulers = rulers;
        }

        @Override
        public int nations() {
            return 3;
        }

        @Override
        public int provinces() {
            return owner.length;
        }

        @Override
        public boolean canWage(int nation) {
            return true;
        }

        @Override
        public int ruler(int nation) {
            return nation;
        }

        @Override
        public Person person(int index) {
            return rulers[index];
        }

        @Override
        public int opinion(int a, int b) {
            return opinion;
        }

        @Override
        public int owner(int province) {
            return owner[province];
        }

        @Override
        public void transfer(int province, int nation) {
            owner[province] = nation;
        }

        @Override
        public int seat(int nation) {
            return new int[]{0, 5, 8}[nation];
        }

        @Override
        public Set<Integer> neighbours(int province) {
            Set<Integer> n = new TreeSet<>();
            if (province > 0) {
                n.add(province - 1);
            }
            if (province < owner.length - 1) {
                n.add(province + 1);
            }
            return n;
        }

        @Override
        public int soldiers(int nation) {
            return soldiers[nation];
        }

        @Override
        public String nationName(int nation) {
            return "N" + nation;
        }

        @Override
        public String provinceName(int province) {
            return "P" + province;
        }

        @Override
        public int[] centre(int province) {
            return new int[]{province * 1000, 0};
        }
    }

    static final Politics.Front FAR = new Politics.Front() {
        @Override
        public boolean fightForReal(War war, int[] site, int attackers, int defenders) {
            return false;
        }

        @Override
        public void declared(War war) {
        }
    };

    static Person ruler(int martial, Personality... traits) {
        int[] stats = new int[Stat.values().length];
        stats[Stat.MARTIAL.ordinal()] = martial;
        stats[Stat.DIPLOMACY.ordinal()] = 10;
        return new Person("R", "X", false, 40, 0, 0, 0, stats, List.of(traits), new Person.Hold(Person.Kind.CAPITAL, 0, 0, 0), -1, 0, false);
    }

    static Row row() {
        return new Row(ruler(10), ruler(10), ruler(10));
    }

    @Test
    void aWarIsOverABorderProvinceNotTheSeat() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, 1, lands, new Random(1), FAR);
        assertNotNull(w);
        assertEquals(3, w.goal(), "nation 1's only province beside nation 0 is 3");
        assertTrue(p.atWar(0) && p.atWar(1));
        assertFalse(p.atWar(2));
        assertNull(p.declare(0, 2, 1, lands, new Random(1), FAR), "no border with nation 2");
    }

    @Test
    void anAllyComesToTheDefence() {
        Row lands = row();
        Politics p = new Politics();
        p.alliances.add(Politics.pair(1, 2));
        War w = p.declare(0, 1, 1, lands, new Random(1), FAR);
        assertEquals(List.of(2), w.defenderAllies());
        assertTrue(p.atWar(2));
        assertEquals(1000 + 500, Politics.soldiers(w, false, lands), "an ally brings half its army");
    }

    @Test
    void theStrongWinTheProvinceAndATruceFollows() {
        Row lands = row();
        lands.soldiers[0] = 20000;
        lands.soldiers[1] = 500;
        Politics p = new Politics();
        p.declare(0, 1, 1, lands, new Random(1), FAR);
        Random random = new Random(2);
        long day = 2;
        while (!p.wars().isEmpty() && day < 60) {
            p.day(day++, lands, random, FAR);
        }
        assertTrue(p.wars().stream().noneMatch(w -> w.attacker() == 0 && w.defender() == 1), "the war never ended");
        assertEquals(0, lands.owner(3), "the province fought over went to the winner");
        assertEquals(0, p.conquered().get(3));
        assertTrue(p.truces.containsKey(Politics.pair(0, 1)), "no truce after the war");
    }

    @Test
    void aWarDraggingOnEndsInPeace() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, 1, lands, new Random(1), FAR);
        w.score = 0;
        p.day(1 + Politics.LONG_WAR, lands, new Random(3), new Politics.Front() {
            @Override
            public boolean fightForReal(War war, int[] site, int attackers, int defenders) {
                return true; // every battle "fought for real": none settles today
            }

            @Override
            public void declared(War war) {
            }
        });
        assertTrue(p.wars().stream().noneMatch(x -> x.id() == w.id()), "a 30-day war still going");
        assertEquals(1, lands.owner(3), "a province changed hands in a drawn war");
    }

    @Test
    void aRealBattleCountsMore() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, 1, lands, new Random(1), FAR);
        p.realBattle(w.id(), true, 2, lands);
        assertEquals(25, w.score());
        assertEquals(1, w.battles());
    }

    @Test
    void warlikeRulersMoreSoThanPeacefulOnes() {
        double hawk = Politics.aggression(ruler(18, Personality.AMBITIOUS, Personality.WRATHFUL));
        double dove = Politics.aggression(ruler(3, Personality.CONTENT, Personality.CRAVEN));
        assertTrue(hawk > 5 * dove, hawk + " vs " + dove);
    }

    @Test
    void warsBreakOutInTimeAmongWarlikeNeighbours() {
        Row lands = new Row(ruler(18, Personality.AMBITIOUS, Personality.WRATHFUL), ruler(18, Personality.BRAVE), ruler(18, Personality.ARROGANT));
        lands.opinion = -30;
        Politics p = new Politics();
        Random random = new Random(5);
        for (long day = 1; day <= 120; day++) {
            p.day(day, lands, random, FAR);
        }
        assertTrue(p.chronicle().stream().anyMatch(e -> e.text().contains("declared war")), "no war in 120 days");
    }

    @Test
    void thePoliticsAreKept() {
        Row lands = row();
        Politics p = new Politics();
        p.alliances.add(Politics.pair(1, 2));
        War w = p.declare(0, 1, 4, lands, new Random(1), FAR);
        w.score = 37;
        p.conquered.put(4, 0);
        Politics back = Politics.load(p.save());
        assertEquals(1, back.wars().size());
        assertEquals(37, back.wars().get(0).score());
        assertEquals(List.of(2), back.wars().get(0).defenderAllies());
        assertTrue(back.allied(1, 2));
        assertEquals(0, back.conquered().get(4));
        assertEquals(p.chronicle(), back.chronicle());
    }
}
