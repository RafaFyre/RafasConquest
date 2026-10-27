package rafasconquest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.core.WorldSettings;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.dev.CityStats;
import rafasconquest.dev.PoolExporter;
import rafasconquest.faction.FactionType;
import rafasconquest.faction.Faction;
import rafasconquest.faction.ResourceType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;
import rafasconquest.sim.ConquestServer;
import rafasconquest.sim.EconomyTick;
import rafasconquest.sim.PoiPrecomputer;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class PveCommands {
    private static final int MAX_POI_LINES = 150;
    private static final SimpleCommandExceptionType NOT_RUNNING = new SimpleCommandExceptionType(Component.literal("RafasConquest is not running in this world"));
    private static final SimpleCommandExceptionType NO_FACTION = new SimpleCommandExceptionType(Component.literal("No faction with that name"));

    private PveCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pve")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("faction")
                        .then(Commands.literal("list").executes(PveCommands::factionList))
                        .then(Commands.literal("info")
                                .then(Commands.literal("nearest").executes(ctx -> factionInfo(ctx, null)))
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(factionNames(), builder))
                                        .executes(ctx -> factionInfo(ctx, StringArgumentType.getString(ctx, "name"))))))
                .then(Commands.literal("poi")
                        .then(Commands.literal("list")
                                .executes(ctx -> poiList(ctx, 500))
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, 30_000))
                                        .executes(ctx -> poiList(ctx, IntegerArgumentType.getInteger(ctx, "radius"))))))
                .then(Commands.literal("debug")
                        .then(Commands.literal("exportpools").executes(PveCommands::exportPools))
                        .then(Commands.literal("citystats")
                                .executes(ctx -> cityStats(ctx, 12))
                                .then(Commands.argument("samples", IntegerArgumentType.integer(1, 200))
                                        .executes(ctx -> cityStats(ctx, IntegerArgumentType.getInteger(ctx, "samples")))))
                        .then(Commands.literal("advance")
                                .then(Commands.argument("days", IntegerArgumentType.integer(1, 100_000))
                                        .executes(ctx -> advance(ctx, IntegerArgumentType.getInteger(ctx, "days")))))));
    }

    private static ConquestServer server() throws CommandSyntaxException {
        ConquestServer server = ConquestServer.get();
        if (server == null) {
            throw NOT_RUNNING.create();
        }
        return server;
    }

    private static List<String> factionNames() {
        ConquestServer server = ConquestServer.get();
        return server == null ? List.of() : server.data().factions().stream().map(Faction::name).toList();
    }

    private static int factionList(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ConquestSavedData data = server().data();
        List<Faction> factions = data.factions().stream()
                .sorted(Comparator.comparingDouble(f -> distanceFromSpawn(f)))
                .toList();
        CommandSourceStack source = ctx.getSource();
        source.sendSuccess(() -> Component.literal(factions.size() + " known factions (distance from spawn = world origin 0, 0):"), false);
        for (Faction f : factions) {
            source.sendSuccess(() -> Component.literal(String.format("%s | %s | %s | origin %d, %d | %,d blocks | %s | strength %,d",
                    f.name(), f.type(), zone(data.settings(), distanceFromSpawn(f)), f.origin().getX(), f.origin().getZ(),
                    Math.round(distanceFromSpawn(f)), claims(f), f.strength())), false);
        }
        return factions.size();
    }

    private static int factionInfo(CommandContext<CommandSourceStack> ctx, @Nullable String name) throws CommandSyntaxException {
        ConquestSavedData data = server().data();
        Vec3 here = ctx.getSource().getPosition();
        Faction faction = data.factions().stream()
                .filter(f -> name == null || f.name().equalsIgnoreCase(name.trim()))
                .min(Comparator.comparingDouble(f -> f.origin().getCenter().distanceToSqr(here.x, f.origin().getY() + 0.5, here.z)))
                .orElseThrow(NO_FACTION::create);
        long farms = data.pois().stream().filter(p -> faction.id().equals(p.owner()) && p.type() == PoiType.FARM).count();
        CommandSourceStack source = ctx.getSource();
        source.sendSuccess(() -> Component.literal(faction.name() + " (" + faction.type() + ")"), false);
        source.sendSuccess(() -> Component.literal(String.format("Origin %d, %d, %d | %s | %,d blocks from spawn",
                faction.origin().getX(), faction.origin().getY(), faction.origin().getZ(),
                zone(data.settings(), distanceFromSpawn(faction)), Math.round(distanceFromSpawn(faction)))), false);
        source.sendSuccess(() -> Component.literal(String.format("%s | strength %,d", claims(faction), faction.strength())), false);
        source.sendSuccess(() -> Component.literal(String.format("Stockpile: food %,d", faction.stock(ResourceType.FOOD))), false);
        source.sendSuccess(() -> Component.literal(String.format("POIs owned: %d (%d farms, capital included)", data.poiCount(faction.id()), farms)), false);
        return 1;
    }

    private static int poiList(CommandContext<CommandSourceStack> ctx, int radius) throws CommandSyntaxException {
        ConquestServer server = server();
        ConquestSavedData data = server.data();
        Vec3 here = ctx.getSource().getPosition();
        for (long[] r : PoiPrecomputer.regionsWithin((int) here.x, (int) here.z, radius)) {
            server.precomputer().precomputeRegion((int) r[0], (int) r[1]);
        }
        long radiusSq = (long) radius * radius;
        List<Poi> pois = data.pois().stream()
                .filter(p -> horizontalDistanceSq(p, here) <= radiusSq)
                .sorted(Comparator.comparingDouble(p -> horizontalDistanceSq(p, here)))
                .toList();
        CommandSourceStack source = ctx.getSource();
        source.sendSuccess(() -> Component.literal(pois.size() + " POIs within " + radius + " blocks:"), false);
        for (Poi p : pois.subList(0, Math.min(pois.size(), MAX_POI_LINES))) {
            Faction owner = data.faction(p.owner());
            source.sendSuccess(() -> Component.literal(String.format("%s at %d, %d, %d | %,d blocks | owner: %s | generated: %s",
                    p.type(), p.pos().getX(), p.pos().getY(), p.pos().getZ(), Math.round(Math.sqrt(horizontalDistanceSq(p, here))),
                    owner == null ? "unowned" : owner.name(), p.generated())), false);
        }
        if (pois.size() > MAX_POI_LINES) {
            source.sendSuccess(() -> Component.literal("... and " + (pois.size() - MAX_POI_LINES) + " more"), false);
        }
        return pois.size();
    }

    private static int advance(CommandContext<CommandSourceStack> ctx, int days) throws CommandSyntaxException {
        ConquestSavedData data = server().data();
        for (int i = 0; i < days; i++) {
            EconomyTick.runDay(data);
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Ran the daily tick " + days + " times (the in-game clock is unchanged)"), true);
        return days;
    }

    private static int exportPools(CommandContext<CommandSourceStack> ctx) {
        Path root = ctx.getSource().getServer().getServerDirectory().toPath().resolve("rafasconquest-export");
        try {
            int n = PoolExporter.exportVanilla(ctx.getSource().getServer(), root) + PoolExporter.exportCity(ctx.getSource().getServer(), root);
            ctx.getSource().sendSuccess(() -> Component.literal("Exported " + n + " pools to " + root.toAbsolutePath()), false);
            return n;
        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.literal("Export failed: " + e));
            return 0;
        }
    }

    private static String claims(Faction f) {
        return f.type() == FactionType.EMPIRE
                ? String.format("radius %d (%,d blocks)", f.radiusSteps(), f.radiusSteps() * WorldSettings.RADIUS_STEP_BLOCKS)
                : "no claims";
    }

    /** Generating many cities takes longer than the watchdog allows on the server thread, so this runs in the background. */
    private static int cityStats(CommandContext<CommandSourceStack> ctx, int samples) throws CommandSyntaxException {
        ConquestServer server = server();
        CompletableFuture.runAsync(() -> {
            long start = System.nanoTime();
            CityStats.measure(server.overworld(), server.data().settings(), samples).forEach(line -> RafasConquest.LOGGER.info("[citystats] {}", line));
            RafasConquest.LOGGER.info("[citystats] done in {} s", (System.nanoTime() - start) / 1_000_000_000);
        }, Util.backgroundExecutor());
        ctx.getSource().sendSuccess(() -> Component.literal("Measuring " + samples + " cities in the background; results go to the server log"), false);
        return 1;
    }

    private static double distanceFromSpawn(Faction f) {
        return Math.sqrt((double) f.origin().getX() * f.origin().getX() + (double) f.origin().getZ() * f.origin().getZ());
    }

    private static double horizontalDistanceSq(Poi p, Vec3 here) {
        double dx = p.pos().getX() + 0.5 - here.x;
        double dz = p.pos().getZ() + 0.5 - here.z;
        return dx * dx + dz * dz;
    }

    private static String zone(WorldSettings s, double distance) {
        if (distance <= s.eyeRadius()) {
            return "eye";
        }
        return distance <= s.warZoneOuterEdge() ? "storm wall" : "outer world";
    }
}
