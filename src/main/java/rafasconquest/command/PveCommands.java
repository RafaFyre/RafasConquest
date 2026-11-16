package rafasconquest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import rafasconquest.character.GameCharacter;
import rafasconquest.character.Rank;
import rafasconquest.character.BattleRole;
import rafasconquest.character.Job;
import rafasconquest.data.Ranks;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import rafasconquest.dev.BattleMap;
import rafasconquest.battle.BattlePlan;
import rafasconquest.politics.Lands.Fort;
import rafasconquest.battle.BattleType;
import rafasconquest.battle.FieldArmy;
import rafasconquest.battle.FieldBattle;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.bridge.RecruitsFactions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.core.WorldSettings;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.dev.CityStats;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionType;
import rafasconquest.faction.Faction;
import rafasconquest.faction.ResourceType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;
import rafasconquest.sim.ConquestServer;
import rafasconquest.sim.EconomyTick;
import rafasconquest.sim.PoiPrecomputer;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public final class PveCommands {
    private static final int MAX_POI_LINES = 150;
    /** Soldiers on each side of a debug battle when no number is given. */
    private static final int DEFAULT_ARMY = 100;
    private static final SimpleCommandExceptionType NOT_RUNNING = new SimpleCommandExceptionType(Component.literal("RafasConquest is not running in this world"));
    private static final SimpleCommandExceptionType NO_FACTION = new SimpleCommandExceptionType(Component.literal("No faction with that name"));

    private PveCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // The war council's choices, clicked in chat: open to every player, not only operators.
        dispatcher.register(Commands.literal("council")
                .then(Commands.argument("topic", com.mojang.brigadier.arguments.StringArgumentType.word())
                        .executes(ctx -> {
                            String line = RecruitsBattle.councilTopic(ctx.getSource().getPlayerOrException(),
                                    com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "topic"));
                            if (!line.isEmpty()) {
                                ctx.getSource().sendFailure(Component.literal(line));
                            }
                            return 1;
                        })));
        dispatcher.register(Commands.literal("pve")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("faction")
                        .then(Commands.literal("list").executes(PveCommands::factionList))
                        .then(Commands.literal("info")
                                .then(Commands.literal("nearest").executes(ctx -> factionInfo(ctx, null)))
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(factionNames(), builder))
                                        .executes(ctx -> factionInfo(ctx, StringArgumentType.getString(ctx, "name"))))))
                .then(jobCommand())
                .then(roleCommand())
                .then(Commands.literal("rank")
                        .executes(PveCommands::rankShow)
                        .then(Commands.literal("set").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("rank", IntegerArgumentType.integer(1, 8))
                                                .executes(ctx -> rankSet(ctx, EntityArgument.getPlayer(ctx, "player"), IntegerArgumentType.getInteger(ctx, "rank")))))))
                .then(Commands.literal("noble").requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("noble", com.mojang.brigadier.arguments.BoolArgumentType.bool())
                                        .executes(ctx -> nobleSet(ctx, EntityArgument.getPlayer(ctx, "player"),
                                                com.mojang.brigadier.arguments.BoolArgumentType.getBool(ctx, "noble"))))))
                .then(Commands.literal("title").requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("none").executes(ctx -> titleSet(ctx, EntityArgument.getPlayer(ctx, "player"), rafasconquest.peerage.NobleTitle.NONE)))
                                .then(Commands.literal("knight").executes(ctx -> titleSet(ctx, EntityArgument.getPlayer(ctx, "player"), rafasconquest.peerage.NobleTitle.KNIGHT)))
                                .then(Commands.literal("count").executes(ctx -> titleSet(ctx, EntityArgument.getPlayer(ctx, "player"), rafasconquest.peerage.NobleTitle.COUNT)))))
                // Testing: the nearest city's holder, stood beside the player, to talk to without travelling there.
                .then(Commands.literal("holder").requires(source -> source.hasPermission(2))
                        .executes(PveCommands::holderHere))
                // A nation's horn style in battle ("goat": its goat horn call, as before horn styles).
                .then(Commands.literal("horn").requires(source -> source.hasPermission(2))
                        .then(Commands.argument("nation", StringArgumentType.string())
                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                .then(Commands.argument("style", StringArgumentType.word())
                                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(java.util.stream.Stream.concat(
                                                java.util.stream.Stream.of("goat"), rafasconquest.bridge.RecruitsBattle.HORN_STYLES.stream()), b))
                                        .executes(PveCommands::hornSet))))
                .then(Commands.literal("muster")
                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.muster(ctx.getSource().getPlayerOrException()))))
                .then(Commands.literal("politics")
                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.report(ctx.getSource().getServer())))
                        .then(Commands.literal("day").requires(source -> source.hasPermission(2))
                                .executes(ctx -> say(ctx, rafasconquest.politics.Wars.runDays(ctx.getSource().getServer(), 1)))
                                .then(Commands.argument("days", IntegerArgumentType.integer(1, 60))
                                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.runDays(ctx.getSource().getServer(),
                                                IntegerArgumentType.getInteger(ctx, "days"))))))
                        .then(Commands.literal("muster").requires(source -> source.hasPermission(2))
                                .then(Commands.literal("finish")
                                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.finishMuster(ctx.getSource().getServer(), null)))
                                        .then(Commands.argument("nation", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                .suggests(PveCommands::suggestNations)
                                                .executes(ctx -> say(ctx, rafasconquest.politics.Wars.finishMuster(ctx.getSource().getServer(),
                                                        arg(ctx, "nation"))))))
                                // Every war's armies (or one nation's wars') straight onto their battle sites.
                                .then(Commands.literal("field")
                                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.toField(ctx.getSource().getServer(), null)))
                                        .then(Commands.argument("nation", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                .suggests(PveCommands::suggestNations)
                                                .executes(ctx -> say(ctx, rafasconquest.politics.Wars.toField(ctx.getSource().getServer(),
                                                        arg(ctx, "nation")))))))
                        // Armies on the march (or one nation's) arrive at their battle sites now.
                        .then(Commands.literal("march").requires(source -> source.hasPermission(2))
                                .then(Commands.literal("finish")
                                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.finishMarch(ctx.getSource().getServer(), null)))
                                        .then(Commands.argument("nation", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                .suggests(PveCommands::suggestNations)
                                                .executes(ctx -> say(ctx, rafasconquest.politics.Wars.finishMarch(ctx.getSource().getServer(),
                                                        arg(ctx, "nation")))))))
                        // Two characters fall to feuding now; optionally a skirmish or duel between them in half a minute.
                        .then(Commands.literal("feud").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("first", com.mojang.brigadier.arguments.StringArgumentType.string())
                                        .then(Commands.argument("second", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                .executes(ctx -> say(ctx, rafasconquest.politics.Wars.forceFeud(ctx.getSource().getServer(),
                                                        arg(ctx, "first"), arg(ctx, "second"), null)))
                                                .then(Commands.argument("fight", StringArgumentType.word())
                                                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(List.of("skirmish", "duel"), b))
                                                        .executes(ctx -> say(ctx, rafasconquest.politics.Wars.forceFeud(ctx.getSource().getServer(),
                                                                arg(ctx, "first"), arg(ctx, "second"), arg(ctx, "fight"))))))))
                        .then(Commands.literal("war").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("attacker", com.mojang.brigadier.arguments.StringArgumentType.string()).suggests(PveCommands::suggestNations)
                                        .then(Commands.argument("defender", com.mojang.brigadier.arguments.StringArgumentType.string()).suggests(PveCommands::suggestNations)
                                                .executes(ctx -> war(ctx, null, null))
                                                // Optionally each side's general: one of its characters, or "best" for its best at war.
                                                .then(Commands.argument("attackerGeneral", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                        .suggests((ctx, b) -> suggestGenerals(ctx, b, "attacker"))
                                                        .executes(ctx -> war(ctx, arg(ctx, "attackerGeneral"), null))
                                                        .then(Commands.argument("defenderGeneral", com.mojang.brigadier.arguments.StringArgumentType.string())
                                                                .suggests((ctx, b) -> suggestGenerals(ctx, b, "defender"))
                                                                .executes(ctx -> war(ctx, arg(ctx, "attackerGeneral"), arg(ctx, "defenderGeneral")))))))))
                // Reads the quest and dialogue files (mods/RafasConquest/quests, dialogue) again.
                .then(Commands.literal("data").then(Commands.literal("reload").executes(ctx -> say(ctx, rafasconquest.data.DataFiles.reload()))))
                .then(Commands.literal("style")
                        // Reads the style folders (mods/RafasConquest/structures) again.
                        .then(Commands.literal("reload").executes(ctx -> say(ctx, rafasconquest.style.StyleLibrary.reload())))
                        // Moves a structure saved with a structure block (as rafasconquest:<name>) into a style's slot.
                        .then(Commands.literal("save")
                                .then(Commands.argument("style", StringArgumentType.word())
                                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(List.of(rafasconquest.style.StyleLibrary.DEFAULT), b))
                                        .then(Commands.argument("slot", StringArgumentType.string())
                                                .suggests((ctx, b) -> SharedSuggestionProvider.suggest(rafasconquest.style.StyleLibrary.SLOTS.stream()
                                                        .map(s -> "\"" + s + "\"").toList(), b))
                                                .then(Commands.argument("name", StringArgumentType.word())
                                                        .executes(ctx -> say(ctx, rafasconquest.style.StyleLibrary.save(
                                                                ctx.getSource().getServer().getWorldPath(net.minecraft.world.level.storage.LevelResource.GENERATED_DIR),
                                                                StringArgumentType.getString(ctx, "style"), StringArgumentType.getString(ctx, "slot"),
                                                                StringArgumentType.getString(ctx, "name")))))))))
                .then(Commands.literal("poi")
                        .then(Commands.literal("list")
                                .executes(ctx -> poiList(ctx, 500))
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, 30_000))
                                        .executes(ctx -> poiList(ctx, IntegerArgumentType.getInteger(ctx, "radius"))))))
                .then(Commands.literal("debug")
                        .then(Commands.literal("recruits").executes(PveCommands::recruitsFactions))
                        .then(Commands.literal("cityterrain").executes(PveCommands::cityTerrain))
                        // A castle 35 blocks ahead, its gate toward you.
                        .then(Commands.literal("castle").executes(PveCommands::castle))
                        .then(Commands.literal("column")
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .executes(ctx -> column(ctx, IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "z"))))))
                        .then(Commands.literal("citystats")
                                .executes(ctx -> cityStats(ctx, 12))
                                .then(Commands.argument("samples", IntegerArgumentType.integer(1, 200))
                                        .executes(ctx -> cityStats(ctx, IntegerArgumentType.getInteger(ctx, "samples")))))
                        .then(Commands.literal("advance")
                                .then(Commands.argument("days", IntegerArgumentType.integer(1, 100_000))
                                        .executes(ctx -> advance(ctx, IntegerArgumentType.getInteger(ctx, "days")))))
                        // One of each kind of a nation's soldiers and engines, frozen on stone blocks, in a line to your
                        // right starting 3 blocks ahead; clear removes them.
                        .then(Commands.literal("display")
                                .then(Commands.literal("clear").executes(ctx -> say(ctx, RecruitsBattle.clearDisplay(ctx.getSource().getLevel()))))
                                .then(Commands.literal("unit").then(Commands.literal("all")
                                        .then(Commands.argument("nation", StringArgumentType.greedyString())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(factionNames(), builder))
                                                .executes(PveCommands::display)))))
                        .then(Commands.literal("battle")
                                .executes(ctx -> battle(ctx, null, DEFAULT_ARMY, null, DEFAULT_ARMY))
                                .then(Commands.literal("clear").executes(PveCommands::battleClear))
                                // A siege of a castle (the nearest unless named), surveyed first, with the attackers'
                                // strategems: all, or any of ladders, towers, rams joined by + (ladders+rams).
                                .then(Commands.literal("siege")
                                        .executes(ctx -> siege(ctx, null, "all"))
                                        .then(Commands.argument("castle", StringArgumentType.string())
                                                .suggests((ctx, b) -> placeSuggestions(ctx.getSource().getPosition(), b))
                                                .executes(ctx -> siege(ctx, StringArgumentType.getString(ctx, "castle"), "all"))
                                                .then(Commands.argument("strategems", StringArgumentType.word())
                                                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(List.of("all", "ladders", "towers",
                                                                "rams", "ladders+towers", "ladders+rams", "towers+rams"), b))
                                                        .executes(ctx -> siege(ctx, StringArgumentType.getString(ctx, "castle"),
                                                                StringArgumentType.getString(ctx, "strategems")))
                                                        // Army sizes, the defenders' first.
                                                        .then(Commands.argument("defenders", IntegerArgumentType.integer(1, BattlePlan.MAX_SOLDIERS))
                                                                .then(Commands.argument("attackers", IntegerArgumentType.integer(1, BattlePlan.MAX_SOLDIERS))
                                                                        .executes(ctx -> siege(ctx, StringArgumentType.getString(ctx, "castle"),
                                                                                StringArgumentType.getString(ctx, "strategems"),
                                                                                IntegerArgumentType.getInteger(ctx, "defenders"),
                                                                                IntegerArgumentType.getInteger(ctx, "attackers"), null, null)))))))
                                // A test battle of one kind: ocean, land, siege or amphibious (the second side defends).
                                .then(Commands.literal("type")
                                        .then(Commands.argument("type", StringArgumentType.word())
                                                .suggests((ctx, b) -> SharedSuggestionProvider.suggest(java.util.Arrays.stream(BattleType.values())
                                                        .map(t -> t.name().toLowerCase(java.util.Locale.ROOT)).toList(), b))
                                                .executes(ctx -> battle(ctx, null, DEFAULT_ARMY, null, DEFAULT_ARMY, battleType(ctx)))
                                                // Army sizes, the defenders' first; then, if wanted, the factions, the defender first.
                                                .then(Commands.argument("defenders", IntegerArgumentType.integer(1, BattlePlan.MAX_SOLDIERS))
                                                        .then(Commands.argument("attackers", IntegerArgumentType.integer(1, BattlePlan.MAX_SOLDIERS))
                                                                .executes(ctx -> battle(ctx, null, IntegerArgumentType.getInteger(ctx, "attackers"), null,
                                                                        IntegerArgumentType.getInteger(ctx, "defenders"), battleType(ctx)))
                                                                .then(Commands.argument("defender", StringArgumentType.string())
                                                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                                        .then(Commands.argument("attacker", StringArgumentType.string())
                                                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                                                .executes(ctx -> battle(ctx, StringArgumentType.getString(ctx, "attacker"),
                                                                                        IntegerArgumentType.getInteger(ctx, "attackers"),
                                                                                        StringArgumentType.getString(ctx, "defender"),
                                                                                        IntegerArgumentType.getInteger(ctx, "defenders"), battleType(ctx)))))))))
                                .then(Commands.literal("ground").executes(ctx -> say(ctx, RecruitsBattle.debugGround())))
                                .then(Commands.literal("start").executes(ctx -> {
                                    String line = RecruitsBattle.startFighting();
                                    ctx.getSource().sendSuccess(() -> Component.literal(line), true);
                                    return 1;
                                }))
                                .then(Commands.literal("end")
                                        .then(Commands.literal("general").then(Commands.argument("side", IntegerArgumentType.integer(0, 1))
                                                .executes(ctx -> battleEnd(ctx, "general"))))
                                        .then(Commands.literal("surrender").then(Commands.argument("side", IntegerArgumentType.integer(0, 1))
                                                .executes(ctx -> battleEnd(ctx, "surrender")))))
                                .then(Commands.literal("livecap")
                                        .executes(ctx -> {
                                            ctx.getSource().sendSuccess(() -> Component.literal("At most " + RecruitsBattle.liveCap()
                                                    + " real battle soldiers at once"), false);
                                            return 1;
                                        })
                                        .then(Commands.argument("soldiers", IntegerArgumentType.integer(0, 10_000))
                                                .executes(ctx -> {
                                                    String line = RecruitsBattle.setLiveCap(IntegerArgumentType.getInteger(ctx, "soldiers"));
                                                    ctx.getSource().sendSuccess(() -> Component.literal(line), true);
                                                    return 1;
                                                })))
                                .then(Commands.literal("watch")
                                        .then(Commands.literal("clear").executes(ctx -> watch(ctx, null)))
                                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                                        .executes(ctx -> watch(ctx, new BattlePlan.Point(IntegerArgumentType.getInteger(ctx, "x"),
                                                                IntegerArgumentType.getInteger(ctx, "z")))))))
                                .then(Commands.literal("simulate")
                                        .then(Commands.argument("first", StringArgumentType.string())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                .then(Commands.argument("firstSoldiers", IntegerArgumentType.integer(BattlePlan.MIN_SOLDIERS, BattlePlan.MAX_SOLDIERS))
                                                        .then(Commands.argument("second", StringArgumentType.string())
                                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                                .then(Commands.argument("secondSoldiers", IntegerArgumentType.integer(BattlePlan.MIN_SOLDIERS, BattlePlan.MAX_SOLDIERS))
                                                                        .executes(ctx -> simulate(ctx, StringArgumentType.getString(ctx, "first"),
                                                                                IntegerArgumentType.getInteger(ctx, "firstSoldiers"),
                                                                                StringArgumentType.getString(ctx, "second"),
                                                                                IntegerArgumentType.getInteger(ctx, "secondSoldiers"))))))))
                                .then(Commands.argument("first", StringArgumentType.string())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                        .then(Commands.argument("second", StringArgumentType.string())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                .executes(ctx -> battle(ctx, StringArgumentType.getString(ctx, "first"), DEFAULT_ARMY,
                                                        StringArgumentType.getString(ctx, "second"), DEFAULT_ARMY)))
                                        .then(Commands.argument("firstSoldiers", IntegerArgumentType.integer(BattlePlan.MIN_SOLDIERS, BattlePlan.MAX_SOLDIERS))
                                                .then(Commands.argument("second", StringArgumentType.string())
                                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(quotedFactionNames(), builder))
                                                        .then(Commands.argument("secondSoldiers", IntegerArgumentType.integer(BattlePlan.MIN_SOLDIERS, BattlePlan.MAX_SOLDIERS))
                                                                .executes(ctx -> battle(ctx, StringArgumentType.getString(ctx, "first"),
                                                                        IntegerArgumentType.getInteger(ctx, "firstSoldiers"),
                                                                        StringArgumentType.getString(ctx, "second"),
                                                                        IntegerArgumentType.getInteger(ctx, "secondSoldiers"))))))))));
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

    private static List<String> quotedFactionNames() {
        return factionNames().stream().map(n -> n.contains(" ") ? "\"" + n + "\"" : n).toList();
    }

    /**
     * Deploys two armies a short way in front of the caller, marching at each other across their view: {@code
     * firstSoldiers} and {@code secondSoldiers} strong (100 each unless given), in blocks of 20. Without names, the two
     * factions nearest the caller fight.
     */
    private static int battle(CommandContext<CommandSourceStack> ctx, @Nullable String first, int firstSoldiers, @Nullable String second,
                              int secondSoldiers) throws CommandSyntaxException {
        return battle(ctx, first, firstSoldiers, second, secondSoldiers, BattleType.LAND);
    }

    /** Builds a castle (with its gate's doors and Gate Block) 35 blocks ahead of the caller, its gate facing them. */
    private static int castle(CommandContext<CommandSourceStack> ctx) {
        net.minecraft.server.level.ServerLevel level = ctx.getSource().getLevel();
        Vec3 here = ctx.getSource().getPosition();
        net.minecraft.core.Direction look = net.minecraft.core.Direction.fromYRot(ctx.getSource().getRotation().y);
        int x = (int) Math.floor(here.x) + look.getStepX() * 35;
        int z = (int) Math.floor(here.z) + look.getStepZ() * 35;
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, x, z);
        // The gate is on local +x (east, unturned): turn it clockwise until it faces back at the caller.
        int turns = 0;
        for (net.minecraft.core.Direction d = net.minecraft.core.Direction.EAST; d != look.getOpposite(); d = d.getClockWise()) {
            turns++;
        }
        var piece = new rafasconquest.worldgen.CountryPiece(rafasconquest.worldgen.CountryPiece.Build.CASTLE, x, y, z, turns);
        piece.postProcess(level, level.structureManager(), level.getChunkSource().getGenerator(), level.random, piece.getBoundingBox(),
                new net.minecraft.world.level.ChunkPos(x >> 4, z >> 4), new net.minecraft.core.BlockPos(x, y, z));
        BUILT_CASTLES.add(new Fort("castle at " + x + " " + z, x, z, rafasconquest.worldgen.CountryPiece.CASTLE_WALL, false, 0, 0));
        ctx.getSource().sendSuccess(() -> Component.literal("A castle stands at " + x + " " + y + " " + z
                + "; power its Gate Block to swing the doors"), true);
        return 1;
    }

    /** Castles built with {@code /pve debug castle} this session, besiegeable as the map's are. */
    private static final List<Fort> BUILT_CASTLES = new java.util.ArrayList<>();

    /** Every walled place: the map's castles and seat cities and the castles built here, the nearest to {@code here} first. */
    private static List<Fort> castles(Vec3 here) {
        List<Fort> all = new java.util.ArrayList<>(BUILT_CASTLES);
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map != null) {
            for (rafasconquest.core.Countryside.Settlement s : map.settlements()) {
                if (s.kind() == rafasconquest.core.Countryside.Kind.CASTLE) {
                    all.add(new Fort("castle of " + map.names().get(s.province()), s.x(), s.z(),
                            rafasconquest.worldgen.CountryPiece.CASTLE_WALL, false, 0, 0));
                }
            }
            rafasconquest.politics.MapLands lands = rafasconquest.politics.MapLands.of(map);
            for (rafasconquest.core.SeatCities.City c : map.cities()) {
                Fort f = lands.fort(c);
                all.add(new Fort("city of " + f.name(), f.x(), f.z(), f.half(), f.port(), f.outX(), f.outZ()));
            }
        }
        all.sort(Comparator.comparingDouble(c -> Math.hypot(c.x() - here.x, c.z() - here.z)));
        return all;
    }

    /**
     * The walled places, quoted, the nearest city first and the rest nearest first. Built by hand, for
     * SharedSuggestionProvider.suggest sorts them by name.
     */
    private static CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> placeSuggestions(Vec3 here,
                                                                                                    com.mojang.brigadier.suggestion.SuggestionsBuilder b) {
        List<Fort> all = new java.util.ArrayList<>(castles(here));
        all.stream().filter(f -> f.half() > rafasconquest.worldgen.CountryPiece.CASTLE_WALL).findFirst().ifPresent(city -> {
            all.remove(city);
            all.add(0, city);
        });
        String typed = b.getRemaining().toLowerCase(Locale.ROOT);
        List<com.mojang.brigadier.suggestion.Suggestion> list = all.stream().map(f -> "\"" + f.name() + "\"")
                .filter(s -> SharedSuggestionProvider.matchesSubStr(typed, s.toLowerCase(Locale.ROOT)))
                .map(s -> new com.mojang.brigadier.suggestion.Suggestion(
                        com.mojang.brigadier.context.StringRange.between(b.getStart(), b.getInput().length()), s))
                .toList();
        return CompletableFuture.completedFuture(new com.mojang.brigadier.suggestion.Suggestions(
                com.mojang.brigadier.context.StringRange.between(b.getStart(), b.getInput().length()), list));
    }

    /**
     * Surveys a castle (the nearest, or the one named) and lays siege to it: the two factions nearest it fight, the
     * nearer attacking, three times the defenders' number, with the {@code strategems} given.
     */
    private static int siege(CommandContext<CommandSourceStack> ctx, @Nullable String name, String strategems) throws CommandSyntaxException {
        return siege(ctx, name, strategems, DEFAULT_ARMY, DEFAULT_ARMY * 3, null, null);
    }

    /** As above, with the armies' sizes and (if named) factions given. */
    private static int siege(CommandContext<CommandSourceStack> ctx, @Nullable String name, String strategems, int defenders,
                             int attackers, @Nullable String defender, @Nullable String attacker) throws CommandSyntaxException {
        ConquestServer server = server();
        CommandSourceStack source = ctx.getSource();
        List<Fort> all = castles(source.getPosition());
        Fort castle = name == null ? (all.isEmpty() ? null : all.get(0))
                : all.stream().filter(c -> c.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (castle == null) {
            throw new SimpleCommandExceptionType(Component.literal(name == null ? "No castle or city to besiege: build a castle with /pve debug castle"
                    : "No castle or city named " + name)).create();
        }
        java.util.Set<rafasconquest.battle.Siege.Engine> engines = java.util.EnumSet.noneOf(rafasconquest.battle.Siege.Engine.class);
        for (String word : strategems.toLowerCase(java.util.Locale.ROOT).split("[+,]")) {
            switch (word) {
                case "all" -> engines.addAll(java.util.EnumSet.allOf(rafasconquest.battle.Siege.Engine.class));
                case "ladders", "ladder" -> engines.add(rafasconquest.battle.Siege.Engine.LADDERS);
                case "towers", "tower" -> engines.add(rafasconquest.battle.Siege.Engine.TOWERS);
                case "rams", "ram" -> engines.add(rafasconquest.battle.Siege.Engine.RAMS);
                default -> throw new SimpleCommandExceptionType(Component.literal("No strategem " + word + ": all, ladders, towers or rams")).create();
            }
        }
        List<Faction> nearest = server.data().factions().stream()
                .sorted(Comparator.comparingDouble(f -> f.origin().getCenter().distanceToSqr(castle.x(), f.origin().getY() + 0.5, castle.z())))
                .toList();
        // The place's own holder defends it (else the second nearest); the nearest other faction attacks.
        Faction holder = server.data().faction(server.precomputer().holderAt(castle.x(), castle.z()));
        Faction d = defender != null ? byName(server, defender) : holder != null ? holder : nearest.size() < 2 ? null : nearest.get(1);
        Faction a = attacker != null ? byName(server, attacker) : nearest.stream().filter(f -> f != d).findFirst().orElse(null);
        if (a == null || d == null || a == d) {
            throw NO_FACTION.create();
        }
        // A city's defenders hold council at its barracks, and its harbour side is never attacked (as in a war's siege).
        boolean city = castle.half() > rafasconquest.worldgen.CountryPiece.CASTLE_WALL;
        if (city) {
            for (rafasconquest.core.SeatCities.City c : rafasconquest.worldgen.ConquestRuntime.cities().inBox(castle.x(), castle.z(), castle.x(), castle.z())) {
                if (c.x() == castle.x() && c.z() == castle.z()) {
                    rafasconquest.worldgen.CityGates.fitAll(server.overworld(), c); // gates are otherwise fitted only with a player near
                }
            }
        }
        source.sendSuccess(() -> Component.literal("Surveying the walls of " + castle.name() + "..."), false);
        rafasconquest.bridge.SiegeSurvey.surveyAsync(server.overworld(), castle.x(), castle.z(), castle.half(),
                city ? rafasconquest.bridge.SiegeSurvey.cityHall(server.overworld(), castle.x(), castle.z()) : null,
                castle.port() ? net.minecraft.core.Direction.fromDelta(castle.outX(), 0, castle.outZ()) : null, engines).whenComplete((survey, error) -> {
            if (error != null) {
                source.sendFailure(Component.literal("Couldn't survey the walls: " + error.getMessage()));
                return;
            }
            for (String line : survey.report(castle.name())) {
                source.sendSuccess(() -> Component.literal(line), false);
            }
            try {
                RecruitsBattle.start(server.overworld(), a, attackers, d, defenders,
                        new BattlePlan.Point(castle.x(), castle.z()), 1, 0, null, null, BattleType.SIEGE, 0, null, survey, line -> {
                            String url = BattleMap.open(server.overworld().getServer());
                            source.sendSuccess(() -> Component.literal(line + ". The armies are deploying: ")
                                    .append(Component.literal("[Open the battle map]").withStyle(style -> style.withColor(ChatFormatting.AQUA)
                                            .withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))))
                                    .append(Component.literal(" or /pve debug battle start")), true);
                        }, why -> source.sendFailure(Component.literal(why)));
            } catch (IllegalStateException e) {
                source.sendFailure(Component.literal(e.getMessage()));
            }
        });
        return 1;
    }

    private static BattleType battleType(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "type");
        try {
            return BattleType.valueOf(name.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new SimpleCommandExceptionType(Component.literal("No battle type " + name + ": ocean, land, siege or amphibious")).create();
        }
    }

    /** As above, of a kind: in a walled battle the second side defends, and an amphibious port sends out a district's ships. */
    private static int battle(CommandContext<CommandSourceStack> ctx, @Nullable String first, int firstSoldiers, @Nullable String second,
                              int secondSoldiers, BattleType type) throws CommandSyntaxException {
        if (type == BattleType.SIEGE) {
            // A siege is fought at a castle's walls, never in the open with camps.
            return siege(ctx, null, "all", secondSoldiers, firstSoldiers, second, first);
        }
        ConquestServer server = server();
        Vec3 here = ctx.getSource().getPosition();
        List<Faction> nearest = server.data().factions().stream()
                .sorted(Comparator.comparingDouble(f -> f.origin().getCenter().distanceToSqr(here.x, f.origin().getY() + 0.5, here.z)))
                .toList();
        Faction a = first == null ? (nearest.isEmpty() ? null : nearest.get(0)) : byName(server, first);
        Faction b = second == null ? (nearest.size() < 2 ? null : nearest.get(1)) : byName(server, second);
        if (a == null || b == null || a == b) {
            throw NO_FACTION.create();
        }
        // Look direction flattened; the armies' line of battle runs across it, so the caller watches from the side.
        float yaw = ctx.getSource().getRotation().y;
        double lookX = -Math.sin(Math.toRadians(yaw));
        double lookZ = Math.cos(Math.toRadians(yaw));
        BattlePlan.Point centre = new BattlePlan.Point(here.x + lookX * 45, here.z + lookZ * 45);
        CommandSourceStack source = ctx.getSource();
        try {
            RecruitsBattle.start(server.overworld(), a, firstSoldiers, b, secondSoldiers, centre, -lookZ, lookX, null, null, type,
                    type == BattleType.AMPHIBIOUS ? rafasconquest.politics.Lands.Fort.HARBOUR_SHIPS_PER_DISTRICT : 0, line -> {
                String url = BattleMap.open(server.overworld().getServer());
                source.sendSuccess(() -> Component.literal(line + ". The armies are deploying: place them on the battle map, then start. ")
                        .append(Component.literal("[Open the battle map]").withStyle(style -> style.withColor(ChatFormatting.AQUA).withUnderlined(true)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))))
                        .append(Component.literal(" or /pve debug battle start")), true);
            }, why -> source.sendFailure(Component.literal(why)));
            source.sendSuccess(() -> Component.literal("The armies are surveying the ground..."), false);
        } catch (IllegalStateException e) {
            throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
        }
        return 1;
    }

    /**
     * Fights a battle between two factions' armies as numbers only ({@link FieldBattle}): no soldiers are spawned,
     * the whole battle is worked out at once, and the outcome is reported.
     */
    private static int simulate(CommandContext<CommandSourceStack> ctx, String first, int firstSoldiers, String second,
                                int secondSoldiers) throws CommandSyntaxException {
        ConquestServer server = server();
        Faction a = byName(server, first);
        Faction b = byName(server, second);
        if (a == null || b == null || a == b) {
            throw NO_FACTION.create();
        }
        RandomSource random = server.overworld().random;
        java.util.Random characters = new java.util.Random(random.nextLong());
        GameCharacter generalA = GameCharacter.general(a.doctrines(), characters);
        GameCharacter generalB = GameCharacter.general(b.doctrines(), characters);
        long start = System.nanoTime();
        FieldBattle battle = new FieldBattle(BattlePlan.blocks(firstSoldiers), BattlePlan.blocks(secondSoldiers),
                new BattlePlan.Point(0, 0), 1, 0, random.nextLong(), generalA.plan(), generalB.plan());
        for (Faction f : new Faction[]{a, b}) { // each siege train its field engines, as a real battle gives them
            battle.equip(f == a ? 0 : 1, rafasconquest.battle.Machine.allowed(rafasconquest.core.WorldFeatures.ages().first(),
                    rafasconquest.gear.Armoury.homeland(rafasconquest.worldgen.ConquestRuntime.map(), f.id()), rafasconquest.battle.BattleType.LAND, false));
        }
        FieldBattle.Outcome outcome = battle.run(BattlePlan.TIME_LIMIT_SECONDS);
        long millis = (System.nanoTime() - start) / 1_000_000;
        FieldArmy armyA = battle.army(0);
        FieldArmy armyB = battle.army(1);
        String result;
        if (outcome == null || outcome.winner() < 0) {
            result = outcome == null ? "no result" : outcome.result();
        } else {
            Faction winner = outcome.winner() == 0 ? a : b;
            Faction loser = outcome.winner() == 0 ? b : a;
            boolean fell = !battle.army(1 - outcome.winner()).generalAlive();
            result = winner.name() + " wins" + (fell ? " (" + loser.name() + "'s general fell)" : "");
        }
        int seconds = outcome == null ? battle.seconds() : outcome.seconds();
        String line = String.format(Locale.ROOT,
                "Simulated %s %d v %s %d: %s after %d:%02d. Left %d and %d; blocks relieved %d and %d; generals %s and %s. Worked out in %d ms.",
                a.name(), armyA.started(), b.name(), armyB.started(), result, seconds / 60, seconds % 60,
                armyA.alive(), armyB.alive(), armyA.reliefs(), armyB.reliefs(), armyA.generalAlive() ? "alive" : "fallen",
                armyB.generalAlive() ? "alive" : "fallen", millis);
        String generals = String.format(Locale.ROOT, "  %s: General %s (%s)%n  %s: General %s (%s)",
                a.name(), generalA.name(), generalA.plan().describe(), b.name(), generalB.name(), generalB.plan().describe());
        ctx.getSource().sendSuccess(() -> Component.literal(line + "\n" + generals), true);
        return 1;
    }

    /** {@code /pve job}: shows the player's job; {@code /pve job <job>} sets it. */
    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> jobCommand() {
        var job = Commands.literal("job").executes(ctx -> {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            Job j = Ranks.get(ctx.getSource().getServer()).job(player.getUUID());
            ctx.getSource().sendSuccess(() -> Component.literal("Your job in battle: " + j.title()), false);
            return 1;
        });
        for (Job j : Job.values()) {
            job.then(Commands.literal(j.id()).executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                Ranks.get(ctx.getSource().getServer()).setJob(player.getUUID(), j);
                ctx.getSource().sendSuccess(() -> Component.literal("You will fight as " + j.title()), false);
                return 1;
            }));
        }
        return job;
    }

    /** {@code /pve role}: shows the player's role; {@code /pve role <role>} sets it. */
    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> roleCommand() {
        var role = Commands.literal("role").executes(ctx -> {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            BattleRole r = Ranks.get(ctx.getSource().getServer()).role(player.getUUID());
            ctx.getSource().sendSuccess(() -> Component.literal("Your unit's role in battle: " + r.title()), false);
            return 1;
        });
        for (BattleRole r : BattleRole.values()) {
            role.then(Commands.literal(r.id()).executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                Ranks.get(ctx.getSource().getServer()).setRole(player.getUUID(), r);
                ctx.getSource().sendSuccess(() -> Component.literal("Your unit will fight as " + r.title()), false);
                return 1;
            }));
        }
        return role;
    }

    private static int battleEnd(CommandContext<CommandSourceStack> ctx, String how) {
        String line = RecruitsBattle.debugEnd(how, IntegerArgumentType.getInteger(ctx, "side"));
        ctx.getSource().sendSuccess(() -> Component.literal(line), true);
        return 1;
    }

    /** Shows the player's own rank. */
    private static int rankShow(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Rank rank = Ranks.get(ctx.getSource().getServer()).of(player.getUUID());
        ctx.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT, "You are a %s (rank %d): you lead %d in battle",
                rank.title(), rank.number(), rank.leads())), false);
        return rank.number();
    }

    private static int rankSet(CommandContext<CommandSourceStack> ctx, ServerPlayer player, int number) {
        Rank rank = Rank.of(number);
        Ranks.get(ctx.getSource().getServer()).set(player.getUUID(), rank);
        ctx.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT, "%s is now a %s (rank %d)",
                player.getName().getString(), rank.title(), rank.number())), true);
        return 1;
    }

    /**
     * Knights a player (or takes their knighthood) without their earning it, for testing: a noble enlists at a
     * barracks as a Major (rank 5). A landed player keeps their county.
     */
    /** Spawns the holder of the city nearest the player two blocks in front of them, facing them. */
    private static int holderHere(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            throw NOT_RUNNING.create();
        }
        rafasconquest.core.SeatCities.City city = map.cities().stream()
                .min(java.util.Comparator.comparingDouble(c -> Math.hypot(c.x() - player.getX(), c.z() - player.getZ()))).orElse(null);
        int person = city == null ? -1 : map.realm().holderOf(city.province());
        if (person < 0) {
            ctx.getSource().sendFailure(Component.literal("The nearest city has no holder"));
            return 0;
        }
        Vec3 ahead = player.position().add(player.getLookAngle().multiply(2, 0, 2));
        rafasconquest.style.CharacterMarkers.character(player.serverLevel(), map, person, net.minecraft.core.BlockPos.containing(ahead), player.getYRot() + 180);
        String name = rafasconquest.peerage.Names.styled(map, map.realm().people().get(person));
        ctx.getSource().sendSuccess(() -> Component.literal(name + " stands before you"), true);
        return 1;
    }

    private static int nobleSet(CommandContext<CommandSourceStack> ctx, ServerPlayer player, boolean noble) {
        rafasconquest.peerage.Peerage peerage = rafasconquest.peerage.Peerage.get(ctx.getSource().getServer());
        rafasconquest.peerage.Peerage.Peer peer = peerage.of(player.getUUID());
        if (peer.hold < 0) {
            peer.title = noble ? rafasconquest.peerage.NobleTitle.KNIGHT : rafasconquest.peerage.NobleTitle.NONE;
            peerage.changed();
        }
        ctx.getSource().sendSuccess(() -> Component.literal(player.getName().getString() + (noble ? " now holds" : " no longer holds")
                + " a noble title"), true);
        return 1;
    }

    /**
     * Gives a player a noble title outright. Count grants the county the player stands in, held freely, its character
     * holder dismissed; a county another player holds is refused. Knight or none gives up any county they hold.
     */
    private static int titleSet(CommandContext<CommandSourceStack> ctx, ServerPlayer player, rafasconquest.peerage.NobleTitle title)
            throws CommandSyntaxException {
        rafasconquest.peerage.Peerage peerage = rafasconquest.peerage.Peerage.get(ctx.getSource().getServer());
        String name = player.getName().getString();
        String said;
        if (title == rafasconquest.peerage.NobleTitle.COUNT) {
            rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
            if (map == null) {
                throw NOT_RUNNING.create();
            }
            int province = map.provinceIndexAt(player.getBlockX(), player.getBlockZ());
            if (province < 0) {
                throw new SimpleCommandExceptionType(Component.literal(name + " is not standing in a county")).create();
            }
            int seat = map.titles().countySeat()[map.titles().titleOf(province, rafasconquest.core.TitleMap.Tier.COUNTY)];
            java.util.UUID holder = peerage.heldBy(seat);
            if (holder != null && !holder.equals(player.getUUID())) {
                throw new SimpleCommandExceptionType(Component.literal("Another player holds that county")).create();
            }
            peerage.release(player.getUUID());
            peerage.grant(player.getUUID(), seat, -1, map.realm().holderOf(seat) >= 0 && !peerage.replaced(seat) ? "dismissed" : null);
            rafasconquest.peerage.Talk.joinSeatNation(player, seat);
            said = name + " is now Count of " + map.names().get(seat);
        } else {
            peerage.release(player.getUUID());
            peerage.of(player.getUUID()).title = title;
            peerage.changed();
            said = name + (title.noble() ? " is now a " + title.title() : " no longer holds a noble title");
        }
        rafasconquest.peerage.PeerageSync.toAll(ctx.getSource().getServer());
        ctx.getSource().sendSuccess(() -> Component.literal(said), true);
        return 1;
    }

    /** Treats a spot like a player standing there, so blocks near it become real soldiers (for testing without one). */
    private static int watch(CommandContext<CommandSourceStack> ctx, @Nullable BattlePlan.Point spot) {
        String line = RecruitsBattle.watch(spot);
        ctx.getSource().sendSuccess(() -> Component.literal(line), true);
        return 1;
    }

    private static int display(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Faction nation = byName(server(), arg(ctx, "nation"));
        if (nation == null) {
            throw NO_FACTION.create();
        }
        CommandSourceStack source = ctx.getSource();
        net.minecraft.core.Direction facing = net.minecraft.core.Direction.fromYRot(source.getRotation().y);
        return say(ctx, RecruitsBattle.display(source.getLevel(), nation, net.minecraft.core.BlockPos.containing(source.getPosition()).relative(facing, 3),
                facing.getClockWise(), facing.getOpposite().toYRot()));
    }

    private static int battleClear(CommandContext<CommandSourceStack> ctx) {
        String line = RecruitsBattle.stop();
        ctx.getSource().sendSuccess(() -> Component.literal(line), true);
        return 1;
    }

    @Nullable
    /** The nations of the world map, for the war command: quoted where a name has spaces. */
    private static java.util.concurrent.CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggestNations(
            CommandContext<CommandSourceStack> ctx, com.mojang.brigadier.suggestion.SuggestionsBuilder builder) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            return builder.buildFuture();
        }
        return SharedSuggestionProvider.suggest(map.nations().stream().filter(n -> !n.sanctum())
                .map(n -> n.name().contains(" ") ? "\"" + n.name() + "\"" : n.name()), builder);
    }

    /** Declares a war by the command, each side's general named or not ("best": its best at war). */
    private static int war(CommandContext<CommandSourceStack> ctx, @Nullable String attackerGeneral, @Nullable String defenderGeneral) {
        return say(ctx, rafasconquest.politics.Wars.forceWar(ctx.getSource().getServer(), arg(ctx, "attacker"), arg(ctx, "defender"),
                attackerGeneral == null || attackerGeneral.equalsIgnoreCase("best") ? null : attackerGeneral,
                defenderGeneral == null || defenderGeneral.equalsIgnoreCase("best") ? null : defenderGeneral));
    }

    private static String arg(CommandContext<CommandSourceStack> ctx, String name) {
        return com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, name);
    }

    /** A side's characters, best at war first, to lead it (quoted, as their names have spaces); and "best". */
    private static java.util.concurrent.CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggestGenerals(
            CommandContext<CommandSourceStack> ctx, com.mojang.brigadier.suggestion.SuggestionsBuilder builder, String side) {
        List<String> names = new java.util.ArrayList<>(List.of("best"));
        for (String name : rafasconquest.politics.Wars.generals(arg(ctx, side))) {
            names.add(name.contains(" ") ? "\"" + name + "\"" : name);
        }
        return SharedSuggestionProvider.suggest(names, builder);
    }

    /** Replies with a line (or several). */
    private static int say(CommandContext<CommandSourceStack> ctx, String text) {
        ctx.getSource().sendSuccess(() -> Component.literal(text), false);
        return 1;
    }

    private static int hornSet(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ConquestServer server = server();
        Faction nation = byName(server, arg(ctx, "nation"));
        String style = arg(ctx, "style").toLowerCase(java.util.Locale.ROOT);
        if (nation == null) {
            ctx.getSource().sendFailure(Component.literal("No nation named " + arg(ctx, "nation")));
            return 0;
        }
        if (!style.equals("goat") && !rafasconquest.bridge.RecruitsBattle.HORN_STYLES.contains(style)) {
            ctx.getSource().sendFailure(Component.literal("No horn style " + style + "; there are: goat, "
                    + String.join(", ", rafasconquest.bridge.RecruitsBattle.HORN_STYLES)));
            return 0;
        }
        nation.setHornStyle(style.equals("goat") ? "" : style);
        server.data().setDirty();
        return say(ctx, nation.name() + " now sounds the " + style + " horn");
    }

    private static Faction byName(ConquestServer server, String name) {
        return server.data().factions().stream().filter(f -> f.name().equalsIgnoreCase(name.trim())).findFirst().orElse(null);
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
        source.sendSuccess(() -> Component.literal("Banner: " + FactionBanner.of(faction.id()).describe()), false);
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

    /** Prints the ground height at a column (top solid block, ignoring leaves and plants). Loads the chunk if needed. */
    private static int column(CommandContext<CommandSourceStack> ctx, int x, int z) throws CommandSyntaxException {
        // getChunk loads (or generates) the chunk; the level's own getHeight would report the floor for unloaded ones.
        int top = server().overworld().getChunk(x >> 4, z >> 4)
                .getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15);
        ctx.getSource().sendSuccess(() -> Component.literal("column " + x + " " + z + " top " + top), false);
        return top;
    }

    /** Prints the terrain plan of the city nearest the caller. */
    private static int cityTerrain(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ConquestServer server = server();
        Vec3 here = ctx.getSource().getPosition();
        List<String> lines = CityStats.describeTerrain(server.overworld(), server.data().settings(), (int) here.x, (int) here.z);
        lines.forEach(line -> ctx.getSource().sendSuccess(() -> Component.literal(line), false));
        return lines.size();
    }

    /** Lists the Recruits factions this mod registered, as Recruits sees them. */
    private static int recruitsFactions(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var ours = RecruitsFactions.ours(server().data().factions());
        ctx.getSource().sendSuccess(() -> Component.literal(ours.size() + " factions registered with Recruits:"), false);
        for (var rf : ours) {
            ctx.getSource().sendSuccess(() -> Component.literal(String.format("%s | id %s | leader %s | colour %s | banner %s",
                    rf.getTeamDisplayName(), rf.getStringID(), rf.getTeamLeaderName(), ChatFormatting.getById(rf.getTeamColor()),
                    rf.getBanner().getString("id"))), false);
        }
        return ours.size();
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
