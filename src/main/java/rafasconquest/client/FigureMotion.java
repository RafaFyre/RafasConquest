package rafasconquest.client;

import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Each distant soldier moves on its own: it walks to its place in its block's ranks at a soldier's pace and turns
 * itself, the way it walks or, standing, toward the enemy, instead of the whole block swinging round its middle when it
 * turns. Blocks in melee flow into each other: the front ranks press on into the enemy block they touch, so the two
 * lines mix, riders deepest. A figure thrown by a charge ({@link ThrownFigures}) lands, then walks back. Client thread
 * only.
 */
final class FigureMotion {
    /** How fast a figure walks to its place, blocks a second: a little faster than a block marches, so it keeps up. */
    static final double FOOT_SPEED = 4.5;
    static final double HORSE_SPEED = 11;
    /** Further behind its place than this, a figure hurries, faster the further behind. */
    static final double CATCH_UP = 3;
    /** Further than this from its place (just seen, or the block moved a long way at once), a figure is put there. */
    static final double SNAP = 40;
    /** How fast a figure turns, radians a second. */
    static final double TURN = Math.toRadians(300);
    /** Slower than this (blocks a second), a figure is standing, and faces its foe or its block's front. */
    static final double MOVING = 1.2;
    /** How far past the gap between two blocks in melee their front ranks press into each other; riders further. */
    static final double MIX = 1.5;
    static final double HORSE_MIX = 3.5;
    /** Two blocks this close front to front (or closer) are at each other. */
    static final double REACH = BattlePlan.FRONT_CONTACT + 4;
    /** How far a figure in melee sways about its place as the fight surges. */
    static final double SWAY = 0.45;
    private static final long FORGET_MILLIS = 5000;

    /** Where to draw a figure and which way it faces, a unit vector. */
    record Placed(double x, double y, double z, double fx, double fz, boolean leader, boolean real) {
    }

    /** One figure's own position (flat) and heading. */
    static final class State {
        double x;
        double z;
        double fx;
        double fz;
        long seen;

        State(double x, double z, double fx, double fz) {
            this.x = x;
            this.z = z;
            this.fx = fx;
            this.fz = fz;
        }
    }

    private static final Map<Long, State> STATES = new HashMap<>();
    private static long last;
    private static long pruned;

    private FigureMotion() {
    }

    /** Moves every figure on to {@code now} and says where each is, a list per formation in the order given. */
    static List<List<Placed>> step(List<FormationSnapshot> formations, FormationSnapshot.Positions positions, long now) {
        double dt = last == 0 ? 0 : Math.min(0.1, Math.max(0, (now - last) / 1000.0));
        last = now;
        List<List<Placed>> all = new ArrayList<>(formations.size());
        for (FormationSnapshot f : formations) {
            all.add(step(f, foe(f, formations), positions, now, dt));
        }
        if (now - pruned > 1000) {
            pruned = now;
            STATES.values().removeIf(s -> now - s.seen > FORGET_MILLIS);
        }
        return all;
    }

