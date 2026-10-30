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
import rafasconquest.core.CityLayout;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.ProvinceNames;
import rafasconquest.core.Countryside;
import rafasconquest.core.Dojos;
import rafasconquest.core.WorldFeatures;
import rafasconquest.core.Nations;
import rafasconquest.core.OriginRoller;
import rafasconquest.core.SeatCities;
import rafasconquest.core.TitleMap;
import rafasconquest.core.WorldSettings;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Draws the world map from the seed alone: terrain height from the noise generator (sampled every few pixels and
 * smoothed between), the biome at the surface for colour, water shaded by depth and land lit from the north-west. Then
 * splits the land into provinces ({@link ProvinceMap}). Nothing is generated or loaded, so the whole known world is
 * drawn at once. Runs off the server thread.
 */
public final class BaseMapGenerator {
    /** Finest detail: one pixel per 16 blocks (a chunk). */
    static final int MIN_BLOCKS_PER_PIXEL = 16;
    /** Largest map side in pixels; a bigger known world gets coarser pixels instead. */
    static final int MAX_SIZE = 1024;
    /** An unowned seat is a ruin (a lone empty house) this often, and nothing at all this often; otherwise people still live there. */
    static final double UNOWNED_RUIN = 0.6;
    static final double UNOWNED_EMPTY = 0.25;
    /** A sword sanctum's city, districts a side. */
    static final int SANCTUM_DISTRICTS = 4;
    /** Heights are sampled every this many pixels; they change slowly and each sample is costly. */
    static final int HEIGHT_STEP = 4;

    private BaseMapGenerator() {
    }

    public static BaseMap generate(ServerLevel level, WorldSettings settings, int visibleRadius, int stormWallRadius) {
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
        int[] coarse = new int[hw * hw];
        IntStream.range(0, hw).parallel().forEach(j -> {
            for (int i = 0; i < hw; i++) {
                int x = minX + (i - 1) * HEIGHT_STEP * bpp;
                int z = minZ + (j - 1) * HEIGHT_STEP * bpp;
                coarse[j * hw + i] = (long) x * x + (long) z * z <= reach
                        ? generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level, random)
                        : sea;
            }
        });

