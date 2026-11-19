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

    /** The succession last sent to everyone: it grows with the world's age, so it is sent again only when it changes. */
    private static byte[] lastSuccession = new byte[0];

    public static void toAll(MinecraftServer server) {
        ConquestNetwork.PoliticsView view = view(server);
        if (java.util.Arrays.equals(view.succession(), lastSuccession)) {
            // Empty: the clients keep the succession they have (a real one is never empty).
            view = new ConquestNetwork.PoliticsView(view.wars(), view.armies(), view.alliances(), view.conquered(), view.news(),
                    view.clashes(), view.clashNames(), new byte[0]);
        } else {
            lastSuccession = view.succession();
        }
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
        List<int[]> armies = new ArrayList<>();
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        double now = Wars.now(server);
        for (int i = 0; i < politics.wars().size(); i++) {
            War w = politics.wars().get(i);
            int counties = map == null ? 1 : w.goal().counties(MapLands.of(map)).size();
            wars.add(new int[]{w.attacker(), w.defender(), w.goal().duchy() ? 1 : 0, w.goal().index(), w.target(), w.taken().size(), counties,
                    (int) w.siteX(), (int) w.siteZ()});
            // Each side's army and fleet: [nation, soldiers, x, z, state (0 marching, 1 mustering, 2 waiting while the
            // other force fights), war, toX, toZ, fleet (1) or army (0), ships (a fleet's; 0 for an army)].
            for (boolean attacking : new boolean[]{true, false}) {
                for (Army army : new Army[]{w.land(attacking), w.fleet(attacking)}) {
                    if (army == null) {
                        continue;
                    }
                    boolean active = army == w.army(attacking);
                    double[] to = active ? army.heading(w) : new double[]{army.x(), army.z()};
                    armies.add(new int[]{attacking ? w.attacker() : w.defender(), army.soldiers(), (int) army.x(), (int) army.z(),
                            !active ? 2 : army.mustering(now) ? 1 : 0, i, (int) to[0], (int) to[1], army == w.fleet(attacking) ? 1 : 0,
                            army == w.fleet(attacking) ? army.ships() : 0});
                }
            }
        }
        List<String> news = new ArrayList<>();
        List<Politics.Event> chronicle = politics.chronicle();
        for (int i = Math.max(0, chronicle.size() - NEWS); i < chronicle.size(); i++) {
            news.add("Day " + chronicle.get(i).day() + ": " + chronicle.get(i).text());
        }
        List<int[]> clashes = new ArrayList<>();
        List<String> clashNames = new ArrayList<>();
        for (Feud f : politics.feuds()) {
            Feud.Clash c = f.clash();
            if (c != null && map != null) {
                Politics.Front front = Wars.front(server, map);
                MapLands lands = MapLands.of(map);
                clashes.add(new int[]{c.kind() == Feud.Kind.DUEL ? 1 : 0, (int) c.x(), (int) c.z()});
                clashNames.add((c.kind() == Feud.Kind.DUEL ? "Duel: " : "Skirmish: ") + Feuds.name(f.lord(true), lands, front) + " against "
                        + Feuds.name(f.lord(false), lands, front));
            }
        }
        return new ConquestNetwork.PoliticsView(wars, armies, politics.alliances(), politics.conquered(), news, clashes, clashNames,
                politics.succession().encode());
    }
}
