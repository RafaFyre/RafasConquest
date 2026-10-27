package rafasconquest.client.map;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import rafasconquest.map.PoliticalStyle;

import java.util.List;

/**
 * Layer 2: the realms, drawn over the parchment rather than mixed into it ({@link PoliticalStyle#territory}). The land
 * is split into provinces; each realm fills the provinces it holds in its own colour, with thin lines between them, a
 * dark line round the realm and a shadow inside it. The storm wall is drawn on top, so it shows through.
 */
public class TerritoryLayer implements MapLayer {
    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BaseMapLayer.drawAligned(g, view, map, MapContext.TERRITORY_TEXTURE);
        RenderSystem.disableBlend();
        BaseMapLayer.drawStormWall(g, view, map);
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(
                new LegendEntry((g, x, y) -> {
                    g.fill(x, y, x + 10, y + 10, 0xFFB98C8C);
                    g.fill(x + 5, y, x + 6, y + 10, 0xFF8F6A6A);
                    g.renderOutline(x, y, 10, 10, 0xFF2B2420);
                }, Component.translatable("gui.rafasconquest.map.legend.territory")),
                new LegendEntry((g, x, y) -> {
                    g.fill(x, y, x + 10, y + 10, 0xFF000000 | PoliticalStyle.PARCHMENT_LAND);
                    g.fill(x + 5, y, x + 6, y + 10, 0xFFA89A7C);
                }, Component.translatable("gui.rafasconquest.map.legend.unclaimed")));
    }
}
