package rafasconquest.core;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The 32-block plates a city grows by in the city manager. Plate (0, 0) starts at the north-west corner of the city's
 * flat box (its square, wall and the margin past it, {@link CityTerrain}), at world (ax, az), {@code boxSize} a side.
 * Ground outside the box is flattened a plate at a time, each touching the box or a plate flattened before it; never
 * over the walls (and what they hold), the harbour or an outside noble district. Shared by server and client.
 */
public record CityPlates(int ax, int az, int boxSize, int plateY, int[] walled, @Nullable int[] harbour, @Nullable int[] annex) {
    public static final int PLATE = 32;

    /** Plates along a side of the box; the last may lie only partly in it. */
    public int boxPlates() {
        return (boxSize + PLATE - 1) / PLATE;
    }

    public int minX(int i) {
        return ax + i * PLATE;
    }

    public int minZ(int j) {
        return az + j * PLATE;
    }

    /** The plate holding world (x, z), as a {@link #key}. */
    public long at(double x, double z) {
        return key(Math.floorDiv((int) Math.floor(x) - ax, PLATE), Math.floorDiv((int) Math.floor(z) - az, PLATE));
    }

    public static long key(int i, int j) {
        return ((long) i << 32) | (j & 0xFFFFFFFFL);
    }

    public static int i(long key) {
        return (int) (key >> 32);
    }

    public static int j(long key) {
        return (int) key;
    }

    /** Wholly inside the city's box: flat already. */
    public boolean inBox(int i, int j) {
        return i >= 0 && j >= 0 && (i + 1) * PLATE <= boxSize && (j + 1) * PLATE <= boxSize;
    }

    /** Overlaps ground that may not be reshaped. */
    public boolean blocked(int i, int j) {
        return overlaps(i, j, walled) || overlaps(i, j, harbour) || overlaps(i, j, annex);
    }

    private boolean overlaps(int i, int j, @Nullable int[] r) {
        return r != null && minX(i) <= r[2] && minX(i) + PLATE - 1 >= r[0] && minZ(j) <= r[3] && minZ(j) + PLATE - 1 >= r[1];
    }

