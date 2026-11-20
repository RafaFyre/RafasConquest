package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.bridge.Sea;
import rafasconquest.core.Swell;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.core.SeaLanes;
import rafasconquest.map.BaseMap;
import rafasconquest.style.ShipHulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ships the client draws, beyond the real ones: the sea lanes', and a battle's (one under each formation afloat).
 * Going away from the camera, each is first the real Sable ship the server sails, then a level of detail, then a sprite:
 * <ul>
 * <li>near, the real ship, drawn by Sable;</li>
 * <li>from {@link #REAL_WITHIN} (or wherever the client has no real ship), out to {@link #SPRITE_PAST}, its 3D level of
 * detail: a box for each block, laid over the world every frame like a soldier's figure, right where the real ship is
 * and turned as it is;</li>
 * <li>past that, a flat sprite.</li>
 * </ul>
 * Both are drawn by {@link DistantArmyRenderer#renderShips}, instanced like the soldiers.
 */
final class DistantShips {
    static final double SPRITE_PAST = 2000;
    /** A real ship this close to where a ship should be, centre to centre, is that ship. */
    private static final double OWN_SHIP = 16;
    /** Within this many blocks (flat) a ship the client has is always the real Sable ship, drawn in full. */
    static final double REAL_WITHIN = 900;
    /** Whether battle ships have been drawn yet this session (logged once, so a test can tell they are). */
    private static boolean announced;

    private DistantShips() {
    }

    /**
     * A ship to draw: the middle of its hull, its heading (a turn about the up axis taking +z to its bow), its size, and
     * whether the client has the real ship there (and so where it really is). A sea lane's ship has its {@code lane}; a
     * battle's, its formation's {@code battle} ship. It floats as the real one does: its deck rising {@code fore} a block
     * toward the bow and {@code port} toward its left, its waterline {@code lift} off the still sea. Its left and right
     * oars row {@code rowLeft} and {@code rowRight} ({@link OarFigures.Rowing}). It was {@code want}ed where its lane or
     * formation has it, which for a real ship is behind where it really is.
     */
    record Far(double x, double z, double yaw, SeaLanes.Size size, boolean real, @Nullable SeaLanes.Ship lane,
               @Nullable FormationSnapshot.Ship battle, double fore, double port, double lift, float rowLeft, float rowRight,
               @Nullable Want want) {
        boolean warship() {
            return lane == null || lane.warship();
        }

        /** Its hull's blocks, in its own frame, as the real one is built. */
        List<ShipHulls.Part> parts() {
            return lane != null ? laneHull(lane)
                    : ShipHulls.battle(size,DyeColor.byId(battle.sail()), DyeColor.byId(battle.stripe()));
        }

        /** Whether Sable's real ship shows for itself, {@code flat} blocks from the camera: the client has it, within {@link #REAL_WITHIN}. */
        boolean shownReal(double flat) {
            return real && flat < REAL_WITHIN;
        }

        /** How far its deck at world (x, z) stands off where it would on a still sea: its lift, and its pitch and roll there. */
        double deckLift(double x, double z) {
            double dx = x - this.x;
            double dz = z - this.z;
            double sin = Math.sin(yaw);
            double cos = Math.cos(yaw);
            // Into its own frame (as OarFigures shears the hull): +z to the bow, +x to port.
            return lift + (dx * sin + dz * cos) * fore + (dx * cos - dz * sin) * port;
        }

        /**
         * A figure laid out about where this ship was wanted (its formation, which the client has a second or two late:
         * {@link DistantArmies}), moved and turned with the ship onto where it is drawn now, and lifted as its deck lies there.
         */
        FigureMotion.Placed carry(FigureMotion.Placed p) {
            double turn = yaw - want.yaw();
            double sin = Math.sin(turn);
            double cos = Math.cos(turn);
            double dx = p.x() - want.x();
            double dz = p.z() - want.z();
            // Turned as Quaterniond.rotationY turns, +z toward +x.
            double x = this.x + dx * cos + dz * sin;
            double z = this.z - dx * sin + dz * cos;
            return new FigureMotion.Placed(x, p.y() + deckLift(x, z), z, p.fx() * cos + p.fz() * sin, p.fz() * cos - p.fx() * sin,
                    p.leader(), false, p.swing(), p.amount());
        }
    }

    /**
     * Carries the figures of each block afloat on the ship drawn under them ({@link Far#carry}), so they sail, heave,
     * pitch and roll with it rather than trailing it by the formation's lag. Real soldiers already stand on the real deck.
     */
    static void rideDecks(List<FormationSnapshot> formations, List<List<FigureMotion.Placed>> figures, List<Far> ships) {
        for (int f = 0; f < formations.size(); f++) {
            FormationSnapshot formation = formations.get(f);
            Far ship = formation.afloat() ? shipUnder(formation.x(), formation.z(), ships) : null;
            if (ship == null) {
                continue;
            }
            List<FigureMotion.Placed> list = figures.get(f);
            for (int i = 0; i < list.size(); i++) {
                FigureMotion.Placed p = list.get(i);
                if (!p.real()) {
                    list.set(i, ship.carry(p));
                }
            }
        }
    }

    /**
     * The battle ship whose deck (x, z) is on, both where the formations have them, which the real ship may be well
     * ahead of: the nearest within half its length (and a block); null if none.
     */
    @Nullable
    private static Far shipUnder(double x, double z, List<Far> ships) {
        Far best = null;
        double nearest = Double.MAX_VALUE;
        for (Far s : ships) {
            if (s.battle() == null) {
                continue;
            }
            double d = Math.hypot(s.want().x() - x, s.want().z() - z);
            if (d < nearest && d <= s.size().length / 2.0 + 1) {
                best = s;
                nearest = d;
            }
        }
        return best;
    }

    /**
     * A sea-lane ship's hull, kept a while: finding it looks through the style folder on disk, too slow to do for every
     * ship every frame. Forgotten every {@link #HULLS_KEPT_MILLIS}, so an edited ship file still shows. Client thread only.
     */
    private static final Map<String, List<ShipHulls.Part>> LANE_HULLS = new HashMap<>();
    private static final long HULLS_KEPT_MILLIS = 10_000;
    private static long hullsSince;

    private static List<ShipHulls.Part> laneHull(SeaLanes.Ship ship) {
        long now = net.minecraft.Util.getMillis();
        if (now - hullsSince > HULLS_KEPT_MILLIS) {
            hullsSince = now;
            LANE_HULLS.clear();
        }
        return LANE_HULLS.computeIfAbsent(ship.id(), id -> ShipHulls.of(ship));
    }

    /** The sea lanes' ships, where the lanes have them now. */
    static List<SeaLanes.Ship> lanes(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        BaseMap map = ClientMapCache.map();
        if (map == null || mc.level == null) {
            return List.of();
        }
        return SeaLanes.at(map.roads().shipping(), mc.level.getGameTime() + partialTick);
    }

    /** Every ship to draw beyond the real ones, each matched to the real one the client has there, if any. */
    static List<Far> all(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return List.of();
        }
        List<double[]> real = SableSeen.ships(mc.level, partialTick);
        List<Want> wants = new ArrayList<>();
        for (SeaLanes.Ship s : lanes(partialTick)) {
            wants.add(new Want(s.id(), s.x(), s.z(), s.yaw(), s.size(), s, null));
        }
        for (FormationSnapshot f : DistantArmies.now()) {
            if (f.ship() != null && f.ship().hull() >= 0) { // one hull to a ship, drawn by the block sailing it
                wants.add(new Want(f.id(), f.x(), f.z(), Math.atan2(f.facingX(), f.facingZ()),
                        SeaLanes.Size.get(f.ship().hull()), null, f.ship()));
            }
        }
        double[][] owns = match(wants, real);
        List<Far> ships = new ArrayList<>();
        RIDES.values().forEach(r -> r.seen = false);
        net.minecraft.world.phys.Vec3 eye = mc.gameRenderer.getMainCamera().getPosition();
        int battle = 0;
        int battleReal = 0;
        for (int i = 0; i < wants.size(); i++) {
            Want w = wants.get(i);
            Far ship = far(owns[i], eye, w);
            ships.add(ship);
            if (w.battle != null) {
                battle++;
                battleReal += ship.real() ? 1 : 0;
            }
        }
        RIDES.values().removeIf(r -> !r.seen);
        if (!announced && battle > 0) {
            announced = true;
            rafasconquest.RafasConquest.LOGGER.info("Drawing a battle's {} ships beyond the real ones ({} of them real on this client, {} Sable ships in all)",
                    battle, battleReal, real.size());
        }
        return ships;
    }

    /**
     * A ship that should be at (x, z): where the client's real one is and lying as it lies, if it has one there; else
     * where it should be, riding the swell as the server rides the real ones ({@link Swell.Ride}). {@code key} names it
     * from frame to frame (a lane ship's id, a battle formation's).
     */
    record Want(Object key, double x, double z, double yaw, SeaLanes.Size size, @Nullable SeaLanes.Ship lane,
                        @Nullable FormationSnapshot.Ship battle) {
    }

    /**
     * Each wanted ship's real one, or null: nearest pairs first, each real ship given to one ship at most. Else, in a
     * packed fleet, two ships both took one real ship, one of them was hidden with its own real hull elsewhere, and as
     * they drifted the claim swapped back and forth: ships blinking out right by the player.
     */
    static double[][] match(List<Want> wants, List<double[]> real) {
        List<double[]> pairs = new ArrayList<>(); // {distance, want, real}
        for (int i = 0; i < wants.size(); i++) {
            for (int j = 0; j < real.size(); j++) {
                double dx = real.get(j)[0] - wants.get(i).x;
                double dz = real.get(j)[1] - wants.get(i).z;
                double d = dx * dx + dz * dz; // squared: it only ranks the pairs
                if (d <= OWN_SHIP * OWN_SHIP) {
                    pairs.add(new double[]{d, i, j});
                }
            }
        }
        pairs.sort((a, b) -> Double.compare(a[0], b[0]));
        double[][] owns = new double[wants.size()][];
        boolean[] taken = new boolean[real.size()];
        for (double[] p : pairs) {
            int i = (int) p[1];
            int j = (int) p[2];
            if (owns[i] == null && !taken[j]) {
                owns[i] = real.get(j);
                taken[j] = true;
            }
        }
        return owns;
    }

    private static Far far(@Nullable double[] own, net.minecraft.world.phys.Vec3 eye, Want w) {
        double x = own != null ? own[0] : w.x;
        double z = own != null ? own[1] : w.z;
        double yaw = own != null ? own[2] : w.yaw;
        SeaLanes.Size size = w.size;
        SeaLanes.Ship lane = w.lane;
        FormationSnapshot.Ship battle = w.battle;
        // Only a ship drawn as a level of detail shows its float: a sprite is too far, and Sable floats the real one.
        // Floating every lane ship in the world was a sixth of a sea battle's frame.
        double flat = Math.hypot(x - eye.x, z - eye.z);
        // A real ship lies as Sable has it: heaved off where its keel sits on a still sea, so its deck's figures ride with it.
        double heave = own != null ? own[5] - keel() : 0;
        if (flat > SPRITE_PAST || own != null && flat < REAL_WITHIN) {
            return new Far(x, z, yaw, size, own != null, lane, battle, own != null ? own[3] : 0, own != null ? own[4] : 0, heave, 0, 0, w);
        }
        Riding r = ride(w.key, x, z, yaw, size);
        Swell.Ride ride = r.ride;
        return own != null ? new Far(x, z, yaw, size, true, lane, battle, own[3], own[4], heave, r.rowLeft, r.rowRight, w)
                : new Far(x, z, yaw, size, false, lane, battle, ride.fore(), ride.port(), ride.offset(), r.rowLeft, r.rowRight, w);
    }

    /**
     * A far ship's float on the swell, and the game tick it was last ticked to; where it lay then, and how its left and
     * right oars row ({@link OarFigures.Rowing}). Client thread only.
     */
    private static final class Riding {
        final Swell.Ride ride = new Swell.Ride();
        long tick = Long.MIN_VALUE / 2;
        boolean seen;
        double x, z, yaw, forward, turn;
        float rowLeft, rowRight;
    }

    private static final Map<Object, Riding> RIDES = new HashMap<>();
    /** A ship first seen floats this many ticks before it is drawn, enough for its springs to settle. */
    private static final int SETTLE_TICKS = 40;

    // shortcut: ticks once a game tick, so the bob steps at 20 a second; interpolate with partialTick if it shows up close.
    private static Riding ride(Object key, double x, double z, double yaw, SeaLanes.Size size) {
        Minecraft mc = Minecraft.getInstance();
        Riding r = RIDES.computeIfAbsent(key, k -> new Riding());
        r.seen = true;
        long now = mc.level.getGameTime();
        double dirX = Math.sin(yaw);
        double dirZ = Math.cos(yaw);
        var calm = Sea.calm(mc.level);
        for (long t = Math.max(r.tick, now - SETTLE_TICKS) + 1; t <= now; t++) {
            r.ride.tick(x, z, dirX, dirZ, size.length / 2.0, size.width / 2.0, t, calm);
        }
        long ticks = now - r.tick;
        if (ticks > 0 && ticks <= SETTLE_TICKS) { // not on the tick it is first seen: it has no motion yet
            // Smoothed: a battle's ships come in uneven steps.
            r.forward += (((x - r.x) * dirX + (z - r.z) * dirZ) / ticks - r.forward) * 0.3;
            r.turn += (Math.IEEEremainder(yaw - r.yaw, 2 * Math.PI) / ticks - r.turn) * 0.3;
            r.rowLeft = OarFigures.Rowing.ease(r.rowLeft, OarFigures.Rowing.target(r.forward, r.turn, false));
            r.rowRight = OarFigures.Rowing.ease(r.rowRight, OarFigures.Rowing.target(r.forward, r.turn, true));
        }
        if (ticks > 0) {
            r.x = x;
            r.z = z;
            r.yaw = yaw;
        }
        r.tick = now;
        return r;
    }

    /**
     * A hull's blocks as a key for its model, equal for equal hulls, with their hash worked out once: hashing a ship's
     * every block for every ship every frame was a thirtieth of a sea battle's frame.
     */
    private static final class HullKey {
        final List<ShipHulls.Part> parts;
        final int hash;

        HullKey(List<ShipHulls.Part> parts) {
            this.parts = parts;
            this.hash = parts.hashCode();
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof HullKey k && (k.parts == parts || k.hash == hash && k.parts.equals(parts));
        }
    }

    /** Each hull list's key, by the list itself; and the one key kept for each hull, so a look-up meets the very same key. */
    private static final Map<List<ShipHulls.Part>, HullKey> HULL_KEYS = new java.util.IdentityHashMap<>();
    private static final Map<HullKey, HullKey> HULLS_KNOWN = new HashMap<>();

    static Object hullKey(List<ShipHulls.Part> parts) {
        if (HULL_KEYS.size() > 1024) {
            HULL_KEYS.clear(); // shortcut: lane hull lists are made anew every 10 s and the old ones stay here; a weak identity map if this ever shows
        }
        return HULL_KEYS.computeIfAbsent(parts, p -> HULLS_KNOWN.computeIfAbsent(new HullKey(p), k -> k));
    }

    /** Where a ship's keel sits: a block down in the water, as the server holds the real ones. */
    static double keel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? 62 : mc.level.getSeaLevel() - 1;
    }
}
