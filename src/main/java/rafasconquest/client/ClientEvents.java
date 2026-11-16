package rafasconquest.client;

import com.talhanation.recruits.client.gui.faction.FactionMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import rafasconquest.RafasConquest;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.client.map.FactionMapScreen;

@EventBusSubscriber(modid = RafasConquest.MOD_ID, value = Dist.CLIENT)
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
        if (event.getScreen() instanceof net.minecraft.client.gui.screens.inventory.InventoryScreen inventory) {
            event.addListener(new InventoryStatsButton(inventory)); // the player's stats
        }
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

    /** The world map, asked for on joining: the sea lanes in it are what distant ships are drawn from, map opened or not. */
    @SubscribeEvent
    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        rafasconquest.net.ConquestNetwork.requestBaseMap();
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientMapCache.clear();
        BackBanners.clear();
        DistantArmies.clear();
        BattleSounds.clear();
        BattleSpotRing.clear();
        DistantHorizonsLink.leftWorld();
        ArrowVolleys.clear();
        ThrownFigures.clear();
        FigureMotion.clear();
        ClientTownsfolk.clear();
    }

    /**
     * Distant battle formations, drawn after the world so its terrain hides any behind hills: after the tripwire
     * layer, the last of it, because Distant Horizons fades the world into its own terrain just before that layer and
     * would fade them too.
     */
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            WaterSwell.prepare();
            DistantArmyRenderer.prepare(event.getPartialTick().getGameTimeDeltaPartialTick(false));
            FarFlags.beginFrame();
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            DistantOcclusion.begin();
            DistantArmyRenderer.render(event.getPoseStack(), event.getCamera(), event.getFrustum(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            ClientTownsfolk.render(event.getPoseStack(), event.getCamera(), event.getFrustum(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            DistantArmyRenderer.renderShips(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            OarFigures.render(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            SailFigures.render(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            // Inside the distant pass: figures' flags share their depth and far plane.
            FarFlags.render(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            DistantOcclusion.end();
            ArrowVolleys.render(event.getPoseStack(), event.getCamera(), event.getFrustum());
            BattleSpotRing.render(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            BattleScreen.renderSelection(event.getPoseStack(), event.getCamera(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
            rafasconquest.client.map.StrategyRelief.render(event.getPoseStack(), event.getCamera());
        }
    }

    /** {@code /pvedepth [off|on|view]}: the prototype that hides distant figures behind Distant Horizons' hills. */
    @SubscribeEvent
    public static void onRegisterClientCommands(net.neoforged.neoforge.client.event.RegisterClientCommandsEvent event) {
        var command = net.minecraft.commands.Commands.literal("pvedepth").executes(c -> {
            c.getSource().sendSuccess(() -> Component.literal("Distant depth: " + DistantOcclusion.mode().name().toLowerCase()
                    + " (" + DistantOcclusion.reason() + ")"), false);
            return 1;
        });
        for (DistantOcclusion.Mode mode : DistantOcclusion.Mode.values()) {
            command.then(net.minecraft.commands.Commands.literal(mode.name().toLowerCase()).executes(c -> {
                DistantOcclusion.setMode(mode);
                c.getSource().sendSuccess(() -> Component.literal("Distant depth: " + mode.name().toLowerCase()), false);
                return 1;
            }));
        }
        event.getDispatcher().register(command);

        // /pvefigures [normal|red|no_lightmap|no_shader_colour|white]: which part of the figures' colour turns them black.
        var figures = net.minecraft.commands.Commands.literal("pvefigures").executes(c -> {
            c.getSource().sendSuccess(() -> Component.literal("Figure colour test: " + FigureBatches.debug.name().toLowerCase()), false);
            return 1;
        });
        for (FigureBatches.Debug debug : FigureBatches.Debug.values()) {
            figures.then(net.minecraft.commands.Commands.literal(debug.name().toLowerCase()).executes(c -> {
                FigureBatches.debug = debug;
                FigureBatches.dump = true;
                c.getSource().sendSuccess(() -> Component.literal("Figure colour test: " + debug.name().toLowerCase()
                        + " (each block's colour, light and textures logged as [figures])"), false);
                return 1;
            }));
        }
        event.getDispatcher().register(figures);
    }

    /** The sound of the battle as a whole: hoofbeats and the din of the front. */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        BattleSounds.tick();
        rafasconquest.client.cloth.ClothRenderer.tick();
    }

    /** A battle soldier (or its horse) past the handover is hidden; its figure stands in for it. */
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        Vec3 eye = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Minecraft mc = Minecraft.getInstance();
        // In the city manager the player, held above the city as a spectator, would float in view as a head.
        if (DistantArmyRenderer.hides(event.getEntity(), eye, event.getPartialTick())
                || event.getEntity() == mc.player && mc.screen instanceof CityManagerScreen) {
            event.setCanceled(true);
        } else {
            var e = event.getEntity(); // its held and worn cloth moves with it
            rafasconquest.client.cloth.ClothRenderer.setCurrent(e, event.getPoseStack().last().pose(),
                    net.minecraft.util.Mth.rotLerp(event.getPartialTick(), e.yBodyRotO, e.yBodyRot));
            FarFlags.drawn(e);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingDone(RenderLivingEvent.Post<?, ?> event) {
        rafasconquest.client.cloth.ClothRenderer.setCurrent(null);
    }

    /** Right-clicking the war horn opens the battle screen instead of blowing it. */
    @SubscribeEvent
    public static void onRightClickItem(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide() && rafasconquest.bridge.WarHorn.is(event.getItemStack())) {
            event.setCanceled(true);
            event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
            if (Minecraft.getInstance().screen == null) {
                BattleScreen.open();
            }
        }
    }

    /**
     * A swing that meets no entity may meet a figure: a soldier the battle keeps as numbers, drawn by {@link
     * DistantArmyRenderer}. The nearest figure along the player's line of sight within reach is struck; the server
     * decides what the blow does.
     */
    @SubscribeEvent
    public static void onAttack(net.neoforged.neoforge.client.event.InputEvent.InteractionKeyMappingTriggered event) {
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

    /** The middle mouse button (pick block) switches a held halberd's or glaive's swing mode instead. */
    @SubscribeEvent
    public static void onPickBlock(net.neoforged.neoforge.client.event.InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.isPickBlock() && mc.player != null && rafasconquest.bridge.SwingModes.installed()
                && rafasconquest.bridge.SwingModes.polearm(mc.player.getMainHandItem()) != null) {
            event.setCanceled(true);
            event.setSwingHand(false);
            rafasconquest.net.ConquestNetwork.cycleSwing();
        }
    }

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            BackBanners.remove(event.getEntity().getId());
        }
    }

    /** In the battle screen the field of view is the plain setting, so the screen's picking matches what is drawn. */
    @SubscribeEvent
    public static void onComputeFov(net.neoforged.neoforge.client.event.ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.usedConfiguredFov() && mc.screen instanceof BattleScreen) {
            event.setFOV(mc.options.fov().get());
        }
    }

    /**
     * The player's body (or what it rides) stands still while its chunk is not loaded here. The battle screen's camera has
     * the server send the chunks about the camera instead (CameraView), so the ground under the body is gone on this side,
     * and the body, moved by this client, would fall through the empty chunks out of the world.
     */
    @SubscribeEvent
    public static void onEntityTick(net.neoforged.neoforge.event.tick.EntityTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        net.minecraft.world.entity.Entity e = event.getEntity();
        if (mc.player != null && e.level().isClientSide && e == mc.player.getRootVehicle()
                && !e.level().hasChunkAt(e.getBlockX(), e.getBlockZ())) {
            e.setDeltaMovement(Vec3.ZERO);
            event.setCanceled(true);
        }
    }

    /**
     * The player's body, ordered in the battle screen like any unit, walks itself straight to where it was sent (jumping
     * what it bumps into); any movement key of their own takes it back.
     */
    @SubscribeEvent
    public static void onMovementInput(net.neoforged.neoforge.client.event.MovementInputUpdateEvent event) {
        net.minecraft.world.phys.Vec3 to = BattleScreen.walkTo;
        Minecraft mc = Minecraft.getInstance();
        if (to == null || event.getEntity() != mc.player) {
            return;
        }
        var in = event.getInput();
        if (mc.screen == null && (in.up || in.down || in.left || in.right)) {
            BattleScreen.walkTo = null;
            return;
        }
        double dx = to.x - mc.player.getX();
        double dz = to.z - mc.player.getZ();
        if (dx * dx + dz * dz < 2.25) {
            BattleScreen.walkTo = null;
            return;
        }
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        mc.player.setYRot(yaw);
        mc.player.setYHeadRot(yaw);
        in.forwardImpulse = 1;
        in.leftImpulse = 0;
        in.up = true;
        in.jumping = mc.player.horizontalCollision;
    }
}
