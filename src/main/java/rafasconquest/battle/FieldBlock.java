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
    /** What a formation's ship is for, at sea; its captain sails it accordingly ({@link FieldBattle}). */
    public enum ShipKind {
        /** Full of archers: keeps behind its allies, at bow range, and shoots. */
        ARCHERS,
        /** Full of foot (swordsmen, shieldwall): sails straight at the enemy and grapples bow first. */
        SWORDSMEN,
        /** Fast, full of swordsmen: keeps clear, then rams an enemy already grappled at its bow, in the side. */
        SHOCK,
        /** The fleet's camp: food, gear and the cavalry's horses; keeps to the rear, nobody on deck. */
        CAMP
    }

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
        /** The army's general, alone and mounted. */
        GENERAL,
        /** The general's guard: 20 riders in full iron who stay with the general and ride out at whoever comes near. */
        GUARD,
        /** The standard-bearer, alone at the general's side; never fights. */
        BEARER,
        /** Cavalry that spent its momentum, riding back to reform; doesn't fight on the way. */
        RALLYING,
        /** A block that broke (its morale, {@link rafasconquest.battle.FieldBattle.Morale}, gave way): running to its wing's backline to
         * regroup, not fighting on the way. */
        ROUTED,
        /** Running from a lost battle; never fights. */
        FLEEING
    }

    final int id;
    final int side;
    /** What it fights as (cavalry dismounted in a siege fights on foot). */
    Arm arm;
    /** Dismounted cavalry in a siege: super heavy infantry, hard to hurt. */
    boolean superHeavy;
    final float[] health;
    /** The captain's place among its soldiers, or -1 (a lone general or standard-bearer). */
    final int captain;
    /** The general: a champion who fights like several soldiers. */
    final boolean champion;
    Point pos;
    Point line;
    /** Where it stood when the armies deployed, for marching back to camp in the evening. */
    Point deployed;
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
    /** Arrows left, all its archers together (cartridges, for musketeers; shots, for engines). */
    int arrows;
    /** The engine a siege train block works ({@link FieldBattle#equip}); null for any other. */
    @Nullable
    Machine machine;
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
    /** Aboard its ship: it sails, and its soldiers fight from the deck, until it comes ashore (if ever). */
    boolean afloat;
    /** At sea, what its ship is for (null for the general's, guard's and standard-bearer's, and ashore). */
    @Nullable
    ShipKind ship;
    /**
     * At sea, the ship it rides: a unit's blocks share one ship, its id the unit's first block's ({@code shipId}); its
     * section of that ship's deck ({@code berth}, 0 first); and the ship's hull.
     */
    int shipId;
    int berth;
    rafasconquest.core.SeaLanes.Size hull = rafasconquest.core.SeaLanes.Size.LARGE;
    /** At sea, the enemy ships grappled to its own: their soldiers may board each other's decks. */
    final java.util.List<FieldBlock> lashed = new java.util.ArrayList<>();
    /**
     * At sea its facing is its ship's heading, the way its bow points, which only turning changes; this is the heading
     * its captain wants it to lie at once it is where it makes for (zero: as it lies).
     */
    double settleX;
    double settleZ;
    int lastThreat;
    /** Real soldiers stand for it now (they walk and fight themselves; the battle reads them back each second). */
    boolean live;
    /** How many of its soldiers are real (see {@link #setReal}). */
    int real;
    /** Damage from blocks that are only numbers, waiting to be dealt to its real soldiers. */
    double liveDamage;
    /** How many soldiers wide its ranks are. */
    int files = BattlePlan.FILE_WIDTH;
    /** The most momentum it can gather (cavalry; twice as much in a Great Spear). */
    double maxMomentum = 1;
    /** The duty it goes back to after rallying (cavalry: SUPPORT, or GUARD for the general's guard). */
    Duty homeDuty;
    /** Whom it means to strike when it charges (chosen by its general's strategies), or null for the nearest. */
    @Nullable
    FieldBlock prey;
    /**
     * Riding in formation toward a charge (a wedge, or around to a flank): not yet broken up to fight, but coming, so
     * infantry in its way braces. {@code waypoint} is where a flanking ride turns in toward its prey.
     */
    boolean approaching;
    @Nullable
    Point waypoint;
    /** In its army's wedge: which half (Unrelenting Charge; 0 leads first), and its place behind the wedge's tip. */
    boolean inWedge;
    int half;
    double wedgeRight;
    double wedgeBack;
    /** Which wing of its army it fights in ({@link FieldArmy#LEFT}, CENTRE, RIGHT). */
    int wing = FieldArmy.CENTRE;
    /** Its platoon in its army ({@link BattlePlan#platoons}), or -1 (the general, guard and bearer, ships). */
    int platoon = -1;
    /**
     * The unit its general moves it with: its platoon in a battle of over {@link FieldRules#UNIT_TACTICS_OVER} soldiers,
     * else -1, and it is moved block by block.
     */
    int unit = -1;
    /** Cavalry outside the wedge: how far to the army's right its wing's backline post is, whether it has charged yet, and
     * which way it is riding along the backline. */
    double homeRight;
    boolean rode;
    int patrolLeg = 1;
    /** How far it moved last second. */
    double moved;
    /** Waiting in reserve: where it deployed, {right, back} of its file's front block, which it keeps. */
    @Nullable
    Point reserve;
    /**
     * Morale: how many soldiers it had each of the last {@link FieldRules#ROUT_WINDOW} seconds (to tell how many it lost
     * in that time), until when it is shaken by its captain's fall, until when it is rallied (can't rout), and
     * whether its captain was alive a second ago.
     */
    int[] history;
    int shakenUntil;
    int rallyUntil;
    boolean captainWasAlive = true;
    /** Until when it is shaken by blows on its flank, on its rear, and by a rout of its own nearby; how often it routed. */
    int flankedUntil;
    int rearedUntil;
    int frightUntil;
    int routs;

    /** Routed {@link FieldRules#SHATTER_ROUTS} times: it retreats to camp and doesn't regroup. */
    public boolean shattered() {
        return routs >= FieldRules.SHATTER_ROUTS;
    }

    /** Seconds it has fought in the front since it last rotated in, and whether it is being rotated out. */
    int fightSeconds;
    boolean rotating;

    /** Placed by hand before the battle: where it keeps its place, {right, back} of what it stands by, or null. */
    @Nullable
    Point placed;
    /** Pincer: the lured enemy block it was sent to strike, and from where: -1 its left, 1 its right, 0 behind. */
    @Nullable
    FieldBlock strikeAt;
    /** A task a player's unit was given for the battle; the general leaves such a block to it. */
    Task task = Task.NONE;
    /** An ambush that has been sprung. */
    boolean sprung;
    int strikeSide;
    /**
     * Led by a player: the general gives it no orders. It stands where the player last sent it ({@code ledLine}, facing
     * {@code ledFacingX/Z}), or charges ({@code ledCharge}), at {@code ledTarget} if one is given.
     */
    boolean led;
    @Nullable
    Point ledLine;
    double ledFacingX;
    double ledFacingZ;
    boolean ledCharge;
    @Nullable
    FieldBlock ledTarget;

    FieldBlock(int id, int side, Arm arm, int soldiers, float soldierHealth, int captain, boolean champion,
               Point line, double facingX, double facingZ, Duty duty) {
        this.id = id;
        this.shipId = id;
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
        this.homeDuty = duty;
        this.arrows = arm == Arm.ARCHERS ? soldiers * FieldRules.ARROWS_EACH : arm == Arm.MUSKETEERS ? soldiers * FieldRules.CARTRIDGES_EACH : 0;
    }

    /** What a player's unit does in a battle ({@link FieldBattle#assign}). */
    public enum Task {
        NONE, BAIT, SUPPORT, FLANK, KILL,
        /** Hidden in a wood before the battle; strikes an enemy block fighting near it. */
        AMBUSH
    }

    /** The duty it was deployed to: its general's, the guard's, the standard's, or a soldier's. */
    public Duty homeDuty() {
        return homeDuty;
    }

    public Task task() {
        return task;
    }

    public boolean superHeavy() {
        return superHeavy;
    }

    public boolean live() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
        liveDamage = 0;
    }

    /**
     * How many of its soldiers are real now: a block is live while any are. The rest are still numbers, and fight as
     * numbers even against a live enemy.
     */
    public void setReal(int real) {
        this.real = real;
        if ((real > 0) != live) {
            setLive(real > 0);
        }
    }

    public int real() {
        return real;
    }

    /** Soldier i, who is only numbers in a live block, takes a hit; who has fallen is counted at the next report. */
    public void hurt(int i, float amount) {
        if (health[i] > 0) {
            health[i] = Math.max(0, health[i] - amount);
        }
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
    public boolean afloat() {
        return afloat;
    }

    @Nullable
    public ShipKind ship() {
        return ship;
    }

    /** The ship it rides at sea, shared by its unit's blocks ({@link FieldBattle#crew}). */
    public int shipId() {
        return shipId;
    }

    public int berth() {
        return berth;
    }

    public rafasconquest.core.SeaLanes.Size hull() {
        return hull;
    }

    /** The enemy ships grappled to its own. */
    public java.util.List<FieldBlock> lashed() {
        return java.util.Collections.unmodifiableList(lashed);
    }

    public boolean braced() {
        return braced;
    }

    /** In its army's wedge (Great Spear, Unrelenting Charge). */
    public boolean inWedge() {
        return inWedge;
    }

    /** Cavalry that has charged at least once (and so keeps riding its backline). */
    public boolean hasCharged() {
        return rode;
    }

    /** Which wing of its army it fights in. */
    public int wing() {
        return wing;
    }

    /** Its platoon in its army, or -1 if it is in none. */
    public int platoon() {
        return platoon;
    }

    /** How many soldiers wide its ranks are. */
    public int files() {
        return files;
    }

    /** Whom it means to strike, or null. */
    @Nullable
    public FieldBlock prey() {
        return prey;
    }

    /** Seconds it has fought in the front since it last rotated in. */
    public int fightSeconds() {
        return fightSeconds;
    }

    /** Riding in formation toward a charge, not yet broken up to fight. */
    public boolean approaching() {
        return approaching;
    }

    /** The enemy block it was sent to strike (a pincer, envelopment, stopped cavalry), or null. */
    @Nullable
    public FieldBlock strikeAt() {
        return strikeAt;
    }

    /** Rallied: its foe routed or lost its captain, and for a while it can't rout. */
    public boolean rallied(int now) {
        return now < rallyUntil;
    }

    /** Rallied by a leader: it shakes off any fright, and can't rout until {@code until} (battle seconds). */
    public void rally(int until) {
        rallyUntil = Math.max(rallyUntil, until);
        shakenUntil = 0;
        frightUntil = 0;
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

    @Nullable
    public Machine machine() {
        return machine;
    }

    /** How near shooters close before they stand and shoot: archers' halt ({@link BattlePlan#ARCHER_RANGE}), an engine as far within its reach. */
    public double standOff() {
        return BattlePlan.ARCHER_RANGE * (machine != null ? machine.reach / FieldRules.ARCHER_RANGE : 1);
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

    /** At sea, the heading its captain wants its ship to lie at once there ({@link #facingX()} as it lies, if none). */
    public double settleX() {
        return settleX == 0 && settleZ == 0 ? facingX : settleX;
    }

    public double settleZ() {
        return settleX == 0 && settleZ == 0 ? facingZ : settleZ;
    }

    /** A real ship's heading, as it really lies: at sea, a real formation's facing is its ship's. */
    public void steered(double dirX, double dirZ) {
        double length = Math.hypot(dirX, dirZ);
        if (afloat && length > 1e-6) {
            facingX = dirX / length;
            facingZ = dirZ / length;
        }
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

    /** Half the width of its front rank. */
    public double halfWidth() {
        return health.length > 1 ? (files - 1) * BattlePlan.SPACING / 2 : FieldRules.LONE_HALF_WIDTH;
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

    /**
     * As {@link #takeDamage}, but each hit goes to a living soldier picked by {@code weight} (how many blows each took),
     * so those the enemy actually reached fall; with no weight on anyone living, to anyone, as there.
     */
    int takeDamage(double damage, java.util.Random random, int[] weight) {
        int total = 0;
        for (int i = 0; i < health.length; i++) {
            total += health[i] > 0 ? weight[i] : 0;
        }
        if (total == 0) {
            return takeDamage(damage, random);
        }
        pendingDamage += damage;
        int fell = 0;
        while (pendingDamage >= FieldRules.HIT && alive > 0 && total > 0) {
            pendingDamage -= FieldRules.HIT;
            int pick = random.nextInt(total);
            for (int i = 0; i < health.length; i++) {
                if (health[i] > 0 && weight[i] > 0 && (pick -= weight[i]) < 0) {
                    health[i] -= FieldRules.HIT;
                    if (health[i] <= 0) {
                        health[i] = 0;
                        alive--;
                        fell++;
                        total -= weight[i];
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

    /** Ranks stand this many soldiers' spacings apart, front to back. */
    static final double SLOT_DEPTH = 1.75;

    /** How far right of its block's middle soldier i stands. */
    public double slotRight(int i) {
        int f = Math.max(1, files);
        return health.length == 1 ? 0 : (place(i) % f - (f - 1) / 2.0) * BattlePlan.SPACING;
    }

    /** How far behind its block's front rank soldier i stands. */
    public double slotBack(int i) {
        return (place(i) / Math.max(1, files)) * BattlePlan.SPACING * SLOT_DEPTH;
    }

    /** Where soldier i stands in the ranks, counting along each rank from the front: the captain in the back rank's middle. */
    private int place(int i) {
        if (captain < 0) {
            return i;
        }
        int back = BattlePlan.captainPlace(health.length, Math.max(1, files));
        return i == captain ? back : i == back ? captain : i;
    }

    /** Each soldier's body in the battle's {@link Bodies} (-1 for none), or null while it has none. */
    @Nullable
    int[] body;
}
