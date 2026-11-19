package rafasconquest.content;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.bridge.SableLink;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * A {@link MediumCannonBlock}'s gun: its lay ({@link CannonAim}), its reload, the shot it is loaded with, and who works
 * it. No entity stands for it, so it rides its Sable ship as any block does and costs nothing while nobody works it.
 * <p>
 * A player takes it by right clicking it and lays and fires it themselves (CannonControl, client side). Otherwise the
 * nearest Recruits soldier standing at it is its crew: it fires at the battle's order for that soldier ({@link #order},
 * from RecruitsBattle) or, with none, at the soldier's own target. Shots fly as {@link CannonShots}.
 */
public class MediumCannonEntity extends BlockEntity {
    /** A soldier this near (blocks) its block is taken as its crew; one further than {@link #LEAVE} has left it. */
    private static final double MAN = 2.5;
    private static final double LEAVE = 4.5;
    /** How fast its crew lays it, degrees a tick; laid within {@link #ON} of the mark, it fires. */
    private static final double SLEW = 3;
    private static final double ON = 1.5;
    /** An order not renewed in this many ticks has lapsed. */
    private static final int ORDER_TICKS = 60;
    /** Clients learn its lay at most this often, ticks. */
    private static final int SYNC_TICKS = 3;

    /** What it may be loaded with. shortcut: roundshot only so far; the captain will choose once the others exist. */
    public enum Shot { ROUNDSHOT }

    /** The battle's orders, by soldier: where to fire and with what. Server thread only. */
    private record Order(Vec3 at, Shot shot, long tick) {
    }

    private static final Map<UUID, Order> ORDERS = new java.util.HashMap<>();
    /** Soldiers working a cannon, so no other takes them. Server thread only. */
    private static final Map<UUID, BlockPos> CREWS = new java.util.HashMap<>();
    /** The client's loaded cannons, for drawing. Render thread only. */
    public static final Set<MediumCannonEntity> CLIENT_LOADED = Collections.newSetFromMap(new WeakHashMap<>());

    /** Its lay, degrees (see {@link CannonAim}). On a client, {@link #shownYaw} and {@code shownPitch} ease after it. */
    public float yaw;
    public float pitch;
    public float shownYaw;
    public float shownPitch;
    private Shot shot = Shot.ROUNDSHOT;
    private int reload;
    @Nullable
    private UUID gunner;
    private boolean unsynced;
    private long syncedTick;

    public MediumCannonEntity(BlockPos pos, BlockState state) {
        super(ConquestContent.MEDIUM_CANNON_ENTITY.get(), pos, state);
    }

    /** The battle's order to soldier {@code crew}: fire at {@code at} loaded with {@code shot}; renewed while it stands. */
    public static void order(UUID crew, Vec3 at, Shot shot, long tick) {
        if (ORDERS.size() > 1024) {
            ORDERS.values().removeIf(o -> tick - o.tick() > ORDER_TICKS); // soldiers gone without being let go
        }
        ORDERS.put(crew, new Order(at, shot, tick));
    }

    /** Whether {@code soldier} works a Medium Cannon now. */
    public static boolean crewing(UUID soldier) {
        return CREWS.containsKey(soldier);
    }

    void serverTick(ServerLevel level, BlockState state) {
        if (reload > 0) {
            reload--;
        }
        long now = level.getGameTime();
        boolean look = Math.floorMod(now + worldPosition.asLong(), 10) == 0; // a crew is looked for twice a second
        if (gunner == null && !look) {
            flush(level, now);
            return; // idle: nothing more each tick, however many guns a fleet carries
        }
        Vec3 base = SableLink.outOfPlot(level, Vec3.atBottomCenterOf(worldPosition));
        Entity crew = gunner == null ? null : level.getEntity(gunner);
        if (crew == null || !crew.isAlive() || crew.distanceToSqr(base) > LEAVE * LEAVE) {
            if (gunner != null) {
                release();
            }
            if (!look) {
                flush(level, now);
                return;
            }
            crew = findCrew(level, base);
            if (crew == null) {
                flush(level, now);
                return;
            }
            take(crew);
        }
        if (!(crew instanceof Player)) {
            Vec3 mark = mark(crew, now);
            if (mark != null) {
                lay(level, state, base, mark);
            }
        }
        flush(level, now);
    }

    /** Where its crew is to fire: the battle's order for them, else their own target; null for nowhere. */
    @Nullable
    private Vec3 mark(Entity crew, long now) {
        Order o = ORDERS.get(crew.getUUID());
        if (o != null && now - o.tick() <= ORDER_TICKS) {
            shot = o.shot();
            return o.at();
        }
        if (crew instanceof Mob mob && mob.getTarget() != null && mob.getTarget().isAlive()) {
            return mob.getTarget().position().add(0, mob.getTarget().getBbHeight() * 0.5, 0);
        }
        return null;
    }

    /** Its crew lays it toward {@code mark} a little each tick, and fires once it bears and is loaded. */
    private void lay(ServerLevel level, BlockState state, Vec3 base, Vec3 mark) {
        Quaterniond ship = SableLink.turnAt(level, worldPosition);
        double facing = CannonAim.facing(state.getValue(MediumCannonBlock.FACING));
        Quaterniond carriage = CannonAim.carriage(ship, facing, yaw);
        Vector3d muzzle = CannonAim.muzzle(carriage, CannonAim.barrel(carriage, pitch)).add(base.x, base.y, base.z);
        double[] want = CannonAim.layFor(CannonAim.ballistic(muzzle, new Vector3d(mark.x, mark.y, mark.z)), ship, facing);
        float y = step(yaw, want[0]);
        float p = step(pitch, want[1]);
        if (y != yaw || p != pitch) {
            yaw = y;
            pitch = p;
            unsynced = true;
        }
        if (Math.abs(want[0] - yaw) <= ON && Math.abs(want[1] - pitch) <= ON) {
            fire(level, gunner == null ? null : level.getEntity(gunner));
        }
    }

