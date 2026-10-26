package rafasconquest.poi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Poi {
    private final BlockPos pos;
    private final PoiType type;
    @Nullable
    private UUID owner;
    private final int dailyOutput;
    private boolean generated;

    public Poi(BlockPos pos, PoiType type, @Nullable UUID owner, int dailyOutput, boolean generated) {
        this.pos = pos;
        this.type = type;
        this.owner = owner;
        this.dailyOutput = dailyOutput;
        this.generated = generated;
    }

    public BlockPos pos() {
        return pos;
    }

    public PoiType type() {
        return type;
    }

    @Nullable
    public UUID owner() {
        return owner;
    }

    /** Only battles change ownership; nothing calls this in Milestone 1. */
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public int dailyOutput() {
        return dailyOutput;
    }

    public boolean generated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("pos", pos.asLong());
        tag.putString("type", type.name());
        if (owner != null) {
            tag.putUUID("owner", owner);
        }
        tag.putInt("dailyOutput", dailyOutput);
        tag.putBoolean("generated", generated);
        return tag;
    }

    public static Poi load(CompoundTag tag) {
        return new Poi(
                BlockPos.of(tag.getLong("pos")),
                PoiType.valueOf(tag.getString("type")),
                tag.hasUUID("owner") ? tag.getUUID("owner") : null,
                tag.getInt("dailyOutput"),
                tag.getBoolean("generated"));
    }
}
