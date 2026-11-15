package rafasconquest.client;

import java.util.HashMap;
import java.util.Map;

/** What the client knows of the players' land: who holds which county (by its seat province), and what befell those they replaced. */
public final class ClientPeerage {
    private static Map<Integer, String> holders = new HashMap<>();
    private static Map<Integer, String> fates = new HashMap<>();

    private ClientPeerage() {
    }

    private static Map<Integer, java.util.UUID> holderIds = new HashMap<>();

    public static void set(Map<Integer, String> held, Map<Integer, String> gone, Map<Integer, java.util.UUID> ids) {
        holders = new HashMap<>(held);
        fates = new HashMap<>(gone);
        holderIds = new HashMap<>(ids);
    }

    /** The player holding a county, or null. */
    public static java.util.UUID holderId(int province) {
        return holderIds.get(province);
    }

    /** Whether a player now holds this person's own hold (not their family's: spouses and children share its province). */
    public static boolean replaced(rafasconquest.people.Person p) {
        rafasconquest.people.Person.Kind kind = p.hold().kind();
        return kind != rafasconquest.people.Person.Kind.DEAD && kind != rafasconquest.people.Person.Kind.EXILE && !kind.household()
                && holderIds.containsKey(p.hold().province());
    }

    /** The player holding a county, styled ("Count Steve"), or null. */
    public static String holder(int province) {
        return holders.get(province);
    }

    /** What befell the character who held it: "died", "dismissed", or null. */
    public static String fate(int province) {
        return fates.get(province);
    }
}
