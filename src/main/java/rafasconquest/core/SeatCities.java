package rafasconquest.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Where each title's seat city stands and how big it is, worked out when the world map is drawn. A city is N × N
 * districts ({@link #DISTRICT} blocks each, as a district of today's cities), by the highest title it seats: a county's
 * Town 2 × 2; a duchy's city 3 × 3, or 4 × 4 for a duchy of 5 or 6 counties; a kingdom's 5 × 5, sometimes 4 × 4; an
 * empire's 7 × 7, 8 × 8 with 5 kingdoms, a 9 × 9 metropolis with 6.
 * <p>
 * Cities are placed highest title first. Each stands as near its seat province's centre as it can while keeping
 * wholly on land (on the map's pixels, with a pixel to spare), clear of the cities already placed by
 * {@link #CLEARANCE}: so a seat by the shore draws up to the water's edge. A seat with no room in its province is
 * moved to another province the title could have seated (one of the seats of the titles it is made of), so seats stay
 * nested; a title with no room anywhere has no city. A big city may spread over the provinces around its own: they are
 * {@link City#covered} by it, and have it as their structure.
 * <p>
 * <b>Ports</b>: a seat with the sea within its reach is drawn to the shore instead, so that one side of the city (its
 * {@link City#harbour}) stands with its wall on land and the sea just past it, for its wharf and piers. Of the spots
 * that make a port, the one nearest the province's centre; a seat with none is an inland city as before. Only a sea
 * of at least {@link #MIN_SEA_AREA} counts: a pond of ocean biome makes no harbour. A kingdom's
 * or empire's capital is always a port where the realm has a shore with room for one: its seat moves to that province
 * (and the seats of the titles under it with it). Only a realm with no such shore keeps an inland capital.
 */
public final class SeatCities {
    /** One district: 5 × 5 city blocks of 32. */
    public static final int DISTRICT = 5 * 32;
    /** Cities keep at least this far apart (blocks), and this much land is kept about each beyond its wall. */
    public static final int CLEARANCE = 64;
    private static final int MARGIN = 10;
    /** A port's harbour side has land this far past its square (its wall's foot), then the sea within {@link #HARBOUR_SEA}. */
    static final int WALL_FOOT = 8;
    static final int HARBOUR_SEA = 40;
    /** A harbour opens only on water covering this many square blocks or more: a round sea some 2 km across. */
    static final long MIN_SEA_AREA = 3_000_000;
    private static final long SALT = 0x5345415443495459L;

    /**
     * A seat city: its centre, how many districts a side, the highest title it seats, its seat province, and the side
     * its harbour faces (a {@link CityLayout.Side}'s ordinal), or -1 for an inland city.
     */
    public record City(int x, int z, int districts, TitleMap.Tier tier, int province, int harbour) {
        /** An inland city. */
        public City(int x, int z, int districts, TitleMap.Tier tier, int province) {
            this(x, z, districts, tier, province, -1);
        }

        public boolean port() {
            return harbour >= 0;
        }

        /** Its side in blocks, edge roads included. */
        public int size() {
            return districts * DISTRICT + 3;
        }

        /** Half its side plus its wall, the land kept about it, and its terrain margin. */
        public int reach() {
            return size() / 2 + MARGIN;
        }
    }

    /** The map the cities are planned on: provinces per pixel, each pixel {@code bpp} blocks, from (minX, minZ). */
    public record Grid(int minX, int minZ, int bpp, int width, int height, int[] provinces, List<ProvinceMap.Centre> centres) {
    }

    private SeatCities() {
    }

    /** How many districts a side a title's seat city has. */
    public static int districts(TitleMap t, TitleMap.Tier tier, int title, Random random) {
        return switch (tier) {
            case COUNTY -> 2;
            case DUCHY -> members(t.duchy(), title) >= 5 ? 4 : 3;
            case KINGDOM -> random.nextInt(4) == 0 ? 4 : 5;
            case EMPIRE -> {
                int kingdoms = members(t.empire(), title);
                yield kingdoms >= 6 ? 9 : kingdoms == 5 ? 8 : 7;
            }
        };
    }

    /**
     * Plans every seat city on the map, moving seats that have no room (updating {@code titles}' seat arrays) and
     * returning the cities, highest title first.
     */
    public static List<City> plan(long seed, Grid g, TitleMap titles) {
        boolean[] ocean = new boolean[g.provinces().length];
        for (int i = 0; i < ocean.length; i++) {
            ocean[i] = g.provinces()[i] == ProvinceMap.WATER;
        }
        return plan(seed, g, titles, ocean);
    }

    /** As {@link #plan(long, Grid, TitleMap)}, with harbours opening only on {@code ocean} pixels (the sea ships sail). */
    public static List<City> plan(long seed, Grid g, TitleMap titles, boolean[] ocean) {
        Random random = new Random(seed ^ SALT);
        // Land, for squares wholly on it: a summed-area table of pixels that aren't (so a square is checked at once).
        int[] notLand = summed(g, i -> g.provinces()[i] < 0);
        boolean[] seas = seas(g, ocean);
        int[] sea = summed(g, i -> seas[i]);
        List<City> cities = new ArrayList<>();
        int[][] seats = {titles.countySeat(), titles.duchySeat(), titles.kingdomSeat(), titles.empireSeat()};
        for (int tier = 3; tier >= 0; tier--) {
            TitleMap.Tier t = TitleMap.Tier.values()[tier];
            int[] seat = seats[tier];
            List<Integer> order = new ArrayList<>();
            for (int title = 0; title < seat.length; title++) {
                order.add(title);
            }
            // Bigger titles of a tier first, so they get the room.
            int[] sizes = new int[seat.length];
            for (int title = 0; title < seat.length; title++) {
                sizes[title] = districts(titles, t, title, random);
            }
            order.sort(Comparator.comparingInt((Integer i) -> -sizes[i]));
            for (int title : order) {
                if (alreadyPlaced(cities, seat[title])) {
                    continue; // the seat of a higher title too: built at that size
                }
                List<Integer> candidates = candidates(titles, tier, title, seat[title]);
                City city = null;
                if (tier >= 2) {
                    // A kingdom's or empire's capital is a port: on the shore of any province of it that has room for one.
                    List<Integer> coast = new ArrayList<>(candidates);
                    for (int p : provincesOf(titles, tier, title)) {
                        if (!coast.contains(p)) {
                            coast.add(p);
                        }
                    }
                    for (int province : coast) {
                        city = fit(g, notLand, sea, cities, province, sizes[title], t, true);
                        if (city != null) {
                            seatChain(titles, tier, province);
                            break;
                        }
                    }
                }
                for (int i = 0; city == null && i < candidates.size(); i++) {
                    city = fit(g, notLand, sea, cities, candidates.get(i), sizes[title], t, false);
                    if (city != null) {
                        moveSeat(titles, tier, title, candidates.get(i));
                    }
                }
                if (city != null) {
                    cities.add(city);
                }
            }
        }
        return cities;
    }

    /** The provinces a title could be seated in, its own seat first: the seats of the titles it is made of. */
    private static List<Integer> candidates(TitleMap t, int tier, int title, int seat) {
        List<Integer> c = new ArrayList<>(List.of(seat));
        int[] of = switch (tier) {
            case 0 -> t.county();
            case 1 -> t.duchy();
            case 2 -> t.kingdom();
            default -> t.empire();
        };
        int[] lowerSeat = switch (tier) {
            case 0 -> null;
            case 1 -> t.countySeat();
            case 2 -> t.duchySeat();
            default -> t.kingdomSeat();
        };
        for (int i = 0; i < of.length; i++) {
            if (of[i] == title) {
                int p = lowerSeat == null ? i : lowerSeat[i];
                if (!c.contains(p)) {
                    c.add(p);
                }
            }
        }
        return c;
    }

    /** A title's seat moves to {@code province}, already the seat of one of the titles it is made of, so seats stay nested. */
    private static void moveSeat(TitleMap t, int tier, int title, int province) {
        int[][] seats = {t.countySeat(), t.duchySeat(), t.kingdomSeat(), t.empireSeat()};
        seats[tier][title] = province;
    }

    /** Every province of a kingdom (tier 2) or empire (tier 3). */
    private static List<Integer> provincesOf(TitleMap t, int tier, int title) {
        List<Integer> out = new ArrayList<>();
        for (int p = 0; p < t.county().length; p++) {
            int k = t.kingdom()[t.duchy()[t.county()[p]]];
            if ((tier == 2 ? k : t.empire()[k]) == title) {
                out.add(p);
            }
        }
        return out;
    }

    /**
     * A title's seat moves to {@code province}, and so do the seats of every title below it that holds the province,
     * so seats stay nested. Those lower titles are placed later, so none has a city yet.
     */
    private static void seatChain(TitleMap t, int tier, int province) {
        int county = t.county()[province];
        int duchy = t.duchy()[county];
        int kingdom = t.kingdom()[duchy];
        t.countySeat()[county] = province;
        t.duchySeat()[duchy] = province;
        t.kingdomSeat()[kingdom] = province;
        if (tier == 3) {
            t.empireSeat()[t.empire()[kingdom]] = province;
        }
    }

    private static boolean alreadyPlaced(List<City> cities, int province) {
        for (City c : cities) {
            if (c.province() == province) {
                return true;
            }
        }
        return false;
    }

    /**
     * A city of {@code districts} a side for a seat in {@code province}, searched within the province's reach: the port
     * nearest its centre, if any spot makes one; else the spot nearest its centre where the whole city (and its margin)
     * is on land, unless {@code portOnly}. Clear of the others either way; null if none.
     */
    private static City fit(Grid g, int[] notLand, int[] sea, List<City> cities, int province, int districts, TitleMap.Tier tier,
                            boolean portOnly) {
        ProvinceMap.Centre centre = g.centres().get(province);
        City probe = new City(centre.x(), centre.z(), districts, tier, province);
        int reach = probe.reach() + g.bpp();
        int half = probe.size() / 2;
        int search = Math.max(ProvinceMap.spacing(), probe.reach());
        City best = null;
        City port = null;
        double bestDistance = Double.MAX_VALUE;
        double portDistance = Double.MAX_VALUE;
        for (int dz = -search; dz <= search; dz += g.bpp()) {
            for (int dx = -search; dx <= search; dx += g.bpp()) {
                double d = Math.hypot(dx, dz);
                if (d >= portDistance || d > search) {
                    continue;
                }
                int x = centre.x() + dx;
                int z = centre.z() + dz;
                if (crowded(cities, x, z, probe.reach())) {
                    continue;
                }
                int px = Math.floorDiv(x - g.minX(), g.bpp());
                int pz = Math.floorDiv(z - g.minZ(), g.bpp());
                boolean own = px >= 0 && pz >= 0 && px < g.width() && pz < g.height() && g.provinces()[pz * g.width() + px] == province;
                for (CityLayout.Side side : CityLayout.Side.values()) {
                    if (own && harbourAt(g, notLand, sea, x, z, half, reach, side)) {
                        portDistance = d;
                        port = new City(x, z, districts, tier, province, side.ordinal());
                        break;
                    }
                }
                if (port == null && own && d < bestDistance && box(g, notLand, x - reach, z - reach, x + reach, z + reach) == 0) {
                    bestDistance = d;
                    best = new City(x, z, districts, tier, province);
                }
            }
        }
        return port != null || portOnly ? port : best;
    }

    /**
     * Whether a city at (x, z) makes a port facing {@code side}: on land with its margin, except that on that side the
     * land need only reach its wall's foot, and the sea lies within {@link #HARBOUR_SEA} past that, off the side's middle.
     */
    static boolean harbourAt(Grid g, int[] notLand, int[] sea, int x, int z, int half, int reach, CityLayout.Side side) {
        int foot = half + WALL_FOOT;
        int along = half / 2;
        return switch (side) {
            case NORTH -> box(g, notLand, x - reach, z - foot, x + reach, z + reach) == 0
                    && box(g, sea, x - along, z - half - HARBOUR_SEA, x + along, z - foot - 1) > 0;
            case SOUTH -> box(g, notLand, x - reach, z - reach, x + reach, z + foot) == 0
                    && box(g, sea, x - along, z + foot + 1, x + along, z + half + HARBOUR_SEA) > 0;
            case WEST -> box(g, notLand, x - foot, z - reach, x + reach, z + reach) == 0
                    && box(g, sea, x - half - HARBOUR_SEA, z - along, x - foot - 1, z + along) > 0;
            case EAST -> box(g, notLand, x - reach, z - reach, x + foot, z + reach) == 0
                    && box(g, sea, x + foot + 1, z - along, x + half + HARBOUR_SEA, z + along) > 0;
        };
    }

    /**
     * The {@code ocean} pixels of seas a harbour can open on: each body of water joined pixel to pixel that covers at
     * least {@link #MIN_SEA_AREA}; smaller ones (ponds of ocean biome) are left out.
     */
    static boolean[] seas(Grid g, boolean[] ocean) {
        int w = g.width();
        int h = g.height();
        boolean[] seas = new boolean[w * h];
        boolean[] seen = new boolean[w * h];
        int[] queue = new int[w * h];
        long minPixels = MIN_SEA_AREA / ((long) g.bpp() * g.bpp());
        for (int i = 0; i < w * h; i++) {
            if (!ocean[i] || seen[i]) {
                continue;
            }
            int head = 0;
            int tail = 0;
            queue[tail++] = i;
            seen[i] = true;
            while (head < tail) {
                int p = queue[head++];
                int x = p % w;
                int z = p / w;
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int nx = x + dx;
                        int nz = z + dz;
                        if (nx >= 0 && nz >= 0 && nx < w && nz < h && ocean[nz * w + nx] && !seen[nz * w + nx]) {
                            seen[nz * w + nx] = true;
                            queue[tail++] = nz * w + nx;
                        }
                    }
                }
            }
            if (tail >= minPixels) {
                for (int k = 0; k < tail; k++) {
                    seas[queue[k]] = true;
                }
            }
        }
        return seas;
    }

    /** A summed-area table of the pixels (by index) that count. */
    private static int[] summed(Grid g, java.util.function.IntPredicate counts) {
        int w = g.width();
        int[] t = new int[(w + 1) * (g.height() + 1)];
        for (int z = 0; z < g.height(); z++) {
            for (int x = 0; x < w; x++) {
                int one = counts.test(z * w + x) ? 1 : 0;
                t[(z + 1) * (w + 1) + x + 1] = one + t[z * (w + 1) + x + 1] + t[(z + 1) * (w + 1) + x] - t[z * (w + 1) + x];
            }
        }
        return t;
    }

    /** How many counted pixels lie under the block box; -1 if it runs off the map (neither land nor sea). */
    private static int box(Grid g, int[] table, int minX, int minZ, int maxX, int maxZ) {
        int x0 = Math.floorDiv(minX - g.minX(), g.bpp());
        int z0 = Math.floorDiv(minZ - g.minZ(), g.bpp());
        int x1 = Math.floorDiv(maxX - g.minX(), g.bpp());
        int z1 = Math.floorDiv(maxZ - g.minZ(), g.bpp());
        if (x0 < 0 || z0 < 0 || x1 >= g.width() || z1 >= g.height()) {
            return -1;
        }
        int w = g.width() + 1;
        return table[(z1 + 1) * w + x1 + 1] - table[z0 * w + x1 + 1] - table[(z1 + 1) * w + x0] + table[z0 * w + x0];
    }

    private static boolean crowded(List<City> cities, int x, int z, int reach) {
        for (City c : cities) {
            int apart = reach + c.reach() + CLEARANCE;
            if (Math.abs(c.x() - x) < apart && Math.abs(c.z() - z) < apart) {
                return true;
            }
        }
        return false;
    }

    /** The provinces a city stands on any part of (its own included), from the map's pixels. */
    public static List<Integer> covered(Grid g, City c) {
        List<Integer> provinces = new ArrayList<>();
        int half = c.size() / 2;
        int x0 = Math.max(0, Math.floorDiv(c.x() - half - g.minX(), g.bpp()));
        int z0 = Math.max(0, Math.floorDiv(c.z() - half - g.minZ(), g.bpp()));
        int x1 = Math.min(g.width() - 1, Math.floorDiv(c.x() + half - g.minX(), g.bpp()));
        int z1 = Math.min(g.height() - 1, Math.floorDiv(c.z() + half - g.minZ(), g.bpp()));
        for (int z = z0; z <= z1; z++) {
            for (int x = x0; x <= x1; x++) {
                int p = g.provinces()[z * g.width() + x];
                if (p >= 0 && !provinces.contains(p)) {
                    provinces.add(p);
                }
            }
        }
        return provinces;
    }

    /** How many titles of the level below make up {@code title} ({@code of}: the title each of them is part of). */
    private static int members(int[] of, int title) {
        int n = 0;
        for (int o : of) {
            if (o == title) {
                n++;
            }
        }
        return n;
    }
}
