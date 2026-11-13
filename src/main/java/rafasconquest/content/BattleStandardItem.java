package rafasconquest.content;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * The battle standard: a battle banner twice the size, carried in the hand or worn on a mob's head as a banner can
 * be. It flies the arms of the nation of the player who carries it; a mob shows the arms it was handed with.
 */
public class BattleStandardItem extends Item {
    public BattleStandardItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static UUID nation(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.hasUUID("nation") ? tag.getUUID("nation") : null;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof ServerPlayer player && level.getGameTime() % 20 == slot % 20) {
            UUID serves = BattleBannerBlockEntity.nationOf(player);
            if (!Objects.equals(serves, nation(stack))) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                    if (serves == null) {
                        tag.remove("nation");
                    } else {
                        tag.putUUID("nation", serves);
                    }
                });
            }
        }
    }

    /** Worn on the head, where mobs wear banners. */
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
