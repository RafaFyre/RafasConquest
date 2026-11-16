package rafasconquest.client;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfig;
import com.seibel.distanthorizons.api.enums.config.EDhApiDepthRange;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiRenderProxy;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBeforeApplyShaderRenderEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiCancelableEventParam;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiRenderParam;
import com.seibel.distanthorizons.api.objects.DhApiResult;
import org.joml.Matrix4f;

/**
 * Distant Horizons' side of the Sprite System: only where its fade of the world into its terrain starts. Soldiers' and
 * ships' 3D figures are laid over its terrain by {@link DistantArmyRenderer}, so they move and turn freely every frame.
 * Only {@link DistantHorizonsLink} calls this, and only when Distant Horizons is installed.
 */
final class DistantHorizonsArmies {
    private static final double FADE_START_FACTOR = 1.5;
    private static final double FADE_PAST_NEAR_CLIP = 16;

    private DistantHorizonsArmies() {
    }

    /** Distant Horizons' projection as of its last frame, sixteen numbers in its own order; null before one. */
    private static volatile float[] projection;

    /** Runs raycasts off the render thread: one at a time, a daemon so it never keeps the game open. */
    private static final java.util.concurrent.ExecutorService RAYCASTS = java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "RafasConquest DH raycast");
        t.setDaemon(true);
        return t;
    });
    private static final long RAYCAST_WAIT_MS = 100;

    /**
     * The top of the first block of Distant Horizons' terrain along a ray, or null if it hits none, isn't ready, or
     * takes too long. Distant Horizons waits on its loaders, and its loaders wait on the render thread to upload to
     * the GPU, so waiting for it unbounded on the render thread hangs the game.
     */
    static net.minecraft.world.phys.Vec3 raycast(net.minecraft.world.phys.Vec3 from, net.minecraft.world.phys.Vec3 dir, int reach) {
        java.util.concurrent.Future<net.minecraft.world.phys.Vec3> hit = RAYCASTS.submit(() -> raycastNow(from, dir, reach));
        try {
            return hit.get(RAYCAST_WAIT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.util.concurrent.TimeoutException e) {
            hit.cancel(false); // drops it if still queued; a running one finishes once the render thread is free again
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (java.util.concurrent.ExecutionException e) {
            return null;
        }
    }

    private static net.minecraft.world.phys.Vec3 raycastNow(net.minecraft.world.phys.Vec3 from, net.minecraft.world.phys.Vec3 dir, int reach) {
        try {
            com.seibel.distanthorizons.api.interfaces.world.IDhApiLevelWrapper level = DhApi.Delayed.worldProxy.getSinglePlayerLevel();
            if (level == null || DhApi.Delayed.terrainRepo == null) {
                return null;
            }
            DhApiResult<com.seibel.distanthorizons.api.objects.data.DhApiRaycastResult> hit = DhApi.Delayed.terrainRepo.raycast(
                    level, from.x, from.y, from.z, (float) dir.x, (float) dir.y, (float) dir.z, reach,
                    DhApi.Delayed.terrainRepo.createSoftCache()); // without a cache Distant Horizons fails every column
            if (!hit.success || hit.payload == null || hit.payload.pos == null || hit.payload.dataPoint == null) {
                return null;
            }
            // pos.y is the bottom of the column it met, which may run far underground; the ground is its top
            return new net.minecraft.world.phys.Vec3(hit.payload.pos.x + 0.5, hit.payload.dataPoint.topYBlockPos, hit.payload.pos.z + 0.5);
        } catch (RuntimeException e) {
            return null; // Distant Horizons has no world loaded yet
        }
    }

    /** Ground heights read from Distant Horizons' terrain, by column ({@link #column}); NaN where it had none. */
    private static final java.util.Map<Long, Float> GROUND = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Set<Long> ASKED = java.util.concurrent.ConcurrentHashMap.newKeySet();
    /** Its own thread: a queue of columns must not hold up the map's raycasts, which wait only 100 ms. */
    private static final java.util.concurrent.ExecutorService GROUND_READS = java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "RafasConquest DH ground");
        t.setDaemon(true);
        return t;
    });
    private static volatile com.seibel.distanthorizons.api.interfaces.data.IDhApiTerrainDataCache groundCache;
    private static volatile long groundAged;
    private static final long GROUND_AGE_MS = 10_000;

    private static long column(int x, int z) {
        return (long) x << 32 | (z & 0xFFFFFFFFL);
    }

    /**
     * The ground (top that blocks motion, under any leaves) at a column as Distant Horizons' terrain has it, or NaN
     * while it is being read or it has none. Reads go to a background thread and are kept; every ten seconds the
     * misses are forgotten, so columns whose terrain arrives later are read again.
     */
    static double ground(int x, int z) {
        long now = System.currentTimeMillis();
        if (now - groundAged > GROUND_AGE_MS) {
            groundAged = now;
            GROUND.values().removeIf(y -> y.isNaN());
            if (GROUND.size() > 200_000) {
                GROUND.clear(); // shortcut: dropped whole when big, not least-recently-used; fine at a few battles' columns
            }
            groundCache = null; // a fresh cache picks up terrain Distant Horizons has built since
        }
        long key = column(x, z);
        Float y = GROUND.get(key);
        if (y != null) {
            return y;
        }
        if (ASKED.size() < 4096 && ASKED.add(key)) {
            GROUND_READS.execute(() -> {
                try {
                    GROUND.put(key, groundNow(x, z));
                } finally {
                    ASKED.remove(key);
                }
            });
        }
        return Double.NaN;
    }

    private static float groundNow(int x, int z) {
        try {
            com.seibel.distanthorizons.api.interfaces.world.IDhApiLevelWrapper level = DhApi.Delayed.worldProxy.getSinglePlayerLevel();
            com.seibel.distanthorizons.api.interfaces.data.IDhApiTerrainDataRepo repo = DhApi.Delayed.terrainRepo;
            if (level == null || repo == null) {
                return Float.NaN;
            }
            com.seibel.distanthorizons.api.interfaces.data.IDhApiTerrainDataCache cache = groundCache;
            if (cache == null) {
                groundCache = cache = repo.createSoftCache(); // without a cache Distant Horizons fails every column
            }
            DhApiResult<com.seibel.distanthorizons.api.objects.data.DhApiTerrainDataPoint[]> col = repo.getColumnDataAtBlockPos(level, x, z, cache);
            if (!col.success || col.payload == null) {
                return Float.NaN;
            }
            int top = Integer.MIN_VALUE;
            for (com.seibel.distanthorizons.api.objects.data.DhApiTerrainDataPoint p : col.payload) {
                if (p == null || p.blockStateWrapper == null || p.blockStateWrapper.isAir()) {
                    continue;
                }
                if (p.blockStateWrapper.getWrappedMcObject() instanceof net.minecraft.world.level.block.state.BlockState s
                        && (s.is(net.minecraft.tags.BlockTags.LEAVES) || !s.blocksMotion() && !s.liquid())) {
                    continue; // canopy, grass and flowers: figures stand under them
                }
                top = Math.max(top, p.topYBlockPos); // as in raycastNow: a point's top is the ground on it
            }
            return top == Integer.MIN_VALUE ? Float.NaN : top;
        } catch (RuntimeException e) {
            return Float.NaN; // Distant Horizons has no world loaded yet
        }
    }

    /** Fails (with a linkage error) if the installed Distant Horizons has no render proxy in its API. */
    static void register() {
        DhApi.Delayed.class.getName();
        IDhApiRenderProxy.class.getName();
        // Fired every frame (3.3.3) just before it puts its terrain on screen: the projection its depth was made with.
        DhApi.events.bind(DhApiBeforeApplyShaderRenderEvent.class, new DhApiBeforeApplyShaderRenderEvent() {
            @Override
            public void beforeRender(DhApiCancelableEventParam<DhApiRenderParam> event) {
                projection = event.value.dhProjectionMatrix.getValuesAsArray();
            }
        });
    }

    /**
     * Distant Horizons' depth this frame, for {@link DistantOcclusion}: {@code {texture, 1 if its depth is -1..1, its
     * empty value}}, and the inverse of the projection it was made with (screen back to view) written into
     * {@code inverse}. Null when there is none.
     */
    static float[] depth(Matrix4f inverse) {
        IDhApiRenderProxy render = DhApi.Delayed.renderProxy;
        float[] values = projection;
        if (render == null || values == null || values.length != 16) {
            return null;
        }
        try {
            DhApiResult<Integer> texture = render.getDhDepthTextureGlId();
            if (!texture.success || texture.payload == null || texture.payload <= 0) {
                return null;
            }
            // JOML reads column by column; Distant Horizons may store either way. In the right order the entry that
            // divides by depth (row 3, column 2) is about -1, and the mirrored spot holds the depth offset instead.
            inverse.set(values);
            if (Math.abs(inverse.m23() + 1) > Math.abs(inverse.m32() + 1)) {
                inverse.transpose();
            }
            if (!(Math.abs(inverse.determinant()) > 1e-20)) {
                return null;
            }
            inverse.invert();
            boolean signed = render.getDepthRange() == EDhApiDepthRange.NEG_ONE_TO_POS_ONE;
            return new float[]{texture.payload, signed ? 1 : 0, render.getDepthDirection().farDepth};
        } catch (IllegalStateException e) {
            return null; // not set up yet
        }
    }

    /**
     * Where Distant Horizons' fade of the world into its terrain starts, as its fade shader works it out: half again
     * the distance a chunk beyond where its terrain begins. The fade's on/off setting isn't in its API, so this
     * assumes it is on (its default); if it is off, figures just take over a little early.
     */
    static double fadeStart(float partialTick) {
        IDhApiConfig configs = DhApi.Delayed.configs;
        IDhApiRenderProxy render = DhApi.Delayed.renderProxy;
        if (configs == null || render == null || !configs.graphics().renderingEnabled().getValue()) {
            return 0;
        }
        try {
            return (render.getNearClipPlaneDistanceInBlocks(partialTick) + FADE_PAST_NEAR_CLIP) * FADE_START_FACTOR;
        } catch (IllegalStateException e) {
            return 0; // not set up yet
        }
    }
}
