package rafasconquest.peerage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.BattleType;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.bridge.SiegeSurvey;
import rafasconquest.politics.Lands;
import rafasconquest.politics.MapLands;
import rafasconquest.core.SeatCities;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;

/** The grand strategy view: the world map with the player's realm and resources laid over it. */
public final class GrandStrategy {
    public static final int MUSTER = 0;
    public static final int DISMISS = 1;
    public static final int MARCH = 2;
    /** March on army {@code arg}, to face it when there. */
    public static final int ATTACK = 3;
    /** Fight army {@code arg} for real, where it stands. */
    public static final int BATTLE = 4;
    /** Creative: the army is gone (a hold's own army is emptied, to muster anew). */
    public static final int DELETE = 5;
    /** The hold's own army's id; spawned armies are 0 and up. */
    public static final int HOLD = -1;
    public static final int NO_TARGET = -2;
    /** Most soldiers a spawned army may be made with. */
    public static final int MAX_SOLDIERS = 40000;

    /**
     * An army on the map: its id ({@link #HOLD}, or a spawned army's), nation (index, -1: none), general ("": the best at
     * war), card cap (0: none), cards ({@link BattlePlan.Arm} ordinals) and each card's soldiers, march, and the army it
     * marches to attack.
     */
    public record Army(int id, int nation, String general, int cap, int[] units, int[] men, double fromX, double fromZ, double toX, double toZ,
                       long start, int target) {
        public int soldiers() {
            return java.util.Arrays.stream(men).sum();
        }
    }

    /** The seat province of the held county (-1: no fief), the nation the player serves (-1: none), the day, and the armies. */
    public record View(int seat, int nation, long day, List<Army> armies) {
        @Nullable
        public Army army(int id) {
            return armies.stream().filter(a -> a.id() == id).findFirst().orElse(null);
        }
    }

    private GrandStrategy() {
    }

    public static void open(ServerPlayer player) {
        ConquestNetwork.sendGrandStrategy(player, view(player));
    }

    static View view(ServerPlayer player) {
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        ConquestServer server = ConquestServer.get();
        int nation = nation(player, ConquestRuntime.map(), server);
        long day = server == null ? 0 : server.data().lastProcessedDay();
        List<Army> armies = new ArrayList<>();
        if (peer.hold >= 0) {
            armies.add(army(HOLD, peerage.garrison(peer.hold, player.getRandom()), peer.hold));
        }
        for (int i = 0; i < peerage.spawned.size(); i++) {
            armies.add(army(i, peerage.spawned.get(i), -1));
        }
        return new View(peer.hold, nation, day, armies);
    }

    private static Army army(int id, Garrison g, int hold) {
        return new Army(id, nationOf(g, hold), g.general, g.cap, g.units.stream().mapToInt(Integer::intValue).toArray(),
                g.men.stream().mapToInt(Integer::intValue).toArray(), g.fromX, g.fromZ, g.toX, g.toZ, g.start, g.target);
    }

    /** An army's nation: a spawned army's own, the hold's army's its holder's. */
    private static int nationOf(Garrison g, int hold) {
        BaseMap map = ConquestRuntime.map();
        if (g.nation >= 0 || map == null || hold < 0 || hold >= map.holders().length) {
            return g.nation;
        }
        return map.holders()[hold];
    }

    /** The army {@code id} the player may order: their hold's, or (in creative) a spawned one; null if none. */
    @Nullable
    private static Garrison find(ServerPlayer player, int id) {
        Peerage peerage = Peerage.get(player.server);
        int hold = peerage.of(player.getUUID()).hold;
        if (id == HOLD) {
            return hold < 0 ? null : peerage.garrison(hold, player.getRandom());
        }
        return player.isCreative() && id >= 0 && id < peerage.spawned.size() ? peerage.spawned.get(id) : null;
    }

