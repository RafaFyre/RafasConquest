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
    /** The cards the muster shop offers, by ordinal: the general roster (no siege train), as the server numbers them. */
    private static int[] shop() {
        return java.util.Arrays.stream(BattlePlan.Arm.values()).filter(BattlePlan.Arm::roster).mapToInt(BattlePlan.Arm::ordinal).toArray();
    }

    /** A card's icon, as its file names it (an item id); a sword if there is none. */
    private static ItemStack icon(int arm) {
        BattlePlan.Arm a = BattlePlan.Arm.get(arm);
        net.minecraft.resources.ResourceLocation id = a == null ? null : net.minecraft.resources.ResourceLocation.tryParse(a.icon());
        Item item = id == null ? null : net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(id).orElse(null);
        return new ItemStack(item == null || item == Items.AIR ? Items.IRON_SWORD : item);
    }
    private static final int BACK = 0xF0201D18;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int GOLD = 0xFFE0C060;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int RED = 0xFFD04030;
    private static final int TAB_H = 14;
    private static final int BUTTON_W = 50;
    static final int CARD_W = 22;
    static final int CARD_H = 34;
    private static final int H = TAB_H + CARD_H + TAB_H + 16; // 4 for the cards' scrollbar
    private static final int SHOP_H = CARD_H + 26; // 4 for the cards' scrollbar
    private static final long SLIDE_MS = 200;

    private int tab = 1;
    /** An army in the field, picked on the map: no tabs, and only Muster and March. */
    private boolean field;
    Mode mode = Mode.NONE;
    private long openedAt = -1;
    private int left;
    private int right;
    private int top;
    /** How far each row of cards is scrolled, in pixels: the army's, the muster shop's. */
    private final double[] scroll = new double[2];
    /** The row whose scrollbar is being dragged, or -1. */
    private int dragging = -1;

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

    /** A spawned army may dismiss in the field; the hold's only in its city. */
    private Mode[] buttons(GrandStrategy.Army army) {
        return field && army.id() == GrandStrategy.HOLD ? new Mode[]{Mode.MUSTER, Mode.MARCH} : new Mode[]{Mode.MUSTER, Mode.DISMISS, Mode.MARCH};
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

    /** How far a row of {@code n} cards runs past the box, in pixels (0: they fit). */
    private int overflow(int n) {
        return Math.max(0, n * (CARD_W + 3) - 3 - (right - left - 12));
    }

    private int thumb(int over) {
        int span = right - left - 12;
        return Math.max(12, span * span / (span + over));
    }

    /** Row {@code row}'s cards (0: the army's, 1: the muster shop's): centred when they fit, else side by side from its scroll. */
    private int[] rowXs(int n, int row) {
        int over = overflow(n);
        scroll[row] = Math.max(0, Math.min(over, scroll[row]));
        if (over == 0) {
            return cardXs(n);
        }
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = left + 6 - (int) scroll[row] + i * (CARD_W + 3);
        }
        return xs;
    }

    /** The top of row {@code row}'s cards. */
    private int rowY(int row) {
        return row == 0 ? top + TAB_H + 4 : top - SHOP_H - 2 + 16;
    }

    private static int count(int row, GrandStrategy.Army army) {
        return row == 0 ? army.units().length : shop().length;
    }

    /** {@link #cardAt} for a row, only within the box's clip. */
    private int rowAt(int[] xs, int y, double mx, double my) {
        return mx >= left + 5 && mx < right - 5 ? cardAt(xs, y, mx, my) : -1;
    }

    /** Draws row {@code row}'s scrollbar under its cards, if they overflow. */
    private void scrollbar(GuiGraphics g, int row, int n, int mouseX, int mouseY) {
        int over = overflow(n);
        if (over == 0) {
            return;
        }
        int span = right - left - 12;
        int thumb = thumb(over);
        int tx = left + 6 + (int) ((span - thumb) * scroll[row] / over);
        int ty = rowY(row) + CARD_H + 3;
        g.fill(left + 6, ty, right - 6, ty + 3, 0xFF2A2620);
        g.fill(tx, ty, tx + thumb, ty + 3, dragging == row || mouseY >= ty - 2 && mouseY < ty + 5 && mouseX >= tx && mouseX < tx + thumb ? GOLD : EDGE);
    }

    /** The mouse wheel over the panel scrolls the row it is over: the shop above, the army's below (true: taken). */
    boolean scrolled(double mx, double my, double delta, int width, int height) {
        if (!over(mx, my, width, height)) {
            return false;
        }
        scroll[my < top ? 1 : 0] -= delta * (CARD_W + 3); // clamped on the next render
        return true;
    }

    /** A drag on a scrollbar (begun by a click on it) moves its row. */
    boolean dragged(double mx, int width, int height, GrandStrategy.Army army) {
        if (dragging < 0 || !shown()) {
            return false;
        }
        layout(width, height);
        int over = overflow(count(dragging, army));
        int span = right - left - 12;
        int thumb = thumb(over);
        scroll[dragging] = over == 0 ? 0 : Math.max(0, Math.min(over, (mx - left - 6 - thumb / 2.0) / (span - thumb) * over));
        return true;
    }

    void released() {
        dragging = -1;
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

    void render(GuiGraphics g, Font font, int width, int height, GrandStrategy.Army army, int mouseX, int mouseY) {
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
            g.drawString(font, army.general().isEmpty() ? "Army in the field" : "Army of " + army.general(), left + 6, top + 5, GOLD, false);
        }
        int body = top + TAB_H + 4;
        List<Component> tip = null;
        if (!field && tab != 1) {
            g.drawCenteredString(font, tab == 0 ? "City upgrades: not built yet" : "Intrigue: not built yet", (left + right) / 2, body + CARD_H / 2, 0xFFA8A49C);
        } else {
            int[] units = army.units();
            int[] xs = rowXs(units.length, 0);
            int hovered = rowAt(xs, body, mouseX, mouseY);
            g.enableScissor(left + 5, body - 1, right - 5, body + CARD_H + 1);
            for (int i = 0; i < units.length; i++) {
                card(g, font, xs[i], body, units[i], army.men()[i], i == hovered ? (mode == Mode.DISMISS ? RED : GOLD) : 0xFFC8C8C8);
            }
            g.disableScissor();
            scrollbar(g, 0, units.length, mouseX, mouseY);
            if (hovered >= 0) {
                tip = tip(units[hovered], army.men()[hovered], mode == Mode.DISMISS ? "Click: dismiss" : null);
            }
            String count = units.length + " units, " + String.format("%,d", army.soldiers()) + " soldiers";
            g.drawString(font, count, right - 6 - font.width(count), top + 5, 0xFFA8A49C, false);
            int by = height - TAB_H - 4;
            Mode[] buttons = buttons(army);
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
                int[] shop = rowXs(shop().length, 1);
                int over = rowAt(shop, shopTop + 16, mouseX, mouseY);
                g.enableScissor(left + 5, shopTop + 15, right - 5, shopTop + 17 + CARD_H);
                for (int i = 0; i < shop().length; i++) {
                    card(g, font, shop[i], shopTop + 16, shop()[i], Garrison.MEN, i == over ? GOLD : 0xFFC8C8C8);
                }
                g.disableScissor();
                scrollbar(g, 1, shop().length, mouseX, mouseY);
                if (over >= 0) {
                    tip = tip(shop()[over], Garrison.MEN, "Click: muster");
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

    static List<Component> tip(int arm, int soldiers, @Nullable String click) {
        BattlePlan.Arm a = BattlePlan.Arm.get(arm);
        Component name = Component.literal(a == null ? "?" : a.title());
        Component men = Component.literal(soldiers + (soldiers < Garrison.MEN ? " / " + Garrison.MEN : "") + " soldiers").withColor(0xA8A49C);
        return click == null ? List.of(name, men) : List.of(name, men, Component.literal(click).withColor(0xE0C060));
    }

    /** A unit card: its arm's icon and name, and its soldiers, on the battle screen's card colours. */
    static void card(GuiGraphics g, Font font, int x, int y, int arm, int soldiers, int edge) {
        g.fill(x - 1, y - 1, x + CARD_W + 1, y + CARD_H + 1, edge);
        g.fill(x, y, x + CARD_W, y + CARD_H, 0xFFFFF2B0);
        g.renderItem(icon(arm), x + 3, y + 4);
        g.fill(x, y + CARD_H - 10, x + CARD_W, y + CARD_H - 9, 0xFFE0D090);
        String men = String.valueOf(soldiers);
        g.pose().pushPose();
        g.pose().translate(x + CARD_W / 2F, y + CARD_H - 7, 0);
        g.pose().scale(0.6F, 0.6F, 1);
        g.drawString(font, men, -font.width(men) / 2, 0, 0xFF3A2A10, false);
        g.pose().popPose();
    }

    /** A left click in the panel (true: taken). */
    boolean click(double mx, double my, int width, int height, GrandStrategy.Army army) {
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
        Mode[] buttons = buttons(army);
        for (int i = 0; i < buttons.length; i++) {
            int x = buttonX(i, buttons.length);
            if (mx >= x && mx < x + BUTTON_W && my >= by && my < by + TAB_H) {
                Mode m = buttons[i];
                mode = mode == m ? Mode.NONE : m;
                return true;
            }
        }
        for (int row = 0; row < (mode == Mode.MUSTER ? 2 : 1); row++) {
            int ty = rowY(row) + CARD_H + 3;
            if (overflow(count(row, army)) > 0 && my >= ty - 2 && my < ty + 5 && mx >= left + 6 && mx < right - 6) {
                dragging = row;
                dragged(mx, width, height, army);
                return true;
            }
        }
        if (mode == Mode.MUSTER) {
            int shop = rowAt(rowXs(shop().length, 1), rowY(1), mx, my);
            if (shop >= 0) {
                ConquestNetwork.orderGarrison(army.id(), GrandStrategy.MUSTER, shop()[shop], 0, 0);
            }
        } else if (mode == Mode.DISMISS) {
            int card = rowAt(rowXs(army.units().length, 0), top + TAB_H + 4, mx, my);
            if (card >= 0) {
                ConquestNetwork.orderGarrison(army.id(), GrandStrategy.DISMISS, card, 0, 0);
            }
        }
        return true;
    }
}
