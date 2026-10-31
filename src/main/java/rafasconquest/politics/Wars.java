package rafasconquest.politics;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.Random;
import java.util.UUID;

/**
 * The politics in the running world: each new day (by the overworld's clock, not days caught up while away) the nations
 * act ({@link Politics#day}); a war's battle near a player is fought for real, with the battle system, and its result
 * goes back to the war; enlisted players are called to muster when their nation goes to war; and heralds tell everyone
 * the day before's news. Conquests change the map's holders, here and on the clients.
 */
public final class Wars {
    /** A war's battle within this many blocks of a player is fought for real. */
    static final int REAL_RANGE = 400;
    /** The most soldiers a side brings to a real battle (the rest are elsewhere): the server's limit, not the war's. */
    static final int REAL_CAP = 1000;

    /** The war whose battle is being fought for real, and its sides' factions. */
    private static long pendingWar = -1;
    @Nullable
    private static UUID pendingAttacker;

    private Wars() {
    }

    /** Puts the conquests back on a freshly loaded map. */
    public static void restore(net.minecraft.server.level.ServerLevel overworld, BaseMap map) {
        PoliticsData.get(overworld).politics.conquered().forEach((p, n) -> {
            if (p < map.holders().length) {
                map.holders()[p] = n;
            }
        });
    }

