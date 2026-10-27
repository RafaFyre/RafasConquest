package rafasconquest.bridge;

import com.talhanation.recruits.FactionEvents;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.BowmanEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import com.talhanation.recruits.util.FormationUtils;
import com.talhanation.recruits.world.RecruitsDiplomacyManager;
import com.talhanation.recruits.world.RecruitsDiplomacyManager.DiplomacyStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.faction.Faction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.UUID;

/**
 * A live battle between two factions' armies of Recruits soldiers, for the prototype: each army deploys as
 * {@link BattlePlan#DEPLOYMENT} and a commander gives each formation an order every second ({@link BattlePlan#order}).
 * The two factions are made enemies in Recruits' diplomacy for the battle (so their soldiers attack each other and
 * nobody else; a bystander is left alone unless they strike first) and their old relation is restored afterwards.
 * Also times every server tick while it runs. One battle at a time; server thread only.
 */
public final class RecruitsBattle {
    private static final int REPORT_SECONDS = 10;

    @Nullable
    private static RecruitsBattle current;

    private final ServerLevel level;
    private final Army[] armies;
    private final DiplomacyStatus[] oldRelations = new DiplomacyStatus[2];
    private final List<AbstractRecruitEntity> spawned = new ArrayList<>();
    private int ticks;
    private boolean over;
    private final TickTimes whole = new TickTimes();
    private final TickTimes window = new TickTimes();
    private long tickStart;

    private RecruitsBattle(ServerLevel level, Army[] armies) {
        this.level = level;
        this.armies = armies;
    }

    private static final class Army {
        final Faction faction;
        final PlayerTeam team;
        final double facingX;
        final double facingZ;
        final List<Formation> formations = new ArrayList<>();
        int started;

        Army(Faction faction, PlayerTeam team, double facingX, double facingZ) {
            this.faction = faction;
            this.team = team;
            this.facingX = facingX;
            this.facingZ = facingZ;
        }

        List<AbstractRecruitEntity> alive() {
            List<AbstractRecruitEntity> all = new ArrayList<>();
            formations.forEach(f -> all.addAll(f.alive()));
            return all;
        }

        boolean infantryEngaged() {
            return formations.stream().anyMatch(f -> f.arm.infantry() && f.charged);
        }
    }

    private static final class Formation {
        final Arm arm;
        final List<AbstractRecruitEntity> soldiers = new ArrayList<>();
        /** The centre of its front rank, where its line is ordered to stand. */
        Point line;
        boolean charged;

        Formation(Arm arm, Point line) {
            this.arm = arm;
            this.line = line;
        }

        List<AbstractRecruitEntity> alive() {
            return soldiers.stream().filter(LivingEntity::isAlive).toList();
        }
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

    /**
     * Deploys both armies around {@code centre}, facing each other along ({@code axisX}, {@code axisZ}) (a unit
     * vector from {@code first}'s side toward {@code second}'s), and starts the battle. Returns a line for the caller,
     * or throws if Recruits isn't ready.
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
        Army a = new Army(first, teamA, axisX, axisZ);
        Army b = new Army(second, teamB, -axisX, -axisZ);
        RecruitsBattle battle = new RecruitsBattle(level, new Army[]{a, b});
        battle.oldRelations[0] = diplomacy.getRelation(teamA.getName(), teamB.getName());
        battle.oldRelations[1] = diplomacy.getRelation(teamB.getName(), teamA.getName());
        diplomacy.setRelation(teamA.getName(), teamB.getName(), DiplomacyStatus.ENEMY, level, false);
        diplomacy.setRelation(teamB.getName(), teamA.getName(), DiplomacyStatus.ENEMY, level, false);

        Point[] fronts = BattlePlan.fronts(centre, axisX, axisZ);
        battle.deploy(a, fronts[0]);
        battle.deploy(b, fronts[1]);
        current = battle;
        String line = String.format(Locale.ROOT, "Battle: %s (%d) against %s (%d) at %d, %d", first.name(), a.started,
                second.name(), b.started, Math.round(centre.x()), Math.round(centre.z()));
        RafasConquest.LOGGER.info("[battle] {}", line);
        return line;
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

    private void deploy(Army army, Point front) {
        float yaw = (float) Math.toDegrees(Math.atan2(-army.facingX, army.facingZ));
        for (BattlePlan.Slot slot : BattlePlan.DEPLOYMENT) {
            Point line = BattlePlan.place(front, army.facingX, army.facingZ, slot.right(), slot.back());
            Formation formation = new Formation(slot.arm(), line);
            for (int i = 0; i < BattlePlan.SOLDIERS_PER_FORMATION; i++) {
                // Stand them roughly where their place in the line will be, so the first order is a short shuffle.
                int rank = i / BattlePlan.FILE_WIDTH;
                double right = slot.right() + (i % BattlePlan.FILE_WIDTH - (BattlePlan.FILE_WIDTH - 1) / 2.0) * BattlePlan.SPACING;
                Point p = BattlePlan.place(front, army.facingX, army.facingZ, right, slot.back() + rank * BattlePlan.SPACING * 1.75);
                AbstractRecruitEntity soldier = spawn(slot.arm(), army.team, p, yaw);
                if (soldier != null) {
                    formation.soldiers.add(soldier);
                }
            }
            army.started += formation.soldiers.size();
            army.formations.add(formation);
            standInLine(army, formation);
        }
    }

    @Nullable
    private AbstractRecruitEntity spawn(Arm arm, PlayerTeam team, Point p, float yaw) {
        EntityType<? extends AbstractRecruitEntity> type = switch (arm) {
            case SWORDSMEN -> ModEntityTypes.RECRUIT.get();
            case SHIELDWALL -> ModEntityTypes.RECRUIT_SHIELDMAN.get();
            case ARCHERS -> ModEntityTypes.BOWMAN.get();
            case CAVALRY -> ModEntityTypes.HORSEMAN.get();
        };
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
        soldier.setPersistenceRequired();
        soldier.setHunger(100);
        soldier.setMoral(100);
        soldier.setAggroState(0); // neutral: fights monsters and enemies of its faction, and whoever attacks it
        level.addFreshEntity(soldier);
        FactionEvents.addRecruitToTeam(soldier, team, level);
        spawned.add(soldier);
        return soldier;
    }

    private BlockPos ground(Point p) {
        int x = (int) Math.floor(p.x());
        int z = (int) Math.floor(p.z());
        return new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
    }

    /** Orders the formation to stand in its line (Recruits' own line formation, held). */
    private void standInLine(Army army, Formation formation) {
        List<AbstractRecruitEntity> alive = new ArrayList<>(formation.alive());
        if (alive.isEmpty()) {
            return;
        }
        FormationUtils.lineFormation(new Vec3(army.facingX, 0, army.facingZ), alive, Vec3.atBottomCenterOf(ground(formation.line)),
                BattlePlan.FILE_WIDTH, BattlePlan.SPACING, true);
    }

