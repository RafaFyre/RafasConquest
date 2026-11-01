package rafasconquest.style;

import org.jetbrains.annotations.Nullable;
import rafasconquest.core.CityLayout;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

/**
 * A city's buildings from the style folders: its Palace (in place of the gold church) and its small Barracks (in place of
 * the recruitment post), in its holder's style, where the slot has a file. The same choice wherever it is asked, as the
 * city is built and as its chunks come into the world, so that the people of the mod's own buildings are left out
 * where a style's building stands instead (its Character Markers bring its people).
 */
public final class CityStyles {
    public static final String PALACE = "city/buildings/landmarks/mandatory/palace";
    public static final String BARRACKS_SMALL = "city/buildings/landmarks/mandatory/barracks/small";
    /** Templates face north; these buildings' doors face west, as the mod's own do. */
    public static final int TURNS_TO_WEST = 3;

    private CityStyles() {
    }

    @Nullable
    public static String palace(CityLayout.Site site) {
        return pick(site, PALACE);
    }

    @Nullable
    public static String barracks(CityLayout.Site site) {
        return pick(site, BARRACKS_SMALL);
    }

    @Nullable
    private static String pick(CityLayout.Site site, String slot) {
        BaseMap map = ConquestRuntime.map();
        int province = map == null ? -1 : map.provinceIndexAt(site.x(), site.z());
        int nation = province < 0 ? -1 : map.holders()[province];
        if (nation < 0) {
            return null;
        }
        return StyleLibrary.pick(rafasconquest.gear.Armoury.homeland(map, map.nations().get(nation).id()), slot,
                (long) site.x() * 73856093L ^ (long) site.z() * 19349663L);
    }
}
