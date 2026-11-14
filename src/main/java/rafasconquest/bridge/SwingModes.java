package rafasconquest.bridge;

import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.component.BetterCombatDataComponents;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import rafasconquest.RafasConquest;

/**
 * Better Combat halberds and glaives swing in one of two modes, switched with the middle mouse button: thrust (a poke,
 * then an overhead blow) or sweep (a swing to the left, then to the right). The mode is Better Combat's preset on the
 * weapon itself (data/rafasconquest/weapon_attributes), so it stays with the weapon and reaches every client with it.
 * Only this class touches Better Combat; call {@link #installed()} first.
 */
public final class SwingModes {
    private SwingModes() {
    }

    public static boolean installed() {
        return ModList.get().isLoaded("bettercombat");
    }

    /** "halberd" or "glaive" for a weapon that has swing modes, else null. */
    public static String polearm(ItemStack stack) {
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        String category = attributes == null ? null : attributes.category();
        return "halberd".equals(category) || "glaive".equals(category) ? category : null;
    }

    /** Thrust (or the weapon's own swings, before the first switch) goes to sweep; sweep goes to thrust. */
    public static void cycle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        String category = polearm(stack);
        if (category == null) {
            return;
        }
        ResourceLocation now = stack.get(BetterCombatDataComponents.WEAPON_PRESET_ID);
        boolean sweep = now == null || !now.getPath().endsWith("_sweep");
        stack.set(BetterCombatDataComponents.WEAPON_PRESET_ID,
                ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, category + (sweep ? "_sweep" : "_thrust")));
        player.displayClientMessage(Component.literal(sweep ? "Sweep: swing left, then right" : "Thrust: poke, then overhead"), true);
    }
}
