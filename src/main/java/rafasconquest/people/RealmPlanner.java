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
 *   <li><b>Cultures</b>: one for each kind of land ({@link Homeland}, a vanilla villager type). A nation's is the
 *       culture of the land its seat stands in, and every one of its characters has it; a hold no nation keeps has its
 *       land's culture.</li>
 *   <li><b>Demographics</b>: a nation's people, counted from its cities, castles and countryside; their races follow
 *       the lands they live in, with a few wanderers from elsewhere, and their cultures lean further towards the
 *       nation's own, some nations having made more of their people their own than others.</li>
 *   <li><b>Faiths</b>: 3 to 5 broad faiths, each spread over the lands nearest where it began, and 4 cults, each from
 *       a home in one empire's lands. A nation keeps its seat's faith, devoutly or not ({@code piety}); its characters
 *       mostly share it, the more devout the nation the more so, the rest keeping a cult or another faith.</li>
 *   <li><b>Holders</b>: one for each capital, city, town and castle, with skills, traits, an age and a liege: the holder
 *       of the next higher seat above them in the same nation (a county's town under its duchy's city, and so on up),
 *       else the nation's ruler. Holds no nation keeps answer to no one. A holder is mostly of the race of the land
 *       their hold stands in, otherwise of one of their nation's peoples.</li>
 *   <li><b>Bonds</b>: friends and rivals among neighbours and those serving the same liege, likelier where their
 *       personalities agree (friends) or clash (rivals).</li>
 * </ul>
 */
public final class RealmPlanner {
    /** A nation: its rank (a title tier's ordinal), its seat province, where its seat stands, and whether it is a sword sanctum. */
    public record Seat(int tier, int province, int x, int z, boolean sanctum) {
    }

    static final int CULTS = 4;
    /** Each character: their own faith (not the nation's) this often at piety 0, and this much less at piety 1. */
    static final double STRAY = 0.3;
    static final double STRAY_DEVOUT = 0.25;
    /** A character is superhuman in prowess this often; each step further up, this likely to go on. */
    static final double SUPERHUMAN = 0.05;
    static final double SUPERHUMAN_STEP = 0.85;
    /** A sanctum's Sword God is away from it this often. */
    static final double SANCTUM_AWAY = 0.3;
    static final int TRAITS = 3;
    /** Bonds are looked for among this many nearest holders and those serving the same liege. */
    private static final int NEIGHBOURS = 6;
    private static final double FRIEND = 0.12;
    private static final double RIVAL = 0.1;
    private static final int MAX_BONDS = 2;
    private static final long SALT = 0x5045_4F50_4C45_5321L;
    private static final long PEOPLE_SALT = 0x4445_4D4F_4752_4150L;
    /** People living in each province's farms and fields, whatever stands there. */
    static final int RURAL = 150;
    /** People in each district of a city, each hamlet (alone or in a village), each castle and each watchtower. */
    static final int PER_DISTRICT = 400;
    static final int PER_HAMLET = 25;
    static final int PER_CASTLE = 120;
    static final int PER_WATCHTOWER = 10;
    /** The share of a nation's people come from lands it doesn't hold: at least this, and up to this much more. */
    static final double WANDERERS = 0.03;
    static final double WANDERERS_MORE = 0.09;
    /** How much of its people a nation has made of its own culture: at least this, and up to this much more. */
    static final double ASSIMILATED = 0.35;
    static final double ASSIMILATED_MORE = 0.5;
    /** A holder is of the race of the land their hold stands in this often; else of any of their nation's peoples. */
    static final double NATIVE_HOLDER = 0.75;

    private RealmPlanner() {
    }

    public static Realm plan(long seed, TitleMap t, List<ProvinceMap.Centre> centres, List<SeatCities.City> cities,
                             List<Countryside.Settlement> settlements, List<Seat> nations, int[] holder) {
        int[] plains = new int[centres.size()];
        java.util.Arrays.fill(plains, Homeland.PLAINS.ordinal());
        return plan(seed, t, centres, cities, settlements, nations, holder, plains, true);
    }

