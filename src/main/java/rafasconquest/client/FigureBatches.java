package rafasconquest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.lwjgl.opengl.ARBInstancedArrays;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import rafasconquest.RafasConquest;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Battle figures drawn as Total War draws its soldiers: each figure {@link SoldierFigure.Shape} is a model the graphics
 * card keeps, built once; each frame only a small record per soldier goes up (where it stands, its size, which way it
 * faces, its light, its colours), and each shape's soldiers are drawn in one call. Colours are named slots in the model,
 * filled from each soldier's {@link SoldierFigure.Palette}, so one model serves every army.
 * <p>
 * Render thread only. Leaves the game's GL state as it found it: its program, vertex array and array buffer.
 */
final class FigureBatches {
    /** A model's corner: where it is (right, up, forward), how it is shaded (side's facing right and forward, or a set shade), its colour. */
    static final int VERTEX_INTS = 7;
    private static final int VERTEX_BYTES = VERTEX_INTS * 4;
    /** A soldier's record: x y z scale, facing x z, light, flags, 8 colours. */
    private static final int INSTANCE_BYTES = 64;
    private static final int GLOW = 1;
    /** A flat figure is shaded like the side of a block, as its rectangles always face the camera. */
    static final float FLAT_SHADE = 0.8F;

    private FigureBatches() {
    }

    // ---- models, built without the graphics card ----

    /** A model's corners, {@link #VERTEX_INTS} each, three to a triangle. */
    static int[] bake(SoldierFigure.Shape shape) {
        Corners out = new Corners();
        if (shape.flat()) {
            DistantArmyRenderer.flatFigure(out, shape, SoldierFigure.Palette.SLOTS);
        } else {
            SoldierFigure.build(shape, SoldierFigure.Palette.SLOTS, out);
        }
        return java.util.Arrays.copyOf(out.ints, out.size);
    }

    /** Collects a model: boxes (3D figures) and rectangles (flat ones) into triangles. */
    private static final class Corners implements SoldierFigure.Boxes, DistantArmyRenderer.Rects {
        int[] ints = new int[VERTEX_INTS * 64];
        int size;

        @Override
        public void box(double right, double back, double halfWidth, double halfDepth, double bottom, double top, int rgb,
                        SoldierFigure.Stuff stuff) {
            float[] r = new float[4];
            float[] f = new float[4];
            for (int i = 0; i < 4; i++) { // round the footprint: front left, front right, back right, back left
                r[i] = (float) (right + halfWidth * (i == 1 || i == 2 ? 1 : -1));
                f[i] = (float) (-back + halfDepth * (i < 2 ? 1 : -1));
            }
            float lo = (float) bottom;
            float hi = (float) top;
            quad(rgb, 0, 0, 1, r[0], hi, f[0], r[1], hi, f[1], r[2], hi, f[2], r[3], hi, f[3]); // top, no bottom: never seen
            float[][] normal = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // front, right, back, left
            for (int i = 0; i < 4; i++) {
                int j = (i + 1) % 4;
                quad(rgb, normal[i][0], normal[i][1], 0, r[i], lo, f[i], r[j], lo, f[j], r[j], hi, f[j], r[i], hi, f[i]);
            }
        }

        @Override
        public void rect(float right, float bottom, float width, float height, int rgb) {
            float l = right - width / 2;
            float h = right + width / 2;
            float top = bottom + height;
            quad(rgb, 0, 0, FLAT_SHADE, l, bottom, 0, h, bottom, 0, h, top, 0, l, top, 0);
        }

        /** Two triangles; {@code shade} > 0 is the face's own shade, else it is shaded by which way ({@code nr}, {@code nf}) it faces. */
        private void quad(int rgb, float nr, float nf, float shade, float... v) {
            for (int corner : new int[]{0, 1, 2, 0, 2, 3}) {
                if (size + VERTEX_INTS > ints.length) {
                    ints = java.util.Arrays.copyOf(ints, ints.length * 2);
                }
                ints[size++] = Float.floatToRawIntBits(v[corner * 3]);
                ints[size++] = Float.floatToRawIntBits(v[corner * 3 + 1]);
                ints[size++] = Float.floatToRawIntBits(v[corner * 3 + 2]);
                ints[size++] = Float.floatToRawIntBits(nr);
                ints[size++] = Float.floatToRawIntBits(nf);
                ints[size++] = Float.floatToRawIntBits(shade);
                ints[size++] = rgb;
            }
        }
    }

    /**
     * How a side facing ({@code nx}, {@code nz}) is shaded, as the game shades a block's: east or west 0.6, north or south
     * 0.8. The shader does the same.
     */
    static float sideShade(float nx, float nz) {
        return 0.6F + 0.2F * nz * nz;
    }

    // ---- this frame's soldiers ----

    private static final class Batch {
        final int vao;
        final int vertices;
        ByteBuffer instances = MemoryUtil.memAlloc(INSTANCE_BYTES * 256);
        int count;

