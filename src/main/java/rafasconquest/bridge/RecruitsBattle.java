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
    private final TickTimes whole = new TickTimes();
    private final TickTimes window = new TickTimes();
    private long tickStart;
    private int snapshotTicks;

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
        @Nullable
        AbstractRecruitEntity commander;
        @Nullable
        AbstractRecruitEntity bearer;
        boolean commanderCharged;
        int started;

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

        List<AbstractRecruitEntity> alive() {
            List<AbstractRecruitEntity> all = new ArrayList<>();
            formations.forEach(f -> all.addAll(f.alive()));
            for (AbstractRecruitEntity e : new AbstractRecruitEntity[]{commander, bearer}) {
                if (e != null && e.isAlive()) {
                    all.add(e);
                }
            }
            return all;
        }

        boolean infantryEngaged() {
            return formations.stream().anyMatch(f -> f.arm.infantry() && f.charged);
        }

        /** The middle of the front line: the average of its (non-reserve) infantry formations' lines. */
        Point frontCentre() {
            double x = 0;
            double z = 0;
            int n = 0;
            for (Formation f : formations) {
                if (f.arm.infantry() && !f.reserve) {
                    x += f.line.x();
                    z += f.line.z();
                    n++;
                }
            }
            return n == 0 ? formations.get(0).line : new Point(x / n, z / n);
        }

        String describe() {
            return faction.name() + " (" + general.label() + ")";
        }
    }

    private static final class Formation {
        final Arm arm;
        final boolean reserve;
        final List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        @Nullable
        AbstractRecruitEntity leader;
        /** The centre of its front rank, where its line is ordered to stand. */
        Point line;
        boolean charged;
        /** A reserve the general has sent into the fight. */
        boolean committed;
        /** A flanking ride's waypoints, once it has set off, and when it did. */
        @Nullable
        List<Point> route;
        int routeIndex;
        int routeStarted;

        Formation(BattlePlan.Slot slot, Point line) {
            this.arm = slot.arm();
            this.reserve = slot.reserve();
            this.line = line;
        }

        List<AbstractRecruitEntity> alive() {
            return soldiers.stream().filter(LivingEntity::isAlive).toList();
        }

        Point centre() {
            List<AbstractRecruitEntity> alive = alive();
            if (alive.isEmpty()) {
                return line;
            }
            return new Point(alive.stream().mapToDouble(Entity::getX).average().orElse(line.x()),
                    alive.stream().mapToDouble(Entity::getZ).average().orElse(line.z()));
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
     * warrior or a tactician, 50/50. Returns a line for the caller, or throws if Recruits isn't ready.
     */
    public static String start(ServerLevel level, Faction first, Faction second, Point centre, double axisX, double axisZ) {
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
        battle.deploy(a, fronts[0]);
        battle.deploy(b, fronts[1]);
        String line = String.format(Locale.ROOT, "Battle: %s, %d soldiers, against %s, %d soldiers, at %d, %d", a.describe(),
                a.started, b.describe(), b.started, Math.round(centre.x()), Math.round(centre.z()));
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

    private void deploy(Army army, Point front) {
        float yaw = (float) Math.toDegrees(Math.atan2(-army.facingX, army.facingZ));
        for (BattlePlan.Slot slot : BattlePlan.deployment(army.general)) {
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
            standInLine(army, formation);
        }

        double back = BattlePlan.generalBack(army.general);
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
                .filter(f -> f.arm == Arm.ARCHERS).flatMap(f -> f.alive().stream()).toList();
        for (AbstractRecruitEntity soldier : formation.alive()) {
            release(soldier, formation.arm == Arm.CAVALRY && !archers.isEmpty() ? archers : enemy.alive());
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
        if (!battle.over) {
            battle.ticks++;
            if (battle.ticks % 20 == 0) {
                battle.second();
            }
        }
        // Survivors still stand (or run) after the battle is decided, so keep showing them until it is cleared.
        if (++battle.snapshotTicks % SNAPSHOT_TICKS == 0) {
            ConquestNetwork.sendBattleSnapshot(battle.level.players(), battle.snapshots());
        }
        if (battle.tickStart != 0) {
            long nanos = System.nanoTime() - battle.tickStart;
            battle.whole.add(nanos);
            battle.window.add(nanos);
        }
    }

    private void second() {
        int seconds = ticks / 20;
        for (int side = 0; side < 2; side++) {
            command(armies[side], armies[1 - side], seconds);
        }
        if (seconds % REPORT_SECONDS == 0) {
            broadcast(String.format(Locale.ROOT, "[battle %ds] %s %d alive, %s %d alive | %s", seconds,
                    armies[0].faction.name(), armies[0].alive().size(), armies[1].faction.name(), armies[1].alive().size(), window.describe()));
            window.clear();
        }
        checkEnd(seconds);
    }

    /** One second of an army's general giving orders. */
    private void command(Army army, Army enemy, int seconds) {
        List<AbstractRecruitEntity> foes = enemy.alive();
        boolean engaged = army.infantryEngaged();
        if (army.general == General.TACTICIAN) {
            commitReserve(army);
        }
        for (Formation formation : army.formations) {
            if (formation.arm == Arm.CAVALRY) {
                armourHorses(army, formation);
            }
            if (formation.charged) {
                formation.alive().forEach(s -> hunt(s, foes));
                continue;
            }
            if (formation.alive().isEmpty()) {
                continue;
            }
            double nearest = foes.stream()
                    .mapToDouble(e -> Math.hypot(e.getX() - formation.line.x(), e.getZ() - formation.line.z()))
                    .min().orElse(Double.MAX_VALUE);
            if (formation.route != null) {
                ride(army, enemy, formation, seconds);
                continue;
            }
            if (formation.reserve && !formation.committed) {
                // Waits behind the front centre, moving up with it; fights only if the enemy reaches it.
                if (nearest <= BattlePlan.INFANTRY_ENGAGE) {
                    charge(formation, enemy);
                } else {
                    Point spot = BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, 0, 24);
                    if (spot.distanceTo(formation.line) > 2) {
                        formation.line = spot;
                        standInLine(army, formation);
                    }
                }
                continue;
            }
            switch (BattlePlan.order(army.general, formation.arm, seconds, nearest, engaged)) {
                case HOLD -> {
                }
                case MARCH -> {
                    formation.line = formation.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                    standInLine(army, formation);
                }
                case FLANK -> {
                    Point enemyCentre = centreOf(foes);
                    formation.route = BattlePlan.flankRoute(enemyCentre, army.facingX, army.facingZ);
                    formation.routeIndex = 0;
                    formation.routeStarted = seconds;
                    ride(army, enemy, formation, seconds);
                }
                case CHARGE -> charge(formation, enemy);
            }
        }
        commandGroup(army, enemy, foes, engaged);
    }

    /** A tactician sends the reserve to the weakest fighting infantry formation once it is badly hurt. */
    private void commitReserve(Army army) {
        Formation reserve = army.formations.stream().filter(f -> f.reserve && !f.committed && !f.charged).findFirst().orElse(null);
        if (reserve == null || reserve.alive().isEmpty()) {
            return;
        }
        List<Formation> front = army.formations.stream().filter(f -> f.arm.infantry() && !f.reserve).toList();
        int[] alive = front.stream().mapToInt(f -> f.alive().size()).toArray();
        int[] started = front.stream().mapToInt(f -> f.soldiers.size()).toArray();
        boolean[] fighting = new boolean[front.size()];
        for (int i = 0; i < fighting.length; i++) {
            fighting[i] = front.get(i).charged;
        }
        int pick = BattlePlan.reserveToCommit(alive, started, fighting);
        if (pick >= 0) {
            reserve.committed = true;
            reserve.line = front.get(pick).centre();
            standInLine(army, reserve);
            broadcast(army.faction.name() + "'s general commits the reserve");
        }
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
            release(general, enemy.alive());
            broadcast(army.faction.name() + "'s warrior general charges");
            return;
        }
        hold(general, BattlePlan.place(army.frontCentre(), army.facingX, army.facingZ, 0, BattlePlan.generalBack(army.general)));
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
        int aliveA = armies[0].alive().size();
        int aliveB = armies[1].alive().size();
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
            army.alive().forEach(s -> s.setTarget(null));
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
        for (AbstractRecruitEntity soldier : loser.alive()) {
            soldier.setAggroState(3); // passive
            BlockPos away = ground(new Point(soldier.getX() - loser.facingX * 60, soldier.getZ() - loser.facingZ * 60));
            soldier.setMovePos(away);
            soldier.setFollowState(0);
            soldier.setShouldMovePos(true);
        }
        winner.alive().forEach(s -> s.setFollowState(2)); // winners hold where they stand
    }

    // ---- distant view ----

    /**
     * Every formation still standing, for clients to draw beyond the range at which they see soldiers. Ids are the
     * side times {@link #IDS_PER_ARMY} plus the formation's place, with the general and the standard-bearer last.
     */
    private List<FormationSnapshot> snapshots() {
        List<FormationSnapshot> all = new ArrayList<>();
        for (int side = 0; side < 2; side++) {
            Army army = armies[side];
            int base = side * IDS_PER_ARMY;
            for (int i = 0; i < army.formations.size(); i++) {
                Formation f = army.formations.get(i);
                boolean leader = f.leader != null && f.leader.isAlive();
                snapshot(base + i, FormationSnapshot.Kind.of(f.arm), army, f.alive(), f.charged, leader).ifPresent(all::add);
            }
            soloSnapshot(base + IDS_PER_ARMY - 2, FormationSnapshot.Kind.GENERAL, army, army.commander, army.commanderCharged).ifPresent(all::add);
            soloSnapshot(base + IDS_PER_ARMY - 1, FormationSnapshot.Kind.BEARER, army, army.bearer, false).ifPresent(all::add);
        }
        return all;
    }

    private static Optional<FormationSnapshot> soloSnapshot(int id, FormationSnapshot.Kind kind, Army army,
                                                            @Nullable AbstractRecruitEntity soldier, boolean fighting) {
        return soldier == null || !soldier.isAlive() ? Optional.empty() : snapshot(id, kind, army, List.of(soldier), fighting, false);
    }

    private static Optional<FormationSnapshot> snapshot(int id, FormationSnapshot.Kind kind, Army army,
                                                        List<AbstractRecruitEntity> soldiers, boolean fighting, boolean leader) {
        if (soldiers.isEmpty()) {
            return Optional.empty();
        }
        int n = soldiers.size();
        double[] xs = new double[n];
        double[] ys = new double[n];
        double[] zs = new double[n];
        for (int i = 0; i < n; i++) {
            AbstractRecruitEntity s = soldiers.get(i);
            // A rider stands on the ground where their horse does, not up in the saddle.
            Entity feet = s.getVehicle() != null ? s.getVehicle() : s;
            xs[i] = feet.getX();
            ys[i] = feet.getY();
            zs[i] = feet.getZ();
        }
        return Optional.of(FormationSnapshot.of(id, kind, army.uniform, army.mark, xs, ys, zs, army.facingX, army.facingZ, fighting, leader));
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
