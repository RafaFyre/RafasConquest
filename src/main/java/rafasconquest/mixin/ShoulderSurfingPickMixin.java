package rafasconquest.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rafasconquest.bridge.SableLink;

/**
 * Shoulder Surfing reads a block hit on a Sable ship where Sable leaves it, in the ship's far-off plot: its entity pick
 * then sweeps a box millions of blocks long every frame, and its crosshair aims at the plot. Both read it where the
 * ship lies in the world. The hit itself is left as is: Sable's block interaction wants the plot.
 */
@Pseudo
@Mixin(targets = {
        "com.github.exopandora.shouldersurfing.client.world.phys.ObjectPicker",
        "com.github.exopandora.shouldersurfing.client.renderer.CrosshairRenderer"}, remap = false)
public class ShoulderSurfingPickMixin {
    @Redirect(method = "pick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/phys/BlockHitResult;getLocation()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 rafasconquest$blockHitInWorld(BlockHitResult hit) {
        return rafasconquest$inWorld(hit.getLocation());
    }

    @Redirect(method = "computeCrosshairOffset", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/phys/HitResult;getLocation()Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 rafasconquest$aimInWorld(HitResult hit) {
        return rafasconquest$inWorld(hit.getLocation());
    }

    private static Vec3 rafasconquest$inWorld(Vec3 at) {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? at : SableLink.outOfPlot(mc.level, at);
    }
}
