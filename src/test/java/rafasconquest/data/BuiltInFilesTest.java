package rafasconquest.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuiltInFilesTest {
    @TempDir
    Path dir;

    /** One game launch: the folder's record read, the jar's version of {@code name} kept, the record saved. */
    private BuiltInFiles.Outcome launch(String name, String jar) throws Exception {
        BuiltInFiles files = BuiltInFiles.in(dir);
        BuiltInFiles.Outcome outcome = files.keep(name, jar);
        files.save();
        return outcome;
    }

    @Test
    void anUneditedFileFollowsTheJarAndAnEditedOneIsKept() throws Exception {
        Path ship = dir.resolve("galley.json");
        assertEquals(BuiltInFiles.Outcome.WRITTEN, launch("galley.json", "v1"));
        assertEquals(BuiltInFiles.Outcome.SAME, launch("galley.json", "v1"));
        assertEquals(BuiltInFiles.Outcome.UPDATED, launch("galley.json", "v2"));
        assertEquals("v2", Files.readString(ship));
        Files.writeString(ship, "mine");
        assertEquals(BuiltInFiles.Outcome.EDITED_KEPT, launch("galley.json", "v3"));
        assertEquals("mine", Files.readString(ship));
        Files.delete(ship); // deleting it takes the jar's again
        assertEquals(BuiltInFiles.Outcome.WRITTEN, launch("galley.json", "v3"));
        assertEquals("v3", Files.readString(ship));
    }

    @Test
    void aFileFromBeforeRecordsIsReplacedAndKeptAsOld() throws Exception {
        Files.writeString(dir.resolve("large.nbt"), "old jar's");
        assertFalse(Files.exists(dir.resolve(BuiltInFiles.RECORD)));
        assertEquals(BuiltInFiles.Outcome.OLD_REPLACED, launch("large.nbt", "new jar's"));
        assertEquals("new jar's", Files.readString(dir.resolve("large.nbt")));
        assertEquals("old jar's", Files.readString(dir.resolve("large.nbt.old")));
        assertTrue(Files.exists(dir.resolve(BuiltInFiles.RECORD)));
        assertEquals(BuiltInFiles.Outcome.SAME, launch("large.nbt", "new jar's"));
    }
}
