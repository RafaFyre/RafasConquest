# RafasConquest optimization plan (18 November 2026)

This plan follows on from [the 10 November gameplan](RafasConquest%20performance%20gameplan.md). Since then 125 commits have gone in: bodies in every land battle, grand strategy in 3D, sea battles with Sable ships, gunships, cloth, client-only townsfolk and far engines. Many of the old fixes landed along the way. This plan says what is done, what is still open, what the new systems cost, and the order to do the rest **without changing how the game plays**.

Nothing here has been profiled yet. The profiling run sheet's results table is still empty. Stage 0 fixes that before any code changes.

---

## 1. How we avoid breaking anything

1. **Measure before and after each stage.** Use the same scenario, the same world copy, and spark plus the battle report's own timings. A change only counts if it beats the run-to-run spread.
2. **One stage, one jar, one in-game check.** Build, swap the jar (only with javaw closed), and wait for your check before the next stage. Never stack untested stages: that is how the naval rewrite went wrong.
3. **Every old jar is kept**, so rolling back is a jar swap.
4. **Stages 1–3 change speed only, never behaviour.** Anything that changes what a player sees or how a battle comes out (live caps, LOD distances, decision timing) waits for your OK and goes in stage 4.
5. **Tests gate every build.** `gradlew test` runs the 72 test classes. `BodiesTest`, `FieldBattleBodiesTest`, `FigureMotionTest`, `FormationSnapshotTest`, `ClientTownsfolkTest` and `DistantShipsTest` cover most of what this plan touches. A speed change must give the same numbers in those tests as before.
6. **Thread rules.** Worker threads never touch the world, never use `Level.random`, and hand results back to the server thread. Thread counts stay small: the PC's CPU throws hardware errors (WHEA 0x124) under full load on every core.
7. **Keep Java processes few** on the 16 GB PC: no `runServer` while the client runs, and no `gradlew --stop` while it runs.

### Core-function check after every stage (about 10 minutes in game)

| # | Check | What "still works" means |
|---|---|---|
| 1 | Fly into new land for 1 minute | Cities, castles, farms and roads still generate; no new "Can't keep up" lines |
| 2 | Leave the world and rejoin, twice | No crash; F3 memory after the second join is close to the first |
| 3 | Small land battle (2 armies, a few hundred soldiers) | Real soldiers near you, figures far off, handover at the same distance, blocks fight and win or lose as before |
| 4 | Siege of a castle | Survey, deployment on the walls, gate, breach |
| 5 | Sea battle, 2–3 ships | Ships don't blink; no "built twice" warnings; boarding works |
| 6 | Stand at a port | Merchant ships sail the lanes; the swell and foam look the same |
| 7 | Open the Faction map, Grand Strategy 3D and the court | Same picture, same labels, and it pans and zooms |
| 8 | `/time add` past a day, then past a year | The chronicle gets new entries; wars and marriages still happen |
| 9 | Walk a city street | Townsfolk walk and ambient sounds play |
| 10 | Save, quit and reload | Politics, peerage and an ongoing battle come back |

---

## 2. Where the 10 November gameplan stands

