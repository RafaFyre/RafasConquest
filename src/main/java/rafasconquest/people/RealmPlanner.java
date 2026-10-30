package rafasconquest.people;

import rafasconquest.core.Countryside;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.SeatCities;
import rafasconquest.core.TitleMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Plans a world's people from its map, the same every time for the same seed:
 * <ul>
 *   <li><b>Cultures</b>: one for each empire's lands. A nation's is the culture of the lands its seat stands in, and
 *       every one of its characters has it; a hold no nation keeps has its lands' culture.</li>
 *   <li><b>Faiths</b>: 3 to 5 broad faiths, each spread over the lands nearest where it began, and 4 cults, each from
 *       a home in one empire's lands. A nation keeps its seat's faith, devoutly or not ({@code piety}); its characters
 *       mostly share it, the more devout the nation the more so, the rest keeping a cult or another faith.</li>
 *   <li><b>Holders</b>: one for each capital, city, town and castle, with skills, traits, an age and a liege: the holder
 *       of the next higher seat above them in the same nation (a county's town under its duchy's city, and so on up),
 *       else the nation's ruler. Holds no nation keeps answer to no one.</li>
 *   <li><b>Bonds</b>: friends and rivals among neighbours and those serving the same liege, likelier where their
 *       personalities agree (friends) or clash (rivals).</li>
 * </ul>
 */
public final class RealmPlanner {
    /** A nation: its rank (a title tier's ordinal), its seat province, and where its seat stands. */
    public record Seat(int tier, int province, int x, int z) {
    }

    static final int CULTS = 4;
    /** Each character: their own faith (not the nation's) this often at piety 0, and this much less at piety 1. */
    static final double STRAY = 0.3;
    static final double STRAY_DEVOUT = 0.25;
    /** A character is superhuman in prowess this often; each step further up, this likely to go on. */
    static final double SUPERHUMAN = 0.05;
    static final double SUPERHUMAN_STEP = 0.85;
    static final int TRAITS = 3;
    /** Bonds are looked for among this many nearest holders and those serving the same liege. */
    private static final int NEIGHBOURS = 6;
    private static final double FRIEND = 0.12;
    private static final double RIVAL = 0.1;
    private static final int MAX_BONDS = 2;
    private static final long SALT = 0x5045_4F50_4C45_5321L;

    private RealmPlanner() {
    }

