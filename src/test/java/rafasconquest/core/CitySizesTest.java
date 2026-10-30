package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Cities of every size a title can seat, from a 2 × 2 Town to a 9 × 9 metropolis, lay out as a 3 × 3 city does. */
class CitySizesTest {
    private static final int[] SIZES = {2, 3, 4, 5, 7, 8, 9};

    @Test
    void theWallClosesAndEveryCellBuildsItsOwnShare() {
        for (int cells : SIZES) {
            List<CityLayout.WallSegment> all = CityLayout.wallSegments(cells);
            assertEquals(4 * cells * CityLayout.BLOCKS_PER_CELL, all.size());
            int total = 0;
            for (int cx = 0; cx < cells; cx++) {
                for (int cz = 0; cz < cells; cz++) {
                    total += CityLayout.wallSegments(cells, cx, cz).size();
                }
            }
            assertEquals(all.size(), total, cells + "×" + cells + ": not every segment is built by exactly one cell");
            assertEquals(4, all.stream().filter(CityLayout.WallSegment::gate).count(), "one gate a side");
        }
    }

    @Test
    void everyRoadIsBuiltOnce() {
        for (int cells : SIZES) {
            Set<Integer> roads = new HashSet<>();
            for (int cell = 0; cell < cells; cell++) {
                for (int k : CityLayout.roadsInCell(cells, cell)) {
                    assertTrue(roads.add(k), "road " + k + " built twice");
                }
            }
            assertEquals(cells * CityLayout.BLOCKS_PER_CELL + 1, roads.size());
        }
    }

    @Test
    void eachColumnIsFlattenedByOneCellWhoseStartIsWithinReach() {
        for (int cells : SIZES) {
            CityLayout.Site site = new CityLayout.Site(0, 0, false, cells);
            for (int cx = 0; cx < cells; cx++) {
                CityLayout.District d = new CityLayout.District(site, cx, 0);
                int start = d.x() - site.originX();
                for (int l = CityTerrain.ownedMin(cx); l <= CityTerrain.ownedMax(cells, cx); l += 5) {
                    assertEquals(cx, CityTerrain.cellOf(cells, l), cells + "×" + cells + " column " + l);
                    assertTrue(Math.abs(l - start) <= 128, "a column beyond the engine's reach from its cell's start");
                }
            }
        }
    }

    @Test
    void theTownCentreIsInTheMiddleCell() {
        for (int cells : SIZES) {
            CityLayout.Site site = new CityLayout.Site(0, 0, false, cells);
            assertEquals(site.centerBlock() / CityLayout.BLOCKS_PER_CELL, site.centerCell());
            assertEquals(cells * cells - 1, CityLayout.districts(site).size());
            int centre = CityLayout.blockStart(site.centerBlock()) + CityLayout.PLOT_SIZE;
            assertTrue(Math.abs(centre - site.halfExtent()) <= CityLayout.ROAD_PERIOD, "the town centre is off the middle");
        }
    }
}
