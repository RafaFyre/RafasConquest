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

    /** Where a nation's capital stands, (x, z). */
    int[] capital(int nation);

    /** A place an army can muster at: a nation's capital, city or castle, by name, and where it stands. */
    record Stronghold(String name, int x, int z) {
    }

    /** The cities and castles a nation now holds, its capital first. */
    java.util.List<Stronghold> strongholds(int nation);

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

    /** Whether a place (x, z) lies in sea or river by the world map, from the world's seed; off the map counts as water. */
    default boolean wet(double x, double z) {
        return false;
    }

    /**
     * A county's walls: its seat city, or its castle. {@code half}: from its middle out to its walls, each way. A port's
     * ({@code outX}, {@code outZ}) is the way out from its harbour wall to sea (zero for one that is no port).
     */
    record Fort(String name, int x, int z, int half, boolean port, int outX, int outZ) {
        /** How far out to sea, past its walls, its fleet meets an enemy's: where the shipping lanes keep. */
        public static final int OFFSHORE = 300;

        /** A port's own ships, sent out against an amphibious assault: this many for each district of its city. */
        public static final int HARBOUR_SHIPS_PER_DISTRICT = 2;
        /** Soldiers aboard each. */
        public static final int HARBOUR_CREW = 20;

        /** How many ships of its own a port has, by its size; none for a castle or a city that is no port. */
        public int harbourShips() {
            return port ? Math.max(1, Math.round(half * 2f / rafasconquest.core.SeatCities.DISTRICT)) * HARBOUR_SHIPS_PER_DISTRICT : 0;
        }

        public double[] sea() {
            return new double[]{x + outX * (half + OFFSHORE), z + outZ * (half + OFFSHORE)};
        }
    }

    /** A county's walls, or null if it has none (no seat city, no castle). */
    @org.jetbrains.annotations.Nullable
    default Fort fort(int county) {
        return null;
    }

    /** Whether a nation holds a port, to send a fleet from. */
    default boolean hasPort(int nation) {
        return false;
    }

    // ---- lords, for feuds ----

    /** The character keeping the hold in a province, or -1. */
    default int holder(int province) {
        return -1;
    }

    /** A county's seat province. */
    default int countySeat(int county) {
        for (int p = 0; p < provinces(); p++) {
            if (county(p) == county) {
                return p;
            }
        }
        return -1;
    }

    /** Every pair of rival characters. */
    default java.util.List<int[]> rivals() {
        return java.util.List.of();
    }

    /** The troops a character brings to a fight: by their standing, with their vassals'. */
    default int troops(int person) {
        return Politics.MIN_ARMY;
    }

    /** Every province under the highest title seated at a province (none if no title is seated there). */
    default java.util.List<Integer> fief(int seat) {
        return java.util.List.of();
    }

    /** The realm's people, for who lives, dies and inherits; null where there is none to keep (as in tests). */
    default rafasconquest.people.Realm realm() {
        return null;
    }

    /** A character's champion for a duel: the best fighter of them and their retinue, none of {@code fallen}; -1 if none. */
    default int champion(int person, Set<Integer> fallen) {
        return fallen.contains(person) ? -1 : person;
    }
}