    /** A new day has come. */
    public static void day(MinecraftServer server, long day) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return;
        }
        PoliticsData data = PoliticsData.get(server);
        if (pendingWar >= 0 && !RecruitsBattle.running()) {
            data.politics.realBattleCancelled(pendingWar); // stopped, or never begun
            pendingWar = -1;
        }
        Random random = new Random(server.overworld().getSeed() ^ day * 0x9E3779B97F4A7C15L);
        data.politics.day(day, MapLands.of(map), random, front(server, map, day));
        data.setDirty();
        claimCities(map, data.politics);
        heralds(server, data.politics, day);
        PoliticsSync.toAll(server);
    }

    private static Politics.Front front(MinecraftServer server, BaseMap map, long day) {
        return new Politics.Front() {
            @Override
            public boolean fightForReal(War war, int[] site, int attackers, int defenders) {
                return fight(server, map, war, site, attackers, defenders);
            }

            @Override
            public void declared(War war) {
                muster(server, map, war);
            }
        };
    }

    /** Fights a war's battle for real if a player is near its site and no other battle is on. */
    private static boolean fight(MinecraftServer server, BaseMap map, War war, int[] site, int attackers, int defenders) {
        if (RecruitsBattle.running() || pendingWar >= 0) {
            return false;
        }
        ServerPlayer near = null;
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level() == server.overworld() && Math.hypot(p.getX() - site[0], p.getZ() - site[1]) <= REAL_RANGE) {
                near = p;
            }
        }
        ConquestServer conquest = ConquestServer.get();
        if (near == null || conquest == null) {
            return false;
        }
        Faction a = conquest.data().faction(map.nations().get(war.attacker()).id());
        Faction d = conquest.data().faction(map.nations().get(war.defender()).id());
        if (a == null || d == null) {
            return false;
        }
        double scale = Math.min(1, REAL_CAP / (double) Math.max(attackers, defenders));
        int[] from = MapLands.of(map).centre(war.goal());
        double ax = from[0] - site[0];
        double az = from[1] - site[1];
        double length = Math.max(1, Math.hypot(ax, az));
        try {
            RecruitsBattle.start(server.overworld(), a, Math.max(20, (int) (attackers * scale)), d, Math.max(20, (int) (defenders * scale)),
                    new BattlePlan.Point(site[0], site[1]), ax / length, az / length,
                    line -> server.getPlayerList().broadcastSystemMessage(Component.literal("[War] " + a.name() + " and " + d.name()
                            + " meet in battle near you, at " + map.names().get(war.goal()) + ". " + line).withStyle(ChatFormatting.RED), false),
                    why -> {
                        RafasConquest.LOGGER.warn("[war] The battle of {} could not be fought for real: {}", map.names().get(war.goal()), why);
                        PoliticsData.get(server).politics.realBattleCancelled(war.id());
                        pendingWar = -1;
                    });
        } catch (IllegalStateException e) {
            return false;
        }
        pendingWar = war.id();
        pendingAttacker = a.id();
        return true;
    }

    /** A battle fought for real is over: if it was a war's, its result goes to the war. */
    public static void battleOver(UUID first, UUID second, int winner) {
        if (pendingWar < 0) {
            return;
        }
        ConquestServer conquest = ConquestServer.get();
        MinecraftServer server = conquest == null ? null : conquest.overworld().getServer();
        if (server == null || !first.equals(pendingAttacker) && !second.equals(pendingAttacker)) {
            return;
        }
        PoliticsData data = PoliticsData.get(server);
        long war = pendingWar;
        pendingWar = -1;
        if (winner < 0) {
            data.politics.realBattleCancelled(war);
        } else {
            UUID won = winner == 0 ? first : second;
            data.politics.realBattle(war, won.equals(pendingAttacker), conquest.overworld().getDayTime() / 24000L, MapLands.of(ConquestRuntime.map()));
        }
        data.setDirty();
        claimCities(ConquestRuntime.map(), data.politics);
        PoliticsSync.toAll(server);
    }

    /** A conquered province's city is its conqueror's: its record (which its guards, recruiters and banners go by) says so. */
    private static void claimCities(BaseMap map, Politics politics) {
        ConquestServer conquest = ConquestServer.get();
        if (conquest == null) {
            return;
        }
        for (rafasconquest.core.SeatCities.City city : map.cities()) {
            Integer n = politics.conquered().get(city.province());
            if (n == null) {
                continue;
            }
            net.minecraft.world.level.ChunkPos chunk = new net.minecraft.world.level.ChunkPos(city.x() >> 4, city.z() >> 4);
            rafasconquest.poi.Poi poi = conquest.data().poiInChunk(chunk, rafasconquest.poi.PoiType.CITY);
            poi = poi != null ? poi : conquest.data().poiInChunk(chunk, rafasconquest.poi.PoiType.CAPITAL);
            UUID owner = map.nations().get(n).id();
            if (poi != null && !owner.equals(poi.owner())) {
                poi.setOwner(owner);
                conquest.data().setDirty();
            }
        }
    }

    /** Calls the enlisted players of every nation in a new war to muster at their capital. */
    private static void muster(MinecraftServer server, BaseMap map, War war) {
        ConquestServer conquest = ConquestServer.get();
        if (conquest == null) {
            return;
        }
        for (boolean attacking : new boolean[]{true, false}) {
            for (int n : war.side(attacking)) {
                Faction nation = conquest.data().faction(map.nations().get(n).id());
                if (nation == null) {
                    continue;
                }
                String enemy = map.nations().get(attacking ? war.defender() : war.attacker()).name();
                String capital = map.names().get(map.nations().get(n).seat());
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (rafasconquest.peerage.Talk.serves(p, nation)) {
                        p.sendSystemMessage(Component.literal("War! " + nation.name() + " is at war with " + enemy + ", over "
                                + map.names().get(war.goal()) + ". The army musters at " + capital + ".").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                    }
                }
            }
        }
    }

    /** The wars, alliances, conquests and latest news, for {@code /pve politics}. */
    public static String report(MinecraftServer server) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        Politics politics = PoliticsData.get(server).politics;
        StringBuilder out = new StringBuilder();
        out.append(politics.wars().size()).append(" war(s):");
        for (War w : politics.wars()) {
            out.append("\n  ").append(map.nations().get(w.attacker()).name()).append(" against ").append(map.nations().get(w.defender()).name())
                    .append(" over ").append(map.names().get(w.goal())).append(", score ").append(w.score()).append(", ").append(w.battles())
                    .append(" battle(s) since day ").append(w.started());
        }
        out.append("\n").append(politics.alliances().size()).append(" alliance(s):");
        for (int[] a : politics.alliances()) {
            out.append("\n  ").append(map.nations().get(a[0]).name()).append(" and ").append(map.nations().get(a[1]).name());
        }
        out.append("\n").append(politics.conquered().size()).append(" province(s) conquered");
        java.util.List<Politics.Event> news = politics.chronicle();
        for (int i = Math.max(0, news.size() - 5); i < news.size(); i++) {
            out.append("\n  Day ").append(news.get(i).day()).append(": ").append(news.get(i).text());
        }
        return out.toString();
    }

    /** Runs {@code days} days of politics at once, from today (for {@code /pve politics day}). */
    public static String runDays(MinecraftServer server, int days) {
        long today = server.overworld().getDayTime() / 24000L;
        int before = PoliticsData.get(server).politics.chronicle().size();
        for (int i = 0; i < days; i++) {
            day(server, today + i);
        }
        int happened = PoliticsData.get(server).politics.chronicle().size() - before;
        return days + " day(s) of politics run: " + Math.max(0, happened) + " event(s). /pve politics to see them";
    }

    /** One nation declares war on another now, by name (for {@code /pve politics war}). */
    public static String forceWar(MinecraftServer server, String attacker, String defender) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        int a = nationNamed(map, attacker);
        int d = nationNamed(map, defender);
        if (a < 0 || d < 0 || a == d) {
            return "No such pair of nations";
        }
        PoliticsData data = PoliticsData.get(server);
        long today = server.overworld().getDayTime() / 24000L;
        War war = data.politics.declare(a, d, today, MapLands.of(map), new Random(), front(server, map, today));
        data.setDirty();
        PoliticsSync.toAll(server);
        return war == null ? map.nations().get(a).name() + " has no border with " + map.nations().get(d).name() + " to fight over"
                : map.nations().get(a).name() + " declared war on " + map.nations().get(d).name() + " over " + map.names().get(war.goal());
    }

    private static int nationNamed(BaseMap map, String name) {
        for (int n = 0; n < map.nations().size(); n++) {
            if (map.nations().get(n).name().equalsIgnoreCase(name.trim())) {
                return n;
            }
        }
        return -1;
    }

    /** The heralds: yesterday's news, today. */
    private static void heralds(MinecraftServer server, Politics politics, long day) {
        for (Politics.Event e : politics.chronicle()) {
            if (e.day() == day - 1) {
                server.getPlayerList().broadcastSystemMessage(Component.literal("[Herald] ").withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("Yesterday: " + e.text()).withStyle(ChatFormatting.YELLOW)), false);
            }
        }
    }
}
