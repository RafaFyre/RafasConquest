package rafasconquest.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.bridge.RecruitsBattle.UnitOrder;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.net.ConquestNetwork.UnitInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The battle screen, opened by the war horn or the strategy board. The player's body stays where it is; the view leaves
 * it for a free camera over the field (WASD to move, space and ctrl to rise and sink, shift to go fast, middle-drag to turn,
 * scroll to come nearer). Along the bottom: the player (gold ring) with their nation and abilities, buttons for their
 * other wings, and a track of cards, one per platoon of the shown wing. Left-click or drag to select platoons (with ctrl
 * held, single units, that is blocks; with shift held, added to the selection), right-click the ground to send them
 * there in formation or an enemy to attack it, right-drag a line on the ground to stand them along it facing square to
 * it (as in Total War); right-click a card for its orders. Orders go out at once, and the units stay selected.
 * Client thread only.
 */
public final class BattleScreen extends Screen {
    /** Draws an entity standing with its feet at (x, feet), {@code scale} pixels a block, looking toward the mouse. */
    static void inventoryFigure(GuiGraphics g, int x, int feet, int scale, int mouseX, int mouseY, net.minecraft.world.entity.LivingEntity entity) {
        // 1.21 centres the entity in a box (and clips to it): a box two blocks high, its middle a block above the feet.
        InventoryScreen.renderEntityInInventoryFollowsMouse(g, x - scale, feet - 2 * scale, x + scale, feet, scale, 0.0625F, mouseX, mouseY, entity);
    }

    private static final String[] ARMS = {"LEVY", "HVY INF", "SKIRMISH", "ZWEIHAND", "ARCHERS", "CAVALRY", "HVY CAV", "MUSKETS", "ENGINES"};
    private static final String[] MODES = {"FOLLOWING", "HOLDING", "MOVING", "CHARGING", "YOUR ORDERS"};
    private static final String[] WINGS = {"L", "C", "R"};
    private static final String[] WING_NAMES = {"Left wing", "Centre", "Right wing"};
    private static final double REACH = 960;
    private static final double TURN = 0.35;
    private static final int PICK = 18;
    private static final int TEXT = 0xFF3A2A10;
    private static final int GOLD = 0xFFFFB000;
    /**
     * Played as units are selected, one for each arm (as {@link rafasconquest.battle.BattlePlan.Arm} orders them), and
     * as they are ordered to move or to attack. Their files are listed in sounds.json, under sounds/battle; silent until
     * they are there.
     */
    private static final SoundEvent[] SELECT = {sound("battle.select.swordsmen"), sound("battle.select.shieldwall"),
            sound("battle.select.swordsmen"), sound("battle.select.swordsmen"), sound("battle.select.archers"),
            sound("battle.select.cavalry"), sound("battle.select.cavalry")}; // by Arm, in order
    private static final SoundEvent ORDER_MOVE = sound("battle.order.move");
    private static final SoundEvent ORDER_ATTACK = sound("battle.order.attack");
    private static final SoundEvent ARMY_CHARGE = sound("battle.army_charge");
    /** Cavalry's hooves are heard this long after the war cry: 0.45 s. */
    private static final int HOOVES_AFTER_CRY = 9;