**Done since then** (leave alone):
- Spawns paced at 8 per tick (`SPAWNS_PER_TICK`, [RecruitsBattle.java:151](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L151))
- Frustum culling for figures, arrows, townsfolk and grapple hooks
- Light looked up once per formation
- Lane-ship hulls cached by ship id on the client ([DistantShips.java:82](../src/main/java/rafasconquest/client/DistantShips.java#L82))
- Calm texture read on a worker, not the render thread
- `Sea.calms` leak fixed (cf4df5f)
- Succession sent only when it changes (5da6aad)
- `predict()` off the server thread
- Glow capped on big selections
- Expired arrows dropped with `removeIf`
- Sieges load chunks by tickets (f5ae1d7)
- Sea-lane and battle ships force-loaded, so no more duplicate ships
- DH raycast off the render thread; camps pitched over several ticks

**Still open** (carried into the stages below):
- Unbudgeted queue drains
- POI regions counted, not timed
- Folder listing on every style pick
- City plans computed inside `computeIfAbsent`
- The place-music packet sent every second
- Static maps not cleared on server stop
- Map layers drawing one rectangle per draw call
- The map rebuilt on every open
- The court hall re-tessellated every frame
- Battle snapshots sent as doubles to every player in the level
- Recruits pathfinding on 1 thread
- The all-entity arrow scan
- The camera ticket radius
- Everything decided in the single day-turn tick

---

## 3. New costs since 10 November

| System | What it costs | Where |
|---|---|---|
| **Bodies in every land battle** | 46k soldiers: about 4 ms/tick after the parallel `touch`. That parallel step runs on Java's shared pool, which other mods and worldgen use too, and it fires on every core 20 times a second. The sequential parts still left: steer ~0.7 ms, embody ~0.8 ms, grid ~0.4 ms, settle/strike ~0.5 ms | [Bodies.java:153](../src/main/java/rafasconquest/battle/Bodies.java#L153), [FieldBattle.java:3757](../src/main/java/rafasconquest/battle/FieldBattle.java#L3757) |
| **`embody` every tick** | Recomputes every soldier's goal and allocates a `Point` per soldier per tick, though a block's place and facing mostly change once a second | [FieldBattle.java:3777](../src/main/java/rafasconquest/battle/FieldBattle.java#L3777) |
| **`FigureMotion.step` every frame** | Runs for **every** formation, including ones off screen, because culling happens after it. Each frame it allocates a moved snapshot per formation, two `Sprite` lists, a `Placed` for each figure and a `double[]` for each thrown figure. At 40k figures that is over 100k objects per frame | [DistantArmyRenderer.java:103](../src/main/java/rafasconquest/client/DistantArmyRenderer.java#L103), [FigureMotion.java:137](../src/main/java/rafasconquest/client/FigureMotion.java#L137) |
| **Slot matching on a turn** | `deal` builds every figure-to-slot pair: 40k `long[]` arrays for a 200-soldier unit, then sorts them. This happens each time a block turns more than 3° | [FigureMotion.java:362](../src/main/java/rafasconquest/client/FigureMotion.java#L362) |
| **Swell uniforms** | Five `glGetUniformLocation` name lookups per terrain pass, every frame | [WaterSwell.java:307](../src/main/java/rafasconquest/client/WaterSwell.java#L307-L316) |

The old gameplan's main conclusion still holds. With real soldiers, the tick goes to Recruits, vanilla and Sable ticking entities, not to the mod's own maths. What is new is that the bodies make the mod's own battle maths matter again at 40k+ soldiers.

---

## 4. The stages

### Stage 0: measure (one jar, no behaviour change)

1. **Name the mod's systems in the profiler.** Wrap each tick hook in `ConquestEvents` ([ConquestEvents.java:121](../src/main/java/rafasconquest/ConquestEvents.java#L121-L158)) and each draw call in `ClientEvents.onRenderLevel` in `profiler.push("rafasconquest:<name>")`/`pop()`. Each one then shows up by name in F3+L and in spark. That is about 30 lines.
2. **Extend the battle report.** It already times `field`, `switch` and `snapshot`. Add `sail`, `works`, `council`, `trample`, `fadeSome` and `pitchTents` the same way ([RecruitsBattle.java:2240](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L2240)).
3. **Fill in the run sheet** in the Reterra instance (it was written for the retired Neo instance). Add three scenarios to the six it already has:
   - a 40k-soldier land battle seen from afar (bodies plus far figures)
   - a sea battle with gunships
   - Grand Strategy 3D open for 60 s
4. **JFR allocation run** on the big battle (`/jfr start`). That confirms or rules out the `FigureMotion` garbage before stage 3.

**Exit:** baseline median, p95 and max for every scenario, saved with the commit hash.

### Stage 1: small, same-behaviour fixes (2 jars: server, then client)

| # | Fix | File | Risk | How we know it's the same |
|---|---|---|---|---|
| 1.1 | Recruits pathfinding threads 1 → 4 (config only) | instance `recruits-server.toml` line 434 | none | Battle check #3 |
| 1.2 | Bodies' parallel `touch` on its own small pool (4 threads) instead of Java's shared one | [Bodies.java:153](../src/main/java/rafasconquest/battle/Bodies.java#L153) | low: `touch(i)` writes only `i` | `BodiesTest` and `FieldBattleBodiesTest` give the same casualties |
| 1.3 | `embody` skips a block whose place, facing and living count haven't changed since last tick. When it does run, it works out the slot without allocating a `Point` | [FieldBattle.java:3777](../src/main/java/rafasconquest/battle/FieldBattle.java#L3777) | low. First confirm that `slotRight`/`slotBack` depend only on those three things | Same test numbers; battle report `field` ms drops |
| 1.4 | Place music sent only when a player's (music, in-city) value changes | [PlaceMusic.java:47](../src/main/java/rafasconquest/PlaceMusic.java#L47) | none. Send once on login so a rejoin still gets it | Check #9 (ambient sounds) |
| 1.5 | Clear the static maps on server stop: `BattleShips.ships`, `SableShips.live`, `CameraView` cameras and settling, `Wars.mustered`, `Barks.EARS`, `Townsfolk.sent` | [ConquestEvents.java:160](../src/main/java/rafasconquest/ConquestEvents.java#L160) | none. Each one is rebuilt on its next use | Check #2 (rejoin twice) |
| 1.6 | `StyleLibrary.choose` caches each folder's listing until styles reload. Today every structure pick lists the folder on disk, on worldgen and DH threads | [StyleLibrary.java:402](../src/main/java/rafasconquest/style/StyleLibrary.java#L402) | low. Clear the cache wherever styles reload | Check #1 (same cities with the same seed) |
| 1.7 | Swell uniform locations looked up once per shader program, not by name every pass | [WaterSwell.java:307](../src/main/java/rafasconquest/client/WaterSwell.java#L307-L316) | low. Look them up again if the program id changes (Sodium reload, F3+T) | Check #6 |
| 1.8 | Base-map parts joined once, when the last part arrives, instead of re-joining all of them on every part | [ClientMapCache.java:34](../src/main/java/rafasconquest/client/map/ClientMapCache.java#L34) | none | Check #7 |

**Exit:** no new spark spikes; battle report `field` ms lower; check list passes.

### Stage 2: flatten server spikes (2–3 jars)

Old rule 1 still applies: **no unbudgeted work on the server thread.** The smallest version needs no new job-queue class. Set one `deadline = start + 2 ms` per tick and have every drain loop stop when it passes:

| # | Fix | File | Risk |
|---|---|---|---|
| 2.1 | Banner and structure-link queues stop at the deadline; the rest waits for the next tick | [ConquestServer.java:188](../src/main/java/rafasconquest/sim/ConquestServer.java#L188), [:206](../src/main/java/rafasconquest/sim/ConquestServer.java#L206) | low: items wait a tick longer |
| 2.2 | POI regions timed instead of `REGIONS_PER_TICK` counted (37–46 ms each in the old logs) | [ConquestServer.java:144](../src/main/java/rafasconquest/sim/ConquestServer.java#L144) | low |
| 2.3 | `BarracksPeople.nobles` loops every person for every chunk loaded. Build "people by province" once per map | [BarracksPeople.java:110](../src/main/java/rafasconquest/sim/BarracksPeople.java#L110) | low: same people placed |
| 2.4 | City plans stored as a future in the map, computed outside `computeIfAbsent`. Today one slow plan (up to 18 s) can block DH threads that only want a different city | [CityGridBuilder.java:101](../src/main/java/rafasconquest/worldgen/CityGridBuilder.java#L101) | low: same plan, same key |
| 2.5 | Arrow cleaner: Recruits' own box was dropped because Sable makes the border box endless. Use a box that covers the loaded area, or only scan while a battle runs, instead of every entity in the level | [RecruitsArrowCleanerMixin.java:28](../src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java#L28) | medium: stuck arrows must still go. **Only if stage 0 shows it costs anything** |
| 2.6 | Siege survey `getChunk` calls: confirm they now only run after `ChunkWait` has the chunks. If so, nothing to do | [SiegeSurvey.java:363](../src/main/java/rafasconquest/bridge/SiegeSurvey.java#L363), [:465](../src/main/java/rafasconquest/bridge/SiegeSurvey.java#L465) | check only |

**Exit:** `/spark tickmonitor --threshold-tick 50` shows no tick over 50 ms at a siege start or while exploring.

### Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)

| # | Fix | File | Risk |
|---|---|---|---|
| 3.1 | `FigureMotion.step` only for formations in view, with a margin. Off-screen ones snap to their place when seen again, as `FORGET_MILLIS` already does. Flags, hiding and strikes on figures must still find theirs | [DistantArmyRenderer.java:103](../src/main/java/rafasconquest/client/DistantArmyRenderer.java#L103) | medium: a figure can't glide in from off screen. Check #3 while turning the camera |
| 3.2 | `FigureMotion` reuses its arrays: keep the placed result in each figure's `State`, and stop allocating a `Placed` and a `double[]` per figure per frame | [FigureMotion.java:137](../src/main/java/rafasconquest/client/FigureMotion.java#L137) | low; `FigureMotionTest` |
| 3.3 | `deal` without the 40k-pair list: a pair key in one `long[]`, sorted with `Arrays.sort` | [FigureMotion.java:362](../src/main/java/rafasconquest/client/FigureMotion.java#L362) | low: same matches |
| 3.4 | Map layers (`TitleLayer`, storm wall in `BaseMapLayer`, `LabelLayer`) draw into one buffer, as `RoadLayer` already does | [RoadLayer.java](../src/main/java/rafasconquest/client/map/RoadLayer.java) | low: same picture |
| 3.5 | Keep the `MapContext` between Faction-map opens; rebuild only when politics change. `StrategyRelief` already keeps one | [FactionMapScreen.java:133](../src/main/java/rafasconquest/client/map/FactionMapScreen.java#L133) | low. It must rebuild after a war or a title change |
| 3.6 | Court hall baked once into a `VertexBuffer` while the screen is open | [CourtView.java](../src/main/java/rafasconquest/client/CourtView.java) | low |
| 3.7 | Battle snapshot: positions as floats relative to the battle's centre instead of doubles, and sent only to players within the battle's draw range rather than everyone in the level | [ConquestNetwork.java:1272](../src/main/java/rafasconquest/net/ConquestNetwork.java#L1272), [RecruitsBattle.java:2282](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L2282) | low: same jar on both sides; `FormationSnapshotTest` |

**Exit:** frame-time p99 lower in the battle and map scenarios.

### Stage 4: changes that need your design call

Each of these changes what you see or how the game plays, so each one waits for your yes:

1. **Live and LOD distances.** `LIVE_WITHIN 96`, `NUMBERS_BEYOND 128` and the 300 live cap ([RecruitsBattle.java:137](../src/main/java/rafasconquest/bridge/RecruitsBattle.java#L137-L143)). This is still the biggest lever on battle cost, because entity ticking is most of the tick.
2. **Spread the day turn.** Today every nation decides in the same tick ([Politics.java:268](../src/main/java/rafasconquest/politics/Politics.java#L268)). Giving each nation its own tick of the day keeps the same odds, but nations would draw different random numbers, so an existing world's history would go differently from then on.
3. **Camera tickets.** The strategy camera makes about 23×23 chunks entity-ticking ([CameraView.java:94](../src/main/java/rafasconquest/bridge/CameraView.java#L94)). Loaded-but-not-ticking would be far cheaper, but soldiers standing only in the camera's chunks would freeze, so it needs a test with a battle in view.
4. **Bodies on their own thread.** Run the 20 Hz body step alongside the tick, double-buffered, so the tick only reads the last step's result. This is the step after 1.2/1.3 if 46k soldiers are still slow.
5. **Per-soldier networking** (bodies stage 3) under a per-player byte budget.
6. **Formation layout on the GPU.** Move `FigureMotion`'s slot maths into the figure shader. This is a big job, and only worth it if 3.1–3.3 aren't enough.
7. **Lithium in the instance.** It is an A/B test, not code. It targets the same pathing and collision costs Recruits battles pay.

---

## 5. Order and size

| Stage | Jars | Main win | Exit test |
|---|---|---|---|
| 0 Measure | 1 | Know where the time goes | Baselines saved |
| 1 Small fixes | 2 | Bodies cost, worldgen file I/O, rejoin memory, pathfinding threads | Same test numbers, lower `field` ms |
| 2 Spikes | 2–3 | No more frozen ticks at siege start or while exploring | No tick over 50 ms |
| 3 Frames | 2–3 | Smoother far battles and maps | Lower p99 frame time |
| 4 Design | one at a time | Bigger battles | Your call each time |

**Deliberately left alone:**
- the GPU swell
- figure instancing (approved in game)
- saves (3–7 KB, written off-thread already)
- the uncommitted combat work (`CombatMode`, `Deflects`, the Better Combat mixin), which is not part of this plan
