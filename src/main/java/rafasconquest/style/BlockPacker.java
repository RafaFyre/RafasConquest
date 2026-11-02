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

    private record Rect(int x, int z, int w, int d) {
    }

    private BlockPacker() {
    }

    public static List<Placed> pack(int blockSize, List<Size> sizes, Random random) {
        List<Placed> placed = new ArrayList<>();
        if (sizes.isEmpty()) {
            return placed;
        }
        List<Rect> free = new ArrayList<>(List.of(new Rect(0, 0, blockSize, blockSize)));
        int misses = 0;
        while (misses < GIVE_UP && !free.isEmpty()) {
            int b = random.nextInt(sizes.size());
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
                int turns = turned ? (centreX < blockSize / 2.0 ? 3 : 1) : (centreZ < blockSize / 2.0 ? 0 : 2);
                placed.add(new Placed(b, best.x(), best.z(), turns));
                done = true;
                break;
            }
            misses = done ? 0 : misses + 1;
        }
        return placed;
    }
}
