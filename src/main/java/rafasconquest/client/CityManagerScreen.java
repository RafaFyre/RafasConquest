package rafasconquest.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rafasconquest.core.SeatCities;
import rafasconquest.net.ConquestNetwork;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * The city manager, opened from the fief menu on a city the player owns. The server holds the player, unseen, above the
 * city ({@link rafasconquest.peerage.CityManager}); here the view leaves them for a camera circling the city's middle:
 * drag to turn it around, scroll to come nearer or draw back. Esc goes back to the game, a click on the city's name to the
 * fief menu.
 * <p>
 * Over it, Frostpunk 2's HUD: painted by {@link CityHud} at the window's own pixels into one texture (repainted when the
 * window or the day changes); the council's figures are drawn
 * over the city each frame.
 */
public class CityManagerScreen extends Screen {
    private static final double TURN = 0.4;
    private static final ResourceLocation HUD = ResourceLocation.fromNamespaceAndPath("rafasconquest", "city_hud");

    private final SeatCities.City city;
    private Marker camera;
    private boolean hidGui;
    private boolean left;
    private double yaw = 45;
    private double pitch = 40;
    private double radius;
    private double groundY = Double.NaN;
    private final List<AbstractRecruitEntity> council = new ArrayList<>();
    private List<CityHud.Spot> spots = List.of();
    private String hudKey = "";

    public CityManagerScreen(SeatCities.City city, String name) {
        super(Component.literal(name));
        this.city = city;
        this.radius = city.size() * 0.9;
    }

    @Override
    protected void init() {
        if (camera == null && minecraft != null) {
            ConquestNetwork.cityView(city.province());
            hidGui = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
            DistantHorizonsLink.lodOnly(true); // the whole city as Distant Horizons draws it, no vanilla chunks
            aim();
        }
        if (council.isEmpty() && minecraft != null && minecraft.level != null) {
            // Five unlike figures: their own skins, and coats and hoods dyed apart, as Frostpunk 2's are.
            int[][] looks = {{6, 4, 0x5A4A3A}, {1, 0, 0x3A4048}, {11, 3, 0x7A1E18}, {16, 1, 0x2C3A30}, {3, 2, 0x6A5A40}};
            for (int[] look : looks) {
                AbstractRecruitEntity e = ModEntityTypes.RECRUIT.get().create(minecraft.level);
                if (e != null) {
                    e.setBiome((byte) look[1]);
                    e.setVariant(look[0]);
                    e.setItemSlot(EquipmentSlot.HEAD, dyed(Items.LEATHER_HELMET, look[2]));
                    e.setItemSlot(EquipmentSlot.CHEST, dyed(Items.LEATHER_CHESTPLATE, look[2]));
                    Vec3 eye = minecraft.gameRenderer.getMainCamera().getPosition();
                    e.setPos(eye.x, eye.y, eye.z); // animated models (EMF) idle only near the camera
                    e.setOnGround(true);
                    council.add(e);
                }
            }
        }
    }

