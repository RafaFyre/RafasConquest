package rafasconquest.net;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;
import rafasconquest.core.WorldSettings;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FactionInfoTest {
    private static ConquestSavedData sample() {
        WorldSettings settings = new WorldSettings(1100, 4200, 7, 2600, 350, 9000, 900, 30, 10, 6,
                1, 3, 310, 1490, 6, 10, 3600, 4900, 21000, 12, 7000, 3900,
                3500, 450, 320, 790, 4);
        ConquestSavedData data = new ConquestSavedData(settings, 0, 0);
        Faction empire = new Faction(new UUID(1, 2), "Valdoria", FactionType.EMPIRE, new BlockPos(2500, 71, -30), 7, 4100);
        Faction city = new Faction(new UUID(3, 4), "Sararmere", FactionType.CITY_STATE, new BlockPos(100, 66, 100), 0, 540);
        data.addFaction(empire);
        data.addFaction(city);
        data.addPoi(new Poi(empire.origin(), PoiType.CAPITAL, empire.id(), 0, true));
        data.addPoi(new Poi(new BlockPos(3000, 64, 0), PoiType.FARM, empire.id(), 6, false));
        data.addPoi(new Poi(new BlockPos(2600, 64, 0), PoiType.FARM, empire.id(), 6, false));
        data.addPoi(new Poi(new BlockPos(-5000, 64, 0), PoiType.FARM, null, 6, false));
        data.addPoi(new Poi(city.origin(), PoiType.CITY, city.id(), 0, true));
        return data;
    }

    @Test
    void nearestFactionFirstWithItsStructuresByType() {
        List<FactionInfo> infos = FactionInfo.collect(sample(), BlockPos.ZERO, 42L);
        assertEquals(List.of("Sararmere", "Valdoria"), infos.stream().map(FactionInfo::name).toList());
        FactionInfo valdoria = infos.get(1);
        assertEquals(3, valdoria.structureCount());
        assertEquals(1, valdoria.structures().get(PoiType.CAPITAL).size());
        // Farms nearest the capital first; the unowned farm belongs to no one.
        assertEquals(List.of(new BlockPos(2600, 64, 0), new BlockPos(3000, 64, 0)), valdoria.structures().get(PoiType.FARM));
        assertEquals(1, infos.get(0).structureCount());
        assertTrue(infos.get(0).structures().containsKey(PoiType.CITY));
    }

    @Test
    void survivesTheNetworkRoundTrip() {
        for (FactionInfo info : FactionInfo.collect(sample(), BlockPos.ZERO, 42L)) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            info.write(buf);
            assertEquals(info, FactionInfo.read(buf));
            assertEquals(0, buf.readableBytes());
        }
    }
}
