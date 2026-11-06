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
import net.minecraft.util.Mth;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattleType;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        PoliticsData.get(overworld).politics.succession().apply(map.realm());
        MapLands.of(map).regroup();
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
        if (pendingFeud >= 0 && !RecruitsBattle.running()) {
            data.politics.realClashCancelled(pendingFeud);
            pendingFeud = -1;
        }
        if (feudTeam != null && !RecruitsBattle.running()) {
            new rafasconquest.bridge.RecruitsFactions(server.overworld()).removeFeudTeam(feudTeam);
            feudTeam = null;
        }
        Random random = new Random(server.overworld().getSeed() ^ day * 0x9E3779B97F4A7C15L);
        data.politics.day(day, MapLands.of(map), random, front(server, map));
        rafasconquest.peerage.Peerage.get(server).inherited(data.politics.succession().drainDeaths());
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
        FeudDuel.tick(server);
        if (map == null || server.overworld().getGameTime() % MARCH_TICKS != 0) {
            return;
        }
        PoliticsData data = PoliticsData.get(server);
        if (data.politics.wars().isEmpty() && !data.politics.clashing()) {
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

    static Politics.Front front(MinecraftServer server, BaseMap map) {
        return new Politics.Front() {
            @Override
            public boolean fightForReal(War war, int attackers, int defenders) {
                return fight(server, map, war, attackers, defenders);
            }

            @Override
            public void raised(War war, boolean attacking) {
                call(server, map, war, attacking);
            }

            @Override
            public Map<Integer, UUID> playerCounties() {
                return rafasconquest.peerage.Peerage.get(server).holdings();
            }

            @Override
            public String playerName(UUID player) {
                ServerPlayer p = server.getPlayerList().getPlayer(player);
                if (p != null) {
                    return p.getName().getString();
                }
                var cache = server.getProfileCache();
                return cache == null ? "a lord" : cache.get(player).map(com.mojang.authlib.GameProfile::getName).orElse("a lord");
            }

            @Override
            public int playerTroops(UUID player) {
                ServerPlayer p = server.getPlayerList().getPlayer(player);
                return Math.max(Politics.MIN_ARMY, p == null ? 0 : rafasconquest.peerage.Retinues.playerTroops(p));
            }

            @Override
            public boolean feudForReal(Feud feud, int troopsA, int troopsB) {
                return feudFight(server, map, feud, troopsA, troopsB);
            }

            @Override
            public void clashSet(Feud feud, String text) {
                server.getPlayerList().broadcastSystemMessage(Component.literal("[Feud] " + text + " (see the map)")
                        .withStyle(ChatFormatting.GOLD), false);
            }

            @Override
            public void pay(Feud.Lord from, Feud.Lord to, int emeralds) {
                Wars.pay(server, from, to, emeralds);
            }
        };
    }

    /** Whether a player is near enough a place for a fight there to be fought for real. */
    static boolean playerNear(MinecraftServer server, double x, double z) {
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level() == server.overworld() && Math.hypot(p.getX() - x, p.getZ() - z) <= REAL_RANGE) {
                return true;
            }
        }
        return false;
    }

    /** The feud whose skirmish is being fought for real, and the temporary team one lord's men fight under. */
    private static long pendingFeud = -1;
    @Nullable
    private static String feudTeam;

    /**
     * Fights a feud's skirmish or duel for real if a player is near it. A skirmish is a battle of the lords' troops; two
     * lords of one nation share its Recruits team, so the second lord's men fight under a temporary team of their own.
     */
    private static boolean feudFight(MinecraftServer server, BaseMap map, Feud feud, int troopsA, int troopsB) {
        Feud.Clash clash = feud.clash();
        if (clash == null || !playerNear(server, clash.x(), clash.z())) {
            return false;
        }
        if (clash.kind() == Feud.Kind.DUEL) {
            return FeudDuel.start(server, map, feud);
        }
        if (RecruitsBattle.running() || pendingWar >= 0 || pendingFeud >= 0) {
            return false;
        }
        ConquestServer conquest = ConquestServer.get();
        Faction a = conquest == null ? null : conquest.data().faction(map.nations().get(feud.nation(true)).id());
        Faction b = conquest == null ? null : conquest.data().faction(map.nations().get(feud.nation(false)).id());
        if (a == null || b == null) {
            return false;
        }
        MapLands lands = MapLands.of(map);
        Politics.Front front = front(server, map);
        String nameA = Feuds.name(feud.lord(true), lands, front);
        String nameB = Feuds.name(feud.lord(false), lands, front);
        net.minecraft.world.scores.PlayerTeam second = null;
        if (a.id().equals(b.id())) {
            second = new rafasconquest.bridge.RecruitsFactions(server.overworld()).feudTeam(b, nameB + "'s men");
            if (second == null) {
                return false;
            }
            feudTeam = second.getName();
        }
        BaseMap.Nation home = map.nations().get(feud.nation(true));
        double dx = clash.x() - home.x();
        double dz = clash.z() - home.z();
        double length = Math.hypot(dx, dz);
        double[] axis = length < 1 ? new double[]{1, 0} : new double[]{dx / length, dz / length};
        try {
            RecruitsBattle.start(server.overworld(), a, Mth.clamp(troopsA, 20, BattlePlan.MAX_SOLDIERS), b,
                    Mth.clamp(troopsB, 20, BattlePlan.MAX_SOLDIERS), new BattlePlan.Point(clash.x(), clash.z()), axis[0], axis[1],
                    feud.lord(true).isPlayer() ? null : nameA, feud.lord(false).isPlayer() ? null : nameB, BattleType.LAND, 0, second,
                    line -> server.getPlayerList().broadcastSystemMessage(Component.literal("[Feud] The men of " + nameA + " and " + nameB
                            + " meet in a skirmish. " + line).withStyle(ChatFormatting.RED), false),
                    why -> {
                        RafasConquest.LOGGER.warn("[feud] The skirmish of {} and {} could not be fought for real: {}", nameA, nameB, why);
                        PoliticsData.get(server).politics.realClashCancelled(feud.id());
                        pendingFeud = -1;
                    });
        } catch (IllegalStateException e) {
            return false;
        }
        pendingFeud = feud.id();
        pendingAttacker = a.id();
        return true;
    }

    /** One lord pays another: an online player's emeralds go or come; characters' purses are only told of. */
    static void pay(MinecraftServer server, Feud.Lord from, Feud.Lord to, int emeralds) {
        // shortcut: an offline player neither pays nor is paid; upgrade when the mod has its own currency, kept per lord.
        if (from.isPlayer()) {
            ServerPlayer p = server.getPlayerList().getPlayer(from.player());
            if (p != null) {
                int paid = p.getInventory().clearOrCountMatchingItems(s -> s.is(net.minecraft.world.item.Items.EMERALD), emeralds,
                        p.inventoryMenu.getCraftSlots());
                p.sendSystemMessage(Component.literal("You paid " + paid + " emeralds in your feud.").withStyle(ChatFormatting.GOLD));
            }
        }
        if (to.isPlayer()) {
            ServerPlayer p = server.getPlayerList().getPlayer(to.player());
            if (p != null) {
                net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD, emeralds);
                if (!p.getInventory().add(stack)) {
                    p.drop(stack, false);
                }
                p.sendSystemMessage(Component.literal("You were paid " + emeralds + " emeralds in your feud.").withStyle(ChatFormatting.GOLD));
            }
        }
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
        if (RecruitsBattle.running() || pendingWar >= 0 || pendingFeud >= 0) {
            return false;
        }
        ConquestServer conquest = ConquestServer.get();
        Faction a = conquest == null ? null : conquest.data().faction(map.nations().get(war.attacker()).id());
        Faction d = conquest == null ? null : conquest.data().faction(map.nations().get(war.defender()).id());
        if (a == null || d == null) {
            return false;
        }
        boolean atSea = war.type() == BattleType.OCEAN || war.type() == BattleType.AMPHIBIOUS;
        if (atSea && !rafasconquest.bridge.SableLink.available()) {
            return false; // without Sable there are no real ships to fight from: by strength alone
        }
        double[] axis = axis(map, war);
        BattlePlan.Point at = new BattlePlan.Point(war.siteX(), war.siteZ());
        Lands.Fort fort = war.type().walled() ? MapLands.of(map).fort(war.target()) : null;
        if (war.type().walled() && fort == null) {
            return false;
        }
        if (fort != null) {
            // At its walls: the attacker comes on from the field (or in from the sea, at its harbour), the defenders'
            // front on the wall facing it.
            double dx = war.type() == BattleType.AMPHIBIOUS ? -fort.outX() : fort.x() - war.siteX();
            double dz = war.type() == BattleType.AMPHIBIOUS ? -fort.outZ() : fort.z() - war.siteZ();
            double length = Math.max(1e-6, Math.hypot(dx, dz));
            axis = new double[]{dx / length, dz / length};
            double wall = (fort.half() - 2) / Math.max(Math.abs(axis[0]), Math.abs(axis[1]));
            at = new BattlePlan.Point(fort.x() - axis[0] * (wall + BattlePlan.DEPLOY_DISTANCE),
                    fort.z() - axis[1] * (wall + BattlePlan.DEPLOY_DISTANCE));
        }
        bringToBattle(server, war, at, axis);
        boolean near = false;
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level() == server.overworld() && Math.hypot(p.getX() - at.x(), p.getZ() - at.z()) <= REAL_RANGE) {
                near = true;
            }
        }
        if (!near) {
            return false;
        }
        // The players who mustered bring their vassals (with theirs, and their troops) and their retinues.
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            long[] answer = mustered.get(p.getUUID());
            int brought = answer == null || answer[0] != war.id() ? 0 : rafasconquest.peerage.Retinues.playerTroops(p);
            if (brought > 0) {
                if (answer[1] == 1) {
                    attackers += brought;
                } else {
                    defenders += brought;
                }
                server.getPlayerList().broadcastSystemMessage(Component.literal("[War] " + p.getName().getString() + " brings "
                        + brought + " of their vassals' troops and retinue to the field.").withStyle(ChatFormatting.GOLD), false);
            }
        }
        String county = MapLands.of(map).titleName(new Goal(false, war.target()));
        try {
            // Fought at full size: the field battle is numbers, only blocks near players are real soldiers.
            RecruitsBattle.start(server.overworld(), a, Mth.clamp(attackers, 20, BattlePlan.MAX_SOLDIERS), d,
                    Mth.clamp(defenders, 20, BattlePlan.MAX_SOLDIERS),
                    at, axis[0], axis[1], general(map, war, true), general(map, war, false), war.type(),
                    war.type() == BattleType.AMPHIBIOUS ? fort.harbourShips() : 0,
                    line -> server.getPlayerList().broadcastSystemMessage(Component.literal("[War] The armies of " + a.name() + " and "
                            + d.name() + switch (war.type()) {
                                case OCEAN -> " meet at sea, their fleets closing,";
                                case AMPHIBIOUS -> " meet at " + fort.name() + ", " + a.name() + " landing under its walls from the sea,";
                                case SIEGE -> " meet at the walls of " + fort.name() + ", " + d.name() + " holding out behind them,";
                                case LAND -> " meet in battle";
                            } + " for the " + county + ". " + line).withStyle(ChatFormatting.RED), false),
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
    private static void bringToBattle(MinecraftServer server, War war, BattlePlan.Point at, double[] axis) {
        ServerLevel level = server.overworld();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            long[] answer = mustered.get(p.getUUID());
            if (answer == null || answer[0] != war.id()) {
                continue;
            }
            double side = answer[1] == 1 ? -1 : 1;
            int x = (int) Math.round(at.x() + axis[0] * BEHIND_LINES * side);
            int z = (int) Math.round(at.z() + axis[1] * BEHIND_LINES * side);
            teleport(p, level, x, z);
            p.sendSystemMessage(Component.literal("Your army has reached the field of battle.").withStyle(ChatFormatting.RED));
        }
    }

    private static void teleport(ServerPlayer p, ServerLevel level, int x, int z) {
        toSurface(p, level, x, z, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
    }

    /**
     * Teleports a player onto the top block of a column of a heightmap's kind. The chunk is loaded (or generated) first:
     * an unloaded chunk's height is the world's floor.
     */
    public static void toSurface(ServerPlayer p, ServerLevel level, int x, int z, Heightmap.Types type) {
        int y = level.getChunk(x >> 4, z >> 4).getHeight(type, x & 15, z & 15) + 1;
        p.teleportTo(level, x + 0.5, y, z + 0.5, p.getYRot(), p.getXRot());
    }

    /** A battle fought for real is over: if it was a war's, its result goes to the war. */
    public static void battleOver(UUID first, UUID second, int winner, double firstLeft, double secondLeft) {
        ConquestServer conquest = ConquestServer.get();
        MinecraftServer server = conquest == null ? null : conquest.overworld().getServer();
        if (pendingFeud >= 0 && server != null) {
            // A feud's skirmish: its first side is always the feud's first lord.
            long feud = pendingFeud;
            pendingFeud = -1;
            PoliticsData data = PoliticsData.get(server);
            BaseMap map = ConquestRuntime.map();
            if (winner < 0 || map == null) {
                data.politics.realClashCancelled(feud);
            } else {
                data.politics.realSkirmish(feud, winner == 0, firstLeft, secondLeft, now(server), MapLands.of(map),
                        new Random(server.overworld().getGameTime()), front(server, map));
                claimCities(map, data.politics);
            }
            data.setDirty();
            PoliticsSync.toAll(server);
            return;
        }
        if (pendingWar < 0) {
            return;
        }
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

    /**
     * Ends the march of every army on the march now, or only those of the sides {@code nation} fights on: each skips its
     * stops and stands on its war's battle site. Armies still mustering stay (for {@code /pve politics march finish}).
     */
    public static String finishMarch(MinecraftServer server, @Nullable String nation) {
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
                if (army != null && !army.mustering(now) && (only < 0 || war.side(attacking).contains(only))) {
                    army.stops.clear();
                    army.x = war.siteX();
                    army.z = war.siteZ();
                    ended++;
                }
            }
        }
        data.setDirty();
        MusterBars.update(server, map, data.politics);
        PoliticsSync.toAll(server);
        return ended == 0 ? "No army is on the march" : ended + " army(ies) done marching: they stand on their battle sites";
    }

    /**
     * Sets every war's armies (or only those of the wars {@code nation} fights in) on their battle site at once, done
     * mustering: they fight within seconds, for real if a player is near (for {@code /pve politics muster field}).
     */
    public static String toField(MinecraftServer server, @Nullable String nation) {
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
        List<String> fields = new ArrayList<>();
        for (War war : data.politics.wars()) {
            if (only >= 0 && !war.side(true).contains(only) && !war.side(false).contains(only)) {
                continue;
            }
            Army a = war.army(true);
            Army d = war.army(false);
            if (a == null || d == null) {
                continue; // a side has no army to send
            }
            for (Army army : new Army[]{a, d}) {
                army.readyAt = Math.min(army.readyAt, now);
                army.stops.clear();
                army.x = war.siteX();
                army.z = war.siteZ();
            }
            fields.add(MapLands.of(map).titleName(new Goal(false, war.target())) + " (" + (int) war.siteX() + ", " + (int) war.siteZ() + ")");
        }
        data.setDirty();
        MusterBars.update(server, map, data.politics);
        PoliticsSync.toAll(server);
        return fields.isEmpty() ? "No war has both its armies in the field"
                : "The armies stand on the field: " + String.join("; ", fields) + ". They fight within seconds, for real if a player is near";
    }

    /** The war a nation fights in with an army in the field, and on which side; null if none. */
    @Nullable
    private static War warOf(Politics politics, int nation, boolean[] attacking) {
        for (War war : politics.wars()) {
            for (boolean side : new boolean[]{true, false}) {
                if (war.side(side).contains(nation) && war.army(side) != null) {
                    attacking[0] = side;
                    return war;
                }
            }
        }
        return null;
    }

    /** The battle a recruiter's nation fights now, for the recruiter to offer to send a player there; null if none. */
    @Nullable
    public static String warCampOf(MinecraftServer server, Faction nation) {
        BaseMap map = ConquestRuntime.map();
        int n = map == null ? -1 : nationIndex(map, nation);
        War war = n < 0 ? null : warOf(PoliticsData.get(server).politics, n, new boolean[1]);
        return war == null ? null : MapLands.of(map).titleName(new Goal(false, war.target()));
    }

    /**
     * Sends a player who serves {@code nation} to its war camp: the command tent of their side's camp if its battle is
     * being fought now, else behind their side's lines on the battle site. Either way they muster with its army, and go
     * with it into battle.
     */
    public static String toWarCamp(ServerPlayer player, Faction nation) {
        BaseMap map = ConquestRuntime.map();
        int n = map == null ? -1 : nationIndex(map, nation);
        boolean[] attacking = new boolean[1];
        War war = n < 0 ? null : warOf(PoliticsData.get(player.server).politics, n, attacking);
        if (war == null) {
            return "There is no war to send you to";
        }
        mustered.put(player.getUUID(), new long[]{war.id(), attacking[0] ? 1 : 0});
        ServerLevel level = player.server.overworld();
        // The battle is fought by the war's leaders' armies: an ally's player goes to its leader's camp.
        ConquestServer conquest = ConquestServer.get();
        Faction leader = conquest == null ? null
                : conquest.data().faction(map.nations().get(attacking[0] ? war.attacker() : war.defender()).id());
        net.minecraft.world.phys.Vec3 tent = pendingWar == war.id() && leader != null ? RecruitsBattle.commandTent(leader.id()) : null;
        String county = MapLands.of(map).titleName(new Goal(false, war.target()));
        if (tent != null) {
            player.teleportTo(level, tent.x, tent.y, tent.z, player.getYRot(), player.getXRot());
            return "You arrive at the war camp, before the command tent. The battle for the " + county + " is being fought";
        }
        double[] axis = axis(map, war);
        double side = attacking[0] ? -1 : 1;
        teleport(player, level, (int) Math.round(war.siteX() + axis[0] * BEHIND_LINES * side),
                (int) Math.round(war.siteZ() + axis[1] * BEHIND_LINES * side));
        return "You arrive behind your lines on the field of the " + county + ". You will fight with your army when it comes";
    }

    /** A faction's nation on the map, by its id; -1 if it is none. */
    private static int nationIndex(BaseMap map, Faction nation) {
        for (int i = 0; i < map.nations().size(); i++) {
            if (map.nations().get(i).id().equals(nation.id())) {
                return i;
            }
        }
        return -1;
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
                    .append(w.goal().counties(lands).size()).append(" counties taken, ").append(w.battles()).append(" battle(s); next: ").append(w.type().title()).append(" for the ")
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
        out.append("\n").append(politics.feuds().size()).append(" feud(s):");
        Politics.Front front = front(server, map);
        for (Feud f : politics.feuds()) {
            out.append("\n  ").append(Feuds.name(f.lord(true), lands, front)).append(" against ").append(Feuds.name(f.lord(false), lands, front))
                    .append(String.format(", %d to %d", f.scoreA, f.scoreB));
            Feud.Clash c = f.clash();
            if (c != null) {
                out.append(String.format("; a %s at %.0f, %.0f", c.kind() == Feud.Kind.DUEL ? "duel" : "skirmish", c.x(), c.z()));
            }
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

    /**
     * Starts a feud between two characters now (or finds theirs), and with {@code kind} ("skirmish" or "duel") calls that
     * fight for half a minute from now, for {@code /pve politics feud}.
     */
    public static String forceFeud(MinecraftServer server, String first, String second, @Nullable String kind) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return "No world map yet";
        }
        int a = personNamed(map, -1, first);
        int b = personNamed(map, -1, second);
        if (a < 0 || b < 0) {
            return "No character called " + (a < 0 ? first : second);
        }
        PoliticsData data = PoliticsData.get(server);
        MapLands lands = MapLands.of(map);
        Politics.Front front = front(server, map);
        Feud.Lord la = Feud.Lord.of(a);
        Feud.Lord lb = Feud.Lord.of(b);
        Feud feud = data.politics.feuds().stream().filter(f -> f.involves(la) && f.involves(lb)).findFirst()
                .orElseGet(() -> data.politics.feud(la, lb, (long) now(server), lands, front));
        if (feud == null) {
            return "They can't feud: one of them is feuding already, or holds no land";
        }
        String text = Feuds.name(feud.lord(true), lands, front) + " is feuding with " + Feuds.name(feud.lord(false), lands, front);
        if (kind != null) {
            Feud.Kind k = kind.equalsIgnoreCase("duel") ? Feud.Kind.DUEL : Feud.Kind.SKIRMISH;
            if (!data.politics.callClash(feud, k, now(server), 30 / 1200.0, lands, front)) {
                return text + "; a fight is already called";
            }
            Feud.Clash c = feud.clash();
            text += String.format("; a %s in half a minute at %.0f, %.0f (fought for real if you are within %d blocks)",
                    k == Feud.Kind.DUEL ? "duel" : "skirmish", c.x(), c.z(), REAL_RANGE);
        }
        data.setDirty();
        PoliticsSync.toAll(server);
        return text;
    }

    /** One of a nation's characters by name (any nation's if -1), or -1. */
    private static int personNamed(BaseMap map, int nation, String name) {
        java.util.List<rafasconquest.people.Person> people = map.realm().people();
        for (int i = 0; i < people.size(); i++) {
            if ((nation < 0 || people.get(i).nation() == nation) && people.get(i).name().equalsIgnoreCase(name.trim())) {
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
        return map.realm().people().stream().filter(p -> p.nation() == n && p.age() >= 16)
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
