package rafasconquest.faction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class Faction {
    private final UUID id;
    private final String name;
    private final FactionType type;
    private final BlockPos origin;
    private final int radiusSteps;
    private int strength;
    private final Map<ResourceType, Integer> stockpile = new EnumMap<>(ResourceType.class);

    public Faction(UUID id, String name, FactionType type, BlockPos origin, int radiusSteps, int strength) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.origin = origin;
        this.radiusSteps = radiusSteps;
        this.strength = strength;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public FactionType type() {
        return type;
    }

    public BlockPos origin() {
        return origin;
    }

    public int radiusSteps() {
        return radiusSteps;
    }

    public int strength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int stock(ResourceType resource) {
        return stockpile.getOrDefault(resource, 0);
    }

    public Map<ResourceType, Integer> stockpile() {
        return stockpile;
    }

    /** Adds to the stockpile, saturating at Integer.MAX_VALUE. */
    public void addStock(ResourceType resource, long amount) {
        long total = (long) stock(resource) + amount;
        stockpile.put(resource, (int) Math.max(0, Math.min(Integer.MAX_VALUE, total)));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", id);
        tag.putString("name", name);
        tag.putString("type", type.name());
        tag.putLong("origin", origin.asLong());
        tag.putInt("radiusSteps", radiusSteps);
        tag.putInt("strength", strength);
        CompoundTag stock = new CompoundTag();
        stockpile.forEach((resource, amount) -> stock.putInt(resource.name(), amount));
        tag.put("stockpile", stock);
        return tag;
    }

    public static Faction load(CompoundTag tag) {
        Faction faction = new Faction(
                tag.getUUID("id"),
                tag.getString("name"),
                FactionType.valueOf(tag.getString("type")),
                BlockPos.of(tag.getLong("origin")),
                tag.getInt("radiusSteps"),
                tag.getInt("strength"));
        CompoundTag stock = tag.getCompound("stockpile");
        for (String key : stock.getAllKeys()) {
            faction.stockpile.put(ResourceType.valueOf(key), stock.getInt(key));
        }
        return faction;
    }
}
