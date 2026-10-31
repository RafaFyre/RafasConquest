package rafasconquest.politics;

import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The world as the politics see it, from the world map: the provinces and who holds them (the map's own record, which
 * conquests change in place), which touch which, the realm's rulers, and each nation's army: a twentieth of its people,
 * shrunk by the share of its land it has lost (or grown by what it has won).
 */
public final class MapLands implements Lands {
    /** A nation fields one soldier for this many of its people. */
    static final int LEVY = 20;

    private final BaseMap map;
    private final List<Set<Integer>> adjacent;
    /** How many provinces each nation held at the start. */
    private final int[] heldAtStart;

    private static MapLands cached;

    private MapLands(BaseMap map) {
        this.map = map;
        this.adjacent = adjacency(map);
        this.heldAtStart = new int[map.nations().size()];
        for (int p = 0; p < map.holders().length; p++) {
            int n = map.holders()[p];
            if (n >= 0) {
                heldAtStart[n]++;
            }
        }
    }

    /** The lands of a map, worked out once for it (its holders may since have changed by conquest). */
    public static synchronized MapLands of(BaseMap map) {
        if (cached == null || cached.map != map) {
            cached = new MapLands(map);
        }
        return cached;
    }

    @Override
    public int nations() {
        return map.nations().size();
    }

    @Override
    public int provinces() {
        return map.holders().length;
    }

    @Override
    public boolean canWage(int nation) {
        return !map.nations().get(nation).sanctum() && ruler(nation) >= 0 && held(nation) > 0;
    }

    @Override
    public int ruler(int nation) {
        return map.realm().ruler(nation);
    }

    @Override
    public Person person(int index) {
        return map.realm().people().get(index);
    }

    @Override
    public int opinion(int a, int b) {
        return map.realm().opinion(a, b);
    }

    @Override
    public int owner(int province) {
        return province < 0 || province >= map.holders().length ? -1 : map.holders()[province];
    }

    @Override
    public void transfer(int province, int nation) {
        map.holders()[province] = nation;
    }

    @Override
    public int seat(int nation) {
        return map.nations().get(nation).seat();
    }

    @Override
    public Set<Integer> neighbours(int province) {
        return adjacent.get(province);
    }

    @Override
    public int soldiers(int nation) {
        int people = map.realm().demographics(nation).population();
        int start = Math.max(1, heldAtStart[nation]);
        return Math.max(20, (int) ((long) people * held(nation) / start / LEVY));
    }

    private int held(int nation) {
        int count = 0;
        for (int n : map.holders()) {
            if (n == nation) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String nationName(int nation) {
        return map.nations().get(nation).name();
    }

    @Override
    public String provinceName(int province) {
        return map.names().get(province);
    }

    @Override
    public int[] centre(int province) {
        return new int[]{map.centres().get(province).x(), map.centres().get(province).z()};
    }

    /** Which provinces touch which, from the map's pixels. */
    private static List<Set<Integer>> adjacency(BaseMap map) {
        int n = map.centres().size();
        List<Set<Integer>> adjacent = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjacent.add(new TreeSet<>());
        }
        int w = map.width();
        int[] provinces = map.provinces();
        for (int z = 0; z < map.height(); z++) {
            for (int x = 0; x < w; x++) {
                int p = provinces[z * w + x];
                if (p < 0) {
                    continue;
                }
                int right = x + 1 < w ? provinces[z * w + x + 1] : -1;
                int down = z + 1 < map.height() ? provinces[(z + 1) * w + x] : -1;
                for (int q : new int[]{right, down}) {
                    if (q >= 0 && q != p) {
                        adjacent.get(p).add(q);
                        adjacent.get(q).add(p);
                    }
                }
            }
        }
        return adjacent;
    }
}
