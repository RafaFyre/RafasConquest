package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import rafasconquest.map.PoliticalStyle;

import java.util.List;

/**
 * Layer 1, the bottom: the world from the server, washed to parchment, and the storm wall drawn as a dashed ring.
 * The known world reaches past the wall; beyond its edge is unexplored.
 */
public class BaseMapLayer implements MapLayer {
    public static final int UNEXPLORED = 0xFF1E2126;
    private static final int STORM_WALL = 0xFF6B4FA8;
    private static final int RING_SEGMENTS = 720;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        g.fill(view.left, view.top, view.right, view.bottom, UNEXPLORED);
        drawAligned(g, view, map, MapContext.BASE_TEXTURE);
    }

    /** The storm wall: a ring of short dashes at its radius. Drawn over the territories, so it shows through them. */
    static void drawStormWall(GuiGraphics g, MapView view, MapContext map) {
        double r = map.base.stormWallRadius();
        for (int i = 0; i < RING_SEGMENTS; i += 2) {
            double a = Math.PI * 2 * i / RING_SEGMENTS;
            double x = view.screenX(Math.cos(a) * r);
            double y = view.screenY(Math.sin(a) * r);
            if (view.near(x, y, 2)) {
                g.fill((int) x - 1, (int) y - 1, (int) x + 1, (int) y + 1, STORM_WALL);
            }
        }
    }

    /** Draws a texture that covers the base map pixel for pixel (the base map itself, or the territory layer). */
    static void drawAligned(GuiGraphics g, MapView view, MapContext map, ResourceLocation texture) {
        var base = map.base;
        g.pose().pushPose();
        g.pose().translate(view.screenX(base.minX()), view.screenY(base.minZ()), 0);
        float s = (float) (view.scale * base.blocksPerPixel());
        g.pose().scale(s, s, 1);
        g.blit(texture, 0, 0, 0F, 0F, base.width(), base.height(), base.width(), base.height());
        g.pose().popPose();
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(
                new LegendEntry((g, x, y) -> {
                    g.fill(x, y, x + 10, y + 10, 0xFF000000 | PoliticalStyle.PARCHMENT_LAND);
                    g.fill(x + 5, y, x + 10, y + 10, 0xFF000000 | PoliticalStyle.PARCHMENT_SEA);
                }, Component.translatable("gui.rafasconquest.map.legend.terrain")),
                new LegendEntry((g, x, y) -> {
                    g.fill(x, y, x + 10, y + 10, 0xFF000000 | PoliticalStyle.PARCHMENT_LAND);
                    for (int i = 0; i < 10; i += 3) {
                        g.fill(x + i, y + 4, x + i + 2, y + 6, STORM_WALL);
                    }
                }, Component.translatable("gui.rafasconquest.map.legend.storm_wall")),
                new LegendEntry((g, x, y) -> g.fill(x, y, x + 10, y + 10, UNEXPLORED),
                        Component.translatable("gui.rafasconquest.map.legend.unexplored")));
    }
}
