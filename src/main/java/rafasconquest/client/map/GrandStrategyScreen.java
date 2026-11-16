package rafasconquest.client.map;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.talhanation.recruits.init.ModEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EquipmentSlot;
import rafasconquest.client.CityManagerScreen;
import rafasconquest.gear.Armoury;
import rafasconquest.peerage.Garrison;
import rafasconquest.client.DistantHorizonsLink;
import rafasconquest.client.NationScreen;
import rafasconquest.client.Portraits;
import rafasconquest.client.TechTreeScreen;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.GrandStrategy;
import rafasconquest.people.Realm;
import rafasconquest.people.Succession;

import java.util.ArrayList;
import java.util.List;

/**
 * The grand strategy screen, opened from the Grand Strategy table: the world map filling the screen, centred on the
 * player's hold, under a HUD. Top right, the resources (money, prestige, population); down the right, a button for
 * each realm menu (vassals to technology); bottom left, the hold, house and character buttons over the player's realm
 * colour (their nation's page), their figure (its head following the cursor), culture, faith and skills; bottom right,
 * the date. The cities they own are ringed in gold: a click shows a city's summary; a double-click, or Manage city in its
 * right-click menu, opens its {@link CityManagerScreen city manager}.
 */
public class GrandStrategyScreen extends FactionMapScreen {
    private static final int PANEL_BACK = 0xD0101214;
    private static final int HUD = 0xFF4A4A4A;
    private static final int HUD_DARK = 0xFF2E2E2E;
    private static final int HUD_EDGE = 0xFF1A1A1A;
    private static final int HUD_HOVER = 0xFF6A6A6A;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int GOLD = 0xFFE0C060;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int HOVER = 0xFF3A3228;
    private static final int ROW = 12;
    /** How near (in screen pixels) a click must be to an owned city to pick it. */
    private static final double REACH = 8;
    private static final int DOUBLE_CLICK_MS = 350;
    private static final String MANAGE = "Manage city";
    private static final String SPAWN = "Spawn army";
    private static final String DELETE = "Delete army";
    private static final String NOT_BUILT = "Not built yet";
    private static final String[] SEASONS = {"Spring", "Summer", "Autumn", "Winter"};
    /** The HUD's spots in {@link #spots()} order: 3 resources, 9 realm menus, 3 left buttons, then these. */
    private static final int NATION = 15;
    private static final int CULTURE = 16;
    private static final int FAITH = 17;
    private static final int SKILLS = 18;
    private static final int COLOURED = 19;
    /** The realms drawn in their colours (on by default); static, so it stays as set the next time the map opens. */
    private static boolean colouredMap = true;

