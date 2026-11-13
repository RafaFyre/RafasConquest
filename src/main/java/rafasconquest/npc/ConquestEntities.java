package rafasconquest.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import rafasconquest.RafasConquest;

/** The mod's own entities: the townsfolk of the cities. */
public final class ConquestEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, RafasConquest.MOD_ID);

    /**
     * Never saved; sent to clients within 64 blocks; their position sent every third tick (the client smooths between),
     * as they only stroll.
     */
    public static final DeferredHolder<EntityType<?>, EntityType<Townsperson>> TOWNSPERSON = ENTITY_TYPES.register("townsperson",
            () -> EntityType.Builder.<Townsperson>of(Townsperson::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .noSave()
                    .noSummon()
                    .clientTrackingRange(4)
                    .updateInterval(3)
                    .build(RafasConquest.MOD_ID + ":townsperson"));

    /** A barracks' recruiter: kept with the world, sent to clients within 64 blocks. */
    public static final DeferredHolder<EntityType<?>, EntityType<Recruiter>> RECRUITER = ENTITY_TYPES.register("recruiter",
            () -> EntityType.Builder.<Recruiter>of(Recruiter::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(4)
                    .build(RafasConquest.MOD_ID + ":recruiter"));

    /** An oar of vanilla blocks; kept with the world, its turns sent every tick to clients within 128 blocks. */
    public static final DeferredHolder<EntityType<?>, EntityType<rafasconquest.content.Oar>> OAR = ENTITY_TYPES.register("oar",
            () -> EntityType.Builder.<rafasconquest.content.Oar>of(rafasconquest.content.Oar::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(RafasConquest.MOD_ID + ":oar"));

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
