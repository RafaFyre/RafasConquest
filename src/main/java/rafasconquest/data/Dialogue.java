package rafasconquest.data;

import com.electronwill.nightconfig.core.Config;
import rafasconquest.RafasConquest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * What characters say, from {@code dialogue/*.toml}. A line's key is its file and its place in the file:
 * {@code greet} in {@code noble.toml} is {@code noble.greet}, {@code ruin} under {@code [targets]} in {@code quests.toml}
 * is {@code quests.targets.ruin}. A line is a text, or a list of texts of which one is said at random; {@code {name}}
 * in it is filled in by the code that says it.
 */
public final class Dialogue {
    private static volatile Map<String, List<String>> lines;

    private Dialogue() {
    }

    static int load() {
        Map<String, List<String>> read = new HashMap<>();
        DataFiles.each("dialogue", (id, config) -> flatten(id.replace('/', '.'), config, read));
        lines = read;
        return read.size();
    }

    private static void flatten(String prefix, Config config, Map<String, List<String>> out) {
        for (Config.Entry e : config.entrySet()) {
            String key = prefix + "." + e.getKey();
            Object v = e.getValue();
            if (v instanceof Config table) {
                flatten(key, table, out);
            } else if (v instanceof List<?> list && !list.isEmpty()) {
                out.put(key, list.stream().map(String::valueOf).toList());
            } else if (v instanceof String s) {
                out.put(key, List.of(s));
            } else {
                RafasConquest.LOGGER.warn("[data] Dialogue {} is neither a text nor a list of texts: {}", key, v);
            }
        }
    }

    /** A line, one of its texts at random, filled in from name, value pairs. The key itself if no file has it. */
    public static String say(String key, Object... vars) {
        return line(ThreadLocalRandom.current(), key, vars);
    }

    /** The same, choosing with this random (so a quest board is the same for everyone). */
    public static String line(Random random, String key, Object... vars) {
        if (lines == null) {
            load(); // said before the server read the files (a test, or the world map's preview)
        }
        List<String> texts = lines.get(key);
        return texts == null ? key : fill(texts.get(random.nextInt(texts.size())), vars);
    }

    public static String fill(String text, Object... vars) {
        for (int i = 0; i + 1 < vars.length; i += 2) {
            text = text.replace("{" + vars[i] + "}", String.valueOf(vars[i + 1]));
        }
        return text;
    }
}
