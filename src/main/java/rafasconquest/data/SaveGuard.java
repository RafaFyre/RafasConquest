package rafasconquest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;
import rafasconquest.RafasConquest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The mod's saved data, read so that a file this version can't read is kept: the game would otherwise start it anew and
 * save the empty one over it. The unreadable file is copied beside it ({@code <name>.dat.broken-<time>}) first.
 */
public final class SaveGuard {
    private SaveGuard() {
    }

    /** The overworld's saved data {@code name}, loaded by {@code load}, else {@code fresh}. */
    public static <T extends SavedData> T get(MinecraftServer server, String name, Supplier<T> fresh, Function<CompoundTag, T> load) {
        return server.overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(fresh,
                (CompoundTag tag, HolderLookup.Provider registries) -> {
                    try {
                        return load.apply(tag);
                    } catch (RuntimeException e) {
                        Path file = server.getWorldPath(LevelResource.ROOT).resolve("data").resolve(name + ".dat");
                        Path kept = file.resolveSibling(name + ".dat.broken-" + System.currentTimeMillis());
                        try {
                            Files.copy(file, kept);
                            RafasConquest.LOGGER.error("Could not read {}: kept it as {} and started anew", file, kept.getFileName(), e);
                        } catch (IOException copy) {
                            RafasConquest.LOGGER.error("Could not read {}, nor keep a copy ({}): started anew", file, copy.toString(), e);
                        }
                        return fresh.get();
                    }
                }), name);
    }
}
