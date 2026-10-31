package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Demographics;
import rafasconquest.people.Person;
import rafasconquest.people.Race;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A nation's page, opened from its capital on the world map: its banner at both top corners, its name and people, what
 * it is (its rank, faith, ways, culture and race, with their shares of its people), and its peerage as a pyramid of
 * people standing on the podiums of its court ({@link CourtView}), in a hall of its land's style: the ruler at the top,
 * the greatest nobles under them, the lesser ones in wider rows below. Clicking someone glides the camera in on them and
 * shows who they are; clicking them again opens their page. Esc (or the arrow) glides back out, then goes back.
 */
public class NationScreen extends Screen {
    private static final int BOX = Portraits.BOX;
    private static final int BOX_EDGE = Portraits.BOX_EDGE;
    private static final int TEXT = Portraits.TEXT;
    private static final int MUTED = Portraits.MUTED;
    private static final int DIAMOND = 0xFF3CC26A;
    private static final int DIAMOND_EDGE = 0xFFE8F8EC;
    /** Shares below this are not named. */
    private static final float NAMED_SHARE = 0.03F;

    private final Screen parent;
    protected final BaseMap base;
    protected final int nation;
    /** The ruler, or -1. */
    private final int ruler;
    /** The court and its camera, kept while the pages it opens come and go. */
    @Nullable
    private CourtView court;
    @Nullable
    private Portraits.Banner banner;
    /** Where the whole court is centred, and where someone looked at stands, on the screen. */
    private static final float WHOLE_X = 0.5F;
    private static final float WHOLE_Y = 0.6F;
    private static final float CLOSE_X = 0.36F;
    private static final float CLOSE_Y = 0.6F;

    public NationScreen(Screen parent, BaseMap base, int nation) {
        super(Component.literal(base.nations().get(nation).name()));
        this.parent = parent;
        this.base = base;
        this.nation = nation;
        this.ruler = base.realm().ruler(nation);
    }

    /** How powerful a noble is: the rank of their hold first, then how many answer to them, then their skills. */
    static int power(Realm realm, int i) {
        Person p = realm.people().get(i);
        int rank = switch (p.hold().kind()) {
            case CAPITAL -> 4;
            case CITY -> 3;
            case TOWN -> 2;
            case CASTLE, LORD -> 1;
        };
        int skills = 0;
        for (Stat s : Stat.values()) {
            skills += s == Stat.PROWESS ? 0 : p.stat(s);
        }
        return rank * 100_000 + realm.vassals(i).size() * 1_000 + skills;
    }

