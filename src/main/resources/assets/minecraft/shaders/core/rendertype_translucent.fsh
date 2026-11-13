#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

// Rafa's Conquest: foam and shading on the sea's swell (rafasconquest.client.WaterSwell): each wave's phase at the
// camera, how tall the sea stands, the camera's place wrapped for the foam noise with the time in seconds, and the foam.
uniform mat4 SwellPhaseA;
uniform mat4 SwellPhaseB;
uniform mat4 SwellPhaseC;
uniform float SwellTall;
uniform vec3 SwellFoamAt;
uniform sampler3D SwellFoam;
// where the hull mask's corner is off the camera, and the mask: 1 over a Sable ship's hull (WaterSwell.hulls)
uniform vec3 SwellHullAt;
uniform sampler2D SwellHull;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 swellAt;
in vec3 swellLight;

out vec4 fragColor;

// rafasconquest.core.Swell.glsl()
float swell(vec2 q, mat4 pa, mat4 pb, mat4 pc) {
    float h = 0.0;
    float x;
    float s;
    x = dot(q, vec2(0.00000000, 0.21000000)) + pa[0][0]; s = exp(sin(x) - 1.0); h += 1.00000000 * s; q -= vec2(0.00000000, 1.37142857) * (s * cos(x));
    x = dot(q, vec2(-0.13296277, 0.20910701)) + pa[0][1]; s = exp(sin(x) - 1.0); h += 0.80000000 * s; q -= vec2(-0.58869714, 0.92582834) * (s * cos(x));
    x = dot(q, vec2(-0.26479474, 0.12403164)) + pa[0][2]; s = exp(sin(x) - 1.0); h += 0.64000000 * s; q -= vec2(-0.79483907, 0.37230797) * (s * cos(x));
    x = dot(q, vec2(-0.34220012, -0.04415217)) + pa[0][3]; s = exp(sin(x) - 1.0); h += 0.51200000 * s; q -= vec2(-0.69639877, -0.08985245) * (s * cos(x));
    x = dot(q, vec2(-0.31278976, -0.26063050)) + pa[1][0]; s = exp(sin(x) - 1.0); h += 0.40960000 * s; q -= vec2(-0.43155718, -0.35959285) * (s * cos(x));
    x = dot(q, vec2(-0.14643990, -0.45756694)) + pa[1][1]; s = exp(sin(x) - 1.0); h += 0.32768000 * s; q -= vec2(-0.13697876, -0.42800460) * (s * cos(x));
    x = dot(q, vec2(0.14389408, -0.54834052)) + pa[1][2]; s = exp(sin(x) - 1.0); h += 0.26214400 * s; q -= vec2(0.09125249, -0.34773797) * (s * cos(x));
    x = dot(q, vec2(0.49046731, -0.45490139)) + pa[1][3]; s = exp(sin(x) - 1.0); h += 0.20971520 * s; q -= vec2(0.21087244, -0.19558116) * (s * cos(x));
    x = dot(q, vec2(0.77640525, -0.14242466)) + pa[2][0]; s = exp(sin(x) - 1.0); h += 0.16777216 * s; q -= vec2(0.22631128, -0.04151480) * (s * cos(x));
    x = dot(q, vec2(0.86328075, 0.34976665)) + pa[2][1]; s = exp(sin(x) - 1.0); h += 0.13421773 * s; q -= vec2(0.17059951, 0.06912006) * (s * cos(x));
    x = dot(q, vec2(0.63815293, 0.89487076)) + pa[2][2]; s = exp(sin(x) - 1.0); h += 0.10737418 * s; q -= vec2(0.08549849, 0.11989305) * (s * cos(x));
    x = dot(q, vec2(0.06884646, 1.29511585)) + pa[2][3]; s = exp(sin(x) - 1.0); h += 0.08589935 * s; q -= vec2(0.00625350, 0.11763872) * (s * cos(x));
    x = dot(q, vec2(-0.75145672, 1.33319915)) + pa[3][0]; s = exp(sin(x) - 1.0); h += 0.06871948 * s; q -= vec2(-0.04627577, 0.08210029) * (s * cos(x));
    return h / 4.72512209 - 0.5;
}

