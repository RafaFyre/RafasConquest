package rafasconquest.people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Which sword sanctum each Sword King and Sword Saint trained at. On paper it is chance, but the nearer a sanctum, the
 * likelier: each draws a sanctum with a weight falling off with distance. A sanctum keeps 2 to 7 Sword Kings (any more
 * trained nowhere in particular); it takes every Sword Saint that draws it.
 */
public final class Organizations {
    static final int MIN_KINGS = 2;
    static final int MAX_KINGS = 7;
    /** At this distance (blocks) a sanctum is half as likely as one at the door. */
    static final double HALF_WEIGHT = 4000;

    /** A sanctum: its name, its nation, its Sword God (or -1), and where it stands. */
    public record Sanctum(String name, int nation, int head, int x, int z) {
    }

    private Organizations() {
    }

    public static List<Organization> plan(Random random, List<Person> people, List<Sanctum> sanctums) {
        if (sanctums.isEmpty()) {
            return List.of();
        }
        List<List<Integer>> kings = new ArrayList<>();
        List<List<Integer>> saints = new ArrayList<>();
        int[] room = new int[sanctums.size()];
        for (int s = 0; s < sanctums.size(); s++) {
            kings.add(new ArrayList<>());
            saints.add(new ArrayList<>());
            room[s] = MIN_KINGS + random.nextInt(MAX_KINGS - MIN_KINGS + 1);
        }
        List<Integer> heads = sanctums.stream().map(Sanctum::head).toList();
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            order.add(i);
        }
        Collections.shuffle(order, random);
        for (int i : order) {
            Person p = people.get(i);
            int prowess = p.stat(Stat.PROWESS);
            if (heads.contains(i) || prowess < Stat.SUPERHUMAN || prowess >= Stat.SWORD_GOD) {
                continue;
            }
            boolean king = prowess >= Stat.SWORD_KING;
            int s = draw(random, p, sanctums, king ? room : null);
            if (s < 0) {
                continue;
            }
            if (king) {
                kings.get(s).add(i);
                room[s]--;
            } else {
                saints.get(s).add(i);
            }
        }
        List<Organization> all = new ArrayList<>();
        for (int s = 0; s < sanctums.size(); s++) {
            Sanctum sanctum = sanctums.get(s);
            // The strongest first, as they stand.
            kings.get(s).sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            saints.get(s).sort((a, b) -> people.get(b).stat(Stat.PROWESS) - people.get(a).stat(Stat.PROWESS));
            all.add(new Organization(sanctum.name(), sanctum.nation(), sanctum.head(), List.copyOf(kings.get(s)), List.copyOf(saints.get(s))));
        }
        return all;
    }

    /** A sanctum for someone, likelier the nearer; only those with room, if {@code room} is given; -1 if none has. */
    static int draw(Random random, Person p, List<Sanctum> sanctums, int[] room) {
        double[] weight = new double[sanctums.size()];
        double total = 0;
        for (int s = 0; s < sanctums.size(); s++) {
            if (room != null && room[s] <= 0) {
                continue;
            }
            Sanctum sanctum = sanctums.get(s);
            weight[s] = weight(Math.hypot(p.hold().x() - sanctum.x(), p.hold().z() - sanctum.z()));
            total += weight[s];
        }
        if (total <= 0) {
            return -1;
        }
        double roll = random.nextDouble() * total;
        for (int s = 0; s < weight.length; s++) {
            roll -= weight[s];
            if (weight[s] > 0 && roll < 0) {
                return s;
            }
        }
        for (int s = weight.length - 1; s >= 0; s--) {
            if (weight[s] > 0) {
                return s;
            }
        }
        return -1;
    }

    static double weight(double distance) {
        double d = distance / HALF_WEIGHT;
        return 1 / (1 + d * d);
    }
}
