package rafasconquest.people;

import org.junit.jupiter.api.Test;
import rafasconquest.core.Countryside;
import rafasconquest.core.Nations;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.SeatCities;
import rafasconquest.core.TitleMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RealmPlannerTest {
    private static final int BPP = 32;
    private static final int CELL = 48;
    private static final int CELLS = 24;

    private record World(TitleMap titles, List<SeatCities.City> cities, Countryside.Plan country, Nations.Plan nations, Realm realm,
                         int[] homeland) {
    }

    /** A land of square provinces 1,536 blocks a side, the sea along the west, centred on spawn. */
    private static World world(long seed) {
        int width = CELLS * CELL;
        int origin = -width * BPP / 2;
        int[] provinces = new int[width * width];
        boolean[] sea = new boolean[width * width];
        List<ProvinceMap.Centre> centres = new ArrayList<>();
        int[][] index = new int[CELLS][CELLS];
        for (int cz = 0; cz < CELLS; cz++) {
            for (int cx = 0; cx < CELLS; cx++) {
                index[cz][cx] = cx == 0 ? -1 : centres.size();
                if (cx > 0) {
                    centres.add(new ProvinceMap.Centre(origin + cx * CELL * BPP + CELL * BPP / 2, origin + cz * CELL * BPP + CELL * BPP / 2, false));
                }
            }
        }
        for (int z = 0; z < width; z++) {
            for (int x = 0; x < width; x++) {
                int p = index[z / CELL][x / CELL];
                provinces[z * width + x] = p < 0 ? ProvinceMap.WATER : p;
                sea[z * width + x] = p < 0;
            }
        }
        TitleMap titles = TitleMap.build(seed, width, width, provinces, centres.size(), sea);
        SeatCities.Grid grid = new SeatCities.Grid(origin, origin, BPP, width, width, provinces, centres);
        List<SeatCities.City> cities = SeatCities.plan(seed, grid, titles);
        Countryside.Plan country = Countryside.plan(seed, grid, titles, cities);
        Nations.Plan nations = Nations.plan(seed, titles, p -> new int[]{centres.get(p).x(), centres.get(p).z()}, 1000, 8000);
        List<RealmPlanner.Seat> seats = nations.nations().stream().map(n -> new RealmPlanner.Seat(n.tier().ordinal(), n.seat(), n.x(), n.z(), n.sanctum())).toList();
        // Bands of land from north to south: snow, taiga, plains, savanna, desert, and so on round.
        int[] homeland = new int[centres.size()];
        for (int p = 0; p < homeland.length; p++) {
            homeland[p] = Math.floorMod((centres.get(p).z() - origin) / (CELL * BPP * 4) + Homeland.SNOW.ordinal(), Homeland.values().length);
        }
        Realm realm = RealmPlanner.plan(seed, titles, centres, country.cities(), country.settlements(), seats, nations.holder(), homeland, true);
        return new World(titles, country.cities(), country, nations, realm, homeland);
    }

    @Test
    void everyHoldHasOneHolderAndEveryNationARuler() {
        World w = world(5);
        Set<Integer> holds = new HashSet<>();
        for (Person p : w.realm().people()) {
            assertTrue(holds.add(p.hold().province()), "two holders of one hold");
        }
        for (SeatCities.City c : w.cities()) {
            assertTrue(holds.contains(c.province()), "a city with no holder");
        }
        for (Countryside.Settlement s : w.country().settlements()) {
            if (s.kind() == Countryside.Kind.CASTLE) {
                assertTrue(holds.contains(s.province()), "a castle with no holder");
            }
        }
        for (int n = 0; n < w.nations().nations().size(); n++) {
            int seat = w.nations().nations().get(n).seat();
            int ruler = w.realm().holderOf(seat);
            assertTrue(ruler >= 0, "a nation with no ruler");
            assertEquals(Person.Kind.CAPITAL, w.realm().people().get(ruler).hold().kind());
            assertEquals(-1, w.realm().people().get(ruler).liege(), "a ruler with a liege");
        }
    }

    @Test
    void liegesServeTheSameNationUpToItsRuler() {
        World w = world(6);
        List<Person> people = w.realm().people();
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.nation() < 0) {
                assertEquals(-1, p.liege(), "an unowned hold's holder with a liege");
                continue;
            }
            int at = i;
            for (int steps = 0; people.get(at).liege() >= 0; steps++) {
                assertTrue(steps < 10, "a loop of lieges");
                at = people.get(at).liege();
                assertEquals(p.nation(), people.get(at).nation(), "a liege of another nation");
            }
            assertEquals(w.nations().nations().get(p.nation()).seat(), people.get(at).hold().province(), "a chain not ending at the ruler");
        }
    }

    @Test
    void skillsTraitsCultureAndFaithFollowTheRules() {
        int peak = 0;
        int superhuman = 0;
        int people = 0;
        for (long seed = 0; seed < 4; seed++) {
            World w = world(seed);
            Realm realm = w.realm();
            long broad = realm.faiths().stream().filter(f -> !f.cult()).count();
            long cults = realm.faiths().stream().filter(Faith::cult).count();
            assertTrue(broad >= 1 && broad <= 5, broad + " broad faiths");
            assertEquals(4, cults);
            int peakHere = 0;
            for (Person p : realm.people()) {
                people++;
                for (Stat s : Stat.values()) {
                    assertTrue(p.stat(s) >= 0 && p.stat(s) <= s.max(), s + " " + p.stat(s));
                }
                superhuman += p.stat(Stat.PROWESS) >= Stat.SUPERHUMAN ? 1 : 0;
                peakHere += p.stat(Stat.PROWESS) >= Stat.SWORD_GOD ? 1 : 0;
                assertEquals(RealmPlanner.TRAITS, p.traits().size());
                for (Personality t : p.traits()) {
                    assertFalse(p.has(t.opposite()), "both " + t + " and its opposite");
                }
                if (p.nation() >= 0) {
                    assertEquals(realm.nationCulture()[p.nation()], p.culture(), "a character not of the nation's culture");
                }
            }
            assertTrue(peakHere <= Stat.MAX_SWORD_GODS, peakHere + " Sword Gods");
            for (int i = 0; i < Stat.TITLE_FLOORS.length; i++) {
                int floor = Stat.TITLE_FLOORS[i];
                int ceiling = i == 0 ? Integer.MAX_VALUE : Stat.TITLE_FLOORS[i - 1];
                long holders = realm.people().stream().filter(p -> p.stat(Stat.PROWESS) >= floor && p.stat(Stat.PROWESS) < ceiling).count();
                assertTrue(holders <= Stat.TITLE_LIMITS[i], holders + " " + Stat.superhumanTier(floor) + "s");
            }
            peak += peakHere;
        }
        assertTrue(superhuman > 0 && superhuman < people / 5, superhuman + " superhuman of " + people);
    }

    @Test
    void everySanctumIsHeldByASwordGod() {
        int sanctums = 0;
        for (long seed = 0; seed < 4; seed++) {
            World w = world(seed);
            for (int n = 0; n < w.nations().nations().size(); n++) {
                rafasconquest.core.Nations.Nation nation = w.nations().nations().get(n);
                if (!nation.sanctum()) {
                    continue;
                }
                sanctums++;
                assertEquals(TitleMap.Tier.COUNTY, nation.tier(), "a sanctum that isn't a City-state");
                Person holder = w.realm().people().get(w.realm().holderOf(nation.seat()));
                assertTrue(holder.stat(Stat.PROWESS) >= Stat.SWORD_GOD, "a sanctum held by prowess " + holder.stat(Stat.PROWESS));
            }
            assertTrue(w.nations().nations().stream().filter(rafasconquest.core.Nations.Nation::sanctum).count()
                    <= rafasconquest.core.Nations.SANCTUMS);
        }
        assertTrue(sanctums > 0, "no sanctums at all");
    }

    @Test
    void theRealmIsKeptWhole() throws IOException {
        Realm realm = world(9).realm();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        realm.write(new DataOutputStream(bytes));
        Realm back = Realm.read(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray())));
        assertEquals(realm.people().size(), back.people().size());
        assertEquals(realm.bonds(), back.bonds());
        for (int i = 0; i < realm.people().size(); i++) {
            Person a = realm.people().get(i);
            Person b = back.people().get(i);
            assertEquals(a.name(), b.name());
            assertEquals(a.hold(), b.hold());
            assertEquals(a.liege(), b.liege());
            assertEquals(a.traits(), b.traits());
            assertTrue(java.util.Arrays.equals(a.stats(), b.stats()));
        }
        assertEquals(realm.cultures().get(0).name(), back.cultures().get(0).name());
        assertEquals(realm.cultures().get(3).homeland(), back.cultures().get(3).homeland());
        assertEquals(realm.demographics().size(), back.demographics().size());
        for (int n = 0; n < realm.demographics().size(); n++) {
            Demographics a = realm.demographics().get(n);
            Demographics b = back.demographics().get(n);
            assertEquals(a.population(), b.population());
            assertTrue(java.util.Arrays.equals(a.cultures(), b.cultures()));
            assertTrue(java.util.Arrays.equals(a.races(), b.races()));
        }
    }

    @Test
    void oneCultureForEachKindOfLand() {
        World w = world(3);
        Realm realm = w.realm();
        assertEquals(Homeland.values().length, realm.cultures().size());
        for (Homeland h : Homeland.values()) {
            Culture c = realm.cultures().get(h.ordinal());
            assertEquals(h, c.homeland());
            assertEquals(h.ordinal(), c.id());
            assertEquals(h, h.race().homeland());
        }
        for (int n = 0; n < w.nations().nations().size(); n++) {
            assertEquals(w.homeland()[w.nations().nations().get(n).seat()], realm.nationCulture()[n], "a nation not of its seat's culture");
        }
        for (Person p : realm.people()) {
            if (p.nation() < 0) {
                assertEquals(w.homeland()[p.hold().province()], p.culture(), "an unowned hold's holder not of its land's culture");
            }
        }
    }

    @Test
    void everyNationHasABroadFaith() {
        for (long seed = 0; seed < 4; seed++) {
            Realm realm = world(seed).realm();
            for (int n = 0; n < realm.nationFaith().length; n++) {
                int faith = realm.nationFaith()[n];
                assertTrue(faith >= 0 && faith < realm.faiths().size(), "a nation of no faith");
                assertFalse(realm.faiths().get(faith).cult(), "a nation keeping a cult");
            }
        }
    }

    @Test
    void demographicsShareOutEveryNationsPeople() {
        for (long seed = 0; seed < 4; seed++) {
            World w = world(seed);
            Realm realm = w.realm();
            assertEquals(w.nations().nations().size(), realm.demographics().size());
            for (Demographics d : realm.demographics()) {
                assertTrue(d.population() > 0, "a nation of no people");
                for (float[] shares : new float[][]{d.cultures(), d.races()}) {
                    assertEquals(Homeland.values().length, shares.length);
                    float total = 0;
                    for (float s : shares) {
                        assertTrue(s >= 0 && s <= 1, "a share of " + s);
                        total += s;
                    }
                    assertEquals(1, total, 1e-4, "shares adding up to " + total);
                }
            }
        }
    }

    @Test
    void racesFollowTheLandAndCulturesTheSeat() {
        int mixed = 0;
        for (long seed = 0; seed < 4; seed++) {
            World w = world(seed);
            Realm realm = w.realm();
            int[] holder = w.nations().holder();
            for (int n = 0; n < realm.demographics().size(); n++) {
                Demographics d = realm.demographics().get(n);
                java.util.Set<Integer> lands = new HashSet<>();
                for (int p = 0; p < holder.length; p++) {
                    if (holder[p] == n) {
                        lands.add(w.homeland()[p]);
                    }
                }
                // Every land the nation holds has people of its race; the wanderers are few.
                float ofItsLands = 0;
                for (int h : lands) {
                    assertTrue(d.races()[h] > 0, "no one of the race of a land it holds");
                    ofItsLands += d.races()[h];
                }
                assertTrue(ofItsLands >= 1 - RealmPlanner.WANDERERS - RealmPlanner.WANDERERS_MORE - 1e-4, ofItsLands + " of its lands' races");
                if (lands.size() == 1) {
                    assertEquals(Demographics.largest(d.races()), lands.iterator().next(), "most not of the race of its only land");
                } else {
                    mixed++;
                }
                // Its culture: at least as common as that land's race, and never less than it has made its own.
                int culture = realm.nationCulture()[n];
                assertTrue(d.cultures()[culture] >= d.races()[culture] - 1e-4, "its culture rarer than its seat's race");
                assertTrue(d.cultures()[culture] >= RealmPlanner.ASSIMILATED - 1e-4, "its culture held by only " + d.cultures()[culture]);
            }
        }
        assertTrue(mixed > 0, "no nation spans more than one kind of land");
    }

    @Test
    void greaterNationsHaveMorePeople() {
        double[] total = new double[TitleMap.Tier.values().length];
        int[] count = new int[total.length];
        for (long seed = 0; seed < 4; seed++) {
            World w = world(seed);
            for (int n = 0; n < w.nations().nations().size(); n++) {
                int tier = w.nations().nations().get(n).tier().ordinal();
                total[tier] += w.realm().demographics().get(n).population();
                count[tier]++;
            }
        }
        double previous = 0;
        for (int tier = 0; tier < total.length; tier++) {
            if (count[tier] == 0) {
                continue;
            }
            double mean = total[tier] / count[tier];
            assertTrue(mean > previous, TitleMap.Tier.values()[tier] + " nations average " + mean + " people, fewer than smaller ones");
            previous = mean;
        }
    }
}
