package rafasconquest.client;

import java.util.HashMap;
import java.util.Map;

/** What the client knows of the players' land: who holds which county (by its seat province), and what befell those they replaced. */
public final class ClientPeerage {
    private static Map<Integer, String> holders = new HashMap<>();
    private static Map<Integer, String> fates = new HashMap<>();

    private ClientPeerage() {
    }

    public static void set(Map<Integer, String> held, Map<Integer, String> gone) {
        holders = new HashMap<>(held);
        fates = new HashMap<>(gone);
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
