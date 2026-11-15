package rafasconquest.client.map;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.Garrison;
import rafasconquest.peerage.GrandStrategy;

import java.util.List;

/**
 * The overlay rising from the bottom of the grand strategy map when the player picks a city they hold: tabs for City
 * (its upgrades, not built yet), Army and Intrigue (not built yet). Army shows the hold's unit cards, as the battle
 * screen does, over Muster (a card shop above the panel: a click adds that card), Dismiss (a click on a card removes it)
 * and March (the next click on the map sends the army there).
 */
final class CityPanel {
    enum Mode { NONE, MUSTER, DISMISS, MARCH }

    private static final String[] TABS = {"City", "Army", "Intrigue"};
    private static final String[] BUTTONS = {"", "Muster", "Dismiss", "March"};
    static final String[] NAMES = {"Levy", "Heavy infantry", "Skirmishers", "Zweihandlers", "Archers", "Cavalry", "Heavy cavalry",
            "Musketeers", "Siege train"};
    private static final Item[] ICONS = {Items.STONE_SWORD, Items.SHIELD, Items.IRON_SWORD, Items.NETHERITE_SWORD, Items.BOW,
            Items.SADDLE, Items.IRON_HORSE_ARMOR, Items.CROSSBOW, Items.DISPENSER};
    private static final int BACK = 0xF0201D18;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int GOLD = 0xFFE0C060;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int RED = 0xFFD04030;
    private static final int TAB_H = 14;
    private static final int BUTTON_W = 50;
    private static final int CARD_W = 22;
    private static final int CARD_H = 34;
    private static final int H = TAB_H + CARD_H + TAB_H + 12;
    private static final int SHOP_H = CARD_H + 22;
    private static final long SLIDE_MS = 200;

    private int tab = 1;
    /** An army in the field, picked on the map: no tabs, and only Muster and March. */
    private boolean field;
    Mode mode = Mode.NONE;
    private long openedAt = -1;
    private int left;
    private int right;
    private int top;

    void open(boolean field) {
        if (this.field != field) {
            mode = Mode.NONE; // a city's Dismiss is not the field army's
        }
        this.field = field;
        if (openedAt < 0) {
            openedAt = Util.getMillis();
        }
    }

    void close() {
        openedAt = -1;
        mode = Mode.NONE;
    }

    boolean shown() {
        return openedAt >= 0;
    }

    /** Lays the panel out for the screen: right of the HUD's bottom-left panel, risen as far as its slide has come. */
    private void layout(int width, int height) {
        left = 162;
        right = width - 4;
        double t = Math.min(1, (Util.getMillis() - openedAt) / (double) SLIDE_MS);
        top = height - (int) (H * (1 - (1 - t) * (1 - t)));
    }

    boolean over(double mx, double my, int width, int height) {
        if (!shown()) {
            return false;
        }
        layout(width, height);
        int shopTop = mode == Mode.MUSTER ? top - SHOP_H - 2 : top;
        return mx >= left && mx < right && my >= shopTop;
    }

    private Mode[] buttons() {
        return field ? new Mode[]{Mode.MUSTER, Mode.MARCH} : new Mode[]{Mode.MUSTER, Mode.DISMISS, Mode.MARCH};
    }

    private int buttonX(int i, int n) {
        return (left + right) / 2 - (BUTTON_W * n + 4 * (n - 1)) / 2 + i * (BUTTON_W + 4);
    }

