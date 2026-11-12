package rafasconquest.bridge;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WarCampTest {
    /** Every block of the farthest-flung tents lies in ground the battle loads beforehand (3 by 3 chunks per point). */
    @Test
    void groundCoversEveryTent() {
        // Facing east: the enemy toward +x, the camp's right toward +z, its tents behind toward -x.
        List<Point> ground = WarCamp.ground(new Point(5, 3), 1, 0, 36, true);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int f = -7 - 6 - row * 9;
                int r = (col - 2) * 7;
                for (int along = -3; along <= 6; along++) { // its patch, door and campfire
                    for (int dr = -2; dr <= 5; dr++) {
                        int x = 5 + f + along;
                        int z = 3 + r + dr;
                        assertTrue(loaded(ground, x, z), "tent block at " + x + ", " + z + " not loaded");
                    }
                }
            }
        }
        // And the guards and banners before the way in.
        assertTrue(loaded(ground, 5 + 14, 3));
    }

    private static boolean loaded(List<Point> ground, int x, int z) {
        for (Point p : ground) {
            if (Math.abs((Math.floorDiv((int) Math.floor(p.x()), 16)) - Math.floorDiv(x, 16)) <= 1
                    && Math.abs((Math.floorDiv((int) Math.floor(p.z()), 16)) - Math.floorDiv(z, 16)) <= 1) {
                return true;
            }
        }
        return false;
    }
}
