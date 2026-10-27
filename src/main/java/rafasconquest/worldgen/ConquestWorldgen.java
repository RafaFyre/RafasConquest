package rafasconquest.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import rafasconquest.RafasConquest;

public final class ConquestWorldgen {
    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, RafasConquest.MOD_ID);
    private static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, RafasConquest.MOD_ID);
    private static final DeferredRegister<StructurePieceType> PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, RafasConquest.MOD_ID);

    public static final RegistryObject<StructureType<FarmStructure>> FARM_STRUCTURE = STRUCTURE_TYPES.register("farm", () -> () -> FarmStructure.CODEC);
    public static final RegistryObject<StructureType<CityStructure>> CITY_STRUCTURE = STRUCTURE_TYPES.register("city", () -> () -> CityStructure.CODEC);
    public static final RegistryObject<StructureType<CityDistrictStructure>> CITY_DISTRICT_STRUCTURE = STRUCTURE_TYPES.register("city_district", () -> () -> CityDistrictStructure.CODEC);

    public static final RegistryObject<StructurePlacementType<FarmPlacement>> FARM_PLACEMENT = PLACEMENT_TYPES.register("farm_spread", () -> () -> FarmPlacement.CODEC);
    public static final RegistryObject<StructurePlacementType<CityPlacement>> CITY_PLACEMENT = PLACEMENT_TYPES.register("city_sites", () -> () -> CityPlacement.CENTER_CODEC);
    public static final RegistryObject<StructurePlacementType<CityPlacement>> CITY_DISTRICT_PLACEMENT = PLACEMENT_TYPES.register("city_district_sites", () -> () -> CityPlacement.DISTRICT_CODEC);

    public static final RegistryObject<StructurePieceType> FARM_PIECE = PIECE_TYPES.register("farm", () -> (context, tag) -> new FarmPiece(tag));

    public static final RegistryObject<StructurePieceType> CITY_ROAD_PIECE = PIECE_TYPES.register("city_road", () -> (context, tag) -> new CityRoadPiece(tag));
    public static final RegistryObject<StructurePieceType> CITY_FILL_PIECE = PIECE_TYPES.register("city_fill", () -> (context, tag) -> new CityFillPiece(tag));

    public static final ResourceKey<Structure> FARM = key("farm");
    public static final ResourceKey<Structure> CITY = key("city");
    public static final ResourceKey<Structure> CITY_DISTRICT = key("city_district");

    private ConquestWorldgen() {
    }

    private static ResourceKey<Structure> key(String path) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, path));
    }

    public static void register(IEventBus modBus) {
        STRUCTURE_TYPES.register(modBus);
        PLACEMENT_TYPES.register(modBus);
        PIECE_TYPES.register(modBus);
    }
}
