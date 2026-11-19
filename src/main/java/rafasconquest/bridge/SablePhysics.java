package rafasconquest.bridge;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Where Sable's server time goes, for the battle report: its native pre-tick (buoyancy, ropes, joints) and its physics
 * steps, timed by {@code SablePhysicsTimingMixin}, and the bodies it moves, biggest first.
 */
public final class SablePhysics {
    /** Nanoseconds since the last report, on the server thread. */
    public static long buoyancyNanos;
    public static long stepNanos;

    private SablePhysics() {
    }

    /** "sable B ms buoyancy, S ms steps a tick; N bodies: name WxHxL d deep, ..." over {@code ticks}; "" with no bodies. Clears the times. */
    static String report(ServerLevel level, int ticks) {
        double buoyancy = ticks == 0 ? 0 : buoyancyNanos / 1e6 / ticks;
        double steps = ticks == 0 ? 0 : stepNanos / 1e6 / ticks;
        buoyancyNanos = 0;
        stepNanos = 0;
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return "";
        }
        List<SubLevel> bodies = new ArrayList<>();
        for (SubLevel sub : container.getAllSubLevels()) {
            if (!sub.isRemoved()) {
                bodies.add(sub);
            }
        }
        if (bodies.isEmpty()) {
            return "";
        }
        bodies.sort(Comparator.comparingLong(SablePhysics::volume).reversed());
        StringBuilder out = new StringBuilder(String.format(Locale.ROOT, "sable %.1f ms buoyancy, %.1f ms steps a tick; %d bodies:",
                buoyancy, steps, bodies.size()));
        for (SubLevel sub : bodies.subList(0, Math.min(6, bodies.size()))) {
            BoundingBox3ic box = sub.getPlot().getBoundingBox();
            // its keel's depth below the sea: how much of it Sable floats block by block
            double keel = sub.logicalPose().transformPosition(new Vector3d((box.minX() + box.maxX() + 1) / 2.0, box.minY(),
                    (box.minZ() + box.maxZ() + 1) / 2.0)).y;
            out.append(String.format(Locale.ROOT, " %s %dx%dx%d %.1f deep,", sub.getName(), box.maxX() - box.minX() + 1,
                    box.maxY() - box.minY() + 1, box.maxZ() - box.minZ() + 1, level.getSeaLevel() - keel));
        }
        out.setLength(out.length() - 1);
        return out.toString();
    }

    private static long volume(SubLevel sub) {
        BoundingBox3ic box = sub.getPlot().getBoundingBox();
        return (long) (box.maxX() - box.minX() + 1) * (box.maxY() - box.minY() + 1) * (box.maxZ() - box.minZ() + 1);
    }
}
