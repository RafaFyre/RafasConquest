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
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoliticsTest {
    /**
     * A row of twelve provinces, 1000 blocks apart; counties of two (county = province / 2), duchies of two counties.
     * Nation 0 holds 0-3 (seat 0), nation 1 holds 4-9 (seat 9), nation 2 holds 10-11 (seat 11). Rulers are people 0-2.
     */
    static final class Row implements Lands {
        final int[] owner = {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2};
        final int[] levy = {1000, 1000, 1000};
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
            return new int[]{0, 9, 11}[nation];
        }

        @Override
        public int[] capital(int nation) {
            return centre(seat(nation));
        }

        /** Each nation's capital, then a castle at each end of its land. */
        @Override
        public List<Stronghold> strongholds(int nation) {
            int[] c = capital(nation);
            return List.of(new Stronghold("capital " + nation, c[0], c[1]),
                    new Stronghold("castle " + nation + "a", centre(nation == 0 ? 3 : nation == 1 ? 4 : 10)[0], 0),
                    new Stronghold("castle " + nation + "b", centre(nation == 0 ? 1 : nation == 1 ? 7 : 11)[0], 0));
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
        public int county(int province) {
            return province / 2;
        }

        @Override
        public int counties() {
            return owner.length / 2;
        }

        @Override
        public int duchy(int county) {
            return county / 2;
        }

        @Override
        public String titleName(Goal goal) {
            return (goal.duchy() ? "Duchy " : "County ") + goal.index();
        }

        @Override
        public int levy(int nation) {
            return levy[nation];
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
        public boolean fightForReal(War war, int attackers, int defenders) {
            return false;
        }

        @Override
        public void raised(War war, boolean attacking) {
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

    /** Runs the politics on, a twentieth of a day at a time, until the war is over (or {@code days} pass). */
    static void runWar(Politics p, Row lands, Random random, double from, int days) {
        for (int step = 1; step <= days * 20 && !p.wars().isEmpty(); step++) {
            double now = from + step / 20.0;
            if (step % 20 == 0) {
                p.day((long) now, lands, random, FAR);
            }
            p.advance(now, lands, random, FAR);
        }
    }

    @Test
    void titlesBesideUsAreClaimedButNotTheEnemySeat() {
        List<Politics.Claim> claims = Politics.claims(0, row());
        assertTrue(claims.stream().anyMatch(c -> !c.goal().duchy() && c.goal().index() == 2 && c.defender() == 1), "the county beside us");
        assertTrue(claims.stream().anyMatch(c -> c.goal().duchy() && c.goal().index() == 1), "the duchy beside us");
        assertFalse(claims.stream().anyMatch(c -> !c.goal().duchy() && c.goal().index() == 4), "the county with nation 1's seat");
        assertFalse(claims.stream().anyMatch(c -> !c.goal().duchy() && c.goal().index() == 3), "a county nowhere near us");
    }

    @Test
    void aHoleInOurLandIsWantedMost() {
        Row lands = row();
        // Nation 0 also holds 6-7: nation 1's 4-5 is now a hole between our two pieces.
        lands.owner[6] = 0;
        lands.owner[7] = 0;
        List<Politics.Claim> claims = Politics.claims(0, lands);
        Politics.Claim hole = claims.stream().filter(c -> !c.goal().duchy() && c.goal().index() == 2).findFirst().orElseThrow();
        Politics.Claim plain = Politics.claims(0, row()).stream().filter(c -> !c.goal().duchy() && c.goal().index() == 2).findFirst().orElseThrow();
        assertTrue(hole.want() > plain.want() + 2, hole.want() + " vs " + plain.want());
    }

    @Test
    void armiesMusterADayAtTheirStrongholdNearestTheFieldThenMarch() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        assertNotNull(w);
        assertEquals(2, w.target());
        assertEquals(3500, w.siteX(), 1e-9, "between our province 3 and their 4");
        assertEquals(3000, w.army(true).x(), 1e-9, "the attacker musters at its castle nearest the field");
        assertEquals("castle 0a", w.army(true).place());
        assertEquals(4000, w.army(false).x(), 1e-9, "the defender musters at its castle nearest the field");
        p.advance(1.5, lands, new Random(2), FAR);
        p.advance(1.9, lands, new Random(2), FAR);
        assertEquals(3000, w.army(true).x(), 1e-9, "marched while mustering");
        p.advance(2.1, lands, new Random(2), FAR);
        assertEquals(3200, w.army(true).x(), 1e-6, "a tenth of a day's march");
    }

    @Test
    void aCeremonialMusterIsAtTheCapitalThenByTheCityNearestTheField() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        w.attackerArmy = null; // its people back home, to be raised again
        Army army = p.raise(w, true, 1, lands, Politics.Muster.CEREMONIAL);
        assertEquals(0, army.x(), 1e-9, "not at the capital");
        assertEquals("capital 0", army.place());
        assertEquals(1, army.stops.size());
        assertEquals(3000, army.heading(w)[0], 1e-9, "not by the castle nearest the field");
        w.attackerArmy = army;
        w.defenderArmy.readyAt = 100; // the defender stays home: no battle
        p.advance(2, lands, new Random(2), FAR);
        p.advance(2.8, lands, new Random(2), FAR);
        p.advance(3.6, lands, new Random(2), FAR); // 1.6 days: 3,200 blocks, by the castle and on
        assertEquals(3200, army.x(), 1e-6);
        assertEquals(3500, army.heading(w)[0], 1e-9, "the castle not passed");
    }

    @Test
    void theStrongTakeTheTitleCountyByCounty() {
        Row lands = row();
        lands.levy[0] = 50000;
        lands.levy[1] = 2000;
        Politics p = new Politics();
        p.declare(0, 1, new Goal(true, 1), 1, lands, new Random(1), FAR);
        runWar(p, lands, new Random(3), 1, 100);
        assertTrue(p.wars().isEmpty(), "the war never ended");
        for (int province = 4; province <= 7; province++) {
            assertEquals(0, lands.owner(province), "province " + province + " of the duchy");
        }
        assertEquals(1, lands.owner(9), "the defender's seat taken");
        assertTrue(p.truces.containsKey(Politics.pair(0, 1)), "no truce after the war");
    }

    @Test
    void aBeatenAttackerChangesNoLand() {
        Row lands = row();
        lands.levy[0] = 300;
        lands.levy[1] = 50000;
        Politics p = new Politics();
        p.declare(0, 1, new Goal(true, 1), 1, lands, new Random(1), FAR);
        runWar(p, lands, new Random(4), 1, 100);
        assertTrue(p.wars().isEmpty(), "the war never ended");
        for (int province = 4; province <= 9; province++) {
            assertEquals(1, lands.owner(province), "province " + province + " changed hands");
        }
        assertTrue(p.dead.getOrDefault(0, 0) > 0, "the beaten attacker lost no one");
    }

    @Test
    void aDefenderWithNoOneLeftCedesTheRest() {
        Row lands = row();
        Politics p = new Politics();
        War w = p.declare(0, 1, new Goal(true, 1), 1, lands, new Random(1), FAR);
        lands.levy[1] = 0; // their people spent: no army can be raised again
        p.after(w, true, 0.9, 0.4, 3, lands, new Random(5), FAR);
        assertTrue(p.wars().isEmpty());
        for (int province = 4; province <= 7; province++) {
            assertEquals(0, lands.owner(province), "province " + province + " of the duchy not ceded");
        }
        assertTrue(p.chronicle().stream().anyMatch(e -> e.text().contains("ceded")));
    }

    @Test
    void theDeadAreMadeGoodSlowly() {
        Row lands = row();
        Politics p = new Politics();
        p.dead.put(1, 300);
        assertEquals(700, p.manpower(1, lands));
        p.day(2, lands, new Random(1), FAR);
        assertEquals(720, p.manpower(1, lands), "2 % of the levy back each day");
    }

    @Test
    void warlikeRulersMoreSoThanPeacefulOnes() {
        assertTrue(Politics.aggression(ruler(18, Personality.AMBITIOUS, Personality.WRATHFUL))
                > 5 * Politics.aggression(ruler(3, Personality.CONTENT, Personality.CRAVEN)));
        assertTrue(Politics.temper(ruler(5, Personality.CRAVEN)) > Politics.temper(ruler(5, Personality.WRATHFUL)));
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
        War w = p.declare(0, 1, new Goal(true, 1), 4, lands, new Random(1), FAR);
        p.conquered.put(4, 0);
        p.dead.put(0, 55);
        Politics back = Politics.load(p.save());
        War b = back.wars().get(0);
        assertEquals(w.goal(), b.goal());
        assertEquals(w.target(), b.target());
        assertEquals(w.army(true).soldiers(), b.army(true).soldiers());
        assertEquals(w.army(false).x(), b.army(false).x(), 1e-9);
        assertEquals(List.of(2), b.defenderAllies());
        assertTrue(back.allied(1, 2));
        assertEquals(0, back.conquered().get(4));
        assertEquals(55, back.dead.get(0));
        assertEquals(p.chronicle(), back.chronicle());
    }
}
