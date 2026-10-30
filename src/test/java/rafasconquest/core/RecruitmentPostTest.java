package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecruitmentPostTest {
    @Test
    void thePostStandsOnAPlotBesideTheTownCentre() {
        for (int cells = 2; cells <= 9; cells++) {
            CityLayout.Site city = new CityLayout.Site(500, -300, false, cells);
            RecruitmentPost post = RecruitmentPost.of(city);
            int lx = post.plotX() - city.originX();
            int lz = post.plotZ() - city.originZ();
            assertEquals(CityLayout.blockStart(city.centerBlock() + 1), lx, "not on the block east of the town centre");
            assertEquals(CityLayout.blockStart(city.centerBlock()), lz);
            assertTrue(post.x1() - post.plotX() < CityLayout.PLOT_SIZE && post.z1() - post.plotZ() < CityLayout.PLOT_SIZE,
                    "the house spills off its plot");
        }
    }

    @Test
    void theRecruiterStandsBehindTheCounterFacingTheDoorWithFiveGuards() {
        RecruitmentPost post = RecruitmentPost.of(new CityLayout.Site(0, 0, false, 3));
        int[] door = post.door();
        int[] counter = post.counter();
        int[] recruiter = post.recruiter();
        assertEquals(door[1], counter[1]);
        assertEquals(door[1], recruiter[1]);
        assertTrue(door[0] < counter[0] && counter[0] < recruiter[0], "the counter isn't between the door and the recruiter");
        assertTrue(recruiter[0] < post.x1() && recruiter[0] > post.x0(), "the recruiter isn't inside");
        assertEquals(5, post.guards().size());
        for (int[] g : post.guards()) {
            boolean inside = g[0] > post.x0() && g[0] < post.x1() && g[1] > post.z0() && g[1] < post.z1();
            boolean outside = g[0] < post.x0();
            assertTrue(inside || outside, "a guard in a wall at " + g[0] + ", " + g[1]);
            assertTrue(!(g[0] == counter[0] && g[1] == counter[1]) && !(g[0] == recruiter[0] && g[1] == recruiter[1]));
        }
    }
}
