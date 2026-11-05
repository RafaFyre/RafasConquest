package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import rafasconquest.core.Roads;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 3: the roads ({@link Roads}), under every mark and icon, each a dotted black line along its points: the high
 * roads thickest, the city roads less so, the town and dojo roads finest. Drawn once zoomed in enough to tell them from
 * the land; the high roads from further out.
 */
public class RoadLayer implements MapLayer {
    private static final int INK = 0xFF141210;
    /** Shipping routes: navy, darker than the sea and clear on the legend. */
    private static final int SEA_INK = 0xFF0B2A55;
    /** Each dot this many screen pixels long, then this many left blank. */
    private static final int DOT = 2;
    private static final int GAP = 2;
    /** Below this zoom (screen pixels per block) only high roads are drawn, and below the other none. */
    private static final double ALL_FROM = 0.03;
    private static final double HIGH_FROM = 0.012;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        if (view.scale < HIGH_FROM) {
            return;
        }
        List<Roads.Road> all = new ArrayList<>(map.base.roads().roads());
        all.addAll(map.base.roads().shipping());
        for (Roads.Road road : all) {
            if (view.scale < ALL_FROM && road.grade() != Roads.Grade.HIGH && road.grade() != Roads.Grade.SEA) {
                continue;
            }
            int thick = thickness(road.grade());
            int ink = ink(road.grade());
            // The dashes run on from one stretch to the next, so the dots are evenly spaced along the whole road.
            double run = 0;
            for (int i = 1; i < road.points(); i++) {
                double x0 = view.screenX(road.xs()[i - 1]);
                double y0 = view.screenY(road.zs()[i - 1]);
                double x1 = view.screenX(road.xs()[i]);
                double y1 = view.screenY(road.zs()[i]);
                double len = Math.hypot(x1 - x0, y1 - y0);
                if (view.near(x0, y0, 4) || view.near(x1, y1, 4)) {
                    dotted(g, x0, y0, x1, y1, len, run, thick, ink);
                }
                run += len;
            }
        }
    }

    private static int thickness(Roads.Grade grade) {
        return switch (grade) {
            case HIGH -> 3;
            case CITY, SEA -> 2;
            default -> 1;
        };
    }

    private static int ink(Roads.Grade grade) {
        return grade == Roads.Grade.SEA ? SEA_INK : INK;
    }

    /** A dotted line, its pattern begun {@code run} pixels in. */
    private static void dotted(GuiGraphics g, double x0, double y0, double x1, double y1, double len, double run, int thick, int ink) {
        int steps = Math.max(1, (int) Math.ceil(len));
        int half = thick / 2;
        for (int s = 0; s <= steps; s++) {
            if ((int) (run + s) % (DOT + GAP) >= DOT) {
                continue;
            }
            int x = (int) Math.round(x0 + (x1 - x0) * s / steps);
            int y = (int) Math.round(y0 + (y1 - y0) * s / steps);
            g.fill(x - half, y - half, x - half + thick, y - half + thick, ink);
        }
    }

    @Override
    public List<LegendEntry> legend() {
        List<LegendEntry> entries = new ArrayList<>();
        String[] keys = {"high_road", "city_road", "town_road", "shipping_route"};
        Roads.Grade[] grades = {Roads.Grade.HIGH, Roads.Grade.CITY, Roads.Grade.TOWN, Roads.Grade.SEA};
        for (int i = 0; i < keys.length; i++) {
            int thick = thickness(grades[i]);
            int ink = ink(grades[i]);
            entries.add(new LegendEntry((g, x, y) -> {
                for (int d = 0; d < 10; d += DOT + GAP) {
                    g.fill(x + d, y + 5 - thick / 2, x + d + DOT, y + 5 - thick / 2 + thick, ink);
                }
            }, Component.translatable("gui.rafasconquest.map.legend." + keys[i])));
        }
        return entries;
    }
}
