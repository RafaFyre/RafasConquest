package rafasconquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwellTest {
    @Test
    void risesAndSlopesMatchTheHeights() {
        double x = 123.4;
        double z = -56.7;
        long t = 98765;
        // A tick on, the sea stands higher by about its rise for a twentieth of a second.
        assertEquals(Swell.rise(x, z, t) / 20, Swell.height(x, z, t + 1) - Swell.height(x, z, t), 0.002);
        double[] slope = Swell.slope(x, z, t);
        assertEquals(slope[0], (Swell.height(x + 0.01, z, t) - Swell.height(x - 0.01, z, t)) / 0.02, 1e-4);
        assertEquals(slope[1], (Swell.height(x, z + 0.01, t) - Swell.height(x, z - 0.01, t)) / 0.02, 1e-4);
    }

    @Test
    void theShaderSeesTheSameSeaFromTheCamera() {
        // Far out and late on, the camera's phases still give the sea a few dozen blocks off it, as the ships ride it.
        double camX = 25_000.3;
        double camZ = -18_000.7;
        long t = 5_000_000;
        float[] phases = Swell.phases(camX, camZ, t);
        for (int dx = -40; dx <= 40; dx += 8) {
            assertEquals(Swell.height(camX + dx, camZ - dx / 2.0, t), Swell.heightOff(phases, dx, -dx / 2.0), 1e-4);
        }
        assertTrue(Swell.glsl("p", "u").contains("sin(dot(p, vec2("));
    }

    @Test
    void minecraftsWaterShaderDrawsTheseWaves() throws java.io.IOException {
        // The shader file is written out by hand from glsl(); change the waves, paste the new sum in there too.
        java.nio.file.Path project = java.nio.file.Path.of("").toAbsolutePath();
        while (!java.nio.file.Files.isDirectory(project.resolve("src/main/resources"))) {
            project = project.getParent(); // tests may run in the run folder
        }
        String shader = java.nio.file.Files.readString(project.resolve(
                "src/main/resources/assets/minecraft/shaders/core/rendertype_translucent.vsh"));
        assertTrue(shader.contains(Swell.glsl("pos.xz", "SwellPhase")));
        assertEquals(3, Swell.waves()); // SwellPhase is a vec3 there
    }

    @Test
    void staysGentle() {
        for (int i = 0; i < 2000; i++) {
            double[] s = Swell.slope(i * 7.3, i * -3.1, i * 37L);
            assertTrue(Math.abs(Swell.height(i * 7.3, i * -3.1, i * 37L)) <= 1.2);
            assertTrue(Math.hypot(s[0], s[1]) < 0.25); // under fourteen degrees of heel
        }
    }
}
