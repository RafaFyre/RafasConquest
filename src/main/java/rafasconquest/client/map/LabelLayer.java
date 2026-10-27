package rafasconquest.client.map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import rafasconquest.faction.FactionType;
import rafasconquest.net.FactionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Layer 6, the top: names and labels. Each faction's name sits at the middle of its territory, sized to fit it, and
 * double-clicking it opens that faction's Faction Info.
 */
public class LabelLayer implements MapLayer {
    private static final int TEXT = 0xFFF4EEDC;
    private static final int HOVER = 0xFFFFD75E;
    private static final int OUTLINE = 0xFF151515;
    /** Smallest a name is drawn; a City-state's territory smaller than this on screen gets no name. */
    private static final float MIN_SCALE = 0.6F;
    /** Largest an Empire's name is drawn, however big its land: names mark territory, they shouldn't cover it. */
    private static final float MAX_EMPIRE_SCALE = 1.2F;
    private static final float MAX_CITY_STATE_SCALE = 1.0F;

    private final Consumer<UUID> openFaction;
    private final List<Hit> hits = new ArrayList<>();

    private record Hit(UUID faction, float x0, float y0, float x1, float y1) {
        boolean contains(double x, double y) {
            return x >= x0 && x < x1 && y >= y0 && y < y1;
        }
    }

    /** {@code openFaction} opens Faction Info on a faction. */
    public LabelLayer(Consumer<UUID> openFaction) {
        this.openFaction = openFaction;
    }

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        hits.clear();
        for (FactionInfo f : map.factions) {
            MapContext.Territory t = map.territory(f.id());
            if (t == null) {
                continue;
            }
            boolean empire = f.type() == FactionType.EMPIRE;
            Component name = Component.literal(empire ? f.name().toUpperCase(Locale.ROOT) : f.name());
            int width = font.width(name);
            // The territory's width on screen, as if it were round; names fill about half of it.
            double across = 2 * Math.sqrt(t.pixels() / Math.PI) * map.base.blocksPerPixel() * view.scale;
            float scale = (float) (across * (empire ? 0.55 : 0.8) / width);
            if (scale < MIN_SCALE && !empire) {
                continue;
            }
            scale = Mth.clamp(scale, MIN_SCALE, empire ? MAX_EMPIRE_SCALE : MAX_CITY_STATE_SCALE);
            float x = (float) view.screenX(t.centerX());
            float y = (float) view.screenY(t.centerZ());
            float w = width * scale;
            float h = font.lineHeight * scale;
            // City-state names sit under their city's icon.
            float top = empire ? y - h / 2 : y + 9;
            Hit hit = new Hit(f.id(), x - w / 2, top, x + w / 2, top + h);
            if (!view.near(x, top, w)) {
                continue;
            }
            hits.add(hit);
            boolean hover = hit.contains(mouseX, mouseY);
            g.pose().pushPose();
            g.pose().translate(hit.x0(), top, 0);
            g.pose().scale(scale, scale, 1);
            FormattedCharSequence text = name.getVisualOrderText();
            font.drawInBatch8xOutline(text, 0, 0, hover ? HOVER : TEXT, OUTLINE, g.pose().last().pose(), g.bufferSource(), 0xF000F0);
            g.flush();
            g.pose().popPose();
        }
    }

    @Override
    public boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        // Topmost (last drawn) name first.
        for (int i = hits.size() - 1; i >= 0; i--) {
            Hit hit = hits.get(i);
            if (hit.contains(mouseX, mouseY)) {
                if (doubleClick && button == 0) {
                    openFaction.accept(hit.faction());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(new LegendEntry((g, x, y) -> {
            Font font = Minecraft.getInstance().font;
            g.drawString(font, "Aa", x, y + 1, TEXT, true);
        }, Component.translatable("gui.rafasconquest.map.legend.name")));
    }
}
