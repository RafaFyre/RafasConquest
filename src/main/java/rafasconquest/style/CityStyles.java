package rafasconquest.style;

import org.jetbrains.annotations.Nullable;
import rafasconquest.core.CityLayout;
import rafasconquest.core.SeatCities;
import rafasconquest.core.TitleMap;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Homeland;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A city's buildings from the style folders, in its holder's style: its Palace (a hold sized by the highest title the
 * city seats), its Barracks (sized by the city), its church (if its nation keeps a faith), its noble buildings and its
 * walls. The same choice wherever it is asked, as the city is built and as its chunks come into the world, so that the
 * people of the mod's own buildings are left out where a style's building stands instead (its Character Markers bring
 * its people).
 */
public final class CityStyles {
    private static final String MANDATORY = "city/buildings/landmarks/mandatory/";
    /** Palace sizes, smallest first: a county's seat builds medium, a duchy's large, a kingdom's xlarge, an empire's xxlarge. */
    public static final List<String> PALACE_SIZES = List.of("small", "medium", "large", "xlarge", "xxlarge");
    /** Barracks sizes, smallest first, by the city's size. */
    public static final List<String> BARRACKS_SIZES = List.of("small", "medium", "large", "xlarge");
    public static final String SWORD_SANCTUM = "city/buildings/landmarks/unique/sword_sanctum";
    public static final String CHURCHES = "city/buildings/landmarks/style/churches";
    public static final String NOBLE = "city/buildings/noble";
    public static final String COMMON = "city/buildings/common";
    public static final String WALLS = "city/walls/walls";
    public static final String GATES = "city/walls/gates";
    public static final String CORNERS = "city/walls/corners";
    public static final String TOWERS = "city/walls/watchtowers";
    /** The Wall Tower Junction: a piece of the outer wall, in place of a segment, where the noble district's U of walls meets it. */
    public static final String JUNCTIONS = "city/walls/junctions";
    /** Templates face north; the mod's own post and church have their doors west. */
    public static final int TURNS_TO_WEST = 3;

    /** Choices made, by slot and city, until the library reloads. */
    private static final Map<String, Optional<String>> CHOSEN = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> PALACES = new ConcurrentHashMap<>();

    private CityStyles() {
    }

    static void forget() {
        CHOSEN.clear();
        PALACES.clear();
    }

    /** Forgets every choice and plan made for the cities of the world before. */
    public static void forgetAll() {
        CHOSEN.clear();
        PALACES.clear();
        CityPlan.forget();
        rafasconquest.sim.RecruitmentPosts.forgetSanctums();
    }

    /** The homeland of the nation that founded a city, or null if no nation did. */
    @Nullable
    public static Homeland homeland(CityLayout.Site site) {
        BaseMap map = ConquestRuntime.map();
        int nation = holder(site);
        return nation < 0 ? null : rafasconquest.gear.Armoury.homeland(map, map.nations().get(nation).id());
    }

    /**
     * The nation that held a city when the world began (its index), or -1. A city is built, and its people placed, by
     * its founder's style however the wars have gone since: a city half built when it changes hands is finished as begun.
     */
    private static int holder(CityLayout.Site site) {
        BaseMap map = ConquestRuntime.map();
        int province = map == null ? -1 : map.provinceIndexAt(site.x(), site.z());
        if (province < 0) {
            return -1;
        }
        int[] founders = ConquestRuntime.founders();
        return founders != null && province < founders.length ? founders[province] : map.holders()[province];
    }

    /** The city's size, 0 (two districts a side) to 3 (six or more). */
    public static int sizeRank(CityLayout.Site site) {
        int cells = site.cells();
        return cells <= 2 ? 0 : cells == 3 ? 1 : cells <= 5 ? 2 : 3;
    }

