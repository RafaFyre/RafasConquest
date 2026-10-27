package rafasconquest.client.map;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.client.FactionInfoScreen;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.net.FactionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The world map, in the style of a strategy game: the land inside the storm wall seen from above, with every faction's
 * territory, cities, farms, players and names drawn over it in {@link #layers}. Drag to pan, scroll to zoom, double-click
 * a faction's name for its Faction Info; the legend sits bottom left.
 */
public class FactionMapScreen extends Screen {
    private static final int FRAME = 0xFF0E0F11;
    private static final int TITLE_BAR = 20;
    private static final int MARGIN = 6;
    private static final int DOUBLE_CLICK_MS = 350;
    private static final int LEGEND_BACK = 0xD0101214;

    private final Screen parent;
    private final MapView view = new MapView();
    /**
     * The layers, bottom to top. Each draws one kind of thing over the ones before it; reorder, add or swap them here.
     */
    private final List<MapLayer> layers = List.of(
            new BaseMapLayer(),     // 1. terrain (from the server) and the storm wall
            new TerritoryLayer(),   // 2. see-through faction territory
            new PoiIconLayer(),     // 3. capitals, City-states, farms
            new RoadLayer(),        // 4. paths and roads (none yet)
            new UnitLayer(),        // 5. players, later armies
            new LabelLayer(this::openFaction)); // 6. faction names
    @Nullable
    private List<FactionInfo> factions;
    @Nullable
    private MapContext map;
    private boolean centred;
    private long lastClick;
    private double lastClickX;
    private double lastClickY;
    private boolean toFactionInfo;

    public FactionMapScreen(Screen parent) {
        super(Component.translatable("gui.rafasconquest.map"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        view.setViewport(MARGIN, TITLE_BAR, width - MARGIN, height - MARGIN);
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose())
                .bounds(width - MARGIN - 14, 4, 14, 14).build());
        if (factions == null) {
            ConquestNetwork.requestFactionInfo();
        }
        if (ClientMapCache.map() == null) {
            ConquestNetwork.requestBaseMap();
        }
        tryBuild();
    }

    /** Faction data from the server. */
    public void setFactions(List<FactionInfo> factions) {
        this.factions = factions;
        tryBuild();
    }

    /** The base map has arrived. */
    public void onBaseMap() {
        tryBuild();
    }

    private void tryBuild() {
        BaseMap base = ClientMapCache.map();
        if (map != null || base == null || factions == null) {
            return;
        }
        map = new MapContext(base, factions);
        double whole = 2.3 * base.visibleRadius();
        view.minScale = Math.min(view.width(), view.height()) / whole;
        if (!centred && minecraft != null && minecraft.player != null) {
            view.centerX = minecraft.player.getX();
            view.centerZ = minecraft.player.getZ();
            view.scale = Math.max(view.minScale, view.width() / 3000.0);
            centred = true;
        }
    }

    private void openFaction(UUID faction) {
        if (minecraft != null) {
            toFactionInfo = true;
            minecraft.setScreen(new FactionInfoScreen(this, faction));
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, FRAME);
        g.drawString(font, title, MARGIN + 2, 6, 0xF4EEDC, false);
        if (map == null) {
            Component waiting = Component.translatable(ClientMapCache.map() == null
                    ? "gui.rafasconquest.map.drawing" : "gui.rafasconquest.faction_info.loading");
            g.fill(view.left, view.top, view.right, view.bottom, BaseMapLayer.UNEXPLORED);
            g.drawCenteredString(font, waiting, width / 2, height / 2, 0xF4EEDC);
        } else {
            g.enableScissor(view.left, view.top, view.right, view.bottom);
            for (MapLayer layer : layers) {
                layer.render(g, view, map, mouseX, mouseY);
            }
            g.disableScissor();
            renderLegend(g);
            if (view.contains(mouseX, mouseY)) {
                String at = String.format("%,d, %,d", Math.round(view.worldX(mouseX)), Math.round(view.worldZ(mouseY)));
                g.drawString(font, at, view.right - font.width(at) - 4, view.bottom - 12, 0xF4EEDC, true);
            }
        }
        g.renderOutline(view.left - 1, view.top - 1, view.width() + 2, view.height() + 2, 0xFF5A5A5A);
        super.render(g, mouseX, mouseY, partialTick);
    }

    /** The legend, bottom left: every layer's entries, top layer first. */
    private void renderLegend(GuiGraphics g) {
        List<MapLayer.LegendEntry> entries = new ArrayList<>();
        for (int i = layers.size() - 1; i >= 0; i--) {
            entries.addAll(layers.get(i).legend());
        }
        int rowHeight = 13;
        int w = 0;
        for (MapLayer.LegendEntry e : entries) {
            w = Math.max(w, font.width(e.label()));
        }
        int boxW = w + 26;
        int boxH = entries.size() * rowHeight + 18;
        int x = view.left + 6;
        int y = view.bottom - 6 - boxH;
        g.fill(x, y, x + boxW, y + boxH, LEGEND_BACK);
        g.renderOutline(x, y, boxW, boxH, 0xFF5A5A5A);
        g.drawString(font, Component.translatable("gui.rafasconquest.map.legend"), x + 6, y + 5, 0xFFD75E, false);
        int row = y + 17;
        for (MapLayer.LegendEntry e : entries) {
            e.symbol().draw(g, x + 6, row);
            g.drawString(font, e.label(), x + 20, row + 1, 0xE0E0E0, false);
            row += rowHeight;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (map == null || !view.contains(mouseX, mouseY)) {
            return false;
        }
        long now = Util.getMillis();
        boolean doubleClick = now - lastClick < DOUBLE_CLICK_MS && Math.abs(mouseX - lastClickX) < 4 && Math.abs(mouseY - lastClickY) < 4;
        lastClick = doubleClick ? 0 : now;
        lastClickX = mouseX;
        lastClickY = mouseY;
        for (int i = layers.size() - 1; i >= 0; i--) {
            if (layers.get(i).mouseClicked(view, map, mouseX, mouseY, button, doubleClick)) {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (map != null && button == 0) {
            view.pan(dx, dy);
            lastClick = 0; // a drag is not half of a double-click
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (map != null && view.contains(mouseX, mouseY)) {
            view.zoomAt(mouseX, mouseY, Math.pow(1.25, delta));
            return true;
        }
        return false;
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public void removed() {
        // Faction Info opened from a name comes back to this screen, so only let go of the textures when leaving the map.
        if (!toFactionInfo && map != null) {
            map.close();
            map = null;
        }
        toFactionInfo = false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
