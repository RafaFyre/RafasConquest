package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Layer 4: paths and roads. The mod doesn't track any yet, so this layer draws nothing; it holds their place between
 * the city icons and the armies. Draw roads here (and add a legend entry) once there is road data.
 */
public class RoadLayer implements MapLayer {
    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
    }
}
