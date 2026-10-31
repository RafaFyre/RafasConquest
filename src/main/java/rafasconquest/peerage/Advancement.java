package rafasconquest.peerage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import rafasconquest.character.Rank;
import rafasconquest.data.Ranks;

/**
 * Finishing a quest, and climbing the two ladders: its emeralds; its renown on its ladder; the giver (a noble) knowing
 * the player from then on; and promotion. On the military ladder a player is promoted as their renown passes each
 * rank's mark ({@link #MILITARY}); on the noble ladder, a noble who knows them knights them once their noble renown
 * reaches {@link #KNIGHTHOOD} (offered in the noble's dialogue).
 */
public final class Advancement {
    /** Military renown to reach each rank (index: rank number); rank 1 needs none. */
    static final int[] MILITARY = {0, 0, 20, 60, 120, 200, 300, 450, 650};
    /** Noble renown a noble asks before knighting a player. */
    public static final int KNIGHTHOOD = 40;

    private Advancement() {
    }

    /** The player finishes a quest they have taken. */
    public static void complete(ServerPlayer player, Peerage peerage, Peerage.Taken taken) {
        Peerage.Peer peer = peerage.of(player.getUUID());
        peer.taken.remove(taken);
        Quest q = taken.quest;
        if (q.emeralds() > 0) {
            ItemStack pay = new ItemStack(Items.EMERALD, q.emeralds());
            if (!player.getInventory().add(pay)) {
                player.drop(pay, false);
            }
        }
        switch (q.ladder()) {
            case NOBLE -> peer.nobleRenown += q.renown();
            case MILITARY -> peer.militaryRenown += q.renown();
            default -> {
            }
        }
        if (q.giver() >= 0) {
            peer.known.add(q.giver());
        }
        peerage.changed();
        player.sendSystemMessage(Component.literal("Quest done: " + q.title() + ". " + (q.emeralds() > 0 ? q.emeralds() + " emeralds" : "")
                + (q.renown() > 0 ? ", " + q.renown() + " " + (q.ladder() == Quest.Ladder.NOBLE ? "noble" : "military") + " renown" : "") + "."));
        if (q.ladder() == Quest.Ladder.MILITARY) {
            promote(player, peer);
        }
    }

    /** Promotes a player up the ranks as far as their military renown carries them. */
    static void promote(ServerPlayer player, Peerage.Peer peer) {
        Ranks ranks = Ranks.get(player.server);
        Rank rank = ranks.of(player.getUUID());
        Rank reached = rank;
        while (reached.number() < Rank.GRAND_MARSHAL.number() && peer.militaryRenown >= MILITARY[reached.number() + 1]) {
            reached = Rank.of(reached.number() + 1);
        }
        if (reached != rank) {
            ranks.set(player.getUUID(), reached);
            player.sendSystemMessage(Component.literal("You are promoted to " + reached.title() + " (rank " + reached.number() + ")."));
            rafasconquest.gear.RankKits.issue(player, rank, reached);
        }
    }

    /** Military renown still needed for the next rank, or -1 at the top. */
    public static int toNextRank(Rank rank, int renown) {
        return rank.number() >= Rank.GRAND_MARSHAL.number() ? -1 : Math.max(0, MILITARY[rank.number() + 1] - renown);
    }
}
