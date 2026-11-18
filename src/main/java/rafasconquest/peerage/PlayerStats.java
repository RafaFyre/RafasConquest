package rafasconquest.peerage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import rafasconquest.character.Rank;
import rafasconquest.data.Ranks;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * A player's stats, as the stats window (opened from the inventory) shows them, and their settings, kept with the
 * player (through death) and changed from that window's settings.
 */
public final class PlayerStats {
    /** Whether, each day of a battle, the player is brought to their place in the line (on unless turned off). */
    public static final String TELEPORT_AT_MUSTER = "teleportAtMuster";
    private static final String SETTINGS = "rafasconquest";

    /** The player-only attributes and weapon skills, each kept under its own name in the player's persisted tag. */
    public static final String[] ATTRIBUTES = {"Strength", "Fortitude", "Agility", "Intelligence", "Willpower", "Charisma"};
    public static final String[] WEAPONS = {"Light", "Medium", "Heavy"};

    /** Each row a label and its value; the attributes and weapon skills the same way; and the settings. */
    public record View(List<String[]> rows, List<String[]> attributes, List<String[]> weapons, boolean teleportAtMuster) {
    }

    private PlayerStats() {
    }

    public static View view(ServerPlayer player) {
        Peerage.Peer peer = Peerage.get(player.server).of(player.getUUID());
        Ranks ranks = Ranks.get(player.server);
        Rank rank = ranks.of(player.getUUID());
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Name", Names.player(player.getName().getString(), peer.title)});
        int[] level = level(settings(player).getInt(EXPERIENCE));
        rows.add(new String[]{"Level", level[0] + " (" + level[1] + " / " + level[2] + " to the next)"});
        rows.add(new String[]{"Serves", player.getTeam() instanceof PlayerTeam team ? team.getDisplayName().getString() : "No nation (not enlisted)"});
        rows.add(new String[]{"Rank", rank.title() + " (rank " + rank.number() + "), leads " + rank.leads()});
        int next = Advancement.toNextRank(rank, peer.militaryRenown);
        rows.add(new String[]{"Military renown", peer.militaryRenown + (next < 0 ? " (highest rank)" : " (" + next + " to the next rank)")});
        rows.add(new String[]{"Noble title", peer.title.noble() ? peer.title.title() : "None"});
        rows.add(new String[]{"Noble renown", peer.nobleRenown + (peer.title.noble() ? "" : " (knighthood at " + Advancement.KNIGHTHOOD + ")")});
        BaseMap map = ConquestRuntime.map();
        if (peer.hold >= 0 && map != null) {
            rows.add(new String[]{"Land", "County of " + map.names().get(peer.hold)});
            if (peer.liege >= 0 && peer.liege < map.realm().people().size()) {
                rows.add(new String[]{"Liege", map.realm().people().get(peer.liege).name()});
            }
        } else {
            rows.add(new String[]{"Land", "None"});
        }
        if (map != null && peer.persona >= 0 && peer.persona < map.realm().people().size()) {
            rafasconquest.people.Person was = map.realm().people().get(peer.persona);
            rows.add(new String[]{"In the place of", was.name()});
            rows.add(new String[]{"Traits", String.join(", ", was.traits().stream().map(rafasconquest.people.Personality::title).toList())});
        }
        rows.add(new String[]{"Job in battle", ranks.job(player.getUUID()).title()});
        if (map != null) {
            if (peer.hold < 0 && peer.liege >= 0 && peer.liege < map.realm().people().size()) {
                rows.add(new String[]{"Sworn to", map.realm().people().get(peer.liege).name()});
            }
            List<String> vassals = peer.vassals.stream().filter(v -> v >= 0 && v < map.realm().people().size())
                    .map(v -> map.realm().people().get(v).name() + " (" + Retinues.rank(map.realm(), v).title() + ", "
                            + peer.vassalJobs.getOrDefault(v, rafasconquest.character.Job.HEAVY_INFANTRY).title().toLowerCase() + ")").toList();
            rows.add(new String[]{"Vassals", vassals.isEmpty() ? "None" : vassals.size() + ": " + String.join(", ", vassals)});
        }
        rows.add(new String[]{"Retinue", peer.retinue.size() + " of " + Retinues.size(Retinues.level(peer.title))
                + (peer.retinue.isEmpty() ? "" : ": " + String.join(", ", peer.retinue.values()))});
        rows.add(new String[]{"Brings to war", Retinues.playerTroops(player) + " troops"});
        rows.add(new String[]{"Battle role", ranks.role(player.getUUID()).title()});
        rows.add(new String[]{"Quests under way", String.valueOf(peer.taken.size())});
        return new View(rows, scores(player, ATTRIBUTES), scores(player, WEAPONS), teleportAtMuster(player));
    }

    /** Each named score as a row; one never set is 0. */
    private static List<String[]> scores(ServerPlayer player, String[] names) {
        CompoundTag settings = settings(player);
        return java.util.Arrays.stream(names).map(n -> new String[]{n, String.valueOf(settings.getInt(n))}).toList();
    }

    /** One attribute or weapon skill ({@link #ATTRIBUTES}, {@link #WEAPONS}); 0 when never set. */
    public static int score(ServerPlayer player, String name) {
        return settings(player).getInt(name);
    }

    public static void setScore(ServerPlayer player, String name, int value) {
        edit(player, settings -> settings.putInt(name, value));
        if (name.equals("Strength") || name.equals("Fortitude")) {
            rafasconquest.net.ConquestNetwork.sendUnhindered(player);
        }
    }

    /** All experience ever gained toward the player's own level (Minecraft's XP feeds it; spending XP takes none away). */
    private static final String EXPERIENCE = "experience";

    public static void gainExperience(ServerPlayer player, int amount) {
        edit(player, settings -> settings.putInt(EXPERIENCE, settings.getInt(EXPERIENCE) + amount));
    }

    /** The level reached with this much experience, and the experience into it: {level, into, needed for the next}. */
    static int[] level(int experience) {
        int level = 0;
        // shortcut: Minecraft's own XP curve until the level system gets its own
        for (int need = toNext(0); experience >= need; need = toNext(level)) {
            experience -= need;
            level++;
        }
        return new int[]{level, experience, toNext(level)};
    }

    private static int toNext(int level) {
        return level >= 30 ? 112 + (level - 30) * 9 : level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }

    private static void edit(ServerPlayer player, java.util.function.Consumer<CompoundTag> change) {
        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag settings = persisted.getCompound(SETTINGS);
        change.accept(settings);
        persisted.put(SETTINGS, settings);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
    }

    public static boolean teleportAtMuster(ServerPlayer player) {
        CompoundTag settings = settings(player);
        return !settings.contains(TELEPORT_AT_MUSTER) || settings.getBoolean(TELEPORT_AT_MUSTER);
    }

    /** Changes a setting (only those the window offers). */
    public static void set(ServerPlayer player, String key, boolean value) {
        if (!key.equals(TELEPORT_AT_MUSTER)) {
            return;
        }
        edit(player, settings -> settings.putBoolean(key, value));
    }

    /** The player's settings, kept through death (Forge copies the persisted tag to the respawned player). */
    private static CompoundTag settings(ServerPlayer player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getCompound(SETTINGS);
    }
}
