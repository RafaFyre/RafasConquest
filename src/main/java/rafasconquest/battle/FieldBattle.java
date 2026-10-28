package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A battle fought as numbers, block by block, one second at a time: no entities, so it costs next to nothing however
 * big the armies are. The generals command exactly as in a live battle (lines, relief, archers, cavalry;
 * {@link BattlePlan}); blocks walk to where they are ordered and fight whoever is within reach
 * ({@link FieldRules}). Each soldier's health is kept, so a block can later become real soldiers and back.
 * <p>
 * Deterministic for a given seed. Not thread safe.
 */
public final class FieldBattle {
    /** How a battle ended: the winning side (0 or 1), or -1 for a draw. */
    public record Outcome(int winner, String result, int seconds) {
    }

    /** A big decision a general makes, which the live battle sounds on the general's horn. */
    public enum Call {
        ADVANCE, CAVALRY_CHARGE, CAVALRY_RECALL, WALL, GENERAL_CHARGES, FINAL_CHARGE
    }

    /** A general's call: which side made it, and what. */
    public record Order(int side, Call call) {
    }

    private final FieldArmy[] armies;
    private final Random random;
    private final FieldRules.Rates rates;
    private int seconds;
    /** When a soldier last fell (0 before anyone has). */
    private int lastFall;
    private boolean finalMelee;
    /** The calls made this second, for the live battle to sound. */
    private final List<Order> calls = new ArrayList<>();
    private final boolean[] advanced = new boolean[2];
    @Nullable
    private Outcome outcome;

