package rafasconquest.people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Which sword sanctum each Sword King and Sword Saint trained at. On paper it is chance, but the nearer a sanctum, the
 * likelier: each draws a sanctum with a weight falling off with distance. A sanctum keeps 2 to 7 Sword Kings (any more
 * trained nowhere in particular); it takes every Sword Saint that draws it.
 */
public final class Organizations {
    static final int MIN_KINGS = 2;
    static final int MAX_KINGS = 7;
    /** At this distance (blocks) a sanctum is half as likely as one at the door. */
    static final double HALF_WEIGHT = 4000;

    /** A sanctum: its name, its nation, its Sword God (or -1), and where it stands. */
    public record Sanctum(String name, int nation, int head, int x, int z) {
    }

    private Organizations() {
    }

    public static List<Organization> plan(Random random, List<Person> people, List<Sanctum> sanctums) {
        if (sanctums.isEmpty()) {
            return List.of();
        }
        List<List<Integer>> kings = new ArrayList<>();
        List<List<Integer>> saints = new ArrayList<>();
        int[] room = new int[sanctums.size()];
        for (int s = 0; s < sanctums.size(); s++) {
            kings.add(new ArrayList<>());
            saints.add(new ArrayList<>());
            room[s] = MIN_KINGS + random.nextInt(MAX_KINGS - MIN_KINGS + 1);
        }
        List<Integer> heads = sanctums.stream().map(Sanctum::head).toList();
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            order.add(i);
        }
        Collections.shuffle(order, random);
        for (int i : order) {
            Person p = people.get(i);
            int prowess = p.stat(Stat.PROWESS);
            if (heads.contains(i) || prowess < Stat.SUPERHUMAN || prowess >= Stat.SWORD_GOD) {
                continue;
            }
            boolean king = prowess >= Stat.SWORD_KING;
            int s = draw(random, p, sanctums, king ? room : null);
            if (s < 0) {
                continue;
            }
            if (king) {
                kings.get(s).add(i);
                room[s]--;
            } else {
                saints.get(s).add(i);
            }
        }
        List<Organization> all = new ArrayList<>();
        for (int s = 0; s < sanctums.size(); s++) {
            Sanctum sanctum = sanctums.get(s);
            // The strongest first, as they stand.
            kings.get(s).sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            saints.get(s).sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            all.add(new Organization(sanctum.name(), sanctum.nation(), sanctum.head(), List.copyOf(kings.get(s)), List.copyOf(saints.get(s))));
        }
        return all;
    }

    /** Each sanctum is filled to 1 or 2 Sword Kings and 12 to 20 Sword Saints, within the world's limits. */
    static final int FILL_MIN_KINGS = 1;
    static final int FILL_MAX_KINGS = 2;
    static final int FILL_MIN_SAINTS = 12;
    static final int FILL_MAX_SAINTS = 20;

    /**
     * Fills each sanctum to its kings and saints: those trained there count, and the rest are new characters of its
     * nation, its retainers (holding nothing, living at the sanctum, sworn to its Sword God), so long as the world's
     * limits for each title ({@link Stat#TITLE_LIMITS}) are not passed. Returns the realm with them and its organizations.
     */
    public static Realm populate(Random random, Realm realm) {
        List<Person> people = new ArrayList<>(realm.people());
        int kingsNow = 0;
        int saintsNow = 0;
        for (Person p : people) {
            int prowess = p.stat(Stat.PROWESS);
            if (prowess >= Stat.SWORD_KING && prowess < Stat.SWORD_GOD) {
                kingsNow++;
            } else if (prowess >= Stat.SUPERHUMAN && prowess < Stat.SWORD_KING) {
                saintsNow++;
            }
        }
        List<Organization> filled = new ArrayList<>();
        for (Organization o : realm.organizations()) {
            List<Integer> kings = new ArrayList<>(o.kings());
            List<Integer> saints = new ArrayList<>(o.saints());
            Person head = o.head() >= 0 ? people.get(o.head()) : null;
            if (head != null) {
                int wantKings = FILL_MIN_KINGS + random.nextInt(FILL_MAX_KINGS - FILL_MIN_KINGS + 1);
                int wantSaints = FILL_MIN_SAINTS + random.nextInt(FILL_MAX_SAINTS - FILL_MIN_SAINTS + 1);
                while (kings.size() < wantKings && kingsNow < Stat.MAX_SWORD_KINGS) {
                    kings.add(people.size());
                    people.add(retainer(random, realm, o, head, Stat.SWORD_KING + random.nextInt(Stat.SWORD_GOD - Stat.SWORD_KING)));
                    kingsNow++;
                }
                while (saints.size() < wantSaints && saintsNow < Stat.MAX_SWORD_SAINTS) {
                    saints.add(people.size());
                    people.add(retainer(random, realm, o, head, Stat.SUPERHUMAN + random.nextInt(Stat.SWORD_KING - Stat.SUPERHUMAN)));
                    saintsNow++;
                }
            }
            kings.sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            saints.sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            filled.add(new Organization(o.name(), o.nation(), o.head(), List.copyOf(kings), List.copyOf(saints)));
        }
        return new Realm(realm.cultures(), realm.faiths(), List.copyOf(people), realm.bonds(), realm.nationCulture(), realm.nationFaith(),
                realm.nationPiety(), realm.demographics(), filled, realm.holds());
    }

    /** A new retainer of a sanctum: of its nation's culture and faith, sworn to its Sword God, living at its seat. */
    private static Person retainer(Random random, Realm realm, Organization o, Person head, int prowess) {
        int culture = o.nation() < realm.nationCulture().length ? realm.nationCulture()[o.nation()] : head.culture();
        int faith = o.nation() < realm.nationFaith().length ? realm.nationFaith()[o.nation()] : head.faith();
        Culture c = realm.cultures().get(culture);
        boolean female = random.nextDouble() < 0.25;
        int[] stats = new int[Stat.values().length];
        for (Stat s : Stat.values()) {
            stats[s.ordinal()] = Math.max(0, Math.min(20, (int) Math.round(6 + random.nextGaussian() * 3)));
        }
        stats[Stat.PROWESS.ordinal()] = prowess;
        Personality[] all = Personality.values();
        List<Personality> traits = List.of(all[random.nextInt(all.length / 2) * 2], all[(random.nextInt(all.length / 2) * 2 + 1) % all.length]);
        Person.Hold hold = new Person.Hold(Person.Kind.RETAINER, head.hold().province(), head.hold().x(), head.hold().z());
        return new Person(c.givenName(female, random), c.familyName(random), female, 18 + random.nextInt(43), culture, faith, head.race(),
                stats, traits.get(0) == traits.get(1) ? List.of(traits.get(0)) : traits, hold, o.head(), o.nation(), false);
    }

    /** A sanctum for someone, likelier the nearer; only those with room, if {@code room} is given; -1 if none has. */
    static int draw(Random random, Person p, List<Sanctum> sanctums, int[] room) {
        double[] weight = new double[sanctums.size()];
        double total = 0;
        for (int s = 0; s < sanctums.size(); s++) {
            if (room != null && room[s] <= 0) {
                continue;
            }
            Sanctum sanctum = sanctums.get(s);
            weight[s] = weight(Math.hypot(p.hold().x() - sanctum.x(), p.hold().z() - sanctum.z()));
            total += weight[s];
        }
        if (total <= 0) {
            return -1;
        }
        double roll = random.nextDouble() * total;
        for (int s = 0; s < weight.length; s++) {
            roll -= weight[s];
            if (weight[s] > 0 && roll < 0) {
                return s;
            }
        }
        for (int s = weight.length - 1; s >= 0; s--) {
            if (weight[s] > 0) {
                return s;
            }
        }
        return -1;
    }

    static double weight(double distance) {
        double d = distance / HALF_WEIGHT;
        return 1 / (1 + d * d);
    }
}
