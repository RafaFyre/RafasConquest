package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeaLanesTest {
    /** Port A with lanes east to B and south to C. */
    private static final List<Roads.Road> LANES = List.of(
            new Roads.Road(Roads.Grade.SEA, new int[]{0, 3000}, new int[]{0, 0}, "A", "B"),
            new Roads.Road(Roads.Grade.SEA, new int[]{0, 0}, new int[]{0, 3000}, "A", "C"));

    private static SeaLanes.Ship find(double ticks, String id) {
        return SeaLanes.at(LANES, ticks).stream().filter(s -> s.id().equals(id)).findFirst().orElseThrow();
    }

    @Test
    void eachPortStartsWithThreeToNineShipsMoored() {
        List<SeaLanes.Ship> all = SeaLanes.at(LANES, 0);
        for (String port : List.of("A", "B", "C")) {
            long moored = all.stream().filter(s -> s.home().equals(port) && s.lane() == null && s.vx() == 0 && s.vz() == 0).count();
            assertTrue(moored >= 3 && moored <= 9, moored + " moored at " + port);
        }
    }

    @Test
    void eachLaneStartsWithFiveToTenShipsAtSea() {
        List<SeaLanes.Ship> all = SeaLanes.at(LANES, 0);
        for (String lane : List.of("A>B", "A>C")) {
            long atSea = all.stream().filter(s -> lane.equals(s.lane())).count();
            assertTrue(atSea >= 5 && atSea <= 10, atSea + " at sea on " + lane);
        }
    }

    @Test
    void aMooredShipWaitsTwoToFiveMinutesThenSetsSail() {
        SeaLanes.Ship moored = find(0, "A#0");
        assertEquals(null, moored.lane());
        SeaLanes.Ship stillThere = find(SeaLanes.MOOR_MIN_TICKS - 1, "A#0");
        assertEquals(moored.x(), stillThere.x(), 1e-9);
        assertEquals(moored.z(), stillThere.z(), 1e-9);
        // Out of the harbour and onto its lane soon after the longest mooring.
        boolean sailed = false;
        for (double t = SeaLanes.MOOR_MAX_TICKS; t < SeaLanes.MOOR_MAX_TICKS + 3000; t += 50) {
            sailed |= find(t, "A#0").lane() != null;
        }
        assertTrue(sailed, "A#0 never set sail");
    }

    @Test
    void afterComingHomeAShipSailsToANewDestination() {
        List<String> voyages = new ArrayList<>();
        String last = null;
        for (double t = 0; t < 400_000; t += 200) {
            String lane = find(t, "A#1").lane();
            if (lane != null && !Objects.equals(lane, last) && (voyages.isEmpty() || find(t - 200, "A#1").lane() == null)) {
                voyages.add(lane);
            }
            last = lane;
        }
        assertTrue(voyages.size() >= 4, voyages.toString());
        for (int i = 1; i < voyages.size(); i++) {
            assertNotEquals(voyages.get(i - 1), voyages.get(i), voyages.toString());
        }
    }

    @Test
    void shipsKeepToTheRightOfTheirLane() {
        for (double t = 0; t < 100_000; t += 1999) {
            for (SeaLanes.Ship s : SeaLanes.at(LANES, t)) {
                if ("A>B".equals(s.lane()) && Math.abs(Math.abs(s.vx()) - SeaLanes.SPEED) < 1e-9) {
                    // Sailing east (+x) the right is south (+z); sailing west it is north.
                    assertTrue(Math.signum(s.z()) == Math.signum(s.vx()), s.toString());
                }
            }
        }
    }

    @Test
    void mediumShipsAreCommonestThenSmallLargeAndExtraLarge() {
        Map<SeaLanes.Size, Integer> counts = new EnumMap<>(SeaLanes.Size.class);
        for (int i = 0; i < 10_000; i++) {
            counts.merge(SeaLanes.Size.pick(("ship" + i).hashCode()), 1, Integer::sum);
        }
        assertTrue(counts.get(SeaLanes.Size.MEDIUM) > counts.get(SeaLanes.Size.SMALL), counts.toString());
        assertTrue(counts.get(SeaLanes.Size.SMALL) > counts.get(SeaLanes.Size.LARGE), counts.toString());
        assertTrue(counts.get(SeaLanes.Size.LARGE) > counts.get(SeaLanes.Size.XLARGE), counts.toString());
    }
}
