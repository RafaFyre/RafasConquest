package rafasconquest.client;

import com.talhanation.recruits.client.gui.faction.FactionMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rafasconquest.RafasConquest;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.client.map.FactionMapScreen;

@Mod.EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEvents {
    /** Size of Recruits' faction menu, which centres itself the same way. */
    private static final int MENU_WIDTH = 246;
    private static final int MENU_HEIGHT = 84;

    private ClientEvents() {
    }

    /**
     * Adds a second row to Recruits' faction menu, lined up under its own two buttons: Faction Info and World Map.
     */
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof FactionMainScreen menu) {
            int left = (menu.width - MENU_WIDTH) / 2;
            int top = (menu.height - MENU_HEIGHT) / 2;
            event.addListener(Button.builder(Component.translatable("gui.rafasconquest.faction_info"),
                            button -> menu.getMinecraft().setScreen(new FactionInfoScreen(menu)))
                    .bounds(left + 20, top + 55, 100, 20)
                    .build());
            event.addListener(Button.builder(Component.translatable("gui.rafasconquest.map"),
                            button -> menu.getMinecraft().setScreen(new FactionMapScreen(menu)))
                    .bounds(left + 130, top + 55, 100, 20)
                    .build());
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientMapCache.clear();
        BackBanners.clear();
        DistantArmies.clear();
        BattleSounds.clear();
        BattleSpotRing.clear();
    }

    /**
     * Distant battle formations, drawn after the world so its terrain hides any behind hills: after the tripwire
     * layer, the last of it, because Distant Horizons fades the world into its own terrain just before that layer and
     * would fade them too. Those in Distant Horizons' terrain are moved before the world is drawn.
     */
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            DistantArmyRenderer.prepare(event.getPartialTick());
            DistantHorizonsLink.update();
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            DistantArmyRenderer.render(event.getPoseStack(), event.getCamera(), event.getPartialTick());
            BattleSpotRing.render(event.getPoseStack(), event.getCamera());
        }
    }

    /** The sound of the battle as a whole: hoofbeats and the din of the front. */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BattleSounds.tick();
        }
    }

    /** A battle soldier (or its horse) past the handover is hidden; its figure stands in for it. */
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        Vec3 eye = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        if (DistantArmyRenderer.hides(event.getEntity(), eye, event.getPartialTick())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            BackBanners.remove(event.getEntity().getId());
        }
    }
}
