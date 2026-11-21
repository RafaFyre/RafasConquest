package rafasconquest.sim;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;
import rafasconquest.peerage.NobleTitle;
import rafasconquest.peerage.Peerage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fast forward: the overworld clock runs 1, 2, 4, 120 or 600 times as fast, so days (and all that runs on them:
 * economy, wars, marches) pass sooner. Alone, the player sets it at will; on a server, Dukes, Kings and Emperors vote
 * on it, and admins set it by command. Not saved: a restart is back at speed 1.
 */
public final class GameSpeed {
    /** Real seconds to a day at each speed: 20 min, 10 min, 5 min, 10 s, 2 s. */
    public static final int[] SECONDS_PER_DAY = {1200, 600, 300, 10, 2};
    /** How long a proposed speed lasts, seconds. */
    public static final int[] DURATIONS = {60, 300, 900, 3600};
    private static final int VOTE_TICKS = 30 * 20;
    private static final int COOLDOWN_TICKS = 30 * 20;
    /** After this long at the top speed anyone may bring it down a step. */
    public static final int SLOW_TICKS = 30 * 20;
    private static final int TOP = SECONDS_PER_DAY.length - 1;

    private record Proposal(UUID by, String name, int speed, int seconds, long ends, Map<UUID, Boolean> votes) {
    }

    private static int speed;
    private static long since;
    /** The tick it falls back to speed 1; -1: never. */
    private static long until = -1;
    @Nullable
    private static Proposal vote;
    private static final Map<UUID, Long> cooldown = new HashMap<>();

    private GameSpeed() {
    }

    public static void reset() {
        speed = 0;
        since = 0;
        until = -1;
        vote = null;
        cooldown.clear();
    }

    public static String perDay(int s) {
        int sec = SECONDS_PER_DAY[s];
        return sec >= 60 ? sec / 60 + " minutes a day" : sec + " seconds a day";
    }

    public static String lasting(int seconds) {
        return seconds >= 3600 ? seconds / 3600 + (seconds == 3600 ? " hour" : " hours")
                : seconds / 60 + (seconds == 60 ? " minute" : " minutes");
    }

    /** Each server tick: the clock's extra ticks, the speed's end, the vote's timeout. */
    public static void tick(ServerLevel overworld) {
        MinecraftServer server = overworld.getServer();
        long now = server.getTickCount();
        if (until >= 0 && now >= until) {
            set(server, 0, -1);
        }
        if (vote != null && now >= vote.ends()) {
            resolve(server, false);
        }
        int extra = 1200 / SECONDS_PER_DAY[speed] - 1; // vanilla already adds one tick a tick: 20 minutes a day
        if (extra > 0 && overworld.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            overworld.setDayTime(overworld.getDayTime() + extra);
        }
    }

    /** Alone (not opened to LAN), there are no votes. */
    public static boolean voting(MinecraftServer server) {
        return !server.isSingleplayer() || server.isPublished();
    }

    public static boolean canPropose(ServerPlayer player) {
        return Peerage.get(player.getServer()).of(player.getUUID()).title.ordinal() >= NobleTitle.DUKE.ordinal();
    }

    /** Ticks until anyone may slow the top speed (0: now); -1 below the top speed. */
    private static long slowIn(MinecraftServer server) {
        return speed == TOP ? Math.max(0, since + SLOW_TICKS - server.getTickCount()) : -1;
    }

