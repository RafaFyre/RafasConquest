package rafasconquest.style;

import org.jetbrains.annotations.Nullable;
import rafasconquest.core.CityLayout;
import rafasconquest.core.RecruitmentPost;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Where a city's styled buildings stand, laid out once for the whole city so that every cell, its ground and the
 * people placed later agree:
 * <ul>
 *   <li>its walls from its style's wall files (corners, a gate on each side's avenue, segments with watchtowers
 *   between), where its style has its own;</li>
 *   <li>its noble district, the middle third of its blocks with no streets inside, where its style has a Palace: the
 *   Palace in the middle, the Barracks (or the mod's own post) and the church beside it, the rest filled with the
 *   style's noble buildings.</li>
 * </ul>
 * Each piece is built by the cell holding its middle, and is kept within reach of that cell's start (a start's pieces
 * reach at most 8 chunks from it). Coordinates are city-local.
 */
public final class CityPlan {
    /** What a piece is. The mod's own post has no file. */
    public enum Kind { WALL, PALACE, BARRACKS, POST, CHURCH, NOBLE }

    /** A piece: its file, its footprint's corner and size (as turned), its height, and its quarter turns. */
    public record Piece(Kind kind, @Nullable String file, int minX, int minZ, int w, int d, int h, int turns) {
        public int centreX() {
            return minX + w / 2;
        }

        public int centreZ() {
            return minZ + d / 2;
        }

        boolean covers(int x, int z) {
            return x >= minX && x < minX + w && z >= minZ && z < minZ + d;
        }
    }

    /** A city's plan; {@code lo == hi} when it has no noble district. */
    public record Plan(int lo, int hi, boolean styledWalls, List<Piece> pieces) {
        public boolean noble() {
            return hi > lo;
        }

        /** Whether block (bx, bz) lies in the noble district. */
        public boolean nobleBlock(int bx, int bz) {
            return noble() && bx >= lo && bx < hi && bz >= lo && bz < hi;
        }

        /** The district's first and last local coordinate along either axis (inside its bounding roads). */
        public int areaMin() {
            return CityLayout.blockStart(lo);
        }

        public int areaMax() {
            return CityLayout.roadStart(hi) - 1;
        }

        /** The height of the tallest styled piece standing over local (x, z), or -1 if none does. */
        public int top(int x, int z) {
            int top = -1;
            for (Piece p : pieces) {
                if (p.file() != null && p.covers(x, z)) {
                    top = Math.max(top, p.h());
                }
            }
            return top;
        }

        /** The pieces cell (cellX, cellZ) builds: those whose middle lies in it (walls outside, by the nearest cell). */
        public List<Piece> inCell(int cells, int cellX, int cellZ) {
            List<Piece> mine = new ArrayList<>();
            for (Piece p : pieces) {
                if (cellOf(cells, p.centreX()) == cellX && cellOf(cells, p.centreZ()) == cellZ) {
                    mine.add(p);
                }
            }
            return mine;
        }

        public boolean has(Kind kind) {
            return pieces.stream().anyMatch(p -> p.kind() == kind);
        }

        @Nullable
        public Piece first(Kind kind) {
            return pieces.stream().filter(p -> p.kind() == kind).findFirst().orElse(null);
        }
    }

    private static final Plan NONE = new Plan(0, 0, false, List.of());
    /** Chunks a structure start's pieces may reach from its own chunk. */
    private static final int REACH_CHUNKS = 8;
    /** Blocks kept clear around the Palace, Barracks and church. */
    private static final int CLEARANCE = 3;
    /** Wall pieces between watchtowers. */
    private static final int TOWER_EVERY = 3;
    /** Positions tried around the wanted spot, every this many blocks. */
    private static final int SEARCH_STEP = 2;

    private static final Map<Long, Plan> PLANS = new ConcurrentHashMap<>();

    private CityPlan() {
    }

    static void forget() {
        PLANS.clear();
    }

