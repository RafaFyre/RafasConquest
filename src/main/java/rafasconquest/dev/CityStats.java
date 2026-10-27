package rafasconquest.dev;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import rafasconquest.core.CityLayout;
import rafasconquest.core.CityTerrain;
import rafasconquest.core.WorldSettings;
import rafasconquest.worldgen.CityGridBuilder;
import rafasconquest.worldgen.CityVariant;
import rafasconquest.worldgen.ConquestWorldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Dev tool: compares cities with vanilla villages of the same style generated at the same sites, in memory only.
 * Buildings = pieces from a houses pool. Footprint = the bounding box of all of a settlement's pieces.
 */
public final class CityStats {
    /** Every block has 4 plots except the town-centre block. */
    private static final int PLOTS = CityLayout.BLOCKS_PER_SIDE * CityLayout.BLOCKS_PER_SIDE * 4 - 4;

    private CityStats() {
    }

    private static final class Totals {
        long buildings;
        long area;
        long width;
        int count;

        void add(int buildings, BoundingBox box) {
            this.buildings += buildings;
            this.area += (long) box.getXSpan() * box.getZSpan();
            this.width += Math.max(box.getXSpan(), box.getZSpan());
            count++;
        }

        double density() {
            return area == 0 ? 0 : buildings * 10_000.0 / area;
        }
    }

