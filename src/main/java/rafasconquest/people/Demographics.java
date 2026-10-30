package rafasconquest.people;

import java.util.ArrayList;
import java.util.List;

/**
 * A nation's people: how many they are, and what share of them (0 to 1, summing to 1) is of each culture and of each
 * race, both indexed by {@link Homeland}.
 */
public record Demographics(int population, float[] cultures, float[] races) {
    public static final Demographics NONE = new Demographics(0, new float[Homeland.values().length], new float[Homeland.values().length]);

    /** A share of the people, with what it is a share of. */
    public record Share(int index, float share) {
    }

    /** The shares worth naming (at least {@code floor}), largest first. */
    public static List<Share> largest(float[] shares, float floor) {
        List<Share> found = new ArrayList<>();
        for (int i = 0; i < shares.length; i++) {
            if (shares[i] >= floor) {
                found.add(new Share(i, shares[i]));
            }
        }
        found.sort((a, b) -> Float.compare(b.share(), a.share()));
        return found;
    }

    /** The index of the largest share. */
    public static int largest(float[] shares) {
        int best = 0;
        for (int i = 1; i < shares.length; i++) {
            if (shares[i] > shares[best]) {
                best = i;
            }
        }
        return best;
    }
}
