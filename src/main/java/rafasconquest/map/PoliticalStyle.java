package rafasconquest.map;

import rafasconquest.core.ProvinceMap;
import rafasconquest.core.SeedHash;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The look of the political map, in the manner of a grand-strategy game: a muted parchment world, realms filled in
 * nearly solid colour with thin lines between their provinces, a dark line round each realm and a soft shadow just
 * inside it, and names laid along each realm's length. Pure, so the same code draws the game's map and the test
 * previews. Colours are ARGB.
 */
public final class PoliticalStyle {
    public static final int PARCHMENT_LAND = 0xDCCBA4;
    public static final int PARCHMENT_SEA = 0x8FA9AB;
    private static final float PARCHMENT_MIX_LAND = 0.72F;
    private static final float PARCHMENT_MIX_SEA = 0.7F;

    private static final int FILL_ALPHA = 0xC8;
    private static final int REALM_EDGE = 0xF02B2420;
    /** Darkening of the band just inside a realm's edge, from the edge in. */
    private static final float[] INNER_SHADE = {0.72F, 0.84F, 0.93F};
    private static final float PROVINCE_LINE = 0.78F;
    private static final int UNCLAIMED_LINE = 0x40302820;

    /** The 16 dye colours by dye id (vanilla's banner and texture colours), so the style needs no game classes. */
    public static final int[] DYE_RGB = {
            0xF9FFFE, 0xF9801D, 0xC74EBD, 0x3AB3DA, 0xFED83D, 0x80C71F, 0xF38BAA, 0x474F52,
            0x9D9D97, 0x169C9C, 0x8932B8, 0x3C44AA, 0x835432, 0x5E7C16, 0xB02E26, 0x1D1D21};

    /** Least colour difference (RGB distance) wanted between neighbouring realms. */
    private static final double NEIGHBOUR_CONTRAST = 70;

    private PoliticalStyle() {
    }

    /** The bottom layer: the terrain washed toward parchment, keeping its relief and water depth faintly. */
    public static int[] parchment(BaseMap map) {
        int[] out = new int[map.pixels().length];
        for (int i = 0; i < out.length; i++) {
            int p = map.pixels()[i];
            if ((p >>> 24) == 0) {
                continue;
            }
            boolean water = map.isWater(i);
            int washed = MapColors.lerp(p & 0xFFFFFF, water ? PARCHMENT_SEA : PARCHMENT_LAND, water ? PARCHMENT_MIX_SEA : PARCHMENT_MIX_LAND);
            out[i] = 0xFF000000 | washed;
        }
        return out;
    }

    /** Which realm (index into the factions) holds each pixel, or -1. {@code provinceHolder} is per province. */
    public static int[] holders(BaseMap map, int[] provinceHolder) {
        int[] out = new int[map.provinces().length];
        for (int i = 0; i < out.length; i++) {
            int province = map.provinces()[i];
            out[i] = province >= 0 ? provinceHolder[province] : -1;
        }
        return out;
    }

