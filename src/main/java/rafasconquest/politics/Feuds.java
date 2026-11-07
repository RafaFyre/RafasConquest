package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Feuds between lords in peacetime. Rival lords whose lands touch, who think ill enough of each other, may fall to
 * feuding, the hot-headed sooner (a neighbour who dislikes a landed player may feud them too); lords of one nation or of
 * two at peace. Each day of a feud one side raids the other's lands, or calls a skirmish of their own troops or a duel of
 * champions for midday on the border between them (fought for real if a player is near), or makes peace. The fights
 * score: when one side is well ahead the other yields, and pays with a border province, a ransom or emeralds. A ruler
 * may order their lords' feud ended. A skirmish across a border may start a war. While a lord's nation is at war its
 * feuds wait. Every feud ends in a short truce.
 * <p>
 * Lords hold land by their county's seat: each province's lord is its county seat's holder (a player, or the character
 * there if they serve the province's nation), else its nation's ruler, unless a feud gave it to another ({@link #lords}).
 */
final class Feuds {
    /** The daily chance that a rival starts a feud, times their aggression. */
    static final double START = 0.05;
    /** How ill a lord must think of a rival (or a player) to feud them. */
    static final int HATE = -40;
    static final int PLAYER_HATE = -20;
    static final int TRUCE_DAYS = 3;
    /** How far ahead a side must be for the other to yield. */
    static final int YIELD = 4;
    /** A feud this old burns out. */
    static final int LONGEST = 12;
    /** A skirmish or duel is fought this long after it is called (at midday, called at dawn). */
    static final double CLASH_DELAY = 0.5;
    static final int RAID_LOOT = 8;
    static final int TRIBUTE = 24;
    static final int RANSOM = 32;
    /** The chance a skirmish across a border starts a war, times the beaten side's ruler's aggression. */
    static final double ESCALATE = 0.2;
    /** A player's sworn sword's prowess in a duel. */
    static final int PLAYER_CHAMPION = 12;

    final List<Feud> list = new ArrayList<>();
    /** Provinces a feud gave to another lord. */
    final Map<Integer, Feud.Lord> lords = new TreeMap<>();
    /** Pairs of lords at truce, and until when. */
    final Map<String, Long> truces = new HashMap<>();
    /** "killer&gt;victim": the first lord's champion slew one of the second's. */
    final Set<String> blood = new HashSet<>();
    /** Champions slain in duels. */
    final Set<Integer> fallen = new TreeSet<>();
    long next = 1;
    /** Feuds whose real fight could not be fought: the next try is by strength. */
    private final Set<Long> simulateNext = new HashSet<>();

    // ---- who holds what ----

    @Nullable
    Feud.Lord lord(int province, Lands lands, Map<Integer, UUID> players) {
        Feud.Lord given = lords.get(province);
        if (given != null) {
            return given;
        }
        int owner = lands.owner(province);
        if (owner < 0) {
            return null;
        }
        int seat = lands.countySeat(lands.county(province));
        UUID player = players.get(seat);
        if (player != null) {
            return Feud.Lord.of(player);
        }
        int holder = lands.holder(seat);
        if (holder >= 0 && lands.person(holder).nation() == owner) {
            return Feud.Lord.of(holder);
        }
        int ruler = lands.ruler(owner);
        return ruler < 0 ? null : Feud.Lord.of(ruler);
    }

    Map<Feud.Lord, Set<Integer>> holdings(Lands lands, Map<Integer, UUID> players) {
        Map<Feud.Lord, Set<Integer>> land = new LinkedHashMap<>();
        for (int p = 0; p < lands.provinces(); p++) {
            Feud.Lord l = lord(p, lands, players);
            if (l != null) {
                land.computeIfAbsent(l, k -> new TreeSet<>()).add(p);
            }
        }
        return land;
    }

    /** The provinces of {@code of} touching {@code by}'s land. */
    static List<Integer> border(Set<Integer> of, Set<Integer> by, Lands lands) {
        List<Integer> found = new ArrayList<>();
        for (int p : of) {
            for (int q : lands.neighbours(p)) {
                if (by.contains(q)) {
                    found.add(p);
                    break;
                }
            }
        }
        return found;
    }

    static int nation(Feud.Lord lord, Lands lands, Map<Feud.Lord, Set<Integer>> land) {
        if (!lord.isPlayer()) {
            return lands.person(lord.person()).nation();
        }
        Set<Integer> held = land.get(lord);
        return held == null || held.isEmpty() ? -1 : lands.owner(held.iterator().next());
    }

    static String pair(Feud.Lord a, Feud.Lord b) {
        String x = a.key();
        String y = b.key();
        return x.compareTo(y) < 0 ? x + "|" + y : y + "|" + x;
    }

    boolean busy(Feud.Lord lord) {
        return list.stream().anyMatch(f -> f.involves(lord));
    }

    static String name(Feud.Lord lord, Lands lands, Politics.Front front) {
        return lord.isPlayer() ? front.playerName(lord.player()) : lands.person(lord.person()).name();
    }

    /** How much an NPC lord dislikes a landed neighbour who is a player: by their envy and temper alone. */
    static int dislike(Person p) {
        int d = 0;
        for (Personality t : p.traits()) {
            d -= switch (t) {
                case AMBITIOUS, GREEDY, ARROGANT, WRATHFUL, VENGEFUL, PARANOID, CALLOUS -> 10;
                case GENEROUS, HUMBLE, FORGIVING, COMPASSIONATE, TRUSTING -> -10;
                default -> 0;
            };
        }
        return d;
    }

    static boolean paused(Politics politics, Feud f) {
        return politics.atWar(f.nationA) || politics.atWar(f.nationB);
    }

    // ---- each day ----

    void day(Politics politics, long day, Lands lands, Random random, Politics.Front front) {
        truces.values().removeIf(until -> until <= day);
        Map<Integer, UUID> players = front.playerCounties();
        Map<Feud.Lord, Set<Integer>> land = holdings(lands, players);
        for (Feud f : new ArrayList<>(list)) {
            if (f.clash != null || f.awaiting || paused(politics, f)) {
                continue;
            }
            if (day - f.started >= LONGEST) {
                end(politics, f, day, Politics.say("feuds.burned_out", "a", name(f.a, lands, front), "b", name(f.b, lands, front)));
                continue;
            }
            act(politics, f, day, lands, random, front, land);
        }
        for (int[] r : lands.rivals()) {
            for (int k = 0; k < 2; k++) {
                consider(politics, Feud.Lord.of(r[k]), Feud.Lord.of(r[1 - k]), lands.opinion(r[k], r[1 - k]), HATE, day, lands, random,
                        front, land);
            }
        }
        for (UUID player : new TreeSet<>(players.values())) {
            Feud.Lord them = Feud.Lord.of(player);
            Set<Integer> theirs = land.get(them);
            if (theirs == null) {
                continue;
            }
            for (Map.Entry<Feud.Lord, Set<Integer>> e : land.entrySet()) {
                Feud.Lord npc = e.getKey();
                if (!npc.isPlayer() && !border(e.getValue(), theirs, lands).isEmpty()) {
                    consider(politics, npc, them, dislike(lands.person(npc.person())), PLAYER_HATE, day, lands, random, front, land);
                }
            }
        }
    }

    /** Whether {@code x} begins a feud with {@code y} today. */
    private void consider(Politics politics, Feud.Lord x, Feud.Lord y, int opinion, int hate, long day, Lands lands, Random random,
                          Politics.Front front, Map<Feud.Lord, Set<Integer>> land) {
        if (opinion > hate || busy(x) || busy(y) || truces.containsKey(pair(x, y))) {
            return;
        }
        Set<Integer> xs = land.get(x);
        Set<Integer> ys = land.get(y);
        if (xs == null || ys == null || border(xs, ys, lands).isEmpty()) {
            return;
        }
        int nx = nation(x, lands, land);
        int ny = nation(y, lands, land);
        if (nx < 0 || ny < 0 || !lands.canWage(nx) || !lands.canWage(ny) || politics.atWar(nx) || politics.atWar(ny)) {
            return;
        }
        Person px = lands.person(x.person());
        if (!y.isPlayer() && (px.liege() == y.person() || lands.person(y.person()).liege() == x.person())) {
            return; // not against their own liege or vassal
        }
        if (random.nextDouble() >= START * Politics.aggression(px)) {
            return;
        }
        Feud f = new Feud(next++, x, y, nx, ny, day);
        list.add(f);
        politics.log(day, Politics.say(nx == ny ? "feuds.began" : "feuds.began_across", "a", name(x, lands, front), "b", name(y, lands, front),
                "nation_a", lands.nationName(nx), "nation_b", lands.nationName(ny)));
    }

    /** Starts a feud now, whatever the lords think of each other (for {@code /pve politics feud}); null if they can't. */
    @Nullable
    Feud force(Politics politics, Feud.Lord x, Feud.Lord y, long day, Lands lands, Politics.Front front) {
        Map<Feud.Lord, Set<Integer>> land = holdings(lands, front.playerCounties());
        int nx = nation(x, lands, land);
        int ny = nation(y, lands, land);
        if (x.equals(y) || busy(x) || busy(y) || nx < 0 || ny < 0) {
            return null;
        }
        Feud f = new Feud(next++, x, y, nx, ny, day);
        list.add(f);
        politics.log(day, Politics.say("feuds.began", "a", name(x, lands, front), "b", name(y, lands, front)));
        return f;
    }

    /** One side's move for the day. */
    private void act(Politics politics, Feud f, long day, Lands lands, Random random, Politics.Front front,
                     Map<Feud.Lord, Set<Integer>> land) {
        // The more warlike side moves (a player's side waits on its NPC rival).
        boolean byA;
        if (f.a.isPlayer() || f.b.isPlayer()) {
            byA = !f.a.isPlayer();
        } else {
            double wa = Politics.aggression(lands.person(f.a.person()));
            double wb = Politics.aggression(lands.person(f.b.person()));
            byA = random.nextDouble() * (wa + wb) < wa;
        }
        Feud.Lord actor = f.lord(byA);
        Person p = lands.person(actor.person());
        int champion = lands.champion(actor.person(), fallen);
        double raid = 1;
        double skirmish = 0.3 + 0.7 * p.stat(Stat.MARTIAL) / 20.0;
        double duel = champion >= 0 && lands.person(champion).stat(Stat.PROWESS) >= 8
                ? 0.2 + lands.person(champion).stat(Stat.PROWESS) / 45.0 : 0;
        double peace = 0;
        for (Personality t : p.traits()) {
            if (t == Personality.FORGIVING || t == Personality.CALM || t == Personality.COMPASSIONATE) {
                peace += 0.4;
            }
        }
        double[] weights = {raid, skirmish, duel, peace, 1}; // the last: nothing today
        double roll = random.nextDouble() * (raid + skirmish + duel + peace + 1);
        int choice = 0;
        while (choice < weights.length - 1 && (roll -= weights[choice]) >= 0) {
            choice++;
        }
        Set<Integer> mine = land.getOrDefault(actor, Set.of());
        Set<Integer> theirs = land.getOrDefault(f.lord(!byA), Set.of());
        List<Integer> targets = border(theirs, mine, lands);
        if (targets.isEmpty()) {
            end(politics, f, day, Politics.say("feuds.apart", "a", name(f.a, lands, front), "b", name(f.b, lands, front)));
            return;
        }
        int target = targets.get(random.nextInt(targets.size()));
        if (choice == 0) {
            if (byA) {
                f.scoreA++;
            } else {
                f.scoreB++;
            }
            front.pay(f.lord(!byA), actor, RAID_LOOT);
            politics.log(day, Politics.say("feuds.raided", "a", name(actor, lands, front), "b", name(f.lord(!byA), lands, front),
                    "place", lands.provinceName(target)));
            yielded(politics, f, day, lands, random, front, land);
        } else if (choice == 1 || choice == 2) {
            boolean isDuel = choice == 2;
            int from = -1;
            for (int q : lands.neighbours(target)) {
                if (mine.contains(q)) {
                    from = q;
                    break;
                }
            }
            int[] c = lands.centre(target);
            int[] o = lands.centre(from < 0 ? target : from);
            double[] site = Politics.onLand(new double[]{(c[0] + o[0]) / 2.0, (c[1] + o[1]) / 2.0}, lands);
            int ca = f.a.isPlayer() ? -1 : lands.champion(f.a.person(), fallen);
            int cb = f.b.isPlayer() ? -1 : lands.champion(f.b.person(), fallen);
            f.clash = new Feud.Clash(isDuel ? Feud.Kind.DUEL : Feud.Kind.SKIRMISH, day + CLASH_DELAY, site[0], site[1], byA, ca, cb,
                    isDuel && toTheDeath(ca, f.a, f.b, lands), isDuel && toTheDeath(cb, f.b, f.a, lands));
            String text = Politics.say(isDuel ? "feuds.challenged" : "feuds.called_out", "a", name(actor, lands, front),
                    "b", name(f.lord(!byA), lands, front), "place", lands.provinceName(target), "x", Math.round(site[0]), "z", Math.round(site[1]));
            politics.log(day, text);
            front.clashSet(f, text);
        } else if (choice == 3) {
            end(politics, f, day, Politics.say("feuds.made_peace", "a", name(actor, lands, front), "b", name(f.lord(!byA), lands, front)));
        }
    }

    /**
     * Whether a champion fights to the death rather than yield: the vengeful, wrathful and callous sometimes; most often
     * if the other side has killed one of their own before.
     */
    boolean toTheDeath(int champion, Feud.Lord own, Feud.Lord other, Lands lands) {
        if (champion < 0) {
            return false;
        }
        double p = 0.03;
        for (Personality t : lands.person(champion).traits()) {
            p += switch (t) {
                case VENGEFUL, WRATHFUL -> 0.12;
                case CALLOUS -> 0.1;
                case ARROGANT -> 0.05;
                case COMPASSIONATE, CRAVEN, CALM -> -0.03;
                default -> 0;
            };
        }
        if (blood.contains(other.key() + ">" + own.key())) {
            p += 0.5;
        }
        // Fixed per champion and feud, so a duel fought again comes out the same way.
        return new Random(champion * 31L + own.key().hashCode() * 17L + other.key().hashCode()).nextDouble() < p;
    }

    // ---- the fights ----

    void advance(Politics politics, double now, Lands lands, Random random, Politics.Front front) {
        for (Feud f : new ArrayList<>(list)) {
            if (f.clash == null || f.awaiting || now < f.clash.at || paused(politics, f)) {
                continue;
            }
            long day = (long) Math.floor(now);
            Feud.Clash c = f.clash;
            boolean simulate = simulateNext.remove(f.id);
            if (c.kind == Feud.Kind.SKIRMISH) {
                int ta = troops(f.a, lands, front);
                int tb = troops(f.b, lands, front);
                if (!simulate && front.feudForReal(f, ta, tb)) {
                    f.awaiting = true;
                    continue;
                }
                double a = Math.pow(ta, 1.2);
                double b = Math.pow(tb, 1.2);
                boolean aWon = random.nextDouble() < a / (a + b);
                double win = 1 - (0.10 + random.nextDouble() * 0.15);
                double lose = 1 - (0.35 + random.nextDouble() * 0.30);
                skirmished(politics, f, aWon, aWon ? win : lose, aWon ? lose : win, day, lands, random, front);
            } else {
                if (!simulate && front.feudForReal(f, 0, 0)) {
                    f.awaiting = true;
                    continue;
                }
                double pa = Math.pow(prowess(c.championA, lands) + 5, 2);
                double pb = Math.pow(prowess(c.championB, lands) + 5, 2);
                boolean aWon = random.nextDouble() < pa / (pa + pb);
                dueled(politics, f, aWon, c.toTheDeath(!aWon), day, lands, random, front);
            }
        }
    }

    static int prowess(int champion, Lands lands) {
        return champion < 0 ? PLAYER_CHAMPION : lands.person(champion).stat(Stat.PROWESS);
    }

    static int troops(Feud.Lord lord, Lands lands, Politics.Front front) {
        return lord.isPlayer() ? front.playerTroops(lord.player()) : lands.troops(lord.person());
    }

    @Nullable
    Feud find(long id) {
        for (Feud f : list) {
            if (f.id == id) {
                return f;
            }
        }
        return null;
    }

    /** A real fight that came to nothing: it is fought again soon, by strength. */
    void cancelled(long id) {
        Feud f = find(id);
        if (f != null) {
            f.awaiting = false;
            simulateNext.add(id);
        }
    }

    /** A skirmish is over: the dead counted, the winner scores and holds captives; across a border it may start a war. */
    void skirmished(Politics politics, Feud f, boolean aWon, double aLeft, double bLeft, long day, Lands lands, Random random,
                    Politics.Front front) {
        f.clash = null;
        f.awaiting = false;
        int ta = troops(f.a, lands, front);
        int tb = troops(f.b, lands, front);
        politics.dead.merge(f.nationA, (int) Math.round(ta * (1 - Math.max(0, Math.min(1, aLeft)))), Integer::sum);
        politics.dead.merge(f.nationB, (int) Math.round(tb * (1 - Math.max(0, Math.min(1, bLeft)))), Integer::sum);
        if (aWon) {
            f.scoreA += 2;
            f.captiveA = true;
        } else {
            f.scoreB += 2;
            f.captiveB = true;
        }
        Feud.Lord winner = f.lord(aWon);
        Feud.Lord loser = f.lord(!aWon);
        politics.log(day, name(winner, lands, front) + "'s men beat " + name(loser, lands, front) + "'s in a skirmish, and took captives.");
        int wn = f.nation(aWon);
        int ln = f.nation(!aWon);
        if (wn != ln && !politics.atWar(wn) && !politics.atWar(ln) && !politics.allied(wn, ln)
                && !politics.truces.containsKey(Politics.pair(wn, ln)) && lands.ruler(ln) >= 0
                && random.nextDouble() < ESCALATE * Politics.aggression(lands.person(lands.ruler(ln)))) {
            Politics.Claim best = null;
            for (Politics.Claim claim : Politics.claims(ln, lands)) {
                if (claim.defender() == wn && (best == null || claim.want() > best.want())) {
                    best = claim;
                }
            }
            if (best != null) {
                politics.log(day, Politics.say("feuds.angered", "nation", lands.nationName(ln)));
                politics.declare(ln, wn, best.goal(), day, lands, random, front);
                return; // the feud waits on the war
            }
        }
        yielded(politics, f, day, lands, random, front, holdings(lands, front.playerCounties()));
    }

    /** A duel is over: the winner scores; a champion who would not yield, beaten, is dead, and remembered. */
    void dueled(Politics politics, Feud f, boolean aWon, boolean loserDied, long day, Lands lands, Random random, Politics.Front front) {
        Feud.Clash c = f.clash;
        f.clash = null;
        f.awaiting = false;
        if (aWon) {
            f.scoreA += 3;
        } else {
            f.scoreB += 3;
        }
        Feud.Lord winner = f.lord(aWon);
        Feud.Lord loser = f.lord(!aWon);
        String wc = champion(c == null ? -1 : c.champion(aWon), winner, lands, front);
        String lc = champion(c == null ? -1 : c.champion(!aWon), loser, lands, front);
        if (loserDied) {
            if (c != null && c.champion(!aWon) >= 0) {
                fallen.add(c.champion(!aWon));
            }
            blood.add(winner.key() + ">" + loser.key());
            politics.log(day, Politics.say("feuds.slew", "champion", wc, "lord", name(winner, lands, front), "loser", lc));
        } else {
            politics.log(day, Politics.say("feuds.yielded_duel", "champion", wc, "lord", name(winner, lands, front), "loser", lc));
        }
        if (c != null && !winner.isPlayer() && c.champion(aWon) >= 0 && c.champion(aWon) != winner.person()) {
            politics.succession.hook(c.champion(aWon), winner.person(), rafasconquest.people.Hook.Kind.FAVOUR); // owed for fighting for them
        }
        yielded(politics, f, day, lands, random, front, holdings(lands, front.playerCounties()));
        if (loserDied && c != null && c.champion(!aWon) >= 0) {
            politics.died(c.champion(!aWon), day, lands, random, Politics.say("deaths.duel")); // after the yielding, so the feud ends first
        }
    }

    private static String champion(int champion, Feud.Lord lord, Lands lands, Politics.Front front) {
        return champion >= 0 ? lands.person(champion).name() : name(lord, lands, front) + "'s sworn sword";
    }

    /** If one side is far enough ahead, the other yields and pays: a border province, a ransom or emeralds. */
    private void yielded(Politics politics, Feud f, long day, Lands lands, Random random, Politics.Front front,
                         Map<Feud.Lord, Set<Integer>> land) {
        int lead = f.scoreA - f.scoreB;
        if (Math.abs(lead) < YIELD) {
            return;
        }
        boolean aWon = lead > 0;
        Feud.Lord winner = f.lord(aWon);
        Feud.Lord loser = f.lord(!aWon);
        List<Integer> provinces = new ArrayList<>();
        for (int p : border(land.getOrDefault(loser, Set.of()), land.getOrDefault(winner, Set.of()), lands)) {
            int owner = lands.owner(p);
            if (p != lands.countySeat(lands.county(p)) && (owner < 0 || p != lands.seat(owner))) {
                provinces.add(p); // never a seat
            }
        }
        Person wp = winner.isPlayer() ? null : lands.person(winner.person());
        double province = provinces.isEmpty() ? 0 : wp != null && wp.has(Personality.AMBITIOUS) ? 2 : 1;
        double ransom = (aWon ? f.captiveA : f.captiveB) ? 1 : 0;
        double emeralds = wp != null && wp.has(Personality.GREEDY) ? 2 : 1;
        double roll = random.nextDouble() * (province + ransom + emeralds);
        String what;
        if ((roll -= province) < 0) {
            int p = provinces.get(random.nextInt(provinces.size()));
            int wn = f.nation(aWon);
            if (lands.owner(p) != wn) {
                lands.transfer(p, wn);
                politics.conquered.put(p, wn);
            }
            lords.put(p, winner);
            what = Politics.say("feuds.giving_up", "place", lands.provinceName(p));
        } else if ((roll -= ransom) < 0) {
            front.pay(loser, winner, RANSOM);
            what = Politics.say("feuds.ransom", "count", RANSOM);
        } else {
            front.pay(loser, winner, TRIBUTE);
            what = Politics.say("feuds.tribute", "count", TRIBUTE);
        }
        end(politics, f, day, Politics.say("feuds.yielded", "loser", name(loser, lands, front), "winner", name(winner, lands, front), "what", what));
    }

    /** A ruler orders a feud of their lords ended; an arbitrary one favours the lord they like better. */
    void settle(Politics politics, Feud f, int ruler, long day, Lands lands, Politics.Front front) {
        Person r = lands.person(ruler);
        String text = Politics.say("feuds.ordered", "ruler", r.name(), "a", name(f.a, lands, front), "b", name(f.b, lands, front));
        if (r.has(Personality.ARBITRARY) && !f.a.isPlayer() && !f.b.isPlayer()) {
            boolean aFavoured = lands.opinion(ruler, f.a.person()) >= lands.opinion(ruler, f.b.person());
            front.pay(f.lord(!aFavoured), f.lord(aFavoured), TRIBUTE);
            text = Politics.say("feuds.ordered_favouring", "ruler", r.name(), "a", name(f.a, lands, front), "b", name(f.b, lands, front),
                    "favoured", name(f.lord(aFavoured), lands, front), "count", TRIBUTE);
        }
        end(politics, f, day, text);
    }

    void end(Politics politics, Feud f, long day, String text) {
        list.remove(f);
        simulateNext.remove(f.id);
        truces.put(pair(f.a, f.b), day + TRUCE_DAYS);
        politics.log(day, text);
    }

    // ---- kept with the world ----

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag feuds = new ListTag();
        for (Feud f : list) {
            feuds.add(f.save());
        }
        tag.put("feuds", feuds);
        CompoundTag l = new CompoundTag();
        lords.forEach((p, lord) -> l.putString(Integer.toString(p), lord.key()));
        tag.put("lords", l);
        CompoundTag t = new CompoundTag();
        truces.forEach(t::putLong);
        tag.put("truces", t);
        ListTag b = new ListTag();
        for (String s : blood) {
            b.add(net.minecraft.nbt.StringTag.valueOf(s));
        }
        tag.put("blood", b);
        tag.putIntArray("fallen", fallen.stream().mapToInt(Integer::intValue).toArray());
        tag.putLong("next", next);
        return tag;
    }

    static Feuds load(CompoundTag tag) {
        Feuds f = new Feuds();
        for (Tag t : tag.getList("feuds", Tag.TAG_COMPOUND)) {
            f.list.add(Feud.load((CompoundTag) t));
        }
        CompoundTag l = tag.getCompound("lords");
        for (String k : l.getAllKeys()) {
            f.lords.put(Integer.parseInt(k), Feud.Lord.ofKey(l.getString(k)));
        }
        CompoundTag t = tag.getCompound("truces");
        for (String k : t.getAllKeys()) {
            f.truces.put(k, t.getLong(k));
        }
        for (Tag s : tag.getList("blood", Tag.TAG_STRING)) {
            f.blood.add(s.getAsString());
        }
        for (int p : tag.getIntArray("fallen")) {
            f.fallen.add(p);
        }
        f.next = Math.max(1, tag.getLong("next"));
        return f;
    }
}
