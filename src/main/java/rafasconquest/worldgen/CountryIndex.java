package rafasconquest.worldgen;

import rafasconquest.core.Countryside;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** The world's countryside settlements, filed by the chunk each one's centre is in. Built once; read-only after. */
public final class CountryIndex {
    public static final CountryIndex EMPTY = new CountryIndex(List.of());

    private final List<Countryside.Settlement> all;
    private final Map<Long, Countryside.Settlement> byChunk = new HashMap<>();

    public CountryIndex(List<Countryside.Settlement> settlements) {
        this.all = List.copyOf(settlements);
        for (Countryside.Settlement s : all) {
            byChunk.put(key(s.x() >> 4, s.z() >> 4), s);
        }
    }

    public List<Countryside.Settlement> all() {
        return all;
    }

    public Optional<Countryside.Settlement> inChunk(int chunkX, int chunkZ) {
        return Optional.ofNullable(byChunk.get(key(chunkX, chunkZ)));
    }

    private static long key(int cx, int cz) {
        return ((long) cx << 32) ^ (cz & 0xffffffffL);
    }
}
