package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.Siege;
import rafasconquest.content.GateBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Looks a fortress over before a siege, in the world as it stands. For the defender: each side's wall (how far out its
 * face is, how thick and high it is, how long a stretch soldiers can stand on), its gates (Gate Blocks), and the stairs
 * and ladders inside that climb onto it. For the attacker: the land outside each side, to come at the flattest that
 * isn't water. Reads the world on the server thread (a castle's few chunks; a city's ring of them).
 */
public final class SiegeSurvey {
    /** A side's land counts as water with this share of it wet; such a side is not attacked if any other will do. */
    private static final double TOO_WET = 0.3;
    /** How far out the attacker looks at the land, and how often. */
    private static final int LAND_FROM = 10;
    private static final int LAND_TO = 60;
    private static final int LAND_STEP = 5;
    /** A wall stands at least this high over the ground outside it. */
    private static final int MIN_WALL = 3;

    /** The four sides, by the way out from them: north, east, south, west. */
    private static final Direction[] SIDES = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    /** One side's wall: its outer face's distance from the middle, thickness, walkway height, the length of wall found. */
    public record Wall(Direction side, int face, int thickness, int walkY, int length, List<BlockPos> climbs) {
    }

    /**
     * One side's land outside, for the attacker: how rough, how wet, and whether a large body of water (a lake, the sea, a
     * river across the way in) lies before it.
     */
    public record Land(Direction side, double roughness, double wet, boolean water) {
    }

    /** What the survey found, and the plan for the siege it makes ({@code gates} in the order of the plan's). */
    public record Result(Siege.Plan plan, List<Wall> walls, List<BlockPos> gates, List<Land> land, Direction attacked) {
        /** The attacked wall's walkway: the height soldiers stand at on it. */
        public int walkY() {
            return walls.stream().filter(w -> w.side() == attacked).findFirst().map(Wall::walkY).orElse(0);
        }

        /** The survey, as lines for the caller: the defender's part, then the attacker's. */
        public List<String> report(String name) {
            List<String> lines = new ArrayList<>();
            lines.add("Survey of " + name + ", for the defender:");
            for (Wall w : walls) {
                lines.add(String.format(Locale.ROOT, "  %s wall: %d long, %d thick, walkway at y %d; %d stairs/ladders up inside",
                        w.side().getName(), w.length(), w.thickness(), w.walkY(), w.climbs().size()));
            }
            lines.add("  Gate Blocks: " + (gates.isEmpty() ? "none" : gates.stream()
                    .map(p -> p.getX() + " " + p.getY() + " " + p.getZ()).toList()));
            lines.add("For the attacker:");
            for (Land l : land) {
                lines.add(String.format(Locale.ROOT, "  %s: roughness %.1f, %d%% water%s", l.side().getName(), l.roughness(),
                        Math.round(l.wet() * 100), l.water() ? " (large body of water: not attacked)" : ""));
            }
            lines.add("  Attack from the " + attacked.getName() + " with " + plan.engines().stream()
                    .map(e -> e.name().toLowerCase(Locale.ROOT)).toList());
            return lines;
        }
    }

    private SiegeSurvey() {
    }

    /**
     * Surveys the fortress about ({@code x}, {@code z}) whose walls stand about {@code half} out each way. The defending
     * general's council meets at {@code hall} (null: the middle). The side its harbour opens on, {@code port} (null: none), is
     * never attacked, nor a seat city's side with its noble district outside the walls.
     */
    public static Result survey(ServerLevel level, int x, int z, int half, @Nullable Point hall, @Nullable Direction port,
                                Set<Siege.Engine> engines) {
        List<Wall> walls = new ArrayList<>();
        for (Direction out : SIDES) {
            walls.add(wall(level, x, z, half, out));
        }
        List<BlockPos> gateBlocks = gateBlocks(level, x, z, half);
        List<Land> land = new ArrayList<>();
        for (int s = 0; s < SIDES.length; s++) {
            land.add(land(level, x, z, walls.get(s).face(), SIDES[s]));
        }
        Direction attacked = attackSide(land, port, nobleSide(x, z));
        Wall front = walls.get(Arrays.asList(SIDES).indexOf(attacked));
        int thickness = Math.max(1, (int) Math.round(walls.stream().filter(w -> w.length() > 0).mapToInt(Wall::thickness).average().orElse(1)));
        List<Siege.Gate> gates = new ArrayList<>();
        for (BlockPos g : new ArrayList<>(gateBlocks)) {
            Siege.Gate gate = gate(level, g);
            if (gate != null) {
                gates.add(gate);
            } else {
                gateBlocks.remove(g); // no doors set: no gate to fight for
            }
        }
        // The box of the walls' outer faces, block edges; a wall not found is taken to be a block's front long.
        Siege.Plan plan = new Siege.Plan(x - walls.get(3).face(), z - walls.get(0).face(), x + walls.get(1).face() + 1,
                z + walls.get(2).face() + 1, thickness, Math.max(rafasconquest.battle.BattlePlan.BLOCK_PITCH, front.length()),
                attacked.getStepX(), attacked.getStepZ(), gates, hall != null ? hall : new Point(x + 0.5, z + 0.5), engines);
        return new Result(plan, walls, gateBlocks, land, attacked);
    }

