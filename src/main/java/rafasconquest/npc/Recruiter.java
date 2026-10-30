package rafasconquest.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rafasconquest.net.ConquestNetwork;

import java.util.UUID;

/**
 * A recruitment post's recruiter: stands behind the counter facing the door, and offers whoever speaks to them
 * (right-click) a place in their nation and its army ({@link Recruitment}). Kept with the world; never moves, can't be
 * hurt or pushed.
 */
public class Recruiter extends Mob {
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(Recruiter.class, EntityDataSerializers.INT);

    /** The nation they recruit for (its faction's id). */
    @Nullable
    private UUID nation;

    public Recruiter(EntityType<? extends Recruiter> type, Level level) {
        super(type, level);
        setNoAi(true);
        setInvulnerable(true);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20);
    }

    /** Sets them at their post, for a nation, facing {@code yaw}. */
    public void post(UUID nation, String nationName, double x, double y, double z, float yaw, int skin) {
        this.nation = nation;
        moveTo(x, y, z, yaw, 0);
        setYHeadRot(yaw);
        setYBodyRot(yaw);
        entityData.set(SKIN, Math.floorMod(skin, Townsperson.SKINS));
        setCustomName(Component.literal("Recruiter of " + nationName));
        setCustomNameVisible(true);
    }

    @Nullable
    public UUID nation() {
        return nation;
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (player instanceof ServerPlayer server) {
            ConquestNetwork.sendRecruiterOffer(server, Recruitment.offer(server, this));
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (nation != null) {
            tag.putUUID("nation", nation);
        }
        tag.putInt("skin", skin());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        nation = tag.hasUUID("nation") ? tag.getUUID("nation") : null;
        entityData.set(SKIN, tag.getInt("skin"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