    public static List<String> measure(ServerLevel level, WorldSettings settings, int samples) {
        List<String> lines = new ArrayList<>();
        if (!settings.citiesEnabled()) {
            lines.add("Cities are disabled in this world");
            return lines;
        }
        Registry<Structure> structures = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Structure city = structures.get(ConquestWorldgen.CITY);
        Structure district = structures.get(ConquestWorldgen.CITY_DISTRICT);
        long seed = level.getSeed();
        Totals cities = new Totals();
        Totals villages = new Totals();
        int minSide = Integer.MAX_VALUE;
        long plotsFilled = 0;

        int size = settings.cityCellSize();
        for (int ring = 0; ring < 40 && cities.count < samples; ring++) {
            for (int cx = -ring; cx <= ring && cities.count < samples; cx++) {
                for (int cz = -ring; cz <= ring && cities.count < samples; cz++) {
                    if (Math.max(Math.abs(cx), Math.abs(cz)) != ring) {
                        continue;
                    }
                    Optional<CityLayout.Site> site = CityLayout.gridSite(seed, settings, cx, cz);
                    if (site.isEmpty()) {
                        continue;
                    }
                    CityLayout.Site s = site.get();
                    ChunkPos centerChunk = new ChunkPos(s.x() >> 4, s.z() >> 4);
                    StructureStart center = generate(level, city, centerChunk);
                    if (!center.isValid()) {
                        continue;
                    }
                    Optional<CityVariant> variant = CityVariant.at(context(level, centerChunk), s.x(), s.z());
                    Structure village = variant.map(v -> structures.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village_" + v.id()))).orElse(null);
                    if (village == null) {
                        continue;
                    }
                    StructureStart villageStart = generate(level, village, centerChunk);
                    if (!villageStart.isValid()) {
                        continue;
                    }

                    int cityBuildings = buildings(center);
                    int lamps = count(center, "lamp");
                    BoundingBox cityBox = footprint(center);
                    int districts = 0;
                    for (CityLayout.District d : CityLayout.districts(s)) {
                        StructureStart start = generate(level, district, new ChunkPos(d.x() >> 4, d.z() >> 4));
                        if (start.isValid()) {
                            districts++;
                            cityBuildings += buildings(start);
                            lamps += count(start, "lamp");
                            cityBox = BoundingBox.encapsulatingBoxes(List.of(cityBox, footprint(start))).orElse(cityBox);
                        }
                    }
                    cities.add(cityBuildings, cityBox);
                    villages.add(buildings(villageStart), villageStart.getBoundingBox());
                    minSide = Math.min(minSide, Math.min(cityBox.getXSpan(), cityBox.getZSpan()));
                    plotsFilled += cityBuildings;
                    lines.add(String.format("%s city at %d, %d: %d buildings (%.0f%% of plots), %d lamp posts, %d districts, %dx%d | village there: %d buildings, %dx%d",
                            variant.get().id(), s.x(), s.z(), cityBuildings, 100.0 * cityBuildings / PLOTS, lamps, districts, cityBox.getXSpan(), cityBox.getZSpan(),
                            buildings(villageStart), villageStart.getBoundingBox().getXSpan(), villageStart.getBoundingBox().getZSpan()));
                }
            }
        }
        if (cities.count == 0) {
            lines.add("No cities found to sample");
            return lines;
        }
        lines.add(String.format("Villages: %.1f buildings, %.0f blocks wide, %.2f buildings per 100x100", (double) villages.buildings / villages.count,
                (double) villages.width / villages.count, villages.density()));
        lines.add(String.format("Cities:   %.1f buildings, %.0f blocks wide, %.2f buildings per 100x100", (double) cities.buildings / cities.count,
                (double) cities.width / cities.count, cities.density()));
        lines.add(String.format("Ratio over %d sites: density x%.2f, width x%.2f, buildings x%.2f", cities.count,
                cities.density() / villages.density(), (double) cities.width / villages.width, (double) cities.buildings / villages.buildings));
        lines.add(String.format("City outline: smallest side %d (grid %d); plots filled %.1f%%",
                minSide, CityLayout.SIZE, 100.0 * plotsFilled / ((long) PLOTS * cities.count)));
        return lines;
    }

    /** Bounds of a cell's roads, buildings and walls; the terrain piece reaches further out to ease slopes. */
    private static BoundingBox footprint(StructureStart start) {
        List<BoundingBox> boxes = start.getPieces().stream()
                .filter(p -> !(p instanceof rafasconquest.worldgen.CityTerrainPiece))
                .map(StructurePiece::getBoundingBox)
                .toList();
        return BoundingBox.encapsulatingBoxes(boxes).orElse(start.getBoundingBox());
    }

    private static int buildings(StructureStart start) {
        return count(start, "/houses/");
    }

    private static int count(StructureStart start, String marker) {
        int n = 0;
        for (StructurePiece piece : start.getPieces()) {
            if (piece.toString().contains(marker)) {
                n++;
            }
        }
        return n;
    }

    /**
     * The terrain plan of the city nearest (x, z): plate height, then each side's edge stretches as one letter each
     * (C carve, F fill, V valley), west to east or north to south, and how far above the plate each stretch's carve
     * has to reach.
     */
    public static List<String> describeTerrain(ServerLevel level, WorldSettings settings, int x, int z) {
        int r = 3 * CityLayout.SIZE;
        Optional<CityLayout.Site> nearest = CityLayout.allSitesInBox(level.getSeed(), settings, x - r, z - r, x + r, z + r).stream()
                .min(java.util.Comparator.comparingLong(s -> (long) (s.x() - x) * (s.x() - x) + (long) (s.z() - z) * (s.z() - z)));
        if (nearest.isEmpty()) {
            return List.of("No city within " + r + " blocks");
        }
        CityLayout.Site site = nearest.get();
        CityTerrain.Plan plan = CityGridBuilder.terrainPlan(context(level, new ChunkPos(site.x() >> 4, site.z() >> 4)), site);
        List<String> lines = new ArrayList<>();
        lines.add(String.format("%s at %d, %d: plate y %d", site.capital() ? "Capital city" : "City", site.x(), site.z(), plan.plateY()));
        for (CityTerrain.Side side : CityTerrain.Side.values()) {
            StringBuilder sb = new StringBuilder();
            StringBuilder carve = new StringBuilder();
            for (int s = 0; s < CityTerrain.SEGMENTS_PER_SIDE; s++) {
                sb.append(plan.edge(side, s).name().charAt(0));
                carve.append(' ').append(plan.carveTo()[side.ordinal()][s]);
            }
            lines.add(side + ": " + sb + "  carve to:" + carve);
        }
        return lines;
    }

    private static Structure.GenerationContext context(ServerLevel level, ChunkPos chunk) {
        ServerChunkCache chunks = level.getChunkSource();
        return new Structure.GenerationContext(level.registryAccess(), chunks.getGenerator(), chunks.getGenerator().getBiomeSource(),
                chunks.randomState(), level.getStructureManager(), level.getSeed(), chunk, level, b -> true);
    }

    private static StructureStart generate(ServerLevel level, Structure structure, ChunkPos chunk) {
        ServerChunkCache chunks = level.getChunkSource();
        return structure.generate(level.registryAccess(), chunks.getGenerator(), chunks.getGenerator().getBiomeSource(), chunks.randomState(),
                level.getStructureManager(), level.getSeed(), chunk, 0, level, b -> true);
    }
}
