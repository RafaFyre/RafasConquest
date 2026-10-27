package rafasconquest.client.map;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Layer 2: each faction's territory as a see-through glob in its banner's main colour, drawn over the terrain rather
 * than mixed into it. Empires hold what they would own a Farm in; City-states hold a small circle round their city.
 */
public class TerritoryLayer implements MapLayer {
    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BaseMapLayer.drawAligned(g, view, map, MapContext.TERRITORY_TEXTURE);
        RenderSystem.disableBlend();
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(new LegendEntry((g, x, y) -> {
            g.fill(x, y, x + 10, y + 10, 0xFF88B85C);
            g.fill(x, y, x + 10, y + 10, 0x66B02E26);
            g.renderOutline(x, y, 10, 10, 0xDDB02E26);
        }, Component.translatable("gui.rafasconquest.map.legend.territory")));
    }
}
