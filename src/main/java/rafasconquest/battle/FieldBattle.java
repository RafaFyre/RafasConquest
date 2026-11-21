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
    /** Shots fired as numbers this second, for clients to draw; {@code gun}: the engine that fired them, null for arrows and shot. */
    public record Volley(FieldBlock archers, FieldBlock target, int shots, @org.jetbrains.annotations.Nullable Machine gun) {
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

    /** A battle line from {@code dialogue/battle.toml}. */
    private static String say(String key, Object... vars) {
        return rafasconquest.data.Dialogue.say("battle." + key, vars);
    }

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
    /** What kind of battle it is: in a walled one, the defenders (side 1) hold the line they deployed on, their walls. */
    private BattleType type = BattleType.LAND;
    /** Behind their walls, defenders take this share of the harm done them. */
    static final double WALL_TAKES = 0.45;
    /** How far out past the wall line a defender still counts as on the walls. */
    static final double WALL_BAND = 2;

    public BattleType type() {
        return type;
    }

    /** A siege's own rules ({@link Siege}), or null in any other battle. */
    @Nullable
    private Siege siege;

    /** Makes it a siege of the fortress {@code plan} surveys, both armies laid out for it; a siege, before it begins only. */
    public void besiege(Siege.Plan plan) {
        if (begun() || type != BattleType.SIEGE) {
            throw new IllegalStateException("a siege is laid out before it begins");
        }
        // The attackers come at one wall: one body before it (as wide as it and as much again either side, so it isn't
        // endlessly deep), not a field army's three wings spread far past it, each with its own camp.
        FieldArmy a = armies[0];
        int files = Math.max(BattlePlan.MAX_FRONT, (int) (3 * plan.wallLength() / BattlePlan.pitch(BattlePlan.FILE_WIDTH)));
        armies[0] = deploy(0, firstBlocks, a.origin, a.facingX, a.facingZ, a.plan, levyFront(a.plan, random), byUnits, files, mixes[0]);
        if (trains.containsKey(0)) {
            equip(0, trains.get(0));
        }
        siege = new Siege(plan);
        siege.deploy(armies[0], armies[1]);
    }

    @Nullable
    public Siege siege() {
        return siege;
    }

    /** Ships sail this many blocks a second, whatever their soldiers' arm. */
    public static final double SHIP_SPEED = 2.5;
    /** In an amphibious assault, a ship this close to the walls has reached the wharf: its soldiers land. */
    static final double LANDING = 10;

    /**
     * Makes it a battle of this kind; before it begins only. At sea both armies sail; in an amphibious assault the
     * attacker does, until it lands.
     */
    public void setType(BattleType type) {
        if (begun()) {
            throw new IllegalStateException("the battle has begun");
        }
        this.type = type;
        crews.clear();
        for (FieldArmy army : armies) {
            if (type == BattleType.OCEAN || type == BattleType.AMPHIBIOUS && army.side == 0) {
                // No horses or engines on a fighting deck, and no supply ships to carry them: they stay out of the battle.
                List<FieldBlock> ashore = army.blocks.stream().filter(b -> (b.arm.engine() || b.arm.mounted())
                        && b.duty != Duty.GENERAL && b.duty != Duty.GUARD && b.duty != Duty.BEARER).toList();
                army.blocks.removeAll(ashore);
                army.wedge.removeAll(ashore);
                army.started -= ashore.stream().mapToInt(FieldBlock::started).sum();
                army.forgetPlatoons();
            }
            int[] swordsmen = {0};
            // Each unit (platoon) is one ship, all its blocks aboard; the general, guard and standard share the flagship.
            java.util.Map<Integer, List<FieldBlock>> byShip = new java.util.LinkedHashMap<>();
            java.util.Map<Integer, FieldBlock.ShipKind> kinds = new java.util.HashMap<>();
            for (FieldBlock b : army.blocks) {
                b.afloat = type == BattleType.OCEAN || type == BattleType.AMPHIBIOUS && army.side == 0;
                b.ship = null;
                b.role = null;
                b.wedgeAt = null;
                b.shipId = b.id;
                b.berth = 0;
                b.hull = rafasconquest.core.SeaLanes.Size.LARGE;
                if (!b.afloat) {
                    continue;
                }
                boolean command = b.duty == Duty.GENERAL || b.duty == Duty.GUARD || b.duty == Duty.BEARER;
                if (!command) {
                    b.ship = kinds.computeIfAbsent(b.platoon, p -> b.arm.ship() != null && b.arm.ship().gunship() ? FieldBlock.ShipKind.GUNSHIP
                            : b.arm.shoots() ? FieldBlock.ShipKind.ARCHERS
                            : b.arm.shielded() ? FieldBlock.ShipKind.SWORDSMEN
                            : swordsmen[0]++ % 3 == 2 ? FieldBlock.ShipKind.SHOCK : FieldBlock.ShipKind.SWORDSMEN);
                }
                byShip.computeIfAbsent(command ? -1 : b.platoon >= 0 ? b.platoon : -2 - b.id, k -> new ArrayList<>()).add(b);
            }
            // The admiral, their guard and standard go aboard the fleet's biggest ship on top of its own crew, and fight as it
            // does; a flagship of their own only with no other ship in the fleet.
            List<FieldBlock> command = byShip.remove(-1);
            if (command != null) {
                List<FieldBlock> biggest = byShip.values().stream().max(Comparator.comparingInt((List<FieldBlock> c) -> {
                    rafasconquest.core.SeaLanes.Size s = shipOf(c);
                    return s.length * s.width;
                }).thenComparing(c -> shipOf(c).gunship())).orElse(null); // a gunship of the same size first: it sails in a battle line
                if (biggest == null) {
                    byShip.put(-1, command);
                } else {
                    command.forEach(c -> c.ship = biggest.get(0).ship);
                    biggest.addAll(command);
                }
            }
            for (List<FieldBlock> crew : byShip.values()) {
                rafasconquest.core.SeaLanes.Size hull = crew.get(0).duty == Duty.GENERAL || crew.get(0).duty == Duty.GUARD || crew.get(0).duty == Duty.BEARER
                        ? rafasconquest.core.SeaLanes.Size.battle("flagship", rafasconquest.core.SeaLanes.Size.XLARGE) : shipOf(crew);
                crew.sort(Comparator.comparingInt((FieldBlock b) -> b.duty == Duty.GENERAL ? 0 : 1).thenComparingInt(b -> b.id));
                for (int i = 0; i < crew.size(); i++) {
                    FieldBlock b = crew.get(i);
                    b.shipId = crew.get(0).id;
                    b.berth = i;
                    b.hull = hull;
                }
                crews.put(crew.get(0).id, crew);
            }
            if (type == BattleType.OCEAN) {
                formFleet(army, fleetsApart() / 2);
                for (List<FieldBlock> crew : byShip.values()) {
                    for (FieldBlock c : crew) { // everyone aboard has its ship's part, whoever sails it later
                        c.role = crew.get(0).role;
                        c.battleLine = crew.get(0).battleLine;
                        c.lineIndex = crew.get(0).lineIndex;
                    }
                }
            } else {
                spreadShips(army, 0);
            }
        }
        syncCrews(); // everyone aboard where their ship now lies
    }

    /** At sea, each ship's blocks by its id ({@link FieldBlock#shipId}), its first block first. */
    private final java.util.Map<Integer, List<FieldBlock>> crews = new java.util.HashMap<>();

    /** Every block aboard {@code b}'s ship, {@code b} among them (just {@code b} ashore). */
    public List<FieldBlock> crew(FieldBlock b) {
        List<FieldBlock> crew = crews.get(b.shipId);
        return crew == null ? List.of(b) : crew;
    }

    /**
     * The block whose captain sails {@code b}'s ship: its first block still standing and not running (else its first
     * standing). The ship's place, heading and orders are kept on it and copied to the rest aboard ({@link #syncCrews}).
     */
    public FieldBlock holder(FieldBlock b) {
        List<FieldBlock> crew = crews.get(b.shipId);
        if (crew == null) {
            return b;
        }
        FieldBlock standing = null;
        for (FieldBlock c : crew) {
            if (c.alive == 0) {
                continue;
            }
            if (c.duty != Duty.FLEEING && c.duty != Duty.ROUTED && c.duty != Duty.RALLYING) {
                return c;
            }
            if (standing == null) {
                standing = c;
            }
        }
        return standing != null ? standing : b;
    }

    /**
     * Who a block afloat fights hand to hand: one of the blocks aboard the enemy ships grappled to its own, each of its
     * ship's blocks a different one in turn (by berth), so a boarding unit spreads over the enemy's deck instead of all
     * falling on its first block; the general and the standard only once no one else aboard stands. Null, not grappled.
     */
    @Nullable
    private static FieldBlock boarded(FieldBlock block) {
        return block.lashed.stream().anyMatch(o -> o.alive > 0) ? spread(block, block.lashed) : null;
    }

    /**
     * Which of {@code among} (blocks aboard enemy ships, one standing at least) block {@code by} strikes: each block of
     * a unit a different one in turn, by its berth; the general and the standard only once no one else stands.
     */
    private static FieldBlock spread(FieldBlock by, List<FieldBlock> among) {
        List<FieldBlock> foes = among.stream().filter(o -> o.alive > 0 && o.duty != Duty.GENERAL && o.duty != Duty.BEARER).toList();
        if (foes.isEmpty()) {
            foes = among.stream().filter(o -> o.alive > 0).toList();
        }
        return foes.get(by.berth % foes.size());
    }

    /** Everyone aboard a ship lies where it does, as it does, and makes for where it does. */
    private void syncCrews() {
        for (List<FieldBlock> crew : crews.values()) {
            FieldBlock h = holder(crew.get(0));
            for (FieldBlock c : crew) {
                if (c == h || c.alive == 0 || !c.afloat || !h.afloat) {
                    continue;
                }
                c.pos = h.pos;
                c.facingX = h.facingX;
                c.facingZ = h.facingZ;
                c.settleX = h.settleX;
                c.settleZ = h.settleZ;
                c.line = h.line;
                c.deployed = h.deployed;
                c.prey = h.prey;
                c.charged = h.charged;
            }
        }
    }

    /** Soldiers stand on deck this far apart, and each block's section this many abreast. */
    public static final double DECK_SPACING = 1.2;
    public static final int DECK_FILES = 4;

    /**
     * Where soldier {@code i} of block {@code b} stands on its ship's deck, {right, back} of the ship's middle: each block
     * of the unit has its own section, two abreast on a unit's wide ship (one on a narrower), down its length; in its
     * section the block stands {@link #DECK_FILES} abreast, its front rank toward the bow.
     */
    public Point deckSpot(FieldBlock b, int i) {
        Point section = deckSection(b);
        if (b.started() == 1) {
            return section;
        }
        return new Point(section.x() + (i % DECK_FILES - (DECK_FILES - 1) / 2.0) * DECK_SPACING,
                section.z() + (i / DECK_FILES - (DECK_RANKS - 1) / 2.0) * DECK_SPACING);
    }

    /** A block's ranks in its deck section. */
    private static final int DECK_RANKS = (BattlePlan.SOLDIERS_PER_FORMATION + DECK_FILES - 1) / DECK_FILES;

    /** The most soldiers {@code hull}'s deck holds in whole block sections, laid out as {@link #deckSection} lays them. */
    public static int deckCrew(rafasconquest.core.SeaLanes.Size hull) {
        int abreast = hull.width >= rafasconquest.core.SeaLanes.Size.unitShip().width ? 2 : 1;
        int rows = Math.max(1, (int) (hull.length / (DECK_RANKS * DECK_SPACING)));
        return abreast * rows * BattlePlan.SOLDIERS_PER_FORMATION;
    }

    /** The middle of block {@code b}'s section of its ship's deck, {right, back} of the ship's middle. */
    public Point deckSection(FieldBlock b) {
        int crew = crew(b).size();
        int abreast = b.hull.width >= rafasconquest.core.SeaLanes.Size.unitShip().width ? 2 : 1;
        int rows = (crew + abreast - 1) / abreast;
        return new Point((b.berth % abreast - (abreast - 1) / 2.0) * (DECK_FILES + 0.3) * DECK_SPACING,
                (b.berth / abreast - (rows - 1) / 2.0) * DECK_RANKS * DECK_SPACING);
    }

    /**
     * Gives an army's siege train its engines, once its battle's type is set and before it begins: its general picks a
     * kind for each unit from those {@code allowed} ({@link Machine#allowed}; side 1 defends a siege or an amphibious
     * assault). With none allowed (at sea) the crews have nothing to shoot and fight as light troops.
     */
    public void equip(int side, List<Machine> allowed) {
        if (begun()) {
            throw new IllegalStateException("the battle has begun");
        }
        trains.put(side, allowed);
        java.util.Map<Integer, Machine> byUnit = new java.util.HashMap<>();
        for (FieldBlock b : armies[side].blocks) {
            if (b.arm.engine()) {
                // An engine's own arm (a grand strategy army's card) keeps its engine; the siege train's units get one picked.
                b.machine = allowed.isEmpty() ? null : b.machine == null && b.arm.machine() != null ? b.arm.machine()
                        : byUnit.computeIfAbsent(b.platoon, p -> allowed.get(random.nextInt(allowed.size())));
                b.arm = b.machine == null ? Arm.ENGINES : Arm.of(b.machine); // a shooting engine's crews are its own unit
                b.arrows = b.machine == null || !b.machine.shoots() ? 0 : (int) Math.round(b.machine.perBlock() * Machine.SHOTS_EACH);
            }
        }
    }

    /** A fleet deploys in one line abreast, its ships this far apart, centre to centre. */
    public static double fleetSpacing() {
        return 2.0 * rafasconquest.core.SeaLanes.Size.unitShip().width;
    }

    /**
     * A fleet's line stands this far behind its army's front, half a unit's ship, so the two fleets' bows start as far apart as
     * the armies' fronts would (well out of grapple-hook reach).
     */
    public static double fleetBack() {
        return rafasconquest.core.SeaLanes.Size.unitShip().length / 2.0;
    }

    /**
     * In a sea battle the two fleets start this much further apart than the armies' fronts would: their fronts twice as
     * far apart as they once were (the armies' fronts, two half unit ships and 100 blocks more).
     */
    public static double fleetsApart() {
        double once = 2 * BattlePlan.DEPLOY_DISTANCE + 2 * fleetBack() + 100;
        return 2 * once - 2 * BattlePlan.DEPLOY_DISTANCE - 2 * fleetBack();
    }

    /** The ship a unit's crew sails: the one its first block's card names, else a unit's ship. */
    private static rafasconquest.core.SeaLanes.Size shipOf(List<FieldBlock> crew) {
        rafasconquest.core.SeaLanes.Size named = crew.get(0).arm.ship();
        return named != null ? named : rafasconquest.core.SeaLanes.Size.unitShip();
    }

    /** Whether {@code b} sails the admiral's ship. */
    private static boolean flagship(FieldArmy army, FieldBlock b) {
        return army.commander != null && b.shipId == army.commander.shipId;
    }

    /** A fleet's ships, each by the block sailing it, in the order they stood from left to right. */
    private static List<FieldBlock> fleet(FieldArmy army) {
        return army.blocks.stream().filter(o -> o.afloat && o.shipId == o.id)
                .sorted(Comparator.comparingDouble(b -> BattlePlan.offset(army.origin, army.facingX, army.facingZ, b.pos).x()))
                .toList();
    }

    /**
     * A fleet deploys in one line abreast across its army's front, {@link #fleetSpacing} apart, its ships in the order
     * they stood from left to right, {@code extra} further back than {@link #fleetBack}.
     */
    private static void spreadShips(FieldArmy army, double extra) {
        lineAbreast(army, fleet(army), fleetBack() + extra);
    }

    /**
     * A fleet at sea deploys by what drives most of its ships, {@code extra} further back than {@link #fleetBack}: a galley
     * fleet (more oared ships than not) in {@link #rows} to ram and board, a sail or steam fleet in {@link #battleLines}.
     */
    private static void formFleet(FieldArmy army, double extra) {
        List<FieldBlock> fleet = fleet(army);
        if (!galleys(fleet)) {
            battleLines(army, fleet, fleetBack() + extra);
        } else {
            rows(army, fleet, fleetBack() + extra);
        }
    }

    /** Whether more of {@code fleet}'s ships are rowed than not (a tie fights in battle lines). */
    static boolean galleys(List<FieldBlock> fleet) {
        long oars = fleet.stream().filter(b -> b.hull.propulsion == rafasconquest.core.SeaLanes.Propulsion.OARS).count();
        return oars * 2 > fleet.size();
    }

    /**
     * Room across between two ships' middles in a fleet's formation: three of its widest ships and a little, so two ships
     * boarding the facing sides of two neighbours never make for the same water between them.
     */
    private static double formationBeam(List<FieldBlock> fleet) {
        return 3.0 * fleet.stream().mapToInt(b -> b.hull.width).max().orElse(rafasconquest.core.SeaLanes.Size.unitShip().width) + 3;
    }

    /** Room fore and aft between two ships' middles in a fleet's formation: its longest ship and ten blocks. */
    private static double formationLength(List<FieldBlock> fleet) {
        return fleet.stream().mapToInt(b -> b.hull.length).max().orElse(rafasconquest.core.SeaLanes.Size.unitShip().length) + 10;
    }

    /**
     * A Steel fleet deploys as a rectangle of rows abreast, a row four times as long as the rectangle is deep (16 ships: two
     * rows of eight): boarding ships to the front, archers and shock ships behind, the admiral in the middle of the back row.
     */
    private static void rows(FieldArmy army, List<FieldBlock> fleet, double back) {
        List<FieldBlock> order = new ArrayList<>(fleet.stream().filter(b -> !flagship(army, b))
                .sorted(Comparator.comparingInt(b -> b.ship == FieldBlock.ShipKind.SWORDSMEN ? 0 : b.ship == FieldBlock.ShipKind.ARCHERS ? 1 : 2))
                .toList());
        int n = fleet.size();
        int rows = Math.max(1, (int) Math.round(Math.sqrt(n / 4.0)));
        int cols = (n + rows - 1) / rows;
        rows = (n + cols - 1) / cols;
        int lastRow = (rows - 1) * cols;
        fleet.stream().filter(b -> flagship(army, b)).forEach(a -> order.add(Math.min(order.size(), lastRow + (n - lastRow) / 2), a));
        double beam = formationBeam(fleet);
        double length = formationLength(fleet);
        int behind = 0;
        for (int i = 0; i < order.size(); i++) {
            int row = i / cols;
            int inRow = row == rows - 1 ? n - lastRow : cols;
            FieldBlock b = order.get(i);
            place(army, b, (i % cols - (inRow - 1) / 2.0) * beam, back + row * length);
            // The back rows: every other ship follows the front to help; the rest, and the admiral, wait.
            b.role = row == 0 ? FieldBlock.FleetRole.FRONT
                    : !flagship(army, b) && behind++ % 2 == 0 ? FieldBlock.FleetRole.SUPPORT : FieldBlock.FleetRole.RESERVE;
        }
    }

    /**
     * A Gunpowder fleet deploys its gunships in battle lines of three to nine, each in line ahead, bows to the enemy, the
     * lines abreast; the admiral's ship in the middle of the middle line; its smaller ships in a screen abreast ahead of them.
     */
    private static void battleLines(FieldArmy army, List<FieldBlock> fleet, double back) {
        List<FieldBlock> screen = fleet.stream().filter(b -> !flagship(army, b) && b.ship != FieldBlock.ShipKind.GUNSHIP).toList();
        List<FieldBlock> guns = new ArrayList<>(fleet.stream().filter(b -> !flagship(army, b) && b.ship == FieldBlock.ShipKind.GUNSHIP).toList());
        int g = guns.size() + (int) fleet.stream().filter(b -> flagship(army, b)).count();
        int lines = Math.max(1, Math.min((g + 8) / 9, g / 3));
        int[] sizes = new int[lines];
        for (int k = 0; k < lines; k++) {
            sizes[k] = g / lines + (k < g % lines ? 1 : 0);
        }
        int middle = lines / 2;
        int before = java.util.Arrays.stream(sizes, 0, middle).sum();
        fleet.stream().filter(b -> flagship(army, b)).forEach(a -> guns.add(Math.min(guns.size(), before + sizes[middle] / 2), a));
        double beam = formationBeam(fleet);
        double length = formationLength(fleet);
        boolean screened = !screen.isEmpty();
        for (int i = 0; i < screen.size(); i++) {
            place(army, screen.get(i), (i - (screen.size() - 1) / 2.0) * beam, back);
            screen.get(i).role = FieldBlock.FleetRole.SCREEN;
        }
        for (int k = 0, i = 0; k < lines; k++) {
            for (int j = 0; j < sizes[k]; j++, i++) {
                FieldBlock b = guns.get(i);
                place(army, b, (k - (lines - 1) / 2.0) * 2 * beam, back + ((screened ? 1 : 0) + j) * length);
                b.role = FieldBlock.FleetRole.LINE;
                b.battleLine = k;
                b.lineIndex = j;
            }
        }
    }

    /** Ship {@code b} deployed {@code right} of the army's middle and {@code back} behind its front, bow to the enemy. */
    private static void place(FieldArmy army, FieldBlock b, double right, double back) {
        b.pos = BattlePlan.place(army.origin, army.facingX, army.facingZ, right, back);
        b.line = b.pos;
        b.deployed = b.pos;
        b.facingX = army.facingX;
        b.facingZ = army.facingZ;
        b.settleX = army.facingX;
        b.settleZ = army.facingZ;
    }

    /** Ships in one line abreast, centred on the army's front, {@code back} behind it. */
    private static void lineAbreast(FieldArmy army, List<FieldBlock> ships, double back) {
        for (int i = 0; i < ships.size(); i++) {
            place(army, ships.get(i), (i - (ships.size() - 1) / 2.0) * fleetSpacing(), back);
        }
    }

    /** Ship {@code b} drops astern until it is clear of every one of {@code others}. */
    private static void dropClear(FieldArmy army, FieldBlock b, List<FieldBlock> others) {
        for (boolean fouled = true; fouled; ) {
            fouled = false;
            for (FieldBlock p : others) {
                double gap = (p.pos.x() - b.pos.x()) * army.facingX + (p.pos.z() - b.pos.z()) * army.facingZ;
                double across = (b.pos.x() - p.pos.x()) * -army.facingZ + (b.pos.z() - p.pos.z()) * army.facingX;
                // The slack: a ship dropped exactly a length astern mustn't read as a hair short and be dropped again forever.
                if (p != b && Math.abs(across) < shipBeam() && Math.abs(gap) < shipLength() - 1e-6) {
                    b.pos = b.pos.plus(-army.facingX * (shipLength() - gap), -army.facingZ * (shipLength() - gap));
                    fouled = true;
                }
            }
        }
        b.line = b.pos;
        b.deployed = b.pos;
    }

    /** The room a battle ship needs abeam: its beam (a unit's ship, the widest) and a few blocks clear. */
    static double shipBeam() {
        return rafasconquest.core.SeaLanes.Size.unitShip().width + 3;
    }

    /** A battle ship's length, the room it needs fore and aft (a unit's ship, the longest, and a little room). */
    public static double shipLength() {
        return rafasconquest.core.SeaLanes.Size.unitShip().length + 2;
    }
    /** Shock ships sail this much faster than the others. */
    static final double SHOCK_PACE = 1.6;
    /**
     * Archer ships keep this far from the nearest enemy ship (in bow range, but clear of a unit's ship's length either
     * way); shock ships waiting for their chance, a little further.
     */
    static final double ARCHER_STANDOFF = FieldRules.ARCHER_RANGE * 0.9;
    static final double SHOCK_STANDOFF = 48;
    /** A shock ship comes about this far further off an engaged enemy's side than where it strikes, before it turns in. */
    static final double RAM_RUN = 6;
    /** Shock ships hold back this many seconds after the battle begins, whatever chance they see. */
    static final int SHOCK_WAIT = 20;
    /** A boarding ship's crew throw their grapple hooks once its hull is this close to its enemy's; every throw holds. */
    public static final double GRAPPLE_RANGE = 20;
    /** Hooked, the lines haul a ship that is only numbers this many blocks a second toward its place alongside, sideways too. */
    public static final double HAUL_SPEED = 2;
    /** Hulls closer than this, side to side, touch: the ships grapple and the crews board. */
    public static final double TOUCH_GAP = 1;

    /** A boarding ship lays itself alongside its enemy this far off, centre to centre: two half beams and half a block between. */
    /** Hooks go over only with the two ships this near parallel, bows the same way or opposite (radians)... */
    public static final double GRAPPLE_PARALLEL = Math.toRadians(15);
    /** ...and lying side by side along more than this share of the shorter hull's length. */
    public static final double GRAPPLE_ALONGSIDE = 0.6;

    /** Whether {@code b}'s crew may throw their hooks at {@code e}: the ships parallel and overlapping along their length. */
    public static boolean hookable(FieldBlock b, FieldBlock e) {
        double lb = Math.max(1e-6, Math.hypot(b.facingX, b.facingZ));
        double le = Math.max(1e-6, Math.hypot(e.facingX, e.facingZ));
        double fx = b.facingX / lb;
        double fz = b.facingZ / lb;
        if (Math.abs(fx * e.facingX / le + fz * e.facingZ / le) < Math.cos(GRAPPLE_PARALLEL)) {
            return false;
        }
        double along = (e.pos.x() - b.pos.x()) * fx + (e.pos.z() - b.pos.z()) * fz;
        double hb = b.hull.length / 2.0;
        double he = e.hull.length / 2.0;
        double overlap = Math.min(hb, along + he) - Math.max(-hb, along - he);
        return overlap > GRAPPLE_ALONGSIDE * 2 * Math.min(hb, he);
    }

    static double boardAbeam(FieldBlock b, FieldBlock e) {
        return (b.hull.width + e.hull.width) / 2.0 + TOUCH_GAP / 2;
    }

    /** Whether {@code at} is on (or within a couple of blocks of) a ship's hull lying at {@code centre}, bow along (fx, fz). */
    public static boolean onHull(Point at, Point centre, double fx, double fz, rafasconquest.core.SeaLanes.Size hull) {
        double ox = at.x() - centre.x();
        double oz = at.z() - centre.z();
        return Math.abs(ox * fx + oz * fz) <= hull.length / 2.0 + 2 && Math.abs(-ox * fz + oz * fx) <= hull.width / 2.0 + 2;
    }

    /** A hooked ship that is only numbers is hauled toward its place alongside, any way the lines pull it. */
    private static void haul(FieldBlock b) {
        double d = b.pos.distanceTo(b.line);
        double step = Math.min(d, HAUL_SPEED);
        if (d > 1e-9) {
            b.pos = b.pos.plus((b.line.x() - b.pos.x()) / d * step, (b.line.z() - b.pos.z()) / d * step);
        }
    }

    /** Whether {@code b}'s hull lies alongside {@code e}'s, less than {@link #TOUCH_GAP} off its side. */
    static boolean touching(FieldBlock b, FieldBlock e) {
        double ox = b.pos.x() - e.pos.x();
        double oz = b.pos.z() - e.pos.z();
        double along = Math.abs(ox * e.facingX + oz * e.facingZ);
        double across = Math.abs(-ox * e.facingZ + oz * e.facingX);
        return along <= e.hull.length / 2.0 && across - (b.hull.width + e.hull.width) / 2.0 < TOUCH_GAP;
    }

    /** A rammer's middle is this far out from the side of the ship it rammed, its bow against it: half a beam and half a length. */
    static double ramContact(FieldBlock rammer, FieldBlock e) {
        return (e.hull.width + rammer.hull.length) / 2.0;
    }

    /** A shock ship rams an enemy's side within this much of its middle, fore or aft: into its hull, short of its ends. */
    static double ramAbreast(FieldBlock e) {
        return e.hull.length / 2.0 - 2;
    }

    /** A gunship keeps this share of its shortest gun's reach from its mark. */
    static final double GUN_STANDOFF = 0.8;

    /** How far off its mark a gunship lies: within its shortest gun's reach (bow range with no guns). */
    static double gunRange(FieldBlock b) {
        double reach = java.util.stream.Stream.concat(b.hull.gunsRight.stream(), b.hull.gunsLeft.stream())
                .map(Machine::byId).filter(java.util.Objects::nonNull).mapToDouble(m -> m.reach).min().orElse(0);
        return reach > 0 ? reach * GUN_STANDOFF : ARCHER_STANDOFF;
    }

    /**
     * A gunship that is only numbers fires a broadside from each side: the side its mark (the ship a player sent it at,
     * else its captain's) lies on fires only at that ship, once it is abeam (within 45 degrees of the beam); the other
     * side fires at will, at the nearest enemy ship abeam of it. Each gun in reach fires once a reload, at random; its
     * shot falls on the struck ship's unit as arrows do. A real gunship's gunners aim the same way (RecruitsBattle), and
     * shoot real crews themselves: its numbers fire only at a ship with nobody real aboard, as its gunners fire at its hull.
     * shortcut: guns fire while any of the unit lives, whoever stands at them; scale by gunners left if that matters.
     */
    void broadside(FieldBlock b, FieldArmy enemy, List<FieldBlock> targets, List<Double> damages) {
        boolean real = crew(b).stream().anyMatch(c -> c.live);
        for (int side : new int[]{1, -1}) {
            List<String> guns = side > 0 ? b.hull.gunsRight : b.hull.gunsLeft;
            FieldBlock foe = guns.isEmpty() ? null : broadsideFoe(b, enemy, side);
            if (foe == null || real && crew(foe).stream().anyMatch(c -> c.live)) {
                continue;
            }
            double d = b.pos.distanceTo(foe.pos);
            double damage = 0;
            List<Machine> fired = new ArrayList<>();
            for (String id : guns) {
                Machine m = Machine.byId(id);
                if (m != null && d <= m.reach && random.nextDouble() < 1 / Math.max(1, m.reload)) {
                    damage += m.damage;
                    fired.add(m);
                }
            }
            if (damage > 0) {
                FieldBlock hit = spread(b, crew(foe));
                targets.add(hit);
                damages.add(damage * hit.arm.takes());
                if (!real) { // drawn by clients, ball, smoke and flash; a real gunship's gunners fire real guns
                    fired.forEach(m -> volleys.add(new Volley(b, hit, 1, m)));
                }
            }
        }
    }

    /**
     * The enemy ship gunship {@code b}'s {@code side} guns bear on: on the side its mark lies (the ship a player sent it
     * at, else its captain's), that ship once abeam; on the other, the nearest enemy ship abeam; null for none.
     */
    public FieldBlock broadsideFoe(FieldBlock b, FieldArmy enemy, int side) {
        FieldBlock mark = b.prey != null && b.prey.alive > 0 && b.prey.afloat && b.prey.side != b.side ? b.prey : null;
        if (mark != null && gunSide(b, mark.pos) == side) {
            return abeam(b, mark.pos, side) ? mark : null;
        }
        FieldBlock foe = null;
        double best = Double.MAX_VALUE;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.afloat && e == holder(e) && abeam(b, e.pos, side) && b.pos.distanceTo(e.pos) < best) {
                best = b.pos.distanceTo(e.pos);
                foe = e;
            }
        }
        return foe;
    }

    /** Which of ship {@code b}'s sides a place lies on: 1 its right (seen from astern), -1 its left. */
    public static int gunSide(FieldBlock b, Point at) {
        return (at.x() - b.pos.x()) * -b.facingZ + (at.z() - b.pos.z()) * b.facingX >= 0 ? 1 : -1;
    }

    /** Whether {@code at} lies off ship {@code b}'s {@code side}, within 45 degrees of its beam: where that side's guns bear. */
    static boolean abeam(FieldBlock b, Point at, int side) {
        double dx = at.x() - b.pos.x();
        double dz = at.z() - b.pos.z();
        double right = dx * -b.facingZ + dz * b.facingX;
        return right * side > 0 && Math.abs(dx * b.facingX + dz * b.facingZ) <= Math.abs(right);
    }

    /** How many blocks a second a formation's ship sails. */
    public static double shipSpeed(FieldBlock block) {
        return (block.ship == FieldBlock.ShipKind.SHOCK ? SHIP_SPEED * SHOCK_PACE : SHIP_SPEED) * block.hull.speed;
    }

    /** Two enemy ships grapple: their soldiers may now board each other's decks (and only now, at sea, fight hand to hand). */
    public void lash(FieldBlock a, FieldBlock b) {
        if (a.side == b.side) {
            return;
        }
        for (FieldBlock x : crew(a)) { // ship to ship: everyone aboard the one may board the other
            for (FieldBlock y : crew(b)) {
                if (x.afloat && y.afloat && !x.lashed.contains(y)) {
                    x.lashed.add(y);
                    y.lashed.add(x);
                }
            }
        }
    }

    /**
     * At sea, each ship's captain decides where it sails, by what it is for: a grappled ship holds fast while its
     * soldiers board; a swordsman ship lays itself alongside an enemy ship, side to side, and grapples; an archer ship
     * keeps at bow range behind its allies; a shock ship holds back a while, and until an enemy is grappled with one of
     * its own, then comes round off that enemy's open side and rams it there, like cavalry into a flank; the general's
     * ship keeps where it deployed. Shock ships that are only numbers grapple when they meet; real ones grapple when
     * they really ram ({@link #lash}).
     * <p>
     * An enemy ship has two sides to come alongside or ram, and each takes one ship of ours, so ours never make for the
     * same water and end up in each other: a ship takes a free side of the enemy it went for last, else of the nearest
     * with one free (shock ships choose first); with every side taken, it waits clear of the fight.
     */
    private void captains() {
        for (int side = 0; side < 2; side++) {
            FieldArmy enemy = armies[1 - side];
            java.util.Set<Long> taken = new java.util.HashSet<>();
            List<Point> archerSpots = new ArrayList<>(); // where our archer ships already make for
            for (FieldBlock e : enemy.blocks) {
                for (FieldBlock o : e.lashed) {
                    taken.add(sideKey(e, sideOf(e, o.pos))); // grappled there already
                }
            }
            // Ships, each by the block sailing it.
            List<FieldBlock> boardable = enemy.blocks.stream().filter(e -> e.alive > 0 && e.afloat && e == holder(e)).toList();
            List<FieldBlock> engaged = seconds < SHOCK_WAIT ? List.of()
                    : enemy.blocks.stream().filter(e -> e.alive > 0 && e.afloat && !e.lashed.isEmpty() && e == holder(e)).toList();
            // Ours the enemy's grapple hooks are in: the lines hold them while they haul.
            java.util.Set<FieldBlock> held = new java.util.HashSet<>();
            for (FieldBlock e : enemy.blocks) {
                if (e.hookedTo != null && e.alive > 0 && e.lashed.isEmpty()) {
                    held.add(holder(e.hookedTo));
                }
            }
            // Fleet tactics: the enemy ships fighting ours, those beating one of ours, how far our front has come, whether
            // it is half gone (then everyone goes in), and each battle line's ships, leader first.
            List<FieldBlock> helping = boardable.stream().filter(e -> !e.lashed.isEmpty()).toList();
            List<FieldBlock> losing = helping.stream().filter(e -> e.lashed.stream()
                    .anyMatch(o -> crewAlive(o) < o.lashed.stream().mapToInt(this::crewAlive).sum())).toList();
            Point advance = frontAdvance(armies[side]);
            boolean allIn = frontHalfGone(armies[side]);
            java.util.Map<Integer, List<FieldBlock>> lines = new java.util.HashMap<>();
            armies[side].blocks.stream().filter(b -> b.role == FieldBlock.FleetRole.LINE && b.alive > 0 && b.afloat && b == holder(b))
                    .sorted(Comparator.comparingInt(b -> b.lineIndex))
                    .forEach(b -> lines.computeIfAbsent(b.battleLine, k -> new ArrayList<>()).add(b));
            List<FieldBlock> order = new ArrayList<>(armies[side].blocks);
            // The front claims its enemies first, then those helping it, then the reserve; shock ships first in each.
            order.sort(Comparator.comparingInt((FieldBlock b) -> b.role == FieldBlock.FleetRole.SUPPORT ? 1 : b.role == FieldBlock.FleetRole.RESERVE ? 2 : 0)
                    .thenComparingInt(b -> b.ship == FieldBlock.ShipKind.SHOCK ? 0 : 1));
            for (FieldBlock b : order) {
                FieldBlock previous = b.prey;
                b.lashed.removeIf(o -> o.alive == 0 || !o.afloat);
                if (!b.afloat || b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED
                        || b.duty == Duty.RALLYING || b != holder(b)) {
                    continue; // the rest aboard go where their ship does (syncCrews)
                }
                FieldBlock hooked = b.hookedTo;
                if (b.lashed.isEmpty()) {
                    b.hookedTo = null; // its lines stay out while they haul (below), and once grappled
                }
                if (held.contains(b) && b.lashed.isEmpty()) {
                    b.charged = false;
                    b.line = b.pos; // hooked by an enemy: held where it lies while their lines haul them in
                    continue;
                }
                FieldBlock.ShipKind kind = b.ship;
                FieldBlock ordered = null; // the enemy ship a player sent it at
                if (b.led) {
                    if (!b.ledCharge) {
                        // An officer's ship sails where they sent it, and lies as they said.
                        b.charged = false;
                        b.prey = null;
                        b.line = b.lashed.isEmpty() && b.ledLine != null ? b.ledLine : b.pos;
                        b.settleX = b.ledFacingX;
                        b.settleZ = b.ledFacingZ;
                        continue;
                    }
                    if (b.ledTarget != null && b.ledTarget.alive > 0) {
                        previous = holder(b.ledTarget); // sent at a ship: its captain goes for that one first
                        if (b.ledTarget.afloat && b.ship != null) {
                            // The player's order, whatever the ship is for: board it abreast, or ram it if told to.
                            ordered = previous;
                            kind = b.ship == FieldBlock.ShipKind.GUNSHIP ? FieldBlock.ShipKind.GUNSHIP // sent at a ship: it engages it with its guns
                                    : b.ledRam ? FieldBlock.ShipKind.SHOCK : FieldBlock.ShipKind.SWORDSMEN;
                        }
                    }
                }
                b.strikeAt = null;
                b.approaching = false;
                b.waypoint = null;
                b.charged = false;
                if (!b.lashed.isEmpty()) {
                    b.prey = b.lashed.get(0);
                    b.line = b.pos;
                    continue;
                }
                b.prey = null;
                if (ordered == null && b.role == FieldBlock.FleetRole.LINE) {
                    sailInLine(b, lines.getOrDefault(b.battleLine, List.of(b)), enemy); // the admiral's ship too
                    continue;
                }
                if (b.ship == null) {
                    b.line = b.deployed != null ? b.deployed : b.pos; // the general's ship keeps its place
                    continue;
                }
                FieldBlock foe = nearestShip(b, enemy);
                if (foe == null) {
                    b.line = b.pos;
                    continue;
                }
                List<FieldBlock> among = boardable; // the enemy ships it may board
                List<FieldBlock> rammable = engaged; // and ram
                if (ordered == null && b.role == FieldBlock.FleetRole.SCREEN) {
                    kind = FieldBlock.ShipKind.ARCHERS; // a Gunpowder fleet's smaller ships keep the enemy busy at range
                } else if (ordered == null && !allIn && (b.role == FieldBlock.FleetRole.SUPPORT || b.role == FieldBlock.FleetRole.RESERVE)) {
                    // Support joins any fight of ours, the reserve only one we are losing; till then they follow the front, or wait.
                    List<FieldBlock> need = b.role == FieldBlock.FleetRole.SUPPORT ? helping : losing;
                    if (need.isEmpty()) {
                        Point home = b.deployed != null ? b.deployed : b.pos;
                        b.line = b.role == FieldBlock.FleetRole.SUPPORT ? home.plus(advance.x(), advance.z()) : home;
                        continue;
                    }
                    among = need;
                    rammable = need;
                    foe = need.stream().min(Comparator.comparingDouble(e -> e.pos.distanceTo(b.pos))).orElse(foe);
                }
                switch (kind) {
                    case ARCHERS -> {
                        // An archer ship behind another, both on the line from one foe, keeps a ship's length further off.
                        double keep = ARCHER_STANDOFF;
                        for (b.line = standOff(b, foe, keep); clash(b.line, archerSpots); b.line = standOff(b, foe, keep)) {
                            keep += shipLength();
                        }
                        archerSpots.add(b.line);
                        b.prey = mark(b, enemy, previous);
                    }
                    case GUNSHIP -> {
                        // Sails round its mark at gun range, under way, with one side to it: the side that faced it
                        // when it first engaged, kept, so it bombards with that broadside as it goes and never comes
                        // about to show the other.
                        FieldBlock e = ordered != null ? ordered
                                : previous != null && previous.side != b.side && previous.alive > 0 && previous.afloat ? previous : foe;
                        b.prey = e;
                        double d = Math.max(1e-6, b.pos.distanceTo(e.pos));
                        double ux = (e.pos.x() - b.pos.x()) / d;
                        double uz = (e.pos.z() - b.pos.z()) / d;
                        if (b.gunSide == 0) {
                            b.gunSide = ux * -b.facingZ + uz * b.facingX >= 0 ? 1 : -1; // its mark off its (-z, x) side (1) or the other
                        }
                        b.settleX = b.gunSide * uz; // the heading whose (-z, x) side, times gunSide, points at the mark
                        b.settleZ = -b.gunSide * ux;
                        b.line = circling(e.pos, -ux, -uz, b.settleX, b.settleZ, gunRange(b));
                    }
                    case SWORDSMEN -> {
                        Target t = claim(b, ordered != null ? List.of(ordered) : among, previous, taken);
                        if (t == null) {
                            b.line = standOff(b, foe, SHOCK_STANDOFF); // every side taken: wait clear of the fight
                            break;
                        }
                        // Alongside, side to side, its bow the way the enemy's points (or the other way, whichever is nearer).
                        FieldBlock e = t.ship();
                        b.prey = e;
                        b.line = e.pos.plus(t.side().x() * boardAbeam(b, e), t.side().z() * boardAbeam(b, e));
                        double same = b.facingX * e.facingX + b.facingZ * e.facingZ < 0 ? -1 : 1;
                        b.settleX = same * e.facingX;
                        b.settleZ = same * e.facingZ;
                        if (hooked == e || Ramming.overlap(footprint(b), footprint(e), GRAPPLE_RANGE / 2) && hookable(b, e)) {
                            b.hookedTo = e; // in reach and lying alongside: grapple hooks over, and the lines haul the two together
                            if (!b.live) {
                                haul(b); // a real ship is hauled on the water (BattleShips)
                            }
                            if (touching(b, e)) {
                                lash(b, e); // hull to hull: they board
                            }
                        }
                    }
                    case SHOCK -> {
                        Target t = claim(b, ordered != null ? List.of(ordered) : rammable, previous, taken);
                        if (t == null) {
                            b.line = standOff(b, foe, SHOCK_STANDOFF);
                            break;
                        }
                        // Abreast of its side, turn in and ram.
                        FieldBlock e = t.ship();
                        Point n = t.side();
                        b.prey = e;
                        double ox = b.pos.x() - e.pos.x();
                        double oz = b.pos.z() - e.pos.z();
                        double along = ox * e.facingX + oz * e.facingZ;
                        double across = ox * n.x() + oz * n.z();
                        // Off its side, within its length, and with room to gather way (not in its side already, its last
                        // enemy sunk under it): else it stands out to its offing first.
                        if (Math.abs(along) < ramAbreast(e) && across > ramContact(b, e) - shipSpeed(b)) {
                            b.charged = true;
                            if (across <= ramContact(b, e) + 1 && !(b.live && e.live)) {
                                lash(b, e); // real ships grapple when they really ram
                                // Its bow against the enemy's side where it struck, not in it, so their real ships are built clear.
                                double cx = e.facingX * along + n.x() * ramContact(b, e);
                                double cz = e.facingZ * along + n.z() * ramContact(b, e);
                                if (!b.live) {
                                    b.pos = e.pos.plus(cx, cz);
                                    b.line = b.pos;
                                } else if (!e.live) {
                                    e.pos = b.pos.plus(-cx, -cz);
                                    e.line = e.pos;
                                }
                            }
                        } else {
                            b.line = e.pos.plus(n.x() * (ramContact(b, e) + RAM_RUN), n.z() * (ramContact(b, e) + RAM_RUN));
                            b.settleX = -n.x(); // comes about bow-in, ready to ram
                            b.settleZ = -n.z();
                        }
                    }
                }
            }
        }
    }

    /**
     * What an archer ship's captain has it shoot at: any enemy ship in bow range. It keeps to the one it shot at last
     * while that is in range, unless an enemy grappled with one of ours comes in range (it shoots into the boarding
     * fight); else the nearest in range, a grappled one first. Null with none in range.
     */
    @Nullable
    static FieldBlock mark(FieldBlock b, FieldArmy enemy, @Nullable FieldBlock previous) {
        FieldBlock nearest = null;
        FieldBlock grappled = null;
        for (FieldBlock e : enemy.blocks) {
            double d = b.pos.distanceTo(e.pos);
            if (e.alive == 0 || !e.afloat || d > FieldRules.ARCHER_RANGE) {
                continue;
            }
            if (nearest == null || d < b.pos.distanceTo(nearest.pos)) {
                nearest = e;
            }
            if (!e.lashed.isEmpty() && (grappled == null || d < b.pos.distanceTo(grappled.pos))) {
                grappled = e;
            }
        }
        boolean keep = previous != null && previous.side != b.side && previous.alive > 0 && previous.afloat
                && b.pos.distanceTo(previous.pos) <= FieldRules.ARCHER_RANGE && (grappled == null || !previous.lashed.isEmpty());
        return keep ? previous : grappled != null ? grappled : nearest;
    }

    /** An enemy ship and the unit normal out of the side of it a ship of ours has taken. */
    private record Target(FieldBlock ship, Point side) {
    }

    /**
     * Takes a free side of one of {@code among} for ship {@code b}: of the one it went for last if it has one, else of
     * the nearest with one; of its two sides, the one facing {@code b} first. Null if every side is taken.
     */
    @Nullable
    private static Target claim(FieldBlock b, List<FieldBlock> among, @Nullable FieldBlock previous, java.util.Set<Long> taken) {
        List<FieldBlock> order = new ArrayList<>(among);
        order.sort(Comparator.comparingDouble(e -> e == previous ? -1 : b.pos.distanceTo(e.pos)));
        for (FieldBlock e : order) {
            int facing = sideOf(e, b.pos);
            for (int s : new int[]{facing, -facing}) {
                if (taken.add(sideKey(e, s))) {
                    return new Target(e, new Point(-e.facingZ * s, e.facingX * s));
                }
            }
        }
        return null;
    }

    /** Which side of {@code ship} a place lies on: 1 its right (seen from astern), -1 its left. */
    private static int sideOf(FieldBlock ship, Point at) {
        return (at.x() - ship.pos.x()) * -ship.facingZ + (at.z() - ship.pos.z()) * ship.facingX >= 0 ? 1 : -1;
    }

    private static long sideKey(FieldBlock ship, int side) {
        return ship.shipId * 2L + (side > 0 ? 1 : 0);
    }

    /**
     * Ships that are only numbers are edged apart this far a second at most, where they would foul a ship of their own
     * side: as fast as the fastest ship sails, so two making for one place still end up side by side, not in each other.
     */
    public static final double SHIP_SHOVE = SHIP_SPEED * SHOCK_PACE;

    /**
     * Ships of one side whose hulls would foul each other edge apart, those that are only numbers (a real ship keeps
     * clear of its own side in the water itself, and a real formation's place is where its soldiers stand). A ship
     * can't be pushed sideways: each goes on, or drops back, along its own heading, the one further ahead of the other
     * going on (side by side, the first of them).
     */
    private void keepShipsApart() {
        for (FieldArmy army : armies) {
            List<FieldBlock> fleet = army.blocks.stream().filter(b -> b.afloat && b.alive > 0 && b == holder(b)).toList();
            double[] shove = new double[fleet.size()];
            for (int i = 0; i < fleet.size(); i++) {
                for (int j = i + 1; j < fleet.size(); j++) {
                    FieldBlock a = fleet.get(i);
                    FieldBlock b = fleet.get(j);
                    if (a.live && b.live || !Ramming.overlap(footprint(a), footprint(b), 1)) {
                        continue;
                    }
                    double ax = a.pos.x() - b.pos.x();
                    double az = a.pos.z() - b.pos.z();
                    double aAhead = ax * a.facingX + az * a.facingZ;
                    double bAhead = -ax * b.facingX - az * b.facingZ;
                    shove[i] += Math.abs(aAhead) > 1e-6 ? Math.signum(aAhead) : 1;
                    shove[j] += Math.abs(bAhead) > 1e-6 ? Math.signum(bAhead) : -1;
                }
            }
            for (int i = 0; i < fleet.size(); i++) {
                FieldBlock b = fleet.get(i);
                if (shove[i] != 0 && !b.live) {
                    double s = Math.signum(shove[i]) * SHIP_SHOVE;
                    b.pos = b.pos.plus(b.facingX * s, b.facingZ * s);
                }
            }
        }
    }

    /** A formation's ship's footprint where it is, bow along its facing: the general's a size up, as the real ones are. */
    private static Ramming.Hull footprint(FieldBlock b) {
        rafasconquest.core.SeaLanes.Size size = b.hull; // the general's a size up, a unit's the biggest
        return new Ramming.Hull(b.pos.x(), b.pos.z(), b.facingX, b.facingZ, size.length / 2.0, size.width / 2.0, 0, 0);
    }

    /** How far ahead round its mark a gunship makes for, radians: never reached, so it keeps under way, its mark abeam. */
    static final double CIRCLE_LEAD = Math.toRadians(35);

    /**
     * The point {@code keep} from {@code centre}, {@link #CIRCLE_LEAD} on round from the radial ({@code rx, rz}, centre
     * to ship) toward the heading ({@code tx, tz}, at right angles to it): sailing for it, a ship circles {@code centre}.
     */
    static Point circling(Point centre, double rx, double rz, double tx, double tz, double keep) {
        double c = Math.cos(CIRCLE_LEAD);
        double s = Math.sin(CIRCLE_LEAD);
        return centre.plus((rx * c + tx * s) * keep, (rz * c + tz * s) * keep);
    }

    /** The point {@code keep} blocks from {@code foe}, on the line from it to {@code b}. */
    private static Point standOff(FieldBlock b, FieldBlock foe, double keep) {
        double d = Math.max(1e-6, b.pos.distanceTo(foe.pos));
        return foe.pos.plus((b.pos.x() - foe.pos.x()) / d * keep, (b.pos.z() - foe.pos.z()) / d * keep);
    }

    /** Soldiers standing aboard {@code b}'s ship. */
    private int crewAlive(FieldBlock b) {
        return crew(b).stream().mapToInt(c -> c.alive).sum();
    }

    /** How far a Steel fleet's front row has come from where it deployed, on average (zero with none left). */
    private Point frontAdvance(FieldArmy army) {
        List<FieldBlock> front = army.blocks.stream().filter(b -> b.role == FieldBlock.FleetRole.FRONT && b.alive > 0
                && b.afloat && b == holder(b) && b.deployed != null).toList();
        return front.isEmpty() ? new Point(0, 0) : new Point(
                front.stream().mapToDouble(b -> b.pos.x() - b.deployed.x()).average().orElse(0),
                front.stream().mapToDouble(b -> b.pos.z() - b.deployed.z()).average().orElse(0));
    }

    /** Whether half a Steel fleet's front and support ships are gone: then its reserve goes in too, wherever it can. */
    private static boolean frontHalfGone(FieldArmy army) {
        java.util.Set<Integer> ships = new java.util.HashSet<>();
        java.util.Set<Integer> afloat = new java.util.HashSet<>();
        for (FieldBlock b : army.blocks) {
            if (b.role == FieldBlock.FleetRole.FRONT || b.role == FieldBlock.FleetRole.SUPPORT) {
                ships.add(b.shipId);
                if (b.alive > 0 && b.afloat) {
                    afloat.add(b.shipId);
                }
            }
        }
        return !ships.isEmpty() && afloat.size() * 2 < ships.size();
    }

    /**
     * A battle line's ship: its leader drives through a gap between enemy ships ({@link #wedge}), both broadsides firing
     * at whatever lies abeam ({@link #broadside}, no mark), and comes about to drive through again; the rest follow in
     * line ahead, each in the wake of the ship before it.
     */
    private void sailInLine(FieldBlock b, List<FieldBlock> mates, FieldArmy enemy) {
        int k = mates.indexOf(b);
        if (k > 0) {
            FieldBlock a = mates.get(k - 1);
            double gap = (a.hull.length + b.hull.length) / 2.0 + 10;
            b.line = a.pos.plus(-a.facingX * gap, -a.facingZ * gap);
            b.settleX = a.facingX;
            b.settleZ = a.facingZ;
            return;
        }
        if (b.wedgeAt == null || b.pos.distanceTo(b.wedgeAt) < shipLength()) {
            b.wedgeAt = wedge(b, enemy);
        }
        if (b.wedgeAt == null) {
            b.line = b.pos;
            return;
        }
        double d = Math.max(1e-6, b.pos.distanceTo(b.wedgeAt));
        b.line = b.wedgeAt;
        b.settleX = (b.wedgeAt.x() - b.pos.x()) / d;
        b.settleZ = (b.wedgeAt.z() - b.pos.z()) / d;
    }

    /**
     * Where a battle line's leader {@code b} makes for: two ship lengths beyond the middle of the nearest gap between two
     * enemy ships (no wider than six ships abreast), a gap ahead of its bow before one behind; with no such gap, past the
     * nearest enemy ship at half its gun range. Null with no enemy ship afloat.
     */
    @Nullable
    private Point wedge(FieldBlock b, FieldArmy enemy) {
        List<FieldBlock> foes = enemy.blocks.stream().filter(e -> e.alive > 0 && e.afloat && e == holder(e)).toList();
        if (foes.isEmpty()) {
            return null;
        }
        Point mid = null;
        double best = Double.MAX_VALUE;
        for (int i = 0; i < foes.size(); i++) {
            for (int j = i + 1; j < foes.size(); j++) {
                Point p = foes.get(i).pos;
                Point q = foes.get(j).pos;
                if (p.distanceTo(q) > 6 * shipBeam()) {
                    continue;
                }
                Point m = new Point((p.x() + q.x()) / 2, (p.z() + q.z()) / 2);
                boolean ahead = (m.x() - b.pos.x()) * b.facingX + (m.z() - b.pos.z()) * b.facingZ > 0;
                double score = b.pos.distanceTo(m) + (ahead ? 0 : 1000);
                if (score < best) {
                    best = score;
                    mid = m;
                }
            }
        }
        if (mid == null) {
            FieldBlock e = nearestShip(b, enemy);
            double d = Math.max(1e-6, b.pos.distanceTo(e.pos));
            double ux = (e.pos.x() - b.pos.x()) / d;
            double uz = (e.pos.z() - b.pos.z()) / d;
            mid = e.pos.plus(-uz * gunRange(b) / 2, ux * gunRange(b) / 2); // off its side, guns bearing as it passes
        }
        double d = Math.max(1e-6, b.pos.distanceTo(mid));
        return mid.plus((mid.x() - b.pos.x()) / d * 2 * shipLength(), (mid.z() - b.pos.z()) / d * 2 * shipLength());
    }

    /** Whether {@code at} lies within a ship's length of any of {@code spots}. */
    private static boolean clash(Point at, List<Point> spots) {
        return spots.stream().anyMatch(p -> p.distanceTo(at) < shipLength());
    }

    /** The nearest enemy ship afloat. */
    @Nullable
    private static FieldBlock nearestShip(FieldBlock b, FieldArmy enemy) {
        FieldBlock best = null;
        for (FieldBlock e : enemy.blocks) {
            if (e.alive > 0 && e.afloat && (best == null || b.pos.distanceTo(e.pos) < b.pos.distanceTo(best.pos))) {
                best = e;
            }
        }
        return best;
    }

    /** The defenders' harbour ships wait this far out past their walls, between them and the attacker's fleet. */
    static final double HARBOUR_OUT = 20;

    /**
     * An amphibious assault's port has its own fleet, on top of the army holding its walls: {@code ships} ships of a
     * formation each (every third of archers, the rest swordsmen), waiting in a line out past the walls, across the
     * front, to go out against the attacker's ships before they land. Before the battle begins only.
     */
    public void addHarbourFleet(int ships) {
        if (begun() || type != BattleType.AMPHIBIOUS) {
            throw new IllegalStateException("a harbour fleet is for an amphibious assault, before it begins");
        }
        FieldArmy d = armies[1];
        int id = d.blocks.stream().mapToInt(FieldBlock::id).max().orElse(SIDE_IDS) + 1;
        double rx = -d.facingZ;
        double rz = d.facingX;
        for (int i = 0; i < ships; i++) {
            double right = (i - (ships - 1) / 2.0) * BattlePlan.BLOCK_PITCH;
            Point at = d.origin.plus(d.facingX * HARBOUR_OUT + rx * right, d.facingZ * HARBOUR_OUT + rz * right);
            FieldBlock b = new FieldBlock(id++, 1, i % 3 == 2 ? Arm.ARCHERS : Arm.SKIRMISHERS, BattlePlan.SOLDIERS_PER_FORMATION,
                    FieldRules.SOLDIER_HEALTH, BattlePlan.LEADER_INDEX, false, at, d.facingX, d.facingZ, Duty.RESTING);
            b.afloat = true;
            b.ship = b.arm == Arm.ARCHERS ? FieldBlock.ShipKind.ARCHERS : FieldBlock.ShipKind.SWORDSMEN;
            b.deployed = at;
            d.blocks.add(b);
            d.started += b.started();
        }
    }

    /**
     * The defenders' harbour ships each go after the nearest of the attacker's ships still afloat. With none left
     * afloat they put back in under the walls, and their soldiers go up onto them, to hold them with the rest.
     */
    private void harbourFleet() {
        FieldArmy d = armies[1];
        for (FieldBlock b : d.blocks) {
            if (!b.afloat || b.alive == 0 || b.duty == Duty.FLEEING || b.duty == Duty.ROUTED || b.duty == Duty.RALLYING) {
                continue;
            }
            FieldBlock prey = null;
            double best = Double.MAX_VALUE;
            for (FieldBlock a : armies[0].blocks) {
                if (a.afloat && a.alive > 0 && b.pos.distanceTo(a.pos) < best) {
                    best = b.pos.distanceTo(a.pos);
                    prey = a;
                }
            }
            b.strikeAt = null;
            b.approaching = false;
            if (prey != null) {
                b.charged = true;
                b.prey = prey;
                continue;
            }
            b.charged = false;
            b.prey = null;
            Point mooring = b.pos.plus(-d.facingX * pastWalls(b.pos), -d.facingZ * pastWalls(b.pos));
            b.line = mooring.plus(d.facingX * LANDING, d.facingZ * LANDING);
            if (pastWalls(b.pos) <= LANDING + 1e-6) {
                // Ashore: into the queue of the nearest file of the wall's front, to relieve it in turn.
                b.afloat = false;
                b.line = mooring.plus(-d.facingX * BattlePlan.LINE_GAP, -d.facingZ * BattlePlan.LINE_GAP);
                int file = -1;
                for (int f = 0; f < d.front.length; f++) {
                    if (d.front[f] != null && (file < 0 || d.front[f].line.distanceTo(b.pos) < d.front[file].line.distanceTo(b.pos))) {
                        file = f;
                    }
                }
                if (file >= 0) {
                    d.join(b, file);
                }
                if (d.blocks.stream().noneMatch(o -> o.afloat && o.alive > 0)) {
                    events.add(new Event(1, rafasconquest.data.Dialogue.say("battle.events.harbour_ships")));
                }
            }
        }
    }

    /** Whether a side began the battle aboard its ships. */
    public boolean afloat(int side) {
        return type == BattleType.OCEAN || type == BattleType.AMPHIBIOUS && side == 0;
    }

    /** Where a block is making for this second: its prey, if it has charged one, else its place in the line. */
    public Point makingFor(FieldBlock block) {
        return block.charged && hunted(block.prey) && block.duty != Duty.FLEEING ? block.prey.pos : block.line;
    }

    /** The attacker's ships that have reached the wharf put their soldiers ashore. */
    private void land() {
        for (FieldBlock b : armies[0].blocks) {
            if (b.afloat && b.alive > 0 && pastWalls(b.pos) <= LANDING) {
                b.afloat = false;
                if (armies[0].blocks.stream().noneMatch(o -> o != b && !o.afloat && o.alive > 0)) {
                    events.add(new Event(0, rafasconquest.data.Dialogue.say("battle.events.first_ships")));
                }
            }
        }
    }

    /** How far a place is out past the defenders' walls (their deployed front line, facing the attacker); below zero inside. */
    private double pastWalls(Point at) {
        FieldArmy d = armies[1];
        return (at.x() - d.origin.x()) * d.facingX + (at.z() - d.origin.z()) * d.facingZ;
    }

    /** The defenders keep within their walls: none charges out, and every place they are sent to is kept inside. */
    private void holdWalls() {
        FieldArmy d = armies[1];
        for (FieldBlock b : d.blocks) {
            if (b.afloat) {
                continue; // the harbour fleet, out on the water (harbourFleet)
            }
            b.charged = false;
            b.approaching = false;
            b.strikeAt = null;
            b.prey = null;
            double past = pastWalls(b.line);
            if (past > 0) {
                b.line = b.line.plus(-d.facingX * past, -d.facingZ * past);
            }
        }
    }

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
        this(firstBlocks, secondBlocks, centre, axisX, axisZ, seed, rates, first, second, null, null);
    }

    /**
     * The same, each army of exactly its mix of blocks per arm (by ordinal; null: the usual shares), as a grand strategy
     * army's cards say. A side with a mix is as many blocks as its mix holds.
     */
    public FieldBattle(int firstBlocks, int secondBlocks, Point centre, double axisX, double axisZ, long seed,
                       FieldRules.Rates rates, WarPlan first, WarPlan second, @Nullable int[] firstMix, @Nullable int[] secondMix) {
        mixes = new int[][]{firstMix, secondMix};
        firstBlocks = firstMix == null ? firstBlocks : java.util.Arrays.stream(firstMix).sum();
        secondBlocks = secondMix == null ? secondBlocks : java.util.Arrays.stream(secondMix).sum();
        this.rates = rates;
        this.random = new Random(seed);
        Point[] fronts = BattlePlan.fronts(centre, axisX, axisZ);
        // Over UNIT_TACTICS_OVER soldiers the generals move their armies unit by unit (platoons); below it, block by block.
        byUnits = (firstBlocks + secondBlocks) * BattlePlan.SOLDIERS_PER_FORMATION > FieldRules.UNIT_TACTICS_OVER;
        this.firstBlocks = firstBlocks;
        armies = new FieldArmy[]{
                deploy(0, firstBlocks, fronts[0], axisX, axisZ, first, levyFront(first, random), byUnits, 0, mixes[0]),
                deploy(1, secondBlocks, fronts[1], -axisX, -axisZ, second, levyFront(second, random), byUnits, 0, mixes[1])};
    }

    /** Each side's blocks per arm (null: the usual shares), for a siege's redeployment. */
    private final int[][] mixes;

    /** Whether a side deployed by its own mix of arms rather than the usual shares. */
    public boolean mixed(int side) {
        return mixes[side] != null;
    }
    /** Whether the generals move their armies unit by unit; and the attacker's blocks, for a siege's redeployment. */
    private final boolean byUnits;
    private final int firstBlocks;
    /** Each side's siege train's engines to pick from ({@link #equip}), kept for a redeployment. */
    private final java.util.Map<Integer, List<Machine>> trains = new java.util.HashMap<>();

    /**
     * Whether a general puts the levies up front, to be crushed and draw the enemy on, rather than behind the heavy
     * infantry, supporting it: Bait and Pincer generals do; Spear and Shield generals don't; a Charge and Decapitate
     * general rolls for it. Their wing commanders deploy the same way.
     */
    static boolean levyFront(WarPlan plan, Random random) {
        if (plan.has(Strategy.BAIT)) {
            return true;
        }
        if (plan.doctrines().contains(Doctrine.SPEAR_AND_SHIELD)) {
            return false;
        }
        return plan.doctrines().contains(Doctrine.CHARGE_AND_DECAPITATE) && random.nextBoolean();
    }

    /** Block ids run from this times their army's side ({@link #deploy}, {@link #addHarbourFleet}). */
    private static final int SIDE_IDS = 10_000;

    /** Which side a block belongs to, by its id. */
    public static int sideOf(int blockId) {
        return blockId / SIDE_IDS;
    }

    private static FieldArmy deploy(int side, int blocks, Point front, double facingX, double facingZ, WarPlan plan, boolean levyFront,
                                    boolean byUnits, int maxFiles, @Nullable int[] mix) {
        FieldArmy army = new FieldArmy(side, facingX, facingZ, plan);
        army.origin = front;
        army.stackFiles = BattlePlan.stackFiles(blocks, plan.has(Formation.GREAT_SHIELD));
        army.stackDepth = BattlePlan.stackDepth(blocks, plan.has(Formation.GREAT_SHIELD));
        boolean shield = plan.has(Formation.GREAT_SHIELD);
        boolean spear = plan.has(Formation.GREAT_SPEAR);
        boolean unrelenting = plan.has(Strategy.UNRELENTING_CHARGE);
        List<BattlePlan.Slot> slots = BattlePlan.deployment(blocks, shield, plan.wingShares(), levyFront, maxFiles, mix);
        int files = (int) slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        army.front = new FieldBlock[files];
        army.baitFiles = new boolean[files];
        army.winged = slots.stream().anyMatch(s -> s.wing() != FieldArmy.CENTRE);
        double reserves = Double.MAX_VALUE;
        int id = side * SIDE_IDS;
        double lastLine = 0;
        double frontHalf = 0;
        double[] wingSum = new double[3];
        int[] wingFiles = new int[3];
        List<FieldBlock> cavalry = new ArrayList<>();
        int[] platoons = BattlePlan.platoons(slots);
        int k = 0;
        for (BattlePlan.Slot slot : slots) {
            Point line = BattlePlan.place(front, facingX, facingZ, slot.right(), slot.back());
            Duty duty = !slot.arm().infantry() ? Duty.SUPPORT : slot.line() == 0 ? Duty.FRONT : Duty.LINE;
            FieldBlock block = new FieldBlock(id++, side, slot.arm(), BattlePlan.SOLDIERS_PER_FORMATION, FieldRules.SOLDIER_HEALTH,
                    BattlePlan.LEADER_INDEX, false, line, facingX, facingZ, duty);
            block.file = slot.file();
            block.files = slot.files();
            block.homeRight = slot.right();
            block.wing = slot.wing();
            block.platoon = platoons[k++];
            block.unit = byUnits ? block.platoon : -1;
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
            } else if (slot.arm().mounted()) {
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
        army.bearer = new FieldBlock(id, side, Arm.SKIRMISHERS, 1, FieldRules.SOLDIER_HEALTH, -1, false,
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
        // Bait: the unit in the middle of each wing's front that has three files or more (its levies, when they stand
        // up front), every front file of it, though it be the wing's whole front.
        if (plan.has(Strategy.BAIT)) {
            for (int wing = 0; wing < 3; wing++) {
                List<Integer> inWing = new ArrayList<>();
                for (FieldBlock f : army.front) {
                    if (f.wing == wing) {
                        inWing.add(f.file);
                    }
                }
                if (inWing.size() >= 3) {
                    makeBait(army, army.front[inWing.get(inWing.size() / 2)]);
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

    /**
     * Makes {@code front}'s unit its wing's bait, in place of any other: every front file it holds runs ahead. Where the
     * wing's front stands now is kept as its line, for when the bait is all of it.
     */
    private static void makeBait(FieldArmy army, FieldBlock front) {
        int wing = front.wing;
        double x = 0;
        double z = 0;
        int n = 0;
        for (FieldBlock f : army.front) {
            if (f.wing == wing) {
                army.baitFiles[f.file] = f.duty == Duty.FRONT && sameUnit(f, front);
                x += f.line.x();
                z += f.line.z();
                n++;
            }
        }
        army.baitFile[wing] = front.file;
        army.baitUnit[wing] = unitKey(front);
        army.baitLine[wing] = new Point(x / n, z / n);
    }

    /**
     * A wing whose whole front is its bait keeps a line all the same, where its front would stand ({@link #behindOthers}):
     * it marches on as a front does until an enemy comes near it or the bait has drawn the enemy in, then holds.
     */
    private void advanceBaitLines(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS) {
            return;
        }
        for (int wing = 0; wing < 3; wing++) {
            Point line = army.baitLine[wing];
            if (line != null && !army.baitSprung[wing] && nearestEnemy(enemy, line) > BattlePlan.INFANTRY_ENGAGE) {
                army.baitLine[wing] = line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
            }
        }
    }

    /** Whether a front file is one of its army's baits. */
    private static boolean isBait(FieldArmy army, int file) {
        return file >= 0 && file < army.baitFiles.length && army.baitFiles[file];
    }

    /**
     * Whether a block waits in a bait's file without being of the bait's unit: it doesn't follow the bait out or go up
     * to relieve it, but keeps to its wing's line.
     */
    private static boolean holdsBehindBait(FieldArmy army, FieldBlock b) {
        return b.duty == Duty.LINE && isBait(army, b.file) && unitKey(b) != army.baitUnit[b.wing];
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
        if (block.afloat) {
            block = holder(block); // a block afloat is placed with its whole ship
        }
        block.line = at;
        block.deployed = at;
        if (!block.live) {
            block.pos = at;
            if (block.afloat) {
                int ship = block.shipId;
                // Ships placed by hand still mustn't lie on top of each other.
                dropClear(army, block, army.blocks.stream().filter(o -> o.shipId != ship && o == holder(o)).toList());
                at = block.pos;
            }
        }
        if (block.afloat) {
            syncCrews();
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
        if (block.arm.mounted() && block.duty == Duty.SUPPORT) {
            block.homeRight = BattlePlan.offset(army.frontCentre(), army.facingX, army.facingZ, at).x();
        }
    }

    /** During deployment, a block a player leads is put straight where they told it, facing as told: no march there. */
    public void snap(FieldBlock block, Point at, double fx, double fz) {
        if (block.afloat) {
            block = holder(block); // the whole ship, and everyone aboard (syncCrews, each second)
        }
        if (!begun()) {
            place(block, at);
        } else if (!block.live) {
            block.pos = at; // a live block's place is read back from its soldiers, put there themselves
        }
        block.facingX = fx;
        block.facingZ = fz;
        if (block.afloat) {
            syncCrews();
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
        FieldBlock foe = hunted(block.contactFoe) ? block.contactFoe : hunted(block.prey) ? block.prey : nearestBlock(block, armies[1 - block.side], false);
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
            syncCrews();
            return;
        }
        seconds++;
        if (bodies != null) {
            placeFromBodies();
        }
        index();
        if (type == BattleType.OCEAN) {
            captains(); // at sea the ships' captains decide everything: none of the land's generalship applies
        } else {
            for (int side = 0; side < 2; side++) {
                if (siege != null && side == 1) {
                    siege.defend(armies[1], armies[0], seconds);
                } else {
                    command(armies[side], armies[1 - side]);
                }
            }
        }
        if (siege != null) {
            siege.attack(armies[0], armies[1], seconds);
        } else if (type.walled()) {
            holdWalls();
        }
        if (type == BattleType.AMPHIBIOUS) {
            harbourFleet();
        }
        for (FieldArmy army : armies) {
            move(army);
        }
        keepShipsApart();
        syncCrews();
        if (type == BattleType.AMPHIBIOUS) {
            land();
        }
        index();
        fight();
        momentum();
        if (siege != null) {
            siege.afterFighting(armies[0], armies[1], seconds);
            events.addAll(siege.takeEvents());
        }
        for (FieldArmy army : armies) {
            morale(army, armies[1 - army.side]);
        }
        if (!finalMelee && siege == null && type != BattleType.OCEAN && BattlePlan.stalled(seconds, lastFall)) {
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
        advanceBaitLines(army, enemy);
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
            if (block.led) {
                obey(army, block); // last, over anything the plans above made of it
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
                if (block.arm.mounted() && block.momentum < FieldRules.SPENT_MOMENTUM
                        && block.contactSeconds >= FieldRules.SPENT_SECONDS
                        && (block.duty == Duty.SUPPORT || block.duty == Duty.GUARD || block.duty == Duty.GENERAL)) {
                    for (FieldBlock mate : unit(army, block, block.pos)) {
                        if (mate.charged && !mate.led) {
                            recall(army, mate); // its unit rode in together and rides out together
                        }
                    }
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
                    Point fileFront = army.front[block.file].line;
                    if (holdsBehindBait(army, block)) {
                        // Behind a bait, from where its file's front would stand in the wing's line.
                        double back = behindOthers(army, block.wing, fileFront);
                        fileFront = fileFront.plus(army.facingX * back, army.facingZ * back);
                    }
                    block.line = BattlePlan.place(fileFront, army.facingX, army.facingZ, offset.x(), offset.z());
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
                    if (!block.shattered() && seconds - block.dutySince >= FieldRules.REGROUP_SECONDS) {
                        // Regrouped, its morale whole again: foot of the line back to the back of its file; cavalry,
                        // archers and the guard back to their own work.
                        java.util.Arrays.fill(block.history, block.alive);
                        if (block.homeDuty == Duty.FRONT || block.homeDuty == Duty.LINE) {
                            army.join(block, block.file);
                        } else {
                            block.duty = block.homeDuty;
                            block.dutySince = seconds;
                            if (block.arm.mounted() && block.duty == Duty.SUPPORT && !block.inWedge) {
                                patrol(army, block); // rides its wing's backline again
                            }
                        }
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
        if (army.baitSprung[block.wing] && order == BattlePlan.Order.MARCH
                && seconds - army.baitSprungAt[block.wing] < FieldRules.BAIT_HOLD_SECONDS) {
            order = BattlePlan.Order.HOLD; // the wing waits a while for its bait to draw the enemy in, then moves up
        }
        switch (order) {
            case MARCH -> {
                // Straight ahead, keeping the line, while the nearest enemy is ahead; once the enemy is beside or
                // behind it (its own foe gone, the lines passed through each other), toward that enemy instead.
                block.engaged = false;
                double stepX = army.facingX;
                double stepZ = army.facingZ;
                FieldBlock foe = nearestBlock(block, enemy, false);
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
                if (step <= 0 && foe == null && behindOthers(army, block.wing, block.line) > FieldRules.LINE_LAG) {
                    step = BattlePlan.MARCH_STEP; // not yet fighting and behind its wing's line: the general brings it up
                }
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
                army.baitSprungAt[block.wing] = seconds;
                calls.add(new Order(army.side, Call.BAIT));
                events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.bait_falls_back", "where", army.where(block.wing))));
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

    /**
     * How far behind the lines of a wing's front blocks, baits aside, a point is (negative: ahead of them); behind where
     * its front stood when its bait was chosen, if the bait is all of its front.
     */
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
            Point line = army.baitLine[wing];
            return line == null ? 0 : (line.x() - at.x()) * army.facingX + (line.z() - at.z()) * army.facingZ;
        }
        return (x / n - at.x()) * army.facingX + (z / n - at.z()) * army.facingZ;
    }

    /**
     * Front-line relief, unit by unit ({@link BattlePlan#platoons}): a front unit worn down to
     * {@link BattlePlan#RELIEF_SHARE} of its soldiers, or (in a stacked front) whose front has fought
     * {@link FieldRules#ROTATE_SECONDS}, cycles out whole: the unit next behind it in its files takes every file it
     * holds at once, and it goes, front and waiting blocks alike, to the back of its files to rest. Between cycles a
     * worn front block is relieved by a waiting block of its own unit; if its unit has none left, the unit cycles early.
     */
    private void relieve(FieldArmy army) {
        java.util.Map<Integer, List<Integer>> held = new java.util.LinkedHashMap<>();
        for (int file = 0; file < army.front.length; file++) {
            held.computeIfAbsent(unitKey(army.front[file]), k -> new ArrayList<>()).add(file);
        }
        held.values().forEach(files -> relieveUnit(army, files));
    }

    /** Relief for the front unit holding {@code files} ({@link #relieve}). */
    private void relieveUnit(FieldArmy army, List<Integer> files) {
        List<Integer> free = new ArrayList<>();
        boolean underWay = false;
        for (int file : files) {
            FieldBlock h = army.front[file];
            if (h.relief != null && h.relief.alive > 0 && h.relief.duty == Duty.RELIEVING) {
                underWay = true;
                continue;
            }
            h.relief = null;
            h.rotating = false; // a relief that never arrived (routed on its way) leaves it holding, not cycling out
            if (isBait(army, file) && unit(army, h, h.pos).isEmpty()) {
                army.baitFiles[file] = false; // the bait's unit is spent here: the file is its line's again
            }
            if (h.strikeAt == null && !h.led) {
                free.add(file); // a player's block is relieved when its player says
            }
        }
        if (underWay || free.isEmpty()) {
            return;
        }
        FieldBlock first = army.front[files.get(0)];
        int alive = 0;
        int started = 0;
        for (FieldBlock b : unitBlocks(army, first)) {
            alive += b.alive;
            started += b.started();
        }
        boolean due = army.stackDepth > 1 && free.stream().anyMatch(f -> army.front[f].alive > 0 && army.front[f].fightSeconds >= FieldRules.ROTATE_SECONDS);
        if ((BattlePlan.needsRelief(alive, started) || due) && cycle(army, free)) {
            return;
        }
        boolean cycled = false;
        for (int file : free) {
            FieldBlock h = army.front[file];
            if (!BattlePlan.needsRelief(h.alive, h.started())) {
                continue;
            }
            FieldBlock relief = ownRelief(army, h);
            if (relief == null && !cycled) {
                cycled = true;
                if (cycle(army, free)) {
                    return; // its unit has no fresh block left: it cycles out early
                }
            }
            if (relief == null) {
                relief = pickRelief(army, file, h.line);
            }
            if (relief == null && h.alive == 0) {
                relief = lastStand(army, h.line);
            }
            if (relief != null) {
                sendToRelieve(relief, h, file);
            }
        }
    }

    /**
     * Cycles the front unit holding {@code files} out: the unit next behind it in those files (the first fit waiting
     * block there of another unit) takes them all at once, its blocks already in a file taking that file, the rest
     * the nearest left; a file it can't fill takes the next fit block waiting in it. The front blocks rotate out to the
     * back of their files, and the unit's waiting blocks go to the back of theirs. False if no unit stands behind.
     */
    private boolean cycle(FieldArmy army, List<Integer> files) {
        int out = unitKey(army.front[files.get(0)]);
        FieldBlock next = null;
        for (int file : files) {
            for (FieldBlock q : army.queue(file)) {
                if (next == null && unitKey(q) != out && canRelieve(army, q)) {
                    next = q;
                }
            }
        }
        if (next == null) {
            return false;
        }
        List<FieldBlock> pool = new ArrayList<>(unit(army, next, next.pos));
        pool.removeIf(b -> b.duty != Duty.LINE || !canRelieve(army, b));
        for (int file : files) {
            FieldBlock h = army.front[file];
            FieldBlock relief = pool.stream().filter(b -> b.file == file).min(Comparator.comparingInt(b -> b.queueTicket))
                    .or(() -> pool.stream().min(Comparator.comparingDouble(b -> b.line.distanceTo(h.line))))
                    .or(() -> army.queue(file).stream().filter(q -> unitKey(q) != out && q.duty == Duty.LINE && canRelieve(army, q)).findFirst())
                    .orElse(null);
            if (relief != null) {
                pool.remove(relief);
                sendToRelieve(relief, h, file);
                h.rotating = h.alive > 0;
            }
        }
        for (FieldBlock b : army.blocks) {
            if (b.duty == Duty.LINE && b.alive > 0 && unitKey(b) == out) {
                army.join(b, b.file); // its waiting blocks fall back to the end of their files with it
            }
        }
        return true;
    }

    /** Whether a waiting block may go up to relieve: fit, free, and not held back behind a bait. */
    private static boolean canRelieve(FieldArmy army, FieldBlock b) {
        return !b.led && b.strikeAt == null && b.task == FieldBlock.Task.NONE && !holdsBehindBait(army, b)
                && BattlePlan.fitToRelieve(b.alive, b.started());
    }

    /** A fit waiting block of {@code held}'s own unit to relieve it: one in its file first, else the nearest. */
    @Nullable
    private static FieldBlock ownRelief(FieldArmy army, FieldBlock held) {
        for (FieldBlock q : army.queue(held.file)) {
            if (sameUnit(q, held) && canRelieve(army, q)) {
                return q;
            }
        }
        return unit(army, held, held.line).stream().filter(b -> b.duty == Duty.LINE && canRelieve(army, b)).findFirst().orElse(null);
    }

    private void sendToRelieve(FieldBlock relief, FieldBlock held, int file) {
        relief.duty = Duty.RELIEVING;
        relief.file = file;
        relief.dutySince = seconds;
        relief.line = held.line;
        held.relief = relief;
    }

    @Nullable
    private static FieldBlock pickRelief(FieldArmy army, int file, Point spot) {
        for (FieldBlock f : army.queue(file)) {
            if (!f.led && !holdsBehindBait(army, f) && BattlePlan.fitToRelieve(f.alive, f.started())) {
                return f;
            }
        }
        return army.blocks.stream()
                .filter(f -> !f.led && !holdsBehindBait(army, f) && f.duty == Duty.LINE && f.alive > 0 && f.strikeAt == null && f.task == FieldBlock.Task.NONE && BattlePlan.fitToRelieve(f.alive, f.started()))
                .min(Comparator.comparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    /**
     * A front file whose block has fallen, with no fit block left to send: any block still in the lines goes, worn or
     * not, else the nearest resting block, so the line doesn't stay open.
     */
    @Nullable
    private static FieldBlock lastStand(FieldArmy army, Point spot) {
        return army.blocks.stream()
                .filter(f -> !f.led && f.alive > 0 && f.strikeAt == null && (f.duty == Duty.LINE || f.duty == Duty.RESTING))
                .min(Comparator.comparingInt((FieldBlock f) -> f.duty == Duty.LINE ? 0 : 1)
                        .thenComparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    private void takeOver(FieldArmy army, FieldBlock relief) {
        FieldBlock relieved = army.front[relief.file];
        if (relieved.relief != relief) {
            army.join(relief, relief.file); // the block it was sent for left the front (routed) before it came: it waits
            return;
        }
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
            // Cycled out with its unit: to the back of its file, where it rests until its unit's turn comes round again.
            relieved.rotating = false;
            relieved.fightSeconds = 0;
            relieved.placed = null;
            army.join(relieved, relief.file);
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
        boolean cavalry = block.arm.mounted();
        if (cavalry && block.rode) {
            patrol(army, block);
        }
        if (seconds < block.reformUntil) {
            return; // reforming after a charge, still riding
        }
        // Cavalry acts on its own wing's fighting, not the whole army's.
        boolean fighting = cavalry ? army.wingEngaged(block.wing) : engaged;
        BattlePlan.Order order = BattlePlan.order(block.arm, seconds, nearest(block, cavalry ? block.pos : block.line, enemy), fighting, block.standOff());
        if (cavalry && order != BattlePlan.Order.CHARGE && seconds >= BattlePlan.FORMING_SECONDS
                && army.plan.has(Strategy.PRECISION_STRIKES)) {
            FieldBlock exposed = mostExposed(enemy, block.pos, FieldRules.WING_REACH);
            if (exposed != null) {
                events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.exposed_block", "where", army.where(block.wing))));
                charge(army, block, exposed);
                return;
            }
        }
        if (cavalry && block.rode && order != BattlePlan.Order.CHARGE) {
            return; // keeps riding its backline
        }
        switch (order) {
            case MARCH -> {
                if (!block.arm.shoots() || behindFront(army, block.line) - BattlePlan.MARCH_STEP >= BattlePlan.archerBack(army.stackFiles(), army.stackDepth)) {
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
     * Its whole unit goes with it, those of it free to, spread over its prey's unit; the wedge rides as its general leads.
     */
    private void charge(FieldArmy army, FieldBlock block, @Nullable FieldBlock prey) {
        FieldBlock enemyPick = prey != null ? prey : choosePrey(army, armies[1 - army.side], block);
        List<FieldBlock> riders = unit(army, block, block.pos);
        riders.removeIf(b -> b != block && (b.charged || b.approaching || b.led || b.strikeAt != null || b.duty != block.duty
                || seconds < b.reformUntil));
        List<FieldBlock> preys = enemyPick == null ? List.of() : unit(armies[enemyPick.side], enemyPick, enemyPick.pos);
        for (int i = 0; i < riders.size(); i++) {
            chargeOne(army, riders.get(i), preys.isEmpty() ? enemyPick : preys.get(i % preys.size()));
        }
        calls.add(new Order(army.side, Call.CAVALRY_CHARGE));
    }

    private void chargeOne(FieldArmy army, FieldBlock block, @Nullable FieldBlock enemyPick) {
        block.prey = enemyPick;
        block.rode = block.arm.mounted();
        if (enemyPick != null && block.arm.mounted() && enemyPick.arm.infantry() && army.plan.has(Strategy.CAVALRY_FLANK)) {
            block.approaching = true;
            block.waypoint = flankPoint(enemyPick, block.pos);
            block.dutySince = seconds;
        } else {
            block.charged = true;
        }
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
            if (!cavalry.arm.mounted() || !(cavalry.charged || cavalry.approaching) || cavalry.alive == 0) {
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
        events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.guard_rides_out")));
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
            events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.wedge_forms")));
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
            if (m.alive > 0 && m.duty != Duty.ROUTED && (half < 0 || m.half == half)) { // a routed rider sits the charge out
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
        String who = target.duty == Duty.GENERAL ? "general" : target == army.pincerTarget[FieldArmy.CENTRE] ? "lured" : "line";
        events.add(new Event(army.side, say("events.wedge", "verb", say(led ? "events.wedge_leads" : "events.wedge_sends"),
                "half", unrelenting ? say(half == 0 ? "events.wedge_first_half" : "events.wedge_second_half") : "",
                "target", say("events.wedge_at_" + who), "around", waypoint != null ? say("events.wedge_around") : "")));
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
        return best != null ? best : nearestBlock(tip, enemy, false);
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
        if (target != null && (unit(enemy, target, target.pos).isEmpty() || seconds - army.pincerSince[wing] > FieldRules.PINCER_SECONDS)) {
            army.pincerTarget[wing] = null;
            for (FieldBlock b : army.blocks) {
                if (b.strikeAt != null && sameUnit(b.strikeAt, target)) {
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
            // Drawn in: too far ahead of its own front, or (when that front moves as one) come up to this wing's line.
            if (e.alive > 0 && e.arm.infantry() && e.duty != Duty.FLEEING && !e.charged && e.distanceTo(bait) <= FieldRules.LURE_REACH
                    && (-behindFront(enemy, e.pos) >= FieldRules.TOO_FAR_FORWARD || behindOthers(army, wing, e.pos) >= 0) && !struck(army, e)
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
            events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.pincer_wedge", "where", where)));
            return;
        }
        boolean cavalry = false;
        for (FieldBlock c : army.blocks) {
            if (c.alive > 0 && c.arm.mounted() && c.duty == Duty.SUPPORT && !c.inWedge && !c.charged && !c.approaching
                    && seconds >= c.reformUntil && (c.wing == wing || !army.winged) && c.pos.distanceTo(lured.pos) <= FieldRules.WING_REACH) {
                charge(army, c, lured);
                cavalry = true;
            }
        }
        if (cavalry) {
            events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.pincer_cavalry", "where", where)));
            return;
        }
        // Else the wing's nearest infantry unit not fighting: three blocks or more surround the lured unit (each block of
        // it struck on both flanks and the rear), fewer strike its flank.
        List<FieldBlock> free = freeUnit(army, idleInfantry(army, wing, lured, Double.MAX_VALUE), lured, false);
        int s = free.isEmpty() ? 0 : sideNearer(lured, free.get(0).pos);
        if (free.size() >= 3) {
            sendUnit(free, lured, s, -s, 0);
            events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.pincer_surround", "where", where)));
        } else if (!free.isEmpty()) {
            sendUnit(free, lured, s);
            events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.pincer_flank", "where", where)));
        } else {
            army.pincerTarget[wing] = null;
        }
    }

    /**
     * The first unit among {@code idle} blocks' units (in their order) that is free to strike, as its blocks that go,
     * nearest {@code target} first; empty if none ({@link #goers}). With {@code reserves}, only a unit with no block in
     * the front.
     */
    private static List<FieldBlock> freeUnit(FieldArmy army, List<FieldBlock> idle, FieldBlock target, boolean reserves) {
        java.util.Set<Integer> seen = new java.util.HashSet<>();
        for (FieldBlock b : idle) {
            if (seen.add(unitKey(b))) {
                List<FieldBlock> goers = goers(army, b, target.pos);
                if (!goers.isEmpty() && !(reserves && goers.stream().anyMatch(g -> g.duty == Duty.FRONT))) {
                    return goers;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * The blocks of {@code block}'s unit that go when it is sent to strike, nearest {@code to} first: all of it in the
     * front or the files and not busy; none if any of it is busy (fighting, striking, charging, braced, a bait, given a
     * task or led), so a unit goes whole or not at all.
     */
    private static List<FieldBlock> goers(FieldArmy army, FieldBlock block, Point to) {
        List<FieldBlock> unit = unit(army, block, to);
        for (FieldBlock b : unit) {
            if (b.led || b.task != FieldBlock.Task.NONE || b.charged || b.braced || b.strikeAt != null || b.contactFoe != null
                    || b.duty == Duty.FRONT && isBait(army, b.file)) {
                return new ArrayList<>();
            }
        }
        unit.removeIf(b -> b.duty != Duty.FRONT && b.duty != Duty.LINE || !b.arm.infantry());
        return unit;
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

    /** Whether any of the army's infantry has been sent against {@code target}'s unit already. */
    private static boolean struck(FieldArmy army, FieldBlock target) {
        return struckUnits(army).contains(unitKey(target));
    }

    /** The enemy units ({@link #unitKey}) the army's infantry has been sent against. */
    private static java.util.Set<Integer> struckUnits(FieldArmy army) {
        java.util.Set<Integer> struck = new java.util.HashSet<>();
        for (FieldBlock b : army.blocks) {
            if (b.strikeAt != null) {
                struck.add(unitKey(b.strikeAt));
            }
        }
        return struck;
    }

    /**
     * Envelopment: once the army is fighting, where a front block has no enemy ahead of it (its wing reaches past the
     * end of the enemy's line), the front holds and the wing's nearest free reserve unit (no block of it in the front)
     * goes round onto the nearest enemy infantry unit beside it within {@link FieldRules#ENVELOP_REACH} that no unit of
     * ours strikes yet: two blocks on each enemy block, its flank and its rear. One unit at a time a wing; when an enemy
     * block falls its strikers turn on the next of its unit ({@link #strike}), so the wing rolls up the exposed flank.
     */
    private void envelop(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS || !army.infantryEngaged()) {
            return;
        }
        java.util.Set<Integer> struck = struckUnits(army);
        boolean[] wheeling = new boolean[3];
        boolean[] still = new boolean[3]; // a unit of the wing is round the enemy's flank already: one at a time
        for (int wing = 0; wing < 3; wing++) {
            still[wing] = army.enveloping[wing] && struckBy(army, wing);
        }
        for (FieldBlock b : army.front) {
            if (b.alive == 0 || b.duty != Duty.FRONT || b.contactFoe != null || b.strikeAt != null || b.braced
                    || b.charged || isBait(army, b.file) || wheeling[b.wing] || still[b.wing]) {
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
                if (!struck.contains(unitKey(target))) {
                    // The front holds; a free unit of the wing's reserves goes round its end: each enemy block struck in
                    // its flank, then its rear.
                    List<FieldBlock> unit = freeUnit(army, idleInfantry(army, b.wing, target, Double.MAX_VALUE), target, true);
                    if (!unit.isEmpty()) {
                        sendUnit(unit, target, sideNearer(target, b.pos), 0);
                        struck.add(unitKey(target));
                        wheeling[b.wing] = true;
                    }
                    break;
                }
            }
        }
        for (int wing = 0; wing < 3; wing++) {
            if (wheeling[wing] && !army.enveloping[wing]) {
                events.add(new Event(army.side, say("events.wheels", "wing", say("wheel." + (army.where(wing).isEmpty() ? "line" : FieldArmy.wingKey(wing))))));
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

    /**
     * Enemy cavalry that has stopped (caught in a melee, or waiting) is set on by the infantry near it: the nearest
     * free unit with an idle block within {@link FieldRules#HUNT_REACH} closes in on its unit, two blocks on each
     * rider block, one on each flank. They let it go once it rides on ({@link #strike}).
     */
    private void huntStoppedCavalry(FieldArmy army, FieldArmy enemy) {
        if (seconds < BattlePlan.FORMING_SECONDS) {
            return;
        }
        // Whose units are hunted already, once for all the stopped cavalry.
        java.util.Set<Integer> hunted = struckUnits(army);
        for (FieldBlock c : enemy.blocks) {
            if (c.alive == 0 || !c.arm.mounted() || c.duty == Duty.FLEEING || c.lastPos == null || c.moved >= FieldRules.STOPPED
                    || hunted.contains(unitKey(c))) {
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
            List<FieldBlock> hunters = freeUnit(army, near, c, false);
            if (!hunters.isEmpty()) {
                int s = sideNearer(c, hunters.get(0).pos);
                sendUnit(hunters, c, s, -s);
                hunted.add(unitKey(c));
                events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.hunt_cavalry", "where", army.where(hunters.get(0).wing))));
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
            makeBait(armies[block.side], block); // a player's unit takes the place of the general's bait in its wing
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
                    events.add(new Event(army.side, rafasconquest.data.Dialogue.say("battle.events.ambush", "where", army.where(b.wing))));
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

    /** A player takes a block to lead: from now on the general leaves it be, and it holds where it stands until ordered. */
    public void lead(FieldBlock block) {
        block.led = true;
        block.ledLine = block.line;
        block.ledFacingX = block.facingX;
        block.ledFacingZ = block.facingZ;
        block.ledCharge = false;
        block.ledTarget = null;
        block.ledRam = false;
    }

    /** A led block's player orders it: to stand at {@code line} facing ({@code fx}, {@code fz}), or to charge (at a target). */
    public void steer(FieldBlock block, Point line, double fx, double fz, boolean charge, @Nullable FieldBlock target) {
        steer(block, line, fx, fz, charge, target, false);
    }

    /** As above; afloat, {@code ram} has its ship ram {@code target} instead of boarding it abreast. */
    public void steer(FieldBlock block, Point line, double fx, double fz, boolean charge, @Nullable FieldBlock target, boolean ram) {
        // Afloat, the order is its ship's: whichever block aboard is ordered, the whole ship goes.
        for (FieldBlock b : block.afloat ? crew(block) : List.of(block)) {
            b.ledLine = line;
            b.ledFacingX = fx;
            b.ledFacingZ = fz;
            b.ledCharge = charge;
            b.ledTarget = target;
            b.ledRam = ram;
        }
    }

    /**
     * A led block does as its player said, whatever the general's plans this second made of it. Breaking and running is
     * the soldiers' own doing, not an order, so a routed or fleeing block runs on.
     */
    private void obey(FieldArmy army, FieldBlock b) {
        if (b.duty == Duty.ROUTED || b.duty == Duty.FLEEING) {
            return;
        }
        b.approaching = false;
        if (b.ledCharge) {
            if (b.ledTarget != null && b.ledTarget.alive > 0 && b.arm.shoots()) {
                loose(b, b.ledTarget);
                return;
            }
            if (b.ledTarget != null && b.ledTarget.alive > 0) {
                if (b.strikeAt != b.ledTarget) {
                    send(b, b.ledTarget, sideNearer(b.ledTarget, b.pos));
                }
                b.charged = false;
                strike(army, b);
            } else {
                b.strikeAt = null;
                b.charged = true;
            }
            return;
        }
        b.strikeAt = null;
        b.charged = false;
        if (b.ledLine != null) {
            b.line = b.ledLine;
            b.facingX = b.ledFacingX;
            b.facingZ = b.ledFacingZ;
        }
    }

    /** Archers sent at a block stand this share of their range from it, so a step back by it keeps it in reach. */
    public static final double LOOSE_AT = 0.8;

    /**
     * Archers sent at a block don't charge it: they close to {@link #LOOSE_AT} of their range, stand there facing it, and
     * shoot it ({@link #shoot}).
     */
    private static void loose(FieldBlock b, FieldBlock target) {
        b.strikeAt = null;
        b.charged = false;
        double stand = FieldRules.ARCHER_RANGE * LOOSE_AT;
        double d = b.pos.distanceTo(target.pos);
        b.line = d <= stand ? b.pos : new Point(target.pos.x() + (b.pos.x() - target.pos.x()) * stand / d,
                target.pos.z() + (b.pos.z() - target.pos.z()) * stand / d);
        face(b, target.pos);
    }

    private static void send(FieldBlock block, FieldBlock target, int side) {
        block.strikeAt = target;
        block.strikeSide = side;
    }

    /**
     * Sends a unit's blocks at {@code target}'s unit (as {@link #send}): the enemy blocks nearest {@code target} first
     * (it first), each taking {@code sides.length} of ours, one from each of {@code sides} in turn, nearest of ours first;
     * more of ours than that go round again.
     */
    private void sendUnit(List<FieldBlock> unit, FieldBlock target, int... sides) {
        List<FieldBlock> foes = unit(armies[target.side], target, target.pos);
        if (foes.isEmpty()) {
            foes = List.of(target);
        }
        unit.sort(Comparator.comparingDouble(b -> b.pos.distanceTo(target.pos)));
        for (int i = 0; i < unit.size(); i++) {
            send(unit.get(i), foes.get(i / sides.length % foes.size()), sides[i % sides.length]);
        }
    }

    /**
     * Whether two blocks are of one unit (a platoon of up to 200, {@link BattlePlan#platoons}), which the general
     * sends whole: the same block, or of one platoon of one army. The wedge's riders ride as their general leads them,
     * each its own unit.
     */
    private static boolean sameUnit(FieldBlock a, FieldBlock b) {
        return a == b || a.side == b.side && a.unit >= 0 && a.unit == b.unit && !a.inWedge && !b.inWedge;
    }

    /** A key for a block's unit, unique within its army: its unit (platoon), else its own. */
    private static int unitKey(FieldBlock b) {
        return b.unit >= 0 && !b.inWedge ? b.unit : -1 - b.id;
    }

    /** The blocks of {@code block}'s unit, standing or not: its platoon in a battle fought by units, else itself. */
    private static List<FieldBlock> unitBlocks(FieldArmy army, FieldBlock block) {
        return block.unit >= 0 && !block.inWedge ? army.platoon(block.unit) : List.of(block);
    }

    /** The standing blocks (alive and not fleeing) of {@code block}'s unit in {@code army}, nearest {@code to} first. */
    private static List<FieldBlock> unit(FieldArmy army, FieldBlock block, Point to) {
        List<FieldBlock> unit = new ArrayList<>();
        for (FieldBlock b : unitBlocks(army, block)) {
            if (sameUnit(b, block) && b.alive > 0 && b.duty != Duty.FLEEING) {
                unit.add(b);
            }
        }
        unit.sort(Comparator.comparingDouble(b -> b.pos.distanceTo(to)));
        return unit;
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
        if (t != null && (t.alive == 0 || t.duty == Duty.FLEEING) && !block.led) {
            // Its block gone, it turns on the nearest of that block's unit still standing.
            List<FieldBlock> rest = unit(armies[t.side], t, block.pos);
            if (!rest.isEmpty()) {
                t = rest.get(0);
                block.strikeAt = t;
            }
        }
        boolean gone = t == null || t.alive == 0 || t.duty == Duty.FLEEING;
        // A kill squad keeps after its officer, mounted or not.
        boolean escaped = !gone && block.task != FieldBlock.Task.KILL && t.arm.mounted() && (t.moved >= FieldRules.STOPPED * 3
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
     * What presses a block toward breaking, each as a share of its soldiers: its losses over the last
     * {@link FieldRules#ROUT_WINDOW} seconds and every shock on it now. It routs once they add up to {@code breaksAt}
     * ({@link FieldRules#ROUT_LOSS} times its nerve), unless rallied.
     */
    public record Morale(double losses, double captain, double flanked, double rear, double fright, double army,
                         double breaksAt, State state) {
        public enum State {
            STEADY, RALLIED, ROUTED, SHATTERED
        }

        public double pressure() {
            return losses + captain + flanked + rear + fright + army;
        }

        /** How near it is to breaking, from 0 (steady) to 1 (it breaks). */
        public double risk() {
            return Math.max(0, Math.min(1, pressure() / breaksAt));
        }
    }

    /**
     * A block's morale now; null for one that never breaks (a lone general or standard-bearer, a besieged garrison) or
     * before its first second.
     */
    @Nullable
    public Morale moraleOf(FieldBlock block) {
        if (block.history == null) {
            return null;
        }
        return moraleOf(block, block.history[(seconds + 1) % FieldRules.ROUT_WINDOW], armyShock(armies[block.side]));
    }

    private Morale moraleOf(FieldBlock block, int before, double armyShock) {
        Morale.State state = block.shattered() ? Morale.State.SHATTERED : block.duty == Duty.ROUTED ? Morale.State.ROUTED
                : block.rallied(seconds) ? Morale.State.RALLIED : Morale.State.STEADY;
        return new Morale(before == 0 ? 0 : Math.max(0, (double) (before - block.alive) / before),
                seconds < block.shakenUntil ? FieldRules.CAPTAIN_SHOCK : 0,
                seconds < block.flankedUntil ? FieldRules.FLANK_SHOCK : 0,
                seconds < block.rearedUntil ? FieldRules.REAR_SHOCK : 0,
                seconds < block.frightUntil ? FieldRules.FRIGHT_SHOCK : 0,
                armyShock, FieldRules.ROUT_LOSS * block.arm.nerve(), state);
    }

    /** The shock of an army's losses (the shattered count as lost) on each of its blocks. */
    private static double armyShock(FieldArmy army) {
        return army.started == 0 ? 0 : FieldRules.ARMY_SHOCK * Math.max(0, 1 - (double) army.standing() / army.started);
    }

    /**
     * Each second, after the fighting: a block (foot, horse or archers) whose {@link Morale} adds up to its breaking
     * point routs, unless rallied. The enemy blocks fighting a block that routs or loses its captain rally.
     */
    private void morale(FieldArmy army, FieldArmy enemy) {
        if (siege != null && army.side == 1) {
            return; // the defenders of a siege never break: there is nowhere to go
        }
        double armyShock = armyShock(army);
        int slot = seconds % FieldRules.ROUT_WINDOW;
        for (FieldBlock block : army.blocks) {
            if (block.started() == 1) { // a lone general or standard-bearer: the general's fall ends the day anyway
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
            Morale m = moraleOf(block, before, armyShock);
            if (m.pressure() >= m.breaksAt() && !block.rallied(seconds)) {
                rout(army, block);
                rallyAgainst(enemy, block);
            }
        }
    }

    /**
     * How near a block is to breaking, from 0 (steady) to 1 (it routs at its next loss): its {@link Morale}. A rallied,
     * broken or beaten block is 0.
     */
    public double routRisk(FieldBlock block) {
        if (block.alive == 0 || block.rallied(seconds) || block.duty == Duty.ROUTED || block.duty == Duty.FLEEING
                || block.duty == Duty.WITHDRAWING) {
            return 0;
        }
        Morale m = moraleOf(block);
        return m == null ? 0 : m.risk();
    }

    /** Where each army's wings camp ({@link FieldArmy#LEFT}, CENTRE, RIGHT), or null: where its shattered blocks go. */
    private final Point[][] camps = new Point[2][3];

    /** An army's camp for a wing (its centre's for the whole army), built on the field: shattered blocks retreat there. */
    public void setCamp(int side, int wing, Point at) {
        camps[side][wing] = at;
    }

    @Nullable
    private Point campOf(FieldBlock block) {
        Point own = siege == null ? null : siege.campOf(block); // a besieger's: its own wall's force's camp
        if (own != null) {
            return own;
        }
        int wing = block.wing();
        Point camp = wing >= 0 && wing < 3 ? camps[block.side][wing] : null;
        return camp != null ? camp : camps[block.side][FieldArmy.CENTRE];
    }

    /** A block's rout frightens its army's blocks near it; an expendable block's rout, only other expendables. */
    private void frighten(FieldArmy army, FieldBlock routed) {
        for (FieldBlock b : army.blocks) {
            if (b != routed && b.alive > 0 && b.started() > 1 && (!routed.arm.expendable() || b.arm.expendable())
                    && b.pos.distanceTo(routed.pos) <= FieldRules.FRIGHT_REACH) {
                b.frightUntil = seconds + FieldRules.FRIGHT_SECONDS;
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
                e.frightUntil = 0;
            }
        }
    }

    /**
     * A block routs: it stops fighting and runs behind its army's reserves to regroup, frightening its own near it
     * ({@link #frighten}); shattered ({@link FieldBlock#shattered}), it retreats to its camp instead, if it has one. A
     * front block's place is taken at once by the next block of its file, if there is one.
     */
    void rout(FieldArmy army, FieldBlock block) {
        block.routs++;
        frighten(army, block);
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
        Point camp = block.shattered() ? campOf(block) : null;
        Point away = siege == null ? null : siege.away(block, FieldRules.ROUT_RUN); // a besieger runs out from its own wall
        block.line = camp != null ? camp : away != null ? away
                : BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, block.homeRight, army.rearBack + FieldRules.ROUT_RUN);
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
            if (e.pos.distanceTo(from) > reach || e.alive == 0 || e.duty == Duty.FLEEING || e.duty == Duty.BEARER || e.duty == Duty.GENERAL || !e.arm.infantry() && !e.arm.shoots()) {
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
     * (Decapitate), else the most soldiers near (Mass Casualties), else an exposed block (Precision Strikes), else the
     * nearest; spread over that block's unit ({@link #spreadOver}). Null, no enemy left.
     */
    @Nullable
    private FieldBlock choosePrey(FieldArmy army, FieldArmy enemy, FieldBlock block) {
        if (block.arm.mounted() && army.plan.has(Strategy.DECAPITATE) && enemy.commander != null && enemy.commander.alive > 0
                && enemy.commander.pos.distanceTo(block.pos) <= 60) {
            return enemy.commander;
        }
        FieldBlock e = army.plan.has(Strategy.MASS_CASUALTIES) ? mostSoldiers(enemy, block.pos, 60) : null;
        if (e == null && army.plan.has(Strategy.PRECISION_STRIKES)) {
            e = mostExposed(enemy, block.pos, 60);
            e = e != null && e.pos.distanceTo(block.pos) <= 60 ? e : null;
        }
        if (e == null) {
            e = nearestBlock(block, enemy, block.arm.mounted());
        }
        return e == null ? null : spreadOver(block, e, Double.MAX_VALUE);
    }

    /**
     * Which block of {@code foe}'s unit (within {@code reach} of {@code by}) {@code by} goes for: the blocks of its own
     * unit spread evenly over the foe's, left to right as both stand, so a unit falls on a whole unit in a wave instead
     * of every block turning on the one nearest.
     */
    private FieldBlock spreadOver(FieldBlock by, FieldBlock foe, double reach) {
        List<FieldBlock> foes = unit(armies[foe.side], foe, foe.pos);
        foes.removeIf(f -> f.duty == Duty.GENERAL || f.duty == Duty.BEARER || by.pos.distanceTo(f.pos) > reach);
        if (foes.size() < 2) {
            return foe;
        }
        FieldArmy army = armies[by.side];
        Comparator<FieldBlock> leftToRight = Comparator.comparingDouble(b -> b.pos.x() * -army.facingZ + b.pos.z() * army.facingX);
        List<FieldBlock> mine = unit(army, by, by.pos);
        foes.sort(leftToRight);
        mine.sort(leftToRight);
        int i = Math.max(0, mine.indexOf(by));
        return foes.get(i * foes.size() / Math.max(1, mine.size()));
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
        FieldBlock e = nearestBlock(block, enemy, false);
        return e != null && block.distanceTo(e) <= reach ? e : null;
    }

    @Nullable
    private static FieldBlock nearestBlock(FieldBlock block, FieldArmy enemy, boolean shooters) {
        if (enemy.grid != null) {
            if (shooters) {
                FieldBlock e = enemy.grid.nearest(block.pos, block.halfWidth(), b -> b.arm.shoots() && b.duty == Duty.SUPPORT, block::distanceTo);
                if (e != null) {
                    return e;
                }
            }
            return enemy.grid.nearest(block.pos, block.halfWidth(), b -> true, block::distanceTo);
        }
        FieldBlock best = null;
        double bestDistance = Double.MAX_VALUE;
        if (shooters) {
            for (FieldBlock e : enemy.blocks) {
                if (e.alive > 0 && e.arm.shoots() && e.duty == Duty.SUPPORT && closeEnough(block, block.pos, e, bestDistance)) {
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

    /** A ship turns at most this much a second (radians). */
    public static final double TURN_RATE = Math.toRadians(15);
    /** Coming round, a ship keeps at least this share of its way, so it sails round in an arc rather than turning where it lies. */
    public static final double MIN_WAY = 0.3;
    /** A place this close astern, a ship backs to rather than coming round to it. */
    public static final double ASTERN_WITHIN = 20;
    /** A ship this close to where it makes for is there: it stops, and comes round to lie as its captain wants. */
    public static final double AT_PLACE = 1.5;

    /**
     * One second of a ship that is only numbers sailing for {@code target}. A ship can't move sideways: it turns, at
     * most {@link #TURN_RATE} a second, and goes ahead along its heading, or astern at half speed. It comes round toward
     * its target as it goes, slower the further it has to turn, so it sails in an arc; a target close astern it backs to,
     * its stern toward it. There, it stops and comes round to the heading its captain wants. It sails round a ship of its
     * own side lying across its way ({@link Ramming#detour}), and where coming round would strike any of {@code ships}
     * (but those it is grappled to, hooking or going for) it backs astern instead, to where it has room to turn.
     */
    static void sail(FieldBlock b, Point target, List<FieldBlock> ships) {
        if (!b.lashed.isEmpty()) {
            return; // grappled (a rammer the second it strikes too): it holds fast while its soldiers board
        }
        List<Ramming.Hull> others = new ArrayList<>();
        List<Ramming.Hull> allies = new ArrayList<>();
        for (FieldBlock o : ships) {
            if (o != b && !b.lashed.contains(o) && o != b.hookedTo && o != b.prey) {
                others.add(footprint(o));
                if (o.side == b.side) {
                    allies.add(footprint(o));
                }
            }
        }
        Ramming.Hull me = footprint(b);
        double dx = target.x() - b.pos.x();
        double dz = target.z() - b.pos.z();
        double d = Math.hypot(dx, dz);
        boolean there = d <= AT_PLACE;
        double[] round = there ? null : Ramming.detour(me, target.x(), target.z(), allies);
        if (round != null) {
            dx = round[0] - b.pos.x();
            dz = round[1] - b.pos.z();
            d = Math.max(1e-6, Math.hypot(dx, dz));
        }
        double wantX = there ? b.settleX() : dx / d;
        double wantZ = there ? b.settleZ() : dz / d;
        boolean astern = !there && wantX * b.facingX + wantZ * b.facingZ < 0 && d <= ASTERN_WITHIN;
        if (astern) {
            wantX = -wantX; // its stern toward it
            wantZ = -wantZ;
        }
        double off = Math.atan2(b.facingX * wantZ - b.facingZ * wantX, b.facingX * wantX + b.facingZ * wantZ);
        if (Ramming.turnFouls(me, off, others)) {
            double back = shipSpeed(b) / 2; // no room to come round: astern, straight, until there is
            b.pos = b.pos.plus(-b.facingX * back, -b.facingZ * back);
            return;
        }
        double turn = Math.max(-TURN_RATE, Math.min(TURN_RATE, off));
        if (!there) {
            // Along its heading halfway through the turn: an arc.
            double[] mid = turned(b.facingX, b.facingZ, turn / 2);
            double pace = Math.min(astern ? shipSpeed(b) / 2 : shipSpeed(b), d) * Math.max(MIN_WAY, Math.cos(off));
            b.pos = b.pos.plus(mid[0] * pace * (astern ? -1 : 1), mid[1] * pace * (astern ? -1 : 1));
        }
        double[] heading = turned(b.facingX, b.facingZ, turn);
        b.facingX = heading[0];
        b.facingZ = heading[1];
    }

    /** A unit heading turned by {@code angle} radians (toward +z from +x). */
    private static double[] turned(double x, double z, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double nx = x * c - z * s;
        double nz = x * s + z * c;
        double length = Math.max(1e-9, Math.hypot(nx, nz));
        return new double[]{nx / length, nz / length};
    }

    /** Every block walks toward its line at its pace; one that has broken formation goes for its prey. */
    private void move(FieldArmy army) {
        FieldArmy enemy = armies[1 - army.side];
        List<FieldBlock> afloat = null;
        for (FieldBlock block : army.blocks) {
            if (block.alive == 0 || block.afloat && block != holder(block)) {
                continue; // aboard another's ship, it goes where the ship does (syncCrews)
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
                        : nearestBlock(block, enemy, block.arm.mounted());
                if (prey == null) {
                    continue;
                }
                if (!block.afloat) {
                    face(block, prey.pos); // a ship turns only as it sails (sail)
                }
                if (block.distanceTo(prey) <= FieldRules.CLOSE_REACH) {
                    continue;
                }
                FieldBlock blocking = block.contactFoe;
                if (blocking != null && blocking != prey && blocking.alive > 0 && blocking.duty != Duty.FLEEING) {
                    continue; // caught fighting whoever stood in the way: it can't ride through them
                }
                target = prey.pos;
            }
            if (block.afloat) {
                if (afloat == null) {
                    afloat = new ArrayList<>(); // every ship on the water, by the block sailing it, for room to turn and pass
                    for (FieldArmy a : armies) {
                        for (FieldBlock o : a.blocks) {
                            if (o.afloat && o.alive > 0 && o == holder(o)) {
                                afloat.add(o);
                            }
                        }
                    }
                }
                sail(block, target, afloat);
                continue;
            }
            double dx = target.x() - block.pos.x();
            double dz = target.z() - block.pos.z();
            double distance = Math.hypot(dx, dz);
            double step = block.afloat ? shipSpeed(block) : rates.speed(block.arm);
            if (siege != null) {
                step *= siege.pace(block);
            }
            if (pursuing && block.duty == Duty.FLEEING) {
                step *= FieldRules.FLEE_SPEED; // running for their lives, gear thrown down
            }
            Point next = distance <= step ? target : block.pos.plus(dx / distance * step, dz / distance * step);
            block.pos = siege == null ? next : siege.fence(block, next);
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
                FieldBlock foe = block.afloat && type == BattleType.OCEAN
                        ? boarded(block) // at sea, hand to hand only once grappled
                        : hunted(block.prey) && block.distanceTo(block.prey) <= reach ? block.prey : nearestWithin(block, enemy, reach);
                if (foe != null && siege != null && !siege.reaches(block, foe)) {
                    foe = null; // a wall between them: archers shoot instead
                }
                int swung = -1;
                if (block.body != null && (foe == null || foe.body != null)) {
                    // Bodies decide: it fights whom its soldiers struck this second, with as many as struck.
                    swung = swung(block);
                    foe = swung > 0 ? bodyFoe : null;
                }
                if (foe != null) {
                    block.contactSeconds++;
                    if (block.duty == Duty.FRONT) {
                        block.fightSeconds++;
                    }
                    block.contactFoe = foe;
                    switch (sideOf(foe, block)) { // blows on its flank or rear shake it (before real soldiers' blocks skip on)
                        case FLANK -> foe.flankedUntil = seconds + FieldRules.FLANK_LINGER;
                        case REAR -> foe.rearedUntil = seconds + FieldRules.FLANK_LINGER;
                        default -> {
                        }
                    }
                    double fighters = block.champion ? rates.warriorFightsAs() : swung >= 0 ? swung : block.alive;
                    if (block.live && foe.live && (type == BattleType.OCEAN || !block.afloat && !foe.afloat)) {
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
                    if (block.arm.mounted() && block.charged && !block.afloat) { // no charging horses from a deck
                        damage *= 1 + rates.chargeBonus() * block.momentum;
                    }
                    if (foe.braced && block.arm.mounted() && sideOf(foe, block) == FieldRules.Side.FRONT) {
                        damage *= FieldRules.BRACED_TAKES;
                    }
                    if (siege != null) {
                        damage *= siege.meleeFactor(block, foe);
                    }
                    if (block.arm.has("anti_horse") && foe.arm.mounted()) {
                        damage *= FieldRules.HALBERDS_VS_HORSE;
                    }
                    if (block.braced && foe.arm.mounted() && sideOf(block, foe) == FieldRules.Side.FRONT) {
                        damage *= FieldRules.BRACED_DEALS;
                    }
                    targets.add(foe);
                    damages.add(damage);
                    continue;
                }
                block.contactSeconds = 0;
                block.contactFoe = null;
                if (block.arm.shoots() && block.arrows > 0) {
                    shoot(army, block, enemy, targets, damages);
                }
                if (block.ship == FieldBlock.ShipKind.GUNSHIP && block.afloat && block == holder(block)) {
                    broadside(block, enemy, targets, damages);
                }
            }
        }
        for (int i = 0; i < targets.size(); i++) {
            FieldBlock target = targets.get(i);
            double damage = damages.get(i) * (resolute(target) ? FieldRules.RESOLVE_TAKES : 1);
            if (siege == null && type.walled() && target.side == 1 && pastWalls(target.pos) <= WALL_BAND) {
                damage *= WALL_TAKES;
            }
            if (damage > 0) {
                lastHurt[target.side] = seconds;
            }
            if (target.live) {
                target.liveDamage += damage; // dealt to the real soldiers by the live battle
            } else if ((target.body != null ? target.takeDamage(damage, random, struckWeights(target)) : target.takeDamage(damage, random)) > 0) {
                lastFall = seconds;
            }
        }
        if (bodies != null) {
            bodies.clearCounts();
        }
    }

    // ---- bodies: every soldier of a watched battle as a body (Bodies), Total War style ----

    /** The bodies of a watched battle's soldiers, or null: a battle nobody watches is fought as numbers alone. */
    @Nullable
    private Bodies bodies;
    /** Each body's block (by body index); bodies are never reused. */
    private final List<FieldBlock> bodyBlock = new ArrayList<>();
    private double originX;
    private double originZ;
    /** The enemy block most of a block's soldiers struck, as {@link #swung} last worked out. */
    @Nullable
    private FieldBlock bodyFoe;

    /**
     * One game tick of a watched battle: every soldier on land who is only numbers walks as a body to its place in its
     * block's ranks, pushing and being pushed, and swings at any enemy in reach. Real soldiers keep their own Recruits
     * AI and have no body. The first call starts the bodies; sieges and battles at sea stay numbers.
     * shortcut: runs on the server thread (3 ms a step for 10,000 soldiers in BodiesTest); move to a worker if it shows in profiles.
     */
    public void bodiesTick() {
        if (bodies == null) {
            if (siege != null || type == BattleType.OCEAN) {
                return;
            }
            bodies = new Bodies();
            bodies.lethal(false); // health is kept here, per block: bodies only say who reached whom
            Point o = armies[0].blocks.isEmpty() ? new Point(0, 0) : armies[0].blocks.get(0).pos;
            originX = o.x();
            originZ = o.z();
        }
        for (FieldArmy army : armies) {
            for (FieldBlock block : army.blocks) {
                embody(block);
            }
        }
        bodies.step();
    }

    /** Gives each of a block's living soldiers on land who is only numbers a body, and its goal: its place in the ranks. */
    private void embody(FieldBlock block) {
        if (block.alive == 0 || block.live || block.afloat) {
            if (block.body != null) {
                for (int b : block.body) {
                    if (b >= 0) {
                        bodies.remove(b);
                    }
                }
                block.body = null;
            }
            return;
        }
        if (block.body == null) {
            block.body = new int[block.health.length];
            java.util.Arrays.fill(block.body, -1);
        }
        Bodies.Kind kind = block.arm.mounted() ? Bodies.Kind.HORSE : Bodies.Kind.FOOT;
        float fx = (float) block.facingX;
        float fz = (float) block.facingZ;
        for (int i = 0; i < block.health.length; i++) {
            int b = block.body[i];
            if (block.health[i] <= 0) {
                if (b >= 0) {
                    bodies.remove(b);
                    block.body[i] = -1;
                }
                continue;
            }
            Point slot = BattlePlan.place(block.pos, block.facingX, block.facingZ, block.slotRight(i), block.slotBack(i));
            float x = (float) (slot.x() - originX);
            float z = (float) (slot.z() - originZ);
            if (b < 0) {
                block.body[i] = bodies.add(block.side, kind, x, z, fx, fz);
                bodyBlock.add(block);
            } else {
                bodies.goal(b, x, z, fx, fz);
            }
        }
    }

    /**
     * How many of a block's soldiers swung this second; {@link #bodyFoe} becomes the enemy block most of them went for.
     */
    private int swung(FieldBlock block) {
        int swung = 0;
        bodyFoe = null;
        java.util.Map<FieldBlock, Integer> foes = new java.util.HashMap<>();
        for (int b : block.body) {
            if (b < 0 || bodies.swings(b) == 0) {
                continue;
            }
            swung++;
            int t = bodies.target(b);
            if (t >= 0) {
                foes.merge(bodyBlock.get(t), 1, Integer::sum);
            }
        }
        int most = 0;
        for (var e : foes.entrySet()) {
            if (e.getValue() > most && e.getKey().alive > 0) {
                most = e.getValue();
                bodyFoe = e.getKey();
            }
        }
        return bodyFoe == null ? 0 : swung;
    }

    /** How many blows each of a block's soldiers took this second, for dealing its damage to those reached. */
    private int[] struckWeights(FieldBlock block) {
        int[] w = new int[block.health.length];
        for (int i = 0; i < w.length; i++) {
            w[i] = block.body[i] >= 0 ? bodies.struck(block.body[i]) : 0;
        }
        return w;
    }

    /**
     * Where each block with bodies is: where its soldiers stand, so a block pushed back is back, and one held up is
     * held. Its position is its front rank's middle, so that is the middle of its soldiers moved by their mean place.
     */
    private void placeFromBodies() {
        for (FieldArmy army : armies) {
            for (FieldBlock block : army.blocks) {
                if (block.body == null) {
                    continue;
                }
                double x = 0;
                double z = 0;
                double right = 0;
                double back = 0;
                int n = 0;
                for (int i = 0; i < block.body.length; i++) {
                    int b = block.body[i];
                    if (b >= 0 && bodies.alive(b)) {
                        x += bodies.x(b);
                        z += bodies.z(b);
                        right += block.slotRight(i);
                        back += block.slotBack(i);
                        n++;
                    }
                }
                if (n > 0) {
                    Point middle = new Point(x / n + originX, z / n + originZ);
                    block.pos = BattlePlan.place(middle, block.facingX, block.facingZ, -right / n, -back / n);
                }
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
                if (!block.arm.mounted() || block.alive == 0) {
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
                        double loss = foe.arm.mounted() ? FieldRules.MOMENTUM_LOSS_CAVALRY : FieldRules.MOMENTUM_LOSS_INFANTRY;
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

    /**
     * Archers shoot the nearest enemy in range, or the one with most soldiers (Mass Casualties). At sea, where no
     * generalship applies, an archer ship shoots what its captain marks ({@link #mark}).
     */
    private void shoot(FieldArmy army, FieldBlock archers, FieldArmy enemy, List<FieldBlock> targets, List<Double> damages) {
        Machine machine = archers.machine;
        double reach = machine != null ? machine.reach : FieldRules.ARCHER_RANGE;
        if (siege != null && siege.batters(archers)) {
            int shots = Math.min(archers.arrows, engineShots(archers));
            archers.arrows -= shots;
            siege.batter(archers, shots * machine.batter); // the wall first, then the men
            return;
        }
        FieldBlock led = archers.ledCharge ? archers.ledTarget : null; // a player's target comes first, once in range
        FieldBlock foe = led != null && led.alive > 0 && archers.pos.distanceTo(led.pos) <= reach ? led
                : type == BattleType.OCEAN
                ? (archers.prey != null && archers.prey.alive > 0 && archers.pos.distanceTo(archers.prey.pos) <= reach ? archers.prey : null)
                : army.plan.has(Strategy.MASS_CASUALTIES) ? mostSoldiers(enemy, archers.pos, reach) : null;
        if (foe == null) {
            FieldBlock[] nearest = {null};
            double[] best = {reach};
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
                enemy.grid.within(archers.pos, reach, consider);
            } else {
                enemy.blocks.forEach(consider);
            }
            foe = nearest[0];
        }
        if (foe == null) {
            return;
        }
        if (foe.afloat && archers.afloat) {
            foe = spread(archers, crew(foe)); // shooting at a ship: its unit's blocks take the arrows, the general last
        } else if (foe != led) {
            foe = spreadOver(archers, foe, reach); // a unit's shooters (Hwachas too) share out the enemy unit's blocks
        }
        int shots = (int) Math.min(archers.arrows, machine != null ? engineShots(archers)
                : Math.ceil(archers.alive * (archers.arm.missile() == Arm.Missile.SHOT ? FieldRules.SHOTS_PER_SECOND : FieldRules.ARROWS_PER_SECOND)));
        archers.arrows -= shots;
        if (shots > 0) {
            volleys.add(new Volley(archers, foe, shots, machine));
        }
        targets.add(foe);
        damages.add((machine != null ? shots * machine.damage * foe.arm.takes() // no shield stops shot or stone
                : archers.arm.missile() == Arm.Missile.SHOT ? shots * FieldRules.shotDamage(archers.arm, foe.arm)
                : shots * rates.arrowDamage() * FieldRules.arrowFactor(sideOf(foe, archers), foe.arm))
                * (siege == null ? 1 : siege.arrowFactor(foe)));
    }

    /** A train block's shots this second: its share of the unit's engines, as many as its crew still works, each once a reload, rounded at random. */
    private int engineShots(FieldBlock b) {
        double expected = b.alive / b.machine.crew / b.machine.reload;
        return (int) expected + (random.nextDouble() < expected % 1 ? 1 : 0);
    }

    /** Where an attack from {@code attacker} falls on {@code defender}. */
    private static FieldRules.Side sideOf(FieldBlock defender, FieldBlock attacker) {
        return FieldRules.side(defender.facingX, defender.facingZ,
                attacker.pos.x() - defender.pos.x(), attacker.pos.z() - defender.pos.z());
    }

    // ---- the end ----

    /** An army whose general falls retreats at once; one worn down to {@link BattlePlan#ROUT_SHARE} routs. */
    private void checkEnd() {
        if (siege != null) {
            siegeEnd();
            return;
        }
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
        boolean routA = BattlePlan.routs(armies[0].standing(), armies[0].started);
        boolean routB = BattlePlan.routs(armies[1].standing(), armies[1].started);
        if (routA && routB) {
            outcome = new Outcome(-1, "both armies broke", seconds);
        } else if (routA || routB) {
            int winner = routA ? 1 : 0;
            outcome = new Outcome(winner, "side " + winner + " wins; side " + (1 - winner) + " routs", seconds);
            flee(armies[1 - winner]);
        } else if (seconds >= BattlePlan.TIME_LIMIT_SECONDS) {
            outcome = type.walled() ? new Outcome(1, "the walls held (time limit)", seconds) : new Outcome(-1, "draw (time limit)", seconds);
        }
    }

    /**
     * A siege ends when the fortress is taken (a gate breached a minute, or every defender down), when the attackers'
     * general falls or their army routs, or when time runs out with the walls held. The defenders never withdraw: their
     * general's fall doesn't end it.
     */
    private void siegeEnd() {
        String taken = siege.taken();
        if (taken == null && armies[1].alive() == 0) {
            taken = "every defender fell";
        }
        if (taken != null) {
            outcome = new Outcome(0, "side 0 wins; " + taken, seconds);
            return;
        }
        if (armies[0].commander != null && armies[0].commander.alive == 0) {
            outcome = new Outcome(1, "side 0's general fell; side 0 retreats", seconds);
            flee(armies[0]);
        } else if (BattlePlan.routs(armies[0].standing(), armies[0].started)) {
            outcome = new Outcome(1, "side 1 wins; side 0 routs", seconds);
            flee(armies[0]);
        } else if (seconds >= BattlePlan.TIME_LIMIT_SECONDS) {
            outcome = new Outcome(1, "the walls held (time limit)", seconds);
        }
    }

    /**
     * Evening: the fighting stops and every block marches back to its army's camp, {@code back[side]} behind where it
     * deployed, in its place, facing the enemy again. From now on the blocks only march.
     */
    public void pullBack(double[] back) {
        pulledBack = true;
        if (siege != null) {
            siege.nightfall();
        }
        for (FieldArmy army : armies) {
            for (FieldBlock b : army.blocks) {
                stand(b);
                Point camp = siege == null ? null : siege.away(b, back[army.side]); // a besieger's, out from its own wall
                if (camp != null) {
                    b.line = camp;
                    continue;
                }
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
            if (siege != null && army.side == 1) {
                continue; // the defenders spent the night on and behind their walls
            }
            for (FieldBlock b : army.blocks) {
                Point camp = siege == null ? null : siege.away(b, back); // a besieger's, out from its own wall
                b.pos = camp != null ? camp : b.line.plus(-army.facingX * back, -army.facingZ * back);
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
            FieldBlock prey = nearestBlock(b, loser, false);
            if (prey != null) {
                b.line = prey.pos;
                face(b, prey.pos);
            }
        }
        for (FieldArmy army : armies) {
            move(army);
        }
        keepShipsApart(); // as in second(): else every pursuer sails into the one nearest fleeing ship
        syncCrews();
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
        b.contactSeconds = 0; // no longer in a fight: the sounds of battle die away
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
