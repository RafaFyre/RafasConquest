package rafasconquest.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rafasconquest.RafasConquest;

/** The mod's own entities: the townsfolk of the cities. */
public final class ConquestEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RafasConquest.MOD_ID);

    /**
     * Never saved; sent to clients within 64 blocks; their position sent every third tick (the client smooths between),
     * as they only stroll.
     */
    public static final RegistryObject<EntityType<Townsperson>> TOWNSPERSON = ENTITY_TYPES.register("townsperson",
            () -> EntityType.Builder.<Townsperson>of(Townsperson::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .noSave()
                    .noSummon()
                    .clientTrackingRange(4)
                    .updateInterval(3)
                    .build(RafasConquest.MOD_ID + ":townsperson"));

    /** A recruitment post's recruiter: kept with the world, sent to clients within 64 blocks. */
    public static final RegistryObject<EntityType<Recruiter>> RECRUITER = ENTITY_TYPES.register("recruiter",
            () -> EntityType.Builder.<Recruiter>of(Recruiter::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(4)
                    .build(RafasConquest.MOD_ID + ":recruiter"));

    private ConquestEntities() {
    }

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
        modBus.addListener(ConquestEntities::attributes);
    }

    private static void attributes(EntityAttributeCreationEvent event) {
        event.put(TOWNSPERSON.get(), Townsperson.attributes().build());
        event.put(RECRUITER.get(), Recruiter.attributes().build());
    }
}
