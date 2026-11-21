package rafasconquest.peerage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityPlates;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.SeatCities;
import rafasconquest.data.SaveGuard;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.worldgen.CityGridBuilder;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Each city's stockpile in the city manager, and the ground flattened round it ({@link CityPlates}), saved with the
 * world. A flattening order pays its gold at once and holds its workers until its plates are flat: the server levels
 * them a few columns a tick, a plate at a time.
 */
public final class CityStock extends SavedData {
    private static final String NAME = "rafasconquest_city_stock";
    public static final int FLATTEN_GOLD = 4;
    public static final int FLATTEN_WORKERS = 25;
    /** Columns levelled each tick, across all cities. */
    private static final int COLUMNS_PER_TICK = 128;

    public enum Resource {
        POPULATION("Population"), WORKERS("Workers"), HOUSING("Housing"), FOOD("Food"), LUXURY("Luxury goods"),
        RAW("Raw materials"), HIGH_GRADE("High grade materials"), GOLD("Gold");

        public final String label;

        Resource(String label) {
            this.label = label;
        }
    }

    /** shortcut: every city starts with this placeholder stockpile and nothing produces yet; real numbers come with districts. */
    private static final int[] START = {1000, 250, 0, 0, 0, 1000, 0, 200};

    /** One city's: its stockpile (by {@link Resource} ordinal), its flat plates, those still being levelled (first one now). */
    static final class Stock {
        final int[] res = START.clone();
        final Set<Long> flat = new LinkedHashSet<>();
        final List<Long> pending = new ArrayList<>();
        /** Columns of the first pending plate already levelled; not saved, so a restart levels it again from the start. */
        int column;

        int busyWorkers() {
            return pending.size() * FLATTEN_WORKERS;
        }
    }

    private final Map<Integer, Stock> cities = new HashMap<>();
    private static final Map<Integer, CityPlates> PLATES = new HashMap<>();

    static CityStock get(MinecraftServer server) {
        return SaveGuard.get(server, NAME, CityStock::new, CityStock::load);
    }

    private Stock stock(int province) {
        return cities.computeIfAbsent(province, p -> {
            setDirty();
            return new Stock();
        });
    }

    @Nullable
    private static SeatCities.City city(int province) {
        BaseMap map = ConquestRuntime.map();
        return map == null ? null : map.cities().stream().filter(c -> c.province() == province).findFirst().orElse(null);
    }

    /** The plate grid round a city, from the terrain plan that built it. */
    static CityPlates plates(ServerLevel level, SeatCities.City c) {
        return PLATES.computeIfAbsent(c.province(), p -> {
            CityLayout.Site site = new CityLayout.Site(c.x(), c.z(), c.tier().ordinal() >= rafasconquest.core.TitleMap.Tier.KINGDOM.ordinal(), c.districts());
            var chunks = level.getChunkSource();
            Structure.GenerationContext ctx = new Structure.GenerationContext(level.registryAccess(), chunks.getGenerator(),
                    chunks.getGenerator().getBiomeSource(), chunks.randomState(), level.getStructureManager(), level.getSeed(),
                    new ChunkPos(c.x() >> 4, c.z() >> 4), level, b -> true);
            CityTerrain.Plan plan = CityGridBuilder.terrainPlan(ctx, site);
            int ox = site.originX();
            int oz = site.originZ();
            int o = plan.outset() + 1; // one block clear of the wall's foot
            int[] walled = {ox - o, oz - o, ox + plan.size() - 1 + o, oz + plan.size() - 1 + o};
            int[] h = CityLayout.harbourCut(site);
            int[] harbour = h == null ? null : new int[]{ox + h[0], oz + h[1], ox + h[2], oz + h[3]};
            return new CityPlates(ox + plan.boxMin(), oz + plan.boxMin(), plan.boxMax() - plan.boxMin() + 1, plan.plateY(), walled, harbour, c.annex());
        });
    }

