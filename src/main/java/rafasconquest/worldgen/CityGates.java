package rafasconquest.worldgen;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import rafasconquest.content.GateBlockEntity;
import rafasconquest.core.CityLayout;
import rafasconquest.core.SeatCities;

/**
 * Doors for every city gate. A city's walls come from templates (the mod's own, or a style's), so the gate's opening
 * isn't known in code: once a player comes near a gate and its ground is loaded, the opening is found where the gate
 * avenue passes under the wall, filled with two oak doors (the left, looking out, the wider), and a Gate Block set just
 * inside. Each gate is done once, kept in the world's saved data.
 */
public final class CityGates {
    /** How near a player comes before a gate is fitted. */
    private static final int NEAR = 96;
    /** How far in from the city's edge, and out past it, the wall is looked for along the avenue. */
    private static final int IN = 14;
    private static final int OUT = 16;
    /** The most an arch's ceiling may stand above the road, and the widest and tallest opening taken for a gate. */
    private static final int ARCH = 10;
    private static final int MAX_WIDTH = 9;
    private static final int MAX_HEIGHT = 8;

    private CityGates() {
    }

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % 100 != 0 || level.players().isEmpty()) {
            return;
        }
        Done done = Done.get(level);
        for (ServerPlayer player : level.players()) {
            int px = player.getBlockX();
            int pz = player.getBlockZ();
            for (SeatCities.City city : ConquestRuntime.cities().inBox(px - NEAR, pz - NEAR, px + NEAR, pz + NEAR)) {
                for (Direction out : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
                    BlockPos gate = gate(city, out);
                    long key = gate.asLong();
                    if (!done.has(key) && Math.hypot(gate.getX() - px, gate.getZ() - pz) <= NEAR
                            && level.hasChunksAt(gate.getX() - OUT - IN - 8, gate.getZ() - OUT - IN - 8, gate.getX() + OUT + IN + 8, gate.getZ() + OUT + IN + 8)) {
                        fit(level, city, out);
                        done.add(key);
                    }
                }
            }
        }
    }

    /** Fits every gate of {@code city} not yet fitted, loading its chunks first: for a siege begun from afar. */
    public static void fitAll(ServerLevel level, SeatCities.City city) {
        Done done = Done.get(level);
        for (Direction out : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
            BlockPos gate = gate(city, out);
            if (done.has(gate.asLong())) {
                continue;
            }
            int r = OUT + IN + 8;
            for (int cx = (gate.getX() - r) >> 4; cx <= (gate.getX() + r) >> 4; cx++) {
                for (int cz = (gate.getZ() - r) >> 4; cz <= (gate.getZ() + r) >> 4; cz++) {
                    level.getChunk(cx, cz);
                }
            }
            fit(level, city, out);
            done.add(gate.asLong());
        }
    }

    /** Where the gate avenue meets the city's edge on side {@code out} (y 0). */
    static BlockPos gate(SeatCities.City city, Direction out) {
        int half = city.size() / 2;
        int blocks = city.districts() * CityLayout.BLOCKS_PER_CELL;
        int along = -half + CityLayout.roadStart(blocks / 2) + 1; // the gate road's middle, from the centre (as Roads.gate)
        return out.getAxis() == Direction.Axis.X
                ? new BlockPos(city.x() + out.getStepX() * half, 0, city.z() + along)
                : new BlockPos(city.x() + along, 0, city.z() + out.getStepZ() * half);
    }

    /** Finds the gate's arch on side {@code out} and fits it with doors and a Gate Block; nothing if no arch is found. */
    private static void fit(ServerLevel level, SeatCities.City city, Direction out) {
        BlockPos edge = gate(city, out);
        Direction side = out.getClockWise(); // along the wall
        // The road's height, a little inside, where nothing stands over it.
        int y = Integer.MAX_VALUE;
        for (int k = -1; k <= 1; k++) {
            BlockPos p = edge.relative(out, -IN).relative(side, k);
            y = Math.min(y, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ()));
        }
        // The arch: road-level air with a roof over it. The doors go in its outer end; the Gate Block inside its inner end.
        int outer = Integer.MIN_VALUE;
        int inner = Integer.MIN_VALUE;
        for (int t = OUT; t >= -IN; t--) {
            boolean arch = arch(level, edge.relative(out, t).atY(y));
            if (arch && outer == Integer.MIN_VALUE) {
                outer = t;
            }
            if (outer != Integer.MIN_VALUE) {
                if (!arch) {
                    break;
                }
                inner = t;
            }
        }
        if (outer == Integer.MIN_VALUE) {
            return;
        }
        BlockPos mouth = edge.relative(out, outer).atY(y);
        int lo = 0;
        while (lo > -MAX_WIDTH && level.getBlockState(mouth.relative(side, lo - 1)).isAir()) {
            lo--;
        }
        int hi = 0;
        while (hi < MAX_WIDTH && level.getBlockState(mouth.relative(side, hi + 1)).isAir()) {
            hi++;
        }
        int height = 0;
        while (height < MAX_HEIGHT && level.getBlockState(mouth.above(height)).isAir()) {
            height++;
        }
        int width = hi - lo + 1;
        if (width < 2 || height < 2 || width > MAX_WIDTH) {
            return;
        }
        // Looking out, `side` (clockwise of out) is to the right: the left door takes the low end, the Gate Block goes past the right.
        int leftWidth = (width + 1) / 2;
        int[] left = {lo, lo + leftWidth - 1};
        int[] right = {lo + leftWidth, hi};
        BlockPos gateBlock = edge.relative(out, inner - 1).atY(y).relative(side, hi + 1);
        GateBlockEntity.install(level, BoundingBox.infinite(), gateBlock, out.getOpposite(),
                door(mouth, side, left, height), door(mouth, side, right, height));
    }

    private static BoundingBox door(BlockPos mouth, Direction side, int[] span, int height) {
        return BoundingBox.fromCorners(mouth.relative(side, span[0]), mouth.relative(side, span[1]).above(height - 1));
    }

    /** Whether a column is open at road level with a roof within {@link #ARCH} blocks above. */
    private static boolean arch(ServerLevel level, BlockPos road) {
        if (!level.getBlockState(road).isAir() || !level.getBlockState(road.above()).isAir()) {
            return false;
        }
        for (int h = 2; h <= ARCH; h++) {
            if (!level.getBlockState(road.above(h)).isAir()) {
                return true;
            }
        }
        return false;
    }

    /** The gates already fitted (or looked at and found to have none), by their edge position. */
    static final class Done extends SavedData {
        private static final String NAME = "rafasconquest_city_gates";
        private final LongOpenHashSet keys = new LongOpenHashSet();

        static Done get(ServerLevel level) {
            return level.getServer().overworld().getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(Done::new, (tag, registries) -> load(tag)), NAME);
        }

        private static Done load(CompoundTag tag) {
            Done done = new Done();
            for (long k : tag.getLongArray("gates")) {
                done.keys.add(k);
            }
            return done;
        }

        boolean has(long key) {
            return keys.contains(key);
        }

        void add(long key) {
            keys.add(key);
            setDirty();
        }

        @Override
        public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
            tag.put("gates", new LongArrayTag(keys.toLongArray()));
            return tag;
        }
    }
}
