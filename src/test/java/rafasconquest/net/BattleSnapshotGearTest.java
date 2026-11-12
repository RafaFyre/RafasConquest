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

    private static FormationSnapshot block(int id) {
        return new FormationSnapshot(id, FormationSnapshot.Kind.LEVY, 0x3C44AA, 0xFFFFFF, 20, 10.5, 64, -3.25, 0, -1, 4, 2,
                false, true, new int[]{100 + id, -1}, 100 + id, null);
    }
}