    /** The plan of a city (none until the world map is known). */
    public static Plan of(CityLayout.Site site) {
        if (ConquestRuntime.map() == null) {
            return NONE;
        }
        long key = ((long) site.originX() << 32 ^ (site.originZ() & 0xffffffffL)) * 31 + site.cells();
        return PLANS.computeIfAbsent(key, k -> make(site));
    }

    /** The plan of the city whose square, walls included, holds world (x, z). */
    public static Plan of(int originX, int originZ, int cells) {
        int half = (cells * CityLayout.BLOCKS_PER_CELL * CityLayout.ROAD_PERIOD + CityLayout.ROAD_WIDTH) / 2;
        return of(new CityLayout.Site(originX + half, originZ + half, false, cells));
    }

    /** Whether a style's Palace stands in the city in place of the gold church: then its markers bring its people. */
    public static boolean palaceStands(CityLayout.Site site) {
        Plan plan = of(site);
        return plan.noble() ? plan.has(Kind.PALACE) : CityStyles.palace(site) != null;
    }

    /** Whether a style's Barracks stands in the city in place of the mod's post: then its markers bring its people. */
    public static boolean barracksStands(CityLayout.Site site) {
        Plan plan = of(site);
        return plan.noble() ? !plan.has(Kind.POST) : CityStyles.barracks(site) != null;
    }

    private static int cellOf(int cells, int local) {
        return Math.max(0, Math.min(cells - 1, Math.floorDiv(local, CityLayout.DISTRICT_SPACING)));
    }

    private static Plan make(CityLayout.Site site) {
        if (CityStyles.homeland(site) == null) {
            return NONE;
        }
        Random random = new Random((long) site.x() * 341873128712L ^ (long) site.z() * 132897987541L ^ 0xC17EL);
        List<Piece> pieces = new ArrayList<>();
        boolean walls = walls(site, random, pieces);
        int bps = site.blocksPerSide();
        int lo = bps / 3;
        int hi = (2 * bps + 2) / 3;
        if (hi <= lo || CityStyles.palace(site) == null) {
            return new Plan(0, 0, walls, pieces);
        }
        noble(site, lo, hi, random, pieces);
        return new Plan(lo, hi, walls, pieces);
    }

    /** The walls from the style's own wall files; false (and none) if it has no wall segments of its own. */
    private static boolean walls(CityLayout.Site site, Random random, List<Piece> pieces) {
        List<CityStyles.Building> segments = CityStyles.own(site, CityStyles.WALLS);
        if (segments.isEmpty()) {
            return false;
        }
        List<CityStyles.Building> gates = orElse(CityStyles.own(site, CityStyles.GATES), segments);
        List<CityStyles.Building> towers = CityStyles.own(site, CityStyles.TOWERS);
        List<CityStyles.Building> corners = orElse(CityStyles.own(site, CityStyles.CORNERS), orElse(towers, segments));
        int size = site.size();
        int inner = CityLayout.WALL_WIDTH - CityLayout.WALL_OUTSET; // the band's inner edge, 3 in from its outer one
        int far = size - inner;                                     // where the far sides' band begins
        // The corners, each over its corner of the band, turned with the side it starts.
        for (int c = 0; c < 4; c++) {
            CityStyles.Building b = corners.get(random.nextInt(corners.size()));
            int w = c % 2 == 0 ? b.x() : b.z();
            int d = c % 2 == 0 ? b.z() : b.x();
            int minX = c == 0 || c == 3 ? inner - w : far;
            int minZ = c < 2 ? inner - d : far;
            pieces.add(new Piece(Kind.WALL, b.file(), minX, minZ, w, d, b.y(), c));
        }
        int gateMiddle = CityLayout.roadStart(site.blocksPerSide() / 2) + 1;
        for (int side = 0; side < 4; side++) {
            CityStyles.Building gate = gates.get(random.nextInt(gates.size()));
            int gateFrom = gateMiddle - gate.x() / 2;
            along(side, size, gate, gateFrom, pieces);
            run(side, size, inner, gateFrom - 1, segments, towers, random, pieces);
            run(side, size, gateFrom + gate.x(), far - 1, segments, towers, random, pieces);
        }
        return true;
    }

