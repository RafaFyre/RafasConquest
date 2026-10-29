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
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.client.BackBanners;
import rafasconquest.client.DistantArmies;
import rafasconquest.client.ClientScreens;
import rafasconquest.sim.ConquestServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * The mod's network channel. The Faction Info screen and the world map ask the server for faction data; the map also
 * asks for the base map, which comes back compressed and in parts (a single packet to a client is limited to 1 MB).
 * Battles tell clients which soldiers wear a banner on their back, and where every formation stands so it can be drawn
 * beyond the range at which soldiers are seen.
 */
public final class ConquestNetwork {
    private static final String VERSION = "4";
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
        CHANNEL.messageBuilder(BattleSnapshot.class, 5, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BattleSnapshot::write)
                .decoder(BattleSnapshot::read)
                .consumerMainThread(ConquestNetwork::handleBattleSnapshot)
                .add();
        CHANNEL.messageBuilder(BattleSpot.class, 6, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BattleSpot::write)
                .decoder(BattleSpot::read)
                .consumerMainThread(ConquestNetwork::handleBattleSpot)
                .add();
    }

    /** Tells an enlisted player where they should stand in their block (shown as a ring), or that they have no spot. */
    public static void sendBattleSpot(ServerPlayer player, boolean has, double x, double y, double z) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BattleSpot(has, x, y, z));
    }

    /** Server → client: where the player should stand in their block, if anywhere. */
    public record BattleSpot(boolean has, double x, double y, double z) {
        void write(FriendlyByteBuf buf) {
            buf.writeBoolean(has);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
        }

        static BattleSpot read(FriendlyByteBuf buf) {
            return new BattleSpot(buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    private static void handleBattleSpot(BattleSpot msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> rafasconquest.client.BattleSpotRing.set(msg.has(), msg.x(), msg.y(), msg.z()));
    }

    /** Tells players where every formation of the battle stands now; an empty list means the battle is gone. */
    public static void sendBattleSnapshot(List<ServerPlayer> players, List<FormationSnapshot> formations) {
        BattleSnapshot msg = new BattleSnapshot(formations);
        for (ServerPlayer player : players) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    /** Server → client: every formation of the battle still standing. */
    public record BattleSnapshot(List<FormationSnapshot> formations) {
        private static final FormationSnapshot.Kind[] KINDS = FormationSnapshot.Kind.values();

        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(formations.size());
            for (FormationSnapshot f : formations) {
                buf.writeVarInt(f.id());
                buf.writeByte(f.kind().ordinal());
                buf.writeInt(f.rgb());
                buf.writeInt(f.mark());
                buf.writeVarInt(f.alive());
                buf.writeDouble(f.x());
                buf.writeFloat((float) f.y());
                buf.writeDouble(f.z());
                buf.writeFloat((float) f.facingX());
                buf.writeFloat((float) f.facingZ());
                buf.writeFloat((float) f.halfWidth());
                buf.writeFloat((float) f.halfDepth());
                buf.writeByte((f.fighting() ? 1 : 0) | (f.leader() ? 2 : 0));
                buf.writeVarIntArray(f.soldierIds());
                buf.writeVarInt(f.leaderId() + 1);
            }
        }

        static BattleSnapshot read(FriendlyByteBuf buf) {
            int n = buf.readVarInt();
            List<FormationSnapshot> formations = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                int id = buf.readVarInt();
                FormationSnapshot.Kind kind = KINDS[buf.readByte()];
                int rgb = buf.readInt();
                int mark = buf.readInt();
                int alive = buf.readVarInt();
                double x = buf.readDouble();
                double y = buf.readFloat();
                double z = buf.readDouble();
                double facingX = buf.readFloat();
                double facingZ = buf.readFloat();
                double halfWidth = buf.readFloat();
                double halfDepth = buf.readFloat();
                int flags = buf.readByte();
                formations.add(new FormationSnapshot(id, kind, rgb, mark, alive, x, y, z, facingX, facingZ, halfWidth, halfDepth,
                        (flags & 1) != 0, (flags & 2) != 0, buf.readVarIntArray(), buf.readVarInt() - 1));
            }
            return new BattleSnapshot(formations);
        }
    }

    private static void handleBattleSnapshot(BattleSnapshot msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DistantArmies.receive(msg.formations()));
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
