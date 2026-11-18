package rafasconquest.bridge;

import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.ticket.SubLevelLoadingTicketType;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.battle.FieldBattle;
import rafasconquest.battle.Ramming;
import rafasconquest.core.Swell;
import rafasconquest.core.SeaLanes;
import rafasconquest.style.ShipHulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A battle's real ships: one under each formation afloat near a player (its soldiers ride on its flat deck), a Sable
 * sub-level of a plain flat-topped hull ({@link ShipHulls#battle}), steered every tick as its {@link SableLink.Helm} says
 * and held at its waterline. A ship whose bow strikes an enemy ship is reported as a ram; grappled ships are held
 * together; a ship keeps clear of its own side's. Ships no helm asks for are taken away. Only {@link SableLink} calls this.
 */
final class BattleShips {
    private static final String TAG = RafasConquest.MOD_ID + ":battle_ship";
    /** How hard a ship is pulled toward where it makes for, per second (capped at its speed). */
    private static final double PULL = 1.5;
    /** Further than this from where its formation is, a ship is moved there at once. */
    private static final double SNAP = 40;
    /**
     * Blocks a second to keep clear of a ship of its own side (more than the fastest ship sails, so no ship pressing on
     * for its place can push into another), and the room kept round each hull.
     */
    private static final double SPREAD = 6;
    private static final double ROOM = 3;

    private static final Map<String, ServerSubLevel> ships = new HashMap<>();
    /** Each ship's footprint this tick, by formation id. */
    private static final Map<Integer, Ramming.Hull> hulls = new HashMap<>();
    /** Each formation's side this tick, by formation id. */
    private static final Map<Integer, Integer> sides = new HashMap<>();
    /** Each ship's deck height this tick (where its soldiers stand), by formation id. */
    private static final Map<Integer, Double> decks = new HashMap<>();
    /** Each ship's deck height above its keel, its card's. */
    private static final Map<Integer, Integer> deckHeights = new HashMap<>();

    private BattleShips() {
    }

    /** The battle a ship was built for: its ships saved by Sable on quitting load back later with the same formation ids. */
    private static final String BATTLE = RafasConquest.MOD_ID + ":battle_run";

    static List<SableLink.Ram> tick(ServerLevel level, String battle, List<SableLink.Helm> helms) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return List.of();
        }
        // Every tick, every battle ship there is, and never two for one formation: a second one, however it came about,
        // would sit in the first, unsteered and never taken away, so it goes at once. So does one of another battle.
        ships.values().removeIf(ServerSubLevel::isRemoved);
        List<ServerSubLevel> twice = new ArrayList<>();
        int stale = 0;
        for (ServerSubLevel sub : container.getAllSubLevels()) {
            CompoundTag tag = sub.getUserDataTag();
            if (tag != null && tag.contains(TAG) && !sub.isRemoved()) {
                if (!battle.equals(tag.getString(BATTLE))) {
                    if (ships.get(tag.getString(TAG)) == sub) {
                        ships.remove(tag.getString(TAG));
                    }
                    container.removeSubLevel(sub, SubLevelRemovalReason.REMOVED);
                    stale++;
                    continue;
                }
                ServerSubLevel tracked = ships.putIfAbsent(tag.getString(TAG), sub);
                if (tracked != null && tracked != sub) {
                    twice.add(sub);
                }
            }
        }
        for (ServerSubLevel sub : twice) {
            container.removeSubLevel(sub, SubLevelRemovalReason.REMOVED);
        }
        if (!twice.isEmpty()) {
            RafasConquest.LOGGER.warn("[battle] took away {} ships built twice for one formation", twice.size());
        }
        if (stale > 0 && !battle.isEmpty()) {
            RafasConquest.LOGGER.info("[battle] took away {} ships left from another battle", stale);
        }
        Set<String> wanted = new HashSet<>();
        Map<Integer, SableLink.Helm> sailing = new HashMap<>();
        hulls.clear();
        decks.clear();
        deckHeights.clear();
        sides.clear();
        List<SableLink.Helm> unbuilt = new ArrayList<>();
        for (SableLink.Helm h : helms) {
            wanted.add(key(h));
            sides.put(h.id(), h.side());
            deckHeights.put(h.id(), size(h).deck);
            ServerSubLevel sub = ships.get(key(h));
            if (sub == null) {
                if (level.getGameTime() % 20 == h.id() % 20) { // a try a second each, spread over the ticks
                    unbuilt.add(h);
                }
            } else {
                hulls.put(h.id(), hull(sub, h)); // where it lies, steered or not, so none is built on it
                decks.put(h.id(), middle(sub).y + size(h).deck + 1);
                RigidBodyHandle body = RigidBodyHandle.of(sub);
                if (body != null && body.isValid()) {
                    sailing.put(h.id(), h);
                }
            }
        }
        // Built only where it would lie clear of every other ship: else, next second.
        Map<Integer, Ramming.Hull> sailingHulls = new HashMap<>(hulls);
        for (SableLink.Helm h : unbuilt) {
            if (clearAt(h, h.x(), h.z()) && build(level, battle, h)) {
                hulls.put(h.id(), footprint(h, h.x(), h.z())); // so none built after it this tick lies on it
            }
        }
        hulls.keySet().retainAll(sailingHulls.keySet()); // a ship just built is measured from next tick
        List<SableLink.Ram> rams = new ArrayList<>();
        for (SableLink.Helm a : sailing.values()) {
            for (SableLink.Helm b : sailing.values()) {
                if (a.side() == b.side() || a.lashed().contains(b.id()) || a.hooked().contains(b.id()) || b.hooked().contains(a.id())) {
                    continue;
                }
                double closing = Ramming.closing(hulls.get(a.id()), hulls.get(b.id()));
                if (closing > 0) {
                    double[] knock = Ramming.knock(hulls.get(a.id()), hulls.get(b.id()), closing);
                    rams.add(new SableLink.Ram(a.id(), b.id(), knock[0], knock[1]));
                }
            }
        }
        for (SableLink.Helm h : sailing.values()) {
            steer(ships.get(key(h)), h, sailing, level.getGameTime());
        }
        for (Map.Entry<String, ServerSubLevel> e : new HashMap<>(ships).entrySet()) {
            if (!wanted.contains(e.getKey())) {
                ships.remove(e.getKey());
                if (!e.getValue().isRemoved()) {
                    container.removeSubLevel(e.getValue(), SubLevelRemovalReason.REMOVED);
                }
            }
        }
        return rams;
    }

    /** Where formation {@code id}'s ship is, which way its bow points and its deck's height: {x, z, dirX, dirZ, deckY}; null with no ship sailing. */
    @org.jetbrains.annotations.Nullable
    static double[] where(int id) {
        Ramming.Hull h = hulls.get(id);
        return h == null ? null : new double[]{h.x(), h.z(), h.dirX(), h.dirZ(), decks.getOrDefault(id, Double.NaN)};
    }

    /**
     * Where a spot on formation {@code id}'s deck is in the world: {@code right} and {@code back} of its hull's middle,
     * standing on the deck as the ship lies now, turned, rolled and pitched: {x, y, z}; null with no ship sailing.
     */
    @org.jetbrains.annotations.Nullable
    static double[] onDeck(int id, double right, double back) {
        ServerSubLevel sub = ships.get("battle-" + id);
        if (sub == null || sub.isRemoved()) {
            return null;
        }
        BoundingBox3ic box = sub.getPlot().getBoundingBox();
        // Built bow to +z, so its right (seen facing the bow) is -x and astern is -z.
        Vector3d at = sub.logicalPose().transformPosition(new Vector3d((box.minX() + box.maxX() + 1) / 2.0 - right,
                box.minY() + deckHeights.getOrDefault(id, ShipHulls.DECK) + 1, (box.minZ() + box.maxZ() + 1) / 2.0 - back));
        return new double[]{at.x, at.y, at.z};
    }

    /** Whether {@code sub} is a battle's ship (its guns' crews are the battle's own soldiers, not engineers of their own). */
    public static boolean battleShip(ServerSubLevel sub) {
        return sub.getUserDataTag().contains(BATTLE);
    }

    private static String key(SableLink.Helm h) {
        return "battle-" + h.id();
    }

    private static SeaLanes.Size size(SableLink.Helm h) {
        return h.hull();
    }

    /** The footprint formation {@code h}'s ship would have at (x, z), at rest, bow along its helm's facing. */
    private static Ramming.Hull footprint(SableLink.Helm h, double x, double z) {
        double length = Math.max(1e-6, Math.hypot(h.faceX(), h.faceZ()));
        SeaLanes.Size size = size(h);
        return new Ramming.Hull(x, z, h.faceX() / length, h.faceZ() / length, size.length / 2.0, size.width / 2.0, 0, 0);
    }

    /** Builds formation {@code h}'s ship where its formation is; false if it couldn't be built there yet. */
    private static boolean build(ServerLevel level, String battle, SableLink.Helm h) {
        double length = Math.max(1e-6, Math.hypot(h.faceX(), h.faceZ()));
        SeaLanes.Ship hull = new SeaLanes.Ship(key(h), h.x(), h.z(), h.faceX() / length, h.faceZ() / length, 0, 0, true,
                size(h), "", "");
        ServerSubLevel sub = SableShips.assemble(level, hull, TAG, ShipHulls.battle(size(h), h.cargo(),
                net.minecraft.world.item.DyeColor.byId(h.sail()), net.minecraft.world.item.DyeColor.byId(h.stripe())));
        if (sub == null) {
            return false;
        }
        CompoundTag tag = sub.getUserDataTag().copy();
        tag.putString(BATTLE, battle);
        sub.setUserDataTag(tag);
        // Else Sable unloads it once it touches a chunk not fully loaded, and it's built again: two ships blinking in and
        // out. Sable's own ticket type, as one of ours left in a save would be unknown to it on loading. Taking it away drops it.
        SubLevelContainer.getContainer(level).addForceLoadTicket(sub, SubLevelLoadingTicketType.COMMAND_FORCED, net.minecraft.util.Unit.INSTANCE);
        ships.put(key(h), sub);
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body != null && body.isValid()) {
            // Built bow to +z, it turns to its heading about the point it turns on (its centre of mass, aft of the middle
            // with the mast and sail), and moves so the middle of its hull is where its formation is.
            Pose3dc pose = sub.logicalPose();
            Vector3d middle = middle(sub);
            double ox = middle.x - pose.position().x();
            double oz = middle.z - pose.position().z();
            double cos = Math.cos(hull.yaw());
            double sin = Math.sin(hull.yaw());
            body.teleport(new Vector3d(h.x() - (ox * cos + oz * sin), sub.getUserDataTag().getDouble("y"), h.z() - (oz * cos - ox * sin)),
                    new Quaterniond().rotationY(hull.yaw()));
        }
        return true;
    }

    /**
     * Whether formation {@code h}'s ship, at (x, z), would lie clear of every other ship: with room to spare from its
     * own side's, and at least not in an enemy's (alongside one, near enough to board, is clear).
     */
    private static boolean clearAt(SableLink.Helm h, double x, double z) {
        Ramming.Hull at = footprint(h, x, z);
        for (Map.Entry<Integer, Ramming.Hull> e : hulls.entrySet()) {
            boolean own = sides.getOrDefault(e.getKey(), -1) == h.side();
            if (e.getKey() != h.id() && Ramming.overlap(at, e.getValue(), own ? ROOM / 2 : -0.25)) {
                return false;
            }
        }
        return true;
    }

    /** The ship's footprint now: the middle of its hull, its bow's heading, its size and velocity (none while it has no body yet). */
    private static Ramming.Hull hull(ServerSubLevel sub, SableLink.Helm h) {
        Pose3dc pose = sub.logicalPose();
        Vector3d bow = new Quaterniond(pose.orientation()).transform(new Vector3d(0, 0, 1));
        double flat = Math.max(1e-6, Math.hypot(bow.x, bow.z));
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        Vector3d v = body != null && body.isValid() ? body.getLinearVelocity(new Vector3d()) : new Vector3d();
        Vector3d middle = middle(sub);
        SeaLanes.Size size = size(h);
        return new Ramming.Hull(middle.x, middle.z, bow.x / flat, bow.z / flat, size.length / 2.0, size.width / 2.0, v.x, v.z);
    }

    /**
     * The middle of a ship's blocks, in the world. Not its pose's position: that is the point it turns about, its centre
     * of mass, a couple of blocks aft with the mast and sail.
     */
    private static Vector3d middle(ServerSubLevel sub) {
        BoundingBox3ic box = sub.getPlot().getBoundingBox();
        return sub.logicalPose().transformPosition(new Vector3d((box.minX() + box.maxX() + 1) / 2.0, box.minY(),
                (box.minZ() + box.maxZ() + 1) / 2.0));
    }

    /**
     * Sails for where its helm makes for, at its waterline, as a ship does ({@link FieldBattle#sail} for one that is only
     * numbers): it can't move sideways, only turn (at most {@link FieldBattle#TURN_RATE} a second) and go ahead along its
     * heading, or astern at half speed; it comes round toward its mark as it goes, slower the further it has to turn, so
     * it sails in an arc, backs to a place close astern, and once there comes round to lie as its helm says. It turns
     * away from ships of its own side it would foul, and drops back from one ahead of it. Grappled, it lies still.
     */
    private static void steer(ServerSubLevel sub, SableLink.Helm h, Map<Integer, SableLink.Helm> sailing, long time) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return;
        }
        Pose3dc pose = sub.logicalPose();
        double y = sub.getUserDataTag().getDouble("y");
        Vector3d velocity = body.getLinearVelocity(new Vector3d());
        Vector3d spin = body.getAngularVelocity(new Vector3d());
        Ramming.Hull me = hulls.get(h.id());
        double heading = Math.atan2(me.dirX(), me.dirZ());
        // Floats on the swell: up and down with it, pulled back to it, and rolling and pitching with it. Grappled ships
        // ride it as one: each on the water under the middle of all those lashed together.
        double[] on = grappledMiddle(h, sailing);
        Swell.Ride ride = SableShips.ride(sub, on[0], on[1], me.dirX(), me.dirZ(), me.halfLength(), me.halfBeam(), time);
        double waterline = ride.rise() + (y + ride.offset() - pose.position().y()) * PULL;
        if (h.lashed().stream().anyMatch(sailing::containsKey)) {
            // Grappled: it lies still alongside, or bow in, as it is.
            body.addLinearAndAngularVelocity(new Vector3d(0, waterline, 0).sub(velocity), SableShips.spinToward(pose, heading, spin, ride.slope()));
            return;
        }
        if (Math.hypot(me.x() - h.x(), me.z() - h.z()) > SNAP && clearAt(h, h.x(), h.z())) {
            // Moved as it lies, so the middle of its hull is where its formation is.
            body.teleport(new Vector3d(pose.position().x() + h.x() - me.x(), y, pose.position().z() + h.z() - me.z()),
                    new Quaterniond(pose.orientation()));
            body.addLinearAndAngularVelocity(velocity.negate(), spin.negate());
            return;
        }
        double dx = h.toX() - me.x();
        double dz = h.toZ() - me.z();
        double d = Math.hypot(dx, dz);
        boolean there = d <= FieldBattle.AT_PLACE;
        double wantX = there ? h.faceX() : dx / d;
        double wantZ = there ? h.faceZ() : dz / d;
        boolean astern = !there && wantX * me.dirX() + wantZ * me.dirZ() < 0 && d <= FieldBattle.ASTERN_WITHIN;
        if (astern) {
            wantX = -wantX; // its stern toward it
            wantZ = -wantZ;
        }
        Vector3d away = crowding(me, h, sailing);
        if (!there && !astern && away.lengthSquared() > 0) {
            wantX += away.x; // going ahead, it turns away from those of its own side it would foul
            wantZ += away.z;
        }
        double off = wrap(Math.atan2(wantX, wantZ) - heading);
        double pace = there ? 0 : Math.min(astern ? h.speed() / 2 : h.speed(), d * PULL) * Math.max(FieldBattle.MIN_WAY, Math.cos(off));
        pace = astern ? -pace : pace;
        // Drops back from one of its own side ahead of it, goes on from one astern: along its heading, never sideways.
        pace += (away.x * me.dirX() + away.z * me.dirZ()) * SPREAD;
        pace = Math.max(-h.speed() / 2, Math.min(h.speed(), pace));
        double turn = Math.max(-FieldBattle.TURN_RATE, Math.min(FieldBattle.TURN_RATE, off)) / SableShips.TURN;
        Vector3d want = new Vector3d(me.dirX() * pace, waterline, me.dirZ() * pace);
        if (h.hooked().stream().anyMatch(sailing::containsKey) && d > 1e-6) {
            // Its grapple lines haul it toward its place alongside, any way they pull, sideways too.
            double haul = Math.min(FieldBattle.HAUL_SPEED, d * PULL);
            want.add(dx / d * haul, 0, dz / d * haul);
        }
        body.addLinearAndAngularVelocity(want.sub(velocity), SableShips.spinToward(pose, heading + turn, spin, ride.slope()));
    }

    /**
     * Where ship {@code h} rides the swell: under its own middle, or, grappled, under the middle of every ship lashed
     * to it and to those (so they heave, roll and pitch as one).
     */
    private static double[] grappledMiddle(SableLink.Helm h, Map<Integer, SableLink.Helm> sailing) {
        Set<Integer> group = new HashSet<>(Set.of(h.id()));
        List<SableLink.Helm> todo = new ArrayList<>(List.of(h));
        while (!todo.isEmpty()) {
            for (int id : todo.remove(todo.size() - 1).lashed()) {
                SableLink.Helm o = sailing.get(id);
                if (o != null && group.add(id)) {
                    todo.add(o);
                }
            }
        }
        double x = 0;
        double z = 0;
        for (int id : group) {
            Ramming.Hull hull = hulls.get(id);
            x += hull.x();
            z += hull.z();
        }
        return new double[]{x / group.size(), z / group.size()};
    }

    /** An angle brought to between -pi and pi. */
    private static double wrap(double angle) {
        return Math.atan2(Math.sin(angle), Math.cos(angle));
    }

    /**
     * Away from each ship of its own side whose hull, with room round it, overlaps its own: the sum of the ways from
     * each of them to it (side by side and centred alike, abeam, the first of them one way and the other the other).
     */
    private static Vector3d crowding(Ramming.Hull me, SableLink.Helm h, Map<Integer, SableLink.Helm> sailing) {
        Vector3d away = new Vector3d();
        for (SableLink.Helm o : sailing.values()) {
            if (o.id() == h.id() || o.side() != h.side()) {
                continue;
            }
            Ramming.Hull other = hulls.get(o.id());
            if (!Ramming.overlap(me, other, ROOM / 2)) {
                continue;
            }
            double ox = me.x() - other.x();
            double oz = me.z() - other.z();
            double d = Math.hypot(ox, oz);
            double side = h.id() < o.id() ? 1 : -1;
            away.add(d < 1e-6 ? -me.dirZ() * side : ox / d, 0, d < 1e-6 ? me.dirX() * side : oz / d);
        }
        return away;
    }
}
