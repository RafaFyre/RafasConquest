#version 150

// The game's depth and Distant Horizons' both turned into one projection (Ours): back to view space through each
// one's own inverse projection, forward through Ours; the nearer of the two is kept. See DistantOcclusion.

uniform sampler2D McDepth;
uniform sampler2D DhDepth;
uniform mat4 McInverse;
uniform mat4 DhInverse;
uniform mat4 Ours;
// x: 1 when Distant Horizons' depth is read, y: 1 when it is -1..1 (else 0..1), z: its empty value.
uniform vec4 DhLayout;

in vec2 texCoord;

out vec4 fragColor;

vec3 viewAt(mat4 inverse, float ndcZ) {
    vec4 p = inverse * vec4(texCoord * 2.0 - 1.0, ndcZ, 1.0);
    return p.xyz / p.w;
}

float ours(vec3 view) {
    vec4 clip = Ours * vec4(view, 1.0);
    if (clip.w <= 0.0) return 1.0; // behind the camera: hides nothing
    return clamp(clip.z / clip.w * 0.5 + 0.5, 0.0, 1.0);
}

void main() {
    float depth = 1.0;
    float mc = texture(McDepth, texCoord).r;
    if (mc < 1.0) depth = ours(viewAt(McInverse, mc * 2.0 - 1.0));
    if (DhLayout.x > 0.5) {
        float dh = texture(DhDepth, texCoord).r;
        if (dh != DhLayout.z) depth = min(depth, ours(viewAt(DhInverse, DhLayout.y > 0.5 ? dh * 2.0 - 1.0 : dh)));
    }
    gl_FragDepth = depth;
    fragColor = vec4(0.0);
}
