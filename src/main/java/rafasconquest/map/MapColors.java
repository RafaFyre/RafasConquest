package rafasconquest.map;

/** Colour arithmetic for the map, on RGB ints. Pure, so map styling works without the game. */
public final class MapColors {
    private MapColors() {
    }

    public static int lerp(int a, int b, float t) {
        int r = (int) (((a >> 16) & 0xFF) + (((b >> 16) & 0xFF) - ((a >> 16) & 0xFF)) * t);
        int g = (int) (((a >> 8) & 0xFF) + (((b >> 8) & 0xFF) - ((a >> 8) & 0xFF)) * t);
        int bl = (int) ((a & 0xFF) + ((b & 0xFF) - (a & 0xFF)) * t);
        return (r << 16) | (g << 8) | bl;
    }

    /** Brightens (factor > 1) or darkens (factor < 1) a colour. */
    public static int shade(int color, float factor) {
        int r = Math.min(255, Math.max(0, (int) (((color >> 16) & 0xFF) * factor)));
        int g = Math.min(255, Math.max(0, (int) (((color >> 8) & 0xFF) * factor)));
        int b = Math.min(255, Math.max(0, (int) ((color & 0xFF) * factor)));
        return (r << 16) | (g << 8) | b;
    }
}
