package rafasconquest.net;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class BattleSnapshotGearTest {
    @Test
    void eachFormationGetsItsKitBackAndAKitIsSentOnce() {
        int[] levy = {5, 0x3C44AA, 6, 0x3C44AA, -1, -1, -1, -1, 12, -1, -1, -1, -1, -1};
        int[] archers = {5, 0x3C44AA, 6, 0x3C44AA, 7, -1, 8, -1, 40, -1, -1, -1, -1, -1};
        List<FormationSnapshot> formations = List.of(block(1), block(2), block(3), block(4));
        // Formations 1 and 3 share a kit (equal lists, not the same one); 4 has none.
        Map<Integer, int[]> gear = Map.of(1, levy, 2, archers, 3, levy.clone());
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        new ConquestNetwork.BattleSnapshot(formations, gear).write(buf);
        ConquestNetwork.BattleSnapshot back = ConquestNetwork.BattleSnapshot.read(buf);
        assertEquals(4, back.formations().size());
        assertArrayEquals(levy, back.gear().get(1));
        assertArrayEquals(archers, back.gear().get(2));
        assertSame(back.gear().get(1), back.gear().get(3), "one kit, sent once");
        assertFalse(back.gear().containsKey(4));
        assertEquals(0, buf.readableBytes());
    }

    @Test
    void siegeEnginesComeBackWhereTheyStandOrAmongTheirCrew() {
        FormationSnapshot.Engine tower = new FormationSnapshot.Engine(rafasconquest.battle.Machine.SIEGE_TOWER.ordinal(), 12.5, 70, -8.25, -1, 0, true);
        FormationSnapshot.Engine onagers = new FormationSnapshot.Engine(rafasconquest.battle.Machine.ONAGER.ordinal(), Double.NaN, 64, Double.NaN, 0, -1, false);
        List<FormationSnapshot> formations = List.of(block(1).withEngine(tower), block(2).withEngine(onagers), block(3));
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        new ConquestNetwork.BattleSnapshot(formations, Map.of()).write(buf);
        List<FormationSnapshot> back = ConquestNetwork.BattleSnapshot.read(buf).formations();
        assertEquals(tower, back.get(0).engine());
        assertEquals(onagers.kind(), back.get(1).engine().kind());
        org.junit.jupiter.api.Assertions.assertTrue(back.get(1).engine().withCrew(), "the onagers stand among their crew");
        assertEquals(null, back.get(2).engine());
        assertEquals(101, back.get(0).leaderId(), "what follows the engine still reads right");
        assertEquals(0, buf.readableBytes());
    }

    private static FormationSnapshot block(int id) {
        return new FormationSnapshot(id, FormationSnapshot.Kind.LEVY, 0x3C44AA, 0xFFFFFF, 20, 10.5, 64, -3.25, 0, -1, 4, 2,
                false, true, new int[]{100 + id, -1}, 100 + id, null);
    }
}
