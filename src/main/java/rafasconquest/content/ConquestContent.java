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
    public static final RegistryObject<Item> WORLD_MAP = ITEMS.register("world_map",
            () -> new WorldMapItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.rafasconquest"))
            .icon(() -> COMMAND_TABLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(COMMAND_TABLE_ITEM.get());
                output.accept(WORLD_MAP.get());
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
