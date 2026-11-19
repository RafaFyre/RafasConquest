package rafasconquest.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckLiftTest {
    private static DistantShips.Far ship(double yaw, double fore, double port) {
        return new DistantShips.Far(10, 20, yaw, null, false, null, null, fore, port, 1, 0, 0);
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
}
