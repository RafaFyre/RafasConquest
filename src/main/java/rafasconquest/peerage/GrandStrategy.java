package rafasconquest.peerage;

import net.minecraft.server.level.ServerPlayer;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

/** The grand strategy view: the world map with the player's realm and resources laid over it. */
public final class GrandStrategy {
    /** The seat province of the held county (-1: no fief), the nation the player serves (-1: none), and the day. */
    public record View(int seat, int nation, long day) {
    }

    private GrandStrategy() {
    }

    public static void open(ServerPlayer player) {
        ConquestNetwork.sendGrandStrategy(player, view(player));
    }

    static View view(ServerPlayer player) {
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        ConquestServer server = ConquestServer.get();
        return new View(peer.hold, nation(player, ConquestRuntime.map(), server), server == null ? 0 : server.data().lastProcessedDay());
    }

    private static int nation(ServerPlayer player, BaseMap map, ConquestServer server) {
        if (map == null || server == null) {
            return -1;
        }
        for (int i = 0; i < map.nations().size(); i++) {
            var f = server.data().faction(map.nations().get(i).id());
            if (f != null && Talk.serves(player, f)) {
                return i;
            }
        }
        return -1;
    }
}
