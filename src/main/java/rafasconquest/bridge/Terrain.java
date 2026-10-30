package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import rafasconquest.RafasConquest;
import rafasconquest.battle.GroundPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

/**
 * The lie of the land for deploying, read on a grid of {@code step} blocks without loading or generating anything:
 * where a chunk is loaded, from the world (the ground's height, water at the top, a canopy of leaves for a wood);
 * elsewhere from the world map (its ground and its sea and rivers), and on the shores, where the map is too coarse to
 * tell, from the world generator (its height, water above the sea floor). Woods are a forest, taiga or jungle biome.
 */
final class Terrain implements GroundPlan.Ground {
    private record Cell(double height, boolean water, boolean forest) {
    }

    /** Leaves at least this far above the ground make a wood. */
    private static final int CANOPY = 3;

    private final ServerLevel level;
    private final int step;
    private final Map<Long, Cell> cells = new HashMap<>();

    Terrain(ServerLevel level, int step) {
        this.level = level;
        this.step = step;
    }

    /** A grid fine enough for a battle of this many soldiers, and coarse enough to read quickly. */
    static int stepFor(int soldiers) {
        return soldiers <= 4000 ? 4 : soldiers <= 20_000 ? 6 : 8;
    }

    @Override
    public double height(double x, double z) {
        return cell(x, z).height();
    }

    @Override
    public boolean water(double x, double z) {
        return cell(x, z).water();
    }

    @Override
    public boolean forest(double x, double z) {
        return cell(x, z).forest();
    }

    private Cell cell(double x, double z) {
        int cx = Mth.floor(x / step);
        int cz = Mth.floor(z / step);
        return cells.computeIfAbsent(((long) cx << 32) ^ (cz & 0xffffffffL), k -> read(cx * step + step / 2, cz * step + step / 2));
    }

    /** Reads every cell of an area not read yet, all here: the rest of a survey ({@link #surveyInBackground}). */
    @Override
    public void survey(double minX, double minZ, double maxX, double maxZ) {
        List<long[]> unloaded = readLoaded(minX, minZ, maxX, maxZ);
        for (long[] c : unloaded) {
            cells.put(c[0], generated((int) c[1], (int) c[2]));
        }
    }

    /**
     * Surveys an area ({minX, minZ, maxX, maxZ}) for a deployment: its loaded cells from the world, now, on the server
     * thread; the rest from the world generator, off it and across the cores (it needs nothing of the world, and is safe
     * there, as world generation itself runs there), so the server keeps running meanwhile. Completes on the server
     * thread, with every cell read.
     */
    CompletableFuture<Void> surveyInBackground(double[] area) {
        long started = System.nanoTime();
        List<long[]> unloaded = area[0] <= area[2] ? readLoaded(area[0], area[1], area[2], area[3]) : List.of();
        int loaded = cells.size();
        return CompletableFuture.supplyAsync(() -> unloaded.parallelStream().map(c -> generated((int) c[1], (int) c[2])).toArray(Cell[]::new))
                .thenAcceptAsync(generated -> {
                    for (int i = 0; i < generated.length; i++) {
                        cells.put(unloaded.get(i)[0], generated[i]);
                    }
                    RafasConquest.LOGGER.info("[battle] surveyed the ground in the background: {} cells from the world and the map, {} "
                            + "shore cells from the world generator, in {} ms", loaded, generated.length, (System.nanoTime() - started) / 1_000_000);
                }, level.getServer());
    }

    /**
     * Reads an area's cells not read yet that are quick to read (the loaded ones from the world, and the ones the world
     * map settles); returns the rest (key, x, z), the shores, for the world generator.
     */
    private List<long[]> readLoaded(double minX, double minZ, double maxX, double maxZ) {
        int x0 = Mth.floor(minX / step);
        int z0 = Mth.floor(minZ / step);
        int x1 = Mth.floor(maxX / step);
        int z1 = Mth.floor(maxZ / step);
        List<long[]> unloaded = new ArrayList<>();
        for (int cx = x0; cx <= x1; cx++) {
            for (int cz = z0; cz <= z1; cz++) {
                long key = ((long) cx << 32) ^ (cz & 0xffffffffL);
                int x = cx * step + step / 2;
                int z = cz * step + step / 2;
                if (cells.containsKey(key)) {
                    continue;
                }
                Cell quick = level.hasChunk(x >> 4, z >> 4) ? read(x, z) : fromMap(x, z);
                if (quick != null) {
                    cells.put(key, quick);
                } else {
                    unloaded.add(new long[]{key, x, z});
                }
            }
        }
        return unloaded;
    }

    private Cell read(int x, int z) {
        if (level.hasChunk(x >> 4, z >> 4)) {
            int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            boolean water = !level.getBlockState(new BlockPos(x, top - 1, z)).getFluidState().isEmpty();
            int canopy = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            return new Cell(top, water, canopy - top >= CANOPY);
        }
        Cell mapped = fromMap(x, z);
        return mapped != null ? mapped : generated(x, z);
    }

    /**
     * A cell as the world map tells it, where it can: away from any shore, the map's water (sea and river) settles
     * whether it is wet, and its ground (blended between pixels) the height. Null on a shore (where land and water meet
     * within a pixel either way), or off the map: those need the world generator.
     */
    @Nullable
    private Cell fromMap(int x, int z) {
        BaseMap map = ConquestRuntime.map();
        int i = map == null ? -1 : map.pixelAt(x, z);
        if (i < 0) {
            return null;
        }
        int bpp = map.blocksPerPixel();
        for (int dx = -bpp; dx <= bpp; dx += bpp) {
            for (int dz = -bpp; dz <= bpp; dz += bpp) {
                int j = map.pixelAt(x + dx, z + dz);
                if (j < 0 || map.wet()[j] != map.wet()[i]) {
                    return null;
                }
            }
        }
        // The ground, blended between the four pixel centres around (x, z).
        double gx = (x - map.minX()) / (double) bpp - 0.5;
        double gz = (z - map.minZ()) / (double) bpp - 0.5;
        int px = Mth.clamp(Mth.floor(gx), 0, map.width() - 2);
        int pz = Mth.clamp(Mth.floor(gz), 0, map.height() - 2);
        double fx = Mth.clamp(gx - px, 0, 1);
        double fz = Mth.clamp(gz - pz, 0, 1);
        int w = map.width();
        int[] g = map.ground();
        double height = Mth.lerp(fz, Mth.lerp(fx, g[pz * w + px], g[pz * w + px + 1]),
                Mth.lerp(fx, g[(pz + 1) * w + px], g[(pz + 1) * w + px + 1]));
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        Holder<Biome> biome = generator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock((int) height),
                QuartPos.fromBlock(z), level.getChunkSource().randomState().sampler());
        boolean forest = biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.IS_JUNGLE);
        return new Cell(height, map.wet()[i], forest);
    }

    private Cell generated(int x, int z) {
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        RandomState random = level.getChunkSource().randomState();
        int floor = generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level, random);
        int surface = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level, random);
        Holder<Biome> biome = generator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(floor),
                QuartPos.fromBlock(z), random.sampler());
        boolean forest = biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.IS_JUNGLE);
        return new Cell(floor, surface > floor, forest);
    }
}
