package rafasconquest.politics;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.sim.ConquestServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A bar at the top of the screen for the members of a nation whose army is mustering: where it musters, and how long
 * until it marches (a day of mustering, {@link Politics#MUSTER}), filling as the time passes. Gone once it marches.
 */
final class MusterBars {
    /** How often the bars are brought up to date, in ticks. */
    static final int EVERY = 20;
    /** Real seconds in an in-game day. */
    private static final double SECONDS_PER_DAY = 1200;

    /** By war and side ("id:a" or "id:d"). */
    private static final Map<String, ServerBossEvent> BARS = new HashMap<>();

    private MusterBars() {
    }

    static void update(MinecraftServer server, BaseMap map, Politics politics) {
        ConquestServer conquest = ConquestServer.get();
        if (conquest == null) {
            return;
        }
        double now = Wars.now(server);
        Set<String> live = new HashSet<>();
        for (War war : politics.wars()) {
            for (boolean attacking : new boolean[]{true, false}) {
                Army army = war.army(attacking);
                if (army == null || !army.mustering(now)) {
                    continue;
                }
                String key = war.id() + (attacking ? ":a" : ":d");
                live.add(key);
                int leader = attacking ? war.attacker() : war.defender();
                ServerBossEvent bar = BARS.computeIfAbsent(key, k -> new ServerBossEvent(Component.empty(),
                        BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10));
                double left = Math.max(0, army.readyAt - now);
                int seconds = (int) Math.ceil(left * SECONDS_PER_DAY);
                bar.setName(Component.literal(map.nations().get(leader).name() + "'s army musters at "
                        + army.place() + String.format(": it marches in %d:%02d", seconds / 60, seconds % 60)));
                bar.setProgress((float) Math.max(0, Math.min(1, 1 - left / Politics.MUSTER)));
                // Its nations' members see it; no one else.
                Set<Faction> side = new HashSet<>();
                for (int n : war.side(attacking)) {
                    Faction f = conquest.data().faction(map.nations().get(n).id());
                    if (f != null) {
                        side.add(f);
                    }
                }
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    boolean member = side.stream().anyMatch(f -> rafasconquest.peerage.Talk.serves(p, f));
                    if (member) {
                        bar.addPlayer(p);
                    } else {
                        bar.removePlayer(p);
                    }
                }
            }
        }
        BARS.entrySet().removeIf(e -> {
            if (live.contains(e.getKey())) {
                return false;
            }
            e.getValue().removeAllPlayers();
            return true;
        });
    }

    /** On leaving a world. */
    static void clear() {
        BARS.values().forEach(ServerBossEvent::removeAllPlayers);
        BARS.clear();
    }
}
