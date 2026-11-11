package rafasconquest.battle;

import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Deploying to the lie of the land, before the battle begins: each platoon ({@link BattlePlan#platoons}) moves whole
 * from its place in the template (up to {@link #REACH} away) to the best ground near it, its blocks keeping their places
 * in it. Armies hate water and keep out of it; they love high ground, archers most of all; and they keep together, each
 * platoon near its place and clear of the others, no further toward the enemy than a little. The front's platoons choose
 * first, and the platoons behind follow their files' fronts, then choose about that. Where a wood lies near a wing's
 * end, a few of that wing's reserve blocks hide in it as an ambush
 * ({@link FieldBlock.Task#AMBUSH}), to strike an enemy block in the flank or rear once it is fighting near them.
 */
public final class GroundPlan {
    /** What the land is like at a point. */
    public interface Ground {
        double height(double x, double z);

        boolean water(double x, double z);

        boolean forest(double x, double z);

        /**
         * Reads the whole area the deployment may look at, at once, before any block chooses: the search then only
         * looks things up. Ground that is costly to read (worked out from the world generator) reads faster all
         * together than spot by spot.
         */
        default void survey(double minX, double minZ, double maxX, double maxZ) {
        }
    }

    /** How far past the blocks' places the deployment may look: a follower's file shift and reach, its own size, an ambush. */
    private static final double SURVEY_MARGIN = 110;

    /** How far a block may move from its place in the template, and how finely the ground is searched. */
    public static final double REACH = 40;
    /** Where all within reach is water, a block looks twice as far, and again, up to this far. */
    private static final double MAX_REACH = 160;
    private static final double STEP = 5;
    /** The blocks behind the front search this far about the place their file's front gives them. */
    private static final double REACH_BEHIND = 20;
    /** A block may stand this much further toward the enemy than its place, no more. */
    private static final double MAX_FORWARD = 8;
    /** What a block of height is worth, by arm, against what a block of distance from its place costs. */
    private static final double ARCHER_HEIGHT = 1.5;
    private static final double INFANTRY_HEIGHT = 0.6;
    private static final double CAVALRY_HEIGHT = 0.2;
    private static final double COHESION = 0.08;
    /** Blocks keep at least this far apart, or pay for it. */
    private static final double CLEARANCE = 8;
    private static final double CROWDING = 40;
    /** A wood within this reach of a wing's end hides an ambush of at most this many blocks. */
    private static final double AMBUSH_REACH = 60;
    private static final int AMBUSH_BLOCKS = 3;
    private static final double AMBUSH_SPACING = 7;

    private GroundPlan() {
    }

    /**
     * The area a deployment may look at, as {minX, minZ, maxX, maxZ}: every block's place, and {@link #SURVEY_MARGIN}
     * around. Surveyed before deploying ({@link Ground#survey}), so the search only looks things up.
     */
    public static double[] area(FieldBattle field) {
        double minX = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;
        for (int side = 0; side < 2; side++) {
            for (FieldBlock b : field.army(side).blocks) {
                minX = Math.min(minX, b.line.x());
                minZ = Math.min(minZ, b.line.z());
                maxX = Math.max(maxX, b.line.x());
                maxZ = Math.max(maxZ, b.line.z());
            }
        }
        if (minX > maxX) {
            return new double[]{0, 0, -1, -1};
        }
        return new double[]{minX - SURVEY_MARGIN, minZ - SURVEY_MARGIN, maxX + SURVEY_MARGIN, maxZ + SURVEY_MARGIN};
    }

    /** Deploys both armies to the ground, and hides ambushes in the woods. Returns how many blocks hide in ambush. */
    public static int deploy(FieldBattle field, Ground ground) {
        double[] area = area(field);
        if (area[0] <= area[2]) {
            ground.survey(area[0], area[1], area[2], area[3]); // nothing left to read if it was surveyed already
        }
        int ambushers = 0;
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            if (field.afloat(side)) {
                continue; // a fleet keeps its places on the water
            }
            place(field, army, ground);
            ambushers += ambush(field, army, ground);
        }
        return ambushers;
    }

    private static void place(FieldBattle field, FieldArmy army, Ground ground) {
        double ref = 0;
        int n = 0;
        for (FieldBlock b : army.blocks) {
            ref += ground.height(b.line.x(), b.line.z());
            n++;
        }
        ref = n == 0 ? 0 : ref / n;
        List<Point> taken = new ArrayList<>();
        Map<Integer, Point> fileShift = new HashMap<>();
        // Each platoon moves whole, its blocks keeping their places in it; a block in no platoon moves alone. The
        // front's platoons first, then the archers' (to the high ground before the reserves take it), then the rest.
        Map<Integer, List<FieldBlock>> platoons = new LinkedHashMap<>();
        int alone = -1;
        for (FieldBlock b : army.blocks) {
            if (b.duty != Duty.GENERAL && b.duty != Duty.GUARD && b.duty != Duty.BEARER && !b.afloat) { // ships keep to the water
                platoons.computeIfAbsent(b.platoon >= 0 ? b.platoon : alone--, k -> new ArrayList<>()).add(b);
            }
        }
        List<List<FieldBlock>> order = new ArrayList<>(platoons.values());
        order.sort(Comparator.comparingInt((List<FieldBlock> p) -> front(p) ? 0 : p.get(0).arm == Arm.ARCHERS ? 1 : 2)
                .thenComparingDouble(p -> p.stream().mapToDouble(b -> BattlePlan.offset(army.origin, army.facingX, army.facingZ, b.line).z())
                        .min().orElse(0)));
        for (List<FieldBlock> p : order) {
            // Behind the front, a platoon starts where its blocks' files' fronts moved it, on the whole.
            double sx = 0;
            double sz = 0;
            if (!front(p)) {
                for (FieldBlock b : p) {
                    Point shift = b.file >= 0 ? fileShift.get(b.file) : null;
                    if (shift == null) {
                        shift = nearestShift(army, fileShift, b.line);
                    }
                    sx += shift.x() / p.size();
                    sz += shift.z() / p.size();
                }
            }
            double reach = front(p) || p.get(0).arm == Arm.ARCHERS ? REACH : REACH_BEHIND; // archers go further for high ground
            Point shift = best(army, p, new Point(sx, sz), reach, ref, taken, ground);
            for (FieldBlock b : p) {
                Point at = b.line.plus(shift.x(), shift.z());
                if (b.duty == Duty.FRONT) {
                    fileShift.put(b.file, shift);
                }
                taken.add(at);
                if (Math.hypot(shift.x(), shift.z()) > 0.5) {
                    field.place(b, at);
                }
            }
        }
    }

    private static boolean front(List<FieldBlock> platoon) {
        return platoon.stream().anyMatch(b -> b.duty == Duty.FRONT);
    }

    /** The shift of the front block nearest a point, for a block with no file of its own. */
    private static Point nearestShift(FieldArmy army, Map<Integer, Point> fileShift, Point at) {
        Point best = new Point(0, 0);
        double bestDistance = Double.MAX_VALUE;
        for (FieldBlock f : army.front) {
            Point shift = fileShift.get(f.file);
            if (shift != null && f.deployed.distanceTo(at) < bestDistance) {
                bestDistance = f.deployed.distanceTo(at);
                best = shift;
            }
        }
        return best;
    }

    /**
     * How far a platoon moves, every block of it alike: to the best ground within {@code reach} of {@code start}; if
     * every move there leaves one of its blocks in water (a stretched world's lakes and rivers are wide), within twice
     * as far, and so on up to {@link #MAX_REACH}; {@code start} if even that is all water.
     */
    private static Point best(FieldArmy army, List<FieldBlock> platoon, Point start, double reach, double ref, List<Point> taken, Ground ground) {
        for (double r = reach; r <= MAX_REACH; r *= 2) {
            Point best = bestWithin(army, platoon, start, r, r > reach ? Math.max(STEP, r / 16) : STEP, ref, taken, ground);
            if (best != null) {
                return best;
            }
        }
        return start;
    }

    /**
     * The best move for a platoon within {@code reach} of {@code start}, looked at every {@code step}, leaving all its
     * blocks dry; its blocks' scores added up. Null if none.
     */
    private static Point bestWithin(FieldArmy army, List<FieldBlock> platoon, Point start, double reach, double step, double ref,
                                    List<Point> taken, Ground ground) {
        Point best = null;
        double bestScore = -Double.MAX_VALUE;
        for (double dx = -reach; dx <= reach; dx += step) {
            for (double dz = -reach; dz <= reach; dz += step) {
                double d = Math.hypot(dx, dz);
                if (d > reach) {
                    continue;
                }
                double forward = dx * army.facingX + dz * army.facingZ;
                if (forward > MAX_FORWARD) {
                    continue;
                }
                Point shift = start.plus(dx, dz);
                double score = 0;
                boolean dry = true;
                for (FieldBlock b : platoon) {
                    Point p = b.line.plus(shift.x(), shift.z());
                    if (wet(ground, army, b, p)) {
                        dry = false;
                        break;
                    }
                    double weight = b.arm == Arm.ARCHERS ? ARCHER_HEIGHT : b.arm == Arm.CAVALRY ? CAVALRY_HEIGHT : INFANTRY_HEIGHT;
                    score += weight * (ground.height(p.x(), p.z()) - ref) - COHESION * p.distanceTo(b.line);
                    for (Point t : taken) {
                        if (t.distanceTo(p) < CLEARANCE) {
                            score -= CROWDING;
                        }
                    }
                }
                if (dry && score > bestScore) {
                    bestScore = score;
                    best = shift;
                }
            }
        }
        return best;
    }

    /** How many blocks stand wet where they were deployed, by the ground's own reading: none, if deploying went well. */
    public static int wetBlocks(FieldBattle field, Ground ground) {
        int wet = 0;
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            for (FieldBlock b : army.blocks) {
                if (wet(ground, army, b, b.deployed)) {
                    wet++;
                }
            }
        }
        return wet;
    }

    /** Whether any of a block's front, from end to end, or its middle, stands in water. */
    private static boolean wet(Ground ground, FieldArmy army, FieldBlock b, Point p) {
        double hw = b.halfWidth();
        double rx = -army.facingZ;
        double rz = army.facingX;
        double depth = Math.max(2, (double) b.started() / Math.max(1, b.files) * BattlePlan.SPACING);
        return ground.water(p.x(), p.z()) || ground.water(p.x() + rx * hw, p.z() + rz * hw)
                || ground.water(p.x() - rx * hw, p.z() - rz * hw)
                || ground.water(p.x() - army.facingX * depth, p.z() - army.facingZ * depth);
    }

    /**
     * Each wing with a wood near its outer end (the end of its front away from the centre) sends a few of its reserve
     * infantry to hide there, to strike once the fighting comes near. Returns how many went.
     */
    private static int ambush(FieldBattle field, FieldArmy army, Ground ground) {
        int sent = 0;
        int[] wings = army.winged ? new int[]{FieldArmy.LEFT, FieldArmy.RIGHT} : new int[]{FieldArmy.CENTRE};
        double rx = -army.facingZ;
        double rz = army.facingX;
        for (int wing : wings) {
            // The wing's outer end.
            FieldBlock end = null;
            double outer = 0;
            for (FieldBlock f : army.front) {
                if (f.wing != wing && army.winged) {
                    continue;
                }
                double right = BattlePlan.offset(army.origin, army.facingX, army.facingZ, f.line).x();
                double out = wing == FieldArmy.LEFT ? -right : wing == FieldArmy.RIGHT ? right : Math.abs(right);
                if (end == null || out > outer) {
                    end = f;
                    outer = out;
                }
            }
            if (end == null) {
                continue;
            }
            Point wood = null;
            double bestDistance = Double.MAX_VALUE;
            for (double dx = -AMBUSH_REACH; dx <= AMBUSH_REACH; dx += STEP) {
                for (double dz = -AMBUSH_REACH; dz <= AMBUSH_REACH; dz += STEP) {
                    Point p = end.line.plus(dx, dz);
                    double forward = dx * army.facingX + dz * army.facingZ;
                    double d = Math.hypot(dx, dz);
                    if (d > AMBUSH_REACH || forward > AMBUSH_REACH / 2 || !ground.forest(p.x(), p.z()) || ground.water(p.x(), p.z())) {
                        continue;
                    }
                    if (d < bestDistance) {
                        bestDistance = d;
                        wood = p;
                    }
                }
            }
            if (wood == null) {
                continue;
            }
            List<FieldBlock> reserves = new ArrayList<>();
            for (FieldBlock b : army.blocks) {
                if (b.duty == Duty.LINE && b.arm.infantry() && b.task == FieldBlock.Task.NONE && (b.wing == wing || !army.winged)) {
                    reserves.add(b);
                }
            }
            Point at = wood;
            reserves.sort(Comparator.comparingDouble(b -> b.line.distanceTo(at)));
            for (int i = 0; i < Math.min(AMBUSH_BLOCKS, reserves.size()); i++) {
                double offset = (i - (Math.min(AMBUSH_BLOCKS, reserves.size()) - 1) / 2.0) * AMBUSH_SPACING;
                FieldBlock b = reserves.get(i);
                field.place(b, wood.plus(rx * offset, rz * offset));
                field.assign(b, FieldBlock.Task.AMBUSH);
                sent++;
            }
        }
        return sent;
    }
}