    /** Something on the HUD: its box, what it is (hover), and what a click does (null: nothing yet). */
    private record Spot(int x, int y, int w, int h, List<Component> tip, @Nullable Runnable click) {
        boolean over(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    private GrandStrategy.View fief;
    private final CityPanel panel = new CityPanel();
    /** An army's figure on the map: a knight of its nation with the nation's battle banner; made on first need. */
    private static final class Marcher {
        com.talhanation.recruits.entities.AbstractRecruitEntity e;
        int nation = -2;
        long walkedAt;
    }

    /** Each army's marcher, by army id. */
    private final java.util.Map<Integer, Marcher> marchers = new java.util.HashMap<>();
    @Nullable
    private SeatCities.City summary;
    /** The selected army's id (a click on its marcher), or {@link GrandStrategy#NO_TARGET}. */
    private int picked = GrandStrategy.NO_TARGET;
    /** The next view's newest spawned army is selected: one was just spawned. */
    private boolean pickNext;
    /** Marches already met at their foe (army id and march start): each face-off, or chase, happens once. */
    private final java.util.Set<String> faced = new java.util.HashSet<>();
    /** Where creative's right-click menu would spawn an army (x, z), or null while it is not open. */
    @Nullable
    private double[] spawnAt;
    /** The army creative's right-click menu would delete, or {@link GrandStrategy#NO_TARGET} while it is not open. */
    private int deleting = GrandStrategy.NO_TARGET;
    @Nullable
    private SeatCities.City menu;
    private int menuX;
    private int menuY;
    @Nullable
    private SeatCities.City lastClicked;
    private long lastClick;
    private long lastHoldClick;
    /** Looks down on Distant Horizons' terrain while the map is shown in 3D; null on the flat map. */
    @Nullable
    private Marker camera;
    private boolean hidGui;
    /** The flat map's titles layer, for its double-click on a hold, worked from the 3D map. */
    private final TitleLayer titles = new TitleLayer();
    /** The flat map's nation names, drawn on the 3D map where each realm's middle falls. */
    private final LabelLayer labels = new LabelLayer(this::openFaction);
    private long lastNameClick;
    /** Degrees the 3D camera looks down: Total War's campaign tilt. */
    private static final double PITCH = 55;

    public GrandStrategyScreen(GrandStrategy.View fief) {
        super(null, Component.translatable("gui.rafasconquest.grand_strategy"));
        this.fief = fief;
    }

    /** The server's answer to an army order: the same fief, its army as it now stands. */
    public void refresh(GrandStrategy.View fief) {
        this.fief = fief;
        if (pickNext) { // the army just spawned comes selected
            pickNext = false;
            picked = fief.armies().stream().mapToInt(GrandStrategy.Army::id).max().orElse(GrandStrategy.NO_TARGET);
            summary = null;
            panel.mode = CityPanel.Mode.NONE;
        }
    }

    /** The Army Spawn Menu sent a spawn: select the army the server's answer brings. */
    void pickNextSpawned() {
        pickNext = true;
    }

    /** Where an army stands now (x, z), or null while it is home in its city. */
    @Nullable
    private double[] pos(GrandStrategy.Army a) {
        return minecraft == null || minecraft.level == null ? null
                : Garrison.at(a.fromX(), a.fromZ(), a.toX(), a.toZ(), a.start(), minecraft.level.getGameTime() + minecraft.getTimer().getGameTimeDeltaPartialTick(false)); // between ticks too, so he glides
    }

    /** An army's nation: its own, else (a hold no nation holds) the player's. */
    private int nationOf(GrandStrategy.Army a) {
        return a.nation() >= 0 ? a.nation() : fief.nation();
    }

    /**
     * An army's marcher, the army out of its city: a knight of its nation with its banner on his back, made once; each
     * call also walks his legs (while the army moves) and turns him the way it marches. Null while the army is home.
     */
    @Nullable
    private com.talhanation.recruits.entities.AbstractRecruitEntity marcher(GrandStrategy.Army a) {
        double[] at = pos(a);
        if (at == null || minecraft.level == null) {
            return null;
        }
        BaseMap base = ClientMapCache.map();
        int n = nationOf(a);
        Marcher m = marchers.computeIfAbsent(a.id(), k -> new Marcher());
        var bearer = m.e;
        if (bearer == null || bearer.level() != minecraft.level || m.nation != n) {
            if (bearer != null) {
                rafasconquest.client.BackBanners.remove(bearer.getId());
            }
            bearer = m.e = ModEntityTypes.RECRUIT.get().create(minecraft.level);
            if (bearer == null) {
                return null;
            }
            // A knight of his nation: its officers' harness, crest and weapon in its land's style (Epic Knights; iron and a
            // sword without it), and its banner on his back.
            java.util.UUID nation = base != null && n >= 0 && n < base.nations().size() ? base.nations().get(n).id() : null;
            if (nation != null && Armoury.available()) {
                Armoury.dress(bearer, Armoury.Kit.GENERAL, base, nation);
            } else {
                bearer.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                bearer.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                bearer.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                bearer.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                bearer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            // A battle banner (cloth that moves) with the nation's arms.
            ItemStack banner = new ItemStack(rafasconquest.content.ConquestContent.BATTLE_BANNER_ITEM.get());
            if (nation != null) {
                net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, banner,
                        tag -> tag.putUUID("nation", nation));
            }
            rafasconquest.client.BackBanners.put(bearer.getId(), banner, 1.5F);
            bearer.setOnGround(true);
            m.nation = n;
            m.walkedAt = Util.getMillis();
        }
        boolean moving = Math.hypot(a.toX() - at[0], a.toZ() - at[1]) > 0.5;
        long now = Util.getMillis();
        for (; m.walkedAt + 50 <= now; m.walkedAt += 50) { // the walk animation's ticks since the last frame
            bearer.walkAnimation.update(moving ? 1F : 0F, 0.4F);
        }
        Portraits.animate(bearer);
        float yaw = (float) Math.toDegrees(Math.atan2(-(a.toX() - a.fromX()), a.toZ() - a.fromZ()));
        bearer.yBodyRot = bearer.yBodyRotO = yaw;
        bearer.setYRot(yaw);
        bearer.yRotO = yaw;
        bearer.yHeadRot = bearer.yHeadRotO = yaw;
        bearer.setXRot(0);
        bearer.xRotO = 0;
        // Fresh Animations (through EMF) only animates models near the camera: he stands at it, drawn where the army is.
        Vec3 eye = minecraft.gameRenderer.getMainCamera().getPosition();
        bearer.setPos(eye.x, eye.y, eye.z);
        bearer.setOldPosAndRot();
        return bearer;
    }

    /** An army's marcher's box on screen (x0, y0, x1, y1, GUI pixels), or null if he is not on the map. */
    @Nullable
    private double[] marcherBox(GrandStrategy.Army a) {
        double[] at = pos(a);
        if (at == null) {
            return null;
        }
        double[] feet = camera == null ? new double[]{view.screenX(at[0]), view.screenY(at[1])} : project(at[0], at[1]);
        double[] east = camera == null ? new double[]{view.screenX(at[0] + 100), feet[1]} : project(at[0] + 100, at[1]);
        if (feet == null || east == null) {
            return null;
        }
        double block = Math.abs(east[0] - feet[0]) / 100; // GUI pixels a block there
        double half = Math.max(5, MARCHER_HEIGHT * 0.3 * block);
        double tall = Math.max(12, MARCHER_HEIGHT * 1.3 * block); // with the banner over his head
        return new double[]{feet[0] - half, feet[1] - tall, feet[0] + half, feet[1] + 2};
    }

    private boolean onMarcher(GrandStrategy.Army a, double mx, double my) {
        double[] b = marcherBox(a);
        return b != null && mx >= b[0] && mx < b[2] && my >= b[1] && my < b[3];
    }

    /** The army whose marcher is under the mouse (the last drawn, on top), or null. */
    @Nullable
    private GrandStrategy.Army armyAt(double mx, double my) {
        GrandStrategy.Army hit = null;
        for (GrandStrategy.Army a : fief.armies()) {
            if (onMarcher(a, mx, my)) {
                hit = a;
            }
        }
        return hit;
    }

    /** How tall the marcher stands on the map, in blocks (banner aside): fixed to the land, as the user approved it. */
    private static final double MARCHER_HEIGHT = 80;
    /** The marcher's banner's breeze, blocks/s² at life size: Wind's least breeze, a little more. */
    private static final double BREEZE = 3;

    /** On the flat map, an army's marcher drawn as a figure over it (the 3D map has him in the world: {@link #renderMarcher}). */
    private void renderArmy(GuiGraphics g, GrandStrategy.Army a) {
        double[] at = camera == null ? pos(a) : null;
        var e = at == null ? null : marcher(a);
        if (e == null) {
            return;
        }
        e.yBodyRot = e.yBodyRotO = e.yHeadRot = e.yHeadRotO = 160; // three-quarters to the screen, looking down on him
        rafasconquest.client.cloth.ClothRenderer.far(e.getId(), null); // a picture over the flat map: his banner hangs at rest
        float scale = (float) (MARCHER_HEIGHT / e.getBbHeight() * view.scale); // GUI pixels a block of him
        float feet = (float) view.screenY(at[1]);
        InventoryScreen.renderEntityInInventory(g, (float) view.screenX(at[0]), feet - e.getBbHeight() * scale / 2, scale / e.getScale(),
                new Vector3f(0, e.getBbHeight() / 2, 0), new Quaternionf().rotateZ((float) Math.PI), null, e);
    }

    /**
     * The 3D map's marcher: an entity standing on the world's ground where the army is, walking toward its goal, drawn
     * {@link #MARCHER_HEIGHT} blocks tall, so he grows and shrinks with the land as the map zooms. Called while the world is drawn.
     */
    static void renderMarcher(PoseStack pose, Vec3 cam, MultiBufferSource buffers) {
        GrandStrategyScreen s = shown();
        if (s == null || s.camera == null) {
            return;
        }
        for (GrandStrategy.Army a : s.fief.armies()) {
            renderMarcher(s, a, pose, cam, buffers);
        }
    }

    private static void renderMarcher(GrandStrategyScreen s, GrandStrategy.Army a, PoseStack pose, Vec3 cam, MultiBufferSource buffers) {
        double[] at = s.pos(a);
        var e = at == null ? null : s.marcher(a);
        BaseMap base = ClientMapCache.map();
        if (e == null || base == null) {
            return;
        }
        double y = StrategyRelief.groundAt(base, at[0], at[1]);
        float k = (float) (MARCHER_HEIGHT / e.getBbHeight());
        pose.pushPose();
        pose.translate(at[0] - cam.x, y - cam.y, at[1] - cam.z);
        pose.scale(k, k, k);
        // His banner's cloth moves though he is far and big: a mild breeze from the left and from the top of the screen
        // (the camera looks north), so it flows a little toward the camera and to the right, swelling and easing.
        double t = Util.getMillis() / 1000.0;
        double[] breeze = {BREEZE * (1 + 0.35 * Math.sin(t * 0.9)), 0, BREEZE * 0.7 * (1 + 0.35 * Math.sin(t * 1.3 + 1))};
        rafasconquest.client.cloth.ClothRenderer.far(e.getId(), new rafasconquest.client.cloth.ClothRenderer.Far(k, breeze));
        rafasconquest.client.cloth.ClothRenderer.setCurrent(e);
        // Through the dispatcher, not his renderer alone: EMF (Fresh Animations) learns which entity it animates there;
        // skipped, he took the last-drawn entity's walk and age, and stood still or jittered.
        var entities = Minecraft.getInstance().getEntityRenderDispatcher();
        entities.setRenderShadow(false);
        entities.render(e, 0, 0, 0, e.getYRot(), Portraits.animate(e), pose, buffers, LightTexture.FULL_BRIGHT);
        entities.setRenderShadow(Minecraft.getInstance().options.entityShadows().get());
        rafasconquest.client.cloth.ClothRenderer.setCurrent(null);
        pose.popPose();
    }

    /**
     * The ground (x, z) under a point on screen: the flat map's, or through the 3D camera onto sea level; null over the sky.
     * shortcut: the 3D map's ground is taken as sea level, so a march aimed at a mountain lands a little off; fine from high up.
     */
    @Nullable
    private double[] unproject(double mx, double my) {
        if (camera == null) {
            return new double[]{view.worldX(mx), view.worldZ(my)};
        }
        double pitch = Math.toRadians(PITCH);
        double focal = height / 2.0 / Math.tan(Math.toRadians(fov()) / 2);
        double k = (height / 2.0 - my) / focal; // up / ahead, as project() has it
        double vy = ground() - camera.getY();
        double vz = -vy * (k * Math.sin(pitch) + Math.cos(pitch)) / (k * Math.cos(pitch) - Math.sin(pitch));
        double ahead = -vy * Math.sin(pitch) - vz * Math.cos(pitch);
        if (ahead <= 1 || !Double.isFinite(ahead)) {
            return null;
        }
        return new double[]{camera.getX() + (mx - width / 2.0) / focal * ahead, camera.getZ() + vz};
    }

    @Override
    protected boolean hud() {
        return true;
    }

    @Override
    protected void centre(BaseMap base) {
        if (fief.seat() >= 0 && fief.seat() < base.centres().size()) {
            view.centerX = base.centres().get(fief.seat()).x();
            view.centerZ = base.centres().get(fief.seat()).z();
        } else {
            super.centre(base);
        }
    }

    /** The cities the player owns: the one seating the county they hold. */
    private List<SeatCities.City> owned() {
        BaseMap base = ClientMapCache.map();
        return base == null || fief.seat() < 0 ? List.of() : base.cities().stream().filter(c -> c.province() == fief.seat()).toList();
    }

    private static String name(SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        return base == null ? "City" : base.names().get(city.province());
    }

    @Nullable
    private SeatCities.City ownedAt(double mouseX, double mouseY) {
        for (SeatCities.City c : owned()) {
            double[] s = at(c);
            if (s != null && Math.hypot(s[0] - mouseX, s[1] - mouseY) < REACH) {
                return c;
            }
        }
        return null;
    }

    /** "Spring 2nd, 12 AD": four seasons to a year of {@link Succession#YEAR_DAYS} days, the first year 1 AD. */
    public static String date(long day) {
        int season = Succession.YEAR_DAYS / SEASONS.length;
        int inYear = (int) (day % Succession.YEAR_DAYS);
        int d = inYear % season + 1;
        String th = d % 100 / 10 == 1 ? "th" : switch (d % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
        return SEASONS[inYear / season] + " " + d + th + ", " + (day / Succession.YEAR_DAYS + 1) + " AD";
    }

    private static List<Component> tip(String name, @Nullable String detail) {
        return detail == null ? List.of(Component.literal(name))
                : List.of(Component.literal(name), Component.literal(detail).withColor(0xA8A49C));
    }

    /** The player's nation's people, or -1 if they serve none. */
    private int population() {
        BaseMap base = ClientMapCache.map();
        return base == null || fief.nation() < 0 ? -1 : base.realm().demographics(fief.nation()).population();
    }

    /** The HUD's spots, laid out for the screen as it is now. */
    private List<Spot> spots() {
        BaseMap base = ClientMapCache.map();
        List<Spot> spots = new ArrayList<>();
        // Resources, top right: money, prestige, population.
        String[] resources = {"Money", "Prestige", "Population"};
        for (int i = 0; i < 3; i++) {
            spots.add(new Spot(width - 62 * (3 - i), 0, 62, 18, tip(resources[i], i < 2 ? NOT_BUILT : population() < 0 ? "You serve no nation" : null), null));
        }
        // The realm menus, down the right.
        String[] menus = {"Vassals", "Court", "Council", "Intrigue", "Factions", "Activities", "Army", "Law", "Technology"};
        for (int i = 0; i < menus.length; i++) {
            boolean tech = i == menus.length - 1;
            spots.add(new Spot(width - 21, 24 + i * 22, 20, 20, tip(menus[i], tech ? null : NOT_BUILT),
                    tech ? () -> minecraft.setScreen(new TechTreeScreen(this)) : null));
        }
        // Bottom left: hold, house, character.
        boolean held = !owned().isEmpty();
        spots.add(new Spot(1, height - 92, 16, 16, tip("Your hold", held ? "Click: look at it. Double-click: city view" : "You hold no land"),
                held ? this::holdClicked : null));
        spots.add(new Spot(1, height - 74, 16, 16, tip("House and bloodline", NOT_BUILT), null));
        spots.add(new Spot(1, height - 56, 16, 16, tip("Your character", null), ConquestNetwork::requestStats));
        // The realm colour (the nation's page), culture, faith, skills.
        Realm realm = base == null ? Realm.EMPTY : base.realm();
        int n = fief.nation();
        String nation = base == null || n < 0 ? null : base.nations().get(n).name();
        spots.add(new Spot(3, height - 31, 28, 28, tip(nation == null ? "No nation" : nation, nation == null ? "You serve no nation" : null),
                nation == null ? null : () -> minecraft.setScreen(new NationScreen(this, base, n))));
        String culture = n >= 0 && n < realm.nationCulture().length ? realm.cultures().get(realm.nationCulture()[n]).name() : null;
        String faith = n >= 0 && n < realm.nationFaith().length ? realm.faiths().get(realm.nationFaith()[n]).name() : null;
        spots.add(new Spot(76, height - 28, 25, 25, tip(culture == null ? "Culture" : "Culture: " + culture, NOT_BUILT), null));
        spots.add(new Spot(104, height - 28, 25, 25, tip(faith == null ? "Faith" : "Faith: " + faith, NOT_BUILT), null));
        spots.add(new Spot(92, height - 51, 21, 21, tip("Skills", NOT_BUILT), null));
        spots.add(new Spot(139, height - 25, 20, 20, tip("Coloured map", colouredMap ? "On: the realms in their colours" : "Off: the land as it is"),
                () -> colouredMap = !colouredMap));
        return spots;
    }

    /** Once: the map looks at the hold. Twice: the hold's city view. */
    private void holdClicked() {
        long now = Util.getMillis();
        BaseMap base = ClientMapCache.map();
        if (now - lastHoldClick < DOUBLE_CLICK_MS) {
            manage(owned().get(0));
        } else if (base != null) {
            centre(base);
        }
        lastHoldClick = now;
    }

    @Override
    protected boolean territory() {
        return colouredMap;
    }

    @Override
    protected boolean world3d() {
        return camera != null;
    }

    /**
     * Zoomed in near enough, and inside the ground Distant Horizons draws about the player, the map gives way to the
     * world itself: a camera high over the map's middle, looking north and down, as a Total War campaign map. Zoomed out
     * past that, or with no Distant Horizons, it is the flat map again.
     */
    private void update3d() {
        LocalPlayer p = minecraft == null ? null : minecraft.player;
        int reach = DistantHorizonsLink.reach();
        double span = view.width() / view.scale;
        // 3D up to four times Distant Horizons' reach across, so the camera climbs high; the middle must stay on its terrain.
        boolean want = map != null && p != null && minecraft.level.dimension() == Level.OVERWORLD && span <= 4.0 * reach
                && Math.hypot(view.centerX - p.getX(), view.centerZ - p.getZ()) <= reach;
        if (want && (camera == null || camera.level() != minecraft.level)) {
            if (camera == null) {
                hidGui = minecraft.options.hideGui;
            }
            camera = new Marker(EntityType.MARKER, minecraft.level);
            minecraft.options.hideGui = true;
            DistantHorizonsLink.lodOnly(true);
        } else if (!want && camera != null) {
            leave3d();
        }
        if (camera == null) {
            return;
        }
        // Far enough back that the map's own scale holds at the middle of the screen.
        double d = view.height() / view.scale / 2 / Math.tan(Math.toRadians(fov()) / 2);
        double pitch = Math.toRadians(PITCH);
        camera.setPos(view.centerX, ground() + d * Math.sin(pitch), view.centerZ + d * Math.cos(pitch));
        camera.setYRot(180); // north
        camera.setXRot((float) PITCH);
        camera.setOldPosAndRot(); // moved every frame, so no blur between ticks
        if (minecraft.getCameraEntity() != camera) {
            minecraft.setCameraEntity(camera);
        }
        StrategyRelief.show(view.scale, labels, map, colouredMap);
    }

    private void leave3d() {
        if (camera != null && minecraft != null) {
            camera = null;
            minecraft.setCameraEntity(minecraft.player);
            minecraft.options.hideGui = hidGui;
            DistantHorizonsLink.lodOnly(false);
            StrategyRelief.hide();
        }
    }

    private double fov() {
        return minecraft.options.fov().get();
    }

    /** shortcut: the camera sits over sea level, not the ground under it; fine thousands of blocks up, not when zoomed right in. */
    private double ground() {
        return minecraft.level.getSeaLevel();
    }

    /** A city on screen (GUI pixels): on the flat map, or on the world through the camera; null if behind it. */
    @Nullable
    private double[] at(SeatCities.City c) {
        if (camera == null) {
            return new double[]{view.screenX(c.x()), view.screenY(c.z())};
        }
        return project(c.x() + 0.5, c.z() + 0.5);
    }

    /** The ground at world (x, z) on screen through the 3D camera (GUI pixels), or null if behind it. */
    @Nullable
    private double[] project(double x, double z) {
        double pitch = Math.toRadians(PITCH);
        BaseMap base = ClientMapCache.map();
        double vx = x - camera.getX();
        double vy = (base == null ? ground() : StrategyRelief.groundAt(base, x, z)) - camera.getY();
        double vz = z - camera.getZ();
        // The camera looks north and down: forward (0, -sin, -cos), right east, up (0, cos, -sin).
        double ahead = -vy * Math.sin(pitch) - vz * Math.cos(pitch);
        if (ahead <= 1) {
            return null;
        }
        double up = vy * Math.cos(pitch) - vz * Math.sin(pitch);
        double focal = height / 2.0 / Math.tan(Math.toRadians(fov()) / 2);
        return new double[]{width / 2.0 + vx / ahead * focal, height / 2.0 - up / ahead * focal};
    }

    /** A menu drawn over the grand strategy map: it names the map under it. */
    interface OverMap {
        GrandStrategyScreen map();
    }

    /** The grand strategy map on screen, by itself or under one of its menus; null if neither. */
    @Nullable
    public static GrandStrategyScreen shown() {
        var screen = Minecraft.getInstance().screen;
        return screen instanceof GrandStrategyScreen s ? s : screen instanceof OverMap o ? o.map() : null;
    }

    /** A menu drawn over this map (the Army Spawn Menu, the face-off): leaving for it keeps the map, camera and marchers. */
    private boolean overMenu;

    private void openOver(net.minecraft.client.gui.screens.Screen menu) {
        overMenu = true;
        minecraft.setScreen(menu);
    }

    /** Drawn under a menu of {@code w} × {@code h}: the live map (no hover), then a clean depth so the menu lies over all of it. */
    void renderUnder(GuiGraphics g, int w, int h, float partialTick) {
        if (width != w || height != h) {
            init(minecraft, w, h); // the window was resized under the menu
        }
        render(g, -1, -1, partialTick);
        g.flush();
        com.mojang.blaze3d.systems.RenderSystem.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
    }

    /** Whether this map is up as itself, not under a menu (which draws it): only then may it leave for another. */
    private boolean onTop() {
        return minecraft != null && minecraft.screen == this;
    }

    @Override
    public void removed() {
        if (overMenu) { // under a menu, which draws this map and comes back to it
            overMenu = false;
            return;
        }
        for (Marcher m : marchers.values()) {
            if (m.e != null) {
                rafasconquest.client.cloth.ClothRenderer.far(m.e.getId(), null);
                rafasconquest.client.BackBanners.remove(m.e.getId());
            }
        }
        marchers.clear();
        leave3d();
        super.removed();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        update3d();
        super.render(g, mouseX, mouseY, partialTick);
        g.enableScissor(view.left, view.top, view.right, view.bottom);
        if (camera != null) {
            renderSeats3d(g);
            if (map != null) {
                labels.render3d(g, map, mouseX, mouseY, (x, z) -> {
                    double[] at = project(x, z);
                    double[] east = project(x + 100, z); // pixels per block there, along the unforeshortened east-west
                    return at == null || east == null ? null : new double[]{at[0], at[1], (east[0] - at[0]) / 100};
                });
            }
        }
        for (SeatCities.City c : owned()) {
            double[] s = at(c);
            if (s != null) {
                g.renderOutline((int) s[0] - 7, (int) s[1] - 7, 14, 14, GOLD);
            }
        }
        GrandStrategy.Army under = menuOpen() ? null : armyAt(mouseX, mouseY);
        for (GrandStrategy.Army a : fief.armies()) {
            renderArmy(g, a);
            double[] box = marcherBox(a);
            if (box != null && (a.id() == picked || a == under)) { // picked, or under the mouse (red: a click attacks it)
                int colour = a.id() == picked ? GOLD : picked != GrandStrategy.NO_TARGET ? 0xC0E04030 : 0x80E0C060;
                g.renderOutline((int) box[0], (int) box[1], (int) (box[2] - box[0]), (int) (box[3] - box[1]), colour);
            }
        }
        g.disableScissor();
        GrandStrategy.Army chosen = fief.army(picked);
        if (chosen == null || marcherBox(chosen) == null) {
            picked = GrandStrategy.NO_TARGET;
        }
        if (summary != null && panelArmy() != null) {
            panel.open(false);
        } else if (picked != GrandStrategy.NO_TARGET) {
            panel.open(true);
        } else {
            panel.close();
        }
        SeatCities.City hovered = !menuOpen() && !overHud(mouseX, mouseY) ? seatAt(mouseX, mouseY) : null;
        List<Spot> spots = spots();
        renderHud(g, spots, mouseX, mouseY);
        g.pose().pushPose();
        g.pose().translate(0, 0, 400); // over every mark and tooltip
        if (summary != null) {
            renderSummary(g, summary);
        }
        if (panelArmy() != null) {
            panel.render(g, font, width, height, panelArmy(), mouseX, mouseY);
        }
        if (menuOpen()) {
            renderMenu(g, mouseX, mouseY);
        } else if (hovered != null) {
            g.renderComponentTooltip(font, seatTip(hovered), mouseX, mouseY);
        } else if (under != null && panel.mode != CityPanel.Mode.MARCH && !overHud(mouseX, mouseY)) {
            g.renderComponentTooltip(font, armyTip(under), mouseX, mouseY);
        } else {
            for (Spot s : spots) {
                if (s.over(mouseX, mouseY)) {
                    g.renderComponentTooltip(font, s.tip(), mouseX, mouseY);
                }
            }
        }
        g.pose().popPose();
        meet(); // last: it may leave this screen for the face-off
    }

    /** The army the bottom panel shows: the hold's under a city's summary, else the picked one; null for none. */
    @Nullable
    private GrandStrategy.Army panelArmy() {
        return fief.army(summary != null ? GrandStrategy.HOLD : picked);
    }

    private boolean menuOpen() {
        return menu != null || spawnAt != null || deleting != GrandStrategy.NO_TARGET;
    }

    private List<Component> armyTip(GrandStrategy.Army a) {
        List<Component> lines = new ArrayList<>(tip(FaceOffScreen.nation(a), FaceOffScreen.general(a)));
        lines.add(Component.literal(a.units().length + " units, " + String.format("%,d", a.soldiers()) + " soldiers").withColor(0xA8A49C));
        lines.add(Component.literal(picked == GrandStrategy.NO_TARGET || a.id() == picked ? "Click: select" : "Click: attack it").withColor(0xE0C060));
        return lines;
    }

    /** A nation's map colour (ARGB), grey for none. */
    private int colour(int nation) {
        BaseMap base = ClientMapCache.map();
        return map == null || base == null || nation < 0 || nation >= base.nations().size() ? 0xFF6A6A6A : 0xFF000000 | map.color(base.nations().get(nation).id());
    }

    /** How near (blocks) an attacker must end its march to its foe to face it; farther, the foe has moved and it gives chase. */
    private static final double MEET = 64;

    /** An attacker at its march's end: face its foe if there, else march on to where the foe is now; once each march. */
    private void meet() {
        if (!onTop()) {
            return; // drawn under a menu: no face-off from there
        }
        for (GrandStrategy.Army a : fief.armies()) {
            double[] at = pos(a);
            GrandStrategy.Army foe = a.target() == GrandStrategy.NO_TARGET ? null : fief.army(a.target());
            double[] there = foe == null ? null : pos(foe);
            if (at == null || there == null || Math.hypot(a.toX() - at[0], a.toZ() - at[1]) > 0.5 || !faced.add(a.id() + ":" + a.start())) {
                continue; // no foe, still marching, or met already
            }
            if (Math.hypot(there[0] - at[0], there[1] - at[1]) > MEET) {
                ConquestNetwork.orderGarrison(a.id(), GrandStrategy.ATTACK, foe.id(), 0, 0);
            } else {
                openOver(new FaceOffScreen(this, a, foe, colour(nationOf(a)), colour(nationOf(foe))));
                return;
            }
        }
    }

    /** Every seat city on the 3D map: a square sized and coloured by its highest title, as on the flat map. */
    private void renderSeats3d(GuiGraphics g) {
        BaseMap base = ClientMapCache.map();
        if (base == null) {
            return;
        }
        for (SeatCities.City c : base.cities()) {
            double[] at = at(c);
            if (at == null || !view.near(at[0], at[1], 8)) {
                continue;
            }
            int s = TitleLayer.SEAT_SIZE[c.tier().ordinal()];
            int x = (int) at[0];
            int y = (int) at[1];
            g.fill(x - s - 1, y - s - 1, x + s + 1, y + s + 1, TitleLayer.OUTLINE);
            g.fill(x - s, y - s, x + s, y + s, TitleLayer.SEAT_COLOR[c.tier().ordinal()]);
        }
    }

    /** The seat city under the cursor on the 3D map (the flat map's layers have their own), or null. */
    @Nullable
    private SeatCities.City seatAt(double mouseX, double mouseY) {
        BaseMap base = ClientMapCache.map();
        if (camera == null || base == null) {
            return null;
        }
        SeatCities.City best = null;
        double near = REACH;
        for (SeatCities.City c : base.cities()) {
            double[] at = at(c);
            double d = at == null ? Double.MAX_VALUE : Math.hypot(at[0] - mouseX, at[1] - mouseY);
            if (d < near) {
                near = d;
                best = c;
            }
        }
        return best;
    }

    /** A seat's tooltip on the 3D map: its titles and holder, as on the flat map, then what a click does on it. */
    private List<Component> seatTip(SeatCities.City c) {
        BaseMap base = ClientMapCache.map();
        List<Component> lines = new ArrayList<>(TitleLayer.tip(base, TitleLayer.names(base, c.province(), c.tier().ordinal()), c.province()));
        if (owned().contains(c)) {
            lines.add(Component.literal("Click: summary. Double-click or right-click: " + MANAGE.toLowerCase()).withColor(0xE0C060));
        }
        return lines;
    }

    private void renderHud(GuiGraphics g, List<Spot> spots, int mouseX, int mouseY) {
        BaseMap base = ClientMapCache.map();
        // Resources.
        int people = population();
        Item[] icons = {Items.GOLD_NUGGET, Items.GOLDEN_HELMET, Items.PLAYER_HEAD};
        String[] values = {"-", "-", people < 0 ? "-" : String.format("%,d", people)}; // money and prestige: no system yet
        for (int i = 0; i < 3; i++) {
            Spot s = spots.get(i);
            g.fill(s.x(), s.y(), s.x() + s.w(), s.y() + s.h(), HUD);
            g.fill(s.x(), s.y(), s.x() + 1, s.y() + s.h(), HUD_EDGE);
            g.renderItem(new ItemStack(icons[i]), s.x() + 3, s.y() + 1);
            g.drawString(font, values[i], s.x() + s.w() - 4 - font.width(values[i]), s.y() + 5, TEXT, false);
        }
        g.fill(spots.get(0).x(), 18, width, 19, HUD_EDGE);
        // Realm menus and the left column: item-icon buttons.
        Item[] buttonIcons = {Items.SHIELD, Items.GOLDEN_HELMET, Items.LECTERN, Items.ENDER_EYE, Items.WHITE_BANNER, Items.BELL,
                Items.IRON_SWORD, Items.WRITABLE_BOOK, Items.ANVIL, Items.STONE_BRICKS, Items.OAK_SAPLING, Items.NAME_TAG};
        for (int i = 0; i < buttonIcons.length; i++) {
            Spot s = spots.get(3 + i);
            g.fill(s.x(), s.y(), s.x() + s.w(), s.y() + s.h(), HUD_EDGE);
            g.fill(s.x() + 1, s.y() + 1, s.x() + s.w() - 1, s.y() + s.h() - 1, s.over(mouseX, mouseY) ? HUD_HOVER : HUD);
            g.pose().pushPose();
            float k = (s.w() - 4) / 16F;
            g.pose().translate(s.x() + 2, s.y() + 2, 0);
            g.pose().scale(k, k, 1);
            g.renderItem(new ItemStack(buttonIcons[i]), 0, 0);
            g.pose().popPose();
        }
        // The bottom-left panel: realm colour, the player, culture, faith, skills.
        g.fill(0, height - 35, 135, height, HUD_EDGE);
        g.fill(0, height - 34, 134, height, HUD_DARK);
        Spot colour = spots.get(NATION);
        int realmColour = map != null && base != null && fief.nation() >= 0 ? 0xFF000000 | map.color(base.nations().get(fief.nation()).id()) : 0xFF6A6A6A;
        g.fill(colour.x() - 1, colour.y() - 1, colour.x() + colour.w() + 1, colour.y() + colour.h() + 1, HUD_EDGE);
        g.fill(colour.x(), colour.y(), colour.x() + colour.w(), colour.y() + colour.h(), realmColour);
        renderPlayer(g, 55, height + 8, 26, mouseX, mouseY);
        Portraits.diamond(g, 88, height - 16, 12, spots.get(CULTURE).over(mouseX, mouseY) ? 0xFF8CB8FF : 0xFF5C9CF0, HUD_EDGE);
        Portraits.diamond(g, 116, height - 16, 12, spots.get(FAITH).over(mouseX, mouseY) ? 0xFFFFE680 : 0xFFF5D040, HUD_EDGE);
        Portraits.circle(g, 102, height - 41, 10, HUD_EDGE);
        Portraits.circle(g, 102, height - 41, 9, spots.get(SKILLS).over(mouseX, mouseY) ? 0xFFE0E0E0 : 0xFFFFFFFF);
        // The coloured map toggle, beside the panel: a menu button, edged in gold while on.
        Spot toggle = spots.get(COLOURED);
        g.fill(toggle.x(), toggle.y(), toggle.x() + toggle.w(), toggle.y() + toggle.h(), colouredMap ? GOLD : HUD_EDGE);
        g.fill(toggle.x() + 1, toggle.y() + 1, toggle.x() + toggle.w() - 1, toggle.y() + toggle.h() - 1,
                toggle.over(mouseX, mouseY) ? HUD_HOVER : HUD);
        g.pose().pushPose();
        g.pose().translate(toggle.x() + 2, toggle.y() + 2, 0);
        g.pose().scale((toggle.w() - 4) / 16F, (toggle.w() - 4) / 16F, 1);
        g.renderItem(new ItemStack(Items.FILLED_MAP), 0, 0);
        g.pose().popPose();
        // The date, bottom right.
        String date = date(fief.day());
        int w = font.width(date) + 12;
        g.fill(width - w - 1, height - 18, width, height, HUD_EDGE);
        g.fill(width - w, height - 17, width, height, HUD);
        g.drawString(font, date, width - w + 6, height - 12, TEXT, false);
    }

    /**
     * The player's own figure, standing square to the screen, only its head turning after the cursor (the eyes follow
     * the head, as a model pack such as Fresh Animations moves them).
     */
    private void renderPlayer(GuiGraphics g, int x, int feet, int scale, int mouseX, int mouseY) {
        LocalPlayer p = minecraft == null ? null : minecraft.player;
        if (p == null) {
            return;
        }
        float centreY = feet - p.getBbHeight() * scale / 2;
        float eyeY = feet - p.getEyeHeight() * scale;
        float yaw = (float) Math.atan((x - mouseX) / 40F);
        float pitch = (float) Math.atan((eyeY - mouseY) / 40F);
        float body = p.yBodyRot;
        float bodyO = p.yBodyRotO;
        float yRot = p.getYRot();
        float xRot = p.getXRot();
        float xRotO = p.xRotO;
        float head = p.yHeadRot;
        float headO = p.yHeadRotO;
        p.yBodyRot = p.yBodyRotO = 180;
        p.setYRot(180 + yaw * 40);
        p.setXRot(-pitch * 20);
        p.xRotO = p.getXRot();
        p.yHeadRot = p.yHeadRotO = p.getYRot();
        InventoryScreen.renderEntityInInventory(g, x, centreY, scale / p.getScale(), new Vector3f(0, p.getBbHeight() / 2, 0),
                new Quaternionf().rotateZ((float) Math.PI), null, p);
        p.yBodyRot = body;
        p.yBodyRotO = bodyO;
        p.setYRot(yRot);
        p.setXRot(xRot);
        p.xRotO = xRotO;
        p.yHeadRot = head;
        p.yHeadRotO = headO;
    }

    /** A city's summary, beside it on the map. */
    private void renderSummary(GuiGraphics g, SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        List<String> lines = List.of(
                base == null ? "" : "Seat of the " + base.titleName(city.tier(), city.province()),
                city.districts() + " × " + city.districts() + " districts, " + city.size() + " blocks a side",
                String.format("At %,d, %,d", city.x(), city.z()),
                "Double-click or right-click to manage");
        int w = font.width(name(city));
        for (String l : lines) {
            w = Math.max(w, font.width(l));
        }
        w += 12;
        int h = ROW * (lines.size() + 1) + 6;
        double[] s = at(city);
        if (s == null) {
            return;
        }
        int x = (int) s[0] + 10;
        int y = (int) s[1] + 10;
        g.fill(x, y, x + w, y + h, PANEL_BACK);
        g.renderOutline(x, y, w, h, EDGE);
        g.drawString(font, name(city), x + 6, y + 4, GOLD, false);
        int row = y + 4 + ROW;
        for (String l : lines) {
            g.drawString(font, l, x + 6, row, TEXT, false);
            row += ROW;
        }
    }

    /** The right-click menu's title and its one row: a city's, or creative's on open ground. */
    private String menuTitle() {
        return menu != null ? name(menu) : "Creative";
    }

    private String menuRow() {
        return menu != null ? MANAGE : deleting != GrandStrategy.NO_TARGET ? DELETE : SPAWN;
    }

    private int menuWidth() {
        return Math.max(font.width(menuTitle()), font.width(menuRow())) + 12;
    }

    private boolean inManageRow(double mouseX, double mouseY) {
        int rowY = menuY + 4 + ROW;
        return mouseX >= menuX && mouseX < menuX + menuWidth() && mouseY >= rowY - 2 && mouseY < rowY + ROW - 2;
    }

    private void renderMenu(GuiGraphics g, int mouseX, int mouseY) {
        int w = menuWidth();
        g.fill(menuX, menuY, menuX + w, menuY + ROW * 2 + 6, 0xF0201D18);
        g.renderOutline(menuX, menuY, w, ROW * 2 + 6, EDGE);
        g.drawString(font, menuTitle(), menuX + 6, menuY + 4, GOLD, false);
        int rowY = menuY + 4 + ROW;
        if (inManageRow(mouseX, mouseY)) {
            g.fill(menuX + 1, rowY - 2, menuX + w - 1, rowY + ROW - 2, HOVER);
        }
        g.drawString(font, menuRow(), menuX + 6, rowY, TEXT, false);
    }

    /** Opens a city's manager; only from the overworld, where the cities are. */
    private void manage(SeatCities.City city) {
        if (minecraft != null && minecraft.level != null && minecraft.level.dimension() == Level.OVERWORLD) {
            minecraft.setScreen(new CityManagerScreen(city, name(city)));
        }
    }

    /** Over the HUD's bars and panels, where the map takes no clicks, drags or scrolls. */
    private boolean overHud(double x, double y) {
        return panel.over(x, y, width, height)
                || y < 19 && x >= width - 186
                || x >= width - 21 && y < 24 + 9 * 22
                || x < 135 && y >= height - 52
                || x < 17 && y >= height - 92
                || x >= 139 && x < 159 && y >= height - 25 && y < height - 5
                || y >= height - 18 && x >= width - font.width(date(fief.day())) - 13;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (menuOpen()) { // any click closes the menu; one on its row also does it
            SeatCities.City city = menu;
            double[] at = spawnAt;
            int doomed = deleting;
            boolean onRow = button == 0 && inManageRow(mouseX, mouseY);
            menu = null;
            spawnAt = null;
            deleting = GrandStrategy.NO_TARGET;
            if (onRow && city != null) {
                manage(city);
            } else if (onRow && doomed != GrandStrategy.NO_TARGET) {
                ConquestNetwork.orderGarrison(doomed, GrandStrategy.DELETE, 0, 0, 0);
                picked = GrandStrategy.NO_TARGET; // the armies after it close up their ids
            } else if (onRow) {
                openOver(new ArmySpawnScreen(this, at[0], at[1], fief.nation()));
            }
            return true;
        }
        GrandStrategy.Army shown = panelArmy();
        if (button == 0 && shown != null && panel.click(mouseX, mouseY, width, height, shown)) {
            return true;
        }
        if (panel.mode == CityPanel.Mode.MARCH && shown != null && !overHud(mouseX, mouseY)) { // the march's goal; a right-click calls it off
            double[] to = button == 0 ? unproject(mouseX, mouseY) : null;
            if (to != null) {
                ConquestNetwork.orderGarrison(shown.id(), GrandStrategy.MARCH, 0, to[0], to[1]);
            }
            panel.mode = CityPanel.Mode.NONE;
            return true;
        }
        if (overHud(mouseX, mouseY)) {
            if (button == 0 && minecraft != null) {
                for (Spot s : spots()) {
                    if (s.over(mouseX, mouseY) && s.click() != null) {
                        s.click().run();
                        break;
                    }
                }
            }
            return true;
        }
        GrandStrategy.Army doomed = button == 1 && minecraft.player != null && minecraft.player.isCreative() ? armyAt(mouseX, mouseY) : null;
        if (doomed != null) { // creative: Delete army
            deleting = doomed.id();
            menuX = (int) mouseX + 4;
            menuY = (int) mouseY + 4;
            return true;
        }
        GrandStrategy.Army clicked = button == 0 ? armyAt(mouseX, mouseY) : null;
        if (clicked != null && picked != GrandStrategy.NO_TARGET && clicked.id() != picked) { // another army: the picked one attacks it
            ConquestNetwork.orderGarrison(picked, GrandStrategy.ATTACK, clicked.id(), 0, 0);
            return true;
        }
        if (clicked != null) { // an army in the field, picked: Muster, Dismiss (spawned) and March
            if (picked != clicked.id()) {
                panel.mode = CityPanel.Mode.NONE;
            }
            picked = clicked.id();
            summary = null;
            return true;
        }
        picked = GrandStrategy.NO_TARGET;
        SeatCities.City city = ownedAt(mouseX, mouseY);
        if (city == null) {
            summary = null;
            SeatCities.City seat = seatAt(mouseX, mouseY);
            if (seat != null && button == 0 && map != null) {
                // The flat map's double-click on a hold (its holder's page), worked as if the seat's mark there were clicked.
                long now = Util.getMillis();
                boolean twice = seat == lastClicked && now - lastClick < DOUBLE_CLICK_MS;
                lastClicked = seat;
                lastClick = now;
                if (twice) {
                    titles.mouseClicked(view, map, view.screenX(seat.x()), view.screenY(seat.z()), 0, true);
                }
                return true;
            }
            if (camera != null && map != null) {
                long now = Util.getMillis();
                boolean twice = now - lastNameClick < DOUBLE_CLICK_MS;
                lastNameClick = now;
                if (labels.mouseClicked(view, map, mouseX, mouseY, button, twice)) {
                    return true; // a nation's name: twice opens its Faction Info
                }
            }
            double[] ground = button == 1 && minecraft.player != null && minecraft.player.isCreative() ? unproject(mouseX, mouseY) : null;
            if (ground != null) { // creative: Spawn army here
                spawnAt = ground;
                menuX = (int) mouseX + 4;
                menuY = (int) mouseY + 4;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (button == 1) {
            menu = city;
            menuX = (int) mouseX + 4;
            menuY = (int) mouseY + 4;
        } else if (button == 0) {
            long now = Util.getMillis();
            if (city == lastClicked && now - lastClick < DOUBLE_CLICK_MS) {
                manage(city);
            } else {
                summary = city;
            }
            lastClicked = city;
            lastClick = now;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        GrandStrategy.Army shown = panelArmy();
        return shown != null && panel.dragged(mouseX, width, height, shown) || overHud(mouseX, mouseY) || super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        panel.released();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        return panel.scrolled(mouseX, mouseY, delta, width, height) || overHud(mouseX, mouseY) || super.mouseScrolled(mouseX, mouseY, scrollX, delta);
    }
}
