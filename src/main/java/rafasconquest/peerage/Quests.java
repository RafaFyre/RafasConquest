package rafasconquest.peerage;

import com.electronwill.nightconfig.core.Config;
import rafasconquest.RafasConquest;
import rafasconquest.core.CityLayout;
import rafasconquest.core.Countryside;
import rafasconquest.core.Dojos;
import rafasconquest.core.RecruitmentPost;
import rafasconquest.data.DataFiles;
import rafasconquest.data.Dialogue;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * The quests on a city's board, worked out for each day from the world, the city and the day (the same for everyone):
 * <ul>
 *   <li>its post's officer (the recruiter);</li>
 *   <li>the city's noble, and the lords of up to two castles of the nation nearby;</li>
 *   <li>two general quests, taken at the board itself, for emeralds alone.</li>
 * </ul>
 * A city no nation holds has only the general quests. What each may post are the {@link Kind}s read from
 * {@code mods/RafasConquest/quests} ({@link #load}): each names its task, who posts it, where it sends the player, its
 * titles and its pay.
 */
public final class Quests {
    /** How far away a castle's lord may post on a city's board. */
    static final int CASTLE_REACH = 2500;
    static final int GENERAL = 2;
    static final Set<String> GIVERS = Set.of("officer", "noble", "board");
    static final Set<String> TARGETS = Set.of("ally_hold", "ruin", "dojo", "foreign_city", "hamlet", "village");

    /** A kind of quest, from its file (its id is the file's path in the folder). */
    public record Kind(String id, Quest.Type task, List<String> givers, List<String> targets, List<String> titles, int need,
                       int renown, int emeralds, int reach, int weight) {
    }

    private static volatile List<Kind> kinds = List.of();

    private Quests() {
    }

    private record Target(String kind, String place, int x, int z) {
    }

    /** Reads the kinds of quest; a file read later (the user's) replaces one of the same id (the mod's). */
    public static int load() {
        Map<String, Kind> read = new TreeMap<>(); // sorted, so a board's picks are the same on every server
        DataFiles.each("quests", (id, config) -> {
            try {
                read.put(id, kind(id, config));
            } catch (RuntimeException e) {
                RafasConquest.LOGGER.warn("[data] Quest {} skipped: {}", id, e.getMessage());
            }
        });
        kinds = List.copyOf(read.values());
        return kinds.size();
    }

    static Kind kind(String id, Config c) {
        Object task = c.get("task");
        if (task == null) {
            throw new IllegalArgumentException("it has no task (deliver, scout or clear)");
        }
        Quest.Type type = Quest.Type.valueOf(task.toString().toUpperCase(Locale.ROOT));
        List<String> givers = texts(c, "givers");
        List<String> targets = texts(c, "targets");
        List<String> titles = texts(c, "title");
        for (String g : givers) {
            if (!GIVERS.contains(g)) {
                throw new IllegalArgumentException("no such giver: " + g + " (one of " + GIVERS + ")");
            }
        }
        for (String t : targets) {
            if (!TARGETS.contains(t)) {
                throw new IllegalArgumentException("no such target: " + t + " (one of " + TARGETS + ")");
            }
        }
        if (givers.isEmpty() || targets.isEmpty() || titles.isEmpty()) {
            throw new IllegalArgumentException("it needs givers, targets and a title");
        }
        int need = number(c, "need", 0);
        if (type == Quest.Type.CLEAR && need <= 0) {
            throw new IllegalArgumentException("a clear quest needs a need above 0");
        }
        return new Kind(id, type, givers, targets, titles, type == Quest.Type.CLEAR ? need : 0, number(c, "renown", 0),
                number(c, "emeralds", 0), number(c, "reach", 3000), number(c, "weight", 1));
    }

    private static List<String> texts(Config c, String key) {
        Object v = c.get(key);
        return v instanceof List<?> list ? list.stream().map(String::valueOf).toList() : v == null ? List.of() : List.of(v.toString());
    }

    private static int number(Config c, String key, int otherwise) {
        Object v = c.get(key);
        return v instanceof Number n ? n.intValue() : otherwise;
    }

    /** The quests on a city's board on a day. */
    public static List<Quest> board(long seed, BaseMap map, Peerage peerage, CityLayout.Site city, long day) {
        Realm realm = map.realm();
        int province = map.provinceIndexAt(city.x(), city.z());
        BaseMap.Nation nation = map.holderOf(province);
        int nationIndex = nation == null ? -1 : map.nations().indexOf(nation);
        RecruitmentPost post = RecruitmentPost.of(city);
        Random random = new Random(seed ^ (province * 0x9E3779B97F4A7C15L) ^ (day * 0xC2B2AE3D27D4EB4FL));
        List<Quest> quests = new ArrayList<>();
        int slot = 0;
        if (nation != null) {
            String nationId = nation.id().toString();
            int[] at = post.recruiter();
            quests.add(make(random, map, nationIndex, Quest.Ladder.MILITARY, "officer", Quest.OFFICER,
                    Dialogue.line(random, "quests.givers.officer", "nation", nation.name()), at[0], at[1],
                    city.x(), city.z(), nationId, province, day, slot++));
            int holder = realm.holderOf(province);
            if (holder >= 0 && !peerage.replaced(province)) {
                int[] n = post.noble();
                quests.add(make(random, map, nationIndex, Quest.Ladder.NOBLE, "noble", holder, Names.styled(map, realm.people().get(holder)), n[0], n[1],
                        city.x(), city.z(), nationId, province, day, slot++));
            }
            List<Integer> castles = new ArrayList<>();
            for (int i = 0; i < realm.people().size(); i++) {
                Person p = realm.people().get(i);
                if (p.nation() == nationIndex && p.hold().kind() == Person.Kind.CASTLE && !peerage.replaced(p.hold().province())
                        && Math.hypot(p.hold().x() - city.x(), p.hold().z() - city.z()) <= CASTLE_REACH) {
                    castles.add(i);
                }
            }
            castles.sort(java.util.Comparator.comparingDouble(i -> Math.hypot(realm.people().get(i).hold().x() - city.x(),
                    realm.people().get(i).hold().z() - city.z())));
            for (int i : castles.subList(0, Math.min(2, castles.size()))) {
                Person lord = realm.people().get(i);
                quests.add(make(random, map, nationIndex, Quest.Ladder.NOBLE, "noble", i, Names.styled(map, lord), lord.hold().x(), lord.hold().z(),
                        city.x(), city.z(), nationId, province, day, slot++));
            }
        }
        int[] b = post.board();
        String boardName = Dialogue.line(random, "quests.givers.board", "place", map.names().get(province));
        for (int i = 0; i < GENERAL; i++) {
            quests.add(make(random, map, nationIndex, Quest.Ladder.NONE, "board", Quest.BOARD, boardName, b[0], b[1],
                    city.x(), city.z(), nation == null ? "" : nation.id().toString(), province, day, slot++));
        }
        quests.removeIf(q -> q == null);
        return quests;
    }

    /** A quest from one of the kinds this giver posts, picked by weight, that has a target near; null if none has. */
    private static Quest make(Random random, BaseMap map, int nation, Quest.Ladder ladder, String giverKind, int giver, String giverName,
                              int gx, int gz, int cx, int cz, String nationId, int province, long day, int slot) {
        List<Kind> pool = new ArrayList<>();
        for (Kind k : kinds) {
            if (k.weight() > 0 && k.givers().contains(giverKind)) {
                pool.add(k);
            }
        }
        while (!pool.isEmpty()) {
            int total = pool.stream().mapToInt(Kind::weight).sum();
            int roll = random.nextInt(total);
            int at = 0;
            while (roll >= pool.get(at).weight()) {
                roll -= pool.get(at++).weight();
            }
            Kind kind = pool.remove(at);
            List<Target> targets = targets(map, nation, kind, cx, cz);
            if (targets.isEmpty()) {
                continue;
            }
            Target t = targets.get(random.nextInt(targets.size()));
            String target = Dialogue.line(random, "quests.targets." + t.kind(), "place", t.place());
            String title = Dialogue.fill(kind.titles().get(random.nextInt(kind.titles().size())), "target", target, "need", kind.need());
            long id = ((long) province << 32) ^ (day << 8) ^ slot;
            return new Quest(id, kind.task(), ladder, giver, giverName, gx, gz, target, t.x(), t.z(), kind.need(),
                    ladder == Quest.Ladder.NONE ? 0 : kind.renown(), kind.emeralds(), nationId, title);
        }
        return null;
    }

    private static List<Target> targets(BaseMap map, int nation, Kind kind, int cx, int cz) {
        List<Target> targets = new ArrayList<>();
        Realm realm = map.realm();
        int reach = kind.reach();
        for (String what : kind.targets()) {
            switch (what) {
                case "ally_hold" -> {
                    for (Person p : realm.people()) {
                        double d = Math.hypot(p.hold().x() - cx, p.hold().z() - cz);
                        if (p.nation() == nation && nation >= 0 && d > 200 && d <= reach) {
                            targets.add(new Target(what, Names.styled(map, p), p.hold().x(), p.hold().z()));
                        }
                    }
                }
                case "ruin", "hamlet", "village" -> {
                    Countryside.Kind wanted = Countryside.Kind.valueOf(what.toUpperCase(Locale.ROOT));
                    for (Countryside.Settlement s : map.settlements()) {
                        if (s.kind() == wanted && Math.hypot(s.x() - cx, s.z() - cz) <= reach) {
                            targets.add(new Target(what, map.names().get(s.province()), s.x(), s.z()));
                        }
                    }
                }
                case "dojo" -> {
                    for (Dojos.Dojo d : map.dojos()) {
                        if (!d.school().hidden() && Math.hypot(d.x() - cx, d.z() - cz) <= reach) {
                            targets.add(new Target(what, d.school().title(), d.x(), d.z()));
                        }
                    }
                }
                case "foreign_city" -> {
                    for (Person p : realm.people()) {
                        if (p.nation() != nation && p.hold().kind() != Person.Kind.CASTLE && p.noble() && !p.hold().kind().heir()
                                && Math.hypot(p.hold().x() - cx, p.hold().z() - cz) <= reach) {
                            targets.add(new Target(what, map.names().get(p.hold().province()), p.hold().x(), p.hold().z()));
                        }
                    }
                }
                default -> {
                }
            }
        }
        return targets;
    }
}
