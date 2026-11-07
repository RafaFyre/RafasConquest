package rafasconquest.client;

import net.minecraft.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.Talk;

import java.util.ArrayList;
import java.util.List;

/**
 * A conversation, close up: a recruiter or a noble ({@link Talk}), or the war council and its messengers. What is said
 * comes as pages, each said by one speaker; the camera glides in on whoever speaks, who turns to look at the player and
 * moves their mouth ({@link MouthLayer}) while their words type out in a dark box where the hotbar was, under their name
 * and their title in their nation's colours. Between pages the player goes on; on the last are their answers, and
 * choosing one sends it to the server, which answers with what follows ({@link #show}). Being hurt, walking off or Esc
 * ends it. A page said by no one near (entity -1, a messenger's word from afar) shows the box alone.
 */
public class DialogueScreen extends Screen {
    /** About the pace of a college-level reader (some 270 words a minute). */
    private static final float CHARS_PER_SECOND = 25;
    private static final int ZOOM_TICKS = 12;
    /** A line starts no sooner than this after the one before began (the war council's old 50 ticks a line)... */
    private static final long LINE_MILLIS = 2500;
    /** ...nor sooner than this after it was typed out, to take it in. */
    private static final long BEAT_MILLIS = 800;
    private static final int TEXT = 0xFFF0EAD8;
    private static final int GOLD = 0xFFE0C060;
    private static final int LINE = 11;
    private static final int OPTION = 12;
    private static final String NEXT = "\u0000next";
    private static final String END = "\u0000end";

    /** Whose mouth is moving, or -1. */
    private static int speaking = -1;

    private List<Talk.View> pages;
    private int page;
    private Talk.View view;
    private List<String> wrapped = List.of();
    private int letters;
    private long shownAt;
    /** Letters typed per line said, and when the player had the rest shown at once (or -1). */
    private List<Integer> beats = List.of();
    private long skippedAt = -1;
    private boolean waiting;

    private Marker camera;
    private Vec3 fromPos;
    private float fromYaw;
    private float fromPitch;
    private boolean hidGui;
    private CameraType cameraType;
    private int ticks;
    private int lastHurt;

    public DialogueScreen(List<Talk.View> pages) {
        super(Component.literal(pages.get(0).name()));
        this.pages = pages;
        this.view = pages.get(0);
    }

    static int speaking() {
        return speaking;
    }

    /** What is said next, in the same conversation: the camera goes on from where it is, the words start again. */
    public void show(List<Talk.View> next) {
        pages = next;
        page = 0;
        turnTo(next.get(0));
    }

    private void turnTo(Talk.View next) {
        if (next.entity() != view.entity() && camera != null) {
            restoreHead(view.entity());
            fromPos = camera.position();
            fromYaw = camera.getYRot();
            fromPitch = camera.getXRot();
            ticks = 0; // and glide over to the new speaker
        }
        view = next;
        waiting = false;
        wrap();
    }

    /** The choices under this page: go on, the answers on the last, or an end if it has none. */
    private List<Talk.Option> options() {
        if (page < pages.size() - 1) {
            return List.of(new Talk.Option(NEXT, "Continue"));
        }
        return view.options().isEmpty() ? List.of(new Talk.Option(END, "Continue")) : view.options();
    }

    private int boxWidth() {
        return Math.min(width - 40, 420);
    }

    private void wrap() {
        List<String> lines = new ArrayList<>();
        List<Integer> lengths = new ArrayList<>();
        for (String paragraph : view.lines()) {
            int length = 0;
            for (FormattedText t : font.getSplitter().splitLines(paragraph, boxWidth() - 16, Style.EMPTY)) {
                lines.add(t.getString());
                length += t.getString().length();
            }
            lengths.add(length);
        }
        wrapped = lines;
        beats = lengths;
        letters = lines.stream().mapToInt(String::length).sum();
        shownAt = Util.getMillis();
        skippedAt = -1;
    }

    /**
     * How far the words have come: {letters shown, 1 while a line is being typed}. Each line starts no sooner than
     * {@link #LINE_MILLIS} after the one before it began, and a beat after it was typed, as the council once paced its lines.
     */
    private long[] progress() {
        if (skippedAt >= 0) {
            return new long[]{letters, 0};
        }
        long t = Util.getMillis() - shownAt;
        long start = 0;
        int chars = 0;
        for (int length : beats) {
            long typing = (long) (length * 1000 / CHARS_PER_SECOND);
            if (t < start + typing) {
                return new long[]{chars + (long) ((t - start) * CHARS_PER_SECOND / 1000), 1};
            }
            chars += length;
            start = Math.max(start + LINE_MILLIS, start + typing + BEAT_MILLIS);
            if (t < start) {
                return new long[]{chars, 0};
            }
        }
        return new long[]{letters, 0};
    }

