package rafasconquest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.client.WaterSwell;

/** Adds the sea's shading to Distant Horizons' terrain shader as it loads it from its jar. */
@Pseudo
@Mixin(targets = "com.seibel.distanthorizons.common.render.openGl.glObject.shader.GlShader", remap = false)
public class DhShaderLoaderMixin {
    @Inject(method = "loadFile", at = @At("RETURN"), cancellable = true)
    private static void rafasconquest$swell(String path, boolean absolute, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(WaterSwell.patchDh(path, cir.getReturnValue()));
    }
}
