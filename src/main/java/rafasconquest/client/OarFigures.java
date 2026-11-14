package rafasconquest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
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
import rafasconquest.bridge.SableLink;
import rafasconquest.content.Oar;
import rafasconquest.content.OarBlock;
import rafasconquest.style.ShipHulls;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Every loaded {@link Oar}, GPU-instanced like the recruit figures: the game's own oak fence and open trapdoor quads,
 * in the block atlas, captured once into one model on the graphics card; each frame one record per oar (where its
 * handle is, how it is turned, its light) and one draw for all. An oar on a Sable ship takes the ship's render pose.
 * Far ships' hulls ({@link #hull}) are drawn the same way, one model per hull from its blocks' own game models.
 * <p>
 * Render thread only. Leaves the game's GL state as it found it.
 */
final class OarFigures {
    /** A corner as {@link RecruitFigures.Capture} takes it: where, texture spot, normal, then a bone (unused here). */
    private static final int VERTEX_BYTES = 12 * 4;
    /** An oar's record: handle x y z from the camera, its turn (three columns), light colour. */
    private static final int INSTANCE_BYTES = 13 * 4;
    private static final BlockState FENCE = Blocks.OAK_FENCE.defaultBlockState();
    /** Open, facing north: a 3/16-thick panel standing at the block's south side (z 13/16 to 1). */
    private static final BlockState BLADE = Blocks.OAK_TRAPDOOR.defaultBlockState()
            .setValue(TrapDoorBlock.OPEN, true).setValue(TrapDoorBlock.FACING, Direction.NORTH);
    /** Shifts the blade panel from z 13/16 back to 10/16, flat against the fence post's face (z 6/16 to 10/16). */
    private static final float BLADE_SHIFT = -3 / 16F;

    /** A model on the graphics card, and this frame's records for it. */
    private static final class Model {
        final int vao, vertices;
        ByteBuffer instances = MemoryUtil.memAlloc(INSTANCE_BYTES * 64);
        int count;

        Model(int vao, int vertices) {
            this.vao = vao;
            this.vertices = vertices;
        }
    }

    private static Model oarModel;
    /** Far ships' hulls, by {@link DistantShips#hullKey}. shortcut: never freed, as FigureBatches' models; free unseen ones if the card's memory shows. */
    private static final java.util.Map<Object, Model> HULLS = new java.util.HashMap<>();
    private static int program = -1, instanceBuffer, mvp, modulator, atlas;
    private static boolean usable = true;
    private static boolean sableBroken;

    private OarFigures() {
    }

