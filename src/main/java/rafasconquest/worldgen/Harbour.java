package rafasconquest.worldgen;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import rafasconquest.core.CityLayout;
import rafasconquest.core.SeatCities;

/**
 * A port city's harbour ({@link SeatCities.City#harbour}): outside the wall on its harbour side, a wharf
 * {@link #WHARF} blocks wide the length of the side, and from it a pier out over the water at the sea gate's avenue and
 * every second road either side of it. A pier runs out to where the water starts, then {@link #OVER_WATER} blocks
 * on; one that finds no water within its cell's reach is left out. All at the city's plate height.
 * <p>
 * Each edge cell on that side builds its own stretch, and keeps every pier within the engine's 8-chunk reach of its
 * structure start.
 */
final class Harbour {
    static final int WHARF = 5;
    static final int PIER_WIDTH = 5;
    static final int OVER_WATER = 32;
    /** A pier with less than this over the water is not worth building. */
    static final int MIN_OVER_WATER = 8;
    private static final int SCAN_STEP = 2;

    private Harbour() {
    }

    /** The side a built city's harbour faces, or null for an inland city. */
    static CityLayout.Side sideOf(CityLayout.Site site) {
        for (SeatCities.City c : ConquestRuntime.cities().all()) {
            if (c.x() == site.x() && c.z() == site.z()) {
                return c.port() ? CityLayout.Side.values()[c.harbour()] : null;
            }
        }
        return null;
    }

    /** Adds the harbour's pieces cell (cellX, cellZ) builds, if any. */
    static void build(Structure.GenerationContext ctx, CityLayout.Site site, int cellX, int cellZ, int plate, StructurePiecesBuilder pieces) {
        CityLayout.Side side = sideOf(site);
        if (side == null) {
            return;
        }
        int last = site.cells() - 1;
        int cellAlong = switch (side) {
            case NORTH -> cellZ == 0 ? cellX : -1;
            case SOUTH -> cellZ == last ? cellX : -1;
            case WEST -> cellX == 0 ? cellZ : -1;
            case EAST -> cellX == last ? cellZ : -1;
        };
        if (cellAlong < 0) {
            return;
        }
        Frame f = new Frame(site, side);
        int wall = rafasconquest.style.CityPlan.wallOutset(site);
        // The furthest out (blocks past the square) this cell's start can reach: the edge of its 8th chunk out.
        CityLayout.District start = new CityLayout.District(site, cellX, cellZ);
        int reach = switch (side) {
            case NORTH -> site.originZ() - (((start.z() >> 4) - 8) << 4);
            case SOUTH -> ((((start.z() >> 4) + 8) << 4) + 15) - (site.originZ() + site.size() - 1);
            case WEST -> site.originX() - (((start.x() >> 4) - 8) << 4);
            case EAST -> ((((start.x() >> 4) + 8) << 4) + 15) - (site.originX() + site.size() - 1);
        };
        int wharfIn = wall + 1;
        int wharfOut = wall + WHARF;
        if (wharfOut > reach) {
            return;
        }
        pieces.addPiece(f.piece(CityLayout.cellMin(cellAlong), CityLayout.cellMax(cellAlong), wharfIn, wharfOut, plate, false));
        int blocks = site.blocksPerSide();
        int gate = blocks / 2;
        for (int k : CityLayout.roadsInCell(site.cells(), cellAlong)) {
            if (k <= 0 || k >= blocks || Math.floorMod(k - gate, 2) != 0) {
                continue;
            }
            int middle = CityLayout.roadStart(k) + CityLayout.ROAD_WIDTH / 2;
            Integer water = null;
            for (int out = wharfOut + 1; out <= reach; out += SCAN_STEP) {
                int[] at = f.world(middle, out);
                if (CityGridBuilder.ground(ctx, at[0], at[1]).water()) {
                    water = out;
                    break;
                }
            }
            if (water == null || water + MIN_OVER_WATER > reach) {
                continue;
            }
            int end = Math.min(reach, water + OVER_WATER);
            pieces.addPiece(f.piece(middle - PIER_WIDTH / 2, middle + PIER_WIDTH / 2, wharfOut + 1, end, plate, true));
        }
    }

    /** One harbour side: (along, out) to world, where along is city-local along the side and out is blocks past the square. */
    private record Frame(CityLayout.Site site, CityLayout.Side side) {
        int[] world(int along, int out) {
            int ox = site.originX();
            int oz = site.originZ();
            int far = site.size() - 1;
            return switch (side) {
                case NORTH -> new int[]{ox + along, oz - out};
                case SOUTH -> new int[]{ox + along, oz + far + out};
                case WEST -> new int[]{ox - out, oz + along};
                case EAST -> new int[]{ox + far + out, oz + along};
            };
        }

        HarbourPiece piece(int along0, int along1, int out0, int out1, int deckY, boolean pier) {
            int[] a = world(along0, out0);
            int[] b = world(along1, out1);
            return new HarbourPiece(Math.min(a[0], b[0]), Math.min(a[1], b[1]), Math.max(a[0], b[0]), Math.max(a[1], b[1]), deckY, pier);
        }
    }
}
