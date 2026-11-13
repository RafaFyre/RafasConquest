package rafasconquest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.Sea;
import rafasconquest.core.Swell;

import net.minecraft.Util;
import rafasconquest.core.SeaCalm;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Draws the {@link Swell} on the sea as Physics Mod draws its ocean: Minecraft's translucent terrain shader (overridden in
 * this mod's assets) and, where Sodium draws the world instead, Sodium's terrain shaders (patched as they load) lift
 * each corner of the still sea's surface by the swell there, shade the water by its slope, and lay foam on it. They get
 * each wave's phase at the camera (sent each frame) and work from how far off the camera the corner is, so far from the
 * world's middle it stays smooth; a texture of the sea's calm ({@link rafasconquest.core.SeaCalm}) over {@link #SIZE}
 * blocks round the camera, so the sea lies flat by the shore; and a 3D foam noise texture of our own, made in the shape
 * of Physics Mod's (two channels: soft blotches, and cells). Only water at sea level moves: a source block's top, with
 * nothing above it, sits 8/9 of a block up. A shader pack (Iris) draws its own water, and this is left out.
 */
public final class WaterSwell {
    private static final String SHADER = "blocks/block_layer_opaque.vsh";
    private static final String AFTER = "vec3 position = _vert_position + translation;";
    private static final String FRAGMENT = "blocks/block_layer_opaque.fsh";
    private static final String BEFORE_FOG = "fragColor = _linearFog(";
    private static final double SURFACE = 8 / 9.0;
    /** The calm texture's side, in blocks (one texel a column); it is made again once the camera is this far / 8 off its middle. */
    static final int SIZE = 512;
    private static final int SODIUM_UNIT = 6;
    /** The foam texture's unit: past the ones Minecraft keeps track of (0–11), so nothing binds over it. */
    private static final int FOAM_UNIT = 14;
    /** The foam texture's side and depth (time), in texels. */
    static final int FOAM_SIDE = 256;
    static final int FOAM_SLICES = 16;
    /** The foam is read at 0.26, 0.02 and 0.1 a block, which all repeat over 50 blocks, so the camera's place is sent wrapped to it. */
    static final int FOAM_WRAP = 50;
    /** Foam boils through its noise and back every this many seconds. */
    static final int FOAM_TIME = 360;

    private static int texture = -1;
    private static int foam = -1;
    private static int originX;
    private static int originZ;
    private static long madeAt = Long.MIN_VALUE;

    private WaterSwell() {
    }

    /** Sodium's shader source as loaded, with the swell added to its terrain vertex shader and the foam to its fragment shader. */
    public static String patch(String path, String source) {
        boolean vertex = path.equals(SHADER);
        if (!vertex && !path.equals(FRAGMENT)) {
            return source;
        }
        String after = vertex ? AFTER : BEFORE_FOG;
        int at = source.indexOf(after);
        int main = source.indexOf("void main()");
        if (at < 0 || main < 0) {
            RafasConquest.LOGGER.warn("[swell] Sodium's terrain shader has changed; the sea is drawn flat");
            return source;
        }
        String head = "uniform mat4 u_SwellPhaseA;\nuniform mat4 u_SwellPhaseB;\nuniform mat4 u_SwellPhaseC;\nuniform float u_SwellTall;\n" + (vertex
                ? "uniform float u_SwellSurface;\nuniform vec3 u_SwellCalmAt;\nuniform sampler2D u_SwellCalm;\nout vec4 v_SwellAt;\nout vec3 v_SwellLight;\n\n"
                + Swell.glsl()
                : "uniform vec3 u_SwellFoamAt;\nuniform sampler3D u_SwellFoam;\nin vec4 v_SwellAt;\nin vec3 v_SwellLight;\n\n" + Swell.glsl() + "\n\n" + foamGlsl()) + "\n\n";
        if (!vertex) {
            return source.substring(0, main) + head + source.substring(main, at)
                    + foamMix("color", "u_", "v_SwellAt", "v_SwellLight") + "\n    " + source.substring(at);
        }
        String lift = "\n    " + lift("position", "u_", "v_SwellAt") + "\n    v_SwellLight = texture(u_LightTex, _vert_tex_light_coord).rgb;";
        int end = at + after.length();
        return source.substring(0, main) + head + source.substring(main, end) + lift + source.substring(end);
    }

    /**
     * Distant Horizons' terrain shader as loaded, with the sea's slope light and foam laid on its water tops (DH's water
     * material, 12, facing up, at sea level). Shading only: DH merges water into big quads, so nothing is lifted, and
     * the calm is read per fragment (open sea past the calm texture). The swell fades out as a pixel covers more than a
     * few blocks, so the far sea doesn't shimmer. Uniforms come from {@link #upload}, as for Sodium.
     */
    public static String patchDh(String path, String source) {
        boolean vertex = path.endsWith("terrain/gl/vert.vert");
        if (!vertex && !path.endsWith("terrain/gl/frag.frag")) {
            return source;
        }
        String anchor = vertex ? "gl_Position = uCombinedMatrix" : "float viewDist = length(vertexWorldPos);";
        int at = source.indexOf(anchor);
        int main = source.indexOf("void main()");
        if (at < 0 || main < 0) {
            RafasConquest.LOGGER.warn("[swell] Distant Horizons' terrain shader has changed; its sea is drawn plain");
            return source;
        }
        if (vertex) {
            String head = "uniform float u_SwellSurface;\nout vec4 v_SwellAt;\nout vec3 v_SwellLight;\n\n";
            String body = "v_SwellAt = vec4(0.0);\n    v_SwellLight = vec3(0.0);\n"
                    + "    if (irisData.x == 12u && irisData.y == 1u && abs(vPosition.y + uModelOffset.y - u_SwellSurface) < 0.6) {\n"
                    + "        v_SwellAt = vec4(vPosition.xz + uModelOffset.xz, 1.0, 1.0);\n"
                    + "        v_SwellLight = texture(uLightMap, vec2(skyLight, blockLight)).xyz;\n    }\n    ";
            return source.substring(0, main) + head + source.substring(main, at) + body + source.substring(at);
        }
        String head = "uniform mat4 u_SwellPhaseA;\nuniform mat4 u_SwellPhaseB;\nuniform mat4 u_SwellPhaseC;\nuniform float u_SwellTall;\n"
                + "uniform vec3 u_SwellCalmAt;\nuniform sampler2D u_SwellCalm;\nuniform vec3 u_SwellFoamAt;\nuniform sampler3D u_SwellFoam;\n"
                + "in vec4 v_SwellAt;\nin vec3 v_SwellLight;\n\n" + Swell.glsl() + "\n\n" + foamGlsl() + "\n\n";
        String body = "vec4 swellAt = v_SwellAt;\n"
                + "    vec2 swellCover = fwidth(swellAt.xy);\n"
                + "    if (swellAt.w > 0.99) {\n"
                + "        vec2 c = (swellAt.xy - u_SwellCalmAt.xy) * u_SwellCalmAt.z;\n"
                + "        float calm = all(greaterThanEqual(c, vec2(0.0))) && all(lessThanEqual(c, vec2(1.0))) ? texture(u_SwellCalm, c).r : 1.0;\n"
                + "        swellAt.z = calm * (1.0 - smoothstep(2.0, 8.0, max(swellCover.x, swellCover.y)));\n"
                + "        if (swellAt.z < 0.01) {\n            swellAt.w = 0.0; // faded out: skip the 48 waves\n        }\n    }\n    "
                + foamMix("fragColor", "u_", "swellAt", "v_SwellLight") + "\n    ";
        return source.substring(0, main) + head + source.substring(main, at) + body + source.substring(at);
    }

    /**
     * The GLSL adding the foam and the slope's light to a fragment's colour, lit by the light at it (as Physics Mod's
     * ocean does), and making the sea's surface less see-through than Minecraft's water (most on open sea, a little less
     * by the shore): the same in both shaders, {@code u} the uniforms' prefix.
     */
    public static String foamMix(String color, String u, String at, String light) {
        return "float swellShade;\n"
                + "    float swellFoam = whitecap(" + at + ", " + u + "SwellPhaseA, " + u + "SwellPhaseB, " + u + "SwellPhaseC, "
                + u + "SwellTall, " + u + "SwellFoamAt, " + u + "SwellFoam, swellShade);\n"
                + "    if (" + at + ".w > 0.99) {\n"
                + "        " + color + ".a = max(" + color + ".a, mix(0.8, 0.92, " + at + ".z));\n    }\n"
                + "    " + color + " = clamp(" + color + " + vec4(" + light + ", 1.0) * (swellFoam + swellShade), 0.0, 1.0);";
    }

    /**
     * The GLSL lifting a camera-relative position on the still sea's surface, the same in both shaders ({@code u} the
     * uniforms' prefix); it also hands the fragment shader (in {@code at}) where on the sea the corner is and how much
     * of the swell it gets.
     */
    public static String lift(String pos, String u, String at) {
        return at + " = vec4(0.0);\n"
                + "    if (abs(" + pos + ".y - " + u + "SwellSurface) < 0.01) { // the still sea's surface\n"
                + "        float swellCalm = texture(" + u + "SwellCalm, (" + pos + ".xz - " + u + "SwellCalmAt.xy) * " + u + "SwellCalmAt.z).r;\n"
                + "        " + pos + ".y += swellCalm * " + u + "SwellTall * swell(" + pos + ".xz, " + u + "SwellPhaseA, " + u + "SwellPhaseB, " + u + "SwellPhaseC);\n"
                + "        " + at + " = vec4(" + pos + ".xz, swellCalm, 1.0);\n    }";
    }

    /**
     * GLSL for the sea's foam and shading, as Physics Mod's ocean lays them:
     * {@code float whitecap(vec4 at, mat4 pa, mat4 pb, mat4 pc, float tall, vec3 foamAt, sampler3D foam, out float shade)}
     * gives how much foam (0–1) lies at a point of the sea ({@code at} from {@link #lift}), with {@code foamAt} the
     * camera's place wrapped to {@link #FOAM_WRAP} blocks and the time in seconds wrapped to {@link #FOAM_TIME};
     * {@code shade} is the light the water's slope throws. The water's normal leans with the waves' slope; the crest's
     * height times how level it lies there (normal.y⁴) is where foam covers the sea; elsewhere only the rare peaks of the
     * three foam blotches (read at 0.26, 0.02 and 0.1 a block, slowly boiling through time) show as scum; and the foam's
     * cells (two sizes) break it all up. Foam rides where the waves push the water, so it bunches on the crests.
     */
    public static String foamGlsl() {
        return """
                float whitecap(vec4 at, mat4 pa, mat4 pb, mat4 pc, float tall, vec3 foamAt, sampler3D foam, out float shade) {
                    shade = 0.0;
                    vec2 pixel = fwidth(at.xy); // before any branch, as derivatives need
                    if (at.w < 0.99) {
                        return 0.0;
                    }
                    vec2 slope;
                    vec2 moved;
                    float h = swellDetail(at.xy, pa, pb, pc, max(pixel.x, pixel.y), slope, moved) * tall * at.z;
                    float lean = tall / 13.0 * at.z;
                    vec3 n = normalize(vec3(slope.x * lean, 0.6, slope.y * lean));
                    shade = mix(n.x * 0.5 + 0.5, 0.0, pow(n.y, 4.0)) * 0.3;
                    float crest = clamp(h * pow(max(n.y, 0.0), 4.0) * 1.2, 0.0, 1.0);
                    vec2 w = mix(at.xy, moved, clamp(at.z * 2.0, 0.2, 1.0)) + foamAt.xy;
                    float t = foamAt.z / 360.0;
                    vec2 s1 = textureLod(foam, vec3(w * 0.26, t), 0.0).rg;
                    vec2 s2 = textureLod(foam, vec3(w * 0.02, t + 0.5), 0.0).rg;
                    vec2 s3 = textureLod(foam, vec3(w * 0.1, t + 1.0), 0.0).rg;
                    float cover = s1.r * s2.r * s3.r * 2.8 * 0.8;
                    cover = (1.0 - crest) * cover + crest * 0.8;
                    float cells = 0.2 + 0.8 * s1.g * (1.0 - s2.g);
                    return clamp(smoothstep(0.45, 1.0, cover) * cells, 0.0, 1.0);
                }""";
    }

    /** The camera's place wrapped for the foam noise, and the time in seconds wrapped likewise. */
    private static float[] foamAt(ClientLevel level) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        double seconds = (level.getGameTime() + mc.getTimer().getGameTimeDeltaPartialTick(false)) / 20.0;
        return new float[] {(float) Math.floorMod((long) Math.floor(cam.x), FOAM_WRAP) + (float) (cam.x - Math.floor(cam.x)),
                (float) Math.floorMod((long) Math.floor(cam.z), FOAM_WRAP) + (float) (cam.z - Math.floor(cam.z)),
                (float) (seconds % FOAM_TIME)};
    }

    /**
     * Before the world is drawn: the camera's wave phases, the sea's height, the calm texture's corner off it and the
     * foam's place, for Minecraft's own translucent terrain shader (assets/minecraft/shaders/core/rendertype_translucent.vsh).
     */
    public static void prepare() {
        ShaderInstance shader = GameRenderer.getRendertypeTranslucentShader();
        ClientLevel level = Minecraft.getInstance().level;
        if (shader == null || level == null) {
            return;
        }
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        Swell.weather(level.getRainLevel(partial), level.getThunderLevel(partial));
        calmTexture(level);
        bindFoam();
        float[] p = phases(level);
        shader.safeGetUniform("SwellPhaseA").set(new Matrix4f().set(p, 0));
        shader.safeGetUniform("SwellPhaseB").set(new Matrix4f().set(p, 16));
        shader.safeGetUniform("SwellPhaseC").set(new Matrix4f().set(p, 32));
        shader.safeGetUniform("SwellTall").set((float) Swell.tall());
        shader.safeGetUniform("SwellSurface").set(surface(level));
        float[] at = calmAt();
        shader.safeGetUniform("SwellCalmAt").set(at[0], at[1], at[2]);
        float[] foamAt = foamAt(level);
        shader.safeGetUniform("SwellFoamAt").set(foamAt[0], foamAt[1], foamAt[2]);
        shader.setSampler("SwellCalm", texture);
    }

    /** The same, sent to Sodium's terrain shader just bound. */
    public static void upload() {
        ClientLevel level = Minecraft.getInstance().level;
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int phaseA = GL20.glGetUniformLocation(program, "u_SwellPhaseA");
        if (level == null || phaseA < 0 || texture < 0 || foam < 0) {
            return;
        }
        float[] p = phases(level);
        GL20.glUniformMatrix4fv(phaseA, false, Arrays.copyOfRange(p, 0, 16));
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(program, "u_SwellPhaseB"), false, Arrays.copyOfRange(p, 16, 32));
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(program, "u_SwellPhaseC"), false, Arrays.copyOfRange(p, 32, 48));
        GL20.glUniform1f(GL20.glGetUniformLocation(program, "u_SwellTall"), (float) Swell.tall());
        GL20.glUniform1f(GL20.glGetUniformLocation(program, "u_SwellSurface"), surface(level));
        float[] at = calmAt();
        GL20.glUniform3f(GL20.glGetUniformLocation(program, "u_SwellCalmAt"), at[0], at[1], at[2]);
        float[] foamAt = foamAt(level);
        GL20.glUniform3f(GL20.glGetUniformLocation(program, "u_SwellFoamAt"), foamAt[0], foamAt[1], foamAt[2]);
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "u_SwellCalm"), SODIUM_UNIT);
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "u_SwellFoam"), FOAM_UNIT);
        GlStateManager._activeTexture(GL13.GL_TEXTURE0 + SODIUM_UNIT);
        GlStateManager._bindTexture(texture);
        GlStateManager._activeTexture(GL13.GL_TEXTURE0);
        bindFoam();
    }

    /** Binds the foam texture on its own unit (made the first time). */
    private static void bindFoam() {
        GlStateManager._activeTexture(GL13.GL_TEXTURE0 + FOAM_UNIT);
        if (foam < 0) {
            byte[] texels = foamTexels();
            ByteBuffer buffer = MemoryUtil.memAlloc(texels.length);
            buffer.put(texels).flip();
            foam = GL11.glGenTextures();
            GL11.glBindTexture(GL12.GL_TEXTURE_3D, foam);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL30.GL_RG8, FOAM_SIDE, FOAM_SIDE, FOAM_SLICES, 0, GL30.GL_RG, GL11.GL_UNSIGNED_BYTE, buffer);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL11.GL_REPEAT);
            MemoryUtil.memFree(buffer);
        }
        GL11.glBindTexture(GL12.GL_TEXTURE_3D, foam);
        GlStateManager._activeTexture(GL13.GL_TEXTURE0);
    }

    /**
     * Our own foam noise, {@link #FOAM_SIDE}² × {@link #FOAM_SLICES} texels of two bytes, repeating every way. Red: soft
     * blotches (gradient noise, 32 then 16 texels across, two slices deep), held to a mean of 0.5 and a spread of 0.075.
     * Green: cells, brightest at each cell's point (64 texels across, two slices deep), a mean of 0.475 and spread of 0.116.
     */
    static byte[] foamTexels() {
        int n = FOAM_SIDE * FOAM_SIDE * FOAM_SLICES;
        double[] red = new double[n];
        double[] green = new double[n];
        for (int z = 0; z < FOAM_SLICES; z++) {
            for (int y = 0; y < FOAM_SIDE; y++) {
                for (int x = 0; x < FOAM_SIDE; x++) {
                    int i = (z * FOAM_SIDE + y) * FOAM_SIDE + x;
                    red[i] = gradient(x / 32.0, y / 32.0, (z + 0.5) / 2, 8, 8) + 0.5 * gradient(x / 16.0, y / 16.0, z + 0.5, 16, 16);
                    green[i] = -nearestPoint(x / 64.0, y / 64.0, z / 2.0, 4, 8);
                }
            }
        }
        spread(red, 0.498, 0.075);
        spread(green, 0.475, 0.116);
        byte[] out = new byte[n * 2];
        for (int i = 0; i < n; i++) {
            out[i * 2] = (byte) Math.round(red[i] * 255);
            out[i * 2 + 1] = (byte) Math.round(green[i] * 255);
        }
        return out;
    }

    /** Sets the values to this mean and spread (standard deviation), held between 0 and 1. */
    private static void spread(double[] v, double mean, double deviation) {
        double m = Arrays.stream(v).average().orElse(0);
        double var = Arrays.stream(v).map(x -> (x - m) * (x - m)).average().orElse(1);
        double scale = deviation / Math.sqrt(var);
        for (int i = 0; i < v.length; i++) {
            v[i] = Math.max(0, Math.min(1, mean + (v[i] - m) * scale));
        }
    }

    private static int hash(int x, int y, int z) {
        int h = x * 374761393 + y * 668265263 + z * 1274126177;
        h = (h ^ (h >>> 13)) * 1274126177;
        return h ^ (h >>> 16);
    }

    private static final double[][] WAYS = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1},
            {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};

    /** Gradient (Perlin) noise repeating every {@code period} cells across and {@code depth} cells deep. */
    private static double gradient(double x, double y, double z, int period, int depth) {
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int z0 = (int) Math.floor(z);
        double fx = x - x0;
        double fy = y - y0;
        double fz = z - z0;
        double sum = 0;
        for (int c = 0; c < 8; c++) {
            int i = c & 1;
            int j = c >> 1 & 1;
            int k = c >> 2;
            double[] g = WAYS[Math.floorMod(hash(Math.floorMod(x0 + i, period), Math.floorMod(y0 + j, period), Math.floorMod(z0 + k, depth)), 12)];
            double dot = g[0] * (fx - i) + g[1] * (fy - j) + g[2] * (fz - k);
            sum += dot * fade(i == 1 ? fx : 1 - fx) * fade(j == 1 ? fy : 1 - fy) * fade(k == 1 ? fz : 1 - fz);
        }
        return sum;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /** How far to the nearest of the points scattered one a cell (cell noise), repeating every {@code period} cells across and {@code depth} deep. */
    private static double nearestPoint(double x, double y, double z, int period, int depth) {
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int z0 = (int) Math.floor(z);
        double best = Double.MAX_VALUE;
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                for (int i = -1; i <= 1; i++) {
                    int h = hash(Math.floorMod(x0 + i, period), Math.floorMod(y0 + j, period), Math.floorMod(z0 + k, depth));
                    double px = x0 + i + (h & 1023) / 1024.0 - x;
                    double py = y0 + j + (h >>> 10 & 1023) / 1024.0 - y;
                    double pz = z0 + k + (h >>> 20 & 1023) / 1024.0 - z;
                    best = Math.min(best, px * px + py * py + pz * pz);
                }
            }
        }
        return Math.sqrt(best);
    }

    /**
     * Makes the calm texture again when the camera has gone an eighth of it off its middle, or it is half a minute old:
     * worked out on a background thread (it reads a thousand chunks' water), and sent to the card once done. Until the
     * first is done the sea lies flat.
     */
    private static void calmTexture(ClientLevel level) {
        if (texture < 0) {
            texture = GlStateManager._genTexture(); // bound before it has texels: reads as calm, so a flat sea
        }
        if (making != null) {
            if (!making.isDone()) {
                return;
            }
            Made made = making.getNow(null);
            making = null;
            if (made != null && made.level == level) {
                upload(made);
            }
        }
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        int x0 = (((int) Math.floor(cam.x)) >> 4 << 4) - SIZE / 2;
        int z0 = (((int) Math.floor(cam.z)) >> 4 << 4) - SIZE / 2;
        long now = level.getGameTime();
        if (madeAt != Long.MIN_VALUE && Math.abs(x0 - originX) < SIZE / 8 && Math.abs(z0 - originZ) < SIZE / 8 && Math.abs(now - madeAt) < 600) {
            return;
        }
        if (workerLevel != level) {
            workerLevel = level;
            worker = Sea.unshared(level);
        }
        SeaCalm calm = worker;
        madeAt = now; // not asked again till this one is done and old
        originX = x0;
        originZ = z0;
        making = CompletableFuture.supplyAsync(() -> {
            try {
                return new Made(level, x0, z0, calm.at(now).square(x0, z0, SIZE));
            } catch (RuntimeException e) { // a chunk changed under the read: tried again in half a minute
                RafasConquest.LOGGER.debug("[swell] calm texture read failed", e);
                return null;
            }
        }, Util.backgroundExecutor());
    }

    private record Made(ClientLevel level, int x0, int z0, byte[] strengths) {
    }

    private static CompletableFuture<Made> making;
    /** The calm the background thread works with: only ever one job at a time touches it. */
    private static SeaCalm worker;
    private static ClientLevel workerLevel;
    /** The texture's corner as uploaded (originX/Z are where the job in hand reads). */
    private static int textureX;
    private static int textureZ;

    private static void upload(Made made) {
        byte[] strengths = made.strengths;
        ByteBuffer buffer = MemoryUtil.memAlloc(strengths.length);
        buffer.put(strengths).flip();
        GlStateManager._bindTexture(texture);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R8, SIZE, SIZE, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        MemoryUtil.memFree(buffer);
        textureX = made.x0;
        textureZ = made.z0;
    }

    /** The calm texture's corner off the camera (x, z), and one over its side, for the shaders. */
    private static float[] calmAt() {
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        return new float[] {(float) (textureX - cam.x), (float) (textureZ - cam.z), 1f / SIZE};
    }

    private static float[] phases(ClientLevel level) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        return Swell.phases(cam.x, cam.z, level.getGameTime() + mc.getTimer().getGameTimeDeltaPartialTick(false));
    }

    private static float surface(ClientLevel level) {
        if (Swell.calm()) {
            return -10_000; // no water there: none is lifted
        }
        return (float) (level.getSeaLevel() - 1 + SURFACE - Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y);
    }
}
