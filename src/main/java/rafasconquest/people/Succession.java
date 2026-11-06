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
    /** The weddings not yet seen to by the politics. */
    private final List<Marriages.Wedding> weddings = new ArrayList<>();
    /** Who has a hook on whom. */
    private final List<Hook> hooks = new ArrayList<>();

    public int years() {
        return years;
    }

    // ---- hooks ----

    /** Intrigue from which one starts uncovering secrets, and the chance a year for each point past it. */
    static final int SCHEMER = 8;
    static final double UNCOVER_PER_POINT = 0.02;
    /** Secrets are uncovered on those living no further than this away (blocks), rivals likeliest. */
    static final double HOOK_REACH = 3000;

    /** The hooks held, living holder on living target. */
    public List<Hook> hooks(List<Person> people) {
        List<Hook> live = new ArrayList<>();
        for (Hook h : hooks) {
            if (alive(people, h.holder()) && alive(people, h.target())) {
                live.add(h);
            }
        }
        return live;
    }

    /** The hook {@code holder} has on {@code target}, or null. */
    public Hook hook(List<Person> people, int holder, int target) {
        for (Hook h : hooks(people)) {
            if (h.holder() == holder && h.target() == target) {
                return h;
            }
        }
        return null;
    }

    /** {@code holder} gains a hook on {@code target} (a secret outweighs a favour, so one replaces the other). */
    public void hook(int holder, int target, Hook.Kind kind) {
        if (holder == target || holder < 0 || target < 0) {
            return;
        }
        for (int i = 0; i < hooks.size(); i++) {
            Hook h = hooks.get(i);
            if (h.holder() == holder && h.target() == target) {
                if (kind.ordinal() > h.kind().ordinal()) {
                    hooks.set(i, new Hook(holder, target, kind));
                }
                return;
            }
        }
        hooks.add(new Hook(holder, target, kind));
    }

    /** Spends {@code holder}'s hook on {@code target}: true if they had one, now gone. */
    public boolean spend(int holder, int target) {
        return hooks.removeIf(h -> h.holder() == holder && h.target() == target);
    }

    /**
     * A year's schemes: each living grown character with an eye for it ({@link #SCHEMER} Intrigue or more) may uncover a
     * secret of someone near, a rival twice as likely as anyone else.
     */
    void uncover(Realm realm, Random random) {
        List<Person> people = realm.people();
        hooks.removeIf(h -> !alive(people, h.holder()) || !alive(people, h.target()));
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            int edge = p.stat(Stat.INTRIGUE) - SCHEMER;
            if (!alive(people, i) || p.age() < Marriages.ADULT || edge < 0 || random.nextDouble() >= (edge + 1) * UNCOVER_PER_POINT) {
                continue;
            }
            List<Integer> near = new ArrayList<>();
            for (int j = 0; j < people.size(); j++) {
                Person q = people.get(j);
                if (j != i && alive(people, j) && q.age() >= Marriages.ADULT
                        && Math.hypot(q.hold().x() - p.hold().x(), q.hold().z() - p.hold().z()) <= HOOK_REACH) {
                    near.add(j);
                    if (realm.bond(i, j) == Realm.Bond.Kind.RIVAL) {
                        near.add(j); // twice
                    }
                }
            }
            if (!near.isEmpty()) {
                hook(i, near.get(random.nextInt(near.size())), Hook.Kind.SECRET);
            }
        }
    }

    private static boolean alive(List<Person> people, int i) {
        return i >= 0 && i < people.size() && people.get(i).hold().kind() != Person.Kind.DEAD;
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
            List<Person> all = realm.people();
            weddings.addAll(Marriages.year(all, realm.cultures(), random, (i, p) -> set(all, i, p)));
            uncover(realm, random);
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

    /** The weddings since last asked: each {who moved, whom they married, the nation they came from, the nation they joined}. */
    public List<int[]> drainWeddings() {
        List<int[]> w = new ArrayList<>();
        for (Marriages.Wedding m : weddings) {
            w.add(new int[]{m.moved(), m.stayed(), m.fromNation(), m.toNation()});
        }
        weddings.clear();
        return w;
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
                p.hold(), p.liege(), p.nation(), p.away(), p.spouse());
    }

    private static Person with(Person p, Person.Hold hold, int liege, int nation) {
        return new Person(p.given(), p.family(), p.female(), p.age(), p.culture(), p.faith(), p.race(), p.stats(), p.traits(), hold, liege,
                nation, p.away(), p.spouse());
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
            out.writeShort(hooks.size());
            for (Hook h : hooks) {
                out.writeShort(h.holder());
                out.writeShort(h.target());
                out.writeByte(h.kind().ordinal());
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
            int hookCount = in.readShort();
            for (int i = 0; i < hookCount; i++) {
                s.hooks.add(new Hook(in.readShort(), in.readShort(), Hook.Kind.values()[in.readUnsignedByte()]));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return s;
    }
}
