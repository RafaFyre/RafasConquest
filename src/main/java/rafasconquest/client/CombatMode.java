package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;
import rafasconquest.RafasConquest;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.WeaponSkills;

/**
 * Combat mode: hotbar slot 1 is the weapon slot. Tapping 1 with a weapon there turns it on (tapping 1 again turns it
 * off, as does leaving the slot or the weapon). In combat mode left clicks only swing, never break blocks, and a click
 * held 0.01–0.7 s is a deflect ({@link rafasconquest.peerage.Deflects}).
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID, value = Dist.CLIENT)
public final class CombatMode {
    private static final long DEFLECT_MIN_NS = 10_000_000L;
    private static final long DEFLECT_MAX_NS = 700_000_000L;

    private static boolean on;
    private static long pressedAt = -1;

    private CombatMode() {
    }

    private static boolean weaponInSlot(Minecraft mc) {
        return WeaponSkills.caliber(mc.player.getInventory().getItem(0)) != null;
    }

    private static void set(Minecraft mc, boolean value) {
        on = value;
        mc.player.displayClientMessage(Component.literal(value ? "Combat mode" : "Combat mode off"), true);
    }

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getAction() == GLFW.GLFW_PRESS && mc.player != null && mc.screen == null
                && mc.options.keyHotbarSlots[0].matches(event.getKey(), event.getScanCode()) && weaponInSlot(mc)) {
            set(mc, !(on && mc.player.getInventory().selected == 0));
        }
    }

    /** Leaving the weapon slot, or the weapon leaving it, ends combat mode. */
    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (on && (mc.player == null || mc.player.getInventory().selected != 0 || !weaponInSlot(mc))) {
            on = false;
            pressedAt = -1;
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("Combat mode off"), true);
            }
        }
    }

    /** How long the attack button is held: a quick click is a deflect. */
    @SubscribeEvent
    public static void onMouse(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (!on || mc.screen != null || !mc.options.keyAttack.matchesMouse(event.getButton())) {
            return;
        }
        if (event.getAction() == GLFW.GLFW_PRESS) {
            pressedAt = System.nanoTime();
        } else if (event.getAction() == GLFW.GLFW_RELEASE && pressedAt >= 0) {
            long held = System.nanoTime() - pressedAt;
            pressedAt = -1;
            if (held >= DEFLECT_MIN_NS && held <= DEFLECT_MAX_NS) {
                ConquestNetwork.deflect();
            }
        }
    }

    /** No block breaking in combat mode: a click on a block is only a swing. */
    @SubscribeEvent
    public static void onAttack(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (on && event.isAttack() && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
            event.setCanceled(true);
            event.setSwingHand(true);
        }
    }
}
