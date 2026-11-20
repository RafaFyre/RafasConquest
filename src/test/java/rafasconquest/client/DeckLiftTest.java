package rafasconquest.client;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.core.SeaLanes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckLiftTest {
    private static DistantShips.Far ship(double yaw, double fore, double port) {
        return new DistantShips.Far(10, 20, yaw, null, false, null, null, fore, port, 1, 0, 0, null);
    }

    @Test
    void figuresRideTheDeckAsItPitchesAndRolls() {
        // Bow to +z: 2 ahead rises 2 × fore; 3 to port (+x) rises 3 × port.
        assertEquals(1.2, ship(0, 0.1, 0).deckLift(10, 22), 1e-9);
        assertEquals(1.3, ship(0, 0, 0.1).deckLift(13, 20), 1e-9);
        // Turned a quarter, bow to +x: 2 along +x is ahead; port is now -z.
        assertEquals(1.2, ship(Math.PI / 2, 0.1, 0).deckLift(12, 20), 1e-9);
        assertEquals(1.3, ship(Math.PI / 2, 0, 0.1).deckLift(10, 17), 1e-9);
        assertEquals(1, ship(0, 0.1, 0.1).deckLift(10, 20), 1e-9); // the middle only heaves
    }

    @Test
    void figuresAboardSailWithTheShipNotTheirLateFormation() {
        // The formation is drawn at (0, 0) bow to +z; the real ship is already 5 blocks on and come round bow to +x.
        SeaLanes.Size size = SeaLanes.Size.LARGE;
        FormationSnapshot.Ship hull = new FormationSnapshot.Ship(0, 0, size.ordinal());
        DistantShips.Want want = new DistantShips.Want(1, 0, 0, 0, size, null, hull);
        DistantShips.Far real = new DistantShips.Far(5, 0, Math.PI / 2, size, true, null, hull, 0, 0, 1, 0, 0, want);
        FormationSnapshot formation = new FormationSnapshot(1, FormationSnapshot.Kind.LEVY, 0, 0, 1, 0, 64, 0, 0, 1, 1, 1,
                false, false, new int[0], -1, hull);
        List<FigureMotion.Placed> figures = new ArrayList<>(List.of(new FigureMotion.Placed(0, 64, 2, 0, 1, false, false, 0, 0)));
        DistantShips.rideDecks(List.of(formation), List.of(figures), List.of(real));
        FigureMotion.Placed p = figures.get(0);
        assertEquals(7, p.x(), 1e-9, "2 ahead of the middle, ahead now being +x");
        assertEquals(0, p.z(), 1e-9);
        assertEquals(65, p.y(), 1e-9, "lifted as the deck lies");
        assertEquals(1, p.fx(), 1e-9, "facing the bow as it is now");
        assertEquals(0, p.fz(), 1e-9);
    }
}
