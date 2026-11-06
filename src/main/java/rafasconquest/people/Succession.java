package rafasconquest.people;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Who has grown old, died and inherited since the world map was drawn. Kept with the politics and written over the
 * realm's people as drawn (on the server when the map loads, on each client when the politics come), so a character
 * keeps their index for good: the dead stay in the list as {@link Person.Kind#DEAD}.
 * <p>
 * Everyone grows a year older every {@link #YEAR_DAYS} days, and may die, the old likelier. When a title holder dies the
 * first in their line ({@link Realm#line}) takes their hold, with its titles, fief, liege and vassals, and the rest of
 * the line follows them (a child's brothers and sisters become theirs); with no one in line a distant cousin of the
 * family comes to hold it.
 */
public final class Succession {
    public static final int YEAR_DAYS = 24;
    /** The chance a year of dying under {@link #OLD_FROM}; from then, this much more, rising so much each year. */
    static final double YOUNG = 0.004;
    static final int OLD_FROM = 50;
    static final double OLD = 0.01;
    static final double OLD_RISE = 1.12;

    /** Each people list as it was drawn, before anything here was written over it. */
    private static final Map<List<Person>, List<Person>> DRAWN = new IdentityHashMap<>();

    private int years;
    /** Characters changed since the drawing, by index (past the drawn ones, new), with their ages less the years since. */
    private final Map<Integer, Person> changed = new TreeMap<>();
    /** Each death not yet seen to by the players' side: {the dead, their heir or -1}. */
    private final List<int[]> deaths = new ArrayList<>();

    public int years() {
        return years;
    }

    /** The chance a year that someone of this age dies. */
    static double yearly(int age) {
        return age < OLD_FROM ? YOUNG : YOUNG + OLD * Math.pow(OLD_RISE, age - OLD_FROM);
    }

    /** Writes it over a realm's people as they were drawn. */
    public void apply(Realm realm) {
        List<Person> people = realm.people();
        List<Person> drawn = drawn(people);
        people.clear();
        for (int i = 0; i < drawn.size() || changed.containsKey(i); i++) {
            people.add(aged(i < drawn.size() ? changed.getOrDefault(i, drawn.get(i)) : changed.get(i), years));
        }
    }

    /** The people as drawn, kept the first time a list is seen (before anything here changes it). */
    private static List<Person> drawn(List<Person> people) {
        synchronized (DRAWN) {
            if (DRAWN.size() > 8 && !DRAWN.containsKey(people)) {
                DRAWN.clear(); // old worlds' maps, long gone
            }
            return DRAWN.computeIfAbsent(people, List::copyOf);
        }
    }

    /**
     * A new day: at each year's turn everyone grows a year older; then who dies of age or sickness (not those whose hold
     * a player has taken, nor the dead): their indexes, for {@link #died}.
     */
    public List<Integer> day(long day, Realm realm, Random random, java.util.Set<Integer> playerHolds) {
        drawn(realm.people());
        if (day > 0 && day % YEAR_DAYS == 0) {
            years++;
            apply(realm);
        }
        List<Integer> dying = new ArrayList<>();
        List<Person> people = realm.people();
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.hold().kind() != Person.Kind.DEAD && !playerHolds.contains(p.hold().province())
                    && random.nextDouble() < yearly(p.age()) / YEAR_DAYS) {
                dying.add(i);
            }
        }
        return dying;
    }

    /** Someone dies; if they held a title, the first in their line inherits. Returns the heir, or -1 if none was needed. */
    public int died(Realm realm, int d, Random random) {
        List<Person> people = realm.people();
        drawn(people);
        Person dead = people.get(d);
        Person.Kind kind = dead.hold().kind();
        if (kind == Person.Kind.DEAD) {
            return -1;
        }
        List<Integer> line = realm.line(d);
        List<Integer> vassals = realm.vassals(d);
        set(people, d, with(dead, new Person.Hold(Person.Kind.DEAD, dead.hold().province(), dead.hold().x(), dead.hold().z()), dead.liege(), -1));
        if (!dead.noble() || kind.heir()) {
            deaths.add(new int[]{d, -1});
            return -1;
        }
        int h;
        if (line.isEmpty()) {
            Culture culture = realm.cultures().get(dead.culture());
            Person c = RealmPlanner.person(random, culture, dead.culture(), dead.faith(), dead.race(), dead.hold(), dead.nation(), false,
                    Stat.TITLE_LIMITS.clone());
            h = people.size();
            set(people, h, new Person(c.given(), dead.family(), c.female(), 20 + random.nextInt(30), c.culture(), c.faith(), c.race(),
                    c.stats(), c.traits(), dead.hold(), dead.liege(), dead.nation(), false));
        } else {
            h = line.get(0);
            Person heir = people.get(h);
            set(people, h, with(heir, dead.hold(), dead.liege(), dead.nation()));
            for (int r : line) {
                if (r == h) {
                    continue;
                }
                Person q = people.get(r);
                // A child's brothers and sisters are the heir's too; the dead's own brothers and sisters stay theirs.
                Person.Kind k = heir.hold().kind() == Person.Kind.CHILD && q.hold().kind() == Person.Kind.CHILD ? Person.Kind.SIBLING : q.hold().kind();
                set(people, r, with(q, new Person.Hold(k, q.hold().province(), q.hold().x(), q.hold().z()), h, q.nation()));
            }
        }
        for (int v : vassals) {
            if (v != h) {
                Person q = people.get(v);
                set(people, v, with(q, q.hold(), h, q.nation()));
            }
        }
        deaths.add(new int[]{d, h});
        return h;
    }

    /** The deaths since last asked, each {the dead, their heir or -1}, for the players' side to see to. */
    public List<int[]> drainDeaths() {
        List<int[]> d = new ArrayList<>(deaths);
        deaths.clear();
        return d;
    }

    private void set(List<Person> people, int i, Person p) {
        if (i == people.size()) {
            people.add(p);
        } else {
            people.set(i, p);
        }
        changed.put(i, aged(p, -years));
    }

    private static Person aged(Person p, int by) {
        return by == 0 ? p : new Person(p.given(), p.family(), p.female(), p.age() + by, p.culture(), p.faith(), p.race(), p.stats(), p.traits(),
                p.hold(), p.liege(), p.nation(), p.away());
    }

    private static Person with(Person p, Person.Hold hold, int liege, int nation) {
        return new Person(p.given(), p.family(), p.female(), p.age(), p.culture(), p.faith(), p.race(), p.stats(), p.traits(), hold, liege,
                nation, p.away());
    }

    public byte[] encode() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeShort(years);
            out.writeShort(changed.size());
            for (Map.Entry<Integer, Person> e : changed.entrySet()) {
                out.writeShort(e.getKey());
                out.writeShort(e.getValue().age()); // may be below 0 for one born after the drawing
                Realm.writePerson(out, aged(e.getValue(), -e.getValue().age()));
            }
            out.writeShort(deaths.size());
            for (int[] d : deaths) {
                out.writeShort(d[0]);
                out.writeShort(d[1]);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return bytes.toByteArray();
    }

    public static Succession decode(byte[] data) {
        Succession s = new Succession();
        if (data == null || data.length == 0) {
            return s;
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(data))) {
            s.years = in.readShort();
            int n = in.readShort();
            for (int i = 0; i < n; i++) {
                int index = in.readShort();
                int age = in.readShort();
                s.changed.put(index, aged(Realm.readPerson(in), age));
            }
            int d = in.readShort();
            for (int i = 0; i < d; i++) {
                s.deaths.add(new int[]{in.readShort(), in.readShort()});
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return s;
    }
}
