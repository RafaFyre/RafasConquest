package rafasconquest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import rafasconquest.RafasConquest;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.Machine;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Foot soldiers drawn as the real recruits look, so the swap at the handover is the same soldier: the game's player
 * model as Recruits draws it, in a Recruits skin and the team cloth nearest the army's colour, wearing its real armour
 * (custom armour models too, as NeoForge hands them out) and holding its real weapon. Each model is captured once from
 * the game's own model parts onto the graphics card, each body part a bone; the shader moves the bones as the game
 * animates a mob: legs and arms swinging with the walk, the idle sway, a sword stroke in melee, a drawn bow.
 * <p>
 * Each frame only a 48-byte record per soldier goes up; each block's soldiers are then drawn once per layer (skin,
 * cloth, each armour texture, each held item). Render thread only; leaves the game's GL state as it found it.
 */
final class RecruitFigures {
    /** A model's corner: where (the model's own frame, blocks, y down), its texture spot, its normal, its bone's pivot and bone. */
    private static final int VERTEX_FLOATS = 12;
    private static final int VERTEX_BYTES = VERTEX_FLOATS * 4;
    /** A soldier's record: x y z scale, facing x z, walk swing and amount, light, flags, phase. */
    private static final int INSTANCE_BYTES = 48;
    static final int GLOW = 1;
    static final int MELEE = 2;
    static final int BOW = 4;
    static final int RIGHT_HAND = 8;
    static final int LEFT_HAND = 16;
    static final int RIDING = 32;
    /** Frames played by progress, not stride: its walk swing is how far (0..1) through them (a tower's bridge coming down, a trebuchet's shot). */
    static final int PLAYED = 64;
    private static final int TEXTURE_UNIT = 0;
    private static final int SKINS = 20;
    private static final int BODY = 0, HEAD = 1, RIGHT_ARM = 2, LEFT_ARM = 3, RIGHT_LEG = 4, LEFT_LEG = 5;
    /** The horse's bone: it doesn't turn, its frames move it. */
    static final int HORSE = 6;
    /**
     * A horse's stride, in the game's limb swing: Fresh Animations' main cycle is {@code limb_swing * 0.8}, so 2.5 pi.
     * Each gait is captured as {@link #STEPS} frames over it; the shader has both numbers written in.
     */
    static final float STRIDE = (float) (2.5 * Math.PI);
    static final int STEPS = 12;
    /** Walk, trot and run as the game's limb speed; frame 0 is the horse standing. */
    private static final float[] GAITS = {0.3F, 0.75F, 1F};
    private static final int FRAMES = 1 + GAITS.length * STEPS;
    /** How long, in game ticks, a horse being filmed stands or walks at a gait first: Fresh Animations eases into one over about 4. */
    private static final float SETTLE_TICKS = 20;
    private static final int FRAME_UNIT = 3;
    /** A texture as a render type names it ({@code RenderStateShard.TextureStateShard.toString}). */
    private static final Pattern TEXTURE = Pattern.compile("Optional\\[([a-z0-9_.-]+:[a-z0-9_./-]+)]");
    private static final String[] TEAMS = {"white", "black", "light_grey", "grey", "dark_grey", "light_blue", "blue",
            "dark_blue", "light_green", "green", "dark_green", "light_red", "red", "dark_red", "light_brown", "brown",
            "dark_brown", "light_cyan", "cyan", "dark_cyan", "yellow", "orange", "magenta", "purple", "gold"};

    private record Mesh(int vao, int vertices) {
    }

    /** One draw over a block's soldiers: a model in a texture (0: plain colour), tinted ARGB; {@code frames} its animation (0: none). */
    private record Layer(Mesh mesh, int texture, int argb, int frames) {
        Layer(Mesh mesh, int texture, int argb) {
            this(mesh, texture, argb, 0);
        }
    }

    /** How a block's soldiers look, worked out once per gear it is sent. */
    private record Look(@Nullable FigureGear gear, int rgb, List<Layer> layers, int hands) {
    }

    private static final Map<Integer, Look> LOOKS = new HashMap<>();
    private static final Map<Object, Mesh> MESHES = new HashMap<>();
    private static final Map<Integer, Integer> CLOTH = new HashMap<>();
    private static HumanoidModel<?> body, inner, outer;

    private static ByteBuffer instances = MemoryUtil.memAlloc(INSTANCE_BYTES * 1024);
    private static int count;
    private static final List<Look> RUN_LOOKS = new ArrayList<>();
    private static final List<int[]> RUNS = new ArrayList<>(); // first soldier, how many
    private static int program = -1;
    private static boolean usable = true;
    private static int instanceBuffer, mvp, modulator, time, tint, untextured, framed, debugUniform;

    private RecruitFigures() {
    }

    /** Whether a block's soldiers are drawn here: foot or horse, close enough for 3D, and the graphics card can. */
    static boolean draws(FormationSnapshot f, boolean flat) {
        return usable && !flat && switch (f.kind()) {
            case LEVY, HEAVY_INFANTRY, SKIRMISHERS, ZWEIHANDLERS, HALBERDIERS, ARCHERS, CAVALRY, HEAVY_CAVALRY, MUSKETEERS, ENGINES -> true;
            default -> false;
        };
    }

    /** Starts a frame's (or a glow pass's) soldiers. */
    static void begin() {
        count = 0;
        RUNS.clear();
        RUN_LOOKS.clear();
        filmHorses();
    }

    /** The next soldiers {@link #add}ed are of this block, until the next call. False if they can't be drawn here. */
    static boolean block(FormationSnapshot f, @Nullable FigureGear gear) {
        if (!ready()) {
            return false;
        }
        Look look = LOOKS.get(f.id());
        boolean sameGear = look != null && (look.gear == gear
                || look.gear != null && gear != null && Arrays.equals(look.gear.raw(), gear.raw()));
        if (!sameGear || look.rgb != f.rgb()) {
            look = look(f, gear);
            LOOKS.put(f.id(), look);
        }
        RUN_LOOKS.add(look);
        RUNS.add(new int[]{count, 0});
        return true;
    }

    /** The flags a block's soldiers carry: their hands, and how they fight. */
    static int flags(FormationSnapshot f) {
        Look look = LOOKS.get(f.id());
        int flags = look == null ? 0 : look.hands;
        if (f.fighting()) {
            flags |= look != null && look.gear != null && look.gear.weapon() == FigureGear.Weapon.BOW ? BOW : MELEE;
        }
        return flags;
    }

    /**
     * One soldier of the block last named: feet ({@code x}, {@code y}, {@code z}) from the camera, {@code scale} times
     * its size, facing ({@code fx}, {@code fz}), its walk cycle, light ({@link FigureBatches#lightColour}), {@link #flags} and phase (0..1, so a block's
     * soldiers don't all strike at once).
     */
    static void add(float x, float y, float z, float scale, float fx, float fz, float swing, float amount, int light, int flags,
                    float phase) {
        if (RUNS.isEmpty()) {
            return;
        }
        if ((count + 1) * INSTANCE_BYTES > instances.capacity()) {
            instances = MemoryUtil.memRealloc(instances, instances.capacity() * 2);
            instances.clear();
        }
        int at = count * INSTANCE_BYTES;
        instances.putFloat(at, x).putFloat(at + 4, y).putFloat(at + 8, z).putFloat(at + 12, scale);
        instances.putFloat(at + 16, fx).putFloat(at + 20, fz).putFloat(at + 24, swing).putFloat(at + 28, amount);
        instances.putInt(at + 32, light).putInt(at + 36, flags).putFloat(at + 40, phase).putFloat(at + 44, 0);
        count++;
        RUNS.get(RUNS.size() - 1)[1]++;
    }

