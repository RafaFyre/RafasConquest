package rafasconquest.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.peerage.WeaponSkills;

/**
 * Better Combat swings a two-handed weapon with both hands and hides and disables the off hand. A player strong enough
 * ({@link WeaponSkills#ONE_HAND_HEAVY}) swings it with the one-handed sword moveset instead, shield in the other hand.
 */
@Pseudo
@Mixin(targets = "net.bettercombat.logic.WeaponRegistry", remap = false)
public class BetterCombatOneHandMixin {
    private static java.lang.reflect.Method twoHanded;

    // full descriptor: getAttributes is overloaded (ResourceLocation / ItemStack) and the bare name hits the wrong one
    @Inject(method = "getAttributes(Lnet/minecraft/world/item/ItemStack;)Lnet/bettercombat/api/WeaponAttributes;", at = @At("RETURN"), cancellable = true)
    private static void rafasconquest$oneHanded(ItemStack stack, CallbackInfoReturnable<Object> cir) {
        Object attributes = cir.getReturnValue();
        if (attributes == null || stack == null) {
            return;
        }
        ItemStack moveset = WeaponSkills.movesetOf(stack);
        if (moveset == stack) {
            return;
        }
        try {
            if (twoHanded == null) {
                twoHanded = attributes.getClass().getMethod("isTwoHanded");
            }
            if ((boolean) twoHanded.invoke(attributes)) {
                // the sword's own attributes (Better Combat ships them for vanilla swords); a sword is not two-handed, so no loop
                cir.setReturnValue(rafasconquest$attributes(moveset));
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
        }
    }

    private static Object rafasconquest$attributes(ItemStack stack) throws ReflectiveOperationException {
        return Class.forName("net.bettercombat.logic.WeaponRegistry").getMethod("getAttributes", ItemStack.class).invoke(null, stack);
    }
}
