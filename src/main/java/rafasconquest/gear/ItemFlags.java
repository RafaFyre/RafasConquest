package rafasconquest.gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Boolean marks this mod keeps on item stacks, in their custom data. */
public final class ItemFlags {
    private ItemFlags() {
    }

    public static void set(ItemStack stack, String key) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(key, true));
    }

    public static boolean has(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBoolean(key);
    }
}
