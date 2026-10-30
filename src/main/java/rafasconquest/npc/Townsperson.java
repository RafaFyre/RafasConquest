package rafasconquest.npc;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import rafasconquest.core.StreetWalks;

/**
 * A townsperson: one of the crowd that makes a city feel alive, and nothing more. They walk their own stretch of road
 * back and forth ({@link StreetWalks}), now and then stopping a while. Built to cost next to nothing: never saved
 * (spawned near players, gone when nobody is near: {@link Townsfolk}), no AI, no pathfinding, no collision, no pushing,
 * nothing can hit or touch them. On the server each tick is a step along their road; the client draws them and
 * animates their walk from the movement it is sent.
 */
public class Townsperson extends Mob {
    /** Which of Recruits' 20 human skins they wear. */
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(Townsperson.class, EntityDataSerializers.INT);
    public static final int SKINS = 20;
    /** Walking speed, blocks a tick (a stroll); each townsperson a little faster or slower. */
    private static final double SPEED = 0.045;
    /** A stop now and then: this likely each tick while walking, for this many ticks. */
    private static final float STOP_CHANCE = 0.004F;
    private static final int STOP_MIN = 40;
    private static final int STOP_MAX = 160;

    private StreetWalks.Walk walk;
    /** How far along their stretch (0 .. its length), which way, how fast, and ticks left standing still. */
    private double along;
    private int heading = 1;
    private double speed = SPEED;
    private int standing;
    /** The ground's height where they stand, worked out again only when they reach a new block. */
    private int groundX = Integer.MIN_VALUE;
    private int groundZ;
    private int groundY;

    public Townsperson(EntityType<? extends Townsperson> type, Level level) {
        super(type, level);
        setNoGravity(true);
        noPhysics = true;
        setInvulnerable(true);
        setSilent(true);
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    /** Sets them on their stretch, somewhere along it, wearing a skin. */
    void place(StreetWalks.Walk walk, double along, int skin, double speedFactor) {
        this.walk = walk;
        this.along = Math.max(0, Math.min(walk.length(), along));
        this.speed = SPEED * speedFactor;
        this.heading = random.nextBoolean() ? 1 : -1;
        entityData.set(SKIN, Math.floorMod(skin, SKINS));
        Vec3 at = position(this.along);
        moveTo(at.x, at.y, at.z, yawAlong(), 0);
        setYHeadRot(getYRot());
        setYBodyRot(getYRot());
    }

    public int skin() {
        return entityData.get(SKIN);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SKIN, 0);
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            super.tick(); // smooths the movement it is sent, and animates the walk from it
            return;
        }
        // The server does nothing else: a step along the road, or standing still a while.
        long started = System.nanoTime();
        try {
            step();
        } finally {
            Townsfolk.spent(System.nanoTime() - started);
        }
    }

    private void step() {
        tickCount++;
        if (walk == null) {
            discard();
            return;
        }
        if (standing > 0) {
            standing--;
            return;
        }
        if (random.nextFloat() < STOP_CHANCE) {
            standing = STOP_MIN + random.nextInt(STOP_MAX - STOP_MIN);
            return;
        }
        along += heading * speed;
        if (along <= 0 || along >= walk.length()) {
            along = Mth.clamp(along, 0, walk.length());
            heading = -heading; // the end of their stretch: turn back, after a moment
            standing = 10 + random.nextInt(30);
        }
        Vec3 at = position(along);
        float yaw = yawAlong();
        setPos(at.x, at.y, at.z);
        setYRot(yaw);
        setYHeadRot(yaw);
        setYBodyRot(yaw);
    }

    private Vec3 position(double along) {
        double f = walk.length() == 0 ? 0 : along / walk.length();
        double x = Mth.lerp(f, walk.x0(), walk.x1());
        double z = Mth.lerp(f, walk.z0(), walk.z1());
        int bx = Mth.floor(x);
        int bz = Mth.floor(z);
        if (bx != groundX || bz != groundZ) {
            groundX = bx;
            groundZ = bz;
            groundY = level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, bx, bz);
        }
        return new Vec3(x, groundY, z);
    }

    private float yawAlong() {
        double dx = (walk.x1() - walk.x0()) * heading;
        double dz = (walk.z1() - walk.z0()) * heading;
        return (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
    }

    // Untouchable and weightless: nothing hits, pushes or collides with them, and they push nothing.

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false; // Townsfolk takes them away itself
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
