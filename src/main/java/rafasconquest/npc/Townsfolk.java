package rafasconquest.npc;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import rafasconquest.core.CityLayout;
import rafasconquest.core.StreetWalks;
import rafasconquest.core.WorldSettings;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Peoples the cities' streets near players with townsfolk: every district within {@link #NEAR} blocks of a player
 * (the nearest {@link #MAX_DISTRICTS}) has its crowd ({@link StreetWalks}: 40 on its roads, the main roads twice as
 * busy). The server only names each player's districts' stretches of road; the player's client walks and draws the
 * townsfolk ({@code ClientTownsfolk}), so they cost the server nothing a tick.
 */
public final class Townsfolk {
    /** A district within this many blocks of a player has its townsfolk. */
    static final int NEAR = 64;
    /** At most this many districts have their townsfolk at once (the nearest), about 40 people each. */
    static final int MAX_DISTRICTS = 2;
    /** Checked once a second. */
    private static final int EVERY = 20;

    private record DistrictKey(int cityX, int cityZ, int cellX, int cellZ) {
    }

    /** The districts each player was last sent. */
    private static final Map<UUID, Set<DistrictKey>> sent = new HashMap<>();
    private static int ticks;

    private Townsfolk() {
    }

    public static void tick(MinecraftServer server) {
        if (++ticks % EVERY != 0) {
            return;
        }
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return;
        }
        long seed = server.overworld().getSeed();
        Set<UUID> online = new java.util.HashSet<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            online.add(player.getUUID());
            Map<DistrictKey, CityLayout.Site> near = player.level().dimension() == Level.OVERWORLD
                    ? near(seed, settings, player.getBlockX(), player.getBlockZ()) : Map.of();
            if (near.keySet().equals(sent.get(player.getUUID()))) {
                continue;
            }
            sent.put(player.getUUID(), near.keySet());
            List<StreetWalks.Walk> walks = new ArrayList<>();
            near.forEach((k, city) -> walks.addAll(StreetWalks.walks(city, k.cellX(), k.cellZ())));
            ConquestNetwork.sendStreets(player, walks);
        }
        sent.keySet().retainAll(online);
    }

    /** The nearest few districts within {@link #NEAR} of (px, pz): a capital's every district at once is too many people. */
    private static Map<DistrictKey, CityLayout.Site> near(long seed, WorldSettings settings, int px, int pz) {
        Map<DistrictKey, CityLayout.Site> near = new HashMap<>();
        Map<DistrictKey, Double> distance = new HashMap<>();
        int reach = NEAR + CityLayout.MAX_HALF_EXTENT;
        for (CityLayout.Site city : CityLayout.allSitesInBox(seed, settings, px - reach, pz - reach, px + reach, pz + reach)) {
            for (int cx = 0; cx < city.cells(); cx++) {
                for (int cz = 0; cz < city.cells(); cz++) {
                    int x0 = city.originX() + CityLayout.cellMin(cx);
                    int x1 = city.originX() + CityLayout.cellMax(cx);
                    int z0 = city.originZ() + CityLayout.cellMin(cz);
                    int z1 = city.originZ() + CityLayout.cellMax(cz);
                    double dx = Math.max(0, Math.max(x0 - px, px - x1));
                    double dz = Math.max(0, Math.max(z0 - pz, pz - z1));
                    double d = dx * dx + dz * dz;
                    if (d <= NEAR * NEAR) {
                        DistrictKey k = new DistrictKey(city.x(), city.z(), cx, cz);
                        near.put(k, city);
                        distance.put(k, d);
                    }
                }
            }
        }
        if (near.size() > MAX_DISTRICTS) {
            Set<DistrictKey> keep = distance.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(MAX_DISTRICTS)
                    .map(Map.Entry::getKey).collect(Collectors.toSet());
            near.keySet().retainAll(keep);
        }
        return near;
    }

    /** The server is stopping: forget what each player was sent. */
    public static void clear() {
        sent.clear();
        ticks = 0;
    }
}
