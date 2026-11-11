package rafasconquest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.bridge.CameraView;

/**
 * A player in the strategy view is sent the chunks about their camera, and the few about their body too, so the body
 * keeps its ground on their client and can go on walking where it was sent.
 */
@Mixin(ChunkMap.class)
public abstract class ChunkMapCameraMixin {
    @Shadow
    abstract int getPlayerViewDistance(ServerPlayer player);

    @Shadow
    private void markChunkPendingToSend(ServerPlayer player, ChunkPos pos) {
        throw new AssertionError();
    }

    @Shadow
    private static void dropChunk(ServerPlayer player, ChunkPos pos) {
        throw new AssertionError();
    }

    @Inject(method = "updateChunkTracking", at = @At("HEAD"), cancellable = true, require = 1)
    private void rafasconquest$cameraChunks(ServerPlayer player, CallbackInfo ci) {
        Vec3 camera = CameraView.of(player);
        ChunkTrackingView old = player.getChunkTrackingView();
        if (camera == null && !(old instanceof CameraView.Chunks)) {
            return; // vanilla's view about the body
        }
        ci.cancel();
        int distance = getPlayerViewDistance(player);
        ChunkTrackingView.Positioned square = (ChunkTrackingView.Positioned) ChunkTrackingView.of(
                camera == null ? player.chunkPosition() : new ChunkPos(BlockPos.containing(camera)), distance);
        ChunkTrackingView view = camera == null ? square : new CameraView.Chunks(square, player.chunkPosition());
        if (view.equals(old)) {
            return;
        }
        ChunkPos oldCentre = old instanceof CameraView.Chunks c ? c.camera().center() : old instanceof ChunkTrackingView.Positioned p ? p.center() : null;
        if (!square.center().equals(oldCentre)) {
            player.connection.send(new ClientboundSetChunkCacheCenterPacket(square.center().x, square.center().z));
        }
        // Vanilla's difference for a view not its own forgets every chunk and sends them all again: only the change here.
        old.forEach(pos -> {
            if (!view.contains(pos)) {
                dropChunk(player, pos);
            }
        });
        view.forEach(pos -> {
            if (!old.contains(pos)) {
                markChunkPendingToSend(player, pos);
            }
        });
        player.setChunkTrackingView(view);
    }
}
