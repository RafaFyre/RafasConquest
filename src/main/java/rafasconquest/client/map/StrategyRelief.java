package rafasconquest.client.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import rafasconquest.RafasConquest;
import rafasconquest.map.BaseMap;

/**
 * The grand strategy map in 3D: while it is shown, Distant Horizons' terrain stands {@link #STRETCH} times as tall about
 * sea level, as a Total War campaign map's does (a line added to its vertex shader, so its own heights and culling are
 * untouched), and the land's borders are laid over it, a mesh following the base map's ground. Client thread only.
 */
public final class StrategyRelief {
    public static final double STRETCH = 2;
    private static final String DRAW = "gl_Position = uCombinedMatrix * vec4(vertexWorldPos, 1.0);";
    /** Most cells a side of the border mesh; coarser cells far out keep it to one cheap draw. */
    private static final int MAX_CELLS = 160;

    private static boolean on;
    private static double centreX;
    private static double centreZ;
    private static double radius;
    @org.jetbrains.annotations.Nullable
    private static LabelLayer labels;
    @org.jetbrains.annotations.Nullable
    private static MapContext map;
    private static double mapScale;
    /** The realms laid over the land in their colours (the coloured map toggle). */
    private static boolean dye;

    private StrategyRelief() {
    }

    /**
     * The 3D map is shown about (x, z), seeing about {@code radius} blocks out at {@code mapScale} screen pixels per
     * block in the middle; {@code labels} paints the nation names on its ground.
     */
    static void show(double x, double z, double radius, double mapScale, LabelLayer labels, @org.jetbrains.annotations.Nullable MapContext map,
                     boolean dye) {
        on = true;
        StrategyRelief.dye = dye;
        centreX = x;
        centreZ = z;
        StrategyRelief.radius = radius;
        StrategyRelief.mapScale = mapScale;
        StrategyRelief.labels = labels;
        StrategyRelief.map = map;
    }

    static void hide() {
        on = false;
        labels = null;
        map = null;
    }

    private static double sea() {
        return Minecraft.getInstance().level == null ? 63 : Minecraft.getInstance().level.getSeaLevel();
    }

    /** A height as the 3D map shows it. */
    static double lift(double y) {
        double sea = sea();
        return on ? sea + (y - sea) * STRETCH : y;
    }

    /** The ground at (x, z) as the 3D map shows it: the base map's, sea level over water. */
    static double groundAt(BaseMap base, double x, double z) {
        int i = base.pixelAt(x, z);
        double y = i < 0 || base.wet()[i] ? sea() : Math.max(sea(), base.ground()[i]);
        return lift(y);
    }

    /** Distant Horizons' terrain vertex shader, as loaded: its heights stretched about sea level while {@link #on}. */
    public static String patchDh(String path, String source) {
        if (!path.endsWith("terrain/gl/vert.vert")) {
            return source;
        }
        int main = source.indexOf("void main()");
        if (main < 0 || !source.contains(DRAW)) {
            RafasConquest.LOGGER.warn("[relief] Distant Horizons' terrain shader has changed; the 3D map's land is not raised");
            return source;
        }
        String head = "uniform vec2 u_RcRelief; // stretch (0: off), sea level from the camera\n\n";
        String draw = "vec3 reliefPos = vertexWorldPos;\n"
                + "    if (u_RcRelief.x > 0.0) {\n"
                + "        reliefPos.y = u_RcRelief.y + (reliefPos.y - u_RcRelief.y) * u_RcRelief.x;\n"
                + "    }\n"
                + "    gl_Position = uCombinedMatrix * vec4(reliefPos, 1.0);";
        return (source.substring(0, main) + head + source.substring(main)).replace(DRAW, draw);
    }

    /** The stretch, sent to Distant Horizons' terrain shader just bound (every frame, so it is cleared when off). */
    public static void upload() {
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int at = GL20.glGetUniformLocation(program, "u_RcRelief");
        if (at >= 0) {
            double camY = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y;
            GL20.glUniform2f(at, on ? (float) STRETCH : 0f, (float) (sea() - camY));
        }
    }

