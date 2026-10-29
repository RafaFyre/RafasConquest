package rafasconquest.battle;

import rafasconquest.battle.BattlePlan.Arm;

/**
 * The numbers of a battle fought without entities ({@link FieldBattle}): how fast blocks move, how far they reach,
 * and how much harm they do. Plain functions, so they can be tested alone.
 * <p>
 * The rates are calibrated against live Recruits battles ({@link Calibration}): six battles of 100 to 500 a side
 * measured the damage each kind of soldier does a second, riding speed and starting health, and the rates were then
 * tuned so whole battles last about as long as the live ones.
 */
public final class FieldRules {
    /** A soldier's health, and a warrior general's (Recruits' top level). */
    public static final float SOLDIER_HEALTH = 21;
    public static final float WARRIOR_HEALTH = 82;
    /** Damage dealt by one hit; a second's damage is dealt in hits this size, to random soldiers. */
    public static final float HIT = 4;

    /** Blocks a second: infantry and archers on foot (not yet measured well), cavalry (measured). */
    public static final double FOOT_SPEED = 3;
    public static final double HORSE_SPEED = 7;

    /** Half the width of a block's front rank, and of a lone soldier. */
    public static final double BLOCK_HALF_WIDTH = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING / 2;
    public static final double LONE_HALF_WIDTH = 0.3;

    /**
     * How far apart two blocks' front ranks can be and still fight: a block holding its place reaches far (its
     * soldiers step out to whoever comes within reach and return), one that has broken formation must close in.
     */
    public static final double HOLDING_REACH = 12;
    /** Orders weigh how near the nearest enemy is up to this far (the furthest any order looks: archers' range). */
    public static final double ORDERS_REACH = 50;
    public static final double CLOSE_REACH = 2.5;

    /** Damage a second dealt by one fighting soldier, by arm. */
    public static final double INFANTRY_DPS = 0.12;
    public static final double CAVALRY_DPS = 0.36;
    /** Archers in melee fight badly. */
    public static final double ARCHER_MELEE_DPS = 0.088;
    /** A warrior general fights like this many soldiers. */
    public static final double WARRIOR_FIGHTS_AS = 4;

    /**
     * Archers shoot this far (Recruits bowmen: 44, though they halt at {@link BattlePlan#ARCHER_RANGE}), about this many
     * arrows a second each, each arrow on average (misses included) doing this much.
     */
    public static final double ARCHER_RANGE = 44;
    public static final double ARROWS_PER_SECOND = 1;
    public static final double ARROW_DAMAGE = 0.19;
    /** Arrows each archer carries (Recruits bowmen in battle get a stack). */
    public static final int ARROWS_EACH = 64;

    /**
     * Cavalry momentum, 0 to 1: gathered riding (full after {@link #RUN_UP} blocks), spent cutting into the enemy. A
     * charge does 1 + {@link #CHARGE_BONUS} x momentum times as much. Each second in melee costs momentum, more against
     * cavalry (both lock up rather than riding through each other); riding into braced infantry costs all of it.
     * Cavalry that has spent it ({@link #SPENT_MOMENTUM}, {@link #SPENT_SECONDS} in melee) is recalled: it rides back
     * {@link #RALLY_DISTANCE} blocks without fighting, reforms for {@link #REFORM_SECONDS}, and charges again.
     */
    public static final double CHARGE_BONUS = 2;
    public static final double RUN_UP = 20;
    public static final double MOMENTUM_LOSS_INFANTRY = 0.3;
    public static final double MOMENTUM_LOSS_CAVALRY = 0.6;
    public static final double SPENT_MOMENTUM = 0.15;
    public static final int SPENT_SECONDS = 2;
    public static final double RALLY_DISTANCE = 35;
    public static final int RALLY_SECONDS = 20;
    public static final int REFORM_SECONDS = 5;

