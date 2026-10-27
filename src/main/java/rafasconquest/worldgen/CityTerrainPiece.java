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
 * the box to the plate, eases the surroundings with slopes, and caps sheer valley edges with wall brick. It is the
 * first piece of every cell, so the city is built on the finished ground. Each cell only touches the columns it owns,
 * so neighbouring cells never work on the same column.
 * <p>
 * It runs during world generation, on the chunk being generated, so nothing here causes block updates, physics or
 * lighting work.
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
        String encoded = tag.getString("edges");
        CityTerrain.Edge[][] edges = new CityTerrain.Edge[CityTerrain.Side.values().length][CityTerrain.SEGMENTS_PER_SIDE];
        for (int i = 0; i < encoded.length(); i++) {
            edges[i / CityTerrain.SEGMENTS_PER_SIDE][i % CityTerrain.SEGMENTS_PER_SIDE] = CityTerrain.Edge.values()[encoded.charAt(i) - '0'];
        }
        this.plan = new CityTerrain.Plan(tag.getInt("plateY"), edges);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("originX", originX);
        tag.putInt("originZ", originZ);
        tag.putString("variant", variant.name());
        tag.putInt("plateY", plan.plateY());
        StringBuilder edges = new StringBuilder();
        for (CityTerrain.Edge[] side : plan.edges()) {
            for (CityTerrain.Edge e : side) {
                edges.append((char) ('0' + e.ordinal()));
            }
        }
        tag.putString("edges", edges.toString());
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
                    case RIM -> flatten(level, pos, x, z, true);
                    case SLOPE -> slope(level, pos, x, z, column.low(), column.high());
                    case NONE -> {
                    }
                }
            }
        }
    }

    /** Clears everything above the plate and fills everything below it, so the column ends exactly at the plate. */
    private void flatten(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, boolean rim) {
        int plate = plan.plateY();
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        for (int y = top; y > plate; y--) {
            level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
        }
        level.setBlock(pos.set(x, plate, z), rim ? variant.wallBrick() : variant.ground(), 2);
        for (int y = plate - 1; y > level.getMinBuildHeight(); y--) {
            BlockState existing = level.getBlockState(pos.set(x, y, z));
            boolean hole = existing.isAir() || !existing.getFluidState().isEmpty() || existing.canBeReplaced();
            boolean underSurface = y >= plate - SUBSOIL_DEPTH;
            if (rim && y >= plate - CityTerrain.RIM_DEPTH + 1) {
                level.setBlock(pos, variant.wallBrick(), 2);
            } else if (hole) {
                level.setBlock(pos, underSurface ? variant.subsoil() : Blocks.STONE.defaultBlockState(), 2);
            } else if (underSurface && existing.is(Blocks.STONE)) {
                level.setBlock(pos, variant.subsoil(), 2); // cut through rock: give the plate a proper soil layer
            } else if (!underSurface) {
                return; // reached solid natural ground
            }
        }
    }

    /**
     * Eases a column outside the box toward the slope: fills it up to {@code low} with stone if the land is lower,
     * or cuts it down to {@code high} if higher. The new top takes the column's original surface block.
     */
    private void slope(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int z, int low, int high) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockState original = level.getBlockState(pos.set(x, top, z));
        int solid = top;
        while (solid > level.getMinBuildHeight() && !isSolidGround(level.getBlockState(pos.set(x, solid, z)))) {
            solid--;
        }
        BlockState surface = surfaceFrom(level.getBlockState(pos.set(x, solid, z)));
        if (solid < low) {
            for (int y = solid + 1; y < low; y++) {
                level.setBlock(pos.set(x, y, z), Blocks.STONE.defaultBlockState(), 2);
            }
            level.setBlock(pos.set(x, low, z), surface, 2);
        } else if (top > high && original.getFluidState().isEmpty()) {
            for (int y = top; y > high; y--) {
                level.setBlock(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            }
            level.setBlock(pos.set(x, high, z), surface, 2);
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
