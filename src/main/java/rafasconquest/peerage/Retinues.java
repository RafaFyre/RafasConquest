package rafasconquest.peerage;

import com.talhanation.recruits.RecruitEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import rafasconquest.RafasConquest;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Organization;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Retinues and vassals' service, by standing in the peerage. A character's (or player's) standing runs from a castle's
 * lord (0) through a town's (1), a city's (2) and a capital's lord (3) to a ruler (4); a player's from none (0) through
 * knight (1) to count (2). A retinue's size rises in a line with it, from 1 (the most intimate) to 12 (a ruler's), and an
 * emperor's always holds a Sword God. A character's retinue is the best fighters of their nation below them who are not
 * already their vassals; a player's is the recruits they hired, each named and made a character as they join, who keep
 * with them wherever they go. In war a player's vassals come with their own vassals and troops, by standing.
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID)
public final class Retinues {
    public static final int TOP = 4;
    /** The troops a vassal brings to war by standing, castle lord to ruler; with all their own vassals' besides. */
    static final int[] TROOPS = {50, 100, 200, 300, 500};
    /** How far a retinue may fall behind before it is brought to its player. */
    private static final double KEEP_UP = 48;
    private static final int EVERY = 40;

    private Retinues() {
    }

    /** A character's standing in the peerage, 0 (castle lord) to {@link #TOP} (ruler). */
    public static int level(Realm realm, int person) {
        Person p = realm.people().get(person);
        if (p.nation() >= 0 && realm.ruler(p.nation()) == person) {
            return TOP;
        }
        if (p.hold() == null) {
            return 0;
        }
        return switch (p.hold().kind()) {
            case CAPITAL, LORD -> 3;
            case CITY -> 2;
            case TOWN -> 1;
            case CASTLE, RETAINER -> 0;
        };
    }

    /** A player's standing: none 0, knight 1, count 2. */
    public static int level(NobleTitle title) {
        return title.ordinal();
    }

    /** How many a retinue holds at a standing: 1 at the bottom, 12 at the top, in a line between. */
    public static int size(int level) {
        return Math.round(1 + 11f * Math.max(0, Math.min(TOP, level)) / TOP);
    }

    /** A character's retinue: a Sword God first if they are an emperor, then their nation's best fighters below them. */
    public static List<Integer> of(BaseMap map, int person) {
        Realm realm = map.realm();
        Person p = realm.people().get(person);
        int level = level(realm, person);
        int size = size(level);
        List<Integer> retinue = new ArrayList<>();
        boolean emperor = level == TOP && p.nation() >= 0
                && map.nations().get(p.nation()).tier() == rafasconquest.core.TitleMap.Tier.EMPIRE;
        if (emperor) {
            BaseMap.Nation own = map.nations().get(p.nation());
            realm.organizations().stream()
                    .filter(o -> o.head() >= 0)
                    .min(Comparator.comparingDouble((Organization o) -> Math.hypot(map.nations().get(o.nation()).x() - own.x(),
                            map.nations().get(o.nation()).z() - own.z())))
                    .ifPresent(o -> retinue.add(o.head()));
        }
        Set<Integer> vassals = new HashSet<>(realm.vassals(person));
        List<Integer> fighters = new ArrayList<>();
        for (int i = 0; i < realm.people().size(); i++) {
            Person q = realm.people().get(i);
            if (i != person && q.nation() == p.nation() && p.nation() >= 0 && !vassals.contains(i) && !retinue.contains(i)
                    && level(realm, i) < level) {
                fighters.add(i);
            }
        }
        fighters.sort(Comparator.comparingInt((Integer i) -> realm.people().get(i).stat(Stat.PROWESS)).reversed().thenComparingInt(i -> i));
        for (int i : fighters) {
            if (retinue.size() >= size) {
                break;
            }
            retinue.add(i);
        }
        return retinue;
    }

    /**
     * A vassal's rank in the army, by their standing: a castle's lord is a Captain (enlisted, leading in the field); a
     * town's a Major, a city's a Colonel and a capital's lord a Marshal: officers, who sit in the war council's tent.
     */
    public static rafasconquest.character.Rank rank(Realm realm, int person) {
        return rafasconquest.character.Rank.of(Math.min(7, 4 + level(realm, person)));
    }

