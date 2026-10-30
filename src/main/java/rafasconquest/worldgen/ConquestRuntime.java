package rafasconquest.worldgen;

import org.jetbrains.annotations.Nullable;
import rafasconquest.core.WorldSettings;

/**
 * The current world's frozen settings, published for worldgen threads. Set from the server thread when the overworld
 * loads (before spawn chunks generate) and read-only everywhere else. Worldgen never touches save data itself.
 */
public final class ConquestRuntime {
    @Nullable
    private static volatile WorldSettings settings;
    /** The world's countryside settlements, from its map. */
    private static volatile CountryIndex countryside = CountryIndex.EMPTY;
    /** The world's seat cities, from its map (empty until it is loaded or drawn). */
    private static volatile CityIndex cities = CityIndex.EMPTY;

    private ConquestRuntime() {
    }

    @Nullable
    public static WorldSettings settings() {
        return settings;
    }

    public static void publish(@Nullable WorldSettings frozen) {
        settings = frozen;
    }

    public static CountryIndex countryside() {
        return countryside;
    }

    public static void publishCountryside(@Nullable CountryIndex index) {
        countryside = index == null ? CountryIndex.EMPTY : index;
    }

    public static CityIndex cities() {
        return cities;
    }

    public static void publishCities(@Nullable CityIndex index) {
        cities = index == null ? CityIndex.EMPTY : index;
    }
}
