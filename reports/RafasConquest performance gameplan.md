# Profile once, then flatten RafasConquest's spikes

RafasConquest's gameplan should start from one finding: the mod's own steady-state work is small, and the lag players feel comes from **single-tick spikes on the server and avoidable per-frame waste on the client**. The game logs show the battle code taking **0.2 ms of a 38.6–50.3 ms server tick** with 204–231 real soldiers. Yet a capital siege start fell **4,773 ms (95 ticks) behind**, and battle openings hit worst-case ticks of 347 ms on land and 1,503 ms at sea. The causes are on the server thread: it loads chunks synchronously, spawns up to 160 soldiers in one tick and drains work queues with no time limit. On the client, the audit found that every frame does a directory listing and a file stat for each sea-lane ship. The sea "calm" texture is periodically rebuilt on the render thread from hundreds of thousands of block reads. Map screens issue one draw call per rectangle and per letter, and battle figures are rebuilt in immediate mode every frame with no frustum culling. The plan has four steps:

1. Build a one-JVM measurement harness: spark plus JFR, because spark's accurate engine does not run on Windows.
2. Ship a batch of small fixes, most of which copy patterns the codebase already uses elsewhere.
3. Replace "everything in one tick" with a time-budgeted job queue and staggered schedules.
4. Only then take on structural work such as battle level-of-detail (LOD) tiers and network deltas, within the compatibility rules set by C2ME, Lithium, Sodium and Iris.

Nothing in the audit was profiled. The server spikes are confirmed by logs; the client costs are estimates from reading the code, and the first profile may change their order. The real ceiling on battle size is entity ticking inside Villager Recruits, vanilla and Sable. The mod controls that only through its live-soldier cap, the chunks it keeps ticking and how fast it spawns soldiers.

## One JVM, six scenarios and JFR beat guesswork on Windows

**First split each problem into server-bound or client-bound.** F3+2 shows the frame-time chart beside a stacked tick-time chart. In the tick chart, a growing dark-red band means main tick logic; a growing light-brown band means scheduled tasks queued onto the server thread ([Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)).

