package rafasconquest.politics;

import rafasconquest.people.Person;

import java.util.Set;

/** What the nations' politics need to know of the world: who holds what, the titles, who rules, and their armies. */
public interface Lands {
    int nations();

    int provinces();

    /** Whether a nation can go to war at all: it has a ruler and land, and is not a sword sanctum (a school, at peace). */
    boolean canWage(int nation);

    /** The nation's ruler (an index into the people), or -1. */
    int ruler(int nation);

    Person person(int index);

    /** What one person thinks of another, -100 to 100. */
    int opinion(int a, int b);

    /** Who holds a province now (conquests included), or -1. */
    int owner(int province);

    /** Hands a province to a nation (a conquest). */
    void transfer(int province, int nation);

    /** The province a nation's seat is in. */
    int seat(int nation);

    /** Where a nation's capital stands, (x, z): its army musters there. */
    int[] capital(int nation);

    Set<Integer> neighbours(int province);

    /** The county a province lies in. */
    int county(int province);

    int counties();

    /** The duchy a county lies in. */
    int duchy(int county);

    /** A title's name: "County of Mill", "Duchy of Shukai". */
    String titleName(Goal goal);

    /** The most soldiers a nation can raise, its people and land as they now are. */
    int levy(int nation);

    String nationName(int nation);

    String provinceName(int province);

    /** A province's middle, (x, z) in blocks. */
    int[] centre(int province);
}
