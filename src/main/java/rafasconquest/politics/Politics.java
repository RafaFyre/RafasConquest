package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The nations' politics, day by day. Each day every war fights (most days a battle, far ones settled by the armies'
 * strength, near ones fought for real), then every ruler not at war makes one decision: war on a neighbour, an alliance,
 * or (mostly) nothing. A war is over one of the defender's provinces on the border; a side whose score reaches 100 wins
 * it (the attacker taking the province), and a war dragging past {@link #LONG_WAR} days ends in peace. Peace brings a
 * truce. Everything that happens goes in the chronicle, which heralds read out a day late.
 */
public final class Politics {
    /** How likely doing nothing is, against each other choice's weight. */
    static final double NOTHING = 40;
    static final double BATTLE_CHANCE = 0.6;
    static final int WIN_SCORE = 100;
    /** A war this many days old ends in peace: the province to the attacker only if they are well ahead. */
    static final int LONG_WAR = 30;
    static final int TRUCE_DAYS = 15;
    /** A defender fights on their own ground: their army counts this much more. */
    static final double DEFENDER_EDGE = 1.15;
    static final int CHRONICLE = 200;

    /** What the world does with the politics: battles fought for real, and armies called up. */
    public interface Front {
        /**
         * Whether today's battle of this war is fought for real (someone is near it); its result then comes later by
         * {@link #realBattle}. {@code site} is (x, z).
         */
        boolean fightForReal(War war, int[] site, int attackers, int defenders);

        /** A war has begun: its armies muster. */
        void declared(War war);
    }

    /** Something that happened, on a day. */
    public record Event(long day, String text) {
    }

    final List<War> wars = new ArrayList<>();
    final Set<Long> alliances = new HashSet<>();
    final Map<Long, Long> truces = new HashMap<>();
    /** Provinces that changed hands, and to whom. */
    final Map<Integer, Integer> conquered = new TreeMap<>();
    final List<Event> chronicle = new ArrayList<>();
    /** Wars whose battle today is being fought for real: no other battle until it is done. */
    private final Set<Long> awaiting = new HashSet<>();
    private long nextWar = 1;

    public List<War> wars() {
        return Collections.unmodifiableList(wars);
    }

    public Map<Integer, Integer> conquered() {
        return Collections.unmodifiableMap(conquered);
    }

    public List<Event> chronicle() {
        return Collections.unmodifiableList(chronicle);
    }

    /** The alliances, as pairs of nations. */
    public List<int[]> alliances() {
        List<int[]> pairs = new ArrayList<>();
        for (long key : new TreeSet<>(alliances)) {
            pairs.add(new int[]{(int) (key >> 16), (int) (key & 0xFFFF)});
        }
        return pairs;
    }

    public boolean allied(int a, int b) {
        return alliances.contains(pair(a, b));
    }

    public boolean atWar(int nation) {
        return wars.stream().anyMatch(w -> w.involves(nation));
    }

    /** The war two nations fight on opposite sides, or null. */
    public War warBetween(int a, int b) {
        for (War w : wars) {
            if (w.side(true).contains(a) && w.side(false).contains(b) || w.side(true).contains(b) && w.side(false).contains(a)) {
                return w;
            }
        }
        return null;
    }

    /** One day: the wars fought, then each ruler's decision. */
    public void day(long day, Lands lands, Random random, Front front) {
        for (War w : new ArrayList<>(wars)) {
            fight(w, day, lands, random, front);
        }
        truces.values().removeIf(until -> until <= day);
        List<Integer> order = new ArrayList<>();
        for (int n = 0; n < lands.nations(); n++) {
            order.add(n);
        }
        Collections.shuffle(order, random);
        for (int n : order) {
            if (lands.canWage(n) && !atWar(n)) {
                decide(n, day, lands, random, front);
            }
        }
        while (chronicle.size() > CHRONICLE) {
            chronicle.remove(0);
        }
    }

    /** A ruler's choice for the day: war on a neighbour, an alliance, or nothing. */
    void decide(int n, long day, Lands lands, Random random, Front front) {
        int ruler = lands.ruler(n);
        if (ruler < 0) {
            return;
        }
        Person p = lands.person(ruler);
        List<Integer> choices = new ArrayList<>(); // +m: war on m; -(m+1): alliance with m
        List<Double> weights = new ArrayList<>();
        double total = NOTHING;
        double aggression = aggression(p);
        for (int m : neighbourNations(n, lands)) {
            int other = lands.ruler(m);
            if (!lands.canWage(m) || other < 0 || allied(n, m)) {
                continue;
            }
            int opinion = lands.opinion(ruler, other);
            if (!truces.containsKey(pair(n, m)) && !goals(n, m, lands).isEmpty()) {
                double odds = Math.max(0.25, Math.min(3, lands.soldiers(n) / (double) Math.max(1, lands.soldiers(m))));
                double w = aggression * odds * (opinion < 0 ? 1.5 : 1);
                choices.add(m);
                weights.add(w);
                total += w;
            }
            if (!atWar(m) && opinion > 0) {
                double w = p.stat(Stat.DIPLOMACY) / 20.0 * (opinion + 50) / 100.0;
                choices.add(-(m + 1));
                weights.add(w);
                total += w;
            }
        }
        double roll = random.nextDouble() * total;
        for (int i = 0; i < choices.size(); i++) {
            roll -= weights.get(i);
            if (roll < 0) {
                int c = choices.get(i);
                if (c >= 0) {
                    declare(n, c, day, lands, random, front);
                } else {
                    propose(n, -c - 1, day, lands);
                }
                return;
            }
        }
    }

    /** How warlike a ruler is: their martial skill and their temper. */
    static double aggression(Person p) {
        double a = 0.2 + p.stat(Stat.MARTIAL) / 20.0 * 0.4;
        for (Personality t : p.traits()) {
            a += switch (t) {
                case AMBITIOUS -> 0.4;
                case WRATHFUL -> 0.3;
                case BRAVE -> 0.2;
                case ARROGANT -> 0.15;
                case IMPATIENT, ZEALOUS, CALLOUS -> 0.1;
                case CONTENT -> -0.4;
                case CRAVEN -> -0.3;
                case CALM, COMPASSIONATE -> -0.15;
                case PATIENT, FORGIVING -> -0.1;
                default -> 0;
            };
        }
        return Math.max(0.02, a);
    }

    /** An offer of alliance; taken if the other ruler doesn't think too ill of the one asking. */
    void propose(int n, int m, long day, Lands lands) {
        if (lands.opinion(lands.ruler(m), lands.ruler(n)) < -10) {
            return;
        }
        alliances.add(pair(n, m));
        log(day, lands.nationName(n) + " and " + lands.nationName(m) + " swore an alliance.");
    }

    /** Declares war over one of the defender's border provinces, and calls the allies of both. */
    public War declare(int n, int m, long day, Lands lands, Random random, Front front) {
        List<Integer> goals = goals(n, m, lands);
        if (goals.isEmpty()) {
            return null;
        }
        War w = new War(nextWar++, n, m, goals.get(random.nextInt(goals.size())), day);
        StringBuilder text = new StringBuilder(lands.nationName(n) + " declared war on " + lands.nationName(m) + " over "
                + lands.provinceName(w.goal) + ".");
        for (int a = 0; a < lands.nations(); a++) {
            if (a == n || a == m || !lands.canWage(a) || atWar(a) || lands.ruler(a) < 0) {
                continue;
            }
            int ruler = lands.ruler(a);
            if (allied(a, m) && !allied(a, n) && lands.opinion(ruler, lands.ruler(m)) >= -20) {
                w.defenderAllies.add(a);
                text.append(' ').append(lands.nationName(a)).append(" came to ").append(lands.nationName(m)).append("'s defence.");
            } else if (allied(a, n) && !allied(a, m) && lands.opinion(ruler, lands.ruler(n)) >= 0 && random.nextBoolean()) {
                w.attackerAllies.add(a);
                text.append(' ').append(lands.nationName(a)).append(" joined ").append(lands.nationName(n)).append('.');
            }
        }
        wars.add(w);
        log(day, text.toString());
        front.declared(w);
        return w;
    }

    /** The defender's provinces beside the attacker's land, but for its seat. */
    static List<Integer> goals(int n, int m, Lands lands) {
        List<Integer> goals = new ArrayList<>();
        for (int p = 0; p < lands.provinces(); p++) {
            if (lands.owner(p) != m || p == lands.seat(m)) {
                continue;
            }
            for (int q : lands.neighbours(p)) {
                if (lands.owner(q) == n) {
                    goals.add(p);
                    break;
                }
            }
        }
        return goals;
    }

    /** The nations whose land touches a nation's. */
    static Set<Integer> neighbourNations(int n, Lands lands) {
        Set<Integer> found = new TreeSet<>();
        for (int p = 0; p < lands.provinces(); p++) {
            if (lands.owner(p) != n) {
                continue;
            }
            for (int q : lands.neighbours(p)) {
                int o = lands.owner(q);
                if (o >= 0 && o != n) {
                    found.add(o);
                }
            }
        }
        return found;
    }

    /** A day of a war: most days a battle, settled by strength unless fought for real. */
    void fight(War w, long day, Lands lands, Random random, Front front) {
        if (awaiting.contains(w.id) || random.nextDouble() >= BATTLE_CHANCE) {
            end(w, day, lands);
            return;
        }
        int attackers = soldiers(w, true, lands);
        int defenders = soldiers(w, false, lands);
        if (front.fightForReal(w, site(w, lands), attackers, defenders)) {
            awaiting.add(w.id);
            return;
        }
        double a = Math.pow(attackers, 1.2);
        double d = Math.pow(defenders * DEFENDER_EDGE, 1.2);
        boolean attackerWon = random.nextDouble() < a / (a + d);
        battle(w, attackerWon, 12 + random.nextInt(13), day, lands);
    }

    /** A battle of a war fought for real has ended (for the attacker or not). */
    public void realBattle(long warId, boolean attackerWon, long day, Lands lands) {
        awaiting.remove(warId);
        for (War w : new ArrayList<>(wars)) {
            if (w.id == warId) {
                battle(w, attackerWon, 25, day, lands);
                return;
            }
        }
    }

    /** A real battle that came to nothing (stopped, or a draw): the war goes on. */
    public void realBattleCancelled(long warId) {
        awaiting.remove(warId);
    }

    private void battle(War w, boolean attackerWon, int swing, long day, Lands lands) {
        w.battles++;
        w.score = Math.max(-WIN_SCORE, Math.min(WIN_SCORE, w.score + (attackerWon ? swing : -swing)));
        String winner = lands.nationName(attackerWon ? w.attacker : w.defender);
        String loser = lands.nationName(attackerWon ? w.defender : w.attacker);
        log(day, "At the battle of " + lands.provinceName(w.goal) + ", " + winner + " defeated " + loser + ".");
        end(w, day, lands);
    }

    /** Ends the war if one side has won, or it has dragged on too long. */
    private void end(War w, long day, Lands lands) {
        if (w.score >= WIN_SCORE || day - w.started >= LONG_WAR && w.score >= WIN_SCORE / 2) {
            peace(w, true, day, lands);
        } else if (w.score <= -WIN_SCORE) {
            peace(w, false, day, lands);
        } else if (day - w.started >= LONG_WAR) {
            wars.remove(w);
            log(day, lands.nationName(w.attacker) + " and " + lands.nationName(w.defender) + " made peace, weary of war; nothing changed hands.");
            truce(w, day);
        }
    }

    private void peace(War w, boolean attackerWon, long day, Lands lands) {
        wars.remove(w);
        awaiting.remove(w.id);
        if (attackerWon && lands.owner(w.goal) == w.defender) {
            lands.transfer(w.goal, w.attacker);
            conquered.put(w.goal, w.attacker);
            log(day, lands.nationName(w.attacker) + " won the war: " + lands.nationName(w.defender) + " ceded " + lands.provinceName(w.goal) + ".");
        } else {
            log(day, lands.nationName(w.defender) + " threw back " + lands.nationName(w.attacker) + ": the war is over.");
        }
        truce(w, day);
    }

    private void truce(War w, long day) {
        for (int a : w.side(true)) {
            for (int b : w.side(false)) {
                truces.put(pair(a, b), day + TRUCE_DAYS);
            }
        }
    }

    /** Where a war's battle is fought: on the border, between the province fought over and the attacker's land beside it. */
    static int[] site(War w, Lands lands) {
        int[] goal = lands.centre(w.goal);
        for (int q : lands.neighbours(w.goal)) {
            if (w.side(true).contains(lands.owner(q))) {
                int[] from = lands.centre(q);
                return new int[]{(goal[0] + from[0]) / 2, (goal[1] + from[1]) / 2};
            }
        }
        return goal;
    }

    /** A side's soldiers: its leader's whole army, and half of each ally's. */
    static int soldiers(War w, boolean attacking, Lands lands) {
        int total = 0;
        List<Integer> side = w.side(attacking);
        for (int i = 0; i < side.size(); i++) {
            total += i == 0 ? lands.soldiers(side.get(i)) : lands.soldiers(side.get(i)) / 2;
        }
        return Math.max(20, total);
    }

    private void log(long day, String text) {
        chronicle.add(new Event(day, text));
    }

    static long pair(int a, int b) {
        return ((long) Math.min(a, b) << 16) | Math.max(a, b);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (War w : wars) {
            list.add(w.save());
        }
        tag.put("wars", list);
        tag.putLongArray("alliances", alliances.stream().mapToLong(Long::longValue).toArray());
        CompoundTag t = new CompoundTag();
        truces.forEach((k, v) -> t.putLong(Long.toString(k), v));
        tag.put("truces", t);
        CompoundTag c = new CompoundTag();
        conquered.forEach((p, n) -> c.putInt(Integer.toString(p), n));
        tag.put("conquered", c);
        ListTag events = new ListTag();
        for (Event e : chronicle) {
            CompoundTag et = new CompoundTag();
            et.putLong("day", e.day());
            et.putString("text", e.text());
            events.add(et);
        }
        tag.put("chronicle", events);
        tag.putLong("nextWar", nextWar);
        return tag;
    }

    public static Politics load(CompoundTag tag) {
        Politics p = new Politics();
        for (Tag t : tag.getList("wars", Tag.TAG_COMPOUND)) {
            p.wars.add(War.load((CompoundTag) t));
        }
        for (long a : tag.getLongArray("alliances")) {
            p.alliances.add(a);
        }
        CompoundTag t = tag.getCompound("truces");
        for (String k : t.getAllKeys()) {
            p.truces.put(Long.parseLong(k), t.getLong(k));
        }
        CompoundTag c = tag.getCompound("conquered");
        for (String k : c.getAllKeys()) {
            p.conquered.put(Integer.parseInt(k), c.getInt(k));
        }
        for (Tag e : tag.getList("chronicle", Tag.TAG_COMPOUND)) {
            CompoundTag et = (CompoundTag) e;
            p.chronicle.add(new Event(et.getLong("day"), et.getString("text")));
        }
        p.nextWar = Math.max(1, tag.getLong("nextWar"));
        return p;
    }
}
