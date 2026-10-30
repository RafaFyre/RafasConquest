package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * One building of the countryside, built in code on ground levelled to a height fixed when its structure started (so
 * every chunk it crosses agrees): a hamlet's shack and sheep pen, a ruin's lone empty shack, a village's well, a watchtower, or a castle (a
 * curtain wall with corner towers and a gate, about a keep). Coordinates are local: (0, 0, 0) is the middle of the
 * ground, x running {@link #facing}'s way after a rotation of {@code turns} quarter turns.
 */
public class CountryPiece extends StructurePiece {
    public enum Build {
        HAMLET(9, 6), RUIN(4, 6), WELL(3, 5), TOWER(4, 16), CASTLE(24, 14);

        /** How far it reaches from its middle, and how tall it stands. */
        final int half;
        final int height;

        Build(int half, int height) {
            this.half = half;
            this.height = height;
        }
    }

    /** Ground lower than the base is filled at most this deep; plants and trees are cleared this far above the top. */
    private static final int FILL_DEPTH = 12;
    private static final int CLEAR_ABOVE = 6;

    private final Build build;
    private final int turns;
    private final int baseX;
    private final int baseY;
    private final int baseZ;

    public CountryPiece(Build build, int x, int y, int z, int turns) {
        super(ConquestWorldgen.COUNTRY_PIECE.get(), 0, new BoundingBox(x - build.half, y - FILL_DEPTH, z - build.half,
                x + build.half, y + build.height + CLEAR_ABOVE, z + build.half));
        this.build = build;
        this.turns = turns & 3;
        this.baseX = x;
        this.baseY = y;
        this.baseZ = z;
        setOrientation(null);
    }

    public CountryPiece(CompoundTag tag) {
        super(ConquestWorldgen.COUNTRY_PIECE.get(), tag);
        this.build = Build.valueOf(tag.getString("build"));
        this.turns = tag.getInt("turns");
        this.baseX = tag.getInt("baseX");
        this.baseY = tag.getInt("baseY");
        this.baseZ = tag.getInt("baseZ");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("build", build.name());
        tag.putInt("turns", turns);
        tag.putInt("baseX", baseX);
        tag.putInt("baseY", baseY);
        tag.putInt("baseZ", baseZ);
    }

    /** The way local +x faces. */
    private Direction facing() {
        Direction d = Direction.EAST;
        for (int i = 0; i < turns; i++) {
            d = d.getClockWise();
        }
        return d;
    }

    private BlockPos at(int lx, int ly, int lz) {
        int x = lx;
        int z = lz;
        for (int i = 0; i < turns; i++) {
            int t = x;
            x = -z;
            z = t;
        }
        return new BlockPos(baseX + x, baseY + ly, baseZ + z);
    }

    private void set(WorldGenLevel level, BoundingBox chunk, int lx, int ly, int lz, BlockState state) {
        BlockPos pos = at(lx, ly, lz);
        if (chunk.isInside(pos)) {
            level.setBlock(pos, state, 2);
        }
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        level(level, chunkBox);
        switch (build) {
            case HAMLET -> hamlet(level, chunkBox, random);
            case RUIN -> shack(level, chunkBox, 6, false);
            case WELL -> well(level, chunkBox);
            case TOWER -> tower(level, chunkBox);
            case CASTLE -> castle(level, chunkBox);
        }
    }

    /** The ground under the whole piece made level at the base: filled with dirt below, cleared above, grass on top. */
    private void level(WorldGenLevel level, BoundingBox chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int half = build.half;
        for (int x = baseX - half; x <= baseX + half; x++) {
            for (int z = baseZ - half; z <= baseZ + half; z++) {
                if (x < chunk.minX() || x > chunk.maxX() || z < chunk.minZ() || z > chunk.maxZ()) {
                    continue;
                }
                for (int y = baseY + build.height + CLEAR_ABOVE; y >= baseY; y--) {
                    pos.set(x, y, z);
                    if (!level.getBlockState(pos).isAir()) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
                pos.set(x, baseY - 1, z);
                level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                for (int y = baseY - 2; y >= baseY - FILL_DEPTH; y--) {
                    pos.set(x, y, z);
                    BlockState here = level.getBlockState(pos);
                    if (!here.isAir() && here.getFluidState().isEmpty() && !here.canBeReplaced()) {
                        break;
                    }
                    level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
                }
            }
        }
    }

    /** A shack and a sheep pen beside it. */
    private void hamlet(WorldGenLevel level, BoundingBox chunk, RandomSource random) {
        shack(level, chunk, 0, true);
        Direction out = facing();
        // The pen: x -1 .. 8, z -4 .. 4, a gate facing the shack, hay inside.
        for (int x = -1; x <= 8; x++) {
            for (int z = -4; z <= 4; z++) {
                boolean edge = x == -1 || x == 8 || z == -4 || z == 4;
                if (!edge) {
                    continue;
                }
                if (x == -1 && z == 0) {
                    set(level, chunk, x, 0, z, Blocks.OAK_FENCE_GATE.defaultBlockState().setValue(FenceGateBlock.FACING, out));
                } else {
                    set(level, chunk, x, 0, z, Blocks.OAK_FENCE.defaultBlockState());
                }
            }
        }
        set(level, chunk, 6, 0, -2, Blocks.HAY_BLOCK.defaultBlockState());
        // Sheep, if the pen's middle is in this chunk.
        BlockPos middle = at(4, 0, 0);
        if (chunk.isInside(middle)) {
            int count = 2 + random.nextInt(3);
            for (int i = 0; i < count; i++) {
                Sheep sheep = EntityType.SHEEP.create(level.getLevel());
                if (sheep == null) {
                    continue;
                }
                sheep.moveTo(middle.getX() + 0.5 + random.nextInt(3) - 1, middle.getY(), middle.getZ() + 0.5 + random.nextInt(5) - 2,
                        random.nextFloat() * 360, 0);
                sheep.setColor(random.nextInt(6) == 0 ? DyeColor.GRAY : DyeColor.WHITE);
                sheep.finalizeSpawn(level, level.getCurrentDifficultyAt(middle), MobSpawnType.STRUCTURE, null, null);
                sheep.setPersistenceRequired();
                level.addFreshEntityWithPassengers(sheep);
            }
        }
    }

    /**
     * A shack (5 × 5, plank walls on log corners, a slab roof, a door facing x), from x -8 + {@code shift} to -4 + shift
     * and z -2 .. 2. Lit with a torch if anyone lives there; a ruin's stands dark and empty.
     */
    private void shack(WorldGenLevel level, BoundingBox chunk, int shift, boolean lived) {
        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        for (int x = -8 + shift; x <= -4 + shift; x++) {
            for (int z = -2; z <= 2; z++) {
                set(level, chunk, x, -1, z, Blocks.COBBLESTONE.defaultBlockState());
                boolean wallX = x == -8 + shift || x == -4 + shift;
                boolean wallZ = z == -2 || z == 2;
                for (int y = 0; y < 3; y++) {
                    if (wallX && wallZ) {
                        set(level, chunk, x, y, z, log);
                    } else if (wallX || wallZ) {
                        boolean window = y == 1 && (x == -6 + shift && wallZ || z == 0 && x == -8 + shift);
                        set(level, chunk, x, y, z, window ? Blocks.GLASS_PANE.defaultBlockState() : planks);
                    }
                }
                set(level, chunk, x, 3, z, Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
            }
        }
        Direction out = facing();
        set(level, chunk, -4 + shift, 0, 0, Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, out)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        set(level, chunk, -4 + shift, 1, 0, Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, out)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        set(level, chunk, -7 + shift, 0, -1, Blocks.BARREL.defaultBlockState());
        set(level, chunk, -5 + shift, 0, -1, Blocks.CRAFTING_TABLE.defaultBlockState());
        if (lived) {
            set(level, chunk, -6 + shift, 1, 1, Blocks.WALL_TORCH.defaultBlockState()
                    .setValue(net.minecraft.world.level.block.WallTorchBlock.FACING, rotate(Direction.NORTH)));
        }
    }

    /** A village's well: a cobblestone ring about water, posts and a slab roof. */
    private void well(WorldGenLevel level, BoundingBox chunk) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                set(level, chunk, x, -1, z, Blocks.COBBLESTONE.defaultBlockState());
                boolean ring = Math.abs(x) == 2 || Math.abs(z) == 2;
                if (ring) {
                    set(level, chunk, x, 0, z, Blocks.COBBLESTONE.defaultBlockState());
                } else {
                    set(level, chunk, x, -1, z, Blocks.WATER.defaultBlockState());
                    set(level, chunk, x, -2, z, Blocks.COBBLESTONE.defaultBlockState());
                }
                if (Math.abs(x) == 2 && Math.abs(z) == 2) {
                    for (int y = 1; y <= 3; y++) {
                        set(level, chunk, x, y, z, Blocks.OAK_FENCE.defaultBlockState());
                    }
                }
                set(level, chunk, x, 4, z, Blocks.COBBLESTONE_SLAB.defaultBlockState());
            }
        }
    }

    /** A watchtower: a stone shaft 5 × 5 with a ladder inside and a door, a crenellated lookout on top. */
    private void tower(WorldGenLevel level, BoundingBox chunk) {
        int top = 12;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                set(level, chunk, x, -1, z, Blocks.COBBLESTONE.defaultBlockState());
                boolean wall = Math.abs(x) == 2 || Math.abs(z) == 2;
                for (int y = 0; y < top; y++) {
                    if (wall) {
                        set(level, chunk, x, y, z, y < 3 ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState());
                    }
                }
            }
        }
        // The door, on the +x side, and a ladder up the far wall.
        set(level, chunk, 2, 0, 0, Blocks.AIR.defaultBlockState());
        set(level, chunk, 2, 1, 0, Blocks.AIR.defaultBlockState());
        for (int y = 0; y < top; y++) {
            set(level, chunk, -1, y, 0, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, rotate(Direction.EAST)));
        }
        // The lookout: a floor a block wider each way, with a hatch over the ladder, merlons on every other block.
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x == -1 && z == 0) {
                    set(level, chunk, x, top, z, Blocks.OAK_TRAPDOOR.defaultBlockState());
                } else {
                    set(level, chunk, x, top, z, Blocks.STONE_BRICKS.defaultBlockState());
                }
                boolean rim = Math.abs(x) == 3 || Math.abs(z) == 3;
                if (rim) {
                    set(level, chunk, x, top + 1, z, Blocks.STONE_BRICK_WALL.defaultBlockState());
                    if ((x + z) % 2 == 0) {
                        set(level, chunk, x, top + 2, z, Blocks.STONE_BRICK_WALL.defaultBlockState());
                    }
                }
            }
        }
        set(level, chunk, 0, top + 1, 0, Blocks.LANTERN.defaultBlockState());
    }

    /**
     * A castle: a curtain wall 41 blocks square (6 high, crenellated, a 3-wide gate on the +x side), a 7 × 7 tower at
     * each corner, and an 11 × 11 keep in the middle with a door, a floor halfway up and a ladder.
     */
    private void castle(WorldGenLevel level, BoundingBox chunk) {
        int r = 20;
        BlockState stone = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState mossy = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        // The courtyard: a gravel path from the gate to the keep, the rest grass.
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                boolean wall = Math.abs(x) == r || Math.abs(z) == r;
                if (Math.abs(z) <= 1 && x > 5 && x < r) {
                    set(level, chunk, x, -1, z, Blocks.GRAVEL.defaultBlockState());
                }
                if (!wall) {
                    continue;
                }
                set(level, chunk, x, -1, z, stone);
                for (int y = 0; y < 6; y++) {
                    set(level, chunk, x, y, z, (x * 7 + z * 3 + y) % 9 == 0 ? mossy : stone);
                }
                if ((x + z) % 2 == 0) {
                    set(level, chunk, x, 6, z, stone);
                }
            }
        }
        // The gate.
        for (int z = -1; z <= 1; z++) {
            for (int y = 0; y < 4; y++) {
                set(level, chunk, r, y, z, Blocks.AIR.defaultBlockState());
            }
        }
        // The corner towers.
        for (int cx : new int[]{-r, r}) {
            for (int cz : new int[]{-r, r}) {
                for (int x = cx - 3; x <= cx + 3; x++) {
                    for (int z = cz - 3; z <= cz + 3; z++) {
                        if (Math.abs(x) > r + 3 || Math.abs(z) > r + 3) {
                            continue;
                        }
                        boolean shell = Math.abs(x - cx) == 3 || Math.abs(z - cz) == 3;
                        for (int y = -1; y < 9; y++) {
                            if (shell || y == -1) {
                                set(level, chunk, x, y, z, stone);
                            }
                        }
                        set(level, chunk, x, 9, z, stone);
                        if (shell && (x + z) % 2 == 0) {
                            set(level, chunk, x, 10, z, stone);
                        }
                    }
                }
            }
        }
        // The keep.
        int k = 5;
        for (int x = -k; x <= k; x++) {
            for (int z = -k; z <= k; z++) {
                boolean shell = Math.abs(x) == k || Math.abs(z) == k;
                set(level, chunk, x, -1, z, stone);
                for (int y = 0; y < 12; y++) {
                    if (shell) {
                        boolean window = (y == 2 || y == 8) && (x == 0 || z == 0);
                        set(level, chunk, x, y, z, window ? Blocks.IRON_BARS.defaultBlockState() : stone);
                    }
                }
                if (!shell) {
                    set(level, chunk, x, 6, z, Blocks.SPRUCE_PLANKS.defaultBlockState());
                }
                set(level, chunk, x, 12, z, stone);
                if (shell && (x + z) % 2 == 0) {
                    set(level, chunk, x, 13, z, stone);
                }
            }
        }
        Direction out = facing();
        set(level, chunk, k, 0, 0, Blocks.SPRUCE_DOOR.defaultBlockState().setValue(DoorBlock.FACING, out)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        set(level, chunk, k, 1, 0, Blocks.SPRUCE_DOOR.defaultBlockState().setValue(DoorBlock.FACING, out)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        for (int y = 0; y < 12; y++) {
            set(level, chunk, -k + 1, y, 0, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, rotate(Direction.EAST)));
        }
        set(level, chunk, -k + 1, 6, 0, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, rotate(Direction.EAST)));
        set(level, chunk, 0, 7, 0, Blocks.LANTERN.defaultBlockState());
        set(level, chunk, 0, 0, 2, Blocks.LANTERN.defaultBlockState());
    }

    /** A local direction turned the way the piece is. */
    private Direction rotate(Direction local) {
        Direction d = local;
        for (int i = 0; i < turns; i++) {
            d = d.getClockWise();
        }
        return d;
    }
}