    /** Each card's left edge in a row of {@code n}: side by side, or overlapping when they no longer fit. */
    private int[] cardXs(int n) {
        int span = right - left - 12;
        double step = n > 1 ? Math.min(CARD_W + 3, (span - CARD_W) / (double) (n - 1)) : 0;
        int x0 = left + 6 + (int) ((span - (step * (n - 1) + CARD_W)) / 2);
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = x0 + (int) (i * step);
        }
        return xs;
    }

    /** The card under the mouse in a row at {@code y}: the topmost (last drawn), or -1. */
    private int cardAt(int[] xs, int y, double mx, double my) {
        if (my < y || my >= y + CARD_H) {
            return -1;
        }
        for (int i = xs.length - 1; i >= 0; i--) {
            if (mx >= xs[i] && mx < xs[i] + CARD_W) {
                return i;
            }
        }
        return -1;
    }

    void render(GuiGraphics g, Font font, int width, int height, GrandStrategy.View fief, int mouseX, int mouseY) {
        if (!shown()) {
            return;
        }
        layout(width, height);
        g.fill(left, top, right, height, BACK);
        g.renderOutline(left, top, right - left, height - top + 1, EDGE);
        for (int i = 0; !field && i < TABS.length; i++) {
            int x = left + 4 + i * 54;
            boolean over = mouseX >= x && mouseX < x + 50 && mouseY >= top + 2 && mouseY < top + TAB_H;
            g.fill(x, top + 2, x + 50, top + TAB_H, i == tab ? EDGE : over ? 0xFF3A3228 : 0xFF2A2620);
            g.drawCenteredString(font, TABS[i], x + 25, top + 5, i == tab ? GOLD : TEXT);
        }
        if (field) {
            g.drawString(font, "Army in the field", left + 6, top + 5, GOLD, false);
        }
        int body = top + TAB_H + 4;
        List<Component> tip = null;
        if (!field && tab != 1) {
            g.drawCenteredString(font, tab == 0 ? "City upgrades: not built yet" : "Intrigue: not built yet", (left + right) / 2, body + CARD_H / 2, 0xFFA8A49C);
        } else {
            int[] army = fief.army();
            int[] xs = cardXs(army.length);
            int hovered = cardAt(xs, body, mouseX, mouseY);
            for (int i = 0; i < army.length; i++) {
                card(g, font, xs[i], body, army[i], i == hovered ? (mode == Mode.DISMISS ? RED : GOLD) : 0xFFC8C8C8);
            }
            if (hovered >= 0) {
                tip = tip(army[hovered], mode == Mode.DISMISS ? "Click: dismiss" : null);
            }
            String count = army.length + " units, " + String.format("%,d", army.length * Garrison.MEN) + " soldiers";
            g.drawString(font, count, right - 6 - font.width(count), top + 5, 0xFFA8A49C, false);
            int by = height - TAB_H - 4;
            Mode[] buttons = buttons();
            for (int i = 0; i < buttons.length; i++) {
                int x = buttonX(i, buttons.length);
                boolean on = mode == buttons[i];
                boolean over = mouseX >= x && mouseX < x + BUTTON_W && mouseY >= by && mouseY < by + TAB_H;
                g.fill(x - 1, by - 1, x + BUTTON_W + 1, by + TAB_H + 1, on ? GOLD : EDGE);
                g.fill(x, by, x + BUTTON_W, by + TAB_H, over ? 0xFF6A6A6A : 0xFF4A4A4A);
                g.drawCenteredString(font, BUTTONS[buttons[i].ordinal()], x + BUTTON_W / 2, by + 3, TEXT);
            }
            if (mode == Mode.MUSTER) {
                int shopTop = top - SHOP_H - 2;
                g.fill(left, shopTop, right, top - 2, BACK);
                g.renderOutline(left, shopTop, right - left, SHOP_H, EDGE);
                g.drawString(font, "Muster: click a card to raise " + Garrison.MEN + " soldiers", left + 6, shopTop + 4, GOLD, false);
                int[] shop = cardXs(NAMES.length);
                int over = cardAt(shop, shopTop + 16, mouseX, mouseY);
                for (int i = 0; i < NAMES.length; i++) {
                    card(g, font, shop[i], shopTop + 16, i, i == over ? GOLD : 0xFFC8C8C8);
                }
                if (over >= 0) {
                    tip = tip(over, "Click: muster");
                }
            } else if (mode == Mode.MARCH) {
                String hint = "Click the map to march there; right-click to stop";
                g.drawString(font, hint, mouseX + 10, mouseY - 4, GOLD, true);
            }
        }
        if (tip != null) {
            g.renderComponentTooltip(font, tip, mouseX, mouseY);
        }
    }

    private static List<Component> tip(int arm, @Nullable String click) {
        Component name = Component.literal(NAMES[arm]);
        Component men = Component.literal(Garrison.MEN + " soldiers").withColor(0xA8A49C);
        return click == null ? List.of(name, men) : List.of(name, men, Component.literal(click).withColor(0xE0C060));
    }

    /** A unit card: its arm's icon and name, and its soldiers, on the battle screen's card colours. */
    private static void card(GuiGraphics g, Font font, int x, int y, int arm, int edge) {
        g.fill(x - 1, y - 1, x + CARD_W + 1, y + CARD_H + 1, edge);
        g.fill(x, y, x + CARD_W, y + CARD_H, 0xFFFFF2B0);
        g.renderItem(new ItemStack(ICONS[Math.floorMod(arm, ICONS.length)]), x + 3, y + 4);
        g.fill(x, y + CARD_H - 10, x + CARD_W, y + CARD_H - 9, 0xFFE0D090);
        String men = String.valueOf(Garrison.MEN);
        g.pose().pushPose();
        g.pose().translate(x + CARD_W / 2F, y + CARD_H - 7, 0);
        g.pose().scale(0.6F, 0.6F, 1);
        g.drawString(font, men, -font.width(men) / 2, 0, 0xFF3A2A10, false);
        g.pose().popPose();
    }

    /** A left click in the panel (true: taken). */
    boolean click(double mx, double my, int width, int height, GrandStrategy.View fief) {
        if (!over(mx, my, width, height)) {
            return false;
        }
        for (int i = 0; !field && i < TABS.length; i++) {
            int x = left + 4 + i * 54;
            if (mx >= x && mx < x + 50 && my >= top + 2 && my < top + TAB_H) {
                tab = i;
                mode = Mode.NONE;
                return true;
            }
        }
        if (!field && tab != 1) {
            return true;
        }
        int by = height - TAB_H - 4;
        Mode[] buttons = buttons();
        for (int i = 0; i < buttons.length; i++) {
            int x = buttonX(i, buttons.length);
            if (mx >= x && mx < x + BUTTON_W && my >= by && my < by + TAB_H) {
                Mode m = buttons[i];
                mode = mode == m ? Mode.NONE : m;
                return true;
            }
        }
        if (mode == Mode.MUSTER) {
            int shop = cardAt(cardXs(NAMES.length), top - SHOP_H - 2 + 16, mx, my);
            if (shop >= 0 && shop < BattlePlan.Arm.values().length) {
                ConquestNetwork.orderGarrison(GrandStrategy.MUSTER, shop, 0, 0);
            }
        } else if (mode == Mode.DISMISS) {
            int card = cardAt(cardXs(fief.army().length), top + TAB_H + 4, mx, my);
            if (card >= 0) {
                ConquestNetwork.orderGarrison(GrandStrategy.DISMISS, card, 0, 0);
            }
        }
        return true;
    }
}
