package rafasconquest.bridge;

import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import org.joml.AxisAngle4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import rafasconquest.RafasConquest;
import rafasconquest.core.SeaLanes;
import rafasconquest.map.BaseMap;
import rafasconquest.style.ShipHulls;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Real ships on the sea lanes: where a lane's ship ({@link SeaLanes}) comes within a player's view distance, its hull
 * ({@link ShipHulls}) is built on the water and made a Sable sub-level, which is then steered every tick to where the
 * lane says the ship is. When no player is near any more it is taken away again; far ships are only drawn by clients.
 * Sable has no buoyancy, so the steering holds the ship at its waterline too. Only {@link SableLink} calls this.
 */
final class SableShips {
    private static final String TAG = RafasConquest.MOD_ID + ":ship";
    /** Ships are taken away this much further out than they are built, so one at the edge isn't built over and over. */
    private static final double KEEP_PAST = 32;
    /** Further than this from where it should be (it was unloaded, or knocked away), a ship is moved there at once. */
    private static final double SNAP = 40;
    /** How hard a ship is pulled back onto its lane and turned to its heading, per second. */
    private static final double PULL = 2;
    static final double TURN = 3;
    /** The pull back to the lane is at most this many blocks a second, so steering clear can win over it. */
    private static final double MAX_PULL = 4;
    /** Blocks a second to steer clear of another ship or of land. */
    private static final double AVOID = 5;

    /** The ships sailing now, by lane ship id; found again from Sable's sub-levels every second. */
    private static final Map<String, ServerSubLevel> live = new HashMap<>();

    private SableShips() {
    }

    static void tick(ServerLevel level) {
        BaseMap map = ConquestRuntime.map();
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (map == null || container == null) {
            return;
        }
        long time = level.getGameTime();
        boolean look = time % 20 == 0;
        if (look) {
            live.clear();
            for (ServerSubLevel sub : container.getAllSubLevels()) {
                CompoundTag tag = sub.getUserDataTag();
                if (tag != null && tag.contains(TAG) && !sub.isRemoved()) {
                    live.put(tag.getString(TAG), sub);
                }
            }
        }
        double range = level.getServer().getPlayerList().getViewDistance() * 16.0;
        Map<String, ServerSubLevel> unclaimed = look ? new HashMap<>(live) : Map.of();
        for (SeaLanes.Ship ship : SeaLanes.at(map.roads().shipping(), time)) {
            ServerSubLevel sub = live.get(ship.id());
            if (look) {
                unclaimed.remove(ship.id());
            }
            double nearest = nearestPlayer(level, ship);
            if (sub == null || sub.isRemoved()) {
                if (look && nearest <= range) {
                    build(level, ship);
                }
            } else if (nearest > range + KEEP_PAST) {
                remove(container, sub, ship.id());
            } else {
                steer(sub, ship);
            }
        }
        // Ships of lanes that are gone (another world map) sail no more.
        unclaimed.forEach((id, sub) -> remove(container, sub, id));
    }

    private static double nearestPlayer(ServerLevel level, SeaLanes.Ship ship) {
        double best = Double.MAX_VALUE;
        for (ServerPlayer player : level.players()) {
            best = Math.min(best, Math.hypot(player.getX() - ship.x(), player.getZ() - ship.z()));
        }
        return best;
    }

    /** Builds the ship's hull on the water where it is, bow to +z, and hands it to Sable, then turns it to its heading. */
    private static void build(ServerLevel level, SeaLanes.Ship ship) {
        ServerSubLevel sub = assemble(level, ship, TAG, ShipHulls.of(ship));
        if (sub != null) {
            live.put(ship.id(), sub);
            steer(sub, ship);
        }
    }

    /**
     * Builds a ship's hull ({@code parts}) on the water where it is, bow to +z, and hands it to Sable, tagged {@code key} = its id, with
     * its waterline height kept as "y"; null if it can't be built there yet.
     */
    @org.jetbrains.annotations.Nullable
    static ServerSubLevel assemble(ServerLevel level, SeaLanes.Ship ship, String key, List<ShipHulls.Part> parts) {
        int sea = level.getSeaLevel();
        BlockPos centre = BlockPos.containing(ship.x(), sea, ship.z());
        if (!level.hasChunkAt(centre) || !level.getFluidState(centre.below()).is(FluidTags.WATER)) {
            return null; // not loaded yet, or the point lies over land at this block (a lane's smoothing cut a corner)
        }
        List<BlockPos> blocks = new ArrayList<>();
        for (ShipHulls.Part part : parts) {
            BlockPos pos = centre.offset(part.x(), part.y(), part.z());
            if (!level.hasChunkAt(pos) || !level.getBlockState(pos).isAir()) {
                return null; // not loaded, or something stands there (a pier, an island): try again later
            }
            blocks.add(pos);
        }
        for (int i = 0; i < parts.size(); i++) {
            level.setBlock(blocks.get(i), parts.get(i).state(), Block.UPDATE_CLIENTS);
        }
        ServerSubLevel sub = SubLevelAssemblyHelper.assembleBlocks(level, centre, blocks, BoundingBox3i.from(blocks));
        if (sub == null) {
            blocks.forEach(pos -> level.removeBlock(pos, false));
            return null;
        }
        CompoundTag tag = new CompoundTag();
        tag.putString(key, ship.id());
        // It sits a block down in the water, where it was built (its floor on the surface) less its draught.
        tag.putDouble("y", sub.logicalPose().position().y() - 1);
        sub.setUserDataTag(tag);
        sub.setName("Ship " + ship.id());
        return sub;
    }

