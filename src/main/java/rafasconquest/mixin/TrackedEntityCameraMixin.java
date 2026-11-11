package rafasconquest.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rafasconquest.bridge.CameraView;

/** A player in the strategy view is sent the entities about their camera, not their body. */
@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public class TrackedEntityCameraMixin {
    @Redirect(method = "updatePlayer", require = 1, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;position()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 rafasconquest$cameraPosition(ServerPlayer player) {
        Vec3 camera = CameraView.of(player);
        return camera == null ? player.position() : camera;
    }
}