    /** The troops a character brings to war: by their standing, with all their vassals' (and theirs') besides. */
    public static int troops(Realm realm, int person) {
        return troops(realm, person, new HashSet<>());
    }

    private static int troops(Realm realm, int person, Set<Integer> counted) {
        if (!counted.add(person)) {
            return 0;
        }
        int total = TROOPS[level(realm, person)];
        for (int v : realm.vassals(person)) {
            total += troops(realm, v, counted);
        }
        return total;
    }

    /** What a player brings to a war beside themselves: their vassals' troops (and their vassals'), and their retinue. */
    public static int playerTroops(ServerPlayer player) {
        BaseMap map = ConquestRuntime.map();
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        int total = peer.retinue.size();
        if (map != null) {
            Set<Integer> counted = new HashSet<>();
            for (int v : peer.vassals) {
                if (v >= 0 && v < map.realm().people().size()) {
                    total += troops(map.realm(), v, counted);
                }
            }
        }
        return total;
    }

    /** A recruit hired by a player joins their retinue as a character, named in their nation's culture, while it has room. */
    @SubscribeEvent
    public static void onHired(RecruitEvent.Hired event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        Peerage peerage = Peerage.get(player.server);
        Peerage.Peer peer = peerage.of(player.getUUID());
        int room = size(level(peer.title));
        if (peer.retinue.size() >= room) {
            player.sendSystemMessage(Component.literal("Your retinue is full (" + room + "): this recruit serves you as a soldier."));
            return;
        }
        Entity recruit = event.getRecruit();
        String name = name(player, new Random(recruit.getUUID().getLeastSignificantBits()));
        recruit.setCustomName(Component.literal(name));
        recruit.setCustomNameVisible(true);
        peer.retinue.put(recruit.getUUID(), name);
        peerage.changed();
        player.sendSystemMessage(Component.literal(name + " joins your retinue (" + peer.retinue.size() + " of " + room + ")."));
    }

    /** A name in the culture of the nation the player serves (else the first culture). */
    private static String name(ServerPlayer player, Random random) {
        BaseMap map = ConquestRuntime.map();
        if (map == null || map.realm().cultures().isEmpty()) {
            return "Retainer";
        }
        int culture = 0;
        if (player.getTeam() instanceof PlayerTeam team) {
            for (int n = 0; n < map.nations().size(); n++) {
                if (map.nations().get(n).name().equals(team.getDisplayName().getString()) && n < map.realm().nationCulture().length) {
                    culture = map.realm().nationCulture()[n];
                }
            }
        }
        var c = map.realm().cultures().get(culture);
        boolean female = random.nextInt(4) == 0;
        return c.givenName(female, random) + " " + c.familyName(random);
    }

    /** A retinue keeps with its player: one fallen far behind (a teleport, a long ride) is brought to them. */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % EVERY != 0) {
            return;
        }
        Map<UUID, String> retinue = Peerage.get(player.server).of(player.getUUID()).retinue;
        if (retinue.isEmpty()) {
            return;
        }
        int i = 0;
        for (UUID id : retinue.keySet()) {
            for (var level : player.server.getAllLevels()) {
                Entity member = level.getEntity(id);
                if (member == null) {
                    continue;
                }
                if (member.level() != player.level() || member.distanceTo(player) > KEEP_UP) {
                    double angle = 2 * Math.PI * i / retinue.size();
                    member.teleportTo(player.serverLevel(), player.getX() + Math.cos(angle) * 2, player.getY(), player.getZ() + Math.sin(angle) * 2,
                            java.util.Set.of(), member.getYRot(), member.getXRot());
                }
                break;
            }
            i++;
        }
    }

    /** One of a retinue who dies leaves it. */
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide || event.getEntity().getServer() == null) {
            return;
        }
        Peerage peerage = Peerage.get(event.getEntity().getServer());
        for (Peerage.Peer peer : peerage.peers().values()) {
            if (peer.retinue.remove(event.getEntity().getUUID()) != null) {
                peerage.changed();
                return;
            }
        }
    }
}
