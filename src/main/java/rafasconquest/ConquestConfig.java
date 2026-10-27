package rafasconquest;

import net.minecraftforge.common.ForgeConfigSpec;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;

/**
 * Starting values. These are copied into a world's save data when it is created and read from there afterwards,
 * so edits here only affect new worlds.
 */
public final class ConquestConfig {
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.IntValue EYE_RADIUS;
    private static final ForgeConfigSpec.IntValue WAR_ZONE_OUTER_EDGE;
    private static final ForgeConfigSpec.IntValue RING_CAPITAL_COUNT;
    private static final ForgeConfigSpec.IntValue RING_DISTANCE;
    private static final ForgeConfigSpec.IntValue RING_JITTER;
    private static final ForgeConfigSpec.IntValue OUTER_CELL_SIZE;
    private static final ForgeConfigSpec.IntValue OUTER_CELL_MARGIN;
    private static final ForgeConfigSpec.IntValue FARM_SPACING;
    private static final ForgeConfigSpec.IntValue FARM_SEPARATION;
    private static final ForgeConfigSpec.IntValue FARM_OUTPUT;
    private static final ForgeConfigSpec.IntValue STORM_RADIUS_MIN;
    private static final ForgeConfigSpec.IntValue STORM_RADIUS_MAX;
    private static final ForgeConfigSpec.IntValue STORM_STRENGTH_MIN;
    private static final ForgeConfigSpec.IntValue STORM_STRENGTH_MAX;
    private static final ForgeConfigSpec.IntValue FAR_RADIUS_MIN;
    private static final ForgeConfigSpec.IntValue FAR_RADIUS_MAX;
    private static final ForgeConfigSpec.IntValue FAR_STRENGTH_MIN;
    private static final ForgeConfigSpec.IntValue FAR_STRENGTH_MAX;
    private static final ForgeConfigSpec.IntValue FAR_DISTANCE_OFFSET;
    private static final ForgeConfigSpec.IntValue MAX_CATCH_UP_DAYS;
    private static final ForgeConfigSpec.IntValue INITIAL_PRECOMPUTE_RADIUS;
    private static final ForgeConfigSpec.IntValue LAZY_PRECOMPUTE_DISTANCE;
    private static final ForgeConfigSpec.IntValue CITY_CELL_SIZE;
    private static final ForgeConfigSpec.IntValue CITY_CELL_MARGIN;
    private static final ForgeConfigSpec.IntValue CITY_STRENGTH_MIN;
    private static final ForgeConfigSpec.IntValue CITY_STRENGTH_MAX;
    private static final ForgeConfigSpec.IntValue CITY_OUTPUT;

