package rafasconquest.map;

import rafasconquest.core.TitleMap;

/**
 * How the titles are drawn on the world map: a border between neighbouring provinces as strong as the highest title
 * they don't share: a faint line between counties, darker between duchies, a double line between kingdoms, and a
 * heavy dark red one between empires. One ARGB pixel per map pixel, transparent elsewhere.
 */
public final class TitleStyle {
    static final int COUNTY = 0x50402A1A;
    static final int DUCHY = 0x9A402A1A;
    static final int KINGDOM = 0xD0402A1A;
    static final int EMPIRE = 0xF0701818;

    private TitleStyle() {
    }

    public static int[] borders(BaseMap map) {
        int w = map.width();
        int h = map.height();
        int[] provinces = map.provinces();
        TitleMap t = map.titles();
        int[] out = new int[w * h];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int i = z * w + x;
                int p = provinces[i];
                if (p < 0) {
                    continue;
                }
                // The strongest border with a neighbour to the right or below; kingdoms and empires draw on both sides.
                int strongest = 0;
                for (int[] d : new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}}) {
                    int nx = x + d[0];
                    int nz = z + d[1];
                    if (nx < 0 || nz < 0 || nx >= w || nz >= h) {
                        continue;
                    }
                    int q = provinces[nz * w + nx];
                    if (q < 0 || q == p) {
                        continue;
                    }
                    int level = level(t, p, q);
                    boolean ownSide = d[0] > 0 || d[1] > 0;
                    if (level >= 3 || ownSide) {
                        strongest = Math.max(strongest, level);
                    }
                }
                out[i] = switch (strongest) {
                    case 1 -> COUNTY;
                    case 2 -> DUCHY;
                    case 3 -> KINGDOM;
                    case 4 -> EMPIRE;
                    default -> 0;
                };
            }
        }
        return out;
    }

    /** 1 if two provinces are in different counties, 2 different duchies, 3 kingdoms, 4 empires; 0 the same county. */
    static int level(TitleMap t, int p, int q) {
        int cp = t.county()[p];
        int cq = t.county()[q];
        if (cp == cq) {
            return 0;
        }
        int dp = t.duchy()[cp];
        int dq = t.duchy()[cq];
        if (dp != dq) {
            int kp = t.kingdom()[dp];
            int kq = t.kingdom()[dq];
            if (kp != kq) {
                return t.empire()[kp] != t.empire()[kq] ? 4 : 3;
            }
            return 2;
        }
        return 1;
    }
}
