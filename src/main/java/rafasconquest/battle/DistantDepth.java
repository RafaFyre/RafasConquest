package rafasconquest.battle;

/**
 * The game's camera sees only four times the render distance; anything further is cut off. A distant soldier is drawn
 * pulled in along the line of sight and shrunk by the same factor, which looks exactly the same on screen. Up to three
 * render distances nothing moves; beyond that, distances are squeezed into the last stretch before the cut-off, still
 * in order, so nearer soldiers are still drawn over further ones.
 */
public final class DistantDepth {
    /** The camera's far plane, in render distances (vanilla's {@code GameRenderer.getDepthFar}). */
    public static final double FAR = 4;
    /** Distances up to this many render distances are drawn as they are. */
    private static final double TRUE_UP_TO = 3;
    /** Squeezed distances approach this many render distances, short of the far plane. */
    private static final double SQUEEZED_LIMIT = 3.9;
    /** How quickly squeezed distances approach the limit, in render distances. */
    private static final double SQUEEZE_SCALE = 2;

    private DistantDepth() {
    }

    /** The distance to draw something {@code distance} blocks away at, for a render distance of {@code renderBlocks}. */
    public static double drawDistance(double distance, double renderBlocks) {
        double start = TRUE_UP_TO * renderBlocks;
        if (distance <= start) {
            return distance;
        }
        double room = (SQUEEZED_LIMIT - TRUE_UP_TO) * renderBlocks;
        double beyond = (distance - start) / (SQUEEZE_SCALE * renderBlocks);
        return start + room * beyond / (beyond + 1);
    }
}
