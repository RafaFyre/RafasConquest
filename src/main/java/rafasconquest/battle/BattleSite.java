package rafasconquest.battle;

import rafasconquest.battle.BattlePlan.Point;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Where a battle is fought: near where it was called, moved over the land so that no part of an army is cut off from
 * the rest of it by water (a river or the sea), as the world map shows it. Each army is in parts (its centre and wings,
 * each with its camp); every part must stand on dry ground all of a piece. A wing cut off from its army's other parts,
 * or the two armies cut off from each other, is allowed but costs the site something; so does every block of distance
 * it is moved. If nowhere within {@link #MAX_SHIFT} fits, the site where the fewest parts are cut off is taken.
 */
public final class BattleSite {
    /** What the land is like, as coarse cells {@code cell()} blocks wide: whether a cell is under water. */
    public interface Water {
        int cell();

        boolean wet(int cellX, int cellZ);
    }

    /** One part of an army (side 0 or 1): the places of its blocks and its camp. */
    public record Part(int side, List<Point> places) {
    }

    /** How far the battle may be moved each way, and in what steps. */
    static final double MAX_SHIFT = 320;
    static final double STEP = 16;
    /** What a cut-off wing, or two armies cut off from each other, cost a site, in blocks of distance moved. */
    static final double WING_CUT = 150;
    static final double ARMIES_CUT = 300;
    /** What a part that doesn't fit at all costs: more than any distance. */
    private static final double PART_UNFIT = 100_000;
    /** Cells of land looked through around the parts, for a way round the water between them. */
    private static final int MARGIN = 3;

    private BattleSite() {
    }

    /** How far to move the battle (x, z): zero if it fits where it is. */
    public static Point shift(List<Part> parts, Water water) {
        // Nearest first: once a site costs little, everything further away than that is passed over.
        List<Point> shifts = new java.util.ArrayList<>();
        for (double dx = -MAX_SHIFT; dx <= MAX_SHIFT; dx += STEP) {
            for (double dz = -MAX_SHIFT; dz <= MAX_SHIFT; dz += STEP) {
                if (Math.hypot(dx, dz) <= MAX_SHIFT) {
                    shifts.add(new Point(dx, dz));
                }
            }
        }
        shifts.sort(java.util.Comparator.comparingDouble(s -> Math.hypot(s.x(), s.z())));
        double bestScore = Double.MAX_VALUE;
        Point best = new Point(0, 0);
        for (Point s : shifts) {
            double distance = Math.hypot(s.x(), s.z());
            if (distance >= bestScore) {
                break;
            }
            double score = distance + cost(parts, water, s.x(), s.z(), bestScore - distance);
            if (score < bestScore) {
                bestScore = score;
                best = s;
            }
        }
        return best;
    }

    /** What the battle's parts cost moved by (dx, dz); stops counting past {@code enough}. */
    static double cost(List<Part> parts, Water water, double dx, double dz, double enough) {
        int c = water.cell();
        double cost = 0;
        List<Set<Long>> cells = new java.util.ArrayList<>();
        for (Part part : parts) {
            Set<Long> own = new HashSet<>();
            for (Point p : part.places()) {
                own.add(key(Math.floorDiv((int) Math.floor(p.x() + dx), c), Math.floorDiv((int) Math.floor(p.z() + dz), c)));
            }
            cells.add(own);
            if (!together(own, own, water)) {
                cost += PART_UNFIT;
                if (cost >= enough) {
                    return cost;
                }
            }
        }
        boolean armiesCut = true;
        for (int i = 0; i < parts.size(); i++) {
            for (int j = i + 1; j < parts.size(); j++) {
                boolean joined = together(cells.get(i), cells.get(j), water);
                if (parts.get(i).side() == parts.get(j).side()) {
                    cost += joined ? 0 : WING_CUT;
                } else if (joined) {
                    armiesCut = false;
                }
            }
        }
        return cost + (armiesCut && parts.stream().map(Part::side).distinct().count() > 1 ? ARMIES_CUT : 0);
    }

    /**
     * Whether every cell of {@code from} and {@code to} is dry and they are all one piece of land: reachable from one
     * another over dry cells, looking no further than {@link #MARGIN} cells past them.
     */
    private static boolean together(Set<Long> from, Set<Long> to, Water water) {
        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        Set<Long> wanted = new HashSet<>(from);
        wanted.addAll(to);
        for (long k : wanted) {
            int x = (int) (k >> 32);
            int z = (int) k;
            if (water.wet(x, z)) {
                return false;
            }
            minX = Math.min(minX, x);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxZ = Math.max(maxZ, z);
        }
        minX -= MARGIN;
        minZ -= MARGIN;
        maxX += MARGIN;
        maxZ += MARGIN;
        long start = wanted.iterator().next();
        Set<Long> seen = new HashSet<>();
        ArrayDeque<Long> open = new ArrayDeque<>();
        seen.add(start);
        open.add(start);
        int found = 0;
        while (!open.isEmpty()) {
            long k = open.poll();
            if (wanted.contains(k) && ++found == wanted.size()) {
                return true;
            }
            int x = (int) (k >> 32);
            int z = (int) k;
            int[][] steps = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] s : steps) {
                int nx = x + s[0];
                int nz = z + s[1];
                long n = key(nx, nz);
                if (nx < minX || nx > maxX || nz < minZ || nz > maxZ || seen.contains(n) || water.wet(nx, nz)) {
                    continue;
                }
                seen.add(n);
                open.add(n);
            }
        }
        return false;
    }

    private static long key(int x, int z) {
        return ((long) x << 32) | (z & 0xffffffffL);
    }
}
