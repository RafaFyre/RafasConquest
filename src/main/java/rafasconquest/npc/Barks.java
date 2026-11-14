package rafasconquest.npc;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import rafasconquest.data.Dialogue;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.sim.ConquestServer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * What a player overhears and who comes up to talk. Within {@link #EARSHOT} of a recruiter, noble or townsperson the
 * player hears a scene from {@code dialogue/barks.toml} above their hotbar; within {@link #CLOSE} of a recruiter or
 * noble, the full conversation ({@link rafasconquest.peerage.Talk}) opens, once each time the player comes up to them.
 */
public final class Barks {
    static final double EARSHOT = 14;
    static final double CLOSE = 2.5;
    /** People standing this near the speaker, of the same kind, take turns at the scene's lines. */
    private static final double TOGETHER = 6;
    private static final long GAP = 20 * 20;
    private static final long SPEAKER_GAP = 20 * 150;

    private static final class Ear {
        long next;
        final Map<Integer, Long> heard = new HashMap<>();
        final Set<Integer> met = new HashSet<>();
    }

    private static final Map<UUID, Ear> EARS = new HashMap<>();

    private Barks() {
    }

    public static void tick(MinecraftServer server) {
        long now = server.getTickCount();
        if (now % 20 != 0) {
            return;
        }
        players:
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Ear ear = EARS.computeIfAbsent(player.getUUID(), u -> new Ear());
            List<Mob> near = player.level().getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(EARSHOT),
                    e -> kind(e) != null && e.distanceTo(player) <= EARSHOT);
            near.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
            ear.met.removeIf(id -> near.stream().noneMatch(e -> e.getId() == id && e.distanceTo(player) < CLOSE * 3));
            for (Mob e : near) {
                if (e instanceof Recruiter r && r.distanceTo(player) <= CLOSE && ear.met.add(r.getId()) && !player.isSpectator()) {
                    rafasconquest.peerage.Talk.open(player, r);
                    continue players;
                }
            }
            if (now < ear.next || player.isSpectator()) {
                continue;
            }
            for (Mob e : near) {
                Long last = ear.heard.get(e.getId());
                if (e.distanceTo(player) <= CLOSE || last != null && now - last < SPEAKER_GAP) {
                    continue;
                }
                bark(player, e, near, ear, now);
                break;
            }
        }
    }

    private static void bark(ServerPlayer player, Mob speaker, List<Mob> near, Ear ear, long now) {
        String kind = kind(speaker);
        List<Mob> cast = new ArrayList<>(near.stream()
                .filter(e -> kind.equals(kind(e)) && e.distanceTo(speaker) <= TOGETHER).toList());
        cast.sort(Comparator.comparingDouble(e -> e.distanceToSqr(speaker)));
        String scene = Dialogue.say("barks." + kind, "nation", nationName(speaker));
        List<String> names = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (String line : scene.split("\\s*\\|\\s*")) {
            Mob who = cast.get(lines.size() % cast.size());
            int colon = line.indexOf(": ");
            boolean named = colon > 0 && colon <= 24;
            names.add(named ? line.substring(0, colon) : who.hasCustomName() ? who.getCustomName().getString() : "");
            lines.add(named ? line.substring(colon + 2) : line);
        }
        for (Mob e : cast) {
            ear.heard.put(e.getId(), now);
        }
        ear.next = now + GAP;
        ConquestNetwork.sendBark(player, names, lines);
    }

    /** Which barks someone says: a key in barks.toml, or null if they say none. */
    private static String kind(Mob e) {
        if (e instanceof Recruiter r) {
            return r.person() < 0 ? "recruiter" : "noble";
        }
        return null;
    }

    private static String nationName(Mob e) {
        ConquestServer server = ConquestServer.get();
        if (e instanceof Recruiter r && server != null && r.nation() != null && server.data().faction(r.nation()) != null) {
            return server.data().faction(r.nation()).name();
        }
        return Dialogue.say("common.no_nation");
    }

    public static void forget(UUID player) {
        EARS.remove(player);
    }
}
