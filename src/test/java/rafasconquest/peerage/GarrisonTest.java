package rafasconquest.peerage;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GarrisonTest {
    @Test
    void anArmyWalksAtItsPaceAndStopsAtItsGoal() {
        assertNull(Garrison.at(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0, 100), "home: not on the map");
        long half = (long) (500 / Garrison.PACE);
        assertArrayEquals(new double[]{500, 0}, Garrison.at(0, 0, 1000, 0, 0, half), 1);
        assertArrayEquals(new double[]{1000, 0}, Garrison.at(0, 0, 1000, 0, 0, half * 10), 1e-9);
        assertArrayEquals(new double[]{0, 0}, Garrison.at(0, 0, 1000, 0, 50, 10), 1e-9);
    }

    @Test
    void aSpawnedArmyFillsToItsCapAndKeepsItsNationGeneralAndFoe() {
        Garrison g = new Garrison();
        g.nation = 3;
        g.general = "Aldric";
        g.cap = 7;
        g.target = 2;
        g.fill(g.cap, net.minecraft.util.RandomSource.create(1));
        assertEquals(7, g.units.size());
        Garrison back = Garrison.load(g.save());
        assertEquals(List.of(3, "Aldric", 7, 2, g.units), List.of(back.nation, back.general, back.cap, back.target, back.units));
        Garrison old = Garrison.load(new net.minecraft.nbt.CompoundTag()); // a save from before spawned armies
        assertEquals(List.of(-1, GrandStrategy.NO_TARGET, 0), List.of(old.nation, old.target, old.cap));
    }
}