    /** A player clicked speed {@code s} (0-4) for {@code seconds} (one of {@link #DURATIONS}; ignored alone). */
    public static void request(ServerPlayer player, int s, int seconds) {
        MinecraftServer server = player.getServer();
        if (s < 0 || s > TOP || s == speed) {
            return;
        }
        if (!voting(server)) {
            set(server, s, -1);
            return;
        }
        if (s == TOP - 1 && slowIn(server) == 0) { // anyone may step down from the top speed after a while
            set(server, s, until);
            return;
        }
        if (!canPropose(player)) {
            player.sendSystemMessage(Component.literal("Only Dukes, Kings and Emperors may propose a speed").withStyle(ChatFormatting.RED));
            return;
        }
        if (vote != null) {
            player.sendSystemMessage(Component.literal("A speed vote is already under way").withStyle(ChatFormatting.RED));
            return;
        }
        long now = server.getTickCount();
        Long free = cooldown.get(player.getUUID());
        if (free != null && now < free) {
            player.sendSystemMessage(Component.literal("You may propose again in " + (free - now + 19) / 20 + " s").withStyle(ChatFormatting.RED));
            return;
        }
        if (java.util.Arrays.stream(DURATIONS).noneMatch(d -> d == seconds)) {
            return;
        }
        vote = new Proposal(player.getUUID(), player.getName().getString(), s, seconds, now + VOTE_TICKS, new HashMap<>());
        vote.votes().put(player.getUUID(), true);
        Component ask = Component.literal(vote.name() + " proposes speed " + (s + 1) + " (" + perDay(s) + ") for " + lasting(seconds) + ". ")
                .withStyle(ChatFormatting.GOLD)
                .append(button("[Yes]", ChatFormatting.GREEN, "/speed vote yes"))
                .append(" ")
                .append(button("[No]", ChatFormatting.RED, "/speed vote no"));
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (canPropose(p)) {
                p.sendSystemMessage(ask);
            }
        }
        count(server);
    }

    private static Component button(String text, ChatFormatting colour, String command) {
        return Component.literal(text).withStyle(st -> st.withColor(colour).withBold(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

    public static void vote(ServerPlayer player, boolean yes) {
        if (vote == null) {
            player.sendSystemMessage(Component.literal("No speed vote is under way").withStyle(ChatFormatting.RED));
        } else if (!canPropose(player)) {
            player.sendSystemMessage(Component.literal("Only Dukes, Kings and Emperors may vote").withStyle(ChatFormatting.RED));
        } else {
            vote.votes().put(player.getUUID(), yes);
            count(player.getServer());
        }
    }

    /** 60% of the Dukes, Kings and Emperors online, rounded up, must say yes; it ends as soon as that is settled. */
    private static void count(MinecraftServer server) {
        int voters = 0;
        int yes = 0;
        int no = 0;
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (canPropose(p)) {
                voters++;
                Boolean v = vote.votes().get(p.getUUID());
                yes += Boolean.TRUE.equals(v) ? 1 : 0;
                no += Boolean.FALSE.equals(v) ? 1 : 0;
            }
        }
        int need = need(voters);
        if (yes >= need) {
            resolve(server, true);
        } else if (voters - no < need) {
            resolve(server, false);
        }
    }

    static int need(int voters) {
        return (voters * 3 + 4) / 5; // ceil(60%)
    }

    private static void resolve(MinecraftServer server, boolean passed) {
        Proposal p = vote;
        vote = null;
        cooldown.put(p.by(), (long) server.getTickCount() + COOLDOWN_TICKS);
        if (passed) {
            set(server, p.speed(), server.getTickCount() + p.seconds() * 20L);
        } else {
            server.getPlayerList().broadcastSystemMessage(Component.literal("The vote for speed " + (p.speed() + 1) + " failed")
                    .withStyle(ChatFormatting.GRAY), false);
        }
    }

    /** Admins: speed {@code s} (0-4) for {@code seconds} (0: until changed), past any vote. */
    public static void force(MinecraftServer server, int s, int seconds) {
        vote = null;
        set(server, s, seconds > 0 ? server.getTickCount() + seconds * 20L : -1);
    }

    private static void set(MinecraftServer server, int s, long untilTick) {
        speed = s;
        since = server.getTickCount();
        until = s == 0 ? -1 : untilTick;
        String end = until < 0 ? "" : " for " + lasting((int) ((until - since) / 20));
        server.getPlayerList().broadcastSystemMessage(Component.literal("Game speed " + (s + 1) + ": " + perDay(s) + end)
                .withStyle(ChatFormatting.GOLD), false);
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            sync(p);
        }
    }

    /** Tells the player the speed, whether it is voted on, whether they may propose, and when the top speed may be slowed. */
    public static void sync(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        rafasconquest.net.ConquestNetwork.sendGameSpeed(player, speed, voting(server), canPropose(player), slowIn(server));
    }
}
