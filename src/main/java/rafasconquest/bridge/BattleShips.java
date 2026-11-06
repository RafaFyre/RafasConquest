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
import rafasconquest.core.SeaLanes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A battle's real ships: one under each formation afloat near a player (its soldiers ride on its deck), a Sable sub-level
 * of a warship's hull ({@link rafasconquest.style.ShipHulls}), steered every tick as its {@link SableLink.Helm} says and
 * held at its waterline. Ships no helm asks for are taken away. Only {@link SableLink} calls this.
 */
final class BattleShips {
    private static final String TAG = RafasConquest.MOD_ID + ":battle_ship";
    /** How hard a ship is pulled toward where it makes for, per second (capped at its speed). */
    private static final double PULL = 1.5;
    /** Further than this from where its formation is, a ship is moved there at once. */
    private static final double SNAP = 40;

    private static final Map<String, ServerSubLevel> ships = new HashMap<>();

    private BattleShips() {
    }

    static void tick(ServerLevel level, List<SableLink.Helm> helms) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return;
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
        for (SableLink.Helm h : helms) {
            wanted.add(key(h));
            ServerSubLevel sub = ships.get(key(h));
            if (sub == null || sub.isRemoved()) {
                if (level.getGameTime() % 20 == h.id() % 20) { // a try a second each, spread over the ticks
                    build(level, h);
                }
            } else {
                steer(sub, h);
            }
        }
        for (Map.Entry<String, ServerSubLevel> e : new HashMap<>(ships).entrySet()) {
            if (!wanted.contains(e.getKey())) {
                ships.remove(e.getKey());
                if (!e.getValue().isRemoved()) {
                    container.removeSubLevel(e.getValue(), SubLevelRemovalReason.REMOVED);
                }
            }
        }
    }

    private static String key(SableLink.Helm h) {
        return "battle-" + h.id();
    }

    private static void build(ServerLevel level, SableLink.Helm h) {
        double length = Math.max(1e-6, Math.hypot(h.faceX(), h.faceZ()));
        SeaLanes.Ship hull = new SeaLanes.Ship(key(h), h.x(), h.z(), h.faceX() / length, h.faceZ() / length, 0, 0, true,
                h.flagship() ? SeaLanes.Size.MEDIUM : SeaLanes.Size.SMALL, "", "");
        ServerSubLevel sub = SableShips.assemble(level, hull, TAG);
        if (sub == null) {
            return;
        }
        ships.put(key(h), sub);
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body != null && body.isValid()) {
            body.teleport(new Vector3d(h.x(), sub.getUserDataTag().getDouble("y"), h.z()), new Quaterniond().rotationY(hull.yaw()));
        }
    }

    /** Sails for where its helm makes for, no faster than its speed, bow along its course; at its waterline. */
    private static void steer(ServerSubLevel sub, SableLink.Helm h) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return;
        }
        Pose3dc pose = sub.logicalPose();
        double y = sub.getUserDataTag().getDouble("y");
        Vector3d velocity = body.getLinearVelocity(new Vector3d());
        Vector3d spin = body.getAngularVelocity(new Vector3d());
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
        double yaw = d > 2 ? Math.atan2(dx, dz) : Math.atan2(h.faceX(), h.faceZ());
        body.addLinearAndAngularVelocity(want.sub(velocity), SableShips.spinToward(pose, yaw, spin));
    }
}