    /**
     * Infantry bracing against charging cavalry: a block that sees enemy cavalry charging its way, within
     * {@link #WALL_ALERT} blocks, stops, turns to face it and closes up into a wall {@link #WALL_FILES} wide (if it isn't
     * already fighting infantry). Cavalry striking a braced wall from the front loses all momentum and does
     * {@link #BRACED_TAKES} of its damage; the wall does {@link #BRACED_DEALS} times as much back. A wall stands down
     * {@link #WALL_HOLD_SECONDS} after the last threat.
     */
    public static final double WALL_ALERT = 30;
    /** How directly the cavalry must be heading at a block to threaten it: the cosine of the angle off. */
    public static final double WALL_HEADING = 0.7;
    public static final int WALL_HOLD_SECONDS = 5;
    public static final int WALL_FILES = 5;
    public static final double WALL_SPACING = 1.3;
    public static final double BRACED_DEALS = 2.5;
    public static final double BRACED_TAKES = 0.4;
    /** Great Spear cavalry gathers up to twice the usual momentum (over twice the run-up). */
    public static final double GREAT_SPEAR_MOMENTUM = 2;
    /** A wedge forms once both armies' front lines have been fighting this long: the front line is established. */
    public static final int FRONTLINE_SECONDS = 5;
    /** A general leading the charge (Unrelenting Charge), and their guard, lose momentum at this share of the rate. */
    public static final double LEADER_MOMENTUM_LOSS = 0.5;
    /**
     * Unrelenting Charge: while the general leads the charge, their own soldiers within {@link #RESOLVE_REACH} of
     * them take only {@link #RESOLVE_TAKES} of the harm done to them.
     */
    public static final double RESOLVE_REACH = 15;
    public static final double RESOLVE_TAKES = 0.7;
    /** A general's guard rides out against enemies that come this close to the general... */
    public static final double GUARD_ALERT = 15;
    /** ...and out to this far at cavalry charging the general. */
    public static final double GUARD_INTERCEPT = 40;
    /**
     * Strategies' judgement: a block this far ahead of its own front line is too far forward (exposed, or drawn in by
     * bait); one down to {@link #WORN_SHARE} of its soldiers is worn. A general with none of their army but the guard
     * and standard within {@link #GENERAL_COVER} stands exposed.
     */
    public static final double TOO_FAR_FORWARD = 6;
    public static final double WORN_SHARE = 0.5;
    public static final double GENERAL_COVER = 15;
    /** A general keeps at least this far from any enemy, falling back behind their lines to stay clear. */
    public static final double GENERAL_SAFE = 25;
    /** Cavalry striking a flank first rides to a point this far out to the side of its target's line. */
    public static final double FLANK_RIDE_OUT = 15;
    /** Bait: runs this far ahead of its own line, and once fighting falls back to this far behind it. */
    public static final double BAIT_LEAD = 12;
    public static final double BAIT_POCKET = 10;
    /** Bait and Pincer: an enemy block this close to the bait, and too far forward, has been drawn in. */
    public static final double LURE_REACH = 20;
    /** A pincer gives up after this long. */
    public static final int PINCER_SECONDS = 40;
    /** The conveyor: a front block that has fought this long is rotated to the back of its file's stack. */
    public static final int ROTATE_SECONDS = 120;
    /**
     * Morale: an infantry block that loses {@link #ROUT_LOSS} of its soldiers within {@link #ROUT_WINDOW} seconds routs
     * (its captain's fall counts as {@link #CAPTAIN_SHOCK} more for as long), runs {@link #ROUT_RUN} behind its army's
     * reserves and regroups for {@link #REGROUP_SECONDS} before rejoining its file. The blocks that broke it, or killed
     * its captain, rally: for {@link #RALLIED_SECONDS} they can't rout.
     */
    public static final int ROUT_WINDOW = 60;
    public static final double ROUT_LOSS = 0.5;
    public static final double CAPTAIN_SHOCK = 0.25;
    public static final double ROUT_RUN = 30;
    public static final int REGROUP_SECONDS = 120;
    public static final int RALLIED_SECONDS = 45;
    /** Wing cavalry looks for exposed enemies this far around it. */
    public static final double WING_REACH = 80;
    /** Cavalry riding its backline goes this far each way of its post. */
    public static final double PATROL_REACH = 12;
    /**
     * Infantry against stopped cavalry: enemy cavalry that moved less than {@link #STOPPED} last second is set on by
     * up to {@link #HUNTERS_PER_RIDERS} idle infantry blocks within {@link #HUNT_REACH}, which let it go once it rides on.
     */
    public static final double STOPPED = 1;
    public static final double HUNT_REACH = 25;
    public static final int HUNTERS_PER_RIDERS = 2;
    /**
     * Envelopment: a front block with no enemy ahead wheels onto enemy infantry beside it within this reach, at most
     * this many onto each enemy block (its flank, then its rear).
     */
    public static final double ENVELOP_REACH = 60;
    public static final int ENVELOPERS = 2;
    /** A front block with fewer than this share of the soldiers of the block it fights gives ground this far a second. */
    public static final double GIVE_GROUND_SHARE = 0.7;
    public static final double GIVE_GROUND = 0.5;
    /**
     * How much more attacks on a block's flank or rear do: nothing more, as in Recruits, where a blow does the same
     * from any side (only a shield, held to the front, makes a difference).
     */
    public static final double FLANK_BONUS = 1;
    public static final double REAR_BONUS = 1;
    /** A shieldwall takes less from the front: in melee, and from arrows. */
    public static final double SHIELD_MELEE = 0.6;
    public static final double SHIELD_ARROWS = 0.4;

    private FieldRules() {
    }

