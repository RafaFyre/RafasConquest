package rafasconquest.client;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Honours;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Race;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A character's page, opened from a hold on the world map or a figure on a nation's page. Over their court
 * ({@link CourtView}), the camera on them where they stand on the left (gliding there from wherever it was): their name
 * and title at the top, with their culture, faith, race, marriage, sex and age under it; their nation's banner on the
 * right; their traits, titles and feats as icons (hovering over one names it and says what it means); a description of
 * them, with their liege, friends and rivals; their skills; and their retainers (vassals) along the bottom. Any person
 * shown, in the court too, opens their own page; the arrow at the top left goes back, the cross closes every page.
 */
public class CharacterScreen extends Screen {
    private static final int TEXT = Portraits.TEXT;
    private static final int MUTED = Portraits.MUTED;
    private static final int GOLD = Portraits.GOLD;
    private static final int GOOD = 0xFF7FC97F;
    private static final int BAD = 0xFFE07A6A;
    private static final int LINK = 0xFFB8D8F8;
    /** Each skill's diamond, as the skills are ordered. */
    private static final int[] STAT_COLOURS = {0xFF3CC26A, 0xFFC01818, 0xFF3A6CC8, 0xFF7A48D8, 0xFFF0A020, 0xFFF4F4F4};
    private static final int BAR = 0xFF1C1D1F;
    private static final int BAR_EDGE = 0xFF555658;
    /** Where they stand on the screen (the middle of them), and how close the camera is: as tall as most of the screen. */
    private static final float MODEL_X = 0.199F;
    private static final float MODEL_Y = 0.62F;
    private static final float MODEL_ZOOM = 0.31F;

    private final Screen parent;
    private final BaseMap base;
    private final int index;
    /** Their court, with the camera on them (handed on from the page that opened this one, so it glides). */
    @Nullable
    private CourtView court;
    @Nullable
    private Portraits.Banner banner;
    private final List<Retainer> retainers = new ArrayList<>();
    private int unseenRetainers;
    /** What can be hovered (icons) or clicked (people), laid out as the page is drawn. */
    private final List<Spot> spots = new ArrayList<>();

    /** A vassal standing along the bottom: their index, where their feet are, how big, and their model. */
    private record Retainer(int person, int x, int feet, int scale, @Nullable AbstractRecruitEntity entity) {
        boolean contains(double mx, double my) {
            return Math.abs(mx - x) <= scale / 2.0 && my >= feet - scale * 2 && my <= feet;
        }
    }

    /** A place on the page: hovering shows its name and description; if it is a person, a click opens their page. */
    private record Spot(int x, int y, int w, int h, String name, String description, int person) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    public CharacterScreen(Screen parent, BaseMap base, int index) {
        this(parent, base, index, null);
    }

    public CharacterScreen(Screen parent, BaseMap base, int index, @Nullable CourtView court) {
        super(Component.literal(base.realm().people().get(index).name()));
        this.parent = parent;
        this.base = base;
        this.index = index;
        this.court = court;
    }

    /**
     * What a character is called with their hold: "Duke Aldric Valcourt of Shukai", with their sword title after it if
     * their prowess earns one ("..., Sword Saint"). A sanctum's ruler is styled by their sword title alone.
     */
    public static String styled(BaseMap base, Person p) {
        return rafasconquest.peerage.Names.styled(base, p);
    }

    @Override
    protected void init() {
        Portraits.buttons(this, width, this::onClose, this::addRenderableWidget);
        Person p = base.realm().people().get(index);
        CourtView shown = CourtView.of(base, index, court);
        boolean fresh = shown != court;
        court = shown;
        court.showPerson(index, MODEL_X, MODEL_Y, MODEL_ZOOM);
        if (fresh) {
            court.arrive();
        }
        banner = p.nation() >= 0 ? new Portraits.Banner(minecraft, base.nations().get(p.nation()).id()) : null;
        layOutRetainers();
    }

