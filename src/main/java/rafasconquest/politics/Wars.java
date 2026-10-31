package rafasconquest.politics;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.BattlePlan;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * The politics in the running world. Each new day (by the overworld's clock, not days caught up while away) the
 * nations act ({@link Politics#day}); every few seconds the armies march on ({@link Politics#advance}). When a side
 * raises an army its enlisted players are called to muster: answering ({@code /pve muster}) takes them to their army, at
 * their capital or on the march, and they go with it into battle. A battle with a player near it is fought for real, by
 * the battle system, and its result goes back to the war. Heralds tell everyone the day before's news. Conquests change
 * the map's holders, here and on the clients.
 */
public final class Wars {
    /** A war's battle within this many blocks of a player is fought for real. */
    static final int REAL_RANGE = 400;
    /** The most soldiers a side brings to a real battle (the rest are elsewhere): the server's limit, not the war's. */
    static final int REAL_CAP = 1000;
    /** How often the armies march on, in ticks. */
    static final int MARCH_TICKS = 100;
    /** How far behind its lines a mustered player is brought to the battle. */
    static final int BEHIND_LINES = 70;

    /** The war whose battle is being fought for real, and its attacker's faction. */
    private static long pendingWar = -1;
    @Nullable
    private static UUID pendingAttacker;
    /** The players who answered a muster call: by war, and on which side (true: attacking). */
    private static final Map<UUID, long[]> mustered = new HashMap<>();

    private Wars() {
    }

    /** Puts the conquests back on a freshly loaded map. */
    public static void restore(ServerLevel overworld, BaseMap map) {
        PoliticsData.get(overworld).politics.conquered().forEach((p, n) -> {
            if (p < map.holders().length) {
                map.holders()[p] = n;
            }
        });
        mustered.clear();
        pendingWar = -1;
        MusterBars.clear();
    }

    /** The time now, in days. */
    static double now(MinecraftServer server) {
        return server.overworld().getDayTime() / 24000.0;
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
        data.politics.day(day, MapLands.of(map), random, front(server, map));
        data.setDirty();
        claimCities(map, data.politics);
        heralds(server, data.politics, day);
        PoliticsSync.toAll(server);
    }

    /** Every tick: every few seconds, the armies march on, and battles are fought where they meet. */
    public static void tick(MinecraftServer server) {
        BaseMap map = ConquestRuntime.map();
        if (map != null && server.overworld().getGameTime() % MusterBars.EVERY == 0) {
            MusterBars.update(server, map, PoliticsData.get(server).politics);
        }
        if (map == null || server.overworld().getGameTime() % MARCH_TICKS != 0) {
            return;
        }
        PoliticsData data = PoliticsData.get(server);
        if (data.politics.wars().isEmpty()) {
            return;
        }
        Random random = new Random(server.overworld().getGameTime() ^ server.overworld().getSeed());
        int events = data.politics.chronicle().size();
        data.politics.advance(now(server), MapLands.of(map), random, front(server, map));
        data.setDirty();
        if (data.politics.chronicle().size() != events) {
            claimCities(map, data.politics);
        }
        PoliticsSync.toAll(server); // the armies have moved
    }

    private static Politics.Front front(MinecraftServer server, BaseMap map) {
        return new Politics.Front() {
            @Override
            public boolean fightForReal(War war, int attackers, int defenders) {
                return fight(server, map, war, attackers, defenders);
            }

            @Override
            public void raised(War war, boolean attacking) {
                call(server, map, war, attacking);
            }
        };
    }

    /** The way from the attacker's side of a battle toward the defender's: from their capital toward the site. */
    private static double[] axis(BaseMap map, War war) {
        BaseMap.Nation a = map.nations().get(war.attacker());
        double dx = war.siteX() - a.x();
        double dz = war.siteZ() - a.z();
        double length = Math.max(1, Math.hypot(dx, dz));
        return new double[]{dx / length, dz / length};
    }

    /**
     * Fights a war's battle for real if a player is near its site (those who mustered with its armies are brought there
     * first) and no other battle is on.
     */
    private static boolean fight(MinecraftServer server, BaseMap map, War war, int attackers, int defenders) {
        if (RecruitsBattle.running() || pendingWar >= 0) {
            return false;
        }
        ConquestServer conquest = ConquestServer.get();
        Faction a = conquest == null ? null : conquest.data().faction(map.nations().get(war.attacker()).id());
        Faction d = conquest == null ? null : conquest.data().faction(map.nations().get(war.defender()).id());
        if (a == null || d == null) {
            return false;
        }
        double[] axis = axis(map, war);
        bringToBattle(server, war, axis);
        boolean near = false;
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level() == server.overworld() && Math.hypot(p.getX() - war.siteX(), p.getZ() - war.siteZ()) <= REAL_RANGE) {
                near = true;
            }
        }
        if (!near) {
            return false;
        }
        double scale = Math.min(1, REAL_CAP / (double) Math.max(attackers, defenders));
        String county = MapLands.of(map).titleName(new Goal(false, war.target()));
        try {
            RecruitsBattle.start(server.overworld(), a, Math.max(20, (int) (attackers * scale)), d, Math.max(20, (int) (defenders * scale)),
                    new BattlePlan.Point(war.siteX(), war.siteZ()), axis[0], axis[1], general(map, war, true), general(map, war, false),
                    line -> server.getPlayerList().broadcastSystemMessage(Component.literal("[War] The armies of " + a.name() + " and "
                            + d.name() + " meet in battle for the " + county + ". " + line).withStyle(ChatFormatting.RED), false),
                    why -> {
                        RafasConquest.LOGGER.warn("[war] The battle for the {} could not be fought for real: {}", county, why);
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

    /** A side's chosen general by name, or null (the battle picks its best). */
    @Nullable
    private static String general(BaseMap map, War war, boolean attacking) {
        int g = war.general(attacking);
        return g < 0 || g >= map.realm().people().size() ? null : map.realm().people().get(g).name();
    }

    /** The players who mustered with a war's armies come with them to the battle, behind their own lines. */
    private static void bringToBattle(MinecraftServer server, War war, double[] axis) {
        ServerLevel level = server.overworld();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            long[] answer = mustered.get(p.getUUID());
            if (answer == null || answer[0] != war.id()) {
                continue;
            }
            double side = answer[1] == 1 ? -1 : 1;
            int x = (int) Math.round(war.siteX() + axis[0] * BEHIND_LINES * side);
            int z = (int) Math.round(war.siteZ() + axis[1] * BEHIND_LINES * side);
            teleport(p, level, x, z);
            p.sendSystemMessage(Component.literal("Your army has reached the field of battle.").withStyle(ChatFormatting.RED));
        }
    }

    private static void teleport(ServerPlayer p, ServerLevel level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        p.teleportTo(level, x + 0.5, y, z + 0.5, p.getYRot(), p.getXRot());
    }

    /** A battle fought for real is over: if it was a war's, its result goes to the war. */
    public static void battleOver(UUID first, UUID second, int winner, double firstLeft, double secondLeft) {
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
        BaseMap map = ConquestRuntime.map();
        if (winner < 0 || map == null) {
            data.politics.realBattleCancelled(war);
        } else {
            boolean attackerFirst = first.equals(pendingAttacker);
            UUID won = winner == 0 ? first : second;
            data.politics.realBattle(war, won.equals(pendingAttacker), attackerFirst ? firstLeft : secondLeft,
                    attackerFirst ? secondLeft : firstLeft, now(server), MapLands.of(map),
                    new Random(server.overworld().getGameTime()), front(server, map));
            claimCities(map, data.politics);
        }
        data.setDirty();
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

    /** Calls the enlisted players of a side's nations to muster: a line, and a button that answers. */
    private static void call(MinecraftServer server, BaseMap map, War war, boolean attacking) {
        ConquestServer conquest = ConquestServer.get();
        if (conquest == null) {
            return;
        }
        Goal goal = war.goal();
        String title = MapLands.of(map).titleName(goal);
        for (int n : war.side(attacking)) {
            Faction nation = conquest.data().faction(map.nations().get(n).id());
            if (nation == null) {
                continue;
            }
            String enemy = map.nations().get(attacking ? war.defender() : war.attacker()).name();
            Army army = war.army(attacking);
            String capital = army != null ? army.place() : map.names().get(map.nations().get(attacking ? war.attacker() : war.defender()).seat());
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                if (rafasconquest.peerage.Talk.serves(p, nation)) {
                    p.sendSystemMessage(Component.literal("To arms! " + nation.name() + " raises an army against " + enemy + ", for the "
                            + title + ". It musters at " + capital + " for a day, then marches. ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
                            .append(Component.literal("[Muster]").withStyle(style -> style.withColor(ChatFormatting.GOLD).withUnderlined(true)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pve muster")))));
                }
            }
        }
    }

    /** A player answers the call: to their side's army, at its capital or on the march. */
    public static String muster(ServerPlayer player) {
        BaseMap map = ConquestRuntime.map();
        ConquestServer conquest = ConquestServer.get();
        if (map == null || conquest == null) {
            return "No world map yet";
        }
        for (War war : PoliticsData.get(player.server).politics.wars()) {
            for (boolean attacking : new boolean[]{true, false}) {
                Army army = war.army(attacking);
                if (army == null) {
                    continue;
                }
                for (int n : war.side(attacking)) {
                    Faction nation = conquest.data().faction(map.nations().get(n).id());
                    if (nation != null && rafasconquest.peerage.Talk.serves(player, nation)) {
                        mustered.put(player.getUUID(), new long[]{war.id(), attacking ? 1 : 0});
                        teleport(player, player.server.overworld(), (int) Math.round(army.x()), (int) Math.round(army.z()));
                        return army.mustering(now(player.server)) ? "You have mustered with the army of " + nation.name()
                                + ". It marches at dawn; you will go with it into battle"
                                : "You have joined the army of " + nation.name() + " on the march; you will go with it into battle";
                    }
                }
            }
        }
        return "Your nation has no army in the field";
    }

    /**
     * Ends the mustering of every army still at its capital now, or only those of the sides {@code nation} (a name) fights
     * on; they march at once (for {@code /pve politics muster finish}).
     */
    public static String finishMuster(MinecraftServer server, @Nullable String nation) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        int only = nation == null ? -1 : nationNamed(map, nation);
        if (nation != null && only < 0) {
            return "No such nation";
        }
        PoliticsData data = PoliticsData.get(server);
        double now = now(server);
        int ended = 0;
        for (War war : data.politics.wars()) {
            for (boolean attacking : new boolean[]{true, false}) {
                Army army = war.army(attacking);
                if (army != null && army.mustering(now) && (only < 0 || war.side(attacking).contains(only))) {
                    army.readyAt = now;
                    ended++;
                }
            }
        }
        data.setDirty();
        MusterBars.update(server, map, data.politics);
        PoliticsSync.toAll(server);
        return ended == 0 ? "No army is mustering" : ended + " army(ies) done mustering: they march now";
    }

    /** The wars, alliances, conquests and latest news, for {@code /pve politics}. */
    public static String report(MinecraftServer server) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        Politics politics = PoliticsData.get(server).politics;
        MapLands lands = MapLands.of(map);
        StringBuilder out = new StringBuilder();
        out.append(politics.wars().size()).append(" war(s):");
        double now = now(server);
        for (War w : politics.wars()) {
            out.append("\n  ").append(map.nations().get(w.attacker()).name()).append(" against ").append(map.nations().get(w.defender()).name())
                    .append(" for the ").append(lands.titleName(w.goal())).append(": ").append(w.taken().size()).append(" of ")
                    .append(w.goal().counties(lands).size()).append(" counties taken, ").append(w.battles()).append(" battle(s); next for the ")
                    .append(lands.titleName(new Goal(false, w.target()))).append(String.format(" at %.0f, %.0f", w.siteX(), w.siteZ()));
            for (boolean attacking : new boolean[]{true, false}) {
                Army army = w.army(attacking);
                if (army != null) {
                    out.append("\n    ").append(map.nations().get(attacking ? w.attacker() : w.defender()).name()).append(": ").append(army.soldiers())
                            .append(" soldiers, ").append(army.mustering(now) ? "mustering" : String.format("at %.0f, %.0f", army.x(), army.z()));
                }
            }
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

    /** Runs {@code days} days of politics at once, armies marching through them (for {@code /pve politics day}). */
    public static String runDays(MinecraftServer server, int days) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        PoliticsData data = PoliticsData.get(server);
        double start = now(server);
        int before = data.politics.chronicle().size();
        Random random = new Random();
        for (int i = 0; i < days; i++) {
            data.politics.day((long) start + i, MapLands.of(map), random, front(server, map));
            for (int step = 1; step <= 20; step++) {
                data.politics.advance(start + i + step / 20.0, MapLands.of(map), random, front(server, map));
            }
        }
        data.setDirty();
        claimCities(map, data.politics);
        PoliticsSync.toAll(server);
        int happened = data.politics.chronicle().size() - before;
        return days + " day(s) of politics run: " + Math.max(0, happened) + " event(s). /pve politics to see them";
    }

    /**
     * One nation declares war on another now, for a title of the other's beside its land (for {@code /pve politics war}),
     * each side led by the general named (one of its own characters), or by its best at war if none is.
     */
    public static String forceWar(MinecraftServer server, String attacker, String defender, @Nullable String attackerGeneral,
                                  @Nullable String defenderGeneral) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        int a = nationNamed(map, attacker);
        int d = nationNamed(map, defender);
        if (a < 0 || d < 0 || a == d) {
            return "No such pair of nations";
        }
        int ga = attackerGeneral == null ? -1 : personNamed(map, a, attackerGeneral);
        int gd = defenderGeneral == null ? -1 : personNamed(map, d, defenderGeneral);
        if (attackerGeneral != null && ga < 0 || defenderGeneral != null && gd < 0) {
            return "No character of that name serves " + map.nations().get(attackerGeneral != null && ga < 0 ? a : d).name();
        }
        MapLands lands = MapLands.of(map);
        Politics.Claim best = null;
        for (Politics.Claim claim : Politics.claims(a, lands)) {
            if (claim.defender() == d && (best == null || claim.want() > best.want())) {
                best = claim;
            }
        }
        if (best == null) {
            return map.nations().get(d).name() + " holds no title beside " + map.nations().get(a).name() + " to fight for";
        }
        PoliticsData data = PoliticsData.get(server);
        War war = data.politics.declare(a, d, best.goal(), (long) now(server), lands, new Random(), front(server, map));
        if (war != null) {
            war.attackerGeneral = ga;
            war.defenderGeneral = gd;
        }
        data.setDirty();
        PoliticsSync.toAll(server);
        return war == null ? map.nations().get(a).name() + " could not raise an army"
                : map.nations().get(a).name() + " declared war on " + map.nations().get(d).name() + " for the " + lands.titleName(war.goal())
                + (ga >= 0 ? "; " + map.realm().people().get(ga).name() + " leads the attack" : "")
                + (gd >= 0 ? "; " + map.realm().people().get(gd).name() + " leads the defence" : "");
    }

    /** One of a nation's characters by name, or -1. */
    private static int personNamed(BaseMap map, int nation, String name) {
        java.util.List<rafasconquest.people.Person> people = map.realm().people();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).nation() == nation && people.get(i).name().equalsIgnoreCase(name.trim())) {
                return i;
            }
        }
        return -1;
    }

    /** A nation's characters' names, best at war first (for the war command's suggestions). */
    public static java.util.List<String> generals(String nation) {
        BaseMap map = ConquestRuntime.map();
        int n = map == null ? -1 : nationNamed(map, nation);
        if (n < 0) {
            return java.util.List.of();
        }
        return map.realm().people().stream().filter(p -> p.nation() == n)
                .sorted(java.util.Comparator.comparingInt((rafasconquest.people.Person p) -> -p.stat(rafasconquest.people.Stat.MARTIAL)))
                .map(rafasconquest.people.Person::name).toList();
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
