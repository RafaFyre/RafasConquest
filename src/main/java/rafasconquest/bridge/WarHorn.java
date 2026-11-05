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
        net.minecraft.core.registries.BuiltInRegistries.INSTRUMENT.getHolder(net.minecraft.resources.ResourceLocation.withDefaultNamespace(CALLS[Math.floorMod(call, CALLS.length)] + "_goat_horn"))
                .ifPresent(instrument -> horn.set(net.minecraft.core.component.DataComponents.INSTRUMENT, instrument));
        rafasconquest.gear.ItemFlags.set(horn, TAG);
        horn.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.literal("War Horn"));
        return horn;
    }

    public static boolean is(ItemStack stack) {
        return rafasconquest.gear.ItemFlags.has(stack, TAG);
    }
}
