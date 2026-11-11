package rafasconquest.client;

import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.FormationSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Soldiers thrown by a cavalry charge: each second the server says which cavalry formation tore into which
 * ({@link rafasconquest.net.ConquestNetwork.ChargeHits}), and a few of the struck formation's figures nearest the riders
 * fly 3 to 7 blocks high and land 4 to 10 blocks on, the way the riders were going; {@link FigureMotion} carries each
 * along its flight and walks it back to its place after. Client thread only.
 */
public final class ThrownFigures {
    /** The game's gravity, in blocks a second squared, so a flight lasts as long as its height asks. */
    private static final double GRAVITY = 32;
    /** How far either side of the riders' heading a figure may fly. */
    private static final double SPREAD = Math.toRadians(15);

    /** A figure's flight: where it lands from its place, how high it rises, when it left and how long it flies. */
    private record Throw(double dx, double dz, double rise, long start, long flight) {
    }

    private static final Map<Long, Throw> THROWN = new HashMap<>();
    private static final Random RANDOM = new Random();

    private ThrownFigures() {
    }

    public static void receive(int[] cavalry, int[] struck) {
        long now = Util.getMillis();
        THROWN.values().removeIf(t -> now - t.start() > t.flight());
        FormationSnapshot.Positions positions = DistantArmies.positions(1);
        for (int i = 0; i < cavalry.length; i++) {
            FormationSnapshot riders = DistantArmies.at(cavalry[i]);
            FormationSnapshot foot = DistantArmies.at(struck[i]);
            if (riders == null || foot == null) {
                continue;
            }
            List<FormationSnapshot.Sprite> figures = foot.place(positions);
            List<Integer> nearest = new ArrayList<>();
            for (int k = 0; k < figures.size(); k++) {
                nearest.add(k);
            }
            nearest.sort(Comparator.comparingDouble(k -> Math.hypot(figures.get(k).x() - riders.x(), figures.get(k).z() - riders.z())));
            int count = 1 + RANDOM.nextInt(3);
            for (int k : nearest) {
                if (count == 0) {
                    break;
                }
                long key = key(foot.id(), k);
                if (THROWN.containsKey(key)) {
                    continue;
                }
                double heading = Math.atan2(riders.facingZ(), riders.facingX()) + (RANDOM.nextDouble() * 2 - 1) * SPREAD;
                double distance = 4 + RANDOM.nextDouble() * 6;
                double rise = 3 + RANDOM.nextDouble() * 4;
                long flight = Math.round(2 * Math.sqrt(2 * rise / GRAVITY) * 1000);
                THROWN.put(key, new Throw(Math.cos(heading) * distance, Math.sin(heading) * distance, rise, now, flight));
                count--;
            }
        }
    }

    /** How far figure {@code index} of formation {@code id} is from its place at {@code now}, {x, y, z}; null unless in the air. */
    @Nullable
    static double[] offset(int id, int index, long now) {
        Throw t = THROWN.get(key(id, index));
        if (t == null || now - t.start() > t.flight()) {
            return null;
        }
        double u = (now - t.start()) / (double) t.flight();
        return new double[]{t.dx() * u, 4 * t.rise() * u * (1 - u), t.dz() * u};
    }

    private static long key(int id, int index) {
        return (long) id << 32 | index;
    }

    public static void clear() {
        THROWN.clear();
    }
}
