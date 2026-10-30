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

import java.util.UUID;

/**
 * Someone to talk to (right-click: {@link rafasconquest.peerage.Talk}): a recruitment post's recruiter, behind the
 * counter facing the door, the nation's officer there, who enlists players ({@link Recruitment}) and gives military
 * quests; or a hold's noble ({@code person}, a character of the world's realm), at their city's post or in their castle's
 * keep, who gives noble quests, knighthood and land. Kept with the world; never moves, can't be hurt or pushed.
 */
public class Recruiter extends Mob {
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(Recruiter.class, EntityDataSerializers.INT);

    /** The nation they recruit for (its faction's id). */
    @Nullable
    private UUID nation;
    /** The character this is (an index into the realm's people), or -1 for a recruiter. */
    private int person = -1;

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

    /** Sets them at their hold as its noble: character {@code person}, styled {@code name}. */
    public void noble(int person, String name, UUID nation, double x, double y, double z, float yaw, int skin) {
        post(nation, "", x, y, z, yaw, skin);
        this.person = person;
        setCustomName(Component.literal(name));
    }

    public int person() {
        return person;
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
            rafasconquest.peerage.Talk.open(server, this);
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
        tag.putInt("person", person);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        nation = tag.hasUUID("nation") ? tag.getUUID("nation") : null;
        entityData.set(SKIN, tag.getInt("skin"));
        person = tag.contains("person") ? tag.getInt("person") : -1;
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
