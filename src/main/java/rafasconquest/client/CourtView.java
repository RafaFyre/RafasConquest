package rafasconquest.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Homeland;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A nation's court as a little 3D scene behind the nation and character pages, in the way Create's Ponder shows its
 * scenes: the hall of {@link CourtSet} in the style of the nation's land, a carpet of its colour, and its people on
 * their podiums, the ruler at the top. A camera looks on, from far enough back to see them all, and glides in on whoever
 * is chosen (and back out). Kept by the pages as they open one another, so the camera carries on where it was.
 */
final class CourtView {
    /** How much of the screen's height the hall fills, seen whole; and a person, seen up close. */
    private static final float WHOLE = 1 / 17F;
    private static final float CLOSE = 1 / 3.4F;
    /** How fast the camera closes on where it is going (per second, of what is left). */
    private static final float GLIDE = 5F;
    /** The middle of the whole court, looked at from afar. */
    private static final Vector3f COURT = new Vector3f(0, 4.2F, -5);

    final BaseMap base;
    final int nation;
    private final CourtSet set;
    private final List<Figure> figures = new ArrayList<>();
    /** People of the nation with no seat (beyond the court's room). */
    final int unseated;

    /** Where the camera looks, how close (screen heights per block), its turn and tilt, and where on the screen that point sits. */
    private final Camera now = new Camera();
    private final Camera goal = new Camera();
    private long lastFrame;
    /** The scene's transform as last drawn: from the scene to the screen. */
    @Nullable
    private Matrix4f drawn;
    private float drawnScale;
    private int focus = -1;

    private record Figure(int person, CourtSet.Seat seat, @Nullable AbstractRecruitEntity entity) {
    }

    private static final class Camera {
        final Vector3f at = new Vector3f(COURT);
        float zoom = WHOLE;
        float yaw;
        float pitch = 14;
        float anchorX = 0.5F;
        float anchorY = 0.55F;

        void set(Camera o) {
            at.set(o.at);
            zoom = o.zoom;
            yaw = o.yaw;
            pitch = o.pitch;
            anchorX = o.anchorX;
            anchorY = o.anchorY;
        }
    }

    private CourtView(BaseMap base, int nation, Homeland homeland, DyeColor colour, List<Integer> people, int[] rows, int unseated) {
        this.base = base;
        this.nation = nation;
        this.unseated = unseated;
        this.set = CourtSet.build(homeland, colour, rows);
        Minecraft mc = Minecraft.getInstance();
        for (int i = 0; i < Math.min(people.size(), set.seats.size()); i++) {
            int person = people.get(i);
            figures.add(new Figure(person, set.seats.get(i), Portraits.model(mc, base, person)));
        }
        goal.set(now);
    }

    /** A nation's court: its ruler, then its nobles, most powerful first, as many as there is room for. */
    static CourtView ofNation(BaseMap base, int nation) {
        return ofNation(base, nation, -1);
    }

    /** A nation's court, with room kept for {@code also} (one of its people) even if they would not otherwise have a seat. */
    static CourtView ofNation(BaseMap base, int nation, int also) {
        Realm realm = base.realm();
        int ruler = realm.ruler(nation);
        List<Integer> nobles = new ArrayList<>();
        for (int i = 0; i < realm.people().size(); i++) {
            Person p = realm.people().get(i);
            // Not those whose hold has been conquered: they have lost their place in its court.
            if (i != ruler && p.nation() == nation && base.holders()[p.hold().province()] == nation) {
                nobles.add(i);
            }
        }
        nobles.sort(Comparator.comparingInt((Integer i) -> NationScreen.power(realm, i)).reversed());
        List<Integer> people = new ArrayList<>();
        if (ruler >= 0) {
            people.add(ruler);
        }
        people.addAll(nobles);
        int seated = Math.min(people.size(), CourtSet.MAX_SEATS);
        if (also >= 0 && people.indexOf(also) >= seated) {
            people.remove((Integer) also);
            people.add(seated - 1, also);
        }
        Homeland homeland = realm.cultures().get(realm.nationCulture()[nation]).homeland();
        DyeColor colour = FactionBannerNbt.baseColor(FactionBanner.of(base.nations().get(nation).id()));
        return new CourtView(base, nation, homeland, colour, people.subList(0, seated), CourtSet.pyramid(seated), people.size() - seated);
    }

