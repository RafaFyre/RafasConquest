package rafasconquest.peerage;

import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;

/** How characters are called, on the server and the client alike. */
public final class Names {
    private Names() {
    }

    /**
     * A character with their hold: "Duke Aldric Valcourt of Shukai", with their sword title after it if their prowess
     * earns one ("..., Sword Saint"). A sanctum's ruler is styled by their sword title alone.
     */
    public static String styled(BaseMap base, Person p) {
        BaseMap.Nation nation = p.nation() >= 0 ? base.nations().get(p.nation()) : null;
        String sword = Stat.superhumanTier(p.stat(Stat.PROWESS));
        String where = " of " + base.names().get(p.hold().province());
        if (nation != null && nation.sanctum() && nation.seat() == p.hold().province() && !sword.isEmpty()) {
            return sword + " " + p.name() + where;
        }
        String title = Realm.title(p, nation == null ? 0 : nation.tier().ordinal()) + " " + p.name() + where;
        return sword.isEmpty() ? title : title + ", " + sword;
    }

    /** A player with their noble title, if they have one: "Count Steve", "Knight Steve", or just "Steve". */
    public static String player(String name, NobleTitle title) {
        return title.noble() ? title.title() + " " + name : name;
    }
}
