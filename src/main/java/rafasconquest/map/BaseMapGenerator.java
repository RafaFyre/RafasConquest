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
import rafasconquest.core.Roads;
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
    /** However slow the terrain, heights are read at least every this many pixels, and biomes every this many. */
    static final int MAX_HEIGHT_STEP = 16;
    static final int MAX_BIOME_STEP = 3;
    /** The heights may take about this long in all before they are read more sparsely (the biomes half of it). */
    static final double BUDGET_SECONDS = 40;

    /** A grid of ground heights, a reading every {@code step} pixels (plus a border), and how many readings it took. */
    private record Heights(int[] values, int width, int read) {
    }

    /**
     * The ground's heights every {@code step} pixels (plus a border for the smoothing and the shading), keeping the
     * readings of a grid twice as coarse where it has them (its point i is this one's 2i - 1).
     */
    private static Heights heights(@org.jetbrains.annotations.Nullable Heights coarser, int step, int size, int minX, int minZ, int bpp,
                                   int visibleRadius, int sea, ChunkGenerator generator, ServerLevel level, RandomState random, long began) {
        int hw = size / step + 3;
        int[] values = new int[hw * hw];
        boolean[] known = new boolean[hw * hw];
        if (coarser != null) {
            for (int j = 0; j < coarser.width(); j++) {
                for (int i = 0; i < coarser.width(); i++) {
                    int fi = 2 * i - 1;
                    int fj = 2 * j - 1;
                    if (fi >= 0 && fj >= 0 && fi < hw && fj < hw) {
                        values[fj * hw + fi] = coarser.values()[j * coarser.width() + i];
                        known[fj * hw + fi] = true;
                    }
                }
            }
        }
        long reach = (long) (visibleRadius + 2L * step * bpp) * (visibleRadius + 2L * step * bpp);
        java.util.concurrent.atomic.AtomicInteger read = new java.util.concurrent.atomic.AtomicInteger();
        java.util.concurrent.atomic.AtomicInteger rows = new java.util.concurrent.atomic.AtomicInteger();
        IntStream.range(0, hw).parallel().forEach(j -> {
            for (int i = 0; i < hw; i++) {
                if (known[j * hw + i]) {
                    continue;
                }
                int x = minX + (i - 1) * step * bpp;
                int z = minZ + (j - 1) * step * bpp;
                if ((long) x * x + (long) z * z <= reach) {
                    values[j * hw + i] = generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level, random);
                    read.incrementAndGet();
                } else {
                    values[j * hw + i] = sea;
                }
            }
            progress("heights every " + step * bpp + " blocks", rows.incrementAndGet(), hw, began);
        });
        return new Heights(values, hw, read.get());
    }

    /** The k-th trial point: spread over the known world, the same for every world. */
    private static int[] probe(int k, int radius) {
        double angle = k * 2.399963; // the golden angle: points spread evenly
        double r = radius * 0.8 * Math.sqrt((k + 0.5) / 128.0);
        return new int[]{(int) (Math.cos(angle) * r), (int) (Math.sin(angle) * r)};
    }

    /** Seconds per call of {@code read}, over {@code count} trial points. */
    private static double timed(int count, java.util.function.IntConsumer read) {
        long start = System.nanoTime();
        for (int k = 0; k < count; k++) {
            read.accept(k);
        }
        return (System.nanoTime() - start) / 1e9 / count;
    }

    /** Logs how far a step of the drawing has got, at each quarter. */
    private static void progress(String what, int done, int of, long began) {
        int quarter = Math.max(1, of / 4);
        if (done % quarter == 0 && done < of) {
            rafasconquest.RafasConquest.LOGGER.info("[world] drawing the world map: {} {}% ({} s)", what, done * 100 / of,
                    (System.nanoTime() - began) / 1_000_000_000L);
        }
    }

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
        long began = System.nanoTime();
        java.util.concurrent.ForkJoinPool pool = java.util.concurrent.ForkJoinTask.getPool();
        int threads = pool != null ? pool.getParallelism() : 1;
        // Heights: a coarse grid first, then finer ones while the time they have taken says the next will fit (a terrain
        // mod can make reading them ten times slower). Each finer grid keeps the coarser one's readings.
        int step = MAX_HEIGHT_STEP;
        long passStart = System.nanoTime();
        Heights heightGrid = heights(null, step, size, minX, minZ, bpp, visibleRadius, sea, generator, level, random, began);
        double passSeconds = (System.nanoTime() - passStart) / 1e9;
        while (step > HEIGHT_STEP && heightGrid.read() > 0) {
            int finer = step / 2;
            double perRead = passSeconds / heightGrid.read();
            double next = perRead * (Math.pow(size / (double) finer + 3, 2) - Math.pow(heightGrid.width(), 2)) * 0.8;
            if ((System.nanoTime() - began) / 1e9 + next > BUDGET_SECONDS) {
                break;
            }
            step = finer;
            passStart = System.nanoTime();
            heightGrid = heights(heightGrid, step, size, minX, minZ, bpp, visibleRadius, sea, generator, level, random, began);
            passSeconds = (System.nanoTime() - passStart) / 1e9;
        }
        final int heightStep = step;
        int hw = heightGrid.width();
        int[] coarse = heightGrid.values();
        // Biomes: read every pixel, or every second or third if this world's are slow to read (timed once warmed up).
        timed(64, k -> biomes.getNoiseBiome(QuartPos.fromBlock(probe(k, visibleRadius)[0]), QuartPos.fromBlock(sea),
                QuartPos.fromBlock(probe(k, visibleRadius)[1]), sampler));
        double perBiome = timed(128, k -> biomes.getNoiseBiome(QuartPos.fromBlock(probe(k, visibleRadius)[0]), QuartPos.fromBlock(sea),
                QuartPos.fromBlock(probe(k, visibleRadius)[1]), sampler));
        int biomeStep = 1;
        while (biomeStep < MAX_BIOME_STEP && 0.8 * Math.pow(size / (double) biomeStep, 2) * perBiome / threads > BUDGET_SECONDS / 2) {
            biomeStep++;
        }
        final int biomeEvery = biomeStep;
        rafasconquest.RafasConquest.LOGGER.info("[world] drawing the world map: {} pixels a side; heights read every {} blocks in {} s, "
                        + "biomes to read every {} blocks ({} µs each, on {} threads)", size, heightStep * bpp,
                (System.nanoTime() - began) / 1_000_000_000L, biomeEvery * bpp, String.format("%.1f", perBiome * 1e6), threads);

        long visible2 = (long) visibleRadius * visibleRadius;
        int n = size * size;
        int[] pixels = new int[n];
        boolean[] water = new boolean[n];
        float[] heights = new float[n];
        int[] biomeIds = new int[n];
        boolean[] rough = new boolean[n];
        boolean[] wet = new boolean[n];
        byte[] hard = new byte[n];
        byte[] homeland = new byte[n];
        // The biomes, read every biomeEvery pixels (each read kept for the pixels about it).
        int bw = (size + biomeEvery - 1) / biomeEvery;
        @SuppressWarnings("unchecked")
        Holder<Biome>[] read = new Holder[bw * bw];
        java.util.concurrent.atomic.AtomicInteger biomeRows = new java.util.concurrent.atomic.AtomicInteger();
        IntStream.range(0, size).parallel().forEach(pz -> {
            for (int px = 0; px < size; px++) {
                int index = pz * size + px;
                int x = minX + px * bpp + bpp / 2;
                int z = minZ + pz * bpp + bpp / 2;
                if ((long) x * x + (long) z * z > visible2) {
                    water[index] = true; // beyond the known world: transparent, and in no province
                    continue;
                }
                float h = height(coarse, hw, heightStep, px, pz);
                heights[index] = h;
                int cell = pz / biomeEvery * bw + px / biomeEvery;
                Holder<Biome> biome = read[cell];
                if (biome == null) {
                    // Read where that cell begins (the same answer whichever thread gets there first).
                    int ax = px - px % biomeEvery;
                    int az = pz - pz % biomeEvery;
                    float ah = height(coarse, hw, heightStep, ax, az);
                    biome = biomes.getNoiseBiome(QuartPos.fromBlock(minX + ax * bpp + bpp / 2), QuartPos.fromBlock(Math.max((int) ah, sea)),
                            QuartPos.fromBlock(minZ + az * bpp + bpp / 2), sampler);
                    read[cell] = biome;
                }
                biomeIds[index] = System.identityHashCode(biome.value());
                water[index] = BiomePalette.isSea(biome);
                wet[index] = water[index] || biome.is(net.minecraft.tags.BiomeTags.IS_RIVER);
                rough[index] = BiomePalette.isRough(biome);
                homeland[index] = (byte) homeland(biome).ordinal();
                hard[index] = (byte) (biome.is(net.minecraft.tags.BiomeTags.IS_JUNGLE) ? 1
                        : biome.is(net.minecraft.tags.BiomeTags.IS_MOUNTAIN) || biome.is(net.minecraftforge.common.Tags.Biomes.IS_PEAK) ? 2 : 0);
                int color = BiomePalette.color(biome, (int) h, sea);
                if (!water[index] && h >= sea) {
                    // Light from the north-west, measured across a full height-sample step so the smoothing between
                    // samples doesn't show as facets. High ground a little paler.
                    float slope = (height(coarse, hw, heightStep, px - heightStep / 2, pz - heightStep / 2)
                            - height(coarse, hw, heightStep, px + heightStep / 2, pz + heightStep / 2)) * HEIGHT_STEP / heightStep;
                    float light = 1 + Math.max(-0.25F, Math.min(0.25F, slope * 0.02F));
                    color = MapColors.shade(color, light);
                    color = MapColors.lerp(color, 0xE8E8E8, Math.max(0, Math.min(0.35F, (h - sea - 60) / 160F)));
                }
                pixels[index] = 0xFF000000 | color;
            }
            progress("lands", biomeRows.incrementAndGet(), size, began);
        });
        rafasconquest.RafasConquest.LOGGER.info("[world] the land read in {} s; now the provinces, titles, people and roads",
                (System.nanoTime() - began) / 1_000_000_000L);

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
                settings.eyeRadius(), settings.warZoneOuterEdge(), visibleRadius, WorldFeatures.martialTitles() ? Nations.SANCTUMS : 0);
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
        // A nation's seat with neither a city nor a castle gets a castle, so its ruler has somewhere to stand.
        for (Nations.Nation nation : plan.nations()) {
            boolean built = cities.stream().anyMatch(c -> c.province() == nation.seat())
                    || settlements.stream().anyMatch(s -> s.kind() == Countryside.Kind.CASTLE && s.province() == nation.seat());
            if (!built) {
                settlements.add(new Countryside.Settlement(nation.x(), nation.z(), Countryside.Kind.CASTLE, nation.seat(), 0));
            }
        }
        int[] ground = new int[n];
        for (int i = 0; i < n; i++) {
            ground[i] = Math.round(heights[i]);
        }
        List<Dojos.Dojo> dojos = WorldFeatures.mantraSchools()
                ? dojos(level.getSeed(), minX, minZ, bpp, size, visibleRadius, settings.eyeRadius(), provinces, wet, cities, nations)
                : List.of();
        Roads.Plan roads = roads(new Roads.Land(minX, minZ, bpp, size, size, ground, wet, water, hard, sea), titles, names, cities,
                settlements, dojos);
        return new BaseMap(minX, minZ, bpp, size, size, visibleRadius, stormWallRadius, pixels, provinces, centres, names, titles,
                cities, settlements, nations, plan.holder(), ground, wet, realm(level.getSeed(), titles, centres, cities, settlements, nations,
                        homelands(provinces, homeland, centres.size()),
                        plan.holder()), dojos, roads);
    }

    /**
     * The roads ({@link Roads}): the seat cities' network, then spurs to the castles (city-grade), villages (dirt paths)
     * and known dojos (cobblestone, from the nearest fork).
     */
    private static Roads.Plan roads(Roads.Land land, TitleMap titles, List<String> names, List<SeatCities.City> cities,
                                    List<Countryside.Settlement> settlements, List<Dojos.Dojo> dojos) {
        long started = System.nanoTime();
        List<Roads.Spur> spurs = new java.util.ArrayList<>();
        for (Countryside.Settlement s : settlements) {
            if (s.kind() == Countryside.Kind.CASTLE) {
                spurs.add(new Roads.Spur(Roads.Grade.CITY, new Roads.Place("Castle of " + names.get(s.province()), s.x(), s.z(), 0, 0), false));
            } else if (s.kind() == Countryside.Kind.VILLAGE) {
                spurs.add(new Roads.Spur(Roads.Grade.TOWN, new Roads.Place("Village of " + names.get(s.province()), s.x(), s.z(), 0, 0), false));
            }
        }
        for (Dojos.Dojo d : dojos) {
            if (!d.school().hidden()) {
                spurs.add(new Roads.Spur(Roads.Grade.DOJO, new Roads.Place("Dojo of the " + d.school().title() + " school", d.x(), d.z(), 0, 0),
                        true));
            }
        }
        Roads.Plan plan = Roads.plan(land, Roads.links(cities, titles, names), spurs,
                cities.stream().map(c -> Roads.place(c, names.get(c.province()))).toList());
        long length = 0;
        for (Roads.Road r : plan.roads()) {
            for (int i = 1; i < r.points(); i++) {
                length += (long) Math.hypot(r.xs()[i] - r.xs()[i - 1], r.zs()[i] - r.zs()[i - 1]);
            }
        }
        rafasconquest.RafasConquest.LOGGER.info("[world] {} roads, {} blocks long, {} forks, planned in {} ms", plan.roads().size(), length,
                plan.junctions().size(), (System.nanoTime() - started) / 1_000_000);
        return plan;
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

    /** The kind of land a biome is, by the villagers who would live there; plains for any other mod's own kinds. */
    private static rafasconquest.people.Homeland homeland(Holder<Biome> biome) {
        net.minecraft.world.entity.npc.VillagerType type = net.minecraft.world.entity.npc.VillagerType.byBiome(biome);
        if (type == net.minecraft.world.entity.npc.VillagerType.DESERT) {
            return rafasconquest.people.Homeland.DESERT;
        } else if (type == net.minecraft.world.entity.npc.VillagerType.JUNGLE) {
            return rafasconquest.people.Homeland.JUNGLE;
        } else if (type == net.minecraft.world.entity.npc.VillagerType.SAVANNA) {
            return rafasconquest.people.Homeland.SAVANNA;
        } else if (type == net.minecraft.world.entity.npc.VillagerType.SNOW) {
            return rafasconquest.people.Homeland.SNOW;
        } else if (type == net.minecraft.world.entity.npc.VillagerType.SWAMP) {
            return rafasconquest.people.Homeland.SWAMP;
        } else if (type == net.minecraft.world.entity.npc.VillagerType.TAIGA) {
            return rafasconquest.people.Homeland.TAIGA;
        }
        return rafasconquest.people.Homeland.PLAINS;
    }

    /** Each province's kind of land: the one most of its pixels are. */
    private static int[] homelands(int[] provinces, byte[] homeland, int count) {
        int kinds = rafasconquest.people.Homeland.values().length;
        int[] tally = new int[count * kinds];
        for (int i = 0; i < provinces.length; i++) {
            if (provinces[i] >= 0) {
                tally[provinces[i] * kinds + homeland[i]]++;
            }
        }
        int[] chosen = new int[count];
        for (int p = 0; p < count; p++) {
            int best = rafasconquest.people.Homeland.PLAINS.ordinal();
            for (int h = 0; h < kinds; h++) {
                if (tally[p * kinds + h] > tally[p * kinds + best]) {
                    best = h;
                }
            }
            chosen[p] = best;
        }
        return chosen;
    }

    /** The world's people, planned on the finished map: its holds' holders, their cultures, faiths and bonds. */
    private static rafasconquest.people.Realm realm(long seed, TitleMap titles, List<ProvinceMap.Centre> centres, List<SeatCities.City> cities,
                                                    List<Countryside.Settlement> settlements, List<BaseMap.Nation> nations, int[] homeland,
                                                    int[] holder) {
        List<rafasconquest.people.RealmPlanner.Seat> seats = nations.stream()
                .map(n -> new rafasconquest.people.RealmPlanner.Seat(n.tier().ordinal(), n.seat(), n.x(), n.z(), n.sanctum())).toList();
        rafasconquest.people.Realm realm = rafasconquest.people.RealmPlanner.plan(seed, titles, centres, cities, settlements, seats, holder, homeland,
                WorldFeatures.martialTitles());
        // The sword sanctums, as organizations: each its Sword God's, with the kings and saints trained there.
        List<rafasconquest.people.Organizations.Sanctum> sanctums = new java.util.ArrayList<>();
        for (int n = 0; n < nations.size(); n++) {
            BaseMap.Nation nation = nations.get(n);
            if (nation.sanctum()) {
                sanctums.add(new rafasconquest.people.Organizations.Sanctum(nation.name(), n, realm.ruler(n), nation.x(), nation.z()));
            }
        }
        realm = realm.withOrganizations(rafasconquest.people.Organizations.plan(new java.util.Random(seed ^ 0x53414E4354L), realm.people(), sanctums));
        long superhuman = realm.people().stream().filter(p -> p.stat(rafasconquest.people.Stat.PROWESS) >= rafasconquest.people.Stat.SUPERHUMAN).count();
        rafasconquest.RafasConquest.LOGGER.info("[world] {} holders ({} superhuman), {} cultures, {} faiths, {} friendships and rivalries",
                realm.people().size(), superhuman, realm.cultures().size(), realm.faiths().size(), realm.bonds().size());
        return realm;
    }

    /** Height at a pixel, smoothed (bilinear) between the coarse samples. */
    private static float height(int[] heights, int hw, int step, int px, int pz) {
        float gx = (float) px / step + 1;
        float gz = (float) pz / step + 1;
        int i = Math.max(0, Math.min(hw - 2, (int) Math.floor(gx)));
        int j = Math.max(0, Math.min(hw - 2, (int) Math.floor(gz)));
        float fx = Math.max(0, Math.min(1, gx - i));
        float fz = Math.max(0, Math.min(1, gz - j));
        float top = heights[j * hw + i] * (1 - fx) + heights[j * hw + i + 1] * fx;
        float bottom = heights[(j + 1) * hw + i] * (1 - fx) + heights[(j + 1) * hw + i + 1] * fx;
        return top * (1 - fz) + bottom * fz;
    }
}
