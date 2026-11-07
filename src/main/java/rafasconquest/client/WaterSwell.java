package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import rafasconquest.RafasConquest;
import rafasconquest.core.Swell;

/**
 * Draws the {@link Swell} on the sea: Minecraft's translucent terrain shader (overridden in this mod's assets) and,
 * where Sodium draws the world instead, Sodium's terrain vertex shader (patched as it loads) lift each corner of the still sea's
 * surface by the swell there, worked out from each wave's phase at the camera (sent each frame) plus how far off the
 * camera the corner is, so far from the world's middle it stays smooth. Only water at sea level moves: a source block's
 * top, with nothing above it, sits 8/9 of a block up. A shader pack (Iris) draws its own water, and this is left out.
 */
public final class WaterSwell {
    private static final String SHADER = "blocks/block_layer_opaque.vsh";
    private static final String AFTER = "vec3 position = _vert_position + translation;";
    private static final double SURFACE = 8 / 9.0;

    private WaterSwell() {
    }

    /** Sodium's shader source as loaded, with the swell added to its terrain vertex shader. */
    public static String patch(String path, String source) {
        if (!path.equals(SHADER)) {
            return source;
        }
        int at = source.indexOf(AFTER);
        int main = source.indexOf("void main()");
        if (at < 0 || main < 0) {
            RafasConquest.LOGGER.warn("[swell] Sodium's terrain shader has changed; the sea is drawn flat");
            return source;
        }
        String uniforms = "uniform float u_SwellPhase[" + Swell.waves() + "];\nuniform float u_SwellSurface;\n\n";
        String lift = "\n    if (abs(position.y - u_SwellSurface) < 0.01) { // the still sea's surface\n"
                + "        position.y += " + Swell.glsl("position.xz", "u_SwellPhase") + ";\n    }";
        int end = at + AFTER.length();
        return source.substring(0, main) + uniforms + source.substring(main, end) + lift + source.substring(end);
    }

    /**
     * Before the world is drawn: the camera's wave phases and the sea's height off it, for Minecraft's own translucent
     * terrain shader (assets/minecraft/shaders/core/rendertype_translucent.vsh).
     */
    public static void prepare() {
        ShaderInstance shader = GameRenderer.getRendertypeTranslucentShader();
        ClientLevel level = Minecraft.getInstance().level;
        if (shader == null || level == null) {
            return;
        }
        float[] p = phases(level);
        shader.safeGetUniform("SwellPhase").set(p[0], p[1], p[2]);
        shader.safeGetUniform("SwellSurface").set(surface(level));
    }

    /** The same, sent to Sodium's terrain shader just bound. */
    public static void upload() {
        ClientLevel level = Minecraft.getInstance().level;
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int phases = GL20.glGetUniformLocation(program, "u_SwellPhase");
        if (level == null || phases < 0) {
            return;
        }
        GL20.glUniform1fv(phases, phases(level));
        GL20.glUniform1f(GL20.glGetUniformLocation(program, "u_SwellSurface"), surface(level));
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
