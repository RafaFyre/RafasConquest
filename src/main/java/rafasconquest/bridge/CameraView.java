package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The strategy view's camera, from the client: while it is set, the world is loaded and sent to the player about the
 * camera rather than their body (chunks and entities alike, see the ChunkMap mixins), so the field they look at is real.
 * The body's own chunks stay loaded on the server by its usual player ticket. Server thread only.
 */
public final class CameraView {
    /** The client's camera reach (BattleScreen.REACH) and a little slack for the time between updates. */
    private static final double REACH = 960 + 48;
    /** Outlives the client's resend (every 2 s) a few times over, then lets the chunks go if the client stopped. */
    private static final TicketType<ChunkPos> TICKET = TicketType.create("rafasconquest_camera", Comparator.comparingLong(ChunkPos::toLong), 200);
    private static final Map<UUID, Camera> CAMERAS = new HashMap<>();

    private record Camera(Vec3 at, ResourceKey<Level> dimension) {
    }

    private CameraView() {
    }

    /** Where the player's world is loaded about instead of their body, or null for the body. */
    @Nullable
    public static Vec3 of(ServerPlayer player) {
        Camera camera = CAMERAS.isEmpty() ? null : CAMERAS.get(player.getUUID());
        return camera == null || camera.dimension() != player.level().dimension() ? null : camera.at();
    }

    /** The camera moved to {@code at}, or is gone (null). Refused (as gone) out of a battle or out of reach. */
    public static void set(ServerPlayer player, @Nullable Vec3 at) {
        if (at != null && (!Double.isFinite(at.x) || !Double.isFinite(at.z)
                || Math.hypot(at.x - player.getX(), at.z - player.getZ()) > REACH
                || RecruitsBattle.unitInfo(player).isEmpty())) {
            at = null;
        }
        ServerLevel level = player.serverLevel();
        Camera old = at == null ? CAMERAS.remove(player.getUUID()) : CAMERAS.put(player.getUUID(), new Camera(at, level.dimension()));
        if (at == null && old == null) {
            return;
        }
        if (at != null) {
            int radius = Math.min(player.requestedViewDistance(), level.getServer().getPlayerList().getViewDistance()) + 1;
            ChunkPos centre = new ChunkPos(BlockPos.containing(at));
            level.getChunkSource().addRegionTicket(TICKET, centre, radius, centre);
        }
        ChunkMap map = level.getChunkSource().chunkMap;
        ((rafasconquest.mixin.ChunkMapAccess) map).rafasconquest$updateChunkTracking(player);
        map.move(player); // pairs the player again with every entity, from the new viewpoint
    }

    public static void forget(ServerPlayer player) {
        CAMERAS.remove(player.getUUID());
    }
}