float swellDetail(vec2 q, mat4 pa, mat4 pb, mat4 pc, vec2 dx, vec2 dy, out vec2 slope, out vec2 moved) {
    float h = 0.0;
    vec2 d = vec2(0.0);
    float x;
    float s;
    float c;
    float f;
    float p;
    vec2 k;
    k = vec2(0.00000000, 0.21000000); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[0][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 1.00000000 * s; d += vec2(0.00000000, 1.00000000) * c; q -= vec2(0.00000000, 1.37142857) * c; }
    k = vec2(-0.13296277, 0.20910701); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[0][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.80000000 * s; d += vec2(-0.50745933, 0.79806780) * c; q -= vec2(-0.58869714, 0.92582834) * c; }
    k = vec2(-0.26479474, 0.12403164); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[0][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.64000000 * s; d += vec2(-0.80997391, 0.37939724) * c; q -= vec2(-0.79483907, 0.37230797) * c; }
    k = vec2(-0.34220012, -0.04415217); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[0][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.51200000 * s; d += vec2(-0.83894277, -0.10824410) * c; q -= vec2(-0.69639877, -0.08985245) * c; }
    k = vec2(-0.31278976, -0.26063050); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[1][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.40960000 * s; d += vec2(-0.61460373, -0.51211547) * c; q -= vec2(-0.43155718, -0.35959285) * c; }
    k = vec2(-0.14643990, -0.45756694); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[1][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.32768000 * s; d += vec2(-0.23061767, -0.72058927) * c; q -= vec2(-0.13697876, -0.42800460) * c; }
    k = vec2(0.14389408, -0.54834052); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[1][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.26214400 * s; d += vec2(0.18162121, -0.69210818) * c; q -= vec2(0.09125249, -0.34773797) * c; }
    k = vec2(0.49046731, -0.45490139); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[1][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.20971520 * s; d += vec2(0.49616280, -0.46018387) * c; q -= vec2(0.21087244, -0.19558116) * c; }
    k = vec2(0.77640525, -0.14242466); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[2][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.16777216 * s; d += vec2(0.62949616, -0.11547549) * c; q -= vec2(0.22631128, -0.04151480) * c; }
    k = vec2(0.86328075, 0.34976665); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[2][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.13421773 * s; d += vec2(0.56097973, 0.22728643) * c; q -= vec2(0.17059951, 0.06912006) * c; }
    k = vec2(0.63815293, 0.89487076); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[2][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.10737418 * s; d += vec2(0.33236124, 0.46606438) * c; q -= vec2(0.08549849, 0.11989305) * c; }
    k = vec2(0.06884646, 1.29511585); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[2][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.08589935 * s; d += vec2(0.02873808, 0.54061079) * c; q -= vec2(0.00625350, 0.11763872) * c; }
    k = vec2(-0.75145672, 1.33319915); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[3][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.06871948 * s; d += vec2(-0.25140306, 0.44602747) * c; q -= vec2(-0.04627577, 0.08210029) * c; }
    k = vec2(-1.59238438, 0.85174058); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[3][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.05497558 * s; d += vec2(-0.42697749, 0.22838333) * c; q -= vec2(-0.06648222, 0.03556026) * c; }
    k = vec2(-2.12489773, -0.16010909); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[3][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.04398047 * s; d += vec2(-0.45665224, -0.03440833) * c; q -= vec2(-0.06014557, -0.00453191) * c; }
    k = vec2(-2.01448793, -1.50482010); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pa[3][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.03518437 * s; d += vec2(-0.34697862, -0.25919262) * c; q -= vec2(-0.03865790, -0.02887741) * c; }
    k = vec2(-1.05313573, -2.77390632); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[0][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.02814750 * s; d += vec2(-0.14538275, -0.38293082) * c; q -= vec2(-0.01370143, -0.03608888) * c; }
    k = vec2(0.70765810, -3.42891001); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[0][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.02251800 * s; d += vec2(0.07829653, -0.37938059) * c; q -= vec2(0.00624185, -0.03024445) * c; }
    k = vec2(2.87568399, -2.96627115); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[0][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.01801440 * s; d += vec2(0.25500616, -0.26303913) * c; q -= vec2(0.01719645, -0.01773815) * c; }
    k = vec2(4.74156815, -1.13290089); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[0][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.01441152 * s; d += vec2(0.33699387, -0.08051780) * c; q -= vec2(0.01922328, -0.00459301) * c; }
    k = vec2(5.43870849, 1.87406910); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[1][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.01152922 * s; d += vec2(0.30980347, 0.10675202) * c; q -= vec2(0.01494890, 0.00515109) * c; }
    k = vec2(4.22900313, 5.30965109); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[1][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00922337 * s; d += vec2(0.19307186, 0.24240801) * c; q -= vec2(0.00788060, 0.00989435) * c; }
    k = vec2(0.84918235, 7.96469160); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[1][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00737870 * s; d += vec2(0.03107223, 0.29143415) * c; q -= vec2(0.00107283, 0.01006233) * c; }
    k = vec2(-4.19732127, 8.46848805); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[1][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00590296 * s; d += vec2(-0.12309323, 0.24835210) * c; q -= vec2(-0.00359509, 0.00725343) * c; }
    k = vec2(-9.54134727, 5.77491792); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[2][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00472237 * s; d += vec2(-0.22426534, 0.13573701) * c; q -= vec2(-0.00554058, 0.00335345) * c; }
    k = vec2(-13.15719851, -0.29080061); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[2][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00377789 * s; d += vec2(-0.24785996, -0.00547820) * c; q -= vec2(-0.00517985, -0.00011449) * c; }
    k = vec2(-12.91712761, -8.62012377); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[2][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00302231 * s; d += vec2(-0.19502908, -0.13015083) * c; q -= vec2(-0.00344768, -0.00230078) * c; }
    k = vec2(-7.40431628, -16.76202558); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[2][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00241785 * s; d += vec2(-0.08960018, -0.20283851) * c; q -= vec2(-0.00133984, -0.00303316) * c; }
    k = vec2(3.24014710, -21.37883554); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[3][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00193428 * s; d += vec2(0.03142528, -0.20734733) * c; q -= vec2(0.00039750, -0.00262278) * c; }
    k = vec2(16.76250785, -19.23640698); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[3][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00154743 * s; d += vec2(0.13029984, -0.14953018) * c; q -= vec2(0.00139420, -0.00159996) * c; }
    k = vec2(28.87087549, -8.54132431); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[3][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00123794 * s; d += vec2(0.17986860, -0.05321335) * c; q -= vec2(0.00162800, -0.00048164) * c; }
    k = vec2(34.15609814, 9.77476550); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pb[3][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00099035 * s; d += vec2(0.17055095, 0.04880814) * c; q -= vec2(0.00130578, 0.00037369) * c; }
    k = vec2(27.82190334, 31.35933993); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[0][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00079228 * s; d += vec2(0.11134307, 0.12549987) * c; q -= vec2(0.00072110, 0.00081279) * c; }
    k = vec2(7.84823987, 48.84159595); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[0][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00063383 * s; d += vec2(0.02517324, 0.15665949) * c; q -= vec2(0.00013791, 0.00085824) * c; }
    k = vec2(-23.10948506, 53.60306593); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[0][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00050706 * s; d += vec2(-0.05940837, 0.13779929) * c; q -= vec2(-0.00027531, 0.00063858) * c; }
    k = vec2(-56.95032106, 38.74321795); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[0][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00040565 * s; d += vec2(-0.11733944, 0.07982585) * c; q -= vec2(-0.00045997, 0.00031292) * c; }
    k = vec2(-81.23865216, 2.52002911); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[1][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00032452 * s; d += vec2(-0.13415320, 0.00416144) * c; q -= vec2(-0.00044484, 0.00001380) * c; }
    k = vec2(-82.48877037, -48.92743044); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[1][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00025961 * s; d += vec2(-0.10917511, -0.06475618) * c; q -= vec2(-0.00030623, -0.00018164) * c; }
    k = vec2(-51.15930273, -100.94763838); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[1][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00020769 * s; d += vec2(-0.05426801, -0.10708175) * c; q -= vec2(-0.00012876, -0.00025407) * c; }
    k = vec2(12.97385073, -132.91019754); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[1][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00016615 * s; d += vec2(0.01103008, -0.11299731) * c; q -= vec2(0.00002214, -0.00022679) * c; }
    k = vec2(97.07157643, -124.13054770); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[2][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00013292 * s; d += vec2(0.06614429, -0.08458220) * c; q -= vec2(0.00011230, -0.00014360) * c; }
    k = vec2(175.25280261, -62.14124856); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[2][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00010634 * s; d += vec2(0.09570965, -0.03393679) * c; q -= vec2(0.00013745, -0.00004874) * c; }
    k = vec2(213.85267715, 49.08536752); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[2][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00008507 * s; d += vec2(0.09360433, 0.02148490) * c; q -= vec2(0.00011371, 0.00002610) * c; }
    k = vec2(181.86460820, 184.27875530); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[2][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00006806 * s; d += vec2(0.06379989, 0.06464680) * c; q -= vec2(0.00006556, 0.00006643) * c; }
    k = vec2(64.41405254, 298.64381719); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[3][0]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00005445 * s; d += vec2(0.01811102, 0.08396838) * c; q -= vec2(0.00001574, 0.00007299) * c; }
    k = vec2(-124.94799481, 338.15803216); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[3][1]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00004356 * s; d += vec2(-0.02815672, 0.07620306) * c; q -= vec2(-0.00002070, 0.00005603) * c; }
    k = vec2(-338.52347650, 257.60849505); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[3][2]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00003484 * s; d += vec2(-0.06114093, 0.04652682) * c; q -= vec2(-0.00003803, 0.00002894) * c; }
    k = vec2(-500.19033883, 42.17487413); p = max(abs(dot(dx, k)), abs(dot(dy, k))); if (p < 3.14159265) { f = min(3.14159265 / max(p, 0.000001) - 1.0, 1.0); x = dot(q, k) + pc[3][3]; s = exp(sin(x) - 1.0) * f; c = s * cos(x); h += 0.00002788 * s; d += vec2(-0.07240508, 0.00610503) * c; q -= vec2(-0.00003809, 0.00000321) * c; }
    moved = q;
    slope = -d / 1.49534044;
    return h / 4.99988850 - 0.5;
}

