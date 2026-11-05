package rafasconquest.peerage;

import net.minecraft.server.level.ServerPlayer;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;

/** The grand strategy view: the player's fief, from the titles they hold, shown over the world map. */
public final class GrandStrategy {
    /** Each row a label and its value; the seat province of the held county (-1: no fief). */
    public record View(List<String[]> rows, int seat) {
    }

    private GrandStrategy() {
    }

    public static void open(ServerPlayer player) {
        ConquestNetwork.sendGrandStrategy(player, view(player));
    }

    static View view(ServerPlayer player) {
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        BaseMap map = ConquestRuntime.map();
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Ruler", Names.player(player.getName().getString(), peer.title)});
        if (peer.hold < 0 || map == null) {
            rows.add(new String[]{"Titles", "None: you hold no land"});
            return new View(rows, -1);
        }
        rows.add(new String[]{"Titles", "County of " + map.names().get(peer.hold)});
        int people = map.realm().people().size();
        if (peer.liege >= 0 && peer.liege < people) {
            rows.add(new String[]{"Liege", map.realm().people().get(peer.liege).name()});
        }
        List<String> vassals = peer.vassals.stream().filter(v -> v >= 0 && v < people)
                .map(v -> map.realm().people().get(v).name()).toList();
        rows.add(new String[]{"Vassals", vassals.isEmpty() ? "None" : String.join(", ", vassals)});
        rows.add(new String[]{"Retinue", peer.retinue.size() + " of " + Retinues.size(Retinues.level(peer.title))});
        return new View(rows, peer.hold);
    }
}
