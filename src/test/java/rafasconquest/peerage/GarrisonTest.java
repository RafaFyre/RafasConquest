package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void aFleetTakesOnlyShipCardsAndAnArmyOnlyLandOnes() {
        rafasconquest.battle.UnitCards.apply("arms", "test_ship", "{\"kind\": \"foot\", \"ship\": \"large\"}");
        try {
            rafasconquest.battle.BattlePlan.Arm ship = rafasconquest.battle.BattlePlan.Arm.byId("test_ship");
            Garrison fleet = Garrison.raiseFleet(net.minecraft.util.RandomSource.create(1), 0, 0);
            assertTrue(fleet.fleet);
            assertTrue(fleet.units.stream().allMatch(u -> rafasconquest.battle.BattlePlan.Arm.get(u).naval()), "a land card in a fleet");
            Garrison army = Garrison.raise(net.minecraft.util.RandomSource.create(1));
            assertTrue(army.units.stream().noneMatch(u -> rafasconquest.battle.BattlePlan.Arm.get(u).naval()), "a ship card in an army");
            assertTrue(fleet.takes(ship) && !army.takes(ship));
            assertFalse(fleet.takes(rafasconquest.battle.BattlePlan.Arm.LEVY));

            // An older save's army with a ship among its cards: the ship is stripped out, to go to the fleet.
            army.add(ship.ordinal());
            Garrison loaded = Garrison.load(army.save());
            assertFalse(loaded.fleet);
            assertEquals(List.of(ship.ordinal()), loaded.strip().stream().map(s -> s[0]).toList());
            assertEquals(Garrison.START, loaded.units.size());

            // A sea battle with no cards given sails ship cards only.
            int[] mix = rafasconquest.battle.BattlePlan.navalMix(7);
            assertEquals(7, java.util.Arrays.stream(mix).sum());
            for (rafasconquest.battle.BattlePlan.Arm a : rafasconquest.battle.BattlePlan.Arm.values()) {
                assertTrue(mix[a.ordinal()] == 0 || a.naval(), a.id + " sails in a navy mix");
            }
        } finally {
            rafasconquest.battle.UnitCards.replace(List.of());
        }
    }

    @Test
    void cardsAreSavedByIdSoAddedFilesDontRenumberThem() {
        rafasconquest.battle.UnitCards.apply("arms", "pikemen", "{\"kind\": \"foot\"}");
        try {
            int pikes = rafasconquest.battle.BattlePlan.Arm.byId("pikemen").ordinal();
            int archers = rafasconquest.battle.BattlePlan.Arm.ARCHERS.ordinal();
            Garrison g = new Garrison();
            g.add(pikes);
            g.add(archers);
            net.minecraft.nbt.CompoundTag saved = g.save();
            assertEquals(List.of(pikes, archers), Garrison.load(saved).units);

            rafasconquest.battle.UnitCards.replace(List.of()); // the pikemen's file gone: the card is dropped
            assertEquals(List.of(archers), Garrison.load(saved).units);

            net.minecraft.nbt.CompoundTag old = new net.minecraft.nbt.CompoundTag(); // a save from before card ids
            old.putIntArray("units", new int[]{archers});
            assertEquals(List.of(archers), Garrison.load(old).units);
        } finally {
            rafasconquest.battle.UnitCards.replace(List.of());
        }
    }
}
