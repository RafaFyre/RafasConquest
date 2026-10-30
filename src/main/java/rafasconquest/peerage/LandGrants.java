package rafasconquest.peerage;

import rafasconquest.core.Countryside;
import rafasconquest.core.TitleMap;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Land for a player who has earned it in battle. A noble grants it once the player has won a battle on the noble's
 * nation's side (a {@link Peerage.Merit}) and the noble fought in it, already knows the player, or holds land near it
 * ({@link #NEAR}); and never if the noble is Craven, or the player holds land already. The county comes one of seven
 * ways, chosen among those the noble's situation allows:
 * <ol>
 *   <li>one of the noble's own counties, held freely;</li>
 *   <li>one of the noble's spare counties, as the noble's vassal;</li>
 *   <li>a vassal's county the noble disliked, the vassal dismissed, as the noble's vassal;</li>
 *   <li>an empty county of the nation, held freely;</li>
 *   <li>ruins to restore near the noble's land, held freely;</li>
 *   <li>a vassal's county whose holder has recently died, held freely;</li>
 *   <li>the same, as the noble's vassal.</li>
 * </ol>
 * A spare or empty county is one with no hold (no town or castle at its seat) and no ruin.
 */
public final class LandGrants {
    /** A noble whose land lies this near a battle can reward it. */
    static final int NEAR = 3000;
    static final int RUIN_REACH = 4000;

    public enum Way {
        OWN_COUNTY("grants you one of their counties"),
        SPARE_COUNTY_VASSAL("takes you as their vassal, and grants you one of their spare counties"),
        DISLIKED_VASSAL("takes you as their vassal, in place of a vassal they had no love for, who is dismissed"),
        EMPTY_COUNTY("grants you an empty county of the realm"),
        RUINS("grants you ruins to restore"),
        DEAD_VASSAL_FREE("grants you the county of a vassal who has recently died"),
        DEAD_VASSAL_SWORN("takes you as their vassal, in place of a vassal who has recently died");

        private final String says;

        Way(String says) {
            this.says = says;
        }

        public String says() {
            return says;
        }
    }

    /** A grant: the way, the county (its seat province), the liege (-1: none), and what became of the old holder. */
    public record Grant(Way way, int seat, int liege, String fate) {
    }

    private LandGrants() {
    }

    /** The merit that lets a noble grant this player land, or null. */
    public static Peerage.Merit merit(BaseMap map, Peerage.Peer peer, int noble) {
        Person p = map.realm().people().get(noble);
        if (p.has(Personality.CRAVEN) || peer.hold >= 0 || p.nation() < 0) {
            return null;
        }
        String nation = map.nations().get(p.nation()).id().toString();
        for (Peerage.Merit m : peer.merits) {
            boolean fought = m.officers().contains(p.name());
            boolean near = Math.hypot(p.hold().x() - m.x(), p.hold().z() - m.z()) <= NEAR;
            if (m.nation().equals(nation) && (fought || peer.known.contains(noble) || near)) {
                return m;
            }
        }
        return null;
    }

    /** The grant a noble makes, chosen among the ways open to them; null if none is. */
    public static Grant choose(BaseMap map, Peerage peerage, int noble, Random random) {
        Realm realm = map.realm();
        TitleMap t = map.titles();
        Person p = realm.people().get(noble);
        List<Grant> grants = new ArrayList<>();
        List<Integer> spare = new ArrayList<>();
        List<Integer> empty = new ArrayList<>();
        for (int c = 0; c < t.counties(); c++) {
            int seat = t.countySeat()[c];
            if (map.holders()[seat] != p.nation() || realm.holderOf(seat) >= 0 || peerage.heldBy(seat) != null || ruin(map, seat)) {
                continue;
            }
            empty.add(seat);
            if (under(t, seat, p.hold().province())) {
                spare.add(seat);
            }
        }
        List<Integer> disliked = new ArrayList<>();
        List<Integer> vassalCounties = new ArrayList<>();
        for (int v : realm.vassals(noble)) {
            Person vassal = realm.people().get(v);
            int seat = vassal.hold().province();
            if ((vassal.hold().kind() == Person.Kind.TOWN || vassal.hold().kind() == Person.Kind.CASTLE) && !peerage.replaced(seat)) {
                vassalCounties.add(seat);
                if (realm.opinion(noble, v) < 0) {
                    disliked.add(seat);
                }
            }
        }
        List<Integer> ruins = new ArrayList<>();
        for (Countryside.Settlement s : map.settlements()) {
            if (s.kind() == Countryside.Kind.RUIN && peerage.heldBy(s.province()) == null
                    && Math.hypot(s.x() - p.hold().x(), s.z() - p.hold().z()) <= RUIN_REACH) {
                ruins.add(s.province());
            }
        }
        if (!spare.isEmpty()) {
            grants.add(new Grant(Way.OWN_COUNTY, pick(spare, random), -1, null));
            grants.add(new Grant(Way.SPARE_COUNTY_VASSAL, pick(spare, random), noble, null));
        }
        if (!disliked.isEmpty()) {
            grants.add(new Grant(Way.DISLIKED_VASSAL, pick(disliked, random), noble, "dismissed"));
        }
        if (!empty.isEmpty()) {
            grants.add(new Grant(Way.EMPTY_COUNTY, pick(empty, random), -1, null));
        }
        if (!ruins.isEmpty()) {
            grants.add(new Grant(Way.RUINS, pick(ruins, random), -1, null));
        }
        if (!vassalCounties.isEmpty()) {
            grants.add(new Grant(Way.DEAD_VASSAL_FREE, pick(vassalCounties, random), -1, "died"));
            grants.add(new Grant(Way.DEAD_VASSAL_SWORN, pick(vassalCounties, random), noble, "died"));
        }
        return grants.isEmpty() ? null : grants.get(random.nextInt(grants.size()));
    }

    private static int pick(List<Integer> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    private static boolean ruin(BaseMap map, int province) {
        for (Countryside.Settlement s : map.settlements()) {
            if (s.province() == province && s.kind() == Countryside.Kind.RUIN) {
                return true;
            }
        }
        return false;
    }

    /** Whether a county's seat lies under the title seated where the noble holds (their highest). */
    static boolean under(TitleMap t, int seat, int nobleSeat) {
        TitleMap.Tier tier = t.seatOf(nobleSeat);
        if (tier == null) {
            return false;
        }
        int c = t.county()[seat];
        int nc = t.county()[nobleSeat];
        return switch (tier) {
            case COUNTY -> c == nc;
            case DUCHY -> t.duchy()[c] == t.duchy()[nc];
            case KINGDOM -> t.kingdom()[t.duchy()[c]] == t.kingdom()[t.duchy()[nc]];
            case EMPIRE -> t.empire()[t.kingdom()[t.duchy()[c]]] == t.empire()[t.kingdom()[t.duchy()[nc]]];
        };
    }
}
