package rafasconquest.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/** Which entities the client has been told wear a banner on their back, by entity id. Client thread only. */
public final class BackBanners {
    private static final Map<Integer, ItemStack> BANNERS = new HashMap<>();

    private BackBanners() {
    }

    public static void put(int entityId, ItemStack banner) {
        BANNERS.put(entityId, banner);
    }

    @Nullable
    public static ItemStack of(Entity entity) {
        return BANNERS.get(entity.getId());
    }

    public static void remove(int entityId) {
        BANNERS.remove(entityId);
    }

    public static void clear() {
        BANNERS.clear();
    }
}
