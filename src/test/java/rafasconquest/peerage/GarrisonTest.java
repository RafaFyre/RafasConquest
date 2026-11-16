package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GarrisonTest {
    @Test
    void lossesEmptyCardsLastToFirst() {
        Garrison g = new Garrison();
        int levy = rafasconquest.battle.BattlePlan.Arm.LEVY.ordinal();
        int archers = rafasconquest.battle.BattlePlan.Arm.ARCHERS.ordinal();
        int halberds = rafasconquest.battle.BattlePlan.Arm.HALBERDIERS.ordinal();
        g.add(levy);
        g.add(archers);
        g.add(levy);
        g.add(levy);
        g.add(halberds);
        double[] kept = new double[rafasconquest.battle.BattlePlan.Arm.values().length];
        java.util.Arrays.fill(kept, -1);
        kept[levy] = 0.45; // 600 levies -> 270: one whole card, one of 70, one gone
        kept[archers] = 0; // the archers' card is gone
        g.survive(kept, 0.5); // halberdiers weren't fielded: the army's share
        assertEquals(List.of(levy, levy, halberds), g.units);
        assertEquals(List.of(200, 70, 100), g.men);
        assertEquals(370, g.soldiers());
    }
}
