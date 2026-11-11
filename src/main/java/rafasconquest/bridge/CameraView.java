package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The strategy view's camera, from the client: while it is set, the world is loaded and sent to the player about the
 * camera rather than their body (chunks and entities alike, see the ChunkMap mixins), so the field they look at is real;
 * the few chunks about the body are sent too ({@link Chunks}), so it keeps its ground and can walk.
 * The body's own chunks stay loaded on the server by its usual player ticket. Server thread only.
 */
public final class CameraView {
    /** The client's camera reach (BattleScreen.REACH) and a little slack for the time between updates. */
    private static final double REACH = 960 + 48;
    /** Outlives the client's resend (every 2 s) a few times over, then lets the chunks go if the client stopped. */
    private static final TicketType<ChunkPos> TICKET = TicketType.create("rafasconquest_camera", Comparator.comparingLong(ChunkPos::toLong), 200);
    private static final Map<UUID, Camera> CAMERAS = new HashMap<>();
    /** Players whose view moved (or came back to the body) lately, by the server tick to stop watching them at. */
    private static final Map<UUID, Integer> SETTLING = new HashMap<>();
    /** Long enough for every chunk about the new view to reach the player. */
    private static final int SETTLE_TICKS = 600;
    /** Settling players sent chunks this tick, to be sent the entities in them at the tick's end. */
    private static final Set<ServerPlayer> ARRIVED = new HashSet<>();

    private record Camera(Vec3 at, ResourceKey<Level> dimension) {
    }

    /** Chunks this far (in chunks) about the body are sent with the camera's, so it can walk on while the view is away. */
    public static final int BODY_CHUNKS = 2;

    /** What a player in the strategy view is sent: the camera's usual square, and the small one about their body. */
    public record Chunks(ChunkTrackingView.Positioned camera, ChunkPos body) implements ChunkTrackingView {
        private boolean nearBody(int x, int z) {
            return Math.abs(x - body.x) <= BODY_CHUNKS && Math.abs(z - body.z) <= BODY_CHUNKS;
        }

        @Override
        public boolean contains(int x, int z, boolean includeOuter) {
            return camera.contains(x, z, includeOuter) || nearBody(x, z);
        }

        @Override
        public void forEach(java.util.function.Consumer<ChunkPos> action) {
            camera.forEach(action);
            for (int x = body.x - BODY_CHUNKS; x <= body.x + BODY_CHUNKS; x++) {
                for (int z = body.z - BODY_CHUNKS; z <= body.z + BODY_CHUNKS; z++) {
                    if (!camera.contains(x, z)) {
                        action.accept(new ChunkPos(x, z));
                    }
                }
            }
        }
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
        SETTLING.put(player.getUUID(), level.getServer().getTickCount() + SETTLE_TICKS);
    }

    /** A chunk reached the player (NeoForge's ChunkWatchEvent.Sent). */
    public static void chunkSent(ServerPlayer player) {
        if (SETTLING.containsKey(player.getUUID())) {
            ARRIVED.add(player);
        }
    }

    /**
     * At each tick's end, a settling player is paired with the entities in the chunks that reached them. The game skips
     * an entity whose chunk the player hasn't been sent yet, and tries again only when one of them moves: without this,
     * soldiers about a view that moved, or about the body once the strategy view closes, stay figures until then.
     */
    public static void tick(net.minecraft.server.MinecraftServer server) {
        for (ServerPlayer player : ARRIVED) {
            player.serverLevel().getChunkSource().move(player);
        }
        ARRIVED.clear();
        int now = server.getTickCount();
        SETTLING.values().removeIf(until -> until < now);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.getY() < player.level().getMinBuildHeight() && (CAMERAS.containsKey(player.getUUID()) || SETTLING.containsKey(player.getUUID()))) {
                rescue(player);
            }
        }
    }

    /**
     * shortcut: the body of a player in the strategy view was seen to fall out of the world (2026-11-11) for a cause not
     * yet found; it is put back on the ground, and the log says how it stood so the cause can be found and this removed.
     */
    private static void rescue(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        int x = player.getBlockX();
        int z = player.getBlockZ();
        boolean loaded = level.hasChunk(x >> 4, z >> 4);
        rafasconquest.RafasConquest.LOGGER.warn("Strategy view: {} fell below the world at {} (camera {}, chunk loaded {}, mode {}, vehicle {}); put back on the ground",
                player.getName().getString(), player.position(), of(player), loaded, player.gameMode.getGameModeForPlayer(), player.getVehicle());
        player.stopRiding();
        int y = level.getChunk(x >> 4, z >> 4).getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
        player.fallDistance = 0;
        player.setDeltaMovement(Vec3.ZERO);
        player.teleportTo(level, player.getX(), y, player.getZ(), player.getYRot(), player.getXRot());
    }

    public static void forget(ServerPlayer player) {
        CAMERAS.remove(player.getUUID());
        SETTLING.remove(player.getUUID());
        ARRIVED.remove(player);
    }
}
