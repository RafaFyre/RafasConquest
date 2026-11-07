package rafasconquest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.core.CityLayout;
import rafasconquest.core.WorldSettings;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.style.CityPlan;
import rafasconquest.style.CityStyles;
import rafasconquest.style.StylePiece;
import rafasconquest.worldgen.ConquestRuntime;
import rafasconquest.worldgen.ConquestWorldgen;
import rafasconquest.worldgen.GoldChurchPiece;
import rafasconquest.worldgen.HarbourPiece;
import rafasconquest.worldgen.BarracksPiece;

/**
 * The music of where each player stands, told to their client once a second: "To Shores, Distant" at sea, on a beach
 * or in a harbour; "Dancing Steps of War" in a barracks, a sword sanctum, a dojo, or the war council's tent;
 * and whether they are in a city's streets, where its ambient sound is heard unless one of those plays.
 */
public final class PlaceMusic {
    public static final int NONE = 0;
    public static final int SHORES = 1;
    public static final int WAR = 2;
    /** Style files sit under style/<style>/, so these are matched anywhere in the path. */
    private static final String LANDMARKS = "city/buildings/landmarks/";

    private static int ticks;

    private PlaceMusic() {
    }

    public static void tick(MinecraftServer server) {
        if (++ticks % 20 != 0) {
            return;
        }
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ConquestNetwork.sendPlace(player, of(player), inCity(player.serverLevel(), player.blockPosition()));
        }
    }

    /**
     * Whether {@code pos} is in a city's streets, where its ambient sound is heard: inside its walls, but not in its noble
     * district nor in a landmark (a style's landmark building or the gold church).
     */
    static boolean inCity(ServerLevel level, BlockPos pos) {
        WorldSettings settings = ConquestRuntime.settings();
        if (settings == null || level.dimension() != Level.OVERWORLD) {
            return false;
        }
        int x = pos.getX();
        int z = pos.getZ();
        int r = CityLayout.MAX_HALF_EXTENT;
        for (CityLayout.Site city : CityLayout.allSitesInBox(level.getSeed(), settings, x - r, z - r, x + r, z + r)) {
            if (inStreets(city.size(), CityPlan.of(city), x - city.originX(), z - city.originZ())) {
                return !inLandmark(level, pos);
            }
        }
        return false;
    }

    /** Whether city-local (lx, lz) of a city {@code size} blocks a side is inside its walls and out of its noble district. */
    static boolean inStreets(int size, CityPlan.Plan plan, int lx, int lz) {
        int in = CityLayout.ROAD_WIDTH; // the outermost road band is where the wall stands
        if (lx < in || lz < in || lx >= size - in || lz >= size - in) {
            return false;
        }
        // The district's bounding roads count as the district: its inner wall stands on them in big cities.
        return !(plan.noble()
                && lx >= CityLayout.roadStart(plan.x0()) && lx < CityLayout.roadStart(plan.x1()) + in
                && lz >= CityLayout.roadStart(plan.z0()) && lz < CityLayout.roadStart(plan.z1()) + in);
    }

    private static boolean inLandmark(ServerLevel level, BlockPos pos) {
        StructureManager structures = level.structureManager();
        for (Structure structure : structures.getAllStructuresAt(pos).keySet()) {
            for (StructurePiece piece : structures.getStructureWithPieceAt(pos, structure).getPieces()) {
                if (piece.getBoundingBox().isInside(pos) && (piece instanceof GoldChurchPiece
                        || piece instanceof StylePiece style && style.file().contains(LANDMARKS))) {
                    return true;
                }
            }
        }
        return false;
    }

    static int of(ServerPlayer player) {
        if (RecruitsBattle.inCouncilTent(player)) {
            return WAR;
        }
        ServerLevel level = player.serverLevel();
        BlockPos pos = player.blockPosition();
        StructureManager structures = level.structureManager();
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        boolean harbour = false;
        for (Structure structure : structures.getAllStructuresAt(pos).keySet()) {
            StructureStart start = structures.getStructureWithPieceAt(pos, structure);
            if (!start.isValid()) {
                continue;
            }
            Holder<Structure> holder = registry.wrapAsHolder(structure);
            if (holder.is(ConquestWorldgen.DOJO) || holder.is(ConquestWorldgen.HIDDEN_DOJO)) {
                return WAR;
            }
            for (StructurePiece piece : start.getPieces()) {
                if (!piece.getBoundingBox().isInside(pos)) {
                    continue;
                }
                if (piece instanceof BarracksPiece
                        || piece instanceof StylePiece style && style.file().contains(CityStyles.SWORD_SANCTUM)) {
                    return WAR;
                }
                harbour |= piece instanceof HarbourPiece;
            }
        }
        Holder<Biome> biome = level.getBiome(pos);
        return harbour || biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_BEACH) ? SHORES : NONE;
    }
}
