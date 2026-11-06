package rafasconquest.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import rafasconquest.RafasConquest;
import rafasconquest.battle.DistantDepth;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Distant figures hidden behind Distant Horizons' hills, pixel by pixel. Distant Horizons keeps its terrain's depth in
 * a texture of its own, never in the game's, so figures drawn over its terrain show through it. Around the figures'
 * draw ({@link #begin}, {@link #end}) this builds a depth of its own: the game's depth and Distant Horizons' both turned
 * into one projection, the game's with its near and far planes moved so every figure fits at its real distance (no
 * {@link DistantDepth} squeeze) with fine depth steps out there. The figures are drawn into the game's picture against
 * that depth; the game's own depth, projection and Distant Horizons are left exactly as they were.
 *
 * <p>Switched with {@code /pvedepth}, on by default; {@code view} paints the merged depth in the bottom left
 * corner (near white, far black, sky blue) to check the reading. Off under a shader pack, whose pictures and depth
 * are its own. Render thread only.
 */
public final class DistantOcclusion {
    public enum Mode { OFF, ON, VIEW }

    /** Nothing drawn through this is nearer: every figure hands over further out than this. */
    private static final float NEAR = 4;

    private static Mode mode = Mode.ON;
    private static String reason = "not drawn yet";
    /** Between {@link #begin} and {@link #end}: figures are drawn at their real distance against the merged depth. */
    private static boolean active;
    /** The farthest figure this frame and the last; the projection's far plane covers last frame's. */
    private static double farthest, lastFarthest;
    private static float far;

    private static ShaderInstance merge, view;
    private static int framebuffer, depthTexture, sampler, width, height;
    private static final Matrix4f MC_INVERSE = new Matrix4f();
    private static final Matrix4f DH_INVERSE = new Matrix4f();
    private static final Matrix4f OURS = new Matrix4f();
    private static final Matrix4f OURS_INVERSE = new Matrix4f();

    private static Method irisInstance, irisPackInUse;

    private DistantOcclusion() {
    }

    static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "distant_depth_merge"), DefaultVertexFormat.POSITION), s -> merge = s);
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "distant_depth_view"), DefaultVertexFormat.POSITION), s -> view = s);
    }

    static Mode mode() {
        return mode;
    }

    static void setMode(Mode m) {
        mode = m;
    }

    /** Why it is or isn't working this frame, for {@code /pvedepth}. */
    static String reason() {
        return reason;
    }

    /**
     * How much to scale a figure {@code distance} blocks away toward the camera: 1 while the merged depth is in use,
     * else {@link DistantDepth}'s squeeze. Called once per figure drawn.
     */
    static double scale(double distance, double renderBlocks) {
        farthest = Math.max(farthest, distance);
        return active ? 1 : DistantDepth.drawDistance(distance, renderBlocks) / distance;
    }

    /** Just before the distant figures are drawn. */
    static void begin() {
        active = false;
        lastFarthest = farthest;
        farthest = 0;
        if (mode == Mode.OFF) {
            reason = "off";
            return;
        }
        if (lastFarthest <= 0 && mode != Mode.VIEW) {
            reason = "no figures far away last frame";
            return;
        }
        if (merge == null || view == null) {
            reason = "its shaders didn't load (see the log)";
            return;
        }
        if (shaderPack()) {
            reason = "off under a shader pack";
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();
        if (GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING) != main.frameBufferId) {
            reason = "the world isn't drawn into the main picture here (Fabulous graphics?)";
            return;
        }
        prepare(main.width, main.height);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
        GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, main.getColorTextureId(), 0);
        if (GlStateManager.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            main.bindWrite(false);
            reason = "its framebuffer isn't complete";
            return;
        }

        // The game's projection (view bobbing and all) with only its depth row changed to the new planes.
        Matrix4f base = RenderSystem.getProjectionMatrix();
        far = (float) Math.max(mc.gameRenderer.getDepthFar(), lastFarthest * 1.1 + 64);
        projection(base, NEAR, far, OURS);
        OURS_INVERSE.set(OURS).invert();
        MC_INVERSE.set(base).invert();
        float[] dh = DistantHorizonsLink.depth(DH_INVERSE);

        merge.setSampler("McDepth", main.getDepthTextureId());
        merge.setSampler("DhDepth", dh != null ? (int) dh[0] : main.getDepthTextureId());
        merge.safeGetUniform("McInverse").set(MC_INVERSE);
        merge.safeGetUniform("DhInverse").set(DH_INVERSE);
        merge.safeGetUniform("Ours").set(OURS);
        merge.safeGetUniform("DhLayout").set(dh != null ? 1 : 0, dh != null ? dh[1] : 0, dh != null ? dh[2] : 1, 0);
        RenderSystem.viewport(0, 0, main.width, main.height);
        RenderSystem.colorMask(false, false, false, false);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        RenderSystem.depthMask(true);
        fullScreen(merge, 2);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(OURS, VertexSorting.DISTANCE_TO_ORIGIN);
        active = true;
        reason = dh != null ? "on" : "on, but Distant Horizons gave no depth this frame: only the game's terrain hides figures";
    }

    /** Just after the distant figures are drawn: everything back as the game had it. */
    static void end() {
        if (!active) {
            return;
        }
        active = false;
        RenderSystem.restoreProjectionMatrix();
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        main.bindWrite(false);
        if (mode == Mode.VIEW) {
            view.setSampler("Depth", depthTexture);
            view.safeGetUniform("OursInverse").set(OURS_INVERSE);
            view.safeGetUniform("Planes").set(NEAR, far);
            RenderSystem.viewport(0, 0, main.width / 2, main.height / 2);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            fullScreen(view, 1);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.viewport(0, 0, main.width, main.height);
        }
    }

    /**
     * {@code base} with its depth row replaced by OpenGL's (-1..1) for these planes. For a projection P times an affine
     * bobbing M, the depth row is {@code -A} times the w row plus {@code B} in the last column.
     */
    static void projection(Matrix4f base, float near, float far, Matrix4f out) {
        float a = -(far + near) / (far - near);
        float b = -2 * far * near / (far - near);
        out.set(base);
        // JOML names entries m<column><row>: the depth row is m02..m32, the w row m03..m33.
        out.m02(-a * base.m03()).m12(-a * base.m13()).m22(-a * base.m23()).m32(-a * base.m33() + b);
    }

    private static void prepare(int w, int h) {
        if (framebuffer == 0) {
            framebuffer = GlStateManager.glGenFramebuffers();
            // Reads a depth texture as numbers whatever its own settings: Distant Horizons' may compare.
            sampler = GL33.glGenSamplers();
            GL33.glSamplerParameteri(sampler, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL33.glSamplerParameteri(sampler, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL33.glSamplerParameteri(sampler, GL11.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
            GL33.glSamplerParameteri(sampler, GL11.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
            GL33.glSamplerParameteri(sampler, GL14.GL_TEXTURE_COMPARE_MODE, GL11.GL_NONE);
        }
        if (w == width && h == height && depthTexture != 0) {
            return;
        }
        if (depthTexture != 0) {
            TextureUtil.releaseTextureId(depthTexture);
        }
        depthTexture = TextureUtil.generateTextureId();
        GlStateManager._bindTexture(depthTexture);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL11.GL_NONE);
        GlStateManager._texImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH_COMPONENT32F, w, h, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
        GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
        width = w;
        height = h;
    }

    /** One quad over the whole viewport through {@code shader}, its first {@code samplers} units read as plain numbers. */
    private static void fullScreen(ShaderInstance shader, int samplers) {
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        buffer.addVertex(-1, -1, 0);
        buffer.addVertex(1, -1, 0);
        buffer.addVertex(1, 1, 0);
        buffer.addVertex(-1, 1, 0);
        RenderSystem.setShader(() -> shader);
        for (int i = 0; i < samplers; i++) {
            GL33.glBindSampler(i, sampler);
        }
        BufferUploader.drawWithShader(buffer.buildOrThrow());
        for (int i = 0; i < samplers; i++) {
            GL33.glBindSampler(i, 0);
        }
    }

    /** Whether Iris draws with a shader pack (it is optional: reached by name). */
    private static boolean shaderPack() {
        if (!ModList.get().isLoaded("iris")) {
            return false;
        }
        try {
            if (irisInstance == null) {
                Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                irisInstance = api.getMethod("getInstance");
                irisPackInUse = api.getMethod("isShaderPackInUse");
            }
            return (boolean) irisPackInUse.invoke(irisInstance.invoke(null));
        } catch (ReflectiveOperationException | RuntimeException e) {
            return true; // can't tell: stay off
        }
    }
}
