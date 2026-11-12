# RafasConquest client-side performance audit (rendering, screens, client tick, sound)

Scope: the mod's own client code on branch `port-1.21.1-neoforge` (working tree as of 2026-11-10, including uncommitted edits). Paths are relative to `src/main/java/rafasconquest/` unless they start with `src/` or `MC:`. `MC:` means the decompiled NeoForge 21.1.265 / MC 1.21.1 sources at `build/moddev/artifacts/neoforge-21.1.265-sources.jar`, read locally. Nothing was built or run. Every cost estimate is labelled an inference.

Method: `graphify query` to find the client event hooks, then reading every file in `client/`, `client/map/` and `mixin/`, plus the shared code they call each frame (`core/Swell`, `core/SeaCalm`, `bridge/Sea`, `battle/FormationSnapshot`, `battle/DistantDepth`, `style/ShipHulls`, `style/StyleLibrary`, `core/SeaLanes`) and the shader assets.

## 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?

### Takeaway
All world-space drawing happens in one `RenderLevelStageEvent` handler, in two stages. Everything drawn there (army figures, ships, arrow volleys, rings, beams) is rebuilt in immediate mode every frame through the shared `Tesselator`, with no frustum culling and per-figure light lookups. The cost scales with the number of distant soldiers, arrows and ships. The worst single item is at `DistantShips.Far.parts()`: for each sea-lane ship drawn as a 3D level of detail, it does a directory listing and a file stat on disk every frame.

### Cited Findings
**Hooks registered (complete list)**
- `client/ClientEvents.java:88-102` `onRenderLevel(RenderLevelStageEvent)` is the only world-render hook. At `AFTER_SKY` (lines 90-92) it calls `WaterSwell.prepare()` and `DistantArmyRenderer.prepare()`. At `AFTER_TRIPWIRE_BLOCKS` (lines 93-101) it calls `DistantOcclusion.begin()`, `DistantArmyRenderer.render()`, `DistantArmyRenderer.renderShips()`, `DistantOcclusion.end()`, `ArrowVolleys.render()`, `BattleSpotRing.render()` and `BattleScreen.renderSelection()`. The handler runs once per stage, about 15 times a frame, but does work in only these two.
- `client/ClientEvents.java:129-138` `RenderLivingEvent.Pre` runs once for every living entity rendered, every frame (see Q6).
- `client/ClientBarks.java:46` `RenderGuiEvent.Post` runs every frame and returns at once when no bark is queued. Otherwise it does 1 fill and 2 `drawString` calls.
- `client/ClientEvents.java:123-126` `ClientTickEvent.Post` calls `BattleSounds.tick()`. `client/PlaceMusicClient.java:94` `SelectMusicEvent` runs every client tick.
- `client/ClientModEvents.java:23-26` registers two core shaders (`distant_depth_merge`, `distant_depth_view`). Lines 28-34 register entity renderers (Townsperson, Recruiter) and block-entity renderers (Marker, Gate). Lines 37-43 add `BackBannerLayer` and `MouthLayer` to every Recruits `RECRUIT` renderer.
- Distant Horizons hook: `client/DistantHorizonsArmies.java:155-165` binds `DhApiBeforeApplyShaderRenderEvent`. Every frame it copies DH's projection with `getValuesAsArray()`, which allocates one `float[16]`.
- Sodium hooks: `mixin/SodiumShaderInterfaceMixin.java:14-17` and `mixin/SodiumShaderLoaderMixin.java:15-20` (see Q4/Q5). A vanilla shader override ships at `src/main/resources/assets/minecraft/shaders/core/rendertype_translucent.{vsh,fsh,json}`.
- No client code anywhere uses a `Frustum`: a search of `client/*.java` for `Frustum|isVisible|getFrustum` returns nothing. NeoForge does expose one: `MC: net/neoforged/neoforge/client/event/RenderLevelStageEvent.java:120` `getFrustum()`.

**Distant army figures: `client/DistantArmyRenderer.java`**
- `render()` (lines 90-132) runs every frame while a battle snapshot exists. It starts a fresh `Tesselator` buffer (line 106). For every formation it calls `formation.place(positions)` (line 111), which allocates a `List<Sprite>` and one `Sprite` per soldier, and for client-known soldiers also a `Vec3` and a `double[3]` (`battle/FormationSnapshot.java:151-174`, `client/DistantArmies.java:109-115`). For every soldier past the handover it calls `lightAt()` (lines 290-296: `BlockPos` allocation, `hasChunkAt`, `LevelRenderer.getLightColor`, which reads the block state and both light layers). It then emits either a flat sprite (4-6 rects = 16-24 vertices) or a 3D level of detail (`SoldierFigure.build`, 4-7 boxes × 5 quads = 80-140 vertices).
- Soldiers behind the camera are built and uploaded too, since there is no culling. Upload: `finish()` (lines 271-284) → `BufferUploader.drawWithShader`, which re-specifies the immediate vertex buffer with `glBufferData` on every draw (`MC: com/mojang/blaze3d/vertex/VertexBuffer.java:129`).
- `Figure.quad(int, float, float...)` (lines 430-437) is varargs. Each of the 5 calls per box (lines 423-427) allocates a `float[12]`, unless escape analysis removes it. `SoldierFigure.build` allocates one `SoldierFigure` per figure (`client/SoldierFigure.java:35-37`).
- `renderShips()` (lines 138-177): `DistantShips.all()` recomputes every sea-lane ship in the world every frame (`client/DistantShips.java:77-104`, `core/SeaLanes.java:110-123`, which synchronizes on each port). It matches each one to every real Sable sub-level, O(ships × sub-levels) (`client/DistantShips.java:107-115`). `SableSeen.ships()` allocates a `Quaterniond`, a `Vector3d` and a `double[]` per sub-level per frame (`client/SableSeen.java:30-55`).
- **Disk I/O on the render thread, every frame:** `shipBoxes()` (line 184) calls `ship.parts()`. For a sea-lane ship this is `ShipHulls.of(lane)` (`client/DistantShips.java:52-55`) → `StyleLibrary.pickIn` (`style/ShipHulls.java:47`) → `choose()` → `files(folder)`. `files()` calls `Files.isDirectory` and `Files.list(folder)`, then sorts and streams the result (`style/StyleLibrary.java:324-335`, `402-409`). `ShipHulls.of` then calls `StyleLibrary.template(file)` → `read()`, which calls `Files.getLastModifiedTime(file)` (`style/StyleLibrary.java:413-416`, `438-441`). So every frame, every sea-lane ship that is not shown as a real Sable ship and lies within `SPRITE_PAST` = 2000 blocks (`client/DistantShips.java:29`) costs one directory listing and one stat syscall under `mods/RafasConquest/structures` (`style/StyleLibrary.java:81-83`). Battle ships use an in-memory `ConcurrentHashMap` (`style/ShipHulls.java:89-97`) and are cheap.
- The `HULLS` `IdentityHashMap` (`client/DistantArmyRenderer.java:201`) caches each hull's shape, so the per-hull face extraction (lines 209-260) runs once per hull list.

