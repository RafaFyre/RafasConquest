package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.Garrison;
import rafasconquest.peerage.GrandStrategy;

import java.util.List;

/**
 * The face-off, as Total War's battle deployment: an army has marched onto the one it attacks. The attacker's cards
 * down the left, the defender's down the right, each under its nation's colour and general; at the bottom, Begin battle
 * fights it for real. Escape retreats to the map.
 */
final class FaceOffScreen extends Screen implements GrandStrategyScreen.OverMap {
    private static final int GOLD = 0xFFE0C060;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int GREY = 0xFFA8A49C;
    private static final int BACK = 0xF0201D18;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int BOX_W = 220;
    private static final int BOX_H = 74;

    private final GrandStrategyScreen parent;
    private final GrandStrategy.Army attacker;
    private final GrandStrategy.Army defender;
    /** Each side's nation colour (ARGB). */
    private final int attackerColour;
    private final int defenderColour;

    FaceOffScreen(GrandStrategyScreen parent, GrandStrategy.Army attacker, GrandStrategy.Army defender, int attackerColour, int defenderColour) {
        super(Component.literal("Battle Deployment"));
        this.parent = parent;
        this.attacker = attacker;
        this.defender = defender;
        this.attackerColour = attackerColour;
        this.defenderColour = defenderColour;
    }

    static String nation(GrandStrategy.Army a) {
        BaseMap base = ClientMapCache.map();
        return base == null || a.nation() < 0 || a.nation() >= base.nations().size() ? "No nation" : base.nations().get(a.nation()).name();
    }

    static String general(GrandStrategy.Army a) {
        return a.general().isEmpty() ? "Led by their best at war" : "Led by " + a.general();
    }

    private int panelW() {
        return Math.min(200, (width - BOX_W) / 2 - 12);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("Begin battle"), b -> {
            ConquestNetwork.orderGarrison(attacker.id(), GrandStrategy.BATTLE, defender.id(), 0, 0);
            parent.removed(); // the map under this menu is left for good: its camera and marchers go
            minecraft.setScreen(null); // to the world: the server sends the player to the battle
        }).bounds(width / 2 - 50, height - 28, 100, 20).build());
    }

    @Override
    public GrandStrategyScreen map() {
        return parent;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        parent.renderUnder(g, width, height, partialTick);
        int pw = panelW();
        List<Component> tip = side(g, 6, attacker, attackerColour, pw, mouseX, mouseY);
        List<Component> other = side(g, width - 6 - pw, defender, defenderColour, pw, mouseX, mouseY);
        // The deployment box, bottom middle.
        int bx = (width - BOX_W) / 2;
        int by = height - BOX_H - 4;
        g.fill(bx, by, bx + BOX_W, by + BOX_H, BACK);
        g.renderOutline(bx, by, BOX_W, BOX_H, EDGE);
        g.drawCenteredString(font, title, width / 2, by + 6, GOLD);
        g.drawCenteredString(font, "You are attacking " + nation(defender) + "!", width / 2, by + 20, TEXT);
        g.drawCenteredString(font, "Esc: retreat", width / 2, by + 32, GREY);
        for (var widget : renderables) { // the widgets only: Screen.render would paint the background over the panels
            widget.render(g, mouseX, mouseY, partialTick);
        }
        if (tip == null) {
            tip = other;
        }
        if (tip != null) {
            g.renderComponentTooltip(font, tip, mouseX, mouseY);
        }
    }

    /** One side's panel: nation bar, general, its cards in rows, its soldiers; the hovered card's tooltip, or null. */
    private List<Component> side(GuiGraphics g, int x, GrandStrategy.Army a, int colour, int w, int mouseX, int mouseY) {
        int top = 6;
        int bottom = height - 6;
        g.fill(x, top, x + w, bottom, BACK);
        g.renderOutline(x, top, w, bottom - top, EDGE);
        g.fill(x + 1, top + 1, x + w - 1, top + 15, colour);
        g.drawCenteredString(font, nation(a), x + w / 2, top + 4, TEXT);
        g.drawCenteredString(font, general(a), x + w / 2, top + 20, GOLD);
        int cols = Math.max(1, (w - 8) / (CityPanel.CARD_W + 3));
        int x0 = x + (w - cols * (CityPanel.CARD_W + 3) + 3) / 2;
        List<Component> tip = null;
        for (int i = 0; i < a.units().length; i++) {
            int cx = x0 + i % cols * (CityPanel.CARD_W + 3);
            int cy = top + 34 + i / cols * (CityPanel.CARD_H + 4);
            boolean over = mouseX >= cx && mouseX < cx + CityPanel.CARD_W && mouseY >= cy && mouseY < cy + CityPanel.CARD_H;
            CityPanel.card(g, font, cx, cy, a.units()[i], a.men()[i], over ? GOLD : 0xFFC8C8C8);
            if (over) {
                tip = CityPanel.tip(a.units()[i], a.men()[i], null);
            }
        }
        String men = a.strength();
        g.drawCenteredString(font, men, x + w / 2, bottom - 12, GREY);
        return tip;
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
