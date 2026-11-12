# Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)

Scope: Minecraft 1.21.1, NeoForge 21.1.x, Java 21, played with Sodium (NeoForge), Distant Horizons (DH) and possibly Iris. Anything that only applies to 1.21.2+ / 1.21.5+ or to Fabric is flagged.

Reading note for the report writer: "Cited Findings" are backed by the linked page. "Inferences" mix reasoning with knowledge of the 1.21.1 vanilla code that I could not re-verify against a page in this session; those are marked "(code knowledge)".

---

## 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)

### Takeaway
On 1.21.1 the right shape is: hook `RenderLevelStageEvent` at the right stage, build static geometry once into a `VertexBuffer` (STATIC usage) and redraw it each frame with the event's matrices; only rebuild when the data changes. Group by render type to cut state changes. Flywheel (Create's engine) has a public visualizer API on NeoForge 1.21.1 for instanced entities/block entities, but it turns its instancing off whenever an Iris shader pack is active unless a compat mod is present.

### Cited Findings
- NeoForge 1.21.1 `RenderLevelStageEvent.Stage` constants: `AFTER_SKY`, `AFTER_SOLID_BLOCKS` (RenderType.solid), `AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS` (name really has "BLOCKS" twice), `AFTER_CUTOUT_BLOCKS`, `AFTER_ENTITIES`, `AFTER_BLOCK_ENTITIES`, `AFTER_TRANSLUCENT_BLOCKS`, `AFTER_TRIPWIRE_BLOCKS`, `AFTER_PARTICLES` (inside the fabulous particles target), `AFTER_WEATHER`, `AFTER_LEVEL`. Getters: `getPoseStack()`, `getModelViewMatrix()`, `getProjectionMatrix()`, `getLevelRenderer()`, `getRenderTick()`, `getPartialTick()` (a `DeltaTracker`), `getCamera()`, `getFrustum()`. Javadoc warns translucency sorting can break at `AFTER_TRANSLUCENT_BLOCKS` and suggests `AFTER_TRIPWIRE_BLOCKS` or `AFTER_PARTICLES` for translucent things. The event fires on the main bus, client only, and fires once per stage, so a handler must check `getStage()`. `RegisterStageEvent` (mod bus) can add custom stages bound to a RenderType. — [NeoForge 1.21.1 RenderLevelStageEvent.java](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/event/RenderLevelStageEvent.java)
- In 1.21.1, `VertexBuffer#drawWithShader` and `VertexBuffer.Usage` exist and take a `ShaderInstance`; 1.21.2 renamed `ShaderInstance` to `CompiledShaderProgram`, moved `VertexBuffer.$Usage` to `com.mojang.blaze3d.buffers.BufferUsage`, added `GpuBuffer`/`GpuFence`, and removed `EffectInstance`. So any tutorial using `CompiledShaderProgram`, `CoreShaders`, or `BufferUsage` is 1.21.2+ and does not apply. — [NeoForge 1.21.1→1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)
- 1.21.5+ removed `BufferRenderer` (Yarn name for `BufferUploader`) and moved to the `GpuBuffer`/`RenderPipeline` model; Fabric's 1.21.8/1.21.11 world-rendering docs use `MappableRingBuffer` and do not port to 1.21.1. — [Fabric discussion #4566](https://github.com/orgs/FabricMC/discussions/4566); [Fabric docs 1.21.11 world rendering](https://docs.fabricmc.net/1.21.11/develop/rendering/world)
- A Forge-forum report: models/textures drawn from `RenderLevelStageEvent` at `AFTER_ENTITIES` were not visible with shaders enabled — a known pitfall for custom world rendering under shader packs. — [Forge forum thread 155104](https://forums.minecraftforge.net/topic/155104-model-and-textures-rendered-on-entity-and-renderlevelstageevent-not-visible-when-using-shaders)
- NeoForge had a 1.21.1 PR titled "[1.21.1] Feature: Add cached ber rendering pipeline" (#1816), aimed at caching vertices from custom renderers (merge status not confirmed). — [NeoForge PR #1816 mirror](https://github-users.yhajoa.workers.dev/neoforged/NeoForge/pull/1816)
- Flywheel describes itself as "a modern engine for modded Minecraft" giving an alternate, unified path for entity and block-entity rendering that uses GPU instancing, and lets developers define instance formats and write custom shaders. NeoForge coordinates (repo `https://maven.createmod.net/`): `compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${minecraft_version}:${flywheel_version}")` + `runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${minecraft_version}:${flywheel_version}")`. — [Flywheel GitHub](https://github.com/Engine-Room/Flywheel)
- Flywheel's `1.21.1/dev` branch has the API package `dev.engine_room.flywheel.api.visualization` with `EntityVisualizer`, `BlockEntityVisualizer`, `VisualizerRegistry`, `VisualEmbedding`, `VisualizationManager`, etc. — [Flywheel 1.21.1/dev visualization package](https://github.com/Engine-Room/Flywheel/tree/1.21.1/dev/common/src/api/java/dev/engine_room/flywheel/api/visualization)
- `VisualizerRegistry.setVisualizer(EntityType<T>, @Nullable EntityVisualizer<? super T>)` and the `BlockEntityType` overload register a visualizer per type; `getVisualizer` returns null if none. — [VisualizerRegistry.java (1.21.1/dev)](https://raw.githubusercontent.com/Engine-Room/Flywheel/1.21.1/dev/common/src/api/java/dev/engine_room/flywheel/api/visualization/VisualizerRegistry.java)
- A 1.21.1 dev branch of Flywheel was merged in January 2025; visualizers return a list of visuals. — [Flywheel mirror (commit history)](https://git.grimmauld.de/mirrors/Flywheel/src/1.20.1/dev)
- When a shader pack is active, Flywheel turns off its instancing and entities fall back to the vanilla renderer, which is CPU-heavy with many entities. The "Iris & Oculus Flywheel Compat" mod merges Flywheel's shader into the pack's shader so instancing keeps working; it lists 1.21.1 and NeoForge among supported platforms and needs specific Iris versions (e.g. Iris/Oculus 1.8.1+ for its 2.0.0). — [Iris Flywheel Compat (Modrinth)](https://modrinth.com/mod/iris-flw-compat)
- Sodium 0.6.0 (NeoForge, 1.21.1) "Added an API for mods that need to conditionally render block entities (i.e. Flywheel.)" — [Sodium mc1.21.1-0.6.0-neoforge](https://modrinth.com/mod/sodium/version/mc1.21.1-0.6.0-neoforge)

### Inferences
- (code knowledge) 1.21.1 cached-geometry pattern: `BufferBuilder b = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);` → write vertices → `MeshData mesh = b.build();` (null if empty) → `vb = new VertexBuffer(VertexBuffer.Usage.STATIC); vb.bind(); vb.upload(mesh); VertexBuffer.unbind();`. Per frame: set render state (blend/depth/cull), `vb.bind(); vb.drawWithShader(poseMatrix, projectionMatrix, GameRenderer.getPositionColorShader()); VertexBuffer.unbind();`. Upload once, redraw many; rebuild only on a dirty flag. Close the `VertexBuffer` on level unload / resource reload. Camera-relative: build vertices relative to a fixed origin and translate the pose by `origin - camera.getPosition()` each frame, so the buffer does not need rebuilding when the camera moves and float precision stays good far from 0,0.
- For RafasConquest's `BattleSpotRing` and any terrain-hugging overlay (survey lines, siege deployment marks, territory borders in-world): the ring geometry should be a cached `VertexBuffer` rebuilt only when the battle spot or radius changes, not re-tessellated every frame. Same for road previews and wall-deployment outlines.
- Immediate-mode (`MultiBufferSource.BufferSource`/`Tesselator`) is fine for small dynamic things (a few hundred vertices) but costs CPU per frame for vertex writing plus an upload; at thousands of quads per frame it shows up in profilers. Batching rule: keep one `BufferSource` per frame, request `getBuffer(renderType)` per type, and call `endBatch()` once at the end; interleaving types forces flushes.
- Instancing on 1.21.1 vanilla is not exposed by `VertexBuffer` (no `glDrawElementsInstanced` wrapper); a mod wanting real instancing either uses Flywheel or raw GL (LWJGL `GL31.glDrawElementsInstanced`) with its own shader, which then needs its own Iris handling. Flywheel is the proven route but brings the shader-pack fallback above; plan for the vanilla path to be acceptable on its own.
- If Flywheel is used for Recruits soldiers, the visualizer must be registered for Villager Recruits' entity types, which RafasConquest does not own; registering visualizers for another mod's types is allowed by the API shape (`EntityType<T>` key) but would conflict if Recruits ever ships its own.

### Gaps
- I did not find Flywheel 1.21.1 javadoc/tutorial text describing how to write an `EntityVisualizer` (InstanceTypes, `SimpleEntityVisualizer` builder, transformed/oriented instance types); only the package listing and registry signatures were confirmed.
- Merge status and final API of NeoForge PR #1816 (cached BER pipeline) not confirmed.
- No measured numbers found for VertexBuffer vs immediate-mode cost in 1.21.1.

---

## 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling

### Takeaway
The proven levers are: (1) occlusion culling on spare threads (Entity Culling), (2) distance tiers — full model near, skipped layers/animation further out, billboard/impostor far, (3) vanilla `entityDistanceScaling` and server tracking range as hard caps, and (4) cheaper immediate-mode buffers (ImmediatelyFast). 1.21.1 has no `EntityRenderState` (that is 1.21.2+), so LOD logic lives in the renderer reading the entity directly.

### Cited Findings
- Entity Culling (tr7zw) uses spare CPU threads for async line-of-sight "path-tracing" that "runs alongside the main game thread without blocking", collecting data on the main thread every few ticks; hidden entities and block entities skip rendering (fewer draw calls). It errs on the side of caution near walls/ceilings. It also does client-only "tick culling" of hidden entities, which does not affect server simulation. Some block entities render beyond their bounds (beacon, Create pulleys) and must be whitelisted in config; there is no documented code API — whitelisting is by config. Supported 1.19.4+ on Fabric/Forge/NeoForge. The author chose CPU raytracing to reduce GPU load and GPU→CPU transfers. — [EntityCulling GitHub](https://github.com/tr7zw/EntityCulling)
- ImmediatelyFast replaces immediate-mode buffers with a custom implementation that batches draw calls and uploads more efficiently, covering entities, block entities, particles, text, GUI/HUD and other mods' immediate rendering; incompatible with OptiFine/OptiFabric and most closed clients. — [ImmediatelyFast GitHub](https://github.com/RaphiMC/ImmediatelyFast)
- ImmediatelyFast's changelog lists 1.21.1 support (Fabric + NeoForge); a later release removed experimental universal HUD batching because it "caused too many graphical issues". — [ImmediatelyFast changelog (Modrinth)](https://modrinth.com/mod/immediatelyfast/changelog?page=2); [ImmediatelyFast issue #512](https://github.com/RaphiMC/ImmediatelyFast/issues/512)
- ImmediatelyFast API: main class `net.raphimc.immediatelyfastapi.ImmediatelyFastApi` (batching system and config access); do not rely on `net.raphimc.immediatelyfast` internals. Maven: `maven.modrinth:immediatelyfast:x.x.x`. — [ImmediatelyFast API_DOCS.md](https://github.com/RaphiMC/ImmediatelyFast/blob/main/docs/API_DOCS.md)
- Sodium 0.6.0 "Improved compatibility with mods that extend the entity renderer." — [Sodium mc1.21.1-0.6.0-neoforge](https://modrinth.com/mod/sodium/version/mc1.21.1-0.6.0-neoforge)
- 1.21.2 introduced `EntityRenderState` (`createRenderState`/`extractRenderState`, `setupAnim` taking only the state). This does not exist in 1.21.1. — [NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)
- Vanilla `entityDistanceScaling` (Entity Distance slider) ranges 0.5–5.0 (50–500%), default 1.0, and scales the max distance entities render. — [Options.txt (wiki mirror)](https://breezewiki.discard.no/minecraft/wiki/Options.txt)
- The client renders entities depending on both Render Distance and Entity Distance; the server does not send entities beyond `entity-broadcast-range-percentage`. — [Simulation distance (wiki mirror)](https://wiki.ronlab.site/content/minecraftwiki_en_all_maxi_2025-11/Simulation_Distance)
- An entity must be tracked before it can be rendered, so raising render distance also needs raising tracking distance. — [Entity View Distance](https://www.modpackindex.com/mod/30107/entity-view-distance)
- Entity Model Features (EMF) has an "animation distance LOD" setting that was changed to factor in entity size so larger entities are affected less. — [EMF changelog (Modrinth)](https://modrinth.com/mod/entity-model-features/changelog?page=18)
- Distance-tiered animation update rate is a standard engine technique: e.g. beyond distance A update animation every 2nd frame, beyond B every 4th, with fewer bones for distant LODs. — [GPU Animation Entities Pro LOD docs](https://sniveler-code.gitbook.io/dots/gpu-animation-entities-pro/8.-lods-and-performance-optimization); [O3DE simple LOD distance](https://docs.o3de.org/docs/user-guide/components/reference/animation/simple-lod-distance/)
- Animating a model makes LOD transitions harder to notice. — [Simplygon blog](https://simplygon.com/blog/11f00494-a699-4f41-a5f1-33b17b3e19ab)

### Inferences
- (code knowledge) Vanilla per-entity cost in 1.21.1: `EntityRenderDispatcher.shouldRender` → `EntityRenderer.shouldRender` (frustum test on `getBoundingBoxForCulling()` + `shouldRenderAtSqrDistance`, which scales with `Entity.getViewScale()`/`entityDistanceScaling`). Then `LivingEntityRenderer.render` runs `setupAnim` for the full model, every render layer (armor, held items, elytra, etc.), the name tag (`shouldShowName`), and the shadow. For a battle army, the big wins per distant soldier are: skip render layers (armor/items) beyond ~32–48 blocks, skip name tags, skip shadows, and freeze/throttle `setupAnim` (reuse last pose every N frames).
- Recruits are Villager Recruits entities, not RafasConquest's own; LOD for them means either (a) a mixin/event on their renderer (`RenderLivingEvent.Pre` from NeoForge can cancel and substitute a cheaper draw), or (b) letting RafasConquest's battle system draw its own impostors for far soldiers. `RenderLivingEvent.Pre` cancel + draw a camera-facing quad (billboard sprite from a small atlas: one per faction/unit type) is the cheapest impostor path and stays Sodium-safe because it uses the normal entity pass.
- Impostor batching: draw all far soldiers' billboards into one buffer under one RenderType (one draw call) from a single `RenderLevelStageEvent` (`AFTER_ENTITIES`), instead of one draw per entity. Build the quad list on the main thread from entity positions; the buffer is dynamic (rebuilt each frame) but tiny (4 vertices per soldier).
- The naval "real → LOD → sprite" ladder maps directly onto this: real Sable ship near, a simplified mesh (cached `VertexBuffer` per ship class) mid, a billboard far. For very far ships that must sit in DH terrain, DH's generic box API (section 4) can draw a box-shaped hull with correct DH depth.
- Ships and other oversized renderers must override the entity's culling box (`getBoundingBoxForCulling`) or they will be frustum- and Entity-Culling-culled while still on screen; Entity Culling users can also whitelist the type in config.
- Entity Culling's tick culling is client-only; RafasConquest's server-side battle logic is unaffected, but any client-side animation state the mod drives from `tick()` on hidden entities may freeze until visible.

### Gaps
- No primary source found for EMF/ETF frame cost numbers or exact EMF option names.
- No source found measuring per-soldier cost of render layers vs body model in vanilla 1.21.1.
- No public API in Entity Culling for other mods to mark entities (only the config whitelist was documented).

---

## 3. GUI and map screens (strategy map, faction map, battle overlays)

### Takeaway
Map mods cache: bake each map region into a `NativeImage` off-thread, upload once on the render thread into a `DynamicTexture`, redraw it as one textured quad per region per frame, re-bake only when that region changes, and release textures for regions not seen for a while. In vanilla 1.21.1, `GuiGraphics.blit` is not batched (batching arrived in 1.21.2), so hundreds of per-frame icon blits are hundreds of draws; draw icons from one atlas into one buffer instead.

### Cited Findings
- FTB Chunks pattern (`MapRegionTexture`): `requestBake()` sets a volatile `baking` flag and runs a `RenderMapImageTask` on `FTBChunksClient.MAP_EXECUTOR` (background), whose callback does `Minecraft.getInstance().execute(() -> upload(image))`. First upload creates `new DynamicTexture(...)` and registers it with the `TextureManager`; later uploads call `texture.setPixels(image)` then `texture.upload()`. The texture is created lazily when `getTextureID()` is first asked for. A `RetryTracker` gates retries after failures. `close()` releases from the TextureManager and closes the texture. — [FTB Chunks MapRegionTexture.java](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegionTexture.java)
- FTB Chunks `MapRegion`: `update(save)` defers a re-bake to the main thread via `Minecraft.getInstance().execute(regionTexture::requestBake)`; data loads are scheduled on `MAP_EXECUTOR` and only publish results under a lock; `touch()` marks a region used so its baked texture is not reaped; `releaseIfStale(now, interval)` releases data and the GPU texture for regions unused past an interval (skipping the region the player is in). — [FTB Chunks MapRegion.java](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegion.java)
- FTB Chunks processes map tasks in small batches per tick by default (an addon exposes this). — [FTB Chunks Additions](https://www.curseforge.com/minecraft/mc-mods/ftb-chunks-additions)
- In 1.21.1, `GuiGraphics` has `drawManaged(...)` and `bufferSource()`; 1.21.2 removed both, made GUI methods take `Function<ResourceLocation, RenderType>`, moved tint into `blit`, and replaced `bufferSource` with `drawSpecial`, which ends the current batch. — [NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)
- Mojang's tracker has a report of a large frame-rate drop when many HUD/hotbar elements are drawn — evidence that per-element GUI draws matter. — [MC-233603](https://bugs-legacy.mojang.com/browse/MC-233603)
- ImmediatelyFast optimizes HUD and text rendering, adds `fast_text_lookup` (caches data between characters), `font_atlas_resizing`, `experimental_sign_text_buffering` (buffer sign text instead of re-rendering each frame), `map_atlas_generation` (pack map textures into one texture) and `avoid_redundant_framebuffer_switching`. — [ImmediatelyFast GitHub](https://github.com/RaphiMC/ImmediatelyFast)
- The Fabric 1.21.1 docs page on `GuiGraphics` covers fill/blit/drawString usage for that version. — [Fabric docs: Drawing to the GUI 1.21.1](https://docs.fabricmc.net/1.21.1/develop/rendering/gui-graphics)

### Inferences
- (code knowledge) 1.21.1 `GuiGraphics.blit(...)` → `innerBlit` binds the texture, begins a `Tesselator` buffer, writes 4 vertices and calls `BufferUploader.drawWithShader` — one draw call and one buffer upload per blit. `fill(...)` and `drawString(...)` write into the shared `bufferSource` and are flushed unless wrapped in `drawManaged(...)`, which defers the flush so many fills/strings batch into one draw per render type. Practical rule for 1.21.1: wrap bulk `fill`/`drawString` in `guiGraphics.drawManaged(() -> { ... })`; do not loop `blit` hundreds of times.
- Strategy/faction map layering for RafasConquest:
  1. Static layers (terrain colour, territory fills, province/title borders, roads) → bake into `NativeImage` tiles (e.g. 256² or 512² pixels per tile) on a worker thread; upload on the render thread as `DynamicTexture`; draw each visible tile as one quad. Re-bake only tiles whose source data changed (dirty set keyed by tile). This turns per-frame work from O(chunks × layers) into O(visible tiles).
  2. Semi-static vector overlays (borders at the current zoom, road polylines) → if not baked into the texture, put them in a cached `VertexBuffer` rebuilt on zoom-level change or data change; pan with a pose translate rather than rebuilding.
  3. Icons (POI, holds, units) → one atlas texture; per frame write all visible icon quads into one `BufferBuilder` with `POSITION_TEX_COLOR` and draw once (or via `bufferSource().getBuffer(RenderType.text(atlas))`-style batching), instead of N `blit` calls.
  4. Labels → cull by zoom (only titles above a size threshold), draw all strings inside one `drawManaged` block; cache `FormattedCharSequence`/width per label instead of re-splitting each frame.
- `NativeImage` pixel writes are fast native memory writes; the costly part is `upload()` (glTexSubImage). Spread re-uploads: cap to a few tiles per frame (queue the rest), exactly as FTB Chunks defers via `Minecraft.execute`.
- An alternative to tiles is rendering the whole static map into an offscreen `RenderTarget` (`TextureTarget`) once and blitting it; tiles are better for a large world map because they allow partial updates and texture reuse across zooms.
- Battle overlays (`BattleScreen`): same rules — static frame/background into a texture or cached buffer, dynamic unit markers batched in one buffer, strings in one managed block.

### Gaps
- JourneyMap's tile caching code was not reviewed (not reached in this session); Xaero's is closed-source; Antique Atlas not reviewed.
- The exact `RenderMapImageTask` image size in FTB Chunks (often stated as 512×512 per region) was not confirmed from source.
- No measured numbers found for blit-per-icon cost on 1.21.1.

---

## 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)

### Takeaway
Avoid copying depth if you can sample it: DH's API returns its own depth and colour texture IDs, so a shader can read DH depth directly next to vanilla depth. Never read depth or pixels back to the CPU synchronously (`glReadPixels`/`glGetTexImage` into client memory stalls the pipeline); if a readback is unavoidable, use PBO ping-pong one frame late. For things that should live in the far DH world (distant ships, banners, markers), DH's generic box renderer draws inside DH's terrain pass with correct depth, SSAO and lighting.

### Cited Findings
- `IDhApiRenderProxy.getDhDepthTextureId()` returns the GL name of DH's depth texture (`success=false`, payload `-1` if not created yet); `getDhColorTextureId()` likewise for colour. `setDeferTransparentRendering(true)` makes DH draw transparent LODs in a second pass during Minecraft's transparent pass. `getNearClipPlaneDistanceInBlocks(partialTicks)` gives DH's near clip, which changes with FOV and speed. `clearRenderDataCache()` forces LOD re-generation (colours are baked into geometry). — [IDhApiRenderProxy javadoc](https://distant-horizons-f74d0f.gitlab.io/com/seibel/distanthorizons/api/interfaces/render/IDhApiRenderProxy.html)
- DH render events (`com.seibel.distanthorizons.api.methods.events.abstractEvents`): `DhApiBeforeRenderEvent`, `DhApiBeforeRenderSetupEvent`, `DhApiBeforeTextureClearEvent`, `DhApiBeforeRenderPassEvent`, `DhApiBeforeBufferRenderEvent`, `DhApiBeforeDeferredRenderEvent`, `DhApiBeforeApplyShaderRenderEvent`, `DhApiBeforeGenericRenderSetupEvent`, `DhApiBeforeGenericObjectRenderEvent`, `DhApiBeforeGenericRenderCleanupEvent`, `DhApiBeforeRenderCleanupEvent`, `DhApiAfterRenderEvent`, `DhApiColorDepthTextureCreatedEvent` (fires when DH (re)creates its colour/depth textures); `DhApiRenderParam` carries info shared by DH and MC rendering. Overrides: `IDhApiFramebuffer`, `IDhApiShaderProgram`, `IDhApiGenericObjectShaderProgram`, `IDhApiCullingFrustum`, `IDhApiShadowCullingFrustum`. — [DH API all classes](https://distant-horizons-f74d0f.gitlab.io/allclasses-index.html)
- DH API 4.1.0 added `DhApiBeforeColorDepthTextureCreatedEvent` / `DhApiAfterColorDepthTextureCreatedEvent`, replacing the deprecated `DhApiColorDepthTextureCreatedEvent`. The API changelog also lists the Generic Rendering API (`DhApiRenderableBox`, `DhApiRenderableBoxGroupShading`). — [DH API 4.1.0](https://modrinth.com/mod/distanthorizonsapi/version/4.1.0); [DH API changelog](https://modrinth.com/mod/distanthorizonsapi/changelog)
- DH 3.0.0-b exists for 1.21.1. — [Distant Horizons 3.0.0-b-1.21.1](https://modrinth.com/mod/distanthorizons/version/3.0.0-b-1.21.1)
- Generic objects: `IDhApiCustomRenderRegister` adds/removes `IDhApiRenderableBoxGroup`s (created by `IDhApiCustomRenderObjectFactory`); a group is a list of `DhApiRenderableBox` rendered in DH's terrain pass. Group methods: `setActive`, `setSsaoEnabled`, `setOriginBlockPos(DhApiVec3d)` (moving the origin needs no rebuild), `setPreRenderFunc`/`setPostRenderFunc(Consumer<DhApiRenderParam>)` (pre-render is "a good place to change the origin"), `triggerBoxChange()` (required after changing a box's colour/position), `setSkyLight`/`setBlockLight` (0–15), `setShading`, and `getResourceLocationNamespace/Path` (shown in DH's F3 profiler pie). Added in API 3.0.0. — [IDhApiRenderableBoxGroup javadoc](https://distant-horizons-f74d0f.gitlab.io/com/seibel/distanthorizons/api/interfaces/render/IDhApiRenderableBoxGroup.html)
- `glReadPixels()` into client memory "blocks the pipeline and waits until all pixel data are transferred"; with a PBO bound to `GL_PIXEL_PACK_BUFFER` it "schedule[s] asynchronous DMA transfer and returns immediately"; two PBOs ping-ponged let the CPU process last frame's data while this frame's copy runs. — [songho: OpenGL Pixel Buffer Object](https://www.songho.ca/opengl/gl_pbo.html)
- Entity Culling chose CPU raytracing specifically to avoid GPU load and GPU→CPU transfers (i.e. no occlusion-query readback). — [EntityCulling GitHub](https://github.com/tr7zw/EntityCulling)
- 1.21.2 renamed post-chain samplers (`DiffuseSampler`/`DiffuseDepthSampler` → `InSampler`/`MainSampler`/`MainDepthSampler`) and `RenderTarget#blitToScreen` → `blitAndBlendToScreen`; 1.21.1 still uses the old names and `EffectInstance`. — [NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)

### Inferences
- (code knowledge) In 1.21.1 `RenderTarget.copyDepthFrom(other)` is a `glBlitFramebuffer` of `GL_DEPTH_BUFFER_BIT` with `GL_NEAREST`. A depth blit is a GPU-side copy (no CPU stall) and costs roughly a full-screen memory pass (e.g. ~8 MB at 1080p D24S8/D32 per copy); one per frame is cheap on desktop GPUs, several per frame add up. It fails silently (GL error) if the formats/sizes differ — which is common between MC's depth and DH's depth texture — so merging by shader (sample both, take the nearer after linearising each with its own near/far) is more robust than blitting.
- For RafasConquest's merged depth (`/pvedepth`): the cheapest robust form is one full-screen pass per frame that samples MC depth + `getDhDepthTextureId()`, linearises both, and writes the merged result into the mod's own texture — no CPU readback, no extra blits. Re-fetch the DH texture ID on `DhApiAfterColorDepthTextureCreatedEvent` (window resize, DH settings changes) instead of every frame; cache it otherwise. Use DH's `getNearClipPlaneDistanceInBlocks` + DH's projection (from `DhApiRenderParam`) for linearisation, since DH's near/far differ from vanilla's.
- Anything that only needs to look right far away (distant navy sprites, siege camps, banners on far keeps) is a better fit for DH generic boxes than for a custom depth trick: DH handles depth, fog, SSAO and shader-pack integration for them. Box groups are cheap to move (origin change needs no `triggerBoxChange`). They are boxes only, so a ship would be a few coloured boxes (hull, sail), which is fine at DH distances.
- Occlusion queries (`GL_SAMPLES_PASSED`/`GL_ANY_SAMPLES_PASSED`) must be read a frame or more late (check `GL_QUERY_RESULT_AVAILABLE`) or they stall like `glReadPixels`.
- With Iris active, the shader pack owns the depth/colour targets; a custom full-screen pass that reads vanilla `getMainRenderTarget()` depth may read a different buffer than the pack. Detect Iris via `net.irisshaders.iris.api.v0.IrisApi` (reflectively, see section 5) and degrade (e.g. skip the merged-depth effect) when a pack is in use.

### Gaps
- Khronos wiki "Synchronization" page returned 403; LearnOpenGL not fetched. No measured cost numbers for depth blits on Minecraft-scale buffers found.
- The exact signature/contents of `DhApiRenderParam` (DH projection/modelview matrices, near/far) were not confirmed.
- No source on how Iris + DH exchange depth (Iris has a DH pipeline for shader packs) and whether a third-party pass can see DH depth while Iris is active.

---

## 5. Water/ocean waves (sea swell, foam, boats on waves)

### Takeaway
Do wave displacement in a vertex shader driven by uniforms (time, wave params) and a small waviness/foam texture — the way Physics Mod feeds shader packs — never by rebuilding chunk meshes. Foam belongs in the fragment shader from noise textures, not particles. Boats riding waves need the same wave function on the CPU (cheap, a few evaluations per boat per tick), not GPU readback.

### Cited Findings
- Physics Mod's ocean integrates with shader packs through uniforms and samplers. Complementary Reimagined's "Ocean Physics Support" water shader declares: `uniform int physics_iterationsNormal; uniform float physics_gameTime; uniform float physics_globalTime; uniform float physics_oceanHeight; uniform float physics_oceanWaveHorizontalScale; uniform float physics_rippleRange; uniform float physics_foamAmount; uniform float physics_foamOpacity;` plus `physics_waveOffset`, `physics_textureOffset`, `physics_modelOffset`, and samplers `physics_waviness` (2D), `physics_ripples` (2D), `physics_foam` (3D), `physics_lightmap` (2D). Displacement happens in the vertex shader: `physics_finalPosition.y = gl_Vertex.y + physics_waveHeight(gl_Vertex.xz, PHYSICS_ITERATIONS_OFFSET, physics_localWaviness, physics_gameTime)` with per-vertex waviness from `texelFetch(physics_waviness, ...)`. Height = weighted sum of rotated `exp(sin(x) - 1.0)` waves (constants e.g. `PHYSICS_ITERATIONS_OFFSET = 13`, `PHYSICS_FREQUENCY = 6.0`, `PHYSICS_SPEED = 2.0`, `PHYSICS_WEIGHT = 0.8`) scaled by `physics_oceanHeight`. Normals are computed per pixel by the same loop with `physics_iterationsNormal`, mixed with 4 ripple-texture taps. Foam = three `textureLod` samples of the 3D `physics_foam` noise at different scales/time offsets × `physics_foamAmount`, blended by wave height × `normal.y^4` and a smoothstep threshold × `physics_foamOpacity`. — [Complementary Reimagined r5.1.1 Ocean Physics diff](https://www.diffchecker.com/24hNvzCu)
- Physics Mod's author recommends ocean physics at render distance ≤12 chunks because higher values can cause large lag spikes, and recommends Iris + Sodium. — [Physics Mod Patreon post (v136)](https://www.patreon.com/posts/80007192)
- Some shader packs (e.g. Arc) advertise special support for Physics Mod waves — i.e. without pack support the effect is lost or degraded under Iris. — [Arc shader (Modrinth)](https://modrinth.com/mod/arc-shader)
- NeoForge `AddSectionGeometryEvent` (present in 1.21.1, with `SectionRenderingContext` giving per-layer vertex consumers, a region view and a pose stack) adds static geometry to chunk sections; registered renderers run on the chunk-rebuild thread, so data must be fetched in the handler. It is for static geometry baked into the section mesh, i.e. not for per-frame animation. — [AddSectionGeometryEvent.SectionRenderingContext (1.21.1 javadoc mirror)](https://aldak.netlify.app/javadoc/1.21.1-21.1.x/net/neoforged/neoforge/client/event/addsectiongeometryevent.sectionrenderingcontext); [AddSectionGeometryEvent (26.2 javadoc mirror)](https://aldak.netlify.app/javadoc/26.2.x/net/neoforged/neoforge/client/event/addsectiongeometryevent)
- In 1.21.1 the core shader type is `ShaderInstance` (renamed `CompiledShaderProgram` in 1.21.2, with the `ChunkOffset` uniform renamed `ModelOffset`). — [NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)

### Inferences
- Three ways to animate an ocean on 1.21.1 + Sodium, from best to worst:
  1. Own ocean surface mesh: a camera-following grid (e.g. a few hundred metres, denser near the camera) in a cached `VertexBuffer`, drawn from `RenderLevelStageEvent` (`AFTER_TRANSLUCENT_BLOCKS`/`AFTER_PARTICLES` for translucency) with a custom `ShaderInstance` (registered via NeoForge `RegisterShadersEvent`) that displaces `y` with the wave sum and computes normals/foam per pixel. Upload the grid once; per frame only set uniforms (time, camera offset, wave params, swell map texture). Sodium does not touch it. This is essentially what Physics Mod does for shader packs.
  2. Override vanilla's water in Sodium's terrain shader — fragile: Sodium has its own chunk shaders (it ignores vanilla's `rendertype_translucent` core shader), and under Iris the pack's `gbuffers_water` is used, so a resource-pack style core-shader override only works in pure vanilla.
  3. Changing block state or forcing section rebuilds to animate — never: each rebuild re-meshes 16³ sections off-thread and re-uploads, causing stutter.
- Under Iris, a custom ocean drawn with RafasConquest's own `ShaderInstance` will not get the pack's lighting/shadows/reflections and may be invisible or wrong in some packs (see the Forge forum report in section 1). Options: skip the custom surface and leave vanilla water when `IrisApi.isShaderPackInUse()` is true; or expose Physics-Mod-style uniforms — not realistic for a content mod. Plan a fallback: CPU-only swell for boats + vanilla water visuals under Iris.
- Foam: sample a tileable noise texture (2D or 3D) in the fragment shader, threshold by wave crest height/steepness and shoreline distance; zero particles. Shore distance can come from a small baked "calm map"/coast-distance texture uploaded once per region.
- Boats/Sable ships on swell: evaluate the same sum-of-waves function in Java at a few hull points per tick (and interpolate on render); never read heights back from the GPU. Keep the function and constants in one shared class so GPU and CPU agree.

### Gaps
- No Physics Mod source/docs found describing how it renders its own ocean when no shader pack is active (only the shader-pack uniform interface was seen).
- Whether Sodium on NeoForge honours `AddSectionGeometryEvent` was not confirmed (not needed for animated water, but relevant for static overlays).
- No source confirming Iris behaviour for mod `ShaderInstance` draws in specific stages on 1.21.1.

---

## 6. Sodium (and Iris) compatibility

### Takeaway
On NeoForge 1.21.1 Sodium (0.6+) ships FRAPI built-in, has a block-entity conditional-render API (for Flywheel), better compat for mods extending entity renderers and custom fluid renderers, and a Config API for options. What breaks Sodium's fast paths is mixing into vanilla chunk building/`LevelRenderer` chunk-layer code (Sodium replaces it) and relying on vanilla terrain shaders. Draw mod geometry through events and your own buffers, and detect Iris reflectively.

### Cited Findings
- Sodium 0.6.0 for NeoForge 1.21.1 notes: "The Fabric Rendering API (FRAPI) is now supported out of the box!" (Indium no longer needed); first multi-loader release; "Added an API for mods that need to conditionally render block entities (i.e. Flywheel.)"; "Improved compatibility with mods that extend the entity renderer"; "Improved compatibility with mods that use custom fluid renderers"; "Fixed compatibility with mods that render their block entities on a glowing render pass"; "Added support for modifying the alpha component of vertex colors in terrain rendering"; "Added support for the FREX Flawless Frame API on NeoForge"; new "Material Downgrading" optimization moves some terrain to a simpler pass; mod authors should report breakage on Sodium's tracker. — [Sodium mc1.21.1-0.6.0-neoforge](https://modrinth.com/mod/sodium/version/mc1.21.1-0.6.0-neoforge)
- Indium is not compatible with Sodium 0.6.0+. — [Indium (moddex)](https://moddex.gg/mod/indium)
- Sodium's Config API: implement `net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint`; registered via annotations on NeoForge; Sodium is on the CaffeineMC Maven. — [Sodium GitHub](https://github.com/CaffeineMC/sodium)
- Sodium requires launchers to use the LWJGL version Minecraft is designed for. — [Sodium wiki: LWJGL Compatibility](https://github.com/CaffeineMC/sodium/wiki/LWJGL-Compatibility)
- A Sodium 0.8.12 backport for 1.21.1 exists and its notes list compatibility with Create Aeronautics (1.3.0+), Sable, Veil and Iris at specific versions. — [Sodium mc1.21.1-0.8.12 release](https://newreleases.io/project/github/CaffeineMC/sodium/release/mc1.21.1-0.8.12)
- Iris detection without a hard dependency: use `net.irisshaders.iris.api.v0.IrisApi` via reflection; Oculus on Forge/NeoForge uses the same API class. — [Shader compatibility doc (third-party mod)](https://ansible-crafting-f28ed4.gitlab.io/shader-compatibility)
- Veil (a rendering library Sodium lists as compatible) exposes an `IrisCompat` with `areShadersLoaded`, null when Iris is absent — an example of the null-check pattern. Mods built on Veil need a separate "Iris Veil compat" mod to render right under shader packs. — [Veil IrisCompat javadoc](https://foundrymc.github.io/Veil/foundry/veil/api/compat/IrisCompat.html); [Iris Veil Compat (CurseForge)](https://www.curseforge.com/minecraft/mc-mods/iris-veil-compat)
- Iris's stated goal is best-effort compatibility with modded environments. — [Iris guide (GitHub)](https://github.com/IrisShaders/Iris/blob/1.19.4/docs/guide.md)

### Inferences
- (code knowledge) Sodium replaces `LevelRenderer`'s chunk compile/render path (`SectionRenderDispatcher`, `renderSectionLayer`, `ChunkRenderDispatcher` on older versions) and the terrain shaders; mixins into those vanilla methods silently do nothing or crash with Sodium. NeoForge events (`RenderLevelStageEvent` stage callbacks, `RenderLivingEvent`, `RegisterShadersEvent`) keep firing under Sodium because Sodium on NeoForge calls them.
- Block entity renderers (BERs) are drawn by Sodium through vanilla's dispatcher each frame — fine for a few, expensive for many (every BER re-tessellates every frame). For lots of static decorative blocks (signposts, banners on many holds), prefer baked block models (meshed once into the chunk) over BERs; reserve BERs for animated parts. `AddSectionGeometryEvent` is the NeoForge-native way to add static custom geometry to chunk meshes (Sodium support unconfirmed).
- Custom baked models: use standard `BakedModel`/`IDynamicBakedModel` + `ModelData`; Sodium handles those. Avoid per-quad random calls that are not thread-safe — chunk meshing runs on worker threads.
- RafasConquest's new mixins (`ChunkMapCameraMixin`, `TrackedEntityCameraMixin`) target server-side `ChunkMap`/tracking, not Sodium's client path, so they should not conflict with Sodium; client-side mixins into `LevelRenderer`/`GameRenderer` deserve a Sodium + Iris test.
- Treat "Iris pack active" as its own mode: custom shaders (ocean, merged depth, map-in-world effects) either disable or fall back. Check once per frame via a cached reflective `IrisApi.getInstance().isShaderPackInUse()` call.

### Gaps
- Sodium's GitHub wiki has no dedicated "mod developer compatibility" page (search found only Config API, nightly builds, LWJGL pages); the internal Sodium vertex-writer API (`VertexBufferWriter`) and the block-entity culling API names were not confirmed from source.
- Exact Iris 1.21.1 API method names (`isShaderPackInUse`, `isRenderingShadowPass`) could not be fetched (repo path 404); they are widely used but unverified here.
- No confirmation of whether Sodium/Embeddium invoke `AddSectionGeometryEvent` on 1.21.1.
- Embeddium (NeoForge Sodium fork) docs were not reviewed; on 1.21.1 most packs now use official Sodium for NeoForge.

---

## 7. Client tick, stutter and sound

### Takeaway
Stutter comes from main-thread spikes, not average cost: decode/bake big packets and map data off-thread and swap results in on the render thread; spread GPU uploads over frames; avoid per-frame allocation in render paths. Vanilla's sound engine caps at ~247 simultaneous sounds and 8 streaming sounds, so ambient city sounds and place music must be budgeted.

### Cited Findings
- FTB Chunks moves heavy work off-thread: map data loads and image baking run on `FTBChunksClient.MAP_EXECUTOR`; results are handed back with `Minecraft.getInstance().execute(...)` for the GPU upload; data publishing takes a lock only to swap the result in; `volatile` flags coordinate threads. — [FTB Chunks MapRegionTexture.java](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegionTexture.java); [FTB Chunks MapRegion.java](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegion.java)
- Entity Culling runs its visibility work on spare CPU threads without blocking the main thread, only collecting data on the main thread every few ticks. — [EntityCulling GitHub](https://github.com/tr7zw/EntityCulling)
- Vanilla's sound engine is capped at 247 simultaneous sounds; when exceeded the client stalls and logs repeated "Maximum sound pool size 247 reached" warnings; other sounds stop until slots free. — [MC-180398](https://bugs-legacy.mojang.com/browse/MC-180398); [Audio Engine Tweaks](https://www.curseforge.com/minecraft/mc-mods/audio-engine-tweaks)
- Minecraft limits streaming sounds to eight at once (jukeboxes, ComputerCraft speakers, etc.). — [Soundlimit mod](https://www.modpackindex.com/mod/145934/soundlimit)
- Audio Engine Tweaks changes how sounds are scheduled to stop the pool filling; Adjustable Sound Limit raises the cap (e.g. ~247 → 1024) but the OpenAL driver/hardware may still cap it; Raise Sound Limit Simplified allows a configurable static/streaming split. — [Audio Engine Tweaks](https://www.curseforge.com/minecraft/mc-mods/audio-engine-tweaks); [Adjustable Sound Limit](https://modrinth.com/mod/adjustable-sound-limit/version/76QMq242); [Raise Sound Limit Simplified](https://awesome.ecosyste.ms/projects/github.com%2Frelativitymc%2Fraise-sound-limit-simplified)
- Physics Mod's ocean at high render distance "can cause large lag spikes" — an example of a world-scale client effect needing a distance cap. — [Physics Mod Patreon post](https://www.patreon.com/posts/80007192)

### Inferences
- (code knowledge) NeoForge payload handlers run on the network thread by default in 1.21.1 only if declared so; `IPayloadContext.enqueueWork(...)` runs on the main thread. Pattern for big sync packets (map/territory/title data): decode the `FriendlyByteBuf` into plain arrays/records in the handler (network thread), build derived data (e.g. tile images) on a worker executor, and `enqueueWork` only the cheap swap (assign the new reference, mark tiles dirty). Never bake textures or rebuild VertexBuffers inside the packet handler.
- GPU uploads (`DynamicTexture.upload`, `VertexBuffer.upload`) must happen on the render thread; queue them and cap per frame (e.g. N tiles or N KB per frame) so a big map refresh spreads over several frames instead of one spike.
- Per-frame allocation: reuse `Matrix4f`/`Vector3f`, `BlockPos.MutableBlockPos`, and a persistent `ByteBufferBuilder`/`BufferBuilder` for dynamic geometry; avoid streams/lambdas/boxing in render loops over hundreds of soldiers or map icons. Allocation churn at 60–144 fps turns into young-gen GC pauses. Java 21's default G1 handles short-lived garbage well but still pauses; ZGC (`-XX:+UseZGC -XX:+ZGenerational`) is the usual pack recommendation for pause-sensitive clients (no source fetched).
- Ambient sounds budget: one looping ambient bed per city (a single `AbstractTickableSoundInstance` whose volume/position follow the player's distance/district), not one source per stall/NPC; place music as one streaming instance (`stream=true` in sounds.json) at a time — it occupies one of the 8 streaming slots. Cross-fade by adjusting volume in `tick()` instead of starting overlapping instances. Short one-shot sounds in big battles (hundreds of soldiers swinging) should be thinned client-side (e.g. play only the nearest N per tick) to stay well under 247.
- Keep `SoundManager.isActive(instance)` checks before re-playing loops; restarting a loop each tick leaks channels.

### Gaps
- No primary source (vanilla code page) fetched for the 247 static / 8 streaming split derivation; numbers come from Mojang bug reports and mod pages.
- No source fetched on NeoForge 1.21.1 payload threading defaults or on GC tuning for Minecraft.
- No measured data on texture upload costs per MB in Minecraft.

---

## 8. Lessons from performance mods that a content mod can apply

### Takeaway
Most perf mods win by the same few moves: do expensive work once and cache it (baked meshes, baked textures, sign text buffers), batch many small draws into few (ImmediatelyFast, map atlas), do visibility/LOD on spare threads (Entity Culling), and degrade gracefully at distance (DH LODs, EMF animation LOD). A content mod can apply all of these to its own render code without depending on the mods.

### Cited Findings
- ImmediatelyFast: batching immediate-mode draws; packing map textures into one atlas (`map_atlas_generation`); buffering sign text instead of re-rendering each frame (`experimental_sign_text_buffering`); caching per-character text lookups (`fast_text_lookup`); avoiding redundant framebuffer switches (`avoid_redundant_framebuffer_switching`); optional disabling of GL error checking. — [ImmediatelyFast GitHub](https://github.com/RaphiMC/ImmediatelyFast)
- Entity Culling: async CPU occlusion for entities and block entities, tick culling of hidden entities, config whitelist for out-of-bounds renderers, "leaves as solid" option for forests. — [EntityCulling GitHub](https://github.com/tr7zw/EntityCulling)
- Sodium 0.6: material downgrading (move terrain to simpler passes), improved occlusion culling, translucency improvements, block-model optimisations, FRAPI built in. — [Sodium mc1.21.1-0.6.0-neoforge](https://modrinth.com/mod/sodium/version/mc1.21.1-0.6.0-neoforge)
- Flywheel: GPU instancing for entities/block entities with custom instance formats and shaders; disabled under shader packs without a compat mod. — [Flywheel GitHub](https://github.com/Engine-Room/Flywheel); [Iris Flywheel Compat](https://modrinth.com/mod/iris-flw-compat)
- Distant Horizons: colours baked into LOD geometry (re-generate only via `clearRenderDataCache`), deferred transparent pass option, generic box objects with SSAO/lighting inside DH's pass, and a full set of render-phase events. — [IDhApiRenderProxy](https://distant-horizons-f74d0f.gitlab.io/com/seibel/distanthorizons/api/interfaces/render/IDhApiRenderProxy.html); [DH API classes](https://distant-horizons-f74d0f.gitlab.io/allclasses-index.html)
- FTB Chunks: per-region baked textures, off-thread bake + main-thread upload, lazy creation, stale-region reaping, failure retry backoff. — [FTB Chunks MapRegionTexture.java](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegionTexture.java)
- EMF: distance-based animation LOD scaled by entity size. — [EMF changelog](https://modrinth.com/mod/entity-model-features/changelog?page=18)

### Inferences
- Applied checklist for RafasConquest's client code (ordered by likely payoff):
  1. Strategy/faction map: tile textures baked off-thread, dirty-tile re-bake, capped uploads per frame, icons from one atlas in one draw, labels in one `drawManaged` block, zoom-based label/icon culling.
  2. Battle armies: impostor billboards beyond a distance (one draw for all), skip name tags/armor/shadows/animation at mid range, rely on `entityDistanceScaling` + server tracking range as hard caps; ensure oversized entities (ships) have correct culling boxes.
  3. Naval LOD: cached `VertexBuffer` per ship class for the LOD mesh; DH generic boxes or billboards for far ships; no per-frame tessellation of whole ships.
  4. Sea swell: shader-side displacement over a cached grid; same wave function on CPU for boats; no chunk rebuilds; no foam particles; Iris fallback.
  5. Depth merge: shader-side merge sampling `getDhDepthTextureId()`; refresh the ID on DH's texture-created event; no CPU readback; skip under Iris packs.
  6. Overlays (`BattleSpotRing`, survey/siege marks): cached buffers rebuilt on change only; draw at `AFTER_TRIPWIRE_BLOCKS`/`AFTER_PARTICLES` if translucent.
  7. Packets/sync: decode off-thread, swap on main.
  8. Sounds: one bed per city, one music stream, thin battle one-shots.
- What is NOT applicable / flagged:
  - Nvidium (NVIDIA mesh-shader terrain renderer) is a Fabric-only Sodium add-on and not a pattern a content mod can use (no source fetched this session).
  - Anything using `GpuBuffer`, `RenderPipeline`, `CompiledShaderProgram`, `EntityRenderState`, `drawSpecial`, or `RenderType`-function blits is 1.21.2+/1.21.5+ and does not apply to 1.21.1. — see [NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2), [Fabric #4566](https://github.com/orgs/FabricMC/discussions/4566)
  - FerriteCore (memory dedup of block states) and ModernFix (dynamic resources, faster launch) help memory/startup, not frame time; a content mod's lesson from them is "intern/share immutable data, don't hold per-instance copies" (no source fetched this session).

### Gaps
- ModernFix, FerriteCore, More Culling, Nvidium and Embeddium sources/docs were not fetched in this session; claims about them above are unsourced and kept in Inferences.
- No benchmark numbers were found for any technique on 1.21.1 specifically (ImmediatelyFast's published benchmarks are on 1.19.3).
- JourneyMap and Antique Atlas tiling code not reviewed.