    /** The vassals, most powerful first, along the bottom between the description and the banner. */
    private void layOutRetainers() {
        retainers.clear();
        Realm realm = base.realm();
        List<Integer> vassals = new ArrayList<>(realm.vassals(index));
        vassals.sort(Comparator.comparingInt((Integer i) -> NationScreen.power(realm, i)).reversed());
        int scale = Math.max(10, (int) (height * 0.17 / 1.95));
        int spacing = Math.max(scale * 2, (int) (width * 0.15));
        int feet = (int) (height * 0.955);
        int x = (int) (width * 0.427);
        int shown = 0;
        for (int v : vassals) {
            if (x > width * 0.80) {
                break;
            }
            retainers.add(new Retainer(v, x, feet, scale, Portraits.model(minecraft, base, v)));
            x += spacing;
            shown++;
        }
        unseenRetainers = vassals.size() - shown;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        court.render(g, width, height, mouseX, mouseY, partialTick);
        // The court shows plainly behind them on the left, dimmed behind the page on the right.
        g.fill((int) (width * 0.31), 0, width, height, 0xC8000000 | court.sky() & 0xFFFFFF);
        spots.clear();
        Realm realm = base.realm();
        Person p = realm.people().get(index);
        BaseMap.Nation nation = p.nation() >= 0 ? base.nations().get(p.nation()) : null;
        boolean ruler = nation != null && realm.ruler(p.nation()) == index;
        String place = base.names().get(p.hold().province());

        // The name, in its box.
        int nameX = (int) (width * 0.103);
        int nameY = (int) (height * 0.027);
        int nameW = (int) (width * 0.43);
        int nameH = Math.max(16, (int) (height * 0.074));
        Portraits.box(g, nameX, nameY, nameW, nameH);
        String name = header(p, nation, ruler, place);
        float size = Math.min(1.5F, (nameW - 10F) / Math.max(1, font.width(name)));
        g.pose().pushPose();
        g.pose().translate(nameX + nameW / 2F - font.width(name) * size / 2, nameY + nameH / 2F - 4 * size, 0);
        g.pose().scale(size, size, 1);
        g.drawString(font, name, 0, 0, TEXT, false);
        g.pose().popPose();

        // Under it, who they are.
        int infoY = (int) (height * 0.125);
        int x = (int) (width * 0.143);
        String faith = realm.faiths().get(p.faith()).name();
        boolean ownFaith = nation == null || realm.nationFaith()[p.nation()] == p.faith();
        x = info(g, x, infoY, realm.cultures().get(p.culture()).name(), 0xFF8A7CE0, "Culture", "Of the " + realm.cultures().get(p.culture()).name()
                + " culture, of the " + realm.cultures().get(p.culture()).homeland().title().toLowerCase(Locale.ROOT) + " lands.");
        x = info(g, x, infoY, faith, 0xFFF0B020, "Faith", ownFaith ? "Keeps the faith of " + faith + "."
                : "Keeps the faith of " + faith + ", not the nation's (" + realm.faiths().get(realm.nationFaith()[p.nation()]).name() + ").");
        Race race = Race.values()[p.race()];
        x = info(g, x, infoY, race.title(), 0xFF3CC26A, "Race", "Of the " + race.title() + ", the people of the "
                + race.homeland().title().toLowerCase(Locale.ROOT) + " lands.");
        x = info(g, x, infoY, "Unmarried", 0xFFE07AB0, "Marriage", "Not married.");
        info(g, x, infoY, (p.female() ? "Female, " : "Male, ") + p.age(), 0xFFE08A8A, "Age", p.age() + " years old.");

        // They stand in their court on the left (drawn above); their nation's banner on the right.
        int bannerX = (int) (width * 0.853);
        int bannerW = (int) (width * 0.077);
        int bannerH = (int) (height * 0.398);
        if (banner != null) {
            banner.draw(g, bannerX, nameY, bannerW, bannerH);
        }

        // Traits, titles and feats: an icon each (blank for now).
        int icon = Math.max(10, (int) (height * 0.047));
        int gap = Math.max(3, icon / 3);
        int middleX = (int) (width * 0.333);
        int rightX = (int) (width * 0.57);
        label(g, "TRAITS", middleX, (int) (height * 0.165));
        int iy = (int) (height * 0.186);
        for (int i = 0; i < p.traits().size(); i++) {
            Personality t = p.traits().get(i);
            square(g, middleX + i * (icon + gap), iy, icon, new Spot(middleX + i * (icon + gap), iy, icon, icon, t.title(), t.description(), -1), mouseX, mouseY);
        }
        label(g, "TITLES", rightX, (int) (height * 0.037));
        List<Honours.Honour> titles = Honours.titles(p, base.titles().seatOf(p.hold().province()), place, nation == null ? null : nation.name(), ruler);
        iy = (int) (height * 0.057);
        for (int i = 0; i < titles.size(); i++) {
            Honours.Honour h = titles.get(i);
            square(g, rightX + i * (icon + gap), iy, icon, new Spot(rightX + i * (icon + gap), iy, icon, icon, h.name(), h.description(), -1), mouseX, mouseY);
        }
        if (titles.isEmpty()) {
            g.drawString(font, "None", rightX, iy + icon / 2 - 4, MUTED, false);
        }
        label(g, "FEATS", rightX, (int) (height * 0.122));
        List<Honours.Honour> feats = Honours.feats(p);
        iy = (int) (height * 0.138);
        for (int i = 0; i < feats.size(); i++) {
            Honours.Honour h = feats.get(i);
            Spot spot = new Spot(rightX + i * (icon + gap), iy, icon, icon, h.name(), h.description(), -1);
            spots.add(spot);
            Portraits.circle(g, spot.x() + icon / 2, iy + icon / 2, icon / 2, spot.contains(mouseX, mouseY) ? Portraits.ICON_HOVER : Portraits.ICON);
        }
        if (feats.isEmpty()) {
            g.drawString(font, "None yet", rightX, iy + icon / 2 - 4, MUTED, false);
        }

        // The description, with the people around them.
        label(g, "DESCRIPTION", middleX, (int) (height * 0.247));
        int boxTop = (int) (height * 0.274);
        int boxBottom = (int) (height * 0.703);
        int descW = (int) (width * 0.552) - middleX;
        Portraits.box(g, middleX, boxTop, descW, boxBottom - boxTop);
        g.enableScissor(middleX + 1, boxTop + 1, middleX + descW - 1, boxBottom - 1);
        describe(g, p, nation, place, middleX + 6, boxTop + 6, descW - 12, mouseX, mouseY);
        g.disableScissor();

        // The skills.
        label(g, "STATS", rightX, (int) (height * 0.206));
        int statsTop = (int) (height * 0.227);
        int statsW = (int) (width * 0.809) - rightX;
        Portraits.box(g, rightX, statsTop, statsW, boxBottom - statsTop);
        int row = (boxBottom - statsTop - 8) / Stat.values().length;
        int sy = statsTop + 6;
        for (Stat s : Stat.values()) {
            int v = p.stat(s);
            String tier = s == Stat.PROWESS ? Stat.superhumanTier(v) : "";
            int r = Math.max(4, Math.min(8, row / 3));
            Portraits.diamond(g, rightX + 8 + r, sy + row / 2 - 1, r, STAT_COLOURS[s.ordinal()], 0xFFE8E8E8);
            int textX = rightX + 16 + 2 * r;
            g.drawString(font, s.title().toUpperCase(Locale.ROOT), textX, sy + 1, TEXT, false);
            String value = v + " / " + s.max() + (tier.isEmpty() ? "" : "  " + tier);
            g.drawString(font, value, rightX + statsW - 10 - font.width(value), sy + 1, tier.isEmpty() ? MUTED : GOLD, false);
            int barY = sy + 11;
            int barW = rightX + statsW - 10 - textX;
            int barH = Math.max(4, Math.min(8, row - 14));
            g.fill(textX, barY, textX + barW, barY + barH, BAR);
            g.renderOutline(textX, barY, barW, barH, BAR_EDGE);
            g.fill(textX + 1, barY + 1, textX + 1 + (barW - 2) * v / s.max(), barY + barH - 1, tier.isEmpty() ? STAT_COLOURS[s.ordinal()] : GOLD);
            sy += row;
        }

        // The retainers, along the bottom.
        label(g, "RETAINERS", middleX, (int) (height * 0.726));
        Retainer hovered = null;
        for (Retainer r : retainers) {
            if (r.contains(mouseX, mouseY)) {
                hovered = r;
            }
            if (r.entity() != null) {
                InventoryScreen.renderEntityInInventoryFollowsMouse(g, r.x(), r.feet(), r.scale(), r.x() - mouseX,
                        r.feet() - r.scale() * 1.6F - mouseY, r.entity());
            }
        }
        if (retainers.isEmpty()) {
            g.drawString(font, "None", middleX, (int) (height * 0.726) + 12, MUTED, false);
        }
        if (unseenRetainers > 0) {
            String more = "and " + unseenRetainers + " more";
            g.drawString(font, more, (int) (width * 0.82), height - 12, MUTED, false);
        }

        super.render(g, mouseX, mouseY, partialTick);

        // What the mouse is over, named while it stays there.
        if (hovered != null) {
            Person v = realm.people().get(hovered.person());
            g.renderComponentTooltip(font, List.of(Component.literal(styled(base, v)),
                    Component.literal("What they think of " + p.given() + ": " + signed(realm.opinion(hovered.person(), index)) + " (click)")
                            .withStyle(ChatFormatting.GRAY)), mouseX, mouseY);
            return;
        }
        for (Spot s : spots) {
            if (s.contains(mouseX, mouseY)) {
                List<FormattedCharSequence> lines = new ArrayList<>();
                lines.add(Component.literal(s.name()).withStyle(ChatFormatting.GOLD).getVisualOrderText());
                lines.addAll(font.split(Component.literal(s.description()), 200));
                g.renderTooltip(font, lines, mouseX, mouseY);
                return;
            }
        }
    }

