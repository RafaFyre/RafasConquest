package rafasconquest.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import rafasconquest.RafasConquest;

/** The mod's own entities: the barracks' recruiters. */
public final class ConquestEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, RafasConquest.MOD_ID);

    /** A barracks' recruiter: kept with the world, sent to clients within 64 blocks. */
    public static final DeferredHolder<EntityType<?>, EntityType<Recruiter>> RECRUITER = ENTITY_TYPES.register("recruiter",
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
        event.put(RECRUITER.get(), Recruiter.attributes().build());
    }
}