    static {
        WorldSettings d = WorldSettings.defaults();
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("Starting values, frozen into each world's save data when the world is created.").push("world");

        b.push("zones");
        EYE_RADIUS = b.comment("Eye radius R1: no faction origins are placed within this many blocks of spawn.")
                .defineInRange("eyeRadius", d.eyeRadius(), 0, 100_000);
        WAR_ZONE_OUTER_EDGE = b.comment("War-zone outer edge R2, in blocks from spawn.")
                .defineInRange("warZoneOuterEdge", d.warZoneOuterEdge(), 1, 100_000);
        b.pop();

        b.push("capitals");
        RING_CAPITAL_COUNT = b.comment("Number of Capitals in the storm-wall ring.")
                .defineInRange("ringCapitalCount", d.ringCapitalCount(), 0, 64);
        RING_DISTANCE = b.comment("Distance of the storm-wall ring from spawn, in blocks.")
                .defineInRange("ringDistance", d.ringDistance(), 0, 100_000);
        RING_JITTER = b.comment("Maximum seeded distance jitter for ring Capitals, in blocks.")
                .defineInRange("ringJitter", d.ringJitter(), 0, 10_000);
        OUTER_CELL_SIZE = b.comment("Outer-world grid cell size in blocks; one Capital per cell beyond R2.")
                .defineInRange("outerCellSize", d.outerCellSize(), 512, 100_000);
        OUTER_CELL_MARGIN = b.comment("Outer-world Capitals stay at least this far inside their cell's edges, in blocks.")
                .defineInRange("outerCellMargin", d.outerCellMargin(), 0, 50_000);
        b.pop();

        b.push("farms");
        FARM_SPACING = b.comment("Farm random-spread spacing, in chunks.")
                .defineInRange("spacing", d.farmSpacingChunks(), 2, 4096);
        FARM_SEPARATION = b.comment("Farm random-spread separation, in chunks. Must be less than spacing.")
                .defineInRange("separation", d.farmSeparationChunks(), 0, 4095);
        FARM_OUTPUT = b.comment("Food a Farm produces per in-game day.")
                .defineInRange("dailyOutput", d.farmDailyOutput(), 0, 1_000_000);
        b.pop();

        b.push("cities");
        CITY_CELL_SIZE = b.comment("City grid cell size in blocks; at most one city (and its City-state) per cell. 0 disables cities.")
                .defineInRange("cellSize", d.cityCellSize(), 0, 100_000);
        CITY_CELL_MARGIN = b.comment("Cities stay at least this far inside their cell's edges, in blocks. Raised to " + CityLayout.HALF_EXTENT
                        + " if lower, so cities never overlap.")
                .defineInRange("cellMargin", d.cityCellMargin(), 0, 50_000);
        CITY_STRENGTH_MIN = b.defineInRange("strengthMin", d.cityStrengthMin(), 0, WorldSettings.MAX_STRENGTH);
        CITY_STRENGTH_MAX = b.defineInRange("strengthMax", d.cityStrengthMax(), 0, WorldSettings.MAX_STRENGTH);
        CITY_OUTPUT = b.comment("Food a city produces per in-game day for its City-state.")
                .defineInRange("dailyOutput", d.cityDailyOutput(), 0, 1_000_000);
        b.pop();

        b.push("empireRolls");
        STORM_RADIUS_MIN = b.defineInRange("stormWallRadiusMin", d.stormWallRadiusMin(), 1, WorldSettings.MAX_RADIUS_STEPS);
        STORM_RADIUS_MAX = b.defineInRange("stormWallRadiusMax", d.stormWallRadiusMax(), 1, WorldSettings.MAX_RADIUS_STEPS);
        STORM_STRENGTH_MIN = b.defineInRange("stormWallStrengthMin", d.stormWallStrengthMin(), 0, WorldSettings.MAX_STRENGTH);
        STORM_STRENGTH_MAX = b.defineInRange("stormWallStrengthMax", d.stormWallStrengthMax(), 0, WorldSettings.MAX_STRENGTH);
        FAR_RADIUS_MIN = b.defineInRange("farRadiusMin", d.farRadiusMin(), 1, WorldSettings.MAX_RADIUS_STEPS);
        FAR_RADIUS_MAX = b.defineInRange("farRadiusMax", d.farRadiusMax(), 1, WorldSettings.MAX_RADIUS_STEPS);
        FAR_STRENGTH_MIN = b.defineInRange("farStrengthMin", d.farStrengthMin(), 0, WorldSettings.MAX_STRENGTH);
        FAR_STRENGTH_MAX = b.defineInRange("farStrengthMax", d.farStrengthMax(), 0, WorldSettings.MAX_STRENGTH);
        FAR_DISTANCE_OFFSET = b.comment("Far rolls apply from R2 + this many blocks; bounds interpolate linearly before that.")
                .defineInRange("farDistanceOffset", d.farDistanceOffset(), 1, 1_000_000);
        b.pop();

        b.push("simulation");
        MAX_CATCH_UP_DAYS = b.comment("Cap on offline catch-up days when the PassIngameTimeForIRLTime gamerule is on. 0 = no cap.")
                .defineInRange("maxCatchUpDays", d.maxCatchUpDays(), 0, Integer.MAX_VALUE);
        INITIAL_PRECOMPUTE_RADIUS = b.comment("POIs within this many blocks of spawn are precomputed on server start.")
                .defineInRange("initialPrecomputeRadius", d.initialPrecomputeRadius(), 0, 100_000);
        LAZY_PRECOMPUTE_DISTANCE = b.comment("Regions are precomputed once a player comes within this many blocks of them.")
                .defineInRange("lazyPrecomputeDistance", d.lazyPrecomputeDistance(), 0, 100_000);
        b.pop();

        b.pop();
        SPEC = b.build();
    }

    private ConquestConfig() {
    }

    public static WorldSettings snapshot() {
        int spacing = FARM_SPACING.get();
        int cityMargin = Math.max(CITY_CELL_MARGIN.get(), CityLayout.HALF_EXTENT);
        // A cell must fit a whole city inside its margins.
        int cityCell = CITY_CELL_SIZE.get() == 0 ? 0 : Math.max(CITY_CELL_SIZE.get(), 2 * cityMargin + 1);
        return new WorldSettings(
                EYE_RADIUS.get(), WAR_ZONE_OUTER_EDGE.get(),
                RING_CAPITAL_COUNT.get(), RING_DISTANCE.get(), RING_JITTER.get(),
                OUTER_CELL_SIZE.get(), OUTER_CELL_MARGIN.get(),
                spacing, Math.min(FARM_SEPARATION.get(), spacing - 1), FARM_OUTPUT.get(),
                STORM_RADIUS_MIN.get(), Math.max(STORM_RADIUS_MIN.get(), STORM_RADIUS_MAX.get()),
                STORM_STRENGTH_MIN.get(), Math.max(STORM_STRENGTH_MIN.get(), STORM_STRENGTH_MAX.get()),
                FAR_RADIUS_MIN.get(), Math.max(FAR_RADIUS_MIN.get(), FAR_RADIUS_MAX.get()),
                FAR_STRENGTH_MIN.get(), Math.max(FAR_STRENGTH_MIN.get(), FAR_STRENGTH_MAX.get()),
                FAR_DISTANCE_OFFSET.get(), MAX_CATCH_UP_DAYS.get(),
                INITIAL_PRECOMPUTE_RADIUS.get(), LAZY_PRECOMPUTE_DISTANCE.get(),
                cityCell, cityCell == 0 ? 0 : Math.min(cityMargin, (cityCell - 1) / 2),
                CITY_STRENGTH_MIN.get(), Math.max(CITY_STRENGTH_MIN.get(), CITY_STRENGTH_MAX.get()), CITY_OUTPUT.get());
    }
}