    /** "EMPRESS CALANTHE OF ULDARESH": their title, given name and where they hold; a sanctum's ruler by their sword. */
    private static String header(Person p, @Nullable BaseMap.Nation nation, boolean ruler, String place) {
        String sword = Stat.superhumanTier(p.stat(Stat.PROWESS));
        String title = nation != null && nation.sanctum() && ruler && !sword.isEmpty() ? sword
                : Realm.title(p, nation == null ? 0 : nation.tier().ordinal());
        String where = ruler && nation != null && !nation.sanctum()
                ? nation.name().endsWith(" Empire") ? nation.name().substring(0, nation.name().length() - " Empire".length()) : nation.name()
                : place;
        return (title + " " + p.given() + " of " + where).toUpperCase(Locale.ROOT);
    }

    /** One of the items under the name: a diamond and a word; returns where the next goes. */
    private int info(GuiGraphics g, int x, int y, String text, int colour, String what, String description) {
        String shown = text.toUpperCase(Locale.ROOT);
        Portraits.diamond(g, x + 5, y + 4, 5, colour, 0xFFF0F0F0);
        g.drawString(font, shown, x + 15, y, TEXT, false);
        int w = 15 + font.width(shown);
        spots.add(new Spot(x, y - 2, w, 12, what, description, -1));
        return x + w + 14;
    }

