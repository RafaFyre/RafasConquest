package rafasconquest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Splits the land into provinces, like the counties of a strategy-game map. Pure, so it can be tested and drawn
 * without the game.
 * <ul>
 *   <li><b>Centres</b> sit on a seeded, jittered grid every {@link #SPACING} blocks, so provinces come out of roughly
 *       even size. Rough land (deserts, snow, peaks) drops some centres, so provinces there are larger. Every city
 *       (City-state home or Empire capital) is a centre itself and replaces the grid centres right next to it.</li>
 *   <li><b>Shape</b>: each land pixel joins the centre that is cheapest to reach in a straight line, not the nearest.
 *       Crossing water costs a lot (coasts and rivers become borders, islands get their own provinces), climbing
 *       costs (ridges become borders), and crossing from one biome into another costs a little (borders lean onto
 *       biome edges).</li>
 * </ul>
 * Everything a pixel looks at lies within {@link #SEARCH} blocks of it, and centres come from a global grid, so any
 * part of the world can be computed on its own and still meet its neighbours seamlessly.
 */
public final class ProvinceMap {
    /** Blocks between province centres on ordinary land. */
    public static final int SPACING = 768;
    /** How far a pixel looks for centres. Covers the gaps rough land leaves. */
    public static final int SEARCH = 3 * SPACING;
    /** Grid centres closer than this to a city give way to it. */
    static final int ANCHOR_CLEARANCE = (int) (SPACING * 0.6);
    /** Chance that rough land drops a grid centre. */
    static final double ROUGH_DROP = 0.5;

    /** Path costs, in blocks of extra distance. */
    static final double WATER_COST = 8;
    static final double CLIMB_COST = 4;
    static final double BIOME_EDGE_COST = 32;
    /** Passes of {@link #smooth} after assigning. */
    static final int SMOOTHING = 2;

    /** Province value of a water pixel, and of land no centre reaches. */
    public static final int WATER = -2;
    public static final int NONE = -1;

    private ProvinceMap() {
    }

    public record Centre(int x, int z, boolean city) {
    }

    /**
     * The terrain of the area being split, one value per map pixel (pixel (px, pz) covers {@code blocksPerPixel}
     * blocks from (minX + px × bpp, minZ + pz × bpp)). {@code rough} marks deserts, snow and peaks.
     */
    public record Terrain(int minX, int minZ, int blocksPerPixel, int width, int height,
                          boolean[] water, float[] heights, int[] biomes, boolean[] rough) {
        int index(int px, int pz) {
            return pz * width + px;
        }

        boolean inside(int px, int pz) {
            return px >= 0 && pz >= 0 && px < width && pz < height;
        }

        int pixelX(int x) {
            return Math.floorDiv(x - minX, blocksPerPixel);
        }

        int pixelZ(int z) {
            return Math.floorDiv(z - minZ, blocksPerPixel);
        }
    }

    /** Province centres for the area, from the seed and the cities in and around it. */
    public static List<Centre> centres(long seed, Terrain t, List<int[]> cities) {
        List<Centre> centres = new ArrayList<>();
        for (int[] city : cities) {
            int px = t.pixelX(city[0]);
            int pz = t.pixelZ(city[1]);
            if (t.inside(px, pz)) {
                centres.add(new Centre(city[0], city[1], true));
            }
        }
        int x0 = Math.floorDiv(t.minX(), SPACING);
        int z0 = Math.floorDiv(t.minZ(), SPACING);
        int x1 = Math.floorDiv(t.minX() + t.width() * t.blocksPerPixel(), SPACING);
        int z1 = Math.floorDiv(t.minZ() + t.height() * t.blocksPerPixel(), SPACING);
        for (int cx = x0; cx <= x1; cx++) {
            for (int cz = z0; cz <= z1; cz++) {
                long h = SeedHash.hash(seed, SeedHash.SALT_PROVINCE, cx, cz);
                int x = cx * SPACING + (int) (SPACING * (0.15 + 0.7 * SeedHash.unit(h)));
                int z = cz * SPACING + (int) (SPACING * (0.15 + 0.7 * SeedHash.unit(SeedHash.mix(h))));
                int px = t.pixelX(x);
                int pz = t.pixelZ(z);
                if (!t.inside(px, pz) || t.water()[t.index(px, pz)]) {
                    continue;
                }
                if (t.rough()[t.index(px, pz)] && SeedHash.unit(SeedHash.mix(h + 1)) < ROUGH_DROP) {
                    continue;
                }
                boolean nearCity = false;
                for (int[] city : cities) {
                    if (Math.abs(city[0] - x) < ANCHOR_CLEARANCE && Math.abs(city[1] - z) < ANCHOR_CLEARANCE) {
                        nearCity = true;
                        break;
                    }
                }
                if (!nearCity) {
                    centres.add(new Centre(x, z, false));
                }
            }
        }
        return centres;
    }

    /** The province (index into {@code centres}) of every pixel; {@link #WATER} for water, {@link #NONE} if unreached. */
    public static int[] assign(Terrain t, List<Centre> centres) {
        // Bucket the centres by grid cell, so each pixel only looks at the ones in range.
        int cell = SPACING;
        int gx0 = Math.floorDiv(t.minX() - SEARCH, cell);
        int gz0 = Math.floorDiv(t.minZ() - SEARCH, cell);
        int gw = Math.floorDiv(t.minX() + t.width() * t.blocksPerPixel() + SEARCH, cell) - gx0 + 1;
        int gh = Math.floorDiv(t.minZ() + t.height() * t.blocksPerPixel() + SEARCH, cell) - gz0 + 1;
        List<List<Integer>> buckets = new ArrayList<>(gw * gh);
        for (int i = 0; i < gw * gh; i++) {
            buckets.add(new ArrayList<>(2));
        }
        for (int i = 0; i < centres.size(); i++) {
            Centre c = centres.get(i);
            int bx = Math.floorDiv(c.x(), cell) - gx0;
            int bz = Math.floorDiv(c.z(), cell) - gz0;
            if (bx >= 0 && bz >= 0 && bx < gw && bz < gh) {
                buckets.get(bz * gw + bx).add(i);
            }
        }
        int reach = SEARCH / cell + 1;
        int[] provinces = new int[t.width() * t.height()];
        IntStream.range(0, t.height()).parallel().forEach(pz -> {
            double[] dist = new double[64];
            int[] near = new int[64];
            for (int px = 0; px < t.width(); px++) {
                int index = t.index(px, pz);
                if (t.water()[index]) {
                    provinces[index] = WATER;
                    continue;
                }
                int x = t.minX() + px * t.blocksPerPixel() + t.blocksPerPixel() / 2;
                int z = t.minZ() + pz * t.blocksPerPixel() + t.blocksPerPixel() / 2;
                int bx = Math.floorDiv(x, cell) - gx0;
                int bz = Math.floorDiv(z, cell) - gz0;
                int n = 0;
                for (int dz = -reach; dz <= reach; dz++) {
                    for (int dx = -reach; dx <= reach; dx++) {
                        int qx = bx + dx;
                        int qz = bz + dz;
                        if (qx < 0 || qz < 0 || qx >= gw || qz >= gh) {
                            continue;
                        }
                        for (int ci : buckets.get(qz * gw + qx)) {
                            Centre c = centres.get(ci);
                            double d = Math.hypot(c.x() - x, c.z() - z);
                            if (d <= SEARCH) {
                                if (n == near.length) {
                                    near = Arrays.copyOf(near, n * 2);
                                    dist = Arrays.copyOf(dist, n * 2);
                                }
                                near[n] = ci;
                                dist[n] = d;
                                n++;
                            }
                        }
                    }
                }
                // Cheapest path wins. A path costs at least its length, so try the nearest first and stop once
                // the remaining ones are already too far.
                sortByDistance(near, dist, n);
                int best = NONE;
                double bestCost = Double.MAX_VALUE;
                for (int k = 0; k < n && dist[k] < bestCost; k++) {
                    Centre c = centres.get(near[k]);
                    double cost = pathCost(t, c.x(), c.z(), x, z, dist[k], bestCost);
                    if (cost < bestCost || cost == bestCost && near[k] < best) {
                        bestCost = cost;
                        best = near[k];
                    }
                }
                provinces[index] = best;
            }
        });
        return smooth(t, provinces, SMOOTHING);
    }

    /**
     * The cost of the straight path from a centre to a point: its length, plus water crossed, biome edges crossed and
     * the height of any ridge between them (how far the path rises above both its ends, so ordinary bumps cost
     * nothing), sampled every pixel. Gives up (returning the limit) once it can't win.
     */
    static double pathCost(Terrain t, int fromX, int fromZ, int toX, int toZ, double length, double limit) {
        int steps = Math.max(1, (int) Math.ceil(length / t.blocksPerPixel()));
        double stepLength = length / steps;
        double cost = length;
        float first = Float.NaN;
        float last = Float.NaN;
        float highest = -Float.MAX_VALUE;
        int lastBiome = Integer.MIN_VALUE;
        for (int i = 0; i <= steps; i++) {
            double f = (double) i / steps;
            int px = t.pixelX((int) Math.round(fromX + (toX - fromX) * f));
            int pz = t.pixelZ((int) Math.round(fromZ + (toZ - fromZ) * f));
            if (!t.inside(px, pz)) {
                continue;
            }
            int index = t.index(px, pz);
            if (t.water()[index]) {
                cost += WATER_COST * stepLength;
            }
            float h = t.heights()[index];
            int biome = t.biomes()[index];
            if (Float.isNaN(first)) {
                first = h;
            } else if (biome != lastBiome) {
                cost += BIOME_EDGE_COST;
            }
            last = h;
            highest = Math.max(highest, h);
            lastBiome = biome;
            if (cost >= limit) {
                return limit;
            }
        }
        if (!Float.isNaN(first)) {
            cost += 2 * CLIMB_COST * Math.max(0, highest - Math.max(first, last));
        }
        return Math.min(cost, limit);
    }

    /**
     * Gives each land pixel the province most of its 8 neighbours have, when that is most of them, which smooths ragged
     * borders and clears out stray specks. Looks only one pixel away per pass, so areas still meet seamlessly.
     */
    static int[] smooth(Terrain t, int[] provinces, int passes) {
        int w = t.width();
        int h = t.height();
        int[] current = provinces;
        for (int pass = 0; pass < passes; pass++) {
            int[] src = current;
            int[] next = src.clone();
            IntStream.range(1, h - 1).parallel().forEach(z -> {
                int[] ids = new int[8];
                int[] counts = new int[8];
                for (int x = 1; x < w - 1; x++) {
                    int i = z * w + x;
                    if (src[i] < 0) {
                        continue;
                    }
                    int n = 0;
                    for (int dz = -1; dz <= 1; dz++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int p = src[(z + dz) * w + x + dx];
                            if ((dx == 0 && dz == 0) || p < 0) {
                                continue;
                            }
                            int k = 0;
                            while (k < n && ids[k] != p) {
                                k++;
                            }
                            if (k == n) {
                                ids[n] = p;
                                counts[n++] = 0;
                            }
                            counts[k]++;
                        }
                    }
                    int best = src[i];
                    int bestCount = 0;
                    for (int k = 0; k < n; k++) {
                        if (counts[k] > bestCount || counts[k] == bestCount && ids[k] < best) {
                            best = ids[k];
                            bestCount = counts[k];
                        }
                    }
                    if (bestCount >= 5) {
                        next[i] = best;
                    }
                }
            });
            current = next;
        }
        return current;
    }

    private static void sortByDistance(int[] near, double[] dist, int n) {
        for (int i = 1; i < n; i++) {
            int ci = near[i];
            double d = dist[i];
            int j = i - 1;
            while (j >= 0 && dist[j] > d) {
                near[j + 1] = near[j];
                dist[j + 1] = dist[j];
                j--;
            }
            near[j + 1] = ci;
            dist[j + 1] = d;
        }
    }
}