    private static List<CityStyles.Building> orElse(List<CityStyles.Building> first, List<CityStyles.Building> second) {
        return first.isEmpty() ? second : first;
    }

    /** Fills a side from {@code from} to {@code to} (inclusive) with segments, a watchtower after every few; the last flush with the end. */
    private static void run(int side, int size, int from, int to, List<CityStyles.Building> segments, List<CityStyles.Building> towers,
                            Random random, List<Piece> pieces) {
        int t = from;
        int n = 0;
        while (t <= to) {
            boolean tower = !towers.isEmpty() && n % (TOWER_EVERY + 1) == TOWER_EVERY;
            CityStyles.Building b = tower ? towers.get(random.nextInt(towers.size())) : segments.get(random.nextInt(segments.size()));
            if (t + b.x() - 1 > to) {
                along(side, size, b, to - b.x() + 1, pieces);
                return;
            }
            along(side, size, b, t, pieces);
            t += b.x();
            n++;
        }
    }

    /** A wall piece along a side, starting {@code t} along it: north, east, south, west, facing out. */
    private static void along(int side, int size, CityStyles.Building b, int t, List<Piece> pieces) {
        int inner = CityLayout.WALL_WIDTH - CityLayout.WALL_OUTSET;
        int far = size - inner;
        int len = b.x();
        int depth = b.z();
        Piece p = switch (side) {
            case 0 -> new Piece(Kind.WALL, b.file(), t, inner - depth, len, depth, b.y(), 0);
            case 1 -> new Piece(Kind.WALL, b.file(), far, t, depth, len, b.y(), 1);
            case 2 -> new Piece(Kind.WALL, b.file(), t, far, len, depth, b.y(), 2);
            default -> new Piece(Kind.WALL, b.file(), inner - depth, t, depth, len, b.y(), 3);
        };
        pieces.add(p);
    }

    /** The noble district: the Palace, the Barracks (or the mod's post), the church, then the noble buildings. */
    private static void noble(CityLayout.Site site, int lo, int hi, Random random, List<Piece> pieces) {
        int a0 = CityLayout.blockStart(lo);
        int a1 = CityLayout.roadStart(hi) - 1;
        int middle = site.size() / 2;
        List<Piece> taken = new ArrayList<>();
        String barracks = CityStyles.barracks(site);
        RecruitmentPost post = RecruitmentPost.of(site);
        // The mod's own post, on its own plot, where the style has no Barracks or its Barracks finds no room.
        Piece ownPost = new Piece(Kind.POST, null, post.plotX() - site.originX(), post.plotZ() - site.originZ(),
                CityLayout.PLOT_SIZE, CityLayout.PLOT_SIZE, 0, 0);
        if (barracks == null) {
            taken.add(ownPost); // its people stand by it
        }
        // The first Palace that fits, the wanted size first.
        Piece palace = null;
        for (String file : CityStyles.palaces(site)) {
            palace = place(site, Kind.PALACE, CityStyles.building(file), middle, middle, a0, a1, taken);
            if (palace != null) {
                taken.add(palace);
                break;
            }
        }
        int nearX = palace == null ? middle : palace.centreX();
        int nearZ = palace == null ? middle : palace.centreZ();
        Piece b = place(site, Kind.BARRACKS, CityStyles.building(barracks), nearX, nearZ, a0, a1, taken);
        if (b != null) {
            taken.add(b);
        } else if (barracks != null && clear(ownPost, taken)) {
            taken.add(ownPost);
        }
        Piece church = place(site, Kind.CHURCH, CityStyles.building(CityStyles.church(site)), nearX, nearZ, a0, a1, taken);
        if (church != null) {
            taken.add(church);
        }
        pieces.addAll(taken);
        // The rest: the noble buildings, packed as a city block is, each facing the nearer edge of the district.
        List<CityStyles.Building> noble = CityStyles.buildings(site, CityStyles.NOBLE);
        if (noble.isEmpty()) {
            return;
        }
        int span = a1 - a0 + 1;
        List<BlockPacker.Rect> free = new ArrayList<>(List.of(new BlockPacker.Rect(0, 0, span, span)));
        for (Piece p : taken) {
            free = BlockPacker.cut(free, new BlockPacker.Rect(p.minX() - a0 - CLEARANCE, p.minZ() - a0 - CLEARANCE,
                    p.w() + 2 * CLEARANCE, p.d() + 2 * CLEARANCE));
        }
        List<BlockPacker.Size> sizes = noble.stream().map(n -> new BlockPacker.Size(n.x(), n.z())).toList();
        for (BlockPacker.Placed p : BlockPacker.pack(free, span, span, sizes, random)) {
            CityStyles.Building n = noble.get(p.building());
            boolean turned = p.turns() % 2 == 1;
            Piece piece = new Piece(Kind.NOBLE, n.file(), a0 + p.x(), a0 + p.z(), turned ? n.z() : n.x(), turned ? n.x() : n.z(), n.y(), p.turns());
            if (reachable(site, piece)) {
                pieces.add(piece);
            }
        }
    }