    @Override
    protected void init() {
        Portraits.buttons(this, width, this::onClose, this::addRenderableWidget);
        banner = new Portraits.Banner(minecraft, base.nations().get(nation).id());
        if (court == null) {
            court = court();
            court.showWhole(WHOLE_X, WHOLE_Y);
            court.arrive();
        } else if (court.focus() >= 0) {
            court.showPerson(court.focus(), CLOSE_X, CLOSE_Y); // back from their page: still on them
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        court.render(g, width, height, mouseX, mouseY, partialTick);
        int hovered = court.pick(mouseX, mouseY);

        // The banners, at both top corners.
        int bannerWidth = Math.max(30, Math.min(64, (int) (width * 0.075)));
        int bannerHeight = Math.min((int) (height * 0.4), bannerWidth * 27 / 10);
        int leftBanner = (int) (width * 0.07);
        if (banner != null) {
            banner.draw(g, leftBanner, 8, bannerWidth, bannerHeight);
            banner.draw(g, width - leftBanner - bannerWidth, 8, bannerWidth, bannerHeight);
        }
        if (court.focus() >= 0) {
            card(g, court.focus());
            super.render(g, mouseX, mouseY, partialTick);
            tooltip(g, hovered, mouseX, mouseY);
            return;
        }

        // The title, with what it counts beside it.
        int boxX = leftBanner + bannerWidth + 4;
        String title = heading();
        String population = subheading();
        int titleWidth = font.width(title) * 3 / 2;
        int titleBoxWidth = titleWidth + font.width(population) + 30;
        g.fill(boxX, 14, boxX + titleBoxWidth, 38, BOX);
        g.renderOutline(boxX, 14, titleBoxWidth, 24, BOX_EDGE);
        g.pose().pushPose();
        g.pose().translate(boxX + 10, 20, 0);
        g.pose().scale(1.5F, 1.5F, 1);
        g.drawString(font, title, 0, 0, TEXT, false);
        g.pose().popPose();
        g.drawString(font, population, boxX + 20 + titleWidth, 23, MUTED, false);

        // What it is.
        List<String[]> bullets = facts();
        int listWidth = 0;
        for (String[] b : bullets) {
            listWidth = Math.max(listWidth, Math.max(font.width(b[0]) + 30, b.length > 1 ? font.width(b[1]) + 30 : 0));
        }
        int listHeight = 8;
        for (String[] b : bullets) {
            listHeight += b.length > 1 ? 22 : 13;
        }
        int listTop = 42;
        g.fill(boxX, listTop, boxX + listWidth, listTop + listHeight, BOX);
        g.renderOutline(boxX, listTop, listWidth, listHeight, BOX_EDGE);
        int y = listTop + 6;
        for (String[] b : bullets) {
            Portraits.diamond(g, boxX + 10, y + 4, 4, DIAMOND, DIAMOND_EDGE);
            g.drawString(font, b[0], boxX + 20, y, TEXT, false);
            y += 11;
            if (b.length > 1) {
                g.drawString(font, b[1], boxX + 20, y, MUTED, false);
                y += 11;
            }
            y += 2;
        }

        if (ruler < 0) {
            g.drawCenteredString(font, "No ruler", width / 2, (int) (height * 0.4), MUTED);
        }
        if (court.unseated > 0) {
            String more = unseated(court.unseated);
            g.drawString(font, more, width - font.width(more) - 8, height - 12, MUTED, false);
        }
        super.render(g, mouseX, mouseY, partialTick);
        tooltip(g, hovered, mouseX, mouseY);
    }

    /** The court shown: the nation's. */
    protected CourtView court() {
        return CourtView.ofNation(base, nation);
    }

    /** "THE EMPIRE OF ULDARESH". */
    protected String heading() {
        return title(base.nations().get(nation));
    }

    /** Beside the title: its people. */
    protected String subheading() {
        return "Population " + String.format("%,d", base.realm().demographics(nation).population());
    }

    /** The lines beside the diamonds: each a line, and maybe a quieter one under it. */
    protected List<String[]> facts() {
        return bullets(base.realm(), base.nations().get(nation), base.realm().demographics(nation));
    }

    /** "and 4 more nobles": those with no room in the court. */
    protected String unseated(int count) {
        return "and " + count + " more noble" + (count == 1 ? "" : "s");
    }

    /** Names whoever the mouse is over. */
    private void tooltip(GuiGraphics g, int person, int mouseX, int mouseY) {
        if (person < 0) {
            return;
        }
        Person p = base.realm().people().get(person);
        String hint = person == court.focus() ? "Click to see their page" : "Holds " + base.names().get(p.hold().province()) + " (click)";
        g.renderComponentTooltip(font, List.of(Component.literal(CharacterScreen.styled(base, p)),
                Component.literal(hint).withStyle(net.minecraft.ChatFormatting.GRAY)), mouseX, mouseY);
    }

    /** Who the camera is on, beside them: their style, hold, liege, vassals and traits, and how to see more. */
    private void card(GuiGraphics g, int person) {
        Realm realm = base.realm();
        Person p = realm.people().get(person);
        int x = (int) (width * 0.56);
        int y = (int) (height * 0.22);
        int w = Math.min(260, width - x - 16);
        List<String> lines = new ArrayList<>();
        lines.add(p.name() + ", " + p.age());
        lines.add("Holds " + base.names().get(p.hold().province()));
        if (p.liege() >= 0) {
            lines.add("Sworn to " + realm.people().get(p.liege()).name());
        }
        int vassals = realm.vassals(person).size();
        if (vassals > 0) {
            lines.add(vassals + " vassal" + (vassals == 1 ? "" : "s"));
        }
        lines.add(p.traits().stream().map(rafasconquest.people.Personality::title).collect(Collectors.joining(", ")));
        List<net.minecraft.util.FormattedCharSequence> head = font.split(Component.literal(CharacterScreen.styled(base, p)), w - 16);
        int h = 12 + head.size() * 10 + 6 + lines.size() * 11 + 18;
        Portraits.box(g, x, y, w, h);
        int ty = y + 8;
        for (net.minecraft.util.FormattedCharSequence line : head) {
            g.drawString(font, line, x + 8, ty, Portraits.GOLD, false);
            ty += 10;
        }
        ty += 6;
        for (String line : lines) {
            g.drawString(font, font.plainSubstrByWidth(line, w - 16), x + 8, ty, TEXT, false);
            ty += 11;
        }
        g.drawString(font, "Click them again to see their page", x + 8, ty + 4, MUTED, false);
    }

    /** "THE EMPIRE OF ULDARESH": the nation's rank and its name, or a sanctum's own name. */
    private static String title(BaseMap.Nation n) {
        if (n.sanctum()) {
            return "THE " + n.name().toUpperCase(java.util.Locale.ROOT);
        }
        String name = n.name().endsWith(" Empire") ? n.name().substring(0, n.name().length() - " Empire".length()) : n.name();
        return ("The " + rank(n) + " of " + name).toUpperCase(java.util.Locale.ROOT);
    }

    /** What kind of nation it is by the title it holds. */
    static String rank(BaseMap.Nation n) {
        if (n.sanctum()) {
            return "Sword Sanctum";
        }
        return switch (n.tier()) {
            case EMPIRE -> "Empire";
            case KINGDOM -> "Kingdom";
            case DUCHY -> "Duchy";
            case COUNTY -> "City-state";
        };
    }

    /** The lines beside the diamonds: each a line, and maybe a quieter one under it. */
    private List<String[]> bullets(Realm realm, BaseMap.Nation n, Demographics people) {
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{rank(n)});
        if (nation < realm.nationFaith().length) {
            String faith = realm.faiths().get(realm.nationFaith()[nation]).name();
            lines.add(new String[]{"Followers of " + (faith.contains(" ") ? "the " : "") + faith, piety(realm.nationPiety()[nation])});
        }
        if (ruler >= 0) {
            lines.add(new String[]{ways(realm.people().get(ruler))});
        }
        if (nation < realm.nationCulture().length) {
            lines.add(new String[]{realm.cultures().get(realm.nationCulture()[nation]).name(),
                    "Cultures: " + shares(people.cultures(), h -> realm.cultures().get(h).name())});
        }
        Race race = Race.values()[Demographics.largest(people.races())];
        lines.add(new String[]{"Mostly " + race.title(), "Races: " + shares(people.races(), h -> Race.values()[h].title())});
        return lines;
    }