    /** Shares a side with a plate of the box or one in {@code flat}. */
    public boolean touches(int i, int j, Set<Long> flat) {
        int n = boxPlates();
        int[][] sides = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : sides) {
            int ni = i + d[0];
            int nj = j + d[1];
            if (ni >= 0 && nj >= 0 && ni < n && nj < n || flat.contains(key(ni, nj))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Those of {@code wanted} that may be flattened, given the plates already {@code flat}: each must touch the box,
     * a flat plate or another accepted one; in the order wanted.
     */
    public List<Long> accept(Collection<Long> wanted, Set<Long> flat) {
        return grow(wanted, flat, k -> !flat.contains(k) && !inBox(i(k), j(k)) && !blocked(i(k), j(k)));
    }

    /**
     * Those of {@code wanted} a district may go on: flat plates without one, each touching the box, a district or
     * another accepted plate, or crossed by a main road (one of {@code roadPlates}); in the order wanted.
     */
    public List<Long> acceptDistricts(Collection<Long> wanted, Set<Long> flat, Set<Long> districts, Set<Long> roadPlates) {
        return grow(wanted, districts, k -> flat.contains(k) && !districts.contains(k), roadPlates::contains);
    }

    private List<Long> grow(Collection<Long> wanted, Set<Long> from, Predicate<Long> ok) {
        return grow(wanted, from, ok, k -> false);
    }

    /**
     * Those of {@code wanted} that {@code ok} allows and that touch the box, {@code from} or one accepted before, or
     * that {@code near} counts as touching already.
     */
    private List<Long> grow(Collection<Long> wanted, Set<Long> from, Predicate<Long> ok, Predicate<Long> near) {
        Set<Long> reach = new HashSet<>(from);
        Set<Long> accepted = new LinkedHashSet<>();
        boolean grew = true;
        while (grew) {
            grew = false;
            for (long k : wanted) {
                if (!accepted.contains(k) && ok.test(k) && (near.test(k) || touches(i(k), j(k), reach))) {
                    accepted.add(k);
                    reach.add(k);
                    grew = true;
                }
            }
        }
        return wanted.stream().filter(accepted::contains).distinct().toList();
    }

    // --- Main roads: straight runs at any angle, {x1, z1, x2, z2} between block middles, 5 wide. ---

    public static final double ROAD_HALF = 2.5;
    public static final int ROAD_MIN = 4;
    public static final int ROAD_MAX = 256;

    public static double length(int[] r) {
        return Math.hypot(r[2] - r[0], r[3] - r[1]);
    }

    /** How far block (x, z)'s middle lies from a road's centreline. */
    public static double distance(int x, int z, int[] r) {
        double dx = r[2] - r[0];
        double dz = r[3] - r[1];
        double len2 = dx * dx + dz * dz;
        double t = len2 == 0 ? 0 : Math.max(0, Math.min(1, ((x - r[0]) * dx + (z - r[1]) * dz) / len2));
        return Math.hypot(x - r[0] - t * dx, z - r[1] - t * dz);
    }

    /** How far along a road (0 at its start, in blocks) block (x, z) lies. */
    public static double along(int x, int z, int[] r) {
        double len = length(r);
        return len == 0 ? 0 : Math.max(0, Math.min(len, ((x - r[0]) * (double) (r[2] - r[0]) + (z - r[1]) * (double) (r[3] - r[1])) / len));
    }

    public static boolean onRoad(int x, int z, List<int[]> roads) {
        for (int[] r : roads) {
            if (distance(x, z, r) <= ROAD_HALF) {
                return true;
            }
        }
        return false;
    }

    /** The blocks a road paves: those whose middles lie within its half width. */
    public static List<int[]> columns(int[] r) {
        List<int[]> cols = new java.util.ArrayList<>();
        int pad = (int) Math.ceil(ROAD_HALF);
        for (int x = Math.min(r[0], r[2]) - pad; x <= Math.max(r[0], r[2]) + pad; x++) {
            for (int z = Math.min(r[1], r[3]) - pad; z <= Math.max(r[1], r[3]) + pad; z++) {
                if (distance(x, z, r) <= ROAD_HALF) {
                    cols.add(new int[]{x, z});
                }
            }
        }
        return cols;
    }

    /** Within the city's flat box (the ground between its walls and the box's edge included). */
    public boolean inBoxArea(int x, int z) {
        return x >= ax && z >= az && x < ax + boxSize && z < az + boxSize;
    }

    private static boolean in(@Nullable int[] r, int x, int z) {
        return r != null && x >= r[0] && x <= r[2] && z >= r[1] && z <= r[3];
    }

    /**
     * Why a road may not be laid beside {@code roads}, or null if it may: it must start on the city's flat ground
     * outside its walls or on another road, and cross no walls, harbour, outside noble district or district.
     */
    @Nullable
    public String roadProblem(int[] r, List<int[]> roads, Set<Long> districts) {
        double len = length(r);
        if (len < ROAD_MIN) {
            return "Too short";
        }
        if (len > ROAD_MAX) {
            return "Longer than " + ROAD_MAX + " blocks: lay it in parts";
        }
        if (!(inBoxArea(r[0], r[1]) && !in(walled, r[0], r[1])) && !onRoad(r[0], r[1], roads)) {
            return "A road must start on the city's edge or on another road";
        }
        for (int[] c : columns(r)) {
            if (in(walled, c[0], c[1]) || in(harbour, c[0], c[1]) || in(annex, c[0], c[1])) {
                return "A road may not cross the walls, the harbour or the noble district";
            }
            if (districts.contains(at(c[0], c[1])) && !onRoad(c[0], c[1], roads)) {
                return "A road may not cross a district";
            }
        }
        return null;
    }

    /** The plates any of {@code roads} paves part of. */
    public Set<Long> roadPlates(List<int[]> roads) {
        Set<Long> plates = new HashSet<>();
        for (int[] r : roads) {
            for (int[] c : columns(r)) {
                plates.add(at(c[0], c[1]));
            }
        }
        return plates;
    }
}
