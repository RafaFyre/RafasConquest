package rafasconquest.mixin;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.client.WaterSwell;

/** Adds the swell to Sodium's terrain shader as it loads it (from its own jar, so a resource pack can't). */
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.gl.shader.ShaderLoader", remap = false)
public class SodiumShaderLoaderMixin {
    @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
    private static void rafasconquest$swell(ResourceLocation name, CallbackInfoReturnable<String> cir) {
        if (name.getNamespace().equals("sodium")) {
            cir.setReturnValue(WaterSwell.patch(name.getPath(), cir.getReturnValue()));
        }
    }
}
