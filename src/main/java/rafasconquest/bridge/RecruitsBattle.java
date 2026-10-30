package rafasconquest.bridge;

import com.talhanation.recruits.FactionEvents;
import com.talhanation.recruits.config.RecruitsServerConfig;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.BowmanEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import com.talhanation.recruits.util.FormationUtils;
import com.talhanation.recruits.world.RecruitsDiplomacyManager;
import com.talhanation.recruits.CommandEvents;
import com.talhanation.recruits.RecruitEvents;
import com.talhanation.recruits.world.RecruitsGroup;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import rafasconquest.character.Rank;
import rafasconquest.character.Trait;
import rafasconquest.battle.Doctrine.Strategy;
import rafasconquest.character.BattleRole;
import rafasconquest.character.Job;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.enchantment.Enchantments;
import rafasconquest.data.Ranks;
import rafasconquest.character.GameCharacter;
import com.talhanation.recruits.world.RecruitsDiplomacyManager.DiplomacyStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattleSite;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.Calibration;
import rafasconquest.battle.FieldArmy;
import rafasconquest.battle.FieldBattle;
import rafasconquest.battle.FieldBlock;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.battle.FieldRules;
import rafasconquest.battle.GroundPlan;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A battle between two factions' armies, for the prototype: fought as numbers ({@link FieldBattle}) everywhere, and
 * by real Recruits soldiers wherever a player is near enough to see it.
 * <p>
 * The numeric battle is the whole battle: every block of 20 (and each general and standard-bearer) is always one of
 * its blocks, and its generals give every order ({@link BattlePlan}). A block within {@link #LIVE_WITHIN} blocks of a
 * player becomes real soldiers, standing where the block stands, each with the health they have left; beyond
 * {@link #NUMBERS_BEYOND} it goes back to numbers, keeping each soldier's health. While a block is real, its soldiers
 * walk and fight as Recruits soldiers do: each second they report where they are and how they are, the numeric battle
 * gives the block its orders, and those are passed on to them (stand in line here, charge, withdraw). Where real
 * soldiers fight a block that is only numbers, the numeric battle works the fight out and its damage is dealt to the
 * real soldiers. At most {@link #MAX_LIVE_SOLDIERS} soldiers are real at once, the nearest first.
 * <p>
 * Soldiers wear full leather dyed the faction's colour; each block's captain wears the faction banner; the general a
 * gold helmet and a banner on the back, with a standard-bearer at the side. The two factions are made enemies in
 * Recruits' diplomacy for the battle (so their soldiers attack each other and nobody else; a bystander is left alone
 * unless they strike first) and their old relation is restored afterwards. Also times every server tick while it
 * runs, and measures what real soldiers fighting real soldiers do, to calibrate the numbers. One battle at a time;
 * server thread only.
 */
public final class RecruitsBattle {
    private static final int REPORT_SECONDS = 10;
    /**
     * Keeps the ground under real soldiers loaded and running, as a forceloaded chunk is, but only while they stand
     * there (not saved with the world).
     */
    private static final TicketType<ChunkPos> BATTLE_TICKET = TicketType.create(RafasConquest.MOD_ID + "_battle", Comparator.comparingLong(ChunkPos::toLong));
    /** A soldier's chunk and those this many chunks around it stay loaded: more than a soldier walks in a second. */
    private static final int HELD_RADIUS = 1;
    /** How often clients hear where the formations stand. */
    private static final int SNAPSHOT_TICKS = 20;
    /**
     * A block becomes real soldiers within this many blocks of a player, and goes back to numbers beyond the second
     * distance; the gap keeps a block from flickering between the two. Players see real soldiers only within about
     * 67 blocks, so a block is always real before anyone can see it.
     */
    public static final double LIVE_WITHIN = 96;
    public static final double NUMBERS_BEYOND = 128;
    /**
     * At most this many real soldiers at once, nearest the players first. A rider counts as two: the horse is an
     * entity too.
     */
    public static final int MAX_LIVE_SOLDIERS = 300;
    /** The cap in force: {@code /pve debug battle livecap}, kept in {@link #LIVE_CAP_FILE} across restarts. */
    private static int liveCap = loadLiveCap();
    private static final String LIVE_CAP_FILE = "livecap.txt";
    /** At most this many soldiers become real each second, the nearest first, so a crowd doesn't appear in one tick. */
    private static final int SPAWNS_PER_SECOND = 160;
    /** A real soldier stays real until this much further out than one that is numbers becomes real, against flicker. */
    private static final double KEEP_REAL_BIAS = 6;
    /** How far a block's soldiers may stand from its position, for skipping blocks far from everyone. */
    private static final double BLOCK_REACH = 40;
    /** Ranks stand this many soldiers' spacings apart, front to back. */
    private static final double SLOT_DEPTH = 1.75;
    /**
     * Battles up to this many blocks (both sides) are also fought through as numbers at the start, for comparing; a
     * bigger one would hold the server up for seconds.
     */
    private static final int PREDICT_UP_TO_BLOCKS = 200;
    /** A general sounds the horn at most this often, this loud (a goat horn at 16 carries 256 blocks). */
    private static final int HORN_SECONDS = 5;
    /** How long an enlisted player's latest order stays above their hotbar. */
    private static final int ORDER_SHOWN_SECONDS = 6;
    /** Officers (Marshals and the general) give an enlisted player at most one order this often; a newer one replaces one waiting. */
    private static final int OFFICER_ORDER_SECONDS = 3;
    /** A front-row soldier goes after enemies this close to its place, and returns to it when there are none. */
    private static final double FRONT_ROW_REACH = 12;
    /** How many ranks of a block fight where real soldiers meet. */
    private static final int FRONT_RANKS = 2;
    /** A soldier in the ranks behind fights an enemy this close to it. */
    private static final double SELF_DEFENCE = 4;
    /** Real enemy blocks this near a block (their positions) are at hand for its soldiers to fight. */
    private static final double ENGAGE_NEAR = 24;
    private static final float HORN_VOLUME = 16;
    /** A charged soldier with no foe goes after the nearest real enemy this close. */
    private static final double HUNT_REACH = 80;
    /** Shield-bearers raise their shields within this reach of enemy archers (Recruits bowmen shoot out to 44). */
    private static final double ARROW_REACH = 50;
    /** ...and within this reach of any enemy, unless they're the ones striking. */
    private static final double MELEE_SHIELD_REACH = 12;
    /** Recruits' square formation, for the command screen. */
    private static final int SQUARE_FORMATION = 2;
    /** Sunset begins (the battle stops and the armies pull back), night falls, and dawn, in the time of day. */
    private static final long EVENING = 12000;
    private static final long NIGHTFALL = 13000;
    private static final long DAWN = 23000;
    /** A day's battle lasts at least this long, even if begun late in the day. */
    private static final int MIN_BATTLE_SECONDS = 60;
    /** A camp's command tent stands this far behind its army's rearmost block. */
    private static final double CAMP_GAP = 40;
    /** In the evening the armies re-form this far short of their camps. */
    private static final double PULLBACK_SHORT_OF_CAMP = 15;
    private static final int SOLDIERS_PER_TENT = 100;
    private static final int MIN_TENTS = 6;
    private static final int MAX_TENTS = 36;
    /** A beaten army leaves the field once the winner hasn't harmed it for this long. */
    private static final int QUIET_SECONDS = 30;
    /** A pursuit lasts this long at most. */
    private static final int PURSUIT_SECONDS = 300;
    /** A winner outnumbered by this much by the beaten army doesn't pursue it. */
    private static final double PURSUE_ONLY_UNDER = 0.8;
    private static final int FADE_PER_TICK = 6;
    /** A beacon shows where an order sends a player farther than this, goes once they're nearer, and shows again past the far reach. */
    private static final double BEACON_NEAR = 10;
    private static final double BEACON_FAR = 45;
    private static final Object TENT_ORDER = new Object();
    /** Orders to save breaking blocks: how near breaking, how near the player, how long, and the rest between them. */
    private static final double SUPPORT_RISK = 0.6;
    private static final double SUPPORT_SAFE = 0.3;
    private static final double SUPPORT_REACH = 150;
    private static final double SAVE_REACH = 60;
    private static final int SUPPORT_SECONDS = 120;
    private static final int SUPPORT_REST = 45;
    /** A unit in a line stands this many files wide. */
    private static final int LINE_FILES = 10;
    /** Units following their player stand this far apart, and their front this far behind the player. */
    private static final double UNIT_GAP = 3;
    private static final double FOLLOW_BEHIND = 2.5;
    /** A player who fought this recently (ticks) is in a fight: their units stop turning with them. */
    private static final int COMBAT_TICKS = 80;
    /** A charging unit goes after real enemies this close. */
    private static final double CHARGE_REACH = 40;
    /** Figures are looked for among enemy blocks this near a player, and struck by soldiers within this reach. */
    private static final double FIGURE_SEARCH = 60;
    private static final double SOLDIER_REACH = 2.5;
    /** A galloping cavalry player tramples a figure this close to their horse, at most once every this many ticks. */
    private static final double TRAMPLE_REACH = 1.8;
    private static final int TRAMPLE_TICKS = 10;
    private static final double TRAMPLE_DAMAGE = 4;
    /** A player's blow at a figure counts if the figure is this close. */
    private static final double PLAYER_FIGURE_REACH = 6;
    /** The deployment's countdown: the first day's, and every later morning's. */
    private static final int FIRST_DEPLOYMENT = 240;
    private static final int MORNING_DEPLOYMENT = 60;
    /** A player this close to their place is in position. */
    private static final double IN_POSITION = 5;
    /** How likely a general is, each morning, to give a player another task when there is a reason to. */
    private static final double NEW_TASK_CHANCE = 0.6;
    /** A messenger gives the orders to a player who comes this close. */
    private static final double MESSENGER_REACH = 4;
    /** A horse this fast (blocks a tick; a Recruits horse gallops at about 0.3) is charging. */
    private static final double GALLOP = 0.15;
    /** Marks the kit a player is given for a battle, to take back after. */
    private static final String KIT_TAG = "RafasConquestBattleKit";
    /** The slowing put on a spent charge's horses. */
    private static final UUID SPENT_HORSE = UUID.fromString("6f1c2d3e-4b5a-4c6d-8e7f-9a0b1c2d3e4f");
    /** A charged soldier this close to its target is taken to be striking it (for calibration, and for momentum). */
    private static final double STRIKING_DISTANCE = 4;

    @Nullable
    private static RecruitsBattle current;
    /** A battle's ground is being surveyed, before it begins. */
    private static boolean preparing;
    private static final Calibration SESSION_CALIBRATION = new Calibration();

    private final ServerLevel level;
    private FieldBattle field;
    /** The battle's name: of the province it is fought in, and the land at its middle. */
    private String name = "The Battle";
    /** Where the battle is fought, for each morning's deployment. */
    private Point centre = new Point(0, 0);
    private double axisX = 1;
    private double axisZ;
    private Phase phase = Phase.DEPLOYMENT;
    /** Seconds since the battle was set up, and when the phase began. */
    private int clock;
    private int phaseSince;
    private int day = 1;
    /** The winner, once decided (-1 before, or for a draw). */
    private int winner = -1;
    private boolean surrendered;
    /** When the beaten army was beaten, and when its real soldiers were last hurt, in battle seconds. */
    private int aftermathSince;
    private final int[] lastRealHurt = {-1, -1};
    /** Sides whose army has left the field, and the soldiers still fading away. */
    private final boolean[] faded = new boolean[2];
    private final List<UUID> fading = new ArrayList<>();
    private final WarCamp[] camps = new WarCamp[2];
    /** Each winged army's camps behind its left and right wings (by {@link FieldArmy#LEFT}, {@link FieldArmy#RIGHT}). */
    private final WarCamp[][] wingCamps = new WarCamp[2][3];
    /** How many tents of each camp are taken by enlisted players. */
    private final Map<WarCamp, Integer> tentsTaken = new HashMap<>();
    /** The land the battle is fought on, as read for deploying. */
    @Nullable
    private Terrain terrain;
    /** Each camp's guards, by side; the war council in the tents, with their side. */
    private final Map<Integer, List<UUID>> campFolk = new HashMap<>();
    private final Map<UUID, Integer> councillors = new HashMap<>();
    private final WarCouncil council = new WarCouncil();
    /** The deployment's countdown bars: one per army, and one for players in neither. */
    private final ServerBossEvent[] deploymentBars = new ServerBossEvent[3];
    /** Each enlisted player's tent for the battle, and the next free tent of each camp. */
    private final Map<UUID, Integer> tents = new HashMap<>();
    private final Map<UUID, WarCamp> tentCamps = new HashMap<>();
    /** The general's orders for the day, for players given another task than their own. */
    private final Map<UUID, GeneralTask> dayRoles = new HashMap<>();
    private final Side[] sides;
    /** The blocks that are real soldiers now, and their soldiers. */
    private final Map<FieldBlock, Live> live = new LinkedHashMap<>();
    /** Blocks that have been real at some time (their soldiers' starting health is measured once). */
    /** Soldiers ever real (block id and index), counted once for calibration. */
    private final Set<Long> everReal = new HashSet<>();
    /** Spots treated like a player standing there, for testing the live area without one. */
    private final List<Point> watchPoints = new ArrayList<>();
    private final DiplomacyStatus[] oldRelations = new DiplomacyStatus[2];
    /** Every soldier and horse of the battle, so their deaths drop nothing. */
    private final Set<UUID> ours = new HashSet<>();
    /** Every real soldier's block, by entity id, for the damage rules ({@link #adjustDamage}). */
    private final Map<Integer, Live> byEntity = new HashMap<>();
    /** When each side's general last sounded the horn. */
    private final int[] lastHorn = {-HORN_SECONDS, -HORN_SECONDS};
    /** Generals' entity ids → the banner on their backs, for players who start seeing them. */
    private final Map<Integer, ItemStack> backBanners = new HashMap<>();
    /** Chunks held loaded, as {@link ChunkPos#toLong}. */
    private final Set<Long> heldChunks = new HashSet<>();
    /**
     * Ground heights where nothing is loaded, from the world generator, one for each chunk (taken at its middle):
     * asking the generator is slow, and distant figures need no finer ground than that.
     */
    private final Map<Long, Integer> groundCache = new HashMap<>();
    /** Each block's last height, and how many generator lookups this second has made. */
    private final Map<Integer, Integer> lastGround = new HashMap<>();
    private int groundLookups;
    private static final int GROUND_LOOKUPS_PER_SECOND = 40;
    private final Calibration calibration = new Calibration();
    private final Map<FieldBlock, Double> lastHealth = new HashMap<>();
    private final Map<FieldBlock, Point> lastCentre = new HashMap<>();
    /** How the same battle came out fought only as numbers, for setting beside this one. */
    private final String prediction;
    private final boolean[] chargeAnnounced = new boolean[2];
    private boolean meleeAnnounced;
    private int ticks;
    private boolean over;
    /** Whether the fighting has begun; until then the armies deploy and their blocks may be placed by hand. */
    private boolean started;
    /** The latest lines told to everyone, for the battle map. */
    private final List<String> lines = new ArrayList<>();
    /** Each side's named groups of blocks, by block id, chosen on the battle map. */
    private final List<Map<String, List<Integer>>> groups = List.of(new LinkedHashMap<>(), new LinkedHashMap<>());
    /** Players enlisted in the battle, by id, and the entity ids of the soldiers given them to command. */
    private final Map<UUID, Enlisted> enlisted = new LinkedHashMap<>();
    private final Set<Integer> commanded = new HashSet<>();
    private final TickTimes whole = new TickTimes();
    private final TickTimes window = new TickTimes();
    private long tickStart;
    private int snapshotTicks;
    /** Time spent on the battle's own work since the last report, in nanoseconds. */
    private long ownNanos;
    /** Of that, the numbers' own second, the switching between real and numbers, and the snapshots. */
    private long fieldNanos;
    private long switchNanos;
    private long snapshotNanos;

    /** What each side fights as: its faction, Recruits team and colours. */
    private record Side(Faction faction, PlayerTeam team, int uniform, int mark, ItemStack banner, int horn, GameCharacter general,
                        String[] marshals, Trait[] traits) {
    }

    /**
     * A player enlisted in the battle: their side and rank, their block (the first to charge) and the blocks beside it
     * whose soldiers they command (as many as their rank leads, up to a Captain's), and the orders last given them.
     */
    private static final class Enlisted {
        final UUID player;
        final int side;
        final Rank rank;
        final FieldBlock home;
        final List<FieldBlock> blocks;
        final int quota;
        final Set<Integer> given = new HashSet<>();
        String order = "";
        int orderSince;
        /** The officers' latest order not yet shown, and when the last was shown. */
        @Nullable
        String pendingTitle;
        int titleAt = -OFFICER_ORDER_SECONDS;
        @Nullable
        Duty lastDuty;
        @Nullable
        FieldBlock lastStrike;
        boolean toldHold;
        boolean toldKill;
        /** Their soldiers, a unit per block, each its own group in Recruits and its own formation. */
        final List<Unit> units = new ArrayList<>();
        /** Which way their following units face (the player's, until they fight), and when they last fought. */
        float formYaw;
        long lastCombat = Long.MIN_VALUE / 2;
        long lastTrample;
        /** The blocks an order has sent them to save, since when, and when the last such order ended. */
        final List<FieldBlock> support = new ArrayList<>();
        @Nullable
        Object supportKey;
        int supportSince;
        int supportEnded = -1000;
        /** The order the beacon shows the way to, and whether it shows now. */
        @Nullable
        Object beaconKey;
        boolean beaconShown;
        /** Their tent in camp (an index into that camp's tents), or -1. */
        int tent = -1;
        @Nullable
        WarCamp tentCamp;
        /** Heading out of camp to their position, their soldiers following them, not yet there. */
        boolean marching;
        /** A messenger with the general's orders, waiting at their tent, and whether they've heard them. */
        @Nullable
        UUID messenger;
        boolean briefed;
        int messengerLeaves;
        Job job = Job.HEAVY_INFANTRY;
        /** The horse a cavalry player was given. */
        @Nullable
        UUID horse;
        /** Cavalry momentum: the player's own, and their riders'; whether each was in melee this second, and with cavalry. */
        double momentum;
        double unitMomentum;
        @Nullable
        Vec3 lastRide;
        @Nullable
        Point lastUnitCentre;
        boolean playerContact;
        boolean playerContactCavalry;
        boolean unitContact;
        boolean unitContactCavalry;

        Enlisted(UUID player, int side, Rank rank, FieldBlock home, List<FieldBlock> blocks, int quota) {
            this.player = player;
            this.side = side;
            this.rank = rank;
            this.home = home;
            this.blocks = blocks;
            this.quota = quota;
        }
    }

    /** A block that is real soldiers now: soldier i stands for the block's soldier i. */
    private static final class Live {
        final FieldBlock block;
        final AbstractRecruitEntity[] soldiers;
        /** The orders last passed on, so they are only passed on when they change. */
        @Nullable
        Point appliedLine;
        boolean appliedCharge;
        boolean appliedBraced;
        @Nullable
        Duty appliedDuty;
        /** In melee with a real enemy block, only its front row fighting ({@link #frontRowFights}). */
        boolean frontRow;
        /** Damage from numbers not yet dealt, short of a whole hit. */
        double damageCarry;

        Live(FieldBlock block) {
            this.block = block;
            this.soldiers = new AbstractRecruitEntity[block.started()];
        }

        List<AbstractRecruitEntity> living() {
            List<AbstractRecruitEntity> living = new ArrayList<>(soldiers.length);
            for (AbstractRecruitEntity s : soldiers) {
                if (s != null && s.isAlive()) {
                    living.add(s);
                }
            }
            return living;
        }
    }

    /** What a soldier is, for its kit. */
    private enum Role {
        SOLDIER, LEADER, GENERAL, BEARER, GUARD
    }

    /** Tick time totals, in nanoseconds. */
    private static final class TickTimes {
        long total;
        long worst;
        int count;

        void add(long nanos) {
            total += nanos;
            worst = Math.max(worst, nanos);
            count++;
        }

        String describe() {
            double mean = count == 0 ? 0 : total / 1e6 / count;
            double tps = Math.min(20, mean <= 0 ? 20 : 1000 / mean);
            return String.format(Locale.ROOT, "%.1f ms/tick average, %.1f ms worst (about %.1f TPS)", mean, worst / 1e6, tps);
        }

        void clear() {
            total = 0;
            worst = 0;
            count = 0;
        }
    }

    private RecruitsBattle(ServerLevel level, FieldBattle field, Side[] sides, String prediction) {
        this.level = level;
        this.field = field;
        this.sides = sides;
        this.prediction = prediction;
    }

    public static boolean running() {
        return current != null && !current.over;
    }

    /** Whether this soldier or horse belongs to the current battle (its deaths drop nothing). */
    public static boolean isOurs(Entity entity) {
        return current != null && current.ours.contains(entity.getUUID());
    }

    /** A player started seeing an entity: if it is a general, tell them about the banner on its back. */
    public static void onStartTracking(ServerPlayer player, Entity entity) {
        ItemStack banner = current == null ? null : current.backBanners.get(entity.getId());
        if (banner != null) {
            ConquestNetwork.sendBackBanner(player, entity.getId(), banner);
        }
    }

    /**
     * Sets up both armies around {@code centre}, facing each other along ({@code axisX}, {@code axisZ}) (a unit
     * vector from {@code first}'s side toward {@code second}'s), and starts the battle. Each army is
     * {@code firstSoldiers} or {@code secondSoldiers} strong, in whole blocks of 20 ({@link BattlePlan#blocks}), plus its general and standard-bearer. Nobody is
     * spawned yet: blocks near players become real soldiers over the next seconds. Throws if Recruits isn't ready, or a
     * battle is already being set up.
     * <p>
     * The ground is surveyed first, off the server thread (reading unloaded land from the world generator is slow, and
     * needs nothing of the world), so the server keeps running meanwhile; the armies are then deployed and the battle
     * begun back on it, and {@code ready} gets a line for the caller (or {@code failed} why not).
     */
    public static void start(ServerLevel level, Faction first, int firstSoldiers, Faction second, int secondSoldiers,
                             Point centre, double axisX, double axisZ, Consumer<String> ready, Consumer<String> failed) {
        if (preparing) {
            throw new IllegalStateException("A battle is already being set up");
        }
        stop();
        RecruitsFactions factions = new RecruitsFactions(level);
        PlayerTeam teamA = factions.team(first);
        PlayerTeam teamB = factions.team(second);
        RecruitsDiplomacyManager diplomacy = FactionEvents.recruitsDiplomacyManager;
        if (teamA == null || teamB == null || diplomacy == null) {
            throw new IllegalStateException("Recruits isn't ready");
        }
        int[] uniforms = BattlePlan.uniformColors(mainColor(first), mainColor(second));
        java.util.Random characters = new java.util.Random(level.random.nextLong());
        Side[] sides = {
                new Side(first, teamA, uniforms[0], markColor(first), banner(level, first), horn(first),
                        GameCharacter.general(first.doctrines(), characters), marshals(characters), traits(characters)),
                new Side(second, teamB, uniforms[1], markColor(second), banner(level, second), horn(second),
                        GameCharacter.general(second.doctrines(), characters), marshals(characters), traits(characters))};
        int blocksA = BattlePlan.blocks(firstSoldiers);
        int blocksB = BattlePlan.blocks(secondSoldiers);
        long seed = level.random.nextLong();
        FieldBattle laidOut = new FieldBattle(blocksA, blocksB, centre, axisX, axisZ, seed, sides[0].general.plan(), sides[1].general.plan());
        // Moved over the land, by the world map, so no part of an army is cut off by a river or the sea.
        BaseMap map = ConquestRuntime.map();
        Point shift = map == null ? new Point(0, 0) : BattleSite.shift(siteParts(laidOut), mapWater(map));
        Point site = centre.plus(shift.x(), shift.z());
        FieldBattle field = shift.x() == 0 && shift.z() == 0 ? laidOut
                : new FieldBattle(blocksA, blocksB, site, axisX, axisZ, seed, sides[0].general.plan(), sides[1].general.plan());
        if (field != laidOut) {
            RafasConquest.LOGGER.info("[battle] moved {} blocks ({}, {}) so no army is cut off by water",
                    Math.round(Math.hypot(shift.x(), shift.z())), Math.round(shift.x()), Math.round(shift.z()));
        }
        Terrain terrain = new Terrain(level, Terrain.stepFor(firstSoldiers + secondSoldiers));
        preparing = true;
        terrain.surveyInBackground(GroundPlan.area(field)).whenCompleteAsync((surveyed, error) -> {
            preparing = false;
            if (error != null) {
                RafasConquest.LOGGER.error("[battle] couldn't survey the ground", error);
                failed.accept("Couldn't survey the ground: " + error.getMessage());
                return;
            }
            try {
                ready.accept(begin(level, field, sides, terrain, teamA, teamB, diplomacy, blocksA, blocksB, site, axisX, axisZ));
            } catch (RuntimeException e) {
                RafasConquest.LOGGER.error("[battle] couldn't begin the battle", e);
                failed.accept("Couldn't begin the battle: " + e.getMessage());
            }
        }, level.getServer());
    }

    /** The rest of {@link #start}, once the ground is surveyed: deploys the armies, pitches the camps, and begins. */
    private static String begin(ServerLevel level, FieldBattle field, Side[] sides, Terrain terrain, PlayerTeam teamA,
                                PlayerTeam teamB, RecruitsDiplomacyManager diplomacy, int blocksA, int blocksB, Point centre,
                                double axisX, double axisZ) {
        int hidden = GroundPlan.deploy(field, terrain);
        String prediction = predict(blocksA, blocksB, sides, centre, axisX, axisZ);
        RecruitsBattle battle = new RecruitsBattle(level, field, sides, prediction);
        battle.terrain = terrain;
        RafasConquest.LOGGER.info("[battle] deployed to the ground; {} blocks hide in ambush, {} of {} stand wet by the survey",
                hidden, GroundPlan.wetBlocks(field, terrain), blocksA + blocksB);
        battle.centre = centre;
        battle.axisX = axisX;
        battle.axisZ = axisZ;
        long campsStarted = System.nanoTime();
        battle.buildCamps();
        battle.spawnCouncil();
        RafasConquest.LOGGER.info("[battle] camps pitched in {} ms", (System.nanoTime() - campsStarted) / 1_000_000);
        battle.enlist(); // players already on a side take their places for the deployment
        battle.oldRelations[0] = diplomacy.getRelation(teamA.getName(), teamB.getName());
        battle.oldRelations[1] = diplomacy.getRelation(teamB.getName(), teamA.getName());
        diplomacy.setRelation(teamA.getName(), teamB.getName(), DiplomacyStatus.ENEMY, level, false);
        diplomacy.setRelation(teamB.getName(), teamA.getName(), DiplomacyStatus.ENEMY, level, false);
        current = battle;
        battle.name = battleName(level, centre);
        String line = String.format(Locale.ROOT, "%s: %s, %d soldiers, against %s, %d soldiers, at %d, %d", battle.name,
                battle.describe(0), field.army(0).started(), battle.describe(1), field.army(1).started(),
                Math.round(centre.x()), Math.round(centre.z()));
        RafasConquest.LOGGER.info("[battle] {}", line);
        battle.broadcast("[battle] " + battle.name);
        for (Side side : sides) {
            FieldArmy army = battle.field.army(side == sides[0] ? 0 : 1);
            String title = army.started() >= Rank.GRAND_MARSHAL.leads() ? "Grand Marshal" : "General";
            battle.broadcast(String.format(Locale.ROOT, "  %s is led by %s %s (%s)", side.faction.name(), title, side.general.name(),
                    side.general.plan().describe()));
            if (army.winged()) {
                battle.broadcast(String.format(Locale.ROOT, "    Marshals: left wing %s, centre %s, right wing %s", side.marshals[0],
                        side.marshals[1], side.marshals[2]));
            }
        }
        return line;
    }

    /** Sets how many soldiers may be real at once (riders counting two), and keeps it for later sessions. */
    public static String setLiveCap(int cap) {
        liveCap = cap;
        try {
            java.nio.file.Path file = liveCapFile();
            java.nio.file.Files.createDirectories(file.getParent());
            java.nio.file.Files.writeString(file, Integer.toString(cap));
        } catch (java.io.IOException | RuntimeException e) {
            return "At most " + cap + " real battle soldiers at once (not saved: " + e.getMessage() + ")";
        }
        return "At most " + cap + " real battle soldiers at once";
    }

    public static int liveCap() {
        return liveCap;
    }

    private static java.nio.file.Path liveCapFile() {
        return FMLPaths.CONFIGDIR.get().resolve("rafasconquest").resolve(LIVE_CAP_FILE);
    }

    private static int loadLiveCap() {
        try {
            return Math.max(0, Integer.parseInt(java.nio.file.Files.readString(liveCapFile()).trim()));
        } catch (Exception e) {
            return MAX_LIVE_SOLDIERS;
        }
    }

    /** Treats a spot like a player standing there (for testing the live area), or with null clears all such spots. */
    public static String watch(@Nullable Point spot) {
        RecruitsBattle battle = current;
        if (battle == null) {
            return "No battle to watch";
        }
        if (spot == null) {
            battle.watchPoints.clear();
            return "Stopped watching";
        }
        battle.watchPoints.add(spot);
        return String.format(Locale.ROOT, "Watching from %d, %d", Math.round(spot.x()), Math.round(spot.z()));
    }

    private String describe(int side) {
        return sides[side].faction.name();
    }

    /** The faction's main colour (its banner's base dye) as RGB, the way vanilla dyes leather with it. */
    private static int mainColor(Faction faction) {
        return rgb(DyeColor.byId(FactionBanner.of(faction.id()).base()));
    }

    private static int markColor(Faction faction) {
        return rgb(DyeColor.byId(FactionBanner.of(faction.id()).layers().get(0).color()));
    }

    private static int rgb(DyeColor dye) {
        float[] c = dye.getTextureDiffuseColors();
        return (int) (c[0] * 255) << 16 | (int) (c[1] * 255) << 8 | (int) (c[2] * 255);
    }

    /**
     * A battle's name, from the province its middle lies in (named as {@link ProvinceNames} names provinces) and the
     * land there: "The Battle of Shukai Plains". Off the map, at sea or before the map is drawn: "The Battle of the
     * Plains".
     */
    static String battleName(ServerLevel level, Point centre) {
        int x = Mth.floor(centre.x());
        int z = Mth.floor(centre.z());
        String province = null;
        rafasconquest.sim.ConquestServer server = rafasconquest.sim.ConquestServer.get();
        rafasconquest.map.BaseMap map = server == null ? null : server.mapStore().map();
        if (map != null) {
            rafasconquest.core.ProvinceMap.Centre c = map.provinceAt(x, z);
            if (c != null) {
                province = rafasconquest.core.ProvinceNames.name(level.getSeed(), c.x(), c.z());
            }
        }
        int y = level.getChunkSource().getGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level,
                level.getChunkSource().randomState());
        String land = landName(level.getBiome(new BlockPos(x, y, z)));
        return province != null ? "The Battle of " + province + " " + land : "The Battle of the " + land;
    }

    /** What a battle's land is called, from the biome at its middle. */
    private static String landName(Holder<net.minecraft.world.level.biome.Biome> biome) {
        String id = biome.unwrapKey().map(k -> k.location().getPath()).orElse("plains");
        String[][] names = {
                {"river", "River"}, {"beach", "Shore"}, {"shore", "Shore"}, {"ocean", "Coast"}, {"desert", "Desert"},
                {"badlands", "Badlands"}, {"savanna", "Savanna"}, {"jungle", "Jungle"}, {"swamp", "Marsh"},
                {"taiga", "Pines"}, {"grove", "Pines"}, {"meadow", "Meadows"}, {"cherry", "Blossoms"},
                {"peaks", "Mountains"}, {"slopes", "Mountains"}, {"windswept", "Hills"}, {"hills", "Hills"},
                {"snowy", "Snows"}, {"ice", "Snows"}, {"frozen", "Snows"}, {"forest", "Woods"}, {"birch", "Woods"},
                {"dark", "Woods"}, {"plains", "Plains"}, {"mushroom", "Fields"}};
        for (String[] n : names) {
            if (id.contains(n[0])) {
                return n[1];
            }
        }
        return "Plains";
    }

    /** The traits of an army's general and its Marshals of the left wing, centre and right wing. */
    private static Trait[] traits(java.util.Random characters) {
        return new Trait[]{Trait.random(characters), Trait.random(characters), Trait.random(characters), Trait.random(characters)};
    }

    /** The Marshals of an army's left wing, centre and right wing. */
    private static String[] marshals(java.util.Random characters) {
        return new String[]{GameCharacter.name(characters), GameCharacter.name(characters), GameCharacter.name(characters)};
    }

    /** The faction's horn: one of Minecraft's eight goat horn calls, always the same for the faction. */
    private static int horn(Faction faction) {
        return Math.floorMod(faction.id().hashCode(), SoundEvents.GOAT_HORN_SOUND_VARIANTS.size());
    }

    private static ItemStack banner(ServerLevel level, Faction faction) {
        return FactionBannerNbt.item(level.registryAccess(), FactionBanner.of(faction.id()));
    }

    /** Ends any battle: removes every real soldier (and horse), and restores the old relations. */
    public static String stop() {
        RecruitsBattle battle = current;
        if (battle == null) {
            return "No battle to clear";
        }
        if (!battle.over) {
            battle.reportCalibration();
        }
        current = null;
        int removed = 0;
        for (Live l : battle.live.values()) {
            removed += battle.discard(l);
        }
        battle.live.clear();
        battle.clearSnapshots();
        battle.dismissEnlisted();
        battle.dismissCouncil();
        battle.hideDeploymentBars();
        for (int side = 0; side < 2; side++) {
            battle.strikeCamp(side);
        }
        for (UUID id : battle.fading) {
            Entity e = battle.level.getEntity(id);
            if (e != null) {
                if (e.getVehicle() != null) {
                    e.getVehicle().discard();
                }
                e.discard();
            }
        }
        battle.fading.clear();
        battle.releaseChunks();
        battle.restoreRelations();
        return "Cleared the battle (" + removed + " soldiers removed)";
    }

    private void restoreRelations() {
        RecruitsDiplomacyManager diplomacy = FactionEvents.recruitsDiplomacyManager;
        if (diplomacy != null && oldRelations[0] != null) {
            String a = sides[0].team.getName();
            String b = sides[1].team.getName();
            diplomacy.setRelation(a, b, oldRelations[0], level, false);
            diplomacy.setRelation(b, a, oldRelations[1], level, false);
            oldRelations[0] = null;
        }
    }

    // ---- real soldiers: becoming and leaving ----

    /**
     * Soldier i's place in its block's ranks, from the front rank's centre: how far to the right and back. The
     * captain stands in the middle of the front rank, as the figures draw it.
     */
    private static double slotRight(FieldBlock block, int i) {
        int files = Math.max(1, block.files());
        return block.started() == 1 ? 0 : (i % files - (files - 1) / 2.0) * BattlePlan.SPACING;
    }

    private static double slotBack(FieldBlock block, int i) {
        return (i / Math.max(1, block.files())) * BattlePlan.SPACING * SLOT_DEPTH;
    }

    /** Where soldier i stands in the ranks of a block whose front rank's centre is at {@code front}. */
    private static Point slot(FieldBlock block, Point front, int i) {
        return BattlePlan.place(front, block.facingX(), block.facingZ(), slotRight(block, i), slotBack(block, i));
    }

    /**
     * Where a block's front rank stands, for placing the soldiers who are only numbers. A live block's position is
     * the middle of all its soldiers (see {@link #readBack}), so its front is that much ahead of it.
     */
    private static Point front(FieldBlock block, float[] health) {
        if (!block.live()) {
            return block.pos();
        }
        double right = 0;
        double back = 0;
        int n = 0;
        for (int i = 0; i < health.length; i++) {
            if (health[i] > 0) {
                right += slotRight(block, i);
                back += slotBack(block, i);
                n++;
            }
        }
        return n == 0 ? block.pos() : BattlePlan.place(block.pos(), block.facingX(), block.facingZ(), -right / n, -back / n);
    }

    /** A block's first soldier becomes real: the block is real from now on, in part or in whole. */
    private Live liveOf(FieldBlock block) {
        Live l = live.get(block);
        if (l == null) {
            loadGround(block.pos());
            l = new Live(block);
            live.put(block, l);
        }
        return l;
    }

    /** Soldier i of a block becomes real, at {@code p}, with the health they have left. */
    private void makeReal(Live l, int i, Point p, float health) {
        FieldBlock block = l.block;
        Side side = sides[block.side()];
        float yaw = yawOf(field.enemyDirection(block));
        EntityType<? extends AbstractRecruitEntity> type = block.duty() == Duty.BEARER ? ModEntityTypes.RECRUIT.get() : type(block.arm());
        Role role = block.duty() == Duty.GENERAL ? Role.GENERAL : block.duty() == Duty.BEARER ? Role.BEARER
                : block.duty() == Duty.GUARD ? Role.GUARD : i == block.captain() ? Role.LEADER : Role.SOLDIER;
        AbstractRecruitEntity soldier = spawn(type, side, role, p, yaw);
        if (soldier == null) {
            return;
        }
        if (block.champion()) {
            makeChampion(soldier);
        }
        if (everReal.add(((long) block.id() << 16) | i) && (role == Role.SOLDIER || role == Role.LEADER)) {
            calibration.addSoldier(soldier.getHealth());
        }
        soldier.setHealth(Math.min(health, soldier.getMaxHealth()));
        if (block.duty() == Duty.BEARER) {
            soldier.setAggroState(3); // passive: carries the flag, doesn't fight
        }
        l.soldiers[i] = soldier;
        byEntity.put(soldier.getId(), l);
    }

    /** Soldier i goes back to numbers (its health already read back), leaving with its horse. */
    private void makeNumbers(Live l, int i) {
        AbstractRecruitEntity e = l.soldiers[i];
        l.soldiers[i] = null;
        if (e == null) {
            return;
        }
        // Cavalry get their horse from Recruits on their first tick; it outlives a rider who falls or dismounts.
        UUID mountId = e.getMountUUID();
        Entity mount = mountId == null ? null : level.getEntity(mountId);
        if (mount != null) {
            mount.discard();
        }
        backBanners.remove(e.getId());
        byEntity.remove(e.getId());
        commanded.remove(e.getId());
        if (!e.isRemoved()) {
            e.discard();
        }
    }

    /** A block goes back to numbers, each soldier's health kept (already read this second), and its soldiers leave. */
    private void dematerialise(Live l) {
        discard(l);
        l.block.setReal(0);
        live.remove(l.block);
        lastHealth.remove(l.block);
        lastCentre.remove(l.block);
    }

    /** Removes a block's soldiers and their horses; returns how many soldiers were still standing. */
    private int discard(Live l) {
        int removed = 0;
        for (int i = 0; i < l.soldiers.length; i++) {
            AbstractRecruitEntity e = l.soldiers[i];
            if (e != null && !e.isRemoved()) {
                removed++;
            }
            makeNumbers(l, i);
        }
        return removed;
    }

    /**
     * Which soldiers should be real: each one near a player (the nearest first, within the budget) is real, the rest
     * are numbers, soldier by soldier, so a block can be part real and part figures as it comes into view. A real
     * soldier is measured from where it stands, one that is numbers from its place in its block's ranks; a real one
     * is kept a little longer than one that is numbers is made real ({@link #KEEP_REAL_BIAS}), so they don't flicker.
     */
    private void switchLive() {
        List<Point> watchers = new ArrayList<>(watchPoints);
        for (ServerPlayer player : level.players()) {
            watchers.add(new Point(player.getX(), player.getZ()));
        }
        record Candidate(FieldBlock block, int index, Point at, double distance, boolean real, int cost) {
        }
        List<Candidate> wanted = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            if (faded[side]) {
                continue;
            }
            for (FieldBlock block : field.army(side).blocks()) {
                if (block.alive() == 0) {
                    continue;
                }
                double near = Double.MAX_VALUE;
                for (Point w : watchers) {
                    near = Math.min(near, w.distanceTo(block.pos()));
                }
                if (near > NUMBERS_BEYOND + BLOCK_REACH) {
                    continue;
                }
                Live l = live.get(block);
                float[] health = block.health();
                Point front = front(block, health);
                int cost = block.arm() == Arm.CAVALRY && block.started() > 1 ? 2 : 1;
                for (int i = 0; i < health.length; i++) {
                    AbstractRecruitEntity s = l == null ? null : l.soldiers[i];
                    boolean real = s != null && s.isAlive() && !s.isRemoved();
                    if (!real && health[i] <= 0) {
                        continue;
                    }
                    Point at = real ? new Point(s.getX(), s.getZ()) : slot(block, front, i);
                    double distance = Double.MAX_VALUE;
                    for (Point w : watchers) {
                        distance = Math.min(distance, w.distanceTo(at));
                    }
                    if (real && commanded.contains(s.getId())) {
                        // A player's own soldiers stay real however far they go: they're theirs, not the numbers'.
                        wanted.add(new Candidate(block, i, at, -Double.MAX_VALUE, true, cost));
                    } else if (distance <= LIVE_WITHIN || real && distance <= NUMBERS_BEYOND) {
                        wanted.add(new Candidate(block, i, at, real ? distance - KEEP_REAL_BIAS : distance, real, cost));
                    }
                }
            }
        }
        wanted.sort(Comparator.comparingDouble(Candidate::distance));
        Map<FieldBlock, Set<Integer>> keep = new HashMap<>();
        int soldiers = 0;
        for (Candidate c : wanted) {
            if (soldiers + c.cost() > liveCap) {
                break;
            }
            soldiers += c.cost();
            keep.computeIfAbsent(c.block(), b -> new HashSet<>()).add(c.index());
        }
        Set<Live> changed = new HashSet<>();
        for (Live l : new ArrayList<>(live.values())) {
            Set<Integer> kept = keep.getOrDefault(l.block, Set.of());
            List<Integer> leaving = new ArrayList<>();
            for (int i = 0; i < l.soldiers.length; i++) {
                if (l.soldiers[i] != null && !kept.contains(i)) {
                    leaving.add(i);
                }
            }
            if (leaving.isEmpty()) {
                continue;
            }
            readBack(l); // their health as it is now, before they go
            if (live.get(l.block) != l) {
                continue; // lost to unloading meanwhile, and gone back to numbers
            }
            for (int i : leaving) {
                makeNumbers(l, i);
            }
            changed.add(l);
        }
        int spawned = 0;
        for (Candidate c : wanted) {
            if (spawned >= SPAWNS_PER_SECOND) {
                break;
            }
            if (c.real() || !keep.getOrDefault(c.block(), Set.of()).contains(c.index())) {
                continue;
            }
            Live l = liveOf(c.block());
            if (l.soldiers[c.index()] != null) {
                continue;
            }
            makeReal(l, c.index(), c.at(), c.block().health()[c.index()]);
            changed.add(l);
            spawned++;
        }
        for (Live l : changed) {
            int real = l.living().size();
            if (real == 0) {
                dematerialise(l);
                continue;
            }
            l.block.setReal(real);
            // Their orders again, for the soldiers who came and the places of those who went.
            l.appliedLine = null;
            l.appliedDuty = null;
            l.appliedCharge = false;
            lastHealth.remove(l.block);
            lastCentre.remove(l.block);
            passOnOrders(l);
        }
    }

    // ---- the soldiers ----

    private static EntityType<? extends AbstractRecruitEntity> type(Arm arm) {
        return switch (arm) {
            case SWORDSMEN -> ModEntityTypes.RECRUIT.get();
            case SHIELDWALL -> ModEntityTypes.RECRUIT_SHIELDMAN.get();
            case ARCHERS -> ModEntityTypes.BOWMAN.get();
            case CAVALRY -> ModEntityTypes.HORSEMAN.get();
        };
    }

    @Nullable
    private AbstractRecruitEntity spawn(EntityType<? extends AbstractRecruitEntity> type, Side side, Role role, Point p, float yaw) {
        AbstractRecruitEntity soldier = type.create(level);
        if (soldier == null) {
            return null;
        }
        BlockPos feet = ground(p);
        soldier.moveTo(feet.getX() + 0.5, feet.getY(), feet.getZ() + 0.5, yaw, 0);
        soldier.finalizeSpawn(level, level.getCurrentDifficultyAt(feet), MobSpawnType.EVENT, null, null);
        if (soldier instanceof BowmanEntity) {
            soldier.getInventory().addItem(new ItemStack(Items.ARROW, 64));
        }
        dress(soldier, side, role);
        soldier.setPersistenceRequired();
        soldier.setCanPickUpLoot(false); // keeps the uniform: no swapping it for armour off the dead
        soldier.setHunger(100);
        soldier.setMoral(100);
        soldier.setAggroState(0); // neutral: fights monsters and enemies of its faction, and whoever attacks it
        ours.add(soldier.getUUID());
        if (role == Role.GENERAL) {
            backBanners.put(soldier.getId(), side.banner.copy());
        }
        level.addFreshEntity(soldier);
        FactionEvents.addRecruitToTeam(soldier, side.team, level);
        return soldier;
    }

    /**
     * Full leather dyed the army's colour. The head shows rank: a leather cap for the ranks and the standard-bearer,
     * the faction banner for a block's captain, a gold helmet for the general. The standard-bearer carries the
     * banner; the general's own is drawn on the back by the client.
     */
    private static void dress(AbstractRecruitEntity soldier, Side side, Role role) {
        if (role == Role.GUARD || role == Role.GENERAL) {
            // The general's guard rides in full iron; the general too, under a gold helmet.
            equip(soldier, EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            equip(soldier, EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            equip(soldier, EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            equip(soldier, EquipmentSlot.HEAD, new ItemStack(role == Role.GENERAL ? Items.GOLDEN_HELMET : Items.IRON_HELMET));
            return;
        }
        equip(soldier, EquipmentSlot.CHEST, dyed(Items.LEATHER_CHESTPLATE, side.uniform));
        equip(soldier, EquipmentSlot.LEGS, dyed(Items.LEATHER_LEGGINGS, side.uniform));
        equip(soldier, EquipmentSlot.FEET, dyed(Items.LEATHER_BOOTS, side.uniform));
        equip(soldier, EquipmentSlot.HEAD, switch (role) {
            case SOLDIER, BEARER -> dyed(Items.LEATHER_HELMET, side.uniform);
            case LEADER -> side.banner.copy();
            case GENERAL -> new ItemStack(Items.GOLDEN_HELMET);
            case GUARD -> new ItemStack(Items.IRON_HELMET);
        });
        if (role == Role.BEARER) {
            equip(soldier, EquipmentSlot.MAINHAND, side.banner.copy());
            equip(soldier, EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }

    /** Recruits keeps a copy of each worn item in its inventory; both have to change or the old kit comes back. */
    private static void equip(AbstractRecruitEntity soldier, EquipmentSlot slot, ItemStack stack) {
        soldier.setItemSlot(slot, stack);
        soldier.getInventory().setItem(soldier.getInventorySlotIndex(slot), stack.copy());
    }

    /**
     * A battle soldier is dying: empty its inventory first. Recruits drops the whole inventory after Forge's own
     * drops are collected, so cancelling those ({@code LivingDropsEvent}) doesn't stop it.
     */
    public static void onDeath(Entity entity) {
        if (entity instanceof AbstractRecruitEntity soldier && isOurs(soldier)) {
            soldier.getInventory().clearContent();
        }
    }

    private static ItemStack dyed(Item item, int rgb) {
        ItemStack stack = new ItemStack(item);
        if (item instanceof DyeableLeatherItem leather) {
            leather.setColor(stack, rgb);
        }
        return stack;
    }

    /** A general: Recruits' top level with every level's bonuses, with an iron sword. */
    private static void makeChampion(AbstractRecruitEntity soldier) {
        int max = RecruitsServerConfig.RecruitsMaxXpLevel.get();
        // Recruits grants a level's bonuses on reaching the next one, reading the level it is leaving.
        for (int level = soldier.getXpLevel(); level < max; level++) {
            soldier.setXpLevel(level);
            soldier.addLevelBuffs();
        }
        soldier.setXpLevel(max);
        equip(soldier, EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    private BlockPos ground(Point p) {
        int x = Mth.floor(p.x());
        int z = Mth.floor(p.z());
        return new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
    }

    // ---- orders, passed on to real soldiers ----

    /**
     * Passes a real block's orders on to its soldiers when they change: where to stand in line, whether to break
     * formation and fight, whether to hold back from fighting (withdrawing, carrying the standard, fleeing).
     */
    private void passOnOrders(Live l) {
        FieldBlock block = l.block;
        List<AbstractRecruitEntity> living = l.living();
        living.removeIf(s -> commanded.contains(s.getId())); // a player commands these
        if (living.isEmpty()) {
            return;
        }
        if (!mayFight(block.side())) {
            // Pulled back, resting, or the fighting over: nobody fights, whatever their duty.
            for (AbstractRecruitEntity s : living) {
                s.setTarget(null);
                if (s.getState() != 3) {
                    s.setAggroState(3);
                }
            }
            l.appliedDuty = null;
        }
        if (block.duty() != l.appliedDuty && mayFight(block.side())) {
            boolean passive = passive(block.duty());
            for (AbstractRecruitEntity s : living) {
                if (passive) {
                    s.setTarget(null);
                }
                s.setAggroState(passive ? 3 : 0);
            }
            l.appliedDuty = block.duty();
        }
        if (block.duty() == Duty.BEARER) {
            Live general = generalOf(block.side());
            if (general != null && !general.living().isEmpty()) {
                for (AbstractRecruitEntity s : living) {
                    s.setProtectUUID(Optional.of(general.living().get(0).getUUID()));
                    s.setFollowState(5); // stays by the general
                }
                return;
            }
        }
        if (block.charged() && block.duty() == Duty.GENERAL) {
            hold(living.get(0), block.line()); // in the fight, right behind the guard; never off alone
            return;
        }
        if (block.charged() && block.duty() != Duty.FLEEING) {
            if (!l.appliedCharge) {
                l.appliedCharge = true;
                List<AbstractRecruitEntity> prey = preyOf(block, enemiesOf(block.side(), block.arm() == Arm.CAVALRY));
                living.forEach(s -> release(s, prey));
            }
            List<AbstractRecruitEntity> foes = preyOf(block, enemiesOf(block.side(), false));
            for (AbstractRecruitEntity s : living) {
                hunt(s, foes, block);
            }
            return;
        }
        if (l.appliedCharge) {
            // Recalled (spent cavalry): back into formation, to charge again later.
            l.appliedCharge = false;
            l.appliedLine = null;
        }
        if (l.appliedLine == null || l.appliedLine.distanceTo(block.line()) > 1 || l.appliedBraced != block.braced()) {
            l.appliedLine = block.line();
            l.appliedBraced = block.braced();
            FieldArmy army = field.army(block.side());
            if (block.real() < block.alive()) {
                // Part real, part figures: each real soldier takes its own place in the ranks, among the figures.
                for (int i = 0; i < l.soldiers.length; i++) {
                    AbstractRecruitEntity s = l.soldiers[i];
                    if (s != null && s.isAlive() && !commanded.contains(s.getId())) {
                        hold(s, slot(block, block.line(), i));
                        s.isInFormation = true;
                    }
                }
            } else if (living.size() == 1) {
                hold(living.get(0), block.line());
            } else if (block.braced()) {
                // A wall: closed up, a few files wide and deep, facing the cavalry.
                FormationUtils.lineFormation(new Vec3(block.facingX(), 0, block.facingZ()), new ArrayList<>(living),
                        Vec3.atBottomCenterOf(ground(block.line())), FieldRules.WALL_FILES, FieldRules.WALL_SPACING, true);
            } else {
                FormationUtils.lineFormation(new Vec3(block.facingX(), 0, block.facingZ()), new ArrayList<>(living),
                        Vec3.atBottomCenterOf(ground(block.line())), block.files(), BattlePlan.SPACING, true);
            }
        }
        faceTheEnemy(l, living);
    }

    /** The yaw a soldier looks along to face the direction (x, z). */
    private static float yawOf(Point direction) {
        return (float) Math.toDegrees(Math.atan2(-direction.x(), direction.z()));
    }

    /**
     * Soldiers standing in their places with nobody to fight turn to face their block's enemy (the block it fights,
     * else the nearest), rather than whichever way they last walked.
     */
    private void faceTheEnemy(Live l, List<AbstractRecruitEntity> living) {
        if (l.block.charged() || passive(l.block.duty())) {
            return;
        }
        float yaw = yawOf(field.enemyDirection(l.block));
        for (AbstractRecruitEntity s : living) {
            LivingEntity target = s.getTarget();
            if ((target == null || !target.isAlive()) && s.getNavigation().isDone()) {
                s.setYRot(yaw);
                s.setYBodyRot(yaw);
                s.setYHeadRot(yaw);
                if (s.getVehicle() instanceof LivingEntity horse) {
                    horse.setYRot(yaw);
                    horse.setYBodyRot(yaw);
                    horse.setYHeadRot(yaw);
                }
            }
        }
    }

    /** Soldiers on these duties hold back from fighting. */
    private static boolean passive(Duty duty) {
        return duty == Duty.WITHDRAWING || duty == Duty.BEARER || duty == Duty.FLEEING || duty == Duty.RALLYING || duty == Duty.ROUTED;
    }

    /**
     * Where real soldiers meet real enemies (its foe's, and those of any enemy block within {@link #ENGAGE_NEAR}), only
     * a block's front ranks fight, to spare the server: the {@link #FRONT_RANKS} ranks nearest the enemy (as many as
     * its ranks are wide, or a wall's {@link FieldRules#WALL_FILES}). Each goes after the nearest enemy within
     * {@link #FRONT_ROW_REACH} of its place, breaking formation to reach them, and comes back to its place once there is
     * none. The ranks behind hold their places, passive, until the front thins and they become it, but defend
     * themselves against an enemy within {@link #SELF_DEFENCE}. The numbers leave such a fight to Recruits.
     */
    private void frontRowFights(Live l) {
        FieldBlock block = l.block;
        List<AbstractRecruitEntity> living = l.living();
        living.removeIf(s -> commanded.contains(s.getId()));
        // The real enemies at hand: its foe's, and every real enemy block near it.
        FieldBlock foe = block.contactFoe();
        List<AbstractRecruitEntity> foes = new ArrayList<>();
        FieldBlock toward = foe != null && live.containsKey(foe) ? foe : null;
        double nearestBlock = Double.MAX_VALUE;
        if (block.arm() != Arm.CAVALRY && !block.charged() && !passive(block.duty()) && mayFight(block.side())) {
            for (Live e : live.values()) {
                if (e.block.side() == block.side()) {
                    continue;
                }
                double d = e.block.pos().distanceTo(block.pos());
                if (e.block == foe || d <= ENGAGE_NEAR) {
                    foes.addAll(e.living());
                    if (toward == null && d < nearestBlock) {
                        nearestBlock = d;
                    }
                }
            }
            if (toward == null && !foes.isEmpty()) {
                for (Live e : live.values()) {
                    if (e.block.side() != block.side() && e.block.pos().distanceTo(block.pos()) == nearestBlock) {
                        toward = e.block;
                    }
                }
            }
        }
        boolean fighting = !foes.isEmpty() && toward != null;
        if (!fighting) {
            if (l.frontRow) {
                l.frontRow = false;
                for (AbstractRecruitEntity s : living) {
                    s.holdFormation = true;
                    s.setAggroState(passive(block.duty()) ? 3 : 0);
                }
            }
            return;
        }
        l.frontRow = true;
        double dx = toward.pos().x() - block.pos().x();
        double dz = toward.pos().z() - block.pos().z();
        List<AbstractRecruitEntity> ranked = new ArrayList<>(living);
        ranked.sort(Comparator.comparingDouble(s -> {
            Vec3 place = placeOf(s);
            return -(place.x * dx + place.z * dz);
        }));
        int front = Math.min((block.braced() ? FieldRules.WALL_FILES : block.files()) * FRONT_RANKS, ranked.size());
        double reachSqr = FRONT_ROW_REACH * FRONT_ROW_REACH;
        double defenceSqr = SELF_DEFENCE * SELF_DEFENCE;
        for (int i = 0; i < ranked.size(); i++) {
            AbstractRecruitEntity s = ranked.get(i);
            if (i >= front) {
                // Behind the front: holds its place, unless an enemy comes at it.
                LivingEntity attacker = foes.stream().filter(f -> f.distanceToSqr(s) <= defenceSqr)
                        .min(Comparator.comparingDouble(f -> f.distanceToSqr(s))).orElse(null);
                if (attacker == null) {
                    attacker = nearestEnemyPlayer(block.side(), s.position(), defenceSqr, s);
                }
                s.setTarget(attacker);
                s.setAggroState(attacker == null ? 3 : 0);
                s.holdFormation = attacker == null;
                continue;
            }
            s.setAggroState(0);
            Vec3 place = placeOf(s);
            LivingEntity target = s.getTarget();
            if (target == null || !target.isAlive() || target.distanceToSqr(place) > reachSqr) {
                target = foes.stream().filter(f -> f.distanceToSqr(place) <= reachSqr)
                        .min(Comparator.comparingDouble(f -> f.distanceToSqr(s))).orElse(null);
                LivingEntity player = nearestEnemyPlayer(block.side(), place, reachSqr, s);
                if (player != null && (target == null || player.distanceToSqr(s) < target.distanceToSqr(s))) {
                    target = player;
                }
                s.setTarget(target);
            }
            s.holdFormation = target == null;
        }
    }

    /** Where a soldier is ordered to stand, or where it is if it has no place. */
    private static Vec3 placeOf(AbstractRecruitEntity soldier) {
        Vec3 place = soldier.getHoldPos();
        return place != null ? place : soldier.position();
    }

    /**
     * Spent cavalry in melee is slowed, its horses pressing on at half pace or less, so it can't ride straight through
     * the enemy: from full speed with full momentum to half with none.
     */
    private void slowSpentHorses(Live l) {
        FieldBlock block = l.block;
        double factor = block.charged() && block.inMelee() ? 0.5 + 0.5 * block.momentum() : 1;
        for (AbstractRecruitEntity rider : l.living()) {
            if (rider.getVehicle() instanceof LivingEntity horse) {
                AttributeInstance speed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speed == null) {
                    continue;
                }
                speed.removeModifier(SPENT_HORSE);
                if (factor < 1) {
                    speed.addTransientModifier(new AttributeModifier(SPENT_HORSE, "spent charge", factor - 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }

    /**
     * Whom a real cavalry block's riders are striking this second, for its momentum: the enemy block most of those
     * within striking distance of their target are fighting, if enough of them are; else nobody.
     */
    private void reportCavalryContact(Live l) {
        Map<FieldBlock, Integer> struck = new HashMap<>();
        List<AbstractRecruitEntity> riders = l.living();
        for (AbstractRecruitEntity rider : riders) {
            LivingEntity target = rider.getTarget();
            if (target == null || !target.isAlive() || rider.distanceTo(target) > STRIKING_DISTANCE) {
                continue;
            }
            Live foe = byEntity.get(target.getId());
            if (foe == null && !target.getPassengers().isEmpty()) {
                foe = byEntity.get(target.getPassengers().get(0).getId());
            }
            if (foe != null && foe.block.side() != l.block.side()) {
                struck.merge(foe.block, 1, Integer::sum);
            }
        }
        FieldBlock foe = struck.entrySet().stream().max(Map.Entry.comparingByValue())
                .filter(e -> e.getValue() >= Math.max(2, riders.size() / 5)).map(Map.Entry::getKey).orElse(null);
        l.block.setLiveContact(foe);
    }

    @Nullable
    private Live generalOf(int side) {
        FieldBlock commander = field.army(side).commander();
        return commander == null ? null : live.get(commander);
    }

    /** The soldiers of the block's prey, if it is real and any stand; else {@code otherwise}. */
    private List<AbstractRecruitEntity> preyOf(FieldBlock block, List<AbstractRecruitEntity> otherwise) {
        Live prey = block.prey() == null ? null : live.get(block.prey());
        List<AbstractRecruitEntity> soldiers = prey == null ? List.of() : prey.living();
        return soldiers.isEmpty() ? otherwise : soldiers;
    }

    /** The real soldiers fighting against {@code side}, its archers only if {@code archersFirst} and any are real. */
    private List<AbstractRecruitEntity> enemiesOf(int side, boolean archersFirst) {
        List<AbstractRecruitEntity> all = new ArrayList<>();
        List<AbstractRecruitEntity> archers = new ArrayList<>();
        for (Live l : live.values()) {
            if (l.block.side() != side && l.block.duty() != Duty.BEARER) {
                List<AbstractRecruitEntity> living = l.living();
                all.addAll(living);
                if (l.block.arm() == Arm.ARCHERS && l.block.duty() == Duty.SUPPORT) {
                    archers.addAll(living);
                }
            }
        }
        return archersFirst && !archers.isEmpty() ? archers : all;
    }

    /** Orders one soldier to stand at a spot (keeping it if it is already ordered close to there). */
    private void hold(AbstractRecruitEntity soldier, Point spot) {
        Vec3 at = soldier.getHoldPos();
        if (at != null && Math.hypot(at.x - spot.x(), at.z - spot.z()) < 2) {
            return;
        }
        soldier.setHoldPos(Vec3.atBottomCenterOf(ground(spot)));
        soldier.setFollowState(3);
    }

    private static void release(AbstractRecruitEntity soldier, List<AbstractRecruitEntity> prey) {
        soldier.isInFormation = false;
        soldier.holdFormation = false;
        soldier.setFollowState(0);
        nearestTo(soldier, prey).ifPresent(soldier::setTarget);
    }

    /**
     * A charged soldier with no foe goes after the nearest real enemy; with none near, it walks toward its block's
     * prey as the numbers have it, which may still be only numbers.
     */
    private void hunt(AbstractRecruitEntity soldier, List<AbstractRecruitEntity> foes, FieldBlock block) {
        LivingEntity target = soldier.getTarget();
        if (target != null && target.isAlive()) {
            return;
        }
        Optional<AbstractRecruitEntity> foe = nearestTo(soldier, foes);
        if (foe.isPresent() && foe.get().distanceTo(soldier) < HUNT_REACH) {
            soldier.setTarget(foe.get());
            soldier.getNavigation().moveTo(foe.get(), 1.0);
        } else {
            FieldBlock prey = block.prey() != null && block.prey().alive() > 0 ? block.prey() : nearestEnemyBlock(block);
            if (prey != null) {
                soldier.getNavigation().moveTo(prey.pos().x(), soldier.getY(), prey.pos().z(), 1.0);
            }
        }
    }

    @Nullable
    private FieldBlock nearestEnemyBlock(FieldBlock block) {
        FieldBlock best = null;
        double bestDistance = Double.MAX_VALUE;
        for (FieldBlock e : field.army(1 - block.side()).blocks()) {
            if (e.alive() > 0 && e.pos().distanceTo(block.pos()) < bestDistance) {
                bestDistance = e.pos().distanceTo(block.pos());
                best = e;
            }
        }
        return best;
    }

    private static Optional<AbstractRecruitEntity> nearestTo(Entity soldier, List<AbstractRecruitEntity> candidates) {
        return candidates.stream().min(Comparator.comparingDouble(c -> c.distanceToSqr(soldier)));
    }

    /**
     * Deals the numbers' damage to a real block: whole hits to random living soldiers, real ones past their armour,
     * those still numbers in the numbers.
     */
    private void dealDamage(Live l) {
        l.damageCarry += l.block.drainLiveDamage();
        float[] health = l.block.health();
        List<Integer> standing = new ArrayList<>();
        for (int i = 0; i < health.length; i++) {
            AbstractRecruitEntity s = l.soldiers[i];
            if (s != null ? s.isAlive() : health[i] > 0) {
                standing.add(i);
            }
        }
        while (l.damageCarry >= FieldRules.HIT && !standing.isEmpty()) {
            l.damageCarry -= FieldRules.HIT;
            int pick = level.random.nextInt(standing.size());
            int i = standing.get(pick);
            AbstractRecruitEntity s = l.soldiers[i];
            boolean fell;
            if (s != null) {
                s.hurt(level.damageSources().magic(), FieldRules.HIT);
                fell = !s.isAlive();
            } else {
                l.block.hurt(i, FieldRules.HIT);
                health[i] -= FieldRules.HIT;
                fell = health[i] <= 0;
            }
            if (fell) {
                standing.remove(pick);
            }
        }
        if (standing.isEmpty()) {
            l.damageCarry = 0;
        }
    }

    /** Recruits gives cavalry their horse on their first tick; dress each horse in the army's colour once. */
    private void armourHorses(Live l) {
        for (AbstractRecruitEntity rider : l.living()) {
            if (rider.getVehicle() instanceof Horse horse && ours.add(horse.getUUID())) {
                boolean iron = l.block.duty() == Duty.GUARD || l.block.duty() == Duty.GENERAL;
                horse.getSlot(401).set(iron ? new ItemStack(Items.IRON_HORSE_ARMOR) // 401: the horse's armour slot
                        : dyed(Items.LEATHER_HORSE_ARMOR, sides[l.block.side()].uniform));
            }
        }
    }

    // ---- ticking ----

    public static void onServerTickStart() {
        if (current != null) {
            current.tickStart = System.nanoTime();
        }
    }

    public static void onServerTickEnd() {
        RecruitsBattle battle = current;
        if (battle == null) {
            return;
        }
        long ownStart = System.nanoTime();
        if (++battle.ticks % 20 == 0) {
            battle.second();
        }
        battle.council.tick(battle.level.getServer(), battle.level.getGameTime());
        if (battle.ticks % 2 == 0) {
            battle.driveUnits();
        }
        if (battle.ticks % 4 == 0) {
            battle.trample();
        }
        battle.fadeSome();
        if (++battle.snapshotTicks % SNAPSHOT_TICKS == 0) {
            battle.holdChunks();
            long t = System.nanoTime();
            ConquestNetwork.sendBattleSnapshot(battle.level.players(), battle.snapshots());
            battle.snapshotNanos += System.nanoTime() - t;
        }
        battle.ownNanos += System.nanoTime() - ownStart;
        if (battle.tickStart != 0) {
            long nanos = System.nanoTime() - battle.tickStart;
            battle.whole.add(nanos);
            battle.window.add(nanos);
        }
    }

    /**
     * One second: real soldiers report in, the numeric battle runs its second (orders, blocks that are only numbers
     * move, fights with them are worked out), the orders and damage are passed on to real soldiers, and blocks switch
     * between real and numbers as players move.
     */
    private void second() {
        clock++;
        switch (phase) {
            case DEPLOYMENT -> {
                deployment();
                return;
            }
            case PULLBACK, NIGHT -> {
                night();
                return;
            }
            case PURSUIT, AFTERMATH -> {
                aftermath();
                return;
            }
            case DONE -> {
                for (Live l : new ArrayList<>(live.values())) {
                    readBack(l);
                    passOnOrders(l);
                }
                switchLive();
                return;
            }
            default -> {
            }
        }
        for (Live l : new ArrayList<>(live.values())) {
            readBack(l);
        }
        if (!over) {
            measure();
        }
        for (Live l : live.values()) {
            if (l.block.arm() == Arm.CAVALRY) {
                reportCavalryContact(l);
            }
        }
        long t = System.nanoTime();
        field.second();
        fieldNanos += System.nanoTime() - t;
        sendVolleys();
        for (FieldBattle.Order order : field.takeCalls()) {
            soundHorn(order.side());
            if (order.call() == FieldBattle.Call.ADVANCE) {
                announce(order.side(), "ALL TROOPS CHARGE!");
            }
        }
        for (FieldBattle.Event event : field.takeEvents()) {
            Side side = sides[event.side()];
            // The order itself in the nation's colour, so it stands out from the reports.
            String who = String.format(Locale.ROOT, "[battle %ds] %s's General %s ", field.seconds(), side.faction.name(), side.general.name());
            TextColor color = color(event.side());
            broadcast(who + event.text(), Component.literal(who).append(Component.literal(event.text()).withStyle(style -> style.withColor(color))));
        }
        for (Live l : new ArrayList<>(live.values())) {
            dealDamage(l);
            passOnOrders(l);
            frontRowFights(l);
            raiseShields(l);
            if (l.block.arm() == Arm.CAVALRY) {
                armourHorses(l);
                slowSpentHorses(l);
            }
        }
        supportOrders();
        updateEnlisted();
        t = System.nanoTime();
        switchLive();
        switchNanos += System.nanoTime() - t;
        if (!over) {
            announce();
            if (field.seconds() % REPORT_SECONDS == 0) {
                report();
            }
            if (field.over()) {
                end();
            } else if (evening() && clock - phaseSince >= MIN_BATTLE_SECONDS) {
                beginPullback();
            }
        }
    }

    /**
     * Before the fighting: blocks near players become real and stand where they are placed, and nothing else
     * happens (no orders, no fighting) until the battle is started.
     */
    private void prepare() {
        for (Live l : new ArrayList<>(live.values())) {
            readBack(l);
            passOnOrders(l);
        }
        switchLive();
    }

    /** Starts the fighting of the battle being prepared. */
    public static String startFighting() {
        RecruitsBattle battle = current;
        if (battle == null) {
            return "No battle to start";
        }
        if (battle.started) {
            return "The battle has already begun";
        }
        battle.enlist(); // anyone who joined a side since
        battle.field.formed();
        battle.hideDeploymentBars();
        for (Enlisted e : battle.enlisted.values()) {
            e.marching = false;
            battle.removeMessenger(e);
        }
        battle.started = true;
        battle.dismissCouncil();
        battle.setPhase(Phase.BATTLE);
        battle.broadcast(String.format(Locale.ROOT, "[battle day %d] The battle begins", battle.day));
        return "The battle begins";
    }

    // ---- for the battle map (dev/BattleMap) ----

    @Nullable
    public static RecruitsBattle current() {
        return current;
    }

    public FieldBattle field() {
        return field;
    }

    public boolean started() {
        return started;
    }

    public boolean isOver() {
        return over;
    }

    public String factionName(int side) {
        return sides[side].faction.name();
    }

    /** The side's uniform colour, as RGB. */
    public int uniform(int side) {
        return sides[side].uniform;
    }

    public GameCharacter general(int side) {
        return sides[side].general;
    }

    /** The latest lines told to everyone, oldest first. */
    public List<String> recentLines() {
        return List.copyOf(lines);
    }

    /** A side's named groups of blocks, by block id. */
    public Map<String, List<Integer>> groups(int side) {
        return groups.get(side);
    }

    // ---- enlisted players ----

    /**
     * At the start, each player on one of the battle's teams (by /team join) is enlisted with that side: they are put
     * in the front block nearest the middle of the line (the first to charge), teleported to its front, and given the
     * soldiers of their block and the blocks beside it to command, as many as their rank leads (a Captain's 100 at
     * most; officers' commands come later), following them.
     */
    private void enlist() {
        Set<FieldBlock> taken = new HashSet<>();
        Ranks ranks = Ranks.get(level.getServer());
        for (ServerPlayer player : level.players()) {
            if (player.getTeam() == null) {
                continue;
            }
            for (int side = 0; side < 2; side++) {
                if (!player.getTeam().getName().equals(sides[side].team.getName())) {
                    continue;
                }
                if (enlisted.containsKey(player.getUUID())) {
                    continue; // enlisted already, for the day
                }
                FieldArmy army = field.army(side);
                Job job = ranks.job(player.getUUID());
                BattleRole role = dayRoles.containsKey(player.getUUID()) ? dayRoles.get(player.getUUID()).role()
                        : ranks.role(player.getUUID());
                List<FieldBlock> front = unitPool(army, role, taken);
                if (front.isEmpty()) {
                    continue;
                }
                FieldBlock home = front.get(0);
                Rank rank = ranks.of(player.getUUID());
                int leads = Math.min(rank.leads(), Rank.CAPTAIN.leads());
                List<FieldBlock> blocks = new ArrayList<>(List.of(home));
                List<FieldBlock> beside = new ArrayList<>(front.subList(1, front.size()));
                beside.sort(Comparator.comparingDouble(b -> b.pos().distanceTo(home.pos())));
                for (FieldBlock b : beside) {
                    if (blocks.size() * BattlePlan.SOLDIERS_PER_FORMATION >= leads) {
                        break;
                    }
                    blocks.add(b);
                }
                taken.addAll(blocks);
                Enlisted e = new Enlisted(player.getUUID(), side, rank, home, blocks, leads - 1);
                e.job = job;
                for (FieldBlock b : blocks) {
                    field.assign(b, b == home || role != BattleRole.BAIT ? task(role) : FieldBlock.Task.NONE);
                }
                e.formYaw = yawOf(new Point(army.facingX(), army.facingZ()));
                CommandEvents.saveFormation(player, SQUARE_FORMATION);
                enlisted.put(player.getUUID(), e);
                WarCamp camp = tentCamps.computeIfAbsent(player.getUUID(), id -> campOf(e.side, home.wing()));
                e.tentCamp = camp;
                e.tent = tents.computeIfAbsent(player.getUUID(), id -> {
                    int used = camp == null ? 0 : tentsTaken.getOrDefault(camp, 0);
                    if (camp == null || used >= camp.tents()) {
                        return -1;
                    }
                    tentsTaken.put(camp, used + 1);
                    return used;
                });
                if (day == 1) {
                    // The first day, straight to their place; on later days they walk out of camp with their unit.
                    Point spot = home.line();
                    player.teleportTo(level, spot.x(), groundY(home, spot.x(), spot.z()), spot.z(),
                            yawOf(new Point(army.facingX(), army.facingZ())), 0);
                }
                kitPlayer(player, e);
                player.sendSystemMessage(Component.literal(String.format(Locale.ROOT,
                        "You fight for %s as a %s of %s, %s, leading %d. Keep to your ring.",
                        sides[side].faction.name(), rank.title(), job.title(), roleTitle(role), leads)).withStyle(style -> style.withColor(color(e.side))));
            }
        }
    }

    /**
     * The blocks a player's unit is drawn from, by their role: the front line for frontline and bait (the block nearest
     * the middle first), the reserves behind it for support and kill squads (nearest the middle and the front first),
     * the reserves of a wing for flankers (the outermost first). Only infantry blocks no one else has taken; the front
     * line if the reserves are empty.
     */
    private static List<FieldBlock> unitPool(FieldArmy army, BattleRole role, Set<FieldBlock> taken) {
        boolean reserve = role == BattleRole.SUPPORT || role == BattleRole.FLANKER || role == BattleRole.KILL_SQUAD;
        List<FieldBlock> pool = new ArrayList<>();
        for (int pass = 0; pass < 2 && pool.isEmpty(); pass++) {
            Duty duty = reserve && pass == 0 ? Duty.LINE : Duty.FRONT;
            for (FieldBlock b : army.blocks()) {
                if (b.duty() == duty && b.alive() > 0 && b.arm().infantry() && b.task() == FieldBlock.Task.NONE && !taken.contains(b)) {
                    pool.add(b);
                }
            }
        }
        Comparator<FieldBlock> order = Comparator.comparingDouble(b -> {
            Point at = BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), b.line());
            return role == BattleRole.FLANKER ? -Math.abs(at.x()) + Math.abs(at.z()) * 0.5 : Math.abs(at.x()) + Math.abs(at.z()) * 0.5;
        });
        pool.sort(order);
        return pool;
    }

    /** Where a player's unit fights, as told them at the start. */
    private static String roleTitle(BattleRole role) {
        return switch (role) {
            case FRONTLINE -> "in the first block to charge";
            case BAIT -> "as bait, running ahead of the line";
            case SUPPORT -> "in support, reinforcing your wing's losing blocks";
            case FLANKER -> "flanking: striking enemy blocks from the side or behind";
            case KILL_SQUAD -> "in a kill squad, hunting the enemy's officers";
        };
    }

    private static String capitalised(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static FieldBlock.Task task(BattleRole role) {
        return switch (role) {
            case BAIT -> FieldBlock.Task.BAIT;
            case SUPPORT -> FieldBlock.Task.SUPPORT;
            case FLANKER -> FieldBlock.Task.FLANK;
            case KILL_SQUAD -> FieldBlock.Task.KILL;
            case FRONTLINE -> FieldBlock.Task.NONE;
        };
    }

    /** What a player's soldiers are, by the player's job. */
    private static EntityType<? extends AbstractRecruitEntity> jobType(Job job) {
        return switch (job) {
            case CAVALRY -> ModEntityTypes.HORSEMAN.get();
            case FOOTMAN -> ModEntityTypes.RECRUIT.get();
            case ARCHER -> ModEntityTypes.BOWMAN.get();
            case SHIELDMAN, HEAVY_INFANTRY -> ModEntityTypes.RECRUIT_SHIELDMAN.get();
        };
    }

    /**
     * Soldier i of a block is given to a player: remade as their job's kind of soldier if it isn't one already (where
     * it stands, with the health it has), and given their job's kit. Returns the soldier, or null if it couldn't be.
     */
    @Nullable
    private AbstractRecruitEntity enlistSoldier(Live l, int i, AbstractRecruitEntity s, Enlisted e) {
        Side side = sides[l.block.side()];
        EntityType<? extends AbstractRecruitEntity> type = jobType(e.job);
        AbstractRecruitEntity soldier = s;
        if (s.getType() != type) {
            float health = s.getHealth();
            Point at = new Point(s.getX(), s.getZ());
            float yaw = s.getYRot();
            makeNumbers(l, i);
            soldier = spawn(type, side, i == l.block.captain() ? Role.LEADER : Role.SOLDIER, at, yaw);
            if (soldier == null) {
                return null;
            }
            soldier.setHealth(Math.min(health, soldier.getMaxHealth()));
            l.soldiers[i] = soldier;
            byEntity.put(soldier.getId(), l);
        }
        boolean leader = i == l.block.captain();
        switch (e.job) {
            case FOOTMAN -> equip(soldier, EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            case SHIELDMAN -> equip(soldier, EquipmentSlot.OFFHAND, bannerShield(side));
            case HEAVY_INFANTRY -> {
                equip(soldier, EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                equip(soldier, EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                equip(soldier, EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                if (!leader) {
                    equip(soldier, EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET)); // the captain keeps the banner
                }
                equip(soldier, EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                equip(soldier, EquipmentSlot.OFFHAND, bannerShield(side));
            }
            default -> {
            }
        }
        return soldier;
    }

    /** A shield bearing the nation's banner. */
    private static ItemStack bannerShield(Side side) {
        ItemStack shield = new ItemStack(Items.SHIELD);
        CompoundTag pattern = side.banner.getTagElement("BlockEntityTag");
        CompoundTag tag = pattern == null ? new CompoundTag() : pattern.copy();
        DyeColor base = side.banner.getItem() instanceof BannerItem banner ? banner.getColor() : DyeColor.WHITE;
        tag.putInt("Base", base.getId());
        shield.getOrCreateTag().put("BlockEntityTag", tag);
        return shield;
    }

    /**
     * The player's own kit for their job, taken back when the battle ends: a horse to ride (cavalry), an iron sword
     * with Sweeping Edge (footman), a bow with Infinity and Unbreaking II (archer), the nation's shield (shieldman),
     * iron armour, an axe and the nation's shield (heavy infantry).
     */
    private void kitPlayer(ServerPlayer player, Enlisted e) {
        Side side = sides[e.side];
        List<ItemStack> kit = new ArrayList<>();
        switch (e.job) {
            case CAVALRY -> {
                Horse horse = EntityType.HORSE.create(level);
                if (horse != null) {
                    horse.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
                    horse.setTamed(true);
                    horse.setOwnerUUID(player.getUUID());
                    horse.equipSaddle(null);
                    horse.getSlot(401).set(dyed(Items.LEATHER_HORSE_ARMOR, side.uniform)); // 401: the armour slot
                    horse.setPersistenceRequired();
                    level.addFreshEntity(horse);
                    player.startRiding(horse, true);
                    e.horse = horse.getUUID();
                }
            }
            case FOOTMAN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                sword.enchant(Enchantments.SWEEPING_EDGE, 3);
                kit.add(sword);
            }
            case ARCHER -> {
                ItemStack bow = new ItemStack(Items.BOW);
                bow.enchant(Enchantments.INFINITY_ARROWS, 1);
                bow.enchant(Enchantments.UNBREAKING, 2);
                kit.add(bow);
                kit.add(new ItemStack(Items.ARROW));
            }
            case SHIELDMAN -> kit.add(bannerShield(side));
            case HEAVY_INFANTRY -> {
                kit.add(new ItemStack(Items.IRON_AXE));
                kit.add(bannerShield(side));
                kit.add(new ItemStack(Items.IRON_HELMET));
                kit.add(new ItemStack(Items.IRON_CHESTPLATE));
                kit.add(new ItemStack(Items.IRON_LEGGINGS));
                kit.add(new ItemStack(Items.IRON_BOOTS));
            }
        }
        kit.add(WarHorn.make(side.horn)); // every enlisted player commands their units with it
        for (ItemStack stack : kit) {
            stack.getOrCreateTag().putBoolean(KIT_TAG, true);
            player.getInventory().add(stack);
        }
    }

    /** The battle is over for an enlisted player: their kit is taken back and their horse led away. */
    private void unkitPlayer(@Nullable ServerPlayer player, Enlisted e) {
        if (e.horse != null) {
            Entity horse = level.getEntity(e.horse);
            if (horse != null) {
                horse.ejectPassengers();
                horse.discard();
            }
        }
        if (player == null) {
            return;
        }
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.hasTag() && stack.getTag().getBoolean(KIT_TAG)) {
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    /**
     * Cavalry momentum for a cavalry player and their riders, as for cavalry blocks: gathered riding (full after
     * {@link FieldRules#RUN_UP} blocks), spent each second in melee (more against cavalry, all of it against a braced
     * shield struck from the front), and while spent in melee their horses slow to half pace.
     */
    private void cavalryMomentum(Enlisted e, ServerPlayer player) {
        Entity mount = player.getVehicle();
        if (mount == null) {
            e.momentum = 0;
            e.lastRide = null;
        } else {
            Vec3 at = mount.position();
            double moved = e.lastRide == null ? 0 : Math.hypot(at.x - e.lastRide.x, at.z - e.lastRide.z);
            e.lastRide = at;
            e.momentum = nextMomentum(e.momentum, moved, e.playerContact, e.playerContactCavalry);
            slow(mount, e.playerContact ? 0.5 + 0.5 * e.momentum : 1);
        }
        List<AbstractRecruitEntity> riders = soldiersOf(e);
        double x = 0;
        double z = 0;
        int n = 0;
        for (AbstractRecruitEntity rider : riders) {
            Entity horse = rider.getVehicle();
            if (horse == null) {
                continue;
            }
            if (horse instanceof Horse h && ours.add(h.getUUID())) {
                h.getSlot(401).set(dyed(Items.LEATHER_HORSE_ARMOR, sides[e.side].uniform));
            }
            x += horse.getX();
            z += horse.getZ();
            n++;
        }
        if (n > 0) {
            Point centre = new Point(x / n, z / n);
            double moved = e.lastUnitCentre == null ? 0 : centre.distanceTo(e.lastUnitCentre);
            e.lastUnitCentre = centre;
            e.unitMomentum = nextMomentum(e.unitMomentum, moved, e.unitContact, e.unitContactCavalry);
            double factor = e.unitContact ? 0.5 + 0.5 * e.unitMomentum : 1;
            for (AbstractRecruitEntity rider : riders) {
                if (rider.getVehicle() != null) {
                    slow(rider.getVehicle(), factor);
                }
            }
        }
        e.playerContact = false;
        e.playerContactCavalry = false;
        e.unitContact = false;
        e.unitContactCavalry = false;
    }

    private static double nextMomentum(double momentum, double moved, boolean contact, boolean againstCavalry) {
        if (contact) {
            return Math.max(0, momentum - (againstCavalry ? FieldRules.MOMENTUM_LOSS_CAVALRY : FieldRules.MOMENTUM_LOSS_INFANTRY));
        }
        return Math.min(1, momentum + moved / FieldRules.RUN_UP);
    }

    private static void slow(Entity horse, double factor) {
        if (!(horse instanceof LivingEntity living)) {
            return;
        }
        AttributeInstance speed = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        speed.removeModifier(SPENT_HORSE);
        if (factor < 1) {
            speed.addTransientModifier(new AttributeModifier(SPENT_HORSE, "spent charge", factor - 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    /** The enlisted player whose soldier this is, or null. */
    @Nullable
    private Enlisted commanderOf(Entity soldier) {
        if (!commanded.contains(soldier.getId())) {
            return null;
        }
        for (Enlisted e : enlisted.values()) {
            if (e.given.contains(soldier.getId())) {
                return e;
            }
        }
        return null;
    }

    /**
     * Whether a rider strikes at a gallop: its horse covered at least {@link #GALLOP} blocks in the last tick. A
     * charge's momentum tells only at impact; a rider standing in the melee hacks like anyone else.
     */
    private static boolean galloping(Entity rider) {
        Entity horse = rider.getVehicle();
        return horse != null && Math.hypot(horse.getX() - horse.xo, horse.getZ() - horse.zo) >= GALLOP;
    }

    /** Whether {@code attacker} stands in front of {@code defender}, as the defender faces. */
    private static boolean inFront(Entity defender, Entity attacker) {
        double yaw = Math.toRadians(defender.getYRot());
        return FieldRules.side(-Math.sin(yaw), Math.cos(yaw), attacker.getX() - defender.getX(), attacker.getZ() - defender.getZ())
                == FieldRules.Side.FRONT;
    }

    /**
     * The players' units' rules on a blow: a cavalry player's or their riders' charge does 1 + {@link
     * FieldRules#CHARGE_BONUS} x their momentum times as much, and puts them in melee; a soldier of a player's unit with
     * its shield up (Recruits' Shields command) is braced like a wall against riders striking its front: it takes
     * {@link FieldRules#BRACED_TAKES} of the blow and the rider's charge is spent, and it deals {@link
     * FieldRules#BRACED_DEALS} times as much to riders in front of it.
     */
    private float unitRules(Entity victim, Entity attacker, float amount) {
        Live of = byEntity.get(victim.getId());
        boolean victimRides = victim.getVehicle() != null;
        Enlisted rider = null;
        boolean player = false;
        for (Enlisted e : enlisted.values()) {
            if (e.job == Job.CAVALRY && attacker.getUUID().equals(e.player) && attacker.getVehicle() != null) {
                rider = e;
                player = true;
            }
        }
        if (rider == null && attacker.getVehicle() != null) {
            Enlisted e = commanderOf(attacker);
            if (e != null && e.job == Job.CAVALRY) {
                rider = e;
            }
        }
        if (rider != null) {
            boolean cavalry = victimRides || of != null && of.block.arm() == Arm.CAVALRY;
            if (galloping(attacker)) {
                amount *= (float) (1 + FieldRules.CHARGE_BONUS * (player ? rider.momentum : rider.unitMomentum));
            }
            if (player) {
                rider.playerContact = true;
                rider.playerContactCavalry |= cavalry;
            } else {
                rider.unitContact = true;
                rider.unitContactCavalry |= cavalry;
            }
        }
        for (Enlisted e : enlisted.values()) {
            boolean struck = victim.getUUID().equals(e.player) && byEntity.containsKey(attacker.getId());
            if (struck || attacker.getUUID().equals(e.player) && byEntity.containsKey(victim.getId())) {
                e.lastCombat = level.getGameTime();
            }
            if (e.job == Job.CAVALRY && struck) {
                e.playerContact = true; // struck in melee: the charge is spent all the same
            }
        }
        Enlisted shielded = commanderOf(victim);
        if (shielded != null && victim instanceof AbstractRecruitEntity s && s.getShouldBlock() && attacker.getVehicle() != null
                && inFront(victim, attacker)) {
            amount *= (float) FieldRules.BRACED_TAKES;
            Live by = byEntity.get(attacker.getId());
            if (by != null && by.block.arm() == Arm.CAVALRY) {
                field.breakCharge(by.block);
            }
            if (rider != null) {
                if (player) {
                    rider.momentum = 0;
                } else {
                    rider.unitMomentum = 0;
                }
            }
        }
        Enlisted bracing = commanderOf(attacker);
        if (bracing != null && attacker instanceof AbstractRecruitEntity s && s.getShouldBlock() && victimRides && inFront(attacker, victim)) {
            amount *= (float) FieldRules.BRACED_DEALS;
        }
        return amount;
    }

    /** Each second: enlisted players get their soldiers as they become real, their ring, and their orders. */
    private void updateEnlisted() {
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null) {
                continue;
            }
            if (giveSoldiers(e, player) && RecruitEvents.recruitsGroupsManager != null) {
                RecruitEvents.recruitsGroupsManager.broadCastGroupsToPlayer(player);
            }
            campLife(e, player);
            unitOrders(e);
            strikeFigures(e, player);
            if (e.job == Job.CAVALRY) {
                cavalryMomentum(e, player);
            }
            markers(e, player);
            ordersFor(e, player);
        }
    }

    /**
     * The player's soldiers, from their block and those beside it as they become real: owned by them and in a group
     * of theirs, so Recruits' command screen lists them. Returns whether any were given.
     */
    private boolean giveSoldiers(Enlisted e, ServerPlayer player) {
        boolean any = false;
        for (FieldBlock b : e.blocks) {
            Live l = live.get(b);
            if (l == null) {
                continue;
            }
            for (int i = 0; i < l.soldiers.length; i++) {
                AbstractRecruitEntity s = l.soldiers[i];
                if (s == null || !s.isAlive() || commanded.contains(s.getId())) {
                    continue;
                }
                if (e.given.size() >= e.quota) {
                    return any;
                }
                s = enlistSoldier(l, i, s, e);
                if (s != null && commanded.add(s.getId())) {
                    e.given.add(s.getId());
                    any = true;
                    s.setOwnerUUID(Optional.of(e.player));
                    s.setIsOwned(true);
                    s.setAggroState(0);
                    s.setTarget(null);
                    Unit u = unitFor(e, b, player);
                    u.ids.add(s.getId());
                    if (u.group != null) {
                        s.setGroupUUID(u.group);
                        RecruitEvents.recruitsGroupsManager.addMember(u.group, s.getUUID(), level);
                    }
                }
            }
        }
        return any;
    }

    /** The player's soldiers that are still alive. */
    private List<AbstractRecruitEntity> soldiersOf(Enlisted e) {
        List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        for (Unit u : e.units) {
            soldiers.addAll(soldiersOf(u));
        }
        return soldiers;
    }

    /**
     * Soldiers with shields raise them within reach of enemy archers, or near enemies they aren't themselves striking,
     * so long as they're near their place (a soldier with its shield up walks at half pace or less). A soldier
     * striking, charging or in a passive duty lowers it, leaving Recruits' own blocking between blows. Soldiers whose
     * player has taken them over are left alone.
     */
    private void raiseShields(Live l) {
        FieldBlock block = l.block;
        boolean archers = false;
        for (FieldBlock enemy : field.army(1 - block.side()).blocks()) {
            if (enemy.arm() == Arm.ARCHERS && enemy.alive() > 0 && enemy.pos().distanceTo(block.pos()) < ARROW_REACH) {
                archers = true;
                break;
            }
        }
        for (AbstractRecruitEntity s : l.living()) {
            if (!s.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) || commanded.contains(s.getId())) {
                // A player's soldiers raise their shields at the player's order (Recruits' Shields command).
                continue;
            }
            LivingEntity target = s.getTarget();
            boolean striking = target != null && target.isAlive() && target.distanceToSqr(s) < 9;
            Vec3 hold = s.getHoldPos();
            boolean inPlace = hold == null || hold.distanceToSqr(s.position()) < 9;
            boolean threatened = archers || enemyWithin(block, s, MELEE_SHIELD_REACH);
            boolean raise = threatened && inPlace && !striking && !block.charged() && !passive(block.duty());
            if (raise != s.getShouldBlock()) {
                s.setShouldBlock(raise);
            }
        }
    }

    /** Whether a real enemy soldier stands within reach of a soldier (looking only at its block's foe and nearby blocks). */
    private boolean enemyWithin(FieldBlock block, AbstractRecruitEntity s, double reach) {
        FieldBlock foe = block.contactFoe();
        if (foe == null) {
            return false;
        }
        Live enemy = live.get(foe);
        if (enemy == null) {
            return false;
        }
        double sq = reach * reach;
        for (AbstractRecruitEntity f : enemy.living()) {
            if (f.distanceToSqr(s) < sq) {
                return true;
            }
        }
        return false;
    }

    /**
     * An enlisted player's orders: above the hotbar, in their faction's colour, what their block is doing (Charge!,
     * Pullback!, Rotate!, Retreat!); in the middle of the screen, what their Marshal or general orders (hold the wing,
     * target a block, kill an officer).
     */
    private void ordersFor(Enlisted e, ServerPlayer player) {
        FieldBlock b = e.home;
        String now = null;
        if (b.duty() == Duty.ROUTED) {
            now = "Retreat!";
        } else if (b.duty() == Duty.WITHDRAWING) {
            now = "Pullback!";
        } else if (e.lastDuty == Duty.FRONT && b.duty() != Duty.FRONT) {
            now = "Rotate!";
        } else if ((b.duty() == Duty.FRONT || b.strikeAt() != null) && b.fighting()) {
            now = "Charge!";
        }
        e.lastDuty = b.duty();
        if (now != null && !now.equals(e.order)) {
            e.order = now;
            e.orderSince = field.seconds();
        }
        if (!e.order.isEmpty() && field.seconds() - e.orderSince < ORDER_SHOWN_SECONDS) {
            player.displayClientMessage(Component.literal(e.order).withStyle(style -> style.withColor(color(e.side)).withBold(true)), true);
        }
        FieldArmy army = field.army(e.side);
        if (!e.toldHold && army.holding(b.wing())) {
            e.toldHold = true;
            title(e, "Hold the " + wingTitle(b.wing(), army.winged()) + "!");
        }
        FieldBlock strike = b.strikeAt();
        if (strike != null && strike != e.lastStrike && strike.alive() > 0 && strike == field.army(1 - e.side).commander()) {
            title(e, "Kill General " + sides[1 - e.side].general.name() + "!");
        } else if (strike != null && strike != e.lastStrike && strike.alive() > 0) {
            title(e, "Target " + sides[1 - e.side].faction.name() + "'s " + wingTitle(strike.wing(), field.army(1 - e.side).winged())
                    + " block!");
        }
        e.lastStrike = strike;
        FieldBlock prey = b.prey();
        if (!e.toldKill && prey != null && prey.duty() == Duty.GENERAL && (b.charged() || b.approaching())) {
            e.toldKill = true;
            title(e, "Kill General " + sides[1 - e.side].general.name() + "!");
        }
        showTitle(e, player);
    }

    /** A Marshal's or general's order, in big letters in the middle of the screen of every player enlisted with a side. */
    private void announce(int side, String text) {
        for (Enlisted e : enlisted.values()) {
            if (e.side == side) {
                title(e, text);
            }
        }
    }

    /** Queues an officer's order for an enlisted player; {@link #showTitle} shows it when the last is old enough. */
    private static void title(Enlisted e, String text) {
        e.pendingTitle = text;
    }

    private void showTitle(Enlisted e, ServerPlayer player) {
        if (e.pendingTitle == null || field.seconds() - e.titleAt < OFFICER_ORDER_SECONDS) {
            return;
        }
        String text = e.pendingTitle;
        int side = e.side;
        e.pendingTitle = null;
        e.titleAt = field.seconds();
        player.connection.send(new ClientboundSetTitlesAnimationPacket(5, 50, 15));
        player.connection.send(new ClientboundSetTitleTextPacket(Component.literal(text).withStyle(style -> style.withColor(color(side)))));
    }

    private static String wingTitle(int wing, boolean winged) {
        if (!winged) {
            return "line";
        }
        return wing == FieldArmy.LEFT ? "left wing" : wing == FieldArmy.RIGHT ? "right wing" : "centre";
    }

    /** A side's colour for its orders: its faction's main colour. */
    private TextColor color(int side) {
        return TextColor.fromRgb(mainColor(sides[side].faction));
    }

    /** The nearest player enlisted against {@code side}, within reach of a soldier's place, or null. */
    @Nullable
    private LivingEntity nearestEnemyPlayer(int side, Vec3 place, double reachSqr, AbstractRecruitEntity soldier) {
        LivingEntity best = null;
        for (Enlisted e : enlisted.values()) {
            if (e.side == side) {
                continue;
            }
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null || !player.isAlive() || player.isSpectator() || player.isCreative() || player.level() != level
                    || player.distanceToSqr(place) > reachSqr) {
                continue;
            }
            if (best == null || player.distanceToSqr(soldier) < best.distanceToSqr(soldier)) {
                best = player;
            }
        }
        return best;
    }

    // ---- a player's units: one per block, moved with the player or by the war horn's orders ----

    /**
     * How a player's unit moves: in formation with the player (moving as they move, turning as they turn until they
     * fight), held where it stands, sent to a place, let loose on the enemy, or left to the orders the player gave it in
     * Recruits' own command screen.
     */
    public enum UnitMode {
        FOLLOW, HOLD, MOVE, CHARGE, RECRUITS
    }

    /** A unit's formation: a square, or a line {@link #LINE_FILES} wide. */
    public enum Shape {
        SQUARE, LINE
    }

    /** A war horn order, given in the horn's screen. */
    public enum UnitOrder {
        FOLLOW, HOLD, MOVE, CHARGE, SQUARE, LINE, SHIELDS_UP, SHIELDS_DOWN
    }

    /** One block's worth of a player's soldiers: its own group in Recruits, its own formation, its own orders. */
    static final class Unit {
        final FieldBlock block;
        final int number;
        @Nullable
        UUID group;
        final Set<Integer> ids = new LinkedHashSet<>();
        UnitMode mode = UnitMode.FOLLOW;
        Shape shape = Shape.SQUARE;
        boolean shields;
        /** Where it holds, and which way it faces (held or sent somewhere). */
        @Nullable
        Vec3 at;
        float yaw;
        /** Where the battle last put each soldier, to tell the player's own Recruits orders from ours. */
        final Map<Integer, Vec3> placed = new HashMap<>();
        /** Soldiers whose places must be set again (new, or the unit was given another order). */
        boolean replace = true;

        Unit(FieldBlock block, int number) {
            this.block = block;
            this.number = number;
        }
    }

    /** The unit a block's soldiers make for a player, made (with its own Recruits group) the first time. */
    private Unit unitFor(Enlisted e, FieldBlock block, ServerPlayer player) {
        for (Unit u : e.units) {
            if (u.block == block) {
                return u;
            }
        }
        Unit u = new Unit(block, e.units.size() + 1);
        if (RecruitEvents.recruitsGroupsManager != null) {
            RecruitsGroup group = new RecruitsGroup(capitalised(e.job.title()) + " " + u.number, player, 0);
            RecruitEvents.recruitsGroupsManager.addOrUpdateGroup(level, player, group);
            u.group = group.getUUID();
        }
        e.units.add(u);
        return u;
    }

    /** A unit's soldiers still standing, in the order they joined. */
    private List<AbstractRecruitEntity> soldiersOf(Unit u) {
        List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        Live l = live.get(u.block);
        if (l != null) {
            for (AbstractRecruitEntity s : l.soldiers) {
                if (s != null && s.isAlive() && u.ids.contains(s.getId())) {
                    soldiers.add(s);
                }
            }
        }
        return soldiers;
    }

    /** How many files a unit stands in. */
    private static int files(Unit u, int soldiers) {
        return u.shape == Shape.LINE ? Math.max(1, Math.min(soldiers, LINE_FILES)) : Math.max(1, (int) Math.ceil(Math.sqrt(soldiers)));
    }

    private static double spacing(Enlisted e) {
        return e.job == Job.CAVALRY ? BattlePlan.SPACING * 1.8 : BattlePlan.SPACING;
    }

    /**
     * Each couple of ticks: units in formation with their player stand side by side behind them, and move as they move
     * (each soldier walked straight to its place, not pathed after the player, so they keep up), turning as the player
     * turns unless the player has fought in the last few seconds. A soldier fighting an enemy at hand is left to it.
     */
    private void driveUnits() {
        long now = level.getGameTime();
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null || !player.isAlive()) {
                continue;
            }
            if (now - e.lastCombat >= COMBAT_TICKS) {
                e.formYaw = player.getYRot();
            }
            double yaw = Math.toRadians(e.formYaw);
            double fx = -Math.sin(yaw);
            double fz = Math.cos(yaw);
            double sp = spacing(e);
            List<Unit> following = new ArrayList<>();
            List<List<AbstractRecruitEntity>> soldiers = new ArrayList<>();
            double total = 0;
            for (Unit u : e.units) {
                List<AbstractRecruitEntity> s = soldiersOf(u);
                if (u.mode == UnitMode.FOLLOW && !s.isEmpty()) {
                    following.add(u);
                    soldiers.add(s);
                    total += (files(u, s.size()) - 1) * sp;
                }
            }
            total += Math.max(0, following.size() - 1) * UNIT_GAP;
            double left = -total / 2;
            for (int k = 0; k < following.size(); k++) {
                Unit u = following.get(k);
                List<AbstractRecruitEntity> s = soldiers.get(k);
                int files = files(u, s.size());
                double width = (files - 1) * sp;
                for (int j = 0; j < s.size(); j++) {
                    double right = left + (j % files) * sp;
                    double back = FOLLOW_BEHIND + (j / files) * sp;
                    double x = player.getX() - fz * right - fx * back;
                    double z = player.getZ() + fx * right - fz * back;
                    double y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
                    drive(u, s.get(j), new Vec3(x, y, z), now);
                }
                left += width + UNIT_GAP;
            }
        }
    }

    /** Walks a soldier of a following unit straight to its place, unless it is fighting, or the player took it over. */
    private void drive(Unit u, AbstractRecruitEntity s, Vec3 place, long now) {
        LivingEntity target = s.getTarget();
        if (target != null && target.isAlive() && target.distanceToSqr(s) < 25) {
            return; // fighting what's at hand
        }
        if (takenOver(u, s)) {
            return;
        }
        s.setHoldPos(place);
        if (s.getFollowState() != 3) {
            s.setFollowState(3);
        }
        s.holdFormation = false;
        u.placed.put(s.getId(), place);
        Entity mover = s.getVehicle() != null ? s.getVehicle() : s;
        double d = Math.hypot(mover.getX() - place.x, mover.getZ() - place.z);
        if (d < 0.4) {
            return;
        }
        double speed = Math.min(1.7, 1.0 + d * 0.12);
        if (s.getVehicle() != null || d > 8) {
            if (now % 10 == 0 || s.getNavigation().isDone()) {
                s.getNavigation().moveTo(place.x, place.y, place.z, speed);
            }
        } else {
            s.getNavigation().stop();
            s.getMoveControl().setWantedPosition(place.x, place.y, place.z, speed);
        }
    }

    /**
     * Whether the player has given this soldier an order of their own in Recruits' command screen (it no longer holds
     * where the battle put it); then its unit is theirs until the war horn orders it again.
     */
    private boolean takenOver(Unit u, AbstractRecruitEntity s) {
        if (u.mode == UnitMode.RECRUITS) {
            return true;
        }
        Vec3 placed = u.placed.get(s.getId());
        Vec3 hold = s.getHoldPos();
        if (placed != null && (s.getFollowState() != 3 || hold == null || hold.distanceToSqr(placed) > 1)) {
            u.mode = UnitMode.RECRUITS;
            return true;
        }
        return false;
    }

    /**
     * Each second, for units not following: a held or sent unit stands in its formation where it was told (placed
     * again when soldiers join it); a charging unit goes after the nearest enemy; every unit's shields are as ordered.
     */
    private void unitOrders(Enlisted e) {
        for (Unit u : e.units) {
            List<AbstractRecruitEntity> soldiers = soldiersOf(u);
            if (soldiers.isEmpty()) {
                continue;
            }
            for (AbstractRecruitEntity s : soldiers) {
                if (s.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) && s.getShouldBlock() != u.shields
                        && u.mode != UnitMode.RECRUITS) {
                    s.setShouldBlock(u.shields);
                }
            }
            switch (u.mode) {
                case HOLD, MOVE -> {
                    boolean newcomer = soldiers.stream().anyMatch(s -> !u.placed.containsKey(s.getId()));
                    if (!u.replace && !newcomer) {
                        for (AbstractRecruitEntity s : soldiers) {
                            if (takenOver(u, s)) {
                                break;
                            }
                        }
                        continue;
                    }
                    u.replace = false;
                    double sp = spacing(e);
                    int files = files(u, soldiers.size());
                    double yaw = Math.toRadians(u.yaw);
                    double fx = -Math.sin(yaw);
                    double fz = Math.cos(yaw);
                    for (int j = 0; j < soldiers.size(); j++) {
                        double right = (j % files - (files - 1) / 2.0) * sp;
                        double back = (j / files) * sp;
                        double x = u.at.x - fz * right - fx * back;
                        double z = u.at.z + fx * right - fz * back;
                        double y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
                        AbstractRecruitEntity s = soldiers.get(j);
                        Vec3 place = new Vec3(x, y, z);
                        s.setHoldPos(place);
                        s.setFollowState(3);
                        s.holdFormation = false;
                        u.placed.put(s.getId(), place);
                    }
                }
                case CHARGE -> {
                    List<AbstractRecruitEntity> foes = enemiesOf(u.block.side(), true);
                    for (AbstractRecruitEntity s : soldiers) {
                        LivingEntity target = s.getTarget();
                        if (target == null || !target.isAlive()) {
                            nearestTo(s, foes).filter(f -> f.distanceTo(s) <= CHARGE_REACH).ifPresent(f -> {
                                s.setTarget(f);
                                s.getNavigation().moveTo(f, 1.2);
                            });
                        }
                    }
                }
                default -> {
                }
            }
        }
    }

    /** War horn orders, given in the horn's screen to some of a player's units; the horn blows if any were. */
    public static void hornOrders(ServerPlayer player, List<int[]> units, List<UnitOrder> orders, List<Vec3> places) {
        RecruitsBattle battle = current;
        if (battle == null) {
            return;
        }
        Enlisted e = battle.enlisted.get(player.getUUID());
        if (e == null || orders.isEmpty()) {
            return;
        }
        for (int k = 0; k < orders.size(); k++) {
            for (int number : units.get(k)) {
                if (number >= 1 && number <= e.units.size()) {
                    battle.order(e, e.units.get(number - 1), orders.get(k), places.get(k), player);
                }
            }
        }
        Holder<SoundEvent> horn = SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(battle.sides[e.side].horn);
        battle.level.playSound(null, player.getX(), player.getEyeY(), player.getZ(), horn.value(), SoundSource.RECORDS, HORN_VOLUME, 1);
    }

    private void order(Enlisted e, Unit u, UnitOrder order, Vec3 place, ServerPlayer player) {
        List<AbstractRecruitEntity> soldiers = soldiersOf(u);
        switch (order) {
            case FOLLOW -> {
                u.mode = UnitMode.FOLLOW;
                u.placed.clear();
            }
            case HOLD -> {
                u.mode = UnitMode.HOLD;
                u.at = centreOf(soldiers, player.position());
                u.yaw = e.formYaw;
                u.replace = true;
                u.placed.clear();
            }
            case MOVE -> {
                u.mode = UnitMode.MOVE;
                Vec3 from = centreOf(soldiers, player.position());
                u.at = place;
                u.yaw = (float) Math.toDegrees(Math.atan2(-(place.x - from.x), place.z - from.z));
                u.replace = true;
                u.placed.clear();
            }
            case CHARGE -> {
                u.mode = UnitMode.CHARGE;
                u.placed.clear();
                for (AbstractRecruitEntity s : soldiers) {
                    s.setFollowState(0);
                    s.holdFormation = false;
                    s.isInFormation = false;
                    s.setAggroState(1);
                }
            }
            case SQUARE, LINE -> {
                u.shape = order == UnitOrder.SQUARE ? Shape.SQUARE : Shape.LINE;
                u.replace = true;
            }
            case SHIELDS_UP, SHIELDS_DOWN -> u.shields = order == UnitOrder.SHIELDS_UP;
        }
        if (u.mode != UnitMode.CHARGE) {
            for (AbstractRecruitEntity s : soldiers) {
                s.setAggroState(0);
            }
        }
    }

    private static Vec3 centreOf(List<AbstractRecruitEntity> soldiers, Vec3 otherwise) {
        if (soldiers.isEmpty()) {
            return otherwise;
        }
        double x = 0;
        double z = 0;
        for (AbstractRecruitEntity s : soldiers) {
            x += s.getX();
            z += s.getZ();
        }
        return new Vec3(x / soldiers.size(), soldiers.get(0).getY(), z / soldiers.size());
    }

    /** What the war horn's screen shows of a player's units: number, size, where, how. */
    public static List<ConquestNetwork.UnitInfo> unitInfo(ServerPlayer player) {
        RecruitsBattle battle = current;
        List<ConquestNetwork.UnitInfo> info = new ArrayList<>();
        Enlisted e = battle == null ? null : battle.enlisted.get(player.getUUID());
        if (e == null) {
            return info;
        }
        for (Unit u : e.units) {
            List<AbstractRecruitEntity> soldiers = battle.soldiersOf(u);
            Vec3 c = centreOf(soldiers, player.position());
            info.add(new ConquestNetwork.UnitInfo(u.number, soldiers.size(), c.x, c.z, u.mode.ordinal(), u.shape.ordinal(), u.shields,
                    battle.mainColorOf(e.side)));
        }
        return info;
    }

    private int mainColorOf(int side) {
        return mainColor(sides[side].faction);
    }

    // ---- blows on figures: soldiers that are only numbers ----

    /** Soldier {@code i} of an enemy block, standing at {@code at} as a figure. */
    private record Figure(FieldBlock block, int index, Point at) {
    }

    /** Enemy blocks near a point (by where they stand), to look for figures among. */
    private List<FieldBlock> enemyBlocksNear(int side, Vec3 at, double reach) {
        List<FieldBlock> near = new ArrayList<>();
        if (faded[1 - side]) {
            return near;
        }
        for (FieldBlock b : field.army(1 - side).blocks()) {
            if (b.alive() > 0 && b.pos().distanceTo(new Point(at.x, at.z)) <= reach + BLOCK_REACH) {
                near.add(b);
            }
        }
        return near;
    }

    /** The nearest enemy figure (not a real soldier) within {@code reach} of a point, among {@code blocks}. */
    @Nullable
    private Figure nearestFigure(List<FieldBlock> blocks, Vec3 at, double reach) {
        Figure best = null;
        double bestDistance = reach;
        for (FieldBlock b : blocks) {
            float[] health = b.health();
            Point front = front(b, health);
            Live l = live.get(b);
            for (int i = 0; i < health.length; i++) {
                if (health[i] <= 0 || l != null && l.soldiers[i] != null) {
                    continue;
                }
                Point p = slot(b, front, i);
                double d = Math.hypot(p.x() - at.x, p.z() - at.z);
                if (d <= bestDistance) {
                    bestDistance = d;
                    best = new Figure(b, i, p);
                }
            }
        }
        return best;
    }

    /**
     * Each second: a player's soldiers with nobody real at hand strike an enemy figure within their reach, as hard as
     * they hit (a cavalry player's riders harder at a gallop, with their momentum).
     */
    private void strikeFigures(Enlisted e, ServerPlayer player) {
        if (!mayFight(e.side)) {
            return;
        }
        List<FieldBlock> near = enemyBlocksNear(e.side, player.position(), FIGURE_SEARCH);
        if (near.isEmpty()) {
            return;
        }
        for (Unit u : e.units) {
            for (AbstractRecruitEntity s : soldiersOf(u)) {
                LivingEntity target = s.getTarget();
                if (target != null && target.isAlive() && target.distanceToSqr(s) < 9) {
                    continue;
                }
                Figure f = nearestFigure(near, s.position(), SOLDIER_REACH);
                if (f == null) {
                    continue;
                }
                float damage = (float) s.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (e.job == Job.CAVALRY && galloping(s)) {
                    damage *= (float) (1 + FieldRules.CHARGE_BONUS * e.unitMomentum);
                    e.unitContact = true;
                }
                field.strikeFigure(f.block(), f.index(), damage);
                s.lookAt(net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.EYES, new Vec3(f.at().x(), s.getEyeY(), f.at().z()));
                s.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            }
        }
    }

    /** Every few ticks: a cavalry player riding at a gallop tramples the enemy figures their horse runs into. */
    private void trample() {
        long now = level.getGameTime();
        for (Enlisted e : enlisted.values()) {
            if (e.job != Job.CAVALRY || !mayFight(e.side)) {
                continue;
            }
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null || player.getVehicle() == null || !galloping(player)) {
                continue;
            }
            List<FieldBlock> near = enemyBlocksNear(e.side, player.position(), TRAMPLE_REACH);
            Figure f = nearestFigure(near, player.getVehicle().position(), TRAMPLE_REACH);
            if (f == null || now - e.lastTrample < TRAMPLE_TICKS) {
                continue;
            }
            e.lastTrample = now;
            field.strikeFigure(f.block(), f.index(), (float) (TRAMPLE_DAMAGE * (1 + FieldRules.CHARGE_BONUS * e.momentum)));
            e.playerContact = true;
            e.lastCombat = now;
        }
    }

    /**
     * A player struck at a figure of formation {@code formation} near ({@code x}, {@code z}) (the client saw the swing
     * meet it): the nearest figure of that block there takes the blow, as hard as the player hits (with their charge,
     * for a cavalry player at a gallop), if it is an enemy within reach.
     */
    public static void playerStrikesFigure(ServerPlayer player, int formation, double x, double z) {
        RecruitsBattle battle = current;
        if (battle == null) {
            return;
        }
        Enlisted e = battle.enlisted.get(player.getUUID());
        int side = e != null ? e.side : -1;
        if (side < 0 || !battle.mayFight(side) || Math.hypot(player.getX() - x, player.getZ() - z) > PLAYER_FIGURE_REACH) {
            return;
        }
        FieldBlock block = null;
        for (FieldBlock b : battle.field.army(1 - side).blocks()) {
            if (b.id() == formation) {
                block = b;
            }
        }
        if (block == null || block.alive() == 0) {
            return;
        }
        Figure f = battle.nearestFigure(List.of(block), new Vec3(x, player.getY(), z), 2.5);
        if (f == null) {
            return;
        }
        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * player.getAttackStrengthScale(0.5F);
        player.resetAttackStrengthTicker();
        if (e.job == Job.CAVALRY && player.getVehicle() != null && galloping(player)) {
            damage *= (float) (1 + FieldRules.CHARGE_BONUS * e.momentum);
            e.playerContact = true;
        }
        e.lastCombat = battle.level.getGameTime();
        battle.field.strikeFigure(f.block(), f.index(), damage);
    }

    /**
     * An enlisted player's ring and beacon. The ring is their place: in their block, or in the evening and at night
     * their tent. The beacon shows the way to where an order sends them: their place while the armies deploy (whenever
     * they are more than {@link #BEACON_NEAR} from it); their tent in the pullback; the blocks they were sent to save, or
     * the block their own is sent against, in the battle. For an order, the beacon shows if the player is more than
     * {@link #BEACON_NEAR} away when it is given, goes once they get there, and shows again if they wander more than
     * {@link #BEACON_FAR} away while the order stands.
     */
    private void markers(Enlisted e, ServerPlayer player) {
        FieldBlock home = e.home;
        Vec3 ring = null;
        Vec3 beaconAt = null;
        Object key = null;
        boolean always = false;
        if ((phase == Phase.PULLBACK || phase == Phase.NIGHT) && e.tentCamp != null && e.tent >= 0) {
            ring = e.tentCamp.tentDoor(e.tent);
            if (phase == Phase.PULLBACK) {
                beaconAt = ring;
                key = TENT_ORDER;
            }
        } else if (home.alive() > 0) {
            Point spot = home.line();
            ring = new Vec3(spot.x(), groundY(home, spot.x(), spot.z()), spot.z());
            if (phase == Phase.DEPLOYMENT) {
                beaconAt = ring;
                always = true;
            } else if (phase == Phase.BATTLE && !e.support.isEmpty()) {
                Point at = middle(e.support);
                beaconAt = new Vec3(at.x(), groundY(e.support.get(0), at.x(), at.z()), at.z());
                key = e.supportKey;
            } else if (phase == Phase.BATTLE && home.strikeAt() != null && home.strikeAt().alive() > 0) {
                FieldBlock target = home.strikeAt();
                beaconAt = new Vec3(target.pos().x(), groundY(target, target.pos().x(), target.pos().z()), target.pos().z());
                key = target;
            }
        }
        boolean show = false;
        if (beaconAt != null) {
            double away = Math.hypot(player.getX() - beaconAt.x, player.getZ() - beaconAt.z);
            if (always) {
                show = away > BEACON_NEAR;
            } else {
                if (key != e.beaconKey) {
                    e.beaconKey = key;
                    e.beaconShown = away > BEACON_NEAR; // a new order: a beacon if they're not there already
                } else if (e.beaconShown && away <= BEACON_NEAR) {
                    e.beaconShown = false; // they got there
                } else if (!e.beaconShown && away > BEACON_FAR) {
                    e.beaconShown = true; // they wandered off
                }
                show = e.beaconShown;
            }
        } else {
            e.beaconKey = null;
        }
        if (ring == null) {
            ConquestNetwork.sendBattleSpot(player, false, 0, 0, 0, show, show ? beaconAt.x : 0, show ? beaconAt.y : 0, show ? beaconAt.z : 0);
        } else {
            ConquestNetwork.sendBattleSpot(player, true, ring.x, ring.y, ring.z, show, show ? beaconAt.x : 0, show ? beaconAt.y : 0,
                    show ? beaconAt.z : 0);
        }
    }

    /** The middle of some blocks, where they stand. */
    private static Point middle(List<FieldBlock> blocks) {
        double x = 0;
        double z = 0;
        for (FieldBlock b : blocks) {
            x += b.pos().x();
            z += b.pos().z();
        }
        return new Point(x / blocks.size(), z / blocks.size());
    }

    /**
     * Orders to save blocks about to break: once a second in the battle, a player with no such order (and none for the
     * last {@link #SUPPORT_REST} seconds) is sent to a block of their army within {@link #SUPPORT_REACH} that is
     * {@link #SUPPORT_RISK} of the way to routing, not one of their own. An enlisted player is told to support that
     * block; an officer is told by the wing's Marshal to save the wing's line there: that block and those beside it
     * about to break too. The order stands until the blocks steady (under {@link #SUPPORT_SAFE}), break, or
     * {@link #SUPPORT_SECONDS} pass.
     */
    private void supportOrders() {
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null) {
                continue;
            }
            if (!e.support.isEmpty()) {
                boolean broke = e.support.stream().allMatch(b -> b.alive() == 0 || b.duty() == Duty.ROUTED || b.duty() == Duty.FLEEING);
                double worst = e.support.stream().mapToDouble(field::routRisk).max().orElse(0);
                if (broke) {
                    player.sendSystemMessage(Component.literal("Too late: they broke.").withStyle(style -> style.withColor(color(e.side))));
                } else if (worst < SUPPORT_SAFE) {
                    player.sendSystemMessage(Component.literal("The line holds. Well done.").withStyle(style -> style.withColor(color(e.side))));
                } else if (clock - e.supportSince <= SUPPORT_SECONDS) {
                    continue;
                }
                e.support.clear();
                e.supportEnded = clock;
                continue;
            }
            if (clock - e.supportEnded < SUPPORT_REST) {
                continue;
            }
            Point at = new Point(player.getX(), player.getZ());
            List<FieldBlock> risky = new ArrayList<>();
            for (FieldBlock b : field.army(e.side).blocks()) {
                if (!e.blocks.contains(b) && b.alive() > 0 && b.pos().distanceTo(at) <= SUPPORT_REACH && field.routRisk(b) >= SUPPORT_RISK) {
                    risky.add(b);
                }
            }
            if (risky.isEmpty()) {
                continue;
            }
            risky.sort(Comparator.comparingDouble(b -> b.pos().distanceTo(at)));
            FieldBlock first = risky.get(0);
            boolean winged = field.army(e.side).winged();
            String where = wingTitle(first.wing(), winged);
            if (e.rank.officer()) {
                for (FieldBlock b : risky) {
                    if (b.wing() == first.wing() && b.pos().distanceTo(first.pos()) <= SAVE_REACH) {
                        e.support.add(b);
                    }
                }
                String marshal = sides[e.side].marshals[Math.max(0, Math.min(2, first.wing()))];
                title(e, "Save the " + where + "!");
                player.sendSystemMessage(Component.literal("Marshal " + marshal + ": ").withStyle(style -> style.withColor(color(e.side)))
                        .append(Component.literal(String.format(Locale.ROOT, "%s, %d of our blocks on the %s are about to break. "
                                + "Bring your men and hold them!", e.rank.title(), e.support.size(), where)).withStyle(net.minecraft.ChatFormatting.WHITE)));
            } else {
                e.support.add(first);
                title(e, "Support the block on the " + where + "!");
                player.sendSystemMessage(Component.literal("A block on the " + where + " is about to break. Go to them!")
                        .withStyle(style -> style.withColor(color(e.side))));
            }
            e.supportKey = new Object();
            e.supportSince = clock;
        }
    }

    /** The battle is over or cleared: enlisted players lose their ring. */
    private void dismissEnlisted() {
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player != null) {
                ConquestNetwork.sendBattleSpot(player, false, 0, 0, 0);
            }
            unkitPlayer(player, e);
            removeMessenger(e);
            if (RecruitEvents.recruitsGroupsManager != null) {
                for (Unit u : e.units) {
                    if (u.group != null) {
                        RecruitEvents.recruitsGroupsManager.removeGroup(u.group);
                    }
                }
                if (player != null) {
                    RecruitEvents.recruitsGroupsManager.broadCastGroupsToPlayer(player);
                }
            }
        }
        enlisted.clear();
        commanded.clear();
    }

    /** A real block's soldiers report to the numbers: where the block stands and each soldier's health. */
    private void readBack(Live l) {
        FieldBlock block = l.block;
        float[] health = new float[l.soldiers.length];
        float[] known = block.health();
        // Where the front stands, as each real soldier has it from its place in the ranks; the block's position is
        // the middle of all its soldiers, real or not (just their middle, when all are real).
        double x = 0;
        double z = 0;
        int n = 0;
        boolean lost = false;
        for (int i = 0; i < l.soldiers.length; i++) {
            AbstractRecruitEntity s = l.soldiers[i];
            if (s == null || s.isRemoved() && s.isAlive()) {
                // Never spawned, or gone without dying (unloaded): keep what the numbers last knew.
                health[i] = known[i];
                lost |= s != null;
                continue;
            }
            if (s.isAlive()) {
                health[i] = s.getHealth();
                Entity feet = s.getVehicle() != null ? s.getVehicle() : s;
                Point front = BattlePlan.place(new Point(feet.getX(), feet.getZ()), block.facingX(), block.facingZ(),
                        -slotRight(block, i), -slotBack(block, i));
                x += front.x();
                z += front.z();
                n++;
            }
        }
        Point pos = block.pos();
        if (n > 0) {
            double right = 0;
            double back = 0;
            int all = 0;
            for (int i = 0; i < health.length; i++) {
                if (health[i] > 0) {
                    right += slotRight(block, i);
                    back += slotBack(block, i);
                    all++;
                }
            }
            pos = BattlePlan.place(new Point(x / n, z / n), block.facingX(), block.facingZ(), right / Math.max(1, all), back / Math.max(1, all));
        }
        field.sync(block, pos, health);
        block.setReal(n);
        if (lost) {
            dematerialise(l);
        }
    }

    /**
     * The arrows archers loosed as numbers this second, for clients to draw in flight. A block part real shoots real
     * arrows from its real archers; only its figures' share is drawn.
     */
    private void sendVolleys() {
        List<FieldBattle.Volley> volleys = field.volleys();
        if (volleys.isEmpty() || level.players().isEmpty()) {
            return;
        }
        int[] from = new int[volleys.size()];
        int[] to = new int[volleys.size()];
        int[] shots = new int[volleys.size()];
        int n = 0;
        for (FieldBattle.Volley v : volleys) {
            FieldBlock archers = v.archers();
            int figures = archers.alive() - (archers.live() ? archers.real() : 0);
            int drawn = archers.alive() == 0 ? 0 : (int) Math.round(v.shots() * (double) figures / archers.alive());
            if (drawn > 0) {
                from[n] = archers.id();
                to[n] = v.target().id();
                shots[n] = drawn;
                n++;
            }
        }
        if (n > 0) {
            ConquestNetwork.sendVolleys(level.players(), new ConquestNetwork.Volleys(java.util.Arrays.copyOf(from, n),
                    java.util.Arrays.copyOf(to, n), java.util.Arrays.copyOf(shots, n)));
        }
    }

    /**
     * The general sounds the horn for a big decision (the advance, a cavalry charge or recall, a wall, the general
     * charging, the final charge): the faction's own goat horn, heard far across the field. At most one every
     * {@link #HORN_SECONDS}; a fallen general sounds none.
     */
    private void soundHorn(int side) {
        FieldBlock commander = field.army(side).commander();
        if (commander == null || commander.alive() == 0 || field.seconds() - lastHorn[side] < HORN_SECONDS) {
            return;
        }
        lastHorn[side] = field.seconds();
        Live real = live.get(commander);
        List<AbstractRecruitEntity> soldiers = real == null ? List.of() : real.living();
        double x = soldiers.isEmpty() ? commander.pos().x() : soldiers.get(0).getX();
        double z = soldiers.isEmpty() ? commander.pos().z() : soldiers.get(0).getZ();
        double y = soldiers.isEmpty() ? groundY(commander, x, z) + 2 : soldiers.get(0).getEyeY();
        Holder<SoundEvent> horn = SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(sides[side].horn);
        level.playSound(null, x, y, z, horn.value(), SoundSource.RECORDS, HORN_VOLUME, 1);
    }

    /**
     * A battle soldier is hurt by another: a charging rider hits harder with its block's momentum
     * ({@link FieldRules#CHARGE_BONUS}), and a braced wall takes less from cavalry striking its front and gives it more
     * back ({@link FieldRules#BRACED_TAKES}, {@link FieldRules#BRACED_DEALS}). Soldiers near their general leading an
     * Unrelenting Charge take less ({@link FieldRules#RESOLVE_TAKES}). A horse is hurt for its rider's block.
     */
    public static float adjustDamage(LivingEntity victim, @Nullable Entity attacker, float amount) {
        RecruitsBattle battle = current;
        if (battle == null || attacker == null) {
            return amount;
        }
        Entity rider = victim.getPassengers().isEmpty() ? victim : victim.getPassengers().get(0);
        if (!battle.enlisted.isEmpty()) {
            amount = battle.unitRules(rider, attacker, amount);
        }
        Live by = battle.byEntity.get(attacker.getId());
        Live of = battle.byEntity.get(rider.getId());
        if (by != null && of != null && by.block.side() != of.block.side()) {
            battle.lastRealHurt[of.block.side()] = battle.field.seconds();
        }
        if (by == null || of == null || by.block.side() == of.block.side()) {
            return amount;
        }
        boolean byCavalry = by.block.arm() == Arm.CAVALRY;
        if (byCavalry && by.block.charged() && galloping(attacker)) {
            amount *= 1 + FieldRules.CHARGE_BONUS * by.block.momentum();
        }
        if (byCavalry && of.block.braced() && FieldRules.side(of.block.facingX(), of.block.facingZ(),
                attacker.getX() - victim.getX(), attacker.getZ() - victim.getZ()) == FieldRules.Side.FRONT) {
            amount *= FieldRules.BRACED_TAKES;
        }
        if (by.block.braced() && of.block.arm() == Arm.CAVALRY && FieldRules.side(by.block.facingX(), by.block.facingZ(),
                victim.getX() - attacker.getX(), victim.getZ() - attacker.getZ()) == FieldRules.Side.FRONT) {
            amount *= FieldRules.BRACED_DEALS;
        }
        if (battle.field.resolute(of.block)) {
            amount *= FieldRules.RESOLVE_TAKES;
        }
        return amount;
    }

    /** Tells everyone when a general charges, or the lines stall. */
    private void announce() {
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            FieldBlock commander = army.commander();
            if (!chargeAnnounced[side] && commander != null && commander.charged()) {
                chargeAnnounced[side] = true;
                broadcast(sides[side].faction.name() + "'s general charges");
            }
        }
        if (!meleeAnnounced && field.finalMelee()) {
            meleeAnnounced = true;
            broadcast("[battle " + field.seconds() + "s] the lines have stalled: everyone left charges");
        }
    }

    private void report() {
        int liveSoldiers = 0;
        int frontRows = 0;
        for (Live l : live.values()) {
            liveSoldiers += l.living().size();
            frontRows += l.frontRow ? 1 : 0;
        }
        broadcast(String.format(Locale.ROOT,
                "[battle %ds] %s %d alive (%d reliefs), %s %d alive (%d reliefs) | %d real soldiers in %d blocks, %d fighting front to front | %s, of which battle %.1f ms/tick (numbers %.1f, switching %.1f, snapshots %.1f)",
                field.seconds(), sides[0].faction.name(), field.army(0).alive(), field.army(0).reliefs(), sides[1].faction.name(),
                field.army(1).alive(), field.army(1).reliefs(), liveSoldiers, live.size(), frontRows, window.describe(),
                perTick(ownNanos), perTick(fieldNanos), perTick(switchNanos), perTick(snapshotNanos)));
        window.clear();
        ownNanos = 0;
        fieldNanos = 0;
        switchNanos = 0;
        snapshotNanos = 0;
    }

    /** Nanoseconds spent since the last report, as milliseconds a tick. */
    private double perTick(long nanos) {
        return window.count == 0 ? 0 : nanos / 1e6 / window.count;
    }

    private void end() {
        over = true;
        FieldBattle.Outcome outcome = field.outcome();
        for (Live l : live.values()) {
            l.living().forEach(s -> s.setTarget(null));
        }
        String result;
        if (outcome.winner() < 0) {
            result = outcome.result();
        } else if (!field.army(1 - outcome.winner()).generalAlive()) {
            Side loser = sides[1 - outcome.winner()];
            result = describe(outcome.winner()) + " wins: " + loser.faction.name() + "'s General " + loser.general.name()
                    + " fell, and " + loser.faction.name() + " retreats";
        } else {
            result = describe(outcome.winner()) + " wins; " + describe(1 - outcome.winner()) + " routs";
        }
        broadcast(String.format(Locale.ROOT, "[" + name + " is over after %ds] %s. %s %d/%d left, %s %d/%d left | whole battle: %s",
                outcome.seconds(), result, sides[0].faction.name(), field.army(0).alive(), field.army(0).started(),
                sides[1].faction.name(), field.army(1).alive(), field.army(1).started(), whole.describe()));
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            broadcast(String.format(Locale.ROOT, "  %s: general %s, standard %s", describe(side),
                    army.generalAlive() ? "survived" : "fell", army.standardAlive() ? "still flies" : "fell"));
        }
        reportCalibration();
        victory(outcome);
    }

    // ---- the phases of a battle: deployment, battle, evening pullback, night, then victory ----

    /**
     * Where a battle is in its day: deployment (the armies form up before their camps, and the war council meets),
     * the battle, the evening pullback to the camps, the night in camp, and once it is won the pursuit or the aftermath
     * (the fighting over, the beaten army about to leave the field), then done.
     */
    public enum Phase {
        DEPLOYMENT, BATTLE, PULLBACK, NIGHT, PURSUIT, AFTERMATH, DONE
    }

    public Phase phase() {
        return phase;
    }

    public int day() {
        return day;
    }

    private void setPhase(Phase next) {
        phase = next;
        phaseSince = clock;
    }

    /** The time of day, 0 (sunrise) to 23999. */
    private long dayTime() {
        return level.getDayTime() % 24000;
    }

    /** Evening: sunset has begun. */
    private boolean evening() {
        long t = dayTime();
        return t >= EVENING && t < DAWN;
    }

    private boolean nightfall() {
        long t = dayTime();
        return t >= NIGHTFALL && t < DAWN;
    }

    private boolean morning() {
        return !evening();
    }

    /** Whether a side's soldiers may fight now: anyone in battle, and the winner in a pursuit. */
    private boolean mayFight(int side) {
        return phase == Phase.BATTLE || phase == Phase.PURSUIT && side == winner;
    }

    /**
     * Each army's camp behind its side of the field: its command tent {@link #CAMP_GAP} behind the rearmost of its
     * blocks, with tents for its size behind that, in the colour nearest its uniform.
     */
    /** Where a wing's camp stands (the army's, if it has no wings): behind its rearmost block, across from its middle. */
    @Nullable
    private static Point campAt(FieldArmy army, int wing) {
        double rear = 0;
        double right = 0;
        int n = 0;
        for (FieldBlock b : army.blocks()) {
            if (army.winged() && b.wing() != wing) {
                continue;
            }
            Point at = BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), b.line());
            rear = Math.max(rear, at.z());
            right += at.x();
            n++;
        }
        return n == 0 ? null : BattlePlan.place(army.origin(), army.facingX(), army.facingZ(), right / n, rear + CAMP_GAP);
    }

    /**
     * The battle's parts for choosing its site ({@link BattleSite}): each army's wings (or the whole army, if it has
     * none), each with its blocks' places and its camp.
     */
    private static List<BattleSite.Part> siteParts(FieldBattle field) {
        List<BattleSite.Part> parts = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            int[] wings = army.winged() ? new int[]{FieldArmy.CENTRE, FieldArmy.LEFT, FieldArmy.RIGHT} : new int[]{FieldArmy.CENTRE};
            for (int wing : wings) {
                List<Point> places = new ArrayList<>();
                for (FieldBlock b : army.blocks()) {
                    if (!army.winged() || b.wing() == wing) {
                        places.add(b.line());
                    }
                }
                Point camp = campAt(army, wing);
                if (camp != null) {
                    places.add(camp);
                    parts.add(new BattleSite.Part(side, places));
                }
            }
        }
        return parts;
    }

    /** The world map's water (sea and river), in its pixels, for choosing a battle's site; off the map counts as water. */
    private static BattleSite.Water mapWater(BaseMap map) {
        int bpp = map.blocksPerPixel();
        return new BattleSite.Water() {
            @Override
            public int cell() {
                return bpp;
            }

            @Override
            public boolean wet(int cellX, int cellZ) {
                int i = map.pixelAt(cellX * bpp + bpp / 2.0, cellZ * bpp + bpp / 2.0);
                return i < 0 || map.wet()[i];
            }
        };
    }

    private void buildCamps() {
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            int[] wings = army.winged() ? new int[]{FieldArmy.CENTRE, FieldArmy.LEFT, FieldArmy.RIGHT} : new int[]{FieldArmy.CENTRE};
            for (int wing : wings) {
                Point at = campAt(army, wing);
                if (at == null) {
                    continue;
                }
                int soldiers = 0;
                for (FieldBlock b : army.blocks()) {
                    if (!army.winged() || b.wing() == wing) {
                        soldiers += b.started();
                    }
                }
                int tents = Math.max(MIN_TENTS, Math.min(MAX_TENTS, soldiers / SOLDIERS_PER_TENT));
                loadGround(at);
                WarCamp camp = WarCamp.build(level, at, army.facingX(), army.facingZ(), tents, nearestDye(sides[side].uniform),
                        sides[side].banner, wing == FieldArmy.CENTRE);
                if (wing == FieldArmy.CENTRE) {
                    camps[side] = camp;
                } else {
                    wingCamps[side][wing] = camp;
                }
                spawnGuards(side, camp);
            }
        }
    }

    /** The camp a player of a wing sleeps in: their wing's, else the general's. */
    @Nullable
    private WarCamp campOf(int side, int wing) {
        WarCamp camp = wing >= 0 && wing < 3 ? wingCamps[side][wing] : null;
        return camp != null ? camp : camps[side];
    }

    /** The dye colour nearest an RGB colour, for a camp's wool. */
    private static DyeColor nearestDye(int rgb) {
        DyeColor best = DyeColor.WHITE;
        double bestDistance = Double.MAX_VALUE;
        for (DyeColor d : DyeColor.values()) {
            int c = d.getFireworkColor();
            double dr = (c >> 16 & 255) - (rgb >> 16 & 255);
            double dg = (c >> 8 & 255) - (rgb >> 8 & 255);
            double db = (c & 255) - (rgb & 255);
            double distance = dr * dr + dg * dg + db * db;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = d;
            }
        }
        return best;
    }

    /** Ten heavy infantry stand guard at the command tent's entrance, facing out, as long as the camp stands. */
    private void spawnGuards(int side, WarCamp camp) {
        for (Vec3 at : camp.guardSpots()) {
            AbstractRecruitEntity guard = campFigure(ModEntityTypes.RECRUIT_SHIELDMAN.get(), side, Role.SOLDIER, at, camp.yaw(0),
                    null);
            if (guard != null) {
                equip(guard, EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                equip(guard, EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                equip(guard, EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                equip(guard, EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                equip(guard, EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                equip(guard, EquipmentSlot.OFFHAND, bannerShield(sides[side]));
                campFolk.computeIfAbsent(side, s -> new ArrayList<>()).add(guard.getUUID());
            }
        }
    }

    /**
     * The war council stands in each command tent while the armies deploy: the general behind the command table,
     * the Marshals of the left and right wings either side of it, facing it.
     */
    private void spawnCouncil() {
        dismissCouncil();
        for (int side = 0; side < 2; side++) {
            WarCamp camp = camps[side];
            if (camp == null) {
                continue;
            }
            Side s = sides[side];
            AbstractRecruitEntity general = campFigure(ModEntityTypes.RECRUIT.get(), side, Role.GENERAL, camp.generalSpot(),
                    camp.yaw(0), "General " + s.general.name());
            AbstractRecruitEntity left = campFigure(ModEntityTypes.RECRUIT.get(), side, Role.GUARD, camp.leftMarshalSpot(),
                    camp.yaw(1), "Marshal " + s.marshals[0]);
            AbstractRecruitEntity right = campFigure(ModEntityTypes.RECRUIT.get(), side, Role.GUARD, camp.rightMarshalSpot(),
                    camp.yaw(3), "Marshal " + s.marshals[2]);
            for (AbstractRecruitEntity member : new AbstractRecruitEntity[]{general, left, right}) {
                if (member != null) {
                    councillors.put(member.getUUID(), side);
                }
            }
        }
    }

    private void dismissCouncil() {
        for (UUID id : councillors.keySet()) {
            Entity e = level.getEntity(id);
            if (e != null) {
                e.discard();
            }
        }
        councillors.clear();
        council.adjourn();
    }

    /**
     * Someone of the camp, standing still where put: not a battle soldier, never hurt, kept out of the numbers.
     * {@code name}, if given, shows above them.
     */
    @Nullable
    private AbstractRecruitEntity campFigure(EntityType<? extends AbstractRecruitEntity> type, int side, Role role, Vec3 at, float yaw,
                                            @Nullable String name) {
        AbstractRecruitEntity figure = spawn(type, sides[side], role, new Point(at.x, at.z), yaw);
        if (figure == null) {
            return null;
        }
        figure.teleportTo(at.x, at.y, at.z);
        figure.setYRot(yaw);
        figure.setYBodyRot(yaw);
        figure.setYHeadRot(yaw);
        figure.setNoAi(true);
        figure.setInvulnerable(true);
        figure.setAggroState(3);
        if (name != null) {
            figure.setCustomName(Component.literal(name).withStyle(style -> style.withColor(color(side))));
            figure.setCustomNameVisible(true);
        }
        return figure;
    }

    /** What the war council of {@code side} knows: its officers, its plan, and the enemy and their weaknesses. */
    private WarCouncil.Facts councilFacts(int side) {
        Side s = sides[side];
        Side e = sides[1 - side];
        FieldArmy enemy = field.army(1 - side);
        return new WarCouncil.Facts(side, color(side),
                new WarCouncil.Officer("General", s.general.name(), s.traits[0]),
                new WarCouncil.Officer("Marshal", s.marshals[0], s.traits[1]),
                new WarCouncil.Officer("Marshal", s.marshals[2], s.traits[3]),
                s.general.plan(), field.army(side).alive(), e.faction.name(), e.general.name(), e.general.plan(), enemy.alive(),
                weaknesses(enemy, e.general.plan()), day);
    }

    /** What the scouts make of the enemy: a thin wing, few horse or archers, no shield wall. */
    private static List<String> weaknesses(FieldArmy enemy, rafasconquest.battle.WarPlan plan) {
        int all = 0;
        int cavalry = 0;
        int archers = 0;
        int[] wing = new int[3];
        for (FieldBlock b : enemy.blocks()) {
            if (b.alive() == 0 || b.duty() == Duty.GENERAL || b.duty() == Duty.GUARD || b.duty() == Duty.BEARER) {
                continue;
            }
            all += b.alive();
            if (b.arm() == Arm.CAVALRY) {
                cavalry += b.alive();
            } else if (b.arm() == Arm.ARCHERS) {
                archers += b.alive();
            } else {
                wing[b.wing()] += b.alive();
            }
        }
        List<String> lines = new ArrayList<>();
        int infantry = wing[0] + wing[1] + wing[2];
        if (enemy.winged() && infantry > 0) {
            int thin = wing[FieldArmy.LEFT] <= wing[FieldArmy.RIGHT] ? FieldArmy.LEFT : FieldArmy.RIGHT;
            if (wing[thin] < infantry * 0.28) {
                String name = thin == FieldArmy.LEFT ? "left" : "right";
                lines.add(String.format(Locale.ROOT, "their %s wing is thin: barely %d foot. Hit it hard and it folds.", name, wing[thin]));
            }
        }
        if (all > 0 && cavalry < all * 0.1) {
            lines.add("they have few horse. Our cavalry can ride where it likes.");
        }
        if (all > 0 && archers < all * 0.1) {
            lines.add("they have few archers. Close in without fear of the arrows.");
        }
        if (!plan.has(rafasconquest.battle.Doctrine.Formation.GREAT_SHIELD)) {
            lines.add("they stand no shield wall. A good charge will break their line.");
        }
        List<String> capitalised = new ArrayList<>();
        for (String l : lines) {
            capitalised.add(Character.toUpperCase(l.charAt(0)) + l.substring(1));
        }
        return capitalised;
    }

    /**
     * For testing, ends the day's battle at once: {@code general}: that side's general falls (its army retreats);
     * {@code surrender}: that side surrenders.
     */
    public static String debugEnd(String how, int side) {
        RecruitsBattle battle = current;
        if (battle == null || battle.phase != Phase.BATTLE) {
            return "No battle being fought";
        }
        if (how.equals("general")) {
            battle.field.killGeneral(side);
            return battle.sides[side].faction.name() + "'s general falls";
        }
        battle.field.concede(side);
        return battle.sides[side].faction.name() + " gives up";
    }

    /** A player right-clicked one of the battle's people: a council member hears them out. */
    public static void onInteract(ServerPlayer player, Entity target) {
        RecruitsBattle battle = current;
        if (battle == null || battle.phase != Phase.DEPLOYMENT) {
            return;
        }
        Enlisted mine = battle.enlisted.get(player.getUUID());
        if (mine != null && target.getUUID().equals(mine.messenger) && !mine.briefed) {
            battle.hearMessenger(mine, player);
            return;
        }
        Integer side = battle.councillors.get(target.getUUID());
        if (side == null) {
            return;
        }
        boolean sameArmy = player.getTeam() != null && player.getTeam().getName().equals(battle.sides[side].team.getName());
        battle.council.approach(player, battle.councilFacts(side), sameArmy, battle.level.getGameTime());
    }

    /** A player chose something at the war council ({@code /council <topic>}). */
    public static String councilTopic(ServerPlayer player, String topic) {
        RecruitsBattle battle = current;
        if (battle == null || battle.phase != Phase.DEPLOYMENT) {
            return "The war council meets only while the armies deploy";
        }
        for (int side = 0; side < 2; side++) {
            if (player.getTeam() != null && player.getTeam().getName().equals(battle.sides[side].team.getName())) {
                battle.council.topic(player, battle.councilFacts(side), topic, battle.level.getGameTime());
                return "";
            }
        }
        return "You're in neither army";
    }

    /**
     * Deployment: on the first day the armies stand in formation before their camps; on later mornings they march out
     * of camp to their places, and enlisted players lead their units out to theirs. When the countdown runs out
     * ({@link #countDown}) they charge.
     */
    private void deployment() {
        if (day == 1) {
            prepare();
            updateEnlisted();
            countDown();
            return;
        }
        for (Live l : new ArrayList<>(live.values())) {
            readBack(l);
        }
        if (field.forming()) {
            field.second();
        }
        for (Live l : new ArrayList<>(live.values())) {
            passOnOrders(l);
        }
        updateEnlisted();
        switchLive();
        countDown();
    }

    /**
     * The deployment's countdown, at the top of every player's screen in their nation's colour (a neutral one for
     * players in neither army): {@link #FIRST_DEPLOYMENT} on the first day, {@link #MORNING_DEPLOYMENT} on later
     * mornings. At 0 the battle begins.
     */
    private void countDown() {
        int total = day == 1 ? FIRST_DEPLOYMENT : MORNING_DEPLOYMENT;
        int left = Math.max(0, total - (clock - phaseSince));
        String time = String.format(Locale.ROOT, "%d:%02d", left / 60, left % 60);
        for (int side = 0; side < 3; side++) {
            ServerBossEvent bar = deploymentBars[side];
            if (bar == null) {
                bar = side < 2
                        ? new ServerBossEvent(Component.empty(), barColour(mainColor(sides[side].faction)), BossEvent.BossBarOverlay.PROGRESS)
                        : new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
                deploymentBars[side] = bar;
            }
            String text = name + ", day " + day + ": " + (side < 2 ? sides[side].faction.name() + " deploys" : "the armies deploy") + ", " + time;
            int s = side;
            bar.setName(side < 2 ? Component.literal(text).withStyle(style -> style.withColor(color(s))) : Component.literal(text));
            bar.setProgress((float) left / total);
        }
        for (ServerPlayer player : level.players()) {
            int side = 2;
            for (int i = 0; i < 2; i++) {
                if (player.getTeam() != null && player.getTeam().getName().equals(sides[i].team.getName())) {
                    side = i;
                }
            }
            for (int i = 0; i < 3; i++) {
                if (i == side) {
                    deploymentBars[i].addPlayer(player);
                } else {
                    deploymentBars[i].removePlayer(player);
                }
            }
        }
        if (left == 0) {
            startFighting();
        }
    }

    private void hideDeploymentBars() {
        for (ServerBossEvent bar : deploymentBars) {
            if (bar != null) {
                bar.removeAllPlayers();
            }
        }
    }

    /** The boss bar colour nearest a nation's colour. */
    private static BossEvent.BossBarColor barColour(int rgb) {
        int[][] colours = {{0xEC00B8}, {0x00B7EC}, {0xEC0000}, {0x00EC00}, {0xECEC00}, {0x7B00EC}, {0xECECEC}};
        BossEvent.BossBarColor[] bars = {BossEvent.BossBarColor.PINK, BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.RED,
                BossEvent.BossBarColor.GREEN, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarColor.WHITE};
        BossEvent.BossBarColor best = BossEvent.BossBarColor.WHITE;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < bars.length; i++) {
            int c = colours[i][0];
            double dr = (c >> 16 & 255) - (rgb >> 16 & 255);
            double dg = (c >> 8 & 255) - (rgb >> 8 & 255);
            double db = (c & 255) - (rgb & 255);
            double distance = dr * dr + dg * dg + db * db;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = bars[i];
            }
        }
        return best;
    }

    /** A task the general gives a player for the day, and why. */
    private record GeneralTask(BattleRole role, String reason) {
    }

    /**
     * Whether the general gives a player another task than their own this morning, from how the battle stands and the
     * general's trait: hunt the enemy general who leads their own charge, support when outnumbered, flank a thin wing,
     * bait for a cunning general, lead the charge for a bold one with the numbers. Not always, even with a reason.
     */
    @Nullable
    private GeneralTask generalTask(int side, BattleRole own) {
        Side s = sides[side];
        Side enemy = sides[1 - side];
        FieldArmy ours = field.army(side);
        FieldArmy theirs = field.army(1 - side);
        List<GeneralTask> reasons = new ArrayList<>();
        if (enemy.general.plan().has(Strategy.UNRELENTING_CHARGE) && s.traits[0] != Trait.CAUTIOUS) {
            reasons.add(new GeneralTask(BattleRole.KILL_SQUAD, "General " + enemy.general.name()
                    + " rides at the head of their charge. Bring them down, and their army breaks."));
        }
        if (ours.alive() < theirs.alive() * 0.85) {
            reasons.add(new GeneralTask(BattleRole.SUPPORT, "We're outnumbered now. Hold your men back of the line "
                    + "and go wherever it bends."));
        }
        for (String w : weaknesses(theirs, enemy.general.plan())) {
            if (w.contains("wing is thin")) {
                reasons.add(new GeneralTask(BattleRole.FLANKER, w + " Take your men round and strike it in the side."));
            }
        }
        if (s.traits[0] == Trait.CUNNING) {
            reasons.add(new GeneralTask(BattleRole.BAIT, "Run ahead of the line, then give ground once they bite. "
                    + "Draw them in, and we'll close on them."));
        }
        if (s.traits[0] == Trait.BOLD && ours.alive() >= theirs.alive()) {
            reasons.add(new GeneralTask(BattleRole.FRONTLINE, "You'll lead the first charge today. Make it count."));
        }
        reasons.removeIf(r -> r.role() == own);
        if (reasons.isEmpty() || level.random.nextDouble() >= NEW_TASK_CHANCE) {
            return null;
        }
        return reasons.get(level.random.nextInt(reasons.size()));
    }

    /**
     * The morning: each enlisted player wakes in camp, enlisted anew with their unit, and heads out to their place,
     * their soldiers following; unless the general has given them another task, and then a messenger waits at their
     * tent to tell them.
     */
    private void morningMuster() {
        dayRoles.clear();
        Ranks ranks = Ranks.get(level.getServer());
        for (ServerPlayer player : level.players()) {
            for (int side = 0; side < 2; side++) {
                if (player.getTeam() != null && player.getTeam().getName().equals(sides[side].team.getName())) {
                    GeneralTask task = generalTask(side, ranks.role(player.getUUID()));
                    if (task != null) {
                        dayRoles.put(player.getUUID(), task);
                    }
                }
            }
        }
        enlist();
        for (Enlisted e : enlisted.values()) {
            e.marching = true;
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null) {
                continue;
            }
            if (dayRoles.containsKey(e.player)) {
                spawnMessenger(e, player);
                title(e, "A messenger waits at your tent");
            } else {
                title(e, "Form up at your position!");
                player.sendSystemMessage(Component.literal("Dawn. Lead your men out to your ring.")
                        .withStyle(style -> style.withColor(color(e.side))));
            }
        }
    }

    /** A messenger stands at the player's tent (or beside them, if they have none), facing in. */
    private void spawnMessenger(Enlisted e, ServerPlayer player) {
        WarCamp camp = e.tentCamp;
        Vec3 at = camp != null && e.tent >= 0 ? camp.tentDoor(e.tent) : player.position().add(1.5, 0, 0);
        float yaw = camp != null ? camp.intoTent() : player.getYRot() + 180;
        AbstractRecruitEntity messenger = campFigure(ModEntityTypes.RECRUIT.get(), e.side, Role.SOLDIER, at, yaw, "Messenger");
        if (messenger != null) {
            e.messenger = messenger.getUUID();
        }
    }

    /** The messenger gives a player the general's orders for the day, then leaves. */
    private void hearMessenger(Enlisted e, ServerPlayer player) {
        GeneralTask task = dayRoles.get(e.player);
        e.briefed = true;
        e.messengerLeaves = clock + 12;
        if (task == null) {
            return;
        }
        List<Component> lines = new ArrayList<>();
        lines.add(messengerSays(e, e.rank.title() + "! Word from General " + sides[e.side].general.name() + "."));
        lines.add(messengerSays(e, "The general orders your men to fight " + task.role().title() + " today."));
        lines.add(messengerSays(e, task.reason()));
        lines.add(messengerSays(e, "Your ring marks where your men gather."));
        council.deliver(player, lines, level.getGameTime());
    }

    private Component messengerSays(Enlisted e, String text) {
        return Component.literal("Messenger: ").withStyle(style -> style.withColor(color(e.side)))
                .append(Component.literal(text).withStyle(net.minecraft.ChatFormatting.WHITE));
    }

    private void removeMessenger(Enlisted e) {
        if (e.messenger != null) {
            Entity m = level.getEntity(e.messenger);
            if (m != null) {
                m.discard();
            }
            e.messenger = null;
        }
    }

    /**
     * Each second for an enlisted player out of the fight: their ring points to their tent in the evening and at
     * night; in the morning, their soldiers follow them out of camp until they reach their place, and a messenger, if
     * any, gives them the general's orders when they come near.
     */
    private void campLife(Enlisted e, ServerPlayer player) {
        if (e.messenger != null) {
            Entity m = level.getEntity(e.messenger);
            if (!e.briefed && m != null && player.distanceTo(m) <= MESSENGER_REACH) {
                hearMessenger(e, player);
            } else if (e.briefed && clock >= e.messengerLeaves) {
                if (m != null) {
                    level.sendParticles(net.minecraft.core.particles.ParticleTypes.POOF, m.getX(), m.getY() + 1, m.getZ(), 6, 0.3, 0.5, 0.3, 0.02);
                }
                removeMessenger(e);
            }
        }
        if (e.marching) {
            Point spot = e.home.line();
            if (Math.hypot(player.getX() - spot.x(), player.getZ() - spot.z()) <= IN_POSITION) {
                e.marching = false;
                title(e, "In position");
            }
        }
    }

    /** Evening: the fighting stops, and both armies march back to their camps before nightfall. */
    private void beginPullback() {
        setPhase(Phase.PULLBACK);
        // Each block marches back by the same distance, so the army re-forms just short of its camp.
        double back = CAMP_GAP - PULLBACK_SHORT_OF_CAMP;
        field.pullBack(new double[]{back, back});
        for (Live l : live.values()) {
            l.living().forEach(s -> s.setTarget(null));
        }
        broadcast(String.format(Locale.ROOT, "[battle day %d] Evening: both armies pull back to their camps", day));
        announce(0, "Pull back to camp!");
        announce(1, "Pull back to camp!");
    }

    /** The pullback and the night: the blocks march to their camps, then rest there until dawn. */
    private void night() {
        for (Live l : new ArrayList<>(live.values())) {
            readBack(l);
        }
        field.second();
        for (Live l : new ArrayList<>(live.values())) {
            passOnOrders(l);
        }
        updateEnlisted();
        switchLive();
        if (phase == Phase.PULLBACK && (field.arrived() || nightfall())) {
            setPhase(Phase.NIGHT);
            for (Enlisted e : enlisted.values()) {
                title(e, "Night falls. Rest in your tent.");
            }
            broadcast(String.format(Locale.ROOT, "[battle day %d] Night falls; the armies rest in their camps", day));
        }
        if (phase == Phase.NIGHT && morning()) {
            regroup();
        }
    }

    /**
     * Dawn: each army's survivors regroup into fresh blocks (the wounded recovered overnight), and both deploy anew from
     * what is left, under the same generals and plans. The war council meets again.
     */
    private void regroup() {
        int[] survivors = new int[2];
        for (int side = 0; side < 2; side++) {
            for (FieldBlock b : field.army(side).blocks()) {
                // The general, their guard and the standard come back as they are; the rest are regrouped.
                Duty home = b.homeDuty();
                if (home != Duty.GENERAL && home != Duty.GUARD && home != Duty.BEARER) {
                    survivors[side] += b.alive();
                }
            }
        }
        for (Live l : live.values()) {
            discard(l);
        }
        live.clear();
        lastHealth.clear();
        lastCentre.clear();
        everReal.clear();
        dismissEnlisted();
        field = new FieldBattle(BattlePlan.blocks(survivors[0]), BattlePlan.blocks(survivors[1]), centre, axisX, axisZ,
                level.random.nextLong(), sides[0].general.plan(), sides[1].general.plan());
        if (terrain != null) {
            GroundPlan.deploy(field, terrain);
        }
        started = false;
        day++;
        setPhase(Phase.DEPLOYMENT);
        morningMuster();
        // The armies rested in camp: they start the day there and march out to their places.
        field.formUp(CAMP_GAP - PULLBACK_SHORT_OF_CAMP);
        broadcast(String.format(Locale.ROOT, "[battle day %d] Dawn: %s musters %d, %s %d; the armies deploy", day,
                sides[0].faction.name(), field.army(0).started(), sides[1].faction.name(), field.army(1).started()));
        spawnCouncil();
    }

    /**
     * After the numbers decide the battle: a general's fall makes their army retreat, and the winner may pursue it
     * for up to a day (a bold or hard-charging general does; a cautious one doesn't; else it is a coin toss). An army
     * worn down with its general alive surrenders instead: the fighting stops where it stands.
     */
    private void victory(FieldBattle.Outcome outcome) {
        winner = outcome.winner();
        if (winner < 0) {
            setPhase(Phase.DONE);
            restoreRelations();
            broadcast("Use /pve debug battle clear to remove the survivors and the camps");
            return;
        }
        int loser = 1 - winner;
        Side w = sides[winner];
        if (!field.army(loser).generalAlive()) {
            boolean outnumbered = field.army(winner).alive() < field.army(loser).alive() * PURSUE_ONLY_UNDER;
            Trait trait = w.traits[0];
            boolean pursue = !outnumbered && (trait == Trait.BOLD || trait != Trait.CAUTIOUS && (w.general.plan().has(Strategy.UNRELENTING_CHARGE)
                    || w.general.plan().has(Strategy.DECAPITATE) || level.random.nextBoolean()));
            if (pursue) {
                field.pursue();
                setPhase(Phase.PURSUIT);
                broadcast(String.format(Locale.ROOT, "[battle] General %s pursues %s's retreating army", w.general.name(),
                        sides[loser].faction.name()));
                announce(winner, "Pursue them!");
            } else {
                field.standDown(false);
                setPhase(Phase.AFTERMATH);
                broadcast(String.format(Locale.ROOT, "[battle] General %s lets %s's army go", w.general.name(),
                        sides[loser].faction.name()));
            }
        } else {
            surrendered = true;
            field.standDown(true);
            setPhase(Phase.AFTERMATH);
            broadcast(String.format(Locale.ROOT, "[battle] %s surrenders; the fighting stops", sides[loser].faction.name()));
            announce(winner, "They surrender! Hold!");
            announce(loser, "We surrender. Lay down arms.");
        }
        aftermathSince = field.seconds();
        for (Live l : live.values()) {
            l.appliedDuty = null;
        }
    }

    /**
     * The pursuit or the aftermath: the beaten army runs (or mills about, surrendered) until none of it has been
     * harmed by the winner for {@link #QUIET_SECONDS}, or a pursuit has lasted a day; then it fades away, with its camp.
     */
    private void aftermath() {
        for (Live l : new ArrayList<>(live.values())) {
            readBack(l);
        }
        field.second();
        for (Live l : new ArrayList<>(live.values())) {
            dealDamage(l);
            passOnOrders(l);
            frontRowFights(l);
        }
        updateEnlisted();
        switchLive();
        int loser = 1 - winner;
        int seconds = field.seconds();
        int hurt = Math.max(field.lastHurt(loser), lastRealHurt[loser]);
        boolean quiet = seconds - Math.max(hurt, aftermathSince) >= QUIET_SECONDS;
        boolean dayGone = phase == Phase.PURSUIT && seconds - aftermathSince >= PURSUIT_SECONDS;
        if (quiet || dayGone) {
            fadeOut(loser);
        }
    }

    /**
     * The beaten army leaves the field: its soldiers fade away a few at a time, its figures are gone from the field,
     * and its camp is struck. The factions' old relations come back.
     */
    private void fadeOut(int side) {
        faded[side] = true;
        for (Live l : new ArrayList<>(live.values())) {
            if (l.block.side() == side) {
                for (AbstractRecruitEntity s : l.living()) {
                    fading.add(s.getUUID());
                }
                for (int i = 0; i < l.soldiers.length; i++) {
                    if (l.soldiers[i] != null) {
                        byEntity.remove(l.soldiers[i].getId());
                        l.soldiers[i] = null;
                    }
                }
                l.block.setReal(0);
                live.remove(l.block);
            }
        }
        strikeCamp(side);
        restoreRelations();
        setPhase(Phase.DONE);
        broadcast(String.format(Locale.ROOT, "[battle] %s's army %s the field", sides[side].faction.name(),
                surrendered ? "lays down its arms and leaves" : "has fled"));
        broadcast("Use /pve debug battle clear to remove the survivors and the camps");
    }

    /** A camp is struck: its guards and council go, and its tents come down. */
    private void strikeCamp(int side) {
        for (UUID id : campFolk.getOrDefault(side, List.of())) {
            Entity e = level.getEntity(id);
            if (e != null) {
                e.discard();
            }
        }
        campFolk.remove(side);
        for (Map.Entry<UUID, Integer> c : new ArrayList<>(councillors.entrySet())) {
            if (c.getValue() == side) {
                Entity e = level.getEntity(c.getKey());
                if (e != null) {
                    e.discard();
                }
                councillors.remove(c.getKey());
            }
        }
        if (camps[side] != null) {
            camps[side].restore();
            camps[side] = null;
        }
        for (int wing = 0; wing < 3; wing++) {
            if (wingCamps[side][wing] != null) {
                wingCamps[side][wing].restore();
                wingCamps[side][wing] = null;
            }
        }
    }

    /** Each tick: a few of a fading army's soldiers vanish in a puff, with their horses. */
    private void fadeSome() {
        for (int n = 0; n < FADE_PER_TICK && !fading.isEmpty(); n++) {
            UUID id = fading.remove(fading.size() - 1);
            Entity e = level.getEntity(id);
            if (e == null) {
                continue;
            }
            Entity horse = e.getVehicle();
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.POOF, e.getX(), e.getY() + 1, e.getZ(), 6, 0.3, 0.5, 0.3, 0.02);
            if (horse != null) {
                horse.discard();
            }
            e.discard();
        }
    }

    // ---- measuring real soldiers fighting real soldiers, to calibrate the numbers ----

    /**
     * One second's measurements for {@link Calibration}, among real blocks only: the health each lost and who was
     * striking it, decided by the numeric battle's own rules (a formed block strikes the nearest enemy block within
     * its reach, archers shoot the nearest within range; a charged soldier strikes the target it is next to), and
     * how fast blocks walk.
     */
    private void measure() {
        Map<Integer, Live> blockOf = new HashMap<>();
        Map<Live, List<AbstractRecruitEntity>> living = new HashMap<>();
        for (Live l : live.values()) {
            List<AbstractRecruitEntity> soldiers = l.living();
            living.put(l, soldiers);
            for (AbstractRecruitEntity s : soldiers) {
                blockOf.put(s.getId(), l);
            }
        }
        Map<Live, double[]> strikers = new HashMap<>();
        for (Live l : live.values()) {
            List<AbstractRecruitEntity> soldiers = living.get(l);
            Duty duty = l.block.duty();
            if (soldiers.isEmpty() || duty == Duty.WITHDRAWING || duty == Duty.BEARER || duty == Duty.FLEEING) {
                continue;
            }
            FieldArmy enemy = field.army(1 - l.block.side());
            if (l.block.charged()) {
                Calibration.Striker kind = melee(l.block.arm());
                for (AbstractRecruitEntity s : soldiers) {
                    LivingEntity target = s.getTarget();
                    Live struck = target == null || !target.isAlive() ? null : blockOf.get(target.getId());
                    if (struck != null && struck.block.side() != l.block.side() && s.distanceTo(target) <= STRIKING_DISTANCE) {
                        strike(strikers, struck, kind, FieldRules.meleeFactor(sideOf(enemy, struck, new Point(s.getX(), s.getZ())), struck.block.arm()));
                    }
                }
                continue;
            }
            Point centre = centre(soldiers);
            FieldArmy army = field.army(l.block.side());
            Live foe = null;
            double best = Double.MAX_VALUE;
            for (Live e : live.values()) {
                List<AbstractRecruitEntity> enemySoldiers = living.get(e);
                if (e.block.side() != l.block.side() && !enemySoldiers.isEmpty()) {
                    double d = frontDistance(army, centre, enemy, centre(enemySoldiers), e.block.started());
                    if (d < best) {
                        best = d;
                        foe = e;
                    }
                }
            }
            if (foe == null) {
                continue;
            }
            if (best <= FieldRules.HOLDING_REACH) {
                strike(strikers, foe, melee(l.block.arm()),
                        soldiers.size() * FieldRules.meleeFactor(sideOf(enemy, foe, centre), foe.block.arm()));
            } else if (l.block.arm() == Arm.ARCHERS && arrows(soldiers) > 0) {
                Live shot = null;
                double nearest = FieldRules.ARCHER_RANGE;
                for (Live e : live.values()) {
                    List<AbstractRecruitEntity> enemySoldiers = living.get(e);
                    if (e.block.side() != l.block.side() && !enemySoldiers.isEmpty() && centre(enemySoldiers).distanceTo(centre) <= nearest) {
                        nearest = centre(enemySoldiers).distanceTo(centre);
                        shot = e;
                    }
                }
                if (shot != null) {
                    strike(strikers, shot, Calibration.Striker.ARROWS,
                            soldiers.size() * FieldRules.arrowFactor(sideOf(enemy, shot, centre), shot.block.arm()));
                }
            }
        }
        for (Live l : live.values()) {
            List<AbstractRecruitEntity> soldiers = living.get(l);
            double health = 0;
            for (AbstractRecruitEntity s : soldiers) {
                health += s.getHealth();
            }
            Double before = lastHealth.put(l.block, health);
            double[] struck = strikers.get(l);
            if (before != null && (struck != null || before > health)) {
                calibration.addDamage(Math.max(0, before - health), struck == null ? new double[Calibration.Striker.values().length] : struck);
            }
            if (soldiers.isEmpty()) {
                continue;
            }
            Point centre = centre(soldiers);
            Point was = lastCentre.put(l.block, centre);
            if (was != null && !l.block.charged() && centre.distanceTo(l.block.line()) >= 8) {
                calibration.addSpeed(l.block.arm() == Arm.CAVALRY, centre.distanceTo(was));
            }
        }
    }

    private static Point centre(List<AbstractRecruitEntity> soldiers) {
        double x = 0;
        double z = 0;
        for (AbstractRecruitEntity s : soldiers) {
            x += s.getX();
            z += s.getZ();
        }
        return soldiers.isEmpty() ? new Point(0, 0) : new Point(x / soldiers.size(), z / soldiers.size());
    }

    private static Calibration.Striker melee(Arm arm) {
        return switch (arm) {
            case SWORDSMEN, SHIELDWALL -> Calibration.Striker.INFANTRY;
            case CAVALRY -> Calibration.Striker.CAVALRY;
            case ARCHERS -> Calibration.Striker.ARCHER_MELEE;
        };
    }

    private static void strike(Map<Live, double[]> strikers, Live struck, Calibration.Striker kind, double weight) {
        strikers.computeIfAbsent(struck, k -> new double[Calibration.Striker.values().length])[kind.ordinal()] += weight;
    }

    /** Where a blow from {@code from} falls on a block of the army {@code defenders}, which faces the army's way. */
    private static FieldRules.Side sideOf(FieldArmy defenders, Live struck, Point from) {
        Point at = centre(struck.living());
        return FieldRules.side(defenders.facingX(), defenders.facingZ(), from.x() - at.x(), from.z() - at.z());
    }

    /** How far apart two blocks' front ranks are, each standing square to its army's facing. */
    private static double frontDistance(FieldArmy army, Point at, FieldArmy enemy, Point enemyAt, int enemySoldiers) {
        double hw = FieldRules.BLOCK_HALF_WIDTH;
        double ehw = enemySoldiers > 1 ? FieldRules.BLOCK_HALF_WIDTH : FieldRules.LONE_HALF_WIDTH;
        return FieldRules.segmentDistance(
                at.x() - army.facingZ() * hw, at.z() + army.facingX() * hw, at.x() + army.facingZ() * hw, at.z() - army.facingX() * hw,
                enemyAt.x() - enemy.facingZ() * ehw, enemyAt.z() + enemy.facingX() * ehw,
                enemyAt.x() + enemy.facingZ() * ehw, enemyAt.z() - enemy.facingX() * ehw);
    }

    /** Arrows the soldiers carry, all together. */
    private static int arrows(List<AbstractRecruitEntity> soldiers) {
        int n = 0;
        for (AbstractRecruitEntity s : soldiers) {
            for (int i = 0; i < s.getInventory().getContainerSize(); i++) {
                ItemStack stack = s.getInventory().getItem(i);
                if (stack.is(Items.ARROW)) {
                    n += stack.getCount();
                }
            }
        }
        return n;
    }

    /**
     * Fights the same battle only as numbers a few times (each army's size as here, different luck), so its end can
     * be set beside this one's.
     */
    private static String predict(int aBlocks, int bBlocks, Side[] sides, Point centre, double axisX, double axisZ) {
        if (aBlocks + bBlocks > PREDICT_UP_TO_BLOCKS) {
            return "not worked out (too big to fight through at the start)";
        }
        int runs = Math.max(2, Math.min(20, 400 / (aBlocks + bBlocks)));
        int[] wins = new int[3];
        long seconds = 0;
        long leftA = 0;
        long leftB = 0;
        for (int i = 0; i < runs; i++) {
            FieldBattle battle = new FieldBattle(aBlocks, bBlocks, centre, axisX, axisZ, i, sides[0].general.plan(), sides[1].general.plan());
            FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
            wins[outcome == null || outcome.winner() < 0 ? 2 : outcome.winner()]++;
            seconds += battle.seconds();
            leftA += battle.army(0).alive();
            leftB += battle.army(1).alive();
        }
        return String.format(Locale.ROOT, "%s wins %d of %d, %s %d, draws %d; about %d s; %d and %d left",
                sides[0].faction.name(), wins[0], runs, sides[1].faction.name(), wins[1], wins[2], seconds / runs, leftA / runs, leftB / runs);
    }

    /** Reports what this battle measured, and everything measured since the server started. */
    private void reportCalibration() {
        SESSION_CALIBRATION.addAll(calibration);
        broadcast("[calibration] this battle: " + calibration.fit().describe());
        broadcast("[calibration] all battles so far: " + SESSION_CALIBRATION.fit().describe());
        broadcast("[calibration] only as numbers: " + prediction);
    }

    // ---- keeping the ground loaded ----

    /**
     * Keeps the chunks around every real soldier loaded and running, wherever the players are, and lets go of the
     * rest. Blocks that are only numbers need no ground.
     */
    private void holdChunks() {
        Set<Long> wanted = new HashSet<>();
        for (Live l : live.values()) {
            for (AbstractRecruitEntity soldier : l.living()) {
                ChunkPos at = soldier.chunkPosition();
                for (int dx = -HELD_RADIUS; dx <= HELD_RADIUS; dx++) {
                    for (int dz = -HELD_RADIUS; dz <= HELD_RADIUS; dz++) {
                        wanted.add(ChunkPos.asLong(at.x + dx, at.z + dz));
                    }
                }
            }
        }
        for (long chunk : wanted) {
            hold(chunk);
        }
        heldChunks.removeIf(chunk -> {
            if (wanted.contains(chunk)) {
                return false;
            }
            ChunkPos pos = new ChunkPos(chunk);
            level.getChunkSource().removeRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
            return true;
        });
    }

    private void hold(long chunk) {
        if (heldChunks.add(chunk)) {
            ChunkPos pos = new ChunkPos(chunk);
            // Distance 2 makes the chunk itself entity-ticking, as /forceload does; forcing ticks (a Forge addition)
            // lets its soldiers run with no player within simulation distance.
            level.getChunkSource().addRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
        }
    }

    /** Loads, and holds, the ground a block is about to stand on, before its soldiers are spawned. */
    private void loadGround(Point at) {
        int cx = Mth.floor(at.x()) >> 4;
        int cz = Mth.floor(at.z()) >> 4;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                hold(ChunkPos.asLong(cx + dx, cz + dz));
                level.getChunk(cx + dx, cz + dz); // loads it now, not when the ticket is processed
            }
        }
    }

    private void releaseChunks() {
        for (long chunk : heldChunks) {
            ChunkPos pos = new ChunkPos(chunk);
            level.getChunkSource().removeRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
        }
        heldChunks.clear();
    }

    /**
     * The ground's height at a spot, for drawing blocks that are only numbers: from the world where it is loaded,
     * else from the world generator (as Distant Horizons draws it), without loading anything. At most
     * {@link #GROUND_LOOKUPS_PER_SECOND} new generator lookups a second; past that, a block keeps the height it last
     * had, so a huge battle's first seconds don't stall the server.
     */
    private int groundY(FieldBlock block, double x, double z) {
        int bx = Mth.floor(x);
        int bz = Mth.floor(z);
        int y;
        if (level.hasChunk(bx >> 4, bz >> 4)) {
            y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, bx, bz);
        } else {
            long chunk = ChunkPos.asLong(bx >> 4, bz >> 4);
            Integer known = groundCache.get(chunk);
            if (known == null && groundLookups < GROUND_LOOKUPS_PER_SECOND) {
                groundLookups++;
                known = level.getChunkSource().getGenerator().getBaseHeight((bx & ~15) + 8, (bz & ~15) + 8,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, level, level.getChunkSource().randomState());
                groundCache.put(chunk, known);
            }
            y = known != null ? known : lastGround.getOrDefault(block.id(), level.getSeaLevel());
        }
        lastGround.put(block.id(), y);
        return y;
    }

    // ---- the distant view ----

    /**
     * Every block still standing, for clients to draw beyond the range at which they see soldiers: real blocks from
     * where their soldiers stand (with their entity ids), the others from the numbers.
     */
    private List<FormationSnapshot> snapshots() {
        groundLookups = 0;
        List<FormationSnapshot> all = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            if (faded[side]) {
                continue;
            }
            FieldArmy army = field.army(side);
            Side s = sides[side];
            for (FieldBlock block : army.blocks()) {
                if (block.alive() == 0) {
                    continue;
                }
                FormationSnapshot.Kind kind = block.duty() == Duty.GENERAL ? FormationSnapshot.Kind.GENERAL
                        : block.duty() == Duty.BEARER ? FormationSnapshot.Kind.BEARER : FormationSnapshot.Kind.of(block.arm());
                Live l = live.get(block);
                List<AbstractRecruitEntity> living = l == null ? List.of() : l.living();
                if (!living.isEmpty()) {
                    all.add(liveSnapshot(block, kind, s, army, l, living));
                    continue;
                }
                boolean lone = block.started() == 1;
                Point middle = BattlePlan.place(block.pos(), block.facingX(), block.facingZ(), 0, lone ? 0 : BattlePlan.SPACING);
                all.add(new FormationSnapshot(block.id(), kind, s.uniform, s.mark, block.alive(), middle.x(),
                        groundY(block, middle.x(), middle.z()), middle.z(), block.facingX(), block.facingZ(),
                        lone ? 0 : block.halfWidth(), lone ? 0 : BattlePlan.SPACING, block.fighting(),
                        block.captainAlive(), new int[0], -1));
            }
        }
        return all;
    }

    /**
     * A real block, from where its real soldiers stand (with their entity ids) and, for a block only part real, where
     * the rest stand in its ranks (with no id, so the client draws them as figures there), in the order of the ranks.
     */
    private static FormationSnapshot liveSnapshot(FieldBlock block, FormationSnapshot.Kind kind, Side side, FieldArmy army, Live l,
                                                  List<AbstractRecruitEntity> soldiers) {
        float[] health = block.health();
        Point front = front(block, health);
        double groundY = 0;
        for (AbstractRecruitEntity s : soldiers) {
            groundY += (s.getVehicle() != null ? s.getVehicle() : s).getY();
        }
        groundY /= soldiers.size();
        List<double[]> at = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < health.length; i++) {
            AbstractRecruitEntity s = l.soldiers[i];
            if (s != null && s.isAlive()) {
                // A rider stands on the ground where their horse does, not up in the saddle.
                Entity feet = s.getVehicle() != null ? s.getVehicle() : s;
                at.add(new double[]{feet.getX(), feet.getY(), feet.getZ()});
                idList.add(s.getId());
            } else if (s == null && health[i] > 0) {
                Point p = slot(block, front, i);
                at.add(new double[]{p.x(), groundY, p.z()});
                idList.add(-1);
            }
        }
        int n = at.size();
        double[] xs = new double[n];
        double[] ys = new double[n];
        double[] zs = new double[n];
        int[] ids = new int[n];
        int leaderId = -1;
        int captain = block.captain();
        for (int i = 0; i < n; i++) {
            xs[i] = at.get(i)[0];
            ys[i] = at.get(i)[1];
            zs[i] = at.get(i)[2];
            ids[i] = idList.get(i);
        }
        if (captain >= 0 && block.captainAlive()) {
            AbstractRecruitEntity c = l.soldiers[captain];
            leaderId = c != null && c.isAlive() ? c.getId() : -1;
        }
        return FormationSnapshot.of(block.id(), kind, side.uniform, side.mark, xs, ys, zs, army.facingX(), army.facingZ(),
                block.fighting(), leaderId >= 0, ids, leaderId);
    }

    /** Tells everyone in the level that the battle's formations are gone. */
    private void clearSnapshots() {
        ConquestNetwork.sendBattleSnapshot(level.players(), List.of());
    }

    /** To every player; the server log gets it too. */
    private void broadcast(String line) {
        broadcast(line, Component.literal(line));
    }

    /** To every player as {@code shown}; the server log and the battle's lines get it as plain text. */
    private void broadcast(String line, Component shown) {
        lines.add(line);
        if (lines.size() > 60) {
            lines.remove(0);
        }
        level.getServer().getPlayerList().broadcastSystemMessage(shown, false);
    }
}
