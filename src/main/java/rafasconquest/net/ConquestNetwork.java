package rafasconquest.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import rafasconquest.RafasConquest;
import rafasconquest.client.ClientScreens;
import rafasconquest.sim.ConquestServer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** The mod's network channel: the Faction Info screen asks the server for faction data, and the server replies. */
public final class ConquestNetwork {
    private static final String VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    private ConquestNetwork() {
    }

    public static void register() {
        CHANNEL.messageBuilder(RequestFactionInfo.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder((msg, buf) -> {
                })
                .decoder(buf -> new RequestFactionInfo())
                .consumerMainThread(ConquestNetwork::handleRequest)
                .add();
        CHANNEL.messageBuilder(FactionInfoReply.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FactionInfoReply::write)
                .decoder(FactionInfoReply::read)
                .consumerMainThread(ConquestNetwork::handleReply)
                .add();
    }

    public static void requestFactionInfo() {
        CHANNEL.sendToServer(new RequestFactionInfo());
    }

    /** Client → server: send me the factions. */
    public record RequestFactionInfo() {
    }

    /** Server → client: the known factions, nearest first. */
    public record FactionInfoReply(List<FactionInfo> factions) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(factions.size());
            factions.forEach(f -> f.write(buf));
        }

        static FactionInfoReply read(FriendlyByteBuf buf) {
            int n = buf.readVarInt();
            List<FactionInfo> factions = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                factions.add(FactionInfo.read(buf));
            }
            return new FactionInfoReply(factions);
        }
    }

    private static void handleRequest(RequestFactionInfo msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        ConquestServer server = ConquestServer.get();
        if (player == null) {
            return;
        }
        List<FactionInfo> factions = server == null ? List.of() : FactionInfo.collect(server.data(), player.blockPosition());
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new FactionInfoReply(factions));
    }

    private static void handleReply(FactionInfoReply msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientScreens.receiveFactionInfo(msg.factions()));
    }
}
