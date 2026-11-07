package rafasconquest.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import rafasconquest.RafasConquest;

public final class ConquestWorldgen {
    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, RafasConquest.MOD_ID);
    private static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, RafasConquest.MOD_ID);
    private static final DeferredRegister<StructurePieceType> PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, RafasConquest.MOD_ID);
    private static final DeferredRegister<net.minecraft.world.level.levelgen.feature.Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, RafasConquest.MOD_ID);

    /** The roads, built into each chunk ({@link RoadFeature}). */
    public static final DeferredHolder<net.minecraft.world.level.levelgen.feature.Feature<?>, RoadFeature> ROADS = FEATURES.register("roads",
            () -> new RoadFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<FarmStructure>> FARM_STRUCTURE = STRUCTURE_TYPES.register("farm", () -> () -> FarmStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<CityStructure>> CITY_STRUCTURE = STRUCTURE_TYPES.register("city", () -> () -> CityStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<CityDistrictStructure>> CITY_DISTRICT_STRUCTURE = STRUCTURE_TYPES.register("city_district", () -> () -> CityDistrictStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<CitySlopeStructure>> CITY_SLOPE_STRUCTURE = STRUCTURE_TYPES.register("city_slope", () -> () -> CitySlopeStructure.CODEC);

    public static final DeferredHolder<StructureType<?>, StructureType<CountryStructure>> COUNTRY_STRUCTURE = STRUCTURE_TYPES.register("countryside", () -> () -> CountryStructure.CODEC);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<CountryPlacement>> COUNTRY_PLACEMENT = PLACEMENT_TYPES.register("countryside_sites", () -> () -> CountryPlacement.CODEC);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> COUNTRY_PIECE = PIECE_TYPES.register("countryside", () -> (context, tag) -> new CountryPiece(tag));
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<FarmPlacement>> FARM_PLACEMENT = PLACEMENT_TYPES.register("farm_spread", () -> () -> FarmPlacement.CODEC);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<DojoPlacement>> DOJO_PLACEMENT = PLACEMENT_TYPES.register("dojo_sites", () -> () -> DojoPlacement.CODEC);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<CityPlacement>> CITY_PLACEMENT = PLACEMENT_TYPES.register("city_sites", () -> () -> CityPlacement.CENTER_CODEC);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<CityPlacement>> CITY_DISTRICT_PLACEMENT = PLACEMENT_TYPES.register("city_district_sites", () -> () -> CityPlacement.DISTRICT_CODEC);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<CityPlacement>> CITY_SLOPE_PLACEMENT = PLACEMENT_TYPES.register("city_slope_sites", () -> () -> CityPlacement.SLOPE_CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> FARM_PIECE = PIECE_TYPES.register("farm", () -> (context, tag) -> new FarmPiece(tag));

    public static final DeferredHolder<StructurePieceType, StructurePieceType> CITY_ROAD_PIECE = PIECE_TYPES.register("city_road", () -> (context, tag) -> new CityRoadPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> HARBOUR_PIECE = PIECE_TYPES.register("harbour", () -> (context, tag) -> new HarbourPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> HARBOUR_CUT_PIECE = PIECE_TYPES.register("harbour_cut", () -> (context, tag) -> new HarbourCutPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CITY_TERRAIN_PIECE =PIECE_TYPES.register("city_terrain", () -> (context, tag) -> new CityTerrainPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> BARRACKS_PIECE = PIECE_TYPES.register("recruitment_post", // the old name, kept so saved worlds load
            () -> (context, tag) -> new BarracksPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> NOBLE_PATH_PIECE = PIECE_TYPES.register("noble_path",
            () -> (context, tag) -> new NoblePathPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> GOLD_CHURCH_PIECE = PIECE_TYPES.register("gold_church",
            () -> (context, tag) -> new GoldChurchPiece(tag));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> STYLE_PIECE = PIECE_TYPES.register("style",
            () -> (context, tag) -> new rafasconquest.style.StylePiece(tag));

    public static final ResourceKey<Structure> FARM = key("farm");
    public static final ResourceKey<Structure> CITY = key("city");
    public static final ResourceKey<Structure> CITY_DISTRICT = key("city_district");
    public static final ResourceKey<Structure> CITY_SLOPE = key("city_slope");
    public static final ResourceKey<Structure> DOJO = key("dojo");
    public static final ResourceKey<Structure> HIDDEN_DOJO = key("hidden_dojo");

    private ConquestWorldgen() {
    }

    private static ResourceKey<Structure> key(String path) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, path));
    }

    public static void register(IEventBus modBus) {
        STRUCTURE_TYPES.register(modBus);
        PLACEMENT_TYPES.register(modBus);
        PIECE_TYPES.register(modBus);
        FEATURES.register(modBus);
    }
}
