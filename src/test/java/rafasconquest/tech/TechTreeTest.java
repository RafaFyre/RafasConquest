package rafasconquest.tech;

import org.junit.jupiter.api.Test;
import rafasconquest.core.Age;
import rafasconquest.tech.TechTree.Tech;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TechTreeTest {
    @Test
    void theShippedTreeHoldsTogether() {
        List<Tech> techs = TechTree.load();
        Map<String, Tech> byId = new HashMap<>();
        for (Tech t : techs) {
            assertNull(byId.put(t.id(), t), "two techs named " + t.id());
        }
        for (Age age : Age.values()) {
            assertTrue(techs.stream().anyMatch(t -> t.age() == age), "no techs in " + age);
        }
        for (Tech t : techs) {
            for (String need : t.needs()) {
                Tech n = byId.get(need);
                assertNotNull(n, t.id() + " needs " + need + ", which isn't a tech");
                assertTrue(n.age().compareTo(t.age()) <= 0, t.id() + " needs " + need + " from a later age");
            }
        }
        Tech crossing = byId.get("gunpowder_1");
        assertEquals(List.of("steel_10"), crossing.needs());
        assertEquals(9, crossing.x());
        assertEquals(57, crossing.y());
    }
}
