package rafasconquest.client;

import dev.ryanhcode.sable.api.sublevel.ClientSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3dc;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.SableLink;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * The real Sable ships this client has been sent (the server sends only those within its view distance), so a battle's
 * ships are drawn as stand-ins exactly where the client has no real one ({@link DistantShips#battle}).
 */
final class SableSeen {
    private static boolean broken;

    private SableSeen() {
    }

    /**
     * Where each Sable sub-level the client has is and how it lies: {x, z, yaw, fore, port}, the yaw a turn about the up
     * axis taking +z to its bow (as {@code Quaterniond.rotationY} turns), fore and port its deck's rise per block toward
     * the bow and toward its +x side, keel its keel's height at its middle; by its Sable id; none without Sable. Each as
     * drawn this frame, so the figures on its deck ride the hull Sable draws.
     */
    static Map<UUID, double[]> ships(ClientLevel level, float partialTick) {
        if (broken || !SableLink.available()) {
            return Map.of();
        }
        try {
            ClientSubLevelContainer container = SubLevelContainer.getContainer(level);
            if (container == null) {
                return Map.of();
            }
            Map<UUID, double[]> ships = new LinkedHashMap<>();
            for (ClientSubLevel sub : container.getAllSubLevels()) {
                // Not until its blocks have come: till then Sable draws nothing there, and the stand-in must stay.
                if (!sub.isRemoved() && columns(level, sub) != null) {
                    // Not its logical pose, nor its box: those are a tick off the pose it is drawn at.
                    Pose3dc pose = sub.renderPose(partialTick);
                    Vector3d bow = new Quaterniond(pose.orientation()).transform(new Vector3d(0, 0, 1));
                    Vector3d port = new Quaterniond(pose.orientation()).transform(new Vector3d(1, 0, 0));
                    // The middle of its blocks at its keel, as the server holds it (BattleShips.middle): not its pose's
                    // position, the point it turns about (its centre of mass).
                    BoundingBox3ic plot = sub.getPlot().getBoundingBox();
                    Vector3d keel = pose.transformPosition(new Vector3d((plot.minX() + plot.maxX() + 1) / 2.0, plot.minY(),
                            (plot.minZ() + plot.maxZ() + 1) / 2.0));
                    ships.put(sub.getUniqueId(), new double[]{keel.x, keel.z, Math.atan2(bow.x, bow.z),
                            bow.y / Math.max(1e-6, Math.hypot(bow.x, bow.z)), port.y / Math.max(1e-6, Math.hypot(port.x, port.z)), keel.y});
                }
            }
            return ships;
        } catch (LinkageError e) {
            broken = true;
            RafasConquest.LOGGER.warn("Sable's client API doesn't match; every battle ship will be drawn as a stand-in", e);
            return Map.of();
        }
    }

    /** Which of a ship's plot columns (x, z off its box's corner) have a solid block, read again every five seconds. */
    private record Columns(int minX, int minZ, int maxX, int maxZ, int minY, int maxY, boolean[] solid, long readAt) {
        boolean same(BoundingBox3ic b) {
            return b.minX() == minX && b.minZ() == minZ && b.maxX() == maxX && b.maxZ() == maxZ && b.minY() == minY && b.maxY() == maxY;
        }
    }

    private static final Map<ClientSubLevel, Columns> COLUMNS = new WeakHashMap<>();

    /**
     * Sets to 1 each cell of the {@code size}² grid, corner (x0, z0), one cell a block, over which a Sable ship's blocks
     * stand, the ship lying as drawn at this partial tick and cut level at height {@code y}; so the sea isn't drawn in it.
     */
    static void hulls(ClientLevel level, float partial, int x0, int z0, int size, double y, byte[] mask) {
        if (broken || !SableLink.available()) {
            return;
        }
        try {
            ClientSubLevelContainer container = SubLevelContainer.getContainer(level);
            if (container == null) {
                return;
            }
            Vector3d p = new Vector3d();
            for (ClientSubLevel sub : container.getAllSubLevels()) {
                if (sub.isRemoved()) {
                    continue;
                }
                Columns c = columns(level, sub);
                if (c == null) {
                    continue;
                }
                Pose3dc pose = sub.renderPose(partial);
                BoundingBox3dc world = sub.boundingBox();
                int width = c.maxX - c.minX + 1;
                // the box is a tick behind the drawn pose, so two blocks spare
                int ax = Math.max(0, (int) Math.floor(world.minX()) - 2 - x0);
                int bx = Math.min(size - 1, (int) Math.ceil(world.maxX()) + 2 - x0);
                int az = Math.max(0, (int) Math.floor(world.minZ()) - 2 - z0);
                int bz = Math.min(size - 1, (int) Math.ceil(world.maxZ()) + 2 - z0);
                for (int tz = az; tz <= bz; tz++) {
                    for (int tx = ax; tx <= bx; tx++) {
                        pose.transformPositionInverse(p.set(x0 + tx + 0.5, y, z0 + tz + 0.5));
                        int ix = (int) Math.floor(p.x) - c.minX;
                        int iz = (int) Math.floor(p.z) - c.minZ;
                        if (ix >= 0 && ix < width && iz >= 0 && iz <= c.maxZ - c.minZ && c.solid[iz * width + ix]) {
                            mask[tz * size + tx] = 1;
                        }
                    }
                }
            }
        } catch (LinkageError e) {
            broken = true;
            RafasConquest.LOGGER.warn("Sable's client API doesn't match; the sea is drawn inside ships", e);
        }
    }

    private static Columns columns(ClientLevel level, ClientSubLevel sub) {
        BoundingBox3ic b = sub.getPlot().getBoundingBox();
        long now = level.getGameTime();
        Columns c = COLUMNS.get(sub);
        if (c != null && c.same(b) && now - c.readAt < 100) {
            return c;
        }
        int width = b.maxX() - b.minX() + 1;
        int depth = b.maxZ() - b.minZ() + 1;
        if (width <= 0 || depth <= 0 || width * depth > 256 * 256) {
            return null;
        }
        boolean[] solid = new boolean[width * depth];
        boolean any = false;
        BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                for (int yy = b.minY(); yy <= b.maxY() && !solid[z * width + x]; yy++) {
                    BlockState s = level.getBlockState(at.set(b.minX() + x, yy, b.minZ() + z));
                    solid[z * width + x] = !s.isAir() && !s.liquid();
                }
                any |= solid[z * width + x];
            }
        }
        if (!any) {
            return c; // its chunks haven't come yet: the old read (if any), asked again next frame
        }
        c = new Columns(b.minX(), b.minZ(), b.maxX(), b.maxZ(), b.minY(), b.maxY(), solid, now);
        COLUMNS.put(sub, c);
        return c;
    }
}
