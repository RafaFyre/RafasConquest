package rafasconquest.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DialogueTest {
    /** A call that says a line, and its key (and the other key, when the key is chosen by a ternary). */
    private static final Pattern CALL = Pattern.compile(
            "(Dialogue\\.say|Dialogue\\.line|Wars\\.say|Politics\\.say|(?<![\\w.])say|(?<![\\w.])t)\\(([^\"\\n;]*?)\"([a-z_.]+)\"(\\s*:\\s*\"([a-z_.]+)\")?");
    /** Where a bare say(...) adds its file's prefix (WarCouncil's own say(...) is its speaker's line, its t(...) the text). */
    private static final Map<String, String> BARE = Map.of("RecruitsBattle.java", "battle.",
            "FieldBattle.java", "battle.", "Wars.java", "wars.", "Politics.java", "chronicle.");

    @Test
    void everyLineTheCodeSaysIsInTheShippedFiles() throws IOException {
        List<String> missing = new ArrayList<>();
        int checked = 0;
        Path project = Path.of("").toAbsolutePath();
        while (project != null && !Files.isDirectory(project.resolve("src/main/java/rafasconquest"))) {
            project = project.getParent(); // tests may run in the run folder
        }
        assertTrue(project != null, "no source folder above " + Path.of("").toAbsolutePath());
        try (Stream<Path> files = Files.walk(project.resolve("src/main/java/rafasconquest"))) {
            for (Path file : (Iterable<Path>) files.filter(f -> f.toString().endsWith(".java"))::iterator) {
                String name = file.getFileName().toString();
                Matcher m = CALL.matcher(Files.readString(file));
                while (m.find()) {
                    String prefix = switch (m.group(1)) {
                        case "Dialogue.say", "Dialogue.line" -> "";
                        case "Wars.say" -> "wars.";
                        case "Politics.say" -> "chronicle.";
                        case "t" -> name.equals("WarCouncil.java") ? "council." : null;
                        default -> BARE.get(name);
                    };
                    if (prefix == null) {
                        continue; // not a dialogue call in this file
                    }
                    for (String key : new String[]{m.group(3), m.group(5)}) {
                        if (key == null || key.endsWith(".") || key.endsWith("_")) {
                            continue; // a key made up as it is said: checked by hand
                        }
                        checked++;
                        String full = prefix + key;
                        if (Dialogue.say(full).equals(full)) {
                            missing.add(name + ": " + full);
                        }
                    }
                }
            }
        }
        assertTrue(checked > 150, "found only " + checked + " lines said: the search has gone wrong");
        assertEquals(List.of(), missing, "lines the code says that no dialogue file has");
    }

    @Test
    void aLineIsFilledIn() {
        assertEquals("Recruiter of Shukai", Dialogue.say("recruiter.name", "nation", "Shukai"));
        assertEquals(" on the left wing", Dialogue.say("battle.where.left"));
    }

    /** Barks.kind's keys, which the scan above can't see (built as "barks." + kind). */
    @Test
    void everyKindBarks() {
        for (String kind : List.of("recruiter", "noble", "townsfolk")) {
            assertTrue(!Dialogue.say("barks." + kind).startsWith("barks."), kind);
        }
    }
}
