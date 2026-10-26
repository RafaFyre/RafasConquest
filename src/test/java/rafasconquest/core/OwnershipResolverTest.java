package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnershipResolverTest {
    private static final UUID A = new UUID(0, 1);
    private static final UUID B = new UUID(0, 2);

    private static OwnershipResolver.Candidate empire(UUID id, int x, int z, int radius, int strength, long tieHash) {
        return new OwnershipResolver.Candidate(id, x, z, radius, strength, tieHash);
    }

    @Test
    void unownedWhenNoEmpireCovers() {
        assertEquals(Optional.empty(), OwnershipResolver.resolve(5000, 0, List.of(empire(A, 0, 0, 3, 1000, 0))));
    }

    @Test
    void singleCoveringEmpireOwns() {
        assertEquals(Optional.of(A), OwnershipResolver.resolve(2500, 0, List.of(empire(A, 0, 0, 3, 1000, 0), empire(B, 9000, 0, 1, 5000, 0))));
    }

    @Test
    void radiusEdgeIsInclusive() {
        assertEquals(Optional.of(A), OwnershipResolver.resolve(3000, 0, List.of(empire(A, 0, 0, 3, 1000, 0))));
        assertEquals(Optional.empty(), OwnershipResolver.resolve(3001, 0, List.of(empire(A, 0, 0, 3, 1000, 0))));
    }

    @Test
    void highestStrengthWinsOverlap() {
        var weakNear = empire(A, 0, 0, 3, 800, 0);
        var strongFar = empire(B, 4000, 0, 3, 1200, 0);
        assertEquals(Optional.of(B), OwnershipResolver.resolve(1500, 0, List.of(weakNear, strongFar)));
        assertEquals(Optional.of(B), OwnershipResolver.resolve(1500, 0, List.of(strongFar, weakNear)));
    }

    @Test
    void strengthTieGoesToNearestCapital() {
        var near = empire(A, 0, 0, 3, 1000, 0);
        var far = empire(B, 4000, 0, 3, 1000, 0);
        assertEquals(Optional.of(A), OwnershipResolver.resolve(1500, 0, List.of(far, near)));
    }

    @Test
    void fullTieGoesToLowerPositionHash() {
        var lowHash = empire(A, -1000, 0, 3, 1000, -5);
        var highHash = empire(B, 1000, 0, 3, 1000, 7);
        assertEquals(Optional.of(A), OwnershipResolver.resolve(0, 0, List.of(highHash, lowHash)));
        assertEquals(Optional.of(A), OwnershipResolver.resolve(0, 0, List.of(lowHash, highHash)));
    }
}