    /**
     * The same, with each province's kind of land ({@link Homeland} ordinal), and with the martial titles or (where the
     * world has none) with no one past ordinary prowess.
     */
    public static Realm plan(long seed, TitleMap t, List<ProvinceMap.Centre> centres, List<SeatCities.City> cities,
                             List<Countryside.Settlement> settlements, List<Seat> nations, int[] holder, int[] homeland,
                             boolean martialTitles) {
        Random random = new Random(seed ^ SALT);
        int empires = t.empires();

        // Cultures: one for each kind of land.
        List<Culture> cultures = new ArrayList<>();
        for (Homeland h : Homeland.values()) {
            cultures.add(Culture.make(h, random));
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

        // Nations: the culture of the land their seat stands in, the faith of its empire's lands, and a piety of their own.
        int[] nationCulture = new int[nations.size()];
        int[] nationFaith = new int[nations.size()];
        float[] nationPiety = new float[nations.size()];
        for (int n = 0; n < nations.size(); n++) {
            int e = empireOf(t, nations.get(n).province());
            nationCulture[n] = homeland[nations.get(n).province()];
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
        // How many of each martial title are taken (Sword Gods, Kings, Saints); the sanctums' Sword Gods first.
        int[] titles = martialTitles ? new int[]{(int) nations.stream().filter(Seat::sanctum).count(), 0, 0}
                : Stat.TITLE_LIMITS.clone(); // no martial titles: every one already full, so no one rises past 20
        // Their peoples first, so each holder can be of one of them.
        List<Demographics> demographics = demographics(seed, cities, settlements, nations.size(), holder, homeland, nationCulture);
        Random races = new Random(seed ^ PEOPLE_SALT ^ 0x52414345L);
        List<Person> people = new ArrayList<>();
        for (Person.Hold hold : holds) {
            int p = hold.province();
            int nation = p < holder.length ? holder[p] : -1;
            int empire = empireOf(t, p);
            int culture = nation >= 0 ? nationCulture[nation] : homeland[p];
            int faith = faithOf(random, nation >= 0 ? nationFaith[nation] : empireFaith[empire],
                    nation >= 0 ? nationPiety[nation] : 0.5f, empire, faiths, cultHome);
            boolean sanctum = nation >= 0 && nations.get(nation).sanctum() && nations.get(nation).province() == p;
            int race = raceOf(races, homeland[p], nation >= 0 ? demographics.get(nation) : null);
            people.add(person(random, cultures.get(culture), culture, faith, race, hold, nation, sanctum, titles));
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

        return new Realm(cultures, faiths, people, bonds(random, people), nationCulture, nationFaith, nationPiety,
                demographics);
    }

    /**
     * A holder's race: mostly that of the land their hold stands in; otherwise one of their nation's peoples, as many of
     * each as it has (the land's again, where they hold none).
     */
    static int raceOf(Random random, int land, Demographics people) {
        if (people == null || random.nextDouble() < NATIVE_HOLDER) {
            return land;
        }
        double roll = random.nextDouble();
        for (int h = 0; h < people.races().length; h++) {
            roll -= people.races()[h];
            if (roll < 0) {
                return h;
            }
        }
        return land;
    }

    /**
     * Each nation's people: counted from the cities, castles and countryside of the provinces it holds; of the race of
     * the land each lives in, but for a few wanderers from other lands; and of the nation's culture as far as it has
     * made them its own, else of their race's.
     */
    static List<Demographics> demographics(long seed, List<SeatCities.City> cities, List<Countryside.Settlement> settlements,
                                           int nationCount, int[] holder, int[] homeland, int[] nationCulture) {
        Random random = new Random(seed ^ PEOPLE_SALT);
        int[] living = new int[holder.length];
        java.util.Arrays.fill(living, RURAL);
        for (SeatCities.City c : cities) {
            living[c.province()] += PER_DISTRICT * c.districts() * c.districts();
        }
        for (Countryside.Settlement s : settlements) {
            living[s.province()] += switch (s.kind()) {
                case HAMLET -> PER_HAMLET;
                case VILLAGE -> PER_HAMLET * s.hamlets();
                case CASTLE -> PER_CASTLE;
                case WATCHTOWER -> PER_WATCHTOWER;
                case RUIN -> 0;
            };
        }
        int kinds = Homeland.values().length;
        List<Demographics> all = new ArrayList<>(nationCount);
        for (int n = 0; n < nationCount; n++) {
            int population = 0;
            float[] land = new float[kinds];
            for (int p = 0; p < holder.length; p++) {
                if (holder[p] == n) {
                    population += living[p];
                    land[homeland[p]] += living[p];
                }
            }
            if (population == 0) {
                land[nationCulture[n]] = 1; // a nation holding no land of its own still has its seat's people
            }
            normalise(land);
            // A few wanderers, from other lands at random.
            float[] wanderers = new float[kinds];
            for (int h = 0; h < kinds; h++) {
                wanderers[h] = random.nextFloat();
            }
            normalise(wanderers);
            float wandering = (float) (WANDERERS + WANDERERS_MORE * random.nextDouble());
            float[] races = new float[kinds];
            for (int h = 0; h < kinds; h++) {
                races[h] = land[h] * (1 - wandering) + wanderers[h] * wandering;
            }
            float assimilated = (float) (ASSIMILATED + ASSIMILATED_MORE * random.nextDouble());
            float[] cultures = new float[kinds];
            for (int h = 0; h < kinds; h++) {
                cultures[h] = races[h] * (1 - assimilated) + (h == nationCulture[n] ? assimilated : 0);
            }
            all.add(new Demographics(population, cultures, races));
        }
        return all;
    }

    private static void normalise(float[] shares) {
        float total = 0;
        for (float s : shares) {
            total += s;
        }
        for (int i = 0; i < shares.length; i++) {
            shares[i] = total > 0 ? shares[i] / total : 1f / shares.length;
        }
    }

    /** A holder: name of their culture, skills from their schooling and traits, prowess now and then superhuman. */
    private static Person person(Random random, Culture culture, int cultureId, int faith, int race, Person.Hold hold, int nation,
                                 boolean sanctum, int[] titles) {
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
        if (!sanctum && random.nextDouble() < SUPERHUMAN) {
            int prowess = Stat.SUPERHUMAN;
            while (prowess < Stat.PROWESS.max() && random.nextDouble() < SUPERHUMAN_STEP) {
                prowess++;
            }
            // Each martial title has only so many holders: where it is full, the top of the title below it.
            for (int i = 0; i < Stat.TITLE_FLOORS.length; i++) {
                if (prowess >= Stat.TITLE_FLOORS[i]) {
                    if (titles[i] < Stat.TITLE_LIMITS[i]) {
                        titles[i]++;
                        break;
                    }
                    prowess = Stat.TITLE_FLOORS[i] - 1;
                }
            }
            stats[Stat.PROWESS.ordinal()] = prowess;
        }
        boolean away = false;
        if (sanctum) {
            // A sanctum is held by a Sword God, now and then away from it.
            stats[Stat.PROWESS.ordinal()] = Stat.SWORD_GOD + random.nextInt(Stat.PROWESS.max() - Stat.SWORD_GOD + 1);
            away = random.nextDouble() < SANCTUM_AWAY;
        }
        return new Person(culture.givenName(female, random), culture.familyName(random), female, age, cultureId, faith, race, stats,
                List.copyOf(traits), hold, -1, nation, away);
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
        return new Person(p.given(), p.family(), p.female(), p.age(), p.culture(), p.faith(), p.race(), p.stats(), p.traits(), p.hold(), liege,
                p.nation(), p.away());
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
