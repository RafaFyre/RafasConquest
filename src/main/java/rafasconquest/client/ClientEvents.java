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

    /** The world creation screen last opened, so its RafasConquest options start from the defaults only once. */
    private static net.minecraft.client.gui.screens.Screen worldBeingCreated;

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
        if (event.getScreen() instanceof net.minecraft.client.gui.screens.worldselection.CreateWorldScreen create) {
            if (create != worldBeingCreated) {
                // A new world: its RafasConquest options start from the defaults.
                worldBeingCreated = create;
                rafasconquest.worldgen.HorizontalStretch.setPending(rafasconquest.worldgen.HorizontalStretch.defaultChoice());
            }
            event.addListener(Button.builder(Component.literal("RafasConquest"),
                            button -> create.getMinecraft().setScreen(new ConquestWorldScreen(create)))
                    .bounds(create.width - 98, 30, 90, 20)
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
        ArrowVolleys.clear();
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
            ArrowVolleys.render(event.getPoseStack(), event.getCamera());
            BattleSpotRing.render(event.getPoseStack(), event.getCamera(), event.getPartialTick());
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

    /** Right-clicking the war horn opens its screen instead of blowing it. */
    @SubscribeEvent
    public static void onRightClickItem(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide() && rafasconquest.bridge.WarHorn.is(event.getItemStack())) {
            event.setCanceled(true);
            event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
            if (Minecraft.getInstance().screen == null) {
                HornScreen.open();
            }
        }
    }

    /**
     * A swing that meets no entity may meet a figure: a soldier the battle keeps as numbers, drawn by {@link
     * DistantArmyRenderer}. The nearest figure along the player's line of sight within reach is struck; the server
     * decides what the blow does.
     */
    @SubscribeEvent
    public static void onAttack(net.minecraftforge.client.event.InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (!event.isAttack() || mc.player == null || mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.ENTITY) {
            return;
        }
        Vec3 eye = mc.player.getEyePosition();
        Vec3 look = mc.player.getViewVector(1);
        double reach = mc.player.getVehicle() != null ? 5 : 3.5;
        double best = reach;
        int formation = -1;
        double fx = 0;
        double fz = 0;
        for (rafasconquest.battle.FormationSnapshot f : DistantArmies.now()) {
            if (Math.hypot(f.x() - eye.x, f.z() - eye.z) > 40) {
                continue;
            }
            for (rafasconquest.battle.FormationSnapshot.Sprite s : f.place(DistantArmies.positions(1))) {
                if (s.real()) {
                    continue;
                }
                Vec3 middle = new Vec3(s.x(), s.y() + 1, s.z());
                Vec3 to = middle.subtract(eye);
                double along = to.dot(look);
                if (along < 0 || along > best) {
                    continue;
                }
                if (to.subtract(look.scale(along)).length() <= 0.8) {
                    best = along;
                    formation = f.id();
                    fx = s.x();
                    fz = s.z();
                }
            }
        }
        if (formation >= 0) {
            rafasconquest.net.ConquestNetwork.sendFigureStrike(formation, fx, fz);
        }
    }

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            BackBanners.remove(event.getEntity().getId());
        }
    }
}
