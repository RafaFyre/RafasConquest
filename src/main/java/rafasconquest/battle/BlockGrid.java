package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * An army's living blocks filed by where they stand, in square cells, so a search for the nearest block, or for those
 * within some distance, looks only at the cells around a point instead of at every block: what keeps battles of
 * 100,000 a side quick. Built from the blocks' positions at one moment; blocks that move a little after it are still
 * found, as searches allow {@link #SLACK} for it.
 */
final class BlockGrid {
    private static final double CELL = 32;
    /** How far a block may have moved since the grid was built and still be searched for correctly. */
    private static final double SLACK = 10;
    /** The widest a block's front rank reaches from its centre, which a search allows for. */
    private static final double MAX_HALF_WIDTH = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING / 2;

    private final Map<Long, List<FieldBlock>> cells = new HashMap<>();
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minZ = Integer.MAX_VALUE;
    private int maxZ = Integer.MIN_VALUE;

    BlockGrid(List<FieldBlock> blocks) {
        for (FieldBlock b : blocks) {
            if (b.alive == 0) {
                continue;
            }
            int cx = cell(b.pos.x());
            int cz = cell(b.pos.z());
            cells.computeIfAbsent(key(cx, cz), k -> new ArrayList<>()).add(b);
            minX = Math.min(minX, cx);
            maxX = Math.max(maxX, cx);
            minZ = Math.min(minZ, cz);
            maxZ = Math.max(maxZ, cz);
        }
    }

    private static int cell(double v) {
        return (int) Math.floor(v / CELL);
    }

    private static long key(int cx, int cz) {
        return ((long) cx << 32) | (cz & 0xFFFFFFFFL);
    }

    /** Every block standing (or, allowing for slack, that might stand) within {@code reach} of {@code at}. */
    void within(Point at, double reach, Consumer<FieldBlock> each) {
        double r = reach + SLACK;
        int x0 = cell(at.x() - r);
        int x1 = cell(at.x() + r);
        int z0 = cell(at.z() - r);
        int z1 = cell(at.z() + r);
        for (int cx = Math.max(x0, minX); cx <= Math.min(x1, maxX); cx++) {
            for (int cz = Math.max(z0, minZ); cz <= Math.min(z1, maxZ); cz++) {
                List<FieldBlock> list = cells.get(key(cx, cz));
                if (list != null) {
                    list.forEach(each);
                }
            }
        }
    }

    /**
     * The block nearest {@code at} by {@code distance} (which is never less than the centres' distance less
     * {@code reachOfOwn} and a block's half width) among those {@code wanted} accepts, searching outward ring by ring
     * of cells and stopping once no nearer one can be further out. Null if none is wanted.
     */
    @Nullable
    FieldBlock nearest(Point at, double reachOfOwn, Predicate<FieldBlock> wanted, ToDoubleFunction<FieldBlock> distance) {
        return nearest(at, reachOfOwn, wanted, distance, Double.MAX_VALUE);
    }

    /** The same, looking no further than {@code maxReach}: null if none is wanted that near. */
    @Nullable
    FieldBlock nearest(Point at, double reachOfOwn, Predicate<FieldBlock> wanted, ToDoubleFunction<FieldBlock> distance,
                       double maxReach) {
        if (cells.isEmpty()) {
            return null;
        }
        int ox = cell(at.x());
        int oz = cell(at.z());
        int rings = Math.max(Math.max(Math.abs(ox - minX), Math.abs(ox - maxX)), Math.max(Math.abs(oz - minZ), Math.abs(oz - maxZ)));
        FieldBlock best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int ring = 0; ring <= rings; ring++) {
            // Blocks in this ring and beyond stand at least (ring - 1) cells from the point.
            double nearestPossible = (ring - 1) * CELL - SLACK - reachOfOwn - MAX_HALF_WIDTH;
            if (nearestPossible > Math.min(bestDistance, maxReach)) {
                break;
            }
            for (int cx = ox - ring; cx <= ox + ring; cx++) {
                for (int cz = oz - ring; cz <= oz + ring; cz++) {
                    if (Math.abs(cx - ox) != ring && Math.abs(cz - oz) != ring) {
                        continue; // inside the ring: searched already
                    }
                    if (cx < minX || cx > maxX || cz < minZ || cz > maxZ) {
                        continue;
                    }
                    List<FieldBlock> list = cells.get(key(cx, cz));
                    if (list == null) {
                        continue;
                    }
                    for (FieldBlock b : list) {
                        if (b.alive > 0 && wanted.test(b)) {
                            double d = distance.applyAsDouble(b);
                            if (d < bestDistance && d <= maxReach) {
                                bestDistance = d;
                                best = b;
                            }
                        }
                    }
                }
            }
        }
        return best;
    }
}
