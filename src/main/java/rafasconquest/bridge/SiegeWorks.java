package rafasconquest.bridge;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock;
import rafasconquest.battle.Machine;
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
 * A siege's strategems and gates in the world, for the soldiers who are real: Siegeworks' ladders, towers and rams
 * where the numbers ({@link Siege}) have them (carried, rolled, stood against the wall), the climb (Recruits soldiers
 * can't path up a ladder, so a climber is lifted up it), the Gate Blocks' buttons pressed by soldiers near them, the
 * real gates kept in step with the numbers, and the breach: carved where the numbers broke the wall, or found where real
 * shot did. Without Siegeworks the strategems aren't seen, but the climb and the breach still are. Only
 * {@link RecruitsBattle} uses it.
 */
final class SiegeWorks {
    /** Every engine a siege puts up carries this tag, to be swept up after ({@link #sweep}). */
    static final String TAG = RafasConquest.MOD_ID + "_siege";
    /** Soldiers this close to a Gate Block's button press it. */
    private static final double PRESS_REACH = 2.5;
    /** Soldiers of a crew this close to their ladder's foot are queued to climb it; this many wait at once. */
    private static final double CLIMB_FROM = 10;
    private static final int QUEUE = 3;
    /** A climber goes up this far a tick. */
    private static final double CLIMB_STEP = 0.15;
    /** A tower's climbers start from this far out behind its middle, and step off this far in from the wall's face. */
    private static final double TOWER_CLIMB = Siege.TOWER_BEHIND - 1;
    /** Up a tower's back the climbers step in one for every two up, this far at most. */
    private static final double TOWER_DEPTH = 6.5;
    /** A tower's front this wide, its full height, is cleared of the trees before it as it rolls. */
    private static final int TOWER_HALF_WIDTH = 3;
    private static final int TOWER_TALL = 15;
    /** A breach carved for the numbers: this many either side of its middle, this high. */
    private static final int BREACH_HALF = 1;
    private static final int BREACH_HIGH = 4;
    /** The real wall is looked over for holes this often (ticks of battle seconds). */
    private static final int LOOK_EVERY = 5;

    private final ServerLevel level;
    private Siege siege;
    private final List<BlockPos> gateBlocks;
    /** The walls as surveyed: each one's walkway, the height soldiers stand at on it. */
    private final SiegeSurvey.Result survey;
    /** Each strategem's Siegeworks engine; and where its climbers start up (a ladder's foot, a tower's back). */
    private final Map<Siege.Work, Entity> engines = new HashMap<>();
    private final Map<Siege.Work, BlockPos> ladders = new HashMap<>();
    /** Each tower last second: where the numbers had it, and where the real one stood. */
    private final Map<Siege.Work, Point[]> rolling = new HashMap<>();
    /** A real tower this close to its place against the wall is there. */
    private static final double DOCKED = 1.5;
    /** A real tower driving yet moving less than this in a second is stuck. */
    private static final double STUCK = 0.05;
    /** Soldiers waiting at a ladder's foot, or on it, by soldier: the ladder's foot. */
    private final Map<UUID, BlockPos> queued = new LinkedHashMap<>();
    private final Set<UUID> climbing = new HashSet<>();
    /** Attackers who have pressed a button (each presses once). */
    private final Set<UUID> pressed = new HashSet<>();
    /** The wall carved away for a breach the numbers made, with what stood there (put back after). */
    private final Map<BlockPos, BlockState> carved = new LinkedHashMap<>();
    /** Climbers sent to board a tower (when, in battle seconds), and those sent out over its bridge. */
    private final Map<UUID, Integer> boarding = new HashMap<>();
    private final Set<UUID> leaving = new HashSet<>();
    /** The breaches the numbers opened that are carved in the real walls (or were real holes already), by their mouths. */
    private final Set<Point> breachesDone = new HashSet<>();
    private int seconds;

    SiegeWorks(ServerLevel level, Siege siege, SiegeSurvey.Result survey) {
        this.level = level;
        this.siege = siege;
        this.gateBlocks = survey.gates();
        this.survey = survey;
        matchTowerSpeed();
    }

    /** The numbers roll their towers as fast as Siegeworks' own do, when it says how fast. */
    private void matchTowerSpeed() {
        double speed = towerSpeed();
        if (speed > 0) {
            siege.towerSpeed(speed);
        }
    }

    /** A Siegeworks tower's speed driven by its crew, blocks a second, from Siegeworks' config; 0 if unknown. */
    private static double towerSpeed() {
        try {
            var type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.fromNamespaceAndPath(Machine.MOD, "siege_tower")).orElse(null);
            if (type == null) {
                return 0;
            }
            Object perTick = Class.forName("me.mss1r.siegeworks.config.SiegeworksServerConfig")
                    .getMethod("getMovementSpeed", net.minecraft.world.entity.EntityType.class, boolean.class).invoke(null, type, false);
            return (Double) perTick * 20;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            return 0;
        }
    }

    /** The way out from the wall a work is brought against, in whole blocks: its ladder or tower faces the other way. */
    private static int ox(Siege.Work w) {
        return (int) Math.round(w.outX());
    }

    private static int oz(Siege.Work w) {
        return (int) Math.round(w.outZ());
    }

    /** The walkway of the wall a work is brought against. */
    private int walkY(Siege.Work w) {
        return survey.walkY(w.outX(), w.outZ());
    }

    /** The ladder or tower whose climbers start up at {@code foot}; null if none does now. */
    @Nullable
    private Siege.Work workAt(BlockPos foot) {
        for (Map.Entry<Siege.Work, BlockPos> e : ladders.entrySet()) {
            if (e.getValue().equals(foot)) {
                return e.getKey();
            }
        }
        return null;
    }

    /** A new day's siege (the armies regrouped): everything of the last is taken down first. */
    void rebind(Siege next) {
        clear();
        siege = next;
        matchTowerSpeed();
    }

    /** The chunks the gates stand in, kept loaded through the battle so the real gates are always there to fight for. */
    List<BlockPos> gates() {
        return gateBlocks;
    }

    // ---- each second ----

    /**
     * Each battle second: the gates (what numbers did to them, then their real state back to the numbers), the
     * strategems shown where they are and stood up or taken down, buttons pressed, climbers queued, and the breach.
     */
    void second(Function<FieldBlock, List<AbstractRecruitEntity>> soldiers, List<AbstractRecruitEntity> attackers,
                List<AbstractRecruitEntity> defenders) {
        syncGates();
        for (Siege.Work w : siege.works()) {
            FieldBlock carrier = w.carrier();
            List<AbstractRecruitEntity> carrying = carrier == null ? List.of() : soldiers.apply(carrier);
            switch (w.kind()) {
                case LADDERS -> ladder(w, carrying);
                case TOWERS -> tower(w, soldiers);
                case RAMS -> ram(w, carrying);
            }
            BlockPos foot = ladders.get(w);
            if (foot != null) {
                queueClimbers(w, foot, soldiers);
            }
        }
        pressButtons(attackers, defenders);
        breach();
        seconds++;
    }

    /** Each server tick: climbers go up their ladders and over onto the walkway at the top. */
    void tick() {
        for (var it = queued.entrySet().iterator(); it.hasNext(); ) {
            var e = it.next();
            if (!(level.getEntity(e.getKey()) instanceof AbstractRecruitEntity s) || !s.isAlive() || !ladders.containsValue(e.getValue())) {
                if (level.getEntity(e.getKey()) instanceof AbstractRecruitEntity s) {
                    s.setNoGravity(false);
                }
                climbing.remove(e.getKey());
                it.remove();
                continue;
            }
            BlockPos foot = e.getValue();
            Siege.Work w = workAt(foot);
            double cx = foot.getX() + 0.5;
            double cz = foot.getZ() + 0.5;
            if (!climbing.contains(s.getUUID())) {
                if (Math.hypot(s.getX() - cx, s.getZ() - cz) <= 1.3) {
                    climbing.add(s.getUUID());
                    s.setNoGravity(true);
                    s.getNavigation().stop();
                }
                continue;
            }
            int walkY = walkY(w);
            if (s.getY() + CLIMB_STEP < walkY) {
                double in = w.kind() == Siege.Engine.TOWERS ? Math.min(TOWER_DEPTH, (s.getY() - foot.getY()) / 2) : 0;
                s.getNavigation().stop();
                s.setDeltaMovement(Vec3.ZERO);
                s.moveTo(cx - ox(w) * in, s.getY() + CLIMB_STEP, cz - oz(w) * in, s.getYRot(), s.getXRot());
                continue;
            }
            // Over the top: onto the walkway, two blocks in past the outer row.
            Vec3 onto = landing(w, foot);
            s.setNoGravity(false);
            s.moveTo(onto.x, walkY, onto.z, s.getYRot(), s.getXRot());
            s.setHoldPos(new Vec3(onto.x, walkY, onto.z));
            climbing.remove(s.getUUID());
            it.remove();
        }
    }

    /** Where a climber up {@code w} (its foot {@code foot}) steps onto the walkway: over its top, toward the inside. */
    private Vec3 landing(Siege.Work w, BlockPos foot) {
        // A tower's ladders are up its back: across it first, to land as far in as off a ladder.
        double in = 2 + (w.kind() == Siege.Engine.TOWERS ? w.out() + TOWER_CLIMB : 0);
        return new Vec3(foot.getX() + 0.5 - ox(w) * in, walkY(w), foot.getZ() + 0.5 - oz(w) * in);
    }

    // ---- the strategems ----

    /** A ladder: on its carriers' shoulders while carried, stood against the wall (and climbed) once up. */
    private void ladder(Siege.Work w, List<AbstractRecruitEntity> carrying) {
        if (w.state() == Siege.State.UP) {
            if (!ladders.containsKey(w)) {
                standLadder(w);
            }
            return;
        }
        unregister(w);
        if (carrying.isEmpty()) {
            drop(w);
            return;
        }
        Vec3 centre = centre(carrying);
        Entity e = engine(w, "siege_ladder");
        if (e != null) {
            e.teleportTo(centre.x, centre.y + 1.7, centre.z);
            e.setYRot(yaw(centre, w.foot()));
        }
    }

    /** Stands a ladder at a gap between the merlons, its foot on the ground, if the place is loaded; its climbers queue there. */
    private void standLadder(Siege.Work w) {
        int ox = ox(w);
        int oz = oz(w);
        int walkY = walkY(w);
        int ax = -oz; // along the wall
        int az = ox;
        BlockPos cell = BlockPos.containing(w.foot().x() - ox, 0, w.foot().z() - oz);
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
        BlockPos foot = cell.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, cell.getX(), cell.getZ()));
        ladders.put(w, foot);
        Entity e = engine(w, "siege_ladder");
        if (e != null) {
            // Long enough for the wall: three blocks a section past the first.
            int sections = Math.max(0, Math.min(4, (int) Math.ceil((walkY - foot.getY() + 1) / 3.0) - 1));
            call(e, "setSections", new Class<?>[]{int.class}, sections);
            // Stood just out from the wall, it tips forward against it.
            Vec3 at = Vec3.atBottomCenterOf(foot).add(ox * 1.5, 0, oz * 1.5);
            call(e, "standAt", new Class<?>[]{Vec3.class, float.class}, at, yaw(at, new Point(foot.getX() + 0.5 - ox, foot.getZ() + 0.5 - oz)));
        }
    }

    /**
     * A tower where it has rolled to, facing the wall, clearing its way; against the wall, its bridge down, and its
     * climbers boarding it by Siegeworks' own way (up its floors, out over the bridge), or lifted up its back without.
     */
    private void tower(Siege.Work w, Function<FieldBlock, List<AbstractRecruitEntity>> soldiers) {
        Point at = w.at();
        BlockPos column = BlockPos.containing(at.x(), 0, at.z());
        if (!level.isLoaded(column)) {
            return;
        }
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ());
        int ox = ox(w);
        int oz = oz(w);
        boolean up = w.state() == Siege.State.UP;
        Entity e = engine(w, "siege_tower");
        Point[] last = rolling.get(w);
        if (e != null && last == null) {
            e.teleportTo(at.x(), ground, at.z()); // just made: on the ground where the numbers have it
            standOff(w);
        }
        // The real tower rolls itself, Siegeworks' own way, while its crew pushes (the numbers move it), and on to the
        // wall if the numbers got there first; it is only put where the numbers are if it sticks short of them.
        Point real = e == null ? at : new Point(e.getX(), e.getZ());
        boolean pushed = last != null && last[0].distanceTo(at) > 1e-3;
        boolean stuck = last != null && last[1].distanceTo(real) < STUCK && (pushed || up);
        boolean docked = e == null ? up : real.distanceTo(w.foot()) <= DOCKED || up && stuck;
        if (e != null && stuck && !docked && at.distanceTo(w.foot()) < real.distanceTo(w.foot())) {
            e.teleportTo(at.x(), ground, at.z());
            real = at;
        }
        rolling.put(w, new Point[]{at, real});
        if (!docked) {
            BlockPos ahead = BlockPos.containing(real.x(), 0, real.z());
            clearAhead(w, real, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ahead.getX(), ahead.getZ()));
            unregister(w);
        } else if (!ladders.containsKey(w) && !boarding()) {
            BlockPos back = BlockPos.containing(w.foot().x() + ox * TOWER_CLIMB, 0, w.foot().z() + oz * TOWER_CLIMB);
            ladders.put(w, back.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, back.getX(), back.getZ())));
        }
        if (e == null) {
            return;
        }
        e.setYRot((float) Math.toDegrees(Math.atan2(ox, -oz))); // facing the wall, so driving forward goes at it
        call(e, "setDebugAutoDriving", new Class<?>[]{boolean.class}, !docked && (pushed || up));
        if (docked) {
            lowerBridge(e); // onto the wall: its climbers step off over it
        }
        if (docked && boarding()) {
            board(w, e, soldiers);
        }
    }

    /**
     * The tower decides where it stops, reading the ground it would stand on: where its ramp comes down onto its wall as
     * near level as it can ({@link Siege#rampStandOff}).
     */
    private void standOff(Siege.Work w) {
        double ox = w.outX();
        double oz = w.outZ();
        // The wall's face, straight in from where it was to stop.
        double faceX = w.foot().x() - ox * w.out();
        double faceZ = w.foot().z() - oz * w.out();
        w.standOff(Siege.rampStandOff(walkY(w), out -> {
            int x = net.minecraft.util.Mth.floor(faceX + ox * out);
            int z = net.minecraft.util.Mth.floor(faceZ + oz * out);
            // Through the chunk, which loads it: an unloaded one reads as the world's floor.
            return level.getChunk(x >> 4, z >> 4).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
        }));
    }

    /** A tower's climbers this close to it are sent to board it; one not aboard after this long is sent again. */
    private static final double BOARD_FROM = 16;
    private static final int BOARD_RETRY = 30;

    /**
     * Siegeworks boards the tower's climbers (its infantry, not its own crew) as it boards Recruits soldiers a player
     * commands: walked round to it, seated up its floors; once aboard, each is sent out over the bridge onto the wall.
     */
    private void board(Siege.Work w, Entity tower, Function<FieldBlock, List<AbstractRecruitEntity>> soldiers) {
        for (FieldBlock crew : w.crews()) {
            if (crew.arm() == BattlePlan.Arm.ENGINES) {
                continue;
            }
            for (AbstractRecruitEntity s : soldiers.apply(crew)) {
                if (s.getVehicle() == tower) {
                    if (leaving.add(s.getUUID())) {
                        invoke(EXIT, s);
                    }
                    continue;
                }
                Integer since = boarding.get(s.getUUID());
                if (s.getY() < walkY(w) - 1 && !s.isPassenger() && s.distanceTo(tower) <= BOARD_FROM
                        && (since == null || seconds - since >= BOARD_RETRY)) {
                    boarding.put(s.getUUID(), seconds);
                    leaving.remove(s.getUUID());
                    invoke(BOARD, s, tower);
                }
            }
        }
    }

    /** Siegeworks' own boarding (package-private there) and exit over the bridge; null if this Siegeworks has neither. */
    @Nullable
    private static final java.lang.reflect.Method BOARD = method("RecruitsSiegeTraversal", "requestBoarding", true);
    @Nullable
    private static final java.lang.reflect.Method EXIT = method("RecruitsCompat", "requestTransportExit", false);

    @Nullable
    private static java.lang.reflect.Method method(String type, String name, boolean withMachine) {
        try {
            Class<?> c = Class.forName("me.mss1r.siegeworks.integration.recruits." + type);
            java.lang.reflect.Method m = withMachine
                    ? c.getDeclaredMethod(name, AbstractRecruitEntity.class, Class.forName("me.mss1r.siegeworks.entity.base.AbstractSiegeEntity"))
                    : c.getDeclaredMethod(name, LivingEntity.class);
            m.setAccessible(true);
            return m;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            return null; // no Siegeworks, or not this one: the tower is climbed by our lift
        }
    }

    /** Whether towers are boarded the Siegeworks way. */
    private static boolean boarding() {
        return BOARD != null && EXIT != null;
    }

    private static void invoke(java.lang.reflect.Method m, Object... args) {
        try {
            m.invoke(null, args);
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.debug("[siege] Siegeworks {} failed: {}", m.getName(), e.toString());
        }
    }

    /** A ram at its crew's hands, carried round to the gate and left there. */
    private void ram(Siege.Work w, List<AbstractRecruitEntity> carrying) {
        if (carrying.isEmpty()) {
            if (w.state() == Siege.State.CARRIED) {
                drop(w);
            }
            return;
        }
        Vec3 centre = centre(carrying);
        Entity e = engine(w, "battering_ram");
        if (e != null) {
            e.teleportTo(centre.x, centre.y, centre.z);
            e.setYRot(yaw(centre, w.foot()));
        }
    }

    /** The work's Siegeworks engine, made if it isn't there yet; null without Siegeworks. */
    @Nullable
    private Entity engine(Siege.Work w, String id) {
        Entity e = engines.get(w);
        if (e != null && !e.isRemoved()) {
            return e;
        }
        e = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.fromNamespaceAndPath(Machine.MOD, id))
                .map(t -> t.create(level)).orElse(null);
        if (e == null) {
            engines.remove(w);
            return null;
        }
        Point foot = w.kind() == Siege.Engine.TOWERS ? w.at() : w.foot();
        e.moveTo(foot.x(), walkY(w), foot.z(), 0, 0);
        e.addTag(TAG);
        level.addFreshEntity(e);
        engines.put(w, e);
        return e;
    }

    /** A Siegeworks tower's bridge (its private controller); null if this Siegeworks has none such. */
    @Nullable
    private static final java.lang.reflect.Field BRIDGE = bridgeField();

    @Nullable
    private static java.lang.reflect.Field bridgeField() {
        try {
            java.lang.reflect.Field f = Class.forName("me.mss1r.siegeworks.entity.siege.SiegeTowerEntity").getDeclaredField("bridge");
            f.setAccessible(true);
            return f;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            return null;
        }
    }

    /**
     * Lowers a tower's bridge. Siegeworks' own {@code setDeployed} does it only for whoever drives the tower or holds its
     * reins; our crew pushes it from behind, so its bridge is opened on its controller instead. Doing it again does nothing.
     */
    private static void lowerBridge(Entity tower) {
        if (BRIDGE == null || !BRIDGE.getDeclaringClass().isInstance(tower)) {
            return;
        }
        try {
            Object bridge = BRIDGE.get(tower);
            bridge.getClass().getMethod("setOpen", boolean.class).invoke(bridge, true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.debug("[siege] couldn't lower {}'s bridge: {}", tower.getType(), e.toString());
        }
    }

    /** Calls one of Siegeworks' own methods on its engine; quietly nothing if this Siegeworks has none such. */
    private static void call(Entity e, String method, Class<?>[] types, Object... args) {
        try {
            e.getClass().getMethod(method, types).invoke(e, args);
        } catch (ReflectiveOperationException | RuntimeException ex) {
            RafasConquest.LOGGER.debug("[siege] Siegeworks {} has no {}: {}", e.getType(), method, ex.toString());
        }
    }

    private void drop(Siege.Work w) {
        Entity e = engines.remove(w);
        if (e != null) {
            e.discard();
        }
    }

    /** A ladder or tower no longer up: no more climbing it; its climbers drop off. */
    private void unregister(Siege.Work w) {
        BlockPos foot = ladders.remove(w);
        if (foot == null) {
            return;
        }
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

    private static Vec3 centre(List<AbstractRecruitEntity> soldiers) {
        Vec3 centre = Vec3.ZERO;
        for (AbstractRecruitEntity s : soldiers) {
            centre = centre.add(s.position());
        }
        return centre.scale(1.0 / soldiers.size());
    }

    /** The yaw (degrees, Minecraft's) from {@code from} toward {@code to}. */
    private static float yaw(Vec3 from, Point to) {
        return (float) Math.toDegrees(Math.atan2(-(to.x() - from.x), to.z() - from.z));
    }

    /**
     * Fells the trees standing in the next couple of blocks before a rolling tower's front, its full width and height
     * from {@code ground} up, but never the walls or anything within them.
     */
    private void clearAhead(Siege.Work w, Point at, int ground) {
        int ox = ox(w);
        int oz = oz(w);
        Set<BlockPos> ahead = new HashSet<>();
        for (double d = 0; d <= 2.5; d += 0.5) {
            for (double a = -TOWER_HALF_WIDTH - 0.5; a <= TOWER_HALF_WIDTH + 0.5; a += 0.5) {
                double x = at.x() - ox * (Siege.TOWER_HALF_LENGTH + d) - oz * a;
                double z = at.z() - oz * (Siege.TOWER_HALF_LENGTH + d) + ox * a;
                if (siege.region(new Point(x, z)) == Siege.Region.OUT && level.isLoaded(BlockPos.containing(x, ground, z))) {
                    for (int y = ground; y < ground + TOWER_TALL + 1; y++) {
                        ahead.add(BlockPos.containing(x, y, z));
                    }
                }
            }
        }
        for (BlockPos p : ahead) {
            BlockState state = level.getBlockState(p);
            // Only trees and bamboo: logs and wood (the logs tag holds both), leaves, bamboo shoots; anything else stands.
            if (state.is(net.minecraft.tags.BlockTags.LOGS) || state.is(net.minecraft.tags.BlockTags.LEAVES)
                    || state.is(net.minecraft.world.level.block.Blocks.BAMBOO) || state.is(net.minecraft.world.level.block.Blocks.BAMBOO_SAPLING)) {
                level.destroyBlock(p, false);
            }
        }
    }

    /** Soldiers of the work's crews near its ladder's foot, still below the walkway, are queued to climb, a few at a time. */
    // With a Siegeworks ladder standing there, its climbers are only told to hold the walkway above it: a Recruits soldier
    // who can't walk to where it should be climbs the nearest ladder there by Siegeworks' own way. Else they queue for the lift.
    private void queueClimbers(Siege.Work w, BlockPos foot, Function<FieldBlock, List<AbstractRecruitEntity>> soldiers) {
        boolean ownClimb = w.kind() == Siege.Engine.LADDERS && engines.get(w) != null && !engines.get(w).isRemoved();
        Vec3 top = landing(w, foot);
        int walkY = walkY(w);
        long waiting = queued.values().stream().filter(foot::equals).count();
        Vec3 base = Vec3.atBottomCenterOf(foot);
        for (FieldBlock crew : w.crews()) {
            if (crew.arm() == BattlePlan.Arm.ENGINES) {
                continue; // a tower's crew stays with it
            }
            for (AbstractRecruitEntity s : soldiers.apply(crew)) {
                if (ownClimb) {
                    if (s.getY() < walkY - 1 && Math.hypot(s.getX() - base.x, s.getZ() - base.z) <= CLIMB_FROM) {
                        s.setHoldPos(top);
                        s.setFollowState(3);
                    }
                    continue;
                }
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

    // ---- the breaches ----

    /**
     * Each breach the numbers open is carved in the real wall there (once loaded); every few seconds each wall the
     * attackers come at, not yet broken, is looked over for a hole real shot made (one soldiers can walk through, ground
     * to inside), which the numbers then take as that wall's breach.
     */
    private void breach() {
        for (Siege.Gate open : siege.breaches()) {
            if (!breachesDone.contains(open.mouth()) && carve(open)) {
                breachesDone.add(open.mouth());
            }
        }
        if (seconds % LOOK_EVERY != 0) {
            return;
        }
        for (Siege.Front f : siege.fronts()) {
            Point hole = f.breached() ? null : findHole(f);
            if (hole != null) {
                int before = siege.breaches().size();
                siege.openBreach(hole);
                if (siege.breaches().size() > before) {
                    breachesDone.add(siege.breaches().get(before).mouth()); // the hole is there already
                }
            }
        }
    }

    /** Carves a breach through the wall at {@code breach}, what stood there kept to put back after. */
    private boolean carve(Siege.Gate breach) {
        int ox = (int) Math.round(breach.outX());
        int oz = (int) Math.round(breach.outZ());
        Point face = breach.mouth();
        int ax = -oz;
        int az = ox;
        BlockPos outside = BlockPos.containing(face.x() + ox * 2, 0, face.z() + oz * 2);
        if (!level.isLoaded(outside) || !level.isLoaded(outside.offset(-ox * (thickness() + 4), 0, -oz * (thickness() + 4)))) {
            return false;
        }
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, outside.getX(), outside.getZ());
        for (int in = 0; in <= thickness() + 1; in++) {
            for (int a = -BREACH_HALF; a <= BREACH_HALF; a++) {
                for (int y = ground; y < ground + BREACH_HIGH; y++) {
                    BlockPos p = new BlockPos((int) Math.floor(face.x()) - ox * in + ax * a, y, (int) Math.floor(face.z()) - oz * in + az * a);
                    BlockState state = level.getBlockState(p);
                    if (!state.isAir() && state.getDestroySpeed(level, p) >= 0) {
                        carved.putIfAbsent(p, state);
                        level.setBlock(p, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return true;
    }

    /** A walkable hole through a wall the attackers come at, away from the gates: its outer face; null if there is none (or it isn't loaded). */
    @Nullable
    private Point findHole(Siege.Front f) {
        int ox = (int) Math.round(f.outX());
        int oz = (int) Math.round(f.outZ());
        int ax = -oz;
        int az = ox;
        Point mid = f.mid();
        int half = (int) Math.floor(f.length() / 2);
        for (int r = -half; r <= half; r++) {
            Point face = new Point(mid.x() + ax * r, mid.z() + az * r);
            boolean byGate = siege.gates().stream().anyMatch(g -> g.gate().mouth().distanceTo(face) < 5);
            BlockPos outside = BlockPos.containing(face.x() + ox * 2, 0, face.z() + oz * 2);
            if (byGate || !level.isLoaded(outside)) {
                continue;
            }
            int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, outside.getX(), outside.getZ());
            for (int y = ground - 1; y <= ground + 1; y++) {
                if (passes(face, y, ox, oz)) {
                    return face;
                }
            }
        }
        return null;
    }

    /** Whether soldiers can walk in through the wall at {@code face}, at height {@code y}: two high and clear all the way. */
    private boolean passes(Point face, int y, int ox, int oz) {
        for (int in = 0; in <= thickness(); in++) {
            BlockPos p = BlockPos.containing(face.x() - ox * in, y, face.z() - oz * in);
            if (!level.isLoaded(p) || !level.getBlockState(p).getCollisionShape(level, p).isEmpty()
                    || !level.getBlockState(p.above()).getCollisionShape(level, p.above()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private int thickness() {
        return (int) Math.ceil(siege.plan().thickness());
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
        if (i >= gateBlocks.size()) {
            return null; // the breach: no Gate Block
        }
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

    /**
     * Everything of the siege taken down: its engines, climbers set down, the carved breach put back (a hole real shot
     * made stays).
     */
    void clear() {
        for (Siege.Work w : new ArrayList<>(ladders.keySet())) {
            unregister(w);
        }
        engines.values().forEach(Entity::discard);
        engines.clear();
        rolling.clear();
        for (UUID id : climbing) {
            if (level.getEntity(id) instanceof AbstractRecruitEntity s) {
                s.setNoGravity(false);
            }
        }
        queued.clear();
        climbing.clear();
        pressed.clear();
        boarding.clear();
        leaving.clear();
        carved.forEach((p, state) -> {
            if (level.isLoaded(p)) {
                level.setBlock(p, state, Block.UPDATE_CLIENTS);
            }
        });
        carved.clear();
        breachesDone.clear();
        seconds = 0;
    }

    /** Siege engines left from a battle that stopped without clearing (a crash), taken away when no battle runs. */
    static void sweep(ServerLevel level) {
        List<Entity> left = new ArrayList<>();
        for (Entity e : level.getAllEntities()) {
            if (e.getTags().contains(TAG)) {
                left.add(e);
            }
        }
        left.forEach(Entity::discard);
    }
}
