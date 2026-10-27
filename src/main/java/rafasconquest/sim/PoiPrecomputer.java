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
import rafasconquest.core.OwnershipResolver;
import rafasconquest.core.SeedHash;
import rafasconquest.core.WorldSettings;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;
import rafasconquest.worldgen.ConquestWorldgen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
    @Nullable
    private final Structure capital;
    @Nullable
    private final Structure city;
    /** Sites whose structure can't generate (biome check failed). Deterministic, so safe to cache. */
    private final Set<Long> rejectedCapitalSites = new HashSet<>();
    private final Set<Long> rejectedCitySites = new HashSet<>();

    public PoiPrecomputer(ServerLevel level, ConquestSavedData data, RecruitsBridge bridge) {
        this.level = level;
        this.data = data;
        this.settings = data.settings();
        this.bridge = bridge;
        this.seed = level.getSeed();
        Registry<Structure> structures = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        this.farm = structures.get(ConquestWorldgen.FARM);
        this.capital = structures.get(ConquestWorldgen.CAPITAL);
        this.city = structures.get(ConquestWorldgen.CITY);
        if (farm == null || capital == null || city == null) {
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
        if (farm == null || capital == null || city == null || data.isRegionPrecomputed(regionX, regionZ)) {
            return;
        }
        int size = WorldSettings.REGION_SIZE;
        int minX = regionX * size;
        int minZ = regionZ * size;
        int maxX = minX + size - 1;
        int maxZ = minZ + size - 1;

        for (CapitalLayout.Site site : CapitalLayout.sitesInBox(seed, settings, minX, minZ, maxX, maxZ)) {
            ensureCapital(site);
        }
        for (CityLayout.Site site : CityLayout.sitesInBox(seed, settings, minX, minZ, maxX, maxZ)) {
            ensureCity(site);
        }

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
                        data.addPoi(new Poi(pos, PoiType.FARM, resolveOwner(pos.getX(), pos.getZ()), settings.farmDailyOutput(), false)));
            }
        }
        data.markRegionPrecomputed(regionX, regionZ);
    }

    /** Ownership is decided once, here, and never reassigned in Milestone 1. */
    @Nullable
    private UUID resolveOwner(int x, int z) {
        int range = WorldSettings.CAPITAL_LOOKUP_RANGE;
        List<OwnershipResolver.Candidate> candidates = new ArrayList<>();
        for (CapitalLayout.Site site : CapitalLayout.sitesInBox(seed, settings, x - range, z - range, x + range, z + range)) {
            Faction f = ensureCapital(site);
            if (f != null && f.type() == FactionType.EMPIRE) {
                candidates.add(new OwnershipResolver.Candidate(f.id(), f.origin().getX(), f.origin().getZ(), f.radiusSteps(),
                        f.strength(), SeedHash.hash(seed, SeedHash.SALT_TIE, f.origin().getX(), f.origin().getZ())));
            }
        }
        return OwnershipResolver.resolve(x, z, candidates).orElse(null);
    }

    /** The Empire at this Capital site, creating it on first sight. Null if the Capital can't generate there. */
    @Nullable
    private Faction ensureCapital(CapitalLayout.Site site) {
        Faction existing = data.factionAtOrigin(site.x(), site.z());
        long key = ConquestSavedData.xzKey(site.x(), site.z());
        if (existing != null || rejectedCapitalSites.contains(key) || capital == null) {
            return existing;
        }
        Optional<BlockPos> pos = findValid(capital, new ChunkPos(site.x() >> 4, site.z() >> 4));
        if (pos.isEmpty()) {
            rejectedCapitalSites.add(key);
            return null;
        }
        OriginRoller.Roll roll = OriginRoller.roll(seed, site.x(), site.z(), settings);
        Faction faction = new Faction(roll.id(), roll.name(), FactionType.EMPIRE, pos.get(), roll.radiusSteps(), roll.strength());
        data.addFaction(faction);
        data.addPoi(new Poi(pos.get(), PoiType.CAPITAL, faction.id(), 0, false));
        bridge.onFactionCreated(faction);
        return faction;
    }

    /**
     * The City-state at this city site, creating it on first sight. Its origin and POI sit on the site itself (which
     * is always inside the start chunk), at the town centre's surface height.
     */
    @Nullable
    private Faction ensureCity(CityLayout.Site site) {
        Faction existing = data.factionAtOrigin(site.x(), site.z());
        long key = ConquestSavedData.xzKey(site.x(), site.z());
        if (existing != null || rejectedCitySites.contains(key) || city == null) {
            return existing;
        }
        Optional<BlockPos> stub = findValid(city, new ChunkPos(site.x() >> 4, site.z() >> 4));
        if (stub.isEmpty()) {
            rejectedCitySites.add(key);
            return null;
        }
        BlockPos pos = new BlockPos(site.x(), stub.get().getY(), site.z());
        OriginRoller.Roll roll = OriginRoller.roll(seed, site.x(), site.z(), settings);
        int strength = SeedHash.intInRange(SeedHash.hash(seed, SeedHash.SALT_STRENGTH, site.x(), site.z()),
                settings.cityStrengthMin(), settings.cityStrengthMax());
        Faction faction = new Faction(roll.id(), roll.name(), FactionType.CITY_STATE, pos, 0, strength);
        data.addFaction(faction);
        data.addPoi(new Poi(pos, PoiType.CITY, faction.id(), settings.cityDailyOutput(), false));
        bridge.onFactionCreated(faction);
        return faction;
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
