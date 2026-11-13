package rafasconquest.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import rafasconquest.RafasConquest;

/**
 * The grapple hook: an item, and the hook a boarding ship's crew throw onto an enemy ship's deck ({@link GrappleHook}),
 * its line hauling the two ships together. Only for boarding: the item does nothing in a player's hand.
 */
public final class GrappleHooks {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, RafasConquest.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, RafasConquest.MOD_ID);

    public static final DeferredHolder<Item, Item> ITEM = ITEMS.register("grapple_hook", () -> new Item(new Item.Properties().stacksTo(16)));

    /** Never saved (a battle throws its own); sent to clients within 128 blocks, its place every tick, as ships move. */
    public static final DeferredHolder<EntityType<?>, EntityType<GrappleHook>> HOOK = ENTITY_TYPES.register("grapple_hook",
            () -> EntityType.Builder.<GrappleHook>of(GrappleHook::new, MobCategory.MISC)
                    .sized(0.4F, 0.4F)
                    .noSave()
                    .noSummon()
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(RafasConquest.MOD_ID + ":grapple_hook"));

    private GrappleHooks() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        ENTITY_TYPES.register(modBus);
        modBus.addListener(GrappleHooks::tab);
    }

    private static void tab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ConquestContent.TAB.getKey())) {
            event.accept(ITEM.get());
        }
    }
}