    public static Realm plan(long seed, TitleMap t, List<ProvinceMap.Centre> centres, List<SeatCities.City> cities,
                             List<Countryside.Settlement> settlements, List<Seat> nations, int[] holder) {
        Random random = new Random(seed ^ SALT);
        int empires = t.empires();

        // Cultures: one for each empire's lands.
        List<Culture> cultures = new ArrayList<>();
        for (int e = 0; e < empires; e++) {
            cultures.add(Culture.make(e, random));
        }

        // Faiths: the broad ones spread from their beginnings over the nearest lands; the cults each from a home.
        List<Faith> faiths = new ArrayList<>();
        int broad = Math.min(empires, 3 + random.nextInt(3));
        List<Integer> origins = spreadOut(t, centres, broad, random);
        for (int i = 0; i < origins.size(); i++) {
            faiths.add(Faith.broad(i, random));
        }
        int[] empireFaith = new int[empires];
        for (int e = 0; e < empires; e++) {
            int[] at = seatAt(t, centres, t.empireSeat()[e]);
            int nearest = 0;
            double best = Double.MAX_VALUE;
            for (int i = 0; i < origins.size(); i++) {
                int[] o = seatAt(t, centres, t.empireSeat()[origins.get(i)]);
                double d = Math.hypot(o[0] - at[0], o[1] - at[1]);
                if (d < best) {
                    best = d;
                    nearest = i;
                }
            }
            empireFaith[e] = nearest;
        }
        Set<String> cultNames = new HashSet<>();
        int[] cultHome = new int[CULTS];
        for (int i = 0; i < CULTS; i++) {
            cultHome[i] = random.nextInt(Math.max(1, empires));
            faiths.add(Faith.cult(faiths.size(), random, cultNames));
        }

        // Nations: culture and faith of their seat's lands, and a piety of their own.
        int[] nationCulture = new int[nations.size()];
        int[] nationFaith = new int[nations.size()];
        float[] nationPiety = new float[nations.size()];
        for (int n = 0; n < nations.size(); n++) {
            int e = empireOf(t, nations.get(n).province());
            nationCulture[n] = e;
            nationFaith[n] = empireFaith[e];
            nationPiety[n] = random.nextFloat();
        }

        // The holds: every seat city and castle; a nation whose seat has neither keeps one at its seat all the same.
        List<Person.Hold> holds = new ArrayList<>();
        Set<Integer> seatProvinces = new HashSet<>();
        for (Seat s : nations) {
            seatProvinces.add(s.province());
        }
        Set<Integer> held = new HashSet<>();
        for (SeatCities.City c : cities) {
            Person.Kind kind = seatProvinces.contains(c.province()) ? Person.Kind.CAPITAL
                    : c.tier() == TitleMap.Tier.COUNTY ? Person.Kind.TOWN : Person.Kind.CITY;
            if (held.add(c.province())) {
                holds.add(new Person.Hold(kind, c.province(), c.x(), c.z()));
            }
        }
        for (Countryside.Settlement s : settlements) {
            if (s.kind() == Countryside.Kind.CASTLE && held.add(s.province())) {
                holds.add(new Person.Hold(seatProvinces.contains(s.province()) ? Person.Kind.CAPITAL : Person.Kind.CASTLE,
                        s.province(), s.x(), s.z()));
            }
        }
        for (Seat s : nations) {
            if (held.add(s.province())) {
                holds.add(new Person.Hold(Person.Kind.CAPITAL, s.province(), s.x(), s.z()));
            }
        }
        holds.sort(Comparator.comparingInt(Person.Hold::province));

        // The holders.
        int[] holdOf = new int[holder.length];
        java.util.Arrays.fill(holdOf, -1);
        for (int i = 0; i < holds.size(); i++) {
            holdOf[holds.get(i).province()] = i;
        }
        int[] peak = {0};
        List<Person> people = new ArrayList<>();
        for (Person.Hold hold : holds) {
            int p = hold.province();
            int nation = p < holder.length ? holder[p] : -1;
            int empire = empireOf(t, p);
            int culture = nation >= 0 ? nationCulture[nation] : empire;
            int faith = faithOf(random, nation >= 0 ? nationFaith[nation] : empireFaith[empire],
                    nation >= 0 ? nationPiety[nation] : 0.5f, empire, faiths, cultHome);
            people.add(person(random, cultures.get(culture), culture, faith, hold, nation, peak));
        }
        // Lieges: the next higher seat above them in the same nation, else the ruler.
        int[] ruler = new int[nations.size()];
        java.util.Arrays.fill(ruler, -1);
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.nation() >= 0 && p.hold().kind() == Person.Kind.CAPITAL && nations.get(p.nation()).province() == p.hold().province()) {
                ruler[p.nation()] = i;
            }
        }
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.nation() < 0 || ruler[p.nation()] == i) {
                continue;
            }
            int liege = ruler[p.nation()];
            int province = p.hold().province();
            int c = t.county()[province];
            int d = t.duchy()[c];
            int k = t.kingdom()[d];
            int e = t.empire()[k];
            for (int seat : new int[]{t.duchySeat()[d], t.kingdomSeat()[k], t.empireSeat()[e]}) {
                int above = holdOf[seat];
                if (seat != province && above >= 0 && people.get(above).nation() == p.nation() && rank(t, seat) > rank(t, province)) {
                    liege = above;
                    break;
                }
            }
            people.set(i, withLiege(p, liege));
        }

        return new Realm(cultures, faiths, people, bonds(random, people), nationCulture, nationFaith, nationPiety);
    }

    /** A holder: name of their culture, skills from their schooling and traits, prowess now and then superhuman. */
    private static Person person(Random random, Culture culture, int cultureId, int faith, Person.Hold hold, int nation, int[] peak) {
        boolean female = random.nextDouble() < 0.25;
        int age = 18 + random.nextInt(53);
        List<Personality> traits = new ArrayList<>();
        List<Integer> pairs = new ArrayList<>();
        for (int i = 0; i < Personality.values().length / 2; i++) {
            pairs.add(i);
        }
        java.util.Collections.shuffle(pairs, random);
        for (int i = 0; i < TRAITS; i++) {
            traits.add(Personality.values()[pairs.get(i) * 2 + random.nextInt(2)]);
        }
        int[] stats = new int[Stat.values().length];
        Stat focus = Stat.values()[random.nextInt(Stat.values().length - 1)]; // what they were schooled in (not prowess)
        for (Stat s : Stat.values()) {
            double base = 6 + random.nextGaussian() * 3 + (s == focus ? 3 + random.nextInt(4) : 0)
                    + (hold.kind() == Person.Kind.CAPITAL ? 1 : 0);
            for (Personality t : traits) {
                base += t.modifies(s);
            }
            stats[s.ordinal()] = Math.max(0, Math.min(20, (int) Math.round(base)));
        }
        if (random.nextDouble() < SUPERHUMAN) {
            int prowess = Stat.SUPERHUMAN;
            while (prowess < Stat.PROWESS.max() && random.nextDouble() < SUPERHUMAN_STEP) {
                prowess++;
            }
            if (prowess == Stat.PROWESS.max() && peak[0]++ >= Stat.PEAK_HOLDERS) {
                prowess--; // no more than three at the peak
            }
            stats[Stat.PROWESS.ordinal()] = prowess;
        }
        return new Person(culture.givenName(female, random), culture.familyName(random), female, age, cultureId, faith, stats,
                List.copyOf(traits), hold, -1, nation);
    }

    /** A character's faith: their nation's (or lands') mostly; otherwise a cult (one from their lands, if any) or another faith. */
    private static int faithOf(Random random, int home, float piety, int empire, List<Faith> faiths, int[] cultHome) {
        if (random.nextDouble() >= STRAY - STRAY_DEVOUT * piety) {
            return home;
        }
        int broad = faiths.size() - CULTS;
        if (random.nextBoolean()) {
            for (int i = 0; i < CULTS; i++) {
                if (cultHome[i] == empire) {
                    return broad + i;
                }
            }
            return broad + random.nextInt(CULTS);
        }
        return broad <= 1 ? home : (home + 1 + random.nextInt(broad - 1)) % broad;
    }

    /** Friends and rivals among each holder's nearest neighbours and fellow vassals; at most two of each. */
    private static List<Realm.Bond> bonds(Random random, List<Person> people) {
        List<Realm.Bond> bonds = new ArrayList<>();
        int[] friends = new int[people.size()];
        int[] rivals = new int[people.size()];
        for (int a = 0; a < people.size(); a++) {
            Person pa = people.get(a);
            List<Integer> near = new ArrayList<>();
            for (int b = 0; b < people.size(); b++) {
                if (b != a && (people.get(b).liege() == pa.liege() && pa.liege() >= 0)) {
                    near.add(b);
                }
            }
            List<Integer> byDistance = new ArrayList<>();
            for (int b = 0; b < people.size(); b++) {
                if (b != a) {
                    byDistance.add(b);
                }
            }
            byDistance.sort(Comparator.comparingDouble(b -> Math.hypot(people.get(b).hold().x() - pa.hold().x(),
                    people.get(b).hold().z() - pa.hold().z())));
            for (int i = 0; i < Math.min(NEIGHBOURS, byDistance.size()); i++) {
                if (!near.contains(byDistance.get(i))) {
                    near.add(byDistance.get(i));
                }
            }
            for (int b : near) {
                if (bonded(bonds, a, b)) {
                    continue; // each pair once
                }
                int agree = agreement(pa, people.get(b));
                double roll = random.nextDouble();
                if (roll < FRIEND + 0.04 * agree && friends[a] < MAX_BONDS && friends[b] < MAX_BONDS) {
                    bonds.add(new Realm.Bond(a, b, Realm.Bond.Kind.FRIEND));
                    friends[a]++;
                    friends[b]++;
                } else if (roll > 1 - (RIVAL - 0.04 * agree) && rivals[a] < MAX_BONDS && rivals[b] < MAX_BONDS) {
                    bonds.add(new Realm.Bond(a, b, Realm.Bond.Kind.RIVAL));
                    rivals[a]++;
                    rivals[b]++;
                }
            }
        }
        return bonds;
    }

    private static boolean bonded(List<Realm.Bond> bonds, int a, int b) {
        for (Realm.Bond bond : bonds) {
            if (bond.joins(a, b)) {
                return true;
            }
        }
        return false;
    }

    /** Traits two characters share, less those they hold opposed. */
    static int agreement(Person a, Person b) {
        int agree = 0;
        for (Personality t : a.traits()) {
            agree += b.has(t) ? 1 : b.has(t.opposite()) ? -1 : 0;
        }
        return agree;
    }

    private static Person withLiege(Person p, int liege) {
        return new Person(p.given(), p.family(), p.female(), p.age(), p.culture(), p.faith(), p.stats(), p.traits(), p.hold(), liege,
                p.nation());
    }

    /** A province's highest seat as a number: -1 none, 0 county ... 3 empire. */
    private static int rank(TitleMap t, int province) {
        TitleMap.Tier tier = t.seatOf(province);
        return tier == null ? -1 : tier.ordinal();
    }

    private static int empireOf(TitleMap t, int province) {
        return t.empire()[t.kingdom()[t.duchy()[t.county()[province]]]];
    }

    private static int[] seatAt(TitleMap t, List<ProvinceMap.Centre> centres, int province) {
        ProvinceMap.Centre c = centres.get(province);
        return new int[]{c.x(), c.z()};
    }

    /** {@code n} empires spread far apart: the first at random, each next the furthest from those chosen. */
    private static List<Integer> spreadOut(TitleMap t, List<ProvinceMap.Centre> centres, int n, Random random) {
        List<Integer> chosen = new ArrayList<>();
        if (t.empires() == 0) {
            return chosen;
        }
        chosen.add(random.nextInt(t.empires()));
        while (chosen.size() < n) {
            int best = -1;
            double bestDistance = -1;
            for (int e = 0; e < t.empires(); e++) {
                if (chosen.contains(e)) {
                    continue;
                }
                int[] at = seatAt(t, centres, t.empireSeat()[e]);
                double nearest = Double.MAX_VALUE;
                for (int c : chosen) {
                    int[] o = seatAt(t, centres, t.empireSeat()[c]);
                    nearest = Math.min(nearest, Math.hypot(o[0] - at[0], o[1] - at[1]));
                }
                if (nearest > bestDistance) {
                    bestDistance = nearest;
                    best = e;
                }
            }
            if (best < 0) {
                break;
            }
            chosen.add(best);
        }
        return chosen;
    }
}
