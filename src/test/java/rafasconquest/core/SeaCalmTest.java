package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeaCalmTest {
    @Test
    void chunkSweepMatchesEachColumnsOwnDiamond() {
        SeaCalm.Depths depths = (x, z) -> Math.floorMod(x * 7 + z * 13 + (x * z >> 3), 41) < 6 ? 0 : Math.floorMod(x * 3 - z, 40);
        SeaCalm calm = new SeaCalm(depths).at(0);
        for (int x = -40; x < 40; x += 3) {
            for (int z = -40; z < 40; z += 5) {
                int most = 0;
                for (int dx = -SeaCalm.REACH; dx <= SeaCalm.REACH; dx++) {
                    int left = SeaCalm.REACH - Math.abs(dx);
                    for (int dz = -left; dz <= left; dz++) {
                        int d = Math.min(SeaCalm.DEEP, depths.depth(x + dx, z + dz));
                        int own = d == 0 ? SeaCalm.REACH : SeaCalm.REACH - d;
                        most = Math.max(most, own - Math.abs(dx) - Math.abs(dz));
                    }
                }
                assertEquals(Math.max(0, 255 - (most + 1) * 8) / 255.0, calm.column(x, z), 1e-12, x + ", " + z);
            }
        }
    }
}
