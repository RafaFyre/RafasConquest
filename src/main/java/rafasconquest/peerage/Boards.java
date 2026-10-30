package rafasconquest.peerage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * A quest board: the quests of the city it stands in (or the nearest, within {@link #REACH}), worked out for the day
 * ({@link Quests}). Reading a quest remembers it, so its giver offers it in conversation; a general quest is taken, and
 * reported, at the board itself.
 */
public final class Boards {
    static final int REACH = 300;

    /** One quest as the board shows it: what, from whom and where, the reward, and the player's part in it. */
    public record Entry(long id, String title, String giver, int giverX, int giverZ, String reward, String state, boolean general) {
    }

    public record View(BlockPos board, String place, List<Entry> entries) {
    }

    private Boards() {
    }

    public static void open(ServerPlayer player, BlockPos board) {
        ConquestNetwork.sendBoard(player, view(player, board));
    }

    /** The city a board serves, or null. */
    static CityLayout.Site city(ServerPlayer player, BlockPos board) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null) {
            return null;
        }
        int r = REACH + CityLayout.MAX_HALF_EXTENT;
        CityLayout.Site best = null;
        double bestDistance = Double.MAX_VALUE;
        for (CityLayout.Site s : CityLayout.allSitesInBox(player.serverLevel().getSeed(), settings, board.getX() - r, board.getZ() - r,
                board.getX() + r, board.getZ() + r)) {
            double d = Math.hypot(s.x() - board.getX(), s.z() - board.getZ());
            if (d < bestDistance && d <= REACH + s.halfExtent()) {
                bestDistance = d;
                best = s;
            }
        }
        return best;
    }

    static List<Quest> quests(ServerPlayer player, CityLayout.Site city) {
        BaseMap map = ConquestRuntime.map();
        if (map == null || city == null) {
            return List.of();
        }
        long day = player.serverLevel().getDayTime() / 24000L;
        return Quests.board(player.serverLevel().getSeed(), map, Peerage.get(player.server), city, day);
    }

    static View view(ServerPlayer player, BlockPos board) {
        CityLayout.Site city = city(player, board);
        BaseMap map = ConquestRuntime.map();
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        List<Entry> entries = new ArrayList<>();
        List<Long> shown = new ArrayList<>();
        for (Quest q : quests(player, city)) {
            shown.add(q.id());
            entries.add(entry(q, peer));
        }
        // The player's general quests from this board stay on it until reported.
        for (Peerage.Taken t : peer.taken) {
            if (t.quest.giver() == Quest.BOARD && !shown.contains(t.quest.id()) && Math.abs(t.quest.giverX() - board.getX()) <= 3
                    && Math.abs(t.quest.giverZ() - board.getZ()) <= 3) {
                entries.add(entry(t.quest, peer));
            }
        }
        String place = city == null || map == null ? "the wilds" : map.names().get(map.provinceIndexAt(city.x(), city.z()));
        return new View(board, place, entries);
    }

    private static Entry entry(Quest q, Peerage.Peer peer) {
        Peerage.Taken taken = peer.taken(q.id());
        String state = taken != null ? (taken.ready() ? "ready" : "taken") : peer.read.containsKey(q.id()) ? "read" : "new";
        String reward = q.emeralds() + " emeralds" + (q.renown() > 0 ? ", " + q.renown() + " " + (q.ladder() == Quest.Ladder.NOBLE ? "noble"
                : "military") + " renown" : "");
        String giver = q.giver() == Quest.BOARD ? "Posted here" : q.giverName();
        return new Entry(q.id(), q.title(), giver, q.giverX(), q.giverZ(), reward, state, q.giver() == Quest.BOARD);
    }

    /** Reading a quest, or taking or reporting a general quest at the board. */
    public static void act(ServerPlayer player, BlockPos board, long id, String action) {
        if (player.distanceToSqr(board.getCenter()) > 64) {
            return;
        }
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        peer.name = player.getName().getString();
        Quest quest = null;
        for (Quest q : quests(player, city(player, board))) {
            if (q.id() == id) {
                quest = q;
            }
        }
        switch (action) {
            case "read" -> {
                if (quest != null && peer.taken(id) == null) {
                    peer.read.put(id, quest);
                    peerage.changed();
                    if (quest.giver() != Quest.BOARD) {
                        player.sendSystemMessage(Component.literal("You read of it: " + quest.title() + ". " + quest.giverName()
                                + " offers it, at " + quest.giverX() + ", " + quest.giverZ() + "."));
                    }
                }
            }
            case "take" -> {
                if (quest != null && quest.giver() == Quest.BOARD && peer.taken(id) == null) {
                    peer.read.remove(id);
                    peerage.take(player.getUUID(), quest);
                    player.sendSystemMessage(Component.literal("Quest taken: " + quest.title() + " (" + quest.targetX() + ", "
                            + quest.targetZ() + ")."));
                }
            }
            case "report" -> {
                Peerage.Taken t = peer.taken(id);
                if (t != null && t.quest.giver() == Quest.BOARD && t.ready()) {
                    Advancement.complete(player, peerage, t);
                }
            }
            default -> {
            }
        }
        open(player, board);
    }
}