        Batch(int vao, int vertices) {
            this.vao = vao;
            this.vertices = vertices;
        }
    }

    private static final Map<SoldierFigure.Shape, Batch> BATCHES = new LinkedHashMap<>();
    private static int program = -1;
    private static int instanceBuffer;
    private static int mvp;
    private static int modulator;
    /** False once the graphics card is found unable (no instancing, or the shader didn't build): figures then aren't drawn. */
    private static boolean usable = true;
    private static boolean divisorArb;

    /** Starts a frame's (or a glow pass's) soldiers. */
    static void begin() {
        for (Batch b : BATCHES.values()) {
            b.count = 0;
        }
    }

    /**
     * One soldier of {@code shape}: its feet ({@code x}, {@code y}, {@code z}) from the camera, drawn {@code scale} times
     * its size, facing ({@code fx}, {@code fz}) (a flat figure: square to the camera), in {@code light}; {@code glow}: all
     * white, for a selected figure's outline.
     */
    static void add(SoldierFigure.Shape shape, int[] palette, float x, float y, float z, float scale, float fx, float fz, int light,
                    boolean glow) {
        if (!usable) {
            return;
        }
        Batch b = BATCHES.get(shape);
        if (b == null) {
            if (!ready()) {
                return;
            }
            b = upload(shape);
            BATCHES.put(shape, b);
        }
        if ((b.count + 1) * INSTANCE_BYTES > b.instances.capacity()) {
            b.instances = MemoryUtil.memRealloc(b.instances, b.instances.capacity() * 2);
            b.instances.clear(); // keeps what's written; lets the new room be written
        }
        ByteBuffer i = b.instances;
        int at = b.count * INSTANCE_BYTES;
        i.putFloat(at, x).putFloat(at + 4, y).putFloat(at + 8, z).putFloat(at + 12, scale);
        i.putFloat(at + 16, fx).putFloat(at + 20, fz).putInt(at + 24, light).putInt(at + 28, glow ? GLOW : 0);
        for (int s = 0; s < 8; s++) {
            i.putInt(at + 32 + s * 4, palette[s]);
        }
        b.count++;
    }

