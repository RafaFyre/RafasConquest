package rafasconquest.core;

import java.util.List;

/**
 * A city's recruitment post: a stone house on the plot beside the town centre (the first plot of the city block just
 * east of it, its door facing the road to the west), where a recruiter stands behind a bedrock counter facing the door,
 * guarded by five soldiers (two flanking the door outside, three within). Only a city a nation holds has one.
 * Positions are in blocks, (x, z); heights are worked out where the post is built.
 */
public record RecruitmentPost(int plotX, int plotZ) {
    /** The house's walls: from (x0, z0) to (x1, z1), 9 × 9 blocks on the 14 × 14 plot. */
    public int x0() {
        return plotX + 1;
    }

    public int x1() {
        return plotX + 9;
    }

    public int z0() {
        return plotZ + 2;
    }

    public int z1() {
        return plotZ + 10;
    }

    /** The door, in the west wall's middle. */
    public int[] door() {
        return new int[]{x0(), plotZ + 6};
    }

    /** The bedrock counter, and the recruiter behind it facing the door (west). */
    public int[] counter() {
        return new int[]{plotX + 6, plotZ + 6};
    }

    public int[] recruiter() {
        return new int[]{plotX + 7, plotZ + 6};
    }

    /**
     * The gold church on the plot just south of the post, where the hold's lord receives petitioners (with the city's
     * own lord, in a duchy's or a nation's capital): its plot's north-west corner.
     */
    public int[] church() {
        return new int[]{plotX, plotZ + CityLayout.PLOT_SIZE + 1};
    }

    /** Roughly where the hold's nobles stand: the middle of the church's plot. */
    public int[] noble() {
        int[] c = church();
        return new int[]{c[0] + CityLayout.PLOT_SIZE / 2, c[1] + CityLayout.PLOT_SIZE / 2};
    }

    /** The quest board, just inside the door. */
    public int[] board() {
        return new int[]{plotX + 2, plotZ + 4};
    }

    /** The five guards: two beside the door outside, three inside along the walls. */
    public List<int[]> guards() {
        return List.of(new int[]{plotX, plotZ + 5}, new int[]{plotX, plotZ + 7},
                new int[]{plotX + 3, plotZ + 3}, new int[]{plotX + 3, plotZ + 9}, new int[]{plotX + 8, plotZ + 3});
    }

    /** The city block holding the post: just east of the town centre's. */
    public static int blockX(CityLayout.Site city) {
        return city.centerBlock() + 1;
    }

    public static int blockZ(CityLayout.Site city) {
        return city.centerBlock();
    }

    /** A city's post (whether or not the city has one: only a held city does). */
    public static RecruitmentPost of(CityLayout.Site city) {
        return new RecruitmentPost(city.originX() + CityLayout.blockStart(blockX(city)), city.originZ() + CityLayout.blockStart(blockZ(city)));
    }
}
