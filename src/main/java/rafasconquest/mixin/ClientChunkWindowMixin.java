package rafasconquest.mixin;

import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The client keeps only the chunks within its window about the centre the server sets, which in the strategy view is
 * the camera. Widened to reach the body (up to 960 blocks, 60 chunks, away, with the few chunks about it) so the chunks
 * the server sends about the body are kept. The window is only an array of slots: chunks the server drops still go.
 */
@Mixin(ClientChunkCache.class)
public class ClientChunkWindowMixin {
    private static final int REACH_CHUNKS = 60 + 1 + rafasconquest.bridge.CameraView.BODY_CHUNKS + 2;

    @Inject(method = "calculateStorageRange", at = @At("RETURN"), cancellable = true, require = 1)
    private static void rafasconquest$reachBody(int viewDistance, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(cir.getReturnValueI(), REACH_CHUNKS));
    }
}
