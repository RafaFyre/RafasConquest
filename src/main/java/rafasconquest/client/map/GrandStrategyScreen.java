package rafasconquest.client.map;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rafasconquest.client.CityManagerScreen;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.peerage.GrandStrategy;

import java.util.List;

/**
 * The grand strategy screen, opened from the Grand Strategy table: the world map, centred on the player's seat, with a
 * panel on the right for their fief (the titles they hold, their liege, vassals and retinue). The cities they own are
 * ringed in gold: a click shows a city's summary; a double-click, or Manage city in its right-click menu, opens its
 * {@link CityManagerScreen city manager}.
 */
public class GrandStrategyScreen extends FactionMapScreen {
    private static final int PANEL_W = 170;
    private static final int PANEL_BACK = 0xD0101214;
    private static final int GOLD = 0xFFE0C060;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int HOVER = 0xFF3A3228;
    private static final int ROW = 12;
    /** How near (in screen pixels) a click must be to an owned city to pick it. */
    private static final double REACH = 8;
    private static final int DOUBLE_CLICK_MS = 350;
    private static final String MANAGE = "Manage city";

    private final GrandStrategy.View fief;
    @Nullable
    private SeatCities.City summary;
    @Nullable
    private SeatCities.City menu;
    private int menuX;
    private int menuY;
    @Nullable
    private SeatCities.City lastClicked;
    private long lastClick;

    public GrandStrategyScreen(GrandStrategy.View fief) {
        super(null, Component.translatable("gui.rafasconquest.grand_strategy"));
        this.fief = fief;
    }

    @Override
    protected void centre(BaseMap base) {
        if (fief.seat() >= 0 && fief.seat() < base.centres().size()) {
            ProvinceMap.Centre seat = base.centres().get(fief.seat());
            view.centerX = seat.x();
            view.centerZ = seat.z();
        } else {
            super.centre(base);
        }
    }

    /** The cities the player owns: the one seating the county they hold. */
    private List<SeatCities.City> owned() {
        BaseMap base = ClientMapCache.map();
        return base == null || fief.seat() < 0 ? List.of() : base.cities().stream().filter(c -> c.province() == fief.seat()).toList();
    }

    private static String name(SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        return base == null ? "City" : base.names().get(city.province());
    }

    @Nullable
    private SeatCities.City ownedAt(double mouseX, double mouseY) {
        for (SeatCities.City c : owned()) {
            if (Math.hypot(view.screenX(c.x()) - mouseX, view.screenY(c.z()) - mouseY) < REACH) {
                return c;
            }
        }
        return null;
    }

    private int panelLeft() {
        return view.right - PANEL_W - 6;
    }

    private boolean overPanel(double x, double y) {
        return x >= panelLeft() && x < view.right - 6 && y >= view.top + 6 && y < view.bottom - 6;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        g.enableScissor(view.left, view.top, view.right, view.bottom);
        for (SeatCities.City c : owned()) {
            int x = (int) view.screenX(c.x());
            int y = (int) view.screenY(c.z());
            g.renderOutline(x - 7, y - 7, 14, 14, GOLD);
        }
        g.disableScissor();
        renderPanel(g);
        g.pose().pushPose();
        g.pose().translate(0, 0, 400); // over every mark and tooltip
        if (summary != null) {
            renderSummary(g, summary);
        }
        if (menu != null) {
            renderMenu(g, mouseX, mouseY);
        }
        g.pose().popPose();
    }

    private void renderPanel(GuiGraphics g) {
        int x = panelLeft();
        int y = view.top + 6;
        int text = PANEL_W - 12;
        int h = 18;
        for (String[] row : fief.rows()) {
            h += 10 + font.split(Component.literal(row[1]), text).size() * 10 + 4;
        }
        g.fill(x, y, x + PANEL_W, y + h, PANEL_BACK);
        g.renderOutline(x, y, PANEL_W, h, 0xFF5A5A5A);
        g.drawString(font, Component.translatable("gui.rafasconquest.grand_strategy.fief"), x + 6, y + 5, 0xFFD75E, false);
        int row = y + 18;
        for (String[] r : fief.rows()) {
            g.drawString(font, r[0], x + 6, row, 0xB0B0B0, false);
            row += 10;
            for (FormattedCharSequence line : font.split(Component.literal(r[1]), text)) {
                g.drawString(font, line, x + 6, row, 0xF4EEDC, false);
                row += 10;
            }
            row += 4;
        }
    }

