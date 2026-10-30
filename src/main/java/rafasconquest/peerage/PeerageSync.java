package rafasconquest.peerage;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.net.ConquestNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Tells clients which counties players hold, and what befell the characters they replaced, for the map and pages. */
public final class PeerageSync {
    private PeerageSync() {
    }

    public static void toAll(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            to(player);
        }
    }

    public static void to(ServerPlayer player) {
        Peerage peerage = Peerage.get(player.server);
        Map<Integer, String> holders = new HashMap<>();
        for (Map.Entry<Integer, UUID> e : peerage.holdings().entrySet()) {
            Peerage.Peer peer = peerage.of(e.getValue());
            holders.put(e.getKey(), Names.player(peer.name, peer.title));
        }
        ConquestNetwork.sendPeerage(player, holders, new HashMap<>(peerage.fates()));
    }
}
