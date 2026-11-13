package rafasconquest.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.bridge.SableLink;

/**
 * A grapple hook thrown by a soldier onto an enemy battle ship's deck: it flies in an arc from the thrower's hand to
 * its place on the enemy's rail (every throw holds), then stays there as the ship moves. Its line, drawn from the
 * thrower's hand like a fishing line, is the rope hauling the two ships together. The battle throws and takes away hooks;
 * one whose thrower falls, or whose ship is gone, takes itself away.
 */
public class GrappleHook extends Entity {
    private static final EntityDataAccessor<Integer> THROWER = SynchedEntityData.defineId(GrappleHook.class, EntityDataSerializers.INT);
    /** Ticks a hook is in the air. */
    static final int FLIGHT = 12;
    /** How high over the straight line between hand and rail its arc rises, at the top. */
    static final double ARC = 3;
    /** A hook whose ship has been gone this long, in ticks, is taken away. */
    static final int LOST_TICKS = 40;

    private int ship = -1;
    /** Its place on the ship, in the ship's frame: along it toward the bow, and across it toward its right. */
    private double along;
    private double across;
    @Nullable
    private Vec3 from;
    private int age;
    private int lost;

    public GrappleHook(EntityType<? extends GrappleHook> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    /** Thrown by {@code thrower} at formation {@code ship}'s battle ship, to catch {@code along} and {@code across} of its middle. */
    public GrappleHook(Level level, LivingEntity thrower, int ship, double along, double across) {
        this(GrappleHooks.HOOK.get(), level);
        this.ship = ship;
        this.along = along;
        this.across = across;
        this.from = hand(thrower, 1);
        entityData.set(THROWER, thrower.getId());
        setPos(from);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(THROWER, -1);
    }

    /** Who threw it, as the client knows them; null if they aren't there. */
    @Nullable
    public Entity thrower() {
        return level().getEntity(entityData.get(THROWER));
    }

    /** Where a soldier holds a hook's line: their right hand. */
    public static Vec3 hand(Entity holder, float partialTick) {
        float yaw = (holder instanceof LivingEntity l ? net.minecraft.util.Mth.lerp(partialTick, l.yBodyRotO, l.yBodyRot) : holder.getYRot())
                * (float) (Math.PI / 180);
        double fx = -Math.sin(yaw);
        double fz = Math.cos(yaw);
        // Their right, facing (fx, fz), is (-fz, fx).
        return holder.getPosition(partialTick).add(-fz * 0.35 + fx * 0.3, holder.getBbHeight() * 0.7, fx * 0.35 + fz * 0.3);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            return;
        }
        Entity thrower = thrower();
        if (thrower == null || !thrower.isAlive() || from == null) {
            discard();
            return;
        }
        double[] at = SableLink.shipAt(ship);
        if (at == null || at.length < 5 || Double.isNaN(at[4])) {
            if (++lost > LOST_TICKS) {
                discard();
            }
            return;
        }
        lost = 0;
        Vec3 hold = new Vec3(at[0] + at[2] * along - at[3] * across, at[4] + 0.2, at[1] + at[3] * along + at[2] * across);
        age++;
        if (age < FLIGHT) {
            double t = age / (double) FLIGHT;
            setPos(from.lerp(hold, t).add(0, ARC * 4 * t * (1 - t), 0));
        } else {
            setPos(hold);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
