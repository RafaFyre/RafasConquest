package rafasconquest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
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
import rafasconquest.client.cloth.Wind;
import rafasconquest.content.Sail;
import rafasconquest.content.SailBlock;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Every loaded sail sheet, GPU-instanced like the oars: the top corner block of each sheet sends one record (where the
 * corner is, how the sheet is turned, its size, how full and how hard it flaps, its light) and its squares' dyes; the
 * graphics card makes the grid itself from the corner's number and works out the belly and ripples, so no corner is
 * made on this side. Sheets with as many corners are drawn together, one draw each.
 * <p>
 * How full the sail is comes from the wind on the ship's bow: full from astern, slack and flogging head to wind.
 * Render thread only. Leaves the game's GL state as it found it.
 */
final class SailFigures {
    private static final ResourceLocation CANVAS = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "textures/block/sail.png");
    /** A sheet's record: corner x y z from the camera, its turn (three columns), w h k dye start, fill flap, light. */
    private static final int INSTANCE_BYTES = 19 * 4;
    /** The furthest a sheet reaches from its corner block along a row or column. */
    private static final int MOST = 32;
    private static final int COLOUR_UNIT = 1;

    private static ByteBuffer staged = MemoryUtil.memAlloc(INSTANCE_BYTES * 64);
    private static ByteBuffer instances = MemoryUtil.memAlloc(INSTANCE_BYTES * 64);
    private static ByteBuffer colours = MemoryUtil.memAlloc(4 * 1024);
    /** Per staged sheet: its corner count, shifted up, then its place in {@link #staged}; sorted, they make the runs. */
    private static long[] order = new long[64];
    private static int count, colourCount;
    private static int program = -1, vao, instanceBuffer, colourBuffer, colourTexture, mvp, modulator, clock, canvas, dyes;
    private static boolean usable = true;
    private static final double[] WIND = new double[3];

    private SailFigures() {
    }

    static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (Sail.CLIENT_LOADED.isEmpty() || mc.level == null || !ready()) {
            return;
        }
        Vec3 eye = camera.getPosition();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Matrix3f turn = new Matrix3f();
        count = 0;
        colourCount = 0;
        long time = mc.level.getGameTime(); // once a frame: asked of each sail block it was a thirtieth of the frame
        for (Sail sail : Sail.CLIENT_LOADED) {
            BlockState state = sail.getBlockState();
            Level level = sail.getLevel();
            if (sail.isRemoved() || level == null || !(state.getBlock() instanceof SailBlock)) {
                continue;
            }
            // Its facing as last measured: reading it off the block state for every sail block every frame was a
            // twentieth of a sea battle's frame.
            Direction facing = sail.facing;
            if (facing == null || time - sail.measured >= 20 || time < sail.measured) {
                facing = sail.facing = state.getValue(SailBlock.FACING);
                measure(sail, level, facing);
            }
            if (sail.inner) {
                continue;
            }
            Direction across = facing.getClockWise();
            int ax = across.getStepX(), az = across.getStepZ(), fx = facing.getStepX(), fz = facing.getStepZ();
            BlockPos pos = sail.getBlockPos();
            // The sheet's top corner, its turn (across, up, forward), and its bow; a ship's turn on top.
            Vector3d at = new Vector3d(pos.getX() + 0.5 - ax * 0.5, pos.getY() + 1, pos.getZ() + 0.5 - az * 0.5);
            turn.set(ax, 0, az, 0, 1, 0, fx, 0, fz);
            Quaterniond ship = OarFigures.shipTurn(mc, pos, partialTick, at);
            if (ship != null) {
                turn.rotateLocal(new Quaternionf(ship));
            }
            double dx = at.x - eye.x, dy = at.y - eye.y, dz = at.z - eye.z;
            if (dx * dx + dz * dz > renderBlocks * renderBlocks) {
                continue;
            }
            Wind.at(level, at.x, at.y, at.z, 1, 1, WIND);
            double bowX = turn.m20, bowZ = turn.m22;
            double speed = Math.hypot(WIND[0], WIND[2]);
            double flat = Math.max(1e-6, Math.hypot(bowX, bowZ));
            double along = speed < 1e-6 ? 1 : (WIND[0] * bowX + WIND[2] * bowZ) / (speed * flat);
            double windy = Math.min(1, speed / 20);
            double caught = smoothstep(-0.6, 0.2, along);
            float fill = (float) ((0.5 + 0.5 * windy) * (0.2 + 0.8 * caught));
            float flap = (float) (0.15 + 0.15 * windy + 0.7 * (1 - caught) * (0.4 + 0.6 * windy));
            // Squares per block side, fewer as it gets further.
            double far = Math.sqrt(dx * dx + dy * dy + dz * dz);
            int k = far < 24 ? 4 : far < 64 ? 2 : 1;
            int light = FigureBatches.lightColour(LevelRenderer.getLightColor(level, pos));
            put((float) dx, (float) dy, (float) dz, turn, sail, k, fill, flap, light);
        }
        draw(pose.last().pose(), (mc.level.getGameTime() % 72_000L + partialTick) / 20F);
    }

    private static void put(float x, float y, float z, Matrix3f turn, Sail sail, int k, float fill, float flap, int light) {
        int w = sail.right + 1, h = sail.down + 1;
        if ((count + 1) * INSTANCE_BYTES > staged.capacity()) {
            staged = MemoryUtil.memRealloc(staged, staged.capacity() * 2);
            instances = MemoryUtil.memRealloc(instances, instances.capacity() * 2);
            order = Arrays.copyOf(order, order.length * 2);
        }
        if ((colourCount + w * h) * 4 > colours.capacity()) {
            colours = MemoryUtil.memRealloc(colours, Math.max(colours.capacity() * 2, (colourCount + w * h) * 4));
        }
        for (int i = 0; i < w * h; i++) {
            colours.putInt((colourCount + i) * 4, sail.colours[i]);
        }
        int at = count * INSTANCE_BYTES;
        staged.putFloat(at, x).putFloat(at + 4, y).putFloat(at + 8, z);
        float[] m = turn.get(new float[9]); // column by column
        for (int i = 0; i < 9; i++) {
            staged.putFloat(at + 12 + i * 4, m[i]);
        }
        staged.putInt(at + 48, w).putInt(at + 52, h).putInt(at + 56, k).putInt(at + 60, colourCount);
        staged.putFloat(at + 64, fill).putFloat(at + 68, flap).putInt(at + 72, light);
        order[count] = (long) corners(w, h, k) << 32 | count;
        colourCount += w * h;
        count++;
    }

    /** A sheet's corners: two faces of (w k) by (h k) squares, two triangles each. */
    static int corners(int w, int h, int k) {
        return w * h * k * k * 12;
    }

    private static void draw(Matrix4f pose, float seconds) {
        if (count == 0) {
            return;
        }
        // Sheets with as many corners next to each other, so each run is one draw.
        Arrays.sort(order, 0, count);
        long from = MemoryUtil.memAddress(staged), to = MemoryUtil.memAddress(instances);
        for (int i = 0; i < count; i++) {
            MemoryUtil.memCopy(from + (int) order[i] * (long) INSTANCE_BYTES, to + (long) i * INSTANCE_BYTES, INSTANCE_BYTES);
        }
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int oldActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int oldTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int oldSampler = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
        GL33.glBindSampler(0, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getInstance().getTextureManager().getTexture(CANVAS).getId());
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, colourBuffer);
        GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, colours.limit(colourCount * 4), GL15.GL_STREAM_DRAW);
        colours.clear();
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + COLOUR_UNIT);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, colourTexture);
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
        GL20.glUniform1f(clock, seconds);
        GL20.glUniform1i(canvas, 0);
        GL20.glUniform1i(dyes, COLOUR_UNIT);
        GlStateManager._glBindVertexArray(vao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instances.limit(count * INSTANCE_BYTES), GL15.GL_STREAM_DRAW);
        instances.clear();
        for (int start = 0; start < count; ) {
            int corners = (int) (order[start] >>> 32);
            int end = start + 1;
            while (end < count && (int) (order[end] >>> 32) == corners) {
                end++;
            }
            attributes((long) start * INSTANCE_BYTES);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, corners, end - start);
            start = end;
        }
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GlStateManager._glUseProgram(oldProgram);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, oldTexture);
        GL33.glBindSampler(0, oldSampler);
        GL13.glActiveTexture(oldActive);
        RenderSystem.enableCull();
    }

    /** Points the per-sheet inputs at the run starting {@code offset} bytes into the bound instance buffer. */
    private static void attributes(long offset) {
        FigureBatches.floats(0, 3, INSTANCE_BYTES, offset, true);
        FigureBatches.floats(1, 3, INSTANCE_BYTES, offset + 12, true);
        FigureBatches.floats(2, 3, INSTANCE_BYTES, offset + 24, true);
        FigureBatches.floats(3, 3, INSTANCE_BYTES, offset + 36, true);
        FigureBatches.ints(4, 4, INSTANCE_BYTES, offset + 48, true);
        FigureBatches.floats(5, 2, INSTANCE_BYTES, offset + 64, true);
        FigureBatches.ints(6, 1, INSTANCE_BYTES, offset + 72, true);
    }

    /**
     * Once a second: whether the block is inside a sheet (a sail facing alike to its left or above it); if it is the
     * corner, how far the sheet runs right and down and each square's dye.
     */
    private static void measure(Sail sail, Level level, Direction facing) {
        long now = level.getGameTime();
        if (now - sail.measured < 20 && now >= sail.measured) {
            return;
        }
        sail.measured = now;
        BlockPos at = sail.getBlockPos();
        Direction across = facing.getClockWise();
        sail.inner = run(level, at, across.getOpposite(), facing, 1) > 0 || run(level, at, Direction.UP, facing, 1) > 0;
        if (sail.inner) {
            return;
        }
        // shortcut: the sheet is the rectangle of the top row and left column; a ragged sail draws only that.
        sail.right = run(level, at, across, facing, MOST);
        sail.down = run(level, at, Direction.DOWN, facing, MOST);
        int w = sail.right + 1;
        int[] colours = new int[w * (sail.down + 1)];
        BlockPos.MutableBlockPos cell = new BlockPos.MutableBlockPos();
        for (int r = 0; r <= sail.down; r++) {
            for (int c = 0; c < w; c++) {
                BlockState s = level.getBlockState(cell.set(at).move(across, c).move(Direction.DOWN, r));
                colours[r * w + c] = s.getBlock() instanceof SailBlock && s.getValue(SailBlock.FACING) == facing
                        ? 0xFF000000 | s.getValue(SailBlock.COLOR).getTextureDiffuseColor() : 0;
            }
        }
        sail.colours = colours;
    }

    private static int run(Level level, BlockPos from, Direction way, Direction facing, int most) {
        BlockPos.MutableBlockPos at = from.mutable();
        int n = 0;
        while (n < most) {
            BlockState s = level.getBlockState(at.move(way));
            if (!(s.getBlock() instanceof SailBlock) || s.getValue(SailBlock.FACING) != facing) {
                break;
            }
            n++;
        }
        return n;
    }

    private static double smoothstep(double from, double to, double x) {
        double t = Math.max(0, Math.min(1, (x - from) / (to - from)));
        return t * t * (3 - 2 * t);
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
        String[] names = {"Origin", "TurnX", "TurnY", "TurnZ", "Sheet", "Wave", "Light"};
        for (int a = 0; a < names.length; a++) {
            GL20.glBindAttribLocation(p, a, names[a]);
        }
        GL20.glLinkProgram(p);
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);
        if (GL20.glGetProgrami(p, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            RafasConquest.LOGGER.error("[sails] shader didn't link: {}", GL20.glGetProgramInfoLog(p));
            GL20.glDeleteProgram(p);
            usable = false;
            return false;
        }
        mvp = GL20.glGetUniformLocation(p, "Mvp");
        modulator = GL20.glGetUniformLocation(p, "ColorModulator");
        clock = GL20.glGetUniformLocation(p, "Seconds");
        canvas = GL20.glGetUniformLocation(p, "Canvas");
        dyes = GL20.glGetUniformLocation(p, "Dyes");
        instanceBuffer = GlStateManager._glGenBuffers();
        colourBuffer = GlStateManager._glGenBuffers();
        int oldActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + COLOUR_UNIT);
        colourTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, colourTexture);
        GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_R32UI, colourBuffer);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(oldActive);
        // No corner data: every input is per sheet, the corners come from gl_VertexID.
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        vao = GlStateManager._glGenVertexArrays();
        GlStateManager._glBindVertexArray(vao);
        GlStateManager._glBindVertexArray(oldVao);
        program = p;
        RafasConquest.LOGGER.info("[sails] drawing sails instanced");
        return true;
    }

    /**
     * The sheet's shape, with no physics: a set belly plus travelling ripples, after the way Total War's sails move. A
     * point (x across, y down from the top edge, both blocks) comes out as (across, up, forward): the top edge stays on
     * its yard, the sides and the foot's corners stay in the plane, and the belly is deepest a little below the
     * middle, where it also draws the foot up. Fill is how full the wind holds it, flap how much it ripples (0 to 1).
     */
    private static final String VERTEX = """
            #version 150
            in vec3 Origin;
            in vec3 TurnX;
            in vec3 TurnY;
            in vec3 TurnZ;
            in uvec4 Sheet; // w h k, its first dye
            in vec2 Wave;   // fill, flap
            in uint Light;
            uniform mat4 Mvp;
            uniform vec4 ColorModulator;
            uniform float Seconds;
            uniform usamplerBuffer Dyes;
            out vec2 uv;
            out vec4 tint;
            const float PI = 3.14159265;
            const float TAU = 6.2831853;
            const float DEPTH = 0.22;    // the deepest belly, as a share of the sheet's smaller side
            const float RIPPLE = 0.18;   // ripple height at the foot, blocks, flogging
            const float HALF_THICK = 1.0 / 64.0;
            float w, h;
            vec3 point(float x, float y) {
                float t = y / h;
                float s = 2.0 * x / w - 1.0;
                float across = 1.0 - s * s;
                float belly = Wave.x * DEPTH * min(w, h) * across * sin(PI * t / 1.6);
                // Ripples run aft and down, none at the yard; more as the cloth hangs looser.
                float hang = sin(PI * t / 2.0) * (0.25 + 0.75 * across);
                float ripple = RIPPLE * Wave.y * hang * (sin(x / 3.1 * TAU + y / 4.7 * TAU - Seconds * 5.0)
                        + 0.5 * sin(x / 1.9 * TAU - y / 2.6 * TAU - Seconds * 8.3));
                return vec3(x, -y + 0.35 * belly * t, belly + ripple);
            }
            void main() {
                w = float(Sheet.x);
                h = float(Sheet.y);
                int k = int(Sheet.z);
                int across = int(Sheet.x) * k;
                int face = across * int(Sheet.y) * k * 6;
                int v = gl_VertexID;
                float side = v < face ? 1.0 : -1.0;
                v -= v < face ? 0 : face;
                int square = v / 6;
                int corner = v - square * 6;
                int i = square % across + (corner == 2 || corner == 4 || corner == 5 ? 1 : 0);
                int j = square / across + (corner == 1 || corner == 2 || corner == 4 ? 1 : 0);
                uint dye = texelFetch(Dyes, int(Sheet.w) + (square / across / k) * int(Sheet.x) + (square % across) / k).r;
                if (dye == 0u) {
                    gl_Position = vec4(2.0, 2.0, 2.0, 1.0); // no sail there: off screen, no area
                    return;
                }
                float x = float(i) / float(k), y = float(j) / float(k);
                const float E = 0.05;
                vec3 c = cross(point(x, y + E) - point(x, y - E), point(x + E, y) - point(x - E, y));
                mat3 turn = mat3(TurnX, TurnY, TurnZ);
                vec3 n = normalize(turn * c) * side;
                gl_Position = Mvp * vec4(Origin + turn * point(x, y) + n * HALF_THICK, 1.0);
                // The game's block face shading: up 1, down 0.5, north/south 0.8, east/west 0.6.
                float shade = n.x * n.x * 0.6 + n.z * n.z * 0.8 + n.y * n.y * (n.y > 0.0 ? 1.0 : 0.5);
                vec4 light = vec4(float(Light & 255u), float((Light >> 8) & 255u), float((Light >> 16) & 255u), 255.0) / 255.0;
                vec4 dyed = vec4(float((dye >> 16) & 255u), float((dye >> 8) & 255u), float(dye & 255u), 255.0) / 255.0;
                tint = dyed * vec4(vec3(shade), 1.0) * light * ColorModulator;
                uv = vec2(float(i) / float(across), float(j) / float(int(Sheet.y) * k));
            }
            """;
    private static final String FRAGMENT = """
            #version 150
            uniform sampler2D Canvas;
            in vec2 uv;
            in vec4 tint;
            out vec4 fragColor;
            void main() {
                vec4 c = texture(Canvas, uv);
                if (c.a < 0.1) {
                    discard;
                }
                fragColor = c * tint;
            }
            """;
}
