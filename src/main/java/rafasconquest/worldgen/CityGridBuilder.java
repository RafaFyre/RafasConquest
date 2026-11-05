package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import rafasconquest.RafasConquest;
import rafasconquest.core.CityLayout;
import rafasconquest.core.RecruitmentPost;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.SeedHash;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds one cell of a city's planned grid: first its share of the flattened ground ({@link CityTerrainPiece}), then
 * its roads, a vanilla building on every plot (or the town centre on the middle block), and its part of the wall,
 * all at the plate height. Buildings are turned so their entrance faces a road and placed against it;
 * anything they link to through their own jigsaws (villagers, cats, golems) is attached the way vanilla does it.
 */
public final class CityGridBuilder {
    /** How many distinct house picks to try before leaving a plot empty. Fitting is just bounding-box math, so be generous. */
    private static final int HOUSE_ATTEMPTS = 64;
    /** How deep to follow a building's own jigsaw links (building → villager is 1). */
    private static final int MAX_CHILD_DEPTH = 2;
    private static final String ENTRANCE = "minecraft:building_entrance";
    private static final String WALL_END = "rafasconquest:wall_end";
    /**
     * How far below the plate the city's spawn protection reaches. The city structures' spawn_overrides block hostile
     * spawns inside every piece's bounding box; the terrain piece's box starts this far down, so caves deep under the
     * city keep their mobs.
     */
    private static final int SPAWN_PROTECTION_DEPTH = 16;

    /** The terrain top at a column; {@code waterDepth} is 0 on land. */
    public record Ground(int y, int waterDepth) {
        public boolean water() {
            return waterDepth > 0;
        }
    }

    private final Structure.GenerationContext ctx;
    private final CityLayout.Site site;
    private final CityVariant variant;
    private final StructurePiecesBuilder pieces;
    private final WorldgenRandom random;
    private final StructureTemplateManager templates;
    private final Registry<StructureTemplatePool> pools;
    private final CityTerrain.Plan plan;
    /** Height of the flattened plate: the ground level of every road, building and wall in the city. */
    private final int plate;
    /** Bounding boxes of everything placed in the current city block, so lamp posts can avoid them. */
    private final List<BoundingBox> occupied = new java.util.ArrayList<>();

    /** Terrain plans by city, so the 9 cells of a city plan it once. Plans are deterministic, so any copy is right. */
    private static final Map<Long, CityTerrain.Plan> PLANS = new ConcurrentHashMap<>();
    private static final int MAX_CACHED_PLANS = 256;

    private CityGridBuilder(Structure.GenerationContext ctx, CityLayout.Site site, CityVariant variant, StructurePiecesBuilder pieces) {
        this.ctx = ctx;
        this.site = site;
        this.variant = variant;
        this.pieces = pieces;
        this.random = ctx.random();
        this.templates = ctx.structureTemplateManager();
        this.pools = ctx.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        this.plan = terrainPlan(ctx, site);
        this.plate = plan.plateY();
    }

    public static CityTerrain.Plan terrainPlan(Structure.GenerationContext ctx, CityLayout.Site site) {
        long key = SeedHash.hash(ctx.seed(), site.x(), site.z());
        CityTerrain.Plan cached = PLANS.get(key);
        if (cached != null) {
            return cached;
        }
        if (PLANS.size() >= MAX_CACHED_PLANS) {
            PLANS.clear();
        }
        // computeIfAbsent makes the other cells of this city wait for the first one's plan instead of redoing it.
        return PLANS.computeIfAbsent(key, k -> planAndLog(ctx, site));
    }

