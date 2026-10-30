package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import rafasconquest.core.Roads;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 4: the roads ({@link Roads}), each drawn along its points in its grade's colour, the high roads widest: grey
 * cobblestone high roads, pale gravel city roads, brown dirt town roads, and cobblestone dojo roads. Drawn once zoomed
 * in enough to tell them from the land; the high roads from further out.
 */
public class RoadLayer implements MapLayer {
    private static final int[] COLOR = {0xFF8C6A44, 0xFFC8B48A, 0xFF8E8E8E, 0xFF707070};
    /** Below this zoom (screen pixels per block) only high roads are drawn, and below the other none. */
    private static final double ALL_FROM = 0.03;
    private static final double HIGH_FROM = 0.012;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        if (view.scale < HIGH_FROM) {
            return;
        }
        for (Roads.Road road : map.base.roads().roads()) {
            if (view.scale < ALL_FROM && road.grade() != Roads.Grade.HIGH) {
                continue;
            }
            int color = COLOR[road.grade().ordinal()];
            int thick = Math.max(1, (int) Math.round(road.grade().width() * view.scale * 1.5));
            if (road.grade() == Roads.Grade.HIGH) {
                thick = Math.max(2, thick);
            }
            for (int i = 1; i < road.points(); i++) {
                double x0 = view.screenX(road.xs()[i - 1]);
                double y0 = view.screenY(road.zs()[i - 1]);
                double x1 = view.screenX(road.xs()[i]);
                double y1 = view.screenY(road.zs()[i]);
                if (!view.near(x0, y0, 4) && !view.near(x1, y1, 4)) {
                    continue;
                }
                segment(g, x0, y0, x1, y1, thick, color);
            }
        }
    }

    /** A thick line, as squares a pixel apart. */
    private static void segment(GuiGraphics g, double x0, double y0, double x1, double y1, int thick, int color) {
        double len = Math.hypot(x1 - x0, y1 - y0);
        int steps = Math.max(1, (int) Math.ceil(len));
        int half = thick / 2;
        for (int s = 0; s <= steps; s++) {
            int x = (int) Math.round(x0 + (x1 - x0) * s / steps);
            int y = (int) Math.round(y0 + (y1 - y0) * s / steps);
            g.fill(x - half, y - half, x - half + thick, y - half + thick, color);
        }
    }

    @Override
    public List<LegendEntry> legend() {
        List<LegendEntry> entries = new ArrayList<>();
        String[] keys = {"high_road", "city_road", "town_road"};
        int[] grades = {Roads.Grade.HIGH.ordinal(), Roads.Grade.CITY.ordinal(), Roads.Grade.TOWN.ordinal()};
        for (int i = 0; i < keys.length; i++) {
            int color = COLOR[grades[i]];
            int thick = 3 - i;
            entries.add(new LegendEntry((g, x, y) -> g.fill(x, y + 5 - thick / 2, x + 10, y + 5 - thick / 2 + thick, color),
                    Component.translatable("gui.rafasconquest.map.legend." + keys[i])));
        }
        return entries;
    }
}