    private static SoundEvent sound(String name) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(rafasconquest.RafasConquest.MOD_ID, name));
    }

    private static List<UnitInfo> units = List.of();
    private static boolean heard;
    /** The player's groups (shift+tab): unit number → group number. Selecting one of a group selects all of it. */
    private static final Map<Integer, Integer> groups = new java.util.HashMap<>();
    /** Where the player's own body is walking to, sent like any other unit; null when it stands. */
    @Nullable
    static Vec3 walkTo;

    private Marker camera;
    /** The camera's chunk as last sent to the server. */
    private long sentChunk = Long.MIN_VALUE;
    private boolean hidGui;
    private double camX;
    private double camY;
    private double camZ;
    private float yaw;
    private float pitch = 50;
    private int ticks;

    private final Set<Integer> selected = new HashSet<>();
    private final Set<Integer> glowing = new HashSet<>();
    private boolean playerSelected;
    private int wing = -1;
    private int pinned = -1;
    private int hovered = -1;
    /** A press of the left or right button: where, and whether it has become a drag. */
    private double pressX;
    private double pressY;
    private int pressButton = -1;
    private boolean dragged;
    /** A right-drag over the ground with units selected: the line they will stand along, from and (so far) to. */
    @Nullable
    private Vec3 lineFrom;
    @Nullable
    private Vec3 lineTo;
    private boolean leading;
    /** Opened for an officer's deployment: the whole army is selected once its units arrive. */
    private boolean selectAll;
    @Nullable
    private Menu menu;

    private record Entry(String label, Runnable action) {
    }

    private record Menu(int x, int y, List<Entry> entries) {
        int width() {
            return 96;
        }
    }

    public BattleScreen() {
        super(Component.literal("Battle"));
    }

    public static void open() {
        units = List.of();
        heard = false;
        ConquestNetwork.requestUnits();
        Minecraft.getInstance().setScreen(new BattleScreen());
    }

    /** An officer's deployment begins: the strategy view opens (if it isn't open) with their whole army selected. */
    public static void openForDeployment() {
        groups.clear(); // a new day's units are numbered afresh
        if (!(Minecraft.getInstance().screen instanceof BattleScreen)) {
            open();
        }
        if (Minecraft.getInstance().screen instanceof BattleScreen screen) {
            screen.selectAll = true;
        }
    }

    /** The server's answer: the player's units now (none: no battle for them). */
    public static void receive(List<UnitInfo> latest) {
        units = latest;
        heard = true;
    }

    /** The formations (block ids) selected in the open battle screen, to glow where they are only figures; else none. */
    /** The player's army colour (their units'), or -1 before any units are known. */
    static int playerColour() {
        return units.isEmpty() ? -1 : units.get(0).rgb();
    }

    /** Whether the player themself is selected in the battle screen. */
    static boolean playerSelected() {
        return Minecraft.getInstance().screen instanceof BattleScreen screen && screen.playerSelected;
    }

    static Set<Integer> selectedFormations() {
        if (!(Minecraft.getInstance().screen instanceof BattleScreen screen) || screen.selected.isEmpty()) {
            return Set.of();
        }
        Set<Integer> blocks = new HashSet<>();
        for (UnitInfo u : units) {
            if (screen.selected.contains(u.number())) {
                blocks.add(u.block());
            }
        }
        return blocks;
    }

    private boolean inBattle() {
        return !units.isEmpty();
    }

    // ---- the camera ----

    @Override
    public void tick() {
        if (++ticks % 5 == 0) {
            ConquestNetwork.requestUnits();
        }
        if (minecraft == null || minecraft.player == null || minecraft.level == null) {
            return;
        }
        glow();
        if (!inBattle()) {
            if (heard) {
                groups.clear();
            }
            letGo();
            return;
        }
        if (selectAll) {
            selectAll = false;
            units.forEach(u -> selected.add(u.number()));
        }
        if (camera == null || camera.level() != minecraft.level) {
            hidGui = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
            camera = new Marker(EntityType.MARKER, minecraft.level);
            yaw = minecraft.player.getYRot();
            double r = Math.toRadians(yaw);
            camX = minecraft.player.getX() + Math.sin(r) * 16;
            camZ = minecraft.player.getZ() - Math.cos(r) * 16;
            camY = minecraft.player.getY() + 18;
            camera.setPos(camX, camY, camZ);
            camera.setOldPosAndRot();
        }
        if (minecraft.getCameraEntity() != camera) {
            minecraft.setCameraEntity(camera);
        }
        camera.setOldPosAndRot();
        fly();
        camera.setPos(camX, camY, camZ);
        camera.setYRot(yaw);
        camera.setXRot(pitch);
    }

    /** Moves the camera by the player's own movement keys, within reach of their body and above the ground. */
    private void fly() {
        double speed = down(minecraft.options.keyShift) ? 3 : 1.2;
        double r = Math.toRadians(yaw);
        double fx = -Math.sin(r);
        double fz = Math.cos(r);
        double f = (down(minecraft.options.keyUp) ? 1 : 0) - (down(minecraft.options.keyDown) ? 1 : 0);
        double s = (down(minecraft.options.keyLeft) ? 1 : 0) - (down(minecraft.options.keyRight) ? 1 : 0);
        camX += (fx * f + fz * s) * speed;
        camZ += (fz * f - fx * s) * speed;
        camY += ((down(minecraft.options.keyJump) ? 1 : 0) - (down(minecraft.options.keySprint) ? 1 : 0)) * speed; // sprint: ctrl by default
        double px = minecraft.player.getX();
        double pz = minecraft.player.getZ();
        double d = Math.hypot(camX - px, camZ - pz);
        if (d > REACH) { // the server loads the world about the camera only this far from the body (CameraView)
            camX = px + (camX - px) * REACH / d;
            camZ = pz + (camZ - pz) * REACH / d;
        }
        long chunk = net.minecraft.world.level.ChunkPos.asLong(Mth.floor(camX) >> 4, Mth.floor(camZ) >> 4);
        if (chunk != sentChunk || ticks % 40 == 0) { // the resend keeps the server's chunk ticket alive
            sentChunk = chunk;
            ConquestNetwork.cameraAt(true, camX, camZ);
        }
        if (minecraft.level.hasChunkAt(Mth.floor(camX), Mth.floor(camZ))) { // an unarrived chunk reads as the world's floor
            double ground = minecraft.level.getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(camX), Mth.floor(camZ));
            camY = Mth.clamp(camY, ground + 2, ground + 240);
        }
    }

    private boolean down(KeyMapping key) {
        return InputConstants.isKeyDown(minecraft.getWindow().getWindow(), key.getKey().getValue());
    }

    /** Back to the player's own eyes. */
    private void letGo() {
        if (camera != null && minecraft != null) {
            ConquestNetwork.cameraAt(false, 0, 0);
            sentChunk = Long.MIN_VALUE;
            minecraft.setCameraEntity(minecraft.player);
            minecraft.options.hideGui = hidGui;
            camera = null;
        }
    }

    @Override
    public void removed() {
        deselect();
        letGo();
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ---- the view: rays and projection ----

    private double[] basis() {
        Camera cam = minecraft.gameRenderer.getMainCamera();
        double y = Math.toRadians(cam.getYRot());
        double p = Math.toRadians(cam.getXRot());
        double[] fwd = {-Math.sin(y) * Math.cos(p), -Math.sin(p), Math.cos(y) * Math.cos(p)};
        double[] right = {-Math.cos(y), 0, -Math.sin(y)};
        double[] up = {right[1] * fwd[2] - right[2] * fwd[1], right[2] * fwd[0] - right[0] * fwd[2], right[0] * fwd[1] - right[1] * fwd[0]};
        return new double[]{fwd[0], fwd[1], fwd[2], right[0], right[1], right[2], up[0], up[1], up[2],
                Math.tan(Math.toRadians(minecraft.options.fov().get()) / 2), (double) width / height};
    }

    /** Where a point of the world falls on the screen, or null behind the camera. */
    @Nullable
    private double[] project(Vec3 at) {
        Vec3 eye = minecraft.gameRenderer.getMainCamera().getPosition();
        double[] b = basis();
        double rx = at.x - eye.x;
        double ry = at.y - eye.y;
        double rz = at.z - eye.z;
        double z = rx * b[0] + ry * b[1] + rz * b[2];
        if (z < 0.5) {
            return null;
        }
        double x = (rx * b[3] + ry * b[4] + rz * b[5]) / (z * b[9] * b[10]);
        double y = (rx * b[6] + ry * b[7] + rz * b[8]) / (z * b[9]);
        return new double[]{(x + 1) / 2 * width, (1 - y) / 2 * height};
    }

    /** The ground under the mouse, or null. */
    @Nullable
    private Vec3 ground(double mouseX, double mouseY) {
        Vec3 eye = minecraft.gameRenderer.getMainCamera().getPosition();
        double[] b = basis();
        double nx = (mouseX / width * 2 - 1) * b[9] * b[10];
        double ny = (1 - mouseY / height * 2) * b[9];
        Vec3 dir = new Vec3(b[0] + b[3] * nx + b[6] * ny, b[1] + b[4] * nx + b[7] * ny, b[2] + b[5] * nx + b[8] * ny).normalize();
        BlockHitResult hit = minecraft.level.clip(new ClipContext(eye, eye.add(dir.scale(2560)), ClipContext.Block.COLLIDER,
                ClipContext.Fluid.ANY, minecraft.player));
        // past render distance the client has no blocks, only Distant Horizons' terrain
        return hit.getType() == HitResult.Type.MISS ? DistantHorizonsLink.raycast(eye, dir, 2560) : hit.getLocation();
    }

    private Vec3 onGround(double x, double z) {
        return new Vec3(x, minecraft.level.getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(x), Mth.floor(z)) + 1, z);
    }

    /**
     * Where a unit's soldiers are drawn, at their middles: the real ones where they stand, the rest as figures in its
     * ranks ({@link DistantArmyRenderer}); its centre if the client has no formation for it yet. Picking goes by these,
     * so it takes the soldiers the player sees, not one point in the middle of them.
     */
    private List<Vec3> marks(UnitInfo u) {
        List<Vec3> marks = new ArrayList<>();
        FormationSnapshot formation = DistantArmies.at(u.block());
        if (formation != null) {
            for (FormationSnapshot.Sprite s : formation.place(DistantArmies.positions(1))) {
                marks.add(new Vec3(s.x(), s.y() + 1, s.z()));
            }
        }
        if (marks.isEmpty()) {
            marks.add(onGround(u.x(), u.z()));
        }
        return marks;
    }

    // ---- cards and wings ----

    /** The cards of a wing, by card id, each its units. */
    private Map<Integer, List<UnitInfo>> cards(int wing) {
        Map<Integer, List<UnitInfo>> cards = new LinkedHashMap<>();
        for (UnitInfo u : units) {
            cards.computeIfAbsent(u.card(), c -> new ArrayList<>()).add(u);
        }
        cards.values().removeIf(c -> c.get(0).wing() != wing);
        return cards;
    }

    private boolean hasWing(int w) {
        return units.stream().anyMatch(u -> u.wing() == w);
    }

    private int shownWing() {
        if (wing < 0 || !hasWing(wing)) {
            int[] counts = new int[3];
            units.forEach(u -> counts[Mth.clamp(u.wing(), 0, 2)]++);
            wing = counts[0] > counts[1] && counts[0] > counts[2] ? 0 : counts[2] > counts[1] ? 2 : 1;
        }
        return wing;
    }

    /** The two wings on the buttons left of the track: the ones not shown, in order. */
    private int[] otherWings() {
        int shown = shownWing();
        int[] others = new int[2];
        int k = 0;
        for (int w = 0; w < 3; w++) {
            if (w != shown) {
                others[k++] = w;
            }
        }
        return others;
    }

    private int trackLeft() {
        return (int) (width * 0.234);
    }

    private int trackRight() {
        return (int) (width * 0.822);
    }

    private int cardW() {
        return Math.max(30, (int) (width * 0.05));
    }

    private int cardH() {
        return (int) (cardW() * 1.55);
    }

    private int cardTop() {
        return (int) (height * 0.955) - cardH();
    }

    /** Each card's left edge on the track: side by side, or overlaid when they no longer fit. */
    private int[] cardXs(int n) {
        int w = cardW();
        int span = trackRight() - trackLeft();
        double step = n > 1 ? Math.min(w + 6, (span - w) / (double) (n - 1)) : 0;
        int left = trackLeft() + (int) ((span - (step * (n - 1) + w)) / 2);
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = left + (int) (i * step);
        }
        return xs;
    }

    /** The cards bottom to top: later over earlier, the selected over the rest, then the pinned and the hovered. */
    private List<Integer> layers(Map<Integer, List<UnitInfo>> cards) {
        List<Integer> order = new ArrayList<>(cards.keySet());
        order.sort(java.util.Comparator.comparing((Integer id) -> cards.get(id).stream().anyMatch(u -> selected.contains(u.number())))); // stable
        for (int top : new int[]{pinned, hovered}) {
            if (order.remove((Integer) top)) {
                order.add(top);
            }
        }
        return order;
    }

    /** The card under the mouse: the topmost there. */
    private int cardAt(double mx, double my, Map<Integer, List<UnitInfo>> cards) {
        if (my < cardTop() || my > cardTop() + cardH()) {
            return -1;
        }
        List<Integer> ids = new ArrayList<>(cards.keySet());
        int[] xs = cardXs(ids.size());
        List<Integer> order = layers(cards);
        for (int k = order.size() - 1; k >= 0; k--) {
            int i = ids.indexOf(order.get(k));
            if (mx >= xs[i] && mx <= xs[i] + cardW()) {
                return order.get(k);
            }
        }
        return -1;
    }

    private int ringX() {
        return (int) (width * 0.08);
    }

    private int ringY() {
        return (int) (height * 0.86);
    }

    private int ringR() {
        return Math.max(18, (int) (width * 0.04));
    }

    private int[] ability(int i) {
        return i == 0 ? new int[]{(int) (width * 0.1), (int) (height * 0.955)} : new int[]{(int) (width * 0.125), (int) (height * 0.93)};
    }

    private int abilityR() {
        return Math.max(8, (int) (width * 0.012));
    }

    private int[] wingButton(int i) {
        int size = Math.max(14, (int) (width * 0.024));
        return new int[]{(int) (width * (i == 0 ? 0.178 : 0.207)), (int) (height * 0.98) - size, size};
    }

    // ---- drawing ----

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (minecraft == null || minecraft.player == null) {
            return;
        }
        if (!inBattle()) {
            if (heard) {
                g.drawCenteredString(font, "There is no battle to plan for", width / 2, height / 2, 0xFFF4EEDC);
            }
            return;
        }
        if (pressButton == 0 && dragged) {
            int x0 = (int) Math.min(pressX, mouseX);
            int y0 = (int) Math.min(pressY, mouseY);
            int x1 = (int) Math.max(pressX, mouseX);
            int y1 = (int) Math.max(pressY, mouseY);
            g.fill(x0, y0, x1, y1, 0x30FFD700);
            g.renderOutline(x0, y0, x1 - x0, y1 - y0, GOLD);
        }
        g.drawString(font, "WASD move · space/ctrl rise/sink · shift fast · middle-drag turn · right-drag line up · scroll zoom · drag to select platoons (ctrl: blocks, shift: add) · shift+tab group · right-click to order",
                6, 6, 0xFFF4EEDC);
        if (leading) {
            g.drawCenteredString(font, "Lead charge: right-click an enemy formation", width / 2, 20, GOLD);
        }
        drawTrack(g, mouseX, mouseY);
        drawPlayer(g, mouseX, mouseY);
        drawMoraleTip(g, mouseX, mouseY);
        if (menu != null) {
            drawMenu(g, mouseX, mouseY);
        }
    }

    private void drawTrack(GuiGraphics g, int mouseX, int mouseY) {
        int barTop = (int) (height * 0.91);
        int barBottom = (int) (height * 0.98);
        g.fill(trackLeft(), barTop, trackRight(), barBottom, 0xCC4A4A4A);
        int[] others = otherWings();
        for (int i = 0; i < 2; i++) {
            int[] b = wingButton(i);
            boolean has = hasWing(others[i]);
            boolean over = has && mouseX >= b[0] && mouseX < b[0] + b[2] && mouseY >= b[1] && mouseY < b[1] + b[2];
            g.fill(b[0], b[1], b[0] + b[2], b[1] + b[2], has ? over ? 0xFF6A6A6A : 0xFF4A4A4A : 0x704A4A4A);
            g.drawCenteredString(font, WINGS[others[i]], b[0] + b[2] / 2, b[1] + (b[2] - 8) / 2, has ? 0xFFFFFFFF : 0xFF888888);
        }
        g.drawString(font, WING_NAMES[shownWing()], trackLeft() + 2, barBottom - 10, 0xFFE0E0E0);
        Map<Integer, List<UnitInfo>> cards = cards(shownWing());
        List<Integer> ids = new ArrayList<>(cards.keySet());
        int[] xs = cardXs(ids.size());
        hovered = menu == null ? cardAt(mouseX, mouseY, cards) : hovered;
        for (int id : layers(cards)) {
            drawCard(g, xs[ids.indexOf(id)], cards.get(id));
        }
    }

    private void drawCard(GuiGraphics g, int x, List<UnitInfo> card) {
        int w = cardW();
        int h = cardH();
        int y = cardTop();
        boolean all = card.stream().allMatch(u -> selected.contains(u.number()));
        boolean some = card.stream().anyMatch(u -> selected.contains(u.number()));
        g.fill(x - 1, y - 1, x + w + 1, y + h + 1, all ? GOLD : some ? 0xFFFFE080 : 0xFFC8C8C8);
        g.fill(x, y, x + w, y + h, 0xFFFFF2B0);
        UnitInfo first = card.get(0);
        int[] arms = new int[ARMS.length];
        int alive = 0;
        int started = 0;
        boolean fighting = false;
        boolean tired = false;
        for (UnitInfo u : card) {
            arms[Mth.clamp(u.arm(), 0, ARMS.length - 1)] += u.soldiers();
            alive += u.soldiers();
            started += u.started();
            fighting |= u.fighting();
            tired |= u.tired();
        }
        int arm = 0;
        for (int i = 1; i < arms.length; i++) {
            arm = arms[i] > arms[arm] ? i : arm;
        }
        String status = (fighting ? "FIGHTING" : MODES[Mth.clamp(first.mode(), 0, MODES.length - 1)]) + (tired ? ", TIRED" : "");
        g.fill(x, y + 9, x + w, y + 10, 0xFFE0D090);
        small(g, ARMS[arm] + (first.split() ? "*" : ""), x + w / 2, y + 3, 0.6F);
        Integer group = groups.get(first.number());
        if (group != null) {
            small(g, "GROUP " + group, x + w / 2, y + h / 2 - 3, 0.5F);
        }
        g.fill(x, y + h - 17, x + w, y + h - 16, 0xFFE0D090);
        small(g, status, x + w / 2, y + h - 15, 0.45F);
        small(g, alive + "/" + started, x + w / 2, y + h - 8, 0.6F);
    }

    /** Text centred on {@code cx}, shrunk to {@code scale}, and further to fit a card. */
    private void small(GuiGraphics g, String s, int cx, int y, float scale) {
        float fit = Math.min(scale, (cardW() - 2) / (float) Math.max(1, font.width(s)));
        g.pose().pushPose();
        g.pose().translate(cx, y, 0);
        g.pose().scale(fit, fit, 1);
        g.drawString(font, s, -font.width(s) / 2, 0, TEXT, false);
        g.pose().popPose();
    }

    private void drawPlayer(GuiGraphics g, int mouseX, int mouseY) {
        int cx = ringX();
        int cy = ringY();
        int r = ringR();
        g.fill(cx - r / 2 - 4, (int) (height * 0.895), (int) (width * 0.135), (int) (height * 0.96), 0xCC4A4A4A);
        disc(g, cx, cy, r - 2, playerSelected ? 0x60FFD700 : 0x40000000);
        inventoryFigure(g, cx, cy + r * 3 / 4, (int) (r * 0.75), mouseX, mouseY, minecraft.player);
        ring(g, cx, cy, r, r - 2, GOLD);
        // The nation: shortcut: a square in its colour; draw its banner once the client knows it.
        int n = Math.max(12, (int) (width * 0.03));
        int nx = cx + r + 6;
        int ny = cy - n / 2;
        g.fill(nx - 1, ny - 1, nx + n + 1, ny + n + 1, 0xFFE8E8E8);
        g.fill(nx, ny, nx + n, ny + n, 0xFF000000 | units.get(0).rgb() & 0xFFFFFF);
        ItemStack[] icons = {new ItemStack(Items.GOAT_HORN), new ItemStack(Items.IRON_SWORD)};
        String[] names = {"Rally: everyone about you takes heart", "Lead charge: select units, then right-click an enemy"};
        for (int i = 0; i < 2; i++) {
            int[] a = ability(i);
            boolean over = Math.hypot(mouseX - a[0], mouseY - a[1]) <= abilityR();
            disc(g, a[0], a[1], abilityR(), i == 1 && leading ? GOLD : over ? 0xFF9AF0FF : 0xFF66E0FF);
            g.renderItem(icons[i], a[0] - 8, a[1] - 8);
            if (over) {
                g.renderTooltip(font, Component.literal(names[i]), mouseX, mouseY);
            }
        }
    }

    private static void disc(GuiGraphics g, int cx, int cy, int r, int color) {
        for (int dy = -r; dy <= r; dy++) {
            int half = (int) Math.sqrt(r * r - dy * dy);
            g.fill(cx - half, cy + dy, cx + half + 1, cy + dy + 1, color);
        }
    }

    private static void ring(GuiGraphics g, int cx, int cy, int outer, int inner, int color) {
        for (int dy = -outer; dy <= outer; dy++) {
            int o = (int) Math.sqrt(outer * outer - dy * dy);
            int i = Math.abs(dy) < inner ? (int) Math.sqrt(inner * inner - dy * dy) : -1;
            if (i < 0) {
                g.fill(cx - o, cy + dy, cx + o + 1, cy + dy + 1, color);
            } else {
                g.fill(cx - o, cy + dy, cx - i, cy + dy + 1, color);
                g.fill(cx + i + 1, cy + dy, cx + o + 1, cy + dy + 1, color);
            }
        }
    }

    private void drawMenu(GuiGraphics g, int mouseX, int mouseY) {
        int w = menu.width();
        g.fill(menu.x(), menu.y(), menu.x() + w, menu.y() + menu.entries().size() * 12 + 2, 0xEE202020);
        for (int i = 0; i < menu.entries().size(); i++) {
            int y = menu.y() + 1 + i * 12;
            if (mouseX >= menu.x() && mouseX < menu.x() + w && mouseY >= y && mouseY < y + 12) {
                g.fill(menu.x(), y, menu.x() + w, y + 12, 0xFF505050);
            }
            g.drawString(font, menu.entries().get(i).label(), menu.x() + 4, y + 2, 0xFFFFFFFF);
        }
    }

    // ---- input ----

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!inBattle()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (menu != null) {
            int i = (int) ((mouseY - menu.y() - 1) / 12);
            boolean inside = mouseX >= menu.x() && mouseX < menu.x() + menu.width() && mouseY >= menu.y() && i < menu.entries().size();
            Menu was = menu;
            menu = null;
            if (inside && button == 0) {
                was.entries().get(i).action().run();
            }
            return true;
        }
        pressX = mouseX;
        pressY = mouseY;
        pressButton = button;
        dragged = false;
        lineFrom = button == 1 && !selected.isEmpty() ? ground(mouseX, mouseY) : null;
        lineTo = null;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (button == pressButton && Math.hypot(mouseX - pressX, mouseY - pressY) > 4) {
            dragged = true;
        }
        if (dragged && button == 1) {
            if (lineFrom != null) {
                Vec3 at = ground(mouseX, mouseY);
                lineTo = at != null ? at : lineTo;
            }
            return true;
        }
        if (dragged && button == 2) {
            yaw += (float) (dx * TURN);
            pitch = Mth.clamp(pitch + (float) (dy * TURN), 5, 89);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != pressButton) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        pressButton = -1;
        if (button == 0) {
            if (dragged) {
                boxSelect(pressX, pressY, mouseX, mouseY);
            } else {
                leftClick(mouseX, mouseY);
            }
        } else if (button == 1 && !dragged) {
            rightClick(mouseX, mouseY);
        } else if (button == 1 && lineFrom != null && lineTo != null && !selected.isEmpty()) {
            lineUp(new ArrayList<>(selected), lineFrom, lineTo);
        }
        lineFrom = null;
        lineTo = null;
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        double[] b = basis();
        camX += b[0] * delta * 4;
        camY += b[1] * delta * 4;
        camZ += b[2] * delta * 4;
        return true;
    }

    private boolean onRing(double mx, double my) {
        return Math.hypot(mx - ringX(), my - ringY()) <= ringR();
    }

    private void leftClick(double mx, double my) {
        boolean add = hasShiftDown();
        for (int i = 0; i < 2; i++) {
            int[] a = ability(i);
            if (Math.hypot(mx - a[0], my - a[1]) <= abilityR()) {
                if (i == 0) {
                    send(List.of(), UnitOrder.RALLY, null);
                } else {
                    leading = !leading;
                }
                return;
            }
        }
        int[] others = otherWings();
        for (int i = 0; i < 2; i++) {
            int[] b = wingButton(i);
            if (mx >= b[0] && mx < b[0] + b[2] && my >= b[1] && my < b[1] + b[2]) {
                if (hasWing(others[i])) {
                    wing = others[i];
                    pinned = -1;
                }
                return;
            }
        }
        if (onRing(mx, my)) {
            if (!add) {
                selected.clear();
            }
            playerSelected = !add || !playerSelected;
            return;
        }
        Map<Integer, List<UnitInfo>> cards = cards(shownWing());
        int card = cardAt(mx, my, cards);
        if (card >= 0) {
            if (!add) {
                selected.clear();
                playerSelected = false;
            }
            cards.get(card).forEach(u -> selected.add(u.number()));
            takeGroups();
            pinned = card;
            soundSelected();
            return;
        }
        // On the field: the unit of the soldier nearest the click, or the player's body; else nothing.
        UnitInfo best = unitAt(mx, my);
        double bestD = best == null ? PICK : pickDistance(best, mx, my);
        double[] me = project(minecraft.player.position().add(0, 1, 0));
        boolean onMe = me != null && Math.hypot(me[0] - mx, me[1] - my) < Math.min(bestD, PICK);
        if (!add) {
            selected.clear();
            playerSelected = false;
            pinned = -1;
        }
        if (onMe) {
            playerSelected = true;
        } else if (best != null) {
            take(best);
            takeGroups();
            soundSelected();
        }
    }

    /** The rest of every group a selected unit is in is selected too. */
    private void takeGroups() {
        Set<Integer> chosen = new HashSet<>();
        for (int n : selected) {
            if (groups.containsKey(n)) {
                chosen.add(groups.get(n));
            }
        }
        groups.forEach((n, group) -> {
            if (chosen.contains(group)) {
                selected.add(n);
            }
        });
    }

    /**
     * Shift+tab: the selected units become a group (out of any they were in), selected and ordered together from then
     * on; if they are already all of one group, it is broken up.
     */
    private void toggleGroup() {
        if (selected.size() < 2) {
            return;
        }
        Set<Integer> in = new HashSet<>();
        selected.forEach(n -> in.add(groups.get(n)));
        Integer only = in.size() == 1 ? in.iterator().next() : null;
        if (only != null && groups.entrySet().stream().filter(x -> x.getValue().equals(only)).allMatch(x -> selected.contains(x.getKey()))) {
            groups.values().removeIf(only::equals);
            return;
        }
        int group = 1;
        while (groups.containsValue(group)) {
            group++;
        }
        selected.forEach(groups::remove);
        for (int n : selected) {
            groups.put(n, group);
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (inBattle() && key == org.lwjgl.glfw.GLFW.GLFW_KEY_TAB && hasShiftDown()) {
            toggleGroup();
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }

    /** The player's unit with a soldier nearest the mouse, within {@link #PICK} pixels; else null. */
    @Nullable
    private UnitInfo unitAt(double mx, double my) {
        UnitInfo best = null;
        double bestD = PICK;
        for (UnitInfo u : units) {
            double d = pickDistance(u, mx, my);
            if (d < bestD) {
                bestD = d;
                best = u;
            }
        }
        return best;
    }

    /** How far, in pixels, the mouse is from the nearest soldier of a unit on screen. */
    private double pickDistance(UnitInfo u, double mx, double my) {
        double best = Double.MAX_VALUE;
        for (Vec3 mark : marks(u)) {
            double[] at = project(mark);
            if (at != null) {
                best = Math.min(best, Math.hypot(at[0] - mx, at[1] - my));
            }
        }
        return best;
    }

    /** The mouse held this long over one of the player's units on the field shows what presses it toward breaking. */
    private static final long MORALE_HOVER_MS = 3000;
    private int hoverUnit = -1;
    private long hoverSince;

    private void drawMoraleTip(GuiGraphics g, int mouseX, int mouseY) {
        UnitInfo over = menu == null && pressButton < 0 && mouseY < cardTop() ? unitAt(mouseX, mouseY) : null;
        if (over == null || over.number() != hoverUnit) {
            hoverUnit = over == null ? -1 : over.number();
            hoverSince = net.minecraft.Util.getMillis();
            return;
        }
        if (over.morale() != null && net.minecraft.Util.getMillis() - hoverSince >= MORALE_HOVER_MS) {
            g.renderComponentTooltip(font, moraleLines(over.morale()), mouseX, mouseY);
        }
    }

    /** A block's morale in words: its state, then each thing pressing it, as a share of its breaking point. */
    static List<Component> moraleLines(rafasconquest.battle.FieldBattle.Morale m) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal(switch (m.state()) {
            case SHATTERED -> "Shattered: retreating to camp for the day";
            case ROUTED -> "Routed: regrouping behind the lines";
            case RALLIED -> "Rallied: it won't break for now";
            case STEADY -> "Morale: " + Math.round(m.risk() * 100) + "% of the way to breaking";
        }));
        if (m.state() == rafasconquest.battle.FieldBattle.Morale.State.SHATTERED
                || m.state() == rafasconquest.battle.FieldBattle.Morale.State.ROUTED) {
            return lines;
        }
        String[] whys = {"Recent losses", "Captain fell", "Attacked in the flank", "Attacked from the rear",
                "Allies routed nearby", "The army's losses"};
        double[] shares = {m.losses(), m.captain(), m.flanked(), m.rear(), m.fright(), m.army()};
        for (int i = 0; i < whys.length; i++) {
            long pct = Math.round(shares[i] / m.breaksAt() * 100);
            if (pct > 0) {
                lines.add(Component.literal("  " + whys[i] + ": +" + pct + "%").withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }
        if (lines.size() == 1) {
            lines.add(Component.literal("  Nothing presses it").withStyle(net.minecraft.ChatFormatting.GRAY));
        }
        return lines;
    }

    /** A unit picked on the field selects its whole platoon (its card), or with ctrl held only itself (its block). */
    private void take(UnitInfo u) {
        if (hasControlDown()) {
            selected.add(u.number());
            return;
        }
        for (UnitInfo o : units) {
            if (o.card() == u.card()) {
                selected.add(o.number());
            }
        }
    }

    /** The selection sound of the arm with the most soldiers selected. */
    private void soundSelected() {
        int[] soldiers = new int[SELECT.length];
        for (UnitInfo u : units) {
            if (selected.contains(u.number())) {
                soldiers[Mth.clamp(u.arm(), 0, SELECT.length - 1)] += Math.max(1, u.soldiers());
            }
        }
        int arm = 0;
        for (int i = 1; i < soldiers.length; i++) {
            arm = soldiers[i] > soldiers[arm] ? i : arm;
        }
        play(SELECT[arm]);
    }

    /** Units sent to attack charge: the army's war cry, and any cavalry's hooves only {@link #HOOVES_AFTER_CRY} after it. */
    private void charged(List<Integer> numbers) {
        play(ARMY_CHARGE);
        for (UnitInfo u : units) {
            FormationSnapshot f = numbers.contains(u.number()) ? DistantArmies.at(u.block()) : null;
            if (f != null && f.kind().mounted()) {
                BattleSounds.holdCharge(u.block(), HOOVES_AFTER_CRY);
            }
        }
    }

    private void play(SoundEvent sound) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F, 1.0F));
    }

    /**
     * Whether a right-click there falls on an enemy formation, so the units attack it rather than move there: the
     * server's own test (RecruitsBattle.enemyBlockAt). shortcut: it measures from where each formation is drawn, the
     * server from its block's place, a block or two apart; send the server's answer back if the two sounds ever disagree.
     */
    private boolean onEnemy(Vec3 at) {
        if (units.isEmpty()) {
            return false;
        }
        int side = rafasconquest.battle.FieldBattle.sideOf(units.get(0).block());
        for (FormationSnapshot f : DistantArmies.now()) {
            if (rafasconquest.battle.FieldBattle.sideOf(f.id()) != side && f.alive() > 0
                    && Math.hypot(f.x() - at.x, f.z() - at.z) <= rafasconquest.bridge.RecruitsBattle.ATTACK_PICK) {
                return true;
            }
        }
        return false;
    }

    private void boxSelect(double x0, double y0, double x1, double y1) {
        if (!hasShiftDown()) {
            selected.clear();
            playerSelected = false;
            pinned = -1;
        }
        double lx = Math.min(x0, x1);
        double hx = Math.max(x0, x1);
        double ly = Math.min(y0, y1);
        double hy = Math.max(y0, y1);
        boolean any = false;
        for (UnitInfo u : units) {
            for (Vec3 mark : marks(u)) { // a unit is taken if any of its soldiers is in the box
                double[] at = project(mark);
                if (at != null && at[0] >= lx && at[0] <= hx && at[1] >= ly && at[1] <= hy) {
                    take(u);
                    any = true;
                    break;
                }
            }
        }
        if (any) {
            takeGroups();
            soundSelected();
        }
        double[] me = project(minecraft.player.position().add(0, 1, 0));
        if (me != null && me[0] >= lx && me[0] <= hx && me[1] >= ly && me[1] <= hy) {
            playerSelected = true;
        }
    }

    private void rightClick(double mx, double my) {
        if (onRing(mx, my)) {
            menu = menuAt(mx, my, List.of(new Entry("Hold", () -> walkTo = null)));
            return;
        }
        Map<Integer, List<UnitInfo>> cards = cards(shownWing());
        int card = cardAt(mx, my, cards);
        if (card >= 0) {
            menu = cardMenu(mx, my, cards.get(card));
            return;
        }
        Vec3 at = ground(mx, my);
        if (at == null) {
            return;
        }
        List<Integer> chosen = new ArrayList<>(selected);
        boolean me = playerSelected;
        if (leading || !chosen.isEmpty() || me) {
            BattleSpotRing.flash(at);
        }
        SoundEvent heard = onEnemy(at) ? ORDER_ATTACK : ORDER_MOVE;
        if (leading) {
            leading = false;
            send(chosen, UnitOrder.LEAD_CHARGE, at);
            walkTo = at; // the player goes in at their head
            play(heard);
            if (heard == ORDER_ATTACK) {
                charged(chosen);
            }
            return;
        }
        if (!chosen.isEmpty()) {
            send(chosen, UnitOrder.GO, at);
            play(heard);
            if (heard == ORDER_ATTACK) {
                charged(chosen);
            }
        }
        if (me) {
            walkTo = at;
        }
    }

    private Menu cardMenu(double mx, double my, List<UnitInfo> card) {
        List<Integer> numbers = card.stream().map(UnitInfo::number).toList();
        List<Entry> entries = new ArrayList<>();
        if (card.get(0).split()) {
            entries.add(new Entry("Reform formation", () -> send(numbers, UnitOrder.REFORM, null)));
        }
        entries.add(new Entry("Change formation ▸", () -> menu = menuAt(mx, my, List.of(
                new Entry("Line", () -> send(numbers, UnitOrder.LINE, null)),
                new Entry("Wedge", () -> send(numbers, UnitOrder.WEDGE, null)),
                new Entry("Square", () -> send(numbers, UnitOrder.SQUARE, null)),
                new Entry("V", () -> send(numbers, UnitOrder.V, null))))));
        entries.add(new Entry("Hold", () -> send(numbers, UnitOrder.HOLD, null)));
        entries.add(new Entry("Charge", () -> {
            send(numbers, UnitOrder.CHARGE, null);
            play(ORDER_ATTACK);
            charged(numbers);
        }));
        entries.add(new Entry("Brace", () -> send(numbers, UnitOrder.BRACE, null)));
        entries.add(new Entry("Follow me", () -> send(numbers, UnitOrder.FOLLOW, null)));
        return menuAt(mx, my, entries);
    }

    private Menu menuAt(double mx, double my, List<Entry> entries) {
        int h = entries.size() * 12 + 2;
        return new Menu((int) Math.min(mx, width - 97), (int) Math.max(0, Math.min(my, height - h) - h), entries);
    }

    /** The units stand along the line from {@code from} to {@code to}, facing square to it (the server lays them out). */
    private void lineUp(List<Integer> numbers, Vec3 from, Vec3 to) {
        int[] units = numbers.stream().mapToInt(Integer::intValue).toArray();
        ConquestNetwork.sendHornOrders(new ConquestNetwork.HornOrders(List.of(units, units),
                new int[]{UnitOrder.LINE_UP.ordinal(), UnitOrder.LINE_UP.ordinal()}, new double[]{from.x, to.x}, new double[]{from.z, to.z}));
        BattleSpotRing.flash(from.add(to).scale(0.5));
        play(ORDER_MOVE);
        ConquestNetwork.requestUnits();
    }

    private void send(List<Integer> numbers, UnitOrder order, @Nullable Vec3 at) {
        double x = at == null ? 0 : at.x;
        double z = at == null ? 0 : at.z;
        ConquestNetwork.sendHornOrders(new ConquestNetwork.HornOrders(List.of(numbers.stream().mapToInt(Integer::intValue).toArray()),
                new int[]{order.ordinal()}, new double[]{x}, new double[]{z}));
        ConquestNetwork.requestUnits();
    }

    /** Everything is let go (the screen closes): unselected, no longer glowing. */
    private void deselect() {
        selected.clear();
        playerSelected = false;
        pinned = -1;
        glow();
    }

    /**
     * More selected than this and none glow (their gold rings still show): every glowing soldier is drawn twice, and
     * the outline costs a pass over the whole screen.
     */
    private static final int GLOW_UP_TO = 60;

    /** The selected soldiers, and the player if selected, glow (here only); the rest don't. Kept up each tick: the server's
     * own updates of an entity's flags would put it out. */
    private void glow() {
        Set<Integer> now = new HashSet<>();
        for (UnitInfo u : units) {
            if (selected.contains(u.number())) {
                for (int id : u.ids()) {
                    now.add(id);
                }
            }
        }
        if (playerSelected && minecraft.player != null) {
            now.add(minecraft.player.getId());
        }
        boolean outline = now.size() <= GLOW_UP_TO;
        for (int id : glowing) {
            if (!outline || !now.contains(id)) {
                net.minecraft.world.entity.Entity e = minecraft.level == null ? null : minecraft.level.getEntity(id);
                if (e != null) {
                    e.setGlowingTag(false);
                }
            }
        }
        for (int id : outline ? now : Set.<Integer>of()) {
            net.minecraft.world.entity.Entity e = minecraft.level == null ? null : minecraft.level.getEntity(id);
            if (e != null) {
                e.setGlowingTag(true);
            }
        }
        glowing.clear();
        glowing.addAll(now);
    }

    /** A gold ring on the ground under each selected soldier (under their horse when mounted), while the screen is open. */
    static void renderSelection(com.mojang.blaze3d.vertex.PoseStack pose, net.minecraft.client.Camera cam, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof BattleScreen screen) || mc.level == null) {
            return;
        }
        if (screen.lineFrom != null && screen.lineTo != null) {
            // The line being dragged, and an arrow from its middle the way the units will face (the line's left).
            Vec3 a = screen.lineFrom;
            Vec3 b = screen.lineTo;
            double len = Math.hypot(b.x - a.x, b.z - a.z);
            if (len >= 1) {
                Vec3 mid = a.add(b).scale(0.5);
                Vec3 face = new Vec3((b.z - a.z) / len, 0, -(b.x - a.x) / len);
                BattleSpotRing.strip(pose, cam.getPosition(), a, b, 0.3F, GOLD);
                BattleSpotRing.strip(pose, cam.getPosition(), mid, mid.add(face.scale(Math.min(6, Math.max(2, len / 4)))), 0.2F, GOLD);
            }
        }
        double[] rings = new double[screen.glowing.size() * 4];
        int count = 0;
        for (int id : screen.glowing) {
            net.minecraft.world.entity.Entity e = mc.level.getEntity(id);
            if (e != null) {
                net.minecraft.world.entity.Entity base = e.getRootVehicle();
                Vec3 at = base.getPosition(partialTick);
                rings[4 * count] = at.x;
                rings[4 * count + 1] = at.y;
                rings[4 * count + 2] = at.z;
                rings[4 * count + 3] = base.getBbWidth() * 0.5F + 0.2F;
                count++;
            }
        }
        BattleSpotRing.rings(pose, cam.getPosition(), rings, count, 0.15F, GOLD); // one draw for them all
    }
}
