package rafasconquest.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Where a city district's townsfolk walk: its roads (not the edge road under the wall) cut into stretches, one for each
 * townsperson, who walks theirs back and forth. A district has {@link #PER_DISTRICT} on its ordinary roads; the main
 * roads (the avenues through the gates, across the town centre) are twice as busy, their stretches half as long. Each
 * keeping to their own stretch, they stay spread out along the roads.
 */
public final class StreetWalks {
    /** Townsfolk on a district's ordinary roads. */
    public static final int PER_DISTRICT = 40;
    /** How much busier the main roads are. */
    static final int MAIN_ROAD_BUSIER = 2;

    /** One townsperson's stretch of road, from (x0, z0) to (x1, z1), along the road's middle (block centres). */
    public record Walk(double x0, double z0, double x1, double z1, boolean mainRoad) {
        public double length() {
            return Math.hypot(x1 - x0, z1 - z0);
        }
    }

    private StreetWalks() {
    }

    /** The stretches of district (cellX, cellZ) of a city. */
    public static List<Walk> walks(CityLayout.Site city, int cellX, int cellZ) {
        int blocks = city.blocksPerSide();
        int main = city.blocksPerSide() / 2;
        // The roads of the district: those running east-west (along x, at a z) and north-south (along z, at an x).
        record Line(boolean alongX, int at, int from, int to, boolean main) {
            int length() {
                return to - from + 1;
            }
        }
        List<Line> lines = new ArrayList<>();
        int x0 = CityLayout.cellMin(cellX);
        int x1 = Math.min(CityLayout.cellMax(cellX), city.size() - 1 - CityLayout.ROAD_WIDTH);
        int z0 = CityLayout.cellMin(cellZ);
        int z1 = Math.min(CityLayout.cellMax(cellZ), city.size() - 1 - CityLayout.ROAD_WIDTH);
        for (int k = cellZ * CityLayout.BLOCKS_PER_CELL; k < (cellZ + 1) * CityLayout.BLOCKS_PER_CELL; k++) {
            if (k > 0 && k < blocks) {
                lines.add(new Line(true, CityLayout.roadStart(k) + 1, Math.max(x0, CityLayout.ROAD_WIDTH), x1, k == main));
            }
        }
        for (int k = cellX * CityLayout.BLOCKS_PER_CELL; k < (cellX + 1) * CityLayout.BLOCKS_PER_CELL; k++) {
            if (k > 0 && k < blocks) {
                lines.add(new Line(false, CityLayout.roadStart(k) + 1, Math.max(z0, CityLayout.ROAD_WIDTH), z1, k == main));
            }
        }
        int ordinary = 0;
        for (Line l : lines) {
            ordinary += l.main() ? 0 : l.length();
        }
        if (ordinary == 0) {
            return List.of();
        }
        double perBlock = (double) PER_DISTRICT / ordinary;
        List<Walk> walks = new ArrayList<>();
        int ox = city.originX();
        int oz = city.originZ();
        for (Line l : lines) {
            int count = Math.max(1, (int) Math.round(l.length() * perBlock * (l.main() ? MAIN_ROAD_BUSIER : 1)));
            double stretch = (double) l.length() / count;
            for (int i = 0; i < count; i++) {
                double a = l.from() + i * stretch + 0.5;
                double b = l.from() + (i + 1) * stretch - 0.5;
                double across = l.at() + 0.5;
                walks.add(l.alongX()
                        ? new Walk(ox + a, oz + across, ox + b, oz + across, l.main())
                        : new Walk(ox + across, oz + a, ox + across, oz + b, l.main()));
            }
        }
        return walks;
    }
}
