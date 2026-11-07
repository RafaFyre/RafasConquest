package rafasconquest;

import org.junit.jupiter.api.Test;
import rafasconquest.core.CityLayout;
import rafasconquest.style.CityPlan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceMusicTest {
    private static final int SIZE = 3 * CityLayout.BLOCKS_PER_CELL * CityLayout.ROAD_PERIOD + CityLayout.ROAD_WIDTH;
    private static final CityPlan.Plan PLAIN = new CityPlan.Plan(0, 0, 0, 0, false, List.of());
    /** Noble district over blocks [2, 4) × [2, 4). */
    private static final CityPlan.Plan NOBLE = new CityPlan.Plan(2, 4, 2, 4, true, List.of());

    @Test
    void streetsAreInsideTheWalls() {
        assertTrue(PlaceMusic.inStreets(SIZE, PLAIN, SIZE / 2, SIZE / 2));
        assertTrue(PlaceMusic.inStreets(SIZE, PLAIN, CityLayout.ROAD_WIDTH, CityLayout.ROAD_WIDTH));
        assertFalse(PlaceMusic.inStreets(SIZE, PLAIN, 0, SIZE / 2), "on the wall");
        assertFalse(PlaceMusic.inStreets(SIZE, PLAIN, SIZE - 1, SIZE / 2), "on the far wall");
        assertFalse(PlaceMusic.inStreets(SIZE, PLAIN, -5, SIZE / 2), "outside");
        assertFalse(PlaceMusic.inStreets(SIZE, PLAIN, SIZE / 2, SIZE + 5), "outside");
    }

    @Test
    void noNobleDistrict() {
        int mid = CityLayout.blockStart(3);
        assertTrue(PlaceMusic.inStreets(SIZE, PLAIN, mid, mid));
        assertFalse(PlaceMusic.inStreets(SIZE, NOBLE, mid, mid), "in the district");
        assertFalse(PlaceMusic.inStreets(SIZE, NOBLE, CityLayout.roadStart(2), mid), "on its bounding road");
        assertTrue(PlaceMusic.inStreets(SIZE, NOBLE, CityLayout.roadStart(4) + CityLayout.ROAD_WIDTH, mid), "past it");
        assertTrue(PlaceMusic.inStreets(SIZE, NOBLE, mid, CityLayout.blockStart(6)), "beside it");
    }
}
