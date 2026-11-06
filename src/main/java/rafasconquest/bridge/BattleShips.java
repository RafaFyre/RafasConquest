package rafasconquest.bridge;

import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.battle.Ramming;
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
    /** Blocks a second to keep clear of a ship of its own side, and the room kept round each hull. */
    private static final double SPREAD = 3;
    private static final double ROOM = 3;

    private static final Map<String, ServerSubLevel> ships = new HashMap<>();
    /** Each ship's footprint this tick, by formation id. */
    private static final Map<Integer, Ramming.Hull> hulls = new HashMap<>();
    /** How far apart two grappled ships' centres were when they grappled, by the pair. */
    private static final Map<Long, Double> lashGaps = new HashMap<>();

    private BattleShips() {
    }

    static List<SableLink.Ram> tick(ServerLevel level, List<SableLink.Helm> helms) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return List.of();
        }
        if (level.getGameTime() % 20 == 0 || helms.isEmpty()) {
            ships.clear();
            for (ServerSubLevel sub : container.getAllSubLevels()) {
                CompoundTag tag = sub.getUserDataTag();
                if (tag != null && tag.contains(TAG) && !sub.isRemoved()) {
                    ships.put(tag.getString(TAG), sub);
                }
            }
        }
        Set<String> wanted = new HashSet<>();
        Map<Integer, SableLink.Helm> sailing = new HashMap<>();
        hulls.clear();
        for (SableLink.Helm h : helms) {
            wanted.add(key(h));
            ServerSubLevel sub = ships.get(key(h));
            if (sub == null || sub.isRemoved()) {
                if (level.getGameTime() % 20 == h.id() % 20) { // a try a second each, spread over the ticks
                    build(level, h);
                }
            } else {
                Ramming.Hull hull = hull(sub, h);
                if (hull != null) {
                    hulls.put(h.id(), hull);
                    sailing.put(h.id(), h);
                }
            }
        }
        List<SableLink.Ram> rams = new ArrayList<>();
        for (SableLink.Helm a : sailing.values()) {
            for (SableLink.Helm b : sailing.values()) {
                if (a.side() == b.side() || a.lashed().contains(b.id())) {
                    continue;
                }
                double closing = Ramming.closing(hulls.get(a.id()), hulls.get(b.id()));
                if (closing > 0) {
                    double[] knock = Ramming.knock(hulls.get(a.id()), hulls.get(b.id()), closing);
                    rams.add(new SableLink.Ram(a.id(), b.id(), knock[0], knock[1]));
                }
            }
        }
        lashGaps.keySet().removeIf(pair -> {
            SableLink.Helm a = sailing.get((int) (pair >> 32));
            return a == null || !a.lashed().contains((int) (long) pair);
        });
        for (SableLink.Helm h : sailing.values()) {
            steer(ships.get(key(h)), h, sailing);
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

    /** Where formation {@code id}'s ship is and which way its bow points: {x, z, dirX, dirZ}; null with no ship sailing. */
    @org.jetbrains.annotations.Nullable
    static double[] where(int id) {
        Ramming.Hull h = hulls.get(id);
        return h == null ? null : new double[]{h.x(), h.z(), h.dirX(), h.dirZ()};
    }

    private static String key(SableLink.Helm h) {
        return "battle-" + h.id();
    }

    private static SeaLanes.Size size(SableLink.Helm h) {
        return h.flagship() ? SeaLanes.Size.XLARGE : SeaLanes.Size.LARGE;
    }

    private static void build(ServerLevel level, SableLink.Helm h) {
        double length = Math.max(1e-6, Math.hypot(h.faceX(), h.faceZ()));
        SeaLanes.Ship hull = new SeaLanes.Ship(key(h), h.x(), h.z(), h.faceX() / length, h.faceZ() / length, 0, 0, true,
                size(h), "", "");
        ServerSubLevel sub = SableShips.assemble(level, hull, TAG, ShipHulls.battle(size(h), h.cargo()));
        if (sub == null) {
            return;
        }
        ships.put(key(h), sub);
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body != null && body.isValid()) {
            body.teleport(new Vector3d(h.x(), sub.getUserDataTag().getDouble("y"), h.z()), new Quaterniond().rotationY(hull.yaw()));
        }
    }

    /** The ship's footprint now: where it is, its bow's heading, its size and velocity. */
    @org.jetbrains.annotations.Nullable
    private static Ramming.Hull hull(ServerSubLevel sub, SableLink.Helm h) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return null;
        }
        Pose3dc pose = sub.logicalPose();
        Vector3d bow = new Quaterniond(pose.orientation()).transform(new Vector3d(0, 0, 1));
        double flat = Math.max(1e-6, Math.hypot(bow.x, bow.z));
        Vector3d v = body.getLinearVelocity(new Vector3d());
        SeaLanes.Size size = size(h);
        return new Ramming.Hull(pose.position().x(), pose.position().z(), bow.x / flat, bow.z / flat, size.length / 2.0,
                size.width / 2.0, v.x, v.z);
    }

    /**
     * Sails for where its helm makes for, no faster than its speed, bow along its course, at its waterline, edging away
     * from ships of its own side it would foul. Grappled, it sails with the ship it is grappled to instead, pulled in to
     * keep the gap there was between them when they grappled, and doesn't turn.
     */
    private static void steer(ServerSubLevel sub, SableLink.Helm h, Map<Integer, SableLink.Helm> sailing) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return;
        }
        Pose3dc pose = sub.logicalPose();
        double y = sub.getUserDataTag().getDouble("y");
        Vector3d velocity = body.getLinearVelocity(new Vector3d());
        Vector3d spin = body.getAngularVelocity(new Vector3d());
        Ramming.Hull me = hulls.get(h.id());
        Integer partner = h.lashed().stream().filter(sailing::containsKey).findFirst().orElse(null);
        if (partner != null) {
            Ramming.Hull other = hulls.get(partner);
            double dx = other.x() - me.x();
            double dz = other.z() - me.z();
            double d = Math.max(1e-6, Math.hypot(dx, dz));
            double gap = lashGaps.computeIfAbsent(((long) Math.min(h.id(), partner) << 32) | Math.max(h.id(), partner), k -> d);
            double pull = Math.max(-h.speed(), Math.min(h.speed(), (d - gap) * PULL));
            Vector3d want = new Vector3d(other.vx() + dx / d * pull, (y - pose.position().y()) * PULL, other.vz() + dz / d * pull);
            body.addLinearAndAngularVelocity(want.sub(velocity), SableShips.spinToward(pose, Math.atan2(me.dirX(), me.dirZ()), spin));
            return;
        }
        if (Math.hypot(pose.position().x() - h.x(), pose.position().z() - h.z()) > SNAP) {
            body.teleport(new Vector3d(h.x(), y, h.z()), new Quaterniond(pose.orientation()));
            body.addLinearAndAngularVelocity(velocity.negate(), spin.negate());
            return;
        }
        double dx = h.toX() - pose.position().x();
        double dz = h.toZ() - pose.position().z();
        double d = Math.hypot(dx, dz);
        double pace = Math.min(h.speed(), d * PULL);
        Vector3d want = new Vector3d(d < 1e-6 ? 0 : dx / d * pace, (y - pose.position().y()) * PULL, d < 1e-6 ? 0 : dz / d * pace);
        want.add(spread(me, h, sailing));
        double yaw = d > 2 ? Math.atan2(dx, dz) : Math.atan2(h.faceX(), h.faceZ());
        body.addLinearAndAngularVelocity(want.sub(velocity), SableShips.spinToward(pose, yaw, spin));
    }

    /** Away from each ship of its own side whose hull, with room round it, overlaps its own: mostly abeam, to spread the line. */
    private static Vector3d spread(Ramming.Hull me, SableLink.Helm h, Map<Integer, SableLink.Helm> sailing) {
        Vector3d away = new Vector3d();
        double rx = -me.dirZ();
        double rz = me.dirX();
        for (SableLink.Helm o : sailing.values()) {
            if (o.id() == h.id() || o.side() != h.side()) {
                continue;
            }
            Ramming.Hull other = hulls.get(o.id());
            double ox = me.x() - other.x();
            double oz = me.z() - other.z();
            double along = ox * me.dirX() + oz * me.dirZ();
            double across = ox * rx + oz * rz;
            double reachAlong = me.halfLength() + other.halfLength() + ROOM;
            double reachAcross = me.halfBeam() + other.halfBeam() + ROOM;
            if (Math.abs(along) < reachAlong && Math.abs(across) < reachAcross) {
                double side = across != 0 ? Math.signum(across) : (h.id() < o.id() ? 1 : -1);
                double strength = SPREAD * (1 - Math.abs(across) / reachAcross);
                away.add(rx * side * strength, 0, rz * side * strength);
            }
        }
        return away;
    }
}
