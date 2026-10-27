package rafasconquest.battle;

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
 */
public record FormationSnapshot(int id, Kind kind, int rgb, int mark, int alive, double x, double y, double z,
                                double facingX, double facingZ, double halfWidth, double halfDepth, boolean fighting,
                                boolean leader) {
    public enum Kind {
        SWORDSMEN, SHIELDWALL, ARCHERS, CAVALRY, GENERAL, BEARER;

        public static Kind of(BattlePlan.Arm arm) {
            return switch (arm) {
                case SWORDSMEN -> SWORDSMEN;
                case SHIELDWALL -> SHIELDWALL;
                case ARCHERS -> ARCHERS;
                case CAVALRY -> CAVALRY;
            };
        }
    }

    /** One soldier to draw, on the ground at ({@code x}, {@code z}). */
    public record Sprite(double x, double z, boolean leader) {
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
                                       double facingX, double facingZ, boolean fighting, boolean leader) {
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
        return new FormationSnapshot(id, kind, rgb, mark, n, cx, cy, cz, facingX, facingZ, halfWidth, halfDepth, fighting, leader);
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
     * as its shape suggests. The leader takes the middle of the front rank, as when the formation was raised. A
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
        int leaderAt = leader ? Math.min(BattlePlan.LEADER_INDEX, files - 1) : -1;
        for (int i = 0; i < alive; i++) {
            int file = i % files;
            int rank = i / files;
            double right = (file + 0.5) * cellRight - width / 2;
            double back = (rank + 0.5) * cellBack - depth / 2;
            if (fighting) {
                right += jitter(i, 0) * cellRight;
                back += jitter(i, 1) * cellBack;
            }
            sprites.add(new Sprite(x + rightX * right - facingX * back, z + rightZ * right - facingZ * back, i == leaderAt));
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
                fx, fz, lerp(from.halfWidth, halfWidth, t), lerp(from.halfDepth, halfDepth, t), fighting, leader);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
