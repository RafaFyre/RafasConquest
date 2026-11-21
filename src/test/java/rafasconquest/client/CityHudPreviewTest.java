package rafasconquest.client;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CityHudPreviewTest {
    @Test
    void paintsTheHud() {
        BufferedImage img = new BufferedImage(1920, 1017, BufferedImage.TYPE_INT_ARGB);
        assertFalse(CityHud.paint(img, "Sarenstead", "SPRING 1ST, 1 AD", 0, () -> { }, new int[8], 0, () -> { }, () -> { }, () -> { }).isEmpty());
        assertNotEquals(0, img.getRGB(1000, 20) >>> 24, "the centre panel is drawn");
        assertNotEquals(0, img.getRGB(100, 1017 - 30) >>> 24, "the bottom-left bracket is drawn");
    }
}
