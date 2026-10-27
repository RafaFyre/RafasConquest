package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerritoryMapTest {
    private static final UUID BIG = new UUID(0, 1);
    private static final UUID SMALL = new UUID(0, 2);
    private static final UUID CITY = new UUID(0, 3);

    private final List<TerritoryMap.Claim> claims = List.of(
            new TerritoryMap.Claim(BIG, 0, 0, false, 3, 2000, 1),
            new TerritoryMap.Claim(SMALL, 2500, 0, false, 1, 500, 2),
            new TerritoryMap.Claim(CITY, -1000, 0, true, 0, 300, 3));
    private final List<OwnershipResolver.Candidate> empires = TerritoryMap.empires(claims);

    @Test
    void empiresFollowTheFarmOwnershipRule() {
        assertEquals(Optional.of(BIG), TerritoryMap.holder(500, 0, claims, empires));
        // Both radii cover x = 2200; the stronger Empire wins, as it would for a Farm there.
        assertEquals(Optional.of(BIG), TerritoryMap.holder(2200, 0, claims, empires));
        assertEquals(Optional.of(SMALL), TerritoryMap.holder(3400, 0, claims, empires));
        assertEquals(Optional.empty(), TerritoryMap.holder(5000, 0, claims, empires));
    }

    @Test
    void cityStateKeepsItsCircleInsideAnEmpire() {
        assertEquals(Optional.of(CITY), TerritoryMap.holder(-1000 + TerritoryMap.CITY_STATE_RADIUS, 0, claims, empires));
        assertEquals(Optional.of(BIG), TerritoryMap.holder(-1000 + TerritoryMap.CITY_STATE_RADIUS + 1, 0, claims, empires));
    }
}
