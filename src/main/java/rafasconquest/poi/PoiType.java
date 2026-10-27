package rafasconquest.poi;

import org.jetbrains.annotations.Nullable;
import rafasconquest.faction.ResourceType;

public enum PoiType {
    CAPITAL(null),
    FARM(ResourceType.FOOD),
    /** A City-state's home city. Produces food only if cityDailyOutput is set above its default of 0. */
    CITY(ResourceType.FOOD);

    @Nullable
    private final ResourceType produces;

    PoiType(@Nullable ResourceType produces) {
        this.produces = produces;
    }

    @Nullable
    public ResourceType produces() {
        return produces;
    }
}
