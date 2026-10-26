package rafasconquest.core;

/**
 * Empire rolls grow with distance from spawn: storm-wall bounds up to R2, far bounds from R2 + offset onwards,
 * and bounds interpolated linearly in between.
 */
public final class DistanceScaling {
    private DistanceScaling() {
    }

    public record Bounds(int radiusMin, int radiusMax, int strengthMin, int strengthMax) {
    }

    public static Bounds boundsAt(double distanceFromSpawn, WorldSettings s) {
        double t = (distanceFromSpawn - s.warZoneOuterEdge()) / Math.max(1, s.farDistanceOffset());
        t = Math.max(0.0, Math.min(1.0, t));
        return new Bounds(
                lerp(s.stormWallRadiusMin(), s.farRadiusMin(), t),
                lerp(s.stormWallRadiusMax(), s.farRadiusMax(), t),
                lerp(s.stormWallStrengthMin(), s.farStrengthMin(), t),
                lerp(s.stormWallStrengthMax(), s.farStrengthMax(), t));
    }

    private static int lerp(int a, int b, double t) {
        return (int) Math.round(a + (b - a) * t);
    }
}
