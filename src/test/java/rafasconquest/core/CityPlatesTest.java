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
        assertEquals(null, plates.roadProblem(west, List.of(), Set.of(), List.of()));
        assertEquals("A road must start on the city's edge or on another road", plates.roadProblem(new int[]{-100, 0, -150, 0}, List.of(), Set.of(), List.of()));
        assertEquals(null, plates.roadProblem(new int[]{-50, 51, -50, 0}, List.of(west), Set.of(), List.of())); // branches off it
        assertEquals("A road may not cross the walls, the harbour or the noble district", plates.roadProblem(new int[]{10, 50, 50, 50}, List.of(), Set.of(), List.of()));
        assertEquals("A road may not cross a district", plates.roadProblem(west, List.of(), Set.of(CityPlates.key(-2, 1)), List.of()));
        assertEquals("Too short", plates.roadProblem(new int[]{10, 50, 12, 50}, List.of(), Set.of(), List.of()));
        assertEquals(true, plates.roadPlates(List.of(west)).contains(CityPlates.key(-2, 1)));
        assertEquals(5, CityPlates.columns(new int[]{0, 0, 0, 10}).stream().filter(c -> c[1] == 5).count()); // 5 wide
    }

    @Test
    void roadsClimbInEightBlockTilesOfFourBlocksThenFourSlabs() {
        int[] ground = new int[24];
        for (int k = 0; k < 24; k++) {
            ground[k] = 64 + k; // a steep hill: one block up each block
        }
        boolean[] fixed = new boolean[24];
        fixed[0] = true; // starts on the city's flat ground at 64
        int[] steps = CityPlates.steps(ground, fixed, 64);
        assertEquals(List.of(64, 65, 66), java.util.Arrays.stream(steps).boxed().toList());
        assertEquals(List.of(64, 0), java.util.Arrays.stream(CityPlates.surface(steps, 3)).boxed().toList()); // full block
        assertEquals(List.of(65, 1), java.util.Arrays.stream(CityPlates.surface(steps, 5)).boxed().toList()); // slab up
        assertEquals(List.of(65, 0), java.util.Arrays.stream(CityPlates.surface(steps, 9)).boxed().toList()); // next tile
        assertEquals(List.of(66, 0), java.util.Arrays.stream(CityPlates.surface(steps, 21)).boxed().toList()); // last tile: flat
        int[] down = {70, 69};
        assertEquals(List.of(70, 1), java.util.Arrays.stream(CityPlates.surface(down, 6)).boxed().toList()); // slab down, on 69
    }

    @Test
    void specialBuildingsStandOnFlatGroundTouchingTheCity() {
        Set<Long> flat = Set.of(CityPlates.key(-1, 1)); // x -32..-1, z 32..63
        assertEquals(null, plates.specialProblem(new int[]{2, 2, 10, 10}, flat, Set.of(), List.of(), List.of())); // the margin
        assertEquals("It may not stand within the walls, on the harbour or the noble district",
                plates.specialProblem(new int[]{25, 25, 35, 35}, flat, Set.of(), List.of(), List.of()));
        assertEquals(null, plates.specialProblem(new int[]{-10, 40, -1, 50}, flat, Set.of(), List.of(), List.of())); // beside the box
        assertEquals("It must touch the city, a road, a district or another building",
                plates.specialProblem(new int[]{-20, 40, -10, 50}, flat, Set.of(), List.of(), List.of()));
        assertEquals(null, plates.specialProblem(new int[]{-20, 40, -10, 50}, flat, Set.of(), List.of(), List.of(new int[]{-9, 40, -1, 45})));
        assertEquals("It needs flat ground", plates.specialProblem(new int[]{-10, 40, -1, 50}, Set.of(), Set.of(), List.of(), List.of()));
        assertEquals("It overlaps another building", plates.specialProblem(new int[]{2, 2, 10, 10}, flat, Set.of(), List.of(), List.of(new int[]{8, 8, 12, 12})));
        assertEquals(List.of(-5, -2, 4, 1), java.util.Arrays.stream(CityPlates.footprint(0, 0, 4, 10, 1)).boxed().toList()); // turned: 10 by 4
    }

    @Test
    void keysRoundTripNegatives() {
        long k = CityPlates.key(-3, -7);
        assertEquals(-3, CityPlates.i(k));
        assertEquals(-7, CityPlates.j(k));
        assertEquals(CityPlates.key(-1, 0), plates.at(-0.5, 31.9));
    }
}
