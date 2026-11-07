package rafasconquest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.client.WaterSwell;

/** Sends the swell to Sodium's terrain shader each time it is bound for a pass. */
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface", remap = false)
public class SodiumShaderInterfaceMixin {
    @Inject(method = "setupState", at = @At("TAIL"))
    private void rafasconquest$swell(CallbackInfo ci) {
        WaterSwell.upload();
    }
}
