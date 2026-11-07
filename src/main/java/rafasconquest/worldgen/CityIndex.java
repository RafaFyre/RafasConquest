package rafasconquest.worldgen;

import rafasconquest.core.SeatCities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The world's seat cities, filed by where they stand for quick lookups from world generation threads. Built once from
 * the world map and read-only after.
 */
public final class CityIndex {
    private static final int CELL = 1024;
    public static final CityIndex EMPTY = new CityIndex(List.of());

    private final List<SeatCities.City> all;
    private final Map<Long, List<SeatCities.City>> cells = new HashMap<>();

    public CityIndex(List<SeatCities.City> cities) {
        this.all = List.copyOf(cities);
        for (SeatCities.City c : all) {
            int[] b = bounds(c);
            for (int cx = Math.floorDiv(b[0], CELL); cx <= Math.floorDiv(b[2], CELL); cx++) {
                for (int cz = Math.floorDiv(b[1], CELL); cz <= Math.floorDiv(b[3], CELL); cz++) {
                    cells.computeIfAbsent(key(cx, cz), k -> new ArrayList<>()).add(c);
                }
            }
        }
    }

    /** A city's square and margin, with the ground kept for its outside noble district: {minX, minZ, maxX, maxZ}. */
    private static int[] bounds(SeatCities.City c) {
        int r = c.reach();
        int[] b = {c.x() - r, c.z() - r, c.x() + r, c.z() + r};
        int[] a = c.annex();
        return a == null ? b : new int[]{Math.min(b[0], a[0]), Math.min(b[1], a[1]), Math.max(b[2], a[2]), Math.max(b[3], a[3])};
    }

    public List<SeatCities.City> all() {
        return all;
    }

    /** Every city whose square (and margin), or its outside noble district's ground, reaches into the box. */
    public List<SeatCities.City> inBox(int minX, int minZ, int maxX, int maxZ) {
        List<SeatCities.City> found = new ArrayList<>();
        for (int cx = Math.floorDiv(minX, CELL); cx <= Math.floorDiv(maxX, CELL); cx++) {
            for (int cz = Math.floorDiv(minZ, CELL); cz <= Math.floorDiv(maxZ, CELL); cz++) {
                for (SeatCities.City c : cells.getOrDefault(key(cx, cz), List.of())) {
                    int[] b = bounds(c);
                    if (!found.contains(c) && b[2] >= minX && b[0] <= maxX && b[3] >= minZ && b[1] <= maxZ) {
                        found.add(c);
                    }
                }
            }
        }
        return found;
    }

    private static long key(int cx, int cz) {
        return ((long) cx << 32) ^ (cz & 0xffffffffL);
    }
}