    /** The city survey's threads: a quarter of the CPU's (at least 2), never all of them. */
    private static final java.util.concurrent.ForkJoinPool SURVEY = new java.util.concurrent.ForkJoinPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() / 4));

    private static CityTerrain.Plan planAndLog(Structure.GenerationContext ctx, CityLayout.Site site) {
        long start = System.nanoTime();
        // Where the survey reads the ground doesn't depend on what it finds: list those places first (each once), read
        // them all at once on every core, then survey from the readings. A heavy terrain mod makes each reading slow.
        java.util.Set<Long> wanted = java.util.concurrent.ConcurrentHashMap.newKeySet();
        int sea = ctx.chunkGenerator().getSeaLevel();
        // The box reaches past the walls: a style's own may stand further out than the mod's.
        int outset = rafasconquest.style.CityPlan.wallOutset(site);
        CityTerrain.plan(site, (x, z) -> {
            wanted.add(xz(x, z));
            return sea;
        }, sea, outset);
        java.util.Map<Long, Integer> read = new java.util.concurrent.ConcurrentHashMap<>();
        // On a pool of its own, a quarter of the threads: the rest stay free for the world generation going on meanwhile.
        SURVEY.submit(() -> wanted.parallelStream().forEach(k -> read.put(k, surface(ctx, (int) (k >> 32), (int) (long) k)))).join();
        CityTerrain.Plan plan = CityTerrain.plan(site, (x, z) -> read.computeIfAbsent(xz(x, z), k -> surface(ctx, x, z)), sea, outset);
        int[] counts = new int[CityTerrain.Edge.values().length];
        for (CityTerrain.Edge[] side : plan.edges()) {
            for (CityTerrain.Edge e : side) {
                counts[e.ordinal()]++;
            }
        }
        RafasConquest.LOGGER.info("Planned city terrain at {}, {}: plate y {}, edges {} carve / {} fill / {} valley, in {} ms",
                site.x(), site.z(), plan.plateY(), counts[0], counts[1], counts[2], (System.nanoTime() - start) / 1_000_000);
        return plan;
    }

    static void build(Structure.GenerationContext ctx, CityLayout.Site site, CityVariant variant, int cellX, int cellZ, StructurePiecesBuilder pieces) {
        new CityGridBuilder(ctx, site, variant, pieces).buildCell(cellX, cellZ);
    }

    /** (x, z) as one key: x in the high half. */
    private static long xz(int x, int z) {
        return ((long) x << 32) | (z & 0xffffffffL);
    }

    /**
     * The y of the top non-air block of the noise terrain at (x, z), as {@link #ground} finds it, but reading down only
     * as far as that block rather than the whole column.
     */
    static int surface(Structure.GenerationContext ctx, int x, int z) {
        return ctx.chunkGenerator().getBaseHeight(x, z, net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                ctx.heightAccessor(), ctx.randomState()) - 1;
    }

    /** The top non-air block of the noise terrain at (x, z), and whether it is water. Needs no generated chunks. */
    public static Ground ground(Structure.GenerationContext ctx, int x, int z) {
        NoiseColumn column = ctx.chunkGenerator().getBaseColumn(x, z, ctx.heightAccessor(), ctx.randomState());
        int min = ctx.heightAccessor().getMinBuildHeight();
        for (int y = ctx.heightAccessor().getMaxBuildHeight() - 1; y > min; y--) {
            BlockState state = column.getBlock(y);
            if (!state.isAir()) {
                int depth = 0;
                while (y - depth > min && !column.getBlock(y - depth).getFluidState().isEmpty()) {
                    depth++;
                }
                return new Ground(y, depth);
            }
        }
        return new Ground(min, 0);
    }

    private void buildCell(int cellX, int cellZ) {
        int ox = site.originX();
        int oz = site.originZ();
        int minY = ctx.heightAccessor().getMinBuildHeight();
        int maxY = ctx.heightAccessor().getMaxBuildHeight() - 1;
        int cellMinX = ox + CityLayout.cellMin(cellX);
        int cellMaxX = ox + CityLayout.cellMax(cellX);
        int cellMinZ = oz + CityLayout.cellMin(cellZ);
        int cellMaxZ = oz + CityLayout.cellMax(cellZ);

        // The ground first: everything else in the cell is built on the flattened plate.
        pieces.addPiece(new CityTerrainPiece(site, cellX, cellZ, variant, plan, plate - SPAWN_PROTECTION_DEPTH, maxY));

        // The styled walls and the noble district, laid out for the whole city.
        rafasconquest.style.CityPlan.Plan styled = rafasconquest.style.CityPlan.of(site); // by its founder, held or not
        boolean noble = styled.noble();

        // Roads next, so buildings placed afterwards win wherever they touch; none inside the noble district.
        for (int k : CityLayout.roadsInCell(site.cells(), cellX)) {
            int x = ox + CityLayout.roadStart(k);
            boolean cut = noble && k > styled.x0() && k < styled.x1();
            for (int[] run : runs(cellMinZ - oz, cellMaxZ - oz, cut ? styled.areaMinZ() : 0, cut ? styled.areaMaxZ() : -1)) {
                pieces.addPiece(new CityRoadPiece(new BoundingBox(x, plate, oz + run[0], x + CityLayout.ROAD_WIDTH - 1, plate, oz + run[1]), variant));
            }
        }
        for (int k : CityLayout.roadsInCell(site.cells(), cellZ)) {
            int z = oz + CityLayout.roadStart(k);
            boolean cut = noble && k > styled.z0() && k < styled.z1();
            for (int[] run : runs(cellMinX - ox, cellMaxX - ox, cut ? styled.areaMinX() : 0, cut ? styled.areaMaxX() : -1)) {
                pieces.addPiece(new CityRoadPiece(new BoundingBox(ox + run[0], plate, z, ox + run[1], plate, z + CityLayout.ROAD_WIDTH - 1), variant));
            }
        }

        for (int bx : CityLayout.blocksInCell(cellX)) {
            for (int bz : CityLayout.blocksInCell(cellZ)) {
                int blockX = ox + CityLayout.blockStart(bx);
                int blockZ = oz + CityLayout.blockStart(bz);
                occupied.clear();
                if (noble && styled.nobleBlock(bx, bz)) {
                    continue; // the noble district's, below
                }
                if (bx == site.centerBlock() && bz == site.centerBlock()) {
                    townCenter(blockX, blockZ);
                } else if (!(bx == RecruitmentPost.blockX(site) && bz == RecruitmentPost.blockZ(site)) && styledHouses().any()) {
                    packBlock(blockX, blockZ); // the style's own houses, of any size
                } else {
                    for (int qx = 0; qx < 2; qx++) {
                        for (int qz = 0; qz < 2; qz++) {
                            boolean postBlock = bx == RecruitmentPost.blockX(site) && bz == RecruitmentPost.blockZ(site);
                            if (noble && postBlock) {
                                // A noble district against a wall leaves the post's block outside it: the plan has the
                                // Palace and Barracks, and the mod's post where it stands.
                                if (qx == 0 && qz == 0 && styled.has(rafasconquest.style.CityPlan.Kind.POST)) {
                                    continue;
                                }
                            } else if (qx == 0 && qz == 0 && postBlock && heldByANation(site)) {
                                // The small Barracks: from the style folders where its slot has a file, else the mod's own.
                                String barracks = rafasconquest.style.CityStyles.barracks(site);
                                java.util.List<rafasconquest.style.StylePiece> styledPiece = barracks == null ? java.util.List.of() : rafasconquest.style.StylePiece.of(barracks,
                                        blockX + CityLayout.PLOT_SIZE / 2, plate, blockZ + CityLayout.PLOT_SIZE / 2, rafasconquest.style.CityStyles.TURNS_TO_WEST);
                                if (styledPiece.isEmpty()) {
                                    pieces.addPiece(new RecruitmentPostPiece(RecruitmentPost.of(site), plate));
                                }
                                styledPiece.forEach(pieces::addPiece);
                                continue; // the recruitment post has this plot
                            }
                            if (!noble && qx == 0 && qz == 1 && postBlock && heldByANation(site)) {
                                int[] church = RecruitmentPost.of(site).church();
                                // The Palace: from the style folders where its slot has a file, else the gold church.
                                String palace = rafasconquest.style.CityStyles.palace(site);
                                java.util.List<rafasconquest.style.StylePiece> styledPiece = palace == null ? java.util.List.of() : rafasconquest.style.StylePiece.of(palace,
                                        church[0] + CityLayout.PLOT_SIZE / 2, plate, church[1] + CityLayout.PLOT_SIZE / 2, rafasconquest.style.CityStyles.TURNS_TO_WEST);
                                if (styledPiece.isEmpty()) {
                                    pieces.addPiece(new GoldChurchPiece(church[0], church[1], plate));
                                }
                                styledPiece.forEach(pieces::addPiece);
                                continue; // and the gold church, the one beside it
                            }
                            int px = blockX + qx * (CityLayout.PLOT_SIZE + 1);
                            int pz = blockZ + qz * (CityLayout.PLOT_SIZE + 1);
                            house(new BoundingBox(px, minY, pz, px + CityLayout.PLOT_SIZE - 1, maxY, pz + CityLayout.PLOT_SIZE - 1),
                                    facing(bx, bz, qx, qz));
                        }
                    }
                }
                lamps(blockX, blockZ);
            }
        }

        // The styled pieces this cell builds: the noble district's and the walls.
        for (rafasconquest.style.CityPlan.Piece p : styled.inCell(site.cells(), cellX, cellZ)) {
            if (p.kind() == rafasconquest.style.CityPlan.Kind.POST) {
                pieces.addPiece(new RecruitmentPostPiece(RecruitmentPost.of(site), plate));
                continue;
            }
            rafasconquest.style.StylePiece.at(p.file(), ox + p.minX(), plate, oz + p.minZ(), p.turns()).forEach(pieces::addPiece);
        }

        // Walls last, so they stand on top of the edge road (the mod's own, where the style has no walls of its own).
        if (!styled.styledWalls()) {
            buildWalls(cellX, cellZ);
        }
        // A port's wharf and piers, outside its harbour wall.
        Harbour.build(ctx, site, cellX, cellZ, plate, pieces);
    }

    /** The stretches of [from, to] outside [cutFrom, cutTo] (all of it if the cut is empty). */
    private static List<int[]> runs(int from, int to, int cutFrom, int cutTo) {
        if (cutTo < cutFrom || cutTo < from || cutFrom > to) {
            return List.of(new int[]{from, to});
        }
        List<int[]> runs = new java.util.ArrayList<>();
        if (cutFrom > from) {
            runs.add(new int[]{from, cutFrom - 1});
        }
        if (cutTo < to) {
            runs.add(new int[]{cutTo + 1, to});
        }
        return runs;
    }

    /** Whether a nation holds the city's province (by the world map): only such a city has a recruitment post. */
    public static boolean heldByANation(CityLayout.Site city) {
        rafasconquest.map.BaseMap map = ConquestRuntime.map();
        return map != null && map.holderOf(map.provinceIndexAt(city.x(), city.z())) != null;
    }

    /**
     * Which side of a plot its entrance faces: one of the two roads it touches, but never the wall. The 4 plots in
     * the city's corners touch only the wall, so they face the alley between plots instead.
     */
    private Direction facing(int bx, int bz, int qx, int qz) {
        int last = site.blocksPerSide() - 1;
        Direction xRoad = qx == 0 ? Direction.WEST : Direction.EAST;
        Direction zRoad = qz == 0 ? Direction.NORTH : Direction.SOUTH;
        boolean xIsWall = (qx == 0 && bx == 0) || (qx == 1 && bx == last);
        boolean zIsWall = (qz == 0 && bz == 0) || (qz == 1 && bz == last);
        boolean pickX = random.nextBoolean();
        if (xIsWall && zIsWall) {
            return pickX ? xRoad.getOpposite() : zRoad.getOpposite();
        }
        if (xIsWall) {
            return zRoad;
        }
        if (zIsWall) {
            return xRoad;
        }
        return pickX ? xRoad : zRoad;
    }

    /**
     * This cell's share of the wall ring. The first segment of each side in the cell is placed from the layout; each
     * following one is joined to the previous segment through its wall_end → wall_start jigsaws. The layout position
     * is checked against the jigsaw position, so a template with misplaced connectors shows up in the log.
     */
    private void buildWalls(int cellX, int cellZ) {
        PlacedWall previous = null;
        for (CityLayout.WallSegment seg : CityLayout.wallSegments(site.cells(), cellX, cellZ)) {
            StructurePoolElement element = firstElement(seg.gate() ? variant.gate() : seg.corner() ? variant.corner() : variant.wall());
            if (element == null) {
                return;
            }
            Rotation rotation = Rotation.values()[seg.quarterTurns()];
            int x = site.originX() + seg.originX();
            int z = site.originZ() + seg.originZ();
            // Rigid on the plate: its bottom layer replaces the plate's surface, like a street.
            BlockPos laidOut = new BlockPos(x, plate, z);
            BlockPos pos = laidOut;
            if (previous != null && previous.segment.side() == seg.side() && previous.segment.index() == seg.index() - 1) {
                BlockPos joined = joinWall(previous, element, rotation);
                if (joined == null || joined.getX() != laidOut.getX() || joined.getZ() != laidOut.getZ()) {
                    RafasConquest.LOGGER.warn("City wall segment {} {} does not line up with its jigsaw (layout {}, jigsaw {})",
                            seg.side(), seg.index(), laidOut, joined);
                } else {
                    pos = joined;
                }
            }
            pieces.addPiece(new PoolElementStructurePiece(templates, element, pos, element.getGroundLevelDelta(), rotation,
                    element.getBoundingBox(templates, pos, rotation), net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings.APPLY_WATERLOGGING));
            previous = new PlacedWall(seg, element, pos, rotation);
        }
    }

    private record PlacedWall(CityLayout.WallSegment segment, StructurePoolElement element, BlockPos pos, Rotation rotation) {
    }

    /** Where {@code next} goes so its wall_start jigsaw meets the previous segment's wall_end jigsaw, or null. */
    private BlockPos joinWall(PlacedWall previous, StructurePoolElement next, Rotation rotation) {
        for (StructureTemplate.StructureBlockInfo end : previous.element.getShuffledJigsawBlocks(templates, previous.pos, previous.rotation, random)) {
            if (end.nbt() == null || !WALL_END.equals(end.nbt().getString("name"))) {
                continue;
            }
            for (StructureTemplate.StructureBlockInfo start : next.getShuffledJigsawBlocks(templates, BlockPos.ZERO, rotation, random)) {
                if (JigsawBlock.canAttach(end, start)) {
                    return end.pos().relative(JigsawBlock.getFrontFacing(end.state())).subtract(start.pos());
                }
            }
        }
        return null;
    }

    private StructurePoolElement firstElement(ResourceKey<StructureTemplatePool> key) {
        Optional<? extends Holder<StructureTemplatePool>> pool = pools.getHolder(key);
        if (pool.isEmpty()) {
            return null;
        }
        for (StructurePoolElement element : pool.get().value().getShuffledTemplates(random)) {
            if (element != EmptyPoolElement.INSTANCE) {
                return element;
            }
        }
        return null;
    }

    /** Places a building on the plot with its entrance on the plot edge facing {@code toRoad}. */
    /** This city's houses in its style, if it has any (worked out once). */
    private rafasconquest.style.CityStyles.Houses styledHouses;

    private rafasconquest.style.CityStyles.Houses styledHouses() {
        if (styledHouses == null) {
            styledHouses = rafasconquest.style.CityStyles.houses(site);
        }
        return styledHouses;
    }

    /** A block filled with the style's houses (BlockPacker): as many as fit, any size, each facing the nearer road. */
    private void packBlock(int blockX, int blockZ) {
        rafasconquest.style.CityStyles.Houses houses = styledHouses();
        int size = 2 * CityLayout.PLOT_SIZE + 1;
        java.util.Random packing = new java.util.Random(random.nextLong());
        for (rafasconquest.style.BlockPacker.Placed p : rafasconquest.style.BlockPacker.pack(size, houses.sizes(), packing)) {
            rafasconquest.style.StylePiece.at(houses.files().get(p.building()), blockX + p.x(), plate, blockZ + p.z(), p.turns())
                    .forEach(pieces::addPiece);
        }
    }

    private void house(BoundingBox plot, Direction toRoad) {
        int half = CityLayout.PLOT_SIZE / 2;
        int ex = switch (toRoad) {
            case WEST -> plot.minX();
            case EAST -> plot.maxX();
            default -> plot.minX() + half;
        };
        int ez = switch (toRoad) {
            case NORTH -> plot.minZ();
            case SOUTH -> plot.maxZ();
            default -> plot.minZ() + half;
        };
        Optional<? extends Holder<StructureTemplatePool>> pool = pools.getHolder(variant.houses());
        if (pool.isEmpty()) {
            return;
        }
        int attempts = 0;
        // Shuffled templates repeat each element by its weight; try each distinct element once.
        java.util.Set<StructurePoolElement> tried = new java.util.HashSet<>();
        for (StructurePoolElement element : pool.get().value().getShuffledTemplates(random)) {
            if (element == EmptyPoolElement.INSTANCE || element.toString().contains("/zombie/") || !tried.add(element)) {
                continue;
            }
            if (++attempts > HOUSE_ATTEMPTS) {
                return;
            }
            for (Rotation rotation : Rotation.getShuffled(random)) {
                StructureTemplate.StructureBlockInfo entrance = entranceFacing(element, rotation, toRoad);
                if (entrance == null) {
                    continue;
                }
                // Like vanilla attaching a rigid house to a terrain-following street: the entrance jigsaw sits on the
                // first free block above the ground, not in it. One lower sinks the house and its villagers.
                int entranceY = plate + 1;
                BlockPos pos = new BlockPos(ex - entrance.pos().getX(), entranceY - entrance.pos().getY(), ez - entrance.pos().getZ());
                BoundingBox box = element.getBoundingBox(templates, pos, rotation);
                if (box.minX() >= plot.minX() && box.maxX() <= plot.maxX() && box.minZ() >= plot.minZ() && box.maxZ() <= plot.maxZ()) {
                    place(element, pos, rotation, box, 0);
                    return;
                }
            }
        }
    }

    private StructureTemplate.StructureBlockInfo entranceFacing(StructurePoolElement element, Rotation rotation, Direction toRoad) {
        for (StructureTemplate.StructureBlockInfo info : element.getShuffledJigsawBlocks(templates, BlockPos.ZERO, rotation, random)) {
            if (info.nbt() != null && ENTRANCE.equals(info.nbt().getString("name")) && JigsawBlock.getFrontFacing(info.state()) == toRoad) {
                return info;
            }
        }
        return null;
    }

    /** The vanilla town centre (well or meeting point), centred on the middle block. */
    private void townCenter(int blockX, int blockZ) {
        Optional<? extends Holder<StructureTemplatePool>> pool = pools.getHolder(variant.townCenters());
        if (pool.isEmpty()) {
            return;
        }
        int cx = blockX + (CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH) / 2;
        int cz = blockZ + (CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH) / 2;
        for (StructurePoolElement element : pool.get().value().getShuffledTemplates(random)) {
            if (element == EmptyPoolElement.INSTANCE || element.toString().contains("/zombie/")) {
                continue;
            }
            Rotation rotation = Rotation.getRandom(random);
            BoundingBox at = element.getBoundingBox(templates, new BlockPos(cx, 0, cz), rotation);
            // Bottom layer replaces the plate's surface, as vanilla does for a village's town centre.
            BlockPos pos = new BlockPos(cx + (cx - at.getCenter().getX()), plate - at.minY(), cz + (cz - at.getCenter().getZ()));
            place(element, pos, rotation, element.getBoundingBox(templates, pos, rotation), 0);
            return;
        }
    }

    /**
     * Lamp posts in the city block's alleys: where each alley meets the road (lighting the street about every 15
     * blocks) and where the alleys cross. Each uses this style's own lamp if it fits without touching a building,
     * otherwise the 1-wide taiga lamp post, which always fits an alley.
     */
    private void lamps(int blockX, int blockZ) {
        int alley = CityLayout.PLOT_SIZE;
        int last = CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH - 1;
        int[][] spots = {{alley, 1}, {alley, last - 1}, {1, alley}, {last - 1, alley}, {alley, alley}};
        BoundingBox block = new BoundingBox(blockX, ctx.heightAccessor().getMinBuildHeight(), blockZ,
                blockX + last, ctx.heightAccessor().getMaxBuildHeight() - 1, blockZ + last);
        List<StructurePoolElement> own = lampElements(variant.decor());
        List<StructurePoolElement> fallback = lampElements(CityVariant.TAIGA.decor());
        for (int[] spot : spots) {
            if (!lamp(own, blockX + spot[0], blockZ + spot[1], block)) {
                lamp(fallback, blockX + spot[0], blockZ + spot[1], block);
            }
        }
    }

    /** Places the first lamp from {@code candidates} that fits centred on (x, z). Returns false if none fits. */
    private boolean lamp(List<StructurePoolElement> candidates, int x, int z, BoundingBox block) {
        for (StructurePoolElement element : candidates) {
            for (Rotation rotation : Rotation.getShuffled(random)) {
                BoundingBox at = element.getBoundingBox(templates, BlockPos.ZERO, rotation);
                // Seat it like vanilla seats decor on a street: its downward jigsaw on the first block above the ground.
                int baseY = at.minY();
                for (StructureTemplate.StructureBlockInfo jigsaw : element.getShuffledJigsawBlocks(templates, BlockPos.ZERO, rotation, random)) {
                    if (JigsawBlock.getFrontFacing(jigsaw.state()) == Direction.DOWN) {
                        baseY = jigsaw.pos().getY();
                        break;
                    }
                }
                BlockPos pos = new BlockPos(x - (at.minX() + at.maxX()) / 2, plate + 1 - baseY, z - (at.minZ() + at.maxZ()) / 2);
                BoundingBox box = element.getBoundingBox(templates, pos, rotation);
                boolean inside = box.minX() >= block.minX() && box.maxX() <= block.maxX() && box.minZ() >= block.minZ() && box.maxZ() <= block.maxZ();
                if (inside && occupied.stream().noneMatch(box::intersects)) {
                    place(element, pos, rotation, box, MAX_CHILD_DEPTH);
                    return true;
                }
            }
        }
        return false;
    }

    /** The lamp posts in a decor pool, each once, in random order. */
    private List<StructurePoolElement> lampElements(ResourceKey<StructureTemplatePool> decorPool) {
        Optional<? extends Holder<StructureTemplatePool>> pool = pools.getHolder(decorPool);
        if (pool.isEmpty()) {
            return List.of();
        }
        return pool.get().value().getShuffledTemplates(random).stream()
                .filter(e -> e.toString().contains("lamp"))
                .distinct()
                .toList();
    }

    private void place(StructurePoolElement element, BlockPos pos, Rotation rotation, BoundingBox box, int depth) {
        occupied.add(box);
        pieces.addPiece(new PoolElementStructurePiece(templates, element, pos, element.getGroundLevelDelta(), rotation, box, net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings.APPLY_WATERLOGGING));
        if (depth >= MAX_CHILD_DEPTH) {
            return;
        }
        for (StructureTemplate.StructureBlockInfo jigsaw : element.getShuffledJigsawBlocks(templates, pos, rotation, random)) {
            String pool = jigsaw.nbt() == null ? "" : jigsaw.nbt().getString("pool");
            // The grid replaces vanilla's streets; everything else (villagers, cats, golems, decor) is attached as normal.
            if (pool.isEmpty() || pool.equals("minecraft:empty") || pool.contains("/streets") || pool.contains("/houses")
                    || pool.contains("/terminators")) {
                continue;
            }
            attachChild(jigsaw, pool, depth + 1);
        }
    }

    private void attachChild(StructureTemplate.StructureBlockInfo parent, String poolName, int depth) {
        ResourceLocation id = ResourceLocation.tryParse(poolName);
        Optional<? extends Holder<StructureTemplatePool>> pool = id == null ? Optional.empty()
                : pools.getHolder(ResourceKey.create(Registries.TEMPLATE_POOL, id));
        if (pool.isEmpty()) {
            return;
        }
        BlockPos target = parent.pos().relative(JigsawBlock.getFrontFacing(parent.state()));
        List<StructurePoolElement> candidates = pool.get().value().getShuffledTemplates(random);
        for (StructurePoolElement child : candidates.subList(0, Math.min(candidates.size(), 4))) {
            if (child == EmptyPoolElement.INSTANCE) {
                return;
            }
            for (Rotation rotation : Rotation.getShuffled(random)) {
                for (StructureTemplate.StructureBlockInfo own : child.getShuffledJigsawBlocks(templates, BlockPos.ZERO, rotation, random)) {
                    if (JigsawBlock.canAttach(parent, own)) {
                        BlockPos pos = target.subtract(own.pos());
                        place(child, pos, rotation, child.getBoundingBox(templates, pos, rotation), depth);
                        return;
                    }
                }
            }
        }
    }
}
