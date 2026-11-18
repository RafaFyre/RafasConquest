package rafasconquest.battle;

import java.util.ArrayList;
import java.util.List;

/**
 * How an army of blocks of 20 and its command group deploy, and how its general decides, worked out on plain numbers
 * so it can be tested without a server. Positions are in the army's own frame: {@code right} along its battle line
 * (positive to the army's right), {@code back} behind its front (positive away from the enemy).
 * <p>
 * An army fights in lines: a front line of infantry blocks and relief lines behind it.
 * A front block that has lost half its soldiers is relieved by a fresh block from behind and withdraws to the rear;
 * that rotation is what makes a big battle last.
 */
public final class BattlePlan {
    public static final int SOLDIERS_PER_FORMATION = 20;
    /** A platoon is at most this many blocks, and this many soldiers ({@link #platoons}). */
    public static final int PLATOON_BLOCKS = 10;
    public static final int PLATOON_SOLDIERS = 200;
    /** Armies are this many soldiers at least and at most (whole blocks). */
    public static final int MIN_SOLDIERS = SOLDIERS_PER_FORMATION;
    public static final int MAX_SOLDIERS = 100_000;
    /** Ranks are this many soldiers wide, so a block of 20 stands 2 deep. */
    public static final int FILE_WIDTH = 10;
    /** Which soldier of a formation is its leader (its captain); it stands at {@link #captainPlace}. */
    public static final int LEADER_INDEX = FILE_WIDTH / 2 - 1;

    /**
     * The place in the ranks (counting along each rank from the front) where a block of {@code soldiers},
     * {@code files} wide, stands its captain: the middle of its back rank.
     */
    public static int captainPlace(int soldiers, int files) {
        int back = (soldiers - 1) / files * files;
        return back + (soldiers - back - 1) / 2;
    }
    /** Blocks between soldiers in a rank (Recruits' own formation spacing). */
    public static final double SPACING = 1.75;
    /** From one block's centre to the next in a line: a block's width and a gap. */
    public static final double BLOCK_PITCH = (FILE_WIDTH - 1) * SPACING + 3;
    /**
     * The front line is at most this many blocks wide; more infantry stands in more lines behind it. A big army
     * (over about 2000 soldiers) widens its front instead, to stand no more than {@link #LINES_DEEP} lines deep, up
     * to {@link #MAX_WIDE_FRONT} blocks. An army of more than {@link #WIDE_OVER} soldiers stands
     * {@link #WIDE_LINES_DEEP} lines deep, so each wing has a broad front of its own.
     */
    public static final int MAX_FRONT = 10;
    public static final int LINES_DEEP = 6;
    public static final int MAX_WIDE_FRONT = 40;
    public static final int WIDE_OVER = 1000;
    public static final int WIDE_LINES_DEEP = 3;
    /** From one line to the next behind it. */
    public static final double LINE_GAP = 16;
    /** Archers stand in rows this far behind the front line, and this far apart. */
    public static final double ARCHER_BACK = 4;
    /**
     * In a Great Shield the archers stand this far behind the stack's rear rank, and the blocks waiting in the files
     * this far behind the archers' front row: room for six rows of archers and a gap.
     */
    public static final double ARCHER_CLEARANCE = 3;
    public static final double ARCHER_BAND = 6 * 4 + 6;
    public static final double ARCHER_ROW = 4;
    /** Cavalry in reserve stands in rows this far apart; a wing with no front of its own is this far past the line's end. */
    public static final double CAVALRY_ROW = 8;
    public static final double CAVALRY_GAP = 12;
    /** Each army deploys this far from the battlefield's centre, so the fronts start twice this apart. */
    public static final double DEPLOY_DISTANCE = 30;
    /** An army of more than this many soldiers fights in a left wing, centre and right wing. */
    public static final int WINGS_OVER = 300;
    /** The wings of such an army stand side by side, this far apart. */
    public static final double WING_GAP = 3;
    /** From this many soldiers the wings stand wider apart: this far at first, up to MAX_WING_GAP at MAX_SOLDIERS. */
    public static final int WIDE_GAP_FROM = 10_000;
    public static final double WIDE_GAP = 35;
    public static final double MAX_WING_GAP = 500;
    /**
     * Archers stand at most this many rows deep; the reserves this far behind the archers, their lines this far apart
     * (rear rank to front rank); the cavalry this far behind the reserves, at most this many rows deep.
     */
    public static final int ARCHER_ROWS = 3;
    public static final double RESERVE_GAP = 24;
    public static final double RESERVE_ROW_GAP = 4;
    public static final double CAVALRY_GAP_BACK = 10;
    public static final int CAVALRY_ROWS = 3;
    /** A stacked front takes about this share of the infantry; the rest stands in reserve. */
    public static final double FRONT_SHARE = 0.7;

    /** Seconds the armies stand in their deployed lines before anyone moves. */
    public static final int FORMING_SECONDS = 5;
    /** How far a marching formation's line moves each second. */
    public static final double MARCH_STEP = 2;
    /** Infantry engages once an enemy is this close to its line... */
    public static final double INFANTRY_ENGAGE = 10;
    /** ...and presses on, keeping its ranks, until its front rank is this close to the enemy's, so the lines meet. */
    public static final double FRONT_CONTACT = 2;
    /** Archers halt this far from the nearest enemy and shoot (Recruits soldiers look for targets within 40). */
    public static final double ARCHER_RANGE = 30;
    /** Cavalry charges once an enemy comes this close, whatever else it was doing. */
    public static final double CAVALRY_THREAT = 20;
    /** A front block down to this share of its soldiers is relieved. */
    public static final double RELIEF_SHARE = 0.5;
    /** Only a block with at least this share of its soldiers goes forward to relieve another. */
    public static final double FIT_SHARE = 0.6;
    /** A relieving block takes over once it is this close to the spot, or after {@link #RELIEF_SECONDS}. */
    public static final double RELIEF_ARRIVED = 6;
    public static final int RELIEF_SECONDS = 20;
    /** An army whose soldiers fall to this share of its starting strength routs. */
    public static final double ROUT_SHARE = 0.2;
    /** The battle is called a draw after this long. */
    public static final int TIME_LIMIT_SECONDS = 1800;
    /**
     * Once fighting has begun, a battle where nobody has fallen for this long has stalled (what is left of the lines
     * can't reach each other): everyone left charges.
     */
    public static final int STALL_SECONDS = 30;

