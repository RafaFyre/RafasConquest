package rafasconquest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rafasconquest.bridge.SableLink;

/**
 * Siegeworks engines on Sable ships. Siegeworks moves its engines with Axiomata's terrain collision, which sees only world
 * blocks, not a Sable ship's: an engine on a deck finds nothing under it and falls through into the sea. On a ship it
 * collides as plain Minecraft does instead, which Sable makes see its ships, so it stands on the deck and rides with it.
 * And on a client, an engine aboard is given its place in the world, not in the ship's plot (see {@link #rafasconquest$lerpOutOfPlot}).
 */
@Pseudo
@Mixin(targets = "me.mss1r.siegeworks.entity.base.AbstractSiegeEntity", remap = false)
public class SiegeworksShipCollisionMixin {
    @Inject(method = "usesGeometryTerrainCollision()Z", at = @At("HEAD"), cancellable = true)
    private void rafasconquest$notOnShips(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        // An Artillery Block's gun is a ship's from its first tick, before Sable tracks it as aboard. Its tag isn't sent to
        // a client, which moves the gun too: there, a gun over a ship's box counts, or it drops through the deck unseen.
        if (self.getTags().contains(rafasconquest.content.ArtilleryMount.SHIP_GUN) || SableLink.onShip(self) || SableLink.overShip(self)) {
            cir.setReturnValue(false);
        }
    }

    /**
     * A living entity standing on a ship is sent to clients by its place in the ship's plot; Sable's client hands that
     * place to lerpTo and keeps the entity on the deck by it itself. Siegeworks' lerpTo queues it for its own interpolation,
     * which flung every gun aboard toward the plot, millions of blocks off, out of sight (with its gunner). It gets the
     * place in the world instead: where Sable puts it anyway, and its turn still comes through.
     */
    @Inject(method = "lerpTo(DDDFFI)V", at = @At("HEAD"), cancellable = true)
    private void rafasconquest$lerpOutOfPlot(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        Vec3 target = new Vec3(x, y, z);
        Vec3 at = SableLink.outOfPlot(self.level(), target);
        if (at != target) {
            ci.cancel();
            self.lerpTo(at.x, at.y, at.z, yRot, xRot, steps); // a world place now, so this call goes through to Siegeworks
        }
    }
}
