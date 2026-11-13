package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * What a client is told about one formation of a battle, so it can draw the formation where its soldiers are too far
 * away to be seen: where it stands, which way it faces, how spread out it is and how many are left. A summary rather
 * than every soldier, so it stays small however big battles get. The general and the standard-bearer are formations
 * of one. Plain numbers, so it can be tested without the game.
 *
 * @param id        stable for the battle, so the client can glide each formation from one snapshot to the next
 * @param rgb       the uniform colour
 * @param mark      the banner's first pattern colour, for the flags of leaders, the general and the standard-bearer
 * @param halfWidth half the formation's width along its line (to its right), measured from where its soldiers stand
 * @param halfDepth half its depth front to back
 * @param fighting  broken up into a melee, rather than standing in ranks
 * @param leader    its leader (wearing the faction banner) is still alive
 * @param soldierIds the entity ids of its living soldiers, so a client that still has them can draw each where it
 *                  really stands
 * @param leaderId  the leader's entity id, or -1 once the leader has fallen
 * @param ship      its ship, at sea (the client draws it in the distance, as the real one is built); null ashore
 * @param engine    the siege engine it works or carries (the client draws it in the distance); null if none
 */
public record FormationSnapshot(int id, Kind kind, int rgb, int mark, int alive, double x, double y, double z,
                                double facingX, double facingZ, double halfWidth, double halfDepth, boolean fighting,
                                boolean leader, int[] soldierIds, int leaderId, @Nullable Ship ship, @Nullable Engine engine) {
    public FormationSnapshot(int id, Kind kind, int rgb, int mark, int alive, double x, double y, double z,
                             double facingX, double facingZ, double halfWidth, double halfDepth, boolean fighting,
                             boolean leader, int[] soldierIds, int leaderId, @Nullable Ship ship) {
        this(id, kind, rgb, mark, alive, x, y, z, facingX, facingZ, halfWidth, halfDepth, fighting, leader, soldierIds, leaderId, ship, null);
    }

    /** {@link Engine#kind} of a ladder: after every {@link Machine}'s ordinal. */
    public static final int LADDER = Machine.values().length;

    /**
     * A formation's siege engine: a {@link Machine}'s ordinal or {@link #LADDER}. A tower, ram or ladder stands where it
     * says ({@code x}, {@code y}, {@code z}), facing ({@code facingX}, {@code facingZ}), {@code up} once against the wall
     * (or the gate); NaN {@code x} for engines that stand among their crew, one under every {@link Machine#crew}-th.
     */
    public record Engine(int kind, double x, double y, double z, double facingX, double facingZ, boolean up) {
        /** Whether it stands among its crew rather than where it says. */
        public boolean withCrew() {
            return Double.isNaN(x);
        }

        /** Partway from {@code from} to this ({@code t} from 0 to 1); its kind and state are this one's. */
        Engine from(@Nullable Engine from, double t) {
            if (from == null || from.kind != kind || withCrew() || from.withCrew()) {
                return this;
            }
            return new Engine(kind, lerp(from.x, x, t), lerp(from.y, y, t), lerp(from.z, z, t), facingX, facingZ, up);
        }
    }

    /** This formation with {@code engine} as its siege engine. */
    public FormationSnapshot withEngine(@Nullable Engine engine) {
        return new FormationSnapshot(id, kind, rgb, mark, alive, x, y, z, facingX, facingZ, halfWidth, halfDepth, fighting, leader, soldierIds, leaderId, ship, engine);
    }
    /**
     * A formation's ship at sea, as its real one is built ({@link rafasconquest.style.ShipHulls#battle}): its sail's and
     * its stripe's dye colours (DyeColor ids), and whether it is a supply ship, stores on deck and nobody showing.
     */
    public record Ship(int sail, int stripe, boolean cargo) {
    }

    /** Aboard its ship, at sea. */
    public boolean afloat() {
        return ship != null;
    }

    public enum Kind {
        LEVY, HEAVY_INFANTRY, SKIRMISHERS, ZWEIHANDLERS, ARCHERS, CAVALRY, HEAVY_CAVALRY, MUSKETEERS, ENGINES, GENERAL, BEARER;

        /** The kind drawn for a block of {@code arm}: the same name. */
        public static Kind of(BattlePlan.Arm arm) {
            return valueOf(arm.name());
        }

        public boolean mounted() {
            return this == CAVALRY || this == HEAVY_CAVALRY;
        }

        /** Foot that fights hand to hand in the ranks. */
        public boolean infantry() {
            return this == LEVY || this == HEAVY_INFANTRY || this == SKIRMISHERS || this == ZWEIHANDLERS;
        }
    }

    /**
     * One soldier to draw, its feet at ({@code x}, {@code y}, {@code z}); {@code real} if it stands where a real
     * soldier the client has is standing (the client may draw that soldier itself), rather than in the ranks.
     */
    public record Sprite(double x, double y, double z, boolean leader, boolean real) {
    }

    /**
     * A uniform spread of width w has a standard deviation of w / √12, so √3 standard deviations is half its width.
     */
    private static final double HALF_EXTENT_PER_SD = Math.sqrt(3);
    /** Even a formation in perfect file has some depth and width to lay sprites out in. */
    private static final double MIN_HALF_EXTENT = 0.5;
    /** How far a fighting soldier's sprite strays from its place in the grid, as a share of a cell. */
    private static final double MELEE_JITTER = 0.35;

    /**
     * Summarises soldiers standing at ({@code xs[i]}, {@code ys[i]}, {@code zs[i]}) facing ({@code facingX},
     * {@code facingZ}), a unit vector. There must be at least one.
     */
    public static FormationSnapshot of(int id, Kind kind, int rgb, int mark, double[] xs, double[] ys, double[] zs,
                                       double facingX, double facingZ, boolean fighting, boolean leader, int[] soldierIds,
                                       int leaderId, @Nullable Ship ship) {
        int n = xs.length;
        double cx = mean(xs);
        double cy = mean(ys);
        double cz = mean(zs);
        double rightX = -facingZ;
        double rightZ = facingX;
        double sumRight = 0;
        double sumBack = 0;
        for (int i = 0; i < n; i++) {
            double dx = xs[i] - cx;
            double dz = zs[i] - cz;
            double right = dx * rightX + dz * rightZ;
            double ahead = dx * facingX + dz * facingZ;
            sumRight += right * right;
            sumBack += ahead * ahead;
        }
        double halfWidth = Math.sqrt(sumRight / n) * HALF_EXTENT_PER_SD;
        double halfDepth = Math.sqrt(sumBack / n) * HALF_EXTENT_PER_SD;
        return new FormationSnapshot(id, kind, rgb, mark, n, cx, cy, cz, facingX, facingZ, halfWidth, halfDepth, fighting, leader, soldierIds, leaderId, ship);
    }

    private static double mean(double[] values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    /**
     * Where to draw each of its soldiers: a grid of ranks filling the formation's width and depth, as many files wide
     * as its shape suggests. The leader takes the middle of the back rank, as when the formation was raised. A
     * fighting formation's soldiers are scattered a little about their places, the same way every time.
     */
    public List<Sprite> layout() {
        List<Sprite> sprites = new ArrayList<>(alive);
        if (alive <= 0) {
            return sprites;
        }
        double width = Math.max(halfWidth, MIN_HALF_EXTENT) * 2;
        double depth = Math.max(halfDepth, MIN_HALF_EXTENT) * 2;
        int files = (int) Math.max(1, Math.min(alive, Math.round(Math.sqrt(alive * width / depth))));
        int ranks = (alive + files - 1) / files;
        double cellRight = width / files;
        double cellBack = depth / ranks;
        double rightX = -facingZ;
        double rightZ = facingX;
        int leaderAt = leader ? BattlePlan.captainPlace(alive, files) : -1;
        for (int i = 0; i < alive; i++) {
            int file = i % files;
            int rank = i / files;
            double right = (file + 0.5) * cellRight - width / 2;
            double back = (rank + 0.5) * cellBack - depth / 2;
            if (fighting) {
                right += jitter(i, 0) * cellRight;
                back += jitter(i, 1) * cellBack;
            }
            sprites.add(new Sprite(x + rightX * right - facingX * back, y, z + rightZ * right - facingZ * back, i == leaderAt, false));
        }
        return sprites;
    }

    /** Where a client has a soldier: its feet, or null if the client doesn't have it. */
    @FunctionalInterface
    public interface Positions {
        @Nullable
        double[] of(int entityId);
    }

    /**
     * Where to draw each of its soldiers, preferring where they really are: a soldier the client has stands at its
     * own position, so it doesn't jump when the game stops drawing it. The rest (the client hasn't been sent them, or
     * the block is only numbers) take their place in the ranks ({@link #layout}).
     */
    public List<Sprite> place(Positions positions) {
        List<Sprite> ranks = null;
        List<Sprite> sprites = new ArrayList<>(alive);
        // A block that is only numbers has no soldiers' ids at all: every soldier takes its place in the ranks.
        for (int i = 0; i < alive; i++) {
            if (i >= soldierIds.length) {
                if (ranks == null) {
                    ranks = layout();
                }
                sprites.add(ranks.get(i));
                continue;
            }
            double[] feet = positions.of(soldierIds[i]);
            if (feet != null) {
                sprites.add(new Sprite(feet[0], feet[1], feet[2], soldierIds[i] == leaderId, true));
            } else {
                if (ranks == null) {
                    ranks = layout();
                }
                if (i < ranks.size()) {
                    sprites.add(ranks.get(i));
                }
            }
        }
        return sprites;
    }

    /** A fixed offset in [-MELEE_JITTER, MELEE_JITTER] for soldier {@code i}, axis {@code axis}. */
    private double jitter(int i, int axis) {
        long h = (id * 0x9E3779B97F4A7C15L) ^ (i * 0xC2B2AE3D27D4EB4FL) ^ (axis * 0x165667B19E3779F9L);
        h ^= h >>> 29;
        h *= 0xBF58476D1CE4E5B9L;
        h ^= h >>> 32;
        return ((h >>> 11) / (double) (1L << 53) * 2 - 1) * MELEE_JITTER;
    }

    /** This formation, its middle moved to ({@code x}, {@code y}, {@code z}). */
    public FormationSnapshot movedTo(double x, double y, double z) {
        return new FormationSnapshot(id, kind, rgb, mark, alive, x, y, z, facingX, facingZ, halfWidth, halfDepth, fighting, leader, soldierIds, leaderId, ship, engine);
    }

    /** Partway from {@code from} to this snapshot of the same formation ({@code t} from 0 to 1). */
    public FormationSnapshot from(FormationSnapshot from, double t) {
        double fx = lerp(from.facingX, facingX, t);
        double fz = lerp(from.facingZ, facingZ, t);
        double length = Math.hypot(fx, fz);
        if (length < 1e-6) {
            fx = facingX;
            fz = facingZ;
        } else {
            fx /= length;
            fz /= length;
        }
        return new FormationSnapshot(id, kind, rgb, mark, alive, lerp(from.x, x, t), lerp(from.y, y, t), lerp(from.z, z, t),
                fx, fz, lerp(from.halfWidth, halfWidth, t), lerp(from.halfDepth, halfDepth, t), fighting, leader, soldierIds, leaderId, ship,
                engine == null ? null : engine.from(from.engine, t));
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
