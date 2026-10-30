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
        CHANNEL.messageBuilder(Volleys.class, 7, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(Volleys::write)
                .decoder(Volleys::read)
                .consumerMainThread(ConquestNetwork::handleVolleys)
                .add();
        CHANNEL.messageBuilder(UnitsRequest.class, 8, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UnitsRequest::write)
                .decoder(UnitsRequest::read)
                .consumerMainThread(ConquestNetwork::handleUnitsRequest)
                .add();
        CHANNEL.messageBuilder(UnitsState.class, 9, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UnitsState::write)
                .decoder(UnitsState::read)
                .consumerMainThread(ConquestNetwork::handleUnitsState)
                .add();
        CHANNEL.messageBuilder(HornOrders.class, 10, NetworkDirection.PLAY_TO_SERVER)
                .encoder(HornOrders::write)
                .decoder(HornOrders::read)
                .consumerMainThread(ConquestNetwork::handleHornOrders)
                .add();
        CHANNEL.messageBuilder(FigureStrike.class, 11, NetworkDirection.PLAY_TO_SERVER)
                .encoder(FigureStrike::write)
                .decoder(FigureStrike::read)
                .consumerMainThread(ConquestNetwork::handleFigureStrike)
                .add();
    }

    // ---- the war horn: a player's units ----

    /** One of a player's units, as the war horn's screen shows it. */
    public record UnitInfo(int number, int soldiers, double x, double z, int mode, int shape, boolean shields, int rgb) {
    }

    /** Client → server: the war horn's screen wants the player's units. */
    public record UnitsRequest() {
        void write(FriendlyByteBuf buf) {
        }

        static UnitsRequest read(FriendlyByteBuf buf) {
            return new UnitsRequest();
        }
    }

    public static void requestUnits() {
        CHANNEL.sendToServer(new UnitsRequest());
    }

    private static void handleUnitsRequest(UnitsRequest msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new UnitsState(rafasconquest.bridge.RecruitsBattle.unitInfo(player)));
        }
    }

    /** Server → client: the player's units. */
    public record UnitsState(List<UnitInfo> units) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(units.size());
            for (UnitInfo u : units) {
                buf.writeVarInt(u.number());
                buf.writeVarInt(u.soldiers());
                buf.writeDouble(u.x());
                buf.writeDouble(u.z());
                buf.writeVarInt(u.mode());
                buf.writeVarInt(u.shape());
                buf.writeBoolean(u.shields());
                buf.writeInt(u.rgb());
            }
        }

        static UnitsState read(FriendlyByteBuf buf) {
            int n = buf.readVarInt();
            List<UnitInfo> units = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                units.add(new UnitInfo(buf.readVarInt(), buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readVarInt(),
                        buf.readVarInt(), buf.readBoolean(), buf.readInt()));
            }
            return new UnitsState(units);
        }
    }

    private static void handleUnitsState(UnitsState msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> rafasconquest.client.HornScreen.receive(msg.units()));
    }

    /** Client → server: the orders given in the war horn's screen, each to some units, with a place for a move. */
    public record HornOrders(List<int[]> units, int[] orders, double[] xs, double[] zs) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(orders.length);
            for (int i = 0; i < orders.length; i++) {
                buf.writeVarIntArray(units.get(i));
                buf.writeVarInt(orders[i]);
                buf.writeDouble(xs[i]);
                buf.writeDouble(zs[i]);
            }
        }

        static HornOrders read(FriendlyByteBuf buf) {
            int n = Math.min(buf.readVarInt(), 256);
            List<int[]> units = new ArrayList<>(n);
            int[] orders = new int[n];
            double[] xs = new double[n];
            double[] zs = new double[n];
            for (int i = 0; i < n; i++) {
                units.add(buf.readVarIntArray(64));
                orders[i] = buf.readVarInt();
                xs[i] = buf.readDouble();
                zs[i] = buf.readDouble();
            }
            return new HornOrders(units, orders, xs, zs);
        }
    }

    public static void sendHornOrders(HornOrders orders) {
        CHANNEL.sendToServer(orders);
    }

    private static void handleHornOrders(HornOrders msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) {
            return;
        }
        rafasconquest.bridge.RecruitsBattle.UnitOrder[] all = rafasconquest.bridge.RecruitsBattle.UnitOrder.values();
        List<int[]> units = new ArrayList<>();
        List<rafasconquest.bridge.RecruitsBattle.UnitOrder> orders = new ArrayList<>();
        List<net.minecraft.world.phys.Vec3> places = new ArrayList<>();
        for (int i = 0; i < msg.orders().length; i++) {
            if (msg.orders()[i] < 0 || msg.orders()[i] >= all.length) {
                continue;
            }
            units.add(msg.units().get(i));
            orders.add(all[msg.orders()[i]]);
            places.add(new net.minecraft.world.phys.Vec3(msg.xs()[i], player.getY(), msg.zs()[i]));
        }
        rafasconquest.bridge.RecruitsBattle.hornOrders(player, units, orders, places);
    }

    /** Client → server: the player's swing met a figure of formation {@code formation}, at (x, z). */
    public record FigureStrike(int formation, double x, double z) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(formation);
            buf.writeDouble(x);
            buf.writeDouble(z);
        }

        static FigureStrike read(FriendlyByteBuf buf) {
            return new FigureStrike(buf.readVarInt(), buf.readDouble(), buf.readDouble());
        }
    }

    public static void sendFigureStrike(int formation, double x, double z) {
        CHANNEL.sendToServer(new FigureStrike(formation, x, z));
    }

    private static void handleFigureStrike(FigureStrike msg, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null) {
            rafasconquest.bridge.RecruitsBattle.playerStrikesFigure(player, msg.formation(), msg.x(), msg.z());
        }
    }

    /** Tells an enlisted player where they should stand in their block (shown as a ring), or that they have no spot. */
    public static void sendBattleSpot(ServerPlayer player, boolean has, double x, double y, double z) {
        sendBattleSpot(player, has, x, y, z, false, 0, 0, 0);
    }

    /** The same, with a beacon beam at ({@code bx}, {@code by}, {@code bz}): where an order sends the player. */
    public static void sendBattleSpot(ServerPlayer player, boolean has, double x, double y, double z, boolean beacon, double bx,
                                      double by, double bz) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BattleSpot(has, x, y, z, beacon, bx, by, bz));
    }

    /** Server → client: where the player should stand in their block, if anywhere. */
    public record BattleSpot(boolean has, double x, double y, double z, boolean beacon, double bx, double by, double bz) {
        void write(FriendlyByteBuf buf) {
            buf.writeBoolean(has);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeBoolean(beacon);
            buf.writeDouble(bx);
            buf.writeDouble(by);
            buf.writeDouble(bz);
        }

        static BattleSpot read(FriendlyByteBuf buf) {
            return new BattleSpot(buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean(),
                    buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    private static void handleBattleSpot(BattleSpot msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> rafasconquest.client.BattleSpotRing.set(msg.has(), msg.x(), msg.y(), msg.z(), msg.beacon(), msg.bx(), msg.by(), msg.bz()));
    }

    /** Tells players of the arrows archers loosed as numbers this second, for drawing in flight. */
    public static void sendVolleys(List<ServerPlayer> players, Volleys volleys) {
        for (ServerPlayer player : players) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), volleys);
        }
    }

    /** Server → client: volleys, each from formation {@code from[i]} at formation {@code to[i]}, {@code shots[i]} arrows. */
    public record Volleys(int[] from, int[] to, int[] shots) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarIntArray(from);
            buf.writeVarIntArray(to);
            buf.writeVarIntArray(shots);
        }

        static Volleys read(FriendlyByteBuf buf) {
            return new Volleys(buf.readVarIntArray(), buf.readVarIntArray(), buf.readVarIntArray());
        }
    }

    private static void handleVolleys(Volleys msg, Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> rafasconquest.client.ArrowVolleys.receive(msg.from(), msg.to(), msg.shots()));
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