    /** A player orders the plates {@code wanted} round the city seating {@code province} flattened: only their own city. */
    public static void flatten(ServerPlayer player, int province, Collection<Long> wanted) {
        SeatCities.City c = city(province);
        if (c == null || Peerage.get(player.server).of(player.getUUID()).hold != province) {
            return;
        }
        CityStock data = get(player.server);
        Stock s = data.stock(province);
        Set<Long> reach = new LinkedHashSet<>(s.flat);
        reach.addAll(s.pending);
        List<Long> ok = plates(player.server.overworld(), c).accept(wanted, reach);
        int n = ok.size();
        int gold = n * FLATTEN_GOLD;
        int workers = n * FLATTEN_WORKERS;
        if (n == 0) {
            player.sendSystemMessage(Component.literal("None of those plates can be flattened: each must touch the city or flat ground."));
        } else if (s.res[Resource.GOLD.ordinal()] < gold) {
            player.sendSystemMessage(Component.literal("Flattening " + n + " plates needs " + gold + " gold."));
        } else if (s.res[Resource.WORKERS.ordinal()] - s.busyWorkers() < workers) {
            player.sendSystemMessage(Component.literal("Flattening " + n + " plates needs " + workers + " free workers."));
        } else {
            s.res[Resource.GOLD.ordinal()] -= gold;
            s.pending.addAll(ok);
            data.setDirty();
        }
        send(player, province);
    }

    /** Levels a few columns of each city's first pending plate in turn; called each server tick. */
    public static void tick(MinecraftServer server) {
        CityStock data = get(server);
        if (data.cities.values().stream().allMatch(s -> s.pending.isEmpty())) {
            return;
        }
        ServerLevel level = server.overworld();
        int budget = COLUMNS_PER_TICK;
        for (Map.Entry<Integer, Stock> e : data.cities.entrySet()) {
            Stock s = e.getValue();
            SeatCities.City c = s.pending.isEmpty() ? null : city(e.getKey());
            if (c == null) {
                continue;
            }
            CityPlates p = plates(level, c);
            long plate = s.pending.get(0);
            int area = CityPlates.PLATE * CityPlates.PLATE;
            while (budget-- > 0 && s.column < area) {
                level(level, p.minX(CityPlates.i(plate)) + s.column % CityPlates.PLATE, p.minZ(CityPlates.j(plate)) + s.column / CityPlates.PLATE, p.plateY());
                s.column++;
            }
            if (s.column >= area) {
                s.column = 0;
                s.pending.remove(0);
                s.flat.add(plate);
                data.setDirty();
                CityManager.viewers(server, e.getKey()).forEach(pl -> send(pl, e.getKey()));
            }
            if (budget <= 0) {
                break;
            }
        }
    }

    /** One column to the plate's height: everything above cleared, gaps below filled with dirt, grass on top. */
    private static void level(ServerLevel level, int x, int z, int plateY) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int y = top; y > plateY; y--) {
            level.setBlock(pos.setY(y), Blocks.AIR.defaultBlockState(), 2);
        }
        // shortcut: the plate is always grass over dirt; a desert or snowy city may want its own ground later.
        level.setBlock(pos.setY(plateY), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
        for (int y = plateY - 1; y > level.getMinBuildHeight(); y--) {
            BlockState state = level.getBlockState(pos.setY(y));
            if (!state.isAir() && !state.liquid() && !state.canBeReplaced()) {
                break;
            }
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
        }
    }

    /** Tells a player viewing the city seating {@code province} its stockpile and plates. */
    public static void send(ServerPlayer player, int province) {
        SeatCities.City c = city(province);
        if (c == null) {
            return;
        }
        CityStock data = get(player.server);
        Stock s = data.stock(province);
        ConquestNetwork.cityState(player, new ConquestNetwork.CityState(province, s.res.clone(), s.busyWorkers(),
                plates(player.server.overworld(), c), new ArrayList<>(s.flat), new ArrayList<>(s.pending)));
    }

    @Override
    public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        CompoundTag all = new CompoundTag();
        cities.forEach((province, s) -> {
            CompoundTag one = new CompoundTag();
            one.putIntArray("res", s.res);
            one.put("flat", new LongArrayTag(new ArrayList<>(s.flat)));
            one.put("pending", new LongArrayTag(s.pending));
            all.put(Integer.toString(province), one);
        });
        tag.put("cities", all);
        return tag;
    }

    private static CityStock load(CompoundTag tag) {
        CityStock data = new CityStock();
        CompoundTag all = tag.getCompound("cities");
        for (String key : all.getAllKeys()) {
            CompoundTag one = all.getCompound(key);
            Stock s = new Stock();
            int[] res = one.getIntArray("res");
            System.arraycopy(res, 0, s.res, 0, Math.min(res.length, s.res.length));
            for (long k : one.getLongArray("flat")) {
                s.flat.add(k);
            }
            for (long k : one.getLongArray("pending")) {
                s.pending.add(k);
            }
            data.cities.put(Integer.parseInt(key), s);
        }
        return data;
    }

    /** Cached grids are for this world only. */
    public static void clear() {
        PLATES.clear();
    }
}