**Arrow volleys: `client/ArrowVolleys.java`**
- Up to `MAX_IN_FLIGHT` = 3000 arrows (line 39). `render()` (lines 100-154) runs every frame. Per arrow it allocates about 30-40 `Vec3`s: `at(u)` twice, `subtract`, `heading`, `normalize` twice, `cross`, three `scale`s, and 8 or more per `streak()` × 3 (lines 157-168). It calls `lightAt()` (lines 175-181) for a `BlockPos` and a light lookup, and emits 12 vertices.
- Expired arrows are removed with `Iterator.remove()` on an `ArrayList` (line 115), which is O(n) per removal, so a mass expiry is O(n²) in one frame.
- No frustum culling.

**Rings and beams: `client/BattleSpotRing.java`, `client/BattleScreen.java`**
- `BattleSpotRing.ring()` (lines 136-156) and `strip()` (lines 103-130) each set the shader and blend state and issue their own `Tesselator` begin and `drawWithShader`. That is one draw call per ring of 66 vertices.
- `BattleScreen.renderSelection()` (`client/BattleScreen.java:836-862`) calls `ring()` once for every selected soldier, every frame, while the battle screen is open. 300 selected soldiers make about 300 draw calls a frame.
- `BattleSpotRing.render()` (lines 72-100) draws up to two beacon beams through the shared buffer source and calls `endBatch()` after each (lines 85, 93). That is negligible.

### Inferences
- Rough cost, assuming 2,000 far soldiers at the 3D level of detail and about 100 vertices each: ~200k vertices × 20 bytes (POSITION_COLOR_LIGHTMAP) ≈ 4 MB built and uploaded per frame, ~240 MB/s at 60 fps. Add 2,000 light lookups and ~10k small allocations. That is likely several milliseconds of render-thread time per frame in a big battle. The Tesselator's 786,432-byte buffer (`MC: com/mojang/blaze3d/vertex/Tesselator.java:9`) grows by reallocation and never shrinks (`MC: com/mojang/blaze3d/vertex/ByteBufferBuilder.java:16,41-53`).
- Candidate fixes, in order of cost-benefit:
  1. Frustum-cull each formation by a bounding circle (centre plus max(halfWidth, halfDepth)) with `event.getFrustum()`. This needs no restructuring and typically skips half or more of the formations.
  2. Compute light once per formation (at its centre) or once per tick, not once per figure.
  3. Give `Figure.quad` a fixed-arity signature (or write the 4 vertices inline) to drop the varargs arrays.
  4. Iterate soldier positions with a callback instead of building a `List<Sprite>` per formation per frame.
  5. Longer term: cache each formation's figure mesh in a `VertexBuffer` in formation-local space and redraw it with a model matrix. It only needs rebuilding when a 1 Hz snapshot arrives, not every frame. This conflicts with drawing real-position soldiers individually, so only do it for "numbers-only" blocks.
- Ship disk I/O: memoize `ShipHulls.of(lane)` per ship id in `DistantShips`, or cache `StyleLibrary.files(folder)` and `choose()` results until the style library reloads. Either is a one-line-scale change. This is likely the cheapest large win on any client with many sea lanes in view. Windows directory listing plus stat, possibly scanned by Defender, is commonly tens to hundreds of microseconds per call, so 10-20 ships could cost milliseconds per frame. This is an estimate, not measured.
- Arrows: compute in doubles without `Vec3`, remove expired arrows with one `removeIf` (or swap-remove) per frame, compute light once per volley, and frustum-cull each arrow by its head position.
- Selection rings: write every ring into one buffer and draw once. The shared helper `BattleSpotRing.ring` already builds vertices; it needs a variant that takes a `BufferBuilder`.

### Gaps
- Real counts of formations, soldiers, arrows and lane ships in a typical battle, and timings, need an in-game profile (Spark or a Tracy/NSight capture). Nothing was run.
- Whether HotSpot C2 scalar-replaces the `quad(...)` varargs arrays was not verified.

## 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?

### Takeaway
The heavy raster layers (parchment base, territory, title borders) are cached `DynamicTexture`s built once per screen open. Every mark drawn over them is redrawn every frame with `GuiGraphics.fill()`. On MC 1.21.1 each `fill()`, `drawString()` and `renderItem()` flushes the whole buffer source, which means one draw call and one buffer upload per rectangle. The map's per-frame draw-call count therefore scales with the number of on-screen settlements, seats, dojos, wars, armies, storm-wall dashes and letters. The codebase already has the fix pattern in `RoadLayer`: write every quad into one buffer and flush once.

### Cited Findings
**Vanilla 1.21.1 behaviour (local source)**
- `MC: net/minecraft/client/gui/GuiGraphics.java:300-320`: `fill(...)` ends with `this.flushIfUnmanaged()`. Lines 96-100: `flushIfUnmanaged()` calls `flush()` unless `managed`. Lines 125-129: `flush()` calls `bufferSource.endBatch()`.
- The same applies to `drawString(...)` (lines 473-492; flush at 491 and 541) and `renderItem(...)` (`flush()` at about line 1296).
- `renderOutline` is 4 `fill`s (lines 670-675).
- `drawManaged(Runnable)` (lines 87-93, `@Deprecated`) suspends the per-call flush.
- `blit` → `innerBlit` (line 953 onward) draws immediately through its own `Tesselator`.

**Cached (good)**
- `client/map/MapContext.java:37-55` builds the parchment, territory and title-border images once, in the constructor, and registers them as `DynamicTexture`s (lines 86-90). `close()` (lines 98-102) releases them. Re-registering a location closes the old texture (`MC: net/minecraft/client/renderer/texture/TextureManager.java:62-74`), so nothing leaks.
- `BaseMapLayer.drawAligned` (`client/map/BaseMapLayer.java:39-47`) draws each layer with a single `blit`.
- `RoadLayer.render` (`client/map/RoadLayer.java:27-64`) already writes every road segment into one `RenderType.gui()` buffer and flushes once (line 63, with the comment "Every road's stretches go into one buffer and are drawn in a single call").

