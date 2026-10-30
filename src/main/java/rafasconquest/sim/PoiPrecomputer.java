package rafasconquest.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.RecruitsBridge;
import rafasconquest.core.CapitalLayout;
import rafasconquest.core.CityLayout;
import rafasconquest.core.FarmLayout;
import rafasconquest.core.OriginRoller;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.SeedHash;
import rafasconquest.core.TerritoryMap;
import rafasconquest.core.WorldSettings;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.map.BaseMap;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;
import rafasconquest.worldgen.ConquestWorldgen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Computes POIs before their chunks exist, the way /locate does: placement math picks candidate chunks, then the
 * structure's own findValidGenerationPoint (height + biome check) decides, so the prediction matches real worldgen.
 * Server thread only.
 */
public class PoiPrecomputer {
    private final ServerLevel level;
    private final ConquestSavedData data;
    private final WorldSettings settings;
    private final RecruitsBridge bridge;
    private final long seed;
    @Nullable
    private final Structure farm;
    /** Both City-state homes and Empire capitals are cities. */
    @Nullable
    private final Structure city;
    /** The world map's provinces, once drawn; Farms belong to whoever holds their province. */
    @Nullable
    private BaseMap provinces;

    public PoiPrecomputer(ServerLevel level, ConquestSavedData data, RecruitsBridge bridge) {
        this.level = level;
        this.data = data;
        this.settings = data.settings();
        this.bridge = bridge;
        this.seed = level.getSeed();
        Registry<Structure> structures = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        this.farm = structures.get(ConquestWorldgen.FARM);
        this.city = structures.get(ConquestWorldgen.CITY);
        if (farm == null || city == null) {
            RafasConquest.LOGGER.error("RafasConquest structures are missing from the registry; POI precompute is disabled");
        }
    }

    public static int regionOf(int blockCoord) {
        return Math.floorDiv(blockCoord, WorldSettings.REGION_SIZE);
    }

    /** Regions whose nearest block lies within {@code radius} of (x, z), nearest first. */
    public static List<long[]> regionsWithin(int x, int z, int radius) {
        List<long[]> regions = new ArrayList<>();
        int size = WorldSettings.REGION_SIZE;
        for (int rx = regionOf(x - radius); rx <= regionOf(x + radius); rx++) {
            for (int rz = regionOf(z - radius); rz <= regionOf(z + radius); rz++) {
                long dx = x - Math.max(rx * size, Math.min(x, rx * size + size - 1));
                long dz = z - Math.max(rz * size, Math.min(z, rz * size + size - 1));
                long distSq = dx * dx + dz * dz;
                if (distSq <= (long) radius * radius) {
                    regions.add(new long[]{rx, rz, distSq});
                }
            }
        }
        regions.sort(Comparator.comparingLong(r -> r[2]));
        return regions;
    }

    public void precomputeRegion(int regionX, int regionZ) {
        if (farm == null || city == null || data.isRegionPrecomputed(regionX, regionZ)) {
            return;
        }
        int size = WorldSettings.REGION_SIZE;
        int minX = regionX * size;
        int minZ = regionZ * size;
        int maxX = minX + size - 1;
        int maxZ = minZ + size - 1;

        int spacing = settings.farmSpacingChunks();
        for (int cellX = Math.floorDiv(minX >> 4, spacing); cellX <= Math.floorDiv(maxX >> 4, spacing); cellX++) {
            for (int cellZ = Math.floorDiv(minZ >> 4, spacing); cellZ <= Math.floorDiv(maxZ >> 4, spacing); cellZ++) {
                FarmLayout.ChunkCoord c = FarmLayout.candidateChunk(seed, settings, cellX, cellZ);
                ChunkPos chunk = new ChunkPos(c.x(), c.z());
                int x = chunk.getMiddleBlockX();
                int z = chunk.getMiddleBlockZ();
                if (x < minX || x > maxX || z < minZ || z > maxZ || data.poiInChunk(chunk, PoiType.FARM) != null
                        || !FarmLayout.isFarmChunk(seed, settings, c.x(), c.z())) {
                    continue;
                }
                findValid(farm, chunk).ifPresent(pos ->
                        data.addPoi(new Poi(pos, PoiType.FARM, farmOwner(pos), settings.farmDailyOutput(), false)));
            }
        }
        data.markRegionPrecomputed(regionX, regionZ);
    }

    /**
     * Turns on province ownership: every Farm on the map goes to whoever holds its province, and Farms found later
     * follow the provinces too. Called once the world map is ready.
     */
    public void useProvinces(BaseMap map) {
        provinces = map;
        int changed = 0;
        for (Poi poi : data.pois()) {
            if (poi.type() == PoiType.FARM) {
                UUID owner = farmOwner(poi.pos());
                if (!Objects.equals(owner, poi.owner())) {
                    data.setPoiOwner(poi, owner);
                    changed++;
                }
            }
        }
        RafasConquest.LOGGER.info("Farms now belong to whoever holds their province ({} changed hands)", changed);
    }

    /** A Farm belongs to whoever holds its province. */
    @Nullable
    private UUID farmOwner(BlockPos pos) {
        return holderAt(pos.getX(), pos.getZ());
    }

    /** Who holds (x, z): the nation holding its province on the world map, if any. */
    @Nullable
    public UUID holderAt(int x, int z) {
        if (provinces == null) {
            return null;
        }
        BaseMap.Nation nation = provinces.holderOf(provinces.provinceIndexAt(x, z));
        return nation == null ? null : nation.id();
    }

    /**
     * Creates the nations of the world map as factions, if not created already: each holding its title, its seat city
     * its capital (a City-state's, its city). Called once the server has started, so Recruits can take them.
     */
    public void ensureNations() {
        if (provinces == null) {
            return;
        }
        int created = 0;
        for (BaseMap.Nation n : provinces.nations()) {
            if (data.faction(n.id()) != null) {
                continue;
            }
            boolean cityState = n.tier() == rafasconquest.core.TitleMap.Tier.COUNTY;
            int y = level.getChunkSource().getGenerator().getBaseHeight(n.x(), n.z(),
                    net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, level, level.getChunkSource().randomState());
            BlockPos pos = new BlockPos(n.x(), y, n.z());
            Faction faction = new Faction(n.id(), n.name(), cityState ? FactionType.CITY_STATE : FactionType.EMPIRE, pos,
                    n.radiusSteps(), n.strength());
            data.addFaction(faction);
            data.addPoi(new Poi(pos, cityState ? PoiType.CITY : PoiType.CAPITAL, faction.id(),
                    cityState ? settings.cityDailyOutput() : 0, false));
            bridge.onFactionCreated(faction);
            created++;
        }
        RafasConquest.LOGGER.info("Nations: {} on the world map, {} created now", provinces.nations().size(), created);
    }

    private Optional<BlockPos> findValid(Structure structure, ChunkPos chunk) {
        ServerChunkCache chunks = level.getChunkSource();
        Structure.GenerationContext ctx = new Structure.GenerationContext(
                level.registryAccess(),
                chunks.getGenerator(),
                chunks.getGenerator().getBiomeSource(),
                chunks.randomState(),
                level.getStructureManager(),
                seed,
                chunk,
                level,
                structure.biomes()::contains);
        return structure.findValidGenerationPoint(ctx).map(Structure.GenerationStub::position);
    }
}
