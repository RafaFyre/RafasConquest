package rafasconquest.bridge;

import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SiegeSurveyTest {
    @Test
    void neverThePortNorAcrossWater() {
        List<SiegeSurvey.Land> land = List.of(
                new SiegeSurvey.Land(Direction.NORTH, 0.1, 0, false), // the flattest, but the port
                new SiegeSurvey.Land(Direction.EAST, 0.2, 0.1, true), // a river before it
                new SiegeSurvey.Land(Direction.SOUTH, 3, 0, false),
                new SiegeSurvey.Land(Direction.WEST, 2, 0, false));
        assertEquals(Direction.WEST, SiegeSurvey.attackSide(land, Direction.NORTH, null));
        assertEquals(Direction.NORTH, SiegeSurvey.attackSide(land, null, null));
        // Never the noble district's side, even when it is the flattest open one.
        assertEquals(Direction.SOUTH, SiegeSurvey.attackSide(land, Direction.NORTH, Direction.WEST));
        // All barred: the least wet that isn't the port.
        List<SiegeSurvey.Land> wet = List.of(
                new SiegeSurvey.Land(Direction.NORTH, 0, 0, false),
                new SiegeSurvey.Land(Direction.EAST, 0, 0.6, true),
                new SiegeSurvey.Land(Direction.SOUTH, 0, 0.2, true),
                new SiegeSurvey.Land(Direction.WEST, 0, 0.9, true));
        assertEquals(Direction.SOUTH, SiegeSurvey.attackSide(wet, Direction.NORTH, null));
        // The least wet is the noble district's: the next least wet instead.
        assertEquals(Direction.EAST, SiegeSurvey.attackSide(wet, Direction.NORTH, Direction.SOUTH));
    }
}
