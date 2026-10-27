package rafasconquest.battle;

import rafasconquest.battle.BattlePlan.Arm;

/**
 * The numbers of a battle fought without entities ({@link FieldBattle}): how fast blocks move, how far they reach,
 * and how much harm they do. Plain functions, so they can be tested alone.
 * <p>
 * The rates are first guesses, set so a block of 20 holding a line against another loses half its soldiers in about
 * a minute, roughly what live Recruits lines do. They are to be calibrated against live battles.
 */
public final class FieldRules {
    /** A soldier's health, and a warrior general's (Recruits' top level). */
    public static final float SOLDIER_HEALTH = 20;
    public static final float WARRIOR_HEALTH = 82;
    /** Damage dealt by one hit; a second's damage is dealt in hits this size, to random soldiers. */
    public static final float HIT = 4;

    /** Blocks a second: infantry and archers on foot, cavalry. */
    public static final double FOOT_SPEED = 3;
    public static final double HORSE_SPEED = 6;

    /** Half the width of a block's front rank, and of a lone soldier. */
    public static final double BLOCK_HALF_WIDTH = (BattlePlan.FILE_WIDTH - 1) * BattlePlan.SPACING / 2;
    public static final double LONE_HALF_WIDTH = 0.3;

    /**
     * How far apart two blocks' front ranks can be and still fight: a block holding its place reaches far (its
     * soldiers step out to whoever comes within reach and return), one that has broken formation must close in.
     */
    public static final double HOLDING_REACH = 12;
    public static final double CLOSE_REACH = 2.5;

    /** Damage a second dealt by one fighting soldier, by arm. */
    public static final double INFANTRY_DPS = 0.15;
    public static final double CAVALRY_DPS = 0.18;
    /** Archers in melee fight badly. */
    public static final double ARCHER_MELEE_DPS = 0.07;
    /** A warrior general fights like this many soldiers. */
    public static final double WARRIOR_FIGHTS_AS = 4;

    /** Archers shoot this far, this many arrows a second each, each arrow on average (misses included) doing this much. */
    public static final double ARCHER_RANGE = BattlePlan.ARCHER_RANGE;
    public static final double ARROWS_PER_SECOND = 0.5;
    public static final double ARROW_DAMAGE = 0.3;
    /** Arrows each archer carries (Recruits bowmen in battle get a stack). */
    public static final int ARROWS_EACH = 64;

    /** Cavalry that has just reached the enemy hits much harder for its first seconds. */
    public static final double CHARGE_BONUS = 3;
    public static final int CHARGE_SECONDS = 3;
    /** Attacks on a block's flank or rear do more. */
    public static final double FLANK_BONUS = 1.5;
    public static final double REAR_BONUS = 2;
    /** A shieldwall takes less from the front: in melee, and from arrows. */
    public static final double SHIELD_MELEE = 0.6;
    public static final double SHIELD_ARROWS = 0.4;

    private FieldRules() {
    }

    public static double speed(Arm arm) {
        return arm == Arm.CAVALRY ? HORSE_SPEED : FOOT_SPEED;
    }

    /** Melee damage a second for each fighting soldier of this arm. */
    public static double meleeDps(Arm arm) {
        return switch (arm) {
            case SWORDSMEN, SHIELDWALL -> INFANTRY_DPS;
            case CAVALRY -> CAVALRY_DPS;
            case ARCHERS -> ARCHER_MELEE_DPS;
        };
    }

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
        return switch (side) {
            case FRONT -> defender == Arm.SHIELDWALL ? SHIELD_MELEE : 1;
            case FLANK -> FLANK_BONUS;
            case REAR -> REAR_BONUS;
        };
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