    /**
     * A unit card: a block's kind of soldier, read from its file in {@code mods/RafasConquest/units/arms} ({@link UnitCards}).
     * How it fights against the plain rates: how hard it {@code hits}, how much of the harm done it it {@code takes}, how
     * fast it goes ({@code pace}), and how much loss it stands before it routs ({@code nerve}, times
     * {@link FieldRules#ROUT_LOSS}). Its {@link Kind} and traits say how the battle uses it, so a new card needs no code.
     * <p>
     * The built-in cards below always exist (their files only change their numbers); a file of a new id adds a card.
     * {@link #ordinal} is its place in {@link #values}: the built-ins first, in the order city armies once saved them.
     */
    public static final class Arm {
        /** Where a card stands and how it fights: in the infantry's files, among the shooters, with the horse, or working an engine. */
        public enum Kind { FOOT, RANGED, MOUNTED, ENGINE }

        /** What a ranged card looses: arrows, or shot that shields don't stop. */
        public enum Missile { NONE, ARROWS, SHOT }

        private static final List<Arm> ALL = new ArrayList<>();
        private static Arm[] array = new Arm[0];

        public static final Arm LEVY = builtIn("levy");
        public static final Arm HEAVY_INFANTRY = builtIn("heavy_infantry");
        public static final Arm SKIRMISHERS = builtIn("skirmishers");
        public static final Arm ZWEIHANDLERS = builtIn("zweihandlers");
        public static final Arm ARCHERS = builtIn("archers");
        public static final Arm CAVALRY = builtIn("cavalry");
        public static final Arm HEAVY_CAVALRY = builtIn("heavy_cavalry");
        public static final Arm MUSKETEERS = builtIn("musketeers");
        /**
         * The siege train: crews working Siegeworks engines ({@link Machine}), each unit its own kind. Only the ram, tower
         * and mantlet stay under it; a unit given a shooting engine becomes that engine's own card ({@link #of}).
         */
        public static final Arm ENGINES = builtIn("engines");
        public static final Arm HALBERDIERS = builtIn("halberdiers");
        public static final Arm CULVERINS = builtIn("culverins");
        public static final Arm SERPENTINES = builtIn("serpentines");
        public static final Arm MONS_MEGS = builtIn("mons_megs");
        public static final Arm HWACHAS = builtIn("hwachas");
        public static final Arm ARCBALLISTAS = builtIn("arcballistas");
        public static final Arm ONAGERS = builtIn("onagers");
        public static final Arm TREBUCHETS = builtIn("trebuchets");
        public static final Arm TOWER_CROSSBOWS = builtIn("tower_crossbows");
        /** A gunship's crew: at sea they sail its ship card ({@link #ship}), on land they fight on foot. */
        public static final Arm GUNSHIP = builtIn("gunship");

        public final String id;
        private int ordinal;
        private boolean builtIn;
        String name = "";
        Kind kind = Kind.FOOT;
        double hits = 1;
        double takes = 1;
        double pace = 1;
        double nerve = 1;
        java.util.Set<String> traits = java.util.Set.of();
        Missile missile = Missile.NONE;
        /** Its soldiers' entity (a Villager Recruits one). */
        String recruit = "recruits:recruit";
        /** Epic Knights' kit it wears where that is installed ({@code rafasconquest.gear.Armoury.Kit}, lower case). */
        String kit = "archer";
        /** Its vanilla gear: head, chest, legs, feet, main hand, off hand (item ids, "banner_shield", "musket"; "" none). */
        String[] gear = new String[6];
        /** How it is drawn far away ({@link FormationSnapshot.Kind}, lower case). */
        String figure = "skirmishers";
        /** The item its card shows (muster shop, army cards). */
        String icon = "minecraft:iron_sword";
        /** In its kind, the order cards take their places from the front: lowest first. */
        int front;
        /** A mod it needs, and the card that fights in its place without it ("" none). */
        String requires = "";
        String otherwise = "";
        boolean roster = true;
        String engine = "";
        /** The ship card ({@link rafasconquest.core.SeaLanes.Size}) its unit sails at sea; "" the usual ones. */
        String ship = "";

        Arm(String id) {
            this.id = id;
        }

        private static Arm builtIn(String id) {
            Arm a = add(id);
            a.builtIn = true;
            UnitCards.readBuiltIn(a);
            return a;
        }

        /** The card {@code id}, made (with the plain rates, after every other) if there is none yet. */
        static Arm add(String id) {
            Arm a = byId(id);
            if (a == null) {
                a = new Arm(id);
                a.ordinal = ALL.size();
                ALL.add(a);
                array = ALL.toArray(Arm[]::new);
            }
            return a;
        }

        /** Drops every card but the built-in ones, before another's set is added in its order ({@link UnitCards#replace}). */
        static void keepBuiltIns() {
            ALL.removeIf(a -> !a.builtIn);
            array = ALL.toArray(Arm[]::new);
        }

        /** Every card, by ordinal. A copy: don't hold it across a reload. */
        public static Arm[] values() {
            return array.clone();
        }

        /** The card at {@code ordinal}; null if there is none. */
        @org.jetbrains.annotations.Nullable
        public static Arm get(int ordinal) {
            return ordinal >= 0 && ordinal < array.length ? array[ordinal] : null;
        }

        @org.jetbrains.annotations.Nullable
        public static Arm byId(String id) {
            for (Arm a : array) {
                if (a.id.equals(id)) {
                    return a;
                }
            }
            return null;
        }

        public int ordinal() {
            return ordinal;
        }

        public boolean builtIn() {
            return builtIn;
        }

        /** Its id in upper case, as the enum it once was named it. */
        public String name() {
            return id.toUpperCase(java.util.Locale.ROOT);
        }

        /** Its name for people: its file's, else its id in words. */
        public String title() {
            return name.isEmpty() ? Character.toUpperCase(id.charAt(0)) + id.substring(1).replace('_', ' ') : name;
        }

        @Override
        public String toString() {
            return name();
        }

        public Kind kind() {
            return kind;
        }

        public boolean has(String trait) {
            return traits.contains(trait);
        }

        public Missile missile() {
            return missile;
        }

        public String recruit() {
            return recruit;
        }

        public String kit() {
            return kit;
        }

        /** Its vanilla gear in slot {@code i}: head, chest, legs, feet, main hand, off hand; "" for none. */
        public String gear(int i) {
            return gear[i] == null ? "" : gear[i];
        }

        public String icon() {
            return icon;
        }

        public String figure() {
            return figure;
        }

        public int front() {
            return front;
        }

        /** The card fighting in its place: its {@code otherwise} while the mod it {@code requires} isn't installed. */
        public Arm fielded() {
            Arm instead = requires.isEmpty() || installed(requires) ? null : byId(otherwise);
            return instead == null || instead == this ? this : instead.fielded();
        }