    /**
     * Deploys two armies around {@code centre}, facing each other along ({@code axisX}, {@code axisZ}) (a unit vector
     * from the first army's side toward the second's), {@code blocks} blocks of 20 each.
     */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed) {
        this(firstBlocks, secondBlocks, centre, axisX, axisZ, seed, FieldRules.RATES);
    }

    /** The same, fought with other rates (for calibration). */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed,
                       FieldRules.Rates rates) {
        this.rates = rates;
        this.random = new Random(seed);
        Point[] fronts = BattlePlan.fronts(centre, axisX, axisZ);
        armies = new FieldArmy[]{
                deploy(0, firstBlocks, fronts[0], axisX, axisZ),
                deploy(1, secondBlocks, fronts[1], -axisX, -axisZ)};
    }

    private static FieldArmy deploy(int side, int blocks, Point front, double facingX, double facingZ) {
        FieldArmy army = new FieldArmy(side, facingX, facingZ);
        List<BattlePlan.Slot> slots = BattlePlan.deployment(blocks);
        int files = (int) slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        army.front = new FieldBlock[files];
        int id = side * 10_000;
        for (BattlePlan.Slot slot : slots) {
            Point line = BattlePlan.place(front, facingX, facingZ, slot.right(), slot.back());
            Duty duty = !slot.arm().infantry() ? Duty.SUPPORT : slot.line() == 0 ? Duty.FRONT : Duty.LINE;
            FieldBlock block = new FieldBlock(id++, side, slot.arm(), BattlePlan.SOLDIERS_PER_FORMATION, FieldRules.SOLDIER_HEALTH,
                    BattlePlan.LEADER_INDEX, false, line, facingX, facingZ, duty);
            block.file = slot.file();
            army.blocks.add(block);
            if (duty == Duty.FRONT) {
                army.front[slot.file()] = block;
            } else if (duty == Duty.LINE) {
                army.join(block, slot.file());
            }
        }
        Point generalSpot = BattlePlan.place(front, facingX, facingZ, 0, BattlePlan.GENERAL_BACK);
        army.commander = new FieldBlock(id++, side, Arm.SWORDSMEN, 1, FieldRules.WARRIOR_HEALTH,
                -1, true, generalSpot, facingX, facingZ, Duty.GENERAL);
        army.bearer = new FieldBlock(id, side, Arm.SWORDSMEN, 1, FieldRules.SOLDIER_HEALTH, -1, false,
                BattlePlan.place(front, facingX, facingZ, BattlePlan.BEARER_RIGHT, BattlePlan.GENERAL_BACK), facingX, facingZ, Duty.BEARER);
        army.blocks.add(army.commander);
        army.blocks.add(army.bearer);
        army.started = army.blocks.stream().mapToInt(FieldBlock::started).sum();
        return army;
    }

    public FieldArmy army(int side) {
        return armies[side];
    }

    public int seconds() {
        return seconds;
    }

    @Nullable
    public Outcome outcome() {
        return outcome;
    }

    /** Whether the lines stalled and everyone left was sent in to a final melee. */
    public boolean finalMelee() {
        return finalMelee;
    }

    public boolean over() {
        return outcome != null;
    }

    /**
     * A live block's soldiers report in: where its front stands and each soldier's health (0 for the fallen), read
     * from the real soldiers. Falls count toward whether the battle has stalled.
     */
    public void sync(FieldBlock block, Point pos, float[] health) {
        int before = block.alive;
        block.pos = pos;
        int alive = 0;
        for (int i = 0; i < block.health.length; i++) {
            block.health[i] = Math.max(0, health[i]);
            if (block.health[i] > 0) {
                alive++;
            }
        }
        block.alive = alive;
        if (alive < before && seconds > 0) {
            lastFall = seconds;
        }
    }

    /** The generals' calls made in the last second, oldest first; taking them clears them. */
    public List<Order> takeCalls() {
        List<Order> taken = List.copyOf(calls);
        calls.clear();
        return taken;
    }

    /** Runs whole seconds until the battle ends or {@code maxSeconds} have passed; returns the outcome, if any. */
    @Nullable
    public Outcome run(int maxSeconds) {
        for (int i = 0; i < maxSeconds && !over(); i++) {
            second();
        }
        return outcome;
    }

    /** One second: each general gives orders, then blocks move, then everyone within reach fights. */
    public void second() {
        if (over()) {
            for (FieldArmy army : armies) {
                move(army);
            }
            return;
        }
        seconds++;
        for (int side = 0; side < 2; side++) {
            command(armies[side], armies[1 - side]);
        }
        for (FieldArmy army : armies) {
            move(army);
        }
        fight();
        momentum();
        if (!finalMelee && BattlePlan.stalled(seconds, lastFall)) {
            finalMelee = true;
            calls.add(new Order(0, Call.FINAL_CHARGE));
            calls.add(new Order(1, Call.FINAL_CHARGE));
            for (FieldArmy army : armies) {
                for (FieldBlock block : army.blocks) {
                    if (block.alive > 0 && block.duty != Duty.BEARER) {
                        block.charged = true;
                    }
                }
            }
        }
        checkEnd();
    }

    // ---- orders: the same rules as a live battle (RecruitsBattle) ----

    private void command(FieldArmy army, FieldArmy enemy) {
        boolean engaged = army.infantryEngaged();
        if (!advanced[army.side] && seconds >= BattlePlan.FORMING_SECONDS) {
            advanced[army.side] = true;
            calls.add(new Order(army.side, Call.ADVANCE));
        }
        relieve(army);
        brace(army, enemy);
        for (FieldBlock block : army.blocks) {
            if (block.alive == 0) {
                continue;
            }
            if (block.charged) {
                if (block.arm == Arm.CAVALRY && block.momentum < FieldRules.SPENT_MOMENTUM
                        && block.contactSeconds >= FieldRules.SPENT_SECONDS && block.duty == Duty.SUPPORT) {
                    recall(army, block);
                }
                continue;
            }
            if (block.braced) {
                if (seconds - block.lastThreat > FieldRules.WALL_HOLD_SECONDS) {
                    block.braced = false;
                    block.facingX = army.facingX;
                    block.facingZ = army.facingZ;
                } else {
                    block.line = block.pos;
                    continue;
                }
            }
            switch (block.duty) {
                case FRONT -> holdFront(army, block, enemy, engaged);
                case LINE -> {
                    int place = army.queue(block.file).indexOf(block) + 1;
                    block.line = BattlePlan.place(army.front[block.file].line, army.facingX, army.facingZ, 0, place * BattlePlan.LINE_GAP);
                }
                case RELIEVING -> {
                    if (block.pos.distanceTo(block.line) <= BattlePlan.RELIEF_ARRIVED || seconds - block.dutySince >= BattlePlan.RELIEF_SECONDS) {
                        takeOver(army, block);
                    }
                }
                case WITHDRAWING -> {
                    if (block.rear == null || block.pos.distanceTo(block.rear) <= BattlePlan.RELIEF_ARRIVED
                            || seconds - block.dutySince >= BattlePlan.RELIEF_SECONDS) {
                        block.duty = BattlePlan.fitToRelieve(block.alive, block.started()) ? Duty.LINE : Duty.RESTING;
                        if (block.duty == Duty.LINE) {
                            army.join(block, block.file);
                        }
                    }
                }
                case RALLYING -> {
                    if (block.pos.distanceTo(block.line) <= BattlePlan.RELIEF_ARRIVED || seconds - block.dutySince >= FieldRules.RALLY_SECONDS) {
                        block.duty = Duty.SUPPORT;
                        block.reformUntil = seconds + FieldRules.REFORM_SECONDS;
                    }
                }
                case SUPPORT -> support(army, enemy, block, engaged);
                case GENERAL -> general(army, enemy, block, engaged);
                case BEARER -> {
                    if (army.commander != null && army.commander.alive > 0) {
                        block.line = BattlePlan.place(army.commander.pos, army.facingX, army.facingZ, BattlePlan.BEARER_RIGHT, 0);
                    }
                }
                default -> {
                }
            }
        }
    }

    private void holdFront(FieldArmy army, FieldBlock block, FieldArmy enemy, boolean engaged) {
        double nearest = nearest(block, block.line, enemy);
        switch (BattlePlan.order(block.arm, seconds, nearest, engaged)) {
            case MARCH -> {
                // Straight ahead, keeping the line, while the nearest enemy is ahead; once the enemy is beside or
                // behind it (its own foe gone, the lines passed through each other), toward that enemy instead.
                block.engaged = false;
                double stepX = army.facingX;
                double stepZ = army.facingZ;
                FieldBlock foe = nearestBlock(block, enemy, null);
                if (foe != null) {
                    double dx = foe.pos.x() - block.line.x();
                    double dz = foe.pos.z() - block.line.z();
                    double ahead = dx * army.facingX + dz * army.facingZ;
                    double aside = Math.abs(dx * army.facingZ - dz * army.facingX);
                    double length = Math.hypot(dx, dz);
                    if (ahead < aside && length > 1e-6) {
                        stepX = dx / length;
                        stepZ = dz / length;
                    }
                }
                block.line = block.line.plus(stepX * BattlePlan.MARCH_STEP, stepZ * BattlePlan.MARCH_STEP);
            }
            case ENGAGE -> {
                // Presses on, in its ranks, until the lines meet.
                block.engaged = true;
                double step = Math.min(BattlePlan.MARCH_STEP, nearest - BattlePlan.FRONT_CONTACT);
                if (step > 0) {
                    block.line = block.line.plus(army.facingX * step, army.facingZ * step);
                }
            }
            default -> {
            }
        }
    }

    private void relieve(FieldArmy army) {
        for (int file = 0; file < army.front.length; file++) {
            FieldBlock held = army.front[file];
            if (held.relief != null && held.relief.alive > 0 && held.relief.duty == Duty.RELIEVING) {
                continue;
            }
            held.relief = null;
            if (!BattlePlan.needsRelief(held.alive, held.started())) {
                continue;
            }
            FieldBlock relief = pickRelief(army, file, held.line);
            if (relief == null && held.alive == 0) {
                relief = lastStand(army, held.line);
            }
            if (relief == null) {
                continue;
            }
            relief.duty = Duty.RELIEVING;
            relief.file = file;
            relief.dutySince = seconds;
            relief.line = held.line;
            held.relief = relief;
        }
    }

    @Nullable
    private static FieldBlock pickRelief(FieldArmy army, int file, Point spot) {
        for (FieldBlock f : army.queue(file)) {
            if (BattlePlan.fitToRelieve(f.alive, f.started())) {
                return f;
            }
        }
        return army.blocks.stream()
                .filter(f -> f.duty == Duty.LINE && f.alive > 0 && BattlePlan.fitToRelieve(f.alive, f.started()))
                .min(Comparator.comparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    /**
     * A front file whose block has fallen, with no fit block left to send: any block still in the lines goes, worn or
     * not, else the nearest resting block, so the line doesn't stay open.
     */
    @Nullable
    private static FieldBlock lastStand(FieldArmy army, Point spot) {
        return army.blocks.stream()
                .filter(f -> f.alive > 0 && (f.duty == Duty.LINE || f.duty == Duty.RESTING))
                .min(Comparator.comparingInt((FieldBlock f) -> f.duty == Duty.LINE ? 0 : 1)
                        .thenComparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    private void takeOver(FieldArmy army, FieldBlock relief) {
        FieldBlock relieved = army.front[relief.file];
        army.front[relief.file] = relief;
        army.reliefs++;
        relief.duty = Duty.FRONT;
        relief.engaged = false;
        relief.dutySince = seconds;
        relieved.relief = null;
        relieved.engaged = false;
        if (relieved.alive == 0) {
            relieved.duty = Duty.RESTING;
            return;
        }
        relieved.duty = Duty.WITHDRAWING;
        relieved.dutySince = seconds;
        int behind = army.queue(relieved.file).size() + 1;
        relieved.rear = BattlePlan.place(relief.line, army.facingX, army.facingZ, 0, behind * BattlePlan.LINE_GAP + BattlePlan.LINE_GAP);
        relieved.line = relieved.rear;
    }

    private void support(FieldArmy army, FieldArmy enemy, FieldBlock block, boolean engaged) {
        if (seconds < block.reformUntil) {
            return; // reforming after a charge
        }
        switch (BattlePlan.order(block.arm, seconds, nearest(block, block.line, enemy), engaged)) {
            case MARCH -> {
                if (block.arm != Arm.ARCHERS || BattlePlan.archersMayAdvance(behindFront(army, block.line))) {
                    block.line = block.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                }
            }
            case CHARGE -> {
                block.charged = true;
                calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
            }
            default -> {
            }
        }
    }

    /**
     * Cavalry that has spent its momentum in melee rides back toward its own lines to reform, not fighting on the way,
     * then charges again ({@link FieldRules#CHARGE_BONUS}).
     */
    private void recall(FieldArmy army, FieldBlock cavalry) {
        cavalry.charged = false;
        cavalry.duty = Duty.RALLYING;
        cavalry.dutySince = seconds;
        cavalry.contactSeconds = 0;
        cavalry.facingX = army.facingX;
        cavalry.facingZ = army.facingZ;
        cavalry.line = cavalry.pos.plus(-army.facingX * FieldRules.RALLY_DISTANCE, -army.facingZ * FieldRules.RALLY_DISTANCE);
        calls.add(new Order(army.side, Call.CAVALRY_RECALL));
    }

    /**
     * Infantry in the path of charging enemy cavalry braces: stops, turns to face it and stands as a wall
     * ({@link FieldRules#WALL_ALERT}). Blocks already fighting infantry can't turn, and stay as they are.
     */
    private void brace(FieldArmy army, FieldArmy enemy) {
        boolean called = false;
        for (FieldBlock cavalry : enemy.blocks) {
            if (cavalry.arm != Arm.CAVALRY || !cavalry.charged || cavalry.alive == 0) {
                continue;
            }
            for (FieldBlock block : army.blocks) {
                boolean free = block.duty == Duty.LINE || block.duty == Duty.RESTING || block.duty == Duty.FRONT && !block.engaged;
                if (block.alive == 0 || !block.arm.infantry() || block.charged || !(free || block.braced)) {
                    continue;
                }
                double dx = block.pos.x() - cavalry.pos.x();
                double dz = block.pos.z() - cavalry.pos.z();
                double distance = Math.hypot(dx, dz);
                if (distance > FieldRules.WALL_ALERT || distance < 1e-6) {
                    continue;
                }
                // Heading at it, or so close it is coming whichever way it faces.
                boolean headingAt = (dx * cavalry.facingX + dz * cavalry.facingZ) / distance >= FieldRules.WALL_HEADING;
                if (!headingAt && distance > FieldRules.WALL_ALERT / 3) {
                    continue;
                }
                if (!block.braced) {
                    block.braced = true;
                    block.facingX = -dx / distance;
                    block.facingZ = -dz / distance;
                    block.line = block.pos;
                    if (!called) {
                        called = true;
                        calls.add(new Order(army.side, Call.WALL));
                    }
                }
                block.lastThreat = seconds;
            }
        }
    }

    private void general(FieldArmy army, FieldArmy enemy, FieldBlock block, boolean engaged) {
        if (engaged) {
            block.charged = true;
            calls.add(new Order(army.side, Call.GENERAL_CHARGES));
            return;
        }
        block.line = BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, 0, BattlePlan.GENERAL_BACK);
    }

    /** How far behind its army's front line a point is (negative: ahead of it). */
    private static double behindFront(FieldArmy army, Point at) {
        Point front = army.frontCentre();
        return (front.x() - at.x()) * army.facingX + (front.z() - at.z()) * army.facingZ;
    }

    /**
     * Whether {@code other} could be nearer than {@code best} to a block standing at {@code at}: their centres'
     * distance, less both half-widths, is as near as their fronts can be. A cheap check before the exact one, which
     * keeps a big battle's searches for the nearest enemy quick.
     */
    private static boolean closeEnough(FieldBlock block, Point at, FieldBlock other, double best) {
        if (best == Double.MAX_VALUE) {
            return true;
        }
        double reach = best + block.halfWidth() + other.halfWidth();
        double dx = other.pos.x() - at.x();
        double dz = other.pos.z() - at.z();
        return dx * dx + dz * dz < reach * reach;
    }

    /** The distance from a block standing at {@code at} to the nearest living enemy block. */
    private static double nearest(FieldBlock block, Point at, FieldArmy enemy) {
        double best = Double.MAX_VALUE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && closeEnough(block, at, e, best)) {
                best = Math.min(best, block.distanceFrom(at, e));
            }
        }
        return best;
    }

    @Nullable
    private static FieldBlock nearestBlock(FieldBlock block, FieldArmy enemy, @Nullable Arm prefer) {
        FieldBlock best = null;
        double bestDistance = Double.MAX_VALUE;
        if (prefer != null) {
            for (FieldBlock e : enemy.blocks) {
                if (e.alive > 0 && e.arm == prefer && e.duty == Duty.SUPPORT && closeEnough(block, block.pos, e, bestDistance)) {
                    double d = block.distanceTo(e);
                    if (d < bestDistance) {
                        bestDistance = d;
                        best = e;
                    }
                }
            }
            if (best != null) {
                return best;
            }
        }
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && closeEnough(block, block.pos, e, bestDistance)) {
                double d = block.distanceTo(e);
                if (d < bestDistance) {
                    bestDistance = d;
                    best = e;
                }
            }
        }
        return best;
    }

    // ---- movement ----

    /** Every block walks toward its line at its pace; one that has broken formation goes for its prey. */
    private void move(FieldArmy army) {
        FieldArmy enemy = armies[1 - army.side];
        for (FieldBlock block : army.blocks) {
            if (block.alive == 0 || block.live) {
                continue; // real soldiers walk themselves
            }
            Point target = block.line;
            if (block.charged && block.duty != Duty.FLEEING) {
                FieldBlock prey = nearestBlock(block, enemy, block.arm == Arm.CAVALRY ? Arm.ARCHERS : null);
                if (prey == null) {
                    continue;
                }
                double dx = prey.pos.x() - block.pos.x();
                double dz = prey.pos.z() - block.pos.z();
                double length = Math.hypot(dx, dz);
                if (length > 1e-6) {
                    block.facingX = dx / length;
                    block.facingZ = dz / length;
                }
                if (block.distanceTo(prey) <= FieldRules.CLOSE_REACH) {
                    continue;
                }
                target = prey.pos;
            }
            double dx = target.x() - block.pos.x();
            double dz = target.z() - block.pos.z();
            double distance = Math.hypot(dx, dz);
            double step = rates.speed(block.arm);
            block.pos = distance <= step ? target : block.pos.plus(dx / distance * step, dz / distance * step);
        }
    }

    // ---- fighting ----

    /**
     * Everyone within reach fights, all at once: each block strikes the nearest enemy block within its reach (an
     * archer block with arrows shoots the nearest within range instead when nobody is at hand). Damage is dealt only
     * once every block has struck, so the order of blocks doesn't matter.
     */
    private void fight() {
        List<FieldBlock> targets = new ArrayList<>();
        List<Double> damages = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            FieldArmy enemy = armies[1 - side];
            for (FieldBlock block : armies[side].blocks) {
                if (block.alive == 0 || block.duty == Duty.WITHDRAWING || block.duty == Duty.BEARER || block.duty == Duty.FLEEING
                        || block.duty == Duty.RALLYING) {
                    block.contactFoe = null;
                    continue;
                }
                double reach = block.charged ? FieldRules.CLOSE_REACH : FieldRules.HOLDING_REACH;
                FieldBlock foe = nearestBlock(block, enemy, null);
                if (foe == null) {
                    continue;
                }
                double distance = block.distanceTo(foe);
                if (distance <= reach) {
                    block.contactSeconds++;
                    block.contactFoe = foe;
                    if (block.live && foe.live) {
                        continue; // real soldiers on both sides: Recruits fights it out
                    }
                    double fighters = block.champion ? rates.warriorFightsAs() : block.alive;
                    double damage = fighters * rates.meleeDps(block.arm)
                            * rates.meleeFactor(sideOf(foe, block), foe.arm);
                    if (block.arm == Arm.CAVALRY && block.charged) {
                        damage *= 1 + rates.chargeBonus() * block.momentum;
                    }
                    if (foe.braced && block.arm == Arm.CAVALRY && sideOf(foe, block) == FieldRules.Side.FRONT) {
                        damage *= FieldRules.BRACED_TAKES;
                    }
                    if (block.braced && foe.arm == Arm.CAVALRY) {
                        damage *= FieldRules.BRACED_DEALS;
                    }
                    targets.add(foe);
                    damages.add(damage);
                    continue;
                }
                block.contactSeconds = 0;
                block.contactFoe = null;
                if (block.arm == Arm.ARCHERS && block.arrows > 0) {
                    shoot(block, enemy, targets, damages);
                }
            }
        }
        for (int i = 0; i < targets.size(); i++) {
            FieldBlock target = targets.get(i);
            if (target.live) {
                target.liveDamage += damages.get(i); // dealt to the real soldiers by the live battle
            } else if (target.takeDamage(damages.get(i), random) > 0) {
                lastFall = seconds;
            }
        }
    }

    /**
     * Cavalry momentum, after the fighting: gathered by riding (not while rallying), spent by each second in melee,
     * more against cavalry, and all of it against a braced wall struck from the front. Real cavalry's melee is as its
     * riders report it ({@link FieldBlock#setLiveContact}); a real block also turns the way it is riding.
     */
    private void momentum() {
        for (FieldArmy army : armies) {
            for (FieldBlock block : army.blocks) {
                if (block.arm != Arm.CAVALRY || block.alive == 0) {
                    continue;
                }
                double moved = block.lastPos == null ? 0 : block.pos.distanceTo(block.lastPos);
                if (block.live && moved > 0.5) {
                    block.facingX = (block.pos.x() - block.lastPos.x()) / moved;
                    block.facingZ = (block.pos.z() - block.lastPos.z()) / moved;
                }
                FieldBlock foe = block.live ? block.liveContactFoe : block.contactFoe;
                if (block.live) {
                    block.contactSeconds = foe == null ? 0 : block.contactSeconds + 1;
                }
                if (foe != null) {
                    if (foe.braced && sideOf(foe, block) == FieldRules.Side.FRONT) {
                        block.momentum = 0;
                    } else {
                        double loss = foe.arm == Arm.CAVALRY ? FieldRules.MOMENTUM_LOSS_CAVALRY : FieldRules.MOMENTUM_LOSS_INFANTRY;
                        block.momentum = Math.max(0, block.momentum - loss);
                    }
                } else if (block.duty != Duty.RALLYING) {
                    block.momentum = Math.min(1, block.momentum + moved / FieldRules.RUN_UP);
                }
                block.lastPos = block.pos;
            }
        }
    }

    private void shoot(FieldBlock archers, FieldArmy enemy, List<FieldBlock> targets, List<Double> damages) {
        FieldBlock foe = null;
        double best = FieldRules.ARCHER_RANGE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0) {
                double d = Math.hypot(e.pos.x() - archers.pos.x(), e.pos.z() - archers.pos.z());
                if (d <= best) {
                    best = d;
                    foe = e;
                }
            }
        }
        if (foe == null) {
            return;
        }
        int shots = (int) Math.min(archers.arrows, Math.ceil(archers.alive * FieldRules.ARROWS_PER_SECOND));
        archers.arrows -= shots;
        targets.add(foe);
        damages.add(shots * rates.arrowDamage() * FieldRules.arrowFactor(sideOf(foe, archers), foe.arm));
    }

    /** Where an attack from {@code attacker} falls on {@code defender}. */
    private static FieldRules.Side sideOf(FieldBlock defender, FieldBlock attacker) {
        return FieldRules.side(defender.facingX, defender.facingZ,
                attacker.pos.x() - defender.pos.x(), attacker.pos.z() - defender.pos.z());
    }

    // ---- the end ----

    private void checkEnd() {
        boolean routA = BattlePlan.routs(armies[0].alive(), armies[0].started);
        boolean routB = BattlePlan.routs(armies[1].alive(), armies[1].started);
        if (routA && routB) {
            outcome = new Outcome(-1, "both armies broke", seconds);
        } else if (routA || routB) {
            int winner = routA ? 1 : 0;
            outcome = new Outcome(winner, "side " + winner + " wins; side " + (1 - winner) + " routs", seconds);
            flee(armies[1 - winner]);
        } else if (seconds >= BattlePlan.TIME_LIMIT_SECONDS) {
            outcome = new Outcome(-1, "draw (time limit)", seconds);
        }
    }

    /** The routed army stops fighting and runs back the way it came. */
    private static void flee(FieldArmy loser) {
        for (FieldBlock block : loser.blocks) {
            block.duty = Duty.FLEEING;
            block.charged = false;
            block.braced = false;
            block.engaged = false;
            block.line = block.pos.plus(-loser.facingX * 60, -loser.facingZ * 60);
        }
    }
}
