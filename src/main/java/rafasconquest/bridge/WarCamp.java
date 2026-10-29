package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Point;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An army's war camp, built of blocks behind its side of the field: a command tent (13 by 13 inside, 5 tall, its
 * entrance toward the enemy, a bedrock block in the middle for the war council to stand around, the nation's banners
 * inside and out) with rows of small tents and campfires behind it, in the army's colour. Everything it replaces is
 * kept, and {@link #restore} puts it all back.
 */
final class WarCamp {
    /** Half the command tent's outer size: walls 7 out from the middle, 13 by 13 inside. */
    private static final int GENERAL_HALF = 7;
    /** Its walls stand 5 tall inside, under the roof. */
    private static final int GENERAL_HEIGHT = 5;
    /** A wing's Marshal's tent: 9 by 9 inside, 4 tall. */
    private static final int MARSHAL_HALF = 5;
    private static final int MARSHAL_HEIGHT = 4;
    /** A camp's site is looked for this far about where it should be, for dry ground this level at most. */
    private static final int SITE_SEARCH = 48;
    private static final int SITE_STEP = 4;
    private static final int SITE_ROUGHNESS = 3;
    /** A small tent is pitched only on dry ground this near the command tent's floor in height. */
    private static final int TENT_RISE = 6;
    private static final int TENTS_PER_ROW = 6;
    private static final int TENT_SPACING = 7;
    private static final int ROW_SPACING = 9;
    /** Ground below the floors is filled this deep at most where it falls away. */
    private static final int FILL_DEPTH = 6;

    private record Saved(BlockState state, @Nullable CompoundTag entity) {
    }

    private final ServerLevel level;
    private final Map<BlockPos, Saved> replaced = new LinkedHashMap<>();
    /** Just outside each small tent's way in, in the order they were pitched. */
    private final List<Vec3> tentDoors = new ArrayList<>();
    /** The way the camp faces: toward the enemy, and its right. */
    private final Direction front;
    private final Direction right;
    /** The command tent's floor, in the middle (where the bedrock stands). */
    private final BlockPos middle;
    /** The command tent's size: half its width outside, and its height inside; whether the war council meets in it. */
    private final int half;
    private final int height;
    private final boolean council;

    private WarCamp(ServerLevel level, BlockPos middle, Direction front, boolean council) {
        this.level = level;
        this.council = council;
        this.half = council ? GENERAL_HALF : MARSHAL_HALF;
        this.height = council ? GENERAL_HEIGHT : MARSHAL_HEIGHT;
        this.middle = middle;
        this.front = front;
        this.right = front.getClockWise();
    }

    /**
     * Builds a camp near {@code at} (on the nearest dry, fairly level ground: never on water or hanging over a drop),
     * facing ({@code facingX}, {@code facingZ}) toward the enemy (squared to the nearest compass direction), with
     * {@code tents} small tents behind it. The general's camp has the command tent where the war council meets; a
     * wing's has its Marshal's smaller tent.
     */
    static WarCamp build(ServerLevel level, Point at, double facingX, double facingZ, int tents, DyeColor colour, ItemStack banner,
                         boolean council) {
        Direction front = Direction.getNearest(facingX, 0, facingZ);
        BlockPos site = site(level, at, council ? GENERAL_HALF : MARSHAL_HALF);
        WarCamp camp = new WarCamp(level, site, front, council);
        camp.commandTent(colour, banner);
        camp.tents(tents, colour);
        return camp;
    }

    /** A point {@code r} to the right and {@code f} toward the enemy of the tent's middle, {@code up} above its floor. */
    private BlockPos local(int r, int f, int up) {
        return middle.relative(right, r).relative(front, f).above(up);
    }

    /** The same point as a place to stand, in the middle of the block. */
    Vec3 spot(int r, int f) {
        return Vec3.atBottomCenterOf(local(r, f, 0));
    }

    /** The yaw of someone facing toward the enemy ({@code dir} 0), to the right (1), back (2) or left (3). */
    float yaw(int dir) {
        Direction d = front;
        for (int i = 0; i < dir; i++) {
            d = d.getClockWise();
        }
        return d.toYRot();
    }

    /** Where the war council stands, around the bedrock: the general behind it, the wings' Marshals either side. */
    Vec3 generalSpot() {
        return spot(0, -2);
    }

    Vec3 leftMarshalSpot() {
        return spot(-2, 0);
    }

    Vec3 rightMarshalSpot() {
        return spot(2, 0);
    }

    /** Guards at the entrance, half either side of the way in, facing out: ten at the general's tent, six at a Marshal's. */
    List<Vec3> guardSpots() {
        List<Vec3> spots = new ArrayList<>();
        for (int i = 0; i < (council ? 5 : 3); i++) {
            spots.add(groundSpot(-2, half + 2 + i));
            spots.add(groundSpot(2, half + 2 + i));
        }
        return spots;
    }

    /** How many small tents there are, and where tent {@code i}'s way in is (outside it). */
    int tents() {
        return tentDoors.size();
    }

    Vec3 tentDoor(int i) {
        return tentDoors.get(i);
    }

    /** The yaw of someone at a tent's way in, facing into it. */
    float intoTent() {
        return yaw(2);
    }

    /** Whether a point is inside the command tent. */
    boolean inCommandTent(Vec3 at) {
        BlockPos p = BlockPos.containing(at);
        int dx = p.getX() - middle.getX();
        int dz = p.getZ() - middle.getZ();
        int r = dx * right.getStepX() + dz * right.getStepZ();
        int f = dx * front.getStepX() + dz * front.getStepZ();
        return Math.abs(r) < half && Math.abs(f) < half && p.getY() >= middle.getY() - 1 && p.getY() <= middle.getY() + height;
    }

    private Vec3 groundSpot(int r, int f) {
        BlockPos p = local(r, f, 0);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ());
        return Vec3.atBottomCenterOf(new BlockPos(p.getX(), y, p.getZ()));
    }

    private void commandTent(DyeColor colour, ItemStack banner) {
        BlockState wool = wool(colour);
        for (int r = -half; r <= half; r++) {
            for (int f = -half; f <= half; f++) {
                foundation(local(r, f, -1));
                boolean wall = Math.abs(r) == half || Math.abs(f) == half;
                for (int up = 0; up < height; up++) {
                    set(local(r, f, up), wall ? wool : Blocks.AIR.defaultBlockState());
                }
                set(local(r, f, height), wool);
                // A ridge above the roof, so it reads as a tent.
                if (Math.abs(r) <= half - 2 && Math.abs(f) <= half - 2) {
                    set(local(r, f, height + 1), wool);
                }
                if (Math.abs(r) <= 1 && Math.abs(f) <= half - 2) {
                    set(local(r, f, height + 2), wool);
                }
            }
        }
        // The way in: three wide, three tall, toward the enemy.
        for (int r = -1; r <= 1; r++) {
            for (int up = 0; up < 3; up++) {
                set(local(r, half, up), Blocks.AIR.defaultBlockState());
            }
        }
        if (council) {
            set(local(0, 0, 0), Blocks.BEDROCK.defaultBlockState()); // the Battle Command Table, for now
        }
        for (int r : new int[]{-half + 1, half - 1}) {
            for (int f : new int[]{-half + 1, half - 1}) {
                set(local(r, f, 0), Blocks.LANTERN.defaultBlockState());
            }
        }
        // Banners: on the back wall and both side walls inside, and either side of the way in outside.
        for (int r : new int[]{-3, 0, 3}) {
            wallBanner(local(r, -half + 1, 3), front, banner);
        }
        wallBanner(local(-half + 1, 0, 3), right, banner);
        wallBanner(local(half - 1, 0, 3), right.getOpposite(), banner);
        for (int r : new int[]{-4, 4}) {
            BlockPos p = local(r, half + 1, 0);
            p = new BlockPos(p.getX(), level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ()), p.getZ());
            standingBanner(p, front, banner);
        }
    }

    private void tents(int count, DyeColor colour) {
        BlockState wool = wool(colour);
        for (int i = 0; i < count; i++) {
            int row = i / TENTS_PER_ROW;
            int col = i % TENTS_PER_ROW;
            int r = (col - (TENTS_PER_ROW - 1) / 2) * TENT_SPACING;
            int f = -half - 6 - row * ROW_SPACING;
            BlockPos centre = local(r, f, 0);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, centre.getX(), centre.getZ());
            BlockPos base = new BlockPos(centre.getX(), y, centre.getZ());
            if (Math.abs(y - middle.getY()) > TENT_RISE || wet(base)) {
                continue; // no tents in the water, nor up a cliff or down a drop
            }
            // A ridge tent along the line of battle's depth: two sloping sides and the ridge, open at both ends.
            for (int along = -2; along <= 1; along++) {
                BlockPos p = base.relative(front, along);
                set(p.relative(right, -1), wool);
                set(p.relative(right, 1), wool);
                set(p, Blocks.AIR.defaultBlockState());
                set(p.above(), wool);
                set(p.relative(right, -1).above(), Blocks.AIR.defaultBlockState());
                set(p.relative(right, 1).above(), Blocks.AIR.defaultBlockState());
            }
            // A bed inside, its head to the back, and the way in toward the enemy.
            Block bed = BuiltInRegistries.BLOCK.get(new ResourceLocation(colour.getName() + "_bed"));
            Direction head = front.getOpposite();
            set(base, bed.defaultBlockState().setValue(BedBlock.FACING, head).setValue(BedBlock.PART, BedPart.FOOT));
            set(base.relative(head), bed.defaultBlockState().setValue(BedBlock.FACING, head).setValue(BedBlock.PART, BedPart.HEAD));
            BlockPos door = base.relative(front, 2);
            int dy = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, door.getX(), door.getZ());
            tentDoors.add(Vec3.atBottomCenterOf(new BlockPos(door.getX(), dy, door.getZ())));
            if (col % 2 == 1) {
                BlockPos fire = base.relative(right, TENT_SPACING / 2 + 1).relative(front, 3);
                int fy = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fire.getX(), fire.getZ());
                set(new BlockPos(fire.getX(), fy, fire.getZ()), Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, true));
            }
        }
    }

    /**
     * Where to put a camp wanted at {@code at}: the nearest point within {@link #SITE_SEARCH} whose command tent would
     * stand on dry ground no rougher than {@link #SITE_ROUGHNESS}; failing that the least rough dry one; failing that,
     * {@code at}. Its floor is the ground there.
     */
    private static BlockPos site(ServerLevel level, Point at, int half) {
        BlockPos best = null;
        double bestScore = Double.MAX_VALUE;
        for (int dx = -SITE_SEARCH; dx <= SITE_SEARCH; dx += SITE_STEP) {
            for (int dz = -SITE_SEARCH; dz <= SITE_SEARCH; dz += SITE_STEP) {
                int x = (int) Math.floor(at.x()) + dx;
                int z = (int) Math.floor(at.z()) + dz;
                int low = Integer.MAX_VALUE;
                int high = Integer.MIN_VALUE;
                boolean wet = false;
                for (int sx = -half; sx <= half; sx += half) {
                    for (int sz = -half; sz <= half; sz += half) {
                        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x + sx, z + sz);
                        low = Math.min(low, y);
                        high = Math.max(high, y);
                        wet |= !level.getFluidState(new BlockPos(x + sx, y - 1, z + sz)).isEmpty();
                    }
                }
                if (wet) {
                    continue;
                }
                int rough = high - low;
                double score = Math.hypot(dx, dz) + (rough <= SITE_ROUGHNESS ? 0 : 1000 + rough * 10);
                if (score < bestScore) {
                    bestScore = score;
                    best = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
                }
            }
        }
        if (best == null) {
            int x = (int) Math.floor(at.x());
            int z = (int) Math.floor(at.z());
            best = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
        }
        return best;
    }

    /** Whether the ground at a point is under water (or lava). */
    private boolean wet(BlockPos ground) {
        return !level.getFluidState(ground.below()).isEmpty() || !level.getFluidState(ground).isEmpty();
    }

    /** Solid ground under a floor: planks, with dirt below where the ground falls away. */
    private void foundation(BlockPos floor) {
        set(floor, Blocks.SPRUCE_PLANKS.defaultBlockState());
        for (int d = 1; d <= FILL_DEPTH; d++) {
            BlockPos below = floor.below(d);
            if (!level.getBlockState(below).isAir() && level.getFluidState(below).isEmpty()) {
                break;
            }
            set(below, Blocks.DIRT.defaultBlockState());
        }
    }

    private void wallBanner(BlockPos pos, Direction facing, ItemStack banner) {
        DyeColor base = banner.getItem() instanceof BannerItem b ? b.getColor() : DyeColor.WHITE;
        Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(base.getName() + "_wall_banner"));
        set(pos, block.defaultBlockState().setValue(WallBannerBlock.FACING, facing));
        pattern(pos, banner, base);
    }

    private void standingBanner(BlockPos pos, Direction facing, ItemStack banner) {
        DyeColor base = banner.getItem() instanceof BannerItem b ? b.getColor() : DyeColor.WHITE;
        Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(base.getName() + "_banner"));
        // Rotation 0 faces south; each step is a sixteenth of a turn clockwise.
        int rotation = Math.floorMod(Math.round(facing.toYRot() / 22.5F), 16);
        set(pos, block.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rotation));
        pattern(pos, banner, base);
    }

    private void pattern(BlockPos pos, ItemStack banner, DyeColor base) {
        if (level.getBlockEntity(pos) instanceof BannerBlockEntity entity) {
            entity.fromItem(banner, base);
            entity.setChanged();
            level.sendBlockUpdated(pos, entity.getBlockState(), entity.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static BlockState wool(DyeColor colour) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation(colour.getName() + "_wool")).defaultBlockState();
    }

    /** Sets a block, keeping what was there first (and its block entity) to put back. */
    private void set(BlockPos pos, BlockState state) {
        BlockPos at = pos.immutable();
        if (!replaced.containsKey(at)) {
            BlockEntity entity = level.getBlockEntity(at);
            replaced.put(at, new Saved(level.getBlockState(at), entity == null ? null : entity.saveWithFullMetadata()));
        }
        level.setBlock(at, state, Block.UPDATE_CLIENTS);
    }

    /** Puts back everything the camp replaced, the last first. */
    void restore() {
        List<Map.Entry<BlockPos, Saved>> entries = new ArrayList<>(replaced.entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            BlockPos pos = entries.get(i).getKey();
            Saved saved = entries.get(i).getValue();
            level.setBlock(pos, saved.state(), Block.UPDATE_CLIENTS);
            if (saved.entity() != null) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity != null) {
                    entity.load(saved.entity());
                    entity.setChanged();
                }
            }
        }
        replaced.clear();
    }
}