    /**
     * A side's wall, read across it every two blocks: walking in from outside, its outer face is where the ground first
     * rises {@link #MIN_WALL} over the land outside; its walkway the height just behind the merlons; its thickness how
     * far that height goes on.
     */
    private static Wall wall(ServerLevel level, int x, int z, int half, Direction out) {
        int ax = out.getStepX();
        int az = out.getStepZ();
        int rx = -az; // along the wall
        int rz = ax;
        List<Integer> faces = new ArrayList<>();
        List<Integer> thicknesses = new ArrayList<>();
        List<Integer> walks = new ArrayList<>();
        List<BlockPos> climbs = new ArrayList<>();
        int found = 0;
        for (int u = -half + 2; u <= half - 2; u += 2) {
            int outside = height(level, x + ax * (half + 8) + rx * u, z + az * (half + 8) + rz * u);
            int face = Integer.MIN_VALUE;
            for (int d = half + 6; d >= half - 12; d--) {
                if (height(level, x + ax * d + rx * u, z + az * d + rz * u) >= outside + MIN_WALL) {
                    face = d;
                    break;
                }
            }
            if (face == Integer.MIN_VALUE) {
                continue;
            }
            int walk = height(level, x + ax * (face - 1) + rx * u, z + az * (face - 1) + rz * u);
            int t = 0;
            while (t < 16 && height(level, x + ax * (face - t) + rx * u, z + az * (face - t) + rz * u) >= walk - 1) {
                t++;
            }
            found++;
            faces.add(face);
            thicknesses.add(t);
            walks.add(walk);
            // Stairs and ladders just inside the wall's inner face, or in its thickness, climbing onto it.
            for (int d = face; d >= face - t - 2; d--) {
                int cx = x + ax * d + rx * u;
                int cz = z + az * d + rz * u;
                for (int y = outside - 2; y <= walk + 1; y++) {
                    for (int du = 0; du < 2; du++) {
                        BlockPos p = new BlockPos(cx + rx * du, y, cz + rz * du);
                        var block = level.getBlockState(p).getBlock();
                        if ((block instanceof StairBlock || block instanceof LadderBlock)
                                && climbs.stream().noneMatch(c -> c.distManhattan(p) <= 3)) {
                            climbs.add(p);
                        }
                    }
                }
            }
        }
        return new Wall(out, faces.isEmpty() ? half : median(faces), thicknesses.isEmpty() ? 1 : median(thicknesses),
                walks.isEmpty() ? height(level, x, z) : median(walks), found * 2, climbs);
    }

    /** The land outside a side, out to {@link #LAND_TO}: how much it rises and falls, and how much of it is water. */
    private static Land land(ServerLevel level, int x, int z, int face, Direction out) {
        int ax = out.getStepX();
        int az = out.getStepZ();
        int rx = -az;
        int rz = ax;
        double rough = 0;
        int steps = 0;
        int wet = 0;
        int samples = 0;
        int rays = 0;
        int crossed = 0; // rays that cross water at least two samples wide
        int longest = 0; // the longest run of water along any ray, in samples
        for (int u = -face; u <= face; u += 6) {
            int last = Integer.MIN_VALUE;
            int run = 0;
            int widest = 0;
            rays++;
            for (int d = face + LAND_FROM; d <= face + LAND_TO; d += LAND_STEP) {
                int px = x + ax * d + rx * u;
                int pz = z + az * d + rz * u;
                int h = height(level, px, pz);
                samples++;
                if (!level.getFluidState(new BlockPos(px, h - 1, pz)).isEmpty()) {
                    wet++;
                    widest = Math.max(widest, ++run);
                } else {
                    run = 0;
                }
                if (last != Integer.MIN_VALUE) {
                    rough += Math.abs(h - last);
                    steps++;
                }
                last = h;
            }
            crossed += widest >= 2 ? 1 : 0;
            longest = Math.max(longest, widest);
        }
        double share = samples == 0 ? 0 : (double) wet / samples;
        // A large body of water: much of the land wet, a river across a third of the way in, or 20 blocks of water on end.
        boolean water = share >= TOO_WET || crossed * 3 >= rays && crossed > 0 || longest >= 4;
        return new Land(out, steps == 0 ? 0 : rough / steps, share, water);
    }

