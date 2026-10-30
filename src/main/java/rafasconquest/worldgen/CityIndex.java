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
            int r = c.reach();
            for (int cx = Math.floorDiv(c.x() - r, CELL); cx <= Math.floorDiv(c.x() + r, CELL); cx++) {
                for (int cz = Math.floorDiv(c.z() - r, CELL); cz <= Math.floorDiv(c.z() + r, CELL); cz++) {
                    cells.computeIfAbsent(key(cx, cz), k -> new ArrayList<>()).add(c);
                }
            }
        }
    }

    public List<SeatCities.City> all() {
        return all;
    }

    /** Every city whose square (and margin) reaches into the box. */
    public List<SeatCities.City> inBox(int minX, int minZ, int maxX, int maxZ) {
        List<SeatCities.City> found = new ArrayList<>();
        for (int cx = Math.floorDiv(minX, CELL); cx <= Math.floorDiv(maxX, CELL); cx++) {
            for (int cz = Math.floorDiv(minZ, CELL); cz <= Math.floorDiv(maxZ, CELL); cz++) {
                for (SeatCities.City c : cells.getOrDefault(key(cx, cz), List.of())) {
                    int r = c.reach();
                    if (!found.contains(c) && c.x() + r >= minX && c.x() - r <= maxX && c.z() + r >= minZ && c.z() - r <= maxZ) {
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