    /** When this page has been said, and its last pause is over: the next page comes then. */
    private long doneAt() {
        if (skippedAt >= 0) {
            return skippedAt + LINE_MILLIS;
        }
        long start = 0;
        for (int length : beats) {
            start = Math.max(start + LINE_MILLIS, start + (long) (length * 1000 / CHARS_PER_SECOND) + BEAT_MILLIS);
        }
        return shownAt + start;
    }

    private int shown() {
        return (int) Math.min(letters, progress()[0]);
    }

    private boolean typed() {
        return shown() >= letters;
    }

    @Override
    protected void init() {
        wrap(); // again on resize: the box's width follows the window's
        if (camera != null || minecraft == null || minecraft.player == null || minecraft.level == null) {
            return;
        }
        camera = new Marker(EntityType.MARKER, minecraft.level);
        fromPos = minecraft.player.getEyePosition();
        fromYaw = minecraft.player.getYRot();
        fromPitch = minecraft.player.getXRot();
        camera.moveTo(fromPos.x, fromPos.y, fromPos.z, fromYaw, fromPitch);
        camera.setOldPosAndRot();
        hidGui = minecraft.options.hideGui;
        minecraft.options.hideGui = true; // the hotbar makes way for the box
        cameraType = minecraft.options.getCameraType();
        minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        minecraft.setCameraEntity(camera);
        lastHurt = minecraft.player.hurtTime;
    }

    @Override
    public void tick() {
        if (minecraft == null || minecraft.player == null || camera == null) {
            return;
        }
        Entity npc = view.entity() < 0 ? null : minecraft.level.getEntity(view.entity());
        // Hurt (its hurt time starts again), or the speaker gone or out of reach: the conversation is over.
        if (view.entity() >= 0 && (npc == null || npc.distanceTo(minecraft.player) > 8) || minecraft.player.hurtTime > lastHurt) {
            onClose();
            return;
        }
        lastHurt = minecraft.player.hurtTime;
        ticks++;
        speaking = npc == null || progress()[1] == 0 ? -1 : npc.getId(); // the mouth rests between lines
        if (!waiting && page < pages.size() - 1 && Util.getMillis() >= doneAt()) {
            page++; // the next speaker goes on, at the same pace
            turnTo(pages.get(page));
        }
        Vec3 eye = minecraft.player.getEyePosition();
        Vec3 to = eye;
        float yaw = minecraft.player.getYRot();
        float pitch = minecraft.player.getXRot();
        if (npc != null) {
            if (npc instanceof LivingEntity living) {
                lookAt(living, eye);
            }
            Vec3 face = npc.getEyePosition();
            Vec3 out = new Vec3(eye.x - face.x, 0, eye.z - face.z);
            out = out.lengthSqr() < 1.0E-4 ? Vec3.directionFromRotation(0, npc.getYRot()) : out.normalize();
            // Over the player's right shoulder, a little nearer the speaker than they stand.
            to = face.add(out.scale(1.7)).add(-out.z * 0.45, 0.05, out.x * 0.45);
            HitResult hit = minecraft.level.clip(new ClipContext(face, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, camera));
            if (hit.getType() != HitResult.Type.MISS) {
                to = face.add(hit.getLocation().subtract(face).scale(0.85));
            }
            Vec3 look = face.subtract(to);
            yaw = (float) Math.toDegrees(Math.atan2(-look.x, look.z));
            pitch = (float) -Math.toDegrees(Math.atan2(look.y, Math.sqrt(look.x * look.x + look.z * look.z)));
        }
        float t = Mth.clamp(ticks / (float) ZOOM_TICKS, 0, 1);
        t = t * t * (3 - 2 * t);
        camera.setOldPosAndRot();
        Vec3 at = fromPos.lerp(to, t);
        camera.setPos(at.x, at.y, at.z);
        camera.setYRot(fromYaw + Mth.wrapDegrees(yaw - fromYaw) * t);
        camera.setXRot(Mth.lerp(t, fromPitch, pitch));
    }