    private static ItemStack dyed(net.minecraft.world.item.Item item, int rgb) {
        ItemStack s = new ItemStack(item);
        s.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb, false));
        return s;
    }

    /** Puts the camera on its circle about the city's middle, looking at it. */
    private void aim() {
        if (minecraft == null || minecraft.level == null) {
            return;
        }
        // A fresh camera if there is none, or the world was swapped under it.
        if (camera == null || camera.level() != minecraft.level) {
            camera = new Marker(EntityType.MARKER, minecraft.level);
            place();
            camera.setOldPosAndRot();
        }
        if (minecraft.getCameraEntity() != camera) {
            minecraft.setCameraEntity(camera);
        }
        camera.setOldPosAndRot();
        place();
    }

    private void place() {
        if (minecraft.level.hasChunkAt(city.x(), city.z())) {
            groundY = minecraft.level.getHeight(Heightmap.Types.MOTION_BLOCKING, city.x(), city.z());
        } else if (Double.isNaN(groundY) && minecraft.player != null) {
            groundY = minecraft.player.getY() - 40; // the server holds the player 40 above the ground
        }
        Vec3 f = forward();
        camera.setPos(city.x() + 0.5 - f.x * radius, groundY - f.y * radius, city.z() + 0.5 - f.z * radius);
        camera.setYRot((float) yaw);
        camera.setXRot((float) pitch);
    }

    /** Minecraft's look direction for the camera's (yaw, pitch). */
    private Vec3 forward() {
        double yawR = Math.toRadians(yaw);
        double pitchR = Math.toRadians(pitch);
        return new Vec3(-Math.sin(yawR) * Math.cos(pitchR), -Math.sin(pitchR), Math.cos(yawR) * Math.cos(pitchR));
    }

    @Override
    public void tick() {
        aim();
    }

    // --- Units: window pixels for drawing; the reference screenshot's units (2000 wide) for the layout. ---

    private double guiScale() {
        return minecraft.getWindow().getGuiScale();
    }

    /** Window pixels per reference unit. */
    private float s() {
        return minecraft.getWindow().getWidth() / CityHud.REF_W;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // No background: the city itself is the view.
        int w = minecraft.getWindow().getWidth();
        int h = minecraft.getWindow().getHeight();
        repaint(w, h);
        g.pose().pushPose();
        g.pose().scale((float) (1 / guiScale()), (float) (1 / guiScale()), 1);
        RenderSystem.enableBlend();
        g.blit(HUD, 0, 0, 0, 0, w, h, w, h);
        RenderSystem.disableBlend();
        g.pose().popPose();
        drawCouncil(g, h);

        double rx = mouseX * guiScale() / s();
        double ry = mouseY * guiScale() / s();
        for (CityHud.Spot sp : spots) {
            if (sp.over(rx, ry)) {
                g.pose().pushPose();
                g.pose().translate(0, 0, 400);
                g.renderComponentTooltip(font, sp.tip(), mouseX, mouseY);
                g.pose().popPose();
                break;
            }
        }
    }

    /** Paints the HUD texture again if the window or the day has changed. */
    private void repaint(int w, int h) {
        long day = day();
        String key = w + "x" + h + ":" + day;
        if (key.equals(hudKey) || w <= 0 || h <= 0) {
            return;
        }
        hudKey = key;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        spots = CityHud.paint(img, title.getString(), date(), day, this::back);
        minecraft.getTextureManager().register(HUD, upload(img));
    }

    private static DynamicTexture upload(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] px = img.getRGB(0, 0, w, h, null, 0, w);
        NativeImage n = new NativeImage(w, h, false);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = px[y * w + x];
                n.setPixelRGBA(x, y, (c & 0xFF00FF00) | ((c >> 16) & 0xFF) | ((c & 0xFF) << 16)); // ARGB to ABGR
            }
        }
        return new DynamicTexture(n);
    }

    /** The council's figures along the rail, head and shoulders above it, cut off at the bottom. */
    private void drawCouncil(GuiGraphics g, int h) {
        float gs = (float) guiScale();
        float s = s();
        float refH = h / s;
        float headTop = (refH - (CityHud.REF_H - 1026)) * s / gs;
        float clipTop = (refH - (CityHud.REF_H - 975)) * s / gs;
        float clipBottom = (refH - (CityHud.REF_H - 1106)) * s / gs;
        float scale = 74 * s / gs; // GUI units per block: head to waist spans about 80 reference units
        float[] turn = {20, -12, 15, -18, 10};
        for (int i = 0; i < CityHud.FACTION_X.length && i < council.size(); i++) {
            AbstractRecruitEntity e = council.get(i);
            Portraits.animate(e);
            float cx = CityHud.FACTION_X[i] * s / gs;
            float half = 60 * s / gs;
            e.yBodyRot = e.yBodyRotO = 180 + turn[i];
            e.setYRot(180 + turn[i]);
            e.yHeadRot = e.yHeadRotO = 180 + turn[i] * 0.5F;
            e.setXRot(0);
            e.xRotO = 0;
            float feet = headTop + e.getBbHeight() * scale;
            g.enableScissor(Math.round(cx - half), Math.round(clipTop), Math.round(cx + half), Math.round(clipBottom));
            InventoryScreen.renderEntityInInventory(g, cx, feet - e.getBbHeight() * scale / 2, scale / e.getScale(),
                    new Vector3f(0, e.getBbHeight() / 2, 0), new Quaternionf().rotateZ((float) Math.PI), null, e);
            g.disableScissor();
        }
    }

    private long day() {
        return minecraft.level == null ? 0 : minecraft.level.getDayTime() / 24000L;
    }

    private String date() {
        return rafasconquest.client.map.GrandStrategyScreen.date(day()).toUpperCase();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double rx = mouseX * guiScale() / s();
            double ry = mouseY * guiScale() / s();
            for (CityHud.Spot sp : spots) {
                if (sp.click() != null && sp.over(rx, ry)) {
                    sp.click().run();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (button == 0) {
            yaw += dx * TURN;
            pitch = Mth.clamp(pitch + dy * TURN, 10, 85);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        radius = Mth.clamp(radius * Math.pow(0.85, delta), 15, 250);
        return true;
    }

    /** Back to the fief menu: the server puts the player back and sends the menu, which replaces this screen. */
    private void back() {
        if (!left) {
            left = true;
            ConquestNetwork.cityView(ConquestNetwork.CITY_BACK);
        }
    }

    @Override
    public void removed() {
        if (!left) {
            left = true;
            ConquestNetwork.cityView(ConquestNetwork.CITY_LEAVE);
        }
        if (minecraft != null) {
            minecraft.setCameraEntity(minecraft.player);
            minecraft.options.hideGui = hidGui;
            DistantHorizonsLink.lodOnly(false);
            minecraft.getTextureManager().release(HUD);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // the server must go on, to send the city's chunks
    }
}
