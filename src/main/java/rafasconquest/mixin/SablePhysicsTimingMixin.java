package rafasconquest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.bridge.SablePhysics;

/** Times Sable's native pre-tick (buoyancy, ropes, joints) and its physics steps, for the battle report ({@link SablePhysics}). */
@Pseudo
@Mixin(targets = "dev.ryanhcode.sable.physics.impl.rapier.RapierPhysicsPipeline", remap = false)
public class SablePhysicsTimingMixin {
    @Unique
    private static long rafasconquest$started;

    @Inject(method = "prePhysicsTicks", at = @At("HEAD"))
    private void rafasconquest$buoyancyStarts(CallbackInfo ci) {
        rafasconquest$started = System.nanoTime();
    }

    @Inject(method = "prePhysicsTicks", at = @At("RETURN"))
    private void rafasconquest$buoyancyEnds(CallbackInfo ci) {
        SablePhysics.buoyancyNanos += System.nanoTime() - rafasconquest$started;
    }

    @Inject(method = "physicsTick", at = @At("HEAD"))
    private void rafasconquest$stepStarts(double dt, CallbackInfo ci) {
        rafasconquest$started = System.nanoTime();
    }

    @Inject(method = "physicsTick", at = @At("RETURN"))
    private void rafasconquest$stepEnds(double dt, CallbackInfo ci) {
        SablePhysics.stepNanos += System.nanoTime() - rafasconquest$started;
    }
}
