package rafasconquest.tech;

import com.electronwill.nightconfig.core.Config;
import rafasconquest.RafasConquest;
import rafasconquest.core.Age;
import rafasconquest.data.DataFiles;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The techs nations research, from {@code mods/RafasConquest/techs}: one file per age (bronze.toml, steel.toml,
 * gunpowder.toml, steam.toml), each tech placed by hand in its age's column. A file of the player's own replaces the
 * mod's for that age.
 */
public final class TechTree {
    public static final String FOLDER = "techs";

    /**
     * A tech: where it is drawn ({@code x} across and {@code y} down its age's column, 0 to 100) and the techs that must
     * be known first, from its own age or an earlier one.
     */
    public record Tech(String id, Age age, String name, int x, int y, List<String> needs) {
    }

    private TechTree() {
    }

    /** Every tech, age by age in order. */
    public static List<Tech> load() {
        Map<Age, List<Tech>> byAge = new EnumMap<>(Age.class);
        DataFiles.each(FOLDER, (file, c) -> {
            Age age = Age.parse(file, null);
            if (age == null) {
                RafasConquest.LOGGER.warn("[techs] {}.toml isn't named after an age (bronze, steel, gunpowder, steam); skipped", file);
                return;
            }
            List<Tech> techs = new ArrayList<>();
            if (c.get("tech") instanceof List<?> entries) {
                for (Object entry : entries) {
                    if (entry instanceof Config t && t.get("id") instanceof String id) {
                        String name = t.get("name") instanceof String n ? n : id;
                        List<String> needs = new ArrayList<>();
                        if (t.get("needs") instanceof List<?> list) {
                            list.forEach(need -> needs.add(String.valueOf(need)));
                        }
                        techs.add(new Tech(id, age, name, percent(t.get("x")), percent(t.get("y")), List.copyOf(needs)));
                    }
                }
            }
            byAge.put(age, techs); // the player's file comes after the mod's, so it wins
        });
        return byAge.values().stream().flatMap(List::stream).toList();
    }

    private static int percent(Object v) {
        return v instanceof Number n ? Math.max(0, Math.min(100, n.intValue())) : 0;
    }
}