    /** Pairs of realms that share a border, as {a, b} with a < b. */
    public static Set<Long> neighbours(int[] holders, int width, int height) {
        Set<Long> pairs = new HashSet<>();
        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                int a = holders[z * width + x];
                if (a < 0) {
                    continue;
                }
                if (x + 1 < width) {
                    addPair(pairs, a, holders[z * width + x + 1]);
                }
                if (z + 1 < height) {
                    addPair(pairs, a, holders[(z + 1) * width + x]);
                }
            }
        }
        return pairs;
    }

    private static void addPair(Set<Long> pairs, int a, int b) {
        if (b >= 0 && b != a) {
            pairs.add(((long) Math.min(a, b) << 32) | Math.max(a, b));
        }
    }

    /**
     * A colour per realm: its banner's main colour, softened to a map tone and varied a little per faction, then
     * adjusted where needed so no two neighbouring realms look alike. {@code ids} seed the variation.
     */
    public static int[] realmColors(int[] bannerRgb, long[] ids, Set<Long> neighbours) {
        int n = bannerRgb.length;
        int[] colors = new int[n];
        boolean[] done = new boolean[n];
        for (int i = 0; i < n; i++) {
            List<Integer> options = variants(bannerRgb[i], ids[i]);
            int best = options.get(0);
            double bestScore = -1;
            for (int option : options) {
                double nearest = Double.MAX_VALUE;
                for (int j = 0; j < n; j++) {
                    if (done[j] && neighbours.contains(((long) Math.min(i, j) << 32) | Math.max(i, j))) {
                        nearest = Math.min(nearest, distance(option, colors[j]));
                    }
                }
                // The first option (truest to the banner) wins unless it is too close to a neighbour.
                double score = Math.min(nearest, NEIGHBOUR_CONTRAST * 1.5);
                if (score > bestScore + 1) {
                    bestScore = score;
                    best = option;
                }
            }
            colors[i] = best;
            done[i] = true;
        }
        return colors;
    }

    /** Map tones for a banner colour, truest first: the faction's own shade, then lighter, darker and hue-shifted ones. */
    static List<Integer> variants(int rgb, long id) {
        float[] hsv = toHsv(rgb);
        long h = SeedHash.mix(id);
        float hue = hsv[0] + (float) (SeedHash.unit(h) - 0.5) * 0.05F;
        // Soften: strong dyes to a map pastel; greys (white, grey, black banners) stay grey.
        float sat = hsv[1] < 0.15F ? hsv[1] : 0.25F + hsv[1] * 0.45F;
        float val = 0.62F + (float) SeedHash.unit(SeedHash.mix(h)) * 0.22F;
        List<Integer> out = new ArrayList<>();
        out.add(fromHsv(hue, sat, val));
        for (float dv : new float[]{0.2F, -0.22F}) {
            out.add(fromHsv(hue, sat, Math.max(0.3F, Math.min(0.95F, val + dv))));
        }
        for (float dh : new float[]{0.07F, -0.07F, 0.14F, -0.14F}) {
            out.add(fromHsv(hue + dh, Math.max(sat, 0.3F), val));
            out.add(fromHsv(hue + dh, Math.max(sat, 0.3F), Math.max(0.3F, val - 0.2F)));
        }
        return out;
    }

    /**
     * The territory layer: realms filled with their colour, thin lines between provinces, a dark line round each realm
     * (coasts included) with a soft shadow inside it, and faint lines between unclaimed provinces.
     */
    public static int[] territory(BaseMap map, int[] holders, int[] realmColors) {
        int w = map.width();
        int h = map.height();
        int[] provinces = map.provinces();
        int[] edgeDistance = edgeDistance(holders, w, h, INNER_SHADE.length);
        int[] out = new int[w * h];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int i = z * w + x;
                int holder = holders[i];
                int province = provinces[i];
                if (province < 0) {
                    continue; // water, or beyond the known world
                }
                boolean provinceEdge = differs(provinces, w, h, x, z, province);
                if (holder < 0) {
                    out[i] = provinceEdge ? UNCLAIMED_LINE : 0;
                    continue;
                }
                int color = realmColors[holder];
                int d = edgeDistance[i];
                if (d == 0) {
                    out[i] = REALM_EDGE;
                } else if (d <= INNER_SHADE.length) {
                    out[i] = (0xE0 << 24) | MapColors.shade(color, INNER_SHADE[d - 1]);
                } else if (provinceEdge) {
                    out[i] = (0xD0 << 24) | MapColors.shade(color, PROVINCE_LINE);
                } else {
                    out[i] = (FILL_ALPHA << 24) | color;
                }
            }
        }
        return out;
    }

    /** True if a 4-neighbour of (x, z) has a different value (or is off the map). */
    private static boolean differs(int[] values, int w, int h, int x, int z, int value) {
        return x == 0 || z == 0 || x == w - 1 || z == h - 1
                || values[z * w + x - 1] != value || values[z * w + x + 1] != value
                || values[(z - 1) * w + x] != value || values[(z + 1) * w + x] != value;
    }

    /** Pixels from the edge of their realm: 0 on the edge, up to {@code max} + 1 for "further in". */
    private static int[] edgeDistance(int[] holders, int w, int h, int max) {
        int[] d = new int[w * h];
        java.util.Arrays.fill(d, max + 1);
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int i = z * w + x;
                if (holders[i] >= 0 && differs(holders, w, h, x, z, holders[i])) {
                    d[i] = 0;
                    queue.add(i);
                }
            }
        }
        while (!queue.isEmpty()) {
            int i = queue.poll();
            if (d[i] >= max) {
                continue;
            }
            int x = i % w;
            int z = i / w;
            int[][] steps = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] s : steps) {
                int nx = x + s[0];
                int nz = z + s[1];
                if (nx < 0 || nz < 0 || nx >= w || nz >= h) {
                    continue;
                }
                int j = nz * w + nx;
                if (holders[j] == holders[i] && d[j] > d[i] + 1) {
                    d[j] = d[i] + 1;
                    queue.add(j);
                }
            }
        }
        return d;
    }

    /**
     * Where a realm's name goes: the middle of its land, the direction it runs longest (radians, clockwise from east,
     * as on screen with north up), how long it is that way and how wide across, in blocks.
     */
    public record LabelShape(double centerX, double centerZ, double angle, double length, double width, int pixels) {
        /** True if the realm runs clearly longer one way than the other, so its name should follow it. */
        public boolean elongated() {
            return length > 1.3 * width;
        }
    }

    /** A label shape per realm (null where a realm holds no land on the map). */
    public static LabelShape[] labelShapes(BaseMap map, int[] holders, int realms) {
        double[] n = new double[realms];
        double[] sx = new double[realms];
        double[] sz = new double[realms];
        double[] sxx = new double[realms];
        double[] szz = new double[realms];
        double[] sxz = new double[realms];
        int bpp = map.blocksPerPixel();
        for (int z = 0; z < map.height(); z++) {
            for (int x = 0; x < map.width(); x++) {
                int r = holders[z * map.width() + x];
                if (r < 0) {
                    continue;
                }
                double wx = map.minX() + (x + 0.5) * bpp;
                double wz = map.minZ() + (z + 0.5) * bpp;
                n[r]++;
                sx[r] += wx;
                sz[r] += wz;
                sxx[r] += wx * wx;
                szz[r] += wz * wz;
                sxz[r] += wx * wz;
            }
        }
        LabelShape[] shapes = new LabelShape[realms];
        for (int r = 0; r < realms; r++) {
            if (n[r] == 0) {
                continue;
            }
            double mx = sx[r] / n[r];
            double mz = sz[r] / n[r];
            double vx = sxx[r] / n[r] - mx * mx;
            double vz = szz[r] / n[r] - mz * mz;
            double cxz = sxz[r] / n[r] - mx * mz;
            double angle = 0.5 * Math.atan2(2 * cxz, vx - vz);
            double root = Math.sqrt(((vx - vz) / 2) * ((vx - vz) / 2) + cxz * cxz);
            double major = (vx + vz) / 2 + root;
            double minor = (vx + vz) / 2 - root;
            // About 4 standard deviations covers a realm end to end.
            shapes[r] = new LabelShape(mx, mz, angle, 4 * Math.sqrt(Math.max(0, major)), 4 * Math.sqrt(Math.max(0, minor)), (int) n[r]);
        }
        return shapes;
    }

    private static double distance(int a, int b) {
        int dr = ((a >> 16) & 0xFF) - ((b >> 16) & 0xFF);
        int dg = ((a >> 8) & 0xFF) - ((b >> 8) & 0xFF);
        int db = (a & 0xFF) - (b & 0xFF);
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }

    static float[] toHsv(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255F;
        float g = ((rgb >> 8) & 0xFF) / 255F;
        float b = (rgb & 0xFF) / 255F;
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float d = max - min;
        float hue = 0;
        if (d > 0) {
            if (max == r) {
                hue = ((g - b) / d) / 6F;
            } else if (max == g) {
                hue = ((b - r) / d + 2) / 6F;
            } else {
                hue = ((r - g) / d + 4) / 6F;
            }
        }
        return new float[]{hue - (float) Math.floor(hue), max == 0 ? 0 : d / max, max};
    }

    static int fromHsv(float hue, float sat, float val) {
        hue -= (float) Math.floor(hue);
        sat = Math.max(0, Math.min(1, sat));
        val = Math.max(0, Math.min(1, val));
        float h6 = hue * 6;
        int sector = (int) h6 % 6;
        float f = h6 - (int) h6;
        float p = val * (1 - sat);
        float q = val * (1 - f * sat);
        float t = val * (1 - (1 - f) * sat);
        float r;
        float g;
        float b;
        switch (sector) {
            case 0 -> { r = val; g = t; b = p; }
            case 1 -> { r = q; g = val; b = p; }
            case 2 -> { r = p; g = val; b = t; }
            case 3 -> { r = p; g = q; b = val; }
            case 4 -> { r = t; g = p; b = val; }
            default -> { r = val; g = p; b = q; }
        }
        return (Math.round(r * 255) << 16) | (Math.round(g * 255) << 8) | Math.round(b * 255);
    }
}
