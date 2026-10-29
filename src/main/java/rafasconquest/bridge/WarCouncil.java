package rafasconquest.bridge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
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
 * player's role for the coming charge. Choices are clickable lines that run {@code /council <topic>}.
 */
final class WarCouncil {
    /** Lines of the briefing come this often, in ticks. */
    private static final int LINE_TICKS = 50;

    /** One of the officers at the council. */
    record Officer(String title, String name, Trait trait) {
        String called() {
            return title + " " + name;
        }
    }

    /** What the council knows, from one army's side. */
    record Facts(int side, TextColor colour, Officer general, Officer left, Officer right, WarPlan plan, int soldiers,
                 String enemyNation, String enemyGeneral, WarPlan enemyPlan, int enemySoldiers, List<String> weaknesses,
                 int day) {
    }

    private static final class Talk {
        final Deque<Component> queue = new ArrayDeque<>();
        long nextAt;
        boolean briefed;
        boolean askedEnemy;
        boolean askedWeakness;
        int side;
    }

    private final Map<UUID, Talk> talks = new HashMap<>();

    /** A player right-clicked a council member: only officers of its army are heard. */
    void approach(ServerPlayer player, Facts facts, boolean sameArmy, long now) {
        Rank rank = Ranks.get(player.server).of(player.getUUID());
        if (!sameArmy) {
            player.sendSystemMessage(say(facts, facts.general(), "You're no officer of ours. Guards!"));
            return;
        }
        if (!rank.officer()) {
            player.sendSystemMessage(say(facts, facts.general(), String.format(Locale.ROOT,
                    "Only officers sit at this council, %s. Back to your block.", rank.title())));
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
            talk.queue.add(say(facts, facts.general(), "Yes, " + rank.title() + "?"));
        } else {
            brief(talk, facts, rank, player);
            talk.briefed = true;
        }
        talk.queue.add(options(talk, facts));
    }

    /** A choice the player clicked ({@code /council <topic>}). */
    void topic(ServerPlayer player, Facts facts, String topic, long now) {
        Talk talk = talks.get(player.getUUID());
        if (talk == null || !talk.briefed || talk.side != facts.side()) {
            player.sendSystemMessage(Component.literal("Speak to the general at the war council first."));
            return;
        }
        Rank rank = Ranks.get(player.server).of(player.getUUID());
        talk.queue.clear();
        talk.nextAt = now;
        WarPlan enemy = facts.enemyPlan();
        switch (topic) {
            case "enemy" -> {
                talk.askedEnemy = true;
                talk.queue.add(say(facts, facts.general(), "General " + facts.enemyGeneral() + " fights by "
                        + doctrines(enemy) + "."));
                for (String line : enemyHabits(enemy)) {
                    talk.queue.add(say(facts, facts.general(), line));
                }
            }
            case "weakness" -> {
                talk.askedWeakness = true;
                if (facts.weaknesses().isEmpty()) {
                    talk.queue.add(say(facts, facts.general(), "They have no plain weakness. We'll have to make one."));
                }
                for (String line : facts.weaknesses()) {
                    talk.queue.add(say(facts, facts.general(), line));
                }
            }
            case "flank_left", "flank_right" -> {
                Officer m = topic.endsWith("left") ? facts.left() : facts.right();
                setRole(player, BattleRole.FLANKER);
                talk.queue.add(say(facts, m, "Good! Ride with my wing, and we'll take them in the side."));
            }
            case "support_left", "support_right" -> {
                Officer m = topic.endsWith("left") ? facts.left() : facts.right();
                setRole(player, BattleRole.SUPPORT);
                talk.queue.add(say(facts, m, "Hold your men back of my line and come where it bends. I'll count on you."));
            }
            case "bait" -> {
                setRole(player, BattleRole.BAIT);
                talk.queue.add(say(facts, facts.general(), "Run ahead of the line, then give ground once they bite. "
                        + "Draw them in, and we'll close the jaws."));
            }
            case "hunt" -> {
                setRole(player, BattleRole.KILL_SQUAD);
                talk.queue.add(say(facts, facts.general(), "Then bring me General " + facts.enemyGeneral()
                        + ". Without their general they'll run."));
            }
            case "propose" -> {
                setRole(player, BattleRole.FLANKER);
                talk.queue.add(say(facts, facts.general(), "Their weak side, then. Take your men round and strike it. "
                        + "Don't let them see you coming."));
            }
            case "question" -> talk.queue.add(say(facts, facts.general(),
                    "You'll question nothing, " + rank.title() + ". The plan stands. To your post."));
            case "line" -> {
                setRole(player, BattleRole.FRONTLINE);
                talk.queue.add(say(facts, facts.general(), "Then you'll lead the first charge. Make it count."));
            }
            case "leave" -> {
                talk.queue.add(say(facts, facts.general(), "Dismissed, " + rank.title() + "."));
                return;
            }
            default -> {
                return;
            }
        }
        talk.queue.add(options(talk, facts));
    }

