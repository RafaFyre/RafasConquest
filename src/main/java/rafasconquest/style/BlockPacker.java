package rafasconquest.style;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Fills a city block with buildings of any size: each chosen at random from those given, turned a quarter turn if it
 * fits better so, and set in the snuggest free space left, a block apart from the next. Free space is split as each
 * building is placed (a guillotine cut), and the block is full once a run of tries finds nothing more that fits.
 * Each building faces the block's nearer edge across its turn (its door toward the road there).
 */
public final class BlockPacker {
    /** Blocks kept clear between neighbours. */
    static final int GAP = 1;
    /** Failed tries in a row before the block counts as full. */
    static final int GIVE_UP = 12;

    /** A building's size along x and z, as built facing north. */
    public record Size(int x, int z) {
    }

    /** A building placed: which, where its footprint's corner lies within the block, and its quarter turns (0: north). */
    public record Placed(int building, int x, int z, int turns) {
    }

    /** Free room: its corner and its size along x and z. */
    public record Rect(int x, int z, int w, int d) {
        public boolean overlaps(Rect o) {
            return x < o.x + o.w && o.x < x + w && z < o.z + o.d && o.z < z + d;
        }
    }

    private BlockPacker() {
    }

    public static List<Placed> pack(int blockSize, List<Size> sizes, Random random) {
        return pack(new ArrayList<>(List.of(new Rect(0, 0, blockSize, blockSize))), blockSize, blockSize, sizes, random);
    }

    /**
     * Fills the free room of an area {@code width} by {@code depth} (corner at 0, 0), each building facing the area's
     * nearer edge.
     */
    public static List<Placed> pack(List<Rect> free, int width, int depth, List<Size> sizes, Random random) {
        return pack(free, width, depth, sizes, random, null);
    }

    /** {@link #pack}, building each no more times than {@code most} gives it (null: no limit). */
    public static List<Placed> pack(List<Rect> free, int width, int depth, List<Size> sizes, Random random, int[] most) {
        List<Placed> placed = new ArrayList<>();
        int[] left = most == null ? null : most.clone();
        List<Integer> open = new ArrayList<>();
        for (int i = 0; i < sizes.size(); i++) {
            if (left == null || left[i] > 0) {
                open.add(i);
            }
        }
        free = new ArrayList<>(free);
        int misses = 0;
        while (misses < GIVE_UP && !free.isEmpty() && !open.isEmpty()) {
            int b = open.get(random.nextInt(open.size()));
            Size s = sizes.get(b);
            boolean turnedFirst = random.nextBoolean();
            boolean done = false;
            for (boolean turned : new boolean[]{turnedFirst, !turnedFirst}) {
                int w = turned ? s.z() : s.x();
                int d = turned ? s.x() : s.z();
                Rect best = free.stream()
                        .filter(r -> w <= r.w() && d <= r.d())
                        .min(Comparator.comparingInt((Rect r) -> r.w() * r.d()))
                        .orElse(null);
                if (best == null) {
                    continue;
                }
                free.remove(best);
                // The room left: beside it along its own depth, and the whole width beyond it.
                int rw = w + GAP;
                int rd = d + GAP;
                if (best.w() - rw > 0) {
                    free.add(new Rect(best.x() + rw, best.z(), best.w() - rw, Math.min(rd, best.d())));
                }
                if (best.d() - rd > 0) {
                    free.add(new Rect(best.x(), best.z() + rd, best.w(), best.d() - rd));
                }
                double centreX = best.x() + w / 2.0;
                double centreZ = best.z() + d / 2.0;
                int turns = turned ? (centreX < width / 2.0 ? 3 : 1) : (centreZ < depth / 2.0 ? 0 : 2);
                placed.add(new Placed(b, best.x(), best.z(), turns));
                if (left != null && --left[b] <= 0) {
                    open.remove((Integer) b);
                }
                done = true;
                break;
            }
            misses = done ? 0 : misses + 1;
        }
        return placed;
    }

    /** The free room left once {@code taken} (and {@link #GAP} around it) is built on: each room it cuts, split around it. */
    public static List<Rect> cut(List<Rect> free, Rect taken) {
        Rect t = new Rect(taken.x() - GAP, taken.z() - GAP, taken.w() + 2 * GAP, taken.d() + 2 * GAP);
        List<Rect> left = new ArrayList<>();
        for (Rect r : free) {
            if (!r.overlaps(t)) {
                left.add(r);
                continue;
            }
            int x0 = Math.max(r.x(), t.x());
            int x1 = Math.min(r.x() + r.w(), t.x() + t.w());
            if (t.x() > r.x()) {
                left.add(new Rect(r.x(), r.z(), t.x() - r.x(), r.d()));
            }
            if (t.x() + t.w() < r.x() + r.w()) {
                left.add(new Rect(t.x() + t.w(), r.z(), r.x() + r.w() - t.x() - t.w(), r.d()));
            }
            if (t.z() > r.z()) {
                left.add(new Rect(x0, r.z(), x1 - x0, t.z() - r.z()));
            }
            if (t.z() + t.d() < r.z() + r.d()) {
                left.add(new Rect(x0, t.z() + t.d(), x1 - x0, r.z() + r.d() - t.z() - t.d()));
            }
        }
        return left;
    }
}
