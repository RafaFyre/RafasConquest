package rafasconquest.faction;

import rafasconquest.core.SeedHash;

import java.util.List;
import java.util.UUID;

/**
 * A faction's banner: a base colour and two pattern layers, derived from the faction's id. Deterministic, so it needs
 * no save data and every banner a faction owns looks the same.
 * <p>
 * Plain data on purpose (dye ids 0–15, vanilla pattern id paths), so it can be unit tested without the game; the
 * code that paints banners converts to DyeColor and registry keys.
 */
public record FactionBanner(int base, List<Layer> layers) {
    /** {@code pattern} is a vanilla banner pattern id path (e.g. "cross" for minecraft:cross); {@code color} a dye id. */
    public record Layer(String pattern, int color) {
    }

    /** Dye names in dye id order. */
    private static final String[] DYES = {
            "white", "orange", "magenta", "light blue", "yellow", "lime", "pink", "gray",
            "light gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

    /** Bold, readable shapes; first pick is a field division, second a charge on top. */
    private static final List<String> FIELDS = List.of(
            "stripe_center", "stripe_middle", "half_horizontal", "half_vertical", "diagonal_left", "diagonal_right",
            "stripe_downright", "stripe_downleft", "cross", "straight_cross", "triangle_top", "triangle_bottom",
            "stripe_top", "stripe_bottom");
    private static final List<String> CHARGES = List.of(
            "circle", "rhombus", "border", "flower", "square_top_left", "triangles_top", "curly_border", "gradient");

    public static FactionBanner of(UUID factionId) {
        long h = SeedHash.hash(factionId.getMostSignificantBits(), factionId.getLeastSignificantBits());
        int base = (int) Long.remainderUnsigned(h, DYES.length);
        return new FactionBanner(base, List.of(
                new Layer(FIELDS.get((int) Long.remainderUnsigned(h >>> 8, FIELDS.size())), otherColor(base, h >>> 16)),
                new Layer(CHARGES.get((int) Long.remainderUnsigned(h >>> 24, CHARGES.size())), otherColor(base, h >>> 32))));
    }

    /** A colour other than the base, so every layer shows. */
    private static int otherColor(int base, long bits) {
        return (base + 1 + (int) Long.remainderUnsigned(bits, DYES.length - 1)) % DYES.length;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder(DYES[base]);
        for (Layer layer : layers) {
            sb.append(", ").append(DYES[layer.color()]).append(' ').append(layer.pattern().replace('_', ' '));
        }
        return sb.toString();
    }
}