    /** A city's summary, beside it on the map. */
    private void renderSummary(GuiGraphics g, SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        List<String> lines = List.of(
                base == null ? "" : "Seat of the " + base.titleName(city.tier(), city.province()),
                city.districts() + " × " + city.districts() + " districts, " + city.size() + " blocks a side",
                String.format("At %,d, %,d", city.x(), city.z()),
                "Double-click or right-click to manage");
        int w = font.width(name(city));
        for (String l : lines) {
            w = Math.max(w, font.width(l));
        }
        w += 12;
        int h = ROW * (lines.size() + 1) + 6;
        int x = (int) view.screenX(city.x()) + 10;
        int y = (int) view.screenY(city.z()) + 10;
        g.fill(x, y, x + w, y + h, PANEL_BACK);
        g.renderOutline(x, y, w, h, EDGE);
        g.drawString(font, name(city), x + 6, y + 4, GOLD, false);
        int row = y + 4 + ROW;
        for (String l : lines) {
            g.drawString(font, l, x + 6, row, 0xF4EEDC, false);
            row += ROW;
        }
    }

    private int menuWidth() {
        return Math.max(font.width(name(menu)), font.width(MANAGE)) + 12;
    }

    private boolean inManageRow(double mouseX, double mouseY) {
        int rowY = menuY + 4 + ROW;
        return mouseX >= menuX && mouseX < menuX + menuWidth() && mouseY >= rowY - 2 && mouseY < rowY + ROW - 2;
    }

    private void renderMenu(GuiGraphics g, int mouseX, int mouseY) {
        int w = menuWidth();
        g.fill(menuX, menuY, menuX + w, menuY + ROW * 2 + 6, 0xF0201D18);
        g.renderOutline(menuX, menuY, w, ROW * 2 + 6, EDGE);
        g.drawString(font, name(menu), menuX + 6, menuY + 4, GOLD, false);
        int rowY = menuY + 4 + ROW;
        if (inManageRow(mouseX, mouseY)) {
            g.fill(menuX + 1, rowY - 2, menuX + w - 1, rowY + ROW - 2, HOVER);
        }
        g.drawString(font, MANAGE, menuX + 6, rowY, 0xFFF4EEDC, false);
    }

    /** Opens a city's manager; only from the overworld, where the cities are. */
    private void manage(SeatCities.City city) {
        if (minecraft != null && minecraft.level != null && minecraft.level.dimension() == Level.OVERWORLD) {
            minecraft.setScreen(new CityManagerScreen(city, name(city)));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (menu != null) { // any click closes the menu; one on its row also opens the manager
            SeatCities.City city = menu;
            boolean onRow = button == 0 && inManageRow(mouseX, mouseY);
            menu = null;
            if (onRow) {
                manage(city);
            }
            return true;
        }
        if (overPanel(mouseX, mouseY)) {
            return true;
        }
        SeatCities.City city = ownedAt(mouseX, mouseY);
        if (city == null) {
            summary = null;
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (button == 1) {
            menu = city;
            menuX = (int) mouseX + 4;
            menuY = (int) mouseY + 4;
        } else if (button == 0) {
            long now = Util.getMillis();
            if (city == lastClicked && now - lastClick < DOUBLE_CLICK_MS) {
                manage(city);
            } else {
                summary = city;
            }
            lastClicked = city;
            lastClick = now;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return overPanel(mouseX, mouseY) || super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        return overPanel(mouseX, mouseY) || super.mouseScrolled(mouseX, mouseY, scrollX, delta);
    }
}
