package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Stat;

import java.util.ArrayDeque;
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
 * The nations' politics. Each day every ruler not at war makes one decision: a war, an alliance, or (mostly) nothing.
 * A war is for a title ({@link Goal}: a county or a duchy), chosen to make the attacker's land whole: to fill a hole in
 * it, take an enclave, join its pieces, or finish a title it partly holds. Both sides raise an army from their manpower,
 * muster it a day at their capital, and march it to the battle site, on the border of the next county to be taken; when
 * both have arrived they fight (for real if a player is near, else by strength). Each attacker victory takes that county.
 * After every battle each ruler may sue for peace: a defender beaten gives up the rest of the title rather than raise
 * another army and lose more of their people; an attacker beaten goes home with what they have. The dead are lost to
 * the nation's manpower, which recovers slowly. Everything goes in the chronicle, which heralds read out a day late.
 */
public final class Politics {
    /** How likely doing nothing is, against each other choice's weight. */
    static final double NOTHING = 40;
    /** How far an army marches in a day, in blocks. */
    static final double MARCH = 2000;
    /** How long an army musters at its capital before it marches, in days. */
    static final double MUSTER = 1;
    /** How long a victorious army rests before marching on, in days. */
    static final double REST = 0.25;
    /** An army smaller than this can't be raised: the nation has no one left to send. */
    static final int MIN_ARMY = 50;
    /** A defender fights on their own ground: their army counts this much more. */
    static final double DEFENDER_EDGE = 1.15;
    /** Of each day's dead, this share of a nation's levy returns to its manpower each day (the young grown, the wounded healed). */
    static final double RECOVERY = 0.02;
    /** A war this old ends: each side keeps what it holds. */
    static final int LONGEST_WAR = 120;
    static final int TRUCE_DAYS = 15;
    static final int CHRONICLE = 200;

    /** What the world does with the politics: battles fought for real, and armies called up. */
    public interface Front {
        /**
         * Whether this battle is fought for real (someone is near the site); its result then comes later by
         * {@link #realBattle}.
         */
        boolean fightForReal(War war, int attackers, int defenders);

        /** A side raises an army (at the start of a war, or another after a defeat): its people muster. */
        void raised(War war, boolean attacking);
    }

    /** Something that happened, on a day. */
    public record Event(long day, String text) {
    }

    final List<War> wars = new ArrayList<>();
    final Set<Long> alliances = new HashSet<>();
    final Map<Long, Long> truces = new HashMap<>();
    /** Provinces that changed hands, and to whom. */
    final Map<Integer, Integer> conquered = new TreeMap<>();
    /** Each nation's dead not yet made good: its manpower is its levy, less these, less its armies in the field. */
    final Map<Integer, Integer> dead = new HashMap<>();
    final List<Event> chronicle = new ArrayList<>();
    /** Wars whose battle is being fought for real. */
    private final Set<Long> awaiting = new HashSet<>();
    private long nextWar = 1;
    /** The time (in days) the armies were last moved. */
    private double moved = -1;

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

    /** The soldiers a nation could raise now: its levy, less its dead not yet made good, less its armies in the field. */
    public int manpower(int nation, Lands lands) {
        int field = 0;
        for (War w : wars) {
            if (w.attacker == nation && w.attackerArmy != null) {
                field += w.attackerArmy.soldiers;
            }
            if (w.defender == nation && w.defenderArmy != null) {
                field += w.defenderArmy.soldiers;
            }
        }
        return Math.max(0, lands.levy(nation) - dead.getOrDefault(nation, 0) - field);
    }

    // ---- each day ----

