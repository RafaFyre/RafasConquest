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
    /** Picking plates to flatten or build on: a click toggles one, a drag paints, a right-drag turns the view. */
    private int mode;
    private static final int NONE = 0;
    private static final int FLATTEN = 1;
    private static final int DISTRICT = 2;
    private static final int ROAD = 3;
    /** Roads drawn and not yet confirmed, and the one being dragged out ({x1, z1, x2, z2}), or null. */
    private final List<int[]> drafts = new ArrayList<>();
    @Nullable
    private int[] drag;
    /** The district kind being placed, a {@link CityStock.District} ordinal. */
    private int kind;
    private final List<Button> kinds = new ArrayList<>();
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
        behind = null;
        if (camera == null && minecraft != null) {
            ConquestNetwork.cityView(city.province());
            hidGui = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
            DistantHorizonsLink.lodOnly(true); // the whole city as Distant Horizons draws it, no vanilla chunks
            aim();
        }
        addButtons();
        if (council.isEmpty() && minecraft != null && minecraft.level != null) {
            // One figure per faction (peasantry, merchant guilds, great houses): their own skins, coats and hoods dyed apart.
            int[][] looks = {{6, 4, 0x5A4A3A}, {1, 0, 0x3A4048}, {11, 3, 0x7A1E18}};
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
        confirm = addRenderableWidget(Button.builder(Component.literal("Confirm"), b -> confirm()).bounds(10, height - 80, 90, 20).build());
        cancel = addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> toggle(mode)).bounds(105, height - 80, 70, 20).build());
        kinds.clear();
        CityStock.District[] all = CityStock.District.values();
        for (int i = 0; i < all.length; i++) {
            int k = i;
            kinds.add(addRenderableWidget(Button.builder(Component.literal(all[i].label), b -> {
                kind = k;
                showButtons();
            }).bounds(10 + i * 76, height - 104, 72, 20).build()));
        }
        showButtons();
    }

    private boolean mine() {
        return state != null && state.province() == city.province();
    }

    /** The server's word on a city: its stockpile and plates. */
    public static void state(ConquestNetwork.CityState s) {
        state = s;
        if (net.minecraft.client.Minecraft.getInstance().screen instanceof CityManagerScreen screen && screen.mine()) {
            if (screen.mode == DISTRICT) { // picked plates are flat: only those now built on are done with
                screen.selection.removeAll(s.districts().keySet());
            } else {
                screen.selection.removeAll(s.flat());
                screen.selection.removeAll(s.pending());
            }
        }
    }

    /** Into mode {@code m}, or out of it if already there (Cancel passes the mode it is in). */
    private void toggle(int m) {
        mode = mode == m || !mine() ? NONE : m;
        selection.clear();
        drafts.clear();
        drag = null;
        showButtons();
    }

    /** The roads drawn so far that may be laid, each checked beside those before it; up to the first that may not. */
    private List<int[]> roadsAccepted() {
        List<int[]> roads = new ArrayList<>(state.roads());
        List<int[]> ok = new ArrayList<>();
        for (int[] r : drafts) {
            if (state.plates().roadProblem(r, roads, state.districts().keySet()) != null) {
                break;
            }
            roads.add(r);
            ok.add(r);
        }
        return ok;
    }

    /** Why the road being dragged may not be laid, or null. */
    @Nullable
    private String dragProblem() {
        List<int[]> roads = new ArrayList<>(state.roads());
        roads.addAll(roadsAccepted());
        return state.plates().roadProblem(drag, roads, state.districts().keySet());
    }

    private void showButtons() {
        if (confirm == null) {
            return;
        }
        confirm.visible = cancel.visible = mode != NONE;
        for (int i = 0; i < kinds.size(); i++) {
            kinds.get(i).visible = mode == DISTRICT;
            kinds.get(i).active = i != kind; // the kind being placed shows pressed
        }
    }

    /** Plates flat or being levelled: what new ones may grow from. */
    private Set<Long> reach() {
        Set<Long> r = new HashSet<>(state.flat());
        r.addAll(state.pending());
        return r;
    }

    /** The selected plates that may be flattened, or built on in district mode. */
    private List<Long> accepted() {
        return mode == DISTRICT ? state.plates().acceptDistricts(selection, new HashSet<>(state.flat()), state.districts().keySet(),
                state.plates().roadPlates(state.roads())) : state.plates().accept(selection, reach());
    }

    /** Whether one more plate, beside those already accepted, may be picked. */
    private boolean allowed(long k, List<Long> ok) {
        if (mode == DISTRICT) {
            Set<Long> districts = new HashSet<>(state.districts().keySet());
            districts.addAll(ok);
            return !state.plates().acceptDistricts(List.of(k), new HashSet<>(state.flat()), districts, state.plates().roadPlates(state.roads())).isEmpty();
        }
        Set<Long> reach = reach();
        reach.addAll(ok);
        return !state.plates().accept(List.of(k), reach).isEmpty();
    }

    private void confirm() {
        if (mode == ROAD) {
            List<int[]> roads = roadsAccepted();
            if (!roads.isEmpty()) {
                ConquestNetwork.cityRoads(city.province(), roads);
            }
            drafts.clear();
            return;
        }
        List<Long> ok = accepted();
        if (!ok.isEmpty()) {
            if (mode == DISTRICT) {
                ConquestNetwork.cityDistrict(city.province(), kind, ok);
            } else {
                ConquestNetwork.cityFlatten(city.province(), ok);
            }
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

    /**
     * The plates' outlines over the world while picking: flat green, being levelled blue, districts in their kind's
     * colour, picked gold (red: not allowed), the one under the mouse white.
     */
    public static void renderWorld(PoseStack pose, Camera cam) {
        if (!(net.minecraft.client.Minecraft.getInstance().screen instanceof CityManagerScreen s) || s.mode == NONE || !s.mine()) {
            return;
        }
        CityPlates p = state.plates();
        Vec3 c = cam.getPosition();
        List<Long> ok = s.accepted();
        RenderSystem.disableDepthTest(); // the city is Distant Horizons' terrain, not in the depth buffer
        for (long k : state.flat()) {
            Integer d = state.districts().get(k);
            if (d == null) {
                plate(pose, c, p, k, 0, 0xA060C060);
            } else {
                int rgb = CityStock.District.values()[d].rgb;
                plate(pose, c, p, k, 0x50000000 | rgb, 0xE0000000 | rgb);
            }
        }
        for (long k : state.pending()) {
            plate(pose, c, p, k, 0x3060A0E0, 0xE060A0E0);
        }
        for (long k : s.selection) {
            boolean good = ok.contains(k);
            plate(pose, c, p, k, good ? 0x40E0B040 : 0x40D8402C, good ? 0xFFE0B040 : 0xFFD8402C);
        }
        if (s.mode != ROAD && s.hover != null && !s.selection.contains(s.hover)) {
            plate(pose, c, p, s.hover, 0, s.allowed(s.hover, ok) ? 0xFFFFFFFF : 0xFFD8402C);
        }
        for (int[] r : state.roads()) {
            road(pose, c, p, r, 0x70FFFFFF);
        }
        if (s.mode == ROAD) {
            List<int[]> good = s.roadsAccepted();
            for (int[] r : s.drafts) {
                road(pose, c, p, r, good.contains(r) ? 0xC0E0B040 : 0xC0D8402C);
            }
            if (s.drag != null) {
                road(pose, c, p, s.drag, s.dragProblem() == null ? 0xC0FFFFFF : 0xC0D8402C);
            }
        }
        RenderSystem.enableDepthTest();
    }

    /** A road's band on the ground, 5 wide, in 4-block pieces on the client's land (the city's height on its flat ground). */
    private static void road(PoseStack pose, Vec3 cam, CityPlates p, int[] r, int argb) {
        var level = net.minecraft.client.Minecraft.getInstance().level;
        int n = Math.max(1, (int) Math.ceil(CityPlates.length(r) / 4));
        Vec3 last = null;
        for (int k = 0; k <= n; k++) {
            double x = r[0] + 0.5 + (r[2] - r[0]) * (double) k / n;
            double z = r[1] + 0.5 + (r[3] - r[1]) * (double) k / n;
            int bx = Mth.floor(x);
            int bz = Mth.floor(z);
            boolean flat = p.inBoxArea(bx, bz) || state.flat().contains(p.at(bx, bz));
            double y = flat || level == null || !level.hasChunkAt(bx, bz) ? p.plateY() + 1 : level.getHeight(Heightmap.Types.MOTION_BLOCKING, bx, bz);
            Vec3 at = new Vec3(x, y, z);
            if (last != null) {
                BattleSpotRing.strip(pose, cam, last, at, (float) CityPlates.ROAD_HALF, argb);
            }
            last = at;
        }
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
        if (mode != NONE && mine() && confirm != null) {
            hover = mode == ROAD ? null : plateAt(mouseX, mouseY);
            int n = mode == ROAD ? roadsAccepted().size() : accepted().size();
            String d = CityStock.District.values()[kind].label;
            String problem = mode == ROAD && drag != null ? dragProblem() : null;
            String line = mode == ROAD
                    ? drag != null ? problem != null ? problem : Math.round(CityPlates.length(drag)) + " blocks"
                    : n == 0 ? "Drag a main road out from the city's edge or another road: right-drag to turn"
                    : "Lay " + n + " main road" + (n == 1 ? "" : "s") + " (free for now)"
                    : mode == DISTRICT
                    ? n == 0 ? "Click flat plates touching the city or a district: drag to paint, right-drag to turn"
                    : "Place " + n + " " + d + " district" + (n == 1 ? "" : "s") + ": " + n * CityStock.DISTRICT_GOLD + " gold, "
                    + n * CityStock.DISTRICT_RAW + " raw materials"
                    : n == 0 ? "Click plates to flatten: drag to paint, right-drag to turn"
                    : "Flatten " + n + " plates: " + n * CityStock.FLATTEN_GOLD + " gold, " + n * CityStock.FLATTEN_WORKERS + " workers";
            g.drawString(font, line, 10, height - 118, problem != null ? 0xFFD8402C : 0xFFE6D7A6);
            confirm.active = n > 0;
            confirm.render(g, mouseX, mouseY, partialTick); // not super.render: that would dim the city behind
            cancel.render(g, mouseX, mouseY, partialTick);
            for (Button b : kinds) {
                b.render(g, mouseX, mouseY, partialTick);
            }
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
        spots = CityHud.paint(img, title.getString(), date(), day, this::back, mine() ? state.res() : null, mine() ? state.busy() : 0, () -> toggle(FLATTEN), () -> toggle(DISTRICT), () -> toggle(ROAD),
                this::back, () -> over(new CityCourtScreen(this, title.getString(), city.province(), -1)), () -> over(new TechTreeScreen(this)),
                f -> over(new CityCourtScreen(this, title.getString(), city.province(), f)));
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
        if (mode == ROAD && button == 0) {
            Vec3 at = mine() ? ground(mouseX, mouseY) : null;
            if (at != null) {
                drag = new int[]{Mth.floor(at.x), Mth.floor(at.z), Mth.floor(at.x), Mth.floor(at.z)};
            }
            return true;
        }
        Long k = button == 0 && mode != NONE ? plateAt(mouseX, mouseY) : null;
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
        if (mode == ROAD && button == 0) {
            Vec3 at = drag == null ? null : ground(mouseX, mouseY);
            if (at != null) {
                drag[2] = Mth.floor(at.x);
                drag[3] = Mth.floor(at.z);
            }
            return true;
        }
        if (mode != NONE && button == 0) {
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
        if (button == 0 || mode != NONE && button == 1) {
            yaw += dx * TURN;
            pitch = Mth.clamp(pitch + dy * TURN, 10, 85);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (mode == ROAD && button == 0 && drag != null) {
            if (CityPlates.length(drag) >= CityPlates.ROAD_MIN) {
                drafts.add(drag);
            }
            drag = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
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

    /** The city manager under a screen opened over it (the court, the idea tree), which goes back to it; else null. */
    @Nullable
    static CityManagerScreen behind;

    /** Opens a screen over this one, keeping the city view (camera, chunks, HUD) for when it comes back. */
    private void over(Screen screen) {
        behind = this;
        minecraft.setScreen(screen);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_R) {
            over(new TechTreeScreen(this));
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public void removed() {
        if (behind == this) {
            return; // a screen over this one; this comes back
        }
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
