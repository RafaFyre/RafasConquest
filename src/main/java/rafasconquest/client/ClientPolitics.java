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
    private static ConquestNetwork.PoliticsView view = new ConquestNetwork.PoliticsView(List.of(), List.of(), List.of(), Map.of(), List.of());

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

    /** Each war: attacker, defender, duchy? 1 : 0, the title, the county fought for next, counties taken, counties in all, site x, z. */
    public static List<int[]> wars() {
        return view.wars();
    }

    /** Each army in the field: nation, soldiers, x, z, mustering? 1 : 0, which war, and where it heads next (x, z). */
    public static List<int[]> armies() {
        return view.armies();
    }

    /** A war's title by name: "County of Mill", "Duchy of Shukai". */
    public static String titleName(BaseMap map, boolean duchy, int index) {
        rafasconquest.core.TitleMap t = map.titles();
        return duchy ? map.titleName(rafasconquest.core.TitleMap.Tier.DUCHY, t.duchySeat()[index])
                : map.titleName(rafasconquest.core.TitleMap.Tier.COUNTY, t.countySeat()[index]);
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
