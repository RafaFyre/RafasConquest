package rafasconquest.map;

import org.jetbrains.annotations.Nullable;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.Countryside;
import rafasconquest.core.SeatCities;
import rafasconquest.core.TitleMap;

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
 * {@code names} names each province; {@code titles} groups them into counties, duchies, kingdoms and empires, and
 * {@code cities} are their seat cities ({@link SeatCities}), {@code settlements} the hamlets, villages, watchtowers and
 * castles of the countryside ({@link Countryside}).
 */
public record BaseMap(int minX, int minZ, int blocksPerPixel, int width, int height, int visibleRadius, int stormWallRadius,
                      int[] pixels, int[] provinces, List<ProvinceMap.Centre> centres, List<String> names, TitleMap titles,
                      List<SeatCities.City> cities, List<Countryside.Settlement> settlements) {
    /** Raise when the drawing changes, so saved maps are redrawn. */
    private static final int FORMAT = 8;

    /** The map's provinces, for planning on. */
    public SeatCities.Grid grid() {
        return new SeatCities.Grid(minX, minZ, blocksPerPixel, width, height, provinces, centres);
    }

    /** A title's name: its tier and its seat province's name ("Duchy of Shukai"). */
    public String titleName(TitleMap.Tier tier, int seat) {
        return tier.title() + " of " + names.get(seat);
    }

    public boolean isWater(int index) {
        return provinces[index] == ProvinceMap.WATER;
    }

    /** The centre of the province at world (x, z), or null if that is off the map, at sea or in no province. */
    @Nullable
    public ProvinceMap.Centre provinceAt(int x, int z) {
        int px = Math.floorDiv(x - minX, blocksPerPixel);
        int pz = Math.floorDiv(z - minZ, blocksPerPixel);
        if (px < 0 || pz < 0 || px >= width || pz >= height) {
            return null;
        }
        int province = provinces[pz * width + px];
        return province >= 0 ? centres.get(province) : null;
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
            for (String name : names) {
                out.writeUTF(name);
            }
            for (int[] level : new int[][]{titles.county(), titles.duchy(), titles.kingdom(), titles.empire(),
                    titles.countySeat(), titles.duchySeat(), titles.kingdomSeat(), titles.empireSeat()}) {
                writeShorts(out, level);
            }
            out.writeInt(cities.size());
            for (SeatCities.City c : cities) {
                out.writeInt(c.x());
                out.writeInt(c.z());
                out.writeByte(c.districts());
                out.writeByte(c.tier().ordinal());
                out.writeShort(c.province());
            }
            out.writeInt(settlements.size());
            for (Countryside.Settlement s : settlements) {
                out.writeInt(s.x());
                out.writeInt(s.z());
                out.writeByte(s.kind().ordinal());
                out.writeShort(s.province());
                out.writeByte(s.hamlets());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return bytes.toByteArray();
    }

    private static void writeShorts(DataOutputStream out, int[] values) throws IOException {
        out.writeInt(values.length);
        for (int v : values) {
            out.writeShort(v);
        }
    }

    private static int[] readShorts(DataInputStream in) throws IOException {
        int n = in.readInt();
        if (n < 0 || n > Short.MAX_VALUE) {
            throw new IOException("bad title count " + n);
        }
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = in.readShort();
        }
        return values;
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
            List<String> names = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                names.add(in.readUTF());
            }
            int[][] levels = new int[8][];
            for (int i = 0; i < levels.length; i++) {
                levels[i] = readShorts(in);
            }
            TitleMap titles = new TitleMap(levels[0], levels[1], levels[2], levels[3], levels[4], levels[5], levels[6], levels[7]);
            int cityCount = in.readInt();
            if (cityCount < 0 || cityCount > Short.MAX_VALUE) {
                throw new IOException("bad city count " + cityCount);
            }
            List<SeatCities.City> cities = new ArrayList<>(cityCount);
            for (int i = 0; i < cityCount; i++) {
                cities.add(new SeatCities.City(in.readInt(), in.readInt(), in.readByte(), TitleMap.Tier.values()[in.readByte()],
                        in.readShort()));
            }
            int settlementCount = in.readInt();
            if (settlementCount < 0 || settlementCount > Short.MAX_VALUE) {
                throw new IOException("bad settlement count " + settlementCount);
            }
            List<Countryside.Settlement> settlements = new ArrayList<>(settlementCount);
            for (int i = 0; i < settlementCount; i++) {
                settlements.add(new Countryside.Settlement(in.readInt(), in.readInt(), Countryside.Kind.values()[in.readByte()],
                        in.readShort(), in.readByte()));
            }
            return new BaseMap(minX, minZ, bpp, width, height, visible, stormWall, pixels, provinces, centres, names, titles, cities,
                    settlements);
        }
    }
}
