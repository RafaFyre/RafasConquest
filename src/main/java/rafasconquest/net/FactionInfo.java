package rafasconquest.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * What the Faction Info screen shows about one faction, as sent from the server: its stats, and every structure it
 * owns, by type. {@code origin} is the Empire's capital or the City-state's home city.
 */
public record FactionInfo(UUID id, String name, FactionType type, int radiusSteps, int strength, BlockPos origin,
                          Map<PoiType, List<BlockPos>> structures) {
    /** Most factions sent in one reply. Nearest first, so these are the ones a player can reach. */
    public static final int MAX_FACTIONS = 64;

    public int structureCount() {
        return structures.values().stream().mapToInt(List::size).sum();
    }

    /** Every known faction, nearest origin first, with the structures each owns (nearest to its origin first). */
    public static List<FactionInfo> collect(ConquestSavedData data, BlockPos near) {
        Map<UUID, Map<PoiType, List<BlockPos>>> owned = new java.util.HashMap<>();
        for (Poi poi : data.pois()) {
            if (poi.owner() != null) {
                owned.computeIfAbsent(poi.owner(), k -> new EnumMap<>(PoiType.class))
                        .computeIfAbsent(poi.type(), k -> new ArrayList<>()).add(poi.pos());
            }
        }
        List<FactionInfo> infos = new ArrayList<>();
        data.factions().stream()
                .sorted(Comparator.comparingDouble(f -> f.origin().distSqr(near)))
                .limit(MAX_FACTIONS)
                .forEach(f -> infos.add(of(f, owned.getOrDefault(f.id(), new EnumMap<>(PoiType.class)))));
        return infos;
    }

    private static FactionInfo of(Faction f, Map<PoiType, List<BlockPos>> structures) {
        structures.values().forEach(list -> list.sort(Comparator.comparingDouble(p -> p.distSqr(f.origin()))));
        return new FactionInfo(f.id(), f.name(), f.type(), f.radiusSteps(), f.strength(), f.origin(), structures);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeUtf(name);
        buf.writeEnum(type);
        buf.writeVarInt(radiusSteps);
        buf.writeVarInt(strength);
        buf.writeBlockPos(origin);
        buf.writeVarInt(structures.size());
        structures.forEach((poiType, positions) -> {
            buf.writeEnum(poiType);
            buf.writeVarInt(positions.size());
            positions.forEach(buf::writeBlockPos);
        });
    }

    public static FactionInfo read(FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        String name = buf.readUtf();
        FactionType type = buf.readEnum(FactionType.class);
        int radiusSteps = buf.readVarInt();
        int strength = buf.readVarInt();
        BlockPos origin = buf.readBlockPos();
        Map<PoiType, List<BlockPos>> structures = new EnumMap<>(PoiType.class);
        int types = buf.readVarInt();
        for (int i = 0; i < types; i++) {
            PoiType poiType = buf.readEnum(PoiType.class);
            int n = buf.readVarInt();
            List<BlockPos> positions = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                positions.add(buf.readBlockPos());
            }
            structures.put(poiType, positions);
        }
        return new FactionInfo(id, name, type, radiusSteps, strength, origin, structures);
    }
}
