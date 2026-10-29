package rafasconquest.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import rafasconquest.character.BattleRole;
import rafasconquest.character.Job;
import rafasconquest.character.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Each player's military rank, job and role, saved with the world. A player not listed is a Soldier, in heavy
 * infantry, on the frontline.
 */
public final class Ranks extends SavedData {
    private static final String NAME = "rafasconquest_ranks";
    private final Map<UUID, Rank> ranks = new HashMap<>();
    private final Map<UUID, Job> jobs = new HashMap<>();
    private final Map<UUID, BattleRole> roles = new HashMap<>();

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

    public Job job(UUID player) {
        return jobs.getOrDefault(player, Job.HEAVY_INFANTRY);
    }

    public void setJob(UUID player, Job job) {
        jobs.put(player, job);
        setDirty();
    }

    public BattleRole role(UUID player) {
        return roles.getOrDefault(player, BattleRole.FRONTLINE);
    }

    public void setRole(UUID player, BattleRole role) {
        roles.put(player, role);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag list = new CompoundTag();
        ranks.forEach((id, rank) -> list.putInt(id.toString(), rank.number()));
        tag.put("ranks", list);
        CompoundTag jobList = new CompoundTag();
        jobs.forEach((id, job) -> jobList.putString(id.toString(), job.id()));
        tag.put("jobs", jobList);
        CompoundTag roleList = new CompoundTag();
        roles.forEach((id, role) -> roleList.putString(id.toString(), role.id()));
        tag.put("roles", roleList);
        return tag;
    }

    private static Ranks load(CompoundTag tag) {
        Ranks data = new Ranks();
        CompoundTag list = tag.getCompound("ranks");
        for (String key : list.getAllKeys()) {
            data.ranks.put(UUID.fromString(key), Rank.of(list.getInt(key)));
        }
        CompoundTag jobList = tag.getCompound("jobs");
        for (String key : jobList.getAllKeys()) {
            data.jobs.put(UUID.fromString(key), Job.byId(jobList.getString(key)));
        }
        CompoundTag roleList = tag.getCompound("roles");
        for (String key : roleList.getAllKeys()) {
            data.roles.put(UUID.fromString(key), BattleRole.byId(roleList.getString(key)));
        }
        return data;
    }
}
