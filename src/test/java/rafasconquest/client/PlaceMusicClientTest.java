package rafasconquest.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceMusicClientTest {
    @Test
    void ambientIsHalfOutdoorsAndMuffledIndoors() {
        assertEquals(0.5f, PlaceMusicClient.ambientVolume(15), 1e-6);
        assertEquals(0.5f, PlaceMusicClient.ambientVolume(14), 1e-6);
        assertEquals(0.12f, PlaceMusicClient.ambientVolume(8), 1e-6);
        assertEquals(0.12f, PlaceMusicClient.ambientVolume(0), 1e-6);
        float doorway = PlaceMusicClient.ambientVolume(11);
        assertTrue(doorway > 0.12f && doorway < 0.5f);
    }
}
