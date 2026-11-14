package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/** The nations' politics ({@link Politics}), kept with the world. */
public final class PoliticsData extends SavedData {
    private static final String NAME = "rafasconquest_politics";

    final Politics politics;

    private PoliticsData(Politics politics) {
        this.politics = politics;
    }

    public static PoliticsData get(MinecraftServer server) {
        return get(server.overworld());
    }

    public static PoliticsData get(net.minecraft.server.level.ServerLevel overworld) {
        return rafasconquest.data.SaveGuard.get(overworld.getServer(), NAME, () -> new PoliticsData(new Politics()),
                tag -> new PoliticsData(Politics.load(tag.getCompound("politics"))));
    }

    public Politics politics() {
        return politics;
    }

    @Override
    public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        tag.put("politics", politics.save());
        return tag;
    }
}
