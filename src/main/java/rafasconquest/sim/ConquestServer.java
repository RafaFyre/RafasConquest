package rafasconquest.sim;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;
import rafasconquest.ConquestGameRules;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.RecruitsBridge;
import rafasconquest.core.DayClock;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The running simulation for the current server. Everything here runs on the server thread, except
 * {@link #queueStructureLink}, which chunk loading may call from any thread.
 */
public final class ConquestServer {
    private static final int REGIONS_PER_TICK = 1;
    private static final int PLAYER_SCAN_INTERVAL = 20;

    private static final Queue<StructureLink> PENDING_LINKS = new ConcurrentLinkedQueue<>();

    @Nullable
    private static ConquestServer current;

    private final ServerLevel overworld;
    private final ConquestSavedData data;
    private final PoiPrecomputer precomputer;
    private final Deque<long[]> pendingRegions = new ArrayDeque<>();
    private final Set<Long> queuedRegions = new HashSet<>();
    private boolean started;
    private long catchUpRemaining;
    private long catchUpDone;

    public record StructureLink(ChunkPos chunk, PoiType type) {
    }

    private ConquestServer(ServerLevel overworld, ConquestSavedData data) {
        this.overworld = overworld;
        this.data = data;
        this.precomputer = new PoiPrecomputer(overworld, data, RecruitsBridge.NONE);
    }

    @Nullable
    public static ConquestServer get() {
        return current;
    }

    /** Overworld loaded: read the offline gap now, before anything can save and overwrite lastSavedRealTime. */
    public static void attach(ServerLevel overworld, ConquestSavedData data) {
        PENDING_LINKS.clear();
        current = new ConquestServer(overworld, data);
        if (overworld.getGameRules().getBoolean(ConquestGameRules.PASS_INGAME_TIME_FOR_IRL_TIME)) {
            current.catchUpRemaining = DayClock.missedDays(System.currentTimeMillis(), data.lastSavedRealTime(), data.settings().maxCatchUpDays());
        }
    }

    public static void detach() {
        current = null;
        PENDING_LINKS.clear();
    }

    public static void queueStructureLink(ChunkPos chunk, PoiType type) {
        PENDING_LINKS.add(new StructureLink(chunk, type));
    }

    public ConquestSavedData data() {
        return data;
    }

    public ServerLevel overworld() {
        return overworld;
    }

    public PoiPrecomputer precomputer() {
        return precomputer;
    }

    public void onServerStarted() {
        long start = System.nanoTime();
        var regions = PoiPrecomputer.regionsWithin(0, 0, data.settings().initialPrecomputeRadius());
        for (long[] r : regions) {
            precomputer.precomputeRegion((int) r[0], (int) r[1]);
        }
        RafasConquest.LOGGER.info("Precomputed {} regions within {} blocks of spawn in {} ms: {} factions, {} POIs",
                regions.size(), data.settings().initialPrecomputeRadius(), (System.nanoTime() - start) / 1_000_000,
                data.factions().size(), data.pois().size());
        if (catchUpRemaining > 0) {
            RafasConquest.LOGGER.info("Offline catch-up: {} days to process", catchUpRemaining);
        }
        started = true;
    }

    public void tick() {
        if (!started) {
            return;
        }
        drainStructureLinks();
        if (overworld.getGameTime() % PLAYER_SCAN_INTERVAL == 0) {
            queueRegionsNearPlayers();
        }
        for (int i = 0; i < REGIONS_PER_TICK && !pendingRegions.isEmpty(); i++) {
            long[] r = pendingRegions.poll();
            queuedRegions.remove(ConquestSavedData.xzKey((int) r[0], (int) r[1]));
            precomputer.precomputeRegion((int) r[0], (int) r[1]);
        }
        catchUpStep();
        dayStep();
    }

    /** Catch-up pays resources in batches of at most 50 days per tick. It never moves the in-game clock. */
    private void catchUpStep() {
        if (catchUpRemaining <= 0) {
            return;
        }
        long batch = Math.min(DayClock.CATCH_UP_DAYS_PER_TICK, catchUpRemaining);
        for (long i = 0; i < batch; i++) {
            EconomyTick.runDay(data);
        }
        catchUpRemaining -= batch;
        catchUpDone += batch;
        RafasConquest.LOGGER.info("Offline catch-up: processed {} days, {} remaining", batch, catchUpRemaining);
        if (catchUpRemaining == 0) {
            RafasConquest.LOGGER.info("Offline catch-up finished: {} days caught up", catchUpDone);
        }
    }

    private void dayStep() {
        DayClock.Step step = DayClock.advance(data.lastProcessedDay(), DayClock.dayIndex(overworld.getDayTime()));
        for (long i = 0; i < step.daysToProcess(); i++) {
            EconomyTick.runDay(data);
        }
        data.setLastProcessedDay(step.newLastProcessedDay());
    }

    private void drainStructureLinks() {
        StructureLink link;
        while ((link = PENDING_LINKS.poll()) != null) {
            ChunkPos chunk = link.chunk();
            precomputer.precomputeRegion(PoiPrecomputer.regionOf(chunk.getMiddleBlockX()), PoiPrecomputer.regionOf(chunk.getMiddleBlockZ()));
            Poi poi = data.poiInChunk(chunk, link.type());
            if (poi == null) {
                RafasConquest.LOGGER.warn("A {} structure generated in chunk {} but no POI was predicted there", link.type(), chunk);
            } else if (!poi.generated()) {
                poi.setGenerated(true);
                // Extension point: apply the owner's banners and colours to the structure here.
            }
        }
    }

    private void queueRegionsNearPlayers() {
        int distance = data.settings().lazyPrecomputeDistance();
        for (ServerPlayer player : overworld.players()) {
            for (long[] r : PoiPrecomputer.regionsWithin(player.getBlockX(), player.getBlockZ(), distance)) {
                int rx = (int) r[0];
                int rz = (int) r[1];
                if (!data.isRegionPrecomputed(rx, rz) && queuedRegions.add(ConquestSavedData.xzKey(rx, rz))) {
                    pendingRegions.add(r);
                }
            }
        }
    }
}
