package rafasconquest.peerage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

import java.util.ArrayList;

/**
 * How taken quests move on: reaching a place (checked once a second; a delivery is done on arrival, a scouting once
 * reported), and killing hostile creatures about a place for a clearing.
 */
public final class QuestProgress {
    private static int ticks;

    private QuestProgress() {
    }

    public static void tick(MinecraftServer server) {
        if (++ticks % 20 != 0) {
            return;
        }
        Peerage peerage = Peerage.get(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Peerage.Peer peer = peerage.peers().get(player.getUUID());
            if (peer == null || peer.taken.isEmpty()) {
                continue;
            }
            for (Peerage.Taken t : new ArrayList<>(peer.taken)) {
                Quest q = t.quest;
                if (t.reached || q.type() == Quest.Type.CLEAR
                        || Math.hypot(player.getX() - q.targetX(), player.getZ() - q.targetZ()) > Quest.ARRIVE) {
                    continue;
                }
                t.reached = true;
                peerage.changed();
                if (q.type() == Quest.Type.DELIVER) {
                    Advancement.complete(player, peerage, t);
                } else {
                    player.sendSystemMessage(Component.literal("You have scouted " + q.targetName() + ". Report to "
                            + (q.giver() == Quest.BOARD ? "the board" : q.giverName()) + "."));
                }
            }
        }
    }

    /** A player killed a creature: it counts toward any clearing about where it fell. */
    public static void killed(ServerPlayer player, LivingEntity victim) {
        if (!(victim instanceof Enemy)) {
            return;
        }
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.peers().get(player.getUUID());
        if (peer == null) {
            return;
        }
        for (Peerage.Taken t : peer.taken) {
            Quest q = t.quest;
            if (q.type() == Quest.Type.CLEAR && t.kills < q.need()
                    && Math.hypot(victim.getX() - q.targetX(), victim.getZ() - q.targetZ()) <= Quest.CLEAR_REACH) {
                t.kills++;
                peerage.changed();
                player.sendSystemMessage(Component.literal(q.targetName() + ": " + t.kills + " / " + q.need() + " cleared"
                        + (t.ready() ? ". Report to " + (q.giver() == Quest.BOARD ? "the board" : q.giverName()) + "." : ".")));
            }
        }
    }
}
