package rafasconquest.npc;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.RafasConquest;
import rafasconquest.core.CityLayout;
import rafasconquest.core.StreetWalks;
import rafasconquest.core.WorldSettings;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Peoples the cities' streets near players with townsfolk ({@link Townsperson}): every district within {@link #NEAR}
 * blocks of a player gets its crowd ({@link StreetWalks}: 40 on its roads, the main roads twice as busy), and loses it
 * once nobody is near. Each townsperson appears only once their stretch of road is loaded. The same district always
 * has the same people (skins, paces) where they start.
 */
public final class Townsfolk {
    /** A district within this many blocks of a player has its townsfolk. */
    static final int NEAR = 64;
    /** Checked once a second. */
    private static final int EVERY = 20;
    private static final long SALT = 0x544F574E53L;

    private record DistrictKey(int cityX, int cityZ, int cellX, int cellZ) {
    }

    /** Each peopled district's townsfolk, by their stretch (null where not yet spawned). */
    private static final Map<DistrictKey, Townsperson[]> peopled = new HashMap<>();
    private static int ticks;

    private Townsfolk() {
    }

    /** Time the townsfolk have cost the server since the last report (their steps, and peopling the districts). */
    private static long spentNanos;
    /** A report of that every this many ticks, while any are walking. */
    private static final int REPORT_EVERY = 600;

    static void spent(long nanos) {
        spentNanos += nanos;
    }

    public static void tick(MinecraftServer server) {
        if (++ticks % REPORT_EVERY == 0) {
            if (!peopled.isEmpty()) {
                RafasConquest.LOGGER.info("[townsfolk] {} walking in {} districts, costing {} ms a tick", walking(), peopled.size(),
                        String.format(java.util.Locale.ROOT, "%.3f", spentNanos / 1e6 / REPORT_EVERY));
            }
            spentNanos = 0;
        }
        if (ticks % EVERY != 0) {
            return;
        }
        long started = System.nanoTime();
        try {
            people(server);
        } finally {
            spentNanos += System.nanoTime() - started;
        }
    }

    private static void people(MinecraftServer server) {
        ServerLevel level = server.overworld();
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return;
        }
        // The districts near a player.
        Map<DistrictKey, CityLayout.Site> near = new HashMap<>();
        for (ServerPlayer player : level.players()) {
            int px = player.getBlockX();
            int pz = player.getBlockZ();
            int reach = NEAR + CityLayout.MAX_HALF_EXTENT;
            for (CityLayout.Site city : CityLayout.allSitesInBox(level.getSeed(), settings, px - reach, pz - reach, px + reach, pz + reach)) {
                for (int cx = 0; cx < city.cells(); cx++) {
                    for (int cz = 0; cz < city.cells(); cz++) {
                        int x0 = city.originX() + CityLayout.cellMin(cx);
                        int x1 = city.originX() + CityLayout.cellMax(cx);
                        int z0 = city.originZ() + CityLayout.cellMin(cz);
                        int z1 = city.originZ() + CityLayout.cellMax(cz);
                        double dx = Math.max(0, Math.max(x0 - px, px - x1));
                        double dz = Math.max(0, Math.max(z0 - pz, pz - z1));
                        if (dx * dx + dz * dz <= NEAR * NEAR) {
                            near.put(new DistrictKey(city.x(), city.z(), cx, cz), city);
                        }
                    }
                }
            }
        }
        // Districts nobody is near any more lose their townsfolk.
        for (Iterator<Map.Entry<DistrictKey, Townsperson[]>> it = peopled.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<DistrictKey, Townsperson[]> e = it.next();
            if (!near.containsKey(e.getKey())) {
                for (Townsperson t : e.getValue()) {
                    if (t != null) {
                        t.discard();
                    }
                }
                it.remove();
            }
        }
        // Districts near a player get theirs, each once their stretch of road is loaded (and again, if taken away).
        for (Map.Entry<DistrictKey, CityLayout.Site> e : near.entrySet()) {
            DistrictKey key = e.getKey();
            List<StreetWalks.Walk> walks = StreetWalks.walks(e.getValue(), key.cellX(), key.cellZ());
            Townsperson[] folk = peopled.computeIfAbsent(key, k -> new Townsperson[walks.size()]);
            Random random = new Random(SALT ^ ((long) key.cityX() * 341873128712L + (long) key.cityZ() * 132897987541L
                    + key.cellX() * 31L + key.cellZ()));
            for (int i = 0; i < folk.length && i < walks.size(); i++) {
                StreetWalks.Walk walk = walks.get(i);
                int skin = random.nextInt(Townsperson.SKINS);
                double start = random.nextDouble() * walk.length();
                double pace = 0.75 + random.nextDouble() * 0.5;
                if (folk[i] != null && !folk[i].isRemoved()) {
                    continue;
                }
                int mx = (int) Math.floor((walk.x0() + walk.x1()) / 2);
                int mz = (int) Math.floor((walk.z0() + walk.z1()) / 2);
                if (!level.hasChunk(mx >> 4, mz >> 4)) {
                    continue;
                }
                Townsperson t = ConquestEntities.TOWNSPERSON.get().create(level);
                if (t == null) {
                    continue;
                }
                t.place(walk, start, skin, pace);
                level.addFreshEntity(t);
                folk[i] = t;
            }
        }
    }

    /** How many townsfolk are walking, for the log and debug. */
    public static int walking() {
        int n = 0;
        for (Townsperson[] folk : peopled.values()) {
            for (Townsperson t : folk) {
                n += t != null && !t.isRemoved() ? 1 : 0;
            }
        }
        return n;
    }

    /** The server is stopping: forget every district (the townsfolk themselves are never saved). */
    public static void clear() {
        peopled.clear();
        ticks = 0;
    }
}
