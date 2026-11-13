package rafasconquest.core;

import org.junit.jupiter.api.Test;
import rafasconquest.client.WaterSwell;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwellTest {
    private static final SeaCalm OPEN_SEA = new SeaCalm((x, z) -> SeaCalm.DEEP);

    @Test
    void aShipFloatsOnTheSeaAndLiesAlongIt() {
        Swell.Ride ride = new Swell.Ride();
        double off = 0;
        double steepest = 0;
        int n = 2000;
        for (long t = 0; t < n; t++) {
            ride.tick(300.5, -120.25, 0.6, 0.8, 1, 1, t, OPEN_SEA);
            if (t >= 100) { // settled
                off += Math.abs(ride.offset() - Swell.height(300.5, -120.25, t, OPEN_SEA)) / (n - 100);
                steepest = Math.max(steepest, Math.hypot(ride.slope()[0], ride.slope()[1]));
            }
        }
        assertTrue(off < 0.5, "waterline off the sea by " + off);
        double most = Math.hypot(Math.tan(Swell.Ride.MAX_ROLL), Math.tan(Swell.Ride.MAX_PITCH));
        assertTrue(steepest > 0.01 && steepest <= most + 1e-9, "steepest slope " + steepest);
    }

    @Test
    void aBigShipRidesSteadierThanASmallOne() {
        // The same sea under a 41-long ship and an 11-long one: the big one heaves, rolls and pitches less.
        double[] small = motion(5.5, 2.5);
        double[] big = motion(20.5, 6.5);
        for (int i = 0; i < 3; i++) {
            assertTrue(big[i] < small[i], "motion " + i + ": big " + big[i] + " vs small " + small[i]);
        }
    }

    /** How much a ship of this size moves, settled: spread of its waterline, mean |roll slope|, mean |pitch slope|. */
    private static double[] motion(double halfLength, double halfBeam) {
        Swell.Ride ride = new Swell.Ride();
        double sum = 0, sq = 0, roll = 0, pitch = 0;
        int n = 0;
        for (long t = 0; t < 4000; t++) {
            ride.tick(-512.5, 77.25, 0.6, 0.8, halfLength, halfBeam, t, OPEN_SEA);
            if (t >= 100) {
                sum += ride.offset();
                sq += ride.offset() * ride.offset();
                roll += Math.abs(ride.port());
                pitch += Math.abs(ride.fore());
                n++;
            }
        }
        double mean = sum / n;
        return new double[] {Math.sqrt(sq / n - mean * mean), roll / n, pitch / n};
    }

    @Test
    void theShaderSeesTheSameSeaFromTheCamera() {
        // Far out and late on, the camera's phases (and the shader's float maths) still give the sea the ships ride.
        double camX = 25_000.3;
        double camZ = -18_000.7;
        long t = 5_000_000;
        float[] phases = Swell.phases(camX, camZ, t);
        for (int dx = -200; dx <= 200; dx += 8) {
            assertEquals(Swell.height(camX + dx, camZ - dx / 2.0, t), Swell.tall() * Swell.heightOff(phases, dx, -dx / 2.0), 0.01);
        }
    }

    @Test
    void minecraftsWaterShaderDrawsTheseWaves() throws java.io.IOException {
        // The shader files are written out from glsl(), lift(), foamGlsl() and foamMix(); change those, paste them in there too.
        java.nio.file.Path core = resources().resolve("assets/minecraft/shaders/core");
        String vertex = java.nio.file.Files.readString(core.resolve("rendertype_translucent.vsh"));
        assertTrue(vertex.contains(Swell.glsl()));
        assertTrue(vertex.contains(WaterSwell.lift("pos", "", "swellAt")));
        String fragment = java.nio.file.Files.readString(core.resolve("rendertype_translucent.fsh"));
        assertTrue(fragment.contains(Swell.glsl()));
        assertTrue(fragment.contains(WaterSwell.foamGlsl()));
        assertTrue(fragment.contains(WaterSwell.foamMix("color", "", "swellAt", "swellLight")));
        assertEquals(48, Swell.DETAIL); // the shaders take them as three mat4s
    }

    static java.nio.file.Path resources() {
        java.nio.file.Path project = java.nio.file.Path.of("").toAbsolutePath();
        while (!java.nio.file.Files.isDirectory(project.resolve("src/main/resources"))) {
            project = project.getParent(); // tests may run in the run folder
        }
        return project.resolve("src/main/resources");
    }

    @Test
    void sodiumsShadersGetTheSwellAndTheFoam() {
        // Sodium's terrain shaders, cut down to the lines the patch hooks onto.
        String vertex = "void main() {\n    _vert_init();\n    vec3 position = _vert_position + translation;\n    gl_Position = vec4(position, 1.0);\n}";
        String fragment = "void main() {\n    vec4 color = v_Color;\n    fragColor = _linearFog(color, v_FragDistance, u_FogColor, u_FogStart, u_FogEnd);\n}";
        String v = WaterSwell.patch("blocks/block_layer_opaque.vsh", vertex);
        String f = WaterSwell.patch("blocks/block_layer_opaque.fsh", fragment);
        assertTrue(v.indexOf("float swell(") < v.indexOf("void main()"));
        assertTrue(v.indexOf("v_SwellAt = vec4(position.xz") > v.indexOf("vec3 position ="));
        assertTrue(f.indexOf("float whitecap(") < f.indexOf("void main()"));
        assertTrue(f.indexOf("whitecap(v_SwellAt, u_SwellPhaseA") < f.indexOf("fragColor = _linearFog("));
        assertEquals("other", WaterSwell.patch("blocks/other.vsh", "other"));
    }

    @Test
    void openSeaStandsAsTallAsPhysicsModsAndRisesInStorms() {
        try {
            assertEquals(9.1, Swell.tall(), 1e-9); // clear
            int n = 40_000;
            double[] h = new double[n];
            java.util.Random random = new java.util.Random(7);
            for (int i = 0; i < n; i++) {
                h[i] = Swell.height(random.nextDouble() * 20_000, random.nextDouble() * 20_000, random.nextInt(1_000_000));
            }
            Arrays.sort(h);
            assertTrue(h[n - 1] <= 9.1 / 2 + 1e-6); // the mean of the shapes is at most 1, less a half
            assertTrue(h[0] >= 9.1 * (Math.exp(-2) - 0.5) - 1e-6); // and at least e^-2
            assertTrue(h[(int) (n * 0.995)] - h[(int) (n * 0.005)] > 3, "span " + (h[(int) (n * 0.995)] - h[(int) (n * 0.005)]));
            Swell.weather(1, 1);
            assertEquals(14.3, Swell.tall(), 1e-9);
            Swell.weather(1, 0);
            assertEquals(13, Swell.tall(), 1e-9);
        } finally {
            Swell.weather(0, 0);
        }
    }

    @Test
    void theCalmSeaLiesStill() {
        Swell.calm(true);
        try {
            assertEquals(0, Swell.height(10, 20, 300, OPEN_SEA));
            Swell.Ride ride = new Swell.Ride();
            for (long t = 0; t < 50; t++) {
                ride.tick(10, 20, 1, 0, 20, 6, t, OPEN_SEA);
            }
            assertEquals(0, ride.offset());
            assertEquals(0, ride.slope()[0], 1e-12);
            assertEquals(0, ride.slope()[1], 1e-12);
        } finally {
            Swell.calm(false);
        }
    }

    @Test
    void wavesDieOutTowardsLandAndOverShallows() {
        // Land for x < 0, then a shallow shelf 3 deep out to x = 20, then deep sea.
        SeaCalm calm = new SeaCalm((x, z) -> x < 0 ? 0 : x < 20 ? 3 : SeaCalm.DEEP);
        assertEquals(0, calm.column(0, 5), 1e-9); // right by the beach: flat
        assertEquals(7 / 255.0, calm.column(10, 5), 1e-9); // over the shelf (calm 30): all but flat
        // The shelf's last column (x = 19) is calm 30, so the deep sea's most (calm 1) starts 29 blocks past it.
        assertEquals(239 / 255.0, calm.column(19 + 29, 5), 1e-9);
        assertEquals(231 / 255.0, calm.column(19 + 28, 5), 1e-9);
        assertEquals(0, calm.column(-5, 5), 1e-9);
        // Waves fade evenly: no step of more than 8/255 from column to column.
        for (int x = -3; x < 60; x++) {
            assertTrue(Math.abs(calm.column(x + 1, 5) - calm.column(x, 5)) <= 8 / 255.0 + 1e-9);
        }
        // Half the sea's height over water 17 deep.
        assertEquals(0.47, new SeaCalm((x, z) -> 17).column(0, 0), 0.01);
    }

    @Test
    void seaNotYetLoadedIsNeverRemembered() {
        // Unloaded at first (open sea for now), then loaded with land just west: the calm shows up at once, not half a minute on.
        boolean[] loaded = {false};
        SeaCalm calm = new SeaCalm((x, z) -> !loaded[0] ? SeaCalm.UNKNOWN : x < 0 ? 0 : SeaCalm.DEEP).at(1000);
        assertEquals(239 / 255.0, calm.column(5, 5), 1e-9);
        assertEquals(239 / 255.0, calm.strength(5.5, 5.5), 1e-9);
        loaded[0] = true;
        calm.at(1001);
        assertEquals(31 / 255.0, calm.column(5, 5), 1e-9); // 6 blocks out: calm 27
        // Once seen it is remembered as usual.
        loaded[0] = false;
        assertEquals(31 / 255.0, calm.column(5, 5), 1e-9);
    }

    @Test
    void theShadersSquareMatchesTheShipsColumns() {
        // An island and a pier: the whole-square sweep (client texture) and the one-column look (ships) agree.
        SeaCalm calm = new SeaCalm((x, z) -> (x - 40) * (x - 40) + (z - 30) * (z - 30) < 100 || (x == 70 && z > 10 && z < 40)
                ? 0 : Math.abs(x - 50) < 30 ? 6 : SeaCalm.DEEP);
        int size = 96;
        byte[] square = calm.square(0, 0, size);
        for (int z = 0; z < size; z += 3) {
            for (int x = 0; x < size; x += 3) {
                assertEquals(calm.column(x, z), (square[z * size + x] & 0xff) / 255.0, 0.003, "at " + x + ", " + z);
            }
        }
    }
}