    /** "Uldareshi 72%, Frostish 20%, others 8%". */
    private static String shares(float[] shares, java.util.function.IntFunction<String> name) {
        List<Demographics.Share> named = Demographics.largest(shares, NAMED_SHARE);
        float rest = 1;
        List<String> parts = new ArrayList<>();
        for (Demographics.Share s : named.subList(0, Math.min(3, named.size()))) {
            parts.add(name.apply(s.index()) + " " + Math.round(s.share() * 100) + "%");
            rest -= s.share();
        }
        if (Math.round(rest * 100) > 0) {
            parts.add("others " + Math.round(rest * 100) + "%");
        }
        return parts.stream().collect(Collectors.joining(", "));
    }

    private static String piety(float piety) {
        return piety < 0.2F ? "Worldly" : piety < 0.4F ? "Lax in their faith" : piety < 0.6F ? "Observant" : piety < 0.8F ? "Pious" : "Devout";
    }

    /** The nation's ways, from what its ruler is best at. */
    private static String ways(Person ruler) {
        Stat best = Stat.DIPLOMACY;
        for (Stat s : Stat.values()) {
            if (s != Stat.PROWESS && ruler.stat(s) > ruler.stat(best)) {
                best = s;
            }
        }
        return switch (best) {
            case DIPLOMACY -> "Nation of Accords";
            case MARTIAL -> "Nation of Swords";
            case STEWARDSHIP -> "Nation of Laws";
            case INTRIGUE -> "Nation of Whispers";
            default -> "Nation of Scholars";
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0) {
            int person = court.pick(mouseX, mouseY);
            if (person >= 0 && person == court.focus()) {
                minecraft.setScreen(new CharacterScreen(this, base, person, court));
            } else if (person >= 0) {
                court.showPerson(person, CLOSE_X, CLOSE_Y);
            } else if (court.focus() >= 0) {
                court.showWhole(WHOLE_X, WHOLE_Y);
            }
            return true;
        }
        return false;
    }

    /** Back out to the whole court first, then to the page before. */
    @Override
    public void onClose() {
        if (court != null && court.focus() >= 0) {
            court.showWhole(WHOLE_X, WHOLE_Y);
            return;
        }
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
