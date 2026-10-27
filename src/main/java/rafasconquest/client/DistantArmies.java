package rafasconquest.client;

import net.minecraft.Util;
import rafasconquest.battle.FormationSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static long receivedAt;

    private DistantArmies() {
    }

    public static void receive(List<FormationSnapshot> formations) {
        Map<Integer, FormationSnapshot> drawn = new HashMap<>();
        now().forEach(f -> drawn.put(f.id(), f));
        from = drawn;
        to = new HashMap<>();
        formations.forEach(f -> to.put(f.id(), f));
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

    public static void clear() {
        from = new HashMap<>();
        to = new HashMap<>();
    }
}
