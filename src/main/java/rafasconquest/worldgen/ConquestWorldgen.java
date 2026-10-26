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
    public static final RegistryObject<StructureType<CapitalStructure>> CAPITAL_STRUCTURE = STRUCTURE_TYPES.register("capital", () -> () -> CapitalStructure.CODEC);

    public static final RegistryObject<StructurePlacementType<FarmPlacement>> FARM_PLACEMENT = PLACEMENT_TYPES.register("farm_spread", () -> () -> FarmPlacement.CODEC);
    public static final RegistryObject<StructurePlacementType<CapitalPlacement>> CAPITAL_PLACEMENT = PLACEMENT_TYPES.register("capital_sites", () -> () -> CapitalPlacement.CODEC);

    public static final RegistryObject<StructurePieceType> FARM_PIECE = PIECE_TYPES.register("farm", () -> (context, tag) -> new FarmPiece(tag));
    public static final RegistryObject<StructurePieceType> CAPITAL_PIECE = PIECE_TYPES.register("capital", () -> (context, tag) -> new CapitalPiece(tag));

    public static final ResourceKey<Structure> FARM = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "farm"));
    public static final ResourceKey<Structure> CAPITAL = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "capital"));

    private ConquestWorldgen() {
    }

    public static void register(IEventBus modBus) {
        STRUCTURE_TYPES.register(modBus);
        PLACEMENT_TYPES.register(modBus);
        PIECE_TYPES.register(modBus);
    }
}