    /**
     * The side the attacker comes at: the flattest with neither the port, the noble district outside the walls, nor a
     * large body of water before it. If every side is barred, the least wet that isn't the port (the port's side only if
     * there is nothing else); never the noble district's.
     */
    static Direction attackSide(List<Land> land, @Nullable Direction port, @Nullable Direction noble) {
        Land best = null;
        for (Land l : land) {
            int r = rank(l, port, noble);
            int b = best == null ? 0 : rank(best, port, noble);
            if (best == null || r < b || r == b && (r == 0 ? l.roughness() < best.roughness() : l.wet() < best.wet())) {
                best = l;
            }
        }
        return best.side();
    }

    /** 0: open to attack; 1: water before it; 2: the port; 3: the noble district. */
    private static int rank(Land l, @Nullable Direction port, @Nullable Direction noble) {
        return l.side() == noble ? 3 : l.side() == port ? 2 : l.water() ? 1 : 0;
    }

    /** The side a seat city at exactly (x, z) has its noble district outside on, or null (none there, or inside). */
    @Nullable
    private static Direction nobleSide(int x, int z) {
        for (rafasconquest.core.SeatCities.City c : rafasconquest.worldgen.ConquestRuntime.cities().inBox(x, z, x, z)) {
            if (c.x() == x && c.z() == z && c.noble() >= 0) {
                return SIDES[c.noble()]; // CityLayout.Side and SIDES are both north, east, south, west
            }
        }
        return null;
    }

    /** Every Gate Block in the chunks along the walls (within 16 blocks of them, inside or out). */
    private static List<BlockPos> gateBlocks(ServerLevel level, int x, int z, int half) {
        List<BlockPos> found = new ArrayList<>();
        int outer = half + 16;
        int inner = half - 16;
        for (int cx = (x - outer) >> 4; cx <= (x + outer) >> 4; cx++) {
            for (int cz = (z - outer) >> 4; cz <= (z + outer) >> 4; cz++) {
                int minX = cx << 4;
                int minZ = cz << 4;
                boolean deepInside = minX > x - inner && minX + 15 < x + inner && minZ > z - inner && minZ + 15 < z + inner;
                if (deepInside) {
                    continue;
                }
                LevelChunk chunk = level.getChunk(cx, cz);
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (be instanceof GateBlockEntity) {
                        found.add(be.getBlockPos());
                    }
                }
            }
        }
        return found;
    }

    /** A gate for the siege: its mouth just outside its doors, its button (or the Gate Block), the way out through it. */
    @Nullable
    private static Siege.Gate gate(ServerLevel level, BlockPos at) {
        if (!(level.getBlockEntity(at) instanceof GateBlockEntity gate)) {
            return null;
        }
        Direction out = gate.facing().getOpposite();
        double mx = 0;
        double mz = 0;
        int doors = 0;
        for (GateBlockEntity.Door door : new GateBlockEntity.Door[]{gate.left, gate.right}) {
            if (door.box != null) {
                BoundingBox box = gate.world(door.box);
                mx += (box.minX() + box.maxX() + 1) / 2.0;
                mz += (box.minZ() + box.maxZ() + 1) / 2.0;
                doors++;
            }
        }
        if (doors == 0) {
            return null;
        }
        Point mouth = new Point(mx / doors + out.getStepX() * 1.5, mz / doors + out.getStepZ() * 1.5);
        BlockPos button = GateBlockEntity.button(level, at);
        BlockPos press = button != null ? button : at;
        return new Siege.Gate(mouth, new Point(press.getX() + 0.5, press.getZ() + 0.5), out.getStepX(), out.getStepZ());
    }

    /** A walled city's barracks (its recruiter's place), where its defenders' council meets; null if no city stands there. */
    @Nullable
    public static Point cityHall(ServerLevel level, int x, int z) {
        rafasconquest.sim.ConquestServer server = rafasconquest.sim.ConquestServer.get();
        if (server == null) {
            return null;
        }
        int r = rafasconquest.core.CityLayout.MAX_HALF_EXTENT;
        rafasconquest.core.CityLayout.Site best = null;
        for (rafasconquest.core.CityLayout.Site s : rafasconquest.core.CityLayout.allSitesInBox(level.getSeed(), server.data().settings(),
                x - r, z - r, x + r, z + r)) {
            if (best == null || Math.hypot(s.x() - x, s.z() - z) < Math.hypot(best.x() - x, best.z() - z)) {
                best = s;
            }
        }
        if (best == null) {
            return null;
        }
        int[] at = rafasconquest.core.Barracks.of(best).recruiter();
        return new Point(at[0] + 0.5, at[1] + 0.5);
    }

    private static int height(ServerLevel level, int x, int z) {
        // Through the chunk, which loads it: Level.getHeight reads an unloaded chunk as the world's floor.
        return level.getChunk(x >> 4, z >> 4).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
    }

    private static int median(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        sorted.sort(null);
        return sorted.get(sorted.size() / 2);
    }
}
