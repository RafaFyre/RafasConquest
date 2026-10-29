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
import rafasconquest.battle.GroundPlan;

import java.util.HashMap;
import java.util.Map;

/**
 * The lie of the land for deploying, read on a grid of {@code step} blocks without loading or generating anything:
 * where a chunk is loaded, from the world (the ground's height, water at the top, a canopy of leaves for a wood);
 * elsewhere, from the world generator (its height, water above the sea floor, and a forest, taiga or jungle biome).
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

    private Cell read(int x, int z) {
        if (level.hasChunk(x >> 4, z >> 4)) {
            int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            boolean water = !level.getBlockState(new BlockPos(x, top - 1, z)).getFluidState().isEmpty();
            int canopy = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            return new Cell(top, water, canopy - top >= CANOPY);
        }
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
