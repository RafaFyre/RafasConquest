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

    public CityTerrainPiece(CityLayout.Site site, int cellX, int cellZ, CityVariant variant, CityTerrain.Plan plan, int minY, int maxY) {
        super(ConquestWorldgen.CITY_TERRAIN_PIECE.get(), 0, new BoundingBox(
                site.originX() + CityTerrain.ownedMin(cellX), minY, site.originZ() + CityTerrain.ownedMin(cellZ),
                site.originX() + CityTerrain.ownedMax(cellX), maxY, site.originZ() + CityTerrain.ownedMax(cellZ)));
        this.originX = site.originX();
        this.originZ = site.originZ();
        this.variant = variant;
        this.plan = plan;
        setOrientation(null);
    }

    public CityTerrainPiece(CompoundTag tag) {
        super(ConquestWorldgen.CITY_TERRAIN_PIECE.get(), tag);
        this.originX = tag.getInt("originX");
        this.originZ = tag.getInt("originZ");
        this.variant = CityVariant.valueOf(tag.getString("variant"));
        int sides = CityTerrain.Side.values().length;
        int segments = CityTerrain.SEGMENTS_PER_SIDE;
        String encoded = tag.getString("edges");
        int[] carve = tag.getIntArray("carveTo");
        CityTerrain.Edge[][] edges = new CityTerrain.Edge[sides][segments];
        int[][] carveTo = new int[sides][segments];
        for (int i = 0; i < sides * segments; i++) {
            edges[i / segments][i % segments] = i < encoded.length()
                    ? CityTerrain.Edge.values()[encoded.charAt(i) - '0'] : CityTerrain.Edge.CARVE;
            carveTo[i / segments][i % segments] = i < carve.length ? carve[i] : 0;
        }
        this.plan = new CityTerrain.Plan(tag.getInt("plateY"), edges, carveTo);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("originX", originX);
        tag.putInt("originZ", originZ);
        tag.putString("variant", variant.name());
        tag.putInt("plateY", plan.plateY());
        StringBuilder edges = new StringBuilder();
        int[] carve = new int[CityTerrain.Side.values().length * CityTerrain.SEGMENTS_PER_SIDE];
        int i = 0;
        for (int side = 0; side < plan.edges().length; side++) {
            for (int seg = 0; seg < plan.edges()[side].length; seg++) {
                edges.append((char) ('0' + plan.edges()[side][seg].ordinal()));
                carve[i++] = plan.carveTo()[side][seg];
            }
        }
        tag.putString("edges", edges.toString());
        tag.putIntArray("carveTo", carve);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = Math.max(boundingBox.minX(), chunkBox.minX()); x <= Math.min(boundingBox.maxX(), chunkBox.maxX()); x++) {
            for (int z = Math.max(boundingBox.minZ(), chunkBox.minZ()); z <= Math.min(boundingBox.maxZ(), chunkBox.maxZ()); z++) {
                CityTerrain.Column column = CityTerrain.column(plan, x - originX, z - originZ);
                switch (column.kind()) {
                    case PLATE -> flatten(level, pos, x, z, false);
                    case WALL_BASE -> flatten(level, pos, x, z, true);
                    case CLEAR_ABOVE -> clearAbove(level, pos, x, z, plan.plateY());
                    case SLOPE -> slope(level, pos, x, z, column);
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
     */
    private void flatten(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, boolean bare) {
        int plate = plan.plateY();
        clearAbove(level, pos, x, z, plate);
        BlockState stone = Blocks.STONE.defaultBlockState();
        level.setBlock(pos.set(x, plate, z), bare ? stone : variant.ground(), 2);
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
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        for (int y = top; y > floor; y--) {
            level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
        }
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
        BlockState surface = surfaceFrom(level.getBlockState(pos.set(x, solid, z)));
        if (column.fill() && solid < column.low()) {
            for (int y = solid + 1; y < column.low(); y++) {
                level.setBlock(pos.set(x, y, z), Blocks.STONE.defaultBlockState(), 2);
            }
            level.setBlock(pos.set(x, column.low(), z), surface, 2);
        } else if (top > column.high() && original.getFluidState().isEmpty()) {
            for (int y = top; y > column.high(); y--) {
                level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            }
            level.setBlock(pos.set(x, column.high(), z), surface, 2);
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
