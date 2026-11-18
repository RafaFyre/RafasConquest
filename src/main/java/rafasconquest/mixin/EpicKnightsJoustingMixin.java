package rafasconquest.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rafasconquest.peerage.WeaponSkills;

/**
 * Epic Knights slows whoever wears jousting armour (Slowness II). A player with the Fortitude
 * ({@link WeaponSkills#JOUST_UNSLOWED}) is not slowed.
 */
@Pseudo
@Mixin(targets = "com.magistuarmory.item.armor.JoustingItem", remap = false)
public class EpicKnightsJoustingMixin {
    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean rafasconquest$hardyNotSlowed(LivingEntity wearer, MobEffectInstance effect) {
        return !WeaponSkills.joustUnslowed(wearer) && wearer.addEffect(effect);
    }
}