        /** The engine this card works, for an engine's own card; null otherwise (the siege train's crews get theirs in battle). */
        @org.jetbrains.annotations.Nullable
        public Machine machine() {
            return engine.isEmpty() ? null : Machine.byId(engine);
        }

        /** The ship card its unit sails at sea; null for the usual ones (a unit's ship, the camp's). */
        @org.jetbrains.annotations.Nullable
        public rafasconquest.core.SeaLanes.Size ship() {
            return ship.isEmpty() ? null : rafasconquest.core.SeaLanes.Size.byId(ship);
        }

        /** Crews working an engine: the siege train, or an engine's own card. */
        public boolean engine() {
            return kind == Kind.ENGINE;
        }

        /** The card of crews working {@code m}: its own card, or the siege train for the ram, tower and mantlet. */
        public static Arm of(Machine m) {
            for (Arm a : array) {
                if (m != null && a.machine() == m) {
                    return a;
                }
            }
            return ENGINES;
        }

        /** On the general roster (city army cards). */
        public boolean roster() {
            return roster;
        }

        public double hits() {
            return hits;
        }

        public double takes() {
            return takes;
        }

        public double pace() {
            return pace;
        }

        public double nerve() {
            return nerve;
        }

        /** Expendable, as in Total War: its rout frightens no one but other expendables. */
        public boolean expendable() {
            return has("expendable");
        }

        /** Foot that fights hand to hand, in files. */
        public boolean infantry() {
            return kind == Kind.FOOT;
        }

        public boolean mounted() {
            return kind == Kind.MOUNTED;
        }

        /** Shoots from behind the front: archers, musketeers and the siege train. */
        public boolean shoots() {
            return kind == Kind.RANGED || kind == Kind.ENGINE;
        }

        /** Bears shields: takes less from the front ({@link FieldRules#SHIELD_MELEE}, {@link FieldRules#SHIELD_ARROWS}). */
        public boolean shielded() {
            return has("shielded");
        }
    }

    /** Mods by id that cards {@code require}, beyond the three below; set at startup (tests: all there). */
    public static java.util.function.Predicate<String> mods = mod -> true;

    /** Whether the mod {@code id} is installed, for cards that need one. */
    public static boolean installed(String id) {
        return switch (id) {
            case "magistuarmory" -> zweihandlers;
            case "musketmod" -> muskets;
            case "siegeworks" -> siegeworks;
            default -> mods.test(id);
        };
    }

    /** Whether Epic Knights is there to arm zweihandlers; without it their share is skirmishers. Set at startup. */
    public static boolean zweihandlers = true; // shortcut: one global for the game, since every battle shares the mods

    /** Whether Musket Mod is installed (Recruits' crossbowmen fire its muskets). Set at startup. */
    public static boolean muskets = false;

    /**
     * Whether armies field musketeers: Musket Mod is there and the world's nations are in the Age of Gunpowder or later.
     * shortcut: every nation is in the world's first age until nations research their own (tech tree stage 3); then
     * this becomes per army.
     */
    public static boolean musketeers() {
        return muskets && rafasconquest.core.WorldFeatures.ages().first().compareTo(rafasconquest.core.Age.GUNPOWDER) >= 0;
    }

    /** Whether Siegeworks is installed. Set at startup. */
    public static boolean siegeworks = false;

    /** Whether armies bring a siege train: Siegeworks is there and the world is in the Age of Steel or later (the same shortcut). */
    public static boolean engines() {
        return siegeworks && rafasconquest.core.WorldFeatures.ages().first().compareTo(rafasconquest.core.Age.STEEL) >= 0;
    }

    /** The siege train's crews, on top of the army: this share of its headcount. */
    static final double TRAIN_SHARE = 0.05;

    /**
     * Of every army, by headcount: levy, heavy infantry, skirmishers, zweihandlers, halberdiers (the infantry), archers,
     * cavalry, heavy cavalry. With musketeers, they take {@link #MUSKETEER_SHARE}: half from the levy, half from the archers.
     */
    static final double LEVY_SHARE = 0.25;
    static final double HEAVY_SHARE = 0.15;
    static final double SKIRMISHER_SHARE = 0.10;
    static final double ZWEIHANDLER_SHARE = 0.05;
    static final double HALBERDIER_SHARE = 0.10;
    static final double ARCHER_SHARE = 0.20;
    static final double CAVALRY_SHARE = 0.10;
    static final double HEAVY_CAVALRY_SHARE = 0.05;
    static final double MUSKETEER_SHARE = 0.10;
    private static final double[] HORSE_SHARES = {CAVALRY_SHARE, HEAVY_CAVALRY_SHARE};

    private static double[] footShares(boolean musketeers) {
        return new double[]{LEVY_SHARE - (musketeers ? MUSKETEER_SHARE / 2 : 0), HEAVY_SHARE, SKIRMISHER_SHARE, ZWEIHANDLER_SHARE, HALBERDIER_SHARE};
    }

    /** The shooters: archers, musketeers, then the siege train. */
    private static double[] shotShares(boolean musketeers, boolean engines) {
        return new double[]{ARCHER_SHARE - (musketeers ? MUSKETEER_SHARE / 2 : 0), musketeers ? MUSKETEER_SHARE : 0, engines ? TRAIN_SHARE : 0};
    }

    /**
     * Deals {@code totals[k]} blocks of each kind out to the wings, each wing taking {@code capacity[w]} in all: each
     * block to the wing furthest short of its fair part of that kind. Returns [wing][kind].
     */
    static int[][] deal(int[] totals, int[] capacity) {
        int all = 0;
        for (int c : capacity) {
            all += c;
        }
        int[][] out = new int[capacity.length][totals.length];
        int[] left = capacity.clone();
        for (int k = 0; k < totals.length; k++) {
            for (int i = 0; i < totals[k]; i++) {
                int best = -1;
                double shortest = -Double.MAX_VALUE;
                for (int w = 0; w < capacity.length; w++) {
                    double shortBy = (double) totals[k] * capacity[w] / Math.max(1, all) - out[w][k];
                    if (left[w] > 0 && shortBy > shortest) {
                        best = w;
                        shortest = shortBy;
                    }
                }
                out[best][k]++;
                left[best]--;
            }
        }
        return out;
    }

    /**
     * Where a block stands when the army deploys, in the army's frame (the centre of its front rank). Infantry stands
     * in {@code line} 0 (the front), 1, 2... behind it, at {@code file} along the line. Archers and cavalry have line
     * -1. {@code files} is how many soldiers wide its ranks are; {@code wing} which wing it stands in (0 left, 1
     * centre, 2 right; all centre in an army too small for wings); {@code band} which part of the wing (front,
     * archers, reserves, cavalry).
     */
    public record Slot(Arm arm, double right, double back, int line, int file, int files, int wing, int band) {
    }

