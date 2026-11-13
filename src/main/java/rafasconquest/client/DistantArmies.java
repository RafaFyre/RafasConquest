package rafasconquest.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.FormationSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The battle's formations as the server last described them, for drawing beyond the range at which soldiers are seen.
 * Snapshots come once a second; each formation glides from where it was drawn to its newest place over the next
 * second, so it moves smoothly (a second behind the soldiers themselves). Client thread only.
 */
public final class DistantArmies {
    /** How long a formation takes to glide to its newest place: the time between snapshots. */
    private static final double GLIDE_MILLIS = 1000;

    private static Map<Integer, FormationSnapshot> from = new HashMap<>();
    private static Map<Integer, FormationSnapshot> to = new HashMap<>();
    /** Entity ids of every soldier of the battle, as of the last snapshot. */
    private static Set<Integer> soldiers = new HashSet<>();
    private static long receivedAt;

    private DistantArmies() {
    }

    /** What each formation's soldiers wear, by formation id, as of the last snapshot. */
    private static Map<Integer, FigureGear> gear = new HashMap<>();

    public static void receive(List<FormationSnapshot> formations, Map<Integer, int[]> kits) {
        Map<int[], FigureGear> resolved = new java.util.IdentityHashMap<>(); // formations of one kit share its list
        Map<Integer, FigureGear> worn = new HashMap<>();
        kits.forEach((id, kit) -> {
            FigureGear g = resolved.computeIfAbsent(kit, FigureGear::of);
            if (g != null) {
                worn.put(id, g);
            }
        });
        gear = worn;
        Map<Integer, FormationSnapshot> drawn = new HashMap<>();
        now().forEach(f -> drawn.put(f.id(), f));
        from = drawn;
        to = new HashMap<>();
        soldiers = new HashSet<>();
        for (FormationSnapshot f : formations) {
            to.put(f.id(), f);
            for (int id : f.soldierIds()) {
                soldiers.add(id);
            }
        }
        receivedAt = Util.getMillis();
    }

    /** Every formation, where it should be drawn right now. */
    public static List<FormationSnapshot> now() {
        List<FormationSnapshot> formations = new ArrayList<>(to.size());
        double t = Math.min(1, (Util.getMillis() - receivedAt) / GLIDE_MILLIS);
        for (FormationSnapshot target : to.values()) {
            FormationSnapshot start = from.get(target.id());
            formations.add(start == null ? target : target.from(start, t));
        }
        return formations;
    }

    /** The latest snapshot of a formation, as the server last told of it (no gliding); null if there is none by that id. */
    @Nullable
    public static FormationSnapshot target(int id) {
        return to.get(id);
    }

    /** One formation, where it should be drawn right now; null if there is none by that id. */
    @Nullable
    public static FormationSnapshot at(int id) {
        FormationSnapshot target = to.get(id);
        if (target == null) {
            return null;
        }
        FormationSnapshot start = from.get(id);
        return start == null ? target : target.from(start, Math.min(1, (Util.getMillis() - receivedAt) / GLIDE_MILLIS));
    }

    /** How fast a formation is moving, in blocks a second: from where it was to where it is going. */
    public static double speed(FormationSnapshot formation) {
        FormationSnapshot start = from.get(formation.id());
        FormationSnapshot target = to.get(formation.id());
        if (start == null || target == null) {
            return 0;
        }
        return Math.hypot(target.x() - start.x(), target.z() - start.z()) * 1000 / GLIDE_MILLIS;
    }

    /** What a formation's soldiers wear; null if the server hasn't said (its figures wear the army's colour). */
    @Nullable
    static FigureGear gear(int id) {
        return gear.get(id);
    }

    /** Whether this entity is one of the battle's soldiers, or the horse under one. */
    public static boolean isOurs(Entity entity) {
        if (soldiers.contains(entity.getId())) {
            return true;
        }
        for (Entity rider : entity.getPassengers()) {
            if (soldiers.contains(rider.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Where a soldier's feet are drawn this frame, if the client has it alive: on the ground under its horse for a
     * rider, as the server measures it too. A horse and its rider share the spot, so they hand over together.
     */
    @Nullable
    public static Vec3 feet(@Nullable Entity entity, float partialTick) {
        if (entity == null || !entity.isAlive()) {
            return null;
        }
        Entity ground = entity.getVehicle() != null ? entity.getVehicle() : entity;
        return ground.getPosition(partialTick);
    }

    /** Where the client has each soldier this frame, for {@link FormationSnapshot#place}. */
    public static FormationSnapshot.Positions positions(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        return id -> {
            Vec3 at = mc.level == null ? null : feet(mc.level.getEntity(id), partialTick);
            return at == null ? null : new double[]{at.x, at.y, at.z};
        };
    }

    public static void clear() {
        from = new HashMap<>();
        to = new HashMap<>();
        soldiers = new HashSet<>();
        gear = new HashMap<>();
        FigureGear.clear();
        RecruitFigures.clear();
    }
}
