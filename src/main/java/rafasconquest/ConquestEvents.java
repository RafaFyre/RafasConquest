package rafasconquest;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.InteractionResult;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.dev.BattleMap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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

@EventBusSubscriber(modid = RafasConquest.MOD_ID)
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
            // The map, and so the titles and their seat cities, before the first chunk is made.
            rafasconquest.map.BaseMap map = ConquestServer.get().mapStore().loadOrDrawNow();
            ConquestRuntime.publishFounders(map.holders().clone()); // who founded each city, before the wars
            rafasconquest.politics.Wars.restore(level, map); // the land as the wars have left it
            ConquestRuntime.publishMap(map);
            ConquestRuntime.publishCities(new rafasconquest.worldgen.CityIndex(map.cities()));
            ConquestRuntime.publishCountryside(new rafasconquest.worldgen.CountryIndex(map.settlements()));
            rafasconquest.core.CityLayout.publishBuilt((min, max) -> ConquestRuntime.cities().inBox(min[0], min[1], max[0], max[1]));
            rafasconquest.core.CityLayout.publishWallOutsets(rafasconquest.style.CityPlan::wallOutset);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestServer.detach();
            ConquestRuntime.publish(null);
            ConquestRuntime.publishCities(null);
            ConquestRuntime.publishMap(null);
            ConquestRuntime.publishCountryside(null);
            CapitalLayout.setLand(0, null);
        }
    }

    /** A player joins: their client is told how far the world is stretched, for its far-terrain quality. */
    @SubscribeEvent
    public static void onPlayerLoggedIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            rafasconquest.net.ConquestNetwork.sendWorldStretch(player, rafasconquest.worldgen.HorizontalStretch.current());
            rafasconquest.peerage.PeerageSync.to(player);
            rafasconquest.politics.PoliticsSync.to(player);
            rafasconquest.peerage.CityManager.close(player); // left mid city view (a crash): put them back
        }
    }

    /** A player leaves mid city view: back where they were, before they are saved. */
    @SubscribeEvent
    public static void onPlayerLoggedOut(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            rafasconquest.peerage.CityManager.close(player);
        }
    }

    /** Before the world's levels are made: its horizontal stretch, as chosen when it was created. */
    @SubscribeEvent
    public static void onServerAboutToStart(net.neoforged.neoforge.event.server.ServerAboutToStartEvent event) {
        rafasconquest.style.StyleLibrary.init(); // the style folders, before any land is built
        rafasconquest.worldgen.HorizontalStretch.onServerAboutToStart(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ConquestServer server = ConquestServer.get();
        if (server != null) {
            server.onServerStarted();
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            ConquestServer server = ConquestServer.get();
            if (server != null) {
                server.tick();
            }
        }
    }

    /** Drives the debug battle and times whole server ticks while it runs. */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        RecruitsBattle.onServerTickStart();
    }

    @SubscribeEvent
    public static void onServerTickEnd(ServerTickEvent.Post event) {
        RecruitsBattle.onServerTickEnd();
        BattleMap.tick();
        rafasconquest.npc.Townsfolk.tick(event.getServer());
        rafasconquest.peerage.QuestProgress.tick(event.getServer());
        rafasconquest.politics.Wars.tick(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        RecruitsBattle.stop();
        BattleMap.close();
        rafasconquest.npc.Townsfolk.clear();
    }

    /** A battle's dead drop nothing: no loot to clutter the field, and no armour for others to swap into. */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        RecruitsBattle.onDeath(event.getEntity());
        if (event.getSource().getEntity() instanceof ServerPlayer killer) {
            rafasconquest.peerage.QuestProgress.killed(killer, event.getEntity());
        }
    }

    /** Cavalry momentum and braced walls change how hard battle soldiers hit each other. */
    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        event.setAmount(RecruitsBattle.adjustDamage(event.getEntity(), event.getSource().getEntity(), event.getAmount()));
    }

    /**
     * A battle's soldiers (and their horses) can't be hired, or otherwise used, by right-clicking them: Recruits would
     * open its hire menu for any recruit nearby.
     */
    /** The war horn isn't blown on use: it opens its screen on the client, and blows when orders are given. */
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (rafasconquest.bridge.WarHorn.is(event.getItemStack())) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (RecruitsBattle.isOurs(event.getTarget())) {
            if (event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND && event.getEntity() instanceof ServerPlayer player) {
                RecruitsBattle.onInteract(player, event.getTarget()); // the war council hears officers out
            }
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
        // Walls (banners, guards), and the inside of cities and castles' keeps (the posts' and churches' people, lords).
        if (settings != null && settings.citiesEnabled() && CityLayout.chunkInCity(level.getSeed(), settings, chunk.getPos().x, chunk.getPos().z)
                || rafasconquest.sim.RecruitmentPosts.holdInChunk(chunk.getPos().x, chunk.getPos().z)) {
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
