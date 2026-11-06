package rafasconquest.client;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistantOcclusionTest {
    private static final float FOV = (float) Math.toRadians(70);

    @Test
    void depthRowMatchesAPerspectiveWithTheNewPlanes() {
        Matrix4f out = new Matrix4f();
        DistantOcclusion.projection(new Matrix4f().perspective(FOV, 1.6F, 0.05F, 1024), 4, 5000, out);
        Matrix4f expected = new Matrix4f().perspective(FOV, 1.6F, 4, 5000);
        for (int i = 0; i < 16; i++) {
            assertEquals(expected.get(i / 4, i % 4), out.get(i / 4, i % 4), 1e-4F, "entry " + i);
        }
    }

    @Test
    void bobbedProjectionKeepsScreenPositionAndPutsPlanesAtTheEnds() {
        Matrix4f bob = new Matrix4f().rotateZ(0.05F).rotateX(0.03F).translate(0.02F, -0.04F, 0);
        Matrix4f base = new Matrix4f().perspective(FOV, 1.6F, 0.05F, 1024).mul(bob);
        Matrix4f out = new Matrix4f();
        DistantOcclusion.projection(base, 4, 5000, out);
        Vector4f point = new Vector4f(30, 12, -800, 1);
        Vector4f a = base.transform(new Vector4f(point));
        Vector4f b = out.transform(new Vector4f(point));
        assertEquals(a.x / a.w, b.x / b.w, 1e-5F);
        assertEquals(a.y / a.w, b.y / b.w, 1e-5F);
        // The bobbed view's own distance along its axis: the near plane maps to -1, the far one to 1.
        Vector4f near = bob.invert(new Matrix4f()).transform(new Vector4f(0, 0, -4, 1));
        Vector4f far = bob.invert(new Matrix4f()).transform(new Vector4f(0, 0, -5000, 1));
        Vector4f n = out.transform(near);
        Vector4f f = out.transform(far);
        assertEquals(-1, n.z / n.w, 1e-4F);
        assertEquals(1, f.z / f.w, 1e-3F);
    }
}
