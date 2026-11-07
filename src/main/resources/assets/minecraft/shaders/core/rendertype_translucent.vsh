#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;

// Rafa's Conquest: the sea's swell (rafasconquest.client.WaterSwell), each wave's phase at the camera, and the still
// sea's surface height off the camera.
uniform vec3 SwellPhase;
uniform float SwellSurface;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vec3 pos = Position + ChunkOffset;
    if (abs(pos.y - SwellSurface) < 0.01) { // the still sea's surface: rafasconquest.core.Swell.glsl("pos.xz", "SwellPhase")
        pos.y += 0.0 + 0.660000 * sin(dot(pos.xz, vec2(0.108036, 0.073912)) + SwellPhase[0]) + 0.360000 * sin(dot(pos.xz, vec2(0.105557, 0.207393)) + SwellPhase[1]) + 0.180000 * sin(dot(pos.xz, vec2(0.362232, 0.073428)) + SwellPhase[2]);
    }
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;
}
