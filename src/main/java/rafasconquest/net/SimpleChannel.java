package rafasconquest.net;

import io.netty.handler.codec.DecoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The mod's messages on NeoForge's payloads, kept in the shape of Forge's old SimpleChannel: every message rides one
 * payload type, tagged by its id, and is handled on the main thread only when it came the way it was registered for.
 */
final class SimpleChannel {
    enum NetworkDirection { PLAY_TO_SERVER, PLAY_TO_CLIENT }

    private static final CustomPacketPayload.Type<Msg> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "main"));

    private record Entry<T>(int id, NetworkDirection direction, BiConsumer<T, RegistryFriendlyByteBuf> encoder,
                            Function<RegistryFriendlyByteBuf, T> decoder, BiConsumer<T, IPayloadContext> handler) {
        @SuppressWarnings("unchecked")
        void encode(RegistryFriendlyByteBuf buf, Object message) {
            encoder.accept((T) message, buf);
        }

        @SuppressWarnings("unchecked")
        void handle(Object message, IPayloadContext context) {
            handler.accept((T) message, context);
        }
    }

    private record Msg(Entry<?> entry, Object message) implements CustomPacketPayload {
        @Override
        public Type<Msg> type() {
            return TYPE;
        }
    }

    private final String version;
    private final Map<Class<?>, Entry<?>> byType = new HashMap<>();
    private final Map<Integer, Entry<?>> byId = new HashMap<>();
    private final StreamCodec<RegistryFriendlyByteBuf, Msg> codec = StreamCodec.of(
            (buf, msg) -> {
                buf.writeVarInt(msg.entry().id());
                msg.entry().encode(buf, msg.message());
            },
            buf -> {
                int id = buf.readVarInt();
                Entry<?> entry = byId.get(id);
                if (entry == null) {
                    throw new DecoderException("Unknown " + RafasConquest.MOD_ID + " message " + id);
                }
                return new Msg(entry, entry.decoder().apply(buf));
            });

    SimpleChannel(String version) {
        this.version = version;
    }

    <T> Builder<T> messageBuilder(Class<T> type, int id, NetworkDirection direction) {
        return new Builder<>(type, id, direction);
    }

    void register(RegisterPayloadHandlersEvent event) {
        event.registrar(version).playBidirectional(TYPE, codec, this::handle);
    }

    private void handle(Msg msg, IPayloadContext context) {
        NetworkDirection came = context.flow() == PacketFlow.SERVERBOUND ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT;
        if (msg.entry().direction() == came) { // a client may not send what only the server should
            msg.entry().handle(msg.message(), context);
        }
    }

    void send(ServerPlayer player, Object message) {
        PacketDistributor.sendToPlayer(player, wrap(message));
    }

    void sendToServer(Object message) {
        PacketDistributor.sendToServer(wrap(message));
    }

    private Msg wrap(Object message) {
        Entry<?> entry = byType.get(message.getClass());
        if (entry == null) {
            throw new IllegalArgumentException("Unregistered message " + message.getClass());
        }
        return new Msg(entry, message);
    }

    /** The player who sent a message to the server, or null. */
    static @Nullable ServerPlayer sender(IPayloadContext context) {
        return context.player() instanceof ServerPlayer player ? player : null;
    }

    final class Builder<T> {
        private final Class<T> type;
        private final int id;
        private final NetworkDirection direction;
        private BiConsumer<T, RegistryFriendlyByteBuf> encoder;
        private Function<RegistryFriendlyByteBuf, T> decoder;
        private BiConsumer<T, IPayloadContext> handler;

        private Builder(Class<T> type, int id, NetworkDirection direction) {
            this.type = type;
            this.id = id;
            this.direction = direction;
        }

        Builder<T> encoder(BiConsumer<T, RegistryFriendlyByteBuf> encoder) {
            this.encoder = encoder;
            return this;
        }

        Builder<T> decoder(Function<RegistryFriendlyByteBuf, T> decoder) {
            this.decoder = decoder;
            return this;
        }

        Builder<T> consumerMainThread(BiConsumer<T, IPayloadContext> handler) {
            this.handler = handler;
            return this;
        }

        void add() {
            Entry<T> entry = new Entry<>(id, direction, encoder, decoder, handler);
            byType.put(type, entry);
            byId.put(id, entry);
        }
    }
}
