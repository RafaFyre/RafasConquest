package rafasconquest.peerage;

import net.minecraft.server.level.ServerPlayer;
import rafasconquest.battle.BattlePlan;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

/** The grand strategy view: the world map with the player's realm and resources laid over it. */
public final class GrandStrategy {
    public static final int MUSTER = 0;
    public static final int DISMISS = 1;
    public static final int MARCH = 2;

    /**
     * The seat province of the held county (-1: no fief), the nation the player serves (-1: none), and the day; the
     * hold's army ({@link Garrison}): its cards ({@link BattlePlan.Arm} ordinals) and its march.
     */
    public record View(int seat, int nation, long day, int[] army, double fromX, double fromZ, double toX, double toZ, long start) {
    }

    private GrandStrategy() {
    }

    public static void open(ServerPlayer player) {
        ConquestNetwork.sendGrandStrategy(player, view(player));
    }

    static View view(ServerPlayer player) {
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        ConquestServer server = ConquestServer.get();
        int nation = nation(player, ConquestRuntime.map(), server);
        long day = server == null ? 0 : server.data().lastProcessedDay();
        if (peer.hold < 0) {
            return new View(-1, nation, day, new int[0], Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0);
        }
        Garrison g = Peerage.get(player.server).garrison(peer.hold, player.getRandom());
        return new View(peer.hold, nation, day, g.units.stream().mapToInt(Integer::intValue).toArray(), g.fromX, g.fromZ, g.toX, g.toZ, g.start);
    }

    /** The player orders their hold's army: muster a card of {@code arg}'s arm, dismiss card {@code arg}, or march to (x, z). */
    public static void order(ServerPlayer player, int op, int arg, double x, double z) {
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        if (peer.hold < 0) {
            return;
        }
        Garrison g = peerage.garrison(peer.hold, player.getRandom());
        long now = player.server.overworld().getGameTime();
        switch (op) {
            case MUSTER -> {
                if (arg >= 0 && arg < BattlePlan.Arm.values().length) {
                    g.units.add(arg);
                }
            }
            case DISMISS -> {
                if (arg >= 0 && arg < g.units.size()) {
                    g.units.remove(arg);
                }
            }
            case MARCH -> {
                double[] at = Garrison.at(g.fromX, g.fromZ, g.toX, g.toZ, g.start, now);
                if (at == null) {
                    at = city(peer.hold);
                }
                if (at == null || !Double.isFinite(x) || !Double.isFinite(z)) {
                    return;
                }
                g.fromX = at[0];
                g.fromZ = at[1];
                g.toX = x;
                g.toZ = z;
                g.start = now;
            }
            default -> {
                return;
            }
        }
        peerage.setDirty();
        open(player);
    }

    /** The middle of the city seating a province (x, z), or null. */
    private static double[] city(int province) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
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
