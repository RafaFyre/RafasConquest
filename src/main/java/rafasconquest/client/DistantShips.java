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
 * <li>from just short of where Distant Horizons starts fading the world into its own terrain (or wherever the client
 * has no real ship), out to {@link #SPRITE_PAST}, its 3D level of detail: a box for each block, laid over the world
 * every frame like a soldier's figure, right where the real ship is and turned as it is, so the real one fades into it
 * (with nothing under it, it would fade into the sea and look like a ghost);</li>
 * <li>past that, a flat sprite.</li>
 * </ul>
 * Both are drawn by {@link DistantArmyRenderer#renderShips}, instanced like the soldiers.
 */
final class DistantShips {
    static final double SPRITE_PAST = 2000;
    /** A real ship this close to where a ship should be, centre to centre, is that ship. */
    private static final double OWN_SHIP = 16;
    /** A real ship's boxes start this much short of the fade, so the fade never shows the sea through its far end. */
    private static final double FADE_MARGIN = 16;
    /** Whether battle ships have been drawn yet this session (logged once, so a test can tell they are). */
    private static boolean announced;

    private DistantShips() {
    }

    /**
     * A ship to draw: the middle of its hull, its heading (a turn about the up axis taking +z to its bow), its size, and
     * whether the client has the real ship there (and so where it really is). A sea lane's ship has its {@code lane}; a
     * battle's, its formation's {@code battle} ship. It floats as the real one does: its deck rising {@code fore} a block
     * toward the bow and {@code port} toward its left, its waterline {@code lift} off the still sea.
     */
    record Far(double x, double z, double yaw, SeaLanes.Size size, boolean real, @Nullable SeaLanes.Ship lane,
               @Nullable FormationSnapshot.Ship battle, double fore, double port, double lift) {
        boolean warship() {
            return lane == null || lane.warship();
        }

        /** Its hull's blocks, in its own frame, as the real one is built. */
        List<ShipHulls.Part> parts() {
            return lane != null ? laneHull(lane)
                    : ShipHulls.battle(size, battle.cargo(), DyeColor.byId(battle.sail()), DyeColor.byId(battle.stripe()));
        }

        /**
         * Whether Sable's real ship shows for itself, {@code flat} blocks from the camera: the client has it, and it is
         * short of where Distant Horizons starts fading the world ({@code fadeStart}, 0 if it isn't).
         */
        boolean shownReal(double flat, double fadeStart) {
            return real && (fadeStart <= 0 || flat < fadeStart - FADE_MARGIN);
        }
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
        List<double[]> real = SableSeen.ships(mc.level);
        List<Far> ships = new ArrayList<>();
        RIDES.values().forEach(r -> r.seen = false);
        for (SeaLanes.Ship s : lanes(partialTick)) {
            ships.add(far(real, s.id(), s.x(), s.z(), s.yaw(), s.size(), s, null));
        }
        int battle = 0;
        int battleReal = 0;
        for (FormationSnapshot f : DistantArmies.now()) {
            if (f.ship() != null && f.ship().hull() >= 0) { // one hull to a ship, drawn by the block sailing it
                SeaLanes.Size size = SeaLanes.Size.values()[f.ship().hull()];
                Far ship = far(real, f.id(), f.x(), f.z(), Math.atan2(f.facingX(), f.facingZ()), size, null, f.ship());
                ships.add(ship);
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
    private static Far far(List<double[]> real, Object key, double x, double z, double yaw, SeaLanes.Size size,
                           @Nullable SeaLanes.Ship lane, @Nullable FormationSnapshot.Ship battle) {
        double[] own = null;
        for (double[] r : real) {
            if (Math.hypot(r[0] - x, r[1] - z) <= OWN_SHIP) {
                own = r;
                x = r[0];
                z = r[1];
                yaw = r[2];
                break;
            }
        }
        Swell.Ride ride = ride(key, x, z, yaw, size);
        return own != null ? new Far(x, z, yaw, size, true, lane, battle, own[3], own[4], ride.offset())
                : new Far(x, z, yaw, size, false, lane, battle, ride.fore(), ride.port(), ride.offset());
    }

    /** A far ship's float on the swell, and the game tick it was last ticked to. Client thread only. */
    private static final class Riding {
        final Swell.Ride ride = new Swell.Ride();
        long tick = Long.MIN_VALUE / 2;
        boolean seen;
    }

    private static final Map<Object, Riding> RIDES = new HashMap<>();
    /** A ship first seen floats this many ticks before it is drawn, enough for its springs to settle. */
    private static final int SETTLE_TICKS = 40;

    // shortcut: ticks once a game tick, so the bob steps at 20 a second; interpolate with partialTick if it shows up close.
    private static Swell.Ride ride(Object key, double x, double z, double yaw, SeaLanes.Size size) {
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
        r.tick = now;
        return r.ride;
    }

    /** Where a ship's keel sits: a block down in the water, as the server holds the real ones. */
    static double keel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? 62 : mc.level.getSeaLevel() - 1;
    }
}
