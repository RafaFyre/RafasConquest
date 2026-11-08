package rafasconquest.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import rafasconquest.RafasConquest;

/** The mod's blocks, items and creative tab. */
public final class ConquestContent {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, RafasConquest.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, RafasConquest.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RafasConquest.MOD_ID);
    private static final DeferredRegister<net.minecraft.world.level.block.entity.BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RafasConquest.MOD_ID);

    public static final DeferredHolder<Block, Block> COMMAND_TABLE = BLOCKS.register("command_table",
            () -> new CommandTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LECTERN)));
    public static final DeferredHolder<Item, Item> COMMAND_TABLE_ITEM = ITEMS.register("command_table",
            () -> new BlockItem(COMMAND_TABLE.get(), new Item.Properties()));
    /** Slabs for easing the roads up and down: gravel and dirt path, which vanilla has none of. */
    public static final DeferredHolder<Block, Block> GRAVEL_SLAB = BLOCKS.register("gravel_slab",
            () -> new net.minecraft.world.level.block.SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final DeferredHolder<Item, Item> GRAVEL_SLAB_ITEM = ITEMS.register("gravel_slab",
            () -> new BlockItem(GRAVEL_SLAB.get(), new Item.Properties()));
    public static final DeferredHolder<Block, Block> DIRT_PATH_SLAB = BLOCKS.register("dirt_path_slab",
            () -> new net.minecraft.world.level.block.SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT_PATH)));
    public static final DeferredHolder<Item, Item> DIRT_PATH_SLAB_ITEM = ITEMS.register("dirt_path_slab",
            () -> new BlockItem(DIRT_PATH_SLAB.get(), new Item.Properties()));
    /** A quest board, in every barracks: the city's quests. */
    public static final DeferredHolder<Block, Block> QUEST_BOARD = BLOCKS.register("quest_board",
            () -> new QuestBoardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARTOGRAPHY_TABLE)));
    public static final DeferredHolder<Item, Item> QUEST_BOARD_ITEM = ITEMS.register("quest_board",
            () -> new BlockItem(QUEST_BOARD.get(), new Item.Properties()));
    /** The Grand Strategy table: the player's fief over the world map. */
    public static final DeferredHolder<Block, Block> GRAND_STRATEGY = BLOCKS.register("grand_strategy",
            () -> new GrandStrategyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARTOGRAPHY_TABLE)));
    public static final DeferredHolder<Item, Item> GRAND_STRATEGY_ITEM = ITEMS.register("grand_strategy",
            () -> new BlockItem(GRAND_STRATEGY.get(), new Item.Properties()));
    /** The Strategy Board: a red cartography table that opens the battle screen. */
    public static final DeferredHolder<Block, Block> STRATEGY_BOARD = BLOCKS.register("strategy_board",
            () -> new StrategyBoardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARTOGRAPHY_TABLE)));
    public static final DeferredHolder<Item, Item> STRATEGY_BOARD_ITEM = ITEMS.register("strategy_board",
            () -> new BlockItem(STRATEGY_BOARD.get(), new Item.Properties()));
    /** The Character Marker: blue bedrock, set in a style's structures where people are to stand. */
    public static final DeferredHolder<Block, Block> MARKER = BLOCKS.register("character_marker",
            () -> new rafasconquest.style.MarkerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).noLootTable()));
    public static final DeferredHolder<Item, Item> MARKER_ITEM = ITEMS.register("character_marker",
            () -> new BlockItem(MARKER.get(), new Item.Properties()));
    public static final DeferredHolder<net.minecraft.world.level.block.entity.BlockEntityType<?>, net.minecraft.world.level.block.entity.BlockEntityType<rafasconquest.style.MarkerBlockEntity>> MARKER_ENTITY =
            BLOCK_ENTITIES.register("character_marker", () -> net.minecraft.world.level.block.entity.BlockEntityType.Builder
                    .of(rafasconquest.style.MarkerBlockEntity::new, MARKER.get()).build(null));
    /** The Gate Block: a black lectern that works a gate's two doors, Sable ships on hinges. */
    public static final DeferredHolder<Block, Block> GATE_BLOCK = BLOCKS.register("gate_block",
            () -> new GateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LECTERN).mapColor(net.minecraft.world.level.material.MapColor.COLOR_BLACK)));
    public static final DeferredHolder<Item, Item> GATE_BLOCK_ITEM = ITEMS.register("gate_block",
            () -> new BlockItem(GATE_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<net.minecraft.world.level.block.entity.BlockEntityType<?>, net.minecraft.world.level.block.entity.BlockEntityType<GateBlockEntity>> GATE_ENTITY =
            BLOCK_ENTITIES.register("gate_block", () -> net.minecraft.world.level.block.entity.BlockEntityType.Builder
                    .of(GateBlockEntity::new, GATE_BLOCK.get()).build(null));
    public static final DeferredHolder<Item, Item> WORLD_MAP = ITEMS.register("world_map",
            () -> new WorldMapItem(new Item.Properties().stacksTo(1)));

    /** Green obsidian: marks where ships dock at a pier (for managing docked ships later). */
    public static final DeferredHolder<Block, Block> DOCK_BLOCK = BLOCKS.register("dock_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).mapColor(net.minecraft.world.level.material.MapColor.EMERALD)));
    public static final DeferredHolder<Item, Item> DOCK_BLOCK_ITEM = ITEMS.register("dock_block",
            () -> new BlockItem(DOCK_BLOCK.get(), new Item.Properties()));

    /** Golden cobblestone: placed in a special structure to mark its entrance, and so its front. */
    public static final DeferredHolder<Block, Block> PATHFINDING_BLOCK = BLOCKS.register("pathfinding_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).mapColor(net.minecraft.world.level.material.MapColor.GOLD)));
    public static final DeferredHolder<Item, Item> PATHFINDING_BLOCK_ITEM = ITEMS.register("pathfinding_block",
            () -> new BlockItem(PATHFINDING_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.rafasconquest"))
            .icon(() -> COMMAND_TABLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(COMMAND_TABLE_ITEM.get());
                output.accept(WORLD_MAP.get());
                output.accept(QUEST_BOARD_ITEM.get());
                output.accept(GRAND_STRATEGY_ITEM.get());
                output.accept(STRATEGY_BOARD_ITEM.get());
                output.accept(GRAVEL_SLAB_ITEM.get());
                output.accept(DIRT_PATH_SLAB_ITEM.get());
                output.accept(MARKER_ITEM.get());
                output.accept(DOCK_BLOCK_ITEM.get());
                output.accept(PATHFINDING_BLOCK_ITEM.get());
                output.accept(GATE_BLOCK_ITEM.get());
            })
            .build());

    private ConquestContent() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TABS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }
}
