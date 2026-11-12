package rafasconquest.battle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShareLiveTest {
    /** A soldier wanted real: its side and its cost (a rider counts two). */
    private record W(int side, int cost) {
    }

    private static long count(List<W> kept, int side) {
        return kept.stream().filter(w -> w.side() == side).count();
    }

    @Test
    void theEnemyGetsHalfEvenWhenTheOwnRanksAreAllNearer() {
        List<W> wanted = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            wanted.add(new W(0, 1)); // the player's own ranks, all nearer
        }
        for (int i = 0; i < 200; i++) {
            wanted.add(new W(1, 1));
        }
        List<W> kept = FieldRules.shareLive(wanted, W::side, W::cost, 100);
        assertEquals(50, count(kept, 0));
        assertEquals(50, count(kept, 1));
    }

    @Test
    void whatOneSideLeavesGoesToTheOther() {
        List<W> wanted = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            wanted.add(new W(0, 1));
        }
        for (int i = 0; i < 10; i++) {
            wanted.add(new W(1, 2)); // ten riders
        }
        List<W> kept = FieldRules.shareLive(wanted, W::side, W::cost, 100);
        assertEquals(80, count(kept, 0));
        assertEquals(10, count(kept, 1));
    }
}
