package rafasconquest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.client.WaterSwell;

/** Sends the swell to Distant Horizons' terrain shader once it has bound it and set its own uniforms. */
@Pseudo
// DH's merged jar runs the _neoforge copy on NeoForge; without it the far sea gets no swell uniforms and shows plain
@Mixin(targets = {"com.seibel.distanthorizons.common.render.openGl.terrain.GlDhTerrainShaderProgram",
        "com.seibel.distanthorizons.common.render.openGl.terrain.GlDhTerrainShaderProgram_neoforge"}, remap = false)
public class DhTerrainShaderMixin {
    @Inject(method = "fillUniformData", at = @At("TAIL"))
    private void rafasconquest$swell(CallbackInfo ci) {
        WaterSwell.upload();
    }
}
