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
    /** Oar blocks: spruce planks with an oar sticking out of the left or right side. */
    public static final DeferredHolder<Block, Block> OAR_LEFT = BLOCKS.register("oar_left",
            () -> new OarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS), false));
    public static final DeferredHolder<Item, Item> OAR_LEFT_ITEM = ITEMS.register("oar_left",
            () -> new BlockItem(OAR_LEFT.get(), new Item.Properties()));
    public static final DeferredHolder<Block, Block> OAR_RIGHT = BLOCKS.register("oar_right",
            () -> new OarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS), true));
    public static final DeferredHolder<Item, Item> OAR_RIGHT_ITEM = ITEMS.register("oar_right",
            () -> new BlockItem(OAR_RIGHT.get(), new Item.Properties()));
    public static final DeferredHolder<net.minecraft.world.level.block.entity.BlockEntityType<?>, net.minecraft.world.level.block.entity.BlockEntityType<Oar>> OAR_ENTITY =
            BLOCK_ENTITIES.register("oar", () -> net.minecraft.world.level.block.entity.BlockEntityType.Builder
                    .of(Oar::new, OAR_LEFT.get(), OAR_RIGHT.get()).build(null));
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
    /** Gate doors' planks: with Sable, a door made of them swings through the wall and the other door (null without Sable). */
    public static final DeferredHolder<Block, Block> GATE_PLANKS = net.neoforged.fml.ModList.get() != null && net.neoforged.fml.ModList.get().isLoaded("sable")
            ? BLOCKS.register("gate_planks", () -> new rafasconquest.bridge.GatePlanksBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)))
            : null;
    public static final DeferredHolder<Item, Item> GATE_PLANKS_ITEM = GATE_PLANKS == null ? null
            : ITEMS.register("gate_planks", () -> new BlockItem(GATE_PLANKS.get(), new Item.Properties()));
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
    /** Battle banners: banners flying their nation's arms in cloth that moves; the big ones twice as tall and wide. */
    public static final DeferredHolder<Block, Block> BATTLE_BANNER = BLOCKS.register("battle_banner",
            () -> new BattleBannerBlock(bannerProperties(), false));
    public static final DeferredHolder<Block, Block> BATTLE_WALL_BANNER = BLOCKS.register("battle_wall_banner",
            () -> new BattleWallBannerBlock(bannerProperties().lootFrom(BATTLE_BANNER), false));
    public static final DeferredHolder<Block, Block> BIG_BATTLE_BANNER = BLOCKS.register("big_battle_banner",
            () -> new BattleBannerBlock(bannerProperties(), true));
    public static final DeferredHolder<Block, Block> BIG_BATTLE_WALL_BANNER = BLOCKS.register("big_battle_wall_banner",
            () -> new BattleWallBannerBlock(bannerProperties().lootFrom(BIG_BATTLE_BANNER), true));
    public static final DeferredHolder<Item, Item> BATTLE_BANNER_ITEM = ITEMS.register("battle_banner",
            () -> new net.minecraft.world.item.StandingAndWallBlockItem(BATTLE_BANNER.get(), BATTLE_WALL_BANNER.get(),
                    new Item.Properties().stacksTo(16), net.minecraft.core.Direction.DOWN));
    public static final DeferredHolder<Item, Item> BIG_BATTLE_BANNER_ITEM = ITEMS.register("big_battle_banner",
            () -> new net.minecraft.world.item.StandingAndWallBlockItem(BIG_BATTLE_BANNER.get(), BIG_BATTLE_WALL_BANNER.get(),
                    new Item.Properties().stacksTo(16), net.minecraft.core.Direction.DOWN));
    public static final DeferredHolder<net.minecraft.world.level.block.entity.BlockEntityType<?>, net.minecraft.world.level.block.entity.BlockEntityType<BattleBannerBlockEntity>> BATTLE_BANNER_ENTITY =
            BLOCK_ENTITIES.register("battle_banner", () -> net.minecraft.world.level.block.entity.BlockEntityType.Builder
                    .of(BattleBannerBlockEntity::new, BATTLE_BANNER.get(), BATTLE_WALL_BANNER.get(), BIG_BATTLE_BANNER.get(), BIG_BATTLE_WALL_BANNER.get())
                    .build(null));
    /** The battle standard: a battle banner twice the size, held or worn on a mob's head. */
    public static final DeferredHolder<Item, Item> BATTLE_STANDARD = ITEMS.register("battle_standard",
            () -> new BattleStandardItem(new Item.Properties().stacksTo(16)));
    /** The cape: battle-banner cloth on the wearer's back (a Curios slot), one dyed colour (tag minecraft:dyeable). */
    public static final DeferredHolder<Item, Item> CAPE = ITEMS.register("cape",
            () -> new Item(new Item.Properties().stacksTo(1)));

    private static BlockBehaviour.Properties bannerProperties() {
        return BlockBehaviour.Properties.of().mapColor(net.minecraft.world.level.material.MapColor.WOOD).forceSolidOn()
                .instrument(net.minecraft.world.level.block.state.properties.NoteBlockInstrument.BASS).noCollission()
                .strength(1.0F).sound(net.minecraft.world.level.block.SoundType.WOOD).ignitedByLava();
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =TABS.register("main", () -> CreativeModeTab.builder()
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
                output.accept(OAR_LEFT_ITEM.get());
                output.accept(OAR_RIGHT_ITEM.get());
                output.accept(PATHFINDING_BLOCK_ITEM.get());
                output.accept(GATE_BLOCK_ITEM.get());
                output.accept(BATTLE_BANNER_ITEM.get());
                output.accept(BIG_BATTLE_BANNER_ITEM.get());
                output.accept(BATTLE_STANDARD.get());
                output.accept(CAPE.get());
                if (GATE_PLANKS_ITEM != null) {
                    output.accept(GATE_PLANKS_ITEM.get());
                }
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
