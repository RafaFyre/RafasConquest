package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;

import java.util.List;

/**
 * One block of a {@link FieldBattle}: 20 soldiers led by a captain (or a lone general or standard-bearer), as
 * numbers. Its {@code line} is where its front rank is ordered to stand; {@code pos} is where it actually is, walking
 * there at its pace. Each soldier's health is kept, so a block that later becomes real soldiers again comes back
 * wounded as it was.
 */
public final class FieldBlock {
    /** What a block is doing in its army. */
    public enum Duty {
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
        SUPPORT,
        /** The army's general, alone. */
        GENERAL,
        /** The standard-bearer, alone at the general's side; never fights. */
        BEARER,
        /** Running from a lost battle; never fights. */
        FLEEING
    }

    final int id;
    final int side;
    final Arm arm;
    /** Where it stands across the army when not in a line (reserve, archers, cavalry). */
    final double slotRight;
    final double slotBack;
    final float[] health;
    /** The captain's place among its soldiers, or -1 (a lone general or standard-bearer). */
    final int captain;
    /** A warrior general: fights like several soldiers. */
    final boolean champion;
    Point pos;
    Point line;
    double facingX;
    double facingZ;
    Duty duty;
    int file;
    int queueTicket;
    int dutySince;
    @Nullable
    Point rear;
    @Nullable
    FieldBlock relief;
    boolean engaged;
    /** Broken formation to chase and fight (cavalry that charged, a warrior general who went in). */
    boolean charged;
    @Nullable
    List<Point> route;
    int routeIndex;
    int routeStarted;
    /** Arrows left, all its archers together. */
    int arrows;
    /** Seconds in a row it has been fighting hand to hand (a cavalry charge tells only at first). */
    int contactSeconds;
    /** Damage taken but not yet dealt out as whole hits. */
    double pendingDamage;
    /** Soldiers alive, kept up to date as they fall. */
    int alive;

    FieldBlock(int id, int side, Arm arm, int soldiers, float soldierHealth, int captain, boolean champion,
               double slotRight, double slotBack, Point line, double facingX, double facingZ, Duty duty) {
        this.id = id;
        this.side = side;
        this.arm = arm;
        this.health = new float[soldiers];
        java.util.Arrays.fill(health, soldierHealth);
        this.alive = soldiers;
        this.captain = captain;
        this.champion = champion;
        this.slotRight = slotRight;
        this.slotBack = slotBack;
        this.line = line;
        this.pos = line;
        this.facingX = facingX;
        this.facingZ = facingZ;
        this.duty = duty;
        this.arrows = arm == Arm.ARCHERS ? soldiers * FieldRules.ARROWS_EACH : 0;
    }

    public int id() {
        return id;
    }

    public int side() {
        return side;
    }

    public Arm arm() {
        return arm;
    }

    public Duty duty() {
        return duty;
    }

    public Point pos() {
        return pos;
    }

    public double facingX() {
        return facingX;
    }

    public double facingZ() {
        return facingZ;
    }

    public int alive() {
        return alive;
    }

    public int started() {
        return health.length;
    }

    public boolean captainAlive() {
        return captain >= 0 && health[captain] > 0;
    }

    /** Each soldier's health (0 once fallen). */
    public float[] health() {
        return health.clone();
    }

    /** Whether it is fighting: in the front line and engaged, or broken formation to fight. */
    public boolean fighting() {
        return engaged || charged || contactSeconds > 0;
    }

    double halfWidth() {
        return health.length > 1 ? FieldRules.BLOCK_HALF_WIDTH : FieldRules.LONE_HALF_WIDTH;
    }

    /** How far its front rank is from another block's. */
    double distanceTo(FieldBlock other) {
        return distanceFrom(pos, other);
    }

    /** How far another block's front rank is from a point on this block's front, as if this block stood at {@code at}. */
    double distanceFrom(Point at, FieldBlock other) {
        double hw = halfWidth();
        double ohw = other.halfWidth();
        return FieldRules.segmentDistance(
                at.x() - facingZ * hw, at.z() + facingX * hw, at.x() + facingZ * hw, at.z() - facingX * hw,
                other.pos.x() - other.facingZ * ohw, other.pos.z() + other.facingX * ohw,
                other.pos.x() + other.facingZ * ohw, other.pos.z() - other.facingX * ohw);
    }

    /** Deals {@code damage} out in whole hits, each to a random living soldier; returns how many fell. */
    int takeDamage(double damage, java.util.Random random) {
        pendingDamage += damage;
        int fell = 0;
        while (pendingDamage >= FieldRules.HIT && alive > 0) {
            pendingDamage -= FieldRules.HIT;
            int pick = random.nextInt(alive);
            for (int i = 0; i < health.length; i++) {
                if (health[i] > 0 && pick-- == 0) {
                    health[i] -= FieldRules.HIT;
                    if (health[i] <= 0) {
                        health[i] = 0;
                        alive--;
                        fell++;
                    }
                    break;
                }
            }
        }
        if (alive == 0) {
            pendingDamage = 0;
        }
        return fell;
    }
}
