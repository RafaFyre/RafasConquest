package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/** Layer 1, the bottom: the terrain from the server, and the storm wall around it. Beyond the wall is unexplored. */
public class BaseMapLayer implements MapLayer {
    public static final int UNEXPLORED = 0xFF1E2126;
    private static final int STORM_WALL = 0xFFB7A6E0;
    private static final int RING_SEGMENTS = 512;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        g.fill(view.left, view.top, view.right, view.bottom, UNEXPLORED);
        drawAligned(g, view, map, MapContext.BASE_TEXTURE);
        // The storm wall: a ring of short dashes at its radius.
        double r = map.base.visibleRadius();
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
                    g.fill(x, y, x + 10, y + 10, 0xFF88B85C);
                    g.fill(x + 5, y, x + 10, y + 10, 0xFF4F86C6);
                }, Component.translatable("gui.rafasconquest.map.legend.terrain")),
                new LegendEntry((g, x, y) -> {
                    g.fill(x, y, x + 10, y + 10, UNEXPLORED);
                    for (int i = 0; i < 10; i += 3) {
                        g.fill(x + i, y + 4, x + i + 2, y + 6, STORM_WALL);
                    }
                }, Component.translatable("gui.rafasconquest.map.legend.storm_wall")));
    }
}
