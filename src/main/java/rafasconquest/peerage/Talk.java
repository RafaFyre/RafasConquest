package rafasconquest.peerage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import rafasconquest.character.Rank;
import rafasconquest.data.Dialogue;
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

    /**
     * What the speaker says, and the choices; who they are ({@code name}, and {@code title} shown in their nation's
     * banner colours, {@code colour} its base and {@code accent} its first layer, as RGB).
     */
    public record View(int entity, String name, String title, int colour, int accent, List<String> lines, List<Option> options) {
    }

    private Talk() {
    }

    /** The player speaks to a recruiter or noble. */
    public static void open(ServerPlayer player, Recruiter npc) {
        ConquestNetwork.sendTalk(player, List.of(view(player, npc)));
    }

    static View view(ServerPlayer player, Recruiter npc) {
        BaseMap map = ConquestRuntime.map();
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        peer.name = player.getName().getString();
        Faction nation = nationOf(npc);
        String nationName = nation == null ? Dialogue.say("common.no_nation") : nation.name();
        boolean serves = nation != null && serves(player, nation);
        List<String> lines = new ArrayList<>();
        List<Option> options = new ArrayList<>();
        String speaker = npc.getName().getString();
        String name = speaker;
        String title = nationName;
        if (npc.person() < 0) {
            name = Dialogue.say("recruiter.short_name");
            title = Dialogue.say("recruiter.title", "nation", nationName);
            // A recruiter: the nation's officer at its post.
            if (!serves) {
                Recruitment.Offer offer = Recruitment.offer(player, npc);
                lines.add(Dialogue.say("recruiter.greet_stranger", "nation", nationName));
                lines.add(Dialogue.say(offer.noble() ? "recruiter.offer_noble" : "recruiter.offer", "nation", nationName, "rank", offer.startRank()));
                options.add(new Option("enlist", Dialogue.say("recruiter.options.enlist")));
            } else {
                Rank rank = Ranks.get(player.server).of(player.getUUID());
                int next = Advancement.toNextRank(rank, peer.militaryRenown);
                lines.add(Dialogue.say("recruiter.greet_soldier", "rank", rank.title()) + " " + (next < 0 ? Dialogue.say("recruiter.top_rank")
                        : Dialogue.say("recruiter.next_rank", "renown", next, "next", Rank.of(rank.number() + 1).title())));
                lines.add(Dialogue.say("recruiter.board_hint"));
                questOptions(peer, npc, Quest.OFFICER, options, lines);
                String war = rafasconquest.politics.Wars.warCampOf(player.server, nation);
                if (war != null) {
                    lines.add(Dialogue.say("recruiter.at_war", "war", war));
                    options.add(new Option("camp", Dialogue.say("recruiter.options.camp")));
                }
            }
        } else if (map != null) {
            Person p = map.realm().people().get(npc.person());
            name = p.name();
            title = rafasconquest.people.Realm.title(p, map.titles().rank(p.hold().province())) + " of " + map.names().get(p.hold().province());
            if (peerage.replaced(p.hold().province())) {
                lines.add(Dialogue.say("noble.not_mine", "speaker", speaker));
            } else if (!serves) {
                lines.add(Dialogue.say("noble.not_serving", "speaker", speaker, "nation", nationName));
            } else {
                lines.add(Dialogue.say("noble.greet", "speaker", speaker, "player", Names.player(peer.name, peer.title)));
                if (peer.known.contains(npc.person())) {
                    lines.add(Dialogue.say("noble.known", "speaker", speaker));
                }
                questOptions(peer, npc, npc.person(), options, lines);
                if (peer.title == NobleTitle.NONE && peer.known.contains(npc.person()) && peer.nobleRenown >= Advancement.KNIGHTHOOD) {
                    lines.add(Dialogue.say("noble.knight_offer", "speaker", speaker));
                    options.add(new Option("kneel", Dialogue.say("noble.options.kneel")));
                }
                if (LandGrants.merit(map, peer, npc.person()) != null) {
                    lines.add(Dialogue.say("noble.land_offer", "speaker", speaker));
                    options.add(new Option("land", Dialogue.say("noble.options.land")));
                }
                fealty(map, peer, npc.person(), options);
                if (peer.hold < 0 && java.util.Arrays.stream(rafasconquest.core.TitleMap.Tier.values()).anyMatch(
                        tier -> !LandGrants.petition(map, npc.person(), tier, s -> peerage.heldBy(s) != null).isEmpty())) {
                    options.add(new Option("petition", Dialogue.say("noble.options.petition", "name", p.name(), "their", p.female() ? "her" : "his")));
                }
            }
            if (player.isCreative() && !peerage.replaced(p.hold().province())) {
                options.add(new Option("replace", Dialogue.say("noble.options.replace")));
            }
        }
        options.add(new Option("bye", Dialogue.say("common.farewell")));
        int colour = 0x404040;
        int accent = 0xE0E0E0;
        if (nation != null) {
            rafasconquest.faction.FactionBanner banner = rafasconquest.faction.FactionBanner.of(nation.id());
            colour = net.minecraft.world.item.DyeColor.byId(banner.base()).getTextureDiffuseColor() & 0xFFFFFF;
            accent = net.minecraft.world.item.DyeColor.byId(banner.layers().get(0).color()).getTextureDiffuseColor() & 0xFFFFFF;
        }
        return new View(npc.getId(), name, title, colour, accent, lines, options);
    }

    /** The quests this speaker can give (read on a board) or take reports of. */
    private static void questOptions(Peerage.Peer peer, Recruiter npc, int giver, List<Option> options, List<String> lines) {
        for (Peerage.Taken t : peer.taken) {
            if (gives(t.quest, npc, giver) && t.ready() && t.quest.type() != Quest.Type.DELIVER) {
                options.add(new Option("report:" + t.quest.id(), Dialogue.say("common.report", "quest", t.quest.title())));
            }
        }
        for (Quest q : peer.read.values()) {
            if (gives(q, npc, giver) && peer.taken(q.id()) == null) {
                options.add(new Option("take:" + q.id(), Dialogue.say("common.take", "quest", q.title())));
            }
        }
        if (options.isEmpty()) {
            lines.add(Dialogue.say("common.no_work"));
        }
    }

    /** Whether this speaker gives this quest: a noble their own; an officer those posted at their post. */
    private static boolean gives(Quest q, Recruiter npc, int giver) {
        if (q.giver() != giver) {
            return false;
        }
        return giver >= 0 || Math.abs(q.giverX() - npc.getBlockX()) <= 3 && Math.abs(q.giverZ() - npc.getBlockZ()) <= 3;
    }

    /** Noble renown enough for a character who doesn't know the player to swear fealty to them. */
    static final int VASSAL_RENOWN = 20;

    /**
     * Fealty between the player and a character: one the player outranks (not a ruler) they may ask to swear to them,
     * or release; one who outranks the player, the player may swear to (if they serve no one) or renounce (unless they
     * hold land of them).
     */
    private static void fealty(BaseMap map, Peerage.Peer peer, int person, List<Option> options) {
        int theirs = Retinues.level(map.realm(), person);
        int mine = Retinues.level(peer.title);
        if (peer.vassals.contains(person)) {
            options.add(new Option("release", Dialogue.say("noble.options.release")));
            rafasconquest.character.Job job = peer.vassalJobs.getOrDefault(person, rafasconquest.character.Job.HEAVY_INFANTRY);
            options.add(new Option("job", Dialogue.say("noble.options.job", "job", job.title())));
        } else if (mine > theirs && theirs < Retinues.TOP) {
            options.add(new Option("vassalize", Dialogue.say("noble.options.vassalize")));
        }
        if (peer.liege == person && peer.hold < 0) {
            options.add(new Option("renounce", Dialogue.say("noble.options.renounce")));
        } else if (peer.liege < 0 && theirs > mine) {
            options.add(new Option("swear", Dialogue.say("noble.options.swear")));
        }
    }

    /** The player chooses. */
    public static void choose(ServerPlayer player, int entityId, String key) {
        if (key.startsWith("council:")) { // the war council checks its own topics
            String refused = rafasconquest.bridge.RecruitsBattle.councilTopic(player, key.substring(8));
            if (!refused.isEmpty()) {
                player.sendSystemMessage(Component.literal(refused));
            }
            return;
        }
        Entity entity = player.serverLevel().getEntity(entityId);
        if (!(entity instanceof Recruiter npc) || entity.distanceTo(player) > REACH) {
            return;
        }
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        // Only what the view offered: a choice is checked against it, never trusted.
        View view = view(player, npc);
        if (key.startsWith("petition:")) { // a title picked on the map: allowed while the petition itself is offered
            if (view.options().stream().anyMatch(o -> o.key().equals("petition"))) {
                petition(player, npc, peerage, peer, key.substring(9));
            }
            return;
        }
        if (view.options().stream().noneMatch(o -> o.key().equals(key))) {
            return;
        }
        if (key.equals("petition")) {
            // Going on from "Which one?" opens the map to pick on (the client's DialogueScreen, by the "map:" key).
            ConquestNetwork.sendTalk(player, List.of(new View(view.entity(), view.name(), view.title(), view.colour(), view.accent(),
                    List.of(Dialogue.say("noble.which")), List.of(new Option("map:" + npc.person(), "Continue")))));
            return;
        }
        if (key.equals("enlist")) {
            Recruitment.accept(player, entityId);
        } else if (key.startsWith("take:")) {
            Quest q = peer.read.remove(Long.parseLong(key.substring(5)));
            if (q != null) {
                peerage.take(player.getUUID(), q);
                player.sendSystemMessage(Component.literal(Dialogue.say("quests.taken", "quest", q.title(), "x", q.targetX(), "z", q.targetZ())));
            }
        } else if (key.startsWith("report:")) {
            Peerage.Taken t = peer.taken(Long.parseLong(key.substring(7)));
            if (t != null && t.ready()) {
                Advancement.complete(player, peerage, t);
            }
        } else if (key.equals("kneel")) {
            peer.title = NobleTitle.KNIGHT;
            peerage.changed();
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.knighted", "speaker", npc.getName().getString(), "player", Names.player(peer.name, peer.title))));
        } else if (key.equals("vassalize")) {
            String name = npc.getName().getString();
            if (peer.known.contains(npc.person()) || peer.nobleRenown >= VASSAL_RENOWN) {
                peer.vassals.add(npc.person());
                peerage.changed();
                player.sendSystemMessage(Component.literal(Dialogue.say("noble.vassal_yes", "speaker", name)));
            } else {
                player.sendSystemMessage(Component.literal(Dialogue.say("noble.vassal_no", "speaker", name)));
            }
        } else if (key.equals("job")) {
            rafasconquest.character.Job[] jobs = rafasconquest.character.Job.values();
            rafasconquest.character.Job now = peer.vassalJobs.getOrDefault(npc.person(), rafasconquest.character.Job.HEAVY_INFANTRY);
            peer.vassalJobs.put(npc.person(), jobs[(now.ordinal() + 1) % jobs.length]);
            peerage.changed();
        } else if (key.equals("release")) {
            peer.vassals.remove(npc.person());
            peer.vassalJobs.remove(npc.person());
            peerage.changed();
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.released", "speaker", npc.getName().getString())));
        } else if (key.equals("swear")) {
            peer.liege = npc.person();
            peerage.changed();
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.sworn", "speaker", npc.getName().getString())));
        } else if (key.equals("renounce")) {
            peer.liege = -1;
            peerage.changed();
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.renounced", "speaker", npc.getName().getString())));
        } else if (key.equals("replace")) {
            // Creative only (the view offers it to no one else): the player takes the noble's place, with their hold,
            // liege and vassals; the noble's traits and bonds are theirs through peer.persona.
            BaseMap map = ConquestRuntime.map();
            if (map == null) {
                return;
            }
            Person p = map.realm().people().get(npc.person());
            int seat = p.hold().province();
            rafasconquest.core.TitleMap.Tier rank = map.titles().rank(seat);
            peerage.release(player.getUUID());
            peerage.grant(player.getUUID(), seat, p.liege(), "replaced");
            peer.title = rank == null ? NobleTitle.COUNT : NobleTitle.of(rank);
            peer.persona = npc.person();
            peer.vassals.addAll(map.realm().vassals(npc.person()));
            PeerageSync.toAll(player.server);
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.replaced", "speaker", p.name(), "player", Names.player(peer.name, peer.title))));
        } else if (key.equals("land")) {
            land(player, npc, peerage, peer);
        } else if (key.equals("camp")) {
            Faction nation = nationOf(npc);
            if (nation != null) {
                player.sendSystemMessage(Component.literal(rafasconquest.politics.Wars.toWarCamp(player, nation) + "."));
            }
            return; // they have left the recruiter
        } else {
            return;
        }
        if (!key.equals("bye")) {
            open(player, npc); // the conversation goes on, with what is left to say
        }
    }

    /** The noble grants the title the player picked, whoever held it, and takes the player as their vassal. */
    private static void petition(ServerPlayer player, Recruiter npc, Peerage peerage, Peerage.Peer peer, String picked) {
        BaseMap map = ConquestRuntime.map();
        int seat;
        try {
            seat = Integer.parseInt(picked);
        } catch (NumberFormatException e) {
            return;
        }
        rafasconquest.core.TitleMap.Tier tier = map == null ? null : map.titles().seatOf(seat);
        if (tier == null || !LandGrants.petition(map, npc.person(), tier, s -> peerage.heldBy(s) != null).contains(seat)) {
            return;
        }
        peerage.grant(player.getUUID(), seat, npc.person(), map.realm().holderOf(seat) >= 0 ? "dismissed" : null);
        peer.title = NobleTitle.of(tier);
        PeerageSync.toAll(player.server);
        player.sendSystemMessage(Component.literal(Dialogue.say("noble.petition_granted", "speaker", npc.getName().getString(),
                "title", map.titleName(tier, seat), "player", Names.player(peer.name, peer.title))));
        open(player, npc);
    }

    private static void land(ServerPlayer player, Recruiter npc, Peerage peerage, Peerage.Peer peer) {
        BaseMap map = ConquestRuntime.map();
        if (map == null) {
            return;
        }
        Peerage.Merit merit = LandGrants.merit(map, peer, npc.person());
        LandGrants.Grant grant = merit == null ? null : LandGrants.choose(map, peerage, npc.person(), new Random(player.getRandom().nextLong()));
        if (grant == null) {
            player.sendSystemMessage(Component.literal(Dialogue.say("noble.no_land", "speaker", npc.getName().getString())));
            return;
        }
        peer.merits.remove(merit);
        peerage.grant(player.getUUID(), grant.seat(), grant.liege(), grant.fate());
        PeerageSync.toAll(player.server);
        player.sendSystemMessage(Component.literal(Dialogue.say("noble.land_granted", "speaker", npc.getName().getString(), "way", grant.way().says(),
                "county", map.names().get(grant.seat()), "player", Names.player(peer.name, peer.title))));
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
