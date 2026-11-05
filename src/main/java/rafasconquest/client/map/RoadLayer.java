package rafasconquest.client.map;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import rafasconquest.core.Roads;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 3: the roads ({@link Roads}), under every mark and icon, each a plain black line along its points: the high
 * roads thickest, the city roads less so, the town and dojo roads finest. Drawn once zoomed in enough to tell them from
 * the land; the high roads from further out.
 */
public class RoadLayer implements MapLayer {
    private static final int INK = 0xFF141210;
    /** Shipping routes: navy, darker than the sea and clear on the legend. */
    private static final int SEA_INK = 0xFF0B2A55;
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
        // Every road's stretches go into one buffer and are drawn in a single call.
        Matrix4f pose = g.pose().last().pose();
        VertexConsumer buffer = g.bufferSource().getBuffer(RenderType.gui());
        for (Roads.Road road : all) {
            if (view.scale < ALL_FROM && road.grade() != Roads.Grade.HIGH && road.grade() != Roads.Grade.SEA) {
                continue;
            }
            float half = thickness(road.grade()) / 2f;
            int ink = ink(road.grade());
            for (int i = 1; i < road.points(); i++) {
                float x0 = (float) view.screenX(road.xs()[i - 1]);
                float y0 = (float) view.screenY(road.zs()[i - 1]);
                float x1 = (float) view.screenX(road.xs()[i]);
                float y1 = (float) view.screenY(road.zs()[i]);
                if (!view.near(x0, y0, 4) && !view.near(x1, y1, 4)) {
                    continue;
                }
                float len = (float) Math.hypot(x1 - x0, y1 - y0);
                if (len < 0.01f) {
                    continue;
                }
                // Half the width out to each side, turned so the corners wind the way the GUI's own fills do.
                float nx = (y1 - y0) / len * half;
                float ny = -(x1 - x0) / len * half;
                buffer.addVertex(pose, x0 + nx, y0 + ny, 0).setColor(ink);
                buffer.addVertex(pose, x0 - nx, y0 - ny, 0).setColor(ink);
                buffer.addVertex(pose, x1 - nx, y1 - ny, 0).setColor(ink);
                buffer.addVertex(pose, x1 + nx, y1 + ny, 0).setColor(ink);
            }
        }
        g.flush();
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

    @Override
    public List<LegendEntry> legend() {
        List<LegendEntry> entries = new ArrayList<>();
        String[] keys = {"high_road", "city_road", "town_road", "shipping_route"};
        Roads.Grade[] grades = {Roads.Grade.HIGH, Roads.Grade.CITY, Roads.Grade.TOWN, Roads.Grade.SEA};
        for (int i = 0; i < keys.length; i++) {
            int thick = thickness(grades[i]);
            int ink = ink(grades[i]);
            entries.add(new LegendEntry((g, x, y) -> g.fill(x, y + 5 - thick / 2, x + 10, y + 5 - thick / 2 + thick, ink),Component.translatable("gui.rafasconquest.map.legend." + keys[i])));
        }
        return entries;
    }
}
