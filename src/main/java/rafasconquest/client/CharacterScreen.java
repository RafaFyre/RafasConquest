package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A character's page, opened from a hold on the world map: who they are and what they hold, their skills (prowess with
 * its superhuman rank) and traits, their culture and faith, and the people around them (liege, vassals, friends,
 * rivals) with what they think of each. Any of those names opens that character's page; Esc goes back.
 */
public class CharacterScreen extends Screen {
    private static final int TEXT = 0xFFE8E0D0;
    private static final int MUTED = 0xFFA89F90;
    private static final int GOOD = 0xFF7FC97F;
    private static final int BAD = 0xFFE07A6A;
    private static final int GOLD = 0xFFE0C060;
    private static final int MAX_VASSALS = 8;

    private final Screen parent;
    private final BaseMap base;
    private final int index;

    public CharacterScreen(Screen parent, BaseMap base, int index) {
        super(Component.literal(base.realm().people().get(index).name()));
        this.parent = parent;
        this.base = base;
        this.index = index;
    }

    /** What a character is called with their hold: "Duke Aldric Valcourt of Shukai". */
    public static String styled(BaseMap base, Person p) {
        int tier = p.nation() >= 0 ? base.nations().get(p.nation()).tier().ordinal() : 0;
        return Realm.title(p, tier) + " " + p.name() + " of " + base.names().get(p.hold().province());
    }

    @Override
    protected void init() {
        Realm realm = base.realm();
        Person p = realm.people().get(index);
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose()).bounds(width - 26, 6, 20, 20).build());
        int x = width / 2 + 10;
        int y = 40;
        if (p.liege() >= 0) {
            y = link(x, y + 12, p.liege());
        }
        List<Integer> vassals = realm.vassals(index);
        y += vassals.isEmpty() ? 0 : 12;
        for (int i = 0; i < Math.min(MAX_VASSALS, vassals.size()); i++) {
            y = link(x, y, vassals.get(i));
        }
        for (Realm.Bond.Kind kind : Realm.Bond.Kind.values()) {
            List<Integer> bonded = realm.bonded(index, kind);
            y += bonded.isEmpty() ? 0 : 12;
            for (int other : bonded) {
                y = link(x, y, other);
            }
        }
    }

    /** A button opening another character's page, at (x, y); returns where the next goes. */
    private int link(int x, int y, int other) {
        Person o = base.realm().people().get(other);
        addRenderableWidget(Button.builder(Component.literal(styled(base, o)), b -> minecraft.setScreen(new CharacterScreen(this, base, other)))
                .bounds(x, y, Math.min(260, width - x - 50), 16).build());
        return y + 18;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        Realm realm = base.realm();
        Person p = realm.people().get(index);
        int x = width / 2 - 250;
        int y = 14;
        g.drawString(font, styled(base, p), x, y, GOLD, false);
        y += 14;
        String nation = p.nation() >= 0 ? base.nations().get(p.nation()).name() : "no nation (independent)";
        g.drawString(font, (p.female() ? "Woman" : "Man") + ", " + p.age() + ", " + hold(p) + " — " + nation, x, y, MUTED, false);
        y += 12;
        g.drawString(font, "Culture: " + realm.cultures().get(p.culture()).name()
                + "    Faith: " + realm.faiths().get(p.faith()).name(), x, y, TEXT, false);
        if (p.nation() >= 0 && realm.nationFaith()[p.nation()] != p.faith()) {
            y += 12;
            g.drawString(font, "Not of the nation's faith (" + realm.faiths().get(realm.nationFaith()[p.nation()]).name() + ")", x, y, BAD, false);
        }
        y += 22;
        for (Stat s : Stat.values()) {
            int v = p.stat(s);
            String tier = s == Stat.PROWESS ? Stat.superhumanTier(v) : "";
            g.drawString(font, s.title(), x, y, TEXT, false);
            g.drawString(font, v + " / " + s.max() + (tier.isEmpty() ? "" : "  " + tier), x + 90, y, tier.isEmpty() ? TEXT : GOLD, false);
            int bar = 120;
            g.fill(x + 170, y + 2, x + 170 + bar, y + 7, 0xFF3A342C);
            g.fill(x + 170, y + 2, x + 170 + bar * v / s.max(), y + 7, tier.isEmpty() ? 0xFFB09060 : GOLD);
            y += 12;
        }
        y += 10;
        g.drawString(font, "Traits: " + p.traits().stream().map(Personality::title).collect(Collectors.joining(", ")), x, y, TEXT, false);

        // The people around them, beside the buttons init() laid out, with what this character thinks of each.
        int rx = width / 2 + 10;
        int ry = 40;
        if (p.liege() >= 0) {
            ry = heading(g, rx, ry, "Liege");
            ry = opinion(g, rx, ry, p.liege());
        }
        List<Integer> vassals = realm.vassals(index);
        if (!vassals.isEmpty()) {
            ry = heading(g, rx, ry, "Vassals (" + vassals.size() + ")");
            for (int i = 0; i < Math.min(MAX_VASSALS, vassals.size()); i++) {
                ry = opinion(g, rx, ry, vassals.get(i));
            }
        }
        for (Realm.Bond.Kind kind : Realm.Bond.Kind.values()) {
            List<Integer> bonded = realm.bonded(index, kind);
            if (!bonded.isEmpty()) {
                ry = heading(g, rx, ry, kind == Realm.Bond.Kind.FRIEND ? "Friends" : "Rivals");
                for (int other : bonded) {
                    ry = opinion(g, rx, ry, other);
                }
            }
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    private int heading(GuiGraphics g, int x, int y, String text) {
        g.drawString(font, text, x, y + 2, MUTED, false);
        return y + 12;
    }

    /** What this character thinks of another, beside the other's button. */
    private int opinion(GuiGraphics g, int x, int y, int other) {
        int o = base.realm().opinion(index, other);
        int bx = x + Math.min(260, width - x - 50) + 6;
        g.drawString(font, (o > 0 ? "+" : "") + o, bx, y + 4, o > 0 ? GOOD : o < 0 ? BAD : MUTED, false);
        return y + 18;
    }

    private String hold(Person p) {
        return switch (p.hold().kind()) {
            case CAPITAL -> "holds the capital";
            case CITY -> "holds a city";
            case TOWN -> "holds a town";
            case CASTLE -> "holds a castle";
        };
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