    private static List<Placed> step(FormationSnapshot f, FormationSnapshot foe, FormationSnapshot.Positions positions, long now, double dt) {
        List<Sprite> sprites = f.place(positions);
        List<Placed> placed = new ArrayList<>(sprites.size());
        boolean horse = f.kind().mounted();
        // Toward the foe, and how far the front rank presses on: half the gap between the fronts, and then some.
        double sx = 0;
        double sz = 0;
        double surge = 0;
        if (foe != null) {
            double dx = foe.x() - f.x();
            double dz = foe.z() - f.z();
            double length = Math.hypot(dx, dz);
            sx = dx / length;
            sz = dz / length;
            surge = Math.max(0, length - f.halfDepth() - foe.halfDepth()) / 2 + (horse ? HORSE_MIX : MIX);
        }
        double depth = Math.max(f.halfDepth(), 0.5);
        for (int i = 0; i < sprites.size(); i++) {
            Sprite s = sprites.get(i);
            double tx = s.x();
            double tz = s.z();
            if (f.fighting() && !s.real()) {
                if (foe != null) {
                    // The front rank presses on furthest; the rear ranks follow less far.
                    double back = -((s.x() - f.x()) * sx + (s.z() - f.z()) * sz);
                    double rank = Math.min(1, Math.max(0, (back + depth) / (2 * depth)));
                    tx += sx * surge * (1 - 0.7 * rank);
                    tz += sz * surge * (1 - 0.7 * rank);
                }
                double phase = (f.id() * 31 + i * 17) % 628 / 100.0;
                double t = now / 1000.0;
                tx += Math.sin(t * 1.3 + phase) * SWAY;
                tz += Math.cos(t * 1.1 + phase * 1.7) * SWAY;
            }
            double faceX = foe != null ? sx : f.facingX();
            double faceZ = foe != null ? sz : f.facingZ();
            State state = STATES.get(key(f.id(), i));
            if (state == null) {
                state = new State(tx, tz, faceX, faceZ);
                STATES.put(key(f.id(), i), state);
            }
            double[] thrown = ThrownFigures.offset(f.id(), i, now);
            if (thrown != null) {
                // In the air: carried along its flight from its place, landing where the flight ends, then walking back.
                state.x = tx + thrown[0];
                state.z = tz + thrown[2];
            } else {
                move(state, tx, tz, faceX, faceZ, s.real(), horse ? HORSE_SPEED : FOOT_SPEED, dt);
            }
            state.seen = now;
            placed.add(new Placed(state.x, s.y() + (thrown == null ? 0 : thrown[1]), state.z, state.fx, state.fz, s.leader(), s.real()));
        }
        return placed;
    }

    /**
     * Moves a figure toward its place ({@code tx}, {@code tz}) by up to {@code speed} blocks a second (more when far
     * behind; a real soldier is simply where it stands), and turns it, by up to {@link #TURN} a second, the way it
     * goes, or, standing, toward ({@code faceX}, {@code faceZ}).
     */
    static void move(State s, double tx, double tz, double faceX, double faceZ, boolean real, double speed, double dt) {
        double dx = tx - s.x;
        double dz = tz - s.z;
        double distance = Math.hypot(dx, dz);
        if (distance > SNAP) {
            s.x = tx;
            s.z = tz;
            s.fx = faceX;
            s.fz = faceZ;
            return;
        }
        double step = real ? distance : Math.min(distance, speed * dt * Math.max(1, distance / CATCH_UP));
        if (distance > 1e-9) {
            s.x += dx / distance * step;
            s.z += dz / distance * step;
        }
        double wantX = faceX;
        double wantZ = faceZ;
        if (dt > 0 && step / dt > MOVING) {
            wantX = dx / distance;
            wantZ = dz / distance;
        }
        double now = Math.atan2(s.fz, s.fx);
        double turn = Math.atan2(wantZ, wantX) - now;
        turn = Math.atan2(Math.sin(turn), Math.cos(turn)); // the short way round
        double most = TURN * dt;
        now += Math.max(-most, Math.min(most, turn));
        s.fx = Math.cos(now);
        s.fz = Math.sin(now);
    }

    /**
     * The enemy block a block in melee is at: the nearest of another colour ahead of it, front to front within
     * {@link #REACH}; null if none.
     */
    private static FormationSnapshot foe(FormationSnapshot f, List<FormationSnapshot> all) {
        if (!f.fighting() || f.afloat()) {
            return null;
        }
        FormationSnapshot best = null;
        double nearest = Double.MAX_VALUE;
        for (FormationSnapshot g : all) {
            if (g == f || g.rgb() == f.rgb() || g.afloat() || g.alive() <= 0) {
                continue;
            }
            double dx = g.x() - f.x();
            double dz = g.z() - f.z();
            double length = Math.hypot(dx, dz);
            if (length < 1e-6 || dx * f.facingX() + dz * f.facingZ() <= 0
                    || length - f.halfDepth() - g.halfDepth() > REACH || length >= nearest) {
                continue;
            }
            best = g;
            nearest = length;
        }
        return best;
    }

    private static long key(int id, int index) {
        return (long) id << 32 | index;
    }

    static void clear() {
        STATES.clear();
        last = 0;
    }
}