    /** The highest title a city seats, or null if it is no seat. */
    @Nullable
    public static TitleMap.Tier tier(CityLayout.Site site) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return null;
        }
        for (SeatCities.City c : map.cities()) {
            if (c.x() == site.x() && c.z() == site.z()) {
                return c.tier();
            }
        }
        return null;
    }

    /** Whether a sword sanctum holds the city. */
    public static boolean sanctum(CityLayout.Site site) {
        int nation = holder(site);
        BaseMap map = ConquestRuntime.map();
        return nation >= 0 && map.nations().get(nation).sanctum();
    }

    /** The city's Palace: the first of {@link #palaces}, or null if it has none. */
    @Nullable
    public static String palace(CityLayout.Site site) {
        List<String> palaces = palaces(site);
        return palaces.isEmpty() ? null : palaces.get(0);
    }

    /**
     * Every Palace the city could build, best first: at a sword sanctum, the sanctum; then the hold of the city's
     * highest title (an empire's xxlarge down to small), then each size down, then up; the style's own before the
     * default style's. The city builds the first that fits its noble district.
     */
    public static List<String> palaces(CityLayout.Site site) {
        if (ConquestRuntime.map() == null) {
            return List.of();
        }
        return PALACES.computeIfAbsent(site.x() + "|" + site.z() + "|" + site.cells(), k -> {
            Homeland land = homeland(site);
            if (land == null) {
                return List.of();
            }
            List<String> palaces = new ArrayList<>();
            if (sanctum(site)) {
                String sanctum = StyleLibrary.pick(land, SWORD_SANCTUM, seed(site));
                if (sanctum != null) {
                    palaces.add(sanctum);
                }
            }
            TitleMap.Tier tier = tier(site);
            int wanted = tier == null ? 0 : switch (tier) {
                case COUNTY -> 1;
                case DUCHY -> 2;
                case KINGDOM -> 3;
                case EMPIRE -> 4;
            };
            palaces.addAll(sized(site, "palace", PALACE_SIZES, wanted));
            return List.copyOf(palaces);
        });
    }

    /** The Barracks, sized by the city: the next size down where the style has none, then up. */
    @Nullable
    public static String barracks(CityLayout.Site site) {
        return chosen("barracks", site, () -> sized(site, "barracks", BARRACKS_SIZES, sizeRank(site)).stream().findFirst().orElse(null));
    }

    /**
     * The city's church, if its nation keeps a faith: the style's churches from smallest to largest, one for each city
     * size, the largest for every size past them.
     */
    @Nullable
    public static String church(CityLayout.Site site) {
        return chosen("church", site, () -> {
            BaseMap map = ConquestRuntime.map();
            int nation = holder(site);
            if (nation < 0 || nation >= map.realm().nationFaith().length || map.realm().nationFaith()[nation] < 0) {
                return null;
            }
            List<String> churches = new ArrayList<>(StyleLibrary.all(homeland(site), CHURCHES));
            churches.removeIf(f -> StyleLibrary.size(f) == null);
            if (churches.isEmpty()) {
                return null;
            }
            churches.sort(Comparator.comparingInt(f -> {
                net.minecraft.core.Vec3i s = StyleLibrary.size(f);
                return s.getX() * s.getZ();
            }));
            return churches.get(Math.min(sizeRank(site), churches.size() - 1));
        });
    }

    /** A building of a style's slot with its size. */
    public record Building(String file, int x, int y, int z) {
    }

    /** Every building of a slot for the city's holder's style (else the default style's), with its size. */
    public static List<Building> buildings(CityLayout.Site site, String slot) {
        Homeland land = homeland(site);
        return land == null ? List.of() : read(StyleLibrary.all(land, slot));
    }

    /** Every building of a slot of the holder's own style only (none from the default style). */
    public static List<Building> own(CityLayout.Site site, String slot) {
        Homeland land = homeland(site);
        return land == null ? List.of() : read(StyleLibrary.own(land, slot));
    }

    @Nullable
    public static Building building(@Nullable String file) {
        if (file == null) {
            return null;
        }
        net.minecraft.core.Vec3i s = StyleLibrary.size(file);
        return s == null ? null : new Building(file, s.getX(), s.getY(), s.getZ());
    }

    private static List<Building> read(List<String> files) {
        List<Building> found = new ArrayList<>();
        for (String file : files) {
            Building b = building(file);
            if (b != null) {
                found.add(b);
            }
        }
        return found;
    }

    /** A city's own houses, from its style's common buildings: each file and its size. */
    public record Houses(List<String> files, List<BlockPacker.Size> sizes) {
        public boolean any() {
            return !files.isEmpty();
        }
    }

    /** The houses a city builds in its style (none if its holder's style has none, nor the default style). */
    public static Houses houses(CityLayout.Site site) {
        List<String> files = new ArrayList<>();
        List<BlockPacker.Size> sizes = new ArrayList<>();
        for (Building b : buildings(site, COMMON)) {
            files.add(b.file());
            sizes.add(new BlockPacker.Size(b.x(), b.z()));
        }
        return new Houses(files, sizes);
    }

    /** A sized slot's files, one per size: the wanted size, then smaller, then larger; in the founder's style, then the default style. */
    private static List<String> sized(CityLayout.Site site, String kind, List<String> sizes, int wanted) {
        Homeland land = homeland(site);
        List<String> found = new ArrayList<>();
        if (land == null) {
            return found;
        }
        List<Integer> order = new ArrayList<>();
        for (int i = Math.min(wanted, sizes.size() - 1); i >= 0; i--) {
            order.add(i);
        }
        for (int i = wanted + 1; i < sizes.size(); i++) {
            order.add(i);
        }
        for (String style : new String[]{StyleLibrary.styleOf(land), StyleLibrary.DEFAULT}) {
            for (int i : order) {
                String file = StyleLibrary.pickIn(style, MANDATORY + kind + "/" + sizes.get(i), seed(site));
                if (file != null) {
                    found.add(file);
                }
            }
        }
        return found;
    }

    private static long seed(CityLayout.Site site) {
        return (long) site.x() * 73856093L ^ (long) site.z() * 19349663L;
    }

    @Nullable
    private static String chosen(String what, CityLayout.Site site, java.util.function.Supplier<String> choose) {
        if (ConquestRuntime.map() == null) {
            return null;
        }
        return CHOSEN.computeIfAbsent(what + "|" + site.x() + "|" + site.z() + "|" + site.cells(),
                k -> Optional.ofNullable(choose.get())).orElse(null);
    }
}
