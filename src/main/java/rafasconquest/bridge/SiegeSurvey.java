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
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

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

    /** A wall's own tower juts at least this far out past its face, or rises at least this far over its merlons. */
    private static final int TOWER_JUT = 2;
    private static final int TOWER_RISE = 4;

    /**
     * One side's wall: its outer face's distance from the middle, thickness, walkway height, the length of wall found,
     * and its own towers, each from end to end along it (in the survey's steps across the side, as {@link #wall} reads).
     */
    public record Wall(Direction side, int face, int thickness, int walkY, int length, List<BlockPos> climbs, List<int[]> towers) {
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

        /** The walkway of the wall nearest ({@code x}, {@code z}): walls are manned all round. */
        public int walkYAt(double x, double z) {
            double[] in = {z - plan.minZ(), plan.maxX() - x, plan.maxZ() - z, x - plan.minX()}; // north, east, south, west, as walls
            int best = 0;
            for (int i = 1; i < in.length; i++) {
                best = Math.abs(in[i]) < Math.abs(in[best]) ? i : best;
            }
            return walls.get(best).walkY();
        }

        /** The walkway of the wall the way out ({@code outX}, {@code outZ}) leads from; the attacked wall's if none such. */
        public int walkY(double outX, double outZ) {
            return walls.stream().filter(w -> w.side().getStepX() == Math.round(outX) && w.side().getStepZ() == Math.round(outZ))
                    .findFirst().map(Wall::walkY).orElse(walkY());
        }

        /** The survey, as lines for the caller: the defender's part, then the attacker's. */
        public List<String> report(String name) {
            List<String> lines = new ArrayList<>();
            lines.add("Survey of " + name + ", for the defender:");
            for (Wall w : walls) {
                lines.add(String.format(Locale.ROOT, "  %s wall: %d long, %d thick, walkway at y %d, %d towers; %d stairs/ladders up inside",
                        w.side().getName(), w.length(), w.thickness(), w.walkY(), w.towers().size(), w.climbs().size()));
            }
            lines.add("  Gate Blocks: " + (gates.isEmpty() ? "none" : gates.stream()
                    .map(p -> p.getX() + " " + p.getY() + " " + p.getZ()).toList()));
            lines.add("For the attacker:");
            for (Land l : land) {
                lines.add(String.format(Locale.ROOT, "  %s: roughness %.1f, %d%% water%s", l.side().getName(), l.roughness(),
                        Math.round(l.wet() * 100), l.water() ? " (large body of water: not attacked)" : ""));
            }
            lines.add("  Main attack from the " + attacked.getName() + " with " + plan.engines().stream()
                    .map(e -> e.name().toLowerCase(Locale.ROOT)).toList() + "; the walls surrounded"
                    + (plan.barred().isEmpty() ? "" : ", save the " + plan.barred().stream()
                    .map(o -> Direction.fromDelta((int) o.x(), 0, (int) o.z()).getName()).toList()));
            return lines;
        }
    }

    private SiegeSurvey() {
    }

    /** As {@link #survey}, once the chunks it reads are loaded by tickets, so the server thread never waits on them. */
    public static java.util.concurrent.CompletableFuture<Result> surveyAsync(ServerLevel level, int x, int z, int half, @Nullable Point hall,
                                                                             @Nullable Direction port, Set<Siege.Engine> engines) {
        // The walls are read from 8 out to 30 in from where they should stand, Gate Blocks within 16 of them.
        int outer = half + 16;
        int inner = half - 32;
        it.unimi.dsi.fastutil.longs.LongArrayList ring = new it.unimi.dsi.fastutil.longs.LongArrayList();
        for (int cx = (x - outer) >> 4; cx <= (x + outer) >> 4; cx++) {
            for (int cz = (z - outer) >> 4; cz <= (z + outer) >> 4; cz++) {
                int minX = cx << 4;
                int minZ = cz << 4;
                if (!(minX > x - inner && minX + 15 < x + inner && minZ > z - inner && minZ + 15 < z + inner)) {
                    ring.add(net.minecraft.world.level.ChunkPos.asLong(cx, cz));
                }
            }
        }
        return ChunkWait.load(level, ring).thenApply(v -> survey(level, x, z, half, hall, port, engines));
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
        Direction noble = nobleSide(x, z);
        Direction attacked = attackSide(land, port, noble);
        Wall front = walls.get(Arrays.asList(SIDES).indexOf(attacked));
        // The army surrounds the walls, save those it can't come at: water before them, the port, the noble district.
        List<Point> barred = land.stream().filter(l -> l.side() != attacked && rank(l, port, noble) > 0)
                .map(l -> new Point(l.side().getStepX(), l.side().getStepZ())).toList();
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
        // Every wall's own towers, end to end on its face, for siege towers to keep clear of.
        List<Siege.WallTower> towers = new ArrayList<>();
        for (Wall w : walls) {
            int ax = w.side().getStepX();
            int az = w.side().getStepZ();
            for (int[] t : w.towers()) {
                towers.add(new Siege.WallTower(new Point(x + ax * w.face() - az * t[0] + 0.5, z + az * w.face() + ax * t[0] + 0.5),
                        new Point(x + ax * w.face() - az * t[1] + 0.5, z + az * w.face() + ax * t[1] + 0.5)));
            }
        }
        // The box of the walls' outer faces, block edges; a wall not found is taken to be a block's front long.
        Siege.Plan plan = new Siege.Plan(x - walls.get(3).face(), z - walls.get(0).face(), x + walls.get(1).face() + 1,
                z + walls.get(2).face() + 1, thickness, Math.max(rafasconquest.battle.BattlePlan.BLOCK_PITCH, front.length()),
                attacked.getStepX(), attacked.getStepZ(), gates, hall != null ? hall : new Point(x + 0.5, z + 0.5), engines, towers, barred);
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
        List<Integer> tops = new ArrayList<>();
        List<Integer> along = new ArrayList<>();
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
            int top = height(level, x + ax * (face - 1) + rx * u, z + az * (face - 1) + rz * u);
            // Under its roof, if it has one: stepping in past a parapet that stands solid up to the roof, which has no
            // floor to stand on, so its reading would be the roof's top.
            int walk = top; // an open walkway: on top
            for (int in = 1; in <= WALK_PROBE; in++) {
                int cx = x + ax * (face - in) + rx * u;
                int cz = z + az * (face - in) + rz * u;
                int floor = stand(level, cx, cz);
                if (floor < height(level, cx, cz)) {
                    walk = floor;
                    break;
                }
            }
            int t = 0;
            while (t < 16 && height(level, x + ax * (face - t) + rx * u, z + az * (face - t) + rz * u) >= top - 1) {
                t++;
            }
            found++;
            faces.add(face);
            thicknesses.add(t);
            walks.add(walk);
            tops.add(top);
            along.add(u);
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
        // The wall's own towers: where it juts out past its face, or rises well over its merlons; runs of such readings.
        List<int[]> towers = new ArrayList<>();
        if (!faces.isEmpty()) {
            int face = median(faces);
            int top = median(tops);
            for (int i = 0; i < along.size(); i++) {
                if (faces.get(i) - face < TOWER_JUT && tops.get(i) - top < TOWER_RISE) {
                    continue;
                }
                int u = along.get(i);
                int[] last = towers.isEmpty() ? null : towers.get(towers.size() - 1);
                if (last != null && u - last[1] <= 3) {
                    last[1] = u + 1;
                } else {
                    towers.add(new int[]{u - 1, u + 1});
                }
            }
        }
        return new Wall(out, faces.isEmpty() ? half : median(faces), thicknesses.isEmpty() ? 1 : median(thicknesses),
                walks.isEmpty() ? height(level, x, z) : median(walks), found * 2, climbs, towers);
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
                // Unloaded land from the world map: loading it here generated hundreds of chunks on the server thread (40 s).
                // shortcut: map ground is per-pixel, so unloaded sides read smoother than loaded ones; fine for picking a side.
                BaseMap map = level.hasChunk(px >> 4, pz >> 4) ? null : ConquestRuntime.map();
                int i = map == null ? -1 : map.pixelAt(px, pz);
                int h = i >= 0 ? map.ground()[i] : height(level, px, pz);
                samples++;
                if (i >= 0 ? map.wet()[i] : !level.getFluidState(new BlockPos(px, h - 1, pz)).isEmpty()) {
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

    /** A roof stands at most this far above the floor under it. */
    private static final int ROOF_GAP = 5;
    /** How many blocks in from a wall's outer face to look for the floor under its roof. */
    private static final int WALK_PROBE = 4;

    /**
     * Where a soldier stands at ({@code x}, {@code z}): on the highest floor under a roof (solid underfoot, two blocks
     * clear above, the roof at most {@link #ROOF_GAP} up), else on top. shortcut: a room just under an open walkway
     * reads as a roofed floor; walls built so would need their walkway marked.
     */
    public static int stand(ServerLevel level, int x, int z) {
        int top = height(level, x, z);
        for (int y = top - 2; y >= top - ROOF_GAP; y--) {
            if (standable(level, x, y, z)) {
                return y;
            }
        }
        return top;
    }

    /** The standable height nearest {@code near} at ({@code x}, {@code z}), within 3 up or down; else {@link #stand}. */
    public static int standNear(ServerLevel level, int x, int z, int near) {
        height(level, x, z); // loads the chunk
        for (int d = 0; d <= 3; d++) {
            for (int y : new int[]{near + d, near - d}) {
                if (standable(level, x, y, z)) {
                    return y;
                }
            }
        }
        return stand(level, x, z);
    }

    private static boolean standable(ServerLevel level, int x, int y, int z) {
        BlockPos feet = new BlockPos(x, y, z);
        return !level.getBlockState(feet.below()).getCollisionShape(level, feet.below()).isEmpty()
                && level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                && level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty();
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
