package rafasconquest.bridge;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock;
import rafasconquest.battle.Siege;
import rafasconquest.content.GateBlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * A siege's strategems and gates in the world, for the soldiers who are real: ladders stood against the wall and towers
 * made fast to it as blocks (to climb), carried ladders, rams and rolling towers as block displays, the climb itself
 * (Recruits soldiers can't path up a ladder, so a climber is lifted up it), the Gate Blocks' buttons pressed by soldiers
 * near them, and the real gates kept in step with the numbers ({@link Siege}). Only {@link RecruitsBattle} uses it.
 */
final class SiegeWorks {
    /** Every display a siege puts up carries this tag, to be swept up after. */
    static final String TAG = RafasConquest.MOD_ID + "_siege";
    /** Soldiers this close to a Gate Block's button press it. */
    private static final double PRESS_REACH = 2.5;
    /** Soldiers of a crew this close to their ladder's foot are queued to climb it; this many wait at once. */
    private static final double CLIMB_FROM = 10;
    private static final int QUEUE = 3;
    /** A climber goes up this far a tick. */
    private static final double CLIMB_STEP = 0.15;
    /** A tower: 7 wide, 9 long, 15 tall, its back rising 2 for each 1 forward; it hovers half a block up (no wheels yet). */
    static final int TOWER_TALL = 15;
    private static final int TOWER_HALF_WIDTH = 3;
    private static final int TOWER_LONG = 9;
    private static final double HOVER = 0.5;
    /** A tower's climbers start from this far out behind its middle, and step off this far in from the wall's face. */
    private static final double TOWER_CLIMB = Siege.TOWER_BEHIND - 1;

    private final ServerLevel level;
    private Siege siege;
    private final List<BlockPos> gateBlocks;
    /** The attacked wall's walkway (the height soldiers stand at on it), and the way out from it. */
    private final int walkY;
    private final int ox;
    private final int oz;
    /** The blocks put up for each ladder or tower, with what stood there before; and the foot of its ladder. */
    private final Map<Siege.Work, Map<BlockPos, BlockState>> placed = new HashMap<>();
    private final Map<Siege.Work, BlockPos> ladders = new HashMap<>();
    private final Map<Siege.Work, List<Display.BlockDisplay>> shown = new HashMap<>();
    /** With Sable: each carried ladder and rolling tower as a real frame ({@link SiegeFrames}), where it should be now. */
    private final Map<Siege.Work, SableLink.Frame> frames = new LinkedHashMap<>();
    private final Map<Siege.Work, String> frameKeys = new HashMap<>();
    private static int frameSerial;
    /** Soldiers waiting at a ladder's foot, or on it, by soldier: the ladder's foot. */
    private final Map<UUID, BlockPos> queued = new LinkedHashMap<>();
    private final Set<UUID> climbing = new HashSet<>();
    /** Attackers who have pressed a button (each presses once). */
    private final Set<UUID> pressed = new HashSet<>();

    SiegeWorks(ServerLevel level, Siege siege, List<BlockPos> gateBlocks, int walkY, double outX, double outZ) {
        this.level = level;
        this.siege = siege;
        this.gateBlocks = gateBlocks;
        this.walkY = walkY;
        this.ox = (int) Math.round(outX);
        this.oz = (int) Math.round(outZ);
    }

    /** A new day's siege (the armies regrouped): everything of the last is taken down first. */
    void rebind(Siege next) {
        clear();
        siege = next;
    }

    /** The chunks the gates stand in, kept loaded through the battle so the real gates are always there to fight for. */
    List<BlockPos> gates() {
        return gateBlocks;
    }

    // ---- each second ----

    /**
     * Each battle second: the gates (what numbers did to them, then their real state back to the numbers), the
     * strategems put up or taken down and shown on the move, buttons pressed, and climbers queued.
     */
    void second(Function<FieldBlock, List<AbstractRecruitEntity>> soldiers, List<AbstractRecruitEntity> attackers,
                List<AbstractRecruitEntity> defenders) {
        syncGates();
        for (Siege.Work w : siege.works()) {
            boolean up = w.kind() == Siege.Engine.LADDERS ? w.state() == Siege.State.UP
                    : w.kind() == Siege.Engine.TOWERS && w.state() != Siege.State.CARRIED;
            if (up && !placed.containsKey(w)) {
                put(w);
            } else if (!up && placed.containsKey(w)) {
                take(w);
            }
            FieldBlock carrier = w.carrier();
            List<AbstractRecruitEntity> carrying = carrier == null ? List.of() : soldiers.apply(carrier);
            boolean ramDone = w.kind() == Siege.Engine.RAMS && w.gate() >= 0 && siege.gates().get(w.gate()).broken();
            if (w.kind() == Siege.Engine.TOWERS) {
                // A tower stands where it rolled to all day; with Sable it stays a frame even against the wall.
                if (SableLink.available() || !placed.containsKey(w)) {
                    showTower(w);
                } else {
                    hide(w);
                }
            } else if (!carrying.isEmpty() && !placed.containsKey(w) && !ramDone) {
                show(w, carrying);
            } else {
                hide(w);
            }
            BlockPos foot = ladders.get(w);
            if (foot != null) {
                queueClimbers(w, foot, soldiers);
            }
        }
        pressButtons(attackers, defenders);
    }

    /** Each server tick: the frames glided on, and climbers go up their ladders and over onto the walkway at the top. */
    void tick() {
        SableLink.siegeFrames(level, List.copyOf(frames.values()));
        for (var it = queued.entrySet().iterator(); it.hasNext(); ) {
            var e = it.next();
            if (!(level.getEntity(e.getKey()) instanceof AbstractRecruitEntity s) || !s.isAlive()) {
                climbing.remove(e.getKey());
                it.remove();
                continue;
            }
            BlockPos foot = e.getValue();
            double cx = foot.getX() + 0.5;
            double cz = foot.getZ() + 0.5;
            if (!climbing.contains(s.getUUID())) {
                if (Math.hypot(s.getX() - cx, s.getZ() - cz) <= 1.3 && (tower(foot) || level.getBlockState(foot).getBlock() instanceof LadderBlock)) {
                    climbing.add(s.getUUID());
                    s.setNoGravity(true);
                    s.getNavigation().stop();
                }
                continue;
            }
            if (s.getY() + CLIMB_STEP < walkY) {
                // Up a tower's back the ladders step in one for every two up.
                double in = tower(foot) ? Math.min(TOWER_LONG - 2.5, (s.getY() - foot.getY()) / 2) : 0;
                s.getNavigation().stop();
                s.setDeltaMovement(Vec3.ZERO);
                s.moveTo(cx - ox * in, s.getY() + CLIMB_STEP, cz - oz * in, s.getYRot(), s.getXRot());
                continue;
            }
            // Over the top: onto the walkway, two blocks in past the outer row.
            Vec3 onto = landing(foot);
            s.setNoGravity(false);
            s.moveTo(onto.x, walkY, onto.z, s.getYRot(), s.getXRot());
            s.setHoldPos(new Vec3(onto.x, walkY, onto.z));
            climbing.remove(s.getUUID());
            it.remove();
        }
    }

    /** Where a climber up the ladder at {@code foot} steps onto the walkway: over its top, toward the inside. */
    private Vec3 landing(BlockPos foot) {
        // A tower's ladders are up its back: across it first, to land as far in as off a ladder.
        double in = 2 + (tower(foot) ? Siege.TOWER_OUT + TOWER_CLIMB : 0);
        return new Vec3(foot.getX() + 0.5 - ox * in, walkY, foot.getZ() + 0.5 - oz * in);
    }

    /** Whether {@code foot} is the foot of a tower's back, not of a ladder. */
    private boolean tower(BlockPos foot) {
        return ladders.entrySet().stream().anyMatch(e -> e.getValue().equals(foot) && e.getKey().kind() == Siege.Engine.TOWERS);
    }

    // ---- gates ----

    private void syncGates() {
        for (Siege.GateAction a : siege.takeActions()) {
            GateBlockEntity gate = gate(a.gate());
            if (gate == null) {
                continue;
            }
            switch (a.kind()) {
                case RAM -> gate.batter((int) Math.ceil(a.amount()));
                case OPEN -> {
                    if (gate.shut()) {
                        gate.pulse();
                    }
                }
                case CLOSE -> {
                    if (gate.swing() == GateBlockEntity.Swing.OPEN && !gate.broken()) {
                        gate.pulse();
                    }
                }
            }
        }
        for (int i = 0; i < gateBlocks.size(); i++) {
            GateBlockEntity gate = gate(i);
            if (gate != null) {
                boolean open = gate.swing() == GateBlockEntity.Swing.OPEN || gate.swing() == GateBlockEntity.Swing.OPENING;
                siege.setGate(i, open, gate.broken());
            }
        }
    }

    @Nullable
    private GateBlockEntity gate(int i) {
        BlockPos at = gateBlocks.get(i);
        return level.isLoaded(at) && level.getBlockEntity(at) instanceof GateBlockEntity gate ? gate : null;
    }

    /**
     * An attacker beside a shut gate's button presses it (each once); a defender beside an open one presses it to swing
     * it shut again.
     */
    private void pressButtons(List<AbstractRecruitEntity> attackers, List<AbstractRecruitEntity> defenders) {
        for (int i = 0; i < gateBlocks.size(); i++) {
            GateBlockEntity gate = gate(i);
            BlockPos button = gate == null ? null : GateBlockEntity.button(level, gateBlocks.get(i));
            if (button == null) {
                continue;
            }
            BlockState state = level.getBlockState(button);
            if (!(state.getBlock() instanceof ButtonBlock b) || state.getValue(ButtonBlock.POWERED)) {
                continue;
            }
            Vec3 at = Vec3.atCenterOf(button);
            if (gate.shut()) {
                for (AbstractRecruitEntity s : attackers) {
                    if (s.position().distanceTo(at) <= PRESS_REACH && pressed.add(s.getUUID())) {
                        b.press(state, level, button, null);
                        s.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                        break;
                    }
                }
            } else if (gate.swing() == GateBlockEntity.Swing.OPEN && !gate.broken()) {
                for (AbstractRecruitEntity s : defenders) {
                    if (s.position().distanceTo(at) <= PRESS_REACH) {
                        b.press(state, level, button, null);
                        s.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                        break;
                    }
                }
            }
        }
    }

    // ---- ladders and towers as blocks ----

    /** Stands a ladder against the wall, or makes a tower fast to it, if its chunks are loaded. */
    private void put(Siege.Work w) {
        Point foot = w.foot();
        Direction out = Direction.fromDelta(ox, 0, oz);
        BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, out);
        int ax = -oz; // along the wall
        int az = ox;
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
        if (w.kind() == Siege.Engine.TOWERS) {
            // Climbers start behind its back; with Sable the tower stays a frame, else it stands as blocks, front to the wall.
            BlockPos back = BlockPos.containing(foot.x() + ox * TOWER_CLIMB, 0, foot.z() + oz * TOWER_CLIMB);
            if (!level.isLoaded(back)) {
                return;
            }
            back = back.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, back.getX(), back.getZ()));
            if (!SableLink.available()) {
                for (Map.Entry<BlockPos, BlockState> e : towerBlocks(ladder).entrySet()) {
                    BlockPos p = e.getKey();
                    int z = p.getZ() + 2; // the back's ladders are at -1, the climbers' cell at -2
                    blocks.put(back.offset(ax * p.getX() - ox * z, p.getY(), az * p.getX() - oz * z), e.getValue());
                }
            }
            place(w, blocks, back);
            return;
        }
        BlockPos cell = BlockPos.containing(foot.x() - ox, 0, foot.z() - oz);
        // At a gap between the merlons, to step over onto the walkway.
        for (int shift : new int[]{0, 1, -1}) {
            BlockPos merlon = new BlockPos(cell.getX() - ox + ax * shift, walkY, cell.getZ() - oz + az * shift);
            if (level.isLoaded(merlon) && level.getBlockState(merlon).isAir()) {
                cell = cell.offset(ax * shift, 0, az * shift);
                break;
            }
        }
        if (!level.isLoaded(cell) || !level.isLoaded(cell.offset(ox * 4, 0, oz * 4))) {
            return;
        }
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, cell.getX(), cell.getZ());
        for (int y = ground; y < walkY; y++) {
            blocks.put(new BlockPos(cell.getX(), y, cell.getZ()), ladder);
        }
        place(w, blocks, new BlockPos(cell.getX(), ground, cell.getZ()));
    }

    /** Puts up a ladder's or tower's blocks where nothing solid stands, remembering what was there; and its ladder's foot. */
    private void place(Siege.Work w, Map<BlockPos, BlockState> blocks, BlockPos ladderFoot) {
        Map<BlockPos, BlockState> before = new LinkedHashMap<>();
        for (Map.Entry<BlockPos, BlockState> e : blocks.entrySet()) {
            BlockState was = level.getBlockState(e.getKey());
            if (was.canBeReplaced()) {
                before.put(e.getKey(), was); // never over anything solid: the ground, the wall
            }
        }
        // Solid parts first, ladders after (a ladder pops off without its wall behind it).
        before.keySet().stream().filter(p -> !(blocks.get(p).getBlock() instanceof LadderBlock))
                .forEach(p -> level.setBlock(p, blocks.get(p), Block.UPDATE_CLIENTS));
        before.keySet().stream().filter(p -> blocks.get(p).getBlock() instanceof LadderBlock)
                .forEach(p -> level.setBlock(p, blocks.get(p), Block.UPDATE_CLIENTS));
        placed.put(w, before);
        ladders.put(w, ladderFoot);
    }

    /**
     * A tower's blocks, front to +z and its back from z 0: a plank shell rising 2 for each 1 forward to its full
     * height, and {@code ladder} up every step of its back (from z -1).
     */
    static Map<BlockPos, BlockState> towerBlocks(BlockState ladder) {
        Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
        BlockState planks = Blocks.SPRUCE_PLANKS.defaultBlockState();
        for (int z = 0; z < TOWER_LONG; z++) {
            int high = Math.min(TOWER_TALL, 2 * (z + 1));
            int lower = Math.min(TOWER_TALL, 2 * z); // the step behind it
            for (int x = -TOWER_HALF_WIDTH; x <= TOWER_HALF_WIDTH; x++) {
                for (int y = 0; y < high; y++) {
                    if (Math.abs(x) == TOWER_HALF_WIDTH || z == TOWER_LONG - 1 || y == 0 || y >= lower) {
                        parts.put(new BlockPos(x, y, z), planks);
                    }
                    if (y >= lower) {
                        parts.put(new BlockPos(x, y, z - 1), ladder);
                    }
                }
            }
        }
        return parts;
    }

    /** Takes a ladder or tower down again, putting back what stood there; its climbers drop off. */
    private void take(Siege.Work w) {
        Map<BlockPos, BlockState> before = placed.remove(w);
        BlockPos foot = ladders.remove(w);
        if (foot != null) {
            queued.entrySet().removeIf(e -> {
                if (!e.getValue().equals(foot)) {
                    return false;
                }
                if (level.getEntity(e.getKey()) instanceof AbstractRecruitEntity s) {
                    s.setNoGravity(false);
                }
                climbing.remove(e.getKey());
                return true;
            });
        }
        if (before == null) {
            return;
        }
        // Ladders first, then the rest, so nothing pops off as a drop.
        before.entrySet().stream().filter(e -> level.getBlockState(e.getKey()).getBlock() instanceof LadderBlock)
                .forEach(e -> level.setBlock(e.getKey(), e.getValue(), Block.UPDATE_CLIENTS));
        before.forEach((p, state) -> level.setBlock(p, state, Block.UPDATE_CLIENTS));
    }

    /** Soldiers of the work's crews near its ladder's foot, still below the walkway, are queued to climb, a few at a time. */
    private void queueClimbers(Siege.Work w, BlockPos foot, Function<FieldBlock, List<AbstractRecruitEntity>> soldiers) {
        long waiting = queued.values().stream().filter(foot::equals).count();
        Vec3 base = Vec3.atBottomCenterOf(foot);
        for (FieldBlock crew : w.crews()) {
            for (AbstractRecruitEntity s : soldiers.apply(crew)) {
                if (queued.containsKey(s.getUUID())) {
                    if (!climbing.contains(s.getUUID())) {
                        s.setHoldPos(base); // the battle's orders move them on each second; they wait their turn here
                        s.setFollowState(3);
                    }
                    continue;
                }
                if (waiting < QUEUE && s.getY() < walkY - 1 && Math.hypot(s.getX() - base.x, s.getZ() - base.z) <= CLIMB_FROM) {
                    queued.put(s.getUUID(), foot);
                    s.setHoldPos(base);
                    s.setFollowState(3);
                    waiting++;
                }
            }
        }
    }

    // ---- what is carried, as block displays ----

    /** Shows a carried ladder or a ram with the soldiers carrying it, moved along with them each second. */
    private void show(Siege.Work w, List<AbstractRecruitEntity> carrying) {
        Vec3 centre = Vec3.ZERO;
        for (AbstractRecruitEntity s : carrying) {
            centre = centre.add(s.position());
        }
        centre = centre.scale(1.0 / carrying.size());
        Point toward = w.foot();
        double dx = toward.x() - centre.x;
        double dz = toward.z() - centre.z;
        // A ladder on their shoulders, a ram at their hands.
        Vec3 at = centre.add(0, w.kind() == Siege.Engine.LADDERS ? 1.7 : 0.8, 0);
        if (SableLink.available() && w.kind() == Siege.Engine.LADDERS) {
            // A real frame: laid on its carriers' shoulders, above their heads.
            String key = frameKeys.computeIfAbsent(w, k -> "siege" + (++frameSerial));
            frames.put(w, new SableLink.Frame(key, false, 0, at.x, at.y + 0.6, at.z, Math.atan2(dx, dz)));
            return;
        }
        moveDisplays(w, at, (float) Math.toDegrees(Math.atan2(-dx, dz)));
    }

    /** Shows a tower where it has rolled to, facing the wall, hovering over the ground. */
    private void showTower(Siege.Work w) {
        Point at = w.at();
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) Math.floor(at.x()), (int) Math.floor(at.z()));
        if (w.state() == Siege.State.CARRIED) {
            clearAhead(at, ground);
        }
        double y = ground + HOVER;
        if (SableLink.available()) {
            String key = frameKeys.computeIfAbsent(w, k -> "siege" + (++frameSerial));
            frames.put(w, new SableLink.Frame(key, true, TOWER_TALL, at.x(), y, at.z(), Math.atan2(-ox, -oz)));
            return;
        }
        moveDisplays(w, new Vec3(at.x(), y, at.z()), (float) Math.toDegrees(Math.atan2(ox, -oz)));
    }

    /**
     * Breaks everything standing in the next couple of blocks before a rolling tower's front, its full width and height
     * from {@code ground} up, but never the walls or anything within them.
     */
    private void clearAhead(Point at, int ground) {
        Set<BlockPos> ahead = new HashSet<>();
        for (double d = 0; d <= 2.5; d += 0.5) {
            for (double a = -TOWER_HALF_WIDTH - 0.5; a <= TOWER_HALF_WIDTH + 0.5; a += 0.5) {
                double x = at.x() - ox * (Siege.TOWER_OUT + d) - oz * a;
                double z = at.z() - oz * (Siege.TOWER_OUT + d) + ox * a;
                if (siege.region(new Point(x, z)) == Siege.Region.OUT && level.isLoaded(BlockPos.containing(x, ground, z))) {
                    for (int y = ground; y < ground + TOWER_TALL + 1; y++) {
                        ahead.add(BlockPos.containing(x, y, z));
                    }
                }
            }
        }
        placed.values().forEach(m -> ahead.removeAll(m.keySet())); // the siege's own ladders stay
        for (BlockPos p : ahead) {
            BlockState state = level.getBlockState(p);
            if (!state.isAir() && !state.liquid() && state.getDestroySpeed(level, p) >= 0) {
                level.destroyBlock(p, false);
            }
        }
    }

    private void moveDisplays(Siege.Work w, Vec3 at, float yaw) {
        List<Display.BlockDisplay> parts = shown.get(w);
        if (parts == null || parts.stream().anyMatch(Entity::isRemoved)) {
            hide(w);
            parts = parts(w, at);
            shown.put(w, parts);
        }
        for (Display.BlockDisplay d : parts) {
            d.teleportTo(at.x, at.y, at.z);
            d.setYRot(yaw);
        }
    }

    private List<Display.BlockDisplay> parts(Siege.Work w, Vec3 at) {
        List<Display.BlockDisplay> parts = new ArrayList<>();
        switch (w.kind()) {
            case LADDERS -> parts.add(display(Blocks.LADDER.defaultBlockState(), 1, 6, 1, at, true));
            case RAMS -> parts.add(display(Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z),
                    0.8f, 0.8f, 5, at, false));
            // shortcut: without Sable a rolling tower shows as a plain box, not the wedge; fine while Sable is always present.
            case TOWERS -> parts.add(display(Blocks.SPRUCE_PLANKS.defaultBlockState(), 2 * TOWER_HALF_WIDTH + 1, TOWER_TALL,
                    TOWER_LONG + 1, at, false));
        }
        return parts;
    }

    /**
     * A block display of {@code state} stretched to {@code sx} × {@code sy} × {@code sz}, centred on {@code at} (laid
     * flat along its length if {@code flat}), gliding to wherever it is moved over a second.
     */
    private Display.BlockDisplay display(BlockState state, float sx, float sy, float sz, Vec3 at, boolean flat) {
        Display.BlockDisplay d = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
        CompoundTag tag = new CompoundTag();
        tag.put("block_state", NbtUtils.writeBlockState(state));
        CompoundTag t = new CompoundTag();
        // Laid flat: a quarter turn about x, so its height runs along z.
        t.put("left_rotation", floats(flat ? 0.7071f : 0, 0, 0, flat ? 0.7071f : 1));
        t.put("right_rotation", floats(0, 0, 0, 1));
        t.put("translation", floats(-sx / 2, flat ? 0.5f : 0, flat ? -sy / 2 : -sz / 2));
        t.put("scale", floats(sx, sy, sz));
        tag.put("transformation", t);
        tag.putInt("teleport_duration", 20);
        d.load(tag);
        d.moveTo(at.x, at.y, at.z, 0, 0);
        d.addTag(TAG);
        level.addFreshEntity(d);
        return d;
    }

    private static ListTag floats(float... values) {
        ListTag list = new ListTag();
        for (float v : values) {
            list.add(FloatTag.valueOf(v));
        }
        return list;
    }

    private void hide(Siege.Work w) {
        frames.remove(w);
        List<Display.BlockDisplay> parts = shown.remove(w);
        if (parts != null) {
            parts.forEach(Entity::discard);
        }
    }

    /** Everything of the siege taken down: ladders, towers, displays; climbers set down. */
    void clear() {
        for (Siege.Work w : new ArrayList<>(placed.keySet())) {
            take(w);
        }
        for (Siege.Work w : new ArrayList<>(shown.keySet())) {
            hide(w);
        }
        for (UUID id : climbing) {
            if (level.getEntity(id) instanceof AbstractRecruitEntity s) {
                s.setNoGravity(false);
            }
        }
        queued.clear();
        climbing.clear();
        pressed.clear();
        frames.clear();
        SableLink.siegeFrames(level, List.of());
    }
}
