package rafasconquest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import rafasconquest.peerage.WeaponSkills;

/**
 * Epic Knights weakens a two-handed weapon held with something in the other hand. A player strong enough
 * ({@link WeaponSkills#ONE_HAND_HEAVY}) wields it one-handed at full strength.
 */
@Pseudo
@Mixin(targets = "com.magistuarmory.item.MedievalWeaponItem", remap = false)
public class EpicKnightsTwoHandedMixin {
    // local 7: whether the weapon is used one-handed (two-handed and something in the off hand)
    @ModifyVariable(method = "inventoryTick", at = @At("STORE"), index = 7)
    private boolean rafasconquest$strongHolds(boolean penalised, ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        return penalised && !WeaponSkills.unhindered(entity);
    }
}
