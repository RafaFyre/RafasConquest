package rafasconquest.peerage;

import org.junit.jupiter.api.Test;
import rafasconquest.character.Rank;
import rafasconquest.core.TitleMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeerageTest {
    @Test
    void aQuestIsKeptWhole() {
        Quest q = new Quest(1234567890123L, Quest.Type.CLEAR, Quest.Ladder.MILITARY, Quest.OFFICER, "Recruiter of Shukai", 10, -20,
                "the hamlet of Mill", 300, 400, 8, 20, 8, "abc");
        assertEquals(q, Quest.load(q.save()));
        assertTrue(q.title().contains("8") && q.title().contains("the hamlet of Mill"));
    }

    @Test
    void ranksClimbWithMilitaryRenown() {
        assertEquals(20, Advancement.toNextRank(Rank.SOLDIER, 0));
        assertEquals(0, Advancement.toNextRank(Rank.SOLDIER, 25));
        assertEquals(-1, Advancement.toNextRank(Rank.GRAND_MARSHAL, 0));
        for (int r = 2; r < Advancement.MILITARY.length; r++) {
            assertTrue(Advancement.MILITARY[r] > Advancement.MILITARY[r - 1], "rank " + r + " no harder than the last");
        }
    }

    @Test
    void aCountyIsUnderTheTitlesAboveIt() {
        // Four provinces: two counties (0, 1 and 2, 3), one duchy, one kingdom, one empire.
        TitleMap t = new TitleMap(new int[]{0, 0, 1, 1}, new int[]{0, 0}, new int[]{0}, new int[]{0},
                new int[]{0, 2}, new int[]{0}, new int[]{0}, new int[]{0});
        assertTrue(LandGrants.under(t, 2, 0), "the empire's seat doesn't hold the other county");
        assertTrue(LandGrants.under(t, 3, 2), "a county's seat (2) doesn't hold its own county");
        assertFalse(LandGrants.under(t, 0, 2), "a count holds land in another county");
        assertFalse(LandGrants.under(t, 0, 3), "a province with no seat holds land");
    }

    @Test
    void knightsAndCountsAreNoble() {
        assertFalse(NobleTitle.NONE.noble());
        assertTrue(NobleTitle.KNIGHT.noble());
        assertTrue(NobleTitle.COUNT.noble());
        assertEquals("Count Steve", Names.player("Steve", NobleTitle.COUNT));
        assertEquals("Steve", Names.player("Steve", NobleTitle.NONE));
    }
}
