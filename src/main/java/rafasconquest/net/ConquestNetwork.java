package rafasconquest.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rafasconquest.net.SimpleChannel.NetworkDirection;
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
import java.util.Map;

/**
 * The mod's network channel. The Faction Info screen and the world map ask the server for faction data; the map also
 * asks for the base map, which comes back compressed and in parts (a single packet to a client is limited to 1 MB).
 * Battles tell clients which soldiers wear a banner on their back, and where every formation stands so it can be drawn
 * beyond the range at which soldiers are seen.
 */
public final class ConquestNetwork {
    private static final String VERSION = "4";
    private static final int MAP_PART_BYTES = 256 * 1024;
    private static final SimpleChannel CHANNEL = new SimpleChannel(VERSION);

    private ConquestNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        CHANNEL.register(event);
    }

    static {
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
        CHANNEL.messageBuilder(WorldStretch.class, 12, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(WorldStretch::write)
                .decoder(WorldStretch::read)
                .consumerMainThread(ConquestNetwork::handleWorldStretch)
                .add();
        CHANNEL.messageBuilder(TalkView.class, 13, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TalkView::write)
                .decoder(TalkView::read)
                .consumerMainThread(ConquestNetwork::handleTalkView)
                .add();
        CHANNEL.messageBuilder(TalkChoice.class, 14, NetworkDirection.PLAY_TO_SERVER)
                .encoder(TalkChoice::write)
                .decoder(TalkChoice::read)
                .consumerMainThread(ConquestNetwork::handleTalkChoice)
                .add();
        CHANNEL.messageBuilder(BoardView.class, 15, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BoardView::write)
                .decoder(BoardView::read)
                .consumerMainThread(ConquestNetwork::handleBoardView)
                .add();
        CHANNEL.messageBuilder(BoardAction.class, 16, NetworkDirection.PLAY_TO_SERVER)
                .encoder(BoardAction::write)
                .decoder(BoardAction::read)
                .consumerMainThread(ConquestNetwork::handleBoardAction)
                .add();
        CHANNEL.messageBuilder(PeerageView.class, 17, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PeerageView::write)
                .decoder(PeerageView::read)
                .consumerMainThread(ConquestNetwork::handlePeerageView)
                .add();
        CHANNEL.messageBuilder(PoliticsView.class, 18, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PoliticsView::write)
                .decoder(PoliticsView::read)
                .consumerMainThread(ConquestNetwork::handlePoliticsView)
                .add();
        CHANNEL.messageBuilder(TeleportTo.class, 19, NetworkDirection.PLAY_TO_SERVER)
                .encoder(TeleportTo::write)
                .decoder(TeleportTo::read)
                .consumerMainThread(ConquestNetwork::handleTeleportTo)
                .add();
        CHANNEL.messageBuilder(StatsRequest.class, 20, NetworkDirection.PLAY_TO_SERVER)
                .encoder((m, b) -> {
                })
                .decoder(b -> new StatsRequest())
                .consumerMainThread(ConquestNetwork::handleStatsRequest)
                .add();
        CHANNEL.messageBuilder(StatsView.class, 21, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(StatsView::write)
                .decoder(StatsView::read)
                .consumerMainThread(ConquestNetwork::handleStatsView)
                .add();
        CHANNEL.messageBuilder(SetSetting.class, 22, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetSetting::write)
                .decoder(SetSetting::read)
                .consumerMainThread(ConquestNetwork::handleSetSetting)
                .add();
        CHANNEL.messageBuilder(MarkerView.class, 23, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MarkerView::write)
                .decoder(MarkerView::read)
                .consumerMainThread(ConquestNetwork::handleMarkerView)
                .add();
        CHANNEL.messageBuilder(MarkerSet.class, 24, NetworkDirection.PLAY_TO_SERVER)
                .encoder((m, b) -> new MarkerView(m.pos(), m.spec()).write(b))
                .decoder(b -> { MarkerView v = MarkerView.read(b); return new MarkerSet(v.pos(), v.spec()); })
                .consumerMainThread(ConquestNetwork::handleMarkerSet)
                .add();
        CHANNEL.messageBuilder(GrandStrategyView.class, 25, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(GrandStrategyView::write)
                .decoder(GrandStrategyView::read)
                .consumerMainThread(ConquestNetwork::handleGrandStrategyView)
                .add();
        CHANNEL.messageBuilder(CityView.class, 26, NetworkDirection.PLAY_TO_SERVER)
                .encoder((m, b) -> b.writeInt(m.province()))
                .decoder(b -> new CityView(b.readInt()))
                .consumerMainThread(ConquestNetwork::handleCityView)
                .add();
        CHANNEL.messageBuilder(CityClose.class, 27, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> {
                })
                .decoder(b -> new CityClose())
                .consumerMainThread((m, c) -> rafasconquest.client.ClientScreens.closeCityManager())
                .add();
        CHANNEL.messageBuilder(Bark.class, 28, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> {
                    b.writeCollection(m.names(), FriendlyByteBuf::writeUtf);
                    b.writeCollection(m.lines(), FriendlyByteBuf::writeUtf);
                })
                .decoder(b -> new Bark(b.readList(FriendlyByteBuf::readUtf), b.readList(FriendlyByteBuf::readUtf)))
                .consumerMainThread((m, c) -> rafasconquest.client.ClientBarks.add(m.names(), m.lines()))
                .add();
        CHANNEL.messageBuilder(Place.class, 29, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> {
                    b.writeByte(m.music());
                    b.writeBoolean(m.city());
                })
                .decoder(b -> new Place(b.readByte(), b.readBoolean()))
                .consumerMainThread((m, c) -> rafasconquest.client.PlaceMusicClient.set(m.music(), m.city()))
                .add();
        CHANNEL.messageBuilder(SeaSwell.class, 30, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> b.writeBoolean(m.on()))
                .decoder(b -> new SeaSwell(b.readBoolean()))
                .consumerMainThread((m, c) -> rafasconquest.core.Swell.calm(!m.on()))
                .add();
        CHANNEL.messageBuilder(GateView.class, 31, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(GateView::write)
                .decoder(GateView::read)
                .consumerMainThread((m, c) -> rafasconquest.client.ClientScreens.openGate(m.pos(), m.left(), m.right()))
                .add();
        CHANNEL.messageBuilder(GateSet.class, 32, NetworkDirection.PLAY_TO_SERVER)
                .encoder((m, b) -> new GateView(m.pos(), m.left(), m.right()).write(b))
                .decoder(b -> { GateView v = GateView.read(b); return new GateSet(v.pos(), v.left(), v.right()); })
                .consumerMainThread(ConquestNetwork::handleGateSet)
                .add();
        CHANNEL.messageBuilder(CameraAt.class, 33, NetworkDirection.PLAY_TO_SERVER)
                .encoder((m, b) -> {
                    b.writeBoolean(m.has());
                    b.writeDouble(m.x());
                    b.writeDouble(m.z());
                })
                .decoder(b -> new CameraAt(b.readBoolean(), b.readDouble(), b.readDouble()))
                .consumerMainThread(ConquestNetwork::handleCameraAt)
                .add();
        CHANNEL.messageBuilder(OpenBattleScreen.class, 35, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> {
                })
                .decoder(b -> new OpenBattleScreen())
                .consumerMainThread((m, c) -> rafasconquest.client.BattleScreen.openForDeployment())
                .add();
        CHANNEL.messageBuilder(ChargeHits.class, 34, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((m, b) -> {
                    b.writeVarIntArray(m.cavalry());
                    b.writeVarIntArray(m.struck());
                })
                .decoder(b -> new ChargeHits(b.readVarIntArray(), b.readVarIntArray()))
                .consumerMainThread((m, context) -> rafasconquest.client.ThrownFigures.receive(m.cavalry(), m.struck()))
                .add();
    }

    /** Server → client: cavalry formation {@code cavalry[i]} tore into formation {@code struck[i]} this second. */
    public record ChargeHits(int[] cavalry, int[] struck) {
    }

    public static void sendChargeHits(List<ServerPlayer> players, ChargeHits hits) {
        for (ServerPlayer player : players) {
            CHANNEL.send(player, hits);
        }
    }

    /** Client → server: where the strategy view's camera is (has false: closed), to load the world about it. */
    public record CameraAt(boolean has, double x, double z) {
    }

    public static void cameraAt(boolean has, double x, double z) {
        CHANNEL.sendToServer(new CameraAt(has, x, z));
    }

    private static void handleCameraAt(CameraAt msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null) {
            rafasconquest.bridge.CameraView.set(player, msg.has() ? new net.minecraft.world.phys.Vec3(msg.x(), player.getY(), msg.z()) : null);
        }
    }

    /** Server → client: whether the sea swells (the seaSwell game rule), for the water drawn. */
    public record SeaSwell(boolean on) {
    }

    public static void sendSeaSwell(ServerPlayer player, boolean on) {
        CHANNEL.send(player, new SeaSwell(on));
    }

    /** Server → client: the music of the place the player stands in, and whether it is a city ({@link rafasconquest.PlaceMusic}). */
    public record Place(int music, boolean city) {
    }

    public static void sendPlace(ServerPlayer player, int music, boolean city) {
        CHANNEL.send(player, new Place(music, city));
    }

    /** Server → client: a scene overheard ({@link rafasconquest.npc.Barks}), each line with who says it. */
    public record Bark(List<String> names, List<String> lines) {
    }

    public static void sendBark(ServerPlayer player, List<String> names, List<String> lines) {
        CHANNEL.send(player, new Bark(names, lines));
    }

    // ---- talking, quest boards, and the players' land ----

    /**
     * Server → client: what is said, page by page (each page one speaker, {@link rafasconquest.peerage.Talk}, the war
     * council), and the player's choices on the last.
     */
    public record TalkView(List<rafasconquest.peerage.Talk.View> pages) {
        void write(FriendlyByteBuf buf) {
            buf.writeCollection(pages, (b, view) -> {
                b.writeVarInt(view.entity());
                b.writeUtf(view.name());
                b.writeUtf(view.title());
                b.writeInt(view.colour());
                b.writeInt(view.accent());
                b.writeCollection(view.lines(), FriendlyByteBuf::writeUtf);
                b.writeCollection(view.options(), (o, option) -> {
                    o.writeUtf(option.key());
                    o.writeUtf(option.label());
                });
            });
        }

        static TalkView read(FriendlyByteBuf buf) {
            return new TalkView(buf.readList(b -> new rafasconquest.peerage.Talk.View(b.readVarInt(), b.readUtf(), b.readUtf(), b.readInt(),
                    b.readInt(), b.readList(FriendlyByteBuf::readUtf), b.readList(o -> new rafasconquest.peerage.Talk.Option(o.readUtf(), o.readUtf())))));
        }
    }

    public static void sendTalk(ServerPlayer player, List<rafasconquest.peerage.Talk.View> pages) {
        if (!pages.isEmpty()) {
            CHANNEL.send(player, new TalkView(pages));
        }
    }

    private static void handleTalkView(TalkView msg, IPayloadContext context) {
        rafasconquest.client.ClientScreens.openTalk(msg.pages());
    }

    /** Client → server: the player's choice in a conversation. */
    public record TalkChoice(int entity, String key) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(entity);
            buf.writeUtf(key);
        }

        static TalkChoice read(FriendlyByteBuf buf) {
            return new TalkChoice(buf.readVarInt(), buf.readUtf());
        }
    }

    public static void choose(int entity, String key) {
        CHANNEL.sendToServer(new TalkChoice(entity, key));
    }

    private static void handleTalkChoice(TalkChoice msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null) {
            rafasconquest.peerage.Talk.choose(player, msg.entity(), msg.key());
        }
    }

    /** Server → client: a quest board's quests, as the player sees them ({@link rafasconquest.peerage.Boards}). */
    public record BoardView(rafasconquest.peerage.Boards.View view) {
        void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(view.board());
            buf.writeUtf(view.place());
            buf.writeCollection(view.entries(), (b, e) -> {
                b.writeLong(e.id());
                b.writeUtf(e.title());
                b.writeUtf(e.giver());
                b.writeInt(e.giverX());
                b.writeInt(e.giverZ());
                b.writeUtf(e.reward());
                b.writeUtf(e.state());
                b.writeBoolean(e.general());
            });
        }

        static BoardView read(FriendlyByteBuf buf) {
            return new BoardView(new rafasconquest.peerage.Boards.View(buf.readBlockPos(), buf.readUtf(), buf.readList(b ->
                    new rafasconquest.peerage.Boards.Entry(b.readLong(), b.readUtf(), b.readUtf(), b.readInt(), b.readInt(), b.readUtf(),
                            b.readUtf(), b.readBoolean()))));
        }
    }

    public static void sendBoard(ServerPlayer player, rafasconquest.peerage.Boards.View view) {
        CHANNEL.send(player, new BoardView(view));
    }

    private static void handleBoardView(BoardView msg, IPayloadContext context) {
        rafasconquest.client.ClientScreens.openBoard(msg.view());
    }

    /** Client → server: reading a quest on a board, or taking or reporting a general one there. */
    public record BoardAction(net.minecraft.core.BlockPos board, long quest, String action) {
        void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(board);
            buf.writeLong(quest);
            buf.writeUtf(action);
        }

        static BoardAction read(FriendlyByteBuf buf) {
            return new BoardAction(buf.readBlockPos(), buf.readLong(), buf.readUtf());
        }
    }

    public static void boardAction(net.minecraft.core.BlockPos board, long quest, String action) {
        CHANNEL.sendToServer(new BoardAction(board, quest, action));
    }

    private static void handleBoardAction(BoardAction msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null) {
            rafasconquest.peerage.Boards.act(player, msg.board(), msg.quest(), msg.action());
        }
    }

    /** Server → client: the counties players hold (seat province → "Count Steve"), and what befell those they replaced. */
    public record PeerageView(java.util.Map<Integer, String> holders, java.util.Map<Integer, String> fates) {
        void write(FriendlyByteBuf buf) {
            buf.writeMap(holders, (b, k) -> b.writeVarInt(k), (b, v) -> b.writeUtf(v));
            buf.writeMap(fates, (b, k) -> b.writeVarInt(k), (b, v) -> b.writeUtf(v));
        }

        static PeerageView read(FriendlyByteBuf buf) {
            return new PeerageView(buf.readMap(b -> b.readVarInt(), b -> b.readUtf()),
                    buf.readMap(b -> b.readVarInt(), b -> b.readUtf()));
        }
    }

    public static void sendPeerage(ServerPlayer player, java.util.Map<Integer, String> holders, java.util.Map<Integer, String> fates) {
        CHANNEL.send(player, new PeerageView(holders, fates));
    }

    private static void handlePeerageView(PeerageView msg, IPayloadContext context) {
        rafasconquest.client.ClientPeerage.set(msg.holders(), msg.fates());
    }

    /**
     * Server → client: the nations' politics: each war (attacker, defender, duchy? 1 : 0, the title, the county fought for
     * next, counties taken, counties in all, the battle site x and z), the armies in the field (nation, soldiers, x, z,
     * mustering? 1 : 0, which war, heading x, z), the alliances (pairs), the provinces conquered and by whom, the latest
     * news, and the feuds' fights called (duel? 1 : 0, x, z) with what each is (a line).
     */
    public record PoliticsView(java.util.List<int[]> wars, java.util.List<int[]> armies, java.util.List<int[]> alliances, java.util.Map<Integer, Integer> conquered,
                               java.util.List<String> news, java.util.List<int[]> clashes, java.util.List<String> clashNames,
                               byte[] succession) {
        void write(FriendlyByteBuf buf) {
            buf.writeCollection(wars, FriendlyByteBuf::writeVarIntArray);
            buf.writeCollection(armies, FriendlyByteBuf::writeVarIntArray);
            buf.writeCollection(alliances, FriendlyByteBuf::writeVarIntArray);
            buf.writeMap(conquered, FriendlyByteBuf::writeVarInt, FriendlyByteBuf::writeVarInt);
            buf.writeCollection(news, FriendlyByteBuf::writeUtf);
            buf.writeCollection(clashes, FriendlyByteBuf::writeVarIntArray);
            buf.writeCollection(clashNames, FriendlyByteBuf::writeUtf);
            buf.writeByteArray(succession);
        }

        static PoliticsView read(FriendlyByteBuf buf) {
            return new PoliticsView(buf.readList(FriendlyByteBuf::readVarIntArray), buf.readList(FriendlyByteBuf::readVarIntArray),
                    buf.readList(FriendlyByteBuf::readVarIntArray),
                    buf.readMap(FriendlyByteBuf::readVarInt, FriendlyByteBuf::readVarInt), buf.readList(FriendlyByteBuf::readUtf),
                    buf.readList(FriendlyByteBuf::readVarIntArray), buf.readList(FriendlyByteBuf::readUtf), buf.readByteArray());
        }
    }

    // ---- Gate Blocks ----

    /** Both ways: a Gate Block's door boxes (to the client, to edit; back to the server, to keep); null: no door. */
    public record GateView(net.minecraft.core.BlockPos pos, @org.jetbrains.annotations.Nullable int[] left, @org.jetbrains.annotations.Nullable int[] right) {
        void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            for (int[] box : new int[][]{left, right}) {
                buf.writeBoolean(box != null);
                if (box != null) {
                    for (int v : box) {
                        buf.writeVarInt(v);
                    }
                }
            }
        }

        static GateView read(FriendlyByteBuf buf) {
            net.minecraft.core.BlockPos pos = buf.readBlockPos();
            int[][] boxes = new int[2][];
            for (int i = 0; i < 2; i++) {
                if (buf.readBoolean()) {
                    boxes[i] = new int[6];
                    for (int k = 0; k < 6; k++) {
                        boxes[i][k] = buf.readVarInt();
                    }
                }
            }
            return new GateView(pos, boxes[0], boxes[1]);
        }
    }

    public static void openGate(ServerPlayer player, net.minecraft.core.BlockPos pos, int[] left, int[] right) {
        CHANNEL.send(player, new GateView(pos, left, right));
    }

    public static void setGate(net.minecraft.core.BlockPos pos, int[] left, int[] right) {
        CHANNEL.sendToServer(new GateSet(pos, left, right));
    }

    /** Client → server: a Gate Block's new door boxes (creative, within reach). */
    public record GateSet(net.minecraft.core.BlockPos pos, int[] left, int[] right) {
    }

    private static void handleGateSet(GateSet msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null && player.isCreative() && player.blockPosition().closerThan(msg.pos(), 16)
                && player.level().getBlockEntity(msg.pos()) instanceof rafasconquest.content.GateBlockEntity gate) {
            String why = rafasconquest.bridge.SableLink.saveGate(gate, msg.left(), msg.right());
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(why));
        }
    }

    // ---- Character Markers ----

    /** Both ways: a Character Marker's settings (to the client, to edit; back to the server, to keep). */
    public record MarkerView(net.minecraft.core.BlockPos pos, rafasconquest.style.MarkerSpec spec) {
        void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeNbt(spec.save());
        }

        static MarkerView read(FriendlyByteBuf buf) {
            net.minecraft.core.BlockPos pos = buf.readBlockPos();
            net.minecraft.nbt.CompoundTag tag = buf.readNbt();
            return new MarkerView(pos, tag == null ? rafasconquest.style.MarkerSpec.DEFAULT : rafasconquest.style.MarkerSpec.load(tag));
        }
    }

    public static void openMarker(ServerPlayer player, net.minecraft.core.BlockPos pos, rafasconquest.style.MarkerSpec spec) {
        CHANNEL.send(player, new MarkerView(pos, spec));
    }

    public static void setMarker(net.minecraft.core.BlockPos pos, rafasconquest.style.MarkerSpec spec) {
        CHANNEL.sendToServer(new MarkerSet(pos, spec));
    }

    private static void handleMarkerView(MarkerView msg, IPayloadContext context) {
        rafasconquest.client.ClientScreens.openMarker(msg.pos(), msg.spec());
    }

    /** Client → server: a Character Marker's settings, to keep (creative, within reach). */
    public record MarkerSet(net.minecraft.core.BlockPos pos, rafasconquest.style.MarkerSpec spec) {
    }

    private static void handleMarkerSet(MarkerSet msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null && player.isCreative() && player.blockPosition().closerThan(msg.pos(), 16)
                && player.level().getBlockEntity(msg.pos()) instanceof rafasconquest.style.MarkerBlockEntity marker) {
            marker.setSpec(msg.spec());
            rafasconquest.style.MarkerBlock.LAST.put(player.getUUID(), msg.spec());
        }
    }

    /** Server → client: the player's fief, for the grand strategy screen ({@link rafasconquest.peerage.GrandStrategy}). */
    public record GrandStrategyView(rafasconquest.peerage.GrandStrategy.View view) {
        void write(FriendlyByteBuf buf) {
            buf.writeInt(view.seat());
            buf.writeInt(view.nation());
            buf.writeLong(view.day());
        }

        static GrandStrategyView read(FriendlyByteBuf buf) {
            return new GrandStrategyView(new rafasconquest.peerage.GrandStrategy.View(buf.readInt(), buf.readInt(), buf.readLong()));
        }
    }

    public static void sendGrandStrategy(ServerPlayer player, rafasconquest.peerage.GrandStrategy.View view) {
        CHANNEL.send(player, new GrandStrategyView(view));
    }

    private static void handleGrandStrategyView(GrandStrategyView msg, IPayloadContext context) {
        rafasconquest.client.ClientScreens.openGrandStrategy(msg.view());
    }

    /**
     * Client → server: the city manager. A province (≥ 0) opens it on the city seating it; {@link #CITY_LEAVE} leaves it
     * for the game, {@link #CITY_BACK} back to the fief menu.
     */
    public record CityView(int province) {
    }

    public static final int CITY_LEAVE = -1;
    public static final int CITY_BACK = -2;

    /** Server → client: shut the city manager (a battle muster called the player away). */
    public record CityClose() {
    }

    public static void closeCityView(ServerPlayer player) {
        CHANNEL.send(player, new CityClose());
    }

    public static void cityView(int province) {
        CHANNEL.sendToServer(new CityView(province));
    }

    private static void handleCityView(CityView msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player == null) {
            return;
        }
        if (msg.province() >= 0) {
            rafasconquest.peerage.CityManager.open(player, msg.province());
        } else {
            rafasconquest.peerage.CityManager.close(player);
            if (msg.province() == CITY_BACK) {
                rafasconquest.peerage.GrandStrategy.open(player);
            }
        }
    }

    // ---- the player's stats and settings (from the inventory) ----

    /** Client → server: show me my stats. */
    public record StatsRequest() {
    }

    public static void requestStats() {
        CHANNEL.sendToServer(new StatsRequest());
    }

    private static void handleStatsRequest(StatsRequest msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null) {
            CHANNEL.send(player, new StatsView(rafasconquest.peerage.PlayerStats.view(player)));
        }
    }

    /** Server → client: the player's stats and settings ({@link rafasconquest.peerage.PlayerStats}). */
    public record StatsView(rafasconquest.peerage.PlayerStats.View view) {
        void write(FriendlyByteBuf buf) {
            buf.writeCollection(view.rows(), (b, row) -> {
                b.writeUtf(row[0]);
                b.writeUtf(row[1]);
            });
            buf.writeBoolean(view.teleportAtMuster());
        }

        static StatsView read(FriendlyByteBuf buf) {
            return new StatsView(new rafasconquest.peerage.PlayerStats.View(buf.readList(b -> new String[]{b.readUtf(), b.readUtf()}),
                    buf.readBoolean()));
        }
    }

    private static void handleStatsView(StatsView msg, IPayloadContext context) {
        rafasconquest.client.ClientScreens.openStats(msg.view());
    }

    /** The {@link SetSetting} key that turns the world's sea swell on or off. */
    public static final String SEA_SWELL = "seaSwell";

    /** Client → server: change one of my settings (or, for its host or an op, the world's {@link #SEA_SWELL}). */
    public record SetSetting(String key, boolean value) {
        void write(FriendlyByteBuf buf) {
            buf.writeUtf(key);
            buf.writeBoolean(value);
        }

        static SetSetting read(FriendlyByteBuf buf) {
            return new SetSetting(buf.readUtf(64), buf.readBoolean());
        }
    }

    public static void setSetting(String key, boolean value) {
        CHANNEL.sendToServer(new SetSetting(key, value));
    }

    private static void handleSetSetting(SetSetting msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player == null) {
            return;
        }
        if (msg.key().equals(SEA_SWELL)) {
            // The world's, not the player's: only its host or an op changes it.
            if (player.hasPermissions(2) || player.server.isSingleplayerOwner(player.getGameProfile())) {
                player.server.getGameRules().getRule(rafasconquest.ConquestGameRules.SEA_SWELL).set(msg.value(), player.server);
            }
            return;
        }
        rafasconquest.peerage.PlayerStats.set(player, msg.key(), msg.value());
    }

    /** Client → server: teleport me to the ground at (x, z), from the world map's menu. Creative mode only. */
    public record TeleportTo(int x, int z) {
        void write(FriendlyByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(z);
        }

        static TeleportTo read(FriendlyByteBuf buf) {
            return new TeleportTo(buf.readInt(), buf.readInt());
        }
    }

    public static void teleport(int x, int z) {
        CHANNEL.sendToServer(new TeleportTo(x, z));
    }

    private static void handleTeleportTo(TeleportTo msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player == null || !player.isCreative()) {
            return; // only in creative
        }
        net.minecraft.server.level.ServerLevel level = player.server.overworld();
        // Onto the top block, trees and roofs included.
        rafasconquest.politics.Wars.toSurface(player, level, msg.x(), msg.z(), net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING);
    }

    public static void sendPolitics(ServerPlayer player, PoliticsView view) {
        CHANNEL.send(player, view);
    }

    private static void handlePoliticsView(PoliticsView msg, IPayloadContext context) {
        rafasconquest.client.ClientPolitics.set(msg);
    }

    /** Server → client: how far the world is stretched (1: not at all), for the client's far-terrain quality. */
    public record WorldStretch(int factor) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(factor);
        }

        static WorldStretch read(FriendlyByteBuf buf) {
            return new WorldStretch(buf.readVarInt());
        }
    }

    public static void sendWorldStretch(ServerPlayer player, int factor) {
        CHANNEL.send(player, new WorldStretch(factor));
    }

    private static void handleWorldStretch(WorldStretch msg, IPayloadContext context) {
        rafasconquest.client.DistantHorizonsLink.worldStretch(msg.factor());
    }

    // ---- the war horn: a player's units ----

    /**
     * One of a player's units (a block's worth), as the battle screen shows it: how many stand of how many it had, where
     * its middle is, its mode ({@code RecruitsBattle.UnitMode}), its card's formation, id and whether that card split off
     * another, its wing, its arm ({@code BattlePlan.Arm}), and whether it is fighting, or tired; its soldiers (entity ids).
     */
    public record UnitInfo(int number, int soldiers, int started, double x, double z, int mode, int shape, boolean shields, int rgb,
                           int card, boolean split, int wing, int arm, boolean fighting, boolean tired, int[] ids, int block,
                           @org.jetbrains.annotations.Nullable rafasconquest.battle.FieldBattle.Morale morale) {
    }

    /** Server → client: an officer's deployment begins; open the strategy view with their whole army selected. */
    public record OpenBattleScreen() {
    }

    public static void openBattleScreen(ServerPlayer player) {
        CHANNEL.send(player, new OpenBattleScreen());
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

    private static void handleUnitsRequest(UnitsRequest msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        if (player != null) {
            CHANNEL.send(player, new UnitsState(rafasconquest.bridge.RecruitsBattle.unitInfo(player)));
        }
    }

    /** Server → client: the player's units. */
    public record UnitsState(List<UnitInfo> units) {
        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(units.size());
            for (UnitInfo u : units) {
                buf.writeVarInt(u.number());
                buf.writeVarInt(u.soldiers());
                buf.writeVarInt(u.started());
                buf.writeDouble(u.x());
                buf.writeDouble(u.z());
                buf.writeVarInt(u.mode());
                buf.writeVarInt(u.shape());
                buf.writeBoolean(u.shields());
                buf.writeInt(u.rgb());
                buf.writeVarInt(u.card());
                buf.writeBoolean(u.split());
                buf.writeVarInt(u.wing());
                buf.writeVarInt(u.arm());
                buf.writeBoolean(u.fighting());
                buf.writeBoolean(u.tired());
                buf.writeVarIntArray(u.ids());
                buf.writeVarInt(u.block());
                writeMorale(buf, u.morale());
            }
        }

        static UnitsState read(FriendlyByteBuf buf) {
            int n = buf.readVarInt();
            List<UnitInfo> units = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                units.add(new UnitInfo(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readDouble(), buf.readDouble(),
                        buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readInt(), buf.readVarInt(), buf.readBoolean(),
                        buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readBoolean(), buf.readVarIntArray(256), buf.readVarInt(), readMorale(buf)));
            }
            return new UnitsState(units);
        }

        private static void writeMorale(FriendlyByteBuf buf, @org.jetbrains.annotations.Nullable rafasconquest.battle.FieldBattle.Morale m) {
            buf.writeBoolean(m != null);
            if (m != null) {
                for (double v : new double[]{m.losses(), m.captain(), m.flanked(), m.rear(), m.fright(), m.army(), m.breaksAt()}) {
                    buf.writeFloat((float) v);
                }
                buf.writeEnum(m.state());
            }
        }

        @org.jetbrains.annotations.Nullable
        private static rafasconquest.battle.FieldBattle.Morale readMorale(FriendlyByteBuf buf) {
            if (!buf.readBoolean()) {
                return null;
            }
            return new rafasconquest.battle.FieldBattle.Morale(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readEnum(rafasconquest.battle.FieldBattle.Morale.State.class));
        }
    }

    private static void handleUnitsState(UnitsState msg, IPayloadContext context) {
        rafasconquest.client.BattleScreen.receive(msg.units());
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
                units.add(buf.readVarIntArray(1024)); // a Grand Marshal's 10,000 are 500 units
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

    private static void handleHornOrders(HornOrders msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
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

    private static void handleFigureStrike(FigureStrike msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
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
        CHANNEL.send(player, new BattleSpot(has, x, y, z, beacon, bx, by, bz));
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

    private static void handleBattleSpot(BattleSpot msg, IPayloadContext context) {
        rafasconquest.client.BattleSpotRing.set(msg.has(), msg.x(), msg.y(), msg.z(), msg.beacon(), msg.bx(), msg.by(), msg.bz());
    }

    /** Tells players of the arrows archers loosed as numbers this second, for drawing in flight. */
    public static void sendVolleys(List<ServerPlayer> players, Volleys volleys) {
        for (ServerPlayer player : players) {
            CHANNEL.send(player, volleys);
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

    private static void handleVolleys(Volleys msg, IPayloadContext context) {
        rafasconquest.client.ArrowVolleys.receive(msg.from(), msg.to(), msg.shots());
    }

    /** Tells players where every formation of the battle stands now; an empty list means the battle is gone. */
    public static void sendBattleSnapshot(List<ServerPlayer> players, List<FormationSnapshot> formations) {
        sendBattleSnapshot(players, formations, Map.of());
    }

    /** As above, with what each formation's soldiers wear ({@link BattleSnapshot#gear}), by formation id. */
    public static void sendBattleSnapshot(List<ServerPlayer> players, List<FormationSnapshot> formations, Map<Integer, int[]> gear) {
        BattleSnapshot msg = new BattleSnapshot(formations, gear);
        for (ServerPlayer player : players) {
            CHANNEL.send(player, msg);
        }
    }

    /**
     * Server → client: every formation of the battle still standing, and what its soldiers wear: by formation id, for
     * each of head, chest, legs, feet, main hand, off hand and horse, the item's registry id (-1 for nothing) and its
     * tint (-1 for none; see {@code GearColours}). Only a few kits differ, so each is sent once and formations name it.
     */
    public record BattleSnapshot(List<FormationSnapshot> formations, Map<Integer, int[]> gear) {
        private static final FormationSnapshot.Kind[] KINDS = FormationSnapshot.Kind.values();

        void write(FriendlyByteBuf buf) {
            writeFormations(buf);
            List<int[]> kits = new ArrayList<>();
            int[] kitOf = new int[formations.size()];
            for (int i = 0; i < kitOf.length; i++) {
                int[] g = gear.get(formations.get(i).id());
                if (g == null) {
                    kitOf[i] = -1;
                    continue;
                }
                int k = 0;
                while (k < kits.size() && !java.util.Arrays.equals(kits.get(k), g)) {
                    k++;
                }
                if (k == kits.size()) {
                    kits.add(g);
                }
                kitOf[i] = k;
            }
            buf.writeVarInt(kits.size());
            for (int[] kit : kits) {
                buf.writeVarInt(kit.length);
                for (int v : kit) {
                    buf.writeVarInt(v + 1); // -1 (nothing) as 0
                }
            }
            for (int k : kitOf) {
                buf.writeVarInt(k + 1);
            }
        }

        private void writeFormations(FriendlyByteBuf buf) {
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
                FormationSnapshot.Ship ship = f.ship();
                buf.writeByte((f.fighting() ? 1 : 0) | (f.leader() ? 2 : 0) | (ship != null ? 4 : 0) | (ship != null && ship.cargo() ? 8 : 0));
                buf.writeVarIntArray(f.soldierIds());
                buf.writeVarInt(f.leaderId() + 1);
                if (ship != null) {
                    buf.writeByte(ship.sail());
                    buf.writeByte(ship.stripe());
                }
            }
        }

        /** A formation's ship, after the rest of it; null if its flags say it is ashore. */
        @org.jetbrains.annotations.Nullable
        private static FormationSnapshot.Ship readShip(FriendlyByteBuf buf, int flags) {
            return (flags & 4) == 0 ? null : new FormationSnapshot.Ship(buf.readByte(), buf.readByte(), (flags & 8) != 0);
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
                        (flags & 1) != 0, (flags & 2) != 0, buf.readVarIntArray(), buf.readVarInt() - 1, readShip(buf, flags)));
            }
            int[][] kits = new int[buf.readVarInt()][];
            for (int k = 0; k < kits.length; k++) {
                kits[k] = new int[buf.readVarInt()];
                for (int j = 0; j < kits[k].length; j++) {
                    kits[k][j] = buf.readVarInt() - 1;
                }
            }
            Map<Integer, int[]> gear = new java.util.HashMap<>();
            for (FormationSnapshot f : formations) {
                int k = buf.readVarInt() - 1;
                if (k >= 0) {
                    gear.put(f.id(), kits[k]);
                }
            }
            return new BattleSnapshot(formations, gear);
        }
    }

    private static void handleBattleSnapshot(BattleSnapshot msg, IPayloadContext context) {
        DistantArmies.receive(msg.formations(), msg.gear());
    }

    /** Tells a player that an entity they now see wears this banner on its back (a battle's general). */
    public static void sendBackBanner(ServerPlayer player, int entityId, ItemStack banner) {
        CHANNEL.send(player, new BackBanner(entityId, banner));
    }

    /** Server → client: entity {@code entityId} wears {@code banner} on its back. */
    public record BackBanner(int entityId, ItemStack banner) {
        void write(RegistryFriendlyByteBuf buf) {
            buf.writeVarInt(entityId);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, banner);
        }

        static BackBanner read(RegistryFriendlyByteBuf buf) {
            return new BackBanner(buf.readVarInt(), ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
    }

    private static void handleBackBanner(BackBanner msg, IPayloadContext context) {
        BackBanners.put(msg.entityId(), msg.banner());
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
            CHANNEL.send(player, new BaseMapPart(i, parts, part));
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

    private static void handleRequest(RequestFactionInfo msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        ConquestServer server = ConquestServer.get();
        if (player == null) {
            return;
        }
        List<FactionInfo> factions = server == null ? List.of()
                : FactionInfo.collect(server.data(), player.blockPosition(), server.overworld().getSeed());
        CHANNEL.send(player, new FactionInfoReply(factions));
    }

    private static void handleReply(FactionInfoReply msg, IPayloadContext context) {
        ClientScreens.receiveFactionInfo(msg.factions());
    }

    private static void handleMapRequest(RequestBaseMap msg, IPayloadContext context) {
        ServerPlayer player = SimpleChannel.sender(context);
        ConquestServer server = ConquestServer.get();
        if (player != null && server != null) {
            server.mapStore().request(player);
        }
    }

    private static void handleMapPart(BaseMapPart msg, IPayloadContext context) {
        ClientScreens.receiveBaseMapPart(msg.index(), msg.count(), msg.bytes());
    }
}
