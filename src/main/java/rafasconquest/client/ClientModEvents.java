package rafasconquest.client;

import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rafasconquest.RafasConquest;

@Mod.EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    /** Generals are Recruits' plain recruits; their back banner is a layer on that renderer (either look). */
    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = event.getRenderer(ModEntityTypes.RECRUIT.get());
        if (renderer != null && renderer.getModel() instanceof HumanoidModel) {
            renderer.addLayer(new BackBannerLayer(renderer, event.getContext().getItemInHandRenderer()));
        }
    }
}
