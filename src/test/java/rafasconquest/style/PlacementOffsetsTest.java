package rafasconquest.style;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlacementOffsetsTest {
    @Test
    void readsTablesAndPlainNumbersByName() {
        Map<String, Integer> o = StyleLibrary.readOffsets("""
                # comment
                "Example House" = { y = -10 }
                "tower.v2" = { Y = 3 }
                plain = 4
                """);
        assertEquals(Map.of("Example House", -10, "tower.v2", 3, "plain", 4), o);
    }
}