    /**
     * A sword sanctum's court: its Sword God on top, its Sword Kings in the wide row beneath, its Sword Saints in the two
     * rows below (the strongest first); in the hall of its nation's land.
     */
    static CourtView ofOrganization(BaseMap base, rafasconquest.people.Organization o) {
        Realm realm = base.realm();
        List<Integer> people = new ArrayList<>();
        int head = o.head() >= 0 ? 1 : 0;
        if (o.head() >= 0) {
            people.add(o.head());
        }
        int kings = Math.min(o.kings().size(), CourtSet.WIDE_ROW);
        people.addAll(o.kings().subList(0, kings));
        int saints = Math.min(o.saints().size(), CourtSet.ROW_SEATS[2] + CourtSet.ROW_SEATS[3]);
        people.addAll(o.saints().subList(0, saints));
        int[] rows = {head, kings, Math.min(saints, CourtSet.ROW_SEATS[2]), Math.max(0, saints - CourtSet.ROW_SEATS[2])};
        Homeland homeland = realm.cultures().get(realm.nationCulture()[o.nation()]).homeland();
        DyeColor colour = FactionBannerNbt.baseColor(FactionBanner.of(base.nations().get(o.nation()).id()));
        return new CourtView(base, o.nation(), homeland, colour, people, rows, o.members().size() - people.size());
    }

    /** One person on their own podium in a hall of their own land: for someone of no nation. */
    static CourtView ofOne(BaseMap base, int person) {
        Person p = base.realm().people().get(person);
        Homeland homeland = base.realm().cultures().get(p.culture()).homeland();
        return new CourtView(base, -1, homeland, DyeColor.GRAY, List.of(person), CourtSet.pyramid(1), 0);
    }

    /** The court a page about {@code person} shows: theirs, or {@code current} if they already stand in it. */
    static CourtView of(BaseMap base, int person, @Nullable CourtView current) {
        if (current != null && current.has(person)) {
            return current;
        }
        int nation = base.realm().people().get(person).nation();
        return nation >= 0 ? ofNation(base, nation, person) : ofOne(base, person);
    }

    boolean has(int person) {
        return figures.stream().anyMatch(f -> f.person() == person);
    }

    /** The colour behind the hall. */
    int sky() {
        return set.style.sky();
    }

    /** Who the camera is on, or -1 for the whole court. */
    int focus() {
        return focus;
    }

    /** Glides out to the whole court, its middle at (anchorX, anchorY) of the screen. */
    void showWhole(float anchorX, float anchorY) {
        focus = -1;
        goal.at.set(COURT);
        goal.zoom = WHOLE;
        goal.yaw = 0;
        goal.pitch = 14;
        goal.anchorX = anchorX;
        goal.anchorY = anchorY;
    }

    /** Glides in on a person, standing at (anchorX, anchorY) of the screen, {@code zoom} screen heights per block. */
    void showPerson(int person, float anchorX, float anchorY) {
        showPerson(person, anchorX, anchorY, CLOSE);
    }

    void showPerson(int person, float anchorX, float anchorY, float zoom) {
        for (Figure f : figures) {
            if (f.person() == person) {
                focus = person;
                CourtSet.Seat s = f.seat();
                goal.at.set((float) s.x(), (float) s.y() + 1.0F, (float) s.z());
                goal.zoom = zoom;
                // A little from the side, as a portrait is painted, and from their own height.
                goal.yaw = s.x() > 0.5 ? -18 : 18;
                goal.pitch = 6;
                goal.anchorX = anchorX;
                goal.anchorY = anchorY;
                return;
            }
        }
    }

    /** Puts the camera where it is going at once, with no glide (for a page opened afresh). */
    void arrive() {
        now.set(goal);
    }

