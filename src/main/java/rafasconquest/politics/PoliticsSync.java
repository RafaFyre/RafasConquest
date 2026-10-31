package rafasconquest.politics;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.List;

/** Sends the politics to the players: on joining, and whenever a day or a battle changes them. */
public final class PoliticsSync {
    /** How much of the news a client keeps. */
    static final int NEWS = 12;

    private PoliticsSync() {
    }

    public static void toAll(MinecraftServer server) {
        ConquestNetwork.PoliticsView view = view(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ConquestNetwork.sendPolitics(player, view);
        }
    }

    public static void to(ServerPlayer player) {
        ConquestNetwork.sendPolitics(player, view(player.server));
    }

    private static ConquestNetwork.PoliticsView view(MinecraftServer server) {
        Politics politics = PoliticsData.get(server).politics;
        List<int[]> wars = new ArrayList<>();
        for (War w : politics.wars()) {
            wars.add(new int[]{w.attacker(), w.defender(), w.goal(), w.score()});
        }
        List<String> news = new ArrayList<>();
        List<Politics.Event> chronicle = politics.chronicle();
        for (int i = Math.max(0, chronicle.size() - NEWS); i < chronicle.size(); i++) {
            news.add("Day " + chronicle.get(i).day() + ": " + chronicle.get(i).text());
        }
        return new ConquestNetwork.PoliticsView(wars, politics.alliances(), politics.conquered(), news);
    }
}
