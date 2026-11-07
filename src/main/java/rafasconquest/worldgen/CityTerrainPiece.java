package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;

/**
 * Reshapes the land under one cell of a city, following its {@link CityTerrain.Plan}: flattens the cell's share of
 * the box to the plate, carves rising land back and fills falling land in, and leaves valley edges as natural cliffs
 * under the wall. It is the first piece of every cell, so the city is built on the finished ground. Each cell only
 * touches the columns it owns, so neighbouring cells never work on the same column.
 * <p>
 * It runs during world generation, on the chunk being generated, so nothing here causes block updates, physics or
 * lighting work. Its bounding box starts a little below the plate: it also bounds where the city keeps hostile mobs
 * from spawning, and caves deep under the city should keep their mobs.
 */
public class CityTerrainPiece extends StructurePiece {
    private static final int SUBSOIL_DEPTH = 3;

    private final int originX;
    private final int originZ;
    private final CityVariant variant;
    private final CityTerrain.Plan plan;
    /** The city's outside noble district's platform, its square's world corner, or null; and whether this piece leaves the city's box alone. */
    @org.jetbrains.annotations.Nullable
    private final CityTerrain.Plan platform;
    private final int platformX;
    private final int platformZ;
    private final boolean annexOnly;
    /** A port's {@link rafasconquest.core.HarbourCut} box, city-local, or null: its own pieces shape that ground, not this one. */
    @org.jetbrains.annotations.Nullable
    private final int[] harbour;

    public CityTerrainPiece(CityLayout.Site site, int cellX, int cellZ, CityVariant variant, CityTerrain.Plan plan, int minY, int maxY,
                            CityGridBuilder.@org.jetbrains.annotations.Nullable Platform platform) {
        super(ConquestWorldgen.CITY_TERRAIN_PIECE.get(), 0, new BoundingBox(
                site.originX() + CityTerrain.ownedMin(site.cells(), cellX, plan.outset()), minY, site.originZ() + CityTerrain.ownedMin(site.cells(), cellZ, plan.outset()),
                site.originX() + CityTerrain.ownedMax(site.cells(), cellX, plan.outset()), maxY,
                site.originZ() + CityTerrain.ownedMax(site.cells(), cellZ, plan.outset())));
        this.originX = site.originX();
        this.originZ = site.originZ();
        this.variant = variant;
        this.plan = plan;
        this.platform = platform == null ? null : platform.plan();
        this.platformX = platform == null ? 0 : platform.x();
        this.platformZ = platform == null ? 0 : platform.z();
        this.annexOnly = false;
        this.harbour = CityLayout.harbourCut(site);
        setOrientation(null);
    }

    /** A stretch of the city's outer slope, or ({@code annexOnly}) of its outside noble district's ground, {@code box} in world coordinates. */
    public CityTerrainPiece(int originX, int originZ, CityVariant variant, CityTerrain.Plan plan, BoundingBox box,
                            CityGridBuilder.@org.jetbrains.annotations.Nullable Platform platform, boolean annexOnly,
                            @org.jetbrains.annotations.Nullable int[] harbour) {
        super(ConquestWorldgen.CITY_TERRAIN_PIECE.get(), 0, box);
        this.originX = originX;
        this.originZ = originZ;
        this.variant = variant;
        this.plan = plan;
        this.platform = platform == null ? null : platform.plan();
        this.platformX = platform == null ? 0 : platform.x();
        this.platformZ = platform == null ? 0 : platform.z();
        this.annexOnly = annexOnly && platform != null;
        this.harbour = harbour;
        setOrientation(null);
    }

    public CityTerrainPiece(CompoundTag tag) {
        super(ConquestWorldgen.CITY_TERRAIN_PIECE.get(), tag);
        this.originX = tag.getInt("originX");
        this.originZ = tag.getInt("originZ");
        this.variant = CityVariant.valueOf(tag.getString("variant"));
        this.plan = read(tag, "");
        this.platform = tag.contains("p.plateY") ? read(tag, "p.") : null;
        this.platformX = tag.getInt("platformX");
        this.platformZ = tag.getInt("platformZ");
        this.annexOnly = platform != null && tag.getBoolean("annexOnly");
        this.harbour = tag.contains("harbour") ? tag.getIntArray("harbour") : null;
    }

