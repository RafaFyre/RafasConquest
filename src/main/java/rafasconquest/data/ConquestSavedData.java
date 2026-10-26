package rafasconquest.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.WorldSettings;
import rafasconquest.faction.Faction;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.LongSupplier;

/** All faction and POI state, attached to the overworld. Only ever touched on the server thread. */
public class ConquestSavedData extends SavedData {
    public static final String NAME = "rafasconquest_world";

    private final WorldSettings settings;
    private final Map<UUID, Faction> factions = new LinkedHashMap<>();
    private final Map<Long, Poi> pois = new LinkedHashMap<>();
    private long lastProcessedDay;
    private long lastSavedRealTime;
    private final Set<Long> precomputedRegions = new LinkedHashSet<>();

    // Rebuilt from the fields above; never saved.
    private final Map<Long, Map<PoiType, Long>> poiByChunk = new HashMap<>();
    private final Map<Long, UUID> factionByOrigin = new HashMap<>();
    private final Map<UUID, Integer> poiCountByOwner = new HashMap<>();

    private LongSupplier clock = System::currentTimeMillis;

    public ConquestSavedData(WorldSettings settings, long lastProcessedDay, long lastSavedRealTime) {
        this.settings = settings;
        this.lastProcessedDay = lastProcessedDay;
        this.lastSavedRealTime = lastSavedRealTime;
    }

    public static ConquestSavedData get(ServerLevel overworld, WorldSettings forNewWorld) {
        return overworld.getDataStorage().computeIfAbsent(
                ConquestSavedData::load,
                () -> new ConquestSavedData(forNewWorld, Math.floorDiv(overworld.getDayTime(), 24_000L), System.currentTimeMillis()),
                NAME);
    }

    public WorldSettings settings() {
        return settings;
    }

    public Collection<Faction> factions() {
        return factions.values();
    }

    @Nullable
    public Faction faction(@Nullable UUID id) {
        return id == null ? null : factions.get(id);
    }

    @Nullable
    public Faction factionAtOrigin(int x, int z) {
        UUID id = factionByOrigin.get(xzKey(x, z));
        return id == null ? null : factions.get(id);
    }

    public void addFaction(Faction faction) {
        factions.put(faction.id(), faction);
        factionByOrigin.put(xzKey(faction.origin().getX(), faction.origin().getZ()), faction.id());
    }

    public Collection<Poi> pois() {
        return pois.values();
    }

    @Nullable
    public Poi poiInChunk(ChunkPos chunk, PoiType type) {
        Map<PoiType, Long> byType = poiByChunk.get(chunk.toLong());
        Long key = byType == null ? null : byType.get(type);
        return key == null ? null : pois.get(key);
    }

    public boolean hasPoi(BlockPos pos) {
        return pois.containsKey(pos.asLong());
    }

    public void addPoi(Poi poi) {
        pois.put(poi.pos().asLong(), poi);
        poiByChunk.computeIfAbsent(new ChunkPos(poi.pos()).toLong(), k -> new EnumMap<>(PoiType.class)).put(poi.type(), poi.pos().asLong());
        if (poi.owner() != null) {
            poiCountByOwner.merge(poi.owner(), 1, Integer::sum);
        }
    }

    public int poiCount(UUID owner) {
        return poiCountByOwner.getOrDefault(owner, 0);
    }

    public long lastProcessedDay() {
        return lastProcessedDay;
    }

    public void setLastProcessedDay(long day) {
        this.lastProcessedDay = day;
    }

    public long lastSavedRealTime() {
        return lastSavedRealTime;
    }

    public boolean isRegionPrecomputed(int regionX, int regionZ) {
        return precomputedRegions.contains(xzKey(regionX, regionZ));
    }

    public void markRegionPrecomputed(int regionX, int regionZ) {
        precomputedRegions.add(xzKey(regionX, regionZ));
    }

    public Set<Long> precomputedRegions() {
        return precomputedRegions;
    }

    public static long xzKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    /** For tests. */
    void setClock(LongSupplier clock) {
        this.clock = clock;
    }

    /** Always save, so lastSavedRealTime is written on every save even when nothing else changed. */
    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        lastSavedRealTime = clock.getAsLong();
        tag.put("settings", settings.save());
        ListTag factionList = new ListTag();
        factions.values().forEach(f -> factionList.add(f.save()));
        tag.put("factions", factionList);
        ListTag poiList = new ListTag();
        pois.values().forEach(p -> poiList.add(p.save()));
        tag.put("pois", poiList);
        tag.putLong("lastProcessedDay", lastProcessedDay);
        tag.putLong("lastSavedRealTime", lastSavedRealTime);
        tag.put("precomputedRegions", new LongArrayTag(precomputedRegions.stream().mapToLong(Long::longValue).toArray()));
        return tag;
    }

    public static ConquestSavedData load(CompoundTag tag) {
        ConquestSavedData data = new ConquestSavedData(
                WorldSettings.load(tag.getCompound("settings")),
                tag.getLong("lastProcessedDay"),
                tag.getLong("lastSavedRealTime"));
        for (Tag t : tag.getList("factions", Tag.TAG_COMPOUND)) {
            data.addFaction(Faction.load((CompoundTag) t));
        }
        for (Tag t : tag.getList("pois", Tag.TAG_COMPOUND)) {
            data.addPoi(Poi.load((CompoundTag) t));
        }
        for (long key : tag.getLongArray("precomputedRegions")) {
            data.precomputedRegions.add(key);
        }
        return data;
    }
}
