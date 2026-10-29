package rafasconquest.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import rafasconquest.character.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Each player's military rank, saved with the world; a player not listed is a Soldier. */
public final class Ranks extends SavedData {
    private static final String NAME = "rafasconquest_ranks";
    private final Map<UUID, Rank> ranks = new HashMap<>();

    public static Ranks get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(Ranks::load, Ranks::new, NAME);
    }

    public Rank of(UUID player) {
        return ranks.getOrDefault(player, Rank.SOLDIER);
    }

    public void set(UUID player, Rank rank) {
        ranks.put(player, rank);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag list = new CompoundTag();
        ranks.forEach((id, rank) -> list.putInt(id.toString(), rank.number()));
        tag.put("ranks", list);
        return tag;
    }

    private static Ranks load(CompoundTag tag) {
        Ranks data = new Ranks();
        CompoundTag list = tag.getCompound("ranks");
        for (String key : list.getAllKeys()) {
            data.ranks.put(UUID.fromString(key), Rank.of(list.getInt(key)));
        }
        return data;
    }
}
