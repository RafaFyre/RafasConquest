package rafasconquest;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rafasconquest.command.PveCommands;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.poi.PoiType;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;
import rafasconquest.worldgen.ConquestWorldgen;

@Mod.EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ConquestEvents {
    private ConquestEvents() {
    }

    /** Fires before spawn is chosen or any chunk generates, so worldgen sees the frozen settings from the start. */
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestSavedData data = ConquestSavedData.get(level, ConquestConfig.snapshot());
            ConquestRuntime.publish(data.settings());
            ConquestServer.attach(level, data);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestServer.detach();
            ConquestRuntime.publish(null);
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ConquestServer server = ConquestServer.get();
        if (server != null) {
            server.onServerStarted();
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestServer server = ConquestServer.get();
            if (server != null) {
                server.tick();
            }
        }
    }

    /** May run off the server thread, so it only queues the link; the server tick applies it. */
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getChunk() instanceof LevelChunk chunk) || !(event.getLevel() instanceof ServerLevel level) || level.dimension() != Level.OVERWORLD) {
            return;
        }
        for (Structure structure : chunk.getAllStarts().keySet()) {
            StructureType<?> type = structure.type();
            if (type == ConquestWorldgen.FARM_STRUCTURE.get()) {
                ConquestServer.queueStructureLink(chunk.getPos(), PoiType.FARM);
            } else if (type == ConquestWorldgen.CAPITAL_STRUCTURE.get()) {
                ConquestServer.queueStructureLink(chunk.getPos(), PoiType.CAPITAL);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        PveCommands.register(event.getDispatcher());
    }
}
