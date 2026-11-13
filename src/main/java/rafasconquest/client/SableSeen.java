package rafasconquest.client;

import dev.ryanhcode.sable.api.sublevel.ClientSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.SableLink;

import java.util.ArrayList;
import java.util.List;

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
     * the bow and toward its +x side; none without Sable.
     */
    static List<double[]> ships(ClientLevel level) {
        if (broken || !SableLink.available()) {
            return List.of();
        }
        try {
            ClientSubLevelContainer container = SubLevelContainer.getContainer(level);
            if (container == null) {
                return List.of();
            }
            List<double[]> ships = new ArrayList<>();
            for (ClientSubLevel sub : container.getAllSubLevels()) {
                if (!sub.isRemoved()) {
                    Pose3dc pose = sub.logicalPose();
                    Vector3d bow = new Quaterniond(pose.orientation()).transform(new Vector3d(0, 0, 1));
                    Vector3d port = new Quaterniond(pose.orientation()).transform(new Vector3d(1, 0, 0));
                    // The middle of its blocks, not its pose's position (the point it turns about, its centre of mass).
                    Vector3d middle = sub.boundingBox().center();
                    ships.add(new double[]{middle.x, middle.z, Math.atan2(bow.x, bow.z),
                            bow.y / Math.max(1e-6, Math.hypot(bow.x, bow.z)), port.y / Math.max(1e-6, Math.hypot(port.x, port.z))});
                }
            }
            return ships;
        } catch (LinkageError e) {
            broken = true;
            RafasConquest.LOGGER.warn("Sable's client API doesn't match; every battle ship will be drawn as a stand-in", e);
            return List.of();
        }
    }
}
