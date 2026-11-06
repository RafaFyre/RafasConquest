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

    // ---- conquest ----

    /**
     * A hold taken: its province and where it stands, who took it (the conquering ruler), whether it is a greater hold
     * than theirs (a bigger city), whether they rank above its holder (by fief), the kind of hold it is for a holder who
     * isn't a ruler, the kind of hold the conqueror's own becomes if they leave it, who holds each province now, and whether
     * its family is gone (its holder fell defending it).
     */
    public record Taking(int hold, int x, int z, int conqueror, boolean greater, boolean higher, Person.Kind holdKind,
                         Person.Kind oldHoldKind, int[] owners, boolean gone) {
    }

    /**
     * A hold changes hands in war, and its family's fate follows from who took it ({@code t.conqueror}, of a culture that
     * puts down roots or not) and from whether its family yielded (its holder lives) or is gone (no one holds it):
     * <ul>
     *   <li>a greater hold, taken by one of no higher rank: one not putting down roots moves in and puts the family out;
     *       one who does keeps the family there, married into their own house and sworn to them, or, the family gone,
     *       gives it to whoever has a hook on them (spending it), else their most loyal vassal;</li>
     *   <li>a greater hold, taken by one of higher rank: the family married into theirs and kept as the city's lord
     *       under them, they move in; the family gone, they move in all the same;</li>
     *   <li>a lesser or equal hold: the family yields and swears to them as vassals, or, gone, it is given as above.</li>
     * </ul>
     * Returns what happened, for the news, or null if nothing changed.
     */
    public String conquer(Realm realm, Taking t, Random random) {
        List<Person> people = realm.people();
        drawn(people);
        int c = realm.holderOf(t.hold());
        if (c == t.conqueror() || t.conqueror() < 0) {
            return null;
        }
        Person r = people.get(t.conqueror());
        boolean gone = t.gone() || c < 0;
        boolean rooted = realm.cultures().get(r.culture()).rooted();
        String hold = realm.holdName(t.hold()).isEmpty() ? "the hold" : realm.holdName(t.hold());
        if (t.greater() && !t.higher() && !rooted || t.greater() && t.higher() && gone) {
            if (c >= 0) {
                exile(realm, c, t);
            }
            moveIn(realm, t.conqueror(), t, r.hold().kind());
            return r.name() + " moved into " + hold + (gone ? "." : ", putting out the family that held it.");
        }
        if (t.greater() && t.higher()) {
            marryIn(realm, c, t.conqueror(), random);
            Person cp = people.get(c);
            set(people, c, with(cp, new Person.Hold(Person.Kind.LORD, t.hold(), t.x(), t.z()), t.conqueror(), r.nation()));
            follow(realm, c, t, r.nation());
            moveIn(realm, t.conqueror(), t, r.hold().kind());
            return r.name() + " moved into " + hold + "; its family, married into theirs, keep it as its lords.";
        }
        if (gone) {
            int a = appointee(realm, t.conqueror());
            if (a < 0) {
                return null;
            }
            if (c >= 0) {
                exile(realm, c, t); // what is left of the family goes
            }
            boolean hooked = spend(a, t.conqueror());
            moveIn(realm, a, t, t.holdKind());
            set(people, a, with(people.get(a), people.get(a).hold(), t.conqueror(), r.nation()));
            return r.name() + " gave " + hold + " to " + people.get(a).name() + (hooked ? ", who had a hook on them." : ", their most loyal vassal.");
        }
        if (t.greater()) {
            marryIn(realm, c, t.conqueror(), random);
        }
        Person cp = people.get(c);
        set(people, c, with(cp, new Person.Hold(t.holdKind(), t.hold(), t.x(), t.z()), t.conqueror(), r.nation()));
        follow(realm, c, t, r.nation());
        return people.get(c).name() + " of " + hold + (t.greater() ? " married into the house of " + r.family() + " and swore to " : " swore to ")
                + r.name() + ".";
    }

    /** Who a conqueror gives a hold to: one with a hook on them (a secret before a favour), else their most loyal vassal. */
    int appointee(Realm realm, int conqueror) {
        List<Person> people = realm.people();
        int best = -1;
        for (Hook h : hooks(people)) {
            if (h.target() == conqueror && people.get(h.holder()).age() >= Marriages.ADULT
                    && (best < 0 || h.kind().ordinal() > hook(people, best, conqueror).kind().ordinal())) {
                best = h.holder();
            }
        }
        if (best >= 0) {
            return best;
        }
        int loyalest = Integer.MIN_VALUE;
        for (int v : realm.vassals(conqueror)) {
            int o = realm.opinion(v, conqueror);
            if (o > loyalest) {
                loyalest = o;
                best = v;
            }
        }
        return best;
    }

    /**
     * Someone moves to a hold, with their household; the hold they leave passes to the first in their line (as a
     * {@code t.oldHoldKind()} if they were a ruler, else as the kind it was), or stands empty if they have none.
     */
    private void moveIn(Realm realm, int a, Taking t, Person.Kind kind) {
        List<Person> people = realm.people();
        Person p = people.get(a);
        Person.Hold old = p.hold();
        boolean held = Marriages.holds(p);
        List<Integer> line = realm.line(a);
        for (int i = 0; i < people.size(); i++) {
            Person q = people.get(i);
            if (q.liege() == a && q.hold().kind().household() && (!held || line.isEmpty() || i != line.get(0))) {
                set(people, i, with(q, new Person.Hold(q.hold().kind(), t.hold(), t.x(), t.z()), a, q.nation()));
            }
        }
        set(people, a, with(p, new Person.Hold(kind, t.hold(), t.x(), t.z()), p.liege(), p.nation()));
        if (held && !line.isEmpty()) {
            int h = line.get(0);
            Person.Kind left = old.kind() == Person.Kind.CAPITAL ? t.oldHoldKind() : old.kind();
            set(people, h, with(people.get(h), new Person.Hold(left, old.province(), old.x(), old.z()), a, p.nation()));
        }
    }

    /** A family put out of its hold: its holder lives on there, holding nothing; their vassals go to the conqueror. */
    private void exile(Realm realm, int c, Taking t) {
        List<Person> people = realm.people();
        List<Integer> vassals = realm.vassals(c);
        Person p = people.get(c);
        set(people, c, with(p, new Person.Hold(Person.Kind.EXILE, p.hold().province(), p.hold().x(), p.hold().z()), -1, p.nation()));
        for (int v : vassals) {
            Person q = people.get(v);
            if (q.hold().province() < t.owners().length && t.owners()[q.hold().province()] == people.get(t.conqueror()).nation()) {
                set(people, v, with(q, q.hold(), t.conqueror(), people.get(t.conqueror()).nation()));
            }
        }
    }

    /**
     * Those who answer to {@code c} come with them to their new nation, their household and the vassals whose land it now
     * holds; the vassals whose land it doesn't go to their own nation's ruler.
     */
    private void follow(Realm realm, int c, Taking t, int nation) {
        List<Person> people = realm.people();
        for (int i = 0; i < people.size(); i++) {
            Person q = people.get(i);
            if (q.liege() != c || q.hold().kind() == Person.Kind.DEAD) {
                continue;
            }
            boolean ours = q.hold().kind().household() || q.hold().province() < t.owners().length && t.owners()[q.hold().province()] == nation;
            set(people, i, ours ? with(q, q.hold(), c, nation) : with(q, q.hold(), q.nation() >= 0 ? realm.ruler(q.nation()) : -1, q.nation()));
        }
    }

    /**
     * The conquered family marries into the conqueror's house: its holder weds an unmarried grown member of it (if both
     * are free) and the family, holder and line, takes the conqueror's name.
     */
    private void marryIn(Realm realm, int c, int conqueror, Random random) {
        List<Person> people = realm.people();
        Person p = people.get(c);
        if (Marriages.free(people, c)) {
            for (int m : realm.line(conqueror)) {
                if (Marriages.free(people, m) && people.get(m).female() != p.female()) {
                    weddings.add(Marriages.marry(people, c, m, (i, q) -> set(people, i, q)));
                    break;
                }
            }
        }
        String house = people.get(conqueror).family();
        List<Integer> family = new ArrayList<>(realm.line(c));
        family.add(c);
        for (int i : family) {
            Person q = people.get(i);
            set(people, i, new Person(q.given(), house, q.female(), q.age(), q.culture(), q.faith(), q.race(), q.stats(), q.traits(), q.hold(),
                    q.liege(), q.nation(), q.away(), q.spouse()));
        }
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