    private static float step(float from, double to) {
        return (float) (from + Math.max(-SLEW, Math.min(SLEW, to - from)));
    }

    /** Fires, if loaded: the shot leaves the muzzle along the bore as the ship lies now. */
    void fire(ServerLevel level, @Nullable Entity by) {
        if (reload > 0) {
            return;
        }
        rafasconquest.battle.Machine card = rafasconquest.battle.Machine.MEDIUM_CANNON;
        reload = (int) Math.round(card.reload * 20);
        Vec3 base = SableLink.outOfPlot(level, Vec3.atBottomCenterOf(worldPosition));
        Quaterniond ship = SableLink.turnAt(level, worldPosition);
        Quaterniond carriage = CannonAim.carriage(ship, CannonAim.facing(getBlockState().getValue(MediumCannonBlock.FACING)), yaw);
        Quaterniond barrel = CannonAim.barrel(carriage, pitch);
        Vector3d muzzle = CannonAim.muzzle(carriage, barrel).add(base.x, base.y, base.z);
        CannonShots.fire(level, new Vec3(muzzle.x, muzzle.y, muzzle.z), CannonAim.bore(barrel).mul(CannonAim.SPEED), shot, by, card.damage);
    }

    /** The cannon at {@code pos} if {@code player} works it, else null. */
    @Nullable
    private static MediumCannonEntity workedBy(net.minecraft.server.level.ServerPlayer player, BlockPos pos) {
        return player.level().isLoaded(pos) && player.level().getBlockEntity(pos) instanceof MediumCannonEntity gun
                && player.getUUID().equals(gun.gunner) ? gun : null;
    }

    /** A player lays the cannon they work (from their client), held within its arc. */
    public static void layBy(net.minecraft.server.level.ServerPlayer player, BlockPos pos, float yaw, float pitch) {
        MediumCannonEntity gun = workedBy(player, pos);
        if (gun != null && Float.isFinite(yaw) && Float.isFinite(pitch)) {
            gun.yaw = (float) CannonAim.clamp(yaw);
            gun.pitch = (float) CannonAim.clamp(pitch);
            gun.unsynced = true;
        }
    }

    /** A player fires the cannon they work, if it is loaded. */
    public static void fireBy(net.minecraft.server.level.ServerPlayer player, BlockPos pos) {
        MediumCannonEntity gun = workedBy(player, pos);
        if (gun != null) {
            gun.fire(player.serverLevel(), player);
        }
    }

    /** A player lets go of the cannon they work. */
    public static void leaveBy(net.minecraft.server.level.ServerPlayer player, BlockPos pos) {
        MediumCannonEntity gun = workedBy(player, pos);
        if (gun != null) {
            gun.release();
        }
    }

    /** Whether {@code player} may take it: nobody works it, or they already do, or its crew is a soldier. */
    boolean takeBy(Player player) {
        Entity crew = gunner == null || !(level instanceof ServerLevel s) ? null : s.getEntity(gunner);
        if (crew instanceof Player other && other != player && other.isAlive()) {
            return false;
        }
        if (gunner != null) {
            release();
        }
        take(player);
        return true;
    }

    @Nullable
    UUID gunner() {
        return gunner;
    }

    private void take(Entity crew) {
        gunner = crew.getUUID();
        CREWS.put(gunner, worldPosition);
    }

    void release() {
        if (gunner != null) {
            CREWS.remove(gunner, worldPosition);
            ORDERS.remove(gunner);
            gunner = null;
        }
    }

    @Nullable
    private static Entity findCrew(ServerLevel level, Vec3 base) {
        AbstractRecruitEntity best = null;
        double nearest = MAN * MAN;
        for (AbstractRecruitEntity r : level.getEntitiesOfClass(AbstractRecruitEntity.class, new AABB(base, base).inflate(MAN))) {
            double d = r.distanceToSqr(base);
            // Only one told to hold its place (a gunner at its post, or one a player set there), not any passing by.
            if (r.isAlive() && !r.isPassenger() && r.getFollowState() == 3 && !CREWS.containsKey(r.getUUID()) && d <= nearest) {
                best = r;
                nearest = d;
            }
        }
        return best;
    }

    /** Clients learn its lay, at most every {@link #SYNC_TICKS}. */
    private void flush(Level level, long now) {
        if (unsynced && now - syncedTick >= SYNC_TICKS) {
            unsynced = false;
            syncedTick = now;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void onLoad() {
        if (level != null && level.isClientSide) {
            shownYaw = yaw;
            shownPitch = pitch;
            CLIENT_LOADED.add(this);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        CLIENT_LOADED.remove(this);
        if (level != null && !level.isClientSide) {
            release();
        }
    }

    @Override
    public void onChunkUnloaded() {
        CLIENT_LOADED.remove(this);
        if (level != null && !level.isClientSide) {
            release();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("yaw", yaw);
        tag.putFloat("pitch", pitch);
        tag.putInt("reload", reload);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        yaw = (float) CannonAim.clamp(tag.getFloat("yaw"));
        pitch = (float) CannonAim.clamp(tag.getFloat("pitch"));
        reload = tag.getInt("reload");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}