    /** Draws everything added since {@link #begin}, through {@code pose} (the level's pose, as the game's own shaders take it). */
    static void draw(Matrix4f pose) {
        if (program < 0) {
            return;
        }
        boolean any = false;
        for (Batch b : BATCHES.values()) {
            any |= b.count > 0;
        }
        if (!any) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        RenderSystem.activeTexture(GL13.GL_TEXTURE2);
        RenderSystem.bindTexture(RenderSystem.getShaderTexture(2));
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
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
        for (Batch b : BATCHES.values()) {
            if (b.count == 0) {
                continue;
            }
            GlStateManager._glBindVertexArray(b.vao);
            GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b.instances.limit(b.count * INSTANCE_BYTES), GL15.GL_STREAM_DRAW);
            b.instances.clear();
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, b.vertices, b.count);
        }
        GlStateManager._glUseProgram(oldProgram);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        RenderSystem.enableCull();
        mc.gameRenderer.lightTexture().turnOffLightLayer();
    }

    // ---- the graphics card's side ----

    /** Builds the shader and the soldiers' buffer on first use; false (for good) if this graphics card can't. */
    private static boolean ready() {
        if (program >= 0) {
            return true;
        }
        GLCapabilities caps = GL.getCapabilities();
        // shortcut: no fallback drawing on a card without instancing (pre-2010); add the old per-figure path back if anyone needs it.
        if (!caps.OpenGL31 || !caps.OpenGL33 && !caps.GL_ARB_instanced_arrays) {
            return fail("this graphics card has no instanced drawing (OpenGL 3.3)");
        }
        divisorArb = !caps.OpenGL33;
        int vs = shader(GL20.GL_VERTEX_SHADER, VERTEX);
        int fs = shader(GL20.GL_FRAGMENT_SHADER, FRAGMENT);
        if (vs < 0 || fs < 0) {
            return fail("its shader didn't build");
        }
        int p = GL20.glCreateProgram();
        GL20.glAttachShader(p, vs);
        GL20.glAttachShader(p, fs);
        String[] names = {"Local", "Shade", "Colour", "PosScale", "Facing", "Light", "Flags", "PalA", "PalB"};
        for (int a = 0; a < names.length; a++) {
            GL20.glBindAttribLocation(p, a, names[a]);
        }
        GL20.glLinkProgram(p);
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);
        if (GL20.glGetProgrami(p, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            RafasConquest.LOGGER.error("[figures] shader didn't link: {}", GL20.glGetProgramInfoLog(p));
            GL20.glDeleteProgram(p);
            return fail("its shader didn't link");
        }
        mvp = GL20.glGetUniformLocation(p, "Mvp");
        modulator = GL20.glGetUniformLocation(p, "ColorModulator");
        int oldProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        GlStateManager._glUseProgram(p);
        GL20.glUniform1i(GL20.glGetUniformLocation(p, "Sampler2"), 2);
        GlStateManager._glUseProgram(oldProgram);
        instanceBuffer = GlStateManager._glGenBuffers();
        program = p;
        RafasConquest.LOGGER.info("[figures] drawing battle figures instanced");
        return true;
    }

    private static boolean fail(String why) {
        usable = false;
        RafasConquest.LOGGER.error("[figures] can't draw distant battle figures: {}", why);
        return false;
    }

    private static int shader(int type, String source) {
        int s = GL20.glCreateShader(type);
        GL20.glShaderSource(s, source);
        GL20.glCompileShader(s);
        if (GL20.glGetShaderi(s, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            RafasConquest.LOGGER.error("[figures] shader didn't compile: {}", GL20.glGetShaderInfoLog(s));
            GL20.glDeleteShader(s);
            return -1;
        }
        return s;
    }

    /** A shape's model, up on the graphics card for good, with its vertex array reading the soldiers' buffer. */
    private static Batch upload(SoldierFigure.Shape shape) {
        int[] corners = bake(shape);
        int oldVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int vao = GlStateManager._glGenVertexArrays();
        GlStateManager._glBindVertexArray(vao);
        int model = GlStateManager._glGenBuffers();
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, model);
        ByteBuffer data = MemoryUtil.memAlloc(corners.length * 4);
        data.asIntBuffer().put(corners);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(data);
        floats(0, 3, VERTEX_BYTES, 0, false);
        floats(1, 3, VERTEX_BYTES, 12, false);
        ints(2, 1, VERTEX_BYTES, 24, false);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceBuffer);
        floats(3, 4, INSTANCE_BYTES, 0, true);
        floats(4, 2, INSTANCE_BYTES, 16, true);
        ints(5, 1, INSTANCE_BYTES, 24, true);
        ints(6, 1, INSTANCE_BYTES, 28, true);
        ints(7, 4, INSTANCE_BYTES, 32, true);
        ints(8, 4, INSTANCE_BYTES, 48, true);
        GlStateManager._glBindVertexArray(oldVao);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
        BufferUploader.invalidate();
        return new Batch(vao, corners.length / VERTEX_INTS);
    }

    private static void floats(int index, int size, int stride, long offset, boolean perSoldier) {
        GL20.glEnableVertexAttribArray(index);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, stride, offset);
        divisor(index, perSoldier);
    }

    private static void ints(int index, int size, int stride, long offset, boolean perSoldier) {
        GL20.glEnableVertexAttribArray(index);
        GL30.glVertexAttribIPointer(index, size, GL11.GL_UNSIGNED_INT, stride, offset);
        divisor(index, perSoldier);
    }

    private static void divisor(int index, boolean perSoldier) {
        if (divisorArb) {
            ARBInstancedArrays.glVertexAttribDivisorARB(index, perSoldier ? 1 : 0);
        } else {
            GL33.glVertexAttribDivisor(index, perSoldier ? 1 : 0);
        }
    }

    private static final String VERTEX = """
            #version 150
            in vec3 Local;
            in vec3 Shade;
            in uint Colour;
            in vec4 PosScale;
            in vec2 Facing;
            in uint Light;
            in uint Flags;
            in uvec4 PalA;
            in uvec4 PalB;
            uniform mat4 Mvp;
            uniform sampler2D Sampler2;
            uniform vec4 ColorModulator;
            out vec4 vertexColor;

            vec3 rgb(uint c) {
                return vec3(float((c >> 16) & 255u), float((c >> 8) & 255u), float(c & 255u)) / 255.0;
            }

            void main() {
                vec2 forward = Facing;
                vec2 right = vec2(-forward.y, forward.x);
                vec2 across = right * Local.x + forward * Local.z;
                gl_Position = Mvp * vec4(PosScale.xyz + vec3(across.x, Local.y, across.y) * PosScale.w, 1.0);
                uint c = Colour;
                if ((c & 0x1000000u) != 0u) {
                    int slot = int(c & 7u);
                    c = slot < 4 ? PalA[slot] : PalB[slot - 4];
                }
                float shade = Shade.z;
                if (shade <= 0.0) {
                    vec2 n = right * Shade.x + forward * Shade.y; // the side's facing, east and south
                    shade = 0.6 + 0.2 * n.y * n.y;
                }
                vec3 colour = rgb(c) * shade;
                if ((Flags & 1u) != 0u) {
                    colour = vec3(1.0);
                }
                vec2 uv = vec2(float(Light & 0xFFFFu), float(Light >> 16));
                vec4 light = texture(Sampler2, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));
                vertexColor = vec4(colour, 1.0) * light * ColorModulator;
            }
            """;

    private static final String FRAGMENT = """
            #version 150
            in vec4 vertexColor;
            out vec4 fragColor;

            void main() {
                fragColor = vertexColor;
            }
            """;
}
