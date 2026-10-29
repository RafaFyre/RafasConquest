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
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import rafasconquest.character.Rank;
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
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.Calibration;
import rafasconquest.battle.FieldArmy;
import rafasconquest.battle.FieldBattle;
import rafasconquest.battle.FieldBlock;
import rafasconquest.battle.FieldBlock.Duty;
import rafasconquest.battle.FieldRules;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
    /** At most this many blocks become real each second, so a crowd doesn't appear in one tick. */
    private static final int SPAWNS_PER_SECOND = 8;
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
    private static final float HORN_VOLUME = 16;
    /** A charged soldier with no foe goes after the nearest real enemy this close. */
    private static final double HUNT_REACH = 80;
    /** Shield-bearers raise their shields within this reach of enemy archers (Recruits bowmen shoot out to 44). */
    private static final double ARROW_REACH = 50;
    /** ...and within this reach of any enemy, unless they're the ones striking. */
    private static final double MELEE_SHIELD_REACH = 12;
    /** Recruits' square formation, for the command screen. */
    private static final int SQUARE_FORMATION = 2;
    /** The slowing put on a spent charge's horses. */
    private static final UUID SPENT_HORSE = UUID.fromString("6f1c2d3e-4b5a-4c6d-8e7f-9a0b1c2d3e4f");
    /** A charged soldier this close to its target is taken to be striking it (for calibration, and for momentum). */
    private static final double STRIKING_DISTANCE = 4;

    @Nullable
    private static RecruitsBattle current;
    private static final Calibration SESSION_CALIBRATION = new Calibration();

    private final ServerLevel level;
    private final FieldBattle field;
    private final Side[] sides;
    /** The blocks that are real soldiers now, and their soldiers. */
    private final Map<FieldBlock, Live> live = new LinkedHashMap<>();
    /** Blocks that have been real at some time (their soldiers' starting health is measured once). */
    private final Set<FieldBlock> everLive = new HashSet<>();
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
                        String[] marshals) {
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
        /** The Recruits group the soldiers join, so they show in the player's command screen. */
        @Nullable
        UUID group;
        /** Where each soldier was put in the square; once the player orders them otherwise, they're the player's. */
        final Map<Integer, Vec3> squarePlace = new HashMap<>();
        @Nullable
        Point squareAt;
        boolean takenOver;

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
     * spawned yet: blocks near players become real soldiers over the next seconds. Returns a line for the caller, or
     * throws if Recruits isn't ready.
     */
    public static String start(ServerLevel level, Faction first, int firstSoldiers, Faction second, int secondSoldiers,
                               Point centre, double axisX, double axisZ) {
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
                        GameCharacter.general(first.doctrines(), characters), marshals(characters)),
                new Side(second, teamB, uniforms[1], markColor(second), banner(level, second), horn(second),
                        GameCharacter.general(second.doctrines(), characters), marshals(characters))};
        int blocksA = BattlePlan.blocks(firstSoldiers);
        int blocksB = BattlePlan.blocks(secondSoldiers);
        FieldBattle field = new FieldBattle(blocksA, blocksB, centre, axisX, axisZ, level.random.nextLong(),
                sides[0].general.plan(), sides[1].general.plan());
        String prediction = predict(blocksA, blocksB, sides, centre, axisX, axisZ);
        RecruitsBattle battle = new RecruitsBattle(level, field, sides, prediction);
        battle.oldRelations[0] = diplomacy.getRelation(teamA.getName(), teamB.getName());
        battle.oldRelations[1] = diplomacy.getRelation(teamB.getName(), teamA.getName());
        diplomacy.setRelation(teamA.getName(), teamB.getName(), DiplomacyStatus.ENEMY, level, false);
        diplomacy.setRelation(teamB.getName(), teamA.getName(), DiplomacyStatus.ENEMY, level, false);
        current = battle;
        String line = String.format(Locale.ROOT, "Battle: %s, %d soldiers, against %s, %d soldiers, at %d, %d",
                battle.describe(0), field.army(0).started(), battle.describe(1), field.army(1).started(),
                Math.round(centre.x()), Math.round(centre.z()));
        RafasConquest.LOGGER.info("[battle] {}", line);
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

    /** A block becomes real soldiers: its survivors stand in its ranks, each with the health they have left. */
    private void materialise(FieldBlock block) {
        FieldArmy army = field.army(block.side());
        Side side = sides[block.side()];
        loadGround(block.pos());
        Live l = new Live(block);
        float[] health = block.health();
        boolean first = everLive.add(block);
        Point enemyWay = field.enemyDirection(block);
        float yaw = yawOf(enemyWay);
        EntityType<? extends AbstractRecruitEntity> type = block.duty() == Duty.BEARER ? ModEntityTypes.RECRUIT.get() : type(block.arm());
        for (int i = 0; i < health.length; i++) {
            if (health[i] <= 0) {
                continue;
            }
            Role role = block.duty() == Duty.GENERAL ? Role.GENERAL : block.duty() == Duty.BEARER ? Role.BEARER
                    : block.duty() == Duty.GUARD ? Role.GUARD : i == block.captain() ? Role.LEADER : Role.SOLDIER;
            int files = block.files();
            int rank = i / files;
            double right = health.length == 1 ? 0 : (i % files - (files - 1) / 2.0) * BattlePlan.SPACING;
            Point p = BattlePlan.place(block.pos(), army.facingX(), army.facingZ(), right, rank * BattlePlan.SPACING * 1.75);
            AbstractRecruitEntity soldier = spawn(type, side, role, p, yaw);
            if (soldier == null) {
                continue;
            }
            if (block.champion()) {
                makeChampion(soldier);
            }
            if (first && (role == Role.SOLDIER || role == Role.LEADER)) {
                calibration.addSoldier(soldier.getHealth());
            }
            soldier.setHealth(Math.min(health[i], soldier.getMaxHealth()));
            l.soldiers[i] = soldier;
            byEntity.put(soldier.getId(), l);
        }
        block.setLive(true);
        live.put(block, l);
        if (block.duty() == Duty.BEARER) {
            for (AbstractRecruitEntity s : l.living()) {
                s.setAggroState(3); // passive: carries the flag, doesn't fight
            }
        }
        passOnOrders(l);
    }

    /** A block goes back to numbers, each soldier's health kept (already read this second), and its soldiers leave. */
    private void dematerialise(Live l) {
        discard(l);
        l.block.setLive(false);
        live.remove(l.block);
        lastHealth.remove(l.block);
        lastCentre.remove(l.block);
    }

    /** Removes a block's soldiers and their horses; returns how many soldiers were still standing. */
    private int discard(Live l) {
        int removed = 0;
        for (AbstractRecruitEntity e : l.soldiers) {
            if (e == null) {
                continue;
            }
            // Cavalry get their horse from Recruits on their first tick; it outlives a rider who falls or dismounts.
            UUID mountId = e.getMountUUID();
            Entity mount = mountId == null ? null : level.getEntity(mountId);
            if (mount != null) {
                mount.discard();
            }
            backBanners.remove(e.getId());
            byEntity.remove(e.getId());
            if (!e.isRemoved()) {
                e.discard();
                removed++;
            }
        }
        return removed;
    }

    /** Where each block should be: real near players (the nearest first, within the budget), else numbers. */
    private void switchLive() {
        List<Point> watchers = new ArrayList<>(watchPoints);
        for (ServerPlayer player : level.players()) {
            watchers.add(new Point(player.getX(), player.getZ()));
        }
        record Candidate(FieldBlock block, double distance) {
        }
        List<Candidate> wanted = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            for (FieldBlock block : field.army(side).blocks()) {
                if (block.alive() == 0) {
                    continue;
                }
                double distance = Double.MAX_VALUE;
                for (Point w : watchers) {
                    distance = Math.min(distance, w.distanceTo(block.pos()));
                }
                if (distance <= LIVE_WITHIN || block.live() && distance <= NUMBERS_BEYOND) {
                    wanted.add(new Candidate(block, distance));
                }
            }
        }
        wanted.sort(Comparator.comparingDouble(Candidate::distance));
        Set<FieldBlock> keep = new HashSet<>();
        int soldiers = 0;
        for (Candidate c : wanted) {
            int cost = c.block.alive() * (c.block.arm() == Arm.CAVALRY && c.block.started() > 1 ? 2 : 1);
            if (soldiers + cost > liveCap) {
                break;
            }
            soldiers += cost;
            keep.add(c.block);
        }
        for (Live l : new ArrayList<>(live.values())) {
            if (!keep.contains(l.block) || l.block.alive() == 0) {
                dematerialise(l);
            }
        }
        int spawned = 0;
        for (Candidate c : wanted) {
            if (keep.contains(c.block) && !c.block.live() && spawned < SPAWNS_PER_SECOND) {
                materialise(c.block);
                spawned++;
            }
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
        if (block.duty() != l.appliedDuty) {
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
            if (living.size() == 1) {
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
     * Where two real blocks meet in melee, only each block's front row fights, to spare the server: the rank nearest
     * the enemy (as many as its ranks are wide, or a wall's {@link FieldRules#WALL_FILES}). Each goes after
     * the nearest enemy soldier within {@link #FRONT_ROW_REACH} of its place, breaking formation to reach them, and
     * comes back to its place once there is none. The ranks behind hold their places, passive, until the front row
     * thins and they become it. The numbers leave such a fight to Recruits.
     */
    private void frontRowFights(Live l) {
        FieldBlock block = l.block;
        List<AbstractRecruitEntity> living = l.living();
        living.removeIf(s -> commanded.contains(s.getId()));
        FieldBlock foe = block.contactFoe();
        Live enemy = foe == null ? null : live.get(foe);
        boolean fighting = enemy != null && block.arm() != Arm.CAVALRY && !block.charged() && !passive(block.duty());
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
        double dx = foe.pos().x() - block.pos().x();
        double dz = foe.pos().z() - block.pos().z();
        List<AbstractRecruitEntity> ranked = new ArrayList<>(living);
        ranked.sort(Comparator.comparingDouble(s -> {
            Vec3 place = placeOf(s);
            return -(place.x * dx + place.z * dz);
        }));
        int front = Math.min(block.braced() ? FieldRules.WALL_FILES : block.files(), ranked.size());
        List<AbstractRecruitEntity> foes = enemy.living();
        double reachSqr = FRONT_ROW_REACH * FRONT_ROW_REACH;
        for (int i = 0; i < ranked.size(); i++) {
            AbstractRecruitEntity s = ranked.get(i);
            if (i >= front) {
                s.setTarget(null);
                s.setAggroState(3);
                s.holdFormation = true;
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

    /** Deals the numbers' damage to a real block: whole hits to random living soldiers, past their armour. */
    private void dealDamage(Live l) {
        l.damageCarry += l.block.drainLiveDamage();
        List<AbstractRecruitEntity> living = l.living();
        while (l.damageCarry >= FieldRules.HIT && !living.isEmpty()) {
            l.damageCarry -= FieldRules.HIT;
            AbstractRecruitEntity s = living.get(level.random.nextInt(living.size()));
            s.hurt(level.damageSources().magic(), FieldRules.HIT);
            if (!s.isAlive()) {
                living.remove(s);
            }
        }
        if (living.isEmpty()) {
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
        if (!started) {
            prepare();
            return;
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
        for (FieldBattle.Order order : field.takeCalls()) {
            soundHorn(order.side());
            if (order.call() == FieldBattle.Call.ADVANCE) {
                announce(order.side(), "ALL TROOPS CHARGE!");
            }
        }
        for (FieldBattle.Event event : field.takeEvents()) {
            Side side = sides[event.side()];
            broadcast(String.format(Locale.ROOT, "[battle %ds] %s's General %s %s", field.seconds(), side.faction.name(),
                    side.general.name(), event.text()));
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
        battle.enlist();
        battle.started = true;
        battle.broadcast("[battle] The battle begins");
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
                FieldArmy army = field.army(side);
                List<FieldBlock> front = new ArrayList<>();
                for (FieldBlock b : army.blocks()) {
                    if (b.duty() == Duty.FRONT && b.alive() > 0 && !taken.contains(b)) {
                        front.add(b);
                    }
                }
                if (front.isEmpty()) {
                    continue;
                }
                front.sort(Comparator.comparingDouble(b -> Math.abs(BattlePlan.offset(army.origin(), army.facingX(), army.facingZ(), b.line()).x())));
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
                if (RecruitEvents.recruitsGroupsManager != null) {
                    RecruitsGroup group = new RecruitsGroup(home.arm() == Arm.SHIELDWALL ? "Shields" : "Battle line", player, 0);
                    RecruitEvents.recruitsGroupsManager.addOrUpdateGroup(level, player, group);
                    e.group = group.getUUID();
                }
                CommandEvents.saveFormation(player, SQUARE_FORMATION);
                enlisted.put(player.getUUID(), e);
                Point spot = home.line();
                player.teleportTo(level, spot.x(), groundY(home, spot.x(), spot.z()), spot.z(),
                        yawOf(new Point(army.facingX(), army.facingZ())), 0);
                player.sendSystemMessage(Component.literal(String.format(Locale.ROOT,
                        "You fight for %s as a %s, in the first block to charge, leading %d. Keep to your ring.",
                        sides[side].faction.name(), rank.title(), leads)).withStyle(style -> style.withColor(color(e.side))));
            }
        }
    }

    /** Each second: enlisted players get their soldiers as they become real, their ring, and their orders. */
    private void updateEnlisted() {
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player == null) {
                continue;
            }
            if (giveSoldiers(e) && RecruitEvents.recruitsGroupsManager != null) {
                RecruitEvents.recruitsGroupsManager.broadCastGroupsToPlayer(player);
            }
            keepSquare(e);
            FieldBlock home = e.home;
            if (home.alive() > 0) {
                Point spot = home.line();
                ConquestNetwork.sendBattleSpot(player, true, spot.x(), groundY(home, spot.x(), spot.z()), spot.z());
            } else {
                ConquestNetwork.sendBattleSpot(player, false, 0, 0, 0);
            }
            ordersFor(e, player);
        }
    }

    /**
     * The player's soldiers, from their block and those beside it as they become real: owned by them and in a group
     * of theirs, so Recruits' command screen lists them. Returns whether any were given.
     */
    private boolean giveSoldiers(Enlisted e) {
        boolean any = false;
        for (FieldBlock b : e.blocks) {
            Live l = live.get(b);
            if (l == null) {
                continue;
            }
            for (AbstractRecruitEntity s : l.living()) {
                if (e.given.size() >= e.quota) {
                    return any;
                }
                if (commanded.add(s.getId())) {
                    e.given.add(s.getId());
                    any = true;
                    s.setOwnerUUID(Optional.of(e.player));
                    s.setIsOwned(true);
                    s.setAggroState(0);
                    s.setTarget(null);
                    if (e.group != null) {
                        s.setGroupUUID(e.group);
                        RecruitEvents.recruitsGroupsManager.addMember(e.group, s.getUUID(), level);
                    }
                }
            }
        }
        return any;
    }

    /** The player's soldiers that are still alive. */
    private List<AbstractRecruitEntity> soldiersOf(Enlisted e) {
        List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        for (FieldBlock b : e.blocks) {
            Live l = live.get(b);
            if (l != null) {
                for (AbstractRecruitEntity s : l.living()) {
                    if (e.given.contains(s.getId())) {
                        soldiers.add(s);
                    }
                }
            }
        }
        return soldiers;
    }

    /**
     * Until the player gives their soldiers orders of their own (from Recruits' command screen), they stand in a square
     * behind the player's ring, facing the enemy, and move with it as the block advances. A soldier found off its place
     * in the square, or no longer holding, means the player has taken them over.
     */
    private void keepSquare(Enlisted e) {
        if (e.takenOver || e.home.alive() == 0) {
            return;
        }
        List<AbstractRecruitEntity> soldiers = soldiersOf(e);
        for (AbstractRecruitEntity s : soldiers) {
            Vec3 placed = e.squarePlace.get(s.getId());
            Vec3 hold = s.getHoldPos();
            if (placed != null && (s.getFollowState() != 3 || hold == null || hold.distanceToSqr(placed) > 1)) {
                e.takenOver = true;
                return;
            }
        }
        Point spot = e.home.line();
        boolean newcomers = soldiers.stream().anyMatch(s -> !e.squarePlace.containsKey(s.getId()));
        if (soldiers.isEmpty() || (!newcomers && e.squarePlace.size() == soldiers.size() && e.squareAt != null
                && Math.hypot(e.squareAt.x() - spot.x(), e.squareAt.z() - spot.z()) < 2)) {
            return;
        }
        e.squareAt = spot;
        e.squarePlace.clear();
        double fx = e.home.facingX();
        double fz = e.home.facingZ();
        int cols = (int) Math.ceil(Math.sqrt(soldiers.size()));
        double sp = BattlePlan.SPACING;
        for (int i = 0; i < soldiers.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            double right = (col - (cols - 1) / 2.0) * sp;
            double back = (row + 1) * sp;
            double x = spot.x() - fx * back - fz * right;
            double z = spot.z() - fz * back + fx * right;
            Vec3 place = new Vec3(x, groundY(e.home, x, z), z);
            AbstractRecruitEntity s = soldiers.get(i);
            s.setHoldPos(place);
            s.setFollowState(3);
            s.isInFormation = true;
            s.holdFormation = false; // free to fight what comes near, then back to its place
            s.formationPos = i;
            e.squarePlace.put(s.getId(), place);
        }
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
            if (!s.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) || takenOver(s)) {
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

    private boolean takenOver(AbstractRecruitEntity s) {
        if (!commanded.contains(s.getId())) {
            return false;
        }
        for (Enlisted e : enlisted.values()) {
            if (e.given.contains(s.getId())) {
                return e.takenOver;
            }
        }
        return false;
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
        } else if (b.duty() == Duty.FRONT && b.fighting()) {
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
        if (strike != null && strike != e.lastStrike && strike.alive() > 0) {
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

    /** The battle is over or cleared: enlisted players lose their ring. */
    private void dismissEnlisted() {
        for (Enlisted e : enlisted.values()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e.player);
            if (player != null) {
                ConquestNetwork.sendBattleSpot(player, false, 0, 0, 0);
            }
            if (e.group != null && RecruitEvents.recruitsGroupsManager != null) {
                RecruitEvents.recruitsGroupsManager.removeGroup(e.group);
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
        float[] health = new float[l.soldiers.length];
        float[] known = l.block.health();
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
                x += feet.getX();
                z += feet.getZ();
                n++;
            }
        }
        field.sync(l.block, n == 0 ? l.block.pos() : new Point(x / n, z / n), health);
        if (lost) {
            dematerialise(l);
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
        Live by = battle.byEntity.get(attacker.getId());
        Entity rider = victim.getPassengers().isEmpty() ? victim : victim.getPassengers().get(0);
        Live of = battle.byEntity.get(rider.getId());
        if (by == null || of == null || by.block.side() == of.block.side()) {
            return amount;
        }
        boolean byCavalry = by.block.arm() == Arm.CAVALRY;
        if (byCavalry && by.block.charged()) {
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
        restoreRelations();
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
        broadcast(String.format(Locale.ROOT, "[battle over after %ds] %s. %s %d/%d left, %s %d/%d left | whole battle: %s",
                outcome.seconds(), result, sides[0].faction.name(), field.army(0).alive(), field.army(0).started(),
                sides[1].faction.name(), field.army(1).alive(), field.army(1).started(), whole.describe()));
        for (int side = 0; side < 2; side++) {
            FieldArmy army = field.army(side);
            broadcast(String.format(Locale.ROOT, "  %s: general %s, standard %s", describe(side),
                    army.generalAlive() ? "survived" : "fell", army.standardAlive() ? "still flies" : "fell"));
        }
        reportCalibration();
        broadcast("Use /pve debug battle clear to remove the survivors");
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

    private static FormationSnapshot liveSnapshot(FieldBlock block, FormationSnapshot.Kind kind, Side side, FieldArmy army, Live l,
                                                  List<AbstractRecruitEntity> soldiers) {
        int n = soldiers.size();
        double[] xs = new double[n];
        double[] ys = new double[n];
        double[] zs = new double[n];
        int[] ids = new int[n];
        int leaderId = -1;
        int captain = block.captain();
        for (int i = 0; i < n; i++) {
            AbstractRecruitEntity s = soldiers.get(i);
            // A rider stands on the ground where their horse does, not up in the saddle.
            Entity feet = s.getVehicle() != null ? s.getVehicle() : s;
            xs[i] = feet.getX();
            ys[i] = feet.getY();
            zs[i] = feet.getZ();
            ids[i] = s.getId();
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
        lines.add(line);
        if (lines.size() > 60) {
            lines.remove(0);
        }
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(line), false);
    }
}
