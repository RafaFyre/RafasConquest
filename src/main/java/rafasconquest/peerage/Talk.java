package rafasconquest.peerage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import rafasconquest.character.Rank;
import rafasconquest.data.Ranks;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.npc.Recruiter;
import rafasconquest.npc.Recruitment;
import rafasconquest.people.Person;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Talking to a recruiter or a noble ({@link Recruiter}): what they say, and the choices the player has, worked out on
 * the server. A recruiter offers to enlist a player who doesn't serve their nation; to one who does, as its officer,
 * the military quests the player has read on the board, and the reports of those done. A noble deals only with those
 * who serve their nation (who have enlisted): their quests read on the board, reports, knighthood (once the player's
 * noble renown reaches {@link Advancement#KNIGHTHOOD} and the noble knows them), and land (once the player has earned
 * it in battle, {@link LandGrants}).
 */
public final class Talk {
    /** How near the speaker a player must stand to choose. */
    static final double REACH = 8;

    /** One choice: what it does (sent back), and what it says. */
    public record Option(String key, String label) {
    }

    /** What the speaker says, and the choices. */
    public record View(int entity, String speaker, List<String> lines, List<Option> options) {
    }

    private Talk() {
    }

    /** The player speaks to a recruiter or noble. */
    public static void open(ServerPlayer player, Recruiter npc) {
        ConquestNetwork.sendTalk(player, view(player, npc));
    }

    static View view(ServerPlayer player, Recruiter npc) {
        BaseMap map = ConquestRuntime.map();
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        peer.name = player.getName().getString();
        Faction nation = nationOf(npc);
        String nationName = nation == null ? "no nation" : nation.name();
        boolean serves = nation != null && serves(player, nation);
        List<String> lines = new ArrayList<>();
        List<Option> options = new ArrayList<>();
        String speaker = npc.getName().getString();
        if (npc.person() < 0) {
            // A recruiter: the nation's officer at its post.
            if (!serves) {
                Recruitment.Offer offer = Recruitment.offer(player, npc);
                lines.add("Well met, traveller. " + nationName + " has need of those who can hold a line.");
                lines.add(offer.noble() ? "One of your standing would not serve in the ranks: you would join as a " + offer.startRank()
                        + ", and lead soldiers of your own." : "Swear to " + nationName + " and you join its army as a " + offer.startRank()
                        + ". Serve well and you will rise.");
                options.add(new Option("enlist", "Join and enlist"));
            } else {
                Rank rank = Ranks.get(player.server).of(player.getUUID());
                int next = Advancement.toNextRank(rank, peer.militaryRenown);
                lines.add("Well met, " + rank.title() + ". " + (next < 0 ? "There is no higher rank to give you."
                        : next + " more renown and you will be a " + Rank.of(rank.number() + 1).title() + "."));
                lines.add("Read the board for the army's work, then come to me.");
                questOptions(peer, npc, Quest.OFFICER, options, lines);
            }
        } else if (map != null) {
            Person p = map.realm().people().get(npc.person());
            if (peerage.replaced(p.hold().province())) {
                lines.add("This land is no longer mine to give.");
            } else if (!serves) {
                lines.add("I have no dealings with those who do not serve " + nationName + ". Enlist at the recruitment post first.");
            } else {
                lines.add("Well met, " + Names.player(peer.name, peer.title) + ".");
                if (peer.known.contains(npc.person())) {
                    lines.add("You have served me well.");
                }
                questOptions(peer, npc, npc.person(), options, lines);
                if (peer.title == NobleTitle.NONE && peer.known.contains(npc.person()) && peer.nobleRenown >= Advancement.KNIGHTHOOD) {
                    lines.add("Your name is spoken well of in my hall. Kneel, and rise a knight.");
                    options.add(new Option("kneel", "Kneel to be knighted"));
                }
                if (LandGrants.merit(map, peer, npc.person()) != null) {
                    lines.add("I have heard what you did in battle. Such service deserves land.");
                    options.add(new Option("land", "Ask for land"));
                }
            }
        }
        options.add(new Option("bye", "Farewell"));
        return new View(npc.getId(), speaker, lines, options);
    }

    /** The quests this speaker can give (read on a board) or take reports of. */
    private static void questOptions(Peerage.Peer peer, Recruiter npc, int giver, List<Option> options, List<String> lines) {
        for (Peerage.Taken t : peer.taken) {
            if (gives(t.quest, npc, giver) && t.ready() && t.quest.type() != Quest.Type.DELIVER) {
                options.add(new Option("report:" + t.quest.id(), "Report: " + t.quest.title()));
            }
        }
        for (Quest q : peer.read.values()) {
            if (gives(q, npc, giver) && peer.taken(q.id()) == null) {
                options.add(new Option("take:" + q.id(), "Take: " + q.title()));
            }
        }
        if (options.isEmpty()) {
            lines.add("I have no work for you that you have read of. See the board.");
        }
    }

    /** Whether this speaker gives this quest: a noble their own; an officer those posted at their post. */
    private static boolean gives(Quest q, Recruiter npc, int giver) {
        if (q.giver() != giver) {
            return false;
        }
        return giver >= 0 || Math.abs(q.giverX() - npc.getBlockX()) <= 3 && Math.abs(q.giverZ() - npc.getBlockZ()) <= 3;
    }

    /** The player chooses. */
    public static void choose(ServerPlayer player, int entityId, String key) {
        Entity entity = player.serverLevel().getEntity(entityId);
        if (!(entity instanceof Recruiter npc) || entity.distanceTo(player) > REACH) {
            return;
        }
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        // Only what the view offered: a choice is checked against it, never trusted.
        View view = view(player, npc);
        if (view.options().stream().noneMatch(o -> o.key().equals(key))) {
            return;
        }
        if (key.equals("enlist")) {
            Recruitment.accept(player, entityId);
        } else if (key.startsWith("take:")) {
            Quest q = peer.read.remove(Long.parseLong(key.substring(5)));
            if (q != null) {
                peerage.take(player.getUUID(), q);
                player.sendSystemMessage(Component.literal("Quest taken: " + q.title() + " (" + q.targetX() + ", " + q.targetZ() + ")."));
            }
        } else if (key.startsWith("report:")) {
            Peerage.Taken t = peer.taken(Long.parseLong(key.substring(7)));
            if (t != null && t.ready()) {
                Advancement.complete(player, peerage, t);
            }
        } else if (key.equals("kneel")) {
            peer.title = NobleTitle.KNIGHT;
            peerage.changed();
            player.sendSystemMessage(Component.literal(npc.getName().getString() + " knights you. Rise, " + Names.player(peer.name, peer.title) + "."));
        } else if (key.equals("land")) {
            land(player, npc, peerage, peer);
        } else {
            return;
        }
        if (!key.equals("bye")) {
            open(player, npc); // the conversation goes on, with what is left to say
        }
    }

    private static void land(ServerPlayer player, Recruiter npc, Peerage peerage, Peerage.Peer peer) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return;
        }
        Peerage.Merit merit = LandGrants.merit(map, peer, npc.person());
        LandGrants.Grant grant = merit == null ? null : LandGrants.choose(map, peerage, npc.person(), new Random(player.getRandom().nextLong()));
        if (grant == null) {
            player.sendSystemMessage(Component.literal(npc.getName().getString() + ": I have no land to give you now."));
            return;
        }
        peer.merits.remove(merit);
        peerage.grant(player.getUUID(), grant.seat(), grant.liege(), grant.fate());
        PeerageSync.toAll(player.server);
        player.sendSystemMessage(Component.literal(npc.getName().getString() + " " + grant.way().says() + ": the county of "
                + map.names().get(grant.seat()) + ". You are now " + Names.player(peer.name, peer.title) + "."));
    }

    /** Whether a player serves a nation: is in its faction (has enlisted). */
    public static boolean serves(ServerPlayer player, Faction nation) {
        return player.getTeam() instanceof PlayerTeam team && team.getDisplayName().getString().equals(nation.name());
    }

    static Faction nationOf(Recruiter npc) {
        ConquestServer server = ConquestServer.get();
        return server == null || npc.nation() == null ? null : server.data().faction(npc.nation());
    }
}
