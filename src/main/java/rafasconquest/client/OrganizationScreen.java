package rafasconquest.client;

import net.minecraft.client.gui.screens.Screen;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Organization;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An organization's page (a sword sanctum's), opened from its city on the world map, laid out as a nation's: its banners
 * at the top corners, its title with its members counted beside it, what it is, and its people on the podiums of its
 * court: the Sword God on top, the Sword Kings trained there beneath, the Sword Saints below them.
 */
public class OrganizationScreen extends NationScreen {
    private final int organization;

    public OrganizationScreen(Screen parent, BaseMap base, int organization) {
        super(parent, base, base.realm().organizations().get(organization).nation());
        this.organization = organization;
    }

    /** The organization a nation is (a sanctum's), or -1. */
    public static int of(BaseMap base, int nation) {
        List<Organization> all = base.realm().organizations();
        for (int o = 0; o < all.size(); o++) {
            if (all.get(o).nation() == nation) {
                return o;
            }
        }
        return -1;
    }

    private Organization org() {
        return base.realm().organizations().get(organization);
    }

    @Override
    protected CourtView court() {
        return CourtView.ofOrganization(base, org());
    }

    @Override
    protected String heading() {
        return "THE " + org().name().toUpperCase(Locale.ROOT);
    }

    @Override
    protected String subheading() {
        int members = org().members().size();
        return members + " member" + (members == 1 ? "" : "s");
    }

    @Override
    protected List<String[]> facts() {
        Organization o = org();
        Realm realm = base.realm();
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Organization", "A sword sanctum: a school of the sword"});
        if (o.head() >= 0) {
            Person head = realm.people().get(o.head());
            lines.add(new String[]{"Headed by " + head.name(), "Sword God"});
        }
        lines.add(new String[]{o.kings().size() + " Sword King" + (o.kings().size() == 1 ? "" : "s"), "Trained here"});
        lines.add(new String[]{o.saints().size() + " Sword Saint" + (o.saints().size() == 1 ? "" : "s"), "Trained here"});
        lines.add(new String[]{"Seat: " + base.names().get(base.nations().get(o.nation()).seat())});
        return lines;
    }

    @Override
    protected String unseated(int count) {
        return "and " + count + " more Sword Saint" + (count == 1 ? "" : "s");
    }
}
