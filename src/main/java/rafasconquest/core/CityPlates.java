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
     * another accepted plate; in the order wanted.
     */
    public List<Long> acceptDistricts(Collection<Long> wanted, Set<Long> flat, Set<Long> districts) {
        return grow(wanted, districts, k -> flat.contains(k) && !districts.contains(k));
    }

    /** Those of {@code wanted} that {@code ok} allows and that touch the box, {@code from} or one accepted before. */
    private List<Long> grow(Collection<Long> wanted, Set<Long> from, Predicate<Long> ok) {
        Set<Long> reach = new HashSet<>(from);
        Set<Long> accepted = new LinkedHashSet<>();
        boolean grew = true;
        while (grew) {
            grew = false;
            for (long k : wanted) {
                if (!accepted.contains(k) && ok.test(k) && touches(i(k), j(k), reach)) {
                    accepted.add(k);
                    reach.add(k);
                    grew = true;
                }
            }
        }
        return wanted.stream().filter(accepted::contains).distinct().toList();
    }
}
