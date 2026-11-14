package rafasconquest.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Recruits' bowmen loose arrows whose pick-up item is the empty stack, which can't be saved: every such arrow in a chunk
 * being saved threw, was left out of the save, and had a whole crash report made for it (reading the PC's hardware), a
 * twenty-fifth of a sea battle's server time. An arrow saved without an item is saved with its kind's own.
 */
@Mixin(AbstractArrow.class)
public abstract class ArrowSaveMixin {
    @Shadow
    private ItemStack pickupItemStack;

    @Shadow
    protected abstract ItemStack getDefaultPickupItem();

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void rafasconquest$neverAnEmptyItem(CompoundTag tag, CallbackInfo ci) {
        if (pickupItemStack.isEmpty()) {
            pickupItemStack = getDefaultPickupItem();
        }
    }
}
