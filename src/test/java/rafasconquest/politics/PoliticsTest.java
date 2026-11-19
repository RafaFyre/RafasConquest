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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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

        /** Who keeps a hold in each province (-1: no one), and the rival pairs. */
        final int[] holders = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        final List<int[]> rivalPairs = new java.util.ArrayList<>();

        Row(Person... rulers) {
            this.rulers = rulers;
        }

        /** Each county's seat. */
        final int[] seats = {0, 2, 4, 6, 8, 10};

        @Override
        public int holder(int province) {
            return holders[province];
        }

        @Override
        public int countySeat(int county) {
            return seats[county];
        }

        @Override
        public List<int[]> rivals() {
            return rivalPairs;
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

        /** Sea wherever this says, for battle sites. */
        java.util.function.BiPredicate<Double, Double> water = (x, z) -> false;

        @Override
        public boolean wet(double x, double z) {
            return water.test(x, z);
        }

        /** Each county's walls (none unless set), and which nations hold a port. */
        final Fort[] forts = new Fort[6];
        final boolean[] ports = new boolean[3];

        @Override
        public Fort fort(int county) {
            return forts[county];
        }

        @Override
        public boolean hasPort(int nation) {
            return ports[nation];
        }

        /** A nation with a port has one at its capital, of one district (two ships). */
        @Override
        public List<Fort> ports(int nation) {
            int[] c = capital(nation);
            return ports[nation] ? List.of(new Fort("port " + nation, c[0], c[1], rafasconquest.core.SeatCities.DISTRICT / 2 + 4, true, 0, 1))
                    : List.of();
        }
    }

    @Test
    void portAgainstPortTheFleetsMeetAtSeaThenTheArmyLandsUnderTheWalls() {
        Row lands = row();
        lands.forts[2] = new Lands.Fort("Haven", 4500, 0, 60, true, 0, 1);
        lands.ports[0] = true;
        lands.ports[1] = true;
        Politics p = new Politics();
        War w = p.declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        assertNotNull(w);
        assertEquals(rafasconquest.battle.BattleType.OCEAN, w.type());
        assertEquals(4500, w.siteX(), 1e-9);
        assertEquals(60 + Lands.Fort.OFFSHORE, w.siteZ(), 1e-9, "out at sea off its harbour");
        // The fleets are forces of their own, crewed from the ports, apart from the armies.
        assertNotNull(w.fleet(true));
        assertNotNull(w.fleet(false));
        assertEquals(2 * Lands.Fort.FLEET_CREW, w.fleet(true).soldiers(), "the port's two ships, crewed");
        assertSame(w.fleet(true), w.army(true), "at sea the fleet fights");
        int armyBefore = w.land(true).soldiers();
        p.after(w, true, 0.9, 0.6, 3, lands, new Random(5), FAR);
        assertEquals(armyBefore, w.land(true).soldiers(), "the sea battle cost the army no one");
        assertNull(w.fleet(true));
        assertNull(w.fleet(false));
        assertEquals(rafasconquest.battle.BattleType.AMPHIBIOUS, w.type(), "the sea won, they land");
        assertEquals(1, lands.owner(4), "the sea battle took no land");
        assertEquals(4500, w.siteX(), 1e-9);
        assertEquals(0, w.siteZ(), 1e-9, "at its walls");
        p.after(w, true, 0.8, 0.3, 4, lands, new Random(5), FAR);
        assertEquals(0, lands.owner(4), "the port stormed and taken");
    }

    @Test
    void aDefenderWithNoFleetCantHoldTheSeaAndAnAttackerWithNoneGoesByLand() {
        Row lands = row();
        lands.forts[2] = new Lands.Fort("Haven", 4500, 0, 60, true, 0, 1);
        lands.ports[0] = true;
        War w = new Politics().declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        assertNotNull(w);
        assertEquals(rafasconquest.battle.BattleType.AMPHIBIOUS, w.type(), "no fleet to meet it: straight to the walls");
        assertNull(w.fleet(true));

        Row dry = row();
        dry.forts[2] = new Lands.Fort("Haven", 4500, 0, 60, true, 0, 1);
        dry.ports[0] = true;
        Politics p = new Politics();
        p.drowned.put(0, 2 * Lands.Fort.FLEET_CREW); // its sailors all lost
        War v = p.declare(0, 1, new Goal(false, 2), 1, dry, new Random(1), FAR);
        assertNotNull(v);
        assertNotEquals(rafasconquest.battle.BattleType.OCEAN, v.type(), "no fleet: by land");
        assertNull(v.fleet(true));
    }

    @Test
    void aPortHasShipsOfItsOwnByItsSizeACastleNone() {
        int oneDistrict = rafasconquest.core.SeatCities.DISTRICT / 2 + 4;
        assertEquals(2, new Lands.Fort("Small", 0, 0, oneDistrict, true, 0, 1).harbourShips());
        assertEquals(6, new Lands.Fort("Big", 0, 0, 3 * rafasconquest.core.SeatCities.DISTRICT / 2 + 4, true, 0, 1).harbourShips());
        assertEquals(0, new Lands.Fort("Inland", 0, 0, oneDistrict, false, 0, 0).harbourShips());
    }

    @Test
    void aHolderOutmatchedKeepsBehindTheWallsTheBoldRideOutSooner() {
        assertTrue(Politics.holdsOut(500, 1000, ruler(10)), "outnumbered two to one");
        assertFalse(Politics.holdsOut(1000, 1000, ruler(10)), "a match in the field");
        assertTrue(Politics.holdsOut(800, 1000, ruler(10)));
        assertFalse(Politics.holdsOut(800, 1000, ruler(10, Personality.BRAVE)), "the brave ride out against the odds");
        assertTrue(Politics.holdsOut(1000, 1000, ruler(10, Personality.CRAVEN, Personality.PATIENT)), "the careful wait");
    }

    @Test
    void aWalledCountyIsBesiegedWhenItsHolderHoldsOut() {
        Row lands = row();
        lands.forts[2] = new Lands.Fort("Castle", 4500, 0, 20, false, 0, 0);
        lands.levy[0] = 50000;
        lands.levy[1] = 2000;
        Politics p = new Politics();
        War w = p.declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        assertEquals(rafasconquest.battle.BattleType.LAND, w.type(), "it marches to the border first");
        runWar(p, lands, new Random(3), 1, 100);
        assertTrue(p.chronicle().stream().anyMatch(e -> e.text().contains("at the siege of Castle")),
                "no siege: " + p.chronicle().stream().map(Politics.Event::text).toList());
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

    @Test
    void battleSitesMoveOntoDryLand() {
        Row lands = row();
        lands.water = (x, z) -> x < 500; // sea west of x = 500
        double[] site = Politics.onLand(new double[]{0, 0}, lands);
        assertTrue(site[0] - Politics.SITE_ROOM >= 500, "dry all about: " + site[0]);
        assertTrue(site[0] < 500 + Politics.SITE_ROOM + 2 * Politics.SITE_STEP, "and the nearest such: " + site[0]);
        assertTrue(Math.abs(site[1]) < 1e-9, "straight across: " + site[1]);
        double[] dry = Politics.onLand(new double[]{2000, 0}, lands);
        assertTrue(dry[0] == 2000 && dry[1] == 0, "a dry site stays");
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
        p.advance(2.01, lands, new Random(2), FAR);
        assertEquals(3000 + Politics.MARCH * 0.01, w.army(true).x(), 1e-6, "a hundredth of a day's march");
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
        p.advance(2.08, lands, new Random(2), FAR);
        p.advance(2.16, lands, new Random(2), FAR); // 0.16 days: 3,200 blocks, by the castle and on
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

    // ---- feuds ----

    static Person lord(int nation, int liege, Personality... traits) {
        int[] stats = new int[Stat.values().length];
        stats[Stat.MARTIAL.ordinal()] = 12;
        stats[Stat.PROWESS.ordinal()] = 15;
        return new Person("L", "Y", false, 30, 0, 0, 0, stats, List.of(traits), new Person.Hold(Person.Kind.TOWN, 0, 0, 0), liege, nation, false);
    }

    /**
     * Nation 1's two lords: person 3 holds county 2 (provinces 4, 5; seat 4), person 4 county 3 (6, 7; seat 7, so 6 on
     * the border is no seat); rivals, thinking ill of each other.
     */
    static Row feudingRow() {
        Row lands = new Row(ruler(10), ruler(10), ruler(10), lord(1, 1, Personality.WRATHFUL, Personality.AMBITIOUS), lord(1, 1));
        lands.holders[4] = 3;
        lands.seats[3] = 7;
        lands.holders[7] = 4;
        lands.rivalPairs.add(new int[]{3, 4});
        lands.opinion = -60;
        return lands;
    }

    @Test
    void eachProvinceIsItsCountySeatHoldersElseItsRulers() {
        Row lands = feudingRow();
        Politics p = new Politics();
        assertEquals(Feud.Lord.of(3), p.lord(5, lands, java.util.Map.of()));
        assertEquals(Feud.Lord.of(4), p.lord(7, lands, java.util.Map.of()));
        assertEquals(Feud.Lord.of(1), p.lord(8, lands, java.util.Map.of()), "no hold at its seat: the ruler's");
        java.util.UUID player = java.util.UUID.randomUUID();
        assertEquals(Feud.Lord.of(player), p.lord(5, lands, java.util.Map.of(4, player)), "a player's county");
    }

    @Test
    void rivalLordsFallToFeudingAndEveryFeudEndsInATruce() {
        Row lands = feudingRow();
        Politics p = new Politics();
        Random random = new Random(3);
        boolean began = false;
        boolean ended = false;
        for (int day = 1; day <= 400 && !ended; day++) {
            p.feuds.day(p, day, lands, random, FAR);
            for (int step = 1; step <= 20; step++) {
                p.feuds.advance(p, day + step / 20.0, lands, random, FAR);
            }
            began |= !p.feuds().isEmpty();
            ended = began && p.feuds().isEmpty();
            if (ended) {
                long until = p.feuds.truces.get(Feuds.pair(Feud.Lord.of(3), Feud.Lord.of(4)));
                assertEquals(day + Feuds.TRUCE_DAYS, until, "a three-day truce");
            }
        }
        assertTrue(began, "the rivals began a feud");
        assertTrue(ended, "and it ended");
    }

    @Test
    void aBeatenLordYieldsTheWinnerMayTakeABorderProvinceNeverASeat() {
        boolean tookLand = false;
        for (int seed = 0; seed < 40 && !tookLand; seed++) {
            Row lands = feudingRow();
            Politics p = new Politics();
            Feud f = p.feud(Feud.Lord.of(3), Feud.Lord.of(4), 1, lands, FAR);
            assertNotNull(f);
            f.scoreA = 3;
            p.feuds.skirmished(p, f, true, 0.9, 0.5, 2, lands, new Random(seed * 0x9E3779B97F4A7C15L), FAR);
            assertTrue(p.feuds().isEmpty(), "two points more: four ahead, the other yields");
            assertTrue(p.dead.get(1) > 0, "the skirmish's dead count against their nation");
            if (!p.feuds.lords.isEmpty()) {
                tookLand = true;
                assertEquals(java.util.Map.of(6, Feud.Lord.of(3)), p.feuds.lords, "the border province, not the seat behind it");
                assertEquals(Feud.Lord.of(3), p.lord(6, lands, java.util.Map.of()));
            }
        }
        assertTrue(tookLand, "an ambitious winner takes land often enough");
    }

    @Test
    void aWarsConquestClearsTheLandFeudsGave() {
        Row lands = feudingRow();
        Politics p = new Politics();
        p.feuds.lords.put(5, Feud.Lord.of(4));
        War w = p.declare(0, 1, new Goal(false, 2), 1, lands, new Random(1), FAR);
        assertNotNull(w);
        p.after(w, true, 0.9, 0.5, 2, lands, new Random(1), FAR);
        assertEquals(0, lands.owner(5));
        assertFalse(p.feuds.lords.containsKey(5));
    }

    @Test
    void feudsAreKeptWithTheWorld() {
        Row lands = feudingRow();
        Politics p = new Politics();
        Feud f = p.feud(Feud.Lord.of(3), Feud.Lord.of(4), 1, lands, FAR);
        assertTrue(p.callClash(f, Feud.Kind.DUEL, 1, 0.5, lands, FAR));
        p.feuds.lords.put(5, Feud.Lord.of(java.util.UUID.randomUUID()));
        p.feuds.fallen.add(7);
        Politics back = Politics.load(p.save());
        assertEquals(1, back.feuds().size());
        assertEquals(Feud.Kind.DUEL, back.feuds().get(0).clash().kind());
        assertEquals(p.feuds.lords, back.feuds.lords);
        assertEquals(p.feuds.fallen, back.feuds.fallen);
    }
}