    /** Where an army stands now (x, z): on its march, else home in its hold's city; null if nowhere. */
    @Nullable
    private static double[] where(Garrison g, int hold, long now) {
        double[] at = Garrison.at(g.fromX, g.fromZ, g.toX, g.toZ, g.start, now);
        return at != null ? at : city(hold);
    }

    /** In creative: a new army of {@code nation} led by {@code general}, made for {@code soldiers}, at (x, z); filled at random if asked. */
    public static void spawn(ServerPlayer player, int nation, String general, int soldiers, boolean autofill, double x, double z) {
        BaseMap map = ConquestRuntime.map();
        if (!player.isCreative() || map == null || nation < 0 || nation >= map.nations().size() || !Double.isFinite(x) || !Double.isFinite(z)) {
            return;
        }
        Garrison g = new Garrison();
        g.nation = nation;
        g.general = general.length() > 64 ? general.substring(0, 64) : general;
        g.cap = Math.max(1, Math.min(MAX_SOLDIERS, soldiers) / Garrison.MEN);
        if (autofill) {
            g.fill(g.cap, player.getRandom());
        }
        g.fromX = g.toX = x;
        g.fromZ = g.toZ = z;
        g.start = player.server.overworld().getGameTime();
        Peerage peerage = Peerage.get(player.server);
        peerage.spawned.add(g);
        peerage.setDirty();
        open(player);
    }

    /** The player orders army {@code army}: muster a card of {@code arg}'s arm, dismiss card {@code arg}, march to (x, z), attack or fight army {@code arg}. */
    public static void order(ServerPlayer player, int army, int op, int arg, double x, double z) {
        Peerage peerage = Peerage.get(player.server);
        int hold = peerage.of(player.getUUID()).hold;
        Garrison g = find(player, army);
        if (g == null) {
            return;
        }
        int home = army == HOLD ? hold : -1;
        long now = player.server.overworld().getGameTime();
        switch (op) {
            case MUSTER -> {
                if (arg >= 0 && arg < BattlePlan.Arm.values().length && BattlePlan.Arm.values()[arg].roster()) {
                    g.add(arg);
                }
            }
            case DISMISS -> {
                if (arg >= 0 && arg < g.units.size()) {
                    g.remove(arg);
                }
            }
            case MARCH, ATTACK -> {
                Garrison foe = op == ATTACK && arg != army ? find(player, arg) : null;
                double[] goal = foe == null ? new double[]{x, z} : where(foe, arg == HOLD ? hold : -1, now);
                double[] at = where(g, home, now);
                if ((op == ATTACK && foe == null) || at == null || goal == null || !Double.isFinite(goal[0]) || !Double.isFinite(goal[1])) {
                    return;
                }
                g.fromX = at[0];
                g.fromZ = at[1];
                g.toX = goal[0];
                g.toZ = goal[1];
                g.start = now;
                g.target = foe == null ? NO_TARGET : arg;
            }
            case BATTLE -> {
                Garrison foe = arg != army ? find(player, arg) : null;
                if (foe != null) {
                    battle(player, g, home, foe, arg == HOLD ? hold : -1, now);
                    g.target = NO_TARGET;
                    peerage.setDirty();
                }
                return; // no fresh view: the player has left the map for the battle, and a view would reopen it
            }
            case DELETE -> {
                if (!player.isCreative()) {
                    return;
                }
                g.units.clear();
                g.men.clear();
                disband(peerage, g);
            }
            default -> {
                return;
            }
        }
        peerage.setDirty();
        open(player);
    }

