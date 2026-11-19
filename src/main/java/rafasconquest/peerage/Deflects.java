package rafasconquest.peerage;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import rafasconquest.RafasConquest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A deflect, from a quick click in combat mode ({@code client.CombatMode}): for the next second all damage to the
 * player comes to nothing. A deflect that met no damage failed, and the player can't deflect again for a second after.
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID)
public final class Deflects {
    private static final int WINDOW = 20;
    private static final int REST_AFTER_FAIL = 20;

    private static final class Window {
        final int until;
        boolean deflected;

        Window(int until) {
            this.until = until;
        }
    }

    private static final Map<UUID, Window> windows = new HashMap<>();

    private Deflects() {
    }

    public static void start(ServerPlayer player) {
        if (WeaponSkills.caliber(player.getMainHandItem()) == null) {
            return;
        }
        int now = player.server.getTickCount();
        Window last = windows.get(player.getUUID());
        // One window at a time (no chaining taps), and a failed one rests a second before the next.
        if (last != null && (now < last.until || !last.deflected && now < last.until + REST_AFTER_FAIL)) {
            return;
        }
        windows.put(player.getUUID(), new Window(now + WINDOW));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        Window w = windows.get(player.getUUID());
        if (w != null && player.server.getTickCount() < w.until) {
            event.setCanceled(true);
            w.deflected = true;
            player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        windows.remove(event.getEntity().getUUID());
    }
}
