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
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.InteractionResult;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.dev.BattleMap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rafasconquest.command.PveCommands;
import net.minecraft.world.level.chunk.ChunkGenerator;
import rafasconquest.core.CapitalLayout;
import rafasconquest.core.CityLayout;
import rafasconquest.worldgen.SettlementBiomes;
import rafasconquest.core.WorldSettings;
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
            ChunkGenerator generator = level.getChunkSource().getGenerator();
            CapitalLayout.setLand(level.getSeed(), SettlementBiomes.land(generator.getBiomeSource(),
                    level.getChunkSource().randomState().sampler(), generator.getSeaLevel()));
            ConquestServer.attach(level, data);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestServer.detach();
            ConquestRuntime.publish(null);
            CapitalLayout.setLand(0, null);
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

    /** Drives the debug battle and times whole server ticks while it runs. */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            RecruitsBattle.onServerTickStart();
        } else {
            RecruitsBattle.onServerTickEnd();
            BattleMap.tick();
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        RecruitsBattle.stop();
        BattleMap.close();
    }

    /** A battle's dead drop nothing: no loot to clutter the field, and no armour for others to swap into. */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        RecruitsBattle.onDeath(event.getEntity());
    }

    /** Cavalry momentum and braced walls change how hard battle soldiers hit each other. */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        event.setAmount(RecruitsBattle.adjustDamage(event.getEntity(), event.getSource().getEntity(), event.getAmount()));
    }

    /**
     * A battle's soldiers (and their horses) can't be hired, or otherwise used, by right-clicking them: Recruits would
     * open its hire menu for any recruit nearby.
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (RecruitsBattle.isOurs(event.getTarget())) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void onEntityInteractAt(PlayerInteractEvent.EntityInteractSpecific event) {
        if (RecruitsBattle.isOurs(event.getTarget())) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (RecruitsBattle.isOurs(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            RecruitsBattle.onStartTracking(player, event.getTarget());
        }
    }

    /** May run off the server thread, so it only queues the link; the server tick applies it. */
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getChunk() instanceof LevelChunk chunk) || !(event.getLevel() instanceof ServerLevel level) || level.dimension() != Level.OVERWORLD) {
            return;
        }
        WorldSettings settings = ConquestRuntime.settings();
        if (settings != null && settings.citiesEnabled() && CityLayout.chunkTouchesWall(level.getSeed(), settings, chunk.getPos().x, chunk.getPos().z)) {
            ConquestServer.queueBannerChunk(chunk.getPos());
        }
        for (Structure structure : chunk.getAllStarts().keySet()) {
            StructureType<?> type = structure.type();
            if (type == ConquestWorldgen.FARM_STRUCTURE.get()) {
                ConquestServer.queueStructureLink(chunk.getPos(), PoiType.FARM);
            } else if (type == ConquestWorldgen.CITY_STRUCTURE.get()) {
                // A City-state home or an Empire capital; the link step tells them apart by the POI recorded there.
                ConquestServer.queueStructureLink(chunk.getPos(), PoiType.CITY);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        PveCommands.register(event.getDispatcher());
    }
}