    /** One day: the dead made good a little, the truces run down, then each ruler's decision. */
    public void day(long day, Lands lands, Random random, Front front) {
        for (int n : new ArrayList<>(dead.keySet())) {
            int left = dead.get(n) - (int) Math.ceil(lands.levy(n) * RECOVERY);
            if (left > 0) {
                dead.put(n, left);
            } else {
                dead.remove(n);
            }
        }
        truces.values().removeIf(until -> until <= day);
        for (War w : new ArrayList<>(wars)) {
            if (day - w.started >= LONGEST_WAR) {
                peace(w, day, lands, lands.nationName(w.attacker) + " and " + lands.nationName(w.defender)
                        + " made peace, weary of war; each keeps what it holds.");
            }
        }
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

    /** A title worth fighting for: whose it mostly is, and how much the attacker wants it. */
    record Claim(Goal goal, int defender, double want) {
    }

    /** A ruler's choice for the day: war for a title, an alliance, or nothing. */
    void decide(int n, long day, Lands lands, Random random, Front front) {
        int ruler = lands.ruler(n);
        if (ruler < 0) {
            return;
        }
        Person p = lands.person(ruler);
        List<Object> choices = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        double total = NOTHING;
        double aggression = aggression(p);
        int strength = manpower(n, lands);
        for (Claim claim : claims(n, lands)) {
            int m = claim.defender();
            if (truces.containsKey(pair(n, m)) || allied(n, m) || atWar(m)) {
                continue;
            }
            double odds = Math.max(0.25, Math.min(3, strength / (double) Math.max(1, manpower(m, lands))));
            double w = aggression * odds * claim.want() * (lands.opinion(ruler, lands.ruler(m)) < 0 ? 1.5 : 1);
            choices.add(claim);
            weights.add(w);
            total += w;
        }
        for (int m : neighbourNations(n, lands)) {
            int other = lands.ruler(m);
            if (!lands.canWage(m) || other < 0 || allied(n, m) || atWar(m) || lands.opinion(ruler, other) <= 0) {
                continue;
            }
            double w = p.stat(Stat.DIPLOMACY) / 20.0 * (lands.opinion(ruler, other) + 50) / 100.0;
            choices.add(m);
            weights.add(w);
            total += w;
        }
        double roll = random.nextDouble() * total;
        for (int i = 0; i < choices.size(); i++) {
            roll -= weights.get(i);
            if (roll < 0) {
                if (choices.get(i) instanceof Claim claim) {
                    declare(n, claim.defender(), claim.goal(), day, lands, random, front);
                } else {
                    propose(n, (Integer) choices.get(i), day, lands);
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

    // ---- what is worth a war ----

    /**
     * The titles a nation might fight for: each county and duchy beside or within its land, held (mostly) by another
     * nation that can fight, not holding that nation's seat; with how much it wants each, the more the title would make
     * its land whole: a hole or enclave in it (the title's other land surrounded by its own), a bridge between its
     * separate pieces, or a title it already partly holds.
     */
    static List<Claim> claims(int n, Lands lands) {
        List<Claim> claims = new ArrayList<>();
        int[] piece = pieces(n, lands);
        for (int c = 0; c < lands.counties(); c++) {
            Claim claim = claim(n, new Goal(false, c), lands, piece);
            if (claim != null) {
                claims.add(claim);
            }
        }
        Set<Integer> duchies = new TreeSet<>();
        for (int c = 0; c < lands.counties(); c++) {
            duchies.add(lands.duchy(c));
        }
        for (int d : duchies) {
            Claim claim = claim(n, new Goal(true, d), lands, piece);
            if (claim != null && claim.goal().counties(lands).size() > 1) {
                claims.add(claim);
            }
        }
        return claims;
    }

    @Nullable
    static Claim claim(int n, Goal goal, Lands lands, int[] piece) {
        List<Integer> provinces = goal.provinces(lands);
        Map<Integer, Integer> held = new HashMap<>();
        int mine = 0;
        for (int p : provinces) {
            int o = lands.owner(p);
            if (o == n) {
                mine++;
            } else if (o >= 0) {
                held.merge(o, 1, Integer::sum);
            }
        }
        if (held.isEmpty()) {
            return null;
        }
        int defender = held.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        if (!lands.canWage(defender) || provinces.contains(lands.seat(defender))) {
            return null;
        }
        // Their land in the title: what touches it, and how much of that is ours; which of our pieces it touches.
        int border = 0;
        int ours = 0;
        Set<Integer> touched = new TreeSet<>();
        for (int p : provinces) {
            if (lands.owner(p) != defender) {
                continue;
            }
            for (int q : lands.neighbours(p)) {
                if (provinces.contains(q) && lands.owner(q) == defender) {
                    continue;
                }
                border++;
                if (lands.owner(q) == n) {
                    ours++;
                    touched.add(piece[q]);
                }
            }
        }
        if (ours == 0 && mine == 0) {
            return null; // nowhere near us
        }
        double enclosed = border == 0 ? 0 : ours / (double) border;
        double share = mine / (double) provinces.size();
        double bridge = touched.size() > 1 ? 1 : 0;
        double want = 0.3 + 2.0 * enclosed + 1.5 * bridge + 1.0 * share;
        return new Claim(goal, defender, want);
    }

    /** Which piece of a nation's land each of its provinces is in (others: -1): its land is whole if it has one piece. */
    static int[] pieces(int n, Lands lands) {
        int[] piece = new int[lands.provinces()];
        java.util.Arrays.fill(piece, -1);
        int count = 0;
        for (int start = 0; start < lands.provinces(); start++) {
            if (lands.owner(start) != n || piece[start] >= 0) {
                continue;
            }
            ArrayDeque<Integer> open = new ArrayDeque<>(List.of(start));
            piece[start] = count;
            while (!open.isEmpty()) {
                for (int q : lands.neighbours(open.poll())) {
                    if (lands.owner(q) == n && piece[q] < 0) {
                        piece[q] = count;
                        open.add(q);
                    }
                }
            }
            count++;
        }
        return piece;
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

    // ---- war ----

    /** Declares war for a title: the allies are called, and both sides raise their armies. */
    @Nullable
    public War declare(int n, int m, Goal goal, long day, Lands lands, Random random, Front front) {
        War w = new War(nextWar++, n, m, goal, day);
        StringBuilder text = new StringBuilder(lands.nationName(n) + " declared war on " + lands.nationName(m) + " for the "
                + lands.titleName(goal) + ".");
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
        double now = Math.max(day, moved);
        if (!aim(w, lands) || (w.attackerArmy = raise(w, true, now, lands)) == null) {
            return null; // nothing to fight over, or no one to fight with
        }
        wars.add(w);
        log(day, text.toString());
        front.raised(w, true);
        w.defenderArmy = raise(w, false, now, lands);
        if (w.defenderArmy == null) {
            ceded(w, day, lands); // no one to defend it
            return w;
        }
        front.raised(w, false);
        return w;
    }

    /** Sets the next county to fight for (the defender's in the title nearest our land) and the battle site on its border. */
    static boolean aim(War w, Lands lands) {
        int best = -1;
        double bestDistance = Double.MAX_VALUE;
        double[] bestSite = null;
        for (int c : w.goal.counties(lands)) {
            for (int p = 0; p < lands.provinces(); p++) {
                if (lands.county(p) != c || lands.owner(p) != w.defender) {
                    continue;
                }
                int[] centre = lands.centre(p);
                for (int q : lands.neighbours(p)) {
                    if (w.side(true).contains(lands.owner(q))) {
                        int[] from = lands.centre(q);
                        double[] site = {(centre[0] + from[0]) / 2.0, (centre[1] + from[1]) / 2.0};
                        double d = w.attackerArmy == null ? 0 : Math.hypot(site[0] - w.attackerArmy.x, site[1] - w.attackerArmy.z);
                        if (d < bestDistance) {
                            bestDistance = d;
                            best = c;
                            bestSite = site;
                        }
                    }
                }
            }
        }
        if (best < 0) {
            // None of it borders us any more (an island of the title): march on the nearest of its counties' land.
            for (int p = 0; p < lands.provinces(); p++) {
                if (w.goal.counties(lands).contains(lands.county(p)) && lands.owner(p) == w.defender) {
                    best = lands.county(p);
                    int[] centre = lands.centre(p);
                    bestSite = new double[]{centre[0], centre[1]};
                    break;
                }
            }
        }
        if (best < 0) {
            return false;
        }
        w.target = best;
        w.siteX = bestSite[0];
        w.siteZ = bestSite[1];
        return true;
    }

    /** Raises a side's army as this world musters them, all the manpower its leader has and half its allies'; null if too few. */
    @Nullable
    Army raise(War w, boolean attacking, double now, Lands lands) {
        return raise(w, attacking, now, lands, MUSTERING);
    }

    @Nullable
    Army raise(War w, boolean attacking, double now, Lands lands, Muster mustering) {
        List<Integer> side = w.side(attacking);
        int soldiers = 0;
        for (int i = 0; i < side.size(); i++) {
            int available = manpower(side.get(i), lands);
            soldiers += i == 0 ? available : available / 2;
        }
        if (soldiers < MIN_ARMY) {
            return null;
        }
        List<Lands.Stronghold> holds = lands.strongholds(side.get(0));
        Lands.Stronghold capital = holds.get(0);
        Lands.Stronghold nearest = capital;
        for (Lands.Stronghold h : holds) {
            if (Math.hypot(h.x() - w.siteX, h.z() - w.siteZ) < Math.hypot(nearest.x() - w.siteX, nearest.z() - w.siteZ)) {
                nearest = h;
            }
        }
        if (mustering == Muster.OFFENSIVE) {
            return new Army(soldiers, nearest.x(), nearest.z(), now + MUSTER, nearest.name());
        }
        // Ceremonial: at the capital, then by the city nearest the field.
        Army army = new Army(soldiers, capital.x(), capital.z(), now + MUSTER, capital.name());
        if (nearest != capital) {
            army.stops.add(new double[]{nearest.x(), nearest.z()});
        }
        return army;
    }

    /**
     * How an army musters. <b>Ceremonial</b>: at the capital, then marching by the nation's city or capital nearest the
     * battle site, then to the battle (in straight lines for now; along the roads is to come). <b>Offensive</b>: at the
     * nation's city or castle nearest the battle site, marching straight to it.
     */
    public enum Muster {
        CEREMONIAL, OFFENSIVE
    }

    /** How every army musters, for now. */
    static final Muster MUSTERING = Muster.OFFENSIVE;

    /** Moves the armies to now (in days), and fights the battles of those that have both arrived. */
    public void advance(double now, Lands lands, Random random, Front front) {
        double dt = moved < 0 ? 0 : Math.max(0, Math.min(1, now - moved));
        moved = now;
        for (War w : new ArrayList<>(wars)) {
            if (awaiting.contains(w.id) || w.attackerArmy == null || w.defenderArmy == null) {
                continue;
            }
            boolean here = march(w.attackerArmy, w, now, dt) & march(w.defenderArmy, w, now, dt);
            if (here) {
                fight(w, now, lands, random, front);
            }
        }
    }

    /** Moves an army toward the battle site; true once it is there. */
    private static boolean march(Army army, War w, double now, double dt) {
        if (army.mustering(now)) {
            return false;
        }
        double step = MARCH * Math.min(dt, now - army.readyAt); // not the time it spent mustering
        // Through its stops, then to the field, as far as the day's march carries it.
        while (step > 0) {
            double[] to = army.heading(w);
            double dx = to[0] - army.x;
            double dz = to[1] - army.z;
            double left = Math.hypot(dx, dz);
            if (left > step) {
                army.x += dx / left * step;
                army.z += dz / left * step;
                return false;
            }
            army.x = to[0];
            army.z = to[1];
            step -= left;
            if (army.stops.isEmpty()) {
                return true;
            }
            army.stops.remove(0);
        }
        return army.stops.isEmpty() && army.x == w.siteX && army.z == w.siteZ;
    }

    /** Both armies are at the site: they fight, for real if someone is near, else by strength. */
    void fight(War w, double now, Lands lands, Random random, Front front) {
        int attackers = w.attackerArmy.soldiers;
        int defenders = w.defenderArmy.soldiers;
        if (front.fightForReal(w, attackers, defenders)) {
            awaiting.add(w.id);
            return;
        }
        double a = Math.pow(attackers, 1.2);
        double d = Math.pow(defenders * DEFENDER_EDGE, 1.2);
        boolean attackerWon = random.nextDouble() < a / (a + d);
        double winnerLoss = 0.10 + random.nextDouble() * 0.15;
        double loserLoss = 0.35 + random.nextDouble() * 0.30;
        after(w, attackerWon, 1 - (attackerWon ? winnerLoss : loserLoss), 1 - (attackerWon ? loserLoss : winnerLoss), now, lands, random, front);
    }

    /**
     * A battle fought for real is over: who won, and what share of each army still stands (scaled from the battle to the
     * armies).
     */
    public void realBattle(long warId, boolean attackerWon, double attackersLeft, double defendersLeft, double now, Lands lands,
                           Random random, Front front) {
        awaiting.remove(warId);
        for (War w : new ArrayList<>(wars)) {
            if (w.id == warId) {
                after(w, attackerWon, attackersLeft, defendersLeft, now, lands, random, front);
                return;
            }
        }
    }

    /** A real battle that came to nothing (stopped before it ended): the armies stay, and will fight again. */
    public void realBattleCancelled(long warId) {
        awaiting.remove(warId);
    }

    /** After a battle: the dead counted, a county taken if the attacker won, then each side's choice to fight on or not. */
    void after(War w, boolean attackerWon, double attackersLeft, double defendersLeft, double now, Lands lands, Random random, Front front) {
        long day = (long) Math.floor(now);
        w.battles++;
        bury(w.attacker, w.attackerArmy, attackersLeft);
        bury(w.defender, w.defenderArmy, defendersLeft);
        String county = lands.titleName(new Goal(false, w.target));
        if (attackerWon) {
            take(w, w.target, lands);
            log(day, lands.nationName(w.attacker) + " beat " + lands.nationName(w.defender) + " in battle, and took the " + county + ".");
            if (!aim(w, lands)) {
                peace(w, day, lands, lands.nationName(w.attacker) + " won the war: the whole " + lands.titleName(w.goal) + " is theirs.");
                return;
            }
            if (sues(w, false, lands, random)) {
                ceded(w, day, lands);
                return;
            }
            if (random.nextDouble() < content(lands.person(lands.ruler(w.attacker)))) {
                peace(w, day, lands, lands.nationName(w.attacker) + " was content with the " + county + " and made peace.");
                return;
            }
            // The beaten army goes home; another is raised, if the people can be found.
            w.defenderArmy = null;
            w.defenderArmy = raise(w, false, now, lands);
            if (w.defenderArmy == null) {
                ceded(w, day, lands);
                return;
            }
            front.raised(w, false);
            w.attackerArmy.readyAt = now + REST;
        } else {
            log(day, lands.nationName(w.defender) + " beat " + lands.nationName(w.attacker) + " in battle for the " + county + ".");
            if (sues(w, true, lands, random)) {
                peace(w, day, lands, lands.nationName(w.attacker) + " sued for peace" + (w.taken.isEmpty() ? ", and gained nothing."
                        : ", keeping what it had taken."));
                return;
            }
            w.attackerArmy = null;
            w.attackerArmy = raise(w, true, now, lands);
            if (w.attackerArmy == null) {
                peace(w, day, lands, lands.nationName(w.attacker) + " had no army left to raise, and made peace.");
                return;
            }
            front.raised(w, true);
        }
    }

    /** An army's dead go on its nation's count. */
    private void bury(int nation, Army army, double left) {
        int fell = (int) Math.round(army.soldiers * (1 - Math.max(0, Math.min(1, left))));
        army.soldiers -= fell;
        dead.merge(nation, fell, Integer::sum);
    }

    /**
     * Whether a side beaten in battle sues for peace rather than raise another army: likelier the less manpower it has
     * left (the more of its people it would risk), the stronger the enemy, the less there is left to lose (a defender),
     * and the meeker its ruler. A side that can't raise an army at all must.
     */
    boolean sues(War w, boolean attacking, Lands lands, Random random) {
        int nation = attacking ? w.attacker : w.defender;
        int levy = Math.max(1, lands.levy(nation));
        int manpower = manpower(nation, lands);
        Army enemy = attacking ? w.defenderArmy : w.attackerArmy;
        if (manpower < MIN_ARMY || enemy != null && manpower < enemy.soldiers / 3) {
            return true;
        }
        double p = 0.15 + 0.5 * (1 - manpower / (double) levy);
        if (enemy != null) {
            p += Math.max(-0.15, Math.min(0.3, 0.15 * (enemy.soldiers / (double) manpower - 1)));
        }
        if (!attacking) {
            int left = 0;
            for (int c : w.goal.counties(lands)) {
                if (!w.taken.contains(c)) {
                    left++;
                }
            }
            p += 0.15 / Math.max(1, left); // the last county is the cheapest to give up
        } else {
            p += 0.1; // a beaten attacker has only their pride to lose
        }
        p += temper(lands.person(lands.ruler(nation)));
        return random.nextDouble() < Math.max(0.02, Math.min(0.95, p));
    }

    /** How readily a ruler gives up a fight: the meek more, the proud and the angry less. */
    static double temper(Person p) {
        double t = 0;
        for (Personality trait : p.traits()) {
            t += switch (trait) {
                case CRAVEN -> 0.2;
                case COMPASSIONATE -> 0.15;
                case CALM, CONTENT -> 0.1;
                case WRATHFUL, ARROGANT -> -0.15;
                case VENGEFUL, BRAVE -> -0.1;
                default -> 0;
            };
        }
        return t;
    }

    /** How likely a winning attacker is to stop with what it has taken. */
    static double content(Person p) {
        double c = 0.03;
        for (Personality trait : p.traits()) {
            c += switch (trait) {
                case CONTENT -> 0.2;
                case CALM, PATIENT -> 0.05;
                case AMBITIOUS, GREEDY -> -0.03;
                default -> 0;
            };
        }
        return Math.max(0, c);
    }

    /** The defender gives up the rest of the title. */
    private void ceded(War w, long day, Lands lands) {
        for (int c : w.goal.counties(lands)) {
            take(w, c, lands);
        }
        peace(w, day, lands, lands.nationName(w.defender) + " sued for peace, and ceded the " + lands.titleName(w.goal) + " to "
                + lands.nationName(w.attacker) + ".");
    }

    /** The defender's provinces in a county go to the attacker. */
    private void take(War w, int county, Lands lands) {
        for (int p = 0; p < lands.provinces(); p++) {
            if (lands.county(p) == county && lands.owner(p) == w.defender) {
                lands.transfer(p, w.attacker);
                conquered.put(p, w.attacker);
            }
        }
        if (!w.taken.contains(county)) {
            w.taken.add(county);
        }
    }

    private void peace(War w, long day, Lands lands, String text) {
        wars.remove(w);
        awaiting.remove(w.id);
        log(day, text);
        for (int a : w.side(true)) {
            for (int b : w.side(false)) {
                truces.put(pair(a, b), day + TRUCE_DAYS);
            }
        }
    }

    private void log(long day, String text) {
        chronicle.add(new Event(day, text));
    }

    static long pair(int a, int b) {
        return ((long) Math.min(a, b) << 16) | Math.max(a, b);
    }

    // ---- kept with the world ----

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
        CompoundTag d = new CompoundTag();
        dead.forEach((n, count) -> d.putInt(Integer.toString(n), count));
        tag.put("dead", d);
        ListTag events = new ListTag();
        for (Event e : chronicle) {
            CompoundTag et = new CompoundTag();
            et.putLong("day", e.day());
            et.putString("text", e.text());
            events.add(et);
        }
        tag.put("chronicle", events);
        tag.putLong("nextWar", nextWar);
        tag.putDouble("moved", moved);
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
        CompoundTag d = tag.getCompound("dead");
        for (String k : d.getAllKeys()) {
            p.dead.put(Integer.parseInt(k), d.getInt(k));
        }
        for (Tag e : tag.getList("chronicle", Tag.TAG_COMPOUND)) {
            CompoundTag et = (CompoundTag) e;
            p.chronicle.add(new Event(et.getLong("day"), et.getString("text")));
        }
        p.nextWar = Math.max(1, tag.getLong("nextWar"));
        p.moved = tag.contains("moved") ? tag.getDouble("moved") : -1;
        return p;
    }
}