    /** The parts of a wing, front to back, as {@link Slot#band()} numbers them. */
    public static final int BAND_FRONT = 0;
    public static final int BAND_ARCHERS = 1;
    public static final int BAND_RESERVES = 2;
    public static final int BAND_CAVALRY = 3;

    /**
     * Gathers an army's blocks into platoons, each of one arm only: in each wing, its front, archers, reserves and
     * cavalry apart, and within those each arm apart, whole files
     * (blocks of the arm standing one behind another) side by side from its left, each platoon at most {@link #PLATOON_BLOCKS}
     * blocks and {@link #PLATOON_SOLDIERS} soldiers. Returns each slot's platoon, numbered from 0.
     */
    public static int[] platoons(List<Slot> slots) {
        int most = Math.min(PLATOON_BLOCKS, PLATOON_SOLDIERS / SOLDIERS_PER_FORMATION);
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < slots.size(); i++) {
            order.add(i);
        }
        order.sort(java.util.Comparator.comparingInt((Integer i) -> slots.get(i).wing()).thenComparingInt(i -> slots.get(i).band())
                .thenComparingInt(i -> slots.get(i).arm().ordinal())
                .thenComparingDouble(i -> slots.get(i).right()).thenComparingDouble(i -> slots.get(i).back()));
        int[] platoon = new int[slots.size()];
        int next = -1;
        int size = 0;
        Slot last = null;
        for (int k = 0; k < order.size(); ) {
            Slot s = slots.get(order.get(k));
            int end = k + 1; // the file starting here
            while (end < order.size() && sameFile(s, slots.get(order.get(end)))) {
                end++;
            }
            if (last == null || last.wing() != s.wing() || last.band() != s.band() || last.arm() != s.arm() || size + (end - k) > most) {
                next++;
                size = 0;
            }
            for (; k < end; k++) {
                if (size == most) { // a file deeper than a platoon is cut
                    next++;
                    size = 0;
                }
                platoon[order.get(k)] = next;
                size++;
            }
            last = s;
        }
        return platoon;
    }

    private static boolean sameFile(Slot a, Slot b) {
        return a.wing() == b.wing() && a.band() == b.band() && a.arm() == b.arm() && Math.abs(a.right() - b.right()) < 0.5;
    }

    /** The wings, as {@link Slot#wing()} numbers them. */
    public static final int LEFT = 0;
    public static final int CENTRE = 1;
    public static final int RIGHT = 2;

    /** How many blocks of 20 an army of {@code soldiers} is: the nearest whole number, within the limits. */
    public static int blocks(int soldiers) {
        int clamped = Math.max(MIN_SOLDIERS, Math.min(MAX_SOLDIERS, soldiers));
        return Math.max(1, Math.round(clamped / (float) SOLDIERS_PER_FORMATION));
    }

    /** How an army of {@code blocks} blocks deploys in plain lines ({@link #deployment(int, boolean)}). */
    public static List<Slot> deployment(int blocks) {
        return deployment(blocks, false);
    }

    /** From one block's centre to the next in a line, for blocks {@code files} wide. */
    public static double pitch(int files) {
        return (files - 1) * SPACING + 3;
    }

    /**
     * How far behind the front block of its file the block {@code place} places back stands. In a front of single
     * blocks ({@code stackFiles} 0) {@link #LINE_GAP} apart. In a stacked front (a Great Shield, or a big army), whose
     * blocks are {@code stackFiles} wide, the first {@link #SHIELD_STACK} closed up one behind the other, the rest
     * further back (the reserves keep the places they deployed to; this is where they would close up to).
     */
    public static double fileBack(int place, int stackFiles) {
        if (stackFiles == 0) {
            return place * LINE_GAP;
        }
        if (place < SHIELD_STACK) {
            return place * stackPitch(stackFiles);
        }
        return archerBack(stackFiles, SHIELD_STACK) + ARCHER_BAND + (place - SHIELD_STACK) * LINE_GAP;
    }

    /** The same for a Great Shield's blocks, or a front of single blocks. */
    public static double fileBack(int place, boolean greatShield) {
        return fileBack(place, greatShield ? SHIELD_FILES : 0);
    }

    /** From one block's front rank to the next's in a stacked file: its ranks and a rank's gap. */
    public static double stackPitch(int files) {
        return (double) SOLDIERS_PER_FORMATION / files * SPACING;
    }

    /**
     * How far behind the front archers stand, at the least: {@link #ARCHER_BACK}, or behind a stacked front (blocks
     * {@code stackFiles} wide) clear of its whole stack (its last block's rear rank, and a gap).
     */
    public static double archerBack(int stackFiles, int depth) {
        if (stackFiles == 0 || depth <= 1) {
            return stackFiles == 0 ? ARCHER_BACK : (SOLDIERS_PER_FORMATION / stackFiles - 1) * SPACING + ARCHER_CLEARANCE;
        }
        double stackRear = (depth - 1) * stackPitch(stackFiles) + (SOLDIERS_PER_FORMATION / stackFiles - 1) * SPACING;
        return stackRear + ARCHER_CLEARANCE;
    }

    /** The same for a Great Shield, or a front of single blocks. */
    public static double archerBack(boolean greatShield) {
        return archerBack(greatShield ? SHIELD_FILES : 0, SHIELD_STACK);
    }

    /**
     * How many blocks deep each front file stands (the conveyor that rotates its blocks): 3 in a Great Shield or an
     * army of more than {@link #WIDE_OVER} soldiers, 2 in one of more than {@link #WINGS_OVER}, else 1 (no conveyor).
     */
    public static int stackDepth(int blocks, boolean greatShield) {
        int soldiers = blocks * SOLDIERS_PER_FORMATION;
        return greatShield || soldiers > WIDE_OVER ? SHIELD_STACK : soldiers > WINGS_OVER ? 2 : 1;
    }

    /** How wide the blocks of a stacked front are, or 0 if the army's front is single blocks. */
    public static int stackFiles(int blocks, boolean greatShield) {
        return greatShield ? SHIELD_FILES : stackDepth(blocks, false) > 1 ? FILE_WIDTH : 0;
    }

    /** How an army of {@code blocks} blocks deploys, its troops shared evenly between its wings. */
    public static List<Slot> deployment(int blocks, boolean greatShield) {
        return deployment(blocks, greatShield, new double[]{1.0 / 3, 1.0 / 3, 1.0 / 3});
    }

    /**
     * How an army of {@code blocks} blocks deploys: a fifth archers, a fifth cavalry (rounded), the rest infantry.
     * <p>
     * An army of more than {@link #WINGS_OVER} soldiers fights as three armies side by side: a left wing, a centre
     * and a right wing, sharing all its troops by {@code wingShares} {left, centre, right} ({@link WarPlan}), with
     * {@link #wingGap} between them. A smaller army is all centre. Each stands alike, front to back:
     * <ul>
     *   <li>its front, at least as wide as its archers' rows. In a Great Shield, or an army of more than
     *   {@link #WINGS_OVER} soldiers, each front file is {@link #stackDepth} blocks closed up one behind the other,
     *   about {@link #FRONT_SHARE} of its infantry; else a single line at most {@link #MAX_FRONT} wide. The army's
     *   front is at most {@link #maxFront} files, shared between its wings;</li>
     *   <li>its archers, in at most {@link #ARCHER_ROWS} rows right behind the front, which covers them;</li>
     *   <li>its reserves, the rest of its infantry, {@link #RESERVE_GAP} further back in a few centred lines;</li>
     *   <li>its cavalry, in at most {@link #CAVALRY_ROWS} centred rows behind the reserves.</li>
     * </ul>
     * Wedges are formed later, once the battle's front line is established ({@link FieldBattle}). In a Great Shield
     * every infantry block bears shields and stands {@link #SHIELD_FILES} wide and five deep.
     */
    public static List<Slot> deployment(int blocks, boolean greatShield, double[] wingShares) {
        return deployment(blocks, greatShield, wingShares, false);
    }

    /**
     * The same, the levies up front ({@code levyFront}: the general wants the front to fail, crushing them and drawing the
     * enemy on) or behind the heavy infantry, supporting it. A Great Shield's front is all heavy infantry.
     */
    public static List<Slot> deployment(int blocks, boolean greatShield, double[] wingShares, boolean levyFront) {
        return deployment(blocks, greatShield, wingShares, levyFront, 0);
    }

    /**
     * The same, its front at most {@code maxFiles} files wide (0: as wide as its size allows) and then one body, without
     * wings: a siege's attackers, before the one wall they storm, rather than spread across a field.
     */
    public static List<Slot> deployment(int blocks, boolean greatShield, double[] wingShares, boolean levyFront, int maxFiles) {
        return deployment(blocks, greatShield, wingShares, levyFront, maxFiles, null);
    }

    /**
     * The same, of exactly {@code mix} blocks of each arm (by ordinal: a grand strategy army's cards) rather than the
     * usual shares; null: the shares. With a mix, {@code blocks} is its sum.
     */
    public static List<Slot> deployment(int blocks, boolean greatShield, double[] wingShares, boolean levyFront, int maxFiles,
                                        @org.jetbrains.annotations.Nullable int[] mix) {
        if (mix != null) {
            // Each card by its kind: foot in the files, shooters (then engines) behind them, horse at the back.
            List<Arm> footTypes = new ArrayList<>();
            List<Arm> shotTypes = new ArrayList<>();
            List<Arm> horseTypes = new ArrayList<>();
            for (Arm a : Arm.values()) {
                if (a.ordinal() < mix.length && mix[a.ordinal()] > 0) {
                    (a.infantry() ? footTypes : a.mounted() ? horseTypes : shotTypes).add(a);
                }
            }
            return deployment(blocks, greatShield, wingShares, levyFront, maxFiles, footTypes, counts(footTypes, mix),
                    shotTypes, counts(shotTypes, mix), horseTypes, counts(horseTypes, mix));
        }
        boolean musketeers = musketeers();
        int archers = Math.round(blocks * (float) (ARCHER_SHARE + (musketeers ? MUSKETEER_SHARE / 2 : 0))); // archers and musketeers
        int cavalry = Math.round(blocks * (float) (CAVALRY_SHARE + HEAVY_CAVALRY_SHARE));
        int infantry = blocks - archers - cavalry;
        while (infantry < 1) {
            if (cavalry > 0) {
                cavalry--;
            } else {
                archers--;
            }
            infantry++;
        }
        boolean engines = engines();
        archers += engines ? Math.round(blocks * (float) TRAIN_SHARE) : 0; // the siege train, on top, stands with them
        // Each kind shared out army-wide first, so the army holds its exact shares, then dealt out to the wings.
        return deployment(blocks, greatShield, wingShares, levyFront, maxFiles,
                List.of(Arm.LEVY, Arm.HEAVY_INFANTRY, Arm.SKIRMISHERS, Arm.ZWEIHANDLERS, Arm.HALBERDIERS), share(infantry, footShares(musketeers), 0),
                List.of(Arm.ARCHERS, Arm.MUSKETEERS, Arm.ENGINES), share(archers, shotShares(musketeers, engines), 0),
                List.of(Arm.CAVALRY, Arm.HEAVY_CAVALRY), share(cavalry, HORSE_SHARES, 0));
    }

    private static int[] counts(List<Arm> types, int[] mix) {
        return types.stream().mapToInt(a -> mix[a.ordinal()]).toArray();
    }

    /**
     * Lays out an army of {@code foot[k]} blocks of each of {@code footTypes} (infantry), {@code shot[k]} of each of
     * {@code shotTypes} (shooters and engines) and {@code horse[k]} of each of {@code horseTypes} (cavalry).
     */
    private static List<Slot> deployment(int blocks, boolean greatShield, double[] wingShares, boolean levyFront, int maxFiles,
                                         List<Arm> footTypes, int[] foot, List<Arm> shotTypes, int[] shot, List<Arm> horseTypes, int[] horse) {
        int infantry = java.util.Arrays.stream(foot).sum();
        int archers = java.util.Arrays.stream(shot).sum();
        int cavalry = java.util.Arrays.stream(horse).sum();
        int soldiers = blocks * SOLDIERS_PER_FORMATION;
        boolean winged = maxFiles == 0 && soldiers > WINGS_OVER && infantry >= 3;
        double[] shares = winged ? wingShares : new double[]{0, 1, 0};
        int[] wingInfantry = share(infantry, shares, winged ? 1 : 0);
        int[] wingArchers = share(archers, shares, 0);
        int[] wingCavalry = share(cavalry, shares, 0);
        int[][] wingFoot = deal(foot, wingInfantry);
        int[][] wingShot = deal(shot, wingArchers);
        int[][] wingHorse = deal(horse, wingCavalry);
        int stackFiles = stackFiles(blocks, greatShield);
        int depth = stackDepth(blocks, greatShield);
        int files = greatShield ? SHIELD_FILES : FILE_WIDTH;
        double pitch = pitch(files);
        double frontCap = maxFiles > 0 ? Math.min(maxFront(soldiers), maxFiles) : maxFront(soldiers);

        // Each wing laid out around its own middle, in order left, centre, right; files numbered left to right.
        List<List<Slot>> wings = new ArrayList<>();
        double[] widths = new double[3];
        int firstFile = 0;
        for (int wing = 0; wing < 3; wing++) {
            List<Slot> slots = new ArrayList<>();
            if (wingInfantry[wing] + wingArchers[wing] + wingCavalry[wing] > 0) {
                int cap = Math.max(1, (int) Math.round(frontCap * (winged ? shares[wing] : 1)));
                widths[wing] = layoutWing(slots, wing, firstFile, foot(footTypes, wingFoot[wing], levyFront), behind(shotTypes, wingShot[wing]),
                        behind(horseTypes, wingHorse[wing]), greatShield, stackFiles, depth, files, pitch, cap);
                firstFile += (int) slots.stream().filter(s -> s.line() == 0).count();
            }
            wings.add(slots);
        }
        double gap = winged ? wingGap(soldiers) : 0;
        double total = widths[LEFT] + widths[CENTRE] + widths[RIGHT] + (winged ? 2 * gap : 0);
        double[] middle = {-total / 2 + widths[LEFT] / 2, -total / 2 + widths[LEFT] + gap + widths[CENTRE] / 2, total / 2 - widths[RIGHT] / 2};
        if (!winged) {
            middle[CENTRE] = 0;
        }
        List<Slot> all = new ArrayList<>(blocks);
        // Front blocks first (every wing's), then the rest: the battle builds its front line from the first.
        for (int pass = 0; pass < 2; pass++) {
            for (int wing = 0; wing < 3; wing++) {
                for (Slot s : wings.get(wing)) {
                    if ((s.line() == 0) == (pass == 0)) {
                        all.add(new Slot(s.arm(), s.right() + middle[wing], s.back(), s.line(), s.file(), s.files(), s.wing(), s.band()));
                    }
                }
            }
        }
        return all;
    }

    /**
     * Lays out one wing (or a small army) around its own middle: front, archers, reserves, cavalry. Its front files
     * are numbered from {@code firstFile}. Returns how wide it stands.
     */
    private static double layoutWing(List<Slot> slots, int wing, int firstFile, List<Arm> foot, List<Arm> shot, List<Arm> horse,
                                     boolean greatShield, int stackFiles, int depth, int files, double pitch, int frontCap) {
        int infantry = foot.size();
        int archers = shot.size();
        int cavalry = horse.size();
        int next = 0; // the next of foot to take its place, front rank first
        int archersPerRow = Math.max(1, (archers + ARCHER_ROWS - 1) / ARCHER_ROWS);
        int cover = archers == 0 ? 1 : (int) Math.ceil(archersPerRow * BLOCK_PITCH / pitch);
        int width;
        int frontRows;
        if (infantry == 0) {
            width = 0;
            frontRows = 0;
        } else if (stackFiles > 0) {
            int share = (int) Math.ceil(infantry * FRONT_SHARE / depth);
            width = Math.max(1, Math.min(Math.min(frontCap, Math.max(cover, share)), Math.max(1, infantry / depth)));
            frontRows = Math.max(1, Math.min(depth, infantry / width));
        } else {
            int plain = infantry <= 4 ? infantry : Math.max(Math.min(MAX_FRONT, (infantry + 1) / 2), (infantry + LINES_DEEP - 1) / LINES_DEEP);
            width = Math.max(1, Math.min(infantry, Math.min(frontCap, Math.max(plain, cover))));
            frontRows = 1;
        }
        double blockDepth = (SOLDIERS_PER_FORMATION / files - 1) * SPACING;
        double[] fileRight = new double[width];
        for (int file = 0; file < width; file++) {
            fileRight[file] = across(file, width, pitch);
        }
        for (int row = 0; row < frontRows; row++) {
            for (int file = 0; file < width; file++) {
                Arm arm = foot.get(next++);
                slots.add(new Slot(greatShield ? Arm.HEAVY_INFANTRY : arm, fileRight[file], fileBack(row, stackFiles), row,
                        firstFile + file, files, wing, BAND_FRONT));
            }
        }
        double frontRear = frontRows == 0 ? 0 : fileBack(frontRows - 1, stackFiles) + blockDepth;
        double widest = width * pitch;

        // Archers (then the musketeers, then the siege train): in rows right behind the front, which covers them (more rows
        // if the front can't widen enough).
        if (width > 0) {
            archersPerRow = Math.max(1, Math.min(archersPerRow, (int) (width * pitch / BLOCK_PITCH)));
        }
        double archersBack = frontRows == 0 ? 0 : archerBack(stackFiles, frontRows);
        int archerRows = archers == 0 ? 0 : (archers + archersPerRow - 1) / archersPerRow;
        for (int i = 0; i < archers; i++) {
            int row = i / archersPerRow;
            int inRow = Math.min(archersPerRow, archers - row * archersPerRow);
            slots.add(new Slot(shot.get(i), across(i % archersPerRow, inRow, BLOCK_PITCH), archersBack + row * ARCHER_ROW, -1, i,
                    FILE_WIDTH, wing, BAND_ARCHERS));
        }
        widest = Math.max(widest, Math.min(archers, archersPerRow) * BLOCK_PITCH);
        double archersRear = archerRows == 0 ? frontRear
                : archersBack + (archerRows - 1) * ARCHER_ROW + (SOLDIERS_PER_FORMATION / FILE_WIDTH - 1) * SPACING;

        // Reserves: the rest of the infantry, after a wide gap, in lines of one width, centred; each joins the file of
        // the front block it stands behind.
        int reserves = infantry - width * frontRows;
        int reserveLines = reserves == 0 ? 0 : (reserves + width - 1) / width;
        int perLine = reserveLines == 0 ? 0 : (reserves + reserveLines - 1) / reserveLines;
        double reservesBack = Math.max(frontRear, archersRear) + RESERVE_GAP;
        for (int i = 0; i < reserves; i++) {
            int line = i / perLine;
            int inLine = Math.min(perLine, reserves - line * perLine);
            double right = across(i % perLine, inLine, pitch);
            int file = firstFile + nearestFile(right, fileRight);
            slots.add(new Slot(foot.get(next++), right, reservesBack + line * (blockDepth + RESERVE_ROW_GAP),
                    frontRows + line, file, files, wing, BAND_RESERVES));
        }
        double reservesRear = reserveLines == 0 ? Math.max(frontRear, archersRear)
                : reservesBack + (reserveLines - 1) * (blockDepth + RESERVE_ROW_GAP) + blockDepth;

        // Cavalry: behind the reserves, centred, in rows no wider than the front (more rows rather than wider ones), so
        // the whole formation stands as one rectangle; at least CAVALRY_ROWS deep's worth of width for a narrow front.
        double cavalryBack = (reserveLines == 0 ? reservesRear + RESERVE_GAP : reservesRear) + CAVALRY_GAP_BACK;
        int cavalryPerRow = width == 0 ? Math.max(1, (cavalry + CAVALRY_ROWS - 1) / CAVALRY_ROWS)
                : Math.max(1, Math.min(cavalry, (int) (width * pitch / BLOCK_PITCH)));
        for (int i = 0; i < cavalry; i++) {
            int row = i / cavalryPerRow;
            int inRow = Math.min(cavalryPerRow, cavalry - row * cavalryPerRow);
            slots.add(new Slot(horse.get(i), across(i % cavalryPerRow, inRow, BLOCK_PITCH), cavalryBack + row * CAVALRY_ROW, -1, i,
                    FILE_WIDTH, wing, BAND_CAVALRY));
        }
        widest = Math.max(widest, Math.min(cavalry, cavalryPerRow) * BLOCK_PITCH);
        return widest;
    }

    /**
     * Shares {@code count} by {@code shares} (between the wings, or the kinds of soldier), in whole blocks (largest
     * remainders first), each part with a share getting at least {@code least}.
     */
    private static int[] share(int count, double[] shares, int least) {
        int n = shares.length;
        double total = 0;
        for (double s : shares) {
            total += s;
        }
        int[] out = new int[n];
        double[] rest = new double[n];
        int given = 0;
        for (int w = 0; w < n; w++) {
            double exact = total <= 0 ? 0 : count * shares[w] / total;
            out[w] = (int) Math.floor(exact);
            rest[w] = exact - out[w];
            given += out[w];
        }
        while (given < count) {
            int best = 0;
            for (int w = 1; w < n; w++) {
                if (rest[w] > rest[best]) {
                    best = w;
                }
            }
            out[best]++;
            rest[best] = -1;
            given++;
        }
        for (int w = 0; w < n; w++) {
            while (shares[w] > 0 && out[w] < least) {
                int from = 0;
                for (int v = 1; v < n; v++) {
                    from = out[v] > out[from] ? v : from;
                }
                out[from]--;
                out[w]++;
            }
        }
        return out;
    }

    /**
     * How far apart an army's wings stand: {@link #WING_GAP} up to 10,000 soldiers, then wider, evenly, to
     * {@link #MAX_WING_GAP} at 100,000: 35 blocks at 10,000.
     */
    public static double wingGap(int soldiers) {
        if (soldiers < WIDE_GAP_FROM) {
            return WING_GAP;
        }
        double t = (double) (soldiers - WIDE_GAP_FROM) / (MAX_SOLDIERS - WIDE_GAP_FROM);
        return Math.min(MAX_WING_GAP, WIDE_GAP + t * (MAX_WING_GAP - WIDE_GAP));
    }

    /** The most front files an army stands: {@link #MAX_WIDE_FRONT} up to 10,000 soldiers, more for bigger armies. */
    public static double maxFront(int soldiers) {
        return MAX_WIDE_FRONT * Math.sqrt(Math.max(1, soldiers / 10_000.0));
    }

    /**
     * A wing's infantry, {@code n[k]} blocks of each of {@code types}, in the order they take their places from the
     * front rank back: by each card's {@link Arm#front} (the heavy infantry, the halberdiers beside it, the levies
     * behind, then the skirmishers and the zweihandlers), the expendable first if they go up front. A card whose mod
     * isn't installed fights as its stand-in ({@link Arm#fielded}): without Epic Knights, zweihandlers as skirmishers
     * and halberdiers as heavy infantry.
     */
    static List<Arm> foot(List<Arm> types, int[] n, boolean levyFront) {
        java.util.Comparator<Integer> order = java.util.Comparator.<Integer>comparingInt(k -> levyFront && types.get(k).expendable() ? 0 : 1)
                .thenComparingInt(k -> types.get(k).front()).thenComparingInt(k -> types.get(k).ordinal());
        return lineUp(types, n, order);
    }

    /** A wing's shooters (then engines) or horse, {@code n[k]} blocks of each of {@code types}, by {@link Arm#front}. */
    static List<Arm> behind(List<Arm> types, int[] n) {
        return lineUp(types, n, java.util.Comparator.<Integer>comparingInt(k -> types.get(k).engine() ? 1 : 0)
                .thenComparingInt(k -> types.get(k).front()).thenComparingInt(k -> types.get(k).ordinal()));
    }

    private static List<Arm> lineUp(List<Arm> types, int[] n, java.util.Comparator<Integer> order) {
        List<Arm> out = new ArrayList<>();
        java.util.stream.IntStream.range(0, types.size()).boxed().sorted(order).forEach(k -> {
            Arm arm = types.get(k).fielded();
            for (int i = 0; i < n[k]; i++) {
                out.add(arm);
            }
        });
        return out;
    }

    /** The front file standing nearest a point {@code right} of the wing's middle. */
    private static int nearestFile(double right, double[] fileRight) {
        int best = 0;
        for (int file = 1; file < fileRight.length; file++) {
            if (Math.abs(fileRight[file] - right) < Math.abs(fileRight[best] - right)) {
                best = file;
            }
        }
        return best;
    }

    /**
     * Where the {@code i}th block of a wedge stands, relative to its tip: {right, back}. Row r (from the tip) holds
     * r + 1 blocks side by side, so the wedge widens behind its point.
     */
    public static double[] wedgeSlot(int i) {
        int row = 0;
        while ((row + 1) * (row + 2) / 2 <= i) {
            row++;
        }
        int inRow = i - row * (row + 1) / 2;
        return new double[]{(inRow - row / 2.0) * WEDGE_PITCH, row * WEDGE_ROW};
    }

    /** Where block {@code i} of {@code n} side by side stands along the line, centred on the army's middle. */
    private static double across(int i, int n, double pitch) {
        return (i - (n - 1) / 2.0) * pitch;
    }

    /**
     * Great Shield: each block re-forms this many soldiers wide (so five deep), and each front file holds this many
     * blocks closed up one behind the other, {@link #SHIELD_BLOCK_DEPTH} apart: fifteen ranks.
     */
    public static final int SHIELD_FILES = 4;
    public static final int SHIELD_STACK = 3;
    public static final double SHIELD_BLOCK_DEPTH = 5 * SPACING;

    /**
     * A wedge of cavalry (Great Spear, Unrelenting Charge) stands at least this far behind the front, behind the last
     * infantry line; its blocks stand this far apart across, and its rows this far apart.
     */
    public static final double SPEAR_BACK = 45;
    public static final double WEDGE_PITCH = BLOCK_PITCH;
    public static final double WEDGE_ROW = 6;

    /** How far behind the front centre the general stands, mounted, with the guard this far behind them. */
    public static final double GENERAL_BACK = 20;
    public static final double GUARD_BACK = 4;

    /** The standard-bearer stands this far to the general's right. */
    public static final double BEARER_RIGHT = 2;

    private BattlePlan() {
    }

    /** A point on the ground. */
    public record Point(double x, double z) {
        public Point plus(double dx, double dz) {
            return new Point(x + dx, z + dz);
        }

        public double distanceTo(Point o) {
            return Math.hypot(x - o.x, z - o.z);
        }
    }

    /**
     * Where a slot lands in the world for an army whose front faces ({@code facingX}, {@code facingZ}) (a unit
     * vector toward the enemy) and whose front line is centred on {@code front}.
     */
    public static Point place(Point front, double facingX, double facingZ, double right, double back) {
        // The army's right, seen from behind it: Minecraft's x east, z south, so right of facing (fx, fz) is (-fz, fx).
        double rightX = -facingZ;
        double rightZ = facingX;
        return front.plus(rightX * right - facingX * back, rightZ * right - facingZ * back);
    }

    /** Where a point lies in the frame of {@link #place}: {right, back} of {@code origin} for an army facing (fx, fz). */
    public static Point offset(Point origin, double facingX, double facingZ, Point at) {
        double dx = at.x() - origin.x();
        double dz = at.z() - origin.z();
        return new Point(dx * -facingZ + dz * facingX, -(dx * facingX + dz * facingZ));
    }

    /** The two armies' front centres: each {@link #DEPLOY_DISTANCE} from the centre along the line of battle. */
    public static Point[] fronts(Point centre, double axisX, double axisZ) {
        return new Point[]{
                centre.plus(-axisX * DEPLOY_DISTANCE, -axisZ * DEPLOY_DISTANCE),
                centre.plus(axisX * DEPLOY_DISTANCE, axisZ * DEPLOY_DISTANCE)};
    }

    /** What a formation does this second. */
    public enum Order {
        /** Stand in formation where it is. */
        HOLD,
        /** Move the formation's line one {@link #MARCH_STEP} toward the enemy, keeping formation. */
        MARCH,
        /**
         * Fight in line: press on to {@link #FRONT_CONTACT} from the enemy's front, keeping ranks, and fight there; its
         * soldiers take on enemies within reach and return to their places.
         */
        ENGAGE,
        /** Break formation and fight. */
        CHARGE
    }

    /**
     * The general's order for a block in the front line, or archers or cavalry that haven't charged yet.
     * {@code nearestEnemy} is the distance from the formation's line to the nearest enemy soldier;
     * {@code infantryEngaged} is whether any of its own army's front line is fighting. Shooters close to bow range.
     */
    public static Order order(Arm arm, int secondsElapsed, double nearestEnemy, boolean infantryEngaged) {
        return order(arm, secondsElapsed, nearestEnemy, infantryEngaged, ARCHER_RANGE);
    }

    /** The same, its shooters closing to {@code reach} ({@link FieldBlock#standOff}). */
    public static Order order(Arm arm, int secondsElapsed, double nearestEnemy, boolean infantryEngaged, double reach) {
        if (secondsElapsed < FORMING_SECONDS) {
            return Order.HOLD;
        }
        if (arm.infantry()) {
            return nearestEnemy <= INFANTRY_ENGAGE ? Order.ENGAGE : Order.MARCH;
        }
        if (arm.mounted()) {
            return nearestEnemy <= CAVALRY_THREAT || infantryEngaged ? Order.CHARGE : Order.HOLD;
        }
        return nearestEnemy <= reach ? Order.HOLD : Order.MARCH; // the shooters
    }

    /** Whether a front block with {@code alive} of its {@code started} soldiers should be relieved. */
    public static boolean needsRelief(int alive, int started) {
        return alive <= started * RELIEF_SHARE;
    }

    /**
     * Whether archers whose line is {@code behindFront} blocks behind their own front line (negative: ahead of it)
     * may march another step: never past the line, so they don't walk out alone ahead of infantry that is waiting.
     */
    public static boolean archersMayAdvance(double behindFront) {
        return archersMayAdvance(behindFront, 0);
    }

    /** The same, for an army in a Great Shield or not: never into its shield stack either. */
    public static boolean archersMayAdvance(double behindFront, int stackFiles) {
        return behindFront - MARCH_STEP >= archerBack(stackFiles, stackFiles == 0 ? 1 : SHIELD_STACK);
    }

    /** Whether a battle has stalled: fighting began, and nobody has fallen for {@link #STALL_SECONDS}. */
    public static boolean stalled(int seconds, int lastFall) {
        return lastFall > 0 && seconds - lastFall >= STALL_SECONDS;
    }

    /** Whether a block with {@code alive} of its {@code started} soldiers is fit to go forward and relieve another. */
    public static boolean fitToRelieve(int alive, int started) {
        return alive >= started * FIT_SHARE;
    }

    /** Whether an army with {@code alive} of its {@code started} soldiers left routs. */
    public static boolean routs(int alive, int started) {
        return alive <= started * ROUT_SHARE;
    }

    /**
     * The two armies' uniform colours (RGB) from their factions' main colours. Factions sharing a colour, or close to
     * it, would look like one army, so then the second side wears a darker shade (a lighter one if that is too dark).
     */
    public static int[] uniformColors(int first, int second) {
        if (colorDistance(first, second) >= 60) {
            return new int[]{first, second};
        }
        int darker = scale(second, 0.55);
        int shade = brightness(darker) >= 40 ? darker : lighten(second, 0.5);
        return new int[]{first, shade};
    }

    private static double colorDistance(int a, int b) {
        int dr = (a >> 16 & 255) - (b >> 16 & 255);
        int dg = (a >> 8 & 255) - (b >> 8 & 255);
        int db = (a & 255) - (b & 255);
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }

    private static int brightness(int rgb) {
        return Math.max(rgb >> 16 & 255, Math.max(rgb >> 8 & 255, rgb & 255));
    }

    private static int scale(int rgb, double f) {
        return (int) ((rgb >> 16 & 255) * f) << 16 | (int) ((rgb >> 8 & 255) * f) << 8 | (int) ((rgb & 255) * f);
    }

    private static int lighten(int rgb, double f) {
        int r = rgb >> 16 & 255;
        int g = rgb >> 8 & 255;
        int b = rgb & 255;
        return (int) (r + (255 - r) * f) << 16 | (int) (g + (255 - g) * f) << 8 | (int) (b + (255 - b) * f);
    }
}
