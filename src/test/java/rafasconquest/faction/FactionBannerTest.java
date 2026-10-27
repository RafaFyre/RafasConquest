package rafasconquest.faction;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FactionBannerTest {
    @Test
    void sameFactionAlwaysGetsTheSameBanner() {
        UUID id = new UUID(123, 456);
        assertEquals(FactionBanner.of(id), FactionBanner.of(id));
    }

    @Test
    void everyLayerContrastsWithTheBase() {
        for (int i = 0; i < 500; i++) {
            FactionBanner b = FactionBanner.of(new UUID(i * 31L, i * 977L));
            assertEquals(2, b.layers().size());
            b.layers().forEach(layer -> assertNotEquals(b.base(), layer.color()));
        }
    }

    @Test
    void factionsAreUsuallyDistinguishable() {
        Set<FactionBanner> seen = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            seen.add(FactionBanner.of(new UUID(i, ~i)));
        }
        assertTrue(seen.size() > 190, "only " + seen.size() + " distinct banners for 200 factions");
    }
}
