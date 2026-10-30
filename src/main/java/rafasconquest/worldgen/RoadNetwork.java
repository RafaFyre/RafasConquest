package rafasconquest.worldgen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import rafasconquest.core.Roads;
import rafasconquest.map.BaseMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The world map's roads, ready for building into the world ({@link RoadFeature}): which stretches cross each chunk,
 * and each road's height profile, worked out from the world generator along its whole length the first time any chunk
 * needs it (so every chunk agrees where the road runs, and no seams show):
 * <ul>
 *   <li>The ground is read every {@link #STEP} blocks along the road.</li>
 *   <li>Where water stands over it (a river), or it falls {@link #RAVINE_DEPTH} or more below the land about it (a
 *       ravine), the road is a <b>bridge</b>: its deck flat across, above the water or level with the far banks.</li>
 *   <li>The road's height is the ground's (or the deck's) smoothed over {@link #SMOOTH} samples each way, so it rises
 *       and falls gently: half a block at a time, which the slabs make.</li>
 * </ul>
 */
public final class RoadNetwork {
    /** Blocks between the ground's readings along a road. */
    public static final int STEP = 4;
    static final int SMOOTH = 3;
    /** The land about a spot: the ground averaged this many samples each way. */
    static final int AROUND = 12;
    static final int RAVINE_DEPTH = 8;
    /** A bridge's deck stands this far above the water. */
    static final int ABOVE_WATER = 2;

    /** A road's height along it, every {@link #STEP} blocks, and where it is a bridge. */
    public record Profile(float[] height, boolean[] bridge) {
        public float heightAt(double along) {
            double f = along / STEP;
            int i = (int) Math.max(0, Math.min(height.length - 1, Math.floor(f)));
            int j = Math.min(height.length - 1, i + 1);
            return (float) (height[i] + (height[j] - height[i]) * Math.max(0, Math.min(1, f - i)));
        }

        public boolean bridgeAt(double along) {
            int i = (int) Math.max(0, Math.min(bridge.length - 1, Math.round(along / STEP)));
            return bridge[i];
        }
    }

    private static volatile RoadNetwork current;

    private final BaseMap map;
    /** For each chunk, the stretches crossing it: {road, segment}. */
    private final Map<Long, List<int[]>> byChunk = new HashMap<>();
    /** Each road's length up to each of its points. */
    private final double[][] along;
    private final Map<Integer, Profile> profiles = new ConcurrentHashMap<>();

    private RoadNetwork(BaseMap map) {
        this.map = map;
        List<Roads.Road> roads = map.roads().roads();
        along = new double[roads.size()][];
        for (int r = 0; r < roads.size(); r++) {
            Roads.Road road = roads.get(r);
            double[] cum = new double[road.points()];
            int reach = road.grade().width() / 2 + 2;
            for (int k = 1; k < road.points(); k++) {
                cum[k] = cum[k - 1] + Math.hypot(road.xs()[k] - road.xs()[k - 1], road.zs()[k] - road.zs()[k - 1]);
                int cx0 = (Math.min(road.xs()[k - 1], road.xs()[k]) - reach) >> 4;
                int cx1 = (Math.max(road.xs()[k - 1], road.xs()[k]) + reach) >> 4;
                int cz0 = (Math.min(road.zs()[k - 1], road.zs()[k]) - reach) >> 4;
                int cz1 = (Math.max(road.zs()[k - 1], road.zs()[k]) + reach) >> 4;
                for (int cx = cx0; cx <= cx1; cx++) {
                    for (int cz = cz0; cz <= cz1; cz++) {
                        byChunk.computeIfAbsent(key(cx, cz), c -> new ArrayList<>()).add(new int[]{r, k - 1});
                    }
                }
            }
            along[r] = cum;
        }
    }

    /** The network for the world's map (made once per map). */
    public static RoadNetwork of(BaseMap map) {
        RoadNetwork network = current;
        if (network == null || network.map != map) {
            synchronized (RoadNetwork.class) {
                network = current;
                if (network == null || network.map != map) {
                    network = new RoadNetwork(map);
                    current = network;
                }
            }
        }
        return network;
    }

    public BaseMap map() {
        return map;
    }

    public List<int[]> inChunk(int chunkX, int chunkZ) {
        return byChunk.getOrDefault(key(chunkX, chunkZ), List.of());
    }

    /** How far along road {@code r} its point {@code k} is. */
    public double along(int r, int k) {
        return along[r][k];
    }

    /** Road {@code r}'s profile, worked out on first asking. */
    public Profile profile(int r, ServerLevel level) {
        return profiles.computeIfAbsent(r, i -> work(map.roads().roads().get(i), along[i], level));
    }

    private static Profile work(Roads.Road road, double[] cum, ServerLevel level) {
        double length = cum[cum.length - 1];
        int n = (int) Math.ceil(length / STEP) + 1;
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        RandomState random = level.getChunkSource().randomState();
        float[] ground = new float[n];
        float[] water = new float[n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            double s = Math.min(length, i * STEP);
            while (k < cum.length - 2 && cum[k + 1] < s) {
                k++;
            }
            double seg = cum[k + 1] - cum[k];
            double f = seg == 0 ? 0 : (s - cum[k]) / seg;
            int x = (int) Math.floor(road.xs()[k] + (road.xs()[k + 1] - road.xs()[k]) * f);
            int z = (int) Math.floor(road.zs()[k] + (road.zs()[k + 1] - road.zs()[k]) * f);
            ground[i] = generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level, random);
            water[i] = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level, random);
        }
        // Rivers and ravines: spans to bridge, each with its deck's height.
        boolean[] bridge = new boolean[n];
        for (int i = 0; i < n; i++) {
            bridge[i] = water[i] > ground[i] || ground[i] < average(ground, i, AROUND) - RAVINE_DEPTH;
        }
        float[] target = ground.clone();
        for (int i = 0; i < n; ) {
            if (!bridge[i]) {
                i++;
                continue;
            }
            int a = i;
            while (i < n && bridge[i]) {
                i++;
            }
            int b = i - 1;
            // The banks: one sample out each way, included in the bridge.
            int left = Math.max(0, a - 1);
            int right = Math.min(n - 1, b + 1);
            float deck = Math.max(ground[left], ground[right]);
            for (int j = a; j <= b; j++) {
                if (water[j] > ground[j]) {
                    deck = Math.max(deck, water[j] + ABOVE_WATER);
                }
            }
            for (int j = left; j <= right; j++) {
                bridge[j] = true;
                target[j] = deck;
            }
        }
        float[] height = new float[n];
        for (int i = 0; i < n; i++) {
            height[i] = bridge[i] ? target[i] : average(target, i, SMOOTH);
        }
        return new Profile(height, bridge);
    }

    private static float average(float[] values, int i, int reach) {
        float sum = 0;
        int count = 0;
        for (int j = Math.max(0, i - reach); j <= Math.min(values.length - 1, i + reach); j++) {
            sum += values[j];
            count++;
        }
        return sum / count;
    }

    private static long key(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) ^ (chunkZ & 0xffffffffL);
    }
}