    /** A plan saved under {@code prefix}; a city's, with its old defaults, where a size or run is missing. */
    private static CityTerrain.Plan read(CompoundTag tag, String prefix) {
        int sides = CityTerrain.Side.values().length;
        int cells = tag.contains(prefix + "cells") ? tag.getInt(prefix + "cells") : CityLayout.CELLS_PER_SIDE;
        int outset = tag.contains(prefix + "outset") ? tag.getInt(prefix + "outset") : CityLayout.WALL_OUTSET;
        int size = tag.contains(prefix + "size") ? tag.getInt(prefix + "size") : CityTerrain.size(cells);
        int run = tag.contains(prefix + "run") ? tag.getInt(prefix + "run") : 2;
        int segments = new CityTerrain.Plan(0, null, null, cells, outset, size, run).segments();
        String encoded = tag.getString(prefix + "edges");
        int[] carve = tag.getIntArray(prefix + "carveTo");
        CityTerrain.Edge[][] edges = new CityTerrain.Edge[sides][segments];
        int[][] carveTo = new int[sides][segments];
        for (int i = 0; i < sides * segments; i++) {
            edges[i / segments][i % segments] = i < encoded.length()
                    ? CityTerrain.Edge.values()[encoded.charAt(i) - '0'] : CityTerrain.Edge.CARVE;
            carveTo[i / segments][i % segments] = i < carve.length ? carve[i] : 0;
        }
        return new CityTerrain.Plan(tag.getInt(prefix + "plateY"), edges, carveTo, cells, outset, size, run);
    }

