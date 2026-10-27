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
import net.minecraft.world.item.ItemStack;
import rafasconquest.client.BackBanners;
import rafasconquest.client.ClientScreens;
import rafasconquest.sim.ConquestServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * The mod's network channel. The Faction Info screen and the world map ask the server for faction data; the map also
 * asks for the base map, which comes back compressed and in parts (a single packet to a client is limited to 1 MB).
 * Battles tell clients which soldiers wear a banner on their back.
 */
public final class ConquestNetwork {
    private static final String VERSION = "3";
    private static final int MAP_PART_BYTES = 256 * 1024;
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
        CHANNEL.messageBuilder(RequestBaseMap.class, 2, NetworkDirection.PLAY_TO_SERVER)
                .encoder((msg, buf) -> {
                })
                .decoder(buf -> new RequestBaseMap())
                .consumerMainThread(ConquestNetwork::handleMapRequest)
                .add();
        CHANNEL.messageBuilder(BaseMapPart.class, 3, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BaseMapPart::write)
                .decoder(BaseMapPart::read)
                .consumerMainThread(ConquestNetwork::handleMapPart)
                .add();
        CHANNEL.messageBuilder(BackBanner.class, 4, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BackBanner::write)
                .decoder(BackBanner::read)
                .consumerMainThread(ConquestNetwork::handleBackBanner)
                .add();
    }

    /** Tells a player that an entity they now see wears this banner on its back (a battle's general). */
    public static void sendBackBanner(ServerPlayer player, int entityId, ItemStack banner) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BackBanner(entityId, banner));
    }

    /** Server → client: entity {@code entityId} wears {@code banner} on its back. */
    public record BackBanner(int entityId, ItemStack banner) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(entityId);
            buf.writeItem(banner);
        }

        static BackBanner read(FriendlyByteBuf buf) {
            return new BackBanner(buf.readVarInt(), buf.readItem());
        }
    }

    private static void handleBackBanner(BackBanner msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BackBanners.put(msg.entityId(), msg.banner()));
    }

    public static void requestFactionInfo() {
        CHANNEL.sendToServer(new RequestFactionInfo());
    }

    public static void requestBaseMap() {
        CHANNEL.sendToServer(new RequestBaseMap());
    }

    /** Sends an encoded base map ({@link rafasconquest.map.BaseMap#encode}) to a player, in parts. */
    public static void sendBaseMap(ServerPlayer player, byte[] encoded) {
        int parts = Math.max(1, (encoded.length + MAP_PART_BYTES - 1) / MAP_PART_BYTES);
        for (int i = 0; i < parts; i++) {
            byte[] part = Arrays.copyOfRange(encoded, i * MAP_PART_BYTES, Math.min(encoded.length, (i + 1) * MAP_PART_BYTES));
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BaseMapPart(i, parts, part));
        }
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

    /** Client → server: send me the base map. */
    public record RequestBaseMap() {
    }

    /** Server → client: part {@code index} of {@code count} of the encoded base map. */
    public record BaseMapPart(int index, int count, byte[] bytes) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(index);
            buf.writeVarInt(count);
            buf.writeByteArray(bytes);
        }

        static BaseMapPart read(FriendlyByteBuf buf) {
            return new BaseMapPart(buf.readVarInt(), buf.readVarInt(), buf.readByteArray(MAP_PART_BYTES));
        }
    }

    private static void handleRequest(RequestFactionInfo msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        ConquestServer server = ConquestServer.get();
        if (player == null) {
            return;
        }
        List<FactionInfo> factions = server == null ? List.of()
                : FactionInfo.collect(server.data(), player.blockPosition(), server.overworld().getSeed());
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new FactionInfoReply(factions));
    }

    private static void handleReply(FactionInfoReply msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientScreens.receiveFactionInfo(msg.factions()));
    }

    private static void handleMapRequest(RequestBaseMap msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        ConquestServer server = ConquestServer.get();
        if (player != null && server != null) {
            server.mapStore().request(player);
        }
    }

    private static void handleMapPart(BaseMapPart msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientScreens.receiveBaseMapPart(msg.index(), msg.count(), msg.bytes()));
    }
}