    /** Draws everything added since {@link #begin}; a glow pass draws each soldier's body only, all white. */
    static void draw(Matrix4f pose, boolean glow) {
        if (program < 0 || count == 0) {
            return;
        }
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int oldActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        // Bound raw and put back after, so the game's cache of what is bound still holds.
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + TEXTURE_UNIT);
        int oldTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int oldSampler = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
        GL33.glBindSampler(TEXTURE_UNIT, 0);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        GlStateManager._glUseProgram(program);
        Matrix4f m = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(RenderSystem.getModelViewMatrix()).mul(pose);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer values = stack.mallocFloat(16);
            m.get(values);
            GL20.glUniformMatrix4fv(mvp, false, values);
        }
        float[] colour = RenderSystem.getShaderColor();
        GL20.glUniform4f(modulator, colour[0], colour[1], colour[2], colour[3]);
        GL20.glUniform1f(time, (net.minecraft.Util.getMillis() % 3_600_000L) / 1000F);
        GL20.glUniform1i(debugUniform, FigureBatches.debug.ordinal());
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instances.limit(count * INSTANCE_BYTES), GL15.GL_STREAM_DRAW);
        instances.clear();
        int bound = -1;
        for (int r = 0; r < RUNS.size(); r++) {
            int[] run = RUNS.get(r);
            if (run[1] == 0) {
                continue;
            }
            for (Layer layer : RUN_LOOKS.get(r).layers) {
                if (glow && layer != RUN_LOOKS.get(r).layers.get(0)) {
                    break; // the body alone outlines the soldier
                }
                GlStateManager._glBindVertexArray(layer.mesh.vao);
                GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
                soldierAttributes((long) run[0] * INSTANCE_BYTES);
                if (layer.texture != 0 && layer.texture != bound) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, layer.texture);
                    bound = layer.texture;
                }
                int c = layer.argb;
                GL20.glUniform4f(tint, (c >> 16 & 255) / 255F, (c >> 8 & 255) / 255F, (c & 255) / 255F, 1F);
                GL20.glUniform1i(untextured, layer.texture == 0 ? 1 : 0);
                GL20.glUniform1i(framed, layer.frames == 0 ? 0 : layer.mesh.vertices);
                if (layer.frames != 0) {
                    GL13.glActiveTexture(GL13.GL_TEXTURE0 + FRAME_UNIT);
                    GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, layer.frames);
                    GL13.glActiveTexture(GL13.GL_TEXTURE0 + TEXTURE_UNIT);
                }
                GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, layer.mesh.vertices, run[1]);
            }
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + FRAME_UNIT);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + TEXTURE_UNIT);
        GlStateManager._glUseProgram(oldProgram);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, oldTexture);
        GL33.glBindSampler(TEXTURE_UNIT, oldSampler);
        GL13.glActiveTexture(oldActive);
        RenderSystem.enableCull();
    }

    /**
     * For {@code /pvefigures}: a block's cloth, and each layer's texture and tint. GONE: deleted on the graphics card;
     * STALE: the game now holds the skin under another texture (a resource reload since the look was made).
     */
    static String describe(FormationSnapshot f) {
        Look look = LOOKS.get(f.id());
        if (look == null) {
            return "no recruit look";
        }
        StringBuilder s = new StringBuilder("cloth ").append(TEAMS[cloth(f.rgb())]);
        int skin = texture(recruits("human/human_" + Math.floorMod(f.id(), SKINS)));
        if (look.layers.get(0).texture != skin) {
            s.append(" STALE (skin now ").append(skin).append(')');
        }
        for (Layer l : look.layers) {
            s.append(" | tex ").append(l.texture).append(l.texture != 0 && !GL11.glIsTexture(l.texture) ? " GONE" : "")
                    .append(" tint ").append(Integer.toHexString(l.argb));
        }
        return s.toString();
    }

    /**
     * Lets go of the looks (their textures and armour models) on leaving the world, and of horses still being filmed
     * there (filmed again in the next). The models on the card stay.
     */
    static void clear() {
        LOOKS.clear();
        TOWNSFOLK.clear();
        ENGINES.clear();
        TOWERS.clear();
        HORSES.values().removeAll(FILMING);
        FILMING.clear();
    }

    // ---- what a block looks like ----

    /** Townsfolk's looks, by skin: the body alone, no cloth or gear. */
    private static final Map<Integer, Look> TOWNSFOLK = new HashMap<>();

    /** The next figures {@link #add}ed are townsfolk in Recruits' skin {@code skin}, until the next call. False if they can't be drawn. */
    static boolean townsfolk(int skin) {
        if (!usable || !ready()) {
            return false;
        }
        RUN_LOOKS.add(TOWNSFOLK.computeIfAbsent(skin, s ->
                new Look(null, 0, List.of(new Layer(bodyMesh(), texture(recruits("human/human_" + s)), -1)), 0)));
        RUNS.add(new int[]{count, 0});
        return true;
    }

    /** Siege engines' looks, by kind and bridge: the real Siegeworks models; no layers where one can't be captured. */
    private static final Map<Integer, Look> ENGINES = new HashMap<>();

    /**
     * The next figures {@link #add}ed are Siegeworks engines of {@link Machine} {@code kind} (a tower {@code up}: its
     * bridge down), until the next call; added unmoving ({@code amount} 0). False if it can't be drawn here.
     */
    static boolean engine(int kind, boolean up) {
        if (!usable || !ready() || Machine.get(kind) == null || Minecraft.getInstance().level == null) {
            return false;
        }
        Machine machine = Machine.get(kind);
        boolean bridge = up && machine.role() == Machine.Role.TOWER;
        Look look = ENGINES.computeIfAbsent(kind * 2 + (bridge ? 1 : 0), k -> engineLook(machine, bridge));
        if (look.layers.isEmpty()) {
            return false;
        }
        RUN_LOOKS.add(look);
        RUNS.add(new int[]{count, 0});
        return true;
    }

    /** The engine as the game draws it, through its own (GeckoLib) renderer, standing still: one layer per texture it drew. */
    private static Look engineLook(Machine machine, boolean bridge) {
        Minecraft mc = Minecraft.getInstance();
        List<Layer> layers = new ArrayList<>();
        try {
            net.minecraft.world.entity.Entity e = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE
                    .getOptional(ResourceLocation.parse(machine.entity())).map(type -> type.create(mc.level)).orElse(null);
            if (e != null && machine.role() == Machine.Role.TOWER) {
                layers.addAll(filmTower(mc, e, bridge));
            } else if (e != null && machine.throwing()) { // filmed through its throw, as Siegeworks animates a trebuchet
                layers.addAll(filmTrebuchet(mc, e));
            } else if (e != null) {
                for (Capture c : film(mc, e).values()) {
                    if (c.texture != null && c.size > 0) {
                        layers.add(new Layer(upload(c), texture(c.texture), c.argb));
                    }
                }
            }
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ex) {
            RafasConquest.LOGGER.warn("[figures] can't capture a {}: {}", machine.id, ex.toString());
        }
        RafasConquest.LOGGER.info("[figures] {}{}: {} layers from its own model{}", machine.id, bridge ? " (bridge down)" : "",
                layers.size(), layers.isEmpty() ? ", drawn as boxes" : "");
        return new Look(null, 0, layers, 0);
    }

    /** A tower's wheels turn 72 degrees a block it rolls forward (Siegeworks' {@code updateTowerWheelRotation}): a turn in 5. */
    private static final double WHEEL_TURN = 5;
    /** Its bridge drops all the way in 160 ticks ({@code TowerBridgeController.PROGRESS_STEP} 0.00625 a tick). */
    private static final double BRIDGE_MILLIS = 8000;
    /** Frames {@link #PLAYED} by progress, after the first: as many as a horse's gaits, the shader has it written in. */
    private static final int PLAYED_FRAMES = GAITS.length * STEPS;

    /**
     * A siege tower filmed as the horse is, all in one frame (Siegeworks poses it from its fields each render; GeckoLib
     * only re-poses it on a new tick, so each shot is a tick on). Bridge up: frame 0 then {@link #STEPS} shots over a
     * turn of its wheels, the shader's walk frames. Bridge down: frame 0 down, then {@link #PLAYED_FRAMES} shots of it
     * coming down from up, {@link #PLAYED}.
     */
    private static List<Layer> filmTower(Minecraft mc, net.minecraft.world.entity.Entity tower, boolean bridge) throws ReflectiveOperationException {
        java.lang.reflect.Field field = tower.getClass().getDeclaredField("bridge");
        field.setAccessible(true);
        Object controller = field.get(tower);
        java.lang.reflect.Method progress = controller.getClass().getDeclaredMethod("setProgress", float.class); // private
        progress.setAccessible(true);
        java.lang.reflect.Field wheels = tower.getClass().getField("wheelRotation");
        progress.invoke(controller, bridge ? 1F : 0F);
        Map<String, Capture> first = film(mc, tower);
        List<Map<String, Capture>> shots = new ArrayList<>();
        shots.add(first);
        for (int k = 0; k < (bridge ? PLAYED_FRAMES : STEPS); k++) {
            if (bridge) {
                progress.invoke(controller, k / (PLAYED_FRAMES - 1F));
            } else {
                wheels.setFloat(tower, Math.floorMod(-k * 360 / STEPS, 360)); // rolling forward winds them back
            }
            tower.tickCount++;
            shots.add(film(mc, tower));
        }
        return framed(shots, bridge ? "siege tower (bridge)" : "siege tower (wheels)");
    }

    /** Filmed shots as layers, one per texture the first drew, each moving through its frames on the card. */
    private static List<Layer> framed(List<Map<String, Capture>> shots, String what) {
        List<Layer> layers = new ArrayList<>();
        for (Map.Entry<String, Capture> layer : shots.get(0).entrySet()) {
            Capture c = layer.getValue();
            if (c.texture != null && c.size > 0) {
                float[] packed = pack(c, shots.stream().map(shot -> shot.get(layer.getKey())).toList());
                layers.add(new Layer(upload(c), texture(c.texture), c.argb, frameBuffer(packed)));
                RafasConquest.LOGGER.info("[figures] {} layer {}: {} frames, moves {} blocks at most", what, c.texture,
                        shots.size(), spread(packed, c.triangles().length / VERTEX_FLOATS));
            }
        }
        return layers;
    }

    /** Siegeworks' trebuchet animations, in ticks: {@code shoot} ({@code SHOOT_ANIMATION_TICKS}), then {@code reload} at speed 1 (its 16.17 s). */
    private static final int SHOT_TICKS = 65, RELOAD_TICKS = 323;
    /** Of the {@link #PLAYED_FRAMES}, the first this many are the shot, the rest the reload ending loaded. */
    private static final int SHOT_FRAMES = 12;

    /**
     * A trebuchet filmed through its own GeckoLib animations, which GeckoLib times by the entity's tick count: loaded
     * first (frame 0), then its {@code shoot} triggered and {@link #SHOT_FRAMES} shots over it, then (ammo in and
     * winding, as Siegeworks' controller asks) its {@code reload} over the other frames, ending loaded again.
     */
    private static List<Layer> filmTrebuchet(Minecraft mc, net.minecraft.world.entity.Entity treb) throws ReflectiveOperationException {
        Class<?> base = treb.getClass().getSuperclass(); // AbstractSiegeEntity
        java.lang.reflect.Field weaponField = base.getDeclaredField("weapon");
        weaponField.setAccessible(true);
        Object weapon = weaponField.get(treb);
        weapon.getClass().getMethod("setWindingTotal", int.class).invoke(weapon, RELOAD_TICKS); // reload at speed 1
        java.lang.reflect.Method ammo = treb.getClass().getMethod("setAmmoLoaded", String.class);
        java.lang.reflect.Method winding = treb.getClass().getMethod("setWindingTime", int.class);
        ammo.invoke(treb, "minecraft:stone");
        winding.invoke(treb, 0);
        List<Map<String, Capture>> shots = new ArrayList<>();
        shots.add(film(mc, treb)); // loaded
        treb.tickCount++;
        treb.getClass().getMethod("triggerAnim", String.class, String.class).invoke(treb, "anim_controller", "shoot");
        shots.add(film(mc, treb)); // the shot begins on this tick
        int start = treb.tickCount;
        for (int k = 1; k < SHOT_FRAMES; k++) {
            treb.tickCount = start + Math.round(k * (float) SHOT_TICKS / (SHOT_FRAMES - 1));
            shots.add(film(mc, treb));
        }
        treb.tickCount = start + SHOT_TICKS + 5; // the shot done, back to the controller's choice: reload
        winding.invoke(treb, 1);
        film(mc, treb);
        start = treb.tickCount;
        int reloads = PLAYED_FRAMES - SHOT_FRAMES;
        for (int k = 0; k < reloads; k++) {
            treb.tickCount = start + 1 + Math.round(k * (float) RELOAD_TICKS / (reloads - 1));
            shots.add(film(mc, treb));
        }
        return framed(shots, "trebuchet");
    }

    /**
     * How a trebuchet moves this frame, for {@link #add}: shooting, it throws then winds back, once every
     * {@code Machine.TREBUCHET.reload} seconds, {@code phase} (0..1) through its turn; otherwise it stands loaded.
     */
    static float[] trebuchet(boolean shooting, float phase) {
        if (!shooting) {
            return new float[]{1, 0, PLAYED};
        }
        double shot = SHOT_TICKS / 20.0, reload = RELOAD_TICKS / 20.0;
        double t = trebuchetTime(phase, 0);
        double frame = t < shot ? t / shot * (SHOT_FRAMES - 1)
                : t < shot + reload ? SHOT_FRAMES + (t - shot) / reload * (PLAYED_FRAMES - SHOT_FRAMES - 1) : PLAYED_FRAMES - 1;
        return new float[]{(float) (frame / (PLAYED_FRAMES - 1)), 0, PLAYED};
    }

    /** How long (s) before a trebuchet's shot frames begin its shot sound starts. */
    private static final double SHOT_SOUND_LEAD = 0.25;

    /** How far (s) a shooting trebuchet is through its turn, {@code lead} seconds ahead; {@code phase} (0..1) sets where it starts. */
    private static double trebuchetTime(float phase, double lead) {
        double period = Math.max(Machine.TREBUCHET.reload, (SHOT_TICKS + RELOAD_TICKS) / 20.0);
        return (net.minecraft.Util.getMillis() / 1000.0 + lead + phase * period) % period;
    }

    /** Each shooting trebuchet's turn time (sound lead added) when last asked, by its key. */
    private static final Map<Long, Double> TREBUCHET_TIMES = new HashMap<>();

    /** Whether the trebuchet {@code key}'s shot sound is due: its turn, {@link #SHOT_SOUND_LEAD} ahead, wrapped round since last asked. */
    static boolean trebuchetSoundDue(long key, float phase) {
        double t = trebuchetTime(phase, SHOT_SOUND_LEAD);
        Double last = TREBUCHET_TIMES.put(key, t);
        if (TREBUCHET_TIMES.size() > 4096) {
            TREBUCHET_TIMES.clear(); // shortcut: ended battles leave keys behind; a clear only skips one shot's sound
        }
        return last != null && t < last;
    }

    /** Each tower's last spot, its wheels' travel forward and when its bridge began to come down (0: up), by formation. */
    private static final Map<Integer, double[]> TOWERS = new HashMap<>();

    /**
     * How a tower of formation {@code id} at ({@code x}, {@code z}) moves this frame, for {@link #add}: walk swing,
     * amount and flags. Bridge up, its wheels roll as far as it went; down, its bridge comes down over
     * {@link #BRIDGE_MILLIS} from when it was first seen down (all the way, if it was down when first seen).
     */
    static float[] tower(int id, double x, double z, double fx, double fz, boolean up) {
        long now = net.minecraft.Util.getMillis();
        double[] t = TOWERS.computeIfAbsent(id, k -> new double[]{x, z, 0, up ? now - BRIDGE_MILLIS : 0});
        double forward = (x - t[0]) * fx + (z - t[1]) * fz;
        t[2] += Math.abs(forward) < 4 ? forward : 0; // a jump (a new battle under the same id) doesn't spin them
        t[0] = x;
        t[1] = z;
        if (!up) {
            t[3] = 0;
            return new float[]{(float) (t[2] / WHEEL_TURN * STRIDE), 0.3F, 0};
        }
        if (t[3] == 0) {
            t[3] = now;
        }
        return new float[]{(float) Math.min(1, (now - t[3]) / BRIDGE_MILLIS), 0, PLAYED};
    }

    private static Mesh bodyMesh() {
        return MESHES.computeIfAbsent("body", k -> {
            Capture c = new Capture();
            humanoid(c, models()[0], List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET));
            return upload(c);
        });
    }

    private static Look look(FormationSnapshot f, @Nullable FigureGear gear) {
        Minecraft mc = Minecraft.getInstance();
        List<Layer> layers = new ArrayList<>();
        Mesh bodyMesh = bodyMesh();
        layers.add(new Layer(bodyMesh, texture(recruits("human/human_" + Math.floorMod(f.id(), SKINS))), -1));
        layers.add(new Layer(bodyMesh, texture(recruits("human/human_team_" + TEAMS[cloth(f.rgb())])), -1));
        int hands = 0;
        LivingEntity wearer = mc.player;
        if (gear != null && wearer != null) {
            // The game's order: chest, legs, feet, head.
            int[] slots = {1, 2, 3, 0};
            EquipmentSlot[] equipment = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (int s : slots) {
                armour(layers, wearer, gear, s, equipment[s]);
            }
            Item main = gear.worn(4);
            Mesh right = main == null ? null : held(main, false);
            if (right != null) {
                layers.add(new Layer(right, texture(TextureAtlas.LOCATION_BLOCKS), -1));
                hands |= RIGHT_HAND;
            }
            Item off = gear.worn(5);
            Mesh left = off == null ? null : held(off, true);
            if (left != null) {
                layers.add(new Layer(left, texture(TextureAtlas.LOCATION_BLOCKS), -1));
                hands |= LEFT_HAND;
            } else if (gear.shield()) { // drawn by its own renderer, as a vanilla shield is: a board in its colour
                layers.add(new Layer(MESHES.computeIfAbsent("shield", k -> shieldBoard()), 0, gear.off() < 0 ? f.rgb() : gear.off()));
                hands |= LEFT_HAND;
            }
        }
        if (f.kind().mounted()) {
            layers.addAll(horse(f, gear));
            hands |= RIDING;
        }
        return new Look(gear, f.rgb(), layers, hands);
    }

    private static final Map<List<Object>, Film> HORSES = new HashMap<>();
    private static final List<Film> FILMING = new ArrayList<>();
    private static final Map<Variant, ResourceLocation> COATS = new HashMap<>();

    /**
     * A rider's horse, in its barding: a real horse drawn by the game's own renderer, so it has whatever shape, coat and
     * animation the resource packs give it (Fresh Animations through Entity Model Features). One layer per texture it
     * drew, the first its coat; each moves through the frames its {@link Film} took of it.
     */
    private static List<Layer> horse(FormationSnapshot f, @Nullable FigureGear gear) {
        Variant coat = Variant.values()[Math.floorMod(f.id(), Variant.values().length)];
        Item barding = gear == null ? null : gear.worn(6);
        int dye = gear == null ? -1 : gear.dye(6);
        List<Capture> steed = HORSES.computeIfAbsent(List.<Object>of(barding == null ? "" : barding, dye), k -> Film.start(barding, dye)).layers;
        List<Layer> layers = new ArrayList<>();
        for (int i = 0; i < steed.size(); i++) {
            Capture c = steed.get(i);
            ResourceLocation texture = i == 0 ? COATS.getOrDefault(coat, c.texture) : c.texture;
            layers.add(new Layer(c.mesh, texture(texture), c.argb, c.frames));
        }
        return layers;
    }

    /**
     * A horse in its barding being filmed: standing, then at each gait, {@link #STEPS} points over a {@link #STRIDE}.
     * Fresh Animations eases into a trot or a run (and drifts its stride) only once per real frame, so the horse stands
     * or walks at each gait over {@link #SETTLE_TICKS} of real frames, one shot a frame, before that gait's points are
     * all taken in one frame, where it holds the gait still. Its first shot is up at once, so the horse stands in that
     * until its frames are done; then each layer's frames go to the graphics card as one buffer the shader reads.
     */
    private static final class Film {
        /** One per texture the horse drew, the first its coat. */
        final List<Capture> layers = new ArrayList<>();
        private final Map<String, Capture> first;
        private final List<Map<String, Capture>> shots = new ArrayList<>();
        @Nullable
        private final Horse horse;
        private int gait = -1;
        private float walked, at;

        private Film(@Nullable Horse horse, Map<String, Capture> first) {
            this.horse = horse;
            this.first = first;
            for (Capture c : first.values()) {
                if (c.texture != null && c.size > 0) {
                    c.mesh = upload(c);
                    layers.add(c);
                }
            }
        }

        /** Starts filming a horse in this barding; also notes which texture each coat is drawn in. */
        static Film start(@Nullable Item barding, int dye) {
            Minecraft mc = Minecraft.getInstance();
            try {
                if (mc.level == null) {
                    return new Film(null, Map.of());
                }
                Horse horse = new Horse(EntityType.HORSE, mc.level) {
                    @Override
                    public boolean isSaddled() {
                        return true;
                    }
                };
                horse.setOnGround(true);
                if (barding != null) {
                    ItemStack stack = new ItemStack(barding);
                    if (dye >= 0 && stack.is(ItemTags.DYEABLE)) {
                        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(dye, false));
                    }
                    horse.setBodyArmorItem(stack);
                }
                for (Variant coat : Variant.values()) {
                    horse.setVariant(coat);
                    Capture first = film(mc, horse).values().stream().findFirst().orElse(null);
                    if (first != null && first.texture != null) {
                        COATS.put(coat, first.texture);
                    }
                }
                horse.setVariant(Variant.WHITE);
                Film film = new Film(horse, film(mc, horse));
                FILMING.add(film);
                return film;
            } catch (RuntimeException e) {
                RafasConquest.LOGGER.warn("[figures] can't capture a horse: {}", e.toString());
                return new Film(null, Map.of());
            }
        }

        /** This frame's shot; true once the film is done (or can't go on). */
        boolean shoot(Minecraft mc) {
            try {
                float speed = gait < 0 ? 0 : GAITS[gait];
                if (walked < SETTLE_TICKS) {
                    walked += mc.getTimer().getGameTimeDeltaTicks();
                    at += speed;
                    stride(horse.walkAnimation, at, speed);
                    film(mc, horse);
                    return false;
                }
                at = (float) Math.ceil(at / STRIDE) * STRIDE;
                for (int k = 0; k < (gait < 0 ? 1 : STEPS); k++) {
                    stride(horse.walkAnimation, at + k * STRIDE / STEPS, speed);
                    shots.add(film(mc, horse));
                }
                walked = 0;
                if (++gait < GAITS.length) {
                    return false;
                }
                for (Map.Entry<String, Capture> layer : first.entrySet()) {
                    Capture c = layer.getValue();
                    if (c.mesh == null) {
                        continue;
                    }
                    float[] packed = pack(c, shots.stream().map(shot -> shot.get(layer.getKey())).toList());
                    c.frames = frameBuffer(packed);
                    RafasConquest.LOGGER.info("[figures] horse layer {}: {} corners, {} frames, moves {} blocks at most",
                            c.texture, c.mesh.vertices(), FRAMES, spread(packed, c.mesh.vertices()));
                }
                LOOKS.clear(); // the looks made meanwhile hold the standing horse
            } catch (RuntimeException e) {
                RafasConquest.LOGGER.warn("[figures] can't film a horse: {}", e.toString());
            }
            return true;
        }
    }

    /** Takes this frame's shot of each horse being filmed (again in a glow pass, which Fresh Animations ignores). */
    private static void filmHorses() {
        Minecraft mc = Minecraft.getInstance();
        if (!FILMING.isEmpty() && !mc.isPaused()) {
            FILMING.removeIf(film -> film.shoot(mc));
        }
    }

    /**
     * Sets a walk to {@code position} at {@code speed}, the speed the same at any partial tick: Entity Model Features
     * reads it at the game's partial tick, not the renderer's.
     */
    static void stride(WalkAnimationState walk, float position, float speed) {
        float step = position - walk.position() - speed;
        walk.setSpeed(step);
        walk.update(step, 0);
        walk.setSpeed(speed);
        walk.update(speed, 0); // the speed it was a tick ago is now this one too
    }

    /**
     * What the game draws of a horse now, by texture, in the model's frame. Drawn through the entity dispatcher, where
     * Entity Texture Features tells Entity Model Features which entity it draws: a renderer called alone isn't animated.
     */
    private static Map<String, Capture> film(Minecraft mc, net.minecraft.world.entity.Entity horse) {
        if (mc.player != null) { // where Entity Model Features animates in full, not at its distant rate
            horse.setPos(mc.player.position());
        }
        Map<String, Capture> byTexture = new LinkedHashMap<>();
        MultiBufferSource buffers = type -> {
            Matcher m = TEXTURE.matcher(type.toString());
            String texture = m.find() ? m.group(1) : "";
            return byTexture.computeIfAbsent(texture, t -> {
                Capture c = new Capture();
                c.entityFrame = true;
                c.bone(HORSE, 0, 0, 0);
                c.texture = t.isEmpty() ? null : ResourceLocation.parse(t);
                return c;
            });
        };
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        try {
            dispatcher.render(horse, 0, 0, 0, 0, 1, new PoseStack(), buffers, LightTexture.FULL_BRIGHT);
        } finally {
            dispatcher.setRenderShadow(true);
        }
        return byTexture;
    }

    /**
     * A layer's frames as the shader reads them: per frame, per corner of its triangles, x y z and the normal, eight
     * floats. Corners are counted as {@code shape} (the mesh on the card) draws them; a frame that is missing or drew
     * another number (a part shown or hidden) repeats the one before, the first repeating {@code shape}.
     */
    static float[] pack(Capture shape, List<Capture> film) {
        float[] before = shape.triangles();
        int corners = before.length / VERTEX_FLOATS;
        float[] out = new float[film.size() * corners * 8];
        int skipped = 0;
        for (int f = 0; f < film.size(); f++) {
            float[] t = film.get(f) == null ? null : film.get(f).triangles();
            if (t == null || t.length != before.length) {
                t = before;
                skipped++;
            }
            before = t;
            for (int v = 0; v < corners; v++) {
                int o = (f * corners + v) * 8;
                int i = v * VERTEX_FLOATS;
                out[o] = t[i];
                out[o + 1] = t[i + 1];
                out[o + 2] = t[i + 2];
                out[o + 3] = t[i + 5];
                out[o + 4] = t[i + 6];
                out[o + 5] = t[i + 7];
            }
        }
        if (skipped > 0) {
            RafasConquest.LOGGER.warn("[figures] {} horse frames drew other parts than the first; they repeat the frame before", skipped);
        }
        return out;
    }

    /** How far the corner that moves most moves over the frames: near 0 means the horse wasn't animated. */
    private static float spread(float[] packed, int corners) {
        float most = 0;
        for (int v = 0; v < corners; v++) {
            for (int f = 1; f < packed.length / 8 / corners; f++) {
                for (int a = 0; a < 3; a++) {
                    most = Math.max(most, Math.abs(packed[(f * corners + v) * 8 + a] - packed[v * 8 + a]));
                }
            }
        }
        return most;
    }

    /** A buffer texture over the packed frames, two RGBA float texels a corner. */
    private static int frameBuffer(float[] packed) {
        int buffer = GlStateManager._glGenBuffers();
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, buffer);
        ByteBuffer data = MemoryUtil.memAlloc(packed.length * 4);
        data.asFloatBuffer().put(packed);
        GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, data, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(data);
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        int oldActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + FRAME_UNIT);
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, texture);
        GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_RGBA32F, buffer);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(oldActive);
        return texture;
    }

    /** A worn piece's layers, as the game's armour layer draws them: its model (the item's own, if it has one), each texture, tinted. */
    private static void armour(List<Layer> layers, LivingEntity wearer, FigureGear gear, int s, EquipmentSlot slot) {
        if (!(gear.worn(s) instanceof ArmorItem armour) || armour.getEquipmentSlot() != slot) {
            return;
        }
        try {
            ItemStack stack = new ItemStack(armour);
            if (gear.dye(s) >= 0 && stack.is(ItemTags.DYEABLE)) {
                stack.set(DataComponents.DYED_COLOR, new DyedItemColor(gear.dye(s), false));
            }
            boolean innerModel = slot == EquipmentSlot.LEGS;
            HumanoidModel<?> fallback = models()[innerModel ? 1 : 2];
            Model model = ClientHooks.getArmorModel(wearer, stack, slot, fallback);
            Mesh mesh = MESHES.computeIfAbsent(List.of(model, slot), k -> {
                Capture c = new Capture();
                if (model instanceof HumanoidModel<?> humanoid) {
                    humanoid(c, humanoid, List.of(slot));
                } else {
                    c.bone(BODY, 0, 0, 0);
                    model.renderToBuffer(new PoseStack(), c, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
                }
                return upload(c);
            });
            IClientItemExtensions extensions = IClientItemExtensions.of(stack);
            int fallbackColour = extensions.getDefaultDyeColor(stack);
            List<ArmorMaterial.Layer> materialLayers = armour.getMaterial().value().layers();
            for (int i = 0; i < materialLayers.size(); i++) {
                ArmorMaterial.Layer layer = materialLayers.get(i);
                int colour = extensions.getArmorLayerTintColor(stack, wearer, layer, i, fallbackColour);
                if (colour != 0) {
                    layers.add(new Layer(mesh, texture(ClientHooks.getArmorTexture(wearer, stack, layer, innerModel, slot)), colour));
                }
            }
        } catch (RuntimeException e) {
            RafasConquest.LOGGER.warn("[figures] can't dress a figure in {}: {}", armour, e.toString());
        }
    }

    /** The parts a slot shows ({@code HumanoidArmorLayer.setPartVisibility}); the body shows head, chest and feet's together: all of them. */
    private static void humanoid(Capture c, HumanoidModel<?> model, List<EquipmentSlot> slots) {
        boolean head = false, body = false, arms = false, legs = false;
        for (EquipmentSlot slot : slots) {
            switch (slot) {
                case HEAD -> head = true;
                case CHEST -> {
                    body = true;
                    arms = true;
                }
                case LEGS -> {
                    body = true;
                    legs = true;
                }
                case FEET -> legs = true;
                default -> {
                }
            }
        }
        if (head) {
            part(c, model.head, HEAD);
            part(c, model.hat, HEAD);
        }
        if (body) {
            part(c, model.body, BODY);
        }
        if (arms) {
            part(c, model.rightArm, RIGHT_ARM);
            part(c, model.leftArm, LEFT_ARM);
        }
        if (legs) {
            part(c, model.rightLeg, RIGHT_LEG);
            part(c, model.leftLeg, LEFT_LEG);
        }
    }

    private static void part(Capture c, ModelPart part, int bone) {
        part.resetPose();
        boolean visible = part.visible;
        part.visible = true;
        c.bone(bone, part.x / 16, part.y / 16, part.z / 16);
        part.render(new PoseStack(), c, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        part.visible = visible;
    }

    /** An item in hand, as the game's held-item layer places it on the arm; null for one drawn by its own renderer. */
    @Nullable
    private static Mesh held(Item item, boolean left) {
        return MESHES.computeIfAbsent(List.of(item, left), k -> {
            try {
                Minecraft mc = Minecraft.getInstance();
                ItemStack stack = new ItemStack(item);
                BakedModel baked = mc.getItemRenderer().getModel(stack, null, null, 0);
                if (baked.isCustomRenderer()) {
                    return null;
                }
                ModelPart arm = left ? models()[0].leftArm : models()[0].rightArm;
                arm.resetPose();
                PoseStack pose = new PoseStack();
                arm.translateAndRotate(pose);
                pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
                pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                pose.translate((left ? -1 : 1) / 16.0F, 0.125F, -0.625F);
                Capture c = new Capture();
                c.bone(left ? LEFT_ARM : RIGHT_ARM, arm.x / 16, arm.y / 16, arm.z / 16);
                mc.getItemRenderer().render(stack, left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        left, pose, type -> c, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, baked);
                return c.size == 0 ? null : upload(c);
            } catch (RuntimeException e) {
                RafasConquest.LOGGER.warn("[figures] can't put {} in a figure's hand: {}", item, e.toString());
                return null;
            }
        });
    }

    /** A board on the left arm's outer side, for a shield: 1 by 12 by 12 pixels. */
    private static Mesh shieldBoard() {
        ModelPart arm = models()[0].leftArm;
        arm.resetPose();
        Capture c = new Capture();
        float px = arm.x / 16, py = arm.y / 16, pz = arm.z / 16;
        c.bone(LEFT_ARM, px, py, pz);
        c.box(px + 3 / 16F, py, pz - 6 / 16F, px + 4 / 16F, py + 12 / 16F, pz + 6 / 16F);
        return upload(c);
    }

    /**
     * The recruit's body, inner armour and outer armour models, baked from the game's own shapes. Not from the entity
     * model set: Entity Model Features packs (Fresh Animations) re-rig those, and at rest their head sits in the chest.
     */
    static HumanoidModel<?>[] models() {
        if (body == null) {
            body = new HumanoidModel<>(LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 64, 64).bakeRoot());
            inner = new HumanoidModel<>(LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32).bakeRoot());
            outer = new HumanoidModel<>(LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32).bakeRoot());
        }
        return new HumanoidModel<?>[]{body, inner, outer};
    }

    private static ResourceLocation recruits(String path) {
        return ResourceLocation.fromNamespaceAndPath("recruits", "textures/entity/" + path + ".png");
    }

    private static int texture(ResourceLocation location) {
        return Minecraft.getInstance().getTextureManager().getTexture(location).getId();
    }

    /** The team cloth nearest an army's colour, by each cloth texture's average colour. */
    private static int cloth(int rgb) {
        return CLOTH.computeIfAbsent(rgb, c -> {
            int best = 0;
            long nearest = Long.MAX_VALUE;
            for (int i = 0; i < TEAMS.length; i++) {
                int avg = average(recruits("human/human_team_" + TEAMS[i]));
                long dr = (avg >> 16 & 255) - (c >> 16 & 255);
                long dg = (avg >> 8 & 255) - (c >> 8 & 255);
                long db = (avg & 255) - (c & 255);
                long d = dr * dr + dg * dg + db * db;
                if (avg >= 0 && d < nearest) {
                    nearest = d;
                    best = i;
                }
            }
            return best;
        });
    }

    private static final Map<ResourceLocation, Integer> AVERAGES = new HashMap<>();

    /** A texture's average colour, its see-through pixels left out; -1 if it can't be read. */
    private static int average(ResourceLocation location) {
        return AVERAGES.computeIfAbsent(location, l -> {
            try (InputStream in = Minecraft.getInstance().getResourceManager().open(l); NativeImage image = NativeImage.read(in)) {
                long r = 0, g = 0, b = 0, n = 0;
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int p = image.getPixelRGBA(x, y); // ABGR
                        if ((p >>> 24) < 128) {
                            continue;
                        }
                        r += p & 255;
                        g += p >> 8 & 255;
                        b += p >> 16 & 255;
                        n++;
                    }
                }
                return n == 0 ? -1 : (int) (r / n) << 16 | (int) (g / n) << 8 | (int) (b / n);
            } catch (Exception e) {
                return -1;
            }
        });
    }

    // ---- capturing the game's models ----

    /** Takes what the game's model parts and item renderer draw: quads, each corner tagged with the bone it moves with. */
    static final class Capture implements VertexConsumer {
        float[] v = new float[VERTEX_FLOATS * 256];
        int size;
        /** Taken in an entity renderer's space (blocks, y up, feet at 0, front +z), turned to the model's frame. */
        boolean entityFrame;
        /** What a renderer drew this in: its texture, and the colour it last gave. */
        @Nullable
        ResourceLocation texture;
        int argb = -1;
        @Nullable
        Mesh mesh;
        /** Its frames' buffer texture, for a horse; 0 for none. */
        int frames;
        private int bone;
        private float px, py, pz;

        void bone(int bone, float px, float py, float pz) {
            this.bone = bone;
            this.px = px;
            this.py = py;
            this.pz = pz;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            if (size + VERTEX_FLOATS > v.length) {
                v = Arrays.copyOf(v, v.length * 2);
            }
            v[size] = x;
            v[size + 1] = entityFrame ? 1.5F - y : y;
            v[size + 2] = entityFrame ? -z : z;
            v[size + 8] = px;
            v[size + 9] = py;
            v[size + 10] = pz;
            v[size + 11] = bone;
            size += VERTEX_FLOATS;
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float w) {
            v[size - VERTEX_FLOATS + 3] = u;
            v[size - VERTEX_FLOATS + 4] = w;
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            v[size - VERTEX_FLOATS + 5] = x;
            v[size - VERTEX_FLOATS + 6] = entityFrame ? -y : y;
            v[size - VERTEX_FLOATS + 7] = entityFrame ? -z : z;
            return this;
        }

        @Override
        public VertexConsumer setColor(int r, int g, int b, int a) {
            argb = a << 24 | r << 16 | g << 8 | b;
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int w) {
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int w) {
            return this;
        }

        /** A plain box from corner to corner, its faces' normals out. */
        void box(float x0, float y0, float z0, float x1, float y1, float z1) {
            float[][] faces = {
                    {x0, y0, z0, x0, y1, z0, x0, y1, z1, x0, y0, z1, -1, 0, 0},
                    {x1, y0, z1, x1, y1, z1, x1, y1, z0, x1, y0, z0, 1, 0, 0},
                    {x0, y0, z0, x0, y0, z1, x1, y0, z1, x1, y0, z0, 0, -1, 0},
                    {x0, y1, z1, x0, y1, z0, x1, y1, z0, x1, y1, z1, 0, 1, 0},
                    {x1, y0, z0, x1, y1, z0, x0, y1, z0, x0, y0, z0, 0, 0, -1},
                    {x0, y0, z1, x0, y1, z1, x1, y1, z1, x1, y0, z1, 0, 0, 1}};
            for (float[] f : faces) {
                for (int i = 0; i < 4; i++) {
                    addVertex(f[i * 3], f[i * 3 + 1], f[i * 3 + 2]).setUv(0, 0).setNormal(f[12], f[13], f[14]);
                }
            }
        }

        /** The quads as triangles, two each. */
        float[] triangles() {
            int quads = size / (VERTEX_FLOATS * 4);
            float[] out = new float[quads * 6 * VERTEX_FLOATS];
            int o = 0;
            for (int q = 0; q < quads; q++) {
                for (int corner : new int[]{0, 1, 2, 0, 2, 3}) {
                    System.arraycopy(v, (q * 4 + corner) * VERTEX_FLOATS, out, o, VERTEX_FLOATS);
                    o += VERTEX_FLOATS;
                }
            }
            return out;
        }
    }

    // ---- the graphics card's side ----

    private static Mesh upload(Capture c) {
        float[] corners = c.triangles();
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int vao = GlStateManager._glGenVertexArrays();
        GlStateManager._glBindVertexArray(vao);
        int model = GlStateManager._glGenBuffers();
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, model);
        ByteBuffer data = MemoryUtil.memAlloc(Math.max(4, corners.length * 4));
        data.asFloatBuffer().put(corners);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(data);
        FigureBatches.floats(0, 3, VERTEX_BYTES, 0, false);
        FigureBatches.floats(1, 2, VERTEX_BYTES, 12, false);
        FigureBatches.floats(2, 3, VERTEX_BYTES, 20, false);
        FigureBatches.floats(3, 3, VERTEX_BYTES, 32, false);
        FigureBatches.floats(4, 1, VERTEX_BYTES, 44, false);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        return new Mesh(vao, corners.length / VERTEX_FLOATS);
    }

    /** Points the bound vertex array's soldier attributes at the records from byte {@code from} on. */
    private static void soldierAttributes(long from) {
        FigureBatches.floats(5, 4, INSTANCE_BYTES, from, true);
        FigureBatches.floats(6, 4, INSTANCE_BYTES, from + 16, true);
        FigureBatches.ints(7, 2, INSTANCE_BYTES, from + 32, true);
        FigureBatches.floats(8, 1, INSTANCE_BYTES, from + 40, true);
    }

    private static boolean ready() {
        if (program >= 0) {
            return true;
        }
        if (!usable || !FigureBatches.ready()) {
            usable = false;
            return false;
        }
        int vs = FigureBatches.shader(GL20.GL_VERTEX_SHADER, VERTEX);
        int fs = FigureBatches.shader(GL20.GL_FRAGMENT_SHADER, FRAGMENT);
        if (vs < 0 || fs < 0) {
            usable = false;
            return false;
        }
        int p = GL20.glCreateProgram();
        GL20.glAttachShader(p, vs);
        GL20.glAttachShader(p, fs);
        String[] names = {"Pos", "Uv", "Normal", "Pivot", "Bone", "PosScale", "Walk", "LightFlags", "Phase"};
        for (int a = 0; a < names.length; a++) {
            GL20.glBindAttribLocation(p, a, names[a]);
        }
        GL20.glLinkProgram(p);
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);
        if (GL20.glGetProgrami(p, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            RafasConquest.LOGGER.error("[figures] recruit shader didn't link: {}", GL20.glGetProgramInfoLog(p));
            GL20.glDeleteProgram(p);
            usable = false;
            return false;
        }
        mvp = GL20.glGetUniformLocation(p, "Mvp");
        modulator = GL20.glGetUniformLocation(p, "ColorModulator");
        time = GL20.glGetUniformLocation(p, "Time");
        tint = GL20.glGetUniformLocation(p, "Tint");
        untextured = GL20.glGetUniformLocation(p, "Untextured");
        framed = GL20.glGetUniformLocation(p, "Framed");
        debugUniform = GL20.glGetUniformLocation(p, "Debug");
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        GlStateManager._glUseProgram(p);
        GL20.glUniform1i(GL20.glGetUniformLocation(p, "Sampler0"), TEXTURE_UNIT);
        GL20.glUniform1i(GL20.glGetUniformLocation(p, "Frames"), FRAME_UNIT);
        GlStateManager._glUseProgram(oldProgram);
        instanceBuffer = GlStateManager._glGenBuffers();
        program = p;
        RafasConquest.LOGGER.info("[figures] drawing foot soldiers as recruits, instanced");
        return true;
    }

    /*
     * The model's frame is the game's: blocks, y down, feet at 1.5, the soldier's right toward -x and its front toward
     * -z. Bones turn as ModelPart does (z, then y, then x, about the pivot). The poses are HumanoidModel.setupAnim's:
     * the walk swing, a held item's lowered arm, the attack stroke, the bow drawn, and AnimationUtils.bobModelPart. A
     * rider sits as the game seats a player on a horse (feet 1.44375 - 0.6 up, legs out, no walk). A horse (Framed: its
     * corner count) takes each corner from its captured frames; the gaits split at limb speed 0.5 and 0.9.
     */
    private static final String VERTEX = """
            #version 150
            in vec3 Pos;
            in vec2 Uv;
            in vec3 Normal;
            in vec3 Pivot;
            in float Bone;
            in vec4 PosScale;
            in vec4 Walk;
            in uvec2 LightFlags;
            in float Phase;
            uniform mat4 Mvp;
            uniform vec4 ColorModulator;
            uniform float Time;
            uniform samplerBuffer Frames;
            uniform int Framed;
            uniform int Debug;
            out vec2 uv;
            out vec4 vertexColor;
            flat out uint flags;

            const float PI = 3.14159265;
            const float STRIDE = 7.8539816;
            const int STEPS = 12;
            const vec3 LIGHT0 = normalize(vec3(0.2, 1.0, -0.7));
            const vec3 LIGHT1 = normalize(vec3(-0.2, 1.0, 0.7));

            mat3 rotX(float a) { float c = cos(a), s = sin(a); return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c); }
            mat3 rotY(float a) { float c = cos(a), s = sin(a); return mat3(c, 0.0, -s, 0.0, 1.0, 0.0, s, 0.0, c); }
            mat3 rotZ(float a) { float c = cos(a), s = sin(a); return mat3(c, s, 0.0, -s, c, 0.0, 0.0, 0.0, 1.0); }

            void main() {
                int bone = int(Bone + 0.5);
                uint f = LightFlags.y;
                bool riding = (f & 32u) != 0u;
                float ls = Walk.z;
                float la = riding && bone < 6 ? 0.0 : Walk.w;
                vec3 r = vec3(0.0);
                if (bone == 2) r.x = cos(ls * 0.6662 + PI) * la;
                else if (bone == 3) r.x = cos(ls * 0.6662) * la;
                else if (bone == 4) r.x = cos(ls * 0.6662) * 1.4 * la;
                else if (bone == 5) r.x = cos(ls * 0.6662 + PI) * 1.4 * la;
                if (riding) {
                    if (bone == 2 || bone == 3) r.x -= PI / 5.0;
                    if (bone == 4) r = vec3(-1.4137167, PI / 10.0, 0.07853982);
                    if (bone == 5) r = vec3(-1.4137167, -PI / 10.0, -0.07853982);
                }
                if (bone == 2 && (f & 8u) != 0u) r.x = r.x * 0.5 - PI / 10.0;
                if (bone == 3 && (f & 16u) != 0u) r.x = r.x * 0.5 - PI / 10.0;
                if ((f & 4u) != 0u) {
                    if (bone == 2) r = vec3(-PI / 2.0, -0.1, 0.0);
                    if (bone == 3) r = vec3(-PI / 2.0, 0.5, 0.0);
                }
                if ((f & 2u) != 0u && bone == 2) {
                    float a = fract(Time / 1.4 + Phase);
                    if (a < 0.45) {
                        float t = a / 0.45;
                        float f1 = 1.0 - pow(1.0 - t, 4.0);
                        r.x -= sin(f1 * PI) * 1.2 + sin(t * PI) * 0.525;
                        r.z -= sin(t * PI) * 0.4;
                    }
                }
                float age = Time * 20.0 + Phase * 1000.0;
                if (bone == 2) { r.z += cos(age * 0.09) * 0.05 + 0.05; r.x += sin(age * 0.067) * 0.05; }
                if (bone == 3) { r.z -= cos(age * 0.09) * 0.05 + 0.05; r.x -= sin(age * 0.067) * 0.05; }
                mat3 m = rotZ(r.z) * rotY(r.y) * rotX(r.x);
                vec3 pos = Pos;
                vec3 nrm = Normal;
                if (Framed > 0) { // a horse (or a tower's wheels): its frame for the gait and the point in its stride, blended to the next
                    int f0 = 0, f1 = 0;
                    float w = 0.0;
                    if ((f & 64u) != 0u) { // played by progress: a tower's bridge coming down, a trebuchet's shot
                        float t = clamp(Walk.z, 0.0, 1.0) * float(STEPS * 3 - 1);
                        f0 = 1 + int(t);
                        f1 = min(f0 + 1, STEPS * 3);
                        w = fract(t);
                    } else if (Walk.w > 0.05) {
                        int gait = Walk.w < 0.5 ? 0 : Walk.w < 0.9 ? 1 : 2;
                        float t = mod(Walk.z, STRIDE) / STRIDE * float(STEPS);
                        int k = int(t) % STEPS;
                        w = fract(t);
                        f0 = 1 + gait * STEPS + k;
                        f1 = 1 + gait * STEPS + (k + 1) % STEPS;
                    }
                    int a = (f0 * Framed + gl_VertexID) * 2;
                    int b = (f1 * Framed + gl_VertexID) * 2;
                    vec4 a0 = texelFetch(Frames, a), a1 = texelFetch(Frames, a + 1);
                    vec4 b0 = texelFetch(Frames, b), b1 = texelFetch(Frames, b + 1);
                    pos = mix(a0.xyz, b0.xyz, w);
                    nrm = mix(vec3(a0.w, a1.xy), vec3(b0.w, b1.xy), w);
                }
                vec3 p = Pivot + m * (pos - Pivot);
                if (riding && bone < 6) p.y -= 0.84375;
                vec3 n = m * nrm;
                vec2 forward = Walk.xy;
                vec2 right = vec2(-forward.y, forward.x);
                vec2 across = right * -p.x + forward * -p.z;
                gl_Position = Mvp * vec4(PosScale.xyz + vec3(across.x, 1.5 - p.y, across.y) * PosScale.w, 1.0);
                vec2 nAcross = right * -n.x + forward * -n.z;
                vec3 normal = normalize(vec3(nAcross.x, -n.y, nAcross.y) + vec3(0.0, 1e-5, 0.0));
                float shade = min(1.0, (max(0.0, dot(LIGHT0, normal)) + max(0.0, dot(LIGHT1, normal))) * 0.6 + 0.4);
                uint light = LightFlags.x; // the lightmap's colour, ABGR
                vec4 lightmap = Debug == 2 ? vec4(1.0) : vec4(float(light & 255u), float((light >> 8) & 255u), float((light >> 16) & 255u), 255.0) / 255.0;
                vertexColor = vec4(vec3(shade), 1.0) * lightmap * (Debug == 3 ? vec4(1.0) : ColorModulator);
                uv = Uv;
                flags = f;
            }
            """;

    private static final String FRAGMENT = """
            #version 150
            uniform sampler2D Sampler0;
            uniform vec4 Tint;
            uniform int Untextured;
            uniform int Debug;
            in vec2 uv;
            in vec4 vertexColor;
            flat in uint flags;
            out vec4 fragColor;

            void main() {
                if ((flags & 1u) != 0u) {
                    fragColor = vec4(1.0);
                    return;
                }
                vec4 c = Untextured != 0 ? vec4(1.0) : texture(Sampler0, uv);
                if (c.a < 0.1) discard;
                if (Debug == 1) {
                    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
                    return;
                }
                fragColor = Debug == 4 ? vertexColor : vec4(c.rgb, 1.0) * Tint * vertexColor;
            }
            """;
}
