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
import rafasconquest.style.CityStyles;
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

    public static final int DISTRICT_GOLD = 40;
    public static final int DISTRICT_RAW = 200;

    /** What a district is for; its buildings come from its style's {@code city/districts/<name>} folder. */
    public enum District {
        HOUSING("Housing", 0xC08A50), FOOD("Food", 0x78B040), EXTRACTION("Extraction", 0x9A9A9A), INDUSTRIAL("Industrial", 0xB85A30),
        LOGISTICS("Logistics", 0x4A88C8);

        public final String label;
        public final int rgb;

        District(String label, int rgb) {
            this.label = label;
            this.rgb = rgb;
        }

        public String slot() {
            return "city/districts/" + name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    /** shortcut: every city starts with this placeholder stockpile and nothing produces yet; real numbers come with districts. */
    private static final int[] START = {1000, 250, 0, 0, 0, 1000, 0, 200};

    /** One city's: its stockpile (by {@link Resource} ordinal), its flat plates, those still being levelled (first one now). */
    static final class Stock {
        final int[] res = START.clone();
        final Set<Long> flat = new LinkedHashSet<>();
        final List<Long> pending = new ArrayList<>();
        /** Districts by plate, as {@link District} ordinals. */
        final Map<Long, Integer> districts = new java.util.LinkedHashMap<>();
        /** Main roads, {x1, z1, x2, z2} ({@link CityPlates}). */
        final List<int[]> roads = new ArrayList<>();
        /** Special buildings put down: their files, footprints {minX, minZ, maxX, maxZ} and quarter turns. */
        final List<Special> specials = new ArrayList<>();

        List<int[]> boxes() {
            return specials.stream().map(Special::box).toList();
        }
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
            CityLayout.Site site = site(c);
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

    /**
     * A player orders districts of one kind on the plates {@code wanted} round the city seating {@code province}: flat
     * plates touching the city or a district. Each is paid for and built at once.
     */
    public static void placeDistricts(ServerPlayer player, int province, int kind, Collection<Long> wanted) {
        SeatCities.City c = city(province);
        if (c == null || kind < 0 || kind >= District.values().length || Peerage.get(player.server).of(player.getUUID()).hold != province) {
            return;
        }
        CityStock data = get(player.server);
        Stock s = data.stock(province);
        ServerLevel level = player.server.overworld();
        CityPlates p = plates(level, c);
        Set<Long> taken = new java.util.HashSet<>(s.districts.keySet());
        taken.addAll(p.platesUnder(s.boxes())); // no district over a special building
        List<Long> ok = p.acceptDistricts(wanted, s.flat, taken, p.roadPlates(s.roads));
        int n = ok.size();
        if (n == 0) {
            player.sendSystemMessage(Component.literal("A district needs flat ground touching the city, another district or a main road."));
        } else if (s.res[Resource.GOLD.ordinal()] < n * DISTRICT_GOLD || s.res[Resource.RAW.ordinal()] < n * DISTRICT_RAW) {
            player.sendSystemMessage(Component.literal(n + " districts need " + n * DISTRICT_GOLD + " gold and " + n * DISTRICT_RAW + " raw materials."));
        } else {
            s.res[Resource.GOLD.ordinal()] -= n * DISTRICT_GOLD;
            s.res[Resource.RAW.ordinal()] -= n * DISTRICT_RAW;
            District d = District.values()[kind];
            CityLayout.Site site = site(c);
            boolean built = false;
            for (long k : ok) {
                s.districts.put(k, kind);
                built |= build(level, site, p, k, d, s.roads);
            }
            data.setDirty();
            if (!built) {
                player.sendSystemMessage(Component.literal("No buildings in this city's style for " + d.label
                        + " districts yet: add .nbt files to structures/style/<style>/" + d.slot() + "."));
            }
        }
        send(player, province);
    }

    private static CityLayout.Site site(SeatCities.City c) {
        return new CityLayout.Site(c.x(), c.z(), c.tier().ordinal() >= rafasconquest.core.TitleMap.Tier.KINGDOM.ordinal(), c.districts());
    }

    /** Fills a district's plate with its style's buildings, packed as a city block's houses are; false if it has none. */
    private static boolean build(ServerLevel level, CityLayout.Site site, CityPlates p, long plate, District d, List<int[]> roads) {
        List<rafasconquest.style.CityStyles.Building> buildings = rafasconquest.style.CityStyles.buildings(site, d.slot()).stream()
                .filter(b -> b.x() <= CityPlates.PLATE && b.z() <= CityPlates.PLATE).toList();
        if (buildings.isEmpty()) {
            return false;
        }
        List<rafasconquest.style.BlockPacker.Size> sizes = buildings.stream().map(b -> new rafasconquest.style.BlockPacker.Size(b.x(), b.z())).toList();
        java.util.Random random = new java.util.Random(level.getSeed() ^ plate * 0x9E3779B97F4A7C15L);
        int x0 = p.minX(CityPlates.i(plate));
        int z0 = p.minZ(CityPlates.j(plate));
        for (rafasconquest.style.BlockPacker.Placed b : rafasconquest.style.BlockPacker.pack(free(x0, z0, roads), CityPlates.PLATE, CityPlates.PLATE, sizes, random)) {
            put(level, buildings.get(b.building()).file(), x0 + b.x(), p.plateY(), z0 + b.z(), b.turns());
        }
        return true;
    }

    /** Builds a style file in the live world, its footprint's corner at (minX, minZ), its floor at y. */
    private static void put(ServerLevel level, String file, int minX, int y, int minZ, int turns) {
        for (rafasconquest.style.StylePiece piece : rafasconquest.style.StylePiece.at(file, minX, y, minZ, turns)) {
            net.minecraft.world.level.levelgen.structure.BoundingBox box = piece.getBoundingBox();
            piece.postProcess(level, level.structureManager(), level.getChunkSource().getGenerator(), level.getRandom(), box,
                    new ChunkPos(box.minX() >> 4, box.minZ() >> 4), box.getCenter());
        }
    }

    /** Where a city's special buildings come from: its style's monuments (else the default style's). */
    public static final String SPECIALS = "city/buildings/landmarks/style/monuments";

    /** A special building put down: its file, footprint {minX, minZ, maxX, maxZ} and quarter turns. */
    record Special(String file, int[] box, int turns) {
    }

    private static List<CityStyles.Building> specialKinds(CityLayout.Site site) {
        return CityStyles.buildings(site, SPECIALS);
    }

    /**
     * A player puts down special buildings round the city seating {@code province}, each centred on its spot and
     * checked beside those before it. Free for now.
     */
    public static void placeSpecials(ServerPlayer player, int province, List<ConquestNetwork.SpecialOrder> wanted) {
        SeatCities.City c = city(province);
        if (c == null || Peerage.get(player.server).of(player.getUUID()).hold != province) {
            return;
        }
        CityStock data = get(player.server);
        Stock s = data.stock(province);
        ServerLevel level = player.server.overworld();
        CityPlates p = plates(level, c);
        List<CityStyles.Building> kinds = specialKinds(site(c));
        for (ConquestNetwork.SpecialOrder o : wanted) {
            CityStyles.Building b = kinds.stream().filter(k -> k.file().equals(o.file())).findFirst().orElse(null);
            int turns = Math.floorMod(o.turns(), 4);
            int[] box = b == null ? null : CityPlates.footprint(o.x(), o.z(), b.x(), b.z(), turns);
            String problem = box == null ? "That building is not in this city's style" : p.specialProblem(box, s.flat, s.districts.keySet(), s.roads, s.boxes());
            if (problem != null) {
                player.sendSystemMessage(Component.literal(problem + "."));
                break;
            }
            s.specials.add(new Special(b.file(), box, turns));
            put(level, b.file(), box[0], p.plateY(), box[1], turns);
            data.setDirty();
        }
        send(player, province);
    }

    /** A plate's room for buildings: whole, less the 4-block squares a road runs through (and a gap round them). */
    private static List<rafasconquest.style.BlockPacker.Rect> free(int x0, int z0, List<int[]> roads) {
        List<rafasconquest.style.BlockPacker.Rect> free = List.of(new rafasconquest.style.BlockPacker.Rect(0, 0, CityPlates.PLATE, CityPlates.PLATE));
        Set<Long> cut = new java.util.HashSet<>();
        for (int[] r : roads) {
            for (int[] c : CityPlates.columns(r)) {
                int lx = c[0] - x0;
                int lz = c[1] - z0;
                if (lx >= 0 && lz >= 0 && lx < CityPlates.PLATE && lz < CityPlates.PLATE && cut.add(CityPlates.key(lx / 4, lz / 4))) {
                    free = rafasconquest.style.BlockPacker.cut(free, new rafasconquest.style.BlockPacker.Rect(lx / 4 * 4, lz / 4 * 4, 4, 4));
                }
            }
        }
        return free;
    }

    /** A player lays main roads round the city seating {@code province}, each checked beside those before it. Free for now. */
    public static void placeRoads(ServerPlayer player, int province, List<int[]> wanted) {
        SeatCities.City c = city(province);
        if (c == null || Peerage.get(player.server).of(player.getUUID()).hold != province) {
            return;
        }
        CityStock data = get(player.server);
        Stock s = data.stock(province);
        ServerLevel level = player.server.overworld();
        CityPlates p = plates(level, c);
        for (int[] r : wanted) {
            String problem = r.length != 4 ? "Not a road" : p.roadProblem(r, s.roads, s.districts.keySet(), s.boxes());
            if (problem != null) {
                player.sendSystemMessage(Component.literal(problem + "."));
                break;
            }
            s.roads.add(r.clone());
            lay(level, p, s, r);
            data.setDirty();
        }
        send(player, province);
    }

    /**
     * Lays a road on the land: 5 wide in cobblestone, climbing in {@link CityPlates#STEP}-block tiles along its length
     * (4 full blocks, then 4 slabs for the half step to the next tile), at the city's height over its box and flat
     * plates; cleared 4 high above, propped up to 12 deep below.
     * shortcut: over water it is a causeway, not a bridge.
     */
    private static void lay(ServerLevel level, CityPlates p, Stock s, int[] r) {
        int n = (int) Math.ceil(CityPlates.length(r));
        int[] h = new int[n + 1];
        boolean[] fixed = new boolean[n + 1];
        for (int k = 0; k <= n; k++) {
            double t = n == 0 ? 0 : (double) k / n;
            int x = (int) Math.round(r[0] + t * (r[2] - r[0]));
            int z = (int) Math.round(r[1] + t * (r[3] - r[1]));
            fixed[k] = p.inBoxArea(x, z) || s.flat.contains(p.at(x, z));
            h[k] = fixed[k] ? p.plateY() : level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
        }
        int[] steps = CityPlates.steps(h, fixed, p.plateY());
        BlockState slab = Blocks.COBBLESTONE_SLAB.defaultBlockState();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int[] col : CityPlates.columns(r)) {
            int[] top = CityPlates.surface(steps, CityPlates.along(col[0], col[1], r));
            int y = top[0];
            level.setBlock(pos.set(col[0], y, col[1]), top[1] == 1 ? slab : Blocks.COBBLESTONE.defaultBlockState(), 2);
            if (top[1] == 1) {
                level.setBlock(pos.setY(y - 1), Blocks.COBBLESTONE.defaultBlockState(), 2);
                y--; // propped up from under the block beneath the slab
            }
            for (int up = top[0] - y + 1; up <= top[0] - y + 4; up++) {
                level.setBlock(pos.setY(y + up), Blocks.AIR.defaultBlockState(), 2);
            }
            for (int down = 1; down <= 12; down++) {
                BlockState below = level.getBlockState(pos.setY(y - down));
                if (!below.isAir() && !below.liquid() && !below.canBeReplaced()) {
                    break;
                }
                level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
            }
        }
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
                for (int[] r : s.roads) { // a road over it is laid again at its new height
                    if (p.roadPlates(List.of(r)).contains(plate)) {
                        lay(level, p, s, r);
                    }
                }
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
                plates(player.server.overworld(), c), new ArrayList<>(s.flat), new ArrayList<>(s.pending), new HashMap<>(s.districts), new ArrayList<>(s.roads),
                specialKinds(site(c)).stream().map(CityStyles.Building::file).toList(),
                specialKinds(site(c)).stream().map(b -> new int[]{b.x(), b.z()}).toList(), new ArrayList<>(s.boxes())));
    }

    @Override
    public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        CompoundTag all = new CompoundTag();
        cities.forEach((province, s) -> {
            CompoundTag one = new CompoundTag();
            one.putIntArray("res", s.res);
            one.put("flat", new LongArrayTag(new ArrayList<>(s.flat)));
            one.put("pending", new LongArrayTag(s.pending));
            one.put("districts", new LongArrayTag(new ArrayList<>(s.districts.keySet())));
            one.putIntArray("districtKinds", s.districts.values().stream().mapToInt(Integer::intValue).toArray());
            one.putIntArray("roads", s.roads.stream().flatMapToInt(java.util.Arrays::stream).toArray());
            net.minecraft.nbt.ListTag specials = new net.minecraft.nbt.ListTag();
            for (Special sp : s.specials) {
                CompoundTag t = new CompoundTag();
                t.putString("file", sp.file());
                t.putIntArray("box", sp.box());
                t.putInt("turns", sp.turns());
                specials.add(t);
            }
            one.put("specials", specials);
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
            long[] plates = one.getLongArray("districts");
            int[] kinds = one.getIntArray("districtKinds");
            for (int i = 0; i < Math.min(plates.length, kinds.length); i++) {
                s.districts.put(plates[i], kinds[i]);
            }
            int[] roads = one.getIntArray("roads");
            for (int i = 0; i + 3 < roads.length; i += 4) {
                s.roads.add(java.util.Arrays.copyOfRange(roads, i, i + 4));
            }
            for (net.minecraft.nbt.Tag t : one.getList("specials", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                CompoundTag sp = (CompoundTag) t;
                int[] box = sp.getIntArray("box");
                if (box.length == 4) {
                    s.specials.add(new Special(sp.getString("file"), box, sp.getInt("turns")));
                }
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
