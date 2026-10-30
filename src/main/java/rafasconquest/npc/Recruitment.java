package rafasconquest.npc;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import rafasconquest.character.Rank;
import rafasconquest.data.Ranks;
import rafasconquest.faction.Faction;
import rafasconquest.sim.ConquestServer;

/**
 * What a recruiter ({@link Recruiter}) offers, and taking it: joining their nation (its Recruits faction, leaving any
 * other) and enlisting in its army, at rank 1, Soldier, or at rank 5, Major, for a player of noble title. A player
 * already of higher rank keeps it.
 */
public final class Recruitment {
    /** How near the recruiter a player must stand to take the offer. */
    static final double REACH = 8;

    /** The offer, as the dialogue shows it. */
    public record Offer(int recruiter, String recruiterName, String nation, boolean member, boolean noble, String startRank) {
    }

    private Recruitment() {
    }

    public static Offer offer(ServerPlayer player, Recruiter recruiter) {
        Faction nation = nationOf(recruiter);
        String name = nation == null ? "no nation" : nation.name();
        boolean member = nation != null && player.getTeam() instanceof net.minecraft.world.scores.PlayerTeam team
                && team.getDisplayName().getString().equals(nation.name());
        boolean noble = noble(player);
        return new Offer(recruiter.getId(), recruiter.getName().getString(), name, member, noble, startRank(noble).title());
    }

    /** Whether a player holds a noble title: knighted, or granted land ({@link rafasconquest.peerage.Peerage}). */
    public static boolean noble(ServerPlayer player) {
        return rafasconquest.peerage.Peerage.get(player.server).of(player.getUUID()).title.noble();
    }

    public static Rank startRank(boolean noble) {
        return noble ? Rank.MAJOR : Rank.SOLDIER;
    }

    /** The player takes the offer of the recruiter with this entity id. */
    public static void accept(ServerPlayer player, int recruiterId) {
        Entity entity = player.serverLevel().getEntity(recruiterId);
        if (!(entity instanceof Recruiter recruiter) || entity.distanceTo(player) > REACH) {
            return;
        }
        Faction nation = nationOf(recruiter);
        ConquestServer server = ConquestServer.get();
        if (nation == null || server == null) {
            player.sendSystemMessage(Component.literal("This post recruits for no one."));
            return;
        }
        if (!server.recruits().join(player, nation)) {
            player.sendSystemMessage(Component.literal("You couldn't join " + nation.name() + " just now."));
            return;
        }
        Ranks ranks = Ranks.get(player.server);
        Rank start = startRank(noble(player));
        if (ranks.of(player.getUUID()).number() < start.number()) {
            ranks.set(player.getUUID(), start);
        }
        Rank rank = ranks.of(player.getUUID());
        player.sendSystemMessage(Component.literal("You have joined " + nation.name() + " and enlisted in its army as "
                + rank.title() + " (rank " + rank.number() + ")."));
    }

    private static Faction nationOf(Recruiter recruiter) {
        ConquestServer server = ConquestServer.get();
        return server == null || recruiter.nation() == null ? null : server.data().faction(recruiter.nation());
    }
}
