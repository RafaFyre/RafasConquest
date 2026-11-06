package rafasconquest.people;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Who marries whom, and the children born to them. A holder marries the nearest unmarried grown heir of another house
 * (of the other sex), who leaves their own family's line to live with them at their hold; failing one, someone new comes
 * to marry them. An heir marries someone new, who comes to live with them. A holder and their spouse have children,
 * born into the holder's family and line, while the wife is young enough.
 */
final class Marriages {
    static final int ADULT = 18;
    /** Of holders old enough when the world is drawn, this many are married. */
    static final double WED_AT_START = 0.7;
    /** Each year, an unmarried grown holder or heir marries this often. */
    static final double WED_A_YEAR = 0.25;
    /** Each year, a holder and their wife have a child this often, until she is this old. */
    static final double CHILD_A_YEAR = 0.3;
    static final int FERTILE_TO = 45;
    /** A match is looked for no further than this from the holder (blocks). */
    static final double REACH = 4000;

    /** A wedding: the two married, the one who moved to the other's hold first, and the nations each came from. */
    record Wedding(int moved, int stayed, int fromNation, int toNation) {
    }

    /** Writes a character over the people (index past the end: a new one). */
    interface Setter {
        void set(int index, Person p);
    }

    private Marriages() {
    }

    static boolean holds(Person p) {
        return switch (p.hold().kind()) {
            case CAPITAL, CITY, TOWN, CASTLE -> true;
            default -> false;
        };
    }

    /** Whether someone is free to marry: alive, grown, a holder or heir, and not married now. */
    static boolean free(List<Person> people, int i) {
        Person p = people.get(i);
        return p.age() >= ADULT && (holds(p) || p.hold().kind().heir()) && !p.married(people);
    }

    /** Most holders old enough are married when the world is drawn. */
    static void atStart(List<Person> people, List<Culture> cultures, Random random, Setter set) {
        int drawn = people.size();
        for (int i = 0; i < drawn; i++) {
            if (holds(people.get(i)) && free(people, i) && people.get(i).age() >= 20 && random.nextDouble() < WED_AT_START) {
                wed(people, i, cultures, random, set);
            }
        }
    }

    /** A year's weddings and births. */
    static List<Wedding> year(List<Person> people, List<Culture> cultures, Random random, Setter set) {
        List<Wedding> weddings = new ArrayList<>();
        int before = people.size();
        for (int i = 0; i < before; i++) {
            if (free(people, i) && random.nextDouble() < WED_A_YEAR) {
                weddings.add(wed(people, i, cultures, random, set));
            }
        }
        for (int i = 0; i < before; i++) {
            Person h = people.get(i);
            if (!holds(h) || !h.married(people)) {
                continue;
            }
            Person wife = h.female() ? h : people.get(h.spouse());
            if (wife.age() <= FERTILE_TO && random.nextDouble() < CHILD_A_YEAR) {
                born(people, i, cultures, random, set);
            }
        }
        return weddings;
    }

    /** Someone marries: a holder, an heir of another house if one is near; anyone, someone new. */
    static Wedding wed(List<Person> people, int i, List<Culture> cultures, Random random, Setter set) {
        Person p = people.get(i);
        int match = -1;
        if (holds(p)) {
            double best = REACH;
            for (int j = 0; j < people.size(); j++) {
                Person q = people.get(j);
                double d = Math.hypot(q.hold().x() - p.hold().x(), q.hold().z() - p.hold().z());
                if (j != i && q.hold().kind().heir() && q.female() != p.female() && !q.family().equals(p.family()) && free(people, j) && d < best) {
                    best = d;
                    match = j;
                }
            }
        }
        int from;
        Person spouse;
        Person.Hold hold = new Person.Hold(Person.Kind.SPOUSE, p.hold().province(), p.hold().x(), p.hold().z());
        if (match >= 0) {
            Person q = people.get(match);
            from = q.nation();
            spouse = new Person(q.given(), q.family(), q.female(), q.age(), q.culture(), q.faith(), q.race(), q.stats(), q.traits(), hold, i,
                    p.nation(), false, i);
        } else {
            match = people.size();
            from = p.nation();
            Culture culture = cultures.get(p.culture());
            Person c = RealmPlanner.person(random, culture, p.culture(), p.faith(), p.race(), hold, p.nation(), false, Stat.TITLE_LIMITS.clone());
            boolean female = !p.female();
            spouse = new Person(culture.givenName(female, random), c.family(), female, Math.max(ADULT, p.age() - 8 + random.nextInt(17)), c.culture(),
                    c.faith(), c.race(), c.stats(), c.traits(), hold, i, p.nation(), false, i);
        }
        set.set(match, spouse);
        set.set(i, withSpouse(p, match));
        return new Wedding(match, i, from, p.nation());
    }

    /** A child born to a holder and their spouse, into the holder's family and line. */
    static void born(List<Person> people, int h, List<Culture> cultures, Random random, Setter set) {
        Person p = people.get(h);
        Person other = people.get(p.spouse());
        Person.Hold hold = new Person.Hold(Person.Kind.CHILD, p.hold().province(), p.hold().x(), p.hold().z());
        // shortcut: born with the skills they will grow into (shown from birth); grow them with age if pages should show a child's
        Person c = RealmPlanner.person(random, cultures.get(p.culture()), p.culture(), p.faith(), random.nextBoolean() ? p.race() : other.race(), hold,
                p.nation(), false, Stat.TITLE_LIMITS.clone());
        set.set(people.size(), new Person(c.given(), p.family(), c.female(), 0, c.culture(), c.faith(), c.race(), c.stats(), c.traits(), hold, h,
                p.nation(), false));
    }

    static Person withSpouse(Person p, int spouse) {
        return new Person(p.given(), p.family(), p.female(), p.age(), p.culture(), p.faith(), p.race(), p.stats(), p.traits(), p.hold(), p.liege(),
                p.nation(), p.away(), spouse);
    }
}
