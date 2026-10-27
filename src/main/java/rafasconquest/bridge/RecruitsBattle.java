package rafasconquest.bridge;

import com.talhanation.recruits.FactionEvents;
import com.talhanation.recruits.config.RecruitsServerConfig;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.BowmanEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import com.talhanation.recruits.util.FormationUtils;
import com.talhanation.recruits.world.RecruitsDiplomacyManager;
import com.talhanation.recruits.world.RecruitsDiplomacyManager.DiplomacyStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.General;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A live battle between two factions' armies of Recruits soldiers, for the prototype. Each army deploys as
 * {@link BattlePlan#deployment}: 5 formations of 20 in full leather dyed the faction's colour, each led by a soldier
 * wearing the faction banner, plus a general (gold helmet, banner on the back) and a standard-bearer at the general's
 * side. The general is a warrior or a tactician, and gives each formation an order every second
 * ({@link BattlePlan#order}). The two factions are made enemies in Recruits' diplomacy for the battle (so their
 * soldiers attack each other and nobody else; a bystander is left alone unless they strike first) and their old
 * relation is restored afterwards. Also times every server tick while it runs. One battle at a time; server thread
 * only.
 */
public final class RecruitsBattle {
    private static final int REPORT_SECONDS = 10;
    /** A flanking ride that hasn't got round in this long charges anyway. */
    private static final int FLANK_SECONDS = 40;
    /**
     * Keeps the battle's chunks loaded with its soldiers running, as a forceloaded chunk is, but only while it lasts
     * (not saved with the world).
     */
    private static final TicketType<ChunkPos> BATTLE_TICKET = TicketType.create(RafasConquest.MOD_ID + "_battle", Comparator.comparingLong(ChunkPos::toLong));
    /** A soldier's chunk and those this many chunks around it stay loaded: more than a soldier walks in a second. */
    private static final int HELD_RADIUS = 1;
    /** How often clients hear where the formations stand. */
    private static final int SNAPSHOT_TICKS = 20;
    /** Snapshot ids each army has: up to 6 formations, then the general and the standard-bearer. */
    private static final int IDS_PER_ARMY = 8;

    @Nullable
    private static RecruitsBattle current;

    private final ServerLevel level;
    private final Army[] armies;
    private final DiplomacyStatus[] oldRelations = new DiplomacyStatus[2];
    private final List<AbstractRecruitEntity> spawned = new ArrayList<>();
    /** Every soldier and horse of the battle, so their deaths drop nothing. */
    private final Set<UUID> ours = new HashSet<>();
    /** Generals' entity ids → the banner on their backs, for players who start seeing them. */
    private final Map<Integer, ItemStack> backBanners = new HashMap<>();
    private int ticks;
    private boolean over;
    /** When a soldier last fell (0 before anyone has), and whether the battle has come to a final melee. */
    private int lastFall;
    private boolean finalMelee;
    private final int[] lastAlive = new int[2];
    private final TickTimes whole = new TickTimes();
    private final TickTimes window = new TickTimes();
    private long tickStart;
    private int snapshotTicks;
    /** Time spent on the battle's own work (orders, snapshots, chunks) since the last report, in nanoseconds. */
    private long ownNanos;
    /** Chunks the battle keeps loaded and running, as {@link ChunkPos#toLong}. */
    private final Set<Long> heldChunks = new HashSet<>();

    private RecruitsBattle(ServerLevel level, Army[] armies) {
        this.level = level;
        this.armies = armies;
    }

    private static final class Army {
        final Faction faction;
        final PlayerTeam team;
        final double facingX;
        final double facingZ;
        final General general;
        final int uniform;
        /** The banner's first pattern colour, flown on distant leaders' flags. */
        final int mark;
        final ItemStack banner;
        final List<Formation> formations = new ArrayList<>();
        /** The block holding each file of the front line (it may have fallen; a relief then takes its place). */
        Formation[] front = new Formation[0];
        /** Half the width of the front line, for the enemy's cavalry to ride round. */
        double halfWidth;
        double generalBack;
        @Nullable
        AbstractRecruitEntity commander;
        @Nullable
        AbstractRecruitEntity bearer;
        boolean commanderCharged;
        boolean reserveCommitted;
        /** Front blocks relieved so far, for the reports. */
        int reliefs;
        int started;
        /** Its living soldiers, worked out once a second. */
        List<AbstractRecruitEntity> living = List.of();
        /** Hands out each block's place in its file's queue, in the order they joined it. */
        int queueTicket;

        Army(Faction faction, PlayerTeam team, double facingX, double facingZ, General general, int uniform, int mark, ItemStack banner) {
            this.faction = faction;
            this.team = team;
            this.facingX = facingX;
            this.facingZ = facingZ;
            this.general = general;
            this.uniform = uniform;
            this.mark = mark;
            this.banner = banner;
        }

        /** Works out every block's living soldiers, and the army's, for this second. */
        void count() {
            List<AbstractRecruitEntity> all = new ArrayList<>();
            for (Formation f : formations) {
                f.count();
                all.addAll(f.living);
            }
            for (AbstractRecruitEntity e : new AbstractRecruitEntity[]{commander, bearer}) {
                if (e != null && e.isAlive()) {
                    all.add(e);
                }
            }
            living = all;
        }

        boolean infantryEngaged() {
            for (Formation f : front) {
                if (f.engaged && !f.living.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        /** The middle of the front line: the average of its blocks' lines. */
        Point frontCentre() {
            double x = 0;
            double z = 0;
            for (Formation f : front) {
                x += f.line.x();
                z += f.line.z();
            }
            return front.length == 0 ? formations.get(0).line : new Point(x / front.length, z / front.length);
        }

        /** The blocks waiting behind the front in {@code file}, nearest the front first. */
        List<Formation> queue(int file) {
            return formations.stream().filter(f -> f.duty == Duty.LINE && f.file == file)
                    .sorted(Comparator.comparingInt(f -> f.queueTicket)).toList();
        }

        /** Puts a block at the back of a file's queue. */
        void join(Formation f, int file) {
            f.duty = Duty.LINE;
            f.file = file;
            f.queueTicket = queueTicket++;
        }

        String describe() {
            return faction.name() + " (" + general.label() + ")";
        }
    }

    /** What an infantry block is doing in the army's lines. */
    private enum Duty {
        /** Holding its file of the front line, marching or fighting. */
        FRONT,
        /** Waiting in its file's queue behind the front, to relieve it. */
        LINE,
        /** Marching up to take over its file of the front. */
        RELIEVING,
        /** Relieved: going back behind the lines, not fighting on the way. */
        WITHDRAWING,
        /** Back behind the lines, too worn to go forward again; fights only if the enemy reaches it. */
        RESTING,
        /** A tactician's reserve, waiting to be committed. */
        RESERVE,
        /** Archers and cavalry, which take their own orders. */
        SUPPORT
    }

    /** A block of 20 soldiers, led by its captain. */
    private static final class Formation {
        final Arm arm;
        /** Where it stands across the army when not in a line (reserve, archers, cavalry). */
        final double slotRight;
        final double slotBack;
        final List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        @Nullable
        AbstractRecruitEntity leader;
        /** The centre of its front rank, where its line is ordered to stand. */
        Point line;
        Duty duty;
        /** The file of the front line it holds, waits behind or relieves. */
        int file;
        int queueTicket;
        /** When it took up its current duty, and where it withdraws to. */
        int dutySince;
        @Nullable
        Point rear;
        /** The block on its way to relieve it, if any. */
        @Nullable
        Formation relief;
        /** In the front line and fighting. */
        boolean engaged;
        /** Broken formation to fight freely (cavalry). */
        boolean charged;
        /** A flanking ride's waypoints, once it has set off, and when it did. */
        @Nullable
        List<Point> route;
        int routeIndex;
        int routeStarted;
        /** Its living soldiers, worked out once a second. */
        List<AbstractRecruitEntity> living = List.of();

        Formation(BattlePlan.Slot slot, Point line) {
            this.arm = slot.arm();
            this.slotRight = slot.right();
            this.slotBack = slot.back();
            this.line = line;
            this.file = slot.file();
            if (!slot.arm().infantry()) {
                duty = Duty.SUPPORT;
            } else if (slot.reserve()) {
                duty = Duty.RESERVE;
            } else {
                duty = slot.line() == 0 ? Duty.FRONT : Duty.LINE;
            }
        }

        void count() {
            living = soldiers.stream().filter(LivingEntity::isAlive).toList();
        }

        List<AbstractRecruitEntity> alive() {
            return living;
        }

        Point centre() {
            if (living.isEmpty()) {
                return line;
            }
            return new Point(living.stream().mapToDouble(Entity::getX).average().orElse(line.x()),
                    living.stream().mapToDouble(Entity::getZ).average().orElse(line.z()));
        }
    }

    /** What a soldier is, for its kit. */
    private enum Role {
        SOLDIER, LEADER, GENERAL, BEARER
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
     * Deploys both armies around {@code centre}, facing each other along ({@code axisX}, {@code axisZ}) (a unit
     * vector from {@code first}'s side toward {@code second}'s), and starts the battle. Each army's general is a
     * warrior or a tactician, 50/50. Each army is {@code firstSoldiers} or {@code secondSoldiers} strong, in whole
     * blocks of 20 ({@link BattlePlan#blocks}), plus its general and standard-bearer. Returns a line for the caller, or
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
        Army a = new Army(first, teamA, axisX, axisZ, rollGeneral(level), uniforms[0], markColor(first), banner(level, first));
        Army b = new Army(second, teamB, -axisX, -axisZ, rollGeneral(level), uniforms[1], markColor(second), banner(level, second));
        RecruitsBattle battle = new RecruitsBattle(level, new Army[]{a, b});
        battle.oldRelations[0] = diplomacy.getRelation(teamA.getName(), teamB.getName());
        battle.oldRelations[1] = diplomacy.getRelation(teamB.getName(), teamA.getName());
        diplomacy.setRelation(teamA.getName(), teamB.getName(), DiplomacyStatus.ENEMY, level, false);
        diplomacy.setRelation(teamB.getName(), teamA.getName(), DiplomacyStatus.ENEMY, level, false);

        current = battle; // before spawning, so the first players to see a general hear about its banner
        Point[] fronts = BattlePlan.fronts(centre, axisX, axisZ);
        long spawnStart = System.nanoTime();
        battle.deploy(a, fronts[0], BattlePlan.blocks(firstSoldiers));
        battle.deploy(b, fronts[1], BattlePlan.blocks(secondSoldiers));
        long spawnMillis = (System.nanoTime() - spawnStart) / 1_000_000;
        battle.holdChunks();
        String line = String.format(Locale.ROOT, "Battle: %s, %d soldiers, against %s, %d soldiers, at %d, %d (deployed in %d ms)",
                a.describe(), a.started, b.describe(), b.started, Math.round(centre.x()), Math.round(centre.z()), spawnMillis);
        RafasConquest.LOGGER.info("[battle] {}", line);
        return line;
    }

    private static General rollGeneral(ServerLevel level) {
        return level.random.nextBoolean() ? General.WARRIOR : General.TACTICIAN;
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

    private static ItemStack banner(ServerLevel level, Faction faction) {
        return FactionBannerNbt.item(level.registryAccess(), FactionBanner.of(faction.id()));
    }

    /** Ends any battle: removes every soldier (and horse) it spawned, alive or not, and restores the old relations. */
    public static String stop() {
        RecruitsBattle battle = current;
        if (battle == null) {
            return "No battle to clear";
        }
        current = null;
        int removed = 0;
        for (AbstractRecruitEntity e : battle.spawned) {
            // Cavalry get their horse from Recruits on their first tick; it outlives a rider who falls or dismounts.
            UUID mountId = e.getMountUUID();
            Entity mount = mountId == null ? null : battle.level.getEntity(mountId);
            if (mount != null) {
                mount.discard();
            }
            if (!e.isRemoved()) {
                e.discard();
                removed++;
            }
        }
        battle.clearSnapshots();
        battle.releaseChunks();
        battle.restoreRelations();
        return "Cleared the battle (" + removed + " soldiers removed)";
    }

    private void restoreRelations() {
        RecruitsDiplomacyManager diplomacy = FactionEvents.recruitsDiplomacyManager;
        if (diplomacy != null && oldRelations[0] != null) {
            String a = armies[0].team.getName();
            String b = armies[1].team.getName();
            diplomacy.setRelation(a, b, oldRelations[0], level, false);
            diplomacy.setRelation(b, a, oldRelations[1], level, false);
            oldRelations[0] = null;
        }
    }

    // ---- deployment ----

    private void deploy(Army army, Point front, int blocks) {
        float yaw = (float) Math.toDegrees(Math.atan2(-army.facingX, army.facingZ));
        List<BattlePlan.Slot> slots = BattlePlan.deployment(army.general, blocks);
        loadGround(army, front, slots);
        int files = (int) slots.stream().filter(s -> s.arm().infantry() && s.line() == 0).count();
        army.front = new Formation[files];
        army.halfWidth = files * BattlePlan.BLOCK_PITCH / 2;
        for (BattlePlan.Slot slot : slots) {
            Point line = BattlePlan.place(front, army.facingX, army.facingZ, slot.right(), slot.back());
            Formation formation = new Formation(slot, line);
            for (int i = 0; i < BattlePlan.SOLDIERS_PER_FORMATION; i++) {
                // Stand them roughly where their place in the line will be, so the first order is a short shuffle.
                int rank = i / BattlePlan.FILE_WIDTH;
                double right = slot.right() + (i % BattlePlan.FILE_WIDTH - (BattlePlan.FILE_WIDTH - 1) / 2.0) * BattlePlan.SPACING;
                Point p = BattlePlan.place(front, army.facingX, army.facingZ, right, slot.back() + rank * BattlePlan.SPACING * 1.75);
                Role role = i == BattlePlan.LEADER_INDEX ? Role.LEADER : Role.SOLDIER;
                AbstractRecruitEntity soldier = spawn(type(slot.arm()), army, role, p, yaw);
                if (soldier != null) {
                    formation.soldiers.add(soldier);
                    if (role == Role.LEADER) {
                        formation.leader = soldier;
                    }
                }
            }
            army.started += formation.soldiers.size();
            army.formations.add(formation);
            if (formation.duty == Duty.FRONT) {
                army.front[slot.file()] = formation;
            } else if (formation.duty == Duty.LINE) {
                army.join(formation, slot.file());
            }
            standInLine(army, formation);
        }

        double back = BattlePlan.generalBack(army.general, slots);
        army.generalBack = back;
        army.commander = spawn(ModEntityTypes.RECRUIT.get(), army, Role.GENERAL,
                BattlePlan.place(front, army.facingX, army.facingZ, 0, back), yaw);
        army.bearer = spawn(ModEntityTypes.RECRUIT.get(), army, Role.BEARER,
                BattlePlan.place(front, army.facingX, army.facingZ, BattlePlan.BEARER_RIGHT, back), yaw);
        if (army.commander != null) {
            army.started++;
            hold(army.commander, BattlePlan.place(front, army.facingX, army.facingZ, 0, back));
            if (army.general == General.WARRIOR) {
                makeChampion(army.commander);
            }
        }
        if (army.bearer != null) {
            army.started++;
            army.bearer.setAggroState(3); // passive: carries the flag, doesn't fight
            if (army.commander != null) {
                army.bearer.setProtectUUID(Optional.of(army.commander.getUUID()));
                army.bearer.setFollowState(5); // stays by the general
            }
        }
    }

    private static EntityType<? extends AbstractRecruitEntity> type(Arm arm) {
        return switch (arm) {
            case SWORDSMEN -> ModEntityTypes.RECRUIT.get();
            case SHIELDWALL -> ModEntityTypes.RECRUIT_SHIELDMAN.get();
            case ARCHERS -> ModEntityTypes.BOWMAN.get();
            case CAVALRY -> ModEntityTypes.HORSEMAN.get();
        };
    }

    @Nullable
    private AbstractRecruitEntity spawn(EntityType<? extends AbstractRecruitEntity> type, Army army, Role role, Point p, float yaw) {
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
        dress(soldier, army, role);
        soldier.setPersistenceRequired();
        soldier.setCanPickUpLoot(false); // keeps the uniform: no swapping it for armour off the dead
        soldier.setHunger(100);
        soldier.setMoral(100);
        soldier.setAggroState(0); // neutral: fights monsters and enemies of its faction, and whoever attacks it
        ours.add(soldier.getUUID());
        if (role == Role.GENERAL) {
            backBanners.put(soldier.getId(), army.banner.copy());
        }
        level.addFreshEntity(soldier);
        FactionEvents.addRecruitToTeam(soldier, army.team, level);
        spawned.add(soldier);
        return soldier;
    }

    /**
     * Full leather dyed the army's colour. The head shows rank: a leather cap for the ranks and the standard-bearer,
     * the faction banner for a formation's leader, a gold helmet for the general. The standard-bearer carries the
     * banner; the general's own is drawn on the back by the client.
     */
    private void dress(AbstractRecruitEntity soldier, Army army, Role role) {
        equip(soldier, EquipmentSlot.CHEST, dyed(Items.LEATHER_CHESTPLATE, army.uniform));
        equip(soldier, EquipmentSlot.LEGS, dyed(Items.LEATHER_LEGGINGS, army.uniform));
        equip(soldier, EquipmentSlot.FEET, dyed(Items.LEATHER_BOOTS, army.uniform));
        equip(soldier, EquipmentSlot.HEAD, switch (role) {
            case SOLDIER, BEARER -> dyed(Items.LEATHER_HELMET, army.uniform);
            case LEADER -> army.banner.copy();
            case GENERAL -> new ItemStack(Items.GOLDEN_HELMET);
        });
        if (role == Role.BEARER) {
            equip(soldier, EquipmentSlot.MAINHAND, army.banner.copy());
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

    /** A warrior general: Recruits' top level with every level's bonuses, at full health, with an iron sword. */
    private static void makeChampion(AbstractRecruitEntity soldier) {
        int max = RecruitsServerConfig.RecruitsMaxXpLevel.get();
        // Recruits grants a level's bonuses on reaching the next one, reading the level it is leaving.
        for (int level = soldier.getXpLevel(); level < max; level++) {
            soldier.setXpLevel(level);
            soldier.addLevelBuffs();
        }
        soldier.setXpLevel(max);
        soldier.setHealth(soldier.getMaxHealth());
        equip(soldier, EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    private BlockPos ground(Point p) {
        int x = (int) Math.floor(p.x());
        int z = (int) Math.floor(p.z());
        return new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
    }

    // ---- orders ----

    /** Orders the formation to stand in its line (Recruits' own line formation, held). */
    private void standInLine(Army army, Formation formation) {
        List<AbstractRecruitEntity> alive = new ArrayList<>(formation.alive());
        if (alive.isEmpty()) {
            return;
        }
        FormationUtils.lineFormation(new Vec3(army.facingX, 0, army.facingZ), alive, Vec3.atBottomCenterOf(ground(formation.line)),
                BattlePlan.FILE_WIDTH, BattlePlan.SPACING, true);
    }

    /** Orders one soldier to stand at a spot (keeping it if it is already ordered close to there). */
    private void hold(AbstractRecruitEntity soldier, Point spot) {
        Vec3 current = soldier.getHoldPos();
        if (current != null && Math.hypot(current.x - spot.x(), current.z - spot.z()) < 2) {
            return;
        }
        soldier.setHoldPos(Vec3.atBottomCenterOf(ground(spot)));
        soldier.setFollowState(3);
    }

    /** Releases the formation to fight freely; cavalry rides for the enemy's archers first, if any are left. */
    private void charge(Formation formation, Army enemy) {
        formation.charged = true;
        List<AbstractRecruitEntity> archers = enemy.formations.stream()
                .filter(f -> f.arm == Arm.ARCHERS).flatMap(f -> f.living.stream()).toList();
        for (AbstractRecruitEntity soldier : formation.alive()) {
            release(soldier, formation.arm == Arm.CAVALRY && !archers.isEmpty() ? archers : enemy.living);
        }
    }

    private static void release(AbstractRecruitEntity soldier, List<AbstractRecruitEntity> prey) {
        soldier.isInFormation = false;
        soldier.holdFormation = false;
        soldier.setFollowState(0);
        nearestTo(soldier, prey).ifPresent(soldier::setTarget);
    }

    /** Soldiers who lose sight of their foe stand idle; send them after the nearest enemy left. */
    private static void hunt(AbstractRecruitEntity soldier, List<AbstractRecruitEntity> foes) {
        LivingEntity target = soldier.getTarget();
        if (target == null || !target.isAlive()) {
            // Recruits drops a target it can't see, so also walk toward it until it comes into view.
            nearestTo(soldier, foes).ifPresent(foe -> {
                soldier.setTarget(foe);
                soldier.getNavigation().moveTo(foe, 1.0);
            });
        }
    }

    private static Optional<AbstractRecruitEntity> nearestTo(Entity soldier, List<AbstractRecruitEntity> candidates) {
        return candidates.stream().min(Comparator.comparingDouble(c -> c.distanceToSqr(soldier)));
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
        if (!battle.over) {
            battle.ticks++;
            if (battle.ticks % 20 == 0) {
                battle.second();
            }
        }
        // Survivors still stand (or run) after the battle is decided, so keep showing them until it is cleared.
        if (++battle.snapshotTicks % SNAPSHOT_TICKS == 0) {
            battle.holdChunks();
            ConquestNetwork.sendBattleSnapshot(battle.level.players(), battle.snapshots());
        }
        battle.ownNanos += System.nanoTime() - ownStart;
        if (battle.tickStart != 0) {
            long nanos = System.nanoTime() - battle.tickStart;
            battle.whole.add(nanos);
            battle.window.add(nanos);
        }
    }

    private void second() {
        int seconds = ticks / 20;
        for (Army army : armies) {
            army.count();
        }
        for (int side = 0; side < 2; side++) {
            if (lastAlive[side] != 0 && armies[side].living.size() < lastAlive[side]) {
                lastFall = seconds;
            }
            lastAlive[side] = armies[side].living.size();
        }
        if (!finalMelee && BattlePlan.stalled(seconds, lastFall)) {
            finalMelee = true;
            broadcast("[battle " + seconds + "s] the lines have stalled: everyone left charges");
            for (int side = 0; side < 2; side++) {
                for (Formation f : armies[side].formations) {
                    if (!f.living.isEmpty()) {
                        charge(f, armies[1 - side]);
                    }
                }
            }
        }
        for (int side = 0; side < 2; side++) {
            command(armies[side], armies[1 - side], seconds);
        }
        if (seconds % REPORT_SECONDS == 0) {
            broadcast(String.format(Locale.ROOT, "[battle %ds] %s %d alive (%d reliefs), %s %d alive (%d reliefs) | %s, of which battle orders %.1f ms/tick",
                    seconds, armies[0].faction.name(), armies[0].living.size(), armies[0].reliefs, armies[1].faction.name(),
                    armies[1].living.size(), armies[1].reliefs, window.describe(), window.count == 0 ? 0 : ownNanos / 1e6 / window.count));
            window.clear();
            ownNanos = 0;
        }
        checkEnd(seconds);
    }

    /** One second of an army's general giving orders. */
    private void command(Army army, Army enemy, int seconds) {
        List<AbstractRecruitEntity> foes = enemy.living;
        boolean engaged = army.infantryEngaged();
        relieve(army, seconds);
        for (Formation formation : army.formations) {
            if (formation.arm == Arm.CAVALRY) {
                armourHorses(army, formation);
            }
            if (formation.living.isEmpty()) {
                continue;
            }
            if (formation.charged) {
                formation.living.forEach(s -> hunt(s, foes));
                continue;
            }
            switch (formation.duty) {
                case FRONT -> holdFront(army, formation, foes, seconds, engaged);
                case LINE -> keepPlaceInFile(army, formation);
                case RELIEVING -> {
                    if (formation.centre().distanceTo(formation.line) <= BattlePlan.RELIEF_ARRIVED
                            || seconds - formation.dutySince >= BattlePlan.RELIEF_SECONDS) {
                        takeOver(army, formation, seconds);
                    }
                }
                case WITHDRAWING -> {
                    if (formation.rear == null || formation.centre().distanceTo(formation.rear) <= BattlePlan.RELIEF_ARRIVED
                            || seconds - formation.dutySince >= BattlePlan.RELIEF_SECONDS) {
                        rest(army, formation);
                    }
                }
                case RESTING -> {
                    // Stands behind the lines; its soldiers fight whoever comes within reach.
                }
                case RESERVE -> moveTo(army, formation,
                        BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, formation.slotRight, formation.slotBack));
                case SUPPORT -> support(army, enemy, formation, foes, seconds, engaged);
            }
        }
        commandGroup(army, enemy, foes, engaged);
    }

    /**
     * A block holding its file of the front: marches with the line until the enemy is close, then stands and fights
     * in line (its soldiers take on whoever comes within reach and return to their places), pressing on when the
     * enemy in front of it is gone.
     */
    private void holdFront(Army army, Formation formation, List<AbstractRecruitEntity> foes, int seconds, boolean engaged) {
        double nearest = nearest(formation.line, foes);
        switch (BattlePlan.order(army.general, formation.arm, seconds, nearest, engaged)) {
            case MARCH -> {
                formation.engaged = false;
                formation.line = formation.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                standInLine(army, formation);
            }
            case ENGAGE -> formation.engaged = true;
            default -> {
            }
        }
    }

    /**
     * Sends a fresh block up to each file of the front whose block is down to half strength (or gone): from that
     * file's queue if it has a fit block, else the nearest fit block from any file. A tactician commits the reserve
     * when the lines run out of fit blocks, or the army is badly worn.
     */
    private void relieve(Army army, int seconds) {
        boolean tactician = army.general == General.TACTICIAN;
        if (tactician && !army.reserveCommitted && BattlePlan.commitReserve(army.living.size(), army.started, false, true)) {
            commitReserve(army);
        }
        for (int file = 0; file < army.front.length; file++) {
            Formation held = army.front[file];
            if (held.relief != null && !held.relief.living.isEmpty() && held.relief.duty == Duty.RELIEVING) {
                continue; // already on its way
            }
            held.relief = null;
            if (!BattlePlan.needsRelief(held.living.size(), held.soldiers.size())) {
                continue;
            }
            Formation relief = pickRelief(army, file, held.line);
            if (relief == null && tactician && !army.reserveCommitted && BattlePlan.commitReserve(army.living.size(), army.started, true, false)) {
                commitReserve(army);
                relief = pickRelief(army, file, held.line);
            }
            if (relief == null && held.living.isEmpty()) {
                relief = lastStand(army, held.line);
            }
            if (relief == null) {
                continue; // nobody left to send: the block fights on
            }
            relief.duty = Duty.RELIEVING;
            relief.file = file;
            relief.dutySince = seconds;
            relief.line = held.line;
            standInLine(army, relief);
            held.relief = relief;
        }
    }

    @Nullable
    private static Formation pickRelief(Army army, int file, Point spot) {
        for (Formation f : army.queue(file)) {
            if (BattlePlan.fitToRelieve(f.living.size(), f.soldiers.size())) {
                return f;
            }
        }
        return army.formations.stream()
                .filter(f -> f.duty == Duty.LINE && BattlePlan.fitToRelieve(f.living.size(), f.soldiers.size()))
                .min(Comparator.comparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    /**
     * A front file whose block has fallen, with no fit block left to send: any block still in the lines goes, worn or
     * not, else the nearest resting block, so the line doesn't stay open.
     */
    @Nullable
    private static Formation lastStand(Army army, Point spot) {
        return army.formations.stream()
                .filter(f -> !f.living.isEmpty() && (f.duty == Duty.LINE || f.duty == Duty.RESTING))
                .min(Comparator.comparingInt((Formation f) -> f.duty == Duty.LINE ? 0 : 1)
                        .thenComparingDouble(f -> f.line.distanceTo(spot))).orElse(null);
    }

    /** A relief has reached the front: it takes over the file, and the block it relieves withdraws. */
    private void takeOver(Army army, Formation relief, int seconds) {
        Formation relieved = army.front[relief.file];
        army.reliefs++;
        army.front[relief.file] = relief;
        relief.duty = Duty.FRONT;
        relief.engaged = false;
        relief.dutySince = seconds;
        relieved.relief = null;
        relieved.engaged = false;
        if (relieved.living.isEmpty()) {
            relieved.duty = Duty.RESTING;
            return;
        }
        // Back through the file's queue to behind its last block, not fighting on the way.
        relieved.duty = Duty.WITHDRAWING;
        relieved.dutySince = seconds;
        int behind = army.queue(relieved.file).size() + 1;
        relieved.rear = BattlePlan.place(relief.line, army.facingX, army.facingZ, 0, behind * BattlePlan.LINE_GAP + BattlePlan.LINE_GAP);
        relieved.line = relieved.rear;
        for (AbstractRecruitEntity soldier : relieved.living) {
            soldier.setTarget(null);
            soldier.setAggroState(3); // passive
        }
        standInLine(army, relieved);
    }

    /** A withdrawn block is back behind the lines: it rejoins its file's queue if still fit, else stands at the rear. */
    private void rest(Army army, Formation formation) {
        for (AbstractRecruitEntity soldier : formation.living) {
            soldier.setAggroState(0);
        }
        if (BattlePlan.fitToRelieve(formation.living.size(), formation.soldiers.size())) {
            army.join(formation, formation.file);
        } else {
            formation.duty = Duty.RESTING;
        }
    }

    /** A block waiting in its file's queue keeps its place: a line gap behind the one in front of it. */
    private void keepPlaceInFile(Army army, Formation formation) {
        int place = army.queue(formation.file).indexOf(formation) + 1;
        Point spot = BattlePlan.place(army.front[formation.file].line, army.facingX, army.facingZ, 0, place * BattlePlan.LINE_GAP);
        moveTo(army, formation, spot);
    }

    /** The reserve joins the lines: each block at the back of the file with the fewest blocks waiting. */
    private void commitReserve(Army army) {
        army.reserveCommitted = true;
        for (Formation f : army.formations) {
            if (f.duty == Duty.RESERVE && !f.living.isEmpty()) {
                int shortest = 0;
                for (int file = 1; file < army.front.length; file++) {
                    if (army.queue(file).size() < army.queue(shortest).size()) {
                        shortest = file;
                    }
                }
                army.join(f, shortest);
            }
        }
        broadcast(army.faction.name() + "'s general commits the reserve");
    }

    /** Archers and cavalry: their own orders. */
    private void support(Army army, Army enemy, Formation formation, List<AbstractRecruitEntity> foes, int seconds, boolean engaged) {
        if (formation.route != null) {
            ride(army, enemy, formation, seconds);
            return;
        }
        double nearest = nearest(formation.line, foes);
        switch (BattlePlan.order(army.general, formation.arm, seconds, nearest, engaged)) {
            case MARCH -> {
                if (formation.arm != Arm.ARCHERS || BattlePlan.archersMayAdvance(behindFront(army, formation.line))) {
                    formation.line = formation.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                    standInLine(army, formation);
                }
            }
            case FLANK -> {
                int side = formation.slotRight >= 0 ? 1 : -1;
                formation.route = BattlePlan.flankRoute(centreOf(foes), army.facingX, army.facingZ, side, enemy.halfWidth);
                formation.routeIndex = 0;
                formation.routeStarted = seconds;
                ride(army, enemy, formation, seconds);
            }
            case CHARGE -> charge(formation, enemy);
            default -> {
            }
        }
    }

    /** How far behind its army's front line a point is (negative: ahead of it). */
    private static double behindFront(Army army, Point at) {
        Point front = army.frontCentre();
        return (front.x() - at.x()) * army.facingX + (front.z() - at.z()) * army.facingZ;
    }

    /** Moves a block's line to a spot, unless it is already ordered close to there. */
    private void moveTo(Army army, Formation formation, Point spot) {
        if (spot.distanceTo(formation.line) > 2) {
            formation.line = spot;
            standInLine(army, formation);
        }
    }

    private static double nearest(Point from, List<AbstractRecruitEntity> foes) {
        double best = Double.MAX_VALUE;
        for (AbstractRecruitEntity foe : foes) {
            double dx = foe.getX() - from.x();
            double dz = foe.getZ() - from.z();
            best = Math.min(best, dx * dx + dz * dz);
        }
        return Math.sqrt(best);
    }

    /** Moves a flanking formation along its route, charging from behind when it gets round (or takes too long). */
    private void ride(Army army, Army enemy, Formation formation, int seconds) {
        List<Point> route = formation.route;
        Point waypoint = route.get(formation.routeIndex);
        if (formation.centre().distanceTo(waypoint) <= BattlePlan.WAYPOINT_REACHED) {
            formation.routeIndex++;
        }
        if (formation.routeIndex >= route.size() || seconds - formation.routeStarted >= FLANK_SECONDS) {
            charge(formation, enemy);
            return;
        }
        Point next = route.get(formation.routeIndex);
        if (!next.equals(formation.line)) {
            formation.line = next;
            standInLine(army, formation);
        }
    }

    /**
     * The general and the standard-bearer: a warrior stands just behind the line and charges in with the infantry;
     * a tactician stays well back behind the front centre. The standard-bearer keeps to the general's side.
     */
    private void commandGroup(Army army, Army enemy, List<AbstractRecruitEntity> foes, boolean engaged) {
        AbstractRecruitEntity general = army.commander;
        if (general == null || !general.isAlive()) {
            return;
        }
        if (army.commanderCharged) {
            hunt(general, foes);
            return;
        }
        if (army.general == General.WARRIOR && engaged) {
            army.commanderCharged = true;
            release(general, enemy.living);
            broadcast(army.faction.name() + "'s warrior general charges");
            return;
        }
        hold(general, BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, 0, army.generalBack));
    }

    /** Recruits gives cavalry their horse on their first tick; dress each horse in the army's colour once. */
    private void armourHorses(Army army, Formation formation) {
        for (AbstractRecruitEntity rider : formation.alive()) {
            if (rider.getVehicle() instanceof Horse horse && ours.add(horse.getUUID())) {
                horse.getSlot(401).set(dyed(Items.LEATHER_HORSE_ARMOR, army.uniform)); // 401: the horse's armour slot
            }
        }
    }

    private static Point centreOf(List<AbstractRecruitEntity> soldiers) {
        return new Point(soldiers.stream().mapToDouble(Entity::getX).average().orElse(0),
                soldiers.stream().mapToDouble(Entity::getZ).average().orElse(0));
    }

    private void checkEnd(int seconds) {
        int aliveA = armies[0].living.size();
        int aliveB = armies[1].living.size();
        boolean routA = BattlePlan.routs(aliveA, armies[0].started);
        boolean routB = BattlePlan.routs(aliveB, armies[1].started);
        String result;
        if (routA && routB) {
            result = "both armies broke";
        } else if (routA || routB) {
            Army winner = routA ? armies[1] : armies[0];
            Army loser = routA ? armies[0] : armies[1];
            result = winner.describe() + " wins; " + loser.describe() + " routs";
            flee(loser, winner);
        } else if (seconds >= BattlePlan.TIME_LIMIT_SECONDS) {
            result = "draw (time limit)";
        } else {
            return;
        }
        over = true;
        for (Army army : armies) {
            army.living.forEach(s -> s.setTarget(null));
        }
        restoreRelations();
        broadcast(String.format(Locale.ROOT, "[battle over after %ds] %s. %s %d/%d left, %s %d/%d left | whole battle: %s",
                seconds, result, armies[0].faction.name(), aliveA, armies[0].started, armies[1].faction.name(), aliveB,
                armies[1].started, whole.describe()));
        for (Army army : armies) {
            boolean generalAlive = army.commander != null && army.commander.isAlive();
            boolean bannerAlive = army.bearer != null && army.bearer.isAlive();
            broadcast(String.format(Locale.ROOT, "  %s: general %s, standard %s", army.describe(),
                    generalAlive ? "survived" : "fell", bannerAlive ? "still flies" : "fell"));
        }
        broadcast("Use /pve debug battle clear to remove the survivors");
    }

    /** The routed army stops fighting and runs back the way it came. */
    private void flee(Army loser, Army winner) {
        for (AbstractRecruitEntity soldier : loser.living) {
            soldier.setAggroState(3); // passive
            BlockPos away = ground(new Point(soldier.getX() - loser.facingX * 60, soldier.getZ() - loser.facingZ * 60));
            soldier.setMovePos(away);
            soldier.setFollowState(0);
            soldier.setShouldMovePos(true);
        }
        winner.living.forEach(s -> s.setFollowState(2)); // winners hold where they stand
    }

    // ---- keeping the field loaded ----

    /**
     * Keeps the chunks around every living soldier loaded and running, wherever the players are, and lets go of the
     * rest. Without this, a player who flies off to watch from afar unloads the battle: its soldiers stop, and count
     * as fallen.
     */
    private void holdChunks() {
        Set<Long> wanted = new HashSet<>();
        for (AbstractRecruitEntity soldier : spawned) {
            if (!soldier.isAlive()) {
                continue;
            }
            ChunkPos at = soldier.chunkPosition();
            for (int dx = -HELD_RADIUS; dx <= HELD_RADIUS; dx++) {
                for (int dz = -HELD_RADIUS; dz <= HELD_RADIUS; dz++) {
                    wanted.add(ChunkPos.asLong(at.x + dx, at.z + dz));
                }
            }
        }
        for (long chunk : wanted) {
            if (heldChunks.add(chunk)) {
                ChunkPos pos = new ChunkPos(chunk);
                // Distance 2 makes the chunk itself entity-ticking, as /forceload does; forcing ticks (a Forge addition)
                // lets its soldiers run with no player within simulation distance.
                level.getChunkSource().addRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
            }
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

    /**
     * Loads, and holds, the ground an army is about to deploy on, before anyone is spawned: a big army stands far
     * beyond what the players around keep loaded, and soldiers put down in unloaded ground would be lost.
     */
    private void loadGround(Army army, Point front, List<BattlePlan.Slot> slots) {
        double margin = BattlePlan.BLOCK_PITCH;
        Set<Long> chunks = new HashSet<>();
        for (BattlePlan.Slot slot : slots) {
            for (double dr = -margin; dr <= margin; dr += 8) {
                for (double db = -8; db <= 16; db += 8) {
                    Point p = BattlePlan.place(front, army.facingX, army.facingZ, slot.right() + dr, slot.back() + db);
                    chunks.add(ChunkPos.asLong(Mth.floor(p.x()) >> 4, Mth.floor(p.z()) >> 4));
                }
            }
        }
        double back = BattlePlan.generalBack(army.general, slots);
        Point general = BattlePlan.place(front, army.facingX, army.facingZ, 0, back);
        chunks.add(ChunkPos.asLong(Mth.floor(general.x()) >> 4, Mth.floor(general.z()) >> 4));
        for (long chunk : chunks) {
            if (heldChunks.add(chunk)) {
                ChunkPos pos = new ChunkPos(chunk);
                level.getChunkSource().addRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
            }
            level.getChunk(ChunkPos.getX(chunk), ChunkPos.getZ(chunk)); // loads it now, not when the ticket is processed
        }
    }

    private void releaseChunks() {
        for (long chunk : heldChunks) {
            ChunkPos pos = new ChunkPos(chunk);
            level.getChunkSource().removeRegionTicket(BATTLE_TICKET, pos, 2, pos, true);
        }
        heldChunks.clear();
    }

    // ---- distant view ----

    /**
     * Every formation still standing, for clients to draw beyond the range at which they see soldiers. Ids are the
     * side times {@link #IDS_PER_ARMY} plus the formation's place, with the general and the standard-bearer last.
     */
    private List<FormationSnapshot> snapshots() {
        List<FormationSnapshot> all = new ArrayList<>();
        for (Army army : armies) {
            army.count(); // also after the battle is decided, when orders (and counting) stop
        }
        for (int side = 0; side < 2; side++) {
            Army army = armies[side];
            int base = side * IDS_PER_ARMY;
            for (int i = 0; i < army.formations.size(); i++) {
                Formation f = army.formations.get(i);
                snapshot(base + i, FormationSnapshot.Kind.of(f.arm), army, f.alive(), f.charged || f.engaged, f.leader).ifPresent(all::add);
            }
            soloSnapshot(base + IDS_PER_ARMY - 2, FormationSnapshot.Kind.GENERAL, army, army.commander, army.commanderCharged).ifPresent(all::add);
            soloSnapshot(base + IDS_PER_ARMY - 1, FormationSnapshot.Kind.BEARER, army, army.bearer, false).ifPresent(all::add);
        }
        return all;
    }

    private static Optional<FormationSnapshot> soloSnapshot(int id, FormationSnapshot.Kind kind, Army army,
                                                            @Nullable AbstractRecruitEntity soldier, boolean fighting) {
        return soldier == null || !soldier.isAlive() ? Optional.empty() : snapshot(id, kind, army, List.of(soldier), fighting, null);
    }

    private static Optional<FormationSnapshot> snapshot(int id, FormationSnapshot.Kind kind, Army army,
                                                        List<AbstractRecruitEntity> soldiers, boolean fighting,
                                                        @Nullable AbstractRecruitEntity leader) {
        if (soldiers.isEmpty()) {
            return Optional.empty();
        }
        int n = soldiers.size();
        double[] xs = new double[n];
        double[] ys = new double[n];
        double[] zs = new double[n];
        int[] ids = new int[n];
        for (int i = 0; i < n; i++) {
            AbstractRecruitEntity s = soldiers.get(i);
            // A rider stands on the ground where their horse does, not up in the saddle.
            Entity feet = s.getVehicle() != null ? s.getVehicle() : s;
            xs[i] = feet.getX();
            ys[i] = feet.getY();
            zs[i] = feet.getZ();
            ids[i] = s.getId();
        }
        boolean leads = leader != null && leader.isAlive();
        return Optional.of(FormationSnapshot.of(id, kind, army.uniform, army.mark, xs, ys, zs, army.facingX, army.facingZ, fighting,
                leads, ids, leads ? leader.getId() : -1));
    }

    /** Tells everyone in the level that the battle's formations are gone. */
    private void clearSnapshots() {
        ConquestNetwork.sendBattleSnapshot(level.players(), List.of());
    }

    /** To every player; the server log gets it too. */
    private void broadcast(String line) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(line), false);
    }
}
