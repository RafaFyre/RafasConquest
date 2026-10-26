package rafasconquest.poi;

import org.jetbrains.annotations.Nullable;
import rafasconquest.faction.ResourceType;

public enum PoiType {
    CAPITAL(null),
    FARM(ResourceType.FOOD);

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