    static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !ready()) {
            return;
        }
        Vec3 eye = camera.getPosition();
        Matrix3f turn = new Matrix3f();
        double phase = phase(mc, partialTick);
        // count isn't reset: the far ships' oars (far()) were put in just before, and go in this one draw.
        for (Oar oar : Oar.CLIENT_LOADED) {
            BlockState state = oar.getBlockState();
            if (oar.isRemoved() || !(state.getBlock() instanceof OarBlock block)) {
                continue;
            }
            Direction side = block.side(state);
            BlockPos pos = oar.getBlockPos();
            // The handle: where the shaft leaves the block's side, half way up.
            Vector3d at = new Vector3d(pos.getX() + 0.5 + side.getStepX() * 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 + side.getStepZ() * 0.5);
            boolean right = side == state.getValue(OarBlock.FACING).getClockWise();
            Quaterniond q = oarTurn(side, right, stroke(mc, oar, state, right), phase);
            Quaterniond ship = shipTurn(mc, pos, partialTick, at);
            if (ship != null) {
                q.premul(ship);
            }
            double dx = at.x - eye.x;
            double dy = at.y - eye.y;
            double dz = at.z - eye.z;
            // Out to where Sable stops drawing its ship, as it does; past that DistantShips' far copy has them (far()).
            // shortcut: no frustum test, the batch is cheap; judged by the oar, not its ship, so near the hand-over a
            // ship's oars can show twice or not at all for a moment; judge by the ship if it shows.
            if (dx * dx + dz * dz > DistantShips.REAL_WITHIN * DistantShips.REAL_WITHIN) {
                continue;
            }
            turn.set(new Quaternionf(q));
            int light = FigureBatches.lightColour(LevelRenderer.getLightColor(oar.getLevel(), pos.relative(side)));
            put(oarModel, (float) dx, (float) dy, (float) dz, turn, light);
        }
        draw(pose.last().pose());
    }

    /**
     * A far ship's hull ({@link DistantArmyRenderer#renderShips}), its own blocks' models with their textures, placed as
     * its oars are ({@link #far}): its middle {@code x y z} from the camera, drawn {@code scale} size, turned to its
     * heading, its deck sheared along the swell's slope and lifted on it. Drawn with the oars in {@link #render}, next.
     */
    static void hull(Object key, java.util.List<ShipHulls.Part> parts, DistantShips.Far ship, float x, float y, float z, float scale, int light) {
        if (Minecraft.getInstance().level == null || !ready()) {
            return;
        }
        Model m = HULLS.computeIfAbsent(key, k -> upload(hullModel(parts)));
        // y' = y + fore * z + port * x: the deck rising toward the bow and toward port, a few degrees at most.
        Matrix3f shear = new Matrix3f().m01((float) ship.port()).m21((float) ship.fore());
        Matrix3f turn = new Matrix3f().rotationY((float) ship.yaw()).mul(shear).scale(scale);
        put(m, x, y + (float) ship.lift() * scale, z, turn, light);
    }

    /**
     * A hull's blocks as the game models them, in its own frame (z to the bow, each block centred on its x and z), with
     * the faces against a solid neighbour left out. A block drawn by its block entity shows as nothing, but a sail as
     * wool of its colour.
     */
    private static float[] hullModel(java.util.List<ShipHulls.Part> parts) {
        java.util.Map<Long, BlockState> cells = new java.util.HashMap<>();
        for (ShipHulls.Part p : parts) {
            BlockState shown = shown(p.state());
            if (shown != null) {
                cells.put(cell(p.x(), p.y(), p.z()), shown);
            }
        }
        RecruitFigures.Capture c = new RecruitFigures.Capture();
        PoseStack pose = new PoseStack();
        var blocks = Minecraft.getInstance().getBlockRenderer();
        net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create(42);
        for (ShipHulls.Part p : parts) {
            BlockState state = cells.get(cell(p.x(), p.y(), p.z()));
            if (state == null) {
                continue;
            }
            var model = blocks.getBlockModel(state);
            pose.pushPose();
            pose.translate(p.x() - 0.5, p.y(), p.z() - 0.5);
            // shortcut: tinted quads (leaves, grass) come out untinted, Capture keeps no colour; tint them if a ship shows it.
            for (Direction d : Direction.values()) {
                BlockState next = cells.get(cell(p.x() + d.getStepX(), p.y() + d.getStepY(), p.z() + d.getStepZ()));
                if (next == null || !next.isSolidRender(net.minecraft.world.level.EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
                    for (var quad : model.getQuads(state, d, random)) {
                        c.putBulkData(pose.last(), quad, 1, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                    }
                }
            }
            for (var quad : model.getQuads(state, null, random)) {
                c.putBulkData(pose.last(), quad, 1, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }
            pose.popPose();
        }
        return c.triangles();
    }

    /** What a hull block is drawn as: itself if the game models it, a sail as wool of its colour, else nothing. */
    private static BlockState shown(BlockState state) {
        if (state.getBlock() instanceof rafasconquest.content.SailBlock) {
            return net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(net.minecraft.resources.ResourceLocation.withDefaultNamespace(
                    state.getValue(rafasconquest.content.SailBlock.COLOR).getName() + "_wool")).defaultBlockState();
        }
        return state.getRenderShape() == net.minecraft.world.level.block.RenderShape.MODEL ? state : null;
    }

    private static long cell(int x, int y, int z) {
        return ((long) (x + 1024) << 32) | ((long) (y + 1024) << 16) | (z + 1024);
    }

    /** Far ships' hull oar blocks, by their parts list (the same list each frame); cleared when it grows, as lane hulls are rebuilt. */
    private static final java.util.Map<java.util.List<ShipHulls.Part>, java.util.List<ShipHulls.Part>> FAR_OARS = new java.util.IdentityHashMap<>();

    /**
     * A far ship's oars ({@link DistantArmyRenderer#renderShips}): one for each oar block of its hull {@code parts}, placed
     * and turned as its far model is (its middle {@code x y z} from the camera, drawn {@code scale} size), rowing as
     * {@link DistantShips.Far#rowLeft} and {@code rowRight} say. Drawn with the near oars in {@link #render}, next.
     */
    static void far(DistantShips.Far ship, java.util.List<ShipHulls.Part> parts, float x, float y, float z, float scale, int light,
                    float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !ready()) {
            return;
        }
        if (FAR_OARS.size() > 64) {
            FAR_OARS.clear();
        }
        var oars = FAR_OARS.computeIfAbsent(parts, k -> k.stream().filter(p -> p.state().getBlock() instanceof OarBlock).toList());
        double phase = phase(mc, partialTick);
        Quaterniond yaw = new Quaterniond().rotationY(ship.yaw()); // the far model's turn (FigureBatches' Facing)
        Matrix3f turn = new Matrix3f();
        Vector3d at = new Vector3d();
        for (ShipHulls.Part p : oars) {
            Direction side = ((OarBlock) p.state().getBlock()).side(p.state());
            boolean right = side == p.state().getValue(OarBlock.FACING).getClockWise();
            double hx = p.x() + side.getStepX() * 0.5;
            double hz = p.z() + side.getStepZ() * 0.5;
            // Lifted as the model's deck is sheared on the swell; not tilted, only a few degrees.
            yaw.transform(at.set(hx, p.y() + 0.5 + hz * ship.fore() + hx * ship.port() + ship.lift(), hz));
            Quaterniond q = new Quaterniond(yaw).mul(oarTurn(side, right, right ? ship.rowRight() : ship.rowLeft(), phase));
            turn.set(new Quaternionf(q)).scale(scale);
            put(oarModel, x + (float) at.x * scale, y + (float) at.y * scale, z + (float) at.z * scale, turn, light);
        }
    }

    private static double phase(Minecraft mc, float partialTick) {
        return (mc.level.getGameTime() + partialTick) * 2 * Math.PI / STROKE_TICKS;
    }

    /**
     * An oar's turn on its hull: the model's shaft runs along +z; swept and dipped by its stroke {@code s}, then turned
     * out of its side as the old renderer did. In the turned-out frame, +x is side's counter-clockwise: the bow for a right oar.
     */
    private static Quaterniond oarTurn(Direction side, boolean right, float s, double phase) {
        return new Quaterniond().rotationY(Math.toRadians(-side.toYRot()))
                .rotateY((right ? 1 : -1) * Math.abs(s) * SWEEP * Math.cos(phase))
                .rotateX(s * DIP * Math.sin(phase)); // blade down while it sweeps sternward (ahead) or bowward (backwards)
    }

    /** Ticks for one stroke; its fore-and-aft sweep and its dip either side of straight out, radians. */
    private static final double STROKE_TICKS = 40, SWEEP = Math.toRadians(30), DIP = Math.toRadians(20);
    /** A ship's motion, smoothed over ticks: x and z velocity, turn rate (+ to its left), the tick last read. */
    private static final java.util.Map<ClientSubLevel, double[]> MOTION = new java.util.WeakHashMap<>();

    /**
     * How an oar rows, eased a little each tick toward {@link Rowing#target}: its ship's motion along the oar's bow and turn
     * rate, from Sable's last two poses. 0 (at rest) off a ship.
     */
    private static float stroke(Minecraft mc, Oar oar, BlockState state, boolean right) {
        long tick = mc.level.getGameTime();
        if (oar.strokeTick == tick) {
            return oar.stroke;
        }
        oar.strokeTick = tick;
        int want = 0;
        if (!sableBroken && SableLink.available()) {
            try {
                if (Sable.HELPER.getContaining(mc.level, oar.getBlockPos()) instanceof ClientSubLevel ship && !ship.isRemoved()) {
                    double[] m = MOTION.computeIfAbsent(ship, k -> new double[]{0, 0, 0, tick});
                    if (m[3] != tick) {
                        m[3] = tick;
                        org.joml.Vector3dc now = ship.logicalPose().position(), was = ship.lastPose().position();
                        Vector3d b1 = new Quaterniond(ship.logicalPose().orientation()).transform(new Vector3d(0, 0, 1));
                        Vector3d b0 = new Quaterniond(ship.lastPose().orientation()).transform(new Vector3d(0, 0, 1));
                        double yaw = Math.atan2(b0.z * b1.x - b0.x * b1.z, b0.x * b1.x + b0.z * b1.z);
                        m[0] += (now.x() - was.x() - m[0]) * 0.3; // smoothed: client poses come in uneven steps
                        m[1] += (now.z() - was.z() - m[1]) * 0.3;
                        m[2] += (yaw - m[2]) * 0.3;
                    }
                    Direction facing = state.getValue(OarBlock.FACING);
                    Vector3d bow = new Quaterniond(ship.logicalPose().orientation())
                            .transform(new Vector3d(facing.getStepX(), 0, facing.getStepZ()));
                    double flat = Math.max(1e-6, Math.hypot(bow.x, bow.z));
                    want = Rowing.target((m[0] * bow.x + m[1] * bow.z) / flat, m[2], right);
                }
            } catch (LinkageError e) {
                sableBroken = true;
                RafasConquest.LOGGER.warn("Sable's client API doesn't match; oars on ships are drawn where their blocks are", e);
            }
        }
        return oar.stroke = Rowing.ease(oar.stroke, want);
    }

    /** Kept apart from the oar drawing's game statics, so tests can call it without starting the game. */
    static final class Rowing {
        /** Slower than these (blocks a tick along the bow, radians a tick about up) a ship is still / not turning. */
        static final double MOVING = 0.02, TURNING = Math.toRadians(3) / 20;

        /**
         * 1 to row ahead, -1 backwards, 0 to rest: turning, the oars on the outside of the turn row ahead and those on
         * the inside backwards; otherwise all row the way the ship goes along its bow.
         */
        static int target(double forward, double turnLeft, boolean right) {
            if (Math.abs(turnLeft) > TURNING) {
                return (turnLeft > 0) == right ? 1 : -1;
            }
            return forward > MOVING ? 1 : forward < -MOVING ? -1 : 0;
        }

        /** A stroke {@code s} eased one tick toward {@code want}: about a second from rest to rowing. */
        static float ease(float s, int want) {
            return s + (want - s) * 0.15F;
        }
    }

    /** The ship's render turn for an oar at {@code pos} on one (moving {@code at} from its plot into the world), or null. */
    static Quaterniond shipTurn(Minecraft mc, BlockPos pos, float partialTick, Vector3d at) {
        if (sableBroken || !SableLink.available()) {
            return null;
        }
        try {
            SubLevel sub = Sable.HELPER.getContaining(mc.level, pos);
            if (!(sub instanceof ClientSubLevel ship) || ship.isRemoved()) {
                return null;
            }
            var pose = ship.renderPose(partialTick);
            pose.transformPosition(at);
            return new Quaterniond(pose.orientation());
        } catch (LinkageError e) {
            sableBroken = true;
            RafasConquest.LOGGER.warn("Sable's client API doesn't match; oars on ships are drawn where their blocks are", e);
            return null;
        }
    }

    private static void put(Model model, float x, float y, float z, Matrix3f turn, int light) {
        if ((model.count + 1) * INSTANCE_BYTES > model.instances.capacity()) {
            model.instances = MemoryUtil.memRealloc(model.instances, model.instances.capacity() * 2);
            model.instances.clear();
        }
        ByteBuffer instances = model.instances;
        int at = model.count * INSTANCE_BYTES;
        instances.putFloat(at, x).putFloat(at + 4, y).putFloat(at + 8, z);
        float[] m = turn.get(new float[9]); // column by column
        for (int i = 0; i < 9; i++) {
            instances.putFloat(at + 12 + i * 4, m[i]);
        }
        instances.putInt(at + 48, light);
        model.count++;
    }

    private static void draw(Matrix4f pose) {
        if (program < 0 || oarModel.count == 0 && HULLS.values().stream().allMatch(h -> h.count == 0)) {
            return;
        }
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int oldActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int oldTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int oldSampler = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
        GL33.glBindSampler(0, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).getId());
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
        GL20.glUniform1i(atlas, 0);
        drawEach(oarModel);
        HULLS.values().forEach(OarFigures::drawEach);
        GlStateManager._glUseProgram(oldProgram);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, oldTexture);
        GL33.glBindSampler(0, oldSampler);
        GL13.glActiveTexture(oldActive);
        RenderSystem.enableCull();
    }

    private static void drawEach(Model model) {
        if (model.count == 0) {
            return;
        }
        GlStateManager._glBindVertexArray(model.vao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, model.instances.limit(model.count * INSTANCE_BYTES), GL15.GL_STREAM_DRAW);
        model.instances.clear();
        GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, model.vertices, model.count);
        model.count = 0;
    }

    /**
     * The oar's model, handle at the origin and shaft along +z: four oak fence posts, the outer two faced with upright open
     * oak trapdoors, captured from the game's own block models (the old block entity renderer's layout, unturned).
     */
    private static float[] model() {
        RecruitFigures.Capture c = new RecruitFigures.Capture();
        PoseStack pose = new PoseStack();
        pose.mulPose(Axis.XP.rotationDegrees(90)); // the shaft, built along +y, laid along +z
        pose.translate(-0.5, 0, -0.5); // fence posts centred on the shaft's line
        var blocks = Minecraft.getInstance().getBlockRenderer();
        for (int i = 0; i < Oar.LENGTH; i++) {
            blocks.renderSingleBlock(FENCE, pose, type -> c, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            if (i >= Oar.LENGTH - 2) {
                pose.pushPose();
                // A quarter turn about the shaft: the blade stands upright in the water, not flat on it.
                pose.translate(0.5, 0, 0.5);
                pose.mulPose(Axis.YP.rotationDegrees(90));
                pose.translate(-0.5, 0, -0.5);
                pose.translate(0, 0, BLADE_SHIFT);
                blocks.renderSingleBlock(BLADE, pose, type -> c, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                pose.popPose();
            }
            pose.translate(0, 1, 0);
        }
        return c.triangles();
    }

    // ---- the graphics card's side ----

    private static boolean ready() {
        if (program >= 0) {
            return true;
        }
        if (!usable || !FigureBatches.ready()) { // it checks the card can instance
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
        String[] names = {"Local", "Uv", "Normal", "Origin", "TurnX", "TurnY", "TurnZ", "Light"};
        for (int a = 0; a < names.length; a++) {
            GL20.glBindAttribLocation(p, a, names[a]);
        }
        GL20.glLinkProgram(p);
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);
        if (GL20.glGetProgrami(p, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            RafasConquest.LOGGER.error("[oars] shader didn't link: {}", GL20.glGetProgramInfoLog(p));
            GL20.glDeleteProgram(p);
            usable = false;
            return false;
        }
        mvp = GL20.glGetUniformLocation(p, "Mvp");
        modulator = GL20.glGetUniformLocation(p, "ColorModulator");
        atlas = GL20.glGetUniformLocation(p, "Atlas");
        instanceBuffer = GlStateManager._glGenBuffers();
        oarModel = upload(model());
        program = p;
        RafasConquest.LOGGER.info("[oars] drawing oars instanced ({} corners)", oarModel.vertices);
        return true;
    }

    /** A model's corners ({@link RecruitFigures.Capture#triangles}) onto the graphics card, wired to the records' buffer. */
    private static Model upload(float[] corners) {
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int vao = GlStateManager._glGenVertexArrays();
        GlStateManager._glBindVertexArray(vao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, GlStateManager._glGenBuffers());
        ByteBuffer data = MemoryUtil.memAlloc(Math.max(4, corners.length * 4));
        data.asFloatBuffer().put(corners);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(data);
        FigureBatches.floats(0, 3, VERTEX_BYTES, 0, false);
        FigureBatches.floats(1, 2, VERTEX_BYTES, 12, false);
        FigureBatches.floats(2, 3, VERTEX_BYTES, 20, false);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
        FigureBatches.floats(3, 3, INSTANCE_BYTES, 0, true);
        FigureBatches.floats(4, 3, INSTANCE_BYTES, 12, true);
        FigureBatches.floats(5, 3, INSTANCE_BYTES, 24, true);
        FigureBatches.floats(6, 3, INSTANCE_BYTES, 36, true);
        FigureBatches.ints(7, 1, INSTANCE_BYTES, 48, true);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        return new Model(vao, corners.length / 12);
    }

    private static final String VERTEX = """
            #version 150
            in vec3 Local;
            in vec2 Uv;
            in vec3 Normal;
            in vec3 Origin;
            in vec3 TurnX;
            in vec3 TurnY;
            in vec3 TurnZ;
            in uint Light;
            uniform mat4 Mvp;
            uniform vec4 ColorModulator;
            out vec2 uv;
            out vec4 tint;
            void main() {
                mat3 turn = mat3(TurnX, TurnY, TurnZ);
                gl_Position = Mvp * vec4(Origin + turn * Local, 1.0);
                vec3 n = normalize(turn * Normal); // a far ship's oar's turn is scaled
                // The game's block face shading: up 1, down 0.5, north/south 0.8, east/west 0.6.
                float shade = n.x * n.x * 0.6 + n.z * n.z * 0.8 + n.y * n.y * (n.y > 0.0 ? 1.0 : 0.5);
                vec4 light = vec4(float(Light & 255u), float((Light >> 8) & 255u), float((Light >> 16) & 255u), 255.0) / 255.0;
                tint = vec4(vec3(shade), 1.0) * light * ColorModulator;
                uv = Uv;
            }
            """;
    private static final String FRAGMENT = """
            #version 150
            uniform sampler2D Atlas;
            in vec2 uv;
            in vec4 tint;
            out vec4 fragColor;
            void main() {
                vec4 c = texture(Atlas, uv);
                if (c.a < 0.1) {
                    discard; // the trapdoor's holes
                }
                fragColor = c * tint;
            }
            """;
}
