package rafasconquest.bridge;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import rafasconquest.RafasConquest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Loads chunks by tickets, as a player's view does, and completes on the server thread once all are in. Asking for them
 * with getChunk or getChunkFuture on the server thread waits there till each is loaded: a siege's walls froze it for 12 s.
 */
public final class ChunkWait {
    private static final TicketType<ChunkPos> TICKET = TicketType.create(RafasConquest.MOD_ID + "_wait", Comparator.comparingLong(ChunkPos::toLong));

    private record Wait(ServerLevel level, LongArrayList all, LongArrayList chunks, CompletableFuture<Void> done) {
    }

    private static final List<Wait> waits = new ArrayList<>();

    private ChunkWait() {
    }

    /** Completes, on the server thread, once every chunk is fully loaded; holds them till then. Call on the server thread. */
    public static CompletableFuture<Void> load(ServerLevel level, LongCollection chunks) {
        LongArrayList pending = new LongArrayList(new LongOpenHashSet(chunks));
        for (long c : pending) {
            ChunkPos pos = new ChunkPos(c);
            level.getChunkSource().addRegionTicket(TICKET, pos, 0, pos);
        }
        Wait wait = new Wait(level, pending.clone(), pending, new CompletableFuture<>());
        waits.add(wait);
        tick(); // all may be in already
        return wait.done;
    }

    /** At each tick's end: completes the waits whose chunks are all in, and lets go of their tickets. */
    public static void tick() {
        for (int i = waits.size() - 1; i >= 0; i--) {
            Wait w = waits.get(i);
            LongArrayList left = w.chunks;
            while (!left.isEmpty()) {
                long c = left.getLong(left.size() - 1);
                if (w.level.getChunkSource().getChunkNow(ChunkPos.getX(c), ChunkPos.getZ(c)) == null) {
                    break;
                }
                left.removeLong(left.size() - 1);
            }
            if (left.isEmpty()) {
                waits.remove(i);
                w.done.complete(null); // its work runs now, while the tickets still hold the chunks
                for (long c : w.all) {
                    ChunkPos pos = new ChunkPos(c);
                    w.level.getChunkSource().removeRegionTicket(TICKET, pos, 0, pos);
                }
            }
        }
    }

    /** Forgets every wait, cancelling them: the server is stopping, and its tickets go with it. */
    public static void clear() {
        for (Wait w : waits) {
            w.done.cancel(false);
        }
        waits.clear();
    }
}
