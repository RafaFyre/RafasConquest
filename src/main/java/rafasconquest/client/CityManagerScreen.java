package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.levelgen.Heightmap;
import rafasconquest.core.SeatCities;
import rafasconquest.net.ConquestNetwork;

/**
 * The city manager, opened from the fief menu on a city the player owns. The server holds the player, unseen, above the
 * city ({@link rafasconquest.peerage.CityManager}); here the view leaves them for a camera circling the city's middle:
 * drag to turn it around, scroll to come nearer or draw back. The arrow top left goes back to the fief menu, Esc to the game.
 */
public class CityManagerScreen extends Screen {
    private static final double TURN = 0.4;

    private final SeatCities.City city;
    private Marker camera;
    private boolean hidGui;
    private boolean left;
    private double yaw = 45;
    private double pitch = 40;
    private double radius;
    private double groundY = Double.NaN;

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
            aim();
        }
        addRenderableWidget(Button.builder(Component.literal("←"), b -> back()).bounds(4, 4, 20, 20).build());
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
        double yawR = Math.toRadians(yaw);
        double pitchR = Math.toRadians(pitch);
        // Minecraft's look direction for (yaw, pitch); the camera stands back from the middle along it.
        double dx = -Math.sin(yawR) * Math.cos(pitchR);
        double dy = -Math.sin(pitchR);
        double dz = Math.cos(yawR) * Math.cos(pitchR);
        camera.setPos(city.x() + 0.5 - dx * radius, groundY - dy * radius, city.z() + 0.5 - dz * radius);
        camera.setYRot((float) yaw);
        camera.setXRot((float) pitch);
    }

    @Override
    public void tick() {
        aim();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // No background: the city itself is the view.
        g.drawCenteredString(font, title, width / 2, 10, 0xF4EEDC);
        for (net.minecraft.client.gui.components.Renderable widget : renderables) { // the widgets only: 1.21 Screen.render would paint the background over what is drawn above
            widget.render(g, mouseX, mouseY, partialTick);
        }
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
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // the server must go on, to send the city's chunks
    }
}
