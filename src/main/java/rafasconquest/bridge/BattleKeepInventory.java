package rafasconquest.bridge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rafasconquest.RafasConquest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * While a battle is on, a player who dies keeps what they carry and their experience, as with the keepInventory rule,
 * but for them alone and only then (the world's rule is never touched, so nothing is left switched on if the game stops
 * mid-battle): their inventory is put aside as they die, so nothing drops, and given back as they respawn.
 */
@Mod.EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BattleKeepInventory {
    /** What each fallen player carried, slot by slot, until they respawn. */
    private static final Map<UUID, List<ItemStack>> KEPT = new HashMap<>();

    private BattleKeepInventory() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !RecruitsBattle.running()
                || player.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        Inventory inventory = player.getInventory();
        List<ItemStack> kept = new ArrayList<>(inventory.getContainerSize());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            kept.add(inventory.getItem(i).copy());
        }
        inventory.clearContent(); // nothing to drop
        KEPT.put(player.getUUID(), kept);
    }

    /** Their experience is kept too: none of it drops. */
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && KEPT.containsKey(player.getUUID())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRespawnCopy(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        List<ItemStack> kept = KEPT.remove(event.getOriginal().getUUID());
        if (kept == null) {
            return;
        }
        Inventory inventory = event.getEntity().getInventory();
        for (int i = 0; i < kept.size() && i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, kept.get(i));
        }
        var original = event.getOriginal();
        var player = event.getEntity();
        player.experienceLevel = original.experienceLevel;
        player.totalExperience = original.totalExperience;
        player.experienceProgress = original.experienceProgress;
    }
}