**Rebuilt every screen open (not cached across opens)**
- `FactionMapScreen.tryBuild` (`client/map/FactionMapScreen.java:96-109`) creates `new MapContext(...)`. That runs `PoliticalMap.build` (`map/PoliticalMap.java`: several full-image passes, namely `parchment`, `holders`, `neighbours` returning a `Set<Long>`, `territory`, `edgeDistance` and `labelShapes`, in `map/PoliticalStyle.java:43-266`) plus `TitleStyle.borders`. It then copies three images pixel by pixel with `setPixelRGBA` (`client/map/MapContext.java:76-84`).
- The base map is at most 1024×1024 pixels (`map/BaseMapGenerator.java:38,128-129`), so this is up to 3 × 4 MB of native images and 3 × 4 MB of VRAM. `removed()` (`client/map/FactionMapScreen.java:219-226`) throws it all away when the map closes, so every reopen rebuilds it on the render thread.

**Per-frame fill and flush volume**
- `TitleLayer.render` (`client/map/TitleLayer.java:33-191`):
  - each seat city: 1 fill + outline (4 fills) = 5 flushes (lines 39-51)
  - each settlement (zoom ≥ 0.05): 1-6 fills (lines 56-69, `countryMark` 276-304)
  - each dojo: 11 fills (lines 80-84)
  - each war site: 40 fills (lines 98-105)
  - each feud clash: 24 fills (lines 121-128)
  - each army: about 14-20 fills (`armyTent` 231-239, `armyOnTheMarch` 245-274)
  - each seated province: 2 fills (lines 166-167)
  - All are culled to the viewport, but each fill is its own flush.
- `BaseMapLayer.drawStormWall` (`client/map/BaseMapLayer.java:26-36`) draws up to 360 single-dash `fill`s whenever the ring is on screen. `TerritoryLayer.render` calls it every frame (`client/map/TerritoryLayer.java:22`).
- `LabelLayer.drawEmpire` (`client/map/LabelLayer.java:78-111`) draws empire names one letter at a time (lines 105-109). Each letter goes through `font.drawInBatch8xOutline`, which draws 8 outline copies plus the glyph, then `g.flush()` (lines 131-135). That is one flush per letter, plus a `Component.literal(...).getVisualOrderText()` and a `substring` allocation per letter.
- `PoiIconLayer.render` (`client/map/PoiIconLayer.java:27-43`) calls `g.renderItem` (line 67) once per farm (zoom ≥ 0.12), city and capital icon, every frame. Each call renders a banner or wheat item model, then flushes. Banner items draw through the banner block-entity-without-level renderer, with its pattern layers. Framed icons add 5 more fill flushes (lines 61-62).
- `UnitLayer.render` (`client/map/UnitLayer.java:22-33`) does one `blit` per loaded player, which is small.
- `RoadLayer.render` (`client/map/RoadLayer.java:31-32`) copies the whole road list into a new `ArrayList` every frame and walks every segment of every road (lines 36-61), projecting both endpoints, before the on-screen check.
- `FactionMapScreen.renderLegend` (`client/map/FactionMapScreen.java:146-169`) rebuilds the legend every frame: each layer's `legend()` allocates new `LegendEntry`s, lambdas and `Component.translatable`s (for example `client/map/TitleLayer.java:345-367`). It measures every label with `font.width` (line 154) and draws about 20 symbols, many of them several fills.
- `FactionMapScreen.render` (line 135) calls `String.format("%,d, %,d")` every frame while the mouse is over the map.
- `GrandStrategyScreen` (`client/map/GrandStrategyScreen.java:92-110`) adds to all of the above:
  - `owned()` streams and filters every city on each call (lines 63-66), twice per frame
  - `renderPanel` word-wraps every row with `font.split` twice per frame (lines 119-120 and 128)
  - `renderSummary` calls `String.format` (line 142)

**Mouse move and zoom**
- Pan and zoom only change `MapView` fields (`client/map/MapView.java:300-312`, `client/map/FactionMapScreen.java:193-209`). Nothing is rebuilt, which is good.
- Hover tooltips build strings only for the hovered item (`client/map/TitleLayer.java:173-190`).
- `PlaceMenuLayer.nearest` builds a list of every place on the map (`client/map/PlaceMenuLayer.java:96-138`), but only on a click.

**Other screens with per-frame fill loops**
- `client/BattleScreen.java:489-492`: the player-portrait disc and ring are drawn row by row (`disc` 512-517 and `ring` 519-530, one or two fills per pixel row). With `ringR()` = max(18, width × 0.04) (line 364), that is roughly 70-150 fills, and so flushes, per frame.
- `client/Portraits.java:102-118` (`diamond`, `circle`) do the same row-by-row filling, used by `CharacterScreen` (for example `client/CharacterScreen.java:233,260,332`).
- `CourtView.render` (`client/CourtView.java:226-298`) runs every frame under `CharacterScreen` and `NationScreen`. It calls `BlockRenderDispatcher.renderSingleBlock` for every block of the court hall (lines 246-252). That is about 1,200 blocks by `CourtSet.hall`/`row` (`client/CourtSet.java:83-118`): a 31×19 floor, a 31×12 back wall, pillars, carpet and podiums. Each block emits all faces with no neighbour culling. The pass then renders up to 21 recruit entities, with armour (lines 258-293).

### Inferences
- In a populated world zoomed in, TitleLayer, the storm wall, labels and banner icons together plausibly reach hundreds to a few thousand flushes per frame. Each flush is a buffer build, `glBufferData` and draw, so the map screen can easily cost more frame time than the world behind it. This is an inference; counts depend on world data.
- Fix with the house pattern already in `RoadLayer`: add a tiny `rect(VertexConsumer, Matrix4f, x0, y0, x1, y1, argb)` helper. Have TitleLayer, the storm wall, BattleScreen `disc`/`ring` and `Portraits.circle`/`diamond` write into `g.bufferSource().getBuffer(RenderType.gui())`, then call one `g.flush()` per layer. Remove the per-letter `g.flush()` in `LabelLayer.draw` and flush once after the layer.
- Wrapping a whole layer in `g.drawManaged(...)` also works, but it is deprecated and reorders fills after immediate `blit`s and items. Per-layer flushing keeps the layer order.
- Cache the legend list, its widths and the GrandStrategy panel's wrapped lines (rebuild on resize or data change). Cache `owned()`.
- For banner icons: draw a cached coloured square at far zoom, or render each faction banner once to a small texture.
- Keep `MapContext`, or at least its `PoliticalMap` and images, alive across opens, keyed by `BaseMap` identity plus factions plus a politics version. Or build it on a worker thread and upload when ready. That removes the open-time hitch.
- CourtView: bake the static hall into a `VertexBuffer` once per `CourtSet` (it never changes while the screen is open) and draw it with the pose each frame. Only the 21 figures need per-frame rendering.
- The world keeps rendering behind the opaque full-screen map (`client/map/FactionMapScreen.java:120` fills the whole screen). Skipping it would need a GameRenderer mixin, which is not recommended as a first step.

