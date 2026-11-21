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
import rafasconquest.core.Barracks;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.SeedHash;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds one cell of a city's planned grid: first its share of the flattened ground ({@link CityTerrainPiece}), then
 * its roads, a vanilla building on every plot (or the town centre on the middle block), and its part of the wall,
 * each on the tiles of the shaped ground ({@link CityTerrain}), buildings on a footprint levelled to the road at their door. Buildings are turned so their entrance faces a road and placed against it;
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
    /** The plate: the harbour's ground level (each tile has its own, {@link #y}). */
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

    /** An outside noble district's platform plan and its square's world corner. */
    public record Platform(CityTerrain.Plan plan, int x, int z) {
    }

    private static final Map<Long, Optional<Platform>> PLATFORMS = new ConcurrentHashMap<>();

    /** The platform of a city's outside noble district ({@link rafasconquest.core.NobleAnnex}), or null if it has none. */
    @org.jetbrains.annotations.Nullable
    public static Platform platform(Structure.GenerationContext ctx, CityLayout.Site site) {
        int[] a = rafasconquest.style.CityPlan.of(site).annex();
        if (a == null) {
            return null;
        }
        long key = SeedHash.hash(ctx.seed(), site.x(), site.z());
        if (PLATFORMS.size() >= MAX_CACHED_PLANS) {
            PLATFORMS.clear();
        }
        return PLATFORMS.computeIfAbsent(key, k -> {
            int x = site.originX() + a[0];
            int z = site.originZ() + a[1];
            int plate = terrainPlan(ctx, site).plateY() + rafasconquest.core.NobleAnnex.RISE;
            return Optional.of(new Platform(CityTerrain.platform(x, z, a[2], a[3], plate, (hx, hz) -> surface(ctx, hx, hz)), x, z));
        }).orElse(null);
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
        boolean port = Harbour.sideOf(site) != null;
        List<CityTerrain.Pin> pins = pins(site, outset);
        CityTerrain.plan(site, (x, z) -> {
            wanted.add(xz(x, z));
            return sea;
        }, sea, outset, port, pins);
        java.util.Map<Long, Integer> read = new java.util.concurrent.ConcurrentHashMap<>();
        // On a pool of its own, a quarter of the threads: the rest stay free for the world generation going on meanwhile.
        SURVEY.submit(() -> wanted.parallelStream().forEach(k -> read.put(k, surface(ctx, (int) (k >> 32), (int) (long) k)))).join();
        CityTerrain.Plan plan = CityTerrain.plan(site, (x, z) -> read.computeIfAbsent(xz(x, z), k -> surface(ctx, x, z)), sea, outset, port, pins);
        int[] counts = new int[CityTerrain.Edge.values().length];
        for (CityTerrain.Edge[] side : plan.edges()) {
            for (CityTerrain.Edge e : side) {
                counts[e.ordinal()]++;
            }
        }
        RafasConquest.LOGGER.info("Planned city terrain at {}, {}: plate y {}, edges {} carve / {} fill, in {} ms",
                site.x(), site.z(), plan.plateY(), counts[0], counts[1],(System.nanoTime() - start) / 1_000_000);
        return plan;
    }

    /**
     * Where a city's ground must stand flat, in the order the tile field lays them: a port's harbour side and the path
     * out to an outside noble district at the plate (their pieces are built there), then the noble district, the town
     * centre's block, the Barracks' block, the plan's other buildings, and each wall segment.
     */
    static List<CityTerrain.Pin> pins(CityLayout.Site site, int outset) {
        List<CityTerrain.Pin> pins = new java.util.ArrayList<>();
        rafasconquest.style.CityPlan.Plan styled = rafasconquest.style.CityPlan.of(site);
        int min = CityTerrain.boxMin(site.cells(), outset);
        int max = CityTerrain.boxMax(site.cells(), outset);
        CityLayout.Side harbour = Harbour.sideOf(site);
        if (harbour != null) {
            int in = CityLayout.ROAD_WIDTH - 1;
            int far = site.size() - CityLayout.ROAD_WIDTH;
            pins.add(switch (harbour) {
                case NORTH -> new CityTerrain.Pin(min, min, max, in, true);
                case SOUTH -> new CityTerrain.Pin(min, far, max, max, true);
                case WEST -> new CityTerrain.Pin(min, min, in, max, true);
                case EAST -> new CityTerrain.Pin(far, min, max, max, true);
            });
        }
        if (!styled.path().isEmpty()) {
            int[] r = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            for (int[] c : styled.path()) {
                r = new int[]{Math.min(r[0], c[0]), Math.min(r[1], c[1]), Math.max(r[2], c[0]), Math.max(r[3], c[1])};
            }
            pins.add(new CityTerrain.Pin(r[0], r[1], r[2], r[3], true));
        }
        if (styled.noble()) {
            pins.add(new CityTerrain.Pin(styled.areaMinX(), styled.areaMinZ(), styled.areaMaxX(), styled.areaMaxZ(), false));
        }
        pins.add(blockPin(site.centerBlock(), site.centerBlock()));
        pins.add(blockPin(Barracks.blockX(site), Barracks.blockZ(site)));
        for (rafasconquest.style.CityPlan.Piece p : styled.pieces()) {
            boolean inNoble = styled.noble() && p.centreX() >= styled.areaMinX() && p.centreX() <= styled.areaMaxX()
                    && p.centreZ() >= styled.areaMinZ() && p.centreZ() <= styled.areaMaxZ();
            if (!styled.inAnnex(p) && !inNoble) {
                pins.add(new CityTerrain.Pin(p.minX(), p.minZ(), p.minX() + p.w() - 1, p.minZ() + p.d() - 1, false));
            }
        }
        if (!styled.styledWalls()) {
            for (CityLayout.WallSegment seg : CityLayout.wallSegments(site.cells())) {
                int[] a = CityLayout.wallBlock(seg, 0, 0);
                int[] b = CityLayout.wallBlock(seg, seg.length() - 1, CityLayout.WALL_WIDTH - 1);
                pins.add(new CityTerrain.Pin(Math.min(a[0], b[0]), Math.min(a[1], b[1]), Math.max(a[0], b[0]), Math.max(a[1], b[1]), false));
            }
        }
        return pins;
    }

    /** City block (bx, bz), out to the roads about it. */
    private static CityTerrain.Pin blockPin(int bx, int bz) {
        return new CityTerrain.Pin(CityLayout.blockStart(bx), CityLayout.blockStart(bz), CityLayout.roadStart(bx + 1) - 1, CityLayout.roadStart(bz + 1) - 1, false);
    }

    /** The ground's level at world (x, z): its tile's. */
    private int y(int x, int z) {
        return plan.level(x - site.originX(), z - site.originZ());
    }

    /**
     * A road over world [x0, x1] × [z0, z1], each column at the level of the tile its centre line crosses there, so it
     * only steps along its length: the centre line at city-local {@code across}, or the road's own middle if MIN_VALUE.
     */
    private void roadPiece(int x0, int z0, int x1, int z1, boolean paved, int across) {
        boolean alongX = x1 - x0 >= z1 - z0;
        int centre = across != Integer.MIN_VALUE ? across : alongX ? (z0 + z1) / 2 - site.originZ() : (x0 + x1) / 2 - site.originX();
        int[] ys = new int[(x1 - x0 + 1) * (z1 - z0 + 1)];
        int lo = Integer.MAX_VALUE;
        int hi = Integer.MIN_VALUE;
        for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
                int y = alongX ? plan.level(x - site.originX(), centre) : plan.level(centre, z - site.originZ());
                ys[(x - x0) * (z1 - z0 + 1) + z - z0] = y;
                lo = Math.min(lo, y);
                hi = Math.max(hi, y);
            }
        }
        pieces.addPiece(new CityRoadPiece(new BoundingBox(x0, lo, z0, x1, hi + 1, z1),
                (paved ? net.minecraft.world.level.block.Blocks.COBBLESTONE : net.minecraft.world.level.block.Blocks.GRAVEL).defaultBlockState(),
                paved ? net.minecraft.world.level.block.Blocks.COBBLESTONE_SLAB.defaultBlockState() : rafasconquest.content.ConquestContent.GRAVEL_SLAB.get().defaultBlockState(), ys));
    }

    /** The ground under a building's footprint, flat at {@code y}: laid before the building, which stands on it. */
    private void foundation(BoundingBox footprint, int y) {
        int[] ys = new int[footprint.getXSpan() * footprint.getZSpan()];
        java.util.Arrays.fill(ys, y);
        pieces.addPiece(new CityRoadPiece(new BoundingBox(footprint.minX(), y, footprint.minZ(), footprint.maxX(), y + 1, footprint.maxZ()),
                variant.ground(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), ys));
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

        // The ground first: everything else in the cell is built on the shaped tiles.
        pieces.addPiece(new CityTerrainPiece(site, cellX, cellZ, variant, plan, plan.minLevel() - SPAWN_PROTECTION_DEPTH, maxY, platform(ctx, site)));

        // The styled walls and the noble district, laid out for the whole city.
        rafasconquest.style.CityPlan.Plan styled = rafasconquest.style.CityPlan.of(site); // by its founder, held or not
        boolean noble = styled.noble();
        // The plan's buildings outside the noble district (the church, a big Barracks) take the plots they cover.
        List<rafasconquest.style.CityPlan.Piece> claims = styled.pieces().stream()
                .filter(p -> p.kind() != rafasconquest.style.CityPlan.Kind.WALL && p.kind() != rafasconquest.style.CityPlan.Kind.NOBLE).toList();

        // Roads next, so buildings placed afterwards win wherever they touch; none inside the noble district, nor
        // between the blocks merged under one of the plan's buildings.
        for (int k : CityLayout.roadsInCell(site.cells(), cellX)) {
            int x = ox + CityLayout.roadStart(k);
            boolean cut = noble && k > styled.x0() && k < styled.x1();
            for (int[] run : merged(runs(cellMinZ - oz, cellMaxZ - oz, cut ? styled.areaMinZ() : 0, cut ? styled.areaMaxZ() : -1), k, true, claims)) {
                roadPiece(x, oz + run[0], x + CityLayout.ROAD_WIDTH - 1, oz + run[1], false, Integer.MIN_VALUE);
            }
        }
        for (int k : CityLayout.roadsInCell(site.cells(), cellZ)) {
            int z = oz + CityLayout.roadStart(k);
            boolean cut = noble && k > styled.z0() && k < styled.z1();
            for (int[] run : merged(runs(cellMinX - ox, cellMaxX - ox, cut ? styled.areaMinX() : 0, cut ? styled.areaMaxX() : -1), k, false, claims)) {
                roadPiece(ox + run[0], z, ox + run[1], z + CityLayout.ROAD_WIDTH - 1, false, Integer.MIN_VALUE);
            }
        }
        // The gate avenues' extra width, on the side toward the origin.
        int gate = site.blocksPerSide() / 2;
        int extra = CityLayout.AVENUE_EXTRA;
        int g = CityLayout.roadStart(gate);
        // At the avenue's own level, so it never steps across its width.
        int avenue = g + CityLayout.ROAD_WIDTH / 2;
        if (CityLayout.roadsInCell(site.cells(), cellX).contains(gate)) {
            road(g - extra, cellMinZ - oz, g - 1, cellMaxZ - oz, styled, avenue);
        }
        if (CityLayout.roadsInCell(site.cells(), cellZ).contains(gate)) {
            road(cellMinX - ox, g - extra, cellMaxX - ox, g - 1, styled, avenue);
        }
        // The ring road just inside the wall, on the edge cells.
        int far = site.size() - 1;
        int in0 = CityLayout.ROAD_WIDTH;
        int in1 = far - CityLayout.ROAD_WIDTH;
        int ring = CityLayout.RING_ROAD_WIDTH;
        int x0 = Math.max(cellMinX - ox, in0);
        int x1 = Math.min(cellMaxX - ox, in1);
        int z0 = Math.max(cellMinZ - oz, in0);
        int z1 = Math.min(cellMaxZ - oz, in1);
        int last = site.cells() - 1;
        if (cellZ == 0) {
            road(x0, in0, x1, in0 + ring - 1, styled, Integer.MIN_VALUE);
        }
        if (cellZ == last) {
            road(x0, in1 - ring + 1, x1, in1, styled, Integer.MIN_VALUE);
        }
        if (cellX == 0) {
            road(in0, z0, in0 + ring - 1, z1, styled, Integer.MIN_VALUE);
        }
        if (cellX == last) {
            road(in1 - ring + 1, z0, in1, z1, styled, Integer.MIN_VALUE);
        }

        for (int bx : CityLayout.blocksInCell(cellX)) {
            for (int bz : CityLayout.blocksInCell(cellZ)) {
                int blockX = ox + CityLayout.blockStart(bx);
                int blockZ = oz + CityLayout.blockStart(bz);
                occupied.clear();
                if (noble && styled.nobleBlock(bx, bz)) {
                    continue; // the noble district's, below
                }
                int[] bsx = CityLayout.blockSpan(site.blocksPerSide(), bx);
                int[] bsz = CityLayout.blockSpan(site.blocksPerSide(), bz);
                List<rafasconquest.style.CityPlan.Piece> here = claims.stream().filter(p -> overlaps(p, bsx[0], bsz[0], bsx[1] - bsx[0] + 1, bsz[1] - bsz[0] + 1)).toList();
                if (bx == site.centerBlock() && bz == site.centerBlock() && here.isEmpty()) {
                    townCenter(blockX, blockZ); // unless the church stands on it (a city whose noble district is outside)
                } else if (!(bx == Barracks.blockX(site) && bz == Barracks.blockZ(site)) && styledHouses().any()) {
                    packBlock(bx, bz, here); // the style's own houses, of any size
                } else {
                    for (int qx = 0; qx < 2; qx++) {
                        for (int qz = 0; qz < 2; qz++) {
                            boolean postBlock = bx == Barracks.blockX(site) && bz == Barracks.blockZ(site);
                            if (qx == 0 && qz == 0 && postBlock && heldByANation(site)
                                    && !styled.has(rafasconquest.style.CityPlan.Kind.BARRACKS)) { // a Barracks too big for the plot has its own ground
                                // The small Barracks: from the style folders where its slot has a file, else the mod's own.
                                String barracks = rafasconquest.style.CityStyles.barracks(site);
                                int ground = y(blockX, blockZ); // the Barracks' block is pinned flat
                                java.util.List<rafasconquest.style.StylePiece> styledPiece = barracks == null ? java.util.List.of() : rafasconquest.style.StylePiece.of(barracks,
                                        blockX + CityLayout.PLOT_SIZE / 2, ground, blockZ + CityLayout.PLOT_SIZE / 2, rafasconquest.style.CityStyles.TURNS_TO_WEST);
                                if (styledPiece.isEmpty()) {
                                    pieces.addPiece(new BarracksPiece(Barracks.of(site), ground));
                                }
                                styledPiece.forEach(pieces::addPiece);
                                continue; // the barracks has this plot
                            }
                            if (!noble && styled.annex() == null && qx == 0 && qz == 1 && postBlock && heldByANation(site)) {
                                int[] church = Barracks.of(site).church();
                                // The Palace: from the style folders where its slot has a file, else the gold church.
                                String palace = rafasconquest.style.CityStyles.palace(site);
                                int ground = y(blockX, blockZ);
                                java.util.List<rafasconquest.style.StylePiece> styledPiece = palace == null ? java.util.List.of() : rafasconquest.style.StylePiece.of(palace,
                                        church[0] + CityLayout.PLOT_SIZE / 2, ground, church[1] + CityLayout.PLOT_SIZE / 2, rafasconquest.style.CityStyles.TURNS_TO_WEST);
                                if (styledPiece.isEmpty()) {
                                    pieces.addPiece(new GoldChurchPiece(church[0], church[1], ground));
                                }
                                styledPiece.forEach(pieces::addPiece);
                                continue; // and the gold church, the one beside it
                            }
                            int px = blockX + qx * (CityLayout.PLOT_SIZE + 1);
                            int pz = blockZ + qz * (CityLayout.PLOT_SIZE + 1);
                            // Plots against the ring road or the wide avenue are that much shallower.
                            int[] sx = CityLayout.blockSpan(site.blocksPerSide(), bx);
                            int[] sz = CityLayout.blockSpan(site.blocksPerSide(), bz);
                            if (here.stream().anyMatch(p -> overlaps(p, px - ox, pz - oz, CityLayout.PLOT_SIZE, CityLayout.PLOT_SIZE))) {
                                continue; // merged under one of the plan's buildings
                            }
                            house(new BoundingBox(Math.max(px, ox + sx[0]), minY, Math.max(pz, oz + sz[0]),
                                            Math.min(px + CityLayout.PLOT_SIZE - 1, ox + sx[1]), maxY, Math.min(pz + CityLayout.PLOT_SIZE - 1, oz + sz[1])),
                                    facing(bx, bz, qx, qz));
                        }
                    }
                }
                if (here.isEmpty()) {
                    lamps(bx, bz); // the alleys of merged plots are gone
                }
            }
        }

        // The noble district's cobbled roads, this cell's share, before its buildings so they win where they touch.
        for (int[] r : styled.roads()) {
            if (styled.inAnnex(r)) {
                continue; // the outside district's tiles lay its own
            }
            int rx0 = Math.max(r[0], cellMinX - ox);
            int rx1 = Math.min(r[2], cellMaxX - ox);
            int rz0 = Math.max(r[1], cellMinZ - oz);
            int rz1 = Math.min(r[3], cellMaxZ - oz);
            if (rx0 <= rx1 && rz0 <= rz1) {
                roadPiece(ox + rx0, oz + rz0, ox + rx1, oz + rz1, true, Integer.MIN_VALUE);
            }
        }
        // The styled pieces this cell builds: the noble district's, the church's and the walls.
        for (rafasconquest.style.CityPlan.Piece p : styled.inCell(site.cells(), cellX, cellZ)) {
            if (p.kind() == rafasconquest.style.CityPlan.Kind.SQUARE) {
                townCenter(FOUNTAIN, ox + p.centreX(), oz + p.centreZ()); // shortcut: the plains fountain always; later from the data packs
                continue;
            }
            // Its ground is pinned flat (the noble district's is one level), so its middle's tile stands for it all.
            rafasconquest.style.StylePiece.at(p.file(), ox + p.minX(), y(ox + p.centreX(), oz + p.centreZ()), oz + p.minZ(), p.turns()).forEach(pieces::addPiece);
        }

        // Walls last, so they stand on top of the edge road (the mod's own, where the style has no walls of its own).
        if (!styled.styledWalls()) {
            buildWalls(cellX, cellZ);
        }
        // A port's wharf and piers, outside its harbour wall.
        Harbour.build(ctx, site, cellX, cellZ, plate, pieces);
    }

    /** A road over city-local [x0, x1] × [z0, z1], less any part inside the noble district. */
    private void road(int x0, int z0, int x1, int z1, rafasconquest.style.CityPlan.Plan styled, int across) {
        boolean alongX = x1 - x0 >= z1 - z0;
        boolean crosses = styled.noble() && (alongX
                ? z1 >= styled.areaMinZ() && z0 <= styled.areaMaxZ()
                : x1 >= styled.areaMinX() && x0 <= styled.areaMaxX());
        int from = alongX ? x0 : z0;
        int to = alongX ? x1 : z1;
        for (int[] run : runs(from, to, crosses ? (alongX ? styled.areaMinX() : styled.areaMinZ()) : 0,
                crosses ? (alongX ? styled.areaMaxX() : styled.areaMaxZ()) : -1)) {
            int ox = site.originX();
            int oz = site.originZ();
            if (alongX) {
                roadPiece(ox + run[0], oz + z0, ox + run[1], oz + z1, false, across);
            } else {
                roadPiece(ox + x0, oz + run[0], ox + x1, oz + run[1], false, across);
            }
        }
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

    /** Whether a piece overlaps city-local [x, x + w) × [z, z + d). */
    private static boolean overlaps(rafasconquest.style.CityPlan.Piece p, int x, int z, int w, int d) {
        return x < p.minX() + p.w() && p.minX() < x + w && z < p.minZ() + p.d() && p.minZ() < z + d;
    }

    /**
     * The stretches of {@code runs} along road k (running along z if {@code alongZ}) left where none of {@code claims}
     * stands across it: a building across a road gets the whole of the blocks it covers, the road between them gone.
     */
    private static List<int[]> merged(List<int[]> runs, int k, boolean alongZ, List<rafasconquest.style.CityPlan.Piece> claims) {
        int r0 = CityLayout.roadStart(k);
        for (rafasconquest.style.CityPlan.Piece p : claims) {
            int a0 = alongZ ? p.minX() : p.minZ();
            int a1 = a0 + (alongZ ? p.w() : p.d()) - 1;
            if (a1 < r0 || a0 > r0 + CityLayout.ROAD_WIDTH - 1) {
                continue;
            }
            int c0 = alongZ ? p.minZ() : p.minX();
            int c1 = c0 + (alongZ ? p.d() : p.w()) - 1;
            int from = CityLayout.blockStart(Math.floorDiv(c0, CityLayout.ROAD_PERIOD));
            int to = CityLayout.roadStart(Math.floorDiv(c1, CityLayout.ROAD_PERIOD) + 1) - 1;
            List<int[]> left = new java.util.ArrayList<>();
            for (int[] run : runs) {
                left.addAll(runs(run[0], run[1], from, to));
            }
            runs = left;
        }
        return runs;
    }

    /** Whether a nation holds the city's province (by the world map): only such a city has a barracks. */
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
            // Rigid on its pinned ground: its bottom layer replaces the surface, like a street.
            int[] middle = CityLayout.wallBlock(seg, seg.length() / 2, CityLayout.WALL_WIDTH / 2);
            BlockPos laidOut = new BlockPos(x, plan.level(middle[0], middle[1]), z);
            BlockPos pos = laidOut;
            if (previous != null && previous.segment.side() == seg.side() && previous.segment.index() == seg.index() - 1) {
                BlockPos joined = joinWall(previous, element, rotation);
                if (joined == null || joined.getX() != laidOut.getX() || joined.getZ() != laidOut.getZ()) {
                    RafasConquest.LOGGER.warn("City wall segment {} {} does not line up with its jigsaw (layout {}, jigsaw {})",
                            seg.side(), seg.index(), laidOut, joined);
                } else {
                    pos = joined.atY(laidOut.getY()); // each segment on its own ground: it may stand a step off the last
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
    private void packBlock(int bx, int bz, List<rafasconquest.style.CityPlan.Piece> claims) {
        rafasconquest.style.CityStyles.Houses houses = styledHouses();
        int[] sx = CityLayout.blockSpan(site.blocksPerSide(), bx);
        int[] sz = CityLayout.blockSpan(site.blocksPerSide(), bz);
        int w = sx[1] - sx[0] + 1;
        int d = sz[1] - sz[0] + 1;
        java.util.Random packing = new java.util.Random(random.nextLong());
        List<rafasconquest.style.BlockPacker.Rect> free = List.of(new rafasconquest.style.BlockPacker.Rect(0, 0, w, d));
        for (rafasconquest.style.CityPlan.Piece c : claims) { // the plan's buildings have their ground first
            free = rafasconquest.style.BlockPacker.cut(free, new rafasconquest.style.BlockPacker.Rect(c.minX() - sx[0], c.minZ() - sz[0], c.w(), c.d()));
        }
        for (rafasconquest.style.BlockPacker.Placed p : rafasconquest.style.BlockPacker.pack(free, w, d, houses.sizes(), packing, houseShares(bx, bz))) {
            rafasconquest.style.BlockPacker.Size size = houses.sizes().get(p.building());
            int fw = p.turns() % 2 == 0 ? size.x() : size.z();
            int fd = p.turns() % 2 == 0 ? size.z() : size.x();
            int x = site.originX() + sx[0] + p.x();
            int z = site.originZ() + sz[0] + p.z();
            int ground = y(x + fw / 2, z + fd / 2);
            foundation(new BoundingBox(x, ground, z, x + fw - 1, ground, z + fd - 1), ground);
            rafasconquest.style.StylePiece.at(houses.files().get(p.building()), x, ground, z, p.turns()).forEach(pieces::addPiece);
        }
    }

    /**
     * How many of each house this block may build, or null if none is limited. A city's cells are built apart, so a
     * limited house's allowance is dealt over the city's house blocks in an order fixed by the house: the same on every cell.
     */
    @org.jetbrains.annotations.Nullable
    private int[] houseShares(int bx, int bz) {
        List<String> files = styledHouses().files();
        int[] most = rafasconquest.style.CityStyles.allowances(site, files);
        if (most == null) {
            return null;
        }
        rafasconquest.style.CityPlan.Plan styled = rafasconquest.style.CityPlan.of(site);
        List<int[]> blocks = new java.util.ArrayList<>();
        for (int x = 0; x < site.blocksPerSide(); x++) {
            for (int z = 0; z < site.blocksPerSide(); z++) {
                if (!(styled.noble() && styled.nobleBlock(x, z)) && !(x == site.centerBlock() && z == site.centerBlock())
                        && !(x == Barracks.blockX(site) && z == Barracks.blockZ(site))) {
                    blocks.add(new int[]{x, z});
                }
            }
        }
        for (int i = 0; i < most.length; i++) {
            if (most[i] == Integer.MAX_VALUE || blocks.isEmpty()) {
                continue;
            }
            long seed = files.get(i).hashCode();
            blocks.sort(java.util.Comparator.comparingLong(b -> rafasconquest.style.CityStyles.mix(seed, b[0], b[1])));
            int rank = 0;
            while (rank < blocks.size() && (blocks.get(rank)[0] != bx || blocks.get(rank)[1] != bz)) {
                rank++;
            }
            most[i] = most[i] / blocks.size() + (rank < most[i] % blocks.size() ? 1 : 0);
        }
        return most;
    }

    private void house(BoundingBox plot, Direction toRoad) {
        int ex = switch (toRoad) {
            case WEST -> plot.minX();
            case EAST -> plot.maxX();
            default -> plot.minX() + plot.getXSpan() / 2;
        };
        int ez = switch (toRoad) {
            case NORTH -> plot.minZ();
            case SOUTH -> plot.maxZ();
            default -> plot.minZ() + plot.getZSpan() / 2;
        };
        Optional<? extends Holder<StructureTemplatePool>> pool = pools.getHolder(variant.houses());
        if (pool.isEmpty()) {
            return;
        }
        // The house stands at the road's level just outside its door, its footprint levelled to it.
        int ground = y(ex + toRoad.getStepX(), ez + toRoad.getStepZ());
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
                int entranceY = ground + 1;
                BlockPos pos = new BlockPos(ex - entrance.pos().getX(), entranceY - entrance.pos().getY(), ez - entrance.pos().getZ());
                BoundingBox box = element.getBoundingBox(templates, pos, rotation);
                if (box.minX() >= plot.minX() && box.maxX() <= plot.maxX() && box.minZ() >= plot.minZ() && box.maxZ() <= plot.maxZ()) {
                    foundation(box, ground);
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
            townCenter(element, cx, cz);
            return;
        }
    }

    /** The town square's centrepiece: the village fountain. */
    private static final StructurePoolElement FOUNTAIN = StructurePoolElement.single("minecraft:village/plains/town_centers/plains_fountain_01")
            .apply(StructureTemplatePool.Projection.RIGID);

    /** A town centre piece centred on world (cx, cz), turned at random, its bottom layer replacing the plate's surface as vanilla does. */
    private void townCenter(StructurePoolElement element, int cx, int cz) {
        Rotation rotation = Rotation.getRandom(random);
        BoundingBox at = element.getBoundingBox(templates, new BlockPos(cx, 0, cz), rotation);
        BlockPos pos = new BlockPos(cx + (cx - at.getCenter().getX()), y(cx, cz) - at.minY(), cz + (cz - at.getCenter().getZ()));
        place(element, pos, rotation, element.getBoundingBox(templates, pos, rotation), 0);
    }

    /**
     * Lamp posts in the city block's alleys: where each alley meets the road (lighting the street about every 15
     * blocks) and where the alleys cross. Each uses this style's own lamp if it fits without touching a building,
     * otherwise the 1-wide taiga lamp post, which always fits an alley.
     */
    private void lamps(int bx, int bz) {
        // City-local: the alleys between plots, and the block's buildable edges (clear of the ring road and avenue).
        int[] sx = CityLayout.blockSpan(site.blocksPerSide(), bx);
        int[] sz = CityLayout.blockSpan(site.blocksPerSide(), bz);
        int ax = CityLayout.blockStart(bx) + CityLayout.PLOT_SIZE;
        int az = CityLayout.blockStart(bz) + CityLayout.PLOT_SIZE;
        int[][] spots = {{ax, sz[0] + 1}, {ax, sz[1] - 1}, {sx[0] + 1, az}, {sx[1] - 1, az}, {ax, az}};
        int ox = site.originX();
        int oz = site.originZ();
        BoundingBox block = new BoundingBox(ox + sx[0], ctx.heightAccessor().getMinBuildHeight(), oz + sz[0],
                ox + sx[1], ctx.heightAccessor().getMaxBuildHeight() - 1, oz + sz[1]);
        List<StructurePoolElement> own = lampElements(variant.decor());
        List<StructurePoolElement> fallback = lampElements(CityVariant.TAIGA.decor());
        for (int[] spot : spots) {
            if (!lamp(own, ox + spot[0], oz + spot[1], block)) {
                lamp(fallback, ox + spot[0], oz + spot[1], block);
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
                BlockPos pos = new BlockPos(x - (at.minX() + at.maxX()) / 2, y(x, z) + 1 - baseY, z - (at.minZ() + at.maxZ()) / 2);
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