// rafasconquest.client.WaterSwell.foamGlsl()
float whitecap(vec4 at, mat4 pa, mat4 pb, mat4 pc, float tall, vec3 foamAt, sampler3D foam, out float shade) {
    shade = 0.0;
    vec2 dx = dFdx(at.xy); // before any branch, as derivatives need
    vec2 dy = dFdy(at.xy);
    if (at.w < 0.99) {
        return 0.0;
    }
    vec2 slope;
    vec2 moved;
    float h = swellDetail(at.xy, pa, pb, pc, dx, dy, slope, moved) * tall * at.z;
    float lean = tall / 13.0 * at.z;
    vec3 n = normalize(vec3(slope.x * lean, 0.6, slope.y * lean));
    shade = mix(n.x * 0.5 + 0.5, 0.0, pow(n.y, 4.0)) * 0.3;
    float crest = clamp(h * pow(max(n.y, 0.0), 4.0) * 1.2, 0.0, 1.0);
    vec2 w = mix(at.xy, moved, clamp(at.z * 2.0, 0.2, 1.0)) + foamAt.xy;
    float t = foamAt.z / 360.0;
    // mipmapped reads over the pixel's footprint: far off, the noise blends to its mean instead of sparkling
    vec2 s1 = textureGrad(foam, vec3(w * 0.26, t), vec3(dx * 0.26, 0.0), vec3(dy * 0.26, 0.0)).rg;
    vec2 s2 = textureGrad(foam, vec3(w * 0.02, t + 0.5), vec3(dx * 0.02, 0.0), vec3(dy * 0.02, 0.0)).rg;
    vec2 s3 = textureGrad(foam, vec3(w * 0.1, t + 1.0), vec3(dx * 0.1, 0.0), vec3(dy * 0.1, 0.0)).rg;
    float cover = s1.r * s2.r * s3.r * 2.8 * 0.8;
    cover = (1.0 - crest) * cover + crest * 0.8;
    float cells = 0.2 + 0.8 * s1.g * (1.0 - s2.g);
    return clamp(smoothstep(0.45, 1.0, cover) * cells, 0.0, 1.0);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    // rafasconquest.client.WaterSwell.foamMix("color", "", "swellAt", "swellLight")
    float swellShade;
    float swellFoam = whitecap(swellAt, SwellPhaseA, SwellPhaseB, SwellPhaseC, SwellTall, SwellFoamAt, SwellFoam, swellShade);
    if (swellAt.w > 0.99) {
        color.a = max(color.a, mix(0.8, 0.92, swellAt.z));
    }
    color = clamp(color + vec4(swellLight, 1.0) * (swellFoam + swellShade), 0.0, 1.0);
    // rafasconquest.client.WaterSwell.hullCut("", "swellAt")
    if (swellAt.w > 0.99) {
        vec2 swellHull = floor(swellAt.xy - SwellHullAt.xy);
        if (all(greaterThanEqual(swellHull, vec2(0.0))) && all(lessThan(swellHull, vec2(256.0))) && texelFetch(SwellHull, ivec2(swellHull), 0).r > 0.5) {
            discard;
        }
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