    private void label(GuiGraphics g, String text, int x, int y) {
        g.drawString(font, text, x, y, TEXT, false);
    }

    /** A blank square icon, lighter while hovered. */
    private void square(GuiGraphics g, int x, int y, int size, Spot spot, int mouseX, int mouseY) {
        spots.add(spot);
        g.fill(x, y, x + size, y + size, spot.contains(mouseX, mouseY) ? Portraits.ICON_HOVER : Portraits.ICON);
        g.renderOutline(x, y, size, size, 0xFFE8FCFF);
    }

    /** Who they are, in words, then their liege, friends and rivals as names to click, each with what they think of them. */
    private void describe(GuiGraphics g, Person p, @Nullable BaseMap.Nation nation, String place, int x, int y, int w, int mouseX, int mouseY) {
        Realm realm = base.realm();
        StringBuilder text = new StringBuilder(p.name()).append(' ').append(hold(p)).append(' ').append(place);
        text.append(nation != null ? ", serving " + nation.name() + "." : ", answering to no nation.");
        String sword = Stat.superhumanTier(p.stat(Stat.PROWESS));
        if (!sword.isEmpty()) {
            text.append(" A ").append(sword).append(": ").append(Stat.superhumanMeaning(p.stat(Stat.PROWESS))).append('.');
        }
        int owner = base.holders()[p.hold().province()];
        if (p.nation() >= 0 && owner != p.nation()) {
            text.append(" Lost ").append(place).append(owner >= 0 ? " to " + base.nations().get(owner).name() : "").append(" in war.");
        }
        int school = realm.organizationOf(index);
        if (school >= 0) {
            rafasconquest.people.Organization o = realm.organizations().get(school);
            text.append(o.head() == index ? " Heads the " : " Trained at the ").append(o.name()).append('.');
        }
        if (p.away()) {
            text.append(" Away from ").append(place).append('.');
        }
        String fate = ClientPeerage.fate(p.hold().province());
        String successor = ClientPeerage.holder(p.hold().province());
        if (fate != null || successor != null) {
            text.append(' ').append("died".equals(fate) ? "Died" : "dismissed".equals(fate) ? "Dismissed" : "No longer holds it")
                    .append(successor != null ? "; it is now held by " + successor : "").append('.');
        }
        for (FormattedCharSequence line : font.split(Component.literal(text.toString()), w)) {
            g.drawString(font, line, x, y, TEXT, false);
            y += 10;
        }
        y += 4;
        if (p.liege() >= 0) {
            y = people(g, "Liege", List.of(p.liege()), x, y, w, mouseX, mouseY);
        }
        y = people(g, "Friends", realm.bonded(index, Realm.Bond.Kind.FRIEND), x, y, w, mouseX, mouseY);
        people(g, "Rivals", realm.bonded(index, Realm.Bond.Kind.RIVAL), x, y, w, mouseX, mouseY);
    }

