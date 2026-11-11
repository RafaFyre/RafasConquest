package rafasconquest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rafasconquest.bridge.CameraView;

/** A player in the strategy view is sent the chunks about their camera, not their body. */
@Mixin(ChunkMap.class)
public class ChunkMapCameraMixin {
    @Redirect(method = "updateChunkTracking", require = 1, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;chunkPosition()Lnet/minecraft/world/level/ChunkPos;"))
    private ChunkPos rafasconquest$cameraChunk(ServerPlayer player) {
        Vec3 camera = CameraView.of(player);
        return camera == null ? player.chunkPosition() : new ChunkPos(BlockPos.containing(camera));
    }
}