    /** Releases the formation to fight freely; cavalry rides for the enemy's archers first, if any are left. */
    private void charge(Formation formation, Army enemy) {
        formation.charged = true;
        List<AbstractRecruitEntity> archers = enemy.formations.stream()
                .filter(f -> f.arm == Arm.ARCHERS).flatMap(f -> f.alive().stream()).toList();
        for (AbstractRecruitEntity soldier : formation.alive()) {
            soldier.isInFormation = false;
            soldier.holdFormation = false;
            soldier.setFollowState(0);
            nearestTo(soldier, formation.arm == Arm.CAVALRY && !archers.isEmpty() ? archers : enemy.alive()).ifPresent(soldier::setTarget);
        }
    }

    private static Optional<AbstractRecruitEntity> nearestTo(AbstractRecruitEntity soldier, List<AbstractRecruitEntity> candidates) {
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
        if (battle.tickStart != 0) {
            long nanos = System.nanoTime() - battle.tickStart;
            battle.whole.add(nanos);
            battle.window.add(nanos);
        }
    }

    private void second() {
        int seconds = ticks / 20;
        for (int side = 0; side < 2; side++) {
            Army army = armies[side];
            Army enemy = armies[1 - side];
            List<AbstractRecruitEntity> foes = enemy.alive();
            for (Formation formation : army.formations) {
                if (formation.charged) {
                    // Soldiers who lose sight of their foe stand idle; send them after the nearest enemy left.
                    for (AbstractRecruitEntity soldier : formation.alive()) {
                        LivingEntity target = soldier.getTarget();
                        if (target == null || !target.isAlive()) {
                            // Recruits drops a target it can't see, so also walk toward it until it comes into view.
                            nearestTo(soldier, foes).ifPresent(foe -> {
                                soldier.setTarget(foe);
                                soldier.getNavigation().moveTo(foe, 1.0);
                            });
                        }
                    }
                    continue;
                }
                if (formation.alive().isEmpty()) {
                    continue;
                }
                double nearest = foes.stream()
                        .mapToDouble(e -> Math.hypot(e.getX() - formation.line.x(), e.getZ() - formation.line.z()))
                        .min().orElse(Double.MAX_VALUE);
                switch (BattlePlan.order(formation.arm, seconds, nearest, army.infantryEngaged())) {
                    case HOLD -> {
                    }
                    case MARCH -> {
                        formation.line = formation.line.plus(army.facingX * BattlePlan.MARCH_STEP, army.facingZ * BattlePlan.MARCH_STEP);
                        standInLine(army, formation);
                    }
                    case CHARGE -> charge(formation, enemy);
                }
            }
        }
        if (seconds % REPORT_SECONDS == 0) {
            broadcast(String.format(Locale.ROOT, "[battle %ds] %s %d alive, %s %d alive | %s", seconds,
                    armies[0].faction.name(), armies[0].alive().size(), armies[1].faction.name(), armies[1].alive().size(), window.describe()));
            window.clear();
        }
        checkEnd(seconds);
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
            result = winner.faction.name() + " wins; " + loser.faction.name() + " routs";
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

    /** To every player; the server log gets it too. */
    private void broadcast(String line) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(line), false);
    }
}