    private static void write(CompoundTag tag, String prefix, CityTerrain.Plan plan) {
        tag.putInt(prefix + "plateY", plan.plateY());
        tag.putInt(prefix + "cells", plan.cells());
        tag.putInt(prefix + "outset", plan.outset());
        tag.putInt(prefix + "size", plan.size());
        tag.putInt(prefix + "run", plan.run());
        StringBuilder edges = new StringBuilder();
        int[] carve = new int[CityTerrain.Side.values().length * plan.segments()];
        int i = 0;
        for (int side = 0; side < plan.edges().length; side++) {
            for (int seg = 0; seg < plan.edges()[side].length; seg++) {
                edges.append((char) ('0' + plan.edges()[side][seg].ordinal()));
                carve[i++] = plan.carveTo()[side][seg];
            }
        }
        tag.putString(prefix + "edges", edges.toString());
        tag.putIntArray(prefix + "carveTo", carve);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("originX", originX);
        tag.putInt("originZ", originZ);
        tag.putString("variant", variant.name());
        write(tag, "", plan);
        if (platform != null) {
            write(tag, "p.", platform);
            tag.putInt("platformX", platformX);
            tag.putInt("platformZ", platformZ);
            tag.putBoolean("annexOnly", annexOnly);
        }
        if (harbour != null) {
            tag.putIntArray("harbour", harbour);
        }
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        // A styled piece may reach into this cell from the next one, which may be built first: its columns are kept.
        rafasconquest.style.CityPlan.Plan styled = rafasconquest.style.CityPlan.of(originX, originZ, plan.cells());
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                if (rafasconquest.core.HarbourCut.inBox(harbour, x - originX, z - originZ)) {
                    continue;
                }
                CityTerrain.Column column;
                if (platform == null) {
                    column = CityTerrain.column(plan, x - originX, z - originZ);
                } else {
                    // The city's own ground (its box) is its cells' to shape, not the outside district's tiles.
                    if (annexOnly && !platform.inBox(x - platformX, z - platformZ)) {
                        CityTerrain.Kind own = CityTerrain.column(plan, x - originX, z - originZ).kind();
                        if (own != CityTerrain.Kind.SLOPE && own != CityTerrain.Kind.NONE) {
                            continue;
                        }
                    }
                    column = CityTerrain.column(plan, x - originX, z - originZ, platform, x - platformX, z - platformZ);
                }
                boolean wall = CityLayout.inWallBand(plan.size(), x - originX, z - originZ);
                int keep = styled.pieces().isEmpty() && styled.path().isEmpty() ? -1 : styled.top(x - originX, z - originZ);
                switch (column.kind()) {
                    case PLATE -> flatten(level, pos, x, z, column.low(), false, wall, keep);
                    case WALL_BASE -> flatten(level, pos, x, z, column.low(), true, wall, keep);
                    case CLEAR_ABOVE -> clearAbove(level, pos, x, z, keep > 0 ? column.low() + keep - 1 : column.low());
                    case SLOPE -> {
                        if (keep < 0) {
                            slope(level, pos, x, z, column); // a wall piece standing out over the slope keeps its ground
                        }
                    }
                    case NONE -> {
                    }
                }
            }
        }
    }

    /**
     * Clears everything above the plate and fills everything below it, so the column ends exactly at the plate.
     * Under the wall on a valley edge ({@code bare}), the fill is plain stone so the cliff face below the wall looks
     * natural.
     * <p>
     * On the wall's footprint ({@code wall}), blocks of the wall itself are kept. Some segments reach a few blocks
     * into the neighbouring cell, whose terrain may be laid after the wall, and must not erase it. Under a styled piece
     * {@code keep} blocks tall, only what is above it is cleared, and its floor is only filled where it is open.
     */
    private void flatten(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, int plate, boolean bare, boolean wall, int keep) {
        BlockState stone = Blocks.STONE.defaultBlockState();
        clearAbove(level, pos, x, z, keep > 0 ? plate + keep - 1 : plate, wall);
        pos.set(x, plate, z);
        BlockState floor = level.getBlockState(pos);
        if (!(wall && isWall(floor)) && (keep < 0 || floor.isAir() || floor.canBeReplaced())) {
            level.setBlock(pos, bare ? stone : variant.ground(), 2);
        }
        for (int y = plate - 1; y > level.getMinBuildHeight(); y--) {
            BlockState existing = level.getBlockState(pos.set(x, y, z));
            boolean hole = existing.isAir() || !existing.getFluidState().isEmpty() || existing.canBeReplaced();
            boolean underSurface = !bare && y >= plate - SUBSOIL_DEPTH;
            if (hole) {
                level.setBlock(pos, underSurface ? variant.subsoil() : stone, 2);
            } else if (underSurface && existing.is(Blocks.STONE)) {
                level.setBlock(pos, variant.subsoil(), 2); // cut through rock: give the plate a proper soil layer
            } else if (!underSurface) {
                return; // reached solid natural ground
            }
        }
    }

    private static void clearAbove(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, int floor) {
        clearAbove(level, pos, x, z, floor, false);
    }

    /** Clears everything above {@code floor}; with {@code keepWall}, blocks of the city wall stay. */
    private static void clearAbove(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, int floor, boolean keepWall) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        for (int y = top; y > floor; y--) {
            if (!(keepWall && isWall(level.getBlockState(pos.set(x, y, z))))) {
                level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    /** Wall material, after its processor lists: stone brick variants or cut sandstone, and the wall's banners. */
    private static boolean isWall(BlockState state) {
        return state.is(BlockTags.STONE_BRICKS) || state.is(Blocks.CUT_SANDSTONE) || state.is(BlockTags.BANNERS);
    }

    /**
     * A column outside the box: carves land above the carve height down to it (never adding blocks), or, where the
     * land falls away and this stretch is filled, fills up to the fill height with stone. The new top takes the
     * column's original surface block.
     */
    private void slope(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, CityTerrain.Column column) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockState original = level.getBlockState(pos.set(x, top, z));
        int solid = top;
        while (solid > level.getMinBuildHeight() && !isSolidGround(level.getBlockState(pos.set(x, solid, z)))) {
            solid--;
        }
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState surface = column.stone() ? stone : surfaceFrom(level.getBlockState(pos.set(x, solid, z)));
        int laid;
        if (column.fill() && solid < column.low()) {
            for (int y = solid + 1; y < column.low(); y++) {
                level.setBlock(pos.set(x, y, z), stone, 2);
            }
            laid = column.low();
        } else if (top > column.high() && original.getFluidState().isEmpty()) {
            for (int y = top; y > column.high(); y--) {
                level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            }
            laid = column.high();
        } else if (column.stone() && solid == column.low()) {
            laid = solid;
        } else {
            return;
        }
        level.setBlock(pos.set(x, laid, z), surface, 2);
        if (column.stone()) {
            // The easing's steps stand 2 apart: their faces too, not the dirt a carve cuts into.
            level.setBlock(pos.set(x, laid - 1, z), stone, 2);
            level.setBlock(pos.set(x, laid - 2, z), stone, 2);
        }
    }

    private static boolean isSolidGround(BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty() && !state.canBeReplaced();
    }

    /** The biome's own top block where the slope meets it; plain ground if that was rock or water. */
    private BlockState surfaceFrom(BlockState natural) {
        if (natural.is(BlockTags.DIRT) || natural.is(BlockTags.SAND) || natural.is(BlockTags.TERRACOTTA)
                || natural.is(Blocks.SNOW_BLOCK) || natural.is(Blocks.GRAVEL)) {
            return natural.is(Blocks.DIRT) ? variant.ground() : natural;
        }
        return variant.ground();
    }
}
