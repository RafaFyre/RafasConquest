package rafasconquest.client.map;

import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.map.BaseMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** The base map received from the server, kept for the rest of the session so reopening the map is instant. */
public final class ClientMapCache {
    @Nullable
    private static byte[][] parts;
    @Nullable
    private static BaseMap map;

    private ClientMapCache() {
    }

    @Nullable
    public static BaseMap map() {
        return map;
    }

    /** Returns true once the last part has arrived and the map is decoded. */
    public static boolean accept(int index, int count, byte[] bytes) {
        if (parts == null || parts.length != count) {
            parts = new byte[count][];
        }
        if (index < 0 || index >= count) {
            return false;
        }
        parts[index] = bytes;
        ByteArrayOutputStream all = new ByteArrayOutputStream();
        for (byte[] part : parts) {
            if (part == null) {
                return false;
            }
            all.writeBytes(part);
        }
        parts = null;
        try {
            map = BaseMap.decode(all.toByteArray());
            return true;
        } catch (IOException e) {
            RafasConquest.LOGGER.error("Could not read the world map from the server", e);
            return false;
        }
    }

    /** On leaving a world. */
    public static void clear() {
        parts = null;
        map = null;
    }
}