        long visible2 = (long) visibleRadius * visibleRadius;
        int n = size * size;
        int[] pixels = new int[n];
        boolean[] water = new boolean[n];
        float[] heights = new float[n];
        int[] biomeIds = new int[n];
        boolean[] rough = new boolean[n];
        boolean[] wet = new boolean[n];
        IntStream.range(0, size).parallel().forEach(pz -> {
            for (int px = 0; px < size; px++) {
                int index = pz * size + px;
                int x = minX + px * bpp + bpp / 2;
                int z = minZ + pz * bpp + bpp / 2;
                if ((long) x * x + (long) z * z > visible2) {
                    water[index] = true; // beyond the known world: transparent, and in no province
                    continue;
                }
                float h = height(coarse, hw, px, pz);
                heights[index] = h;
                Holder<Biome> biome = biomes.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(Math.max((int) h, sea)),
                        QuartPos.fromBlock(z), sampler);
                biomeIds[index] = System.identityHashCode(biome.value());
                water[index] = BiomePalette.isSea(biome);
                wet[index] = water[index] || biome.is(net.minecraft.tags.BiomeTags.IS_RIVER);
                rough[index] = BiomePalette.isRough(biome);
                int color = BiomePalette.color(biome, (int) h, sea);
                if (!water[index] && h >= sea) {
                    // Light from the north-west, measured across a full height-sample step so the smoothing between
                    // samples doesn't show as facets. High ground a little paler.
                    float slope = height(coarse, hw, px - HEIGHT_STEP / 2, pz - HEIGHT_STEP / 2)
                            - height(coarse, hw, px + HEIGHT_STEP / 2, pz + HEIGHT_STEP / 2);
                    float light = 1 + Math.max(-0.25F, Math.min(0.25F, slope * 0.02F));
                    color = MapColors.shade(color, light);
                    color = MapColors.lerp(color, 0xE8E8E8, Math.max(0, Math.min(0.35F, (h - sea - 60) / 160F)));
                }
                pixels[index] = 0xFF000000 | color;
            }
        });

        // Provinces first, from the land alone: titles and their seat cities are drawn on them.
        ProvinceMap.Terrain terrain = new ProvinceMap.Terrain(minX, minZ, bpp, size, size, water, heights, biomeIds, rough);
        List<ProvinceMap.Centre> centres = ProvinceMap.centres(level.getSeed(), terrain, List.of());
        int[] provinces = ProvinceMap.assign(terrain, centres);
        // The titles: provinces into counties, duchies, kingdoms and empires, telling the shore by the sea beside it
        // (the sea within the known world; beyond it is no shore).
        boolean[] ocean = new boolean[n];
        for (int i = 0; i < n; i++) {
            ocean[i] = provinces[i] == ProvinceMap.WATER && pixels[i] != 0;
        }
        TitleMap titles = TitleMap.build(level.getSeed(), size, size, provinces, centres.size(), ocean);
        List<String> names = centres.stream().map(c -> ProvinceNames.name(level.getSeed(), c.x(), c.z())).toList();
        SeatCities.Grid grid = new SeatCities.Grid(minX, minZ, bpp, size, size, provinces, centres);
        List<SeatCities.City> planned = SeatCities.plan(level.getSeed(), grid, titles);
        Countryside.Plan countryside = Countryside.plan(level.getSeed(), grid, titles, planned);
        // Where each seat stands: its city, its castle, or its province's centre.
        java.util.Map<Integer, int[]> seatAt = new java.util.HashMap<>();
        for (Countryside.Settlement s : countryside.settlements()) {
            if (s.kind() == Countryside.Kind.CASTLE) {
                seatAt.put(s.province(), new int[]{s.x(), s.z()});
            }
        }
        for (SeatCities.City c : countryside.cities()) {
            seatAt.put(c.province(), new int[]{c.x(), c.z()});
        }
        Nations.Plan plan = Nations.plan(level.getSeed(), titles,
                p -> seatAt.getOrDefault(p, new int[]{centres.get(p).x(), centres.get(p).z()}),
                settings.eyeRadius(), settings.warZoneOuterEdge(), WorldFeatures.martialTitles() ? Nations.SANCTUMS : 0);
        List<BaseMap.Nation> nations = new java.util.ArrayList<>();
        for (Nations.Nation nation : plan.nations()) {
            OriginRoller.Roll roll = OriginRoller.roll(level.getSeed(), nation.x(), nation.z(), settings);
            String name = nation.sanctum() ? "Sword Sanctum of " + roll.name()
                    : nation.tier() == TitleMap.Tier.EMPIRE ? roll.name() + " Empire" : roll.name();
            nations.add(new BaseMap.Nation(roll.id(), name, roll.strength(), roll.radiusSteps(), nation.tier(), nation.title(),
                    nation.seat(), nation.x(), nation.z(), nation.sanctum()));
        }
        // A sword sanctum's city is 4 × 4 districts, in place of its seat's own (a new one, if its seat had none).
        java.util.Map<Integer, Nations.Nation> sanctumAt = new java.util.HashMap<>();
        for (Nations.Nation nation : plan.nations()) {
            if (nation.sanctum()) {
                sanctumAt.put(nation.seat(), nation);
            }
        }
        // The seats no nation holds: mostly a ruin (one empty house where the city would have stood), sometimes nothing
        // at all, sometimes still lived in (with no owner).
        java.util.Random fates = new java.util.Random(level.getSeed() ^ 0x4641544553L);
        List<SeatCities.City> cities = new java.util.ArrayList<>();
        List<Countryside.Settlement> settlements = new java.util.ArrayList<>(countryside.settlements());
        for (SeatCities.City c : countryside.cities()) {
            if (sanctumAt.remove(c.province()) != null) {
                cities.add(new SeatCities.City(c.x(), c.z(), SANCTUM_DISTRICTS, c.tier(), c.province()));
                continue;
            }
            if (plan.holder()[c.province()] >= 0) {
                cities.add(c);
                continue;
            }
            double roll = fates.nextDouble();
            if (roll < UNOWNED_RUIN) {
                settlements.add(new Countryside.Settlement(c.x(), c.z(), Countryside.Kind.RUIN, c.province(), 0));
            } else if (roll >= UNOWNED_RUIN + UNOWNED_EMPTY) {
                cities.add(c);
            }
        }
        for (Nations.Nation sanctum : sanctumAt.values()) {
            cities.add(new SeatCities.City(sanctum.x(), sanctum.z(), SANCTUM_DISTRICTS, TitleMap.Tier.KINGDOM, sanctum.seat()));
        }
        int[] ground = new int[n];
        for (int i = 0; i < n; i++) {
            ground[i] = Math.round(heights[i]);
        }
        List<Dojos.Dojo> dojos = WorldFeatures.mantraSchools()
                ? dojos(level.getSeed(), minX, minZ, bpp, size, visibleRadius, settings.eyeRadius(), provinces, wet, cities, nations)
                : List.of();
        return new BaseMap(minX, minZ, bpp, size, size, visibleRadius, stormWallRadius, pixels, provinces, centres, names, titles,
                cities, settlements, nations, plan.holder(), ground, wet, realm(level.getSeed(), titles, centres, cities, settlements, nations,
                        plan.holder()), dojos);
    }

    /** How far a dojo keeps from the edge of any city. */
    static final int DOJO_CITY_CLEARANCE = 200;

    /**
     * The mantra schools' dojos ({@link Dojos}): on dry land of a province, clear of every city, apart from each other
     * and from the sword sanctums.
     */
    private static List<Dojos.Dojo> dojos(long seed, int minX, int minZ, int bpp, int size, int radius, int eyeRadius, int[] provinces,
                                          boolean[] wet, List<SeatCities.City> cities, List<BaseMap.Nation> nations) {
        List<int[]> sanctums = nations.stream().filter(BaseMap.Nation::sanctum).map(n -> new int[]{n.x(), n.z()}).toList();
        List<Dojos.Dojo> dojos = Dojos.plan(seed, radius, eyeRadius, sanctums, (x, z) -> {
            int px = Math.floorDiv(x - minX, bpp);
            int pz = Math.floorDiv(z - minZ, bpp);
            if (px < 0 || pz < 0 || px >= size || pz >= size || provinces[pz * size + px] < 0 || wet[pz * size + px]) {
                return false;
            }
            for (SeatCities.City c : cities) {
                if (Math.abs(c.x() - x) < c.size() / 2 + DOJO_CITY_CLEARANCE && Math.abs(c.z() - z) < c.size() / 2 + DOJO_CITY_CLEARANCE) {
                    return false;
                }
            }
            return true;
        });
        rafasconquest.RafasConquest.LOGGER.info("[world] {} dojos of the mantra schools: {}", dojos.size(),
                dojos.stream().map(d -> d.school().title() + (d.school().hidden() ? " (hidden)" : "") + " at " + d.x() + ", " + d.z())
                        .collect(java.util.stream.Collectors.joining("; ")));
        return dojos;
    }

    /** The world's people, planned on the finished map: its holds' holders, their cultures, faiths and bonds. */
    private static rafasconquest.people.Realm realm(long seed, TitleMap titles, List<ProvinceMap.Centre> centres, List<SeatCities.City> cities,
                                                    List<Countryside.Settlement> settlements, List<BaseMap.Nation> nations, int[] holder) {
        List<rafasconquest.people.RealmPlanner.Seat> seats = nations.stream()
                .map(n -> new rafasconquest.people.RealmPlanner.Seat(n.tier().ordinal(), n.seat(), n.x(), n.z(), n.sanctum())).toList();
        rafasconquest.people.Realm realm = rafasconquest.people.RealmPlanner.plan(seed, titles, centres, cities, settlements, seats, holder,
                WorldFeatures.martialTitles());
        long superhuman = realm.people().stream().filter(p -> p.stat(rafasconquest.people.Stat.PROWESS) >= rafasconquest.people.Stat.SUPERHUMAN).count();
        rafasconquest.RafasConquest.LOGGER.info("[world] {} holders ({} superhuman), {} cultures, {} faiths, {} friendships and rivalries",
                realm.people().size(), superhuman, realm.cultures().size(), realm.faiths().size(), realm.bonds().size());
        return realm;
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
