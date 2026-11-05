package rafasconquest.bridge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import rafasconquest.RafasConquest;

/**
 * While a battle is on, a player who dies keeps what they carry and their experience, as with the keepInventory rule,
 * but for them alone and only then (the world's rule is never touched, so nothing is left switched on if the game stops
 * mid-battle): their inventory is put aside as they die, so nothing drops, and given back as they respawn. It is put
 * aside in the player's own saved data (which Forge carries over to the respawned player), so it survives the game
 * closing on the death screen.
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID)
public final class BattleKeepInventory {
    private static final String KEPT = "rafasconquest_battle_kept";

    private BattleKeepInventory() {
    }

    private static CompoundTag persisted(Player player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !RecruitsBattle.running()
                || player.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        CompoundTag persisted = persisted(player);
        persisted.put(KEPT, player.getInventory().save(new ListTag()));
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
        player.getInventory().clearContent(); // nothing to drop
    }

    /** Their experience is kept too: none of it drops. */
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && persisted(player).contains(KEPT)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRespawnCopy(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        Player player = event.getEntity();
        CompoundTag persisted = persisted(player);
        if (!persisted.contains(KEPT)) {
            return;
        }
        player.getInventory().load(persisted.getList(KEPT, Tag.TAG_COMPOUND));
        persisted.remove(KEPT);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
        Player original = event.getOriginal();
        player.experienceLevel = original.experienceLevel;
        player.totalExperience = original.totalExperience;
        player.experienceProgress = original.experienceProgress;
    }
}