**spark** is the first profiler to reach for:
- In singleplayer, `/spark` profiles the integrated server and `/sparkc` profiles the client.
- `/spark tickmonitor --threshold-tick 50` followed by `/spark profiler --only-ticks-over <ms>` isolates spikes that a normal profile averages away ([spark: Finding lag spikes](https://spark.lucko.me/docs/guides/Finding-lag-spikes)).
- The viewer's Sources view builds a separate tree for each mod ([spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)).

The catch: spark's async-profiler engine, which avoids safepoint bias, runs only on Linux and macOS ([spark: Using async-profiler](https://spark.lucko.me/docs/misc/Using-async-profiler)). On this Windows PC spark falls back to the Java sampler, and its `--alloc` mode is probably unavailable (this has not been tested).

**JFR is the necessary second tool.** `/jfr start` has been in the game since 1.18 and records Minecraft's own events: chunk generation per stage, tick time, packets and region-file I/O ([Minecraft Wiki: /jfr](https://minecraft.wiki/w/Commands/jfr)). JDK 21's `jfr view` reads the recording without a GUI, with views such as `hot-methods`, `allocation-by-site` and `gc-pauses` ([inside.java: JFR view command](https://inside.java/2023/05/31/jfr-view-command/)). To attach to a running game from outside, Oracle recommends `jcmd <pid> JFR.start` ([Oracle JDK 21 diagnostic tools](https://docs.oracle.com/en/java/javase/21/troubleshoot/diagnostic-tools.html)).

**Built-in commands give cheap numbers without extra mods:**
- `/tick sprint` runs ticks as fast as possible and reports their timing, which is a clean throughput benchmark ([Minecraft Wiki: /tick](https://minecraft.wiki/w/Commands/tick)).
- `/neoforge track start entity 30`, then `/neoforge track entity`, lists the ten costliest entities with their coordinates ([NeoForge TrackCommand](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/TrackCommand.java)).
- `/neoforge entity list recruits:*` counts soldiers per chunk ([NeoForge EntityCommand](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/EntityCommand.java)).

**Name the mod's own systems in the profiler.** It costs a few lines to wrap each big system in `ProfilerFiller.push("rafasconquest:…")`/`pop()`, so it shows up by name in F3+L output and the F3+1 pie chart ([NeoForge docs: Debug Profiler](https://docs.neoforged.net/docs/1.21.1/misc/debugprofiler)). The candidates are battle, sea lanes, the calm-texture rebuild, the chunk queues, the precompute and daily politics. The mod already has a good in-house instrument: the battle report times the whole tick from `ServerTickEvent.Pre` to the end of `Post` ([RecruitsBattle.java:1783](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L1783-L1823)). Extending that breakdown is cheaper than adding a profiling library.

**Keep to one game JVM.** The PC's memory is tight, so profile in the CurseForge instance, which is a single JVM, not `runClient`, which is a Gradle daemon plus a forked client. Changing JVM args spawns another daemon, and an unconfigured daemon takes up to 512 MB ([Gradle: The Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)). Let the daemon expire, or stop it before launching the instance, and never while a client is running. Analyse JFR files and heap dumps after the game is closed.

**JVM settings:**
- Keep G1 with a fixed heap.
- Try Generational ZGC only as an A/B test judged on stutter. On JDK 21 it needs `-XX:+UseZGC -XX:+ZGenerational` ([JEP 439](https://openjdk.org/jeps/439)), and community tests put its memory overhead at about 10–20% ([Space-node](https://space-node.net/blog/zgc-vs-g1gc-minecraft-server-2026)). That overhead could push this PC into paging, which is worse than any GC pause.
- Add `-Xlog:gc*` during performance sessions so GC pauses can be lined up with spark spikes ([PaperMC: Aikar's flags](https://docs.papermc.io/paper/aikars-flags)).

**GPU passes:** RenderDoc attaches through the launcher with "Capture Child Processes" turned on. Nsight's GPU Trace gives per-pass timings, but only on NVIDIA ([ShaderLABS: Attaching Graphics Debuggers](https://shaderlabs.org/wiki/Attaching_Graphics_Debuggers_to_Minecraft)).

The harness needs six scenarios. Each one targets suspects named in the audit, so a single profile can confirm or reorder them:

| Scenario | Setup | Primary measurement | Suspects it confirms |
|---|---|---|---|
| Large land battle | 300 live soldiers, sea lanes in view | `/tick sprint`, `--only-ticks-over 60`, frame-time p99 | spawn bursts, battle figures, arrows, lane-ship disk I/O |
| Siege start | armies meet at a capital, player within 400 blocks | `tickmonitor`, JFR | synchronous survey and camp chunk loads |
| Coast | stand 2 minutes, then sail 500 blocks | frame-time max, JFR `allocation-by-site` | calm-texture rebuild |
| Map screens | populated world, Faction and Grand Strategy maps zoomed in | `/sparkc` on the Render thread, Nsight | per-fill flushes, map rebuild on open |
| Exploration | fly into fresh 2048-block regions | `tickmonitor` | POI precompute at about 40 ms per region |
| Day and year turn | advance the clock past both | single-tick spike | nation decisions, marriages, schemes |

Run every comparison the same way:
- Start from a pregenerated, fixed-seed world copied fresh for each run.
- Do at least three runs per build and drop the first 30–60 s of warm-up.
- Report median, p95 and max.
- Count a change only if it is bigger than the run-to-run spread.
- Save the spark link with `--comment "<commit>, <scenario>"` ([spark Command Usage](https://spark.lucko.me/docs/Command-Usage)).

## Battle code costs 0.2 ms; the multi-second freezes come from blocking

**Steady state: the cost is entity ticking, not the mod's maths.** The 5 November logs report "204 real soldiers in 14 blocks … 50.3 ms/tick average … of which battle 0.2 ms/tick". With 231 soldiers the whole tick averaged 38.6 ms and the battle code still took 0.2 ms ([game logs](file:///C:/Users/carat/curseforge/minecraft/Instances/RafasConquest1.21Neo/logs/)). The rest of the tick goes to ticking the entities themselves: Recruits AI, vanilla movement and collision, and Sable. For example, vanilla `LivingEntity.pushEntities()` queries neighbours every tick for every unit, so packed formations pay per neighbour ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)). The mod's levers here are the live cap (`MAX_LIVE_SOLDIERS = 300`, [RecruitsBattle.java:139](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L139)), the chunks it keeps ticking, and the pace of spawns.

**One zero-code lever comes first.** The instance's Recruits config runs async pathfinding on **one thread**. Its own comment advises about n/4 of the logical cores ([recruits-server.toml](file:///C:/Users/carat/curseforge/minecraft/Instances/RafasConquest1.21Neo/config/recruits-server.toml)).

**Spikes: almost all trace back to the server thread blocking on chunks.** In 1.21.1, `ServerChunkCache.getChunk` called on the main thread for a missing chunk waits in `managedBlock` until the chunk is loaded or generated ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)). Paper treats this as a bug in its own pathfinding: it reads only loaded chunks and treats unloaded ones as blocked ([Paper patch](https://ayakael.net/mirrors/papermc/src/commit/38e1465620f2a0e0baf2b6156df72f653be80781/patches/server/Do-not-load-chunks-for-Pathfinding.patch)).

RafasConquest blocks on chunks in several places:
- **The siege survey.** It calls `level.getChunk` for every chunk in a 32-block ring around a fort's walls, and again for each height sample, all inside `Wars.tick` ([SiegeSurvey.java:253](../src/main/java/rafasconquest/bridge/SiegeSurvey.java#L253-L273), [Wars.java:319](../src/main/java/rafasconquest/politics/Wars.java#L319-L327)).
- **Battle camps.** Building camps loads 3×3 chunks per camp synchronously, for up to six camps ([RecruitsBattle.java:5497](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L5497-L5506)).
- **The first soldier of each block.** Its spawn triggers another synchronous 3×3 load.
- **Distant battle blocks.** These read the world generator for heights, up to 40 lookups per snapshot. The mod's own comment says each lookup "costs ~10 ms" when heavy terrain mods are installed ([RecruitsBattle.java:5522](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L5522-L5544), [Terrain.java:201](../src/main/java/rafasconquest/bridge/Terrain.java#L201)).

The log ties this to the user's own game. When the 10,022-versus-5,022 "Siege of Quarormark Woods" began, the server logged "camps pitched in 532 ms" and then "Can't keep up! … 4773ms or 95 ticks behind" ([latest.log, 08 Nov](file:///C:/Users/carat/curseforge/minecraft/Instances/RafasConquest1.21Neo/logs/latest.log)). That same start also runs `predict()`, up to 20 full numeric battles, on the server thread ([RecruitsBattle.java:649](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L649-L688)).

The table ranks the server spikes. Every fix reuses machinery the mod already has.

| Rank | Spike | Evidence | Fix |
|---|---|---|---|
| 1 | Siege survey loads or generates chunks on the server thread | perimeter-scaled `getChunk` calls; a capital is far worse than a castle | Add a chunk ticket and wait for the chunks off-thread, or read unloaded columns from the generator as `Terrain` already does, then resume on the server thread |
| 2 | Battle start: `predict()` plus synchronous camp ground loads | 532 ms for camps; 4,773 ms behind | `predict()` is pure numbers, so move it off-thread; pitch camps over several ticks |
| 3 | `SPAWNS_PER_SECOND = 160`, all spawned in one tick per second | worst ticks of 347 ms (land) and 1,503 ms (sea) ([RecruitsBattle.java:144](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L144)) | Spawn about 8 per tick; preload the ground with tickets a second ahead |
| 4 | The chunk-load queue is drained with no budget, and `BarracksPeople.nobles` loops over every person × every city for each chunk | O(people × cities) per chunk ([ConquestServer.java:204](../src/main/java/rafasconquest/sim/ConquestServer.java#L204-L214), [BarracksPeople.java:103](../src/main/java/rafasconquest/sim/BarracksPeople.java#L103-L163)) | Time budget per tick; build the person-to-city-cells array once per map, as `holdInChunk` already does |
| 5 | POI precompute: one 2048-block region per tick | 60 regions in 2,197–2,754 ms, so 37–46 ms each ([PoiPrecomputer.java:94](../src/main/java/rafasconquest/sim/PoiPrecomputer.java#L94-L120)) | Time budget, or compute the candidates off-thread |
| 6 | Generator height reads for distant battle blocks | up to 40 per snapshot | Take heights from the battle survey's `Terrain`, or from the base map's ground |
| 7 | `SableShips.tick` rebuilds every sea-lane ship in the world each tick | estimated 800–1,200 ships across 67–107 routes ([SableShips.java:62](../src/main/java/rafasconquest/bridge/SableShips.java#L62-L98), [SeaLanes.java:110](../src/main/java/rafasconquest/core/SeaLanes.java#L110-L123)) | Only compute lanes whose bounds come near a player; cache the id strings |
| 8 | `SeaCalm` rescans unloaded chunks on every call | 256 `depth()` calls per call, never cached ([SeaCalm.java:138](../src/main/java/rafasconquest/core/SeaCalm.java#L138-L158)) | Check `hasChunk` once per chunk; cache "unknown" for about 20 ticks |
| 9 | Strategy camera tickets at view distance + 1 make chunks entity-ticking | roughly 23×23 ticking chunks per camera spot, each kept for 10 s ([CameraView.java:56](../src/main/java/rafasconquest/bridge/CameraView.java#L56-L60)) | Use a smaller radius, or level-33 tickets (loaded but not ticking) |
| 10 | Day and year rollover run in one tick | all nations decide together; the yearly `uncover` calls a linear bond scan per nearby pair ([Succession.java:341](../src/main/java/rafasconquest/people/Succession.java#L341-L350), [Realm.java:68](../src/main/java/rafasconquest/people/Realm.java#L68-L75)) | One nation per tick; a `HashMap` for bonds; spatial buckets of people |

Two more server costs come from the queues and the world-generation threads, not the tick loop.

**The day and year turn.** `dayStep` runs the economy, every nation's `decide`, feuds and succession in a single tick, and can catch up three political days at once ([ConquestServer.java:170](../src/main/java/rafasconquest/sim/ConquestServer.java#L170-L184), [Politics.java:249](../src/main/java/rafasconquest/politics/Politics.java#L249-L287)). The daily pass is only a few milliseconds by the audit's estimate. The yearly schemes pass grows with population and could reach hundreds of milliseconds. That is unmeasured, because no log line times it.

**World generation:**
- City terrain plans are computed inside `ConcurrentHashMap.computeIfAbsent`. The logs show a median of 83 ms, a p90 of 436 ms and a maximum of **18,534 ms** ([CityGridBuilder.java:101](../src/main/java/rafasconquest/worldgen/CityGridBuilder.java#L101)). The Javadoc warns that this mapping function should be "short and simple", because other threads updating the same bin may block ([ConcurrentHashMap Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html#computeIfAbsent(K,java.util.function.Function))). Most of these plans run on Distant Horizons' threads, so a stall there stalls DH's LOD generation.
- `StyleLibrary.read` stats the file on every template request. Its get-then-put is not atomic, so two worldgen threads can parse the same large NBT file twice ([StyleLibrary.java:437](../src/main/java/rafasconquest/style/StyleLibrary.java#L437-L466)).

The fix for the plans is to store a future in the map, or use a per-key lock, rather than computing inside the call.

**Watch-list items.** `RecruitsArrowCleanerMixin` swaps Recruits' bounded box query for a scan of every entity in the level ([RecruitsArrowCleanerMixin.java:102](../src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java#L102-L109)). How often Recruits runs it is unconfirmed. The naval `claim()` sorts every enemy ship for each own ship, and `keepShipsApart` checks every pair of ships ([FieldBattle.java:236](../src/main/java/rafasconquest/battle/FieldBattle.java#L236-L262)). Both are fine today and only bite toward the 100,000-soldier limit. The existing `BlockGrid` nearest-neighbour index is the ready-made fix for both.

## Every frame pays for disk listings, per-rectangle flushes and unculled soldiers

**1. Disk I/O for sea-lane ships, every frame.** This is the client's cheapest large win. Each sea-lane ship drawn as a 3D level-of-detail model calls `ShipHulls.of(lane)`. That leads to `StyleLibrary.files()`, which runs `Files.isDirectory` and `Files.list`, then sorts and streams the result, and `read()` then stats the template file. All of this repeats every frame for every lane ship within 2,000 blocks that is not a real Sable ship ([DistantShips.java:52](../src/main/java/rafasconquest/client/DistantShips.java#L52-L55), [ShipHulls.java:47](../src/main/java/rafasconquest/style/ShipHulls.java#L47), [StyleLibrary.java:324](../src/main/java/rafasconquest/style/StyleLibrary.java#L324-L335)). The fix is to memoize the hull per ship id, or cache the folder listing until the style library reloads. A single cache inside `StyleLibrary` would also remove the per-read stat that hits the world-generation threads, so one change fixes both sides.

**2. The sea calm texture.** This is the likeliest periodic stutter near the sea. The 512×512 calm texture is rebuilt on the render thread every 600 ticks and every time the camera moves 64 blocks:
- Each rebuild makes **334,084** `own()` calls, each doing two `HashMap` lookups with a boxed `Long` key ([SeaCalm.java:99](../src/main/java/rafasconquest/core/SeaCalm.java#L99-L130), [WaterSwell.java:328](../src/main/java/rafasconquest/client/WaterSwell.java#L328-L354)).
- The cache's staleness window equals the refresh interval (both 600 ticks), so every other refresh re-reads every loaded column from blocks.
- Columns in unloaded chunks are never cached, so they are rescanned on every rebuild.
- The rebuild runs even in the Nether and the End, and under Iris shader packs, where the swell is not drawn.

The audit estimates 10–20 ms for a cached rebuild and around 100 ms for a full re-read. The fixes, smallest first:
- Skip the rebuild when the sea is calm, when an Iris pack is active, or when the level has no sea.
- Switch to fastutil's `Long2ObjectOpenHashMap`, which already ships with Minecraft.
- Spread chunk re-reads across frames.
- Upload with `glTexSubImage2D` instead of re-specifying the texture.

The wave displacement itself is already done the right way. It runs entirely on the GPU through patched Sodium shaders and never rebuilds chunks, which matches Physics Mod's uniform-driven design ([Complementary Reimagined Ocean Physics diff](https://www.diffchecker.com/24hNvzCu)).

**3. Map screens issue one draw call per rectangle.** In 1.21.1, `GuiGraphics.fill`, `drawString` and `renderItem` each end with `flushIfUnmanaged()`, so every rectangle is its own buffer build, upload and draw ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)). Batched GUI rendering only arrived in 1.21.2 ([NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)). The map layers pay this cost on a large scale:
- `TitleLayer` draws each war site as 40 fills ([TitleLayer.java:98](../src/main/java/rafasconquest/client/map/TitleLayer.java#L98-L105)).
- The storm wall draws up to 360 single-dash fills ([BaseMapLayer.java:26](../src/main/java/rafasconquest/client/map/BaseMapLayer.java#L26-L36)).
- Empire labels flush after every letter ([LabelLayer.java:105](../src/main/java/rafasconquest/client/map/LabelLayer.java#L105-L109)).
- Every farm and city icon renders a full banner item model ([PoiIconLayer.java:67](../src/main/java/rafasconquest/client/map/PoiIconLayer.java#L67)).

The codebase already contains the fix. `RoadLayer` writes every road segment into one `RenderType.gui()` buffer and flushes once ([RoadLayer.java:27](../src/main/java/rafasconquest/client/map/RoadLayer.java#L27-L64)). The same small `rect()` helper should serve `TitleLayer`, the storm wall, the battle-screen portrait disc and `Portraits`.

Two more map-screen costs:
- **The map is rebuilt on every open.** `MapContext` rebuilds the political map and three images of up to 4 MB each on the render thread every time the map opens, then throws them away on close ([FactionMapScreen.java:96](../src/main/java/rafasconquest/client/map/FactionMapScreen.java#L96-L109)). FTB Chunks shows the established pattern: bake on a worker, upload through `Minecraft.execute`, and re-bake only what changed ([FTB Chunks MapRegionTexture](https://raw.githubusercontent.com/FTBTeam/FTB-Chunks/main/common/src/main/java/dev/ftb/mods/ftbchunks/client/map/MapRegionTexture.java)).
- **The court hall is re-tessellated every frame.** `CourtView` re-draws about 1,200 hall blocks every frame, although the hall never changes while the screen is open ([CourtView.java:246](../src/main/java/rafasconquest/client/CourtView.java#L246-L252)). It belongs in a cached `VertexBuffer`.

**4. Battle rendering.** All world drawing runs from one `RenderLevelStageEvent` handler and is rebuilt every frame through the shared `Tesselator` ([DistantArmyRenderer.java:90](../src/main/java/rafasconquest/client/DistantArmyRenderer.java#L90-L132)). It has three costs:
- **No culling.** No client code uses a frustum, although the event exposes `getFrustum()` ([NeoForge RenderLevelStageEvent](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/event/RenderLevelStageEvent.java)).
- **A light lookup per figure.** Every figure does its own lookup, and every quad allocates a varargs `float[12]`.
- **Arrow garbage.** Each of up to 3,000 arrows allocates about 30–40 `Vec3`s per frame, an estimated 3.4 MB of garbage per frame. Expired arrows are removed with `Iterator.remove()` on an `ArrayList`, which is O(n²) when many expire at once ([ArrowVolleys.java:100](../src/main/java/rafasconquest/client/ArrowVolleys.java#L100-L168)).

The battle screen adds two more costs:
- **Selection rings.** It draws one ring, and so one draw call, per selected soldier ([BattleScreen.java:836](../src/main/java/rafasconquest/client/BattleScreen.java#L836-L862)).
- **Glowing.** It sets the glowing tag on every selected soldier every tick ([BattleScreen.java:805](../src/main/java/rafasconquest/client/BattleScreen.java#L805-L830)). Vanilla then renders each of them a second time into the outline target and runs a full-screen outline post pass.

The fix order is:
1. Cull formations and arrows with the frustum, using bounding circles.
2. Look up light once per formation.
3. Give `quad()` a fixed-arity signature instead of varargs.
4. Do the arrow maths in primitives instead of `Vec3`s.
5. Draw all rings from one buffer.
6. Glow only when the selection is small, relying on the rings otherwise.

The established mods follow the same principles. Entity Culling does its visibility tests on spare threads ([EntityCulling](https://github.com/tr7zw/EntityCulling)), and ImmediatelyFast batches immediate-mode draws ([ImmediatelyFast](https://github.com/RaphiMC/ImmediatelyFast)).

**5. Smaller one-off and per-pass costs:**
- **Foam noise on the first frame.** The 256×256×16 foam texture is generated synchronously on the first frame in a world, about 45 million hash evaluations ([WaterSwell.java:214](../src/main/java/rafasconquest/client/WaterSwell.java#L214-L262)). It could ship as a 2 MB resource or be built on a worker thread.
- **Sodium terrain passes.** Each pass runs `glGetInteger` plus 12 `glGetUniformLocation` lookups by name ([WaterSwell.java:188](../src/main/java/rafasconquest/client/WaterSwell.java#L188-L211)).
- **Depth merge.** `DistantOcclusion` queries the bound framebuffer every frame ([DistantOcclusion.java:119](../src/main/java/rafasconquest/client/DistantOcclusion.java#L119)). NVIDIA forum guidance says any `glGet*` call can force a sync point between the app and driver threads under threaded optimization ([NVIDIA developer forums](https://forums.developer.nvidia.com/t/app-runs-only-with-gl-threaded-optimizations-1-whats-it-do/28873)), so cache uniform locations per program and compare against Minecraft's own record of the bound framebuffer instead.
- **Base-map download.** The client concatenates all received parts on every part, then inflates and decodes on the main thread, on every login ([ClientMapCache.java:34](../src/main/java/rafasconquest/client/map/ClientMapCache.java#L34-L40)).

**Already sound, leave alone:**
- the depth merge never reads back to the CPU and is skipped when idle
- the map's base layers are cached textures, and pan and zoom rebuild nothing
- battle sound instances are bounded (8 charge and 12 din sources)

## A tick budget and staggered schedules make the fixes stick

The individual fixes above share three underlying rules. Making them house rules keeps the next feature from bringing the spikes back.

**Rule 1: no unbudgeted work on the server thread.** One mod-owned job queue, drained in `ServerTickEvent.Post` until a `System.nanoTime()` budget of about 1–3 ms is spent, should own all of the following:
- the banner and structure-link chunk queues
- POI precompute
- soldier spawns and camp building
- city-gate fitting
- daily nation decisions

A budget measured in time rather than a fixed count matters because the cost per item varies. One block placement may touch lighting and another may not, since every `LevelChunk.setBlockState` updates four heightmaps and may queue light work whatever flags are passed ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)). Edits aimed at unloaded chunks should wait in a queue keyed by `ChunkPos.toLong()` and be applied once `getChunkNow` returns a chunk, instead of forcing a load.

**Rule 2: stagger, never synchronise.** Vanilla already spreads mob AI by `tickCount + getId()` parity in `Mob#serverAiStep` ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)). Server forks extend the idea with distance-based tick skipping:
- Spigot and Paper use entity activation range ([PaperMC spigot.yml reference](https://docs.papermc.io/paper/reference/spigot-configuration/)).
- Pufferfish uses Dynamic Activation of Brain (DAB) ([Pufferfish docs](https://docs.pufferfish.host/setup/pufferfish-fork-configuration/)).

For RafasConquest this means running per-unit or per-hold checks only when `(gameTime + id) % N == 0`, and giving each hold its own decision tick within the day (`hash(holdId) % ticksPerDay`) instead of having every nation decide at rollover.

**Rule 3: only pure computation goes off-thread, and results come back under the budget.** Rules for worker threads:
- Never touch the world from a worker. `getChunk` from a worker simply parks the thread until the main thread serves it ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)).
- Never use `Level.random` from a worker. C2ME's "unsafe world random access detection" flags it ([C2ME GitHub](https://github.com/RelativityMC/C2ME-fabric)).
- Plans, battle prediction, POI candidates and yearly schemes should take copied inputs and run on one small named pool. They should not use `Util.backgroundExecutor()`, which worldgen and DH already saturate; the logs show 62 "DH Turbo Structure Prefetch" threads alone.
- Check an epoch counter before applying a result, so stale results are dropped.

**Networking.** The same discipline applies in four places:
- **Battle snapshots** go to every player in the level every second ([RecruitsBattle.java:1811](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L1811-L1816)). They carry double-precision coordinates and every standing block, real or not ([ConquestNetwork.java:917](../src/main/java/rafasconquest/net/ConquestNetwork.java#L917-L946)). That is an estimated 38–40 KB per player per second at 750 blocks, and about 0.5 MB/s at the 100,000-soldier limit. Quantised coordinates, deltas, and NeoForge's `sendToPlayersNear` would cut this ([NeoForge 21.1.265 sources](../build/moddev/artifacts/neoforge-21.1.265-sources.jar)).
- **The succession blob** inside `PoliticsView` grows without bound and goes to all players every 5 s during wars ([PoliticsSync.java:18](../src/main/java/rafasconquest/politics/PoliticsSync.java#L18-L68)). It should be versioned and sent only when it changes.
- **The place-music packet** goes once a second per player even when nothing changed ([PlaceMusic.java:47](../src/main/java/rafasconquest/PlaceMusic.java#L47-L54)).
- **Packet size.** NeoForge 21.1 does split payloads over the 1 MiB clientbound cap, but only for NeoForge clients, and at the cost of an extra full encode.

**Memory.** These fixes matter most for the user's leave-and-rejoin testing loop:
- **A leaked `Level` per world joined.** `Sea.calms` is a `WeakHashMap<Level, SeaCalm>` whose value captures its own key in a lambda ([Sea.java:18](../src/main/java/rafasconquest/bridge/Sea.java#L18-L25)). The `WeakHashMap` Javadoc forbids exactly this, so every level ever joined stays in memory ([WeakHashMap Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/WeakHashMap.html)).
- **Static maps not cleared on stop.** `SableShips.live`, `BattleShips.ships`, `SiegeFrames.frames` and `FeudDuel.current` all hold sub-levels or entities after the server stops, because `onServerStopping` clears only the battle, battle map and townsfolk ([ConquestEvents.java:148](../src/main/java/rafasconquest/ConquestEvents.java#L148-L153)).

**Saved data is not a concern.** The `.dat` files are 3–7 KB, and NeoForge 21.1 already writes SavedData off the main thread.

**Battle scaling: a design question for the user.** The steady-state cost is entity ticking, so scaling past 300 live soldiers is a design question, not a code question. The proven model is the "real → LOD → sprite" ladder the naval design already uses: numbers when no player is near, a representative subset of real entities for a player watching from afar, and full entities in the fight. Which units become data is a choice the user must make. Formation-level pathfinding, with one path per wing and slot offsets for each soldier, would cut the number of path searches without changing who fights.

The order of work follows from cost and evidence:

| Phase | Work | Exit test |
|---|---|---|
| 0. Measure | Profiler sections, the six scenarios, baseline spark and JFR runs saved with the commit hash | Baseline median, p95 and max recorded per scenario |
| 1. Small diffs | Hull and file-listing cache; calm-texture skip plus fastutil; `Sea.calms` leak; spawn about 8 per tick; camera ticket level; music packet only on change; Recruits thread count; glow only on selection change | Lane-ship I/O gone from the Render thread; no spawn tick over 50 ms |
| 2. Spike flattening | Budgeted job queue; async survey and camps; `predict()` off-thread; POI budget; heights from `Terrain`; nobles index; futures instead of work in `computeIfAbsent` | `tickmonitor` reports no tick over 50 ms at siege start or while exploring |
| 3. Frame work | `RoadLayer`-style batching on every map layer; keep `MapContext` across opens; frustum culling; light per formation; arrow maths in primitives; one buffer for all rings; `CourtView` in a `VertexBuffer`; foam texture as a resource | Lower frame-time p99 in the map-screen and battle scenarios |
| 4. Structural | Staggered day and year; bond index; grid-based naval claims; snapshot quantization and deltas; `BaseMap` as `short[]`; battle LOD tiers agreed with the user | `/tick sprint` ms/tick at fixed battle sizes |

## C2ME, Moonrise and Iris set the rules for every optimization

Performance mods share three habits: never change vanilla behaviour, make each optimization switchable on its own, and never assume ownership of vanilla internals. Lithium says it optimizes "without changing game mechanics or visuals", lets each optimization be disabled separately, and has shipped native NeoForge 1.21.1 builds since October 2024 ([Lithium GitHub](https://github.com/CaffeineMC/lithium); [Modrinth API: Lithium](https://api.modrinth.com/v2/project/lithium/version?loaders=%5B%22neoforge%22%5D&game_versions=%5B%221.21.1%22%5D)). Its pathing and collision mixins target the same costs that dominate Recruits battles ([Lithium NeoForge mixin config](https://raw.githubusercontent.com/CaffeineMC/lithium/develop/lithium-neoforge-mixin-config.md)). That makes Lithium the first optimization mod to test alongside the pack.

**C2ME** is the actively maintained chunk optimizer for NeoForge 1.21.1, at alpha 0.4.0 in October 2026. Its changelog includes a "bandaid for create aeronautics" ([Modrinth API: C2ME](https://api.modrinth.com/v2/project/c2me-neoforge/version?loaders=%5B%22neoforge%22%5D&game_versions=%5B%221.21.1%22%5D)), which shows that Sable-based content has already tripped its thread checks. RafasConquest's worldgen must therefore use only the `WorldGenRegion` it is given and its own seeded random source.

**Moonrise** offers only unsupported betas for 1.21.1, is incompatible with C2ME, and replaces the chunk system wholesale ([Moonrise GitHub](https://github.com/Tuinity/Moonrise)). That puts the two uncommitted camera mixins at risk. They redirect calls inside `ChunkMap.updateChunkTracking` and `TrackedEntity.updatePlayer` ([ChunkMapCameraMixin.java](../src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java), [TrackedEntityCameraMixin.java](../src/main/java/rafasconquest/mixin/TrackedEntityCameraMixin.java)). They cost nothing while the strategy view is unused, but they should stay minimal and non-required, and be tested with C2ME installed.

**FerriteCore** deduplicates `VoxelShape`s and baked-quad arrays, assuming they are never modified ([FerriteCore summary](https://github.com/malte0811/FerriteCore/blob/main/summary.md)). The mod must never mutate either after creation.

**Sable.** The Sable Optimizer addon treats a ship as inactive once its velocity stays below 0.01 for 400 ticks ([Sable Optimizer](https://www.curseforge.com/minecraft/mc-mods/aeronautics-sable-optimizer)). The mod's swell forces may keep moored ships from ever settling, which would cancel that saving. This needs checking in game.

**On the client:**
- **Sodium.** The swell works by patching Sodium's shader source at fixed anchor strings, so each Sodium update needs a check that the patch still applied. The mod's loader logs a warning and leaves the shader unpatched when an anchor is missing.
- **Iris.** A shader pack owns the depth and colour targets. Every custom effect needs an Iris-off mode: the depth merge already has one, and the calm texture should get one. A Forge forum report shows custom stage rendering going invisible under shader packs ([Forge forum](https://forums.minecraftforge.net/topic/155104-model-and-textures-rendered-on-entity-and-renderlevelstageevent-not-visible-when-using-shaders)).
- **Newer-version advice does not apply.** Tutorials built on `GpuBuffer`, `CompiledShaderProgram`, `EntityRenderState` or `drawSpecial` target 1.21.2 or later and do not work on 1.21.1 ([NeoForge 1.21.2 primer](https://docs.neoforged.net/primer/docs/1.21.2)).

**Make the heavy knobs configurable.** Battle LOD radius, spawn pacing, map sync rate and the calm-refresh interval should be config values, the way ServerCore and Sable Optimizer expose theirs, so servers can tune without a rebuild ([ServerCore GitHub](https://github.com/Wesley1808/ServerCore)).

## Conclusion

RafasConquest does not need clever new algorithms. It needs its existing good patterns applied everywhere. The codebase already has:
- `RoadLayer` batching
- the `BlockGrid` spatial index
- a capped pool for city plans
- an off-thread battle survey in `Terrain`
- GPU-only swell displacement
- worldgen pieces placed with flag 2
- pausing DH generation during battles

Almost every finding is a neighbouring piece of code that skipped one of these. Most of the plan is therefore small diffs in existing files with no new abstractions. The scheduling rule needs only one genuinely new component: a single time-budgeted job queue.

Two conclusions change what "performance work" means here. First, the server's worst moments are now explained by logs, while the client ranking is still inferred. Profile the client before investing in phase 3, and treat the server spike fixes as safe to start now. Second, the mod cannot buy much more battle size by optimizing its own loops, because Recruits, vanilla and Sable entity ticking already take almost all of a tick at 200–300 soldiers. Larger battles depend on a level-of-detail design the user signs off on, not on code tuning. On this Windows PC, every claim about where time goes should be checked in JFR as well as spark, because spark's accurate engine is not available.
