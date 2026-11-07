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
 *   <li>its noble district, about a sixth of its blocks with no streets inside, in the middle or against one wall, where
 *   its style has a Palace: the Palace in the middle, the Barracks (or the mod's own post) beside it,
 *   the rest filled with the style's noble buildings; in cities of 3+ districts a side, a second ring of the style's
 *   walls around it (a U meeting the outer wall, with corner towers there, when it stands against one);</li>
 *   <li>its church (and, with no Palace, a Barracks too big for the post's plot), outside the noble district near the
 *   town centre (the post), taking the plots it covers; one bigger than a block merges as many as it needs, the roads
 *   between them gone.</li>
 * </ul>
 * Each piece is built by the cell holding its middle, and is kept within reach of that cell's start (a start's pieces
 * reach at most 8 chunks from it). Coordinates are city-local.
 */
public final class CityPlan {
    /** What a piece is. The mod's own post has no file. */
    public enum Kind { WALL, PALACE, BARRACKS, POST, CHURCH, NOBLE }

    /** A piece: its file, its footprint's corner and size (as turned), its top above the plate, and its quarter turns. */
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

    /** A city's plan: its noble district's blocks [x0, x1) × [z0, z1), empty when it has none. */
    public record Plan(int x0, int x1, int z0, int z1, boolean styledWalls, List<Piece> pieces) {
        public boolean noble() {
            return x1 > x0;
        }

        /** Whether block (bx, bz) lies in the noble district. */
        public boolean nobleBlock(int bx, int bz) {
            return noble() && bx >= x0 && bx < x1 && bz >= z0 && bz < z1;
        }

        /** The district's first and last local coordinates along each axis (inside its bounding roads). */
        public int areaMinX() {
            return CityLayout.blockStart(x0);
        }

        public int areaMaxX() {
            return CityLayout.roadStart(x1) - 1;
        }

        public int areaMinZ() {
            return CityLayout.blockStart(z0);
        }

        public int areaMaxZ() {
            return CityLayout.roadStart(z1) - 1;
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

    private static final Plan NONE = new Plan(0, 0, 0, 0, false, List.of());
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
        OUTSETS.clear();
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
        int inner = CityLayout.WALL_WIDTH - CityLayout.WALL_OUTSET; // the outer band's inner edge, 3 in from its outer one
        int avenue = CityLayout.roadStart(site.blocksPerSide() / 2) + 1;
        int[] outer = {inner, site.size() - inner, inner, site.size() - inner};
        if (CityStyles.palace(site) == null) {
            boolean styledWalls = walls(site, outer, avenue, avenue, -1, -1, null, random, pieces);
            addCityBuildings(site, 0, 0, 0, 0, pieces);
            return new Plan(0, 0, 0, 0, styledWalls, lifted(pieces));
        }
        int edge = random.nextBoolean() ? -1 : random.nextInt(4);
        int bps = site.blocksPerSide();
        int[] d = district(bps, edge);
        // Cities of 3+ districts a side wall their noble district too, just inside its edge; along the city's edge the
        // ring is a U whose open side is the outer wall. The buildings keep clear.
        int depth = site.cells() >= 3 ? wallDepth(site) : 0;
        List<CityStyles.Building> junctions = depth == 0 || edge < 0 ? List.of() : CityStyles.own(site, CityStyles.JUNCTIONS);
        int[] area;
        int[] box;
        int[] joins;
        for (boolean shifted = false; ; shifted = true) {
            // Its area, inside its bounding roads: west, east, north, south edges, as the wall ring's box is.
            area = new int[]{CityLayout.blockStart(d[0]), CityLayout.roadStart(d[0] + d[2]), CityLayout.blockStart(d[1]), CityLayout.roadStart(d[1] + d[2])};
            box = area.clone();
            for (int s = 0; s < 4; s++) {
                if (s != edge) {
                    shrink(box, s, depth);
                }
            }
            // Where the U meets the outer wall, the outer wall has a Wall Tower Junction in place of a segment, centred on the U's arms.
            joins = junctions.isEmpty() ? null
                    : edge % 2 == 0 ? new int[]{box[0] - depth / 2, box[1] + depth / 2} : new int[]{box[2] - depth / 2, box[3] + depth / 2};
            if (shifted || !hitsGate(site, joins, avenue, junctions)) {
                break;
            }
            // A junction on the gate: the district moves along the wall by two wall segments, in whole blocks, staying in the city.
            int segment = CityStyles.own(site, CityStyles.WALLS).stream().mapToInt(CityStyles.Building::x).max().orElse(0);
            int shift = Math.max(1, (2 * segment + CityLayout.ROAD_PERIOD - 1) / CityLayout.ROAD_PERIOD);
            int axis = edge % 2 == 0 ? 0 : 1; // north and south districts run along x
            d[axis] = d[axis] + d[2] + shift <= bps ? d[axis] + shift : Math.max(0, d[axis] - shift);
        }
        int x1 = d[0] + d[2];
        int z1 = d[1] + d[2];
        boolean walls = walls(site, outer, avenue, avenue, -1, edge, joins, random, pieces);
        if (depth > 0 && walls(site, box, CityLayout.roadStart((d[0] + x1) / 2) + 1, CityLayout.roadStart((d[1] + z1) / 2) + 1,
                edge, -1, null, random, pieces)) {
            area = box;
            for (int s = 0; s < 4; s++) {
                shrink(area, s, CLEARANCE);
            }
        }
        noble(site, area[0], area[1] - 1, area[2], area[3] - 1, random, pieces);
        addCityBuildings(site, d[0], x1, d[1], z1, pieces);
        return new Plan(d[0], x1, d[1], z1, walls, lifted(pieces));
    }

    /**
     * The city's own buildings outside the noble district [nx0, nx1) × [nz0, nz1), placed before its houses: the church
     * nearest the town centre and, where there is no Palace, a Barracks too big for the post's plot nearest the post.
     */
    private static void addCityBuildings(CityLayout.Site site, int nx0, int nx1, int nz0, int nz1, List<Piece> pieces) {
        int c = site.centerBlock();
        inCity(site, Kind.CHURCH, CityStyles.building(CityStyles.church(site)), c, c, nx0, nx1, nz0, nz1, pieces);
        CityStyles.Building barracks = CityStyles.building(CityStyles.barracks(site));
        if (nx1 <= nx0 && barracks != null && Math.max(barracks.x(), barracks.z()) > CityLayout.PLOT_SIZE
                && rafasconquest.worldgen.CityGridBuilder.heldByANation(site)) {
            inCity(site, Kind.BARRACKS, barracks, RecruitmentPost.blockX(site), RecruitmentPost.blockZ(site), nx0, nx1, nz0, nz1, pieces);
        }
    }

    /**
     * A building centred on as many blocks as it needs, merged into one (the plots, and the roads between them, given up
     * to it), as near block (nearX, nearZ) as it goes: clear of the noble district, the town centre's and the post's
     * blocks, the gate avenues and what the plan has placed already. Left out if no run of blocks holds it.
     */
    private static void inCity(CityLayout.Site site, Kind kind, @Nullable CityStyles.Building b, int nearX, int nearZ,
                               int nx0, int nx1, int nz0, int nz1, List<Piece> pieces) {
        if (b == null) {
            return;
        }
        int bps = site.blocksPerSide();
        int gate = bps / 2; // the avenue runs along the west and north edges of this block
        List<Piece> taken = pieces.stream().filter(p -> p.kind() != Kind.WALL && p.kind() != Kind.NOBLE).toList();
        List<int[]> tries = new ArrayList<>(); // first block x and z, blocks along x and z, turns
        for (int turns = 0; turns < 2; turns++) {
            int nx = blocksFor(turns == 0 ? b.x() : b.z());
            int nz = blocksFor(turns == 0 ? b.z() : b.x());
            for (int x = 0; x + nx <= bps; x++) {
                for (int z = 0; z + nz <= bps; z++) {
                    if (!(x < gate && x + nx > gate) && !(z < gate && z + nz > gate)) {
                        tries.add(new int[]{x, z, nx, nz, turns});
                    }
                }
            }
        }
        // Nearest first, by the doubled distance between the run's middle and the wanted block's.
        tries.sort(java.util.Comparator.comparingInt(t -> sq(2 * t[0] + t[2] - 1 - 2 * nearX) + sq(2 * t[1] + t[3] - 1 - 2 * nearZ)));
        for (int[] t : tries) {
            if (!blocksFree(site, t, nx0, nx1, nz0, nz1)) {
                continue;
            }
            int w = t[4] == 0 ? b.x() : b.z();
            int d = t[4] == 0 ? b.z() : b.x();
            int sx0 = CityLayout.blockSpan(bps, t[0])[0];
            int sx1 = CityLayout.blockSpan(bps, t[0] + t[2] - 1)[1];
            int sz0 = CityLayout.blockSpan(bps, t[1])[0];
            int sz1 = CityLayout.blockSpan(bps, t[1] + t[3] - 1)[1];
            if (w > sx1 - sx0 + 1 || d > sz1 - sz0 + 1) {
                continue; // the ring road or the avenue's extra width leaves an edge run too short
            }
            Piece p = new Piece(kind, b.file(), (sx0 + sx1 + 1 - w) / 2, (sz0 + sz1 + 1 - d) / 2, w, d, b.y(), t[4]);
            if (clear(p, taken) && reachable(site, p)) {
                pieces.add(p);
                return;
            }
        }
    }

    /** Blocks needed side by side to hold {@code length}: n blocks merged hold n road periods less one road. */
    static int blocksFor(int length) {
        return Math.max(1, (length + CityLayout.ROAD_WIDTH + CityLayout.ROAD_PERIOD - 1) / CityLayout.ROAD_PERIOD);
    }

    private static int sq(int v) {
        return v * v;
    }

    /** Whether none of a run's blocks is the noble district's, the town centre's or the post's. */
    private static boolean blocksFree(CityLayout.Site site, int[] t, int nx0, int nx1, int nz0, int nz1) {
        for (int x = t[0]; x < t[0] + t[2]; x++) {
            for (int z = t[1]; z < t[1] + t[3]; z++) {
                if (x >= nx0 && x < nx1 && z >= nz0 && z < nz1 || x == site.centerBlock() && z == site.centerBlock()
                        || x == RecruitmentPost.blockX(site) && z == RecruitmentPost.blockZ(site)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** The pieces with their height counted from the plate, their folder's y offset included (read once here, not per column). */
    private static List<Piece> lifted(List<Piece> pieces) {
        return pieces.stream().map(p -> p.file() == null ? p : new Piece(p.kind(), p.file(), p.minX(), p.minZ(), p.w(), p.d(),
                Math.max(0, p.h() + StyleLibrary.yOffset(p.file())), p.turns())).toList();
    }

    /**
     * The noble district's first block along x and z and its side, in blocks: two fifths of the city's side, about a
     * sixth of its area, in the middle ({@code edge} -1) or along the middle of its north, east, south or west edge (0 to 3).
     */
    static int[] district(int blocksPerSide, int edge) {
        int side = Math.max(1, blocksPerSide * 2 / 5);
        int middle = (blocksPerSide - side) / 2;
        int x = edge == 3 ? 0 : edge == 1 ? blocksPerSide - side : middle;
        int z = edge == 0 ? 0 : edge == 2 ? blocksPerSide - side : middle;
        return new int[]{x, z, side};
    }

    /** Whether a junction centred on any of {@code joins} would overlap the outer wall's gate at {@code avenue}, the widest of each counted. */
    static boolean hitsGate(CityLayout.Site site, int @Nullable [] joins, int avenue, List<CityStyles.Building> junctions) {
        if (joins == null) {
            return false;
        }
        int gate = orElse(CityStyles.own(site, CityStyles.GATES), CityStyles.own(site, CityStyles.WALLS)).stream()
                .mapToInt(CityStyles.Building::x).max().orElse(0);
        int junction = junctions.stream().mapToInt(CityStyles.Building::x).max().orElse(0);
        for (int at : joins) {
            if (Math.abs(at - avenue) < (gate + junction) / 2 + 1) {
                return true;
            }
        }
        return false;
    }

    /** Moves a box's side (north, east, south, west) in by {@code by}; the box is {west, east, north, south}, east and south exclusive. */
    private static void shrink(int[] box, int side, int by) {
        switch (side) {
            case 0 -> box[2] += by;
            case 1 -> box[1] -= by;
            case 2 -> box[3] -= by;
            default -> box[0] += by;
        }
    }

    /**
     * How far a city's wall stands out past its square: the deepest file in its style's wall folders, less the 3 blocks
     * it stands in from the square's edge; the mod's own wall's where the style has no walls of its own.
     */
    public static int wallOutset(CityLayout.Site site) {
        if (ConquestRuntime.map() == null) {
            return CityLayout.WALL_OUTSET; // no styles known yet; not cached
        }
        int inner = CityLayout.WALL_WIDTH - CityLayout.WALL_OUTSET; // as make() lays the outer ring
        return OUTSETS.computeIfAbsent(site, s -> Math.max(CityLayout.WALL_OUTSET, wallDepth(s) - inner));
    }

    private static final Map<CityLayout.Site, Integer> OUTSETS = new ConcurrentHashMap<>();

    /** The deepest of the style's own wall pieces (corners either way), or 0 if it has no wall segments of its own. */
    private static int wallDepth(CityLayout.Site site) {
        if (CityStyles.own(site, CityStyles.WALLS).isEmpty()) {
            return 0;
        }
        int depth = 0;
        for (String kind : List.of(CityStyles.WALLS, CityStyles.GATES, CityStyles.TOWERS)) {
            for (CityStyles.Building b : CityStyles.own(site, kind)) {
                depth = Math.max(depth, b.z());
            }
        }
        for (CityStyles.Building b : CityStyles.own(site, CityStyles.CORNERS)) {
            depth = Math.max(depth, Math.max(b.x(), b.z()));
        }
        return depth;
    }

    /**
     * A ring of walls from the style's own wall files around a box {west, east, north, south} (east and south
     * exclusive), standing just outside it, with a gate on each side at {@code gateX} along the north and south sides and
     * {@code gateZ} along the others. Side {@code open} (north, east, south, west; -1 for none) is left out; where the
     * style has a Wall Tower Junction its two corners are too, the junctions standing in the outer wall instead. Side
     * {@code joinSide} has a junction centred on each of {@code joins} in place of its segments there. False (and none)
     * if the style has no wall segments of its own.
     */
    private static boolean walls(CityLayout.Site site, int[] box, int gateX, int gateZ, int open, int joinSide, int @Nullable [] joins,
                                 Random random, List<Piece> pieces) {
        List<CityStyles.Building> segments = CityStyles.own(site, CityStyles.WALLS);
        if (segments.isEmpty()) {
            return false;
        }
        List<CityStyles.Building> gates = orElse(CityStyles.own(site, CityStyles.GATES), segments);
        List<CityStyles.Building> towers = CityStyles.own(site, CityStyles.TOWERS);
        List<CityStyles.Building> corners = orElse(CityStyles.own(site, CityStyles.CORNERS), orElse(towers, segments));
        List<CityStyles.Building> junctions = CityStyles.own(site, CityStyles.JUNCTIONS);
        // The corners, each over its corner of the band, turned with the side it starts.
        for (int c = 0; c < 4; c++) {
            if (!junctions.isEmpty() && (open == c || open == (c + 3) % 4)) {
                continue;
            }
            CityStyles.Building b = corners.get(random.nextInt(corners.size()));
            int w = c % 2 == 0 ? b.x() : b.z();
            int d = c % 2 == 0 ? b.z() : b.x();
            int minX = c == 0 || c == 3 ? box[0] - w : box[1];
            int minZ = c < 2 ? box[2] - d : box[3];
            pieces.add(new Piece(Kind.WALL, b.file(), minX, minZ, w, d, b.y(), c));
        }
        for (int side = 0; side < 4; side++) {
            if (side == open) {
                continue;
            }
            boolean alongX = side % 2 == 0;
            // The pieces set in the side (its gate, and any junctions), by where they start; segments fill between them.
            java.util.TreeMap<Integer, CityStyles.Building> fixed = new java.util.TreeMap<>();
            CityStyles.Building gate = gates.get(random.nextInt(gates.size()));
            fixed.put((alongX ? gateX : gateZ) - gate.x() / 2, gate);
            if (side == joinSide && joins != null) {
                for (int at : joins) {
                    CityStyles.Building j = junctions.get(random.nextInt(junctions.size()));
                    fixed.put(at - j.x() / 2, j);
                }
            }
            int t = alongX ? box[0] : box[2];
            for (Map.Entry<Integer, CityStyles.Building> f : fixed.entrySet()) {
                along(side, box, f.getValue(), f.getKey(), pieces);
                run(side, box, t, f.getKey() - 1, segments, towers, random, pieces);
                t = f.getKey() + f.getValue().x();
            }
            run(side, box, t, (alongX ? box[1] : box[3]) - 1, segments, towers, random, pieces);
        }
        return true;
    }

    private static List<CityStyles.Building> orElse(List<CityStyles.Building> first, List<CityStyles.Building> second) {
        return first.isEmpty() ? second : first;
    }

    /** Fills a side from {@code from} to {@code to} (inclusive) with segments, a watchtower after every few; the last flush with the end. */
    private static void run(int side, int[] box, int from, int to, List<CityStyles.Building> segments, List<CityStyles.Building> towers,
                            Random random, List<Piece> pieces) {
        int t = from;
        int n = 0;
        while (t <= to) {
            boolean tower = !towers.isEmpty() && n % (TOWER_EVERY + 1) == TOWER_EVERY;
            CityStyles.Building b = tower ? towers.get(random.nextInt(towers.size())) : segments.get(random.nextInt(segments.size()));
            if (t + b.x() - 1 > to) {
                along(side, box, b, to - b.x() + 1, pieces);
                return;
            }
            along(side, box, b, t, pieces);
            t += b.x();
            n++;
        }
    }

    /** A wall piece along a side of the box, starting {@code t} along it: north, east, south, west, facing out. */
    private static void along(int side, int[] box, CityStyles.Building b, int t, List<Piece> pieces) {
        int len = b.x();
        int depth = b.z();
        Piece p = switch (side) {
            case 0 -> new Piece(Kind.WALL, b.file(), t, box[2] - depth, len, depth, b.y(), 0);
            case 1 -> new Piece(Kind.WALL, b.file(), box[1], t, depth, len, b.y(), 1);
            case 2 -> new Piece(Kind.WALL, b.file(), t, box[3], len, depth, b.y(), 2);
            default -> new Piece(Kind.WALL, b.file(), box[0] - depth, t, depth, len, b.y(), 3);
        };
        pieces.add(p);
    }

    /**
     * The noble district within [x0, x1] × [z0, z1] (inclusive): the Palace, the Barracks (or the mod's post), then
     * the noble buildings.
     */
    private static void noble(CityLayout.Site site, int x0, int x1, int z0, int z1, Random random, List<Piece> pieces) {
        int middleX = (x0 + x1) / 2;
        int middleZ = (z0 + z1) / 2;
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
            palace = place(site, Kind.PALACE, CityStyles.building(file), middleX, middleZ, x0, x1, z0, z1, taken);
            if (palace != null) {
                taken.add(palace);
                break;
            }
        }
        int nearX = palace == null ? middleX : palace.centreX();
        int nearZ = palace == null ? middleZ : palace.centreZ();
        Piece b = place(site, Kind.BARRACKS, CityStyles.building(barracks), nearX, nearZ, x0, x1, z0, z1, taken);
        if (b != null) {
            taken.add(b);
        } else if (barracks != null && clear(ownPost, taken)) {
            taken.add(ownPost);
        }
        pieces.addAll(taken);
        // The rest: the noble buildings, packed as a city block is, each facing the nearer edge of the district.
        List<CityStyles.Building> noble = CityStyles.buildings(site, CityStyles.NOBLE);
        if (noble.isEmpty()) {
            return;
        }
        int spanX = x1 - x0 + 1;
        int spanZ = z1 - z0 + 1;
        List<BlockPacker.Rect> free = new ArrayList<>(List.of(new BlockPacker.Rect(0, 0, spanX, spanZ)));
        for (Piece p : taken) {
            free = BlockPacker.cut(free, new BlockPacker.Rect(p.minX() - x0 - CLEARANCE, p.minZ() - z0 - CLEARANCE,
                    p.w() + 2 * CLEARANCE, p.d() + 2 * CLEARANCE));
        }
        List<BlockPacker.Size> sizes = noble.stream().map(n -> new BlockPacker.Size(n.x(), n.z())).toList();
        for (BlockPacker.Placed p : BlockPacker.pack(free, spanX, spanZ, sizes, random)) {
            CityStyles.Building n = noble.get(p.building());
            boolean turned = p.turns() % 2 == 1;
            Piece piece = new Piece(Kind.NOBLE, n.file(), x0 + p.x(), z0 + p.z(), turned ? n.z() : n.x(), turned ? n.x() : n.z(), n.y(), p.turns());
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
    private static Piece place(CityLayout.Site site, Kind kind, @Nullable CityStyles.Building b, int x, int z, int x0, int x1, int z0, int z1, List<Piece> taken) {
        if (b == null || b.x() > x1 - x0 + 1 || b.z() > z1 - z0 + 1) {
            return null;
        }
        int reach = Math.max(x1 - x0, z1 - z0);
        for (int r = 0; r <= reach; r += SEARCH_STEP) {
            // The ring r out: its top and bottom rows, then its sides between them.
            for (int i = -r; i <= r; i += SEARCH_STEP) {
                for (int[] d : r == 0 ? new int[][]{{0, 0}} : new int[][]{{i, -r}, {i, r}, {-r, i}, {r, i}}) {
                    Piece p = new Piece(kind, b.file(), x + d[0] - b.x() / 2, z + d[1] - b.z() / 2, b.x(), b.z(), b.y(), 0);
                    if (p.minX() >= x0 && p.minZ() >= z0 && p.minX() + p.w() - 1 <= x1 && p.minZ() + p.d() - 1 <= z1
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
        CityLayout.District start = new CityLayout.District(site, cellOf(cells, p.centreX()), cellOf(cells, p.centreZ()));
        int startX = start.x();
        int startZ = start.z();
        int x0 = site.originX() + p.minX();
        int z0 = site.originZ() + p.minZ();
        return (x0 >> 4) >= (startX >> 4) - REACH_CHUNKS && ((x0 + p.w() - 1) >> 4) <= (startX >> 4) + REACH_CHUNKS
                && (z0 >> 4) >= (startZ >> 4) - REACH_CHUNKS && ((z0 + p.d() - 1) >> 4) <= (startZ >> 4) + REACH_CHUNKS;
    }
}
