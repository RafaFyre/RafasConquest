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
import rafasconquest.core.CityLayout;

import java.util.List;
import java.util.Optional;

/**
 * Builds one cell of a city's planned grid: its share of the roads, then a vanilla building on every plot (or the
 * town centre on the middle block). Buildings are turned so their entrance faces a road and placed against it;
 * anything they link to through their own jigsaws (villagers, cats, golems) is attached the way vanilla does it.
 */
public final class CityGridBuilder {
    /** How many distinct house picks to try before leaving a plot empty. Fitting is just bounding-box math, so be generous. */
    private static final int HOUSE_ATTEMPTS = 64;
    /** How deep to follow a building's own jigsaw links (building → villager is 1). */
    private static final int MAX_CHILD_DEPTH = 2;
    private static final String ENTRANCE = "minecraft:building_entrance";

    /** The terrain top at a column; {@code waterDepth} is 0 on land. */
    public record Ground(int y, int waterDepth) {
        public boolean water() {
            return waterDepth > 0;
        }

        /** Water the city can't reclaim; nothing is built there. */
        public boolean deepWater() {
            return waterDepth > CityFillPiece.MAX_DEPTH;
        }
    }

    private final Structure.GenerationContext ctx;
    private final CityLayout.Site site;
    private final CityVariant variant;
    private final StructurePiecesBuilder pieces;
    private final WorldgenRandom random;
    private final StructureTemplateManager templates;
    private final Registry<StructureTemplatePool> pools;

    private CityGridBuilder(Structure.GenerationContext ctx, CityLayout.Site site, CityVariant variant, StructurePiecesBuilder pieces) {
        this.ctx = ctx;
        this.site = site;
        this.variant = variant;
        this.pieces = pieces;
        this.random = ctx.random();
        this.templates = ctx.structureTemplateManager();
        this.pools = ctx.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
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

        // Roads first, so buildings placed afterwards win wherever they touch.
        for (int k : CityLayout.roadsInCell(cellX)) {
            int x = ox + CityLayout.roadStart(k);
            pieces.addPiece(new CityRoadPiece(new BoundingBox(x, minY, cellMinZ, x + CityLayout.ROAD_WIDTH - 1, maxY, cellMaxZ), variant));
        }
        for (int k : CityLayout.roadsInCell(cellZ)) {
            int z = oz + CityLayout.roadStart(k);
            pieces.addPiece(new CityRoadPiece(new BoundingBox(cellMinX, minY, z, cellMaxX, maxY, z + CityLayout.ROAD_WIDTH - 1), variant));
        }

        for (int bx : CityLayout.blocksInCell(cellX)) {
            for (int bz : CityLayout.blocksInCell(cellZ)) {
                int blockX = ox + CityLayout.blockStart(bx);
                int blockZ = oz + CityLayout.blockStart(bz);
                int blockSize = CityLayout.ROAD_PERIOD - CityLayout.ROAD_WIDTH;
                pieces.addPiece(new CityFillPiece(new BoundingBox(blockX, minY, blockZ, blockX + blockSize - 1, maxY, blockZ + blockSize - 1), variant));
                if (bx == CityLayout.CENTER_BLOCK && bz == CityLayout.CENTER_BLOCK) {
                    townCenter(blockX, blockZ);
                    continue;
                }
                for (int qx = 0; qx < 2; qx++) {
                    for (int qz = 0; qz < 2; qz++) {
                        int px = blockX + qx * (CityLayout.PLOT_SIZE + 1);
                        int pz = blockZ + qz * (CityLayout.PLOT_SIZE + 1);
                        Direction toRoad = random.nextBoolean()
                                ? (qx == 0 ? Direction.WEST : Direction.EAST)
                                : (qz == 0 ? Direction.NORTH : Direction.SOUTH);
                        house(new BoundingBox(px, minY, pz, px + CityLayout.PLOT_SIZE - 1, maxY, pz + CityLayout.PLOT_SIZE - 1), toRoad);
                    }
                }
            }
        }
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
        Ground ground = ground(ctx, ex, ez);
        if (ground.deepWater()) {
            return;
        }
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
                BlockPos pos = new BlockPos(ex - entrance.pos().getX(), ground.y() - entrance.pos().getY(), ez - entrance.pos().getZ());
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
        Ground ground = ground(ctx, cx, cz);
        for (StructurePoolElement element : pool.get().value().getShuffledTemplates(random)) {
            if (element == EmptyPoolElement.INSTANCE || element.toString().contains("/zombie/")) {
                continue;
            }
            Rotation rotation = Rotation.getRandom(random);
            BoundingBox at = element.getBoundingBox(templates, new BlockPos(cx, 0, cz), rotation);
            BlockPos pos = new BlockPos(cx + (cx - at.getCenter().getX()), ground.y() - at.minY(), cz + (cz - at.getCenter().getZ()));
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
