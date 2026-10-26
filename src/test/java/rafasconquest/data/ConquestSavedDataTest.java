package rafasconquest.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import org.junit.jupiter.api.Test;
import rafasconquest.core.WorldSettings;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.faction.ResourceType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;
import rafasconquest.sim.EconomyTick;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConquestSavedDataTest {
    private static ConquestSavedData sample() {
        WorldSettings settings = new WorldSettings(1100, 4200, 7, 2600, 350, 9000, 900, 30, 10, 6,
                1, 3, 310, 1490, 6, 10, 3600, 4900, 21000, 12, 7000, 3900);
        ConquestSavedData data = new ConquestSavedData(settings, 42, 1_700_000_000_000L);
        Faction a = new Faction(new UUID(1, 2), "Valdoria", FactionType.EMPIRE, new BlockPos(2500, 71, -30), 2, 845);
        a.addStock(ResourceType.FOOD, 1234);
        Faction b = new Faction(new UUID(3, 4), "Korthmark", FactionType.EMPIRE, new BlockPos(-12_000, 64, 9000), 7, 4100);
        data.addFaction(a);
        data.addFaction(b);
        data.addPoi(new Poi(new BlockPos(2500, 71, -30), PoiType.CAPITAL, a.id(), 0, true));
        data.addPoi(new Poi(new BlockPos(2007, 66, 391), PoiType.FARM, a.id(), 6, false));
        data.addPoi(new Poi(new BlockPos(-6000, 80, 6000), PoiType.FARM, null, 6, true));
        data.markRegionPrecomputed(0, 0);
        data.markRegionPrecomputed(-3, 2);
        return data;
    }

    @Test
    void saveReloadRoundTripIsIdentical() {
        ConquestSavedData original = sample();
        original.setClock(() -> 1_800_000_000_000L);
        CompoundTag first = original.save(new CompoundTag());

        ConquestSavedData reloaded = ConquestSavedData.load(first);
        reloaded.setClock(() -> 1_800_000_000_000L);
        CompoundTag second = reloaded.save(new CompoundTag());

        assertEquals(first, second);
        assertEquals(original.settings(), reloaded.settings());
        assertEquals(42, reloaded.lastProcessedDay());
        assertEquals(1_800_000_000_000L, reloaded.lastSavedRealTime());
        assertEquals(original.precomputedRegions(), reloaded.precomputedRegions());

        Faction a = reloaded.faction(new UUID(1, 2));
        assertNotNull(a);
        assertEquals("Valdoria", a.name());
        assertEquals(new BlockPos(2500, 71, -30), a.origin());
        assertEquals(2, a.radiusSteps());
        assertEquals(845, a.strength());
        assertEquals(1234, a.stock(ResourceType.FOOD));

        Poi unowned = reloaded.poiInChunk(new ChunkPos(new BlockPos(-6000, 80, 6000)), PoiType.FARM);
        assertNotNull(unowned);
        assertNull(unowned.owner());
        assertEquals(true, unowned.generated());
        assertEquals(6, unowned.dailyOutput());
        assertEquals(2, reloaded.poiCount(a.id()));
    }

    @Test
    void saveWritesCurrentRealTime() {
        ConquestSavedData data = sample();
        data.setClock(() -> 123_456_789L);
        assertEquals(123_456_789L, data.save(new CompoundTag()).getLong("lastSavedRealTime"));
    }

    @Test
    void dailyTickPaysEachOwnedFarm() {
        ConquestSavedData data = sample();
        Faction a = data.faction(new UUID(1, 2));
        int before = a.stock(ResourceType.FOOD);
        for (int i = 0; i < 3; i++) {
            EconomyTick.runDay(data);
        }
        // One owned Farm at 6 food/day; the Capital produces nothing and the unowned Farm pays no one.
        assertEquals(before + 3 * 6 * 1, a.stock(ResourceType.FOOD));
        assertEquals(0, data.faction(new UUID(3, 4)).stock(ResourceType.FOOD));
    }
}