    /** The speaker turns their head (no more than a neck allows) to look at the player. */
    private static void lookAt(LivingEntity npc, Vec3 eye) {
        Vec3 d = eye.subtract(npc.getEyePosition());
        float yaw = (float) Math.toDegrees(Math.atan2(-d.x, d.z));
        yaw = npc.yBodyRot + Mth.clamp(Mth.wrapDegrees(yaw - npc.yBodyRot), -70, 70);
        float pitch = (float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z)));
        npc.setYHeadRot(Mth.approachDegrees(npc.getYHeadRot(), yaw, 15));
        npc.setXRot(Mth.approach(npc.getXRot(), Mth.clamp(pitch, -40, 40), 10));
    }

    private void restoreHead(int entity) {
        if (minecraft != null && minecraft.level != null && entity >= 0 && minecraft.level.getEntity(entity) instanceof LivingEntity npc) {
            npc.setYHeadRot(npc.yBodyRot);
            npc.setXRot(0);
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // No background: the speaker is the view.
        List<Talk.Option> options = options();
        int w = boxWidth();
        int x = width / 2 - w / 2;
        int h = 16 + wrapped.size() * LINE + 6 + options.size() * OPTION;
        int y = height - 10 - h;
        g.fill(x, y, x + w, y + h, 0xB0000000);

        // Their name, and their title in their nation's colours, in boxes over the top left corner.
        int nameW = font.width(view.name()) + 10;
        g.fill(x, y - 15, x + nameW, y - 1, 0xC0000000);
        g.drawString(font, view.name(), x + 5, y - 12, GOLD);
        int titleW = font.width(view.title()) + 10;
        g.fill(x + nameW + 2, y - 15, x + nameW + 2 + titleW, y - 1, 0xE0000000 | view.colour());
        g.drawString(font, view.title(), x + nameW + 7, y - 12, 0xFF000000 | readable(view.accent(), view.colour()));

        int left = shown();
        int ty = y + 8;
        for (String line : wrapped) {
            if (left <= 0) {
                break;
            }
            g.drawString(font, line.substring(0, Math.min(line.length(), left)), x + 8, ty, TEXT);
            left -= line.length();
            ty += LINE;
        }
        if (!typed() || waiting) {
            return;
        }
        int oy = y + 8 + wrapped.size() * LINE + 6;
        for (int i = 0; i < options.size(); i++) {
            boolean over = option(mouseX, mouseY) == i;
            g.drawString(font, (i + 1) + ". " + options.get(i).label(), x + 12, oy + i * OPTION, over ? GOLD : 0xFFC8C0B0);
        }
    }

    /** The accent, or black or white if it would barely show on the colour. */
    private static int readable(int text, int back) {
        if (Math.abs(luma(text) - luma(back)) >= 90) {
            return text;
        }
        return luma(back) > 128 ? 0x101010 : 0xFFFFFF;
    }

    private static int luma(int rgb) {
        return ((rgb >> 16 & 255) * 3 + (rgb >> 8 & 255) * 6 + (rgb & 255)) / 10;
    }

    /** The option row under the mouse, or -1. */
    private int option(double mouseX, double mouseY) {
        int count = options().size();
        int w = boxWidth();
        int x = width / 2 - w / 2;
        int h = 16 + wrapped.size() * LINE + 6 + count * OPTION;
        int oy = height - 10 - h + 8 + wrapped.size() * LINE + 6;
        int i = (int) Math.floor((mouseY - oy + 2) / OPTION);
        return mouseX >= x && mouseX < x + w && mouseY >= oy - 2 && i >= 0 && i < count ? i : -1;
    }

    private void choose(int i) {
        List<Talk.Option> options = options();
        if (waiting || !typed() || i < 0 || i >= options.size()) {
            return;
        }
        String key = options.get(i).key();
        if (key.equals(NEXT)) {
            page++;
            turnTo(pages.get(page));
            return;
        }
        if (key.equals(END)) {
            onClose();
            return;
        }
        ConquestNetwork.choose(view.entity(), key);
        if (key.equals("bye") || key.equals("camp")) { // the camp: they are sent away
            onClose();
        } else {
            waiting = true; // until the server says what follows
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!typed()) {
            skippedAt = Util.getMillis(); // a click shows the rest at once
        } else {
            choose(option(mouseX, mouseY));
        }
        return true;
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key >= GLFW.GLFW_KEY_1 && key <= GLFW.GLFW_KEY_9) {
            choose(key - GLFW.GLFW_KEY_1);
            return true;
        }
        if (key == GLFW.GLFW_KEY_SPACE || key == GLFW.GLFW_KEY_ENTER) {
            if (!typed()) {
                skippedAt = Util.getMillis();
            } else if (page < pages.size() - 1 || view.options().isEmpty()) {
                choose(0); // go on
            }
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public void removed() {
        speaking = -1;
        if (minecraft == null || camera == null) {
            return;
        }
        minecraft.setCameraEntity(minecraft.player);
        minecraft.options.hideGui = hidGui;
        minecraft.options.setCameraType(cameraType);
        restoreHead(view.entity());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