    /** Draws the court over the whole screen, then clears the depth so the page draws over it. */
    void render(GuiGraphics g, int width, int height, int mouseX, int mouseY, float partialTick) {
        glide();
        g.fill(0, 0, width, height, sky());
        Minecraft mc = Minecraft.getInstance();
        PoseStack pose = g.pose();
        float scale = now.zoom * height;
        pose.pushPose();
        pose.translate(now.anchorX * width, now.anchorY * height, 1500);
        pose.mulPoseMatrix(new Matrix4f().scaling(scale, -scale, scale));
        pose.mulPose(Axis.XP.rotationDegrees(now.pitch));
        pose.mulPose(Axis.YP.rotationDegrees(now.yaw));
        pose.translate(-now.at.x(), -now.at.y(), -now.at.z());
        drawn = new Matrix4f(pose.last().pose());
        drawnScale = scale;

        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();
        MultiBufferSource.BufferSource buffers = g.bufferSource();
        BlockRenderDispatcher blocks = mc.getBlockRenderer();
        for (Map.Entry<BlockPos, BlockState> e : set.blocks.entrySet()) {
            BlockPos at = e.getKey();
            pose.pushPose();
            pose.translate(at.getX(), at.getY(), at.getZ());
            blocks.renderSingleBlock(e.getValue(), pose, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            pose.popPose();
        }
        buffers.endBatch();

        EntityRenderDispatcher entities = mc.getEntityRenderDispatcher();
        entities.setRenderShadow(false);
        Lighting.setupForEntityInInventory();
        for (Figure f : figures) {
            AbstractRecruitEntity e = f.entity();
            if (e == null) {
                continue;
            }
            // Facing the hall's front; the one looked at turns their head a little toward the mouse.
            float head = f.person() == focus ? Math.max(-40, Math.min(40, (mouseX - now.anchorX * width) / width * 90)) : 0;
            e.setYRot(0);
            e.yRotO = 0;
            e.setYBodyRot(0);
            e.yBodyRotO = 0;
            e.setYHeadRot(head);
            e.yHeadRotO = head;
            e.setXRot(0);
            e.xRotO = 0;
            CourtSet.Seat s = f.seat();
            RenderSystem.runAsFancy(() -> entities.render(e, s.x(), s.y(), s.z(), 0, 1, pose, buffers, LightTexture.FULL_BRIGHT));
        }
        buffers.endBatch();
        entities.setRenderShadow(true);
        pose.popPose();
        Lighting.setupFor3DItems();
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
    }

    /** The person under the mouse (the nearest, if several), or -1. */
    int pick(double mouseX, double mouseY) {
        if (drawn == null) {
            return -1;
        }
        int best = -1;
        float nearest = -Float.MAX_VALUE;
        for (Figure f : figures) {
            CourtSet.Seat s = f.seat();
            Vector3f feet = drawn.transformPosition(new Vector3f((float) s.x(), (float) s.y(), (float) s.z()));
            Vector3f head = drawn.transformPosition(new Vector3f((float) s.x(), (float) s.y() + 1.9F, (float) s.z()));
            float half = drawnScale * 0.4F;
            if (Math.abs(mouseX - feet.x()) <= half && mouseY <= feet.y() && mouseY >= head.y() && feet.z() > nearest) {
                nearest = feet.z();
                best = f.person();
            }
        }
        return best;
    }

    /** Where a person's head is on the screen, as last drawn, or null if they have no seat. */
    @Nullable
    float[] headOnScreen(int person) {
        if (drawn == null) {
            return null;
        }
        for (Figure f : figures) {
            if (f.person() == person) {
                CourtSet.Seat s = f.seat();
                Vector3f head = drawn.transformPosition(new Vector3f((float) s.x(), (float) s.y() + 2.1F, (float) s.z()));
                return new float[]{head.x(), head.y()};
            }
        }
        return null;
    }

    /** Moves the camera toward where it is going, by how long since the last frame. */
    private void glide() {
        long time = System.nanoTime();
        float seconds = lastFrame == 0 ? 0 : Math.min(0.1F, (time - lastFrame) / 1e9F);
        lastFrame = time;
        float k = 1 - (float) Math.exp(-GLIDE * seconds);
        now.at.lerp(goal.at, k);
        now.zoom += (goal.zoom - now.zoom) * k;
        now.yaw += (goal.yaw - now.yaw) * k;
        now.pitch += (goal.pitch - now.pitch) * k;
        now.anchorX += (goal.anchorX - now.anchorX) * k;
        now.anchorY += (goal.anchorY - now.anchorY) * k;
    }
}
