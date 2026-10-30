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
 */
public final class SeatCities {
    /** One district: 5 × 5 city blocks of 32. */
    public static final int DISTRICT = 5 * 32;
    /** Cities keep at least this far apart (blocks), and this much land is kept about each beyond its wall. */
    public static final int CLEARANCE = 64;
    private static final int MARGIN = 10;
    private static final long SALT = 0x5345415443495459L;

    /**
     * A seat city: its centre, how many districts a side, the highest title it seats, its seat province, and whether
     * it lies in ruins (an unowned seat, abandoned).
     */
    public record City(int x, int z, int districts, TitleMap.Tier tier, int province, boolean ruin) {
        public City(int x, int z, int districts, TitleMap.Tier tier, int province) {
            this(x, z, districts, tier, province, false);
        }

        public City ruined() {
            return new City(x, z, districts, tier, province, true);
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
        Random random = new Random(seed ^ SALT);
        // Land, for squares wholly on it: a summed-area table of pixels that aren't (so a square is checked at once).
        int w = g.width();
        int h = g.height();
        int[] notLand = new int[(w + 1) * (h + 1)];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int bad = g.provinces()[z * w + x] < 0 ? 1 : 0;
                notLand[(z + 1) * (w + 1) + x + 1] = bad + notLand[z * (w + 1) + x + 1] + notLand[(z + 1) * (w + 1) + x]
                        - notLand[z * (w + 1) + x];
            }
        }
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
                for (int province : candidates) {
                    city = fit(g, notLand, cities, province, sizes[title], t);
                    if (city != null) {
                        moveSeat(titles, tier, title, province);
                        break;
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

    private static boolean alreadyPlaced(List<City> cities, int province) {
        for (City c : cities) {
            if (c.province() == province) {
                return true;
            }
        }
        return false;
    }

    /**
     * A city of {@code districts} a side for a seat in {@code province}: the spot nearest its centre where the whole
     * city (and its margin) is on land and clear of the others, searched within the province's reach; null if none.
     */
    private static City fit(Grid g, int[] notLand, List<City> cities, int province, int districts, TitleMap.Tier tier) {
        ProvinceMap.Centre centre = g.centres().get(province);
        City probe = new City(centre.x(), centre.z(), districts, tier, province);
        int reach = probe.reach() + g.bpp();
        int search = Math.max(ProvinceMap.SPACING, probe.reach());
        City best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dz = -search; dz <= search; dz += g.bpp()) {
            for (int dx = -search; dx <= search; dx += g.bpp()) {
                double d = Math.hypot(dx, dz);
                if (d >= bestDistance || d > search) {
                    continue;
                }
                int x = centre.x() + dx;
                int z = centre.z() + dz;
                if (!onLand(g, notLand, x, z, reach) || crowded(cities, x, z, probe.reach())) {
                    continue;
                }
                bestDistance = d;
                best = new City(x, z, districts, tier, province);
            }
        }
        return best;
    }

    private static boolean onLand(Grid g, int[] notLand, int x, int z, int reach) {
        int x0 = Math.floorDiv(x - reach - g.minX(), g.bpp());
        int z0 = Math.floorDiv(z - reach - g.minZ(), g.bpp());
        int x1 = Math.floorDiv(x + reach - g.minX(), g.bpp());
        int z1 = Math.floorDiv(z + reach - g.minZ(), g.bpp());
        if (x0 < 0 || z0 < 0 || x1 >= g.width() || z1 >= g.height()) {
            return false;
        }
        int w = g.width() + 1;
        int bad = notLand[(z1 + 1) * w + x1 + 1] - notLand[z0 * w + x1 + 1] - notLand[(z1 + 1) * w + x0] + notLand[z0 * w + x0];
        return bad == 0;
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
