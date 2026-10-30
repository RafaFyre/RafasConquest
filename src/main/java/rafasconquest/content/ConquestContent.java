package rafasconquest.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rafasconquest.RafasConquest;

/** The mod's blocks, items and creative tab. */
public final class ConquestContent {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RafasConquest.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RafasConquest.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RafasConquest.MOD_ID);

    public static final RegistryObject<Block> COMMAND_TABLE = BLOCKS.register("command_table",
            () -> new CommandTableBlock(BlockBehaviour.Properties.copy(Blocks.LECTERN)));
    public static final RegistryObject<Item> COMMAND_TABLE_ITEM = ITEMS.register("command_table",
            () -> new BlockItem(COMMAND_TABLE.get(), new Item.Properties()));
    /** Slabs for easing the roads up and down: gravel and dirt path, which vanilla has none of. */
    public static final RegistryObject<Block> GRAVEL_SLAB = BLOCKS.register("gravel_slab",
            () -> new net.minecraft.world.level.block.SlabBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistryObject<Item> GRAVEL_SLAB_ITEM = ITEMS.register("gravel_slab",
            () -> new BlockItem(GRAVEL_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> DIRT_PATH_SLAB = BLOCKS.register("dirt_path_slab",
            () -> new net.minecraft.world.level.block.SlabBlock(BlockBehaviour.Properties.copy(Blocks.DIRT_PATH)));
    public static final RegistryObject<Item> DIRT_PATH_SLAB_ITEM = ITEMS.register("dirt_path_slab",
            () -> new BlockItem(DIRT_PATH_SLAB.get(), new Item.Properties()));
    /** A quest board, in every recruitment post: the city's quests. */
    public static final RegistryObject<Block> QUEST_BOARD = BLOCKS.register("quest_board",
            () -> new QuestBoardBlock(BlockBehaviour.Properties.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final RegistryObject<Item> QUEST_BOARD_ITEM = ITEMS.register("quest_board",
            () -> new BlockItem(QUEST_BOARD.get(), new Item.Properties()));
    public static final RegistryObject<Item> WORLD_MAP = ITEMS.register("world_map",
            () -> new WorldMapItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.rafasconquest"))
            .icon(() -> COMMAND_TABLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(COMMAND_TABLE_ITEM.get());
                output.accept(WORLD_MAP.get());
                output.accept(QUEST_BOARD_ITEM.get());
                output.accept(GRAVEL_SLAB_ITEM.get());
                output.accept(DIRT_PATH_SLAB_ITEM.get());
            })
            .build());

    private ConquestContent() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TABS.register(modBus);
    }
}
