package rafasconquest.bridge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * An enlisted player's war horn: a goat horn, in their nation's call, that opens the horn's screen to command their
 * units instead of being blown; it blows once the screen closes on orders given.
 */
public final class WarHorn {
    private static final String TAG = "RafasConquestWarHorn";
    private static final String[] CALLS = {"ponder", "sing", "seek", "feel", "admire", "call", "yearn", "dream"};

    private WarHorn() {
    }

    /** A war horn with the nation's call (one of the eight goat horn calls). */
    public static ItemStack make(int call) {
        ItemStack horn = new ItemStack(Items.GOAT_HORN);
        horn.getOrCreateTag().putString("instrument", "minecraft:" + CALLS[Math.floorMod(call, CALLS.length)] + "_goat_horn");
        horn.getOrCreateTag().putBoolean(TAG, true);
        horn.setHoverName(Component.literal("War Horn"));
        return horn;
    }

    public static boolean is(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(TAG);
    }
}
