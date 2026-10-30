package rafasconquest.peerage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The players' standing, saved with the world: each player's noble title and renown on both ladders, the county they
 * hold (if any) and their liege, the nobles who know them (whom they have done a quest for), the quests they have read
 * and taken, and their merits in battle; and which counties players hold, and what became of the characters they
 * replaced ("died", "dismissed").
 */
public final class Peerage extends SavedData {
    private static final String NAME = "rafasconquest_peerage";

    /** A merit in battle: a battle the player's side won, with them in it; spent when it earns them land. */
    public record Merit(String battle, int x, int z, String nation, List<String> officers) {
    }

    /** A quest taken, with how far along it is. */
    public static final class Taken {
        public final Quest quest;
        public int kills;
        public boolean reached;

        Taken(Quest quest) {
            this.quest = quest;
        }

        /** Whether it only wants reporting to its giver. */
        public boolean ready() {
            return switch (quest.type()) {
                case DELIVER -> reached;
                case SCOUT -> reached;
                case CLEAR -> kills >= quest.need();
            };
        }
    }

    /** A player's standing. */
    public static final class Peer {
        public String name = "";
        public NobleTitle title = NobleTitle.NONE;
        public int nobleRenown;
        public int militaryRenown;
        /** The county held (its seat province), and the character they serve as vassal (-1: none). */
        public int hold = -1;
        public int liege = -1;
        public final Set<Integer> known = new HashSet<>();
        /** The quests read on a board, not yet taken, by id. */
        public final Map<Long, Quest> read = new HashMap<>();
        public final List<Taken> taken = new ArrayList<>();
        public final List<Merit> merits = new ArrayList<>();

        public Taken taken(long id) {
            for (Taken t : taken) {
                if (t.quest.id() == id) {
                    return t;
                }
            }
            return null;
        }
    }

    private final Map<UUID, Peer> peers = new HashMap<>();
    private final Map<Integer, UUID> heldBy = new HashMap<>();
    private final Map<Integer, String> fates = new HashMap<>();

    public static Peerage get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(Peerage::load, Peerage::new, NAME);
    }

    public Peer of(UUID player) {
        return peers.computeIfAbsent(player, k -> new Peer());
    }

    public Map<UUID, Peer> peers() {
        return peers;
    }

    /** The player holding a county (by its seat province), or null. */
    public UUID heldBy(int province) {
        return heldBy.get(province);
    }

    /** What became of a county's character holder, if a player replaced them: "died", "dismissed", or null. */
    public String fate(int province) {
        return fates.get(province);
    }

    public Map<Integer, UUID> holdings() {
        return heldBy;
    }

    public Map<Integer, String> fates() {
        return fates;
    }

    /** A player takes a county; the character who held it (if any) is gone, as {@code fate} says. */
    public void grant(UUID player, int province, int liege, String fate) {
        Peer peer = of(player);
        peer.hold = province;
        peer.liege = liege;
        peer.title = NobleTitle.COUNT;
        heldBy.put(province, player);
        if (fate != null) {
            fates.put(province, fate);
        }
        setDirty();
    }

    /** Whether a hold's character has been replaced by a player, or is gone. */
    public boolean replaced(int province) {
        return heldBy.containsKey(province) || fates.containsKey(province);
    }

    public void changed() {
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag list = new CompoundTag();
        peers.forEach((id, p) -> {
            CompoundTag t = new CompoundTag();
            t.putString("name", p.name);
            t.putString("title", p.title.name());
            t.putInt("nobleRenown", p.nobleRenown);
            t.putInt("militaryRenown", p.militaryRenown);
            t.putInt("hold", p.hold);
            t.putInt("liege", p.liege);
            t.putIntArray("known", p.known.stream().mapToInt(Integer::intValue).toArray());
            ListTag read = new ListTag();
            p.read.values().forEach(q -> read.add(q.save()));
            t.put("read", read);
            ListTag taken = new ListTag();
            for (Taken q : p.taken) {
                CompoundTag qt = q.quest.save();
                qt.putInt("kills", q.kills);
                qt.putBoolean("reached", q.reached);
                taken.add(qt);
            }
            t.put("taken", taken);
            ListTag merits = new ListTag();
            for (Merit m : p.merits) {
                CompoundTag mt = new CompoundTag();
                mt.putString("battle", m.battle());
                mt.putInt("x", m.x());
                mt.putInt("z", m.z());
                mt.putString("nation", m.nation());
                mt.putString("officers", String.join("\n", m.officers()));
                merits.add(mt);
            }
            t.put("merits", merits);
            list.put(id.toString(), t);
        });
        tag.put("peers", list);
        CompoundTag held = new CompoundTag();
        heldBy.forEach((province, id) -> held.putUUID(Integer.toString(province), id));
        tag.put("heldBy", held);
        CompoundTag gone = new CompoundTag();
        fates.forEach((province, fate) -> gone.putString(Integer.toString(province), fate));
        tag.put("fates", gone);
        return tag;
    }

    private static Peerage load(CompoundTag tag) {
        Peerage data = new Peerage();
        CompoundTag list = tag.getCompound("peers");
        for (String key : list.getAllKeys()) {
            CompoundTag t = list.getCompound(key);
            Peer p = new Peer();
            p.name = t.getString("name");
            p.title = NobleTitle.valueOf(t.getString("title").isEmpty() ? "NONE" : t.getString("title"));
            p.nobleRenown = t.getInt("nobleRenown");
            p.militaryRenown = t.getInt("militaryRenown");
            p.hold = t.getInt("hold");
            p.liege = t.getInt("liege");
            for (int k : t.getIntArray("known")) {
                p.known.add(k);
            }
            for (Tag rt : t.getList("read", Tag.TAG_COMPOUND)) {
                Quest q = Quest.load((CompoundTag) rt);
                p.read.put(q.id(), q);
            }
            for (Tag qt : t.getList("taken", Tag.TAG_COMPOUND)) {
                Taken q = new Taken(Quest.load((CompoundTag) qt));
                q.kills = ((CompoundTag) qt).getInt("kills");
                q.reached = ((CompoundTag) qt).getBoolean("reached");
                p.taken.add(q);
            }
            for (Tag mt : t.getList("merits", Tag.TAG_COMPOUND)) {
                CompoundTag m = (CompoundTag) mt;
                String officers = m.getString("officers");
                p.merits.add(new Merit(m.getString("battle"), m.getInt("x"), m.getInt("z"), m.getString("nation"),
                        officers.isEmpty() ? List.of() : List.of(officers.split("\n"))));
            }
            data.peers.put(UUID.fromString(key), p);
        }
        CompoundTag held = tag.getCompound("heldBy");
        for (String key : held.getAllKeys()) {
            data.heldBy.put(Integer.parseInt(key), held.getUUID(key));
        }
        CompoundTag gone = tag.getCompound("fates");
        for (String key : gone.getAllKeys()) {
            data.fates.put(Integer.parseInt(key), gone.getString(key));
        }
        return data;
    }

    /** Taking a quest. */
    public Taken take(UUID player, Quest quest) {
        Peer peer = of(player);
        Taken t = new Taken(quest);
        peer.taken.add(t);
        setDirty();
        return t;
    }
}
