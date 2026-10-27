package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * One layer of the world map. {@link FactionMapScreen#LAYERS} stacks them bottom to top; each draws only its own kind
 * of thing, so a layer can be changed, reordered or replaced on its own.
 */
public interface MapLayer {
    /** Draws this layer. Drawing is already clipped to the viewport. */
    void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY);

    /** What this layer adds to the legend, bottom of the list first. */
    default List<LegendEntry> legend() {
        return List.of();
    }

    /** A click on the map; layers are asked top first. Returns true if this layer used it. */
    default boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        return false;
    }

    /** A row of the legend: a small symbol, drawn in a 10 × 10 box at (x, y), and its meaning. */
    record LegendEntry(Symbol symbol, Component label) {
    }

    @FunctionalInterface
    interface Symbol {
        void draw(GuiGraphics g, int x, int y);
    }
}
