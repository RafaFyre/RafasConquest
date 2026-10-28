package rafasconquest.battle;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;


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
        /** Archers and cavalry, which take their own orders. */
        SUPPORT,
        /** The army's general, alone. */
        GENERAL,
        /** The standard-bearer, alone at the general's side; never fights. */
        BEARER,
        /** Cavalry that spent its momentum, riding back to reform; doesn't fight on the way. */
        RALLYING,
        /** Running from a lost battle; never fights. */
        FLEEING
    }

    final int id;
    final int side;
    final Arm arm;
    final float[] health;
    /** The captain's place among its soldiers, or -1 (a lone general or standard-bearer). */
    final int captain;
    /** The general: a champion who fights like several soldiers. */
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
    /** Arrows left, all its archers together. */
    int arrows;
    /** Seconds in a row it has been fighting hand to hand (a cavalry charge tells only at first). */
    int contactSeconds;
    /** Damage taken but not yet dealt out as whole hits. */
    double pendingDamage;
    /** Soldiers alive, kept up to date as they fall. */
    int alive;
    /** Cavalry momentum, 0 to 1 ({@link FieldRules#CHARGE_BONUS}), where it was a second ago, and whom it last
     * struck in melee (null if nobody). */
    double momentum;
    @Nullable
    Point lastPos;
    @Nullable
    FieldBlock contactFoe;
    /** For real cavalry, whom its riders are striking, as the live battle reports it each second. */
    @Nullable
    FieldBlock liveContactFoe;
    /** Spent cavalry reforms until this second before charging again. */
    int reformUntil;
    /** Braced against charging cavalry, facing it, and when it last saw cavalry coming. */
    boolean braced;
    int lastThreat;
    /** Real soldiers stand for it now (they walk and fight themselves; the battle reads them back each second). */
    boolean live;
    /** Damage from blocks that are only numbers, waiting to be dealt to its real soldiers. */
    double liveDamage;

    FieldBlock(int id, int side, Arm arm, int soldiers, float soldierHealth, int captain, boolean champion,
               Point line, double facingX, double facingZ, Duty duty) {
        this.id = id;
        this.side = side;
        this.arm = arm;
        this.health = new float[soldiers];
        java.util.Arrays.fill(health, soldierHealth);
        this.alive = soldiers;
        this.captain = captain;
        this.champion = champion;
        this.line = line;
        this.pos = line;
        this.facingX = facingX;
        this.facingZ = facingZ;
        this.duty = duty;
        this.arrows = arm == Arm.ARCHERS ? soldiers * FieldRules.ARROWS_EACH : 0;
    }

    public boolean live() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
        liveDamage = 0;
    }

    /** Takes the damage waiting for its real soldiers, which the caller now deals. */
    public double drainLiveDamage() {
        double d = liveDamage;
        liveDamage = 0;
        return d;
    }

    /** Cavalry momentum, 0 to 1. */
    public double momentum() {
        return momentum;
    }

    /** In melee this second. */
    public boolean inMelee() {
        return contactSeconds > 0;
    }

    /** Braced against charging cavalry. */
    public boolean braced() {
        return braced;
    }

    /** The enemy block it fought this second, or null. */
    @Nullable
    public FieldBlock contactFoe() {
        return contactFoe;
    }

    /** The enemy block a real cavalry block's riders are striking this second, or null. */
    public void setLiveContact(@Nullable FieldBlock foe) {
        liveContactFoe = foe;
    }

    /** Where its front rank is ordered to stand. */
    public Point line() {
        return line;
    }

    public boolean charged() {
        return charged;
    }

    /** The captain's place among its soldiers, or -1. */
    public int captain() {
        return captain;
    }

    /** The general, a champion. */
    public boolean champion() {
        return champion;
    }

    public int file() {
        return file;
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