    /** Fights two armies for real where the defender stands, the attacker coming at it from where it is. */
    private static void battle(ServerPlayer player, Garrison attacker, int attackerHold, Garrison defender, int defenderHold, long now) {
        BaseMap map = ConquestRuntime.map();
        ConquestServer server = ConquestServer.get();
        int na = nationOf(attacker, attackerHold);
        int nd = nationOf(defender, defenderHold);
        Faction a = map == null || server == null || na < 0 || na >= map.nations().size() ? null : server.data().faction(map.nations().get(na).id());
        Faction d = map == null || server == null || nd < 0 || nd >= map.nations().size() ? null : server.data().faction(map.nations().get(nd).id());
        double[] from = where(attacker, attackerHold, now);
        double[] at = where(defender, defenderHold, now);
        if (a == null || d == null || a == d) {
            player.sendSystemMessage(Component.literal("A battle needs two armies of different nations").withStyle(ChatFormatting.RED));
            return;
        }
        if (attacker.units.isEmpty() || defender.units.isEmpty() || from == null || at == null) {
            player.sendSystemMessage(Component.literal("Both armies need soldiers to fight").withStyle(ChatFormatting.RED));
            return;
        }
        // Both at sea: a sea battle. The defender within a castle's or city's walls: a siege. Otherwise: a land battle.
        MapLands lands = MapLands.of(map);
        boolean sea = lands.wet(at[0], at[1]);
        if (sea != lands.wet(from[0], from[1])) {
            player.sendSystemMessage(Component.literal("One army is at sea and the other on land").withStyle(ChatFormatting.RED));
            return;
        }
        Lands.Fort fort = sea ? null : fortAt(map, lands, at);
        BattleType type = sea ? BattleType.OCEAN : fort != null ? BattleType.SIEGE : BattleType.LAND;
        int menA = sea ? shipMen(attacker) : attacker.soldiers();
        int menD = sea ? shipMen(defender) : defender.soldiers();
        int[] mixA = sea ? shipMix(attacker) : attacker.mix();
        int[] mixD = sea ? shipMix(defender) : defender.mix();
        if (sea && !rafasconquest.bridge.SableLink.available()) {
            player.sendSystemMessage(Component.literal("A sea battle needs Sable for its ships").withStyle(ChatFormatting.RED));
            return;
        }
        if (sea && (mixA == null || mixD == null)) {
            player.sendSystemMessage(Component.literal("At sea only ship cards fight: both armies need some").withStyle(ChatFormatting.RED));
            return;
        }
        double dx = at[0] - from[0];
        double dz = at[1] - from[1];
        double len = Math.hypot(dx, dz);
        double ux = len < 1 ? 1 : dx / len;
        double uz = len < 1 ? 0 : dz / len;
        java.util.function.Consumer<SiegeSurvey.Result> fight = survey -> {
            try {
                // The line of battle runs across the attacker's march, as /pve battle lays it across the caller's look.
                RecruitsBattle.start(player.server.overworld(), a, menA, d, menD, new BattlePlan.Point(at[0], at[1]), -uz, ux,
                        attacker.general.isEmpty() ? null : attacker.general, defender.general.isEmpty() ? null : defender.general,
                        type, 0, null, survey, mixA, mixD, line -> {
                            RecruitsBattle fought = RecruitsBattle.current();
                            if (fought != null) {
                                Garrison[] armies = {attacker, defender};
                                int[][] started = {perArm(fought.field().army(0), false), perArm(fought.field().army(1), false)};
                                fought.whenOver(outcome -> aftermath(player.server, fought, armies, started, outcome.winner(), at, ux, uz, sea));
                            }
                            toBattle(player);
                            String url = rafasconquest.dev.BattleMap.open(player.server);
                            player.sendSystemMessage(Component.literal(line + ". The armies are deploying: ")
                                    .append(Component.literal("[Open the battle map]").withStyle(style -> style.withColor(ChatFormatting.AQUA)
                                            .withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))))
                                    .append(Component.literal(" or /pve debug battle start")));
                        }, why -> player.sendSystemMessage(Component.literal(why).withStyle(ChatFormatting.RED)));
            } catch (IllegalStateException e) {
                player.sendSystemMessage(Component.literal(e.getMessage()).withStyle(ChatFormatting.RED));
            }
        };
        if (fort == null) {
            fight.accept(null);
            player.sendSystemMessage(Component.literal(sea ? "The fleets are closing..." : "The armies are surveying the ground..."));
            return;
        }
        // As /pve siege: a city's gates fitted, its defenders' council at its barracks, its harbour side never attacked.
        net.minecraft.server.level.ServerLevel level = player.server.overworld();
        boolean city = fort.half() > rafasconquest.worldgen.CountryPiece.CASTLE_WALL;
        if (city) {
            for (SeatCities.City c : ConquestRuntime.cities().inBox(fort.x(), fort.z(), fort.x(), fort.z())) {
                if (c.x() == fort.x() && c.z() == fort.z()) {
                    rafasconquest.worldgen.CityGates.fitAll(level, c);
                }
            }
        }
        player.sendSystemMessage(Component.literal("Surveying the walls of " + fort.name() + "..."));
        SiegeSurvey.surveyAsync(level, fort.x(), fort.z(), fort.half(), city ? SiegeSurvey.cityHall(level, fort.x(), fort.z()) : null,
                fort.port() ? net.minecraft.core.Direction.fromDelta(fort.outX(), 0, fort.outZ()) : null,
                java.util.EnumSet.allOf(rafasconquest.battle.Siege.Engine.class)).whenComplete((survey, error) -> {
            if (error != null) {
                player.sendSystemMessage(Component.literal("Couldn't survey the walls: " + error.getMessage()).withStyle(ChatFormatting.RED));
            } else {
                fight.accept(survey);
            }
        });
    }

    /** The castle or seat city whose walls stand about (x, z); null if none. */
    @Nullable
    private static Lands.Fort fortAt(BaseMap map, MapLands lands, double[] at) {
        List<Lands.Fort> forts = new ArrayList<>();
        for (SeatCities.City c : map.cities()) {
            forts.add(lands.fort(c));
        }
        for (rafasconquest.core.Countryside.Settlement s : map.settlements()) {
            if (s.kind() == rafasconquest.core.Countryside.Kind.CASTLE) {
                forts.add(new Lands.Fort("the castle of " + map.names().get(s.province()), s.x(), s.z(), 20, false, 0, 0)); // as MapLands.fort
            }
        }
        for (Lands.Fort f : forts) {
            if (f != null && Math.max(Math.abs(at[0] - f.x()), Math.abs(at[1] - f.z())) <= f.half()) {
                return f;
            }
        }
        return null;
    }

    /** At sea only the cards sailing ships fight: their soldiers. */
    private static int shipMen(Garrison g) {
        int men = 0;
        for (int i = 0; i < g.units.size(); i++) {
            BattlePlan.Arm a = BattlePlan.Arm.get(g.units.get(i));
            men += a != null && a.ship() != null ? g.men.get(i) : 0;
        }
        return men;
    }

    /** At sea only the cards sailing ships fight: their blocks per arm; null if there are none. */
    @Nullable
    private static int[] shipMix(Garrison g) {
        int[] mix = g.mix();
        if (mix == null) {
            return null;
        }
        for (int a = 0; a < mix.length; a++) {
            mix[a] = BattlePlan.Arm.get(a).ship() != null ? mix[a] : 0;
        }
        return java.util.Arrays.stream(mix).sum() > 0 ? mix : null;
    }

    /** How far a beaten army falls back from the field, in blocks. */
    private static final double RETREAT = 1500;

    /** An army's soldiers in the field by arm (ordinal): those it started with, or those still alive. */
    private static int[] perArm(rafasconquest.battle.FieldArmy army, boolean alive) {
        int[] n = new int[BattlePlan.Arm.values().length];
        for (rafasconquest.battle.FieldBlock b : army.blocks()) {
            n[b.arm().ordinal()] += alive ? b.alive() : b.started();
        }
        return n;
    }

    /**
     * The battle over: each army's cards lose what its arms lost in the field, an army left without any is gone, and
     * the beaten one falls back from the field, away from the victor (the attacker the way it came).
     */
    private static void aftermath(net.minecraft.server.MinecraftServer server, RecruitsBattle fought, Garrison[] armies,
                                  int[][] started, int winner, double[] at, double ux, double uz, boolean sea) {
        Peerage peerage = Peerage.get(server);
        long now = server.overworld().getGameTime();
        for (int side = 0; side < 2; side++) {
            int[] alive = perArm(fought.field().army(side), true);
            double[] kept = new double[alive.length];
            int all = 0;
            int left = 0;
            for (int a = 0; a < alive.length; a++) {
                kept[a] = started[side][a] > 0 ? alive[a] / (double) started[side][a] : -1;
                all += started[side][a];
                left += alive[a];
            }
            Garrison g = armies[side];
            g.survive(kept, sea ? 1 : left / (double) Math.max(1, all)); // at sea the cards left ashore fought no one
            double away = side == 0 ? -1 : 1; // the attacker came along +u
            boolean beaten = winner == 1 - side;
            g.fromX = at[0];
            g.fromZ = at[1];
            g.toX = beaten ? at[0] + ux * away * RETREAT : at[0];
            g.toZ = beaten ? at[1] + uz * away * RETREAT : at[1];
            g.start = now;
            g.target = NO_TARGET;
        }
        for (Garrison g : armies) {
            if (g.units.isEmpty()) {
                disband(peerage, g);
            }
        }
        peerage.setDirty();
    }

    /** A spawned army wiped out leaves the map; the ids after it close up, and whoever marched on it stops. */
    private static void disband(Peerage peerage, Garrison gone) {
        int id = peerage.spawned.indexOf(gone);
        if (id < 0) {
            gone.fromX = gone.fromZ = gone.toX = gone.toZ = Double.NaN; // a hold's army stays, empty, home in its city to muster anew
            return;
        }
        peerage.spawned.remove(id);
        List<Garrison> all = new ArrayList<>(peerage.spawned);
        all.addAll(peerage.garrisons.values());
        for (Garrison g : all) {
            g.target = g.target == id ? NO_TARGET : g.target > id ? g.target - 1 : g.target;
        }
    }

    /** How far behind the attacking army's general the player lands, in blocks. */
    private static final double BEHIND_GENERAL = 8;

    /**
     * The battle just set up: the player to the attacking army, just behind its general and facing the foe. One the
     * battle already enlisted and placed (on the attacker's team) is left where it put them.
     */
    private static void toBattle(ServerPlayer player) {
        RecruitsBattle battle = RecruitsBattle.current();
        rafasconquest.battle.FieldArmy army = battle == null ? null : battle.field().army(0);
        rafasconquest.battle.FieldBlock general = army == null ? null : army.commander();
        if (general == null || !player.isAlive()) {
            return;
        }
        double x = general.pos().x() - army.facingX() * BEHIND_GENERAL;
        double z = general.pos().z() - army.facingZ() * BEHIND_GENERAL;
        net.minecraft.server.level.ServerLevel level = player.server.overworld();
        if (player.level() == level && Math.hypot(player.getX() - x, player.getZ() - z) < 200) {
            return; // enlisted and placed already
        }
        level.getChunk((int) Math.floor(x) >> 4, (int) Math.floor(z) >> 4); // loaded, so its height is real
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) Math.floor(x), (int) Math.floor(z));
        float yaw = (float) Math.toDegrees(Math.atan2(-army.facingX(), army.facingZ()));
        player.teleportTo(level, x, y, z, yaw, 0);
    }

    /** The middle of the city seating a province (x, z), or null. */
    @Nullable
    private static double[] city(int province) {
        BaseMap map = ConquestRuntime.map();
        if (map == null || province < 0) {
            return null;
        }
        for (SeatCities.City c : map.cities()) {
            if (c.province() == province) {
                return new double[]{c.x() + 0.5, c.z() + 0.5};
            }
        }
        return null;
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
