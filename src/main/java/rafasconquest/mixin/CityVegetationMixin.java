package rafasconquest.mixin;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.VinesFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.worldgen.RoadFeature;

/**
 * Vanilla plants trees and vines after the structures step, so a city's own clearing can't stop them: they are not
 * planted with their root inside a city's walls. Trees carry their own vines and other decorations, so those go too.
 */
@Mixin({TreeFeature.class, VinesFeature.class})
public class CityVegetationMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void rafasconquest$notInCities(FeaturePlaceContext<?> context, CallbackInfoReturnable<Boolean> cir) {
        if (RoadFeature.inCity(context.origin().getX(), context.origin().getZ())) {
            cir.setReturnValue(false);
        }
    }
}
