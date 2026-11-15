package rafasconquest.client.map;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import rafasconquest.RafasConquest;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.map.TitleStyle;

/**
 * The grand strategy map in 3D, worked into Distant Horizons' terrain shader while it is shown: the land stands
 * {@link #STRETCH} times as tall about sea level, as a Total War campaign map's does; its natural ground (grass, dirt,
 * sand, leaves, snow, stone) is dyed in the realms' colours as Crusader Kings' is, keeping its light and shade, while
 * water, wood and metal, and every seat city's square, keep their own; and the title borders are painted on it, so hills
 * hide what lies behind them. The nation names are laid on the ground after the world is drawn ({@link #render}).
 * Distant Horizons' own heights and culling are untouched. Client thread only.
 */
public final class StrategyRelief {
    public static final double STRETCH = 2;
    /** How much of a realm's colour the land takes, times the colour's own alpha on the flat map (about 0.8). */
    private static final float DYE = 0.6f;
    /** A city's square is left undyed this much past its walls, for the houses outside them. */
    private static final double CITY_MARGIN = 1.25;
    // Texture units the shader reads the map from, past those Minecraft tracks (0-11); the swell holds 6, 14 and 15.
    private static final int TERRITORY_UNIT = 12;
    private static final int BORDERS_UNIT = 13;
    private static final int CITIES_UNIT = 16;
    /** Border widths in screen pixels, county to empire, at any zoom. */
    private static final float[] LINE_PX = {1.0f, 1.3f, 1.8f, 2.5f};
    private static final String DRAW ="gl_Position = uCombinedMatrix * vec4(vertexWorldPos, 1.0);";
    private static final String FRAG_AT = "float viewDist = length(vertexWorldPos);";

    private static boolean on;
    /** The vertex shader took its patch (Distant Horizons loads it before the fragment shader). */
    private static boolean vertexPatched;
    @org.jetbrains.annotations.Nullable
    private static LabelLayer labels;
    @org.jetbrains.annotations.Nullable
    private static MapContext map;
    private static double mapScale;
    /** The realms dyed in their colours (the coloured map toggle). */
    private static boolean dye;
    /** White, black over each seat city's square: where the land is not dyed; made once per base map. */
    @org.jetbrains.annotations.Nullable
    private static DynamicTexture cities;
    @org.jetbrains.annotations.Nullable
    private static BaseMap citiesOf;

    private StrategyRelief() {
    }