    /**
     * The rates a battle is fought with, which calibration sets: damage a second of one fighting soldier by arm, of
     * one arrow, how much a charge multiplies cavalry damage for its first seconds, walking and riding speeds, and how
     * many soldiers a warrior general fights like.
     */
    public record Rates(double infantryDps, double cavalryDps, double archerMeleeDps, double arrowDamage, double chargeBonus,
                        double footSpeed, double horseSpeed, double warriorFightsAs, double flankBonus, double rearBonus) {
        public double meleeDps(Arm arm) {
            return switch (arm) {
                case SWORDSMEN, SHIELDWALL -> infantryDps;
                case CAVALRY -> cavalryDps;
                case ARCHERS -> archerMeleeDps;
            };
        }

        public double speed(Arm arm) {
            return arm == Arm.CAVALRY ? horseSpeed : footSpeed;
        }

        /** How much more (or less) a melee attack does, for where it falls and on whom. */
        public double meleeFactor(Side side, Arm defender) {
            return switch (side) {
                case FRONT -> defender == Arm.SHIELDWALL ? SHIELD_MELEE : 1;
                case FLANK -> flankBonus;
                case REAR -> rearBonus;
            };
        }
    }

    /** The rates battles are fought with. */
    public static final Rates RATES = new Rates(INFANTRY_DPS, CAVALRY_DPS, ARCHER_MELEE_DPS, ARROW_DAMAGE, CHARGE_BONUS,
            FOOT_SPEED, HORSE_SPEED, WARRIOR_FIGHTS_AS, FLANK_BONUS, REAR_BONUS);

    /** Where an attack falls on the defender: its front, a flank or its rear. */
    public enum Side {
        FRONT, FLANK, REAR
    }

    /**
     * Which side of a defender facing ({@code facingX}, {@code facingZ}) an attacker standing ({@code dx},
     * {@code dz}) from it strikes.
     */
    public static Side side(double facingX, double facingZ, double dx, double dz) {
        double length = Math.hypot(dx, dz);
        if (length < 1e-9) {
            return Side.FRONT;
        }
        double dot = (dx * facingX + dz * facingZ) / length;
        return dot >= 0.5 ? Side.FRONT : dot <= -0.5 ? Side.REAR : Side.FLANK;
    }

    /** How much more (or less) a melee attack does, for where it falls and on whom. */
    public static double meleeFactor(Side side, Arm defender) {
        return RATES.meleeFactor(side, defender);
    }

    /** How much of a volley of arrows tells, for where it falls and on whom. */
    public static double arrowFactor(Side side, Arm defender) {
        return side == Side.FRONT && defender == Arm.SHIELDWALL ? SHIELD_ARROWS : 1;
    }

    /**
     * The shortest distance between two segments, from ({@code ax1}, {@code az1}) to ({@code ax2}, {@code az2}) and
     * from ({@code bx1}, {@code bz1}) to ({@code bx2}, {@code bz2}): how far apart two blocks' front ranks are.
     */
    public static double segmentDistance(double ax1, double az1, double ax2, double az2,
                                         double bx1, double bz1, double bx2, double bz2) {
        if (segmentsCross(ax1, az1, ax2, az2, bx1, bz1, bx2, bz2)) {
            return 0;
        }
        return Math.min(Math.min(pointToSegment(ax1, az1, bx1, bz1, bx2, bz2), pointToSegment(ax2, az2, bx1, bz1, bx2, bz2)),
                Math.min(pointToSegment(bx1, bz1, ax1, az1, ax2, az2), pointToSegment(bx2, bz2, ax1, az1, ax2, az2)));
    }

    static double pointToSegment(double px, double pz, double x1, double z1, double x2, double z2) {
        double dx = x2 - x1;
        double dz = z2 - z1;
        double lengthSq = dx * dx + dz * dz;
        double t = lengthSq < 1e-12 ? 0 : Math.max(0, Math.min(1, ((px - x1) * dx + (pz - z1) * dz) / lengthSq));
        return Math.hypot(px - (x1 + t * dx), pz - (z1 + t * dz));
    }

    private static boolean segmentsCross(double ax1, double az1, double ax2, double az2,
                                         double bx1, double bz1, double bx2, double bz2) {
        double d1 = cross(bx1, bz1, bx2, bz2, ax1, az1);
        double d2 = cross(bx1, bz1, bx2, bz2, ax2, az2);
        double d3 = cross(ax1, az1, ax2, az2, bx1, bz1);
        double d4 = cross(ax1, az1, ax2, az2, bx2, bz2);
        return d1 * d2 < 0 && d3 * d4 < 0;
    }

    private static double cross(double x1, double z1, double x2, double z2, double px, double pz) {
        return (x2 - x1) * (pz - z1) - (z2 - z1) * (px - x1);
    }
}
