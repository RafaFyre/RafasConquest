package rafasconquest.peerage;

import rafasconquest.core.CityLayout;
import rafasconquest.core.Countryside;
import rafasconquest.core.Dojos;
import rafasconquest.core.RecruitmentPost;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The quests on a city's board, worked out for each day from the world, the city and the day (the same for everyone):
 * <ul>
 *   <li>its post's officer (the recruiter): a military quest, to scout or to clear;</li>
 *   <li>the city's noble, and the lords of up to two castles of the nation nearby: a noble quest, to deliver, scout or
 *       clear;</li>
 *   <li>two general quests, taken at the board itself, to deliver or scout, for emeralds alone.</li>
 * </ul>
 * A city no nation holds has only the general quests. Targets are near: a hold of the same nation to deliver to; a
 * ruin, a known dojo or a foreign city to scout; a hamlet or village to clear about.
 */
public final class Quests {
    /** How far away a castle's lord may post on a city's board, and how far away a target may be. */
    static final int CASTLE_REACH = 2500;
    static final int DELIVER_REACH = 4000;
    static final int SCOUT_REACH = 3500;
    static final int CLEAR_REACH = 2000;
    static final int CLEAR_NEED = 8;
    static final int GENERAL = 2;

    private Quests() {
    }

    private record Target(String name, int x, int z) {
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
            quests.add(make(random, map, nationIndex, Quest.Ladder.MILITARY, Quest.OFFICER, "Recruiter of " + nation.name(), at[0], at[1],
                    city.x(), city.z(), nationId, province, day, slot++, new Quest.Type[]{Quest.Type.SCOUT, Quest.Type.CLEAR}));
            int holder = realm.holderOf(province);
            if (holder >= 0 && !peerage.replaced(province)) {
                int[] n = post.noble();
                quests.add(make(random, map, nationIndex, Quest.Ladder.NOBLE, holder, Names.styled(map, realm.people().get(holder)), n[0], n[1],
                        city.x(), city.z(), nationId, province, day, slot++, Quest.Type.values()));
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
                quests.add(make(random, map, nationIndex, Quest.Ladder.NOBLE, i, Names.styled(map, lord), lord.hold().x(), lord.hold().z(),
                        city.x(), city.z(), nationId, province, day, slot++, Quest.Type.values()));
            }
        }
        int[] b = post.board();
        for (int i = 0; i < GENERAL; i++) {
            quests.add(make(random, map, nationIndex, Quest.Ladder.NONE, Quest.BOARD, "the board of " + map.names().get(province), b[0], b[1],
                    city.x(), city.z(), nation == null ? "" : nation.id().toString(), province, day, slot++,
                    new Quest.Type[]{Quest.Type.DELIVER, Quest.Type.SCOUT}));
        }
        quests.removeIf(q -> q == null);
        return quests;
    }

    private static Quest make(Random random, BaseMap map, int nation, Quest.Ladder ladder, int giver, String giverName, int gx, int gz,
                              int cx, int cz, String nationId, int province, long day, int slot, Quest.Type[] types) {
        List<Quest.Type> order = new ArrayList<>(List.of(types));
        java.util.Collections.shuffle(order, random);
        for (Quest.Type type : order) {
            List<Target> targets = targets(map, nation, type, cx, cz);
            if (targets.isEmpty()) {
                continue;
            }
            Target t = targets.get(random.nextInt(targets.size()));
            int renown = ladder == Quest.Ladder.NONE ? 0 : switch (type) {
                case DELIVER -> 10;
                case SCOUT -> 15;
                case CLEAR -> 20;
            };
            int emeralds = switch (type) {
                case DELIVER -> 4;
                case SCOUT -> 6;
                case CLEAR -> 8;
            };
            long id = ((long) province << 32) ^ (day << 8) ^ slot;
            return new Quest(id, type, ladder, giver, giverName, gx, gz, t.name(), t.x(), t.z(), type == Quest.Type.CLEAR ? CLEAR_NEED : 0,
                    renown, emeralds, nationId);
        }
        return null;
    }

    private static List<Target> targets(BaseMap map, int nation, Quest.Type type, int cx, int cz) {
        List<Target> targets = new ArrayList<>();
        Realm realm = map.realm();
        switch (type) {
            case DELIVER -> {
                for (Person p : realm.people()) {
                    double d = Math.hypot(p.hold().x() - cx, p.hold().z() - cz);
                    if (p.nation() == nation && nation >= 0 && d > 200 && d <= DELIVER_REACH) {
                        targets.add(new Target(Names.styled(map, p), p.hold().x(), p.hold().z()));
                    }
                }
            }
            case SCOUT -> {
                for (Countryside.Settlement s : map.settlements()) {
                    if (s.kind() == Countryside.Kind.RUIN && Math.hypot(s.x() - cx, s.z() - cz) <= SCOUT_REACH) {
                        targets.add(new Target("the ruins of " + map.names().get(s.province()), s.x(), s.z()));
                    }
                }
                for (Dojos.Dojo d : map.dojos()) {
                    if (!d.school().hidden() && Math.hypot(d.x() - cx, d.z() - cz) <= SCOUT_REACH) {
                        targets.add(new Target("the dojo of the " + d.school().title() + " school", d.x(), d.z()));
                    }
                }
                for (Person p : realm.people()) {
                    if (p.nation() != nation && p.hold().kind() != Person.Kind.CASTLE && Math.hypot(p.hold().x() - cx, p.hold().z() - cz) <= SCOUT_REACH) {
                        targets.add(new Target("the city of " + map.names().get(p.hold().province()), p.hold().x(), p.hold().z()));
                    }
                }
            }
            case CLEAR -> {
                for (Countryside.Settlement s : map.settlements()) {
                    if ((s.kind() == Countryside.Kind.HAMLET || s.kind() == Countryside.Kind.VILLAGE)
                            && Math.hypot(s.x() - cx, s.z() - cz) <= CLEAR_REACH) {
                        targets.add(new Target("the " + (s.kind() == Countryside.Kind.VILLAGE ? "village" : "hamlet") + " of "
                                + map.names().get(s.province()), s.x(), s.z()));
                    }
                }
            }
        }
        return targets;
    }
}