    /**
     * A building set as near (x, z) as it goes: inside the district, clear of what is taken, and within reach of the
     * start of the cell holding its middle. Null if it has no file or nowhere fits.
     */
    @Nullable
    private static Piece place(CityLayout.Site site, Kind kind, @Nullable CityStyles.Building b, int x, int z, int a0, int a1, List<Piece> taken) {
        if (b == null || b.x() > a1 - a0 + 1 || b.z() > a1 - a0 + 1) {
            return null;
        }
        int reach = a1 - a0;
        for (int r = 0; r <= reach; r += SEARCH_STEP) {
            // The ring r out: its top and bottom rows, then its sides between them.
            for (int i = -r; i <= r; i += SEARCH_STEP) {
                for (int[] d : r == 0 ? new int[][]{{0, 0}} : new int[][]{{i, -r}, {i, r}, {-r, i}, {r, i}}) {
                    Piece p = new Piece(kind, b.file(), x + d[0] - b.x() / 2, z + d[1] - b.z() / 2, b.x(), b.z(), b.y(), 0);
                    if (p.minX() >= a0 && p.minZ() >= a0 && p.minX() + p.w() - 1 <= a1 && p.minZ() + p.d() - 1 <= a1
                            && clear(p, taken) && reachable(site, p)) {
                        return p;
                    }
                }
                if (r == 0) {
                    break;
                }
            }
        }
        return null;
    }

    private static boolean clear(Piece p, List<Piece> taken) {
        for (Piece t : taken) {
            if (p.minX() < t.minX() + t.w() + CLEARANCE && t.minX() < p.minX() + p.w() + CLEARANCE
                    && p.minZ() < t.minZ() + t.d() + CLEARANCE && t.minZ() < p.minZ() + p.d() + CLEARANCE) {
                return false;
            }
        }
        return true;
    }

    /** Whether every chunk of the piece lies within reach of the start of the cell that builds it. */
    private static boolean reachable(CityLayout.Site site, Piece p) {
        int cells = site.cells();
        int startX = site.originX() + cellOf(cells, p.centreX()) * CityLayout.DISTRICT_SPACING + CityLayout.DISTRICT_SPACING / 2 + 1;
        int startZ = site.originZ() + cellOf(cells, p.centreZ()) * CityLayout.DISTRICT_SPACING + CityLayout.DISTRICT_SPACING / 2 + 1;
        int x0 = site.originX() + p.minX();
        int z0 = site.originZ() + p.minZ();
        return (x0 >> 4) >= (startX >> 4) - REACH_CHUNKS && ((x0 + p.w() - 1) >> 4) <= (startX >> 4) + REACH_CHUNKS
                && (z0 >> 4) >= (startZ >> 4) - REACH_CHUNKS && ((z0 + p.d() - 1) >> 4) <= (startZ >> 4) + REACH_CHUNKS;
    }
}