    /** Turns a ship toward a heading (a turn about the up axis), keeping it upright, by setting its spin. */
    static Vector3d spinToward(Pose3dc pose, double yaw, Vector3d spin) {
        Quaterniond heading = new Quaterniond().rotationY(yaw);
        AxisAngle4d turn = new AxisAngle4d(heading.mul(new Quaterniond(pose.orientation()).invert()));
        double angle = turn.angle > Math.PI ? turn.angle - 2 * Math.PI : turn.angle;
        return new Vector3d(turn.x, turn.y, turn.z).mul(angle * TURN).sub(spin);
    }

    /**
     * Sets the ship's velocity to its pace on the lane plus a pull back to where the lane says it is, at its
     * waterline, and pushes it clear of the other ships and of land seen ahead; its spin turns it to its heading and
     * keeps it upright. Far off, it is moved there at once.
     */
    private static void steer(ServerSubLevel sub, SeaLanes.Ship ship) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return;
        }
        Pose3dc pose = sub.logicalPose();
        CompoundTag tag = sub.getUserDataTag();
        Vector3d target = new Vector3d(ship.x(), tag.getDouble("y"), ship.z());
        Quaterniond heading = new Quaterniond().rotationY(ship.yaw());
        Vector3d off = target.sub(pose.position(), new Vector3d());
        Vector3d velocity = body.getLinearVelocity(new Vector3d());
        Vector3d spin = body.getAngularVelocity(new Vector3d());
        if (off.length() > SNAP) {
            body.teleport(target, heading);
            body.addLinearAndAngularVelocity(velocity.negate(), spin.negate());
            return;
        }
        Vector3d pull = off.mul(PULL);
        if (pull.length() > MAX_PULL) {
            pull.normalize(MAX_PULL);
        }
        Vector3d want = new Vector3d(ship.vx() * 20, 0, ship.vz() * 20).add(pull).add(avoid(sub, pose, ship));
        body.addLinearAndAngularVelocity(want.sub(velocity), spinToward(pose, ship.yaw(), spin));
    }

    /**
     * Blocks a second to steer clear: away from each other ship within reach of its hull, and away from land ahead
     * (towards whichever bow quarter is open water).
     */
    private static Vector3d avoid(ServerSubLevel sub, Pose3dc pose, SeaLanes.Ship ship) {
        Vector3d away = new Vector3d();
        Vector3dc at = pose.position();
        double reach = ship.size().length / 2.0 + 8;
        for (ServerSubLevel other : live.values()) {
            if (other == sub || other.isRemoved()) {
                continue;
            }
            Vector3dc there = other.logicalPose().position();
            double dx = at.x() - there.x();
            double dz = at.z() - there.z();
            double d = Math.hypot(dx, dz);
            double room = reach + SeaLanes.Size.XLARGE.length / 2.0;
            if (d > 1e-6 && d < room) {
                away.add(dx / d * AVOID * (1 - d / room), 0, dz / d * AVOID * (1 - d / room));
            }
        }
        ServerLevel level = (ServerLevel) sub.getLevel();
        double hx = ship.dirX();
        double hz = ship.dirZ();
        double rx = -hz;
        double rz = hx;
        for (double ahead = ship.size().length / 2.0 + 4; ahead <= reach + 16; ahead += 6) {
            if (land(level, at.x() + hx * ahead, at.z() + hz * ahead)) {
                boolean rightOpen = !land(level, at.x() + (hx + rx) * ahead, at.z() + (hz + rz) * ahead);
                double side = rightOpen ? 1 : -1;
                away.add(rx * side * AVOID - hx * AVOID / 2, 0, rz * side * AVOID - hz * AVOID / 2);
                break;
            }
        }
        return away;
    }

    /** Whether there is something solid at the waterline here: a shore, an island, a pier. */
    private static boolean land(ServerLevel level, double x, double z) {
        BlockPos pos = BlockPos.containing(x, level.getSeaLevel() - 1, z);
        if (!level.hasChunkAt(pos)) {
            return false;
        }
        return !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                || !level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty();
    }

    private static void remove(ServerSubLevelContainer container, ServerSubLevel sub, String id) {
        live.remove(id);
        if (!sub.isRemoved()) {
            container.removeSubLevel(sub, SubLevelRemovalReason.REMOVED);
        }
    }
}
