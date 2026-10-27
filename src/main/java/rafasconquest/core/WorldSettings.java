package rafasconquest.core;

import net.minecraft.nbt.CompoundTag;

/**
 * The frozen copy of the starting values. Taken from config when a world is created, then always read from save data,
 * so later config edits only affect new worlds.
 */
public record WorldSettings(
        int eyeRadius,
        int warZoneOuterEdge,
        int ringCapitalCount,
        int ringDistance,
        int ringJitter,
        int outerCellSize,
        int outerCellMargin,
        int farmSpacingChunks,
        int farmSeparationChunks,
        int farmDailyOutput,
        int stormWallRadiusMin,
        int stormWallRadiusMax,
        int stormWallStrengthMin,
        int stormWallStrengthMax,
        int farRadiusMin,
        int farRadiusMax,
        int farStrengthMin,
        int farStrengthMax,
        int farDistanceOffset,
        int maxCatchUpDays,
        int initialPrecomputeRadius,
        int lazyPrecomputeDistance,
        int cityCellSize,
        int cityCellMargin,
        int cityStrengthMin,
        int cityStrengthMax,
        int cityDailyOutput) {

    public static final int REGION_SIZE = 2048;
    public static final int RADIUS_STEP_BLOCKS = 1000;
    public static final int MAX_RADIUS_STEPS = 10;
    public static final int MAX_STRENGTH = 400_000;
    /** Capitals farther than this can never cover a POI (MAX_RADIUS_STEPS × RADIUS_STEP_BLOCKS). */
    public static final int CAPITAL_LOOKUP_RANGE = MAX_RADIUS_STEPS * RADIUS_STEP_BLOCKS;

    public static WorldSettings defaults() {
        return new WorldSettings(1000, 4000, 6, 2500, 400, 8000, 1000, 32, 12, 5,
                1, 3, 300, 1500, 6, 10, 3500, 5000, 20000, 0, 8000, 4000,
                3000, 400, 300, 800, 0);
    }

    public boolean citiesEnabled() {
        return cityCellSize > 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("eyeRadius", eyeRadius);
        tag.putInt("warZoneOuterEdge", warZoneOuterEdge);
        tag.putInt("ringCapitalCount", ringCapitalCount);
        tag.putInt("ringDistance", ringDistance);
        tag.putInt("ringJitter", ringJitter);
        tag.putInt("outerCellSize", outerCellSize);
        tag.putInt("outerCellMargin", outerCellMargin);
        tag.putInt("farmSpacingChunks", farmSpacingChunks);
        tag.putInt("farmSeparationChunks", farmSeparationChunks);
        tag.putInt("farmDailyOutput", farmDailyOutput);
        tag.putInt("stormWallRadiusMin", stormWallRadiusMin);
        tag.putInt("stormWallRadiusMax", stormWallRadiusMax);
        tag.putInt("stormWallStrengthMin", stormWallStrengthMin);
        tag.putInt("stormWallStrengthMax", stormWallStrengthMax);
        tag.putInt("farRadiusMin", farRadiusMin);
        tag.putInt("farRadiusMax", farRadiusMax);
        tag.putInt("farStrengthMin", farStrengthMin);
        tag.putInt("farStrengthMax", farStrengthMax);
        tag.putInt("farDistanceOffset", farDistanceOffset);
        tag.putInt("maxCatchUpDays", maxCatchUpDays);
        tag.putInt("initialPrecomputeRadius", initialPrecomputeRadius);
        tag.putInt("lazyPrecomputeDistance", lazyPrecomputeDistance);
        tag.putInt("cityCellSize", cityCellSize);
        tag.putInt("cityCellMargin", cityCellMargin);
        tag.putInt("cityStrengthMin", cityStrengthMin);
        tag.putInt("cityStrengthMax", cityStrengthMax);
        tag.putInt("cityDailyOutput", cityDailyOutput);
        return tag;
    }

    /** Worlds created before cities existed have no city keys; they load with cities disabled (cell size 0). */

    public static WorldSettings load(CompoundTag tag) {
        return new WorldSettings(
                tag.getInt("eyeRadius"),
                tag.getInt("warZoneOuterEdge"),
                tag.getInt("ringCapitalCount"),
                tag.getInt("ringDistance"),
                tag.getInt("ringJitter"),
                tag.getInt("outerCellSize"),
                tag.getInt("outerCellMargin"),
                tag.getInt("farmSpacingChunks"),
                tag.getInt("farmSeparationChunks"),
                tag.getInt("farmDailyOutput"),
                tag.getInt("stormWallRadiusMin"),
                tag.getInt("stormWallRadiusMax"),
                tag.getInt("stormWallStrengthMin"),
                tag.getInt("stormWallStrengthMax"),
                tag.getInt("farRadiusMin"),
                tag.getInt("farRadiusMax"),
                tag.getInt("farStrengthMin"),
                tag.getInt("farStrengthMax"),
                tag.getInt("farDistanceOffset"),
                tag.getInt("maxCatchUpDays"),
                tag.getInt("initialPrecomputeRadius"),
                tag.getInt("lazyPrecomputeDistance"),
                tag.getInt("cityCellSize"),
                tag.getInt("cityCellMargin"),
                tag.getInt("cityStrengthMin"),
                tag.getInt("cityStrengthMax"),
                tag.getInt("cityDailyOutput"));
    }
}
