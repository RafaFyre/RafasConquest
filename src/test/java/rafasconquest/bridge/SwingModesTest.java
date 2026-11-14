package rafasconquest.bridge;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The presets SwingModes.cycle names exist, inherit their weapon, and swing twice. */
class SwingModesTest {
    @Test
    void presetsParse() throws IOException {
        for (String weapon : new String[] {"halberd", "glaive"}) {
            for (String mode : new String[] {"thrust", "sweep"}) {
                String file = "/data/rafasconquest/weapon_attributes/" + weapon + "_" + mode + ".json";
                JsonObject json = JsonParser.parseString(new String(SwingModesTest.class.getResourceAsStream(file).readAllBytes())).getAsJsonObject();
                assertEquals("bettercombat:" + weapon, json.get("parent").getAsString());
                assertEquals(2, json.getAsJsonObject("attributes").getAsJsonArray("attacks").size());
            }
        }
    }
}
