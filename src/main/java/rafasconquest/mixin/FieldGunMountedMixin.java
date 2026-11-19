package rafasconquest.mixin;

import me.mss1r.siegeworks.entity.siege.AbstractFieldGunEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.content.MountedCannonBlock;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * A culverin on a Mounted Light Cannon is drawn barrel only: no wheels, carriage or rack. Every culverin shares one
 * model's bones, so each is set shown or hidden every time one is drawn.
 */
@Pseudo
@Mixin(targets = "me.mss1r.siegeworks.client.entity.fieldgun.FieldGunModel", remap = false)
public abstract class FieldGunMountedMixin {
    @Inject(method = "setCustomAnimations(Lme/mss1r/siegeworks/entity/siege/AbstractFieldGunEntity;JLsoftware/bernie/geckolib/animation/AnimationState;)V",
            at = @At("TAIL"))
    private void rafasconquest$barrelOnly(AbstractFieldGunEntity gun, long instanceId, AnimationState<?> state, CallbackInfo ci) {
        boolean mounted = MountedCannonBlock.MOUNTED.contains(((net.minecraft.world.entity.Entity) (Object) gun).getUUID());
        GeoModel<?> model = (GeoModel<?>) (Object) this;
        model.getBone("wheels").ifPresent(b -> b.setHidden(mounted));
        model.getBone("racks").ifPresent(b -> b.setHidden(mounted));
        model.getBone("base").ifPresent(b -> {
            b.setHidden(mounted); // the carriage's own cubes
            b.setChildrenHidden(false); // the barrel hangs from it
        });
    }
}