### Gaps
- Counts of settlements, dojos, wars, armies and factions in real save data were not checked, so the flush totals are not quantified.
- The cost of `PoliticalMap.build` plus `TitleStyle.borders` per open was not measured.

## 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?

### Takeaway
`DistantOcclusion` never reads back to the CPU (no `glReadPixels`) and only reallocates its depth texture on resize. It is skipped when no figure was drawn the previous frame, under a shader pack, or with `/pvedepth off`. When active, it adds per frame: one `glGetInteger` query (a possible driver sync point), a framebuffer re-attach plus completeness check, and one full-screen merge pass at native resolution writing `gl_FragDepth`. It turns on whenever any figure was drawn last frame, not only when DH terrain could hide one.

### Cited Findings
- Skip conditions in `client/DistantOcclusion.java:97-122`:
  - mode OFF (line 101)
  - no figures last frame (line 105: `lastFarthest <= 0`; `farthest` is fed by `scale()` at lines 91-94 for every figure drawn, at any distance)
  - shaders missing (line 109)
  - Iris shader pack (lines 113 and 243-257)
  - main framebuffer not bound (line 119)
- Per active frame:
  - `GL11.glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)` (line 119)
  - framebuffer bind and color re-attach (lines 124-125)
  - `glCheckFramebufferStatus` (line 126)
  - 6 uniform/sampler sets
  - viewport, color-mask and depth state changes (lines 146-153)
  - one full-screen quad through the merge shader, built with `Tesselator` and `drawWithShader` (lines 151 and 226-240; allocates a `() -> shader` lambda at line 232)
  - `glBindSampler` per unit
  - projection backup and restore (lines 155-156 and 167)
