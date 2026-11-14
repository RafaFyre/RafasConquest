package rafasconquest.client;

import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import rafasconquest.RafasConquest;

@EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(DistantHorizonsLink::link);
        if (net.neoforged.fml.ModList.get().isLoaded("curios")) {
            event.enqueueWork(rafasconquest.client.cloth.CapeCurio::register);
        }
    }

    /** Battle banners, the standard and the cape draw their cloth as items too. */
    @SubscribeEvent
    public static void onClientExtensions(net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent event) {
        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions cloth = new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private rafasconquest.client.cloth.ClothItemRenderer renderer;

            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new rafasconquest.client.cloth.ClothItemRenderer();
                }
                return renderer;
            }
        };
        event.registerItem(cloth, rafasconquest.content.ConquestContent.BATTLE_BANNER_ITEM.get(),
                rafasconquest.content.ConquestContent.BIG_BATTLE_BANNER_ITEM.get(),
                rafasconquest.content.ConquestContent.BATTLE_STANDARD.get(), rafasconquest.content.ConquestContent.CAPE.get());
    }

    @SubscribeEvent
    public static void onRegisterShaders(net.neoforged.neoforge.client.event.RegisterShadersEvent event) throws java.io.IOException {
        DistantOcclusion.registerShaders(event);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(rafasconquest.npc.ConquestEntities.TOWNSPERSON.get(), TownspersonRenderer::new);
        event.registerEntityRenderer(rafasconquest.npc.ConquestEntities.RECRUITER.get(), RecruiterRenderer::new);
        event.registerBlockEntityRenderer(rafasconquest.content.ConquestContent.OAR_ENTITY.get(), OarRenderer::new);
        event.registerBlockEntityRenderer(rafasconquest.content.ConquestContent.MARKER_ENTITY.get(), MarkerRenderer::new);
        event.registerBlockEntityRenderer(rafasconquest.content.ConquestContent.GATE_ENTITY.get(), GateRenderer::new);
        event.registerBlockEntityRenderer(rafasconquest.content.ConquestContent.BATTLE_BANNER_ENTITY.get(),
                rafasconquest.client.cloth.BattleBannerRenderer::new);
    }

    /** Generals are Recruits' plain recruits; their back banner is a layer on that renderer (either look). */
    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (event.getRenderer(ModEntityTypes.RECRUIT.get()) instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel) {
            renderer.addLayer(new BackBannerLayer(renderer, event.getContext().getItemInHandRenderer()));
            renderer.addLayer(new MouthLayer(renderer)); // the war council, as they speak
        }
    }
}
