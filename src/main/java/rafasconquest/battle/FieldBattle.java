package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.Doctrine.Formation;
import rafasconquest.battle.Doctrine.Strategy;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A battle fought as numbers, block by block, one second at a time: no entities, so it costs next to nothing however
 * big the armies are. The generals command exactly as in a live battle (lines, relief, archers, cavalry;
 * {@link BattlePlan}), each by their own {@link WarPlan}; blocks walk to where they are ordered and fight whoever is
 * within reach ({@link FieldRules}). Each soldier's health is kept, so a block can later become real soldiers and back.
 * <p>
 * Each general rides with a guard of 20 iron-clad riders. When a general falls, their army retreats.
 * <p>
 * Deterministic for a given seed. Not thread safe.
 */
public final class FieldBattle {
    /** How a battle ended: the winning side (0 or 1), or -1 for a draw. */
    public record Outcome(int winner, String result, int seconds) {
    }

    /** A big decision a general makes, which the live battle sounds on the general's horn. */
    public enum Call {
        ADVANCE, CAVALRY_CHARGE, CAVALRY_RECALL, WALL, GENERAL_CHARGES, FINAL_CHARGE, BAIT, PINCER
    }

    /** A general's call: which side made it, and what. */
    public record Order(int side, Call call) {
    }

    /** Something a general's strategy did, worth telling: which side, and what (to follow "the general"). */
    public record Event(int side, String text) {
    }

    /** Arrows loosed in the last second by an archer block, at a block: drawn by clients as arrows in flight. */
    public record Volley(FieldBlock archers, FieldBlock target, int shots) {
    }

    private final FieldArmy[] armies;
    private final Random random;
    private final FieldRules.Rates rates;
    private int seconds;
    /** When a soldier last fell (0 before anyone has). */
    private int lastFall;
    private boolean finalMelee;
    /** The calls made this second, for the live battle to sound, and what the strategies did. */
    private final List<Order> calls = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();
    private final List<Volley> volleys = new ArrayList<>();
    /** The armies are marching out of camp to their places for the day (see {@link #formUp}). */
    private boolean forming;
    /** Pulled back to camp for the night: blocks only march. */
    private boolean pulledBack;
    /** The winner pursues the beaten army. */
    private boolean pursuing;
    /** When each side last took harm, in battle seconds (-1: never). */
    private final int[] lastHurt = {-1, -1};
    private final boolean[] advanced = new boolean[2];
    @Nullable
    private Outcome outcome;

