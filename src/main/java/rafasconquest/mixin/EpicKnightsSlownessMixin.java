package rafasconquest.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rafasconquest.peerage.WeaponSkills;

/**
 * Epic Knights slows whoever holds a heavy shield (Slowness I). A player strong enough ({@link WeaponSkills#ONE_HAND_HEAVY})
 * is not slowed.
 */
@Pseudo
@Mixin(targets = "com.magistuarmory.item.MedievalShieldItem", remap = false)
public class EpicKnightsSlownessMixin {
    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean rafasconquest$strongNotSlowed(LivingEntity wearer, MobEffectInstance effect) {
        return !WeaponSkills.unhindered(wearer) && wearer.addEffect(effect);
    }
}