    /** A heading and a name to click for each person, with what this character thinks of them. */
    private int people(GuiGraphics g, String heading, List<Integer> who, int x, int y, int w, int mouseX, int mouseY) {
        if (who.isEmpty()) {
            return y;
        }
        g.drawString(font, heading, x, y, MUTED, false);
        y += 10;
        for (int other : who) {
            Person o = base.realm().people().get(other);
            int opinion = base.realm().opinion(index, other);
            String score = signed(opinion);
            String name = font.plainSubstrByWidth(o.name(), w - 8 - font.width(score) - 6);
            Spot spot = new Spot(x + 6, y - 1, font.width(name), 10, styled(base, o), "What " + base.realm().people().get(index).given()
                    + " thinks of them: " + score + ". Click to see their page.", other);
            spots.add(spot);
            boolean over = spot.contains(mouseX, mouseY);
            g.drawString(font, Component.literal(name).withStyle(over ? ChatFormatting.UNDERLINE : ChatFormatting.RESET), x + 6, y, LINK, false);
            g.drawString(font, score, x + w - font.width(score), y, opinion > 0 ? GOOD : opinion < 0 ? BAD : MUTED, false);
            y += 10;
        }
        return y + 3;
    }

    private static String signed(int value) {
        return (value > 0 ? "+" : "") + value;
    }

    private static String hold(Person p) {
        return switch (p.hold().kind()) {
            case CAPITAL -> "holds the capital,";
            case CITY -> "holds the city of";
            case TOWN -> "holds the town of";
            case CASTLE -> "holds the castle of";
            case LORD -> "lord of the city of";
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0) {
            // Someone else in the court, where it shows plainly on the left.
            int other = mouseX < width * 0.31 ? court.pick(mouseX, mouseY) : -1;
            if (other >= 0 && other != index) {
                minecraft.setScreen(new CharacterScreen(this, base, other, court));
                return true;
            }
            for (Retainer r : retainers) {
                if (r.contains(mouseX, mouseY)) {
                    minecraft.setScreen(new CharacterScreen(this, base, r.person(), court));
                    return true;
                }
            }
            for (Spot s : spots) {
                if (s.person() >= 0 && s.contains(mouseX, mouseY)) {
                    minecraft.setScreen(new CharacterScreen(this, base, s.person(), court));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
