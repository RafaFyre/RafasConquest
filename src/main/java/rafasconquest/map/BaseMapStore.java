package rafasconquest.map;

import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.core.WorldSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * The world's base map, drawn once and kept in {@code data/rafasconquest_basemap.bin} in the world folder (it depends
 * only on the seed and the settings). Drawing starts when the server starts, on a background thread;
 * players who ask before it is ready get it as soon as it is. Server thread only, apart from the drawing.
 */
public class BaseMapStore {
    private static final String FILE = "rafasconquest_basemap.bin";

    /** The known world on the map reaches this many times as far as the storm wall. */
    public static final int KNOWN_WORLD_FACTOR = 2;

    private final ServerLevel level;
    private final WorldSettings settings;
    private final int visibleRadius;
    private final Path file;
    private final BiConsumer<ServerPlayer, byte[]> sender;
    private final List<UUID> waiting = new ArrayList<>();
    @Nullable
    private volatile byte[] encoded;
    @Nullable
    private CompletableFuture<byte[]> drawing;

    /** {@code sender} delivers the encoded map ({@link BaseMap#encode}) to a player. */
    public BaseMapStore(ServerLevel level, WorldSettings settings, BiConsumer<ServerPlayer, byte[]> sender) {
        this.level = level;
        this.settings = settings;
        this.visibleRadius = KNOWN_WORLD_FACTOR * settings.warZoneOuterEdge();
        this.sender = sender;
        this.file = level.getServer().getWorldPath(LevelResource.ROOT).resolve("data").resolve(FILE);
    }

    /** Loads the saved map, or starts drawing it. */
    public void start() {
        if (encoded != null || drawing != null) {
            return;
        }
        MinecraftServer server = level.getServer();
        drawing = CompletableFuture.supplyAsync(this::loadOrDraw, Util.backgroundExecutor());
        drawing.whenComplete((bytes, error) -> server.execute(() -> {
            if (error != null) {
                RafasConquest.LOGGER.error("Could not draw the world map", error);
                drawing = null;
                return;
            }
            encoded = bytes;
            for (UUID id : waiting) {
                ServerPlayer player = server.getPlayerList().getPlayer(id);
                if (player != null) {
                    sender.accept(player, bytes);
                }
            }
            waiting.clear();
        }));
    }

    /** Sends the map to a player now if it is ready, or once it is. */
    public void request(ServerPlayer player) {
        byte[] bytes = encoded;
        if (bytes != null) {
            sender.accept(player, bytes);
            return;
        }
        if (!waiting.contains(player.getUUID())) {
            waiting.add(player.getUUID());
        }
        start();
    }

    private byte[] loadOrDraw() {
        try {
            if (Files.exists(file)) {
                byte[] saved = Files.readAllBytes(file);
                if (BaseMap.decode(saved).visibleRadius() == visibleRadius) {
                    return saved;
                }
            }
        } catch (IOException e) {
            RafasConquest.LOGGER.warn("Redrawing the world map: the saved one could not be read ({})", e.getMessage());
        }
        long start = System.nanoTime();
        BaseMap map = BaseMapGenerator.generate(level, settings, visibleRadius, settings.warZoneOuterEdge());
        byte[] bytes = map.encode();
        RafasConquest.LOGGER.info("Drew the world map: {} x {} pixels of {} blocks out to {} blocks, {} provinces, in {} ms ({} KB)",
                map.width(), map.height(), map.blocksPerPixel(), visibleRadius, map.centres().size(),
                (System.nanoTime() - start) / 1_000_000, bytes.length / 1024);
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, bytes);
        } catch (IOException e) {
            RafasConquest.LOGGER.warn("Could not save the world map: {}", e.getMessage());
        }
        return bytes;
    }
}
