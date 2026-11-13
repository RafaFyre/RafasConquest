package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.npc.ConquestEntities;

/**
 * An oar: four oak fences for its shaft and two open oak trapdoors for its blade, drawn from vanilla's own block models.
 * Its handle is at its position; it points the way it faces (yaw), tilted down by its pitch, blade outermost.
 * One held by an {@link OarBlock} goes when that block does.
 */
public class Oar extends Entity {
    /** Shaft length in blocks, handle to blade tip. */
    public static final int LENGTH = 4;

    /** The oar block holding it; null for a loose one. */
    @Nullable
    private BlockPos home;

    public Oar(EntityType<? extends Oar> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    /** Held by the oar block at {@code home}, its handle at {@code at}, pointing level toward {@code yaw}. */
    public Oar(Level level, BlockPos home, Vec3 at, float yaw) {
        this(ConquestEntities.OAR.get(), level);
        this.home = home.immutable();
        moveTo(at.x, at.y, at.z, yaw, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && home != null && level().isLoaded(home)
                && !(level().getBlockState(home).getBlock() instanceof OarBlock)) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().inflate(LENGTH); // the shaft reaches far past its small box
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        home = NbtUtils.readBlockPos(tag, "Home").orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (home != null) {
            tag.put("Home", NbtUtils.writeBlockPos(home));
        }
    }
}