    /**
     * Deploys two armies around {@code centre}, facing each other along ({@code axisX}, {@code axisZ}) (a unit vector
     * from the first army's side toward the second's), {@code blocks} blocks of 20 each, with no doctrine.
     */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed) {
        this(firstBlocks, secondBlocks, centre, axisX, axisZ, seed, FieldRules.RATES, WarPlan.NONE, WarPlan.NONE);
    }

    /** The same, fought with other rates (for calibration). */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed,
                       FieldRules.Rates rates) {
        this(firstBlocks, secondBlocks, centre, axisX, axisZ, seed, rates, WarPlan.NONE, WarPlan.NONE);
    }

    /** The same, each general fighting by their plan. */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed,
                       WarPlan first, WarPlan second) {
        this(firstBlocks, secondBlocks, centre, axisX, axisZ, seed, FieldRules.RATES, first, second);
    }

    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed,
                       FieldRules.Rates rates, WarPlan first, WarPlan second) {
        this.rates = rates;
        this.random = new Random(seed);
        Point[] fronts = BattlePlan.fronts(centre, axisX, axisZ);
        armies = new FieldArmy[]{
                deploy(0, firstBlocks, fronts[0], axisX, axisZ, first),
                deploy(1, secondBlocks, fronts[1], -axisX, -axisZ, second)};
    }

    private static FieldArmy deploy(int side, int blocks, Point front, double facingX, double facingZ, WarPlan plan) {
        FieldArmy army = new FieldArmy(side, facingX, facingZ, plan);
        army.origin = front;
        army.stackFiles = BattlePlan.stackFiles(blocks, plan.has(Formation.GREAT_SHIELD));
        army.stackDepth = BattlePlan.stackDepth(blocks, plan.has(Formation.GREAT_SHIELD));
        boolean shield = plan.has(Formation.GREAT_SHIELD);
        boolean spear = plan.has(Formation.GREAT_SPEAR);
        boolean unrelenting = plan.has(Strategy.UNRELENTING_CHARGE);
        List<BattlePlan.Slot> slots = BattlePlan.deployment(blocks, shield, plan.wingShares());
        int files = (int) slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        army.front = new FieldBlock[files];
        army.winged = slots.stream().anyMatch(s -> s.wing() != FieldArmy.CENTRE);
        double reserves = Double.MAX_VALUE;
        int id = side * 10_000;
        double lastLine = 0;
        double frontHalf = 0;
        double[] wingSum = new double[3];
        int[] wingFiles = new int[3];
        List<FieldBlock> cavalry = new ArrayList<>();
        for (BattlePlan.Slot slot : slots) {
            Point line = BattlePlan.place(front, facingX, facingZ, slot.right(), slot.back());
            Duty duty = !slot.arm().infantry() ? Duty.SUPPORT : slot.line() == 0 ? Duty.FRONT : Duty.LINE;
            FieldBlock block = new FieldBlock(id++, side, slot.arm(), BattlePlan.SOLDIERS_PER_FORMATION, FieldRules.SOLDIER_HEALTH,
                    BattlePlan.LEADER_INDEX, false, line, facingX, facingZ, duty);
            block.file = slot.file();
            block.files = slot.files();
            block.homeRight = slot.right();
            block.wing = slot.wing();
            army.blocks.add(block);
            if (duty == Duty.FRONT) {
                army.front[slot.file()] = block;
            } else if (duty == Duty.LINE) {
                army.join(block, slot.file());
                FieldBlock fileFront = army.front[slot.file()];
                Point frontAt = BattlePlan.offset(front, facingX, facingZ, fileFront.line);
                block.reserve = new Point(slot.right() - frontAt.x(), slot.back() - frontAt.z());
            }
            if (slot.arm().infantry()) {
                lastLine = Math.max(lastLine, slot.back());
                if (duty == Duty.FRONT) {
                    frontHalf = Math.max(frontHalf, Math.abs(slot.right()) + BattlePlan.pitch(slot.files()) / 2);
                    wingSum[block.wing] += slot.right();
                    wingFiles[block.wing]++;
                }
            } else if (slot.arm() == Arm.CAVALRY) {
                cavalry.add(block);
                reserves = Math.min(reserves, slot.back());
            }
        }
        // The rear reserves, where the cavalry waits and later rides its backline.
        army.rearBack = reserves == Double.MAX_VALUE ? lastLine + BattlePlan.LINE_GAP : reserves;
        for (int wing = 0; wing < 3; wing++) {
            army.wingRight[wing] = wingFiles[wing] > 0 ? wingSum[wing] / wingFiles[wing]
                    : wing == FieldArmy.CENTRE ? 0 : (wing == FieldArmy.RIGHT ? 1 : -1) * (frontHalf + BattlePlan.CAVALRY_GAP);
        }
        Point generalSpot = BattlePlan.place(front, facingX, facingZ, 0, BattlePlan.GENERAL_BACK);
        army.commander = new FieldBlock(id++, side, Arm.CAVALRY, 1, FieldRules.WARRIOR_HEALTH,
                -1, true, generalSpot, facingX, facingZ, Duty.GENERAL);
        army.guard = new FieldBlock(id++, side, Arm.CAVALRY, BattlePlan.SOLDIERS_PER_FORMATION, FieldRules.SOLDIER_HEALTH,
                BattlePlan.LEADER_INDEX, false, BattlePlan.place(generalSpot, facingX, facingZ, 0, BattlePlan.GUARD_BACK),
                facingX, facingZ, Duty.GUARD);
        army.bearer = new FieldBlock(id, side, Arm.SWORDSMEN, 1, FieldRules.SOLDIER_HEALTH, -1, false,
                BattlePlan.place(front, facingX, facingZ, BattlePlan.BEARER_RIGHT, BattlePlan.GENERAL_BACK), facingX, facingZ, Duty.BEARER);
        army.blocks.add(army.commander);
        army.blocks.add(army.guard);
        army.blocks.add(army.bearer);

        // The wedge, which forms only once the front line is established (formWedge): the general leads it
        // (Unrelenting Charge) from right behind their guard, which rides at its tip, with half the cavalry, those
        // waiting nearest the middle; the rest stays in its wings' reserves. A Great Spear alone takes all the cavalry.
        // Until it forms, each of its blocks waits where it deployed.
        if (spear || unrelenting) {
            if (unrelenting) {
                army.wedge.add(army.guard);
                army.wedge.add(army.commander);
                army.wedge.addAll(halfTheCavalry(cavalry));
            } else {
                army.wedge.addAll(cavalry);
            }
            army.wedgeHomeBack = Math.max(BattlePlan.SPEAR_BACK, army.rearBack);
            int firstHalf = (army.wedge.size() + 1) / 2;
            for (int i = 0; i < army.wedge.size(); i++) {
                FieldBlock member = army.wedge.get(i);
                double[] at = BattlePlan.wedgeSlot(i);
                member.inWedge = true;
                member.wedgeRight = at[0];
                member.wedgeBack = at[1];
                member.half = unrelenting && i >= firstHalf ? 1 : 0;
                if (member == army.commander || member == army.guard) {
                    member.half = 0; // the general and their guard always ride together, in the first half
                }
                if (member == army.commander) {
                    // Straight behind the guard, between it and the next row.
                    member.wedgeRight = 0;
                    member.wedgeBack = BattlePlan.WEDGE_ROW / 2;
                }
                if (spear) {
                    member.maxMomentum = FieldRules.GREAT_SPEAR_MOMENTUM;
                }
            }
        }
        // Bait: a single block in the middle of each wing's front that has three files or more.
        if (plan.has(Strategy.BAIT)) {
            for (int wing = 0; wing < 3; wing++) {
                List<Integer> inWing = new ArrayList<>();
                for (FieldBlock f : army.front) {
                    if (f.wing == wing) {
                        inWing.add(f.file);
                    }
                }
                if (inWing.size() >= 3) {
                    army.baitFile[wing] = inWing.get(inWing.size() / 2);
                }
            }
        }
        for (FieldBlock b : army.blocks) {
            b.deployed = b.line;
        }
        army.started = army.blocks.stream().mapToInt(FieldBlock::started).sum();
        return army;
    }

    /**
     * The half of the cavalry an Unrelenting Charge takes into its wedge: the centre's first, then the left's and
     * right's in turn, those nearest the middle first, so the two wings keep alike reserves.
     */
    private static List<FieldBlock> halfTheCavalry(List<FieldBlock> cavalry) {
        List<List<FieldBlock>> byWing = List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        for (FieldBlock c : cavalry) {
            byWing.get(c.wing).add(c);
        }
        byWing.forEach(w -> w.sort(Comparator.comparingDouble(c -> Math.abs(c.homeRight))));
        List<FieldBlock> half = new ArrayList<>();
        int want = (cavalry.size() + 1) / 2;
        List<FieldBlock> centre = byWing.get(FieldArmy.CENTRE);
        while (half.size() < want && !centre.isEmpty()) {
            half.add(centre.remove(0));
        }
        boolean left = true;
        while (half.size() < want) {
            List<FieldBlock> from = byWing.get(left ? FieldArmy.LEFT : FieldArmy.RIGHT);
            List<FieldBlock> other = byWing.get(left ? FieldArmy.RIGHT : FieldArmy.LEFT);
            half.add(!from.isEmpty() ? from.remove(0) : other.remove(0));
            left = !left;
        }
        return half;
    }

    /** Whether a front file is one of its army's baits. */
    private static boolean isBait(FieldArmy army, int file) {
        for (int bait : army.baitFile) {
            if (bait == file && bait >= 0) {
                return true;
            }
        }
        return false;
    }

    /** Whether the battle has begun: its first second has run. Before then blocks may be placed by hand. */
    public boolean begun() {
        return seconds > 0;
    }

    /**
     * Before the battle begins, places a block by hand: its front rank's centre at {@code at}. It keeps that place in
     * the army once the battle begins, relative to what it stands by: a front block marches from there; a block
     * waiting in a file keeps its place relative to the file's front block; the wedge, the general, their guard and
     * the standard keep theirs relative to the front, the general and the general again; other cavalry waits there
     * and patrols the backline behind it. Throws once the battle has begun.
     */
    public void place(FieldBlock block, Point at) {
        if (begun()) {
            throw new IllegalStateException("the battle has begun");
        }
        FieldArmy army = armies[block.side];
        block.line = at;
        block.deployed = at;
        if (!block.live) {
            block.pos = at;
        }
        switch (block.duty) {
            case LINE -> block.placed = BattlePlan.offset(army.front[block.file].line, army.facingX, army.facingZ, at);
            case GENERAL -> block.placed = BattlePlan.offset(holdingCentre(army), army.facingX, army.facingZ, at);
            case GUARD -> block.placed = army.commander == null ? null
                    : BattlePlan.offset(army.commander.line, army.facingX, army.facingZ, at);
            case BEARER -> block.placed = army.commander == null ? null
                    : BattlePlan.offset(army.commander.line, army.facingX, army.facingZ, at);
            default -> {
            }
        }
        if (block.arm == Arm.CAVALRY && block.duty == Duty.SUPPORT) {
            block.homeRight = BattlePlan.offset(army.frontCentre(), army.facingX, army.facingZ, at).x();
        }
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

    /** The volleys of the last second, from archers shooting as numbers. */
    public List<Volley> volleys() {
        return java.util.Collections.unmodifiableList(volleys);
    }

    /** What the generals' strategies did since last asked, oldest first; taking them clears them. */
    public List<Event> takeEvents() {
        List<Event> taken = List.copyOf(events);
        events.clear();
        return taken;
    }

    /**
     * Whether a block takes less harm for its general's presence: an Unrelenting Charge led by its general, who rides
     * within {@link FieldRules#RESOLVE_REACH} of it.
     */
    public boolean resolute(FieldBlock block) {
        FieldArmy army = armies[block.side];
        FieldBlock general = army.commander;
        return army.plan.has(Strategy.UNRELENTING_CHARGE) && general != null && general.alive > 0
                && (general.charged || general.approaching) && general.pos.distanceTo(block.pos) <= FieldRules.RESOLVE_REACH;
    }

    /**
     * Which way a block's enemy lies, as a unit vector: the block it is fighting, else whom it is going for, else the
     * nearest enemy block; its army's facing if there is none.
     */
    public Point enemyDirection(FieldBlock block) {
        FieldArmy army = armies[block.side];
        FieldBlock foe = hunted(block.contactFoe) ? block.contactFoe : hunted(block.prey) ? block.prey : nearestBlock(block, armies[1 - block.side], null);
        if (foe == null) {
            return new Point(army.facingX, army.facingZ);
        }
        double dx = foe.pos.x() - block.pos.x();
        double dz = foe.pos.z() - block.pos.z();
        double length = Math.hypot(dx, dz);
        return length < 1e-6 ? new Point(army.facingX, army.facingZ) : new Point(dx / length, dz / length);
    }

    /** Whether a block is a general who leads the charge (Unrelenting Charge), or that general's guard. */
    public static boolean leadsTheCharge(FieldBlock block) {
        return block.inWedge && (block.homeDuty == Duty.GENERAL || block.homeDuty == Duty.GUARD);
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
        volleys.clear();
        if (forming) {
            // The morning's deployment: blocks march out of camp to their places, nothing else. The battle hasn't begun.
            for (FieldArmy army : armies) {
                move(army);
            }
            return;
        }
        if (pulledBack) {
            seconds++;
            for (FieldArmy army : armies) {
                move(army);
            }
            return;
        }
        if (over() && pursuing) {
            pursueSecond();
            return;
        }
        if (over()) {
            seconds++; // time goes on after the battle: the aftermath is timed in it
            for (FieldArmy army : armies) {
                move(army);
            }
            return;
        }
        seconds++;
        index();
        for (int side = 0; side < 2; side++) {
            command(armies[side], armies[1 - side]);
        }
        for (FieldArmy army : armies) {
            move(army);
        }
        index();
        fight();
        momentum();
        for (FieldArmy army : armies) {
            morale(army, armies[1 - army.side]);
        }
        if (!finalMelee && BattlePlan.stalled(seconds, lastFall)) {
            finalMelee = true;
            calls.add(new Order(0, Call.FINAL_CHARGE));
            calls.add(new Order(1, Call.FINAL_CHARGE));
            for (FieldArmy army : armies) {
                for (FieldBlock block : army.blocks) {
                    // The general, their guard and the standard stay back: if the general falls, the army is lost.
                    if (block.alive > 0 && block.duty != Duty.BEARER && block.duty != Duty.GENERAL && block.duty != Duty.GUARD) {
                        block.charged = true;
                        block.approaching = false;
                        block.strikeAt = null;
                    }
                }
            }
        }
        checkEnd();
    }

    /** Files each army's blocks by where they now stand, for quick searches ({@link BlockGrid}). */
    private void index() {
        for (FieldArmy army : armies) {
            army.grid = new BlockGrid(army.blocks);
        }
    }

    // ---- orders: the same rules as a live battle (RecruitsBattle) ----

    private void command(FieldArmy army, FieldArmy enemy) {
        boolean engaged = army.infantryEngaged();
        if (!advanced[army.side] && seconds >= BattlePlan.FORMING_SECONDS) {
            advanced[army.side] = true;
            calls.add(new Order(army.side, Call.ADVANCE));
        }
        relieve(army);
        // Each file's queue, worked out once for this second's places (a block's place in it).
        java.util.Map<Integer, List<FieldBlock>> queues = new java.util.HashMap<>();
        brace(army, enemy);
        pincer(army, enemy);
        huntStoppedCavalry(army, enemy);
        tasks(army, enemy);
        shieldGeneral(army, enemy);
        envelop(army, enemy);
        formWedge(army, enemy);
        wedge(army, enemy, engaged);
        for (FieldBlock block : army.blocks) {
            if (block.alive == 0) {
                continue;
            }
            if (block.strikeAt != null) {
                strike(army, block);
                continue;
            }
            if (block.task == FieldBlock.Task.AMBUSH && !block.sprung) {
                continue; // lying hidden where it was put, whatever its file does
            }
            if (block.approaching) {
                if (!block.inWedge) {
                    ride(block);
                }
                continue;
            }
            if (block.charged && block == army.commander) {
                // In the fight the general stays right behind their guard, and falls back to reform when it does.
                FieldBlock guard = army.guard;
                if (guard == null || guard.alive == 0 || !guard.charged) {
                    recall(army, block);
                } else {
                    block.line = BattlePlan.place(guard.pos, guard.facingX, guard.facingZ, 0, BattlePlan.GUARD_BACK);
                }
                continue;
            }
            if (block.charged) {
                if (block.arm == Arm.CAVALRY && block.momentum < FieldRules.SPENT_MOMENTUM
                        && block.contactSeconds >= FieldRules.SPENT_SECONDS
                        && (block.duty == Duty.SUPPORT || block.duty == Duty.GUARD || block.duty == Duty.GENERAL)) {
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
                    int place = queues.computeIfAbsent(block.file, army::queue).indexOf(block) + 1;
                    // Placed by hand; else in the reserves where it deployed, or closed up in a Great Shield's stack.
                    Point offset = block.placed != null ? block.placed
                            : block.reserve != null && (army.stackFiles() == 0 || place >= army.stackDepth) ? block.reserve
                            : new Point(0, BattlePlan.fileBack(place, army.stackFiles()));
                    block.line = BattlePlan.place(army.front[block.file].line, army.facingX, army.facingZ, offset.x(), offset.z());
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
                        block.duty = block.homeDuty;
                        block.reformUntil = seconds + FieldRules.REFORM_SECONDS;
                        if (block.duty == Duty.SUPPORT && !block.inWedge) {
                            patrol(army, block); // rides straight on along the backline
                        }
                    }
                }
                case SUPPORT -> support(army, enemy, block, engaged);
                case GENERAL -> general(army, enemy, block);
                case GUARD -> guard(army, enemy, block);
                case ROUTED -> {
                    if (seconds - block.dutySince >= FieldRules.REGROUP_SECONDS) {
                        // Regrouped: back to the back of its file, its morale whole again.
                        java.util.Arrays.fill(block.history, block.alive);
                        army.join(block, block.file);
                    }
                }
                case BEARER -> {
                    if (army.commander != null && army.commander.alive > 0) {
                        Point at = block.placed != null ? block.placed : new Point(BattlePlan.BEARER_RIGHT, 0);
                        block.line = BattlePlan.place(army.commander.pos, army.facingX, army.facingZ, at.x(), at.z());
                    }
                }
                default -> {
                }
            }
        }
    }

    private void holdFront(FieldArmy army, FieldBlock block, FieldArmy enemy, boolean engaged) {
        double nearest = nearest(block, block.line, enemy);
        BattlePlan.Order order = BattlePlan.order(block.arm, seconds, nearest, engaged);
        if (isBait(army, block.file)) {
            bait(army, block, order);
            return;
        }
        if (army.baitSprung[block.wing] && order == BattlePlan.Order.MARCH) {
            order = BattlePlan.Order.HOLD; // the wing waits for its bait to draw the enemy in
        }
        switch (order) {
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
                // Presses on, in its ranks, until the lines meet; one clearly outnumbered by the block it fights gives
                // ground, a little at a time, so the stronger side pushes in and the line bends.
                block.engaged = true;
                FieldBlock foe = block.contactFoe;
                if (foe != null && foe.arm.infantry() && block.alive < foe.alive * FieldRules.GIVE_GROUND_SHARE) {
                    block.line = block.line.plus(-army.facingX * FieldRules.GIVE_GROUND, -army.facingZ * FieldRules.GIVE_GROUND);
                    return;
                }
                double step = Math.min(BattlePlan.MARCH_STEP, nearest - BattlePlan.FRONT_CONTACT);
                if (step > 0) {
                    block.line = block.line.plus(army.facingX * step, army.facingZ * step);
                }
            }
            default -> {
            }
        }
    }

    /**
     * Bait: a single block runs ahead of its wing's line, up to {@link FieldRules#BAIT_LEAD} in front of it; once
     * fighting it falls back a step a second, still fighting, to {@link FieldRules#BAIT_POCKET} behind the line,
     * drawing its enemy on.
     */
    private void bait(FieldArmy army, FieldBlock block, BattlePlan.Order order) {
        double behind = behindOthers(army, block.wing, block.line);
        if (order == BattlePlan.Order.ENGAGE || block.contactFoe != null) {
            block.engaged = true;
            if (!army.baitSprung[block.wing]) {
                army.baitSprung[block.wing] = true;
                calls.add(new Order(army.side, Call.BAIT));
                events.add(new Event(army.side, "has a bait fall back" + army.where(block.wing) + ", drawing the enemy in"));
            }
            if (behind < FieldRules.BAIT_POCKET) {
                block.line = block.line.plus(-army.facingX, -army.facingZ);
            }
        } else if (order == BattlePlan.Order.MARCH) {
            block.engaged = false;
            double step = behind > -FieldRules.BAIT_LEAD ? rates.footSpeed() : BattlePlan.MARCH_STEP;
            block.line = block.line.plus(army.facingX * step, army.facingZ * step);
        }
    }

    /** How far behind the lines of a wing's front blocks, baits aside, a point is (negative: ahead of them). */
    private static double behindOthers(FieldArmy army, int wing, Point at) {
        double x = 0;
        double z = 0;
        int n = 0;
        for (FieldBlock f : army.front) {
            if (f.wing == wing && !isBait(army, f.file)) {
                x += f.line.x();
                z += f.line.z();
                n++;
            }
        }
        if (n == 0) {
            return 0;
        }
        return (x / n - at.x()) * army.facingX + (z / n - at.z()) * army.facingZ;
    }

    private void relieve(FieldArmy army) {
        for (int file = 0; file < army.front.length; file++) {
            FieldBlock held = army.front[file];
            if (held.relief != null && held.relief.alive > 0 && held.relief.duty == Duty.RELIEVING) {
                continue;
            }
            held.relief = null;
            if (held.strikeAt != null) {
                continue;
            }
            boolean worn = BattlePlan.needsRelief(held.alive, held.started());
            if (!worn) {
                // The conveyor: after ROTATE_SECONDS of fighting, the block behind takes its place, and it goes to the
                // back of its file's stack to rest.
                List<FieldBlock> stack = army.queue(file);
                if (army.stackDepth > 1 && held.fightSeconds >= FieldRules.ROTATE_SECONDS && !stack.isEmpty() && held.alive > 0) {
                    FieldBlock next = stack.get(0);
                    next.duty = Duty.RELIEVING;
                    next.file = file;
                    next.dutySince = seconds;
                    next.line = held.line;
                    held.relief = next;
                    held.rotating = true;
                }
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
                .filter(f -> f.duty == Duty.LINE && f.alive > 0 && f.strikeAt == null && f.task == FieldBlock.Task.NONE && BattlePlan.fitToRelieve(f.alive, f.started()))
                .min(Comparator.comparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    /**
     * A front file whose block has fallen, with no fit block left to send: any block still in the lines goes, worn or
     * not, else the nearest resting block, so the line doesn't stay open.
     */
    @Nullable
    private static FieldBlock lastStand(FieldArmy army, Point spot) {
        return army.blocks.stream()
                .filter(f -> f.alive > 0 && f.strikeAt == null && (f.duty == Duty.LINE || f.duty == Duty.RESTING))
                .min(Comparator.comparingInt((FieldBlock f) -> f.duty == Duty.LINE ? 0 : 1)
                        .thenComparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    private void takeOver(FieldArmy army, FieldBlock relief) {
        FieldBlock relieved = army.front[relief.file];
        army.front[relief.file] = relief;
        army.reliefs++;
        relief.duty = Duty.FRONT;
        relief.wing = relieved.wing;
        relief.placed = null;
        relief.engaged = false;
        relief.dutySince = seconds;
        relieved.relief = null;
        relieved.engaged = false;
        if (relieved.rotating && relieved.alive > 0) {
            // Rotated out: to the back of its file's stack, where it rests until its turn comes round again.
            relieved.rotating = false;
            relieved.fightSeconds = 0;
            relieved.duty = Duty.LINE;
            relieved.file = relief.file;
            relieved.placed = null;
            army.insertInQueue(relieved, relief.file, army.stackDepth - 2);
            relief.fightSeconds = 0;
            return;
        }
        relieved.rotating = false;
        if (relieved.alive == 0) {
            relieved.duty = Duty.RESTING;
            return;
        }
        relieved.duty = Duty.WITHDRAWING;
        relieved.dutySince = seconds;
        int behind = army.queue(relieved.file).size() + 1;
        relieved.rear = BattlePlan.place(relief.line, army.facingX, army.facingZ, 0,
                BattlePlan.fileBack(behind, army.stackFiles()) + BattlePlan.LINE_GAP);
        relieved.line = relieved.rear;
    }

    private void support(FieldArmy army, FieldArmy enemy, FieldBlock block, boolean engaged) {
        if (block.inWedge) {
            if (seconds < block.reformUntil) {
                return; // reforming after a charge
            }
            // Waits in reserve until the wedge forms, then in the wedge for its general's word; rides out alone only
            // at an enemy upon it.
            if (army.wedgeFormed) {
                block.line = wedgeHome(army, block);
            }
            if (seconds >= BattlePlan.FORMING_SECONDS && nearest(block, block.pos, enemy) <= BattlePlan.CAVALRY_THREAT) {
                charge(army, block, null);
            }
            return;
        }
        boolean cavalry = block.arm == Arm.CAVALRY;
        if (cavalry && block.rode) {
            patrol(army, block);
        }
        if (seconds < block.reformUntil) {
            return; // reforming after a charge, still riding
        }
        // Cavalry acts on its own wing's fighting, not the whole army's.
        boolean fighting = cavalry ? army.wingEngaged(block.wing) : engaged;
        BattlePlan.Order order = BattlePlan.order(block.arm, seconds, nearest(block, cavalry ? block.pos : block.line, enemy), fighting);
        if (cavalry && order != BattlePlan.Order.CHARGE && seconds >= BattlePlan.FORMING_SECONDS
                && army.plan.has(Strategy.PRECISION_STRIKES)) {
            FieldBlock exposed = mostExposed(enemy, block.pos, FieldRules.WING_REACH);
            if (exposed != null) {
                events.add(new Event(army.side, "strikes an exposed enemy block with cavalry" + army.where(block.wing)));
                charge(army, block, exposed);
                return;
            }
        }
        if (cavalry && block.rode && order != BattlePlan.Order.CHARGE) {
            return; // keeps riding its backline
        }
        switch (order) {
            case MARCH -> {
                if (block.arm != Arm.ARCHERS || behindFront(army, block.line) - BattlePlan.MARCH_STEP >= BattlePlan.archerBack(army.stackFiles(), army.stackDepth)) {
                    block.line = block.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                }
            }
            case CHARGE -> charge(army, block, null);
            default -> {
            }
        }
    }

    /**
     * Sends a block in at {@code prey} (or whom its general's strategies choose): cavalry following Cavalry Flank at
     * infantry first rides out to the side of its prey's line and turns in there; anyone else breaks formation now.
     */
    private void charge(FieldArmy army, FieldBlock block, @Nullable FieldBlock prey) {
        FieldBlock enemyPick = prey != null ? prey : choosePrey(army, armies[1 - army.side], block);
        block.prey = enemyPick;
        block.rode = block.arm == Arm.CAVALRY;
        if (enemyPick != null && block.arm == Arm.CAVALRY && enemyPick.arm.infantry() && army.plan.has(Strategy.CAVALRY_FLANK)) {
            block.approaching = true;
            block.waypoint = flankPoint(enemyPick, block.pos);
            block.dutySince = seconds;
        } else {
            block.charged = true;
        }
        calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
    }

    /**
     * Cavalry that has charged doesn't stand to wait: it rides back and forth along its wing's backline,
     * {@link FieldRules#PATROL_REACH} each way of its post, keeping its momentum up until it goes in again.
     */
    private void patrol(FieldArmy army, FieldBlock block) {
        Point post = BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, block.homeRight, army.rearBack);
        Point end = BattlePlan.place(post, army.facingX, army.facingZ, block.patrolLeg * FieldRules.PATROL_REACH, 0);
        if (block.pos.distanceTo(end) <= rates.speed(block.arm)) {
            // Turns before reaching the end, so it never stands there for a second.
            block.patrolLeg = -block.patrolLeg;
            end = BattlePlan.place(post, army.facingX, army.facingZ, block.patrolLeg * FieldRules.PATROL_REACH, 0);
        }
        block.line = end;
        block.facingX = army.facingX;
        block.facingZ = army.facingZ;
    }

    /** A block riding at its prey in formation: to its flank first if it has a waypoint, then in. */
    private void ride(FieldBlock block) {
        FieldBlock prey = block.prey;
        if (prey == null || prey.alive == 0 || prey.duty == Duty.FLEEING) {
            block.approaching = false;
            block.waypoint = null;
            block.prey = null;
            return;
        }
        if (block.waypoint != null && (block.pos.distanceTo(block.waypoint) <= 6 || seconds - block.dutySince > 25)) {
            block.waypoint = null;
        }
        if (block.waypoint == null && block.distanceTo(prey) <= FieldRules.CLOSE_REACH + 1.5
                || nearest(block, block.pos, armies[1 - block.side]) <= FieldRules.CLOSE_REACH) {
            block.approaching = false;
            block.waypoint = null;
            block.charged = true;
            return;
        }
        Point aim = block.waypoint != null ? block.waypoint : prey.pos;
        face(block, aim);
        block.line = aim;
    }

    /** The point out to the side of {@code prey}'s line nearer {@code from}, where a flanking ride turns in. */
    private static Point flankPoint(FieldBlock prey, Point from) {
        double out = prey.halfWidth() + FieldRules.FLANK_RIDE_OUT;
        Point right = prey.pos.plus(-prey.facingZ * out, prey.facingX * out);
        Point left = prey.pos.plus(prey.facingZ * out, -prey.facingX * out);
        return right.distanceTo(from) <= left.distanceTo(from) ? right : left;
    }

    private static void face(FieldBlock block, Point at) {
        double dx = at.x() - block.pos.x();
        double dz = at.z() - block.pos.z();
        double length = Math.hypot(dx, dz);
        if (length > 1e-6) {
            block.facingX = dx / length;
            block.facingZ = dz / length;
        }
    }

    /**
     * Cavalry that has spent its momentum in melee rides back to reform, not fighting on the way, then charges again
     * ({@link FieldRules#CHARGE_BONUS}): to its wing's backline (where it keeps riding, {@link #patrol}), the wedge
     * {@link FieldRules#RALLY_DISTANCE} straight back, the guard to the general's back.
     */
    private void recall(FieldArmy army, FieldBlock cavalry) {
        cavalry.charged = false;
        cavalry.duty = Duty.RALLYING;
        cavalry.dutySince = seconds;
        cavalry.contactSeconds = 0;
        cavalry.facingX = army.facingX;
        cavalry.facingZ = army.facingZ;
        if (cavalry.homeDuty == Duty.GUARD && !cavalry.inWedge && army.commander != null) {
            // The guard reforms at the general's back, not off behind the lines where the general would be alone.
            cavalry.line = BattlePlan.place(army.commander.pos, army.facingX, army.facingZ, 0, BattlePlan.GUARD_BACK);
        } else if (cavalry.homeDuty == Duty.SUPPORT && !cavalry.inWedge) {
            cavalry.line = BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, cavalry.homeRight, army.rearBack);
        } else {
            cavalry.line = cavalry.pos.plus(-army.facingX * FieldRules.RALLY_DISTANCE, -army.facingZ * FieldRules.RALLY_DISTANCE);
        }
        calls.add(new Order(army.side, Call.CAVALRY_RECALL));
    }

    /**
     * Infantry in the path of charging enemy cavalry braces: stops, turns to face it and stands as a wall
     * ({@link FieldRules#WALL_ALERT}). Blocks already fighting infantry can't turn, and stay as they are.
     */
    private void brace(FieldArmy army, FieldArmy enemy) {
        boolean called = false;
        for (FieldBlock cavalry : enemy.blocks) {
            if (cavalry.arm != Arm.CAVALRY || !(cavalry.charged || cavalry.approaching) || cavalry.alive == 0) {
                continue;
            }
            List<FieldBlock> near = new ArrayList<>();
            if (army.grid != null) {
                army.grid.within(cavalry.pos, FieldRules.WALL_ALERT, near::add);
            } else {
                near = army.blocks;
            }
            for (FieldBlock block : near) {
                boolean free = block.duty == Duty.LINE || block.duty == Duty.RESTING || block.duty == Duty.FRONT && !block.engaged;
                if (block.alive == 0 || !block.arm.infantry() || block.charged || block.strikeAt != null || !(free || block.braced)) {
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

    /**
     * The general: in the wedge's tip if they lead it, else mounted behind the middle of the front. Cavalry charging
     * at them sends them to shelter behind the nearest of their own infantry blocks, which it then has to cut through.
     */
    private void general(FieldArmy army, FieldArmy enemy, FieldBlock general) {
        FieldBlock hunter = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.prey == general && (e.charged || e.approaching) && e.pos.distanceTo(general.pos) <= FieldRules.GUARD_INTERCEPT
                    && (hunter == null || e.pos.distanceTo(general.pos) < hunter.pos.distanceTo(general.pos))) {
                hunter = e;
            }
        }
        if (hunter != null) {
            FieldBlock shelter = null;
            for (FieldBlock b : army.blocks) {
                if (b.alive > 0 && b.arm.infantry() && b.duty != Duty.FLEEING && !b.charged
                        && (shelter == null || b.pos.distanceTo(general.pos) < shelter.pos.distanceTo(general.pos))) {
                    shelter = b;
                }
            }
            if (shelter != null) {
                double dx = shelter.pos.x() - hunter.pos.x();
                double dz = shelter.pos.z() - hunter.pos.z();
                double length = Math.max(1e-6, Math.hypot(dx, dz));
                double behind = (double) shelter.started() / shelter.files * BattlePlan.SPACING + 4;
                general.line = shelter.pos.plus(dx / length * behind, dz / length * behind);
                return;
            }
        }
        if (general.inWedge && army.wedgeFormed) {
            general.line = wedgeHome(army, general);
            return;
        }
        // Behind the middle of the line, but never where the enemy has pushed in: further back until clear of them.
        Point at = general.placed != null ? general.placed : new Point(0, BattlePlan.GENERAL_BACK);
        Point spot = BattlePlan.place(holdingCentre(army), army.facingX, army.facingZ, at.x(), at.z());
        for (int i = 0; i < 8 && nearestEnemy(enemy, spot) < FieldRules.GENERAL_SAFE; i++) {
            spot = spot.plus(-army.facingX * 10, -army.facingZ * 10);
        }
        general.line = spot;
    }

    /** The middle of the front blocks holding the line (those off striking a flank aside). */
    private static Point holdingCentre(FieldArmy army) {
        double x = 0;
        double z = 0;
        int n = 0;
        for (FieldBlock f : army.front) {
            if (f.strikeAt == null) {
                x += f.line.x();
                z += f.line.z();
                n++;
            }
        }
        return n == 0 ? army.frontCentre() : new Point(x / n, z / n);
    }

    /** How far the nearest living enemy block stands from a point. */
    private static double nearestEnemy(FieldArmy enemy, Point at) {
        double best = Double.MAX_VALUE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.duty != Duty.FLEEING) {
                best = Math.min(best, e.pos.distanceTo(at));
            }
        }
        return best;
    }

    /** The general's guard: behind the general (or in the wedge), riding out at any enemy that comes near them. */
    /**
     * The guard shields the general: whatever comes for the general (enemy charging or riding at them) within
     * {@link FieldRules#GUARD_INTERCEPT} is met by the guard first, wherever it is, even mid-charge in the wedge.
     */
    private void shieldGeneral(FieldArmy army, FieldArmy enemy) {
        FieldBlock general = army.commander;
        FieldBlock guard = army.guard;
        if (general == null || guard == null || general.alive == 0 || guard.alive == 0 || guard.duty == Duty.RALLYING
                || guard.duty == Duty.FLEEING) {
            return;
        }
        FieldBlock threat = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.prey == general && (e.charged || e.approaching) && e.pos.distanceTo(general.pos) <= FieldRules.GUARD_INTERCEPT
                    && (threat == null || e.pos.distanceTo(general.pos) < threat.pos.distanceTo(general.pos))) {
                threat = e;
            }
        }
        if (threat == null || guard.prey == threat && guard.charged) {
            return;
        }
        guard.approaching = false;
        guard.waypoint = null;
        guard.prey = threat;
        guard.charged = true;
        calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
        events.add(new Event(army.side, "has their guard ride out to meet the enemy coming for them"));
    }

    private void guard(FieldArmy army, FieldArmy enemy, FieldBlock guard) {
        if (seconds < guard.reformUntil) {
            return;
        }
        if (guard.inWedge && army.wedgeFormed) {
            guard.line = wedgeHome(army, guard);
            return;
        }
        FieldBlock general = army.commander;
        if (general == null || general.alive == 0) {
            return;
        }
        Point at = guard.placed != null ? guard.placed : new Point(0, BattlePlan.GUARD_BACK);
        guard.line = BattlePlan.place(general.line, army.facingX, army.facingZ, at.x(), at.z());
        // Whoever comes near the general; cavalry riding at the general is met further out.
        FieldBlock threat = null;
        double best = Double.MAX_VALUE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive == 0 || e.duty == Duty.FLEEING || e.duty == Duty.BEARER) {
                continue;
            }
            double d = general.distanceTo(e);
            double alert = e.prey == general && (e.charged || e.approaching) ? FieldRules.GUARD_INTERCEPT : FieldRules.GUARD_ALERT;
            if (d <= alert && d < best) {
                best = d;
                threat = e;
            }
        }
        if (threat != null) {
            guard.prey = threat;
            guard.charged = true;
            calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
        }
    }

    // ---- the wedge (Great Spear, Unrelenting Charge) ----

    /**
     * The wedge forms once the battle's front line is established: both armies' front infantry have met, and have
     * been fighting {@link FieldRules#FRONTLINE_SECONDS}. Its blocks then leave their reserves for their places in it.
     */
    private void formWedge(FieldArmy army, FieldArmy enemy) {
        if (army.wedge.isEmpty() || army.wedgeFormed) {
            return;
        }
        if (army.frontlineSince < 0) {
            if (army.infantryEngaged() && enemy.infantryEngaged()) {
                army.frontlineSince = seconds;
            }
            return;
        }
        if (seconds - army.frontlineSince >= FieldRules.FRONTLINE_SECONDS) {
            army.wedgeFormed = true;
            events.add(new Event(army.side, "forms the wedge behind the established front line"));
        }
    }

    /** Where a wedge member waits: its place behind the wedge's tip, far behind the front centre. */
    private Point wedgeHome(FieldArmy army, FieldBlock member) {
        // Behind the middle of the line, but never where the enemy has pushed in (drawn in by bait, say): the whole
        // wedge stands further back until its tip is well clear of them, so the general waiting in it is safe.
        FieldArmy enemy = armies[1 - army.side];
        Point tip = BattlePlan.place(holdingCentre(army), army.facingX, army.facingZ, 0, army.wedgeHomeBack);
        double back = army.wedgeHomeBack;
        for (int i = 0; i < 8 && nearestEnemy(enemy, tip) < 2 * FieldRules.GENERAL_SAFE; i++) {
            back += 10;
            tip = BattlePlan.place(holdingCentre(army), army.facingX, army.facingZ, 0, back);
        }
        return BattlePlan.place(holdingCentre(army), army.facingX, army.facingZ, member.wedgeRight, back + member.wedgeBack);
    }

    /** The wedge's members riding this charge: one half (Unrelenting Charge) or all, alive. */
    private static List<FieldBlock> wedgeRiders(FieldArmy army, int half) {
        List<FieldBlock> riders = new ArrayList<>();
        for (FieldBlock m : army.wedge) {
            if (m.alive > 0 && (half < 0 || m.half == half)) {
                riders.add(m);
            }
        }
        return riders;
    }

    /**
     * The wedge charges as one, in formation, its tip at its target, and breaks up to fight on striking it. A Great
     * Spear waits for its lines to be fighting; the general's strategies may send it sooner. In an Unrelenting Charge
     * the general leads the first half; once it is spent and riding back, the other half goes in, and so on.
     */
    private void wedge(FieldArmy army, FieldArmy enemy, boolean engaged) {
        if (army.wedge.isEmpty() || finalMelee) {
            return;
        }
        if (army.wedgeTarget != null) {
            steerWedge(army);
            return;
        }
        boolean unrelenting = army.plan.has(Strategy.UNRELENTING_CHARGE);
        int half = unrelenting ? army.nextHalf : -1;
        List<FieldBlock> riders = wedgeRiders(army, half);
        if (riders.isEmpty()) {
            if (unrelenting) {
                army.nextHalf = 1 - army.nextHalf;
            }
            return;
        }
        if (!wedgeReady(army)) {
            return;
        }
        FieldBlock tip = riders.get(0);
        FieldBlock target = wedgeTarget(army, enemy, engaged, tip);
        if (target == null) {
            return;
        }
        army.wedgeTarget = target;
        army.wedgeHalf = half;
        army.wedgeSince = seconds;
        Point waypoint = army.plan.has(Strategy.CAVALRY_FLANK) && target.arm.infantry() ? flankPoint(target, tip.pos) : null;
        for (FieldBlock r : riders) {
            r.approaching = true;
            r.prey = target;
            r.waypoint = waypoint;
            r.dutySince = seconds;
        }
        calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
        boolean led = riders.contains(army.commander);
        if (led) {
            calls.add(new Order(army.side, Call.GENERAL_CHARGES));
        }
        String who = target.duty == Duty.GENERAL ? "the enemy general" : target == army.pincerTarget[FieldArmy.CENTRE] ? "the lured enemy block"
                : "the enemy line";
        events.add(new Event(army.side, (led ? "leads " : "sends ") + (unrelenting ? (half == 0 ? "the first half of " : "the second half of ") : "")
                + "the wedge at " + who + (waypoint != null ? ", around its flank" : "")));
        steerWedge(army);
    }

    /**
     * Whether the wedge (its next half, in an Unrelenting Charge) stands ready to charge: formed, none of it in or
     * riding back from a charge, and the other half no longer in.
     */
    private boolean wedgeReady(FieldArmy army) {
        if (army.wedge.isEmpty() || !army.wedgeFormed || army.wedgeTarget != null || finalMelee || seconds < BattlePlan.FORMING_SECONDS) {
            return false;
        }
        boolean unrelenting = army.plan.has(Strategy.UNRELENTING_CHARGE);
        int half = unrelenting ? army.nextHalf : -1;
        List<FieldBlock> riders = wedgeRiders(army, half);
        if (riders.isEmpty()) {
            return false;
        }
        for (FieldBlock r : riders) {
            if (r.charged || r.approaching || r.duty == Duty.RALLYING || seconds < r.reformUntil) {
                return false;
            }
        }
        if (unrelenting) {
            for (FieldBlock other : wedgeRiders(army, 1 - half)) {
                if (other.charged || other.approaching) {
                    return false; // the other half is still in
                }
            }
        }
        return true;
    }

    /** Whom the wedge rides at: a lured block, an exposed general or block, else the enemy line once it is fighting. */
    @Nullable
    private FieldBlock wedgeTarget(FieldArmy army, FieldArmy enemy, boolean engaged, FieldBlock tip) {
        FieldBlock lured = army.pincerTarget[FieldArmy.CENTRE];
        if (lured != null && lured.alive > 0) {
            return lured;
        }
        FieldBlock general = enemy.commander != null && enemy.commander.alive > 0 ? enemy.commander : null;
        if (general != null && army.plan.has(Strategy.DECAPITATING_CHARGE) && exposed(enemy)) {
            return general;
        }
        if (army.plan.has(Strategy.PRECISION_STRIKES)) {
            FieldBlock e = mostExposed(enemy, tip.pos, Double.MAX_VALUE);
            if (e != null) {
                return e;
            }
        }
        if (!engaged) {
            return null;
        }
        if (general != null && army.plan.has(Strategy.DECAPITATE)) {
            return general;
        }
        if (army.plan.has(Strategy.MASS_CASUALTIES)) {
            FieldBlock e = mostSoldiers(enemy, tip.pos, Double.MAX_VALUE);
            if (e != null) {
                return e;
            }
        }
        FieldBlock best = null;
        for (FieldBlock f : enemy.front) {
            if (f.alive > 0 && f.duty != Duty.FLEEING && (best == null || f.pos.distanceTo(tip.pos) < best.pos.distanceTo(tip.pos))) {
                best = f;
            }
        }
        return best != null ? best : nearestBlock(tip, enemy, null);
    }

    /** Moves the charging wedge on: each rider's line is its place behind the tip, the tip at the target (or the flank). */
    private void steerWedge(FieldArmy army) {
        FieldBlock target = army.wedgeTarget;
        List<FieldBlock> riders = new ArrayList<>();
        for (FieldBlock r : wedgeRiders(army, army.wedgeHalf)) {
            if (r.approaching) {
                riders.add(r);
            }
        }
        if (target == null || target.alive == 0 || target.duty == Duty.FLEEING || riders.isEmpty()) {
            for (FieldBlock r : riders) {
                r.approaching = false;
                r.waypoint = null;
                r.prey = null;
            }
            army.wedgeTarget = null;
            return;
        }
        FieldBlock general = army.commander;
        if (general != null && riders.contains(general) && (army.guard == null || !riders.contains(army.guard))) {
            // The guard has ridden off to meet a threat, or fallen: the general doesn't ride on in front alone.
            general.approaching = false;
            general.waypoint = null;
            general.prey = null;
            riders.remove(general);
            if (riders.isEmpty()) {
                army.wedgeTarget = null;
                return;
            }
        }
        FieldBlock tip = riders.get(0);
        Point waypoint = tip.waypoint;
        if (waypoint != null && (tip.pos.distanceTo(waypoint) <= 6 || seconds - army.wedgeSince > 25)) {
            waypoint = null;
            for (FieldBlock r : riders) {
                r.waypoint = null;
            }
        }
        // It breaks up to fight on striking its target, or whoever stands in its way first.
        boolean struck = seconds - army.wedgeSince > 60;
        FieldArmy enemy = armies[1 - army.side];
        for (FieldBlock r : riders) {
            struck |= waypoint == null && r.distanceTo(target) <= FieldRules.CLOSE_REACH + 1.5
                    || nearest(r, r.pos, enemy) <= FieldRules.CLOSE_REACH;
        }
        if (struck) {
            for (FieldBlock r : riders) {
                r.approaching = false;
                r.charged = true;
            }
            army.wedgeTarget = null;
            if (army.plan.has(Strategy.UNRELENTING_CHARGE)) {
                army.nextHalf = 1 - army.wedgeHalf;
            }
            return;
        }
        Point aim = waypoint != null ? waypoint : target.pos;
        double dx = aim.x() - tip.pos.x();
        double dz = aim.z() - tip.pos.z();
        double length = Math.hypot(dx, dz);
        double fx = length > 1e-6 ? dx / length : army.facingX;
        double fz = length > 1e-6 ? dz / length : army.facingZ;
        for (FieldBlock r : riders) {
            r.line = BattlePlan.place(aim, fx, fz, r.wedgeRight - tip.wedgeRight, r.wedgeBack - tip.wedgeBack);
            r.facingX = fx;
            r.facingZ = fz;
        }
    }

    // ---- bait and pincer, and infantry against stopped cavalry ----

    /**
     * Pincer, in each wing with a bait: once an enemy block, drawn on by the bait, stands too far forward near it, the
     * general springs that wing's trap with what is to hand: a waiting cavalry charge (in the centre the wedge, on a
     * wing that wing's cavalry), else three of the wing's idle infantry blocks to surround it (its flanks and rear),
     * else one to hit its flank.
     */
    private void pincer(FieldArmy army, FieldArmy enemy) {
        if (!army.plan.has(Strategy.PINCER)) {
            return;
        }
        for (int wing = 0; wing < 3; wing++) {
            if (army.baitFile[wing] >= 0) {
                pincer(army, enemy, wing);
            }
        }
    }

    private void pincer(FieldArmy army, FieldArmy enemy, int wing) {
        FieldBlock target = army.pincerTarget[wing];
        if (target != null && (target.alive == 0 || target.duty == Duty.FLEEING || seconds - army.pincerSince[wing] > FieldRules.PINCER_SECONDS)) {
            army.pincerTarget[wing] = null;
            for (FieldBlock b : army.blocks) {
                if (b.strikeAt == target) {
                    release(army, b);
                }
            }
            target = null;
        }
        FieldBlock bait = army.front[army.baitFile[wing]];
        if (target != null || !army.baitSprung[wing] || bait.alive == 0) {
            return;
        }
        FieldBlock lured = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.arm.infantry() && e.duty != Duty.FLEEING && !e.charged && e.distanceTo(bait) <= FieldRules.LURE_REACH
                    && -behindFront(enemy, e.pos) >= FieldRules.TOO_FAR_FORWARD && !struck(army, e)
                    && (lured == null || e.distanceTo(bait) < lured.distanceTo(bait))) {
                lured = e;
            }
        }
        if (lured == null) {
            return;
        }
        army.pincerTarget[wing] = lured;
        army.pincerSince[wing] = seconds;
        calls.add(new Order(army.side, Call.PINCER));
        String where = army.where(wing);
        // A prepared cavalry charge: in the centre the wedge goes for the lured block (wedgeTarget), else the wing's
        // cavalry that is ready.
        if (wing == FieldArmy.CENTRE && wedgeReady(army)) {
            events.add(new Event(army.side, "springs the pincer" + where + ": the wedge charges the lured block"));
            return;
        }
        boolean cavalry = false;
        for (FieldBlock c : army.blocks) {
            if (c.alive > 0 && c.arm == Arm.CAVALRY && c.duty == Duty.SUPPORT && !c.inWedge && !c.charged && !c.approaching
                    && seconds >= c.reformUntil && (c.wing == wing || !army.winged) && c.pos.distanceTo(lured.pos) <= FieldRules.WING_REACH) {
                charge(army, c, lured);
                cavalry = true;
            }
        }
        if (cavalry) {
            events.add(new Event(army.side, "springs the pincer" + where + ": cavalry charges the lured block"));
            return;
        }
        // Else the wing's infantry not fighting, nearest first: three surround it, or one strikes its flank.
        List<FieldBlock> free = idleInfantry(army, wing, lured, Double.MAX_VALUE);
        if (free.size() >= 3) {
            send(free.get(0), lured, sideNearer(lured, free.get(0).pos));
            send(free.get(1), lured, -free.get(0).strikeSide);
            send(free.get(2), lured, 0);
            events.add(new Event(army.side, "springs the pincer" + where + ": infantry surrounds the lured block"));
        } else if (!free.isEmpty()) {
            send(free.get(0), lured, sideNearer(lured, free.get(0).pos));
            events.add(new Event(army.side, "springs the pincer" + where + ": infantry strikes the lured block's flank"));
        } else {
            army.pincerTarget[wing] = null;
        }
    }

    /**
     * Infantry not fighting, fit and free, nearest {@code target} first, within {@code reach} of it: front blocks of a
     * wing (any wing if {@code wing} is -1) standing idle, baits aside, and blocks waiting in the files.
     */
    private static List<FieldBlock> idleInfantry(FieldArmy army, int wing, @Nullable FieldBlock target, double reach) {
        List<FieldBlock> free = new ArrayList<>();
        for (FieldBlock b : army.blocks) {
            if (idle(army, b, wing) && (target == null || b.pos.distanceTo(target.pos) <= reach)) {
                free.add(b);
            }
        }
        if (target != null) {
            free.sort(Comparator.comparingDouble(b -> b.pos.distanceTo(target.pos)));
        }
        return free;
    }

    /** Whether a block is infantry free to be sent to strike: not fighting, fit, of {@code wing} (any if -1). */
    private static boolean idle(FieldArmy army, FieldBlock b, int wing) {
        boolean idleFront = b.duty == Duty.FRONT && !isBait(army, b.file) && !b.engaged && b.contactFoe == null;
        return b.alive > 0 && b.task == FieldBlock.Task.NONE && b.arm.infantry() && !b.charged && !b.braced && b.strikeAt == null && (wing < 0 || b.wing == wing)
                && (idleFront || b.duty == Duty.LINE) && BattlePlan.fitToRelieve(b.alive, b.started());
    }

    /** Whether any of the army's infantry has been sent against {@code target} already. */
    private static boolean struck(FieldArmy army, FieldBlock target) {
        for (FieldBlock b : army.blocks) {
            if (b.strikeAt == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enemy cavalry that has stopped (caught in a melee, or waiting) is set on by the infantry near it: up to
     * {@link FieldRules#HUNTERS_PER_RIDERS} idle blocks within {@link FieldRules#HUNT_REACH} close in on its flanks.
     * They let it go once it rides on ({@link #strike}).
     */
    /**
     * Envelopment: once the army is fighting, a front block with no enemy ahead of it (its wing reaches past the end
     * of the enemy's line) wheels onto the nearest enemy infantry beside it, within {@link FieldRules#ENVELOP_REACH}:
     * the first block strikes its flank, a second its rear. When that block falls it turns on the next, so an
     * overlapping wing rolls up the enemy's exposed flank. The blocks waiting in its file follow it round.
     */
    private void envelop(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS || !army.infantryEngaged()) {
            return;
        }
        java.util.Map<FieldBlock, Integer> attackers = new java.util.HashMap<>();
        for (FieldBlock b : army.blocks) {
            if (b.strikeAt != null) {
                attackers.merge(b.strikeAt, 1, Integer::sum);
            }
        }
        boolean[] wheeling = new boolean[3];
        for (FieldBlock b : army.front) {
            if (b.alive == 0 || b.duty != Duty.FRONT || b.contactFoe != null || b.strikeAt != null || b.braced
                    || b.charged || isBait(army, b.file)) {
                continue;
            }
            List<FieldBlock> beside = new ArrayList<>();
            boolean ahead = false;
            for (FieldBlock e : enemy.blocks) {
                if (e.alive == 0 || !e.arm.infantry() || e.duty == Duty.FLEEING || e.pos.distanceTo(b.pos) > FieldRules.ENVELOP_REACH) {
                    continue;
                }
                double dx = e.pos.x() - b.pos.x();
                double dz = e.pos.z() - b.pos.z();
                double front = dx * army.facingX + dz * army.facingZ;
                double side = Math.abs(dx * army.facingZ - dz * army.facingX);
                if (front >= side) {
                    ahead = true; // an enemy in front: the line deals with it
                    break;
                }
                beside.add(e);
            }
            if (ahead || beside.isEmpty()) {
                continue;
            }
            beside.sort(Comparator.comparingDouble(e -> e.pos.distanceTo(b.pos)));
            for (FieldBlock target : beside) {
                int n = attackers.getOrDefault(target, 0);
                if (n < FieldRules.ENVELOPERS) {
                    send(b, target, n == 0 ? sideNearer(target, b.pos) : 0);
                    attackers.merge(target, 1, Integer::sum);
                    wheeling[b.wing] = true;
                    break;
                }
            }
        }
        for (int wing = 0; wing < 3; wing++) {
            if (wheeling[wing] && !army.enveloping[wing]) {
                events.add(new Event(army.side, "wheels" + (army.where(wing).isEmpty() ? " the line's end" : army.where(wing).replace(" on the", " the").replace(" in the", " the"))
                        + " onto the enemy's exposed flank"));
            }
            army.enveloping[wing] = wheeling[wing] || army.enveloping[wing] && struckBy(army, wing);
        }
    }

    /** Whether any block of a wing is striking something (enveloping, in a pincer, or hunting cavalry). */
    private static boolean struckBy(FieldArmy army, int wing) {
        for (FieldBlock b : army.blocks) {
            if (b.wing == wing && b.strikeAt != null) {
                return true;
            }
        }
        return false;
    }

    private void huntStoppedCavalry(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS) {
            return;
        }
        // Who is already hunting whom, and who is free, once for all the stopped cavalry.
        java.util.Map<FieldBlock, Integer> hunters = new java.util.HashMap<>();
        for (FieldBlock b : army.blocks) {
            if (b.strikeAt != null) {
                hunters.merge(b.strikeAt, 1, Integer::sum);
            }
        }
        for (FieldBlock c : enemy.blocks) {
            if (c.alive == 0 || c.arm != Arm.CAVALRY || c.duty == Duty.FLEEING || c.lastPos == null || c.moved >= FieldRules.STOPPED) {
                continue;
            }
            int sent = hunters.getOrDefault(c, 0);
            if (sent >= FieldRules.HUNTERS_PER_RIDERS) {
                continue;
            }
            List<FieldBlock> near = new ArrayList<>();
            java.util.function.Consumer<FieldBlock> consider = b -> {
                if (b.strikeAt == null && b.pos.distanceTo(c.pos) <= FieldRules.HUNT_REACH && idle(army, b, -1)) {
                    near.add(b);
                }
            };
            if (army.grid != null) {
                army.grid.within(c.pos, FieldRules.HUNT_REACH, consider);
            } else {
                army.blocks.forEach(consider);
            }
            near.sort(Comparator.comparingDouble(b -> b.pos.distanceTo(c.pos)));
            for (int i = 0; i < near.size() && sent < FieldRules.HUNTERS_PER_RIDERS; i++, sent++) {
                FieldBlock hunter = near.get(i);
                send(hunter, c, i == 0 ? sideNearer(c, hunter.pos) : -sideNearer(c, near.get(0).pos));
                if (i == 0) {
                    events.add(new Event(army.side, "sends infantry at stopped enemy cavalry" + army.where(hunter.wing)));
                }
            }
        }
    }

    /**
     * Before the battle begins, gives a block a player's unit's task: bait (a front block, which becomes its wing's
     * bait whatever the general's plan), support, flank or kill. The general leaves such a block to its task.
     */
    public void assign(FieldBlock block, FieldBlock.Task task) {
        if (begun()) {
            throw new IllegalStateException("the battle has begun");
        }
        block.task = task;
        if (task == FieldBlock.Task.BAIT && block.duty == Duty.FRONT) {
            armies[block.side].baitFile[block.wing] = block.file;
        }
    }

    /**
     * Blocks with a task, once the lines have formed, free and not yet sent: support goes for the enemy fighting the
     * nearest losing block of its wing; a flanker for the nearest enemy infantry in a fight, from the side nearer it
     * (from behind, if others strike it already); a kill squad for the enemy general, else the nearest enemy block whose
     * captain still stands, the nearer to a fight the better. Each strikes as infantry sent by the general does
     * ({@link #strike}), and when its target is gone it waits where it belongs until the next.
     */
    private void tasks(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS) {
            return;
        }
        for (FieldBlock b : army.blocks) {
            if (b.alive == 0 || b.task == FieldBlock.Task.NONE || b.task == FieldBlock.Task.BAIT || b.strikeAt != null
                    || b.charged || b.braced || b.duty != Duty.LINE && b.duty != Duty.FRONT) {
                continue;
            }
            if (b.task == FieldBlock.Task.AMBUSH && b.sprung && b.strikeAt == null) {
                b.sprung = false; // its quarry gone: it waits for the next
            }
            FieldBlock target = switch (b.task) {
                case SUPPORT -> losingFoe(army, b);
                case FLANK -> nearestFighting(enemy, b, FieldRules.TASK_REACH);
                case KILL -> officers(enemy, b);
                case AMBUSH -> nearestFighting(enemy, b, FieldRules.AMBUSH_SPRING);
                default -> null;
            };
            if (target != null) {
                if (b.task == FieldBlock.Task.AMBUSH && !b.sprung) {
                    b.sprung = true;
                    events.add(new Event(army.side, "springs an ambush from the woods" + army.where(b.wing)));
                }
                send(b, target, b.task == FieldBlock.Task.FLANK && struck(army, target) ? 0 : sideNearer(target, b.pos));
            }
        }
    }

    /** The enemy fighting the nearest block of its wing that is losing (worn down, or outnumbered by its foe). */
    @Nullable
    private static FieldBlock losingFoe(FieldArmy army, FieldBlock block) {
        FieldBlock best = null;
        for (FieldBlock f : army.blocks) {
            FieldBlock foe = f.contactFoe;
            if (f == block || f.alive == 0 || foe == null || foe.alive == 0 || army.winged && f.wing != block.wing) {
                continue;
            }
            boolean losing = f.alive < f.started() * FieldRules.LOSING_SHARE || f.alive < foe.alive * FieldRules.GIVE_GROUND_SHARE;
            if (losing && (best == null || f.pos.distanceTo(block.pos) < best.pos.distanceTo(block.pos))) {
                best = f;
            }
        }
        return best == null ? null : best.contactFoe;
    }

    /** The nearest enemy infantry block in a fight, within {@code reach}. */
    @Nullable
    private static FieldBlock nearestFighting(FieldArmy enemy, FieldBlock block, double reach) {
        FieldBlock best = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.arm.infantry() && e.contactFoe != null && e.duty != Duty.FLEEING && e.duty != Duty.ROUTED
                    && e.pos.distanceTo(block.pos) <= reach
                    && (best == null || e.pos.distanceTo(block.pos) < best.pos.distanceTo(block.pos))) {
                best = e;
            }
        }
        return best;
    }

    /** The enemy general within reach, else the nearest enemy block whose captain stands, those fighting first. */
    @Nullable
    private static FieldBlock officers(FieldArmy enemy, FieldBlock block) {
        FieldBlock general = enemy.commander;
        if (general != null && general.alive > 0 && general.pos.distanceTo(block.pos) <= FieldRules.TASK_REACH) {
            return general;
        }
        FieldBlock best = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive == 0 || !e.captainAlive() || e.duty == Duty.FLEEING || e.duty == Duty.ROUTED || e.pos.distanceTo(block.pos) > FieldRules.TASK_REACH) {
                continue;
            }
            boolean better;
            if (best == null) {
                better = true;
            } else if ((e.contactFoe != null) != (best.contactFoe != null)) {
                better = e.contactFoe != null;
            } else {
                better = e.pos.distanceTo(block.pos) < best.pos.distanceTo(block.pos);
            }
            if (better) {
                best = e;
            }
        }
        return best;
    }

    /**
     * A real soldier or player strikes soldier {@code i} of a block where it is only a figure: it takes the blow in
     * the numbers (a block part real counts its fallen at its next report).
     */
    public void strikeFigure(FieldBlock block, int i, float damage) {
        if (i < 0 || i >= block.health.length || block.health[i] <= 0) {
            return;
        }
        lastHurt[block.side] = seconds;
        if (block.live) {
            block.hurt(i, damage);
            return;
        }
        block.health[i] = Math.max(0, block.health[i] - damage);
        if (block.health[i] == 0) {
            block.alive--;
            lastFall = seconds;
        }
    }

    /** A cavalry block's charge is broken against a braced shield: all its momentum is spent. */
    public void breakCharge(FieldBlock block) {
        block.momentum = 0;
    }

    private static void send(FieldBlock block, FieldBlock target, int side) {
        block.strikeAt = target;
        block.strikeSide = side;
    }

    /** Which flank of {@code target} is nearer {@code from}: -1 its left, 1 its right. */
    private static int sideNearer(FieldBlock target, Point from) {
        double right = (from.x() - target.pos.x()) * -target.facingZ + (from.z() - target.pos.z()) * target.facingX;
        return right >= 0 ? 1 : -1;
    }

    /**
     * Infantry sent against a block (a pincer's lured block, stopped cavalry) closes on its flank or rear, facing it,
     * and fights it there; it lets go once the block is gone, the cavalry rides on, or it is led too far off.
     */
    private void strike(FieldArmy army, FieldBlock block) {
        FieldBlock t = block.strikeAt;
        boolean gone = t == null || t.alive == 0 || t.duty == Duty.FLEEING;
        // A kill squad keeps after its officer, mounted or not.
        boolean escaped = !gone && block.task != FieldBlock.Task.KILL && t.arm == Arm.CAVALRY && (t.moved >= FieldRules.STOPPED * 3
                || block.pos.distanceTo(t.pos) > FieldRules.HUNT_REACH * 2);
        if (gone || escaped) {
            release(army, block);
            return;
        }
        Point at;
        if (block.strikeSide == 0) {
            double depth = (double) t.started() / t.files * BattlePlan.SPACING + 2;
            at = t.pos.plus(-t.facingX * depth, -t.facingZ * depth);
        } else {
            double out = t.halfWidth() + 2;
            at = t.pos.plus(-t.facingZ * out * block.strikeSide, t.facingX * out * block.strikeSide);
        }
        block.line = at;
        face(block, t.pos);
    }

    /** Back to its place in the army: a front block holds where it stands, facing the enemy again. */
    private static void release(FieldArmy army, FieldBlock block) {
        block.strikeAt = null;
        block.facingX = army.facingX;
        block.facingZ = army.facingZ;
        if (block.duty == Duty.FRONT) {
            block.line = block.pos;
        }
    }

    // ---- morale ----

    /**
     * Each second, after the fighting: an infantry block that has lost {@link FieldRules#ROUT_LOSS} of its soldiers
     * within the last {@link FieldRules#ROUT_WINDOW} seconds routs, unless rallied; its captain's fall shakes it, counting
     * as {@link FieldRules#CAPTAIN_SHOCK} more for as long. The enemy blocks fighting a block that routs or loses its
     * captain rally.
     */
    private void morale(FieldArmy army, FieldArmy enemy) {
        int slot = seconds % FieldRules.ROUT_WINDOW;
        for (FieldBlock block : army.blocks) {
            if (!block.arm.infantry() || block.started() == 1) {
                continue;
            }
            if (block.history == null) {
                block.history = new int[FieldRules.ROUT_WINDOW];
                java.util.Arrays.fill(block.history, block.alive);
            }
            int before = block.history[slot];
            block.history[slot] = block.alive;
            if (block.alive == 0 || block.duty == Duty.ROUTED || block.duty == Duty.FLEEING || block.duty == Duty.WITHDRAWING) {
                block.captainWasAlive = block.captainAlive();
                continue;
            }
            boolean captainFell = block.captainWasAlive && !block.captainAlive();
            block.captainWasAlive = block.captainAlive();
            if (captainFell) {
                block.shakenUntil = seconds + FieldRules.ROUT_WINDOW;
                rallyAgainst(enemy, block);
            }
            double lost = before == 0 ? 0 : (double) (before - block.alive) / before;
            if (seconds < block.shakenUntil) {
                lost += FieldRules.CAPTAIN_SHOCK;
            }
            if (lost >= FieldRules.ROUT_LOSS && !block.rallied(seconds)) {
                rout(army, block);
                rallyAgainst(enemy, block);
            }
        }
    }

    /** The enemy blocks fighting a block that just broke, or lost its captain, rally. */
    private void rallyAgainst(FieldArmy enemy, FieldBlock broken) {
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.contactFoe == broken) {
                e.rallyUntil = seconds + FieldRules.RALLIED_SECONDS;
                if (e.history != null) {
                    java.util.Arrays.fill(e.history, e.alive);
                }
                e.shakenUntil = 0;
            }
        }
    }

    /**
     * A block routs: it stops fighting and runs behind its army's reserves to regroup. A front block's place is
     * taken at once by the next block of its file, if there is one.
     */
    private void rout(FieldArmy army, FieldBlock block) {
        if (block.duty == Duty.FRONT && army.front[block.file] == block) {
            List<FieldBlock> queue = army.queue(block.file);
            if (!queue.isEmpty()) {
                FieldBlock next = queue.get(0);
                army.front[block.file] = next;
                next.duty = Duty.FRONT;
                next.line = block.line;
                next.engaged = false;
                next.relief = null;
                next.fightSeconds = 0;
                next.placed = null;
            }
        }
        block.duty = Duty.ROUTED;
        block.dutySince = seconds;
        block.contactFoe = null;
        block.contactSeconds = 0;
        block.charged = false;
        block.approaching = false;
        block.braced = false;
        block.engaged = false;
        block.strikeAt = null;
        block.relief = null;
        block.rotating = false;
        block.fightSeconds = 0;
        block.facingX = army.facingX;
        block.facingZ = army.facingZ;
        block.line = BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, block.homeRight, army.rearBack + FieldRules.ROUT_RUN);
    }

    // ---- judging the enemy ----

    /** Whether the enemy's general stands exposed: their guard away or fallen, and none of their army near. */
    private static boolean exposed(FieldArmy enemy) {
        FieldBlock general = enemy.commander;
        if (general == null || general.alive == 0) {
            return false;
        }
        for (FieldBlock b : enemy.blocks) {
            if (b != general && b.alive > 0 && b.duty != Duty.BEARER && b.duty != Duty.FLEEING
                    && b.pos.distanceTo(general.pos) <= FieldRules.GENERAL_COVER) {
                return false;
            }
        }
        return true;
    }

    /**
     * The most exposed enemy block, nearest {@code from} among equals: too far ahead of its own line, fighting with a
     * flank open (an end of the line, or its neighbour fallen), or worn down, within {@code reach}. Null if none is
     * exposed.
     */
    @Nullable
    private static FieldBlock mostExposed(FieldArmy enemy, Point from, double reach) {
        FieldBlock best = null;
        int bestScore = 0;
        for (FieldBlock e : enemy.blocks) {
            if (e.pos.distanceTo(from) > reach || e.alive == 0 || e.duty == Duty.FLEEING || e.duty == Duty.BEARER || e.duty == Duty.GENERAL || !e.arm.infantry() && e.arm != Arm.ARCHERS) {
                continue;
            }
            int score = 0;
            if (-behindFront(enemy, e.pos) >= FieldRules.TOO_FAR_FORWARD) {
                score++;
            }
            if (e.duty == Duty.FRONT && e.engaged && openFlank(enemy, e)) {
                score++;
            }
            if (e.alive <= e.started() * FieldRules.WORN_SHARE) {
                score++;
            }
            if (score > bestScore || score == bestScore && score > 0 && e.pos.distanceTo(from) < best.pos.distanceTo(from)) {
                best = e;
                bestScore = score;
            }
        }
        return best;
    }

    private static boolean openFlank(FieldArmy army, FieldBlock block) {
        int file = block.file;
        boolean left = file == 0 || army.front[file - 1].alive == 0;
        boolean right = file == army.front.length - 1 || army.front[file + 1].alive == 0;
        return left || right;
    }

    /** The enemy block within {@code reach} of {@code from} worth the most: the most soldiers, for the distance. */
    @Nullable
    private static FieldBlock mostSoldiers(FieldArmy enemy, Point from, double reach) {
        FieldBlock best = null;
        double bestScore = 0;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive == 0 || e.duty == Duty.FLEEING || e.duty == Duty.BEARER) {
                continue;
            }
            double d = e.pos.distanceTo(from);
            double score = e.alive / (d + 20);
            if (d <= reach && score > bestScore) {
                best = e;
                bestScore = score;
            }
        }
        return best;
    }

    /**
     * Whom a block that breaks formation goes for, by its general's strategies: cavalry the enemy general
     * (Decapitate), else the most soldiers near (Mass Casualties), else an exposed block (Precision Strikes); null for
     * the nearest.
     */
    @Nullable
    private FieldBlock choosePrey(FieldArmy army, FieldArmy enemy, FieldBlock block) {
        if (block.arm == Arm.CAVALRY && army.plan.has(Strategy.DECAPITATE) && enemy.commander != null && enemy.commander.alive > 0
                && enemy.commander.pos.distanceTo(block.pos) <= 60) {
            return enemy.commander;
        }
        if (army.plan.has(Strategy.MASS_CASUALTIES)) {
            FieldBlock e = mostSoldiers(enemy, block.pos, 60);
            if (e != null) {
                return e;
            }
        }
        if (army.plan.has(Strategy.PRECISION_STRIKES)) {
            FieldBlock e = mostExposed(enemy, block.pos, 60);
            if (e != null && e.pos.distanceTo(block.pos) <= 60) {
                return e;
            }
        }
        return null;
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
        if (enemy.grid != null) {
            // Orders only care how near an enemy is up to a few dozen blocks: further is as good as none.
            FieldBlock e = enemy.grid.nearest(at, block.halfWidth(), b -> true, b -> block.distanceFrom(at, b), FieldRules.ORDERS_REACH);
            return e == null ? Double.MAX_VALUE : block.distanceFrom(at, e);
        }
        double best = Double.MAX_VALUE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && closeEnough(block, at, e, best)) {
                best = Math.min(best, block.distanceFrom(at, e));
            }
        }
        return best;
    }

    /** The nearest living enemy block no further than {@code reach}, or null. */
    @Nullable
    private static FieldBlock nearestWithin(FieldBlock block, FieldArmy enemy, double reach) {
        if (enemy.grid != null) {
            return enemy.grid.nearest(block.pos, block.halfWidth(), b -> true, block::distanceTo, reach);
        }
        FieldBlock e = nearestBlock(block, enemy, null);
        return e != null && block.distanceTo(e) <= reach ? e : null;
    }

    @Nullable
    private static FieldBlock nearestBlock(FieldBlock block, FieldArmy enemy, @Nullable Arm prefer) {
        if (enemy.grid != null) {
            if (prefer != null) {
                FieldBlock e = enemy.grid.nearest(block.pos, block.halfWidth(), b -> b.arm == prefer && b.duty == Duty.SUPPORT, block::distanceTo);
                if (e != null) {
                    return e;
                }
            }
            return enemy.grid.nearest(block.pos, block.halfWidth(), b -> true, block::distanceTo);
        }
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

    /** Whether a block is a living enemy worth going after. */
    private static boolean hunted(@Nullable FieldBlock prey) {
        return prey != null && prey.alive > 0 && prey.duty != Duty.FLEEING;
    }

    // ---- movement ----

    /** Every block walks toward its line at its pace; one that has broken formation goes for its prey. */
    private void move(FieldArmy army) {
        FieldArmy enemy = armies[1 - army.side];
        for (FieldBlock block : army.blocks) {
            if (block.alive == 0) {
                continue;
            }
            if (block.charged && block.duty != Duty.FLEEING && !hunted(block.prey)) {
                block.prey = choosePrey(army, enemy, block);
            }
            if (block.live) {
                continue; // real soldiers walk themselves
            }
            Point target = block.line;
            if (block.charged && block.duty != Duty.FLEEING && block != army.commander) {
                FieldBlock prey = hunted(block.prey) ? block.prey
                        : nearestBlock(block, enemy, block.arm == Arm.CAVALRY ? Arm.ARCHERS : null);
                if (prey == null) {
                    continue;
                }
                face(block, prey.pos);
                if (block.distanceTo(prey) <= FieldRules.CLOSE_REACH) {
                    continue;
                }
                FieldBlock blocking = block.contactFoe;
                if (blocking != null && blocking != prey && blocking.alive > 0 && blocking.duty != Duty.FLEEING) {
                    continue; // caught fighting whoever stood in the way: it can't ride through them
                }
                target = prey.pos;
            }
            double dx = target.x() - block.pos.x();
            double dz = target.z() - block.pos.z();
            double distance = Math.hypot(dx, dz);
            double step = rates.speed(block.arm);
            if (pursuing && block.duty == Duty.FLEEING) {
                step *= FieldRules.FLEE_SPEED; // running for their lives, gear thrown down
            }
            block.pos = distance <= step ? target : block.pos.plus(dx / distance * step, dz / distance * step);
        }
    }

    // ---- fighting ----

    /**
     * Everyone within reach fights, all at once: each block strikes its prey if within reach, else the nearest enemy
     * block within its reach (an archer block with arrows shoots instead when nobody is at hand). Blocks waiting in a
     * file's queue reach no further than those that broke formation. Damage is dealt only once every block has struck,
     * so the order of blocks doesn't matter.
     */
    private void fight() {
        List<FieldBlock> targets = new ArrayList<>();
        List<Double> damages = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            FieldArmy army = armies[side];
            FieldArmy enemy = armies[1 - side];
            for (FieldBlock block : army.blocks) {
                if (block.alive == 0 || block.duty == Duty.WITHDRAWING || block.duty == Duty.BEARER || block.duty == Duty.FLEEING
                        || block.duty == Duty.RALLYING || block.duty == Duty.ROUTED) {
                    block.contactFoe = null;
                    continue;
                }
                boolean holding = !block.charged && !block.approaching && block.duty != Duty.LINE && block.duty != Duty.GENERAL && block.duty != Duty.GUARD;
                double reach = holding ? FieldRules.HOLDING_REACH : FieldRules.CLOSE_REACH;
                FieldBlock foe = hunted(block.prey) && block.distanceTo(block.prey) <= reach ? block.prey : nearestWithin(block, enemy, reach);
                if (foe != null) {
                    block.contactSeconds++;
                    if (block.duty == Duty.FRONT) {
                        block.fightSeconds++;
                    }
                    block.contactFoe = foe;
                    double fighters = block.champion ? rates.warriorFightsAs() : block.alive;
                    if (block.live && foe.live) {
                        // Real soldiers on both sides: Recruits fights it out between them. Those of the block still
                        // only numbers fight on as numbers.
                        int figures = block.alive - block.real;
                        if (figures <= 0) {
                            continue;
                        }
                        fighters = block.champion ? fighters : figures;
                    }
                    double damage = fighters * rates.meleeDps(block.arm)
                            * rates.meleeFactor(sideOf(foe, block), foe.arm);
                    if (block.arm == Arm.CAVALRY && block.charged) {
                        damage *= 1 + rates.chargeBonus() * block.momentum;
                    }
                    if (foe.braced && block.arm == Arm.CAVALRY && sideOf(foe, block) == FieldRules.Side.FRONT) {
                        damage *= FieldRules.BRACED_TAKES;
                    }
                    if (block.braced && foe.arm == Arm.CAVALRY && sideOf(block, foe) == FieldRules.Side.FRONT) {
                        damage *= FieldRules.BRACED_DEALS;
                    }
                    targets.add(foe);
                    damages.add(damage);
                    continue;
                }
                block.contactSeconds = 0;
                block.contactFoe = null;
                if (block.arm == Arm.ARCHERS && block.arrows > 0) {
                    shoot(army, block, enemy, targets, damages);
                }
            }
        }
        for (int i = 0; i < targets.size(); i++) {
            FieldBlock target = targets.get(i);
            double damage = damages.get(i) * (resolute(target) ? FieldRules.RESOLVE_TAKES : 1);
            if (damage > 0) {
                lastHurt[target.side] = seconds;
            }
            if (target.live) {
                target.liveDamage += damage; // dealt to the real soldiers by the live battle
            } else if (target.takeDamage(damage, random) > 0) {
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
                    // A general leading the charge, and their guard, spend it at half the rate.
                    double rate = leadsTheCharge(block) ? FieldRules.LEADER_MOMENTUM_LOSS : 1;
                    if (foe.braced && sideOf(foe, block) == FieldRules.Side.FRONT) {
                        block.momentum -= block.momentum * rate;
                    } else {
                        double loss = foe.arm == Arm.CAVALRY ? FieldRules.MOMENTUM_LOSS_CAVALRY : FieldRules.MOMENTUM_LOSS_INFANTRY;
                        block.momentum = Math.max(0, block.momentum - loss * rate);
                    }
                } else if (block.duty != Duty.RALLYING) {
                    block.momentum = Math.min(block.maxMomentum, block.momentum + moved / FieldRules.RUN_UP);
                }
                block.moved = moved;
                block.lastPos = block.pos;
            }
        }
    }

    /** Archers shoot the nearest enemy in range, or the one with most soldiers (Mass Casualties). */
    private void shoot(FieldArmy army, FieldBlock archers, FieldArmy enemy, List<FieldBlock> targets, List<Double> damages) {
        FieldBlock foe = army.plan.has(Strategy.MASS_CASUALTIES) ? mostSoldiers(enemy, archers.pos, FieldRules.ARCHER_RANGE) : null;
        if (foe == null) {
            FieldBlock[] nearest = {null};
            double[] best = {FieldRules.ARCHER_RANGE};
            java.util.function.Consumer<FieldBlock> consider = e -> {
                if (e.alive > 0) {
                    double d = Math.hypot(e.pos.x() - archers.pos.x(), e.pos.z() - archers.pos.z());
                    if (d <= best[0]) {
                        best[0] = d;
                        nearest[0] = e;
                    }
                }
            };
            if (enemy.grid != null) {
                enemy.grid.within(archers.pos, FieldRules.ARCHER_RANGE, consider);
            } else {
                enemy.blocks.forEach(consider);
            }
            foe = nearest[0];
        }
        if (foe == null) {
            return;
        }
        int shots = (int) Math.min(archers.arrows, Math.ceil(archers.alive * FieldRules.ARROWS_PER_SECOND));
        archers.arrows -= shots;
        if (shots > 0) {
            volleys.add(new Volley(archers, foe, shots));
        }
        targets.add(foe);
        damages.add(shots * rates.arrowDamage() * FieldRules.arrowFactor(sideOf(foe, archers), foe.arm));
    }

    /** Where an attack from {@code attacker} falls on {@code defender}. */
    private static FieldRules.Side sideOf(FieldBlock defender, FieldBlock attacker) {
        return FieldRules.side(defender.facingX, defender.facingZ,
                attacker.pos.x() - defender.pos.x(), attacker.pos.z() - defender.pos.z());
    }

    // ---- the end ----

    /** An army whose general falls retreats at once; one worn down to {@link BattlePlan#ROUT_SHARE} routs. */
    private void checkEnd() {
        boolean fellA = armies[0].commander != null && armies[0].commander.alive == 0;
        boolean fellB = armies[1].commander != null && armies[1].commander.alive == 0;
        if (fellA || fellB) {
            if (fellA && fellB) {
                outcome = new Outcome(-1, "both generals fell", seconds);
                flee(armies[0]);
                flee(armies[1]);
            } else {
                int loser = fellA ? 0 : 1;
                outcome = new Outcome(1 - loser, "side " + loser + "'s general fell; side " + loser + " retreats", seconds);
                flee(armies[loser]);
            }
            return;
        }
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

    /**
     * Evening: the fighting stops and every block marches back to its army's camp, {@code back[side]} behind where it
     * deployed, in its place, facing the enemy again. From now on the blocks only march.
     */
    public void pullBack(double[] back) {
        pulledBack = true;
        for (FieldArmy army : armies) {
            for (FieldBlock b : army.blocks) {
                stand(b);
                b.line = b.deployed.plus(-army.facingX * back[army.side], -army.facingZ * back[army.side]);
                b.facingX = army.facingX;
                b.facingZ = army.facingZ;
            }
        }
    }

    /**
     * A morning: every block starts {@code back} behind its place, in camp where it rested, and marches out to it,
     * until {@link #formed}. Blocks only march meanwhile.
     */
    public void formUp(double back) {
        forming = true;
        for (FieldArmy army : armies) {
            for (FieldBlock b : army.blocks) {
                b.pos = b.line.plus(-army.facingX * back, -army.facingZ * back);
            }
        }
    }

    public boolean forming() {
        return forming;
    }

    /** The armies are in place (or out of time): the battle may begin. */
    public void formed() {
        forming = false;
    }

    public boolean pulledBack() {
        return pulledBack;
    }

    /** Whether every block still standing is back where it was sent (a block's width out at most). */
    public boolean arrived() {
        for (FieldArmy army : armies) {
            for (FieldBlock b : army.blocks) {
                if (b.alive > 0 && b.pos.distanceTo(b.line) > FieldRules.ARRIVED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * After a win, the winner pursues the beaten army as it runs: each of its blocks (the general, their guard and the
     * standard aside) makes for the nearest of the enemy, and fights it when it catches it; the beaten army keeps running
     * back the way it came.
     */
    public void pursue() {
        if (outcome == null || outcome.winner() < 0) {
            return;
        }
        pursuing = true;
    }

    public boolean pursuing() {
        return pursuing;
    }

    private void pursueSecond() {
        seconds++;
        index();
        FieldArmy winner = armies[outcome.winner()];
        FieldArmy loser = armies[1 - outcome.winner()];
        for (FieldBlock b : loser.blocks) {
            if (b.alive > 0) {
                b.line = b.pos.plus(-loser.facingX * FieldRules.FLEE_STEP, -loser.facingZ * FieldRules.FLEE_STEP);
            }
        }
        for (FieldBlock b : winner.blocks) {
            if (b.alive == 0 || b.duty == Duty.BEARER || b.duty == Duty.GENERAL || b.duty == Duty.GUARD) {
                continue;
            }
            FieldBlock prey = nearestBlock(b, loser, null);
            if (prey != null) {
                b.line = prey.pos;
                face(b, prey.pos);
            }
        }
        for (FieldArmy army : armies) {
            move(army);
        }
        index();
        fight();
    }

    /**
     * The fighting is over without a pursuit: the winner halts where it stands, and so does the loser if it {@code
     * surrendered} (it mills about, no longer fighting); else it keeps running back the way it came.
     */
    public void standDown(boolean surrendered) {
        if (outcome == null) {
            return;
        }
        for (FieldArmy army : armies) {
            boolean beaten = outcome.winner() >= 0 && army.side != outcome.winner();
            if (beaten && !surrendered) {
                continue;
            }
            for (FieldBlock b : army.blocks) {
                stand(b);
                b.line = b.pos;
                if (beaten) {
                    b.duty = Duty.FLEEING;
                }
            }
        }
    }

    /** For testing: side {@code side} gives up the battle (surrenders, its general alive), as if worn down. */
    public void concede(int side) {
        if (outcome != null) {
            return;
        }
        outcome = new Outcome(1 - side, "side " + side + " surrenders", seconds);
        flee(armies[side]);
    }

    /** For testing: side {@code side}'s general falls. */
    public void killGeneral(int side) {
        FieldBlock general = armies[side].commander;
        if (general != null) {
            sync(general, general.pos, new float[general.health.length]);
        }
    }

    /** A block stops whatever it was doing. */
    private static void stand(FieldBlock b) {
        b.charged = false;
        b.braced = false;
        b.engaged = false;
        b.approaching = false;
        b.waypoint = null;
        b.prey = null;
        b.strikeAt = null;
        b.contactFoe = null;
    }

    /** When a side last took harm, in battle seconds; -1 if never. */
    public int lastHurt(int side) {
        return lastHurt[side];
    }

    /** The routed army stops fighting and runs back the way it came. */
    private static void flee(FieldArmy loser) {
        for (FieldBlock block : loser.blocks) {
            block.duty = Duty.FLEEING;
            block.charged = false;
            block.braced = false;
            block.engaged = false;
            block.approaching = false;
            block.waypoint = null;
            block.prey = null;
            block.strikeAt = null;
            block.line = block.pos.plus(-loser.facingX * 60, -loser.facingZ * 60);
        }
    }
}