    /** Sends each player their next line when it is due. */
    void tick(MinecraftServer server, long now) {
        for (Map.Entry<UUID, Talk> entry : talks.entrySet()) {
            Talk talk = entry.getValue();
            if (talk.queue.isEmpty() || now < talk.nextAt) {
                continue;
            }
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player == null) {
                talk.queue.clear();
                continue;
            }
            player.sendSystemMessage(talk.queue.poll());
            talk.nextAt = now + LINE_TICKS;
        }
    }

    /** The battle begins: the council breaks up, and whatever was still to be said goes unsaid. */
    void adjourn() {
        talks.values().forEach(t -> t.queue.clear());
    }

    private static void brief(Talk talk, Facts facts, Rank rank, ServerPlayer player) {
        Officer g = facts.general();
        Deque<Component> q = talk.queue;
        q.add(say(facts, g, String.format(Locale.ROOT, "%s. Good, you've come. Day %d: here is how we fight.",
                rank.title(), facts.day())));
        q.add(say(facts, g, String.format(Locale.ROOT, "We have %d soldiers against their %d. %s", facts.soldiers(),
                facts.enemySoldiers(), odds(facts.soldiers(), facts.enemySoldiers()))));
        q.add(say(facts, g, "We fight by " + doctrines(facts.plan()) + ". " + ourPlan(facts.plan())));
        q.add(say(facts, g, "Our troops stand " + facts.plan().distribution().title().toLowerCase(Locale.ROOT) + "."));
        q.add(say(facts, g, "Against us: " + facts.enemyNation() + ", under General " + facts.enemyGeneral() + "."));
        if (!facts.weaknesses().isEmpty()) {
            q.add(say(facts, g, "Our scouts say: " + facts.weaknesses().get(0)));
        }
        q.add(say(facts, facts.left(), opener(facts.left(), "left")));
        q.add(say(facts, facts.right(), opener(facts.right(), "right")));
        BattleRole role = Ranks.get(player.server).role(player.getUUID());
        q.add(say(facts, g, "As it stands, your men go " + role.title() + ". Speak, if you'd have it otherwise."));
    }

    /** What a wing's Marshal says when the briefing comes to them, by their trait. */
    private static String opener(Officer m, String wing) {
        return switch (m.trait()) {
            case BOLD -> "The " + wing + " wing will go in hard. I'd welcome more men at the front of it.";
            case CAUTIOUS -> "The " + wing + " wing will hold. I'd sleep better with a reserve behind it.";
            case CUNNING -> "On the " + wing + " we'll let them overreach, then make them pay for it.";
            case PROUD -> "The " + wing + " wing needs no help. Mine never has.";
        };
    }

    /** The choices on offer now, as clickable lines. */
    private static Component options(Talk talk, Facts facts) {
        MutableComponent line = Component.literal("  ");
        List<MutableComponent> choices = new ArrayList<>();
        if (!talk.askedEnemy) {
            choices.add(choice("Ask about General " + facts.enemyGeneral(), "enemy"));
        }
        if (!talk.askedWeakness) {
            choices.add(choice("Ask where they are weakest", "weakness"));
        }
        for (String wing : new String[]{"left", "right"}) {
            Officer m = wing.equals("left") ? facts.left() : facts.right();
            if (m.trait() == Trait.BOLD) {
                choices.add(choice("Join Marshal " + m.name() + "'s attack on the " + wing, "flank_" + wing));
            } else if (m.trait() == Trait.CAUTIOUS) {
                choices.add(choice("Back Marshal " + m.name() + "'s " + wing + " wing in support", "support_" + wing));
            }
        }
        if (facts.general().trait() == Trait.CUNNING) {
            choices.add(choice("Volunteer as bait", "bait"));
        }
        boolean enemyLeads = facts.enemyPlan().has(Strategy.UNRELENTING_CHARGE);
        if (talk.askedEnemy && (enemyLeads || facts.general().trait() == Trait.BOLD)) {
            choices.add(choice("Volunteer to hunt General " + facts.enemyGeneral(), "hunt"));
        }
        if (talk.askedWeakness && !facts.weaknesses().isEmpty()) {
            choices.add(facts.general().trait() == Trait.PROUD ? choice("Question the plan", "question")
                    : choice("Propose striking their weak side", "propose"));
        }
        choices.add(choice("Ask to lead the first charge", "line"));
        choices.add(choice("Take your leave", "leave"));
        for (int i = 0; i < choices.size(); i++) {
            if (i > 0) {
                line.append(Component.literal("  "));
            }
            line.append(choices.get(i));
        }
        return line;
    }

    private static MutableComponent choice(String text, String topic) {
        return Component.literal("[" + text + "]").withStyle(style -> style.withColor(ChatFormatting.GOLD)
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/council " + topic))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(text))));
    }

    private static Component say(Facts facts, Officer who, String text) {
        return Component.literal("[War council] ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(who.called() + ": ").withStyle(style -> style.withColor(facts.colour())))
                .append(Component.literal(text).withStyle(ChatFormatting.WHITE));
    }

    private static void setRole(ServerPlayer player, BattleRole role) {
        Ranks.get(player.server).setRole(player.getUUID(), role);
        player.sendSystemMessage(Component.literal("Your unit will fight as " + role.title() + ".").withStyle(ChatFormatting.YELLOW));
    }

    private static String odds(int ours, int theirs) {
        double r = (double) ours / Math.max(1, theirs);
        if (r >= 1.3) {
            return "We have the numbers.";
        }
        if (r <= 0.77) {
            return "They outnumber us; every man must count.";
        }
        return "We're evenly matched.";
    }

    private static String doctrines(WarPlan plan) {
        if (plan.doctrines().isEmpty()) {
            return "no particular doctrine";
        }
        List<String> names = new ArrayList<>();
        plan.doctrines().forEach(d -> names.add(d.title()));
        return String.join(" and ", names);
    }

    private static String ourPlan(WarPlan plan) {
        List<String> parts = new ArrayList<>();
        if (plan.has(Formation.GREAT_SHIELD)) {
            parts.add("our front stands three blocks deep behind its shields");
        }
        if (plan.has(Formation.GREAT_SPEAR)) {
            parts.add("all our horse rides as one great wedge");
        }
        if (plan.has(Strategy.UNRELENTING_CHARGE)) {
            parts.add("I lead the cavalry myself, in two waves");
        }
        if (plan.has(Strategy.DECAPITATE)) {
            parts.add("our cavalry goes for their general");
        }
        if (plan.has(Strategy.PINCER)) {
            parts.add("each wing baits them in and closes on them");
        }
        if (plan.has(Strategy.MASS_CASUALTIES)) {
            parts.add("we strike wherever they're thickest");
        }
        if (parts.isEmpty()) {
            return "We hold the line and let them break on it.";
        }
        String s = String.join("; ", parts);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1) + ".";
    }

    /** What a general knows of the enemy's ways, from their plan. */
    private static List<String> enemyHabits(WarPlan enemy) {
        List<String> lines = new ArrayList<>();
        if (enemy.has(Strategy.UNRELENTING_CHARGE)) {
            lines.add("They ride at the head of their own charge. Bring them down and their army breaks.");
        }
        if (enemy.has(Formation.GREAT_SPEAR)) {
            lines.add("All their horse comes as one great wedge. When it comes, brace.");
        }
        if (enemy.has(Strategy.BAIT)) {
            lines.add("Watch for a block that gives ground too easily: it's bait. Don't chase it.");
        }
        if (enemy.has(Formation.GREAT_SHIELD)) {
            lines.add("Their front stands behind a wall of shields, three deep. Go round it, not through it.");
        }
        if (enemy.has(Strategy.DECAPITATE)) {
            lines.add("Their cavalry hunts generals. Keep my guard close.");
        }
        if (lines.isEmpty()) {
            lines.add("A plain fighter: no tricks, no surprises.");
        }
        return lines;
    }
}