    /**
     * The 3D map is shown, at {@code mapScale} screen pixels per block in its middle; {@code labels} paints the nation
     * names on its ground, and {@code dye} says whether the realms are in their colours.
     */
    static void show(double mapScale, LabelLayer labels, @org.jetbrains.annotations.Nullable MapContext map, boolean dye) {
        on = true;
        StrategyRelief.dye = dye;
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

    /** Distant Horizons' terrain shaders, as loaded: the stretch in the vertex shader, the dye and borders in the fragment's. */
    public static String patchDh(String path, String source) {
        if (path.endsWith("terrain/gl/vert.vert")) {
            int main = source.indexOf("void main()");
            if (main < 0 || !source.contains(DRAW)) {
                RafasConquest.LOGGER.warn("[relief] Distant Horizons' terrain shader has changed; the 3D map's land is not raised");
                return source;
            }
            String head = "uniform vec2 u_RcRelief; // stretch (0: off), sea level from the camera\nflat out uint v_RcMaterial;\n\n";
            String draw = "v_RcMaterial = irisData.x;\n"
                    + "    vec3 reliefPos = vertexWorldPos;\n"
                    + "    if (u_RcRelief.x > 0.0) {\n"
                    + "        reliefPos.y = u_RcRelief.y + (reliefPos.y - u_RcRelief.y) * u_RcRelief.x;\n"
                    + "    }\n"
                    + "    gl_Position = uCombinedMatrix * vec4(reliefPos, 1.0);";
            vertexPatched = true;
            return (source.substring(0, main) + head + source.substring(main)).replace(DRAW, draw);
        }
        if (path.endsWith("terrain/gl/frag.frag")) {
            int main = source.indexOf("void main()");
            int at = source.indexOf(FRAG_AT);
            if (main < 0 || at < 0 || !vertexPatched) { // it reads the material the vertex shader passes on
                RafasConquest.LOGGER.warn("[relief] Distant Horizons' terrain shader has changed; the 3D map's land is not dyed");
                return source;
            }
            String head = "uniform vec2 u_RcTint; // dye strength (0: none), borders (0: none)\n"
                    + "uniform vec4 u_RcMapAt; // the map's corner from the camera (x, z), and 1 / its size in blocks\n"
                    + "uniform sampler2D u_RcTerritory;\nuniform sampler2D u_RcBorders;\nuniform sampler2D u_RcCities;\n"
                    + "flat in uint v_RcMaterial;\n"
                    // A border line of a level (1 county .. 4 empire) a distance in screen pixels from the fragment.
                    + "vec4 rcLine(float v, float px) {\n"
                    + "    int l = int(floor(v * 255.0 / 64.0 + 0.5));\n"
                    + "    if (l <= 0) return vec4(0.0);\n"
                    + "    vec4 c = l == 1 ? " + vec4(TitleStyle.COUNTY) + " : l == 2 ? " + vec4(TitleStyle.DUCHY)
                    + " : l == 3 ? " + vec4(TitleStyle.KINGDOM) + " : " + vec4(TitleStyle.EMPIRE) + ";\n"
                    + "    float w = l == 1 ? " + LINE_PX[0] + " : l == 2 ? " + LINE_PX[1] + " : l == 3 ? " + LINE_PX[2] + " : " + LINE_PX[3] + ";\n"
                    + "    return vec4(c.rgb, c.a * clamp(w * 0.5 + 0.5 - px, 0.0, 1.0));\n"
                    + "}\n"
                    + "vec4 rcStronger(vec4 a, vec4 b) { return b.a > a.a ? b : a; }\n\n";
            // DH's materials: 1 leaves, 2 stone, 5 dirt, 7 deepslate, 8 snow, 9 sand, 10 terracotta, 13 grass.
            String body = "if (u_RcTint.x > 0.0 || u_RcTint.y > 0.0) {\n"
                    + "        vec2 rcUv = (vertexWorldPos.xz - u_RcMapAt.xy) * u_RcMapAt.zw;\n"
                    + "        ivec2 rcSize = textureSize(u_RcBorders, 0);\n"
                    + "        vec2 rcT = rcUv * vec2(rcSize);\n"
                    + "        vec2 rcPx = max(fwidth(rcT), vec2(1e-6)); // map pixels per screen pixel\n"
                    + "        if (all(greaterThanEqual(rcUv, vec2(0.0))) && all(lessThanEqual(rcUv, vec2(1.0)))) {\n"
                    + "            uint rcM = v_RcMaterial;\n"
                    + "            bool rcNatural = rcM == 1u || rcM == 2u || rcM == 5u || rcM == 7u || rcM == 8u || rcM == 9u || rcM == 10u || rcM == 13u;\n"
                    + "            if (u_RcTint.x > 0.0 && rcNatural) {\n"
                    + "                vec4 realm = texture(u_RcTerritory, rcUv);\n"
                    + "                float lum = dot(fragColor.rgb, vec3(0.299, 0.587, 0.114));\n"
                    + "                vec3 dyed = clamp(realm.rgb * (0.35 + lum * 1.3), 0.0, 1.0); // the realm's colour, in the land's light and shade\n"
                    + "                fragColor.rgb = mix(fragColor.rgb, dyed, realm.a * u_RcTint.x * texture(u_RcCities, rcUv).r);\n"
                    + "            }\n"
                    + "            if (u_RcTint.y > 0.0) {\n"
                    // Thin lines on the pixel edges: this pixel's right and bottom, its left and upper neighbours'.
                    + "                ivec2 hi = rcSize - 1;\n"
                    + "                ivec2 c = clamp(ivec2(floor(rcT)), ivec2(0), hi);\n"
                    + "                vec2 f = clamp(rcT - vec2(c), 0.0, 1.0);\n"
                    + "                vec4 e = texelFetch(u_RcBorders, c, 0);\n"
                    + "                vec4 border = rcStronger(rcLine(e.r, (1.0 - f.x) / rcPx.x), rcLine(e.g, (1.0 - f.y) / rcPx.y));\n"
                    + "                if (c.x > 0) border = rcStronger(border, rcLine(texelFetch(u_RcBorders, c - ivec2(1, 0), 0).r, f.x / rcPx.x));\n"
                    + "                if (c.y > 0) border = rcStronger(border, rcLine(texelFetch(u_RcBorders, c - ivec2(0, 1), 0).g, f.y / rcPx.y));\n"
                    + "                fragColor.rgb = mix(fragColor.rgb, border.rgb, border.a);\n"
                    + "            }\n"
                    + "        }\n"
                    + "    }\n    ";
            return source.substring(0, main) + head + source.substring(main, at) + body + source.substring(at);
        }
        return source;
    }

    /** The stretch, dye and borders, sent to Distant Horizons' terrain shader just bound (every frame, so they clear when off). */
    public static void upload() {
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int relief = GL20.glGetUniformLocation(program, "u_RcRelief");
        if (relief < 0) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        net.minecraft.world.phys.Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        GL20.glUniform2f(relief, on ? (float) STRETCH : 0f, (float) (sea() - cam.y));
        MapContext context = map;
        boolean painted = on && context != null;
        GL20.glUniform2f(GL20.glGetUniformLocation(program, "u_RcTint"), painted && dye ? DYE : 0f, painted ? 1f : 0f);
        if (!painted) {
            return;
        }
        BaseMap base = context.base;
        GL20.glUniform4f(GL20.glGetUniformLocation(program, "u_RcMapAt"), (float) (base.minX() - cam.x), (float) (base.minZ() - cam.z),
                1f / (base.blocksPerPixel() * base.width()), 1f / (base.blocksPerPixel() * base.height()));
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "u_RcTerritory"), TERRITORY_UNIT);
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "u_RcBorders"), BORDERS_UNIT);
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "u_RcCities"), CITIES_UNIT);
        // Raw GL: Minecraft's texture state tracks only units 0-11; the unit it thinks is active is put back after.
        int active = GlStateManager._getActiveTexture();
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + TERRITORY_UNIT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getTextureManager().getTexture(MapContext.TERRITORY_TEXTURE).getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + BORDERS_UNIT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getTextureManager().getTexture(MapContext.EDGES_TEXTURE).getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + CITIES_UNIT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, cities(base).getId());
        GL13.glActiveTexture(active);
    }

    /** An ARGB colour as a GLSL vec4. */
    private static String vec4(int argb) {
        return String.format(java.util.Locale.ROOT, "vec4(%.4f, %.4f, %.4f, %.4f)", (argb >> 16 & 0xFF) / 255f,
                (argb >> 8 & 0xFF) / 255f, (argb & 0xFF) / 255f, (argb >>> 24) / 255f);
    }

    /** The cities mask for this base map, made the first time it is asked for. */
    private static DynamicTexture cities(BaseMap base) {
        if (cities == null || citiesOf != base) {
            if (cities != null) {
                cities.close();
            }
            NativeImage image = new NativeImage(base.width(), base.height(), false);
            image.fillRect(0, 0, base.width(), base.height(), 0xFFFFFFFF);
            for (SeatCities.City c : base.cities()) {
                double half = c.size() * CITY_MARGIN / 2;
                int x0 = Math.max(0, (int) Math.floor((c.x() - half - base.minX()) / base.blocksPerPixel()));
                int z0 = Math.max(0, (int) Math.floor((c.z() - half - base.minZ()) / base.blocksPerPixel()));
                int x1 = Math.min(base.width() - 1, (int) Math.ceil((c.x() + half - base.minX()) / base.blocksPerPixel()));
                int z1 = Math.min(base.height() - 1, (int) Math.ceil((c.z() + half - base.minZ()) / base.blocksPerPixel()));
                if (x1 >= x0 && z1 >= z0) {
                    image.fillRect(x0, z0, x1 - x0 + 1, z1 - z0 + 1, 0xFF000000);
                }
            }
            cities = new DynamicTexture(image);
            cities.setFilter(true, false); // the dye fades in at a city's edge
            citiesOf = base;
        }
        return cities;
    }

    /** The nation names laid on the 3D map's ground, after the world is drawn. */
    public static void render(PoseStack pose, Camera camera) {
        if (!on || labels == null || map == null || mapScale <= 0) {
            return;
        }
        // Minecraft's far plane is a few hundred blocks (its render distance); the camera here is thousands up.
        Matrix4f projection = new Matrix4f(RenderSystem.getProjectionMatrix());
        com.mojang.blaze3d.vertex.VertexSorting sorting = RenderSystem.getVertexSorting();
        Minecraft mc = Minecraft.getInstance();
        float aspect = (float) mc.getWindow().getWidth() / Math.max(1, mc.getWindow().getHeight());
        RenderSystem.setProjectionMatrix(new Matrix4f().setPerspective((float) Math.toRadians(mc.options.fov().get()), aspect, 4f, 200_000f),
                com.mojang.blaze3d.vertex.VertexSorting.DISTANCE_TO_ORIGIN);
        net.minecraft.client.renderer.MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        labels.renderGround(pose, camera.getPosition(), map, mapScale, buffers);
        buffers.endBatch(); // under the far plane set above
        RenderSystem.setProjectionMatrix(projection, sorting);
    }
}
