package rafasconquest.map;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.stream.IntStream;

/**
 * Draws the base map from the seed alone: terrain height from the noise generator (sampled every few pixels and
 * smoothed between), the biome at the surface for colour, water shaded by depth and land lit from the north-west.
 * Nothing is generated or loaded, so the whole area inside the storm wall is known at once. Runs off the server thread.
 */
public final class BaseMapGenerator {
    /** Finest detail: one pixel per 16 blocks (a chunk). */
    static final int MIN_BLOCKS_PER_PIXEL = 16;
    /** Largest map side in pixels; a bigger storm wall gets coarser pixels instead. */
    static final int MAX_SIZE = 1024;
    /** Heights are sampled every this many pixels; they change slowly and each sample is costly. */
    static final int HEIGHT_STEP = 4;

    private BaseMapGenerator() {
    }

    public static BaseMap generate(ServerLevel level, int visibleRadius) {
        int bpp = Math.max(MIN_BLOCKS_PER_PIXEL, (2 * visibleRadius + MAX_SIZE - 1) / MAX_SIZE);
        int size = (2 * visibleRadius + bpp - 1) / bpp;
        int minX = -size * bpp / 2;
        int minZ = -size * bpp / 2;
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        RandomState random = level.getChunkSource().randomState();
        BiomeSource biomes = generator.getBiomeSource();
        Climate.Sampler sampler = random.sampler();
        int sea = generator.getSeaLevel();
        long reach = (long) (visibleRadius + 2L * HEIGHT_STEP * bpp) * (visibleRadius + 2L * HEIGHT_STEP * bpp);

        // Coarse heights, one sample every HEIGHT_STEP pixels (plus a border for the smoothing and the shading).
        int hw = size / HEIGHT_STEP + 3;
        int[] heights = new int[hw * hw];
        IntStream.range(0, hw).parallel().forEach(j -> {
            for (int i = 0; i < hw; i++) {
                int x = minX + (i - 1) * HEIGHT_STEP * bpp;
                int z = minZ + (j - 1) * HEIGHT_STEP * bpp;
                heights[j * hw + i] = (long) x * x + (long) z * z <= reach
                        ? generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level, random)
                        : sea;
            }
        });

        long visible2 = (long) visibleRadius * visibleRadius;
        int[] pixels = new int[size * size];
        IntStream.range(0, size).parallel().forEach(pz -> {
            for (int px = 0; px < size; px++) {
                int x = minX + px * bpp + bpp / 2;
                int z = minZ + pz * bpp + bpp / 2;
                if ((long) x * x + (long) z * z > visible2) {
                    continue; // beyond the storm wall: left transparent
                }
                float h = height(heights, hw, px, pz);
                Holder<Biome> biome = biomes.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(Math.max((int) h, sea)),
                        QuartPos.fromBlock(z), sampler);
                int color = BiomePalette.color(biome, (int) h, sea);
                if (h >= sea && !BiomePalette.isOcean(biome)) {
                    // Light from the north-west: slopes facing it brighter, slopes away darker; high ground a little paler.
                    // Measured across a full height-sample step, so the smoothing between samples doesn't show as facets.
                    float slope = height(heights, hw, px - HEIGHT_STEP / 2, pz - HEIGHT_STEP / 2)
                            - height(heights, hw, px + HEIGHT_STEP / 2, pz + HEIGHT_STEP / 2);
                    float light = 1 + Math.max(-0.25F, Math.min(0.25F, slope * 0.02F));
                    color = BiomePalette.shade(color, light);
                    color = BiomePalette.lerp(color, 0xE8E8E8, Math.max(0, Math.min(0.35F, (h - sea - 60) / 160F)));
                }
                pixels[pz * size + px] = 0xFF000000 | color;
            }
        });
        return new BaseMap(minX, minZ, bpp, size, size, visibleRadius, pixels);
    }

    /** Height at a pixel, smoothed (bilinear) between the coarse samples. */
    private static float height(int[] heights, int hw, int px, int pz) {
        float gx = (float) px / HEIGHT_STEP + 1;
        float gz = (float) pz / HEIGHT_STEP + 1;
        int i = Math.max(0, Math.min(hw - 2, (int) Math.floor(gx)));
        int j = Math.max(0, Math.min(hw - 2, (int) Math.floor(gz)));
        float fx = Math.max(0, Math.min(1, gx - i));
        float fz = Math.max(0, Math.min(1, gz - j));
        float top = heights[j * hw + i] * (1 - fx) + heights[j * hw + i + 1] * fx;
        float bottom = heights[(j + 1) * hw + i] * (1 - fx) + heights[(j + 1) * hw + i + 1] * fx;
        return top * (1 - fz) + bottom * fz;
    }
}
