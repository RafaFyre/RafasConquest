package rafasconquest.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 * The files the mod writes out of its jar for players to edit (unit, engine and ship cards, ship hulls), kept up to
 * date with the jar: each folder's {@value #RECORD} holds a hash of every file as the mod last wrote it. A file still
 * as written is replaced when the jar's own changes; one a player has edited is kept (logged: delete it for the new
 * one); one written before there were records is replaced, the old one kept beside it as {@code .old}.
 */
public final class BuiltInFiles {
    static final String RECORD = ".written.json";
    private static final Gson GSON = new Gson();

    private final Path dir;
    private final Map<String, String> written;
    private boolean changed;

    private BuiltInFiles(Path dir, Map<String, String> written) {
        this.dir = dir;
        this.written = written;
    }

    /** The folder {@code dir}'s record (none yet: empty). */
    public static BuiltInFiles in(Path dir) {
        Map<String, String> written = new TreeMap<>();
        try {
            Path record = dir.resolve(RECORD);
            if (Files.exists(record)) {
                Map<String, String> read = GSON.fromJson(Files.readString(record), new TypeToken<Map<String, String>>() {
                }.getType());
                if (read != null) {
                    written.putAll(read);
                }
            }
        } catch (IOException | RuntimeException e) {
            rafasconquest.RafasConquest.LOGGER.warn("Built-in files: couldn't read {}; its files count as unrecorded", dir.resolve(RECORD), e);
        }
        return new BuiltInFiles(dir, written);
    }

    /** What happened to a file ({@link #keep}). */
    public enum Outcome { WRITTEN, UPDATED, SAME, EDITED_KEPT, OLD_REPLACED }

    /** File {@code name} brought up to the jar's {@code fresh} bytes, unless a player has edited it. */
    public Outcome keep(String name, byte[] fresh) throws IOException {
        Path file = dir.resolve(name);
        String want = hash(fresh);
        if (!Files.exists(file)) {
            Files.write(file, fresh);
            record(name, want);
            return Outcome.WRITTEN;
        }
        String now = hash(Files.readAllBytes(file));
        if (now.equals(want)) {
            record(name, want);
            return Outcome.SAME;
        }
        String last = written.get(name);
        if (last == null) {
            Files.copy(file, dir.resolve(name + ".old"), StandardCopyOption.REPLACE_EXISTING);
            Files.write(file, fresh);
            record(name, want);
            rafasconquest.RafasConquest.LOGGER.info("Built-in files: {} replaced by this version's; the one before is {}.old", file, name);
            return Outcome.OLD_REPLACED;
        }
        if (last.equals(now)) {
            Files.write(file, fresh);
            record(name, want);
            rafasconquest.RafasConquest.LOGGER.info("Built-in files: {} updated to this version's", file);
            return Outcome.UPDATED;
        }
        rafasconquest.RafasConquest.LOGGER.info("Built-in files: {} was edited, so it is kept; delete it to get this version's", file);
        return Outcome.EDITED_KEPT;
    }

    /** {@link #keep} for text. */
    public Outcome keep(String name, String fresh) throws IOException {
        return keep(name, fresh.getBytes(StandardCharsets.UTF_8));
    }

    private void record(String name, String hash) {
        if (!hash.equals(written.put(name, hash))) {
            changed = true;
        }
    }

    /** Writes the folder's record, if anything in it changed. */
    public void save() {
        if (!changed) {
            return;
        }
        try {
            Files.writeString(dir.resolve(RECORD), GSON.toJson(written));
            changed = false;
        } catch (IOException e) {
            rafasconquest.RafasConquest.LOGGER.warn("Built-in files: couldn't write {}", dir.resolve(RECORD), e);
        }
    }

    static String hash(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
