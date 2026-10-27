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

    static CityTerrain.Plan terrainPlan(Structure.GenerationContext ctx, CityLayout.Site site) {
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

    private static CityTerrain.Plan planAndLog(Structure.GenerationContext ctx, CityLayout.Site site) {
        long start = System.nanoTime();
        CityTerrain.Plan plan = CityTerrain.plan(site, (x, z) -> ground(ctx, x, z).y(), ctx.chunkGenerator().getSeaLevel());
        int[] counts = new int[CityTerrain.Edge.values().length];
        for (CityTerrain.Edge[] side : plan.edges()) {
            for (CityTerrain.Edge e : side) {
                counts[e.ordinal()]++;
            }
        }
        RafasConquest.LOGGER.info("Planned city terrain at {}, {}: plate y {}, edges {} eased / {} valley / {} cliff, in {} ms",
                site.x(), site.z(), plan.plateY(), counts[0], counts[1], counts[2], (System.nanoTime() - start) / 1_000_000);
        return plan;
    }

    static void build(Structure.GenerationContext ctx, CityLayout.Site site, CityVariant variant, int cellX, int cellZ, StructurePiecesBuilder pieces) {
        new CityGridBuilder(ctx, site, variant, pieces).buildCell(cellX, cellZ);
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
        pieces.addPiece(new CityTerrainPiece(site, cellX, cellZ, variant, plan, minY, maxY));

        // Roads next, so buildings placed afterwards win wherever they touch.
        for (int k : CityLayout.roadsInCell(cellX)) {
            int x = ox + CityLayout.roadStart(k);
            pieces.addPiece(new CityRoadPiece(new BoundingBox(x, plate, cellMinZ, x + CityLayout.ROAD_WIDTH - 1, plate, cellMaxZ), variant));
        }
        for (int k : CityLayout.roadsInCell(cellZ)) {
            int z = oz + CityLayout.roadStart(k);
            pieces.addPiece(new CityRoadPiece(new BoundingBox(cellMinX, plate, z, cellMaxX, plate, z + CityLayout.ROAD_WIDTH - 1), variant));
        }

        for (int bx : CityLayout.blocksInCell(cellX)) {
            for (int bz : CityLayout.blocksInCell(cellZ)) {
                int blockX = ox + CityLayout.blockStart(bx);
                int blockZ = oz + CityLayout.blockStart(bz);
                if (bx == CityLayout.CENTER_BLOCK && bz == CityLayout.CENTER_BLOCK) {
                    townCenter(blockX, blockZ);
                    continue;
                }
                for (int qx = 0; qx < 2; qx++) {
                    for (int qz = 0; qz < 2; qz++) {
                        int px = blockX + qx * (CityLayout.PLOT_SIZE + 1);
                        int pz = blockZ + qz * (CityLayout.PLOT_SIZE + 1);
                        house(new BoundingBox(px, minY, pz, px + CityLayout.PLOT_SIZE - 1, maxY, pz + CityLayout.PLOT_SIZE - 1),
                                facing(bx, bz, qx, qz));
                    }
                }
            }
        }

        // Walls last, so they stand on top of the edge road.
        buildWalls(cellX, cellZ);
    }

    /**
     * Which side of a plot its entrance faces: one of the two roads it touches, but never the wall. The 4 plots in
     * the city's corners touch only the wall, so they face the alley between plots instead.
     */
    private Direction facing(int bx, int bz, int qx, int qz) {
        int last = CityLayout.BLOCKS_PER_SIDE - 1;
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
        for (CityLayout.WallSegment seg : CityLayout.wallSegments(cellX, cellZ)) {
            StructurePoolElement element = firstElement(seg.gate() ? variant.gate() : variant.wall());
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
                    element.getBoundingBox(templates, pos, rotation)));
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

    private void place(StructurePoolElement element, BlockPos pos, Rotation rotation, BoundingBox box, int depth) {
        pieces.addPiece(new PoolElementStructurePiece(templates, element, pos, element.getGroundLevelDelta(), rotation, box));
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