    /** The land's borders laid over the 3D map's ground, after the world is drawn. */
    public static void render(PoseStack pose, Camera camera) {
        BaseMap base = ClientMapCache.map();
        if (!on || base == null) {
            return;
        }
        int bpp = base.blocksPerPixel();
        int step = Math.max(1, (int) Math.ceil(2 * radius / bpp / MAX_CELLS)) * bpp;
        int x0 = (int) Math.floor((centreX - radius - base.minX()) / step) * step + base.minX();
        int z0 = (int) Math.floor((centreZ - radius - base.minZ()) / step) * step + base.minZ();
        int cells = (int) Math.ceil(2 * radius / step) + 1;
        Vec3 cam = camera.getPosition();
        Matrix4f m = pose.last().pose();
        // Minecraft's far plane is a few hundred blocks (its render distance); the camera here is thousands up.
        Matrix4f projection = new Matrix4f(RenderSystem.getProjectionMatrix());
        com.mojang.blaze3d.vertex.VertexSorting sorting = RenderSystem.getVertexSorting();
        Minecraft mc = Minecraft.getInstance();
        float aspect = (float) mc.getWindow().getWidth() / Math.max(1, mc.getWindow().getHeight());
        RenderSystem.setProjectionMatrix(new Matrix4f().setPerspective((float) Math.toRadians(mc.options.fov().get()), aspect, 4f, 200_000f),
                com.mojang.blaze3d.vertex.VertexSorting.DISTANCE_TO_ORIGIN);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        // shortcut: no depth test (Distant Horizons' terrain isn't in Minecraft's depth); a border behind a ridge shows through it.
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        if (dye) {
            drape(base, cam, m, MapContext.TERRITORY_TEXTURE, x0, z0, cells, step); // the realms in their colours, as CK3's map
        }
        drape(base, cam, m, MapContext.TITLES_TEXTURE, x0, z0, cells, step);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        if (labels != null && map != null && mapScale > 0) {
            net.minecraft.client.renderer.MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            labels.renderGround(pose, cam, map, mapScale, buffers);
            buffers.endBatch(); // under the far plane set above
        }
        RenderSystem.setProjectionMatrix(projection, sorting);
    }

    /** One of the flat map's textures laid over the ground: a {@code cells}-square mesh of {@code step}-block cells from (x0, z0). */
    private static void drape(BaseMap base, Vec3 cam, Matrix4f m, net.minecraft.resources.ResourceLocation texture, int x0, int z0, int cells, int step) {
        double wide = (double) base.blocksPerPixel() * base.width();
        double tall = (double) base.blocksPerPixel() * base.height();
        // One row of heights ahead, so each corner is worked out once.
        float[] north = new float[cells + 1];
        float[] south = new float[cells + 1];
        for (int i = 0; i <= cells; i++) {
            north[i] = height(base, x0 + i * step, z0, cam);
        }
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder b = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        for (int j = 0; j < cells; j++) {
            int za = z0 + j * step;
            int zb = za + step;
            for (int i = 0; i <= cells; i++) {
                south[i] = height(base, x0 + i * step, zb, cam);
            }
            for (int i = 0; i < cells; i++) {
                int xa = x0 + i * step;
                int xb = xa + step;
                float ua = (float) ((xa - base.minX()) / wide);
                float ub = (float) ((xb - base.minX()) / wide);
                float va = (float) ((za - base.minZ()) / tall);
                float vb = (float) ((zb - base.minZ()) / tall);
                b.addVertex(m, (float) (xa - cam.x), north[i], (float) (za - cam.z)).setUv(ua, va);
                b.addVertex(m, (float) (xa - cam.x), south[i], (float) (zb - cam.z)).setUv(ua, vb);
                b.addVertex(m, (float) (xb - cam.x), south[i + 1], (float) (zb - cam.z)).setUv(ub, vb);
                b.addVertex(m, (float) (xb - cam.x), north[i + 1], (float) (za - cam.z)).setUv(ub, va);
            }
            float[] t = north;
            north = south;
            south = t;
        }
        BufferUploader.drawWithShader(b.buildOrThrow());
    }

    /** A mesh corner's height from the camera: a little over the ground, so the borders lie on it. */
    private static float height(BaseMap base, double x, double z, Vec3 cam) {
        return (float) (groundAt(base, x, z) + 2 * STRETCH - cam.y);
    }
}
