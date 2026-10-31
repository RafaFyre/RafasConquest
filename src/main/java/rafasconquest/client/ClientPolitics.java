package rafasconquest.client;

import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;

import java.util.List;
import java.util.Map;

/**
 * The nations' politics as the server last sent them: the wars, alliances, conquests and news. Conquests are written
 * into the client's world map as they come (and again when the map itself arrives), so it is drawn as the land now lies.
 */
public final class ClientPolitics {
    private static ConquestNetwork.PoliticsView view = new ConquestNetwork.PoliticsView(List.of(), List.of(), Map.of(), List.of());

    private ClientPolitics() {
    }

    public static void set(ConquestNetwork.PoliticsView politics) {
        view = politics;
        BaseMap map = rafasconquest.client.map.ClientMapCache.map();
        if (map != null) {
            apply(map);
        }
    }

    /** Writes the conquests into a map's holders. */
    public static void apply(BaseMap map) {
        view.conquered().forEach((p, n) -> {
            if (p >= 0 && p < map.holders().length && n < map.nations().size()) {
                map.holders()[p] = n;
            }
        });
    }

    /** Each war: attacker, defender, the province fought over, and its score (to 100 for the attacker). */
    public static List<int[]> wars() {
        return view.wars();
    }

    public static List<int[]> alliances() {
        return view.alliances();
    }

    public static Map<Integer, Integer> conquered() {
        return view.conquered();
    }

    public static List<String> news() {
        return view.news();
    }
}
