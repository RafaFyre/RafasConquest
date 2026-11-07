package rafasconquest.bridge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.battle.Doctrine.Formation;
import rafasconquest.battle.Doctrine.Strategy;
import rafasconquest.battle.WarPlan;
import rafasconquest.character.BattleRole;
import rafasconquest.character.Rank;
import rafasconquest.character.Trait;
import rafasconquest.data.Ranks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * The war council in an army's command tent, during deployment: the general and the Marshals of the two wings stand
 * around the command table. An officer of the army (rank 5 and up) who right-clicks the general is briefed, a line at
 * a time: the plan, the enemy, their weaknesses, and the officer's own part. Then they may talk on; what is on offer
 * depends on the officers' traits and on what the player has asked: a bold Marshal invites them to a flank attack, a
 * cautious one asks for support, a cunning general wants a bait, the enemy general riding at the head of the charge
 * (once asked about) can be hunted, and a proud general won't have the plan questioned. Choosing a part sets the
 * player's role for the coming charge. All of it is said in the dialogue screen ({@link #pages}); a choice comes back
 * as {@code council:<topic>}.
 */
final class WarCouncil {
    /** One of the officers at the council, and their figure in the tent (its entity id, or -1). */
    record Officer(String title, String name, Trait trait, int entity) {
        String called() {
            return title + " " + name;
        }
    }

    /**
     * What the council knows, from one army's side; {@code nation} its own, {@code accent} its banner's second colour,
     * {@code vassals} a vassal officer's figure in the tent (its entity id by their character, or -1).
     */
    record Facts(int side, TextColor colour, int accent, String nation, Officer general, Officer left, Officer right, WarPlan plan,
                 int soldiers, String enemyNation, String enemyGeneral, WarPlan enemyPlan, int enemySoldiers,
                 List<String> weaknesses, int day, java.util.function.IntUnaryOperator vassals) {
    }

    /** A line someone says to the player, and who says it, as the dialogue screen shows it. */
    record Said(int entity, String name, String title, int colour, int accent, String text) {
    }

    private static final class Talk {
        final Deque<Said> queue = new ArrayDeque<>();
        List<rafasconquest.peerage.Talk.Option> options = List.of();
        long nextAt;
        boolean briefed;
        boolean askedEnemy;
        boolean askedWeakness;
        int side;
    }

    private final Map<UUID, Talk> talks = new HashMap<>();
    /** Told who speaks first as a talk is said (the player it is said to, and the speaker as they are called), so the council can look at them. */
    java.util.function.BiConsumer<ServerPlayer, Integer> onSpeak;

