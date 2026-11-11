package rafasconquest.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Lets {@link rafasconquest.bridge.CameraView} re-centre a player's chunks at once when the camera moves. */
@Mixin(ChunkMap.class)
public interface ChunkMapAccess {
    @Invoker("updateChunkTracking")
    void rafasconquest$updateChunkTracking(ServerPlayer player);
}
