#version 150

// The merged depth on screen: near white to black at the far plane, on a log scale so a block away and the horizon
// both read; the sky dark blue.

uniform sampler2D Depth;
uniform mat4 OursInverse;
uniform vec2 Planes;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    float d = texture(Depth, texCoord).r;
    if (d >= 1.0) {
        fragColor = vec4(0.08, 0.12, 0.32, 1.0);
        return;
    }
    vec4 p = OursInverse * vec4(texCoord * 2.0 - 1.0, d * 2.0 - 1.0, 1.0);
    float distance = length(p.xyz / p.w);
    float shade = 1.0 - log(max(distance, Planes.x) / Planes.x) / log(Planes.y / Planes.x);
    fragColor = vec4(vec3(clamp(shade, 0.0, 1.0)), 1.0);
}