- The merge shader `src/main/resources/assets/rafasconquest/shaders/core/distant_depth_merge.fsh` does, per pixel: 2 depth-texture reads and up to 2 × (mat4 × vec4 inverse + mat4 × vec4 forward), then writes `gl_FragDepth` with depth func ALWAYS.
- Depth texture: `GL_DEPTH_COMPONENT32F` at window size, reallocated only when the size changes and the old one released (`client/DistantOcclusion.java:207-218`). Framebuffer and sampler are created once (lines 197-206). No `glReadPixels`, `glMapBuffer` or `glFinish` anywhere in the client package.
- `shaderPack()` (lines 243-257) calls `ModList.isLoaded("iris")` and 2 cached reflective `Method.invoke`s every frame.
- The DH side (`client/DistantHorizonsArmies.java:172-198`) is called once per active frame. It gets DH's depth texture id and inverts a `Matrix4f`, allocating a result `float[3]`.
- `battle/DistantDepth.java:23-31` is pure arithmetic (the squeeze used when occlusion is off).
- NVIDIA developer forum guidance: "in generally any sort of glGet* call will cause a synchronization point" between the app and driver threads under threaded optimization — [NVIDIA forums](https://forums.developer.nvidia.com/t/app-runs-only-with-gl-threaded-optimizations-1-whats-it-do/28873). An X.Org talk on threaded GL dispatch lists avoiding synchronizing calls as the ideal — [XDC2014 slides](https://www.x.org/wiki/Events/XDC2014/XDC2014MonakovLibrary/tangl_mangl.pdf). Both sources are anecdotal or vendor-forum level.

### Inferences
- GPU cost of the merge is one full-resolution pass: about 3.7M fragments at 1440p, each with 2 texture fetches and some mat4 math. That is likely about 0.1-0.5 ms on a mid-range GPU (estimate). The CPU cost is small apart from the `glGetInteger` sync risk on NVIDIA's threaded driver.
- Candidate fixes:
  1. Replace the `glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)` check with a comparison against what Minecraft last bound (`main.frameBufferId`, or GlStateManager's cached binding).
  2. Re-attach the color texture and check completeness only when `main.getColorTextureId()` or the size changes.
  3. Run `begin()` only when last frame's farthest figure was beyond the vanilla render distance or past DH's fade start. Nearer figures are already hidden correctly by the game's own depth.
  4. Optionally scissor the merge to the screen rectangle that contains the figures.
- No program-switch problem beyond the one merge shader and the POSITION_COLOR_LIGHTMAP shader used by the figures.

### Gaps
- Not verified on AMD or Intel drivers, or whether NVIDIA's threaded optimization is on for the user's machine.

## 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?

### Takeaway
The wave displacement is entirely GPU-side. Sodium's terrain vertex and fragment shaders are patched at load, and a vanilla translucent-shader override covers the non-Sodium path, so chunk meshes are never rebuilt for waves. The CPU costs are elsewhere:
1. Each time Sodium binds its terrain shader (once per pass), the uniform upload does a `glGetInteger` plus 12 `glGetUniformLocation` string lookups, recomputes 48 wave phases and allocates.
2. A 512×512 "calm" texture is rebuilt on the render thread from block reads over a 578×578-column window. That happens every 600 ticks and every 64 blocks of camera movement. Cached chunks expire after 600 ticks, so roughly every other refresh (about every 60 s standing still) re-reads every loaded column, at sea up to 33 block and fluid reads each. This is a likely periodic stutter near oceans.
3. A one-time CPU generation of a 256×256×16 3D foam-noise texture on the first frame in a world.

### Cited Findings
**Shader side**
- `client/WaterSwell.java:62-85` patches Sodium's `blocks/block_layer_opaque.vsh` and `.fsh` source as it loads. The vertex shader gets `lift()` (lines 106-112): a branch `abs(pos.y - u_SwellSurface) < 0.01`, and for matched vertices one calm-texture fetch plus the 13-wave `swell()`.
- Line 82 adds `v_SwellLight = texture(u_LightTex, _vert_tex_light_coord).rgb;` to every terrain vertex in every pass, unconditionally. It also adds 2 varyings (vec4 + vec3) to all terrain.
- The fragment shader gets `foamMix`/`whitecap` (lines 92-99 and 124-148). It returns early when `at.w < 0.99`. On actual sea surface it runs `swellDetail` over `DETAIL` = 48 waves (each `exp`, `sin`, `cos`), plus 3 `textureLod` reads of the 3D foam texture, per fragment.
- The GLSL is generated by `core/Swell.java:144-162` (`WAVES` = 13 at line 16, `DETAIL` = 48 at line 18).
- The vanilla path is `src/main/resources/assets/minecraft/shaders/core/rendertype_translucent.vsh` (131 lines) and `.fsh` (143 lines), with the same functions.
- No chunk rebuilds: nothing in the swell code marks sections dirty. The calm map is a texture uniform (`client/WaterSwell.java:328-354`), and the height is computed from `u_SwellPhase*` uniforms relative to the camera.

**Per frame, CPU (`WaterSwell.prepare`, `client/WaterSwell.java:164-185`, called at `AFTER_SKY`)**
- `Swell.weather`, the `calmTexture` check, and `bindFoam` (an active-texture switch plus bind).
- `phases()` → `Swell.phases` (`core/Swell.java:109-115`: allocates a `float[48]`).
- 3 `new Matrix4f`, 2 `float[]`.
- Uniform sets on the vanilla translucent shader. These run even when Sodium draws the terrain and the vanilla shader is not used for water, and even when `Swell.calm()` is true.

**Per Sodium terrain pass (`WaterSwell.upload`, `client/WaterSwell.java:188-211`)**
- Triggered by `mixin/SodiumShaderInterfaceMixin.java:14-17` at the TAIL of `DefaultShaderInterface.setupState`.
- Sodium calls `setupState()` once in `ShaderChunkRenderer.begin(pass)`, right after binding the program — [Sodium 0.6.13 source](https://raw.githubusercontent.com/CaffeineMC/sodium/mc1.21.1-0.6.13/common/src/main/java/net/caffeinemc/mods/sodium/client/render/chunk/ShaderChunkRenderer.java).
- Per call:
  - `glGetInteger(GL_CURRENT_PROGRAM)` (line 190)
  - 12 `glGetUniformLocation` by name (lines 191-206)
  - recomputes `phases()` (line 195)
  - 3 `Arrays.copyOfRange` allocations (lines 196-198)
  - 2 `float[]` allocations (`calmAt`, `foamAt`)
  - 10 uniform uploads and 2 texture binds (lines 207-210)

**Calm texture (`client/WaterSwell.java:328-354`)**
- Rebuilt when the camera moves ≥ `SIZE/8` = 64 blocks off its centre, or after 600 ticks (line 333, `SIZE` = 512 at line 40).
- The rebuild calls `Sea.calm(level).square(x0, z0, 512)` (line 336) and re-specifies the texture with `glTexImage2D` (line 344) rather than `glTexSubImage2D`.
- `core/SeaCalm.java:99-130` `square()` covers n = 512 + 2 × 33 = 578, so 334,084 `own()` calls. Each does two `HashMap<Long,…>` lookups with a boxed `Long` key (lines 139-142). There are then 4 spreading passes over the int grid and 262,144 `strength()` + `Math.round` calls.
- For a chunk that is not cached or is stale, `own()` reads all 256 columns (lines 143-150) via `bridge/Sea.java:28-42`. Each column costs `hasChunkAt`, `getBlockState`, `getCollisionShape`, then up to 32 `getFluidState` calls down the water column.
- Columns in unloaded chunks are never cached (lines 151-156), so they are rescanned on every rebuild.
- `STALE` = 600 ticks (`core/SeaCalm.java:18`; `stale()` is `now - when > 600`, line 57) and the refresh interval is also 600 ticks. Chunks cached at a refresh are still fresh at the next refresh (age exactly 600) and stale at the one after. So when the camera is still, every loaded chunk in the window is re-read from blocks every 1,200 ticks.
- The window is 578 blocks across, about 36×36 chunks, which is larger than the loaded area at typical render distances.

**Foam texture (`client/WaterSwell.java:214-262`)**
- Generated synchronously the first time `bindFoam()` runs: on the first frame in a world, from `prepare()`.
- 256 × 256 × 16 = 1,048,576 texels. Each texel needs 2 Perlin evaluations (8 hashed corners each) and one 27-cell Worley search (lines 248-251 and 284-325). That is about 45M hash evaluations, plus two `double[1M]` arrays (16 MB) and 4 `Arrays.stream(...).average()` passes (lines 266-267).
- The result is uploaded once as an RG8 3D texture (2 MB VRAM) and never regenerated.

### Inferences
- **Periodic stutter (likely the most visible swell cost):**
  - Cached rebuild (no block reads): about 334k boxed-Long `HashMap` lookups plus grid passes, roughly 10-20 ms on the render thread (estimate). It happens every 30 s and on every 64 blocks of movement. Flying the battle or city camera, or sailing, crosses 64 blocks often.
  - Full re-read (about every 60 s at rest, and for new chunks when moving): at render distance 12, about 160k loaded columns × (1 block state + collision shape + up to 33 fluid reads) over open ocean. That could reach about 100 ms or more (estimate). It runs even in the Nether/End and under Iris shader packs, where the swell is not drawn.
- Candidate fixes, smallest first:
  1. Skip `calmTexture()` when `Swell.calm()`, when Iris is active, or when the level has no sea.
  2. Switch `SeaCalm`'s maps to fastutil `Long2ObjectOpenHashMap` / `Long2LongOpenHashMap` (bundled with Minecraft) to remove 668k boxings per rebuild.
  3. Spread chunk re-reads over frames (time-slice: N chunks per frame), or stagger `STALE` per chunk so they do not all expire together.
  4. Upload with `glTexSubImage2D` into the existing texture.
  5. Longer term: keep a per-chunk calm byte grid updated on chunk load, compute the square on a worker thread, and upload only the changed strip.
- Foam: precompute and ship the 2 MB noise as a resource, or build it on a worker thread (pure math, no GL) and upload when ready. Either removes an estimated 0.2-0.5 s hitch on world join.
- `upload()`:
  - Cache the uniform locations per program id (`Int2ObjectMap`), or look them up once by injecting into the `DefaultShaderInterface` constructor.
  - Avoid `glGetInteger(GL_CURRENT_PROGRAM)` by reading the program handle Sodium just bound.
  - Compute `phases()` once per frame in `prepare()` and reuse the `float[]` slices.
  - With about 3 passes per frame (solid, cutout, translucent) the absolute CPU cost is small, but the `glGet*` calls are driver-sync risks on NVIDIA threaded optimization (see Q3 sources).
- GPU: the 48-wave fragment loop on every sea pixel is the main GPU cost (about 144 transcendental ops per fragment). It matches Physics Mod's design. If low-end GPUs struggle, a cheaper `DETAIL` or computing the slope from the 13-wave `swell()` in the vertex stage are options. The unconditional extra lightmap fetch and 7 varyings on all terrain vertices (line 82) are probably minor; GLSL compilers usually merge identical fetches.

### Gaps
- Whether Sable's sub-level renderer reuses Sodium's `DefaultShaderInterface.setupState` (which would multiply `upload()` per ship) is unclear. The Sable jar (`~/.gradle/caches/.../sable-fg9dTRz9.jar`) has its own `Fancy`/`VanillaSubLevelRenderDispatcher` and a `SodiumWorldRendererMixin`, so it probably uses its own path, but this was not confirmed.
- Whether anything still uses the vanilla `rendertype_translucent` shader under Sodium (Sable sub-level water, for example) was not established. If nothing does, the vanilla uniform work in `prepare()` is wasted every frame.
- No measurement of `square()` or of the foam generation time.

## 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?

### Takeaway
There are only two client mixins, both `@Pseudo` and both on Sodium. One patches shader source at load (zero runtime cost). The other runs once per terrain render pass (see Q4). Neither disables a Sodium, Iris or DH fast path. The other five mixins are server-side; one of them (`CameraView`) indirectly makes the client load and mesh chunks around the strategy camera.

### Cited Findings
- `src/main/resources/rafasconquest.mixins.json` lists the client mixins as `SodiumShaderInterfaceMixin` and `SodiumShaderLoaderMixin`. The common mixins are `ChunkMapAccess`, `ChunkMapCameraMixin`, `CityVegetationMixin`, `TrackedEntityCameraMixin` and `RecruitsArrowCleanerMixin`. `defaultRequire` is 0.
- `mixin/SodiumShaderLoaderMixin.java:15-20`: `@Inject` at RETURN of `ShaderLoader.getShaderSource`, string-patching Sodium shaders (`client/WaterSwell.java:62-85`). This runs only at shader (re)compile time. If Sodium's source changes, the patch logs a warning and leaves the shader unpatched (lines 70-72). Under Iris, Iris supplies its own programs, `u_SwellPhaseA` is not found, and `upload()` returns early after one `glGetInteger` and one `glGetUniformLocation` (`client/WaterSwell.java:190-194`).
- `mixin/SodiumShaderInterfaceMixin.java:14-17`: `@Inject` at TAIL of `DefaultShaderInterface.setupState`, once per terrain pass (Sodium source in Q4). Per-call cost is listed in Q4.
- The vanilla core-shader override `src/main/resources/assets/minecraft/shaders/core/rendertype_translucent.*` is not a mixin, but it replaces a vanilla shader for every user. Any resource pack or mod that also overrides `rendertype_translucent` will win or lose by load order.
- Server-side: `mixin/ChunkMapCameraMixin.java:16-21` and `mixin/TrackedEntityCameraMixin.java:13-18` (new, uncommitted) make the server send chunks and entities around the strategy-view camera (`bridge/CameraView.java:39-62`, `REACH` = 1008 at line 26). On the client this means chunk loads, unloads and Sodium mesh builds as the battle or city camera flies.

### Inferences
- No Sodium fast path is disabled. The terrain shader gains one branch per vertex and an early-out per fragment.
- The only client mixin runtime costs worth fixing are the `glGet*` calls and string lookups in `upload()` (fix in Q4).
- Camera-driven chunk streaming is inherent to the design. If camera flights stutter, the cause is Sodium meshing newly sent chunks, not mod code.

### Gaps
- Sodium version pinning (`sodium_version` in `gradle.properties`) was not checked against the `block_layer_opaque` anchor strings `vec3 position = _vert_position + translation;` and `fragColor = _linearFog(`.

## 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival

### Takeaway
Tick work is small and bounded. The per-entity-per-frame hook and a few packets do avoidable boxing or rebuilding. Two items stand out:
- `BattleScreen.glow()` sets the glowing tag on every selected soldier every tick. That makes vanilla render each selected soldier a second time into the outline buffer and run the full-screen `entity_outline` post pass every frame.
- The base-map download concatenates all received parts on every part, O(parts²), then inflates and decodes on the main thread.

### Cited Findings
- `ClientEvents.onRenderLiving` (`client/ClientEvents.java:129-138`) runs per living entity rendered per frame. It calls `DistantArmyRenderer.hides` (`client/DistantArmyRenderer.java:81-88`) → `DistantArmies.isOurs` (`client/DistantArmies.java:83-93`, `HashSet<Integer>.contains`, which boxes ids > 127) → `feet()` (lines 100-106, one `Vec3` per call).
- `BackBannerLayer.render` (`client/BackBannerLayer.java:28-30`) calls `BackBanners.of` (`client/BackBanners.java:12`, a `HashMap<Integer, ItemStack>`) for every recruit rendered, which boxes again. `MouthLayer` (`client/MouthLayer.java:27-29`) is an int compare.
- `BattleScreen.tick` (`client/BattleScreen.java:129-160`) calls `requestUnits()` every 5 ticks and `glow()` every tick. `glow()` (lines 805-830) builds a new `HashSet<Integer>` and calls `setGlowingTag` on every selected entity.
- Vanilla `LevelRenderer` marks the outline pass when an entity "should appear glowing" (`MC: net/minecraft/client/renderer/LevelRenderer.java:1027-1028`), renders such entities into the outline target as well, and runs `entityEffect.process(...)`, the `shaders/post/entity_outline.json` chain (lines 511 and 1120), every frame.
- `BattleSounds.tick` (`client/BattleSounds.java:153-180`) recomputes sources once a second (line 160, `DistantArmies.now()`, which allocates a list plus a snapshot per formation). `din()` (lines 215 onward) sorts the remaining fighting formations inside a loop, O(F² log F) once a second.
- `ChargeSound.tick` (line 134) runs each tick per playing charge clip and calls `DistantArmies.at` (which allocates one interpolated `FormationSnapshot`) and `speed`. `DinSound.tick` (line 88) every 10 ticks calls `fightingNear` (line 104), a full `DistantArmies.now()` per din clip.
- `PlaceMusicClient.onSelectMusic` (`client/PlaceMusicClient.java:94-107`) does one sky-light read per tick (line 102).
- Packets (all `consumerMainThread`, `net/ConquestNetwork.java:61,81,86,91`):
  - Formation snapshots arrive once a second → `DistantArmies.receive` (`client/DistantArmies.java:35-48`): rebuilds two `HashMap`s and a `HashSet<Integer>` of every soldier id. This is fine.
  - Volleys → `ArrowVolleys.receive` (`client/ArrowVolleys.java:72-94`): `archers.layout()` allocates a full sprite list per volley. Bounded.
  - Units state 4×/s while the battle screen is open → `BattleScreen.receive` (`net/ConquestNetwork.java:778-779`).
  - Politics view → `ClientPolitics.set` (`client/ClientPolitics.java` `set`/`apply`): decodes succession and calls `MapLands.of(map).regroup()` (`politics/MapLands.java:89-91`). Frequency is decided server-side (`politics/PoliticsSync.java:21,26`).
  - Base map parts (256 KB each, `net/ConquestNetwork.java:30`) → `ClientMapCache.accept` (`client/map/ClientMapCache.java:26-50`): on every part it copies all parts received so far into a fresh `ByteArrayOutputStream` (lines 34-40) before finding the first missing one. When complete it inflates and decodes the whole map (line 43, `map/BaseMap.java:269-270`) and applies politics (line 44) on the main thread. This is requested on every login (`client/ClientEvents.java:67-70`).
- `ClientEvents.onAttack` (`client/ClientEvents.java:157-195`) runs per attack-key press and places every formation within 40 blocks. That is fine.

### Inferences
- Glowing: with hundreds of soldiers selected in the battle screen, every selected soldier is drawn twice (normal plus outline) and a full-screen blur post pass runs each frame. That is likely the largest battle-screen cost apart from the figures. The mod already draws its own gold ring under each selected soldier (`client/BattleScreen.java:853-860`), so dropping the glow when the selection is large, or relying only on the rings (batched as suggested in Q1), would remove it. `glow()` itself could also run only when the selection changes.
- Swap `HashSet<Integer>` and `HashMap<Integer, …>` for fastutil `IntOpenHashSet` / `Int2ObjectOpenHashMap` in `DistantArmies.soldiers` and `BackBanners` to remove per-entity-per-frame boxing.
- `ClientMapCache.accept`: keep a received count and concatenate only once all parts are present. Decode on a worker thread and hand the result to the main thread.
- Have `DinSound` share the once-a-second source computation instead of each clip calling `DistantArmies.now()`.

### Gaps
- How often the server sends `PoliticsView`, and how expensive `TitleMap.regroup` is on a full map, were not measured (shared code, server cadence).

## 7. Per-frame allocations in hot paths

### Takeaway
The allocation hot spots are the battle renderers, at tens of thousands of small objects per frame in large battles, and, to a lesser degree, map-screen text and legend rebuilding. The world frame outside battles allocates almost nothing except `WaterSwell`'s small arrays.

### Cited Findings
- Battle (per frame, scaling with soldiers, arrows and ships):
  - `FormationSnapshot.place` lists and `Sprite`s (`battle/FormationSnapshot.java:151-174`; `layout()` 110-136 allocates a full list when any soldier is unknown)
  - `DistantArmies.positions` lambda `double[3]` and `Vec3` per known soldier (`client/DistantArmies.java:109-115`)
  - `DistantArmies.now()` list plus one interpolated snapshot per formation (`client/DistantArmies.java:51-59`), called from render, sounds and ships
  - `lightAt` `BlockPos` per figure and arrow (`client/DistantArmyRenderer.java:291`, `client/ArrowVolleys.java:176`)
  - varargs `float[12]` per quad (`client/DistantArmyRenderer.java:430`)
  - `new SoldierFigure` per 3D figure (`client/SoldierFigure.java:35-37`)
  - about 30-40 `Vec3` per arrow (`client/ArrowVolleys.java:100-168`)
  - `SeaLanes.at` new list plus `Ship` per lane ship (`core/SeaLanes.java:110-123`)
  - `Far` records (`client/DistantShips.java:77-115`)
  - `SableSeen` quaternion, vector and array per sub-level (`client/SableSeen.java:42-46`)
  - `float[12]` per ship box draw (`client/DistantArmyRenderer.java:188`)
- World, every frame: `Swell.phases` `float[48]` four times (once in `prepare`, once per Sodium pass), 3 `Matrix4f`, `Arrays.copyOfRange` ×3 per pass, `float[3]` ×2 per call (`client/WaterSwell.java:151-211`, `core/Swell.java:109-115`); DH `float[16]` (`client/DistantHorizonsArmies.java:162`); the `RenderLivingEvent` boxing and `Vec3` per rendered living entity (Q6).
- Screens, every frame:
  - legend entries, lambdas and `Component`s (`client/map/FactionMapScreen.java:146-169`)
  - per-letter `Component.literal` and `substring` (`client/map/LabelLayer.java:105-109,131-135`)
  - road list copy (`client/map/RoadLayer.java:31-32`)
  - `owned()` stream ×2 and `font.split` ×2 (`client/map/GrandStrategyScreen.java:63-66,113-135`)
  - `String.format` (`client/map/FactionMapScreen.java:135`)
  - `new ItemStack` ×2 (`client/BattleScreen.java:499`)
  - `MarkerRenderer` `spec().describe()` string per marker in range (`client/MarkerRenderer.java:27`)

### Inferences
- In a large battle, the per-arrow `Vec3` churn alone is about 3000 × 35 × 32 B ≈ 3.4 MB of garbage per frame (about 200 MB/s at 60 fps; estimate). That adds young-generation GC frequency, and with it pause stutter on a memory-constrained PC.
- Fix order:
  1. ArrowVolleys math in primitives
  2. `place()` via callback
  3. fixed-arity `quad`
  4. fastutil sets for id lookups
  5. reuse the `phases` array
- Screen allocations are small in bytes; the flush count (Q2) matters more there.

### Gaps
- No allocation profile (JFR or Spark allocation mode) was taken.

## 8. Sounds: how many instances are created and updated, and how often?

### Takeaway
Sound use is bounded and cheap: at most 8 charge sources plus 12 din sources, each starting a 10 s clip every 8-9 s, and 2 looping streamed tracks for place music and city ambience. Nothing here is a performance problem.

### Cited Findings
- `client/BattleSounds.java:36-37`: `HOOF_SOURCES` = 8, `DIN_SOURCES` = 12. A new clip per source every 160 + rand(20) ticks (`CLIP_EVERY`/`CLIP_JITTER`), each about 10 s long, so up to about 2 overlapping instances per source and about 40 tickable instances in the worst case (`getSoundManager().play` at lines 166 and 173).
- `src/main/resources/assets/rafasconquest/sounds.json`: `battle.charge` (11 clips) and `battle.din` (6 clips) are not `stream: true`, about 82-100 KB of OGG each (`src/main/resources/assets/rafasconquest/sounds/battle/`). The music and ambient entries are `stream: true` (sounds.json lines 27-34).
- `client/PlaceMusicClient.java:111-127`: at most one music track and one ambient track, refreshed through `SelectMusicEvent` each tick. It guards against starting at slider 0 so streams don't leak.

### Inferences
- The 17 non-streamed battle clips decode to an estimated 15-30 MB of OpenAL PCM buffers, held until a resource reload. That is acceptable. Vanilla decodes non-streamed sounds off the main thread, so first play should not hitch.
- Per-tick cost is a few map lookups plus the `DistantArmies.now()` calls noted in Q6.

### Gaps
- Whether the battle clips are mono was not checked. Stereo clips are not positioned by OpenAL, which affects how the sound carries, not performance.

## 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound

### Takeaway
No unbounded client cache was found. Every map texture is released on close, the occlusion depth texture is reallocated only on resize, and the swell's two GL textures are single instances. The memory items worth noting are churn rather than leaks: map-texture rebuild on every open, the Tesselator's grown native buffer, and garbage from the battle renderers.

### Cited Findings
- `client/map/MapContext.java:52-54,86-90,98-102`: 3 `DynamicTexture`s (up to 1024² RGBA = 4 MB each, native plus VRAM), released in `close()` from `FactionMapScreen.removed()` (`client/map/FactionMapScreen.java:219-226`). Re-registering closes the old one (`MC: .../TextureManager.java:62-74`).
- `client/DistantOcclusion.java:207-218`: one `DEPTH_COMPONENT32F` texture at window size (about 14.7 MB at 2560×1440), old id released on resize. Framebuffer and sampler are created once and never deleted (bounded).
- `client/WaterSwell.java:52-53,214-234,328-354`: one R8 512² calm texture (256 KB) and one RG8 256²×16 foam texture (2 MB), never deleted (bounded; they persist across worlds).
- `core/SeaCalm.java:33-35,46-54`: per-level chunk cache swept of entries older than 600 ticks. `bridge/Sea.java:18` `WeakHashMap<Level, SeaCalm>` is collected with the level.
- `client/DistantArmyRenderer.java:201` `HULLS` `IdentityHashMap`, never cleared. It is keyed by `ShipHulls` lists, which are bounded: `READ` is a `WeakHashMap` by template, `PLAIN` is an `EnumMap`, and `BATTLE` has at most 4 sizes × 2 × 16 × 16 kinds (`style/ShipHulls.java:33-34,89-97`). Each style-file edit creates a new template list (`style/StyleLibrary.java:441-462`), so `HULLS` grows slowly during style editing only.
- `client/ArrowVolleys.java:39` caps arrows at 3000. `client/BackBanners.java:12` is cleared on entity leave and logout (`client/ClientEvents.java:72-81,197-202`). `client/DistantArmies.java` is replaced on each snapshot and cleared on logout.
- The Tesselator's native buffer grows by reallocation (2 MB steps) to fit the largest frame and never shrinks (`MC: com/mojang/blaze3d/vertex/ByteBufferBuilder.java:16,41-53`). A big battle can leave it at several MB.
- `client/Portraits.java` creates `RECRUIT` model entities per screen init (`Portraits.model`). They are never added to the level, so they are garbage after the screen closes.

### Inferences
- No leak fix is needed. Keeping `MapContext` across opens (Q2) trades about 12 MB of resident textures for removing the rebuild hitch, which is a reasonable trade on this PC.
- The bigger memory-pressure contributor is the per-frame garbage in battles (Q7).

### Gaps
- No heap or VRAM measurement in game.

## 10. Overall ranking, and areas checked that look fine

### Takeaway
Ranked by likely impact on frame time or stutter. Each item has a concrete fix, and most fixes are small.
1. Sea-lane ship 3D level of detail does disk I/O (directory listing plus stat) per ship per frame (Q1).
2. Calm-texture rebuild on the render thread: a periodic full block re-read about every 60 s near the sea, and a boxed-map rebuild every 30 s or 64 blocks (Q4).
3. Map screens: one flush (draw call) per `fill`, letter and item icon, so hundreds to thousands per frame (Q2).
4. Battle figures, arrows and rings: immediate mode, no frustum culling, per-figure light lookups, heavy allocation, one draw per selection ring (Q1/Q7).
5. Glowing selection in the battle screen: entities drawn twice plus a full-screen outline post pass (Q6).
6. Map rebuild on every open; CourtView re-tessellating about 1,200 blocks per frame (Q2).
7. One-time foam-noise generation hitch on world join (Q4).
8. `glGet*` and uniform-name lookups per Sodium pass, and the DH-occlusion framebuffer re-attach, `glGetInteger` and full-screen merge (Q3/Q4).

### Cited Findings
Areas checked that look fine:
- Swell displacement is GPU-only and causes no chunk rebuilds (`client/WaterSwell.java:62-112`).
- `DistantOcclusion` is skipped when idle and never reads back to the CPU (`client/DistantOcclusion.java:97-122,196-223`).
- Map base, territory and title layers are cached textures (`client/map/MapContext.java:52-54`). Pan and zoom only move `MapView` (`client/map/MapView.java:300-312`).
- `RoadLayer` batches its draws (`client/map/RoadLayer.java:33-63`).
- `BattleSounds` and `PlaceMusicClient` instance counts are bounded (`client/BattleSounds.java:36-37`, `client/PlaceMusicClient.java:111-127`).
- `ClientBarks` draws only when a line is queued (`client/ClientBarks.java:46-52`).
- `MouthLayer` returns early for everyone but the speaker (`client/MouthLayer.java:29`). `MarkerRenderer` and `GateRenderer` return early outside 24 or 32 blocks (or outside creative for gates) (`client/MarkerRenderer.java:21-26`, `client/GateRenderer.java:21-26`).
- Hull face extraction is cached (`client/DistantArmyRenderer.java:184,201`).
- DH quality overrides are set only on world join and leave (`client/DistantHorizonsQuality.java:90-125`).
- Formation snapshot packets arrive once a second (`client/DistantArmies.java:24,35-48`).
- `DistantArmyRenderer.prepare` and `DistantHorizonsLink.fadeStart` are a few config reads per frame (`client/DistantArmyRenderer.java:74-78`, `client/DistantHorizonsArmies.java:205-216`).

Minor items:
- `GateRenderer.getRenderBoundingBox` returns `AABB.INFINITE` (`client/GateRenderer.java:39-40`), so every loaded gate's block-entity renderer runs every frame and is never culled. Each call is just a distance check that exits early; returning the door-box bounds would let it be culled.
- `MarkerRenderer` builds `describe()` per frame per marker in range (`client/MarkerRenderer.java:27`).

### Inferences
- If only three changes are made, they should be:
  1. Memoize `ShipHulls.of` per lane ship, or cache `StyleLibrary.files`.
  2. Batch map fills the RoadLayer way, and drop the per-letter flush.
  3. Frustum-cull formations and arrows, compute light per formation, and time-slice the `SeaCalm` refresh with fastutil maps.
- All three are small diffs in existing files and need no new abstractions.

### Gaps
- Nothing here was measured in game. The ranking is by code inspection plus the estimates above. A Spark profile during (a) a large battle with sea lanes in view, (b) standing at a coast for 2 minutes, and (c) the map screen zoomed in would confirm or reorder it.
