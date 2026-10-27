package rafasconquest.client;

import com.talhanation.recruits.client.gui.faction.FactionMainScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
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
    }
}
