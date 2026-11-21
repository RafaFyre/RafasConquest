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
import rafasconquest.core.CityPlates;
import rafasconquest.core.SeatCities;
import rafasconquest.peerage.CityStock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.Nullable;
import rafasconquest.net.ConquestNetwork;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
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
    /** The last city state the server sent; {@link #mine()} says whether it is this city's. */
    @Nullable
    private static ConquestNetwork.CityState state;
    /** Picking plates to flatten: a click toggles one, a drag paints, a right-drag turns the view. */
    private boolean flattening;
    private boolean paintAdd;
    private final Set<Long> selection = new LinkedHashSet<>();
    @Nullable
    private Long hover;
    @Nullable
    private Button confirm;
    @Nullable
    private Button cancel;

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
        addButtons();
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

    private void addButtons() {
        confirm = addRenderableWidget(Button.builder(Component.literal("Confirm"), b -> confirmFlatten()).bounds(10, height - 80, 90, 20).build());
        cancel = addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> toggleFlatten()).bounds(105, height - 80, 70, 20).build());
        confirm.visible = cancel.visible = flattening;
    }

    private boolean mine() {
        return state != null && state.province() == city.province();
    }

    /** The server's word on a city: its stockpile and plates. */
    public static void state(ConquestNetwork.CityState s) {
        state = s;
        if (net.minecraft.client.Minecraft.getInstance().screen instanceof CityManagerScreen screen && screen.mine()) {
            screen.selection.removeAll(s.flat());
            screen.selection.removeAll(s.pending());
        }
    }

    private void toggleFlatten() {
        flattening = !flattening && mine();
        selection.clear();
        if (confirm != null) {
            confirm.visible = cancel.visible = flattening;
        }
    }

    /** Plates flat or being levelled: what new ones may grow from. */
    private Set<Long> reach() {
        Set<Long> r = new HashSet<>(state.flat());
        r.addAll(state.pending());
        return r;
    }

    /** The selected plates that may be flattened. */
    private List<Long> accepted() {
        return state.plates().accept(selection, reach());
    }

    private void confirmFlatten() {
        List<Long> ok = accepted();
        if (!ok.isEmpty()) {
            ConquestNetwork.cityFlatten(city.province(), ok);
        }
        selection.clear();
    }

    /** The ground under a point of the screen, marched along the camera's ray over the loaded chunks; null over the sky. */
    @Nullable
    private Vec3 ground(double mx, double my) {
        if (camera == null || minecraft.level == null) {
            return null;
        }
        double yawR = Math.toRadians(yaw);
        Vec3 f = forward();
        Vec3 right = new Vec3(-Math.cos(yawR), 0, -Math.sin(yawR));
        Vec3 up = right.cross(f);
        double tan = Math.tan(Math.toRadians(minecraft.options.fov().get()) / 2);
        Vec3 dir = f.add(right.scale((2 * mx / width - 1) * tan * width / height)).add(up.scale((1 - 2 * my / height) * tan)).normalize();
        Vec3 from = camera.position();
        for (double t = 0; t < 600; t += 0.5) {
            Vec3 q = from.add(dir.scale(t));
            int x = Mth.floor(q.x);
            int z = Mth.floor(q.z);
            if (minecraft.level.hasChunkAt(x, z) && q.y <= minecraft.level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z)) {
                return q;
            }
        }
        return null;
    }

    @Nullable
    private Long plateAt(double mx, double my) {
        Vec3 at = mine() ? ground(mx, my) : null;
        return at == null ? null : state.plates().at(at.x, at.z);
    }

    /** The plates' outlines over the world while flattening: flat green, being levelled blue, picked gold (red: not allowed). */
    public static void renderWorld(PoseStack pose, Camera cam) {
        if (!(net.minecraft.client.Minecraft.getInstance().screen instanceof CityManagerScreen s) || !s.flattening || !s.mine()) {
            return;
        }
        CityPlates p = state.plates();
        Vec3 c = cam.getPosition();
        List<Long> ok = s.accepted();
        RenderSystem.disableDepthTest(); // the city is Distant Horizons' terrain, not in the depth buffer
        for (long k : state.flat()) {
            plate(pose, c, p, k, 0, 0xA060C060);
        }
        for (long k : state.pending()) {
            plate(pose, c, p, k, 0x3060A0E0, 0xE060A0E0);
        }
        for (long k : s.selection) {
            boolean good = ok.contains(k);
            plate(pose, c, p, k, good ? 0x40E0B040 : 0x40D8402C, good ? 0xFFE0B040 : 0xFFD8402C);
        }
        if (s.hover != null && !s.selection.contains(s.hover)) {
            Set<Long> reach = s.reach();
            reach.addAll(ok);
            boolean good = !p.accept(List.of(s.hover), reach).isEmpty();
            plate(pose, c, p, s.hover, 0, good ? 0xFFFFFFFF : 0xFFD8402C);
        }
        RenderSystem.enableDepthTest();
    }

    /** One plate at the height it is levelled to: a fill (none if 0) and its outline. */
    private static void plate(PoseStack pose, Vec3 cam, CityPlates p, long k, int fill, int line) {
        double x0 = p.minX(CityPlates.i(k));
        double z0 = p.minZ(CityPlates.j(k));
        double x1 = x0 + CityPlates.PLATE;
        double z1 = z0 + CityPlates.PLATE;
        double y = p.plateY() + 1;
        if (fill != 0) {
            BattleSpotRing.strip(pose, cam, new Vec3(x0, y, z0 + CityPlates.PLATE / 2.0), new Vec3(x1, y, z0 + CityPlates.PLATE / 2.0), CityPlates.PLATE / 2F, fill);
        }
        float half = 0.6F;
        BattleSpotRing.strip(pose, cam, new Vec3(x0 - half, y, z0), new Vec3(x1 + half, y, z0), half, line);
        BattleSpotRing.strip(pose, cam, new Vec3(x1, y, z0), new Vec3(x1, y, z1), half, line);
        BattleSpotRing.strip(pose, cam, new Vec3(x1 + half, y, z1), new Vec3(x0 - half, y, z1), half, line);
        BattleSpotRing.strip(pose, cam, new Vec3(x0, y, z1), new Vec3(x0, y, z0), half, line);
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
        if (flattening && mine() && confirm != null) {
            hover = plateAt(mouseX, mouseY);
            int n = accepted().size();
            g.drawString(font, n == 0 ? "Click plates to flatten: drag to paint, right-drag to turn"
                    : "Flatten " + n + " plates: " + n * CityStock.FLATTEN_GOLD + " gold, " + n * CityStock.FLATTEN_WORKERS + " workers", 10, height - 94, 0xFFE6D7A6);
            confirm.active = n > 0;
            confirm.render(g, mouseX, mouseY, partialTick); // not super.render: that would dim the city behind
            cancel.render(g, mouseX, mouseY, partialTick);
        }

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
        String key = w + "x" + h + ":" + day + ":" + (mine() ? java.util.Arrays.toString(state.res()) + state.busy() : "");
        if (key.equals(hudKey) || w <= 0 || h <= 0) {
            return;
        }
        hudKey = key;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        spots = CityHud.paint(img, title.getString(), date(), day, this::back, mine() ? state.res() : null, mine() ? state.busy() : 0, this::toggleFlatten);
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
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        Long k = button == 0 && flattening ? plateAt(mouseX, mouseY) : null;
        if (k != null) {
            paintAdd = selection.add(k);
            if (!paintAdd) {
                selection.remove(k);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (flattening && button == 0) {
            Long k = plateAt(mouseX, mouseY);
            if (k != null) {
                if (paintAdd) {
                    selection.add(k);
                } else {
                    selection.remove(k);
                }
            }
            return true;
        }
        if (button == 0 || flattening && button == 1) {
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
