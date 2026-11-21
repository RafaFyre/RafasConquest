package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityPlatesTest {
    // A 100-block box from (0, 0): plates 0..3 a side, the last only 4 blocks in it; walls 30..69.
    private final CityPlates plates = new CityPlates(0, 0, 100, 64, new int[]{30, 30, 69, 69}, null, null);

    @Test
    void platesTouchingTheBoxOrAnAcceptedOneAreAccepted() {
        long west = CityPlates.key(-1, 1);
        long further = CityPlates.key(-2, 1);
        long corner = CityPlates.key(-1, -1); // only a corner touches the box
        long far = CityPlates.key(-5, 1);
        assertEquals(List.of(further, west), plates.accept(List.of(further, west, corner, far), Set.of()));
    }

    @Test
    void partlyInBoxPlateIsAcceptedButWallsAndFlatOnesAreNot() {
        assertEquals(List.of(CityPlates.key(3, 0)), plates.accept(List.of(CityPlates.key(3, 0)), Set.of()));
        assertEquals(List.of(), plates.accept(List.of(CityPlates.key(0, 0), CityPlates.key(1, 1)), Set.of())); // in the box; over the walls
        long done = CityPlates.key(-1, 1);
        assertEquals(List.of(CityPlates.key(-2, 1)), plates.accept(List.of(done, CityPlates.key(-2, 1)), Set.of(done)));
    }

    @Test
    void districtsGoOnFlatPlatesTouchingTheCityOrADistrict() {
        long near = CityPlates.key(-1, 1);
        long out = CityPlates.key(-2, 1);
        long far = CityPlates.key(-3, 1);
        Set<Long> flat = Set.of(near, out, far);
        assertEquals(List.of(), plates.acceptDistricts(List.of(out), flat, Set.of(), Set.of())); // flat, but not touching the city
        assertEquals(List.of(out, near), plates.acceptDistricts(List.of(out, near), flat, Set.of(), Set.of()));
        assertEquals(List.of(out), plates.acceptDistricts(List.of(near, out), flat, Set.of(near), Set.of())); // near has one already
        assertEquals(List.of(), plates.acceptDistricts(List.of(CityPlates.key(-1, 2)), flat, Set.of(), Set.of())); // not flat
        assertEquals(List.of(far), plates.acceptDistricts(List.of(far), flat, Set.of(), Set.of(far))); // a road crosses it
    }

    @Test
    void roadsStartAtTheCityEdgeOrARoadAndAvoidWallsAndDistricts() {
        int[] west = {10, 50, -60, 50}; // from the margin west of the walls, outward
        assertEquals(null, plates.roadProblem(west, List.of(), Set.of()));
        assertEquals("A road must start on the city's edge or on another road", plates.roadProblem(new int[]{-100, 0, -150, 0}, List.of(), Set.of()));
        assertEquals(null, plates.roadProblem(new int[]{-50, 51, -50, 0}, List.of(west), Set.of())); // branches off it
        assertEquals("A road may not cross the walls, the harbour or the noble district", plates.roadProblem(new int[]{10, 50, 50, 50}, List.of(), Set.of()));
        assertEquals("A road may not cross a district", plates.roadProblem(west, List.of(), Set.of(CityPlates.key(-2, 1))));
        assertEquals("Too short", plates.roadProblem(new int[]{10, 50, 12, 50}, List.of(), Set.of()));
        assertEquals(true, plates.roadPlates(List.of(west)).contains(CityPlates.key(-2, 1)));
        assertEquals(5, CityPlates.columns(new int[]{0, 0, 0, 10}).stream().filter(c -> c[1] == 5).count()); // 5 wide
    }

    @Test
    void keysRoundTripNegatives() {
        long k = CityPlates.key(-3, -7);
        assertEquals(-3, CityPlates.i(k));
        assertEquals(-7, CityPlates.j(k));
        assertEquals(CityPlates.key(-1, 0), plates.at(-0.5, 31.9));
    }
}
