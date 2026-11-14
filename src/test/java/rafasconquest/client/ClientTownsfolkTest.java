package rafasconquest.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTownsfolkTest {
    @Test
    void walksOutThenBackThenOutAgain() {
        assertEquals(0, ClientTownsfolk.along(0, 10), 1e-9);
        assertEquals(4, ClientTownsfolk.along(4, 10), 1e-9);
        assertEquals(-8, ClientTownsfolk.along(12, 10), 1e-9); // turned at 10, walking back
        assertEquals(3, ClientTownsfolk.along(23, 10), 1e-9);
        assertEquals(0, ClientTownsfolk.along(5, 0), 1e-9);
    }
}
