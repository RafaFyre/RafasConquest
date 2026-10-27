package rafasconquest.map;

import rafasconquest.core.ProvinceMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * The world map as the server draws it: one terrain colour (ARGB) and one province per pixel, each pixel
 * {@code blocksPerPixel} blocks wide, starting at (minX, minZ). Pixels beyond {@code visibleRadius} (the edge of the
 * known world) are transparent and drawn as unexplored; {@code stormWallRadius} is where the storm wall stands.
 * {@code provinces} holds an index into {@code centres}, or {@link ProvinceMap#WATER} / {@link ProvinceMap#NONE}.
 */
public record BaseMap(int minX, int minZ, int blocksPerPixel, int width, int height, int visibleRadius, int stormWallRadius,
                      int[] pixels, int[] provinces, List<ProvinceMap.Centre> centres) {
    /** Raise when the drawing changes, so saved maps are redrawn. */
    private static final int FORMAT = 5;

    public boolean isWater(int index) {
        return provinces[index] == ProvinceMap.WATER;
    }

    /** Compressed form, used both for the save file and for sending to clients. */
    public byte[] encode() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(new DeflaterOutputStream(bytes))) {
            out.writeInt(FORMAT);
            out.writeInt(minX);
            out.writeInt(minZ);
            out.writeInt(blocksPerPixel);
            out.writeInt(width);
            out.writeInt(height);
            out.writeInt(visibleRadius);
            out.writeInt(stormWallRadius);
            for (int p : pixels) {
                out.writeInt(p);
            }
            for (int p : provinces) {
                out.writeShort(p);
            }
            out.writeInt(centres.size());
            for (ProvinceMap.Centre c : centres) {
                out.writeInt(c.x());
                out.writeInt(c.z());
                out.writeBoolean(c.city());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return bytes.toByteArray();
    }

    public static BaseMap decode(byte[] data) throws IOException {
        try (DataInputStream in = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(data)))) {
            if (in.readInt() != FORMAT) {
                throw new IOException("unknown base map format");
            }
            int minX = in.readInt();
            int minZ = in.readInt();
            int bpp = in.readInt();
            int width = in.readInt();
            int height = in.readInt();
            int visible = in.readInt();
            int stormWall = in.readInt();
            if (width <= 0 || height <= 0 || (long) width * height > 4_000_000) {
                throw new IOException("bad base map size " + width + "x" + height);
            }
            int[] pixels = new int[width * height];
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = in.readInt();
            }
            int[] provinces = new int[width * height];
            for (int i = 0; i < provinces.length; i++) {
                provinces[i] = in.readShort();
            }
            int n = in.readInt();
            if (n < 0 || n > Short.MAX_VALUE) {
                throw new IOException("bad province count " + n);
            }
            List<ProvinceMap.Centre> centres = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                centres.add(new ProvinceMap.Centre(in.readInt(), in.readInt(), in.readBoolean()));
            }
            return new BaseMap(minX, minZ, bpp, width, height, visible, stormWall, pixels, provinces, centres);
        }
    }
}
