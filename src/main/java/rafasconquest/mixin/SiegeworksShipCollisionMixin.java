package rafasconquest.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.bridge.SableLink;

/**
 * Siegeworks moves its engines with Axiomata's terrain collision, which sees only world blocks, not a Sable ship's: an
 * engine on a deck finds nothing under it and falls through into the sea. On a ship it collides as plain Minecraft
 * does instead, which Sable makes see its ships, so it stands on the deck and rides with it.
 */
@Pseudo
@Mixin(targets = "me.mss1r.siegeworks.entity.base.AbstractSiegeEntity", remap = false)
public class SiegeworksShipCollisionMixin {
    @Inject(method = "usesGeometryTerrainCollision()Z", at = @At("HEAD"), cancellable = true)
    private void rafasconquest$notOnShips(CallbackInfoReturnable<Boolean> cir) {
        if (SableLink.onShip((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }
}