    /**
     * What is to be said, as the dialogue screen's pages: each run of lines by one speaker a page, the choices on the
     * last (none: the talk ends there).
     */
    static List<rafasconquest.peerage.Talk.View> pages(List<Said> said, List<rafasconquest.peerage.Talk.Option> options) {
        List<rafasconquest.peerage.Talk.View> views = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < said.size(); i++) {
            Said s = said.get(i);
            lines.add(s.text());
            boolean last = i == said.size() - 1;
            if (last || !said.get(i + 1).name().equals(s.name())) {
                views.add(new rafasconquest.peerage.Talk.View(s.entity(), s.name(), s.title(), s.colour(), s.accent(), lines,
                        last ? options : List.of()));
                lines = new ArrayList<>();
            }
        }
        return views;
    }

    /** One line said at once, ending the talk (a refusal). */
    private static void send(ServerPlayer player, Said said) {
        rafasconquest.net.ConquestNetwork.sendTalk(player, pages(List.of(said), List.of()));
    }

    /**
     * A player right-clicked one of the council: the general briefs them (as below); a wing's Marshal speaks for their
     * wing; a vassal officer answers to the player who is their lord, and to no one else.
     */
    void approach(ServerPlayer player, Facts facts, boolean sameArmy, long now, String who) {
        if (who.startsWith("vassal:")) {
            String[] parts = who.split(":");
            vassal(player, facts, Integer.parseInt(parts[1]), UUID.fromString(parts[2]), now);
        } else if (who.equals("left") || who.equals("right")) {
            marshal(player, facts, who, sameArmy, now);
        } else {
            approach(player, facts, sameArmy, now);
        }
    }

    /** A wing's Marshal: a greeting by their trait, and what they offer for their wing. */
    private void marshal(ServerPlayer player, Facts facts, String wing, boolean sameArmy, long now) {
        Officer m = wing.equals("left") ? facts.left() : facts.right();
        Rank rank = Ranks.get(player.server).of(player.getUUID());
        if (!sameArmy) {
            send(player, say(facts, m, t("marshal.stranger")));
            return;
        }
        Talk talk = fresh(player, facts, now);
        talk.queue.add(say(facts, m, t(switch (m.trait()) {
            case BOLD -> "marshal.greet_bold";
            case CAUTIOUS -> "marshal.greet_cautious";
            default -> "marshal.greet";
        }, "rank", rank.title(), "wing", t("wings." + wing))));
        talk.options = marshalOptions(m, wing, rank.officer());
    }

    /** A council line from {@code dialogue/council.toml}. */
    private static String t(String key, Object... vars) {
        return rafasconquest.data.Dialogue.say("council." + key, vars);
    }

    private static List<rafasconquest.peerage.Talk.Option> marshalOptions(Officer m, String wing, boolean officer) {
        List<rafasconquest.peerage.Talk.Option> choices = new ArrayList<>();
        String w = t("wings." + wing);
        if (officer && m.trait() == Trait.BOLD) {
            choices.add(choice(t("options.marshal_attack", "wing", w), "flank_" + wing));
        } else if (officer && m.trait() == Trait.CAUTIOUS) {
            choices.add(choice(t("options.marshal_support", "wing", w), "support_" + wing));
        } else if (officer) {
            choices.add(choice(t("options.marshal_offer", "wing", w), "wing_" + wing));
        }
        choices.add(choice(t("options.leave"), "leave_" + wing));
        return choices;
    }

    /** A vassal officer: answers to their lord with their troops and their job, which the lord may change here. */
    private void vassal(ServerPlayer player, Facts facts, int person, UUID lord, long now) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            return;
        }
        Officer me = vassalOfficer(map, facts, person);
        if (!player.getUUID().equals(lord)) {
            send(player, say(facts, me, t("vassal.not_yours")));
            return;
        }
        Talk talk = fresh(player, facts, now);
        talk.queue.add(say(facts, me, t("vassal.greet")));
        talk.options = vassalOptions(person);
    }

    private static Officer vassalOfficer(rafasconquest.map.BaseMap map, Facts facts, int person) {
        return new Officer(rafasconquest.peerage.Retinues.rank(map.realm(), person).title(), map.realm().people().get(person).name(),
                facts.general().trait(), facts.vassals().applyAsInt(person));
    }

    private static List<rafasconquest.peerage.Talk.Option> vassalOptions(int person) {
        return List.of(choice(t("options.vassal_report"), "vassal_report_" + person),
                choice(t("options.vassal_job"), "vassal_job_" + person),
                choice(t("options.leave"), "vassal_leave_" + person));
    }

    /** A talk begun afresh with one of the council: what was still to be said is dropped. */
    private Talk fresh(ServerPlayer player, Facts facts, long now) {
        Talk talk = talks.computeIfAbsent(player.getUUID(), id -> new Talk());
        if (talk.side != facts.side()) {
            talk.side = facts.side();
            talk.briefed = false;
        }
        talk.queue.clear();
        talk.options = List.of();
        talk.nextAt = now;
        return talk;
    }

    /** The choices of a Marshal or a vassal officer, which need no briefing first. */
    private boolean aside(ServerPlayer player, Facts facts, String topic, long now) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (topic.startsWith("vassal_") && map != null) {
            String[] parts = topic.split("_");
            int person = Integer.parseInt(parts[2]);
            rafasconquest.peerage.Peerage peerage = rafasconquest.peerage.Peerage.get(player.server);
            rafasconquest.peerage.Peerage.Peer peer = peerage.of(player.getUUID());
            if (!peer.vassals.contains(person) || person < 0 || person >= map.realm().people().size()) {
                return true;
            }
            Officer me = vassalOfficer(map, facts, person);
            Talk talk = fresh(player, facts, now);
            rafasconquest.character.Job job = peer.vassalJobs.getOrDefault(person, rafasconquest.character.Job.HEAVY_INFANTRY);
            switch (parts[1]) {
                case "report" -> talk.queue.add(say(facts, me, t("vassal.report", "troops", rafasconquest.peerage.Retinues.troops(map.realm(), person),
                        "job", job.title().toLowerCase(Locale.ROOT))));
                case "job" -> {
                    rafasconquest.character.Job[] jobs = rafasconquest.character.Job.values();
                    job = jobs[(job.ordinal() + 1) % jobs.length];
                    peer.vassalJobs.put(person, job);
                    peerage.changed();
                    talk.queue.add(say(facts, me, t("vassal.job", "job", job.title().toLowerCase(Locale.ROOT))));
                }
                default -> {
                    talk.queue.add(say(facts, me, t("vassal.leave")));
                    return true;
                }
            }
            talk.options = vassalOptions(person);
            return true;
        }
        String wing = topic.endsWith("left") ? "left" : "right";
        Officer m = wing.equals("left") ? facts.left() : facts.right();
        if (topic.startsWith("flank_")) {
            setRole(player, BattleRole.FLANKER);
            fresh(player, facts, now).queue.add(say(facts, m, t("marshal.flank")));
            return true;
        }
        if (topic.startsWith("support_")) {
            setRole(player, BattleRole.SUPPORT);
            fresh(player, facts, now).queue.add(say(facts, m, t("marshal.support")));
            return true;
        }
        if (topic.startsWith("wing_")) {
            setRole(player, BattleRole.SUPPORT);
            fresh(player, facts, now).queue.add(say(facts, m, t("marshal.wing")));
            return true;
        }
        if (topic.startsWith("leave_")) {
            fresh(player, facts, now).queue.add(say(facts, m, t("marshal.leave")));
            return true;
        }
        return false;
    }

    /** A player right-clicked a council member: only officers of its army are heard. */
    void approach(ServerPlayer player, Facts facts, boolean sameArmy, long now) {
        Rank rank = Ranks.get(player.server).of(player.getUUID());
        if (!sameArmy) {
            send(player, say(facts, facts.general(), t("general.stranger")));
            return;
        }
        if (!rank.officer()) {
            send(player, say(facts, facts.general(), t("general.not_officer", "rank", rank.title())));
            return;
        }
        Talk talk = talks.computeIfAbsent(player.getUUID(), id -> new Talk());
        if (talk.side != facts.side()) {
            talk.side = facts.side();
            talk.briefed = false;
        }
        if (!talk.queue.isEmpty()) {
            return; // still being briefed
        }
        talk.nextAt = now;
        if (talk.briefed) {
            talk.queue.add(say(facts, facts.general(), t("general.again", "rank", rank.title())));
        } else {
            brief(talk, facts, rank, player);
            talk.briefed = true;
        }
        talk.options = options(talk, facts);
    }

    /** A choice the player clicked ({@code /council <topic>}). */
    void topic(ServerPlayer player, Facts facts, String topic, long now) {
        if (aside(player, facts, topic, now)) {
            return; // a Marshal's or a vassal's
        }
        Talk talk = talks.get(player.getUUID());
        if (talk == null || !talk.briefed || talk.side != facts.side()) {
            player.sendSystemMessage(Component.literal(t("speak_to_general_first")));
            return;
        }
        Rank rank = Ranks.get(player.server).of(player.getUUID());
        talk.queue.clear();
        talk.options = List.of();
        talk.nextAt = now;
        WarPlan enemy = facts.enemyPlan();
        switch (topic) {
            case "enemy" -> {
                talk.askedEnemy = true;
                talk.queue.add(say(facts, facts.general(), t("general.enemy_doctrine", "general", facts.enemyGeneral(), "doctrines", doctrines(enemy))));
                for (String line : enemyHabits(enemy)) {
                    talk.queue.add(say(facts, facts.general(), line));
                }
            }
            case "weakness" -> {
                talk.askedWeakness = true;
                if (facts.weaknesses().isEmpty()) {
                    talk.queue.add(say(facts, facts.general(), t("general.no_weakness")));
                }
                for (String line : facts.weaknesses()) {
                    talk.queue.add(say(facts, facts.general(), line));
                }
            }
            case "flank_left", "flank_right" -> {
                Officer m = topic.endsWith("left") ? facts.left() : facts.right();
                setRole(player, BattleRole.FLANKER);
                talk.queue.add(say(facts, m, t("marshal.flank")));
            }
            case "support_left", "support_right" -> {
                Officer m = topic.endsWith("left") ? facts.left() : facts.right();
                setRole(player, BattleRole.SUPPORT);
                talk.queue.add(say(facts, m, t("marshal.support")));
            }
            case "bait" -> {
                setRole(player, BattleRole.BAIT);
                talk.queue.add(say(facts, facts.general(), t("general.bait")));
            }
            case "hunt" -> {
                setRole(player, BattleRole.KILL_SQUAD);
                talk.queue.add(say(facts, facts.general(), t("general.hunt", "general", facts.enemyGeneral())));
            }
            case "propose" -> {
                setRole(player, BattleRole.FLANKER);
                talk.queue.add(say(facts, facts.general(), t("general.propose")));
            }
            case "question" -> talk.queue.add(say(facts, facts.general(), t("general.question", "rank", rank.title())));
            case "line" -> {
                setRole(player, BattleRole.FRONTLINE);
                talk.queue.add(say(facts, facts.general(), t("general.line")));
            }
            case "leave" -> {
                talk.queue.add(say(facts, facts.general(), t("general.dismissed", "rank", rank.title())));
                return;
            }
            default -> {
                return;
            }
        }
        talk.options = options(talk, facts);
    }

    /** Sends each player what is to be said to them, as the dialogue screen's pages, which pace it themselves. */
    void tick(MinecraftServer server, long now) {
        for (Map.Entry<UUID, Talk> entry : talks.entrySet()) {
            Talk talk = entry.getValue();
            if (talk.queue.isEmpty() || now < talk.nextAt) {
                continue;
            }
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player != null) {
                rafasconquest.net.ConquestNetwork.sendTalk(player, pages(List.copyOf(talk.queue), talk.options));
                if (onSpeak != null) {
                    onSpeak.accept(player, talk.queue.peek().entity());
                }
            }
            talk.queue.clear();
            talk.options = List.of();
        }
    }

    /** Lines for a player from someone other than the council (a messenger), said like a briefing. */
    void deliver(ServerPlayer player, List<Said> lines, long now) {
        Talk talk = talks.computeIfAbsent(player.getUUID(), id -> new Talk());
        if (talk.queue.isEmpty()) {
            talk.nextAt = now;
        }
        talk.queue.addAll(lines);
    }

    /** The battle begins: the council breaks up, and whatever was still to be said goes unsaid. */
    void adjourn() {
        talks.values().forEach(t -> t.queue.clear());
    }

    private static void brief(Talk talk, Facts facts, Rank rank, ServerPlayer player) {
        Officer g = facts.general();
        Deque<Said> q = talk.queue;
        q.add(say(facts, g, t("general.brief_open", "rank", rank.title(), "day", facts.day())));
        q.add(say(facts, g, t("general.brief_numbers", "ours", facts.soldiers(), "theirs", facts.enemySoldiers(),
                "odds", odds(facts.soldiers(), facts.enemySoldiers()))));
        q.add(say(facts, g, t("general.brief_doctrine", "doctrines", doctrines(facts.plan()), "plan", ourPlan(facts.plan()))));
        q.add(say(facts, g, t("general.brief_stand", "distribution", facts.plan().distribution().title().toLowerCase(Locale.ROOT))));
        q.add(say(facts, g, t("general.brief_enemy", "nation", facts.enemyNation(), "general", facts.enemyGeneral())));
        if (!facts.weaknesses().isEmpty()) {
            q.add(say(facts, g, t("general.brief_scouts", "weakness", facts.weaknesses().get(0))));
        }
        q.add(say(facts, facts.left(), opener(facts.left(), "left")));
        q.add(say(facts, facts.right(), opener(facts.right(), "right")));
        BattleRole role = Ranks.get(player.server).role(player.getUUID());
        q.add(say(facts, g, t("general.brief_role", "role", role.title())));
    }

    /** What a wing's Marshal says when the briefing comes to them, by their trait. */
    private static String opener(Officer m, String wing) {
        return t("marshal.opener_" + m.trait().name().toLowerCase(Locale.ROOT), "wing", t("wings." + wing));
    }

    /** The choices on offer now. */
    private static List<rafasconquest.peerage.Talk.Option> options(Talk talk, Facts facts) {
        List<rafasconquest.peerage.Talk.Option> choices = new ArrayList<>();
        if (!talk.askedEnemy) {
            choices.add(choice(t("options.ask_enemy", "general", facts.enemyGeneral()), "enemy"));
        }
        if (!talk.askedWeakness) {
            choices.add(choice(t("options.ask_weakness"), "weakness"));
        }
        for (String wing : new String[]{"left", "right"}) {
            Officer m = wing.equals("left") ? facts.left() : facts.right();
            if (m.trait() == Trait.BOLD) {
                choices.add(choice(t("options.join_attack", "name", m.name(), "wing", t("wings." + wing)), "flank_" + wing));
            } else if (m.trait() == Trait.CAUTIOUS) {
                choices.add(choice(t("options.back_wing", "name", m.name(), "wing", t("wings." + wing)), "support_" + wing));
            }
        }
        if (facts.general().trait() == Trait.CUNNING) {
            choices.add(choice(t("options.bait"), "bait"));
        }
        boolean enemyLeads = facts.enemyPlan().has(Strategy.UNRELENTING_CHARGE);
        if (talk.askedEnemy && (enemyLeads || facts.general().trait() == Trait.BOLD)) {
            choices.add(choice(t("options.hunt", "general", facts.enemyGeneral()), "hunt"));
        }
        if (talk.askedWeakness && !facts.weaknesses().isEmpty()) {
            choices.add(facts.general().trait() == Trait.PROUD ? choice(t("options.question"), "question")
                    : choice(t("options.propose"), "propose"));
        }
        choices.add(choice(t("options.line"), "line"));
        choices.add(choice(t("options.leave"), "leave"));
        return choices;
    }

    /** A choice, sent back as {@code council:<topic>} ({@link rafasconquest.peerage.Talk#choose}). */
    private static rafasconquest.peerage.Talk.Option choice(String text, String topic) {
        return new rafasconquest.peerage.Talk.Option("council:" + topic, text);
    }

    /** An officer's line: their name, and their rank of their nation as their title. */
    private static Said say(Facts facts, Officer who, String text) {
        return new Said(who.entity(), who.name(), who.title() + " of " + facts.nation(), facts.colour().getValue(), facts.accent(), text);
    }

    private static void setRole(ServerPlayer player, BattleRole role) {
        Ranks.get(player.server).setRole(player.getUUID(), role);
        player.sendSystemMessage(Component.literal(t("role_set", "role", role.title())).withStyle(ChatFormatting.YELLOW));
    }

    private static String odds(int ours, int theirs) {
        double r = (double) ours / Math.max(1, theirs);
        if (r >= 1.3) {
            return t("odds.more");
        }
        if (r <= 0.77) {
            return t("odds.fewer");
        }
        return t("odds.even");
    }

    private static String doctrines(WarPlan plan) {
        if (plan.doctrines().isEmpty()) {
            return t("plan.no_doctrine");
        }
        List<String> names = new ArrayList<>();
        plan.doctrines().forEach(d -> names.add(d.title()));
        return String.join(t("plan.doctrine_and"), names);
    }

    private static String ourPlan(WarPlan plan) {
        List<String> parts = new ArrayList<>();
        if (plan.has(Formation.GREAT_SHIELD)) {
            parts.add(t("plan.great_shield"));
        }
        if (plan.has(Formation.GREAT_SPEAR)) {
            parts.add(t("plan.great_spear"));
        }
        if (plan.has(Strategy.UNRELENTING_CHARGE)) {
            parts.add(t("plan.unrelenting_charge"));
        }
        if (plan.has(Strategy.DECAPITATE)) {
            parts.add(t("plan.decapitate"));
        }
        if (plan.has(Strategy.PINCER)) {
            parts.add(t("plan.pincer"));
        }
        if (plan.has(Strategy.MASS_CASUALTIES)) {
            parts.add(t("plan.mass_casualties"));
        }
        if (parts.isEmpty()) {
            return t("plan.hold");
        }
        String s = String.join("; ", parts);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1) + ".";
    }

    /** What a general knows of the enemy's ways, from their plan. */
    private static List<String> enemyHabits(WarPlan enemy) {
        List<String> lines = new ArrayList<>();
        if (enemy.has(Strategy.UNRELENTING_CHARGE)) {
            lines.add(t("habits.unrelenting_charge"));
        }
        if (enemy.has(Formation.GREAT_SPEAR)) {
            lines.add(t("habits.great_spear"));
        }
        if (enemy.has(Strategy.BAIT)) {
            lines.add(t("habits.bait"));
        }
        if (enemy.has(Formation.GREAT_SHIELD)) {
            lines.add(t("habits.great_shield"));
        }
        if (enemy.has(Strategy.DECAPITATE)) {
            lines.add(t("habits.decapitate"));
        }
        if (lines.isEmpty()) {
            lines.add(t("habits.plain"));
        }
        return lines;
    }
}
